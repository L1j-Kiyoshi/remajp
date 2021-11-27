package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1NameList {

	private List<String> _excludeList = new ArrayList<String>();
	//private List<String> _includeList = new ArrayList<String>();
	private List<String> _mailExcludeList = new ArrayList<String>();

	public void addExclude(String name) {
		_excludeList.add(name);
	}

	public String removeExclude(String name) {
		for (String each : _excludeList) {
			if (each.equalsIgnoreCase(name)) {
				_excludeList.remove(each);
				return each;
			}
		}
		return null;
	}

	public boolean isExclude(String name) {
		for (String each : _excludeList) {
			if (each.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isFullExclude() {
		return _excludeList.size() >= 50;
	}

	public List<String> getExcludeNames() {
		return _excludeList;
	}

//	public List<String> getIncludeNames() {
//		return _includeList;
//	}
//
//	public void addInclude(String name) {
//		_includeList.add(name);
//	}
//
//	public String removeInclude(String name) {
//		for (String each : _includeList) {
//			if (each.equalsIgnoreCase(name)) {
//				_includeList.remove(each);
//				return each;
//			}
//		}
//		return null;
//	}
//
//	public boolean isInclude(String name) {
//		for (String each : _includeList) {
//			if (each.equalsIgnoreCase(name)) {
//				return true;
//			}
//		}
//		return false;
//	}
//
//	public boolean isFullInclude() {
//		return _includeList.size() >= 50;
//	}
//
//	public int InCount() {
//		return _includeList.size();
//	}

	public List<String> getMailExcludeNames() {
		return _mailExcludeList;
	}

	public void addMailExclude(String name) {
		_mailExcludeList.add(name);
	}

	public String removeMailExclude(String name) {
		for (String each : _mailExcludeList) {
			if (each.equalsIgnoreCase(name)) {
				_mailExcludeList.remove(each);
				return each;
			}
		}
		return null;
	}

	public boolean isMailExclude(String name) {
		for (String each : _mailExcludeList) {
			if (each.equalsIgnoreCase(name)) {
				return true;
			}
		}
		return false;
	}

	public boolean isFullMailExclude() {
		return _mailExcludeList.size() >= 50;
	}

	public static void addExclude(L1PcInstance pc, String name, int type) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("INSERT INTO character_blocks SET type=?, char_name=?, block_name=?")) {
			pstm.setInt(1, type);
			pstm.setString(2, pc.getName());
			pstm.setString(3, name);
			pstm.execute();
		} catch (SQLException e) {
		}
	}

	public static void removeExclude(L1PcInstance pc, String name, int type) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("DELETE FROM character_blocks WHERE type=? AND char_name=? AND block_name=?")) {
			pstm.setInt(1, type);
			pstm.setString(2, pc.getName());
			pstm.setString(3, name);
			pstm.execute();
		} catch (SQLException e) {
		}
	}
}