package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SupportSystem extends ServerBasePacket {
	public static final int SC_START_PLAY_SUPPORT_ACK 			= 2102; 
	public static final int SC_FINISH_PLAY_SUPPORT_ACK			= 2104;
	public static final int SC_FORCE_FINISH_PLAY_SUPPORT_NOTI 	= 2105;
	public static final int SC_PSS_TIME_CHECK_ACK 				= 2107;
	public static int _currentNum = -1 + 1;
	
	public S_SupportSystem(L1PcInstance pc, int Op){
		switch (Op) {
		case SC_START_PLAY_SUPPORT_ACK:
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(SC_START_PLAY_SUPPORT_ACK);
			writeC(8);
			writeC(0);		//0: 시작. 1: 이 곳에서는 사용할 수 없습니다. 2: 사용 가능한 레벨이 아닙니다. 3: 사용 시간이 다 되었습니다.
			break;
		case SC_FINISH_PLAY_SUPPORT_ACK:
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(SC_FINISH_PLAY_SUPPORT_ACK);
			writeC(8);
			writeC(0);		//0: 종료
			break;
		case SC_FORCE_FINISH_PLAY_SUPPORT_NOTI:
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(SC_FORCE_FINISH_PLAY_SUPPORT_NOTI);
			writeC(0x08);
			writeC(0x00);	//0: 시작. 1: 사용 시간이 다 되었습니다. 2: 이 곳에서는 사용할 수 없습니다.
			writeC(0x10);
			writeBit(180000);
			break;
		case SC_PSS_TIME_CHECK_ACK:
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(SC_PSS_TIME_CHECK_ACK);
			writeC(0x08);
			writeBit(1800);
			break;
		case 819:
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(819);
			writeC(0x08);
			writeBit(13);
			writeC(0x10);
			writeBit(0);
			break;
		}
		writeH(0x00);
	}
	
	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
