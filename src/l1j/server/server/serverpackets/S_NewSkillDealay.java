package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_NewSkillDealay extends ServerBasePacket {

	private static final String S_NewSkillDealay = "[S] S_NewSkillDealay";
	private byte[] _byte = null;
	public static final int RemasterSkillDealay = 1039;

	public S_NewSkillDealay(L1PcInstance pc, int reason, int type, long time) {
		buildPacket(pc, reason, type, time);
	}
	
	public S_NewSkillDealay() {
		buildPacket();
	}
	/**b4 10 04 08 01 10 00 f4 b7*/
	public void buildPacket() {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeC(0x10);
			writeC(0x04);
			writeC(0x08);
			writeC(0x01);
			writeC(0x10);
			writeC(0x00);
			writeH(0x00);
	}
	
	public void buildPacket(L1PcInstance pc, int reason, int skillid, long time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(reason);
			writeC(0x08);
			writeBit(time);
			writeC(0x10);
			if (pc.getType() == 8) {
				writeC(0x9b);
				writeC(0x08);
			} else {
				writeBit(time);
			}
			writeC(0x18);
			if (skillid == 237){
				writeC(0x01);
			} else if (skillid == 239){
				writeC(0x02);
			} else {
				writeC(0x00);
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
		return S_NewSkillDealay;
	}
}