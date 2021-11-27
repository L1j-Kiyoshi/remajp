package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.PerformanceTimer;
import l1j.server.server.utils.SQLUtil;

public class AttendanceTable {
	public class AttendanceItem {
		public int _index;
		public int _itemId;
		public int _count;
		public int _enchant;
		public int _probability;
		public boolean _broadcast_item;
	}
	
	private static Logger _log = Logger.getLogger(AttendanceTable.class.getName());

	private static AttendanceTable _instance;

	private Map<Integer, ArrayList<AttendanceItem>> _list = new HashMap<Integer, ArrayList<AttendanceItem>>();

	private Map<Integer, ArrayList<AttendanceItem>> _listPc = new HashMap<Integer, ArrayList<AttendanceItem>>();

	public static AttendanceTable getInstance() {
		if (_instance == null) {
			_instance = new AttendanceTable();
		}
		return _instance;
	}

	public void load() {
		PerformanceTimer timer = new PerformanceTimer();
		System.out.print("loading " + _log.getName().substring(_log.getName().lastIndexOf(".") + 1) + "...");
		
		loadAttendance(_list);
		loadAttendancePc(_listPc);
		
		System.out.println("OK! " + timer.get() / 1000 + "ms");
	}

	public void realod() {
		PerformanceTimer timer = new PerformanceTimer();
		System.out.print("reloading " + _log.getName().substring(_log.getName().lastIndexOf(".") + 1) + "...");
		
		Map<Integer, ArrayList<AttendanceItem>> list = new HashMap<Integer, ArrayList<AttendanceItem>>();
		Map<Integer, ArrayList<AttendanceItem>> listPc = new HashMap<Integer, ArrayList<AttendanceItem>>();
		
		loadAttendance(list);
		loadAttendancePc(listPc);
		
		_list = list;
		_listPc = listPc;
		
		System.out.println("OK! " + timer.get() / 1000 + "ms");
	}

	public AttendanceTable() {
		load();
	}

	public void loadAttendance(Map<Integer, ArrayList<AttendanceItem>> list) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM attendance_item ORDER BY id ASC");
			rs = pstm.executeQuery();
			ArrayList<AttendanceItem> items;
			AttendanceItem item;
			while (rs.next()) {
				int index = rs.getInt("index");
				
				items = list.get(index);
				if(items == null) {
					items = new ArrayList<AttendanceItem>();
					list.put(index, items);
				}
				
				item = new AttendanceItem();
				item._itemId = rs.getInt("item_id");
				item._count = rs.getInt("count");
				item._enchant = rs.getInt("enchant");
				item._probability = rs.getInt("probability");
				item._broadcast_item = rs.getInt("broadcast_item") == 0 ? false : true;
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public Map<Integer, ArrayList<AttendanceItem>> attendanceList() {
		return _list;
	}

	public ArrayList<AttendanceItem> getAttendHomeItem(int value) {
		return _list.get(value);
	}

	public int getHomeSize() {
		return _list.size();
	}

	public void loadAttendancePc(Map<Integer, ArrayList<AttendanceItem>> list) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM attendance_item_pc ORDER BY id ASC");
			rs = pstm.executeQuery();
			ArrayList<AttendanceItem> items;
			AttendanceItem item;
			while (rs.next()) {
				int index = rs.getInt("index");
				
				items = list.get(index);
				if(items == null) {
					items = new ArrayList<AttendanceItem>();
					list.put(index, items);
				}
				
				item = new AttendanceItem();
				item._itemId = rs.getInt("item_id");
				item._count = rs.getInt("count");
				item._enchant = rs.getInt("enchant");
				item._probability = rs.getInt("probability");
				item._broadcast_item = rs.getInt("broadcast_item") == 0 ? false : true;
				items.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public Map<Integer, ArrayList<AttendanceItem>> attendancePcList() {
		return _listPc;
	}

	public ArrayList<AttendanceItem> getAttendPCItem(int value) {
		return _listPc.get(value);
	}

	public int getPcSize() {
		return _listPc.size();
	}
}