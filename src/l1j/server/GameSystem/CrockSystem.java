package l1j.server.GameSystem;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import l1j.server.server.datatables.EvaSystemTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1EvaSystem;
import l1j.server.server.utils.L1SpawnUtil;

public class CrockSystem extends Thread {

	private static CrockSystem _instance;

	public static CrockSystem getInstance() {
		if (_instance == null) {
			_instance = new CrockSystem();
		}
		return _instance;
	}

	L1EvaSystem eva = EvaSystemTable.getInstance().getSystem(1);
	/**
	 * 균열 시간 설정
	 */

	/** 균열이 열렸는지 아닌지 */
	private boolean isOpen = false;

	/** 보스 시간이 시작 되었는지 */
	private boolean isBossTime = false;

	/** 시간의 균열 테베 보스 횟수 */
	private static int dieCount = 0;

	/** 보스방 인원 카운트 (최초 20명만 접 가능 귀환 해도 리셋 안됨) **/
	public int in_count = 0;

	/** 균열 좌표 */
	private static final int[][] loc = { { 32737, 32796, 99 },
			{ 32737, 32796, 99 },{ 32737, 32796, 99 },{ 32737, 32796, 99 },
			{ 32737, 32796, 99 },{ 32737, 32796, 99 },{ 32737, 32796, 99 },
			{ 32737, 32796, 99 },{ 32737, 32796, 99 }};
	// 33258 32742 783
	/** 보스방 선착순 20명을 담기 위한 리스트 */
	private static ArrayList<L1PcInstance> sList = new ArrayList<L1PcInstance>();

	public void reload() {
		EvaSystemTable.getInstance().reload(eva);
	}

	private CrockSystem() {
		super("l1j.server.GameSystem.CrockSystem.CrockSystem");
		if (eva.getOpenContinuation() == 1) {
			isOpen = true; //테베 오픈 여부
			ready();
		}
		start();
	}

	private int ckck = 5;

