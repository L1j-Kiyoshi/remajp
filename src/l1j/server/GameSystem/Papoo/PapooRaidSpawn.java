package l1j.server.GameSystem.Papoo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.ActionCodes;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class PapooRaidSpawn {
	private static Logger _log = Logger.getLogger(PapooRaidSpawn.class
			.getName());
	private static PapooRaidSpawn _instance;

	public static PapooRaidSpawn getInstance() {
		if (_instance == null) {
			_instance = new PapooRaidSpawn();
		}
		return _instance;
	}

	private PapooRaidSpawn() {

	}

	public void fillSpawnTable(int mapid, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_fafurion");
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
							if (fi.getNpcId() == 4038202) {
								fi.setDirection(1);
								fi.setLeftEdgeLocation(fi.getY() - 3);
								fi.setRightEdgeLocation(fi.getY() + 3);
							} else {
								fi.setDirection(0);
								fi.setLeftEdgeLocation(fi.getX() - 3);
								fi.setRightEdgeLocation(fi.getX() + 3);
							}
							fi.setOpenStatus(ActionCodes.ACTION_Close);
							fi.isPassibleDoor(false);
							fi.setPassable(1);
						}

						if (field.getNpcId() == 4039012
								|| field.getNpcId() == 4039013) {
							PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
									mapid);
							ar.AddMon(field);
						}

						L1World.getInstance().storeObject(field);
						L1World.getInstance().addVisibleObject(field);
						if (field.getNpcId() == 4039000
								|| field.getNpcId() == 4039006
								|| field.getNpcId() == 4039007) {
							PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
									mapid);
							ar.setPaPoo(field);
						}

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
	}

}
