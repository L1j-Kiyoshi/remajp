package l1j.server.GameSystem.Delivery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class DeliveryThread extends Thread {

	private static DeliveryThread _instance;

	public static DeliveryThread getInstance() {
		if (_instance == null) {
			_instance = new DeliveryThread();
		}
		return _instance;
	}

	public DeliveryThread() {
		super("l1j.server.GameSystem.Delivery.DeliveryThread");
		start();
	}

	@Override
	public void run() {
		try {
			while (true) {
				timecheck();
				itemcheck();
				Thread.sleep(10000L);
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void timecheck() {
		Delivery[] list = DeliverySystem.getInstance().getList();
		if (list == null || list.length <= 0) {
			list = null;
			return;
		}
		for (Delivery del : list) {
			if (del == null)
				continue;
			if (System.currentTimeMillis() >= del.getTime().getTime()) {
				L1PcInstance pc = L1World.getInstance()
						.getPlayer(del.getName());
				if (pc != null && pc.getNetConnection() != null) {
					L1ItemInstance paper = pc.getInventory().getItem(
							del.getItemObjId());
					if (paper != null) {
						pc.getInventory().storeItem(del.getItemId(),
								del.getCount());
						pc.getInventory().deleteItem(paper);
					}
					DeliverySystem.getInstance().remove(del);
					del = null;
				}
			}
		}
		list = null;
	}

	private void itemcheck() {
		Delivery[] list = DeliverySystem.getInstance().getList();
		if (list == null || list.length <= 0) {
			list = null;
			return;
		}
		for (Delivery del : list) {
			if (del == null)
				continue;
			if (!getItem(del.getItemObjId())) {
				DeliverySystem.Delete(del.getItemObjId());
			}
		}
		list = null;
	}

	public boolean getItem(int objId) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM character_items WHERE id = ?");
			pstm.setInt(1, objId);
			rs = pstm.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}
}