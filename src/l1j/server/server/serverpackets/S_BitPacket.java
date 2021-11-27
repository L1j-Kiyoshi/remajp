package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_BitPacket extends ServerBasePacket {

	private static final String S_BitPacket = "[S] S_BitPacket";
	private byte[] _byte = null;

	public static final int TELEPORT_FAIL = 566;

	public S_BitPacket(int type) {
		buildPacket(type);
	}

	private void buildPacket(int type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case TELEPORT_FAIL:
			writeC(0x08);
			writeC(0x02);
			writeC(0x10);
			writeC(0x01);
			break;
		}
		writeH(0x00);
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
		return S_BitPacket;
	}
}
