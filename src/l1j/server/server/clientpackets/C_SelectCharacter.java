package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.ADDITIONAL_FIRE;
import static l1j.server.server.model.skill.L1SkillId.BERSERKERS;
import static l1j.server.server.model.skill.L1SkillId.CLEAR_MIND;
import static l1j.server.server.model.skill.L1SkillId.CONCENTRATION;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_닭고기;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_탐닭고기;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_탐한우;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_한우;
import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.DECREASE_WEIGHT;
import static l1j.server.server.model.skill.L1SkillId.DRESS_EVASION;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_FALL_DOWN;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_FIRE;
import static l1j.server.server.model.skill.L1SkillId.ELEMENTAL_PROTECTION;
import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION2;
import static l1j.server.server.model.skill.L1SkillId.EXP_POTION3;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_A;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_B;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_C;
import static l1j.server.server.model.skill.L1SkillId.FEATHER_BUFF_D;
import static l1j.server.server.model.skill.L1SkillId.HEUKSA;
import static l1j.server.server.model.skill.L1SkillId.INSIGHT;
import static l1j.server.server.model.skill.L1SkillId.MORTAL_BODY;
import static l1j.server.server.model.skill.L1SkillId.NATURES_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.PANIC;
import static l1j.server.server.model.skill.L1SkillId.PATIENCE;
import static l1j.server.server.model.skill.L1SkillId.POLLUTE_WATER;
import static l1j.server.server.model.skill.L1SkillId.RESIST_ELEMENTAL;
import static l1j.server.server.model.skill.L1SkillId.RESIST_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import static l1j.server.server.model.skill.L1SkillId.SILENCE;
import static l1j.server.server.model.skill.L1SkillId.SOUL_OF_FLAME;
import static l1j.server.server.model.skill.L1SkillId.SPECIAL_COOKING;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CASHSCROLL3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CHAT_PROHIBITED;
import static l1j.server.server.model.skill.L1SkillId.STATUS_COMA_3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_COMA_5;
import static l1j.server.server.model.skill.L1SkillId.STATUS_ELFBRAVE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FRUIT;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_TIKAL_BOSSDIE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_커츠명궁;
import static l1j.server.server.model.skill.L1SkillId.STATUS_커츠투사;
import static l1j.server.server.model.skill.L1SkillId.STATUS_커츠현자;
import static l1j.server.server.model.skill.L1SkillId.STRIKER_GALE;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit1;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit2;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit3;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit4;
import static l1j.server.server.model.skill.L1SkillId.Tam_Fruit5;
import static l1j.server.server.model.skill.L1SkillId.VENOM_RESIST;
import static l1j.server.server.model.skill.L1SkillId.WEAKNESS;
import static l1j.server.server.model.skill.L1SkillId.WIND_SHACKLE;
import static l1j.server.server.model.skill.L1SkillId.miso1;
import static l1j.server.server.model.skill.L1SkillId.miso2;
import static l1j.server.server.model.skill.L1SkillId.miso3;
import static l1j.server.server.model.skill.L1SkillId.싸이매콤한라면;
import static l1j.server.server.model.skill.L1SkillId.천하장사버프;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random; //추가
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.SpecialEventHandler;
import l1j.server.GameSystem.UserRanking.UserRankingController;
import l1j.server.IndunSystem.MiniGame.BattleZone;
import l1j.server.MJBookQuestSystem.Loader.UserMonsterBookLoader;
import l1j.server.MJBookQuestSystem.Loader.UserWeekQuestLoader;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.CharacterQuestTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.CharcterRevengeTable;
import l1j.server.server.datatables.EvaSystemTable;
import l1j.server.server.datatables.ExcludeLetterTable;
import l1j.server.server.datatables.ExcludeTable;
import l1j.server.server.datatables.GetBackRestartTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.datatables.MonsterBookTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_CharacterConfig;
import l1j.server.server.serverpackets.S_ClanWindow;
import l1j.server.server.serverpackets.S_CreateItem;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ElfIcon;
import l1j.server.server.serverpackets.S_EventNotice;
//import l1j.server.server.serverpackets.S_EventNotice;
import l1j.server.server.serverpackets.S_FairlyConfig;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InvList;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_MaanIcons;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_MonsterUi;
import l1j.server.server.serverpackets.S_NewAddSkill;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_NewSkillIcons;
import l1j.server.server.serverpackets.S_NewUI;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharPack;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Party;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_Revenge;
import l1j.server.server.serverpackets.S_SMPacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SabuBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_SummonPack;
import l1j.server.server.serverpackets.S_SurvivalCry;
import l1j.server.server.serverpackets.S_SystemMessage; //추가
import l1j.server.server.serverpackets.S_UnityIcon;
import l1j.server.server.serverpackets.S_Unknown1;
import l1j.server.server.serverpackets.S_War;
import l1j.server.server.serverpackets.S_Weather;
import l1j.server.server.serverpackets.S_bonusstats;
import l1j.server.server.serverpackets.S_문장주시;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1EvaSystem;
import l1j.server.server.templates.L1GetBackRestart;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CheckInitStat;
import l1j.server.server.utils.SQLUtil;
import manager.LinAllManager;
import server.GameServer;
import server.LineageClient;
import server.controller.InvSwapController;
import server.system.autoshop.AutoShop;
import server.system.autoshop.AutoShopManager;

public class C_SelectCharacter extends ClientBasePacket {
	private static final String C_LOGIN_TO_SERVER = "[C] C_LoginToServer";
	private static Logger _log = Logger.getLogger(C_SelectCharacter.class.getName());

	private static final int[] omanLocX = { 32781, 32818, 32818, 32818 };
	private static final int[] omanLocY = { 32781, 32781, 32818, 32781 };

	public static final SimpleDateFormat ss = new SimpleDateFormat("MM-dd HH:mm", Locale.KOREA);
	public static FastTable<String> nameList = new FastTable<String>();
	public static FastTable<String> nameINOUTList = new FastTable<String>();

	private static final Random _Random = new Random();

