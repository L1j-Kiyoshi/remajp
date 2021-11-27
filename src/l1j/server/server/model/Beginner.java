/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.model:
// L1PcInstance

public class Beginner {

	private static Logger _log = Logger.getLogger(Beginner.class.getName());

	private static Beginner _instance;

	public static Beginner getInstance() {
		if (_instance == null) {
			_instance = new Beginner();
		}
		return _instance;
	}

	private Beginner() {
	}

	public void writeBookmark(L1PcInstance pc) {
		Connection c = null;
		PreparedStatement p = null;
		PreparedStatement p1 = null;
		ResultSet r = null;

		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("SELECT * FROM beginner_teleport");
			p1 = c.prepareStatement(
					"INSERT INTO character_teleport SET id = ?, char_id = ?, name = ?, locx = ?, locy = ?, mapid = ?, randomX = ?, randomY = ?");
			r = p.executeQuery();
			while (r.next()) {
				p1.setInt(1, ObjectIdFactory.getInstance().nextId());
				p1.setInt(2, pc.getId());
				p1.setString(3, r.getString("name"));
				p1.setInt(4, r.getInt("locx"));
				p1.setInt(5, r.getInt("locy"));
				p1.setShort(6, r.getShort("mapid"));
				p1.setInt(7, r.getInt("randomX"));
				p1.setInt(8, r.getInt("randomY"));
				p1.executeUpdate();
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, "북마크의 추가로 에러가 발생했습니다.", e);
		} finally {
			SQLUtil.close(r);
			SQLUtil.close(p1);
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
	}

