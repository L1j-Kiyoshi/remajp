package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class PhoneCheck {
	private static PhoneCheck _instance;

	private PhoneCheck() {
	}

	public static PhoneCheck getInstance() {
		if (_instance == null) {
			_instance = new PhoneCheck();
		}
		return _instance;
	}

	private static FastTable<String> Phonecheck_accountlist = new FastTable<String>();
	private static FastTable<String> PhoneNOcheck_accountlist = new FastTable<String>();

	public synchronized static void 폰인증확인(L1PcInstance gm, String phno) {
		String result = "";
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM autocheck WHERE Pnumber=?");
			pstm.setString(1, phno);
			rs = pstm.executeQuery();
			while (rs.next()) {
				result = rs.getString("account");
				i++;
				gm.sendPackets(new S_SystemMessage("[" + result + "] 번호에 등록된 "
						+ i + "번째 계정 : " + result));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public synchronized static int 폰인증완료(String accountname) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM autocheck WHERE account=?");
			pstm.setString(1, accountname);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("ischeck");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public synchronized static int 폰등록갯수(String pn) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT count(*) as cnt FROM autocheck WHERE pnumber=?");
			pstm.setString(1, pn);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public synchronized static int 폰등록완료(String accountname) {
		int result = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT count(*) as cnt FROM autocheck WHERE account=?");
			pstm.setString(1, accountname);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt("cnt");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}

	public static void 폰등록(String phn, String accountname, String name, int ii) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO autocheck SET account=?, name=?, Pnumber=? , time=? , ischeck=?");
			pstm.setString(1, accountname);
			pstm.setString(2, name);
			pstm.setString(3, phn);
			pstm.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
			pstm.setInt(5, ii);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static boolean getnocheck(String n) {
		if (PhoneNOcheck_accountlist.contains(n))
			return true;
		else
			return false;
	}

	public static void addnocheck(String n) {
		if (!PhoneNOcheck_accountlist.contains(n))
			PhoneNOcheck_accountlist.add(n);
	}

	public static void removenocheck(String n) {
		if (PhoneNOcheck_accountlist.contains(n))
			PhoneNOcheck_accountlist.remove(n);
	}

	public static void clearnocheck() {
		PhoneNOcheck_accountlist.clear();
	}

	public static boolean get(String n) {
		synchronized (Phonecheck_accountlist) {
			if (Phonecheck_accountlist.contains(n))
				return true;
			else
				return false;
		}
	}

	public static void add(String n) {
		synchronized (Phonecheck_accountlist) {
			if (!Phonecheck_accountlist.contains(n))
				Phonecheck_accountlist.add(n);
		}
	}

	public static void remove(String n) {
		synchronized (Phonecheck_accountlist) {
			if (Phonecheck_accountlist.contains(n))
				Phonecheck_accountlist.remove(n);
		}
	}

	public static void clear() {
		synchronized (Phonecheck_accountlist) {
			Phonecheck_accountlist.clear();
		}
	}
}