	public C_SelectCharacter(byte abyte0[], LineageClient client) throws FileNotFoundException, Exception {
		super(abyte0);

		String charname = readS();

		try {
			GeneralThreadPool.getInstance().schedule(new login(charname, client), _Random.nextInt(500));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public C_SelectCharacter(LineageClient client, String NameChange) throws FileNotFoundException, Exception {
		super();
		try {
			GeneralThreadPool.getInstance().schedule(new login(NameChange, client), _Random.nextInt(100));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String HexToDex(int data, int digits) {
		String number = Integer.toHexString(data);
		for (int i = number.length(); i < digits; i++)
			number = "0" + number;
		return number;
	}

	public static String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(HexToDex(i, 4) + ": ");
			}
			result.append(HexToDex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char) t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}
		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char) t1);
				} else {
					result.append('.');
				}
			}
			result.append("\n");
		}
		return result.toString();
	}

	class login implements Runnable {
		String name = "";
		LineageClient client = null;

		public login(String _name, LineageClient _client) {
			name = _name;
			client = _client;
		}

		@Override
		public void run() {
			try {
				if (name.equalsIgnoreCase("") || client == null /*
																 * || client.close
																 */)
					return;
				login_server(name, client);
			} catch (Exception e) {
			}
		}
	}

	private void login_server(String charName, LineageClient client) {
		System.out.println("Login with " + charName);



		try {
			if (client.getAccount() == null) {
				System.out.println("계정 Null 접속 시도 " + charName);
				client.kick();
				client.close();
				return;
			}

			if (client.getActiveChar() != null) { // restart 상태가 아니란 소린가?
				System.out.println("1계정 2케릭 버그 시도 : " + charName + " 감지코드 1");
				client.kick();
				client.close();
				return;
			}

			if (GMCommands.접속이름체크) {
				if (nameList.contains(charName)) {
					if (L1World.getInstance().getPlayer(charName) == null) {
						GeneralThreadPool.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								GMCommands.connectCharNameReset();
							}
						}, 3000);
					} else {
						GameServer.DuplicationLoginCheck(charName, "2초내 연속 접속");
					}
					client.kick();
					client.close();
					return;
				}
				nameList.add(charName);
				GeneralThreadPool.getInstance().schedule(new charNameDelete(charName), 2000);
			}

			AutoShopManager shopManager = AutoShopManager.getInstance();
			AutoShop shopPlayer = shopManager.getShopPlayer(charName);
			if (shopPlayer != null) {
				L1PcInstance pc = L1World.getInstance().getPlayer(charName);
				shopPlayer.logout();
				shopManager.remove(shopPlayer);
				shopPlayer = null;
				pc.zombie = false;
			}

			if (GMCommands.접속이름체크) {
				if (nameINOUTList.contains(charName)) {
					if (L1World.getInstance().getPlayer(charName) == null) {
						GeneralThreadPool.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								GMCommands.connectCharNameReset();
							}
						}, 3000);
					} else {
						GameServer.DuplicationLoginCheck(charName, "다중 접속");
					}
					client.kick();
					client.close();
					return;
				}
				nameINOUTList.add(charName);
			}

			L1PcInstance pc = L1PcInstance.load(charName);
			String accountName = client.getAccountName();
			Account account = Account.load(pc.getAccountName());
			pc.setNetConnection(client);
			client.setActiveChar(pc);

			if (pc == null || !accountName.equals(pc.getAccountName())) {
				System.out.println("현제 계정에 없는 케릭 접속시도 : " + charName + " 계정 : " + client.getAccountName());
				client.kick();
				client.close();
				return;
			}

			// ///////////////// 스텟버그방지소스 /////////////////////
			L1PcInstance ckpc = L1World.getInstance().getPlayer(charName);
			if (ckpc != null && !ckpc.zombie && !ckpc.getAIprivateShop()) {
				System.out.println("1계정 2케릭 버그 시도 : " + charName + " 감지코드 2");
				client.kick();
				client.close();
				ckpc.logout();
				if (ckpc.getNetConnection() != null)
					ckpc.getNetConnection().kick();
				return;
			}
			for (L1PcInstance _client2 : L1World.getInstance().getAllPlayers()) {
				if (_client2 == null || _client2.zombie || _client2.샌드백 || _client2.getAIprivateShop()) {
					continue;
				}
				if (client.getAccountName().equalsIgnoreCase(_client2.getAccountName())) {
					System.out.println("1계정 2케릭 버그 시도 : " + charName + " 감지코드 3");
					client.kick();
					client.close();
					if (_client2.getNetConnection() != null) {
						_client2.getNetConnection().kick();
						_client2.getNetConnection().close();
					}
					return;
				}
			}

			if (pc.getLevel() > pc.getHighLevel()) {
				System.out.println("정상 레벨보다 높은레벨 절단 : " + charName);
				client.kick();
				client.close();
				return;
			}
			if (pc.getAbility().getCon() > 75 || pc.getAbility().getStr() > 75 || pc.getAbility().getDex() > 75 || pc.getAbility().getCha() > 75
					|| pc.getAbility().getInt() > 75 || pc.getAbility().getWis() > 75) {
				System.out.println("한가지 스탯 70이상 절단 : " + charName);
				client.kick();
				client.close();
				return;
			}

			// ///////////////// 스텟버그방지소스 /////////////////////

			try {
				_log.info("캐릭터 로그인: char=" + charName + " account=" + accountName + " host=" + client.getHostname());
			} catch (Exception e) {
				_log.info("캐릭터 로그인: char=" + charName + " account=" + accountName + " host= 호스트 에러");
			}

			LinAllManager.getInstance().LogConnectAppend(pc.getName(), client.getIp());
			
			int currentHpAtLoad = pc.getCurrentHp();
			int currentMpAtLoad = pc.getCurrentMp();

			pc.clearSkillMastery();
			pc.setOnlineStatus(1);
			CharacterTable.updateOnlineStatus(pc);
			L1World.getInstance().storeObject(pc);

			pc.set_delete(false); // 허공버그픽스

			client.setActiveChar(pc);

			pc.sendPackets(new S_Unknown1(), true);

			if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
				if (pc.getLevel() == 1) {
					pc.sendPackets(new S_CharacterConfig(pc.getId(), 1), true);
				} else {
					pc.sendPackets(new S_CharacterConfig(pc.getId()), true);
				}

			}

			pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.getbase_Er()), true);
			// 0000: 76 84 03 v..

			pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_Er()), true);
			// 0000: 76 84 0b

			HashMap<Integer, Integer> mbq = MonsterBookTable.getInstace().getMonQuest(pc.getId());
			if (mbq != null) {
				pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_BOOK, mbq));
			} else {
				pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_BOOK, null));
			}

			HashMap<Integer, Integer> mbl = MonsterBookTable.getInstace().getMonBookList(pc.getId());
			if (mbl != null) {
				pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_LOAD, mbl));
			} else {
				pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_LOAD, null));
			}

			북(pc);

			items(pc);

			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, pc.getRingSlotLevel()));
			if (pc.getEarringSlotLevel() > 0) {
				pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 16));
			} else {
				pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT_CLOSE, S_ReturnedStat.SUBTYPE_RING, 16));
			}

			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RUNE, 1));

			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.Unknown_LOGIN2, 0, 0), true);
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.LOGIN_EQUIP));

			if (pc.getEmblem_Slot() == 1) {
				pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 128));
			}
			if (pc.getShoulder_Slot() == 1) {
				pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 64));
			}

			for (L1ItemInstance item : pc.getInventory().getItems()) {

				if (item.isEquipped()) {
					if (item.getItem().getType() == 19) {
						pc.sendPackets(new S_SabuBox(S_SabuBox.아이템장착슬롯관리, item.getId(), 29, true));
					} else if (item.getItem().getType() == 20) {
						pc.sendPackets(new S_SabuBox(S_SabuBox.아이템장착슬롯관리, item.getId(), 30, true));
					} else if (item.getItem().getType() == 21) {
						pc.sendPackets(new S_SabuBox(S_SabuBox.아이템장착슬롯관리, item.getId(), 31, true));
					}
				}
			}

			// 현재 피 디비에서 불러오기
			pc.setCurrentHp(pc.get_loadHp());
			pc.setCurrentMp(pc.get_loadMp());

			pc.sendPackets(new S_PacketBox(S_PacketBox.WORLDMAP_UNKNOWN1), true);

			bookmarks(pc);
			pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.BOOKMARK), true);

			skills(pc);
			/** 2016.11.26 MJ 앱센터 LFC **/
			MJInstanceSpace.getInstance().getBackPc(pc);
			/** 2016.11.26 MJ 앱센터 LFC **/

			if (!client.getInterServer()) {
				// restart처가 getback_restart 테이블로 지정되고 있으면(자) 이동시킨다
				GetBackRestartTable gbrTable = GetBackRestartTable.getInstance();
				L1GetBackRestart[] gbrList = gbrTable.getGetBackRestartTableList();
				if (gbrList != null) {
					for (L1GetBackRestart gbr : gbrList) {
						if (pc.getMapId() == gbr.getArea()) {
							if (pc.getMapId() == 248 || pc.getMapId() == 249 || pc.getMapId() == 250 || pc.getMapId() == 251) {
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == 4) {
										break;
									}
								}
								pc.setX(33429);
								pc.setY(32814);
								pc.setMap((short) 4);
							}
							if (pc.getMapId() == 240 || pc.getMapId() == 241 || pc.getMapId() == 242 || pc.getMapId() == 243) {
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == 1) {
										break;
									}
								}
								pc.setX(33429);
								pc.setY(32814);
								pc.setMap((short) 4);
							}
							if (pc.getMapId() == 15) {
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == 1) {
										break;
									}
								}
							} else if (pc.getMapId() == 29) {
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == 3) {
										break;
									}
								}
							} else if (pc.getMapId() == 260) {
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == 2) {
										break;
									}
								}
							} else if (pc.getMapId() == 52) {
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == 4) {
										break;
									}
								}
							} else if (pc.getMapId() == 64) {
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == 5) {
										break;
									}
								}
							} else if (pc.getMapId() == 300) {
								L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == 7) {
										break;
									}
								}
							}
							pc.setX(gbr.getLocX());
							pc.setY(gbr.getLocY());
							pc.setMap(gbr.getMapId());
							break;
						}
					}
				}

				gbrList = null;

				if ((pc.getMapId() >= 271 && pc.getMapId() <= 278) || (pc.getMapId() >= 2100 && pc.getMapId() <= 2151)) {
					pc.setX(34060);
					pc.setY(32282);
					pc.setMap((short) 4);
				}

				if (pc.getMapId() == 4 && ((pc.getX() >= 33332 && pc.getX() <= 33338 && pc.getY() >= 32433 && pc.getY() <= 32439)
						|| (pc.getX() >= 33259 && pc.getX() <= 33265 && pc.getY() >= 32399 && pc.getY() <= 32405)
						|| (pc.getX() >= 33389 && pc.getX() <= 33395 && pc.getY() >= 32341 && pc.getY() <= 32347))) {
					L1Location newLocation = pc.getLocation().randomLocation(30, true);
					pc.setX(newLocation.getX());
					pc.setY(newLocation.getY());
				}

				if (pc.getMapId() == 814) {
					pc.setX(33616);
					pc.setY(33244);
					pc.setMap((short) 4);
				}

				if (pc.getMapId() == 2010 || (pc.getMapId() >= 2221 && pc.getMapId() <= 2235)) {
					pc.setX(33085);
					pc.setY(33391);
					pc.setMap((short) 4);
				}
				if (pc.getMapId() >= 2600 && pc.getMapId() <= 2699) {
					pc.setX(33702);
					pc.setY(32502);
					pc.setMap((short) 4);
				}
				if (pc.getMapId() >= 1400 && pc.getMapId() <= 1499) {
					pc.setX(33489);
					pc.setY(32764);
					pc.setMap((short) 4);
				}
				if (pc.getMapId() >= 1911 && pc.getMapId() <= 1913) {
					pc.setX(32768);
					pc.setY(32835);
					pc.setMap((short) 622);
				}
				if (pc.getMapId() >= 2301 && pc.getMapId() <= 2350) {
					pc.setX(33438);
					pc.setY(32799);
					pc.setMap((short) 4);
				}
				if (pc.getMapId() >= 1936 && pc.getMapId() <= 1940) {
					pc.setX(33438);
					pc.setY(32799);
					pc.setMap((short) 4);
				}

				L1Map map = L1WorldMap.getInstance().getMap(pc.getMapId());
				// altsettings.properties로 GetBack가 true라면 거리에 이동시킨다
				int tile = map.getTile(pc.getX(), pc.getY());
				if (Config.GET_BACK || !map.isInMap(pc.getX(), pc.getY()) || tile == 0 || tile == 4 || tile == 12) {
					int[] loc = Getback.GetBack_Location(pc, true);
					try {
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
					} catch (Exception e) {
						System.out.println("캐릭터 Get_Back Error: char=" + charName + " X: " + pc.getX() + " Y: " + pc.getY() + " Tile: " + tile);
						_log.info("캐릭터 Get_Back Error: char=" + charName + " X: " + pc.getX() + " Y: " + pc.getY() + " Tile: " + tile);
						e.printStackTrace();
					}
				}

				if (pc.getMapId() == 101) {// 오만의 탑 귀환 설정일경우 1층으로 세팅되어있음
					int rnd = _Random.nextInt(omanLocX.length);
					pc.setX(omanLocX[rnd]);
					pc.setY(omanLocY[rnd]);
					pc.setMap((short) 101);
				}
