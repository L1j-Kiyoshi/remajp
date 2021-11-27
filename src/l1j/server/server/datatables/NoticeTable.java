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
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class NoticeTable {

	private static Logger _log = Logger.getLogger(NoticeTable.class.getName());

	private static NoticeTable _instance;

	private final ArrayList<String> _notice = new ArrayList<String>();

	public static NoticeTable getInstance() {
		if (_instance == null) {
			_instance = new NoticeTable();
		}
		return _instance;
	}

	private NoticeTable() {
		// PerformanceTimer timer = new PerformanceTimer();
		// System.out.print("[NoticeTable] loading NoticeList...");
		NoticeList();
		// System.out.println("OK! " + timer.get() + " ms");
	}

	public void NoticeList() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select content from notice_list");
			rs = pstm.executeQuery();

			while (rs.next()) {
				_notice.add(rs.getString("content"));
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
		NoticeTable oldInstance = _instance;
		_instance = new NoticeTable();
		if (oldInstance != null)
			oldInstance._notice.clear();
	}

	public Object[] getNoticeList() {
		return _notice.toArray();
	}
}
