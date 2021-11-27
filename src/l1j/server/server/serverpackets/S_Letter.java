package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Letter extends ServerBasePacket {

	private static final String S_LETTER = "[S] S_Letter";
	private byte[] _byte = null;

	public S_Letter(L1PcInstance pc, int LetterType, int RecieveType, int objid, String subject) {
		buildPacket(pc, LetterType, RecieveType, objid, subject);
	}
	
//	public S_Letter(L1PcInstance pc, int objid, String subject) {
//		buildPacket(pc, objid, subject);
//	}
	
	public S_Letter(String s) {
		buildPacket(s);
	}

//	private void buildPacket(L1PcInstance pc, int type, int iden, int objid,
//			String 제목) {
//		writeC(Opcodes.S_MAIL_INFO);
//		writeC(type); // 0:메일함 1:혈맹메일함 2:보관함
//		writeH(1);
//		writeD(objid);
//		writeC(iden); // 확인여부 0:미확인 1:확인
//		writeD((int) (System.currentTimeMillis() / 1000));
//		writeC(0x00);
//		writeS(pc.getName());
//		writeSS(제목);
//	}
	
	private void buildPacket(L1PcInstance pc, int LetterType, int RecieveType, int objid, String subject) {
		writeC(Opcodes.S_MAIL_INFO);
		/**혈맹편지 일반편지 타입 일반50, 혈맹 51*/
		writeC(LetterType);
		/**혈맹편지 일반편지 타입 일반50, 혈맹 51*/
		/**편지 오브젝트?*/
		writeD(objid);
		/**편지 오브젝트?*/
		/**편지 보관함 타입 1:보낸사람 메일함 0:받는 사람 메일함 및 혈맹메일 2:보관함*/
		writeC(RecieveType); 
		/**편지 보관함 타입 1:메일함 0:혈맹메일함 2:보관함*/
		/**편지 받는사람 이름*/
		writeS(pc.getName());
		/**편지 받는사람 이름*/
		writeSS(subject);
	}
//	
	public void buildPacket(String s) {
		writeC(Opcodes.S_MAIL_INFO);
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
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
		return S_LETTER;
	}
}
