package l1j.server.server.serverpackets;

import java.util.ArrayList;

import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Clan;

public class S_ClanBlessHuntUi extends ServerBasePacket {
	private byte[] _byte = null;
	private static final String S_ClanBlessHuntUi = "S_ClanBlessHuntUi";

	public static final int CLAN_BLESS_HUNT_TELEPORT = 1019;

	public S_ClanBlessHuntUi(int type, L1Clan clan) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case CLAN_BLESS_HUNT_TELEPORT:
			ArrayList<Integer> maps = clan.getBlessHuntMaps();
			ArrayList<Integer> button_type = clan.getBlessHuntMapsType();
			for (int i = 0; i < maps.size(); i++) {
				int size = getBitSize(maps.get(i)) + 3;
				writeC(0x0a);
				writeBit(size);
				writeC(0x08);
				writeBit(maps.get(i));
				writeC(0x10);
				writeBit(button_type.get(i)); //1:선택 , 2:이동, 3:변경		
			}
			writeC(0x10);
			writeBit(0x01); // 전체변경가능여부 확인할것
			break;
		}
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_ClanBlessHuntUi;
	}

}