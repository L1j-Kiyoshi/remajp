package l1j.server.server.templates;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class L1BoardPost {
	private static final String LFCBOARD		= "board_mjlfc";
	private static final String FREEBOARD		= "board_sm_freeboard";	
	private static final String APPCENTER_BOARD	= "board_mjnotice";
	
	private final int 		_id;
	private final String 	_name;
	private final String 	_date;
	private final String 	_title;
	private final String 	_content;
	
	public int getId() {
		return _id;
	}

	public String getName() {
		return _name;
	}
	public String getDate() {
		return _date;
	}

	public String getTitle() {
		return _title;
	}

	public String getContent() {
		return _content;
	}
	
	private String today(String timeZoneID) {
		TimeZone tz = TimeZone.getTimeZone(timeZoneID);
		Calendar cal = Calendar.getInstance(tz);
		int year = cal.get(Calendar.YEAR) - 2000;
		int month = cal.get(Calendar.MONTH) + 1;
		int date = cal.get(Calendar.DATE);
		return String.format("%02d/%02d/%02d", year, month, date);
	}

	private L1BoardPost(int id, String name, String title, String content) {
		_id = id;
		_name = name;
		_date = today(Config.TIME_ZONE);
		_title = title;
		_content = content;
	}

	private L1BoardPost(ResultSet rs) throws SQLException {
		_id = rs.getInt("id");
		_name = rs.getString("name");
		_date = rs.getString("date");
		_title = rs.getString("title");
		_content = rs.getString("content");
	}
	
	
	
	/** 2016.11.26 MJ 앱센터 LFC **/
	private synchronized static L1BoardPost create(String name, String title, String content, String table){
		Connection con 			= null;
		PreparedStatement pstm1 = null;
		ResultSet rs 			= null;
		PreparedStatement pstm2 = null;
		StringBuilder sbQry 	= new StringBuilder();
		try {
			sbQry.append("SELECT max(id) + 1 as newid FROM ").append(table);
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm1 = con.prepareStatement(sbQry.toString());
			rs = pstm1.executeQuery();
			rs.next();
			
			int id = rs.getInt("newid");
			L1BoardPost topic = new L1BoardPost(id, name, title, content);
			sbQry = new StringBuilder();
			sbQry.append("INSERT INTO ").append(table).append(" SET id=?, name=?, date=?, title=?, content=?");
			pstm2 = con.prepareStatement(sbQry.toString());
			pstm2.setInt(1, topic.getId());
			pstm2.setString(2, topic.getName());
			pstm2.setString(3, topic.getDate());
			pstm2.setString(4, topic.getTitle());
			pstm2.setString(5, topic.getContent());
			pstm2.execute();
			return topic;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm1);
			SQLUtil.close(pstm2);
			SQLUtil.close(con);
		}
		return null;
	}
	
	private static void delData(String name, String table){
		Connection con = null;
		PreparedStatement pstm = null;
		StringBuilder sbQry = new StringBuilder();
		try {
			sbQry.append("DELETE FROM ").append(table).append(" WHERE name=?");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			pstm.setString(1, name);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void delData(int id, String table){
		Connection con = null;
		PreparedStatement pstm = null;
		StringBuilder sbQry = new StringBuilder();
		try {
			sbQry.append("DELETE FROM ").append(table).append(" WHERE id=?");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			pstm.setInt(1, id);
			pstm.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private static L1BoardPost findById(int id, String table){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		StringBuilder sbQry = new StringBuilder();
		try {
			sbQry.append("SELECT * FROM ").append(table).append(" WHERE id=?");
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(sbQry.toString());
			pstm.setInt(1, id);
			rs = pstm.executeQuery();
			if (rs.next()) {
				return new L1BoardPost(rs);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	public static List<L1BoardPost> index(int id, int limit, String table){
		List<L1BoardPost> result = new ArrayList<L1BoardPost>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = makeIndexStatement(con, id, limit, table);
			rs = pstm.executeQuery();
			while (rs.next()) {
				result.add(new L1BoardPost(rs));
			}
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return null;
	}
	
	private static PreparedStatement makeIndexStatement(Connection con, int id, int limit, String table) throws SQLException{
		PreparedStatement result = null;
		int offset = 1;
		StringBuilder sbQry = new StringBuilder();
		sbQry.append("SELECT * FROM ").append(table);
		if(id == 0){
			sbQry.append(" ORDER BY id DESC LIMIT ?");
			result = con.prepareStatement(sbQry.toString());
		}else{
			sbQry.append(" WHERE id < ? ORDER BY id DESC LIMIT ?");
			result = con.prepareStatement(sbQry.toString());
			result.setInt(1, id);
			offset++;
		}
		result.setInt(offset, limit);
		return result;
	}
	
	public static L1BoardPost createLfc(String name, String title, String content){
		return create(name, title, content, LFCBOARD);
	}
	
	public static L1BoardPost createAppCenterNotice(String name, String title, String content){
		return create(name, title, content, APPCENTER_BOARD);
	}
	
	public static L1BoardPost createFREEBOARD(String name, String title, String content){
		return create(name, title, content, FREEBOARD);
	}
	
	public static void delLfc(String name){
		delData(name, LFCBOARD);
	}
	public static void delLfc(int id){
		delData(id, LFCBOARD);
	}
	
	public static L1BoardPost findByIdLfc(int id){
		return findById(id, LFCBOARD);
	}
	
	public static L1BoardPost findByIdAppCenterNotice(int id){
		return findById(id, APPCENTER_BOARD);
	}
	
	public static List<L1BoardPost> indexLfc(int id, int limit){
		return index(id, limit, LFCBOARD);	
	}
	
	public static List<L1BoardPost> indexAppCenterNotice(int id, int limit){
		return index(id, limit, APPCENTER_BOARD);	
	}
	/** 2016.11.26 MJ 앱센터 LFC **/
	
	
	/**자유게시판관련*/
	public static void delFREEBOARD(String name){
		delData(name, FREEBOARD);
	}
	public static void delFREEBOARD(int id){
		delData(id, FREEBOARD);
	}
	
	public static L1BoardPost findByIdFREEBOARD(int id){
		return findById(id, FREEBOARD);
	}
	public static List<L1BoardPost> indexFREEBOARD(int id, int limit){
		return index(id, limit, FREEBOARD);	
	}
	/**자유게시판관련*/
}