//				/**말하는 섬으로 복귀*/
//				if (pc.getMapId() == 9) {
//					pc.setX(32581);
//					pc.setY(32933);
//					pc.setMap((short) 0);
//				}
//				/**붉은 기사단의 훈련소로 복귀*/
//				if (pc.getMapId() == 7 || pc.getMapId() == 12146 || pc.getMapId() == 12147 || pc.getMapId() == 8) {
//					pc.setX(32680);
//					pc.setY(32843);
//					pc.setMap((short) 3);
//				}

				if (pc.getMapId() == 410) {
					pc.setX(32929);
					pc.setY(32995);
					pc.setMap((short) 410);
				}

				if (pc.getMapId() == 5153 || pc.getMapId() == 5125 || pc.getMapId() == 5140 || pc.getMapId() == 5143) {
					pc.setX(33429);
					pc.setY(32814);
					pc.setMap((short) 4);
				}

				if ((pc.getMapId() >= 1005 && pc.getMapId() <= 1010) || (pc.getMapId() >= 1011 && pc.getMapId() <= 1016)
						|| (pc.getMapId() >= 10000 && pc.getMapId() <= 10005)) {
					pc.setX(33719);
					pc.setY(32506);
					pc.setMap((short) 4);
				}

				if (pc.getMapId() >= 9101 && pc.getMapId() <= 9199) {
					pc.setX(33442);
					pc.setY(32799);
					pc.setMap((short) 4);
				}

				if (pc.getMapId() >= 653 && pc.getMapId() <= 656) { // 수상한감옥 -> 수상한 하늘정원
					pc.setX(32764);
					pc.setY(32836);
					pc.setMap((short) 622);
				}
				if (pc.getMapId() == 785 || pc.getMapId() == 788 || pc.getMapId() == 789) {
					pc.setX(33442);
					pc.setY(32799);
					pc.setMap((short) 4);
				}
				if (pc.getReturnStat() == 0 && pc.getMapId() == 5166) {
					pc.setX(32612);
					pc.setY(32734);
					pc.setMap((short) 4);
				}
				if (!pc.isGm() && (pc.getMapId() == 610 || pc.getMapId() == 612 || pc.getMapId() == 5554)) {
					pc.setX(33442);
					pc.setY(32799);
					pc.setMap((short) 4);
				}

				if ((pc.getMapId() == 420)) {
					pc.setX(32693);
					pc.setY(32800);
					pc.setMap((short) 450);
				}

				if (!pc.isGm() && (pc.getMapId() == 430)) {
					pc.setX(33442);
					pc.setY(32799);
					pc.setMap((short) 4);
				}
				if ((pc.getMapId() >= 9103 && pc.getMapId() <= 9200)) {
					pc.setX(32573);
					pc.setY(32940);
					pc.setMap((short) 0);
				}
			}

			if (!pc.isGm() && Config.LEVEL_DOWN_RANGE != 0) {
				if (pc.getHighLevel() - pc.getLevel() >= Config.LEVEL_DOWN_RANGE) {
					pc.sendPackets(new S_ServerMessage(64), true);
					pc.sendPackets(new S_Disconnect(), true);
					_log.info(String.format("[%s]: 렙따 범위를 넘었기 때문에 강제 절단 했습니다.", pc.getName()));
				}
			}

			// 전쟁중의 기내에 있었을 경우, 성주 혈맹이 아닌 경우는 귀환시킨다.
			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			if (pc.getMapId() == 66) {
				castle_id = 6;
			}
			if (0 < castle_id) {
				if (WarTimeController.getInstance().isNowWar(castle_id)) {
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					if (clan != null && clan.getCastleId() != castle_id) {
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
						loc = null;
					} else if (pc.getMapId() == 4) {
						int[] loc = new int[3];
						loc = L1CastleLocation.getGetBackLoc(castle_id);
						pc.setX(loc[0]);
						pc.setY(loc[1]);
						pc.setMap((short) loc[2]);
						loc = null;
					}
				}
			}

			L1World.getInstance().addVisibleObject(pc);

			pc.beginGameTimeCarrier();

			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_MapID(pc.getMapId(), pc.getMap().isUnderwater()), true);
			pc.sendPackets(new S_OwnCharPack(pc), true);

			// 적용중인 버프 리스트 설정
			buff(client, pc);

			pc.sendPackets(new S_Weather(L1World.getInstance().getWeather()), true);

			// 본섭은 위 아래 패킷 사이에 S_OPCODE_CASTLEMASTER 패킷이 온다 (총 9개)
			pc.sendCastleMaster();

			pc.sendPackets(new S_OwnCharStatus2(pc), true);
			pc.sendPackets(new S_SPMR(pc), true);

			pc.sendPackets(new S_ReturnedStat(pc, 4), true);

			pc.sendVisualEffectAtLogin(); // 독, 수중 등의 시각 효과를 표시

			pc.sendPackets(new S_PacketBox(pc, S_PacketBox.KARMA), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.INIT_DG, 0x00));
			pc.sendPackets(new S_PacketBox(S_PacketBox.UPDATE_DG, pc.getDg()));

			pc.sendPackets(new S_SurvivalCry(6000), true);

			pc.getLight().turnOnOffLight();

			// 존재버그 관련 추가
			L1PcInstance jonje = L1World.getInstance().getPlayer(pc.getName());
			if (jonje == null) {
				S_SystemMessage sm = new S_SystemMessage("존재버그 강제종료! 재접속하세요");
				pc.sendPackets(sm, true);
				client.kick();
				return;
			}

			if (pc.getGfxId().getTempCharGfx() == 11326 || pc.getGfxId().getTempCharGfx() == 11427 || pc.getGfxId().getTempCharGfx() == 10047
					|| pc.getGfxId().getTempCharGfx() == 9688 || pc.getGfxId().getTempCharGfx() == 11322 || pc.getGfxId().getTempCharGfx() == 10069
					|| pc.getGfxId().getTempCharGfx() == 10034 || pc.getGfxId().getTempCharGfx() == 10032) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
				L1PolyMorph.undoPoly(pc);
			}

			if (pc.getCurrentHp() > 0) {
				pc.setDead(false);
				pc.setActionStatus(0);
			} else {
				pc.setDead(true);
				pc.setActionStatus(ActionCodes.ACTION_Die);
			}

			if (pc.getLevel() >= 51 && pc.getLevel() - 50 > pc.getAbility().getBonusAbility() && pc.getAbility().getAmount() < 150) {
				int temp = (pc.getLevel() - 50) - pc.getAbility().getBonusAbility();
				pc.sendPackets(new S_bonusstats(pc.getId(), temp), true);
			}

			if (pc.getReturnStat() != 0) {
				SpecialEventHandler.getInstance().ReturnStats(pc);
			}

			pc.sendPackets(new S_PacketBox(S_PacketBox.LOGIN_UNKNOWN3));

			pc.sendPackets(new S_FairlyConfig(pc));

			pc.sendPackets(new S_PacketBox(86, 0), true);// 모름1
			pc.sendPackets(new S_PacketBox(86, 1), true);// 모름2

			pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_Er()), true);

			hasadbuff(pc);

			pc.사망패널티(true);

			pc.sendPackets(new S_NewCreateItem(0x01e3, 1, pc.getAbility().getTotalStr(), pc.getAbility().getTotalCon(), "힘", pc.getType(), pc));
			pc.sendPackets(new S_NewCreateItem(0x01e3, 1, pc.getAbility().getTotalDex(), 0, "덱", pc.getType(), pc));
			pc.sendPackets(new S_NewCreateItem(0x01e3, 1, pc.getAbility().getTotalCon(), pc.getAbility().getTotalStr(), "콘", pc.getType(), pc));
			pc.sendPackets(new S_NewCreateItem(0x01e3, 1, pc.getAbility().getTotalInt(), 0, "인트", pc.getType(), pc));
			pc.sendPackets(new S_NewCreateItem(0x01e3, 1, pc.getAbility().getTotalWis(), 0, "위즈", pc.getType(), pc));
			pc.sendPackets(new S_NewCreateItem(0x01e3, 1, pc.getAbility().getTotalCha(), 0, "카리", pc.getType(), pc));
			pc.sendPackets(new S_NewCreateItem("무게", pc));

			pc.sendPackets(new S_NewCreateItem(0x01e7, "툴팁1", pc));
			pc.sendPackets(new S_NewCreateItem(0x01e7, "툴팁2", pc));
			pc.sendPackets(new S_NewCreateItem(0x01e7, "툴팁3", pc));

			pc.sendPackets(new S_NewCreateItem(0x01ea, "스탯툴팁", pc));

			pc.sendPackets(new S_NewCreateItem(0x01e9, "08 00 c9 c9"));// 변신이벤트?

			pc.sendPackets(new S_NewCreateItem(0x7E, "08 00 10 01 63 d7"));// 변신이벤트?

			pc.sendPackets(new S_PacketBox(S_PacketBox.ACTION_GUIDE_2));

			UserRankingController.getInstance().setBuffSetting(pc);

			serchSummon(pc);

			WarTimeController.getInstance().checkCastleWar(pc);

			if (pc.getClanid() != 0) { // 크란 소속중

				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					if (pc.getClanid() == clan.getClanId() && pc.getClanname().toLowerCase().equals(clan.getClanName().toLowerCase())) {
						clan.addOnlineClanMember(pc.getName(), pc);
						S_ServerMessage sm = new S_ServerMessage(843, pc.getName());
						for (L1PcInstance clanMember : clan.getOnlineClanMember()) {
							if (clanMember.getId() != pc.getId()) {
								clanMember.sendPackets(sm);
							}
						}
						sm = null;
						// 전전쟁 리스트를 취득
						for (L1War war : L1World.getInstance().getWarList()) {
							boolean ret = war.CheckClanInWar(pc.getClanname());
							if (ret) { // 전쟁에 참가중
								String enemy_clan_name = war.GetEnemyClanName(pc.getClanname());
								if (enemy_clan_name != null) {
									// 당신의 혈맹이 현재_혈맹과 교전중입니다.
									pc.sendPackets(new S_War(8, pc.getClanname(), enemy_clan_name), true);
								}
								break;
							}
						}
					} else {
						pc.setClanid(0);
						pc.setClanname("");
						pc.setClanRank(0);
						pc.save(); // DB에 캐릭터 정보를 기입한다
					}
				}
			}

			if (pc.getPartnerId() != 0) { // 결혼중
				L1PcInstance partner = (L1PcInstance) L1World.getInstance().findObject(pc.getPartnerId());
				if (partner != null && partner.getPartnerId() != 0) {
					if (pc.getPartnerId() == partner.getId() && partner.getPartnerId() == pc.getId()) {
						pc.sendPackets(new S_ServerMessage(548), true); // 당신의
																		// 파트너는
																		// 지금
						// 게임중입니다.
						partner.sendPackets(new S_ServerMessage(549), true); // 당신의
																				// 파트너는
						// 방금
						// 로그인했습니다.
					}
				}
			}

			if (currentHpAtLoad > pc.getCurrentHp()) {
				pc.setCurrentHp(currentHpAtLoad);
			}
			if (currentMpAtLoad > pc.getCurrentMp()) {
				pc.setCurrentMp(currentMpAtLoad);
			}

			pc.startObjectAutoUpdate();
			client.CharReStart(false);
			pc.beginExpMonitor();

			pc.tempx = pc.getX();
			pc.tempy = pc.getY();
			pc.tempm = pc.getMapId();
			pc.temph = pc.getMoveState().getHeading();
			long sysTime = System.currentTimeMillis();

			// System.out.println(pc.getDETime2());
			if (pc.getDETime2() != null) {
				if (sysTime <= pc.getDETime2().getTime()) {
					long DETIME = pc.getDETime2().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGON_EME_1);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.DRAGON_EME_1, (int) DETIME);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_EME, 0x01, (int) DETIME / 1000), true);
				}
			}
			// System.out.println(pc.getDETime());
			if (pc.getDETime() != null) {
				if (sysTime <= pc.getDETime().getTime()) {
					long DETIME = pc.getDETime().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGON_EME_2);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.DRAGON_EME_2, (int) DETIME);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGON_EME, 0x02, (int) DETIME / 1000), true);
				}
			}
			if (pc.getPUPLETime() != null) {
				if (sysTime <= pc.getPUPLETime().getTime()) {
					long DETIME = pc.getPUPLETime().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGON_PUPLE);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.DRAGON_PUPLE, (int) DETIME);
					pc.sendPackets(new S_PacketBox((int) DETIME / 1000, 1, true, true), true);
				}
			}

			if (pc.getTOPAZTime() != null) {
				if (sysTime <= pc.getTOPAZTime().getTime()) {
					long DETIME = pc.getTOPAZTime().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGON_TOPAZ);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.DRAGON_TOPAZ, (int) DETIME);
					pc.sendPackets(new S_PacketBox((int) DETIME / 1000, 2, true, true), true);
				}
			}
			
			if (pc.get_halpas_armor_faith_delay() != null) {
				if (sysTime <= pc.get_halpas_armor_faith_delay().getTime()) {
					long DETIME = pc.get_halpas_armor_faith_delay().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.HALPAS_FAITH_DELAY);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.HALPAS_FAITH_DELAY, (int) DETIME);
					pc.sendPackets(new S_MaanIcons(L1SkillId.HALPAS_FAITH_DELAY, true, DETIME / 1000), true);
					
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.HALPAS_FAITH_DELAY)) {
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.HALPAS_FAITH_STANDBY);
					pc.sendPackets(new S_MaanIcons(L1SkillId.HALPAS_FAITH_STANDBY, false, 0));
					}
				}
			}

			int tamcount = pc.tamcount();
			if (tamcount > 0) {
				long tamtime = pc.TamTime();

				int aftertamtime = (int) tamtime;

				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, tamtime, tamcount, true), true);

				if (aftertamtime < 0) {
					aftertamtime = 0;
				}

				if (tamcount == 1) {
					pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit1, aftertamtime);
					pc.getAC().addAc(-1);
				} else if (tamcount == 2) {
					pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit2, aftertamtime);
					pc.getAC().addAc(-2);
				} else if (tamcount == 3) {
					pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit3, aftertamtime);
					pc.getAC().addAc(-3);
				} else if (tamcount == 4) { // 메티즈 탐
					pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit4, aftertamtime);
					pc.getAC().addAc(-4);
				} else if (tamcount == 5) {
					pc.getSkillEffectTimerSet().setSkillEffect(Tam_Fruit5, aftertamtime);
					pc.getAC().addAc(-5);
				}

				pc.sendPackets(new S_OwnCharStatus(pc));
			}

			if (pc.getNetConnection().getAccount().getBuff_HPMP() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_HPMP().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_HPMP().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_활력);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_활력, (int) bufftime);
					pc.sendPackets(new S_NewUI("활력", (long) bufftime), true);
					pc.addMaxHp(50);
					pc.addMaxMp(50);
					pc.addWeightReduction(3);
					pc.sendPackets(new S_HPUpdate(pc));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_DMG() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_DMG().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_DMG().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_공격);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_공격, (int) bufftime);
					pc.sendPackets(new S_NewUI("공격", (long) bufftime), true);
					pc.addDmgup(1);
					pc.addBowDmgup(1);
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_REDUC() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_REDUC().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_REDUC().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_방어);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_방어, (int) bufftime);
					pc.sendPackets(new S_NewUI("방어", (long) bufftime), true);
					pc.addDamageReductionByArmor(1);
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_MAGIC() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_MAGIC().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_MAGIC().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_마법);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_마법, (int) bufftime);
					pc.sendPackets(new S_NewUI("마법", (long) bufftime), true);
					pc.getAbility().addSp(1);
					pc.sendPackets(new S_SPMR(pc));
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_STUN() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_STUN().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_STUN().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_스턴);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_스턴, (int) bufftime);
					pc.sendPackets(new S_NewUI("스턴", (long) bufftime), true);
					pc.addTechniqueTolerance(2);
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_STR() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_STR().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_STR().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_완력);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_완력, (int) bufftime);
					pc.sendPackets(new S_NewUI("완력", (long) bufftime), true);
					pc.getAbility().addAddedStr((byte) 1);
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_DEX() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_DEX().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_DEX().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_민첩);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_민첩, (int) bufftime);
					pc.sendPackets(new S_NewUI("민첩", (long) bufftime), true);
					pc.getAbility().addAddedDex((byte) 1);
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_INT() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_INT().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_INT().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_지식);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_지식, (int) bufftime);
					pc.sendPackets(new S_NewUI("지식", (long) bufftime), true);
					pc.getAbility().addAddedInt((byte) 1);
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_WIS() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_WIS().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_WIS().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_지혜);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_지혜, (int) bufftime);
					pc.sendPackets(new S_NewUI("지혜", (long) bufftime), true);
					pc.getAbility().addAddedWis((byte) 1);
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_HOLD() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_HOLD().getTime()) {
					long bufftime = pc.getNetConnection().getAccount().getBuff_HOLD().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.강화버프_홀드);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.강화버프_홀드, (int) bufftime);
					pc.sendPackets(new S_NewUI("홀드", (long) bufftime), true);
				}
			}

			if (pc.getNetConnection().getAccount().getBuff_PC방() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getBuff_PC방().getTime()) {
					long 피씨타임 = pc.getNetConnection().getAccount().getBuff_PC방().getTime() - sysTime;

					TimeZone seoul = TimeZone.getTimeZone("UTC");
					Calendar calendar = Calendar.getInstance(seoul);
					calendar.setTimeInMillis(피씨타임);
					int d = calendar.get(Calendar.DATE) - 1;
					int h = calendar.get(Calendar.HOUR_OF_DAY);
					int m = calendar.get(Calendar.MINUTE);
					int sc = calendar.get(Calendar.SECOND);

					if (d > 0) {
						pc.sendPackets(new S_SystemMessage("[PC방 이용 시간] " + d + "일 " + h + "시간 " + m + "분 " + sc + "초 남았습니다."));
					} else if (h > 0) {
						pc.sendPackets(new S_SystemMessage("[PC방 이용 시간] " + h + "시간 " + m + "분 " + sc + "초 남았습니다."));
					} else if (m > 0) {
						pc.sendPackets(new S_SystemMessage("[PC방 이용 시간] " + m + "분 " + sc + "초 남았습니다."));
					} else {
						pc.sendPackets(new S_SystemMessage("[PC방 이용 시간] " + sc + "초 남았습니다."));
					}
					PC버프(pc);
				}
			}

			if (pc.PC방_버프) {
				String s = "08 01 10 01 f1 d5";// 피씨방..
				// pc.sendPackets(new S_NewCreateItem(s ));
				pc.sendPackets(new S_NewCreateItem(126, s));
			} else {
				String s = "08 00 10 01 e7 6d";// 피씨방..
				// pc.sendPackets(new S_NewCreateItem(s ));
				pc.sendPackets(new S_NewCreateItem(126, s));
			}

			getAttendanceCheck(account, pc);

			if (pc.getNetConnection().getAccount().getDragonRaid() != null) {
				if (sysTime <= pc.getNetConnection().getAccount().getDragonRaid().getTime()) {
					long BloodTime = pc.getNetConnection().getAccount().getDragonRaid().getTime() - sysTime;
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGONRAID_BUFF);
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.DRAGONRAID_BUFF, (int) BloodTime);
					pc.sendPackets(new S_PacketBox(S_PacketBox.드래곤레이드버프, (int) BloodTime / 1000), true);
				}
			}

			try {
				pc.save(); // DB에 캐릭터 정보를 기입한다
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (pc.getMoveState().getHeading() < 0 || pc.getMoveState().getHeading() > 7) {
				pc.getMoveState().setHeading(0);
			}

			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.Elixir, pc.getAbility().getElixirCount()));
			if (pc.getEarringSlotLevel() != 0) {
				pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, 16));
			}
			pc.sendPackets(new S_ReturnedStat(S_ReturnedStat.RING_RUNE_SLOT, S_ReturnedStat.SUBTYPE_RING, pc.getRingSlotLevel()));

			huntoption(pc); // 수배효과
			bapo(pc);
			UserMonsterBookLoader.load(pc);
			/* pc.getMonsterBook().sendList(); */
			UserWeekQuestLoader.load(pc);
			// pc.sendPackets(new S_EventNotice(tamcount, tamcount, tamcount, false)); //
			// 이벤트알람

			/** 니팩에 마쳐서 안해준다 니가 보고 바꿔라 ㅋㅋ */
			pc.sendPackets(new S_EventNotice(), true);
			pc.sendPackets(new S_EventNotice(pc, false, false), true);
			/** 클라우디아 helped by GHost */

			/** 복수 리스트 로드 */
			RevengeLoad(pc);
			/** 복수 리스트 로드 */
