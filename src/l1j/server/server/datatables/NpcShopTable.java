package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.NpcBuyShop.NpcBuyShop;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class NpcShopTable {
	private static Logger _log = Logger.getLogger(NpcShopTable.class.getName());

	private static NpcShopTable _instance;

	private final Map<Integer, L1Shop> _npcShops = new HashMap<Integer, L1Shop>();

	public static NpcShopTable getInstance() {
		if (_instance == null) {
			_instance = new NpcShopTable();
		}
		return _instance;
	}

	public void relodingac() {
		L1Shop shop = null;
		L1NpcShopInstance npc = null;
		for (int npcId : enumNpcIds()) {
			try {
				shop = get(npcId);
				npc = L1World.getInstance().getNpcShop(npcId);
				if (npc == null) {
					continue;
				}
				NpcBuyShop.getInstance().상점아이템삭제(npcId);
				shop = NpcShopTable.getInstance().get(npcId);
				for (L1ShopItem shopitem : shop.getSellingItems()) {
					NpcBuyShop.getInstance().SaveShop(npc, shopitem,
							shopitem.getPrice(), shopitem.getCount());
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void reloding() {
		NpcShopTable oldInstance = _instance;
		_instance = new NpcShopTable();
		oldInstance._npcShops.clear();
	}

	private NpcShopTable() {
		loadShops();
	}

	private ArrayList<Integer> enumNpcIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT DISTINCT npc_id FROM shop_npc_org");
			rs = pstm.executeQuery();
			while (rs.next()) {
				ids.add(rs.getInt("npc_id"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return ids;
	}

	private L1Shop loadShop(int npcId, ResultSet rs) throws SQLException {
		List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
		List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
		L1ShopItem item = null;
		while (rs.next()) {
			int itemId = rs.getInt("item_id");
			int sellingPrice = rs.getInt("selling_price");
			int purchasingPrice = rs.getInt("purchasing_price");
			int count = rs.getInt("count");
			int enchant = rs.getInt("enchant");
			if (0 <= sellingPrice) {
				int objid = ObjectIdFactory.getInstance().nextId();
				L1Item temp = ItemTable.getInstance().getTemplate(itemId);
				item = new L1ShopItem(itemId, sellingPrice, 1, enchant,
						temp.getBless(), objid);
				item.setCount(count);
				sellingList.add(item);
			}
			if (0 <= purchasingPrice) {
				int objid = ObjectIdFactory.getInstance().nextId();
				L1Item temp = ItemTable.getInstance().getTemplate(itemId);
				item = new L1ShopItem(itemId, purchasingPrice, 1, enchant,
						temp.getBless(), objid);
				purchasingList.add(item);
			}
		}
		return new L1Shop(npcId, sellingList, purchasingList);
	}

	private void loadShops() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM shop_npc_org WHERE npc_id=?");
			L1Shop shop = null;
			for (int npcId : enumNpcIds()) {
				pstm.setInt(1, npcId);
				try {
					rs = pstm.executeQuery();
					shop = loadShop(npcId, rs);
					_npcShops.put(npcId, shop);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					SQLUtil.close(rs);
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void loadSellShops(int npcId) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM shop_npc_org WHERE npc_id=?");
			L1Shop shop = null;
			pstm.setInt(1, npcId);
			rs = pstm.executeQuery();
			shop = loadShop(npcId, rs);
			_npcShops.put(npcId, shop);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public L1Shop get(int npcId) {
		return _npcShops.get(npcId);
	}
}
