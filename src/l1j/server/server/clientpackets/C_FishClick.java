package l1j.server.server.clientpackets;

import l1j.server.server.TimeController.FishingTimeController;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_FishClick extends ClientBasePacket {
	

	private static final String C_FISHCLICK = "[C] C_FishClick";

	public C_FishClick(byte abyte0[], LineageClient clientthread)
			throws Exception {
		super(abyte0);
		try {
			L1PcInstance pc = clientthread.getActiveChar();
			if (!pc.isFishing())
				return;
			pc.setFishingTime(0);
			pc.setFishingReady(false);
			pc.setFishing(false);
			pc.setFishingItem(null);
			S_CharVisualUpdate cv = new S_CharVisualUpdate(pc);
			pc.sendPackets(cv);
			Broadcaster.broadcastPacket(pc, cv, true);
			FishingTimeController.getInstance().removeMember(pc);
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_FISHCLICK;
	}
}
