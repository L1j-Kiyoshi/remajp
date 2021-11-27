package l1j.server.server.clientpackets;

import java.util.ArrayList;

import l1j.server.Warehouse.SupplementaryService;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ClanWindow;
import l1j.server.server.serverpackets.S_RetrieveSupplementaryService;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.LineageClient;

public class C_ClanNotice extends ClientBasePacket {
	

	private static final String C_ClanNotice = "[C] C_ClanNotice";
	private static final int 공지사항 = 0x0f;
	private static final int 메모 = 0x10;
	private static final int 부가서비스 = 0x06;

	public C_ClanNotice(byte abyte0[], LineageClient client) throws Exception {

		super(abyte0);
		try {
			int type = readC();
			L1PcInstance pc = client.getActiveChar();
			if (pc == null)
				return;
			L1Clan clan = null;
			if (type != 부가서비스) {
				if (pc.getClan() == null)
					return;
				clan = pc.getClan();
			}

			switch (type) {
			case 부가서비스: {
				SupplementaryService warehouse = WarehouseManager.getInstance()
						.getSupplementaryService(pc.getAccountName());
				int size = warehouse.getSize();
				if (size > 0)
					pc.sendPackets(new S_RetrieveSupplementaryService(pc
							.getId(), pc));
				else
					pc.sendPackets(new S_ServerMessage(1625), true);
			}
				break;
			case 공지사항: {
				if (!clan.getLeaderName().equalsIgnoreCase(pc.getName()))
					return;
				String Notice = readS();
				if (Notice.length() > 160) {
					pc.sendPackets(new S_SystemMessage(
							"혈맹 공지는 최대 160Byte 이상을 넘을 수 없습니다."));
					return;
				}
				clan.setNotice(Notice);
				ClanTable.getInstance().updateClan(clan);
				for (L1PcInstance targetPc : clan.getOnlineClanMember()) { // 온라인
					S_ClanWindow packet = new S_ClanWindow(pc,
							S_ClanWindow.혈맹공지변경);
					targetPc.sendPackets(packet);
					packet = null;
				}
			}
				break;

			case 메모: {
				String Memo = readS();
				if (Memo.length() > 60) {
					pc.sendPackets(new S_SystemMessage(
							"혈맹 메모는 최대 60Byte 이상을 넘을 수 없습니다."));
					return;
				}
				pc.setMemo(Memo);
				pc.save();
				ArrayList<ClanMember> clanMemberList = clan.getClanMemberList(); // 모든
																					// 혈맹원의
																					// 이름과
																					// 등급
				for (ClanMember member : clanMemberList) { // 전체
					if (member.name.equalsIgnoreCase(pc.getName())) {
						member.memo = Memo;
						break;
					}
				}
				for (L1PcInstance targetPc : clan.getOnlineClanMember()) { // 온라인
					S_ClanWindow packet = new S_ClanWindow(pc,
							S_ClanWindow.혈맹원메모변경);
					targetPc.sendPackets(packet);
					packet = null;
				}
			}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getType() {
		return C_ClanNotice;
	}
}
