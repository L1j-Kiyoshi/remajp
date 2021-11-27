package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Buddy;
import l1j.server.server.model.L1World;

public class S_Buddy extends ServerBasePacket {
	private static final String _S_Buddy = "[S] _S_Buddy";

	private byte[] _byte = null;

	public S_Buddy(int objId, L1Buddy buddy) {
		buildPacket(objId, buddy);
	}

	private void buildPacket(int objId, L1Buddy buddy) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x51);
		writeC(0x01);
		writeC(0x08);
		writeC(0x01);
		for (String buddyname : buddy.getBuddyList()) {
			writeC(0x12);
			writeC(buddyname.getBytes().length + 6);// 이름+6
			writeC(0x0a);
			writeC(buddyname.getBytes().length);// 이름길이
			writeS2(buddyname);
			writeC(0x10);
			if (L1World.getInstance().getPlayer(buddyname) == null) {
				writeC(0x00);// 접속여부
			} else {
				writeC(0x01);// 접속여부
			}
			writeC(0x1a);
			writeC(0x00);
		}
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return _S_Buddy;
	}
}
