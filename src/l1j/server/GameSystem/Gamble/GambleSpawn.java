package l1j.server.GameSystem.Gamble;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class GambleSpawn {
	private static Logger _log = Logger.getLogger(GambleSpawn.class.getName());

	// private byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	// private byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
	private FastTable<String> support_npc_name;
	private static GambleSpawn _instance;

	public static GambleSpawn getInstance() {
		if (_instance == null) {
			_instance = new GambleSpawn();
		}
		return _instance;
	}

	private GambleSpawn() {
		support_npc_name = new FastTable<String>();
		load();
	}

	public FastTable<L1NpcShopInstance> load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		FastTable<L1NpcShopInstance> npcShoplist = new FastTable<L1NpcShopInstance>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npc_gamble_spawnlist");
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(
						rs.getInt("npc_id"));
				if (l1npc != null) {
					try {
						GambleInstance npc = (GambleInstance) NpcTable
								.getInstance().newNpcInstance(
										rs.getInt("npc_id"));
						npc.setId(ObjectIdFactory.getInstance().nextId());
						npc.setName(rs.getString("name"));
						npc.setNameId(npc.getName());
						npc.setType(rs.getByte("type"));
						npc.setX(rs.getInt("locx"));
						npc.setY(rs.getInt("locy"));
						npc.setMap(rs.getShort("mapid"));
						npc.getMoveState().setHeading(rs.getInt("heading"));
						npc.setLightSize(l1npc.getLightSize());
						npc.getLight().turnOnOffLight();
						npc.setTitle(rs.getString("title"));
						int gfxid = rs.getInt("gfxid");
						if (gfxid == 0)
							npc.getGfxId().setGfxId(0);
						npc.getGfxId().setTempCharGfx(gfxid);
						if (npc.getGfxId().getTempCharGfx() == 0
								|| npc.getGfxId().getTempCharGfx() == 61)
							npc.setActionStatus(50);
						npc.setLawful(rs.getInt("lawful"));
						npc.setTempLawful(npc.getLawful());
						npc.setChatMsg(rs.getString("chat_msg"));
						npc.setClanid(rs.getInt("clan_id"));
						npc.setClanname(rs.getString("clan_name"));
						support_spawn(con, npc);
						Gamble.get().add(npc);
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
		return npcShoplist;
	}

	/** 소막 타입(1)만 사용하도록 셋팅되어있음 추후 업그레이드시 타입 체크 해줘야됨 **/
	private void support_spawn(Connection con, GambleInstance npc) {
		PreparedStatement pstm2 = null;
		ResultSet rs2 = null;
		try {
			pstm2 = con
					.prepareStatement("SELECT * FROM npc_gamble_support_spawnlist");
			rs2 = pstm2.executeQuery();
			while (rs2.next()) {
				if (npc.wand_npc[5] != null)
					break;
				if (npc.getType() != rs2.getByte("type")
						|| support_npc_name.contains(rs2.getString("name")))
					continue;
				L1Npc l1npc2 = NpcTable.getInstance().getTemplate(
						rs2.getInt("npc_id"));
				if (l1npc2 != null) {
					try {
						GambleInstance npc2 = (GambleInstance) NpcTable
								.getInstance().newNpcInstance(
										rs2.getInt("npc_id"));
						npc2.setId(ObjectIdFactory.getInstance().nextId());
						npc2.setName(rs2.getString("name"));
						npc2.setNameId(npc2.getName());
						npc2.setType(rs2.getByte("type"));
						npc2.setX(rs2.getInt("locx"));
						npc2.setY(rs2.getInt("locy"));
						npc2.setMap(npc.getMapId());
						npc2.getMoveState().setHeading(rs2.getInt("heading"));
						npc2.setLightSize(l1npc2.getLightSize());
						npc2.getLight().turnOnOffLight();
						npc2.setTitle(rs2.getString("title"));
						int gfxid = rs2.getInt("gfxid");
						if (gfxid == 0)
							npc2.getGfxId().setGfxId(0);
						npc2.getGfxId().setTempCharGfx(gfxid);
						if (npc2.getGfxId().getTempCharGfx() == 37
								|| npc2.getGfxId().getTempCharGfx() == 138) {
							npc2.setActionStatus(20);
						} else if (npc2.getGfxId().getTempCharGfx() == 0
								|| npc2.getGfxId().getTempCharGfx() == 61)
							npc2.setActionStatus(50);
						npc2.setLawful(rs2.getInt("lawful"));
						npc2.setTempLawful(npc2.getLawful());
						npc2.setChatMsg(rs2.getString("chat_msg"));
						npc2.setClanid(npc.getClanid());
						npc2.setClanname(npc.getClanname());
						npc2.Npc_trade = true;
						if (npc.wand_npc[0] == null) {
							npc.wand_npc[0] = npc2;
						} else if (npc.wand_npc[1] == null) {
							npc.wand_npc[1] = npc2;
						} else if (npc.wand_npc[2] == null) {
							npc.wand_npc[2] = npc2;
						} else if (npc.wand_npc[3] == null) {
							npc.wand_npc[3] = npc2;
						} else if (npc.wand_npc[4] == null) {
							npc.wand_npc[4] = npc2;
						} else if (npc.wand_npc[5] == null) {
							npc.wand_npc[5] = npc2;
						}
						support_npc_name.add(npc2.getName());
					} catch (Exception e) {
						_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs2);
			SQLUtil.close(pstm2);
		}
	}
}
