package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;

public class S_INN extends ServerBasePacket {
	public S_INN(int objId, int max, String htmlid, String price) {
		writeC(Opcodes.S_HYPERTEXT_INPUT);
		writeD(objId);
		writeD(Integer.parseInt(price)); // ?
		writeD(1); // 스핀 컨트롤의 초기 가격
		writeD(1);
		writeD(max);
		writeH(0); // ?
		writeS(htmlid);
		writeS(htmlid);
		writeH(2);
		writeS("여관주인");
		writeS(price);
		/*
		 * 0000: 86 44 45 00 00 10 04 00 00 01 00 00 00 01 00 00
		 * .DE............. 0010: 00 08 00 00 00 00 00 69 6e 6e 32 00 00 02 00
		 * 24 .......inn2....$ 0020: 39 31 38 00 31 30 34 30 00 918.1040.
		 */
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
