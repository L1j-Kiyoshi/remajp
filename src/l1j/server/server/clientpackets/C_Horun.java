package l1j.server.server.clientpackets;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Horun;
import server.LineageClient;

public class C_Horun extends ClientBasePacket {
	private static final String C_HORUN = "[C] C_Horun";

	public C_Horun(byte abyte0[], LineageClient clientthread) throws Exception {
		super(abyte0);
		try {
			int i = readD();

			L1PcInstance pc = clientthread.getActiveChar();

			if (pc == null || pc.isGhost()) {
				return;
			}

			S_Horun h = new S_Horun(i, pc);
			pc.sendPackets(h, true);
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_HORUN;
	}

}
