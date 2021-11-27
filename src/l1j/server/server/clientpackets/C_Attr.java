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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.GhostHouse;
import l1j.server.GameSystem.PetRacing;
import l1j.server.GameSystem.Antaras.AntarasRaid;
import l1j.server.GameSystem.Antaras.AntarasRaidSystem;
import l1j.server.GameSystem.Antaras.AntarasRaidTimer;
import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.GameSystem.Lind.LindRaid;
import l1j.server.GameSystem.MiniGame.DeathMatch;
import l1j.server.GameSystem.Papoo.PaPooRaid;
import l1j.server.GameSystem.Papoo.PaPooRaidSystem;
import l1j.server.GameSystem.Papoo.PaPooTimer;
import l1j.server.GameSystem.UserRanking.UserRankingController;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.BoardTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ChatParty;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Question;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BuffNpcInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ClanJoinLeaveStatus;
import l1j.server.server.serverpackets.S_ClanWindow;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_Resurrection;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Trade;
import l1j.server.server.serverpackets.S_bonusstats;
import l1j.server.server.serverpackets.S_문장주시;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.types.Point;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;
import server.message.ServerMessage;

public class C_Attr extends ClientBasePacket {
	

	private static final Logger _log = Logger.getLogger(C_Attr.class.getName());
	private static final String C_ATTR = "[C] C_Attr";
	
	public static final int MSGCODE_622_LFC		= 2;
	public static final int MSGCODE_NO 			= 0;
	public static final int MSGCODE_YES 		= 1;

