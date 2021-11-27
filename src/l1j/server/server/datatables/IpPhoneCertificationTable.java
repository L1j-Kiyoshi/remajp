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

public final class IpPhoneCertificationTable {
	private static Logger _log = Logger
			.getLogger(IpPhoneCertificationTable.class.getName());

	private static IpPhoneCertificationTable _instance;

	private final FastTable<String> _iplist = new FastTable<String>();

	public static IpPhoneCertificationTable getInstance() {
		if (_instance == null) {
			_instance = new IpPhoneCertificationTable();
		}
		return _instance;
	}

	private IpPhoneCertificationTable() {
		load();
	}

	public static void reload() { // Gn.67
		IpPhoneCertificationTable oldInstance = _instance;
		_instance = new IpPhoneCertificationTable();
		oldInstance._iplist.clear();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM ip_phone_certification");
			rs = pstm.executeQuery();

			while (rs.next()) {
				_iplist.add(rs.getString("ip"));
			}

			_log.config("IPCheckList " + _iplist.size());
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void add(String ip) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO ip_phone_certification SET ip=?");
			pstm.setString(1, ip);
			pstm.executeUpdate();
			_iplist.add(ip);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public FastTable<String> list() {
		return _iplist;
	}

}
