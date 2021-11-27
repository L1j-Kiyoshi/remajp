package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_NewCharSelect extends ServerBasePacket {
	public static final String S_NewCharSelect = "[S] S_NewCharSelect";

	private byte[] _byte = null;

	public S_NewCharSelect() {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(0x3F);
		writeC(0x01);
		writeC(0x00);
		writeC(0x00);
		writeC(0x00);
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
		return S_NewCharSelect;
	}
}
