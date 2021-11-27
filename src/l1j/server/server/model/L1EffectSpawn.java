/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.templates.L1Npc;

// Referenced classes of package l1j.server.server.model:
// L1EffectSpawn

public class L1EffectSpawn {

	private static final Logger _log = Logger.getLogger(L1EffectSpawn.class
			.getName());

	private static L1EffectSpawn _instance;

	private Constructor<?> _constructor;

	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	private L1EffectSpawn() {
	}

	public static L1EffectSpawn getInstance() {
		if (_instance == null) {
			_instance = new L1EffectSpawn();
		}
		return _instance;
	}

	public L1EffectInstance spawnEffect(int npcid, int time, int locX,
			int locY, short mapId) {
		L1Npc template = NpcTable.getInstance().getTemplate(npcid);
		L1EffectInstance effect = null;

		if (template == null) {
			return null;
		}

		String className = (new StringBuilder())
				.append("l1j.server.server.model.Instance.")
				.append(template.getImpl()).append("Instance").toString();

		try {
			_constructor = Class.forName(className).getConstructors()[0];
			Object obj[] = { template };
			effect = (L1EffectInstance) _constructor.newInstance(obj);

			effect.setId(ObjectIdFactory.getInstance().nextId());
			effect.getGfxId().setGfxId(template.get_gfxid());
			effect.setX(locX);
			effect.setY(locY);
			effect.setHomeX(locX);
			effect.setHomeY(locY);
			effect.getMoveState().setHeading(0);
			effect.setMap(mapId);
			L1World.getInstance().storeObject(effect);
			L1World.getInstance().addVisibleObject(effect);

			/*
			 * for (L1PcInstance pc :
			 * L1World.getInstance().getRecognizePlayer(effect)) {
			 * effect.getNearObjects().addKnownObject(pc);
			 * pc.getNearObjects().addKnownObject(effect); pc.sendPackets(new
			 * S_NPCPack(effect), true); Broadcaster.broadcastPacket(pc, new
			 * S_NPCPack(effect), true); }
			 */

			L1NpcDeleteTimer timer = new L1NpcDeleteTimer(effect, time);
			timer.begin();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		return effect;
	}

	public L1EffectInstance spawnEffect(int npcid, int time, int locX,
			int locY, short mapId, String name, int gfxid) {
		L1Npc template = NpcTable.getInstance().getTemplate(npcid);
		L1EffectInstance effect = null;

		if (template == null) {
			return null;
		}

		String className = (new StringBuilder())
				.append("l1j.server.server.model.Instance.")
				.append(template.getImpl()).append("Instance").toString();

		try {
			_constructor = Class.forName(className).getConstructors()[0];
			Object obj[] = { template };
			effect = (L1EffectInstance) _constructor.newInstance(obj);

			effect.setId(ObjectIdFactory.getInstance().nextId());
			effect.getGfxId().setGfxId(template.get_gfxid());
			effect.getGfxId().setTempCharGfx(gfxid);
			effect.setNameId(name);
			effect.setX(locX);
			effect.setY(locY);
			effect.setHomeX(locX);
			effect.setHomeY(locY);
			effect.getMoveState().setHeading(0);
			effect.setMap(mapId);
			L1World.getInstance().storeObject(effect);
			L1World.getInstance().addVisibleObject(effect);

			/*
			 * for (L1PcInstance pc : L1World.getInstance()
			 * .getRecognizePlayer(effect)) {
			 * effect.getNearObjects().addKnownObject(pc);
			 * pc.getNearObjects().addKnownObject(effect); pc.sendPackets(new
			 * S_NPCPack(effect), true); Broadcaster.broadcastPacket(pc, new
			 * S_NPCPack(effect), true); }
			 */
			L1NpcDeleteTimer timer = new L1NpcDeleteTimer(effect, time);
			timer.begin();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

		return effect;
	}

	public void doSpawnFireWall(L1Character cha, int targetX, int targetY) {
		L1Npc firewall = NpcTable.getInstance().getTemplate(81157);
		int duration = SkillsTable.getInstance()
				.getTemplate(L1SkillId.FIRE_WALL).getBuffDuration();

		if (firewall == null) {
			throw new NullPointerException(
					"FireWall data not found:npcid=81157");
		}

		L1Character base = cha;
		ArrayList<L1Object> list = L1World.getInstance().getVisibleObjects(cha);
		for (int i = 0; i < 8; i++) {
			int a = CharPosUtil.targetDirection(base, targetX, targetY);
			int x = base.getX();
			int y = base.getY();

			x += HEADING_TABLE_X[a];
			y += HEADING_TABLE_Y[a];

			if (!CharPosUtil.isAttackPosition(base, x, y, base.getMapId(), 1)) {
				x = base.getX();
				y = base.getY();
			}
			L1Map map = L1WorldMap.getInstance().getMap(cha.getMapId());

			if (!map.isArrowPassable(x, y, cha.getMoveState().getHeading())) {
				break;
			}
			L1EffectInstance effect = spawnEffect(81157, duration * 1000, x, y,
					cha.getMapId());
			if (effect == null) {
				break;
			}
			L1EffectInstance npc = null;
			for (L1Object objects : list) {
				if (objects.getX() != x || objects.getY() != y)
					continue;
				if (objects instanceof L1EffectInstance) {
					npc = (L1EffectInstance) objects;
					if (npc.getNpcTemplate().get_npcId() == 81157) {
						npc.deleteMe();
					}
				}
			}
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				effect.setCubePc(pc);
			}
			if (targetX == x && targetY == y) {
				break;
			}
			base = effect;
		}

	}

	public void doSpawnPoisonClude(L1Character cha, int targetX, int targetY) {
		GeneralThreadPool.getInstance().execute(new PoisonCludeSpawn(cha));
	}

	public void doSpawnPoisonClude(L1Character cha, int targetX, int targetY,
			boolean damage) {
		GeneralThreadPool.getInstance().execute(
				new PoisonCludeSpawn(cha, damage));
	}

	public void doSpawnAntaBreathPoisonClude(L1Character cha, int targetX,
			int targetY) {
		GeneralThreadPool.getInstance().execute(
				new PoisonAntaBreathCludeSpawn(cha, targetX, targetY));
	}

	class PoisonCludeSpawn implements Runnable {

		private L1Character cha;
		private boolean normal = false;

		public PoisonCludeSpawn(L1Character _cha) {
			cha = _cha;
		}

		public PoisonCludeSpawn(L1Character _cha, boolean _normal) {
			cha = _cha;
			normal = _normal;
		}

		@Override
		public void run() {
			try {

				L1Npc firewall = NpcTable.getInstance().getTemplate(100394);
				int duration = 7;

				if (firewall == null) {
					throw new NullPointerException(
							"PoisonClude data not found:npcid=100394");
				}

				Random _rnd = new Random(System.nanoTime());
				ArrayList<L1Object> list = L1World.getInstance()
						.getVisibleObjects(cha);
				// long time = System.currentTimeMillis();
				// int count = 12;
				int count = 40;
				if (cha instanceof L1MonsterInstance) {
					L1MonsterInstance monn = (L1MonsterInstance) cha;
					if (monn.getNpcId() == 100584)
						count = 60;
					else if (monn.getNpcId() == 100588
							|| monn.getNpcId() == 100589)
						count = 80;
				}
				for (int i = 0; i < count; i++) {
					// int tx = (-4 + _rnd.nextInt(9));
					// int ty = (-4 + _rnd.nextInt(9));
					// for(int o = 0; o < count; o++){
					// int x = cha.getX() + (-6 + i);
					// int y = cha.getY() + (-6 + o);
					int x = cha.getX() + (-8 + _rnd.nextInt(16));
					int y = cha.getY() + (-8 + _rnd.nextInt(16));
					/*
					 * L1Map map =
					 * L1WorldMap.getInstance().getMap(cha.getMapId());
					 * 
					 * if (!map.isArrowPassable(x, y,
					 * cha.getMoveState().getHeading())) { continue; }
					 */
					L1EffectInstance effect = spawnEffect(100394,
							duration * 1000, x, y, cha.getMapId());
					if (effect == null) {
						break;
					}
					effect.PoisonDamageStrength = normal;
					L1EffectInstance npc = null;
					for (L1Object objects : list) {
						if (objects.getX() != x || objects.getY() != y)
							continue;

						if (objects instanceof L1EffectInstance) {
							npc = (L1EffectInstance) objects;
							if (npc.getNpcTemplate().get_npcId() == 100394) {
								npc.deleteMe();
								break;
							}
						} else if (objects instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) objects;
							if (cha instanceof L1MonsterInstance) {
								L1MonsterInstance monn = (L1MonsterInstance) cha;
								if (monn.getNpcId() == 100338
										|| monn.getNpcId() == 100584
										|| monn.getNpcId() == 100588
										|| monn.getNpcId() == 100589)
									continue;
							}
							new L1SkillUse().handleCommands(pc, 40, pc.getId(),
									pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
						}
					}
					if (cha instanceof L1MonsterInstance) {
						effect.setPoisonCludeMon((L1MonsterInstance) cha);
					}
					// }
				}
				// System.out.println(System.currentTimeMillis() - time);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class PoisonAntaBreathCludeSpawn implements Runnable {

		private L1Character cha;
		private int targetX;
		private int targetY;

		public PoisonAntaBreathCludeSpawn(L1Character _cha, int x, int y) {
			cha = _cha;
			targetX = x;
			targetY = y;
		}

		@Override
		public void run() {
			try {

				L1Npc firewall = NpcTable.getInstance().getTemplate(100394);
				int duration = 7;

				if (firewall == null) {
					throw new NullPointerException(
							"PoisonClude data not found:npcid=100394");
				}
				int heading = CharPosUtil
						.targetDirection(cha, targetX, targetY);
				int headingRotate[] = { 6, 7, 0, 1, 2, 3, 4, 5 };
				double cosSita = Math.cos(headingRotate[heading] * Math.PI / 4);
				double sinSita = Math.sin(headingRotate[heading] * Math.PI / 4);
				ArrayList<L1Object> list = L1World.getInstance()
						.getVisibleObjects(cha);
				int count = 14;
				for (int i = 0; i < count; i++) {
					for (int o = 0; o < count; o++) {

						if (o % 2 == 1)
							continue;

						int x = cha.getX() + (-7 + i);
						int y = cha.getY() + (-7 + o);

						int x1 = x - cha.getX();
						int y1 = y - cha.getY();

						int distance = Math.max(Math.abs(cha.getX() - x),
								Math.abs(cha.getY() - y));
						if (distance <= 2)
							continue;
						int tileCount = (Math.abs(cha.getX() - x) + Math
								.abs(cha.getY() - y));
						if (tileCount >= 10)
							continue;

						// Z축회전시키고 각도를 0번으로 한다.
						int rotX = (int) Math
								.round(x1 * cosSita + y1 * sinSita);
						int rotY = (int) Math.round(-x1 * sinSita + y1
								* cosSita);
						int xmin = 0;
						int xmax = 5;
						if (heading == 1 || heading == 5)
							xmax = 7;
						int ymin = -7;
						int ymax = 7;

						if (!(rotX > xmin && rotX <= xmax && rotY >= ymin && rotY < ymax)) {
							continue;
						}

						L1EffectInstance effect = spawnEffect(100394,
								duration * 1000, x, y, cha.getMapId());
						if (effect == null) {
							break;
						}
						L1EffectInstance npc = null;
						for (L1Object objects : list) {
							if (objects.getX() != x || objects.getY() != y)
								continue;
							if (objects instanceof L1EffectInstance) {
								npc = (L1EffectInstance) objects;
								if (npc.getNpcTemplate().get_npcId() == 100394) {
									npc.deleteMe();
									break;
								}
							} else if (objects instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) objects;
								if (cha instanceof L1MonsterInstance) {
									L1MonsterInstance monn = (L1MonsterInstance) cha;
									if (monn.getNpcId() == 100338)
										continue;
								}
								new L1SkillUse().handleCommands(pc, 40,
										pc.getId(), pc.getX(), pc.getY(), null,
										0, L1SkillUse.TYPE_GMBUFF);
							}
						}
						if (cha instanceof L1MonsterInstance) {
							effect.setPoisonCludeMon((L1MonsterInstance) cha);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
