package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_NewAddSkill extends ServerBasePacket {

	private static final String S_NewAddSkill = "[S] S_NewAddSkill";
	private byte[] _byte = null;
	public static final int AddskillNew = 1041;

	public S_NewAddSkill(int type, L1PcInstance pc, int skillId) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case AddskillNew: 
			writeC(0x0a);
			if (skillId >= 235 && skillId <= 239) {
				skillId += 4765;
			} else {
				skillId -= 1;
			}

			int length = getBitSize(skillId) + 3;
			writeBit(length);
			writeC(0x08);
			writeBit(skillId);
			writeC(0x10);
			writeC(0x01);
			break;
		}
		writeH(0x00);
	}

	public S_NewAddSkill(int type, L1PcInstance pc, int skillId, boolean on) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case AddskillNew: 
			writeC(0x0a);
			if (skillId >= 235 && skillId <= 239) {
				skillId += 4765;
			} else {
				skillId -= 1;
			}

			int length = getBitSize(skillId) + 3;
			writeBit(length);
			writeC(0x08);
			writeBit(skillId);
			writeC(0x10);
			writeBit(on ? 1 : 0);
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

	public String getType() {
		return S_NewAddSkill;
	}
}