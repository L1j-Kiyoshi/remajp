package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Random;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.RaceRecordTable;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1LittleBugInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1RaceTicket;
import l1j.server.server.utils.SQLUtil;

public class L1BugBearRace {

	public final ArrayList<L1LittleBugInstance> _littleBug = new ArrayList<L1LittleBugInstance>();

	private final ArrayList<L1NpcInstance> _merchant = new ArrayList<L1NpcInstance>();

	private final ArrayList<L1RaceTicket> _ticketPrice = new ArrayList<L1RaceTicket>();
	public static final int STATUS_NONE = 0;
	public static final int STATUS_READY = 1;
	public static final int STATUS_PLAYING = 2;
	public boolean buyTickets = true;
	public static boolean racing_im = false; // 레이스가 중간이 넘었는지

	private static final int[] startX = { 33522, 33520, 33518, 33516, 33514 };
	private static final int[] startY = { 32861, 32863, 32865, 32867, 32869 };

	private static final int[][] movingCount = { { 45, 4, 5, 6, 50 }, { 42, 6, 5, 7, 50 }, { 39, 8, 5, 8, 50 }, { 36, 10, 5, 9, 50 }, { 33, 12, 5, 10, 50 } };

	public static int[] bugStat = new int[5];

	private static final int[] heading = { 6, 7, 0, 1, 2 };

	private static final Random _random = new Random();

	private int[] _betting = new int[5];

	private int _round;

	private int _roundId;

	private int _bugRaceStatus;

	public boolean _goal;

	private static L1BugBearRace _instance;

	private static int racetime = 3;

	public static L1BugBearRace getInstance() {
		if (_instance == null) {
			_instance = new L1BugBearRace();
		}
		return _instance;
	}

