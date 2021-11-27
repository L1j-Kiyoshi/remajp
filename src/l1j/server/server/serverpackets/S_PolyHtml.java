package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_PolyHtml extends ServerBasePacket {
	public static final String S_문장주시 = "[S] S_PolyHtml";
	private byte[] _byte = null;

	public S_PolyHtml() {
		buildPacket();
	}

	// 3c 00 00 f1 1f 00 00 b4 00
	private void buildPacket() {
		writeC(Opcodes.S_ASK);
		writeH(0x00);
		writeH(8177);
		writeH(0x00);
		writeH(180);
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
		return S_문장주시;
	}
}