	public int GiveItem(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT * FROM beginner WHERE activate IN(?,?)");

			pstm1.setString(1, "A");
			if (pc.isCrown()) {
				pstm1.setString(2, "P");
			} else if (pc.isKnight()) {
				pstm1.setString(2, "K");
			} else if (pc.isElf()) {
				pstm1.setString(2, "E");
			} else if (pc.isWizard()) {
				pstm1.setString(2, "W");
			} else if (pc.isDarkelf()) {
				pstm1.setString(2, "D");
			} else if (pc.isDragonknight()) {
				pstm1.setString(2, "T");
			} else if (pc.isWarrior()) {
				pstm1.setString(2, "R");
			} else if (pc.isIllusionist()) {
				pstm1.setString(2, "B");
			} else {
				pstm1.setString(2, "A");
			}

			rs = pstm1.executeQuery();
			PreparedStatement pstm2 = null;
			while (rs.next()) {
				try {
					int count = rs.getInt("count");
					if (count <= 0)
						continue;
					pstm2 = con.prepareStatement(
							"INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?, end_time=?");
					pstm2.setInt(1, ObjectIdFactory.getInstance().nextId());
					int itemid = rs.getInt("item_id");
					pstm2.setInt(2, itemid);
					pstm2.setInt(3, pc.getId());
					pstm2.setString(4, rs.getString("item_name"));
					pstm2.setInt(5, count);
					pstm2.setInt(6, 0);
					pstm2.setInt(7, rs.getInt("enchantlvl"));
					pstm2.setInt(8, 1);
					pstm2.setInt(9, 0);
					pstm2.setInt(10, rs.getInt("charge_count"));
					L1Item tempItem = ItemTable.getInstance().getTemplate(itemid);
					if (tempItem.getType2() == 0 && tempItem.getType() == 2) { // light
						pstm2.setInt(11, tempItem.getLightFuel());
					} else
						pstm2.setInt(11, 0);
					pstm2.setTimestamp(12, null);
					pstm2.setInt(13, rs.getInt("bless"));
					pstm2.setInt(14, 0);
					Timestamp deleteTime = null;
					if ((itemid >= 425000 && itemid <= 425002) || // 엘모어 방어구
							(itemid >= 450000 && itemid <= 450007)) { // 엘모어 무기
						deleteTime = new Timestamp(System.currentTimeMillis() + (3600000 * 24 * 3));// 3일
					} else if ((itemid >= 21099 && itemid <= 21112) || itemid == 21254 || itemid == 7 || itemid == 35 || itemid == 48 || itemid == 73
							|| itemid == 105 || itemid == 120 || itemid == 147 || itemid == 7232 || itemid == 156 || itemid == 174 || itemid == 175
							|| itemid == 224) {
						// System.out.println("1111111111111111");
						deleteTime = new Timestamp(System.currentTimeMillis() + (3600000 * 24 * 7));// 3일
					}
					pstm2.setTimestamp(15, deleteTime);
					pstm2.executeUpdate();
				} catch (SQLException e2) {
					_log.log(Level.SEVERE, e2.getLocalizedMessage(), e2);
				} finally {
					SQLUtil.close(pstm2);
				}
			}
		} catch (SQLException e1) {
			_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}
		return 0;
	}

	public void GiveItem(L1PcInstance pc, L1ItemInstance item, boolean eq) {
		Connection con = null;
		PreparedStatement pstm2 = null;
		int itemid = item.getItemId();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm2 = con.prepareStatement(
					"INSERT INTO character_items SET id=?, item_id=?, char_id=?, char_name=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?,  end_time=?");
			pstm2.setInt(1, ObjectIdFactory.getInstance().nextId());
			pstm2.setInt(2, item.getItemId());
			pstm2.setInt(3, pc.getId());

			pstm2.setInt(3, pc.getId());

			pstm2.setString(4, pc.getName());

			pstm2.setString(5, item.getName());
			pstm2.setInt(6, item.getCount());
			pstm2.setInt(7, 0);// eq ? 1 : 0);
			pstm2.setInt(8, item.getEnchantLevel());
			pstm2.setInt(9, 1);
			pstm2.setInt(10, 0);
			pstm2.setInt(11, item.getChargeCount());

			if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light
				pstm2.setInt(12, item.getItem().getLightFuel());
			} else
				pstm2.setInt(12, 0);

			pstm2.setTimestamp(13, null);
			pstm2.setInt(14, item.getBless());
			pstm2.setInt(15, 0);
			Timestamp deleteTime = null;
			if ((itemid >= 21099 && itemid <= 21112) || itemid == 21254 || itemid == 7 || itemid == 35 || itemid == 48 || itemid == 73 || itemid == 105
					|| itemid == 120 || itemid == 147 || itemid == 7232 || itemid == 156 || itemid == 174 || itemid == 175 || itemid == 224) {
				deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24 * 7);// 3일
			}
			pstm2.setTimestamp(16, deleteTime);

			pstm2.executeUpdate();
		} catch (SQLException e2) {
			_log.log(Level.SEVERE, e2.getLocalizedMessage(), e2);
		} finally {
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
	}

	public void GiveTestItem(int type, L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT * FROM beginner_testitem WHERE activate IN(?,?)");
			pstm1.setString(1, "A");
			if (type == 0) {
				pstm1.setString(2, "P");
			} else if (type == 1) {
				pstm1.setString(2, "K");
			} else if (type == 2) {
				pstm1.setString(2, "E");
			} else if (type == 3) {
				pstm1.setString(2, "W");
			} else if (type == 4) {
				pstm1.setString(2, "D");
			} else if (type == 5) {
				pstm1.setString(2, "T");
			} else if (type == 6) {
				pstm1.setString(2, "B");
			} else {
				pstm1.setString(2, "A");
			}
			rs = pstm1.executeQuery();
			while (rs.next()) {
				try {
					// L1ItemInstance item = null;
					// item.setItemId(rs.getInt(2));
					// item.setCount(rs.getInt(3));
					// item.setEnchantLevel(rs.getInt(4));
					// item.setIdentified(false);
					pc.getInventory().storeItem(rs.getInt(2), rs.getInt(3), rs.getInt(4));
					// pc.sendPackets(new S_ServerMessage(403,
					// item.getItem().getNameId()));
				} catch (SQLException e2) {
					_log.log(Level.SEVERE, e2.getLocalizedMessage(), e2);
				}
			}
		} catch (SQLException e1) {
			_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}
	}

	public void 신규경험치지급단GiveItem(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT * FROM beginner_event WHERE activate IN(?,?)");

			pstm1.setString(1, "A");
			if (pc.isCrown()) {
				pstm1.setString(2, "P");
			} else if (pc.isKnight()) {
				pstm1.setString(2, "K");
			} else if (pc.isElf()) {
				pstm1.setString(2, "E");
			} else if (pc.isWizard()) {
				pstm1.setString(2, "W");
			} else if (pc.isDarkelf()) {
				pstm1.setString(2, "D");
			} else if (pc.isDragonknight()) {
				pstm1.setString(2, "T");
			} else if (pc.isIllusionist()) {
				pstm1.setString(2, "B");
			} else {
				pstm1.setString(2, "A");
			}

			rs = pstm1.executeQuery();
			PreparedStatement pstm2 = null;
			while (rs.next()) {
				try {
					int count = rs.getInt("count");
					if (count <= 0)
						continue;
					pstm2 = con.prepareStatement(
							"INSERT INTO character_items SET id=?, item_id=?, char_id=?, item_name=?, count=?, is_equipped=?, enchantlvl=?, is_id=?, durability=?, charge_count=?, remaining_time=?, last_used=?, bless=?, attr_enchantlvl=?");
					pstm2.setInt(1, ObjectIdFactory.getInstance().nextId());
					int itemid = rs.getInt("item_id");
					pstm2.setInt(2, itemid);
					pstm2.setInt(3, pc.getId());
					pstm2.setString(4, rs.getString("item_name"));
					pstm2.setInt(5, count);
					pstm2.setInt(6, 0);
					pstm2.setInt(7, rs.getInt("enchantlvl"));
					pstm2.setInt(8, 1);
					pstm2.setInt(9, 0);
					pstm2.setInt(10, rs.getInt("charge_count"));
					L1Item tempItem = ItemTable.getInstance().getTemplate(itemid);
					if (tempItem.getType2() == 0 && tempItem.getType() == 2) { // light
						pstm2.setInt(11, tempItem.getLightFuel());
					} else
						pstm2.setInt(11, 0);
					pstm2.setTimestamp(12, null);
					pstm2.setInt(13, rs.getInt("bless"));
					pstm2.setInt(14, 0);
					pstm2.executeUpdate();
				} catch (SQLException e2) {
					_log.log(Level.SEVERE, e2.getLocalizedMessage(), e2);
				} finally {
					SQLUtil.close(pstm2);
				}
			}
		} catch (SQLException e1) {
			_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}
	}

	public int GiveItem장비함(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pstm1 = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement("SELECT * FROM beginner WHERE activate IN(?,?)");

			pstm1.setString(1, "A");
			if (pc.isCrown()) {
				pstm1.setString(2, "P");
			} else if (pc.isKnight()) {
				pstm1.setString(2, "K");
			} else if (pc.isElf()) {
				pstm1.setString(2, "E");
			} else if (pc.isWizard()) {
				pstm1.setString(2, "W");
			} else if (pc.isDarkelf()) {
				pstm1.setString(2, "D");
			} else if (pc.isDragonknight()) {
				pstm1.setString(2, "T");
			} else if (pc.isIllusionist()) {
				pstm1.setString(2, "B");
			} else if (pc.isWarrior()) {
				pstm1.setString(2, "R");
			} else {
				pstm1.setString(2, "A");
			}

			rs = pstm1.executeQuery();
			PreparedStatement pstm2 = null;
			while (rs.next()) {
				try {

					int count = rs.getInt("count");
					if (count <= 0)
						continue;
					int itemid = rs.getInt("item_id");

					L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
					item.setCount(count);
					item.setEnchantLevel(rs.getInt("enchantlvl"));
					item.setIdentified(true);
					item.setChargeCount(rs.getInt("charge_count"));

					pc.getInventory().storeItem(item);

				} catch (SQLException e2) {
					_log.log(Level.SEVERE, e2.getLocalizedMessage(), e2);
				} finally {
					SQLUtil.close(pstm2);
				}
			}
		} catch (SQLException e1) {
			_log.log(Level.SEVERE, e1.getLocalizedMessage(), e1);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(con);
		}
		return 0;
	}
}