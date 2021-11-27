package l1j.server.GameSystem.DreamsTemple;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.GameSystem.GameList;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.utils.L1SpawnUtil;

public class DreamsTempleController implements Runnable {

	private static Random rnd = new Random(System.currentTimeMillis());
	private boolean on = true;
	private L1PcInstance pc = null;
	private int _mapnum = 0;
	private int step = 0;
	private int sub_step = 0;
	private int sub_step2 = 0;
	private int round = 0;
	private L1NpcInstance 유니콘;
	private FastTable<L1NpcInstance> monster;

	public DreamsTempleController(L1PcInstance _pc, int mapid) {
		pc = _pc;
		_mapnum = mapid;

		L1Teleport.teleport(pc, 32798, 32867, (short) mapid, 5, true);

		DreamsTempleSpawn.getInstance().fillSpawnTable(mapid, 0);
		monster = DreamsTempleSpawn.getInstance().fillSpawnTable(mapid, 1);
		유니콘 = L1SpawnUtil.spawn4(32801, 32862, (short) mapid, 4, 100749, 0, 0,
				0);

		GeneralThreadPool.getInstance().schedule(new timer(), 1000);

		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				for (L1NpcInstance npc : monster) {
					if (npc.getNpcId() == 100747)
						SHOUT_MSG(npc, "누구냐 어떻게 왔지?");
				}
			}
		}, 2000);
		GeneralThreadPool.getInstance().schedule(new Runnable() {
			@Override
			public void run() {
				for (L1NpcInstance npc : monster) {
					if (npc.getNpcId() == 100748) {
						SHOUT_MSG(npc, "그분께서 널 가만 두지 않을 것이다.");
						break;
					}
				}
			}
		}, 3000);
	}

	class timer implements Runnable {
		int time = 3600 * 60;
		boolean ck = false;

		@Override
		public void run() {
			try {
				if (!on)
					return;
				if (유니콘 == null || 유니콘._destroyed || 유니콘.isDead()) {
					step = 11;
					return;
				}
				if (!ck) {
					int percent = (int) Math.round((double) 유니콘.getCurrentHp()
							/ (double) 유니콘.getMaxHp() * 100);
					if (percent < 30) {
						GREEN_MSG("유니콘이 너무 많은 피해를 입었습니다.");
					}
					ck = true;
				}
				if (time-- < 0) {
					quit();
					return;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	class 대정령 implements Runnable {
		private boolean check = false;
		private L1MonsterInstance 정령 = null;

		@Override
		public void run() {
			try {

				if (!on)
					return;
				if (!check) {
					switch (rnd.nextInt(4)) {
					case 0:
						GREEN_MSG("땅의 대정령이 나타났습니다.");
						정령 = (L1MonsterInstance) L1SpawnUtil.spawn2(32806,
								32862, (short) _mapnum, 100758, 3, 0, 0);
						check = true;
						break;
					case 1:
						GREEN_MSG("바람의 대정령이 나타났습니다.");
						정령 = (L1MonsterInstance) L1SpawnUtil.spawn2(32801,
								32870, (short) _mapnum, 100759, 3, 0, 0);
						check = true;
						break;
					case 2:
						GREEN_MSG("물의 대정령이 나타났습니다.");
						정령 = (L1MonsterInstance) L1SpawnUtil.spawn2(32793,
								32861, (short) _mapnum, 100760, 3, 0, 0);
						check = true;
						break;
					case 3:
						GREEN_MSG("불의 대정령이 나타났습니다.");
						정령 = (L1MonsterInstance) L1SpawnUtil.spawn2(32799,
								32854, (short) _mapnum, 100761, 3, 0, 0);
						check = true;
						break;
					default:
						break;
					}

				} else {
					if (정령._destroyed || 정령.isDead()) {
						GREEN_MSG("대정령이 막대를 떨어뜨렸습니다. 막대를 이용하면 전투에 큰 도움이 될 것입니다.");
						List<Integer> dirList = new ArrayList<Integer>();
						for (int j = 0; j < 8; j++) {
							dirList.add(j);
						}
						for (int i = 0; i < 2; i++) {
							int x = 0;
							int y = 0;
							int dir = 0;
							do {
								if (dirList.size() == 0) {
									x = 0;
									y = 0;
									break;
								}
								int randomInt = rnd.nextInt(dirList.size());
								dir = dirList.get(randomInt);
								dirList.remove(randomInt);
								x = HEADING_TABLE_X[dir];
								y = HEADING_TABLE_Y[dir];
							} while (!정령.getMap().isPassable(정령.getX() + x,
									정령.getY() + y));
							L1GroundInventory targetInventory = L1World
									.getInstance().getInventory(정령.getX() + x,
											정령.getY() + y, 정령.getMapId());
							targetInventory.storeItem(60513, 1);
						}
						return;
					}
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class 지배자 implements Runnable {
		private int 지배자step = 0;
		private L1MonsterInstance mon = null;

		@Override
		public void run() {
			try {
				if (!on)
					return;
				switch (지배자step) {
				case 0:
					GREEN_MSG("몽환의 지배자 : 유니콘을 빼앗아가려고? 그렇게 놔둘 순 없지!!");
					지배자step++;
					break;
				case 1:
					Random random = new Random(System.nanoTime());
					int chance = random.nextInt(100);
					if (chance <= 35) {
					mon = (L1MonsterInstance) L1SpawnUtil.spawn2(32794, 32861,
							(short) _mapnum, 110001, 3, 0, 0);//아즈모단
					} else if (chance <= 35) {
						mon = (L1MonsterInstance) L1SpawnUtil.spawn2(32794, 32861, 
								(short) _mapnum, 110000, 3, 0, 0);//아비쉬
					} else {
						mon = (L1MonsterInstance) L1SpawnUtil.spawn2(32794, 32861,
								(short) _mapnum, 100762, 3, 0, 0);//천년구미호
					}
					지배자step++;
					break;
				case 2:// 죽었는지 체크
					if (mon == null || mon._destroyed || mon.isDead()) {
						step = 5;
						GREEN_MSG("몽환의 지배자가 퇴치 되었습니다.");
						유니콘.setCurrentHp(유니콘.getMaxHp());
						return;
					}
					break;
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static Random _random = new Random(System.nanoTime());

	@Override
	public void run() {
		try {
			if (!on)
				return;
			if (pc == null) {
				quit();
				return;
			}
			L1PcInstance _pc = L1World.getInstance().getPlayer(pc.getName());
			if (_pc == null || _pc.getMapId() != _mapnum) {
				quit();
				return;
			}
			switch (step) {
			case 0:
				int count = 0;
				L1NpcInstance delnpc = null;
				for (L1NpcInstance npc : monster) {
					if (npc == null || npc._destroyed || npc.isDead()) {
						count++;
						delnpc = npc;
					}
				}
				if (count >= monster.size()) {
					L1GroundInventory targetInventory = L1World.getInstance()
							.getInventory(delnpc.getX(), delnpc.getY(),
									delnpc.getMapId());
					targetInventory.storeItem(60512, 200);
					step++;
					monster = null;
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				} else {
					monster.remove(delnpc);
				}
				break;
			case 1:
				if (sub_step == 0)
					MSG(유니콘, "도와주러 와 주셔서 감사합니다.");
				else if (sub_step == 1)
					MSG(유니콘, "이계의 존재가 곧 돌아올겁니다.");
				else if (sub_step == 2)
					MSG(유니콘, "그전에 제가 봉인을 풀 수 있도록 시간을 벌어주세요.");
				else if (sub_step == 3) {
					MSG(유니콘, "마법 막대를 이용해 적을 처치해주세요.");
					GREEN_MSG("마법 막대를 이용해 적을 처치해주세요.");

				}

				if (sub_step++ >= 3) {
					step++;
				} else {
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				}
			case 2:
				if (round == 0)
					GREEN_MSG("적들이 몰려오고 있습니다.");
				else
					GREEN_MSG("적들이 더 몰려옵니다. 준비해 주세요");
				Effect_PEQ();
				Round();
				round++;
				step++;
				sub_step = 0;
				sub_step2 = 0;
				if (round == 3)
					sub_step2 = 1;
				else if (round == 4)
					sub_step2 = 2;
				GeneralThreadPool.getInstance().schedule(this, 5000);
				return;
			case 3:
				if (sub_step++ < 4) {
					DreamsTempleSpawn.getInstance().fillSpawnTable(_mapnum,
							1 + sub_step, 유니콘, round != 1);
					if (round == 3 || round == 4)
						GeneralThreadPool.getInstance().schedule(this, 8000);
					else
						GeneralThreadPool.getInstance().schedule(this, 12000);
					return;
				}

				if (sub_step2 > 0) {
					sub_step2--;
					sub_step = 0;
					if (round == 4 && sub_step2 == 0) {
						GeneralThreadPool.getInstance().schedule(new 지배자(),
								7000);
					} else if (round == 4 && sub_step2 == 1) {
						MSG(유니콘, "얼마 남지 않았습니다. 조금만 더 버텨주세요");
					}
					GeneralThreadPool.getInstance().schedule(this, 2000);
					return;
				}

				if (round >= 4)
					step++;
				else {
					step = 2;
					GeneralThreadPool.getInstance().schedule(new 대정령(), 1);
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				}
				break;
			case 4:// 대기 몽환의지배자 죽었는지
				break;
			case 5:
				for (L1Object ob : L1World.getInstance()
						.getVisibleObjects(_mapnum).values()) {
					if (ob instanceof L1MonsterInstance) {
						L1MonsterInstance npc = (L1MonsterInstance) ob;
						if (npc == null || npc._destroyed || npc.isDead())
							continue;
						npc.receiveDamage(pc, 10000);
					}
				}
				step++;
				sub_step = 0;
				GeneralThreadPool.getInstance().schedule(this, 5000);
				return;
			case 6:
				if (sub_step == 0) {
					유니콘.setTempCharGfx(12493);
					pc.sendPackets(new S_ChangeShape(유니콘.getId(), 12493), true);
					sub_step++;
					GeneralThreadPool.getInstance().schedule(this, 1000);
					return;
				} else if (sub_step == 1) {
					MSG(유니콘, "감사합니다");
					GREEN_MSG("감사합니다!");
					sub_step++;
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				} else if (sub_step == 2) {
					MSG(유니콘, "당분간 그것은 돌아올 수 없을 것입니다.");
					GREEN_MSG("당분간 그것은 돌아올 수 없을 것입니다.");
					sub_step++;
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				} else if (sub_step == 3) {
					MSG(유니콘, "어서 몽환의 섬으로 돌아가 봐야겠군요.");
					GREEN_MSG("어서 몽환의 섬으로 돌아가 봐야겠군요.");
					sub_step++;
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				} else if (sub_step == 4) {
					MSG(유니콘, "선물을 드리고 싶군요. 마음에 드셨으면 좋겠네요.");
					GREEN_MSG("선물을 드리고 싶군요. 마음에 드셨으면 좋겠네요.");
					sub_step++;
					GeneralThreadPool.getInstance().schedule(this, 5000);
					return;
				} else if (sub_step == 5) {
					int rnd = _random.nextInt(1000) + 1;
					int itemid = 0;

					if (rnd == 5) {
						itemid = 50004;//축 유니 갑옷
					} else if (rnd == 5) {
						itemid = 50003;//유니 갑옷
					} else if (rnd <= 5) {
						itemid = 50005;// 저주 유니 갑옷
					} else if (rnd <= 15) {
						itemid = 100030;// 고대 신의 창
					} else if (rnd <= 15) {
						itemid = 100031;// 고대 신의 도끼
					} else if (rnd <= 30) {
						itemid = 40074;// 갑옷마법주문서
					} else if (rnd <= 25) {
						itemid = 40087;// 무기마법주문서
					}

					L1GroundInventory targetInventory = L1World.getInstance()
							.getInventory(유니콘.getX(), 유니콘.getY(),
									유니콘.getMapId());
					targetInventory.storeItem(60514, 1);
					if (itemid > 0)
						targetInventory.storeItem(itemid, 1);

					pc.sendPackets(new S_SkillSound(유니콘.getId(), 169));
					pc.sendPackets(new S_RemoveObject(유니콘));
					pc.getNearObjects().removeKnownObject(유니콘);
					유니콘.teleport(32771, 32835, 6);
					step++;
					GeneralThreadPool.getInstance().schedule(this, 20000);
					return;
				}
				break;
			case 7:
				GREEN_MSG("5초뒤 마을로 이동합니다.");
				step++;
				GeneralThreadPool.getInstance().schedule(this, 3000);
				return;
			case 8:
				GREEN_MSG("2초뒤 마을로 이동합니다.");
				step++;
				GeneralThreadPool.getInstance().schedule(this, 2000);
				return;
			case 9:
				GREEN_MSG("1초뒤 마을로 이동합니다.");
				step++;
				GeneralThreadPool.getInstance().schedule(this, 1000);
				return;
			case 10:// 정상 종료
				quit();
				break;
			case 11:// 유니콘 사망
				GREEN_MSG("유니콘이 사라집니다.");
				for (L1Object ob : L1World.getInstance()
						.getVisibleObjects(_mapnum).values()) {
					if (ob instanceof L1MonsterInstance) {
						L1MonsterInstance npc = (L1MonsterInstance) ob;
						if (npc == null || npc._destroyed || npc.isDead())
							continue;
						npc.deleteMe();
					}
				}
				step = 7;
				GeneralThreadPool.getInstance().schedule(this, 1000);
				return;
			default:
				break;
			}
		} catch (Exception e) {
		}
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	public int getMapId() {
		return _mapnum;
	}

	private void quit() {
		on = false;
		HOME_TELEPORT();
		DreamsTemple DT = GameList.getDT(getMapId());
		if (DT != null) {
			DT.Reset();
		}
	}

	private void GREEN_MSG(String msg) {
		pc.sendPackets(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, msg));
	}

	private void Effect_PEQ() {
		pc.sendPackets(new S_Sound(184));
		pc.sendPackets(new S_PacketBox(83, 1));
		pc.sendPackets(new S_PacketBox(83, 2));
	}

	private void Round() {
		pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND_SHOW, round + 1, 4),
				true);
	}

	private void MSG(L1NpcInstance npc, String msg) {
		Broadcaster
				.broadcastPacket(npc, new S_NpcChatPacket(npc, msg, 0), true);
	}

	private void SHOUT_MSG(L1NpcInstance npc, String msg) {
		Broadcaster
				.broadcastPacket(npc, new S_NpcChatPacket(npc, msg, 2), true);
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

}