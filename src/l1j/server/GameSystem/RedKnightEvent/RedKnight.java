package l1j.server.GameSystem.RedKnightEvent;

import java.util.Random;

import javolution.util.FastTable;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.L1SpawnUtil;

public class RedKnight implements Runnable {

	private static Random rnd = new Random(System.currentTimeMillis());
	private int _mapnum = 0;
	private int step = 0;
	private int READY_TIME = 4;
	private int ROUND_1_STEP = 8;
	private int ROUND_2_STEP = 2;
	private int ROUND_3_STEP = 2;
	private int END_TIME = 13;
	private static final int SPAWN = 0;
	private static final int READY = 1;
	private static final int MEMBER_CHECK = 2;
	private static final int ROUND_1 = 3;
	private static final int ROUND_2 = 4;
	private static final int ROUND_3 = 5;
	private static final int END = 6;
	private static final int TIME_OVER = 7;

	private FastTable<L1NpcInstance> 바리1 = null;
	private FastTable<L1NpcInstance> 바리2 = null;
	private FastTable<L1NpcInstance> 바리3 = null;

	private FastTable<L1NpcInstance> 잡몹 = null;
	private L1NpcInstance 보스 = null;

	private L1NpcInstance 데포로쥬 = null;
	private L1NpcInstance 붉은기사단1 = null;
	private L1NpcInstance 붉은기사단2 = null;

	private boolean on = true;

