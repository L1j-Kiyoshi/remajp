package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import server.LineageClient;


public class C_Macro extends ClientBasePacket {
	

	private static final String C_Macro = "[C] C_Macro";
	public static final int MACRO = 0x0c;
	public C_Macro(byte abyte0[], LineageClient client) {
		super(abyte0);
		if (client == null)
			return;

		readC();
		L1PcInstance pc = client.getActiveChar();

		if (pc == null) 
			return;

		chatWorld(pc, readS());
	}

	private void chatWorld(L1PcInstance pc, String chatText) {
		if (pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
			if (pc.isGm() || L1World.getInstance().isWorldChatElabled()) {
				if (pc.get_food() >= 12) { // 5%°ÙÁö?
					S_PacketBox pb = new S_PacketBox(S_PacketBox.FOOD,pc.get_food());
					pc.sendPackets(pb, true);
/*					if (pc.isGm()) {
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[******] " + chatText));
							return;
						}*/
					}
					for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							S_NewCreateItem cp = new S_NewCreateItem(pc,4,3,chatText,"");   // 13Àå»ç 12  ÀüÃ¼  4 Ç÷¸Í  
							listner.sendPackets(cp, true);
							}
					}//ÄÄÆÎ
				} else { //½ÇÇàÇØ~
					S_ServerMessage sm = new S_ServerMessage(462);
					pc.sendPackets(sm, true);
				}
		} else {
			S_ServerMessage sm = new S_ServerMessage(195,
					String.valueOf(Config.GLOBAL_CHAT_LEVEL));
			pc.sendPackets(sm, true);
		}
	}
	@Override
	public String getType() {
		return C_Macro;
	}
}
