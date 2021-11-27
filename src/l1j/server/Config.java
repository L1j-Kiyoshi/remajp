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
 * http://www.gnu.org/copyleft/gpl.html1
 */
package l1j.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.utils.IntRange;
import server.GameServer;

public final class Config {
	public static IND_Q _IND_Q = null;
	public static quit_Q _quit_Q = null;
	public static INN_Q _INN_Q = null;
	private static final Logger _log = Logger.getLogger(Config.class.getName());
	public static int craft = 1005;
	public static boolean mainenc = false;
	public static boolean STANDBY_SERVER = false;
	public static int test222 = 3;
	public static boolean 새로운패킷구조 = false;
	public static boolean 서버패킷출력 = false;
	public static boolean 클라패킷출력 = false;
	private static final String servername = "테스트";

	public static ArrayList<L1PrivateShopSellList> sellList = new ArrayList<L1PrivateShopSellList>();

	public static ArrayList<L1PrivateShopBuyList> buyList = new ArrayList<L1PrivateShopBuyList>();

	public static byte[] FIRST_PACKET = { (byte) 0x0b, (byte) 0x00, // size
			/** 190512 리마 by feel. **/
			(byte) 0xeb, (byte) 0xd6, (byte) 0x45, (byte) 0x9d, (byte) 0x02, (byte) 0xfe, (byte) 0xb0, (byte) 0xc8, (byte) 0x0a };

//	S_INITPACKET : 235 [0xeb] / Length : 11   (07:16)
//	eb f0 7c ab 50 d2 8a b3 	7a

	public static long SeedVal = 0x029d45d6L; // 리마

	public static void addSellList(L1PrivateShopSellList sellitem) {
		sellList.add(sellitem);
	}

	public static void SellListClear(L1PcInstance pc) {
		for (L1PrivateShopSellList slist : pc.getSellList())
			if (sellList.contains(slist))
				sellList.remove(slist);
	}

	public static void addBuyList(L1PrivateShopBuyList buyitem) {
		buyList.add(buyitem);
	}

	public static void BuyListClear(L1PcInstance pc) {
		for (L1PrivateShopBuyList blist : pc.getBuyList())
			if (buyList.contains(blist))
				buyList.remove(blist);
	}

	public static int 이름확인(char[] _values) {
		for (char value : _values) {
			if ((value >= 'A' && value <= 'z') || (value >= 'a' && value <= 'z')) {
				// System.out.println("영어 : "+value);
			} else if (value >= '0' && value <= '9') {
				// System.out.println("숫자 : "+value);
			} else if (value >= '\uAC00' && value <= '\uD7A3') {
				// System.out.println("한글 : "+value);
			} else {
				return -1;
			}
		}
		return 0;
		// .... 이하 생략
	}

	public static ArrayList<Integer> spractionerr = new ArrayList<Integer>();

	public static int teste = 8700;
	public static boolean 폰인증 = false;
	public static int test = 0;
	public static int aaaaa = 0;

	public synchronized static void addaa() {
		aaaaa++;
	}

	public static int iitest = 0;

	public static String getserver() {
		return servername;
	}

	/** Debug/release mode */
	public static final boolean DEBUG = false;
	// public static boolean 패킷로그 = true;//로그

	public static final int 정무시간 = 60 * 90;
	public static final int PC정무시간 = 60 * 60 * 2;

	public static final int 계정_용의둥지_시간 = 60 * 60 * 3;
	public static final int 검은전함 = 60 * 60 * 2;
	public static final int PC_용의둥지_시간 = 60 * 60 * 3;

	public static int 기란감옥던전시간 = 7200;
	public static int 상아탑발록진영시간 = 7200;
	public static int 상아탑야히진영시간 = 3600;
	public static int 얼음수정동굴시간 = 3600;
	public static int 수상한천상의계곡시간 = 3600;
	public static int 말섬던전시간 = 3600;
	public static int 몽환의섬시간 = 3600;
	public static int 라스타바드던전시간 = 3600;
	public static int PROTECT_CLAN_ID;
	public static int AUTO_REMOVECLAN;
	public static boolean 신규혈맹보호처리;
	public static boolean CLAN_BLESS_ALL_USE;
	public static int 신규레벨;
	public static int 인형확률2;
	public static int 인형확률3;
	public static int 인형확률4;
	public static int 인형확률5;
	/** 보호 주문서 확률 차감 */
	public static int 보호주문서확률0;
	public static int 보호주문서확률1;
	public static int 보호주문서확률2;
	public static int 보호주문서확률3;
	public static int 보호주문서확률4;
	public static int 보호주문서확률5;
	public static int 보호주문서확률6;

	public static int 출석초기화시간;
	public static int AINHASADDPRESETTIME;

	// 제작테이블
	public static int CRAFT_TABLE_ONE;
	public static int CRAFT_TABLE_TWO;
	public static int CRAFT_TABLE_THREE;
	public static int CRAFT_TABLE_FOUR;
	public static int CRAFT_TABLE_FIVE;
	public static int CRAFT_TABLE_SIX;
	public static int CRAFT_TABLE_SEVEN;
	public static int CRAFT_TABLE_EIGHT;
	public static int CRAFT_TABLE_NINE;
	public static int CRAFT_TABLE_TEN;
	public static int CRAFT_TABLE;

	protected static ArrayList<L1PcInstance> 혈맹채팅모니터 = new ArrayList<L1PcInstance>();
	protected static ArrayList<L1PcInstance> 파티채팅모니터 = new ArrayList<L1PcInstance>();
	protected static ArrayList<L1PcInstance> 귓말채팅모니터 = new ArrayList<L1PcInstance>();
	protected static ArrayList<L1PcInstance> 인첸채팅모니터 = new ArrayList<L1PcInstance>();
	protected static ArrayList<L1PcInstance> 삭제채팅모니터 = new ArrayList<L1PcInstance>();
	protected static ArrayList<L1PcInstance> 접속채팅모니터 = new ArrayList<L1PcInstance>();

	protected static ArrayList<L1PcInstance> 버그채팅모니터 = new ArrayList<L1PcInstance>();

	public static int 혈맹채팅모니터() {
		return 혈맹채팅모니터.size();
	}

	public static int 파티채팅모니터() {
		return 파티채팅모니터.size();
	}

	public static int 귓말채팅모니터() {
		return 귓말채팅모니터.size();
	}

	public static int 인첸채팅모니터() {
		return 인첸채팅모니터.size();
	}

	public static int 삭제채팅모니터() {
		return 삭제채팅모니터.size();
	}

	public static int 접속채팅모니터() {
		return 접속채팅모니터.size();
	}

	public static int 버그채팅모니터() {
		return 버그채팅모니터.size();
	}

	public static L1PcInstance[] toArray혈맹채팅모니터() {
		return 혈맹채팅모니터.toArray(new L1PcInstance[혈맹채팅모니터.size()]);
	}

	public static L1PcInstance[] toArray파티채팅모니터() {
		return 파티채팅모니터.toArray(new L1PcInstance[파티채팅모니터.size()]);
	}

	public static L1PcInstance[] toArray귓말채팅모니터() {
		return 귓말채팅모니터.toArray(new L1PcInstance[귓말채팅모니터.size()]);
	}

	public static L1PcInstance[] toArray인첸채팅모니터() {
		return 인첸채팅모니터.toArray(new L1PcInstance[인첸채팅모니터.size()]);
	}

	public static L1PcInstance[] toArray삭제채팅모니터() {
		return 삭제채팅모니터.toArray(new L1PcInstance[삭제채팅모니터.size()]);
	}

	public static L1PcInstance[] toArray접속채팅모니터() {
		return 접속채팅모니터.toArray(new L1PcInstance[접속채팅모니터.size()]);
	}

	public static L1PcInstance[] toArray버그채팅모니터() {
		return 버그채팅모니터.toArray(new L1PcInstance[버그채팅모니터.size()]);
	}

	public static void add전체(L1PcInstance pc) {
		if (!혈맹채팅모니터.contains(pc)) {
			혈맹채팅모니터.add(pc);
			;
		}
		if (!파티채팅모니터.contains(pc)) {
			파티채팅모니터.add(pc);
			;
		}
		if (!귓말채팅모니터.contains(pc)) {
			귓말채팅모니터.add(pc);
			;
		}
		if (!인첸채팅모니터.contains(pc)) {
			인첸채팅모니터.add(pc);
			;
		}
		if (!삭제채팅모니터.contains(pc)) {
			삭제채팅모니터.add(pc);
			;
		}
		if (!접속채팅모니터.contains(pc)) {
			접속채팅모니터.add(pc);
			;
		}
		if (!버그채팅모니터.contains(pc)) {
			버그채팅모니터.add(pc);
			;
		}
	}

	public static void remove전체(L1PcInstance pc) {
		if (혈맹채팅모니터.contains(pc)) {
			혈맹채팅모니터.remove(pc);
			;
		}
		if (파티채팅모니터.contains(pc)) {
			파티채팅모니터.remove(pc);
			;
		}
		if (귓말채팅모니터.contains(pc)) {
			귓말채팅모니터.remove(pc);
			;
		}
		if (인첸채팅모니터.contains(pc)) {
			인첸채팅모니터.remove(pc);
			;
		}
		if (삭제채팅모니터.contains(pc)) {
			삭제채팅모니터.remove(pc);
			;
		}
		if (접속채팅모니터.contains(pc)) {
			접속채팅모니터.remove(pc);
			;
		}
		if (버그채팅모니터.contains(pc)) {
			버그채팅모니터.remove(pc);
			;
		}
	}

	public static void add혈맹(L1PcInstance pc) {
		if (혈맹채팅모니터.contains(pc)) {
			return;
		}
		혈맹채팅모니터.add(pc);
	}

	public static void remove혈맹(L1PcInstance pc) {
		if (!혈맹채팅모니터.contains(pc)) {
			return;
		}
		혈맹채팅모니터.remove(pc);
	}

	public static void add파티(L1PcInstance pc) {
		if (파티채팅모니터.contains(pc)) {
			return;
		}
		파티채팅모니터.add(pc);
	}

	public static void remove파티(L1PcInstance pc) {
		if (!파티채팅모니터.contains(pc)) {
			return;
		}
		파티채팅모니터.remove(pc);
	}

	public static void add귓말(L1PcInstance pc) {
		if (귓말채팅모니터.contains(pc)) {
			return;
		}
		귓말채팅모니터.add(pc);
	}

	public static void remove귓말(L1PcInstance pc) {
		if (!귓말채팅모니터.contains(pc)) {
			return;
		}
		귓말채팅모니터.remove(pc);
	}

	public static void add인첸(L1PcInstance pc) {
		if (인첸채팅모니터.contains(pc)) {
			return;
		}
		인첸채팅모니터.add(pc);
	}

	public static void remove인첸(L1PcInstance pc) {
		if (!인첸채팅모니터.contains(pc)) {
			return;
		}
		인첸채팅모니터.remove(pc);
	}

	public static void add삭제(L1PcInstance pc) {
		if (삭제채팅모니터.contains(pc)) {
			return;
		}
		삭제채팅모니터.add(pc);
	}

	public static void remove삭제(L1PcInstance pc) {
		if (!삭제채팅모니터.contains(pc)) {
			return;
		}
		삭제채팅모니터.remove(pc);
	}

	public static void add접속(L1PcInstance pc) {
		if (접속채팅모니터.contains(pc)) {
			return;
		}
		접속채팅모니터.add(pc);
	}

	public static void remove접속(L1PcInstance pc) {
		if (!접속채팅모니터.contains(pc)) {
			return;
		}
		접속채팅모니터.remove(pc);
	}

	public static void add버그(L1PcInstance pc) {
		if (버그채팅모니터.contains(pc)) {
			return;
		}
		버그채팅모니터.add(pc);
	}

	public static void remove버그(L1PcInstance pc) {
		if (!버그채팅모니터.contains(pc)) {
			return;
		}
		버그채팅모니터.remove(pc);
	}

	public static boolean 뚫어방어사용;
	public static boolean 유체이탈방어사용;

	/** Thread pools size */
	public static int THREAD_P_EFFECTS;
	public static int THREAD_P_GENERAL;
	public static int AI_MAX_THREAD;
	public static int THREAD_P_TYPE_GENERAL;
	public static int THREAD_P_SIZE_GENERAL;

	/** Server control */
	public static int GAME_SERVER_TYPE;
	public static String GAME_SERVER_HOST_NAME;
	public static int GAME_SERVER_PORT;
	public static String DB_DRIVER;
	public static String DB_URL;
	public static String DB_LOGIN;
	public static String DB_PASSWORD;
	public static String TIME_ZONE;
	public static int CLIENT_LANGUAGE;

