package l1j.server.MJCTSystem;

//import l1j.server.MJTemplate.MJProto.MainServer_Client_Inventory.SC_WAREHOUSE_ITEM_LIST_NOTI;
import l1j.server.server.serverpackets.S_CTPacket;
/** 
 * MJCTCharInfo
 * MJSoft Character TradeSystem - Object
 * made by mjsoft, 2016.
 **/
public class MJCTObject {
	public int			marbleId;
	public int			charId;
	public String		name;
	public S_CTPacket	invPck;
	public S_CTPacket	spPck;
	public S_CTPacket	infoPck;
	
	public void dispose(){
		if(invPck != null)
			invPck.clear();
		
		if(spPck != null)
			spPck.clear();
		
		if(infoPck != null)
			infoPck.clear();
	}
}
