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

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.MiniGame.DeathMatch;
import l1j.server.GameSystem.Robot.Robot_Location;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1LittleBugInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1ScarecrowInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.types.Point;

public class L1Spawn {
	private static Logger _log = Logger.getLogger(L1Spawn.class.getName());
	private final L1Npc _template;

	private int _id; // just to find this in the spawn table
	private String _location;
	private int _maximumCount;
	private int _npcid;
	private int _groupId;
	private int _locx;
	private int _locy;
	private int _randomx;
	private int _randomy;
	private int _locx1;
	private int _locy1;
	private int _locx2;
	private int _locy2;
	private int _heading;
	private int _minRespawnDelay;
	private int _maxRespawnDelay;
	private final Constructor<?> _constructor;
	private short _mapid;
	private boolean _respaenScreen;
	private int _movementDistance;
	private boolean _rest;
	private int _spawnType;
	private int _delayInterval;
	private HashMap<Integer, Point> _homePoint = null;
	private boolean _initSpawn = false;
	private boolean _spawnHomePoint;
	private static Random _random = new Random(System.nanoTime());
	private String _name;
	private static final int SPAWN_TYPE_PC_AROUND = 1;

	// private static final int PC_AROUND_DISTANCE = 30;

	private class SpawnTask implements Runnable {
		private int _spawnNumber;
		private int _objectId;

		private SpawnTask(int spawnNumber, int objectId) {
			_spawnNumber = spawnNumber;
			_objectId = objectId;
		}

