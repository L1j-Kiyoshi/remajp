package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.RevengeTime;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.IND;
import l1j.server.IND_Q;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.Dungeon.DungeonInfo;
import l1j.server.GameSystem.Dungeon.DungeonSystem;
import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.GameSystem.InterServer.InterServer;
import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.GameSystem.RotationNotice.RotationNoticeTable;
import l1j.server.GameSystem.RotationNotice.RotationNoticeTable.EventNotice;
import l1j.server.GameSystem.SupportSystem.L1SupportMap;
import l1j.server.GameSystem.SupportSystem.SupportMapTable;
import l1j.server.GameSystem.UserRanking.UserRankingController;
import l1j.server.server.Account;
import l1j.server.server.ActionCodes;
import l1j.server.server.BadNamesList;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.UserCommands;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.AttendanceTable;
import l1j.server.server.datatables.AttendanceTable.AttendanceItem;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.CharcterRevengeTable;
import l1j.server.server.datatables.ClanBlessHuntInfo;
import l1j.server.server.datatables.ClanBlessHuntInfo.HuntInfo;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.CraftInfoTable;
import l1j.server.server.datatables.CraftInfoTable.L1CraftInfo;
import l1j.server.server.datatables.CraftInfoTable.Material;
import l1j.server.server.datatables.ExcludeLetterTable;
import l1j.server.server.datatables.ExcludeTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.MonsterBookTable;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.datatables.NpcShopSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetsSkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1ExcludingLetterList;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharMapTime;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_ClanBlessHuntUi;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_EventNotice;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_MonsterUi;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_NewUI;
import l1j.server.server.serverpackets.S_Notice;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Party;
import l1j.server.server.serverpackets.S_PetWindow;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_Revenge;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_ServerVersion;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SupportSystem;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1LoginInfo;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1PetSkill;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.templates.L1UserRanking;
import l1j.server.server.templates.eCharacterDeleteResult;
import l1j.server.server.utils.BinaryOutputStream;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;
import manager.LinAllManager;
import server.LineageClient;
import server.LoginController;
import server.controller.InvSwapController;

public class C_ActionUi extends ClientBasePacket {
	Random _Random = new Random(System.nanoTime());
	private static final String C_ACTION_UI = "[C] C_ActionUi";
	private static final String[] textFilter = { "시발" };

	private static final int CRAFT_ITEM = 54;
	private static final int CRAFT_ITEMLIST = 56;
	private static final int CRAFT_OK = 58;
	private static final int SIEGE_WAR_REQ = 69;
	private static final int ServerInter = 117;
	private static final int DOLL_START = 122;
	private static final int DOLL_RESULT = 124;
	private static final int TELEPORT_SKY_GARDEN = 132;
	private static final int 유저랭킹 = 135;
	private static final int EventNotice = 143;
	private static final int ACTIONKEY = 319;
	private static final int CLAN_JOIN = 322;
	private static final int CLAN_MEMBERS = 324;
	private static final int CLAN_OPTION_CHANGE = 326;
	private static final int CLAN_OPTION = 332;
	private static final int PARTY_SIGN = 338;
	private static final int SPELL_PASSIVE_ONOFF_REQ = 408;
	private static final int ACCOUNT_TAM = 460;
	private static final int ACCOUNT_TAM_CANCEL = 480;
	private static final int CHAR_STAT = 484;
	private static final int CHAT = 514;
	private static final int EXCLUDE = 543;
	private static final int MONSTERBOOK_CLEAR = 563;
	private static final int MONSTERBOOK_TEL = 565;
	private static final int SEAL = 569;
	private static final int Party_Assist = 599;
	private static final int BMTYPE_DEL_CHECK_REQ = 604;
	private static final int EQUIPMENT_CHANGE = 801;
	private static final int RESTART_UI = 802;
	private static final int BOOKWEEKQUEST_TEL = 815;
	private static final int BOOKWEEKQUEST_CLEAR = 811;
	private static final int 개인상점 = 817;
	private static final int ServerVersion = 820;
	private static final int Party_Invitation = 828;
	private static final int WorldMapTeleport = 829;
	private static final int MOVE_SERVER_AUTH_REQ = 830;
	private static final int NP_LOGIN_REQ = 997;
	private static final int ENVIRONMENT_SETTING = 1002;
	private static final int ATTENDANCE_CHECK_COMPLETE = 1006;
	private static final int CLAN_BLESS_HUNT_CHANGE = 1016;
	private static final int CLAN_BLESS_HUNT_ALL_CHANGE = 1017;
	private static final int 상태창랭킹 = 1021;
	private static final int RevengeBoard = 1051;
	private static final int RevengeProvoke = 1054;
	private static final int RevengeChase = 1056;
	private static final int RevengeChase2 = 1058;
	private static final int PetName = 2001;
	private static final int Petstat = 2003;
	private static final int PetCommand = 2004;
	private static final int PetSkillLevel = 2007;
	private static final int PetSkill = 2008;
	private static final int START_PLAY_SUPPORT = 2101;
	private static final int FINISH_PLAY_SUPPORT = 2103;
	private static final int InDungeonList = 2211;
	private static final int InDungeonAccess = 2213;
	private static final int InDungeonType = 2215;
	private static final int InDungeonSlot = 2217;
	private static final int InDungeonStancet = 2220;
	private static final int InDungeonReady = 2223;
	private static final int InDungeStart = 2225;
	private static final int InDungeoninvite = 2227;
	private static final int InDungeon = 2229;
	private static final int InDungeExit = 2231;
	private static final int InDungeonOut = 2234;
	private static final int PETBALL_CONTENTS_START_REQ = 2376;
	private static final int PETBALL_CONTENTS_END_REQ = 2377;

	private static final String REMOVE = "08 01 1a 04 08 00 10 00 85 32";
	private static final String ADD = "08 00 12 29 08 d3 7a 10 01 18 ff ff ff ff ff ff ff ff ff 01 20 00 28 01 30 00 38 01 42 06 24 31 30 39 34 37 48 00 50 00 58 b0 2d 62 00 9d 80";
	private static final int[] bigSuccessItem = {};

