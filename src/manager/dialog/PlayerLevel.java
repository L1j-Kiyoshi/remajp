package manager.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class PlayerLevel extends Dialog {

	protected Object result;
	protected Shell shell;
	private Text text_1;
	public static Display display;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public PlayerLevel(Shell parent, int style) {
		super(parent, style);
		setText("SWT Dialog");
	}

	/**
	 * Open the dialog.
	 * 
	 * @return the result
	 */
	public Object open(L1PcInstance pc) {
		createContents(pc);
		shell.open();
		shell.layout();
		Display display = getParent().getDisplay();
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
	private void createContents(final L1PcInstance pc) {

		shell = new Shell(getParent(), getStyle());
		shell.setSize(251, 70);
		shell.setText("플레이어 레벨변경");
		// 화면중앙으로
		display = Display.getDefault();
		shell.setBounds((display.getBounds().width / 2) - (shell.getBounds().width / 2), (display.getBounds().height / 2)
				- (shell.getBounds().height / 2), shell.getBounds().width, shell.getBounds().height);
		GridLayout gl_shell = new GridLayout(3, false);
		gl_shell.marginWidth = 10;
		gl_shell.marginHeight = 10;
		gl_shell.horizontalSpacing = 10;
		shell.setLayout(gl_shell);

		Label lblNewLabel_2 = new Label(shell, SWT.NONE);
		lblNewLabel_2.setText("변경할 레벨");

		text_1 = new Text(shell, SWT.BORDER);
		text_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		text_1.setEditable(true);

		Button lblNewButton = new Button(shell, SWT.PUSH);
		GridData gd_lblNewButton = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gd_lblNewButton.widthHint = 65;
		lblNewButton.setLayoutData(gd_lblNewButton);
		lblNewButton.setText("실 행");
		lblNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				pc.setExp(ExpTable.getExpByLevel(Integer.valueOf(text_1.getText())));
				pc.sendPackets(new S_SystemMessage("운영자님이 당신의 레벨을 " + Integer.valueOf(text_1.getText()) + " 으로 변경하였습니다."));
				close();
			}
		});

	}

	private void close() {
		shell.dispose();
	}
}