	private L1BugBearRace() {
		for (L1Object obj : L1World.getInstance().getObject()) {
			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (npc.getNpcTemplate().get_npcId() == 70041 || npc.getNpcTemplate().get_npcId() == 70035 || npc.getNpcTemplate().get_npcId() == 70042) {
					_merchant.add(npc);
				}
			}
		}
		race_loading();
		loadingGame();
	}

	public void loadingGame() {
		try {
			clearBug();
			setRoundId(ObjectIdFactory.getInstance().nextId());
			setRound(getRound() + 1);
			clearBetting();
			_goal = false;

			GeneralThreadPool.getInstance().schedule(new Startbug(), 10000);
			/*
			 * storeBug(); closeDoor(); setBugRaceStatus(STATUS_READY);
			 * //broadCastTime("$376 " + racetime + " $377"); broadCastTime("경기 시작 2 분 전!");
			 * L1ReadyThread rt = new L1ReadyThread();
			 * GeneralThreadPool.getInstance().execute(rt);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void storeBug() {
		int arr[] = new int[5];
		for (int i = 0; i < 5; i++) {
			arr[i] = _random.nextInt(20);
			for (int j = 0; j < i; j++) {
				if (arr[i] == arr[j]) {
					arr[i] = _random.nextInt(20);
					i = i - 1;
					break;
				}
			}
		}

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(100000);
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(1);
		nf.setMinimumFractionDigits(1);
		for (int i = 0; i < 5; i++) {
			L1LittleBugInstance bug = new L1LittleBugInstance(npcTemp, arr[i], startX[i], startY[i]);
			RaceRecordTable.getInstance().getRaceRecord(arr[i], bug);
			float winpoint = 0;
			float record = bug.getWin() + bug.getLose();
			if (record == 0) {
				winpoint = 0;
			} else {
				winpoint = bug.getWin() / record * 100;
			}
			bug.setWinPoint(nf.format(winpoint));
			_littleBug.add(bug);
		}
	}

	private void setSpeed() {
		for (L1LittleBugInstance bug : _littleBug) {
			int pulsSpeed = 0;
			int condition = bug.getCondition();
			if (condition == L1LittleBugInstance.GOOD) {
				pulsSpeed = 30;
			} else if (condition == L1LittleBugInstance.NORMAL) {
				pulsSpeed = 60;
			} else if (condition == L1LittleBugInstance.BAD) {
				pulsSpeed = 90;
			}
			int time = (int) (bug.getPassispeed() - (bug.getPassispeed() * 0.40));// 기존
																					// 버경
			// int time = (int) (bug.getPassispeed() - (bug.getPassispeed() *
			// 0.20));//기존 인경
			// bug.setPassispeed(bug.getPassispeed() +
			// _random.nextInt(pulsSpeed));
			bug.setPassispeed(time + _random.nextInt(pulsSpeed));
			// System.out.println(bug.getName() + " > "+bug.getPassispeed());
		}
	}

	public void startRace() {
		setBugRaceStatus(STATUS_PLAYING);
		buyTickets = false;
		calcDividend();
		setSpeed();
		openDoor();
		int i = 0;
		for (L1LittleBugInstance bug : _littleBug) {
			L1BugBearRacing bbr = new L1BugBearRacing(bug, i++);
			GeneralThreadPool.getInstance().execute(bbr);
		}
	}

	private void clearBug() {
		try {
			ArrayList<L1LittleBugInstance> clonn = new ArrayList<L1LittleBugInstance>();
			clonn.addAll(_littleBug);
			GeneralThreadPool.getInstance().schedule(new deleteBug(clonn), 10000);
			/*
			 * for (L1LittleBugInstance bug : _littleBug) { bug.deleteMe(); }
			 */
			_littleBug.clear();
			for (int i = 0; i < 5; i++) {
				bugStat[i] = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class deleteBug implements Runnable {
		ArrayList<L1LittleBugInstance> clonn;

		public deleteBug(ArrayList<L1LittleBugInstance> ll) {
			clonn = ll;
		}

		@Override
		public void run() {
			for (L1LittleBugInstance bug : clonn) {
				bug.deleteMe();
			}
		}

	}

	class Startbug implements Runnable {
		public Startbug() {
		}

		@Override
		public void run() {
			try {
				L1ReadyThread rt = new L1ReadyThread();
				GeneralThreadPool.getInstance().execute(rt);

				storeBug();
				closeDoor();
				setBugRaceStatus(STATUS_READY);
				// broadCastTime("$376 " + racetime + " $377");
				broadCastTime("경기 시작 3 분전");

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void openDoor() {
		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (door.getGfxId().getGfxId() == 1487) {
				door.open();
			}
		}
	}

	private void closeDoor() {
		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (door.getGfxId().getGfxId() == 1487) {
				door.close();
			}
		}
	}

	private void broadCastTime(String chat) {
		for (L1NpcInstance npc : _merchant) {
			Broadcaster.wideBroadcastPacket(npc, new S_NpcChatPacket(npc, chat, 2), true);
		}
	}

	private void broadCastWinner(String winner) {
		String chat = "제 " + getRound() + "$366" + " '" + winner + "' " + "$367";
		for (L1NpcInstance npc : _merchant) {
			if (npc.getNpcTemplate().get_npcId() == 70035) { // 세실
				Broadcaster.wideBroadcastPacket(npc, new S_NpcChatPacket(npc, chat, 2), true);
			}
		}
	}

	private void calcDividend() {
		float[] dividend = new float[5];
		L1LittleBugInstance[] bugs = getBugsArray();
		float allBetting = 0;
		for (int b : _betting) {
			allBetting += b;
		}

		long AllbettingPrice = (long) (allBetting * 500);
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.getNetConnection() == null || !pc.isGm())
				continue;
			pc.sendPackets(new S_SystemMessage("제 " + getRound() + " 버경 티켓 구입 총 금액: " + AllbettingPrice), true);
		}

		for (int i = 0; i < 5; i++) {
			if (_betting[i] == 0) {
				dividend[i] = 0;
			} else {
				// dividend[i] = (float)((allBetting / _betting[i]) * 0.8f);
				// dividend[i] = (float)((allBetting / _betting[i]) * 0.89f);
				dividend[i] = (float) ((allBetting / _betting[i]) * (float) ((float) GMCommands.환수율 * 0.01f));
				if (dividend[i] < 1) {
					dividend[i] = 1f;
				}

				if (dividend[i] > 10000) {
					dividend[i] = 10000;
				}
			}
			bugs[i].setDividend(dividend[i]);
		}
	}

	public int bugRound() {
		return getRound();
	}

	public String getTicketName(int i) {
		L1LittleBugInstance bug = _littleBug.get(i);
		return new StringBuilder().append(getRound()).append("-").append(bug.getNumber() + 1).append(" ").append(bug.getName()).toString();
	}

	public int[] getTicketInfo(int i) {
		return new int[] { getRoundId(), getRound(), _littleBug.get(i).getNumber() };
	}

	public String getTicketBugName(int i) {
		for (int ai = 0; ai < 5; ai++) {
			L1LittleBugInstance bug = _littleBug.get(ai);
			if (bug.getNumber() == i)
				return bug.getName();
		}
		return null;
	}

	public double getTicketPrice(L1ItemInstance item) {
		for (L1RaceTicket ticket : _ticketPrice) {
			if (ticket.getRoundId() == item.getSecondId() && ticket.getWinner() == item.getTicketId()) {
				return ticket.getDividend();
			}
		}
		/*
		 * L1RaceTicket ticket = _ticketPrice.get(item.getSecondId()); if (ticket ==
		 * null && item.getSecondId() != getRoundId()) { return 1; } if
		 * (ticket.getWinner() == item.getTicketId()) { return ticket.getDividend(); }
		 */
		return 0;
	}

	public String[] makeStatusString() {
		ArrayList<String> status = new ArrayList<String>();
		for (L1LittleBugInstance bug : _littleBug) {
			status.add(bug.getName());
			if (bug.getCondition() == L1LittleBugInstance.GOOD) {
				status.add("좋음");// $368
			} else if (bug.getCondition() == L1LittleBugInstance.NORMAL) {
				status.add("보통");// $369
			} else if (bug.getCondition() == L1LittleBugInstance.BAD) {
				status.add("나쁨");// $370
			}
			status.add(bug.getWinPoint() + "%");
		}
		return status.toArray(new String[status.size()]);
	}

	private synchronized void finish(L1LittleBugInstance bug) {
		try {
			if (!_goal && getBugRaceStatus() == STATUS_PLAYING) {
				_goal = true;
				racing_im = false;
				byte i = 0;
				for (L1LittleBugInstance b : _littleBug) {
					if (b == bug)
						break;
					i++;
				}
				int allBetting = 0;
				for (int b : _betting) {
					allBetting += b;
				}
				allBetting = allBetting * 500;
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc == null || pc.getNetConnection() == null || !pc.isGm())
						continue;
					pc.sendPackets(new S_SystemMessage("제 " + getRound() + " 버경 우승"), true);
					pc.sendPackets(new S_SystemMessage(" 총티켓: " + (long) allBetting + " 우승티켓: " + (long) (_betting[i] * bug.getDividend() * 500) + " 차액: "
							+ ((long) allBetting - (long) (_betting[i] * bug.getDividend() * 500))), true);
				}

				race_difference((int) allBetting, (int) (_betting[i] * bug.getDividend() * 500));
				L1RaceTicket ticket = new L1RaceTicket(getRoundId(), bug.getNumber(), bug.getDividend());
				_ticketPrice.add(ticket);
				race_divAdd(getRoundId(), bug.getNumber(), bug.getDividend());
				broadCastWinner(bug.getNameId());
				setBugRaceStatus(STATUS_NONE);
				RaceRecordTable.getInstance().updateRaceRecord(bug.getNumber(), bug.getWin() + 1, bug.getLose());
				// BugRaceFinishRecord.getInstance().addRecord(getRoundId(),
				// bug.getNumber(), bug.getDividend());
				L1WaitingTimer wt = new L1WaitingTimer();
				wt.begin();
			} else {
				RaceRecordTable.getInstance().updateRaceRecord(bug.getNumber(), bug.getWin(), bug.getLose() + 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private L1LittleBugInstance[] getBugsArray() {
		return _littleBug.toArray(new L1LittleBugInstance[_littleBug.size()]);
	}

	public synchronized void addBetting(int num, int count) {
		if (getBugRaceStatus() == STATUS_READY) {
			_betting[num] += count;
		}
	}

	// public synchronized void addBetting(int num, int count) {
	// if (getBugRaceStatus() == STATUS_READY) {
	// _betting[num] += count;
	// }
	// }

	private void clearBetting() {
		_betting = new int[5];
	}

	public void setRound(int i) {
		_round = i;
	}

	public int getRound() {
		return _round;
	}

	public void setRoundId(int i) {
		_roundId = i;
	}

	public int getRoundId() {
		return _roundId;
	}

	private void setBugRaceStatus(int i) {
		_bugRaceStatus = i;
	}

	public int getBugRaceStatus() {
		return _bugRaceStatus;
	}

	class L1WaitingTimer implements Runnable {// TimerTask {
		@Override
		public void run() {
			loadingGame();
		}

		public void begin() {
			GeneralThreadPool.getInstance().schedule(this, 5 * 1000);
		}
	}

	class L1ReadyThread implements Runnable {
		@Override
		public void run() {
			try {
				buyTickets = true;
				broadCastTime("레이스 표 판매가 시작되었습니다.");
				for (int time = racetime; time > 0; time--) {
					if (time <= 2) {
						broadCastTime("$376 " + time + " $377");
					}
					try {
						/*
						 * if(time == 3) Thread.sleep(60000); else
						 */
						Thread.sleep(60000);
					} catch (Exception e) {
					}
				}
				buyTickets = false;
				broadCastTime("레이스표 판매가 종료되었습니다.");
				L1BroadCastDividend bcd = new L1BroadCastDividend();
				GeneralThreadPool.getInstance().execute(bcd);
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}

	class L1BroadCastDividend implements Runnable {
		private L1NpcInstance _npc;

		public L1BroadCastDividend() {
			for (L1NpcInstance npc : _merchant) {
				if (npc.getNpcTemplate().get_npcId() == 70041) { // 퍼킨
					_npc = npc;
				}
			}
		}

		@Override
		public void run() {
			try {

				if (_npc != null)
					Broadcaster.wideBroadcastPacket(_npc, new S_NpcChatPacket(_npc, "$363", 2), true);

				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				if (_npc != null)
					Broadcaster.wideBroadcastPacket(_npc, new S_NpcChatPacket(_npc, "$364", 2), true);
				startRace();
				try {
					Thread.sleep(2000);
				} catch (Exception e) {
				}
				NumberFormat nf = NumberFormat.getInstance();
				nf.setMaximumFractionDigits(1);
				nf.setMinimumFractionDigits(1);
				for (L1LittleBugInstance bug : _littleBug) {
					String chat = bug.getName() + " $402 " + nf.format(bug.getDividend());
					if (_npc != null)
						Broadcaster.wideBroadcastPacket(_npc, new S_NpcChatPacket(_npc, chat, 2), true);
					try {
						Thread.sleep(1000);
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class L1BugBearRacing implements Runnable {
		private L1LittleBugInstance _bug;
		private int _num;
		private int count = 0;
		private int i = 0;

		public L1BugBearRacing(L1LittleBugInstance bug, int num) {
			_bug = bug;
			_num = num;
			count = movingCount[_num][0];
		}

		@Override
		public void run() {
			try {

				if (_bug._destroyed)
					return;
				if (_bug.getX() == 33527) {
					finish(_bug);
					return;
				}
				if (count == 0) {
					count = movingCount[_num][++i];
				}

				// 버경 로봇 되돌아가는 시점
				if (!_goal && !racing_im && i == 4 && _bugRaceStatus == STATUS_PLAYING)
					racing_im = true;

				if (_random.nextInt(150) == 0) {
					Broadcaster.broadcastPacket(_bug, new S_DoActionGFX(_bug.getId(), 30), true); // 버경
					// Broadcaster.broadcastPacket(_bug, new
					// S_DoActionGFX(_bug.getId(), 66 + _random.nextInt(2)),
					// true); 인경
					GeneralThreadPool.getInstance().schedule(this, 3360);
				} else {
					count--;
					_bug.setDirectionMove(heading[i]);
					GeneralThreadPool.getInstance().schedule(this, _bug.getPassispeed());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * class L1BugBearRacing implements Runnable { private L1LittleBugInstance _bug;
	 * private int _num;
	 * 
	 * public L1BugBearRacing(L1LittleBugInstance bug, int num) { _bug = bug; _num =
	 * num; }
	 * 
	 * @Override public void run() { int i = 0; int count = movingCount[_num][i];
	 * while (true) { if (count == 0) { count = movingCount[_num][++i]; } if
	 * (_random.nextInt(150) == 0) { Broadcaster.broadcastPacket(_bug, new
	 * S_DoActionGFX(_bug.getId(), 30)); try { Thread.sleep(3360); } catch(Exception
	 * e) { } } else { count--; _bug.setDirectionMove(heading[i]); try {
	 * 
	 * Thread.sleep(_bug.getPassispeed()); } catch(Exception e) { } } if
	 * (_bug.getX() == 33527) { finish(_bug); break; } } } }
	 */

	private void race_difference(int b, int s) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO race_difference SET date=?, Round=?, buy=?, winner_sell=?, difference=?");
			pstm.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
			pstm.setInt(2, getRound());
			pstm.setInt(3, b);
			pstm.setInt(4, s);
			pstm.setInt(5, b - s);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void race_divAdd(int id, int b, double d) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO race_div_record SET id=?, bug_number=?, dividend=?");
			pstm.setInt(1, id);
			pstm.setInt(2, b);
			pstm.setInt(3, (int) (d * 1000));
			pstm.executeUpdate();
		} catch (SQLException e) {
			// e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void race_loading() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM race_div_record");
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1RaceTicket ticket = new L1RaceTicket(rs.getInt("id"), rs.getInt("bug_number"), (double) (rs.getInt("dividend")) / 1000);
				_ticketPrice.add(ticket);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}