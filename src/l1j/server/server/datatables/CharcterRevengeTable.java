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
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class CharcterRevengeTable {

	private static Logger _log = Logger.getLogger(CharcterRevengeTable.class.getName());

	private static CharcterRevengeTable _instance;

	public static CharcterRevengeTable getInstance() {
		if (_instance == null) {
			_instance = new CharcterRevengeTable();
		}
		return _instance;
	}
	

	public CharcterRevengeTable() {
	}
	


	public void StoreRevengeResult(int CharobjId, int TargetobjId, String TargetName, String TargetClanName, int TargetClanId, int TargetClass, int result, int resultcounter, int BreakTime, int remaintime, int remaincounter, int chasertime) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO charcter_revenge SET objId=?, TargetId=?, targetname=?, TargetClanName=?, TargetClanId=?, TargetClass=?, result=?, resultcounter=?, breaktime=?, remaintime=?, remaincounter=?, chasertime=?");
			pstm.setInt(1, CharobjId);
			pstm.setInt(2, TargetobjId);
			pstm.setString(3, TargetName);
			pstm.setString(4, TargetClanName);
			pstm.setInt(5, TargetClanId);
			pstm.setInt(6, TargetClass);
			pstm.setInt(7, result);
			pstm.setInt(8, resultcounter);
			pstm.setInt(9, BreakTime);
			pstm.setInt(10, remaintime);
			pstm.setInt(11, remaincounter);
			pstm.setInt(12, chasertime);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public int TargetConnection(int tarobjt) {
		int result = 0;
		L1Object target = L1World.getInstance().findObject(tarobjt);
		if (target != null && target instanceof L1PcInstance) {
			L1PcInstance Targetpc = (L1PcInstance) target;
			if (Targetpc.getNetConnection() == null) {
				result = 0;
			} else if (Targetpc.getNetConnection() != null) {
				result = 1;
			}
		}
		return result;
	}
	
	public String TarObjFind(String name) {
		Connection con = null;
		PreparedStatement pstm = null;
		String result = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT TargetId FROM charcter_revenge WHERE targetname=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			if (rs.next()) {
			   result = rs.getString(1);
			}
		} catch (SQLException e) {
			_log.warning("could not check existing TarObjFind:" + e.getMessage());
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
			SQLUtil.close(rs);
		}
		return result;
	}
	
	public boolean doesChaserTimeExist(int pcid) {
		boolean result = true;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_buff WHERE char_obj_id=? AND skill_id = 80020");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			_log.warning("could not check existing doesChaserTimeExist:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}
	
	public void StoreChaserTime(int pcid, int targetobj, int time) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE charcter_revenge SET chasertime=? WHERE objId=? AND TargetId=?");
			pstm.setInt(1, time);
			pstm.setInt(2, pcid);
			pstm.setInt(3, targetobj);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.warning("could not check existing StoreChaserTime:" + e.getMessage());
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public int LoadChaserTarGet(int pcid) {
		int result = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT TargetId FROM charcter_revenge WHERE objId=? AND chasertime > 0");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			_log.warning("could not check existing StoreChaserTime:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}
	
	public static void TarResultReset(int pcid, int targetobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		int BreakTime = (int) (System.currentTimeMillis() / 1000);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE charcter_revenge SET result=?, resultcounter=?, breaktime=?, remaintime=?, remaincounter=? WHERE objId=? AND TargetId=?");
			pstm.setInt(1, 2);
			pstm.setInt(2, 1);
			pstm.setInt(3, BreakTime);
			pstm.setInt(4, 86400);
			pstm.setInt(5, 3);
			pstm.setInt(6, pcid);
			pstm.setInt(7, targetobj);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.warning("could not check existing UpdateResultCount:" + e.getMessage());
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void AtkerResultReset(int pcid, int targetobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		int BreakTime = (int) (System.currentTimeMillis() / 1000);
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE charcter_revenge SET result=?, resultcounter=?, breaktime=?, remaintime=?, remaincounter=? WHERE objId=? AND TargetId=?");
			pstm.setInt(1, 1);
			pstm.setInt(2, 1);
			pstm.setInt(3, BreakTime);
			pstm.setInt(4, 86400);
			pstm.setInt(5, 1);
			pstm.setInt(6, pcid);
			pstm.setInt(7, targetobj);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.warning("could not check existing UpdateResultCount:" + e.getMessage());
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void UpdateRemainCount(int pcid, int targetobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE charcter_revenge SET remaincounter=? WHERE objId=? AND TargetId=?");
			pstm.setInt(1, RemainCounter(pcid, targetobj) - 1);
			pstm.setInt(2, pcid);
			pstm.setInt(3, targetobj);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.warning("could not check existing UpdateRemainCount:" + e.getMessage());
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void UpdateRemainCount(int pcid, String tarname) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE charcter_revenge SET remaincounter=? WHERE objId=? AND targetname=?");
			pstm.setInt(1, 0);
			pstm.setInt(2, pcid);
			pstm.setString(3, tarname);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.warning("could not check existing UpdateRemainCount:" + e.getMessage());
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public int RemainCounter(int pcid, int targetobj) { /**결과 조회*/
		int result  = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT remaincounter FROM charcter_revenge WHERE objId=? AND TargetId=?");
			pstm.setInt(1, pcid);
			pstm.setInt(2, targetobj);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			_log.warning("could not check existing RemainCounter:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}
	
	public static void UpdateResultCount(int pcid, int targetobj) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE charcter_revenge SET resultcounter=? WHERE objId=? AND TargetId=?");
			pstm.setInt(1, Resultcounter(pcid, targetobj) + 1);
			pstm.setInt(2, pcid);
			pstm.setInt(3, targetobj);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.warning("could not check existing UpdateResultCount:" + e.getMessage());
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static int Resultcounter(int pcid, int targetobj) { /**결과 조회*/
		int result  = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT resultcounter FROM charcter_revenge WHERE objId=? AND TargetId=?");
			pstm.setInt(1, pcid);
			pstm.setInt(2, targetobj);
			rs = pstm.executeQuery();
			if (rs.next()) {
				result = rs.getInt(1);
			}
		} catch (SQLException e) {
			_log.warning("could not check existing Resultcounter:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}
	
	
	
	public int ResultCheck(int pcid, int targetobj) { /**결과 조회*/
		int result  = 0;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT result FROM charcter_revenge WHERE objId=? AND TargetId=?");
			pstm.setInt(1, pcid);
			pstm.setInt(2, targetobj);
			rs = pstm.executeQuery();
			if (rs.next()) {
			   result = rs.getInt(1);
			}
		} catch (SQLException e) {
			_log.warning("could not check existing ResultCheck:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}
	
	public boolean doesTargetExist(int pcid, int targetid) {
		boolean result = true;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM charcter_revenge WHERE objId=? AND TargetId=?");
			pstm.setInt(1, pcid);
			pstm.setInt(2, targetid);
			rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			_log.warning("could not check existing TargetId:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}


	public ArrayList<Integer> GetRevengeObj(int pcid) { /**복수 발생시간을 불러옴*/
		ArrayList<Integer> inform  = new ArrayList<Integer>();
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT objId FROM charcter_revenge WHERE objId=?");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing objId:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}
	
	public ArrayList<Integer> GettargetObj(int pcid) { /**복수 발생시간을 불러옴*/
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<Integer> inform  = new ArrayList<Integer>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT TargetId FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing objId:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}

	public ArrayList<Integer> BreakTime(int pcid) { /**복수 발생시간을 불러옴*/
		ArrayList<Integer> inform = new ArrayList<Integer>();
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT breaktime FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing revenge_objId:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}

	public ArrayList<Integer> Remaintime(int pcid) { /**복수 남은 시간을 불러옴*/
		ArrayList<Integer> inform = new ArrayList<Integer>();
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT remaintime FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing revenge_remaintime:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}

	public ArrayList<Integer> CPRemainCount(int pcid) {  /**추적이나 도발 횟수를 불러옴*/
		ArrayList<Integer> inform = new ArrayList<Integer>();
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT remaincounter FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing revenge_remaincounter:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}

	public ArrayList<Integer> WLCount(int pcid) {  /**결과의 카운터를 불러옴*/
		ArrayList<Integer> inform = new ArrayList<Integer>();
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT resultcounter FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing revenge_resultcounter:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}
	
	public ArrayList<Integer> ChaserTimelist(int pcid) {  /**체이서타임을불러옴*/
		ArrayList<Integer> inform = new ArrayList<Integer>();
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT chasertime FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing ChaserTimelist:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}

	public ArrayList<Integer> isWin(int pcid) {  /**패배인지 승리인지의 결과를 불러옴*/
		ArrayList<Integer> inform = new ArrayList<Integer>();
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT result FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing revenge_result:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}

	public ArrayList<Integer> TargetClass(int pcid) {  /**대상의 클래스를 불러옴*/
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<Integer> inform = new ArrayList<Integer>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT TargetClass FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing Type:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}


	public ArrayList<Integer> TargetClanId(int pcid) { /**대상의 클랜아이디를 불러옴*/
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<Integer> inform = new ArrayList<Integer>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT TargetClanId FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getInt(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing ClanID:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}


	public ArrayList<String> TargetName(int pcid) {  /**대상의 이름을 불러옴*/
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		ArrayList<String> inform = new ArrayList<String>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT TargetName FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getString(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing char_name:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}

	public ArrayList<String> TargetClanName(int pcid) {  /**대상의 클랜이름을 불러옴*/
		ArrayList<String> inform = new ArrayList<String>();
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT TargetClanName FROM charcter_revenge WHERE objId = ? ORDER BY breaktime DESC");
			pstm.setInt(1, pcid);
			rs = pstm.executeQuery();
			int count = 0;
			while (rs.next()) {
				inform.add(rs.getString(1));
				count++;
				if(count > inform.size()){
					break;
				}
			}
		} catch (SQLException e) {
			_log.warning("could not check existing char_name:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return inform;
	}
}
