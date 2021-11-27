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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public final class ForceItem {
	private static Logger _log = Logger.getLogger(ForceItem.class.getName());

	private static ForceItem _instance;

	private final Map<Integer, Integer> _itempercent = new HashMap<Integer, Integer>();
	
	private final Map<Integer, Integer> _succitem = new HashMap<Integer, Integer>();

	public static ForceItem getInstance() {
		if (_instance == null) {
			_instance = new ForceItem();
		}
		return _instance;
	}

	private ForceItem() {
		SuccPercent();
		SuccItem();		
	}

	public static void reload() { // Gn.67
		ForceItem oldInstance = _instance;
		_instance = new ForceItem();
		oldInstance._itempercent.clear();
		oldInstance._succitem.clear();
	}

	private void SuccPercent() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM revive_Item");
			for (rs = pstm.executeQuery(); rs.next();) {
				int itemId = rs.getInt("itemId");
				int SuccPercent = rs.getInt("percent");
				_itempercent.put(new Integer(itemId), SuccPercent);
			}
			_log.config("forceitem " + _itempercent.size());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void SuccItem() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM revive_Item");
			for (rs = pstm.executeQuery(); rs.next();) {
				int itemId = rs.getInt("itemId");
				int SuccItem = rs.getInt("succ_itemId");
				_succitem.put(new Integer(itemId), SuccItem);
			}
			_log.config("succitem " + _succitem.size());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public int getSuccItem(int itemId) {
		int sitem = 0;
		if (_succitem.containsKey(itemId)) {
			sitem = _succitem.get(itemId);
		}
		return sitem;
	}
	
	public int getSuccPercent(int itemId) {
		int percent = 0;
		if (_itempercent.containsKey(itemId)) {
			percent = _itempercent.get(itemId);
		}
		return percent;
	}

}
