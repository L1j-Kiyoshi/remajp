package l1j.server.GameSystem.SupportSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class SupportMapTable {
	
	public static SupportMapTable _instance;
	
	public Map<Integer, L1SupportMap> _list = new HashMap<Integer, L1SupportMap>();
	
	public static SupportMapTable getInstance() {
		if (_instance == null) {
			_instance = new SupportMapTable();
		}
		return _instance;
	}
	
	public static void reload() {
		SupportMapTable oldInstance = _instance;
		_instance = new SupportMapTable();
		oldInstance._list.clear();
	}
	
	private SupportMapTable(){
		loadSupportMap();
	}
	
	private void loadSupportMap(){
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			if(_list.size() > 0){
				_list.clear();
			}
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM support_system");
			rs = pstm.executeQuery();
			while(rs.next()) {
				L1SupportMap SM = new L1SupportMap();
				int mapId = rs.getInt("MapId");
				SM.setName(rs.getString("MapName"));
				SM.setExpRate(rs.getInt("ExpPer") * 0.01);
				SM.setAdenaChance(rs.getInt("AdenaChance") * 0.01);
				_list.put(mapId, SM);
			}
			
		}catch(SQLException e) {
			e.printStackTrace();
		}finally {
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public L1SupportMap getSupportMap(int mapid){
		return _list.get(mapid);
	}
	
	public boolean isSupportMap(int mapid){
		Set<Integer> keys = _list.keySet();
		int givemon;
		boolean OK = false;
		for (Iterator<Integer> iterator = keys.iterator(); iterator.hasNext();) {
			givemon = iterator.next();
			if(givemon == mapid){
				OK = true;
				break;
			}
		}
		return OK;
	}
}