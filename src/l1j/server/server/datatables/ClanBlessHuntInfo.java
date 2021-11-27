package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.Astar.share.TimeLine;
import l1j.server.server.model.L1Clan;
import l1j.server.server.utils.SQLUtil;

public class ClanBlessHuntInfo {
	private static ClanBlessHuntInfo _instance;
	private Map<Integer, HuntInfo> _huntList = new HashMap<Integer, HuntInfo>();

	public static ClanBlessHuntInfo getInstance() {
		if (_instance == null) {
			_instance = new ClanBlessHuntInfo();
		}
		return _instance;
	}

	private ClanBlessHuntInfo() {
		TimeLine.start("loading ClanBlessHuntInfo...");

		load(_huntList);

		TimeLine.end();
	}

	private void load(Map<Integer, HuntInfo> huntList) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		HuntInfo hi = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_bless_hunt_info");
			rs = pstm.executeQuery();
			while (rs.next()) {
				hi = new HuntInfo();
				int number = rs.getInt("mapNumber");
				hi.setId(number);
				hi.setName(rs.getString("mapName"));
				hi.setTeleportLocX(rs.getInt("loc_x"));
				hi.setTeleportLocY(rs.getInt("loc_y"));
				hi.setTeleportMapId(rs.getInt("loc_map"));
				hi.setChangePrice(rs.getInt("change_price"));

				try {
					String[] result = rs.getString("mapId").split(",");
					ArrayList<Integer> maps = new ArrayList<Integer>();
					for (String stt : result) {
						maps.add(Integer.valueOf(stt));
					}
					hi.setMapList(maps);
				} catch (Exception e) {
					e.printStackTrace();
				}

				huntList.put(number, hi);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
	}

	public void reload() {
		TimeLine.start("loading ClanBlessHuntInfo...");

		Map<Integer, HuntInfo> huntList = new HashMap<Integer, HuntInfo>();
		load(huntList);
		_huntList = huntList;

		TimeLine.end();
	}

	public HuntInfo getClanBlessHuntInfo(int i) {
		return _huntList.get(i);
	}
	
	public ArrayList<Integer> getMapList(int i){
		return _huntList.get(i).getMapList();
	}

	public void settingClanBlessHuntMaps(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM clan_bless_hunt_info ORDER BY RAND() LIMIT 3");
			rs = pstm.executeQuery();
			int count = 0;
			ArrayList<Integer> maps = new ArrayList<Integer>();
			ArrayList<Integer> type = new  ArrayList<Integer>();
			ArrayList<Integer> mapids = new  ArrayList<Integer>();
			while (rs.next()) {
				maps.add(rs.getInt("mapNumber"));
				type.add(1);
				mapids.add(-1);
				count++;
				if(count >= 3){
					break;
				}
			}
			
			clan.setBlessHuntMapIds(mapids);
			clan.setHuntMapChoice(false);
			clan.setBlessHuntMaps(maps);
			clan.setBlessHuntMapsType(type);
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public class HuntInfo {
		private int _id;
		private ArrayList<Integer> _maplist = new ArrayList<Integer>();
		private String _name;
		private int _locx;
		private int _locy;
		private int _locmap;
		private int _change_price;

		public int getId() {
			return _id;
		}

		public void setId(int i) {
			_id = i;
		}

		public ArrayList<Integer> getMapList() {
			return _maplist;
		}

		public void setMapList(ArrayList<Integer> i) {
			_maplist = i;
		}

		public String getName() {
			return _name;
		}

		public void setName(String name) {
			_name = name;
		}

		public int getTeleportLocX() {
			return _locx;
		}

		public void setTeleportLocX(int i) {
			_locx = i;
		}

		public int getTeleportLocY() {
			return _locy;
		}

		public void setTeleportLocY(int i) {
			_locy = i;
		}

		public int getTeleportMapId() {
			return _locmap;
		}

		public void setTeleportMapId(int i) {
			_locmap = i;
		}

		public int getChangePrice() {
			return _change_price;
		}

		public void setChangePrice(int i) {
			_change_price = i;
		}
	}
}