	public void run() {
		while (true) {
			try {
				if (ckck-- < 1) {
					if (size() > 0) {
						L1PcInstance[] list = toArray균열();
						for (L1PcInstance mem : list) {
							if (mem != null && mem.getNetConnection() != null) {
								if (mem.getMapId() != 782
										&& mem.getMapId() != 784) {
									del(mem);
								}
							}
						}
						list = null;
					}
					ckck = 5;
					// System.out.println(in_count+" >> "+size());
				}

				// System.out.println(in_count+" >> "+size());
				if (System.currentTimeMillis() >= eva.getEvaTime()
						.getTimeInMillis()) {
					openCrock(eva.getEvaTime().getTimeInMillis());
					if (eva.getOpenContinuation() == 0
							&& eva.bossTime < System.currentTimeMillis()) {
						boss();
					}
					if (eva.getOpenContinuation() == 0) {
						bosscheck();
					} else if (eva.getOpenContinuation() == 1) {
						close();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					sleep(1000);
				} catch (Exception e) {
				}
			}
		}
	}

	private static final S_ServerMessage sm1469 = new S_ServerMessage(1469);

	private void openCrock(long time) {
		if (!isOpen()) {
			OpenTime = time;
			// System.out.println("테베오픈");
			setOpen(true);
			ready();
			L1World.getInstance().broadcastPacketToAll(sm1469);// 열렸다~
			in_count = 0;
		}
	}

	private boolean boss_room_in = false;
	private int boss_room_in_check_count = 0;

	private void boss() {
		try {
			if (!isBossTime()) {
				setBossTime(true);
				// System.out.println("보스 타임!!");
			} else {
				if (!boss_room_in) {
					if (boss_room_in_check_count-- <= 0) {
						// System.out.println("보스 타임 방에는 아무도 없음.");
						boss_room_in_check_count = 20;
					}
					if (sList.size() > 0) {
						boss_room_in = true;
						// System.out.println("보스 타임 방에 인원있음 스폰~");
						sleep(2000);
						if (eva.getMoveLocation() == 1) { // 테베
							sendSystemChat("오시리스 : 어리석은 것들..이곳이 어디라고!! 아누비스! 호루스! 저것들을 쓸어버려라!!");
							sleep(3000);
							sendSystemChat("아누비스 : 너희에게 죽음을....");
							sendSystemChat("호루스 : 자비는 없다....");
						} else if (eva.getMoveLocation() == 2) { // 티칼
							sendSystemChat("쿠쿨칸 : 감히 이곳에 들어오다니!! 제브 레퀴!! 깨어나거라!!");
							sleep(3000);
							sendSystemChat("제브 레퀴 : 스으으으으으....스으으으으....");
							sendSystemChat("제브 레퀴 : 휘이이이익....휘이이이익...");
						}
						bossStart();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void bosscheck() {
		try {
			if (isOpen()) {
				if (isBossDie()) {
					// System.out.println("보스다이 시간연장");
					if (eva.getMoveLocation() == 1) { // 테베
						sendSystemChat("테베 오시리스 : 이럴수가..!!! 우리가 졌다.");
						sleep(2000);
						sendSystemChat("테베 오시리스 : 지금 이 시간부터 하루 동안 테베라스를 개방하도록 하겠다.");
					} else if (eva.getMoveLocation() == 2) { // 티칼
						sendSystemChat("쿠쿨칸 : 이럴수가..!!! 우리가 졌다.");
						sleep(2000);
						sendSystemChat("쿠쿨칸 : 지금 이 시간부터 하루 동안 티칼사원을 개방하도록 하겠다.");
					}

					eva.bosscheck += (long) ((long) 60000 * (long) 60 * (long) 19);
					CrockContinuation();
				} else if (eva.bosscheck < System.currentTimeMillis()) {
					if (isBossTime()) {
						if (eva.getMoveLocation() == 1) { // 테베
							sendSystemChat("테베 오시리스 : 너희들은 실패했다!!!");
						} else if (eva.getMoveLocation() == 2) { // 티칼
							sendSystemChat("쿠쿨칸 : 너희들의 무모한 용기와 어리석음을 기억 할지어다!!!");
						}
					}
					// System.out.println("그냥... 클리어");
					setOpen(false);
					setBossTime(false);
					clear();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void close() {
		if (isOpen()) {
			if (System.currentTimeMillis() > eva.bosscheck) {
				// System.out.println("연장시간종료");
				setOpen(false);
				setBossTime(false);
				clear();
			}
		}
	}

	private void ready() {
		if (eva.getMoveLocation() == 0) {
			eva.setOpenLocation((int) (Math.random() * 8));
			eva.setMoveLocation((int) (Math.random() * 2 + 1));
		}
		int OL = eva.getOpenLocation();
		L1SpawnUtil.spawn2(loc[OL][0], loc[OL][1], (short) loc[OL][2], 4500100,
				0, 0, 0);// 위치에 스폰한다
	}

	private void bossStart() {
		// 보스를 스폰하고 보스 타임을 잰다
		// System.out.println("보스 스폰!! "+eva.getMoveLocation());
		switch (eva.getMoveLocation()) {
		case 1:// 테베
			L1SpawnUtil.spawn2(32794, 32825, (short) 782, 400016, 0,
					(3600 * 1000) * 3, 0);
			L1SpawnUtil.spawn2(32794, 32836, (short) 782, 400017, 0,
					(3600 * 1000) * 3, 0);
			break;
		case 2:// 티칼
			L1SpawnUtil.spawn2(32753, 32870, (short) 784, 4036016, 0,
					(3600 * 1000) * 3, 0);
			L1SpawnUtil.spawn2(32751, 32859, (short) 784, 4036017, 0,
					(3600 * 1000) * 3, 0);
			break;
		default:
			break;
		}
	}

	private static final S_ServerMessage sm1468 = new S_ServerMessage(1468);
	private static final S_ServerMessage sm1467 = new S_ServerMessage(1467);

	private void clear() {
		try {
			// 모든 상태를 초기화 한다 그리고 다음 오픈을 준비한다
			eva.setOpenLocation(0);
			eva.setMoveLocation(0);
			eva.setOpenContinuation(0);
			boss_room_in = false;
			// long longtime = OpenTime;
			Calendar cal = (Calendar) Calendar.getInstance().clone();
			cal.setTimeInMillis(eva.getEvaTime().getTimeInMillis()
					+ (long) ((long) 60000 * (long) 60 * (long) 24 * (long) 2));
			eva.setEvaTime(cal);
			eva.bossTime = eva.getEvaTime().getTimeInMillis()
					+ (long) ((long) 60000 * (long) 60 * (long) 2)
					+ ((long) 60000 * (long) 30);
			eva.bosscheck = eva.getEvaTime().getTimeInMillis()
					+ (long) ((long) 60000 * (long) 60 * (long) 4);
			EvaSystemTable.getInstance().updateSystem(eva);

			L1World.getInstance().broadcastPacketToAll(sm1467);// 시간의 균열이 곧
																// 닫힙니다.
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getInventory().checkItem(L1ItemId.TEBEOSIRIS_KEY, 1))
					pc.getInventory().consumeItem(L1ItemId.TEBEOSIRIS_KEY, 1);
			}
			teleportMsg();
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null)
					continue;
				if (pc.getInventory().checkItem(L1ItemId.TEBEOSIRIS_KEY, 1))
					pc.getInventory().consumeItem(L1ItemId.TEBEOSIRIS_KEY, 1);
				if (pc.getMap().getId() >= 780 && pc.getMap().getId() <= 784) {
					L1Teleport.teleport(pc, 33970, 33246, (short) 4, 4, true);
				}
			}
			dieCount = 0;
			if (sList.size() > 0)
				sList.clear();
			L1World.getInstance().broadcastPacketToAll(sm1468);// 시간의 균열이 사라집니다
			crockDelete();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void crockDelete() {
		L1FieldObjectInstance f = null;
		for (L1Object l1object : L1World.getInstance().getObject()) {
			if (l1object != null && l1object instanceof L1FieldObjectInstance) {
				f = (L1FieldObjectInstance) l1object;
				if (f.getNpcTemplate().get_npcId() == 4500100
						&& l1object != null)
					f.deleteMe();
			}
		}
	}

	private void teleportMsg() {
		try {
			sleep(2000);
			sendSystemChat("시스템 메시지 : 30초 후에 텔레포트 합니다.");
			sleep(10000);
			sendSystemChat("시스템 메시지 : 20초 후에 텔레포트 합니다.");
			sleep(10000);
			sendSystemChat("시스템 메시지 : 10초 후에 텔레포트 합니다.");
			sleep(5000);
			sendSystemChat("시스템 메시지 : 5초 후에 텔레포트 합니다.");
			sleep(1000);
			sendSystemChat("시스템 메시지 : 4초 후에 텔레포트 합니다.");
			sleep(1000);
			sendSystemChat("시스템 메시지 : 3초 후에 텔레포트 합니다.");
			sleep(1000);
			sendSystemChat("시스템 메시지 : 2초 후에 텔레포트 합니다.");
			sleep(1000);
			sendSystemChat("시스템 메시지 : 1초 후에 텔레포트 합니다.");
			sleep(1000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 보스가 둘다 잡혀서 선물 주고 연장까지 설정한다
	 */
	private static final S_ServerMessage sm1470 = new S_ServerMessage(1470);

	public void CrockContinuation() {
		try {
			if (eva.getMoveLocation() == 2)
				BossDieBuff();// 버프를 주고
			eva.setOpenContinuation(1);// 연장 상태를 변경
			EvaSystemTable.getInstance().updateExtend(1);
			L1World.getInstance().broadcastPacketToAll(sm1470);// 시간의 균열이 사라집니다
			teleportMsg();

			L1PcInstance[] list5 = toArray균열();
			if (list5.length > 0) {
				for (L1PcInstance pc : list5) {
					if (pc == null)
						continue;
					switch (pc.getMapId()) {
					case 782:
						L1Teleport.teleport(pc, 32628, 32906, (short) 780, 5,
								true);
						break;
					case 784:
						L1Teleport.teleport(pc, 32793, 32754, (short) 783, 2,
								false);
						break;
					default:
						break;
					}
				}
			}
			list5 = null;
			synchronized (sList) {
				if (sList.size() > 0)
					sList.clear();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 시간의 균열 보스공략 확인
	 * 
	 * @return (boolean) 2보스다 죽었다면 ture 1보스 이하 죽였다면 false
	 */
	private boolean isBossDie() {
		boolean sTemp = false;
		switch (dieCount()) {
		case 2:
			sTemp = true;
			break;
		default:
			sTemp = false;
			break;
		}
		return sTemp;
	}

	/**
	 * 시간의 균열 테베 보스 다이 반납
	 * 
	 * @return (int) dieCount 보스 다이 횟수
	 */
	public int dieCount() {
		return dieCount;
	}

	public void dieCount(int dieCount) {
		CrockSystem.dieCount = dieCount;
	}

	/**
	 * 시간의 균열 이동 상태
	 * 
	 * @return (boolean) move 이동 여부
	 */
	public boolean isOpen() {
		return isOpen;
	}

	private void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
	}

	/**
	 * 테베나 티칼이 보스 타임인지 여부
	 * 
	 * @return
	 */
	public boolean isBossTime() {
		return isBossTime;
	}

	private void setBossTime(boolean isBossTime) {
		this.isBossTime = isBossTime;
	}

	public boolean isContinuationTime() {
		if (eva.getOpenContinuation() == 0)
			return false;
		else
			return true;
	}

	/**
	 * 지정된 npcId 에 대한 loc 을 반납
	 * 
	 * @return (int[]) loc 좌표 배열
	 */
	public int[] loc() {
		return loc[eva.getOpenLocation()];
	}

	/**
	 * 선착순 20명 등록
	 */
	public void add(L1PcInstance c) {
		/** 등록되어 있지 않고 */
		synchronized (sList) {
			if (!sList.contains(c)) {
				/** 선착순 20명 이하라면 */
				if (sList.size() < 20)
					sList.add(c);
			}
		}
	}

	public void del(L1PcInstance c) {
		synchronized (sList) {
			/** 등록되어 있지 않고 */
			if (sList.contains(c)) {
				/** 선착순 20명 이하라면 */
				sList.remove(c);
			}
		}
	}

	public void sendSystemChat(String msg) {
		L1PcInstance[] list = toArray균열();
		if (list.length > 0) {
			S_SystemMessage smMsg = new S_SystemMessage(msg);
			for (L1PcInstance pc : toArray균열()) {
				if (pc == null || pc.getNetConnection() == null)
					continue;
				if (pc.getMapId() != 782 && pc.getMapId() != 784)
					del(pc);
				else
					pc.sendPackets(smMsg);
			}
		}
	}

	public static L1PcInstance[] toArray균열() {
		L1PcInstance[] list = null;
		synchronized (sList) {
			list = sList.toArray(new L1PcInstance[sList.size()]);
		}
		return list;
	}

	/**
	 * 선착순 리스트 사이즈 반납
	 * 
	 * @return (int) sList 의 사이즈
	 */
	public int size() {
		synchronized (sList) {
			return sList.size();
		}
	}

	/**
	 * 티칼 보스가 잡혔으니 월드 피씨 전원에게 버프를 준다.
	 */
	public void BossDieBuff() {
		L1PcInstance[] list = toArray균열();
		for (L1PcInstance pc : list) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_TIKAL_BOSSJOIN))
				pc.getSkillEffectTimerSet().removeSkillEffect(
						L1SkillId.STATUS_TIKAL_BOSSJOIN);
		}
		list = null;
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.getNetConnection() == null
					|| pc.isPrivateShop()) {
				continue;
			}
			L1SkillUse su = new L1SkillUse();
			su.handleCommands(pc, L1SkillId.STATUS_TIKAL_BOSSDIE, pc.getId(),
					pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
			su = null;
		}
	}

	/**
	 * 보스가 잡혀서 연장 상태인지 돌려준다
	 * 
	 * @return true : 연장
	 */
	public boolean isCrockIng() {
		if (eva.getOpenContinuation() == 1)
			return true;
		else
			return false;
	}

	/** 시각 데이터 포맷 */
	private static final SimpleDateFormat ss = new SimpleDateFormat(
			"MM-dd HH:mm", Locale.KOREA);
	private Timestamp ts = new Timestamp(System.currentTimeMillis());
	private long OpenTime = 0;// 오픈시간

	public String OpenTime() {
		String resul = "사용 불가능";
		if (OpenTime == 0) {
			return resul;
		} else {
			ts.setTime(OpenTime);
			return ss.format(ts);
		}
	}
}
