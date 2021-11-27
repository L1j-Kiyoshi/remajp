/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.Antaras.AntarasRaid;
import l1j.server.GameSystem.Antaras.AntarasRaidSystem;
import l1j.server.GameSystem.IceQeen.IceQeen;
import l1j.server.GameSystem.Papoo.PaPooRaid;
import l1j.server.GameSystem.Papoo.PaPooRaidSystem;
import l1j.server.server.ActionCodes;
import l1j.server.server.IdFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ArrowInstance;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.templates.L1Npc;

public class L1SpawnUtil {
	private static Logger _log = Logger.getLogger(L1SpawnUtil.class.getName());

	public static void spawn(L1PcInstance pc, int npcId, int randomRange,
			int timeMillisToDelete, boolean isUsePainwand) {
		try {

			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(ObjectIdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());
			/*
			 * if (randomRange == 0) { randomRange = 3; }
			 */
			if (randomRange == 0) {
				npc.getLocation().set(pc.getLocation());
				npc.getLocation().forward(pc.getMoveState().getHeading());
				if (npc.getMap().isInMap(npc.getLocation())
						&& npc.getMap().isPassable(npc.getLocation())) {
				} else {
					npc.getLocation().set(pc.getLocation());
				}
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(pc.getX() + (int) (Math.random() * randomRange)
							- (int) (Math.random() * randomRange));
					npc.setY(pc.getY() + (int) (Math.random() * randomRange)
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
					npc.getLocation().set(pc.getLocation());
					npc.getLocation().forward(pc.getMoveState().getHeading());
					if (npc.getMap().isInMap(npc.getLocation())
							&& npc.getMap().isPassable(npc.getLocation())) {
					} else {
						npc.getLocation().set(pc.getLocation());
					}
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(pc.getMoveState().getHeading());
			if (npc instanceof L1CastleGuardInstance) {
				((L1CastleGuardInstance) npc).default_heading = pc
						.getMoveState().getHeading();
			}
			if (isUsePainwand) {
				if (npc instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) npc;
					mon.set_storeDroped((byte) 2);
				}
			}

			/*
			 * if(npc instanceof L1DoorInstance){ L1DoorInstance fi =
			 * (L1DoorInstance) npc; System.out.println("123123123");
			 * fi.setDirection(1); fi.setLeftEdgeLocation(fi.getY()-3);
			 * fi.setRightEdgeLocation(fi.getY()+3);
			 * 
			 * fi.setOpenStatus( ActionCodes.ACTION_Close);
			 * fi.isPassibleDoor(false); fi.setPassable(1); }
			 */

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			if (npcId == 45000172) {
				npc.broadcastPacket(new S_NPCPack(npc), true);
				npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 11), true);
			}

			/*if (npcId == 90000 || npcId == 90002 || npcId == 90009
					|| npcId == 90013 || npcId == 90016 || npcId == 910056 || npcId == 910021
					|| npcId == 910028 || npcId == 910036 || npcId == 910042 || npcId == 910050
					|| npcId == 910056 || npcId == 910062 || npcId == 910069|| npcId == 910075
					|| npcId == 910014 || npcId == 910103 || npcId == 910104  || npcId == 45516|| npcId == 45617 || npcId == 100339 || npcId == 910117) {
				npc.broadcastPacket(new S_NPCPack(npc), true);
				npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 4), true);
			}*/

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {

				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc,
						timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	/**
	 * 화살을 스폰한다
	 */

	public static FastTable<L1ArrowInstance> ArrowSpawn() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1ArrowInstance field = null;
		FastTable<L1ArrowInstance> list = null;
		list = new FastTable<L1ArrowInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_Arrow");
			rs = pstm.executeQuery();
			while (rs.next()) {
				l1npc = NpcTable.getInstance().getTemplate(rs.getInt("npc_id"));
				if (l1npc != null) {
					try {
						field = (L1ArrowInstance) NpcTable.getInstance()
								.newNpcInstance(rs.getInt("npc_id"));
						field.setId(ObjectIdFactory.getInstance().nextId());
						field.setX(rs.getInt("locx"));
						field.setY(rs.getInt("locy"));
						field.setTarX(rs.getInt("tarx"));
						field.setTarY(rs.getInt("tary"));
						field.setMap((short) rs.getInt("mapid"));
						field.setLightSize(l1npc.getLightSize());
						field.getLight().turnOnOffLight();

						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);
						int delay = rs.getInt("start_delay");
						if (delay == 0)
							field.setAction(true);
						list.add(field);
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (IllegalArgumentException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return list;
	}
	
	public static void spawn6(int x, int y, short map, int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(IdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x,y,map);
				npc.getLocation().forward(5);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange) - (int) (Math.random() * randomRange));
					if (npc.getMap(). isInMap(npc.getLocation()) && npc.getMap(). isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);

				if (tryCount >= 50) {
					npc.getLocation().set(x,y,map);
					npc.getLocation().forward(5);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.setHeading(5);

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc); 
			
			//예시 스폰액션
			if (npcId == 910056 || npcId == 910021
					|| npcId == 910028 || npcId == 910036 || npcId == 910042 || npcId == 910050
					|| npcId == 910056 || npcId == 910062 || npcId == 910069|| npcId == 910075
					|| npcId == 910014 || npcId == 910103 || npcId == 910104  || npcId == 45516|| npcId == 45617 || npcId == 100339 || npcId == 910117) {
				for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc)) {
					npc.onPerceive(visiblePc);
					visiblePc.sendPackets(new S_DoActionGFX(npc.getId(), 4));
				}
			}

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc, timeMillisToDelete);				
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public static L1NpcInstance spawn2(int x, int y, short map, int npcId,
			int randomRange, int timeMillisToDelete, int movemap) {
		int heading = 5;
		if (npcId == 7000044 || npcId == 100325 || npcId == 100326
				|| (npcId == 100213 && x == 33094 && y == 33401)
				|| npcId == 100563 || npcId == 100646 || npcId == 100692)
			heading = 6;
		else if (npcId == 100430 || npcId == 100709 || npcId == 100710)
			heading = 4;
		return spawn4(x, y, map, heading, npcId, randomRange,
				timeMillisToDelete, movemap, false);
	}

