package l1j.server.server;

import java.util.Calendar;
import java.util.Locale;
import java.util.Random;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.GameSystem.RotationNotice.RotationNoticeTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;

public class Jibaetower extends Thread {

	private static Jibaetower _instance;

	private boolean _JBStart;
	private static Random _random = new Random(System.nanoTime());

	public boolean getOBStart() {
		return _JBStart;
	}

	public void setOBStart(boolean OB) {
		_JBStart = OB;
	}
	
	public boolean isGmOpen = false;

	public boolean isGmClose = false;

	public static Jibaetower getInstance() {
		if (_instance == null) {
			_instance = new Jibaetower();
		}
		return _instance;
	}

	@Override
	public void run() {
		while (true) {
			try {
//				if (isOpen() || isGmOpen) {
//					isGmOpen = false;
//					Spawn1();
//					Thread.sleep(120000);
//				}
//				if (isClose() || isGmClose) {
//					isGmClose = false;
//					End();
//					Thread.sleep(120000);
//				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000L);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void Spawn1() {
		try {
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\지배의 탑 개방."));
			L1World.getInstance().broadcastServerMessage("\\aH지배의 탑 : 개방 시간 동안 사냥이 가능합니다.");
			L1World.getInstance().broadcastServerMessage("\\aH지배의 탑 개방 시간 : 오전 오후 8시에만 열립니다.");
			/** 로테이션 컨트롤러 현재 시간부터 언제까지 가능한지 체크 문 */
			long Time = System.currentTimeMillis();
			/** 1시간 15분 동안 유지 */
			long EndTime = System.currentTimeMillis() + (1000 * 3600 * 5);
			RotationNoticeTable.getInstance().UpdateNotice(16, new Timestamp(Time), new Timestamp(EndTime));
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc instanceof L1RobotInstance)
					continue;
				pc.알림서비스(pc, true);
			}
			setOBStart(true);
		} catch (Exception e2) {
			e2.printStackTrace();
		}
	}

	
	private boolean isOpen() {
		Calendar calender = Calendar.getInstance();
		int hour, minute;
		hour = calender.get(Calendar.HOUR_OF_DAY);
		minute = calender.get(Calendar.MINUTE);
		if ((hour == 7 && minute == 59) || (hour == 19 && minute == 59)) {
			return true;
		}
		return false;
	}

	private boolean isClose() {
		Calendar calender = Calendar.getInstance();
		int hour, minute;
		hour = calender.get(Calendar.HOUR_OF_DAY);
		minute = calender.get(Calendar.MINUTE);
		if ((hour == 13 && minute == 15) || (hour == 1 && minute == 15)) {
			return true;
		}
		return false;
	}

	/** 종료 **/
	public void End() {
		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aH지배의 탑 : 지배의 탑이 닫혔습니다 ."));
		L1World.getInstance().broadcastServerMessage("\\aH지배의 탑 : 지배의 탑이 닫혔습니다.");
		setOBStart(false);
	}
}