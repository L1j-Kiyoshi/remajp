package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_RenewLetter extends ServerBasePacket {
	private static final String S_RENEWLETTER = "[S] S_RenewLetter";
	private byte[] _byte = null;

	public S_RenewLetter(L1PcInstance pc, int type, int id) {

		buildPacket(pc, type, id);
	}
	
	public S_RenewLetter(L1PcInstance pc) {

		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc, int type, int id) {
		writeC(Opcodes.S_MAIL_INFO);
		writeC(type); // 30:받은편지 보낸편지 삭제시 재갱신 31:혈맹편지함에서 편지 삭제시   40:받은편지 보낸편지 보관함 이동시    32:보관편지함 삭제시
		writeD(id); // 게시물 넘버
		writeC(0x01);
	}
	
	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_MAIL_INFO);
		writeC(0x03);
		writeH(0x00); 
	}
	//c3 03 00 00 
	

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_RENEWLETTER;
	}
}