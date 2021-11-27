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

public class AutoAdvertising {

	private static Logger _log = Logger.getLogger(AutoAdvertising.class
			.getName());
	private static AutoAdvertising _instance;

	private AutoAdvertising() {
	}

	public static AutoAdvertising getInstance() {
		if (_instance == null) {
			_instance = new AutoAdvertising();
		}
		return _instance;
	}

	public ArrayList<Integer> getPrDetails(String logid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<Integer> prDetails = new ArrayList<Integer>();

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("select prcheck, prcount from accounts where login = '"
							+ logid + "'");
			rs = pstm.executeQuery();

			if (rs.next()) {
				prDetails.add(rs.getInt(1));
				prDetails.add(rs.getInt(2));
			}

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs, pstm, con);
		}

		return prDetails;
	}

	public void storePrCount(String logid, int prCount) {
		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE accounts SET prcount=? WHERE login=?");
			pstm.setInt(1, prCount);
			pstm.setString(2, logid);
			pstm.executeUpdate();

		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}