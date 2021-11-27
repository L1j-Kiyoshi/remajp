/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
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

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.ARMOR_BREAK;
import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.BRAVE_AURA;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_닭고기;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_연어;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_칠면조;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_한우;
import static l1j.server.server.model.skill.L1SkillId.DRAGON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_B;
import static l1j.server.server.model.skill.L1SkillId.FREEZING_BREATH;
import static l1j.server.server.model.skill.L1SkillId.GLOWING_AURA;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.IMMUNE_TO_HARM;
import static l1j.server.server.model.skill.L1SkillId.IRON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.IllUSION_AVATAR;
import static l1j.server.server.model.skill.L1SkillId.NATURES_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.PATIENCE;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import static l1j.server.server.model.skill.L1SkillId.SPECIAL_COOKING;
import static l1j.server.server.model.skill.L1SkillId.SPECIAL_COOKING2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;
import static l1j.server.server.model.skill.L1SkillId.메티스정성스프;
import static l1j.server.server.model.skill.L1SkillId.메티스정성요리;
import static l1j.server.server.model.skill.L1SkillId.메티스축복주문서;
import static l1j.server.server.model.skill.L1SkillId.싸이매콤한라면;
import static l1j.server.server.model.skill.L1SkillId.싸이시원한음료;
import static l1j.server.server.model.skill.L1SkillId.크레이;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.IND;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.CrockSystem;
import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.GhostHouse;
import l1j.server.GameSystem.PetRacing;
import l1j.server.GameSystem.Delivery.DeliverySystem;
import l1j.server.GameSystem.DogFight.DogFight;
import l1j.server.GameSystem.MiniGame.DeathMatch;
import l1j.server.GameSystem.TraningCenter.TraningCenter;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.TimeController.DevilController;
//import l1j.server.server.GidunController;
import l1j.server.server.TimeController.WarTimeController;
/*import l1j.server.server.TimeController.라던Controller;*/
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.CharSoldierTable;
import l1j.server.server.datatables.ClanBlessHuntInfo;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetsSkillsTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.TownTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.gametime.RealTime;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.poison.L1SilencePoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_AGShopSellList;
import l1j.server.server.serverpackets.S_ApplyAuction;
import l1j.server.server.serverpackets.S_AuctionBoardRead;
import l1j.server.server.serverpackets.S_BerryShopSellList;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ClanHistory;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_Deposit;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Drawal;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_HouseMap;
import l1j.server.server.serverpackets.S_INN;
import l1j.server.server.serverpackets.S_Lawful;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NavarWarfare_Ranking;
import l1j.server.server.serverpackets.S_NoTaxShopSellList;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PetList;
import l1j.server.server.serverpackets.S_PetWindow;
import l1j.server.server.serverpackets.S_PremiumShopSellList;
import l1j.server.server.serverpackets.S_PsyShopSellList;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_RetrieveElfList;
import l1j.server.server.serverpackets.S_RetrieveList;
import l1j.server.server.serverpackets.S_RetrievePackageList;
import l1j.server.server.serverpackets.S_RetrievePledgeList;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SMPacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_SelectTarget;
import l1j.server.server.serverpackets.S_SellHouse;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ShopBuyList;
import l1j.server.server.serverpackets.S_ShopSellList;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SoldierBuyList;
import l1j.server.server.serverpackets.S_SoldierGiveList;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TaxRate;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1CharSoldier;
import l1j.server.server.templates.L1House;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.templates.L1Town;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;

//import l1j.server.server.GidunController;

public class C_NPCAction extends ClientBasePacket {
	

	private static final String C_NPC_ACTION = "[C] C_NPCAction";
	private static Logger _log = Logger.getLogger(C_NPCAction.class.getName());
	private static Random _random = new Random(System.nanoTime());

	
	public C_NPCAction(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);
		try {
			int objid = readD();
			String s = readS();
			 //System.out.println("엔피씨 오브젝트 ID : " + objid);
			 //System.out.println(s);
			String s2 = null;
			if (s.equalsIgnoreCase("select") || s.equalsIgnoreCase("map") || s.equalsIgnoreCase("apply")) {
				s2 = readS();
			} else if (s.equalsIgnoreCase("ent")) {
				L1Object obj = L1World.getInstance().findObject(objid);
				if (obj != null && obj instanceof L1NpcInstance) {
					final int PET_MATCH_MANAGER = 80088;
					if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == PET_MATCH_MANAGER) {
						s2 = readS();
					}
				}
			}

			int[] materials = null;
			int[] counts = null;
			int[] createitem = null;
			int[] createcount = null;

			String htmlid = null;
			String[] htmldata = null;

			L1PcInstance pc = client.getActiveChar();
			L1PcInstance target;
			L1Object obj = L1World.getInstance().findObject(objid);

			if (obj != null && obj instanceof L1NpcInstance) {
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				if (!s.startsWith("teleport")
						&& !s.equalsIgnoreCase("sell")
						&& !s.equalsIgnoreCase("buy") // 액션값
						&& npcid != 70798 // 리키
						&& npcid != 4203000
						&& !(npcid >= 100376 && npcid <= 100385)
						&& npcid != 100399 && npcid != 100434) {// 마을버프사
					if (pc.getInventory().calcWeightpercent() >= 90) {
						pc.sendPackets(new S_SystemMessage("무게 게이지 : 무게 게이지 90% 이상 행동에 제약을 받습니다."));
						return;
					}
				}
			}
		//	System.out.println("Entered !!" + s);
			if (s.equalsIgnoreCase("sea harphy morph") || s.equalsIgnoreCase("kelenis girl2")) { // 픽시아이템 변신
				if (pc.픽시아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem(pc.픽시아이템사용id);
					if (item != null && item.getItemId() == 60167) {// 픽시아이템일경우
						int time = polyLawfulTime(pc.getLawful(), 1200, 400);
						L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
						if (poly != null) {
							if (poly.getMinLevel() == 100) {
								pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
							} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
								L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
								pc.getInventory().removeItem(item, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
							}
						} else {
							pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
						}
					}
				}
				pc.sendPackets(new S_NPCTalkReturn(objid, "", htmldata));
				return;
			} else if(s.equalsIgnoreCase("event jin death knight")){
				if (pc.픽시아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem(pc.픽시아이템사용id);
					L1PolyMorph.doPoly(pc, 17273, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					pc.sendPackets(new S_CloseList(pc.getId()), true);
				}
			} else if(s.equalsIgnoreCase("event jin assassin")){
				if (pc.픽시아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem(pc.픽시아이템사용id);
					L1PolyMorph.doPoly(pc, 17274, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					pc.sendPackets(new S_CloseList(pc.getId()), true);
				}
			} else if(s.equalsIgnoreCase("event jin baphomet master")){
				if (pc.픽시아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem(pc.픽시아이템사용id);
					L1PolyMorph.doPoly(pc, 17277, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					pc.sendPackets(new S_CloseList(pc.getId()), true);
				}
			} else if(s.equalsIgnoreCase("event jin lance master")){
				if (pc.픽시아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem(pc.픽시아이템사용id);
					L1PolyMorph.doPoly(pc, 17275, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					pc.sendPackets(new S_CloseList(pc.getId()), true);
				}
			} else if(s.equalsIgnoreCase("event owen")){
				if (pc.픽시아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem(pc.픽시아이템사용id);
					L1PolyMorph.doPoly(pc, 17276, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					pc.sendPackets(new S_CloseList(pc.getId()), true);
				}
			} else if(s.equalsIgnoreCase("event guard spear red")){
				if (pc.픽시아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem(pc.픽시아이템사용id);
					L1PolyMorph.doPoly(pc, 17272, 1800, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					pc.sendPackets(new S_CloseList(pc.getId()), true);
				}
			} else if (s.equalsIgnoreCase("dinos hitter") || s.equalsIgnoreCase("dinos pitcher")) { // 선수  변신
				if (pc.선수아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem( pc.선수아이템사용id);
					if (item != null && item.getItemId() == 60308) {// 픽시아이템일경우
						int time = 1800;
						L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
						if (poly != null) {
							if (poly.getMinLevel() == 100) {
								pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
							} else if (poly.getMinLevel() <= pc.getLevel()
									|| pc.isGm()) {
								L1PolyMorph.doPoly(pc, poly.getPolyId(), time,
										L1PolyMorph.MORPH_BY_ITEMMAGIC);
								pc.getInventory().removeItem(item, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
							}
						} else {
							pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
						}
					}
				}
				pc.sendPackets(new S_NPCTalkReturn(objid, "", htmldata));
				return;
			} else if (s.equalsIgnoreCase("ayoung gunter")
					|| s.equalsIgnoreCase("ayoung joewoo")
					|| s.equalsIgnoreCase("ayoung gilian")
					|| s.equalsIgnoreCase("ayoung bluedica")
					|| s.equalsIgnoreCase("death 80a")
					|| s.equalsIgnoreCase("spearm 80a")
					|| s.equalsIgnoreCase("darkelf 80a")) { // 영웅 80 변신
				if (pc.영웅80변신아이템사용id != 0) {
					L1ItemInstance item = pc.getInventory().getItem(pc.영웅80변신아이템사용id);
					if (item != null && item.getItemId() == 60325) {// 픽시아이템일경우
						int time = 1800;
						L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
						if (poly != null) {
							if (poly.getMinLevel() == 100) {
								pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
							} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
								L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
								pc.getInventory().removeItem(item, 1);
							} else {
								pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
							}
						} else {
							pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한 monster에게는 변신할 수 없습니다.
						}
					}
				}
				pc.sendPackets(new S_NPCTalkReturn(objid, "", htmldata));
				return;
			}
			if (obj != null) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					int difflocx = Math.abs(pc.getX() - npc.getX());
					int difflocy = Math.abs(pc.getY() - npc.getY());

					if (obj instanceof L1PetInstance || obj instanceof L1SummonInstance) {
						if (difflocx > 50 || difflocy > 50) {
							return;
						}
					} else {
						// if (difflocx > 3 || difflocy > 3) {
						if (difflocx > 15 || difflocy > 15) {
							return;
						}
					}
					npc.onFinalAction(pc, s);
				} else if (obj instanceof L1PcInstance) {
					target = (L1PcInstance) obj;
					/*
					 * if
					 * (target.getSkillEffectTimerSet().hasSkillEffect(L1SkillId
					 * .SCALES_EARTH_DRAGON) ||
					 * target.getSkillEffectTimerSet().hasSkillEffect
					 * (L1SkillId.SCALES_FIRE_DRAGON) ||
					 * target.getSkillEffectTimerSet
					 * ().hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)){
					 * target.sendPackets(new S_ServerMessage(1384)); return; }
					 */
					// System.out.println("sssss :"+s);
					if (target.isShapeChange()) {
						L1PolyMorph.handleCommands(target, s);
						target.setShapeChange(false);
					} else if (target.isArchShapeChange()) {
						int time;
						if (target.isArchPolyType() == true) {
							time = polyLawfulTime(pc.getLawful(), 1200, 400);
						} else {
							time = -1;
						}
						L1PolyMorph.ArchPoly(target, s, time);
						target.setArchShapeChange(false);
					} else {
						L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
						if (poly != null || s.equals("none")) {
							if (target.getInventory().checkItem(40088) && usePolyScroll(target, 40088, s)) { }
							if (target.getInventory().checkItem(40096) && usePolyScroll(target, 40096, s)) { }
							if (target.getInventory().checkItem(140088) && usePolyScroll(target, 140088, s)) { }
						}
					}
					return;
				}
			} else {
				return;
				// _log.warning("object not found, oid " + i);
			}
			// int npcid1 = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
			// System.out.println("NPC번호 : " + npcid1 + " / 액션 : " + s);
			//
			L1NpcAction action = NpcActionTable.getInstance().get(s, pc, obj);
			if (action != null) {
				L1NpcHtml result = action.execute(s, pc, obj, readByte());
				if (result != null) {
					pc.sendPackets(new S_NPCTalkReturn(obj.getId(), result));
				}
				return;
			}
			if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100396 && s.equalsIgnoreCase("sell")) // 바칸스
				s = "buy";
			if (s.equalsIgnoreCase("buy")) {
				if (pc.getInventory().calcWeightpercent() >= 100) {
					pc.sendPackets(new S_ServerMessage(270)); // 무게 게이지가 가득찼습니다.
					return;
				}
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (isNpcSellOnly(npc)) {
					return;
				}
				if (npcid == 4221700 || npcid == 4220018 || npcid == 4220000
						|| npcid == 4220001 || npcid == 646414
						|| npcid == 646415 || npcid == 646416
						|| npcid == 4220002 || npcid == 4220003
						|| npcid == 4220700 || npcid == 7000043
						|| npcid == 4500172 || npcid == 4500169
						|| npcid == 100289 || npcid == 100290
						|| npcid == 100387 || npcid == 100725
						|| npcid == 77221 || npcid == 77223
						|| npcid == 70014 || (npcid >= 77227 && npcid <= 77230)) {
					pc.sendPackets(new S_PremiumShopSellList(objid, pc));
					return;
				}
				if (npcid == 4208001) {
					pc.sendPackets(new S_AGShopSellList(objid));
					return;
				}
				if (npcid == 100430 || npcid == 100564) {
					pc.sendPackets(new S_PsyShopSellList(objid), true);
					return;
				}
				if (npcid == 100605) {// 행 베리
					pc.sendPackets(new S_BerryShopSellList(objid), true);
					return;
				}
				/*
				 * if (npcid == 70068 || npcid == 70020 || npcid == 70056 ||
				 * npcid == 70051 //|| npcid == 4500173 || npcid == 70055 ||
				 * npcid == 4213002 || npcid == 70017 || npcid == 4200105) {
				 * pc.sendPackets(new S_NoTaxShopSellList(objid)); return; }
				 */
				/** 특정 엔피씨 이외의 세금 안붙게 **/
				if (npcid == 70030 || npcid == 4500166 || npcid == 4500171 || npcid == 4200102) {
					pc.sendPackets(new S_ShopSellList(objid, pc));
					return;
				} else {
					pc.sendPackets(new S_NoTaxShopSellList(objid, pc));
					return;
				}
			} else if (s.equalsIgnoreCase("sell")) {
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				if (npcid == 70523 || npcid == 70805) {
					htmlid = "ladar2";
				} else if (npcid == 70537 || npcid == 70807) {
					htmlid = "farlin2";
				} else if (npcid == 70525 || npcid == 70804) {
					htmlid = "lien2";
				} else if (npcid == 50527 || npcid == 50505 || npcid == 50519
						|| npcid == 50545 || npcid == 50531 || npcid == 50529
						|| npcid == 50516 || npcid == 50538 || npcid == 50518
						|| npcid == 50509 || npcid == 50536 || npcid == 50520
						|| npcid == 50543 || npcid == 50526 || npcid == 50512
						|| npcid == 50510 || npcid == 50504 || npcid == 50525
						|| npcid == 50534 || npcid == 50540 || npcid == 50515
						|| npcid == 50513 || npcid == 50528 || npcid == 50533
						|| npcid == 50542 || npcid == 50511 || npcid == 50501
						|| npcid == 50503 || npcid == 50508 || npcid == 50514
						|| npcid == 50532 || npcid == 50544 || npcid == 50524
						|| npcid == 50535 || npcid == 50521 || npcid == 50517
						|| npcid == 50537 || npcid == 50539 || npcid == 50507
						|| npcid == 50530 || npcid == 50502 || npcid == 50506
						|| npcid == 50522 || npcid == 50541 || npcid == 50523
						|| npcid == 50620 || npcid == 50623 || npcid == 50619
						|| npcid == 50621 || npcid == 50622 || npcid == 50624
						|| npcid == 50617 || npcid == 50614 || npcid == 50618
						|| npcid == 50616 || npcid == 50615 || npcid == 50626
						|| npcid == 50627 || npcid == 50628 || npcid == 50629
						|| npcid == 50630 || npcid == 50631) {
					String sellHouseMessage = sellHouse(pc, objid, npcid);
					if (sellHouseMessage != null) {
						htmlid = sellHouseMessage;
					}
				} else {
					pc.sendPackets(new S_ShopBuyList(objid, pc));
				}
			} else if (s.equalsIgnoreCase("retrieve")) {
				if (pc.getLevel() >= 5) {
					if (isTwoLogin(pc)) {
						return;
					}

					S_RetrieveList rpl = new S_RetrieveList(objid, pc);
					if (rpl.NonValue) {
						htmlid = "noitemret";
					} else {
						pc.sendPackets(rpl, true);
					}
				}
			} else if (s.equalsIgnoreCase("retrieve-elven")) {
				if (pc.getLevel() >= 5 && pc.isElf()) {
					if (isTwoLogin(pc)) {
						return;
					}
					S_RetrieveElfList rpl = new S_RetrieveElfList(objid, pc);
					if (rpl.NonValue) {
						htmlid = "noitemret";
					} else {
						pc.sendPackets(rpl, true);
					}
				}
			} else if (s.equalsIgnoreCase("retrieve-aib")) {
				if (isTwoLogin(pc)) {
					return;
				}

				if (Config.GAME_SERVER_TYPE == 1) {
					pc.sendPackets(new S_SystemMessage("테스트서버 중에는 패키지 창고를 사용하실 수 없습니다."));
				} else {
					S_RetrievePackageList rpl = new S_RetrievePackageList(objid, pc);
					if (rpl.NonValue) {
						htmlid = "noitemret";
					} else {
						pc.sendPackets(rpl, true);
					}
				}
			} else if (s.equalsIgnoreCase("retrieve-pledge")) {
				if (pc.getLevel() >= 5) {
					if (isTwoLogin(pc)) {
						return;
					}

					if (pc.getClanid() == 0) {
						pc.sendPackets(new S_SystemMessage("혈맹창고를 사용하려면 혈맹이 있어야 합니다."));
						return;
					}
					int rank = pc.getClanRank();
					if (rank != L1Clan.CLAN_RANK_PUBLIC
							&& rank != L1Clan.CLAN_RANK_정예
							&& rank != L1Clan.CLAN_RANK_GUARDIAN
							&& rank != L1Clan.CLAN_RANK_PRINCE) {
						pc.sendPackets(new S_ServerMessage(728));
						return;
					}
					// if (rank != L1Clan.CLAN_RANK_PRINCE &&
					// pc.getTitle().equalsIgnoreCase("")) {
					// pc.sendPackets(new S_ServerMessage(728));
					// return;
					// }

					S_RetrievePledgeList rpl = new S_RetrievePledgeList(objid, pc);
					if (rpl.NonValue) {
						htmlid = "noitemret";
					} else {
						pc.sendPackets(rpl, true);
					}
				}
			} else if (s.equalsIgnoreCase("history")) { // 혈맹 창고 내역
				if (pc.getClanid() > 0) {
					// ClanHistoryTable.getInstance().history(pc);
					pc.sendPackets(new S_ClanHistory(pc), true);
				} else {
					pc.sendPackets(new S_SystemMessage("당신은 혈맹이 없습니다."));
				}
			} else if (s.equalsIgnoreCase("EnterSeller")) {
				htmlid = enterseller(pc);
			/** 펫 관련 엔피시 엑션 정리 */
			/** 펫부활 망각 론다 */
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 72204) {
				try {
					/** 경험치 복구 용 */
					if (s.equalsIgnoreCase("a")) {
						L1PetInstance Pet = (L1PetInstance)pc.getPet();
						/** 펫 소환 안되어있어 진행 불가 */
						if (Pet == null) htmlid = "c_saint4";
						/** 다이 상태가 아니라면 */
						if(Pet.isDeadExp()){
							int Money = 0;
							if(Pet.getLevel() <= 40){
								Money = 250000;
							} else if(Pet.getLevel() <= 50){
								Money = 500000;
							} else {
								Money = Pet.getLevel() * 100000;
							}
							String[] HtmlData = { ""+Money+"" };
							pc.sendPackets(new S_NPCTalkReturn(obj.getId(), "c_saint10", HtmlData), true);
							return;
						} else {
							htmlid = "c_saint5";
						}
					/** 복구 멘트후 복구 */
					}else if (s.equalsIgnoreCase("c")) {
						L1PetInstance Pet = (L1PetInstance)pc.getPet();
						/** 펫 소환 안되어있어 진행 불가 */
						if (Pet == null) htmlid = "c_saint4";
						int Money = 0;
						if(Pet.getLevel() <= 40){
							Money = 250000;
						} else if(Pet.getLevel() <= 50){
							Money = 500000;
						} else {
							Money = Pet.getLevel() * 100000;
						}
						/** 아데나 10만이 있는가 없는가 체크 */
						if(pc.getInventory().consumeItem(40308, Money)){
							int needExp = ExpTable.getNeedExpNextLevel(Pet.getLevel());
							Pet.AddExpPet((int)(needExp * 0.05));
							Pet.setDeadExp(false);

							Broadcaster.broadcastPacket(Pet, new S_DoActionGFX(Pet.getId(), 67 + _random.nextInt(2)), true);
							Broadcaster.broadcastPacket(Pet, new S_SkillSound(Pet.getId(), 3944), true);

							pc.sendPackets(new S_PetWindow(S_PetWindow.DeadExp, Pet), true);
							PetTable.UpDatePet(Pet);
							htmlid = "c_saint3";
						} else {
							htmlid = "c_saint2";
						}
					/** 망각 부분 */
					} else if (s.equalsIgnoreCase("b")) {
						L1PetInstance Pet = (L1PetInstance)pc.getPet();
						/** 펫 소환 안되어있어 진행 불가 */
						if (Pet == null) htmlid = "c_saint7";
						/** 다이상태에서는 망각 불가능 */
						if (Pet.isDead()) htmlid = "c_saint8";
						/** 아데나 10만이 있는가 없는가 체크 */
						if(pc.getInventory().consumeItem(40308, 100000)){
							Pet.setFighting(0);
							Pet.setFriendship(0);
							/** 스텟 정보 초기화 */
							Pet.setHunt(0);
							Pet.setSacred(0);
							Pet.setSurvival(0);
							int BonusPointTemp = 0;
							if(Pet.getLevel() > 50) {
								BonusPointTemp = (5 + (Pet.getLevel() - 50));
							} else {
								BonusPointTemp = (Pet.getLevel() / 10);
							}
							Pet.setBonusPoint(BonusPointTemp);
							Pet.setProduct(true);
							Pet.deletePet();
							PetsSkillsTable.SkillsDelete(Pet.getId());
							L1ItemInstance Item = pc.getInventory().getItem(Pet.getItemObjId());
							pc.sendPackets(new S_PetWindow(Item, Pet, true, false, false, false, true), true);
							htmlid = "c_saint6";
						} else {
							htmlid = "c_saint2";
						}
					}
				} catch (Exception e) { }
			/** 펫 훈련사 관련 세팅 */
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 72201) {
				htmlid = 훈련사(pc, s);
			/** 펫 사냥터 텔 바이올렛 세팅 */
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 72203) {
				htmlid = 바이올렛(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 72218) {
				htmlid = 화말귀환(pc, s);
			} else if ((((L1NpcInstance) obj).getNpcId() == 70760
					|| ((L1NpcInstance) obj).getNpcId() == 100802
					|| ((L1NpcInstance) obj).getNpcId() == 100224)) {// 샤베스
					htmlid = 정령력변경(pc, obj, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 70798) {//
				htmlid = 리키(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100219 || ((L1NpcInstance) obj).getNpcId() == 101046) { //
				htmlid = 수련자관리인(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100804) {//
				htmlid = 쿠루(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 101006) {
				htmlid = 정무(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 4208002) { // 기감 멀린
				htmlid = 기란감옥(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 111112) { // 기감 멀린
				htmlid = 지배자결계(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100722) {// 헤이트
				htmlid = 헤이트(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 102001) {// 지룡의오브
				htmlid = 지룡의오브(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 102002) {// 헤이트
				htmlid = 수룡의오브(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 102003) {// 헤이트
				htmlid = 풍룡의오브(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 102004) {// 헤이트
				htmlid = 화룡의오브(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 60244) {// 헤이트
				htmlid = 에바왕국수로(pc, s);
				/**클라우디아 퀘스트 이동 엔피씨*/
			} else if (((L1NpcInstance) obj).getNpcId() == 60229) {// 헤이트
				htmlid = 피그만(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 60230) {// 헤이트
				htmlid = 스파인(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 60231) {// 헤이트
				htmlid = 그레이브(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 60232) {// 헤이트
				htmlid = 워든(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 60233) {// 헤이트
				htmlid = 도아만(pc, s);
				/**클라우디아 퀘스트 이동 엔피씨*/
			} else if (((L1NpcInstance) obj).getNpcId() == 910106) {//
				htmlid = 지배의균열1층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910107) {//
				htmlid = 지배의균열2층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910108) {//
				htmlid = 지배의균열3층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910109) {//
				htmlid = 지배의균열4층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910110) {//
				htmlid = 지배의균열5층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910111) {//
				htmlid = 지배의균열6층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910112) {//
				htmlid = 지배의균열7층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910113) {//
				htmlid = 지배의균열8층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910114) {//
				htmlid = 지배의균열9층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910115) {//
				htmlid = 지배의균열10층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910116) {//
				htmlid = 지배의균열정상(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910091) {//
				htmlid = 레지(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910092) {//
				htmlid = 오르타무1층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910093) {//
				htmlid = 오르타무2층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910094) {//
				htmlid = 오르타무3층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910095) {//
				htmlid = 오르타무4층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910096) {//
				htmlid = 오르타무5층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910097) {//
				htmlid = 오르타무6층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910098) {//
				htmlid = 오르타무7층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910099) {//
				htmlid = 오르타무8층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910100) {//
				htmlid = 오르타무9층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910101) {//
				htmlid = 오르타무10층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 910102) {//
				htmlid = 오르타무정상층(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 77220) {// 헤이트
				htmlid = 루핀(pc, s);
				/** 염탐꾼 오크 **/
			} else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 60188) {
				htmlid = 염탐꾼오크(pc, s);
				/** 오만의 탑 이동 동상 **/
			} else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 60217) {
				htmlid = 오만의탑이동동상(pc, s);
			} else if (((L1NpcInstance)obj).getNpcTemplate().get_npcId() == 100724) { // 몽섬 에킨스
				if (s.equalsIgnoreCase("d")) { // 촉매: 유니콘의 성장 징표 / 재료: 드래곤의
					// 다이아몬드(1)
					if (pc.getInventory().checkItem(60499, 1) && pc.getInventory().checkItem(437010, 1)) {
						pc.getInventory().consumeItem(60499, 1);
						pc.getInventory().consumeItem(437010, 1);
						pc.getInventory().storeItem(60514, 1);
						에킨스경험치6(pc);
					} else {
						htmlid = "ekins5";
					}
				} else if (s.equalsIgnoreCase("c")) { // 촉매: 유니콘의 성장 징표
					if (pc.getInventory().checkItem(60499, 1)) {
						pc.getInventory().consumeItem(60499, 1);
						pc.getInventory().storeItem(60514, 5);
						에킨스경험치2(pc);
					} else {
						htmlid = "ekins5";
					}
				} else if (s.equalsIgnoreCase("b")) { // 촉매: 성장의 구슬
					if (pc.getInventory().checkItem(60500, 1)) {
						pc.getInventory().consumeItem(60500, 1);
						pc.getInventory().storeItem(60514, 1);
						에킨스경험치6(pc);
					} else {
						htmlid = "ekins5";
					}
				} else if (s.equalsIgnoreCase("a")) { // 촉매: 성장의 구슬 조각
					if (pc.getInventory().checkItem(60499, 1)) {
						pc.getInventory().consumeItem(60499, 1);
						pc.getInventory().storeItem(60514, 1);
						에킨스경험치2(pc);
					} else {
						htmlid = "ekins5";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcId() == 102008) {// 에킨스
				htmlid = 검은전함(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100691) {// 민티스
				htmlid = 민티스(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 15000001) {// 피터
				htmlid = 상탑발록(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100672) {// 킹버몽
				htmlid = 킹버몽(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100645) {// 붉은기사단 전령
				htmlid = 붉은전령(pc, s);
				if (htmlid.equals("evsiege104")) {
					htmldata = new String[50];
					for (int i = 1; i <= 50; i++) {
						Collection<L1Object> list = L1World.getInstance().getVisibleObjects(2300 + i).values();
						int count = 0;
						for (L1Object tempObj : list) {
							if (tempObj instanceof L1PcInstance) {
								count++;
							}
						}
						if (count >= 50) {
							htmldata[i - 1] = "검은 기사단 진영" + i + " (FULL)";
						} else {
							htmldata[i - 1] = "검은 기사단 진영" + i;
						}

					}
				}
			} else if (((L1NpcInstance) obj).getNpcId() >= 100663 && ((L1NpcInstance) obj).getNpcId() <= 100668) {// 기란 투석기
				htmlid = 기란투석기A(pc, s, (L1NpcInstance) obj);
			} else if (((L1NpcInstance) obj).getNpcId() == 100646) {// 붉은기사단 참모
				htmlid = 붉은참모(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 4200009) {// 데이빗
				htmlid = 데이빗(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100643) {// 리들
				htmlid = 리들(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100878) {// 네르바
				htmlid = 네르바(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100879) {// 세이룬
				htmlid = 책더미(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 70028) {// 란달
				htmlid = 란달(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100631) {// 칠흑의 수정
				htmlid = 칠흑의수정(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100630) {// 가웨인
				htmlid = 가웨인(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 70614) {// 안톤
				htmlid = 안톤(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100625) {// 칠흑의 마법 문자
				htmlid = 오림(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100595) {// 픽시상자교환원
				htmlid = 픽시상자교환원(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100594) {// 베테르랑
				htmlid = 베테르랑(pc, ((L1NpcInstance) obj), s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100583) {// 다크엘프 생존자
				htmlid = 다엘생존자(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100546) {// 카너스
				htmlid = 카너스(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100582) {// 수호병사 (나가기)
				htmlid = 훈련기지나가기(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100544) {// 수호병사
				htmlid = 훈련기지이동(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100575
					|| ((L1NpcInstance) obj).getNpcId() == 100576
					|| ((L1NpcInstance) obj).getNpcId() == 100581
					|| ((L1NpcInstance) obj).getNpcId() == 7979) {// 메티스 버프
				htmlid = 메티스버프(pc, s);
//			} else if (((L1NpcInstance) obj).getNpcId() == 7979) {// 메티스 마음
//				htmlid = 메티스마음(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100212) { // 해상전 순위
				htmlid = 해상전순위(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100569) { // 탐사원 조수
				htmlid = 탐사원조수(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 80077) { // 알드란
				htmlid = 알드란(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100563) { // 세실리아
				htmlid = 세실리아(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100561) { // 훈련대장 카이저
				htmlid = 카이저(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100562) { // 보상창고 관리인  미아네스
				htmlid = 미아네스(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100553) { // 비자야
				htmlid = 비자야(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100552) { // 도비와
				htmlid = 도비와(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100410) { // 페오시
				htmlid = 페오시(pc, s);
//			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100415) {
//				if (s.equalsIgnoreCase("lava_giran")) { // 중앙 광장
//					라던입장(pc, 32933, 32772);
//				}
			} else if (((L1NpcInstance) obj).getNpcId() == 100389) { // 반지 슬롯 개방
				htmlid = 스냅퍼(pc, s);

			} else if (((L1NpcInstance) obj).getNpcId() == 100832) { // 엘드나스
				htmlid = 엘드나스(pc, s);

			} else if (((L1NpcInstance) obj).getNpcId() == 100372) { // 땅굴 개미
				htmlid = 땅굴개미(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100371) { // 감시자의 눈
				htmlid = 감시자의눈(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100370) { // 호슈 샌드웜 지역 관찰
				htmlid = 호슈(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100324) { // 사냥터 관리인^노베르
				htmlid = 노베르(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100326) { // 포고령 배포자^시이니
				htmlid = 시이니(pc, s);
			} else if (((L1NpcInstance) obj).getNpcId() == 100325) { // 국왕의 보좌관^노무르
				htmlid = 노무르(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100288) { // 수상한 강화 마법사 피도르
				htmlid = 피도르(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 101042) { // 피아르
				htmlid = 피아르(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 1006311) { // 용의 전령
				htmlid = 마법의문(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100807) { // 용의
				htmlid = 용의전령후오스(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80048) { // 그신버땅
				htmlid = 그신버땅(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80058) { // 욕망버땅
				htmlid = 욕망버땅(pc, s);
				/*
				 * } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId()
				 * == 100214){ // 기초 훈련 교관 htmlid = 기초훈련교관(pc, ((L1NpcInstance)
				 * obj), s); } else if (((L1NpcInstance)
				 * obj).getNpcTemplate().get_npcId() == 100215){ // 토벌 대원 htmlid
				 * = 토벌대원(pc, s); } else if (((L1NpcInstance)
				 * obj).getNpcTemplate().get_npcId() == 100216){ // 드래곤뼈 수집꾼
				 * htmlid = 드래곤뼈수집꾼(pc, s);
				 */
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70908) { // 피어스 (파괴의크로우,이도류 제작)
				htmlid = 피어스(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100213) { // 아툰의 기사단원 (무료변신)
				htmlid = 아툰무료변신(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70017) { // 오림
				htmlid = 오림(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 45000169) { // 렉스아 npc 아이디는 입맛대로 하시구요
				if (pc.getInventory().checkItem(5000038, 5)) {
					pc.getInventory().consumeItem(5000038, 5); // 마력의 결정
					if (pc.isElf()) { // 요정이면
						pc.getInventory().storeItem(11011, 1); // 클래스별 눈덩이 지급
					}
					if (pc.isCrown() || pc.isKnight() || pc.isDragonknight()) {
						pc.getInventory().storeItem(11012, 1);
					}
					if (pc.isIllusionist() || pc.isWizard()) {
						pc.getInventory().storeItem(11013, 1);
					}
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "신비한 눈덩이"), true);
					// pc.sendPackets(new
					// S_SystemMessage("신비한 눈덩이를 획득 하였습니다."));
					htmlid = "kmas_lexa3";
				} else { // 재료가 부족한 경우
					pc.sendPackets(new S_SystemMessage("마력의 결정이 부족합니다."));
					htmlid = "kmas_lexa2";
				}
			/** 인던 입장 엔피씨 체크 */
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5000038) {
				htmlid = 유리에(pc, ((L1NpcInstance) obj).getName(), s);
				// 보조 연구원
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5000039) {
				if (s.equalsIgnoreCase("a")) {
					L1Teleport.teleport(pc, 32574, 32942, (short) 0, 5, true);
					htmlid = "";
				}
				// 벽에 새겨진 마법의 문자
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5000041) {
				if (!pc.getInventory().checkItem(500022, 1)) {
					htmlid = "j_html11";
				} else if (!pc.getInventory().checkItem(41246, 20000)) {
					htmlid = "j_html12";
				} else if (pc.getInventory().consumeItem(500022, 1) && pc.getInventory().consumeItem(41246, 20000)) {
					if (s.equalsIgnoreCase("a")) { // 검 제작 의식
						pc.getInventory().storeItem(41916, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "숨겨진 마족의 무기함(검)"), true);
					} else if (s.equalsIgnoreCase("b")) { // 활 제작 의식
						pc.getInventory().storeItem(41917, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "숨겨진 마족의 무기함(활)"), true);
					} else if (s.equalsIgnoreCase("c")) { // 지팡이 제작 의식
						pc.getInventory().storeItem(41918, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "숨겨진 마족의 무기함(지팡이)"), true);
					} else if (s.equalsIgnoreCase("d")) { // 크로우 제작 의식
						pc.getInventory().storeItem(41920, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "숨겨진 마족의 무기함(크로우)"), true);
					} else if (s.equalsIgnoreCase("e")) { // 체인소드 제작 의식
						pc.getInventory().storeItem(41921, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "숨겨진 마족의 무기함(체인소드)"), true);
					} else if (s.equalsIgnoreCase("f")) { // 키링크 제작 의식
						pc.getInventory().storeItem(41919, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "숨겨진 마족의 무기함(키링크)"), true);
					} else if (s.equalsIgnoreCase("g")) { // 도끼 제작 의식
						pc.getInventory().storeItem(7251, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "숨겨진 마족의 무기함(도끼)"), true);
					}
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5000040) {
				htmlid = hadinEnter(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 101043) {
				htmlid = 오림인던하(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 101044) {
				htmlid = 오림인던중(s, pc);
				// 휴그린트
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 5000094) {
				htmlid = 휴그린트(pc, s);
				/** 여행자 도우미 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100001) {
				if (pc.getLevel() > 65) {
					htmlid = "lowlvno";
				} else {
					htmlid = helper(s, pc);
				}
				/*** 신비한 눈뭉치 ***/
				/*** 눈사람 인형 ***/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 45000170) { // npc 번호 니디스 인형
				htmlid = 니디스(s, pc, obj);
				/*** 눈사람 인형 ***/
			} else

			if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039004) { // 저주받은무녀 사엘 (오색버프)
				if (s.equalsIgnoreCase("A")) {
					pc.getSkillEffectTimerSet().removeSkillEffect(10500);
					pc.getSkillEffectTimerSet().setSkillEffect(10501, 60000);
					L1SkillUse l1skilluse = null;
					l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(pc, 10501, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039005) { // 저주받은무녀 사엘2 (신비한버프)
				if (s.equalsIgnoreCase("A")) {
					pc.getSkillEffectTimerSet().removeSkillEffect(10501);
					pc.getSkillEffectTimerSet().setSkillEffect(10500, 60000);
					L1SkillUse l1skilluse = null;
					l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(pc, 10500, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
				}
				// //////////추가 크레이버프 ///////////////
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212012) { // 크레이
				if (s.equalsIgnoreCase("a")) {
					pc.sendPackets(new S_SkillSound(pc.getId(), 7681));
					pc.broadcastPacket(new S_SkillSound(pc.getId(), 7681));
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.사엘)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.사엘);
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.크레이)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.크레이);
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.군터의조언)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.군터의조언);
					}
					pc.크레이 = true;
					pc.getAC().addAc(-8);
					pc.getResistance().addMr(20);
					pc.addMaxHp(200);
					pc.addMaxMp(100);
					pc.addHpr(3);
					pc.addMpr(3);
					pc.getResistance().addEarth(30);
					pc.addDmgup(3);
					pc.addBowDmgup(3);
					pc.addHitup(10);
					pc.addBowHitup(10);
					pc.addWeightReduction(40);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					pc.sendPackets(new S_OwnCharStatus(pc));
					pc.sendPackets(new S_SPMR(pc), true);
					pc.getSkillEffectTimerSet().setSkillEffect(크레이, 2400 * 1000);
					htmlid = "grayknight2";
				}
				// ////////////추가 크레이버프 ///////////////
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4039009) { // 저주받은무녀 사엘(입구)
				htmlid = 저주받은무녀사엘(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100632) { // 린드비오르 군터
				htmlid = 군터의조언(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3310018) { // 발라카스
				// 데스나이트
                htmlid = 데스나이트버프(pc, s);
				/*** 게렝 쫄병 ***/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 45000174) {
				if (s.equalsIgnoreCase("0")) { // 게렝의행운의 물약 1개 교환
					if (pc.getInventory().checkItem(5000061, 1) && pc.getInventory().checkItem(437017, 2)) {
						pc.getInventory().consumeItem(5000061, 1);
						pc.getInventory().consumeItem(437017, 2);
						pc.getInventory().storeItem(5000059, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "게렝의 전투의 물약"), true);
						htmlid = "gerengfoll2"; // 거래 완료 액션
					} else {
						htmlid = "gerengfoll3"; // 재료가 부족시 액션
					}
				}
				if (s.equalsIgnoreCase("1")) { // 게렝의 행운의 물약 10개 교환
					if (pc.getInventory().checkItem(5000061, 10) && pc.getInventory().checkItem(437017, 20)) {
						pc.getInventory().consumeItem(5000061, 10);
						pc.getInventory().consumeItem(437017, 20);
						pc.getInventory().storeItem(5000059, 10);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "게렝의 전투의 물약 (10)"), true);
						htmlid = "gerengfoll2"; // 거래 완료 액션
					} else {
						htmlid = "gerengfoll3"; // 재료가 부족시 액션
					}
				}
				if (s.equalsIgnoreCase("2")) { // 게렝의행운의 물약 1개 교환
					if (pc.getInventory().checkItem(5000062, 1) && pc.getInventory().checkItem(437017, 2)) {
						pc.getInventory().consumeItem(5000062, 1);
						pc.getInventory().consumeItem(437017, 2);
						pc.getInventory().storeItem(5000060, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "게렝의 행운의 물약"), true);
						htmlid = "gerengfoll2"; // 거래 완료 액션
					} else {
						htmlid = "gerengfoll3"; // 재료가 부족시 액션
					}
				}
				if (s.equalsIgnoreCase("3")) { // 게렝의 행운의 물약 10개 교환
					if (pc.getInventory().checkItem(75003, 10) && pc.getInventory().checkItem(437017, 20)) {
						pc.getInventory().consumeItem(75003, 10);
						pc.getInventory().consumeItem(437017, 20);
						pc.getInventory().storeItem(5000060, 10);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "게렝의 행운의 물약 (10)"), true);
						htmlid = "gerengfoll2"; // 거래 완료 액션
					} else {
						htmlid = "gerengfoll3"; // 재료가 부족시 액션
					}
				}
				/*** 게렝쫄병 ***/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3200015) {
				L1Teleport.teleport(pc, 32827, 32774, (short) 68, 5, true);
				htmlid = "";
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 3200016) {
				L1Teleport.teleport(pc, 32704, 32787, (short) 69, 5, true);
				htmlid = "";
			} else if (s.equalsIgnoreCase("get")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				int npcId = npc.getNpcTemplate().get_npcId();
				if (npcId == 70099 || npcId == 70796) {
					L1ItemInstance item = pc.getInventory() .storeItem(447012, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					pc.getQuest().set_end(L1Quest.QUEST_OILSKINMANT);

					htmlid = "";
				} else if (npcId == 70528 || npcId == 70546 || npcId == 70567
						|| npcId == 70594 || npcId == 70654 || npcId == 70748
						|| npcId == 70774 || npcId == 70799 || npcId == 70815
						|| npcId == 70860) {
					if (pc.getHomeTownId() > 0) {
					} else {
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70012
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70019
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70031
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70054
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70065
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70070
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70075
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70084
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70096) {
				if (s.equalsIgnoreCase("room")) {
					if (pc.getInventory().checkItem(40312)) {
						htmlid = "inn5";
					} else {
						pc.sendPackets(new S_INN(obj.getId(), 20, "inn2", "300"));
						return;
					}
				} else if (s.equalsIgnoreCase("hall")) {
					if (pc.getInventory().checkItem(49312)) {
						htmlid = "inn15";
					} else {
						int c1 = 0;
						try {
							c1 = pc.getClassId();
						} catch (Exception exception1) {
						}
						if (c1 == 0 || c1 == 1) {
							pc.sendPackets(new S_INN(obj.getId(), 300, "inn12", "600"));
							return;
						} else {
							htmlid = "inn10";
						}
					}
				} else if (s.equalsIgnoreCase("return")) {
					if (pc.getInventory().checkItem(40312)) {
						// L1ItemInstance[] key = null;
						// key = pc.getInventory().findItemsId(40312);
						/*
						 * for (int i = 0; i < key.length; i++) {
						 * if(key[i].getEndTime().getTime() >
						 * System.currentTimeMillis()){
						 * INN.setINN(key[i].getKey(), false); InnTimer IT =
						 * INN.getInnTimer(key[i].getKey()); if(IT !=null){
						 * IT.키차감(); } break; } }
						 */
						int ct = pc.getInventory().findItemId(40312).getCount();
						int cash = ct * 60;
						pc.getInventory().consumeItem(40312, ct);
						pc.getInventory().storeItem(40308, cash);
						htmlid = "inn20";
						String count = Integer.toString(cash);
						htmldata = (new String[] { "여관주인", count });
					} else if (pc.getInventory().checkItem(49312)) {
						int ct = pc.getInventory().findItemId(49312).getCount();
						int cash = ct * 120;
						pc.getInventory().consumeItem(49312, ct);
						pc.getInventory().storeItem(40308, cash);
						htmlid = "inn20";
						String count = Integer.toString(cash);
						htmldata = (new String[] { "여관주인", count });
					} else {
						htmlid = "";
					}
				} else if (s.equalsIgnoreCase("enter")) {
					short keymap = 0;
					L1ItemInstance[] key = null;
					if (pc.getInventory().checkItem(40312)) {
						key = pc.getInventory().findItemsId(40312);
					}
					if (pc.getInventory().checkItem(49312)) {
						key = pc.getInventory().findItemsId(49312);
					}
					if (key == null) {
						return;
					}
					if (key.length == 0) {
						pc.sendPackets(new S_SystemMessage("방또는 홀 대여를 먼저 해주세요."));
						return;
					}
					for (int i = 0; i < key.length; i++) {
						if (key[i].getEndTime().getTime() > System.currentTimeMillis()) {
							keymap = (short) key[i].getKey();
							break;
						}
					}
					if (keymap == 0) {
						pc.sendPackets(new S_SystemMessage("방또는 홀 대여를 먼저 해주세요."));
						return;
					}

					if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70012) {
						if (keymap >= 16384 && keymap <= 16684) {
							L1Teleport.teleport(pc, 32746, 32803, (short) keymap, 5, true);// 말섬 방
						} else if (keymap >= 16896 && keymap <= 17196) {
							L1Teleport.teleport(pc, 32744, 32808,
									(short) keymap, 5, true);// 말섬 홀
						} else {
							pc.sendPackets(new S_SystemMessage( "저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else if (((L1NpcInstance) obj).getNpcTemplate()
							.get_npcId() == 70019) {
						if (keymap >= 17408 && keymap <= 17708) {
							L1Teleport.teleport(pc, 32744, 32803, (short) keymap, 5, true); // 글말 방
						} else if (keymap >= 17920 && keymap <= 18220) {
							L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 글말  홀
						} else {
							pc.sendPackets(new S_SystemMessage( "저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70031) {
						if (keymap >= 18432 && keymap <= 18732) {
							L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 기란 방
						} else if (keymap >= 18944 && keymap <= 19244) {
							L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 기란 홀
						} else {
							pc.sendPackets(new S_SystemMessage("저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70054) {
						if (keymap >= 19456 && keymap <= 19756) {
							L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 아덴 방
						} else if (keymap >= 19968 && keymap <= 20268) {
							L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 아덴 홀
						} else {
							pc.sendPackets(new S_SystemMessage("저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else if (((L1NpcInstance) obj).getNpcTemplate()
							.get_npcId() == 70065) {
						if (keymap >= 23552 && keymap <= 23852) {
							L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 오렌 방
						} else if (keymap >= 24064 && keymap <= 24364) {
							L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 오렌 홀
						} else {
							pc.sendPackets(new S_SystemMessage("저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else if (((L1NpcInstance) obj).getNpcTemplate()
							.get_npcId() == 70070) {
						if (keymap >= 20480 && keymap <= 20780) {
							L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true); // 윈말 방
						} else if (keymap >= 20992 && keymap <= 21292) {
							L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 윈말 홀
						} else {
							pc.sendPackets(new S_SystemMessage("저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else if (((L1NpcInstance) obj).getNpcTemplate()
							.get_npcId() == 70075) {
						if (keymap >= 21504 && keymap <= 21804) {
							L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 은기사 방
						} else if (keymap >= 22016 && keymap <= 22316) {
							L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 은기사 홀
						} else {
							pc.sendPackets(new S_SystemMessage("저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70084) {
						if (keymap >= 22528 && keymap <= 22828) {
							L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 하이네  방
						} else if (keymap >= 23040 && keymap <= 23340) {
							L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 하이네 홀
						} else {
							pc.sendPackets(new S_SystemMessage("저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70096) {
						if (keymap >= 24576 && keymap <= 24876) {
							L1Teleport.teleport(pc, 32745, 32803, (short) keymap, 5, true);// 해적섬 방
						} else if (keymap >= 25088 && keymap <= 25388) {
							L1Teleport.teleport(pc, 32745, 32807, (short) keymap, 5, true);// 해적섬 홀
						} else {
							pc.sendPackets(new S_SystemMessage("저희 여관 열쇠가 아닙니다."));
							return;
						}
					} else {
						return;
					}
				} else {
					htmlid = "";
				}

			} else if (s.equalsIgnoreCase("hall") && obj instanceof L1MerchantInstance) {

			} else if (s.equalsIgnoreCase("return")) {

			} else if (s.equalsIgnoreCase("enter")) {
				if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100847) {
					htmlid = 흑데스(s, pc);
				}
			} else if (s.equalsIgnoreCase("openigate")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				openCloseGate(pc, npc.getNpcTemplate().get_npcId(), true);
				htmlid = "";
			} else if (s.equalsIgnoreCase("closeigate")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				openCloseGate(pc, npc.getNpcTemplate().get_npcId(), false);
				htmlid = "";
			} else if (s.equalsIgnoreCase("askwartime")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (npc.getNpcTemplate().get_npcId() == 60514) {
					htmldata = makeWarTimeStrings(L1CastleLocation.KENT_CASTLE_ID);
					htmlid = "ktguard7";
				} else if (npc.getNpcTemplate().get_npcId() == 60560) {
					htmldata = makeWarTimeStrings(L1CastleLocation.OT_CASTLE_ID);
					htmlid = "orcguard7";
				} else if (npc.getNpcTemplate().get_npcId() == 60552) {
					htmldata = makeWarTimeStrings(L1CastleLocation.WW_CASTLE_ID);
					htmlid = "wdguard7";
				} else if (npc.getNpcTemplate().get_npcId() == 60524
						|| npc.getNpcTemplate().get_npcId() == 60525
						|| npc.getNpcTemplate().get_npcId() == 60529) {
					htmldata = makeWarTimeStrings(L1CastleLocation.GIRAN_CASTLE_ID);
					htmlid = "grguard7";
				} else if (npc.getNpcTemplate().get_npcId() == 70857) {
					htmldata = makeWarTimeStrings(L1CastleLocation.HEINE_CASTLE_ID);
					htmlid = "heguard7";
				} else if (npc.getNpcTemplate().get_npcId() == 60530
						|| npc.getNpcTemplate().get_npcId() == 60531) {
					htmldata = makeWarTimeStrings(L1CastleLocation.DOWA_CASTLE_ID);
					htmlid = "dcguard7";
				} else if (npc.getNpcTemplate().get_npcId() == 60533
						|| npc.getNpcTemplate().get_npcId() == 60534) {
					htmldata = makeWarTimeStrings(L1CastleLocation.ADEN_CASTLE_ID);
					htmlid = "adguard7";
				} else if (npc.getNpcTemplate().get_npcId() == 81156) {
					htmldata = makeWarTimeStrings(L1CastleLocation.DIAD_CASTLE_ID);
					htmlid = "dfguard3";
				}
			} else if (s.equalsIgnoreCase("inex")) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					int castle_id = clan.getCastleId();
					if (castle_id != 0) {
						if (castle_id == 4) {
							htmlid = "orville2";
						} else if (castle_id == 6) {
							htmlid = "potempin2";
						}
						L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
						int money = l1castle.getShowMoney();// 결산
						int a = money / 2 * 3;// 소비액
						int b = money + a; // 총액
						int pm = l1castle.getPublicMoney();
						htmldata = new String[] { "" + b + "", "" + a + "","" + money + "", "" + pm + "" };
					}
				}
			} else if (s.equalsIgnoreCase("stdex")) { // 기본지출
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					int castle_id = clan.getCastleId();
					if (castle_id != 0) {
						if (castle_id == 4) {
							htmlid = "orville3";
						} else if (castle_id == 6) {
							htmlid = "potempin3";
						}
						L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
						int i = l1castle.getShowMoney();// 계산 금액
						int totalmoney = i + i / 2 * 3;
						int money = totalmoney;
						int a = money / 100 * 25;// 25%
						int b = money / 100 * 10;// 10%
						int c = money / 100 * 5;// 5%
						htmldata = new String[] { "" + a + "", "" + b + "","" + c + "", "" + c + "", "" + b + "","" + c + "" };
					}
				}
			} else if (s.equalsIgnoreCase("tax")) {
				if (pc.getId() != pc.getClan().getLeaderId() || pc.getClanRank() != L1Clan.CLAN_RANK_PRINCE || !pc.isCrown()) {
					return;
				}
				int castle_id = pc.getClan().getCastleId();
				if (castle_id != 0) {
					L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
					pc.sendPackets(new S_TaxRate(pc.getId(), l1castle.getTaxRate()));
				}
			} else if (s.equalsIgnoreCase("withdrawal")) {
				if (pc.getId() != pc.getClan().getLeaderId() || pc.getClanRank() != L1Clan.CLAN_RANK_PRINCE || !pc.isCrown()) {
					return;
				}
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					int castle_id = clan.getCastleId();
					if (castle_id != 0) {
						L1Castle l1castle = CastleTable.getInstance().getCastleTable(castle_id);
						if (l1castle.getPublicMoney() <= 0) {
							return;
						}
						pc.sendPackets(new S_Drawal(pc.getId(), l1castle.getPublicMoney()));
					}
				}
			} else if (s.equalsIgnoreCase("cdeposit")) {// 자금입금
				pc.sendPackets(new S_Deposit(pc.getId()));
			} else if (s.equalsIgnoreCase("employ")) {// 용병고용
				int castle_id = pc.getClan().getCastleId();
				pc.sendPackets(new S_SoldierBuyList(objid, castle_id));
			} else if (s.equalsIgnoreCase("arrange")) {// 용병배치
				int castle_id = pc.getClan().getCastleId();
				pc.sendPackets(new S_SoldierGiveList(objid, castle_id));
			} else if (s.equalsIgnoreCase("castlegate")) { // 성문
				castleGateStatus(pc, objid);
			} else if (s.equalsIgnoreCase("demand")) {
				GiveSoldier(pc, objid);
			} else if (s.equalsIgnoreCase("allhealegate")) {
				repairGate(pc, 2001, 1);
				repairGate(pc, 8053, 1);
				repairGate(pc, 8052, 1);
				pc.sendPackets(new S_SystemMessage("성문 수리: 켄트성의 성문을 수리함."));
			} else if (s.equalsIgnoreCase("healegate_giran outer gatef")) {// 외성 남문
				repairGate(pc, 2031, 4);
				repairGate(pc, 8050, 4);
				repairGate(pc, 8051, 4);
			} else if (s.equalsIgnoreCase("healegate_giran outer gatel")) {// 외성 서문
				repairGate(pc, 2032, 4);
			} else if (s.equalsIgnoreCase("healegate_giran inner gatef")) {// 내성 남문
				repairGate(pc, 2033, 4);
			} else if (s.equalsIgnoreCase("healegate_giran inner gatel")) {// 내성 서문
				repairGate(pc, 2034, 4);
			} else if (s.equalsIgnoreCase("healegate_giran inner gater")) {// 내성 동문
				repairGate(pc, 2035, 4);
			} else if (s.equalsIgnoreCase("healigate_giran castle house door")) {// 현관문
				repairGate(pc, 2030, 4);
			} else if (s.equalsIgnoreCase("hhealegate_iron door a")) {// 난성 외성 남문
				repairGate(pc, 2051, 4);
			} else if (s.equalsIgnoreCase("hhealegate_iron door b")) {// 난성 외성 동문문
				repairGate(pc, 2052, 4);
			} else if (s.equalsIgnoreCase("autorepairon")) {// 자동수리 On
				repairAutoGate(pc, 1);
			} else if (s.equalsIgnoreCase("autorepairoff")) {// 자동수리 Off
				repairAutoGate(pc, 0);
			} else if (s.equalsIgnoreCase("encw")) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				L1NpcInstance npc = (L1NpcInstance) obj;
				int npcid = npc.getNpcTemplate().get_npcId();

				if (npcid == 70508) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
						return;
					}
				} else if (npcid == 70547) { // 켄트성
					if (clan != null && clan.getCastleId() != 1)
						return;
				} else if (npcid == 70816) { // 오크성
					if (clan != null && clan.getCastleId() != 2)
						return;
				} else if (npcid == 70777) { // 윈다우드
					if (clan != null && clan.getCastleId() != 3)
						return;
				} else if (npcid == 70599) { // 기란
					if (clan != null && clan.getCastleId() != 4)
						return;
				} else if (npcid == 70861) { // 하이네
					if (clan != null && clan.getCastleId() != 5)
						return;
				} else if (npcid == 70655) { // 난성
					if (clan != null && clan.getCastleId() != 6)
						return;
				} else if (npcid == 70686) { // 아덴
					if (clan != null && clan.getCastleId() != 7)
						return;
				}
				if (L1CastleLocation.getCastleIdByInnerArea(pc.getLocation()) == 0)
					return;
				if (pc.getWeapon() == null) {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."));
				} else {
					L1SkillUse l1skilluse = null;
					for (L1ItemInstance item : pc.getInventory().getItems()) {
						if (pc.getWeapon().equals(item)) {
							l1skilluse = new L1SkillUse();
							l1skilluse.handleCommands(pc, L1SkillId.ENCHANT_WEAPON, item.getId(), 0, 0, null, 0, L1SkillUse.TYPE_SPELLSC);
							break;
						}
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("enca")) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				L1NpcInstance npc = (L1NpcInstance) obj;
				int npcid = npc.getNpcTemplate().get_npcId();

				if (npcid == 70509) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
						return;
					}
				} else if (npcid == 70550) { // 켄트성
					if (clan != null && clan.getCastleId() != 1)
						return;
				} else if (npcid == 70820) { // 오크성
					if (clan != null && clan.getCastleId() != 2)
						return;
				} else if (npcid == 70780) { // 윈다우드
					if (clan != null && clan.getCastleId() != 3)
						return;
				} else if (npcid == 70601) { // 기란
					if (clan != null && clan.getCastleId() != 4)
						return;
				} else if (npcid == 70865) { // 하이네
					if (clan != null && clan.getCastleId() != 5)
						return;
				} else if (npcid == 70657) { // 난성
					if (clan != null && clan.getCastleId() != 6)
						return;
				} else if (npcid == 70692) { // 아덴
					if (clan != null && clan.getCastleId() != 7)
						return;
				}
				if (L1CastleLocation.getCastleIdByInnerArea(pc.getLocation()) == 0) {
					return;
				}

				L1ItemInstance item = pc.getInventory().getItemEquipped(2, 2);
				if (item != null) {
					L1SkillUse l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(pc, L1SkillId.BLESSED_ARMOR, item.getId(), 0, 0, null, 0, L1SkillUse.TYPE_SPELLSC);
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."));
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("withdrawnpc")) {
				pc.sendPackets(new S_PetList(objid, pc));
			} else if (s.equalsIgnoreCase("changename")) {
				pc.setTempID(objid);
				pc.sendPackets(new S_Message_YN(325, ""));
			} else if (s.equalsIgnoreCase("attackchr")) {
				if (obj instanceof L1Character) {
					L1Character cha = (L1Character) obj;
					pc.sendPackets(new S_SelectTarget(cha.getId()));
				}
			} else if (s.equalsIgnoreCase("select")) {
				pc.sendPackets(new S_AuctionBoardRead(objid, s2));
			} else if (s.equalsIgnoreCase("map")) {
				pc.sendPackets(new S_HouseMap(objid, s2));
			} else if (s.equalsIgnoreCase("apply")) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
						if (pc.getLevel() >= 15) {
							if (clan.getHouseId() == 0) {
								pc.sendPackets(new S_ApplyAuction(objid, s2));
							} else {
								pc.sendPackets(new S_ServerMessage(521));
								htmlid = "";
							}
						} else {
							pc.sendPackets(new S_ServerMessage(519));
							htmlid = "";
						}
					} else {
						pc.sendPackets(new S_ServerMessage(518));
						htmlid = "";
					}
				} else {
					pc.sendPackets(new S_ServerMessage(518));
					htmlid = "";
				}
			} else if (s.equalsIgnoreCase("open") || s.equalsIgnoreCase("close")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				openCloseDoor(pc, npc, s);
				htmlid = "";
			} else if (s.equalsIgnoreCase("expel")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				expelOtherClan(pc, npc.getNpcTemplate().get_npcId());
				htmlid = "";
			} else if (s.equalsIgnoreCase("pay")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				htmldata = makeHouseTaxStrings(pc, npc);
				htmlid = "agpay";
				if (htmldata == null) {
					htmlid = "agnofee";
				}
			} else if (s.equalsIgnoreCase("payfee")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				payFee(pc, npc);
				htmlid = "";
			} else if (s.equalsIgnoreCase("name")) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					int houseId = clan.getHouseId();
					if (houseId != 0) {
						if (!pc.isCrown() || pc.getId() != clan.getLeaderId()) {
							pc.sendPackets(new S_ServerMessage(518));
							return;
						}
						L1House house = HouseTable.getInstance().getHouseTable(houseId);
						int keeperId = house.getKeeperId();
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (npc.getNpcTemplate().get_npcId() == keeperId) {
							pc.setTempID(houseId);
							pc.sendPackets(new S_Message_YN(512, ""));
						}
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("rem")) { // 집안 가구 제거
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					int houseId = clan.getHouseId();
					if (houseId != 0) {
						L1House house = HouseTable.getInstance().getHouseTable(houseId);
						int keeperId = house.getKeeperId();
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (npc.getNpcTemplate().get_npcId() == keeperId) {
							for (L1FurnitureInstance furn : L1World.getInstance().getAllFurniture()) {
								if (L1HouseLocation.isInHouseLoc(houseId, furn.getX(), furn.getY(), furn.getMapId())) {
									furn.deleteMe();
									FurnitureSpawnTable.getInstance().deleteFurniture(furn);
								}
							}
						}
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("tel0") || s.equalsIgnoreCase("tel1") || s.equalsIgnoreCase("tel2") || s.equalsIgnoreCase("tel3")) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					int houseId = clan.getHouseId();
					if (houseId != 0) {
						L1House house = HouseTable.getInstance().getHouseTable(houseId);
						int keeperId = house.getKeeperId();
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (npc.getNpcTemplate().get_npcId() == keeperId) {
							int[] loc = new int[3];
							if (s.equalsIgnoreCase("tel0")) {
								loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
							} else if (s.equalsIgnoreCase("tel1")) {
								loc = L1HouseLocation.getHouseTeleportLoc(houseId, 1);
							} else if (s.equalsIgnoreCase("tel2")) {
								loc = L1HouseLocation.getHouseTeleportLoc(houseId, 2);
							} else if (s.equalsIgnoreCase("tel3")) {
								loc = L1HouseLocation.getHouseTeleportLoc(houseId, 3);
							}
							L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
						}
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("upgrade")) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					int houseId = clan.getHouseId();
					if (houseId != 0) {
						L1House house = HouseTable.getInstance().getHouseTable(houseId);
						int keeperId = house.getKeeperId();
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (npc.getNpcTemplate().get_npcId() == keeperId) {
							if (pc.isCrown() && pc.getId() == clan.getLeaderId()) {
								if (!house.isPurchaseBasement()) {
									pc.sendPackets(new S_ServerMessage(1135));
								} else {
									if (pc.getInventory().consumeItem(L1ItemId.ADENA, 5000000)) {
										house.setPurchaseBasement(false);
										HouseTable.getInstance().updateHouse(house);
										if (!Config.CLAN_BLESS_ALL_USE) {
											pc.getClan().setCaveOpen(true);
											ClanBlessHuntInfo.getInstance().settingClanBlessHuntMaps(clan);
											ClanTable.getInstance().updateClanBlessHunt(clan);
										}
										pc.sendPackets(new S_ServerMessage(1099));
									} else {
										pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다."));
									}
								}
							} else {
								pc.sendPackets(new S_ServerMessage(518));
							}
						}
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("hall") && obj instanceof L1HousekeeperInstance) {
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					int houseId = clan.getHouseId();
					if (houseId != 0) {
						L1House house = HouseTable.getInstance().getHouseTable(houseId);
						int keeperId = house.getKeeperId();
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (npc.getNpcTemplate().get_npcId() == keeperId) {
							if (house.isPurchaseBasement()) {
								int[] loc = new int[3];
								loc = L1HouseLocation.getBasementLoc(houseId);
								L1Teleport.teleport(pc, loc[0], loc[1], (short) (loc[2]), 5, true);
							} else {
								pc.sendPackets(new S_ServerMessage(1098));
							}
						}
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("exp")) {
				if (pc.getExpRes() == 1) {
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

					pc.sendPackets(new S_Message_YN(738, String.valueOf(cost)));
				} else {
					pc.sendPackets(new S_ServerMessage(739));
					htmlid = "";
				}
			} else if (s.equalsIgnoreCase("pk")) {
				if (pc.getLawful() < 30000) {
					pc.sendPackets(new S_SystemMessage("아직 잘못을 씻기에 충분한 선행을 하지 않았습니다."));
				} else if (pc.get_PKcount() < 5) {
					pc.sendPackets(new S_SystemMessage("아직 잘못을 씻는 행위를 할 필요가 없습니다."));
				} else {
					if (pc.getInventory().consumeItem(L1ItemId.ADENA, 700000)) {
						pc.set_PKcount(pc.get_PKcount() - 5);
						pc.sendPackets(new S_ServerMessage(561, String.valueOf(pc.get_PKcount())));
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("ent")) {
				int npcId = ((L1NpcInstance) obj).getNpcId();
				if (npcId == 80085) {
					htmlid = enterHauntedHouse(pc);
				} else if (npcId == 4206002) { // 펫 레이싱
					htmlid = enterPetRacing(pc);
				} else if (npcId == 4206000) {
					if (pc.getLevel() > 54)
						if (pc.getInventory().checkItem(L1ItemId.REMINISCING_CANDLE)) {
							pc.getInventory().consumeItem(L1ItemId.REMINISCING_CANDLE, 1);
							L1Teleport.teleport(pc, 32723 + _random.nextInt(10), 32851 + _random.nextInt(10), (short) 5166, 5, true);
							StatInitialize(pc);
							htmlid = "";
						} else {
							pc.sendPackets(new S_ServerMessage(1290));// 스테이터스 초기화에 필요한 아이템이 없습니다.
						} else {
						pc.sendPackets(new S_SystemMessage( "스텟초기화는 레벨55부터 이용하실수 있습니다."));
					}
				} else if (npcId == 50038 || npcId == 50042 || npcId == 50029 || npcId == 50019 || npcId == 50062) {
					htmlid = watchUb(pc, npcId);
				} else {
					htmlid = enterUb(pc, npcId);
				}
			} else if (s.equalsIgnoreCase("par")) {
				htmlid = enterUb(pc, ((L1NpcInstance) obj).getNpcId());
			} else if (s.equalsIgnoreCase("info")) {
				int npcId = ((L1NpcInstance) obj).getNpcId();
				if (npcId == 80085 || npcId == 80086 || npcId == 80087) {
				} else {
					htmlid = "colos2";
				}
			} else if (s.equalsIgnoreCase("sco")) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				UbRank(pc, npc);
			} else if (s.equalsIgnoreCase("haste")) { // 헤이스트
				L1NpcInstance l1npcinstance = (L1NpcInstance) obj;
				int npcid = l1npcinstance.getNpcTemplate().get_npcId();
				if (npcid == 70514) {
					if (pc.getLevel() < 13) {
						pc.sendPackets(new S_SkillHaste(pc.getId(), 1, 1800));
						Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 1, 0));
						pc.sendPackets(new S_SkillSound(pc.getId(), 755));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 755));
						pc.getMoveState().setMoveSpeed(1);
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.STATUS_HASTE, 1800 * 1000);
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
						pc.sendPackets(new S_SkillSound(pc.getId(), 830));
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					}
					htmlid = "";
				}

			} else if (s.equalsIgnoreCase("skeleton nbmorph")) {
				if (pc.getLevel() < 13) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						poly(client, 2374);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("lycanthrope nbmorph")) {
				if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
					poly(client, 3874);
					pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("shelob nbmorph")) {
				if (pc.getLevel() < 13) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						poly(client, 95);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("ghoul nbmorph")) {
				if (pc.getLevel() < 13) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						poly(client, 3873);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("ghast nbmorph")) {
				if (pc.getLevel() < 13) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						poly(client, 3875);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("atuba orc nbmorph")) {
				if (pc.getLevel() < 13) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						poly(client, 3868);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("skeleton axeman nbmorph")) {
				if (pc.getLevel() < 13) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						poly(client, 2376);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
					}
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("troll nbmorph")) {
				if (pc.getLevel() < 13) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
						poly(client, 3878);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
					} else {
						pc.sendPackets(new S_SystemMessage("아데나가 충분치 않습니다.")); // 아데나가 충분치 않습니다.
					}
				}
				htmlid = "";
			/** 액션 다로 계산 */
			} else if (s.equalsIgnoreCase("status")) {
				int NpcId = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				if(NpcId == 70041 || NpcId == 70042 || NpcId == 70035){
					htmlid = "maeno4";
					htmldata = L1BugBearRace.getInstance().makeStatusString();
				} else if(NpcId == 72320){
					htmlid = "maeno4";
					htmldata = DogFight.getInstance().makeStatusString();
				}
			} else if (s.equalsIgnoreCase("contract1")) {
				pc.getQuest().set_step(L1Quest.QUEST_LYRA, 1);
				htmlid = "lyraev2";
			} else if (s.equalsIgnoreCase("contract1yes")
					|| s.equalsIgnoreCase("contract1no")) {
				if (s.equalsIgnoreCase("contract1yes")) {
					htmlid = "lyraev5";
				} else if (s.equalsIgnoreCase("contract1no")) {
					pc.getQuest().set_step(L1Quest.QUEST_LYRA, 0);
					htmlid = "lyraev4";
				}
				int totem = 0;
				if (pc.getInventory().checkItem(40131)) {
					totem++;
				}
				if (pc.getInventory().checkItem(40132)) {
					totem++;
				}
				if (pc.getInventory().checkItem(40133)) {
					totem++;
				}
				if (pc.getInventory().checkItem(40134)) {
					totem++;
				}
				if (pc.getInventory().checkItem(40135)) {
					totem++;
				}
				if (totem != 0) {
					materials = new int[totem];
					counts = new int[totem];
					createitem = new int[totem];
					createcount = new int[totem];
					totem = 0;
					if (pc.getInventory().checkItem(40131)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40131);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40131;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 50;
						totem++;
					}
					if (pc.getInventory().checkItem(40132)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40132);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40132;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 100;
						totem++;
					}
					if (pc.getInventory().checkItem(40133)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40133);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40133;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 50;
						totem++;
					}
					if (pc.getInventory().checkItem(40134)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40134);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40134;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 30;
						totem++;
					}
					if (pc.getInventory().checkItem(40135)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40135);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40135;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 200;
						totem++;
					}
				}
			} else if (s.equalsIgnoreCase("pandora6")
					|| s.equalsIgnoreCase("cold6")
					|| s.equalsIgnoreCase("balsim3")
					|| s.equalsIgnoreCase("mellin3")
					|| s.equalsIgnoreCase("glen3")) {
				htmlid = s;
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				int taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(npcid);
				htmldata = new String[] { String.valueOf(taxRatesCastle) };
			} else if (s.equalsIgnoreCase("set")) {
				if (obj instanceof L1NpcInstance) {
					int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
					int town_id = L1TownLocation.getTownIdByNpcid(npcid);

					if (town_id >= 1 && town_id <= 10) {
						if (pc.getHomeTownId() == -1) {
							pc.sendPackets(new S_SystemMessage("새롭게 마을에 등록하기 위해서는 시간 경과가 필요합니다. 이후에 다시 등록해 주시기 바랍니다."));
							htmlid = "";
						} else if (pc.getHomeTownId() > 0) {
							if (pc.getHomeTownId() != town_id) {
								L1Town town = TownTable.getInstance().getTownTable(pc.getHomeTownId());
								if (town != null) {
									pc.sendPackets(new S_ServerMessage(758, town.get_name()));
								}
								htmlid = "";
							} else {
								htmlid = "";
							}
						} else if (pc.getHomeTownId() == 0) {
							if (pc.getLevel() < 10) {
								pc.sendPackets(new S_SystemMessage("마을 등록을 위해서는 10 레벨 이상이 되어야 합니다."));
								htmlid = "";
							} else {
								int level = pc.getLevel();
								int cost = level * level * 10;
								if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
									pc.setHomeTownId(town_id);
									pc.setContribution(0);
									pc.save();
								} else {
									pc.sendPackets(new S_ServerMessage(337, "$4"));
								}
								htmlid = "";
							}
						}
					}
				}
			} else if (s.equalsIgnoreCase("clear")) {
				if (obj instanceof L1NpcInstance) {
					int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
					int town_id = L1TownLocation.getTownIdByNpcid(npcid);
					if (town_id > 0) {
						if (pc.getHomeTownId() > 0) {
							if (pc.getHomeTownId() == town_id) {
								pc.setHomeTownId(-1);
								pc.setContribution(0);
								pc.save();
							} else {
								pc.sendPackets(new S_SystemMessage("당신은 이미 다른 마을 소속으로 등록되어 있습니다."));
							}
						}
						htmlid = "";
					}
				}

			} else if (s.equalsIgnoreCase("request cold of kiringku")) {
				if (pc.getInventory().checkItem(6009, 1)
						&& pc.getInventory().checkItem(6023, 2)
						&& pc.getInventory().checkItem(41246, 50000)) {
					pc.getInventory().consumeItem(41246, 50000);
					pc.getInventory().consumeItem(6023, 2);
					pc.getInventory().consumeItem(6009, 1);
					pc.getInventory().storeItem(6001, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "냉한의 키링크"), true);
					// pc.sendPackets(new S_SystemMessage("냉한의 키링크 를 얻었습니다."));
				} else {
					pc.sendPackets(new S_SystemMessage("재료가 부족합니다."));
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("request cold of chainsword")) {
				if (pc.getInventory().checkItem(6008, 1)
						&& pc.getInventory().checkItem(6023, 2)
						&& pc.getInventory().checkItem(41246, 50000)) {
					pc.getInventory().consumeItem(41246, 50000);
					pc.getInventory().consumeItem(6023, 2);
					pc.getInventory().consumeItem(6008, 1);
					pc.getInventory().storeItem(6000, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "극한의 체인소드"), true);
					// pc.sendPackets(new S_SystemMessage("극한의 체인소드 를 얻었습니다."));
				} else {
					pc.sendPackets(new S_SystemMessage("재료가 부족합니다."));
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("request diamond of dragon 2009")) {
				if (pc.getInventory().checkItem(5000067, 1)) {
					pc.getInventory().storeItem(437010, 1);
					pc.getInventory().consumeItem(5000067, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "드래곤의 다이아몬드"), true);
					// pc.sendPackets(new
					// S_SystemMessage("극한의 티아라 원석 을 얻었습니다."));
				} else {
					pc.sendPackets(new S_SystemMessage("재료가 부족합니다."));
				}
				htmlid = "";
			} else if (s
					.equalsIgnoreCase("request treasure box of dragon 2009")) {
				if (pc.getInventory().checkItem(5000068, 1)) {
					pc.getInventory().storeItem(437009, 1);
					pc.getInventory().consumeItem(5000068, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "드래곤의 다이아몬드 상자"), true);
					// pc.sendPackets(new
					// S_SystemMessage("극한의 티아라 원석 을 얻었습니다."));
				} else {
					pc.sendPackets(new S_SystemMessage("재료가 부족합니다."));
				}
				htmlid = "";

			} else if (s.equalsIgnoreCase("request arctic of helm")) {
				if (pc.getInventory().checkItem(20006, 1)
						&& pc.getInventory().checkItem(6010, 1)
						&& pc.getInventory().checkItem(41246, 50000)) {
					pc.getInventory().consumeItem(41246, 50000);
					pc.getInventory().consumeItem(6010, 1);
					pc.getInventory().consumeItem(20006, 1);
					pc.getInventory().storeItem(6003, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "극한의 투구"), true);
					// pc.sendPackets(new S_SystemMessage("극한의 투구 를 얻었습니다."));
				} else {
					pc.sendPackets(new S_SystemMessage("재료가 부족합니다."));
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("request arctic of armor")) {
				if (pc.getInventory().checkItem(20154, 1)
						&& pc.getInventory().checkItem(6011, 1)
						&& pc.getInventory().checkItem(41246, 50000)) {
					pc.getInventory().consumeItem(41246, 50000);
					pc.getInventory().consumeItem(6011, 1);
					pc.getInventory().consumeItem(20154, 1);
					pc.getInventory().storeItem(6004, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "극한의 갑옷"), true);
					// pc.sendPackets(new S_SystemMessage("극한의 갑옷 을 얻었습니다."));
				} else {
					pc.sendPackets(new S_SystemMessage("재료가 부족합니다."));
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("request arctic of boots")) {
				if (pc.getInventory().checkItem(20205, 1)
						&& pc.getInventory().checkItem(6012, 1)
						&& pc.getInventory().checkItem(41246, 50000)) {
					pc.getInventory().consumeItem(41246, 50000);
					pc.getInventory().consumeItem(6012, 1);
					pc.getInventory().consumeItem(20205, 1);
					pc.getInventory().storeItem(6005, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "극한의 부츠"), true);
					// pc.sendPackets(new S_SystemMessage("극한의 부츠 를 얻었습니다."));
				} else {
					pc.sendPackets(new S_SystemMessage("재료가 부족합니다."));
				}
				htmlid = "";
			} else if (s.equalsIgnoreCase("ask")) {
				if (obj instanceof L1NpcInstance) {
					int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
					int town_id = L1TownLocation.getTownIdByNpcid(npcid);

					if (town_id >= 1 && town_id <= 10) {
						L1Town town = TownTable.getInstance().getTownTable(town_id);
						String leader = town.get_leader_name();
						if (leader != null && leader.length() != 0) {
							htmlid = "owner";
							htmldata = new String[] { leader };
						} else {
							htmlid = "noowner";
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71038) {
				if (s.equalsIgnoreCase("A")) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(41060, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "orcfnoname9";
				} else if (s.equalsIgnoreCase("Z")) {
					if (pc.getInventory().consumeItem(41060, 1)) {
						htmlid = "orcfnoname11";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71039) {
				if (s.equalsIgnoreCase("teleportURL")) {
					htmlid = "orcfbuwoo2";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71040) {
				if (s.equalsIgnoreCase("A")) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(41065, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "orcfnoa4";
				} else if (s.equalsIgnoreCase("Z")) {
					if (pc.getInventory().consumeItem(41065, 1)) {
						htmlid = "orcfnoa7";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71041) {
				if (s.equalsIgnoreCase("A")) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(41064, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "orcfhuwoomo4";
				} else if (s.equalsIgnoreCase("Z")) {
					if (pc.getInventory().consumeItem(41064, 1)) {
						htmlid = "orcfhuwoomo6";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71042) {
				if (s.equalsIgnoreCase("A")) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(41062, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "orcfbakumo4";
				} else if (s.equalsIgnoreCase("Z")) {
					if (pc.getInventory().consumeItem(41062, 1)) {
						htmlid = "orcfbakumo6";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71043) {
				if (s.equalsIgnoreCase("A")) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(41063, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "orcfbuka4";
				} else if (s.equalsIgnoreCase("Z")) {
					if (pc.getInventory().consumeItem(41063, 1)) {
						htmlid = "orcfbuka6";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71044) {
				if (s.equalsIgnoreCase("A")) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					L1ItemInstance item = pc.getInventory().storeItem(41061, 1);
					String npcName = npc.getNpcTemplate().get_name();
					String itemName = item.getItem().getName();
					pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					htmlid = "orcfkame4";
				} else if (s.equalsIgnoreCase("Z")) {
					if (pc.getInventory().consumeItem(41061, 1)) {
						htmlid = "orcfkame6";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71078) {
				if (s.equalsIgnoreCase("teleportURL")) {
					htmlid = "usender2";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71080) {
				if (s.equalsIgnoreCase("teleportURL")) {
					htmlid = "amisoo2";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71180) { // 제이프
				htmlid = 제이프(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 6000101) {
				htmlid = 오만(s, pc, (L1NpcInstance) obj);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71181) { // 에마이
				if (s.equalsIgnoreCase("A")) { // 곰인형
					if (pc.getInventory().checkItem(41093)) {
						pc.getInventory().consumeItem(41093, 1);
						pc.getInventory().storeItem(41097, 1);
						htmlid = "my5";
					} else {
						htmlid = "my4";
					}
				} else if (s.equalsIgnoreCase("B")) { // 향수
					if (pc.getInventory().checkItem(41094)) {
						pc.getInventory().consumeItem(41094, 1);
						pc.getInventory().storeItem(41097, 1);
						htmlid = "my6";
					} else {
						htmlid = "my4";
					}
				} else if (s.equalsIgnoreCase("C")) { // 드레스
					if (pc.getInventory().checkItem(41095)) {
						pc.getInventory().consumeItem(41095, 1);
						pc.getInventory().storeItem(41097, 1);
						htmlid = "my7";
					} else {
						htmlid = "my4";
					}
				} else if (s.equalsIgnoreCase("D")) { // 반지
					if (pc.getInventory().checkItem(41093)) {
						pc.getInventory().consumeItem(41093, 1);
						pc.getInventory().storeItem(41097, 1);
						htmlid = "my8";
					} else {
						htmlid = "my4";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71182) { // 에셈
				if (s.equalsIgnoreCase("A")) { // 영웅의 위인전
					if (pc.getInventory().checkItem(41098)) {
						pc.getInventory().consumeItem(41098, 1);
						pc.getInventory().storeItem(41102, 1);
						htmlid = "sm5";
					} else {
						htmlid = "sm4";
					}
				} else if (s.equalsIgnoreCase("B")) { // 세련된 모자
					if (pc.getInventory().checkItem(41099)) {
						pc.getInventory().consumeItem(41099, 1);
						pc.getInventory().storeItem(41102, 1);
						htmlid = "sm6";
					} else {
						htmlid = "sm4";
					}
				} else if (s.equalsIgnoreCase("C")) { // 최고급 와인
					if (pc.getInventory().checkItem(41100)) {
						pc.getInventory().consumeItem(41100, 1);
						pc.getInventory().storeItem(41102, 1);
						htmlid = "sm7";
					} else {
						htmlid = "sm4";
					}
				} else if (s.equalsIgnoreCase("D")) { // 알 수 없는 열쇠
					if (pc.getInventory().checkItem(41101)) {
						pc.getInventory().consumeItem(41101, 1);
						pc.getInventory().storeItem(41102, 1);
						htmlid = "sm8";
					} else {
						htmlid = "sm4";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80048) {
				if (s.equalsIgnoreCase("2")) {
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80049) {
				if (s.equalsIgnoreCase("1")) {
					if (pc.getKarma() <= -10000000) {
						pc.setKarma(1000000);
						pc.sendPackets(new S_ServerMessage(1078));
						htmlid = "betray13";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80050) {
				if (s.equalsIgnoreCase("1")) {
					htmlid = "meet105";
				} else if (s.equalsIgnoreCase("2")) {
					if (pc.getInventory().checkItem(40718)) {
						htmlid = "meet106";
					} else {
						htmlid = "meet110";
					}
				} else if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().consumeItem(40718, 1)) {
						pc.addKarma((int) (-100 * Config.RATE_KARMA));
						pc.sendPackets(new S_ServerMessage(1079));

						htmlid = "meet107";
					} else {
						htmlid = "meet104";
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().consumeItem(40718, 10)) {
						pc.addKarma((int) (-1000 * Config.RATE_KARMA));
						pc.sendPackets(new S_ServerMessage(1079));

						htmlid = "meet108";
					} else {
						htmlid = "meet104";
					}
				} else if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().consumeItem(40718, 100)) {
						pc.addKarma((int) (-10000 * Config.RATE_KARMA));
						pc.sendPackets(new S_ServerMessage(1079));

						htmlid = "meet109";
					} else {
						htmlid = "meet104";
					}
				} else if (s.equalsIgnoreCase("d")) {
					if (pc.getInventory().checkItem(40615)
							|| pc.getInventory().checkItem(40616)) {
						htmlid = "";
					} else {
						if (pc.getKarmaLevel() <= -1) {
							L1Teleport.teleport(pc, 32683, 32895, (short) 608, 5, true);
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80052) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_CURSE_YAHEE)) {
						pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."));
					} else {
						pc.getSkillEffectTimerSet().setSkillEffect(STATUS_CURSE_BARLOG, 1020 * 1000); // 1020
						pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 1, 1020));
						pc.sendPackets(new S_SkillSound(pc.getId(), 750));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 750));
						pc.sendPackets(new S_ServerMessage(1127));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71168) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(41028)) {
						L1ItemInstance useitem = pc.getInventory().findItemId(41028);
						pc.getInventory().removeItem(useitem, 1);
						L1Teleport.teleport(pc, 32648, 32921, (short) 535, 5, true);
					}
				} else {
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80055) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				htmlid = getYaheeAmulet(pc, npc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80056) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (pc.getKarma() <= -10000000) {
					getBloodCrystalByKarma(pc, npc, s);
				}
				htmlid = "";
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80063) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(40921)) {
						L1Teleport.teleport(pc, 32674, 32832, (short) 603, 2, true);
					} else {
						htmlid = "gpass02";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80064) {
				if (s.equalsIgnoreCase("1")) {
					htmlid = "meet005";
				} else if (s.equalsIgnoreCase("2")) {
					if (pc.getInventory().checkItem(40678)) {
						htmlid = "meet006";
					} else {
						htmlid = "meet010";
					}
				} else if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().consumeItem(40678, 1)) {
						pc.addKarma((int) (100 * Config.RATE_KARMA));
						pc.sendPackets(new S_ServerMessage(1078));
						htmlid = "meet007";
					} else {
						htmlid = "meet004";
					}
				}
				else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().consumeItem(40678, 10)) {
						pc.addKarma((int) (1000 * Config.RATE_KARMA));
						pc.sendPackets(new S_ServerMessage(1078));
						htmlid = "meet008";
					} else {
						htmlid = "meet004";
					}
				} else if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().consumeItem(40678, 100)) {
						pc.addKarma((int) (10000 * Config.RATE_KARMA));
						pc.sendPackets(new S_ServerMessage(1078));
						htmlid = "meet009";
					} else {
						htmlid = "meet004";
					}
				} else if (s.equalsIgnoreCase("d")) {
					if (pc.getInventory().checkItem(40909)
							|| pc.getInventory().checkItem(40910)
							|| pc.getInventory().checkItem(40911)
							|| pc.getInventory().checkItem(40912)
							|| pc.getInventory().checkItem(40913)
							|| pc.getInventory().checkItem(40914)
							|| pc.getInventory().checkItem(40915)
							|| pc.getInventory().checkItem(40916)
							|| pc.getInventory().checkItem(40917)
							|| pc.getInventory().checkItem(40918)
							|| pc.getInventory().checkItem(40919)
							|| pc.getInventory().checkItem(40920)
							|| pc.getInventory().checkItem(40921)) {
						htmlid = "";
					} else {
						if (pc.getKarmaLevel() >= 1) {
							L1Teleport.teleport(pc, 32674, 32832, (short) 602, 2, true);
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80066) {
				if (s.equalsIgnoreCase("1")) {
					if (pc.getKarma() >= 10000000) {
						pc.setKarma(-1000000);
						pc.sendPackets(new S_ServerMessage(1079));
						htmlid = "betray03";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80071) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				htmlid = getBarlogEarring(pc, npc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80073) {
				if (s.equalsIgnoreCase("a")) {
					if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_CURSE_BARLOG)) {
						pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."));
					} else {
						pc.getSkillEffectTimerSet().setSkillEffect(STATUS_CURSE_YAHEE, 1020 * 1000);
						pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA, 2, 1020));
						pc.sendPackets(new S_SkillSound(pc.getId(), 750));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 750));
						pc.sendPackets(new S_ServerMessage(1127));
					}
				}
			/*} // 바르로그의 대장간
			else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80072) {
				int karmaLevel = pc.getKarmaLevel();

				if (s.equalsIgnoreCase("0")) {
					htmlid = "lsmitha";
				} else if (s.equalsIgnoreCase("1")) {
					htmlid = "lsmithb";
				} else if (s.equalsIgnoreCase("2")) {
					htmlid = "lsmithc";
				} else if (s.equalsIgnoreCase("3")) {
					htmlid = "lsmithd";
				} else if (s.equalsIgnoreCase("4")) {
					htmlid = "lsmithe";
				} else if (s.equalsIgnoreCase("5")) {
					htmlid = "lsmithf";
				} else if (s.equalsIgnoreCase("6")) {
					htmlid = "";
				} else if (s.equalsIgnoreCase("7")) {
					htmlid = "lsmithg";
				} else if (s.equalsIgnoreCase("8")) {
					htmlid = "lsmithh";
				} // 야히의 셔츠 / 바르로그의 대장간
				else if (s.equalsIgnoreCase("a") && karmaLevel >= 1) {
					materials = new int[] { 20158, 40669, 40678 };
					counts = new int[] { 1, 50, 100 };
					createitem = new int[] { 20083 };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = "lsmithaa";
				} // 야히의 아모 / 바르로그의 대장간
				else if (s.equalsIgnoreCase("b") && karmaLevel >= 2) {
					materials = new int[] { 20144, 40672, 40678 };
					counts = new int[] { 1, 5, 100 };
					createitem = new int[] { 20131 };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = "lsmithbb";
				} // 야히의 아모 / 바르로그의 대장간
				else if (s.equalsIgnoreCase("c") && karmaLevel >= 3) {
					materials = new int[] { 20075, 40671, 40678 };
					counts = new int[] { 1, 50, 100 };
					createitem = new int[] { 20069 };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = "lsmithcc";
				} // 야히의 글로브 / 바르로그의 대장간
				else if (s.equalsIgnoreCase("d") && karmaLevel >= 4) {
					materials = new int[] { 20183, 40674, 40678 };
					counts = new int[] { 1, 20, 100 };
					createitem = new int[] { 20179 };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = "lsmithdd";
				} // 야히의 부츠 / 바르로그의 대장간
				else if (s.equalsIgnoreCase("e") && karmaLevel >= 5) {
					materials = new int[] { 20190, 40674, 40678 };
					counts = new int[] { 1, 40, 100 };
					createitem = new int[] { 20209 };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = "lsmithee";
				} // 야히의 링 / 바르로그의 대장간
				else if (s.equalsIgnoreCase("f") && karmaLevel >= 6) {
					materials = new int[] { 20078, 40674, 40678 };
					counts = new int[] { 1, 10, 100 };
					createitem = new int[] { 20290 };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = "lsmithff";
				} // 야히의 아뮤렛트 / 바르로그의 대장간
				else if (s.equalsIgnoreCase("g") && karmaLevel >= 7) {
					materials = new int[] { 20078, 40670, 40678 };
					counts = new int[] { 1, 1, 100 };
					createitem = new int[] { 20261 };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = "lsmithgg";
				} // 야히의 헤룸 / 바르로그의 대장간
				else if (s.equalsIgnoreCase("h") && karmaLevel >= 8) {
					materials = new int[] { 40719, 40673, 40678 };
					counts = new int[] { 1, 1, 100 };
					createitem = new int[] { 20031 };
					createcount = new int[] { 1 };
					success_htmlid = "";
					failure_htmlid = "lsmithhh";
				}*/
				// 업의 관리자
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80074) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (pc.getKarma() >= 10000000) {
					getSoulCrystalByKarma(pc, npc, s);
				}
				htmlid = "";
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80057) {
				htmlid = karmaLevelToHtmlId(pc.getKarmaLevel());
				htmldata = new String[] { String.valueOf(pc.getKarmaPercent()) };
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80059
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80060
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80061
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80062) {
				htmlid = talkToDimensionDoor(pc, (L1NpcInstance) obj, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4205000) {
				if (s.equalsIgnoreCase("entertestdg")) {
					L1Teleport.teleport(pc, 32769, 32768, (short) 22, 5, false);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81124) {
				if (s.equalsIgnoreCase("1")) {
					poly(client, 4002);
					htmlid = "";
				} else if (s.equalsIgnoreCase("2")) {
					poly(client, 4004);
					htmlid = "";
				} else if (s.equalsIgnoreCase("3")) {
					poly(client, 4950);
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 50016) {
				if (pc.getLevel() <= 13) {
					if (s.equalsIgnoreCase("0")) {
						L1Teleport.teleport(pc, 32685, 32870, (short) 2005, 5, true);
						htmlid = "";
					}
				}
			} else if (s.equalsIgnoreCase("contract1")) {
				pc.getQuest().set_step(L1Quest.QUEST_LYRA, 1);
				htmlid = "lyraev2";
			} else if (s.equalsIgnoreCase("contract1yes")
					|| s.equalsIgnoreCase("contract1no")) {

				if (s.equalsIgnoreCase("contract1yes")) {
					htmlid = "lyraev5";
				} else if (s.equalsIgnoreCase("contract1no")) {
					pc.getQuest().set_step(L1Quest.QUEST_LYRA, 0);
					htmlid = "lyraev4";
				}
				int totem = 0;
				if (pc.getInventory().checkItem(40131)) {
					totem++;
				}
				if (pc.getInventory().checkItem(40132)) {
					totem++;
				}
				if (pc.getInventory().checkItem(40133)) {
					totem++;
				}
				if (pc.getInventory().checkItem(40134)) {
					totem++;
				}
				if (pc.getInventory().checkItem(40135)) {
					totem++;
				}
				if (totem != 0) {
					materials = new int[totem];
					counts = new int[totem];
					createitem = new int[totem];
					createcount = new int[totem];

					totem = 0;
					if (pc.getInventory().checkItem(40131)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40131);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40131;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 50;
						totem++;
					}
					if (pc.getInventory().checkItem(40132)) {
						L1ItemInstance l1iteminstance = pc.getInventory()
								.findItemId(40132);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40132;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 100;
						totem++;
					}
					if (pc.getInventory().checkItem(40133)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40133);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40133;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 50;
						totem++;
					}
					if (pc.getInventory().checkItem(40134)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40134);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40134;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 30;
						totem++;
					}
					if (pc.getInventory().checkItem(40135)) {
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40135);
						int i1 = l1iteminstance.getCount();
						materials[totem] = 40135;
						counts[totem] = i1;
						createitem[totem] = L1ItemId.ADENA;
						createcount[totem] = i1 * 200;
						totem++;
					}
				}
			} else if (s.equalsIgnoreCase("pandora6")
					|| s.equalsIgnoreCase("cold6")
					|| s.equalsIgnoreCase("balsim3")
					|| s.equalsIgnoreCase("mellin3")
					|| s.equalsIgnoreCase("glen3")) {
				htmlid = s;
				int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
				int taxRatesCastle = L1CastleLocation.getCastleTaxRateByNpcId(npcid);
				htmldata = new String[] { String.valueOf(taxRatesCastle) };
			} else if (s.equalsIgnoreCase("set")) {
				if (obj instanceof L1NpcInstance) {
					int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
					int town_id = L1TownLocation.getTownIdByNpcid(npcid);

					if (town_id >= 1 && town_id <= 10) {
						if (pc.getHomeTownId() == -1) {
							pc.sendPackets(new S_SystemMessage("새롭게 마을에 등록하기 위해서는 시간 경과가 필요합니다. 이후에 다시 등록해 주시기 바랍니다."));
							htmlid = "";
						} else if (pc.getHomeTownId() > 0) {
							if (pc.getHomeTownId() != town_id) {
								L1Town town = TownTable.getInstance().getTownTable(pc.getHomeTownId());
								if (town != null) {
									pc.sendPackets(new S_ServerMessage(758, town.get_name()));
								}
								htmlid = "";
							} else {
								htmlid = "";
							}
						} else if (pc.getHomeTownId() == 0) {
							if (pc.getLevel() < 10) {
								pc.sendPackets(new S_ServerMessage(757));
								htmlid = "";
							} else {
								int level = pc.getLevel();
								int cost = level * level * 10;
								if (pc.getInventory().consumeItem(L1ItemId.ADENA, cost)) {
									pc.setHomeTownId(town_id);
									pc.setContribution(0);
									pc.save();
								} else {
									pc.sendPackets(new S_ServerMessage(337, "$4"));
								}
								htmlid = "";
							}
						}
					}
				}
			} else if (s.equalsIgnoreCase("clear")) {
				if (obj instanceof L1NpcInstance) {
					int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
					int town_id = L1TownLocation.getTownIdByNpcid(npcid);
					if (town_id > 0) {
						if (pc.getHomeTownId() > 0) {
							if (pc.getHomeTownId() == town_id) {
								pc.setHomeTownId(-1);
								pc.setContribution(0);
								pc.save();
							} else {
								pc.sendPackets(new S_ServerMessage(756));
							}
						}
						htmlid = "";
					}
				}
			} else if (s.equalsIgnoreCase("ask")) {
				if (obj instanceof L1NpcInstance) {
					int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
					int town_id = L1TownLocation.getTownIdByNpcid(npcid);

					if (town_id >= 1 && town_id <= 10) {
						L1Town town = TownTable.getInstance().getTownTable(town_id);
						String leader = town.get_leader_name();
						if (leader != null && leader.length() != 0) {
							htmlid = "owner";
							htmldata = new String[] { leader };
						} else {
							htmlid = "noowner";
						}
					}
				}
				// 야메 시스템
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 450001797) {
				htmlid = birthday(pc, obj, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70534
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70556
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70572
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70631
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70663
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70761
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70788
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70806
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70830
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70876) {
				if (s.equalsIgnoreCase("r")) {
					if (obj instanceof L1NpcInstance) {
						int npcid = ((L1NpcInstance) obj).getNpcTemplate().get_npcId();
						L1TownLocation.getTownIdByNpcid(npcid);
					}
				} else if (s.equalsIgnoreCase("t")) {

				} else if (s.equalsIgnoreCase("c")) {

				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70512
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71037) {
				if (pc.getLevel() >= 13)
					return;
				if (s.equalsIgnoreCase("fullheal")) {
					pc.setCurrentHp(pc.getMaxHp());
					pc.setCurrentMp(pc.getMaxMp());
					pc.sendPackets(new S_ServerMessage(77));
					pc.sendPackets(new S_SkillSound(pc.getId(), 830));
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71030) {
				if (s.equalsIgnoreCase("fullheal")) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 5)) { // check
						pc.getInventory().consumeItem(L1ItemId.ADENA, 5); // del
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
						pc.sendPackets(new S_ServerMessage(77));
						pc.sendPackets(new S_SkillSound(pc.getId(), 830));
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
						if (pc.isInParty()) {
							pc.getParty().updateMiniHP(pc);
						}
					} else {
						pc.sendPackets(new S_ServerMessage(337, "$4"));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7) {
				if (s.equals("a")) {// 일반보상
					if (pc.getInventory().checkItem(30151, 1)) {
						pc.getInventory().consumeItem(30151, 1);
						아놀드경험치(pc, 1);
						htmlid = "anold3";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						htmlid = "anold4";
					}
				} else if (s.equals("b")) {// 특별한보상
					if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(437010, 1)) {
						pc.getInventory().consumeItem(30151, 1);
						pc.getInventory().consumeItem(437010, 1);
						아놀드경험치(pc, 2);
						htmlid = "anold3";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						htmlid = "anold4";
					}
				} else if (s.equals("c")) {// 빛나는 특별한보상
					if (pc.getInventory().checkItem(30151, 1) && pc.getInventory().checkItem(1437010, 1)) {
						pc.getInventory().consumeItem(30151, 1);
						pc.getInventory().consumeItem(1437010, 1);
						아놀드경험치(pc, 3);
						htmlid = "anold3";
					} else {
						pc.sendPackets(new S_SystemMessage("제작 아이템이 부족합니다."));
						htmlid = "anold4";
					}
				}
				// 알드란
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71002) {
				if (s.equalsIgnoreCase("0")) {
					if (pc.getLevel() <= 13) {
						L1SkillUse skillUse = new L1SkillUse();
						skillUse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_NPCBUFF, (L1NpcInstance) obj);
						htmlid = "";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71055) {
				if (s.equalsIgnoreCase("0")) {
					L1ItemInstance item = pc.getInventory().storeItem(40701, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 1);
					htmlid = "lukein8";
				}
				if (s.equalsIgnoreCase("1")) {
					pc.getQuest().set_end(L1Quest.QUEST_TBOX3);
					materials = new int[] { 40716 }; // 할아버지의 보물
					counts = new int[] { 1 };
					createitem = new int[] { 20269 }; // 해골목걸이
					createcount = new int[] { 1 };
					htmlid = "lukein0";
				} else if (s.equalsIgnoreCase("2")) {
					htmlid = "lukein12";
					pc.getQuest().set_step(L1Quest.QUEST_RESTA, 3);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71063) {
				if (s.equalsIgnoreCase("0")) {
					materials = new int[] { 40701 };
					counts = new int[] { 1 };
					createitem = new int[] { 40702 };
					createcount = new int[] { 1 };
					htmlid = "maptbox1";
					pc.getQuest().set_end(L1Quest.QUEST_TBOX1);
					int[] nextbox = { 1, 2, 3 };
					int pid = _random.nextInt(nextbox.length);
					int nb = nextbox[pid];
					if (nb == 1) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 2);
					} else if (nb == 2) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 3);
					} else if (nb == 3) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 4);
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71064
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71065
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71066) {
				if (s.equalsIgnoreCase("0")) {
					materials = new int[] { 40701 };
					counts = new int[] { 1 };
					createitem = new int[] { 40702 };
					createcount = new int[] { 1 };
					htmlid = "maptbox1";
					pc.getQuest().set_end(L1Quest.QUEST_TBOX2);
					int[] nextbox2 = { 1, 2, 3, 4, 5, 6 };
					int pid = _random.nextInt(nextbox2.length);
					int nb2 = nextbox2[pid];
					if (nb2 == 1) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 5);
					} else if (nb2 == 2) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 6);
					} else if (nb2 == 3) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 7);
					} else if (nb2 == 4) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 8);
					} else if (nb2 == 5) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 9);
					} else if (nb2 == 6) {
						pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 10);
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71067
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71068
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71069
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71070
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71071
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71072) { // 작은 상자-3번째
				if (s.equalsIgnoreCase("0")) {
					htmlid = "maptboxi";
					materials = new int[] { 40701 }; // 작은 보물의 지도
					counts = new int[] { 1 };
					createitem = new int[] { 40716 }; // 할아버지의 보물
					createcount = new int[] { 1 };
					pc.getQuest().set_end(L1Quest.QUEST_TBOX3);
					pc.getQuest().set_step(L1Quest.QUEST_LUKEIN1, 11);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71056) { // 시미즈(해적섬)
				// 아들을 찾는다
				if (s.equalsIgnoreCase("a")) {
					pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, 1);
					htmlid = "simizz7";
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(40661) && pc.getInventory().checkItem(40662) && pc.getInventory().checkItem(40663)) {
						htmlid = "simizz8";
						pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, 2);
						materials = new int[] { 40661, 40662, 40663 };
						counts = new int[] { 1, 1, 1 };
						createitem = new int[] { 20044 };
						createcount = new int[] { 1 };
					} else {
						htmlid = "simizz9";
					}
				} else if (s.equalsIgnoreCase("d")) {
					htmlid = "simizz12";
					pc.getQuest().set_step(L1Quest.QUEST_SIMIZZ, L1Quest.QUEST_END);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71057) { // 도일(해적섬)
				// 러쉬에 대해 듣는다
				if (s.equalsIgnoreCase("3")) {
					htmlid = "doil4";
				} else if (s.equalsIgnoreCase("6")) {
					htmlid = "doil6";
				} else if (s.equalsIgnoreCase("1")) {
					if (pc.getInventory().checkItem(40714)) {
						htmlid = "doil8";
						materials = new int[] { 40714 };
						counts = new int[] { 1 };
						createitem = new int[] { 40647 };
						createcount = new int[] { 1 };
						pc.getQuest().set_step(L1Quest.QUEST_DOIL, L1Quest.QUEST_END);
					} else {
						htmlid = "doil7";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71059) { // 루디
																						// 안(해적섬)
				// 루디 안의 부탁을 받아들인다
				if (s.equalsIgnoreCase("A")) {
					htmlid = "rudian6";
					L1ItemInstance item = pc.getInventory().storeItem(40700, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_RUDIAN, 1);
				} else if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().checkItem(40710)) {
						htmlid = "rudian8";
						materials = new int[] { 40700, 40710 };
						counts = new int[] { 1, 1 };
						createitem = new int[] { 40647 };
						createcount = new int[] { 1 };
						pc.getQuest().set_step(L1Quest.QUEST_RUDIAN, L1Quest.QUEST_END);
					} else {
						htmlid = "rudian9";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71060) { // 레스타(해적섬)
				// 동료들에 대해
				if (s.equalsIgnoreCase("A")) {
					if (pc.getQuest().get_step(L1Quest.QUEST_RUDIAN) == L1Quest.QUEST_END) {
						htmlid = "resta6";
					} else {
						htmlid = "resta4";
					}
				} else if (s.equalsIgnoreCase("B")) {
					htmlid = "resta10";
					pc.getQuest().set_step(L1Quest.QUEST_RESTA, 2);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71061) { // 카좀스(해적섬)
				// 지도를 조합해 주세요
				if (s.equalsIgnoreCase("A")) {
					if (pc.getInventory().checkItem(40647, 3)) {
						htmlid = "cadmus6";
						pc.getInventory().consumeItem(40647, 3);
						pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 2);
					} else {
						htmlid = "cadmus5";
						pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 1);
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71062) { // 카밋트(해적섬)
				// 할아버지가 기다리고 있으니 함께 오세요
				if (s.equalsIgnoreCase("start")) {
					htmlid = "kamit2";
					final int[] item_ids = { 40711 };
					final int[] item_amounts = { 1 };
					for (int i = 0; i < item_ids.length; i++) {
						L1ItemInstance item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						pc.getQuest().set_step(L1Quest.QUEST_CADMUS, 3);
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71036) {
				htmlid = 카미라(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71089) {
				if (s.equalsIgnoreCase("a")) {
					htmlid = "francu10";
					L1ItemInstance item = pc.getInventory().storeItem(40644, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
					pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 2);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71090) {
				if (s.equalsIgnoreCase("a")) {
					htmlid = "";
					if (pc.getQuest().get_step(L1Quest.QUEST_CRYSTAL) == 0) {
						final int[] item_ids = { 246, 247, 248, 249, 40660 };
						final int[] item_amounts = { 1, 1, 1, 1, 5 };
						L1ItemInstance item = null;
						for (int i = 0; i < item_ids.length; i++) {
							item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
							pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
							pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 1);
						}
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkEquipped(246)
							|| pc.getInventory().checkEquipped(247)
							|| pc.getInventory().checkEquipped(248)
							|| pc.getInventory().checkEquipped(249)) {
						htmlid = "jcrystal5";
					} else if (pc.getInventory().checkItem(40660)) {
						htmlid = "jcrystal4";
					} else {
						pc.getInventory().consumeItem(246, 1);
						pc.getInventory().consumeItem(247, 1);
						pc.getInventory().consumeItem(248, 1);
						pc.getInventory().consumeItem(249, 1);
						pc.getInventory().consumeItem(40620, 1);
						pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 2);
						L1Teleport.teleport(pc, 32801, 32895, (short) 483, 4,true);
					}
				} else if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().checkEquipped(246)
							|| pc.getInventory().checkEquipped(247)
							|| pc.getInventory().checkEquipped(248)
							|| pc.getInventory().checkEquipped(249)) {
						htmlid = "jcrystal5";
					} else {
						pc.getInventory().checkItem(40660);
						L1ItemInstance l1iteminstance = pc.getInventory().findItemId(40660);
						int sc = l1iteminstance.getCount();
						if (sc > 0) {
							pc.getInventory().consumeItem(40660, sc);
						}
						pc.getInventory().consumeItem(246, 1);
						pc.getInventory().consumeItem(247, 1);
						pc.getInventory().consumeItem(248, 1);
						pc.getInventory().consumeItem(249, 1);
						pc.getInventory().consumeItem(40620, 1);
						pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, 0);
						L1Teleport.teleport(pc, 32736, 32800, (short) 483, 4, true);
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71091) {
				if (s.equalsIgnoreCase("a")) {
					htmlid = "";
					pc.getInventory().consumeItem(40654, 1);
					pc.getQuest().set_step(L1Quest.QUEST_CRYSTAL, L1Quest.QUEST_END);
					L1Teleport.teleport(pc, 32744, 32927, (short) 483, 4, true);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71074) {
				if (s.equalsIgnoreCase("A")) {
					htmlid = "lelder5";
					pc.getQuest().set_step(L1Quest.QUEST_LIZARD, 1);
				} else if (s.equalsIgnoreCase("B")) {
					htmlid = "lelder10";
					pc.getInventory().consumeItem(40633, 1);
					pc.getQuest().set_step(L1Quest.QUEST_LIZARD, 3);
				} else if (s.equalsIgnoreCase("C")) {
					htmlid = "lelder13";
					if (pc.getQuest().get_step(L1Quest.QUEST_LIZARD) == L1Quest.QUEST_END) {
					}
					materials = new int[] { 40634 };
					counts = new int[] { 1 };
					createitem = new int[] { 20167 }; // 리자드망로브
					createcount = new int[] { 1 };
					pc.getQuest().set_step(L1Quest.QUEST_LIZARD, L1Quest.QUEST_END);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71198) {
				if (s.equalsIgnoreCase("A")) {
					if (pc.getQuest().get_step(71198) != 0 || pc.getInventory().checkItem(21059, 1)) {
						return;
					}
					if (pc.getInventory().consumeItem(41339, 5)) {
						L1ItemInstance item = ItemTable.getInstance().createItem(41340);
						if (item != null) {
							if (pc.getInventory().checkAddItem(item, 1) == 0) {
								pc.getInventory().storeItem(item);
								pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
							}
						}
						pc.getQuest().set_step(71198, 1);
						htmlid = "tion4";
					} else {
						htmlid = "tion9";
					}
				} else if (s.equalsIgnoreCase("B")) {
					if (pc.getQuest().get_step(71198) != 1 || pc.getInventory().checkItem(21059, 1)) {
						return;
					}
					if (pc.getInventory().consumeItem(41341, 1)) {
						pc.getQuest().set_step(71198, 2);
						htmlid = "tion5";
					} else {
						htmlid = "tion10";
					}
				} else if (s.equalsIgnoreCase("C")) {
					if (pc.getQuest().get_step(71198) != 2 || pc.getInventory().checkItem(21059, 1)) {
						return;
					}
					if (pc.getInventory().consumeItem(41343, 1)) {
						L1ItemInstance item = ItemTable.getInstance().createItem(21057);
						if (item != null) {
							if (pc.getInventory().checkAddItem(item, 1) == 0) {
								pc.getInventory().storeItem(item);
								pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
							}
						}
						pc.getQuest().set_step(71198, 3);
						htmlid = "tion6";
					} else {
						htmlid = "tion12";
					}
				} else if (s.equalsIgnoreCase("D")) {
					if (pc.getQuest().get_step(71198) != 3 || pc.getInventory().checkItem(21059, 1)) {
						return;
					}
					if (pc.getInventory().consumeItem(41344, 1)) {
						L1ItemInstance item = ItemTable.getInstance().createItem(21058);
						if (item != null) {
							pc.getInventory().consumeItem(21057, 1);
							if (pc.getInventory().checkAddItem(item, 1) == 0) {
								pc.getInventory().storeItem(item);
								pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
							}
						}
						pc.getQuest().set_step(71198, 4);
						htmlid = "tion7";
					} else {
						htmlid = "tion13";
					}
				} else if (s.equalsIgnoreCase("E")) {
					if (pc.getQuest().get_step(71198) != 4 || pc.getInventory().checkItem(21059, 1)) {
						return;
					}
					if (pc.getInventory().consumeItem(41345, 1)) {
						L1ItemInstance item = ItemTable.getInstance().createItem(21059);
						if (item != null) {
							pc.getInventory().consumeItem(21058, 1);
							if (pc.getInventory().checkAddItem(item, 1) == 0) {
								pc.getInventory().storeItem(item);
								pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
							}
						}
						pc.getQuest().set_step(71198, 0);
						pc.getQuest().set_step(71199, 0);
						htmlid = "tion8";
					} else {
						htmlid = "tion15";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71199) {
				if (s.equalsIgnoreCase("A")) {
					if (pc.getQuest().get_step(71199) != 0 || pc.getInventory().checkItem(21059, 1)) {
						return;
					}
					if (pc.getInventory().checkItem(41340, 1)) {
						pc.getQuest().set_step(71199, 1);
						htmlid = "jeron2";
					} else {
						htmlid = "jeron10";
					}
				} else if (s.equalsIgnoreCase("B")) {
					if (pc.getQuest().get_step(71199) != 1 || pc.getInventory().checkItem(21059, 1)) {
						return;
					}
					if (pc.getInventory().consumeItem(L1ItemId.ADENA, 1000000)) {
						L1ItemInstance item = ItemTable.getInstance().createItem(41341);
						if (item != null) {
							if (pc.getInventory().checkAddItem(item, 1) == 0) {
								pc.getInventory().storeItem(item);
								pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
							}
						}
						pc.getInventory().consumeItem(41340, 1);
						pc.getQuest().set_step(71199, 255);
						htmlid = "jeron6";
					} else {
						htmlid = "jeron8";
					}
				} else if (s.equalsIgnoreCase("C")) {
					if (pc.getQuest().get_step(71199) != 1 || pc.getInventory().checkItem(21059, 1)) {
						return;
					}
					if (pc.getInventory().consumeItem(41342, 1)) {
						L1ItemInstance item = ItemTable.getInstance().createItem(41341);
						if (item != null) {
							if (pc.getInventory().checkAddItem(item, 1) == 0) {
								pc.getInventory().storeItem(item);
								pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
							}
						}
						pc.getInventory().consumeItem(41340, 1);
						pc.getQuest().set_step(71199, 255);
						htmlid = "jeron5";
					} else {
						htmlid = "jeron9";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80079) {
				if (s.equalsIgnoreCase("0")) {
					if (!pc.getInventory().checkItem(41312)) {
						L1ItemInstance item = pc.getInventory().storeItem(41312, 1);
						if (item != null) {
							pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
							pc.getQuest().set_step(L1Quest.QUEST_KEPLISHA, L1Quest.QUEST_END);
						}
						htmlid = "keplisha7";
					}
				} else if (s.equalsIgnoreCase("1")) {
					if (!pc.getInventory().checkItem(41314)) {
						if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
							materials = new int[] { L1ItemId.ADENA, 41313 };
							counts = new int[] { 1000, 1 };
							createitem = new int[] { 41314 };
							createcount = new int[] { 1 };
							int htmlA = _random.nextInt(3) + 1;
							int htmlB = _random.nextInt(100) + 1;
							if (htmlA == 1) {
								htmlid = "horosa" + htmlB; // horosa1 ~ horosa100
							} else if (htmlA == 2) {
								htmlid = "horosb" + htmlB; // horosb1 ~ horosb100
							} else if (htmlA == 3) {
								htmlid = "horosc" + htmlB; // horosc1 ~ horosc100
							}
						} else {
							htmlid = "keplisha8";
						}
					}
				} else if (s.equalsIgnoreCase("2")) {
					if (pc.getGfxId().getTempCharGfx() != pc.getClassId()) {
						htmlid = "keplisha9";
					} else {
						if (pc.getInventory().checkItem(41314)) {
							pc.getInventory().consumeItem(41314, 1);
							int html = _random.nextInt(9) + 1;
							int PolyId = 6180 + _random.nextInt(64);
							polyByKeplisha(client, PolyId);

							switch (html) {
							case 1:
								htmlid = "horomon11";
								break;
							case 2:
								htmlid = "horomon12";
								break;
							case 3:
								htmlid = "horomon13";
								break;
							case 4:
								htmlid = "horomon21";
								break;
							case 5:
								htmlid = "horomon22";
								break;
							case 6:
								htmlid = "horomon23";
								break;
							case 7:
								htmlid = "horomon31";
								break;
							case 8:
								htmlid = "horomon32";
								break;
							case 9:
								htmlid = "horomon33";
								break;
							default:
								break;
							}
						}
					}
				} else if (s.equalsIgnoreCase("3")) {
					if (pc.getInventory().checkItem(41312)) {
						pc.getInventory().consumeItem(41312, 1);
						htmlid = "";
					}
					if (pc.getInventory().checkItem(41313)) {
						pc.getInventory().consumeItem(41313, 1);
						htmlid = "";
					}
					if (pc.getInventory().checkItem(41314)) {
						pc.getInventory().consumeItem(41314, 1);
						htmlid = "";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80082) { // 낚시꼬마(IN)
				if (s.equalsIgnoreCase("a")) {
					if (pc.getLevel() >= 30) {
						if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
							// 토끼투구 착용해제
							int itemid[] = { 20343, 20344 };
							for (int i = 0; i < itemid.length; i++) {
								L1ItemInstance tempItem = pc.getInventory().findItemId(itemid[i]);
								if (tempItem != null && pc.getInventory().checkEquipped(tempItem.getItemId()))
									pc.getInventory().setEquipped(tempItem, false);
							}

							L1PolyMorph.undoPoly(pc);
							// L1Teleport.teleport(pc, 32742, 32799, (short)
							// 5302, 6, true);
							L1Teleport.teleport(pc, 32766, 32831, (short) 5490, 6, true);
						} else {
							htmlid = "fk_in_0";
						}
					} else {
						htmlid = "fk_in_lv";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80083) { // 낚시꼬마(OUT)
				// 낚시를 멈추어 밖에 나온다
				if (s.equalsIgnoreCase("a") || s.equalsIgnoreCase("teleport fishing-out") || s.equalsIgnoreCase("teleportURL")) {
					L1Teleport.teleport(pc, 32613, 32781, (short) 4, 4, true);
				} else if (s.equalsIgnoreCase("b")) {
					L1Teleport.teleport(pc, 32768, 32833, (short) 5490, 4, true);
					htmlid = "";
				} else if (s.equalsIgnoreCase("c")) {
					L1Teleport.teleport(pc, 32794, 32864, (short) 5490, 4, true);
					htmlid = "";
				} else if (s.equalsIgnoreCase("d")) {
					L1Teleport.teleport(pc, 32735, 32810, (short) 5490, 4, true);
					htmlid = "";
				} else if (s.equalsIgnoreCase("e")) {
					L1Teleport.teleport(pc, 32732, 32869, (short) 5490, 4, true);
					htmlid = "";
				} else if (s.equalsIgnoreCase("f")) {
					L1Teleport.teleport(pc, 32795, 32795, (short) 5490, 4, true);
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80084) {
				if (s.equalsIgnoreCase("q")) {
					if (pc.getInventory().checkItem(41356, 1)) {
						htmlid = "rparum4";
					} else {
						L1ItemInstance item = pc.getInventory().storeItem(41356, 1);
						if (item != null) {
							pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate() .get_name(), item.getItem().getName()));
						}
						htmlid = "rparum3";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80083) { // 낚시꼬마(OUT)
				// 낚시를 멈추어 밖에 나온다
				if (s.equalsIgnoreCase("O")) {
					if (!pc.getInventory().checkItem(41293, 1) && !pc.getInventory().checkItem(41294, 1)) {
						htmlid = "fk_out_0";
					} else if (pc.getInventory().consumeItem(41293, 1)) {
						L1Teleport.teleport(pc, 32613, 32781, (short) 4, 4, true);
					} else if (pc.getInventory().consumeItem(41294, 1)) {
						L1Teleport.teleport(pc, 32613, 32781, (short) 4, 4, true);
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80084) {
				if (s.equalsIgnoreCase("q")) {
					if (pc.getInventory().checkItem(41356, 1)) {
						htmlid = "rparum4";
					} else {
						L1ItemInstance item = pc.getInventory().storeItem(41356, 1);
						if (item != null) {
							pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
						htmlid = "rparum3";
					}
				}

			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80105) {
				htmlid = npc80105(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70702) {
				if (s.equalsIgnoreCase("chg")) {
					if (pc.getPartnerId() != 0) {
						/*
						 * for(L1PcInstance partner :
						 * L1World.getInstance().getVisiblePlayer(pc, 3)){
						 * if(partner.getId() == pc.getPartnerId()){ break; }
						 * return; }
						 */
						if (pc.getInventory().checkItem(40903)
								|| pc.getInventory().checkItem(40904)
								|| pc.getInventory().checkItem(40905)
								|| pc.getInventory().checkItem(40906)
								|| pc.getInventory().checkItem(40907)
								|| pc.getInventory().checkItem(40908)) {
							if (pc.getInventory().checkItem(L1ItemId.ADENA, 200000)) {
								int chargeCount = 0;
								for (int itemId = 40903; itemId <= 40908; itemId++) {
									L1ItemInstance item = pc.getInventory().findItemId(itemId);
									if (itemId == 40903 || itemId == 40904 || itemId == 40905) {
										chargeCount = itemId - 40902;
									}
									if (itemId == 40906) {
										chargeCount = 5;
									}
									if (itemId == 40907 || itemId == 40908) {
										chargeCount = 20;
									}
									if (item != null&& item.getChargeCount() != chargeCount) {
										item.setChargeCount(chargeCount);
										pc.getInventory().updateItem(item, L1PcInventory.COL_CHARGE_COUNT);
										pc.getInventory().consumeItem(L1ItemId.ADENA, 200000);
										pc.sendPackets(new S_SystemMessage("200000 아데나로 결혼반지를 충전하였습니다."));
										htmlid = "";
									}
								}
							} else {
								pc.sendPackets(new S_SystemMessage("결혼반지를 충전하기위해서는 200000 아데나가 필요합니다."));
							}
						} else {
							pc.sendPackets(new S_SystemMessage("충전해야할 반지가 없습니다."));
						}
					} else {
						pc.sendPackets(new S_SystemMessage("당신은 결혼 중이지 않습니다."));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4202000) { // 용기사 피에나
				if (s.equalsIgnoreCase("teleportURL") && pc.isDragonknight()) {
					htmlid = "feaena3";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4201000) { // 환술사 아샤
				if (s.equalsIgnoreCase("teleportURL") && pc.isIllusionist()) {
					htmlid = "asha3";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4206001) {
				if (s.equalsIgnoreCase("0")) {
					if (pc.getInventory().checkItem(L1ItemId.REMINISCING_CANDLE)) {
						htmlid = "candleg3";
					} else {
						pc.getInventory().storeItem(L1ItemId.REMINISCING_CANDLE, 1);
						htmlid = "candleg2";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200003
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200007) {
				if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().checkItem(L1ItemId.TIMECRACK_BROKENPIECE)) {
						pc.getInventory().consumeItem(L1ItemId.TIMECRACK_BROKENPIECE, 1);
						L1Teleport.teleport(pc, 33970, 33246, (short) 4, 4, true);
					} else {
						htmlid = "joegolem20";
					}
				}
				
				/*
				 * } else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId()
				 * == 4218003) { // 장로 프로켈(베히모스) if (s.equalsIgnoreCase("a")) {
				 * htmlid = "prokel3"; pc.getInventory().storeItem(210087, 1);
				 * pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 1); } else if
				 * (s.equalsIgnoreCase("b")) {
				 * if(pc.getInventory().checkItem(210088)
				 * ||pc.getInventory().checkItem(210089)
				 * ||pc.getInventory().checkItem(210090)){ htmlid = "prokel5";
				 * pc.getInventory().consumeItem(210088, 1);
				 * pc.getInventory().consumeItem(210089, 1);
				 * pc.getInventory().consumeItem(210090, 1);
				 * pc.getInventory().storeItem(410002, 1);
				 * pc.getInventory().storeItem
				 * (L1ItemId.DRAGONKNIGHTTABLET_DRAGONSKIN, 1);
				 * pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 255); }else{
				 * htmlid = "prokel6"; } } } else if (((L1NpcInstance)
				 * obj).getNpcTemplate().get_npcId() == 4219004) { // 실베리아
				 * 실레인(퀘스트) if (s.equalsIgnoreCase("a")) { htmlid = "silrein4";
				 * pc.getInventory().storeItem(210092, 5);
				 * pc.getInventory().storeItem(210093, 1);
				 * pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 1); } else if
				 * (s.equalsIgnoreCase("b")) {
				 * if(pc.getInventory().checkItem(210091, 10)
				 * ||pc.getInventory().checkItem(40510)
				 * ||pc.getInventory().checkItem(40511)
				 * ||pc.getInventory().checkItem(40512)
				 * ||pc.getInventory().checkItem(41080)){ htmlid = "silrein7";
				 * pc.getInventory().consumeItem(210091, 10);
				 * pc.getInventory().consumeItem(40510, 1);
				 * pc.getInventory().consumeItem(40511, 1);
				 * pc.getInventory().consumeItem(40512, 1);
				 * pc.getInventory().consumeItem(41080, 1);
				 * pc.getInventory().storeItem(410005, 1);
				 * pc.getInventory().storeItem
				 * (L1ItemId.MEMORIALCRYSTAL_CUBE_IGNITION, 1);
				 * pc.getQuest().set_step(L1Quest.QUEST_LEVEL15, 255); }else{
				 * htmlid = "silrein8"; } }
				 */
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4309003) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				try {
					int count = 0;
					if (!pc.isWarrior()) {
						if (s.equalsIgnoreCase(":")) {
							count = 11;
						} else if (s.equalsIgnoreCase(";")) {
							count = 12;
						} else if (s.equalsIgnoreCase("<")) {
							count = 13;
						} else if (s.equalsIgnoreCase("=")) {
							count = 14;
						} else if (s.equalsIgnoreCase(">")) {
							count = 15;
						} else if (s.equalsIgnoreCase("?")) {
							count = 16;
						} else if (s.equalsIgnoreCase("@")) {
							count = 17;
						} else if (s.equalsIgnoreCase("A")) {
							count = 18;
						} else if (s.equalsIgnoreCase("B")) {
							count = 19;
						} else if (s.equalsIgnoreCase("C")) {
							count = 20;
						} else {
							count = Integer.parseInt(s) + 1;
						}
						// if (pc.getLevel() < 70) {
						// htmlid = "sharna4";
						// pc.sendPackets(new
						// S_SystemMessage("샤르나 변신은 70레벨이 되어야 가능합니다."));
						/* } else */
						if (count <= 0) {
							htmlid = "sharna5";
						} else {
							if (pc.getInventory().checkItem(L1ItemId.ADENA, 2500 * count)) {
								int itemid = 0;
								if (pc.getLevel() >= 30 && pc.getLevel() < 40) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV30;
								} else if (pc.getLevel() >= 40
										&& pc.getLevel() < 52) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV40;
								} else if (pc.getLevel() >= 52
										&& pc.getLevel() < 55) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV52;
								} else if (pc.getLevel() >= 55
										&& pc.getLevel() < 60) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV55;
								} else if (pc.getLevel() >= 60
										&& pc.getLevel() < 65) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV60;
								} else if (pc.getLevel() >= 65
										&& pc.getLevel() < 70) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV65;
								} else if (pc.getLevel() >= 70
										&& pc.getLevel() < 75) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV70;
								} else if (pc.getLevel() >= 75
										&& pc.getLevel() < 80) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV75;
								} else if (pc.getLevel() >= 80) {
									itemid = L1ItemId.SHARNA_POLYSCROLL_LV80;
								}
								pc.getInventory().consumeItem(L1ItemId.ADENA, 2500 * count);
								L1ItemInstance item = pc.getInventory().storeItem(itemid, 1 * count);
								if (item != null) {
									String npcName = npc.getNpcTemplate().get_name();
									String itemName = item.getItem().getName();
									pc.sendPackets(new S_ServerMessage(143, npcName, itemName + (count > 1 ? " (" + count + ")" : "")));
								}
								htmlid = "sharna3";
							} else {
								htmlid = "sharna5";
							}
						}

					}
				} catch (Exception e) {
				}
			} /*else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71126) {
				int level = 70;
				if (s.equalsIgnoreCase("0")) { // 중앙 광장
					if (pc.getLevel() < level)
						htmlid = "eris21";
					else
						이리스라던입장(pc, 32838, 32769);
				} else if (s.equalsIgnoreCase("1")) { // 명법군
					if (pc.getLevel() < level)
						htmlid = "eris21";
					else
						이리스라던입장(pc, 32791, 32683);
				} else if (s.equalsIgnoreCase("2")) { // 마령군
					if (pc.getLevel() < level)
						htmlid = "eris21";
					else
						이리스라던입장(pc, 32919, 32704);
				} else if (s.equalsIgnoreCase("3")) { // 마수군
					if (pc.getLevel() < level)
						htmlid = "eris21";
					else
						이리스라던입장(pc, 32913, 32813);
				} else if (s.equalsIgnoreCase("4")) { // 암살군
					if (pc.getLevel() < level)
						htmlid = "eris21";
					else
						이리스라던입장(pc, 32743, 32841);
				} else if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().checkItem(41007, 1)) {
						htmlid = "eris10";
					} else {
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(
								41007, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(143, npcName,
								itemName));
						htmlid = "eris6";
					}
				} else if (s.equalsIgnoreCase("C")) {
					if (pc.getInventory().checkItem(41009, 1)) {
						htmlid = "eris10";
					} else {
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(
								41009, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(143, npcName,
								itemName));
						htmlid = "eris8";
					}
				} else if (s.equalsIgnoreCase("A")) {
					if (pc.getInventory().checkItem(41007, 1)) {
						if (pc.getInventory().checkItem(40969, 20)) {
							htmlid = "eris18";
							materials = new int[] { 40969, 41007 };
							counts = new int[] { 20, 1 };
							createitem = new int[] { 41008 };
							createcount = new int[] { 1 };
						} else {
							htmlid = "eris5";
						}
					} else {
						htmlid = "eris2";
					}
				} else if (s.equalsIgnoreCase("E")) {
					if (pc.getInventory().checkItem(41010, 1)) {
						htmlid = "eris19";
					} else {
						htmlid = "eris7";
					}
				} else if (s.equalsIgnoreCase("D")) {
					if (pc.getInventory().checkItem(41010, 1)) {
						htmlid = "eris19";
					} else {
						if (pc.getInventory().checkItem(41009, 1)) {
							if (pc.getInventory().checkItem(40959, 1)) {
								htmlid = "eris17";
								materials = new int[] { 40959, 41009 };
								counts = new int[] { 1, 1 };
								createitem = new int[] { 41010 };
								createcount = new int[] { 1 };
							} else if (pc.getInventory().checkItem(40960, 1)) {
								htmlid = "eris16";
								materials = new int[] { 40960, 41009 };
								counts = new int[] { 1, 1 };
								createitem = new int[] { 41010 };
								createcount = new int[] { 1 };
							} else if (pc.getInventory().checkItem(40961, 1)) {
								htmlid = "eris15";
								materials = new int[] { 40961, 41009 };
								counts = new int[] { 1, 1 };
								createitem = new int[] { 41010 };
								createcount = new int[] { 1 };
							} else if (pc.getInventory().checkItem(40962, 1)) {
								htmlid = "eris14";
								materials = new int[] { 40962, 41009 };
								counts = new int[] { 1, 1 };
								createitem = new int[] { 41010 };
								createcount = new int[] { 1 };
							} else if (pc.getInventory().checkItem(40635, 10)) {
								htmlid = "eris12";
								materials = new int[] { 40635, 41009 };
								counts = new int[] { 10, 1 };
								createitem = new int[] { 41010 };
								createcount = new int[] { 1 };
							} else if (pc.getInventory().checkItem(40638, 10)) {
								htmlid = "eris11";
								materials = new int[] { 40638, 41009 };
								counts = new int[] { 10, 1 };
								createitem = new int[] { 41010 };
								createcount = new int[] { 1 };
							} else if (pc.getInventory().checkItem(40642, 10)) {
								htmlid = "eris13";
								materials = new int[] { 40642, 41009 };
								counts = new int[] { 10, 1 };
								createitem = new int[] { 41010 };
								createcount = new int[] { 1 };
							} else if (pc.getInventory().checkItem(40667, 10)) {
								htmlid = "eris13";
								materials = new int[] { 40667, 41009 };
								counts = new int[] { 10, 1 };
								createitem = new int[] { 41010 };
								createcount = new int[] { 1 };
							} else {
								htmlid = "eris8";
							}
						} else {
							htmlid = "eris7";
						}
					}
				}
			}*/ else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80076) { // 넘어진 항해사
				if (s.equalsIgnoreCase("A")) {
					int[] diaryno = { 49082, 49083 };
					int pid = _random.nextInt(diaryno.length);
					int di = diaryno[pid];
					if (di == 49082) { // 홀수 페이지 뽑아라
						htmlid = "voyager6a";
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(di, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					} else if (di == 49083) { // 짝수 페이지 뽑아라
						htmlid = "voyager6b";
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(di, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80091) {
				/*
				 * 요리 리뉴얼로 책 주는부분 삭제 if (s.equalsIgnoreCase("A")) {
				 * if(pc.getInventory().checkItem(L1ItemId.ADENA, 10000)){
				 * pc.getInventory().consumeItem(L1ItemId.ADENA, 10000);
				 * pc.getInventory().storeItem(41255, 1); pc.sendPackets(new
				 * S_SystemMessage("요리책 상인 라폰스가 당신에게 요리책 : 1단계를 주었습니다.", true));
				 * htmlid = "rrafons1"; }else{ htmlid = "rrafons2"; } }else if
				 * (s.equalsIgnoreCase("B")) {
				 * if(!pc.getInventory().checkItem(41256)){
				 * if(pc.getInventory().checkItem(L1ItemId.ADENA, 3000)){
				 * if(pc.getInventory().checkItem(41255)){
				 * pc.getInventory().consumeItem(L1ItemId.ADENA, 3000);
				 * pc.getInventory().consumeItem(41255, 1);
				 * pc.getInventory().storeItem(41256, 1); pc.sendPackets(new
				 * S_SystemMessage("요리책 상인 라폰스가 당신에게 요리책 : 2단계를 주었습니다.", true));
				 * htmlid = "rrafons4"; }else{ htmlid = "rrafons5"; } }else{
				 * htmlid = "rrafons2"; } }else{ htmlid = "rrafons3"; } }else if
				 * (s.equalsIgnoreCase("q")) {
				 * if(!pc.getInventory().checkItem(41257)){
				 * if(pc.getInventory().checkItem(L1ItemId.ADENA, 5000)){
				 * if(pc.getInventory().checkItem(41256)){
				 * pc.getInventory().consumeItem(L1ItemId.ADENA, 5000);
				 * pc.getInventory().consumeItem(41256, 1);
				 * pc.getInventory().storeItem(41257, 1); pc.sendPackets(new
				 * S_SystemMessage("요리책 상인 라폰스가 당신에게 요리책 : 3단계를 주었습니다.", true));
				 * htmlid = "rrafons10"; }else{ htmlid = "rrafons11"; } }else{
				 * htmlid = "rrafons2"; } }else{ htmlid = "rrafons9"; } }
				 */
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71128) { // 연금  술사 페리타
				if (s.equals("A")) {
					if (pc.getInventory().checkItem(41010, 1)) { // 이리스의 추천서
						htmlid = "perita2";
					} else {
						htmlid = "perita3";
					}
				} else if (s.equals("p")) {
					// 저주해진 블랙 귀 링 판별
					if (pc.getInventory().checkItem(40987, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(40988, 1) // 나이트 클래스
							&& pc.getInventory().checkItem(40989, 1)) { // 워리아크라스
						htmlid = "perita43";
					} else if (pc.getInventory().checkItem(40987, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(40989, 1)) { // 워리아크라스
						htmlid = "perita44";
					} else if (pc.getInventory().checkItem(40987, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(40988, 1)) { // 나이트 클래스
						htmlid = "perita45";
					} else if (pc.getInventory().checkItem(40988, 1) // 나이트 클래스
							&& pc.getInventory().checkItem(40989, 1)) { // 워리아크라스
						htmlid = "perita47";
					} else if (pc.getInventory().checkItem(40987, 1)) { // 위저드 클래스
						htmlid = "perita46";
					} else if (pc.getInventory().checkItem(40988, 1)) { // 나이트 클래스
						htmlid = "perita49";
					} else if (pc.getInventory().checkItem(40987, 1)) { // 워리아크라스
						htmlid = "perita48";
					} else {
						htmlid = "perita50";
					}
				} else if (s.equals("q")) {
					// 블랙 귀 링 판별
					if (pc.getInventory().checkItem(41173, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(41174, 1) // 나이트 클래스
							&& pc.getInventory().checkItem(41175, 1)) { // 워리아크라스
						htmlid = "perita54";
					} else if (pc.getInventory().checkItem(41173, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(41175, 1)) { // 워리아크라스
						htmlid = "perita55";
					} else if (pc.getInventory().checkItem(41173, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(41174, 1)) { // 나이트 클래스
						htmlid = "perita56";
					} else if (pc.getInventory().checkItem(41174, 1) // 나이트 클래스
							&& pc.getInventory().checkItem(41175, 1)) { // 워리아크라스
						htmlid = "perita58";
					} else if (pc.getInventory().checkItem(41174, 1)) { // 위저드 클래스
						htmlid = "perita57";
					} else if (pc.getInventory().checkItem(41175, 1)) { // 나이트 클래스
						htmlid = "perita60";
					} else if (pc.getInventory().checkItem(41176, 1)) { // 워리아크라스
						htmlid = "perita59";
					} else {
						htmlid = "perita61";
					}
				} else if (s.equals("s")) {
					// 신비적인 블랙 귀 링 판별
					if (pc.getInventory().checkItem(41161, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(41162, 1) // 나이트 클래스
							&& pc.getInventory().checkItem(41163, 1)) { // 워리아크라스
						htmlid = "perita62";
					} else if (pc.getInventory().checkItem(41161, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(41163, 1)) { // 워리아크라스
						htmlid = "perita63";
					} else if (pc.getInventory().checkItem(41161, 1) // 위저드 클래스
							&& pc.getInventory().checkItem(41162, 1)) { // 나이트 클래스
						htmlid = "perita64";
					} else if (pc.getInventory().checkItem(41162, 1) // 나이트 클래스
							&& pc.getInventory().checkItem(41163, 1)) { // 워리아크라스
						htmlid = "perita66";
					} else if (pc.getInventory().checkItem(41161, 1)) { // 위저드 클래스
						htmlid = "perita65";
					} else if (pc.getInventory().checkItem(41162, 1)) { // 나이트 클래스
						htmlid = "perita68";
					} else if (pc.getInventory().checkItem(41163, 1)) { // 워리아크라스
						htmlid = "perita67";
					} else {
						htmlid = "perita69";
					}
				} else if (s.equals("B")) {
					// 정화의 일부
					if (pc.getInventory().checkItem(40651, 10) // 불의 숨결
							&& pc.getInventory().checkItem(40643, 10) // 수의 숨결
							&& pc.getInventory().checkItem(40618, 10) // 대지의 숨결
							&& pc.getInventory().checkItem(40645, 10) // 돌풍이 심함 취
							&& pc.getInventory().checkItem(40676, 10) // 어둠의 숨결
							&& pc.getInventory().checkItem(40442, 5) // 프롭브의 위액
							&& pc.getInventory().checkItem(40051, 1)) { // 고급 에메랄드
						htmlid = "perita7";
						materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40442, 40051 };
						counts = new int[] { 10, 10, 10, 10, 20, 5, 1 };
						createitem = new int[] { 40925 }; // 정화의 일부
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita8";
					}
				} else if (s.equals("G") || s.equals("h") || s.equals("i")) {
					// 신비적인 일부：1 단계
					if (pc.getInventory().checkItem(40651, 5) // 불의 숨결
							&& pc.getInventory().checkItem(40643, 5) // 수의 숨결
							&& pc.getInventory().checkItem(40618, 5) // 대지의 숨결
							&& pc.getInventory().checkItem(40645, 5) // 돌풍이 심함 취
							&& pc.getInventory().checkItem(40676, 5) // 어둠의 숨결
							&& pc.getInventory().checkItem(40675, 5) // 어둠의 광석
							&& pc.getInventory().checkItem(40049, 3) // 고급 루비
							&& pc.getInventory().checkItem(40051, 1)) { // 고급
																		// 에메랄드
						htmlid = "perita27";
						materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40675, 40049, 40051 };
						counts = new int[] { 5, 5, 5, 5, 10, 10, 3, 1 };
						createitem = new int[] { 40926 }; // 신비적인 일부：1 단계
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita28";
					}
				} else if (s.equals("H") || s.equals("j") || s.equals("k")) {
					// 신비적인 일부：2 단계
					if (pc.getInventory().checkItem(40651, 10) // 불의 숨결
							&& pc.getInventory().checkItem(40643, 10) // 수의 숨결
							&& pc.getInventory().checkItem(40618, 10) // 대지의 숨결
							&& pc.getInventory().checkItem(40645, 10) // 돌풍이 심함 취
							&& pc.getInventory().checkItem(40676, 20) // 어둠의 숨결
							&& pc.getInventory().checkItem(40675, 10) // 어둠의 광석
							&& pc.getInventory().checkItem(40048, 3) // 고급 다이아몬드
							&& pc.getInventory().checkItem(40051, 1)) { // 고급
																		// 에메랄드
						htmlid = "perita29";
						materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40675, 40048, 40051 };
						counts = new int[] { 10, 10, 10, 10, 20, 10, 3, 1 };
						createitem = new int[] { 40927 }; // 신비적인 일부：2 단계
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita30";
					}
				} else if (s.equals("I") || s.equals("l") || s.equals("m")) {
					// 신비적인 일부：3 단계
					if (pc.getInventory().checkItem(40651, 20) // 불의 숨결
							&& pc.getInventory().checkItem(40643, 20) // 수의 숨결
							&& pc.getInventory().checkItem(40618, 20) // 대지의 숨결
							&& pc.getInventory().checkItem(40645, 20) // 돌풍이 심함 취
							&& pc.getInventory().checkItem(40676, 30) // 어둠의 숨결
							&& pc.getInventory().checkItem(40675, 10) // 어둠의 광석
							&& pc.getInventory().checkItem(40050, 3) // 고급 사파이어
							&& pc.getInventory().checkItem(40051, 1)) { // 고급 에메랄드
						htmlid = "perita31";
						materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40675, 40050, 40051 };
						counts = new int[] { 20, 20, 20, 20, 30, 10, 3, 1 };
						createitem = new int[] { 40928 }; // 신비적인 일부：3 단계
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita32";
					}
				} else if (s.equals("J") || s.equals("n") || s.equals("o")) {
					// 신비적인 일부：4 단계
					if (pc.getInventory().checkItem(40651, 30) // 불의 숨결
							&& pc.getInventory().checkItem(40643, 30) // 수의 숨결
							&& pc.getInventory().checkItem(40618, 30) // 대지의 숨결
							&& pc.getInventory().checkItem(40645, 30) // 돌풍이 심함 취
							&& pc.getInventory().checkItem(40676, 30) // 어둠의 숨결
							&& pc.getInventory().checkItem(40675, 20) // 어둠의 광석
							&& pc.getInventory().checkItem(40052, 1) // 최고급 다이아몬드
							&& pc.getInventory().checkItem(40051, 1)) { // 고급 에메랄드
						htmlid = "perita33";
						materials = new int[] { 40651, 40643, 40618, 40645, 40676, 40675, 40052, 40051 };
						counts = new int[] { 30, 30, 30, 30, 30, 20, 1, 1 };
						createitem = new int[] { 40928 }; // 신비적인 일부：4 단계
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita34";
					}
				} else if (s.equals("K")) { // 1 단계 귀 링(영혼의 귀 링)
					int earinga = 0;
					int earingb = 0;
					if (pc.getInventory().checkEquipped(21014)
							|| pc.getInventory().checkEquipped(21006)
							|| pc.getInventory().checkEquipped(21007)) {
						htmlid = "perita36";
					} else if (pc.getInventory().checkItem(21014, 1)) { // 위저드 클래스
						earinga = 21014;
						earingb = 41176;
					} else if (pc.getInventory().checkItem(21006, 1)) { // 나이트 클래스
						earinga = 21006;
						earingb = 41177;
					} else if (pc.getInventory().checkItem(21007, 1)) { // 워리아크라스
						earinga = 21007;
						earingb = 41178;
					} else {
						htmlid = "perita36";
					}
					if (earinga > 0) {
						materials = new int[] { earinga };
						counts = new int[] { 1 };
						createitem = new int[] { earingb };
						createcount = new int[] { 1 };
					}
				} else if (s.equals("L")) { // 2 단계 귀 링(지혜의 귀 링)
					if (pc.getInventory().checkEquipped(21015)) {
						htmlid = "perita22";
					} else if (pc.getInventory().checkItem(21015, 1)) {
						materials = new int[] { 21015 };
						counts = new int[] { 1 };
						createitem = new int[] { 41179 };
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita22";
					}
				} else if (s.equals("M")) { // 3 단계 귀 링(진실의 귀 링)
					if (pc.getInventory().checkEquipped(21016)) {
						htmlid = "perita26";
					} else if (pc.getInventory().checkItem(21016, 1)) {
						materials = new int[] { 21016 };
						counts = new int[] { 1 };
						createitem = new int[] { 41182 };
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita26";
					}
				} else if (s.equals("b")) { // 2 단계 귀 링(정열의 귀 링)
					if (pc.getInventory().checkEquipped(21009)) {
						htmlid = "perita39";
					} else if (pc.getInventory().checkItem(21009, 1)) {
						materials = new int[] { 21009 };
						counts = new int[] { 1 };
						createitem = new int[] { 41180 };
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita39";
					}
				} else if (s.equals("d")) { // 3 단계 귀 링(명예의 귀 링)
					if (pc.getInventory().checkEquipped(21012)) {
						htmlid = "perita41";
					} else if (pc.getInventory().checkItem(21012, 1)) {
						materials = new int[] { 21012 };
						counts = new int[] { 1 };
						createitem = new int[] { 41183 };
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita41";
					}
				} else if (s.equals("a")) { // 2 단계 귀 링(분노의 귀 링)
					if (pc.getInventory().checkEquipped(21008)) {
						htmlid = "perita38";
					} else if (pc.getInventory().checkItem(21008, 1)) {
						materials = new int[] { 21008 };
						counts = new int[] { 1 };
						createitem = new int[] { 41181 };
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita38";
					}
				} else if (s.equals("c")) { // 3 단계 귀 링(용맹의 귀 링)
					if (pc.getInventory().checkEquipped(21010)) {
						htmlid = "perita40";
					} else if (pc.getInventory().checkItem(21010, 1)) {
						materials = new int[] { 21010 };
						counts = new int[] { 1 };
						createitem = new int[] { 41184 };
						createcount = new int[] { 1 };
					} else {
						htmlid = "perita40";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71129) { // 보석 세공인 룸스
				if (s.equals("Z")) {
					htmlid = "rumtis2";
				} else if (s.equals("Y")) {
					if (pc.getInventory().checkItem(41010, 1)) { // 이리스의 추천서
						htmlid = "rumtis3";
					} else {
						htmlid = "rumtis4";
					}
				} else if (s.equals("q")) {
					htmlid = "rumtis92";
				} else if (s.equals("A")) {
					if (pc.getInventory().checkItem(41161, 1)) {
						// 신비적인 블랙 귀 링
						htmlid = "rumtis6";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("B")) {
					if (pc.getInventory().checkItem(41164, 1)) {
						// 신비적인 위저드 귀 링
						htmlid = "rumtis7";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("C")) {
					if (pc.getInventory().checkItem(41167, 1)) {
						// 신비적인 회색 위저드 귀 링
						htmlid = "rumtis8";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("T")) {
					if (pc.getInventory().checkItem(41167, 1)) {
						// 신비적인 화이트 위저드 귀 링
						htmlid = "rumtis9";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("w")) {
					if (pc.getInventory().checkItem(41162, 1)) {
						// 신비적인 블랙 귀 링
						htmlid = "rumtis14";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("x")) {
					if (pc.getInventory().checkItem(41165, 1)) {
						// 신비적인 나이트 귀 링
						htmlid = "rumtis15";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("y")) {
					if (pc.getInventory().checkItem(41168, 1)) {
						// 신비적인 회색 나이트 귀 링
						htmlid = "rumtis16";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("z")) {
					if (pc.getInventory().checkItem(41171, 1)) {
						// 신비적인 화이트 나이트 귀 링
						htmlid = "rumtis17";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("U")) {
					if (pc.getInventory().checkItem(41163, 1)) {
						// 신비적인 블랙 귀 링
						htmlid = "rumtis10";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("V")) {
					if (pc.getInventory().checkItem(41166, 1)) {
						// 미스테리아스워리아이아링
						htmlid = "rumtis11";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("W")) {
					if (pc.getInventory().checkItem(41169, 1)) {
						// 미스테리아스그레이워리아이아링
						htmlid = "rumtis12";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("X")) {
					if (pc.getInventory().checkItem(41172, 1)) {
						// 미스테리아스화이워리아이아링
						htmlid = "rumtis13";
					} else {
						htmlid = "rumtis101";
					}
				} else if (s.equals("D") || s.equals("E") || s.equals("F") || s.equals("G")) {
					int insn = 0;
					int bacn = 0;
					int me = 0;
					int mr = 0;
					int mj = 0;
					int an = 0;
					int men = 0;
					int mrn = 0;
					int mjn = 0;
					int ann = 0;
					if (pc.getInventory().checkItem(40959, 1) // 명법군왕의 인장
							&& pc.getInventory().checkItem(40960, 1) // 마령군왕의 인장
							&& pc.getInventory().checkItem(40961, 1) // 마수군왕의 인장
							&& pc.getInventory().checkItem(40962, 1)) { // 암살군왕의 인장
						insn = 1;
						me = 40959;
						mr = 40960;
						mj = 40961;
						an = 40962;
						men = 1;
						mrn = 1;
						mjn = 1;
						ann = 1;
					} else if (pc.getInventory().checkItem(40642, 10) // 명법군의 배지
							&& pc.getInventory().checkItem(40635, 10) // 마령군의 배지
							&& pc.getInventory().checkItem(40638, 10) // 마수군의 배지
							&& pc.getInventory().checkItem(40667, 10)) { // 암살군의 배지
						bacn = 1;
						me = 40642;
						mr = 40635;
						mj = 40638;
						an = 40667;
						men = 10;
						mrn = 10;
						mjn = 10;
						ann = 10;
					}
					if (pc.getInventory().checkItem(40046, 1) // 사파이어
							&& pc.getInventory().checkItem(40618, 5) // 대지의 숨결
							&& pc.getInventory().checkItem(40643, 5) // 수의 숨결
							&& pc.getInventory().checkItem(40645, 5) // 돌풍이 심함 취
							&& pc.getInventory().checkItem(40651, 5) // 불의 숨결
							&& pc.getInventory().checkItem(40676, 5)) { // 어둠의 숨결
						if (insn == 1 || bacn == 1) {
							htmlid = "rumtis60";
							materials = new int[] { me, mr, mj, an, 40046, 40618, 40643, 40651, 40676 };
							counts = new int[] { men, mrn, mjn, ann, 1, 5, 5, 5, 5, 5 };
							createitem = new int[] { 40926 }; // 가공된 사파이어：1 단계
							createcount = new int[] { 1 };
						} else {
							htmlid = "rumtis18";
						}
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71119) {
				if (s.equalsIgnoreCase("request las history book")) {
					materials = new int[] { 41019, 41020, 41021, 41022, 41023, 41024, 41025, 41026 };
					counts = new int[] { 1, 1, 1, 1, 1, 1, 1, 1 };
					createitem = new int[] { 41027 };
					createcount = new int[] { 1 };
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71170) {
				if (s.equalsIgnoreCase("request las weapon manual")) {
					materials = new int[] { 41027 };
					counts = new int[] { 1 };
					createitem = new int[] { 40965 };
					createcount = new int[] { 1 };
					htmlid = "";
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 6000015) {
				if (s.equalsIgnoreCase("1")) {
					L1Teleport.teleport(pc, 33966, 33253, (short) 4, 5, true);
				} else if (s.equalsIgnoreCase("a")
						&& pc.getInventory().checkItem(41158, 10)) {
					L1Teleport.teleport(pc, 32800, 32800, (short) 110, 5, true);
					pc.getInventory().consumeItem(41158, 10);
				} else if (s.equalsIgnoreCase("b")
						&& pc.getInventory().checkItem(41158, 20)) {
					L1Teleport.teleport(pc, 32800, 32800, (short) 120, 5, true);
					pc.getInventory().consumeItem(41158, 20);
				} else if (s.equalsIgnoreCase("c")
						&& pc.getInventory().checkItem(41158, 30)) {
					L1Teleport.teleport(pc, 32800, 32800, (short) 130, 5, true);
					pc.getInventory().consumeItem(41158, 30);
				} else if (s.equalsIgnoreCase("d")
						&& pc.getInventory().checkItem(41158, 40)) {
					L1Teleport.teleport(pc, 32800, 32800, (short) 140, 5, true);
					pc.getInventory().consumeItem(41158, 40);
				} else if (s.equalsIgnoreCase("e")
						&& pc.getInventory().checkItem(41158, 50)) {
					L1Teleport.teleport(pc, 32796, 32796, (short) 150, 5, true);
					pc.getInventory().consumeItem(41158, 50);
				} else if (s.equalsIgnoreCase("f")
						&& pc.getInventory().checkItem(41158, 60)) {
					L1Teleport.teleport(pc, 32720, 32821, (short) 160, 5, true);
					pc.getInventory().consumeItem(41158, 60);
				} else if (s.equalsIgnoreCase("g")
						&& pc.getInventory().checkItem(41158, 70)) {
					L1Teleport.teleport(pc, 32720, 32821, (short) 170, 5, true);
					pc.getInventory().consumeItem(41158, 70);
				} else if (s.equalsIgnoreCase("h")
						&& pc.getInventory().checkItem(41158, 80)) {
					L1Teleport.teleport(pc, 32724, 32822, (short) 180, 5, true);
					pc.getInventory().consumeItem(41158, 80);
				} else if (s.equalsIgnoreCase("i")
						&& pc.getInventory().checkItem(41158, 90)) {
					L1Teleport.teleport(pc, 32722, 32827, (short) 190, 5, true);
					pc.getInventory().consumeItem(41158, 90);
				} else if (s.equalsIgnoreCase("j")
						&& pc.getInventory().checkItem(41158, 100)) {
					L1Teleport.teleport(pc, 32731, 32856, (short) 200, 5, true);
					pc.getInventory().consumeItem(41158, 100);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 80067) { // 첩보원(욕망의 동굴측)
				// 동요하면서도 승낙한다
				if (s.equalsIgnoreCase("n")) {
					htmlid = "";
					poly(client, 6034);
					final int[] item_ids = { 41132, 41133, 41134 };
					final int[] item_amounts = { 1, 1, 1 };
					L1ItemInstance item = null;
					for (int i = 0; i < item_ids.length; i++) {
						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						pc.getQuest().set_step(L1Quest.QUEST_DESIRE, 1);
					}
					// 그런 임무는 그만둔다
				} else if (s.equalsIgnoreCase("d")) {
					htmlid = "minicod09";
					pc.getInventory().consumeItem(41130, 1);
					pc.getInventory().consumeItem(41131, 1);
					// 초기화한다
				} else if (s.equalsIgnoreCase("k")) {
					htmlid = "";
					pc.getInventory().consumeItem(41132, 1); // 핏자국의 타락 한 가루
					pc.getInventory().consumeItem(41133, 1); // 핏자국의 무력 한 가루
					pc.getInventory().consumeItem(41134, 1); // 핏자국의 아집 한 가루
					pc.getInventory().consumeItem(41135, 1); // 카헬의 타락 한 정수
					pc.getInventory().consumeItem(41136, 1); // 카헬의 무력 한 정수
					pc.getInventory().consumeItem(41137, 1); // 카헬의 아집 한 정수
					pc.getInventory().consumeItem(41138, 1); // 카헬의 정수
					pc.getQuest().set_step(L1Quest.QUEST_DESIRE, 0);
					// 정수를 건네준다
				} else if (s.equalsIgnoreCase("e")) {
					if (pc.getQuest().get_step(L1Quest.QUEST_DESIRE) == L1Quest.QUEST_END || pc.getKarmaLevel() >= 1) {
						htmlid = "";
					} else {
						if (pc.getInventory().checkItem(41138)) {
							htmlid = "";
							pc.addKarma((int) (1600 * Config.RATE_KARMA));
							pc.getInventory().consumeItem(41130, 1); // 핏자국의 계약서
							pc.getInventory().consumeItem(41131, 1); // 핏자국의 지령서
							pc.getInventory().consumeItem(41138, 1); // 카헬의 정수
							pc.getQuest().set_step(L1Quest.QUEST_DESIRE, L1Quest.QUEST_END);
						} else {
							htmlid = "minicod04";
						}
					}
					// 선물을 받는다
				} else if (s.equalsIgnoreCase("g")) {
					L1ItemInstance item = pc.getInventory().storeItem(41130, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4204000) { // 달장퀘스트 로빈후드
				if (s.equals("A")) { /* robinhood1~7 */
					if (pc.getInventory().checkItem(40068)) { /* 사과주스 체크 40028 리뉴얼로 와퍼로 교체  */
						pc.getInventory().consumeItem(40068, 1); /* 사과주스 소비 */
						pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 1); /*  1단계 완료 */
						htmlid = "robinhood4";
					} else {
						htmlid = "robinhood19";
					}
				} else if (s.equals("B")) { /* robinhood8 */
					final int[] item_ids = { 41346, 41348 };
					final int[] item_amounts = { 1, 1, };
					L1ItemInstance item = null;
					for (int i = 0; i < item_ids.length; i++) {
						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
						pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 2);
						htmlid = "robinhood13";
					}
				} else if (s.equals("C")) { /* robinhood9 */
					if (pc.getInventory().checkItem(41346)
							&& pc.getInventory().checkItem(41351)
							&& pc.getInventory().checkItem(41352, 4)
							&& pc.getInventory().checkItem(40618, 30)
							&& pc.getInventory().checkItem(40643, 30)
							&& pc.getInventory().checkItem(40645, 30)
							&& pc.getInventory().checkItem(40651, 30)
							&& pc.getInventory().checkItem(40676, 30)) {
						pc.getInventory().consumeItem(41346, 1); /* 메모장, 정기, 유뿔, 불, 물, 바람, 대지 어둠숨결 */
						pc.getInventory().consumeItem(41351, 1);
						pc.getInventory().consumeItem(41352, 4);
						pc.getInventory().consumeItem(40651, 30);
						pc.getInventory().consumeItem(40643, 30);
						pc.getInventory().consumeItem(40645, 30);
						pc.getInventory().consumeItem(40618, 30);
						pc.getInventory().consumeItem(40676, 30);
						final int[] item_ids = { 41350, 41347 };
						final int[] item_amounts = { 1, 1, };
						L1ItemInstance item = null;
						for (int i = 0; i < item_ids.length; i++) {
							item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
							pc.sendPackets(new S_ServerMessage(403, item .getLogName()));
						}
						pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 7); /* 7단계  완료  */
						htmlid = "robinhood10"; /* 나머지 재료를 찾아오게.. */
					} else {
						htmlid = "robinhood15"; /* 달빛정기, 유뿔 가져왔는가 */
					}
				} else if (s.equals("E")) { /* robinhood11 */
					if (pc.getInventory().checkItem(41350)
							&& pc.getInventory().checkItem(41347)
							&& pc.getInventory().checkItem(40491, 30)
							&& pc.getInventory().checkItem(40495, 40)
							&& pc.getInventory().checkItem(100)
							&& pc.getInventory().checkItem(40509, 12)
							&& pc.getInventory().checkItem(40052)
							&& pc.getInventory().checkItem(40053)
							&& pc.getInventory().checkItem(40054)
							&& pc.getInventory().checkItem(40055)) {
						pc.getInventory().consumeItem(41350, 1); /* 반지, 메모지, 그리폰깃털, 미스릴실, 오리뿔, 오판, 최고급보석1개씩  */
						pc.getInventory().consumeItem(41347, 1);
						pc.getInventory().consumeItem(40491, 30);
						pc.getInventory().consumeItem(40495, 40);
						pc.getInventory().consumeItem(100, 1);
						pc.getInventory().consumeItem(40509, 12);
						pc.getInventory().consumeItem(40052, 1);
						pc.getInventory().consumeItem(40053, 1);
						pc.getInventory().consumeItem(40054, 1);
						pc.getInventory().consumeItem(40055, 1);
						final int[] item_ids = { 205 };
						final int[] item_amounts = { 1 };
						L1ItemInstance item = null;
						for (int i = 0; i < item_ids.length; i++) {
							item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
							pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						}
						pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 0); /* 퀘스트 리셋 */
						htmlid = "robinhood12"; /* 완성이야 */
					} else {
						htmlid = "robinhood17"; /* 재료가 부족한걸 */
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4210000) {
				if (s.equals("A")) { /* zybril1 ~ zybril6 */
					if (pc.getInventory().checkItem(41348)) { /* 소개장 */
						pc.getInventory().consumeItem(41348, 1);
						pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 3); /* 3단계 완료 */
						htmlid = "zybril13"; /* 아 그 활쟁이 */
					} else {
						htmlid = "zybril11"; /* 편지는 어디에? */
					}
				} else if (s.equals("B")) { /* zybril7 */
					if (pc.getInventory().checkItem(40048, 10)
							&& pc.getInventory().checkItem(40049, 10)
							&& pc.getInventory().checkItem(40050, 10)
							&& pc.getInventory().checkItem(40051, 10)) {
						pc.getInventory().consumeItem(40048, 10); /* 고다, 고루, 고사, 고에 */
						pc.getInventory().consumeItem(40049, 10);
						pc.getInventory().consumeItem(40050, 10);
						pc.getInventory().consumeItem(40051, 10);
						pc.getInventory().storeItem(41353, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "에바의 단검"), true);
						pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 4); /*  4단계 완료  */
						htmlid = "zybril12"; /* 기부금을 받을게요 */
					} else {
						htmlid = "";
					}
				} else if (s.equals("C")) { /* zybril8 */
					if (pc.getInventory().checkItem(40514, 10)
							&& pc.getInventory().checkItem(41353, 1)) {
						pc.getInventory().consumeItem(40514, 10); /* 정령의 눈물, 에바의  단검  */
						pc.getInventory().consumeItem(41353, 1);
						pc.getInventory().storeItem(41354, 1);
						pc.sendPackets(new S_ServerMessage(143,
								((L1NpcInstance) obj).getName(), "신성한 에바의 물"),
								true);
						pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 5); /* 5단계  완료 */
						htmlid = "zybril9"; /* 고마워요 한가지부탁더 */
					} else {
						htmlid = "zybril13"; /* 정령의눈물이 필요합니다.. */
					}
				} else if (s.equals("D")) { /* zybril18 */
					if (pc.getInventory().checkItem(41349)) { /* 사엘의 반지 */
						pc.getInventory().consumeItem(41349, 1);
						pc.getInventory().storeItem(41351, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "달빛의 정기"), true);
						pc.getQuest().set_step(L1Quest.QUEST_MOONBOW, 6); /*  6단계 완료  */
						htmlid = "zybril10"; /* 달빛의 정기를 받으세요 */
					} else {
						htmlid = "zybril14"; /* 어떻게 믿죠? */
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 71179) {// 디에츠(빛고목 제작)
				if (s.equalsIgnoreCase("A")) {// 복원된 고대의 목걸이
					Random random = new Random();
					if (pc.getInventory().checkItem(49028, 1)
							&& pc.getInventory().checkItem(49029, 1)
							&& pc.getInventory().checkItem(49030, 1)
							&& pc.getInventory().checkItem(41139, 1)) { // 보석과 볼품없는 목걸이 확인
						if (random.nextInt(10) > 6) {
							materials = new int[] { 49028, 49029, 49030, 41139 };
							counts = new int[] { 1, 1, 1, 1 };
							createitem = new int[] { 41140 }; // 복원된 고대의 목걸이
							createcount = new int[] { 1 };
							htmlid = "dh8";
						} else { // 실패의 경우 아이템만 사라짐
							materials = new int[] { 49028, 49029, 49030, 41139 };
							counts = new int[] { 1, 1, 1, 1 };
							createitem = new int[] { L1ItemId.GEMSTONE_POWDER }; // 보석 가루
							createcount = new int[] { 5 };
							htmlid = "dh7";
						}
					} else { // 재료가 부족한 경우
						htmlid = "dh6";
					}
				} else if (s.equalsIgnoreCase("B")) {// 빛나는 고대의 목걸이 제작을 부탁한다.
					Random random = new Random();
					if (pc.getInventory().checkItem(49027, 1)
							&& pc.getInventory().checkItem(41140, 1)) { // 다이아몬드와 복원된 목걸이
						if (random.nextInt(10) > 7) {
							materials = new int[] { 49027, 41140 };
							counts = new int[] { 1, 1 };
							createitem = new int[] { 20422 }; // 빛나는 고대 목걸이
							createcount = new int[] { 1 };
							htmlid = "dh9";
						} else {
							materials = new int[] { 49027, 41140 };
							counts = new int[] { 1, 1 };
							createitem = new int[] { L1ItemId.GEMSTONE_POWDER }; // 보석가루
							createcount = new int[] { 5 };
							htmlid = "dh7";
						}
					} else { // 재료가 부족한 경우
						htmlid = "dh6";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 81202) { // 첩보원(그림자의 신전측)
				// 화가 나지만 승낙한다
				if (s.equalsIgnoreCase("n")) {
					htmlid = "";
					poly(client, 6035);
					final int[] item_ids = { 41123, 41124, 41125 };
					final int[] item_amounts = { 1, 1, 1 };
					L1ItemInstance item = null;
					for (int i = 0; i < item_ids.length; i++) {
						item = pc.getInventory().storeItem(item_ids[i], item_amounts[i]);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
						pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, 1);
					}
					// 그런 임무는 그만둔다
				} else if (s.equalsIgnoreCase("d")) {
					htmlid = "minitos09";
					pc.getInventory().consumeItem(41121, 1);
					pc.getInventory().consumeItem(41122, 1);
					// 초기화한다
				} else if (s.equalsIgnoreCase("k")) {
					htmlid = "";
					pc.getInventory().consumeItem(41123, 1); // 카헬의 타락 한 가루
					pc.getInventory().consumeItem(41124, 1); // 카헬의 무력 한 가루
					pc.getInventory().consumeItem(41125, 1); // 카헬의 아집 한 가루
					pc.getInventory().consumeItem(41126, 1); // 핏자국의 타락 한 정수
					pc.getInventory().consumeItem(41127, 1); // 핏자국의 무력 한 정수
					pc.getInventory().consumeItem(41128, 1); // 핏자국의 아집 한 정수
					pc.getInventory().consumeItem(41129, 1); // 핏자국의 정수
					pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, 0);
					// 정수를 건네준다
				} else if (s.equalsIgnoreCase("e")) {
					if (pc.getQuest().get_step(L1Quest.QUEST_SHADOWS) == L1Quest.QUEST_END || pc.getKarmaLevel() >= 1) {
						htmlid = "";
					} else {
						if (pc.getInventory().checkItem(41129)) {
							htmlid = "";
							pc.addKarma((int) (-1600 * Config.RATE_KARMA));
							pc.getInventory().consumeItem(41121, 1); // 카헬의 계약서
							pc.getInventory().consumeItem(41122, 1); // 카헬의 지령서
							pc.getInventory().consumeItem(41129, 1); // 핏자국의 정수
							pc.getQuest().set_step(L1Quest.QUEST_SHADOWS, L1Quest.QUEST_END);
						} else {
							htmlid = "minitos04";
						}
					}
					// 재빠르게 받는다
				} else if (s.equalsIgnoreCase("g")) {
					L1ItemInstance item = pc.getInventory().storeItem(41121, 1);
					pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getNpcTemplate().get_name(), item.getItem().getName()));
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70842) { // 마르바
				if (pc.getInventory().checkItem(40665)) {
					htmlid = "marba17";
					if (s.equalsIgnoreCase("B")) {
						htmlid = "marba7";
						if (pc.getInventory().checkItem(214)
								&& pc.getInventory().checkItem(20389)
								&& pc.getInventory().checkItem(20393)
								&& pc.getInventory().checkItem(20401)
								&& pc.getInventory().checkItem(20406)
								&& pc.getInventory().checkItem(20409)) {
							htmlid = "marba15";
						}
					}
				} else if (s.equalsIgnoreCase("A")) {
					if (pc.getInventory().checkItem(40637)) {
						htmlid = "marba20";
					} else {
						L1NpcInstance npc = (L1NpcInstance) obj;
						L1ItemInstance item = pc.getInventory().storeItem(40637, 1);
						String npcName = npc.getNpcTemplate().get_name();
						String itemName = item.getItem().getName();
						pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
						htmlid = "marba6";
					}
				}

			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 777017) { // 악마왕
				if (s.equalsIgnoreCase("b")) {
					htmlid = "";
					if (DevilController.getInstance().getDevilStart() == true) {
						Random random = new Random();
						int i13 = 32723 + random.nextInt(10);
						int k19 = 32800 + random.nextInt(10);
						L1Teleport.teleport(pc, i13, k19, (short) 5167, 6, true);
						pc.sendPackets(new S_SystemMessage("영토가 열린시각으로부터 1시간동안 입장가능합니다."));
						return;
					} else {
						pc.sendPackets(new S_SystemMessage("악마왕의영토: 입장시간이 아닙니다"));
						return;
					}
				}



			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100282) { // 버모스 용의던전
				if (s.equalsIgnoreCase("1")) {
					htmlid = "";
					int level = 60;
					if (pc.getLevel() >= level && pc.getLevel() < 99) {
						Random random = new Random();
						int i13 = 32808 + random.nextInt(1);
						int k19 = 32737 + random.nextInt(1);
						L1Teleport.teleport(pc, i13, k19, (short) 30, 5, true);
						return;
					} else {
						pc.sendPackets(new S_PacketBox(84, "\\fQ용의 전령: \\f3[입장불가]\\f2[용의 계곡 던전] 입장 가능 레벨은 60레벨부터 입니다."));
						return;
					}

				}
				/** 클라우디아 훈련교관 테온 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 202066) {// 테온
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(99115, 5)) {
						pc.sendPackets(new S_ChatPacket(pc, "테온(훈련교관): '클라우디아 이동 주문서'을 소지하고 계십니다."));
						htmlid = "";
					} else {
						pc.getInventory().storeItem(99115, 5);
						pc.sendPackets(new S_ChatPacket(pc, "테온(훈련교관): '클라우디아 이동 주문서'을 드렸습니다."));
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fH테온(훈련교관): 이제 아덴월드 지배자를 처치하시오.."));
					}
				}
				/** 클라우디아 라라 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 202057) {// 라라
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(40308, 0)) {
						pc.getInventory().consumeItem(40308, 0);
						// L1Teleport.teleport(pc, 32646, 32865, (short) 7783,
						// 5, true);
					} else {
					}
					if (pc.getLevel() >= 59) {// 해당레벨이상일경우
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"\\fH라라: 클라우디아는 60레벨까지만 입장이 가능해요!!"));
						htmlid = "tel_lala2";
						// return htmlid;
					}
					if (pc.getLevel() >= 1 & pc.getLevel() <= 59) {
						pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"\\fH라라: 클라우디아에서 [60]레벨까지 성장하세요! "));
						htmlid = "tel_lala1";
						// 레벨52기준보상경험치(pc, 1);
						L1Teleport.teleport(pc, 32646, 32865, (short) 7783, 5, true);
					}
				}

				/** 클라우디아 훈련 군터 **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 202065) {// 군터
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(447011, 1)) {
						// pc.getInventory().consumeItem(447011, 0);
					} else {
						// }
						if (pc.getLevel() <= 4) {// 해당레벨이하일경우
							pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\fH군터(스승): 자네는..아직 레벨[5]도 못만들었나?!"));
							pc.sendPackets(new S_SystemMessage("\\aA군터: 레벨[\\aG5\\aA]를 만들고 오게나.."));
							htmlid = "archgunter2";
							// return htmlid;
						}
						if (pc.getLevel() >= 5) {// 해당레벨이상일경우
							if (pc.getLevel() < 51){
								pc.addExp((ExpTable.getExpByLevel(15)-1) - pc.getExp()+((ExpTable.getExpByLevel(15)-1)/100));
							} else if (pc.getLevel() >= 51 && pc.getLevel() < Config.EXP_GIVE){
								pc.addExp((ExpTable.getExpByLevel(pc.getLevel()+1)-1) - pc.getExp()+100);
								pc.setCurrentHp(pc.getMaxHp());
								pc.setCurrentMp(pc.getMaxMp());
							}
							pc.sendPackets(new S_SystemMessage("\\aA군터: 이제 [\\aG훈련교관 테온\\aA]을 만나거라.."));
							L1Teleport.teleport(pc, 32646, 32865, (short) 7783, 5, true);
						}
					}
				}
				/** 붉은기사단 훈련소 카시오페아**/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 202067) {// 군터
				if (pc.getInventory().checkItem(40308)) {
					L1Teleport.teleport(pc, 33430, 32799, (short) 4,5, true);
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70845) { // 아라스
				if (pc.getInventory().checkItem(40665)) {
					htmlid = "aras8";
				} else if (pc.getInventory().checkItem(40637)) {
					htmlid = "aras1";
					if (s.equalsIgnoreCase("A")) {
						if (pc.getInventory().checkItem(40664)) {
							htmlid = "aras6";
							if (pc.getInventory().checkItem(40679)
									|| pc.getInventory().checkItem(40680)
									|| pc.getInventory().checkItem(40681)
									|| pc.getInventory().checkItem(40682)
									|| pc.getInventory().checkItem(40683)
									|| pc.getInventory().checkItem(40684)
									|| pc.getInventory().checkItem(40693)
									|| pc.getInventory().checkItem(40694)
									|| pc.getInventory().checkItem(40695)
									|| pc.getInventory().checkItem(40697)
									|| pc.getInventory().checkItem(40698)
									|| pc.getInventory().checkItem(40699)) {
								htmlid = "aras3";
							} else {
								htmlid = "aras6";
							}
						} else {
							L1NpcInstance npc = (L1NpcInstance) obj;
							L1ItemInstance item = pc.getInventory().storeItem(40664, 1);
							String npcName = npc.getNpcTemplate().get_name();
							String itemName = item.getItem().getName();
							pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
							htmlid = "aras6";
						}
					} else if (s.equalsIgnoreCase("B")) {
						if (pc.getInventory().checkItem(40664)) {
							pc.getInventory().consumeItem(40664, 1);
							L1NpcInstance npc = (L1NpcInstance) obj;
							L1ItemInstance item = pc.getInventory().storeItem(40665, 1);
							String npcName = npc.getNpcTemplate().get_name();
							String itemName = item.getItem().getName();
							pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
							htmlid = "aras13";
						} else {
							htmlid = "aras14";
							L1NpcInstance npc = (L1NpcInstance) obj;
							L1ItemInstance item = pc.getInventory().storeItem(40665, 1);
							String npcName = npc.getNpcTemplate().get_name();
							String itemName = item.getItem().getName();
							pc.sendPackets(new S_ServerMessage(143, npcName, itemName));
						}
					} else {
						if (s.equalsIgnoreCase("7")) {
							if (pc.getInventory().checkItem(40693)
									&& pc.getInventory().checkItem(40694)
									&& pc.getInventory().checkItem(40695)
									&& pc.getInventory().checkItem(40697)
									&& pc.getInventory().checkItem(40698)
									&& pc.getInventory().checkItem(40699)) {
								htmlid = "aras10";
							} else {
								htmlid = "aras9";
							}
						}
					}
				} else {
					htmlid = "aras7";
				}
				// 도망친 맘보토끼

			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70701) { // npc 번호
				구호증서(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 778812) { // npc 번호
				if (s.equalsIgnoreCase("0")) {
					if (!pc.getInventory().checkItem(20343, 1)) {
						pc.getInventory().storeItem(20343, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "맘보토끼 변신 모자"), true);
						// pc.sendPackets(new
						// S_SystemMessage("맘보토끼 변신 모자를 얻었습니다."));
						htmlid = "friendmambo2";
					} else { // 재료가 부족한 경우
						htmlid = "friendmambo3";
					}
				}
				if (s.equalsIgnoreCase("1")) {
					if (pc.getInventory().checkItem(41159, 10)) { // 신비한 날개 깃털
						if (pc.getLevel() >= 52) {
							pc.getInventory().consumeItem(41159, 10);
							int[] allBuffSkill = { PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR, BLESS_WEAPON };
							pc.setBuffnoch(1);
							L1SkillUse l1skilluse = new L1SkillUse();
							for (int i = 0; i < allBuffSkill.length; i++) {
								l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
							}
							htmlid = "";
						} else {
							pc.sendPackets(new S_SystemMessage("\\fU52레벨 이상부터 사용 가능합니다."));
						}
					} else {
						pc.sendPackets(new S_SystemMessage("\\fU신비한날개깃털[10]개가 부족합니다."));
					}
				}
				/*****************************
				 * 추가 부분
				 *****************************/
				// 이벤트 축하사절 단장
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4203000
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 8596771
					|| (((L1NpcInstance) obj).getNpcId() >= 100376 && ((L1NpcInstance) obj).getNpcId() <= 100385)
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100399
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 170017
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100434) { // //젠도르 npc 번호
				if (pc._젠도르빈줌갯수 > 0) {
					int itemid = 0;
					int aden = 50;
					int emptyScroll = 40090;
					if (s.equalsIgnoreCase("A")) {
						itemid = 40860;
					} else if (s.equalsIgnoreCase("B")) {
						itemid = 40861;
					} else if (s.equalsIgnoreCase("C")) {
						itemid = 40862;
					} else if (s.equalsIgnoreCase("D")) {
						itemid = 40866;
					} else if (s.equalsIgnoreCase("E")) {
						itemid = 40859;
					} else if (s.equalsIgnoreCase("F")) { // 디크리즈
						aden = 100;
						itemid = 40872;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("G")) { // 디텍
						aden = 100;
						itemid = 40871;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("H")) { // 인첸트웨폰
						aden = 100;
						itemid = 40870;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("I")) { // 큐어 포이즌
						aden = 100;
						itemid = 40867;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("J")) { // 파이어 애로우
						aden = 100;
						itemid = 40873;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("K")) { // 라이트닝
						aden = 100;
						itemid = 40875;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("L")) { // 블레스드 아머
						aden = 100;
						itemid = 40879;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("M")) { // 익스트라 힐
						aden = 100;
						itemid = 40877;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("N")) { // 프로즌 클라우드
						aden = 100;
						itemid = 40880;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("O")) { // 턴언데드
						aden = 100;
						itemid = 40876;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("P")) { // 메디테이션
						aden = 200;
						itemid = 40890;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("Q")) { // 파이어볼
						aden = 200;
						itemid = 40883;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("R")) { // 인첸트 덱스터리
						aden = 200;
						itemid = 40884;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("S")) { // 카운터 매직
						aden = 200;
						itemid = 40889;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("T")) { // 슬로우
						aden = 200;
						itemid = 40887;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("U")) { // 그힐
						aden = 200;
						itemid = 40893;
						emptyScroll = 40094;
					} else if (s.equalsIgnoreCase("V")) { // 리무브 커스
						aden = 200;
						itemid = 40895;
						emptyScroll = 40094;
					} else if (s.equalsIgnoreCase("W")) { // 마나드레인
						aden = 200;
						itemid = 40897;
						emptyScroll = 40094;
					} else if (s.equalsIgnoreCase("X")) { // 콘오브콜드
						aden = 200;
						itemid = 40896;
						emptyScroll = 40094;
					} else if (s.equalsIgnoreCase("Y")) { // 콜라이트닝
						aden = 200;
						itemid = 40892;
						emptyScroll = 40094;
					}

					if (!pc.getInventory().checkItem(40308, aden * pc._젠도르빈줌갯수)) // 아덴 체크
						htmlid = "bs_m6";
					else if (!pc.getInventory().checkItem(emptyScroll, pc._젠도르빈줌갯수)) // 빈줌 체크
						htmlid = "bs_m2";
					else if ((itemid == 40887 || itemid == 40889) && !pc.getInventory().consumeItem(40318, pc._젠도르빈줌갯수)) // 마돌 체크
						htmlid = "bs_m2";
					else if (itemid != 0) {
						pc.getInventory().consumeItem(40308, aden * pc._젠도르빈줌갯수);
						pc.getInventory().consumeItem(emptyScroll, pc._젠도르빈줌갯수);
						L1ItemInstance item = pc.getInventory().storeItem(itemid, pc._젠도르빈줌갯수);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), item.getName() + " (" + pc._젠도르빈줌갯수 + ")"), true);
						// true);
						htmlid = "bs_m1";
					}
				} else {
					if (s.equalsIgnoreCase("a")) { // 근거리 버프
						/*if (!pc.isPinkName()) {*/
							if (pc.getInventory().checkItem(60061) || pc.getInventory().checkItem(40308, 1000)) { // 신비한날개 깃털
								if (pc.getClanid() >= 0) {
									if (!pc.getInventory().checkItem(60061))
										pc.getInventory().consumeItem(40308, 1000);
									int[] allBuffSkill = { L1SkillId.REMOVE_CURSE, L1SkillId.PHYSICAL_ENCHANT_STR, L1SkillId.PHYSICAL_ENCHANT_DEX, L1SkillId.BLESS_WEAPON };
									pc.setBuffnoch(1); // 추가적으로 버프는 미작동
									L1SkillUse l1skilluse = new L1SkillUse();
									for (int i = 0; i < allBuffSkill.length; i++) {
										l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
									}
									htmlid = "";
								} else {
									pc.sendPackets(new S_SystemMessage("혈맹을 가입하셔야 버프를 받을수 있습니다."));
								}
							} else {
								pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."));
							}
						/*} else {
							htmlid = "bs_01a";
						}*/

					} else if (s.equalsIgnoreCase("1")) {// 빈줌 1개
						htmlid = "bs_m4";
						htmldata = new String[] { "50", "100", "100", "200", "200", "1" };
						pc._젠도르빈줌갯수 = 1;
					} else if (s.equalsIgnoreCase("2")) {// 빈줌 5개
						htmlid = "bs_m4";
						htmldata = new String[] { "250", "500", "500", "1000", "1000", "5" };
						pc._젠도르빈줌갯수 = 5;
					} else if (s.equalsIgnoreCase("3")) {// 빈줌 10개
						htmlid = "bs_m4";
						htmldata = new String[] { "500", "1000", "1000", "2000", "2000", "10" };
						pc._젠도르빈줌갯수 = 10;
					} else if (s.equalsIgnoreCase("4")) {// 빈줌 100개
						htmlid = "bs_m4";
						htmldata = new String[] { "5000", "10000", "10000", "20000", "20000", "100" };
						pc._젠도르빈줌갯수 = 100;
					} else if (s.equalsIgnoreCase("5")) {// 빈줌 500개
						htmlid = "bs_m4";
						htmldata = new String[] { "25000", "50000", "50000", "50000", "50000", "500" };
						pc._젠도르빈줌갯수 = 500;
					}
				}
				/** 성혈전용 (버프사) **/
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000065) { // npc 번호
				if (pc._젠도르빈줌갯수 > 0) {
					int itemid = 0;
					int aden = 50;
					int emptyScroll = 40090;
					if (s.equalsIgnoreCase("A")) {
						itemid = 40860;
					} else if (s.equalsIgnoreCase("B")) {
						itemid = 40861;
					} else if (s.equalsIgnoreCase("C")) {
						itemid = 40862;
					} else if (s.equalsIgnoreCase("D")) {
						itemid = 40866;
					} else if (s.equalsIgnoreCase("E")) {
						itemid = 40859;
					} else if (s.equalsIgnoreCase("F")) { // 디크리즈
						aden = 100;
						itemid = 40872;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("G")) { // 디텍
						aden = 100;
						itemid = 40871;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("H")) { // 인첸트웨폰
						aden = 100;
						itemid = 40870;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("I")) { // 큐어 포이즌
						aden = 100;
						itemid = 40867;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("J")) { // 파이어 애로우
						aden = 100;
						itemid = 40873;
						emptyScroll = 40091;
					} else if (s.equalsIgnoreCase("K")) { // 라이트닝
						aden = 100;
						itemid = 40875;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("L")) { // 블레스드 아머
						aden = 100;
						itemid = 40879;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("M")) { // 익스트라 힐
						aden = 100;
						itemid = 40877;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("N")) { // 프로즌 클라우드
						aden = 100;
						itemid = 40880;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("O")) { // 턴언데드
						aden = 100;
						itemid = 40876;
						emptyScroll = 40092;
					} else if (s.equalsIgnoreCase("P")) { // 메디테이션
						aden = 200;
						itemid = 40890;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("Q")) { // 파이어볼
						aden = 200;
						itemid = 40883;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("R")) { // 인첸트 덱스터리
						aden = 200;
						itemid = 40884;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("S")) { // 카운터 매직
						aden = 200;
						itemid = 40889;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("T")) { // 슬로우
						aden = 200;
						itemid = 40887;
						emptyScroll = 40093;
					} else if (s.equalsIgnoreCase("U")) { // 그힐
						aden = 200;
						itemid = 40893;
						emptyScroll = 40094;
					} else if (s.equalsIgnoreCase("V")) { // 리무브 커스
						aden = 200;
						itemid = 40895;
						emptyScroll = 40094;
					} else if (s.equalsIgnoreCase("W")) { // 마나드레인
						aden = 200;
						itemid = 40897;
						emptyScroll = 40094;
					} else if (s.equalsIgnoreCase("X")) { // 콘오브콜드
						aden = 200;
						itemid = 40896;
						emptyScroll = 40094;
					} else if (s.equalsIgnoreCase("Y")) { // 콜라이트닝
						aden = 200;
						itemid = 40892;
						emptyScroll = 40094;
					}

					if (!pc.getInventory().checkItem(40308, aden * pc._젠도르빈줌갯수)) // 아덴 체크
						htmlid = "bs_m6";
					else if (!pc.getInventory().checkItem(emptyScroll, pc._젠도르빈줌갯수)) // 빈줌 체크
						htmlid = "bs_m2";
					else if ((itemid == 40887 || itemid == 40889) && !pc.getInventory().consumeItem(40318, pc._젠도르빈줌갯수)) // 마돌 체크
						htmlid = "bs_m2";
					else if (itemid != 0) {
						pc.getInventory().consumeItem(40308, aden * pc._젠도르빈줌갯수);
						pc.getInventory().consumeItem(emptyScroll, pc._젠도르빈줌갯수);
						L1ItemInstance item = pc.getInventory().storeItem(itemid, pc._젠도르빈줌갯수);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), item.getName() + " (" + pc._젠도르빈줌갯수 + ")"), true);
						htmlid = "bs_m1";
					}
				} else {
					if (s.equalsIgnoreCase("a")) { // 근거리 버프
							if (pc.getInventory().checkItem(40308, 1000)) { // 신비한 날개 깃털
								if (pc.getLevel() >= 52) {
									pc.getInventory().consumeItem(40308, 1000);
									int[] allBuffSkill = { L1SkillId.IRON_SKIN };
									pc.setBuffnoch(1);
									L1SkillUse l1skilluse = new L1SkillUse();
									for (int i = 0; i < allBuffSkill.length; i++) {
										l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
									}
									pc.getSkillEffectTimerSet().removeSkillEffect(7893);
									pc.getSkillEffectTimerSet().removeSkillEffect(7894);
									pc.getSkillEffectTimerSet().removeSkillEffect(7895);
									pc.addDmgup(3);
									pc.addHitup(3);
									pc.getAbility().addSp(3);
									pc.sendPackets(new S_SPMR(pc));
									pc.sendPackets(new S_SkillSound(pc.getId(), 7895));
									Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 7895));
									pc.getSkillEffectTimerSet().setSkillEffect(7895, 1800 * 1000);
									htmlid = "";
									htmlid = "";
								} else {
									pc.sendPackets(new S_SystemMessage("52레벨 이상부터 사용 가능합니다."));
								}
							} else {
								pc.sendPackets(new S_SystemMessage("1000 아데나가 필요합니다."));
							}

					} else if (s.equalsIgnoreCase("1")) {// 빈줌 1개
						htmlid = "bs_m4";
						htmldata = new String[] { "50", "100", "100", "200", "200", "1" };
						pc._젠도르빈줌갯수 = 1;
					} else if (s.equalsIgnoreCase("2")) {// 빈줌 5개
						htmlid = "bs_m4";
						htmldata = new String[] { "250", "500", "500", "1000", "1000", "5" };
						pc._젠도르빈줌갯수 = 5;
					} else if (s.equalsIgnoreCase("3")) {// 빈줌 10개
						htmlid = "bs_m4";
						htmldata = new String[] { "500", "1000", "1000", "2000", "2000", "10" };
						pc._젠도르빈줌갯수 = 10;
					} else if (s.equalsIgnoreCase("4")) {// 빈줌 100개
						htmlid = "bs_m4";
						htmldata = new String[] { "5000", "10000", "10000", "20000", "20000", "100" };
						pc._젠도르빈줌갯수 = 100;
					} else if (s.equalsIgnoreCase("5")) {// 빈줌 500개
						htmlid = "bs_m4";
						htmldata = new String[] { "25000", "50000", "50000", "50000", "50000", "500" };
						pc._젠도르빈줌갯수 = 500;
					}
				}

				// 신녀 유리스 (속죄)
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4213001) {
				int count = 0;
				if (s.equalsIgnoreCase("0")) {
					count = 1;
				} else if (s.equalsIgnoreCase("1")) {
					count = 3;
				} else if (s.equalsIgnoreCase("2")) {
					count = 5;
				} else if (s.equalsIgnoreCase("3")) {
					count = 10;
				}

				if (count > 0 && pc.getInventory().checkItem(L1ItemId.REDEMPTION_BIBLE, count)) {
					pc.getInventory().consumeItem(L1ItemId.REDEMPTION_BIBLE, count);
					pc.addLawful(3000 * count);
					pc.sendPackets(new S_Lawful(pc.getId(), pc.getLawful()));
					pc.sendPackets(new S_SkillSound(pc.getId(), 9009));
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9009));
					htmlid = "yuris2";
				} else {
					htmlid = "yuris3";
				}
				// 상아탑조수 라미아스
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4309000) {
				int polyId = 0;
				if (s.equalsIgnoreCase("1")) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
						if (pc.getLevel() <= 52) {
							polyId = 7036;
						} else if (pc.getLevel() <= 55) {
							polyId = 7037;
						} else if (pc.getLevel() > 55) {
							polyId = 7038;
						}
						L1PolyMorph.doPoly(pc, polyId, 3600, L1PolyMorph.MORPH_BY_NPC);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
						htmlid = "event_boss9";
					} else {
						htmlid = "event_boss8";
					}
				}
				// 상아탑조수 엔디아스
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4309002) {
				int polyId = 0;
				if (s.equalsIgnoreCase("1")) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
						if (pc.getLevel() <= 52) {
							polyId = 7039;
						} else if (pc.getLevel() <= 55) {
							polyId = 7040;
						} else if (pc.getLevel() > 55) {
							polyId = 7041;
						}
						L1PolyMorph.doPoly(pc, polyId, 3600, L1PolyMorph.MORPH_BY_NPC);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
						htmlid = "event_boss10";
					} else {
						htmlid = "event_boss8";
					}
				}
				// 상아탑조수 이데아
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4309001) {
				int polyId = 0;
				if (s.equalsIgnoreCase("1")) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000)) {
						if (pc.getLevel() <= 52) {
							polyId = 7042;
						} else if (pc.getLevel() <= 55) {
							polyId = 7043;
						} else if (pc.getLevel() > 55) {
							polyId = 7044;
						}
						L1PolyMorph.doPoly(pc, polyId, 3600, L1PolyMorph.MORPH_BY_NPC);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
						htmlid = "event_boss11";
					} else {
						htmlid = "event_boss8";
					}
				}
				// 아우라키아의 전령
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4218005) {
				if (s.equalsIgnoreCase("1")) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000) && !pc.getInventory().checkItem(423014)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
						pc.getInventory().storeItem(423014, 1);
						htmlid = "evdcs3";
					} else if (pc.getInventory().checkItem(423014)) {
						htmlid = "evdcs4";
					} else {
						htmlid = "evdcs5";
					}
				}
				if (s.equalsIgnoreCase("2")) {
					if (pc.getInventory().checkItem(L1ItemId.MIRACLE_FRAGMENT, 6)) {
						pc.getInventory().consumeItem(L1ItemId.MIRACLE_FRAGMENT, 6);
						pc.getInventory() .storeItem(L1ItemId.AURAKIA_PRESENT, 1);
						htmlid = "evdcs6";
					} else if (pc.getInventory().checkItem(L1ItemId.DISTURBING_PROOF, 1)) {
						pc.getInventory().consumeItem(L1ItemId.DISTURBING_PROOF, 1);
						pc.getInventory() .storeItem(L1ItemId.AURAKIA_PRESENT, 1);
						htmlid = "evdcs6";
					} else {
						htmlid = "evdcs7";
					}
				}
				// 실렌의 전령
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4219005) {
				if (s.equalsIgnoreCase("1")) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 1000) && !pc.getInventory().checkItem(423015)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 1000);
						pc.getInventory().storeItem(423015, 1);
						htmlid = "evics3";
					} else if (pc.getInventory().checkItem(423015)) {
						htmlid = "evics4";
					} else {
						htmlid = "evics5";
					}
				}
				if (s.equalsIgnoreCase("2")) {
					if (pc.getInventory().checkItem(L1ItemId.SHINY_LEAF, 2)) {
						pc.getInventory().consumeItem(L1ItemId.SHINY_LEAF, 2);
						pc.getInventory().storeItem(L1ItemId.SILEN_PRESENT, 1);
						htmlid = "evics6";
					} else if (pc.getInventory().checkItem(L1ItemId.DISTURBING_PROOF, 1)) {
						pc.getInventory().consumeItem(L1ItemId.DISTURBING_PROOF, 1);
						pc.getInventory().storeItem(L1ItemId.SILEN_PRESENT, 1);
						htmlid = "evics6";
					} else {
						htmlid = "evics7";
					}
				}
				// 미니 게임 코마
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4206003) {
				htmlid = "";// coma(pc, s, objid); 리뉴얼로 인해 삭제

				/*****************************
				 * 시간의 균열
				 ********************************/
				// 테베 문지기
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200005) {
				if (s.equalsIgnoreCase("e")) {
					for (L1PcInstance gm : CrockSystem.toArray균열()) {
						if (gm.getMapId() != 782) {
							CrockSystem.getInstance().del(gm);
						}
					}
					if (!CrockSystem.getInstance().isBossTime()) {// 보스 타임이 아니면
						htmlid = "tebegate2";
					} else if (!pc.getInventory().checkItem(
							L1ItemId.TEBEOSIRIS_KEY, 1)) {// 키없으면
						htmlid = "tebegate3";
					} else if (CrockSystem.getInstance().in_count >= 20) {// CrockSystem.getInstance().size() >= 20){// 인원이 찼다면
						htmlid = "tebegate4";
					} else {
						CrockSystem.getInstance().in_count++;
						pc.getInventory().consumeItem(L1ItemId.TEBEOSIRIS_KEY, 1);
						L1Teleport.teleport(pc, 32735, 32831, (short) 782, 5, true);
						GeneralThreadPool.getInstance().schedule( new CrockSystemAdd(pc), 280);
						htmlid = "";
					}
				}
				// 티칼 문지기
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200006) {
				if (s.equalsIgnoreCase("e")) {
					for (L1PcInstance gm : CrockSystem.toArray균열()) {
						if (gm.getMapId() != 784) {
							CrockSystem.getInstance().del(gm);
						}
					}
					if (CrockSystem.getInstance().isCrockIng()) {// 연장상태면
						htmlid = "tikalgate5";
					} else if (!CrockSystem.getInstance().isBossTime()) {// 보스 타임이 아니면
						htmlid = "tikalgate2";
					} else if (!pc.getInventory().checkItem(L1ItemId.TIKAL_KEY,
							1)) {// 키없으면
						htmlid = "tikalgate3";
					} else if (CrockSystem.getInstance().in_count >= 20) {// 인원이 찼다면
						htmlid = "tikalgate4";
					} else {
						CrockSystem.getInstance().in_count++;
						pc.getInventory().consumeItem(L1ItemId.TIKAL_KEY, 1);
						L1Teleport.teleport(pc, 32731, 32863, (short) 784, 5, true);
						GeneralThreadPool.getInstance().schedule(new CrockSystemAdd(pc), 280);
						htmlid = "";
						// new L1SkillUse().handleCommands(pc,
						// L1SkillId.STATUS_TIKAL_BOSSJOIN, pc.getId(),
						// pc.getX(), pc.getY(), null, 0,
						// L1SkillUse.TYPE_SPELLSC);
					}
				}
				// 조우의 돌골렘
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212000) { // npcid
				// 마력의 단검
				if (s.equalsIgnoreCase("A")) {
					if (pc.getInventory().MakeCheckEnchant(5, 7)
							&& pc.getInventory().MakeCheckEnchant(6, 7)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().MakeDeleteEnchant(5, 7);
						pc.getInventory().MakeDeleteEnchant(6, 7);
						pc.getInventory().consumeItem(41246, 30000);
						pc.getInventory().storeItem(412002, 1);
						htmlid = "joegolem9";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 광풍의 도끼
				if (s.equalsIgnoreCase("B")) {
					if (pc.getInventory().MakeCheckEnchant(145, 7)
							&& pc.getInventory().MakeCheckEnchant(148, 7)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().MakeDeleteEnchant(145, 7);
						pc.getInventory().MakeDeleteEnchant(148, 7);
						pc.getInventory().consumeItem(41246, 30000);
						pc.getInventory().storeItem(412005, 1);
						htmlid = "joegolem10";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 파멸의 대검
				if (s.equalsIgnoreCase("C")) {
					if (pc.getInventory().MakeCheckEnchant(52, 7)
							&& pc.getInventory().MakeCheckEnchant(64, 7)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().MakeDeleteEnchant(52, 7);
						pc.getInventory().MakeDeleteEnchant(64, 7);
						pc.getInventory().consumeItem(41246, 30000);
						pc.getInventory().storeItem(412001, 1);
						htmlid = "joegolem11";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 아크메이지의 지팡이
				if (s.equalsIgnoreCase("D")) {
					if (pc.getInventory().MakeCheckEnchant(125, 7)
							&& pc.getInventory().MakeCheckEnchant(129, 7)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().MakeDeleteEnchant(125, 7);
						pc.getInventory().MakeDeleteEnchant(129, 7);
						pc.getInventory().consumeItem(41246, 30000);
						pc.getInventory().storeItem(412003, 1);
						htmlid = "joegolem12";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 혹한의 창
				if (s.equalsIgnoreCase("E")) {
					if (pc.getInventory().MakeCheckEnchant(99, 7)
							&& pc.getInventory().MakeCheckEnchant(104, 7)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().MakeDeleteEnchant(99, 7);
						pc.getInventory().MakeDeleteEnchant(104, 7);
						pc.getInventory().consumeItem(41246, 30000);
						pc.getInventory().storeItem(412004, 1);
						htmlid = "joegolem13";
					} else {
						htmlid = "joegolem15";
					}
				}
				// 뇌신검
				if (s.equalsIgnoreCase("F")) {
					if (pc.getInventory().MakeCheckEnchant(32, 7)
							&& pc.getInventory().MakeCheckEnchant(42, 7)
							&& pc.getInventory().checkItem(41246, 30000)) {
						pc.getInventory().MakeDeleteEnchant(32, 7);
						pc.getInventory().MakeDeleteEnchant(42, 7);
						pc.getInventory().consumeItem(41246, 30000);
						pc.getInventory().storeItem(412000, 1);
						htmlid = "joegolem14";
					} else {
						htmlid = "joegolem15";
					}
				}
				/**************************
				 * 안타라스 리뉴얼 New System
				 *****************************/
				// 경험치 지급단
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4200018) {
				if (s.equals("0")){
					if (pc.getLevel() < 51){
						pc.addExp((ExpTable.getExpByLevel(51)-1) - pc.getExp()+((ExpTable.getExpByLevel(51)-1)/100));
					} else if (pc.getLevel() >= 51 && pc.getLevel() < Config.EXP_GIVE){
						pc.addExp((ExpTable.getExpByLevel(pc.getLevel()+1)-1) - pc.getExp()+100);
						pc.setCurrentHp(pc.getMaxHp());
						pc.setCurrentMp(pc.getMaxMp());
					}
					if (ExpTable.getLevelByExp(pc.getExp()) >= Config.EXP_GIVE){
						htmlid = "expgive3";
					} else {
					//	htmlid = "expgive1";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100395) {
				if (s.equalsIgnoreCase("0")) {
					if (pc.getLevel() < 75) {
						pc.setExp(ExpTable.getExpByLevel(75));
						if (pc.getQuest().get_step(L1Quest.QUEST_55_Roon) == 1
								|| pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 1
								|| pc.getInventory().checkItem(60381)) {
						} else {
							L1ItemInstance item = pc.getInventory().storeItem(60381, 1);
							pc.sendPackets(new S_ServerMessage(143, "경험치 지급단", item.getName() + " (1)"), true);
						}
						// pc.addExp((ExpTable.getNeedExpNextLevel(pc.getLevel()
						// + 1) / 100) * 50);
					}
					if (ExpTable.getLevelByExp(pc.getExp()) >= 75) {
						htmlid = "expgive3";
					} else {
						htmlid = "expgive1";
					}
				}
				// 드루가 베일
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212001) {
				htmlid = 드루가베일(pc, s);
				// 슈에르메
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4212002) {
				L1ItemInstance item = null;
				// a(지룡),b(수룡),c(화룡),d(풍룡) 마안을 만든다 - 봉인해제 부분
				if (s.equalsIgnoreCase("a")) {
					if (pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_EARTH)) {
						htmlid = "sherme0";
					} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 100000)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_EARTH_B)) {
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_EARTH_B, 1);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100000);
						item = pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_EARTH, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
						htmlid = "";
					} else {
						htmlid = "sherme1"; // 돈이 없거나 마안이 없다.
					}
				} else if (s.equalsIgnoreCase("b")) {
					if (pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_WATER)) {
						htmlid = "sherme0";
					} else if (pc.getInventory().checkItem(L1ItemId.ADENA,100000)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_WATER_B)) {
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_WATER_B, 1);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100000);
						item = pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_WATER, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
						htmlid = "";
					} else {
						htmlid = "sherme1"; // 돈이 없거나 마안이 없다.
					}
				} else if (s.equalsIgnoreCase("c")) {
					if (pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_FIRE)) {
						htmlid = "sherme0";
					} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 100000)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_FIRE_B)) {
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_FIRE_B, 1);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100000);
						item = pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_FIRE, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
						htmlid = "";
					} else {
						htmlid = "sherme1"; // 돈이 없거나 마안이 없다.
					}
				} else if (s.equalsIgnoreCase("d")) {
					if (pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_WIND)) {
						htmlid = "sherme0";
					} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 100000)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_WIND_B)) {
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_WIND_B, 1);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100000);
						item = pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_WIND, 1);
						pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
						htmlid = "";
					} else {
						htmlid = "sherme1"; // 돈이 없거나 마안이 없다.
					}
				} else if (s.equalsIgnoreCase("e")) {
					// 탄생의 마안
					int chance = _random.nextInt(100) + 1;
					if (pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_BIRTH)) {
						htmlid = "sherme0";
					} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 200000)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_EARTH)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_WATER)) {
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_EARTH, 1);
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_WATER, 1);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 200000);
						if (chance <= 40) {
							item = pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_BIRTH, 1);
							pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
							htmlid = "";
						} else {
							htmlid = "sherme5";
						}
					} else {
						htmlid = "sherme1"; // 돈이 없거나 마안이 없다.
					}
				} else if (s.equalsIgnoreCase("f")) {
					// 형상의 마안
					int chance = _random.nextInt(100) + 1;
					if (pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_SHAPE)) {
						htmlid = "sherme0";
					} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 200000)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_BIRTH)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_WIND)) {
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_BIRTH, 1);
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_WIND, 1);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 200000);
						if (chance <= 30) {
							item = pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_SHAPE, 1);
							pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
							htmlid = "";
						} else {
							htmlid = "sherme5";
						}
					} else {
						htmlid = "sherme1"; // 돈이 없거나 마안이 없다.
					}

				} else if (s.equalsIgnoreCase("g")) {
					// 생명의 마안
					int chance = _random.nextInt(100) + 1;
					if (pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_LIFE)) {
						htmlid = "sherme0";
					} else if (pc.getInventory().checkItem(L1ItemId.ADENA, 200000)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_SHAPE)
							&& pc.getInventory().checkItem(L1ItemId.DRAGONMAAN_FIRE)) {
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_SHAPE, 1);
						pc.getInventory().consumeItem(L1ItemId.DRAGONMAAN_FIRE, 1);
						pc.getInventory().consumeItem(L1ItemId.ADENA, 200000);
						if (chance <= 25) {
							item = pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_LIFE, 1);
							pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
							htmlid = "";
						} else {
							htmlid = "sherme5";
						}
					} else {
						htmlid = "sherme1"; // 돈이 없거나 마안이 없다.
					}
				} else if (s.equalsIgnoreCase("h")) {
					if (pc.getInventory().checkItem(5000064, 32)
							&& pc.getInventory().checkItem(40308, 100000)) {
						pc.getInventory().consumeItem(5000064, 32);
						pc.getInventory().consumeItem(40308, 100000);
						pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_EARTH, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "지룡의 마안"), true);
						// pc.sendPackets(new
						// S_SystemMessage("\\fR 지룡의 마안을 얻었습니다."));
						htmlid = "";
					} else {
						htmlid = "sherme10";
					}

				} else if (s.equalsIgnoreCase("i")) {
					if (pc.getInventory().checkItem(5000066, 32)
							&& pc.getInventory().checkItem(40308, 100000)) {
						pc.getInventory().consumeItem(5000066, 32);
						pc.getInventory().consumeItem(40308, 100000);
						pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_WATER, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "수룡의 마안"), true);
						// pc.sendPackets(new
						// S_SystemMessage("\\fR 수룡의 마안을 얻었습니다."));
						htmlid = "";
					} else {
						htmlid = "sherme10";
					}
				} else if (s.equalsIgnoreCase("j")) {
					if (pc.getInventory().checkItem(5000063, 32)
							&& pc.getInventory().checkItem(40308, 100000)) {
						pc.getInventory().consumeItem(5000063, 32);
						pc.getInventory().consumeItem(40308, 100000);
						pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_FIRE, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "화룡의 마안"), true);
						// pc.sendPackets(new
						// S_SystemMessage("\\fR 화룡의 마안을 얻었습니다."));
						htmlid = "";
					} else {
						htmlid = "sherme10";
					}

				} else if (s.equalsIgnoreCase("k")) {
					if (pc.getInventory().checkItem(5000065, 32)
							&& pc.getInventory().checkItem(40308, 100000)) {
						pc.getInventory().consumeItem(5000065, 32);
						pc.getInventory().consumeItem(40308, 100000);
						pc.getInventory().storeItem(L1ItemId.DRAGONMAAN_WIND, 1);
						pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "풍룡의 마안"), true);
						// pc.sendPackets(new
						// S_SystemMessage("\\fR 풍룡의 마안을 얻었습니다."));
						htmlid = "";
					} else {
						htmlid = "sherme10";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 757576) {
				마에노브불꽃(s, pc, obj);
				htmlid = "";
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70758) {
				htmlid = 스빈(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 90020) {
				htmlid = 첩보원(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70751) {
				htmlid = 얼음여왕성(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 70000) {
				htmlid = 마빈(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100831) {
				htmlid = 햄(s, pc);

			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 500002) {
				htmlid = maetnob(pc, s);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 45000) {// 초보자 도우미
				/*
				 * if(s.equalsIgnoreCase("A")){ int list[] = {20126, 20028,
				 * 20173, 20206, 21098}; boolean ck = false; for(int i : list){
				 * if(!pc.getInventory().checkItem(i)){ ck = true;
				 * pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance)
				 * obj).getName(), pc.getInventory().storeItem(i, 1).getName()),
				 * true); //pc.sendPackets(new
				 * S_SystemMessage(pc.getInventory().storeItem(i, 1).getName()+
				 * " 을 획득 하였습니다."), true); } } if(ck){ htmlid = "tutorrw5";
				 * }else{ htmlid = "tutorrw6"; } }
				 */
				/*
				 * if (pc.getLevel() < 13){ pc.sendPackets(new
				 * S_SkillHaste(pc.getId(), 1, 1800));
				 * Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(),
				 * 1, 0)); pc.sendPackets(new S_SkillSound(pc.getId(), 755));
				 * Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
				 * 755)); pc.getMoveState().setMoveSpeed(1);
				 * pc.getSkillEffectTimerSet
				 * ().setSkillEffect(L1SkillId.STATUS_HASTE, 1800 * 1000);
				 * pc.setCurrentHp(pc.getMaxHp());
				 * pc.setCurrentMp(pc.getMaxMp()); pc.sendPackets(new
				 * S_SkillSound(pc.getId(), 830)); pc.sendPackets(new
				 * S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
				 * pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(),
				 * pc.getMaxMp())); } htmlid = "tutorm4"; if
				 * (s.equalsIgnoreCase("Q")){ htmlid = "";
				 * L1Teleport.teleport(pc, 32605, 32837, (short) 2005, 4, true);
				 * } else if (s.equalsIgnoreCase("R")){ htmlid = "";
				 * L1Teleport.teleport(pc, 32733, 32902, (short) 2005, 4, true);
				 * } else if (s.equalsIgnoreCase("S")){ htmlid = "";
				 * L1Teleport.teleport(pc, 32802, 32803, (short) 2005, 4, true);
				 * } else if (s.equalsIgnoreCase("T")){ htmlid = "";
				 * L1Teleport.teleport(pc, 32642, 32763, (short) 2005, 4, true);
				 * } else if (s.equalsIgnoreCase("H")){ htmlid = "";
				 * L1Teleport.teleport(pc, 32572, 32945, (short) 0, 4, true); }
				 * else if (s.equalsIgnoreCase("K")){ htmlid = "";
				 * L1Teleport.teleport(pc, 32586, 32957, (short) 0, 4, true); }
				 * else if (s.equalsIgnoreCase("J")){ htmlid = "";
				 * L1Teleport.teleport(pc, 32872, 32871, (short) 86, 4, true); }
				 */
				// 수상한 텔레포터
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4220013
					|| ((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100287) {
				int x = 0, y = 0, mapid = 0, count = 0;
				if (s.equalsIgnoreCase("b")) {
					L1Teleport.teleport(pc, 33442, 32800, (short) 4, 5, true);
					htmlid = "";
				} else if (s.equalsIgnoreCase("c")) {
					x = 34058;
					y = 32280;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("d")) {
					x = 33708;
					y = 32501;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("e")) {
					x = 33612;
					y = 33254;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("f")) {
					x = 33052;
					y = 32780;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("g")) {
					x = 32608;
					y = 32734;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("h")) {
					x = 33073;
					y = 33391;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("i")) {
					x = 32644;
					y = 33207;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("j")) {
					x = 32741;
					y = 32450;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("k")) {
					x = 32583;
					y = 32924;
					mapid = 0;
					count = 250;
				} else if (s.equalsIgnoreCase("l")) {
					x = 33951;
					y = 33363;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("m")) {
					x = 32602;
					y = 32721;
					mapid = 4;
					count = 100;
				} else if (s.equalsIgnoreCase("N")) {
					x = 32797;
					y = 32928;
					mapid = 800;
					count = 100;// 시장
				} else if (s.equalsIgnoreCase("V")) {// 32595 33163
					x = 32595;
					y = 33163;
					mapid = 4;
					count = 1002;// 데포로쥬앞
					/*
					 * } else if (s.equalsIgnoreCase("o")) { x = 32702; y =
					 * 32842; mapid = 350; count = 2; } else if
					 * (s.equalsIgnoreCase("p")) { x = 32733; y = 32792; mapid =
					 * 360; count = 2; } else if (s.equalsIgnoreCase("q")) { x =
					 * 32733; y = 32792; mapid = 370; count = 2;
					 */
				}
				if (x != 0 && y != 0 && count != 0) {
					if (pc.getInventory().checkItem(40308, count)) {
						pc.getInventory().consumeItem(40308, count);
						L1Teleport.teleport(pc, x, y, (short) mapid, 5, true);
						htmlid = "";
					} else {
						htmlid = "pctel2";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 4220012) {
				int html;
				if (s.equalsIgnoreCase("0")) {
					if (itemCkStore(pc, 41159, 437023, 45)) {
						pc.sendPackets(new S_ServerMessage(403, "$8538"));
					} else {
						htmlid = "suschef5";
					}
				} else if (s.equalsIgnoreCase("1")) {
					if (checkmemo(pc)) {
						htmlid = "";
					} else if (itemCkStore(pc, 437027, 437031)) {
                            htmlid = "fortunea0";
                          } else if (itemCkStore(pc, 437028, 437032)) {
                            html = _random.nextInt(10);
                            htmlid = new StringBuilder().append("fortuneb").append(html).toString();
                          } else if (itemCkStore(pc, 437029, 437033)) {
                            html = _random.nextInt(30);
                            htmlid = new StringBuilder().append("fortunec").append(html).toString();
                          } else if (itemCkStore(pc, 437030, 437034)) {
                            htmlid = "forthned0";
                          } else {
                            htmlid = "suschef4";
                          }
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 7000044) {
				if (s.equalsIgnoreCase("0")) {
					if (!pc.getInventory().checkItem(40308, 5000)) {
						htmlid = "rabbita4";
					} else {
						pc.getInventory().consumeItem(40308, 5000);
						pc.getInventory().storeItem(20344, 1);
						htmlid = "rabbita5";
					}
				}
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100020) {
				if (s.equalsIgnoreCase("0"))
					htmlid = 메린(pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100021) {
				if (s.equalsIgnoreCase("0"))
					htmlid = 킬톤(pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100022) {
				htmlid = 모포(s, pc);
			} else if (((L1NpcInstance) obj).getNpcTemplate().get_npcId() == 100029) {
				htmlid = 조우불골렘(s, pc);
			}

			if (htmlid != null && htmlid.equalsIgnoreCase("colos2")) {
				htmldata = makeUbInfoStrings(((L1NpcInstance) obj).getNpcTemplate().get_npcId());
			}

			if (createitem != null) {
				boolean isCreate = true;
				for (int j = 0; j < materials.length; j++) {
					if (!pc.getInventory().checkItemNotEquipped(materials[j], counts[j])) {
						L1Item temp = ItemTable.getInstance().getTemplate(materials[j]);
						pc.sendPackets(new S_ServerMessage(337, temp.getName()));
						isCreate = false;
					}
				}

				if (isCreate) {
					int create_count = 0;
					int create_weight = 0;
					L1Item temp = null;
					for (int k = 0; k < createitem.length; k++) {
						temp = ItemTable.getInstance().getTemplate(createitem[k]);
						if (temp.isStackable()) {
							if (!pc.getInventory().checkItem(createitem[k])) {
								create_count += 1;
							}
						} else {
							create_count += createcount[k];
						}
						create_weight += temp.getWeight() * createcount[k];
					}

					if (pc.getInventory().getSize() + create_count > 180) {
						pc.sendPackets(new S_ServerMessage(263));
						return;
					}

					if (pc.getMaxWeight() < pc.getInventory().getWeight() + create_weight) {
						pc.sendPackets(new S_ServerMessage(82));
						return;
					}

					for (int j = 0; j < materials.length; j++) {
						pc.getInventory().consumeItem(materials[j], counts[j]);
					}

					L1ItemInstance item = null;
					for (int k = 0; k < createitem.length; k++) {
						item = pc.getInventory().storeItem(createitem[k], createcount[k]);
						if (item != null) {
							String itemName = ItemTable.getInstance().getTemplate(createitem[k]).getName();
							String createrName = "";
							if (obj instanceof L1NpcInstance) {
								createrName = ((L1NpcInstance) obj).getNpcTemplate().get_name();
							}
							if (createcount[k] > 1) {
								pc.sendPackets(new S_ServerMessage(143, createrName, itemName + " (" + createcount[k] + ")"));
							} else {
								pc.sendPackets(new S_ServerMessage(143, createrName, itemName));
							}
						}
					}
				}
			}

			if (htmlid != null) {
				pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata));
			}

		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	/** 소스 수정 및 정보 를 위한 정보
	 * 1. getPetQuest() 디비에 날짜를 기옥해서 그 날짜일시에 1일 정보로
	 * 훈련사에게 훈련 할수잇도록 해줌
	 * 2. 디비 업데이트
	 * 3. 디비 날짜가 만약 9시 초기화 이후라면 무조건 훈련시켜주고 디비 다시 리셋
	 * 이해 안되는거 지금 말하도록*/
	private String 훈련사(L1PcInstance pc, String s) {
		String htmlid = "";
		try {
			if (s.equalsIgnoreCase("B")) {
				L1PetInstance Pet = (L1PetInstance)pc.getPet();
				/** 펫 소환 안되어있어 진행 불가 */
				if (Pet == null) return htmlid = "c_trainer3";
				/** 다이상태라면 */
				if(Pet.isDeadExp()) return htmlid = "c_trainer4";
				/** 레벨이 30이하면 진행 불가 */
				if(Pet.getLevel() < 30) return htmlid = "c_trainer8";
				//Timestamp PetQuest = pc.getNetConnection().getAccount().getPetQuest();
				/** 펫 퀘스트가 놀이거나 참이라면 진행 아니라면 미진행 */
				//if(PetQuest == null || isPetQuest(PetQuest)){
					/** 아이템 체크 2개 체크 다이아 있는지 체크후에
					 * 우정포인트 체크 둘다 있을때만 삭제 아니라면 메세지 링크*/
					L1ItemInstance Item = pc.getInventory().findItemId(60517);
					if(Item != null && Item.getCount() >= 5) {
						if(Pet.getFriendship() >= 2) {
							/** 모두 확인된 상태라 체크후에 삭제하고 경험치 올려줌 */
							pc.getInventory().removeItem(Item, 5);
							Pet.setFriendship(Pet.getFriendship() - 2);

							/** 디비 정보 업데이트 */
							//Timestamp TimeMillis = new Timestamp(System.currentTimeMillis());
							//pc.getNetConnection().getAccount().setPetQuest(TimeMillis);
							//pc.getNetConnection().getAccount().UpdatePetQuest();

							/** 경험치 리액션 */
							int NeedExp = ExpTable.getNeedExpNextLevel(52);
							int Exp = (int)(NeedExp * 0.03);
							Pet.AddExpPet(Exp);
							Broadcaster.broadcastPacket(Pet, new S_DoActionGFX(Pet.getId(), 67 + _random.nextInt(2)), true);
							Broadcaster.broadcastPacket(Pet, new S_SkillSound(Pet.getId(), 3944), true);
							pc.sendPackets(new S_PetWindow(S_PetWindow.PatPoint, Pet), true);
							PetTable.UpDatePet(Pet);
							return htmlid = "c_trainer2";
						} else {
							return htmlid = "c_trainer5";
						}
					} else {
						return htmlid = "c_trainer6";
					}
				//}else return htmlid = "c_trainer7";
			/** 액션값이 다르다면 */
			} else {
				return htmlid = null;
			}

		} catch (Exception e) { }

		return htmlid;
	}


	private String 바이올렛(L1PcInstance pc, String s) {
		String htmlid = "";
		try {
			if (s.equalsIgnoreCase("pet island")) {
				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				if (pc.get수련day() == null) pc.set수련day(nowday);

				try {
					if(!pc.getInventory().consumeItem(40308, 1000)){
						return htmlid = "violetf";
					}

					int outtime = 60 * 60 * 1;
					int usetime = pc.get수련time();
					String Check = isPC입장가능여부(pc.get수련day(), outtime, usetime);
					L1Location loc = new L1Location(32820, 32901, (short)10101).randomLocation(4,true);
					if (Check.equals("입장가능")) {
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						if (h > 0) {
							pc.sendPackets(new S_SystemMessage("입장 시간 : " + h + "시간 " + m + "분 남음"), true);
						} else {
							pc.sendPackets(new S_SystemMessage("입장 시간 : " + m + "분 남음"), true);
						}
						L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short)loc.getMapId(), 6, true);
					} else if (Check.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_SystemMessage("입장 시간 : 2시간을 모두 사용"), true);
					} else if (Check.equals("초기화")) {// 초기화
						pc.set수련time(0);
						pc.set수련day(nowday);
						pc.getNetConnection().getAccount().updateDGTime();
						pc.sendPackets(new S_SystemMessage("입장 시간 : 1시간이 남았습니다."), true);;
						L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short)loc.getMapId(), 6, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				pc.sendPackets(new S_SystemMessage("다른사냥터는 현재 사용 불가능합니다."), true);
				return htmlid = null;
			}
		} catch (Exception e) {}
		return htmlid;
	}


	private String 화말귀환(L1PcInstance pc, String s) {
		String htmlid = "";
		try {
			if (s.equalsIgnoreCase("T_orc")) {
				L1Location loc = new L1Location(32753, 32447, (short)4).randomLocation(4,true);
				L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short)loc.getMapId(), 6, true);
			}
		} catch (Exception e) { }
		return htmlid;
	}

	private String 정령력변경(L1PcInstance pc, L1Object obj, String s) {
		String htmlid = "";

		if (!pc.isElf()) {
			return "";
		}

		if (s.equalsIgnoreCase("fire")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return "";
				}
				pc.setElfAttr(2);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("water")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return "";
				}
				pc.setElfAttr(4);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 2), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("air")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return "";
				}
				pc.setElfAttr(8);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 4), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("earth")) {
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0) {
					return "";
				}
				pc.setElfAttr(1);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 8), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("count")) {
			String[] htmldata = { "50000" };

			int adena = 50000 * (pc.getElfAttrResetCount() + 1);
			if (adena > 10000000) {
				adena = 10000000;
			}

			htmldata[0] = String.valueOf(adena);

			pc.sendPackets(new S_NPCTalkReturn(obj.getId(), "ellyonne12", htmldata));
			htmlid = null;
		} else if (s.equalsIgnoreCase("money") || s.equalsIgnoreCase("init")) {
			if (pc.getElfAttr() == 0) {
				return "";
			}

			int adena = 50000 * (pc.getElfAttrResetCount() + 1);
			if (adena > 10000000) {
				adena = 10000000;
			}

			if (pc.getInventory().consumeItem(L1ItemId.ADENA, adena)) {
				pc.setElfAttr(0);
				pc.setElfAttrResetCount(pc.getElfAttrResetCount() + 1);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 0), true);
				htmlid = "";
			} else {
				htmlid = "ellyonne13";
			}
		} else if (s.equalsIgnoreCase("bm")) {
			if (pc.getElfAttr() == 0) {
				return "";
			}

			if (pc.getInventory().consumeItem(430005, 2)) {
				pc.setElfAttr(0);
				pc.setElfAttrResetCount(pc.getElfAttrResetCount() + 1);
				pc.sendPackets(new S_ServerMessage(678));
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 0), true);
				htmlid = "";
			} else {
				htmlid = "ellyonne13";
			}
	  } else if (s.equalsIgnoreCase("gfw") && pc.글로리어스) { //불물
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0 || !pc.글로리어스) {
					return "";
				}
				pc.setElfAttr(24);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 2), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("gfa") && pc.글로리어스) {//불바람
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0 || !pc.글로리어스) {
					return "";
				}
				pc.setElfAttr(28);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 4), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("gfe") && pc.글로리어스) { //불땅
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0 || !pc.글로리어스) {
					return "";
				}
				pc.setElfAttr(21);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 8), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("gwa") && pc.글로리어스) { //물바람
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0 || !pc.글로리어스) {
					return "";
				}
				pc.setElfAttr(48);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 2), true);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 4), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("gwe") && pc.글로리어스) { //물땅
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0 || !pc.글로리어스) {
					return "";
				}
				pc.setElfAttr(41);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 2), true);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 8), true);
				htmlid = "";
			}
		} else if (s.equalsIgnoreCase("gae") && pc.글로리어스) { //바람땅
			if (pc.isElf()) {
				if (pc.getElfAttr() != 0 || !pc.글로리어스) {
					return "";
				}
				pc.setElfAttr(81);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 4), true);
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 8), true);
				htmlid = "";
			}
		}

		return htmlid;
	}


	private String 수련자관리인(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equals("b")) {// 숨계
			L1Teleport.teleport(pc, 32767, 32747, (short) 2005, pc.getMoveState().getHeading(), true);
		} else if (s.equals("c")) {// 기란
			L1Teleport.teleport(pc, 32606, 32943, (short) 2005, pc.getMoveState().getHeading(), true);
		} else if (s.equals("d")) {// 기란
			L1Teleport.teleport(pc, 32677, 32739, (short) 2005, pc.getMoveState().getHeading(), true);
		} else if (s.equals("e")) {// 기란
			L1Teleport.teleport(pc, 32730, 32989, (short) 2005, pc.getMoveState().getHeading(), true);
		} else if (s.equals("f")) {// 기란
			L1Teleport.teleport(pc, 32539, 32965, (short) 2005, pc.getMoveState().getHeading(), true);
		}
		return htmlid;
	}

	private String 리키(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equals("a")) {// 숨계
			if (pc.getLevel() <= 44) {
				L1Teleport.teleport(pc, 32669, 32858, (short) 2005, 2, true);
			}
		} else if (s.equals("b")) {// 기란
			L1Teleport.teleport(pc, 33437, 32797, (short) 4, 2, true);
		}
		if (pc.getLevel() >= 10 && pc.getLevel() < 30) {
			if (s.equals("c")) {// 로우풀
				L1Teleport.teleport(pc, 33184, 33449, (short) 4, 2, true);
			} else if (s.equals("d")) {// 카오틱
				L1Teleport.teleport(pc, 33066, 33218, (short) 4, 2, true);
			}
		}
		if (pc.getLevel() >= 10 && pc.getLevel() < 45) {
			if (s.equals("A")) {// 믿음
				if (pc.getLevel() >= 10 && pc.getLevel() <= 19) {
					L1Teleport.teleport(pc, 32801, 32806, (short) 25, 2, true);
				} else if (pc.getLevel() >= 20 && pc.getLevel() <= 29) {
					L1Teleport.teleport(pc, 32805, 32747, (short) 26, 2, true);
				} else if (pc.getLevel() >= 30 && pc.getLevel() <= 39) {
					L1Teleport.teleport(pc, 32809, 32766, (short) 27, 2, true);
				} else if (pc.getLevel() >= 40 && pc.getLevel() <= 44) {
					L1Teleport.teleport(pc, 32799, 32798, (short) 28, 2, true);
				}
			} else if (s.equals("B")) {// 용기
				if (pc.getLevel() >= 10 && pc.getLevel() <= 19) {
					L1Teleport.teleport(pc, 32801, 32806, (short) 2221, 2, true);
				} else if (pc.getLevel() >= 20 && pc.getLevel() <= 29) {
					L1Teleport.teleport(pc, 32805, 32747, (short) 2222, 2, true);
				} else if (pc.getLevel() >= 30 && pc.getLevel() <= 39) {
					L1Teleport.teleport(pc, 32809, 32766, (short) 2223, 2, true);
				} else if (pc.getLevel() >= 40 && pc.getLevel() <= 44) {
					L1Teleport.teleport(pc, 32799, 32798, (short) 2224, 2, true);
				}
			} else if (s.equals("C")) {// 집중
				if (pc.getLevel() >= 10 && pc.getLevel() <= 19) {
					L1Teleport.teleport(pc, 32801, 32806, (short) 2225, 2, true);
				} else if (pc.getLevel() >= 20 && pc.getLevel() <= 29) {
					L1Teleport.teleport(pc, 32805, 32747, (short) 2226, 2, true);
				} else if (pc.getLevel() >= 30 && pc.getLevel() <= 39) {
					L1Teleport.teleport(pc, 32809, 32766, (short) 2227, 2, true);
				} else if (pc.getLevel() >= 40 && pc.getLevel() <= 44) {
					L1Teleport.teleport(pc, 32799, 32798, (short) 2228, 2, true);
				}
			} else if (s.equals("D")) {// 신뢰
				if (pc.getLevel() >= 10 && pc.getLevel() <= 19) {
					L1Teleport.teleport(pc, 32801, 32806, (short) 2229, 2, true);
				} else if (pc.getLevel() >= 20 && pc.getLevel() <= 29) {
					L1Teleport.teleport(pc, 32805, 32747, (short) 2230, 2, true);
				} else if (pc.getLevel() >= 30 && pc.getLevel() <= 39) {
					L1Teleport.teleport(pc, 32809, 32766, (short) 2231, 2, true);
				} else if (pc.getLevel() >= 40 && pc.getLevel() <= 44) {
					L1Teleport.teleport(pc, 32799, 32798, (short) 2232, 2, true);
				}
			}
		}

		if (pc.getLevel() >= 45 && pc.getLevel() < 52) {
			if (s.equals("E")) {// 신뢰
				L1Teleport.teleport(pc, 32905, 32626, (short) 2010, 2, true);
			} else if (s.equals("F")) {// 신뢰
				L1Teleport.teleport(pc, 32905, 32626, (short) 2233, 2, true);
			} else if (s.equals("G")) {// 신뢰
				L1Teleport.teleport(pc, 32905, 32626, (short) 2234, 2, true);
			} else if (s.equals("H")) {// 신뢰
				L1Teleport.teleport(pc, 32905, 32626, (short) 2235, 2, true);
			}
		}

		return htmlid;
	}

	private String 쿠루(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("teleport tamshop")) {// 집시촌
			L1Teleport.teleport(pc, 33962, 32957, (short) 4, 2, true);
		} else {
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.CRAFT_ITEM));
		}
		return htmlid;
	}

	@SuppressWarnings("deprecation")
	private String isAccount입장가능여부(Timestamp accountday, int outtime, int usetime) {
		Timestamp nowday = new Timestamp(System.currentTimeMillis());
		String end = "불가능";
		String ok = "입장가능";
		String start = "초기화";
		if (accountday != null) {
			long clac = nowday.getTime() - accountday.getTime();

			int hours = nowday.getHours();
			int lasthours = accountday.getHours();

			if (accountday.getDate() != nowday.getDate()) {
				if (clac > 86400000 || hours >= Config.D_Reset_Time || lasthours < Config.D_Reset_Time) {
					return start;
				}
			} else {
				if (lasthours < Config.D_Reset_Time && hours >= Config.D_Reset_Time) {
					return start;
				}
			}

			if (outtime <= usetime) {
				return end;// 모두사용
			} else {
				return ok;
			}

		} else {
			return start;
		}
	}


	@SuppressWarnings("deprecation")
	private String isPC입장가능여부(Timestamp accountday, int outtime, int usetime) {
		Timestamp nowday = new Timestamp(System.currentTimeMillis());
		String end = "불가능";
		String ok = "입장가능";
		String start = "초기화";
		if (accountday != null) {
			long clac = nowday.getTime() - accountday.getTime();

			int hours = nowday.getHours();
			int lasthours = accountday.getHours();

			if (accountday.getDate() != nowday.getDate()) {
				// System.out.println(nowday.getHours());
				if (clac > 86400000 || hours >= Config.D_Reset_Time || lasthours < Config.D_Reset_Time) {
					return start;
				}
			} else {
				if (lasthours < Config.D_Reset_Time && hours >= Config.D_Reset_Time) {
					return start;
				}
			}

			if (outtime <= usetime) {
				return end;// 모두사용
			} else {
				return ok;
			}

		} else {
			return start;
		}
	}
	 private String 정무(L1PcInstance pc, String s) {

			String htmlid = "";
			try {
				int x = 0, y = 0, mapid = 0;
				if (s.equalsIgnoreCase("a_soul")) {
					x = 32902 + _random.nextInt(2);
					y = 32810 + _random.nextInt(2);
					mapid = 430;
				} else if (s.equalsIgnoreCase("a_soul_center")) {
					x = 32797 + _random.nextInt(2);
					y = 32916 + _random.nextInt(2);
					mapid = 430;
				} else if (s.equalsIgnoreCase("a_giant")) {
					x = 32769 + _random.nextInt(2);
					y = 32881 + _random.nextInt(2);
					mapid = 624;
				}
				if (mapid != 0) {
					try {
						int usetime = pc.get고무time();
						int outtime = 60 * 60 * 2;
						Timestamp nowday = new Timestamp(System.currentTimeMillis());
						L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
						String s1 = isPC입장가능여부(pc.get고무day(), outtime, usetime);
						if (s1.equals("입장가능")) {// 입장가능
							int h = (outtime - usetime) / 60 / 60;
							if (h < 0) {
								h = 0;
							}
							int m = (outtime - usetime) / 60 % 60;
							if (m < 0) {
								m = 0;
							}
							if (h > 0) {
								pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간  %분남았습니다.
							} else {
								pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
							}
							L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
						} else if (s1.equals("불가능")) {// 입장불가능
							pc.sendPackets(new S_ServerMessage(1522, "2"));// 5시간 모두사용했다.
							return "";
						} else if (s1.equals("초기화")) {// 초기화
							pc.set고무time(1);
							pc.set고무day(nowday);
							pc.save();
							pc.sendPackets(new S_ServerMessage(1526, "2"));// 시간남았다.
							L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
			}
			return htmlid;
		}

	private String 헤이트(L1PcInstance pc, String s) {

		String htmlid = "";
		if (pc.getLevel() < 50)
			return "edlen5";
		if (s.equalsIgnoreCase("a")) {

			if (!pc.getInventory().checkItem(60511)) {
				htmlid = "edlen4";// 중앙 사원 비밀 열쇠 없을시에
			} else {
				L1Teleport.teleport(pc, 32800, 32798, (short) 1935, 2, true);
			}
		} else if (s.equalsIgnoreCase("b")) {// 기란
			L1Teleport.teleport(pc, 33440, 32809, (short) 4, 2, true);
		}
		return htmlid;
	}
	private String 지룡의오브(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
			if(pc.getInventory().checkItem(40308, 3000)) {
				pc.getInventory().consumeItem(40308, 3000);
				L1Teleport.teleport(pc, 33331, 32445, (short) 15430, 2, true);//지룡의오브
			} else {
				pc.sendPackets(new S_ServerMessage(337, "$4"));
			}
		} else if (s.equalsIgnoreCase("b")) {// 용의계곡던전 1층
			if(pc.getInventory().checkItem(40308, 4000)) {
				pc.getInventory().consumeItem(4000);
				L1Teleport.teleport(pc, 32811, 32810, (short) 30, 2, true);
			} else {
				pc.sendPackets(new S_ServerMessage(337, "$4"));
			}
		} else if (s.equalsIgnoreCase("c")) {// 용의계곡던전 2층
			if(pc.getInventory().checkItem(40308, 5000)) {
				pc.getInventory().consumeItem(5000);
				L1Teleport.teleport(pc, 32786, 32785, (short) 31, 2, true);
			} else {
				pc.sendPackets(new S_ServerMessage(337, "$4"));
			}
		} else if (s.equalsIgnoreCase("d")) {// 용의계곡던전 3층
			if(pc.getInventory().checkItem(40308, 6000)) {
				pc.getInventory().consumeItem(6000);
				L1Teleport.teleport(pc, 32706, 32820, (short) 32, 2, true);
			} else {
				pc.sendPackets(new S_ServerMessage(337, "$4"));
			}
		} else if (s.equalsIgnoreCase("e")) {// 용의계곡던전 4층
			if(pc.getInventory().checkItem(40308, 7000)) {
				pc.getInventory().consumeItem(7000);
				L1Teleport.teleport(pc, 32669, 32869, (short) 33, 2, true);
			} else {
				pc.sendPackets(new S_ServerMessage(337, "$4"));
			}
		} else if (s.equalsIgnoreCase("f")) {// 용의계곡던전 5층
			if(pc.getInventory().checkItem(40308, 10000)) {
				pc.getInventory().consumeItem(10000);
				L1Teleport.teleport(pc, 32744, 32803, (short) 35, 2, true);
			} else {
				pc.sendPackets(new S_ServerMessage(337, "$4"));
			}
		} else if (s.equalsIgnoreCase("g")) {// 용의계곡던전 6층
			if(pc.getInventory().checkItem(40308, 15000)) {
				pc.getInventory().consumeItem(15000);
				L1Teleport.teleport(pc, 32665, 32847, (short) 36, 2, true);
			} else {
				pc.sendPackets(new S_ServerMessage(337, "$4"));
			}
		}
		return htmlid;
	}
	private String 수룡의오브(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 34140, 32191, (short) 15420, 2, true);//수룡의오브
		}
		return htmlid;
	}
	private String 풍룡의오브(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 34145, 32795, (short) 15410, 2, true);//풍룡의오브
		}
		return htmlid;
	}
	private String 화룡의오브(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 33654, 32412, (short) 15440, 2, true);//화룡의오브
		}
		return htmlid;
	}

	private String 피그만(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 32768, 32832, (short) 7, 1, true);//피그만
		}
		return htmlid;
	}
	private String 스파인(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 32757, 32801, (short) 12146, 1, true);//피그만
		}
		return htmlid;
	}

	private String 그레이브(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 32759, 33046, (short) 12147, 1, true);//피그만
		}
		return htmlid;
	}

	private String 워든(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 32832, 32959, (short) 8, 1, true);//피그만
		}
		return htmlid;
	}

	private String 도아만(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 32681, 32844, (short) 9, 1, true);//피그만
		}
		return htmlid;
	}

	private String 지배의균열1층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("1")) {
			if (pc.getInventory().checkItem(5001120, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32727, 32800, (short) 12852, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}

	private String 지배의균열2층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("2")) {
			if (pc.getInventory().checkItem(5001121, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32721, 32803, (short) 12853, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}
	private String 지배의균열3층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("3")) {
			if (pc.getInventory().checkItem(5001122, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32724, 32803, (short) 12854, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}
	private String 지배의균열4층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("4")) {
			if (pc.getInventory().checkItem(5001123, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32597, 32863, (short) 12855, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}
	private String 지배의균열5층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("5")) {
			if (pc.getInventory().checkItem(5001124, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32592, 32866, (short) 12856, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}

	private String 지배의균열6층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("6")) {
			if (pc.getInventory().checkItem(5001125, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32601, 32866, (short) 12857, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}

	private String 지배의균열7층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("7")) {
			if (pc.getInventory().checkItem(5001126, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32603, 32866, (short) 12858, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}

	private String 지배의균열8층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("8")) {
			if (pc.getInventory().checkItem(5001127, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32593, 32866, (short) 12859, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}

	private String 지배의균열9층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("9")) {
			if (pc.getInventory().checkItem(5001128, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32602, 32866, (short) 12860, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}

	private String 지배의균열10층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("10")) {
			if (pc.getInventory().checkItem(5001129, 1) || pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32732, 32803, (short) 12861, 2, true);// 화룡의오브
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}

	private String 지배의균열정상(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("11")) {
			if (pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32639, 32813, (short) 12862, 2, true);// 화룡의오브
			} else if (pc.getInventory().checkItem(40113, 1)) {
				L1Teleport.teleport(pc, 32639, 32813, (short) 12862, 2, true);// 화룡의오브
				pc.getInventory().consumeItem(40113, 1);
			} else {
				htmlid = "inter_o_enf";
			}
		} else if (s.equalsIgnoreCase("12")) {
			if (pc.getInventory().checkItem(60203, 1)) {
				L1Teleport.teleport(pc, 32761, 32803, (short) 12862, 2, true);// 화룡의오브
			} else if (pc.getInventory().checkItem(40113, 1)) {
				L1Teleport.teleport(pc, 32761, 32803, (short) 12862, 2, true);// 화룡의오브
				pc.getInventory().consumeItem(40113, 1);
			} else {
				htmlid = "inter_o_enf";
			}
		}
		return htmlid;
	}
	private String 레지(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
				L1Teleport.teleport(pc, 33444, 32812, (short) 4, 2, true);// 화룡의오브
		}
		return htmlid;
	}

	private String 오르타무1층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("21")) {
				L1Teleport.teleport(pc, 32758, 32797, (short) 12852, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무2층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("22")) {
			L1Teleport.teleport(pc, 32761, 32802, (short) 12853, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무3층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("23")) {
			L1Teleport.teleport(pc, 32762, 32799, (short) 12854, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무4층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("24")) {
			L1Teleport.teleport(pc, 32624, 32846, (short) 12855, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무5층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("25")) {
			L1Teleport.teleport(pc, 32616, 32866, (short) 12856, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무6층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("26")) {
			L1Teleport.teleport(pc, 32611, 32886, (short) 12857, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무7층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("27")) {
			L1Teleport.teleport(pc, 32626, 32866, (short) 12858, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무8층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("28")) {
			L1Teleport.teleport(pc, 32617, 32862, (short) 12859, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무9층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("29")) {
			L1Teleport.teleport(pc, 32619, 32865, (short) 12860, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무10층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("30")) {
			L1Teleport.teleport(pc, 32752, 32803, (short) 12861, 3, true);// 화룡의오브
		}
		return htmlid;
	}
	private String 오르타무정상층(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("14")) {
			L1Teleport.teleport(pc, 32639, 32813, (short) 12862, 2, true);// 화룡의오브
		} else if (s.equalsIgnoreCase("15")) {
			L1Teleport.teleport(pc, 32761, 32803, (short) 12862, 2, true);// 화룡의오브
	    }
		return htmlid;
	}

	private String 루핀(L1PcInstance pc, String s) {
		String htmlid = "";
		try {
			int x = 0, y = 0, mapid = 0;
			if (s.equalsIgnoreCase("a")) {
				if (pc.getLevel() >= 1 & pc.getLevel() <= 80) {// 해당레벨
					if (pc.getInventory().checkItem(60716, 500)) {
						pc.getInventory().consumeItem(60716, 500);
						x = 32665 + _random.nextInt(2);
						y = 32791 + _random.nextInt(2);
						mapid = 2;
					} else {
						pc.sendPackets(new S_ServerMessage(337, "$30202")); // \f1%0이 부족합니다.
					}
				} else {
					htmlid = "lupinf";
				}
				if (mapid != 0) {
					L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
					L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
				}
			} else if (s.equalsIgnoreCase("b")){ // 수련 던전 1층
				if (pc.getLevel() >= 55 & pc.getLevel() <= 74){
					if (pc.getInventory().checkItem(60716, 500)) {
						pc.getInventory().consumeItem(60716, 500);
						L1Teleport.teleport(pc, 32808, 32810, (short) 25, 5, true);
					} else {
						pc.sendPackets(new S_ServerMessage(337, "$30202")); // \f1%0이 부족합니다.
					}
				} else {
					htmlid = "lupinf";
				}
			} else if (s.equalsIgnoreCase("c")){ // 수련 던전 3층
				if (pc.getLevel() >= 55 & pc.getLevel() <= 74){
					if (pc.getInventory().checkItem(60716, 500)) {
						pc.getInventory().consumeItem(60716, 500);
						L1Teleport.teleport(pc, 32808, 32766, (short) 27, 5, true);
					} else {
						pc.sendPackets(new S_ServerMessage(337, "$30202")); // \f1%0이 부족합니다.
					}
				} else {
					htmlid = "lupinf";
				}
			} else if (s.equalsIgnoreCase("d")){ // 버림받은 자들의 땅
				if (pc.getLevel() >= 75) {// 해당레벨
					x = 32537 + _random.nextInt(2);
					y = 32954 + _random.nextInt(2);
					mapid = 777;
				} else {
					htmlid = "lupinf";
				}
				if (mapid != 0) {
					int usetime = pc.get버땅time();
					int outtime = 0;

					outtime = 60 * 60 * 2;

					Timestamp nowday = new Timestamp(System.currentTimeMillis());
					L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
					try {
						String s1 = isPC입장가능여부(pc.get버땅day(), outtime, usetime);
						if (s1.equals("입장가능")) {// 입장가능
							int h = (outtime - usetime) / 60 / 60;
							if (h < 0) {
								h = 0;
							}
							int m = (outtime - usetime) / 60 % 60;
							if (m < 0) {
								m = 0;
							}
							if (h > 0) {
								pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));
							} else {
								pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));
							}

							L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
							pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
						} else if (s1.equals("불가능")) { // 입장불가능
							pc.sendPackets(new S_ServerMessage(1522, "2"));// 모두사용했다.
							return "";
						} else if (s1.equals("초기화")) { // 초기화
							pc.set버땅time(1);
							pc.set버땅day(nowday);
							pc.save();
							pc.sendPackets(new S_ServerMessage(1526, "2"));// 시간남았다.
							L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
							pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else if (s.equalsIgnoreCase("e")){ // 드워프 지역
				L1Teleport.teleport(pc, 32658, 33059, (short) 9, 5, true);
			} else if (s.equalsIgnoreCase("f")){ // 말하는 섬 던전 입구
				L1Teleport.teleport(pc, 32491, 32859, (short) 9, 5, true);
			} else if (s.equalsIgnoreCase("g")){ // 오크 지역
				L1Teleport.teleport(pc, 32393, 32945, (short) 9, 5, true);
			} else if (s.equalsIgnoreCase("h")){ // 거미 지역
				L1Teleport.teleport(pc, 32456, 33020, (short) 9, 5, true);
			} else if (s.equalsIgnoreCase("i")){ // 흑기사 전초 기지
				L1Teleport.teleport(pc, 32707, 33149, (short) 9, 5, true);
			} else if (s.equalsIgnoreCase("j")){ // 검은 전함
				L1Teleport.teleport(pc, 32671, 33250, (short) 9, 5, true);
			} else if (s.equalsIgnoreCase("k")){ // 게렝의 집
				L1Teleport.teleport(pc, 32563, 33083, (short) 9, 5, true);
			}
		} catch (Exception e) {
		}

		return htmlid;
	}


	private String 오만의탑이동동상(L1PcInstance pc, String s) {
		String htmlid = "";
		try {
			int x = 0, y = 0, mapid = 0;
			if (s.equalsIgnoreCase("1")) {
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(60202, 1)
						|| pc.getInventory().checkItem(5001120, 1)){ // 부적
					x = 32735 + _random.nextInt(2);
					y = 32798 + _random.nextInt(2);
					mapid = 101;
				} else if (pc.getInventory().checkItem(60200, 1)){ // 주문서
					pc.getInventory().consumeItem(60200, 1);
					x = 32735 + _random.nextInt(2);
					y = 32798 + _random.nextInt(2);
					mapid = 101;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$12333")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("2")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40289, 1)
						|| pc.getInventory().checkItem(5001121, 1)){ // 부적
					x = 32730 + _random.nextInt(2);
					y = 32802 + _random.nextInt(2);
					mapid = 102;
				} else if (pc.getInventory().checkItem(40104, 1)){ // 주문서
					pc.getInventory().consumeItem(40104, 1);
					x = 32730 + _random.nextInt(2);
					y = 32802 + _random.nextInt(2);
					mapid = 102;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2169")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("3")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40290, 1)
						|| pc.getInventory().checkItem(5001122, 1)){ // 부적
					x = 32726 + _random.nextInt(2);
					y = 32803 + _random.nextInt(2);
					mapid = 103;
				} else if (pc.getInventory().checkItem(40105, 1)){ // 주문서
					pc.getInventory().consumeItem(40105, 1);
					x = 32726 + _random.nextInt(2);
					y = 32803 + _random.nextInt(2);
					mapid = 103;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2168")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("4")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40291, 1)
						|| pc.getInventory().checkItem(5001123, 1)){ // 부적
					x = 32621 + _random.nextInt(2);
					y = 32858 + _random.nextInt(2);
					mapid = 104;
				} else if (pc.getInventory().checkItem(40106, 1)){ // 주문서
					pc.getInventory().consumeItem(40106, 1);
					x = 32621 + _random.nextInt(2);
					y = 32858 + _random.nextInt(2);
					mapid = 104;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2404")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("5")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40292, 1)
						|| pc.getInventory().checkItem(5001124, 1)){ // 부적
					x = 32599 + _random.nextInt(2);
					y = 32866 + _random.nextInt(2);
					mapid = 105;
				} else if (pc.getInventory().checkItem(40107, 1)){ // 주문서
					pc.getInventory().consumeItem(40107, 1);
					x = 32599 + _random.nextInt(2);
					y = 32866 + _random.nextInt(2);
					mapid = 105;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2405")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("6")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40293, 1)
						|| pc.getInventory().checkItem(5001125, 1)){ // 부적
					x = 32611 + _random.nextInt(2);
					y = 32862 + _random.nextInt(2);
					mapid = 106;
				} else if (pc.getInventory().checkItem(40108, 1)){ // 주문서
					pc.getInventory().consumeItem(40108, 1);
					x = 32611 + _random.nextInt(2);
					y = 32862 + _random.nextInt(2);
					mapid = 106;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2673")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("7")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40294, 1)
						|| pc.getInventory().checkItem(5001126, 1)){ // 부적
					x = 32618 + _random.nextInt(2);
					y = 32866 + _random.nextInt(2);
					mapid = 107;
				} else if (pc.getInventory().checkItem(40109, 1)){ // 주문서
					pc.getInventory().consumeItem(40109, 1);
					x = 32618 + _random.nextInt(2);
					y = 32866 + _random.nextInt(2);
					mapid = 107;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2674")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("8")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40295, 1)
						|| pc.getInventory().checkItem(5001127, 1)){ // 부적
					x = 32600 + _random.nextInt(2);
					y = 32866 + _random.nextInt(2);
					mapid = 108;
				} else if (pc.getInventory().checkItem(40110, 1)){ // 주문서
					pc.getInventory().consumeItem(40110, 1);
					x = 32600 + _random.nextInt(2);
					y = 32866 + _random.nextInt(2);
					mapid = 108;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2675")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("9")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40296, 1)
						|| pc.getInventory().checkItem(5001128, 1)){ // 부적
					x = 32612 + _random.nextInt(2);
					y = 32866 + _random.nextInt(2);
					mapid = 109;
				} else if (pc.getInventory().checkItem(40111, 1)){ // 주문서
					pc.getInventory().consumeItem(40111, 1);
					x = 32612 + _random.nextInt(2);
					y = 32866 + _random.nextInt(2);
					mapid = 109;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2676")); // \f1%0이 부족합니다.
				}
			} else if (s.equalsIgnoreCase("10")){
				if (pc.getInventory().checkItem(60203, 1)
						|| pc.getInventory().checkItem(40297, 1)
						|| pc.getInventory().checkItem(5001129, 1)){ // 부적
					x = 32729 + _random.nextInt(2);
					y = 32802 + _random.nextInt(2);
					mapid = 110;
				} else if (pc.getInventory().checkItem(40112, 1)){ // 주문서
					pc.getInventory().consumeItem(40112, 1);
					x = 32729 + _random.nextInt(2);
					y = 32802 + _random.nextInt(2);
					mapid = 110;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$2677")); // \f1%0이 부족합니다.
				}
			}

			if (mapid != 0) {
				L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
				L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
			}

		} catch (Exception e) {
		}

		return htmlid;
	}



	private String 염탐꾼오크(L1PcInstance pc, String s) {
		String htmlid = "";
		try {
			int x = 0, y = 0, mapid = 0;
			if (s.equalsIgnoreCase("atuba")) {
				if (pc.getInventory().checkItem(40308, 10000)) {
					pc.getInventory().consumeItem(40308, 10000);
				    x = 32748 + _random.nextInt(2);
				    y = 32745 + _random.nextInt(2);
				    mapid = 491;
				} else {
					pc.sendPackets(new S_ServerMessage(337, "$4")); // \f1%0이 부족합니다.
				}
			}
			if (mapid != 0) {
				int usetime = pc.get아투바time();
				int outtime = 0;

				outtime = 60 * 60 * 3;

				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
				try {
					String s1 = isPC입장가능여부(pc.get아투바day(), outtime, usetime);
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));
						} else {
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));
						}

						L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);

					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "3"));// 모두사용했다.
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.set아투바time(1);
						pc.set아투바day(nowday);
						pc.save();
						pc.sendPackets(new S_ServerMessage(1526, "3"));// 시간남았다.
						L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
		return htmlid;
	}

	private void 에킨스경험치2(L1PcInstance pc) {
		int needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		int exp = 0;
		if (pc.getLevel() <= 60) {
			exp = (int) (needExp * 0.04D);
		} else if (pc.getLevel() <= 65) {
			exp = (int) (needExp * 0.03D);
		} else if (pc.getLevel() <= 70) {
			exp = (int) (needExp * 0.02D);
		} else if (pc.getLevel() <= 75) {
			exp = (int) (needExp * 0.01D);
		} else {
			exp = (int) (needExp * 16D * exppenalty);
		}
		pc.addExp(exp);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
	}

	private void 에킨스경험치6(L1PcInstance pc) {
		int needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		int exp = 0;
		if (pc.getLevel() <= 60) {
			exp = (int) (needExp * 0.12D);
		} else if (pc.getLevel() <= 65) {
			exp = (int) (needExp * 0.09D);
		} else if (pc.getLevel() <= 70) {
			exp = (int) (needExp * 0.06D);
		} else if (pc.getLevel() <= 75) {
			exp = (int) (needExp * 0.03D);
		} else {
			exp = (int) (needExp * 48D * exppenalty);
		}
		pc.addExp(exp);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
	}

	private String 민티스(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equals("K")) {
			if (pc.getInventory().consumeItem(60445, 1)) {
				pc.getInventory().storeItem(60471, 1);
			} else
				htmlid = "mintis9";
		} else {
			if (pc.getLevel() < 45) {
				htmlid = "mintis6";
			} else if (pc.getInventory().checkItem(60446)) {
				try {
					Random random = new Random(System.nanoTime());
					int gn1 = random.nextInt(3);
					int time = 0;
					int outtime = 60 * 40;
					Timestamp nowday = new Timestamp(System.currentTimeMillis());
					random = null;
					if (pc.get솔로타운day() == null) {
						pc.set솔로타운time(1);
						pc.set솔로타운day(nowday);
						pc.sendPackets(new S_ServerMessage(1527, "40"));// 분 남았다
						L1Teleport.teleport(pc, 32833 + gn1, 32958 + gn1, (short) 5501, 5, true, true, 5000);
					} else {
						time = pc.get솔로타운time();
						if (pc.get솔로타운day().getDate() != nowday.getDate()) {
							pc.set솔로타운time(1);
							pc.sendPackets(new S_ServerMessage(1527, "40"));	// 분 남았다
							L1Teleport.teleport(pc, 32608 + gn1, 32875 + gn1, (short) 820, 5, true, true, 5000);
						} else if (outtime > time) {
							L1Teleport.teleport(pc, 32608 + gn1, 32875 + gn1, (short) 820, 5, true, true, 5000);
						} else {
							pc.sendPackets(new S_ServerMessage(1523, "40"));	// 분 모두사용
						}
					}
				} catch (Exception e) {
				}
			} else
				htmlid = "mintis7";
		}
		return htmlid;
	}

	private String 에바왕국수로(L1PcInstance pc, String s2) {
		String htmlid = "";
		Timestamp nowday = new Timestamp(System.currentTimeMillis());
		int x = 0;
		int y = 0;
		short map = 0;

		if (s2.equalsIgnoreCase("a") && pc.getInventory().checkItem(40308, 3000)) {	// 에바 왕국 수로
			x = 32709;
			y = 32793;
			map = 59;
			pc.getInventory().consumeItem(40308, 3000);
		} else if (s2.equalsIgnoreCase("b") && pc.getInventory().checkItem(40308, 6000)) {	// 에바 왕국 수로
			x = 32739;
			y = 32860;
			map = 60;
			pc.getInventory().consumeItem(40308, 6000);
		} else if (s2.equalsIgnoreCase("c") && pc.getInventory().checkItem(40308, 9000)) {	// 에바 왕국 수로
			x = 32727;
			y = 32808;
			map = 61;
			pc.getInventory().consumeItem(40308, 9000);
		} else {
			pc.sendPackets(new S_SystemMessage("입장 조건에 맞지 않습니다."), true);
		}

		try {
			int outtime = 60 * 60 * 2;
			int usetime = pc.get에바time();

			String s = isAccount입장가능여부(pc.get에바day(), outtime, usetime);
			if (s.equals("입장가능")) {	// 입장가능
				L1Teleport.teleport(pc, x, y, map, 6, true);
			} else if (s.equals("불가능")) {	// 입장불가능
				pc.sendPackets(new S_SystemMessage("입장 시간 :모두 사용 하였습니다."), true);
				return "";
			} else if (s.equals("초기화")) {// 초기화
				pc.set에바time(1);
				pc.set에바day(nowday);
				pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간이 아직 남아있습니다."), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlid;
	}

	private String 상탑발록(L1PcInstance pc, String s2) {
		String htmlid = "";
		Timestamp nowday = new Timestamp(System.currentTimeMillis());
		int x = 0;
		int y = 0;
		short map = 0;

		if (s2.equalsIgnoreCase("1")) {// 상아탑 1층
			x = 32770;
			y = 32826;
			map = 75;
		} else if (s2.equalsIgnoreCase("2")) {// 상아탑 2층
			x = 32772;
			y = 32823;
			map = 76;
		} else if (s2.equalsIgnoreCase("3")) {// 상아탑 3층
			x = 32762;
			y = 32839;
			map = 77;
		} else if (s2.equalsIgnoreCase("4")) {// 상아탑 4층
			x = 32731;
			y = 32797;
			map = 282;
		} else if (s2.equalsIgnoreCase("5")) {// 상아탑5층
			x = 32809;
			y = 32869;
			map = 283;
		} else if (s2.equalsIgnoreCase("6")) {// 상아탑 6층
			x = 32679;
			y = 32803;
			map = 284;
		} else if (s2.equalsIgnoreCase("7")) {// 상아탑 7층
			x = 32679;
			y = 32795;
			map = 285;
		}
		try {
			int outtime = 60 * 60 * 2;
			int usetime = pc.getivorytime();

			String s = isAccount입장가능여부(pc.getivoryday(), outtime, usetime);
			if (s.equals("입장가능")) {// 입장가능
				L1Teleport.teleport(pc, x, y, map, 6, true);
			} else if (s.equals("불가능")) {// 입장불가능
				pc.sendPackets(new S_SystemMessage("입장 시간 :모두 사용 하였습니다."), true);
				return "";
			} else if (s.equals("초기화")) {// 초기화
				pc.setivorytime(1);
				pc.setivoryday(nowday);
				pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간이 아직 남아있습니다."), true);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return htmlid;
	}

	@SuppressWarnings("deprecation")
	private String 킹버몽(L1PcInstance pc, String s) {

		String html = "";
		if (s.equalsIgnoreCase("0")) {
			if (pc.getInventory().consumeItem(40308, 30000)) {
				pc.sendPackets(new S_ServerMessage(143, "킹 버몽", pc.getInventory().storeItem(21123, 1).getName()), true);
			} else
				html = "halloween04";
		} else {
			try {
				Random random = new Random(System.nanoTime());
				int gn1 = random.nextInt(3);
				int time = 0;
				int outtime = 60 * 60;
				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				random = null;
				if (pc.get할로윈day() == null) {
					pc.set할로윈time(1);
					pc.set할로윈day(nowday);
					pc.sendPackets(new S_ServerMessage(1526, "1"));// 시간 남았다.
					L1Teleport.teleport(pc, 32833 + gn1, 32958 + gn1, (short) 5501, 5, true, true, 5000);
				} else {
					time = pc.get할로윈time();
					if (pc.get할로윈day().getDate() != nowday.getDate()) {
						pc.set할로윈time(1);
						pc.sendPackets(new S_ServerMessage(1526, "1"));// 시간 남았다.
						L1Teleport.teleport(pc, 32833 + gn1, 32958 + gn1, (short) 5501, 5, true, true, 5000);
					} else if (outtime > time) {
						L1Teleport.teleport(pc, 32833 + gn1, 32958 + gn1, (short) 5501, 5, true, true, 5000);
					} else {
						pc.sendPackets(new S_ServerMessage(1522, "1"));// 5시간 모두 사용했다.
					}
				}
			} catch (Exception e) {
			}
		}
		return html;
	}

	private String 기란투석기A(L1PcInstance pc, String s, L1NpcInstance _npc) {

		try {
			L1MerchantInstance npc = (L1MerchantInstance) _npc;
			int CASTLE_ID = 0;
			int ATT_NPCID = 0;
			int DEF_NPCID = 0;
			if (npc.getNpcId() == 100664 || npc.getNpcId() == 100665) {
				CASTLE_ID = L1CastleLocation.GIRAN_CASTLE_ID;
				ATT_NPCID = 100664;
				DEF_NPCID = 100665;
			} else if (npc.getNpcId() == 100663 || npc.getNpcId() == 100666) {
				CASTLE_ID = L1CastleLocation.KENT_CASTLE_ID;
				ATT_NPCID = 100663;
				DEF_NPCID = 100666;
			} else if (npc.getNpcId() == 100667 || npc.getNpcId() == 100668) {
				CASTLE_ID = L1CastleLocation.OT_CASTLE_ID;
				ATT_NPCID = 100667;
				DEF_NPCID = 100668;
			}
			if (CASTLE_ID == 0)
				return "";
			if (!pc.isCrown() || pc.getClanid() == 0) {
				pc.sendPackets(new S_ServerMessage(2498), true); // 혈맹 군주만 사용 가능
				return "";
			} else if (npc.공성병기ディレイ) {
				pc.sendPackets(new S_ServerMessage(3680), true); // 재장전시간 필요
				return "";
			} else if (!WarTimeController.getInstance().isNowWar(CASTLE_ID)) {
				pc.sendPackets(new S_ServerMessage(3683), true); // 공성시간에만 사용 가능
				return "";
			} else if (pc.getClan().getCastleId() != CASTLE_ID && npc.getNpcId() == DEF_NPCID) {
				pc.sendPackets(new S_ServerMessage(3682), true); // 성혈 군주만 사용가능
				return "";
			} else {
				if (npc.getNpcId() == ATT_NPCID) {
					int castleid = L1CastleLocation.getCastleIdByArea(pc);
					if (castleid != 0) {
						boolean in_war = false;
						List<L1War> wars = L1World.getInstance().getWarList(); // 전전쟁 리스트를 취득
						for (L1War war : wars) {
							if (castleid == war.GetCastleId()) { // 이마이성의 전쟁
								in_war = war.CheckClanInWar(pc.getClanname());
								break;
							}
						}
						if (pc.getClan().getCastleId() != 0 || !in_war) {
							pc.sendPackets(new S_ServerMessage(3681), true); // 선포한 군주만 사용가능
							return "";
						}
					}
				}
			}
			if (pc.getInventory().consumeItem(60410, 1)) {
				npc.공성병기투척ディレイ();
				npc.broadcastPacket(new S_DoActionGFX(npc.getId(),
						ActionCodes.ACTION_Attack), true);
			} else {
				pc.sendPackets(new S_ServerMessage(337, "포탄"), true);
				return "";
			}

			if (s.equalsIgnoreCase("0-1") || s.equalsIgnoreCase("0-2")
					|| s.equalsIgnoreCase("1-11") || s.equalsIgnoreCase("1-12")
					|| s.equalsIgnoreCase("1-13")) {// 켄트 A
				if (CASTLE_ID != L1CastleLocation.KENT_CASTLE_ID || npc.getNpcId() != ATT_NPCID)
					return "";
				if (s.equalsIgnoreCase("0-1")) { // 외성문 방향으로 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33109, 33114, 32768, 32772, (short) 12201, false), 1500);
				} else if (s.equalsIgnoreCase("0-2")) { // 수호탑 방향으로 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33166, 33170, 32777, 32781, (short) 12201, false), 1500);
				} else if (s.equalsIgnoreCase("1-11")) { // 외성문 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33109, 33114, 32768, 32772, (short) 12201, true), 1500);
				} else if (s.equalsIgnoreCase("1-12")) { // 외성문 뒤쪽 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33119, 33124, 32768, 32772, (short) 12201, true), 1500);
				} else if (s.equalsIgnoreCase("1-13")) { // 수호탑 우측으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33171, 33175, 32777, 32781, (short) 12201, true), 1500);
				}
			} else if (s.equalsIgnoreCase("0-8")) {// 켄트 D 외성문 방향으로 발사
				if (CASTLE_ID != L1CastleLocation.KENT_CASTLE_ID || npc.getNpcId() != DEF_NPCID) {
					return "";
				}

				GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33109, 33114, 32768, 32772, (short) 12197, false), 1500);
			} else if (s.equalsIgnoreCase("0-3") || s.equalsIgnoreCase("0-4")
					|| s.equalsIgnoreCase("1-14") || s.equalsIgnoreCase("1-15")) {	// 오크 A

				if (CASTLE_ID != L1CastleLocation.OT_CASTLE_ID || npc.getNpcId() != ATT_NPCID) {
					return "";
				}

				if (s.equalsIgnoreCase("0-3")) { // 외성문 방향으로 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 32792, 32797, 32316, 32320, (short) 12205, false), 1500);
				} else if (s.equalsIgnoreCase("0-4")) { // 수호탑 방향으로 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 32795, 32801, 32282, 32288, (short) 12205, false), 1500);
				} else if (s.equalsIgnoreCase("1-14")) { // 외성문 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 32792, 32797, 32316, 32320, (short) 12205, true), 1500);
				} else if (s.equalsIgnoreCase("1-15")) { // 수호탑 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 32795, 32801, 32282, 32288, (short) 12205, true), 1500);
				}
			} else if (s.equalsIgnoreCase("0-9")) {// 오크 D 외성문 방향으로 발사
				if (CASTLE_ID != L1CastleLocation.OT_CASTLE_ID || npc.getNpcId() != DEF_NPCID)
					return "";
				GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 32792, 32797, 32316, 32320, (short) 12193, false), 1500);
			} else if (s.equalsIgnoreCase("0-5") || s.equalsIgnoreCase("0-6")
					|| s.equalsIgnoreCase("0-7") || s.equalsIgnoreCase("1-16")
					|| s.equalsIgnoreCase("1-17") || s.equalsIgnoreCase("1-18")
					|| s.equalsIgnoreCase("1-19") || s.equalsIgnoreCase("1-20")) {// 기란  A
				if (CASTLE_ID != L1CastleLocation.GIRAN_CASTLE_ID || npc.getNpcId() != ATT_NPCID)
					return "";
				if (s.equalsIgnoreCase("0-5")) { // 외성문 방향으로 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33627, 33637, 32731, 32737, (short) 12205, false), 1500);
				} else if (s.equalsIgnoreCase("0-6")) { // 내성문 방향으로 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33629, 33636, 32700, 32704, (short) 12205, false), 1500);
				} else if (s.equalsIgnoreCase("0-7")) { // 수호탑 방향으로 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33628, 33631, 32675, 32861, (short) 12205, false), 1500);
				} else if (s.equalsIgnoreCase("1-16")) { // 외성문 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33627, 33637, 32731, 32737, (short) 12205, true), 1500);
				} else if (s.equalsIgnoreCase("1-17")) { // 내성문 앞쪽 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33629, 33636, 32700, 32704, (short) 12205, true), 1500);
				} else if (s.equalsIgnoreCase("1-18")) { // 내성문 좌측 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33629, 33632, 32675, 32861, (short) 12205, true), 1500);
				} else if (s.equalsIgnoreCase("1-19")) {// 내성문 우측 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule( new 공성병기투척(npc, 33633, 33636, 32675, 32861, (short) 12205, true), 1500);
				} else if (s.equalsIgnoreCase("1-20")) { // 수호탑 방향으로 침묵포탄 발사
					GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33628, 33631, 32675, 32861, (short) 12205, true), 1500);
				}
			} else if (s.equalsIgnoreCase("0-10")) {// 기란 D 외성문 방향으로 발사
				if (CASTLE_ID != L1CastleLocation.GIRAN_CASTLE_ID || npc.getNpcId() != DEF_NPCID) {
					return "";
				}
				GeneralThreadPool.getInstance().schedule(new 공성병기투척(npc, 33627, 33637, 32731, 32737, (short) 12193, false), 1500);
			}
		} catch (Exception e) {

		}
		return "";
	}

	class 공성병기투척 implements Runnable {
		private int minX = 0;
		private int maxX = 0;
		private int minY = 0;
		private int maxY = 0;
		private L1MerchantInstance npc;
		private L1Location loc;
		private boolean silence;

		public 공성병기투척(L1MerchantInstance _npc, int _minx, int _maxx, int _miny,
				int _maxy, final short gfx, boolean _silence) {
			minX = _minx;
			maxX = _maxx;
			minY = _miny;
			maxY = _maxy;
			npc = _npc;
			silence = _silence;
			loc = new L1Location(minX + (maxX - minX) / 2, minY + (maxY - minY) / 2, 4);
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					for (L1Object obj : L1World.getInstance().getVisiblePoint(loc, 3)) {
						if (obj instanceof L1Character) {
							L1Character cha = (L1Character) obj;
							if (obj instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) obj;
								pc.sendPackets(new S_EffectLocation(loc.getX(), loc.getY(), gfx), true);
							}
							Broadcaster.broadcastPacket(cha, new S_EffectLocation(loc.getX(), loc.getY(), gfx), true);
							break;
						}
					}
				}
			}, 1000);
		}

		@Override
		public void run() {
			// 12193 5시 12197 7시 수성
			// 12201 1시 12205 11시 공성
			int range = 10;
			if (silence) {
				range = 5;
			}
			for (L1Object obj : L1World.getInstance().getVisiblePoint(loc, range)) {
				if (obj instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) obj;
					if (pc.getX() < minX || pc.getX() > maxX || pc.getY() < minY || pc.getY() > maxY)
						continue;
					if (pc.getSkillEffectTimerSet().hasSkillEffect(ABSOLUTE_BARRIER)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_안전모드)) {
						continue;
					}

					int dmg = 250 + _random.nextInt(151);
					if (silence) dmg *= 0.5;
					if (pc.getSkillEffectTimerSet().hasSkillEffect(SPECIAL_COOKING)) {
						dmg -= 5;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(SPECIAL_COOKING2)) {
						dmg -= 5;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EARTH_BLESS)) {
						dmg -= 2;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(COOKING_NEW_닭고기)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(COOKING_NEW_연어)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(COOKING_NEW_칠면조)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(COOKING_NEW_한우)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.miso2)) {
						dmg -= 2;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(메티스정성스프)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(싸이시원한음료)) {
						dmg -= 5;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(메티스정성요리)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(싸이매콤한라면)) {
						dmg -= 5;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(메티스축복주문서)
							|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.흑사의기운)) {
						dmg -= 3;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(IMMUNE_TO_HARM)) {
						dmg *= 0.65;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(ARMOR_BREAK)) {
						dmg *= 1.58;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_AVATAR)) {
						dmg += dmg / 5;
					}
					if (pc.드래곤스킨) {
						dmg -= 2;
					}
					if(pc.드래곤스킨 && pc.getLevel() >= 80) {
						int ddmg = pc.getLevel() - 78;
						int i = (ddmg / 2) * 1;
						dmg -= 2 + i;
					
					}
					if (pc.글로리어스) {
						if (_random.nextInt(100) < 2 + 8 - 5) {
							dmg -= 30;
							pc.sendPackets(new S_SkillSound(pc.getId(), 19318));
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 19318));
						}
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(PATIENCE)) {
						dmg -= 4;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(FEATHER_BUFF_A)) {
						dmg -= 3;
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(FEATHER_BUFF_B)) {
						dmg -= 2;
					}
					if (dmg > 0) {
						pc.receiveDamage(npc, dmg, true);
					}
					if (silence) {
						L1SilencePoison.doInfection(pc, 15);
					}
				}
			}
		}

	}

	private String[] 전령actionList = { "A", "B", "C", "D", "E", "F", "G", "H",
			"I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
			"V", "W", "X", "Y", "Z", "[", "\\\\", "]", "^", "_", "`", "a", "b",
			"c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o",
			"p", "q", "r" };


	@SuppressWarnings("deprecation")
	private String 붉은전령(L1PcInstance pc, String s) {
		String html = "";
		Date day = new Date(System.currentTimeMillis());
		if (day.getHours() % 2 != 0 || day.getMinutes() >= 5) {
			html = "evsiege102";
		} else if (pc.getLevel() < 45) {
			html = "evsiege106";
		} else if (!pc.getInventory().checkItem(60392)) {
			html = "evsiege105";
		} else {
			if (s.equalsIgnoreCase("0")) {
				html = "evsiege104";
			} else {
				int mapid = 0;
				for (String action : 전령actionList) {
					mapid++;
					if (action.equals(s)) {
						mapid += 2300;
						break;
					}
				}
				if (mapid > 2300) {
					Collection<L1Object> list = L1World.getInstance().getVisibleObjects(mapid).values();
					int count = 0;
					for (L1Object tempObj : list) {
						if (tempObj instanceof L1PcInstance) {
							count++;
						}
					}
					if (count >= 50) {
						html = "evsiege103";
					}
					else {
						L1Teleport.teleport(pc, 32766 + _random.nextInt(4), 32831 + _random.nextInt(4), (short) mapid, 5, true);
					}
				}
			}
		}
		return html;
	}


	private String 붉은참모(L1PcInstance pc, String s) {
		String html = "";
		if (pc.getLevel() < 45) {
			html = "evsiege202";
		} else if (s.equalsIgnoreCase("A")) {// 3장 가져옴
			if (!pc.getInventory().checkItem(60392))
				return "evsiege205";
			if (pc.getInventory().checkItem(60392)
					&& pc.getInventory().checkItem(60393)
					&& pc.getInventory().checkItem(60394)
					&& pc.getInventory().checkItem(60395)) {
				pc.getInventory().consumeItem(60392, 1);
				pc.getInventory().consumeItem(60393,pc.getInventory().countItems(60393));
				pc.getInventory().consumeItem(60394,pc.getInventory().countItems(60394));
				pc.getInventory().consumeItem(60395,pc.getInventory().countItems(60395));
				pc.getInventory().storeItem(60396, 3);
				pc.sendPackets(new S_ServerMessage(403, "붉은 기사단의 훈장 (3)"), true);
				html = "evsiege203";
			} else
				html = "evsiege204";
		} else if (s.equalsIgnoreCase("B")) {// 성장 시켜달라
			if (pc.getInventory().checkItem(60396) && pc.getInventory().checkItem(40308, 20000)) {
				pc.getInventory().consumeItem(60396, 1);
				pc.getInventory().consumeItem(40308, 20000);
				int needExp = ExpTable.getNeedExpNextLevel(pc.getLevel());
				int addexp = 0;
				
				if (pc.getLevel() >= 45 && pc.getLevel() <= 64) {
					addexp = (int) (needExp * 0.03);
				} else if (pc.getLevel() >= 65 && pc.getLevel() <= 69) {
					addexp = (int) (needExp * 0.015);
				} else if (pc.getLevel() >= 70 && pc.getLevel() < 75) {
					addexp = (int) (needExp * 0.0075);
				} else if (pc.getLevel() >= 75) {
					addexp = (int) (needExp * 0.00375);
				}
				
				if (addexp != 0) {
					int level = ExpTable.getLevelByExp(pc.getExp() + addexp);
					if (level > Config.MAXLEVEL) {
						pc.sendPackets(new S_SystemMessage("레벨 제한으로 인해  더이상 경험치를 획득할 수 없습니다."), true);
					} else {
						pc.addExp(addexp);
						
						try {
							pc.save();
						} catch (Exception e) {
							
						}
					}
				}
				html = "evsiege206";
			} else
				html = "evsiege207";
		} else if (s.equalsIgnoreCase("C")) {// 보물상자 줘라
			if (pc.getInventory().checkItem(60396) && pc.getInventory().checkItem(40308, 20000)) {
				pc.getInventory().consumeItem(60396, 1);
				pc.getInventory().consumeItem(40308, 20000);
				pc.getInventory().storeItem(60397, 1);
				pc.sendPackets(new S_ServerMessage(403, "붉은 기사단의 보물상자"), true);
				html = "evsiege208";
			} else
				html = "evsiege207";
		}
		return html;
	}

	
	private String 데이빗(L1PcInstance pc, String s) {
		
		String htmlid = "";
		if (s.equalsIgnoreCase("b")) {
			if (pc.getInventory().checkItem(21081) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21081, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21082, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("c")) {
			if (pc.getInventory().checkItem(21082) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21082, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21083, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("d")) {
			if (pc.getInventory().checkItem(21083) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21083, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21084, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("e")) {
			if (pc.getInventory().checkItem(21084) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21084, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21085, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("f")) {
			if (pc.getInventory().checkItem(21085) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21085, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21086, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("g")) {
			if (pc.getInventory().checkItem(21086) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21086, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21087, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("h")) {
			if (pc.getInventory().checkItem(21087) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21087, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21088, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("i")) {
			if (pc.getInventory().checkItem(21088) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21088, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21089, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("j")) {
			if (pc.getInventory().checkItem(21088) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21088, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21090, 1);
				htmlid = "8event4";
			}
		} else if (s.equalsIgnoreCase("k")) {
			if (pc.getInventory().checkItem(21088) && pc.getInventory().checkItem(49031)) {
				pc.getInventory().consumeItem(21088, 1);
				pc.getInventory().consumeItem(49031, 1);
				pc.getInventory().storeItem(21091, 1);
				htmlid = "8event4";
			}
		}
		
		return htmlid;
	}

	private String 리들(L1PcInstance pc, String s) {

		String htmlid = "";
		if (pc.getInventory().consumeItem(60385, 1)) {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 1) {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(9000, 1);
					} else if (pc.isKnight()) {
						pc.getInventory().storeItem(9010, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(9020, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(9030, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(9040, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(9050, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(9060, 1);
					} else if (pc.isWarrior()) {
						pc.getInventory().storeItem(9070, 1);
					}
				} else {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(21207, 1);
					} else if (pc.isKnight() || pc.isWarrior()) {
						pc.getInventory().storeItem(21212, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(21217, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(21222, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(21227, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(21232, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(21237, 1);
					}
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 1) {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(9001, 1);
					} else if (pc.isKnight()) {
						pc.getInventory().storeItem(9011, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(9021, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(9031, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(9041, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(9051, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(9061, 1);
					} else if (pc.isWarrior()) {
						pc.getInventory().storeItem(9071, 1);
					}
				} else {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(21208, 1);
					} else if (pc.isKnight() || pc.isWarrior()) {
						pc.getInventory().storeItem(21213, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(21218, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(21223, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(21228, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(21233, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(21238, 1);
					}
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 1) {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(9002, 1);
					} else if (pc.isKnight()) {
						pc.getInventory().storeItem(9012, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(9022, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(9032, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(9042, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(9052, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(9062, 1);
					} else if (pc.isWarrior()) {
						pc.getInventory().storeItem(9072, 1);
					}
				} else {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(21209, 1);
					} else if (pc.isKnight() || pc.isWarrior()) {
						pc.getInventory().storeItem(21214, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(21219, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(21224, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(21229, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(21234, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(21239, 1);
					}
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 1) {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(9003, 1);
					} else if (pc.isKnight()) {
						pc.getInventory().storeItem(9013, 1);
					} else if (pc.isElf()) {pc.getInventory().storeItem(9023, 1);
					
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(9033, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(9043, 1);
					} else if (pc.isDragonknight()) {
					    pc.getInventory().storeItem(9053, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(9063, 1);
					} else if (pc.isWarrior()) {
						pc.getInventory().storeItem(9073, 1);
					}
				} else {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(21210, 1);
					} else if (pc.isKnight() || pc.isWarrior()) {
						pc.getInventory().storeItem(21215, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(21220, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(21225, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(21230, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(21235, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(21240, 1);
					}
				}
			} else if (s.equalsIgnoreCase("E")) {
				if (pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 1) {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(9004, 1);
					} else if (pc.isKnight()) {
						pc.getInventory().storeItem(9014, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(9024, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(9034, 1);
					} else if (pc.isDarkelf()) {
						pc.getInventory().storeItem(9044, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(9054, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(9064, 1);
					} else if (pc.isWarrior()) {
						pc.getInventory().storeItem(9074, 1);
					}
				} else {
					if (pc.isCrown()) {
						pc.getInventory().storeItem(21211, 1);
					} else if (pc.isKnight() || pc.isWarrior()) {
						pc.getInventory().storeItem(21216, 1);
					} else if (pc.isElf()) {
						pc.getInventory().storeItem(21221, 1);
					} else if (pc.isWizard()) {
						pc.getInventory().storeItem(21226, 1);
					} else if (pc.isDarkelf()) {
					     pc.getInventory().storeItem(21231, 1);
					} else if (pc.isDragonknight()) {
						pc.getInventory().storeItem(21236, 1);
					} else if (pc.isIllusionist()) {
						pc.getInventory().storeItem(21241, 1);
					}
				}
			}
		} else {
			htmlid = "riddle2";
		}
		
		return htmlid;
	}

	private String 네르바(L1PcInstance pc, String s) {
		String htmlid = "";
		if (!pc.getInventory().checkItem(60381) || !pc.getInventory().checkItem(60382)) {
			htmlid = "seirune5";
		} else {
			if (s.equalsIgnoreCase("A")) {
				pc.getQuest().get_step(L1Quest.QUEST_55_Roon);
				pc.getQuest().set_step(L1Quest.QUEST_55_Roon, 1);
				pc.getInventory().consumeItem(60381, 1);
				pc.getInventory().consumeItem(60382, 1);
				pc.getInventory().storeItem(60384, 1);
				
				if (pc.isCrown()) {
					pc.getInventory().storeItem(21207, 1);
				} else if (pc.isKnight() || pc.isWarrior()) {
					pc.getInventory().storeItem(21212, 1);
				} else if (pc.isElf()) {
					pc.getInventory().storeItem(21217, 1);
				} else if (pc.isWizard()) {
					pc.getInventory().storeItem(21222, 1);
				} else if (pc.isDarkelf()) {
					pc.getInventory().storeItem(21227, 1);
				} else if (pc.isDragonknight()) {
					pc.getInventory().storeItem(21232, 1);
				} else if (pc.isIllusionist()) {
					pc.getInventory().storeItem(21237, 1);
				} else if (pc.isFencer()) {
					pc.getInventory().storeItem(20627, 1);
				}
			} else if (s.equalsIgnoreCase("B")) {
				pc.getQuest().get_step(L1Quest.QUEST_55_Roon);
				pc.getQuest().set_step(L1Quest.QUEST_55_Roon, 1);
				pc.getInventory().consumeItem(60381, 1);
				pc.getInventory().consumeItem(60382, 1);
				pc.getInventory().storeItem(60384, 1);
				
				if (pc.isCrown()) {
					pc.getInventory().storeItem(21208, 1);
				} else if (pc.isKnight() || pc.isWarrior()) {
					pc.getInventory().storeItem(21213, 1);
				} else if (pc.isElf()) {
					pc.getInventory().storeItem(21218, 1);
				} else if (pc.isWizard()) {
					pc.getInventory().storeItem(21223, 1);
				} else if (pc.isDarkelf()) {
					pc.getInventory().storeItem(21228, 1);
				} else if (pc.isDragonknight()) {
					pc.getInventory().storeItem(21233, 1);
				} else if (pc.isIllusionist()) {
					pc.getInventory().storeItem(21238, 1);
				} else if (pc.isFencer()) {
					pc.getInventory().storeItem(20628, 1);
				}
			} else if (s.equalsIgnoreCase("C")) {
				pc.getQuest().get_step(L1Quest.QUEST_55_Roon);
				pc.getQuest().set_step(L1Quest.QUEST_55_Roon, 1);
				pc.getInventory().consumeItem(60381, 1);
				pc.getInventory().consumeItem(60382, 1);
				pc.getInventory().storeItem(60384, 1);
				
				if (pc.isCrown()) {
					pc.getInventory().storeItem(21209, 1);
				} else if (pc.isKnight() || pc.isWarrior()) {
					pc.getInventory().storeItem(21214, 1);
				} else if (pc.isElf()) {
					pc.getInventory().storeItem(21219, 1);
				} else if (pc.isWizard()) {
					pc.getInventory().storeItem(21224, 1);
				} else if (pc.isDarkelf()) {
					pc.getInventory().storeItem(21229, 1);
				} else if (pc.isDragonknight()) {
					pc.getInventory().storeItem(21234, 1);
				} else if (pc.isIllusionist()) {
					pc.getInventory().storeItem(21239, 1);
				} else if (pc.isFencer()) {
					pc.getInventory().storeItem(20629, 1);
				}
			} else if (s.equalsIgnoreCase("D")) {
				pc.getQuest().get_step(L1Quest.QUEST_55_Roon);
				pc.getQuest().set_step(L1Quest.QUEST_55_Roon, 1);
				pc.getInventory().consumeItem(60381, 1);
				pc.getInventory().consumeItem(60382, 1);
				pc.getInventory().storeItem(60384, 1);
				
				if (pc.isCrown()) { 
					pc.getInventory().storeItem(21210, 1);
				} else if (pc.isKnight() || pc.isWarrior()) {
					pc.getInventory().storeItem(21215, 1);
				} else if (pc.isElf()) {
					pc.getInventory().storeItem(21220, 1);
				} else if (pc.isWizard()) {
					pc.getInventory().storeItem(21225, 1);
				} else if (pc.isDarkelf()) {
					pc.getInventory().storeItem(21230, 1);
				} else if (pc.isDragonknight()) {
					pc.getInventory().storeItem(21235, 1);
				} else if (pc.isIllusionist()) {
					pc.getInventory().storeItem(21240, 1);
				} else if (pc.isFencer()) {
					pc.getInventory().storeItem(20630, 1);
				}
			} else if (s.equalsIgnoreCase("E")) {
				pc.getQuest().get_step(L1Quest.QUEST_55_Roon);
				pc.getQuest().set_step(L1Quest.QUEST_55_Roon, 1);
				pc.getInventory().consumeItem(60381, 1);
				pc.getInventory().consumeItem(60382, 1);
				pc.getInventory().storeItem(60384, 1);
				
				if (pc.isCrown()) {
					pc.getInventory().storeItem(21211, 1);
				} else if (pc.isKnight() || pc.isWarrior()){
					pc.getInventory().storeItem(21216, 1);
				} else if (pc.isElf()){
					pc.getInventory().storeItem(21221, 1);
				} else if (pc.isWizard()){
					pc.getInventory().storeItem(21226, 1);
				} else if (pc.isDarkelf()){
					pc.getInventory().storeItem(21231, 1);
				} else if (pc.isDragonknight()){
					pc.getInventory().storeItem(21236, 1);
				} else if (pc.isIllusionist()){
					pc.getInventory().storeItem(21241, 1);
				} else if (pc.isFencer()){
					pc.getInventory().storeItem(20631, 1);
				}
			}
		}
		
		/** 엘릭서 업데이트후 알림 패킷 처리 */
		return htmlid;
	}

	private String 책더미(L1PcInstance pc, String s) {
		String htmlid = "";
		if (pc.getQuest().get_step(L1Quest.QUEST_55_Roon) == 1
				|| pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 1
				|| pc.getInventory().checkItem(60381) || pc.getLevel() < 55) {
			pc.sendPackets(new S_SystemMessage("이미 낡은 고서를 소지하고 계시거나 레벨이 부족합니다."));
			htmlid = "oldbook2";
		} else {
			if (s.equalsIgnoreCase("a")) {
				pc.getInventory().storeItem(60381, 1);
			}
		}
		
		return htmlid;
	}

	private String 군터의조언(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
			pc.sendPackets(new S_SkillSound(pc.getId(), 7683));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7683));
			
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.사엘)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.사엘);
			}
			
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.크레이)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.크레이);
			}
			
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.군터의조언)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.군터의조언);
			}
			
			pc.군터 = true;
			pc.addBowDmgup(5);
			pc.addBowHitup(7);
			pc.addHpr(10);
			pc.addMaxHp(100);
			pc.addMaxMp(40);
			pc.getResistance().addMr(15);
			pc.getAbility().addAddedDex((byte) 5);

			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
			pc.sendPackets(new S_SPMR(pc), true);
			pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.군터의조언, 2400 * 1000);

			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc, L1SkillId.DRAGONBLOOD_P, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			S_PacketBox pb = new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, 40);
			pc.sendPackets(pb);
			// Timestamp deleteTime = null;
			// deleteTime = new Timestamp(System.currentTimeMillis() +
			// 2400000);// 40분
			// pc.setpaTime(deleteTime);
			htmlid = "gunterdg2";
		}
		
		return htmlid;
	}

	private String birthday(L1PcInstance pc, L1Object obj, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
			htmlid = "";
			if (pc.getInventory().checkItem(5000136, 1) && pc.getInventory().checkItem(5000123, 1)) {
				L1Teleport.teleport(pc, 32797, 32909, (short) 2006, 6, true);
			} else {
				pc.sendPackets(new S_SystemMessage("이벤트 시간이 종료되어 이동이 불가능 합니다."));
				htmlid = "";
			}
		}

		if (s.equalsIgnoreCase("b")) {
			htmlid = "";
			if (pc.getInventory().checkItem(5000125, 1)
					&& pc.getInventory().checkItem(5000126, 1)
					&& pc.getInventory().checkItem(5000127, 1)
					&& pc.getInventory().checkItem(5000128, 2)
					&& pc.getInventory().checkItem(5000129, 1)
					&& pc.getInventory().checkItem(5000130, 1)) { // 재료
																	// 체크
				pc.getInventory().consumeItem(5000125, 1);
				pc.getInventory().consumeItem(5000126, 1);
				pc.getInventory().consumeItem(5000127, 1);
				pc.getInventory().consumeItem(5000128, 2);
				pc.getInventory().consumeItem(5000129, 1);
				pc.getInventory().consumeItem(5000130, 1); // 아이템 체크
				pc.getInventory().storeItem(5000134, 1); // 메티스의 두번째 선물
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "메티스의 두번째 선물"), true);
				// pc.sendPackets(new S_SystemMessage("메티스의 두번째 선물을 얻었습니다."));
				htmlid = "birthdayb3";
			} else { // 재료가 부족한 경우
				htmlid = "birthdayb2";
			}
		}
		
		return htmlid;
	}

	private String 란달(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("0")){
			htmlid = "randal1";
		} else if (s.equalsIgnoreCase("1")){
			htmlid = "randal2";
		} else if (s.equalsIgnoreCase("2")){
			htmlid = "randal3";
		} else if (s.equalsIgnoreCase("3")){
			htmlid = "randal4";
		} else if (s.equalsIgnoreCase("4")){
			htmlid = "randal5";
		} else if (s.equalsIgnoreCase("5")){
			htmlid = "randal6";
		} else if (s.equalsIgnoreCase("6")){
			htmlid = "randal7";
		}

		if (!htmlid.equalsIgnoreCase("")) {
			pc.란달대화창 = htmlid;
		}
		
		try {
			int i = Integer.parseInt(s, 16) - 9;
			if (pc.란달대화창.equalsIgnoreCase("randal1")) { // 농축 용기
				if (pc.getInventory().checkItem(40308, 1000 * i) && pc.getInventory().checkItem(40014, 3 * i)) {
					pc.getInventory().consumeItem(40308, 1000 * i);
					pc.getInventory().consumeItem(40014, 3 * i);
					pc.getInventory().storeItem(550001, i);
				} else
					htmlid = "randal8";
			} else if (pc.란달대화창.equalsIgnoreCase("randal2")) { // 농축 집중
				if (pc.getInventory().checkItem(40308, 1000 * i) && pc.getInventory().checkItem(40068, 3 * i)) {
					pc.getInventory().consumeItem(40308, 1000 * i);
					pc.getInventory().consumeItem(40068, 3 * i);
					pc.getInventory().storeItem(550002, i);
				} else
					htmlid = "randal8";
			} else if (pc.란달대화창.equalsIgnoreCase("randal3")) { // 농축 지혜
				if (pc.getInventory().checkItem(40308, 1000 * i) && pc.getInventory().checkItem(40016, 3 * i)) {
					pc.getInventory().consumeItem(40308, 1000 * i);
					pc.getInventory().consumeItem(40016, 3 * i);
					pc.getInventory().storeItem(550003, i);
				} else
					htmlid = "randal8";
			} else if (pc.란달대화창.equalsIgnoreCase("randal4")) { // 농축 마력
				if (pc.getInventory().checkItem(40308, 1000 * i) && pc.getInventory().checkItem(40015, 3 * i)) {
					pc.getInventory().consumeItem(40308, 1000 * i);
					pc.getInventory().consumeItem(40015, 3 * i);
					pc.getInventory().storeItem(550004, i);
				} else
					htmlid = "randal8";
			} else if (pc.란달대화창.equalsIgnoreCase("randal5")) { // 농축 속도
				if (pc.getInventory().checkItem(40308, 1000 * i) && pc.getInventory().checkItem(40013, 3 * i)) {
					pc.getInventory().consumeItem(40308, 1000 * i);
					pc.getInventory().consumeItem(40013, 3 * i);
					pc.getInventory().storeItem(550000, i);
				} else
					htmlid = "randal8";
			} else if (pc.란달대화창.equalsIgnoreCase("randal6")) { // 농축 호흡
				if (pc.getInventory().checkItem(40308, 1000 * i) && pc.getInventory().checkItem(40032, 3 * i)) {
					pc.getInventory().consumeItem(40308, 1000 * i);
					pc.getInventory().consumeItem(40032, 3 * i);
					pc.getInventory().storeItem(550005, i);
				} else
					htmlid = "randal8";
			} else if (pc.란달대화창.equalsIgnoreCase("randal7")) { // 농축 변신
				if (pc.getInventory().checkItem(40308, 1000 * i) && pc.getInventory().checkItem(40088, 3 * i)) {
					pc.getInventory().consumeItem(40308, 1000 * i);
					pc.getInventory().consumeItem(40088, 3 * i);
					pc.getInventory().storeItem(550006, i);
				} else
					htmlid = "randal8";
			}
			
			pc.란달대화창 = "";
		} catch (Exception e) {
		}
		
		return htmlid;
	}

	/*
	 * private String 잊혀진섬(L1PcInstance pc, String s) {
	 *
	 * String htmlid = ""; try { int x = 0, y = 0, mapid = 0; if
	 * (s.equalsIgnoreCase("b")) { if (pc.getInventory().consumeItem(40308,
	 * 1000)) { x = 32828; y = 32848; mapid = 70; } else pc.sendPackets(new
	 * S_SystemMessage("아데나가 부족합니다."), true); } if (mapid != 0) {
	 *
	 * int usetime = pc.getgirantime(); int outtime = 60 * 60 * 3; Timestamp
	 * nowday = new Timestamp(System.currentTimeMillis()); L1Location loc = new
	 * L1Location(x, y, mapid).randomLocation(1, true); try { String s1 =
	 * isPC입장가능여부(pc.getgiranday(), outtime, usetime); if (s1.equals("입장가능"))
	 * {// 입장가능 int h = (outtime - usetime) / 60 / 60; if (h < 0) { h = 0; } int
	 * m = (outtime - usetime) / 60 % 60; if (m < 0) { m = 0; } if (h > 0) {
	 * pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));// %시간 %분남았습니다.
	 * // pc.sendPackets(new S_SystemMessage(pc, // "던전 체류 가능시간이 "+h+"시간 "+m+
	 * "분 남았습니다."), true); } else { // pc.sendPackets(new S_SystemMessage(pc, //
	 * "던전 체류 가능시간이 "+m+"분 남았습니다."), true); pc.sendPackets(new
	 * S_ServerMessage(1527, "" + m + ""));// 분 남았다 } L1Teleport.teleport(pc,
	 * loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000); }
	 * else if (s1.equals("불가능")) {// 입장불가능 // pc.sendPackets(new
	 * S_SystemMessage(pc, // "던전 체류 가능 시간 3시간을 모두 사용하셨습니다."), true);
	 * pc.sendPackets(new S_ServerMessage(1522, "3"));// 5시간 모두 // 사용했다. return
	 * ""; } else if (s1.equals("초기화")) {// 초기화 pc.setgirantime(1);
	 * pc.setgiranday(nowday); pc.save(); // pc.sendPackets(new
	 * S_SystemMessage(pc, // "던전 체류 시간이 3시간 남았습니다."), true); pc.sendPackets(new
	 * S_ServerMessage(1526, "3"));// 시간 // 남았다. L1Teleport.teleport(pc,
	 * loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000); } }
	 * catch (Exception e) { e.printStackTrace(); }
	 *
	 * } } catch (Exception e) { } return htmlid; }
	 */

	// ||
	/*private String 기란감옥(L1PcInstance pc, String s) {

		String htmlid = "";
		try {
			int x = 0, y = 0, mapid = 0;
			if (s.equalsIgnoreCase("D_giran")
					|| (s.equalsIgnoreCase("teleport giranD"))) {
				int level = 1;
				if (pc.getLevel() >= level && pc.getLevel() < 99) {
					x = 32806 + _random.nextInt(5);
					y = 32732 + _random.nextInt(3);
					mapid = 53;
				} else
					pc.sendPackets(
							new S_PacketBox(84,
									"\\fQ마법사 멀린: \\f3[입장불가]\\f2[기란 감옥] 입장 가능 레벨은  55레벨입니다."),
							false);
			}
			if (mapid != 0) {
				try {
					if (pc.getLevel() <= 89){
					int usetime = pc.getgirantime();
					int outtime = 60 * 60 * 2;
					Timestamp nowday = new Timestamp(System.currentTimeMillis());
					L1Location loc = new L1Location(x, y, mapid).randomLocation(1,
							true);
					String s1 = isPC입장가능여부(pc.getgiranday(), outtime, usetime);
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m
									+ ""));// %시간
											// %분남았습니다.
						} else {
							pc.sendPackets(new S_ServerMessage(1527, "" + m
									+ ""));// 분
											// 남았다
						}

						L1Teleport.teleport(pc, loc.getX(), loc.getY(),
								(short) loc.getMapId(), 5, true, true, 5000);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "2"));// 5시간
																		// 모두사용했다.
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.setgirantime(1);
						pc.setgiranday(nowday);
						pc.save();
						pc.sendPackets(new S_ServerMessage(1526, "2"));// 시간남았다.
						L1Teleport.teleport(pc, loc.getX(), loc.getY(),
								(short) loc.getMapId(), 5, true, true, 5000);
					}
				  }else if (pc.getLevel() >= 90){
						int usetime = pc.getgirantime();
						int outtime = 60 * 60 * 3;
						Timestamp nowday = new Timestamp(System.currentTimeMillis());
						L1Location loc = new L1Location(x, y, mapid).randomLocation(1,
								true);
						String s1 = isPC입장가능여부(pc.getgiranday(), outtime, usetime);
						if (s1.equals("입장가능")) {// 입장가능
							int h = (outtime - usetime) / 60 / 60;
							if (h < 0) {
								h = 0;
							}
							int m = (outtime - usetime) / 60 % 60;
							if (m < 0) {
								m = 0;
							}
							if (h > 0) {
								pc.sendPackets(new S_ServerMessage(1525, h + "", m
										+ ""));// %시간
												// %분남았습니다.
							} else {
								pc.sendPackets(new S_ServerMessage(1527, "" + m
										+ ""));// 분
												// 남았다
							}

							L1Teleport.teleport(pc, loc.getX(), loc.getY(),
									(short) loc.getMapId(), 5, true, true, 5000);
						} else if (s1.equals("불가능")) {// 입장불가능
							pc.sendPackets(new S_ServerMessage(1522, "3"));// 5시간
																			// 모두사용했다.
							return "";
						} else if (s1.equals("초기화")) {// 초기화
							pc.setgirantime(1);
							pc.setgiranday(nowday);
							pc.save();
							pc.sendPackets(new S_ServerMessage(1526, "3"));// 시간남았다.
							L1Teleport.teleport(pc, loc.getX(), loc.getY(),
									(short) loc.getMapId(), 5, true, true, 5000);
						}
					  }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
		return htmlid;
	}*/

	private String 기란감옥(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("D_giran") || (s.equalsIgnoreCase("teleport giranD"))) {
			L1Teleport.teleport(pc, 32810, 32732, (short) 53, 5, true, true, 5000);
		}
		
		return htmlid;
	}

	private String 지배자결계(L1PcInstance pc, String s) {

		String htmlid = "";
		try {
			int x = 0, y = 0, mapid = 0;
			if (s.equalsIgnoreCase("control")) {
				int level = 82;
				if (pc.getLevel() >= level && pc.getLevel() < 99) {
					x = 32787 + _random.nextInt(2);
					y = 32882 + _random.nextInt(2);
					mapid = 15403;
				} else{
					pc.sendPackets(new S_PacketBox(84, "\\fQ리드: \\f3[입장불가]\\f2[지배의 결계] 입장 가능 레벨은  82레벨입니다."), false);
				}
			}
			
			if (mapid != 0) {
				try {
					if (pc.getLevel() <= 89){
					int usetime = pc.getgirantime();
					int outtime = 60 * 60 * 2;
					Timestamp nowday = new Timestamp(System.currentTimeMillis());
					L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
					String s1 = isPC입장가능여부(pc.getgiranday(), outtime, usetime);
					
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간  %분남았습니다.
						} else {
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
						}
						
						L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "2"));// 5시간 모두사용했다.
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.setgirantime(1);
						pc.setgiranday(nowday);
						pc.save();
						pc.sendPackets(new S_ServerMessage(1526, "2"));// 시간남았다.
						L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
					}
				  } else if (pc.getLevel() >= 90){
						int usetime = pc.getgirantime();
						int outtime = 60 * 60 * 3;
						Timestamp nowday = new Timestamp(System.currentTimeMillis());
						L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
						String s1 = isPC입장가능여부(pc.getgiranday(), outtime, usetime);
						if (s1.equals("입장가능")) {// 입장가능
							int h = (outtime - usetime) / 60 / 60;
							if (h < 0) {
								h = 0;
							}
							
							int m = (outtime - usetime) / 60 % 60;
							if (m < 0) {
								m = 0;
							}
							
							if (h > 0) {
								pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간 %분남았습니다.
							} else {
								pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
							}
							
							L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
						} else if (s1.equals("불가능")) {// 입장불가능
							pc.sendPackets(new S_ServerMessage(1522, "3"));  // 5시간 모두사용했다.
							return "";
						} else if (s1.equals("초기화")) {// 초기화
							pc.setgirantime(1);
							pc.setgiranday(nowday);
							pc.save();
							pc.sendPackets(new S_ServerMessage(1526, "3"));  // 시간남았다.
							L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
						}
					  }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
		
		return htmlid;
	}

	private String 칠흑의수정(L1PcInstance pc, String s) {

		String htmlid = "";
		try {
			if (s.equalsIgnoreCase("D_gludin")) {
				if (pc.getInventory().consumeItem(40308, 4000)) {
					L1Teleport.teleport(pc, 32812, 32725, (short) 807, 2, true);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."), true);
				}
			} else if (s.equalsIgnoreCase("D_gludin2f")) {
				if (pc.getInventory().consumeItem(40308, 5000)) {
					L1Teleport.teleport(pc, 32749, 32799, (short) 808, 2, true);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."), true);
				}
			} else if (s.equalsIgnoreCase("D_gludin3f")) {
				if (pc.getInventory().consumeItem(40308, 6000)) {
					L1Teleport.teleport(pc, 32799, 32754, (short) 809, 2, true);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."), true);
				}
			} else if (s.equalsIgnoreCase("D_gludin4f")) {
				if (pc.getInventory().consumeItem(40308, 7000)) {
					L1Teleport.teleport(pc, 32764, 32775, (short) 810, 2, true);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."), true);
				}
			} else if (s.equalsIgnoreCase("D_gludin5f")) {
				if (pc.getInventory().consumeItem(40308, 10000)) {
					L1Teleport.teleport(pc, 32727, 32726, (short) 811, 2, true);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."), true);
				}
			} else if (s.equalsIgnoreCase("D_gludin6f")) {
				if (pc.getInventory().consumeItem(40308, 15000)) {
					L1Teleport.teleport(pc, 32805, 32726, (short) 812, 2, true);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."), true);
				}
			} else if (s.equalsIgnoreCase("D_gludin7f")) {
				if (pc.getInventory().consumeItem(40308, 20000)) {
					L1Teleport.teleport(pc, 32726, 32727, (short) 813, 2, true);
				} else {
					pc.sendPackets(new S_SystemMessage("아데나가 부족합니다."), true);
				}
			}
		} catch (Exception e) {
		}
		
		return htmlid;
	}

	private String 가웨인(L1PcInstance pc, String s) {

		String htmlid = "";
		short enchant = 0;
		int itemid = 0;
		if (s.equalsIgnoreCase("A")) {
			itemid = 410000;
			enchant = 8;
		} else if (s.equalsIgnoreCase("B")) {
			itemid = 410000;
			enchant = 9;
		} else if (s.equalsIgnoreCase("C")) {
			itemid = 410001;
			enchant = 8;
		} else if (s.equalsIgnoreCase("D")) {
			itemid = 410001;
			enchant = 9;
		}
		if (enchant != 0) {
			L1ItemInstance 재료 = null;
			if (pc.getInventory().checkItem(40308, 5000000 * (1 + enchant - 8))) {
				L1ItemInstance[] list2 = pc.getInventory().findItemsIdNotEquipped(itemid);
				for (L1ItemInstance item : list2) {
					if (item.getEnchantLevel() == enchant) {
						재료 = item;
						break;
					}
				}
			}
			
			if (재료 != null) {
				pc.getInventory().removeItem(재료);
				pc.getInventory().consumeItem(40308, 5000000 * (1 + enchant - 8));
				L1ItemInstance item = pc.getInventory().storeItem(275, 1, enchant - 1);
				pc.sendPackets(new S_ServerMessage(403, item.getName()));
				htmlid = "gawain05";
			} else {
				htmlid = "gawain04";
			}
		}
		
		return htmlid;
	}

	private String 저주받은무녀사엘(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("A")) {
			pc.sendPackets(new S_SkillSound(pc.getId(), 7680));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7680));
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.사엘)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.사엘);
			}
			
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.크레이)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.크레이);
			}
			
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.군터의조언)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.군터의조언);
			}
			
			pc.addMaxHp(100);
			pc.addMaxMp(50);
			pc.addHpr(3);
			pc.addMpr(3);
			pc.getResistance().addWater(30);
			pc.addDmgup(1);
			pc.addBowDmgup(1);
			pc.addHitup(5);
			pc.addBowHitup(5);
			pc.addWeightReduction(40);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			pc.sendPackets(new S_OwnCharStatus(pc));
			pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.사엘, 2400 * 1000);

			L1SkillUse l1skilluse = new L1SkillUse();
			l1skilluse.handleCommands(pc, L1SkillId.DRAGONBLOOD_A, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			S_PacketBox pb = new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, 80);
			pc.sendPackets(pb);
			// Timestamp deleteTime = null;
			// deleteTime = new Timestamp(System.currentTimeMillis() +
			// 2400000);// 40분
			// pc.setAnTime(deleteTime);

			htmlid = "shamansael2";
		}
		
		return htmlid;
	}

	private String 안톤(L1PcInstance pc, String s) {

		String htmlid = "";
		int itemid = 0;
		int 마사인첸 = 7;
		int 고대아이템 = 0;
		if (s.equalsIgnoreCase("A")) {// 0멸마판금
			itemid = 21169;
			고대아이템 = 20095;
		} else if (s.equalsIgnoreCase("B")) {// 1멸마판금
			itemid = 21169;
			마사인첸 = 8;
			고대아이템 = 20095;
		} else if (s.equalsIgnoreCase("C")) {// 2멸마판금
			itemid = 21169;
			마사인첸 = 9;
			고대아이템 = 20095;
		} else if (s.equalsIgnoreCase("D")) {// 3멸마판금
			itemid = 21169;
			마사인첸 = 10;
			고대아이템 = 20095;
		} else if (s.equalsIgnoreCase("E")) {// 0멸마비늘
			itemid = 21170;
			고대아이템 = 20094;
		} else if (s.equalsIgnoreCase("F")) {// 1멸마비늘
			itemid = 21170;
			마사인첸 = 8;
			고대아이템 = 20094;
		} else if (s.equalsIgnoreCase("G")) {// 2멸마비늘
			itemid = 21170;
			마사인첸 = 9;
			고대아이템 = 20094;
		} else if (s.equalsIgnoreCase("H")) {// 3멸마비늘
			itemid = 21170;
			마사인첸 = 10;
			고대아이템 = 20094;
		} else if (s.equalsIgnoreCase("I")) {// 0멸마가죽
			itemid = 21171;
			고대아이템 = 20092;
		} else if (s.equalsIgnoreCase("J")) {// 1멸마가죽
			itemid = 21171;
			마사인첸 = 8;
			고대아이템 = 20092;
		} else if (s.equalsIgnoreCase("K")) {// 2멸마가죽
			itemid = 21171;
			마사인첸 = 9;
			고대아이템 = 20092;
		} else if (s.equalsIgnoreCase("L")) {// 3멸마가죽
			itemid = 21171;
			마사인첸 = 10;
			고대아이템 = 20092;
		} else if (s.equalsIgnoreCase("M")) {// 0멸마로브
			itemid = 21172;
			고대아이템 = 20093;
		} else if (s.equalsIgnoreCase("N")) {// 1멸마로브
			itemid = 21172;
			마사인첸 = 8;
			고대아이템 = 20093;
		} else if (s.equalsIgnoreCase("O")) {// 2멸마로브
			itemid = 21172;
			마사인첸 = 9;
			고대아이템 = 20093;
		} else if (s.equalsIgnoreCase("P")) {// 3멸마로브
			itemid = 21172;
			마사인첸 = 10;
			고대아이템 = 20093;
		}

		boolean ck = false;
		if (s.equalsIgnoreCase("1")) {// 멸마판금
			itemid = 21169;
			if (pc.getInventory().checkItem(49015, 1)) {
				L1ItemInstance[] list1 = pc.getInventory().findItemsIdNotEquipped(20095);
				for (L1ItemInstance item2 : list1) {
					pc.getInventory().consumeItem(49015, 1);
					pc.getInventory().removeItem(item2);
					ck = true;
					break;
				}
			}
		} else if (s.equalsIgnoreCase("2")) {// 멸마비늘
			itemid = 21170;
			if (pc.getInventory().checkItem(49015, 1)) {
				L1ItemInstance[] list1 = pc.getInventory().findItemsIdNotEquipped(20094);
				for (L1ItemInstance item2 : list1) {
					pc.getInventory().consumeItem(49015, 1);
					pc.getInventory().removeItem(item2);
					ck = true;
					break;
				}
			}
		} else if (s.equalsIgnoreCase("3")) {// 멸마가죽
			itemid = 21171;
			if (pc.getInventory().checkItem(49015, 1)) {
				L1ItemInstance[] list1 = pc.getInventory().findItemsIdNotEquipped(20092);
				for (L1ItemInstance item2 : list1) {
					pc.getInventory().consumeItem(49015, 1);
					pc.getInventory().removeItem(item2);
					ck = true;
					break;
				}
			}
		} else if (s.equalsIgnoreCase("4")) {// 멸마로브
			itemid = 21172;
			if (pc.getInventory().checkItem(49015, 1)) {
				L1ItemInstance[] list1 = pc.getInventory().findItemsIdNotEquipped(20093);
				for (L1ItemInstance item2 : list1) {
					pc.getInventory().consumeItem(49015, 1);
					pc.getInventory().removeItem(item2);
					ck = true;
					break;
				}
			}
		} else {
			if (고대아이템 != 0) {
				if (pc.getInventory().checkItem(41246, 100000)) {
					L1ItemInstance[] list1 = pc.getInventory().findItemsIdNotEquipped(고대아이템);
					if (list1.length > 0) {
						L1ItemInstance 마사 = null;
						L1ItemInstance[] list2 = pc.getInventory().findItemsIdNotEquipped(20110);
						
						for (L1ItemInstance item : list2) {
							if (item.getEnchantLevel() == 마사인첸) {
								마사 = item;
								break;
							}
						}
						
						if (마사 == null) {
							list2 = pc.getInventory().findItemsIdNotEquipped(
									1020110);
							for (L1ItemInstance item : list2) {
								if (item.getEnchantLevel() == 마사인첸) {
									마사 = item;
									break;
								}
							}
						}
						
						if (마사 != null) {
							pc.getInventory().removeItem(마사);
							for (L1ItemInstance item2 : list1) {
								pc.getInventory().removeItem(item2);
								pc.getInventory().consumeItem(41246, 100000);
								ck = true;
								break;
							}
						}
					}
				}
			}
		}
		
		if (ck && itemid != 0) {
			L1ItemInstance item = pc.getInventory().storeItem(itemid, 1, 마사인첸 - 7);
			pc.sendPackets(new S_ServerMessage(403, item.getName()));
		} else {
			htmlid = "anton9";
		}
		
		return htmlid;
	}

	private String 휴그린트(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("0")) {
			if (pc.getInventory().checkItem(500018, 1)) {
				htmlid = "hugrint4";
			} else if (pc.getInventory().checkItem(40308, 1000)) {
				pc.getInventory().consumeItem(40308, 1000);
				pc.getInventory().storeItem(500018, 1);
				pc.sendPackets(new S_ServerMessage(403, "$9164")); // 7
				htmlid = "hugrint2";
			} else {
				htmlid = "hugrint3";
			}
		} else if (s.equalsIgnoreCase("1")) {
			if (pc.getInventory().checkItem(500033, 5)) {
				pc.getInventory().consumeItem(500033, 5);
				pc.getInventory().storeItem(500019, 1);
				pc.sendPackets(new S_ServerMessage(403, "$9165")); // 7
				htmlid = "";
			} else {
				htmlid = "hugrint6";
			}
		}
		return htmlid;
	}

	private String 픽시상자교환원(L1PcInstance pc, String s) {

		String htmlid = "";
		Timestamp nowday = new Timestamp(System.currentTimeMillis());
		try {
			/*
			 * jigur4 - pc방 있는 분들에게만 교환해줌
			 */
			// 빛나는 픽시 상자를 주세요.
			if (pc.getLevel() < 45)
				return "jigur5";

			if (s.equalsIgnoreCase("D") || s.equalsIgnoreCase("G")) {
				if (pc.getInventory().checkItem(60319)) {
					htmlid = "jigur3";
				} else {
					int count = pc.getInventory().countItems(60321);
					if (count >= 30) {
						pc.getInventory().consumeItem(60321, count);
						pc.getInventory().storeItem(60319, 1);
						pc.getInventory().storeItem(60320, 1);
						htmlid = "jigur6";
					} else
						htmlid = "jigur7";
				}
				// 1구역 이동
			} else if (s.equalsIgnoreCase("A")) {
				Random random = new Random(System.nanoTime());
				int gn1 = random.nextInt(3);
				try {
					int outtime = 60 * 60;
					int usetime = pc.get수상한천상계곡time();
					String s1 = isPC입장가능여부(pc.get수상한천상계곡day(), outtime, usetime);
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간 %분남았습니다.
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						} else {
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));   // 분 남았다
						}
						L1Teleport.teleport(pc, 32858 + gn1, 32860 + gn1, (short) 1913, 5, true, true, 5000);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "1"));   // 5시간 모두 사용했다.
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.set수상한천상계곡time(1);
						pc.set수상한천상계곡day(nowday);
						pc.save();
						pc.sendPackets(new S_ServerMessage(1526, "1"));   // 시간  남았다.
						L1Teleport.teleport(pc, 32858 + gn1, 32860 + gn1, (short) 1913, 5, true, true, 5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 2구역 이동
			} else if (s.equalsIgnoreCase("B")) {
				Random random = new Random(System.nanoTime());
				int gn1 = random.nextInt(3);
				
				try {
					int outtime = 60 * 60;
					int usetime = pc.get수상한천상계곡time();
					String s1 = isPC입장가능여부(pc.get수상한천상계곡day(), outtime, usetime);
					
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간 %분남았습니다.
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						} else {
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
						}
						L1Teleport.teleport(pc, 32858 + gn1, 32860 + gn1, (short) 1911, 5, true, true, 5000);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "1"));  // 5시간 모두 사용했다.
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.set수상한천상계곡time(1);
						pc.set수상한천상계곡day(nowday);
						pc.save();
						pc.sendPackets(new S_ServerMessage(1526, "1"));  // 시간 남았다.
						L1Teleport.teleport(pc, 32858 + gn1, 32860 + gn1, (short) 1911, 5, true, true, 5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 3구역 이동
			} else if (s.equalsIgnoreCase("C")) {
				Random random = new Random(System.nanoTime());
				int gn1 = random.nextInt(3);
				
				try {
					int outtime = 60 * 60;
					int usetime = pc.get수상한천상계곡time();
					String s1 = isPC입장가능여부(pc.get수상한천상계곡day(), outtime, usetime);
					
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간 %분남았습니다.
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						} else {
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
						}
						L1Teleport.teleport(pc, 32858 + gn1, 32860 + gn1, (short) 1912, 5, true, true, 5000);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "1"));  // 5시간 모두 사용했다.
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.set수상한천상계곡time(1);
						pc.set수상한천상계곡day(nowday);
						pc.save();
						pc.sendPackets(new S_ServerMessage(1526, "1"));// 시간 남았다
						L1Teleport.teleport(pc, 32858 + gn1, 32860 + gn1, (short) 1912, 5, true, true, 5000);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		} catch (Exception e) {
		}
		
		return htmlid;
	}

	private String npc80105(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("c")) {
			if (pc.isCrown()) {
				if (pc.getInventory().checkItem(20383, 1)) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100000)) {
						L1ItemInstance item = pc.getInventory().findItemId(20383);
						if (item != null && item.getRemainingTime() != 180000) {
							item.setRemainingTime(180000);
							pc.getInventory().updateItem(item, L1PcInventory.COL_REMAINING_TIME);
							pc.getInventory().consumeItem(L1ItemId.ADENA, 100000);
							htmlid = "";
						}
					} else {
						pc.sendPackets(new S_ServerMessage(337, "$4"));
					}
				}
			}
		}
		
		return htmlid;
	}

	private String 다엘생존자(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("exitghost") && pc.isGhost()) {
			pc.makeReadyEndGhost();
			pc.endGhost();
		}
		return htmlid;
	}

	private String 카너스(L1PcInstance pc, String s) {

		String htmlid = "";
		try {
			if (s.equalsIgnoreCase("a")) { // 기르타스 전방
				L1Teleport.teleport(pc, 32853 + _random.nextInt(3), 32861 + _random.nextInt(3), (short) 537, 5, true);
			} else if (s.equalsIgnoreCase("b")) { // 전초기지로 이동
				L1Teleport.teleport(pc, 32806 + _random.nextInt(3), 32864 + _random.nextInt(3), (short) 537, 5, true);
			} else if (s.equalsIgnoreCase("d")) { // 전투 지역을 확인
				if (pc.getInventory().consumeItem(40308, 5000)) {
					pc.save();

					L1Location loc = new L1Location();
					loc.set(32854, 32862, 537);
					loc = loc.randomLocation(5, false);
					pc.beginGhost(loc.getX(), loc.getY(), (short) loc.getMapId(), true, 60 * 5);
				}
			}
		} catch (Exception e) {
		}
		
		return htmlid;
	}

	private String 훈련기지나가기(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
			L1Teleport.teleport(pc, 32611 + _random.nextInt(3), 33194 + _random.nextInt(3), (short) 4, 5, true);
		}
		
		return htmlid;
	}

	private String 훈련기지이동(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
			L1Teleport.teleport(pc, 32616 + _random.nextInt(3), 33214 + _random.nextInt(3), (short) 4, 5, true);
		}
		
		return htmlid;
	}

	private String 니디스(String s, L1PcInstance pc, L1Object obj) {
		String htmlid = "";
		if (s.equalsIgnoreCase("0")) {
			if (pc.getInventory().checkItem(5000038, 5)) { // 마력의 결정 ( 아데나로 하셔도됨)
				pc.getInventory().consumeItem(5000038, 5); // 마력의 결정
				pc.getInventory().storeItem(500144, 1); // 원거리 인형 지급
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "눈사람 인형(A)"), true);
				htmlid = "kmas_nidis2";
			} else { // 재료가 부족한 경우
				pc.sendPackets(new S_SystemMessage("마력의 결정이 부족합니다."));
				htmlid = "kmas_nidis3";
			}
		}
		
		if (s.equalsIgnoreCase("1")) {
			if (pc.getInventory().checkItem(5000038, 5)) { // 마력의 결정
				pc.getInventory().consumeItem(5000038, 5); // 마력의 결정
				pc.getInventory().storeItem(500145, 1); // 마력 회복 인형 지급
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "눈사람 인형(B)"), true);
				htmlid = "kmas_nidis2";
			} else { // 재료가 부족한 경우
				pc.sendPackets(new S_SystemMessage("마력의 결정이 부족합니다."));
				htmlid = "kmas_nidis3";
			}
		}
		
		if (s.equalsIgnoreCase("2")) {
			if (pc.getInventory().checkItem(5000038, 5)) { // 마력의 결정
				pc.getInventory().consumeItem(5000038, 5); // 마력의 결정
				pc.getInventory().storeItem(500146, 1); // 체력 회복 인형 지급
				pc.sendPackets(new S_ServerMessage(143, ((L1NpcInstance) obj).getName(), "눈사람 인형(C)"), true);
				htmlid = "kmas_nidis2";
			} else { // 재료가 부족한 경우
				pc.sendPackets(new S_SystemMessage("마력의 결정이 부족합니다."));
				htmlid = "kmas_nidis3";
			}
		}
		
		return htmlid;
	}

	private String 메티스버프(L1PcInstance pc, String s) {
		if (s.equalsIgnoreCase("a")) {
			
			int[] allBuffSkill = { PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR,
					 L1SkillId.miso1, L1SkillId.miso2, L1SkillId.miso3, L1SkillId.BLESS_WEAPON};
			
			pc.setBuffnoch(1);
			L1SkillUse l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}
		}
		
		return "";
	}

	private String 메티스마음(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) { // 근거리 버프
              
				int[] allBuffSkill = { PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR,
						 L1SkillId.miso1, L1SkillId.miso2, L1SkillId.miso3, L1SkillId.BLESS_WEAPON, L1SkillId.SHIELD};
				
				pc.setBuffnoch(1); // 추가적으로 버프는 미작동
				L1SkillUse l1skilluse = new L1SkillUse();
				for (int i = 0; i < allBuffSkill.length; i++) {
					l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
				}
			}
			
		return htmlid;
		}

	private String 해상전순위(L1PcInstance pc, String s) {

		if (s.equalsIgnoreCase("query")) {
			pc.sendPackets(new S_NavarWarfare_Ranking(0, 0), true);
		}
		
		return null;
	}

	private String 탐사원조수(L1PcInstance pc, String s) {

		if (s.equalsIgnoreCase("B")) {
			L1Teleport.teleport(pc, 32842 + _random.nextInt(5), 32692 + _random.nextInt(5), (short) 550, 5, true);
		}
		
		return "";
	}

	private String enterseller(L1PcInstance pc) {

		String htmlid = "";
		if (pc.getMapId() == 800) {
			try {
				String name = readS();
				if (name == null) {
					return htmlid;
				}
				
				Random rnd = new Random(System.nanoTime());
				L1PcInstance pn = L1World.getInstance().getPlayer(name);
				if (pn != null && pn.getMapId() == 800 && pn.isPrivateShop()) {
					pc.dx = pn.getX() + rnd.nextInt(3) - 1;
					pc.dy = pn.getY() + rnd.nextInt(3) - 1;
					pc.dm = (short) pn.getMapId();
					pc.dh = calcheading(pc.dx, pc.dy, pn.getX(), pn.getY());
					pc.상인찾기Objid = pn.getId();
					pc.setTelType(7);
					pc.sendPackets(new S_SabuTell(pc), true);
				} else {
					L1NpcShopInstance nn = L1World.getInstance().getNpcShop(name);
					if (nn != null && nn.getMapId() == 800 && nn.getState() == 1) {
						pc.dx = nn.getX() + rnd.nextInt(3) - 1;
						pc.dy = nn.getY() + rnd.nextInt(3) - 1;
						pc.dm = (short) nn.getMapId();
						pc.dh = calcheading(pc.dx, pc.dy, nn.getX(), nn.getY());
						pc.상인찾기Objid = nn.getId();
						pc.setTelType(7);
						pc.sendPackets(new S_SabuTell(pc), true);
					} else {
						pc.sendPackets(new S_SystemMessage("\\fY찾으시는 상인이 없습니다."), true);
					}
				}
				
				rnd = null;
			} catch (Exception e) {
				return htmlid;
			}
		}
		
		// S_SystemMessage myn = new S_SystemMessage("\\fY찾는 상인의 이름은?");
		// pc.sendPackets(myn); myn.clear(); myn = null;
		
		return htmlid;
	}

	private String 알드란(L1PcInstance pc, String s) {

		String htmlid = "";
		if (!pc.getInventory().consumeItem(41207, 1)) {
			return "aldran9";
		}
		
		if (s.equalsIgnoreCase("a")) {
			L1Teleport.teleport(pc, 32669 + _random.nextInt(4), 32866 + _random.nextInt(7), (short) 550, 5, true);
		} else if (s.equalsIgnoreCase("b")) {
			L1Teleport.teleport(pc, 32777 + _random.nextInt(4), 33007 + _random.nextInt(7), (short) 550, 5, true);
		} else if (s.equalsIgnoreCase("c")) {
			L1Teleport.teleport(pc, 32468 + _random.nextInt(4), 32763 + _random.nextInt(7), (short) 550, 5, true);
		} else if (s.equalsIgnoreCase("d")) {
			L1Teleport.teleport(pc, 32508 + _random.nextInt(4), 32996 + _random.nextInt(7), (short) 550, 5, true);
		} else if (s.equalsIgnoreCase("e")) {
			L1Teleport.teleport(pc, 33011 + _random.nextInt(4), 33011 + _random.nextInt(7), (short) 558, 5, true);
		}
		
		return htmlid;
	}

	private String 세실리아(L1PcInstance pc, String s) {

		String htmlid = "";
		int monid = 0;
		if (s.equals("A")) {// 하딘의분신
			if (pc.getInventory().consumeItem(60286, 1)) {
				monid = 7200185;
			}
		} else if (s.equals("B")) {// 흑마법사
			if (pc.getInventory().consumeItem(60287, 1)) {
				monid = 7140179;
			}
		} else if (s.equals("C")) {// 데몬
			if (pc.getInventory().consumeItem(60288, 1)) {
				monid = 45649;
			}
		} else if (s.equals("D")) {// 타락
			if (pc.getInventory().consumeItem(60289, 1)) {
				monid = 45685;
			}
		} else if (s.equals("E")) {// 케이나
			if (pc.getInventory().consumeItem(60294, 1)) {
				monid = 45955;
			}
		} else if (s.equals("F")) {// 이데아
			if (pc.getInventory().consumeItem(60295, 1)) {
				monid = 45959;
			}
		} else if (s.equals("G")) {// 비아타스
			if (pc.getInventory().consumeItem(60296, 1)) {
				monid = 45956;
			}
		} else if (s.equals("H")) {// 바로메스
			if (pc.getInventory().consumeItem(60297, 1)) {
				monid = 45957;
			}
		} else if (s.equals("I")) {// 티아메스
			if (pc.getInventory().consumeItem(60298, 1)) {
				monid = 45960;
			}
		} else if (s.equals("J")) {// 엔디아스
			if (pc.getInventory().consumeItem(60299, 1)) {
				monid = 45958;
			}
		} else if (s.equals("K")) {// 라미아스
			if (pc.getInventory().consumeItem(60300, 1)) {
				monid = 45961;
			}
		} else if (s.equals("L")) {// 바로드
			if (pc.getInventory().consumeItem(60301, 1)) {
				monid = 45962;
			}
		} else if (s.equals("M")) {// 헬바인
			if (pc.getInventory().consumeItem(60302, 1)) {
				monid = 45676;
			}
		} else if (s.equals("N")) {// 라이아
			if (pc.getInventory().consumeItem(60303, 1)) {
				monid = 45863;
			}
		} else if (s.equals("O")) {// 바란카
			if (pc.getInventory().consumeItem(60304, 1)) {
				monid = 45844;
			}
		} else if (s.equals("P")) {// 슬레이브
			if (pc.getInventory().consumeItem(60305, 1)) {
				monid = 45648;
			}
		} else if (s.equals("Q")) {// 네크로맨서
			if (pc.getInventory().consumeItem(60336, 1)) {
				monid = 45456;
			}
		} else if (s.equals("S")) {// 데스나이트
			if (pc.getInventory().consumeItem(60337, 1)) {
				monid = 45601;
			}
		} else if (s.equals("T")) {// 왜곡의 제니스 퀸
			if (pc.getInventory().consumeItem(60461, 1)) {
				monid = 45513;
			}
		} else if (s.equals("U")) {// 불신의 시어
			if (pc.getInventory().consumeItem(60462, 1)) {
				monid = 45547;
			}
		} else if (s.equals("V")) {// 공포의 뱀파이어
			if (pc.getInventory().consumeItem(60463, 1)) {
				monid = 45606;
			}
		} else if (s.equals("W")) {// 죽음의 좀비 로드
			if (pc.getInventory().consumeItem(60464, 1)) {
				monid = 45650;
			}
		} else if (s.equals("X")) {// 지옥의 쿠거
			if (pc.getInventory().consumeItem(60465, 1)) {
				monid = 45652;
			}
		} else if (s.equals("Y")) {// 불사의 머미 로드
			if (pc.getInventory().consumeItem(60466, 1)) {
				monid = 45653;
			}
		} else if (s.equals("Z")) {// 냉혹의 아이리스
			if (pc.getInventory().consumeItem(60467, 1)) {
				monid = 45654;
			}
		} else if (s.equals("a")) {// 어둠의 나이트발드
			if (pc.getInventory().consumeItem(60468, 1)) {
				monid = 45618;
			}
		} else if (s.equals("b")) {// 불멸의 리치
			if (pc.getInventory().consumeItem(60469, 1)) {
				monid = 45672;
			}
		} else if (s.equals("c")) {// 사신 그림 리퍼
			if (pc.getInventory().consumeItem(60470, 1)) {
				monid = 81047;
			}
		} else if (s.equals("d")) {// 흑기사대장
			if (pc.getInventory().consumeItem(7243, 1)) {
				monid = 45600;
			}
		}
		
		// 영혼석없으면
		if (monid == 0) {
			htmlid = "bosskey10";
		} else {
			L1NpcInstance npc = L1SpawnUtil.spawn2(32878, 32816, (short) pc.getMapId(), monid, 0, 0, 0);
			
			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc.getMapId()).values()) {
				if (obj != null && obj instanceof L1PcInstance) {
					L1PcInstance temp = (L1PcInstance) obj;
					temp.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, npc.getNameId() + "이 나타났습니다."), true);
				}
			}
			
		}
		
		return htmlid;
	}

	private String 카이저(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("1")) { // 대여
			if (TraningCenter.get().mapCheck() == null) {
				htmlid = "bosskey3";
			} else {
				htmlid = "bosskey4";
			}
		} else if (s.equalsIgnoreCase("2") || s.equalsIgnoreCase("3") || s.equalsIgnoreCase("4")) { // 4, 8, 16개
			int adena = 1200;
			int count = 4;
			if (s.equalsIgnoreCase("3")) {
				adena = 2400;
				count = 8;
			} else if (s.equalsIgnoreCase("4")) {
				adena = 4800;
				count = 16;
			}

			if (pc.getInventory().checkItem(60285)) {
				htmlid = "bosskey6";
			} else if (TraningCenter.get().mapCheck() == null) {
				htmlid = "bosskey3";
			} else if (pc.getInventory().consumeItem(40308, adena)) {
				L1Map map = TraningCenter.get().start();
				if (map == null) {
					htmlid = "bosskey3";
				} else {
					L1ItemInstance item = null;
					for (int i = 0; i < count; i++) {
						item = ItemTable.getInstance().createItem(60285);
						item.setCount(1);
						item.setKey(map.getId());
						item.setIdentified(true);
						item.setEndTime(new Timestamp(System.currentTimeMillis() + TraningCenter.time));
						pc.getInventory().storeItem(item);
					}
					
					pc.sendPackets(new S_ServerMessage(403, item.getName() + " (" + count + ")"));
					htmlid = "bosskey7";
				}
			} else {
				htmlid = "bosskey5";
			}
		} else if (s.equalsIgnoreCase("6")) { // 입장
			L1ItemInstance item = pc.getInventory().findItemId(60285);
			if (item == null) {
				htmlid = "bosskey2";
			} else {
				L1Teleport.teleport(pc, 32898 + _random.nextInt(4), 32815 + _random.nextInt(7), (short) item.getKey(), 5, true);
			}
		}
		
		return htmlid;
	}

	private String 미아네스(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("1")) {
			htmlid = "bosskey12";// 영혼석 복구 대상 아님
		}
		
		return htmlid;
	}

	private String 비자야(L1PcInstance pc, String s) {

		String htmlid = "vjaya04";
		int enc = 0;
		
		try {
			if (s.equalsIgnoreCase("A")) {
				if (pc.getInventory().checkItem(40308, 5000000)) {
					L1ItemInstance[] item = pc.getInventory().findItemsIdNotEquipped(410003);
					for (L1ItemInstance temp : item) {
						if (temp.getEnchantLevel() == 8) {
							pc.getInventory().removeItem(temp);
							pc.getInventory().consumeItem(40308, 5000000);
							enc = 7;
							return "vjaya05";
						}
					}
				}
			} else if (s.equalsIgnoreCase("B")) {
				if (pc.getInventory().checkItem(40308, 10000000)) {
					L1ItemInstance[] item = pc.getInventory().findItemsIdNotEquipped(410003);
					for (L1ItemInstance temp : item) {
						if (temp.getEnchantLevel() == 9) {
							pc.getInventory().removeItem(temp);
							pc.getInventory().consumeItem(40308, 10000000);
							enc = 8;
							return "vjaya05";
						}
					}
				}
			} else if (s.equalsIgnoreCase("C")) {
				if (pc.getInventory().checkItem(40308, 5000000)) {
					L1ItemInstance[] item = pc.getInventory().findItemsIdNotEquipped(410004);
					for (L1ItemInstance temp : item) {
						if (temp.getEnchantLevel() == 8) {
							pc.getInventory().removeItem(temp);
							pc.getInventory().consumeItem(40308, 5000000);
							enc = 7;
							return "vjaya05";
						}
					}
				}
			} else if (s.equalsIgnoreCase("D")) {
				if (pc.getInventory().checkItem(40308, 10000000)) {
					L1ItemInstance[] item = pc.getInventory().findItemsIdNotEquipped(410004);
					for (L1ItemInstance temp : item) {
						if (temp.getEnchantLevel() == 9) {
							pc.getInventory().removeItem(temp);
							pc.getInventory().consumeItem(40308, 10000000);
							enc = 8;
							return "vjaya05";
						}
					}
				}
			}
		} catch (Exception e) {
			
		} finally {
			if (enc != 0) {
				L1ItemInstance key = ItemTable.getInstance().createItem(266);
				key.setCount(1);
				key.setEnchantLevel(enc);
				key.setIdentified(true);
				pc.getInventory().storeItem(key);
				pc.sendPackets(new S_ServerMessage(143, "+" + key.getEnchantLevel() + " " + key.getName()), true);
			} else {
				htmlid = "vjaya04";
			}
		}
		
		return htmlid;
	}

	private String 도비와(L1PcInstance pc, String s) {

		String htmlid = "";
		int count = 1;
		int itemid = 0;
		if (s.equalsIgnoreCase("0")) {
			if (pc.getInventory().consumeItem(60234, 1)) {
				itemid = 60235;
			}
		} else if (s.equalsIgnoreCase("1")) {
			if (pc.getInventory().consumeItem(60234, 2)) {
				itemid = 60236;
			}
		} else if (s.equalsIgnoreCase("2")) {
			if (pc.getInventory().consumeItem(60234, 3)) {
				itemid = 60237;
			}
		} else if (s.equalsIgnoreCase("3")) {
			if (pc.getInventory().consumeItem(60234, 4)) {
				itemid = 60238;
			}
		} else if (s.equalsIgnoreCase("4")) {
			if (pc.getInventory().consumeItem(60234, 5)) {
				itemid = 60239;
			}
		} else if (s.equalsIgnoreCase("5")) {
			if (pc.getInventory().consumeItem(60234, 7)) {
				itemid = 60240;
			}
		} else if (s.equalsIgnoreCase("6")) {
			if (pc.getInventory().consumeItem(60234, 9)) {
				itemid = 60241;
			}
		} else if (s.equalsIgnoreCase("7")) {
			if (pc.getInventory().consumeItem(60234, 11)) {
				itemid = 60242;
			}
		} else if (s.equalsIgnoreCase("8")) {
			if (pc.getInventory().consumeItem(60234, 13)) {
				itemid = 60243;
			}
		} else if (s.equalsIgnoreCase("9")) {
			if (pc.getInventory().consumeItem(60234, 15)) {
				itemid = 60244;
			}
		} else if (s.equalsIgnoreCase("A")) {
			if (pc.getInventory().consumeItem(60234, 17)) {
				itemid = 60245;
			}
		} else if (s.equalsIgnoreCase("N")) {
			if (pc.getInventory().consumeItem(60234, 19)) {
				itemid = 60246;
			}
		} else if (s.equalsIgnoreCase("O")) {
			if (pc.getInventory().consumeItem(60235, 1)) {
				itemid = 60234;
			}
		} else if (s.equalsIgnoreCase("P")) {
			if (pc.getInventory().consumeItem(60236, 1)) {
				itemid = 60234;
				count = 2;
			}
		} else if (s.equalsIgnoreCase("Q")) {
			if (pc.getInventory().consumeItem(60237, 1)) {
				itemid = 60234;
				count = 3;
			}
		} else if (s.equalsIgnoreCase("R")) {
			if (pc.getInventory().consumeItem(60238, 1)) {
				itemid = 60234;
				count = 4;
			}
		} else if (s.equalsIgnoreCase("S")) {
			if (pc.getInventory().consumeItem(60239, 1)) {
				itemid = 60234;
				count = 5;
			}
		} else if (s.equalsIgnoreCase("T")) {
			if (pc.getInventory().consumeItem(60240, 1)) {
				itemid = 60234;
				count = 7;
			}
		} else if (s.equalsIgnoreCase("U")) {
			if (pc.getInventory().consumeItem(60241, 1)) {
				itemid = 60234;
				count = 9;
			}
		} else if (s.equalsIgnoreCase("V")) {
			if (pc.getInventory().consumeItem(60242, 1)) {
				itemid = 60234;
				count = 11;
			}
		} else if (s.equalsIgnoreCase("W")) {
			if (pc.getInventory().consumeItem(60243, 1)) {
				itemid = 60234;
				count = 13;
			}
		} else if (s.equalsIgnoreCase("X")) {
			if (pc.getInventory().consumeItem(60244, 1)) {
				itemid = 60234;
				count = 15;
			}
		} else if (s.equalsIgnoreCase("Y")) {
			if (pc.getInventory().consumeItem(60245, 1)) {
				itemid = 60234;
				count = 17;
			}
		} else if (s.equalsIgnoreCase("Z")) {
			if (pc.getInventory().consumeItem(60246, 1)) {
				itemid = 60234;
				count = 19;
			}
		}
		
		if (itemid != 0) {
			L1ItemInstance item = pc.getInventory().storeItem(itemid, count);
			pc.sendPackets(new S_ServerMessage(403, item.getName() + (count > 1 ? "( " + count + ")" : "")));
		} else {
			htmlid = "paosy102";
		}
		
		return htmlid;
	}

	private String 페오시(L1PcInstance pc, String s) {

		String htmlid = "";
		if (pc.getInventory().checkItem(60284, 1)) {
			if (s.equalsIgnoreCase("0")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60273, 1);
			} else if (s.equalsIgnoreCase("1")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60274, 1);
			} else if (s.equalsIgnoreCase("2")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60275, 1);
			} else if (s.equalsIgnoreCase("3")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60276, 1);
			} else if (s.equalsIgnoreCase("4")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60277, 1);
			} else if (s.equalsIgnoreCase("5")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60278, 1);
			} else if (s.equalsIgnoreCase("6")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60279, 1);
			} else if (s.equalsIgnoreCase("7")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60280, 1);
			} else if (s.equalsIgnoreCase("8")) {
				pc.getInventory().consumeItem(60284, 1);
				pc.getInventory().storeItem(60281, 1);
			}
		} else {
			htmlid = "paosy02";
		}
		
		return htmlid;
	}

	private String maetnob(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("J")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(60200, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(60200, 2);
				L1Teleport.teleport(pc, 33766, 32863, (short) 106, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("A")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40104, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40104, 2);
				L1Teleport.teleport(pc, 32766, 32862, (short) 116, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("B")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40105, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40105, 2);
				L1Teleport.teleport(pc, 32766, 32862, (short) 126, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("C")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40106, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40106, 2);
				L1Teleport.teleport(pc, 32766, 32862, (short) 136, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("D")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40107, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40107, 2);
				L1Teleport.teleport(pc, 32766, 32862, (short) 146, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("E")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40108, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40108, 2);
				L1Teleport.teleport(pc, 32753, 32796, (short) 156, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("F")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40109, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40109, 2);
				L1Teleport.teleport(pc, 32753, 32796, (short) 166, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("G")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40110, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40110, 2);
				L1Teleport.teleport(pc, 32753, 32796, (short) 176, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("H")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40111, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40111, 2);
				L1Teleport.teleport(pc, 32753, 32796, (short) 186, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		} else if (s.equalsIgnoreCase("I")) {
			if (pc.getInventory().checkItem(40308, 300) && pc.getInventory().checkItem(40112, 2)) {
				pc.getInventory().consumeItem(40308, 300);
				pc.getInventory().consumeItem(40112, 2);
				L1Teleport.teleport(pc, 32753, 32796, (short) 196, 5, true);
				htmlid = "";
			} else {
				htmlid = "maetnob2";
			}
		}
		
		return htmlid;
	}

	private String 카미라(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
			htmlid = "kamyla7";
			pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 1);
		} else if (s.equalsIgnoreCase("c")) {
			htmlid = "kamyla10";
			pc.getInventory().consumeItem(40644, 1);
			pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 3);
		} else if (s.equalsIgnoreCase("e")) {
			htmlid = "kamyla13";
			pc.getInventory().consumeItem(40630, 1);
			pc.getQuest().set_step(L1Quest.QUEST_KAMYLA, 4);
		} else if (s.equalsIgnoreCase("i")) {
			htmlid = "kamyla25";
		} else if (s.equalsIgnoreCase("b")) { // 카 미라(흐랑코의 미궁)
			if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 1) {
				L1Teleport.teleport(pc, 32679, 32742, (short) 482, 5, true);
			}
		} else if (s.equalsIgnoreCase("d")) { // 카 미라(디에고가 닫힌 뇌)
			if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 3) {
				L1Teleport.teleport(pc, 32736, 32800, (short) 483, 5, true);
			}
		} else if (s.equalsIgnoreCase("f")) { // 카 미라(호세 지하소굴)
			if (pc.getQuest().get_step(L1Quest.QUEST_KAMYLA) == 4) {
				L1Teleport.teleport(pc, 32746, 32807, (short) 484, 5, true);
			}
		}
		
		return htmlid;
	}

	private String 제이프(L1PcInstance pc, String s) {

		String htmlid = "";
		// 49026 고대의 금화
		if (s.equalsIgnoreCase("A")) { // 꿈꾸는 곰인형
			if (pc.getInventory().checkItem(49026, 1000)) {
				pc.getInventory().consumeItem(49026, 1000);
				pc.getInventory().storeItem(41093, 1);
				htmlid = "jp6";
			} else {
				htmlid = "jp5";
			}
		} else if (s.equalsIgnoreCase("B")) { // 향수
			if (pc.getInventory().checkItem(49026, 5000)) {
				pc.getInventory().consumeItem(49026, 5000);
				pc.getInventory().storeItem(41094, 1);
				htmlid = "jp6";
			} else {
				htmlid = "jp5";
			}
		} else if (s.equalsIgnoreCase("C")) { // 드레스
			if (pc.getInventory().checkItem(49026, 10000)) {
				pc.getInventory().consumeItem(49026, 10000);
				pc.getInventory().storeItem(41095, 1);
				htmlid = "jp6";
			} else {
				htmlid = "jp5";
			}
		} else if (s.equalsIgnoreCase("D")) { // 반지
			if (pc.getInventory().checkItem(49026, 100000)) {
				pc.getInventory().consumeItem(49026, 100000);
				pc.getInventory().storeItem(41095, 1);
				htmlid = "jp6";
			} else {
				htmlid = "jp5";
			}
		} else if (s.equalsIgnoreCase("E")) { // 위인전
			if (pc.getInventory().checkItem(49026, 1000)) {
				pc.getInventory().consumeItem(49026, 1000);
				pc.getInventory().storeItem(41098, 1);
				htmlid = "jp8";
			} else {
				htmlid = "jp5";
			}
		} else if (s.equalsIgnoreCase("F")) { // 세련된 모자
			if (pc.getInventory().checkItem(49026, 5000)) {
				pc.getInventory().consumeItem(49026, 5000);
				pc.getInventory().storeItem(41099, 1);
				htmlid = "jp8";
			} else {
				htmlid = "jp5";
			}
		} else if (s.equalsIgnoreCase("G")) { // 최고급 와인
			if (pc.getInventory().checkItem(49026, 10000)) {
				pc.getInventory().consumeItem(49026, 10000);
				pc.getInventory().storeItem(41100, 1);
				htmlid = "jp8";
			} else {
				htmlid = "jp5";
			}
		} else if (s.equalsIgnoreCase("H")) { // 알 수 없는 열쇠
			if (pc.getInventory().checkItem(49026, 100000)) {
				pc.getInventory().consumeItem(49026, 100000);
				pc.getInventory().storeItem(41101, 1);
				htmlid = "jp8";
			} else {
				htmlid = "jp5";
			}
		}
		
		return htmlid;
	}

	@SuppressWarnings("deprecation")
	private String 드루가베일(L1PcInstance pc, String s) {

		String htmlid = "";
		L1ItemInstance item = null;
		Date day = new Date(System.currentTimeMillis());
		if (day.getHours() >= 2 && day.getHours() <= 7) {
			return "noveil2";
		}
		
		if (s.equalsIgnoreCase("a")) {
			if (getDragonKeyCheck(60350) >= 6) {
				htmlid = "veil8";
			} else if (!pc.getInventory().checkItem(60350) && pc.getInventory().consumeItem(L1ItemId.ADENA, 10000000)) {   // 1000만 아데나가 없다.
				item = pc.getInventory().storeItem(60350, 1);
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
				htmlid = "okveil";
			} else {
				htmlid = "noveil";
			}
		} else if (s.equalsIgnoreCase("b")) {
			if (getDragonKeyCheck(60351) >= 6) {
				htmlid = "veil8";
			} else if (!pc.getInventory().checkItem(60351) && pc.getInventory().consumeItem(L1ItemId.ADENA, 10000000)) { // 1000만 아데나가 없다.
				item = pc.getInventory().storeItem(60351, 1);
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
				htmlid = "okveil";
			} else {
				htmlid = "noveil";
			}
		} else if (s.equalsIgnoreCase("c")) {
			if (getDragonKeyCheck(60352) >= 6) {
				htmlid = "veil8";
			} else if (!pc.getInventory().checkItem(60352) && pc.getInventory().consumeItem(L1ItemId.ADENA, 10000000)) { // 1000만 아데나가 없다.
				item = pc.getInventory().storeItem(60352, 1);
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
				htmlid = "okveil";
			} else {
				htmlid = "noveil";
			}
		} else if (s.equalsIgnoreCase("d")) {
			if (getDragonKeyCheck(61000) >= 6) {
				htmlid = "veil8";
			} else if (!pc.getInventory().checkItem(61000) && pc.getInventory().consumeItem(L1ItemId.ADENA, 10000000)) {
				item = pc.getInventory().storeItem(61000, 1);
				pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
				htmlid = "okveil";
			} else {
				htmlid = "noveil";
			}
		}

		return htmlid;
	}

	public int getDragonKeyCheck(int itemid) {
		int i = 0;
		for (L1FieldObjectInstance npc : L1World.getInstance().getAllField()) {
			if (itemid == 60350 && npc.getNpcId() == 4212015) {
				i++;
			} else if (itemid == 60351 && (npc.getNpcId() == 777773 || npc.getNpcId() == 4212016)) {
				i++;
			} else if (itemid == 60352 && npc.getNpcId() == 100011) {
				i++;
			} else if (itemid == 61000 && npc.getNpcId() == 910008){
				i++;
			}
		}
		
		for (L1Object obj : L1World.getInstance().getAllItem()) {
			if (!(obj instanceof L1ItemInstance)) {
				continue;
			}

			L1ItemInstance item = (L1ItemInstance) obj;
			
			if (item.getItemId() == itemid){
				i++;
			}
			
			if (i >= 6){
				return i;
			}
		}
		
		Connection con = null;
		PreparedStatement pstm = null;
		PreparedStatement pstm2 = null;
		ResultSet rs = null;
		ResultSet rs2 = null;
		
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_items WHERE item_id = ?");
			pstm.setInt(1, itemid);
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				i++;
				if (i >= 6){
					break;
				}
			}
			
			pstm2 = con.prepareStatement("SELECT * FROM npc_shop_sell WHERE item_id = ?");
			pstm2.setInt(1, itemid);
			rs2 = pstm2.executeQuery();
			
			while (rs2.next()) {
				i++;
				if (i >= 6){
					break;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs2);
			SQLUtil.close(pstm2);
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
		return i;
	}

	/*private String 지그프리드(L1PcInstance pc, String s) {

		String htmlid = "";
		int level = 70;

		if (pc.getLevel() < level)
			htmlid = "zigpride2";
		else
			이리스라던입장(pc, 32741, 32768);
		return htmlid;
	}*/

	/*private void 이리스라던입장(L1PcInstance pc, int x, int y) {

		try {
			Random random = new Random();
			int gn1 = random.nextInt(3);
			int time = 0;
			int outtime = 3600;
			if (pc.get라던day() == null) {
				pc.set라던time(1);

				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				pc.set라던day(nowday);
				pc.save();
				// pc.sendPackets(new S_SystemMessage(pc,
				// "던전 체류 시간이 2시간 남았습니다."), true);
				pc.sendPackets(new S_ServerMessage(1526, "2"));
				L1Teleport.teleport(pc, x + gn1, y + gn1, (short) 479, 5, true,
						true, 5000);
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
				// L1Teleport.teleport(pc, 32730 + gn1, 32846 + gn1, (short)
				// 451, 5, true, true, 5000);
			} else {
				time = pc.get라던time();

				 * if (pc.getravaday().getDate() != nowday.getDate()){
				 * pc.setravatime(1); pc.save(); pc.sendPackets(new
				 * S_ServerMessage(1526, "3"));// 3시간 남았다.
				 * L1Teleport.teleport(pc, 32730 + gn1, 32846 + gn1, (short)
				 * 451, 5, true); }else

				if (outtime > time) {
					int h = (outtime - time) / 60 / 60;
					if (h < 0) {
						h = 0;
					}
					int m = (outtime - time) / 60 % 60;
					if (m < 0) {
						m = 0;
					}

					if (h > 0) {
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));// %시간
																					// %분남았습니다.
					} else {
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 시간이 "+m+"분 남았습니다."), true);
						pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));// 분
																				// 남았다.
					}
					L1Teleport.teleport(pc, x + gn1, y + gn1, (short) 479, 5,
							true, true, 5000);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ABSOLUTE_BARRIER, 3000);
					// L1Teleport.teleport(pc, 32730 + gn1, 32846 + gn1, (short)
					// 451, 5, true, true, 5000);
				} else {
					// pc.sendPackets(new S_SystemMessage(pc,
					// "던전 체류 가능 시간 2시간을 모두 사용하셨습니다."), true);
					pc.sendPackets(new S_ServerMessage(1522, "2"));// 5시간 모두
																	// 사용했다.
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/

	private void 마에노브불꽃(String s, L1PcInstance pc, L1Object obj) {

		if (s.equalsIgnoreCase("A")) {
			if (pc.getInventory().checkItem(40308, 300)) {
				switch (pc.getMapId()) {
				case 106:
					if (pc.getInventory().consumeItem(60200, 2) && pc.getInventory().consumeItem(40308, 300)) {
						L1Teleport.teleport(pc, 32800, 32800, (short) (pc.getMapId() + 4), 5, true);
					}
					break;
				case 116:
					if (pc.getInventory().consumeItem(40104, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32792 && obj.getY() == 32862) {
							L1Teleport.teleport(pc, 32817, 32834, (short) (pc.getMapId() + 4), 5, true);
						} else {
							L1Teleport.teleport(pc, 32753, 32738, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				case 126:
					if (pc.getInventory().consumeItem(40105, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32792 && obj.getY() == 32862) {
							L1Teleport.teleport(pc, 32817, 32834, (short) (pc.getMapId() + 4), 5, true);
						}
						else {
							L1Teleport.teleport(pc, 32753, 32738, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				case 136:
					if (pc.getInventory().consumeItem(40106, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32792 && obj.getY() == 32862) {
							L1Teleport.teleport(pc, 32817, 32834, (short) (pc.getMapId() + 4), 5, true);
						}
						else {
							L1Teleport.teleport(pc, 32753, 32738, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				case 146:
					if (pc.getInventory().consumeItem(40107, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32792 && obj.getY() == 32862) {
							L1Teleport.teleport(pc, 32817, 32834, (short) (pc.getMapId() + 4), 5, true);
						} else {
							L1Teleport.teleport(pc, 32753, 32738, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				case 156:
					if (pc.getInventory().consumeItem(40108, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32795 && obj.getY() == 32798) {
							L1Teleport.teleport(pc, 32683, 32814, (short) (pc.getMapId() + 4), 5, true);
						} else {
							L1Teleport.teleport(pc, 32748, 32812, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				case 166:
					if (pc.getInventory().consumeItem(40109, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32734 && obj.getY() == 32799) {
							L1Teleport.teleport(pc, 32633, 32786, (short) (pc.getMapId() + 4), 5, true);
						} else {
							L1Teleport.teleport(pc, 32712, 32925, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				case 176:
					if (pc.getInventory().consumeItem(40110, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32795 && obj.getY() == 32798) {
							L1Teleport.teleport(pc, 32750, 32876, (short) (pc.getMapId() + 4), 5, true);
						} else {
							L1Teleport.teleport(pc, 32633, 32787, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				case 186:
					if (pc.getInventory().consumeItem(40111, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32734 && obj.getY() == 32799) {
							L1Teleport.teleport(pc, 32632, 32787, (short) (pc.getMapId() + 4), 5, true);
						} else {
							L1Teleport.teleport(pc, 32750, 32877, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				case 196:
					if (pc.getInventory().consumeItem(40112, 2) && pc.getInventory().consumeItem(40308, 300)) {
						if (obj.getX() == 32734 && obj.getY() == 32799) {
							L1Teleport.teleport(pc, 32733, 32857, (short) (pc.getMapId() + 4), 5, true);
						} else {
							L1Teleport.teleport(pc, 32632, 32972, (short) (pc.getMapId() + 4), 5, true);
						}
					}
					break;
				}
			} else {
				pc.sendPackets(new S_SystemMessage("아데나 가 모자릅니다."));
			}
		} else if (s.equalsIgnoreCase("B")) {
			if (pc.getInventory().consumeItem(40308, 300)) {
				switch (pc.getMapId()) {
				case 106:
					L1Teleport.teleport(pc, 32798, 32802, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 116:
					L1Teleport.teleport(pc, 32631, 32935, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 126:
					L1Teleport.teleport(pc, 32631, 32935, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 136:
					L1Teleport.teleport(pc, 32631, 32935, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 146:
					L1Teleport.teleport(pc, 32631, 32935, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 156:
					L1Teleport.teleport(pc, 32669, 32814, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 166:
					L1Teleport.teleport(pc, 32669, 32814, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 176:
					L1Teleport.teleport(pc, 32669, 32814, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 186:
					L1Teleport.teleport(pc, 32669, 32814, (short) (pc.getMapId() - 5), 5, true);
					break;
				case 196:
					L1Teleport.teleport(pc, 32669, 32814, (short) (pc.getMapId() - 5), 5, true);
					break;
				}
			} else {
				pc.sendPackets(new S_SystemMessage("아데나 가 모자릅니다."));
			}
		} else if (s.equalsIgnoreCase("C")) {
			if (pc.getInventory().consumeItem(40308, 300)) {
				switch (pc.getMapId()) {
				case 106:
					if (obj.getX() == 33816 && obj.getY() == 32862) {
						L1Teleport.teleport(pc, 33773, 32867, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 33813, 32862, (short) pc.getMapId(), 5, true);
					}
					break;
				case 116:
					if (obj.getX() == 32792 && obj.getY() == 32862) {
						L1Teleport.teleport(pc, 32749, 32868, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32789, 32862, (short) pc.getMapId(), 5, true);
					}
					break;
				case 126:
					if (obj.getX() == 32792 && obj.getY() == 32862) {
						L1Teleport.teleport(pc, 32748, 32868, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32788, 32863, (short) pc.getMapId(), 5, true);
					}
					break;
				case 136:
					if (obj.getX() == 32792 && obj.getY() == 32862) {
						L1Teleport.teleport(pc, 32748, 32868, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32788, 32863, (short) pc.getMapId(), 5, true);
					}
					break;
				case 146:
					if (obj.getX() == 32792 && obj.getY() == 32862) {
						L1Teleport.teleport(pc, 32748, 32868, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32788, 32863, (short) pc.getMapId(), 5, true);
					}
					break;
				case 156:
					if (obj.getX() == 32795 && obj.getY() == 32798) {
						L1Teleport.teleport(pc, 32738, 32799, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32790, 32799, (short) pc.getMapId(), 5, true);
					}
					break;
				case 166:
					if (obj.getX() == 32734 && obj.getY() == 32799) {
						L1Teleport.teleport(pc, 32791, 32799, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32738, 32799, (short) pc.getMapId(), 5, true);
					}
					break;
				case 176:
					if (obj.getX() == 32795 && obj.getY() == 32798) {
						L1Teleport.teleport(pc, 32739, 32800, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32790, 32798, (short) pc.getMapId(), 5, true);
					}
					break;
				case 186:
					if (obj.getX() == 32734 && obj.getY() == 32799) {
						L1Teleport.teleport(pc, 32790, 32799, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32739, 32800, (short) pc.getMapId(), 5, true);
					}
					break;
				case 196:
					if (obj.getX() == 32734 && obj.getY() == 32799) {
						L1Teleport.teleport(pc, 32790, 32799, (short) pc.getMapId(), 5, true);
					} else {
						L1Teleport.teleport(pc, 32739, 32800, (short) pc.getMapId(), 5, true);
					}
					break;
				}
			} else {
				pc.sendPackets(new S_SystemMessage("아데나 가 모자릅니다."));
			}
		}
	}

	private String 스냅퍼(L1PcInstance pc, String s) {
		try {

			String htmlid = "";
			if (s.equalsIgnoreCase("A")) { // 76개방
				if (pc.getRingSlotLevel() > 0) { // 이미 개방 되어있음
					htmlid = "slot5";
				} else {
					if (pc.getLevel() >= 76 && pc.getInventory().consumeItem(40308, 10000000)) {
						pc.setRingSlotLevel(1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
						pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT,S_ReturnedStat.SUBTYPE_RING, pc.getRingSlotLevel()));
						
						try {
							pc.save();
						} catch (Exception e) {}
						htmlid = "slot9";
						// pc.sendPackets(new
						// S_SystemMessage("영웅의 특권으로 왼쪽 반지 슬롯을 개방하였습니다."),
						// true);
					} else {
						htmlid = "slot10";
					}
				}
			} else if (s.equalsIgnoreCase("B")) { // 81개방
				if (pc.getRingSlotLevel() >= 2) { // 이미 개방 되어있음
					htmlid = "slot5";
				} else if (pc.getRingSlotLevel() == 0) {// 개방되어있는게 없음
					pc.sendPackets(new S_SystemMessage("76 슬롯부터 개방 하십시요."), true);
				} else {
					if (pc.getLevel() >= 81 && pc.getInventory().consumeItem(40308, 30000000)) {
						pc.setRingSlotLevel(2);
						pc.sendPackets(new S_SkillSound(pc.getId(), 12003));
						pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT,S_ReturnedStat.SUBTYPE_RING, pc.getRingSlotLevel()));
						try {
							pc.save();
						} catch (Exception e) {}
						htmlid = "slot9";
					} else
						htmlid = "slot10";
				}
			} else if (s.equalsIgnoreCase("C")) { // 59귀걸이
				if (pc.getEarringSlotLevel() >= 1) { // 이미 개방 되어있음
					htmlid = "slot5";
				} else {
					if (pc.getLevel() >= 59 && pc.getInventory().consumeItem(40308, 2000000)) {
						pc.setEarringSlotLevel(1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
						pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 16));
						
						try {
							pc.save();
						} catch (Exception e) {}
						
						htmlid = "slot9";
					} else {
						htmlid = "slot10";
					}
				}
			} else if (s.equals("F")) { // 휘장
				if (pc.getEmblem_Slot() == 1) {
					pc.sendPackets(new S_ServerMessage(3255));
					htmlid = "slot5";
					// 해당 슬롯은 이미 확장되었습니다.
				} else {
					if (pc.getInventory().checkItem(40308, 2000000) && pc.getLevel() >= 70) {
						pc.getInventory().consumeItem(40308, 2000000);
						pc.setEmblem_Slot(1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
						pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 128));
						pc.sendPackets(new S_NPCTalkReturn(pc.getId(), "slot9"));
						try {
							pc.save();
						} catch (Exception e) {
						}
						
						htmlid = "slot9";
					} else {
						htmlid = "slot11";
					}
				}
			} else if (s.equals("E")) { // 견갑
				if (pc.getShoulder_Slot() == 1) {
					pc.sendPackets(new S_ServerMessage(3255));
					// 해당 슬롯은 이미 확장되었습니다.
				} else {
					if (pc.getInventory().checkItem(40308, 30000000) && pc.getLevel() >= 83) {
						pc.getInventory().consumeItem(40308, 30000000);
						pc.getInventory().storeItem(301068, 1);

						pc.setShoulder_Slot(1);
						pc.sendPackets(new S_SkillSound(pc.getId(), 12004));
						pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 64));
						
						try {
							pc.save();
						} catch (Exception e) {}
						
						htmlid = "slot9";
					} else {
						htmlid = "slot11";
					}
				}
			}
			
			return htmlid;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

	private String 땅굴개미(L1PcInstance pc, String s) {

		String htmlid = "";
		if (pc.getInventory().consumeItem(L1ItemId.ADENA, 500)) {
			if (s.equalsIgnoreCase("b")) {
				L1Teleport.teleport(pc, 32784, 32751, (short) 43, 7, true);
			} else if (s.equalsIgnoreCase("c")) {
				L1Teleport.teleport(pc, 32798, 32754, (short) 44, 1, true);
			} else if (s.equalsIgnoreCase("d")) {
				L1Teleport.teleport(pc, 32759, 32742, (short) 45, 6, true);
			} else if (s.equalsIgnoreCase("e")) {
				L1Teleport.teleport(pc, 32750, 32764, (short) 46, 7, true);
			} else if (s.equalsIgnoreCase("f")) {
				L1Teleport.teleport(pc, 32795, 32746, (short) 47, 7, true);
			} else if (s.equalsIgnoreCase("g")) {
				L1Teleport.teleport(pc, 32768, 32805, (short) 50, 7, true);
			}
		} else {
			htmlid = "cave2";
		}
		
		return htmlid;
	}

	private String 감시자의눈(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("exitghost") && pc.isGhost()) {
			pc.makeReadyEndGhost();
			pc.endGhost();
		}
		
		return htmlid;
	}

	private String 호슈(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a") && pc.getInventory().consumeItem(L1ItemId.ADENA, 2000)) {
			try {
				pc.save();

				L1Location loc = new L1Location();
				loc.set(32763, 33173, 4);
				loc = loc.randomLocation(10, false);
				pc.beginGhost(loc.getX(), loc.getY(), (short) loc.getMapId(), true, 60 * 5);
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		} else if (s.equalsIgnoreCase("1") && pc.getInventory().consumeItem(L1ItemId.ADENA, 10000)) {
			L1Teleport.teleport(pc, 32724, 33138, (short) 4, 5, true);
		} else {
			htmlid = "hyosue1";
		}
		
		return htmlid;
	}

	private String 노베르(L1PcInstance pc, String s) {

		String htmlid = "";
		if (pc.getInventory().checkItem(60177, 1)) {
			
			try {
				int usetime = pc.get수렵이벤트time();
				int outtime = 60 * 60 * 1;
				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				if (s.equalsIgnoreCase("A")) {
					
					try {
						String s1 = isPC입장가능여부(pc.get수렵이벤트day(), outtime, usetime);
						if (s1.equals("입장가능")) {// 입장가능
							int h = (outtime - usetime) / 60 / 60;
							if (h < 0) {
								h = 0;
							}
							
							int m = (outtime - usetime) / 60 % 60;
							if (m < 0) {
								m = 0;
							}
							
							if (h > 0) {
								// pc.sendPackets(new S_SystemMessage(pc,
								// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
								pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간  %분남았습니다.
							} else {
								// pc.sendPackets(new S_SystemMessage(pc,
								// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
								pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
							}
							
							L1Teleport.teleport(pc, 32791, 32738, (short) 785, 5, true);
						} else if (s1.equals("불가능")) {// 입장불가능
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능 시간 1시간을 모두 사용하셨습니다."), true);
							pc.sendPackets(new S_ServerMessage(1522, "1"));// 5시간 모두 사용했다
							return "";
						} else if (s1.equals("초기화")) {// 초기화
							pc.set수렵이벤트time(1);
							pc.set수렵이벤트day(nowday);
							pc.save();
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 시간이 1시간 남았습니다."), true);
							pc.sendPackets(new S_ServerMessage(1526, "1"));   // 시간 남았다.
							L1Teleport.teleport(pc, 32791, 32738, (short) 785, 5, true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else if (s.equalsIgnoreCase("B")) {
					try {
						String s1 = isPC입장가능여부(pc.get수렵이벤트day(), outtime, usetime);
						if (s1.equals("입장가능")) {// 입장가능
							int h = (outtime - usetime) / 60 / 60;
							if (h < 0) {
								h = 0;
							}
							
							int m = (outtime - usetime) / 60 % 60;
							if (m < 0) {
								m = 0;
							}
							
							if (h > 0) {
								// pc.sendPackets(new S_SystemMessage(pc,
								// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
								pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));   // %시간 %분남았습니다.
							} else {
								// pc.sendPackets(new S_SystemMessage(pc,
								// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
								pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));   // 분 남았다
							}
							L1Teleport.teleport(pc, 32791, 32738, (short) 788,
									5, true);
						} else if (s1.equals("불가능")) {// 입장불가능
							pc.sendPackets(new S_ServerMessage(1522, "1"));   // 5시간 모두 사용했다
							return "";
						} else if (s1.equals("초기화")) {// 초기화
							pc.set수렵이벤트time(1);
							pc.set수렵이벤트day(nowday);
							pc.save();
							pc.sendPackets(new S_ServerMessage(1526, "1"));  // 시간 남았다.
							L1Teleport.teleport(pc, 32791, 32738, (short) 788, 5, true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (s.equalsIgnoreCase("C")) {
					try {
						String s1 = isPC입장가능여부(pc.get수렵이벤트day(), outtime, usetime);
						if (s1.equals("입장가능")) {// 입장가능
							int h = (outtime - usetime) / 60 / 60;
							if (h < 0) {
								h = 0;
							}
							int m = (outtime - usetime) / 60 % 60;
							if (m < 0) {
								m = 0;
							}
							if (h > 0) {
								// pc.sendPackets(new S_SystemMessage(pc,
								// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
								pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));   // %시간 %분남았습니다.
							} else {
								// pc.sendPackets(new S_SystemMessage(pc,
								// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
								pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분  남았다
							}
							L1Teleport.teleport(pc, 32791, 32738, (short) 789, 5, true);
						} else if (s1.equals("불가능")) {// 입장불가능
							pc.sendPackets(new S_ServerMessage(1522, "1"));  // 5시간 모두 사용했다
							return "";
						} else if (s1.equals("초기화")) {// 초기화
							pc.set수렵이벤트time(1);
							pc.set수렵이벤트day(nowday);
							pc.save();
							pc.sendPackets(new S_ServerMessage(1526, "1"));   // 시간 남았다.
							L1Teleport.teleport(pc, 32791, 32738, (short) 789, 5, true);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			} catch (Exception e) {
			}
		}
		
		return htmlid;
	}

	private String 시이니(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("A")) {
			if (pc.getInventory().checkItem(40308, 1000) && !pc.getInventory().checkItem(60177, 1)) {
				// 출입증 지급
				pc.getInventory().storeItem(60177, 1);
				pc.getInventory().storeItem(60180, 5);// 은빛의 마법 구슬 조각
				// pc.getInventory().storeItem(60182, 5);//금빛의 마법 구슬 조각
				htmlid = "sini4";
			}
		}
		
		return htmlid;
	}

	private String 노무르(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
			// 은빛의 눈물100, 은빛의 마법 구슬 조각1
			if (pc.getInventory().checkItem(60179, 100) && pc.getInventory().checkItem(60180, 1)) {
				pc.getInventory().consumeItem(60179, pc.getInventory().countItems(60179));
				pc.getInventory().consumeItem(60180, 1);
				int needExp = ExpTable.getNeedExpNextLevel(pc.getLevel());
				int addexp = (int) (needExp * 0.05);
				if (addexp != 0) {
					if (addexp != 0) {
						int level = ExpTable .getLevelByExp(pc.getExp() + addexp);
						if (level > Config.MAXLEVEL) {
							pc.sendPackets(new S_SystemMessage("레벨 제한으로 인해  더이상 경험치를 획득할 수 없습니다."), true);
						} else if (level > 75)
							pc.sendPackets(new S_SystemMessage("레벨 75를 초과하여 올릴 수 없습니다."), true);
						else {
							pc.addExp(addexp);
						}
					}
				}
				
				try {
					pc.save();
				} catch (Exception e) {
				}
				
				htmlid = "rewarddkev7";
			} else {
				htmlid = "rewarddkev8";
			}
		} else if (s.equalsIgnoreCase("b")) {
			// 금빛의 눈물100, 금빛의 마법 구슬 조각1
			if (pc.getInventory().checkItem(60181, 100) && pc.getInventory().checkItem(60182, 1)) {
				pc.getInventory().consumeItem(60181, pc.getInventory().countItems(60181));
				pc.getInventory().consumeItem(60182, 1);
				int needExp = ExpTable.getNeedExpNextLevel(pc.getLevel());
				int addexp = (int) (needExp * 0.05);
				if (addexp != 0) {
					pc.addExp(addexp);
				}
				
				try {
					pc.save();
				} catch (Exception e) {
				}
				
				htmlid = "rewarddkev7";
			} else {
				htmlid = "rewarddkev8";
			}
		}
		
		return htmlid;
	}

	private String 피아르(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) { // 근거리 버프
			if (!pc.PC방_버프) {
				pc.sendPackets(new S_SystemMessage("PC방 코인 상품을 사용 중인 유저만 입장 가능합니다."));
				return "";
			}
			
			L1Teleport.teleport(pc, 32769, 32838, (short) 622, 5, true);
		}
		
		return htmlid;
	}

	private String 피도르(L1PcInstance pc, String s) {

		String htmlid = "";
		if (s.equalsIgnoreCase("a")) { // 근거리 버프
			if (pc.getInventory().checkItem(40308, 500)) { // 신비한 날개 깃털
				// if (pc.getClanid() >= 0){
				pc.getInventory().consumeItem(40308, 500);
				int[] allBuffSkill = { L1SkillId.PHYSICAL_ENCHANT_DEX, L1SkillId.PHYSICAL_ENCHANT_STR, L1SkillId.BLESS_WEAPON };
				pc.setBuffnoch(1); // 추가적으로 버프는 미작동
				L1SkillUse l1skilluse = new L1SkillUse();
				for (int i = 0; i < allBuffSkill.length; i++) {
					l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
				}
				htmlid = "";
				// } else {
				// pc.sendPackets(new
				// S_SystemMessage("혈맹을 가입하셔야 버프를 받을수 있습니다."));
				// }
			} else {
				pc.sendPackets(new S_SystemMessage("아데나 가 부족합니다."));
			}
		}


		return htmlid;
	}



	private String 욕망버땅(L1PcInstance pc, String s) {
		String htmlid = "";

		if (s.equalsIgnoreCase("a")) {
			L1Teleport.teleport(pc, 32757, 32794, (short) 600, 5, true);
		}
		if (s.equalsIgnoreCase("b")) {
			int level = 60;
			if (pc.getLevel() >= level && pc.getLevel() <= 69) {
				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				try {
					int outtime = Config.계정_용의둥지_시간;
					int usetime = pc.get용둥time();

					String s2 = isAccount입장가능여부(pc.get용둥day(), outtime, usetime);
					if (s2.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						if (h > 0) {
							pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 " + h + "시간 " + m + "분 남음"), true);
						} else {
							pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 " + m + "분 남음"), true);
						}
					} else if (s2.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 6시간 모두 사용"), true);
						return "";
					} else if (s2.equals("초기화")) {// 초기화
						pc.set용둥time(1);
						pc.set용둥day(nowday);
						pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 6시간 남음"), true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					int outtime = Config.PC_용의둥지_시간;
					int usetime = pc.getpc용둥time();
					String s1 = isPC입장가능여부(pc.getpc용둥day(), outtime, usetime);
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));// %시간
											// %분남았습니다.
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						} else {
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));// 분
											// 남았다
						}
						L1Teleport.teleport(pc, 32834, 32790, (short) 778, 5, true);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "3"));   // 5시간 모두 사용했다.
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 가능 시간 2시간을 모두 사용하셨습니다."), true);
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.setpc용둥time(1);
						pc.setpc용둥day(nowday);
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 시간이 2시간 남았습니다."), true);
						pc.sendPackets(new S_ServerMessage(1526, "3"));   // 시간 남았다.
						L1Teleport.teleport(pc, 32834, 32790, (short) 778, 5, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				htmlid = "";
			}
		}
		
		return htmlid;
	}

	private String 마법의문(L1PcInstance pc, String s) {
		String htmlid = "";

		if (s.equalsIgnoreCase("a")) {
			int level = 1;
			if (pc.getLevel() >= level && pc.getLevel() <= 70) {
				L1Teleport.teleport(pc, 32667, 32804, (short) 1, 5, true);
			}
			
			pc.sendPackets(new S_SystemMessage("레벨 1부터 70레벨까지 이용가능합니다."), true);
		}
		if (s.equalsIgnoreCase("3")) {
			int level = 60;
			if (pc.getLevel() >= level && pc.getLevel() <= 59) {
				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				try {
					int outtime = Config.계정_용의둥지_시간;
					int usetime = pc.get용둥time();

					String s2 = isAccount입장가능여부(pc.get용둥day(), outtime, usetime);
					if (s2.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 " + h + "시간 " + m + "분 남음"), true);
						} else {
							pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 " + m + "분 남음"), true);
						}
					} else if (s2.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 6시간 모두 사용"), true);
						return "";
					} else if (s2.equals("초기화")) {// 초기화
						pc.set용둥time(1);
						pc.set용둥day(nowday);
						pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 6시간 남음"), true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					int outtime = Config.PC_용의둥지_시간;
					int usetime = pc.getpc용둥time();
					String s1 = isPC입장가능여부(pc.getpc용둥day(), outtime, usetime);
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));// %시간
											// %분남았습니다.
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						} else {
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));// 분
											// 남았다
						}
						L1Teleport.teleport(pc, 32538, 32958, (short) 777, 5, true);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "3"));   // 5시간 모두 사용했다.
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 가능 시간 2시간을 모두 사용하셨습니다."), true);
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.setpc용둥time(1);
						pc.setpc용둥day(nowday);
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 시간이 2시간 남았습니다."), true);
						pc.sendPackets(new S_ServerMessage(1526, "3"));   // 시간  남았다.
						L1Teleport.teleport(pc, 32538, 32958, (short) 777, 5, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				htmlid = "";
			}
		}
		
		return htmlid;
	}

	private String 검은전함(L1PcInstance pc, String s) {

		String htmlid = "";
		try {
			int x = 0, y = 0, mapid = 0;
			if (s.equalsIgnoreCase("a")) {
				int level = 1;
				if (pc.getLevel() >= level && pc.getLevel() < 71) {
					x = 32787 + _random.nextInt(2);
					y = 32735 + _random.nextInt(2);
					mapid = 10;
				} else {
					pc.sendPackets(new S_PacketBox(84, "\\fQ와슨: \\f3[입장불가]\\f2[검은전함] 입장 가능 레벨은  1~70 레벨입니다."), false);
				}
			}
			
			if (mapid != 0) {
				try {
					if (pc.getLevel() <= 89){
					int usetime = pc.get검은전함time();
					int outtime = 60 * 60 * 2;
					Timestamp nowday = new Timestamp(System.currentTimeMillis());
					L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
					String s1 = isPC입장가능여부(pc.get검은전함day(), outtime, usetime);
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간 %분남았습니다.
						} else {
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
						}

						L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "2"));  // 5시간 모두사용했다.
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.set검은전함time(1);
						pc.set검은전함day(nowday);
						pc.save();
						pc.sendPackets(new S_ServerMessage(1526, "2"));  // 시간남았다.
						L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
					}
				  } else if (pc.getLevel() >= 90) {
					  int usetime = pc.get검은전함time();
					  int outtime = 60 * 60 * 3;
					  Timestamp nowday = new Timestamp(System.currentTimeMillis());
					  L1Location loc = new L1Location(x, y, mapid).randomLocation(1, true);
					  String s1 = isPC입장가능여부(pc.get검은전함day(), outtime, usetime);
					  if (s1.equals("입장가능")) {// 입장가능
						  int h = (outtime - usetime) / 60 / 60;
						  if (h < 0) {
							  h = 0;
						  }
						  int m = (outtime - usetime) / 60 % 60;
						  if (m < 0) {
							  m = 0;
						  }
						  if (h > 0) {
							  pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간  %분남았습니다.
						  } else {
							  pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
						  }
						  L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
					  } else if (s1.equals("불가능")) {// 입장불가능
						  pc.sendPackets(new S_ServerMessage(1522, "3"));  // 5시간 모두사용했다.
						  return "";
					  } else if (s1.equals("초기화")) {// 초기화
						  pc.set검은전함time(1);
						  pc.set검은전함day(nowday);
						  pc.save();
						  pc.sendPackets(new S_ServerMessage(1526, "3"));  // 시간남았다.
						  L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
					  }
				  }
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
		}
		
		return htmlid;
	}

	private String 그신버땅(L1PcInstance pc, String s) {
		String htmlid = "";

		if (s.equalsIgnoreCase("1")) {
			L1Teleport.teleport(pc, 32676, 32960, (short) 521, 5, true);
		}
		
		if (s.equalsIgnoreCase("3")) {
			int level = 60;
			if (pc.getLevel() >= level && pc.getLevel() <= 69) {
				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				try {
					int outtime = Config.계정_용의둥지_시간;
					int usetime = pc.get용둥time();

					String s2 = isAccount입장가능여부(pc.get용둥day(), outtime, usetime);
					if (s2.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 " + h + "시간 " + m + "분 남음"), true);
						} else {
							pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 " + m + "분 남음"), true);
						}
					} else if (s2.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 6시간 모두 사용"), true);
						return "";
					} else if (s2.equals("초기화")) {// 초기화
						pc.set용둥time(1);
						pc.set용둥day(nowday);
						pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 6시간 남음"), true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					int outtime = Config.PC_용의둥지_시간;
					int usetime = pc.getpc용둥time();
					String s1 = isPC입장가능여부(pc.getpc용둥day(), outtime, usetime);
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간  %분남았습니다.
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						} else {
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
						}
						L1Teleport.teleport(pc, 32538, 32958, (short) 777, 5,
								true);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "3"));  // 5시간 모두 사용했다.
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 가능 시간 2시간을 모두 사용하셨습니다."), true);
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.setpc용둥time(1);
						pc.setpc용둥day(nowday);
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 시간이 2시간 남았습니다."), true);
						pc.sendPackets(new S_ServerMessage(1526, "3"));  // 시간  남았다.
						L1Teleport.teleport(pc, 32538, 32958, (short) 777, 5, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				htmlid = "";
			}
		}
		
		return htmlid;
	}

	private String 용의전령후오스(L1PcInstance pc, String s) {
		String htmlid = "";

		if (s.equalsIgnoreCase("1")) {
			int level = 55;

			if (pc.getLevel() >= level && pc.getLevel() < Config.MAX_후오스_DUNGEON_LEVEL) {
				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				try {
					int outtime = Config.계정_용의둥지_시간;
					int usetime = pc.get용둥time();

					String s2 = isAccount입장가능여부(pc.get용둥day(), outtime, usetime);
					if (s2.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 " + h + "시간 " + m + "분 남음"), true);
						} else {
							pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 " + m + "분 남음"), true);
						}
					} else if (s2.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 6시간 모두 사용"), true);
						return "";
					} else if (s2.equals("초기화")) {// 초기화
						pc.set용둥time(1);
						pc.set용둥day(nowday);
						pc.sendPackets(new S_SystemMessage("입장 시간 : 계정 입장 시간 6시간 남음"), true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					int outtime = Config.PC_용의둥지_시간;
					int usetime = pc.getpc용둥time();
					String s1 = isPC입장가능여부(pc.getpc용둥day(), outtime, usetime);
					if (s1.equals("입장가능")) {// 입장가능
						int h = (outtime - usetime) / 60 / 60;
						if (h < 0) {
							h = 0;
						}
						
						int m = (outtime - usetime) / 60 % 60;
						if (m < 0) {
							m = 0;
						}
						
						if (h > 0) {
							pc.sendPackets(new S_ServerMessage(1525, h + "", m + ""));  // %시간 %분남았습니다.
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						} else {
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
							pc.sendPackets(new S_ServerMessage(1527, "" + m + ""));  // 분 남았다
						}
						L1Teleport.teleport(pc, 32773, 32804, (short) 814, 5, 	true);
					} else if (s1.equals("불가능")) {// 입장불가능
						pc.sendPackets(new S_ServerMessage(1522, "3"));  // 5시간 모두  사용했다.
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 가능 시간 2시간을 모두 사용하셨습니다."), true);
						return "";
					} else if (s1.equals("초기화")) {// 초기화
						pc.setpc용둥time(1);
						pc.setpc용둥day(nowday);
						// pc.sendPackets(new S_SystemMessage(pc,
						// "던전 체류 시간이 2시간 남았습니다."), true);
						pc.sendPackets(new S_ServerMessage(1526, "3"));  // 시간  남았다.
						L1Teleport.teleport(pc, 32773, 32804, (short) 814, 5, true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				htmlid = "dvdgate2";
			}
		}
		
		return htmlid;
	}

	private String 아툰무료변신(L1PcInstance pc, String s) {

		String html = "";
		int poly = 0;
		if (s.equalsIgnoreCase("a")) {
			poly = 11404;
		} else if (s.equalsIgnoreCase("b")) {
			poly = 11407;
		} else if (s.equalsIgnoreCase("c")) {
			poly = 11406;
		} else if (s.equalsIgnoreCase("d")) {
			poly = 11405;
		} else if (s.equalsIgnoreCase("e")) {
			poly = 12681;
		} else if (s.equalsIgnoreCase("f")) {
			poly = 9012;
		} else if (s.equalsIgnoreCase("g")) {
            poly = 9226;
		} else if (s.equalsIgnoreCase("i")) {
			poly = 8817;
		} else if (s.equalsIgnoreCase("j")) {
			poly = 8774;
		} else if (s.equalsIgnoreCase("k")) {
			poly = 8900;
		} else if (s.equalsIgnoreCase("l")) {
			poly = 8851;
		} else if (s.equalsIgnoreCase("m")) {
			poly = 9205;
		} else if (s.equalsIgnoreCase("n")) {
			poly = 9011;
		} else if (s.equalsIgnoreCase("o")) {
			poly = 9225;
		}
		
		if (poly != 0) {
			html = "atonf";
			L1PolyMorph.doPoly(pc, poly, 1800, L1PolyMorph.MORPH_BY_GM);
		}
		
		return html;
	}

	private String 오림(L1PcInstance pc, String s) {
		String htmlid = "";

		if (pc.getInventory().checkItem(60123) && pc.getInventory().checkItem(41246, 50000)) {
			if (s.equalsIgnoreCase("request guarder of orim hp")) { // 체력의 가더
				pc.getInventory().storeItem(21095, 1);
				pc.getInventory().consumeItem(60123, 1);
				pc.getInventory().consumeItem(41246, 50000);
				pc.sendPackets(new S_ServerMessage(403, "체력의 가더"), true);
				// pc.sendPackets(new S_SystemMessage("체력의 가더를 얻었습니다."), true);
			} else if (s.equalsIgnoreCase("request guarder of orim mp")) { // 마나의 가더
				pc.getInventory().storeItem(21096, 1);
				pc.getInventory().consumeItem(60123, 1);
				pc.getInventory().consumeItem(41246, 50000);
				pc.sendPackets(new S_ServerMessage(403, "수호의 가더"), true);
				// pc.sendPackets(new S_SystemMessage("마나의 가더를 얻었습니다."), true);
			} else if (s.equalsIgnoreCase("request guarder of orim wiz")) { // 마법사의 가더
				pc.getInventory().storeItem(21097, 1);
				pc.getInventory().consumeItem(60123, 1);
				pc.getInventory().consumeItem(41246, 50000);
				pc.sendPackets(new S_ServerMessage(403, "마법사의 가더"), true);
				// pc.sendPackets(new S_SystemMessage("마법사의 가더를 얻었습니다."), true);
			}
		} else {
			pc.sendPackets(new S_SystemMessage("오림의 일기장 또는 결정체가 부족합니다."), true);
		}
		
		return htmlid;
	}

	private static int[] 담금질아이템 = new int[] { 37, 100037, 42, 100042, 41, 100041, 52, 100052, 180, 100180, 181, 100181, 131 };

	private String 조우불골렘(String s, L1PcInstance pc) {
		String html = null;

		if (s.equals("a")) {
			boolean ck1 = false, ck2 = false;
			for (int f : 담금질아이템) {
				for (L1ItemInstance item : pc.getInventory().findItemsId(f)) {
					if (item.isEquipped()) {
						continue;
					}
					
					if (item.getEnchantLevel() == 8 && pc.getInventory().checkItem(40308, 5000000)) {
						ck1 = true;
					} else if (item.getEnchantLevel() == 9 && pc.getInventory().checkItem(40308, 10000000)) {
						ck2 = true;
					}
				}
			}
			
			if (ck1 && ck2) {
				html = "rushi01";
			} else if (ck1) {
				html = "rushi03";
			} else if (ck2) {
				html = "rushi02";
			} else {
				html = "rushi04";
			}
			
			return html;
		}
		
		int enc = 8, itemid = 0;
		int value = 5000000;
		if (s.equals("A")) {// 7마단 
			itemid = 60041;
		} else if (s.equals("B")) {// 7광풍
			itemid = 60042;
		} else if (s.equals("C")) {// 7파대 
			itemid = 60043;
		} else if (s.equals("D")) {// 7천사지팡이
			itemid = 60044;
		} else if (s.equals("E")) {// 7혹한의창
			itemid = 60045;
		} else if (s.equals("F")) {// 7뇌신검
			itemid = 60046;
		} else if (s.equals("G")) {// 7살천의활
			itemid = 60047;
		} else if (s.equals("H")) {// 8마력의단검
			itemid = 60048;
		} else if (s.equals("I")) {// 8광풍
			itemid = 60049;
		} else if (s.equals("J")) {// 8파대
			itemid = 60050;
		} else if (s.equals("K")) {// 8천상의 지팡이
			itemid = 60051;
		} else if (s.equals("L")) {// 8혹한의 창
			itemid = 60052;
		} else if (s.equals("M")) {// 8뇌신검
			itemid = 60053;
		} else if (s.equals("N")) {// 8살천의활
			itemid = 60054;
		} else {
			return html;
		}
		
		if (itemid >= 60048 && itemid <= 60054) {
			enc = 9;
			value = 10000000;
		}
		
		if (pc.getInventory().checkItem(40308, value)) {
			boolean ck = false;
			for (int f : 담금질아이템) {
				for (L1ItemInstance item : pc.getInventory().findItemsId(f)) {
					if (item.getEnchantLevel() == enc && !item.isEquipped()) {
						ck = true;
						pc.getInventory().consumeItem(40308, value);
						pc.getInventory().deleteItem(item);
						L1ItemInstance t = pc.getInventory().storeItem(itemid, 1);
						pc.sendPackets(new S_ServerMessage(403, t.getLogName()));   // %0를 손에 넣었습니다.
						html = "";
						break;
					}
				}
				
				if (ck) {
					break;
				}
			}
			
			if (!ck) {
				html = "rushi04";
			}
		} else {
			html = "rushi04";
		}
		
		return html;
	}

	private static int[] 피어스아이템 = new int[] { 81, 162, 177, 194, 13 };

	private String 피어스(String s, L1PcInstance pc) {
		String html = null;

		if (s.equals("a")) {
			boolean ck1 = false, ck2 = false;
			for (int f : 피어스아이템) {
				for (L1ItemInstance item : pc.getInventory().findItemsId(f)) {
					if (item.isEquipped()){
						continue;
					}
					
					if (item.getEnchantLevel() == 8 && pc.getInventory().checkItem(40308, 5000000)) {
						ck1 = true;
					} else if (item.getEnchantLevel() == 9 && pc.getInventory().checkItem(40308, 10000000)) {
						ck2 = true;
					}
				}
			}
			
			if (ck1 && ck2) {
				html = "piers01";
			} else if (ck1) {
				html = "piers03";
			} else if (ck2) {
				html = "piers02";
			} else {
				html = "piers04";
			}
			
			return html;
		}
		
		int enc = 8, itemid = 0;
		int value = 5000000;
		if (s.equals("A")) {// 7파크
			itemid = 60126;
		} else if (s.equals("B")) {// 7파이
			itemid = 60128;
		} else if (s.equals("C")) {// 8파크
			itemid = 60127;
		} else if (s.equals("D")) {// 8파이
			itemid = 60129;
		} else {
			return html;
		}
		
		if (itemid == 60127 || itemid == 60129) {
			enc = 9;
			value = 10000000;
		}
		
		if (pc.getInventory().checkItem(40308, value)) {
			boolean ck = false;
			for (int f : 피어스아이템) {
				for (L1ItemInstance item : pc.getInventory().findItemsId(f)) {
					if (item.getEnchantLevel() == enc && !item.isEquipped()) {
						ck = true;
						pc.getInventory().consumeItem(40308, value);
						pc.getInventory().deleteItem(item);
						L1ItemInstance t = pc.getInventory().storeItem(itemid, 1);
						pc.sendPackets(new S_ServerMessage(403, t.getLogName()));   // %0를 손에 넣었습니다.
						html = "";
						break;
					}
				}
				
				if (ck) {
					break;
				}
			}
			
			if (!ck) {
				html = "piers04";
			}
			
		} else {
			html = "piers04";
		}
		
		return html;
	}

	private static int[] 배송관련아이템 = new int[] { 60009, 60010, 60011, 60012,
			60013, 60014, 60015, 60016, 60017, 60018, 60019, 60020, 60021,
			60022, 60023, 60024 };

	
	private String 모포(String s, L1PcInstance pc) {

		String html = null;
		int count = 0, itemid = 0, newitem = 0, clockcount = 0;
		if (s.equalsIgnoreCase("a")) {
			itemid = 60011;
			count = 100;
			newitem = 60019;
			clockcount = 1;
			// 난쟁이 포도 주스 100개 배송시간9시간 엉뚱한시계 1개 사용가능
		} else if (s.equalsIgnoreCase("b")) {
			itemid = 60012;
			count = 200;
			newitem = 60020;
			clockcount = 2;
			// 주스 200개 배송시간17시간 엉뚱한시계 2개 사용가능
		} else if (s.equalsIgnoreCase("c")) {
			itemid = 60013;
			count = 300;
			newitem = 60021;
			clockcount = 3;
			// 주스 300개 배송시간24시간 엉뚱한시계 3개 사용가능
		} else if (s.equalsIgnoreCase("d")) {
			itemid = 60014;
			count = 100;
			newitem = 60022;
			clockcount = 1;
			// 난쟁이 포도 진액 100개 배송시간9시간 엉뚱한시계 1개 사용가능
		} else if (s.equalsIgnoreCase("e")) {
			itemid = 60015;
			count = 200;
			newitem = 60023;
			clockcount = 2;
			// 난쟁이 포도 진액 200개 배송시간17시간 엉뚱한시계 2개 사용가능
		} else if (s.equalsIgnoreCase("f")) {
			itemid = 60016;
			count = 300;
			newitem = 60024;
			clockcount = 3;
			// 난쟁이 포도 진액 300개 배송시간24시간 엉뚱한시계 3개 사용가능
		}
		
		if (count <= 0) {
			return html;
		}
		
		if (!pc.getInventory().checkItem(40308, 30 * count)) {
			html = "mopo5";
		} else if (배송관련체크(pc)) {
			html = "mopo4";
		} else {
			pc.getInventory().consumeItem(40308, 30 * count);
			L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
			pc.sendPackets(new S_ServerMessage(143, "모포", item.getLogName()), true);
			// pc.sendPackets(new
			// S_SystemMessage(item.getLogName()+" 1개를 얻었습니다."));
			DeliverySystem.getInstance().add(pc.getName(), item.getId(), newitem, item.getEndTime(), 1, clockcount);// 7일
			html = "";
		}
		
		return html;
	}

	private String 킬톤(L1PcInstance pc) {
		String html = null;

		if (!pc.getInventory().checkItem(40308, 500000)) {
			html = "killton3";
		} else if (배송관련체크(pc)) {
			html = "killton4";
		} else {
			pc.getInventory().consumeItem(40308, 500000);
			L1ItemInstance item = pc.getInventory().storeItem(60010, 1);
			pc.sendPackets(new S_ServerMessage(143, "킬톤", item.getLogName()), true);
			// pc.sendPackets(new
			// S_SystemMessage(item.getLogName()+" 1개를 얻었습니다."));
			DeliverySystem.getInstance().add(pc.getName(), item.getId(), 60018, item.getEndTime(), 1, 0);// 7일
			html = "killton2";
		}
		
		return html;
	}

	private String 메린(L1PcInstance pc) {
		String html = null;

		if (!pc.getInventory().checkItem(40308, 500000)) {
			html = "merin3";
		} else if (배송관련체크(pc)) {
			html = "merin4";
		} else {
			pc.getInventory().consumeItem(40308, 500000);
			L1ItemInstance item = pc.getInventory().storeItem(60009, 1);
			pc.sendPackets(new S_ServerMessage(143, "메린", item.getLogName()), true);
			// pc.sendPackets(new
			// S_SystemMessage(item.getLogName()+" 1개를 얻었습니다."));
			DeliverySystem.getInstance().add(pc.getName(), item.getId(), 60017, item.getEndTime(), 1, 0);// 7일
			html = "merin2";
		}
		
		return html;
	}

	private boolean 배송관련체크(L1PcInstance pc) {
		for (int d : 배송관련아이템) {
			if (pc.getInventory().checkItem(d))
				return true;
		}
		
		return false;
	}

	private String helper(String s, L1PcInstance pc) {

		String htmlid = null;
		boolean tel = false;
		/** Back **/
		if (s.equalsIgnoreCase("0")) {
			htmlid = "lowlvS1";
			/** 게렝에게 텔레포트 **/
		} else if (s.equalsIgnoreCase("a")) {
			pc.dx = 32562;
			pc.dy = 33082;
			pc.dm = 0;
			tel = true;
			/** 라우풀 신전 **/
		} else if (s.equalsIgnoreCase("b")) {
			pc.dx = 33118;
			pc.dy = 32933;
			pc.dm = 4;
			tel = true;
			/** 카오틱 신전 **/
		} else if (s.equalsIgnoreCase("c")) {
			pc.dx = 32885;
			pc.dy = 32652;
			pc.dm = 4;
			tel = true;
			/** 군터에게로 **/
		} else if (s.equalsIgnoreCase("j")) {
			pc.dx = 32671;
			pc.dy = 32791;
			pc.dm = 3;
			tel = true;
			/** 정령마법 알려주는 린다에게 **/
		} else if (s.equalsIgnoreCase("d")) {
			pc.dx = 32790;
			pc.dy = 32821;
			pc.dm = 75;
			tel = true;
			/** 상아탑 정령마법 수련실 **/
		} else if (s.equalsIgnoreCase("e")) {
			pc.dx = 32789;
			pc.dy = 32850;
			pc.dm = 75;
			tel = true;
			/** 상아탑 엘리온에게 **/
		} else if (s.equalsIgnoreCase("f")) {
			pc.dx = 32749;
			pc.dy = 32848;
			pc.dm = 76;
			tel = true;
			/** 침욱의 동굴 세디아에게 **/
		} else if (s.equalsIgnoreCase("g")) {
			if (!pc.isDarkelf()) {
				htmlid = "lowlv40";
			} else {
				pc.dx = 32879;
			}
			pc.dy = 32905;
			pc.dm = 304;
			tel = true;
			/** 용기사 제파르에게 **/
		} else if (s.equalsIgnoreCase("h")) {
			if (!pc.isDragonknight()) {
				htmlid = "lowlv41";
			} else {
				pc.dx = 32825;
			}
			pc.dy = 32873;
			pc.dm = 1001;
			tel = true;
			/** 환술사 스비엘에게 **/
		} else if (s.equalsIgnoreCase("i")) {
			if (!pc.isDragonknight()) {
				htmlid = "lowlv42";
			} else {
				pc.dx = 32758;
			}
			pc.dy = 32883;
			pc.dm = 1000;
			tel = true;
			/** 다른 조언을 듣는다. **/
		} else if (s.equalsIgnoreCase("1")) {
			htmlid = "lowlv" + (14 + _random.nextInt(2));
			/** 상아탑 장비 다시 받기 **/
		} else if (s.equalsIgnoreCase("2")) {
			int[] itemid = { 7, 35, 48, 73, 105, 120, 147, 156, 174, 175, 224, 20028, 20082, 20126, 20173, 20206, 20232 };
			for (int d : itemid) {
				if (pc.getInventory().checkItem(d)) {
					return "lowlv17";
				}
			}
			GiveItem(pc);
			htmlid = "lowlv16";
			/** 상아탑 젤 사기 **/
		} else if (s.equalsIgnoreCase("3") || s.equalsIgnoreCase("8")) {
			if (pc.getInventory().checkItem(40308, 1000)) {
				pc.getInventory().storeItem(5000502, 1);
				htmlid = "lowlv18";
				pc.getInventory().consumeItem(40308, 1000);
			} else {
				htmlid = "lowlv20";
			}
			/** 상아탑 데이 사기 **/
		} else if (s.equalsIgnoreCase("4") || s.equalsIgnoreCase("L")) {
			if (pc.getInventory().checkItem(40308, 1500)) {
				pc.getInventory().storeItem(5000501, 1);
				htmlid = "lowlv19";
				pc.getInventory().consumeItem(40308, 1500);
			} else {
				htmlid = "lowlv20";
			}
			/** 상아탑 묘약 사기 **/
		} else if (s.equalsIgnoreCase("5")) {
			if (pc.getInventory().checkItem(40308, 250)) {
				pc.getInventory().storeItem(60000, 1);
				htmlid = "lowlv21";
				pc.getInventory().consumeItem(40308, 250);
			} else {
				htmlid = "lowlv20";
			}
			/** 상아탑 마법 주머니 사기 **/
		} else if (s.equalsIgnoreCase("6")) {
			if (pc.getInventory().checkItem(60001, 2000)) {
				htmlid = "lowlv23";
			} else {
				if (pc.getInventory().checkItem(40308, 2000)) {
					pc.getInventory().storeItem(60001, 1);
					htmlid = "lowlv22";
					pc.getInventory().consumeItem(40308, 2000);
				} else {
					htmlid = "lowlv20";
				}
			}
			/** 상아탑 장신구 **/
		} /*
		 * else if (s.equalsIgnoreCase("k")){ if(pc.getLevel() >= 35){
		 * if(pc.getInventory().checkItem(20282) ||
		 * pc.getInventory().checkItem(420010)) htmlid = "lowlv45"; else{
		 * pc.getInventory().storeItem(20282, 1);
		 * pc.getInventory().storeItem(420010, 1); htmlid = "lowlv43"; } }else
		 * htmlid = "lowlv44"; }
		 */
		
		if (tel) {
			pc.dh = pc.getMoveState().getHeading();
			pc.setTelType(7);
			pc.sendPackets(new S_SabuTell(pc));
		}
		
		return htmlid;
	}

	public void GiveItem(L1PcInstance pc) {
		if (pc.isKnight()) {
			createNewItem2(pc, 35, 1, 0); // 상아탑의 한손검
			createNewItem2(pc, 48, 1, 0); // 상아탑의 양손검
			createNewItem2(pc, 147, 1, 0); // 상아탑의 도끼
			createNewItem2(pc, 7, 1, 0); // 상아탑의 단검
			createNewItem2(pc, 105, 1, 0); // 상아탑의 창
		} else if (pc.isDragonknight()) {
			createNewItem2(pc, 35, 1, 0); // 상아탑의 한손검
			createNewItem2(pc, 48, 1, 0); // 상아탑의 양손검
			createNewItem2(pc, 147, 1, 0); // 상아탑의 도끼
		} else if (pc.isCrown()) {
			createNewItem2(pc, 7, 1, 0); // 상아탑의 단검
			createNewItem2(pc, 48, 1, 0); // 상아탑의 양손검
			createNewItem2(pc, 147, 1, 0); // 상아탑의 도끼
		} else if (pc.isWizard()) {
			createNewItem2(pc, 7, 1, 0); // 상아탑의 단검
			createNewItem2(pc, 224, 1, 0); // 상아탑의 지팡이
		} else if (pc.isIllusionist()) {
			createNewItem2(pc, 174, 1, 0); // 상아탑의 석궁
			createNewItem2(pc, 147, 1, 0); // 상아탑의 도끼
			createNewItem2(pc, 224, 1, 0); // 상아탑의 지팡이
		} else if (pc.isElf()) {
			createNewItem2(pc, 35, 1, 0); // 상아탑의 한손검
			createNewItem2(pc, 174, 1, 0); // 상아탑의 석궁
			createNewItem2(pc, 175, 1, 0); // 상아탑의 활
		} else if (pc.isDarkelf()) {
			createNewItem2(pc, 35, 1, 0); // 상아탑의 한손검
			createNewItem2(pc, 174, 1, 0); // 상아탑의 석궁
			createNewItem2(pc, 7, 1, 0); // 상아탑의 단검
			createNewItem2(pc, 156, 1, 0); // 상아탑의 크로우
			createNewItem2(pc, 73, 1, 0); // 상아탑의 이도류
		}
		
		createNewItem2(pc, 20028, 1, 0);
		createNewItem2(pc, 20126, 1, 0);
		createNewItem2(pc, 20173, 1, 0);
		createNewItem2(pc, 20206, 1, 0);
		createNewItem2(pc, 20232, 1, 0);
	}

	private boolean createNewItem2(L1PcInstance pc, int item_id, int count, int EnchantLevel) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(true);
			
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance().getInventory(pc.getX(), pc.getY(), pc.getMapId()).storeItem(item);
			}
			
			pc.sendPackets(new S_ServerMessage(403, item.getLogName()));  // %0를 손에 넣었습니다.
			return true;
		} else {
			return false;
		}
	}

	private boolean itemCkStore(L1PcInstance pc, int ck, int store) {
		if (pc.getInventory().checkItem(ck, 1)) {
			pc.getInventory().consumeItem(ck, 1);
			pc.getInventory().storeItem(store, 1);
			return true;
		}
		
		return false;
	}

	private boolean itemCkStore(L1PcInstance pc, int ck, int store, int ckcount) {
		if (pc.getInventory().checkItem(ck, ckcount)) {
			pc.getInventory().consumeItem(ck, ckcount);
			pc.getInventory().storeItem(store, 1);
			return true;
		}
		
		return false;
	}

	private boolean checkmemo(L1PcInstance pc) {
		if (pc.getInventory().checkItem(437031)) {
			pc.getInventory().consumeItem(437031, 1);
			new L1SkillUse().handleCommands(pc, L1SkillId.FEATHER_BUFF_A, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
			return true;
		} else if (pc.getInventory().checkItem(437032)) {
			pc.getInventory().consumeItem(437032, 1);
			new L1SkillUse().handleCommands(pc, L1SkillId.FEATHER_BUFF_B, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
			return true;
		} else if (pc.getInventory().checkItem(437033)) {
			pc.getInventory().consumeItem(437033, 1);
			new L1SkillUse().handleCommands(pc, L1SkillId.FEATHER_BUFF_C, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
			return true;
		} else if (pc.getInventory().checkItem(437034)) {
			pc.getInventory().consumeItem(437034, 1);
			new L1SkillUse().handleCommands(pc, L1SkillId.FEATHER_BUFF_D, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
			return true;
		}
		
		return false;
	}

	private String karmaLevelToHtmlId(int level) {
		if (level == 0 || level < -7 || 7 < level) {
			return "";
		}
		
		String htmlid = "";
		if (0 < level) {
			htmlid = "vbk" + level;
		} else if (level < 0) {
			htmlid = "vyk" + Math.abs(level);
		}
		
		return htmlid;
	}

	private String watchUb(L1PcInstance pc, int npcId) {
		pc.sendPackets(new S_SystemMessage("현제 관람모드는 지원하지 않습니다."));
		// L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		// L1Location loc = ub.getLocation();
		/*
		 * if (pc.getInventory().consumeItem(L1ItemId.ADENA, 100)) { try {
		 * pc.save(); pc.beginGhost(loc.getX(), loc.getY(), (short)
		 * loc.getMapId(), true); } catch (Exception e) { _log.log(Level.SEVERE,
		 * e.getLocalizedMessage(), e); } } else { pc.sendPackets(new
		 * S_ServerMessage(189)); }
		 */
		return "";
	}

	private String enterUb(L1PcInstance pc, int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		if (ub == null || pc == null)
			return "";
		try {
			if (!ub.isActive() || !ub.canPcEnter(pc)) {
				return "colos2";
			}
		} catch (Exception e) {
			// System.out.println("무한대전 잘못된 엔피시번호 :"+npcId);
		}
		
		if (ub.isNowUb()) {
			return "colos1";
		}
		
		if (ub.getMembersCount() >= ub.getMaxPlayer()) {
			return "colos4";
		}

		ub.addMember(pc);
		L1Location loc = ub.getLocation().randomLocation(10, false);
		L1Teleport.teleport(pc, loc.getX(), loc.getY(), ub.getMapId(), 5, true);
		return "";
	}

	private String enterHauntedHouse(L1PcInstance pc) {
		// 렙, 플레이중, 초과, 이미있냐
		if (pc.getLevel() < 30) {
			pc.sendPackets(new S_ServerMessage(1273, "30", "99"));
			return "";
		}
		
		if (GhostHouse.getInstance().isPlayingNow()) {
			pc.sendPackets(new S_ServerMessage(1182));
			return "";
		}
		
		if (GhostHouse.getInstance().getEnterMemberCount() >= 10) {
			pc.sendPackets(new S_ServerMessage(1184));
			return "";
		}
		
		if (GhostHouse.getInstance().isEnterMember(pc)) {
			pc.sendPackets(new S_ServerMessage(1254));
			return "";
		}
		
		if (DeathMatch.getInstance().isEnterMember(pc)) {
			DeathMatch.getInstance().removeEnterMember(pc);
		}
		
		if (PetRacing.getInstance().isEnterMember(pc)) {
			PetRacing.getInstance().removeEnterMember(pc);
		}
		
		GhostHouse.getInstance().addEnterMember(pc);
		return "";
	}

	private String enterPetRacing(L1PcInstance pc) {
		if (pc.getLevel() < 30) {
			pc.sendPackets(new S_ServerMessage(1273, "30", "99"));
			return "";
		}
		
		if (PetRacing.getInstance().getEnterMemberCount() >= 10) {
			pc.sendPackets(new S_SystemMessage("이미 펫레이싱이 포화 상태라네."));
			return "";
		}
		
		if (PetRacing.getInstance().isEnterMember(pc)) {
			pc.sendPackets(new S_ServerMessage(1254));
			return "";
		}
		
		if (GhostHouse.getInstance().isEnterMember(pc)) {
			GhostHouse.getInstance().removeEnterMember(pc);
		}
		
		if (DeathMatch.getInstance().isEnterMember(pc)) {
			DeathMatch.getInstance().removeEnterMember(pc);
		}
		
		PetRacing.getInstance().addMember(pc);
		return "";
	}

	private void poly(LineageClient clientthread, int polyId) {
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_EARTH_DRAGON)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_FIRE_DRAGON)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			pc.sendPackets(new S_ServerMessage(1384));
			return;
		}
		
		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_EARTH_DRAGON)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_FIRE_DRAGON)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			pc.sendPackets(new S_ServerMessage(1384));			
			return;
		}
		
		if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
			pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC);
		} else {
			pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
	}

	private void polyByKeplisha(LineageClient clientthread, int polyId) {
		L1PcInstance pc = clientthread.getActiveChar();
		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_EARTH_DRAGON)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_FIRE_DRAGON)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON)) {
			pc.sendPackets(new S_ServerMessage(1384));
			return;
		}
				
		if (pc.getInventory().checkItem(L1ItemId.ADENA, 100)) {
			pc.getInventory().consumeItem(L1ItemId.ADENA, 100);
			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_KEPLISHA);
		} else {
			pc.sendPackets(new S_ServerMessage(337, "$4"));
		}
		
	}

	private String sellHouse(L1PcInstance pc, int objectId, int npcId) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan == null) {
			return "";
		}
		
		int houseId = clan.getHouseId();
		if (houseId == 0) {
			return "";
		}
		
		L1House house = HouseTable.getInstance().getHouseTable(houseId);
		int keeperId = house.getKeeperId();
		if (npcId != keeperId) {
			return "";
		}
		
		if (!pc.isCrown()) {
			pc.sendPackets(new S_ServerMessage(518));
			return "";
		}
		
		if (pc.getId() != clan.getLeaderId()) {
			pc.sendPackets(new S_ServerMessage(518));
			return "";
		}
		
		if (house.isOnSale()) {
			return "agonsale";
		}

		pc.sendPackets(new S_SellHouse(objectId, String.valueOf(houseId)));
		return null;
	}

	private void openCloseDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
		// int doorId = 0;
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					L1DoorInstance door1 = null;
					L1DoorInstance door2 = null;
					L1DoorInstance door3 = null;
					L1DoorInstance door4 = null;
					for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
						if (door.getKeeperId() == keeperId) {
							if (door1 == null) {
								door1 = door;
								continue;
							}
							
							if (door2 == null) {
								door2 = door;
								continue;
							}
							
							if (door3 == null) {
								door3 = door;
								continue;
							}
							
							if (door4 == null) {
								door4 = door;
								break;
							}
						}
					}
					
					if (door1 != null) {
						if (s.equalsIgnoreCase("open")) {
							door1.open();
						} else if (s.equalsIgnoreCase("close")) {
							door1.close();
						}
					}
					
					if (door2 != null) {
						if (s.equalsIgnoreCase("open")) {
							door2.open();
						} else if (s.equalsIgnoreCase("close")) {
							door2.close();
						}
					}
					
					if (door3 != null) {
						if (s.equalsIgnoreCase("open")) {
							door3.open();
						} else if (s.equalsIgnoreCase("close")) {
							door3.close();
						}
					}
					
					if (door4 != null) {
						if (s.equalsIgnoreCase("open")) {
							door4.open();
						} else if (s.equalsIgnoreCase("close")) {
							door4.close();
						}
					}
				}
			}
		}
	}

	
	private void openCloseGate(L1PcInstance pc, int keeperId, boolean isOpen) {
		boolean isNowWar = false;
		int pcCastleId = 0;
		
		if (pc.getClanid() != 0) {
			L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
			if (clan != null) {
				pcCastleId = clan.getCastleId();
			}
		}
		
		if (keeperId == 70656 || keeperId == 70549 || keeperId == 70985) {
			if (isExistDefenseClan(L1CastleLocation.KENT_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.KENT_CASTLE_ID) {
					return;
				}
			}
			
			isNowWar = WarTimeController.getInstance().isNowWar(L1CastleLocation.KENT_CASTLE_ID);
		} else if (keeperId == 70600) { // OT
			if (isExistDefenseClan(L1CastleLocation.OT_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.OT_CASTLE_ID) {
					return;
				}
			}
			
			isNowWar = WarTimeController.getInstance().isNowWar(L1CastleLocation.OT_CASTLE_ID);
		} else if (keeperId == 70778 || keeperId == 70987 || keeperId == 70687) {
			if (isExistDefenseClan(L1CastleLocation.WW_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.WW_CASTLE_ID) {
					return;
				}
			}
			
			isNowWar = WarTimeController.getInstance().isNowWar(L1CastleLocation.WW_CASTLE_ID);
		} else if (keeperId == 70817 || keeperId == 70800 || keeperId == 70988 || keeperId == 70990 || keeperId == 70989 || keeperId == 70991) {
			if (isExistDefenseClan(L1CastleLocation.GIRAN_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.GIRAN_CASTLE_ID) {
					return;
				}
			}
			
			isNowWar = WarTimeController.getInstance().isNowWar(L1CastleLocation.GIRAN_CASTLE_ID);
		} else if (keeperId == 70863 || keeperId == 70992 || keeperId == 70862) {
			if (isExistDefenseClan(L1CastleLocation.HEINE_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.HEINE_CASTLE_ID) {
					return;
				}
			}
			
			isNowWar = WarTimeController.getInstance().isNowWar(L1CastleLocation.HEINE_CASTLE_ID);
		} else if (keeperId == 70995 || keeperId == 70994 || keeperId == 70993) {
			if (isExistDefenseClan(L1CastleLocation.DOWA_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.DOWA_CASTLE_ID) {
					return;
				}
			}
			
			isNowWar = WarTimeController.getInstance().isNowWar(L1CastleLocation.DOWA_CASTLE_ID);
		} else if (keeperId == 70996) {
			if (isExistDefenseClan(L1CastleLocation.ADEN_CASTLE_ID)) {
				if (pcCastleId != L1CastleLocation.ADEN_CASTLE_ID) {
					return;
				}
			}
			
			isNowWar = WarTimeController.getInstance().isNowWar(L1CastleLocation.ADEN_CASTLE_ID);
		} else {
			return;
		}

		// if (isNowWar) {
		// pc.sendPackets(new S_SystemMessage("\\fY공성중에는 성문을 제어할 수 없습니다."));
		// return;
		// }

		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (door.getKeeperId() == keeperId) {
				if (isNowWar && door.getMaxHp() > 1) {
				} else {
					if (isOpen) {
						door.open();
					} else {
						door.close();
					}
				}
			}
		}
	}

	private boolean isExistDefenseClan(int castleId) {
		boolean isExistDefenseClan = false;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (castleId == clan.getCastleId()) {
				isExistDefenseClan = true;
				break;
			}
		}
		
		return isExistDefenseClan;
	}

	private void expelOtherClan(L1PcInstance clanPc, int keeperId) {
		int houseId = 0;
		for (L1House house : HouseTable.getInstance().getHouseTableList()) {
			if (house.getKeeperId() == keeperId) {
				houseId = house.getHouseId();
			}
		}
		
		if (houseId == 0) {
			return;
		}

		int[] loc = new int[3];
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (L1HouseLocation.isInHouseLoc(houseId, pc.getX(), pc.getY(), pc.getMapId()) && clanPc.getClanid() != pc.getClanid() && !pc.isGm()) {
				loc = L1HouseLocation.getHouseTeleportLoc(houseId, 0);
				if (pc != null) {
					L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
				}
			}
		}
		
		loc = null;
	}

	private void payFee(L1PcInstance pc, L1NpcInstance npc) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					if (pc.getInventory().checkItem(L1ItemId.ADENA, 100000)) {
						pc.getInventory().consumeItem(L1ItemId.ADENA, 100000);
						TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
						Calendar cal = Calendar.getInstance(tz);
						cal.add(Calendar.DATE, Config.HOUSE_TAX_INTERVAL);
						cal.set(Calendar.MINUTE, 0);
						cal.set(Calendar.SECOND, 0);
						house.setTaxDeadline(cal);
						HouseTable.getInstance().updateHouse(house);
					} else {
						pc.sendPackets(new S_ServerMessage(189), true);
					}
				}
			}
		}
	}

	
	private String[] makeHouseTaxStrings(L1PcInstance pc, L1NpcInstance npc) {
		String name = npc.getNpcTemplate().get_name();
		String[] result;
		result = new String[] { name, "100000", "1", "1", "00" };
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				int keeperId = house.getKeeperId();
				if (npc.getNpcTemplate().get_npcId() == keeperId) {
					Calendar cal = house.getTaxDeadline();
					if (cal.getTimeInMillis() - Calendar.getInstance().getTimeInMillis() < 3600000 * 24 * 20) {
						int month = cal.get(Calendar.MONTH) + 1;
						int day = cal.get(Calendar.DATE);
						int hour = cal.get(Calendar.HOUR_OF_DAY);
						result = new String[] { name, "100000", String.valueOf(month), String.valueOf(day), String.valueOf(hour) };
					} else {
						return null;
					}
				}
			}
		}
		
		return result;
	}

	
	private String[] makeWarTimeStrings(int castleId) {
		L1Castle castle = CastleTable.getInstance().getCastleTable(castleId);
		if (castle == null) {
			return null;
		}
		
		Calendar warTime = castle.getWarTime();
		int year = warTime.get(Calendar.YEAR);
		int month = warTime.get(Calendar.MONTH) + 1;
		int day = warTime.get(Calendar.DATE);
		int hour = warTime.get(Calendar.HOUR_OF_DAY);
		int minute = warTime.get(Calendar.MINUTE);
		String[] result;
		
		if (castleId == L1CastleLocation.OT_CASTLE_ID) {
			result = new String[] { String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute) };
		} else {
			result = new String[] { "", String.valueOf(year), String.valueOf(month), String.valueOf(day), String.valueOf(hour), String.valueOf(minute) };
		}
		
		return result;
	}

	private String getYaheeAmulet(L1PcInstance pc, L1NpcInstance npc, String s) {
		int[] amuletIdList = { 20358, 20359, 20360, 20361, 20362, 20363, 20364, 20365 };
		int amuletId = 0;
		L1ItemInstance item = null;
		String htmlid = null;
		
		if (s.equalsIgnoreCase("1")) {
			if (pc.getKarmaLevel() <= -1) {
				amuletId = amuletIdList[0];
			}
		} else if (s.equalsIgnoreCase("2")) {
			if (pc.getKarmaLevel() <= -2) {
				amuletId = amuletIdList[1];
			}
		} else if (s.equalsIgnoreCase("3")) {
			if (pc.getKarmaLevel() <= -3) {
				amuletId = amuletIdList[2];
			}
		} else if (s.equalsIgnoreCase("4")) {
			if (pc.getKarmaLevel() <= -4) {
				amuletId = amuletIdList[3];
			}
		} else if (s.equalsIgnoreCase("5")) {
			if (pc.getKarmaLevel() <= -5) {
				amuletId = amuletIdList[4];
			}
		} else if (s.equalsIgnoreCase("6")) {
			if (pc.getKarmaLevel() <= -6) {
				amuletId = amuletIdList[5];
			}
		} else if (s.equalsIgnoreCase("7")) {
			if (pc.getKarmaLevel() <= -7) {
				amuletId = amuletIdList[6];
			}
		} else if (s.equalsIgnoreCase("8")) {
			if (pc.getKarmaLevel() <= -8) {
				amuletId = amuletIdList[7];
			}
		}
		
		if (amuletId != 0) {
			item = pc.getInventory().storeItem(amuletId, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			for (int id : amuletIdList) {
				if (id == amuletId) {
					break;
				}
				
				if (pc.getInventory().checkItem(id)) {
					pc.getInventory().consumeItem(id, 1);
				}
			}
			
			htmlid = "";
		}
		
		amuletIdList = null;
		
		return htmlid;
	}

	private String getBarlogEarring(L1PcInstance pc, L1NpcInstance npc, String s) {
		int[] earringIdList = { 21020, 21021, 21022, 21023, 21024, 21025, 21026, 21027 };
		int earringId = 0;
		L1ItemInstance item = null;
		String htmlid = null;
		
		if (s.equalsIgnoreCase("1")) {
			if (pc.getKarmaLevel() >= 1) {
				earringId = earringIdList[0];
			}
		} else if (s.equalsIgnoreCase("2")) {
			if (pc.getKarmaLevel() >= 2) {
				earringId = earringIdList[1];
			}
		} else if (s.equalsIgnoreCase("3")) {
			if (pc.getKarmaLevel() >= 3) {
				earringId = earringIdList[2];
			}
		} else if (s.equalsIgnoreCase("4")) {
			if (pc.getKarmaLevel() >= 4) {
				earringId = earringIdList[3];
			}
		} else if (s.equalsIgnoreCase("5")) {
			if (pc.getKarmaLevel() >= 5) {
				earringId = earringIdList[4];
			}
		} else if (s.equalsIgnoreCase("6")) {
			if (pc.getKarmaLevel() >= 6) {
				earringId = earringIdList[5];
			}
		} else if (s.equalsIgnoreCase("7")) {
			if (pc.getKarmaLevel() >= 7) {
				earringId = earringIdList[6];
			}
		} else if (s.equalsIgnoreCase("8")) {
			if (pc.getKarmaLevel() >= 8) {
				earringId = earringIdList[7];
			}
		}
		
		if (earringId != 0) {
			item = pc.getInventory().storeItem(earringId, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			for (int id : earringIdList) {
				if (id == earringId) {
					break;
				}
				
				if (pc.getInventory().checkItem(id)) {
					pc.getInventory().consumeItem(id, 1);
				}
			}
			
			htmlid = "";
		}
		
		earringIdList = null;
		
		
		return htmlid;
	}

	private String[] makeUbInfoStrings(int npcId) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npcId);
		if (ub == null) {
			return null;
		}
		
		return ub.makeUbInfoStrings();
	}

	private String talkToDimensionDoor(L1PcInstance pc, L1NpcInstance npc, String s) {
		String htmlid = "";
		int protectionId = 0;
		int sealId = 0;
		int locX = 0;
		int locY = 0;
		short mapId = 0;
		if (npc.getNpcTemplate().get_npcId() == 80059) {
			protectionId = 40909;
			sealId = 40913;
			locX = 32773;
			locY = 32835;
			mapId = 607;
		} else if (npc.getNpcTemplate().get_npcId() == 80060) {
			protectionId = 40912;
			sealId = 40916;
			locX = 32757;
			locY = 32842;
			mapId = 606;
		} else if (npc.getNpcTemplate().get_npcId() == 80061) {
			protectionId = 40910;
			sealId = 40914;
			locX = 32830;
			locY = 32822;
			mapId = 604;
		} else if (npc.getNpcTemplate().get_npcId() == 80062) {
			protectionId = 40911;
			sealId = 40915;
			locX = 32835;
			locY = 32822;
			mapId = 605;
		}

		if (s.equalsIgnoreCase("a")) {
			L1Teleport.teleport(pc, locX, locY, mapId, 5, true);
			htmlid = "";
		} else if (s.equalsIgnoreCase("b")) {
			L1ItemInstance item = pc.getInventory().storeItem(protectionId, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			htmlid = "";
		} else if (s.equalsIgnoreCase("c")) {
			htmlid = "wpass07";
		} else if (s.equalsIgnoreCase("d")) {
			if (pc.getInventory().checkItem(sealId)) {
				L1ItemInstance item = pc.getInventory().findItemId(sealId);
				pc.getInventory().consumeItem(sealId, item.getCount());
			}
		} else if (s.equalsIgnoreCase("e")) {
			htmlid = "";
		} else if (s.equalsIgnoreCase("f")) {
			if (pc.getInventory().checkItem(protectionId)) {
				pc.getInventory().consumeItem(protectionId, 1);
			}
			
			if (pc.getInventory().checkItem(sealId)) {
				L1ItemInstance item = pc.getInventory().findItemId(sealId);
				pc.getInventory().consumeItem(sealId, item.getCount());
			}
			
			htmlid = "";
		}
		
		return htmlid;
	}

	private boolean isNpcSellOnly(L1NpcInstance npc) {
		int npcId = npc.getNpcTemplate().get_npcId();
		String npcName = npc.getNpcTemplate().get_name();
		if (npcId == 70027 || "아덴상단".equals(npcName)) {
			return true;
		}
		
		return false;
	}

	private void getBloodCrystalByKarma(L1PcInstance pc, L1NpcInstance npc, String s) {
		L1ItemInstance item = null;

		if (s.equalsIgnoreCase("1")) {
			pc.addKarma((int) (500 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			pc.sendPackets(new S_ServerMessage(1081), true);
		} else if (s.equalsIgnoreCase("2")) {
			pc.addKarma((int) (5000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 10);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			pc.sendPackets(new S_ServerMessage(1081), true);
		} else if (s.equalsIgnoreCase("3")) {
			pc.addKarma((int) (50000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40718, 100);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			pc.sendPackets(new S_ServerMessage(1081), true);
		}
	}

	private void getSoulCrystalByKarma(L1PcInstance pc, L1NpcInstance npc, String s) {
		L1ItemInstance item = null;

		if (s.equalsIgnoreCase("1")) {
			pc.addKarma((int) (-500 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 1);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			pc.sendPackets(new S_ServerMessage(1080), true);
		} else if (s.equalsIgnoreCase("2")) {
			pc.addKarma((int) (-5000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 10);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			pc.sendPackets(new S_ServerMessage(1080), true);
		} else if (s.equalsIgnoreCase("3")) {
			pc.addKarma((int) (-50000 * Config.RATE_KARMA));
			item = pc.getInventory().storeItem(40678, 100);
			if (item != null) {
				pc.sendPackets(new S_ServerMessage(143, npc.getNpcTemplate().get_name(), item.getLogName()), true);
			}
			
			pc.sendPackets(new S_ServerMessage(1080), true);
		}
		
	}

	private void StatInitialize(L1PcInstance pc) {
		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);
		l1skilluse = null;
		pc.getInventory().takeoffEquip(945);
		pc.sendPackets(new S_CharVisualUpdate(pc), true);
		pc.setReturnStat(pc.getExp());
		pc.setReturnStatus(1);
		pc.sendPackets(new S_SPMR(pc), true);
		pc.sendPackets(new S_OwnCharAttrDef(pc), true);
		pc.sendPackets(new S_OwnCharStatus2(pc), true);
		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START), true);
		
		try {
			pc.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		
	}

	private void GiveSoldier(L1PcInstance pc, int objid) {
		if (pc.getPetList().size() > 0) {
			pc.sendPackets(new S_CloseList(pc.getId()), true);
			return;
		}
		
		RealTime r = new RealTime();
		long time = r.getSeconds();

		ArrayList<L1CharSoldier> list = CharSoldierTable.getInstance().getCharSoldier(pc.getId(), time);

		L1CharSoldier t;

		int d = CharSoldierTable.getInstance().SoldierCalculate(pc.getId());
		if (d > 0 && list.size() == 0) {
			pc.sendPackets(new S_NPCTalkReturn(objid, "colbert2"), true);
			return;
		} else if (d == 0) {
			pc.sendPackets(new S_NPCTalkReturn(objid, "colbert3"), true);
			return;
		}

		boolean ck = false;
		for (int i = 0; i < list.size(); i++) {
			t = list.get(i);
			int a = t.getSoldierNpc();
			int b = t.getSoldierCount();
			L1Npc npcTemp = NpcTable.getInstance().getTemplate(a);
			
			@SuppressWarnings("unused")
			L1SummonInstance summon = null;
			for (int c = 0; c < b; c++) {
				int petcost = 0;
				for (Object pet : pc.getPetList()) {
					petcost += ((L1NpcInstance) pet).getPetcost();
				}
				
				int charisma = pc.getAbility().getTotalCha();
				if (pc.isWizard()) {
					charisma += 6;
				} else {
					charisma += 12;
				}
				
				charisma -= petcost;
				if (charisma >= 6) {
					summon = new L1SummonInstance(npcTemp, pc, true);
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(319), true);
					ck = true;
					break;
				}
			}
			
			CharSoldierTable.getInstance().delCharCastleSoldier(pc.getId(), t.getSoldierTime());
		}
		
		t = null;
		pc.sendPackets(new S_CloseList(pc.getId()), true);
	}

	private void castleGateStatus(L1PcInstance pc, int objid) {
		String htmlid = null;
		String doorStatus = null;
		String[] htmldata = null;
		String[] doorName = null;
		String doorCrack = null;
		int[] doornpc = null;

		switch (pc.getClan().getCastleId()) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
			htmlid = "orville5";
			doornpc = new int[] { 2031, 2032, 2033, 2034, 2035, 2030 };
			doorName = new String[] { "$1399", "$1400", "$1401", "$1402", "$1403", "$1386" };
			htmldata = new String[12];
			break;
		case 5:
		case 6:
			htmlid = "potempin5";
			doornpc = new int[] { 2051, 2052, 2050 }; // 남문, 동문, 현관문
			doorName = new String[] { "$1399", "$1603", "$1386" };
			htmldata = new String[4];
			break;
		}

		for (int i = 0; i < doornpc.length; i++) {
			L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(	doornpc[i]);
			if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
				doorStatus = "$442"; // 닫혀
			} else if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				doorStatus = "$443"; // 열려
			}
			
			htmldata[i] = "" + doorName[i] + "" + doorStatus + "";
			// System.out.println("도어상태 " + door.getCrackStatus());
			switch (door.getCrackStatus()) {
			case 0:
				doorCrack = "$439";
				break;
			case 1:
				doorCrack = "$438";
				break;
			case 2:
				doorCrack = "$437";
				break;
			case 3:
				doorCrack = "$436";
				break;
			case 4:
				doorCrack = "$435";
				break;
			default:
				doorCrack = "$434";
				break;
			}
			
			htmldata[i + doornpc.length] = "" + doorName[i] + "" + doorCrack + "";
		}
		
		pc.sendPackets(new S_NPCTalkReturn(objid, htmlid, htmldata), true);
		htmlid = null;
		doorStatus = null;
		htmldata = null;
		doorName = null;
		doorCrack = null;
		doornpc = null;
		
	}

	private void repairGate(L1PcInstance pc, int npcId, int castleId) {
		if (pc.getClan().getCastleId() != castleId) {
			return;
		}
		
		if (WarTimeController.getInstance().isNowWar(castleId)) {
			return;
		}
		
		L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(npcId);
		
		if (door == null) {
			return;
		}
		
		door.repairGate();
	}

	private void repairAutoGate(L1PcInstance pc, int order) {
		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int castleId = clan.getCastleId();
			if (castleId != 0) {
				if (!WarTimeController.getInstance().isNowWar(castleId)) {
					for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
						if (L1CastleLocation.checkInWarArea(castleId, door)) {
							door.setAutoStatus(order);
						}
					}
					
					pc.sendPackets(new S_ServerMessage(990), true);
				} else {
					pc.sendPackets(new S_ServerMessage(991), true);
				}
			}
		}
	}

	private boolean usePolyScroll(L1PcInstance pc, int itemId, String s) {
		int time = 0;
		if (itemId == 40088 || itemId == 40096) {
			time = 1800;
		} else if (itemId == 140088) {
			time = 2100;
		}

		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		L1ItemInstance item = pc.getInventory().findItemId(itemId);
		boolean isUseItem = false;
		
		if (poly != null || s.equals("none")) {
			if (s.equals("none")) {
				if (pc.getGfxId().getTempCharGfx() == 6034 || pc.getGfxId().getTempCharGfx() == 6035) {
					isUseItem = true;
				} else {
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.SHAPE_CHANGE);
					isUseItem = true;
				}
			} else if (poly.getMinLevel() == 100) {
				isUseItem = true;
			} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
				isUseItem = true;
			}
		}
		
		if (isUseItem) {
			pc.getInventory().removeItem(item, 1);
		} else {
			pc.sendPackets(new S_ServerMessage(181), true);
		}
		
		return isUseItem;
	}

	private boolean isTwoLogin(L1PcInstance c) {
		boolean bool = false;
		for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			// 무인PC 는 제외
			if (target == null) {
				continue;
			}
			
			if (target.noPlayerCK) {
				continue;
			}
			
			if (target.샌드백) {
				continue;
			}
			
			//
			if (c.getId() != target.getId() && !target.isPrivateShop()) {
				if (c.getNetConnection().getAccountName().equalsIgnoreCase(target.getNetConnection().getAccountName())) {
					bool = true;
					break;
				}
			}
			
		}
		
		
		return bool;
	}

	private void UbRank(L1PcInstance pc, L1NpcInstance npc) {
		L1UltimateBattle ub = UBTable.getInstance().getUbForNpcId(npc.getNpcTemplate().get_npcId());
		
		if (ub == null) {
			return;
		}
		
		String[] htmldata = null;
		htmldata = new String[11];
		htmldata[0] = npc.getNpcTemplate().get_name();
		String htmlid = "colos3";
		int i = 1;

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ub_rank WHERE ub_id=? order by score desc limit 10");
			pstm.setInt(1, ub.getUbId());
			rs = pstm.executeQuery();
			
			while (rs.next()) {
				htmldata[i] = rs.getString(2) + " : " + String.valueOf(rs.getInt(3));
				i++;
			}
			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		
		pc.sendPackets(new S_NPCTalkReturn(pc.getId(), htmlid, htmldata), true);
		htmldata = null;
	}

	
	private String 오만(String s, L1PcInstance pc, L1NpcInstance npc) {
		String htmlid = null;
		if (s.equalsIgnoreCase("request oman amulet bag0")) {// 1층 부적
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(60201) && pc.getInventory().checkItem(60202)) {
				pc.getInventory().consumeItem(60201, 1);
				pc.getInventory().consumeItem(60202, 1);
				item = pc.getInventory().storeItem(5000110, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("1층 이동부적이나,봉인된 1층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag1")) {  // 11층 부적 by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40280) && pc.getInventory().checkItem(40289)) {
				pc.getInventory().consumeItem(40280, 1);
				pc.getInventory().consumeItem(40289, 1);
				item = pc.getInventory().storeItem(5000111, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("11층 이동부적이나,봉인된 11층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag2")) {  // 21층 부적 by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40281) && pc.getInventory().checkItem(40290)) {
				pc.getInventory().consumeItem(40281, 1);
				pc.getInventory().consumeItem(40290, 1);
				item = pc.getInventory().storeItem(5000112, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("21층 이동부적이나,봉인된 21층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag3")) {  // 31층 부적 by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40282) && pc.getInventory().checkItem(40291)) {
				pc.getInventory().consumeItem(40282, 1);
				pc.getInventory().consumeItem(40291, 1);
				item = pc.getInventory().storeItem(5000113, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("31층 이동부적이나,봉인된 31층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag4")) {  // 41층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40283) && pc.getInventory().checkItem(40292)) {
				pc.getInventory().consumeItem(40283, 1);
				pc.getInventory().consumeItem(40292, 1);
				item = pc.getInventory().storeItem(5000114, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("41층 이동부적이나,봉인된 41층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag5")) {  // 51층 부적 by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40284) && pc.getInventory().checkItem(40293)) {
				pc.getInventory().consumeItem(40284, 1);
				pc.getInventory().consumeItem(40293, 1);
				item = pc.getInventory().storeItem(5000115, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("51층 이동부적이나,봉인된 51층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag6")) {  // 61층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40285) && pc.getInventory().checkItem(40294)) {
				pc.getInventory().consumeItem(40285, 1);
				pc.getInventory().consumeItem(40294, 1);
				item = pc.getInventory().storeItem(5000116, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("61층 이동부적이나,봉인된 61층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag7")) {  // 71층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40286) && pc.getInventory().checkItem(40295)) {
				pc.getInventory().consumeItem(40286, 1);
				pc.getInventory().consumeItem(40295, 1);
				item = pc.getInventory().storeItem(5000117, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("71층 이동부적이나,봉인된 71층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag8")) {  // 81층 부적   by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40287) && pc.getInventory().checkItem(40296)) {
				pc.getInventory().consumeItem(40287, 1);
				pc.getInventory().consumeItem(40296, 1);
				item = pc.getInventory().storeItem(5000118, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("81층 이동부적이나,봉인된 81층 이동부적이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet bag9")) {  // 91층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40288) && pc.getInventory().checkItem(40297)) {
				pc.getInventory().consumeItem(40288, 1);
				pc.getInventory().consumeItem(40297, 1);
				item = pc.getInventory().storeItem(5000119, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("91층 이동부적이나,봉인된 91층 이동부적이 부족합니다."), true);
			}
			// ////////////////////부적의 혼란 합성 끝 by.송윤아
			// ////////////////////아래부터 부적의변이 제작 by.송윤아
		} else if (s.equalsIgnoreCase("request oman amulet box0")) {// 11층 부적
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(60200, 1000) && pc.getInventory().checkItem(60202)) {
				pc.getInventory().consumeItem(60200, 1000);
				pc.getInventory().consumeItem(60202, 1);
				item = pc.getInventory().storeItem(5000100, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("1층 이동부적이나,1층 주문서(1000)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box1")) {  // 11층 부적 by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40104, 900) && pc.getInventory().checkItem(40289)) {
				pc.getInventory().consumeItem(40104, 900);
				pc.getInventory().consumeItem(40289, 1);
				item = pc.getInventory().storeItem(5000101, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("11층 이동부적이나,11층 주문서(900)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box2")) {  // 21층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40105, 800) && pc.getInventory().checkItem(40290)) {
				pc.getInventory().consumeItem(40105, 800);
				pc.getInventory().consumeItem(40290, 1);
				item = pc.getInventory().storeItem(5000102, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item .getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("21층 이동부적이나,21층 주문서(800)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box3")) {  // 31층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40106, 700) 	&& pc.getInventory().checkItem(40291)) {
				pc.getInventory().consumeItem(40106, 700);
				pc.getInventory().consumeItem(40291, 1);
				item = pc.getInventory().storeItem(5000103, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("31층 이동부적이나,31층 주문서(700)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box4")) {  // 41층 부적 by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40107, 600)	&& pc.getInventory().checkItem(40292)) {
				pc.getInventory().consumeItem(40107, 600);
				pc.getInventory().consumeItem(40292, 1);
				item = pc.getInventory().storeItem(5000104, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("41층 이동부적이나,41층 주문서(600)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box5")) {  // 51층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40108, 500) 	&& pc.getInventory().checkItem(40293)) {
				pc.getInventory().consumeItem(40108, 500);
				pc.getInventory().consumeItem(40293, 1);
				item = pc.getInventory().storeItem(5000105, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item	.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage(	"51층 이동부적이나,51층 주문서(500)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box6")) {  // 61층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40109, 400) && pc.getInventory().checkItem(40294)) {
				pc.getInventory().consumeItem(40109, 400);
				pc.getInventory().consumeItem(40294, 1);
				item = pc.getInventory().storeItem(5000106, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("61층 이동부적이나,61층 주문서(400)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box7")) {  // 71층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40110, 300) && pc.getInventory().checkItem(40295)) {
				pc.getInventory().consumeItem(40110, 300);
				pc.getInventory().consumeItem(40295, 1);
				item = pc.getInventory().storeItem(5000107, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("71층 이동부적이나,71층 주문서(300)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box8")) {  // 81층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40111, 200) && pc.getInventory().checkItem(40296)) {
				pc.getInventory().consumeItem(40111, 200);
				pc.getInventory().consumeItem(40296, 1);
				item = pc.getInventory().storeItem(5000108, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("81층 이동부적이나,81층 주문서(200)장이 부족합니다."), true);
			}
		} else if (s.equalsIgnoreCase("request oman amulet box9")) {  // 91층 부적  by.송윤아
			L1ItemInstance item = null;
			if (pc.getInventory().checkItem(40112, 100) && pc.getInventory().checkItem(40297)) {
				pc.getInventory().consumeItem(40112, 100);
				pc.getInventory().consumeItem(40297, 1);
				item = pc.getInventory().storeItem(5000109, 1);
				pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
				htmlid = "";
			} else {
				pc.sendPackets(new S_SystemMessage("91층 이동부적이나,91층 주문서(100)장이 부족합니다."), true);
			} // 변이제작 끝 by.송윤아
				// 아래부터 합성 by.송윤아
			/*
			 * }else if (s.equalsIgnoreCase("request scroll of oman tower 11f"
			 * )){//11층 주문서 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(60200,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(60200,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40104, 1);
			 *
			 * pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
			 * htmlid = ""; } else { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑1층 이동주문서가  부족합니다."), true); } }else if
			 * (s.equalsIgnoreCase("request scroll of oman tower 21f")){//21층
			 * 주문서 by.송윤아 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(40104,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(40104,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40105, 1);
			 *
			 * pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
			 * htmlid = ""; } else { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑11층 이동주문서가  부족합니다."), true); } }else if
			 * (s.equalsIgnoreCase("request scroll of oman tower 31f")){//31층
			 * 주문서 by.송윤아 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(40105,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(40105,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40106, 1); pc.sendPackets(new
			 * S_ServerMessage(403, item.getName()), true); htmlid = ""; } else
			 * { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑21층 이동주문서가  부족합니다."), true); } }else if
			 * (s.equalsIgnoreCase("request scroll of oman tower 41f")){//41층
			 * 주문서 by.송윤아 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(40106,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(40106,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40107, 1); pc.sendPackets(new
			 * S_ServerMessage(403, item.getName()), true); htmlid = ""; } else
			 * { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑31층 이동주문서가  부족합니다."), true); } }else if
			 * (s.equalsIgnoreCase("request scroll of oman tower 51f")){//51층
			 * 주문서 by.송윤아 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(40107,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(40107,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40108, 1); pc.sendPackets(new
			 * S_ServerMessage(403, item.getName()), true); htmlid = ""; } else
			 * { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑41층 이동주문서가  부족합니다."), true); } }else if
			 * (s.equalsIgnoreCase("request scroll of oman tower 61f")){//61층
			 * 주문서 by.송윤아 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(40108,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(40108,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40109, 1); pc.sendPackets(new
			 * S_ServerMessage(403, item.getName()), true); htmlid = ""; } else
			 * { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑51층 이동주문서가  부족합니다."), true); } }else if
			 * (s.equalsIgnoreCase("request scroll of oman tower 71f")){//71층
			 * 주문서 by.송윤아 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(40109,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(40109,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40110, 1); pc.sendPackets(new
			 * S_ServerMessage(403, item.getName()), true); htmlid = ""; } else
			 * { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑61층 이동주문서가  부족합니다."), true); } }else if
			 * (s.equalsIgnoreCase("request scroll of oman tower 81f")){//81층
			 * 주문서 by.송윤아 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(40110,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(40110,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40111, 1); pc.sendPackets(new
			 * S_ServerMessage(403, item.getName()), true); htmlid = ""; } else
			 * { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑71층 이동주문서가  부족합니다."), true); } }else if
			 * (s.equalsIgnoreCase("request scroll of oman tower 91f")){//91층
			 * 주문서 by.송윤아 L1ItemInstance item = null; if
			 * (pc.getInventory().checkItem(40111,2) &&
			 * pc.getInventory().checkItem(40308,300)){
			 * pc.getInventory().consumeItem(40111,2);
			 * pc.getInventory().consumeItem(40308,300); item =
			 * pc.getInventory().storeItem(40112, 1); pc.sendPackets(new
			 * S_ServerMessage(403, item.getName()), true); htmlid = ""; } else
			 * { pc.sendPackets(new S_SystemMessage(
			 * "아데나나 오만의탑81층 이동주문서가  부족합니다."), true); }//합성제작 끝 by.송윤아
			 */
		} else if (s.equalsIgnoreCase("request oman book")) {
			boolean ck = false;
			
			for (int i = 5001120; i <= 5001129; i++) {
				if (!pc.getInventory().checkItem(i, 1)) {
					pc.sendPackets(new S_SystemMessage(ItemTable.getInstance().getTemplate(i).getName()+ " (1) 가  부족합니다."), true);
					ck = true;
				}
			}
			
			if (!pc.getInventory().checkItem(40308, 10000000)) {
				pc.sendPackets(new S_SystemMessage("아데나가  부족합니다."), true);
				ck = true;
			}
			
			if (ck) {
				return "";
			}
			
			for (int i = 5001120; i <= 5001129; i++) {
				pc.getInventory().consumeItem(i, 1);
			}
			
			pc.getInventory().consumeItem(40308, 10000000);
			L1ItemInstance item = pc.getInventory().storeItem(60203, 1);
			pc.sendPackets(new S_ServerMessage(143, npc.getNameId(), item.getItem().getNameId()));
			htmlid = "";
		}
		
		return htmlid;
	}

	private String 햄(String s, L1PcInstance pc) {
		L1Quest q = pc.getQuest();
		String HtmlId = null;
		if (s.equalsIgnoreCase("a")) {
			if (q.get_step(L1Quest.QUEST_60_HAM) == 0) {
				if (pc.getLevel() >= 60) {
					L1ItemInstance 아이템 = null;
					아이템 = pc.getInventory().storeItem(7337, 1);
					pc.sendPackets(new S_ServerMessage(143, "햄", 아이템.getLogName()), true);
					q.set_step(L1Quest.QUEST_60_HAM, 1);// 햄 받음
					HtmlId = "hamo4";
				} else {
					// pc.sendPackets(new
					// S_SystemMessage("레벨 60 부터 이용 가능합니다."));
					HtmlId = "hamo3";
				}
			} else {
				HtmlId = "hamo1";
			}
		}
		
		return HtmlId;
	}

	private String 마빈(String s, L1PcInstance pc) {
		L1Quest q = pc.getQuest();
		Random random = new Random();
		String HtmlId = null;
		if (s.equalsIgnoreCase("b")) {
			if (q.get_step(L1Quest.QUEST_MARBIN) == 0) {
				if (pc.getLevel() >= 70) {
					if (pc.getInventory().checkItem(6015)) {
						pc.sendPackets(new S_SystemMessage("당신은 이미 마빈의 주머니를 소지하고 계십니다."), true);
						HtmlId = "";
					} else {
						if (pc.getLevel() > 1 && pc.getLevel() <= Config.클라우디아레벨) {
							L1QuestInfo info = pc.getQuestList(367);
							if (info != null && info.end_time == 0) {
								info.ck[0] = 1;
								pc.sendPackets(new S_QuestTalkIsland(pc, 367, info));
							}
						}
						
						L1ItemInstance 아이템 = null;
						아이템 = pc.getInventory().storeItem(6015, 1);
						pc.sendPackets(new S_ServerMessage(143, "마빈", 아이템.getLogName()), true);
						q.set_step(L1Quest.QUEST_MARBIN, 1);// 마빈퀘시작
						HtmlId = "marbinquest2";
					}
				} else {
					// pc.sendPackets(new S_SystemMessage("레벨 52부터 이용 가능합니다."));
					HtmlId = "marbinquest8";
				}
			} else {
				HtmlId = "";
			}
		} else if (s.equalsIgnoreCase("a")) {
			if (q.get_step(L1Quest.QUEST_MARBIN) == 1) {
				if (pc.getLevel() >= 70) {
					if (pc.getInventory().checkItem(6016) && pc.getInventory().checkItem(6018, 100) && pc.getInventory().checkItem(6019)) {

						pc.getInventory().consumeItem(6016, 1);
						pc.getInventory().consumeItem(6018, pc.getInventory().countItems(6018));  // 눈물은 인벤에있는거 모두 삭제
						pc.getInventory().consumeItem(6019, 1);
						// int needExp =
						// ExpTable.getNeedExpNextLevel(pc.getLevel());
						int addexp = 0;
						// 퀘보상52~64 10퍼 65~75 5퍼 76 1
						addexp = (int) (240000 * Config.RATE_XP);
						double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
						addexp *= exppenalty;
						if (addexp != 0) {
							int level = ExpTable.getLevelByExp(pc.getExp() + addexp);
							if (level > Config.MAXLEVEL) {
								pc.sendPackets(new S_SystemMessage("레벨 제한으로 인해  더이상 경험치를 획득할 수 없습니다."), true);
							} else
								pc.addExp(addexp);
						}
						
						try {
							pc.save();
						} catch (Exception e) {
						}
						
						L1ItemInstance 아이템 = pc.getInventory().storeItem(6020, 5);
						pc.sendPackets(new S_ServerMessage(143, "마빈", 아이템.getName() + " (5)"), true);
						/*
						 * int rnd = random.nextInt(16)+2011; int rnd2 =
						 * random.nextInt(20)+2028; pc.sendPackets(new
						 * S_SkillSound(pc.getId(), rnd), true);
						 * Broadcaster.broadcastPacket(pc,new
						 * S_SkillSound(pc.getId(), rnd), true);
						 * pc.sendPackets(new S_SkillSound(pc.getId(), rnd2),
						 * true); Broadcaster.broadcastPacket(pc,new
						 * S_SkillSound(pc.getId(), rnd2), true);
						 */
						// pc.sendPackets(new S_SkillSound(pc.getId(), 8473),
						// true);
						// Broadcaster.broadcastPacket(pc,new
						// S_SkillSound(pc.getId(), 8473), true);
						pc.sendPackets(new S_SkillSound(pc.getId(), 3944), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944), true);
						HtmlId = "marbinquest4";
					} else {
						HtmlId = "marbinquest5";
					}
				} else {
					// pc.sendPackets(new S_SystemMessage("레벨 60부터 이용 가능합니다."));
					HtmlId = "marbinquest8";
				}
			} else {
				HtmlId = "";
			}
		} else if (s.equalsIgnoreCase("c")) {
			if (q.get_step(L1Quest.QUEST_MARBIN) == 1) {
				if (pc.getLevel() >= 70) {
					if (pc.getInventory().checkItem(6017) && pc.getInventory().checkItem(6018, 100) && pc.getInventory().checkItem(6019)) {
						pc.getInventory().consumeItem(6017, 1);
						pc.getInventory().consumeItem(6018, pc.getInventory().countItems(6018));  // 눈물은 인벤에있는거 모두 삭제
						pc.getInventory().consumeItem(6019, 1);
						// 100번째 퀘보상
						// int needExp =
						// ExpTable.getNeedExpNextLevel(pc.getLevel());
						int addexp = 0;
						addexp = (int) (720000 * Config.RATE_XP);
						double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
						addexp *= exppenalty;
						// 퀘보상52~64 20퍼 65~75 7퍼 76 3퍼
						/*
						 * if(pc.getLevel() >=52 && pc.getLevel() <=64){ addexp
						 * = (int) (needExp * 0.06); }else if(pc.getLevel() >=
						 * 65 && pc.getLevel() <= 69){ addexp = (int) (needExp *
						 * 0.03); }else if(pc.getLevel() >= 70 && pc.getLevel()
						 * < 75){ addexp = (int) (needExp * 0.015); }else
						 * if(pc.getLevel() >= 75){ addexp = (int) (needExp *
						 * 0.0075); }
						 */
						if (addexp != 0) {
							pc.addExp(addexp);
						}
						
						try {
							pc.save();
						} catch (Exception e) {
						}
						
						L1ItemInstance 아이템 = null;
						아이템 = pc.getInventory().storeItem(6020, 15);
						// pc.sendPackets(new S_ServerMessage(403,
						// 아이템.getLogName()));
						pc.sendPackets(new S_ServerMessage(143, "마빈", 아이템.getName() + " (5)"), true);
						// pc.sendPackets(new S_ServerMessage(403,
						// 아이템.getLogName()));
						pc.sendPackets(new S_ServerMessage(143, "마빈", 아이템.getName() + " (1)"), true);
						int rnd = random.nextInt(16) + 2011;
						int rnd2 = random.nextInt(20) + 2028;
						pc.sendPackets(new S_SkillSound(pc.getId(), rnd));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), rnd), true);
						pc.sendPackets(new S_SkillSound(pc.getId(), rnd2));
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), rnd2), true);
						pc.sendPackets(new S_SkillSound(pc.getId(), 3944));// 8473
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944), true);
						HtmlId = "marbinquest6";
					} else {
						HtmlId = "marbinquest7";
					}
				} else {
					// pc.sendPackets(new
					// S_SystemMessage("레벨 52 부터 이용 가능합니다."));
					HtmlId = "marbinquest8";
				}
			} else {
				HtmlId = "";
			}
		}
		
		return HtmlId;
	}

	private String 스빈(String s, L1PcInstance pc) {
		String HtmlId = "";
		if (s.equalsIgnoreCase("a")) {

			L1ItemInstance 아이템 = pc.getInventory().storeItem(49031, 1);
			pc.sendPackets(new S_ServerMessage(143, "스빈", 아이템.getName() + " (1)"), true);
			L1ItemInstance 아이템2 = pc.getInventory().storeItem(21081, 1);
			pc.sendPackets(new S_ServerMessage(143, "스빈", 아이템2.getName() + " (1)"), true);

			pc.setTelType(11);
			pc.sendPackets(new S_SabuTell(pc), true);

		}
		
		return HtmlId;
	}

	private String 구호증서(String s, L1PcInstance pc) {
		String HtmlId = "";
		if (s.equalsIgnoreCase("material")) {
			if (pc.getExpRes() == 1) {
				pc.sendPackets(new S_Message_YN(2551, "$21348"));
			} else {
				pc.sendPackets(new S_ServerMessage(739));
				return HtmlId = "";
			}
		}
		
		
		return HtmlId;
	}

	private String 첩보원(String s, L1PcInstance pc) {
		String HtmlId = "";
		if (s.equalsIgnoreCase("a")) {
			if (pc.getInventory().checkItem(6013)) {
				HtmlId = "icqwand4";
			} else {
				L1ItemInstance 아이템 = pc.getInventory().storeItem(6013, 150);
				HtmlId = "icqwand2";
				pc.sendPackets(new S_ServerMessage(143, "상아탑 첩보원", 아이템.getName() + " (150)"), true);
			}
		} else if (s.equalsIgnoreCase("b")) {
			if (pc.getInventory().checkItem(6014)) {
				HtmlId = "icqwand4";
			} else {
				L1ItemInstance 아이템 = pc.getInventory().storeItem(6014, 100);
				HtmlId = "icqwand3";
				pc.sendPackets(new S_ServerMessage(143, "상아탑 첩보원", 아이템.getName() + " (100)"), true);
			}
		}
		
		
		return HtmlId;
	}

	private static final int 화둥인던입장 = 60;

	private static final int 얼던인던입장 = 80;

	private String 엘드나스(L1PcInstance pc, String s) {
		String HtmlId = "";
		if (s.equalsIgnoreCase("a")) {
			if (pc.getLevel() >= 화둥인던입장) {
				if (pc.getInventory().checkItem(7337)) {
					pc.dx = 32624;
					pc.dy = 33057;
					pc.dm = (short) 2699;
					pc.dh = 6;
					pc.setTelType(7);
					pc.sendPackets(new S_SabuTell(pc), true);

					L1NpcInstance 흑데스 = GameList.getbdeath();
					if (흑데스 != null) {
						흑데스.Bchat_start(pc);
					}
				} else {
					HtmlId = "eldnas1";
				}
			} else {
				pc.sendPackets(new S_SystemMessage("레벨 " + 화둥인던입장 + " 부터 입장 가능 합니다."));
				HtmlId = "eldnas3";
			}
		}
		
		
		return HtmlId;
	}

	private static synchronized String 흑데스(String s, L1PcInstance pc) {
		String HtmlId = "";
		if (s.equalsIgnoreCase("enter")) {
			/*
			 * if(흑데스ディレイ.get()){ pc.sendPackets(new S_SystemMessage(
			 * "인스턴스 던전 초기화중입니다. 잠시후 입장해 주세요.")); return ""; } 흑데스ディレイ.set(true);
			 * GeneralThreadPool.getInstance().schedule(new Runnable(){
			 *
			 * @Override public void run() { 흑데스ディレイ.set(false); } }, 3000);
			 */
			if (pc.getInventory().checkItem(7337)) {
				if (pc.getLevel() >= 화둥인던입장) {
					Config._IND_Q.requestWork(new IND(pc.getName(), 5));
					/*
					 * int count = 0; for (L1PcInstance pl:
					 * L1World.getInstance().getAllPlayers()) {
					 *
					 * count++; } System.out.println("입장 : "+count);
					 */
				} else {
					pc.sendPackets(new S_SystemMessage("레벨 " + 화둥인던입장 + " 부터 입장 가능 합니다."));
					return HtmlId = "fd_death2";
				}
			} else {
				pc.sendPackets(new S_SystemMessage("냉한의 기운이 없습니다. 인벤창을 확인해주세요."), true);
			}
		} else {
			HtmlId = "";
		}
		
		
		return HtmlId;
	}

	private static synchronized String 얼음여왕성(String s, L1PcInstance pc) {
		String HtmlId = "";
		if (s.equalsIgnoreCase("a") || s.equalsIgnoreCase("b")) {
			if (pc.isKnight() || pc.isWarrior()) {
				pc.dx = 32737;
				pc.dy = 32811;
				pc.dm = 276;
				pc.dh = pc.getMoveState().getHeading();
				pc.setTelType(7);
				pc.sendPackets(new S_SabuTell(pc), true);
			} else if (pc.isCrown()) {
				pc.dx = 32734;
				pc.dy = 32852;
				pc.dm = 277;
				pc.dh = pc.getMoveState().getHeading();
				pc.setTelType(7);
				pc.sendPackets(new S_SabuTell(pc), true);
			} else if (pc.isElf()) {
				pc.dx = 32735;
				pc.dy = 32867;
				pc.dm = 275;
				pc.dh = pc.getMoveState().getHeading();
				pc.setTelType(7);
				pc.sendPackets(new S_SabuTell(pc), true);
			} else if (pc.isDarkelf()) {
				pc.dx = 32736;
				pc.dy = 32809;
				pc.dm = 273;
				pc.dh = pc.getMoveState().getHeading();
				pc.setTelType(7);
				pc.sendPackets(new S_SabuTell(pc), true);
			} else if (pc.isDragonknight()) {
				pc.dx = 32734;
				pc.dy = 32852;
				pc.dm = 274;
				pc.dh = pc.getMoveState().getHeading();
				pc.setTelType(7);
				pc.sendPackets(new S_SabuTell(pc), true);
			} else if (pc.isIllusionist()) {
				pc.dx = 32809;
				pc.dy = 32830;
				pc.dm = 272;
				pc.dh = pc.getMoveState().getHeading();
				pc.setTelType(7);
				pc.sendPackets(new S_SabuTell(pc), true);
			} else if (pc.isWizard()) {
				pc.dx = 32739;
				pc.dy = 32856;
				pc.dm = 271;
				pc.dh = pc.getMoveState().getHeading();
				pc.setTelType(7);
				pc.sendPackets(new S_SabuTell(pc), true);
			}
		} else if (s.equalsIgnoreCase("c")) {
			if (pc.getInventory().checkItem(600369)) {
				return "newbrad6";
			}
			
			if (pc.getInventory().checkItem(6022)) {
				if (pc.getLevel() >= 얼던인던입장) {
					if (pc.인던입장중) {
						pc.sendPackets(new S_SystemMessage("입장 대기중입니다. 잠시후 다시 클릭해주세요."), true);
						return "";
					}
					pc.sendPackets(new S_SystemMessage("인던 맵을 설정 중 입니다. 잠시만 기다려주세요."), true);
					Config._IND_Q.requestWork(new IND(pc.getName(), 1));
				} else {
					pc.sendPackets(new S_SystemMessage("레벨 " + 얼던인던입장 + " 부터 입장 가능 합니다."));
				}
			} else {
				pc.sendPackets(new S_SystemMessage("화염의 기운이 없습니다. 인벤창을 확인해주세요."), true);
			}
		} else {
			HtmlId = "";
		}
		
		
		return HtmlId;
	}

	/** 유리에 인던 엔피씨 정보 리셋 */
	private String 유리에(L1PcInstance pc, String NpcName, String String) {
		String htmlid = null;
		
		// 유리에 액션 넘버 a
		if (String.equalsIgnoreCase("b")) {
			// 레벨 제한에 걸린다
			if(pc.getLevel() < 80) {
				return "id_yurie07";
			}
			
			// 시공의 열쇠 상자 이미 소지 하고있따면
			if (pc.getInventory().checkItem(500016, 1)) {
				return "id_yurie04";
			}
			
			// 필요한 조건 : 아데나 30만이 있는지 체크 없다면 리턴
			if (pc.getInventory().consumeItem(L1ItemId.ADENA, 300000)){
				L1ItemInstance Item = pc.getInventory().storeItem(500016, 1);
				pc.sendPackets(new S_ServerMessage(143, NpcName, Item.getLogName()),true);
				htmlid = "id_yurie06";
			} else {
				htmlid = "id_yurie05";
			}
		}
		
		
		return htmlid;
	}

	
	private final static int 하딘렙제 = 70;
	private final static int 하딘렙제1 = 80;

	/** 하딘, 해상전 **/
	private static synchronized String hadinEnter(String s, L1PcInstance pc) {
		String htmlid = null;
		/*
		 * if(하딘ディレイ.get()){ pc.sendPackets(new S_SystemMessage(
		 * "인스턴스 던전 초기화중입니다. 잠시후 입장해 주세요.")); return ""; } 하딘ディレイ.set(true);
		 * GeneralThreadPool.getInstance().schedule(new Runnable(){
		 *
		 * @Override public void run() { 하딘ディレイ.set(false); } }, 3000);
		 */
		if (s.equalsIgnoreCase("enter")) {
			if (pc.isInParty()) {
				if (pc.getParty().isLeader(pc)) {
					if (pc.getParty().getNumOfMembers() >= 5) {
						boolean lvck = true;
						for (L1PcInstance Ppc : pc.getParty().getMembers()) {
							if (하딘렙제 > Ppc.getLevel()) {
								pc.sendPackets(new S_SystemMessage("레벨 제한 : 파티원 전체 (" + 하딘렙제 + ")레벨 부터 입장 가능."), true);
								Ppc.sendPackets(new S_SystemMessage("레벨 제한 : " + 하딘렙제 + " 레벨 부터 입장 가능."));
								lvck = false;
								break;
							}
						}
						
						if (!lvck) {
							pc.sendPackets(new S_SystemMessage("레벨 제한 : " + 하딘렙제 + " 레벨 부터 입장 가능."));
							return "";
						}

						boolean ck = true;
						for (L1PcInstance Ppc : pc.getParty().getMembers()) {
							if (pc.getMapId() != Ppc.getMapId()) {
								pc.sendPackets(new S_SystemMessage("파티원이 다 모이지 않았습니다."), true);
								ck = false;
								break;
							}
						}
						
						if (ck) {
							if (pc.getMapId() == 9202)
								Config._IND_Q.requestWork(new IND(pc.getName(), 3));
							else
								Config._IND_Q.requestWork(new IND(pc.getName(), 0));
						}
						
						htmlid = "";
					} else {
						if (pc.getMapId() == 9202) {
							htmlid = "id1_1";
						} else {
							htmlid = "id0_1";
						}
					}
				} else {
					if (pc.getMapId() == 9202) {
						htmlid = "id1_2";
					} else {
						htmlid = "id0_2";
					}
				}
			} else {
				if (pc.getMapId() == 9202) {
					htmlid = "id1_2";
				}
				else {
					htmlid = "id0_2";
				}
			}
		}
		
		return htmlid;
	}

	
	private static synchronized String 오림인던중(String s, L1PcInstance pc) {
		String htmlid = null;
		if (s.equalsIgnoreCase("enter")) {
			if (pc.isInParty()) {
				if (pc.getParty().isLeader(pc)) {
					if (pc.getParty().getNumOfMembers() >= 3) {
						boolean lvck = true;
						for (L1PcInstance Ppc : pc.getParty().getMembers()) {
							if (하딘렙제1 > Ppc.getLevel()) {
								pc.sendPackets(new S_SystemMessage("레벨 제한 : 파티원 전체 (" + 하딘렙제1 + ")레벨 부터 입장 가능."), true);
								Ppc.sendPackets(new S_SystemMessage("레벨 제한 : " + 하딘렙제1 + " 레벨 부터 입장 가능."));
								lvck = false;
								break;
							}
						}
						
						if (!lvck) {
							pc.sendPackets(new S_SystemMessage("레벨 제한 : " + 하딘렙제1 + " 레벨 부터 입장 가능."));
							return "";
						}

						boolean ck = true;
						for (L1PcInstance Ppc : pc.getParty().getMembers()) {
							if (pc.getMapId() != Ppc.getMapId()) {
								pc.sendPackets(new S_SystemMessage("파티원이 다 모이지 않았습니다."), true);
								ck = false;
								break;
							}
						}
						
						if (ck) {
							if (pc.getMapId() == 9202) {
								Config._IND_Q.requestWork(new IND(pc.getName(), 4));
							}
						}
						
						htmlid = "";
					} else {
						if (pc.getMapId() == 9202) {
							htmlid = "id1_1";
						} else {
							htmlid = "id0_1";
						}
					}
				} else {
					if (pc.getMapId() == 9202) {
						htmlid = "id1_2";
					} else {
						htmlid = "id0_2";
					}
				}
			} else {
				if (pc.getMapId() == 9202) {
					htmlid = "id1_2";
				} else {
					htmlid = "id0_2";
				}
			}
		}
		
		return htmlid;
	}

	private static synchronized String 오림인던하(String s, L1PcInstance pc) {
		String htmlid = null;
		/*
		 * if(하딘ディレイ.get()){ pc.sendPackets(new S_SystemMessage(
		 * "인스턴스 던전 초기화중입니다. 잠시후 입장해 주세요.")); return ""; } 하딘ディレイ.set(true);
		 * GeneralThreadPool.getInstance().schedule(new Runnable(){
		 *
		 * @Override public void run() { 하딘ディレイ.set(false); } }, 3000);
		 */
		if (s.equalsIgnoreCase("enter")) {
			if (pc.isInParty()) {
				if (pc.getParty().isLeader(pc)) {
					if (pc.getParty().getNumOfMembers() >= 3) {
						boolean lvck = true;
						for (L1PcInstance Ppc : pc.getParty().getMembers()) {
							if (하딘렙제 > Ppc.getLevel()) {
								pc.sendPackets(new S_SystemMessage("레벨 제한 : 파티원 전체 (" + 하딘렙제 + ")레벨 부터 입장 가능."), true);
								Ppc.sendPackets(new S_SystemMessage("레벨 제한 : " + 하딘렙제 + " 레벨 부터 입장 가능."));
								lvck = false;
								break;
							}
						}
						
						if (!lvck) {
							pc.sendPackets(new S_SystemMessage("레벨 제한 : " 	+ 하딘렙제 + " 레벨 부터 입장 가능."));
							return "";
						}

						boolean ck = true;
						for (L1PcInstance Ppc : pc.getParty().getMembers()) {
							if (pc.getMapId() != Ppc.getMapId()) {
								pc.sendPackets(new S_SystemMessage("파티원이 다 모이지 않았습니다."), true);
								ck = false;
								break;
							}
						}
						
						if (ck) {
							if (pc.getMapId() == 9202) {
								Config._IND_Q.requestWork(new IND(pc.getName(), 3));
							}

						}
						
						htmlid = "";
					} else {
						if (pc.getMapId() == 9202) {
							htmlid = "id1_1";
						} else {
							htmlid = "id0_1";
						}
					}
				} else {
					if (pc.getMapId() == 9202) {
						htmlid = "id1_2";
					} else {
						htmlid = "id0_2";
					}
				}
			} else {
				if (pc.getMapId() == 9202) {
					htmlid = "id1_2";
				} else {
					htmlid = "id0_2";
				}
			}
		}
		
		
		return htmlid;
	}

	private String 베테르랑(L1PcInstance pc, L1NpcInstance npc, String s) {

		// veteranE1 -> 용사의장비지원증서가 없음
		// veteranE2 -> 받았을때
		// veteranE3 -> 무기or방어구 -> 물약or주문서
		// veteranE4 -> 교환할 장비 없음
		// veteranE5 -> 인증서가 없을때 대화창
		// veteranE6 -> 당신은 이 아이템 착용 불가
		String htmlid = "";
		if (s.equals("a")) { // 단검
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 267);
		} else if (s.equals("b")) { // 한손검
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 268);
		} else if (s.equals("c")) { // 양손검
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 269);
		} else if (s.equals("d")) { // 보우건
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 270);
		} else if (s.equals("e")) { // 지팡이
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 271);
		} else if (s.equals("f")) { // 크로우
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 272);
		} else if (s.equals("g")) { // 체인소드
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 274);
		} else if (s.equals("h")) { // 키링크
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 273);
		} else if (s.equals("i")) { // 판금 갑옷
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 21160);
		} else if (s.equals("j")) { // 가죽 갑옷
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 21161);
		} else if (s.equals("k")) { // 로브
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 21162);
		} else if (s.equals("l")) { // 방패
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 21165);
		} else if (s.equals("m")) { // 티셔츠
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 21159);
		} else if (s.equals("n")) { // 장화
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 21164);
		} else if (s.equals("o")) { // 해골 투구
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 21158);
		} else if (s.equals("p")) { // 마법 망토
			htmlid = 베테르랑아이템무기방어구생성(pc, npc, 21163);
		} else if (s.equals("1") || s.equals("2") || s.equals("3")) { // 무기 ->  물약 교환, 용사데이, 용사젤
			boolean ck = false;
			
			for (int i = 267; i <= 274; i++) {
				L1ItemInstance[] list = pc.getInventory().findItemsIdNotEquipped(i);
				
				if (list != null && list.length > 0) {
					
					for (L1ItemInstance item : list) {
						pc.getInventory().removeItem(item);
						
						if (s.equals("1")) {
							pc.getInventory().storeItem(60029, 100);
						} else if (s.equals("2")) {
							pc.getInventory().storeItem(60314, 1);
						} else if (s.equals("3")) {
							pc.getInventory().storeItem(60315, 1);
						}
					}
				}
			}
			
			if (ck) {
				htmlid = "veteranE3";
			} else {
				htmlid = "veteranE4";
			}
		} else if (s.equals("4") || s.equals("5") || s.equals("6")) { // 방어구 -> 물약 교환, 용사데이, 용사젤
			boolean ck = false;
			
			for (int i = 21158; i <= 21165; i++) {
				L1ItemInstance[] list = pc.getInventory().findItemsIdNotEquipped(i);
				
				if (list != null && list.length > 0) {
					
					for (L1ItemInstance item : list) {
						pc.getInventory().removeItem(item);
						
						if (s.equals("4")) {
							pc.getInventory().storeItem(60029, 100);
						} else if (s.equals("5")) {
							pc.getInventory().storeItem(60314, 1);
						} else if (s.equals("6")) {
							pc.getInventory().storeItem(60315, 1);
						}
					}
				}
			}
			
			if (ck) {
				htmlid = "veteranE3";
			} else {
				htmlid = "veteranE4";
			}
		} else if (s.equals("A")) { // 라폰스에게
			L1Location loc = new L1Location(32603, 32722, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("B")) { // 폴과 마르코
			L1Location loc = new L1Location(32609, 32771, 4).randomLocation(2, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("C")) { // 모포
			pc.sendPackets(new S_SystemMessage("현재 존재하지 않는 NPC 입니다."));
		} else if (s.equals("D")) { // 아르카
			L1Location loc = new L1Location(32618, 32724, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("E")) { // 다투
			L1Location loc = new L1Location(32620, 32773, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("F")) { // 듀오
			L1Location loc = new L1Location(32628, 32771, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("G")) { // 두포
			L1Location loc = new L1Location(32616, 32776, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("H")) { // 펫매치
			pc.sendPackets(new S_SystemMessage("현재 존재하지 않는 NPC 입니다."));
			// L1Location loc = new L1Location(32630, 32741,
			// 4).randomLocation(3, true);
			// L1Teleport.teleport(pc, loc.getX(), loc.getY(),
			// (short)loc.getMapId(), 5, true);
		} else if (s.equals("I")) { // 은말 콜로
			L1Location loc = new L1Location(33055, 33363, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("J")) { // 말섬 콜로
			pc.sendPackets(new S_SystemMessage("현재 존재하지 않는 NPC 입니다."));
		} else if (s.equals("K")) { // 글말 콜로
			L1Location loc = new L1Location(32572, 32756, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("L")) { // 웰던 콜로
			L1Location loc = new L1Location(33746, 32529, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		} else if (s.equals("M")) { // 기란 콜로
			L1Location loc = new L1Location(33505, 32766, 4).randomLocation(3, true);
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true);
		}
		
		return htmlid;
	}

	private String 베테르랑아이템무기방어구생성(L1PcInstance pc, L1NpcInstance npc, int itemid) {
		String htmlid = "";
		L1Item itemck = ItemTable.getInstance().getTemplate(itemid);
		
		if (!(pc.isCrown() && itemck.isUseRoyal() || pc.isKnight()
				&& itemck.isUseKnight() || pc.isElf() && itemck.isUseElf()
				|| pc.isWizard() && itemck.isUseMage() || pc.isDarkelf()
				&& itemck.isUseDarkelf() || pc.isDragonknight()
				&& itemck.isUseDragonKnight() || pc.isIllusionist()
				&& itemck.isUseBlackwizard())) {
			return htmlid = "veteranE6";
		}
		
		if (pc.getInventory().consumeItem(60316, 1)) {
			L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
			item.setCount(1);
			item.setIdentified(true);
			pc.getInventory().storeItem(item);
			pc.sendPackets(new S_ServerMessage(143, npc.getName(), item.getName()), true);
			htmlid = "veteranE2";
		} else {
			htmlid = "veteranE1";
		}
		
		return htmlid;
	}

	
	class CrockSystemAdd implements Runnable {
		L1PcInstance pc;

		public CrockSystemAdd(L1PcInstance _pc) {
			pc = _pc;
		}

		@Override
		public void run() {
			try {
				if (pc == null || pc.getNetConnection() == null) {
					return;
				}
				CrockSystem.getInstance().add(pc);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
	}

	private int polyLawfulTime(int lawful, int max, int min) {
		if (lawful >= 32767) {
			return max;
		} else if (lawful <= -32768) {
			return min;
		}
		
		double d = 65535 / (max - min);
		int lawfulex = lawful + 32768;
		if (lawfulex > 65535) {
			lawfulex = 65535;
		}
		
		int time = (int) (lawfulex / d + min);
		if (time > max) {
			time = max;
		} else if (time < min) {
			time = min;
		}
		
		return time;
	}

	private String 데스나이트버프(L1PcInstance pc, String s) {
		String htmlid = "";
		if (s.equalsIgnoreCase("a")) {
			pc.sendPackets(new S_SkillSound(pc.getId(), 7682));
			pc.broadcastPacket(new S_SkillSound(pc.getId(), 7682));
			
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.사엘)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.사엘);
			}
			
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.크레이)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.크레이);
			}
			
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.군터의조언)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.군터의조언);
			}
			
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			pc.sendPackets(new S_OwnCharStatus(pc));
			htmlid = "vdeath2";
		}
		
		return htmlid;
	}

	private void 아놀드경험치(L1PcInstance pc, int type) {
		int needExp = ExpTable.getNeedExpNextLevel(52);
		double exppenalty = ExpTable.getPenaltyRate(pc.getLevel());
		int exp = 0;
		
		if (type == 1) {
			exp = (int) (needExp * 0.02D * exppenalty);
		} else if (type == 2) {
			exp = (int) (needExp * 0.05D * exppenalty);
		} else if (type == 3) {
			exp = (int) (needExp * 0.20D * exppenalty);
		} else {
			pc.sendPackets(new S_SystemMessage("잘못된 요구입니다."));
		}
		
		pc.addExp(exp);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944));
		pc.sendPackets(new S_SkillSound(pc.getId(), 3944));
	}

	
	private int calcheading(int myx, int myy, int tx, int ty) {
		if (tx > myx && ty > myy) {
			return 3;
		} else if (tx < myx && ty < myy) {
			return 7;
		} else if (tx > myx && ty == myy) {
			return 2;
		} else if (tx < myx && ty == myy) {
			return 6;
		} else if (tx == myx && ty < myy) {
			return 0;
		} else if (tx == myx && ty > myy) {
			return 4;
		} else if (tx < myx && ty > myy) {
			return 5;
		} else {
			return 1;
		}
	}

	
	@Override
	public String getType() {
		return C_NPC_ACTION;
	}
	
}