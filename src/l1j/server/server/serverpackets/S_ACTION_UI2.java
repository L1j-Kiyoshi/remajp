package l1j.server.server.serverpackets;

import java.util.Collection;
import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ACTION_UI2 extends ServerBasePacket {
	private byte[] _byte = null;

	private static final String S_ACTION_UI2 = "S_ACTION_UI2";
	
	public static final int CLAN_JOIN_WAIT = 0x45;
	public static final int unk1 = 0x41;
	public static final int unknown1 = 0x4E;
	public static final int unknown2 = 0x91;
	public static final int CLAN_RANK = 0x19;
	public static final int ICON_BUFF = 0x6e;

	public S_ACTION_UI2(int type, boolean ck) {
		buildPacket(type, ck);
	}

	public S_ACTION_UI2(int i, int t, int o, int gf, int ms) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(ICON_BUFF);
		writeC(0x00);
		writeC(0x08);
		writeC(0x02);
		writeC(0x10);
		write7B(i);
		writeC(0x18);
		write7B(t);
		writeC(0x20);
		writeC(0x09);
		writeC(0x28);
		write7B(gf);
		writeC(0x30);
		writeC(0x00);
		writeC(0x38);
		writeC(o);
		writeC(0x40);
		write7B(ms);
		writeC(0x48);
		writeC(0x00);
		writeC(0x50);
		writeC(0x00);
		writeC(0x58);
		writeC(0x01);
		writeH(0);
	}

	private void buildPacket(int type, boolean ck) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case CLAN_JOIN_WAIT:
			writeC(1);
			writeC(8);
			writeC(2);
			writeH(0);
			break;
		}
	}

	public S_ACTION_UI2(int subCode) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(subCode);
		switch (subCode) {

			case unknown2:// 모름
				writeC(01);
				writeC(0x88);
				writeC(0xd4);
				break;
			case unk1:
				writeC(1);
				writeC(8);
				writeC(0x80);
				writeC(0xe1);
				writeC(1);
				writeC(0x10);
				writeC(0xe5);
				writeC(0xe0);
				writeC(1);
				writeC(0x4a);
				writeC(0);
				break;
			case unknown1:
				writeC(1);
				writeC(8);
				writeC(3);
				writeC(0x10);
				writeC(0);
				writeC(0x18);
				writeC(0);
				writeH(0);
				break;
			case CLAN_JOIN_WAIT:
				writeC(0x01);
				writeH(0x0208);
				int size = 1;
	
				if (size > 0) {
					Collection<L1PcInstance> list = L1World.getInstance()
							.getAllPlayers();
					int i = 0;
					for (L1PcInstance pc : list) {
						writeC(0x12);
						if (i == 0)
							writeC(39);
						else if (i == 1)
							writeC(38);
						else if (i == 2)
							writeC(40);
						else
							writeC(39);
	
						writeC(0x08);
						writeD(0x1203A9A2);
						writeD(0xC5C3B208);
						writeD(0xB7ACC5EB);
						writeH(0x18B4);
	
						if (i == 0 || i >= 3) {
							writeD(0x02D8D1BE);
						} else {
							writeC(0xC3);
							writeH(0x5C8A);
						}
						writeC(0x22);
						byte[] name = pc.getName().getBytes();
						writeC(name.length);
						writeByte(name);
						writeC(0x2A);
						writeC("1".getBytes().length);// name.length);
						writeByte("1".getBytes());
						writeC(0x30);
						writeC(L1World.getInstance().getPlayer(pc.getName()) != null ? 1
								: 0);// 접속중
						writeC(0x38);
						writeC(pc.getType());// 클래스
						i++;
						if (i == 2)
							break;
					}
				}
				writeH(0x00);
				break;
		}
	}

	private static final String 활력_활력버프1 = "08 02 10 " + "f2 "// 버프 종류
			+ "12 18";
	private static final String 활력_활력버프2 = "20 09 28 97 34 30 00 38 00 40 "
			+ "fb 21 "// 버프종류
			+ "48 " + "00 50 00 58 01 60 01 68 e8 21 70 01 45 63";

	private static final String 활력_공격버프1 = "08 02 10 f3 12 18";
	private static final String 활력_공격버프2 = "20 09 28 97 34 30 00 38 00 40 fc 21 48 "
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 6d 23";

	private static final String 활력_방어버프1 = "08 02 10 f4 12 18";
	private static final String 활력_방어버프2 = "20 09 28 97 34 30 00 38 00 40 fd 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_마법버프1 = "08 02 10 f5 12 18";
	private static final String 활력_마법버프2 = "20 09 28 97 34 30 00 38 00 40 fe 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_스턴버프1 = "08 02 10 f6 12 18";
	private static final String 활력_스턴버프2 = "20 09 28 97 34 30 00 38 00 40 ff 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_홀드버프1 = "08 02 10 f7 12 18";
	private static final String 활력_홀드버프2 = "20 09 28 97 34 30 00 38 00 40 80 22 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	public S_ACTION_UI2(String 활력코드, long 시간) {
		writeC(Opcodes.C_EXTENDED_PROTOBUF);
		// writeC(활력버프);
		writeH(ICON_BUFF);

		String 활력버프패킷 = "";
		if (활력코드.equals("활력")) {
			활력버프패킷 = 활력_활력버프1;
		} else if (활력코드.equals("공격")) {
			활력버프패킷 = 활력_공격버프1;
		} else if (활력코드.equals("방어")) {
			활력버프패킷 = 활력_방어버프1;
		} else if (활력코드.equals("마법")) {
			활력버프패킷 = 활력_마법버프1;
		} else if (활력코드.equals("스턴")) {
			활력버프패킷 = 활력_스턴버프1;
		} else if (활력코드.equals("홀드")) {
			활력버프패킷 = 활력_홀드버프1;
		}

		StringTokenizer st = new StringTokenizer(활력버프패킷.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}

		byteWrite(시간 / 1000);

		if (활력코드.equals("활력")) {
			활력버프패킷 = 활력_활력버프2;
		} else if (활력코드.equals("공격")) {
			활력버프패킷 = 활력_공격버프2;
		} else if (활력코드.equals("방어")) {
			활력버프패킷 = 활력_방어버프2;
		} else if (활력코드.equals("마법")) {
			활력버프패킷 = 활력_마법버프2;
		} else if (활력코드.equals("스턴")) {
			활력버프패킷 = 활력_스턴버프2;
		} else if (활력코드.equals("홀드")) {
			활력버프패킷 = 활력_홀드버프2;
		}
		st = new StringTokenizer(활력버프패킷.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85,
			0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90,
			0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b,
			0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6,
			0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1,
			0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc,
			0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0xc6, 0xc7,
			0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2,
			0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd,
			0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8,
			0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3,
			0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe,
			0xff };

	private void byteWrite(long value) {
		long temp = value / 128;
		if (temp > 0) {
			writeC(hextable[(int) value % 128]);
			while (temp >= 128) {
				writeC(hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				writeC((int) temp);
		} else {
			if (value == 0) {
				writeC(0);
			} else {
				writeC(hextable[(int) value]);
				writeC(0);
			}
		}
	}


	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_ACTION_UI2;
	}
}
