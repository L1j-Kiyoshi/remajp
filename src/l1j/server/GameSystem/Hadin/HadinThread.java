/**
 * 본섭 리뉴얼된 Chapter1. 비밀의 마법사 하딘
 * 메인 이벤트 처리 스레드
 * by. 케인
 */
package l1j.server.GameSystem.Hadin;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Door;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.Teleportation;

public class HadinThread extends Thread {

	private static final int lobby = 1;
	private static final int DUNGEON_1 = 2; // 첫번째 트랩 -> 두번째 몹 생성
	private static final int DUNGEON_2 = 3; // 3번째 트랩 발판 다 눌렸는지 체크 -> 몹 생성 시작
	private static final int DUNGEON_3 = 4; // 몹생성 -> 몹 다 잡으면 골방입구 문 오픈 & 몹생성
	private static final int DUNGEON_4 = 5; // 골방 몹 생성 -> 몹 다 잡으면 셸방 문 오픈 **/
	private static final int DUNGEON_5 = 6;
	private static final int DUNGEON_6 = 7;
	private static final int DUNGEON_ENDING = 8;
	private static final int DUNGEON_ENDING_BaphoDie = 9;
	private static final int DUNGEON_ENDING_KereDie = 10;
	private static final int DUNGEON_END = 11;
	private Random _rnd;
	private L1Location loc = new L1Location();
	private static int WHITE_MAGIC_ZONE = 5000080;

	/** 트랩 관련 **/
	private boolean LEVEL_1_TRAP_1, LEVEL_1_TRAP_2, LEVEL_1_TRAP_3,
			LEVEL_1_TRAP_4, LEVEL_2_TRAP_1, LEVEL_2_TRAP_2, LEVEL_2_TRAP_3,
			LEVEL_2_TRAP_4, LEVEL_3_TRAP_1, LEVEL_3_TRAP_2, LEVEL_3_TRAP_3,
			LEVEL_3_TRAP_4, /* LEVEL_4_TRAP, */TRAP_1, /* TRAP_2, */TRAP_3,
			LAST_TRAP_1, LAST_TRAP_2, LAST_TRAP_3, LAST_TRAP_4, LAST_TRAP_5,
			LAST_TRAP_6, LAST_TRAP_7, LAST_TRAP_8, LAST_TRAP;
	private int LAST_1, LAST_2, LAST_3, LAST_4, LAST_5, LAST_6, LAST_7,
			LAST_8 = 0;

	private final FastTable<HadinParty> party;
	private boolean stop;

	public HadinThread() {
		super("l1j.server.GameSystem.HadinThread");
		party = new FastTable<HadinParty>();
		get = this;
		_rnd = new Random();
		start();
		//System.out.println(":: HadinThread Loading Compleate");
	}

	private static HadinThread get;

	public static HadinThread get() {
		if (get == null)
			new HadinThread();
		return get;
	}

	private static Logger _log = Logger.getLogger(HadinThread.class.getName());

