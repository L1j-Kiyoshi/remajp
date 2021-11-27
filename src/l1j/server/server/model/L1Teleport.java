/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model;

import java.util.HashSet;
import java.util.List;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_OtherCharPacks;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.utils.Teleportation;
import server.threads.pc.SabuTel;

public class L1Teleport {

	public static final int TELEPORT = 0;
	public static final int CHANGE_POSITION = 1;
	public static final int ADVANCED_MASS_TELEPORT = 2;
	public static final int CALL_CLAN = 3;
	public static final int DUNGEON_TELEPORT = 4;
	public static final int NODELAY_TELEPORT = 5;

	public static final int[] EFFECT_SPR = { 169, 169, 2236, 2281, 169 };
	public static final int[] EFFECT_TIME = { 280, 440, 440, 1120, 280 };

	private L1Teleport() {
	}

	public static void teleport(L1PcInstance pc, L1Location loc, int head, boolean effectable) {
		teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), head, effectable, TELEPORT);
	}

	public static void teleport(L1PcInstance pc, int x, int y, short mapid, int head, boolean effectable, boolean swich) {
		if (swich) {
			if (pc instanceof L1RobotInstance) {
				((L1RobotInstance) pc).텔(x, y, mapid, 1, effectable);
				return;
			}
			pc.setTeleportX(x);
			pc.setTeleportY(y);
			pc.setTeleportMapId(mapid);
			pc.setTeleportHeading(head);
			
			long Time = 100;
			if (pc.getNetConnection().getInterServer() && pc.getMapId() != mapid){
				if(!(mapid >= 128520 && mapid <= 128620) && pc.getMapId() >= 128520 && pc.getMapId() <= 128620)
					Time = 0;
			}
			if (effectable && Time != 0) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 169), true);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),169), true);
			}
			SabuTel 텔 = new SabuTel(200, pc);
			// GeneralThreadPool.getInstance().execute(텔);
			GeneralThreadPool.getInstance().schedule(텔, 200);
		} else {
			teleport(pc, x, y, mapid, head, effectable, false, 0);
		}
	}

	public static void teleport(L1PcInstance pc, int x, int y, short mapid, int head, boolean effectable) {
		teleport(pc, x, y, mapid, head, effectable, false, 0);
	}

	public static void teleport(L1PcInstance pc, int x, int y, short mapid, int head, boolean effectable, boolean absolut, int absolutTime) {
		if (pc instanceof L1RobotInstance) {
			((L1RobotInstance) pc).텔(x, y, mapid, 1, effectable);
		} else {
			if (effectable)
				pc.setTelType(7);
			else
				pc.setTelType(10);
			pc.dx = x;
			pc.dy = y;
			pc.dm = mapid;
			pc.dh = head;
			if (absolut)
				pc.sendPackets(new S_SabuTell(pc, absolutTime), true);
			else
				pc.sendPackets(new S_SabuTell(pc), true);
		}
	}

	public static void teleport(L1PcInstance pc, int x, int y, short mapid,
			int head) {
		// dotel dt = new dotel(pc, x, y, mapid, head);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
				EFFECT_SPR[0]), true);
		DoTelThread dt = new DoTelThread(pc, x, y, mapid, head);
		// GeneralThreadPool.getInstance().execute(dt);
		// GeneralThreadPool.getInstance().schedule(dt,
		// EFFECT_TIME[NODELAY_TELEPORT]);
		GeneralThreadPool.getInstance().schedule(dt, /* EFFECT_TIME[TELEPORT] */
				1);
	}

	public static void ロボットテル(L1RobotInstance rob, int x, int y, short m,
			boolean swich) {
		rob.텔(x, y, m);
		/*
		 * rob.setTeleport(true); for (L1PcInstance pc :
		 * L1World.getInstance().getRecognizePlayer(rob)) { if(swich){
		 * pc.sendPackets(new S_SkillSound(rob.getId(), 169), true); }
		 * pc.sendPackets(new S_RemoveObject(rob), true);
		 * pc.getNearObjects().removeKnownObject(rob); }
		 * 
		 * L1World.getInstance().moveVisibleObject(rob, x, y, m); rob.setX(x);
		 * rob.setY(y); rob.setMap(m); rob.setTeleport(false); rob.loc = null;
		 */
	}

	public static void 분신텔(L1PcInstance rob, int x, int y, short m,
			boolean swich) {
		try {

			rob.setTeleport(true);

			List<L1PcInstance> list = rob.getNearObjects().getKnownPlayers();
			S_RemoveObject ro = new S_RemoveObject(rob.getId());
			for (L1PcInstance target : list) {
				if (target == null)
					continue;
				target.sendPackets(ro);
			}

			L1World.getInstance().moveVisibleObject(rob, x, y, m);
			rob.setX(x);
			rob.setY(y);
			rob.setMap(m);
			rob.setTeleport(false);

			// if (!pc.isGhost() && !pc.isGmInvis() && !pc.isInvisble()) {

			// }
			for (L1PcInstance pc2 : L1World.getInstance().getVisiblePlayer(rob)) {
				pc2.sendPackets(new S_OtherCharPacks(rob, pc2));
			}

			if (rob.isPinkName()) {
				Broadcaster.broadcastPacket(
						rob,
						new S_PinkName(rob.getId(), rob
								.getSkillEffectTimerSet()
								.getSkillEffectTimeSec(
										L1SkillId.STATUS_PINK_NAME)), true);
			}

			// 1123
			rob.getNearObjects().removeAllKnownObjects();
			rob.sendVisualEffectAtTeleport();
			rob.updateObject();
			//

			rob.getSkillEffectTimerSet().killSkillEffectTimer(
					L1SkillId.MEDITATION);
			rob.setCallClanId(0);
			HashSet<L1PcInstance> subjects = new HashSet<L1PcInstance>();
			subjects.add(rob);

			if (!rob.isGhost()) {
				if (rob.getMap().isTakePets()) {
					int pet_heading = 5;
					if (rob.getPetList() != null && rob.getPetListSize() > 0) {
						for (L1NpcInstance petNpc : rob.getPetList()) {
							try {
								if (petNpc == null)
									continue;
								L1Location loc = rob.getLocation()
										.randomLocation(3, false);
								int nx = loc.getX();
								int ny = loc.getY();
								if (rob.getMapId() == 5125
										|| rob.getMapId() == 5131
										|| rob.getMapId() == 5132
										|| rob.getMapId() == 5133
										|| rob.getMapId() == 5134) {
									boolean xy_check = false;
									for (L1Object obj : L1World.getInstance()
											.getVisibleObjects(rob)) {
										if (obj == null
												|| !(obj instanceof L1PetInstance))
											continue;
										if (obj.getX() == 32797
												&& obj.getY() == 32863
												&& m == obj.getMapId())
											xy_check = true;
									}

									if (xy_check) {
										nx = 32801;
										ny = 32863;
										pet_heading = 6;
									} else {
										nx = 32797;
										ny = 32863;
										pet_heading = 2;
									}
								}
								Teleportation.teleport(petNpc, nx, ny, m,
										pet_heading);

								for (L1PcInstance visiblePc : L1World
										.getInstance().getVisiblePlayer(petNpc)) {
									try {
										if (visiblePc.getNearObjects()
												.knownsObject(petNpc))
											visiblePc.sendPackets(
													new S_RemoveObject(petNpc),
													true);
										visiblePc.getNearObjects()
												.removeKnownObject(petNpc);
										subjects.add(visiblePc);
									} catch (Exception e) {
									}
								}
							} catch (Exception e) {
							}
						}
					}
				} else {
					try {
						if (rob.getPetList() != null
								&& rob.getPetListSize() > 0) {
							for (L1NpcInstance petNpc : rob.getPetList()) {
								if (petNpc instanceof L1SummonInstance) {
									((L1SummonInstance) petNpc).Death(null);
								}
							}
						}
					} catch (Exception e) {

					}
				}

				if (rob.getDollList() != null && rob.getDollListSize() > 0) {
					for (L1DollInstance doll : rob.getDollList()) {
						if (doll == null)
							continue;
						L1Location loc = rob.getLocation().randomLocation(3,
								false);
						int nx = loc.getX();
						int ny = loc.getY();

						// 11_29 추가
						for (L1PcInstance visiblePc : L1World.getInstance()
								.getVisiblePlayer(doll)) {
							try {
								if (visiblePc.getNearObjects().knownsObject(
										doll)) {
									visiblePc.sendPackets(new S_RemoveObject(
											doll), true);
									visiblePc.getNearObjects()
											.removeKnownObject(doll);
								}
								if (!subjects.contains(visiblePc))
									subjects.add(visiblePc);
							} catch (Exception e) {
							}
						}

						Teleportation.teleport(doll, nx, ny, m, 5);

						for (L1PcInstance visiblePc : L1World.getInstance()
								.getVisiblePlayer(doll)) {
							try {
								if (visiblePc.getNearObjects().knownsObject(
										doll)) {
									visiblePc.sendPackets(new S_RemoveObject(
											doll), true);
									visiblePc.getNearObjects()
											.removeKnownObject(doll);
								}
								if (!subjects.contains(visiblePc))
									subjects.add(visiblePc);
							} catch (Exception e) {
							}
						}
					}
				}
			}

			for (L1PcInstance updatePc : subjects) {
				try {
					updatePc.updateObject();
				} catch (Exception e) {
				}
			}
			for (L1PcInstance updatePc : L1World.getInstance()
					.getVisiblePlayer(rob)) {
				try {
					if (updatePc.getNearObjects().knownsObject(rob)) {
						updatePc.sendPackets(new S_RemoveObject(rob), true);
						updatePc.getNearObjects().removeKnownObject(rob);
					}
					updatePc.updateObject();
				} catch (Exception e) {
				}
			}
			rob.setTeleport(false);

			if (rob.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.WIND_SHACKLE)) {
				rob.sendPackets(
						new S_SkillIconWindShackle(rob.getId(), rob
								.getSkillEffectTimerSet()
								.getSkillEffectTimeSec(L1SkillId.WIND_SHACKLE)),
						true);

			}
			if (rob.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_DRAGONPERL)) {
				Broadcaster.broadcastPacket(rob, new S_DRAGONPERL(rob.getId(),
						8), true);
				rob.set진주속도(1);
			}
		} catch (Exception e) {
			System.out.println("텔 심각 오류코드 100");
			e.printStackTrace();
		}
	}

	public static void ロボットテル2(L1RobotInstance rob, int x, int y, short m,
			boolean swich) {
		try {

			rob.setTeleport(true);

			List<L1PcInstance> list = rob.getNearObjects().getKnownPlayers();
			S_RemoveObject ro = new S_RemoveObject(rob.getId());
			for (L1PcInstance target : list) {
				if (target == null)
					continue;
				target.sendPackets(ro);
			}

			L1World.getInstance().moveVisibleObject(rob, x, y, m);
			rob.setX(x);
			rob.setY(y);
			rob.setMap(m);
			rob.setTeleport(false);

			// if (!pc.isGhost() && !pc.isGmInvis() && !pc.isInvisble()) {

			// }
			for (L1PcInstance pc2 : L1World.getInstance().getVisiblePlayer(rob)) {
				pc2.sendPackets(new S_OtherCharPacks(rob, pc2));
			}

			if (rob.isPinkName()) {
				Broadcaster.broadcastPacket(
						rob,
						new S_PinkName(rob.getId(), rob
								.getSkillEffectTimerSet()
								.getSkillEffectTimeSec(
										L1SkillId.STATUS_PINK_NAME)), true);
			}

			// 1123
			rob.getNearObjects().removeAllKnownObjects();
			rob.sendVisualEffectAtTeleport();
			rob.updateObject();
			//

			rob.getSkillEffectTimerSet().killSkillEffectTimer(
					L1SkillId.MEDITATION);
			rob.setCallClanId(0);
			HashSet<L1PcInstance> subjects = new HashSet<L1PcInstance>();
			subjects.add(rob);

			if (!rob.isGhost()) {
				if (rob.getMap().isTakePets()) {
					int pet_heading = 5;
					if (rob.getPetList() != null && rob.getPetListSize() > 0) {
						for (L1NpcInstance petNpc : rob.getPetList()) {
							try {
								if (petNpc == null)
									continue;
								L1Location loc = rob.getLocation()
										.randomLocation(3, false);
								int nx = loc.getX();
								int ny = loc.getY();
								if (rob.getMapId() == 5125
										|| rob.getMapId() == 5131
										|| rob.getMapId() == 5132
										|| rob.getMapId() == 5133
										|| rob.getMapId() == 5134) {
									boolean xy_check = false;
									for (L1Object obj : L1World.getInstance()
											.getVisibleObjects(rob)) {
										if (obj == null
												|| !(obj instanceof L1PetInstance))
											continue;
										if (obj.getX() == 32797
												&& obj.getY() == 32863
												&& m == obj.getMapId())
											xy_check = true;
									}

									if (xy_check) {
										nx = 32801;
										ny = 32863;
										pet_heading = 6;
									} else {
										nx = 32797;
										ny = 32863;
										pet_heading = 2;
									}
								}
								Teleportation.teleport(petNpc, nx, ny, m,
										pet_heading);

								for (L1PcInstance visiblePc : L1World
										.getInstance().getVisiblePlayer(petNpc)) {
									try {
										if (visiblePc.getNearObjects()
												.knownsObject(petNpc))
											visiblePc.sendPackets(
													new S_RemoveObject(petNpc),
													true);
										visiblePc.getNearObjects()
												.removeKnownObject(petNpc);
										subjects.add(visiblePc);
									} catch (Exception e) {
									}
								}
							} catch (Exception e) {
							}
						}
					}
				} else {
					try {
						if (rob.getPetList() != null
								&& rob.getPetListSize() > 0) {
							for (L1NpcInstance petNpc : rob.getPetList()) {
								if (petNpc instanceof L1SummonInstance) {
									((L1SummonInstance) petNpc).Death(null);
								}
							}
						}
					} catch (Exception e) {

					}
				}

				if (rob.getDollList() != null && rob.getDollListSize() > 0) {
					for (L1DollInstance doll : rob.getDollList()) {
						if (doll == null)
							continue;
						L1Location loc = rob.getLocation().randomLocation(3,
								false);
						int nx = loc.getX();
						int ny = loc.getY();

						// 11_29 추가
						for (L1PcInstance visiblePc : L1World.getInstance()
								.getVisiblePlayer(doll)) {
							try {
								if (visiblePc.getNearObjects().knownsObject(
										doll)) {
									visiblePc.sendPackets(new S_RemoveObject(
											doll), true);
									visiblePc.getNearObjects()
											.removeKnownObject(doll);
								}
								if (!subjects.contains(visiblePc))
									subjects.add(visiblePc);
							} catch (Exception e) {
							}
						}

						Teleportation.teleport(doll, nx, ny, m, 5);

						for (L1PcInstance visiblePc : L1World.getInstance()
								.getVisiblePlayer(doll)) {
							try {
								if (visiblePc.getNearObjects().knownsObject(
										doll)) {
									visiblePc.sendPackets(new S_RemoveObject(
											doll), true);
									visiblePc.getNearObjects()
											.removeKnownObject(doll);
								}
								if (!subjects.contains(visiblePc))
									subjects.add(visiblePc);
							} catch (Exception e) {
							}
						}
					}
				}
			}

			for (L1PcInstance updatePc : subjects) {
				try {
					updatePc.updateObject();
				} catch (Exception e) {
				}
			}
			for (L1PcInstance updatePc : L1World.getInstance()
					.getVisiblePlayer(rob)) {
				try {
					if (updatePc.getNearObjects().knownsObject(rob)) {
						updatePc.sendPackets(new S_RemoveObject(rob), true);
						updatePc.getNearObjects().removeKnownObject(rob);
					}
					updatePc.updateObject();
				} catch (Exception e) {
				}
			}
			rob.setTeleport(false);

			if (rob.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.WIND_SHACKLE)) {
				rob.sendPackets(
						new S_SkillIconWindShackle(rob.getId(), rob
								.getSkillEffectTimerSet()
								.getSkillEffectTimeSec(L1SkillId.WIND_SHACKLE)),
						true);

			}
			if (rob.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_DRAGONPERL)) {
				Broadcaster.broadcastPacket(rob, new S_DRAGONPERL(rob.getId(),
						8), true);
				rob.set진주속도(1);
			}
			rob.loc = null;
		} catch (Exception e) {
			System.out.println("텔 심각 오류코드 100");
			e.printStackTrace();
		}
	}

	public static void teleport(L1PcInstance pc, int x, int y, short mapId,
			int head, boolean effectable, int skillType) {
		if (pc instanceof L1RobotInstance) {
			L1RobotInstance rob = (L1RobotInstance) pc;
			ロボットテル(rob, x, y, mapId, true);
			return;
		}
		pc.setTeleportX(x);
		pc.setTeleportY(y);
		pc.setTeleportMapId(mapId);
		pc.setTeleportHeading(head);
		if (pc.getTelType() != 99) {
			long Time = 280;
			if (pc.getNetConnection().getInterServer() && pc.getMapId() != mapId){
				if(!(mapId >= 128520 && mapId <= 128620) && pc.getMapId() >= 128520 && pc.getMapId() <= 128620)
					Time = 0;
			}else if (pc.getTelType() == 1 || pc.getTelType() == 4) {
				Time = 100;
			}
			if (effectable && Time != 0) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 169), true);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),169), true);
			}
			SabuTel Teleport = new SabuTel(Time, pc);
			GeneralThreadPool.getInstance().schedule(Teleport, Time);
			pc.setTelType(0);
		}else{
			if (effectable) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 169), true);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),169), true);
			}
		}
	}

	public static L1Location ToTargetFront(L1Character target, int distance) {

		L1Location loc = new L1Location();

		int locX = target.getX();
		int locY = target.getY();
		int heading = target.getMoveState().getHeading();
		loc.setMap(target.getMapId());
		switch (heading) {
		case 1:
			locX += distance;
			locY -= distance;
			break;
		case 2:
			locX += distance;
			break;
		case 3:
			locX += distance;
			locY += distance;
			break;
		case 4:
			locY += distance;
			break;
		case 5:
			locX -= distance;
			locY += distance;
			break;
		case 6:
			locX -= distance;
			break;
		case 7:
			locX -= distance;
			locY -= distance;
			break;
		case 0:
			locY -= distance;
			break;
		default:
			break;
		}

		loc.setX(locX);
		loc.setY(locY);

		return loc;
	}
	
	public static L1Location Pantera(L1PcInstance attaker, L1Character target, int distance) {

		L1Location loc = new L1Location();
//		방향 0. 좌상 1. 상 2. 우상 3. 오른쪽 4. 우하 5. 하 6. 좌하 7. 좌
		int locX = target.getX();
		int locY = target.getY();
		loc.setMap(target.getMapId());
		int pcX = attaker.getX();
		int pcY = attaker.getY();
		int heading = 0;
		if (pcX < locX && pcY == locY) {
			heading = 3;
			locX -= distance;
		} else if (locX == pcX && pcY > locY) {
			heading = 0;
			locY += distance;
		} else if (pcX > locX && pcY == locY) {
			heading = 7;
			locX += distance;
		} else if (pcX == locX && pcY < locY) {
			heading = 5;
			locY -= distance;
		} else if (pcX > locX && pcY < locY) {
			heading = 6;
			locX += distance;
			locY -= distance;
		} else if (pcX < locX && pcY > locY) {
			heading = 1;
			locX -= distance;
			locY += distance;
		} else if (pcX < locX && pcY < locY) {
			heading = 3;
			locX -= distance;
			locY -= distance;
		} else if (pcX > locX && pcY > locY) {
			heading = 7;
			locX += distance;
			locY += distance;
		}
		
		loc.setX(locX);
		loc.setY(locY);
		attaker.getMoveState().setHeading(heading);
		Broadcaster.broadcastPacket(attaker, new S_ChangeHeading(attaker));
		return loc;
	}
	
	public static L1Location ShadowStep(L1PcInstance attaker, L1Character target, int distance) {

		L1Location loc = new L1Location();
//		방향 0. 좌상 1. 상 2. 우상 3. 오른쪽 4. 우하 5. 하 6. 좌하 7. 좌
		int locX = target.getX();
		int locY = target.getY();
		loc.setMap(target.getMapId());
		int pcX = attaker.getX();
		int pcY = attaker.getY();
		int heading = 0;
		if (pcX < locX && pcY == locY) {
			heading = 3;
			locX -= distance;
		} else if (locX == pcX && pcY > locY) {
			heading = 0;
			locY += distance;
		} else if (pcX > locX && pcY == locY) {
			heading = 7;
			locX += distance;
		} else if (pcX == locX && pcY < locY) {
			heading = 5;
			locY -= distance;
		} else if (pcX > locX && pcY < locY) {
			heading = 6;
			locX += distance;
			locY -= distance;
		} else if (pcX < locX && pcY > locY) {
			heading = 1;
			locX -= distance;
			locY += distance;
		} else if (pcX < locX && pcY < locY) {
			heading = 3;
			locX -= distance;
			locY -= distance;
		} else if (pcX > locX && pcY > locY) {
			heading = 7;
			locX += distance;
			locY += distance;
		}
		
		loc.setX(locX);
		loc.setY(locY);
		attaker.getMoveState().setHeading(heading);
		Broadcaster.broadcastPacket(attaker, new S_ChangeHeading(attaker));
		return loc;
	}

	public static void teleportToTargetFront(L1Character cha,
			L1Character target, int distance) {
		int locX = target.getX();
		int locY = target.getY();
		int heading = target.getMoveState().getHeading();
		L1Map map = target.getMap();
		short mapId = target.getMapId();

		switch (heading) {
		case 1:
			locX += distance;
			locY -= distance;
			break;
		case 2:
			locX += distance;
			break;
		case 3:
			locX += distance;
			locY += distance;
			break;
		case 4:
			locY += distance;
			break;
		case 5:
			locX -= distance;
			locY += distance;
			break;
		case 6:
			locX -= distance;
			break;
		case 7:
			locX -= distance;
			locY -= distance;
			break;
		case 0:
			locY -= distance;
			break;
		default:
			break;
		}

		if (map.isPassable(locX, locY)) {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (pc.noPlayerCK) {
					if (pc instanceof L1RobotInstance) {
						L1RobotInstance rob = (L1RobotInstance) pc;
						L1Teleport.ロボットテル(rob, locX, locY, mapId, true);
					} else {
						teleport((L1PcInstance) cha, locX, locY, mapId, cha
								.getMoveState().getHeading());
					}
				} else {
					teleport((L1PcInstance) cha, locX, locY, mapId, cha
							.getMoveState().getHeading(), true);
				}
			} else if (cha instanceof L1NpcInstance) {
			}
		}
	}

	public static void randomBookmarkTeleport(L1PcInstance pc,
			L1BookMark bookm, int heading, boolean effectable) {
		L1Location newLocation = L1Location.randomBookmarkLocation(bookm, true);
		int newX = newLocation.getX();
		int newY = newLocation.getY();
		int newHeading = pc.getMoveState().getHeading();
		short mapId = (short) newLocation.getMapId();

		L1Teleport.teleport(pc, newX, newY, mapId, newHeading, effectable);
	}

	public static void randomTeleport(L1PcInstance pc, boolean effectable) {
		L1Location newLocation = pc.getLocation().randomLocation(30, true);
		int newX = newLocation.getX();
		int newY = newLocation.getY();
		int newHeading = pc.getMoveState().getHeading();
		short mapId = (short) newLocation.getMapId();

		L1Teleport.teleport(pc, newX, newY, mapId, newHeading, effectable);
	}
}
