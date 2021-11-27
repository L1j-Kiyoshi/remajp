package l1j.server.Warehouse;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class SupplementaryService extends Warehouse {
	private static final long serialVersionUID = 1L;
	protected static Logger _log = Logger.getLogger(SupplementaryService.class
			.getName());

	public SupplementaryService(String an) {
		super(an);
	}

	@Override
	protected int getMax() {
		return Config.MAX_PERSONAL_WAREHOUSE_ITEM;
	}

	@SuppressWarnings("resource")
	@Override
	public synchronized boolean checkitem(int itemid, int count) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT count FROM character_supplementary_service WHERE account_name = ? AND item_id = ?");
			pstm.setString(1, getName());
			pstm.setInt(2, itemid);
			rs = pstm.executeQuery();
			while (rs.next()) {
				if (count <= rs.getInt("count")) {
					return true;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	@Override
	public synchronized void loadItems() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM character_supplementary_service WHERE account_name = ?");
			pstm.setString(1, getName());
			rs = pstm.executeQuery();
			L1ItemInstance item = null;
			L1Item itemTemplate = null;
			while (rs.next()) {
				itemTemplate = ItemTable.getInstance().getTemplate(
						rs.getInt("item_id"));
				item = ItemTable.getInstance().FunctionItem(itemTemplate);
				int objectId = rs.getInt("id");
				item.setId(objectId);
				item.setItem(itemTemplate);
				item.setCount(rs.getInt("count"));
				item.setEquipped(false);
				item.setEnchantLevel(rs.getInt("enchantlvl"));
				item.setIdentified(rs.getInt("is_id") != 0 ? true : false);
				item.set_durability(rs.getInt("durability"));
				item.setChargeCount(rs.getInt("charge_count"));
				item.setRemainingTime(rs.getInt("remaining_time"));
				item.setLastUsed(rs.getTimestamp("last_used"));
				item.setAttrEnchantLevel(rs.getInt("attr_enchantlvl"));
				item.setStepEnchantLevel(rs.getInt("step_enchantlvl"));
				item.setBless(rs.getInt("bless"));
				item.setSecondId(rs.getInt("second_id"));
				item.setRoundId(rs.getInt("round_id"));
				item.setTicketId(rs.getInt("ticket_id"));
				item.setEndTime(rs.getTimestamp("end_time"));
				item.setCreaterName(rs.getString("CreaterName"));
				item.setDemonBongin(rs.getInt("demon_bongin") == 0 ? false
						: true);

				_items.add(item);
				L1World.getInstance().storeObject(item);
			}

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public synchronized void insertItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM character_supplementary_service WHERE id = ?");
			pstm.setInt(1, item.getId());
			rs = pstm.executeQuery();
			pstm.close();
			if (!rs.next()) {
				pstm = con
						.prepareStatement("INSERT INTO character_supplementary_service SET id = ?, account_name = ?, item_id = ?, item_name = ?, count = ?, is_equipped=0, enchantlvl = ?, is_id = ?, durability = ?, charge_count = ?, remaining_time = ?, last_used = ?, attr_enchantlvl = ?, step_enchantlvl = ? , bless = ?, second_id=?, round_id=?, ticket_id=?, end_time=?, CreaterName=?, demon_bongin=?");
				pstm.setInt(1, item.getId());
				pstm.setString(2, getName());
				pstm.setInt(3, item.getItemId());
				pstm.setString(4, item.getName());
				pstm.setInt(5, item.getCount());
				pstm.setInt(6, item.getEnchantLevel());
				pstm.setInt(7, item.isIdentified() ? 1 : 0);
				pstm.setInt(8, item.get_durability());
				pstm.setInt(9, item.getChargeCount());
				pstm.setInt(10, item.getRemainingTime());
				pstm.setTimestamp(11, item.getLastUsed());
				pstm.setInt(12, item.getAttrEnchantLevel());
				pstm.setInt(13, item.getStepEnchantLevel());
				pstm.setInt(14, item.getBless());
				pstm.setInt(15, item.getSecondId());
				pstm.setInt(16, item.getRoundId());
				pstm.setInt(17, item.getTicketId());
				pstm.setTimestamp(18, item.getEndTime());
				pstm.setString(19, item.getCreaterName());
				pstm.setInt(20, item.isDemonBongin() ? 1 : 0);
				pstm.executeUpdate();
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public synchronized void updateItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE character_supplementary_service SET count = ? WHERE id = ?");
			pstm.setInt(1, item.getCount());
			pstm.setInt(2, item.getId());
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	@Override
	public synchronized void deleteItem(L1ItemInstance item) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("DELETE FROM character_supplementary_service WHERE id = ?");
			pstm.setInt(1, item.getId());
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_items.remove(_items.indexOf(item));
	}
}
