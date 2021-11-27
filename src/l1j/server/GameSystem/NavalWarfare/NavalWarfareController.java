package l1j.server.GameSystem.NavalWarfare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.ORIM.ORIM;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
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
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;

public class NavalWarfareController implements Runnable {

	private static final byte STATUS_READY = 0;
	private static final byte STATUS_LEVEL_1 = 1;
	private static final byte STATUS_LEVEL_2 = 2;
	private static final byte STATUS_LEVEL_3 = 3;
	private static final byte STATUS_LEVEL_4 = 4;
	private static final byte STATUS_LEVEL_5 = 5;
	private static final byte STATUS_LEVEL_END = 6;
	private static final Random _rnd = new Random(System.nanoTime());

	public boolean on = true;

	public boolean level = false;

	private boolean trapCK = false;
	private boolean shootTrapCK = false;
	public boolean bossMoveOn = false;
	public boolean bossMoveOn2 = false;
	public boolean bossMoveOn3 = false;
	private FastTable<L1NpcInstance> baseList;
	private FastTable<L1NpcInstance> monList = new FastTable<L1NpcInstance>();
	private FastTable<L1NpcInstance> trapList;
	public int mapid = 0;
	private byte step = 0;
	private byte sub_step = 0;
	public byte stage = 1;
	public int score = 0;
	private L1NpcInstance ship = null;

	private FastTable<L1PcInstance> _member = null;

	public void addMember(L1PcInstance pc) {
		_member.add(pc);
	}

	public int getMembersCount() {
		return _member.size();
	}

	public void removeMember(L1PcInstance pc) {
		_member.remove(pc);
	}

	public void clearMember() {
		_member.clear();
	}

	public boolean isMember(L1PcInstance pc) {
		return _member.contains(pc);
	}

	public L1PcInstance[] getMemberArray() {
		return _member.toArray(new L1PcInstance[getMembersCount()]);
	}

