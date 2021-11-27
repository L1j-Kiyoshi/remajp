package l1j.server.GameSystem.NpcTradeShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class NpcTradeShopTable {
	private static Logger _log = Logger.getLogger(NpcTradeShopTable.class
			.getName());

	private static NpcTradeShopTable _instance;

	public static NpcTradeShopTable getInstance() {
		if (_instance == null) {
			_instance = new NpcTradeShopTable();
		}
		return _instance;
	}

	private NpcTradeShopTable() {
	}

	public FastTable<ShopItem> load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		FastTable<ShopItem> list = new FastTable<ShopItem>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npc_trade_sell");
			rs = pstm.executeQuery();
			while (rs.next()) {
				try {
					ShopItem item = new ShopItem();
					item.setNpcId(rs.getInt("npc_id"));
					item.setItemId(rs.getInt("item_id"));
					// String name = rs.getString("item_name");
					item.setEnchant(rs.getByte("enchant"));
					item.setPrice(rs.getInt("price"));
					item.setMsg(rs.getString("msg"));
					item.setX(rs.getInt("x"));
					item.setY(rs.getInt("y"));
					item.setMapId(rs.getShort("mapid"));
					item.setHeading(rs.getByte("heading"));
					String title = rs.getString("title");
					item.setTitle(title == null ? "" : title);
					list.add(item);
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
		return list;
	}

}
