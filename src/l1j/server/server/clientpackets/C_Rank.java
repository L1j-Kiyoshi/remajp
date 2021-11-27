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

import java.util.Random; // 생존
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound; // 생존
import l1j.server.server.serverpackets.S_SystemMessage; // 생존
import l1j.server.server.utils.FaceToFace;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Rank extends ClientBasePacket {
	

	private static final String C_RANK = "[C] C_Rank";
	private static Logger _log = Logger.getLogger(C_Rank.class.getName());

	public C_Rank(byte abyte0[], LineageClient clientthread) throws Exception {
		super(abyte0);

		try {
			int type = readC(); // ?
			int rank = 0;
			
			try{
				rank = readC();
			}catch(Exception e){}

			L1PcInstance pc = clientthread.getActiveChar();
			if (pc == null) {
				return;
			}

			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			String clanname = pc.getClanname();

			if ((clan == null) && (type < 5)) {
				return;
			}

			int Enchantlvl = 0; // 생존

			switch (type) {
			case 0: // 혈맹에 속한 인원수의 변동이 있었을 경우
				// pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_ONE));
				S_PacketBox pb = new S_PacketBox(pc, S_PacketBox.PLEDGE_TWO);
				pc.sendPackets(pb, true);
				break;
			case 1:// 계급
				String name = readS();
				L1PcInstance targetPc = L1World.getInstance().getPlayer(name);
				if (rank != L1Clan.CLAN_RANK_PROBATION
						&& rank != L1Clan.CLAN_RANK_PUBLIC
						&& rank != L1Clan.CLAN_RANK_GUARDIAN
						&& rank != L1Clan.CLAN_RANK_정예) {
					// 랭크를 변경하는 사람의 이름과 랭크를 입력해 주세요. [랭크=가디안, 일반, 견습]
					S_ServerMessage sm = new S_ServerMessage(781);
					pc.sendPackets(sm, true);
					return;
				}

				if (pc.isCrown()) { // 군주
					String d = pc.getClan().getClanSubPrince();
					if (d == null || !d.equalsIgnoreCase(pc.getName())) {
						if (pc.getId() != clan.getLeaderId()) { // 혈맹주
							S_ServerMessage sm = new S_ServerMessage(785);
							pc.sendPackets(sm, true);
							return;
						}
					}
				} else {
					if (pc.getClanRank() != L1Clan.CLAN_RANK_GUARDIAN) {
						S_ServerMessage sm = new S_ServerMessage(518);
						pc.sendPackets(sm, true);
						return;
					}
				}
				if (pc.getName().equalsIgnoreCase(name)) {
					S_SystemMessage sm = new S_SystemMessage(
							"자신에게 계급을 부여 할 수 없습니다.");
					pc.sendPackets(sm, true);
					return;
				}

				if (targetPc != null) { // 온라인중
					if (pc.getClanid() == targetPc.getClanid()) { // 같은 크란
						try {
							if (rank == L1Clan.CLAN_RANK_GUARDIAN
									&& targetPc.getLevel() < 40) {
								S_SystemMessage sm = new S_SystemMessage(
										"수호기사는 레벨 40이상 혈맹원에게만 부여 가능합니다.");
								pc.sendPackets(sm, true);
								return;
							}
							if (rank == L1Clan.CLAN_RANK_GUARDIAN
									&& pc.getClanRank() == L1Clan.CLAN_RANK_GUARDIAN) {
								S_SystemMessage sm = new S_SystemMessage(
										"당신은 계급 부여를 수습기사 까지만  할 수 있습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							if (targetPc.getClanRank() == L1Clan.CLAN_RANK_PRINCE) {
								S_SystemMessage sm = new S_SystemMessage(
										"군주의 계급을 변경 할 수 없습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							int gc = 5;
							if (pc.getClan().getClanSubPrince() != null) {
								gc = 12;
							}
							if (pc.getClan().getGuardianCount() > gc
									&& rank == L1Clan.CLAN_RANK_GUARDIAN) {
								S_SystemMessage sm = new S_SystemMessage(
										"일반 혈맹 수호기사는 " + gc + "명 이상이 될수 없습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							if (rank == L1Clan.CLAN_RANK_정예
									&& pc.getClan().get정예Count() > 19) {
								S_SystemMessage sm = new S_SystemMessage(
										"정예기사는 " + gc + "명 이상이 될수 없습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							targetPc.setClanRank(rank);
							clan.setClanRankMember(targetPc.getName(), rank);
							targetPc.save(); // DB에 캐릭터 정보를 기입한다
							String rankString = "일반";
							if (rank == L1Clan.CLAN_RANK_PROBATION) {
								rankString = "수련";
							} else if (rank == L1Clan.CLAN_RANK_PUBLIC) {
								rankString = "일반";
							} else if (rank == L1Clan.CLAN_RANK_GUARDIAN) {
								rankString = "수호기사";
							} else if (rank == L1Clan.CLAN_RANK_정예) {
								rankString = "정예기사";
							}
							// targetPc.sendPackets(new S_ServerMessage(784,
							// rankString)); // 당신의 랭크가%s로 변경되었습니다.
							S_SystemMessage sm = new S_SystemMessage("당신의 계급이 "
									+ rankString + "(으)로 변경되었습니다.");
							targetPc.sendPackets(sm, true);
							S_SystemMessage sm2 = new S_SystemMessage(
									targetPc.getName() + "의 계급이 " + rankString
											+ "(으)로 변경되었습니다.");
							pc.sendPackets(sm2, true);
						} catch (Exception e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						}
					} else {
						S_ServerMessage sm = new S_ServerMessage(414);
						pc.sendPackets(sm, true);
						return;
					}
				} else { // 오프 라인중
					L1PcInstance restorePc = CharacterTable.getInstance()
							.restoreCharacter(name);
					if (restorePc != null
							&& restorePc.getClanid() == pc.getClanid()) { // 같은
																			// 크란
						try {
							if (rank == L1Clan.CLAN_RANK_GUARDIAN
									&& restorePc.getLevel() < 40) {
								S_SystemMessage sm = new S_SystemMessage(
										"수호기사는 레벨 40이상 혈맹원에게만 부여 가능합니다.");
								pc.sendPackets(sm, true);
								return;
							}
							if (rank == L1Clan.CLAN_RANK_GUARDIAN
									&& pc.getClanRank() == L1Clan.CLAN_RANK_GUARDIAN) {
								S_SystemMessage sm = new S_SystemMessage(
										"당신은 계급 부여를 수습기사 까지만  할 수 있습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							if (restorePc.getClanRank() == L1Clan.CLAN_RANK_PRINCE) {
								S_SystemMessage sm = new S_SystemMessage(
										"군주의 계급을 변경 할 수 없습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							restorePc.setClanRank(rank);
							clan.setClanRankMember(name, rank);
							restorePc.save(); // DB에 캐릭터 정보를 기입한다
						} catch (Exception e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						}
					} else {
						S_ServerMessage sm = new S_ServerMessage(109, name);
						pc.sendPackets(sm, true);
						return;
					}
				}
				break;

			case 2:// 목록
					// 5c 3e c7 c1 b8 ae bf a1 b9 d9 20 00 3f 53 ac 73
					// 5c 3e 45 76 61 73 74 6f 72 79 20 00 00 3d ea 03 // 10월21일
					// 확인
					// 옵 /서브 /동맹클랜네임

				if (clan.AllianceSize() > 0) {
					S_PacketBox pb2 = new S_PacketBox(pc,
							S_PacketBox.ALLIANCE_LIST);
					pc.sendPackets(pb2, true);
					// pc.sendPackets(new S_PacketBox(S_PacketBox.PLEDGE_UNION,
					// clan.getAlliance()));
				}/*
				 * else { S_ServerMessage sm = new S_ServerMessage(1233);
				 * pc.sendPackets(sm, true); return; }
				 */
				break;
			case 3:// 가입
				L1PcInstance allianceLeader = FaceToFace.faceToFace(pc);
				if (pc.getLevel() < 25 || !pc.isCrown()) {
					S_ServerMessage sm = new S_ServerMessage(1206);
					pc.sendPackets(sm, true);
					return;
				}/*
				 * if (pc.getClan().getAlliance() != 0) { pc.sendPackets(new
				 * S_ServerMessage(1202));// 이미 동맹에 가입된 상태입니다. return; }
				 */
				for (L1War war : L1World.getInstance().getWarList()) {
					if (war.CheckClanInWar(clanname)) {
						S_ServerMessage sm = new S_ServerMessage(1234);
						pc.sendPackets(sm, true);
						return;
					}
				}
				// 동맹 수 제한(4개혈맹) 추가해야함 // 1201 // 동맹에 가입할 수 없습니다.
				if (clan.AllianceSize() > 4) {
					S_SystemMessage sm = new S_SystemMessage(
							"동맹은 4개 혈맹 까지만 가능합니다.");
					pc.sendPackets(sm, true);
					return;
				}
				if (allianceLeader != null) {
					if (allianceLeader.getLevel() > 24
							&& allianceLeader.isCrown()) {
						allianceLeader.setTempID(pc.getId());
						S_Message_YN yn = new S_Message_YN(223, pc.getName());
						allianceLeader.sendPackets(yn, true);
					} else {
						S_ServerMessage sm = new S_ServerMessage(1201);
						pc.sendPackets(sm, true);
					}
				}
				break;
			case 4:// 탈퇴
				for (L1War war : L1World.getInstance().getWarList()) {
					if (war.CheckClanInWar(clanname)) {
						S_ServerMessage sm = new S_ServerMessage(1203);
						pc.sendPackets(sm, true);
						return;
					}
				}
				if (clan.AllianceSize() > 0) {
					S_Message_YN yn = new S_Message_YN(1210, "");
					pc.sendPackets(yn, true);
				} else {
					S_ServerMessage sm = new S_ServerMessage(1233);
					pc.sendPackets(sm, true);
				}
				break;

			// /////////////////생존의 외침
			case 5:
				Random random = new Random();
				try {
					int NewHp = 0;

					if (pc.get_food() >= 225) {
						try {
							Enchantlvl = pc.getWeapon().getEnchantLevel();
						} catch (Exception e) {
							// pc.sendPackets(new
							// S_SystemMessage("\\fY무기를 착용해야 생존의 외침을 사용할 수 있습니다."));
							S_ServerMessage sm = new S_ServerMessage(1973);
							pc.sendPackets(sm, true);
							return;
						}

						if ((60000L * 100) < System.currentTimeMillis()
								- pc.getSurvivalCry()) {

							NewHp = pc.getCurrentHp() + (Enchantlvl * 100);

							if (NewHp > pc.getMaxHp()) {
								NewHp = pc.getMaxHp();
							}

							pc.setCurrentHp(NewHp);
							S_SkillSound ss = null;
							if (pc.getWeapon().getItemId() == 61
									|| pc.getWeapon().getItemId() == 86)
								ss = new S_SkillSound(pc.getId(), 8773);
							else if (Enchantlvl <= 6)
								ss = new S_SkillSound(pc.getId(), 8684);
							else if ((Enchantlvl >= 7) && (Enchantlvl <= 8))
								ss = new S_SkillSound(pc.getId(), 8685);
							else if ((Enchantlvl >= 9) && (Enchantlvl <= 10))
								ss = new S_SkillSound(pc.getId(), 8773);
							else if (Enchantlvl >= 11)
								ss = new S_SkillSound(pc.getId(), 8686);
							pc.sendPackets(ss);
							Broadcaster.broadcastPacket(pc, ss, true);
							pc.set_food(0);
							S_PacketBox pb2 = new S_PacketBox(11, pc.get_food());
							pc.sendPackets(pb2, true);
							pc.setSurvivalCry(System.currentTimeMillis());
							/*
							 * if(pc.getWeapon().getItemId() == 61 ||
							 * pc.getWeapon().getItemId() == 86){ int[]
							 * probability = { 50, 60 }; int percent =
							 * probability[random.nextInt(probability.length)];
							 * 
							 * NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 *
							 * percent;
							 * 
							 * if (NewHp > pc.getMaxHp()) { NewHp =
							 * pc.getMaxHp(); }
							 * 
							 * pc.setCurrentHp(NewHp); S_SystemMessage sm = new
							 * S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " +
							 * percent + "%를 회복하였습니다."); pc.sendPackets(sm,
							 * true); S_SkillSound ss = new
							 * S_SkillSound(pc.getId(), 8773);
							 * pc.sendPackets(ss);
							 * Broadcaster.broadcastPacket(pc, ss, true);
							 * pc.set_food(0); S_PacketBox pb2 = new
							 * S_PacketBox(11, pc.get_food());
							 * pc.sendPackets(pb2, true);
							 * pc.setSurvivalCry(System.currentTimeMillis());
							 * probability = null; }else if (Enchantlvl <= 6) {
							 * int[] probability = { 20, 30, 40 }; int percent =
							 * probability[random.nextInt(probability.length)];
							 * NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 *
							 * percent;
							 * 
							 * if (NewHp > pc.getMaxHp()) { NewHp =
							 * pc.getMaxHp(); }
							 * 
							 * pc.setCurrentHp(NewHp); S_SystemMessage sm = new
							 * S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " +
							 * percent + "%를 회복하였습니다."); pc.sendPackets(sm,
							 * true); S_SkillSound ss = new
							 * S_SkillSound(pc.getId(), 8684);
							 * pc.sendPackets(ss);
							 * Broadcaster.broadcastPacket(pc, ss, true);
							 * pc.set_food(0); S_PacketBox pb2 = new
							 * S_PacketBox(11, pc.get_food());
							 * pc.sendPackets(pb2, true);
							 * pc.setSurvivalCry(System.currentTimeMillis());
							 * probability = null; } else if ((Enchantlvl >= 7)
							 * && (Enchantlvl <= 8)) { int[] probability = { 30,
							 * 40, 50 }; int percent =
							 * probability[random.nextInt(probability.length)];
							 * 
							 * NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 *
							 * percent;
							 * 
							 * if (NewHp > pc.getMaxHp()) { NewHp =
							 * pc.getMaxHp(); }
							 * 
							 * pc.setCurrentHp(NewHp); S_SystemMessage sm = new
							 * S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " +
							 * percent + "%를 회복하였습니다."); pc.sendPackets(sm,
							 * true); S_SkillSound ss = new
							 * S_SkillSound(pc.getId(), 8685);
							 * pc.sendPackets(ss);
							 * Broadcaster.broadcastPacket(pc, ss, true);
							 * pc.set_food(0); S_PacketBox pb2 = new
							 * S_PacketBox(11, pc.get_food());
							 * pc.sendPackets(pb2, true);
							 * pc.setSurvivalCry(System.currentTimeMillis());
							 * probability = null; } else if ((Enchantlvl >= 9)
							 * && (Enchantlvl <= 10)) { int[] probability = {
							 * 50, 60 }; int percent =
							 * probability[random.nextInt(probability.length)];
							 * 
							 * NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 *
							 * percent;
							 * 
							 * if (NewHp > pc.getMaxHp()) { NewHp =
							 * pc.getMaxHp(); }
							 * 
							 * pc.setCurrentHp(NewHp); S_SystemMessage sm = new
							 * S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " +
							 * percent + "%를 회복하였습니다."); pc.sendPackets(sm,
							 * true); S_SkillSound ss = new
							 * S_SkillSound(pc.getId(), 8773);
							 * pc.sendPackets(ss);
							 * Broadcaster.broadcastPacket(pc, ss, true);
							 * pc.set_food(0); S_PacketBox pb2 = new
							 * S_PacketBox(11, pc.get_food());
							 * pc.sendPackets(pb2, true);
							 * pc.setSurvivalCry(System.currentTimeMillis());
							 * probability = null; } else if (Enchantlvl >= 11)
							 * { NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100
							 * * 70;
							 * 
							 * if (NewHp > pc.getMaxHp()) { NewHp =
							 * pc.getMaxHp(); }
							 * 
							 * pc.setCurrentHp(NewHp); S_SystemMessage sm = new
							 * S_SystemMessage
							 * ("\\fY생존의 외침을 사용하여 최대HP 70%를 회복하였습니다.");
							 * pc.sendPackets(sm, true); S_SkillSound ss = new
							 * S_SkillSound(pc.getId(), 8686);
							 * pc.sendPackets(ss);
							 * Broadcaster.broadcastPacket(pc, ss, true);
							 * pc.set_food(0); S_PacketBox pb2 = new
							 * S_PacketBox(11, pc.get_food());
							 * pc.sendPackets(pb2, true);
							 * pc.setSurvivalCry(System.currentTimeMillis()); }
							 */
						} else {
							long time = 6000L - (System.currentTimeMillis() - pc
									.getSurvivalCry()) / 1000L;

							long minute = time / 60L;
							long second = time % 60L;

							if (minute < 100L) {
								S_SystemMessage sm = new S_SystemMessage(
										"\\fY생존의 외침은 " + minute + "분 " + second
												+ "초 후에 재사용 가능합니다.");
								pc.sendPackets(sm, true);
								return;
							}
							/*
							 * NewHp = pc.getCurrentHp() + pc.getMaxHp() / 100 *
							 * (30 - (int)minute);
							 * 
							 * if (NewHp > pc.getMaxHp()) { NewHp =
							 * pc.getMaxHp(); }
							 * 
							 * pc.setCurrentHp(NewHp); S_SystemMessage sm = new
							 * S_SystemMessage("\\fY생존의 외침을 사용하여 최대HP " + (30 -
							 * (int)minute) + "%를 회복하였습니다."); pc.sendPackets(sm,
							 * true); S_SkillSound ss = new
							 * S_SkillSound(pc.getId(), 8683);
							 * pc.sendPackets(ss);
							 * Broadcaster.broadcastPacket(pc, ss, true);
							 * pc.set_food(0); S_PacketBox pb2 = new
							 * S_PacketBox(11, pc.get_food());
							 * pc.sendPackets(pb2, true);
							 * pc.setSurvivalCry(System.currentTimeMillis());
							 */
						}
					} else {
						// S_SystemMessage sm = new
						// S_SystemMessage("\\fY생존의 외침은 배고픔게이지 100% 채운 시점부터,");
						// pc.sendPackets(sm); sm = null;
						// pc.sendPackets(new
						// S_SystemMessage("\\fY30분뒤에 사용가능합니다."));
						S_ServerMessage sm2 = new S_ServerMessage(1974);
						pc.sendPackets(sm2, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					random = null;
				}
				break;
			case 6:
				try {
					if (pc.get_food() >= 225) {
						try {
							Enchantlvl = pc.getWeapon().getEnchantLevel();
						} catch (Exception e) {
							S_SystemMessage sm = new S_SystemMessage(
									"\\fY무기를 착용해야 생존의 외침을 사용할 수 있습니다.");
							pc.sendPackets(sm, true);
							return;
						}

						// 집행, 붉이 인첸9~10 효과 나게
						if (pc.getWeapon().getItemId() == 61
								|| pc.getWeapon().getItemId() == 86) {
							S_SkillSound ss = new S_SkillSound(pc.getId(), 8773);
							pc.sendPackets(ss);
							Broadcaster.broadcastPacket(pc, ss, true);
						} else if (Enchantlvl <= 6) {
							S_SkillSound ss = new S_SkillSound(pc.getId(), 8684);
							pc.sendPackets(ss);
							Broadcaster.broadcastPacket(pc, ss, true);
						} else if ((Enchantlvl >= 7) && (Enchantlvl <= 8)) {
							S_SkillSound ss = new S_SkillSound(pc.getId(), 8685);
							pc.sendPackets(ss);
							Broadcaster.broadcastPacket(pc, ss, true);
						} else if ((Enchantlvl >= 9) && (Enchantlvl <= 10)) {
							S_SkillSound ss = new S_SkillSound(pc.getId(), 8773);
							pc.sendPackets(ss);
							Broadcaster.broadcastPacket(pc, ss, true);
						} else if (Enchantlvl >= 11) {
							S_SkillSound ss = new S_SkillSound(pc.getId(), 8686);
							pc.sendPackets(ss);
							Broadcaster.broadcastPacket(pc, ss, true);
						}
					} else {
						S_SystemMessage ss = new S_SystemMessage(
								"\\fY생존의 외침은 배고픔게이지 100% 채우고,");
						pc.sendPackets(ss, true);
						S_SystemMessage ss2 = new S_SystemMessage(
								"\\fY사용하시기 바랍니다.");
						pc.sendPackets(ss2, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				break;
			case 8:
				int time = (60 * 60 * 2) - pc.getgirantime();
				time = time > 0 ? time / 120 : 0;
				pc.sendPackets(new S_ServerMessage(2535, "기란/지배자의 결계", time + ""), true);
				time = 7200 - pc.getivorytime();
				time = time > 0 ? time / 60 : 0;
				pc.sendPackets(new S_ServerMessage(2535, "상아탑:발록 진영", time + ""), true);
				time = 10800 - pc.getpc용둥time();
				time = time > 0 ? time / 60 : 0;
				pc.sendPackets(new S_ServerMessage(2535, "용의 둥지", time + ""), true);
				time = 3600 - pc.get수상한천상계곡time();
				time = time > 0 ? time / 60 : 0;
				pc.sendPackets(new S_ServerMessage(2535, "수상한 천상의 계곡", time + ""), true);
				time = 3600 - pc.getivoryyaheetime();
				time = time > 0 ? time / 60 : 0;
				pc.sendPackets(new S_ServerMessage(2535, "상아탑:야히 진영(PC)", time + ""), true);
				time = 28800 - pc.get낚시time();
				time = time > 0 ? time / 480 : 0;
				pc.sendPackets(new S_ServerMessage(2535, "낚시터", time + ""), true);
				time = 7200 - pc.get잊섬time();
				time = time > 0 ? time / 120 : 0;
				pc.sendPackets(new S_ServerMessage(2535, "잊혀진 섬", time + ""), true);
				break;
			case 9:
				pc.sendPackets(new S_PacketBox(pc, S_PacketBox.DG_TIME_RESTART), true);
				break;
			}
		} catch (Exception e) {
			
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_RANK;
	}
}
