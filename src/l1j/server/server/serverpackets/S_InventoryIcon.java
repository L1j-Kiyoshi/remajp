package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_InventoryIcon extends ServerBasePacket {
	public static final int SHOW_INVEN_BUFFICON = 110;
	
	public S_InventoryIcon(int skillId, boolean on, int msgNum, int time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_INVEN_BUFFICON);
		writeC(0x08);
		//writeC(on ? 2 : 3);//true/false
		writeC(on ? 2 : 3);//true/false
		writeC(0x10);
		writeBit(skillId);
		writeC(0x18);
		writeBit(time);//초
		writeC(0x20);
		writeC(0x08);
		writeC(0x28);
		writeBit(skillId);//원하는 아이콘 모양 인벤번호
		writeH(0x30);
		writeC(0x38);
		writeC(0x03);
		writeC(0x40);
		writeBit(msgNum);//스트링 번호(아이콘 안에 내용)
		writeC(0x48);
		writeC(0x00);
		writeH(0x0050);
		writeC(0x58);
		writeC(0x01);
		writeC(0x60);
		writeC(0x00);
		writeC(0x68);
		writeC(0x00);
		writeC(0x70);
		writeC(0x00);
		writeH(0x00);
	}

	public S_InventoryIcon(int type, int time, int invgfx, int desc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(SHOW_INVEN_BUFFICON);
		writeC(0x08);
		writeC(0x02);
		writeC(0x10);
		writeH(type);
		writeC(0x18);
		writeBit(time);
		writeC(0x28);
		writeBit(invgfx);
		writeC(0x40);
		writeBit(desc);
		writeH(0x00);
	}
	
	public byte[] getContent() {
		return getBytes();
	}
}