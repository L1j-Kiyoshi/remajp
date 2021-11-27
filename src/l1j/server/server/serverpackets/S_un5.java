package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_un5 extends ServerBasePacket {
	public static final String S_LOGIN_RESULT = "[S] S_LoginResult";
	private byte[] _byte = null;

	public S_un5() {
		buildPacket();
	}

	private void buildPacket() {
		writeC(Opcodes.S_EVENT);
		writeC(0xa0);
		writeC(0x01);
		// [OPCODE = 178]
		// 0000: b2 a0 01 ...

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
		return S_LOGIN_RESULT;
	}
}
