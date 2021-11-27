package l1j.server.GameSystem.IceQeen;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.GameList;
import l1j.server.server.ActionCodes;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class IceQeenSpawn {
	private static IceQeenSpawn _instance;

	public static IceQeenSpawn getInstance() {
		if (_instance == null) {
			_instance = new IceQeenSpawn();
		}
		return _instance;
	}

	private IceQeenSpawn() {
	}

	public void Spawn(int mapid, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_IceQeen");
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;

				L1Npc l1npc = NpcTable.getInstance().getTemplate(
						rs.getInt("npc_id"));
				if (l1npc != null) {
					L1NpcInstance field;
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

						if (field instanceof L1DoorInstance) {
							L1DoorInstance fi = (L1DoorInstance) field;
							fi.setDoorId(rs.getInt("npc_id"));
							if (fi.getDoorId() == 4040003) {
								fi.setDirection(0);
								fi.setLeftEdgeLocation(fi.getX() - 3);
								fi.setRightEdgeLocation(fi.getX() + 3);
							} else if (fi.getDoorId() >= 4040000
									&& fi.getDoorId() <= 4040004) {
								fi.setDirection(1);
								fi.setLeftEdgeLocation(fi.getY() - 3);
								fi.setRightEdgeLocation(fi.getY() + 3);
							}

							fi.setOpenStatus(ActionCodes.ACTION_Close);
							fi.isPassibleDoor(false);
							fi.setPassable(1);

							for (L1Object obj : L1World.getInstance()
									.getVisibleObjects(mapid).values()) {
								if (obj instanceof L1DoorInstance) {
									L1DoorInstance door = (L1DoorInstance) obj;
									if (door.getX() == fi.getX() && door.getY() == fi.getY()) {
										fi.isPassibleDoor(true);
										fi.setPassable(0);
										fi.deleteMe();
									}
								}
							}

							synchronized (GameList.IQList) {
								IceQeen IQ = GameList.getIQ(mapid);
								if (IQ != null)
									IQ.AddDoor(fi);
							}

						}

						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);

					} catch (Exception e) {
					}
				}
			}
		} catch (SQLException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

}
