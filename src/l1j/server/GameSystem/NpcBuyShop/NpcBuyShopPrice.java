package l1j.server.GameSystem.NpcBuyShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class NpcBuyShopPrice {
	private static Logger _log = Logger.getLogger(NpcBuyShopPrice.class
			.getName());

	private static NpcBuyShopPrice _instance;

	public static NpcBuyShopPrice getInstance() {
		if (_instance == null) {
			_instance = new NpcBuyShopPrice();
		}
		return _instance;
	}

	private NpcBuyShopPrice() {
	}

	public FastTable<L1ShopItem> load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		FastTable<L1ShopItem> itemlist = new FastTable<L1ShopItem>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM npc_shop_buy_price");
			rs = pstm.executeQuery();
			while (rs.next()) {
				try {
					int id = rs.getInt("item_id");
					// String name = rs.getString("item_name");
					byte enchant = rs.getByte("enchant");
					int buy_price = rs.getInt("buy_price");
					int sell_price = rs.getInt("sell_price");
					String msg = rs.getString("msg");
					for (int i = 0; i < 13; i++) {
						int sellp = 0;
						int buyp = 0;
						switch (i) {
						case 1:
						case 4:
						case 7:
						case 10:
							sellp = (int) (sell_price * 0.01);
							buyp = (int) (buy_price * 0.01);
							break;
						case 2:
						case 5:
						case 8:
						case 11:
							sellp = (int) (sell_price * 0.02);
							buyp = (int) (buy_price * 0.02);
							break;
						case 3:
						case 6:
						case 9:
						case 12:
							sellp = (int) (sell_price * 0.03);
							buyp = (int) (buy_price * 0.03);
							break;
						default:
							break;
						}
						if (sell_price > 1000000000)
							sell_price = 1000000000;
						if (buy_price > 1000000000)
							buy_price = 1000000000;
						// System.out.println("sell1 >> "+(sell_price+sellp)
						// +" buy >> "+(buy_price+buyp));
						int objid = ObjectIdFactory.getInstance().nextId();
						L1Item temp = ItemTable.getInstance().getTemplate(id);
						L1ShopItem item = new L1ShopItem(id,
								sell_price + sellp, buy_price + buyp, 1,
								enchant, i, msg, temp.getBless(), objid);
						itemlist.add(item);
					}
					for (int i = 33; i < 41; i++) {
						int sellp = 0;
						int buyp = 0;
						switch (i) {
						case 33:
						case 35:
						case 37:
						case 39:
							sellp = (int) (sell_price * 0.05);
							buyp = (int) (buy_price * 0.05);
							break;
						case 34:
						case 36:
						case 38:
						case 40:
							sellp = (int) (sell_price * 0.10);
							buyp = (int) (buy_price * 0.10);
							break;
						default:
							break;
						}
						if (sell_price > 1000000000)
							sell_price = 1000000000;
						if (buy_price > 1000000000)
							buy_price = 1000000000;
						// System.out.println("sell2 >> "+(sell_price+sellp)
						// +" buy >> "+(buy_price+buyp));
						int objid = ObjectIdFactory.getInstance().nextId();
						L1Item temp = ItemTable.getInstance().getTemplate(id);
						L1ShopItem item = new L1ShopItem(id,
								sell_price + sellp, buy_price + buyp, 1,
								enchant, i, msg, temp.getBless(), objid);
						itemlist.add(item);
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
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM npc_shop_buy_price_user");
			rs = pstm.executeQuery();
			while (rs.next()) {
				try {
					int npcid = rs.getInt("npcid");
					if (npcid == 0)
						continue;
					int id = rs.getInt("item_id");
					byte enchant = rs.getByte("enchant");
					int buy_price = rs.getInt("buy_price");
					int sell_price = rs.getInt("sell_price");
					String msg = rs.getString("msg");
					for (int i = 0; i < 13; i++) {
						int sellp = 0;
						int buyp = 0;
						switch (i) {
						case 1:
						case 4:
						case 7:
						case 10:
							sellp = (int) (sell_price * 0.01);
							buyp = (int) (buy_price * 0.01);
							break;
						case 2:
						case 5:
						case 8:
						case 11:
							sellp = (int) (sell_price * 0.02);
							buyp = (int) (buy_price * 0.02);
							break;
						case 3:
						case 6:
						case 9:
						case 12:
							sellp = (int) (sell_price * 0.03);
							buyp = (int) (buy_price * 0.03);
							break;
						default:
							break;
						}
						if (sell_price > 1000000000)
							sell_price = 1000000000;
						if (buy_price > 1000000000)
							buy_price = 1000000000;
						int objid = ObjectIdFactory.getInstance().nextId();
						L1Item temp = ItemTable.getInstance().getTemplate(id);
						L1ShopItem item = new L1ShopItem(npcid, id, sell_price
								+ sellp, buy_price + buyp, 1, enchant, i, msg,
								temp.getBless(), objid);
						itemlist.add(item);
					}
					for (int i = 33; i < 41; i++) {
						int sellp = 0;
						int buyp = 0;
						switch (i) {
						case 33:
						case 35:
						case 37:
						case 39:
							sellp = (int) (sell_price * 0.05);
							buyp = (int) (buy_price * 0.05);
							break;
						case 34:
						case 36:
						case 38:
						case 40:
							sellp = (int) (sell_price * 0.10);
							buyp = (int) (buy_price * 0.10);
							break;
						default:
							break;
						}
						if (sell_price > 1000000000)
							sell_price = 1000000000;
						if (buy_price > 1000000000)
							buy_price = 1000000000;
						int objid = ObjectIdFactory.getInstance().nextId();
						L1Item temp = ItemTable.getInstance().getTemplate(id);
						L1ShopItem item = new L1ShopItem(npcid, id, sell_price
								+ sellp, buy_price + buyp, 1, enchant, i, msg,
								temp.getBless(), objid);
						itemlist.add(item);
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
		return itemlist;
	}

}
