package manager.dialog;

import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class PresentDialog extends Dialog {
	protected Shell shell;
	protected Object result;
	public Text text;
	public Text text_1;
	public Text text_2;
	public Text text_3;
	public Label label_2;
	public PresentDialog(Shell parent) {
		super(parent);
		setText("SWT Dialog");
	}
	
	public Object open() {
		Display display = getParent().getDisplay();
		createContents();
		shell.setBounds((display.getBounds().width/2)-(shell.getBounds().width/2), (display.getBounds().height/2)-(shell.getBounds().height/2), shell.getBounds().width, shell.getBounds().height);
		
		shell.open();
		shell.layout();

		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return result;
	}
	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.CLOSE | SWT.TITLE | SWT.PRIMARY_MODAL);
		shell.setSize(228, 125);
		
		text = new Text(shell, SWT.BORDER);
		text.setBounds(63, 10, 152, 21);
		
		Label lblNewLabel = new Label(shell, SWT.NONE);
		lblNewLabel.setBounds(10, 13, 47, 15);
		lblNewLabel.setText("\uCE90\uB9AD\uBA85:");
		
		text_1 = new Text(shell, SWT.BORDER);
		text_1.setBounds(63, 37, 73, 21);
		
		text_2 = new Text(shell, SWT.BORDER);
		text_2.setBounds(170, 37, 45, 21);
		
		Label label = new Label(shell, SWT.NONE);
		label.setText("\uC544\uC774\uD15C:");
		label.setBounds(10, 40, 47, 15);
		
		Label label_1 = new Label(shell, SWT.NONE);
		label_1.setText("\uAC2F\uC218:");
		label_1.setBounds(139, 40, 31, 15);
		
		text_3 = new Text(shell, SWT.BORDER);
		text_3.setBounds(63, 64, 73, 21);
		
		label_2 = new Label(shell, SWT.NONE);
		label_2.setText("\uC778\uCC48\uD2B8:");
		label_2.setBounds(10, 67, 47, 15);
		
		Button btnNewButton = new Button(shell, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			/**선물 보내기*/
			@Override
			public void widgetSelected(SelectionEvent e) {
				try{
				
				MessageBox dialog = new MessageBox(shell,SWT.OK|SWT.CANCEL|SWT.ICON_INFORMATION);
				dialog.setText("Present"); 
				dialog.setMessage("정말 선물을 보내시겠습니까?"); 
				int flag = dialog.open();
				if (flag != SWT.OK) { 
					return;
				}
				
				
				L1PcInstance target = L1World.getInstance().getPlayer(text.getText());
				if(target != null){
					
					int itemid = Integer.parseInt(text_1.getText());
					int count = Integer.parseInt(text_2.getText());
					int enchant = Integer.parseInt(text_3.getText());
					
                    L1Item temp = null; 
        			temp = ItemTable.getInstance().getTemplate(itemid);
                    if (temp.isStackable()) {
    					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
    					item.setEnchantLevel(enchant);
    					item.setCount(count);
    					if (target .getInventory().checkAddItem(item, count) == L1Inventory.OK) {
    						target .getInventory().storeItem(item);
    						//sendPackets(new S_SystemMessage(item.getViewName()+ "(을)를 선물하였습니다."));
    					}
    				} else {
    					L1ItemInstance item = null;
    					int createCount;
    					for (createCount = 0; createCount < count; createCount++) {
    						item = ItemTable.getInstance().createItem(itemid);
    						item.setEnchantLevel(enchant);
    						if (target .getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
    							target .getInventory().storeItem(item);
    						} else {
    							break;
    						}
    					}
    					if (createCount > 0) {
    						//sendPackets(new S_SystemMessage(item.getViewName()+ "(을)를" + createCount + "개를 선물하였습니다."));
    					}
    				}
                    MessageBox dialog2 = new MessageBox(shell, SWT.CANCEL|SWT.ICON_INFORMATION);
    				dialog2.setText("Present"); 
    				dialog2.setMessage("선물을 정상적으로 보냈습니다."); 
    				dialog2.open();
                    
				}else {
					MessageBox dialog2 = new MessageBox(shell, SWT.CANCEL|SWT.ICON_INFORMATION);
					dialog2.setText("Present"); 
					dialog2.setMessage(text.getText()+"님은 월드에 존재하지않습니다."); 
					dialog2.open();
				}
			}catch(Exception e1){
				MessageBox dialog2 = new MessageBox(shell, SWT.CANCEL|SWT.ICON_INFORMATION);
				dialog2.setText("Present"); 
				dialog2.setMessage("값이 잘못되었습니다."); 
				dialog2.open();
			}
			}
		});
		btnNewButton.setBounds(140, 62, 76, 25);
		btnNewButton.setText("\uC120\uBB3C \uBCF4\uB0B4\uAE30");
	}
}
