package l1j.server.GameSystem.NpcBuyShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class NpcBuyShopSell {

	private static Logger _log = Logger.getLogger(NpcBuyShopSell.class
			.getName());

	private static NpcBuyShopSell _instance;

	public static NpcBuyShopSell getInstance() {
		if (_instance == null) {
			_instance = new NpcBuyShopSell();
		}
		return _instance;
	}

	private NpcBuyShopSell() {
	}

	public FastMap<Integer, L1Shop> load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		FastMap<Integer, L1Shop> itemlist = new FastMap<Integer, L1Shop>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npc_shop_sell");
			rs = pstm.executeQuery();
			/*
			 * for(int i = 8110000; i <= 8110003; i++){
			 * NpcBuyShop.getInstance().add_2(i, 40100, 0, 0, null, 1); }
			 */
			while (rs.next()) {
				try {
					int id = rs.getInt("npc_id");
					int itemid = rs.getInt("item_id");
					byte enchant = rs.getByte("enchant");
					byte attr = rs.getByte("attr");
					Timestamp time = rs.getTimestamp("deletetime");
					byte bless = rs.getByte("bless");
					NpcBuyShop.getInstance().add(id, itemid, enchant, attr,
							time, bless);
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
		return itemlist;
	}

	public void storeItem(int npcid, String name, L1ShopItem item)
			throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO npc_shop_sell SET npc_id=?, npc_name=?, item_id=?, item_name=?, enchant=?, attr=?, deletetime=? , bless=?");
			pstm.setInt(1, npcid);
			pstm.setString(2, name);
			pstm.setInt(3, item.getItemId());
			pstm.setString(4, item.getItem().getName());
			pstm.setInt(5, item.getEnchant());
			pstm.setInt(6, item.getAttr());
			pstm.setTimestamp(7, item.getDeleteTime());
			pstm.setInt(8, item.getBless());
			pstm.executeUpdate();
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void deleteItem(int npcid, L1ShopItem item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int num = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (item.getDeleteTime() == null) {
				pstm = con
						.prepareStatement("SELECT * FROM npc_shop_sell WHERE npc_id=? AND item_id=? AND enchant=? AND attr=?");
				pstm.setInt(1, npcid);
				pstm.setInt(2, item.getItemId());
				pstm.setInt(3, item.getEnchant());
				pstm.setInt(4, item.getAttr());
			} else {
				pstm = con
						.prepareStatement("SELECT * FROM npc_shop_sell WHERE npc_id=? AND item_id=? AND enchant=? AND attr=? AND deletetime=?");
				pstm.setInt(1, npcid);
				pstm.setInt(2, item.getItemId());
				pstm.setInt(3, item.getEnchant());
				pstm.setInt(4, item.getAttr());
				pstm.setTimestamp(5, item.getDeleteTime());
			}
			rs = pstm.executeQuery();
			if (rs.next()) {
				pstm.close();
				num = rs.getInt("id");
				pstm = con.prepareStatement("DELETE FROM npc_shop_sell WHERE id = ?");
				pstm.setInt(1, num);
				pstm.executeUpdate();
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public boolean serchItem(int npcid, L1ShopItem item) throws Exception {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			if (item.getDeleteTime() == null) {
				pstm = con
						.prepareStatement("SELECT * FROM npc_shop_sell WHERE npc_id=? AND item_id=? AND enchant=? AND attr=?");
				pstm.setInt(1, npcid);
				pstm.setInt(2, item.getItemId());
				pstm.setInt(3, item.getEnchant());
				pstm.setInt(4, item.getAttr());
			} else {
				pstm = con
						.prepareStatement("SELECT * FROM npc_shop_sell WHERE npc_id=? AND item_id=? AND enchant=? AND attr=? AND deletetime=?");
				pstm.setInt(1, npcid);
				pstm.setInt(2, item.getItemId());
				pstm.setInt(3, item.getEnchant());
				pstm.setInt(4, item.getAttr());
				pstm.setTimestamp(5, item.getDeleteTime());
			}
			rs = pstm.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			throw e;
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}
}