	public static L1NpcInstance spawn2(int x, int y, short map, int npcId,
			int randomRange, int timeMillisToDelete, int movemap, boolean level) {
		int heading = 5;
		if (npcId == 7000044 || npcId == 100325 || npcId == 100326
				|| (npcId == 100213 && x == 33094 && y == 33401)
				|| npcId == 100563 || npcId == 100646 || npcId == 100692)
			heading = 6;
		else if (npcId == 100430 || npcId == 100709 || npcId == 100710)
			heading = 4;
		return spawn4(x, y, map, heading, npcId, randomRange,
				timeMillisToDelete, movemap, level);
	}

	public static L1NpcInstance spawn4(int x, int y, short map, int heading,
			int npcId, int randomRange, int timeMillisToDelete, int movemap) {
		return spawn4(x, y, map, heading, npcId, randomRange,
				timeMillisToDelete, movemap, false);
	}

	public static L1NpcInstance spawn4(int x, int y, short map, int heading,
			int npcId, int randomRange, int timeMillisToDelete, int movemap,
			boolean level) {
		L1NpcInstance npc = null;
		try {
			if (level) {
				npc = NpcTable.getInstance().newNpcInstance(npcId + 1000000);
			} else {
				npc = NpcTable.getInstance().newNpcInstance(npcId);
			}

			npc.setId(ObjectIdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
				/**
				 * 용땅도포함~~~ 하딘 관련 NPC가 아닐 경우에만 적용 일단 주석 처리 해봄.
				 **/
				/*
				 * if(npcId != 4212013 && !(npcId >= 5000038 && npcId <=
				 * 5000093)) npc.getLocation().forward(5);
				 */
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange)
							- (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange)
							- (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation())
							&& npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().set(x, y, map);
					// npc.getLocation().forward(5);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(heading);
			if (npcId == 4500101 || npcId == 4500102 || npcId == 4500103 || npcId == 4212015 || npcId == 4212016 || npcId == 4500107
					|| npcId == 100011 || npcId == 910008 || npcId == 4038060) {
				L1FieldObjectInstance fobj = (L1FieldObjectInstance) npc;
				fobj.setMoveMapId(movemap);
			}
			if (npc.getNpcId() == 5000091) {
				L1DoorInstance door = (L1DoorInstance) npc;
				door.setDoorId(npc.getNpcTemplate().get_npcId());
				door.setDirection(0);
				door.setLeftEdgeLocation(door.getX());
				door.setRightEdgeLocation(door.getX());

				door.setOpenStatus(ActionCodes.ACTION_Close);
				door.isPassibleDoor(false);
				door.setPassable(1);
				// door.setOpenStatus(ActionCodes.ACTION_Close);
				// door.isPassibleDoor(false);
				// door.setPassable(L1DoorInstance.PASS);
			}

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);
			// 안타라스의 경우 튀어 나오게끔..
			if (npcId == 4212015 || npcId == 4212016 || npcId == 4038000 || npcId == 910008
					|| npcId == 4200010 || npcId == 4200011 || npcId == 4039000  || npcId == 145684
					|| npcId == 4039006 || npcId == 4039007 || npcId == 100011 || npcId == 145685 || npcId == 145686) { // 안타
																					// 파푸
				// npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 11));
				// npc.setActionStatus(11);
				npc.broadcastPacket(new S_NPCPack(npc), true);
				npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 11), true);
				// npc.setActionStatus(3);
				// npc.broadcastPacket(new S_NPCPack(npc));
				// npc.broadcastPacket(new S_NPCPack(npc));
			} else if (npcId == 100586 || npcId == 100587) {
				npc.broadcastPacket(new S_NPCPack(npc), true);
				npc.broadcastPacket(new S_DoActionGFX(npc.getId(), 4), true);
			}
			if (npc.getNpcId() == 4038000 || npc.getNpcId() == 4200010
					|| npc.getNpcId() == 4200011) {
				AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(map);
				ar.setanta(npc);
			}
			if (npc.getNpcId() == 4039000 || npc.getNpcId() == 4039006
					|| npc.getNpcId() == 4039007) {
				PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(map);
				ar.setPaPoo(npc);
			}

			if (map >= 2101 && map <= 2151) {
				synchronized (GameList.IQList) {
					IceQeen IQ = GameList.getIQ(map);
					if (IQ != null)
						IQ.AddMon(npc);
				}
			}

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc,
						timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return npc;
	}

	public static L1NpcInstance spawn5(int x, int y, short map, int heading,
			int npcId, int randomRange, int timeMillisToDelete, L1Clan clan) {
		L1NpcInstance npc = null;
		try {
			npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(ObjectIdFactory.getInstance().nextId());
			npc.setMap(map);
			if (randomRange == 0) {
				npc.getLocation().set(x, y, map);
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(x + (int) (Math.random() * randomRange)
							- (int) (Math.random() * randomRange));
					npc.setY(y + (int) (Math.random() * randomRange)
							- (int) (Math.random() * randomRange));
					if (npc.getMap().isInMap(npc.getLocation())
							&& npc.getMap().isPassable(npc.getLocation())) {
						break;
					}
					Thread.sleep(1);
				} while (tryCount < 50);
				if (tryCount >= 50) {
					npc.getLocation().set(x, y, map);
				}
			}

			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(heading);
			if (npc instanceof L1MerchantInstance) {
				L1MerchantInstance mer = (L1MerchantInstance) npc;
				mer.setNameId(clan.getClanName());
				mer.setClanid(clan.getClanId());
				mer.setClanname(clan.getClanName());
			}

			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc,
						timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return npc;
	}

	/**
	 * 보스를 스폰한다
	 * 
	 * @param x
	 * @param y
	 * @param map
	 * @param npcId
	 * @param randomRange
	 * @param timeMillisToDelete
	 * @param movemap
	 *            (이동시킬 맵을 설정한다 - 안타레이드)
	 */
	public static void logofbossTime(L1NpcInstance npc) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO log_Boss_Time SET Name=?, Npc_ID=?, x=?, y=?, map=?, Spawn_Time=?");
			pstm.setString(1, npc.getName());
			pstm.setInt(2, npc.getNpcTemplate().get_npcId());
			pstm.setInt(3, npc.getX());
			pstm.setInt(4, npc.getY());
			pstm.setInt(5, npc.getMapId());
			Calendar cal = Calendar.getInstance();
			pstm.setTimestamp(6, new Timestamp(cal.getTimeInMillis()));
			pstm.executeUpdate();
		} catch (SQLException e) {
		} catch (Exception e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}


	public static void spawn3(L1NpcInstance pc, int npcId, int randomRange,
			int timeMillisToDelete, boolean isUsePainwand) {
		try {
			L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(npcId);
			npc.setId(ObjectIdFactory.getInstance().nextId());
			npc.setMap(pc.getMapId());

			if (randomRange == 0) {
				npc.getLocation().set(pc.getLocation());
				// npc.getLocation().forward(pc.getMoveState().getHeading());
			} else {
				int tryCount = 0;
				do {
					tryCount++;
					npc.setX(pc.getX() + (int) (Math.random() * randomRange)
							- (int) (Math.random() * randomRange));
					npc.setY(pc.getY() + (int) (Math.random() * randomRange)
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
					npc.getLocation().set(pc.getLocation());
					// npc.getLocation().forward(pc.getMoveState().getHeading());
				}
			}
			npc.setHomeX(npc.getX());
			npc.setHomeY(npc.getY());
			npc.getMoveState().setHeading(pc.getMoveState().getHeading());
			if (isUsePainwand) {
				if (npc instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) npc;
					mon.set_storeDroped((byte) 2);
				}
			}
			if (npcId == 4039004) {
				PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
						pc.getMapId());
				ar.셋사엘(npc);
			}
			if (npcId == 4039005) {
				PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
						pc.getMapId());
				ar.셋사엘2(npc);
			}
			L1World.getInstance().storeObject(npc);
			L1World.getInstance().addVisibleObject(npc);

			npc.getLight().turnOnOffLight();
			npc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE); // 채팅 개시
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(npc,
						timeMillisToDelete);
				timer.begin();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}
}
