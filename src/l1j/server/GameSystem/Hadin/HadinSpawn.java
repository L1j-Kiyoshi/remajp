/**
 * 본섭 리뉴얼된 Chapter1. 비밀의 마법사 하딘
 * 하딘 맵 관련 스폰 처리 클래스
 * by. 케인
 */
package l1j.server.GameSystem.Hadin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ActionCodes;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class HadinSpawn {
	private static Logger _log = Logger.getLogger(HadinSpawn.class.getName());
	private static HadinSpawn _instance;

	public static HadinSpawn getInstance() {
		if (_instance == null) {
			_instance = new HadinSpawn();
		}
		return _instance;
	}

	private HadinSpawn() {
	}

	public void fillSpawnTable(int mapid, int type) {
		fillSpawnTable(mapid, type, false);
	}

	public FastTable<L1NpcInstance> fillSpawnTable(int mapid, int type,
			boolean RT) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		FastTable<L1NpcInstance> list = null;
		if (RT)
			list = new FastTable<L1NpcInstance>();

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_hadin");
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;
				if (type >= 1 && type <= 3)
					count = 1;
				else if (type == 24 || type == 25)
					count = 3;
				else
					count = 0;
				for (int i = 0; i <= count; i++) {

					l1npc = NpcTable.getInstance().getTemplate(
							rs.getInt("npc_id"));
					if (l1npc != null) {
						try {
							field = NpcTable.getInstance().newNpcInstance(
									rs.getInt("npc_id"));
							field.setId(ObjectIdFactory.getInstance().nextId());
							field.setX(rs.getInt("locx"));
							field.setY(rs.getInt("locy"));
							int tryCount = 0;
							if (type != 0) {
								// int tryCount = 0;
								do {
									tryCount++;
									field.setX(field.getX()
											+ ((int) (Math.random() * 5) - (int) (Math
													.random() * 5)));
									field.setY(field.getY()
											+ ((int) (Math.random() * 5) - (int) (Math
													.random() * 5)));
									if (field.getMap().isInMap(
											field.getLocation())
											&& field.getMap().isPassable(
													field.getLocation())) {
										break;
									}
								} while (tryCount < 50);
							}
							if (tryCount >= 50) {
								field.setX(rs.getInt("locx"));
								field.setY(rs.getInt("locy"));
							}
							field.setMap((short) mapid);
							field.setHomeX(field.getX());
							field.setHomeY(field.getY());
							field.getMoveState().setHeading(
									rs.getInt("heading"));
							field.setLightSize(l1npc.getLightSize());
							field.getLight().turnOnOffLight();
							field.setSpawnLocation(rs.getString("location"));
							if (field instanceof L1DoorInstance) {
								L1DoorInstance fi = (L1DoorInstance) field;
								fi.setDoorId(rs.getInt("npc_id"));
								if (fi.getDoorId() == 5000044
										|| fi.getDoorId() == 5000046) {
									fi.setDirection(0);
									fi.setLeftEdgeLocation(fi.getX());
									fi.setRightEdgeLocation(fi.getX());
								} else if (fi.getDoorId() == 5000045
										|| fi.getDoorId() == 5000047) {
									fi.setDirection(1);
									fi.setLeftEdgeLocation(fi.getY());
									fi.setRightEdgeLocation(fi.getY());
								}
								fi.isPassibleDoor(false);
								fi.setPassable(1);
							}

							L1World.getInstance().storeObject(field);
							L1World.getInstance().addVisibleObject(field);
							if (RT)
								list.add(field);
						} catch (Exception e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						}
					}
					l1npc = null;
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

	public FastTable<L1NpcInstance> fillSpawnTable(int x, int y, int mapid,
			int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		FastTable<L1NpcInstance> list = null;
		list = new FastTable<L1NpcInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_hadin");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;
				for (int i = 0; i <= 1; i++) {

					l1npc = NpcTable.getInstance().getTemplate(
							rs.getInt("npc_id"));
					if (l1npc != null) {
						try {
							field = NpcTable.getInstance().newNpcInstance(
									rs.getInt("npc_id"));
							field.setId(ObjectIdFactory.getInstance().nextId());
							field.setX(x);
							field.setY(y);
							if (type != 0) {
								int tryCount = 0;
								do {
									tryCount++;
									field.setX(field.getX()
											+ ((int) (Math.random() * 2) - (int) (Math
													.random() * 2)));
									field.setY(field.getY()
											+ ((int) (Math.random() * 2) - (int) (Math
													.random() * 2)));

									if (field.getMap().isInMap(
											field.getLocation())
											&& field.getMap().isPassable(
													field.getLocation())) {
										break;
									}
								} while (tryCount < 50);
								if (tryCount >= 50) {
									field.setX(x);
									field.setY(y);
								}
							}
							field.setMap((short) mapid);
							field.setHomeX(field.getX());
							field.setHomeY(field.getY());
							field.getMoveState().setHeading(
									rs.getInt("heading"));
							field.setLightSize(l1npc.getLightSize());
							field.getLight().turnOnOffLight();
							field.setSpawnLocation(rs.getString("location"));
							L1World.getInstance().storeObject(field);
							L1World.getInstance().addVisibleObject(field);
							list.add(field);
						} catch (Exception e) {
							_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
						}
					}
					l1npc = null;
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

	public FastMap<String, L1NpcInstance> fillSpawnTable2(int mapid, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		FastMap<String, L1NpcInstance> list = null;
		list = new FastMap<String, L1NpcInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_hadin");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;
				l1npc = NpcTable.getInstance().getTemplate(rs.getInt("npc_id"));
				if (l1npc != null) {
					try {
						field = NpcTable.getInstance().newNpcInstance(
								rs.getInt("npc_id"));
						field.setId(ObjectIdFactory.getInstance().nextId());
						field.setX(rs.getInt("locx"));
						field.setY(rs.getInt("locy"));
						field.setMap((short) mapid);
						field.setMap(L1WorldMap.getInstance().getMap(
								(short) mapid));
						field.setHomeX(field.getX());
						field.setHomeY(field.getY());
						field.getMoveState().setHeading(rs.getInt("heading"));
						field.setLightSize(l1npc.getLightSize());
						field.getLight().turnOnOffLight();
						field.setSpawnLocation(rs.getString("location"));
						if (field instanceof L1DoorInstance) {
							L1DoorInstance fi = (L1DoorInstance) field;
							fi.setDoorId(rs.getInt("npc_id"));
							fi.setDirection(0);
							fi.setLeftEdgeLocation(fi.getX());
							fi.setRightEdgeLocation(fi.getX());
							fi.isPassibleDoor(false);
							fi.setPassable(1);
							fi.setOpenStatus(ActionCodes.ACTION_Close);
							fi.setDead(false);
						}

						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);
						list.put(field.getSpawnLocation(), field);
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

}
