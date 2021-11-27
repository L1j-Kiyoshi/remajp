/*
\ * This program is free software; you can redistribute it and/or modify
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
package l1j.server.server.serverpackets;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 스킬 아이콘이나 차단 리스트의 표시 등 복수의 용도에 사용되는 패킷의 클래스
 */
public class S_PacketBox extends ServerBasePacket {
	private static final String S_PACKETBOX = "[S] S_PacketBox";

	private byte[] _byte = null;

	// *** S_107 sub code list ***
	/** 바포 시스템 by사부 */
	public static final int BAPO = 114;
	// 1:Kent 2:Orc 3:WW 4:Giran 5:Heine 6:Dwarf 7:Aden 8:Diad 9:성명 9 ...

	public static final int DRAGONMENU = 102; // 드래곤 메뉴
	public static final int DRAGON_EME = 86;

	/** C(id) H(?): %s의 공성전이 시작되었습니다. */
	public static final int MSG_WAR_BEGIN = 0;

	/** C(id) H(?): %s의 공성전이 종료했습니다. */
	public static final int MSG_WAR_END = 1;

	/** C(id) H(?): %s의 공성전이 진행중입니다. */
	public static final int MSG_WAR_GOING = 2;

	/** -: 성의 주도권을 잡았습니다. (음악이 바뀐다) */
	public static final int MSG_WAR_INITIATIVE = 3;

	/** -: 성을 점거했습니다. */
	public static final int MSG_WAR_OCCUPY = 4;

	/** ?: 결투가 끝났습니다. (음악이 바뀐다) */
	public static final int MSG_DUEL = 5;

	/** C(count): SMS의 송신에 실패했습니다. / 전부%d건송신되었습니다. */
	public static final int MSG_SMS_SENT = 6;

	/** -: 축복안, 2명은 부부로서 연결되었습니다. (음악이 바뀐다) */
	public static final int MSG_MARRIED = 9;

	/** C(weight): 중량(30 단계) */
	public static final int WEIGHT = 10;

	/** C(food): 만복도(30 단계) */
	public static final int FOOD = 11;

	/** C(0) C(level): 이 아이템은%d레벨 이하만 사용할 수 있습니다. (0~49이외는 표시되지 않는다) */
	public static final int MSG_LEVEL_OVER = 12;

	/** UB정보 HTML */
	public static final int HTML_UB = 14;

	/**
	 * C(id)<br>
	 * 1:몸에 담겨져 있던 정령의 힘이 공기안에 녹아 가는 것을 느꼈습니다.<br>
	 * 2:몸의 구석구석에 화의 정령력이 스며들어 옵니다.<br>
	 * 3:몸의 구석구석에 물의 정령력이 스며들어 옵니다.<br>
	 * 4:몸의 구석구석에 바람의 정령력이 스며들어 옵니다.<br>
	 * 5:몸의 구석구석에 땅의 정령력이 스며들어 옵니다.<br>
	 */

	public static final int MSG_ELF = 15;

	/** C(count) S(name)...: 차단 리스트 복수 추가 */
	public static final int ADD_EXCLUDE2 = 17;

	/** S(name): 차단 리스트 추가 */
	public static final int ADD_EXCLUDE = 18;

	/** S(name): 차단 해제 */
	public static final int REM_EXCLUDE = 19;

	/** 스킬 아이콘 */
	public static final int ICONS1 = 20;

	/** 스킬 아이콘 */
	public static final int ICONS2 = 21;

	/** 아우라계의 스킬 아이콘 및 이레이즈매직 아이콘 삭제 */
	public static final int ICON_AURA = 22;

	/** S(name): 타운 리더에게%s가 선택되었습니다. */
	public static final int MSG_TOWN_LEADER = 23;

	/**
	 * D(혈맹원수) (S(혈원이름) C(혈원계급)) 혈맹원 갱신이 된 상태에서의 /혈맹.
	 */
	public static final int PLEDGE_TWO = 24;

	/**
	 * D(혈맹원이름) C(랭크) 혈맹에 추가된 인원이 있을때 보내주는 패킷
	 */
	public static final int PLEDGE_REFRESH_PLUS = 25;

	/**
	 * D(혈맹원이름) C(랭크) 혈맹에 삭제된 인원이 있을때 보내주는 패킷
	 */
	public static final int PLEDGE_REFRESH_MINUS = 26;

	/**
	 * C(id): 당신의 랭크가%s로 변경되었습니다. (1-견습 2-일반 3-수호기사)
	 */
	public static final int MSG_RANK_CHANGED = 27;

	/**
	 * D(혈맹원수) (S(혈원이름) C(혈원계급)) 혈맹원 갱신이 안된 상태에서의 /혈맹.
	 */
	// public static final int PLEDGE_ONE = 119;

	/** D(?) S(name) S(clanname): %s혈맹의%s가 라스타바드군을 치웠습니다. */
	public static final int MSG_WIN_LASTAVARD = 30;

	/** -: \f1기분이 좋아졌습니다. */
	public static final int MSG_FEEL_GOOD = 31;

	/** 불명.C_30 패킷이 난다 */
	public static final int SOMETHING1 = 33;

