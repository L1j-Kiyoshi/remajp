package l1j.server.GameSystem;

import l1j.server.Config;
import l1j.server.server.model.L1MobGroupSpawn;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;

public class BossSpawnThread implements Runnable {

	L1NpcInstance npc = null;
	int tempx = 0;
	int tempy = 0;
	short tempm = 0;
	int timeMillisToDelete = 0;
	long _sleep = 1;
	int randomRange = 0;
	int sec = 0;

	public BossSpawnThread(L1NpcInstance _npc, int x, int y, short map,
			int _timeMillisToDelete, long sleep, int _randomRange) {
		npc = _npc;
		tempx = x;
		tempy = y;
		tempm = map;
		timeMillisToDelete = _timeMillisToDelete;
		_sleep = sleep;
		randomRange = _randomRange;
	}

	public void run() {
		try {
			if (npc == null)
				return;
			sec = (int) _sleep / 1000;
		//	System.out.println(npc.getName() + " 이 " + sec + "초 후 스폰됨");
	//		eva.EventLogAppend("[알림]-["+ sec +" 초] 후 "+ npc.getName() + " 스폰" );이벤트로그오류
			Thread.sleep(_sleep);
			
			npc.setMap(tempm);
			if (randomRange == 0) {
				npc.getLocation().set(tempx, tempy, tempm);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(tempx + (int) (Math.random() * randomRange)
							- (int) (Math.random() * randomRange));
					npc.setY(tempx + (int) (Math.random() * randomRange)
							- (int) (Math.random() * randomRange));
					if ((npc.getX() >= 32936 && npc.getY() <= 32945)
							&& (npc.getY() >= 32861 && npc.getY() <= 32870)
							&& npc.getMapId() == 410)
						continue;
					if (npc.getMap().isInMap(npc.getLocation())
							&& npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(tempx, tempy, tempm);
				}
			}

			npc.getLocation().set(tempx, tempy, tempm);
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(5);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			L1SpawnUtil.logofbossTime(npc);

			npc.getLight().turnOnOffLight();

			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc,
						timeMillisToDelete);
				timer.begin();
			}

			if (npc.getNpcId() == 100338) {// 에르자베
				L1MobGroupSpawn.getInstance().doSpawn(npc, 106, false, false);
			}

			if (npc.getNpcId() == 100824) {// 스파이스
				L1MobGroupSpawn.getInstance().doSpawn(npc, 150, false, false);
			}

			if (npc.getNpcId() == 45684) {
				L1World.getInstance().broadcastPacketToAll(
						new S_PacketBox(S_PacketBox.GREEN_MESSAGE,
								"화룡의 둥지 최상부에 발라카스가 출현하였습니다."), true);
			}

	//		L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"보스가 스폰되었습니다 " + npc.getName()), true);
			
			try {
				if (Config.접속채팅모니터() > 0) {
					S_SystemMessage sm = new S_SystemMessage("보스스폰 "
							+ npc.getName() + " X " + npc.getX() + " / y "
							+ npc.getY() + " / id " + npc.getMapId());
					for (L1PcInstance gm : Config.toArray접속채팅모니터()) {
						L1PcInstance ck = L1World.getInstance().getPlayer(
								gm.getName());
						if (ck == null) {
							Config.remove접속(gm);
							continue;
						}
						if (gm.getNetConnection() == null) {
							Config.remove접속(gm);
							continue;
						}
						gm.sendPackets(sm);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
