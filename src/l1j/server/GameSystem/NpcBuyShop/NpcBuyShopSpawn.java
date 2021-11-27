package l1j.server.GameSystem.NpcBuyShop;

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
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class NpcBuyShopSpawn {
	private static Logger _log = Logger.getLogger(NpcBuyShopSpawn.class
			.getName());

	private static NpcBuyShopSpawn _instance;

	public static NpcBuyShopSpawn getInstance() {
		if (_instance == null) {
			_instance = new NpcBuyShopSpawn();
		}
		return _instance;
	}

	private FastTable<L1NpcShopInstance> list = new FastTable<L1NpcShopInstance>();

	private NpcBuyShopSpawn() {
	}

	private static final int gfxlist[] = { 11322 /* 11326<-이전 이미지 영자상점 이미지
												 * , 11427, 10047, 9688, 11322,
												 * 10069, 10034, 10032
												 */};

	public FastTable<L1NpcShopInstance> load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		FastTable<L1NpcShopInstance> npcShoplist = new FastTable<L1NpcShopInstance>();
		Random _rnd = new Random(System.nanoTime());
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npc_shop_spawnlist");
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1Npc l1npc = NpcTable.getInstance().getTemplate(
						rs.getInt("npc_id"));
				if (l1npc != null) {
					try {
						L1NpcShopInstance npc = (L1NpcShopInstance) NpcTable
								.getInstance().newNpcInstance(
										rs.getInt("npc_id"));
						npc.setId(ObjectIdFactory.getInstance().nextId());
						npc.setName(rs.getString("name"));
						npc.setNameId(npc.getName());
						int type = rs.getByte("type");
						npc.setX(rs.getInt("locx"));
						npc.setY(rs.getInt("locy"));
						npc.setMap(rs.getShort("mapid"));
						npc.getMoveState().setHeading(rs.getInt("heading"));
						npc.setLightSize(l1npc.getLightSize());
						npc.getLight().turnOnOffLight();
						npc.setTitle(rs.getString("title"));
						if (type == 1) {
							npc.getGfxId().setTempCharGfx(
									gfxlist[_rnd.nextInt(gfxlist.length)]);
							npc.setShopName(rs.getString("shop_name"));
							npc.setDefaultName(npc.getShopName());
							// int law = _rnd.nextInt(32767);
							npc.setLawful(l1npc.get_lawful());
							npc.setTempLawful(l1npc.get_lawful());
						} else {
							if (type == 3)
								npc.setShopName(rs.getString("shop_name"));
							NpcChatThread.get().add(npc);
							npc.setTempLawful(l1npc.get_lawful());
							npc.setLawful(l1npc.get_lawful());
						}
						npc.setNormalChat(rs.getString("normal_chat"));
						npc.setState(type);
						if (npc.getState() != 3 && npc.getShopName() != null
								&& !npc.getShopName().equalsIgnoreCase("")
								&& !npc.getShopName().equalsIgnoreCase(" ")) {
							npcShoplist.add(npc);
						}
						list.add(npc);
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
			// _rnd = null;
		}
		return npcShoplist;
	}

	public void ShopNameReload() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npc_shop_spawnlist");
			rs = pstm.executeQuery();
			while (rs.next()) {
				try {
					int npcid = rs.getInt("npc_id");
					int type = rs.getByte("type");
					String title = rs.getString("title");
					String normalChat = rs.getString("normal_chat");
					L1NpcShopInstance npc = null;
					for (L1NpcShopInstance ns : list) {
						if (ns.getNpcId() == npcid) {
							npc = ns;
							break;
						}
					}
					if (npc == null)
						continue;
					if (type == 1) {
						npc.setShopName(rs.getString("shop_name"));
						npc.setDefaultName(npc.getShopName());
					} else {
						if (type == 3)
							npc.setShopName(rs.getString("shop_name"));
					}
					if (title != null
							&& !npc.getTitle().equalsIgnoreCase(title)) {
						npc.setTitle(title);
						if (L1World.getInstance().getNpcShop(npc.getName()) != null) {
							Broadcaster.broadcastPacket(npc, new S_CharTitle(
									npc.getId(), title), true);
						}
					}
					if (normalChat != null) {
						npc.setNormalChat(normalChat);
					}

				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
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
