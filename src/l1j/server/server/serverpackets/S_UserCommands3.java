package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands3 extends ServerBasePacket {

	private static final String S_UserCommands3 = "[C] S_UserCommands3";

	// private static Logger _log =
	// Logger.getLogger(S_UserCommands3.class.getName());

	private byte[] _byte = null;

	public S_UserCommands3(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);// 넘버
		writeS(" 운영자 ");// 글쓴이?
		writeS(" 인형 옵션 설명 ");// 날짜?
		writeS("");// 제목?
		writeS("\n========[인형 정보]==========\n" + "\n"
				+ "[버그베어:무게10% 감소           ]\n" + "[에티:Ac-2 결빙내성+7       ]\n"
				+ "[해츨링:엠틱+4 무게5%      ]\n" + "[라미아:엠틱+4         독공격   ]\n"
				+ "[서큐:발동시엠틱+15 장로:마법추타엠틱:18]\n" + "[늑인,시안:발동시 추타 +15  ]\n"
				+ "[코카트리스:공성+1  활추타+1  ]\n" + "[돌골램:확률적 리덕15       ]\n"
				+ "[시댄서:발동시 피회복 70 증가]\n" + "[스파토이:근거리 추타+2 스턴내성+10]\n"
				+ "[허수아비:피+50 엠+30 공성/활공성+3]\n" + "\n\n"
				+ "=============================");
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_UserCommands3;
	}
}
