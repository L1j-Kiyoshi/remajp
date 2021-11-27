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

import java.io.File;

import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_ServerMessage;
import server.LineageClient;
import server.message.ServerMessage;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_LeaveClan extends ClientBasePacket {
	

	private static final String C_LEAVE_CLAN = "[C] C_LeaveClan";

	public C_LeaveClan(byte abyte0[], LineageClient clientthread)
			throws Exception {
		super(abyte0);
		try {
			String clanname = readS();
			L1PcInstance player = clientthread.getActiveChar();
			int clan_id = player.getClanid();

			if (clan_id == 0)
				return;

			L1Clan clan = L1World.getInstance().getClan(player.getClanname());
			if (clan == null || player.getClanname() == null)
				return;
			if (!player.getClanname().equalsIgnoreCase(clanname))
				return;

			// ÇØ´ç Ç÷ÀÇ ±ºÁÖÀÎ°¡?
			if (player.isCrown() && player.getId() == clan.getLeaderId()) {
				leaveClanBoss(clan, player);
			} else { // ±ºÁÖ°¡ ¾Æ´Ñ Ç÷¸Í¿øÀÇ Å»Åð
				// ÀüÀüÀï ¸®½ºÆ®¸¦ Ãëµæ
				boolean warc = false;
				if (player.getLevel() >= 70) {
					for (L1War war : L1World.getInstance().getWarList()) {
						boolean ret = war.CheckClanInWar(player.getClanname());
						if (ret) {
							String enemy_clan_name = war
									.GetEnemyClanName(player.getClanname());
							if (enemy_clan_name != null) {
								S_Message_YN myn = new S_Message_YN(1906, null);
								player.sendPackets(myn, true);
								warc = true;
							}
						}
					}
				}
				if (!warc)
					leaveClanMember(clan, player);
			}
		} catch (Exception e) {
		}
		clear();
	}

	private void leaveClanBoss(L1Clan clan, L1PcInstance player)
			throws Exception {
		String player_name = player.getName();
		String clan_name = player.getClanname();

		if (clan.getCastleId() > 0 || clan.getHouseId() > 0) {
			S_ServerMessage sm = new S_ServerMessage(
					ServerMessage.HAVING_NEST_OF_CLAN);
			player.sendPackets(sm, true);
			return;
		}

		S_ServerMessage sm = new S_ServerMessage(
				ServerMessage.CANNOT_BREAK_CLAN);
		for (L1War war : L1World.getInstance().getWarList()) {
			if (war.CheckClanInWar(clan_name)) {
				player.sendPackets(sm, true);
				return;
			}
		}

		if (clan.AllianceSize() > 0) {
			S_ServerMessage sm2 = new S_ServerMessage(
					ServerMessage.CANNOT_BREAK_CLAN_HAVING_ALLIANCE);
			player.sendPackets(sm2, true);
			return;
		}

		L1PcInstance pc = null;
		S_ServerMessage sm3 = new S_ServerMessage(269, player_name, clan_name);
		for (int i = 0; i < clan.getClanMemberList().size(); i++) { // Ç÷¸Í¿øµéÀÇ Ç÷¸Í
																	// Á¤º¸¸¦ ÃÊ±âÈ­
			pc = L1World.getInstance().getPlayer(
					clan.getClanMemberList().get(i).name);

			if (pc == null) { // Ç÷¸Í¿øÀÌ ¿ÀÇÁ¶óÀÎÀÎ °æ¿ì
				pc = CharacterTable.getInstance().restoreCharacter(
						clan.getClanMemberList().get(i).name);
			} else { // %1Ç÷¸ÍÀÇ ±ºÁÖ %0°¡ Ç÷¸ÍÀ» ÇØ»ê½ÃÄ×½À´Ï´Ù.
				pc.sendPackets(sm3);
			}
			pc.ClearPlayerClanData(clan);
		}
		String emblem_file = String.valueOf(player.getClanid());
		File file = new File("emblem/" + emblem_file);
		file.delete();

		ClanTable.getInstance().deleteClan(clan_name);

		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.CLAN_JOIN_LEAVE),
				true);
		file = null;
		emblem_file = null;
	}

	private void leaveClanMember(L1Clan clan, L1PcInstance player)
			throws Exception {
		String player_name = player.getName();
		String clan_name = player.getClanname();
		L1PcInstance clanMember[] = clan.getOnlineClanMember();

		S_ServerMessage sm = new S_ServerMessage(ServerMessage.LEAVE_CLAN,
				player_name, clan_name);
		for (int i = 0; i < clanMember.length; i++) {
			clanMember[i].sendPackets(sm); // \f1%0ÀÌ %1Ç÷¸ÍÀ» Å»ÅðÇß½À´Ï´Ù.
		}
		clanMember = null;
		player.ClearPlayerClanData(clan);
		clan.removeOnlineClanMember(player_name);
		clan.removeClanMember(player_name);
	}

	@Override
	public String getType() {
		return C_LEAVE_CLAN;
	}
}
