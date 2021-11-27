package l1j.server.server.serverpackets;

import l1j.server.GameSystem.UserRanking.UserRankingController;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1UserRanking;

public class S_ChatNewPacket extends ServerBasePacket {

	private byte[] bytes = null;

	public S_ChatNewPacket(L1PcInstance pc, int type, int chat_type,
			String chat_text, String target_name) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);

		switch (type) {
		case 3:
			writeC(0x03);
			break;
		case 4:
			writeC(0x04);
			break;
		}

		writeC(0x02);
		writeC(0x08);
		writeC(0x00);
		writeC(0x10);
		writeC(chat_type);

		writeC(0x1a);
		byte[] text_byte = chat_text.getBytes();
		writeC(text_byte.length);
		writeByte(text_byte);

		switch (type) {
		case 3:
			writeC(0x22);

			if (chat_type == 0) {
				writeC(0x00);
				writeC(0x28);
				writeC(0x00);
				writeC(0x30);
				writeC(0x18);
			} else if (chat_type == 1) {
				byte[] name_byte = target_name.getBytes();
				writeC(name_byte.length);
				writeByte(name_byte);
				writeC(0x30);
				writeC(0x00);
			}
			break;
		case 4:
			writeC(0x2a);
			byte[] name_byte = pc.getName().getBytes();
			writeC(name_byte.length);
			writeByte(name_byte);

			if (chat_type == 0) {
				writeC(0x38);
				byteWrite(pc.getId());
				writeC(0x40);
				byteWrite(pc.getX());
				writeC(0x48);
				byteWrite(pc.getY());
			}
			break;
		}

		L1UserRanking rank = UserRankingController.getInstance().getTotalRank(
				pc.getName());

		if (rank != null) {
			int uRank = rank.getCurRank();
			int number = 0;
			if (uRank >= 1 && uRank <= 10) {
				number = 11;
			} else if (uRank >= 11 && uRank <= 20) {
				number = 10;
			} else if (uRank >= 21 && uRank <= 40) {
				number = 9;
			} else if (uRank >= 41 && uRank <= 60) {
				number = 8;
			} else if (uRank >= 61 && uRank <= 80) {
				number = 7;
			} else if (uRank >= 81 && uRank <= 100) {
				number = 6;
			} else if (uRank >= 101 && uRank <= 120) {
				number = 5;
			} else if (uRank >= 121 && uRank <= 140) {
				number = 4;
			} else if (uRank >= 141 && uRank <= 160) {
				number = 3;
			} else if (uRank >= 161 && uRank <= 180) {
				number = 2;
			} else if (uRank >= 181 && uRank <= 200) {
				number = 1;
			}
			writeC(0x50);
			writeC(number);
		}

		writeH(0x00);
	}

	private void byteWrite(int id) {

	}

	@Override
	public byte[] getContent() {
		if (bytes == null) {
			bytes = getBytes();
		}
		return bytes;
	}

}
