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
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public final class IpCheckTable {
	private static Logger _log = Logger.getLogger(IpCheckTable.class.getName());

	private static IpCheckTable _instance;

	private final FastTable<String> _ipcheck = new FastTable<String>();

	public static IpCheckTable getInstance() {
		if (_instance == null) {
			_instance = new IpCheckTable();
		}
		return _instance;
	}

	private IpCheckTable() {
		load();
	}

	public static void reload() { // Gn.67
		IpCheckTable oldInstance = _instance;
		_instance = new IpCheckTable();
		oldInstance._ipcheck.clear();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ip_check");
			rs = pstm.executeQuery();

			while (rs.next()) {
				_ipcheck.add(rs.getString("ip"));
			}

			_log.config("IPCheckList " + _ipcheck.size());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public FastTable<String> list() {
		return _ipcheck;
	}

}
