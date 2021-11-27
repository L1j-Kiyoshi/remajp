package l1j.server.GameSystem;
import java.util.Date;
import java.util.Random;

import l1j.server.Config;
import l1j.server.MJBookQuestSystem.Loader.UserWeekQuestLoader;
import l1j.server.MJBookQuestSystem.Templates.WeekQuestDateCalculator;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.model.L1MobGroupSpawn;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.utils.L1SpawnUtil;

public class BossTimer implements Runnable {
	private static BossTimer _instance;

	public static BossTimer getInstance() {
		if (_instance == null) {
			_instance = new BossTimer();
		}
		return _instance;
	}

	public boolean 젠사용 = false;

	public boolean 공지사용 = false;

	private Date day = new Date(System.currentTimeMillis());

	public BossTimer() {
		// super("l1j.server.GameSystem.BossTimer");
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run() {
		try {
			// while (true) {
			day.setTime(System.currentTimeMillis());
			boss();
			fairlyQueen();
			MerchantOneDayBuyReset();
			// Thread.sleep(1000L);
			// }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	@SuppressWarnings("deprecation")
	private void MerchantOneDayBuyReset() {
		if (day.getMinutes() == 0 && day.getSeconds() == 0 && day.getHours() == 0) {
			L1MerchantInstance.resetOneDayBuy();
			C_ItemUSe.reset시공의항아리횟수();
			C_ItemUSe.reset마빈주머니_계정횟수();
			// C_Shop.reset상점개설계정횟수();
		}
	}

	private boolean QueenAMspawn = false;
	private boolean QueenPMspawn = false;

	private void fairlyQueen() {
		long time = GameTimeClock.getInstance().getGameTime().getSeconds() % 86400;
		if ((time > 60 * 60 * 9 && time < 60 * 60 * 10) || (time < -60 * 60 * 9 && time > -60 * 60 * 10)) {
			if (!QueenAMspawn) {
				QueenAMspawn = true;
				// 9~12
				fairlyQueenSpawn();
			}
		} else {
			QueenAMspawn = false;
		}

		if ((time > 60 * 60 * 19 && time < 60 * 60 * 20) || (time < -60 * 60 * 19 && time > -60 * 60 * 20)) {
			if (!QueenPMspawn) {
				QueenPMspawn = true;
				// 19~24
				fairlyQueenSpawn();
			}
		} else {
			QueenPMspawn = false;
		}
	}

	private void fairlyQueenSpawn() {
		Random _rnd = new Random(System.nanoTime());
		int delay = _rnd.nextInt(600000 * 3) + 1;
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				Random _rnd = new Random(System.nanoTime());
				int deletetime = (_rnd.nextInt(11) + 10) * 60000;
				L1NpcInstance n = L1SpawnUtil.spawn2(33164, 32284, (short) 4, 70852, 0, deletetime, 0);
				L1MobGroupSpawn.getInstance().doSpawn(n, 107, true, false);
				for (L1NpcInstance npc : n.getMobGroupInfo().getMember()) {
					L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, deletetime);
					timer.begin();
				}
			}

		}, delay);
	}

	private int _4시간주기 = (60000 * 60 * 3) + (60000 * 40);
	Random _random = new Random(System.nanoTime());


	@SuppressWarnings("deprecation")
	public void boss() {
		try {
			if (젠사용 == true) {
				return;
			}
			if (day.getSeconds() == 0 && day.getMinutes() == 00) {
				int rh = day.getHours();
				젠사용 = true;
				BossTimerCheck check = new BossTimerCheck(this);
				check.begin();

				if (rh == Config.출석초기화시간) {
					L1SpawnUtil.spawn6(33174, 33001, (short) 4, 203048, 0, _4시간주기, 0);// 어둠의 군주
					WeekQuestDateCalculator.getInstance().reloadTime();
					L1World.getInstance().broadcastServerMessage("\\aD출석 리스트 및 주간퀘스트 재 갱신(매일 오전 9시)");
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						UserWeekQuestLoader.load(pc);
					}
				}
			}   
		} catch (Exception e) {
			e.printStackTrace();
		}
		/**
		 * ==================== 1 시 간 젠 보 스 들 ===================================
		 * 크로커다일.드레이크선장.맘보토끼. 이프리트 드레이크 맘보토끼 흑장로 도펠갱어 그레이트 미노타우르스,커츠,에이션트
		 * 자이언트,피닉스,대왕오징어,우두머리 반어인,
		 * 
		 * 오염된 오크투사,스피리드,쿠만.카스파 패밀리.유니콘,몽성대정령,저주받은 물의 대정령 ,저주받은 무녀 사엘,물의 정령,심연의 주인,마수 군왕
		 * 바란카,카푸,자이언트 웜,쿠만 네크로맨서 ======================================
		 * =================================
		 **/
	}

	public class EventNoticeTimer implements Runnable {
		private BossTimer 공지체크 = null;

		public EventNoticeTimer(BossTimer bt) {
			공지체크 = bt;
		}

		@Override
		public void run() {
			try {
				공지체크.공지사용 = false;
				공지체크 = null;
				// this.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void begin() {
			// Timer timer = new Timer();
			// timer.schedule(this, 3000);
			GeneralThreadPool.getInstance().schedule(this, 3000);
		}
	}

	public class BossTimerCheck implements Runnable {
		private BossTimer 젠체크 = null;

		public BossTimerCheck(BossTimer bt) {
			젠체크 = bt;
		}

		@Override
		public void run() {
			try {
				젠체크.젠사용 = false;
				젠체크 = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void begin() {
			// Timer timer = new Timer();
			// timer.schedule(this, 3000);
			GeneralThreadPool.getInstance().schedule(this, 3000);
		}
	}
}