	public C_ActionUi(byte abyte0[], LineageClient client) throws IOException {
		super(abyte0);
		int type = readH();

		L1PcInstance pc = client.getActiveChar();

		// System.out.println("액션 > " + type);

		switch (type) {
		case CHAR_STAT: {
			try {
				int length = readH();// 길이
				ArrayList<byte[]> arrb = new ArrayList<byte[]>();
				for (int i = 0; i < length / 2; i++) {
					arrb.add(readByte(2));
				}

				int level, classtype = 0, status = 0, unknown2, unknown3 = 0, str = 0, cha = 0, inte = 0, dex = 0, con = 0, wis = 0;
				for (byte[] b : arrb) {
					switch (b[0]) {
					case 0x08:
						level = b[1] & 0xff;
						break;// 모름
					case 0x10:
						classtype = b[1] & 0xff;
						break;// 클래스 타입
					case 0x18:
						status = b[1] & 0xff;
						break;// 초기상태 = 1 / 스탯변경상태 = 8

					case 0x20:
						unknown2 = b[1] & 0xff;
						break;// 모름
					case 0x28:
						unknown3 = b[1] & 0xff;
						break;// 모름

					case 0x30:
						str = b[1] & 0xff;
						break;// 힘
					case 0x38:
						inte = b[1] & 0xff;
						break;// 인트
					case 0x40:
						wis = b[1] & 0xff;
						break;// 위즈
					case 0x48:
						dex = b[1] & 0xff;
						break;// 덱
					case 0x50:
						con = b[1] & 0xff;
						break;// 콘
					case 0x58:
						cha = b[1] & 0xff;
						break;// 카리

					default:
						int i = 0;
						try {
							i = b[0] & 0xff;
						} catch (Exception e) {
						}
						System.out.println("[스탯관련 정의되지 않은 패킷] op : " + i);
						break;
					}
				}

				if (str != 0 && unknown3 != 1) {
					client.sendPacket(new S_NewCreateItem(0x01e3, status, str, con, "힘", classtype, null));
				}

				if (dex != 0) {
					client.sendPacket(new S_NewCreateItem(0x01e3, status, dex, 0, "덱", classtype, null));
				}

				if (con != 0 && unknown3 != 16) {
					client.sendPacket(new S_NewCreateItem(0x01e3, status, con, str, "콘", classtype, null));
				}

				if (inte != 0) {
					client.sendPacket(new S_NewCreateItem(0x01e3, status, inte, 0, "인트", classtype, null));
				}

				if (wis != 0) {
					client.sendPacket(new S_NewCreateItem(0x01e3, status, wis, 0, "위즈", classtype, null));
				}

				if (cha != 0) {
					client.sendPacket(new S_NewCreateItem(0x01e3, status, cha, 0, "카리", classtype, null));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;
		case CLAN_OPTION: {
			if (pc.getClanid() == 0) {
				return;
			}
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.CLAN_JOIN_SETTING, pc.getClan().getJoinSetting(), pc.getClan().getJoinType()), true);
		}
			break;
		case CLAN_JOIN: {
			/** 2016.11.25 MJ 앱센터 혈맹 **/
			int joinType = readC();
			readH();
			int length = readC();

			// 존재하지 않는 혈맹입니다.
			if (pc.isCrown()) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 4), true);
				return;
			}

			// 이미 혈맹에 가입한 상태 입니다.
			if (pc.getClanid() != 0) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 9), true);
				return;
			}

			// 군주를 만나 가입해 주세요.
			try {
				String clanname = new String(readByteL(length), 0, length, "MS932");
				L1Clan clan = L1World.getInstance().getClan(clanname);
				//
				if (clan == null) {
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 13), true);
					return;
				}

				L1PcInstance crown = clan.getonline간부();
				switch (clan.getJoinType()) {
				case 1:
					if (crown == null) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 11), true);
						return;
					}

					crown.setTempID(pc.getId()); // 상대의 오브젝트 ID를 보존해 둔다
					S_Message_YN myn = new S_Message_YN(97, pc.getName());
					crown.sendPackets(myn, true);
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 1), true);
					return;
				case 2:
					readD();
					readC();
					int size = readC();
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < size; i++)
						sb.append(String.format("%02X", readC()));
					if (clan.getJoinPassword() == null || !clan.getJoinPassword().equalsIgnoreCase(sb.toString())) {
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 3), true);
						return;
					}
				case 0:
					if (crown != null) {
						C_Attr.혈맹가입(crown, pc, clan);
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 0), true);
					} else
						pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 1), true);
					break;
				default:
					pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 11), true);
					break;
				}
			} catch (Exception e) {
			}
		}
			break;
		case CLAN_OPTION_CHANGE: {
			if (pc.getClanid() == 0 || (!pc.isCrown() && pc.getClanRank() != L1Clan.CLAN_RANK_GUARDIAN)) {
				return;
			}

			readC();
			readH();
			int setting = readC();
			readC();
			int setting2 = readC();
			if (setting2 == 2) {
				pc.sendPackets(new S_SystemMessage("현재 암호 가입 유형으로 설정할 수 없습니다."), true);
				setting2 = 1;
			}

			pc.getClan().setJoinSetting(setting);
			pc.getClan().setJoinType(setting2);
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.CLAN_JOIN_SETTING, setting, setting2), true);
			ClanTable.getInstance().updateClan(pc.getClan());
			pc.sendPackets(new S_ServerMessage(3980), true);
		}
			break;
		case SPELL_PASSIVE_ONOFF_REQ: {
			try {
				readH();
				readC();
				int passive_id = readBit();
				readC();
				boolean on_off = readBit() == 1 ? true : false;
				if (passive_id == 38) {
					if (on_off) {
						pc.startBloodToSoul();
					} else {
						pc.stopBloodToSoul();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;
		case PARTY_SIGN: {
			try {

				int length = readH();

				byte[] BYTE = readByte();
				byte[] objid = new byte[length - 3];
				byte[] subtype = new byte[1];

				if (pc.getParty() == null) {
					return;
				}

				if (!pc.getParty().isLeader(pc)) {
					return;
				}

				System.arraycopy(BYTE, 1, objid, 0, objid.length);
				System.arraycopy(BYTE, length - 1, subtype, 0, 1);

				StringBuffer sb = new StringBuffer();

				for (byte zzz : objid) {
					sb.append(String.valueOf(zzz));
				}

				String s = sb.toString();

				L1PcInstance 표식pc = null;

				// System.out.println(s);

				for (L1PcInstance player : pc.getParty().getMembers()) {
					// System.out.println(player.encobjid);
					if (s.equals(player.encobjid)) {
						player.set표식(subtype[0]);
						표식pc = player;
					}
				}

				if (표식pc != null) {
					for (L1PcInstance player : pc.getParty().getMembers()) {
						player.sendPackets(new S_NewUI(0x53, 표식pc));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;
		case ACCOUNT_TAM_CANCEL: {
			readC();
			readH();
			byte[] BYTE = readByte();
			byte[] temp = new byte[BYTE.length - 1];
			for (int i = 0; i < temp.length; i++) {
				temp[i] = BYTE[i];
			}

			StringBuffer sb = new StringBuffer();
			for (byte zzz : temp) {
				sb.append(String.valueOf(zzz));
			}

			int day = Nexttam(sb.toString());
			int charobjid = TamCharid(sb.toString());
			if (charobjid != pc.getId()) {
				pc.sendPackets(new S_SystemMessage("해당 케릭터만 취소를 할 수 있습니다."));
				return;
			}

			int itemid = 0;
			if (day != 0) {
				if (day == 7) {
					itemid = 5559;
				} else if (day == 30) {
					itemid = 5560;
				}
				L1ItemInstance item = pc.getInventory().storeItem(itemid, 1);
				if (item != null) {
					pc.sendPackets(new S_ServerMessage(403, item.getName() + " (1)"), true);
					tamcancle(sb.toString());
					pc.sendPackets(new S_NewCreateItem(pc.getAccountName(), 0xcd));
				}
			}
		}
			break;
		case ACCOUNT_TAM: {
			pc.sendPackets(new S_NewCreateItem(pc.getAccountName(), S_NewCreateItem.TamPage));
		}
			break;
		case TELEPORT_SKY_GARDEN: {
			if (!pc.PC방_버프) {
				pc.sendPackets(new S_SystemMessage("PC방 이용권을 사용중에만 사용 가능한 행동입니다."), true);
				return;
			}

			if (pc.getMapId() == 99 || pc.getMapId() == 6202) {
				pc.sendPackets(new S_SystemMessage("주위의 마력에의해 순간이동을 사용할 수 없습니다."), true);
				return;
			}

			/*
			 * if (!pc.getMap().isTeleportable()) { pc.sendPackets(new
			 * S_SystemMessage("주위의 마력에의해 순간이동을 사용할 수 없습니다."), true); return; }
			 */

			int ran = _Random.nextInt(4);

			if (ran == 0) {
				L1Teleport.teleport(pc, 32779, 32825, (short) 622, pc.getMoveState().getHeading(), true);
			} else if (ran == 1) {
				L1Teleport.teleport(pc, 32761, 32819, (short) 622, pc.getMoveState().getHeading(), true);
			} else if (ran == 2) {
				L1Teleport.teleport(pc, 32756, 32837, (short) 622, pc.getMoveState().getHeading(), true);
			} else {
				L1Teleport.teleport(pc, 32770, 32839, (short) 622, pc.getMoveState().getHeading(), true);
			}
		}
			break;
		case CLAN_MEMBERS: {
			pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_WAIT, true), true);
		}
			break;
		case SIEGE_WAR_REQ: {
			try {
				readH();
				readC();
				int castleType = readC();
				// 1켄트 2기란 4오크요새
				String s = "";
				for (L1Clan cc : L1World.getInstance().getAllClans()) {
					if (castleType == cc.getCastleId()) {
						s = cc.getClanName();
						break;
					}
				}

				if (s.equalsIgnoreCase("")) {
					return;
				}

				L1PcInstance player = pc;
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

				if (player.getId() != clan.getLeaderId()) { // 혈맹주
					S_ServerMessage sm = new S_ServerMessage(478);
					player.sendPackets(sm, true);
					return;
				}

				if (clanName.toLowerCase().equals(s.toLowerCase())) { // 자크란을 지정
					S_SystemMessage sm = new S_SystemMessage("자신의 혈에 공성 선포는 불가능합니다.");
					player.sendPackets(sm, true);
					return;
				}

				L1Clan enemyClan = null;
				String enemyClanName = null;
				for (L1Clan checkClan : L1World.getInstance().getAllClans()) { // 크란명을 체크
					if (checkClan.getClanName().toLowerCase().equals(s.toLowerCase())) {
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

				List<L1War> warList = L1World.getInstance().getWarList(); // 전쟁 리스트를 취득
				if (clan.getCastleId() != 0) { // 자크란이 성주
					S_ServerMessage sm = new S_ServerMessage(474);
					player.sendPackets(sm, true);
					return;
				}

				if (enemyClan.getCastleId() != 0 && // 상대 크란이 성주로, 자캐릭터가
													// Lv25 미만
						player.getLevel() < 25) {
					S_ServerMessage sm = new S_ServerMessage(475);
					player.sendPackets(sm, true); // 공성전을 선언하려면 레벨 25에 이르지
													// 않으면 안됩니다.
					return;
				}

				int onLineMemberSize = 0;
				for (L1PcInstance onlineMember : clan.getOnlineClanMember()) {
					if (onlineMember.isPrivateShop())
						continue;
					onLineMemberSize++;
				}

				if (onLineMemberSize < Config.warmember) {
					player.sendPackets(new S_SystemMessage("접속중인 혈맹 구성원이 " + Config.warmember + " 명 이상 되어야 선포가 가능합니다."), true);
					return;
				}

				/*
				 * if (clan.getHouseId() > 0) { S_SystemMessage sm = new S_SystemMessage(
				 * "아지트가 있는 상태에서는 선전 포고를 할 수 없습니다."); player.sendPackets(sm, true); return; }
				 */
				if (enemyClan.getCastleId() != 0) { // 상대 크란이 성주
					int castle_id = enemyClan.getCastleId();
					if (WarTimeController.getInstance().isNowWar(castle_id)) { // 전쟁 시간내
						L1PcInstance clanMember[] = clan.getOnlineClanMember();
						for (int k = 0; k < clanMember.length; k++) {
							if (L1CastleLocation.checkInWarArea(castle_id, clanMember[k])) {
								// S_ServerMessage sm = new
								// S_ServerMessage(477);
								// player.sendPackets(sm, true); // 당신을 포함한
								// 모든 혈맹원이 성의 밖에 나오지 않으면 공성전은 선언할 수 없습니다.
								int[] loc = new int[3];
								Random _rnd = new Random(System.nanoTime());
								loc = L1CastleLocation.getGetBackLoc(castle_id);
								int locx = loc[0] + (_rnd.nextInt(4) - 2);
								int locy = loc[1] + (_rnd.nextInt(4) - 2);
								short mapid = (short) loc[2];
								L1Teleport.teleport(clanMember[k], locx, locy, mapid, clanMember[k].getMoveState().getHeading(), true);
							}
						}

						boolean enemyInWar = false;
						for (L1War war : warList) {
							if (war.CheckClanInWar(enemyClanName)) { // 상대 크랜이 이미 전쟁중
								war.DeclareWar(clanName, enemyClanName);
								war.AddAttackClan(clanName);
								enemyInWar = true;
								break;
							}
						}
						if (!enemyInWar) { // 상대 크란이 전쟁중 이외로, 선전포고
							L1War war = new L1War();
							war.handleCommands(1, clanName, enemyClanName); // 공성전 개시
						}
					} else { // 전쟁 시간외
						S_ServerMessage sm = new S_ServerMessage(476);
						player.sendPackets(sm, true); // 아직 공성전의 시간이 아닙니다.
					}
				} else { // 상대 크란이 성주는 아니다
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;
		case ACTIONKEY: {
			readD();
			readC();
			int action = readC();
			if (action >= 1 && action <= 11) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.EMOTICON, action, pc.getId()), true);
				Broadcaster.broadcastPacket(pc, new S_ACTION_UI(S_ACTION_UI.EMOTICON, action, pc.getId()), true);
			}
		}
			break;
		case ATTENDANCE_CHECK_COMPLETE: {
			try {
				if (pc == null) {
					return;
				}

				if (pc.hasSkillEffect(L1SkillId.출석체크ディレイ)) {
					return;
				}

				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.출석체크ディレイ, 3000);
				readH();
				readC();

				int objectId = readBit();
				readC();
				int size = readBit();
				Account account = pc.getAccount();
				AttendanceTable attend = AttendanceTable.getInstance();

				boolean check_one = size == 0 ? account.getAttendanceHomeBytes()[objectId - 1] != 1 : account.getAttendancePcBytes()[objectId - 1] != 1;
				boolean check_two = size == 0 ? account.getAttendanceHomeBytes()[objectId - 1] == 2 : account.getAttendancePcBytes()[objectId - 1] == 2;

				if (check_one || check_two) {
					System.out.println("★☆★ 중계기(출석체크) 의심 유저 : [" + account.getName() + "] ★☆★");
					return;
				}

				ArrayList<AttendanceItem> _item = size == 0 ? attend.getAttendHomeItem(objectId) : attend.getAttendPCItem(objectId);
				L1ItemInstance reward_item = null;
				AttendanceItem reward_info = null;
				int chance = CommonUtil.random(100);
				int reward_size = _item.size();

				for (int i = 0; i < reward_size; i++) {
					reward_info = _item.get(i);
					if (chance < reward_info._probability) {
						reward_item = ItemTable.getInstance().createItem(reward_info._itemId);
						if (reward_item != null) {
							if (reward_info._enchant != 0) {
								reward_item.setEnchantLevel(reward_info._enchant);
							}

							reward_item.setCount(reward_info._count);
							break;
						}
					}
				}

				if (size == 0) {
					account.getAttendanceHomeBytes()[objectId - 1] = 2;
					account.updateAttendaceDate();
					/** 출석체크 모두 완료시 초기화 **/
					if (account.isAllHomeAttendCheck()) {
						byte[] b = new byte[42];
						account.setAttendanceHomeBytes(b);
					}

				} else {
					account.getAttendancePcBytes()[objectId - 1] = 2;
					account.updateAttendacePcDate();

					/** 출석체크 모두 완료시 초기화 **/
					if (account.isAllPcAttendCheck()) {
						byte[] b = new byte[42];
						account.setAttendancePcBytes(b);
					}
				}

				pc.sendPackets(
						new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_ITEM_COMPLETE, objectId, size == 0 ? false : true, reward_item, reward_info._broadcast_item));

				long delay_time = reward_size >= 2 ? 7000 : 0;
				/**
				 * 룰렛시간 지난후 적용
				 */
				AttendanceGiveItem agi = new AttendanceGiveItem(pc, reward_item, reward_info);
				GeneralThreadPool.getInstance().schedule(agi, delay_time);

				account.storeAttendBytes();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				clear();
			}
		}
			break;
		case 유저랭킹: {
			readH();
			readC();
			int classId = readC();
			ArrayList<L1UserRanking> list = UserRankingController.getInstance().getList(classId);

			if (list.size() > 100) {
				List<L1UserRanking> cutlist = list.subList(0, 100);
				List<L1UserRanking> cutlist2 = list.subList(100, list.size() > 200 ? 200 : list.size());
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.유저랭킹, cutlist, classId, 2, 1));
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.유저랭킹, cutlist2, classId, 2, 2));
			} else {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.유저랭킹, list, classId, 1, 1));
			}
		}
			break;
		case EXCLUDE: {
			readH();
			readC();
			int excludeType = readC();
			readC();
			int subType = readC();
			int nameFlag = readC();
			String name = "";
			if (nameFlag == 0x1A) {
				int nameLength = readC();
				name = readS(nameLength);
			}

			try {
				if (name.isEmpty()) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE2, pc.getExcludingList().getList(), 0), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE2, pc.getExcludingLetterList().getList(), 1), true);
					return;
				}

				if (excludeType == 1) {// 추가
					L1ExcludingList exList = pc.getExcludingList();
					L1ExcludingLetterList exletterList = pc.getExcludingLetterList();
					switch (subType) {// 일반 0 편지 1
					case 0:
						if (exList.contains(name)) {
							/*
							 * String temp = exList.remove(name); S_PacketBox pb = new
							 * S_PacketBox(S_PacketBox.REM_EXCLUDE, temp, type); pc.sendPackets(pb, true);
							 * ExcludeTable.getInstance().delete(pc.getName( ), name);
							 */
						} else {
							if (exList.isFull()) {
								S_SystemMessage sm = new S_SystemMessage("차단된 사용자가 너무 많습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							exList.add(name);
							S_PacketBox pb = new S_PacketBox(S_PacketBox.ADD_EXCLUDE, name, 0);
							pc.sendPackets(pb, true);
							ExcludeTable.getInstance().add(pc.getName(), name);
						}
						break;
					case 1:
						if (exletterList.contains(name)) {
						} else {
							if (exletterList.isFull()) {
								S_SystemMessage sm = new S_SystemMessage("차단된 사용자가 너무 많습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							exletterList.add(name);
							S_PacketBox pb = new S_PacketBox(S_PacketBox.ADD_EXCLUDE, name, 1);
							pc.sendPackets(pb, true);
							ExcludeLetterTable.getInstance().add(pc.getName(), name);
						}
						break;
					default:
						break;
					}
				} else if (excludeType == 2) {// 삭제
					L1ExcludingList exList = pc.getExcludingList();
					L1ExcludingLetterList exletterList = pc.getExcludingLetterList();
					switch (subType) {// 일반 0 편지 1
					case 0:
						if (exList.contains(name)) {
							String temp = exList.remove(name);
							S_PacketBox pb = new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp, 0);
							pc.sendPackets(pb, true);
							ExcludeTable.getInstance().delete(pc.getName(), name);
						}
						break;
					case 1:
						if (exletterList.contains(name)) {
							String temp = exletterList.remove(name);
							S_PacketBox pb = new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp, 1);
							pc.sendPackets(pb, true);
							ExcludeLetterTable.getInstance().delete(pc.getName(), name);
						}
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;
		case 상태창랭킹: {
			int 타입 = readH();
			readC();
			long 시간 = read4(read_size());
			L1UserRanking classRank = UserRankingController.getInstance().getClassRank(pc.getType(), pc.getName());
			L1UserRanking rank = UserRankingController.getInstance().getTotalRank(pc.getName());
			if (classRank == null && rank == null) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.상태창랭킹, classRank, rank, true));
			} else if (타입 == 2) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.상태창랭킹, classRank, rank, true));
			} else if (타입 == 6 && UserRankingController.랭킹갱신 >= 시간) {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.상태창랭킹, classRank, rank, true));
			} else {
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.상태창랭킹, classRank, rank, false));
			}
		}
			break;
		case FINISH_PLAY_SUPPORT: {
			pc.setAutoPlay(false);
			pc.sendPackets(new S_SupportSystem(pc, S_SupportSystem.SC_FINISH_PLAY_SUPPORT_ACK), true);
		}
			break;
		case START_PLAY_SUPPORT: {
			if (SupportMapTable.getInstance().isSupportMap(pc.getMapId())) {
				L1SupportMap SM = SupportMapTable.getInstance().getSupportMap(pc.getMapId());
				if (SM != null) {
					if (pc.getLevel() > 1 && pc.getLevel() <= Config.클라우디아레벨) {
						L1QuestInfo info = pc.getQuestList(368);
						if (info != null && info.end_time == 0) {
							info.ck[0] = 1;
							pc.sendPackets(new S_QuestTalkIsland(pc, 368, info));
						}
					}

					pc.setAutoPlay(true);
					pc.sendPackets(new S_SupportSystem(pc, S_SupportSystem.SC_START_PLAY_SUPPORT_ACK), true);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("이 곳에서는 사용할 수 없습니다."), true);
			}
		}
			break;
		case WorldMapTeleport: {
			readC();
			readH();
			int chatlen = readBit(); // 바이트 사이트
			byte[] actionName = readByte(chatlen);
			String code = new String(actionName, "MS932");

			for (L1NpcInstance tel_map_npc : L1World.getInstance().getAllTeleporter()) {
				if (tel_map_npc.getNpcId() == 50036) {
					L1Object npc = L1World.getInstance().findObject(tel_map_npc.getId());
					L1NpcAction action = NpcActionTable.getInstance().get(code, pc, npc);
					if (pc.getInventory().checkItem(140100)) {
						pc.getInventory().consumeItem(140100, 1);
						action.execute(code, pc, npc, null);
						// System.out.println("message" + code);
					}
				}
			}
		}
			break;
		case EQUIPMENT_CHANGE: {
			readH();

			// readC();
			int index = readC();
			int code = readC();
			if (index == 0x08) {
				InvSwapController.getInstance().toSaveSet(pc, code);
			} else if (index == 0x10) {
				InvSwapController.getInstance().toChangeSet(pc, code);
			}
		}
			break;
		case CHAT: {
			int totallen = readH();// 전체길이
			패킷위치변경((byte) 0x10);// 위치이동
			int chattype = readC();// 채팅타입
			패킷위치변경((byte) 0x1a);// 위치이동
			int chatlen = readC();// 채팅길이
			BinaryOutputStream os = new BinaryOutputStream();

			for (int i = 0; i < chatlen; i++) {
				os.writeC(readC());
			}

			byte[] chat = os.getBytes();
			String chat2 = new String(chat, "EUC-KR");
			os.close();
			String name = "";

			if (chattype != 1) {
				Chat(pc, chattype, totallen, chat, chat2, client);
				return;
			}

			패킷위치변경((byte) 0x2a);// 위치이동
			int namelen = readC();// 이름길이
			if (namelen != 0) {
				name = readS(namelen);
			}

			ChatWhisper(pc, chattype, totallen, chat, chat2, name);
		}
			break;
		case ENVIRONMENT_SETTING: {
			if (pc == null) {
				return;
			}

			if (pc.isOneMinuteStart()) {
				resetAttendanceTime(pc.getAccount(), pc);
				getAttendanceHome(pc, pc.getAccount());
				getAttendancePCRoom(pc, pc.getAccount());
			}

			if (!pc.isOneMinuteStart()) {
				pc.setOneMinuteStart(true);
			}
		}
			break;
		case RESTART_UI: {
			pc.sendPackets(new S_CharMapTime(pc));
		}
			break;
		case ServerVersion: {
			client.sendPacket(new S_ServerVersion(S_ServerVersion.SERVER_VERSION, 2004281703, S_ServerVersion.RESULT_OK));
		}
			break;
		case ServerInter: {
			try {
				int ServerInterTotal = readH();
				String Name = null;

				for (int i = 0; i < ServerInterTotal; i++) {
					if (readLength() == 0) {
						break;
					}

					int Type = readC(), Length = 0;
					if (Type == 0x08) {
						read4(read_size());
					} else if (Type == 0x12) {
						Length = readC();
						Name = readS(Length);
					} else {
						break;
					}
				}

				L1PcInstance Player = L1World.getInstance().getPlayer(Name);
				client.setInterServer(Player.getNetConnection().getInterServer());
				client.setInterServerType(Player.getNetConnection().getInterServerType());
				client.setInterServerName(Player.getName());
				client.setInterServerParty(Player.getParty());
				client.setInterServerNotice(Player.get표식());

				client.setAccount(Player.getNetConnection().getAccount());
				LoginController.getInstance().login(client, client.getAccount());

				Player.getNetConnection().ServerInterKick();
				InterServer.RequestInterServer(client);

				return;
			} catch (Exception e) {
				e.printStackTrace();
				/** 만약 에러나면 정보 리셋 */
				client.kick();
			}
		}
			break;
		case 개인상점: {
			if (pc == null || pc.isGhost() || pc.isDead()) {
				return;
			}
			if (pc.isInvisble()) {
				pc.sendPackets(new S_ServerMessage(755), true);
				return;
			}
			if (pc.getMapId() != 800) {
				pc.sendPackets(new S_ServerMessage(3405)); // 개설 불가 지역
				return;
			}

			if (Config.STANDBY_SERVER) {
				pc.sendPackets(new S_SystemMessage("오픈대기중에는 사용할 수 없습니다."), true);
				pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
				Broadcaster.broadcastPacket(pc, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
				break;
			}

			for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
				if (target.getId() != pc.getId() && target.getAccountName().toLowerCase().equals(pc.getAccountName().toLowerCase()) && target.isPrivateShop()) {
					pc.sendPackets(new S_SystemMessage("\\aH알림: 이미 무인상점이 개설 되어 있습니다 ."), true);
					return;
				}
			}

			ArrayList<L1PrivateShopSellList> sellList = pc.getSellList();
			ArrayList<L1PrivateShopBuyList> buyList = pc.getBuyList();
			L1ItemInstance checkItem;
			boolean tradable = true;

			readC();
			int length = readBit();

			readC();
			readC();
			int shoptype = readC(); // 0 or 1

			if (shoptype == 0) { // 개시
				int sellTotalCount = 0;
				int buyTotalCount = 0;
				int sellObjectId = 0;
				int sellPrice = 0;
				int sellCount = 0;
				L1ItemInstance sellitem = null;
				Object[] petlist = null;
				L1ItemInstance buyitem = null;
				for (int i = 0; i < length; i++) {
					int code = readC();
					if (code == 0x12 || code == 0x1a) {
						readC();
						for (int i2 = 0; i2 < 3; i2++) {
							int code2 = readC();
							if (code2 == 0x08)
								sellObjectId = readBit();
							else if (code2 == 0x10)
								sellPrice = readBit();
							else if (code2 == 0x18)
								sellCount = readBit();
						}
						// 거래 가능한 아이템이나 체크
						checkItem = pc.getInventory().getItem(sellObjectId);

						if (checkItem == null) {
							continue;
						}

						if (sellObjectId != checkItem.getId()) {
							tradable = false;
							pc.sendPackets(new S_SystemMessage("\\aH알림: 비정상 아이템 입니다. 다시 시도해주세요."), true);
						}
						if (!checkItem.isStackable() && sellCount != 1) {
							tradable = false;
							pc.sendPackets(new S_SystemMessage("\\aH알림: 비정상 아이템 입니다. 다시 시도해주세요."), true);
						}
						if (sellCount > checkItem.getCount()) {
							sellCount = checkItem.getCount();
						}
						if (checkItem.getCount() < sellCount || checkItem.getCount() <= 0 || sellCount <= 0) {
							tradable = false;
							pc.sendPackets(new S_SystemMessage("\\aH알림: 비정상 아이템 입니다. 다시 시도해주세요."), true);
						}
						if (checkItem.getBless() >= 128) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(210, checkItem.getItem().getName())); // 봉인상태
						}

						if (!checkItem.getItem().isTradable()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(941), true); // 거래 불가 아이템입니다.
						}

						L1DollInstance 인형 = null;
						for (Object 인형오브젝트 : pc.getDollList()) {
							if (인형오브젝트 instanceof L1DollInstance) {
								인형 = (L1DollInstance) 인형오브젝트;
								if (checkItem.getId() == 인형.getItemObjId()) {
									tradable = false;
									pc.sendPackets(new S_ServerMessage(941), true); // 거래 불가 아이템입니다.
								}
							}
						}

						petlist = pc.getPetList().toArray();
						for (Object petObject : petlist) {
							if (petObject instanceof L1PetInstance) {
								L1PetInstance pet = (L1PetInstance) petObject;
								if (checkItem.getId() == pet.getItemObjId()) {
									tradable = false;
									pc.sendPackets(new S_ServerMessage(166, checkItem.getItem().getName(), "\\aH알림: 거래가 불가능 합니다."));
									return;
								}
							}
						}

						if (code == 0x12) {
							if (sellTotalCount > 7) {
								pc.sendPackets(new S_SystemMessage("\\aH알림: 물품은 7개 까지 등록 가능합니다."));
								return;
							}

							L1PrivateShopSellList pssl = new L1PrivateShopSellList();
							pssl.setItemObjectId(sellObjectId);
							pssl.setSellPrice(sellPrice);
							pssl.setSellTotalCount(sellCount);
							sellList.add(pssl);
							sellTotalCount++;
						} else if (code == 0x1a) {
							if (buyTotalCount > 7) {
								pc.sendPackets(new S_SystemMessage("\\aH알림: 물품은 7개 까지 등록 가능합니다."));
								return;
							}
							L1PrivateShopBuyList psbl = new L1PrivateShopBuyList();
							psbl.setItemObjectId(sellObjectId);
							psbl.setBuyPrice(sellPrice);
							psbl.setBuyTotalCount(sellCount);
							buyList.add(psbl);
							buyTotalCount++;
						}
					} else {
						break;
					}
				}

				if (sellTotalCount == 0 && buyTotalCount == 0) {
					pc.sendPackets(new S_ServerMessage(908), true);
					pc.setPrivateShop(false);
					pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle), true);
					Broadcaster.broadcastPacket(pc, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle), true);
					return;
				}

				if (!tradable) { // 거래 불가능한 아이템이 포함되어 있는 경우, 개인 상점 종료
					sellList.clear();
					buyList.clear();
					pc.setPrivateShop(false);
					pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle), true);
					Broadcaster.broadcastPacket(pc, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle), true);
					return;
				}

				/** 수수료 부과 **/
				int shopOpenCount = pc.getNetConnection().getAccount().Shop_open_count;
				if (shopOpenCount >= 40) {
					int OpenAdena = 20000 + ((shopOpenCount - 40) * 1000);
					if (!pc.getInventory().consumeItem(40308, OpenAdena)) {
						sellList.clear();
						buyList.clear();
						pc.setPrivateShop(false);
						pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						Broadcaster.broadcastPacket(pc, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						pc.sendPackets(new S_ServerMessage(189), true); // 아데나가 부족합니다
						return;
					} else {
						pc.getInventory().consumeItem(40308, OpenAdena);
					}
				} else {
					if (!pc.getInventory().consumeItem(40308, 1000)) {
						sellList.clear();
						buyList.clear();
						pc.setPrivateShop(false);
						pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						Broadcaster.broadcastPacket(pc, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						pc.sendPackets(new S_ServerMessage(189), true); // 아데나가 부족합니다
						return;
					} else {
						pc.getInventory().consumeItem(40308, 1000);
					}
				}

				int l1 = readC();
				byte[] chat = readByte(l1);

				readC();
				int l2 = readC();
				String polynum = readS(l2);

				pc.setShopChat(chat);

				pc.setPrivateShop(true);

				pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat), true);
				Broadcaster.broadcastPacket(pc, new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat), true);

				try {
					for (L1PrivateShopSellList pss : pc.getSellList()) {
						int sellp = pss.getSellPrice();
						int sellc = pss.getSellTotalCount();
						sellitem = pc.getInventory().getItem(pss.getItemObjectId());
						if (sellitem == null)
							continue;
						pc.임시SaveShop(pc, sellitem, sellp, sellc, 1);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					for (L1PrivateShopBuyList psb : pc.getBuyList()) {
						int buyp = psb.getBuyPrice();
						int buyc = psb.getBuyTotalCount();
						buyitem = pc.getInventory().getItem(psb.getItemObjectId());
						if (buyitem == null)
							continue;
						pc.임시SaveShop(pc, buyitem, buyp, buyc, 0);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					int polyId = 0;
					if (polynum.equalsIgnoreCase("tradezone1"))
						polyId = 11326;
					else if (polynum.equalsIgnoreCase("tradezone2"))
						polyId = 11427;
					else if (polynum.equalsIgnoreCase("tradezone3"))
						polyId = 10047;
					else if (polynum.equalsIgnoreCase("tradezone4"))
						polyId = 9688;
					else if (polynum.equalsIgnoreCase("tradezone5"))
						polyId = 11322;
					else if (polynum.equalsIgnoreCase("tradezone6"))
						polyId = 10069;
					else if (polynum.equalsIgnoreCase("tradezone7"))
						polyId = 10034;
					else if (polynum.equalsIgnoreCase("tradezone8"))
						polyId = 10032;

					if (polyId != 0) {
						pc.getSkillEffectTimerSet().killSkillEffectTimer(67);
						L1PolyMorph.undoPoly(pc);
						L1ItemInstance weapon = pc.getWeapon();
						if (weapon != null)
							pc.getInventory().setEquipped(weapon, false, false, false);
						pc.getGfxId().setTempCharGfx(polyId);
						pc.sendPackets(new S_ChangeShape(pc.getId(), polyId, pc.getCurrentWeapon()));
						if ((!pc.isGmInvis()) && (!pc.isInvisble())) {
							Broadcaster.broadcastPacket(pc, new S_ChangeShape(pc.getId(), polyId));
						}
						S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc, 0x46);
						pc.sendPackets(charVisual);
						Broadcaster.broadcastPacket(pc, charVisual);
					}
					pc.getNetConnection().getAccount().updateShopOpenCount();
					pc.sendPackets(new S_PacketBox(S_PacketBox.상점개설횟수, pc.getNetConnection().getAccount().Shop_open_count), true);
					pc.sendPackets(new S_ChatPacket(pc, "\\aH알림: .무인상점 입력시 무인상점 진행 ."));
				} catch (Exception e) {
					pc.상점아이템삭제(pc.getId());
					sellList.clear();
					buyList.clear();
					pc.setPrivateShop(false);
					pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle), true);
					Broadcaster.broadcastPacket(pc, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle), true);
					return;
				}
				petlist = null;
			} else if (shoptype == 1) { // 종료
				if (isTwoLogin(pc)) {
					pc.sendPackets(new S_Disconnect());
				}
				sellList.clear();
				buyList.clear();
				pc.setPrivateShop(false);
				pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
				pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
				L1PolyMorph.undoPoly(pc);
				try {
					pc.상점아이템삭제(pc.getId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
			break;

		case Party_Assist: {
			readC();
			readH();
			int Object = read4(read_size());
			if (pc.isInParty()) {
				pc.getParty().updateAssist(pc, Object);
			}
		}
			break;
		case Party_Invitation: {
			readC();
			readH();
			int Type = readC();
			L1PcInstance Player = null;
			if (Type != 2 && Type != 4 && Type != 5 && Type != 7) {
				readC();
				L1Object target = L1World.getInstance().findObject(readBit());
				if (target != null)
					Player = (L1PcInstance) target;
			}
			readC();
			int NameLength = readC();
			if (Player == null) {
				Player = L1World.getInstance().getPlayer(readS(NameLength));
			} else
				readS(NameLength);
			if (Player == null)
				return;
			/** 0번 일반 파티 1번 분배파티 2번 채팅파티 5/초대 이름 6번 표식 7번추방 8번파티위임 */
			if (Type == 0 || Type == 1 || Type == 4 || Type == 5) {
				if (Player instanceof L1PcInstance) {
					if (pc.getId() == Player.getId())
						return;
					if (Player.isInParty()) {
						/** 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다 */
						pc.sendPackets(new S_ServerMessage(415), true);
						return;
					}

					if (pc.isInParty()) {
						if (pc.getParty().isLeader(pc)) {
							Player.setPartyID(pc.getId());
							/** \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N) */
							Player.sendPackets(new S_Message_YN(953, pc.getName()), true);
						} else {
							/** 파티의 리더만을 초대할 수 있습니다. */
							pc.sendPackets(new S_ServerMessage(416), true);
						}
					} else {
						Player.setPartyID(pc.getId());
						switch (Type) {
						case 4:
						case 0:
							pc.setPartyType(0);
							/** \f2%0\f>%s로부터 \fU파티 \f> 에 초대되었습니다. 응합니까? (Y/N) */
							Player.sendPackets(new S_Message_YN(953, pc.getName()), true);
							break;
						case 5:
						case 1:
							pc.setPartyType(1);
							/** \f2%0\f>%s \fU자동분배파티\f> 초대하였습니다. 허락하시겠습니까? (Y/N) */
							Player.sendPackets(new S_Message_YN(954, pc.getName()), true);
							break;
						}
					}
				}
			} else if (Type == 2) { // 채팅 파티
				if (pc.getId() == Player.getId())
					return;
				if (Player.isInChatParty()) {
					/** 벌써 다른 파티에 소속해 있기 (위해)때문에 초대할 수 없습니다 */
					pc.sendPackets(new S_ServerMessage(415), true);
					return;
				}

				if (pc.isInChatParty()) {
					if (pc.getChatParty().isLeader(pc)) {
						Player.setPartyID(pc.getId());
						/** \f2%0\f>%s로부터\fU채팅 파티 \f>에 초대되었습니다. 응합니까? (Y/N) */
						Player.sendPackets(new S_Message_YN(951, pc.getName()), true);
					} else {
						/** 파티의 리더만을 초대할 수 있습니다. */
						pc.sendPackets(new S_ServerMessage(416), true);
					}
				} else {
					Player.setPartyID(pc.getId());
					/** \f2%0\f>%s로부터\fU채팅 파티 \f>에 초대되었습니다. 응합니까? (Y/N) */
					Player.sendPackets(new S_Message_YN(951, pc.getName()), true);
				}
			} else if (Type == 3) { // 위임
				/** 파티장이 아니거나 같은사람이라면 안되도록 하자 */
				if (!pc.getParty().isLeader(pc) || pc.getId() == Player.getId()) {
					return;
				}
				/** 맵이 다르거나 10미터 이하라면 위임 불가능 하도록 하자 */
				/*
				 * if (!isDistance(pc.getX(), pc.getY(), pc.getMapId(), Player.getX(),
				 * Player.getY(), Player.getMapId(), 10)){ pc.sendPackets(new
				 * S_ServerMessage(1695)); return; }
				 */
				pc.getParty().passLeader(Player);
			} else if (Type == 6) {
				readC();
				Player.set표식(readC());
				for (L1PcInstance player : pc.getParty().getMembers()) {
					player.sendPackets(new S_Party(0x6e, Player));
				}
			} else if (Type == 7) { // 추방
				if (!pc.getParty().isLeader(pc)) {
					/** 파티의 리더만을 추방할 수 있습니다. */
					pc.sendPackets(new S_ServerMessage(427));
					return;
				}
				pc.getParty().leaveMember(Player);
			}
		}
			break;
		case SEAL:
			readC();
			readH();
			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(read4(read_size()));
			if (l1iteminstance1.getItem().getType2() == 0) { // etc 아이템이라면
				pc.sendPackets(new S_ServerMessage(79)); // 아무일도 일어나지 않는다 (멘트)
				return;
			}
			if (l1iteminstance1.getBless() == 0 || l1iteminstance1.getBless() == 1 || l1iteminstance1.getBless() == 2 || l1iteminstance1.getBless() == 3) {
				int Bless = 0;
				switch (l1iteminstance1.getBless()) {
				case 0:
					Bless = 128;
					break; // 축
				case 1:
					Bless = 129;
					break; // 보통
				case 2:
					Bless = 130;
					break; // 저주
				case 3:
					Bless = 131;
					break; // 미확인
				}
				l1iteminstance1.setBless(Bless);
				int st = 0;
				if (l1iteminstance1.isIdentified())
					st += 1;
				if (!l1iteminstance1.getItem().isTradable())
					st += 2;
				if (l1iteminstance1.getItem().isCantDelete())
					st += 4;
				if (l1iteminstance1.getItem().get_safeenchant() < 0)
					st += 8;
				if (l1iteminstance1.getBless() >= 128) {
					st = 32;
					if (l1iteminstance1.isIdentified()) {
						st += 15;
					} else {
						st += 14;
					}
				}
				pc.sendPackets(new S_PacketBox(S_PacketBox.ITEM_, l1iteminstance1, st));
				pc.getInventory().updateItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
				pc.getInventory().saveItem(l1iteminstance1, L1PcInventory.COL_IS_ID);
			} else
				pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
			break;
		case DOLL_START: {
			
			int doll_obj_id = 0;
			if(pc.getDollListSize() > 0) {
				for(L1DollInstance doll : pc.getDollList()) {
					doll_obj_id = doll.getId();
					break;
				}
			}
			
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_READY, doll_obj_id));
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_START));
			break;
		}
		case DOLL_RESULT: {
			int total = (readH() - 2) / 12;
			readC();
			int step = readC();
			int[] dollids = new int[total];
			L1ItemInstance item;
			for (int i = 0; i < total; i++) {
				readC();
				readD();
				read4(read_size());
				readC();
				int objid = read4(read_size());

				item = pc.getInventory().getItem(objid);
				if (item == null) {
					return;
				}
				dollids[i] = item.getItemId();
				pc.getInventory().removeItem(item);
			}
			int chance2 = ((total * Config.인형확률2)); // 인형합성 확률 설정
			int chance3 = ((total * Config.인형확률3)); // 인형합성 확률 설정
			int chance4 = ((total * Config.인형확률4)); // 인형합성 확률 설정
			int chance5 = ((total * Config.인형확률5)); // 인형합성 확률 설정
			switch (step) {
			case 1:
				if (CommonUtil.random(100) + 1 <= chance2) {
					dollids = new int[] { 430001, // 장로
							41249, // 서큐버스
							430500, // 코카트리스
							500109, // 눈사람(A) ?
							500108, // 인어
							600242 // 라바골렘
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
					if (step >= 3) {
						try {
							Thread.sleep(10000);
							L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4433, item), true);
						} catch (Exception e) {
						}
					}
				} else {
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, false, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
				}
				break;
			case 2:
				if (CommonUtil.random(100) + 1 <= chance3) {
					dollids = new int[] { 500205, // 서큐 퀸
							500204, // 흑장로
							500203, // 자이언트
							60324, // 드레이크
							500110, // 킹 버그베어
							600243 // 다이아몬드골렘
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
					if (step >= 3) {
						try {
							Thread.sleep(10000);
							// L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(4433,
							// item.getItem().getNameId(), pc.getName()));
							L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4433, item), true);
						} catch (Exception e) {
						}
					}
				} else {
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, false, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
				}
				break;
			case 3:
				if (CommonUtil.random(100) + 1 <= chance4) {
					dollids = new int[] { 500202, // 사이클롭스
							5000035, // 리치
							600245, // 나이트발드
							600244, // 시어
							142920, // 아이리스
							142921, // 뱀파이어
							751 // 머미로드
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
					if (step >= 3) {
						try {
							Thread.sleep(10000);
							L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4433, item), true);
						} catch (Exception e) {
						}
					}
				} else {
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, false, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
				}
				break;
			case 4:// 마지막 나오는 단계
				if (CommonUtil.random(100) + 1 <= chance5) {
					dollids = new int[] { 600246, // 데몬
							600247, // 데스나이트 746
							142922, // 바란카
							752, // 타락
							753, // 바포
							754, // 얼녀
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
					if (step >= 3) {
						try {
							Thread.sleep(10000);
							L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4433, item), true);
						} catch (Exception e) {
						}
					}
				} else {
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, false, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
				}
				break;
			case 5:
				if (CommonUtil.random(100) + 1 <= 50) {
					dollids = new int[] { 600321, // 데몬
							600322, // 데스나이트 746
							600323, // 바란카
							600324, // 타락
							600325, // 바포
							600326, // 얼녀
							600327, // 커츠
							142922, // 바란카
							600327, // 커츠
							142922, // 바란카
							600324, // 타락
							600325, // 바포
							600327, // 커츠
							142922, // 바란카
							600324, // 타락
							600325, // 바포
							600327, // 커츠
							142922, // 바란카
							600324, // 타락
							600325, // 바포
							600327, // 커츠
							142922, // 바란카
							600324, // 타락
							600325, // 바포
							600327, // 커츠
							142922, // 바란카
							600324, // 타락
							600325, // 바포
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), true));
					pc.getInventory().storeItem(item);
					if (step >= 3) {
						try {
							Thread.sleep(10000);
							L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4433, item), true);
						} catch (Exception e) {
						}
					}
				} else {
					dollids = new int[] { 600246, // 데몬
							600247, // 데스나이트 746
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							142922, // 바란카
							752, // 타락
							753, // 바포
							755, // 커츠
							600259, // 안타
							600261, // 린드
							600260, // 파푸
							600262 // 발라
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
					if (step >= 3) {
						try {
							Thread.sleep(10000);
							L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4433, item), true);
						} catch (Exception e) {
						}
					}
				}
				break;
			case 6:
				if (CommonUtil.random(100) + 1 <= 50) {
					dollids = new int[] { 600308, // 축흑장
							600309, // 축자이언트
							600310, // 축서큐
							600311, // 축드레끼
							600312, // 축킹버그
							600313 // 축다이아몬드골렘
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), true));
					pc.getInventory().storeItem(item);
				} else {
					dollids = new int[] { 500205, // 서큐 퀸
							500204, // 흑장로
							500203, // 자이언트
							60324, // 드레이크
							500110, // 킹 버그베어
							600243 // 다이아몬드골렘
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
				}
				break;
			case 7:
				if (CommonUtil.random(100) + 1 <= 40) {
					dollids = new int[] { 600314, // 축리치
							600315, // 축시어
							600316, // 축나발
							600317, // 축시어
							600318, // 축아이리스
							600319, // 축뱀파
							600320 // 축머미
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), true));
					pc.getInventory().storeItem(item);
					if (step >= 3) {
						try {
							Thread.sleep(10000);
							L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4433, item), true);
						} catch (Exception e) {
						}
					}
				} else {
					dollids = new int[] { 500202, // 사이클롭스
							5000035, // 리치
							600245, // 나이트발드
							600244, // 시어
							142920, // 아이리스
							142921, // 뱀파이어
							751, // 머미로드
					};
					item = ItemTable.getInstance().createItem(dollids[CommonUtil.random(dollids.length)]);
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.DOLL_RESULT, true, item.getId(), item.get_gfxid(), false));
					pc.getInventory().storeItem(item);
					if (step >= 3) {
						try {
							Thread.sleep(10000);
							L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4433, item), true);
						} catch (Exception e) {
						}
					}
				}
				break;
			}
		}
			break;
		/** 방 정보 호출 */
		case InDungeon: {
			pc.getInventory().CheckItemSkill();
			pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeon), true);
			break;
		}

		case InDungeStart: {
			readH();
			readC();
			int RoomNumber = read4(read_size());
			DungeonInfo DungeonInfo = DungeonSystem.isDungeonInfo(RoomNumber);

			DungeonInfo.InPlaygame = true;
			pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeStart, 1, RoomNumber), true);
			IND_Q.requestWork(new IND(pc.getName(), DungeonInfo.TypeNumber == 201 ? 3 : 4, DungeonInfo));
			// 리스타트 상태가 아니면 메세지 날리고 리턴
			/*
			 * if (DungeonInfo == null) { pc.sendPackets(new
			 * S_EventNotice(S_EventNotice.InDungeStart, 7, RoomNumber), true); return;
			 * }else if(DungeonInfo.isDungeonInfoCheck()){ pc.sendPackets(new
			 * S_EventNotice(S_EventNotice.InDungeStart, 8, RoomNumber), true); return;
			 * }else if (DungeonInfo.DungeonList.size() < DungeonInfo.MinSize) {
			 * pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeStart, 6, RoomNumber),
			 * true); return; }else{ for(L1PcInstance PcList :
			 * DungeonInfo.isDungeonInfoUset()){
			 * if(!DungeonInfo.isDungeonInfoCheck(PcList)){ pc.sendPackets(new
			 * S_EventNotice(S_EventNotice.InDungeStart, 8, RoomNumber), true); return; } }
			 * DungeonInfo.InPlaygame = true; pc.sendPackets(new
			 * S_EventNotice(S_EventNotice.InDungeStart, 1, RoomNumber), true);
			 * IND_Q.requestWork(new IND(pc.getName(), DungeonInfo.TypeNumber == 201 ? 3 :
			 * 4, DungeonInfo)); }
			 */
			break;
		}

		case InDungeonOut: {
			readH();
			readC();
			int RoomNumber = read4(read_size());
			DungeonInfo DungeonInfo = DungeonSystem.isDungeonInfo(RoomNumber);
			readC();
			L1PcInstance OutUse = DungeonInfo.isDungeonInfoCheckUset(read4(read_size()));
			OutUse.sendPackets(new S_EventNotice(S_EventNotice.InDungeExit, 1, RoomNumber), true);
			OutUse.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOut, OutUse.getId(), RoomNumber), true);
			OutUse.isDungeonTeleport(false);
			DungeonInfo.setUser(pc);

			for (L1PcInstance PcList : DungeonInfo.isDungeonInfoUset()) {
				PcList.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOutUse, DungeonInfo, OutUse), true);
				PcList.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOutExit, DungeonInfo, OutUse), true);
				PcList.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOpen, DungeonInfo, null), true);
			}

			break;
		}

		case InDungeoninvite: {
			int InDungeonTotal = readH();
			int RoomNumber = 0, Object = 0;
			String UseName = null;

			for (int i = 0; i < InDungeonTotal; i++) {
				if (readLength() == 0) {
					break;
				}

				int Type = readC(), Length = 0;

				if (Type == 0x08) {
					RoomNumber = read4(read_size());
				} else if (Type == 0x10) {
					Object = read4(read_size());
				} else if (Type == 0x1a) {
					Length = readC();
					UseName = readS(Length);
				} else {
					break;
				}
			}

			L1PcInstance Player = null, PlayerUse = null;
			L1Object PlayerObject = L1World.getInstance().findObject(Object);

			if (PlayerObject != null) {
				Player = (L1PcInstance) PlayerObject;
			}

			if (UseName != null) {
				PlayerUse = L1World.getInstance().getPlayer(UseName);
			}

			if (Player != null && PlayerUse != null) {
				PlayerUse.sendPackets(new S_EventNotice(S_EventNotice.InDungeoninvite, RoomNumber, Player.getName(), PlayerUse.getName()), true);
			}

			break;
		}

		/** 방 리스트 정리 */
		case InDungeonList: {
			int ListLength = readH();
			readH();
			readC();
			/** 올 정보 갱신 */
			if (ListLength == 2) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonList, 0, 0), true);
				/** 각 클레스 정보 갱신 */
			} else {
				int InDungeonNumber = read4(read_size());
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonList, InDungeonNumber, 0), true);
			}

			break;
		}

		/** 방 계설 정보 */
		case InDungeonType: {
			/** 열쇠가 있는지 체크 부분 열쇠가 없다면 메세지만 출력 */
			if (!pc.getInventory().checkItem(500021, 1)) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonType, 15, 0), true);
				return;
			}

			/** 낚시나 상점 상태라면 리턴 종료 해버리자 */
			if (pc.isPrivateShop() && pc.isFishing()) {
				return;
			}

			readH();
			readC();
			/** 전체 사이즈 계산 */
			int InDungeonTotal = readC();
			DungeonInfo DungeonInfo = new DungeonInfo();
			DungeonInfo.RoomNumber = DungeonSystem.getDungeonInfo();

			for (int i = 0; i < InDungeonTotal; i++) {
				if (readLength() == 0) {
					break;
				}

				int Type = readC(), Length = 0;
				if (Type == 0x0a) {
					Length = readC();
					DungeonInfo.Title = readS(Length);
					Length = Length + 1;
				} else if (Type == 0x10) {
					DungeonInfo.Level = read4(read_size());
					Length = getBitSize(DungeonInfo.Level);
					/** 소비 아데나 */
				} else if (Type == 0x18) {
					DungeonInfo.Adena = read4(read_size());
					Length = getBitSize(DungeonInfo.Adena);
					/** 이건 그냥 기본 0 으로만 뜸 */
				} else if (Type == 0x20) {
					DungeonInfo.Type = read4(read_size()) == 0 ? 1 : 2;
					Length = getBitSize(DungeonInfo.Type);
					/** 자동 분배 */
				} else if (Type == 0x28) {
					DungeonInfo.Division = read4(read_size());
					Length = getBitSize(DungeonInfo.Division);
					/** 공개 비공개 */
				} else if (Type == 0x30) {
					DungeonInfo.Open = read4(read_size());
					Length = getBitSize(DungeonInfo.Open);
					/** 총 인원수 고정 6명 */
				} else if (Type == 0x38) {
					DungeonInfo.MaxSize = read4(read_size());
					DungeonInfo.MinSize = 4;
					Length = getBitSize(DungeonInfo.MaxSize);
					/** 방 비밀 번호 없다면 0 */
				} else if (Type == 0x42) {
					Length = readC();
					DungeonInfo.OpenPassword = readS(Length);
					Length = Length + 1;
					/** 방 타입 (고유 넘버) */
				} else if (Type == 0x48) {
					DungeonInfo.TypeNumber = read4(read_size());
					Length = getBitSize(DungeonInfo.TypeNumber);
				}

				i += Length;
			}

			/** 방 생성시 입장 패킷 에 대한 정보도 같이 갱신 */
			DungeonInfo.setDungeonLeadt(pc.getId());
			DungeonInfo.setDungeonInfoCheck(pc, true);
			DungeonInfo.setUser(pc);
			pc.isDungeonTeleport(true);
			pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonType, 1, DungeonInfo.RoomNumber), true);

			DungeonSystem.getDungeonInfo(DungeonInfo.RoomNumber, DungeonInfo);
			pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOpen, DungeonInfo, null), true);
			pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonSetUp, DungeonInfo, null), true);

			break;
		}

		/** 슬롯 정보 패킷 인듯 */
		case InDungeonSlot: {
			/** 전체 사이즈 계산 */
			int InDungeonTotal = readH();
			DungeonInfo DungeonInfo = null;
			for (int i = 0; i < InDungeonTotal; i++) {

				if (readLength() == 0) {
					break;
				}

				int Type = readC(), Length = 0;

				if (Type == 0x08) {
					int RoomNumber = read4(read_size());
					DungeonInfo = DungeonSystem.isDungeonInfo(RoomNumber);
					Length = getBitSize(RoomNumber);
				} else if (Type == 0x12) {
					Length = readC();
					DungeonInfo.Title = readS(Length);
					Length = Length + 1;
				} else if (Type == 0x18) {
					DungeonInfo.Level = read4(read_size());
					Length = getBitSize(DungeonInfo.Level);
				} else if (Type == 0x20) {
					DungeonInfo.Open = read4(read_size());
					Length = getBitSize(DungeonInfo.Open);
				} else if (Type == 0x2a) {
					Length = readC();
					DungeonInfo.OpenPassword = readS(Length);
					Length = Length + 1;
				} else if (Type == 0x30) {
					DungeonInfo.Division = read4(read_size());
					Length = getBitSize(DungeonInfo.Division);
					/** 총 인원수 고정 6명 */
				} else if (Type == 0x38) {
					DungeonInfo.MaxSize = read4(read_size());
					Length = getBitSize(DungeonInfo.MaxSize);
					/** 방 타입 (고유 넘버) */
				} else if (Type == 0x40) {
					DungeonInfo.TypeNumber = read4(read_size());
					Length = getBitSize(DungeonInfo.TypeNumber);
				}

				i += Length;
			}

			/** 모든케릭터 리스정보 패스 */
			DungeonInfo.isDungeonReady();
			pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonList, 1, DungeonInfo.RoomNumber), true);

			for (L1PcInstance PcList : DungeonInfo.isDungeonInfoUset()) {
				PcList.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOpen, DungeonInfo, null), true);
			}

			break;
		}

		/** 방 입장 정보 */
		/** 51 a5 08 02 00 08 0d d2 */
		case InDungeonAccess: {
			readH();
			readC();
			/** 방 넘버 체크 */
			int RoomNumber = read4(read_size());
			DungeonInfo DungeonInfo = DungeonSystem.isDungeonInfo(RoomNumber);
			pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonSetUp, DungeonInfo, null), true);
		}
			break;
		case PETBALL_CONTENTS_START_REQ: {

			if (pc.getZoneType() != 1) {
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.PETBALL_CONTENTS_START_ACK, 1));
				return;
			}

			pc.setReturnX(pc.getX());
			pc.setReturnY(pc.getY());
			pc.setReturnMap(pc.getMapId());
			L1Teleport.teleport(pc, 32766, 32830, (short) 5167, pc.getHeading(), false);
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.PETBALL_CONTENTS_START_ACK, 0));
		}
			break;
		case PETBALL_CONTENTS_END_REQ: {
			L1Teleport.teleport(pc, pc.getReturnX(), pc.getReturnY(), (short) pc.getReturnMap(), pc.getHeading(), false);
		}
			break;
		case MOVE_SERVER_AUTH_REQ: {
			try {
				L1LoginInfo wli = client.getLoginInfo();
				BinaryOutputStream builder = new BinaryOutputStream();
				builder.writeC(Opcodes.C_LOGOUT);
				builder.writeC(0);
				builder.writeC(0);

				client.packetHandler.handlePacket(builder.getBytes(), null);

				builder.close();

				builder = new BinaryOutputStream();
				builder.writeC(Opcodes.C_LOGIN);
				builder.writeS(wli.getAccount());
				builder.writeS(wli.getPassword());

				client.packetHandler.handlePacket(builder.getBytes(), null);
				builder.close();
			} catch (Exception e) {
				e.printStackTrace();
				client.close();
			}
		}
			break;
		case NP_LOGIN_REQ: {
			try {
				readH(); // 30 00
				readC(); // 08
				readBit(); // c0 d1 e2 09
				readC(); // 10
				readBit(); // 00
				readC(); // 1a
				int size1 = readBit();
				readByte(size1);
				readC(); // 0x22
				int token_size = readBit();
				byte[] auth_token_byte = readByte(token_size); // set_auth_provider
				String auth_token = new String(auth_token_byte, "MS932");
				String _authtoken = auth_token.toUpperCase();

				L1LoginInfo wli = new L1LoginInfo();

				System.out.println("auth_token: " + auth_token);

				if (_authtoken.contains("SHOTGUN")) {
					// 두현팩 동봉 접속기 로그인 구현
					//System.out.println("USER: " + auth_token.split("\\|")[0]);
					//System.out.println("PASSWORD: " + auth_token.split("\\|")[2]);
					wli.setAccount(auth_token.split("\\|")[0]);
					wli.setPassword(auth_token.split("\\|")[2]);
				} else {
					// 게임도리 닉넴 Baron 로컬 접속기 로그인 구현
					//System.out.println(auth_token);
					//wli.setAccount(auth_token.split("	")[0]); // 옛날 초창기 로그인 추가 by feel.
					//wli.setPassword(auth_token.split("	")[1]); // 그시절 다시 기억하며.. 2007~2019
					wli.setAccount(auth_token.split("\\|")[0]);
					wli.setPassword(auth_token.split("\\|")[1]);
				}

				client.setLoginInfo(wli);

				BinaryOutputStream login_c_packet = new BinaryOutputStream();
				login_c_packet.writeC(Opcodes.C_LOGIN);
				login_c_packet.writeS(wli.getAccount());
				login_c_packet.writeS(wli.getPassword());

				client.packetHandler.handlePacket(login_c_packet.getBytes(), null);

				login_c_packet.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;
		/** 51 ac 08 07 00 08 d9 1b 12 00 18 00 */
		case InDungeonStancet: {
			/** 낚시나 상점 상태라면 리턴 종료 해버리자 */
			if (pc.isPrivateShop() && pc.isFishing())
				return;

			/** 인터서버라면 메세지후 입장 불가능하게 정리 */
			if (pc.getNetConnection().getInterServer()) {
				pc.sendPackets(new S_SystemMessage("인터서버(지배의탑,잊혀진섬)내에서는 입장이 불가능합니다."), true);
				return;
				/** 열쇠가 없을시 입장 불가능하게 하루 1회정도만 입장 가능하게 정리 */
			} else if (!pc.getInventory().checkItem(500021, 1)) {
				pc.sendPackets(new S_SystemMessage("인던 열쇠를 소유하지 않아 입장 할수 없습니다."), true);
				return;
			}

			readH();
			readC();
			/** 방 넘버 체크 */
			int RoomNumber = read4(read_size());
			readC();
			int PasswordLength = readC();
			String Password = readS(PasswordLength);

			DungeonInfo DungeonInfo = DungeonSystem.isDungeonInfo(RoomNumber);
			if (DungeonInfo == null) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 7, RoomNumber), true);
				return;
			} else if (DungeonInfo.InPlaygame) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 3, RoomNumber), true);
				return;
			} else if (!pc.getInventory().checkItem(L1ItemId.ADENA, DungeonInfo.Adena)) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 4, RoomNumber), true);
				return;
			} else if (DungeonInfo.Open != 0 && !Password.contentEquals(DungeonInfo.OpenPassword)) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 5, RoomNumber), true);
				return;
			} else if (DungeonInfo.DungeonList.size() >= DungeonInfo.MaxSize) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 8, RoomNumber), true);
				return;
			} else if (DungeonInfo.DungeonList.contains((Object) pc)) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 9, RoomNumber), true);
				return;
			} else if (DungeonSystem.isDungeonInfoCheck(pc)) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 10, RoomNumber), true);
				return;
			} else if (DungeonInfo.Level > pc.getLevel()) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 11, RoomNumber), true);
				return;
			} else if (CharPosUtil.getZoneType(pc) != 1) {
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 12, RoomNumber), true);
				return;
			}

			DungeonInfo.setUser(pc);
			DungeonInfo.setDungeonInfoCheck(pc, false);
			pc.isDungeonTeleport(true);
			pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonStancet, 1, RoomNumber), true);

			for (L1PcInstance PcList : DungeonInfo.isDungeonInfoUset()) {
				PcList.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOpen, DungeonInfo, null), true);
				PcList.sendPackets(new S_EventNotice(S_EventNotice.InDungeonRenewal, DungeonInfo, pc), true);
			}

			break;
		}

		case InDungeonReady: {
			readH();
			readC();
			/** 방 넘버 체크 */
			int RoomNumber = read4(read_size());
			readC();
			DungeonInfo DungeonInfo = DungeonSystem.isDungeonInfo(RoomNumber);

			if (DungeonInfo.isDungeonInfoCheck(pc)) {
				DungeonInfo.setDungeonInfoCheck(pc, false);
			} else {
				DungeonInfo.setDungeonInfoCheck(pc, true);
			}

			for (L1PcInstance PcList : DungeonInfo.isDungeonInfoUset()) {
				PcList.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOpen, DungeonInfo, null), true);
			}

			break;
		}

		case InDungeExit: {
			readH();
			readC();
			int RoomNumber = read4(read_size());
			DungeonInfo DungeonInfo = DungeonSystem.isDungeonInfo(RoomNumber);

			if (DungeonInfo.isDungeonLeadt() == pc.getId()) {
				for (L1PcInstance ListPc : DungeonInfo.isDungeonInfoUset()) {
					ListPc.isDungeonTeleport(false);
					ListPc.sendPackets(new S_EventNotice(S_EventNotice.InDungeExit, 1, RoomNumber), true);
				}

				DungeonSystem.DungeonInfoRemove(RoomNumber);
			} else {
				DungeonInfo.setUser(pc);
				pc.isDungeonTeleport(false);
				pc.sendPackets(new S_EventNotice(S_EventNotice.InDungeExit, 1, RoomNumber), true);

				for (L1PcInstance ListPc : DungeonInfo.isDungeonInfoUset()) {
					ListPc.sendPackets(new S_EventNotice(S_EventNotice.InDungeonOpen, DungeonInfo, null), true);
				}
			}

			break;
		}

		/*
		 * case 초보이동: readD(); // 쓰레기 처리 int a = readC(); if(a==1 && pc.cL==0){
		 * pc.sendPackets(new S_CreateItem(S_CreateItem.퀘스트진행, 1), true);
		 * pc.sendPackets(new S_CreateItem(S_CreateItem.퀘스트진행, 2), true);
		 * pc.sendPackets(new S_CreateItem(S_CreateItem.퀘스트진행, 3), true); } break;
		 * 
		 * case 초보완료: readD(); // 쓰레기 처리 int d = readC(); pc.sendPackets(new
		 * S_CreateItem(S_CreateItem.퀘스트완료, d), true); pc.sendPackets(new
		 * S_OwnCharStatus(pc)); break;
		 * 
		 * case 초보로드: readD(); // 쓰레기 처리 int q = readC(); pc.sendPackets(new
		 * S_CreateItem(S_CreateItem.퀘스트로드, q), true); break;
		 * 
		 * case 초보진행: readD(); // 쓰레기 처리 int m = readC(); pc.sendPackets(new
		 * S_CreateItem(S_CreateItem.퀘스트진행, m), true); break;
		 */
		case RevengeBoard: {
			readH();
			readC();
			CharcterRevengeTable revenge = CharcterRevengeTable.getInstance();
			ArrayList<Integer> objid = revenge.GetRevengeObj(pc.getId());

			if (objid.size() < 1) {
				return;
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(RevengeTime)) {
				int targetId = revenge.LoadChaserTarGet(pc.getId());
				L1Object target = L1World.getInstance().findObject(targetId);

				if (target != null && target instanceof L1PcInstance) {
					L1PcInstance Targetpc = (L1PcInstance) target;
					pc.sendPackets(new S_Revenge(S_Revenge.Revenge_Chase_Loc, Targetpc), true);
				} else {
					pc.sendPackets(new S_Revenge(S_Revenge.Revenge_List_Add, pc), true);
				}
			}

			pc.sendPackets(new S_Revenge(S_Revenge.Revenge_All_List, pc), true);
			break;
		}
		case RevengeProvoke: {
			readD();
			readC();
			int NameLength = readC();
			String Name = readS(NameLength);
			CharcterRevengeTable revenge = CharcterRevengeTable.getInstance();

			if (Name == null) {
				return;
			} else {
				revenge.UpdateRemainCount(pc.getId(), Name);
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, pc.getName() + "님이 " + Name + "님을 도발하였습니다"), true);
			}

			pc.sendPackets(new S_Revenge(S_Revenge.Revenge_List_Provoke, pc), true);

			break;
		}
		case RevengeChase: {
			readD();
			readH();
			readC();
			int NameLength = readC();
			String Name = readS(NameLength);
			CharcterRevengeTable revenge = CharcterRevengeTable.getInstance();
			String OBJT = revenge.TarObjFind(Name);
			int targetId = Integer.parseInt(OBJT);
			L1Object target = L1World.getInstance().findObject(targetId);

			if (target == null) {
				return;
			}

			if (!pc.getInventory().checkItem(40308, 10000)) {
				pc.sendPackets(new S_SystemMessage("아데나가  부족합니다."), true);
				return;
			}

			if (target != null && target instanceof L1PcInstance) {
				L1PcInstance Targetpc = (L1PcInstance) target;
				if (revenge.RemainCounter(pc.getId(), Targetpc.getId()) > 0) {
					pc.getInventory().removeItem(40308, 10000);
					revenge.UpdateRemainCount(pc.getId(), Targetpc.getId());
					pc.getSkillEffectTimerSet().setSkillEffect(RevengeTime, 600 * 1000);
					revenge.StoreChaserTime(pc.getId(), target.getId(), 600);
					pc.sendPackets(new S_Revenge(S_Revenge.Revenge_Chase_Loc, Targetpc), true);
				} else {
					return;
				}
			}

			pc.sendPackets(new S_Revenge(S_Revenge.Revenge_All_List, pc), true);

			break;
		}
		case RevengeChase2: {
			readD();
			readH();
			readC();
			int NameLength = readC();
			String Name = readS(NameLength);
			CharcterRevengeTable revenge = CharcterRevengeTable.getInstance();
			String OBJT = revenge.TarObjFind(Name);
			int targetId = Integer.parseInt(OBJT);
			L1Object target = L1World.getInstance().findObject(targetId);

			if (target == null) {
				return;
			}

			if (target != null && target instanceof L1PcInstance && pc.getSkillEffectTimerSet().hasSkillEffect(RevengeTime)) {
				L1PcInstance Targetpc = (L1PcInstance) target;
				pc.sendPackets(new S_Revenge(S_Revenge.Revenge_Chase_MinMap, Targetpc), true);
				revenge.StoreChaserTime(pc.getId(), target.getId(), pc.getSkillEffectTimerSet().getSkillEffectTimeSec(80020));
			}

			break;
		}
		case BMTYPE_DEL_CHECK_REQ: {
			int delete_result = eCharacterDeleteResult.Success.toInt();
			try {
				readH();
				readC();
				int name_size = readBit();
				byte[] name_byte = readByte(name_size); // set_auth_provider
				String name = new String(name_byte, "MS932");

				pc = CharacterTable.getInstance().restoreCharacter(name);

				if (pc == null) {
					client.sendPacket(new S_Notice("존재하지 않는 캐릭터 입니다."));
					delete_result = eCharacterDeleteResult.BMTypeFail.toInt();
					client.sendPacket(new S_ACTION_UI(S_ACTION_UI.BMTYPE_DEL_CHECK_ACK, delete_result));
					return;
				}

				for (L1PcInstance target : L1World.getInstance().getAllPlayers3()) {
					if (target.getId() == pc.getId()) {
						client.sendPacket(new S_Notice("접속 중인 캐릭터는 삭제할 수 없습니다."));
						delete_result = eCharacterDeleteResult.BMTypeFail.toInt();
						client.sendPacket(new S_ACTION_UI(S_ACTION_UI.BMTYPE_DEL_CHECK_ACK, delete_result));
						return;
					}
				}

				client.sendPacket(new S_ACTION_UI(S_ACTION_UI.BMTYPE_DEL_CHECK_ACK, delete_result));
			} catch (Exception e) {
				delete_result = eCharacterDeleteResult.InvalidPacket.toInt();
				client.sendPacket(new S_ACTION_UI(S_ACTION_UI.BMTYPE_DEL_CHECK_ACK, delete_result));
			}
		}
			break;
		case MONSTERBOOK_CLEAR:
			readH();
			readC();
			int monNum = read4(read_size());
			int value = 0;

			if (monNum == 1) {
				value = 1;
			} else if (monNum == 2) {
				value = 2;
			}

			if (monNum >= 3) {
				value = monNum % 3;
			}

			switch (value) {// 도감 1~3단계별로 아이템지급
			case 1:
				pc.addExp(50000);
				pc.getInventory().storeItem(60765, 10);
				L1Cooking.newEatCooking(pc, L1SkillId.천하장사버프, 1800);
				break;
			case 2:
				pc.getInventory().storeItem(60765, 20);
				L1Cooking.newEatCooking(pc, L1SkillId.천하장사버프, 1800);
				pc.addExp(500000);
				break;
			case 3:
				pc.getInventory().storeItem(60765, 30);
				L1Cooking.newEatCooking(pc, L1SkillId.천하장사버프, 1800);
				pc.addExp(5000000);
				break;
			}

			pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_END, monNum));
			MonsterBookTable.getInstace().setMon_Quest(pc.getId(), monNum, 1);
			MonsterBookTable.getInstace().saveMonsterQuest(pc.getId());
			pc.sendPackets(new S_SkillSound(pc.getId(), 3944), true);
			Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 3944), true);

			break;

		case MONSTERBOOK_TEL:
			readH();
			readC();
			int monsternumber = read4(read_size()) / 3 + 1;
			MonsterBookTable Mui_gi = MonsterBookTable.getInstace();
			int mn = Mui_gi.getMonNum(monsternumber);

			if (mn != 0 && mn <= 557 && pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
				int itemId = Mui_gi.getMarterial(monsternumber);
				String itemName = ItemTable.getInstance().findItemIdByName(itemId);

				if (itemName != null) {
					int locx = Mui_gi.getLocX(monsternumber);
					int locy = Mui_gi.getLocY(monsternumber);
					int mapid = Mui_gi.getMapId(monsternumber);

					if (pc.getMap().isEscapable()) {
						if (pc.getInventory().consumeItem(itemId, 1)) {
							pc.sendPackets(new S_SystemMessage("5초후 텔레포트됩니다."), true);
							L1Teleport.teleport(pc, locx, locy, (short) mapid, 5, true);
						} else {
							pc.sendPackets(new S_ServerMessage(4692, itemName));
							return;
						}
					} else {
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
						pc.sendPackets(new S_ServerMessage(4726));
					}
				}
			} else if (mn >= 558 && mn <= 609 && pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
				int itemId = Mui_gi.getMarterial(monsternumber - 10);
				String itemName = ItemTable.getInstance().findItemIdByName(itemId);

				if (itemName != null) {
					int locx = Mui_gi.getLocX(monsternumber - 10);
					int locy = Mui_gi.getLocY(monsternumber - 10);
					int mapid = Mui_gi.getMapId(monsternumber - 10);

					if (pc.getMap().isEscapable()) {
						if (pc.getInventory().consumeItem(itemId, 1)) {
							pc.sendPackets(new S_SystemMessage("5초후 텔레포트됩니다."), true);
							L1Teleport.teleport(pc, locx, locy, (short) mapid, 5, true);
						} else {
							pc.sendPackets(new S_ServerMessage(4692, itemName));
							return;
						}
					} else {
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
						pc.sendPackets(new S_ServerMessage(4726));
					}
				}
			} else if (mn >= 610 && pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
				int itemId = Mui_gi.getMarterial(monsternumber - 11);
				String itemName = ItemTable.getInstance().findItemIdByName(itemId);

				if (itemName != null) {
					int locx = Mui_gi.getLocX(monsternumber - 11);
					int locy = Mui_gi.getLocY(monsternumber - 11);
					int mapid = Mui_gi.getMapId(monsternumber - 11);

					if (pc.getMap().isEscapable()) {
						if (pc.getInventory().consumeItem(itemId, 1)) {
							pc.sendPackets(new S_SystemMessage("5초후 텔레포트됩니다."), true);
							L1Teleport.teleport(pc, locx, locy, (short) mapid, 5, true);
						} else {
							pc.sendPackets(new S_ServerMessage(4692, itemName));
							return;
						}
					} else {
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
						pc.sendPackets(new S_ServerMessage(4726));
					}
				}
			} else {
				pc.sendPackets(new S_SystemMessage("\\aH알림: SafetyZone에서만 이용 가능합니다 ."), true);
			}

			break;

		case BOOKWEEKQUEST_TEL:
			if (pc.getWeekQuest() != null) {
				readH(); // 0x04, 0x00
				readC(); // 0x08,
				int difficulty = readC();
				readC(); // 0x10,
				int section = readC();
				pc.getWeekQuest().teleport(difficulty, section);
			}

			break;

		case BOOKWEEKQUEST_CLEAR:
			if (pc.getWeekQuest() != null) {
				readH(); // 0x04, 0x00
				readC(); // 0x08,
				int difficulty = readC();
				readC(); // 0x10,
				int section = readC();
				pc.getWeekQuest().complete(difficulty, section);
			}

			break;

		case EventNotice: {
			readH(); // size;
			readC(); // dummy
			int Action = readC();

			/** 지엠룸 감옥등 곳에서는 사용불가하도록 세팅 */
			if (pc.getMapId() == 99 || (pc.getMapId() >= 2236 && pc.getMapId() <= 2237)) {
				pc.sendPackets(new S_SystemMessage("이곳에서는 사용할수 없습니다."), true);
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
				return;
			}

			/** 귀환이 불가능한 지역에서는 귀환안되도록 세팅 하긔! */
			if (!pc.getMap().isEscapable()) {
				pc.sendPackets(new S_SystemMessage("이곳에서는 사용할수 없습니다."), true);
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
				return;
			}

			EventNotice Notice = RotationNoticeTable.getInstance().getRotation(Action);
			int locx = Notice.Teleport[0], locy = Notice.Teleport[1], mapid = Notice.Teleport[2], Adena = Notice.Teleport[3];
			if (Action == 20) {
				if (!pc.getInventory().checkItem(60384) && pc.getQuest().get_step(L1Quest.QUEST_55_Roon) == 0) {
					locx = 32882;
					locy = 32783;
					mapid = 4;
					Adena = 10000;
				} else if (!pc.getInventory().checkItem(60391) && pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 0 && pc.getLevel() >= 70) {
					locx = 32869;
					locy = 32797;
					mapid = 4;
					Adena = 10000;
				} else if (!pc.getInventory().checkItem(60398) && pc.getQuest().get_step(L1Quest.QUEST_80_Roon) == 0 && pc.getLevel() >= 80) {
					locx = 32869;
					locy = 32797;
					mapid = 4;
					Adena = 10000;
				} else if (!pc.getInventory().checkItem(60405) && pc.getQuest().get_step(L1Quest.QUEST_85_Roon) == 0 && pc.getLevel() >= 85) {
					locx = 32869;
					locy = 32797;
					mapid = 4;
					Adena = 10000;
				} else if (!pc.getInventory().checkItem(60412) && pc.getQuest().get_step(L1Quest.QUEST_90_Roon) == 0 && pc.getLevel() >= 90) {
					locx = 32869;
					locy = 32797;
					mapid = 4;
					Adena = 10000;
				}
			}

			L1Location loc = new L1Location(locx, locy, (short) mapid).randomLocation(Action == 16 ? 10 : 5, true);
			/** 아데나 체크후에 아데나가있다면 남은 시간 체크해서 시간이 남음 텔 가능하도록 한다 */
			if (!pc.getInventory().checkItem(40308, Adena)) {
				pc.sendPackets(new S_SystemMessage("아데나가  부족합니다."), true);
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
				return;
			}

			pc.getInventory().consumeItem(40308, Adena);
			pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
			L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 5, true, true, 5000);
			break;
		}

		/** 펫 스킬 관련 일단 임시로 작업 */
		case PetSkill: {
			readH(); // size;
			readC(); // dummy
			/** 스킬 넘버 받아옴 기본적으로 */
			int NameNumber = readC();
			L1PetInstance Pet = (L1PetInstance) pc.getPet();
			/** 확율 아이템 모두 삭제 해주고 정리해줘야 하는데 일단 패스 */
			if (Pet != null) {
				/**
				 * 스킬 사용 가능한 패킷 처리 해야됨 임시로 새팅
				 */
				int SkillLevel = 0;
				int[] Amount = null;
				if (NameNumber == 1 || NameNumber == 7 || NameNumber == 15 || NameNumber == 18 || NameNumber == 19 || NameNumber == 20 || NameNumber == 21
						|| NameNumber == 22) {
					SkillLevel = 1;
					Amount = new int[] { 3, 4, 5, 7, 10, 15, 25, 50, 100, 200 };
				} else if (NameNumber == 2 || NameNumber == 8 || NameNumber == 16) {
					SkillLevel = 2;
					Amount = new int[] { 13, 24, 35, 47, 60, 75, 95, 130, 190, 300 };
				} else if (NameNumber == 24 || NameNumber == 14 || NameNumber == 12 || NameNumber == 23 || NameNumber == 10 || NameNumber == 17) {
					SkillLevel = 3;
					Amount = new int[] { 28, 54, 80, 107, 135, 165, 200, 250, 325, 450 };
				} else if (NameNumber == 4 || NameNumber == 9 || NameNumber == 11 || NameNumber == 5 || NameNumber == 13) {
					SkillLevel = 4;
					Amount = new int[] { 48, 94, 140, 187, 235, 285, 340, 410, 505, 650 };
				} else if (NameNumber == 3) {
					SkillLevel = 5;
					Amount = new int[] { 73, 144, 215, 287, 360, 435, 515, 610, 730, 900 };
				}

				L1PetSkill PetSkill = Pet.getPetSkills(NameNumber);
				if (PetSkill != null && Pet.getFriendship() >= Amount[PetSkill.getSkillLevel()] && pc.getInventory().consumeItem(43204, 1)) {
					Pet.setFriendship(Pet.getFriendship() - Amount[PetSkill.getSkillLevel()]);
					pc.sendPackets(new S_PetWindow(S_PetWindow.Friendship, Pet), true);
					/** 스킬 성공 실패 확율 처리 */
					int Chance = _Random.nextInt(100) + 1;
					/** 높아서 성공하였다면 스킬 정보 패킷 처리 해서 완료 시켜준다 */
					if (50 - (PetSkill.getSkillLevel() * SkillLevel) >= Chance) {
						PetSkill.setSkillLevel(PetSkill.getSkillLevel() + 1);
						/** npc정보 패킷 처리로 변환하여 도록 메소드를 하나 더 만들어서 돌리자 */
						Pet.SkillsUpdate(PetSkill.getSkillNumber(), PetSkill.getSkillLevel());
						pc.sendPackets(new S_PetWindow(PetSkill), true);
						pc.sendPackets(new S_PetWindow(NameNumber, true), true);
						/** 디비 저장 시스템 */
						PetsSkillsTable.SaveSkills(Pet, false);
					} else {
						pc.sendPackets(new S_PetWindow(NameNumber, false), true);
					}
				} else {
					pc.sendPackets(new S_PetWindow(NameNumber, false), true);
				}

				/** 변수들 초기화 시킬때 사용 */
				Arrays.fill(Amount, 0);
			}

			break;
		}

		/** 펫 관련 스킬 레벨 체크 */
		case PetSkillLevel: {
			/** 스킬 단계 계산해서 1단계인지 2단계인지 체크해야됨 */
			/** 1단계가 모두 완료고 레벨이 맞는지 체크해서 2단계로 넘겨줄수잇도록 새팅 */
			L1PetInstance Pet = (L1PetInstance) pc.getPet();
			/** 각 스킬 참인지 거짓인지 체크 */
			if (Pet != null) {
				if (Pet.getPetSkills(Pet.getPetType().getSkillOneStep())) {
					/** 메세지 링크 */
					pc.sendPackets(new S_ServerMessage(5314), true);
					/** 기본 스킬 배워지도록 */
					Pet.addPetSkills(Pet.getPetType().getSkillOneStep());
					pc.sendPackets(new S_PetWindow(Pet.ArrayPetSkills()), true);
					/** 디비 저장t 시스템 */
					PetsSkillsTable.SaveSkills(Pet, false);
				} else if (Pet.getPetSkills(Pet.getPetType().getSkillTwoStep())) {
					/** 아데나가 없다면 리턴 */
					if (!pc.getInventory().consumeItem(40308, 100000)) {
						pc.sendPackets(new S_SystemMessage("아데나가 부족하여 2단계 야성오픈을 할수없습니다."), true);
						return;
					}

					/** 메세지 링크 */
					pc.sendPackets(new S_ServerMessage(5314), true);
					/** 기본 스킬 배워지도록 */
					Pet.addPetSkills(Pet.getPetType().getSkillTwoStep());
					pc.sendPackets(new S_PetWindow(Pet.ArrayPetSkills()), true);
					/** 디비 저장t 시스템 */
					PetsSkillsTable.SaveSkills(Pet, false);
				} else if (Pet.getPetSkills(Pet.getPetType().getSkillThreeStep())) {
					/** 아데나가 없다면 리턴 */
					if (!pc.getInventory().consumeItem(40308, 500000)) {
						pc.sendPackets(new S_SystemMessage("아데나가 부족하여 3단계 야성오픈을 할수없습니다."), true);
						return;
					}

					/** 메세지 링크 */
					pc.sendPackets(new S_ServerMessage(5314), true);
					/** 기본 스킬 배워지도록 */
					Pet.addPetSkills(Pet.getPetType().getSkillThreeStep());
					pc.sendPackets(new S_PetWindow(Pet.ArrayPetSkills()), true);
					/** 디비 저장t 시스템 */
					PetsSkillsTable.SaveSkills(Pet, false);
				} else if (Pet.getPetSkills(Pet.getPetType().getSkillFourStep())) {
					/** 아데나가 없다면 리턴 */
					if (!pc.getInventory().consumeItem(40308, 1000000)) {
						pc.sendPackets(new S_SystemMessage("아데나가 부족하여 4단계 야성오픈을 할수없습니다."), true);
						return;
					}

					/** 메세지 링크 */
					pc.sendPackets(new S_ServerMessage(5314), true);
					/** 기본 스킬 배워지도록 */
					Pet.addPetSkills(Pet.getPetType().getSkillFourStep());
					pc.sendPackets(new S_PetWindow(Pet.ArrayPetSkills()), true);
					/** 디비 저장t 시스템 */
					PetsSkillsTable.SaveSkills(Pet, false);
				} else if (Pet.getPetSkills(Pet.getPetType().getSkillFiveStep())) {
					/** 아데나가 없다면 리턴 */
					if (!pc.getInventory().consumeItem(40308, 5000000)) {
						pc.sendPackets(new S_SystemMessage("아데나가 부족하여 5단계 야성오픈을 할수없습니다."), true);
						return;
					}

					/** 메세지 링크 */
					pc.sendPackets(new S_ServerMessage(5314), true);
					/** 기본 스킬 배워지도록 */
					Pet.addPetSkills(Pet.getPetType().getSkillFiveStep());
					pc.sendPackets(new S_PetWindow(Pet.ArrayPetSkills()), true);
					/** 디비 저장t 시스템 */
					PetsSkillsTable.SaveSkills(Pet, false);
				} else {
					pc.sendPackets(new S_SystemMessage("스킬을 배울수 없습니다."), true);
				}
			}

			break;
		}

		case PetName: {
			readH(); // size;
			readC(); // size;
			int NameLength = readC();
			String Name = readS(NameLength);
			L1PetInstance Pet = (L1PetInstance) pc.getPet();
			if (Pet != null) {
				if (Pet.isInvalidName(Name)) {
					pc.sendPackets(new S_SystemMessage("사용할수 없는 이름 입니다."), true);
					return;
				}

				/** 같은 이름의 펫은 사용불가 */
				if (PetTable.isNameExists(Name)) {
					pc.sendPackets(new S_ServerMessage(327), true);
					return;
				}

				Pet.PetName(Name);
			}

			break;
		}

		case Petstat: {
			readH();
			int Temp[] = new int[3];
			L1PetInstance Pet = (L1PetInstance) pc.getPet();

			if (Pet != null && Pet.getBonusPoint() > 0) {
				for (int i = 0; i < Temp.length; i++) {
					readC();
					Temp[i] = readC();
				}

				Pet.BonusPoint(Temp);
			}

			/** 변수들 초기화 시킬때 사용 */
			Arrays.fill(Temp, 0);
			break;
		}

		case PetCommand: {
			readH();
			readC();
			int Command = readC();
			L1PetInstance Pet = (L1PetInstance) pc.getPet();
			if (Pet != null) {
				switch (Command) {
				case 2:
					/** 공격 태세 */
					Pet.onFinalAction(1, null);
					break;

				case 3:
					/** 방어 태세 */
					Pet.onFinalAction(2, null);
					break;

				case 6:
					/** 수집 상태 */
					Pet.onFinalAction(8, null);
					break;

				case 7:
					/** 대상 공격 */
					readC();
					L1Object obj = L1World.getInstance().findObject(read4(read_size()));
					Pet.onFinalAction(9, obj);
					break;

				case 9:
					/** 태각 모든정보 리셋 */
					Pet.onFinalAction(10, null);
					break;

				case 101:
					/** 펫 액션 관련 패킷 처리인듯 */
					Pet.onFinalAction(67, null);
					break;
				}

			}

			break;
		}

		case CLAN_BLESS_HUNT_ALL_CHANGE: {
			if (pc.getClan() == null) {
				return;
			}

			if (pc.getClanRank() != L1Clan.CLAN_RANK_PRINCE && pc.getClanRank() != L1Clan.CLAN_RANK_GUARDIAN) {
				pc.sendPackets(new S_SystemMessage("혈맹의 군주나 수호기사만 사용가능합니다."));
				return;
			}

			if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 300000)) {
				pc.sendPackets(new S_ServerMessage(189));
				return;
			}

			ClanBlessHuntInfo.getInstance().settingClanBlessHuntMaps(pc.getClan());
			ClanTable.getInstance().updateClanBlessHunt(pc.getClan());
			pc.sendPackets(new S_ClanBlessHuntUi(S_ClanBlessHuntUi.CLAN_BLESS_HUNT_TELEPORT, pc.getClan()));

			break;
		}

		case CLAN_BLESS_HUNT_CHANGE: {
			if (pc.getClan() == null) {
				return;
			}

			/*
			 * if (pc.getClanRank() != L1Clan.CLAN_RANK_PRINCE && pc.getClanRank()
			 * !=L1Clan.CLAN_RANK_GUARDIAN) { pc.sendPackets(new
			 * S_SystemMessage("혈맹의 군주나 수호기사만 사용가능합니다.")); return; }
			 */
			if (pc.getClan().isHuntMapChoice()) {
				if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 1000)) {
					pc.sendPackets(new S_ServerMessage(189));
					return;
				}
			}

			readH();
			readC();
			readBit();
			readC();
			int mapNumber = readBit();
			readC();
			int code = readBit();
			ArrayList<Integer> mapList = ClanBlessHuntInfo.getInstance().getMapList(mapNumber);

			if (code == 1 || code == 3) {
				for (int i = 0; i < pc.getClan().getBlessHuntMaps().size(); i++) {
					if (pc.getClan().getBlessHuntMaps().get(i) == mapNumber) {
						pc.getClan().getBlessHuntMapsType().set(i, 2);
						pc.getClan().setBlessHuntMapIds(mapList);
					} else {
						pc.getClan().getBlessHuntMapsType().set(i, 3);
					}
				}
				pc.sendPackets(new S_ClanBlessHuntUi(S_ClanBlessHuntUi.CLAN_BLESS_HUNT_TELEPORT, pc.getClan()));
				pc.getClan().setHuntMapChoice(true);
				ClanTable.getInstance().updateClanBlessHunt(pc.getClan());
			} else {
				HuntInfo hi = ClanBlessHuntInfo.getInstance().getClanBlessHuntInfo(mapNumber);
				if (hi != null) {
					L1Teleport.teleport(pc, hi.getTeleportLocX(), hi.getTeleportLocY(), (short) hi.getTeleportMapId(), pc.getHeading(), true);
				}
			}

			break;
		}

		case CRAFT_ITEM: // 제작 시스템
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.CRAFT_ITEM));
			break;

		case CRAFT_ITEMLIST: // 0x38
			readH(); // size;
			readC(); // dummy
			int objectId = read4(read_size());
			L1Object obj = L1World.getInstance().findObject(objectId);

			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				int[] craftList = CraftInfoTable.getIns().getCraftNpc(npc.getNpcId());
				if (craftList != null && craftList.length != 0) {
					pc.sendPackets(new S_ACTION_UI(craftList));
				} else {
					pc.sendPackets(new S_ACTION_UI(npc));
				}
			}

			break;

		case 92: {
			if (pc.getInventory().calcWeightpercent() >= 90) {
				pc.sendPackets(new S_SystemMessage("제작 실패: 무게 게이지 90% 이상 제작 불가."));
				return;
			}

			int len = readC() - 3;
			for (int i = 0; i < len; i++) {
				readC();
			}

			pc.sendPackets(new S_NewCreateItem(0X5D, "08 00 10 e3 03 56 ce"));// 1479

			break;
		}

		case CRAFT_OK: {// 0x3a
			try {

				if (pc.getInventory().calcWeightpercent() >= 90) {
					pc.sendPackets(new S_SystemMessage("제작 실패: 무게 게이지 90% 이상 제작 불가."));
					return;
				}

				// 여기서 모든패킷을 한번 읽어드린다. 그리고 필요한 자료 갯수 여러정보를 가공한다 가져온다.
				if (!readCraftInfo(pc)) {
					craftErrMsg(pc, craftId, "!readCraftInfo(pc)");
					return;
				}

				if (materialDescIdList == null) {
					craftErrMsg(pc, craftId, "materialDescIdList == null");
					return;
				}

				L1CraftInfo ci = CraftInfoTable.getIns().getCraftInfo(craftId);
				if (ci == null) {
					craftErrMsg(pc, craftId, "ci == null");
					return;
				}

				L1Item item = ItemTable.getInstance().findDescId(ci.descId, ci.bless);
				if (item == null) {
					craftErrMsg(pc, craftId, "temp == null");
					return;
				}

				ArrayList<Material> materialList = ci.materialList;
				if (materialList == null) {
					craftErrMsg(pc, craftId, "materialList == null");
					return;
				}

				if (materialList.size() != materialDescIdList.size() && craftId != 3667 && craftId != 3599 && craftId != 6134 && craftId != 5913
						&& craftId != 5914 && craftId != 5915 && craftId != 5916 && craftId != 5917 // 아인하사드의 섬광
						&& craftId != 5918 && craftId != 5919 && craftId != 5920 && craftId != 5921 && craftId != 5922 // 아인하사드의 섬광
						&& craftId != 5933 && craftId != 5934 && craftId != 5935 && craftId != 5936 && craftId != 5937 // 그랑카인의 분노
						&& craftId != 5938 && craftId != 5939 && craftId != 5940 && craftId != 5941 && craftId != 5942 // 그랑카인의 분노
						&& craftId != 79 && craftId != 80 && craftId != 81 && craftId != 82 && craftId != 83 // 발라,파푸,린드,안타, 갑옷류
						&& craftId != 84 && craftId != 85 && craftId != 86 && craftId != 87 && craftId != 88 // 발라,파푸,린드,안타, 갑옷류
						&& craftId != 89 && craftId != 90 && craftId != 91 && craftId != 92 && craftId != 93 && craftId != 94) { // 발라,파푸,린드,안타, 갑옷류
					craftErrMsg(pc, craftId, "materialList.size() != materialDescIdList.size()");
					return;
				}

				HashMap<L1ItemInstance, Integer> deleteItemIntArrayList = new HashMap<L1ItemInstance, Integer>(); // 아이템,삭제할수
				L1Inventory pcIv = pc.getInventory();

				for (Material material : materialList) { // 하나의 재료아이템
					if (material == null) {
						craftErrMsg(pc, craftId, "material == null");
						return;
					}

					int[] descIds = material.descIds;
					if (descIds == null) {
						craftErrMsg(pc, craftId, "descIds == null");
						return;
					}

					if (material.enchant == null) {
						craftErrMsg(pc, craftId, "material.enchant == null");
						return;
					}

					if (material.count == null) {
						craftErrMsg(pc, craftId, "material.count == null");
						return;
					}

					if (material.bless == null) {
						craftErrMsg(pc, craftId, "material.bless == null");
						return;
					}

					boolean materialCk = false;
					for (int index = 0; index < descIds.length; index++) { // 하나의 재료아이템 대체아이템까지 검색.
						int descId = descIds[index];

						if (materialDescIdList.get(descId) == null) {
							continue;
						}

						materialCk = true;
						int enchant = materialDescIdList.get(descId);

						if (enchant != material.enchant[index]) {
							craftErrMsg(pc, craftId, "enchant != material.enchant[index]");
							return;
						}

						int count = material.count[index] * createItemCount;
						// int bless = material.bless[index];
						L1Item item1 = ItemTable.getInstance().getTemplateByDescId(descId);

						if (item1.isStackable()) { // 아이템이 스택커블일경우
							L1ItemInstance deletItemCk = pcIv.findCraftMaterialItem(descId, count);
							if (deletItemCk == null) {
								craftErrMsg(pc, craftId, "1deletItemCk == null");
								return;
							}

							if (deleteItemIntArrayList.containsKey(deletItemCk) && craftId != 3667 && craftId != 3599) {
								craftErrMsg(pc, craftId, "deleteItemIntArrayList.containsKey(deletItemCk)");
								return;
							}

							deleteItemIntArrayList.put(deletItemCk, count);
							break;
						} else { // 일반 아이템일경우
							int ckCount = 0;
							for (L1ItemInstance itemIns : pcIv.getItems()) {

								if (ckCount >= count) {
									break;
								}

								if (itemIns != null && !itemIns.isEquipped() && itemIns.getItem().getItemDescId() == descId
										&& itemIns.getEnchantLevel() == enchant) {
									if (deleteItemIntArrayList.containsKey(itemIns)) {
										craftErrMsg(pc, craftId, "deleteItemIntArrayList.containsKey(itemIns)");
										return;
									}

									deleteItemIntArrayList.put(itemIns, 1);
									ckCount++;
								}
							}

							if (ckCount < count) {
								craftErrMsg(pc, craftId, "ckCount < count");
								return;
							} else {
								break;
							}
						}
					}

					if (!materialCk) {
						craftErrMsg(pc, craftId, "!materialCk");
						return;
					}
				}

				int probability = ci.probability;
				boolean success = true;

				if (probability != 0) {
					if (_Random.nextInt(100) > probability) {
						success = false;
					}
				}

				int deleteItemId = 0;
				for (L1ItemInstance itemIns : deleteItemIntArrayList.keySet()) {
					if (itemIns == null) {
						craftErrMsg(pc, craftId, "itemIns == null");
						return;
					}
					if (!pcIv.consumeItem(itemIns, deleteItemIntArrayList.get(itemIns))) { // item, count
						craftErrMsg(pc, craftId, "!pcIv.consumeItem(itemIns, deleteItemIntArrayList.get(itemIns))");
						return;
					}
				}

				if (success) {
					L1ItemInstance createItem = null;

					for (int itemId : bigSuccessItem) {
						if (itemId == item.getItemId()) {
							if (_Random.nextInt(100) < 100) { // 10% 확률로 대성공아이템.
								createItem = pcIv.storeItem2(item.getItemId(), ci.makeCount * createItemCount, ci.enchant, ci.bless, ci.attr);
								createItem.setBless(0);
								L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 3599, createItem), true);
								commit("대성공 아이템 제작^금속테이블 : " + item.getName(), "", 1);
								pc.sendPackets(new S_SkillSound(pc.getId(), 7976));
								pc.broadcastPacket(new S_SkillSound(pc.getId(), 7976));
								pc.sendPackets(new S_SkillSound(pc.getId(), 2047));
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 2047));
							}

							break;
						}
					}

					if (createItem == null) {
						createItem = pcIv.storeItem2(item.getItemId(), ci.makeCount * createItemCount, ci.enchant, ci.bless, ci.attr);
					}

					pc.sendPackets(new S_ServerMessage(403, createItem.getName()));
					pc.sendPackets(new S_NewCreateItem(0X3b, ADD)); // 성공 멘트
					pc.sendPackets(new S_SkillSound(pc.getId(), 2047)); // 폭죽

					LinAllManager.getInstance().CraftInfo(success, pc.getName(), ci.name, craftId);
				} else {
					/** 제작 실패 **/

					/** 제작 실패시 제료 보존 **/
					if (craftId >= 2652 && craftId <= 2653) { // 고대상자 시리즈
						pc.getInventory().storeItem(deleteItemId, _Random.nextInt(2) + 1);
					} else if (craftId >= 1043 && craftId <= 1048) { // 엘릭서 시리즈
						pc.getInventory().storeItem(500209, 30);
					} else if (craftId == 5892) {
						pc.getInventory().storeItem(60765, 30);
					} else if (craftId == 2747) { // 축복받은 오림의 장신구 마법 주문서
						pc.getInventory().storeItem(7323, 1);
					} else if (craftId == 3599) { // 지배이반
						pc.getInventory().storeItem(20288, 1);
					} else if (craftId == 108) { // 형상의 마안
						pc.getInventory().storeItem(430108, 1); // <- 탄생의마안
					} else if (craftId == 109) { // 생명이 마안
						pc.getInventory().storeItem(430109, 1); // <- 형상의마안

						/** 용 갑옷류 용갑옷 보존 **/
					} else if (craftId == 2595) { // 6안타인내
						인첸트지급(pc, 420102, 1, 6, 0);
					} else if (craftId == 2596) { // 6파푸인내
						인첸트지급(pc, 420106, 1, 6, 0);
					} else if (craftId == 2597) { // 6린드인내
						인첸트지급(pc, 420110, 1, 6, 0);
					} else if (craftId == 2598) { // 6발라인내
						인첸트지급(pc, 420112, 1, 6, 0);
					} else if (craftId == 2599) { // 7안타인내
						인첸트지급(pc, 420102, 1, 7, 0);
					} else if (craftId == 2600) { // 7파푸인내
						인첸트지급(pc, 420106, 1, 7, 0);
					} else if (craftId == 2601) { // 7린드인내
						인첸트지급(pc, 420110, 1, 7, 0);
					} else if (craftId == 2602) { // 7발라인내
						인첸트지급(pc, 420112, 1, 7, 0);
					} else if (craftId == 2603) { // 8안타인내
						인첸트지급(pc, 420102, 1, 8, 0);
					} else if (craftId == 2604) { // 8파푸인내
						인첸트지급(pc, 420106, 1, 8, 0);
					} else if (craftId == 2605) { // 8린드인내
						인첸트지급(pc, 420110, 1, 8, 0);
					} else if (craftId == 2606) { // 8발라인내
						인첸트지급(pc, 420112, 1, 8, 0);
					} else if (craftId == 2607) { // 6안타예지
						인첸트지급(pc, 420101, 1, 6, 0);
					} else if (craftId == 2608) { // 6파푸예지
						인첸트지급(pc, 420105, 1, 6, 0);
					} else if (craftId == 2609) { // 6린드예지
						인첸트지급(pc, 420109, 1, 6, 0);
					} else if (craftId == 2610) { // 6발라예지
						인첸트지급(pc, 420113, 1, 6, 0);
					} else if (craftId == 2611) { // 7안타예지
						인첸트지급(pc, 420101, 1, 7, 0);
					} else if (craftId == 2612) { // 7파푸예지
						인첸트지급(pc, 420105, 1, 7, 0);
					} else if (craftId == 2613) { // 7린드예지
						인첸트지급(pc, 420109, 1, 7, 0);
					} else if (craftId == 2614) { // 7발라예지
						인첸트지급(pc, 420113, 1, 7, 0);
					} else if (craftId == 2615) { // 8안타예지
						인첸트지급(pc, 420101, 1, 8, 0);
					} else if (craftId == 2616) { // 8파푸예지
						인첸트지급(pc, 420105, 1, 8, 0);
					} else if (craftId == 2617) { // 8린드예지
						인첸트지급(pc, 420109, 1, 8, 0);
					} else if (craftId == 2618) { // 8발라예지
						인첸트지급(pc, 420113, 1, 8, 0);
					} else if (craftId == 2571) { // 6안타완력
						인첸트지급(pc, 420100, 1, 6, 0);
					} else if (craftId == 2572) { // 6파푸완력
						인첸트지급(pc, 420104, 1, 6, 0);
					} else if (craftId == 2573) { // 6린드완력
						인첸트지급(pc, 420108, 1, 6, 0);
					} else if (craftId == 2574) { // 6발라완력
						인첸트지급(pc, 420115, 1, 6, 0);
					} else if (craftId == 2575) { // 7안타완력
						인첸트지급(pc, 420100, 1, 7, 0);
					} else if (craftId == 2576) { // 7파푸완력
						인첸트지급(pc, 420104, 1, 7, 0);
					} else if (craftId == 2577) { // 7린드완력
						인첸트지급(pc, 420108, 1, 7, 0);
					} else if (craftId == 2578) { // 7발라완력
						인첸트지급(pc, 420115, 1, 7, 0);
					} else if (craftId == 2579) { // 8안타완력
						인첸트지급(pc, 420100, 1, 8, 0);
					} else if (craftId == 2580) { // 8파푸완력
						인첸트지급(pc, 420104, 1, 8, 0);
					} else if (craftId == 2581) { // 8린드완력
						인첸트지급(pc, 420108, 1, 8, 0);
					} else if (craftId == 2582) { // 8발라완력
						인첸트지급(pc, 420115, 1, 8, 0);
					} else if (craftId == 2583) { // 6안타마력
						인첸트지급(pc, 420103, 1, 6, 0);
					} else if (craftId == 2584) { // 6파푸마력
						인첸트지급(pc, 420107, 1, 6, 0);
					} else if (craftId == 2585) { // 6린드마력
						인첸트지급(pc, 420111, 1, 6, 0);
					} else if (craftId == 2586) { // 6발라마력
						인첸트지급(pc, 420114, 1, 6, 0);
					} else if (craftId == 2587) { // 7안타마력
						인첸트지급(pc, 420103, 1, 7, 0);
					} else if (craftId == 2588) { // 7파푸마력
						인첸트지급(pc, 420107, 1, 7, 0);
					} else if (craftId == 2589) { // 7린드마력
						인첸트지급(pc, 420111, 1, 7, 0);
					} else if (craftId == 2590) { // 7발라마력
						인첸트지급(pc, 420114, 1, 7, 0);
					} else if (craftId == 2591) { // 8안타마력
						인첸트지급(pc, 420103, 1, 8, 0);
					} else if (craftId == 2592) { // 8파푸마력
						인첸트지급(pc, 420107, 1, 8, 0);
					} else if (craftId == 2593) { // 8린드마력
						인첸트지급(pc, 420111, 1, 8, 0);
					} else if (craftId == 2594) { // 8발라마력
						인첸트지급(pc, 420114, 1, 8, 0);

						/** 할파스 갑옷 시리즈 할파스 보존 **/
					} else if (craftId == 6138) { // +1 할파스의 완력
						인첸트지급(pc, 27528, 1, 0, 0);
					} else if (craftId == 6139) { // +2 할파스의 완력
						인첸트지급(pc, 27528, 1, 1, 0);
					} else if (craftId == 6140) { // +3 할파스의 완력
						인첸트지급(pc, 27528, 1, 2, 0);
					} else if (craftId == 6141) { // +4 할파스의 완력
						인첸트지급(pc, 27528, 1, 3, 0);
					} else if (craftId == 6142) { // +5 할파스의 완력
						인첸트지급(pc, 27528, 1, 4, 0);
					} else if (craftId == 6143) { // +6 할파스의 완력
						인첸트지급(pc, 27528, 1, 5, 0);
					} else if (craftId == 6144) { // +7 할파스의 완력
						인첸트지급(pc, 27528, 1, 6, 0);
					} else if (craftId == 6145) { // +8 할파스의 완력
						인첸트지급(pc, 27528, 1, 7, 0);
					} else if (craftId == 6146) { // +9 할파스의 완력
						인첸트지급(pc, 27528, 1, 8, 0);
					} else if (craftId == 6147) { // +1 할파스의 예지력
						인첸트지급(pc, 27529, 1, 0, 0);
					} else if (craftId == 6148) { // +2 할파스의 예지력
						인첸트지급(pc, 27529, 1, 1, 0);
					} else if (craftId == 6149) { // +3 할파스의 예지력
						인첸트지급(pc, 27529, 1, 2, 0);
					} else if (craftId == 6150) { // +4 할파스의 예지력
						인첸트지급(pc, 27529, 1, 3, 0);
					} else if (craftId == 6151) { // +5 할파스의 예지력
						인첸트지급(pc, 27529, 1, 4, 0);
					} else if (craftId == 6152) { // +6 할파스의 예지력
						인첸트지급(pc, 27529, 1, 5, 0);
					} else if (craftId == 6153) { // +7 할파스의 예지력
						인첸트지급(pc, 27529, 1, 6, 0);
					} else if (craftId == 6154) { // +8 할파스의 예지력
						인첸트지급(pc, 27529, 1, 7, 0);
					} else if (craftId == 6155) { // +9 할파스의 예지력
						인첸트지급(pc, 27529, 1, 8, 0);
					} else if (craftId == 6156) { // +0 할파스의 마력
						인첸트지급(pc, 27530, 1, 0, 0);
					} else if (craftId == 6157) { // +1 할파스의 마력
						인첸트지급(pc, 27530, 1, 1, 0);
					} else if (craftId == 6158) { // +2 할파스의 마력
						인첸트지급(pc, 27530, 1, 2, 0);
					} else if (craftId == 6159) { // +3 할파스의 마력
						인첸트지급(pc, 27530, 1, 3, 0);
					} else if (craftId == 6160) { // +4 할파스의 마력
						인첸트지급(pc, 27530, 1, 4, 0);
					} else if (craftId == 6161) { // +5 할파스의 마력
						인첸트지급(pc, 27530, 1, 5, 0);
					} else if (craftId == 6162) { // +6 할파스의 마력
						인첸트지급(pc, 27530, 1, 6, 0);
					} else if (craftId == 6163) { // +7 할파스의 마력
						인첸트지급(pc, 27530, 1, 7, 0);
					} else if (craftId == 6164) { // +8 할파스의 마력
						인첸트지급(pc, 27530, 1, 8, 0);

						/** 용무기 시리즈 재료 보존 **/ // 발라카스의 장검
					} else if (craftId == 6195) { // 발라카스의 장검(포르세의 검)
						인첸트지급(pc, 100033, 1, 10, 0); // 포르세의 검, 1개 , 10강 ,0 속성 순서
					} else if (craftId == 6214) { // 발라카스의 장검(포르세의 검)
						인첸트지급(pc, 100033, 1, 10, 1);
					} else if (craftId == 6224) {
						인첸트지급(pc, 100033, 1, 10, 2);
					} else if (craftId == 6234) {
						인첸트지급(pc, 100033, 1, 10, 3);
					} else if (craftId == 6244) {
						인첸트지급(pc, 100033, 1, 10, 4);
					} else if (craftId == 6254) {
						인첸트지급(pc, 100033, 1, 10, 5);
					} else if (craftId == 6196) { // 발라카스의 장검(듀크데필의 검)
						인첸트지급(pc, 30109, 1, 10, 0);
					} else if (craftId == 6215) {
						인첸트지급(pc, 30109, 1, 10, 1);
					} else if (craftId == 6225) {
						인첸트지급(pc, 30109, 1, 10, 2);
					} else if (craftId == 6235) {
						인첸트지급(pc, 30109, 1, 10, 3);
					} else if (craftId == 6245) {
						인첸트지급(pc, 30109, 1, 10, 4);
					} else if (craftId == 6255) {
						인첸트지급(pc, 30109, 1, 10, 5);

						/** 용무기 시리즈 재료 보존 **/ // 발라카스의 양손검
					} else if (craftId == 6197) {
						인첸트지급(pc, 59, 1, 10, 0);
					} else if (craftId == 6216) {
						인첸트지급(pc, 59, 1, 10, 1);
					} else if (craftId == 6226) {
						인첸트지급(pc, 59, 1, 10, 2);
					} else if (craftId == 6236) {
						인첸트지급(pc, 59, 1, 10, 3);
					} else if (craftId == 6246) {
						인첸트지급(pc, 59, 1, 10, 4);
					} else if (craftId == 6256) {
						인첸트지급(pc, 59, 1, 10, 5);

						/** 용무기 시리즈 재료 보존 **/ // 파푸리온의 장궁
					} else if (craftId == 6198) {
						인첸트지급(pc, 293, 1, 10, 0);
					} else if (craftId == 6217) {
						인첸트지급(pc, 293, 1, 10, 1);
					} else if (craftId == 6227) {
						인첸트지급(pc, 293, 1, 10, 2);
					} else if (craftId == 6237) {
						인첸트지급(pc, 293, 1, 10, 3);
					} else if (craftId == 6247) {
						인첸트지급(pc, 293, 1, 10, 4);
					} else if (craftId == 6257) {
						인첸트지급(pc, 293, 1, 10, 5);

						/** 용무기 시리즈 재료 보존 **/ // 파푸리온의 이도류
					} else if (craftId == 6199) {
						인첸트지급(pc, 292, 1, 10, 0);
					} else if (craftId == 6218) {
						인첸트지급(pc, 292, 1, 10, 1);
					} else if (craftId == 6228) {
						인첸트지급(pc, 292, 1, 10, 2);
					} else if (craftId == 6238) {
						인첸트지급(pc, 292, 1, 10, 3);
					} else if (craftId == 6248) {
						인첸트지급(pc, 292, 1, 10, 4);
					} else if (craftId == 6258) {
						인첸트지급(pc, 292, 1, 10, 5);
					} else if (craftId == 6200) {
						인첸트지급(pc, 90083, 1, 10, 0);
					} else if (craftId == 6219) {
						인첸트지급(pc, 90083, 1, 10, 1);
					} else if (craftId == 6229) {
						인첸트지급(pc, 90083, 1, 10, 2);
					} else if (craftId == 6239) {
						인첸트지급(pc, 90083, 1, 10, 3);
					} else if (craftId == 6249) {
						인첸트지급(pc, 90083, 1, 10, 4);
					} else if (craftId == 6259) {
						인첸트지급(pc, 90083, 1, 10, 5);

						/** 용무기 시리즈 재료 보존 **/ // 안타라스의 도끼
					} else if (craftId == 6201) {
						인첸트지급(pc, 7227, 1, 10, 0);
					} else if (craftId == 6220) {
						인첸트지급(pc, 7227, 1, 10, 1);
					} else if (craftId == 6230) {
						인첸트지급(pc, 7227, 1, 10, 2);
					} else if (craftId == 6240) {
						인첸트지급(pc, 7227, 1, 10, 3);
					} else if (craftId == 6250) {
						인첸트지급(pc, 7227, 1, 10, 4);
					} else if (craftId == 6260) {
						인첸트지급(pc, 7227, 1, 10, 5);

						/** 용무기 시리즈 재료 보존 **/ // 안타라스의 지팡이
					} else if (craftId == 6202) {
						인첸트지급(pc, 291, 1, 10, 0);
					} else if (craftId == 6221) {
						인첸트지급(pc, 291, 1, 10, 1);
					} else if (craftId == 6231) {
						인첸트지급(pc, 291, 1, 10, 2);
					} else if (craftId == 6241) {
						인첸트지급(pc, 291, 1, 10, 3);
					} else if (craftId == 6251) {
						인첸트지급(pc, 291, 1, 10, 4);
					} else if (craftId == 6261) {
						인첸트지급(pc, 291, 1, 10, 5);

						/** 용무기 시리즈 재료 보존 **/ // 린드비오르의 체인소드
					} else if (craftId == 6203) {
						인첸트지급(pc, 90084, 1, 10, 0);
					} else if (craftId == 6222) {
						인첸트지급(pc, 90084, 1, 10, 1);
					} else if (craftId == 6232) {
						인첸트지급(pc, 90084, 1, 10, 2);
					} else if (craftId == 6242) {
						인첸트지급(pc, 90084, 1, 10, 3);
					} else if (craftId == 6252) {
						인첸트지급(pc, 90084, 1, 10, 4);
					} else if (craftId == 6262) {
						인첸트지급(pc, 90084, 1, 10, 5);

						/** 용무기 시리즈 재료 보존 **/ // 린드비오르의 키링크
					} else if (craftId == 6204) {
						인첸트지급(pc, 7238, 1, 10, 0);
					} else if (craftId == 6223) {
						인첸트지급(pc, 7238, 1, 10, 1);
					} else if (craftId == 6233) {
						인첸트지급(pc, 7238, 1, 10, 2);
					} else if (craftId == 6243) {
						인첸트지급(pc, 7238, 1, 10, 3);
					} else if (craftId == 6253) {
						인첸트지급(pc, 7238, 1, 10, 4);
					} else if (craftId == 6263) {
						인첸트지급(pc, 7238, 1, 10, 5);

						/** 집행류 숨결로 인챈트시 **/
					} else if (craftId >= 3715 && craftId <= 3724) { // 집행인첸시
						인첸트지급2(pc, 61, 1, craftId - 3715, 5);
					} else if (craftId >= 3728 && craftId <= 3737) { // 바칼
						인첸트지급2(pc, 12, 1, craftId - 3728, 5);
					} else if (craftId >= 3741 && craftId <= 3750) { // 수결지
						인첸트지급2(pc, 134, 1, craftId - 3741, 10);
					} else if (craftId >= 3754 && craftId <= 3763) { // 붉이
						인첸트지급2(pc, 86, 1, craftId - 3754, 5);
					} else if (craftId >= 3767 && craftId <= 3776) { // 격노
						인첸트지급2(pc, 30082, 1, craftId - 3767, 15);
					} else if (craftId >= 3780 && craftId <= 3789) { // 크공
						인첸트지급2(pc, 30080, 1, craftId - 3780, 5);
					} else if (craftId >= 3793 && craftId <= 3802) { // 히페
						인첸트지급2(pc, 30081, 1, craftId - 3793, 5);
					} else if (craftId >= 3806 && craftId <= 3815) { // 타분
						인첸트지급2(pc, 30083, 1, craftId - 3806, 5);
					} else if (craftId >= 5893 && craftId <= 5902) { // 사신의검
						인첸트지급2(pc, 30110, 1, craftId - 5893, 5);

						/** 할파스의 숨결로 인챈트시 **/
					} else if (craftId >= 5913 && craftId <= 5922) { // 아인하사드의섬광
						인첸트지급2(pc, 30112, 1, craftId - 5913, 5);
					} else if (craftId >= 5933 && craftId <= 5942) { // 그랑카인의 심판
						인첸트지급2(pc, 30111, 1, craftId - 5933, 5);

						/** 사신의 숨결로 인첸트시 */
					} else if (craftId >= 4429 && craftId <= 4438) { // 집행인첸시
						인첸트지급2(pc, 61, 1, craftId - 4429, 5);
					} else if (craftId >= 4442 && craftId <= 4451) { // 바칼
						인첸트지급2(pc, 12, 1, craftId - 4442, 5);
					} else if (craftId >= 4455 && craftId <= 4464) { // 수결지
						인첸트지급2(pc, 134, 1, craftId - 4455, 10);
					} else if (craftId >= 4468 && craftId <= 4477) { // 붉이
						인첸트지급2(pc, 86, 1, craftId - 4468, 5);
					} else if (craftId >= 4481 && craftId <= 4490) { // 격노
						인첸트지급2(pc, 30082, 1, craftId - 4481, 15);
					} else if (craftId >= 4494 && craftId <= 4503) { // 크공
						인첸트지급2(pc, 30080, 1, craftId - 4494, 5);
					} else if (craftId >= 4507 && craftId <= 4516) { // 히페
						인첸트지급2(pc, 30081, 1, craftId - 4507, 5);
					} else if (craftId >= 4520 && craftId <= 4529) { // 타분
						인첸트지급2(pc, 30083, 1, craftId - 4520, 5);
					} else if (craftId >= 5903 && craftId <= 5912) { // 사신의검
						인첸트지급2(pc, 30110, 1, craftId - 5903, 5);
					}

					/** 사신의 숨결로 인첸트시 */
					pc.sendPackets(new S_NewCreateItem(0X3b, REMOVE)); // 실패 멘트
					LinAllManager.getInstance().CraftInfo(success, pc.getName(), ci.name, craftId);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		}

		} // end switch

	}

	private boolean 인첸트지급(L1PcInstance pc, int item_id, int count, int EnchantLevel, int AttrEnchantLevel) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);

		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setIdentified(true);
			item.setAttrEnchantLevel(AttrEnchantLevel);

			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82));// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}

			// pc.sendPackets(new S_SystemMessage("아이템 제작에 성공했습니다."));
			pc.sendPackets(new S_ServerMessage(143, item.getLogName())); // %0를 손에 넣었습니다.
			// pc.sendPackets(new S_SkillSound(pc.getId(), 2032));
			// pc.broadcastPacket(new S_SkillSound(pc.getId(), 2032));
			return true;
		} else {
			return false;
		}

	}

	private boolean 인첸트지급2(L1PcInstance pc, int item_id, int count, int EnchantLevel, int AttrEnchantLevel) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			item.setEnchantLevel(EnchantLevel);
			item.setAttrEnchantLevel(AttrEnchantLevel);
			item.setIdentified(true);

			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else {
				pc.sendPackets(new S_ServerMessage(82));
				// 무게 게이지가 부족하거나 인벤토리가 꽉차서 더 들 수 없습니다.
				return false;
			}

			// pc.sendPackets(new S_SystemMessage("아이템 제작에 성공했습니다."));
			pc.sendPackets(new S_ServerMessage(143, item.getLogName())); // %0를 손에 넣었습니다.
			// pc.sendPackets(new S_SkillSound(pc.getId(), 2032));
			// pc.broadcastPacket(new S_SkillSound(pc.getId(), 2032));
			return true;
		} else {
			return false;
		}

	}

	private int craftId;
	private int createItemCount;
	private HashMap<Integer, Integer> materialDescIdList; // descId, encahnt

	private boolean readCraftInfo(L1PcInstance pc) {
		materialDescIdList = new HashMap<Integer, Integer>();

		readP(3); // dummy //토탈사이즈 포함
		L1Object obj = L1World.getInstance().findObject(readBit());

		if (obj == null) {
			craftErrMsg(pc, 0, "obj == null");
			return false;
		}

		if (obj instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) obj;
			int difflocx = Math.abs(pc.getX() - npc.getX());
			int difflocy = Math.abs(pc.getY() - npc.getY());
			if (difflocx > 15 || difflocy > 15 || (pc.getMapId() != npc.getMapId())) {
				pc.sendPackets(new S_ServerMessage(3575), true); // 제작 NPC가 너무 멀리 있습니다.
				return false;
			}
		}

		readP(1); // dummy
		craftId = readBit();

		if (GMCommands.제작체크) {
			System.out.println("제작아이디 - craftId : " + craftId);
		}

		if (craftId == 0) {
			craftErrMsg(pc, craftId, "craftId == 0");
			return false;
		}

		readP(1); // dummy
		createItemCount = readBit();
		if (GMCommands.제작체크) {
			System.out.println("제작만들갯수 - createItemCount : " + createItemCount);
		}

		if (createItemCount == 0) {
			craftErrMsg(pc, craftId, "createItemCount == 0");
			return false;
		}

		while (isRead(1)) {
			int dummyType = readC();

			if (dummyType != 0x22) {
				break;
			}

			readP(4); // 재료템 길이, dummy 0x08 , 재료아이템 순번. dummy 0x10
			int descId = readBit();

			if (materialDescIdList.containsKey(descId) && craftId != 3410 && craftId != 3411 && craftId != 3412 && craftId != 3667 && craftId != 3599
					&& craftId != 2044) {
				craftErrMsg(pc, craftId, "materialDescIdList.containsKey(descId)");
				return false;
			}

			int enchant = 0;
			int isEnchant = readC();

			if (isEnchant == 0x20) { // 인첸트템.
				enchant = readC();
				readP(6); // dummy
			} else {
				readP(5); // dummy
			}

			if (GMCommands.제작체크) {
				System.out.println("제작재료템 - descId : " + descId + " / enchant : " + enchant);
			}

			materialDescIdList.put(descId, enchant);
		}

		return true;
	}

	private void craftErrMsg(L1PcInstance pc, int craftId, String msg) {
		System.out.println("제작 실패: " + msg + " / craftId : " + craftId + " / 사용자 : " + pc.getName());
	}

	public void commit(String com, String name, int count) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM request_log WHERE command=?");
			pstm.setString(1, com);
			rs = pstm.executeQuery();
			Connection con2 = null;
			PreparedStatement pstm2 = null;

			try {
				con2 = L1DatabaseFactory.getInstance().getConnection();
				if (rs.next()) {
					int amount = rs.getInt("count");
					pstm2 = con2.prepareStatement("UPDATE request_log SET count=? WHERE command=?");
					pstm2.setInt(1, amount + count);
					pstm2.setString(2, com);
				} else {
					pstm2 = con2.prepareStatement("INSERT INTO request_log SET command=?, count=?");
					pstm2.setString(1, com);
					pstm2.setInt(2, count);
				}
				pstm2.executeUpdate();
			} catch (SQLException e) {

			} finally {
				SQLUtil.close(pstm2);
				SQLUtil.close(con2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private boolean isTwoLogin(L1PcInstance c) {// 중복체크 변경
		boolean bool = false;
		for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			if (target.noPlayerCK || target.isRobot())
				continue;
			if (c.getId() != target.getId() && (!target.isPrivateShop())) {
				if (c.getNetConnection().getAccountName().equalsIgnoreCase(target.getNetConnection().getAccountName())) {
					bool = true;
					break;
				}
			}
		}
		return bool;
	}

	private void resetAttendanceTime(Account account, L1PcInstance pc) {
		Calendar cal = Calendar.getInstance(Locale.KOREA);
		if (cal.get(Calendar.HOUR_OF_DAY) == 0 && cal.get(Calendar.MINUTE) == 0) {
			if (account.isAttendanceHome()) {
				account.setAttendanceHomeTime(0);
				account.setAttendanceHome(false);
			}
			if (account.isAttendancePcHome()) {
				account.setAttendancePcHomeTime(0);
				account.setAttendancePcHome(false);
			}
			account.updateAttendanceTime();
			account.storeAttendCheck();
			updateAttendanceTime();
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_COMPLETE, account, pc.PC방_버프));
		}
	}

	private void getAttendanceHome(L1PcInstance pc, Account account) {
		if (account.isAttendanceHome())
			return;
		account.addAttendanceHomeTime(1);
		if (account.getAttendanceHomeTime() >= 60) {
			for (int i = 0; i < account.getAttendanceHomeBytes().length; i++) {
				if (account.getAttendanceHomeBytes()[i] == 0) {
					account.getAttendanceHomeBytes()[i] = 1;
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_TIMEOVER, i + 1, false));
					break;
				}
			}
			account.storeAttendBytes();
			account.setAttendanceHomeTime(0);
			account.setAttendanceHome(true);
			account.storeAttendCheck();
		}
	}

	private void getAttendancePCRoom(L1PcInstance pc, Account account) {
		if (!pc.PC방_버프)
			return;
		if (account.isAttendancePcHome())
			return;
		account.addAttendancePcHomeTime(1);
		if (account.getAttendancePcHomeTime() >= 60) {
			for (int i = 0; i < account.getAttendancePcBytes().length; i++) {
				if (account.getAttendancePcBytes()[i] == 0) {
					/* System.out.println("완료패킷될 패킷 번호 : " + (i + 1)); */
					account.getAttendancePcBytes()[i] = 1;
					pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.ATTENDANCE_TIMEOVER, i + 1, true));
					break;
				}
			}
			account.storeAttendBytes();
			account.setAttendancePcHomeTime(0);
			account.setAttendancePcHome(true);
			account.storeAttendCheck();
		}
	}

	private void updateAttendanceTime() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET attendanceHomeTime=0, attendancePcHomeTime=0, attendanceHome=0,attendancePcHome=0");
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private class AttendanceGiveItem implements Runnable {
		private L1PcInstance _pc;
		private L1ItemInstance _reward_item;
		private AttendanceItem _reward_info;

		public AttendanceGiveItem(L1PcInstance pc, L1ItemInstance reward_item, AttendanceItem reward_info) {
			_pc = pc;
			_reward_item = reward_item;
			_reward_info = reward_info;
		}

		@Override
		public void run() {
			try {
				_pc.getInventory().storeItem(_reward_item);
				_pc.sendPackets(new S_ServerMessage(403, _reward_item.getName() + "(" + _reward_item.getCount() + ")"));

				if (_reward_info._broadcast_item) {
					L1World.getInstance().broadcastPacketToAll(
							new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "출석 보상으로 아덴 월드의 어느 용사가 " + _reward_item.getName() + " 를(을) 획득하였습니다."));
				}

				L1SkillUse l1skilluse = new L1SkillUse();
				l1skilluse.handleCommands(_pc, L1SkillId.출석체크, _pc.getId(), _pc.getX(), _pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void ChatWhisper(L1PcInstance whisperFrom, int chatType, int chatcount, byte[] chatdata, String text, String targetName) {
		try {
			// 채팅 금지중의 경우
			if (whisperFrom.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
				S_ServerMessage sm = new S_ServerMessage(242);
				whisperFrom.sendPackets(sm, true);
				return;
			}

			if (whisperFrom.getLevel() < Config.WHISPER_CHAT_LEVEL) {
				S_ServerMessage sm = new S_ServerMessage(404, String.valueOf(Config.WHISPER_CHAT_LEVEL));
				whisperFrom.sendPackets(sm, true);
				return;
			}

			/*
			 * if (!whisperFrom.isGm() && (targetName.compareTo("메티스") == 0)) {
			 * S_SystemMessage sm = new S_SystemMessage( "운영자님께는 귓속말을 할 수 없습니다.");
			 * whisperFrom.sendPackets(sm, true); return; }
			 */

			if (targetName.equalsIgnoreCase("***")) {
				S_SystemMessage sm = new S_SystemMessage("-> (***) " + text);
				whisperFrom.sendPackets(sm, true);
				return;
			}

			L1PcInstance whisperTo = L1World.getInstance().getPlayer(targetName);

			// 월드에 없는 경우
			if (whisperTo == null) {
				L1NpcShopInstance npc = null;
				npc = L1World.getInstance().getNpcShop(targetName);
				if (npc != null) {
					// S_ChatPacket scp = new S_ChatPacket(npc,
					// text,Opcodes.S_MESSAGE, 9);
					S_NewCreateItem scp = new S_NewCreateItem(chatType, chatdata, chatcount, whisperFrom);
					whisperFrom.sendPackets(scp, true);
					// S_SystemMessage sm = new
					// S_SystemMessage("-> ("+targetName+") "+text);
					// whisperFrom.sendPackets(sm); sm.clear(); sm = null;
					return;
				}

				S_ServerMessage sm = new S_ServerMessage(73, targetName);
				whisperFrom.sendPackets(sm, true);
				return;
			}

			// 자기 자신에 대한 wis의 경우
			if (whisperTo.equals(whisperFrom)) {
				return;
			}

			if (whisperTo.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) {
				S_SystemMessage sm = new S_SystemMessage("채팅금지중인 유저에게 귓속말은 할수 없습니다.");
				whisperFrom.sendPackets(sm, true);
				return;
			}

			if (text.length() > 45) {
				S_SystemMessage sm = new S_SystemMessage("귓말로 보낼 수 있는 글자수를 초과하였습니다.");
				whisperFrom.sendPackets(sm, true);
				return;
			}

			// 차단되고 있는 경우
			if (whisperTo.getExcludingList().contains(whisperFrom.getName())) {
				S_ServerMessage sm = new S_ServerMessage(117, whisperTo.getName());
				whisperFrom.sendPackets(sm, true);
				return;
			}

			if (!whisperTo.isCanWhisper()) {
				S_ServerMessage sm = new S_ServerMessage(205, whisperTo.getName());
				whisperFrom.sendPackets(sm, true);
				return;
			}

			if (whisperTo instanceof L1RobotInstance) {
				// S_ChatPacket scp = new S_ChatPacket(whisperTo,
				// text,Opcodes.S_MESSAGE, 9);
				whisperFrom.sendPackets(new S_NewCreateItem(chatType, chatdata, chatcount, whisperTo.getName(), whisperFrom));
				return;
			}

			whisperFrom.sendPackets(new S_NewCreateItem(whisperFrom, 3, chatType, text, whisperTo.getName()));
			whisperTo.sendPackets(new S_NewCreateItem(whisperFrom, 4, chatType, text, whisperTo.getName()));
			LinAllManager.getInstance().WisperChatAppend(whisperFrom.getName(), whisperTo.getName(), text);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	private void Chat(L1PcInstance pc, int chatType, int chatcount, byte[] chatdata, String chatText, LineageClient clientthread) {
		try {
			if (pc.캐릭명변경) {
				try {
					String chaName = chatText;
					if (pc.getClanid() > 0) {
						pc.sendPackets(new S_SystemMessage("혈맹탈퇴후 캐릭명을 변경할수 있습니다."));
						pc.캐릭명변경 = false;
						return;
					}

					if (!pc.getInventory().checkItem(467009, 1)) { // 있나 체크
						pc.sendPackets(new S_SystemMessage("케릭명 변경 비법서를 소지하셔야 가능합니다."));
						pc.캐릭명변경 = false;
						return;
					}

					for (int i = 0; i < chaName.length(); i++) {
						if (chaName.charAt(i) == 'ㄱ' || chaName.charAt(i) == 'ㄲ' || chaName.charAt(i) == 'ㄴ' || chaName.charAt(i) == 'ㄷ' // 한문자(char)단위로 비교.
								|| chaName.charAt(i) == 'ㄸ' || chaName.charAt(i) == 'ㄹ' || chaName.charAt(i) == 'ㅁ' || chaName.charAt(i) == 'ㅂ' // 한문자(char)단위로
																																				// 비교
								|| chaName.charAt(i) == 'ㅃ' || chaName.charAt(i) == 'ㅅ' || chaName.charAt(i) == 'ㅆ' || chaName.charAt(i) == 'ㅇ' // 한문자(char)단위로
																																				// 비교
								|| chaName.charAt(i) == 'ㅈ' || chaName.charAt(i) == 'ㅉ' || chaName.charAt(i) == 'ㅊ' || chaName.charAt(i) == 'ㅋ' // 한문자(char)단위로
																																				// 비교.
								|| chaName.charAt(i) == 'ㅌ' || chaName.charAt(i) == 'ㅍ' || chaName.charAt(i) == 'ㅎ' || chaName.charAt(i) == 'ㅛ' // 한문자(char)단위로
																																				// 비교.
								|| chaName.charAt(i) == 'ㅕ' || chaName.charAt(i) == 'ㅑ' || chaName.charAt(i) == 'ㅐ' || chaName.charAt(i) == 'ㅔ' // 한문자(char)단위로
																																				// 비교.
								|| chaName.charAt(i) == 'ㅗ' || chaName.charAt(i) == 'ㅓ' || chaName.charAt(i) == 'ㅏ' || chaName.charAt(i) == 'ㅣ' // 한문자(char)단위로
																																				// 비교.
								|| chaName.charAt(i) == 'ㅠ' || chaName.charAt(i) == 'ㅜ' || chaName.charAt(i) == 'ㅡ' || chaName.charAt(i) == 'ㅒ' // 한문자(char)단위로
																																				// 비교.
								|| chaName.charAt(i) == 'ㅖ' || chaName.charAt(i) == 'ㅢ' || chaName.charAt(i) == 'ㅟ' || chaName.charAt(i) == 'ㅝ' // 한문자(char)단위로
																																				// 비교.
								|| chaName.charAt(i) == 'ㅞ' || chaName.charAt(i) == 'ㅙ' || chaName.charAt(i) == 'ㅚ' || chaName.charAt(i) == 'ㅘ' // 한문자(char)단위로
																																				// 비교.
								|| chaName.charAt(i) == '씹' || chaName.charAt(i) == '좃' || chaName.charAt(i) == '좆' || chaName.charAt(i) == 'ㅤ') {
							pc.sendPackets(new S_SystemMessage("사용할수없는 케릭명입니다."));
							pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
							pc.캐릭명변경 = false;
							return;
						}
					}

					if (chaName.getBytes().length > 12) {
						pc.sendPackets(new S_SystemMessage("이름이 너무 깁니다."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					if (chaName.length() == 0) {
						pc.sendPackets(new S_SystemMessage("변경할 케릭명을 입력하세요."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					if (BadNamesList.getInstance().isBadName(chaName)) {
						pc.sendPackets(new S_SystemMessage("사용할 수 없는 케릭명입니다."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					if (isInvalidName(chaName)) {
						pc.sendPackets(new S_SystemMessage("사용할 수 없는 케릭명입니다."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					if (CharacterTable.doesCharNameExist(chaName)) {
						pc.sendPackets(new S_SystemMessage("동일한 케릭명이 존재합니다."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					if (CharacterTable.RobotNameExist(chaName)) {
						pc.sendPackets(new S_SystemMessage("동일한 케릭명이 존재합니다."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					if (CharacterTable.RobotCrownNameExist(chaName)) {
						pc.sendPackets(new S_SystemMessage("동일한 케릭명이 존재합니다."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					if (NpcShopSpawnTable.getInstance().getNpc(chaName) || npcshopNameCk(chaName)) {
						pc.sendPackets(new S_SystemMessage("동일한 케릭명이 존재합니다."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					if (CharacterTable.somakname(chaName)) {
						pc.sendPackets(new S_SystemMessage("동일한 케릭명이 존재합니다."));
						pc.sendPackets(new S_SystemMessage("캐릭명 변경 비법서를 다시 클릭후 이용해 주세요."));
						pc.캐릭명변경 = false;
						return;
					}

					pc.getInventory().consumeItem(467009, 1); // 소모

					String oldname = pc.getName();

					chaname(chaName, oldname);

					long sysTime = System.currentTimeMillis();
					logchangename(chaName, oldname, new Timestamp(sysTime));

					pc.sendPackets(new S_SystemMessage(chaName + " 아이디로 변경 하셨습니다."));
					pc.sendPackets(new S_SystemMessage("원할한  이용을 위해 클라이언트가 강제로 종료 됩니다."));

					Thread.sleep(1000);
					clientthread.kick();
				} catch (Exception e) {
				}

				return;
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SILENCE) || pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.AREA_OF_SILENCE)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE)) {
				return;
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_CHAT_PROHIBITED)) { // 채팅 금지중
				S_ServerMessage sm = new S_ServerMessage(242);
				pc.sendPackets(sm); // 현재 채팅 금지중입니다.
				sm = null;
				return;
			}

			if (pc.isDeathMatch() && !pc.isGhost() && !pc.isGm()) {
				S_ServerMessage sm = new S_ServerMessage(912);
				pc.sendPackets(sm); // 채팅을 할 수 없습니다.
				sm = null;
				return;
			}

			if (!pc.isGm()) {

				for (String tt : textFilter) {
					int indexof = chatText.indexOf(tt);

					if (indexof != -1) {
						int count = 100;

						while ((indexof = chatText.indexOf(tt)) != -1) {
							if (count-- <= 0) {
								break;
							}

							char[] dd = chatText.toCharArray();
							chatText = "";

							for (int i = 0; i < dd.length; i++) {
								if (i >= indexof && i <= (indexof + tt.length() - 1)) {
									chatText = chatText + "  ";
								} else
									chatText = chatText + dd[i];
							}
						}
					}
				}
			}

			switch (chatType) {
			case 0: {
				if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
					return;
				}

				if (chatText.startsWith(".시각")) {
					StringBuilder sb = null;
					sb = new StringBuilder();
					TimeZone kst = TimeZone.getTimeZone("GMT+9");
					Calendar cal = Calendar.getInstance(kst);
					sb.append("[Server Time]" + cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH) + 1) + "월 " + cal.get(Calendar.DATE) + "일 "
							+ cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));

					S_SystemMessage sm = new S_SystemMessage(sb.toString());
					pc.sendPackets(sm, true);
					sb = null;
					return;
				}

				// GM커멘드
				if (chatText.startsWith(".") && (pc.getAccessLevel() == Config.GMCODE || pc.getAccessLevel() == 7777)) {
					String cmd = chatText.substring(1);
					GMCommands.getInstance().handleCommands(pc, cmd);
					return;
				}

				if (chatText.startsWith("$")) {
					if (pc.isGm()) {
						chatWorld(pc, chatdata, chatType, chatcount, chatText);
					} else {
						chatWorld(pc, chatdata, 12, chatcount, chatText);
					}

					if (!pc.isGm()) {
						pc.checkChatInterval();
					}

					return;
				}

				Gamble(pc, chatText);
				if (chatText.startsWith(".")) { // 유저코멘트
					String cmd = chatText.substring(1);
					if (cmd == null) {
						return;
					}

					UserCommands.getInstance().handleCommands(pc, cmd);
					return;
				}

				if (chatText.startsWith("$")) { // 월드채팅
					if (pc.isGm()) {
						chatWorld(pc, chatdata, chatType, chatcount, chatText);
					} else {
						chatWorld(pc, chatdata, 12, chatcount, chatText);
					}

					if (!pc.isGm()) {
						pc.checkChatInterval();
					}

					return;
				}

				pc.sendPackets(new S_NewCreateItem(pc, 3, chatType, chatText, ""));
				S_NewCreateItem s_chatpacket = new S_NewCreateItem(pc, 4, chatType, chatText, "");

				if (!pc.getExcludingList().contains(pc.getName())) {
					if (pc.getMapId() != 2699) {
						pc.sendPackets(s_chatpacket);
					}
				}

				for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(pc)) {
					if (!listner.getExcludingList().contains(pc.getName())) {
						if (listner.getMapId() == 2699) {
							continue;
						}

						listner.sendPackets(s_chatpacket);
					}
				}

				// 돕펠 처리
				L1MonsterInstance mob = null;
				for (L1Object obj : pc.getNearObjects().getKnownObjects()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
							Broadcaster.broadcastPacket(mob, new S_NpcChatPacket(mob, chatText, 0), true);
						}
					}
				}

				LinAllManager.getInstance().NomalchatAppend(pc.getName(), chatText);
			}
				break;
			case 2: {
				if (pc.isGhost()) {
					return;
				}

				// S_ChatPacket s_chatpacket = new S_ChatPacket(pc,
				// chatText,Opcodes.S_SAY, 2);
				// S_NewCreateItem chat5 = new S_NewCreateItem(chatType,
				// chatdata, chatcount, pc);
				S_NewCreateItem chat5 = new S_NewCreateItem(pc, 4, chatType, chatText, "");
				if (!pc.getExcludingList().contains(pc.getName())) {
					pc.sendPackets(chat5);
				}

				for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(pc, 50)) {
					if (!listner.getExcludingList().contains(pc.getName())) {
						listner.sendPackets(chat5);
					}
				}

				LinAllManager.getInstance().NomalchatAppend(pc.getName(), chatText);
				// 돕펠 처리
				L1MonsterInstance mob = null;
				for (L1Object obj : pc.getNearObjects().getKnownObjects()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
							for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(mob, 30)) {
								listner.sendPackets(new S_NpcChatPacket(mob, chatText, 2), true);
							}
						}
					}
				}
			}
				break;
			case 3:
				chatWorld(pc, chatdata, chatType, chatcount, chatText);
				break;
			case 4: {
				if (pc.getClanid() != 0) { // 크란 소속중
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					S_NewCreateItem chat4 = new S_NewCreateItem(pc, 4, chatType, chatText, "");
					LinAllManager.getInstance().ClanChatAppend(pc.getClanname(), pc.getName(), chatText);
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(chat4);
						}
					}
				}
			}
				break;
			case 11: {
				if (pc.isInParty()) { // 파티중
					// S_NewCreateItem s_chatpacket = new
					// S_NewCreateItem(chatType, chatdata, chatcount, pc);
					S_NewCreateItem s_chatpacket = new S_NewCreateItem(pc, 4, chatType, chatText, "");
					for (L1PcInstance listner : pc.getParty().getMembers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(s_chatpacket);
						}
					}
				}
				LinAllManager.getInstance().PartyChatAppend(pc.getName(), chatText);
			}
				break;
			case 12:
				if (pc.isGm()) {
					chatWorld(pc, chatdata, chatType, chatcount, chatText);
				} else {
					chatWorld(pc, chatdata, 3, chatcount, chatText);
				}
				break;
			case 13: { // 수호기사 채팅
				if (pc.getClanid() != 0) { // 혈맹 소속중
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					int rank = pc.getClanRank();
					if (clan != null && (rank == L1Clan.CLAN_RANK_GUARDIAN || rank == L1Clan.CLAN_RANK_SUBPRINCE || rank == L1Clan.CLAN_RANK_PRINCE)) {
						// S_NewCreateItem chat1 = new S_NewCreateItem(chatType,
						// chatdata, chatcount, pc);
						S_NewCreateItem chat1 = new S_NewCreateItem(pc, 4, chatType, chatText, "");

						for (L1PcInstance listner : clan.getOnlineClanMember()) {
							int listnerRank = listner.getClanRank();
							if (!listner.getExcludingList().contains(pc.getName()) && (listnerRank == L1Clan.CLAN_RANK_GUARDIAN
									|| rank == L1Clan.CLAN_RANK_SUBPRINCE || listnerRank == L1Clan.CLAN_RANK_PRINCE)) {
								listner.sendPackets(chat1);
							}
						}
					}
				}
				LinAllManager.getInstance().ClanChatAppend(pc.getClanname(), pc.getName(), chatText);
			}
				break;
			case 14: { // 채팅 파티
				if (pc.isInChatParty()) { // 채팅 파티중
					S_NewCreateItem s_chatpacket = new S_NewCreateItem(pc, 4, chatType, chatText, "");
					LinAllManager.getInstance().PartyChatAppend(pc.getName(), chatText);
					for (L1PcInstance listner : pc.getChatParty().getMembers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(s_chatpacket);
						}
					}
				}
			}
				break;
			case 15: { // 동맹채팅
				if (pc.getClanid() != 0) { // 혈맹 소속중
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());

					if (clan != null) {
						Integer allianceids[] = clan.Alliance();
						if (allianceids.length > 0) {
							String TargetClanName = null;
							L1Clan TargegClan = null;
							S_NewCreateItem s_chatpacket = new S_NewCreateItem(pc, 4, chatType, chatText, "");
							for (L1PcInstance listner : clan.getOnlineClanMember()) {
								int AllianceClan = listner.getClanid();
								if (pc.getClanid() == AllianceClan) {
									listner.sendPackets(s_chatpacket);
								}
							} // 자기혈맹 전송용

							for (int j = 0; j < allianceids.length; j++) {
								TargegClan = clan.getAlliance(allianceids[j]);
								if (TargegClan != null) {
									TargetClanName = TargegClan.getClanName();
									if (TargetClanName != null) {
										for (L1PcInstance alliancelistner : TargegClan.getOnlineClanMember()) {
											alliancelistner.sendPackets(s_chatpacket);
										} // 동맹혈맹 전송용
									}
								}

							}
						}

					}
				}
				break;
			}
			case 17:
				if (pc.getClanid() != 0) { // 혈맹 소속중
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					if (clan != null && (pc.isCrown() && pc.getId() == clan.getLeaderId())) {
						S_NewCreateItem s_chatpacket5 = new S_NewCreateItem(pc, 4, chatType, chatText, "");
						LinAllManager.getInstance().ClanChatAppend(pc.getClan().getClanName(), pc.getName(), chatText);
						for (L1PcInstance listner : clan.getOnlineClanMember()) {
							if (!listner.getExcludingList().contains(pc.getName())) {
								listner.sendPackets(s_chatpacket5);
							}
						}
					}
				}

				break;

			} // end switch

			if (!pc.isGm()) {
				pc.checkChatInterval();
			}

		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private void chatWorld(L1PcInstance pc, byte[] chatdata, int chatType, int chatcount, String text) {
		if (pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
			if (pc.isGm() || L1World.getInstance().isWorldChatElabled()) {
				if (pc.get_food() >= 12) { // 5%겟지?
					S_PacketBox pb = new S_PacketBox(S_PacketBox.FOOD, pc.get_food());
					pc.sendPackets(pb, true);
					if (chatType == 3) {
						if (pc.isGm()) {
//							L1World.getInstance().broadcastPacketToAll(new S_NewCreateItem(pc, 4, chatType, text, ""));
							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[운영자] " + text));
						}
						LinAllManager.getInstance().AllChatAppend(pc.getName(), text);
					} else if (chatType == 12) {
						if (pc.isGm()) {
//							L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[운영자] " + text));
						}
						LinAllManager.getInstance().AllChatAppend("[장사]" + pc.getName(), text);
					}

					for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							if (listner.isShowTradeChat() && chatType == 12) {
								listner.sendPackets(new S_NewCreateItem(chatType, chatdata, chatcount, pc));
								listner.sendPackets(new S_NewCreateItem(pc, 4, chatType, text, ""));
							} else if (listner.isShowWorldChat() && chatType == 3) {
								listner.sendPackets(new S_NewCreateItem(pc, 4, chatType, text, ""));
							}
						}
					}
					if (chatType == 3) {
						LinAllManager.getInstance().AllChatAppend(pc.getName(), text);
					} else if (chatType == 12) {
						LinAllManager.getInstance().AllChatAppend("[장사]" + pc.getName(), text);
					}
				} else {
					S_ServerMessage sm = new S_ServerMessage(462);
					pc.sendPackets(sm, true);
				}
			} else {
				S_ServerMessage sm = new S_ServerMessage(510);
				pc.sendPackets(sm, true);
			}
		} else {
			S_ServerMessage sm = new S_ServerMessage(195, String.valueOf(Config.GLOBAL_CHAT_LEVEL));
			pc.sendPackets(sm, true);
		}
	}

	private boolean npcshopNameCk(String name) {
		return NpcTable.getInstance().findNpcShopName(name);
	}

	private void logchangename(String chaName, String oldname, Timestamp datetime) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "INSERT INTO Log_Change_name SET Old_Name=?,New_Name=?, Time=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, oldname);
			pstm.setString(2, chaName);
			pstm.setTimestamp(3, datetime);
			pstm.executeUpdate();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private static boolean isInvalidName(String name) {
		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes("EUC-KR").length;
		} catch (UnsupportedEncodingException e) {
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}

		if (isAlphaNumeric(name)) {
			return false;
		}

		if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) {
			return false;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			return false;
		}

		return true;
	}

	private static boolean isAlphaNumeric(String s) {
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}

			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}

			i++;
		} while (true);

		return flag;
	}

	private void chaname(String chaName, String oldname) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET char_name=? WHERE char_name=?");
			pstm.setString(1, chaName);
			pstm.setString(2, oldname);
			pstm.executeUpdate();
		} catch (Exception e) {

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void Gamble(L1PcInstance pc, String chatText) {

		if (pc.Gamble_Somak) { // 소막
			for (int i : GambleInstance.mobArray) {
				L1Npc npck = NpcTable.getInstance().getTemplate(i);
				String name = npck.get_name().replace(" ", "");
				if (name.equalsIgnoreCase(chatText) || npck.get_name().equalsIgnoreCase(chatText)
				/*
				 * || chatText.startsWith(npck.get_name())|| chatText.startsWith(name)
				 */) {
					pc.Gamble_Text = npck.get_name();
				}
			}
		}
	}

	public int Nexttam(String encobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int day = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT day FROM `tam` WHERE encobjid = ? order by id asc limit 1"); // 케릭터 테이블에서 군주만 골라와서
			pstm.setString(1, encobj);
			rs = pstm.executeQuery();

			while (rs.next()) {
				day = rs.getInt("Day");
			}
		} catch (SQLException e) {
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return day;
	}

	public int TamCharid(String encobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int objid = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT objid FROM `tam` WHERE encobjid = ? order by id asc limit 1"); // 케릭터 테이블에서 군주만 골라와서
			pstm.setString(1, encobj);
			rs = pstm.executeQuery();

			while (rs.next()) {
				objid = rs.getInt("objid");
			}
		} catch (SQLException e) {
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return objid;
	}

	public void tamcancle(String objectId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("delete from tam where encobjid = ? order by id asc limit 1");
			pstm.setString(1, objectId);
			pstm.executeUpdate();
		} catch (SQLException e) {
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public String getType() {
		return C_ACTION_UI;
	}

}