	public static boolean HOSTNAME_LOOKUPS;
	public static int AUTOMATIC_KICK;
	public static boolean AUTO_CREATE_ACCOUNTS;
	public static short MAX_ONLINE_USERS;
	public static boolean CACHE_MAP_FILES;
	public static boolean LOAD_V2_MAP_FILES;
	public static boolean CHECK_MOVE_INTERVAL;
	public static boolean CHECK_ATTACK_INTERVAL;
	public static boolean CHECK_SPELL_INTERVAL;
	public static short INJUSTICE_COUNT;
	public static int JUSTICE_COUNT;
	public static int CHECK_STRICTNESS;
	public static byte LOGGING_WEAPON_ENCHANT;
	public static byte LOGGING_ARMOR_ENCHANT;
	public static int LOGGING_TIME;
	public static boolean LOGGING_CHAT_NORMAL;
	public static boolean LOGGING_CHAT_WHISPER;
	public static boolean LOGGING_CHAT_SHOUT;
	public static boolean LOGGING_CHAT_WORLD;
	public static boolean LOGGING_CHAT_CLAN;
	public static boolean LOGGING_CHAT_PARTY;
	public static boolean LOGGING_CHAT_COMBINED;
	public static boolean LOGGING_CHAT_CHAT_PARTY;
	public static int ENCHANT_CHANCE_ACCESSORY;
	public static int 활명중;
	public static int 라던수표;

	public static boolean 배틀존작동유무;
	public static int 배틀존입장레벨;
	public static String 배틀존아이템;
	public static String 배틀존아이템갯수;

	public static String 도감1단아이템;
	public static String 도감1단아이템갯수;

	public static String 도감2단아이템;
	public static String 도감2단아이템갯수;

	public static String 도감3단아이템;
	public static String 도감3단아이템갯수;

	public static boolean Tam_Ok;
	public static int Tam_Time;
	public static int Tam_Count;

	public static int D_Reset_Time;

	public static boolean Event_Box;

	public static int AUTOSAVE_INTERVAL;
	public static int AUTOSAVE_INTERVAL_INVENTORY;
	public static int SKILLTIMER_IMPLTYPE;
	public static int NPCAI_IMPLTYPE;
	public static boolean TELNET_SERVER;
	public static int TELNET_SERVER_PORT;
	public static int PC_RECOGNIZE_RANGE;
	public static boolean CHARACTER_CONFIG_IN_SERVER_SIDE;
	public static boolean ALLOW_2PC;
	public static int LEVEL_DOWN_RANGE;
	public static boolean SEND_PACKET_BEFORE_TELEPORT;
	public static boolean DETECT_DB_RESOURCE_LEAKS;
	public static boolean AUTO_CHECK;

	public static boolean WAR_TIME_AUTO_SETTING;

	/** Rate control */
	public static int 스냅퍼최대인챈;
	public static int 보호주문서최대인챈;
	public static int 조이의유물;
	public static int 아인하사드데일리포인트;
	public static int 룸티스최대인챈;
	public static int 장신구최대인챈;
	public static double RATE_XP;
	public static double fishrate;
	public static double RATE_LAWFUL;
	public static double RATE_KARMA;
	public static double RATE_DROP_ADENA;
	public static double RATE_DROP_ITEMS;
	public static int RATE_ROBOT_TIME; // 무인PC(쿠우)
	public static int ENCHANT_CHANCE_WEAPON;
	public static int ENCHANT_CHANCE_WEAPON7;
	public static int ENCHANT_CHANCE_WEAPON8;
	public static int ENCHANT_CHANCE_WEAPON9;
	public static int ENCHANT_CHANCE_WEAPON10;
	public static int ENCHANT_CHANCE_ARMOR;
	public static double RATE_WEIGHT_LIMIT;
	public static double RATE_WEIGHT_LIMIT_PET;
	public static double RATE_SHOP_SELLING_PRICE;
	public static double RATE_SHOP_PURCHASING_PRICE;
	public static int RATE_DREAM; // 만월의정기 이벤트드랍
	public static int CREATE_CHANCE_DIARY;
	public static int CREATE_CHANCE_RECOLLECTION;
	public static int CREATE_CHANCE_MYSTERIOUS;
	public static int CREATE_CHANCE_PROCESSING;
	public static int CREATE_CHANCE_PROCESSING_DIAMOND;
	public static int CREATE_CHANCE_DANTES;
	public static int CREATE_CHANCE_ANCIENT_AMULET;
	public static int CREATE_CHANCE_HISTORY_BOOK;
	public static int MAX_WEAPON;
	public static int MAX_ARMOR;
	public static int MAX_WEAPON1; // 마족무기 인첸제한
	public static int MAX_LEVEL; // 신규보호렙제
	public static int FEATHER_TIME;
	public static int FEATHER_NUMBER;
	public static int CLAN_NUMBER;
	public static int CASTLE_NUMBER;
	public static double RATE_CLAN_XP;
	public static double RATE_CASTLE_XP;
	public static double RATE_7_DMG_RATE;// 인첸추타 외부화
	public static int RATE_7_DMG_PER;
	public static double RATE_8_DMG_RATE;
	public static int RATE_8_DMG_PER;
	public static double RATE_9_DMG_RATE;
	public static int RATE_9_DMG_PER;
	public static double RATE_10_DMG_RATE;
	public static int RATE_10_DMG_PER;
	public static double RATE_11_DMG_RATE;
	public static int RATE_11_DMG_PER;
	public static double RATE_12_DMG_RATE;
	public static int RATE_12_DMG_PER;
	public static double RATE_13_DMG_RATE;
	public static int RATE_13_DMG_PER;
	public static double RATE_14_DMG_RATE;
	public static int RATE_14_DMG_PER;
	public static double RATE_15_DMG_RATE;
	public static int RATE_15_DMG_PER;
	public static double RATE_16_DMG_RATE;
	public static int RATE_16_DMG_PER;
	public static double RATE_17_DMG_RATE;
	public static int RATE_17_DMG_PER;
	public static double RATE_18_DMG_RATE;
	public static int RATE_18_DMG_PER;
	public static int AC_170;
	public static int AC_160;
	public static int AC_150;
	public static int AC_140;
	public static int AC_130;
	public static int AC_120;
	public static int AC_110;
	public static int AC_100;
	public static int AC_90;
	public static int AC_80;
	public static int AC_70;
	public static int AC_60;
	public static int AC_50;
	public static int AC_40;
	public static int AC_30;
	public static int AC_20;
	public static int AC_10;

	//// 자동공지
	public static int systime;
	public static String sys1;
	public static String sys2;
	public static String sys3;
	public static String sys4;
	public static String sys5;
	public static String sys6;
	public static String sys7;
	public static String sys8;
	public static String sys9;
	public static String sys10;
	public static String sys11;
	public static String sys12;
	public static String sys13;
	public static String sys14;
	public static String sys15;
	public static String sys16;

	/** AltSettings control */
	public static short GLOBAL_CHAT_LEVEL;
	public static short WHISPER_CHAT_LEVEL;
	public static byte AUTO_LOOT;
	public static int LOOTING_RANGE;
	public static boolean ALT_NONPVP;
	public static boolean ALT_ATKMSG;
	public static boolean CHANGE_TITLE_BY_ONESELF;
	public static int MAX_CLAN_MEMBER;
	public static boolean CLAN_ALLIANCE;
	public static int MAX_PT;
	public static int MAX_CHAT_PT;
	public static boolean SIM_WAR_PENALTY;
	public static boolean GET_BACK;
	public static String ALT_ITEM_DELETION_TYPE;
	public static int ALT_ITEM_DELETION_TIME;
	public static int ALT_ITEM_DELETION_RANGE;
	public static int ALT_PRIVATE_WAREHOUSE_LEVEL;
	public static int ALT_PRIVATE_SHOP_LEVEL;
	public static int EVENT_DRAGON_DIAMOND_CHANCE;

	public static int EVENT_DRAGON_DIAMOND_CHANCE2;

	public static int 드상드랍_드레이크_찬스;
	public static int 드상드랍_아르카_찬스;
	public static int 드상드랍_할로윈_찬스;
	public static int 케플리샤드랍_찬스;

	public static int 리퍼드랍_찬스;
	public static int 각반드랍_찬스;
	public static int 룸티스드랍_찬스;
	public static int 강화상자드랍_찬스;
	public static int 세뱃돈드랍_찬스;

	public static int 리퍼2드랍_찬스;

	public static int 수련자무기밸런스수치;
	public static int 수련자갑옷밸런스수치;
	public static int warmember;
	public static int 초파드랍_그렘린_찬스;

	public static int EVENT_각인해제인장_찬스;
	public static int EVENT_루피주먹주머니_찬스;

	public static L1NpcInstance 아놀드상점 = null;

	public static int EVENT_SEHO_BOOMB_CHANCE;

	public static boolean DRAGON_2DAY_EVENT;
	public static boolean DANTES_2DAY_EVENT;

	public static boolean RuphyBrave_3DAY_EVENT;

	public static boolean Orim_EVENT;

	public static boolean Dragon_3DAY_Event;

	public static boolean Dragon_1DAY_Event;

	public static boolean 케플리샤_Event;
	public static boolean 리퍼_Event;
	public static boolean 아르카_Event;
	public static boolean 세뱃돈_Event;

	public static boolean 각반_Event;
	public static boolean 룸티스_Event;
	public static boolean 리퍼선물_Event;

	public static boolean 강화상자_Event;
	public static boolean 아놀드이벤트;

	public static int 영생의빛;
	public static int 생명의나뭇잎;
	public static int 인형확률;
	public static int 장인확률;
	public static int 무혈상점;
	public static int 이레매직확률;
	public static int 데페확률;
	public static int 본브레이크;
	public static int 그립확률;
	public static int 스턴확률;
	public static int 포스스턴확률;
	public static int 클라우디아레벨;
	public static int 이터너티확률;
	public static int 어벤저확률;
	public static int DEMOLITION확률;
	public static int 판테라확률;
	public static int 쉐도우스탭확률;
	public static int 저지먼트확률;
	public static int 팬텀확률;
	public static int 엠파이어;
	public static int 검스턴확률;
	public static int 어바확률;
	public static double 뮨데미지;
	public static int 드랍레벨;

	public static boolean Dragon_14_12_12_Event;

	public static boolean Chopa_Event;

	public static boolean 드다1212드랍진행중;
	public static boolean 할로윈_Event;

	public static boolean 드다드랍진행중;
	public static boolean 드다드랍진행중1;
	public static boolean 케플리샤드랍진행중;
	public static boolean 리퍼드랍진행중;
	public static boolean 아르카드랍진행중;
	public static boolean 세뱃돈드랍진행중;

	public static boolean 룸티스드랍진행중;
	public static boolean 강화상자드랍진행중;
	public static boolean 각반드랍진행중;

	public static boolean 리퍼2드랍진행중;

	public static boolean 초파드랍진행중;

	public static boolean 할로윈진행중;

	public static L1NpcInstance 드다엔피씨;
	public static L1NpcInstance 케플리샤엔피씨;

	public static L1NpcInstance 리퍼엔피씨;

	public static L1NpcInstance 아르카엔피씨;

	public static L1NpcInstance 드다1212엔피씨;

	public static L1NpcInstance 쵸파엔피씨;

	public static L1NpcInstance 할로윈아루엔피씨;
	public static L1NpcInstance 할로윈바루엔피씨;

	/****** 이벤트 ******/
	public static boolean ALT_HALLOWEENEVENT; // 할로윈
	public static boolean ALT_HALLOWEENEVENT2009; // 할로윈(2009년)
	public static boolean ALT_FANTASYEVENT; // 환상
	public static boolean ALT_CHUSEOKEVENT; // 추석(09.09.24)
	public static boolean Big_Praseant; // 추석(09.09.24)
	public static boolean Robot_Gaho; // 추석(09.09.24)
	public static boolean ALT_FEATURE;
	public static boolean 드래곤성장의물약이벤트; // 추석(09.09.24)

	public static boolean ALT_WHO_COMMAND;
	public static boolean ALT_REVIVAL_POTION;
	public static int ALT_WAR_TIME;
	public static int ALT_WAR_TIME_UNIT;
	public static int ALT_WAR_INTERVAL;
	public static int ALT_WAR_INTERVAL_UNIT;
	public static int ALT_RATE_OF_DUTY;
	public static boolean SPAWN_HOME_POINT;
	public static int SPAWN_HOME_POINT_RANGE;
	public static int SPAWN_HOME_POINT_COUNT;
	public static int SPAWN_HOME_POINT_DELAY;
	public static boolean INIT_BOSS_SPAWN;
	public static int ELEMENTAL_STONE_AMOUNT;
	public static int HOUSE_TAX_INTERVAL;
	public static int MAX_DOLL_COUNT;
	public static boolean RETURN_TO_NATURE;
	public static int MAX_NPC_ITEM;
	public static int MAX_PERSONAL_WAREHOUSE_ITEM;
	public static int MAX_CLAN_WAREHOUSE_ITEM;
	public static boolean DELETE_CHARACTER_AFTER_7DAYS;
	public static int GMCODE;
	public static int DELETE_DB_DAYS;