	public NavalWarfareController(L1PcInstance pc, int _mapid, boolean lv) {
		level = lv;
		mapid = _mapid;
		baseList = NavalWarfareSpawn.getInstance().spawnList(_mapid, 0);
		_member = new FastTable<L1PcInstance>();
		/** 인원 체크 스레드 **/
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				if (!on)
					return;
				try {
					MemberCheck();
					if (monList != null && monList.size() > 0) {
						L1NpcInstance[] list = (L1NpcInstance[]) monList.toArray(new L1NpcInstance[monList
								.size()]);
						for (L1NpcInstance npc : list) {
							if (npc == null || npc.isDead() || npc._destroyed)
								monList.remove(npc);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					GeneralThreadPool.getInstance().schedule(this, 500);
				}
			}
		}, 5000);
	}

	// 드랍 리스트 및 밸런스

	// 배파편?
	// 순위 및 점수?
	private static final int bossid1[] = { 100146, 100154, 100153, 100152,
			100151, 100150, 100148, 100145, 100146, 100147 };

	// private static final int bossid2[] =
	// {100146,100154,100153,100152,100151,100150,100148,100145,100146,100147};
	private void 대포액션() {
		for (L1NpcInstance npc : baseList) {
			if (npc != null && npc.getNpcId() == 100108) {
				Broadcaster.broadcastPacket(npc, new S_DoActionGFX(npc.getId(),
						2), true);
			}
		}

	}

	private void 화면이팩() {
		L1PcInstance[] list = getMemberArray();
		if (list != null && list.length > 0) {
			for (L1PcInstance pc : list) {
				if (pc == null)
					continue;
				pc.sendPackets(new S_PacketBox(83, 1));
				pc.sendPackets(new S_PacketBox(83, 2));
			}
		}
		list = null;
	}

	private int _timecount = -1;

	@Override
	public void run() {
		try {
			// System.out.println("해상전 스레드 가동중");
			if (!on) {
				reset();
				return;
			}
			if (getMembersCount() <= 2) {
				on = false;
				GeneralThreadPool.getInstance().schedule(this, 1000);
				return;
			}

			if (_timecount > 1) {
				_timecount--;
				GeneralThreadPool.getInstance().schedule(this, 500);
				return;
			}

			/*
			 * if(totalDamageCount > 10){ GreenMsg("\\f=배가 많이 손상되어 침몰하였습니다.");
			 * Thread.sleep(5000); for(L1PcInstance pc :
			 * L1World.getInstance().getAllPlayers()){ if(pc.getMapId() ==
			 * mapid){ L1Teleport.teleport(pc, 32574, 32942, (short)0, 4, true);
			 * } } on = false; GeneralThreadPool.getInstance().schedule(this,
			 * 1000); return; }
			 */
			//System.out.println("스텝> " + step + " 보조스텝> " + sub_step + " 스테이지> " + stage);
			switch (step) {
			case STATUS_READY:
				오림메시지();
				_timecount = 5 * 2;
				// GeneralThreadPool.getInstance().schedule(this, 5000);
				break;
			case STATUS_LEVEL_1:
				if (sub_step == 1) {
					int shipid = 100110;
					ship = L1SpawnUtil.spawn2(32797, 32831, (short) mapid,
							shipid, 0, 0, 0, level);
					GeneralThreadPool.getInstance().execute(
							new ShipMove(32797, 32811));
					_timecount = 20 * 2;
				} else if (sub_step == 2) {
					화면이팩();
					GreenMsg("첫번째 부대가 난입 했습니다."); // 원래는 노란색
				} else if (sub_step == 3) {
					// Thread.sleep(19800);
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 4) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 5) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 6) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 7) {
					대포액션();
				} else if (sub_step == 8) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 9) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 10) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 11) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 12) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 13) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 14) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 15) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 16) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 17) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 18) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(12) + 2, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 19) {
					GreenMsg("적의 배가 손상되여 후퇴 하였습니다."); // 원래는 노란색
					GeneralThreadPool.getInstance().execute(
							new ShipMove(32797, 32831));
				} else if (sub_step == 20) {
					// Thread.sleep(19800);
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 21) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 22) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 23) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 24) {
					대포액션();
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 25) {
					int rnd = _rnd.nextInt(100) + 1;
					if (rnd < 50) {
						GreenMsg("오림: 주변에 이상한 기운이 느껴집니다..조심하세요!");
					} else {
						GreenMsg("오림: 적들이 후퇴 해서 잠시 숨좀 돌릴 수 있겠군요.");
						step = STATUS_LEVEL_2;
						sub_step = 0;
					}
					_timecount = 60 * 2;// Thread.sleep(10000);Thread.sleep(10000);
					// /GeneralThreadPool.getInstance().schedule(this, 1000);
				} else if (sub_step == 26) {// 보스
					/*
					 * 100146 올딘 제니스퀸 100157 해상전 대왕 오징어 100154 해상전 카스파 100153
					 * 해상전 세마 2 100152 해상전 세마 100151 해상전 메르키오르 100150 해상전 발터자르
					 * 100148 해상전 바포메트 100145 올딘 리치 100146 올딘 제니스퀸 100147 올딘 타락
					 */
					int bossid = 0;
					int rrr = _rnd.nextInt(100);
					if (level) {
						if (rrr < 1) {
							bossid = 100157;
						} else {
							bossid = bossid1[_rnd.nextInt(bossid1.length)];
						}
					} else {
						bossid = bossid1[_rnd.nextInt(bossid1.length)];
					}
					monList.add(L1SpawnUtil.spawn2(32789 + _rnd.nextInt(10),
							32797 + _rnd.nextInt(11), (short) mapid, bossid, 0,
							0, 0, level));
					sub_step = 0;
					step = STATUS_LEVEL_2;
					_timecount = 60 * 2;// Thread.sleep(10000);Thread.sleep(10000);
					// GeneralThreadPool.getInstance().schedule(this, 1000);
				}
				sub_step++;
				break;
			case STATUS_LEVEL_2:
				if (sub_step == 1) {
					_timecount = 15 * 2;
				} else if (sub_step == 2) {
					GreenMsg("오림: 적들이 몰려오고 있습니다. 조심하세요!");
					_timecount = 5 * 2;
				} else if (sub_step == 3) {
					// Thread.sleep(5000);
					int shipid = 100111;
					ship = L1SpawnUtil.spawn2(32797, 32831, (short) mapid,
							shipid, 0, 0, 0, level);
					GeneralThreadPool.getInstance().execute(
							new ShipMove(32797, 32811));
					_timecount = 19 * 2;
				} else if (sub_step == 4) {
					// /Thread.sleep(19800);
					화면이팩();
					GreenMsg("두번째 부대가 난입 했습니다."); // 원래는 노란색
				} else if (sub_step == 5) {
					// Thread.sleep(19800);
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 6) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 7) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 8) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 9) {
					대포액션();
				} else if (sub_step == 10) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;
				} else if (sub_step == 11) {
					// Thread.sleep(10000);
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 12) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 13) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 14) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 15) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 16) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 17) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 18) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 19) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 20) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 30, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 21) {
					GreenMsg("적의 배가 손상되여 후퇴 하였습니다."); // 원래는 노란색
					GeneralThreadPool.getInstance().execute(
							new ShipMove(32797, 32831));
				} else if (sub_step == 22) {
					// Thread.sleep(19800);
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 23) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 24) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 25) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 26) {
					대포액션();
					_timecount = 10 * 2;// Thread.sleep(10000);Thread.sleep(10000);
				} else if (sub_step == 27) {
					int rnd = _rnd.nextInt(100) + 1;
					if (rnd < 50) {
						GreenMsg("오림: 주변에 이상한 기운이 느껴집니다..조심하세요!");
					} else {
						GreenMsg("오림: 적들이 후퇴 해서 잠시 숨좀 돌릴 수 있겠군요.");
						step = STATUS_LEVEL_3;
						sub_step = 0;
					}
					_timecount = 60 * 2;// Thread.sleep(10000);Thread.sleep(10000);
					// _timecount =
					// 1*2;//GeneralThreadPool.getInstance().schedule(this,
					// 1000);
				} else if (sub_step == 28) {// 보스
					int bossid = 0;
					int rrr = _rnd.nextInt(100);
					if (level) {
						if (rrr < 1) {
							bossid = 100157;
						} else {
							bossid = bossid1[_rnd.nextInt(bossid1.length)];
						}
					} else {
						bossid = bossid1[_rnd.nextInt(bossid1.length)];
					}
					monList.add(L1SpawnUtil.spawn2(32789 + _rnd.nextInt(10),
							32797 + _rnd.nextInt(11), (short) mapid, bossid, 0,
							0, 0, level));
					sub_step = 0;
					step = STATUS_LEVEL_3;
					_timecount = 60 * 2;// Thread.sleep(10000);Thread.sleep(10000);
				}
				sub_step++;
				break;
			case STATUS_LEVEL_3:
				if (sub_step == 1) {
					GreenMsg("오림: 으..! 이번에 오는 배에서는 올딘의 기운이 느껴집니다!");
					_timecount = 15 * 2;// Thread.sleep(15000);
				} else if (sub_step == 2) {
					int shipid = 100112;
					ship = L1SpawnUtil.spawn2(32797, 32831, (short) mapid,
							shipid, 0, 0, 0, level);
					GeneralThreadPool.getInstance().execute(
							new ShipMove(32797, 32811));
					_timecount = 19 * 2;// Thread.sleep(19800);
				} else if (sub_step == 3) {
					화면이팩();
					GreenMsg("세번째 부대가 난입 했습니다."); // 원래는 노란색
				} else if (sub_step == 4) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 5) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 6) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 7) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 8) {
					대포액션();
				} else if (sub_step == 9) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 10) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 11) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 12) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 13) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 14) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 15) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 16) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 17) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 18) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 19) {
					NavalWarfareSpawn.getInstance().monster_spawn(mapid,
							_rnd.nextInt(6) + 37, level); // 몹스폰
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 20) {
					GreenMsg("적의 배가 손상되여 후퇴 하였습니다."); // 원래는 노란색
					GeneralThreadPool.getInstance().execute(
							new ShipMove(32797, 32831));
				} else if (sub_step == 21) {
					// if(drakeCount >= 3){
				} else if (sub_step == 22) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 23) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 24) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 25) {
					대포액션();
					_timecount = 1;// Thread.sleep(500);
				} else if (sub_step == 26) {
					대포액션();
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 27) {
					GreenMsg("오림: 올딘의 기운이 점점 느껴집니다!");
					_timecount = 60 * 2;// Thread.sleep(10000);Thread.sleep(10000);
				} else if (sub_step == 28) {
					monList.add(L1SpawnUtil.spawn2(32789 + _rnd.nextInt(10),
							32797 + _rnd.nextInt(11), (short) mapid, 100149, 0,
							0, 0, level));
					_timecount = 1 * 2;// GeneralThreadPool.getInstance().schedule(this,
										// 1000);
					sub_step = 0;
					step = STATUS_LEVEL_4;
				}
				sub_step++;
				break;
			case STATUS_LEVEL_4: // 보스
				for (L1NpcInstance npc : monList) {
					if (npc == null || npc._destroyed || npc.isDead()) {
						if (monList.contains(npc))
							monList.remove(npc);
					}
				}
				if (monList == null || monList.size() <= 0) {
					step = STATUS_LEVEL_5;
					sub_step = 1;
				}
				_timecount = 2 * 2;// GeneralThreadPool.getInstance().schedule(this,
									// 3000);
				break;
			case STATUS_LEVEL_5:
				if (sub_step == 1) {
					GreenMsg("오림: 많은 설명이 필요하진 않을것 같습니다...");
					// L1MonsterInstance mon = (L1MonsterInstance)
					// L1SpawnUtil.spawn2(32795, 32803, (short)mapid, 100210, 0,
					// 0, 0); // 보상 미믹 스폰
					// mon.해상전보상미믹단계 = 보상미믹레벨;
					_timecount = 5 * 2;//
				} else if (sub_step == 2) {
					GreenMsg("오림: 지금쯤 말하는 섬은 놈들에 의해 폐허가 되었을 겁니다.");
					_timecount = 10 * 2;//
				} else if (sub_step == 3) {
					// Thread.sleep(10000);
					GreenMsg("오림: 다시 돌아가면, 기억하고 있던 많은 곳들은 흔적도 남아 있지 않을겁니다.");
					_timecount = 5 * 2;//
				} else if (sub_step == 4) {
					// Thread.sleep(5000);
					GreenMsg("오림: 싸움은 시작 되었습니다. 저는 스승님을 찾아 이 싸움을 끝낼 방법을 찾겠습니다.");
					_timecount = 10 * 2;// Thread.sleep(10000);
				} else if (sub_step == 5) {
					GreenMsg("오림: 여러분은 부디 살아남아 주세요. 이 싸움이 끝날때까지...");
					_timecount = 5 * 2;// Thread.sleep(5000);
				} else if (sub_step == 6) {
					GreenMsg("오림: 글루디오 영지에 거의 도착했습니다! 어서 이 위험을 사람들에게 알려야 합니다!");
					_timecount = 5 * 2;// Thread.sleep(10000);
				} else if (sub_step == 7) {
					GreenMsg("종료 까지 3분 남았습니다.");
					_timecount = 60 * 2;// Thread.sleep(10000);
				} else if (sub_step == 8) {
					GreenMsg("종료 까지 2분 남았습니다.");
					_timecount = 60 * 2;// Thread.sleep(10000);
				} else if (sub_step == 9) {
					GreenMsg("종료 까지 1분 남았습니다.");
					_timecount = 30 * 2;// Thread.sleep(10000);
				} else if (sub_step == 10) {
					GreenMsg("종료 까지 30초 남았습니다.");
					_timecount = 25 * 2;// Thread.sleep(10000);
				} else if (sub_step == 11) {
					GreenMsg("종료 까지 5초 남았습니다.");
					_timecount = 5 * 2;// Thread.sleep(10000);
				} else if (sub_step == 12) {
					step = STATUS_LEVEL_END;
					_timecount = 1 * 2;// Thread.sleep(10000);
				}
				sub_step++;
				break;
			case STATUS_LEVEL_END:
				// 순위 및 점수 출력
				/*
				 * rankInsert(); int rank = rankLoad(); L1PcInstance[] list =
				 * getMemberArray(); if(list != null && list.length > 0){
				 * for(L1PcInstance pc : list){ if(pc == null) continue;
				 * pc.sendPackets(new S_NavarWarfare_Ranking(rank >= 6 ? 6 :
				 * rank, score), true); //pc.sendPackets(new
				 * S_SystemMessage("해상전 - 점수: "+score+" 순위: "+rank+"위"), true);
				 * } } list = null;
				 */
				// Thread.sleep(30000);
				L1PcInstance[] list2 = Getmembers();
				if (list2 != null && list2.length > 0) {
					for (L1PcInstance pc : list2) {
						if (pc == null)
							continue;
						L1Teleport.teleport(pc, 32574, 32942, (short) 0, 4,
								true);
					}
				}
				list2 = null;
				on = false;
				score = 0;

				GeneralThreadPool.getInstance().schedule(this, 1000);
				break;
			default:
				break;
			}
			GeneralThreadPool.getInstance().schedule(this, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reset() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(mapid)
				.values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				npc.deleteMe();
			}
			if (ob instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) ob;
				L1Teleport.teleport(pc, 32574, 32942, (short) 0, 4, true);
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != mapid)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(
					obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}

		if (baseList != null && baseList.size() > 0)
			baseList.clear();
		baseList = null;
		if (monList != null && monList.size() > 0)
			monList.clear();
		monList = null;
		if (trapList != null && trapList.size() > 0)
			trapList.clear();
		trapList = null;
		ship = null;
		if (_member != null && _member.size() > 0)
			_member.clear();
		_member = null;
		NavalWarfare.getInstance().quit(
				L1WorldMap.getInstance().getMap((short) mapid));

		ORIM OR = GameList.getOR(mapid);
		if (OR != null) {
			OR.Reset();
		}

	}

	private boolean 오림메시지() {
		boolean ck = false;
		String msg = "";
		switch (step) {
		case STATUS_READY:
			if (sub_step == 0)
				msg = "오림 : 여러분 당황하지 마시고 배를 사수 해야 합니다.";
			else if (sub_step == 1)
				msg = "오림 : 우리 배를 공격하고 있는 것은 올딘이라 불리는 것입니다.";
			else if (sub_step == 2)
				msg = "오림 : 자세한 얘기는 조금 후에 하지!.";
			else if (sub_step == 3) {
				msg = "오림 : 명심하게 올딘을 상대 하려면 최소 3명이 있어야 하오! 배가 충돌하면, 적들이 뛰어들 것입니다! 조심하세요!";
				step = STATUS_LEVEL_1;
				sub_step = 0;
				ck = true;
				/*
				 * int shipid = 100110; ship = L1SpawnUtil.spawn2(32797, 32831,
				 * (short)mapid, shipid, 0, 0, 0);
				 * GeneralThreadPool.getInstance().execute(new ShipMove(32797,
				 * 32811));
				 */
			}
			/*
			 * else if(sub_step == 4){ msg =
			 * "오림 : 이제, 적의 공격에 대비해야 합니다. 서둘러주세요."; step = STATUS_LEVEL_1;
			 * sub_step = -1; ck = true; }
			 */
			sub_step++;
			break;
		case STATUS_LEVEL_1:
			if (sub_step == 0)
				msg = "오림 : 배에 숨어들었던 몬스터들이 있습니다! 처치해주세요!";
			else if (sub_step == 1)
				msg = "오림 : 주변에 뭔가가 맴돌고 있는 기운이 감지됩니다. 조심하세요.";
			else if (sub_step == 2)
				msg = "오림 : 주변에 상어떼가 있습니다. 조심하세요.";
			else if (sub_step == 3)
				msg = "오림 : 전보다 강한 기운이 느껴집니다! 조심하세요!";
			else if (sub_step == 6)
				msg = "오림 : 배가 곧 충돌합니다! 바다에 빠지지 않게 조심하세요!";
			else if (sub_step == 11)
				msg = "오림 : 배가 충돌하면, 적들이 뛰어들 것입니다! 조심하세요!";
			break;
		default:
			return false;
		}
		GreenMsg(msg);
		return ck;
	}

	private void GreenMsg(String msg) {
		if (msg == null || msg.equalsIgnoreCase(""))
			return;
		L1PcInstance[] list = getMemberArray();
		if (list != null && list.length > 0) {
			for (L1PcInstance pc : list) {
				if (pc == null)
					continue;
				pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg),
						true);
			}
		}
		list = null;
	}

	protected int calcSleepTime(L1NpcInstance npc, int sleepTime, int type) {
		switch (npc.getMoveState().getMoveSpeed()) {
		case 0:
			break;
		case 1:
			sleepTime -= (sleepTime * 0.25);
			break;
		case 2:
			sleepTime *= 2;
			break;
		}
		if (npc.getMoveState().getBraveSpeed() == 1) {
			sleepTime -= (sleepTime * 0.25);
		}
		if (npc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
			if (type == L1NpcInstance.ATTACK_SPEED
					|| type == L1NpcInstance.MAGIC_SPEED) {
				sleepTime += (sleepTime * 0.25);
			}
		}
		return sleepTime;
	}

	private void MemberCheck() {
		L1PcInstance[] list = Getmembers();
		if (list != null && list.length > 0) {
			for (L1PcInstance pc : list) {
				if (pc == null || pc.getNetConnection() == null
						|| pc.getMapId() != mapid || pc.getParty() == null)
					removeMember(pc);
			}
		}
		list = null;
	}

	private L1PcInstance[] Getmembers() {
		FastTable<L1PcInstance> _member = new FastTable<L1PcInstance>();
		L1PcInstance[] list = getMemberArray();
		if (list != null && list.length > 0) {
			for (L1PcInstance tpc : list) {
				if (tpc == null) {
					removeMember(tpc);
					continue;
				}
				L1PcInstance pc = L1World.getInstance()
						.getPlayer(tpc.getName());
				if (pc == null) {
					removeMember(tpc);
					continue;
				}
				if (pc.getNetConnection() == null) {
					removeMember(tpc);
					continue;
				}
				if (pc.getMapId() != mapid) {
					removeMember(tpc);
					continue;
				}
				if (pc.getParty() == null) {
					removeMember(tpc);
					continue;
				}
				_member.add(tpc);
			}
		} else {
			return null;
		}
		list = null;
		if (_member.size() == 0) {
			return null;
		}
		return _member.toArray(new L1PcInstance[_member.size()]);
	}

	public void scorePacket(int score2) {
		L1PcInstance[] list = getMemberArray();
		if (list != null && list.length > 0) {
			for (L1PcInstance pc : list) {
				if (pc == null)
					continue;
				pc.sendPackets(new S_PacketBox(true, score2), true);
			}
		}
		list = null;
	}

	/** ad - True 방어 False 공격 **/
	public void TrapAction(boolean ad) {
		if ((ad && trapCK) || (!ad && shootTrapCK))
			return;
		int count = 0;
		if (trapList != null && trapList.size() > 0) {
			L1PcInstance[] list = getMemberArray();
			if (list != null && list.length > 0) {
				for (L1NpcInstance npc : trapList) {
					if ((npc.getNpcId() == 100116 && !ad)
							|| (npc.getNpcId() == 100115 && ad)) {
						for (L1PcInstance pc : list) {
							if (npc.getX() == pc.getX()
									&& npc.getY() == pc.getY())
								count++;
							/** 운영자 테스트 용 **/
							if (pc.isGm()) {
								if (ad)
									trapCK = true;
								else
									shootTrapCK = true;
							}
						}
					}
				}
			}
			list = null;
		}
		if (ad && count >= 3)
			trapCK = true;
		else if (!ad && count >= 1)
			shootTrapCK = true;
	}

	class ShipMove implements Runnable {
		private int x, y;

		public ShipMove(int _x, int _y) {
			x = _x;
			y = _y;
		}

		@Override
		public void run() {
			while (true) {
				try {
					if (ship == null)
						return;
					else if (ship.getX() == x && ship.getY() == y)
						break;
					ship.setDirectionMove(ship.moveDirection(mapid, x, y));
					Thread.sleep(calcSleepTime(ship, ship.getPassispeed(),
							L1NpcInstance.MOVE_SPEED));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (x == 32797 && y == 32831 && ship != null)
				try {
					ship.deleteMe();
				} catch (Exception e) {
				}
		}
	}

	class EventNpcMove implements Runnable {

		private FastTable<L1NpcInstance> list;
		private int[] x = { 32787, 32791, 32803, 32783, 32792, 32790 };
		private int[] y = { 32807, 32799, 32807, 32807, 32810, 32808 };

		public EventNpcMove(int _npcid) {
			if (_npcid == 100159) {
				list = NavalWarfareSpawn.getInstance().spawnList(mapid, 80);
			} else if (_npcid == 100160) {
				list = NavalWarfareSpawn.getInstance().spawnList(mapid, 81);
			}
		}

		@Override
		public void run() {
			int count = 200;
			while (count-- > 0) {
				try {
					if (list == null || list.size() <= 0)
						break;
					for (int i = 0; i < list.size(); i++) {
						L1NpcInstance npc = list.get(i);
						if (npc._destroyed || npc.isDead())
							continue;
						npc.setDirectionMove(CharPosUtil.targetDirection(npc,
								x[i], y[i]));
						if (npc.getX() == x[i] && npc.getY() == y[i]) {
							if (i == 1 && npc.getX() == 32791
									&& npc.getY() == 32799) {
								x[i] = 32790;
								y[i] = 32813;
							} else if (i == 2 && npc.getX() == 32803
									&& npc.getY() == 32807) {
								x[i] = 32820;
								y[i] = 32806;
							} else if (i == 4 && npc.getX() == 32792
									&& npc.getY() == 32810) {
								x[i] = 32790;
								y[i] = 32818;
							} else if (i == 5 && npc.getX() == 32790
									&& npc.getY() == 32808) {
								x[i] = 32814;
								y[i] = 32807;
							} else {
								npc.deleteMe();
							}
						}
					}
					Thread.sleep(360);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			list = null;
			x = null;
			y = null;
		}
	}

	public void rankInsert() {
		Connection con = null;
		PreparedStatement pstm = null;
		int i = 0;
		L1PcInstance[] list = getMemberArray();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO navalwarfare_score SET score = ?, name1 = ?, name2 = ?, name3 = ?, name4=?, name5=?, name6=?, name7=?, name8=?");
			pstm.setInt(1, score);
			for (; i < list.length; i++) {
				pstm.setString(2 + i, list[i] != null ? list[i].getName() : "");
			}
			if (i < 8) {
				for (; i < 8; i++) {
					pstm.setString(2 + i, "");
				}
			}
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
			list = null;
		}
	}
}
