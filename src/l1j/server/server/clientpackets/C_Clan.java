package l1j.server.server.clientpackets;

import server.LineageClient;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Emblem;

public class C_Clan extends ClientBasePacket {
	

	private static final String C_Clan = "[C] C_Clan";

	public C_Clan(byte abyte0[], LineageClient clientthread) {
		super(abyte0);

		int emblemId = readD();
		int numid = readD();

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null) {
			return;
		}
		pc.sendPackets(new S_Emblem(emblemId, numid));
	}

	@Override
	public String getType() {
		return C_Clan;
	}
}