	public RedKnight(int mapid) {
		_mapnum = mapid;
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			private int TIMER = 90;

			@Override
			public void run() {
				try {
					if (!on)
						return;
					if (TIMER == 5) {
						GREEN_MSG("전령: 서두르세요. 5분 후에 적의 증원군이 도착할 것 같습니다. 그 전에 작전을 완료하지 못하면 마을로 후퇴해야 합니다.");
					} else if (TIMER == 4) {
						GREEN_MSG("전령: 서두르세요. 4분 후에 적의 증원군이 도착할 것 같습니다.");
					} else if (TIMER == 3) {
						GREEN_MSG("전령: 서두르세요. 3분 후에 적의 증원군이 도착할 것 같습니다.");
					} else if (TIMER == 2) {
						GREEN_MSG("전령: 서두르세요. 2분 후에 적의 증원군이 도착할 것 같습니다.");
					} else if (TIMER == 1) {
						GREEN_MSG("전령: 서두르세요. 1분 후에 적의 증원군이 도착할 것 같습니다.");
					} else if (TIMER == 0) {
						GREEN_MSG("전령: 적의 증원군이 코 앞까지 도착했습니다. 더 이상 지체할 수 없으니 마을로 후퇴하겠습니다.");
						step = TIME_OVER;
						return;
					}
					TIMER--;
				} catch (Exception e) {
				}
				GeneralThreadPool.getInstance().schedule(this, 60000);
			}
		}, 60000);
	}

	@Override
	public void run() {
		int sleep = 1;
		try {
			switch (step) {
			case SPAWN:
				바리1 = RedKnightSpawn.getInstance().fillSpawnTable(_mapnum, 0);
				바리2 = RedKnightSpawn.getInstance().fillSpawnTable(_mapnum, 1);
				바리3 = RedKnightSpawn.getInstance().fillSpawnTable(_mapnum, 2);
				sleep = 60;
				step++;
				break;
			case READY:
				if (READY_TIME == 4)
					GREEN_MSG("전령: 4분 후 출발할 예정입니다.");
				else
					GREEN_MSG("전령: " + READY_TIME
							+ "분 후 출발할 예정입니다. 참여 인원이 10명 미만이면 출정이 취소됩니다.");
				sleep = 60;
				READY_TIME--;
				if (READY_TIME <= 0)
					step++;
				break;
			case MEMBER_CHECK:
				int count = 0;
				for (L1Object ob : L1World.getInstance()
						.getVisibleObjects(_mapnum).values()) {
					if (ob == null)
						continue;
					if (ob instanceof L1PcInstance)
						count++;
				}
				if (count < 1) {
					GREEN_MSG("전령: 참여 인원 부족으로 이번 출정이 취소되었습니다. 다음 출정을 기다려 주세요.");
					Thread.sleep(3000);
					HOME_TELEPORT();
					Object_Delete();
					return;
				} else {
					GREEN_MSG("전령: 출정에 앞서 데포로쥬님이 여러분들을 격려하러 오실 예정입니다.");
					sleep = 5;
				}
				step++;
				break;
			case ROUND_1:
				sleep = 5;
				if (ROUND_1_STEP == 8) {
					붉은기사단1 = L1SpawnUtil.spawn4(32772, 32814, (short) _mapnum,
							4, 100660, 0, 0, 0);
					붉은기사단2 = L1SpawnUtil.spawn4(32768, 32814, (short) _mapnum,
							4, 100660, 0, 0, 0);
					GeneralThreadPool.getInstance().schedule(
							new NpcMove(붉은기사단1, 4, 5), 50);
					GeneralThreadPool.getInstance().schedule(
							new NpcMove(붉은기사단2, 4, 5), 50);
				} else if (ROUND_1_STEP == 7) {
					SHOUT_MSG(붉은기사단2, "붉은 기사단: 집중해 주십시요!! 데포로쥬님이 오십니다.");
					데포로쥬 = L1SpawnUtil.spawn4(32770, 32814, (short) _mapnum, 4,
							100659, 0, 0, 0);
					GeneralThreadPool.getInstance().schedule(
							new NpcMove(데포로쥬, 4, 7), 50);
				} else if (ROUND_1_STEP == 6) {
					SHOUT_MSG(데포로쥬,
							"데포로쥬: 이렇게 우리 붉은 기사단을 도우러 와준 그대들의 노고를 치하하네.");
					sleep = 10;
				} else if (ROUND_1_STEP == 5) {
					SHOUT_MSG(데포로쥬,
							"데포로쥬: 서신과 부관들을 통해 이미 전해 들었겠지만 이번 임무는 정말 중요하다네.");
					sleep = 10;
				} else if (ROUND_1_STEP == 4) {
					SHOUT_MSG(데포로쥬,
							"데포로쥬: 곧.. 앞을 가로 막고 있는 방책을 무너트릴테니 앞으로 나아가서 단서를 찾아오게.");
					sleep = 10;
				} else if (ROUND_1_STEP == 3) {
					SHOUT_MSG(데포로쥬, "데포로쥬: 그대들을 믿고 난 돌아가서 성의 탈환을 준비 하겠네.");
					GeneralThreadPool.getInstance().schedule(
							new NpcMove(붉은기사단1, 0, 5), 2500);
					GeneralThreadPool.getInstance().schedule(
							new NpcMove(붉은기사단2, 0, 5), 2500);
					GeneralThreadPool.getInstance().schedule(
							new NpcMove(데포로쥬, 0, 7), 50);
					sleep = 10;
				} else if (ROUND_1_STEP == 2) {
					바리삭제(바리1);
					잡몹 = RedKnightSpawn.getInstance()
							.fillSpawnTable(_mapnum, 3);
					GREEN_MSG("전령: 첫 번째 방책이 파괴됐다. 모두 진격하라!!");
				} else if (ROUND_1_STEP == 1) {
					if (잡몹체크()) {
						보스 = L1SpawnUtil.spawn2(32770, 32923, (short) _mapnum,
								100653, 3, 0, 0);// 라미아스
						GREEN_MSG("부대장: 여기를 어떻게 찾아냈지? 우리 검은 기사단을 건드린 죄값을 치르게 해주마!!");
					} else {
						GeneralThreadPool.getInstance()
								.schedule(this, 5 * 1000);
						return;
					}
				} else if (ROUND_1_STEP == 0) {
					if (보스체크())
						step++;
				}
				if (ROUND_1_STEP != 0)
					ROUND_1_STEP--;
				break;
			case ROUND_2:
				sleep = 5;
				if (ROUND_2_STEP == 2) {
					바리삭제(바리2);
					잡몹 = RedKnightSpawn.getInstance()
							.fillSpawnTable(_mapnum, 5);
					GREEN_MSG("전령: 두 번째 방책이 파괴됐다. 돌격~앞으로~~!!");
				} else if (ROUND_2_STEP == 1) {
					if (잡몹체크()) {
						보스 = L1SpawnUtil.spawn2(32771, 33009, (short) _mapnum,
								100654, 3, 0, 0);// 바로드
						GREEN_MSG("부대장: 여기까지 왔다는 것은 앞의 부대를 쓰러트렸다는 것인데... 쉽게 볼 놈들이 아니군. 내가 상대해 주마!!");
					} else {
						GeneralThreadPool.getInstance()
								.schedule(this, 5 * 1000);
						return;
					}
				} else if (ROUND_2_STEP == 0) {
					if (보스체크())
						step++;
				}
				if (ROUND_2_STEP != 0)
					ROUND_2_STEP--;
				break;
			case ROUND_3:
				sleep = 5;
				if (ROUND_3_STEP == 2) {
					바리삭제(바리3);
					잡몹 = RedKnightSpawn.getInstance()
							.fillSpawnTable(_mapnum, 7);
					GREEN_MSG("전령: 마지막 방책이 파괴됐다. 조금 더 힘을 내라~~!!");
				} else if (ROUND_3_STEP == 1) {
					if (잡몹체크()) {
						보스 = L1SpawnUtil.spawn2(32769, 33093, (short) _mapnum,
								100655, 3, 0, 0);// 그림리퍼
						GREEN_MSG("부대장: 씹어먹어도 시원찮을 놈들!! 내 너희를 가만두지 않겠다!!");
					} else {
						GeneralThreadPool.getInstance()
								.schedule(this, 5 * 1000);
						return;
					}
				} else if (ROUND_3_STEP == 0) {
					if (보스체크())
						step++;
				}
				if (ROUND_3_STEP != 0)
					ROUND_3_STEP--;
				break;
			case END:
				if (END_TIME <= 0) {
					HOME_TELEPORT();
					Object_Delete();
					return;
				} else if (END_TIME == 13) {
					GREEN_MSG("전령: 이번 원정을 성공적으로 끝마친 것에 대해 데포로쥬님이 기뻐하실 것입니다.");
					sleep = 3;
				} else if (END_TIME == 12) {
					GREEN_MSG("전령: 획득한 단서 3종류를 '참모'에게 가져가 주시기 바랍니다.");
					sleep = 3;
				} else if (END_TIME == 11) {
					GREEN_MSG("시스템 메시지: 1분후 마을로 강제 텔레포트 됩니다.");
					sleep = 50;
				} else {
					GREEN_MSG("시스템 메시지: " + END_TIME + "초");
				}
				END_TIME--;
				break;
			case TIME_OVER:
				HOME_TELEPORT();
				Object_Delete();
				return;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, sleep * 1000);
	}

	private boolean 보스체크() {
		if (보스 == null || 보스._destroyed || 보스.isDead())
			return true;
		return false;
	}

	private boolean 잡몹체크() {
		if (잡몹 == null || 잡몹.size() <= 0)
			return true;
		for (L1NpcInstance npc : 잡몹) {
			if (npc == null || npc._destroyed || npc.isDead())
				continue;
			// System.out.println(npc.getX()+" > "+npc.getY());
			return false;
		}
		return true;
	}

	private void 바리삭제(FastTable<L1NpcInstance> list) {
		if (list == null || list.size() <= 0)
			return;
		for (L1NpcInstance npc : list) {
			if (npc == null || npc._destroyed)
				return;
			npc.getMap().setPassable(npc.getLocation(), true);
			npc.deleteMe();
		}
	}

	private void GREEN_MSG(String msg) {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum)
				.values()) {
			if (ob == null)
				continue;
			if (ob instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) ob;
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg));
			}
		}
	}

	private void SHOUT_MSG(L1NpcInstance npc, String msg) {
		Broadcaster
				.broadcastPacket(npc, new S_NpcChatPacket(npc, msg, 2), true);
		/*
		 * for(L1Object ob :
		 * L1World.getInstance().getVisibleObjects(_mapnum).values()){ if(ob ==
		 * null) continue; if(ob instanceof L1PcInstance){ L1PcInstance pc =
		 * (L1PcInstance) ob; pc.sendPackets(new S_SystemMessage(msg, true)); }
		 * }
		 */
	}

	private void HOME_TELEPORT() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum)
				.values()) {
			if (ob == null)
				continue;
			if (ob instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) ob;
				L1Teleport.teleport(pc, 33436 + rnd.nextInt(12),
						32795 + rnd.nextInt(14), (short) 4, 5, true);
			}
		}
	}

	private void Object_Delete() {
		on = false;
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum)
				.values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				npc.deleteMe();
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != _mapnum)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(
					obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}

	class NpcMove implements Runnable {

		private L1NpcInstance npc = null;
		private int count = 0;
		private int direct = 0;

		public NpcMove(L1NpcInstance _npc, int _direct, int _count) {
			npc = _npc;
			count = _count;
			direct = _direct;
		}

		@Override
		public void run() {
			try {
				if (count <= 0) {
					if (direct == 0)
						npc.deleteMe();
					return;
				}
				count--;
				npc.setDirectionMove(direct);
				GeneralThreadPool.getInstance().schedule(this, 640);
			} catch (Exception e) {
			}
		}

	}
}