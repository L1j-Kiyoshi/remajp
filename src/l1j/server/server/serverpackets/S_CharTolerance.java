package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharTolerance extends ServerBasePacket {
	private static final String S_CharTolerance = "[S] S_CharTolerance";
	private byte[] _byte = null;

	public static final int TOLERANCE = 0x0a;
	public static final int HIT = 0x12;

	public static final int TECHNIQUE = 1;
	public static final int SPIRIT = 2;
	public static final int DRAGON = 3;
	public static final int FEAR = 4;
	public static final int ALL = 5;

	public static final int CHAR_TOLERANCE_AND_HIT = 1015;
	
	public S_CharTolerance(int type, int code, int sub_code, int value) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case CHAR_TOLERANCE_AND_HIT:
			writeC(code);
			switch (code) {
			case TOLERANCE:
				int tolerance_size = getBitSize(sub_code) + getBitSize(value) + 2;
				writeBit(tolerance_size);
				writeC(0x08);
				writeBit(sub_code);
				writeC(0x10);
				writeBit(value);
				break;
			case HIT:
				int hit_size = getBitSize(sub_code) + getBitSize(value) + 2;
				writeBit(hit_size);
				writeC(0x08);
				writeBit(sub_code);
				writeC(0x10);
				writeBit(value);
				break;
			}
			break;
		}

		writeH(0);
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
		return S_CharTolerance;
	}
}