package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.utils.SQLUtil;
public class DogFightRecordTable {

	private static Logger _log = Logger.getLogger(DogFightRecordTable.class.getName());

	private static DogFightRecordTable _instance;

	public DogFightRecordTable() {}

	public static DogFightRecordTable getInstance() {
		if (_instance == null) {
			_instance = new DogFightRecordTable();
		}
		return _instance;
	}

	public void updateDogFightRecord(int number, int win, int lose) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE racedog_record SET win=?,lose=? WHERE number=?");
			pstm.setInt(1, win);
			pstm.setInt(2, lose);
			pstm.setInt(3, number);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void getDogFightRecord(int number, L1PetInstance bug) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT win, lose FROM racedog_record WHERE number=?");
			pstm.setInt(1, number);
			rs = pstm.executeQuery();
			if (rs.next()) {
				bug.setWin(rs.getInt("win"));
				bug.setLose(rs.getInt("lose"));
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}