	/** H(time): 블루 일부의 아이콘이 표시된다. */
	public static final int ICON_BLUEPOTION = 34;

	/** H(time): 변신의 아이콘이 표시된다. */
	public static final int ICON_POLYMORPH = 35;

	/** H(time): 채팅 금지의 아이콘이 표시된다. */
	public static final int ICON_CHATBAN = 36;

	/** 펫 아이템 갱신 패킷 */
	public static final int PET_ITEM = 37;

	/** 혈맹 정보의 HTML가 표시된다 */
	public static final int HTML_CLAN1 = 38;

	/** H(time): 이뮤의 아이콘이 표시된다 */
	public static final int ICON_I2H = 40;

	/** 캐릭터의 게임 옵션, 쇼트 컷 정보등을 보낸다 */
	public static final int CHARACTER_CONFIG = 41;

	/** 캐릭터 선택 화면으로 돌아간다 */
	public static final int LOGOUT = 42;

	/** 전투중에 재시 동요할 수 없습니다. */
	public static final int MSG_CANT_LOGOUT = 43;

	/**
	 * C(count) D(time) S(name) S(info):<br>
	 * [CALL] 버튼이 붙은 윈도우가 표시된다. 이름을 더블 클릭 하면(자) C_RequestWho가 날아, 클라이언트의 폴더에
	 * bot_list.txt가 생성된다.이름을 선택해+키를 누르면(자) 새로운 윈도우가 열린다.
	 */
	public static final int CALL_SOMETHING = 45;

	/**
	 * C(id): 배틀 콜롯세움, 카오스 대전이―<br>
	 * id - 1:개시합니다 2:삭제되었던 3:종료합니다
	 */
	public static final int MSG_COLOSSEUM = 49;

	/** 혈맹 정보의 HTML */
	public static final int HTML_CLAN2 = 51;

	/** 요리 윈도우를 연다 */
	public static final int COOK_WINDOW = 52;

	/** C(type) H(time): 요리 아이콘이 표시된다 */
	public static final int ICON_COOKING = 53;

	/** 물고기찌 흔들림포시 */
	public static final int FISHING = 55;

	/** 아이콘 삭제 */
	public static final int DEL_ICON = 59;

	public static final int DRAGONPERL = 60; // 진주

	public static final int EXP_POTION3 = 9278;

	public static final int EXP_POTION2 = 9279;

	/** 계정 시간 type:예약결제 time:시간 */
	public static final int ACCOUNT_TIME = 61;

	/** 동맹 목록 */
	public static final int ALLIANCE_LIST = 62;

	/** 미니게임 : 5,4,3,2,1 카운트 */
	public static final int MINIGAME_START_COUNT = 64;

	/** 미니게임 : 타임(0:00시작) */
	public static final int MINIGAME_START_TIME = 65;

	/** 미니게임 : 게임자 리스트 */
	public static final int MINIGAME_LIST = 66;

	/** 미니게임 : 잠시 후 마을로 이동됩니다(10초 음) **/
	public static final int MINIGAME_10SECOND_COUNT = 69;

	/** 미니게임 : 종료 */
	public static final int MINIGAME_END = 70;

	/** 미니게임 : 타임 */
	public static final int MINIGAME_TIME = 71;

	/** 미니게임 : 타임삭제 */
	public static final int MINIGAME_TIME_CLEAR = 72;

	// 73 value 100잡으면 웨폰 소리 들림

	/** 팔을 다쳐 공격 능력이 하락합니다. */
	public static final int DAMAGE_DOWN = 74;

	/** 용기사 : 약점 노출 */
	public static final int SPOT = 75;

	/**
	 * 공성전이 시작되었습니다. 78 성을 소유하고 있는 혈맹은 다음과 같습니다.
	 */

	/**
	 * 공성전이 종료되었습니다. 79 성을 소유하고 있는 혈맹은 다음과 같습니다.
	 */

	/**
	 * 공성전이 진행중입니다. 80 성을 소유하고 있는 혈맹은 다음과 같습니다.
	 */

	// public static final int HADIN_DISPLAY = 83;

	public static final int HADIN_DISPLAY = 83;
	public static final int GREEN_MESSAGE = 84;
	public static final int YELLOW_MESSAGE = 00000; // 인던 챕터2 대기
	public static final int RED_MESSAGE = 51; // 레드메세지
	/** 아인하사드 버프 */
	/* public static final int AINHASAD = 82; */

	/**
	 * 우호도 UI 표시 + 욕망의 동굴 - 그림자 신전
	 */
	public static final int KARMA = 87;

	/**
	 * 우호도 다음에 오는 불분명 패킷 2개 ↓
	 */
	public static final int INIT_DG = 88;
	public static final int DRAGONBLOOD = 100;
	public static final int UPDATE_DG = 101;

	public static final int DG_TIME_RESTART = 159;// 9F
	public static final int TIME_COUNT = 0x99;
	public static final int SECURITY_BUFF = 125;
	public static final int PC방버프 = 127;
	public static final int LOGIN_UNKNOWN3 = 32;
	public static final int char_ER = 132;
	public static final int 무기손상마우스 = 138;
	public static final int 기억창_확장 = 141;

	public static final int 버프시간변경 = 86;