		@Override
		public void run() {
			try {
				doSpawn(_spawnNumber, _objectId);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public L1Spawn(L1Npc mobTemplate) throws SecurityException,
			ClassNotFoundException {
		_template = mobTemplate;
		String implementationName = _template.getImpl();
		_constructor = Class.forName(
				"l1j.server.server.model.Instance." + implementationName
						+ "Instance").getConstructors()[0];
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	public short getMapId() {
		return _mapid;
	}

	public void setMapId(short _mapid) {
		this._mapid = _mapid;
	}

	public boolean isRespawnScreen() {
		return _respaenScreen;
	}

	public void setRespawnScreen(boolean flag) {
		_respaenScreen = flag;
	}

	public int getMovementDistance() {
		return _movementDistance;
	}

	public void setMovementDistance(int i) {
		_movementDistance = i;
	}

	public int getAmount() {
		return _maximumCount;
	}

	public void setAmount(int amount) {
		_maximumCount = amount;
	}

	public int getGroupId() {
		return _groupId;
	}

	public void setGroupId(int i) {
		_groupId = i;
	}

	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public String getLocation() {
		return _location;
	}

	public void setLocation(String location) {
		_location = location;
	}

	public int getLocX() {
		return _locx;
	}

	public void setLocX(int locx) {
		_locx = locx;
	}

	public int getLocY() {
		return _locy;
	}

	public void setLocY(int locy) {
		_locy = locy;
	}

	public int getNpcId() {
		return _npcid;
	}

	public void setNpcid(int npcid) {
		_npcid = npcid;
	}

	public int getHeading() {
		return _heading;
	}

	public void setHeading(int heading) {
		_heading = heading;
	}

	public int getRandomx() {
		return _randomx;
	}

	public void setRandomx(int randomx) {
		_randomx = randomx;
	}

	public int getRandomy() {
		return _randomy;
	}

	public void setRandomy(int randomy) {
		_randomy = randomy;
	}

	public int getLocX1() {
		return _locx1;
	}

	public void setLocX1(int locx1) {
		_locx1 = locx1;
	}

	public int getLocY1() {
		return _locy1;
	}

	public void setLocY1(int locy1) {
		_locy1 = locy1;
	}

	public int getLocX2() {
		return _locx2;
	}

	public void setLocX2(int locx2) {
		_locx2 = locx2;
	}

	public int getLocY2() {
		return _locy2;
	}

	public void setLocY2(int locy2) {
		_locy2 = locy2;
	}

	public int getMinRespawnDelay() {
		return _minRespawnDelay;
	}

	public void setMinRespawnDelay(int i) {
		_minRespawnDelay = i;
	}

	public int getMaxRespawnDelay() {
		return _maxRespawnDelay;
	}

	public void setMaxRespawnDelay(int i) {
		_maxRespawnDelay = i;
	}

	private int calcRespawnDelay() {
		int respawnDelay = _minRespawnDelay * 1000;
		if (_delayInterval > 0) {
			respawnDelay += _random.nextInt(_delayInterval) * 1000;
		}
		return respawnDelay;
	}

	public void executeSpawnTask(int spawnNumber, int objectId) {
		SpawnTask task = new SpawnTask(spawnNumber, objectId);
		GeneralThreadPool.getInstance().schedule(task, calcRespawnDelay());
	}

	public void initReload() {
		_delayInterval = _maxRespawnDelay - _minRespawnDelay;
	}

	public void init() {
		_delayInterval = _maxRespawnDelay - _minRespawnDelay;
		_initSpawn = true;
		if (Config.SPAWN_HOME_POINT
				&& Config.SPAWN_HOME_POINT_COUNT <= getAmount()
				&& Config.SPAWN_HOME_POINT_DELAY >= getMinRespawnDelay()
				&& isAreaSpawn()) {
			_spawnHomePoint = true;
			_homePoint = new HashMap<Integer, Point>();
		}

		int spawnNum = 0;

		while (spawnNum < _maximumCount) {
			doSpawn(++spawnNum);
		}
		_initSpawn = false;
	}

	protected void doSpawn(int spawnNumber) {
		doSpawn(spawnNumber, 0);
	}

	/**
	 * @param spawnNumber
	 * @param objectId
	 */
	/**
	 * @param spawnNumber
	 * @param objectId
	 */
	/**
	 * @param spawnNumber
	 * @param objectId
	 */
	private L1Location mapRandomXY(int mapid) {
		L1Location loc = new L1Location();
		loc.setMap(mapid);
		loc.set(loc.getMap().getX(), loc.getMap().getY());
		loc = L1Location.randomRangeLocation(loc, loc.getMap().getWidth(), loc
				.getMap().getHeight(), false);
		return loc;
	}

	private static final int[] gfxlist = { 7522, 7525, 7528, 7531 };

	protected void doSpawn(int spawnNumber, int objectId) {
		L1NpcInstance mob = null;
		try {
			Object parameters[] = { _template };

			int newlocx = getLocX();
			int newlocy = getLocY();

			int mapid = getMapId();

			if (!_initSpawn) {
				boolean isNight = GameTimeClock.getInstance().getGameTime()
						.isNight();
				if (!isNight) {// !isNight
					if (mapid == 0 && (newlocx >= 32525 && newlocx <= 32711)
							&& (newlocy >= 32796 && newlocy <= 32873)) {
						// System.out.println("밤 아니라서 스폰안된다.");
						return;
					}
					if (mapid == 0 && (newlocx >= 32462 && newlocx <= 32560)
							&& (newlocy >= 32784 && newlocy <= 32863)) {
						// System.out.println("밤 아니라서 스폰안된다.");
						return;
					}
				}/*
				 * else{ if(mapid == 0 && (newlocx >= 32525 && newlocx <= 32711)
				 * && (newlocy >= 32796 && newlocy <= 32873)){
				 * System.out.println("지금은 밤이다 스폰된다."); } if(mapid == 0 &&
				 * (newlocx >= 32462 && newlocx <= 32560) && (newlocy >= 32784
				 * && newlocy <= 32863)){ System.out.println("지금은 밤이다 스폰된다."); }
				 * }
				 */
			}

			int tryCount = 0;
			mob = (L1NpcInstance) _constructor.newInstance(parameters);
			if (objectId == 0) {
				mob.setId(ObjectIdFactory.getInstance().nextId());
			} else {
				mob.setId(objectId);
			}

			int heading = 5;
			if (0 <= getHeading() && getHeading() <= 7) {
				heading = getHeading();
			}
			mob.getMoveState().setHeading(heading);

			int npcId = mob.getNpcTemplate().get_npcId();

			if (npcId == 45488 && getMapId() == 9) {
				mob.setMap((short) (getMapId() + _random.nextInt(2)));
			} else if (npcId == 45601 && getMapId() == 11) {
				mob.setMap((short) (getMapId() + _random.nextInt(3)));
			} else {
				mob.setMap(getMapId());
			}

			if (npcId == 100106) {
				mob.getGfxId().setTempCharGfx(
						gfxlist[_random.nextInt(gfxlist.length)]);
			}

			mob.setMovementDistance(getMovementDistance());
			mob.setRest(isRest());
			ArrayList<L1PcInstance> players = null;
			L1PcInstance pc = null;
			L1Location loc = null;
			Point pt = null;

			/*
			 * 오만 Die Location Spawn 처리 if(getMapId() >= 101 && getMapId() <=
			 * 200){ setSpawnType(1); }
			 */

			while (tryCount <= 50) {
				switch (getSpawnType()) {
				case SPAWN_TYPE_PC_AROUND:
					if (!_initSpawn) {
						players = new ArrayList<L1PcInstance>();
						for (L1PcInstance _pc : L1World.getInstance()
								.getAllPlayers()) {
							if (getMapId() == _pc.getMapId()) {
								players.add(_pc);
							}
						}
						if (players.size() > 0) {
							pc = players.get(_random.nextInt(players.size()));
							loc = pc.getLocation()
									.randomLocation(21, 25, false);
							newlocx = loc.getX();
							newlocy = loc.getY();
							players.clear();
							break;
						}
					}
				default:
					if (isAreaSpawn()) {
						if (!_initSpawn && _spawnHomePoint) {
							pt = _homePoint.get(spawnNumber);
							loc = new L1Location(pt, getMapId())
									.randomLocation(
											Config.SPAWN_HOME_POINT_RANGE,
											false);
							newlocx = loc.getX();
							newlocy = loc.getY();
						} else {
							int rangeX = getLocX2() - getLocX1();
							int rangeY = getLocY2() - getLocY1();
							newlocx = _random.nextInt(rangeX) + getLocX1();
							newlocy = _random.nextInt(rangeY) + getLocY1();
						}
						if (tryCount > 50) {
							newlocx = getLocX();
							newlocy = getLocY();
						}
					} else if (isRandomSpawn()) {
						newlocx = (getLocX() + ((int) (Math.random() * getRandomx()) - (int) (Math
								.random() * getRandomx())));
						newlocy = (getLocY() + ((int) (Math.random() * getRandomy()) - (int) (Math
								.random() * getRandomy())));
					} else {
						newlocx = getLocX();
						newlocy = getLocY();
					}
				}

				mob.setX(newlocx);
				mob.setHomeX(newlocx);
				mob.setY(newlocy);
				mob.setHomeY(newlocy);

				if (npcId >= 101017 && npcId <= 101022) {
					L1Location locc = mapRandomXY(mob.getMapId());
					mob.setX(locc.getX());
					mob.setY(locc.getY());
					mob.setHomeX(locc.getX());
					mob.setHomeY(locc.getY());
				}

				if (mob.getMapId() == 4
						&& ((mob.getX() >= 33333 && mob.getX() <= 33336
								&& mob.getY() >= 32430 && mob.getY() <= 32441)
								|| (mob.getX() >= 33261 && mob.getX() <= 33264
										&& mob.getY() >= 32396 && mob.getY() <= 32407) || (mob
								.getX() >= 33390
								&& mob.getX() <= 33394
								&& mob.getY() >= 32339 && mob.getY() <= 32350))) {
					tryCount++;
					continue;
				}
				if ((mob.getX() >= 32936 && mob.getY() <= 32945)
						&& (mob.getY() >= 32861 && mob.getY() <= 32870)
						&& mob.getMapId() == 410) {
					tryCount++;
					continue;
				}
				if (mob instanceof L1MonsterInstance) {
					if ((mob.getX() >= 32802 && mob.getY() <= 32818)
							&& (mob.getY() >= 32791 && mob.getY() <= 32815)
							&& mob.getMapId() == 813) {
						tryCount++;
						continue;
					}
				}
				if (mob.getMap().isInMap(mob.getLocation())
						&& mob.getMap().isPassable(mob.getLocation())) {
					break; // 화면내에 유저가 있어도 무조건 스폰되게

					/*
					 * if (mob instanceof L1MonsterInstance) {
					 * 
					 * if (isRespawnScreen()) { break; } L1MonsterInstance
					 * mobtemp = (L1MonsterInstance) mob; if
					 * (L1World.getInstance().getVisiblePlayer(mobtemp).size()
					 * == 0) { break; } if((getMapId()>=53&&getMapId()<=56)
					 * ||(getMapId()>=271&&getMapId()<=278) ){ break; }
					 * 
					 * SpawnTask task = new SpawnTask(spawnNumber, mob.getId());
					 * GeneralThreadPool.getInstance().schedule(task, 3000L);
					 * return; }
					 */
				}
				tryCount++;
			}

			if (tryCount > 50) {
				mob.setX(getLocX());
				mob.setHomeX(getLocX());
				mob.setY(getLocY());
				mob.setHomeY(getLocY());
			}

			if (mob instanceof L1MonsterInstance) {
				((L1MonsterInstance) mob).initHide();
			}
			mob.setSpawn(this);
			mob.setRespawn(true);
			mob.setSpawnNumber(spawnNumber);

			if (_initSpawn && _spawnHomePoint) {
				pt = new Point(mob.getX(), mob.getY());
				_homePoint.put(spawnNumber, pt);
			}

			if (mob instanceof L1MonsterInstance) {
				if (mob.getMapId() == 666) {
					((L1MonsterInstance) mob).set_storeDroped((byte) 0);
				}
			}
			if (mob instanceof L1CastleGuardInstance) {
				((L1CastleGuardInstance) mob).default_heading = mob
						.getMoveState().getHeading();
			}
			doCrystalCave(npcId);
			doAntCaveCloseDoor(getId());

			/**
			 * 광역으로 스폰되는 몹중 홈포인트 업는 몹들 몬스터 : 저주받은허수아비, 붉은오크, 버그베어(추석이벤트용)
			 */
			if (mob.getNpcId() == 45166 || mob.getNpcId() == 4035000
					|| mob.getNpcId() == 4030002 || mob.getNpcId() == 100388) {
				_homePoint.remove(spawnNumber);
				_spawnHomePoint = false;
				if (mob.getMap().isSafetyZone(newlocx, newlocy)
						|| L1CastleLocation
								.checkInAllWarArea(mob.getLocation())) {
					if (mob.getNpcId() == 100388) {
						SpawnTask task = new SpawnTask(spawnNumber, mob.getId());
						GeneralThreadPool.getInstance().schedule(task, 30000L);
					}
					return;
				}
			}

			/**보스멘트띠우기**/ 
		/*
			if (npcId == 45617 || npcId == 45618 || npcId == 45752 || npcId == 45673 || npcId == 477201 || 
					npcId == 45600 || npcId == 45513 
					|| npcId == 45547 || npcId == 45606 || npcId == 45650 || npcId == 45672 || npcId == 45653 
					|| npcId == 45652 || npcId == 45654
					|| npcId == 42120119 || npcId == 42120125 || npcId == 42120132 || npcId == 42120138 || npcId == 42120145
					|| npcId == 42120150 || npcId == 42120155 || npcId == 4220000 || npcId == 42120165 
					|| npcId == 99065 || npcId == 99037 || npcId == 990372 || npcId == 990371 || npcId == 460062
					|| npcId == 99085 || npcId == 99086 || npcId == 454691 || npcId == 99087
					|| npcId == 42120170 || npcId == 42120181 || npcId == 99087
				|| npcId == 42120160 || npcId == 40178 || npcId == 9414){/*피닉스,발록,그림리퍼,흑장로
			L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE,"["+getName()+"]가(이) 스폰되었습니다."));
			L1World.getInstance().broadcastPacketToAll(new S_SystemMessage("\\fY["+getName()+"]가(이) 스폰되었습니다."));
			}
			
			*/
			
		
			
			

			if (objectId == 0) {
				if ((mob.getMapId() >= 7 && mob.getMapId() <= 13)
						|| (mob.getMapId() >= 25 && mob.getMapId() <= 28)
						|| (mob.getMapId() == 430 || mob.getMapId() == 420)
						|| (mob.getMapId() >= 30 && mob.getMapId() <= 36)
						|| (mob.getMapId() == 63)
						|| (mob.getMapId() == 558)
						|| (mob.getMapId() >= 43 && mob.getMapId() <= 51)
						|| (mob.getMapId() == 70)
						|| (mob.getMapId() == 244)
						|| (mob.getMapId() >= 53 && mob.getMapId() <= 56)
						|| (mob.getMapId() >= 307 && mob.getMapId() <= 309)
						|| (mob.getMapId() == 303)
						|| (mob.getMapId() == 400 && mob.getX() >= 32720
								&& mob.getX() <= 32860 && mob.getY() >= 32871 && mob
								.getY() <= 33005)
						|| (mob.getMapId() >= 778 && mob.getMapId() <= 779)
						|| (mob.getMapId() == 777)
						|| (mob.getMapId() >= 451 && mob.getMapId() <= 456) // 라던1층
						|| (mob.getMapId() >= 460 && mob.getMapId() <= 466) // 라던2층
						|| (mob.getMapId() >= 470 && mob.getMapId() <= 478) // 라던3층
				) {
					Robot_Location.로케이션등록(mob.getX(), mob.getY(),
							mob.getMapId());
				}
				if (npcId == 80086) {
					DeathMatch.kusan = mob;
				} else if (npcId == 80087) {
					DeathMatch.datoo = mob;
				}
				if (mob.getMapId() == 4) {
					if (mob instanceof L1ScarecrowInstance) {
						Robot_Location.로케이션등록(mob.getX(), mob.getY(), 4);
					}
					if (npcId == 70042 || npcId == 70035 || npcId == 70041) {
						if (mob instanceof L1NpcInstance) {
							if (!(mob instanceof L1LittleBugInstance)) {
								Robot_Location.로케이션등록(mob.getX(), mob.getY(),
										7626);
							}
						}
					}
				}
			}
			/*
			 * if(mob.getMapId() == 2228){ System.out.println("2228 스폰완료"); }
			 */
			L1World.getInstance().storeObject(mob);
			L1World.getInstance().addVisibleObject(mob);
			if (mob instanceof L1MonsterInstance) {
				L1MonsterInstance mobtemp = (L1MonsterInstance) mob;
				if (!_initSpawn && mobtemp.getHiddenStatus() == 0) {
					mobtemp.onNpcAI();
				}
			}
			if (getGroupId() != 0) {
				L1MobGroupSpawn.getInstance().doSpawn(mob, getGroupId(),
						isRespawnScreen(), _initSpawn);
			}
			if (npcId == 90000 || npcId == 90002 || npcId == 90009
					|| npcId == 90013 || npcId == 90016) {
				mob.broadcastPacket(new S_NPCPack(mob), true);
				mob.broadcastPacket(new S_DoActionGFX(mob.getId(), 4), true);
			}
			/*if (npcId == 910056 || npcId == 910021
					|| npcId == 910028 || npcId == 910036 || npcId == 910042 || npcId == 910050
					|| npcId == 910056 || npcId == 910062 || npcId == 910069|| npcId == 910075
					|| npcId == 910014 || npcId == 910103 || npcId == 910104  || npcId == 45516|| npcId == 45617 || npcId == 100339) {
				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(mob)) {
					mob.onPerceive(visiblePc);
					visiblePc.sendPackets(new S_DoActionGFX(mob.getId(), 4));
				}
			}*/
		
			
			if (npcId == 100716) {// 대 흑장로 스폰시 주변 텔시키기
				for (L1Object obj : L1World.getInstance().getVisibleObjects(
						mob, 3)) {
					if (obj instanceof L1PcInstance) {
						L1PcInstance tpc = (L1PcInstance) obj;
						L1Teleport.randomTeleport(tpc, true);
					}
				}
			}
			if (npcId == 100847) {
				GameList.setbdeath(mob);
			}
			if (npcId == 100848) {
				GameList.setwdeath(mob);
			}

			mob.getLight().turnOnOffLight();
			mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
		} catch (Exception e) {
			System.out.println("엔피씨아이디: " + mob.getNpcId());
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public void setRest(boolean flag) {
		_rest = flag;
	}

	public boolean isRest() {
		return _rest;
	}

	private int getSpawnType() {
		return _spawnType;
	}

	public void setSpawnType(int type) {
		_spawnType = type;
	}

	private boolean isAreaSpawn() {
		return getLocX1() != 0 && getLocY1() != 0 && getLocX2() != 0
				&& getLocY2() != 0;
	}

	private boolean isRandomSpawn() {
		return getRandomx() != 0 || getRandomy() != 0;
	}

	private static final int[] antEggWareHouse1 = { 7200, 7201, 7202, 7203,
			7204, 7205, 7206 };
	private static final int[] antEggWareHouse2 = { 7300, 7301, 7302, 7303,
			7304, 7305, 7306 };
	private static final int[] antCave4F_1 = { 7510, 7511, 7512, 7513, 7514,
			7515, 7516, 7517 };
	private static final int[] antCave4F_2 = { 7520, 7521, 7522, 7523, 7524,
			7525, 7526 };
	private static final int[] antCave4F_3 = { 7530, 7531, 7532, 7533, 7534,
			7535, 7536 };
	private static final int[] antCave4F_4 = { 7540, 7541, 7542, 7543, 7544,
			7545, 7546 };
	private static final int[] antCave4F_5 = { 7550, 7551, 7552, 7553, 7554,
			7555, 7556 };

	public static void doAntCaveCloseDoor(int spawnId) {
		switch (spawnId) {
		case 54100001:
			closeDoorCaveArray(antEggWareHouse1);
			break;
		case 54200001:
			closeDoorCaveArray(antEggWareHouse2);
			break;
		case 54300001:
			closeDoorCaveArray(antCave4F_1);
			break;
		case 54300002:
			closeDoorCaveArray(antCave4F_2);
			break;
		case 54300003:
			closeDoorCaveArray(antCave4F_3);
			break;
		case 54300004:
			closeDoorCaveArray(antCave4F_4);
			break;
		case 54300005:
			closeDoorCaveArray(antCave4F_5);
			break;
		default:
			break;
		}
	}

	public static void doCrystalCave(int npcId) {
		switch (npcId) {
		case 46143:
			closeDoorCave(5000);
			break;
		case 46144:
			closeDoorCave(5001);
			break;
		case 46145:
			closeDoorCave(5002);
			break;
		case 46146:
			closeDoorCave(5003);
			break;
		case 46147:
			closeDoorCave(5004);
			break;
		case 46148:
			closeDoorCave(5005);
			break;
		case 46149:
			closeDoorCave(5006);
			break;
		case 46150:
			closeDoorCave(5007);
			break;
		case 46151:
			closeDoorCave(5008);
			break;
		case 46152:
			closeDoorCave(5009);
			break;
		default:
			break;
		}
	}

	private static void closeDoorCaveArray(int[] doorId) {
		L1DoorInstance door = null;
		for (int i = 0, a = doorId.length; i < a; i++) {
			for (L1Object obj : L1World.getInstance().getObject()) {
				if (obj instanceof L1DoorInstance) {
					door = (L1DoorInstance) obj;
					if (door.getDoorId() == doorId[i]) {
						if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
							door.close();
						}
					}
				}

			}
		}
	}

	private static void closeDoorCave(int doorId) {
		L1DoorInstance door = null;
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance) object;
				if (door.getDoorId() == doorId) {
					door.close();
				}
			}
		}
	}
}
