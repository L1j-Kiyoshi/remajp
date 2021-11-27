package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_PartyAssist extends ServerBasePacket {

	private static final String S_PartyAssist = "[S] S_PartyAssist";
	private byte[] _byte = null;
	public static final int PartyAssist = 600;


	/** 복수 종합패킷 
	 * @param type */
	/*
	 * public S_PartyAssist(L1Object target) { writeC(Opcodes.S_EXTENDED_PROTOBUF);
	 * writeC(0x58); writeC(0x02); writeC(0x08); writeD(target.getId()); }
	 */
	
	public S_PartyAssist(int type, int Object) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(PartyAssist);
		writeC(0x08);
		writeBit(Object);
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
		return S_PartyAssist;
	}
}