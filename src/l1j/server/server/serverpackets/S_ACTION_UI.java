package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.AttendanceTable;
import l1j.server.server.datatables.AttendanceTable.AttendanceItem;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_ACTION_UI extends ServerBasePacket {

	private byte[] _byte = null;

	public static final int CRAFT_ITEM = 55; // 제작 아이템
	public static final int CRAFT_ITEMLIST = 57; // 제작 리스트
	public static final int CRAFT_OK = 59; // 제작 완료
	public static final int CRAFT_GAUGEUI = 93;

	public static final int 신스킬 = 110;
	public static final int DOLL_START = 123; // 시작
	public static final int DOLL_RESULT = 125; // 결과
	public static final int PCBANG_SET = 126;
	public static final int DOLL_READY = 128; // 시작

	public static final int SAFETYZONE = 207;
	public static final int EMOTICON = 320;
	public static final int CLAN_JOIN_MESSAGE = 323;
	public static final int CLAN_JOIN_SETTING = 333;
	public static final int LOGIN_ADD_SKILL = 401;
	public static final int ADD_SKILL = 402;
	public static final int SPELL_PASSIVE_ONOFF_ACK = 409;
	public static final int TAM = 450;
	public static final int stateProfile = 487;
	public static final int Elixir = 489;
	public static final int ATTENDANCE_START = 544;
	public static final int ATTENDANCE_ITEM = 548;
	public static final int 리마월드메시지 = 592;
	public static final int BMTYPE_DEL_CHECK_ACK = 605;
	public static final int ATTENDANCE_COMPLETE = 1004;
	public static final int ATTENDANCE_TIMEOVER = 1005;
	public static final int ATTENDANCE_ITEM_COMPLETE = 1007;

	public static final int AINHASAD = 1020;

	public static final int SUMMON_PET_NOTI = 2321; // 마법인형 아이콘 표기
	public static final int PETBALL_CONTENTS_START_ACK = 2383; // 마법인형 시작시

	private static final String S_ACTION_UI = "S_ACTION_UI";

	public S_ACTION_UI(int sub, boolean isSuccess, int objid, int gfxid, boolean isBless) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(sub);
		switch (sub) {
		case DOLL_RESULT:
			writeC(0x00);
			writeC(0x08);
			writeC(isSuccess ? 0 : 1);
			writeC(0x12);
			int Size = getBitSize(objid) + getBitSize(gfxid) + 4;
			writeC(Size);
			writeC(0x08);
			write4bit(objid);
			writeC(0x10);
			write4bit(gfxid);
			writeC(0x18);
			write4bit(isBless ? 0 : 1);
			writeH(0x00);
			break;
		}
	}

	public S_ACTION_UI(int subType, int time, L1DollInstance doll, L1ItemInstance item, boolean repeat) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(subType);
		switch (subType) {
		case SUMMON_PET_NOTI:
			writeC(0x08);
			writeBit(time);

			writeC(0x10);
			if (repeat)
				writeBit(doll.getId());
			else {
				if (time < 0)
					writeBit(-1);
				else
					writeBit(0);
			}

			writeC(0x18);
			if (repeat)
				writeBit(item.getId());
			else {
				if (time < 0)
					writeBit(-1);
				else
					writeBit(0);
			}

			if (item != null) {
				writeC(0x22);
				byte[] stats = item.getStatusBytes();
				writeBit(stats.length); // 아이템표기 길이
				writeByte(stats); // 아이템표기
			}
			break;
		}
		writeH(0);
	}

	public S_ACTION_UI(int type, int value, boolean isPCRoom, L1ItemInstance item, boolean world_message) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case ATTENDANCE_ITEM_COMPLETE:
			writeC(0x08);
			writeBit(value);
			writeC(0x10);
			writeBit(isPCRoom ? 0x01 : 0x00);
			writeC(0x18);
			writeBit(3);
			writeC(0x20);
			writeBit(item.getItem().getItemDescId());
			writeC(0x28);
			writeBit(item.getCount());
			writeC(0x30);
			writeBit(world_message ? 1 : 0);
			break;
		}
		writeH(0);
	}

	public S_ACTION_UI(int skillid, int time, int gfxid, int msg) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x6e);
		writeC(0x00);
		writeC(0x08);
		writeC(0x02);
		writeC(0x10);
		write7B(skillid);
		writeC(0x18);
		write7B(time);
		writeC(0x20);
		int duration_show_type = 8;
		if (skillid == L1SkillId.CLAN_BLESS_BUFF)
			duration_show_type = 10;
		writeC(duration_show_type);

		writeC(0x28);
		write7B(gfxid);
		writeC(0x30);
		writeC(0x00);
		writeC(0x38);
		writeC(0x03);
		writeC(0x40);
		write7B(msg);
		writeC(0x48);
		writeC(0x00);
		writeC(0x50);
		writeC(0x00);
		writeC(0x58);
		writeC(0x01);
		writeC(0x60);
		writeC(0x00);
		writeC(0x68);
		writeC(0x00);
		writeC(0x70);
		writeC(0x00);
		writeH(0x00);
	}

	/**
	 * 파티 표식 설정시 사용함.
	 */
	public S_ACTION_UI(byte[] flag) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(339);
		writeByte(flag);
		writeH(0);
	}

	public S_ACTION_UI(int type, int MessageType, L1ItemInstance Item) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case 리마월드메시지:
			String ItemEnchantLV = " +" + Item.getEnchantLevel();
			String Bleesed = "축복 받은 ";
			String ItemAttrEnchantLV = null;
			switch (Item.getAttrEnchantLevel()) {
			case 1:
				ItemAttrEnchantLV = "화령:1단 ";
				break; // 화령1단
			case 2:
				ItemAttrEnchantLV = "화령:2단 ";
				break; // 화령2단
			case 3:
				ItemAttrEnchantLV = "화령:3단 ";
				break; // 화령3단
			case 4:
				ItemAttrEnchantLV = "화령:4단 ";
				break; // 화령4단
			case 5:
				ItemAttrEnchantLV = "화령:5단 ";
				break; // 화령5단
			case 6:
				ItemAttrEnchantLV = "수령:1단 ";
				break; // 수령1단
			case 7:
				ItemAttrEnchantLV = "수령:2단 ";
				break; // 수령2단
			case 8:
				ItemAttrEnchantLV = "수령:3단 ";
				break; // 수령3단
			case 9:
				ItemAttrEnchantLV = "수령:4단 ";
				break; // 수령4단
			case 10:
				ItemAttrEnchantLV = "수령:5단 ";
				break; // 수령5단
			case 11:
				ItemAttrEnchantLV = "풍령:1단 ";
				break; // 풍령1단
			case 12:
				ItemAttrEnchantLV = "풍령:2단 ";
				break; // 풍령2단
			case 13:
				ItemAttrEnchantLV = "풍령:3단 ";
				break; // 풍령3단
			case 14:
				ItemAttrEnchantLV = "풍령:4단 ";
				break; // 풍령4단
			case 15:
				ItemAttrEnchantLV = "풍령:5단 ";
				break; // 풍령5단

			case 16:
				ItemAttrEnchantLV = "지령:1단 ";
				break; // 지령1단
			case 17:
				ItemAttrEnchantLV = "지령:2단 ";
				break; // 지령2단
			case 18:
				ItemAttrEnchantLV = "지령:3단 ";
				break; // 지령3단
			case 19:
				ItemAttrEnchantLV = "지령:4단 ";
				break; // 지령4단
			case 20:
				ItemAttrEnchantLV = "지령:5단 ";
				break; // 지령5단
			default:
				break;
			}
			writeC(0x08);
			writeC(0x03);
			writeC(0x10);
			writeBit(MessageType);
			writeC(0x22);
			if (Item.getEnchantLevel() > 0) {
				if (Item.getAttrEnchantLevel() > 0) {
					writeBit(ItemAttrEnchantLV.getBytes().length + ItemEnchantLV.getBytes().length + Item.getItem().getNameId().getBytes().length);
					writeByte(ItemAttrEnchantLV.getBytes());
					writeByte(ItemEnchantLV.getBytes());
					writeByte(Item.getItem().getNameId().getBytes());
				} else {
					writeBit(ItemEnchantLV.getBytes().length + Item.getItem().getNameId().getBytes().length);
					writeByte(ItemEnchantLV.getBytes());
					writeByte(Item.getItem().getNameId().getBytes());
				}
			} else {
				if (Item.getItem().getBless() == 0) {
					writeBit(Bleesed.getBytes().length + Item.getItem().getNameId().getBytes().length);
					writeByte(Bleesed.getBytes());
					writeByte(Item.getItem().getNameId().getBytes());
				} else {
					writeBit(Item.getItem().getNameId().getBytes().length);
					writeByte(Item.getItem().getNameId().getBytes());
				}
			}
			writeC(0x28);
			writeBit(Item.getItem().getItemDescId());
			break;
		}
		writeH(0);
	}

	public S_ACTION_UI(int type, int MessageType, String addmessage, L1ItemInstance Item) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case 리마월드메시지:
			String ItemEnchantLV = " +" + Item.getEnchantLevel();
			String Bleesed = "축복 받은 ";
			String ItemAttrEnchantLV = null;
			switch (Item.getAttrEnchantLevel()) {
			case 1:
				ItemAttrEnchantLV = "화령:1단 ";
				break; // 화령1단
			case 2:
				ItemAttrEnchantLV = "화령:2단 ";
				break; // 화령2단
			case 3:
				ItemAttrEnchantLV = "화령:3단 ";
				break; // 화령3단
			case 4:
				ItemAttrEnchantLV = "화령:4단 ";
				break; // 화령4단
			case 5:
				ItemAttrEnchantLV = "화령:5단 ";
				break; // 화령5단
			case 6:
				ItemAttrEnchantLV = "수령:1단 ";
				break; // 수령1단
			case 7:
				ItemAttrEnchantLV = "수령:2단 ";
				break; // 수령2단
			case 8:
				ItemAttrEnchantLV = "수령:3단 ";
				break; // 수령3단
			case 9:
				ItemAttrEnchantLV = "수령:4단 ";
				break; // 수령4단
			case 10:
				ItemAttrEnchantLV = "수령:5단 ";
				break; // 수령5단
			case 11:
				ItemAttrEnchantLV = "풍령:1단 ";
				break; // 풍령1단
			case 12:
				ItemAttrEnchantLV = "풍령:2단 ";
				break; // 풍령2단
			case 13:
				ItemAttrEnchantLV = "풍령:3단 ";
				break; // 풍령3단
			case 14:
				ItemAttrEnchantLV = "풍령:4단 ";
				break; // 풍령4단
			case 15:
				ItemAttrEnchantLV = "풍령:5단 ";
				break; // 풍령5단

			case 16:
				ItemAttrEnchantLV = "지령:1단 ";
				break; // 지령1단
			case 17:
				ItemAttrEnchantLV = "지령:2단 ";
				break; // 지령2단
			case 18:
				ItemAttrEnchantLV = "지령:3단 ";
				break; // 지령3단
			case 19:
				ItemAttrEnchantLV = "지령:4단 ";
				break; // 지령4단
			case 20:
				ItemAttrEnchantLV = "지령:5단 ";
				break; // 지령5단
			default:
				break;
			}
			writeC(0x08);
			writeC(0x03);
			writeC(0x10);
			writeBit(MessageType);
			writeC(0x22);
			if (Item.getEnchantLevel() > 0) {
				if (Item.getAttrEnchantLevel() > 0) {
					writeBit(addmessage.getBytes().length + ItemAttrEnchantLV.getBytes().length + ItemEnchantLV.getBytes().length
							+ Item.getItem().getNameId().getBytes().length);
					writeByte(addmessage.getBytes());
					writeByte(ItemAttrEnchantLV.getBytes());
					writeByte(ItemEnchantLV.getBytes());
					writeByte(Item.getItem().getNameId().getBytes());
				} else {
					writeBit(addmessage.getBytes().length + ItemEnchantLV.getBytes().length + Item.getItem().getNameId().getBytes().length);
					writeByte(addmessage.getBytes());
					writeByte(ItemEnchantLV.getBytes());
					writeByte(Item.getItem().getNameId().getBytes());
				}
			} else {
				if (Item.getItem().getBless() == 0) {
					writeBit(addmessage.getBytes().length + Bleesed.getBytes().length + Item.getItem().getNameId().getBytes().length);
					writeByte(addmessage.getBytes());
					writeByte(Bleesed.getBytes());
					writeByte(Item.getItem().getNameId().getBytes());
				} else {
					writeBit(addmessage.getBytes().length + Item.getItem().getNameId().getBytes().length);
					writeByte(addmessage.getBytes());
					writeByte(Item.getItem().getNameId().getBytes());
				}
			}
			writeC(0x28);
			writeBit(Item.getItem().getItemDescId());
			break;
		}
		writeH(0);
	}

	public S_ACTION_UI(int descid, L1ItemInstance Item) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(592);
		writeC(0x08);
		writeC(0x03);
		writeC(0x10);
		writeBit(3599);
		writeC(0x22);
		writeBit(Item.getItem().getNameId().getBytes().length);
		writeByte(Item.getItem().getNameId().getBytes());
		writeC(0x28);
		writeBit(descid);
		writeH(0);
	}

	public S_ACTION_UI(int type, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case AINHASAD:
			int value = pc.getAinHasad() / 10000;
			writeC(0x08);
			writeBit(value);

			/** 기본 퍼센테이지 100퍼 */
			int valueTemp = value;
			if (valueTemp > 0)
				valueTemp = 10000;
			writeC(0x10);
			writeBit(valueTemp);

			writeC(0x18);
			writeBit(pc.get축복경험치());

			/** 축복 소모율 **/
			writeC(0x20);
			writeBit(pc.get축복소모효율());

			/** 일일 보너스 **/
			writeC(0x30);
			writeBit(pc.getAinHasadDP());

			writeC(0x38);
			writeBit(0x01);
			/** 혈맹버프가 있따면 버프 타임 돌리긔 */
			if (pc.getClan() != null) {
				short mapId = pc.getMapId();
				if (pc.getClan().isHuntMapChoice()) {
					if (pc.getClan().getBlessHuntMapIds().contains((int) mapId)) {// 요건혈축ㅋㅋㅋ
						writeC(0x28);
						writeBit(1); // 축복 버프
					}
				}
			}
			break;
		}
		writeH(0);
	}

	public S_ACTION_UI(int type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case DOLL_START:
			writeC(0x08);
			writeC(0x03);
			break;
		case CRAFT_ITEM:
			writeH(0x08);
			writeC(0x08);
			writeC(0x03);
			break;
		case CRAFT_OK:
			writeH(0x08);
			break;
		case ATTENDANCE_START:
			writeC(0x08);
			writeBit(60);
			writeC(0x10);
			writeBit(1440);
			writeC(0x18);
			writeBit(0x01);
			writeC(0x20);
			writeBit(0x01);
			writeC(0x28);
			writeBit(0x02);
			break;
		}
		writeH(0);
	}

	/**
	 * PC방,세이프티존 설정 by 맞고
	 * 
	 * @param isOpen on/off
	 **/
	public S_ACTION_UI(int code, boolean isOpen) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(code);
		switch (code) {
		case PCBANG_SET: {
			writeC(0x00);
			writeC(0x08);
			writeC(isOpen ? 1 : 0);
			writeC(0x10);
			writeC(0x01); // 랭킹버튼활성화 패킷
			writeH(0);
			break;
		}
		case SAFETYZONE: {
			writeC(0x01);
			writeC(0x08);
			write7B(isOpen ? 128 : 0);
			writeC(0x10);
			writeC(0x00);
			writeC(0x18);
			writeC(0x00);
			writeH(0);
			break;
		}
		case CRAFT_GAUGEUI:
			writeC(0x08);
			writeC(0x00);
			writeC(0x10);
			writeC(0);
		}
	}

	// 제작테이블
	public S_ACTION_UI(int[] craftList) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CRAFT_ITEMLIST); // 57
		writeC(0x08);
		writeBit(0);
		for (int i = 0; i < craftList.length; i++) {
			int list = craftList[i];
			int size = getBitSize(list) + 5;
			writeC(0x12);
			writeBit(size);
			writeC(0x08);
			writeBit(list);
			writeC(0x10);
			writeBit(0x00);
			writeC(0x18);
			writeBit(0x00);
		}
		writeH(0x00);
	}

	public S_ACTION_UI(L1NpcInstance npc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CRAFT_ITEMLIST);
		writeH(0x08);
		int craftlist[] = null;
		try {
			switch (npc.getNpcId()) { // 69,70,71,72,73,74,75,76,77,78얇은판금 ~ 백금판금

			/** 갑옷 소생장인^테믈 */
			case 102010:
				craftlist = new int[] { 5105, 5106, 5107, 5108, 5109, 5110, 5111, 5112, 5075, 5081, 5084, 5093, 5076, 5077, 5078, 5079, 5080, 5082, 5083, 5085,
						5086, 5087, 5088, 5089, 5090, 5091, 5092, 5094, 5095, 5096, 5097, 5098, 5099, 5100, 5101, 5102, 5103, 5104, 5114 };
				break;

			/** 악세서리 소생장인^모우 */
			case 102011:
				craftlist = new int[] { 5139, 5151, 5152, 5130, 5131, 5132, 5133, 5134, 5135, 5136, 5137, 5138, 5140, 5141, 5142, 5143, 5144, 5145, 5146, 5147,
						5148, 5149, 5150, 5153 };
				break;

			/** 마법서 소생장인^드리 */
			case 102012:
				craftlist = new int[] { 5030, 5031, 5032, 5020, 5021, 5022, 5023, 5024, 5193, 5194, 5025, 5026, 5027, 5028, 5029, 5820, 5821, 5822, 5823, 5824,
						5825 };
				break;

			/** 연금술 소생장인^고 */
			case 102013:
				craftlist = new int[] { 5190, 5171, 5172, 5173, 5174, 5175, 5176, 5177, 5178, 5179, 5180, 5181, 5182, 5183, 5184, 5185, 5186, 5187, 5188,
						5189 };
				break;
			/** 기란마을 - 액세서리 - 드래곤의 수정구 - **/ //
			case 101026:
				craftlist = new int[] { 496, 497 };
				break;

			case 77225:
				craftlist = new int[] { 4078, 4079, 4080, 4081, 4082, 4083, 4084, 4085, 4086, 4087, 4088, 4089 };
				break;
			/** 몽섬 탐스 - **/ //
			case 100778:
				craftlist = new int[] { 2388, 2389, 2390 };
				break;
			/** 기란마을 - 엘릭서 - **/ // 완료
			case 101035:
				craftlist = new int[] { 1043, 1044, 1045, 1046, 1047, 1048 };
				break;
			/****************************************************************************************************/
			/** 웰던마을 - 칼루아 **/ // 완료
			case 100629:
				craftlist = new int[] { 2865, 60, 63, 64 };
				break;
			/** 전설 스킬 이벤트 제작 **/ // 완료
			case 60245:
				craftlist = new int[] { 5726, 6068, 6069, 6070, 6071, 6072, 6073, 6074, 6075, 6076 };
				break;
			/** 웰던마을 - 슈에르메 **/ // 완료
			case 4212002:
				craftlist = new int[] { 103, 104, 105, 106, 107, 108, 109, 496, 497 };
				break;
			/** 웰던마을 - 세심한 슈누 **/ // 완료
			case 4212003:
				craftlist = new int[] { 2583, 2584, 2585, 2586, 2587, 2588, 2589, 2590, 2591, 2592, 2593, 2594 };
				break;
			/** 웰던마을 - 끈질긴 도오호 **/ // 완료
			case 4212004:
				craftlist = new int[] { 2595, 2596, 2597, 2598, 2599, 2600, 2601, 2602, 2603, 2604, 2605, 2606 };
				break;
			/** 기란마을 - 하이오스 **/ // 은기사의 견갑, 사냥꾼의 견갑, 예언자의 견갑, 지휘관의 견갑, 사이하의 견갑, 대마법사의 견갑
			case 4212005:
				craftlist = new int[] { 3043, 3044, 3045, 6129, 6130, 6131, };
				break;
			/** 웰던마을 - 찬란한 바에미 **/ // 완료
			case 4212006:
				craftlist = new int[] { 423, 424, 425, 2182, 2183, 2184, 2227, 2185 };
				break;
			/** 웰던마을 - 조우의 불골렘 **/ // 완료 선택한 아이템으로 제작되게끔 추후수정 //완료
			case 100029:
				craftlist = new int[] { 515, 514, 516, 517, 116, 123, 159, 160, 161, 162, 271, 272, 273, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284,
						285, 286, 287, 288, 289, 290 };
				break;
			/** 잊섬 - 럭키 **/ // 럭키완료
			case 7310086:
				craftlist = new int[] { 1771, 1772, 1773, 1774, 1775, 1776, 1777, 1778, };
				break;
			/****************************************************************************************************/
			/** 용기사마을 - 대장장이 퓨알 **/ // 완료
			case 4218001:
				craftlist = new int[] { 62, 65, 63, 64, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78 };
				break;
			/** 환술사마을 - 대장장이 바트르 **/ // 완료 ////완료신규
			case 4219001:
				craftlist = new int[] { 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78 };
				break;
			/****************************************************************************************************/
			/** 화전민마을 - 라이라 **/ // 완료 ////신규 완료 라이
			case 70811:
				craftlist = new int[] { 417, 418, 419, 420 };
				break;
			/****************************************************************************************************/
			/** 오렌마을3층 - 타라스 리뉴얼 **/ // 완료 신규완료
			case 70763:
				craftlist = new int[] { 212, 213, 214, 2057, 2058, 2059, 2060, 2061, 2064, 2063, 2065 };
				break;
			/****************************************************************************************************/
			/** 아덴마을 - 무브니 **/ // 완료 //신규완료
			case 6000101:
				craftlist = new int[] { 4589, 823, 824, 825, 826, 827, 828, 829, 830, 831, 832, 833, 834, 835, 836, 837, 838, 839, 840, 841, 842, 843, 844, 845,
						846, 847, 848, 849, 850, 851, 852 };
				break;
			/****************************************************************************************************/
			/** 연구실 - 세이룬 **/ // 완료
			case 100644:
				craftlist = new int[] { 577, 578, 579, 580, 581, 4354, 4355, 4356, 4357, 4358, 4359, 4360, 4361, 4362, 4363, 4364, 4365, 4366, 4367, 4368 };
				break;
			/** 드워프의 대장간 */
			case 60238:
				craftlist = new int[] { 3996, 4091 };
				break;
			/****************************************************************************************************/
			/** 아타로제 **/ // 완료
			case 71119:
				craftlist = new int[] { 56 };
				break;
			/** 아델리오 **/ // 완료
			case 71125:
				craftlist = new int[] { 704, 706, 707, 705, 305, 309, 313, 1734, 1735, 1736, 1737, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55 };
				break;

			/** 모리아 **/ // 완료 진행중
			case 70598:
				craftlist = new int[] { 5892, 1043, 1044, 1045, 1046, 1047, 1048 };
				break;// 198 찾아야함
			/** 레옹 신규 **/ // 완료
			case 11887:
				craftlist = new int[] { 1771, 1772, 1773, 1774, 1775, 1776, 1777, 1778, 1763, 1764, 1765, 1766, 1767, 1768, 1769, 1770 };
				break;
			/** 안톤신규 **/ // 드래곤 슬레이어 각반
			case 70614:
				craftlist = new int[] { 6132 };
				break;
			/** 문장 강화 수정 **/ // 유물주머니빼고완료
			case 301028:
				craftlist = new int[] { 3385, 3386, 3387, 2747, 4396, 4397, 4398, 4399, 4400, 4401, 4402, 4403, 4404, 4405, 4406, 4407 };
				break;
			/** 추가 시작 **/ // 네루파 완료
			case 70838:
				craftlist = new int[] { 2864, 2857, 2865, 2861, 2768, 2769, 2770, 2771, 2772, 2773, 2774, 2759, 173, 183, 211 };
				break; // 네루파

			case 70840: // 로빈
				craftlist = new int[] { 2762, 2864, 2857, 2865, 2861, 2768, 2769, 2770, 2771, 2772, 2773, 2774, 2759, 173, 183, 211 };
				break; // 네루파

			/** 레서 데몬(발록) **/ //// 완료
			case 80069:
				craftlist = new int[] { 35, 36, 37, 38 };
				break;
			/** 발록진영 - 발록의 분신 **/
			case 80068:
				craftlist = new int[] { 39, 40, 41, 42 };
				break;
			/** 래리 보좌관 **/
			case 70652:
				craftlist = new int[] { 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 32, 33 };
				break;
			/** 엔트 **/
			case 70848:
				craftlist = new int[] { 188, 189, 190 };
				break;
			/** 나르엔 **/
			case 70837:
				craftlist = new int[] { 186 };
				break;
			/** 주티스 (솔로부대이벤트상자) **/
			case 1:
				craftlist = new int[] { 360, 361, 362, 363 };
				break;
			/** 가웨인 **/
			case 7000078:
				craftlist = new int[] { 159, 160 };
				break;
			/** 루디엘 **/
			case 70841:
				craftlist = new int[] { 187, 188 };
				break;
			/** 아라크네 **/
			case 70846:
				craftlist = new int[] { 163, 164, 165 };
				break;
			/** 판 **/
			case 70850:
				craftlist = new int[] { 166, 167, 168 };
				break;
			/** 페어리 **/
			case 70851:
				craftlist = new int[] { 169, 170 };
				break;
			/** 페어리 퀸 **/
			case 70852:
				craftlist = new int[] { 169, 170, 171 };
				break;
			/** 야히진영 - 야히의 대장장이 **/
			case 80053:
				craftlist = new int[] { 156, 2044 };
				break;
			/** 야히진영 - 연구원 **/
			case 80054:
				craftlist = new int[] { 7, 8, 9 };
				break;
			/** 야히진영 - 야히 **/
			case 80051:
				craftlist = new int[] { 6 };
				break;
			/** 발록진영 - 발록의 대장장이 **/
			case 80072:
				craftlist = new int[] { 156, 2044, 35, 36, 37, 38, 3378, 141, 142, 143, 144, 145, 146, 147, 148, 39, 40, 2036, 1805, 1806, 1807, 1808, 1809 };
				break;
			/** 리넴 **/
			case 102019:
				craftlist = new int[] { 708, 709, 710, 711, 156, 2044, 35, 36, 37, 38, 3378, 141, 142, 143, 144, 145, 146, 147, 148, 39, 40, 2036, 1805, 1806,
						1807, 1808, 1809 };
				break;
			/** 콜트 **/
			case 102020:
				craftlist = new int[] { 3196, 2788, 2789, 194, 195, 196 };
				break;
			// 아놀드 이벤트 엔피씨
			case 6:
				craftlist = new int[] { 1629, 1630, 1631, 1632, 1633, 1634, 1635, 1636, 1637, 1638, 1639, 1640, 1641, 1642, 1643, 1644, 1645, 1646 };
				break;
			case 8:
				craftlist = new int[] { 1647, 1648, 1649, 1650, 1651, 1652, 1653, 1654, 1655 };
				break;

			// 바레트 완료
			case 70520:
				craftlist = new int[] { 1861, 95, 96, 97, 98, 99, 1960, 1961 };
				break;

			// 고대 물품 연금술사^아만
			case 7210073:
				craftlist = new int[] { 2652, 2653 };
				break;

			// 보석상인_디오 완료
			case 70027:
				craftlist = new int[] { 2779, 2780, 2781, 2782, 3420, 2739, 2792, 2731, 2732, 2733, 2734, 2735, 2736, 2737, 2738, 2793, 2790, 2791 };
				break;

			// 류미엘 //완료
			case 70676:
				craftlist = new int[] { 238, 241, 240, 239, 232, 233, 234, 237, 236, 235, 228, 230, 231, 229 };
				break;

			// 서큐버스 //완료
			case 87000:
				craftlist = new int[] { 2879, 2880 };
				break;

			/** 켄트성 마을, 이벨빈(숨결 강화술사) **/ // by.초작살미남
			case 70662:
				craftlist = new int[] { 5913, 5914, 5915, 5916, 5917, 5918, 5919, 5920, 5921, 5922, // 아인하사드의섬광 //할파스 숨결 러쉬
						5933, 5934, 5935, 5936, 5937, 5938, 5939, 5940, 5941, 5942, // 그랑카인의심판 //할파스 숨결 러쉬

						5893, 5894, 5895, 5896, 5897, 5898, 5899, 5900, 5901, 5902, // 사신의검 //숨결
						3715, 3716, 3717, 3718, 3719, 3720, 3721, 3722, 3723, 3724, // 집명황의 집행검 //숨결
						3728, 3729, 3730, 3731, 3732, 3733, 3734, 3735, 3736, 3737, // 바람칼날의 단검 //숨결
						3741, 3742, 3743, 3744, 3745, 3746, 3747, 3748, 3749, 3750, // 수정결정체 지팡이 //숨결
						3754, 3755, 3756, 3757, 3758, 3759, 3760, 3761, 3762, 3763, // 붉은 그림자의 이도류 //숨결
						3767, 3768, 3769, 3770, 3771, 3772, 3773, 3774, 3775, 3776, // 가이아의 격노 //숨결
						3780, 3781, 3782, 3783, 3784, 3785, 3786, 3787, 3788, 3789, // 크로노스의 공포 //숨결
						3793, 3794, 3795, 3796, 3797, 3798, 3799, 3800, 3801, 3802, // 히페리온의 절망 //숨결
						3806, 3807, 3808, 3809, 3810, 3811, 3812, 3813, 3814, 3815, // 타이탄의 분노 //숨결

						5903, 5904, 5905, 5906, 5907, 5908, 5909, 5910, 5911, 5912, // 사신의 검 //사신숨결
						4429, 4430, 4431, 4432, 4433, 4434, 4435, 4436, 4437, 4438, // 집명황의 집행검 //사신숨결
						4442, 4443, 4444, 4445, 4446, 4447, 4448, 4449, 4450, 4451, // 바람칼날의 단검 //사신숨결
						4455, 4456, 4457, 4458, 4459, 4460, 4461, 4462, 4463, 4464, // 수정결정체 지팡이 //사신숨결
						4468, 4469, 4470, 4471, 4472, 4473, 4474, 4475, 4476, 4477, // 붉은 그림자의 이도류 //사신숨결
						4481, 4482, 4483, 4484, 4485, 4486, 4487, 4488, 4489, 4490, // 가이아의 격노 //사신숨결
						4494, 4495, 4496, 4497, 4498, 4499, 4500, 4501, 4502, 4503, // 크로노스의 공포 //사신숨결
						4507, 4508, 4509, 4510, 4511, 4512, 4513, 4514, 4515, 4516, // 히페리온의 절망 //사신숨결
						4520, 4521, 4522, 4523, 4524, 4525, 4526, 4527, 4528, 4529, // 타이탄의 분노 //사신숨결

						2571, 2575, 2579, 2572, 2576, 2580, 2573, 2577, 2581, 2574, 2578, 2582, // 안타완력 789 //파푸완력 789 //린드완력 789 //발라완력 789
						2607, 2611, 2615, 2608, 2612, 2616, 2609, 2613, 2617, 2610, 2614, 2618, // 안타예지 789 //파푸예지 789 //린드예지 789 //발라예지 789
						2595, 2599, 2603, 2596, 2600, 2604, 2597, 2601, 2605, 2598, 2602, 2606, // 안타인내 789 //파푸인내 789 //린드인내 789 //발라인내 789
						2583, 2587, 2591, 2584, 2588, 2592, 2585, 2589, 2593, 2586, 2590, 2594, // 안타마력 789 //파푸마력 789 //린드마력 789 //발라마력 789

						6195, 6196, 6197, 6198, 6200, 6199, 6201, 6202, 6203, 6204, // 발라장검(포르세), 발라장검(듀크데필), 발라양검(나발), 파푸장궁(악몽), 파푸이도류(포효), 파푸이도류(진노),
																					// 안타도끼(태풍), 안타지팡이(제로스), 린드체인소드(섬체), 린드키링크(살키)
						6214, 6215, 6216, 6217, 6219, 6218, 6220, 6221, 6222, 6223, // 속성 1단
						6224, 6225, 6226, 6227, 6229, 6228, 6230, 6231, 6232, 6233, // 속성 2단
						6234, 6235, 6236, 6237, 6239, 6238, 6240, 6241, 6242, 6243, // 속성 3단
						6244, 6245, 6246, 6247, 6249, 6248, 6250, 6251, 6252, 6253, // 속성 4단
						6254, 6255, 6256, 6257, 6259, 6258, 6260, 6261, 6262, 6263, // 속성 5단
				};

				break;

			/** 켄트성 마을, 쿠프(드래곤 제작상인) **/
			case 70904:
				craftlist = new int[] { 203, 204, 205, 206, 207, 208, 209, 210, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90, 91, 92, 93, 94, 220, 226, 223,
						219, 225, 222, 221, 224, 216, 215, 217, 218, 70, 71, 72, 73, 74, 75, 76, 77, 78, 103, 104, 105, 106, 107, 108, 109, 6133, 6134 // 지룡,수룡,화룡,풍룡,탄생,형생,생명,흑룡,절대
																																						// 마안
						, 6135, 6136, 6137// 할파스 갑옷 제작
						, 6138, 6139, 6140, 6141, 6142, 6143, 6144, 6145, 6146/** 할파스완력 인첸 */
						, 6147, 6148, 6149, 6150, 6151, 6152, 6153, 6154, 6155/** 할파스예지력인첸 */
						, 6156, 6157, 6158, 6159, 6160, 6161, 6162, 6163, 6164/** 할파스마력인첸 */
						, 6165, 6166, 6167, 6168, 6169, 6170, 6171, 6172, 6173, 6174/** 할파스완력교환 */
						, 6175, 6176, 6177, 6178, 6179, 6180, 6181, 6182, 6183, 6184/** 할파스예지력교환 */
						, 6185, 6186, 6187, 6188, 6189, 6190, 6191, 6192, 6193, 6194/** 할파스마력교환 */
				};
				break;
			// 고대 쿠프테스트
			case 709044:
				craftlist = new int[] { Config.CRAFT_TABLE_ONE, Config.CRAFT_TABLE_TWO, Config.CRAFT_TABLE_THREE, Config.CRAFT_TABLE_FOUR,
						Config.CRAFT_TABLE_FIVE, Config.CRAFT_TABLE_SIX, Config.CRAFT_TABLE_SEVEN, Config.CRAFT_TABLE_EIGHT, Config.CRAFT_TABLE_NINE,
						Config.CRAFT_TABLE_TEN };
				break;
			// 마법의문자
			case 100625:
				craftlist = new int[] { 3879, 3880, 3881, 3882, 110, 111, 112, 113, 114, 115, 520 };
				break;

			// 3408,3409 고대의 은빛릴, 고대의금빛릴

			case 460000128:
				craftlist = new int[] { Config.CRAFT_TABLE_ONE, Config.CRAFT_TABLE_TWO, Config.CRAFT_TABLE_THREE, Config.CRAFT_TABLE_FOUR,
						Config.CRAFT_TABLE_FIVE, Config.CRAFT_TABLE_SIX, Config.CRAFT_TABLE_SEVEN, Config.CRAFT_TABLE_EIGHT, Config.CRAFT_TABLE_NINE,
						Config.CRAFT_TABLE_TEN };
				break;

			case 870001:
				int t = Config.CRAFT_TABLE;
				craftlist = new int[] { t, t + 1, t + 2, t + 3, t + 4, t + 5, t + 6, t + 7, t + 8, t + 9 };
				break;
			case 870002:
				int tt = Config.CRAFT_TABLE + 10;
				craftlist = new int[] { tt, tt + 1, tt + 2, tt + 3, tt + 4, tt + 5, tt + 6, tt + 7, tt + 8, tt + 9 };
				break;
			case 870003:
				int ttt = Config.CRAFT_TABLE + 20;
				craftlist = new int[] { ttt, ttt + 1, ttt + 2, ttt + 3, ttt + 4, ttt + 5, ttt + 6, ttt + 7, ttt + 8, ttt + 9 };
				break;
			case 870004:
				int tttt = Config.CRAFT_TABLE + 30;
				craftlist = new int[] { tttt, tttt + 1, tttt + 2, tttt + 3, tttt + 4, tttt + 5, tttt + 6, tttt + 7, tttt + 8, tttt + 9 };
				break;
			case 870005:
				int ttttt = Config.CRAFT_TABLE + 40;
				craftlist = new int[] { ttttt, ttttt + 1, ttttt + 2, ttttt + 3, ttttt + 4, ttttt + 5, ttttt + 6, ttttt + 7, ttttt + 8, ttttt + 9 };
				break;
			case 870006:
				int tttttt = Config.CRAFT_TABLE + 50;
				craftlist = new int[] { tttttt, tttttt + 1, tttttt + 2, tttttt + 3, tttttt + 4, tttttt + 5, tttttt + 6, tttttt + 7, tttttt + 8, tttttt + 9 };
				break;
			case 870007:
				int ttttttt = Config.CRAFT_TABLE + 60;
				craftlist = new int[] { ttttttt, ttttttt + 1, ttttttt + 2, ttttttt + 3, ttttttt + 4, ttttttt + 5, ttttttt + 6, ttttttt + 7, ttttttt + 8,
						ttttttt + 9 };
				break;
			case 870008:
				int tttttttt = Config.CRAFT_TABLE + 70;
				craftlist = new int[] { tttttttt, tttttttt + 1, tttttttt + 2, tttttttt + 3, tttttttt + 4, tttttttt + 5, tttttttt + 6, tttttttt + 7,
						tttttttt + 8, tttttttt + 9 };
				break;
			case 870009:
				int ttttttttt = Config.CRAFT_TABLE + 80;
				craftlist = new int[] { ttttttttt, ttttttttt + 1, ttttttttt + 2, ttttttttt + 3, ttttttttt + 4, ttttttttt + 5, ttttttttt + 6, ttttttttt + 7,
						ttttttttt + 8, ttttttttt + 9 };
				break;
			case 870010:
				int tttttttttt = Config.CRAFT_TABLE + 90;
				craftlist = new int[] { tttttttttt, tttttttttt + 1, tttttttttt + 2, tttttttttt + 3, tttttttttt + 4, tttttttttt + 5, tttttttttt + 6,
						tttttttttt + 7, tttttttttt + 8, tttttttttt + 9 };
				break;
			// 요단_구원의 신녀
			case 450001831:
				craftlist = new int[] { 3870, 3871 };
				break;
			}

			int num;
			for (int i = 0; i < craftlist.length; i++) {
				writeC(0x12);
				num = craftlist[i];
				if (num > 127) {
					writeC(0x07);
				} else {
					writeC(0x06);
				}
				writeC(0x08);
				write4bit(num);
				writeH(0x10);
				writeH(0x18);
			}
			writeH(0x00);
		} catch (Exception e) {

		}
	}

	/**
	 * 혈맹관련
	 */
	public S_ACTION_UI(String clanname, int rank) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x19);
		writeC(0x02);
		writeC(0x0a);
		int length = 0;
		if (clanname != null)
			length = clanname.getBytes().length;
		if (length > 0) {
			writeC(length); // 클랜명 SIZE
			writeByte(clanname.getBytes()); // 클랜명
			writeC(0x10);
			writeC(rank); // 클랜 랭크
		} else {
			writeC(0x00);
		}
		writeH(0x00);
	}

	public S_ACTION_UI(int type, int value, boolean isPCRoom) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case ATTENDANCE_TIMEOVER:
			writeC(0x08);
			writeC(value);
			writeC(0x10);
			writeC(isPCRoom ? 0x01 : 0x00);
			writeC(0x18);
			writeC(2);
			break;
		case SPELL_PASSIVE_ONOFF_ACK:
			writeC(0x08);
			writeBit(1);
			writeC(0x10);
			writeBit(value);
			writeC(0x18);
			writeBit(isPCRoom ? 1 : 0);
			break;
		}
		writeH(0);
	}

	/**
	 * 전사스킬을 위해
	 */
	public S_ACTION_UI(int type, int skillnum) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case DOLL_READY:
			writeC(0x08);
			writeBit(skillnum);
			break;
		case Elixir:
			writeC(0x08);
			writeBit(skillnum);
			break;
		case stateProfile:
			writeC(0x0a); // 힘
			if (skillnum == 45) {
				writeH(0x0808);
				writeC(skillnum);
				writeD(0x03180310);
				writeH(0x0120);
			} else {
				writeH(0x0806);
				writeC(skillnum);
				writeD(0x01180110);
			}

			writeC(0x12); // 인트
			if (skillnum == 45) {
				writeH(0x0808);
				writeC(skillnum);
				writeD(0x03180310);
				writeH(0x0120);
			} else {
				writeH(0x0806);
				writeC(skillnum);
				writeD(0x01180110);
			}

			writeC(0x1a);
			if (skillnum == 25) {
				writeH(0x0806);
				writeC(skillnum);
				writeD(0x01180110);
				writeH(0x3238);
			} else if (skillnum == 35) {
				writeH(0x0808);
				writeC(skillnum);
				writeD(0x01180110);
				writeH(0x6438);
			} else if (skillnum == 45) {
				writeH(0x0809);
				writeC(skillnum);
				writeD(0x03180310);
				writeC(0x38);
				writeH(0x0196);
			}

			writeC(0x22);
			if (skillnum == 45) {
				writeH(0x0808);
				writeC(skillnum);
				writeD(0x03180310);
				writeH(0x0120);
			} else {
				writeH(0x0806);
				writeC(skillnum);
				writeD(0x01180110);
			}

			writeC(0x2a);
			if (skillnum == 25) {
				writeH(0x0806);
				writeC(skillnum);
				writeD(0x32300110);
			} else if (skillnum == 35) {
				writeH(0x0808);
				writeC(skillnum);
				writeD(0x01180110);
				writeH(0x6430);
			} else if (skillnum == 45) {
				writeH(0x0809);
				writeC(skillnum);
				writeD(0x02180310);
				writeC(0x30);
				writeH(0x0196);
			}

			writeD(0xff080b32);
			writeD(0xffffffff);
			writeD(0xffffffff);
			writeC(0x01);
			break;
		case ATTENDANCE_ITEM:
			writeC(0x08);
			writeC(skillnum);

			AttendanceTable attend = AttendanceTable.getInstance();
			Map<Integer, ArrayList<AttendanceTable.AttendanceItem>> list = skillnum == 1 ? attend.attendancePcList() : attend.attendanceList();

			Set<Integer> keys = list.keySet();
			int index;
			ArrayList<AttendanceTable.AttendanceItem> items;
			for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
				index = iterator.next();
				items = list.get(index);

				byte[] attendance_item_info = sendAttendancePacketToRandom(items);
				int main_size = attendance_item_info.length + getBitSize(index) + 1;
				// System.out.println("순서 : " + index + " / 메인사이즈 : " + main_size + " / 아이템사이즈 :
				// " + items.size());

				writeC(0x12);
				writeBit(main_size);

				writeC(0x08);
				writeBit(index);

				writeByte(attendance_item_info);
			}
			break;
		case LOGIN_ADD_SKILL:
			writeC(0x0a);
			writeBit(skillnum == 5 ? 4L : 2L);
			writeC(0x08);
			writeBit(skillnum);
			if (skillnum != 5)
				break;
			writeC(0x10);
			writeC(0x0a);
			break;
		case ADD_SKILL:
			writeC(0x08);
			writeBit(skillnum);
			if (skillnum == 5) { // 아머가드
				writeC(0x10);
				writeC(0x0a);
			}
			break;
		case TAM:
			writeC(0x08);
			write4bit(skillnum);
			break;
		case BMTYPE_DEL_CHECK_ACK:
		case CLAN_JOIN_MESSAGE:
		case PETBALL_CONTENTS_START_ACK:
			writeC(0x08);
			writeBit(skillnum);
			break;
		}
		writeH(0x00);
	}

	public S_ACTION_UI(int type, Account account, boolean isCheck) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case ATTENDANCE_COMPLETE:
			int normal_minute = 60 - account.getAttendanceHomeTime();
			int pc_minute = 60 - account.getAttendancePcHomeTime();
			int normal_size = getBitSize(normal_minute) + 7;
			int pc_size = getBitSize(pc_minute) + 7;
			writeC(0x0a);
			writeBit(normal_size);
			writeC(0x08);
			writeBit(0); // 일반 0 , 피씨방 1
			writeC(0x10);
			writeBit(normal_minute); // 출석 남은 시간
			writeC(0x18);
			writeBit(account.isAttendanceHome() ? 1 : 0); // 완료한 총 횟수
			writeC(0x20);
			writeBit(0x01); // 완료가능한 총 횟수

			writeC(0x0a);
			writeBit(pc_size);
			writeC(0x08);
			writeBit(1); // 일반 0 , 피씨방 1
			writeC(0x10);
			writeBit(isCheck ? pc_minute : 60); // 피씨방 출석 남은시간
			writeC(0x18);
			writeBit(account.isAttendancePcHome() ? 1 : 0); // 완료한 총 횟수
			writeC(0x20);
			writeBit(0x01); // 완료가능한 총 횟수

			for (int i = 0; i < account.getAttendanceHomeBytes().length; i++) {
				writeC(0x12);
				writeBit(0x08);
				writeC(0x08);
				writeBit(i + 1);
				writeC(0x10);
				writeBit(0);
				writeC(0x18);
				int state = account.getAttendanceHomeBytes()[i] == 2 ? 3 : account.getAttendanceHomeBytes()[i] == 1 ? 2 : 1;
				writeBit(state);
				writeC(0x20);
				int time = state == 3 || state == 2 ? 60 : 0;
				writeBit(time);

				if (state == 1)
					break;
			}

			for (int i = 0; i < account.getAttendancePcBytes().length; i++) {
				writeC(0x12);
				writeBit(0x08);
				writeC(0x08);
				writeBit(i + 1);
				writeC(0x10);
				writeBit(1);
				writeC(0x18);
				int state = account.getAttendancePcBytes()[i] == 2 ? 3 : account.getAttendancePcBytes()[i] == 1 ? 2 : 1;
				writeBit(state);
				writeC(0x20);
				int time = state == 3 || state == 2 ? 60 : 0;
				writeBit(time);

				if (state == 1)
					break;
			}
			break;
		}

		writeH(0);
	}

	public S_ACTION_UI(int type, int exp1, int exp2) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case CLAN_JOIN_SETTING:
			writeC(0x08);
			writeC(0x01);
			writeC(0x10);
			writeBit(exp1);
			writeC(0x18);
			writeBit(exp2);
			writeC(0x22);
			writeBit(20);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			break;
		case EMOTICON:
			writeC(0x08);
			writeBit(exp2);
			writeC(0x10);
			writeBit(0x02);
			writeC(0x18);
			writeBit(exp1);
			break;
		}
		writeH(0);
	}

	private byte[] sendAttendancePacket(L1Item item, L1ItemInstance dummy) {
		BinaryOutputStream os = new BinaryOutputStream();

		os.writeC(0x08);
		os.writeC(0x02); // type 1:즉시사용 , 2:아이템지급

		os.writeC(0x10);
		os.writeBit(item.getItemDescId());

		os.writeC(0x18);
		os.writeBit(dummy.getCount());

		os.writeC(0x22);
		os.writeBit(item.getName().getBytes().length);
		os.writeByte(item.getName().getBytes());

		os.writeC(0x28);
		os.writeBit(0x00);

		os.writeC(0x30);
		os.writeBit(item.getGfxId()); //

		os.writeC(0x38);
		os.writeBit(item.getBless()); // 아이템 블레스

		os.writeC(0x42);
		os.writeBit(item.getNameId().getBytes().length);
		os.writeByte(item.getNameId().getBytes());

		os.writeC(0x4a);
		byte[] status = dummy.getStatusBytes();
		os.writeBit(status.length);
		os.writeByte(status);

		os.writeC(0x50);
		os.writeBit(dummy.getItem().getType2() == 1 ? dummy.getAttrEnchantLevel() : -1); // 속성인챈

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return os.getBytes();
	}

	private byte[] sendAttendancePacketToRandom(ArrayList<AttendanceTable.AttendanceItem> list) {
		BinaryOutputStream os = new BinaryOutputStream();
		try {
			int size = list.size();
			for (int i = 0; i < size; i++) {
				L1ItemInstance dummy = new L1ItemInstance();
				AttendanceItem _item = list.get(i);
				L1Item item = ItemTable.getInstance().getTemplate(_item._itemId);
				dummy.setItem(item);
				dummy.setCount(_item._count);

				byte[] stats = sendAttendancePacket(item, dummy);
				os.writeBit(0x12);
				os.writeBit(stats.length);
				os.writeByte(stats);
			}

			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_ACTION_UI;
	}
}
