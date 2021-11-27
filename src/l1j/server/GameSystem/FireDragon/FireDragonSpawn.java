package l1j.server.GameSystem.FireDragon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.GameList;
import l1j.server.server.ActionCodes;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class FireDragonSpawn {
	private static FireDragonSpawn _instance;
	private Random random = new Random(System.nanoTime());

	public static FireDragonSpawn getInstance() {
		if (_instance == null) {
			_instance = new FireDragonSpawn();
		}
		return _instance;
	}

	private FireDragonSpawn() {
	}

	public void Spawn(L1PcInstance pc, int mapid, int type) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		L1Npc l1npc = null;
		L1NpcInstance field;
		int rnd = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spawnlist_Fire_Dragon");
			rs = pstm.executeQuery();
			int count = 0;
			int range = 0;
			L1Location temploc = new L1Location();
			L1Location newloc = null;
			FireDragon FD = null;
			synchronized (GameList.FDList) {
				FD = GameList.getFD(mapid);
			}

			if (FD == null) {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
				return;
			}

			int rndd = 0;
			int npcid = 0;
			while (rs.next()) {
				if (type != rs.getInt("type"))
					continue;
				npcid = rs.getInt("npc_id");
				if (npcid == 100852) {
					npcid += random.nextInt(4);
				}
				l1npc = NpcTable.getInstance().getTemplate(npcid);
				if (l1npc != null) {

					try {
						count = rs.getInt("count");
						range = rs.getInt("range");
						if (count >= 1) {
							for (int i = 0; i < count; i++) {
								field = NpcTable.getInstance().newNpcInstance(
										npcid);
								field.setId(ObjectIdFactory.getInstance()
										.nextId());
								temploc.setX(rs.getInt("locx"));
								temploc.setY(rs.getInt("locy"));
								temploc.setMap(mapid);
								if (count > 1) {
									newloc = L1Location.randomLocation(temploc,
											1, range, false);
									field.setX(newloc.getX());
									field.setY(newloc.getY());
								} else {
									field.setX(temploc.getX());
									field.setY(temploc.getY());
								}
								field.setMap((short) mapid);
								field.setHomeX(field.getX());
								field.setHomeY(field.getY());
								field.getMoveState().setHeading(
										rs.getInt("heading"));
								field.setLightSize(l1npc.getLightSize());
								field.getLight().turnOnOffLight();

								if (npcid == 100833 || npcid == 100834
										|| npcid == 100835) {
									if (field instanceof L1DoorInstance) {
										L1DoorInstance fi = (L1DoorInstance) field;
										fi.setDoorId(rs.getInt("npc_id"));
										if (fi.getDoorId() == 100835) {
											fi.setDirection(0);
											fi.setLeftEdgeLocation(fi.getX() - 6);
											fi.setRightEdgeLocation(fi.getX() + 6);
										} else {
											fi.setDirection(1);
											fi.setLeftEdgeLocation(fi.getY() - 6);
											fi.setRightEdgeLocation(fi.getY() + 6);
										}
										fi.setOpenStatus(ActionCodes.ACTION_Close);
										fi.isPassibleDoor(false);
										fi.setPassable(1);
									}
								}

								L1World.getInstance().storeObject(field);
								L1World.getInstance().addVisibleObject(field);
								FD.AddMon(field);
								if (field.getNpcId() == 100848) {
									// L1NpcInstance wdeath =
									// GameList.getwdeath();
									// if(wdeath != null){
									field.Wchat_start(pc);
									// }
								}
								if (type == 6) {
									if (field.getNpcId() == 100858) {
										field.vchat_start(pc);
									}
								}

							}
						}

					} catch (Exception e) {
						e.printStackTrace();
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
