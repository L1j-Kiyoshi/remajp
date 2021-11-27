/**
 * 본섭 리뉴얼된 풍룡 린드비오르
 * 린드비오르 맵 관련 스폰 처리 클래스
 * by. 케인
 */
package l1j.server.GameSystem.Lind;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class LindSpawn {
	private static Logger _log = Logger.getLogger(LindSpawn.class.getName());
	private static LindSpawn _instance;
	private Random _rnd = new Random(System.nanoTime());

	public static LindSpawn getInstance() {
		if (_instance == null) {
			_instance = new LindSpawn();
		}
		return _instance;
	}

	private LindSpawn() {
	}

	public FastMap<String, L1NpcInstance> fillSpawnTable(int mapid, int type,
			boolean list_return) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		FastMap<String, L1NpcInstance> list = null;

		if (list_return)
			list = new FastMap<String, L1NpcInstance>();

		int level2_randomck = 0;
		if (type == 2)
			level2_randomck = _rnd.nextInt(2);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_lind");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;
				l1npc = NpcTable.getInstance().getTemplate(rs.getInt("npc_id"));
				if (l1npc != null) {
					try {
						if (type == 2) {
							if (level2_randomck == 0) {
								if (rs.getString("location").equalsIgnoreCase(
										"린드2차 왼쪽"))
									continue;
							} else if (level2_randomck == 1) {
								if (rs.getString("location").equalsIgnoreCase(
										"린드2차 오른쪽"))
									continue;
							}
						}
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
						if (field.getSpawnLocation()
								.equalsIgnoreCase("린드2차 왼쪽")) {
							field.getGfxId().setTempCharGfx(8047);
						} else if (field.getSpawnLocation().equalsIgnoreCase(
								"린드2차 오른쪽")) {
							field.getGfxId().setTempCharGfx(8057);
						}

						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);
						if (field.getNpcId() >= 100012
								&& field.getNpcId() <= 100014) {
							field.broadcastPacket(new S_NPCPack(field), true);
							field.broadcastPacket(
									new S_DoActionGFX(field.getId(), 11), true);
						}
						if (list_return)
							list.put(field.getSpawnLocation(), field);
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

	public L1NpcInstance fillSpawnTable(int mapid, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_lind");
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

						if (field.getNpcId() == 100014)
							field.getMoveState().setMoveSpeed(1);

						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);
						if (field.getNpcId() == 100012
								|| field.getNpcId() == 100014) {
							field.broadcastPacket(new S_NPCPack(field), true);
							field.broadcastPacket(
									new S_DoActionGFX(field.getId(), 11), true);
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

		return field;
	}
}
