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

import java.util.List;
import java.util.Random;

import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_War extends ClientBasePacket {
	

	private static final String C_WAR = "[C] C_War";

	public C_War(byte abyte0[], LineageClient clientthread) throws Exception {
		super(abyte0);
		try {
			int type = readC();
			// System.out.println(type);
			String s = readS();
			// System.out.println(s);
			L1PcInstance player = clientthread.getActiveChar();
			String playerName = player.getName();
			String clanName = player.getClanname();
			int clanId = player.getClanid();

			if (!player.isCrown()) { // 군주 이외
				S_ServerMessage sm = new S_ServerMessage(478);
				player.sendPackets(sm, true);
				return;
			}
			if (clanId == 0) { // 크란미소속
				S_ServerMessage sm = new S_ServerMessage(272);
				player.sendPackets(sm, true);
				return;
			}
			L1Clan clan = L1World.getInstance().getClan(clanName);
			if (clan == null) { // 자크란이 발견되지 않는다
				S_SystemMessage sm = new S_SystemMessage("대상 혈맹이 발견되지 않았습니다.");
				player.sendPackets(sm, true);
				return;
			}
			if (player.getId() != clan.getLeaderId()
					|| (type == 2 && player.getClanRank() != L1Clan.CLAN_RANK_PRINCE)) { // 혈맹주
				S_ServerMessage sm = new S_ServerMessage(478);
				player.sendPackets(sm, true);
				return;
			}

			if (clanName.toLowerCase().equals(s.toLowerCase())) { // 자크란을 지정
				S_SystemMessage sm = new S_SystemMessage(
						"자신의 혈에 공성 선포는 불가능합니다.");
				player.sendPackets(sm, true);
				return;
			}

			L1Clan enemyClan = null;
			String enemyClanName = null;

			for (L1Clan checkClan : L1World.getInstance().getAllClans()) { // 크란명을
																			// 체크
				if (checkClan.getClanName().toLowerCase()
						.equals(s.toLowerCase())) {
					enemyClan = checkClan;
					enemyClanName = checkClan.getClanName();
					break;
				}
			}
			if (enemyClan == null) { // 상대 크란이 발견되지 않았다
				S_SystemMessage sm = new S_SystemMessage("대상 혈맹이 발견되지 않았습니다.");
				player.sendPackets(sm, true);
				return;
			}
			if (clan.getAlliance(enemyClan.getClanId()) == enemyClan) {
				S_ServerMessage sm = new S_ServerMessage(1205);
				player.sendPackets(sm, true);
				return;
			}
			boolean inWar = false;
			List<L1War> warList = L1World.getInstance().getWarList(); // 전쟁 리스트를
																		// 취득
			for (L1War war : warList) {
				if (war.CheckClanInWar(clanName)) { // 자크란이 이미 전쟁중
					/*
					 * if (type == 0) { // 선전포고 S_ServerMessage sm = new
					 * S_ServerMessage(234); player.sendPackets(sm); // \f1당신의
					 * 혈맹은 벌써 전쟁중입니다. sm.clear(); sm = null; return; }
					 */
					inWar = true;
					break;
				}
			}
			if (!inWar && (type == 2 || type == 3)) { // 자크란이 전쟁중 이외로, 항복 또는 종결
				S_SystemMessage sm = new S_SystemMessage(
						"전쟁중이 아니므로 진행중이라 항복 을 할수 없습니다.");
				player.sendPackets(sm, true);
				return;
			}

			if (clan.getCastleId() != 0) { // 자크란이 성주
				if (type == 0) { // 선전포고
					S_ServerMessage sm = new S_ServerMessage(474);
					player.sendPackets(sm, true);
					return;
				} else if (type == 2 || type == 3) { // 항복, 종결
					S_SystemMessage sm = new S_SystemMessage("항복을 할 수 없습니다.");
					player.sendPackets(sm, true);
					return;
				}
			}

			if (enemyClan.getCastleId() == 0 && player.getLevel() <= 15) {
				player.sendPackets(new S_ServerMessage(232), true);
				return;
			}
			if (enemyClan.getCastleId() != 0 && player.getLevel() < 80) {
				player.sendPackets(new S_ServerMessage(475), true); // 공성전을
																	// 선언하려면 레벨
																	// 25에 이르지
																	// 않으면 안됩니다.
				return;
			}

			int onLineMemberSize = 0;
			for (L1PcInstance onlineMember : clan.getOnlineClanMember()) {
				if (onlineMember.isPrivateShop())
					continue;
				onLineMemberSize++;
			}
			// if(onLineMemberSize < 15){
			// player.sendPackets(new
			// S_SystemMessage("접속중인 혈맹 구성원이 [15]명 이상 되어야 선포가 가능합니다."), true);
			// return;
			// }

			/*
			 * if (clan.getHouseId() > 0){ S_SystemMessage sm = new
			 * S_SystemMessage("아지트가 있는 상태에서는 선전 포고를 할 수 없습니다.");
			 * player.sendPackets(sm, true); return; }
			 */
			if (enemyClan.getCastleId() != 0) { // 상대 크란이 성주
				int castle_id = enemyClan.getCastleId();
				if (WarTimeController.getInstance().isNowWar(castle_id)) { // 전쟁
																			// 시간내
					L1PcInstance clanMember[] = clan.getOnlineClanMember();
					for (int k = 0; k < clanMember.length; k++) {
						if (L1CastleLocation.checkInWarArea(castle_id,
								clanMember[k])) {
							// S_ServerMessage sm = new S_ServerMessage(477);
							// player.sendPackets(sm, true); // 당신을 포함한 모든 혈맹원이
							// 성의 밖에 나오지 않으면 공성전은 선언할 수 없습니다.
							int[] loc = new int[3];
							Random _rnd = new Random(System.nanoTime());
							loc = L1CastleLocation.getGetBackLoc(castle_id);
							int locx = loc[0] + (_rnd.nextInt(4) - 2);
							int locy = loc[1] + (_rnd.nextInt(4) - 2);
							short mapid = (short) loc[2];
							L1Teleport.teleport(clanMember[k], locx, locy,
									mapid, clanMember[k].getMoveState()
											.getHeading(), true);
						}
					}
					boolean enemyInWar = false;
					for (L1War war : warList) {
						if (war.CheckClanInWar(enemyClanName)) { // 상대 크란이 이미전쟁중
							if (type == 0) { // 선전포고
								war.DeclareWar(clanName, enemyClanName);
								war.AddAttackClan(clanName);
							} else if (type == 2 || type == 3) {
								if (!war.CheckClanInSameWar(clanName,enemyClanName)) { // 자크란과 상대 크란이 다른 전쟁
									S_SystemMessage sm = new S_SystemMessage("다른 혈과 전쟁중입니다.");
									player.sendPackets(sm, true);
									return;
								}
								if (type == 2) { // 항복
									war.SurrenderWar(clanName, enemyClanName);
								} else if (type == 3) { // 종결
									war.CeaseWar(clanName, enemyClanName);
								}
							}
							enemyInWar = true;
							break;
						}
					}
					if (!enemyInWar && type == 0) { // 상대 크란이 전쟁중 이외로, 선전포고
						L1War war = new L1War();
						war.handleCommands(1, clanName, enemyClanName); // 공성전개시
					}
				} else { // 전쟁 시간외
					if (type == 0) { // 선전포고
						S_ServerMessage sm = new S_ServerMessage(476);
						player.sendPackets(sm, true); // 아직 공성전의 시간이 아닙니다.
					}
				}
			} else { // 상대 크란이 성주는 아니다
				for (int i = 1; i < 9; i++) {
					if (WarTimeController.getInstance().isNowWar(i)) {
						player.sendPackets(new S_SystemMessage(
								"공성 중에는 성혈외에는 신청을 할 수 없습니다."));
						return;
					}
				}
				boolean enemyInWar = false;
				for (L1War war : warList) {
					if (war.CheckClanInWar(enemyClanName)) { // 상대 크란이 이미 전쟁중
						if (type == 0) { // 선전포고
							S_ServerMessage sm = new S_ServerMessage(236,
									enemyClanName);
							player.sendPackets(sm, true);
							return;
						} else if (type == 2 || type == 3) { // 항복 또는 종결
							if (!war.CheckClanInSameWar(clanName, enemyClanName)) { // 자크란과
																					// 상대
																					// 크란이
																					// 다른
																					// 전쟁
								S_SystemMessage sm = new S_SystemMessage(
										"이미 전쟁 중입니다.");
								player.sendPackets(sm, true);
								return;
							}
						}
						enemyInWar = true;
						break;
					}
				}
				if (!enemyInWar && (type == 2 || type == 3)) { // 상대 크란이 전쟁중
																// 이외로, 항복 또는 종결
					S_SystemMessage sm = new S_SystemMessage("전쟁중 아니므로 항복 불가능");
					player.sendPackets(sm, true);
					return;
				}

				// 공성전이 아닌 경우, 상대의 혈맹주의 승인이 필요
				L1PcInstance enemyLeader = L1World.getInstance().getPlayer(
						enemyClan.getLeaderName());

				if (enemyLeader == null) { // 상대의 혈맹주가 발견되지 않았다
					S_ServerMessage sm = new S_ServerMessage(218, enemyClanName);
					player.sendPackets(sm, true); // \f1%0 혈맹의 군주는 현재 월드에 없습니다.
					return;
				}

				if (type == 0) { // 선전포고
					enemyLeader.setTempID(player.getId()); // 상대의 오브젝트 ID를 보존해
															// 둔다
					S_Message_YN myn = new S_Message_YN(217, clanName,
							playerName);
					enemyLeader.sendPackets(myn, true); // %0혈맹의%1가 당신의 혈맹과의 전쟁을
														// 바라고 있습니다. 전쟁에 응합니까?
														// (Y/N)
				} else if (type == 2) { // 항복
					enemyLeader.setTempID(player.getId()); // 상대의 오브젝트 ID를 보존해
															// 둔다
					S_Message_YN myn = new S_Message_YN(221, clanName);
					enemyLeader.sendPackets(myn, true); // %0혈맹이 항복을 바라고 있습니다.
														// 받아들입니까? (Y/N)
				} else if (type == 3) { // 종결
					enemyLeader.setTempID(player.getId()); // 상대의 오브젝트 ID를 보존해
															// 둔다
					S_Message_YN myn = new S_Message_YN(222, clanName);
					enemyLeader.sendPackets(myn, true); // %0혈맹이 전쟁의 종결을 바라고
														// 있습니다. 종결합니까? (Y/N)
				}
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_WAR;
	}

}
