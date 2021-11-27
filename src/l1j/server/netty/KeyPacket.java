package l1j.server.netty;

import l1j.server.server.Opcodes;
import l1j.server.server.serverpackets.ServerBasePacket;

public class KeyPacket extends ServerBasePacket {
	private byte[] _byte = null;
	public KeyPacket() {
		writeByte(Opcodes.S_KEYBYTES);
	}


	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}