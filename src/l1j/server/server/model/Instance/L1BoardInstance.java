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

package l1j.server.server.model.Instance;

//import l1j.server.GameSystem.BugRaceController;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_Age;
import l1j.server.server.serverpackets.S_Board;
import l1j.server.server.serverpackets.S_BoardRead;
import l1j.server.server.serverpackets.S_EnchantRanking;
import l1j.server.server.serverpackets.S_Hunt;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NavarWarfare_Ranking;
import l1j.server.server.serverpackets.S_Ranking;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Npc;

public class L1BoardInstance extends L1NpcInstance {
	// private GameServerSetting _GameServerSetting =
	// GameServerSetting.getInstance();
	private static final long serialVersionUID = 1L;

	public L1BoardInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		if (this.getNpcTemplate().get_npcId() == 999999) {// 버그베어 승률 게시판
		} else if (this.getNpcTemplate().get_npcId() == 4200012) {// 랭킹 게시판
			S_Ranking rk = new S_Ranking(this);
			player.sendPackets(rk, true);
		} else if (this.getNpcTemplate().get_npcId() == 4200013) {// 랭킹 게시판 2
			S_EnchantRanking er = new S_EnchantRanking(this);
			player.sendPackets(er, true);
		} else if (this.getNpcTemplate().get_npcId() == 4200016) {// 현상금 게시판
			S_Hunt h = new S_Hunt(this);
			player.sendPackets(h, true);
		} else if (this.getNpcTemplate().get_npcId() == 100212) {// 해상전 랭킹
			// player.sendPackets(new S_NavarWarfare_Ranking(this), true);
			player.sendPackets(new S_NPCTalkReturn(getId(), "id_s"), true);
		} else if (getNpcTemplate().get_npcId() == 4500200
				|| getNpcTemplate().get_npcId() == 4500201) {
			String htmlid = null;
			String[] htmldata = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(
					this, 5)) {
				if (object instanceof L1HousekeeperInstance) {
					L1HousekeeperInstance keeper = (L1HousekeeperInstance) object;
					int npcid = keeper.getNpcTemplate().get_npcId();
					L1House targetHouse = null;
					for (L1House house : HouseTable.getInstance()
							.getHouseTableList()) {
						if (npcid == house.getKeeperId()) {
							targetHouse = house;
							break;
						}
					}

					boolean isOccupy = false;
					String clanName = null;
					String leaderName = null;

					for (L1Clan targetClan : L1World.getInstance()
							.getAllClans()) {
						if (targetHouse.getHouseId() == targetClan.getHouseId()) {
							isOccupy = true;
							clanName = targetClan.getClanName();
							leaderName = targetClan.getLeaderName();
							break;
						}
					}

					if (isOccupy) {
						htmlid = "agname";
						htmldata = new String[] { clanName, leaderName,
								targetHouse.getHouseName() };
					} else {
						htmlid = "agnoname";
						htmldata = new String[] { targetHouse.getHouseName() };
					}
				}

				if (htmlid != null) {
					if (htmldata != null) {
						S_NPCTalkReturn nt = new S_NPCTalkReturn(getId(),
								htmlid, htmldata);
						player.sendPackets(nt, true);
						break;
					} else {
						S_NPCTalkReturn nt = new S_NPCTalkReturn(getId(),
								htmlid);
						player.sendPackets(nt, true);
						break;
					}
				}
			}
			htmlid = null;
			htmldata = null;
		} else {
			S_Board b = new S_Board(this);
			player.sendPackets(b, true);
		}
	}

	public void onAction(L1PcInstance player, int number) {
		S_Board b = new S_Board(this, number);
		player.sendPackets(b, true);
	}

	public void onActionRead(L1PcInstance player, int number) {

		if (this.getNpcTemplate().get_npcId() == 4200012) {// 랭킹 게시판
			S_Ranking kr = new S_Ranking(player, number);
			player.sendPackets(kr, true);
		} else if (this.getNpcTemplate().get_npcId() == 4200013) {// 랭킹 게시판 2
			S_EnchantRanking er = new S_EnchantRanking(player, number);
			player.sendPackets(er, true);
		} else if (this.getNpcTemplate().get_npcId() == 4200016) {// 현상금
			S_Hunt h = new S_Hunt(player, number);
			player.sendPackets(h, true);
		} else if (this.getNpcTemplate().get_npcId() == 100212) {// 해상전 랭킹
			player.sendPackets(new S_NavarWarfare_Ranking(player, number), true);
		} else {
			if (this.getNpcTemplate().get_npcId() == 4200020) {
				if (!player.isGm()) {
					S_SystemMessage sm = new S_SystemMessage("건의사항입니다.");
					player.sendPackets(sm, true);
					return;
				}
			}
			S_BoardRead br = new S_BoardRead(this, number);
			player.sendPackets(br, true);
		}
	}

	public void onAge(L1PcInstance player) {
		S_Age a = new S_Age(this);
		player.sendPackets(a, true);
	}

	public void onAgeRead(L1PcInstance player, int number) {
		S_Age a = new S_Age(player, number);
		player.sendPackets(a, true);
	}

}