//			if (client.getletteron()) {
//				/** 편지 리스트 로드 */
//				LetterList(pc, 0, 40);
//				LetterList(pc, 1, 80);
//				LetterList(pc, 2, 10);
//				/** 편지 리스트 로드 */
//			}
//			client.setletteron(false);

			if (pc.getLevel() <= Config.클라우디아레벨) {
				CharacterQuestTable.getInstance().LoginQuestInfo(pc); // 퀘스트 정보로딩
				pc.sendPackets(new S_QuestTalkIsland(pc));
				pc.sendPackets(new S_QuestTalkIsland(14, 257));
			}

			if (pc.getHellTime() > 0) {
				pc.beginHell(false);
			}

			if (pc.getHuntCount() == 1) {
				pc.initBeWanted();
				int[] beWanted = { 1, 1, 1, 1, 1, 1 };
				pc.setBeWanted(beWanted);
				pc.addBeWanted();
			}
			if (pc.getHuntCount() == 2) {
				pc.initBeWanted();
				int[] beWanted = { 2, 2, 2, 2, 2, 2 };
				pc.setBeWanted(beWanted);
				pc.addBeWanted();
			}
			if (pc.getHuntCount() == 3) {
				pc.initBeWanted();
				int[] beWanted = { 5, 5, 5, 5, 5, 5 };
				pc.setBeWanted(beWanted);
				pc.addBeWanted();
			}
			securityBuff(pc);
			checkBattleZone(pc);

			pc.sendPackets(new S_OwnCharStatus(pc), true);
			if (pc.getClanid() != 0) { // 크란 소속중
				pc.sendPackets(new S_ClanWindow(S_ClanWindow.혈마크띄우기, pc.getClan().getmarkon()), true);
				pc.sendPackets(new S_문장주시(pc.getClan(), 2), true);
				pc.sendPackets(new S_NewUI(0x19, pc.getClan().getClanName(), pc), true);

			}

			/** 배틀존 **/
			if (pc.getMapId() == 5153) {
				if (!BattleZone.getInstance().getDuelOpen()) {
					if (pc.get_DuelLine() != 0) {
						pc.set_DuelLine(0);
					}
					L1Teleport.teleport(pc, 33090, 33402, (short) 4, 0, true);
				} else {
					if (pc.get_DuelLine() == 0) {
						L1Teleport.teleport(pc, 33090, 33402, (short) 4, 0, true);
					}
				}
			} else {
				if (pc.get_DuelLine() != 0) {
					pc.set_DuelLine(0);
				}
			}

			if (pc.getReturnStat() != 0) {
				L1SkillUse l1skilluse = new L1SkillUse();
				l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);
				if (pc.getSecondWeapon() != null) {
					pc.getInventory().setEquipped(pc.getSecondWeapon(), false, false, false, true);
				}
				if (pc.getWeapon() != null) {
					pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
				}

				pc.sendPackets(new S_CharVisualUpdate(pc));
				// pc.sendPackets(new S_OwnCharStatus2(pc));
				pc.sendPackets(new S_OwnCharStatus(pc));

				for (L1ItemInstance armor : pc.getInventory().getItems()) {
					for (int type = 0; type <= 12; type++) {
						if (armor != null) {
							pc.getInventory().setEquipped(armor, false, false, false);
						}
					}
				}
				pc.sendPackets(new S_SPMR(pc));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
				// pc.sendPackets(new S_OwnCharStatus2(pc));
				pc.sendPackets(new S_OwnCharStatus(pc));
				pc.setReturnStatus(1);
				pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
			} else if (!pc.LoadCheckStatus()) {
				if (!CheckInitStat.CheckPcStat(pc)) {
					pc.setReturnStat(pc.getExp());
					pc.setReturnStatus(1);
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
					return;
				}
			}
			if (pc.getGfxId().getTempCharGfx() == 11120 || pc.getGfxId().getTempCharGfx() == 11121 || pc.getGfxId().getTempCharGfx() == 11122
					|| pc.getGfxId().getTempCharGfx() == 11123 || pc.getGfxId().getTempCharGfx() == 11124 || pc.getGfxId().getTempCharGfx() == 11125
					|| pc.getGfxId().getTempCharGfx() == 11126 || pc.getGfxId().getTempCharGfx() == 11127) {
				pc.getInventory().takeoffEquip(pc.getGfxId().getTempCharGfx());
			}

			if (LetterTable.getInstance().CheckNoReadLetter(pc.getName())) {
				S_SkillSound ss = new S_SkillSound(pc.getId(), 1091);
				pc.sendPackets(ss, true);
				S_ServerMessage sm = new S_ServerMessage(428);
				pc.sendPackets(sm, true);
			}

			int currentTime = (int) (System.currentTimeMillis() / 1000);
			if (pc.getSealScrollTime() > 0) {
				if (pc.getSealScrollTime() < currentTime) {
					pc.sendPackets(new S_SystemMessage("봉인해제주문서 [" + pc.getSealScrollCount() + "]장이 지급되었습니다."));
					pc.getInventory().storeItem(50021, pc.getSealScrollCount());
					pc.setSealScrollTime(0);
					pc.setSealScrollCount(0);
					pc.save();
				} else {
					int remainMin = (pc.getSealScrollTime() - currentTime) / 60 + 1;
					int remainHour = remainMin / 60;
					remainMin -= remainHour * 60;
					int remainDay = remainHour / 24;
					remainHour -= remainDay * 24;
					pc.sendPackets(new S_SystemMessage("해제주문서 발급일은[" + remainDay + "일] [" + remainHour + "시간] [" + remainMin + "분] 남았습니다."));
				}
			}

			ExcludeTable.getInstance().load(pc);
			ExcludeLetterTable.getInstance().load(pc);

			if (pc.isGm()) {
				L1EvaSystem eva = EvaSystemTable.getInstance().getSystem(1);
				Timestamp ts = new Timestamp(eva.getEvaTime().getTimeInMillis());
				Date day = new Date(System.currentTimeMillis());
				if (ts.getMonth() == day.getMonth() && ts.getDate() == day.getDate()) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd HH:mm");
					String time = dateFormat.format(ts);
					String time2 = dateFormat.format(new Timestamp(eva.bossTime));
					String time3 = dateFormat.format(new Timestamp(eva.bosscheck));
					pc.sendPackets(new S_SystemMessage("균열: " + time + " 보스: " + time2 + " 종료: " + time3), true);
					dateFormat = null;
				}
				ts = null;
				day = null;
			}
			pc.setSurvivalCry(sysTime);

			pc.sendPackets(new S_SPMR(pc), true);

			// pc.sendPackets(new S_NewUI(0x13));

			// 12_09
			for (L1Object obj : L1World.getInstance().getObject()) {
				if (obj == null)
					continue;
				if (obj instanceof L1DollInstance) {
					L1DollInstance doll = (L1DollInstance) obj;
					L1Character cha = doll.getMaster();
					if (cha == null || ((L1PcInstance) cha).getNetConnection() == null || cha.isDead())
						doll.deleteDoll();
					else if (doll.getMaster().getName().equalsIgnoreCase(charName))
						doll.deleteDoll();
				}
			}

			/** 인스 던전 입장시에는 무조건 펫이랑 인형정보 삭제 한번 하도록 하자 */
			if (client.getInterServer()) {
				if (client.getInterServerParty() != null) {
					pc.setParty(client.getInterServerParty());
					pc.getParty().isMembers(pc);
					pc.set표식(client.getInterServerNotice());
					pc.sendPackets(new S_Party(0x68, pc), true);

					client.setInterServerParty(null);
					client.setInterServerNotice(0);
				} else if (client.getInterServerType() == 10) {
					pc.setDungeonInfoCheck(true);
				} else if (client.getInterServerType() == 99)
					client.setInterServer(false);
			}

			pc.encobjid = byteWrite(pc.getId());// 표식설정을위해

			InvSwapController.getInstance().toWorldJoin(pc);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85, 0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90, 0x91, 0x92,
			0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b, 0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6, 0xa7, 0xa8, 0xa9, 0xaa,
			0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1, 0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc, 0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2,
			0xc3, 0xc4, 0xc5, 0xc6, 0xc7, 0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2, 0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda,
			0xdb, 0xdc, 0xdd, 0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8, 0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2,
			0xf3, 0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe, 0xff };

	private String byteWrite(long value) {
		long temp = value / 128;
		StringBuffer sb = new StringBuffer();
		if (temp > 0) {
			sb.append((byte) hextable[(int) value % 128]);
			while (temp >= 128) {
				sb.append((byte) hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				sb.append((int) temp);
		} else {
			if (value == 0) {
				sb.append(0);
			} else {
				sb.append((byte) hextable[(int) value]);
				sb.append(0);
			}
		}
		return sb.toString();
	}

	private void PC버프(L1PcInstance pc) {

		pc.sendPackets(new S_NewSkillIcons(L1SkillId.PC방, true, -1));
		pc.PC방_버프 = true;
	}

	class earthCheck implements Runnable {

		private L1PcInstance pc = null;

		public earthCheck(L1PcInstance _pc) {
			pc = _pc;
		}

		@Override
		public void run() {

			try {
				if (pc == null || pc.getNetConnection() == null)
					return;
				if (pc.getMapId() != 6202) {
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				}
				if (!pc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)) {
					pc.getSkillEffectTimerSet().setSkillEffect(EARTH_BIND, 9999 * 1000);
					pc.sendPackets(new S_Poison(pc.getId(), 2), true);
					Broadcaster.broadcastPacket(pc, new S_Poison(pc.getId(), 2), true);
					pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true), true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			GeneralThreadPool.getInstance().schedule(this, 1000);
		}

	}

	private void securityBuff(L1PcInstance pc) {

		pc.sendPackets(new S_PacketBox(S_PacketBox.SECURITY_BUFF), true);
		pc.getAC().addAc(-1);
		pc.sendPackets(new S_OwnCharAttrDef(pc), true);
	}

	private void bapo(L1PcInstance pc) {
		int level = pc.getLevel();
		if (level <= 50) {
			pc.sendPackets(new S_PacketBox(S_PacketBox.BAPO, 6, true));
		}
		pc.setNBapoLevell(7);
	}

	/** 배틀존 **/
	private void checkBattleZone(L1PcInstance pc) {
		if (pc.getMapId() == 5153) {
			L1Teleport.teleport(pc, 33437, 32800, (short) 4, 4, false);
		}
	}

	/** 배틀존 **/
	private void hasadbuff(L1PcInstance pc) {
		try {

			int AinHasad = pc.getAinHasad();
			int temp = 0;

			if (pc.getLogOutTime() != null && AinHasad < 2000000) {
				temp = (int) ((System.currentTimeMillis() - pc.getLogOutTime().getTime()) / 900000);
				int sum = pc.getAinHasad() + (temp * 10000);
				if (sum >= 2000000) {
					pc.setAinHasad(2000000);
				} else {
					pc.setAinHasad(sum);
				}
			}

			if (AinHasad > 80000000) {
				AinHasad = 80000000;
			} else if (AinHasad < 0) {
				AinHasad = 0;
			}

			pc._dragonbless_1 = false;
			pc._dragonbless_2 = false;
			pc._dragonbless_3 = false;

			pc.setAinHasad(AinHasad);
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.AINHASAD, pc), true);
		} catch (Exception e) {
		}
	}

	private void RevengeLoad(L1PcInstance pc) {
		CharcterRevengeTable revenge = CharcterRevengeTable.getInstance();
		ArrayList<Integer> objid = revenge.GetRevengeObj(pc.getId());
		if (objid.size() >= 1) {
			pc.sendPackets(new S_Revenge(S_Revenge.Revenge_All_List, pc), true);
		}
	}

	private void items(L1PcInstance pc) {
		// DB로부터 캐릭터와 창고의 아이템을 읽어들인다
		CharacterTable.getInstance().restoreInventory(pc);

		pc.sendPackets(new S_InvList(pc));
	}

	private void bookmarks(L1PcInstance pc) {

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			// pstm =
			// con.prepareStatement("SELECT * FROM character_teleport WHERE
			// char_id=? ORDER BY name ASC");
			pstm = con.prepareStatement("SELECT * FROM character_teleport WHERE char_id=? ORDER BY id ASC");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			L1BookMark bookmark = null;
			while (rs.next()) {
				bookmark = new L1BookMark();
				bookmark.setId(rs.getInt("id"));
				bookmark.setCharId(rs.getInt("char_id"));
				bookmark.setName(rs.getString("name"));
				bookmark.setLocX(rs.getInt("locx"));
				bookmark.setLocY(rs.getInt("locy"));
				bookmark.setMapId(rs.getShort("mapid"));
				bookmark.setRandomX(rs.getShort("randomX"));
				bookmark.setRandomY(rs.getShort("randomY"));
				bookmark.set_fast(rs.getShort("fast"));
				// S_Bookmarks s_bookmarks = new S_Bookmarks(bookmark.getName(),
				// bookmark.getLocX(), bookmark.getLocY(), bookmark.getMapId(),
				// bookmark.getId());
				pc.addBookMark(bookmark);
				// pc.sendPackets(s_bookmarks);
				// s_bookmarks.clear(); s_bookmarks = null;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void skills(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_skills WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();

			int i = 0;
			boolean passive = false;
			boolean 크래쉬 = false;
			boolean 퓨리 = false;
			boolean 슬레이어 = false;
			boolean 아머가드 = false;
			boolean 타이탄락 = false;
			boolean 타이탄블릿 = false;
			boolean 타이탄매직 = false;
			boolean 데스페라도앱솔루트 = false;
			boolean 타이탄라이징 = false;
			L1Skills l1skills = null;
			while (rs.next()) {

				int skillId = rs.getInt("skill_id");
				try {
					l1skills = SkillsTable.getInstance().getTemplate(skillId);
					if (l1skills.getSkillLevel() == 99) {
						switch (l1skills.getId()) {
						case 1:
							크래쉬 = true;
							pc.isCrash = true;
							break;
						case 2:
							퓨리 = true;
							pc.isPurry = true;
							break;
						case 3:
							슬레이어 = true;
							pc.isSlayer = true;
							break;
						case 5:
							아머가드 = true;
							pc.isAmorGaurd = true;
							break;
						case 6:
							타이탄락 = true;
							pc.isTaitanR = true;
							break;
						case 7:
							타이탄블릿 = true;
							pc.isTaitanB = true;
							break;
						case 8:
							타이탄매직 = true;
							pc.isTaitanM = true;
							break;
						}
						passive = true;
					}
					if (l1skills != null && pc.isWarrior() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 515) {// 데페앱솔
						pc.sendPackets(new S_CreateItem(145, 13, 0));
						pc.데스페라도앱솔루트 = true;
					}
					if (l1skills != null && pc.isDarkelf() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 511) {// 다엘
						pc.sendPackets(new S_CreateItem(145, 12, 0));
						pc.더블브레이크데스티니 = true;
					}
					if (l1skills != null && pc.isDarkelf() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 510) {// 다엘
						pc.sendPackets(new S_CreateItem(145, 11, 0));
						pc.아머브레이크데스티니 = true;
					}
					if (l1skills != null && pc.isKnight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 512) {// 기사패시브
						pc.sendPackets(new S_CreateItem(145, 10, 0));
						pc.카운터배리어베테랑 = true;
					}
					if (l1skills != null && pc.isDragonknight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 513) {
						pc.sendPackets(new S_CreateItem(145, 14, 0));
						pc.썬더그랩브레이브 = true;
					}
					if (l1skills != null && pc.isDragonknight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 514) {
						pc.sendPackets(new S_CreateItem(145, 15, 0));
						pc.포우슬레이어브레이브 = true;
					}
					if (l1skills != null && pc.isDragonknight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 518) {
						pc.sendPackets(new S_CreateItem(145, 16, 0));
						pc.아우라키아 = true;
					}
					if (l1skills != null && pc.isIllusionist() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 516) {
						pc.sendPackets(new S_CreateItem(145, 17, 0));
						pc.다크호스 = true;
					}
					if (l1skills != null && pc.isDarkelf() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 517) {
						pc.sendPackets(new S_CreateItem(145, 18, 0));
						pc.파이널번 = true;
					}
					if (l1skills != null && pc.isElf() && l1skills.getSkillLevel() == 18 && l1skills.getSkillId() == 138) {
						pc.sendPackets(new S_CreateItem(145, 21, 0));
						pc.레지스트엘리멘트 = true;
						pc.getResistance().addMr(5);
						pc.getResistance().addAllNaturalResistance(5);
					}
					if (l1skills != null && pc.isElf() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 519) {
						pc.sendPackets(new S_CreateItem(145, 22, 0));
						pc.글로리어스 = true;
					}
					if (l1skills != null && pc.isElf() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 564) {
						pc.sendPackets(new S_CreateItem(145, 38, 0));
						pc.블러드투소울 = true;
						pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SPELL_PASSIVE_ONOFF_ACK, 38, false));
					}
					if (l1skills != null && pc.isDragonknight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 188) {
						pc.sendPackets(new S_CreateItem(145, 40, 0));
						pc.디스트로이피어 = true;
					}
					if (l1skills != null && pc.isDragonknight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 193) {
						pc.sendPackets(new S_CreateItem(145, 41, 0));
						pc.디스트로이호러 = true;
					}
					if (l1skills != null && pc.isCrown() && l1skills.getSkillLevel() == 16 && l1skills.getSkillId() == 121) {
						pc.sendPackets(new S_CreateItem(145, 42, 0));
						pc.브레이브아바타 = true;
					}
					if (l1skills != null && pc.isKnight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 568) {// 기사패시브
						pc.sendPackets(new S_CreateItem(145, 43, 0));
						pc.리덕션아머베테랑 = true;
					}
					if (l1skills != null && pc.isKnight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 569) {// 기사패시브
						pc.sendPackets(new S_CreateItem(145, 44, 0));
						pc.레이징포스 = true;
						if(pc.레이징포스 && pc.getLevel() >= 80) {
							int pvpre = pc.getLevel() - 77;
							i = (pvpre / 3) * 1;
							pc.addTechniqueHit(i);
						}
					}
					if (l1skills != null && pc.isDarkelf() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 566) {
						pc.sendPackets(new S_CreateItem(145, 47, 0));
						pc.버닝스피릿츠 = true;
					}
					if (l1skills != null && pc.isDarkelf() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 565) {
						pc.sendPackets(new S_CreateItem(145, 48, 0));
						pc.Add_Er(18);
						pc.드레스이베이젼 = true;
					}
					if (l1skills != null && pc.isDarkelf() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 567) {
						pc.sendPackets(new S_CreateItem(145, 49, 0));
						pc.루시퍼데스티니 = true;
					}
					if (l1skills != null && pc.isDragonknight() && l1skills.getSkillLevel() == 51 && l1skills.getSkillId() == 181) {
						pc.sendPackets(new S_CreateItem(145, 61, 0));
						pc.드래곤스킨 = true;
					}

					if (l1skills != null && pc.isFencer() && l1skills.getSkillLevel() == 55 && (l1skills.getSkillId() >= 550 && l1skills.getSkillId() <= 561)) {
						switch (l1skills.getSkillId()) {
						case 550:
							pc.infinity_A = true;
							break;
						case 551:
							pc.infinity_B = true;
							break;
						case 552:
							pc.infinity_D = true;
							break;
						case 553:
							pc.damascus = true;
							break;
						case 554:
							pc.paradox = true;
							break;
						case 555:
							pc.grous = true;
							break;
						case 556:
							pc.rage = true;
							break;
						case 557:
							pc.phantom_R = true;
							break;
						case 558:
							pc.phantom_D = true;
							break;
						case 559:
							pc.flame = true;
							break;
						case 560:
							pc.infinity_BL = true;
							break;
						case 561:
							pc.survive = true;
							break;
						}
						pc.sendPackets(new S_CreateItem(145, (l1skills.getSkillId() - 527), 0));
					} else if (l1skills != null && pc.isFencer() && l1skills.getSkillLevel() == 55 && l1skills.getSkillId() == 562) {
						pc.Pantera_S = true;
						pc.sendPackets(new S_CreateItem(145, 36, 0));
					}

					if (l1skills != null && l1skills.getSkillId() > 0) {
						pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew, pc, l1skills.getSkillId()), true);
					}

					if (pc.getElfAttr() != 0 && pc.isElf()) {
						int attr = pc.getElfAttr();
						switch (attr) {
						case 1:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 8), true);
							break;
						case 2:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
							break;
						case 4:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 2), true);
							break;
						case 8:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 5), true);
							break;
						case 21:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 8), true);
							break;
						case 24:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 2), true);
							break;
						case 28:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 4), true);
							break;
						case 41:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 1), true);
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 8), true);
							break;
						case 48:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 2), true);
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 4), true);
							break;
						case 81:
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 4), true);
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSpiritAttr, 8), true);
							break;
						}
					}

					// System.out.println(l1skills.getName() + l1skills.getSkillId());

					pc.setSkillMastery(skillId);
				} catch (Exception e) {
					// System.out.println(skillId);
				}
			}

			if (passive) {
				S_NewUI sn1 = new S_NewUI(크래쉬, 퓨리, 슬레이어, 아머가드, 타이탄락, 타이탄블릿, 타이탄매직);
				pc.sendPackets(sn1);
				sn1 = null;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void huntoption(L1PcInstance pc) { // 수배옵션
		if (pc.getHuntCount() == 1) {
			if (pc.isWizard() || pc.isIllusionist()) {
				if (pc.getHuntPrice() == 1000000) {
					pc.getAbility().addSp(1);
					pc.sendPackets(new S_SPMR(pc));
				}
			} else if (pc.isCrown() || pc.isKnight() || pc.isDarkelf() || pc.isDragonknight() || pc.isElf() || pc.isWarrior() || pc.isFencer()) {
				if (pc.getHuntPrice() == 1000000) {
					pc.addDmgup(1);
					pc.addBowDmgup(1);
					pc.addDamageReductionByArmor(1);
				}
			}
		}

		if (pc.getHuntCount() == 2) {
			if (pc.isWizard() || pc.isIllusionist()) {
				if (pc.getHuntPrice() == 2000000) {
					pc.getAbility().addSp(2);
					pc.sendPackets(new S_SPMR(pc));
				}
			} else if (pc.isCrown() || pc.isKnight() || pc.isDarkelf() || pc.isDragonknight() || pc.isElf() || pc.isWarrior() || pc.isFencer()) {
				if (pc.getHuntPrice() == 2000000) {
					pc.addDmgup(2);
					pc.addBowDmgup(2);
					pc.addDamageReductionByArmor(2);
				}
			}
		}
		if (pc.getHuntCount() == 3) {
			if (pc.isWizard() || pc.isIllusionist()) {
				if (pc.getHuntPrice() == 3000000) {
					pc.getAbility().addSp(5);
					pc.sendPackets(new S_SPMR(pc));
				}
			} else if (pc.isCrown() || pc.isKnight() || pc.isDarkelf() || pc.isDragonknight() || pc.isElf() || pc.isWarrior() || pc.isFencer()) {
				if (pc.getHuntPrice() == 3000000) {
					pc.addDmgup(5);
					pc.addBowDmgup(5);
					pc.addDamageReductionByArmor(5);
				}
			}
		}

	}

	private void serchSummon(L1PcInstance pc) {
		for (L1SummonInstance summon : L1World.getInstance().getAllSummons()) {
			if (summon.getMaster().getId() == pc.getId()) {
				summon.setMaster(pc);
				pc.addPet(summon);
				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(summon)) {
					S_SummonPack sp = new S_SummonPack(summon, visiblePc);
					visiblePc.sendPackets(sp, true);
				}
			}
		}
	}

	/**
	 * 적용중인 버프 리스트 설정
	 */
	private void buff(LineageClient clientthread, L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {

			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			rs = pstm.executeQuery();
			int icon[] = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

			while (rs.next()) {
				int skillid = rs.getInt("skill_id");
				int remaining_time = rs.getInt("remaining_time");
				if (skillid >= COOKING_1_0_N && skillid <= COOKING_1_6_N || skillid >= COOKING_1_8_N && skillid <= COOKING_1_14_N
						|| skillid >= COOKING_1_16_N && skillid <= COOKING_1_22_N || skillid >= COOKING_1_0_S && skillid <= COOKING_1_6_S
						|| skillid >= COOKING_1_8_S && skillid <= COOKING_1_14_S || skillid >= COOKING_1_16_S && skillid <= COOKING_1_22_S) { // 요리(디저트는 제외하다)
					L1Cooking.eatCooking(pc, skillid, remaining_time);
					continue;
				}

				if (skillid >= COOKING_NEW_한우 && skillid <= COOKING_NEW_닭고기) {
					L1Cooking.newEatCooking(pc, skillid, remaining_time);
					continue;
				}

				if (skillid >= COOKING_NEW_탐한우 && skillid <= COOKING_NEW_탐닭고기) {
					L1Cooking.newEatCooking(pc, skillid, remaining_time);
					continue;
				}

				if (skillid >= 싸이매콤한라면 && skillid <= L1SkillId.싸이시원한음료) {
					L1Cooking.newEatCooking(pc, skillid, remaining_time);
					continue;
				}

				if (skillid == 천하장사버프) {
					L1Cooking.newEatCooking(pc, skillid, remaining_time);
					continue;
				}

				switch (skillid) {
				case HEUKSA:
					pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.HEUKSA);
					pc.sendPackets(new S_PacketBox(S_PacketBox.UNLIMITED_ICON, 4914, remaining_time));
					break;

				case 999:// 드진 스킬아이디
					int stime = (remaining_time / 4) - 2;
					pc.sendPackets(new S_DRAGONPERL(pc.getId(), 8), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONPERL, 8, stime), true);
					pc.set진주속도(1);
					break;
				case L1SkillId.STATUS_WISDOM_POTION:
					icon[38] = (remaining_time + 8) / 16;
					break;

				case DECREASE_WEIGHT:
					icon[0] = remaining_time / 16;
					break;

				case WEAKNESS:// 위크니스 //
					icon[4] = remaining_time / 4;
					pc.addDmgup(-5);
					pc.addHitup(-1);
					break;

				case BERSERKERS:// 버서커스 //
					icon[7] = remaining_time / 4;
					pc.getAC().addAc(10);
					pc.addDmgup(2);
					pc.addHitup(8);
					break;

				/*
				 * case DISEASE:// 디지즈 // icon[5] = remaining_time / 4; pc.addDmgup(-6);
				 * pc.getAC().addAc(12); break;
				 */
				case SILENCE:
					icon[2] = remaining_time / 4;
					break;

				case SHAPE_CHANGE:
					int poly_id = rs.getInt("poly_id");
					if (poly_id >= 0) {
						L1PolyMorph.doPoly(pc, poly_id, remaining_time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
					} else {
						L1PolyMorph.doPoly(pc, poly_id * -1, remaining_time, L1PolyMorph.MORPH_BY_Domination);
					}
					continue;
				case DECAY_POTION:
					icon[1] = remaining_time / 4;
					break;

				case VENOM_RESIST:// 베놈 레지스트 //
					icon[3] = remaining_time / 4;
					break;

				case DRESS_EVASION:// 드레스 이베이젼 //
					icon[6] = remaining_time / 4;
					pc.Add_Er(18);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					break;

				case RESIST_MAGIC:// 레지스트 매직
					pc.getResistance().addMr(10);
					pc.sendPackets(new S_ElfIcon(remaining_time / 16, 0, 0, 0), true);
					break;

				case ELEMENTAL_FALL_DOWN:
					icon[12] = remaining_time / 4;
					int playerAttr = pc.getElfAttr();
					int i = -50;
					switch (playerAttr) {
					case 0:
						pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
						break;
					case 1:
						pc.getResistance().addEarth(i);
						pc.setAddAttrKind(1);
						break;
					case 2:
						pc.getResistance().addFire(i);
						pc.setAddAttrKind(2);
						break;
					case 4:
						pc.getResistance().addWater(i);
						pc.setAddAttrKind(4);
						break;
					case 8:
						pc.getResistance().addWind(i);
						pc.setAddAttrKind(8);
						break;
					case 21:
						pc.getResistance().addFire(i);
						pc.getResistance().addEarth(i);
						pc.setAddAttrKind(21);
						break;
					case 24:
						pc.getResistance().addFire(i);
						pc.getResistance().addWater(i);
						pc.setAddAttrKind(24);
						break;
					case 28:
						pc.getResistance().addFire(i);
						pc.getResistance().addWind(i);
						pc.setAddAttrKind(28);
						break;
					case 41:
						pc.getResistance().addWater(i);
						pc.getResistance().addEarth(i);
						pc.setAddAttrKind(41);
						break;
					case 48:
						pc.getResistance().addWater(i);
						pc.getResistance().addWind(i);
						pc.setAddAttrKind(48);
						break;
					case 81:
						pc.getResistance().addWind(i);
						pc.getResistance().addEarth(i);
						pc.setAddAttrKind(81);
						break;
					default:
						break;
					}
					break;

				case CLEAR_MIND:// 클리어 마인드
					/*
					 * pc.getAbility().addAddedWis((byte) 3); pc.resetBaseMr();
					 */
					pc.getAbility().addAddedInt((byte) 1);
					pc.getAbility().addAddedDex((byte) 1);
					pc.getAbility().addAddedStr((byte) 1);
					pc.sendPackets(new S_ElfIcon(0, remaining_time / 16, 0, 0), true);
					break;

				case RESIST_ELEMENTAL:// 레지스트 엘리멘탈
					pc.getResistance().addAllNaturalResistance(10);
					pc.sendPackets(new S_ElfIcon(0, 0, remaining_time / 16, 0), true);
					break;

				case ELEMENTAL_PROTECTION:// 프로텍션 프롬 엘리멘탈
					int attr = pc.getElfAttr();
					if (attr == 1) {
						pc.getResistance().addEarth(50);
					} else if (attr == 2) {
						pc.getResistance().addFire(50);
					} else if (attr == 4) {
						pc.getResistance().addWater(50);
					} else if (attr == 8) {
						pc.getResistance().addWind(50);
					} else if (attr == 21) {
						pc.getResistance().addFire(50);
						pc.getResistance().addEarth(50);
					} else if (attr == 24) {
						pc.getResistance().addFire(50);
						pc.getResistance().addWater(50);
					} else if (attr == 28) {
						pc.getResistance().addFire(50);
						pc.getResistance().addWind(50);
					} else if (attr == 41) {
						pc.getResistance().addWater(50);
						pc.getResistance().addEarth(50);
					} else if (attr == 48) {
						pc.getResistance().addWater(50);
						pc.getResistance().addWind(50);
					} else if (attr == 81) {
						pc.getResistance().addWind(50);
						pc.getResistance().addEarth(50);
					}
					pc.sendPackets(new S_ElfIcon(0, 0, 0, remaining_time / 16), true);
					break;

				case ERASE_MAGIC:
					icon[10] = remaining_time / 4;
					break;

				case NATURES_TOUCH:// 네이쳐스 터치 //
					icon[8] = remaining_time / 4;
					break;

				case WIND_SHACKLE:
					icon[9] = remaining_time / 4;
					break;

				case ELEMENTAL_FIRE:
					icon[13] = remaining_time / 4;
					break;

				case POLLUTE_WATER:// 폴루트 워터 //
					icon[16] = remaining_time / 4;
					break;

				case STRIKER_GALE:// 스트라이커 게일 //
					icon[14] = remaining_time / 4;
					break;

				case SOUL_OF_FLAME:// 소울 오브 프레임 //
					icon[15] = remaining_time / 4;
					break;

				case ADDITIONAL_FIRE:
					icon[11] = remaining_time / 16;
					break;
				case MORTAL_BODY:// 모탈바디 //
					icon[24] = remaining_time / 4;
					break;
				case CONCENTRATION:
					icon[21] = remaining_time / 16;
					break;

				case PATIENCE:// 페이션스 //
					icon[27] = remaining_time / 4;
					break;

				case INSIGHT:
					icon[22] = remaining_time / 16;
					pc.getAbility().addAddedStr((byte) 1);
					pc.getAbility().addAddedDex((byte) 1);
					pc.getAbility().addAddedCon((byte) 1);
					pc.getAbility().addAddedInt((byte) 1);
					pc.getAbility().addAddedWis((byte) 1);
					pc.getAbility().addAddedCha((byte) 1);
					pc.resetBaseMr();
					break;

				case PANIC:
					icon[23] = remaining_time / 16;
					pc.getAbility().addAddedStr((byte) -1);
					pc.getAbility().addAddedDex((byte) -1);
					pc.getAbility().addAddedCon((byte) -1);
					pc.getAbility().addAddedInt((byte) -1);
					pc.getAbility().addAddedWis((byte) -1);
					pc.getAbility().addAddedCha((byte) -1);
					pc.resetBaseMr();
					break;

				case L1SkillId.BLOOD_LUST:
					pc.sendPackets(new S_SkillBrave(pc.getId(), 6, remaining_time), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 6, 0), true);
					pc.getMoveState().setBraveSpeed(1);
					break;

				case STATUS_BRAVE:
					pc.sendPackets(new S_SkillBrave(pc.getId(), 1, remaining_time), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0), true);
					pc.getMoveState().setBraveSpeed(1);
					break;

				case STATUS_HASTE:
					pc.sendPackets(new S_SkillHaste(pc.getId(), 1, remaining_time), true);
					Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 1, 0), true);
					pc.getMoveState().setMoveSpeed(1);
					break;

				case STATUS_BLUE_POTION:
				case STATUS_BLUE_POTION2:
				case STATUS_BLUE_POTION3:
					pc.sendPackets(new S_SkillIconGFX(34, remaining_time), true);
					break;

				case STATUS_ELFBRAVE:
					pc.sendPackets(new S_SkillBrave(pc.getId(), 3, remaining_time), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 3, 0), true);
					pc.getMoveState().setBraveSpeed(1);
					break;

				case STATUS_CHAT_PROHIBITED:
					pc.sendPackets(new S_SkillIconGFX(36, remaining_time), true);
					break;

				case STATUS_TIKAL_BOSSDIE:
					icon[20] = (remaining_time + 8) / 16;
					L1SkillUse sk1 = new L1SkillUse();
					sk1.handleCommands(clientthread.getActiveChar(), skillid, pc.getId(), pc.getX(), pc.getY(), null, remaining_time, L1SkillUse.TYPE_LOGIN);
					sk1 = null;
					break;

				case STATUS_COMA_3:// 코마 3
					icon[31] = (remaining_time + 16) / 32;
					icon[32] = 40;
					L1SkillUse sk2 = new L1SkillUse();
					sk2.handleCommands(clientthread.getActiveChar(), skillid, pc.getId(), pc.getX(), pc.getY(), null, remaining_time, L1SkillUse.TYPE_LOGIN);
					sk2 = null;
					break;

				case STATUS_COMA_5:// 코마 5
					icon[31] = (remaining_time + 16) / 32;
					icon[32] = 41;
					L1SkillUse sk3 = new L1SkillUse();
					sk3.handleCommands(clientthread.getActiveChar(), skillid, pc.getId(), pc.getX(), pc.getY(), null, remaining_time, L1SkillUse.TYPE_LOGIN);
					sk3 = null;
					break;

				case SPECIAL_COOKING:
					if (pc.getSkillEffectTimerSet().hasSkillEffect(SPECIAL_COOKING)) {
						if (pc.getSkillEffectTimerSet().getSkillEffectTimeSec(SPECIAL_COOKING) < remaining_time) {
							pc.getSkillEffectTimerSet().setSkillEffect(SPECIAL_COOKING, remaining_time * 1000);
						}
					}
					continue;

				case L1SkillId.STATUS_시원한얼음조각:
					icon[18] = (remaining_time + 8) / 16;
					icon[19] = 0x4C;
					pc.addDmgup(2);
					pc.getAbility().addSp(2);
					pc.addHpr(1);
					pc.addMpr(1);
					pc.sendPackets(new S_SPMR(pc), true);
					break;

				case STATUS_CASHSCROLL:// 체력증강주문서 //
					icon[18] = (remaining_time + 8) / 16;
					pc.addHpr(4);
					pc.addMaxHp(50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					break;

				case STATUS_CASHSCROLL2:// 마력증강주문서 //
					icon[18] = (remaining_time + 8) / 16;
					icon[19] = 1;
					pc.addMpr(4);
					pc.addMaxMp(40);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					break;

				case STATUS_CASHSCROLL3:
					icon[18] = (remaining_time + 8) / 16;
					icon[19] = 2;
					pc.addDmgup(3);
					pc.addHitup(3);
					pc.getAbility().addSp(3);
					pc.sendPackets(new S_SPMR(pc), true);
					break;

				case STATUS_커츠투사:
					icon[18] = (remaining_time + 8) / 16;
					icon[19] = 111;
					pc.addDmgup(3);
					pc.addHitup(5);
					pc.addDamageReductionByArmor(3);
					break;

				case STATUS_커츠현자:
					icon[18] = (remaining_time + 8) / 16;
					icon[19] = 111;
					pc.addSuccMagic(5);
					pc.getAbility().addSp(3);
					pc.addDamageReductionByArmor(3);
					pc.sendPackets(new S_SPMR(pc), true);
					break;

				case STATUS_커츠명궁:
					icon[18] = (remaining_time + 8) / 16;
					icon[19] = 111;
					pc.addBowDmgup(3);
					pc.addBowHitup(5);
					pc.addDamageReductionByArmor(3);
					break;

				case STATUS_FRUIT:// 유그드라
					pc.sendPackets(new S_SkillBrave(pc.getId(), 4, remaining_time), true);
					Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 4, 0), true);
					pc.getMoveState().setBraveSpeed(1);
					break;

				case EXP_POTION:
				case L1SkillId.EXP_POTION_cash:
					if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
						pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.EMERALD_ICON, remaining_time, 6, 0x01));
						pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.EMERALD_ICON, remaining_time, 6, 0x02));
						// pc.setExpPotion(true);
					} else {
						pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.EMERALD_ICON, remaining_time, 6, 0x01));
						// pc.setExpPotion(false);
					}
					break;

				case EXP_POTION2:
					icon[17] = remaining_time / 16;
					break;

				case EXP_POTION3:
					icon[17] = remaining_time / 16;
					break;

				case FEATHER_BUFF_A:// 운세에 따른 깃털 버프 // 매우좋은
					icon[33] = remaining_time / 16;
					icon[34] = 70;
					pc.addDmgup(2);
					pc.addHitup(2);
					pc.getAbility().addSp(2);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.addHpr(3);
					pc.addMaxHp(50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.addMpr(3);
					pc.addMaxMp(30);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					break;

				case FEATHER_BUFF_B:// 운세에 따른 깃털 버프 // 좋은
					icon[33] = remaining_time / 16;
					icon[34] = 71;
					pc.addHitup(2);
					pc.getAbility().addSp(1);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.addMaxHp(50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.addMaxMp(30);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					break;

				case FEATHER_BUFF_C:// 운세에 따른 깃털 버프 // 보통
					icon[33] = remaining_time / 16;
					icon[34] = 72;
					pc.addMaxHp(50);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
					pc.addMaxMp(30);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					pc.getAC().addAc(-2);
					break;

				case L1SkillId.STATUS_UNDERWATER_BREATH:
					pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), remaining_time), true);
					break;

				case FEATHER_BUFF_D:// 운세에 따른 깃털 버프 // 나쁜
					icon[33] = remaining_time / 16;
					icon[34] = 73;
					pc.getAC().addAc(-1);
					break;

				case L1SkillId.레벨업보너스://
					pc.sendPackets(new S_PacketBox(remaining_time, true, true), true);
					break;

				case L1SkillId.PHYSICAL_ENCHANT_STR:
					pc.getAbility().addAddedStr((byte) 5);
					pc.sendPackets(new S_Strup(pc, 5, remaining_time), true);
					break;

				case L1SkillId.드래곤의성장버프:
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.신스킬, 6072), true);
					pc.sendPackets(new S_ACTION_UI(6072, remaining_time, 8382, 5087), true);
					break;

				case L1SkillId.PHYSICAL_ENCHANT_DEX:
					pc.getAbility().addAddedDex((byte) 5);
					pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					pc.sendPackets(new S_Dexup(pc, 5, remaining_time), true);
					break;

				case L1SkillId.흑사의기운:
					pc.getAC().addAc(-2);
					pc.addMaxHp(20);
					pc.addMaxMp(13);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					pc.sendPackets(new S_OwnCharStatus(pc));
					icon[37] = remaining_time / 16;
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.흑사의기운, remaining_time * 1000);
					break;

				// 2020.04.05 로그인 시 프라임 버프 적용 처리 by white
				case L1SkillId.PRIME:
					pc.addDmgup(3);
					pc.addHitup(3);
					pc.addBowDmgup(3);
					pc.addBowHitup(3);
					pc.getAbility().addSp(2);
					pc.프라임 = 5;
					pc.addTechniqueHit(pc.프라임);
					// 프라임 증가 효과 적용
					pc.sendPackets(new S_SPMR(pc), true);
					// 프라임 아이콘 표시
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.PRIME, true, remaining_time), true);
					break;

				// 2020.04.05 로그인 시 샤이닝 아머 버프 적용 처리 by white
				case L1SkillId.SHINING_AURA:
					// 시전자를 확인할 방법 찾을때 까지 공주나 군주면 시전자로 처리
					if (pc.getClassId() == L1PcInstance.CLASSID_PRINCE || pc.getClassId() == L1PcInstance.CLASSID_PRINCESS) {
						pc.getAC().addAc(-8);
					} else {
						pc.getAC().addAc(-4);
					}
					pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSkillIconAura, 115, remaining_time), true);
					break;
					
				case L1SkillId.miso1: {	
					pc.addDmgup(3);
					pc.addBowDmgup(3);
					pc.addMaxMp(50);
					pc.getAbility().addSp(3);
					pc.addMpr(2);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_MaanIcons(L1SkillId.miso1, true, remaining_time), true);
				}
				break;
				
			case L1SkillId.miso2: {					
					pc.addMaxHp(100);
					pc.getResistance().addMr(10);
					pc.addHpr(2);
					pc.sendPackets(new S_SPMR(pc));
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_MaanIcons(L1SkillId.miso2, true, remaining_time), true);
					
			}
			break;
			case L1SkillId.miso3: {
					pc.sendPackets(new S_MaanIcons(L1SkillId.miso3, true, remaining_time), true);					
			}
			break;
				default:
					L1SkillUse sk111 = new L1SkillUse();
					sk111.handleCommands(clientthread.getActiveChar(), skillid, pc.getId(), pc.getX(), pc.getY(), null, remaining_time, L1SkillUse.TYPE_LOGIN);
					sk111 = null;
					continue;
				}

				pc.getSkillEffectTimerSet().setSkillEffect(skillid, remaining_time * 1000);
			}

			S_UnityIcon uni = new S_UnityIcon(icon[0], icon[1], icon[2], icon[3], icon[4], icon[5], icon[6], icon[7], icon[8], icon[9], icon[10], icon[11],
					icon[12], icon[13], icon[14], icon[15], icon[16], icon[17], icon[18], icon[19], icon[20], icon[21], icon[22], icon[23], icon[24], icon[25],
					icon[26], icon[27], icon[28], icon[29], icon[30], icon[31], icon[32], icon[33], icon[34], icon[35], icon[36], icon[37], icon[38]);
			pc.sendPackets(uni, true);
			icon = null;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		/*
		 * { Random random = new Random(); int chance = random.nextInt(3); { switch
		 * (chance) { case 0: { pc.sendPackets(new
		 * S_PacketBox(S_PacketBox.GREEN_MESSAGE,"WOLF 서버에 오신걸 환영합니다."));
		 * 
		 * }
		 * 
		 * break; case 1: { pc.sendPackets(new
		 * S_PacketBox(S_PacketBox.GREEN_MESSAGE,"WOLF 서버에 오신걸 환영합니다.")); } break; case
		 * 2: { pc.sendPackets(new
		 * S_PacketBox(S_PacketBox.GREEN_MESSAGE,"WOLF 서버에 오신걸 환영합니다.")); } break; } } }
		 */
	}

	class charNameDelete implements Runnable {
		private String name = null;

		public charNameDelete(String _name) {
			name = _name;
		}

		@Override
		public void run() {

			try {
				if (nameList.contains(name))
					nameList.remove(name);
			} catch (Exception e) {
				System.out.println("접속 이름 리스트에서 삭제 에러 : " + name);
			}
		}

	}

	private void 북(L1PcInstance pc) {
		try {

			HashMap<Integer, Integer> mbl = MonsterBookTable.getInstace().getMonBookList(pc.getId());

			if (mbl != null) {
				mbl.forEach((monNum, value) -> {
					int monquest1 = MonsterBookTable.getInstace().getQuest1(monNum);
					int monquest2 = MonsterBookTable.getInstace().getQuest2(monNum);
					int monquest3 = MonsterBookTable.getInstace().getQuest3(monNum);

					if (value >= monquest1) {
						pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_CLEAR, (monNum * 3) - 2));
					}
					if (value >= monquest2) {
						pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_CLEAR, (monNum * 3) - 1));
					}
					if (value >= monquest3) {
						pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_CLEAR, monNum * 3));
					}
				});
			}

		} catch (Exception e) {
		}
	}

	private void getAttendanceCheck(Account account, L1PcInstance pc) {
		// 마지막 출석 체크일을 검색해야하지 않을까??

		pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_START));
		pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_ITEM, 0));
		pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_ITEM, 1));
		pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_COMPLETE, pc.getAccount(), pc.PC방_버프));
	}

	// 편지리스트 출력을위한 메소드
	private void LetterList(L1PcInstance pc, int type, int count) {
		if (pc != null)
			pc.sendPackets(new S_LetterList(pc, type, count), true);
	}

	@Override
	public String getType() {
		return C_LOGIN_TO_SERVER;
	}
}