	public void run() {
		int size = 0;
		HadinParty[] list = null;
		while (true) {
			try {
				synchronized (party) {
					if ((size = party.size()) > 0)
						list = (HadinParty[]) party
								.toArray(new HadinParty[party.size()]);
				}
				if (size > 0 && list != null) {
					try {
						for (HadinParty p : list) {
							if (p == null) {
								party.remove(null);
								continue;
							}
							if (StepBase(p)) {
								LeaderStep(p);
								Step(p);
								TrapCheck(p);
								BowTrap(p);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
				list = null;
				sleep(500);
				if (size <= 0) {
					synchronized (this) {
						stop = true;
						this.wait();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		}
	}

	public void add(L1Party p, short mapid) {
		synchronized (party) {
			party.add(new HadinParty(p, mapid));
		}
		synchronized (get) {
			if (stop) {
				stop = false;
				get.notify();
			}
		}
	}

	private void remove(HadinParty p) {
		synchronized (party) {
			if (party.contains(p))
				party.remove(p);
			p.reset();
			p = null;
		}
	}

	private void quit(HadinParty p) {
		if (p == null)
			return;
		Object_Delete(p);
		Hadin.get().quit(L1WorldMap.getInstance().getMap(p.mapid));
		remove(p);
	}

	private boolean StepBase(HadinParty p) {
		boolean ck = true;

		if (p.UserCountCK) {
			int i = 0;
			for (L1PcInstance pc : p.getParty().getMembers()) {
				if (pc.getMapId() != p.mapid) {
					p.firtCkCount++;
					break;
				} else
					i++;
			}
			if (i >= p.getParty().getNumOfMembers()) {
				p.UserCountCK = false;
				p.firtCkCount = 0;
			} else {
				if (p.firtCkCount >= 10) {
					RETURN_HOME(p);
					p.UserCountCK = false;
				} else
					ck = false;
			}
		} else {
			boolean q = false;
			for (L1PcInstance pc : p.getParty().getMembers()) {
				if (pc == null)
					continue;
				if (p.getParty().getNumOfMembers() < 5
						|| pc.getMapId() != p.mapid) {
					Teleport(pc, 32574, 32942, (short) 0);
					try {
						if (pc.isInParty())
							p.getParty().leaveMember(pc);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (p.getParty().getNumOfMembers() < 4) {
					ck = false;
					q = true;
				}
			}
			if (q) {
				for (L1Object obj : L1World.getInstance()
						.getVisibleObjects(p.mapid).values()) {
					if (obj == null || !(obj instanceof L1PcInstance))
						continue;
					if (((L1PcInstance) obj).isGm())
						continue;
					if (!((L1PcInstance) obj).isInParty())
						Teleport((L1PcInstance) obj, 32574, 32942, (short) 0);
				}
				quit(p);
			}
		}
		for (L1Object obj : L1World.getInstance().getVisibleObjects(p.mapid)
				.values()) {
			if (obj == null || !(obj instanceof L1PcInstance))
				continue;
			if (((L1PcInstance) obj).isGm())
				continue;
			if (!((L1PcInstance) obj).isInParty())
				Teleport((L1PcInstance) obj, 32574, 32942, (short) 0);
		}

		if (!ck)
			return false;

		if (p.NpcList != null) {
			for (L1NpcInstance npc : p.NpcList) {
				if (npc == null || npc.isDead())
					p.NpcList.remove(npc);
			}
		}
		if (p.NpcList2 != null) {
			for (L1NpcInstance npc : p.NpcList2) {
				if (npc == null || npc.isDead())
					p.NpcList2.remove(npc);
			}
		}
		if (p.NpcList3 != null) {
			for (L1NpcInstance npc : p.NpcList3) {
				if (npc == null || npc.isDead())
					p.NpcList3.remove(npc);
			}
		}
		if (p.NpcList4 != null) {
			for (L1NpcInstance npc : p.NpcList4) {
				if (npc == null || npc.isDead())
					p.NpcList4.remove(npc);
			}
		}
		if (p.Leader_NpcList != null) {
			for (L1NpcInstance npc : p.Leader_NpcList) {
				if (npc == null || npc.isDead())
					p.Leader_NpcList.remove(npc);
			}
		}
		return ck;
	}

	private void LeaderStep(HadinParty p) {
		if (!p.leaderOn)
			return;
		if (p.leader_delayTime > System.currentTimeMillis())
			return;
		if (p.leader_step == 5 && !p.Leader_Chapter_3) {
			Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(p.Kere,
					"$7583", 0), true);
		} else
			LeaderChatSend(p);
		LeaderSleep_time_setting(p);
		switch (p.leader_step) {
		/** 케레니스 처음 대사 **/
		case 1:
			if (p.leader_sub_step == 5) {
				// if(p.getParty().getLeader().getClassId() !=
				// p.getParty().getLeader().getGfxId().getTempCharGfx())
				// L1PolyMorph.undoPoly(p.getParty().getLeader());
				return;
			} else if (p.leader_sub_step == 6) {
				L1SpawnUtil.spawn2(p.Kere.getX(), p.Kere.getY(),
						p.Kere.getMapId(), 5000049, 0, 0, 0);
				// BossRoomLast2(p);
			} else if (p.leader_sub_step == 7) {
				S_NPCTalkReturn ntr = new S_NPCTalkReturn(p.Kere.getId(),
						"j_ep003", null);
				p.getParty().getLeader().sendPackets(ntr);
				ntr = null;
				BonginSendPacket(p, 7615, "하딘");
			}
			p.leader_sub_step++;
			if (p.leader_sub_step == 9) {
				p.leader_step = 2;
				p.leader_sub_step = 0;
			}
			break;
		/** 오크 소환 부분 **/
		case 2:
			if (p.leader_sub_step == 1) {
				Teleportation.teleport(p.Npc_Orc, 32747, 32930, p.mapid, 5);
				Teleportation.teleport(p.Npc_OrcWarrior, 32745, 32933, p.mapid,
						5);
				Teleportation
						.teleport(p.Npc_OrcAcher, 32746, 32935, p.mapid, 5);
				Teleportation.teleport(p.Npc_Balsim, 32748, 32934, p.mapid, 5);
				S_NPCTalkReturn ntr = new S_NPCTalkReturn(p.Kere.getId(),
						"j_ep005", null);
				p.getParty().getLeader().sendPackets(ntr);
				ntr = null;
			}
			p.leader_sub_step++;
			if (p.leader_sub_step == 6) {
				p.Leader_NpcList = HadinSpawn.getInstance().fillSpawnTable(
						p.mapid, 9, true);
				p.leader_step = 3;
				p.leader_sub_step = 0;
			}
			break;
		/** 오크 이벤트 부분 **/
		case 3:
			if (p.Leader_NpcList.size() > 0)
				return;
			if (p.leader_sub_step == 1)
				p.Npc_OrcWarrior.setDirectionMove(7);
			else if (p.leader_sub_step == 2)
				p.Npc_OrcWarrior.setDirectionMove(7);
			else if (p.leader_sub_step == 3)
				p.Npc_OrcWarrior.setDirectionMove(0);
			else if (p.leader_sub_step == 6)
				p.Npc_OrcWarrior.setDirectionMove(3);
			else if (p.leader_sub_step == 7)
				p.Npc_OrcWarrior.setDirectionMove(3);
			else if (p.leader_sub_step == 9)
				p.Npc_OrcWarrior.setDirectionMove(3);
			else if (p.leader_sub_step == 11)
				p.Npc_OrcWarrior.setDirectionMove(3);
			else if (p.leader_sub_step == 12)
				p.Npc_OrcWarrior.setDirectionMove(3);
			else if (p.leader_sub_step == 13)
				p.Npc_OrcWarrior.deleteMe();
			else if (p.leader_sub_step == 19) {
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 9);
				p.Npc_Orc.setDirectionMove(4);
			} else if (p.leader_sub_step == 20)
				p.Npc_Orc.setDirectionMove(4);
			else if (p.leader_sub_step == 21)
				p.Npc_Orc.setDirectionMove(4);
			else if (p.leader_sub_step == 22)
				p.Npc_Orc.setDirectionMove(4);
			else if (p.leader_sub_step == 23)
				p.Npc_Orc.setDirectionMove(3);
			else if (p.leader_sub_step == 24)
				p.Npc_Orc.deleteMe();
			else if (p.leader_sub_step == 25)
				p.Npc_Orc.setDirectionMove(2);
			else if (p.leader_sub_step == 26)
				p.Npc_Orc.setDirectionMove(2);
			else if (p.leader_sub_step == 27)
				p.Npc_OrcAcher.deleteMe();
			else if (p.leader_sub_step == 30)
				p.Npc_Orc.setDirectionMove(4);
			else if (p.leader_sub_step == 31)
				p.Npc_Balsim.deleteMe();
			p.leader_sub_step++;
			if (p.leader_sub_step == 33) {
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 9);
				p.leader_step = 4;
				p.leader_sub_step = 0;
			}
			break;
		/** 2번째 트랩 가동 후 이벤트 **/
		case 4:
			if (p.Leader_Chapter_2) {
				if (p.leader_sub_step == 2) {
					S_NPCTalkReturn ntr = new S_NPCTalkReturn(p.Kere.getId(),
							"j_ep006", null);
					p.getParty().getLeader().sendPackets(ntr);
					ntr = null;
				} else if (p.leader_sub_step == 5)
					HadinSpawn.getInstance().fillSpawnTable(p.mapid, 9);
				p.leader_sub_step++;
				if (p.leader_sub_step == 8) {
					p.leader_step = 5;
					p.leader_sub_step = 0;
				}
			} else
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 9);
			break;
		/** 3번째 트랩 가동후 이벤트 **/
		case 5:
			if (p.Leader_Chapter_3) {
				if (p.leader_sub_step == 1) {
					S_NPCTalkReturn ntr = new S_NPCTalkReturn(p.Kere.getId(),
							"j_ep007", null);
					p.getParty().getLeader().sendPackets(ntr);
					ntr = null;
				} else if (p.leader_sub_step == 3)
					p.Leader_NpcList = HadinSpawn.getInstance().fillSpawnTable(
							p.mapid, 9, false);

				if (p.leader_sub_step == 4) {
					p.leader_step = 6;
					p.leader_sub_step = 0;
				} else
					p.leader_sub_step++;
			} else {
				Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(p.Kere,
						"$7583", 0), true);
			}
			break;
		/** 보스방 문열렸을때 이벤트 **/
		case 6:
			if (p.leader_bossroom_sub_step > 0) {
				if (p.leader_bossroom_sub_step == 3) {
					Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(
							p.Kere, "$7625", 0), true);
				} else if (p.leader_bossroom_sub_step == 2) {
					Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(
							p.Kere, "$7626", 0), true);
				} else if (p.leader_bossroom_sub_step == 1) {
					Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(
							p.Kere, "$7628", 0), true);
				}
				p.leader_bossroom_sub_step--;
				return;
			}
			if (p.Leader_Chapter_4) {
				if (p.Leader_NpcList != null && p.Leader_NpcList.size() > 0)
					return;
				else {
				}
				if (p.leader_sub_step == 1) {
					p.getParty()
							.getLeader()
							.sendPackets(
									new S_NPCTalkReturn(p.Kere.getId(),
											"j_ep008", null), true);
				}

				if (p.leader_sub_step == 5) {
					Door(p, "해골문 13");
					Door(p, "해골문 14");
					L1Teleport.teleport(p.getParty().getLeader(), 32719, 32848,
							p.mapid, 3, true); // 보스방 좌표로 이동
				}

				p.leader_sub_step++;
				if (p.leader_sub_step == 7) {
					BonginSendPacket(p, 7647, "하딘");
					p.leader_step = 7;
					p.leader_sub_step = 0;
				}
			} else
				p.leader_bossroom_sub_step = 4;
			break;
		case 7:
			p.leaderOn = false;
			break;
		default:
			break;
		}
	}

	private void Step(HadinParty p) {
		if (p.delayTime > System.currentTimeMillis())
			return;
		ChatSend(p);
		Sleep_time_setting(p);
		switch (p.step) {
		/** 하딘 로비 **/
		case lobby:
			if (p.sub_step == 2) {
				BonginSendPacket(p, 8693, "하딘");
				Teleportation
						.teleport(p.Hadin_Effect, 32716, 32846, p.mapid, 6);
				/** 최종 점검 **/
			} else if (p.sub_step == 5) {
				// if(p.getParty().getLeader().getClassId() !=
				// p.getParty().getLeader().getGfxId().getTempCharGfx())
				// L1PolyMorph.undoPoly(p.getParty().getLeader());
				return;
			} else if (p.sub_step == 12)
				BonginSendPacket(p, 8702, "하딘");
			else if (p.sub_step == 14 || p.sub_step == 15) {
				// if(p.getParty().getLeader().getClassId() !=
				// p.getParty().getLeader().getGfxId().getTempCharGfx())
				// L1PolyMorph.undoPoly(p.getParty().getLeader());
			} else if (p.sub_step == 17) {
				Teleport(p.getParty().getLeader(), 32730, 32921, p.mapid);
				p.leaderOn = true;
			} else if (p.sub_step == 19) {
				p.TrapCkOn = true;
				L1SpawnUtil.spawn2(32727, 32724, p.mapid, WHITE_MAGIC_ZONE, 0,
						0, 0);
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 1, true);
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 2);
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 3);
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 4);
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 5);
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 23, false);
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 24);
			} else if (p.sub_step == 25) {
				Teleportation.teleport(p.Npc_Hadin, 32716, 32846, p.mapid, 6);
				p.step = 2;
				p.sub_step = 0;
				return;
			}
			p.sub_step++;
			break;
		/** 첫번째 트랩 -> 두번째 몹 생성 **/
		case DUNGEON_1:
			if (p.sub_step == 0)
				BonginSendPacket(p, 7611, "하딘");
			else if (p.sub_step == 1) {
				BonginSendPacket(p, 7613, "하딘");
			} else if (p.sub_step == 3) {
				Door(p, "해골문 1");
				Effect_EQ(p, false);
				p.step = 3;
				p.sub_step = 0;
				return;
			}
			p.sub_step++;
			break;
		/** 3번째 트랩 발판 다 눌렸는지 체크 -> 몹 생성 시작 **/
		case DUNGEON_2:
			if (p.LEVEL_3_TRAP_CK) {
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 6, true);
				p.Leader_Chapter_3 = true;
				p.step = 4;
			}
			break;
		/** 몹생성 -> 몹 다 잡으면 골방입구 문 오픈 & 몹생성 **/
		case DUNGEON_3:
			Door(p, "해골문 12");
			p.sub_step = 0;
			HadinSpawn.getInstance().fillSpawnTable(p.mapid, 7, true);
			p.step = 5;
			break;
		/** 골방 몹 생성 -> 몹 다 잡으면 셸방 문 오픈 **/
		case DUNGEON_4:
			p.Leader_Chapter_4 = true;
			p.sub_step = 0;
			p.step = 6;
			break;
		/** 트랩으로 보스방 문 오픈 체크해서 일정시간후 닫히게 설정 **/
		case DUNGEON_5:
			if (p.BossRoomCK /* && !p.leaderOn */) {
				if (p.sub_step == 1) {
					Door(p, "해골문 14", false);
					Effect_EQ(p, true);
					BonginSendPacket(p, 7650, "하딘");
					p.ArrowTrap = true;
				} else if (p.sub_step == 2) {
					BonginSendPacket(p, 7651, "하딘");
					p.step = 7;
					p.sub_step = 10;
					return;
				}
				p.sub_step++;
			}
			break;
		/** 보스방 이벤트 로직 설정 **/
		case DUNGEON_6:
			if (p.ssub_step == 0
					&& ((p.NpcList != null && p.NpcList.size() > 0)
							|| (p.NpcList2 != null && p.NpcList2.size() > 0)
							|| (p.NpcList3 != null && p.NpcList3.size() > 0) || (p.NpcList4 != null && p.NpcList4
							.size() > 0)))
				return;
			if (p.ssub_step == 0) {
				p.ssub_step++;
				return;
			}
			if (p.ssub_step == 1) {
				if (p.sub_step == 21) {
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "$7654", 0), true);
					BonginSendPacket(p, 7654, "하딘");
				} else if (p.sub_step == 17) {
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "$7653", 0), true);
					BonginSendPacket(p, 7653, "하딘");
				} else {
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "$7652", 0), true);
					BonginSendPacket(p, 7652, "하딘");
				}
				Effect_P(p);
				p.ssub_step++;
				return;
			}

			if (p.ssub_step == 2) {
				BonginNotice(p);
				p.ssub_step++;
				return;
			}
			if (p.ssub_step == 3) {
				BonginSendPacket(p, 8689, "하딘");
				p.NpcList = HadinSpawn.getInstance().fillSpawnTable(32710,
						32836, p.mapid, p.sub_step);
			}

			if (p.sub_step == 17) {
				if (p.ssub_step == 3)
					p.NpcList.add(L1SpawnUtil.spawn2(32707, 32846, p.mapid,
							5000073, 0, 0, 0));
			} else if (p.sub_step == 13) {
				if (p.ssub_step == 3)
					p.NpcList.add(L1SpawnUtil.spawn2(32707, 32846, p.mapid,
							5000066, 0, 0, 0));
			}
			if (p.ssub_step == 3) {
				p.ssub_step++;
				return;
			}

			if (p.ssub_step == 4) {
				BonginSendPacket(p, 8690);
				p.NpcList2 = HadinSpawn.getInstance().fillSpawnTable(32697,
						32841, p.mapid, p.sub_step);
				p.ssub_step++;
				return;
			}
			if (p.ssub_step == 5) {
				BonginSendPacket(p, 8691);
				p.NpcList3 = HadinSpawn.getInstance().fillSpawnTable(32707,
						32859, p.mapid, p.sub_step);
				p.ssub_step++;
				return;
			}
			if (p.ssub_step == 6) {
				BonginSendPacket(p, 8692);
				int rnd = _rnd.nextInt(100);
				if (rnd < 3) {
					p.NpcList4 = HadinSpawn.getInstance().fillSpawnTable(32707,
							32846, p.mapid, 23);
					Effect_P(p);
					Effect_EQ(p, true);
					S_PacketBox pb = new S_PacketBox(84, "하딘 : W I T H !!!");
					for (L1PcInstance pc : p.getParty().getMembers()) {
						pc.sendPackets(pb);
					}
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "W I T H !!!", 0),
							true);
				} else
					p.NpcList4 = HadinSpawn.getInstance().fillSpawnTable(32707,
							32846, p.mapid, p.sub_step);
				p.ssub_step++;
				return;
			} else {
				if (p.sub_step == 21) {
					if (p.ssub_step == 7) {
						p.Baphomet = L1SpawnUtil.spawn2(32707, 32846, p.mapid,
								5000076, 0, 0, 0);
						p.Baphomet.getMoveState().setHeading(2);
						p.Baphomet.setParalyzed(true);
						Teleportation
								.teleport(p.Kere, 32708, 32846, p.mapid, 6);
						p.vsThread = new Hadin_KerevsBapho(p.Kere, p.Baphomet);
						Broadcaster.broadcastPacket(p.Kere,
								new S_NpcChatPacket(p.Kere, "$7820", 0), true);
					} else if (p.ssub_step == 8) {
						Broadcaster.broadcastPacket(p.Npc_Hadin,
								new S_NpcChatPacket(p.Npc_Hadin, "$7821", 0),
								true);
					} else if (p.ssub_step == 9) {
						Broadcaster.broadcastPacket(p.Kere,
								new S_NpcChatPacket(p.Kere, "$7822", 0), true);
					} else if (p.ssub_step == 10) {
						Broadcaster.broadcastPacket(p.Kere,
								new S_NpcChatPacket(p.Kere, "$7823", 0), true);
					} else if (p.ssub_step == 11) {
						Broadcaster.broadcastPacket(p.Npc_Hadin,
								new S_NpcChatPacket(p.Npc_Hadin, "$7824", 0),
								true);
					} else {
						p.step = 8;
						p.sub_step = 0;
						return;
					}
					p.ssub_step++;
					return;
				}
			}
			p.sub_step++;
			p.ssub_step = 0;
			if (p.sub_step == 22) {
				p.step = 8;
				p.sub_step = 0;
			}
			break;
		/** 케레니스 or 바포 죽은 이후 이벤트 **/
		case DUNGEON_ENDING:
			if (p.Baphomet == null || p.Baphomet.isDead()) {
				p.Baphomet.setParalyzed(false);
				p.vsThread.on = false;
				if (p.sub_step == 0) {
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "$7827", 0), true);
				} else if (p.sub_step == 1) {
					Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(
							p.Kere, "$7828", 0), true);
				} else if (p.sub_step == 2) {
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "$7829", 0), true);
				} else if (p.sub_step == 3) {
					Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(
							p.Kere, "$7830", 0), true);
				} else if (p.sub_step == 4) {
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "$7831", 0), true);
				} else if (p.sub_step == 5) {
					Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(
							p.Kere, "$7832", 0), true);
				}
				if (p.sub_step <= 5) {
					p.sub_step++;
					return;
				}
				int x = p.Kere.getX();
				int y = p.Kere.getY();
				byte heading = (byte) p.Kere.getMoveState().getHeading();
				p.Kere.deleteMe();
				p.Kere = L1SpawnUtil.spawn2(x, y, p.mapid, 5000090, 0, 0, 0);
				p.Kere.getMoveState().setHeading(heading);
				p.step = 9;
				p.sub_step = 0;
			} else if (p.Kere == null || p.Kere.isDead()) {
				p.vsThread.on = false;
				p.Baphomet.setParalyzed(false);
				if (p.sub_step == 0) {
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "$7833", 0), true);
				} else if (p.sub_step == 1) {
					Broadcaster.broadcastPacket(p.Npc_Hadin,
							new S_NpcChatPacket(p.Npc_Hadin, "$7834", 0), true);
				}

				if (p.sub_step <= 1) {
					p.sub_step++;
					return;
				}
				p.step = 10;
				p.sub_step = 0;
			}
			break;
		/** 케레 죽은 이후 이벤트 **/
		case DUNGEON_ENDING_BaphoDie:
			// if((p.NpcList != null && p.NpcList.size() > 0) || (p.NpcList2 !=
			// null && p.NpcList2.size() > 0) || (p.NpcList3 != null &&
			// p.NpcList3.size() > 0) || (p.NpcList4 != null &&
			// p.NpcList4.size() > 0)) return;
			if (p.Kere == null || p.Kere.isDead())
				BossRoomLast(p);
			break;
		/** 바포 죽은 이후 이벤트 **/
		case DUNGEON_ENDING_KereDie:
			// if((p.NpcList != null && p.NpcList.size() > 0) || (p.NpcList2 !=
			// null && p.NpcList2.size() > 0) || (p.NpcList3 != null &&
			// p.NpcList3.size() > 0) || (p.NpcList4 != null &&
			// p.NpcList4.size() > 0)) return;
			if (p.Baphomet == null || p.Baphomet.isDead())
				BossRoomLast(p);
			break;
		/** 보상방 이벤트 이후 **/
		case DUNGEON_END:
			if (p.sub_step == 0) {
				p.sub_step++;
				return;
			}
			quit(p);
			RETURN_HOME(p);
			break;
		default:
			break;
		}
	}

	private void TrapCheck(HadinParty p) {
		if (!p.TrapCkOn)
			return;
		TRAP_RESET();
		for (L1PcInstance pc : p.getParty().getMembers()) {
			if (pc != null)
				TRAP_ON(pc, p);
		}
		TRAP_ON_CK(p);
	}

	private void Sleep_time_setting(HadinParty p) {
		int time = 2000;
		switch (p.step) {
		case lobby:
			if (p.sub_step == 1 || p.sub_step == 2 || p.sub_step == 3
					|| p.sub_step == 13 || p.sub_step == 14 || p.sub_step == 17)
				time = 6000;
			break;
		case DUNGEON_1:
			if (p.sub_step == 2)
				time = 4000;
			break;
		case DUNGEON_2:
			break;
		case DUNGEON_3:
			break;
		case DUNGEON_4:
			if (p.sub_step > 2)
				time = 3000;
			break;
		case DUNGEON_5:
			if (p.sub_step == 0)
				time = 1000;// 60000
			else if (p.sub_step == 1)
				time = 5000;
			break;
		case DUNGEON_6:
			if (p.ssub_step == 0)
				time = 2000;
			else if (p.ssub_step == 1)
				time = 8000;
			else if (p.sub_step == 21
					&& (p.ssub_step == 3 || p.ssub_step == 4
							|| p.ssub_step == 5 || p.ssub_step == 6))
				time = 1000;
			else if (p.ssub_step == 3 || p.ssub_step == 4 || p.ssub_step == 5)
				time = 9000;
			else if (p.ssub_step == 7)
				time = 5000;
			break;
		case DUNGEON_ENDING:
			time = 1000;
			break;
		case DUNGEON_ENDING_KereDie:
		case DUNGEON_ENDING_BaphoDie:
			if (p.sub_step == 8)
				time = 1000;
			else if (p.sub_step == 9)
				time = 20000;
			break;
		case DUNGEON_END:
			if (p.sub_step == 0)
				time = 180000;
			break;
		default:
			break;
		}
		p.delayTime = System.currentTimeMillis() + time;
	}

	private void LeaderSleep_time_setting(HadinParty p) {
		int time = 1000;
		switch (p.leader_step) {
		case 1:
			time = 5000;
			break;
		case 2:
			if (p.leader_sub_step == 2 || p.leader_sub_step == 3)
				time = 4000;
			else
				time = 5000;
			break;
		case 3:
			if (p.leader_sub_step == 4 || p.leader_sub_step == 7
					|| (p.leader_sub_step >= 12 && p.leader_sub_step <= 17)
					|| p.leader_sub_step == 27 || p.leader_sub_step == 28
					|| p.leader_sub_step == 31)
				time = 4000;
			else if ((p.leader_sub_step >= 3 && p.leader_sub_step <= 11)
					|| p.leader_sub_step == 30 || p.leader_sub_step == 32)
				time = 2000;
			else if (p.leader_sub_step == 18)
				time = 3000;
			else if (p.leader_sub_step == 24)
				time = 5000;
			else if (p.leader_sub_step == 29)
				time = 20000;
			break;
		case 4:
			if (p.Leader_Chapter_2) {
				if (p.leader_sub_step == 0 || p.leader_sub_step == 5
						|| p.leader_sub_step == 6)
					time = 5000;
				else
					time = 3000;
			} else
				time = 15000;
			break;
		case 5:
			if (p.Leader_Chapter_3) {
				if (p.leader_sub_step == 3)
					time = 10000;
				else
					time = 5000;
			} else
				time = 10000;
			break;
		case 6:
			if (p.leader_bossroom_sub_step == 4)
				time = 5000;
			else if (p.leader_bossroom_sub_step == 3
					|| p.leader_bossroom_sub_step == 2)
				time = 7000;
			else if (p.leader_bossroom_sub_step == 1)
				time = 1000;
			else if (p.Leader_Chapter_4) {
				if (p.leader_sub_step == 0)
					time = 4000;
				else if (p.leader_sub_step == 4)
					time = 2000;
				else if (p.leader_sub_step == 5)
					time = 1000;
				else
					time = 5000;
			}
			break;
		default:
			break;
		}
		p.leader_delayTime = System.currentTimeMillis() + time;
	}

	private void RETURN_HOME(HadinParty p) {
		for (L1PcInstance pc : p.getParty().getMembers()) {
			if (pc != null)
				Teleport(pc, 32572, 32944, (short) 0);
		}
	}

	private void Object_Delete(HadinParty p) {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(p.mapid)
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
			if (obj.getMapId() != p.mapid)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(
					obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}

	private void BonginSendPacket(HadinParty p, int count) {
		BonginSendPacket(p, count, null);
	}

	private void BonginSendPacket(HadinParty p, int count, String name) {
		S_PacketBox pb = new S_PacketBox(84, (name == null ? "" : name + " : ")
				+ "$" + count);
		for (L1PcInstance pc : p.getParty().getMembers()) {
			if (pc != null)
				pc.sendPackets(pb);
		}
	}

	private void 보스방후문패킷전송(HadinParty p, L1DoorInstance door) {
		S_Door packet = new S_Door(door);
		for (L1PcInstance pc : p.getParty().getMembers()) {
			if (pc != null)
				pc.sendPackets(packet);
		}
	}

	private static String[][] Chat = {
			{},
			{ null, "$7598", "$8693", "$8695", "$8694", null, "$8696", "$8697",
					"$8698", "$8699", "$8700", "$8701", "$8702", "$7599",
					"$7601", "$7602", "$7603", "$7604", "$7605", "$7606" }, {},
			{}, {}, {}, { null, "$7650", "$7651" }, {}, {},
			{ null, "$7835", "$7836", "$7837", "$7838", "$7839", "$7840" },
			{ null, "$7835", "$7836", "$7837", "$7838", "$7839", "$7840" } };
	private static String[][] LeaderChat = {
			{},
			{ null, "$7607", "$7608", "$7609", "$7610", "$7612", "$7614",
					"$7616", "$7581" },
			{ null, "$7623", "$7848", "$7842", "$7854", "$7624" }, // 7
			{ null, "$7849", null, null, "$7855", "$7856", null, "$7857",
					"$7843", null, "$7858", null, "$7844", "$7859", "$7578",
					"$7579", "$7845", "$7851", "$7851", "$7852", null, null,
					null, null, "$7853", "$7846", null, null, "$7578", "$7579",
					"$7847", null, "$7585" },
			{ null, "$7629", "$7630", "$7631", "$7584", null, "$7580", "$7636" },
			{ null, "$7637", "$7638", "$7639", "$7582" },
			{ null, "$7643", "$7644", "$7645", "$7646" } };

	private void ChatSend(HadinParty p) {
		String chat = null;
		try {
			chat = Chat[p.step][p.sub_step];
		} catch (Exception e) {
		}
		if (chat != null) {
			if (p.step == 8
					&& (p.sub_step == 1 || p.sub_step == 3 || p.sub_step == 5)) {
				Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(p.Kere,
						chat, 0), true);
			} else {
				Broadcaster.broadcastPacket(p.Npc_Hadin, new S_NpcChatPacket(
						p.Npc_Hadin, chat, 0), true);
			}
		}
	}

	private void LeaderChatSend(HadinParty p) {
		String chat = null;
		try {
			chat = LeaderChat[p.leader_step][p.leader_sub_step];
		} catch (Exception e) {
		}
		if (chat != null) {
			if ((p.leader_step == 2 && p.leader_sub_step == 2)
					|| (p.leader_step == 3 && (p.leader_sub_step == 1 || p.leader_sub_step == 18))) {
				Broadcaster.broadcastPacket(p.Npc_Orc, new S_NpcChatPacket(
						p.Npc_Orc, chat, 0), true);
			} else if ((p.leader_step == 2 && p.leader_sub_step == 3)
					|| (p.leader_step == 3 && (p.leader_sub_step == 8
							|| p.leader_sub_step == 12
							|| p.leader_sub_step == 16
							|| p.leader_sub_step == 25 || p.leader_sub_step == 30))) {
				Broadcaster.broadcastPacket(p.Npc_Balsim, new S_NpcChatPacket(
						p.Npc_Balsim, chat, 0), true);
			} else if ((p.leader_step == 2 && p.leader_sub_step == 4)
					|| (p.leader_step == 3 && (p.leader_sub_step >= 4 && p.leader_sub_step <= 7))
					|| (p.leader_step == 3 && (p.leader_sub_step == 10 || p.leader_sub_step == 13))) {
				Broadcaster.broadcastPacket(p.Npc_OrcWarrior,
						new S_NpcChatPacket(p.Npc_OrcWarrior, chat, 0), true);
			} else if ((p.leader_step == 3 && (p.leader_sub_step == 17
					|| p.leader_sub_step == 19 || p.leader_sub_step == 24))) {
				Broadcaster.broadcastPacket(p.Npc_OrcAcher,
						new S_NpcChatPacket(p.Npc_OrcAcher, chat, 0), true);
			} else {
				Broadcaster.broadcastPacket(p.Kere, new S_NpcChatPacket(p.Kere,
						chat, 0), true);
			}
		}
	}

	private void Teleport(L1PcInstance pc, int newX, int newY, short mapId) {
		// L1Teleport.teleport(pc, newX, newY, )
		pc.dx = newX;
		pc.dy = newY;
		pc.dm = mapId;
		pc.dh = pc.getMoveState().getHeading();
		pc.setTelType(7);
		S_SabuTell st = new S_SabuTell(pc);
		pc.sendPackets(st, true);
	}

	private void TRAP_ON(L1PcInstance pc, HadinParty p) {
		if (pc.getX() == 32727 && pc.getY() == 32724)
			Teleport(pc, 32665, 32793, pc.getMapId());
		else if (!p.BossRoomCK
				&& (p.partybossroomcount() >= (p.partymembercount() - 1))) {
			p.BossRoomCK = true;
		} else if (p.LEVEL_3_TRAP_CK) {
			if (pc.getX() == 32808 && pc.getY() == 32838)
				Teleport(pc, 32785, 32821, p.mapid);
		}
		/** 운영자 개인 테스트용 **/
		if (pc.isGm()) {
			if (pc.getX() == 32666 && pc.getY() == 32817) {
				LEVEL_1_TRAP_1 = true;
				LEVEL_1_TRAP_2 = true;
				LEVEL_1_TRAP_3 = true;
				LEVEL_1_TRAP_4 = true;
			} else if (pc.getX() == 32684 && pc.getY() == 32816)
				TRAP_1 = true;
			else if (pc.getX() == 32703 && pc.getY() == 32800)
				LEVEL_2_TRAP_1 = true;
			else if (pc.getX() == 32703 && pc.getY() == 32791)
				LEVEL_2_TRAP_2 = true;
			else if (pc.getX() == 32710 && pc.getY() == 32803)
				LEVEL_2_TRAP_3 = true;
			else if (pc.getX() == 32712 && pc.getY() == 32793)
				LEVEL_2_TRAP_4 = true;
			else if (pc.getX() == 32732 && pc.getY() == 32789) {
				LEVEL_2_TRAP_1 = true;
				LEVEL_2_TRAP_2 = true;
				LEVEL_2_TRAP_3 = true;
				LEVEL_2_TRAP_4 = true;
				// TRAP_2 = true;
			} else if (pc.getX() == 32760 && pc.getY() == 32791) // 활 트랩
				TRAP_3 = true;
			else if (pc.getX() == 32807 && pc.getY() == 32837) {
				LEVEL_3_TRAP_1 = true;
				LEVEL_3_TRAP_2 = true;
				LEVEL_3_TRAP_3 = true;
				LEVEL_3_TRAP_4 = true;
			} else if (pc.getX() == 32729 && pc.getY() == 32854) {
				// LEVEL_4_TRAP = true;
			} else if (pc.getX() == 32798 && pc.getY() == 32866)
				LAST_TRAP_1 = true;
			else if (pc.getX() == 32801 && pc.getY() == 32864)
				LAST_TRAP_2 = true;
			else if (pc.getX() == 32805 && pc.getY() == 32864)
				LAST_TRAP_3 = true;
			else if (pc.getX() == 32798 && pc.getY() == 32872)
				LAST_TRAP_4 = true;
			else if (pc.getX() == 32800 && pc.getY() == 32874)
				LAST_TRAP_5 = true;
			else if (pc.getX() == 32804 && pc.getY() == 32874)
				LAST_TRAP_6 = true;
			else if (pc.getX() == 32805 && pc.getY() == 32871)
				LAST_TRAP_7 = true;
			else if (pc.getX() == 32798 && pc.getY() == 32868)
				LAST_TRAP_8 = true;
			else if (pc.getX() == 32802 && pc.getY() == 32868)
				LAST_TRAP = true;
			return;
		}
		if (pc.getX() == 32666 && pc.getY() == 32817)
			LEVEL_1_TRAP_1 = true;
		else if (pc.getX() == 32668 && pc.getY() == 32817)
			LEVEL_1_TRAP_2 = true;
		else if (pc.getX() == 32666 && pc.getY() == 32819)
			LEVEL_1_TRAP_3 = true;
		else if (pc.getX() == 32668 && pc.getY() == 32819)
			LEVEL_1_TRAP_4 = true;
		else if (pc.getX() == 32684 && pc.getY() == 32816)
			TRAP_1 = true;
		else if (pc.getX() == 32703 && pc.getY() == 32800)
			LEVEL_2_TRAP_1 = true;
		else if (pc.getX() == 32703 && pc.getY() == 32791)
			LEVEL_2_TRAP_2 = true;
		else if (pc.getX() == 32710 && pc.getY() == 32803)
			LEVEL_2_TRAP_3 = true;
		else if (pc.getX() == 32712 && pc.getY() == 32793)
			LEVEL_2_TRAP_4 = true;
		else if (pc.getX() == 32732 && pc.getY() == 32789) {
			// TRAP_2 = true;//히든 트랩?
		} else if (pc.getX() == 32760 && pc.getY() == 32791) // 활 트랩
			TRAP_3 = true;
		else if (pc.getX() == 32807 && pc.getY() == 32837)
			LEVEL_3_TRAP_1 = true;
		else if (pc.getX() == 32809 && pc.getY() == 32837)
			LEVEL_3_TRAP_2 = true;
		else if (pc.getX() == 32807 && pc.getY() == 32839)
			LEVEL_3_TRAP_3 = true;
		else if (pc.getX() == 32809 && pc.getY() == 32839)
			LEVEL_3_TRAP_4 = true;
		else if (pc.getX() == 32729 && pc.getY() == 32854) {
			// LEVEL_4_TRAP = true; // 히든 트랩??
		} else if (pc.getX() == 32798 && pc.getY() == 32866)
			LAST_TRAP_1 = true;
		else if (pc.getX() == 32801 && pc.getY() == 32864)
			LAST_TRAP_2 = true;
		else if (pc.getX() == 32805 && pc.getY() == 32864)
			LAST_TRAP_3 = true;
		else if (pc.getX() == 32798 && pc.getY() == 32872)
			LAST_TRAP_4 = true;
		else if (pc.getX() == 32800 && pc.getY() == 32874)
			LAST_TRAP_5 = true;
		else if (pc.getX() == 32804 && pc.getY() == 32874)
			LAST_TRAP_6 = true;
		else if (pc.getX() == 32805 && pc.getY() == 32871)
			LAST_TRAP_7 = true;
		else if (pc.getX() == 32798 && pc.getY() == 32868)
			LAST_TRAP_8 = true;
		else if (pc.getX() == 32802 && pc.getY() == 32868)
			LAST_TRAP = true;
	}

	private void TRAP_RESET() {
		LEVEL_1_TRAP_1 = false;
		LEVEL_1_TRAP_2 = false;
		LEVEL_1_TRAP_3 = false;
		LEVEL_1_TRAP_4 = false;
		LEVEL_2_TRAP_1 = false;
		LEVEL_2_TRAP_2 = false;
		LEVEL_2_TRAP_3 = false;
		LEVEL_2_TRAP_4 = false;
		LEVEL_3_TRAP_1 = false;
		LEVEL_3_TRAP_2 = false;
		LEVEL_3_TRAP_3 = false;
		LEVEL_3_TRAP_4 = false;
		TRAP_1 = false; /* TRAP_2 = false; */
		TRAP_3 = false;
		LAST_TRAP_1 = false;
		LAST_TRAP_2 = false;
		LAST_TRAP_3 = false;
		LAST_TRAP_4 = false;
		LAST_TRAP_5 = false;
		LAST_TRAP_6 = false;
		LAST_TRAP_7 = false;
		LAST_TRAP_8 = false;
		LAST_TRAP = false;
		LAST_1 = 0;
		LAST_2 = 0;
		LAST_3 = 0;
		LAST_4 = 0;
		LAST_5 = 0;
		LAST_6 = 0;
		LAST_7 = 0;
		LAST_8 = 0;
	}

	private void TRAP_ON_CK(HadinParty p) {
		if (LEVEL_1_TRAP_1 && LEVEL_1_TRAP_2 && LEVEL_1_TRAP_3
				&& LEVEL_1_TRAP_4) {
			if (!p.LEVEL_1_TRAP_CK) {
				p.LEVEL_1_TRAP_CK = true;
				Door(p, "해골문 2");
				L1SpawnUtil.spawn2(32743, 32930, p.mapid, 5000050, 0, 0, 0);
				L1SpawnUtil.spawn2(32667, 32818, p.mapid, WHITE_MAGIC_ZONE, 0,
						0, 0);
				L1SpawnUtil.spawn2(32684, 32816, p.mapid, 5000093, 0, 0, 0);
				BonginSendPacket(p, 7621, "하딘");
				Door(p, "해골문 4"); // 추가
				Door(p, "해골문 5");
				Door(p, "해골문 6");
				Door(p, "해골문 8");
			}
		} else if (LEVEL_2_TRAP_1 && LEVEL_2_TRAP_2 && LEVEL_2_TRAP_3
				&& LEVEL_2_TRAP_4) {
			if (!p.Leader_Chapter_2) {
				p.Leader_Chapter_2 = true;
				L1SpawnUtil.spawn2(32741, 32928, p.mapid, 5000050, 0, 0, 0);
				BonginSendPacket(p, 7628, "하딘");
				Door(p, "해골문 7");
				Door(p, "해골문 9");
				Door(p, "해골문 10");
				Door(p, "해골문 11");
				HadinSpawn.getInstance().fillSpawnTable2(p.mapid, 42);
				HadinSpawn.getInstance().fillSpawnTable(p.mapid, 25);
				Effect_EQ(p, false);
			}
		} else if (LEVEL_3_TRAP_1 && LEVEL_3_TRAP_2 && LEVEL_3_TRAP_3
				&& LEVEL_3_TRAP_4) {
			if (p.Leader_Chapter_2 && !p.LEVEL_3_TRAP_CK) {
				p.LEVEL_3_TRAP_CK = true;
				L1SpawnUtil.spawn2(32808, 32838, p.mapid, WHITE_MAGIC_ZONE, 0,
						0, 0); // 마법진
				L1SpawnUtil.spawn2(32743, 32928, p.mapid, 5000050, 0, 0, 0);
				BonginSendPacket(p, 7635, "하딘");
			}
		}

		if (TRAP_1) {
			Door(p, "해골문 4");
		} else if (TRAP_3) {
			p.ArrowTrap = true;
		} else if (!p.LAST_TRAP_CK) {
			if (LAST_TRAP_1)
				LAST_1 = 1;
			if (LAST_TRAP_2)
				LAST_2 = 1;
			if (LAST_TRAP_3)
				LAST_3 = 1;
			if (LAST_TRAP_4)
				LAST_4 = 1;
			if (LAST_TRAP_5)
				LAST_5 = 1;
			if (LAST_TRAP_6)
				LAST_6 = 1;
			if (LAST_TRAP_7)
				LAST_7 = 1;
			if (LAST_TRAP_8)
				LAST_8 = 1;
		} else if (LAST_TRAP && p.LAST_TRAP_CK) {
			for (L1PcInstance pc : p.getParty().getMembers()) {
				if (pc != null) {
					// pc.sendPackets(new S_ServerMessage(403, "$9355 (3)"));
					// //7
					// pc.getInventory().storeItem(500021, 3);
					L1Inventory inv = L1World.getInstance().getInventory(
							pc.getX(), pc.getY(), pc.getMapId());
					L1ItemInstance item = ItemTable.getInstance().createItem(
							500021);
					if (pc.getX() == 32802 && pc.getY() == 32868)
						item.setCount(3);
					inv.storeTradeItem(item);
				}
			}
			p.TrapCkOn = false;
			BonginSendPacket(p, 8719);
			return;
		}

		if (p.getParty().getNumOfMembers() > 0
				&& p.getParty().getNumOfMembers() <= (LAST_1 + LAST_2 + LAST_3
						+ LAST_4 + LAST_5 + LAST_6 + LAST_7 + LAST_8)) {
			S_ServerMessage sm = new S_ServerMessage(8719);
			for (L1PcInstance pc : p.getParty().getMembers()) {
				if (pc != null)
					pc.sendPackets(sm);
			}
			sm = null;
			L1SpawnUtil.spawn2(32802, 32868, p.mapid, 5000078, 0, 0, 0);
			p.LAST_TRAP_CK = true;
		}
	}

	private void Door(HadinParty p, String name) {
		Door(p, name, true);
	}

	private void Door(HadinParty p, String name, boolean open) {
		for (L1NpcInstance npc : p.BasicNpcList) {
			if (npc == null || !(npc instanceof L1DoorInstance))
				continue;
			if (npc.getNpcTemplate().get_npcId() == 5000044
					|| npc.getNpcTemplate().get_npcId() == 5000045) {
				if (npc.getSpawnLocation().equals(name)) {
					if (open)
						((L1DoorInstance) npc).open();
					else {
						if (((L1DoorInstance) npc).getOpenStatus() == ActionCodes.ACTION_Open)
							((L1DoorInstance) npc).close();
					}
					break;
				}
			}
		}
	}

	private void Effect_EQ(HadinParty p, boolean leader) {
		S_PacketBox pb = new S_PacketBox(83, 2);
		for (L1PcInstance pc : p.getParty().getMembers()) {
			if (!leader && pc == p.getParty().getLeader())
				continue;
			pc.sendPackets(pb);
		}
	}

	private void Effect_P(HadinParty p) {
		S_Sound s = new S_Sound(184);
		S_PacketBox pb = new S_PacketBox(83, 1);
		for (L1PcInstance pc : p.getParty().getMembers()) {
			pc.sendPackets(s);
			pc.sendPackets(pb);
		}
	}

	String[] BSM = { "$8708", "$8709", "$8710", "$8704", "$8711", "$8712",
			"$8713", "$8706", "$8714", "$8715", "$8716", "$8717" };

	private void BonginNotice(HadinParty p) {

		S_PacketBox pb = new S_PacketBox(84, BSM[p.BS]);
		L1PcInstance[] pl = p.getParty().getMembers();
		for (L1PcInstance pc : pl) {
			pc.sendPackets(pb);
			pc.sendPackets(
					new S_PacketBox(S_PacketBox.ROUND_SHOW, p.BS + 1, 12), true);
		}
		p.BS++;
		pl = null;
	}

	public void Round_Show(L1Party l1party, L1PcInstance pc) {
		try {
			HadinParty hp = null;
			for (HadinParty p : party) {
				if (p.getParty() == l1party) {
					hp = p;
					break;
				}
			}
			if (hp != null) {
				if (hp.step < DUNGEON_6)
					pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND_SHOW, 12,
							12), true);
				else
					pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND_SHOW,
							hp.BS, 12), true);
			}
		} catch (Exception e) {
		}
	}

	/** true 긍정 Alt+2, false 부정 Alt+4 **/
	public void Alt_Action(L1Party l1party, boolean action) {
		HadinParty hp = null;
		for (HadinParty p : party) {
			if (p.getParty() == l1party) {
				hp = p;
				break;
			}
		}
		if (hp == null)
			return;
		if (hp.step == 1 && hp.sub_step == 5) {
			if (!action)
				hp.sub_step = 6;
			else
				hp.sub_step = 12;
		} else if (hp.step == 1 && (hp.sub_step == 14 || hp.sub_step == 15)) {
			if (action) {
				hp.sub_step = 16;
				Broadcaster.broadcastPacket(hp.Npc_Hadin, new S_NpcChatPacket(
						hp.Npc_Hadin, "$7600", 0), true);
				hp.delayTime = 0;
			}
		} else if (hp.leader_step == 1 && hp.leader_sub_step == 5) {
			hp.leader_sub_step = 6;
			hp.delayTime = 0;
		}
	}

	private void BossRoomLast(HadinParty p) {
		L1NpcInstance door = null;
		int x = 32703 + _rnd.nextInt(11);
		int y = 32859 + _rnd.nextInt(5);

		if (p.sub_step == 0) {
			p.sub_step++;
			return;
		}
		if (p.sub_step == 1) {
			door = p.BossRoomDoor.get("보스방 후문 문 8");
			Effect_EQ(p, true);
		} else if (p.sub_step == 2) {
			Effect_P(p);
			door = p.BossRoomDoor.get("보스방 후문 문 7");
		} else if (p.sub_step == 3) {
			door = p.BossRoomDoor.get("보스방 후문 문 6");
			Effect_P(p);
		} else if (p.sub_step == 4) {
			door = p.BossRoomDoor.get("보스방 후문 문 5");
		} else if (p.sub_step == 5) {
			door = p.BossRoomDoor.get("보스방 후문 문 16");
			Effect_P(p);
		} else if (p.sub_step == 6) {
			door = p.BossRoomDoor.get("보스방 후문 문 15");
		} else if (p.sub_step == 7) {
			door = p.BossRoomDoor.get("보스방 후문 문 14");
			Effect_P(p);
			S_PacketBox pb = new S_PacketBox(84,
					"하딘 : 3분 뒤에 말하는 섬으로 이동 될 것이네 서두르게");
			L1PcInstance[] pl = p.getParty().getMembers();
			for (L1PcInstance pc : pl) {
				pc.sendPackets(pb);
			}
		} else if (p.sub_step == 8) {
			door = p.BossRoomDoor.get("보스방 후문 문 13");
			Broadcaster.broadcastPacket(p.Npc_Hadin, new S_SkillSound(
					p.Npc_Hadin.getId(), 169), true);
			Teleportation.teleport(p.Npc_Hadin, 32747, 32930, p.mapid, 5);
			Teleportation.teleport(p.Hadin_Effect, 32747, 32930, p.mapid, 5);
		}
		if (p.sub_step >= 1 && p.sub_step <= 8) {
			try {
				if (door != null) {
					L1DoorInstance dor = (L1DoorInstance) door;
					// Teleportation.teleport(door, x, y, p.mapid, 5);
					// dor.open();
					// Broadcaster.broadcastPacket(dor, new S_Door(dor.getX(),
					// dor.getY(), 0, L1DoorInstance.PASS), true);

					dor.isPassibleDoor(true);
					dor.setPassable(0);
					보스방후문패킷전송(p, dor);
					dor.deleteMe();
					L1SpawnUtil.spawn2(x, y, p.mapid, 5000091, 0, 0, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			p.sub_step++;
			return;
		}
		BonginSendPacket(p, 8718);
		p.step = 11;
		p.sub_step = 0;
	}

	/*
	 * private void BossRoomLast2(HadinParty p){ L1NpcInstance door = null; int
	 * x = 32703 + _rnd.nextInt(11); int y = 32861 + _rnd.nextInt(5); door =
	 * p.BossRoomDoor.get("보스방 후문 문 8"); Effect_EQ(p, true); L1DoorInstance dor
	 * = (L1DoorInstance)door; //Teleportation.teleport(door, x, y, p.mapid, 5);
	 * dor.open(); dor.deleteMe(); L1SpawnUtil.spawn2(x, y, p.mapid, 5000091, 0,
	 * 0, 0); BonginSendPacket(p, 8718); }
	 */

	public void BowTrap(HadinParty p) {
		try {
			loc.setMap(p.mapid);
			int x = 0, y = 0, tx = 0, ty = 0, start = 0, end = 0;
			if (p.Arrow == null)
				return;
			for (FastMap.Entry<String, L1NpcInstance> e = p.Arrow.head(), mapEnd = p.Arrow
					.tail(); (e = e.getNext()) != mapEnd;) {
				String s = e.getKey();
				L1NpcInstance npc = e.getValue();
				boolean ck = false;
				if (npc == null)
					continue;
				if (p.ArrowTrap) {
					if (s.equalsIgnoreCase("화살 1")
							|| s.equalsIgnoreCase("화살 2")
							|| s.equalsIgnoreCase("화살 3")
							|| s.equalsIgnoreCase("화살 4")
							|| s.equalsIgnoreCase("화살 5")
							|| s.equalsIgnoreCase("화살 6")
							|| s.equalsIgnoreCase("화살 7")
							|| s.equalsIgnoreCase("화살 8")
							|| s.equalsIgnoreCase("화살 9")
							|| s.equalsIgnoreCase("화살 10")
							|| s.equalsIgnoreCase("화살 11")
							|| s.equalsIgnoreCase("화살 12"))
						continue;
				}
				x = npc.getX();
				y = npc.getY();
				if (s.equalsIgnoreCase("화살 1")) {
					tx = 32751;
					ty = 32798;
				} else if (s.equalsIgnoreCase("화살 2")) {
					tx = 32755;
					ty = 32798;
				} else if (s.equalsIgnoreCase("화살 3")) {
					tx = 32759;
					ty = 32798;
				} else if (s.equalsIgnoreCase("화살 4")) {
					tx = 32763;
					ty = 32798;
				} else if (s.equalsIgnoreCase("화살 5")) {
					tx = 32745;
					ty = 32800;
				} else if (s.equalsIgnoreCase("화살 6")) {
					tx = 32745;
					ty = 32801;
				} else if (s.equalsIgnoreCase("화살 7")) {
					tx = 32745;
					ty = 32802;
				} else if (s.equalsIgnoreCase("화살 8")) {
					tx = 32745;
					ty = 32803;
				} else if (s.equalsIgnoreCase("화살 9")) {
					tx = 32753;
					ty = 32804;
				} else if (s.equalsIgnoreCase("화살 10")) {
					tx = 32753;
					ty = 32805;
				} else if (s.equalsIgnoreCase("화살 11")) {
					tx = 32737;
					ty = 32806;
				} else if (s.equalsIgnoreCase("화살 12")) {
					tx = 32737;
					ty = 32807;
				} else if (s.equalsIgnoreCase("화살 13")) {
					tx = 32803 - 8;
					ty = 32838;
				} else if (s.equalsIgnoreCase("화살 14")) {
					tx = 32803 - 8;
					ty = 32841;
				} else if (s.equalsIgnoreCase("화살 15")) {
					tx = 32760 - 8;
					ty = 32863;
				} else if (s.equalsIgnoreCase("화살 16")) {
					tx = 32757;
					ty = 32854 + 8;
				} else if (s.equalsIgnoreCase("화살 17")) {
					tx = 32754 - 8;
					ty = 32855;
				} else if (s.equalsIgnoreCase("화살 18")) {
					tx = 32754 - 8;
					ty = 32842;
				} else if (s.equalsIgnoreCase("화살 19")) {
					tx = 32760 - 8;
					ty = 32833;
				} else if (s.equalsIgnoreCase("화살 20")) {
					tx = 32759;
					ty = 32828 + 8;
				} else if (s.equalsIgnoreCase("화살 21")) {
					tx = 32750;
					ty = 32828 + 8;
				} else if (s.equalsIgnoreCase("화살 22")) {
					tx = 32748;
					ty = 32828 + 8;
				} else if (s.equalsIgnoreCase("화살 23")) {
					tx = 32746;
					ty = 32828 + 8;
				} else if (s.equalsIgnoreCase("화살 24")) {
					tx = 32741;
					ty = 32832 + 4;
				} else if (s.equalsIgnoreCase("화살 25")) {
					tx = 32733;
					ty = 32832 + 4;
				} else if (s.equalsIgnoreCase("화살 26")) {
					tx = 32728;
					ty = 32832 + 4;
				} else if (s.equalsIgnoreCase("화살 27")) {
					tx = 32743;
					ty = 32860 + 4;
				} else if (s.equalsIgnoreCase("화살 28")) {
					tx = 32735;
					ty = 32860 + 4;
				} else if (s.equalsIgnoreCase("화살 29")) {
					tx = 32732;
					ty = 32858 + 6;
				} else if (s.equalsIgnoreCase("화살 30")) {
					tx = 32730;
					ty = 32858 + 6;
				} else if (s.equalsIgnoreCase("화살 31")) {
					tx = 32728;
					ty = 32858 + 6;
				} else if (s.equalsIgnoreCase("화살 32")) {
					tx = 32733;
					ty = 32840 + 3;
				} else if (s.equalsIgnoreCase("화살 33")) {
					tx = 32731;
					ty = 32840 + 3;
				} else if (s.equalsIgnoreCase("화살 34")) {
					tx = 32729;
					ty = 32840 + 3;
				} else if (s.equalsIgnoreCase("화살 35")) {
					tx = 32727;
					ty = 32840 + 3;
				} else if (s.equalsIgnoreCase("화살 36")) {
					tx = 32736;
					ty = 32853 + 3;
				} else if (s.equalsIgnoreCase("화살 37")) {
					tx = 32734;
					ty = 32853 + 3;
				} else if (s.equalsIgnoreCase("화살 38")) {
					tx = 32732;
					ty = 32853 + 3;
				} else if (s.equalsIgnoreCase("화살 39")) {
					tx = 32730;
					ty = 32853 + 3;
				} else if (s.equalsIgnoreCase("화살 40")) {
					tx = 32728;
					ty = 32853 + 3;
				} else if (s.equalsIgnoreCase("화살 41")) {
					tx = 32726;
					ty = 32853 + 1;
				} else if (s.equalsIgnoreCase("화살 42")) {
					tx = 32736;
					ty = 32845 + 6;
				} else if (s.equalsIgnoreCase("화살 43")) {
					tx = 32734;
					ty = 32845 + 6;
				} else if (s.equalsIgnoreCase("화살 44")) {
					tx = 32732;
					ty = 32845 + 6;
				} else if (s.equalsIgnoreCase("화살 45")) {
					tx = 32730;
					ty = 32845 + 6;
				} else if (s.equalsIgnoreCase("화살 46")) {
					tx = 32728;
					ty = 32845 + 6;
				} else if (s.equalsIgnoreCase("화살 47")) {
					tx = 32726;
					ty = 32845 + 6;
				} else if (s.equalsIgnoreCase("화살 48")) {
					tx = 32720;
					ty = 32842 + 8;
				} else if (s.equalsIgnoreCase("화살 49")) {
					tx = 32721 - 12;
					ty = 32834;
				} else if (s.equalsIgnoreCase("화살 50")) {
					tx = 32694;
					ty = 32832 + 8;
				} else if (s.equalsIgnoreCase("화살 51")) {
					tx = 32701 - 6;
					ty = 32843;
				}

				if (x != 0 && y != 0 && tx != 0 && ty != 0) {
					L1PcInstance target = null;
					int v = -1;
					for (L1PcInstance pc : L1World.getInstance()
							.getVisiblePlayer(npc)) {
						if (pc == null)
							continue;
						if (x == tx) {
							start = y;
							end = ty;
						} else {
							start = tx;
							end = x;
						}
						if (x == pc.getX() && y == pc.getY()) {
							ck = true;
							target = pc;
							break;
						}
						for (int i = start; i < end; i++) {
							if (v != -1 && i - start < v)
								continue;
							if ((pc.getX() == x && pc.getY() == i)
									|| (pc.getX() == i && pc.getY() == y)) {
								ck = true;
								v = i - start;
								target = pc;
								break;
							}
						}
					}
					if (!ck) { // 화살 GFX 66
						S_UseArrowSkill ua = new S_UseArrowSkill(npc, 0, 171,
								tx, ty, false);
						Broadcaster.broadcastPacket(npc, ua);
						ua = null;
					} else {
						S_UseArrowSkill ua = new S_UseArrowSkill(npc,
								target.getId(), 171, target.getX(),
								target.getY(), true);
						Broadcaster.broadcastPacket(npc, ua);
						ua = null;
						S_DoActionGFX da = new S_DoActionGFX(target.getId(),
								ActionCodes.ACTION_Damage);
						Broadcaster.broadcastPacketExceptTargetSight(target,
								da, npc);
						da = null;
						target.setCurrentHp(target.getCurrentHp() - 30); // 본섭은
																			// Dmg
																			// 5
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
