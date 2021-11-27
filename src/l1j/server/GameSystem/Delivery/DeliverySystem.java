package l1j.server.GameSystem.Delivery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class DeliverySystem {
	private static DeliverySystem _instance;
	public static FastTable<Delivery> list;

	public static DeliverySystem getInstance() {
		if (_instance == null) {
			_instance = new DeliverySystem();
		}
		return _instance;
	}

	private DeliverySystem() {
		list = new FastTable<Delivery>();
	}

	public void Load() {
		Init();
		DeliveryThread.getInstance();
	//	System.out.println(":: Delivery System Loading Complete");
	}

	public Delivery[] getList() {
		Delivery[] l = null;
		synchronized (list) {
			l = (Delivery[]) list.toArray(new Delivery[list.size()]);
		}
		return l;
	}

	public void remove(Delivery d) {
		synchronized (list) {
			if (list.contains(d)) {
				list.remove(d);
				Delete(d.getItemObjId());
				d.reset();
				d = null;
			}
		}
	}

	public void add(String name, int objid, int id, long time, int count,
			int ccount) {
		Delivery del = new Delivery();
		del.setName(name);
		del.setItemObjId(objid);
		del.setItemId(id);
		del.setTime(new Timestamp(System.currentTimeMillis() + time));
		del.setCount(count);
		del.setClockCount(ccount);
		add(del);
	}

	public void add(String name, int objid, int id, Timestamp time, int count,
			int ccount) {
		Delivery del = new Delivery();
		del.setName(name);
		del.setItemObjId(objid);
		del.setItemId(id);
		del.setTime(time);
		del.setCount(count);
		del.setClockCount(ccount);
		add(del);
	}

	public void add(Delivery d) {
		synchronized (list) {
			if (!list.contains(d)) {
				Store(d.getName(), d.getItemObjId(), d.getItemId(),
						d.getTime(), d.getCount(), d.getClockCount());
				list.add(d);
			}
		}
	}

	public Delivery get(int id) {
		synchronized (list) {
			for (Delivery del : list) {
				if (del == null)
					continue;
				if (del.getItemObjId() == id)
					return del;
			}
		}
		return null;
	}

	public static void Init() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM delivery_system");
			rs = pstm.executeQuery();
			while (rs.next()) {
				Delivery del = new Delivery();
				del.setName(rs.getString("char_name"));
				del.setItemObjId(rs.getInt("itemobj"));
				del.setItemId(rs.getInt("itemid"));
				del.setTime(rs.getTimestamp("time"));
				del.setCount(rs.getInt("count"));
				del.setClockCount(rs.getInt("clock_count"));
				list.add(del);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void Store(String name, int id, int itemid, Timestamp time,
			int count, int clock) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO delivery_system SET char_name=?,itemobj=?,itemid=?,time=?,count=?,clock_count=?");
			pstm.setString(1, name);
			pstm.setInt(2, id);
			pstm.setInt(3, itemid);
			pstm.setTimestamp(4, time);
			pstm.setInt(5, count);
			pstm.setInt(6, clock);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void Update(int id, int count, int clock_count) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE delivery_system SET count =?, clock_count=? WHERE itemobj=?");
			pstm.setInt(1, count);
			pstm.setInt(2, clock_count);
			pstm.setInt(3, id);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void UpdateTime(int id, Timestamp time, int clock_count) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE delivery_system SET time =?, clock_count=? WHERE itemobj=?");
			pstm.setTimestamp(1, time);
			pstm.setInt(2, clock_count);
			pstm.setInt(3, id);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void Delete(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("delete FROM delivery_system WHERE itemobj=?");
			pstm.setInt(1, id);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