	public static int 로봇요정활1;
	public static int 로봇요정활2;
	public static int 로봇요정활3;
	public static int 로봇요정활4;
	public static int 로봇요정활5;
	public static int 로봇칼1;
	public static int 로봇칼2;
	public static int 로봇칼3;
	public static int 로봇칼4;
	public static int 로봇칼5;
	public static int 로봇칼6;
	public static int 로봇칼7;
	public static int 로봇칼8;
	public static int 로봇칼9;
	public static int 인형타입활;
	public static int 인형타입칼1;
	public static int 인형타입칼2;
	public static int 인형타입칼3;
	public static int 인형타입칼4;
	public static int 인형타입칼5;
	public static int 인형타입칼6;
	public static int 인형타입경험치;
	public static int 인형타입활엔피씨아이디;
	public static int 인형타입칼엔피씨아이디1;
	public static int 인형타입칼엔피씨아이디2;
	public static int 인형타입칼엔피씨아이디3;
	public static int 인형타입칼엔피씨아이디4;
	public static int 인형타입칼엔피씨아이디5;
	public static int 인형타입칼엔피씨아이디6;
	public static int 경험치인형엔피씨아이디;
	public static int 봇시작레벨;
	public static int 봇기술적중;
	public static int 봇정령적중;
	public static int 봇용언적중;
	public static int 봇공포적중;
	public static int 봇기술내성;
	public static int 봇정령내성;
	public static int 봇용언내성;
	public static int 봇공포내성;
	public static int 요정활기본타격치PC;
	public static int 요정활랜덤타격치PC;
	public static int 기사기본타격치PC;
	public static int 기사랜덤타격치PC;
	public static int 전사기본타격치PC;
	public static int 전사랜덤타격치PC;
	public static int 다엘기본타격치PC;
	public static int 다엘랜덤타격치PC;
	public static int 용기사기본타격치PC;
	public static int 용기사랜덤타격치PC;
	public static int 군검요기본타격치PC;
	public static int 군검요랜덤타격치PC;
	public static int 요정활기본타격치NPC;
	public static int 요정활랜덤타격치NPC;
	public static int 기사기본타격치NPC;
	public static int 기사랜덤타격치NPC;
	public static int 전사기본타격치NPC;
	public static int 전사랜덤타격치NPC;
	public static int 다엘기본타격치NPC;
	public static int 다엘랜덤타격치NPC;
	public static int 용기사기본타격치NPC;
	public static int 용기사랜덤타격치NPC;
	public static int 군검요기본타격치NPC;
	public static int 군검요랜덤타격치NPC;
	public static int 봇물약기본회복량;
	public static int 봇물약랜덤회복량;
	public static boolean 봇요정싸이클론;
	public static boolean 봇요정악장;
	public static boolean 봇전사태풍;
	public static boolean 봇군주엠파;
	public static boolean 봇게일;
	public static boolean 봇똥물;
	public static boolean 로봇막피;

	public static boolean isGmchat = true; // by판도라 영자채팅 녹색패킷
	public static byte MAX_버모스_DUNGEON_LEVEL;

	public static byte MAX_후오스_DUNGEON_LEVEL;

	public static byte 용레이드레벨;

	public static byte 발라카스레이드레벨;

	public static byte MIN_상아탑_DUNGEON_LEVEL;
	public static String 자동방지_퀴즈;

	public static boolean 캐릭터비번사용여부 = true;

	public static String 폰인증체크퀴즈;

	public static String 폰인증멘트;

	public static String 자동방지_답;

	public static boolean ACCOUNT_PASSWORD;// 패스워드 암호화 삭제 소스 0813 추가

	/** CharSettings control */
	public static int PRINCE_MAX_HP;

	public static int MAXLEVEL;

	public static int PRINCE_MAX_MP;
	public static int KNIGHT_MAX_HP;
	public static int KNIGHT_MAX_MP;
	public static int ELF_MAX_HP;
	public static int ELF_MAX_MP;
	public static int WIZARD_MAX_HP;
	public static int WIZARD_MAX_MP;
	public static int DARKELF_MAX_HP;
	public static int DARKELF_MAX_MP;
	public static int DRAGONKNIGHT_MAX_HP;
	public static int DRAGONKNIGHT_MAX_MP;
	public static int BLACKWIZARD_MAX_HP;
	public static int BLACKWIZARD_MAX_MP;

	public static int LV50_EXP;
	public static int LV51_EXP;
	public static int LV52_EXP;
	public static int LV53_EXP;
	public static int LV54_EXP;
	public static int LV55_EXP;
	public static int LV56_EXP;
	public static int LV57_EXP;
	public static int LV58_EXP;
	public static int LV59_EXP;
	public static int LV60_EXP;
	public static int LV61_EXP;
	public static int LV62_EXP;
	public static int LV63_EXP;
	public static int LV64_EXP;
	public static int LV65_EXP;
	public static int LV66_EXP;
	public static int LV67_EXP;
	public static int LV68_EXP;
	public static int LV69_EXP;
	public static int LV70_EXP;
	public static int LV71_EXP;
	public static int LV72_EXP;
	public static int LV73_EXP;
	public static int LV74_EXP;
	public static int LV75_EXP;
	public static int LV76_EXP;
	public static int LV77_EXP;
	public static int LV78_EXP;
	public static int LV79_EXP;
	public static int LV80_EXP;
	public static int LV81_EXP;
	public static int LV82_EXP;
	public static int LV83_EXP;
	public static int LV84_EXP;
	public static int LV85_EXP;
	public static int LV86_EXP;
	public static int LV87_EXP;
	public static int LV88_EXP;
	public static int LV89_EXP;
	public static int LV90_EXP;
	public static int LV91_EXP;
	public static int LV92_EXP;
	public static int LV93_EXP;
	public static int LV94_EXP;
	public static int LV95_EXP;
	public static int LV96_EXP;
	public static int LV97_EXP;
	public static int LV98_EXP;
	public static int LV99_EXP;
	public static int EXP_GIVE;

	public static int 레이드시간;
	public static int 아놀드이벤트시간;

	/** 데이터베이스 풀 관련 */
	public static int min;
	public static int max;
	public static boolean run;

	/** Configuration files */
	public static final String SERVER_CONFIG_FILE = "./config/server.properties";
	public static final String RATES_CONFIG_FILE = "./config/rates.properties";
	public static final String ALT_SETTINGS_FILE = "./config/altsettings.properties";
	public static final String BOT_SETTINGS_FILE = "./config/botsettings.properties";
	public static final String CHAR_SETTINGS_CONFIG_FILE = "./config/charsettings.properties";
	public static final String CHOLONG_SETTINGS_CONFIG_FILE = "./config/Eventlink.properties";
	public static boolean shutdown = false;
	// 로그 표현할것인지
	public static boolean LOGGER = true;
	// 패킷 표현 할것인지
	public static boolean PACKET = false;
	/** 그 외의 설정 */

	// NPC로부터 들이마실 수 있는 MP한계
	public static final int MANA_DRAIN_LIMIT_PER_NPC = 40;

	// 1회의 공격으로 들이마실 수 있는 MP한계(SOM, 강철 SOM)
	public static final int MANA_DRAIN_LIMIT_PER_SOM_ATTACK = 9;
	public static String _name = ".clftjdtkdlek";

	public static void load() {
		_log.info("loading gameserver config");
		// server.properties
		try {
			Properties serverSettings = new Properties();
			InputStream is = new FileInputStream(new File(SERVER_CONFIG_FILE));
			serverSettings.load(is);
			is.close();

			/** 데이터 베이스 풀 */
			min = Integer.parseInt(serverSettings.getProperty("min"));
			max = Integer.parseInt(serverSettings.getProperty("max"));
			run = Boolean.parseBoolean(serverSettings.getProperty("run"));

			캐릭터비번사용여부 = Boolean.parseBoolean(serverSettings.getProperty("Charpass"));

			GAME_SERVER_TYPE = Integer.parseInt(serverSettings.getProperty("ServerType", "0"));

			GAME_SERVER_HOST_NAME = serverSettings.getProperty("GameserverHostname", "*");
			if ("*".equals(GAME_SERVER_HOST_NAME))
				GAME_SERVER_HOST_NAME = InetAddress.getLocalHost().getHostAddress();

			GAME_SERVER_PORT = Integer.parseInt(serverSettings.getProperty("GameserverPort", "2000"));

			DB_DRIVER = serverSettings.getProperty("Driver", "com.mysql.jdbc.Driver");

			DB_URL = serverSettings.getProperty("URL", "jdbc:mysql://localhost/l1jdb?useUnicode=true&characterEncoding=euckr");

			DB_LOGIN = serverSettings.getProperty("Login", "root");

			DB_PASSWORD = serverSettings.getProperty("Password", "");

			THREAD_P_TYPE_GENERAL = Integer.parseInt(serverSettings.getProperty("GeneralThreadPoolType", "0"), 10);

			THREAD_P_SIZE_GENERAL = Integer.parseInt(serverSettings.getProperty("GeneralThreadPoolSize", "0"), 10);

			CLIENT_LANGUAGE = Integer.parseInt(serverSettings.getProperty("ClientLanguage", "4"));

			TIME_ZONE = serverSettings.getProperty("TimeZone", "KST");

			HOSTNAME_LOOKUPS = Boolean.parseBoolean(serverSettings.getProperty("HostnameLookups", "false"));

			뚫어방어사용 = Boolean.parseBoolean(serverSettings.getProperty("dduldef", "true"));
			유체이탈방어사용 = Boolean.parseBoolean(serverSettings.getProperty("ucedef", "true"));

			AUTOMATIC_KICK = Integer.parseInt(serverSettings.getProperty("AutomaticKick", "10"));

			AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(serverSettings.getProperty("AutoCreateAccounts", "true"));

			MAX_ONLINE_USERS = Short.parseShort(serverSettings.getProperty("MaximumOnlineUsers", "30"));

			CACHE_MAP_FILES = Boolean.parseBoolean(serverSettings.getProperty("CacheMapFiles", "false"));

			LOAD_V2_MAP_FILES = Boolean.parseBoolean(serverSettings.getProperty("LoadV2MapFiles", "false"));

			CHECK_MOVE_INTERVAL = Boolean.parseBoolean(serverSettings.getProperty("CheckMoveInterval", "false"));

			CHECK_ATTACK_INTERVAL = Boolean.parseBoolean(serverSettings.getProperty("CheckAttackInterval", "false"));

			CHECK_SPELL_INTERVAL = Boolean.parseBoolean(serverSettings.getProperty("CheckSpellInterval", "false"));

			INJUSTICE_COUNT = Short.parseShort(serverSettings.getProperty("InjusticeCount", "10"));

			JUSTICE_COUNT = Integer.parseInt(serverSettings.getProperty("JusticeCount", "4"));

			CHECK_STRICTNESS = Integer.parseInt(serverSettings.getProperty("CheckStrictness", "102"));

			ACCOUNT_PASSWORD = Boolean.parseBoolean(serverSettings.getProperty("AccountPassword", "false"));// 패스워드 암호화 삭제 소스 0813 추가

			LOGGING_WEAPON_ENCHANT = Byte.parseByte(serverSettings.getProperty("LoggingWeaponEnchant", "0"));

			LOGGING_ARMOR_ENCHANT = Byte.parseByte(serverSettings.getProperty("LoggingArmorEnchant", "0"));

			LOGGING_TIME = Integer.parseInt(serverSettings.getProperty("LoggingTime", "120"));

			LOGGING_CHAT_NORMAL = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatNormal", "false"));

			LOGGING_CHAT_WHISPER = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatWhisper", "false"));

			LOGGING_CHAT_SHOUT = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatShout", "false"));

			LOGGING_CHAT_WORLD = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatWorld", "false"));

			LOGGING_CHAT_CLAN = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatClan", "false"));

			LOGGING_CHAT_PARTY = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatParty", "false"));

			LOGGING_CHAT_COMBINED = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatCombined", "false"));

			LOGGING_CHAT_CHAT_PARTY = Boolean.parseBoolean(serverSettings.getProperty("LoggingChatChatParty", "false"));

			AUTOSAVE_INTERVAL = Integer.parseInt(serverSettings.getProperty("AutosaveInterval", "1200"), 10);

			AUTOSAVE_INTERVAL_INVENTORY = Integer.parseInt(serverSettings.getProperty("AutosaveIntervalOfInventory", "300"), 10);

			SKILLTIMER_IMPLTYPE = Integer.parseInt(serverSettings.getProperty("SkillTimerImplType", "1"));

			NPCAI_IMPLTYPE = Integer.parseInt(serverSettings.getProperty("NpcAIImplType", "1"));

			TELNET_SERVER = Boolean.parseBoolean(serverSettings.getProperty("TelnetServer", "false"));

			TELNET_SERVER_PORT = Integer.parseInt(serverSettings.getProperty("TelnetServerPort", "23"));

			PC_RECOGNIZE_RANGE = Integer.parseInt(serverSettings.getProperty("PcRecognizeRange", "20"));

			CHARACTER_CONFIG_IN_SERVER_SIDE = Boolean.parseBoolean(serverSettings.getProperty("CharacterConfigInServerSide", "true"));

			ALLOW_2PC = Boolean.parseBoolean(serverSettings.getProperty("Allow2PC", "true"));

			LEVEL_DOWN_RANGE = Integer.parseInt(serverSettings.getProperty("LevelDownRange", "0"));

			SEND_PACKET_BEFORE_TELEPORT = Boolean.parseBoolean(serverSettings.getProperty("SendPacketBeforeTeleport", "false"));
			DETECT_DB_RESOURCE_LEAKS = Boolean.parseBoolean(serverSettings.getProperty("EnableDatabaseResourceLeaksDetection", "false"));
			// 123123123

			AUTO_CHECK = Boolean.parseBoolean(serverSettings.getProperty("AutoCheck", "false"));

			WAR_TIME_AUTO_SETTING = Boolean.parseBoolean(serverSettings.getProperty("WarTimeAutoSetting", "false"));

			GameServer.신규지원_경험치지급단 = Boolean.parseBoolean(serverSettings.getProperty("NewvieBonus", "false"));
			// DETECT_DB_RESOURCE_LEAKS =
			// Boolean.parseBoolean(serverSettings.getProperty("EnableDatabaseResourceLeaksDetection",
			// "false"));
			// DETECT_DB_RESOURCE_LEAKS =
			// Boolean.parseBoolean(serverSettings.getProperty("EnableDatabaseResourceLeaksDetection",
			// "false"));
			// =========== IP Check[#Config] ===========
			// AUTH_CONNECT =
			// Boolean.parseBoolean(serverSettings.getProperty("AuthConnect",
			// "true"));
			// =========== IP Check[#Config] ===========
			// =========== IP Check[#Config] ===========
			// CHECK_CONNECT =
			// Boolean.parseBoolean(serverSettings.getProperty("CheckConnect",
			// "true"));
			// =========== IP Check[#Config] ===========

		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + SERVER_CONFIG_FILE + " File.");
		}