	/*
	 * public static final int 뭘까 = 142; [서버패킷] [2014-07-09 오전 10:49:27:667] [OPCODE
	 * = 118]
	 * 
	 * 0000: 76 8e 00 ef 4e 0d 00 24 31 33 37 31 39 00 08 5c v...N..$13719..\ 0010:
	 * 66 56 5b bd c5 ba f1 5d 20 5c 66 58 be f3 c0 bd fV[....] \fX.... 0020: 20 c8
	 * a3 bc f6 20 c1 df be d3 00 04 00 00 00 d2 .... .......... 0030: 84 49 7e 5c
	 * 66 56 5b bd c5 ba f1 5d 20 5c 66 58 .I~\fV[....] \fX 0040: be f3 c0 bd 20 c8
	 * a3 bc f6 20 bf dc b0 a2 00 04 .... .... ...... 0050: 00 00 00 ca 84 4c 7e 5c
	 * 66 56 5b bd c5 ba f1 5d .....L~\fV[....] 0060: 20 5c 66 58 be f3 c0 bd 20 c0
	 * fd ba ae 00 04 00 \fX.... ....... 0070: 00 00 a4 85 57 7e 5c 66 56 5b bd c5
	 * ba f1 5d 20 ....W~\fV[....] 0080: 5c 66 58 be c6 b5 a7 20 c0 db c0 ba 20 b9
	 * e8 00 \fX.... .... ... 0090: 04 00 00 00 92 85 6f 81 5c 66 56 5b bd c5 ba f1
	 * ......o.\fV[.... 00a0: 5d 20 5c 66 58 c7 cf c0 cc b3 d7 20 b0 e6 b0 e8 ]
	 * \fX...... .... 00b0: c5 be 00 04 00 00 00 df 82 e1 82 5c 66 56 5b bd
	 * ...........\fV[. 00c0: c5 ba f1 5d 20 5c 66 58 bf eb c0 c7 20 b0 e8 b0 ...]
	 * \fX.... ... 00d0: ee 20 c0 fd ba ae 00 04 00 00 00 ba 82 38 7e 5c .
	 * ...........8~\ 00e0: 66 56 5b bd c5 ba f1 5d 20 5c 66 58 bc fb b0 dc fV[....]
	 * \fX.... 00f0: c1 f8 20 b0 c5 ba cf c0 cc 20 bc b6 00 04 00 00 .. ......
	 * ...... 0100: 00 3f 82 8b 81 5c 66 56 5b bd c5 ba f1 5d 20 5c .?...\fV[....] \
	 * 0110: 66 58 c1 a4 bd c5 b0 fa 20 bd c3 b0 a3 c0 c7 20 fX...... ...... 0120:
	 * bc b6 00 04 00 00 00 2b 7f d3 80 .......+..
	 */

	public static final int SKILL_WEAPON_ICON = 154;
	public static final int ROUND_SHOW = 156;
	public static final int ROUND_DEL = 158;
	public static final int WEAPON_RANGE = 160;
	public static final int 커스종류 = 161;// A1
	public static final int 혈맹버프 = 165;// 173
	/*
	 * [OPCODE = 239] 0000: ef ac e3 27 ab 11 17 00 00 00 00 00 01 d7 a5 e6
	 * ...'............ 0010: 19 00 00 00 00 00 00 00 00 07 00 00 00 00 2e 3f
	 * ...............?
	 */
	public static final int 인챈변경 = 172;
	public static final int 지도위치보정 = 176;
	public static final int 드래곤레이드버프 = 179;
	public static final int 낚시_베리 = 182;
	public static final int 배틀샷 = 181;
	public static final int NONE_TIME_ICON = 180;
	public static final int WORLDMAP_UNKNOWN1 = 184;
	public static final int ACTION_GUIDE_1 = 188;
	public static final int ACTION_GUIDE_2 = 189;
	public static final int 상점개설횟수 = 198;
	public static final int 유저빽스탭 = 193;
	public static final int 이미지스폰 = 194;//

	/** 추가패킷 10.20opcodes **/
	public static final int MINI_MAP_SEND = 111;
	public static final int SCORE_MARK = 4;
	public static final int aaaa1 = 78;
	public static final int bbbb2 = 79;
	public static final int cccc3 = 80;
	public static final int HTML_PLEDGE_REALEASE_ANNOUNCE = 168;
	public static final int HTML_PLEDGE_ONLINE_MEMBERS = 171;
	public static final int UNLIMITED_ICON = 147;
	public static final int ITEM_ = 149;
	public static final int ICON_COMBO_BUFF = 204;

	private static final int PLEDGE_ONE = 0;

	public static final int BUFFICON = 0;

	/** 10.20 추가패킷 **/

