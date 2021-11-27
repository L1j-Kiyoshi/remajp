package l1j.server.GameSystem.Boss;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.GameSystem.Boss.NewBossSpawnTable.BossTemp;
import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.GameSystem.RotationNotice.RotationNoticeTable;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.IdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1MobGroupSpawn;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1Npc;
import manager.LinAllManager;

public class BossTimeController implements Runnable {
	private static Logger _log = Logger.getLogger(BossTimeController.class.getName());

	private static BossTimeController _instance;

	private Random rnd = new Random(System.nanoTime());
	private int _time = 0;
	private int _timeM = 0;
	private Date day = new Date(System.currentTimeMillis());

	public static BossTimeController getInstance() {
		if (_instance == null)
			_instance = new BossTimeController();
		return _instance;
	}

	ArrayList<BossTemp> bosslist = null;

	private boolean isNow = false;

	public BossTimeController() {
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(1000);
				BossChack();
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	private void BossChack() {

		int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
		int minute = Calendar.getInstance().get(Calendar.MINUTE);
		
		bosslist = NewBossSpawnTable.getInstance().getlist();
		for (BossTemp temp : bosslist) {
			boolean isDay = false;
			for (int i : temp.Day) {
				if (i == day.getDay()) {
					isDay = true;
					break;
				}
			}
			if (!isDay) {
				continue;
			}
			
			if(temp.isSpawn){
				boolean ck = false;
				for (int Minute_temp : temp.SpawnMinute) {
					if(minute == Minute_temp){
						ck = true;
					}
				}
				if(ck)continue;
				else temp.isSpawn = false;
			}
			
			
			for (int i = 0; i < temp.SpawnHour.length; i++) {
				if(hour == temp.SpawnHour[i]-1 && minute==temp.SpawnMinute[i]){
						
					/**
					switch(temp.npcid){
						case 100338:
							if(!BossAlive.getInstance().is에르자베){
								BossAlive.getInstance().is에르자베 = true;
								BossAlive.getInstance().set에르자베타임(RealTimeClock.getInstance().getRealTime().getSeconds()+3600);
								int time = (int)(BossAlive.getInstance().ezTime - RealTimeClock.getInstance().getRealTime().getSeconds());
								L1World.getInstance().broadcastPacketToAll(new S_EventNotice(1, time, 3600, true));

							}
						break;
						case 100420:
							if(!BossAlive.getInstance().is샌드웜){
								BossAlive.getInstance().is샌드웜 = true;
								BossAlive.getInstance().set샌드웜타임(RealTimeClock.getInstance().getRealTime().getSeconds()+3600);
								int time = (int)(BossAlive.getInstance().sdTime - RealTimeClock.getInstance().getRealTime().getSeconds());
								L1World.getInstance().broadcastPacketToAll(new S_EventNotice(2, time, 3600, true));
							}
							break;
					}
					*/
				}
				if(hour == temp.SpawnHour[i] && minute == temp.SpawnMinute[i]){
					temp.isSpawn = true;
					GeneralThreadPool.getInstance().execute(new BossThread(temp));
				}	
			}
		}
	}
	
	
	/**보스 스폰 처리 */
	class BossThread implements Runnable {
		BossTemp temp;
		public BossThread(BossTemp _temp){
			temp = _temp;
		}
		
		public void run(){
			try{
				int rndtime = rnd.nextInt(temp.rndTime) + 1;
				if(rndtime > 0)Thread.sleep(rndtime * 60 * 1000);
				StoreBoss(temp.npcid, temp.SpawnLoc, temp.rndLoc, temp.Groupid, temp.isYn, temp.isMent, temp.Ment, temp.RMent,temp.DeleteTime);
			}catch(Exception e){}
		}
	}
	
	

	public void StoreBoss(int npcid, int[] Loc, int rndXY, int groupid, boolean isYN, boolean isMent, String ment, String Rment, int deleteTime) {
		try {
			L1Npc template = NpcTable.getInstance().getTemplate(npcid);
			if (template == null) {
				_log.warning("Boss mob data for id:" + npcid + " missing in npc table");
				System.out.println("보스스폰 컨트롤러 보스 npcid " + npcid + "가 존재하지 않습니다.");
				return;
			}
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcid);
			
			npc.setId(IdFactory.getInstance().nextId());

			L1Location loc = new L1Location(Loc[0], Loc[1], Loc[2]);
			 if (rndXY != 0) {
				loc.randomLocation(rndXY, false);
			}
			 
			/** 엔피씨 위치 정보 갱신 */
			npc.setLocation(loc);
			npc.getLocation().forward(5);

			/** 특정 엔피씨 오브젝트 추가 부분 *//*
			/** 등장시 4번액션 추가해서 범위내 모든유저에게 뿌려준다 *//*
			/** Appear 액션 참거짓 Chat 멘트 놀 */
			boolean Appear = false; String Chat = null;
			switch (npcid){	
				/** 왜곡의 제니스 퀸 지배 */
				case 910021: Appear = true; Chat = "\\fY$27422"; break;
				/** 불신의 시어 지배 */
				case 910028: Appear = true; Chat = "\\fY$27432"; break;
				/** 공포의 뱀파이어 지배 */
				case 910036: Appear = true; Chat = "\\fY$27442"; break;
				/** 죽음의 좀비 로드 지배 */
				case 910042: Appear = true; Chat = "\\fY$27452"; break;
				/** 지옥의 쿠거 지배 */
				case 910050: Appear = true; Chat = "\\fY$27462"; break;
				/** 불사의 머미 로드 지배 */
				case 910056: Appear = true; Chat = "\\fY$27472"; break;
				/** 잔혹한 아이리스 지배 */
				case 910062: Appear = true; Chat = "\\fY$27482"; break;
				/** 어둠의 나이트 발드 지배 */
				case 910069: Appear = true; Chat = "\\fY$27649"; break;
				/** 불멸의 리치 지배 */
				case 910075: Appear = true; Chat = "\\fY$27659"; break;
				/** 오만한 우그누스 지배 */
				case 910014: Appear = true; Chat = "\\fY$27669"; break;
				/** 포악한 사신의 영혼 */
				//case 7310078: Appear = true; Chat = "\\fY$27679"; break;
				/** 간악한 사신의 영혼 */
				//case 7310079: Appear = true; Chat = "\\fY$27689"; break;
				/** 사신 그림 리퍼 */
				case 910088: Appear = true; Chat = "\\fY$27699"; break;
			}
			
			/** 멘트가 있는 액션만 등장 액션 처리 하도록 변경 */
			for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(npc)) {
				npc.onPerceive(pc);
				pc.sendPackets(new S_NPCPack(npc), true);
				if(Appear) pc.sendPackets(new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Appear), true);
				if(Chat != null) pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, Chat), true);
			}
			
			/** 알람 로테이션마다 각 로테이션 정보 변경 */
			switch(npcid){
				case 100338:
					/** 로테이션 컨트롤러 현재 시간부터 언제까지 가능한지 체크 문 */
					long Time = System.currentTimeMillis();
					/** 30분 동안 유지 */
					long EndTime = System.currentTimeMillis() + (1000 * 1800);
					RotationNoticeTable.getInstance().UpdateNotice(1, new Timestamp(Time), new Timestamp(EndTime));
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (pc instanceof L1RobotInstance) continue;
						pc.알림서비스(pc, true);
					}
					break;
					
				case 100420:{
					long Time2 = System.currentTimeMillis();
					/** 30분 동안 유지 */
					long EndTime2 = System.currentTimeMillis() + (1000 * 1800);
					RotationNoticeTable.getInstance().UpdateNotice(2, new Timestamp(Time2), new Timestamp(EndTime2));
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (pc instanceof L1RobotInstance) continue;
						pc.알림서비스(pc, true);
					}
					break;
				}
				case 45752:
					long Time3 = System.currentTimeMillis();
					/** 30분 동안 유지 */
					long EndTime3 = System.currentTimeMillis() + (1000 * 1800);
					RotationNoticeTable.getInstance().UpdateNotice(11, new Timestamp(Time3), new Timestamp(EndTime3));
					for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
						if (pc instanceof L1RobotInstance) continue;
						pc.알림서비스(pc, true);
					}
					break;
			}
			
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			if (groupid > 0)
				L1MobGroupSpawn.getInstance().doSpawn(npc, groupid, true, false);
			
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			
			BossAlive.getInstance().BossSpawn(loc.getMapId());
			
			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);

			if (isMent) {
				L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\aH[보스알림] :"+ ment.toString()));
				L1World.getInstance().broadcastServerMessage("\\aH[보스 알림] :"+ ment.toString());
				L1World.getInstance().broadcastServerMessage("\\aJ[주요 리워드] :"+ Rment.toString());
			}
			if (isYN) {

			}

			if (0 < deleteTime) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, deleteTime * 1000);
				timer.begin();
			} else {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, 3600 * 1000);
				timer.begin();
			}
			
			LinAllManager.getInstance().BossAppend(npc.getName());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}