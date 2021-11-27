/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 * Author: ChrisLiu.2007.06.30
 */
package l1j.server.server.clientpackets;

import java.util.List;

import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ClanBlessHuntUi;
import l1j.server.server.serverpackets.S_ClanWindow;
import l1j.server.server.serverpackets.S_ServerMessage;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Pledge extends ClientBasePacket {
	

	private static final String C_PLEDGE = "[C] C_Pledge";

	public C_Pledge(byte abyte0[], LineageClient clientthread) {
		super(abyte0);
		try {
			L1PcInstance pc = clientthread.getActiveChar();

			if (pc.getClanid() > 0) {
				String s = "45 01 08 02 "
						+ "12 27 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 "
						+
						// + "b6 ea d9 02 "
						/*
						 * + "c5 8a 5d 02 "//objid + "22 " +
						 * "06 b3 d7 b8 a3 c0 cc " //+ "04 b0 a3 b4 d9 " + "2a "
						 * + "06 31 32 33 31 33 33 " + "30 " + "00 " //접속중 +
						 * "38 " + "03 "//클래스?
						 */

						"be d1 d8 02 "
						+ "22 "
						+ "06 b6 f3 b3 d7 b8 a3 "
						+ "2a "
						+ "06 6a 6f 69 6e 31 31 "
						+ "30 "
						+ "00 "
						+ "38 "
						+ "05 "
						+

						"12 26 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 "
						+

						"c5 8a 5d "// objid
						+ "22 "
						+ "06 b3 d7 b8 a3 c0 cc "
						+ "2a "
						+ "06 31 32 33 31 33 33 "
						+ "30 "
						+ "00 " // 접속중
						+ "38 "
						+ "03 "
						+ // 클래스?

						"12 28 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 " +

						"c5 8a 5f " + "22 " + "07 c5 d7 c6 e4 c1 ee 77 "
						+ "2a " + "05 6a 6f 69 6e 32 " + "30 " + "01 " + "38 "
						+ "05 " +

						"12 27 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 " +

						"c5 8a 5a 02 " + "22 " + "07 c5 d7 c6 e4 c1 ef 77 "
						+ "2a " + "05 6a 6f 69 6e 32 " + "30 " + "01 " + "38 "
						+ "05 " +

						"00 30";
				String ss = "45 01 08 02 "
						+ "12 27 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 " +

						"be d1 d8 02 " + "22 " + "06 b6 f3 b3 d7 b8 a3 "
						+ "2a " + "06 6a 6f 69 6e 31 31 " + "30 " + "00 "
						+ "38 " + "05 " +

						"12 26 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 " +

						"c5 8a 5c " + "22 " + "07 c5 d7 c6 e4 c1 ee 77 "
						+ "2a " + "05 6a 6f 69 6e 32 " + "30 " + "01 " + "38 "
						+ "05 " +

						"00 30";

				String sss = "45 01 08 02 " +

				"12 26 08 a2 a9 03 12 08 b2 c3 c5 eb c5 ac b7 b4 18 "
						+ "c5 8a 5c 22 "
						+ "07 c5 d7 c6 e4 c1 ee 77 2a 05 6a 6f 69 "
						+ "6e 32 30 00 38 05 " +

						/*
						 * "12 26 08 a2 a9 03 12 08 b2 c3 c5 eb c5 ac b7 b4 18 c5 "+
						 * "8a 5c 22 07 c5 d7 c6 e4 c1 ee 77 2a 05 6a 6f 69 "+
						 * "6e 32 30 00 38 05 "+
						 */
						"12 27 08 a2 a9 03 12 08 b2 c3 c5 eb c5 ac b7 b4 18 "
						+ "be d1 d8 02 22 " + "06 b6 f3 b3 d7 "
						+ "b8 a3 2a 06 6a 6f 69 6e 31 31 30 00 38 05 " +

						"12 28 08 a2 a9 03 12 08 b2 c3 "
						+ "c5 eb c5 ac b7 b4 18 c3 8a 5c 22 09 b7 b9 c0 cc "
						+ "b8 c6 bd ba 6f 2a 05 6a 6f 69 6e 33 30 00 38 01 " +

						"12 27 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 be d1 d8 02 22 06 b6 f3 b3 d7 "
						+ "b8 a3 2a 06 6a 6f 69 6e 31 31 30 01 38 05 "
						+ "12 27 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 be d1 d8 02 22 06 b6 f3 b3 d7 "
						+ "b8 a3 2a 06 6a 6f 69 6e 31 31 30 00 38 05 "
						+ "12 27 08 a2 a9 03 12 08 b2 c3 c5 "
						+ "eb c5 ac b7 b4 18 be d1 d8 02 22 06 b6 f3 b3 d7 "
						+ "b8 a3 2a 06 6a 6f 69 6e 31 31 30 00 38 05 " +

						/*
						 * "12 26 08 a2 a9 03 12 "+
						 * "08 b2 c3 c5 eb c5 ac b7 b4 18 d6 e1 df 02 22 06 "+
						 * "47 6f b7 e7 b7 e7 2a 05 6a 6f 69 6e 35 30 00 38 "+
						 * "03 "+ /*
						 * "12 27 08 a2 a9 03 12 08 b2 c3 c5 eb c5 ac b7 b4 "+
						 * "18 b9 b9 e0 02 22 07 c4 c9 c4 c9 c4 c9 61 2a 05 "+
						 * "6a 6f 69 6e 34 30 00 38 01 "+
						 * 
						 * "12 29 08 a2 a9 03 12 08 b2 c3 c5 eb c5 ac b7 "+
						 * "b4 18 b9 8a 5c 22 0a c4 ab c0 cc b4 cf bd ba 71 "+
						 * "77 2a 05 6a 6f 69 6e 36 30 00 38 05 "+//00 00 ";
						 * 
						 * "12 27 08 a2 a9 03 12 08 b2 c3 c5 eb c5 ac b7 b4 "+
						 * "18 b9 b9 e0 02 22 07 c4 c9 c4 c9 c4 c9 61 2a 05 "+
						 * "6a 6f 69 6e 34 30 00 38 01 "+
						 * 
						 * "12 27 08 a2 a9 03 12 08 b2 c3 c5 eb c5 ac b7 b4 "+
						 * "18 b9 b9 e0 02 22 07 c4 c9 c4 c9 c4 c9 61 2a 05 "+
						 * "6a 6f 69 6e 34 30 00 38 01 "+
						 * 
						 * "12 27 08 a2 a9 03 12 08 b2 c3 c5 eb c5 ac b7 b4 "+
						 * "18 b9 b9 e0 02 22 07 c4 c9 c4 c9 c4 c9 61 2a 05 "+
						 * "6a 6f 69 6e 34 30 00 38 01 "+
						 */
						"00 00";
				/*
				 * C -> S 0000: 08 48 01 08 00 08 01 12 04 b0 a3 b4 d9 00 00 00
				 * .H.............. C -> S 0000: 08 48 01 08 00 08 02 12 04 b0
				 * a3 b4 d9 43 98 5b .H...........C.[
				 */
				// pc.sendPackets(new
				// S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_WAIT), true);
				// pc.sendPackets(new S_NewCreateItem(sss));
				pc.sendPackets(new S_ClanWindow(pc, S_ClanWindow.혈맹공지및정보), true);
				// pc.sendPackets(new S_ClanWindow(pc, S_ClanWindow.혈맹원목록),
				// true); // 여기서 연합셋팅됨
				// pc.sendPackets(new S_ClanWindow(null, 1, 0));

				int size = pc.getClan().getClanMemberList().size();
				int i = size / 90;
				for (int a = 0; a <= i; a++) {
					List<ClanMember> list = pc.getClan().getClanMemberList()
							.subList(a * 90, a == i ? size : (a + 1) * 90);
					pc.sendPackets(new S_ClanWindow(list, i + 1, a));
				}

				// pc.sendPackets(new
				// S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_WAIT));
				pc.sendPackets(new S_ClanWindow(pc, S_ClanWindow.접속유저), true);
				
				if(pc.getClan().isCaveOpen() && pc.getClan().getBlessHuntMaps() != null){
					pc.sendPackets(new S_ClanBlessHuntUi(S_ClanBlessHuntUi.CLAN_BLESS_HUNT_TELEPORT, pc.getClan()));
			    }

				// pc.sendPackets(new S_NewUI(0x45, pc.getClan().getClanName(),
				// pc), true);

				// System.out.println(1);

				// /pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED,
				// 0x07, pc.getName()), true);

				// pc.sendPackets(new
				// S_PacketBox(S_PacketBox.WORLDMAP_UNKNOWN1), true);

				// pc.sendPackets(new S_ClanJoinLeaveStatus(pc), true);
				// Broadcaster.broadcastPacket(pc, new
				// S_ClanJoinLeaveStatus(pc));

				// pc.sendPackets(new S_ReturnedStat(pc,
				// S_ReturnedStat.CLAN_JOIN_LEAVE), true);
				// Broadcaster.broadcastPacket(pc, new S_ReturnedStat(pc,
				// S_ReturnedStat.CLAN_JOIN_LEAVE));
			} else {
				S_ServerMessage sm = new S_ServerMessage(1064);
				pc.sendPackets(sm);
				sm = null;
				// pc.sendPackets(new S_Pledge("pledge", pc.getId()));
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_PLEDGE;
	}

}
