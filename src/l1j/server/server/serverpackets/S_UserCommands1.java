package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_UserCommands1 extends ServerBasePacket {

	private static final String S_UserCommands1 = "[C] S_UserCommands1";

	// private static Logger _log =
	// Logger.getLogger(S_UserCommands1.class.getName());

	private byte[] _byte = null;

	public S_UserCommands1(int number) {
		buildPacket(number);
	}

	private void buildPacket(int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);// 넘버
		writeS(" 운영자 ");// 글쓴이?
		writeS(" 무인PC 설명법 ");// 날짜?
		writeS("");// 제목?
		writeS("\n === 무인PC 티켓 설명서 ===\n" + "\n" + " 무인PC티켓을 들고있다면\n"
				+ " 케릭터 종료나 리스후에도\n" + " 서버내에서 케릭터가 종료\n" + " 되지않습니다.\n"
				+ " 쉽게말씀드려서 좀비케릭이\n" + " 되버리며 그상태일때에는\n" + " 깃털이 지급되지않습니다.\n"
				+ " 서버를 위해 종료시에도 \n" + " 무인PC를 만들어서 축서버를\n" + " 만들어나갑시다.\n\n"
				+ " 게임중일때 창고에 꼭 맡기자!\n" + "\n" + " === 무인PC 티켓 설명서 ===");

	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_UserCommands1;
	}
}
