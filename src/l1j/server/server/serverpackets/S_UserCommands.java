package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands extends ServerBasePacket {

	private static final String S_UserCommands = "[C] S_UserCommands";

	// private static Logger _log =
	// Logger.getLogger(S_UserCommands.class.getName());

	private byte[] _byte = null;

	public S_UserCommands(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);// 넘버
		writeS("");// 글쓴이?
		writeS(" 명령어 설명 ");// 날짜?
		writeS("");// 제목?
		writeS("[====================] [==]\n" + "[    .조사           ] [사]\n"
				+ "[    .버프           ] [용]\n" + "[    .수배           ] [하]\n"
				+ "[    .정보           ] [실]\n" + "[    .드랍         ]  [때]\n"
				+ "[    .몹드랍         ] [쩜]\n" + "[    .무인상점       ] [을]\n"
				+ "[    .텔렉풀기       ] [누]\n" + "[    .혈맹파티       ] [른]\n"
				+ "[    .인형정보       ] [후]\n" + "[    .암호변경       ] []\n"
				+ "[    .퀴즈설정       ] \n" + "[    .퀴즈인증       ] [입]\n"
				+ "[    .오토멘트       ] [력]\n" + "[    .홍보인증&확인 ] [하]\n"
				+ "[    .홍보정산       ] [세]\n" + "[    .봉인해제신청   ] [요]\n"
				+ "[    .나이          ] \n" + "[====================] [==] ");

	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_UserCommands;
	}
}
