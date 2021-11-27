package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_CharSpeedUpdate extends ServerBasePacket {
	private static final String S_CharSpeedUpdate = "[S] S_CharSpeedUpdate";

	public S_CharSpeedUpdate(int objid, int speed, int level) {
		  writeC(Opcodes.S_CHANGE_LEVEL);//S_CHANGE_LEVEL
		    writeD(objid);
		    writeC(speed);
		    writeC(level);
		    writeC(0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
	@Override
	public String getType() {
		return S_CharSpeedUpdate;
	}
}
