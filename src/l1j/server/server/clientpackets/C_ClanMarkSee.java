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
 */
package l1j.server.server.clientpackets;

import java.io.IOException;

import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_문장주시;
import server.LineageClient;

public class C_ClanMarkSee extends ClientBasePacket {
	private static final String C_ClanMarkSee = "[C] C_ClanMarkSee";
	public C_ClanMarkSee(byte[] decrypt, LineageClient client)
			throws IOException {
		super(decrypt);
		try {
			int type = readC();
			String clanname = readS();
			L1PcInstance pc = client.getActiveChar();
			if (pc == null || pc.getClanid() == 0
					|| pc.getClanRank() != L1Clan.CLAN_RANK_PRINCE)
				return;
			L1Clan target_clan = L1World.getInstance().getClan(clanname);
			if (target_clan == null)
				return;
			L1PcInstance target_clanMaster = L1World.getInstance().getPlayer(
					target_clan.getLeaderName());
			if (target_clanMaster == null) {
				pc.sendPackets(new S_SystemMessage("해당 혈맹의 군주가 접속중이 아닙니다."));
				return;
			}
			switch (type) {
			case 0: // 추가
				pc.set주시아이디(target_clanMaster.getClanid()); // 상대의 오브젝트 ID를 보존해
															// 둔다
				target_clanMaster.set주시아이디(pc.getClanid());
				S_Message_YN yn = new S_Message_YN(3348, pc.getClanname());
				target_clanMaster.sendPackets(yn, true);
				// target_clan 에 수락 메시지 보내기
				break;
			case 1: // 삭제
				pc.getClan().removeMarkSeeList(clanname);
				ClanTable.getInstance().deleteObserveClan(pc.getClanid(),
						clanname);
				for (L1PcInstance tp : pc.getClan().getOnlineClanMember()) {
					tp.sendPackets(new S_문장주시(pc.getClan(), 2), true);
					tp.sendPackets(new S_문장주시(pc.getClan(), 1), true);
					tp.sendPackets(new S_ServerMessage(3359, clanname), true);
				}
				break;
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_ClanMarkSee;
	}
}