package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

/**
 * 스킬 아이콘이나 차단 리스트의 표시 등 복수의 용도에 사용되는 패킷의 클래스
 */
public class S_SabuBox extends ServerBasePacket {
	private static final String S_SabuBox = "[S] S_SabuBox";

	private byte[] _byte = null;
	/*
	 * [OPCODE = 33] 0000: 21 41 06 5a 90 1b 01 01 00 00 00 5b 90 1b 01 02
	 * !A.Z.......[.... 0010: 00 00 00 5c 90 1b 01 05 00 00 00 5d 90 1b 01 06
	 * ...\.......].... 0020: 00 00 00 5e 90 1b 01 07 00 00 00 59 90 1b 01 08
	 * ...^.......Y.... 0030: 00 00 00 48 4e ...HN
	 */
	public static final int 아이템장착슬롯관리전체 = 65;
	public static final int 아이템장착슬롯관리 = 66;
	public static final int 의문1 = 67;
	public static final int 의문2 = 68;
	public static final int 파워북검색 = 25;
	public static final int 몰라 = 207;

	/**
	 * 찾아야할 의문의 패킷 [OPCODE = 33] 0000: 21 43 01 00 00 00 03 00 00 00 00 00 00 00
	 * 00 !C.............. 0010: 00 00 00 00 00 00 00 00 00 00 .........
	 * 
	 * [OPCODE = 33] 0000: 21 43 02 00 00 00 01 00 00 00 00 00 00 00 00 00
	 * !C.............. 0010: 00 00 00 00 00 00 00 b1 c4 .........
	 * 
	 * [OPCODE = 33] 0000: 21 44 01 00 00 00 0c 61 7f !D.....a
	 * 
	 * [OPCODE = 33] 0000: 21 43 01 00 00 00 03 00 00 00 00 00 00 00 00 00
	 * !C.............. 0010: 00 00 00 00 00 00 00 00 00 .........
	 * 
	 * [OPCODE = 33] 0000: 21 43 02 00 00 00 01 00 00 00 00 00 00 00 00 00
	 * !C.............. 0010: 00 00 00 00 00 00 00 00 00 .........
	 * 
	 * [OPCODE = 33] 0000: 21 44 01 00 00 00 0c c5 3c !D......<
	 */
	public S_SabuBox(int subCode) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 의문2:
			writeC(0x01);
			writeH(0x0000);
			writeC(0x00);
			writeC(0x0C);
			writeC(0xc5);
			writeC(0x3c);
			break;
		// 0000: 2c b5 18 00 14 00 14 00 14 00 ,.........

		case 몰라:
			writeC(0x18);
			writeC(0x00);
			writeC(0x14);
			writeC(0x00);
			writeC(0x14);
			writeC(0x00);
			writeC(0x14);
			writeC(0x00);

			break;
		default:
			break;
		}
	}

	/**
	 * 0000: 21 43 01 00 00 00 03 00 00 00 00 00 00 00 00 00 !C..............
	 * 0010: 00 00 00 00 00 00 00 00 00 ......... 0000: 21 43 02 00 00 00 01 00
	 * 00 00 00 00 00 00 00 00 !C.............. 0010: 00 00 00 00 00 00 00 00 00
	 * .........
	 */
	public S_SabuBox(int subCode, int value) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 의문1:
			writeC(value);
			writeH(0x0000);
			writeC(0);
			if (value == 1) {
				writeC(3);
			} else {
				writeC(1);
			}
			writeD(0x00000000);
			writeD(0x00000000);
			writeD(0x00000000);
			writeD(0x00000000);
			writeH(0x0000);
			break;
		default:
			break;
		}
	}

	public S_SabuBox(int subCode, int obj, int type, boolean 장착) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 아이템장착슬롯관리:
			writeD(obj);
			writeC(type);
			writeC(장착 ? 0x01 : 0x00);
			writeH(0x00);
			break;
		default:
			break;
		}
	}

	public S_SabuBox(int subCode, int type, int time) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 1:
			break;
		default:
			break;
		}
	}

	public S_SabuBox(int subCode, int type, int petid, int ac) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);

		switch (subCode) {
		case 1:
			break;
		default:
			break;
		}
	}

	public S_SabuBox(int subCode, L1ItemInstance item) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 1:
			break;
		default:
			break;
		}
	}

	/**
	 * 0f 19 00 a6 a1 24 2c 40 2e 2c 46 81 79 56 10 38 1a 77 72
	 * 
	 * 79 aa db 0a 7b cd cf b8 3d e6 9c 2a 30 2d 5e 9a
	 * 
	 * 64 13 79 aa db 0a 7b cd cf b8 3d e6 9c 2a 30 2d d.y...{...=..*0- 5e 9a 5c
	 * 00 ^.\.
	 */

	public S_SabuBox(int subCode, String val) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 파워북검색:
			writeC(0x00);
			writeD(0x2c24a1a6);
			writeD(0x462c2e40);
			writeD(0x10567981);
			writeD(0x72771a38);
			writeS(val);
			break;
		default:
			break;
		}
	}

	public S_SabuBox(int subCode, Object[] names) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 1:
			writeC(names.length);
			for (Object name : names) {
				writeS(name.toString());
			}
			break;
		default:
			break;
		}
	}

	public S_SabuBox(int subCode, int id, String name, String clanName) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 1:
			break;
		default:
			break;
		}
	}

	public S_SabuBox(L1PcInstance pc, int subCode) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(subCode);
		switch (subCode) {
		case 1:
			break;
		default:
			break;
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
		return S_SabuBox;
	}
}
