package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_bonusstats extends ServerBasePacket {

	private byte[] _byte = null;

	public S_bonusstats(int i, int j) {
		buildPacket(i, j);
	}

	private void buildPacket(int i, int j) {
		writeC(Opcodes.S_ASK);
		writeH(0);
		writeD(0x00000e7c);
		writeH(0x01df);
		writeS(Integer.toString(j));
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
		return "[S] S_bonusstats";
	}
}
