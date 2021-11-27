package l1j.server.server.serverpackets;

import l1j.server.Config;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_CharMapTime extends ServerBasePacket {

	private static final String S_MAPTIMER = "[S] S_MapTimer";
	private byte[] _byte = null;
	private static final int RESTART_MAPUI = 803;
	private static int[] 고정값 = new int[] { 1, 2, 6, 7, 15, 17, 18, 23, 99, 100 };
	private static int[] totaltime = new int[] { Config.기란감옥던전시간,
			Config.상아탑발록진영시간, 7200, 10800,
			7200, 7200, Config.PC정무시간, Config.PC정무시간, Config.PC정무시간, 7200, Config.PC정무시간 };

	public S_CharMapTime(L1PcInstance pc) {
		int[] usetime = new int[] { Config.기란감옥던전시간 - pc.getgirantime(),
				Config.상아탑발록진영시간 - pc.getivorytime(),
				7200 - pc.get버땅time(), 10800 - pc.get아투바time(),
				7200 - pc.get에바time(), //더미
				7200- pc.get고무time(),
				Config.PC정무시간 - pc.get고무time() //미소피아의 숨겨진마을
				,Config.PC정무시간 - pc.get고무time() //미소피아의 숨겨진마을pc
				,Config.PC정무시간 - pc.get고무time() //불타는여정 이벤트맵
				,7200 - pc.get검은전함time()
				,Config.PC정무시간 - pc.get고무time()};//말섬던전
		String[] name = new String[] { "$12125", "$6081", "$30247", "$14667",
			"$30983", "$19375", "$30733", "$30734", "$28760", "$27281", "$23478" };
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(RESTART_MAPUI);
		for (int i = 0; i < 고정값.length; i++) {
			writeC(0x0a);
			int size = 4 + writeLenght(고정값[i])
					+ writeLenght(name[i].getBytes().length)
					+ name[i].getBytes().length + writeLenght(usetime[i])
					+ writeLenght(totaltime[i]);
			writeBit(size);
			writeC(0x08);
			writeBit(고정값[i]);
			writeC(0x12);
			writeC(name[i].getBytes().length);
			writeByte(name[i].getBytes());
			writeC(0x18); // 남은이용시간
			writeBit(usetime[i]);
			writeC(0x20);
			writeBit(totaltime[i]);

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

	@Override
	public String getType() {
		return S_MAPTIMER;
	}
}