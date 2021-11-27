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

import java.sql.Timestamp;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.IND;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Npc;

//Referenced classes of package l1j.server.server.model:
//L1NpcInstance, L1Teleport, L1NpcTalkData, L1PcInstance,
//L1TeleporterPrices, L1TeleportLocations

public class L1TeleporterInstance extends L1NpcInstance {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1TeleporterInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		attack.calcHit();
		attack.action();
		attack = null;
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		int npcid = getNpcTemplate().get_npcId();
		String htmlid = null;

		if (talking != null) {
			switch (npcid) {
			case 50056:
				if (player.getLevel() < 45) {
					htmlid = "telesilver4";
				} else if (player.getLevel() >= 45 && player.getLevel() <= 51) {
					htmlid = "telesilver5";
				}
				break;
			case 50001:
				if (player.isElf()) {
					htmlid = "barnia3";
				} else if (player.isKnight() || player.isCrown()) {
					htmlid = "barnia2";
				} else if (player.isWizard() || player.isDarkelf()) {
					htmlid = "barnia1";
				}
				break;
			/*
			 * case 50014: if (player.isWizard()) { // 위저드 if
			 * (quest.get_step(L1Quest.QUEST_LEVEL30) == 1 &&
			 * !player.getInventory().checkItem(40579)) { // 안 데드의 뼈 htmlid = "dilong1"; }
			 * else { htmlid = "dilong3"; } } break;
			 */
			case 50016:
				if (player.getLevel() >= 13)
					htmlid = "zeno2";
				break;
			/*
			 * case 50031: if (player.isElf()) { // 에르프 if
			 * (quest.get_step(L1Quest.QUEST_LEVEL45) == 2) { if
			 * (!player.getInventory().checkItem(40602)) { // 블루 플룻 htmlid = "sepia1"; } } }
			 * break;
			 */
			/*
			 * case 50043: if (quest.get_step(L1Quest.QUEST_LEVEL50) == L1Quest.QUEST_END) {
			 * htmlid = "ramuda2"; } else if (quest.get_step(L1Quest.QUEST_LEVEL50) == 1) {
			 * // 디가르딘 동의가 끝난 상태 if (player.isCrown()) { // 군주 if (_isNowDely) { // 텔레포트 지연중
			 * htmlid = "ramuda4"; } else { htmlid = "ramudap1"; } } else { // 군주 이외 htmlid
			 * = "ramuda1"; } } else { htmlid = "ramuda3"; } break;
			 */
			case 50055:
				if (player.getLevel() >= 13)
					htmlid = "drist1";
				break;
			case 50069:
				if (!player.isDarkelf())
					htmlid = "enya2";
				else if (player.getLevel() >= 13)
					htmlid = "enya4";
				break;
			/*
			 * case 70779: if (player.getGfxId().getTempCharGfx() == 1037) { // 쟈이안트안트 변신
			 * htmlid = "ants3"; } else if (player.getGfxId().getTempCharGfx() == 1039) {//
			 * 쟈이안트안트소르쟈 변신 if (player.isCrown()) { // 군주 if
			 * (quest.get_step(L1Quest.QUEST_LEVEL30) == 1) { if
			 * (player.getInventory().checkItem(40547)) { // 주민들의 유품 htmlid = "antsn"; }
			 * else { htmlid = "ants1"; } } else { // Step1 이외 htmlid = "antsn"; } } else {
			 * // 군주 이외 htmlid = "antsn"; } } break;
			 */
			/*
			 * case 70853: if (player.isElf()) { // 에르프 if
			 * (quest.get_step(L1Quest.QUEST_LEVEL30) == 1) { if
			 * (!player.getInventory().checkItem(40592)) { // 저주해진 정령서 Random random = new
			 * Random(System.nanoTime()); if (random.nextInt(100) < 50) { // 50%로 다크마르단젼
			 * htmlid = "fairyp2"; } else { // 다크 에르프 지하 감옥 htmlid = "fairyp1"; } random =
			 * null; } } } break;
			 */
			case 100218: // 도리아
				if (player.getLevel() >= 10)
					htmlid = "doria1";
				break;
			/*
			 * case 100219: if(player.getLevel() < 2){ htmlid player.sendPackets(new
			 * S_NPCTalkReturn(objid, "adminrw3")); }else if(player.getLevel() < 3){
			 * player.setExp(ExpTable.getExpByLevel(3)); }else if(player.getLevel() < 5){
			 * player.sendPackets(new S_NPCTalkReturn(objid, "adminrw1")); }else
			 * if(player.getLevel() < 6){ player.setExp(ExpTable.getExpByLevel(6));
			 * player.sendPackets(new S_NPCTalkReturn(objid, "newadmin2")); }else{
			 * player.sendPackets(new S_NPCTalkReturn(objid, "newadmin2")); } break;
			 */
			case 50036: // 윌마 기란
			case 50020: // 스텐리 켄트
			case 50024: // 아스터 글말
			case 50054: // 트레이 윈다우드
			case 50066: // 리올 하이네
			case 50039: // 레슬리 웰던
			case 50051: // 키리우스 오렌
			case 50044: // 시리우스 아덴
			case 50046: // 엘레리스 아덴
				if (player.getLevel() < 45)
					htmlid = "starttel1";
				else if (player.getLevel() < 52)
					htmlid = "starttel2";
				else
					htmlid = "starttel3";
				break;
			case 100573: // 시장 텔레포터
				if (player.getLevel() < 45)
					htmlid = "linge1";
				else if (player.getLevel() < 52)
					htmlid = "linge2";
				else
					htmlid = "linge3";
				break;
			}
			// html 표시
			if (htmlid != null) { // htmlid가 지정되고 있는 경우
				S_NPCTalkReturn nt = new S_NPCTalkReturn(objid, htmlid);
				player.sendPackets(nt, true);
			} else {
				if (player.getLawful() < -1000) { // 플레이어가 카오틱
					S_NPCTalkReturn nt = new S_NPCTalkReturn(talking, objid, 2);
					player.sendPackets(nt, true);
				} else {
					S_NPCTalkReturn nt = new S_NPCTalkReturn(talking, objid, 1);
					player.sendPackets(nt, true);
				}
			}
		} else {
			_log.finest((new StringBuilder()).append("No actions for npc id : ").append(objid).toString());
		}
		htmlid = null;
	}

	@Override
	public void onFinalAction(L1PcInstance player, String action) {
		if (player.getMapId() == 6202) {
			return;
		}
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		if (getNpcId() == 100746) {
			if (player.getInventory().getSize() >= 180)
				return;
			if (action.equalsIgnoreCase("enter")) {
				// if (player.getInventory().checkItem(60511)) {
				Config._IND_Q.requestWork(new IND(player.getName(), 6));
				// } else {
				// player.sendPackets(new S_NPCTalkReturn(objid, "ctid1"));
				// }
			}
			return;
		}
		if (action.equalsIgnoreCase("teleportURL")) {
			L1NpcHtml html = new L1NpcHtml(talking.getTeleportURL());
			String[] price = null;
			int npcid = getNpcTemplate().get_npcId();
			switch (npcid) {
			case 100438: // 요숲 엘루나
				price = new String[] { "28", "28", "28", "69", "69", "69", "69", "103", "103", "115", "115", "345" };
				break;
			case 50015: // 말하는섬 루카
				price = new String[] { "1500" };
				break;
			case 15000001: // 피터^텔레포터15000001
				price = new String[] { "7000", "7000", "7000", "14000", "14000" };
				break;
			case 50017: // 말하는 섬 케이스
				price = new String[] { "50" };
				break;
			case 50020: // 켄트 스탠리
				price = new String[] { "50", "50", "50", "120", "120", "120", "120", "180", "180", "200", "200", "600", "7100", "1155", "10000" };
				break;
			case 50024: // 글루디오 아스터
				price = new String[] { "75", "75", "75", "180", "180", "270", "270", "270", "360", "360", "360", "300", "300", "750", "10200", "1155",
						"10000" };
				break;
			case 50026: // 그르딘 시장⇒기란 시장, 오렌 시장, 실버 나이트 타운 시장
				price = new String[] { "550", "700", "810" };
				break;
			case 50033: // 기란 시장⇒그르딘 시장, 오렌 시장, 실버 나이트 타운 시장
				price = new String[] { "560", "720", "560" };
				break;
			case 50035: // 기란성 게이트 기퍼
				price = new String[] { "210", "210", "420", "210" };
				break;
			case 50036: // 기란 윌마
				price = new String[] { "75", "75", "75", "180", "180", "180", "180", "270", "270", "450", "450", "1050", "11100", "1155", "10000" };
				break;
			case 50039: // 웰던 레슬리
				price = new String[] { "72", "72", "174", "174", "261", "261", "261", "348", "348", "1160", "580", "580", "11165", "1155", "10000" };
				break;
			case 50040: // 난성 게이트 키퍼
				price = new String[] { "210", "420", "210" };
				break;
			case 50044: // 아덴 시리우스
			case 50046: // 아덴 엘레리스
				price = new String[] { "70", "168", "168", "252", "252", "252", "336", "336", "420", "1260", "700", "700", "10360", "1155", "10000" };
				break;
			case 50049: // 오렌 시장⇒그르딘 시장, 기란 시장, 실버 나이트 타운 시장
				price = new String[] { "1150", "980", "590" };
				break;
			case 50051: // 오렌키리우스
				price = new String[] { "75", "180", "270", "270", "360", "360", "360", "450", "450", "1350", "750", "750", "12000", "1155", "10000" };
				break;
			case 50054: // 윈다우드트레이
				price = new String[] { "75", "75", "180", "180", "180", "270", "270", "360", "450", "300", "300", "750", "9750", "1155", "10000" };
				break;
			case 50056: // 은기사마을 메트
				price = new String[] { "75", "75", "75", "180", "180", "180", "270", "270", "270", "360", "360", "450", "450", "1050", "10200", "10000" };
				break;
			case 50059: // 실버 나이트 타운 시장⇒그르딘 시장, 기란 시장, 오렌 시장
				price = new String[] { "580", "680", "680" };
				break;
			case 50063: // 오크 요새 게이트 키퍼
				price = new String[] { "210", "420", "210" };
				break;
			case 50066: // 하이네리올
				price = new String[] { "990", "450", "400", "550", "400", "710", "350", "680", "1000", "1050", "180", "180", "3200", "1155", "6900" };
				break;
			case 50068: // 디아노스
				price = new String[] { "1500", "800", "600", "1800", "1800", "1000" };
				break;
			case 50072: // 공간이동사 디아루즈
				price = new String[] { "2200", "1800", "1000", "1600", "2200", "1200", "1300", "2000", "2000" };
				break;
			case 50073: // 공간이동사 디아베스
				price = new String[] { "380", "850", "290", "290", "290", "180", "480", "150", "150", "380", "480", "380", "850", "1000" };
				break;
			case 50079: // 마법사 다니엘
				price = new String[] { "550", "550", "550", "600", "600", "600", "650", "700", "750", "750", "500", "500", "700" };
				break;
			case 4208002: // 마법사멀린
				break;
			case 4918000: // 데카비아 베히모스
				price = new String[] { "50", "50", "50", "50", "120", "120", "180", "180", "180", "240", "240", "400", "400", "800", "7700" };
				break;
			case 4919000: // 실베리아 샤리엘
				price = new String[] { "50", "50", "50", "120", "180", "180", "240", "240", "240", "300", "300", "500", "500", "900", "8000" };
				break;
			case 6000014: // 시종장 맘몬
				price = new String[] { "14000" };
				break;
			case 6000016: // 신녀 플로라
				price = new String[] { "1000" };
				break;
			case 100573: // 린지
				price = new String[] { "82", "82", "82", "198", "198", "198", "198", "297", "297", "495", "495", "495", "1155", "12210" };
				break;
			default:
				price = new String[] { "" };
				break;
			}
			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLA")) {

			String html = "";
			String[] price = null;
			int npcid = getNpcTemplate().get_npcId();
			if (npcid == 50079) {
				html = "telediad3";
				price = new String[] { "700", "800", "800", "1000", "10000" };
			} else if (npcid == 4918000) {
				html = "dekabia3";
				price = new String[] { "100", "220", "220", "220", "330", "330", "330", "330", "440", "440" };
			} else if (npcid == 4919000) {
				html = "sharial3";
				price = new String[] { "220", "330", "330", "330", "440", "440", "550", "550", "550", "550" };
			} else {
				price = new String[] { "" };
			}
			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLB")) {
			String html = "guide_1_1";
			String[] price = null;

			price = new String[] { "450", "450", "450", "450" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLC")) {
			String html = "guide_1_2";
			String[] price = null;

			price = new String[] { "465", "465", "465", "465", "1065", "1065" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLD")) {
			String html = "guide_1_3";
			String[] price = null;

			price = new String[] { "480", "480", "480", "480", "630", "1080", "630" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLE")) {
			String html = "guide_2_1";
			String[] price = null;

			price = new String[] { "600", "600", "750", "750" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLF")) {
			String html = "guide_2_2";
			String[] price = null;

			price = new String[] { "615", "615", "915", "765" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLG")) {
			String html = "guide_2_3";
			String[] price = null;

			price = new String[] { "630", "780", "630", "1080", "930" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLH")) {
			String html = "guide_3_1";
			String[] price = null;

			price = new String[] { "750", "750", "750", "1200", "1050" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLI")) {
			String html = "guide_3_2";
			String[] price = null;

			price = new String[] { "765", "765", "765", "765", "1515", "1215", "915" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLJ")) {
			String html = "guide_3_3";
			String[] price = null;

			price = new String[] { "780", "780", "780", "780", "780", "1080", "1080", "1080" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLK")) {
			String html = "guide_4";
			String[] price = null;

			price = new String[] { "780", "780", "780", "780", "780", "1080", "1080", "1080" };

			player.sendPackets(new S_NPCTalkReturn(objid, html, price));
			html = null;
			price = null;
		} else if (action.equalsIgnoreCase("teleportURLL")) {
			if (getNpcId() == 50056) { // 은말 텔녀
				String html = "guide_0_1";
				String[] price = null;

				price = new String[] { "15", "15", "15", "35", "40", "45", "50" };

				player.sendPackets(new S_NPCTalkReturn(objid, html, price));
				html = null;
				price = null;
			} else {
				String html = "guide_6";
				String[] price = new String[] { "700", "700" };
				player.sendPackets(new S_NPCTalkReturn(objid, html, price), true);
				html = null;
				price = null;
			}
		} else if (action.equalsIgnoreCase("teleportURLM")) {
			if (getNpcId() == 50056) { // 은말 텔녀
				player.sendPackets(new S_NPCTalkReturn(objid, "hp_storm1"));
				return;
			} else {
				String html = "guide_7";
				String[] price = new String[] { "700", "700", "700", "700", "700", "700", "700", "700", "700", "700", "700", };
				player.sendPackets(new S_NPCTalkReturn(objid, html, price), true);
				html = null;
				price = null;
			}
		} else if (action.equalsIgnoreCase("teleportURLQ")) {
			String html = "guide_10";
			// String[] price = null;

			// price = new String[]{
			// "780","780","780","780","780","1080","1080","1080" };

			player.sendPackets(new S_NPCTalkReturn(objid, html));
			html = null;// price = null;
		} else {
			if (getNpcId() == 50036 || getNpcId() == 50020 || getNpcId() == 50024 || getNpcId() == 50054 || getNpcId() == 50066 || getNpcId() == 50039
					|| getNpcId() == 50051 || getNpcId() == 50044 || getNpcId() == 50046) {
				if (action.equalsIgnoreCase("teleport guide7_1_1")) {
					TeleGuide(player, 32694, 33496, 4);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_2")) {
					TeleGuide(player, 32861, 33251, 4);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_3")) {
					TeleGuide(player, 32699, 33255, 4);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_4")) {
					TeleGuide(player, 32864, 33108, 4);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_5")) {
					TeleGuide(player, 34184, 32244, 4);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_6")) {
					TeleGuide(player, 34049, 32478, 4);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_7")) {
					TeleGuide(player, 33907, 32277, 4);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_8")) {
					TeleGuide(player, 32678, 32857, 0);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_9")) {
					TeleGuide(player, 32513, 33025, 0);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_10")) {
					TeleGuide(player, 32535, 32823, 0);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_11")) {
					TeleGuide(player, 32474, 32858, 0);
					return;
				} else if (action.equalsIgnoreCase("teleport guide7_1_12")) {
					TeleGuide(player, 32843, 32694, 550);
					return;
				}
			}
		}
		if (action.startsWith("teleport")) {
			_log.finest((new StringBuilder()).append("Setting action to : ").append(action).toString());
			doFinalAction(player, action);
		} else if (getNpcId() == 100287) {
			try {
				if (action.equalsIgnoreCase("R") || action.equalsIgnoreCase("S") || action.equalsIgnoreCase("T") || action.equalsIgnoreCase("U")) { // 수상한감옥
					/*
					 * int time = 0; int outtime = 3600; Timestamp nowday = new
					 * Timestamp(System.currentTimeMillis()); if(player.getivoryyaheeday() == null){
					 * player.setivoryyaheetime(1); player.setivoryyaheeday(nowday); player.save();
					 * player.sendPackets(new S_SystemMessage(player, "던전 체류 시간이 1시간 남았습니다."),
					 * true); if(action.equalsIgnoreCase("R")){ L1Teleport.teleport(player, 32901,
					 * 32765, (short) 285, 5, true, true, 5000); }else
					 * if(action.equalsIgnoreCase("S")){ L1Teleport.teleport(player, 32808, 32862,
					 * (short) 286, 5, true, true, 5000); }else if(action.equalsIgnoreCase("T")){
					 * L1Teleport.teleport(player, 32738, 32795, (short) 287, 5, true, true, 5000);
					 * }else if(action.equalsIgnoreCase("U")){ L1Teleport.teleport(player, 32732,
					 * 32798, (short) 288, 5, true, true, 5000); } }else{ time =
					 * player.getivoryyaheetime(); if (player.getivoryyaheeday().getDate() !=
					 * nowday.getDate()){ player.setivoryyaheetime(1);
					 * player.setivoryyaheeday(nowday); player.save(); player.sendPackets(new
					 * S_SystemMessage(player, "던전 체류 시간이 1시간 남았습니다."), true);
					 * if(action.equalsIgnoreCase("R")){ L1Teleport.teleport(player, 32901, 32765,
					 * (short) 285, 5, true, true, 5000); }else if(action.equalsIgnoreCase("S")){
					 * L1Teleport.teleport(player, 32808, 32862, (short) 286, 5, true, true, 5000);
					 * }else if(action.equalsIgnoreCase("T")){ L1Teleport.teleport(player, 32738,
					 * 32795, (short) 287, 5, true, true, 5000); }else
					 * if(action.equalsIgnoreCase("U")){ L1Teleport.teleport(player, 32732, 32798,
					 * (short) 288, 5, true, true, 5000); } }else if(outtime > time){ int c =
					 * (outtime - time) / 60 % 60; player.sendPackets(new S_SystemMessage(player,
					 * "던전 체류 시간이 "+c+"분 남았습니다."), true); if(action.equalsIgnoreCase("R")){
					 * L1Teleport.teleport(player, 32901, 32765, (short) 285, 5, true, true, 5000);
					 * }else if(action.equalsIgnoreCase("S")){ L1Teleport.teleport(player, 32808,
					 * 32862, (short) 286, 5, true, true, 5000); }else
					 * if(action.equalsIgnoreCase("T")){ L1Teleport.teleport(player, 32738, 32795,
					 * (short) 287, 5, true, true, 5000); }else if(action.equalsIgnoreCase("U")){
					 * L1Teleport.teleport(player, 32732, 32798, (short) 288, 5, true, true, 5000);
					 * } }else{ player.sendPackets(new S_SystemMessage(player,
					 * "던전 체류 가능 시간 1시간을 모두 사용하셨습니다."), true); } } /* Random random = new Random();
					 * int gn1 =random.nextInt(3); int time = 0; int outtime = 60 * 60 * 2;
					 * Timestamp nowday = new Timestamp(System.currentTimeMillis()); random = null;
					 * if(player.get수상한감옥day() == null){ player.set수상한감옥time(1);
					 * player.set수상한감옥day(nowday); player.save(); player.sendPackets(new
					 * S_SystemMessage(player, "던전 체류 시간이 2시간 남았습니다."), true);
					 * L1Teleport.teleport(player, 32806 + gn1, 32732 + gn1, (short) 53, 5, true,
					 * true, 5000); }else{ time = player.get수상한감옥time(); if
					 * (player.get수상한감옥day().getDate() != nowday.getDate()){ player.set수상한감옥time(1);
					 * player.save(); player.sendPackets(new S_SystemMessage(player,
					 * "던전 체류 시간이 2시간 남았습니다."), true); L1Teleport.teleport(player, 32806 + gn1,
					 * 32732 + gn1, (short) 53, 5, true, true, 5000); }else if(outtime > time){ int
					 * h = (outtime - time) / 60 / 60; if(h < 0 ){ h = 0; } int m = (outtime - time)
					 * / 60 % 60; if(m < 0){ m= 0; }
					 * 
					 * if(h > 0){ player.sendPackets(new S_SystemMessage(player,
					 * "던전 체류 시간이 "+h+"시간 "+m+"분 남았습니다."), true); }else{ player.sendPackets(new
					 * S_SystemMessage(player, "던전 체류 시간이 "+m+"분 남았습니다."), true); }
					 * L1Teleport.teleport(player, 32806 + gn1, 32732 + gn1, (short) 53, 5, true,
					 * true, 5000); }else{ player.sendPackets(new S_SystemMessage(player,
					 * "던전 체류 가능 시간 2시간을 모두 사용하셨습니다."), true); } }
					 */
				}
			} catch (Exception e) {
			}
		}
	}

	private void TeleGuide(L1PcInstance player, int x, int y, int mapid) {
		if (!player.getInventory().checkItem(L1ItemId.ADENA, 700)) {
			player.sendPackets(new S_ServerMessage(337, "$4"), true);
			return;
		}
		player.getInventory().consumeItem(L1ItemId.ADENA, 700);
		L1Teleport.teleport(player, x, y, (short) mapid, 6, true, true, 5000);
	}

	private void doFinalAction(L1PcInstance player, String action) {
		try {
			if (action.equalsIgnoreCase("D_giran") || action.equalsIgnoreCase("D_giran") || action.equalsIgnoreCase("D_giran")) { // 기란던전
				Random random = new Random();
				int gn1 = random.nextInt(3);

				int usetime = player.getgirantime();
				int outtime = 60 * 60 * 3;
				Timestamp nowday = new Timestamp(System.currentTimeMillis());
				try {
					String s1 = isPC입장가능여부(player.getgiranday(), outtime, usetime);
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
							player.sendPackets(new S_ServerMessage(1525, h + "", m + ""));// %시간 %분남았습니다.
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+h+"시간 "+m+"분 남았습니다."), true);
						} else {
							// pc.sendPackets(new S_SystemMessage(pc,
							// "던전 체류 가능시간이 "+m+"분 남았습니다."), true);
							player.sendPackets(new S_ServerMessage(1527, "" + m + ""));// 분 남았다
						}
						if (action.equalsIgnoreCase("D_giran")) {
							L1Teleport.teleport(player, 32809, 32794, (short) 54, 5, true, true, 5000);
						} else {
							L1Teleport.teleport(player, 32806 + gn1, 32732 + gn1, (short) 53, 5, true, true, 5000);
						}
					} else if (s1.equals("불가능")) {// 입장불가능
						player.sendPackets(new S_ServerMessage(1522, "3"));// 5시간 모두 사용했다.
						return;
					} else if (s1.equals("초기화")) {// 초기화
						player.setgirantime(1);
						player.setgiranday(nowday);
						player.save();
						player.sendPackets(new S_ServerMessage(1526, "3"));// 시간 남았다.
						if (action.equalsIgnoreCase("D_giran")) {
							L1Teleport.teleport(player, 32809, 32794, (short) 54, 5, true, true, 5000);
						} else {
							L1Teleport.teleport(player, 32806 + gn1, 32732 + gn1, (short) 53, 5, true, true, 5000);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

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
					// 24시간이 지낫거나 오전9시이후라면
					return start;
				}
			} else {
				if (lasthours < Config.D_Reset_Time && hours >= Config.D_Reset_Time) {// 같은날 9시이전에 들어간거체크
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
	private static Logger _log = Logger.getLogger(l1j.server.server.model.Instance.L1TeleporterInstance.class.getName());

}