	private static final int HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final int HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	public C_Attr(byte abyte0[], LineageClient clientthread) throws Exception {
		super(abyte0);
		try {
			int i = readH();
			int attrcode;
			int c;
			int msgIdx = 0;
			String name;

			if (i == 479) {
				attrcode = i;
			} else {
				msgIdx 		= readD();
				attrcode 	= readH();
				i			= attrcode;
			}
			L1PcInstance pc = clientthread.getActiveChar();
			if (pc == null)
				return;
			
			if (attrcode == 622) {
				/** 2016.11.26 MJ 앱센터 LFC **/
				if(msgIdx == MSGCODE_622_LFC){
					c = readC();
					if(c == MSGCODE_NO){
						MJLFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_NONE);
					}else if(c == MSGCODE_YES){
						if(pc.getInstStatus() == InstStatus.INST_USERSTATUS_LFCREADY)
							MJLFCCreator.setInstStatus(pc, InstStatus.INST_USERSTATUS_LFCINREADY);
					}
					return;
				}
			}

			if (pc.system != -1)
				i = 622;
			switch (i) {
			case 180:// 셰이프체인지로 변신하기
				c = readC();
				String polys = readS();
				// L1PolyMorph poly =
				// PolyTable.getInstance().getTemplate(polys);
				
				if(polys.equalsIgnoreCase("ranking class polymorph"))
				{
						if(pc.isCrown()) {
							if(pc.get_sex() == 0)	polys = "rangking prince male";
							else					polys = "rangking prince female";
						} else if(pc.isKnight()) {
							if(pc.get_sex() == 0)	polys = "rangking knight male";
							else					polys = "rangking knight female";
						} else if(pc.isElf()) {
							if(pc.get_sex() == 0)	polys = "rangking elf male";
							else					polys = "rangking elf female";
						} else if(pc.isWizard()) {
							if(pc.get_sex() == 0)	polys = "rangking wizard male";
							else					polys = "rangking wizard female";
						} else if(pc.isDarkelf()) {
							if(pc.get_sex() == 0)	polys = "rangking darkelf male";
							else					polys = "rangking darkelf female";
						} else if(pc.isDragonknight()) {
							if(pc.get_sex() == 0)	polys = "rangking dragonknight male";
							else					polys = "rangking dragonknight female";
						} else if(pc.isIllusionist()) {
							if(pc.get_sex() == 0)	polys = "rangking illusionist male";
							else					polys = "rangking illusionist female";
						} else if(pc.isWarrior()) {
							if(pc.get_sex() == 0)	polys = "rangking warrior male";
							else					polys = "rangking warrior female";
						} else if(pc.isFencer()) {
							if(pc.get_sex() == 0)	polys = "rangking fencer male";
							else					polys = "rangking fencer female";
						}
				}

				if (polys.startsWith("rangking ")) {
					int star = UserRankingController.getInstance().getStarCount(pc.getName());
					if (star <= 8) {
						if(!UserRankingController.getInstance().getStarCount(pc.getType(), pc.getName())) {
						return;
				 }
			   }
			 }
				
				L1PolyMorph.handleCommands(pc, polys);
				pc.cancelAbsoluteBarrier();
				break;
			case 3348:// 희미한 기억구슬
				L1Clan target_clan = L1World.getInstance().getClan(
						pc.getClanid());
				L1Clan use_clan = L1World.getInstance().getClan(pc.get주시아이디());
				L1PcInstance target_clanMaster = L1World.getInstance()
						.getPlayer(target_clan.getLeaderName());
				L1PcInstance use_clanMaster = L1World.getInstance().getPlayer(
						use_clan.getLeaderName());
				c = readC();
				if (c == 0) {// 거절
					target_clanMaster.sendPackets(new S_SystemMessage(
							"혈맹 주시 : 혈맹 주시 신청을 거절함."));
					use_clanMaster.sendPackets(new S_SystemMessage(
							"혈맹 주시 : 혈맹 주시 신청이 거절됨."));
					use_clanMaster.set주시아이디(0);
					target_clanMaster.set주시아이디(0);
				} else if (c == 1) {
					target_clan.addMarkSeeList(use_clan.getClanName());
					use_clan.addMarkSeeList(target_clan.getClanName());
					ClanTable.getInstance().insertObserverClan(
							target_clan.getClanId(), use_clan.getClanName());
					ClanTable.getInstance().insertObserverClan(
							use_clan.getClanId(), target_clan.getClanName());
					for (L1PcInstance tp : target_clan.getOnlineClanMember()) {
						tp.sendPackets(new S_문장주시(pc.getClan(), 2), true);
						tp.sendPackets(new S_문장주시(pc.getClan(), 0), true);
						tp.sendPackets(
								new S_ServerMessage(3360, use_clan
										.getClanName()), true);
					}
					for (L1PcInstance tp : use_clan.getOnlineClanMember()) {
						tp.sendPackets(new S_문장주시(pc.getClan(), 2), true);
						tp.sendPackets(new S_문장주시(pc.getClan(), 0), true);
						tp.sendPackets(
								new S_ServerMessage(3360, target_clan
										.getClanName()), true);
					}
					use_clanMaster.set주시아이디(0);
					target_clanMaster.set주시아이디(0);
				}

			case 2936:// 희미한 기억구슬
				c = readC();
				if (c == 0) {
				} else if (c == 1) {
					int size = pc.getBookMarkSize();
					L1ItemInstance item = pc.getInventory().getItem(pc.구슬아이템);
					if (item.getItemId() == 60084) {
						int itemsize = L1BookMark.ItemBookmarkChehck(pc.구슬아이템);
						if (size + itemsize > pc.getBookmarkMax()) {
							pc.sendPackets(new S_ServerMessage(2961, ""
									+ (size - pc.getBookmarkMax() + itemsize)),
									true);
							pc.구슬아이템 = 0;
							return;
						}
						if (L1BookMark.ItemBookmarkLoad(pc, pc.구슬아이템))
							pc.getInventory().removeItem(item, 1);
					} else {
						if (size + 29 > pc.getBookmarkMax()) {
							pc.sendPackets(new S_ServerMessage(2961, ""
									+ (size - pc.getBookmarkMax() + 30)), true);
							pc.구슬아이템 = 0;
							return;
						}
						if (L1BookMark.아이템기억(pc, 29))
							pc.getInventory().removeItem(item, 1);
					}
				}
				pc.구슬아이템 = 0;
				break;
			case 3016: // 신비한 기억구슬
				c = readC();
				if (c == 0) {
				} else if (c == 1) {
					int size2 = pc.getBookMarkSize();
					if (size2 + 8 > pc.getBookmarkMax()) {
						pc.sendPackets(new S_ServerMessage(2961, ""
								+ (size2 - pc.getBookmarkMax() + 8)), true);
						return;
					}
					L1ItemInstance item2 = pc.getInventory().getItem(pc.구슬아이템);
					if (L1BookMark.아이템기억(pc, 8))
						pc.getInventory().removeItem(item2, 1);
				}
				pc.구슬아이템 = 0;
				break;
			/*
			 * case 2935: //케플리샤 기억저장구슬 c = readC(); if(c == 0){ }else if(c ==
			 * 1){ int size3 = pc.getBookMarkSize(); if (size3 <= 0) {
			 * pc.sendPackets(new S_ServerMessage(2963, ""), true); return; }
			 * L1ItemInstance item3 = pc.getInventory().getItem(pc.구슬아이템);
			 * pc.getInventory().removeItem(item3, 1);
			 * 
			 * L1ItemInstance item4 = ItemTable.getInstance().createItem(60084);
			 * item4.setCount(1); item4.setIdentified(true);
			 * item4.setCreaterName(pc.getName());
			 * pc.getInventory().storeItem(item4); //L1ItemInstance item4 =
			 * pc.getInventory().storeItem(60084, 1);
			 * L1BookMark.ItemaddBookmark(pc, item4.getId()); } pc.구슬아이템 = 0;
			 * break;
			 */
			// case 3055: // 찾을 상인 이름
			// pc.상인찾기 = false;
			// break;
			case 1565: // 드키 등록할지
				c = readC();
				if (c == 0) {
				} else if (c == 1) {
					if (checkdragonkey(pc)) {
						L1ItemInstance dragonkey = pc.getInventory().getItem(
								pc.드키등록체크id);
						// L1ItemInstance dragonkey =
						// pc.getInventory().findItemId(L1ItemId.DRAGON_KEY);
						BoardTable.getInstance().writeDragonKey(pc, dragonkey,
								currentTime(), 4212014);
						pc.sendPackets(new S_ServerMessage(1567), true);
						L1World.getInstance()
								.broadcastServerMessage(
										"강철 길드 난쟁이: 마법사 게렝님께서 방금 아덴 대륙에 드래곤 키가 나타났다고 하십니다. 선택 받은 드래곤 슬레이어에게 영광과 축복을!");
					}
				}
				pc.드키등록체크id = 0;
				break;
			case 1906: // 혈맹 혈전 중 탈퇴 리뉴얼
				c = readC();
				if (pc.getClanid() > 0) {
					if (c == 0) { // 임의 탈퇴
					} else if (c == 1) { // 동의 요청

						pc.sendPackets(new S_SystemMessage(
								"군주에게 혈맹 탈퇴에 대한 승인을 요청 중입니다. 잠시 기다려 주세요."));// 요청중입니다
																			// 잠시
																			// 기다려줘
						L1Clan clan = L1World.getInstance().getClan(
								pc.getClanname());
						L1PcInstance cl = L1World.getInstance().getPlayer(
								clan.getLeaderName());
						if (cl != null) {
							cl.sendPackets(new S_Message_YN(1908, pc.getName()));// 탈퇴
																					// 신청하였습니다.
																					// 군주한테
																					// 보내기
							cl.setTempID(pc.getId());
						} else {
							boolean ck = false;
							String sm = pc.getClan().getClanSubPrince();
							if (sm != null) {
								L1PcInstance subM = L1World.getInstance()
										.getPlayer(sm);
								if (subM != null) {
									subM.sendPackets(new S_Message_YN(1908, pc
											.getName()));// 탈퇴 신청하였습니다. 군주한테 보내기
									subM.setTempID(pc.getId());
									ck = true;
								}
							}
							if (!ck)
								pc.sendPackets(new S_Message_YN(1914, null)); // 군주/부군주
																				// 미접속
																				// 임의
																				// 탈퇴
																				// 하겠냐?
						}
					}
				}
				break;
			case 1908: // 군주 탈퇴 신청 처리하기
				c = readC();
				L1PcInstance tpc = (L1PcInstance) L1World.getInstance()
						.findObject(pc.getTempID());
				pc.setTempID(0);
				if (tpc != null && pc.getClanid() > 0) {
					if (c == 0) { // 거부
						pc.sendPackets(new S_ServerMessage(1910, tpc.getName())); // %0의
																					// 혈맹
																					// 탈퇴를
																					// 거부
																					// 하였습니다.
						tpc.sendPackets(new S_Message_YN(1901, null)); // 거부했다
																		// 임의 탈퇴
																		// 할래?
					} else if (c == 1) { // 승인
						leaveClan(tpc);
						pc.sendPackets(new S_ServerMessage(1909, tpc.getName())); // %0의
																					// 혈맹
																					// 탈퇴가
																					// 이루어졌습니다.
						tpc.sendPackets(new S_ServerMessage(1900, null)); // 군주가
																			// 혈맹
																			// 탈퇴의
																			// 승인을
																			// 동의
																			// 하였습니다.
					}
				}
			case 1914: // 군주/부군주 미접속 탈퇴 하겠냐?
				c = readC();
				if (pc.getClanid() > 0) {
					if (c == 1) {
						pc.sendPackets(new S_Message_YN(1915, null)); // 진짜 탈퇴
																		// 할거?
					}
				}
				break;
			case 1901: // 군주 거부했음 임의 탈퇴 할래?
			case 1915: // 정말로 임의 탈퇴 하겠냐?
				c = readC();
				if (pc.getClanid() > 0) {
					if (c == 1) {
						pc.sendPackets(new S_SystemMessage(pc.getClanname()
								+ " 혈맹에서 임의 탈퇴 되었습니다. 3시간의 혈맹 가입 제한이 걸리게 됩니다."));
						leaveClan(pc);
						if (leaveNameCK == null) {
							leaveNameCK = new ClanLeave_joinNameCK();
							GeneralThreadPool.getInstance()
									.execute(leaveNameCK);
						}
						leaveNameCK.add(pc.getName(),
								System.currentTimeMillis() + (60000 * 60 * 1));
					}
				}
				break;
			case 97: // %0가 혈맹에 가입했지만은 있습니다. 승낙합니까? (Y/N)
				c = readC();
				L1PcInstance joinPc = (L1PcInstance) L1World.getInstance()
						.findObject(pc.getTempID());
				pc.setTempID(0);
				if (joinPc != null) {
					if (c == 0) { // No
						joinPc.sendPackets(new S_ServerMessage(96, pc.getName())); // \f1%0은
																					// 당신의
																					// 요청을
																					// 거절했습니다.
					} else if (c == 1) { // Yes
						String clanName = pc.getClanname();
						L1Clan clan = L1World.getInstance().getClan(clanName);
						if (clan != null) {
							혈맹가입(pc, joinPc, clan);
						}
					}
				}
				break;

			case 217: // %0혈맹의%1가 당신의 혈맹과의 전쟁을 바라고 있습니다. 전쟁에 응합니까? (Y/N)
			case 221: // %0혈맹이 항복을 바라고 있습니다. 받아들입니까? (Y/N)
			case 222: // %0혈맹이 전쟁의 종결을 바라고 있습니다. 종결합니까? (Y/N)
				c = readC();
				L1PcInstance enemyLeader = (L1PcInstance) L1World.getInstance()
						.findObject(pc.getTempID());
				if (enemyLeader == null) {
					return;
				}
				pc.setTempID(0);
				String clanName = pc.getClanname();
				String enemyClanName = enemyLeader.getClanname();
				if (c == 0) { // No
					if (i == 217) {
						enemyLeader.sendPackets(new S_ServerMessage(236,
								clanName)); // %0혈맹이 당신의 혈맹과의 전쟁을 거절했습니다.
					} else if (i == 221 || i == 222) {
						enemyLeader.sendPackets(new S_ServerMessage(237,
								clanName)); // %0혈맹이 당신의 제안을 거절했습니다.
					}
				} else if (c == 1) { // Yes
					if (i == 217) {
						L1War war = new L1War();
						war.handleCommands(2, enemyClanName, clanName); // 모의전
																		// 개시
					} else if (i == 221 || i == 222) {
						for (L1War war : L1World.getInstance().getWarList()) { // 전쟁
																				// 리스트를
																				// 취득
							if (war.CheckClanInWar(clanName)) { // 자크란이 가고 있는
																// 전쟁을 발견
								if (i == 221) {
									war.SurrenderWar(enemyClanName, clanName); // 항복
								} else if (i == 222) {
									war.CeaseWar(enemyClanName, clanName); // 종결
								}
								break;
							}
						}
					}
				}
				break;

			case 223: // %0%s 동맹을 원합니다. 받아들이시겠습니까? (Y/N)
				c = readC();
				L1PcInstance allianceLeader = (L1PcInstance) L1World
						.getInstance().findObject(pc.getTempID());
				if (allianceLeader == null)
					return;
				pc.setTempID(0);
				int PcClanId = pc.getClanid();
				int TargetClanId = allianceLeader.getClanid();
				String PcClanName = pc.getClanname();
				String TargetClanName = allianceLeader.getClanname();
				L1Clan PcClan = L1World.getInstance().getClan(PcClanName);
				L1Clan TargegClan = L1World.getInstance().getClan(
						TargetClanName);
				if (c == 1) { // Yes
					PcClan.addAlliance(TargetClanId);
					// PcClan.setAlliance(TargetClanId);
					// TargegClan.setAlliance(PcClanId);
					TargegClan.addAlliance(PcClanId);
					ClanTable.getInstance().updateClan(PcClan);
					ClanTable.getInstance().updateClan(TargegClan);
					pc.sendPackets(new S_ServerMessage(1200, TargetClanName));// %0
																				// 혈맹이
																				// 동맹에
																				// 가입되었습니다.
					allianceLeader.sendPackets(new S_ServerMessage(224,
							PcClanName, TargetClanName));// %0 혈맹과 %1 혈맹이 동맹을
															// 맺었습니다.
				} else if (c == 0) { // No

					pc.sendPackets(new S_SystemMessage("상대가 동맹을 거절했습니다."));// 상대가
																			// 동맹을
																			// 거절했습니다.
				}

			case 1210: // 정말로 동맹을 탈퇴하시겠습니까? (Y/N)
				if (readC() == 1) {
					if (pc.getClan() != null && pc.getClan().AllianceSize() > 0) {
						for (int clanid : pc.getClan().Alliance()) {
							if (clanid == 0)
								continue;
							L1Clan clan = L1World.getInstance().getClan(clanid);
							if (clan == null)
								continue;
							for (L1PcInstance tempPc : clan
									.getOnlineClanMember()) {
								tempPc.sendPackets(new S_ServerMessage(1204, pc
										.getClan().getClanName()));
							}
							clan.removeAlliance(pc.getClanid());
							ClanTable.getInstance().updateClan(clan);
						}
					}
					pc.sendPackets(new S_SystemMessage(pc.getClanname()
							+ " 혈맹이 동맹을 탈퇴 하였습니다."));
					pc.getClan().AllianceDelete();
					ClanTable.getInstance().updateClan(pc.getClan());
				}
				break;
			case 252: // %0%s가 당신과 아이템의 거래를 바라고 있습니다. 거래합니까? (Y/N)
				c = readC();
				if (pc.getTradeID() == 0 || pc.getTradeReady())
					return;
				L1Object trading_partner = L1World.getInstance().findObject(
						pc.getTradeID());
				if (trading_partner != null) {
					if (trading_partner instanceof L1PcInstance) {
						L1PcInstance target = (L1PcInstance) trading_partner;
						if (target.getTradeReady())
							return;
						if (c == 0) { // No
							target.sendPackets(new S_ServerMessage(253, pc
									.getName())); // %0%d는 당신과의 거래에 응하지 않았습니다.
							pc.setTradeID(0);
							target.setTradeID(0);
							pc.setTradeReady(false);
							target.setTradeReady(false);
						} else if (c == 1) { // Yes
							if (pc.getLocation().getTileLineDistance(
									new Point(target.getX(), target.getY())) > 20) {
								pc.sendPackets(new S_SystemMessage(
										"대상과 거리가 너무 멀어 거래를 할 수 없습니다."));

								pc.setTradeID(0);
								target.setTradeID(0);
								pc.setTradeReady(false);
								target.setTradeReady(false);
								return;
							}

							pc.sendPackets(new S_Trade(target.getName()));
							target.sendPackets(new S_Trade(pc.getName()));
							pc.setTradeReady(true);
							target.setTradeReady(true);
						}
					} else if (trading_partner instanceof L1BuffNpcInstance) {
						L1BuffNpcInstance target = (L1BuffNpcInstance) trading_partner;
						if (c == 0) { // No
							pc.setTradeID(0);
							target.setTradeID(0);
							pc.setTradeReady(false);
						} else if (c == 1) { // Yes
							pc.sendPackets(new S_Trade(target.getName()));
							pc.setTradeReady(true);
							target.setTradeID(pc.getId()); // 상대의 오브젝트 ID를 보존해
															// 둔다
						}
					} else if (trading_partner instanceof L1NpcShopInstance) {
						L1NpcShopInstance target = (L1NpcShopInstance) trading_partner;
						if (c == 0) { // No
							pc.setTradeID(0);
							pc.setTradeReady(false);
						} else if (c == 1) { // Yes
							target.Npc_trade = true;
							pc.setTradeReady(true);
							pc.sendPackets(new S_Trade(target.getName()));
						}
					} else if (trading_partner instanceof GambleInstance) {
						GambleInstance target = (GambleInstance) trading_partner;
						if (c == 0) { // No
							pc.setTradeID(0);
							pc.setTradeReady(false);
						} else if (c == 1) { // Yes
							if (target.getTileLineDistance(pc) > 1) {
								pc.setTradeID(0);
								pc.setTradeReady(false);
								pc.sendPackets(new S_SystemMessage(
										"거리가 멀어 수락 할 수 없습니다."));
								return;
							}
							target.Npc_trade = true;
							pc.setTradeReady(true);
							pc.sendPackets(new S_Trade(target.getName()));
						}
					}

				}
				break;

			case 321: // 또 부활하고 싶습니까? (Y/N)
				c = readC();
				L1PcInstance resusepc1 = (L1PcInstance) L1World.getInstance().findObject(pc.getTempID());
				pc.setTempID(0);
				if (resusepc1 != null) { // 부활 스크롤
					if (c == 0) { // No
						;
					} else if (c == 1) { // Yes
						if (pc.isInParty()) {// 파티추가
							if (pc.isDead()) {
								pc.getParty().refresh(pc);
							}
						}
						pc.sendPackets(new S_SkillSound(pc.getId(), '\346'));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), '\346'));
						pc.resurrect(pc.getMaxHp() / 2);
						pc.setCurrentHp(pc.getMaxHp() / 2);
						// pc.startHpRegeneration();
						// pc.startMpRegeneration();
						pc.startMpRegenerationByDoll();
						pc.sendPackets(new S_Resurrection(pc, resusepc1, 0));
						Broadcaster.broadcastPacket(pc, new S_Resurrection(pc, resusepc1, 0));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
					}
				}
				break;

			case 322: // 또 부활하고 싶습니까? (Y/N)
				c = readC();
				L1PcInstance resusepc2 = (L1PcInstance) L1World.getInstance()
						.findObject(pc.getTempID());
				pc.setTempID(0);
				if (resusepc2 != null) { // 축복된 부활 스크롤, 리자레크션, 그레이타리자레크션
					if (c == 0) { // No
						;
					} else if (c == 1) { // Yes
						if (pc.isInParty()) {// 파티추가
							if (pc.isDead()) {
								pc.getParty().refresh(pc);
							}
						}
						pc.sendPackets(new S_SkillSound(pc.getId(), '\346'));
						Broadcaster.broadcastPacket(pc,
								new S_SkillSound(pc.getId(), '\346'));
						pc.resurrect(pc.getMaxHp());
						pc.setCurrentHp(pc.getMaxHp());
						// pc.startHpRegeneration();
						// pc.startMpRegeneration();
						pc.startMpRegenerationByDoll();
						pc.startMpRegenerationByDoll();
						pc.sendPackets(new S_Resurrection(pc, resusepc2, 0));
						Broadcaster.broadcastPacket(pc, new S_Resurrection(pc,
								resusepc2, 0));
						pc.sendPackets(new S_CharVisualUpdate(pc));
						Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(
								pc));
						// EXP 로스트 하고 있는, G-RES를 걸 수 있던, EXP 로스트 한 사망
						// 모두를 채우는 경우만 EXP 복구
						if (pc.getExpRes() == 1 && pc.isGres()
								&& pc.isGresValid()) {
							pc.resExp();
							pc.setExpRes(0);
							pc.setGres(false);
						}
					}
				}
				break;

			case 512: // 가의 이름은?
				c = readC(); // ?
				name = readS();
				int houseId = pc.getTempID();
				if (houseId == 0)
					return;
				pc.setTempID(0);
				if (name.length() <= 16) {
					L1House house = HouseTable.getInstance().getHouseTable(
							houseId);
					house.setHouseName(name);
					HouseTable.getInstance().updateHouse(house); // DB에 기입해
				} else {

					pc.sendPackets(new S_SystemMessage("집 이름이 너무 깁니다.")); // 가의
																			// 이름이
																			// 너무
																			// 깁니다.
				}
				break;

			case 622: // 설문
				c = readC();
				 switch (pc.getMsgType()) {

		         
				 //테스트 스폰테스트
				 case L1PcInstance.SPAWN_GIRTAS:


		                pc.setMsgType(0);

		                if (c == 0) {

		                } else if (c == 1) {

		                    // 텔레포트 처리

		                }

		                break;
				 }
				 //테스트
				
				switch (pc.system) {
				case 1:
					pc.system = -1;// 초기화
					if (c == 0) {
					} else if (c == 1) {
						int count = 0;
						int trcount = 0;
						AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(
								pc.dragonmapid);
						int count1 = ar.countLairUser();
						if (pc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.DRAGONBLOOD_A)) {
							pc.sendPackets(new S_SystemMessage(
									"혈흔 버프가 있어 입장 할 수 없습니다."));
							return;
						}
						if (pc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.DRAGONRAID_BUFF)) {
							pc.sendPackets(new S_SystemMessage(
									"드래곤 레이드 마법으로 인해 드래곤 포탈에 입장 할 수 없습니다."));
							return;
						}
						if (count1 > 0) {
							for (L1PcInstance player : L1World.getInstance()
									.getAllPlayers()) {
								if (player.getMapId() == pc.dragonmapid) {
									trcount++;
								}
							}
							if (trcount == 0) {
								for (L1Object npc : L1World.getInstance()
										.getObject()) {
									if (npc instanceof L1MonsterInstance) {
										if (npc.getMapId() == pc.dragonmapid) {
											L1MonsterInstance _npc = (L1MonsterInstance) npc;
											_npc.deleteMe();
										}
									}
								}
								ar.clLairUser();
								ar.setAntaras(false);
								ar.setanta(null);
							}
						}
						for (L1PcInstance player : L1World.getInstance()
								.getAllPlayers()) {
							if (player.getMapId() == pc.dragonmapid) {
								count += 1;
								if (count > 31) {
									pc.sendPackets(new S_SystemMessage(
											"입장 가능 인원수를 초과 하였습니다."));
									return;
								}
							}
						}
						L1Teleport.teleport(pc, 32668, 32675,
								(short) pc.dragonmapid, 5, true);
					}
					break;
				case 2:
					pc.system = -1;// 초기화
					if (c == 0) {
					} else if (c == 1) {
						int count = 0;
						int trcount = 0;
						PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
								pc.dragonmapid);
						int count1 = ar.countLairUser();
						if (pc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.DRAGONBLOOD_P)) {
							pc.sendPackets(new S_SystemMessage(
									"혈흔 버프가 있어 입장 할 수 없습니다."));
							return;
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.DRAGONRAID_BUFF)) {
							pc.sendPackets(new S_SystemMessage(
									"드래곤 레이드 마법으로 인해 드래곤 포탈에 입장 할 수 없습니다."));
							return;
						}
						if (count1 > 0) {
							for (L1PcInstance player : L1World.getInstance()
									.getAllPlayers()) {
								if (player.getMapId() == pc.dragonmapid) {
									trcount++;
								}
							}
							if (trcount == 0) {
								for (L1Object npc : L1World.getInstance()
										.getObject()) {
									if (npc instanceof L1MonsterInstance) {
										if (npc.getMapId() == pc.dragonmapid) {
											L1MonsterInstance _npc = (L1MonsterInstance) npc;
											_npc.deleteMe();
										}
									}
								}
								ar.clLairUser();
								ar.setPapoo(false);
								ar.setPaPoo(null);
							}
						}
						for (L1PcInstance player : L1World.getInstance()
								.getAllPlayers()) {
							if (player.getMapId() == pc.dragonmapid) {
								count += 1;
								if (count > 31) {
									pc.sendPackets(new S_SystemMessage(
											"입장 가능 인원수를 초과 하였습니다."));
									return;
								}
							}
						}
						L1Teleport.teleport(pc, 32920, 32672,
								(short) pc.dragonmapid, 5, true);
					}
					break;
				case 3:
					pc.system = -1;// 초기화
					if (c == 0) {
					} else if (c == 1) {
						AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(
								pc.dragonmapid);
						/*
						 * L1Party pcpt = pc.getParty(); if(pcpt == null){
						 * pc.sendPackets(new
						 * S_SystemMessage("당신은 파티 중이 아닙니다.")); return; }
						 * if(pcpt.getLeader().getName() != pc.getName()){
						 * pc.sendPackets(new
						 * S_SystemMessage("파티장만 입장을 할 수 있습니다.")); return; }
						 */
						boolean ok = true;
						int num = 0;
						/*
						 * for (int i1 = 1; i1 < 4 ; i1++){ num --; ok =
						 * ar.Check(i1); if(ok == false){ ar.setParty(pcpt, i1);
						 * System.out.println("파티 등록 "+i1+"번 째 방 등록"); break; }
						 * }
						 */

						num = -1;
						ok = ar.Check(1);
						ar.addMember(pc);
						if (ok == true) {

							// pc.sendPackets(new
							// S_SystemMessage("이미 시작되었습니다."));
							// return;
						} else {
							AntarasRaidTimer art = new AntarasRaidTimer(null,
									ar, num, 10000);// 2분 체크
							art.begin();
							ar.MiniRoom1 = true;
						}
						num *= -1;

						switch (num) {
						case 1:
							if (pc.getMapId() != pc.dragonmapid) {
								return;
							}
							L1Teleport.teleport(pc, 32679, 32803,
									(short) pc.dragonmapid, 5, true);
							break;
						/*
						 * case 1: for (L1PcInstance mem : pcpt.getMembers()) {
						 * if(mem.getMapId() != pc.dragonmapid){ continue; }
						 * L1Teleport.teleport(mem, 32679, 32803, (short)
						 * pc.dragonmapid, 5, true); } break; case 2: for
						 * (L1PcInstance mem : pcpt.getMembers()) {
						 * if(mem.getMapId() != pc.dragonmapid){ continue; }
						 * L1Teleport.teleport(mem, 32935, 32611, (short)
						 * pc.dragonmapid, 5, true); } break; case 3: for
						 * (L1PcInstance mem : pcpt.getMembers()) {
						 * if(mem.getMapId() != pc.dragonmapid){ continue; }
						 * L1Teleport.teleport(mem, 32935, 32803, (short)
						 * pc.dragonmapid, 5, true); } break; case 4: for
						 * (L1PcInstance mem : pcpt.getMembers()) {
						 * if(mem.getMapId() != pc.dragonmapid){ continue; }
						 * L1Teleport.teleport(mem, 32807, 32803, (short)
						 * pc.dragonmapid, 5, true); } break;
						 */
						case 5:
							pc.sendPackets(new S_SystemMessage("이미 시작되었습니다."));
							break;
						}

					}
					break;

				case 4:
					pc.system = -1;// 초기화
					if (c == 0) {
					} else if (c == 1) {
						PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
								pc.dragonmapid);
						// System.out.println(pc.dragonmapid);
						/*
						 * L1Party pcpt = pc.getParty(); if(pcpt == null){
						 * pc.sendPackets(new
						 * S_SystemMessage("당신은 파티 중이 아닙니다.")); return; }
						 * if(pcpt.getLeader().getName() != pc.getName()){
						 * pc.sendPackets(new
						 * S_SystemMessage("파티장만 입장을 할 수 있습니다.")); return; }
						 */
						boolean ok = true;
						int num = 0;
						/*
						 * for (int i1 = 1; i1 < 4 ; i1++){ num --; ok =
						 * ar.Check(i1); if(ok == false){ ar.setParty(pcpt, i1);
						 * System.out.println("파푸 단계루트 파티 등록 "+i1+"번 째 방 등록");
						 * break; } }
						 */
						num = -1;
						ok = ar.Check(1);
						ar.addMember(pc);
						if (ok == true) {
							// pc.sendPackets(new
							// S_SystemMessage("이미 시작되었습니다."));
							// return;
						} else {

							PaPooTimer art = new PaPooTimer(null, ar, num,
									10000);// 2분 체크
							art.begin();
							ar.MiniRoom1 = true;
						}
						num *= -1;
						switch (num) {
						case 1:
							if (pc.getMapId() != pc.dragonmapid) {
								return;
							}
							L1Teleport.teleport(pc, 32759, 32852,
									(short) pc.dragonmapid, 5, true);
							break;
						/*
						 * case 1:
						 * 
						 * for (L1PcInstance mem : pcpt.getMembers()) {
						 * if(mem.getMapId() != pc.dragonmapid){ continue; }
						 * L1Teleport.teleport(mem, 32759, 32852, (short)
						 * pc.dragonmapid, 5, true); } break; case 2: for
						 * (L1PcInstance mem : pcpt.getMembers()) {
						 * if(mem.getMapId() != pc.dragonmapid){ continue; }
						 * 
						 * L1Teleport.teleport(mem, 32759, 32726, (short)
						 * pc.dragonmapid, 5, true); } break; case 3: for
						 * (L1PcInstance mem : pcpt.getMembers()) {
						 * if(mem.getMapId() != pc.dragonmapid){ continue; }
						 * 
						 * L1Teleport.teleport(mem, 32759, 32597, (short)
						 * pc.dragonmapid, 5, true); } break; case 4: for
						 * (L1PcInstance mem : pcpt.getMembers()) {
						 * if(mem.getMapId() != pc.dragonmapid){ continue; }
						 * L1Teleport.teleport(mem, 32952, 32596, (short)
						 * pc.dragonmapid, 5, true); } break;
						 */
						case 5:
							pc.sendPackets(new S_SystemMessage("이미 시작되었습니다."));
							break;
						}

					}
					break;
				// 린드
				case 5:
					pc.system = -1;// 초기화
					if (c == 0) {
					} else if (c == 1) {
						if (pc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.DRAGONBLOOD_L)) {
							pc.sendPackets(new S_SystemMessage(
									"혈흔 버프가 있어 입장 할 수 없습니다."));
							return;
						}
						if (pc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.DRAGONRAID_BUFF)) {
							pc.sendPackets(new S_SystemMessage(
									"드래곤 레이드 마법으로 인해 드래곤 포탈에 입장 할 수 없습니다."));
							return;
						}
						int count = 0;
						for (L1PcInstance player : L1World.getInstance()
								.getAllPlayers()) {
							if (player.getMapId() == pc.dragonmapid) {
								count += 1;
								if (count > 31) {
									pc.sendPackets(new S_SystemMessage(
											"입장 가능 인원수를 초과 하였습니다."));
									return;
								}
							}
						}
						LindRaid.get().in(pc);
					}
					break;
				default:
					pc.system = -1;// 초기화
					if (c == 0)
						L1Question.bad += 1;
					else if (c == 1)
						L1Question.good += 1;
					break;
				}
				break;
			case 630:
				c = readC();
				L1PcInstance fightPc = (L1PcInstance) L1World.getInstance()
						.findObject(pc.getFightId());
				if (c == 0) {
					pc.setFightId(0);
					fightPc.setFightId(0);
					fightPc.sendPackets(new S_ServerMessage(631, pc.getName()));
				} else if (c == 1) {
					fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL,
							fightPc.getFightId(), fightPc.getId()));
					pc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, pc
							.getFightId(), pc.getId()));
				}
				break;

			case 653: // 이혼을 하면(자) 링은 사라져 버립니다.이혼을 바랍니까? (Y/N)
				c = readC();
				L1PcInstance par = (L1PcInstance) L1World.getInstance()
						.findObject(pc.getPartnerId());
				if (c == 0) { // No
					;
				} else if (c == 1) { // Yes
					// if(pc.getInventory().checkItem(40308, 10000000)){//위자료 BY
					// 리미노
					// pc.getInventory().consumeItem(40308, 10000000);
					// pc.sendPackets(new
					// S_SystemMessage("\\fC이혼 위자료로 천만원을 소비했습니다.")); //
					if (pc.getInventory().checkItem(40901))
						pc.getInventory().consumeItem(40901,
								pc.getInventory().countItems(40901));
					if (pc.getInventory().checkItem(40902))
						pc.getInventory().consumeItem(40902,
								pc.getInventory().countItems(40902));
					if (pc.getInventory().checkItem(40903))
						pc.getInventory().consumeItem(40903,
								pc.getInventory().countItems(40903));
					if (pc.getInventory().checkItem(40904))
						pc.getInventory().consumeItem(40904,
								pc.getInventory().countItems(40904));
					if (pc.getInventory().checkItem(40905))
						pc.getInventory().consumeItem(40905,
								pc.getInventory().countItems(40905));
					if (pc.getInventory().checkItem(40906))
						pc.getInventory().consumeItem(40906,
								pc.getInventory().countItems(40906));
					if (par != null) {
						par.sendPackets(new S_SystemMessage(pc.getName()
								+ "님 께서 당신과 이혼을 하였습니다..")); //
						par.setPartnerId(0);
						par.save(); // DB에 캐릭터 정보를 기입한다
					} else {
						phone(pc.getPartnerId());
					}
					pc.setPartnerId(0);
					pc.save(); // DB에 캐릭터 정보를 기입한다
					// }
					// else{
					// pc.sendPackets(new
					// S_SystemMessage("\\fC이혼을 하려면 위자료 천만원이 필요합니다")); //

					// }
				}
				break;

			case 654: // %0%s당신과 결혼 하고 싶어하고 있습니다. %0과 결혼합니까? (Y/N)
				c = readC();
				L1PcInstance partner = (L1PcInstance) L1World.getInstance()
						.findObject(pc.getTempID());
				pc.setTempID(0);
				if (partner != null) {
					if (c == 0) { // No
						partner.sendPackets(new S_ServerMessage(656, pc
								.getName())); // %0%s는 당신과의 결혼을 거절했습니다.
					} else if (c == 1) { // Yes
						pc.setPartnerId(partner.getId());
						pc.save();

						pc.sendPackets(new S_SystemMessage(
								"두 사람의 결혼이 모든 사람의 축복속에 이루어졌습니다.")); // 모두의 축복
																	// 중(안)에서, 두
																	// 명의 결혼을
																	// 했습니다.
						pc.sendPackets(new S_ServerMessage(655, partner
								.getName())); // 축하합니다! %0과 결혼했습니다.
						pc.sendPackets(new S_SkillSound(pc.getId(), 2059));
						Broadcaster.broadcastPacket(pc,
								new S_SkillSound(pc.getId(), 2059));
						partner.setPartnerId(pc.getId());
						partner.save();
						partner.sendPackets(new S_ServerMessage(790)); // 모두의 축복
																		// 중(안)에서,
																		// 두 명의
																		// 결혼을
																		// 했습니다.
						partner.sendPackets(new S_ServerMessage(655, pc
								.getName())); // 축하합니다! %0과 결혼했습니다.
						partner.sendPackets(new S_SkillSound(partner.getId(),
								2059));
						Broadcaster.broadcastPacket(partner, new S_SkillSound(
								partner.getId(), 2059));
					}
				}
				break;

			// 콜 크란
			case 729: // 혈맹원이 당신을 텔레포트 시키려고 하고 있습니다. 응합니까? (Y/N)
				c = readC();
				if (c == 0) {
				} else if (c == 1) { // Yes
					callClan(pc);
				}
				break;

			case 738:// 경험치를 회복하려면%0의 아데나가 필요합니다. 경험치를 회복합니까?
				c = readC();
				if (c == 0) {
				} else if (c == 1 && pc.getExpRes() == 1) { // Yes
					// int cost = readD();
					int cost = 0;
					int level = pc.getLevel();
					int lawful = pc.getLawful();

					if (level < 45) {
						cost = level * level * 50;
					} else {
						cost = level * level * 100;
					}

					if (lawful >= 0) {
						cost = (cost / 2);
					}

					cost *= 2;

					if (cost >= 200000000 || cost < 0)
						return;

					if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
						pc.resExpToTemple();
						pc.setExpRes(0);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다."),
								true);// \f1아데나가 부족합니다.
					}

				}
				break;
			case 2551:// 구호증서를 이용해 회복
				c = readC();
				if (c == 0) {
				} else if (c == 1 && pc.getExpRes() == 1) { // Yes
					if (pc.getInventory().consumeItem(600256, 1)) {
						pc.resExpTo구호();
						pc.setExpRes(0);
					} else {
						pc.sendPackets(new S_SystemMessage("구호 증서가 필요 합니다."),
								true);// \f1아데나가 부족합니다.
					}
				}
				break;

			case 951: // 채팅 파티 초대를 허가합니까? (Y/N)
				c = readC();
				L1PcInstance chatPc = (L1PcInstance) L1World.getInstance()
						.findObject(pc.getPartyID());
				pc.setPartyID(0);
				if (chatPc != null) {
					if (c == 0) { // No
						chatPc.sendPackets(new S_ServerMessage(423, pc
								.getName())); // %0가 초대를 거부했습니다.
					} else if (c == 1) { // Yes
						if (chatPc.isInChatParty()) {
							if (chatPc.getChatParty().isVacancy()
									|| chatPc.isGm()) {
								chatPc.getChatParty().addMember(pc);
							} else {
								chatPc.sendPackets(new S_ServerMessage(417)); // 더
																				// 이상
																				// 파티
																				// 멤버를
																				// 받아들일
																				// 수
																				// 없습니다.
							}
						} else {
							L1ChatParty chatParty = new L1ChatParty();
							chatParty.addMember(chatPc);
							chatParty.addMember(pc);
							chatPc.sendPackets(new S_ServerMessage(424, pc
									.getName())); // %0가 파티에 들어갔습니다.
						}
					}
				}
				break;

			case 953: // 파티 초대를 허가합니까? (Y/N)
			case 954:
				c = readC();
				L1PcInstance target = (L1PcInstance) L1World.getInstance().findObject(pc.getPartyID());
				pc.setPartyID(0);
				if (target != null) {
					if (c == 0) { // No
						target.sendPackets(new S_ServerMessage(423, pc.getName())); // %0가 초대를 거부했습니다.
					} else if (c == 1) { // Yes
						/** 배틀존 **/
						if (target.getMapId() == 5153 || target.getMapId() == 5001 || pc.getMapId() == 5153 || pc.getMapId() == 5001) {
							target.sendPackets(new S_ServerMessage(423, pc.getName())); // %0가 초대를 거부했습니다.
							return;
						}

						if (target.isInParty()) { // 초대주가 파티중
							if (target.getParty().isVacancy() || target.isGm()) { // 파티에 빈 곳이 있다
								target.getParty().addMember(pc);
							} else { // 파티에 빈 곳이 없다
								target.sendPackets(new S_ServerMessage(417)); // 더 이상 파티 멤버를 받아들일 수 없습니다.
							}
						} else { // 초대주가 파티중이 아니다
							L1Party party = new L1Party();
							party.addMember(target);
							party.addMember(pc);
						}
					}
				}
				break;

			case 1256: // 경기장에 입장하시겠습니까? (Y/N)
				c = readC();
				if (c == 0) {
					miniGameRemoveEnterMember(pc);
				} else if (c == 1) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
						if (pc.isInParty())
							pc.getParty().leaveMember(pc);
						L1SkillUse l1skilluse = new L1SkillUse();
						l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION,
								pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_LOGIN);
						if (pc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.STATUS_DRAGONPERL)) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(
									L1SkillId.STATUS_DRAGONPERL);
							pc.sendPackets(new S_PacketBox(
									S_PacketBox.DRAGONPERL, 0, 0));
							Broadcaster.broadcastPacket(pc,
									new S_DRAGONPERL(pc.getId(), 0));
							pc.sendPackets(new S_DRAGONPERL(pc.getId(), 0));
							pc.set진주속도(0);
						}

						if (GhostHouse.getInstance().isEnterMember(pc)) {
							if (GhostHouse.getInstance().isPlayingNow()) {
								pc.sendPackets(new S_ServerMessage(1182));
								return;
							}
							for (L1DollInstance doll : pc.getDollList()) {
								doll.deleteDoll();
							}
							GhostHouse.getInstance().addPlayMember(pc);
							L1Teleport.teleport(pc, 32722, 32830, (short) 5140,
									2, true);
						} else if (PetRacing.getInstance().isEnterMember(pc)) {
							if (PetRacing.getInstance().isPlay()) {
								pc.sendPackets(new S_ServerMessage(1182));
								return;
							}
							for (L1DollInstance doll : pc.getDollList()) {
								doll.deleteDoll();
							}
							pc.setPetRacing(true);
							PetRacing.getInstance().removeEnterMember(pc);
							PetRacing.getInstance().addPlayMember(pc);
							L1Teleport.teleport(pc, 32768, 32848, (short) 5143,
									5, true);
						}
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다."));// \f1아데나가
																				// 부족합니다.
						miniGameRemoveEnterMember(pc);
					}
				}
				break;

			case 1268: // 데스매치에 입장하시겠습니까? (Y/N)
				c = readC();
				if (c == 0)
					DeathMatch.getInstance().giveBackAdena(pc);
				else if (c == 1)
					DeathMatch.getInstance().addPlayMember(pc);
				break;
			case 479: // 어느 능력치를 향상시킵니까? (str, dex, int, con, wis, cha)
				if (readC() == 1) {
					String s = readS();
					final int BONUS_ABILITY = pc.getAbility().getBonusAbility();

					if (!(pc.getLevel() - 50 > BONUS_ABILITY))
						return;
					if (s.toLowerCase().equals("str".toLowerCase())) {
					 if(pc.getLevel() <= 89){
						if (pc.getAbility().getStr() < 50) {
							pc.getAbility().addStr((byte) 1); // 소의 STR치에+1
							pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save(); // DB에 캐릭터 정보를 기입한다
						} else {
							pc.sendPackets(new S_SystemMessage(
									"한 능력치의 최대값은 50 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																				// 능력치의
																				// 최대치는
																				// 25입니다.
																				// 다른
																				// 능력치를
																				// 선택해
																				// 주세요
						}
					}
					 else if(pc.getLevel() >= 90){
						 if (pc.getAbility().getStr() < 55) {
								pc.getAbility().addStr((byte) 1); // 소의 STR치에+1
								pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
								pc.sendPackets(new S_OwnCharStatus2(pc));
								pc.sendPackets(new S_CharVisualUpdate(pc));
								pc.save(); // DB에 캐릭터 정보를 기입한다
							} else {
								pc.sendPackets(new S_SystemMessage(
										"한 능력치의 최대값은 55 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																					// 능력치의
																					// 최대치는
																					// 25입니다.
																					// 다른
																					// 능력치를
																					// 선택해
																					// 주세요
							}
					 }
					} else if (s.toLowerCase().equals("dex".toLowerCase())) {
					if(pc.getLevel() <= 89){
						if (pc.getAbility().getDex() < 50) {
							pc.getAbility().addDex((byte) 1);
							pc.resetBaseAc();
							pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER,
									pc.get_PlusEr()), true);
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save();
						} else {
							pc.sendPackets(new S_SystemMessage(
									"한 능력치의 최대값은 50 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																				// 능력치의
																				// 최대치는
																				// 25입니다.
																				// 다른
																				// 능력치를
																				// 선택해
																				// 주세요
						}
					}
					else if(pc.getLevel() >= 90){
						if (pc.getAbility().getDex() < 55) {
							pc.getAbility().addDex((byte) 1);
							pc.resetBaseAc();
							pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER,
									pc.get_PlusEr()), true);
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save();
						} else {
							pc.sendPackets(new S_SystemMessage(
									"한 능력치의 최대값은 55 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																				// 능력치의
																				// 최대치는
																				// 25입니다.
																				// 다른
																				// 능력치를
																				// 선택해
																				// 주세요
						}
					}
					
					} else if (s.toLowerCase().equals("con".toLowerCase())) {
					 if(pc.getLevel() <= 89){
						if (pc.getAbility().getCon() < 50) {
							pc.getAbility().addCon((byte) 1);
							pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save();
						} else {
							pc.sendPackets(new S_SystemMessage(
									"한 능력치의 최대값은 50 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																				// 능력치의
																				// 최대치는
																				// 25입니다.
																				// 다른
																				// 능력치를
																				// 선택해
																				// 주세요
						}
					 }
					 else if(pc.getLevel() >= 90){
							if (pc.getAbility().getCon() < 55) {
								pc.getAbility().addCon((byte) 1);
								pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
								pc.sendPackets(new S_OwnCharStatus2(pc));
								pc.sendPackets(new S_CharVisualUpdate(pc));
								pc.save();
							} else {
								pc.sendPackets(new S_SystemMessage(
										"한 능력치의 최대값은 55 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																					// 능력치의
																					// 최대치는
																					// 25입니다.
																					// 다른
																					// 능력치를
																					// 선택해
																					// 주세요
							}
						 }
					} else if (s.toLowerCase().equals("int".toLowerCase())) {
					  if(pc.getLevel() <= 89){
						if (pc.getAbility().getInt() < 50) {
							pc.getAbility().addInt((byte) 1);
							pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save();
						} else {
							pc.sendPackets(new S_ServerMessage(481));
						}
					  }
					  else if(pc.getLevel() >= 90){
							if (pc.getAbility().getInt() < 55) {
								pc.getAbility().addInt((byte) 1);
								pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
								pc.sendPackets(new S_OwnCharStatus2(pc));
								pc.sendPackets(new S_CharVisualUpdate(pc));
								pc.save();
							} else {
								pc.sendPackets(new S_ServerMessage(481));
							}
						  }
					} else if (s.toLowerCase().equals("wis".toLowerCase())) {
					  if(pc.getLevel() <= 89){
						if (pc.getAbility().getWis() < 50) {
							pc.getAbility().addWis((byte) 1);
							pc.resetBaseMr();
							pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save();
						} else {
							pc.sendPackets(new S_SystemMessage(
									"한 능력치의 최대값은 50 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																				// 능력치의
																				// 최대치는
																				// 25입니다.
																				// 다른
																				// 능력치를
																				// 선택해
																				// 주세요
						}
					  }
					  else if(pc.getLevel() >= 90){
							if (pc.getAbility().getWis() < 55) {
								pc.getAbility().addWis((byte) 1);
								pc.resetBaseMr();
								pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
								pc.sendPackets(new S_OwnCharStatus2(pc));
								pc.sendPackets(new S_CharVisualUpdate(pc));
								pc.save();
							} else {
								pc.sendPackets(new S_SystemMessage(
										"한 능력치의 최대값은 55 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																					// 능력치의
																					// 최대치는
																					// 25입니다.
																					// 다른
																					// 능력치를
																					// 선택해
																					// 주세요
							}
						  }
					} else if (s.toLowerCase().equals("cha".toLowerCase())) {
					  if(pc.getLevel() <= 89){
						if (pc.getAbility().getCha() < 50) {
							pc.getAbility().addCha((byte) 1);
							pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save();
						} else {
							pc.sendPackets(new S_SystemMessage(
									"한 능력치의 최대값은 50 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																				// 능력치의
																				// 최대치는
																				// 25입니다.
																				// 다른
																				// 능력치를
																				// 선택해
																				// 주세요
						}
					}
					} else if(pc.getLevel() >= 90){
						if (pc.getAbility().getCha() < 55) {
							pc.getAbility().addCha((byte) 1);
							pc.getAbility().setBonusAbility(BONUS_ABILITY + 1);
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.save();
						} else {
							pc.sendPackets(new S_SystemMessage(
									"한 능력치의 최대값은 55 입니다. 다른 능력치를 선택해 주세요.")); // 하나의
																				// 능력치의
																				// 최대치는
																				// 25입니다.
																				// 다른
																				// 능력치를
																				// 선택해
																				// 주세요
						}
					}

					pc.CheckStatus();
					if (pc.getLevel() >= 51
							&& pc.getLevel() - 50 > pc.getAbility()
									.getBonusAbility()) {
						if ((pc.getAbility().getStr()
								+ pc.getAbility().getDex()
								+ pc.getAbility().getCon()
								+ pc.getAbility().getInt()
								+ pc.getAbility().getWis() + pc.getAbility()
								.getCha()) < 150) {
							int temp = (pc.getLevel() - 50)
									- pc.getAbility().getBonusAbility();
							pc.sendPackets(new S_bonusstats(pc.getId(), temp));
						}
					}
				}
				break;

			default:
				break;
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	// joinPc = 유저, pc = 군주
	public static void 혈맹가입(L1PcInstance pc, L1PcInstance joinPc, L1Clan clan) {
		// try{
		if (pc == null)
			return;
		int maxMember = 0;
		/*
		 * int maxMember = 0; int charisma = 0; if(pc == null)return;
		 * if(pc.getClanRank() == L1Clan.CLAN_RANK_PRINCE ){ charisma =
		 * pc.getAbility().getTotalCha(); if (pc.getLevel() >= 50) { // Lv50 이상
		 * maxMember = charisma * 9; } else if (pc.getLevel() >= 45) { // Lv45
		 * 미만 maxMember = charisma * 6; }else{ maxMember = charisma * 3; }
		 * 
		 * }else if(pc.getClanRank() >= L1Clan.CLAN_RANK_GUARDIAN){ maxMember =
		 * clan.getCrownMaxMember(); } //pc.sendPackets(new
		 * S_SystemMessage("dd "+maxMember)); if(maxMember > 300){ maxMember =
		 * 300; }
		 */
		maxMember = 40;

		if (joinPc.getClanid() == 0) { // 크란미가입
			if (leaveNameCK != null) {
				long ck = leaveNameCK.ck(joinPc.getName());
				if (ck > 0) {
					SimpleDateFormat dateFormat = new SimpleDateFormat(
							"HH시간 mm분");
					String time = dateFormat.format(new Timestamp((ck - System
							.currentTimeMillis()) + (60 * 1000 * 60 * 15)));
					if (pc != null)
						pc.sendPackets(new S_SystemMessage(joinPc.getName()
								+ " 님은 현재 가입 제한이 걸려있는 상태 입니다."));
					joinPc.sendPackets(new S_SystemMessage(time
							+ " 후에 재가입 할 수 있습니다."));
					return;
				}
			}
			if (maxMember <= clan.getClanMemberList().size()) {// clanMembersName.length)
																// { // 빈 곳이 없다
				if (pc != null)
					joinPc.sendPackets(new S_ServerMessage(188, pc.getName())); // %0는
																				// 당신을
																				// 혈맹원으로서
																				// 받아들일
																				// 수가
																				// 없습니다.
				joinPc.sendPackets(new S_SystemMessage("[" + clan.getClanName()
						+ "] 혈맹은\n현재 가입할 수 없습니다."), true);
				return;
			}
			/**클라우디아 혈맹가입*/
			if (joinPc.getClanid() != 0) {
				L1QuestInfo info = joinPc.getQuestList(271);
				if (info != null && info.end_time == 0) {
					info.ck[0] = joinPc.getClanid();
					if (info.ck[0] > 1) {
						info.ck[0] = 1;
					}
					joinPc.sendPackets(new S_QuestTalkIsland(joinPc, 271, info));
				}
			}
			for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
				clanMembers.sendPackets(new S_ServerMessage(94, joinPc
						.getName())); // \f1%0이 혈맹의 일원으로서 받아들여졌습니다.
			}
			joinPc.setClanid(clan.getClanId());
			joinPc.setClanname(clan.getClanName());
			joinPc.setClanRank(L1Clan.CLAN_RANK_PROBATION);
			joinPc.setTitle("");
			joinPc.sendPackets(new S_CharTitle(joinPc.getId(), ""));
			Broadcaster.broadcastPacket(joinPc, new S_CharTitle(joinPc.getId(),""));
			joinPc.setClanJoinDate(new Timestamp(System.currentTimeMillis()));
			try {
				joinPc.save();
			} catch (Exception e) {
				e.printStackTrace();
			} // DB에 캐릭터 정보를 기입한다
			clan.addClanMember(joinPc.getName(), joinPc.getClanRank(),joinPc.getLevel(), joinPc.getType(), joinPc.getMemo(), 
					joinPc.getOnlineStatus(), joinPc);
			joinPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED,0x07, joinPc.getName()), true);
			joinPc.sendPackets(new S_PacketBox(S_PacketBox.WORLDMAP_UNKNOWN1),true);
			joinPc.sendPackets(new S_ClanJoinLeaveStatus(joinPc), true);
			Broadcaster.broadcastPacket(joinPc, new S_ClanJoinLeaveStatus(joinPc));
			joinPc.sendPackets(new S_ReturnedStat(joinPc,S_ReturnedStat.CLAN_JOIN_LEAVE), true);
			Broadcaster.broadcastPacket(joinPc, new S_ReturnedStat(joinPc,S_ReturnedStat.CLAN_JOIN_LEAVE));
			joinPc.sendPackets(new S_ClanWindow(S_ClanWindow.혈마크띄우기, joinPc.getClan().getmarkon()), true);
			joinPc.sendPackets(new S_문장주시(joinPc.getClan(), 2), true);
			ClanTable.getInstance().updateClan(joinPc.getClan());
			if (pc != null) {
				pc.sendPackets(new S_PacketBox(pc,
						S_PacketBox.PLEDGE_REFRESH_PLUS));
				joinPc.sendPackets(new S_ServerMessage(95, clan.getClanName())); // \f1%0
																					// 혈맹에
																					// 가입했습니다.
			}
		} else { // 크란 가입이 끝난 상태(크란 연합)
			if (Config.CLAN_ALLIANCE) {
				if (pc != null)
					changeClan(pc.getNetConnection(), pc, joinPc, maxMember);
			} else {
				joinPc.sendPackets(new S_SystemMessage("이미 혈맹에 가입했습니다.")); // \f1당신은
																			// 벌써
																			// 혈맹에
																			// 가입하고
																			// 있습니다.
			}
		}
		/*
		 * }catch(Exception e){ e.printStackTrace(); }
		 */
	}

	private void leaveClan(L1PcInstance pc) {
		try {
			String player_name = pc.getName();
			String clan_name = pc.getClanname();
			L1Clan clan = L1World.getInstance().getClan(clan_name);
			L1PcInstance clanMember[] = clan.getOnlineClanMember();

			for (int f = 0; f < clanMember.length; f++) {
				clanMember[f].sendPackets(new S_ServerMessage(
						ServerMessage.LEAVE_CLAN, player_name, clan_name)); // \f1%0이
																			// %1혈맹을
																			// 탈퇴했습니다.
			}

			pc.ClearPlayerClanData(clan);
			clan.removeOnlineClanMember(player_name);
			clan.removeClanMember(player_name);

			ClanTable.getInstance().updateClan(clan);

			pc.sendPackets(new S_ReturnedStat(pc,
					S_ReturnedStat.CLAN_JOIN_LEAVE), true);
		} catch (Exception e) {
		}
	}

	private static void changeClan(LineageClient clientthread, L1PcInstance pc,
			L1PcInstance joinPc, int maxMember) {
		int clanId = pc.getClanid();
		String clanName = pc.getClanname();
		L1Clan clan = L1World.getInstance().getClan(clanName);
		int clanNum = clan.getClanMemberList().size();

		int oldClanId = joinPc.getClanid();
		String oldClanName = joinPc.getClanname();
		L1Clan oldClan = L1World.getInstance().getClan(oldClanName);
		int oldClanNum = oldClan.getClanMemberList().size();
		if (clan != null && oldClan != null && joinPc.isCrown()
				&& joinPc.getId() == oldClan.getLeaderId()) {
			if (maxMember < clanNum + oldClanNum) { // 빈 곳이 없다
				joinPc.sendPackets(new S_ServerMessage(188, pc.getName())); // %0는
																			// 당신을
																			// 혈맹원으로서
																			// 받아들일
																			// 수가
																			// 없습니다.
				return;
			}
			L1PcInstance clanMember[] = clan.getOnlineClanMember();
			for (int cnt = 0; cnt < clanMember.length; cnt++) {
				clanMember[cnt].sendPackets(new S_ServerMessage(94, joinPc
						.getName())); // \f1%0이 혈맹의 일원으로서 받아들여졌습니다.
			}

			for (int i = 0; i < oldClan.getClanMemberList().size(); i++) {
				L1PcInstance oldClanMember = L1World.getInstance().getPlayer(
						oldClan.getClanMemberList().get(i).name);
				if (oldClanMember != null) { // 온라인중의 구크란 멤버
					oldClanMember.setClanid(clanId);
					oldClanMember.setClanname(clanName);
					// 혈맹 연합에 가입한 군주는 가디안
					// 군주가 데려 온 혈맹원은 본받아
					if (oldClanMember.getId() == joinPc.getId()) {
						// oldClanMember.setClanRank(L1Clan.CLAN_RANK_GUARDIAN);
						oldClanMember.setClanRank(L1Clan.CLAN_RANK_SUBPRINCE);
					} else {
						oldClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
					}
					try {
						// DB에 캐릭터 정보를 기입한다
						oldClanMember.save();
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
					clan.addClanMember(oldClanMember.getName(),
							oldClanMember.getClanRank(),
							oldClanMember.getLevel(), oldClanMember.getType(),
							oldClanMember.getMemo(),
							oldClanMember.getOnlineStatus(), oldClanMember);
					oldClanMember
							.sendPackets(new S_ServerMessage(95, clanName)); // \f1%0
																				// 혈맹에
																				// 가입했습니다.
				} else { // 오프 라인중의 구크란 멤버
					try {
						L1PcInstance offClanMember = CharacterTable
								.getInstance()
								.restoreCharacter(
										oldClan.getClanMemberList().get(i).name);
						offClanMember.setClanid(clanId);
						offClanMember.setClanname(clanName);
						offClanMember.setClanRank(L1Clan.CLAN_RANK_PROBATION);
						offClanMember.save();
						clan.addClanMember(offClanMember.getName(),
								offClanMember.getClanRank(),
								offClanMember.getLevel(),
								offClanMember.getType(),
								offClanMember.getMemo(),
								offClanMember.getOnlineStatus(), offClanMember);
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
			// 이전혈맹 삭제
			String emblem_file = String.valueOf(oldClanId);
			File file = new File("emblem/" + emblem_file);
			file.delete();
			ClanTable.getInstance().deleteClan(oldClanName);
		}
	}

	private void callClan(L1PcInstance pc) {
		L1PcInstance callClanPc = (L1PcInstance) L1World.getInstance()
				.findObject(pc.getTempID());

		if (callClanPc == null) {
			return;
		}

		boolean isInWarArea = false;
		short mapId = callClanPc.getMapId();
		int castleId = L1CastleLocation.getCastleIdByArea(callClanPc);

		pc.setTempID(0);

		if (!pc.getMap().isEscapable() && !pc.isGm()) {
			pc.sendPackets(new S_ServerMessage(647));
			L1Teleport.teleport(pc, pc.getLocation(), pc.getMoveState()
					.getHeading(), false);
			return;
		}

		if (pc.getId() != callClanPc.getCallClanId()) {
			return;
		}

		if (castleId != 0) {
			isInWarArea = true;
			if (WarTimeController.getInstance().isNowWar(castleId)) {
				isInWarArea = false;
			}
		}

		if (mapId != 0 && mapId != 4 && mapId != 304 || isInWarArea) {
			pc.sendPackets(new S_ServerMessage(547));
			return;
		}

		if (mapId == 4
				&& ((callClanPc.getX() >= 33331 && callClanPc.getX() <= 33341
						&& callClanPc.getY() >= 32430 && callClanPc.getY() <= 32441)
						|| (callClanPc.getX() >= 33258
								&& callClanPc.getX() <= 33267
								&& callClanPc.getY() >= 32396 && callClanPc
								.getY() <= 32407)
						|| (callClanPc.getX() >= 33388
								&& callClanPc.getX() <= 33397
								&& callClanPc.getY() >= 32339 && callClanPc
								.getY() <= 32350) || (callClanPc.getX() >= 33443
						&& callClanPc.getX() <= 33483
						&& callClanPc.getY() >= 32315 && callClanPc.getY() <= 32357))) {
			pc.sendPackets(new S_ServerMessage(547));
			return;
		}

		L1Map map = callClanPc.getMap();
		int locX = callClanPc.getX();
		int locY = callClanPc.getY();
		int heading = callClanPc.getCallClanHeading();
		locX += HEADING_TABLE_X[heading];
		locY += HEADING_TABLE_Y[heading];
		heading = (heading + 4) % 4;

		boolean isExsistCharacter = false;
		L1Character cha = null;
		for (L1Object object : L1World.getInstance().getVisibleObjects(
				callClanPc, 1)) {
			if (object instanceof L1Character) {
				cha = (L1Character) object;
				if (cha.getX() == locX && cha.getY() == locY
						&& cha.getMapId() == mapId) {
					isExsistCharacter = true;
					break;
				}
			}
		}
		if (mapId == 4
				&& ((locX >= 33331 && locX <= 33341 && locY >= 32430 && locY <= 32441)
						|| (locX >= 33258 && locX <= 33267 && locY >= 32396 && locY <= 32407)
						||

						(locX >= 34197 && locX <= 34302 && locY >= 33327 && locY <= 33533)
						|| // 황혼의산맥
						(locX >= 33453 && locX <= 33468 && locY >= 32331 && locY <= 32341)
						|| // 아덴의한국민

						(locX >= 33388 && locX <= 33397 && locY >= 32339 && locY <= 32350) || (locX >= 33464
						&& locX <= 33531 && locY >= 33168 && locY <= 33248)
				/*
				 * (newX2 >= 33443 && newX2 <= 33483 && newY2 >= 32315 && newY2
				 * <= 32357)
				 */) /* && !pc.isGm() */) {
			pc.sendPackets(new S_ServerMessage(627));
			return;
		}
		if (locX == 0 && locY == 0 || !map.isPassable(locX, locY)
				|| isExsistCharacter) {
			pc.sendPackets(new S_ServerMessage(627));
			return;
		}
		L1Teleport.teleport(pc, locX, locY, mapId, heading, true,
				L1Teleport.CALL_CLAN);
	}

	private void phone(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "UPDATE characters SET PartnerID=0 WHERE objid=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setInt(1, id);
			pstm.executeUpdate();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void miniGameRemoveEnterMember(L1PcInstance pc) {
		if (GhostHouse.getInstance().isEnterMember(pc))
			GhostHouse.getInstance().removeEnterMember(pc);
		else if (PetRacing.getInstance().isEnterMember(pc))
			PetRacing.getInstance().removeEnterMember(pc);
	}

	private static ClanLeave_joinNameCK leaveNameCK = null;

	class ClanLeave_joinNameCK implements Runnable {
		private final FastMap<String, Long> list;

		public ClanLeave_joinNameCK() {
			list = new FastMap<String, Long>();
		}

		@Override
		public void run() {
			while (true) {
				try {
					if (list.size() > 0) {
						FastTable<String> sl = new FastTable<String>();
						for (FastMap.Entry<String, Long> e = list.head(), mapEnd = list
								.tail(); (e = e.getNext()) != mapEnd;) {
							if (e.getValue() < System.currentTimeMillis()) {
								sl.add(e.getKey());
							}
						}
						for (String name : sl) {
							list.remove(name);
						}
						Thread.sleep(1000);
					} else {
						Thread.sleep(5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		public void add(String name, long time) {
			list.put(name, time);
		}

		public long ck(String name) {
			long d = 0;
			try {
				d = list.get(name);
			} catch (Exception e) {
			}
			return d;
		}
	}

	/*
	 * private boolean checkdragonkey(L1PcInstance pc){
	 * if(pc.getInventory().checkItem(L1ItemId.DRAGON_KEY)){
	 * if(BoardTable.getInstance().checkExistName(pc.getName(), 4212014)){
	 * S_ServerMessage sm = new S_ServerMessage(1568); pc.sendPackets(sm);// 이미
	 * 등록되어 있어 sm = null; return false; }else{ return true; } }else{
	 * S_ServerMessage sm = new S_ServerMessage(1566); pc.sendPackets(sm);// 드래곤
	 * 키 있어야 해 sm = null; return false; } }
	 */
	private boolean checkdragonkey(L1PcInstance pc) {
		int keyid = pc.드키등록체크id;
		if (keyid == 0)
			return false;
		L1ItemInstance item = pc.getInventory().getItem(keyid);
		if (item != null && item.getItemId() == L1ItemId.DRAGON_KEY) {
			if (BoardTable.getInstance().checkExistkey(keyid, 4212014)) {
				S_ServerMessage sm = new S_ServerMessage(1568);
				pc.sendPackets(sm);// 이미 등록되어 있어
				sm = null;
				return false;
			} else {
				return true;
			}
		} else {
			S_ServerMessage sm = new S_ServerMessage(1566);
			pc.sendPackets(sm);// 드래곤 키 있어야 해
			sm = null;
			return false;
		}
	}

	private static String currentTime() {
		TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(tz);
		int year = cal.get(Calendar.YEAR) - 2000;
		String year2;
		if (year < 10) {
			year2 = "0" + year;
		} else {
			year2 = Integer.toString(year);
		}
		int Month = cal.get(Calendar.MONTH) + 1;
		String Month2 = null;
		if (Month < 10) {
			Month2 = "0" + Month;
		} else {
			Month2 = Integer.toString(Month);
		}
		int date = cal.get(Calendar.DATE);
		String date2 = null;
		if (date < 10) {
			date2 = "0" + date;
		} else {
			date2 = Integer.toString(date);
		}
		return year2 + "/" + Month2 + "/" + date2;
	}

	@Override
	public String getType() {
		return C_ATTR;
	}
}