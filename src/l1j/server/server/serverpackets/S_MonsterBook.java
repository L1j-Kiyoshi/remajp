package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_MonsterBook extends ServerBasePacket{
	private byte[] _byte = null;	
	
	public static final int MB_LOGIN_SYN		= 0x022f;//접속시 불려감
	public static final int MB_LOGIN_LIST		= 0x0230;//접속시 불러감
	public static final int MB_ADD 				= 0x0237;//게임중에 추가시킴
	public static final int MB_TELEPORT_FAIL	= 0x0236;//텔레포트 실패
	public static final int MB_USE 				= 0x0234;
	public static final int MB_NEXT 			= 0x0238;
	
	private void writeSignature(int type){
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
	}
	
	public S_MonsterBook(int type){
		writeSignature(type);
		
		switch(type){
		case MB_LOGIN_SYN:
			loginSyn();
			break;
			
		case MB_TELEPORT_FAIL:
			failTeleport();
			break;	
		}
	}
	
	public S_MonsterBook(int type, int bookId, int section){
		writeSignature(type);
		switch(type){
		case MB_USE:
			use(bookId);
			break;
			
		case MB_NEXT:
			next(bookId, section);
			break;
			
		case MB_ADD:
			ingameAdd(bookId, section);
			break;
		}
	}
	
	private void loginSyn(){
		writeH(0x08);
		writeH(0x10);
		writeH(0x00);
	}
	
	private void ingameAdd(int bookId, int section){
		writeC(0x08);
		write4bit(bookId);
		writeC(0x10);
		write4bit(section);
		writeH(0x00);
	}
	
	private void failTeleport(){
		writeC(0x08);
		writeC(0x02);
		writeC(0x10);
		writeC(0x01);
		writeH(0x00);
	}
	
	private void use(int bookid){
		writeC(0x08);
		writeC(0x00);
		writeC(0x10);
		write4bit(bookid);
		writeH(0x00);
	}
	
	private void next(int bookid, int section){
		writeC(0x08);
		int bookNum = bookid*3;
		if(section == 5)
			bookNum -= 2;
		else if(section == 6)
			bookNum -= 1;
		else if(section != 7)
			bookNum = 0;
		
		write4bit(bookNum);
		writeC(0x10);
		writeC(0x88);
		writeC(0x85);
		writeC(0x91);
		writeC(0xb4);
		writeC(section);
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public int getSize7B(int i){
		return size7B(i);
	}
	
	public void addC(int i){
		writeC(i);
	}
	
	public void addH(int i){
		writeH(i);
	}
	
	public void add4Bit(int i){
		write4bit(i);
	}
}
