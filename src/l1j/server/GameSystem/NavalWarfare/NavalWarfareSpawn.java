package l1j.server.GameSystem.NavalWarfare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class NavalWarfareSpawn {

	private static Logger _log = Logger.getLogger(NavalWarfareSpawn.class
			.getName());
	private static final Random _rnd = new Random(System.nanoTime());

	private static NavalWarfareSpawn _instance;

	public static NavalWarfareSpawn getInstance() {
		if (_instance == null) {
			_instance = new NavalWarfareSpawn();
		}
		return _instance;
	}

	public void spawn(int mapid, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_naval");
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
						field.setHomeX(field.getX());
						field.setHomeY(field.getY());
						field.getMoveState().setHeading(rs.getInt("heading"));
						field.setLightSize(l1npc.getLightSize());
						field.getLight().turnOnOffLight();
						field.setSpawnLocation(rs.getString("location"));
						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);

					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
				l1npc = null;
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
	}

	public FastTable<L1NpcInstance> spawnList(int mapid, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		FastTable<L1NpcInstance> list = null;
		list = new FastTable<L1NpcInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_naval");
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
						int x = rs.getInt("locx");
						int y = rs.getInt("locy");
						if (type == 1) {
							x = 32790 + _rnd.nextInt(9);
							y = 32798 + _rnd.nextInt(10);
						}
						field.setX(x);
						field.setY(y);
						int tryCount = 0;
						if (type != 0) {
							do {
								tryCount++;
								field.setX(field.getX()
										+ ((int) (Math.random() * 3) - (int) (Math
												.random() * 3)));
								field.setY(field.getY()
										+ ((int) (Math.random() * 3) - (int) (Math
												.random() * 3)));
								if (field.getMap().isInMap(field.getLocation())
										&& field.getMap().isPassable(
												field.getLocation())) {
									break;
								}
							} while (tryCount < 50);
						}
						if (tryCount >= 50) {
							field.setX(x);
							field.setY(y);
						}
						field.setMap((short) mapid);
						field.setHomeX(field.getX());
						field.setHomeY(field.getY());
						field.getMoveState().setHeading(rs.getInt("heading"));
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

	public FastTable<L1NpcInstance> monster_spawn(int mapid, int type,
			boolean level) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;

		FastTable<L1NpcInstance> list = null;
		/*
		 * if(RT){ list = new FastTable<L1NpcInstance>(); }
		 */
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_naval");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;
				l1npc = NpcTable.getInstance().getTemplate(rs.getInt("npc_id"));
				if (l1npc != null) {
					try {
						int count = rs.getInt("count");
						int randomCount = count > 1 ? _rnd.nextInt(count) + 1
								: 1;
						if (l1npc.get_npcId() == 100155
								|| l1npc.get_npcId() == 100156)
							randomCount = 3;
						for (int i = 0; i < randomCount; i++) {
							if (level) {
								field = NpcTable.getInstance().newNpcInstance(
										rs.getInt("npc_id") + 1000000);
							} else {
								field = NpcTable.getInstance().newNpcInstance(
										rs.getInt("npc_id"));
							}

							field.setId(ObjectIdFactory.getInstance().nextId());
							int x = 32789 + _rnd.nextInt(10);
							int y = 32797 + _rnd.nextInt(11);
							field.setX(x);
							field.setY(y);
							int tryCount = 0;
							if (type != 0) {
								do {
									tryCount++;
									field.setX(field.getX()
											+ ((int) (Math.random() * 3) - (int) (Math
													.random() * 3)));
									field.setY(field.getY()
											+ ((int) (Math.random() * 3) - (int) (Math
													.random() * 3)));
									if (field.getMap().isInMap(
											field.getLocation())
											&& field.getMap().isPassable(
													field.getLocation())) {
										break;
									}
								} while (tryCount < 50);
							}
							if (tryCount >= 50) {
								field.setX(x);
								field.setY(y);
							}

							field.setMap((short) mapid);
							field.setHomeX(field.getX());
							field.setHomeY(field.getY());
							field.getMoveState().setHeading(_rnd.nextInt(8));
							field.setLightSize(l1npc.getLightSize());
							field.getLight().turnOnOffLight();
							field.setSpawnLocation(rs.getString("location"));
							L1World.getInstance().storeObject(field);
							L1World.getInstance().addVisibleObject(field);
							/*
							 * if(RT){ Broadcaster.broadcastPacket(field, new
							 * S_SkillSound(field.getId(), 7930), true);
							 * list.add(field); }
							 */
						}
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
				l1npc = null;
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