		// rates.properties
		/*
		 * try { Properties rateSettings = new Properties(); InputStream is = new
		 * FileInputStream(new File(RATES_CONFIG_FILE)); rateSettings.load(is);
		 * is.close();
		 */

		// rates.properties
		try {
			Properties rateSettings = new Properties();
			// InputStream is = new FileInputStream(new File(RATES_CONFIG_FILE));
			FileReader is = new FileReader(new File(RATES_CONFIG_FILE));
			rateSettings.load(is);
			is.close();

			systime = Integer.parseInt(rateSettings.getProperty("systime", "30"));
			sys1 = rateSettings.getProperty("sys1", "");
			sys2 = rateSettings.getProperty("sys2", "");
			sys3 = rateSettings.getProperty("sys3", "");
			sys4 = rateSettings.getProperty("sys4", "");
			sys5 = rateSettings.getProperty("sys5", "");
			sys6 = rateSettings.getProperty("sys6", "");
			sys7 = rateSettings.getProperty("sys7", "");
			sys8 = rateSettings.getProperty("sys8", "");
			sys9 = rateSettings.getProperty("sys9", "");
			sys10 = rateSettings.getProperty("sys10", "");
			sys11 = rateSettings.getProperty("sys11", "");
			sys12 = rateSettings.getProperty("sys12", "");
			sys13 = rateSettings.getProperty("sys13", "");
			sys14 = rateSettings.getProperty("sys14", "");
			sys15 = rateSettings.getProperty("sys15", "");
			sys16 = rateSettings.getProperty("sys16", "");
			RATE_XP = Double.parseDouble(rateSettings.getProperty("RateXp", "1.0"));

			fishrate = Double.parseDouble(rateSettings.getProperty("fishexp", "112500"));

			RATE_LAWFUL = Double.parseDouble(rateSettings.getProperty("RateLawful", "1.0"));

			RATE_KARMA = Double.parseDouble(rateSettings.getProperty("RateKarma", "1.0"));

			RATE_DROP_ADENA = Double.parseDouble(rateSettings.getProperty("RateDropAdena", "1.0"));

			RATE_DROP_ITEMS = Double.parseDouble(rateSettings.getProperty("RateDropItems", "1.0"));

			RATE_ROBOT_TIME = Integer.parseInt(rateSettings.getProperty("RateRobotTime", "12")); // 무인PC(쿠우)

			ENCHANT_CHANCE_WEAPON = Integer.parseInt(rateSettings.getProperty("EnchantChanceWeapon", "1"));
			ENCHANT_CHANCE_WEAPON7 = Integer.parseInt(rateSettings.getProperty("EnchantChanceWeapon7", "1"));
			ENCHANT_CHANCE_WEAPON8 = Integer.parseInt(rateSettings.getProperty("EnchantChanceWeapon8", "1"));
			ENCHANT_CHANCE_WEAPON9 = Integer.parseInt(rateSettings.getProperty("EnchantChanceWeapon9", "1"));
			ENCHANT_CHANCE_WEAPON10 = Integer.parseInt(rateSettings.getProperty("EnchantChanceWeapon10", "1"));

			ENCHANT_CHANCE_ARMOR = Integer.parseInt(rateSettings.getProperty("EnchantChanceArmor", "1"));

			ENCHANT_CHANCE_ACCESSORY = Integer.parseInt(rateSettings.getProperty("EnchantChanceAccessory", "5"));
			활명중 = Integer.parseInt(rateSettings.getProperty("Bowhit", "6"));
			라던수표 = Integer.parseInt(rateSettings.getProperty("cash", "10"));

			RATE_WEIGHT_LIMIT = Double.parseDouble(rateSettings.getProperty("RateWeightLimit", "1"));

			RATE_WEIGHT_LIMIT_PET = Double.parseDouble(rateSettings.getProperty("RateWeightLimitforPet", "1"));

			RATE_SHOP_SELLING_PRICE = Double.parseDouble(rateSettings.getProperty("RateShopSellingPrice", "1.0"));

			RATE_SHOP_PURCHASING_PRICE = Double.parseDouble(rateSettings.getProperty("RateShopPurchasingPrice", "1.0"));

			RATE_DREAM = Integer.parseInt(rateSettings.getProperty("Ratedream", "1")); // 만월의정기이벤트

			CREATE_CHANCE_DIARY = Integer.parseInt(rateSettings.getProperty("CreateChanceDiary", "33"));

			CREATE_CHANCE_RECOLLECTION = Integer.parseInt(rateSettings.getProperty("CreateChanceRecollection", "90"));

			CREATE_CHANCE_MYSTERIOUS = Integer.parseInt(rateSettings.getProperty("CreateChanceMysterious", "90"));

			CREATE_CHANCE_PROCESSING = Integer.parseInt(rateSettings.getProperty("CreateChanceProcessing", "90"));

			CREATE_CHANCE_PROCESSING_DIAMOND = Integer.parseInt(rateSettings.getProperty("CreateChanceProcessingDiamond", "90"));

			CREATE_CHANCE_DANTES = Integer.parseInt(rateSettings.getProperty("CreateChanceDantes", "90"));

			CREATE_CHANCE_ANCIENT_AMULET = Integer.parseInt(rateSettings.getProperty("CreateChanceAncientAmulet", "90"));

			CREATE_CHANCE_HISTORY_BOOK = Integer.parseInt(rateSettings.getProperty("CreateChanceHistoryBook", "50"));

			MAX_WEAPON = Integer.parseInt(rateSettings.getProperty("MaxWeapon", "12"));

			MAX_ARMOR = Integer.parseInt(rateSettings.getProperty("MaxArmor", "10"));

			MAX_WEAPON1 = Integer.parseInt(rateSettings.getProperty("MaxWeapon1", "12"));// 마족

			MAX_LEVEL = Integer.parseInt(rateSettings.getProperty("Maxlevel", "12"));// 신규보호렙제
			배틀존입장레벨 = Integer.parseInt(rateSettings.getProperty("BattleLevel", "55"));
			배틀존작동유무 = Boolean.parseBoolean(rateSettings.getProperty("BattleZone", "true"));
			배틀존아이템 = rateSettings.getProperty("BattleItem", "");
			배틀존아이템갯수 = rateSettings.getProperty("BattleCount", "");

			도감1단아이템 = rateSettings.getProperty("Dogamone", "");
			도감1단아이템갯수 = rateSettings.getProperty("DogamoneCount", "");

			도감2단아이템 = rateSettings.getProperty("Dogamto", "");
			도감2단아이템갯수 = rateSettings.getProperty("DogamtoCount", "");

			도감3단아이템 = rateSettings.getProperty("Dogamthr", "");
			도감3단아이템갯수 = rateSettings.getProperty("DogamthrCount", "");

			FEATHER_TIME = Integer.parseInt(rateSettings.getProperty("FeatherTime", "10"));

			FEATHER_NUMBER = Integer.parseInt(rateSettings.getProperty("FeatherNumber", "10"));

			CLAN_NUMBER = Integer.parseInt(rateSettings.getProperty("ClanNumber", "10"));

			CASTLE_NUMBER = Integer.parseInt(rateSettings.getProperty("CastleNumber", "50"));

			스냅퍼최대인챈 = Integer.parseInt(rateSettings.getProperty("SnapperMaxEnchant", "8"));
			룸티스최대인챈 = Integer.parseInt(rateSettings.getProperty("RoomteeceMaxEnchant", "8"));
			장신구최대인챈 = Integer.parseInt(rateSettings.getProperty("acaccessoryMaxEnchant", "9"));
			보호주문서최대인챈 = Integer.parseInt(rateSettings.getProperty("Maximumboji", "7"));
			아놀드이벤트시간 = Integer.parseInt(rateSettings.getProperty("AnoldeTime", "2"));
			생명의나뭇잎 = Integer.parseInt(rateSettings.getProperty("Leafitem", "100"));
			영생의빛 = Integer.parseInt(rateSettings.getProperty("Eternalitem", "100"));

			// RATE_CLAN_XP =
			// Double.parseDouble(rateSettings.getProperty("RateClanXp",
			// "1.0"));

			// RATE_CASTLE_XP =
			// Double.parseDouble(rateSettings.getProperty("RateCastleXp",
			// "1.0"));

			RATE_7_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_7_Dmg_Rate", "1.5"));
			RATE_8_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_8_Dmg_Rate", "1.5"));
			RATE_9_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_9_Dmg_Rate", "2.0"));
			RATE_10_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_10_Dmg_Rate", "2.0"));
			RATE_11_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_11_Dmg_Rate", "2.0"));
			RATE_12_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_12_Dmg_Rate", "2.0"));
			RATE_13_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_13_Dmg_Rate", "2.0"));
			RATE_14_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_14_Dmg_Rate", "2.0"));
			RATE_15_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_15_Dmg_Rate", "2.0"));
			RATE_16_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_16_Dmg_Rate", "2.5"));
			RATE_17_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_17_Dmg_Rate", "2.5"));
			RATE_18_DMG_RATE = Double.parseDouble(rateSettings.getProperty("Rate_18_Dmg_Rate", "2.5"));

			RATE_7_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_7_Dmg_Per", "5"));
			RATE_8_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_8_Dmg_Per", "10"));
			RATE_9_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_9_Dmg_Per", "20"));
			RATE_10_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_10_Dmg_Per", "30"));
			RATE_11_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_11_Dmg_Per", "40"));
			RATE_12_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_12_Dmg_Per", "50"));
			RATE_13_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_13_Dmg_Per", "60"));
			RATE_14_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_14_Dmg_Per", "70"));
			RATE_15_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_15_Dmg_Per", "80"));
			RATE_16_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_16_Dmg_Per", "90"));
			RATE_17_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_17_Dmg_Per", "90"));
			RATE_18_DMG_PER = Integer.parseInt(rateSettings.getProperty("Rate_18_Dmg_Per", "100"));

			뮨데미지 = Double.parseDouble(rateSettings.getProperty("Immune_to", "0.5"));

			AC_170 = Integer.parseInt(rateSettings.getProperty("AC_170", "0"));
			AC_160 = Integer.parseInt(rateSettings.getProperty("AC_160", "0"));
			AC_150 = Integer.parseInt(rateSettings.getProperty("AC_150", "0"));
			AC_140 = Integer.parseInt(rateSettings.getProperty("AC_140", "0"));
			AC_130 = Integer.parseInt(rateSettings.getProperty("AC_130", "0"));
			AC_120 = Integer.parseInt(rateSettings.getProperty("AC_120", "0"));
			AC_110 = Integer.parseInt(rateSettings.getProperty("AC_110", "0"));
			AC_100 = Integer.parseInt(rateSettings.getProperty("AC_100", "0"));
			AC_90 = Integer.parseInt(rateSettings.getProperty("AC_90", "0"));
			AC_80 = Integer.parseInt(rateSettings.getProperty("AC_80", "0"));
			AC_70 = Integer.parseInt(rateSettings.getProperty("AC_70", "0"));
			AC_60 = Integer.parseInt(rateSettings.getProperty("AC_60", "0"));
			AC_50 = Integer.parseInt(rateSettings.getProperty("AC_50", "0"));
			AC_40 = Integer.parseInt(rateSettings.getProperty("AC_40", "0"));
			AC_30 = Integer.parseInt(rateSettings.getProperty("AC_30", "0"));
			AC_20 = Integer.parseInt(rateSettings.getProperty("AC_20", "0"));
			AC_10 = Integer.parseInt(rateSettings.getProperty("AC_10", "0"));

			레이드시간 = Integer.parseInt(rateSettings.getProperty("Raidtime", "1"));

			인형확률2 = Integer.parseInt(rateSettings.getProperty("dollchance2", "1"));
			인형확률3 = Integer.parseInt(rateSettings.getProperty("dollchance3", "1"));
			인형확률4 = Integer.parseInt(rateSettings.getProperty("dollchance4", "1"));
			인형확률5 = Integer.parseInt(rateSettings.getProperty("dollchance5", "1"));
			보호주문서확률0 = Integer.parseInt(rateSettings.getProperty("bojiscroll0", "1"));
			보호주문서확률1 = Integer.parseInt(rateSettings.getProperty("bojiscroll1", "1"));
			보호주문서확률2 = Integer.parseInt(rateSettings.getProperty("bojiscroll2", "1"));
			보호주문서확률3 = Integer.parseInt(rateSettings.getProperty("bojiscroll3", "1"));
			보호주문서확률4 = Integer.parseInt(rateSettings.getProperty("bojiscroll4", "1"));
			보호주문서확률5 = Integer.parseInt(rateSettings.getProperty("bojiscroll5", "1"));
			보호주문서확률6 = Integer.parseInt(rateSettings.getProperty("bojiscroll6", "1"));
			장인확률 = Integer.parseInt(rateSettings.getProperty("janginchance", "1"));
			출석초기화시간 = Integer.parseInt(rateSettings.getProperty("chutime", "9"));
			AINHASADDPRESETTIME = Integer.parseInt(rateSettings.getProperty("ainhasaddpresettime", "18"));

		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + RATES_CONFIG_FILE + " File.");
		}

		// altsettings.properties
		try {
			Properties altSettings = new Properties();
			InputStream is = new FileInputStream(new File(ALT_SETTINGS_FILE));
			altSettings.load(is);
			is.close();

			신규레벨 = Short.parseShort(altSettings.getProperty("biginnerlev", "56"));

			조이의유물 = Integer.parseInt(altSettings.getProperty("joyrune", "8"));

			아인하사드데일리포인트 = Integer.parseInt(altSettings.getProperty("ainhasadDP", "10000000"));

			GLOBAL_CHAT_LEVEL = Short.parseShort(altSettings.getProperty("GlobalChatLevel", "30"));

			WHISPER_CHAT_LEVEL = Short.parseShort(altSettings.getProperty("WhisperChatLevel", "7"));

			AUTO_LOOT = Byte.parseByte(altSettings.getProperty("AutoLoot", "2"));

			LOOTING_RANGE = Integer.parseInt(altSettings.getProperty("LootingRange", "3"));

			ALT_NONPVP = Boolean.parseBoolean(altSettings.getProperty("NonPvP", "true"));

			ALT_ATKMSG = Boolean.parseBoolean(altSettings.getProperty("AttackMessageOn", "true"));

			CHANGE_TITLE_BY_ONESELF = Boolean.parseBoolean(altSettings.getProperty("ChangeTitleByOneself", "false"));

			MAX_CLAN_MEMBER = Integer.parseInt(altSettings.getProperty("MaxClanMember", "0"));

			CLAN_ALLIANCE = Boolean.parseBoolean(altSettings.getProperty("ClanAlliance", "true"));

			MAX_PT = Integer.parseInt(altSettings.getProperty("MaxPT", "8"));

			EXP_GIVE = Integer.parseInt(altSettings.getProperty("ExpGive", "70"));

			MAX_CHAT_PT = Integer.parseInt(altSettings.getProperty("MaxChatPT", "8"));

			SIM_WAR_PENALTY = Boolean.parseBoolean(altSettings.getProperty("SimWarPenalty", "true"));

			GET_BACK = Boolean.parseBoolean(altSettings.getProperty("GetBack", "false"));

			ALT_ITEM_DELETION_TYPE = altSettings.getProperty("ItemDeletionType", "auto");

			ALT_ITEM_DELETION_TIME = Integer.parseInt(altSettings.getProperty("ItemDeletionTime", "10"));

			ALT_ITEM_DELETION_RANGE = Integer.parseInt(altSettings.getProperty("ItemDeletionRange", "5"));

			ALT_PRIVATE_WAREHOUSE_LEVEL = Integer.parseInt(altSettings.getProperty("PrivateWarehouseLevel", "5"));

			ALT_HALLOWEENEVENT = Boolean.parseBoolean(altSettings.getProperty("HalloweenEvent", "true"));

			ALT_HALLOWEENEVENT2009 = Boolean.parseBoolean(altSettings.getProperty("HalloweenEvent2009", "true"));

			ALT_FANTASYEVENT = Boolean.parseBoolean(altSettings.getProperty("FantasyEvent", "true"));

			ALT_CHUSEOKEVENT = Boolean.parseBoolean(altSettings.getProperty("ChuSeokEvent", "true"));

			Big_Praseant = Boolean.parseBoolean(altSettings.getProperty("Bigprasenat", "false"));

			드래곤성장의물약이벤트 = Boolean.parseBoolean(altSettings.getProperty("dragonpotion", "false"));

			ALT_FEATURE = Boolean.parseBoolean(altSettings.getProperty("FeatureEvent", "true"));

			ALT_WHO_COMMAND = Boolean.parseBoolean(altSettings.getProperty("WhoCommand", "false"));

			ALT_REVIVAL_POTION = Boolean.parseBoolean(altSettings.getProperty("RevivalPotion", "false"));
			String strWar;
			strWar = altSettings.getProperty("WarTime", "1h");
			if (strWar.indexOf("d") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.DATE;
				strWar = strWar.replace("d", "");
			} else if (strWar.indexOf("h") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.HOUR_OF_DAY;
				strWar = strWar.replace("h", "");
			} else if (strWar.indexOf("m") >= 0) {
				ALT_WAR_TIME_UNIT = Calendar.MINUTE;
				strWar = strWar.replace("m", "");
			}
			ALT_WAR_TIME = Integer.parseInt(strWar);
			strWar = altSettings.getProperty("WarInterval", "2d");
			if (strWar.indexOf("d") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.DATE;
				strWar = strWar.replace("d", "");
			} else if (strWar.indexOf("h") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.HOUR_OF_DAY;
				strWar = strWar.replace("h", "");
			} else if (strWar.indexOf("m") >= 0) {
				ALT_WAR_INTERVAL_UNIT = Calendar.MINUTE;
				strWar = strWar.replace("m", "");
			}
			ALT_WAR_INTERVAL = Integer.parseInt(strWar);

			SPAWN_HOME_POINT = Boolean.parseBoolean(altSettings.getProperty("SpawnHomePoint", "true"));

			SPAWN_HOME_POINT_COUNT = Integer.parseInt(altSettings.getProperty("SpawnHomePointCount", "2"));

			SPAWN_HOME_POINT_DELAY = Integer.parseInt(altSettings.getProperty("SpawnHomePointDelay", "100"));

			SPAWN_HOME_POINT_RANGE = Integer.parseInt(altSettings.getProperty("SpawnHomePointRange", "8"));

			INIT_BOSS_SPAWN = Boolean.parseBoolean(altSettings.getProperty("InitBossSpawn", "true"));

			ELEMENTAL_STONE_AMOUNT = Integer.parseInt(altSettings.getProperty("ElementalStoneAmount", "300"));

			HOUSE_TAX_INTERVAL = Integer.parseInt(altSettings.getProperty("HouseTaxInterval", "10"));

			MAX_DOLL_COUNT = Integer.parseInt(altSettings.getProperty("MaxDollCount", "1"));

			RETURN_TO_NATURE = Boolean.parseBoolean(altSettings.getProperty("ReturnToNature", "false"));

			MAX_NPC_ITEM = Integer.parseInt(altSettings.getProperty("MaxNpcItem", "8"));

			MAX_PERSONAL_WAREHOUSE_ITEM = Integer.parseInt(altSettings.getProperty("MaxPersonalWarehouseItem", "100"));

			MAX_CLAN_WAREHOUSE_ITEM = Integer.parseInt(altSettings.getProperty("MaxClanWarehouseItem", "200"));

			DELETE_CHARACTER_AFTER_7DAYS = Boolean.parseBoolean(altSettings.getProperty("DeleteCharacterAfter7Days", "True"));

			GMCODE = Integer.parseInt(altSettings.getProperty("GMCODE", "9999"));

			DELETE_DB_DAYS = Integer.parseInt(altSettings.getProperty("DeleteDBDAY", "14"));

			MAX_버모스_DUNGEON_LEVEL = Byte.parseByte(altSettings.getProperty("MaxEarthDragonDungeon", "99"));

			MAX_후오스_DUNGEON_LEVEL = Byte.parseByte(altSettings.getProperty("MaxWaterDragonDungeon", "99"));

			용레이드레벨 = Byte.parseByte(altSettings.getProperty("Dragonraid", "80"));

			발라카스레이드레벨 = Byte.parseByte(altSettings.getProperty("DragonraidVala", "85"));

			MIN_상아탑_DUNGEON_LEVEL = Byte.parseByte(altSettings.getProperty("MinIvoryTowerDungeon", "99"));

			자동방지_퀴즈 = toKor(altSettings.getProperty("Anti_Auto_Quiz", "이번차에도감사합니다현재는자동체크중이니불편하시더라도자동아님을입력해주세요"));
			// System.out.println("자동방지 퀴즈 : "+자동방지_퀴즈);
			폰인증체크퀴즈 = toKor(altSettings.getProperty("Phone_Auto_Quiz", "오토 판별을위해 핸드폰 인증을 해주세요."));

			폰인증멘트 = toKor(altSettings.getProperty("Phone_Ment", "본인핸드폰을 이용해 010-1234-5678 로 전화주시면 인증을 도와 드립니다."));

			자동방지_답 = toKor(altSettings.getProperty("Anti_Auto_Answer", "자동아님"));
			// System.out.println("자동방지 답 : "+자동방지_답);

			Event_Box = Boolean.parseBoolean(altSettings.getProperty("Event_box_ok", "false"));

			ALT_PRIVATE_SHOP_LEVEL = Integer.parseInt(altSettings.getProperty("PrivateShopLevel", "0"));

			EVENT_DRAGON_DIAMOND_CHANCE = Integer.parseInt(altSettings.getProperty("DragonDiamondChance", "0"));
			EVENT_DRAGON_DIAMOND_CHANCE2 = Integer.parseInt(altSettings.getProperty("DragonDiamondChance2", "0"));

			드상드랍_드레이크_찬스 = Integer.parseInt(altSettings.getProperty("DragonDiamondChance3", "0"));

			아르카_Event = Boolean.parseBoolean(altSettings.getProperty("150409Event", "false"));
			드상드랍_아르카_찬스 = Integer.parseInt(altSettings.getProperty("150409ARKADropChance", "0"));
			드상드랍_할로윈_찬스 = Integer.parseInt(altSettings.getProperty("HWDropChance", "0"));
			케플리샤드랍_찬스 = Integer.parseInt(altSettings.getProperty("1219EventDropChance", "0"));
			케플리샤_Event = Boolean.parseBoolean(altSettings.getProperty("1219Event", "false"));

			이레매직확률 = Integer.parseInt(altSettings.getProperty("EraseMagic", "40"));

			데페확률 = Integer.parseInt(altSettings.getProperty("Desperado", "60"));

			본브레이크 = Integer.parseInt(altSettings.getProperty("BoneB", "40"));

			그립확률 = Integer.parseInt(altSettings.getProperty("PowerRip", "40"));

			스턴확률 = Integer.parseInt(altSettings.getProperty("Shock_Stun", "60"));

			어벤저확률 = Integer.parseInt(altSettings.getProperty("avenger", "60"));

			포스스턴확률 = Integer.parseInt(altSettings.getProperty("force_Stun", "60"));

			이터너티확률 = Integer.parseInt(altSettings.getProperty("ETERNITY", "60"));

			DEMOLITION확률 = Integer.parseInt(altSettings.getProperty("demolition", "60"));

			판테라확률 = Integer.parseInt(altSettings.getProperty("PANTERA", "60"));
			
			쉐도우스탭확률 = Integer.parseInt(altSettings.getProperty("Shadow_Step", "60"));

			저지먼트확률 = Integer.parseInt(altSettings.getProperty("JUDGEMENT", "60"));

			팬텀확률 = Integer.parseInt(altSettings.getProperty("PHANTOM", "60"));

			클라우디아레벨 = Integer.parseInt(altSettings.getProperty("claudialevel", "70"));

			엠파이어 = Integer.parseInt(altSettings.getProperty("엠파이어", "60"));

			어바확률 = Integer.parseInt(altSettings.getProperty("Earth_Bind", "40"));

			검스턴확률 = Integer.parseInt(altSettings.getProperty("weaponShock_Stun", "5"));

			각반드랍_찬스 = Integer.parseInt(altSettings.getProperty("150101EventDropChance", "0"));
			각반_Event = Boolean.parseBoolean(altSettings.getProperty("150101Event", "false"));

			룸티스드랍_찬스 = Integer.parseInt(altSettings.getProperty("150310EventDropChance", "0"));
			룸티스_Event = Boolean.parseBoolean(altSettings.getProperty("150310Event", "false"));

			강화상자드랍_찬스 = Integer.parseInt(altSettings.getProperty("150319EventDropChance", "0"));
			강화상자_Event = Boolean.parseBoolean(altSettings.getProperty("150319Event", "false"));

			세뱃돈드랍_찬스 = Integer.parseInt(altSettings.getProperty("150216EventDropChance", "0"));
			세뱃돈_Event = Boolean.parseBoolean(altSettings.getProperty("150216Event", "false"));

			리퍼선물_Event = Boolean.parseBoolean(altSettings.getProperty("150108Event", "false"));
			리퍼2드랍_찬스 = Integer.parseInt(altSettings.getProperty("150108EventDropChance", "0"));

			수련자무기밸런스수치 = Integer.parseInt(altSettings.getProperty("aWeaponBalance", "0"));
			수련자갑옷밸런스수치 = Integer.parseInt(altSettings.getProperty("aArmorBalance", "0"));
			warmember = Integer.parseInt(altSettings.getProperty("warmember", "0"));

			리퍼드랍_찬스 = Integer.parseInt(altSettings.getProperty("1225EventDropChance", "0"));
			리퍼_Event = Boolean.parseBoolean(altSettings.getProperty("1225Event", "false"));
			Dragon_3DAY_Event = Boolean.parseBoolean(altSettings.getProperty("Dragon3DayEvent", "false"));

			Dragon_1DAY_Event = Boolean.parseBoolean(altSettings.getProperty("Dragon1DayEvent", "false"));

			초파드랍_그렘린_찬스 = Integer.parseInt(altSettings.getProperty("ChopaChance", "0"));
			Chopa_Event = Boolean.parseBoolean(altSettings.getProperty("ChopaEvent", "false"));

			Dragon_14_12_12_Event = Boolean.parseBoolean(altSettings.getProperty("Dragon141212event", "false"));

			할로윈_Event = Boolean.parseBoolean(altSettings.getProperty("HW_Event", "false"));

			EVENT_각인해제인장_찬스 = Integer.parseInt(altSettings.getProperty("GakinEventChance", "0"));

			EVENT_루피주먹주머니_찬스 = Integer.parseInt(altSettings.getProperty("RuphyBraveEventChance", "0"));

			EVENT_SEHO_BOOMB_CHANCE = Integer.parseInt(altSettings.getProperty("Event_box_chance", "0"));

			DRAGON_2DAY_EVENT = Boolean.parseBoolean(altSettings.getProperty("Dragon2DayEvent", "false"));

			DANTES_2DAY_EVENT = Boolean.parseBoolean(altSettings.getProperty("Dantes2DayEvent", "false"));

			Orim_EVENT = Boolean.parseBoolean(altSettings.getProperty("OrimEvent", "false"));

			RuphyBrave_3DAY_EVENT = Boolean.parseBoolean(altSettings.getProperty("RuphyBrave3DayEvent", "false"));

			Tam_Ok = Boolean.parseBoolean(altSettings.getProperty("TamOK", "false"));
			Tam_Time = Integer.parseInt(altSettings.getProperty("TamTime", "15"));
			Tam_Count = Integer.parseInt(altSettings.getProperty("TamCount", "600"));

			무혈상점 = Integer.parseInt(altSettings.getProperty("ShopLevel", "5"));
			드랍레벨 = Integer.parseInt(altSettings.getProperty("DropLevel", "5"));

			D_Reset_Time = Integer.parseInt(altSettings.getProperty("DungeonResetTime", "9"));

		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + ALT_SETTINGS_FILE + " File.");
		}

		try {
			Properties botSettings = new Properties();
			InputStream is = new FileInputStream(new File(BOT_SETTINGS_FILE));
			botSettings.load(is);
			is.close();
			Robot_Gaho = Boolean.parseBoolean(botSettings.getProperty("Robotgaho", "false"));
			로봇요정활1 = Integer.parseInt(botSettings.getProperty("robotbow1", "12314"));
			로봇요정활2 = Integer.parseInt(botSettings.getProperty("robotbow2", "12314"));
			로봇요정활3 = Integer.parseInt(botSettings.getProperty("robotbow3", "12314"));
			로봇요정활4 = Integer.parseInt(botSettings.getProperty("robotbow4", "12314"));
			로봇요정활5 = Integer.parseInt(botSettings.getProperty("robotbow5", "12314"));
			로봇칼1 = Integer.parseInt(botSettings.getProperty("robotsowrd1", "12283"));
			로봇칼2 = Integer.parseInt(botSettings.getProperty("robotsowrd2", "12283"));
			로봇칼3 = Integer.parseInt(botSettings.getProperty("robotsowrd3", "12283"));
			로봇칼4 = Integer.parseInt(botSettings.getProperty("robotsowrd4", "12283"));
			로봇칼5 = Integer.parseInt(botSettings.getProperty("robotsowrd5", "12283"));
			로봇칼6 = Integer.parseInt(botSettings.getProperty("robotsowrd6", "12283"));
			로봇칼7 = Integer.parseInt(botSettings.getProperty("robotsowrd7", "12283"));
			로봇칼8 = Integer.parseInt(botSettings.getProperty("robotsowrd8", "12283"));
			로봇칼9 = Integer.parseInt(botSettings.getProperty("robotsowrd9", "12283"));
			인형타입활 = Integer.parseInt(botSettings.getProperty("robotdollbowtype", "22"));
			인형타입칼1 = Integer.parseInt(botSettings.getProperty("robotdollswordtype1", "21"));
			인형타입칼2 = Integer.parseInt(botSettings.getProperty("robotdollswordtype2", "26"));
			인형타입칼3 = Integer.parseInt(botSettings.getProperty("robotdollswordtype3", "26"));
			인형타입칼4 = Integer.parseInt(botSettings.getProperty("robotdollswordtype4", "26"));
			인형타입칼5 = Integer.parseInt(botSettings.getProperty("robotdollswordtype5", "26"));
			인형타입칼6 = Integer.parseInt(botSettings.getProperty("robotdollswordtype6", "26"));
			인형타입경험치 = Integer.parseInt(botSettings.getProperty("robotdolltypeexp", "26"));
			인형타입활엔피씨아이디 = Integer.parseInt(botSettings.getProperty("robotdollbownpcid", "41915"));
			인형타입칼엔피씨아이디1 = Integer.parseInt(botSettings.getProperty("robotdollswordnpcid1", "4500161"));
			인형타입칼엔피씨아이디2 = Integer.parseInt(botSettings.getProperty("robotdollswordnpcid2", "45000161"));
			인형타입칼엔피씨아이디3 = Integer.parseInt(botSettings.getProperty("robotdollswordnpcid3", "4500161"));
			인형타입칼엔피씨아이디4 = Integer.parseInt(botSettings.getProperty("robotdollswordnpcid4", "4500161"));
			인형타입칼엔피씨아이디5 = Integer.parseInt(botSettings.getProperty("robotdollswordnpcid5", "4500161"));
			인형타입칼엔피씨아이디6 = Integer.parseInt(botSettings.getProperty("robotdollswordnpcid6", "4500161"));
			경험치인형엔피씨아이디 = Integer.parseInt(botSettings.getProperty("robotdollexpnpcid", "4500161"));
			봇시작레벨 = Integer.parseInt(botSettings.getProperty("botstartLev", "56"));
			봇기술적중 = Integer.parseInt(botSettings.getProperty("bottechnichit", "5"));
			봇정령적중 = Integer.parseInt(botSettings.getProperty("botspirithit", "0"));
			봇용언적중 = Integer.parseInt(botSettings.getProperty("botdragonhit", "0"));
			봇공포적중 = Integer.parseInt(botSettings.getProperty("botfearhit", "0"));
			봇기술내성 = Integer.parseInt(botSettings.getProperty("bottechnictollance", "30"));
			봇정령내성 = Integer.parseInt(botSettings.getProperty("botspirittollance", "10"));
			봇용언내성 = Integer.parseInt(botSettings.getProperty("botdragontollance", "10"));
			봇공포내성 = Integer.parseInt(botSettings.getProperty("botfeartollance", "10"));
			요정활기본타격치PC = Integer.parseInt(botSettings.getProperty("bowbasepc", "10"));
			요정활랜덤타격치PC = Integer.parseInt(botSettings.getProperty("bowrndpc", "10"));
			기사기본타격치PC = Integer.parseInt(botSettings.getProperty("knightbasepc", "10"));
			기사랜덤타격치PC = Integer.parseInt(botSettings.getProperty("knightrndpc", "10"));
			전사기본타격치PC = Integer.parseInt(botSettings.getProperty("warriorbasepc", "10"));
			전사랜덤타격치PC = Integer.parseInt(botSettings.getProperty("warriorrndpc", "10"));
			다엘기본타격치PC = Integer.parseInt(botSettings.getProperty("darkelfbasepc", "10"));
			다엘랜덤타격치PC = Integer.parseInt(botSettings.getProperty("darkelfrndpc", "10"));
			용기사기본타격치PC = Integer.parseInt(botSettings.getProperty("dragonebasepc", "10"));
			용기사랜덤타격치PC = Integer.parseInt(botSettings.getProperty("dragonerndpc", "10"));
			군검요기본타격치PC = Integer.parseInt(botSettings.getProperty("crownbasepc", "10"));
			군검요랜덤타격치PC = Integer.parseInt(botSettings.getProperty("crownrndpc", "10"));
			요정활기본타격치NPC = Integer.parseInt(botSettings.getProperty("bowbasenpc", "10"));
			요정활랜덤타격치NPC = Integer.parseInt(botSettings.getProperty("bowrndnpc", "10"));
			기사기본타격치NPC = Integer.parseInt(botSettings.getProperty("knightbasenpc", "10"));
			기사랜덤타격치NPC = Integer.parseInt(botSettings.getProperty("knightrndnpc", "10"));
			전사기본타격치NPC = Integer.parseInt(botSettings.getProperty("warriorbasenpc", "10"));
			전사랜덤타격치NPC = Integer.parseInt(botSettings.getProperty("warriorrndnpc", "10"));
			다엘기본타격치NPC = Integer.parseInt(botSettings.getProperty("darkelfbasenpc", "10"));
			다엘랜덤타격치NPC = Integer.parseInt(botSettings.getProperty("darkelfrndnpc", "10"));
			용기사기본타격치NPC = Integer.parseInt(botSettings.getProperty("dragonebasenpc", "10"));
			용기사랜덤타격치NPC = Integer.parseInt(botSettings.getProperty("dragonerndnpc", "10"));
			군검요기본타격치NPC = Integer.parseInt(botSettings.getProperty("crownbasenpc", "10"));
			군검요랜덤타격치NPC = Integer.parseInt(botSettings.getProperty("crownrndnpc", "10"));
			봇물약기본회복량 = Integer.parseInt(botSettings.getProperty("botposionbase", "45"));
			봇물약랜덤회복량 = Integer.parseInt(botSettings.getProperty("botposionrnd", "65"));
			봇요정싸이클론 = Boolean.parseBoolean(botSettings.getProperty("botelfcyclone", "false"));
			봇요정악장 = Boolean.parseBoolean(botSettings.getProperty("botnightmare", "false"));
			봇전사태풍 = Boolean.parseBoolean(botSettings.getProperty("bottypoon", "false"));
			봇군주엠파 = Boolean.parseBoolean(botSettings.getProperty("botcrownempire", "false"));
			봇게일 = Boolean.parseBoolean(botSettings.getProperty("botgalse", "false"));
			봇똥물 = Boolean.parseBoolean(botSettings.getProperty("botpolute", "false"));
			로봇막피 = Boolean.parseBoolean(botSettings.getProperty("botattckpc", "false"));

		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + BOT_SETTINGS_FILE + " File.");
		}

		// charsettings.properties
		try {
			Properties charSettings = new Properties();
			InputStream is = new FileInputStream(new File(CHAR_SETTINGS_CONFIG_FILE));
			charSettings.load(is);
			is.close();

			PRINCE_MAX_HP = Integer.parseInt(charSettings.getProperty("PrinceMaxHP", "1000"));

			PRINCE_MAX_MP = Integer.parseInt(charSettings.getProperty("PrinceMaxMP", "800"));

			KNIGHT_MAX_HP = Integer.parseInt(charSettings.getProperty("KnightMaxHP", "1400"));

			KNIGHT_MAX_MP = Integer.parseInt(charSettings.getProperty("KnightMaxMP", "600"));

			ELF_MAX_HP = Integer.parseInt(charSettings.getProperty("ElfMaxHP", "1000"));

			ELF_MAX_MP = Integer.parseInt(charSettings.getProperty("ElfMaxMP", "900"));

			WIZARD_MAX_HP = Integer.parseInt(charSettings.getProperty("WizardMaxHP", "800"));

			WIZARD_MAX_MP = Integer.parseInt(charSettings.getProperty("WizardMaxMP", "1200"));

			DARKELF_MAX_HP = Integer.parseInt(charSettings.getProperty("DarkelfMaxHP", "1000"));

			DARKELF_MAX_MP = Integer.parseInt(charSettings.getProperty("DarkelfMaxMP", "900"));

			DRAGONKNIGHT_MAX_HP = Integer.parseInt(charSettings.getProperty("DragonknightMaxHP", "1000"));

			DRAGONKNIGHT_MAX_MP = Integer.parseInt(charSettings.getProperty("DragonknightMaxMP", "900"));

			BLACKWIZARD_MAX_HP = Integer.parseInt(charSettings.getProperty("BlackwizardMaxHP", "900"));

			BLACKWIZARD_MAX_MP = Integer.parseInt(charSettings.getProperty("BlackwizardMaxMP", "1100"));

			MAXLEVEL = Integer.parseInt(charSettings.getProperty("LimitLevel", "99"));

			LV50_EXP = Integer.parseInt(charSettings.getProperty("Lv50Exp", "1"));
			LV51_EXP = Integer.parseInt(charSettings.getProperty("Lv51Exp", "1"));
			LV52_EXP = Integer.parseInt(charSettings.getProperty("Lv52Exp", "1"));
			LV53_EXP = Integer.parseInt(charSettings.getProperty("Lv53Exp", "1"));
			LV54_EXP = Integer.parseInt(charSettings.getProperty("Lv54Exp", "1"));
			LV55_EXP = Integer.parseInt(charSettings.getProperty("Lv55Exp", "1"));
			LV56_EXP = Integer.parseInt(charSettings.getProperty("Lv56Exp", "1"));
			LV57_EXP = Integer.parseInt(charSettings.getProperty("Lv57Exp", "1"));
			LV58_EXP = Integer.parseInt(charSettings.getProperty("Lv58Exp", "1"));
			LV59_EXP = Integer.parseInt(charSettings.getProperty("Lv59Exp", "1"));
			LV60_EXP = Integer.parseInt(charSettings.getProperty("Lv60Exp", "1"));
			LV61_EXP = Integer.parseInt(charSettings.getProperty("Lv61Exp", "1"));
			LV62_EXP = Integer.parseInt(charSettings.getProperty("Lv62Exp", "1"));
			LV63_EXP = Integer.parseInt(charSettings.getProperty("Lv63Exp", "1"));
			LV64_EXP = Integer.parseInt(charSettings.getProperty("Lv64Exp", "1"));
			LV65_EXP = Integer.parseInt(charSettings.getProperty("Lv65Exp", "2"));
			LV66_EXP = Integer.parseInt(charSettings.getProperty("Lv66Exp", "2"));
			LV67_EXP = Integer.parseInt(charSettings.getProperty("Lv67Exp", "2"));
			LV68_EXP = Integer.parseInt(charSettings.getProperty("Lv68Exp", "2"));
			LV69_EXP = Integer.parseInt(charSettings.getProperty("Lv69Exp", "2"));
			LV70_EXP = Integer.parseInt(charSettings.getProperty("Lv70Exp", "4"));
			LV71_EXP = Integer.parseInt(charSettings.getProperty("Lv71Exp", "4"));
			LV72_EXP = Integer.parseInt(charSettings.getProperty("Lv72Exp", "4"));
			LV73_EXP = Integer.parseInt(charSettings.getProperty("Lv73Exp", "4"));
			LV74_EXP = Integer.parseInt(charSettings.getProperty("Lv74Exp", "4"));
			LV75_EXP = Integer.parseInt(charSettings.getProperty("Lv75Exp", "8"));
			LV76_EXP = Integer.parseInt(charSettings.getProperty("Lv76Exp", "8"));
			LV77_EXP = Integer.parseInt(charSettings.getProperty("Lv77Exp", "8"));
			LV78_EXP = Integer.parseInt(charSettings.getProperty("Lv78Exp", "8"));
			LV79_EXP = Integer.parseInt(charSettings.getProperty("Lv79Exp", "16"));
			LV80_EXP = Integer.parseInt(charSettings.getProperty("Lv80Exp", "32"));
			LV81_EXP = Integer.parseInt(charSettings.getProperty("Lv81Exp", "64"));
			LV82_EXP = Integer.parseInt(charSettings.getProperty("Lv82Exp", "128"));
			LV83_EXP = Integer.parseInt(charSettings.getProperty("Lv83Exp", "256"));
			LV84_EXP = Integer.parseInt(charSettings.getProperty("Lv84Exp", "512"));
			LV85_EXP = Integer.parseInt(charSettings.getProperty("Lv85Exp", "1024"));
			LV86_EXP = Integer.parseInt(charSettings.getProperty("Lv86Exp", "2048"));
			LV87_EXP = Integer.parseInt(charSettings.getProperty("Lv87Exp", "4096"));
			LV88_EXP = Integer.parseInt(charSettings.getProperty("Lv88Exp", "8192"));
			LV89_EXP = Integer.parseInt(charSettings.getProperty("Lv89Exp", "16384"));
			LV90_EXP = Integer.parseInt(charSettings.getProperty("Lv90Exp", "32768"));
			LV91_EXP = Integer.parseInt(charSettings.getProperty("Lv91Exp", "65536"));
			LV92_EXP = Integer.parseInt(charSettings.getProperty("Lv92Exp", "131072"));
			LV93_EXP = Integer.parseInt(charSettings.getProperty("Lv93Exp", "262144"));
			LV94_EXP = Integer.parseInt(charSettings.getProperty("Lv94Exp", "524288"));
			LV95_EXP = Integer.parseInt(charSettings.getProperty("Lv95Exp", "1048576"));
			LV96_EXP = Integer.parseInt(charSettings.getProperty("Lv96Exp", "2097152"));
			LV97_EXP = Integer.parseInt(charSettings.getProperty("Lv97Exp", "4194304"));
			LV98_EXP = Integer.parseInt(charSettings.getProperty("Lv98Exp", "8388608"));
			LV99_EXP = Integer.parseInt(charSettings.getProperty("Lv99Exp", "16777216"));

			ExpTable._expPenalty[0] = LV50_EXP;
			ExpTable._expPenalty[1] = LV51_EXP;
			ExpTable._expPenalty[2] = LV52_EXP;
			ExpTable._expPenalty[3] = LV53_EXP;
			ExpTable._expPenalty[4] = LV54_EXP;
			ExpTable._expPenalty[5] = LV55_EXP;
			ExpTable._expPenalty[6] = LV56_EXP;
			ExpTable._expPenalty[7] = LV57_EXP;
			ExpTable._expPenalty[8] = LV58_EXP;
			ExpTable._expPenalty[9] = LV59_EXP;
			ExpTable._expPenalty[10] = LV60_EXP;
			ExpTable._expPenalty[11] = LV61_EXP;
			ExpTable._expPenalty[12] = LV62_EXP;
			ExpTable._expPenalty[13] = LV63_EXP;
			ExpTable._expPenalty[14] = LV64_EXP;
			ExpTable._expPenalty[15] = LV65_EXP;
			ExpTable._expPenalty[16] = LV66_EXP;
			ExpTable._expPenalty[17] = LV67_EXP;
			ExpTable._expPenalty[18] = LV68_EXP;
			ExpTable._expPenalty[19] = LV69_EXP;
			ExpTable._expPenalty[20] = LV70_EXP;
			ExpTable._expPenalty[21] = LV71_EXP;
			ExpTable._expPenalty[22] = LV72_EXP;
			ExpTable._expPenalty[23] = LV73_EXP;
			ExpTable._expPenalty[24] = LV74_EXP;
			ExpTable._expPenalty[25] = LV75_EXP;
			ExpTable._expPenalty[26] = LV76_EXP;
			ExpTable._expPenalty[27] = LV77_EXP;
			ExpTable._expPenalty[28] = LV78_EXP;
			ExpTable._expPenalty[29] = LV79_EXP;
			ExpTable._expPenalty[30] = LV80_EXP;
			ExpTable._expPenalty[31] = LV81_EXP;
			ExpTable._expPenalty[32] = LV82_EXP;
			ExpTable._expPenalty[33] = LV83_EXP;
			ExpTable._expPenalty[34] = LV84_EXP;
			ExpTable._expPenalty[35] = LV85_EXP;
			ExpTable._expPenalty[36] = LV86_EXP;
			ExpTable._expPenalty[37] = LV87_EXP;
			ExpTable._expPenalty[38] = LV88_EXP;
			ExpTable._expPenalty[39] = LV89_EXP;
			ExpTable._expPenalty[40] = LV90_EXP;
			ExpTable._expPenalty[41] = LV91_EXP;
			ExpTable._expPenalty[42] = LV92_EXP;
			ExpTable._expPenalty[43] = LV93_EXP;
			ExpTable._expPenalty[44] = LV94_EXP;
			ExpTable._expPenalty[45] = LV95_EXP;
			ExpTable._expPenalty[46] = LV96_EXP;
			ExpTable._expPenalty[47] = LV97_EXP;
			ExpTable._expPenalty[48] = LV98_EXP;
			ExpTable._expPenalty[49] = LV99_EXP;
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + CHAR_SETTINGS_CONFIG_FILE + " File.");
		}
		try {
			Properties Eventlink = new Properties();
			InputStream is = new FileInputStream(new File(CHOLONG_SETTINGS_CONFIG_FILE));
			Eventlink.load(is);
			is.close();

			/** 이벤트라인 셋팅파일 **/
			/** 이벤트용 **/
			CRAFT_TABLE_ONE = Integer.parseInt(Eventlink.getProperty("one", "0")); // 장군;
			CRAFT_TABLE_TWO = Integer.parseInt(Eventlink.getProperty("two", "0")); // 장군;;
			CRAFT_TABLE_THREE = Integer.parseInt(Eventlink.getProperty("three", "0")); // 장군;;
			CRAFT_TABLE_FOUR = Integer.parseInt(Eventlink.getProperty("four", "0")); // 장군;;
			CRAFT_TABLE_FIVE = Integer.parseInt(Eventlink.getProperty("five", "0")); // 장군;;
			CRAFT_TABLE_SIX = Integer.parseInt(Eventlink.getProperty("six", "0")); // 장군;;
			CRAFT_TABLE_SEVEN = Integer.parseInt(Eventlink.getProperty("seven", "0")); // 장군;;
			CRAFT_TABLE_EIGHT = Integer.parseInt(Eventlink.getProperty("eight", "0")); // 장군;;
			CRAFT_TABLE_NINE = Integer.parseInt(Eventlink.getProperty("nine", "0")); // 장군;
			CRAFT_TABLE_TEN = Integer.parseInt(Eventlink.getProperty("ten", "0")); // 장군;;
			CRAFT_TABLE = Integer.parseInt(Eventlink.getProperty("zero", "1")); // 장군;;
			인형확률 = Integer.parseInt(Eventlink.getProperty("dollchance", "1"));
			CLAN_BLESS_ALL_USE = Boolean.parseBoolean(Eventlink.getProperty("ClanBlessAllUse", "false"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, "Config.에서 에러가 발생했습니다.", e);
			throw new Error("Failed to Load " + CHOLONG_SETTINGS_CONFIG_FILE + " File.");
		}
		validate();
		loadWeekQuest();
	}

	public static final String WEEKQUEST_CONFIG_FILE = "./config/weekquest.properties";
	public static int WQ_UPDATE_TYPE;
	public static int WQ_UPDATE_WEEK;
	public static int WQ_UPDATE_TIME;

	public static void loadWeekQuest() {
		try {
			Properties wq = new Properties();
			InputStream is = new FileInputStream(new File(WEEKQUEST_CONFIG_FILE));
			wq.load(is);
			is.close();
			WQ_UPDATE_TYPE = Integer.parseInt(wq.getProperty("WeekQuest_UpdateType", "1"));
			WQ_UPDATE_WEEK = Integer.parseInt(wq.getProperty("WeekQuest_UpdateWeek", "4"));
			WQ_UPDATE_TIME = Integer.parseInt(wq.getProperty("WeekQuest_UpdateTime", "10"));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			throw new Error("Failed to Load " + WEEKQUEST_CONFIG_FILE + " File.");
		}

	}

	public static String toKor(String src) {
		String str = null;
		try {
			byte[] b = src.getBytes("8859_1");
			str = new String(b, "MS932");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	private static void validate() {
		if (!IntRange.includes(Config.ALT_ITEM_DELETION_RANGE, 0, 5)) {
			throw new IllegalStateException("ItemDeletionRange의 값이 설정 가능 범위외입니다. ");
		}

		if (!IntRange.includes(Config.ALT_ITEM_DELETION_TIME, 1, 35791)) {
			throw new IllegalStateException("ItemDeletionTime의 값이 설정 가능 범위외입니다. ");
		}
	}

	public static boolean setParameterValue(String pName, String pValue) {
		// server.properties
		if (pName.equalsIgnoreCase("ServerType")) {
			GAME_SERVER_TYPE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("GameserverHostname")) {
			GAME_SERVER_HOST_NAME = pValue;
		} else if (pName.equalsIgnoreCase("GameserverPort")) {
			GAME_SERVER_PORT = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Driver")) {
			DB_DRIVER = pValue;
		} else if (pName.equalsIgnoreCase("URL")) {
			DB_URL = pValue;
		} else if (pName.equalsIgnoreCase("Login")) {
			DB_LOGIN = pValue;
		} else if (pName.equalsIgnoreCase("Password")) {
			DB_PASSWORD = pValue;
		} else if (pName.equalsIgnoreCase("ClientLanguage")) {
			CLIENT_LANGUAGE = Integer.parseInt(pValue);

		} else if (pName.equalsIgnoreCase("TimeZone")) {
			TIME_ZONE = pValue;
		} else if (pName.equalsIgnoreCase("AutomaticKick")) {
			AUTOMATIC_KICK = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AutoCreateAccounts")) {
			AUTO_CREATE_ACCOUNTS = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("MaximumOnlineUsers")) {
			MAX_ONLINE_USERS = Short.parseShort(pValue);
		} else if (pName.equalsIgnoreCase("LoggingWeaponEnchant")) {
			LOGGING_WEAPON_ENCHANT = Byte.parseByte(pValue);
		} else if (pName.equalsIgnoreCase("LoggingArmorEnchant")) {
			LOGGING_ARMOR_ENCHANT = Byte.parseByte(pValue);
		} else if (pName.equalsIgnoreCase("LoggingTime")) {
			LOGGING_TIME = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("CharacterConfigInServerSide")) {
			CHARACTER_CONFIG_IN_SERVER_SIDE = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("Charpass")) {
			캐릭터비번사용여부 = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("Allow2PC")) {
			ALLOW_2PC = Boolean.parseBoolean(pValue);
		} else if (pName.equalsIgnoreCase("LevelDownRange")) {
			LEVEL_DOWN_RANGE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AccountPassword")) {
			ACCOUNT_PASSWORD = Boolean.parseBoolean(pValue);// 패스워드 암호화 삭제 소스 0813 추가
		} else if (pName.equalsIgnoreCase("SendPacketBeforeTeleport")) {
			SEND_PACKET_BEFORE_TELEPORT = Boolean.parseBoolean(pValue);
		}
		// rates.properties
		else if (pName.equalsIgnoreCase("RateXp")) {
			RATE_XP = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("RateLawful")) {
			RATE_LAWFUL = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("RateKarma")) {
			RATE_KARMA = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("RateDropAdena")) {
			RATE_DROP_ADENA = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("RateDropItems")) {
			RATE_DROP_ITEMS = Double.parseDouble(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceWeapon")) {
			ENCHANT_CHANCE_WEAPON = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceWeapon7")) {
			ENCHANT_CHANCE_WEAPON7 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceWeapon8")) {
			ENCHANT_CHANCE_WEAPON8 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceWeapon9")) {
			ENCHANT_CHANCE_WEAPON9 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceWeapon10")) {
			ENCHANT_CHANCE_WEAPON10 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("FeatherTime")) {
			FEATHER_TIME = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("FeatherNumber")) {
			FEATHER_NUMBER = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("BattleZone")) {
			배틀존작동유무 = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("BattleLevel")) {
			배틀존입장레벨 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ClanNumber")) {
			CLAN_NUMBER = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("CastleNumber")) {
			CASTLE_NUMBER = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceArmor")) {
			ENCHANT_CHANCE_ARMOR = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("EnchantChanceAccessory")) {
			ENCHANT_CHANCE_ACCESSORY = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Weightrate")) {
			RATE_WEIGHT_LIMIT = Byte.parseByte(pValue);
		}

		// altsettings.properties
		else if (pName.equalsIgnoreCase("GlobalChatLevel")) {
			GLOBAL_CHAT_LEVEL = Short.parseShort(pValue);
		} else if (pName.equalsIgnoreCase("WhisperChatLevel")) {
			WHISPER_CHAT_LEVEL = Short.parseShort(pValue);
		} else if (pName.equalsIgnoreCase("AutoLoot")) {
			AUTO_LOOT = Byte.parseByte(pValue);
		} else if (pName.equalsIgnoreCase("LOOTING_RANGE")) {
			LOOTING_RANGE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AltNonPvP")) {
			ALT_NONPVP = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("AttackMessageOn")) {
			ALT_ATKMSG = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("ChangeTitleByOneself")) {
			CHANGE_TITLE_BY_ONESELF = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxClanMember")) {
			MAX_CLAN_MEMBER = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ClanAlliance")) {
			CLAN_ALLIANCE = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxPT")) {
			MAX_PT = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("MaxChatPT")) {
			MAX_CHAT_PT = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("SimWarPenalty")) {
			SIM_WAR_PENALTY = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("GetBack")) {
			GET_BACK = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("AutomaticItemDeletionTime")) {
			ALT_ITEM_DELETION_TIME = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AutomaticItemDeletionRange")) {
			ALT_ITEM_DELETION_RANGE = Byte.parseByte(pValue);
		} else if (pName.equalsIgnoreCase("HalloweenEvent")) {
			ALT_HALLOWEENEVENT = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("HalloweenEvent2009")) {
			ALT_HALLOWEENEVENT2009 = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("FantasyEvent")) {
			ALT_FANTASYEVENT = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("ChuSeokEvent")) {
			ALT_CHUSEOKEVENT = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("Robotgaho")) {
			Robot_Gaho = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("HouseTaxInterval")) {
			HOUSE_TAX_INTERVAL = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxDollCount")) {
			MAX_DOLL_COUNT = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("ReturnToNature")) {
			RETURN_TO_NATURE = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxNpcItem")) {
			MAX_NPC_ITEM = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxPersonalWarehouseItem")) {
			MAX_PERSONAL_WAREHOUSE_ITEM = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("MaxClanWarehouseItem")) {
			MAX_CLAN_WAREHOUSE_ITEM = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("DeleteCharacterAfter7Days")) {
			DELETE_CHARACTER_AFTER_7DAYS = Boolean.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("GMCODE")) {
			GMCODE = Integer.valueOf(pValue);
		} else if (pName.equalsIgnoreCase("DeleteDBDAY")) {
			DELETE_DB_DAYS = Integer.valueOf(pValue);
		}

		else if (pName.equalsIgnoreCase("ExpGive")) {
			EXP_GIVE = Integer.parseInt(pValue);
		}
		// charsettings.properties
		else if (pName.equalsIgnoreCase("PrinceMaxHP")) {
			PRINCE_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("LimitLevel")) {
			MAXLEVEL = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("PrinceMaxMP")) {
			PRINCE_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("KnightMaxHP")) {
			KNIGHT_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("KnightMaxMP")) {
			KNIGHT_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ElfMaxHP")) {
			ELF_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ElfMaxMP")) {
			ELF_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("WizardMaxHP")) {
			WIZARD_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("WizardMaxMP")) {
			WIZARD_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DarkelfMaxHP")) {
			DARKELF_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DarkelfMaxMP")) {
			DARKELF_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DragonknightMaxHP")) {
			DRAGONKNIGHT_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("DragonknightMaxMP")) {
			DRAGONKNIGHT_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("BlackwizardMaxHP")) {
			BLACKWIZARD_MAX_HP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("BlackwizardMaxMP")) {
			BLACKWIZARD_MAX_MP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv50Exp")) {
			LV50_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv51Exp")) {
			LV51_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv52Exp")) {
			LV52_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv53Exp")) {
			LV53_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv54Exp")) {
			LV54_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv55Exp")) {
			LV55_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv56Exp")) {
			LV56_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv57Exp")) {
			LV57_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv58Exp")) {
			LV58_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv59Exp")) {
			LV59_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv60Exp")) {
			LV60_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv61Exp")) {
			LV61_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv62Exp")) {
			LV62_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv63Exp")) {
			LV63_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv64Exp")) {
			LV64_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv65Exp")) {
			LV65_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv66Exp")) {
			LV66_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv67Exp")) {
			LV67_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv68Exp")) {
			LV68_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv69Exp")) {
			LV69_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv70Exp")) {
			LV70_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv71Exp")) {
			LV71_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv72Exp")) {
			LV72_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv73Exp")) {
			LV73_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv74Exp")) {
			LV74_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv75Exp")) {
			LV75_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv76Exp")) {
			LV76_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv77Exp")) {
			LV77_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv78Exp")) {
			LV78_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv79Exp")) {
			LV79_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv80Exp")) {
			LV80_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv81Exp")) {
			LV81_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv82Exp")) {
			LV82_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv83Exp")) {
			LV83_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv84Exp")) {
			LV84_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv85Exp")) {
			LV85_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv86Exp")) {
			LV86_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv87Exp")) {
			LV87_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv88Exp")) {
			LV88_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv89Exp")) {
			LV89_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv90Exp")) {
			LV90_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv91Exp")) {
			LV91_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv92Exp")) {
			LV92_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv93Exp")) {
			LV93_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv94Exp")) {
			LV94_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv95Exp")) {
			LV95_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv96Exp")) {
			LV96_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv97Exp")) {
			LV97_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv98Exp")) {
			LV98_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Lv99Exp")) {
			LV99_EXP = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("Raidtime")) {
			레이드시간 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("SnapperMaxEnchant")) {
			스냅퍼최대인챈 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("RoomteeceMaxEnchant")) {
			룸티스최대인챈 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("acaccessoryMaxEnchant")) {
			장신구최대인챈 = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("AnoldeTime")) {
			아놀드이벤트시간 = Integer.parseInt(pValue);
			// Eventlink set file
		} else if (pName.equalsIgnoreCase("one")) {
			CRAFT_TABLE_ONE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("two")) {
			CRAFT_TABLE_TWO = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("three")) {
			CRAFT_TABLE_THREE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("four")) {
			CRAFT_TABLE_FOUR = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("five")) {
			CRAFT_TABLE_FIVE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("six")) {
			CRAFT_TABLE_SIX = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("seven")) {
			CRAFT_TABLE_SEVEN = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("eight")) {
			CRAFT_TABLE_EIGHT = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("nine")) {
			CRAFT_TABLE_NINE = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("ten")) {
			CRAFT_TABLE_TEN = Integer.parseInt(pValue);
		} else if (pName.equalsIgnoreCase("zero")) {
			CRAFT_TABLE = Integer.parseInt(pValue);
		} else {
			return false;
		}
		return true;
	}

	private Config() {
	}

	public final static int etc_arrow = 0;
	public final static int etc_wand = 1;
	public final static int etc_light = 2;
	public final static int etc_gem = 3;
	public final static int etc_potion = 6;
	public final static int etc_firecracker = 5;
	public final static int etc_food = 7;
	public final static int etc_scroll = 8;
	public final static int etc_questitem = 9;
	public final static int etc_spellbook = 10;
	public final static int etc_other = 12;
	public final static int etc_material = 13;
	public final static int etc_sting = 15;
	public final static int etc_treasurebox = 16;
	public final static int etc_Doll = 17;
	public final static int etc_pet = 18;
	public final static int etc_petitem = 20;

	public static enum LOG {
		chat, error, system, badplayer, enchant, inventory, time
	}

	/*
	 * public static synchronized String YearMonthDate2(){ try{ int 년 = Year();
	 * String 년2; if(년 < 10){ 년2 = "0"+년; }else{ 년2 = Integer.toString(년); } int 월 =
	 * Month(); String 월2=null; if(월 < 10){ 월2 = "0"+월; }else{ 월2 =
	 * Integer.toString(월); } int 일 = Date(); String 일2 = null; if(일 < 10){ 일2 =
	 * "0"+일; }else{ 일2 = Integer.toString(일); } return 년2+월2+일2; }catch (Exception
	 * e){}
	 *
	 * return "000000"; }
	 */
	public static synchronized String YearMonthDate2() {
		try {

			Date day = new Date(System.currentTimeMillis());
			int 년 = day.getYear() - 100;
			String 년2;
			if (년 < 10) {
				년2 = "0" + 년;
			} else {
				년2 = Integer.toString(년);
			}
			int 월 = (day.getMonth() + 1);
			String 월2 = null;
			if (월 < 10) {
				월2 = "0" + 월;
			} else {
				월2 = Integer.toString(월);
			}
			int 일 = day.getDate();
			String 일2 = null;
			if (일 < 10) {
				일2 = "0" + 일;
			} else {
				일2 = Integer.toString(일);
			}
			return 년2 + 월2 + 일2;
		} catch (Exception e) {
		}

		return "000000";
	}

	public static synchronized int Year() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("KST"));
		return cal.get(Calendar.YEAR) - 2000;
	}

	public static synchronized int Month() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("KST"));
		return cal.get(Calendar.MONTH) + 1;
	}

	public static synchronized int Date() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("KST"));
		return cal.get(Calendar.DATE);
	}

	public static synchronized int Hour() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("KST"));
		return cal.get(Calendar.HOUR_OF_DAY);
	}

	public static synchronized int Minute() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("KST"));
		return cal.get(Calendar.MINUTE);
	}

	public static synchronized String Time() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("KST"));
		int h = cal.get(Calendar.HOUR);
		int m = cal.get(Calendar.MINUTE);
		StringBuffer sb = new StringBuffer();
		if (h < 10) {
			sb.append("0");
		}
		sb.append(h);
		sb.append(":");
		if (m < 10) {
			sb.append("0");
		}
		sb.append(m);
		return sb.toString();
	}
}