	public S_PacketBox(int subCode, L1ItemInstance item, int type) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case ITEM_:
			writeD(item.getId());
			writeH(type);
		}
	}

	public S_PacketBox(int subCode, Object[] names) {
		writeC(108);
		writeC(subCode);

		switch (subCode) {
		case 171:
			writeH(names.length);
			for (Object name : names)
				if (name != null) {
					L1PcInstance pc = (L1PcInstance) name;
					writeS(pc.getName());
					writeC(0);
				}
			break;
		}
	}

	public S_PacketBox(int subCode, String name, int mapid, int x, int y, int Mid) {
		writeC(108);
		writeC(subCode);
		switch (subCode) {
		case 111:
			writeS(name);
			writeH(mapid);
			writeH(x);
			writeH(y);
			writeD(Mid);
			break;
		}
	}

	// b4 //옵

	// 50 02 //넘버

	// 08 03 //사이즈 겟수

	// 10 dc 22 //문자 넘버

	// 22 09 2b 39 20 24 33 30 39 38 37 //이름

	// 28 b7 d5 01 //아이템 데스크 넘버 체크

	// 183 남은 시간 : 일반 XX(분) / PC방 약 XX(분)이 남았습니다.
	public S_PacketBox(L1ItemInstance item, int subcode) {
		writeC(Opcodes.S_EVENT);
		writeC(subcode);

		writeD(item.getId());
		writeD(0x18);
		writeH(0x00);
		writeC(item.getEnchantLevel());
		writeD(item.getId());
		writeD(0);
		writeD(0);
		if (!((item.getItemId() >= 437010 && item.getItemId() <= 437013)
				/** 기사단 장비 */
				|| (item.getItemId() >= 60716 && item.getItemId() <= 60725) || (item.getItemId() >= 30098 && item.getItemId() <= 30104)
				|| (item.getItemId() >= 20615 && item.getItemId() <= 20626)
				/** 기사단장비 */
				|| item.getItemId() == 5000067 || item.getItemId() == 60104 || (item.getItemId() >= 21125 && item.getItemId() <= 21136)
				|| (item.getItemId() >= 20452 && item.getItemId() <= 20455) || (item.getItemId() >= 66757 && item.getItemId() <= 66788)
				|| item.getItemId() == 1437010 || item.getItemId() == 1437011 || item.getItemId() == 60291 || item.getItemId() == 888819
				|| (item.getItemId() >= 421000 && item.getItemId() <= 421020) || item.getItemId() == 560025 || item.getItemId() == 560027
				|| item.getItemId() == 560028 || item.getItemId() == 41159 || (item.getItemId() >= 21139 && item.getItemId() <= 21156)
				|| (item.getItemId() >= 60286 && item.getItemId() <= 60289) || (item.getItemId() >= 60261 && item.getItemId() <= 60263)
				|| item.getItemId() == 60354 || item.getItemId() == 60396 || item.getItemId() == 60398 || item.getItemId() == 9096 || item.getItemId() == 600369
				|| (item.getItemId() >= 60427 && item.getItemId() <= 60444) || (item.getItemId() >= 60447 && item.getItemId() <= 60472)
				|| item.getItemId() == 21256 || item.getItemId() == 21257 || item.getItemId() == 60492) && !item.getItem().isTradable()) {
			writeD(0x00);
		} else {
			writeD(7);// 창고 보관 가능
			/*
			 * if(item.getItem().getType2()==2){ writeC(3); }else{ writeC(7);//창고 보관 가능 }
			 */
		}

		writeC(0);
		writeC(item.getAttrEnchantBit(item.getAttrEnchantLevel()));
		writeH(0);
	}

	public S_PacketBox(int subCode) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case 188:
			writeD(0);
			writeD(0);
			break;
		case 189:
			writeD(0);
			break;
		case MSG_WAR_INITIATIVE: // 3
		case MSG_WAR_OCCUPY: // 4
		case MSG_MARRIED: // 9
		case MSG_FEEL_GOOD: // 31
		case MSG_CANT_LOGOUT: // 43
		case LOGOUT: // 42
		case ROUND_DEL: // 158
		case 125: // securityBuff
			break;
		case FISHING:
		case MINIGAME_START_TIME:
		case MINIGAME_TIME_CLEAR:
			break;
		case CALL_SOMETHING:
			callSomething();
			break;
		case WORLDMAP_UNKNOWN1:
		case DEL_ICON:
			writeH(0);
			break;
		case ICON_AURA:
			writeC(0x98);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			break;
		case MINIGAME_10SECOND_COUNT:
			writeC(10);
			writeC(109);
			writeC(85);
			writeC(208);
			writeC(2);
			writeC(220);
			break;
		case MINIGAME_END:
			writeC(147);
			writeC(92);
			writeC(151);
			writeC(220);
			writeC(42);
			writeC(74);
			break;
		case MINIGAME_START_COUNT:
			writeC(5);
			writeC(129);
			writeC(252);
			writeC(125);
			writeC(110);
			writeC(17);
			break;
		case LOGIN_UNKNOWN3:
			/*
			 * 10 17 fc 3f 00 00 00 00 00 00 00 00 00 00 00 00 00
			 */
			writeC(0x10);// writeC(0x17);writeC(0xfc);writeC(0x3f);
			writeD(0);
			writeD(2);
			writeD(0);
			writeD(0x00200000);
			break;
		/*
		 * case ACTION_GUIDE_1: //writeD(0x0002F070);//이 값에 따라 최초죽었는지 고기 보상받았는지 나오는듯
		 * //writeD(0x0006F1F8);
		 * 
		 * writeD(0xFFFFFFFF); writeD(0x00); break;
		 */

		case 지도위치보정:
			writeC(0x01);
			writeH(0x00);
			break;
		case 73:
			writeS("voice_kirtas_12");
			break;

		default:
			// writeH(0xFFFF);
			break;
		}
	}

	public S_PacketBox(int subCode, int objid, int value, boolean show) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case ICON_AURA:
			writeC(value);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0);
			break;
		case 이미지스폰:

			/*
			 * 12750 = 화룡안식처 스킬
			 */
			writeD(objid); // 오브젝트아이디
			writeD(value); // 이미지 번호
			writeD(show ? 0x01 : 0x00); // 삭제 추가
			writeH(0);
			break;
		}
	}

	public S_PacketBox(int subCode, int value, boolean show) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case DRAGON_EME:
			writeC(0x4C);
			writeC(show ? 0x01 : 0x00);
			writeC(value);// 0x2d(45)크레이 0x4a(74)군터
			break;
		case 147:
			writeC(show ? 1 : 0);
			writeC(value);
			break;
		case 180:
			writeC(show ? 1 : 0);
			writeD(value);
			writeD(0);
			writeH(0);
			break;
		case BAPO:
			writeD(value); // 1~7 깃발
			writeD(show ? 0x01 : 0x00); // On Off
			break;

		default:
			break;
		}
	}

	public S_PacketBox(int subCode1, int subCode2, String name, boolean ok) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode1);
		switch (subCode2) {
		case RED_MESSAGE:
		case YELLOW_MESSAGE:
			writeC(2);
			writeH(26204);
			writeC(subCode2);
			writeS(name);
			break;
		case SCORE_MARK:
			writeC(subCode2);
			writeS(name);
			break;
		default: // ?
			switch (subCode1) {
			case MSG_RANK_CHANGED:
				writeC(subCode2);
				writeS(name);
				break;
			case ADD_EXCLUDE:
			case REM_EXCLUDE:
				writeS(name);
				writeC(subCode2);
				break;
			}
			break;
		}
	}

	public S_PacketBox(int subCode, int value, String svalue) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {

		case MSG_RANK_CHANGED:
			writeC(value);
			writeS(svalue);
			break;

		default:
			break;
		}
	}

	public S_PacketBox(int subCode, int value) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case PC방버프:
			if (value == 1) {
				writeC(0x18);
			} else {
				writeC(0);
			}
			break;
		case DRAGON_EME:
			if (value == 0) {
				writeH(0x90);
				writeC(0);
			} else {
				writeH(0xb7);
				writeC(0);
			}
			break;
		case ICON_AURA:
			writeC(0x5a);
			writeH(value);
			break;
		case ICON_BLUEPOTION:
		case ICON_CHATBAN:
		case ICON_I2H:
		case MINIGAME_TIME:
		case INIT_DG:
			writeH(value); // time
			break;
		case MSG_WAR_BEGIN:
		case MSG_WAR_END:
		case MSG_WAR_GOING:
			writeC(value); // castle id
			writeH(0); // ?
			break;
		case MSG_SMS_SENT:
		case WEIGHT:
		case FOOD:
		case UPDATE_DG:
		case MSG_ELF:
		case MSG_COLOSSEUM:
		case SPOT:
		case 기억창_확장:
			writeC(value); // msg id
			break;
		case 204: // 콤보
			writeH(value);
			break;
		case MSG_LEVEL_OVER:
			writeC(0); // ?
			writeC(value); // 0-49이외는 표시되지 않는다
			break;
		case COOK_WINDOW:
			writeC(0xdb); // ?
			writeC(0x31);
			writeC(0xdf);
			writeC(0x02);
			writeC(0x01);
			writeC(value); // level
			break;
		case HADIN_DISPLAY:
			writeD(value);
			writeH(0);
			break;
		case TIME_COUNT:
			writeH(value);
			writeH(0);
			break;
		case 129:
			writeC(value);
			writeC(value);
			break;
		case 혈맹버프:
			writeC(1);
			if (value == 0)
				writeC(0);
			else if (value == 1) {
				writeC(1);
			}
			writeD(0);
			break;
		case ROUND_SHOW:
			writeD(value);
			writeD(12);
			break;
		case 무기손상마우스:
			writeC(value);
			break;
		case 낚시_베리:
			writeD(value);
			writeH(0x00);
			break;
		case 배틀샷:
			writeD(value);
			break;
		case 드래곤레이드버프:
			writeC(0x01);
			writeC(0x27);
			writeC(0x0E);
			writeD(value);// 남은초
			writeH(0x63EF);
			break;
		case char_ER:
			writeC(value);
			break;
		case 상점개설횟수:
			writeD(value);
			writeD(0x28);
			writeD(0x00);
			break;
		default:
			break;
		}
	}

	/** 자수정 **/
	// public S_PacketBox(int time, int val, boolean ck, boolean ck2) {
	public S_PacketBox(int time, int val, boolean ck, boolean ck2) {
		writeC(Opcodes.S_EVENT);
		writeC(DRAGON_EME);
		writeC(0x81);
		writeC(0x01);
		writeC(val);
		writeH(time);
	}// 7e 56 81 01 02 08 07

	public S_PacketBox(int i, int time, boolean ck, boolean ck2, boolean ck3) {
		writeC(Opcodes.S_EVENT);
		writeC(DRAGON_EME);
		writeC(0x3e);
		writeC(i);
		writeH(time);
		writeC(0x14);
		writeC(0x86);
	}// 0f 56 3e 01 08 07 14 86

	public S_PacketBox(L1PcInstance pc, int time, boolean ck, boolean ck2, boolean ck3) {
		writeC(Opcodes.S_EVENT);
		writeC(DRAGON_EME);
		writeC(0x3e);
		if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
			writeC(0x02);
		} else {
			writeC(0x01);
		}
		writeH(time);
		writeC(0x14);
		writeC(0x86);
	}// 0f 56 3e 01 08 07 14 86

	/** 레벨업 버프 **/
	public S_PacketBox(int time, boolean ck, boolean ck2) {
		writeC(Opcodes.S_EVENT);
		writeC(0x56);
		writeC(0xAA);
		writeC(0x01);
		writeH(time / 16);
		writeH(0x00);
	}

	public S_PacketBox(String s) {
		writeC(Opcodes.S_EVENT);
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}

	public S_PacketBox(int subCode, int type, int time, boolean second, boolean temp) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case SKILL_WEAPON_ICON:
			writeH(time);
			writeH(type);
			writeH(0);
			writeH(second ? 0x01 : 0x00); // 삭제 추가
			break;
		}// b0 04 80 08 00 00 00 00
	}

	public S_PacketBox(int subCode, int type, int time) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case ICON_POLYMORPH: // 변신아이콘 패킷 변경으로!
			writeH(type); // 시간
			writeC(time); // 타입(1 보통변신 , 2 지배자 변신)
			break;

		case DRAGONBLOOD:
			writeC(type);
			writeD(time);
			break;
		case DRAGON_EME:
			writeC(0x70);
			writeC(0x01);
			writeC(type);
			writeD(time);
			break;
		case DRAGONPERL:
			writeC((int) ((time + 2) / 4));
			writeC(type);
			break;
		case EXP_POTION2:
			writeC(time);
			writeC(type);
			break;
		case EXP_POTION3:
			writeC(time);
			writeC(type);
			break;
		// 0f
		// 3d e3 8b 24 00 00 피씨방
		case ACCOUNT_TIME:
			writeD(0);
			writeH(type);
			break;
		case ICON_COOKING:
			writeC(0x13);
			writeC(0x0b);
			writeC(0x0a);
			writeC(0x12);
			writeH(0x0909);
			writeC(0xD0);
			writeC(0x07);
			writeC(type);
			writeC(0x26);
			writeH(time);
			writeC(0x61);
			/*
			 * if (type != 7) { writeC(0x0c); writeC(0x0c); writeC(0x0c); writeC(0x12);
			 * writeC(0x0c); writeC(0x09); writeC(0x00); writeC(0x00); writeC(type);
			 * writeC(0x24); writeH(time); writeH(0x00);
			 * 
			 * } else { writeC(0x0c); writeC(0x0c); writeC(0x0c); writeC(0x12);
			 * writeC(0x0c); writeC(0x09); writeC(0xc8); writeC(0x00); writeC(type);
			 * writeC(0x26); writeH(time); writeC(0x3e); writeC(0x87); }
			 */
			break;
		case ICON_AURA:
			writeC(0xdd);
			writeH(time);
			writeC(type);
			break;

		case MSG_DUEL:
			writeD(type);
			writeD(time);
			break;
		case 커스종류:// 161
			writeC(type); // 1독, 2패럴, 6사일
			if (type == 2) {
				writeH(0x00); // 포이즌 시간, 사일
				writeH(time); // 패럴 시간
			} else {
				writeH(time); // 포이즌 시간, 사일
				writeH(0x00); // 패럴 시간
			}
			break;

		case NONE_TIME_ICON:
			writeC(type);// on/off
			writeD(time);// 166 exp30%, 228 시원한얼음조각, 286 exp40%, 343 기르타스지역사망패널티, 409 아머브레이크, 497
							// 붉은기사의증표, 이벤트공성존 //477~479
			writeD(3431);
			writeH(0);
			break;
		default:
			break;
		}
	}

	public S_PacketBox(int subCode, int type, int petid, int ac) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {

		case PET_ITEM:
			writeC(type);
			writeD(petid); // pet objid
			writeH(ac);
			break;
		default:
			break;
		}
	}

	public S_PacketBox(int subCode, L1ItemInstance item) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case DRAGONMENU:
			writeD(item.getId());
			writeC(1);
			writeC(1);
			writeC(1);
			writeC(0);
			// writeD(item.getId());
			// writeC(item.getItemId() == 490012 ? 1 : 0);
			// writeC(item.getItemId() == 490013 ? 1 : 0);
			// writeC(item.getItemId() == 490014 ? 1 : 0);
			// writeC(0);
			break;
		case 172: // 인첸변경
			writeD(item.getId());
			writeC(23);
			writeC(0);
			writeH(0);
			writeH(0);
			writeC(item.getEnchantLevel());
			writeD(item.getId());
			writeD(0);
			writeD(0);
			writeD(item.getItem().isTradable() ? 7 : item.getBless() >= 128 ? 3 : 2);
			writeD(0);
			break;
		}
	}

	public S_PacketBox(int subCode, String name) {

		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case MSG_TOWN_LEADER:
			// case PLEDGE_UNION:
		case 168:
			writeS(name);
			break;
		case GREEN_MESSAGE:
			writeC(2);
			writeS(name);
			break;
		case YELLOW_MESSAGE:
			break;
		default:
			break;
		}
	}

	public S_PacketBox(int subCode, String name, int type) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case ADD_EXCLUDE:
		case REM_EXCLUDE:
			writeS(name);
			writeC(type);// 타입 0:차단리스트 1:편지 차단리스트
			break;
		default:
			break;
		}
	}

	public S_PacketBox(int subCode, Object[] names, int type) {
		writeC(Opcodes.S_EVENT);
		writeH(subCode);

		switch (subCode) {
		case ADD_EXCLUDE2:
			writeC(type);// 타입 0: 차단리스트, 1: 편지차단
			writeC(names.length);
			for (Object name : names) {
				writeS(name.toString());
			}
			break;
		default:
			break;
		}
	}

	public S_PacketBox(int subCode, int id, String name, String clanName) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case MSG_WIN_LASTAVARD:
			writeD(id); // 크란 ID인가 무엇인가?
			writeS(name);
			writeS(clanName);
			break;
		default:
			break;
		}
	}

	public S_PacketBox(L1PcInstance pc, int subCode) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);

		switch (subCode) {
		case PLEDGE_ONE:
		case PLEDGE_TWO:
			String clanName = pc.getClanname();
			L1Clan clan = L1World.getInstance().getClan(clanName);
			writeD(clan.getClanMemberList().size());

			ClanMember member;
			ArrayList<ClanMember> clanMemberList = clan.getClanMemberList(); // 모든
																				// 혈맹원의
																				// 이름과
																				// 등급
			for (int i = 0; i < clanMemberList.size(); i++) {
				member = clanMemberList.get(i);
				writeS(member.name);
				if (member.rank == 5) {
					writeC(3);
				} else {
					writeC(member.rank);
				}
			}

			/*
			 * if (offlineMemberCount > 0) { for(int i = 0 ; i < offlineMemberCount ; i++) {
			 * // 오프라인 String name = clan.getOfflineMemberName(i); int rank =
			 * clan.getOfflineMemberRank(name); if(rank < 0) continue; writeS(name);
			 * writeC(rank); } }
			 */

			writeD(clan.getOnlineMemberCount());
			for (L1PcInstance targetPc : clan.getOnlineClanMember()) { // 온라인
				writeS(targetPc.getName());
			}
			break;
		case PLEDGE_REFRESH_PLUS:
		case PLEDGE_REFRESH_MINUS:
			writeS(pc.getName());
			writeC(pc.getClanRank());
			writeH(0);
			break;
		case KARMA:
			writeD(pc.getKarma());
			break;
		case ALLIANCE_LIST:
			StringBuffer sb = new StringBuffer();
			for (int i : pc.getClan().Alliance()) {
				if (i == 0)
					continue;
				L1Clan c = L1World.getInstance().getClan(i);
				if (c == null)
					continue;
				sb.append(c.getClanName() + " ");
			}
			writeS(sb.toString());
			break;

		case DG_TIME_RESTART:
			writeD(7);
			writeD(1);
			writeS("$12125");// 기감
			int time = pc.던전시간체크("기감");
			writeD(time > 0 ? time / 120 : 0);
			writeD(15);

			writeS("$6081");
			time = pc.던전시간체크("상아탑");
			writeD(time > 0 ? time / 60 : 0);
			writeD(19);

			writeS("$12126");
			time = pc.던전시간체크("고무");
			writeD(time > 0 ? time / 60 : 0);
			writeD(41);

			// writeS("$14250");
			// time = pc.던전시간체크("고무");
			// writeD(time > 0 ? time / 60 : 0);
			// writeD(500);

			writeS("$19375");
			time = pc.던전시간체크("고무");
			writeD(time > 0 ? time / 60 : 0);
			writeD(49200);

			time = 7200 - pc.getravatime();
			writeD(time > 0 ? time / 60 : 0);
			break;
		default:
			break;
		}
	}

	public S_PacketBox(String name, int mapid, int x, int y, int Mid) {

		writeC(Opcodes.S_EVENT);
		writeC(0x6F);
		writeS(name);
		writeH(mapid);
		writeH(x);
		writeH(y);
		writeH(Mid);
		writeH(0);
	}

	/**
	 * 왼쪽 상단 SCORE
	 * 
	 * @param on    TRUE : 표시 FALSE : 비표시
	 * @param score 띄울 점수
	 */
	public S_PacketBox(boolean on, int score) {
		writeC(Opcodes.S_EVENT);
		writeC(0x54);
		if (on) {
			writeC(4);
			writeS("" + score);
		} else {
			writeC(3);
			writeS("" + score);
		}
	}

	public S_PacketBox(int subCode, L1PcInstance pc, L1ItemInstance item, boolean bool) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case WEAPON_RANGE: {
			int polyId = pc.getGfxId().getTempCharGfx();
			int 길이 = 1;
			int 착용 = 0;
			int 마법사용 = 0;
			if (polyId == 3784 || polyId == 13152 || polyId == 13153 || polyId == 15154 || polyId == 15232 || polyId == 14923 || polyId == 15223
					|| polyId == 12702 || polyId == 12681 || polyId == 6137 || polyId == 6142 || polyId == 6147 || polyId == 6152 || polyId == 6157
					|| polyId == 9205 || polyId == 9206 || polyId == 16014 || polyId == 17541 || polyId == 17515 || polyId == 17531 || polyId == 15986
					|| polyId == 16027 || polyId == 16284 || polyId == 16053 || polyId == 16040) {
				길이 = 1;
			} else {
				if (bool) {
					if (pc.getWeapon().getItem().getType() == 4) {
						길이 = 17;
					} else if (pc.getWeapon().getItem().getType() == 13 || pc.getWeapon().getItem().getType() == 10) {
						길이 = 15;
					} else if (pc.getWeapon().getItem().getType() == 5 || pc.getWeapon().getItem().getType() == 14
							|| pc.getWeapon().getItem().getType() == 18) {
						길이 = 2;
					} else
						길이 = 1;

					if (길이 == 2 && (polyId == 7967 || polyId == 10874 || polyId == 7846 || polyId == 7848 || polyId == 8719)) {
						길이 = 1;
					}
					if (길이 == 2 && polyId >= 11328 && polyId <= 11448 && !(polyId >= 11408 && polyId <= 11421)) {
						if (polyId == 11330 || polyId == 11344 || polyId == 11351 || polyId == 11368 || polyId == 11376 || polyId == 11447 || polyId == 16008
								|| polyId == 16056)
							길이 = 2;
						else
							길이 = 1;
					}
				} else {
					길이 = 1;
				}
			}
			if (길이 == 17 || 길이 == 15) {
				착용 = 3;
				마법사용 = 1;
				if (길이 == 15) {
					마법사용 = 0;
				}
			} else {
				if (bool) {
					착용 = 1;
					if (pc.getWeapon().getItem().getType() == 3) {
						마법사용 = 1;
					}

					if (pc.getWeapon().getItem().getType() == 6 || pc.getWeapon().getItem().getType() == 15) {
						착용 = 2;
					}

					if (pc.getWeapon().getItem().getType() == 18) {
						착용 = 10;
						마법사용 = 1;
					}
					if (pc.getWeapon().getItem().getType() == 12) {
						착용 = 4;
						마법사용 = 0;
					}
				}
			}
			writeC(길이);
			writeC(착용);
			writeC(마법사용);
		}
			break;

		}
	}

	public S_PacketBox(int subCode, L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		/*
		 * case AINHASAD: int value = pc.getAinHasad(); int value2 =
		 * pc.getAinhasadbless(); value /= 10000; writeD(value);// % 수치 1~200
		 * writeD(10000); writeD(0x00); writeD(value2);//축복소모효율 break;
		 */
		case 유저빽스탭:
			writeH(pc.getX());
			writeH(pc.getY());
			break;

		}
	}

	public S_PacketBox(int subCode, L1PcInstance pc, int value1, int value2) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case ICON_COOKING:
			writeH(pc.getAbility().getStr());
			writeH(pc.getAbility().getInt());
			writeH(pc.getAbility().getWis());
			writeH(pc.getAbility().getDex());
			writeH(pc.getAbility().getCon());
			writeH(pc.getAbility().getCha());
			writeH((pc.get_food() - 25) * 10);
			writeC(value1);
			writeC(0x24);
			writeH(value2);
			writeC(pc.getInventory().calcWeightpercent());
			break;
		}
	}

	public S_PacketBox(int subCode, boolean show) {
		writeC(Opcodes.S_EVENT);
		writeC(subCode);
		switch (subCode) {
		case 165: // 혈맹버프
			writeC(show ? 1 : 0);
			break;
		}
	}

	private void callSomething() {

		Iterator<L1PcInstance> itr = L1World.getInstance().getAllPlayers().iterator();

		writeC(L1World.getInstance().getAllPlayers().size());
		L1PcInstance pc = null;
		Account acc = null;
		Calendar cal = null;
		while (itr.hasNext()) {
			pc = itr.next();
			acc = Account.load(pc.getAccountName());

			// 시간 정보 우선 로그인 시간을 넣어 본다655

			if (acc == null) {
				writeD(0);
			} else {
				cal = Calendar.getInstance(TimeZone.getTimeZone(Config.TIME_ZONE));
				long lastactive = acc.getLastActive().getTime();
				cal.setTimeInMillis(lastactive);
				cal.set(Calendar.YEAR, 1970);
				int time = (int) (cal.getTimeInMillis() / 1000);
				writeD(time); // JST 1970 1/1 09:00 이 기준
			}

			// 캐릭터 정보
			writeS(pc.getName()); // 반각 12자까지
			writeS(pc.getClanname()); // []내에 표시되는 캐릭터 라인.반각 12자까지
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}

		return _byte;
	}

	@Override
	public String getType() {
		return S_PACKETBOX;
	}
	/*
	 * 미러이미지 패킷 0e <- PacektBox 15 00 00 00 00 <- 초 1000초 일경우 1000 / 16 + 1
	 */
}
