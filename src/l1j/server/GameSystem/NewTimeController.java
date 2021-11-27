package l1j.server.GameSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class NewTimeController extends Thread {

	private static NewTimeController _instance;

	public static NewTimeController get() {
		if (_instance == null) {
			_instance = new NewTimeController();
		}
		return _instance;
	}

	public NewTimeController() {
		load();
		start();
	}

	public Timestamp nw_time;
	public Timestamp sandW_time;
	public boolean SandWorm_On = false;;

	public void run() {
		while (true) {
			try {
				if (System.currentTimeMillis() >= nw_time.getTime()) {
					try {
						nw_time.setTime(nw_time.getTime() + (long) (60000 * 60 * 24 * 7));
						navalReset();
						nwupdate(nw_time);
					} catch (Exception e) {
					}
				}

				if (System.currentTimeMillis() >= sandW_time.getTime()) {
					try {
						Date date = (Date) Calendar.getInstance().getTime();
						if (sandW_time.getYear() < date.getYear() || sandW_time.getMonth() < date.getMonth()
								|| sandW_time.getDate() < date.getDate()) {
						} else {
							SandWorm_On = true;
						}
						sandW_time.setTime(sandW_time.getTime() + (long) (60000 * 60 * 24 * 2));
						swupdate(sandW_time);
					} catch (Exception e) {
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void navalReset() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM navalwarfare_score order by score desc limit 3");
			rs = pstm.executeQuery();
			while (rs.next()) {
				for (int i = 1; i <= 8; i++) {
					String name = rs.getString("name" + i);
					if (name == null || name.equalsIgnoreCase(""))
						continue;
					L1PcInstance pc = L1World.getInstance().getPlayer(name);
					if (pc == null) {
						itemInsert(name);
					} else {
						pc.getInventory().storeItem(60104, 1);
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM navalwarfare_score");
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void ravadbreset() {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET RDGDay=? ");
			pstm.setTimestamp(1, null);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void itemInsert(String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int charid = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			if (rs.next()) {
				charid = rs.getInt("objid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		if (charid == 0)
			return;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_items WHERE char_id=? AND item_id =?");
			pstm.setInt(1, charid);
			pstm.setInt(2, 60104);
			rs = pstm.executeQuery();
			if (rs.next()) {
				int item_id = rs.getInt("id");
				int count = rs.getInt("count");
				itemUpdate(item_id, count + 1);
			} else {
				itemInsert2(charid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void itemInsert2(int id) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO character_items SET id = ?, item_id = ?, char_id = ?, item_name = ?, count = ?, bless=?");
			pstm.setInt(1, ObjectIdFactory.getInstance().nextId());
			pstm.setInt(2, 60104);
			pstm.setInt(3, id);
			pstm.setString(4, "마족 무기 보호 주문서");
			pstm.setInt(5, 1);
			pstm.setInt(6, 1);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void itemUpdate(int id, int count) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE character_items SET count=? WHERE id=?");
			pstm.setInt(1, count);
			pstm.setInt(2, id);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/*private void ravaTimeReset(L1PcInstance player) {
		try {
			player.setravatime(1);
			player.setravaday((Timestamp) lb_time.clone());
			
			 * try { player.save(); } catch (Exception e) { e.printStackTrace();
			 * }
			 
			if (player.getMapId() >= 451 && player.getMapId() <= 536 && player.getMapId() != 480 && player.getMapId() != 481
					&& player.getMapId() != 482 && player.getMapId() != 483 && player.getMapId() != 484
					&& player.getMapId() != 521 && player.getMapId() != 522 && player.getMapId() != 523
					&& player.getMapId() != 524) {// 라던
				player.sendPackets(new S_PacketBox(S_PacketBox.TIME_COUNT, 7199), true);
				player.sendPackets(new S_SystemMessage("라던 입장 시간이 초기화 되었습니다."), true);
				player.sendPackets(new S_ServerMessage(1526, "2"), true);// 1 시간
																			// 남았다.
			}
		} catch (Exception e) {
		}
	}*/

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM time_controller");
			rs = pstm.executeQuery();
			while (rs.next()) {
				/*lb_time = rs.getTimestamp("lastabard_time");*/
				nw_time = rs.getTimestamp("navalwarfare_time");
				sandW_time = rs.getTimestamp("sandworm_time");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void lbupdate(Timestamp ts) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE time_controller SET lastabard_time=?");
			pstm.setTimestamp(1, ts);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void nwupdate(Timestamp ts) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE time_controller SET navalwarfare_time=?");
			pstm.setTimestamp(1, ts);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void swupdate(Timestamp ts) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE time_controller SET sandworm_time=?");
			pstm.setTimestamp(1, ts);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void Update(Timestamp ts) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE accounts SET RDGTime =?, RDGDay=?");
			pstm.setInt(1, 0);
			pstm.setTimestamp(2, ts);
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

}
