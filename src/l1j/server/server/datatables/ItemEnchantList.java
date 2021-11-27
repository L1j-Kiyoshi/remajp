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

public class ItemEnchantList {
	public class ItemEnchant {
		int Chance = 0;
	}

	private static Logger _log = Logger.getLogger(ItemEnchantList.class.getName());

	private static ItemEnchantList _instance;

	private final Map<Integer, ItemEnchant> _idlist = new HashMap<Integer, ItemEnchant>();

	public static ItemEnchantList getInstance() {
		if (_instance == null) {
			_instance = new ItemEnchantList();
		}
		return _instance;
	}

	private ItemEnchantList() {		
		itemEnchantList();			
	}

	public void itemEnchantList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select item_id, chance from item_enchant_list");
			rs = pstm.executeQuery();

			ItemEnchant itemenchant = null;
			while (rs.next()) {
				itemenchant = new ItemEnchant();

				itemenchant.Chance = rs.getInt("chance");

				_idlist.put(rs.getInt("item_id"), itemenchant);
			}

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void reload() {
		ItemEnchantList oldInstance = _instance;
		_instance = new ItemEnchantList();
		if (oldInstance != null)
			oldInstance._idlist.clear();
	}

	public int getItemEnchant(int itemId) {
		ItemEnchant itemenchant = _idlist.get(itemId);

		if (itemenchant == null) {
			return 0;
		}

		return itemenchant.Chance;
	}
}
