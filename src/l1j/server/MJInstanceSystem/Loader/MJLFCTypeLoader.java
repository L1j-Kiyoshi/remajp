package l1j.server.MJInstanceSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJInstanceSystem.MJLFC.MJLFCType;
import l1j.server.server.utils.SQLUtil;

public class MJLFCTypeLoader {
	private static final Object _lock = new Object();
	private static MJLFCTypeLoader _instance;
	public static MJLFCTypeLoader getInstance(){
		if(_instance == null)
			_instance = new MJLFCTypeLoader();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJLFCTypeLoader tmp 	= _instance;
		_instance 					= new MJLFCTypeLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	private HashMap<Integer, MJLFCType> _types;
	private MJLFCTypeLoader(){
		_types	= new HashMap<Integer, MJLFCType>(8);
		Connection 			con 	= null;
		PreparedStatement 	pstm 	= null;
		ResultSet 			rs 		= null;
		MJLFCType			type	= null;
		String field				= null;
		try{
			con 		= L1DatabaseFactory.getInstance().getConnection();
			pstm		= con.prepareStatement("select * from TB_LFCTYPES");
			rs			= pstm.executeQuery();
			while(rs.next()){
				type	= new MJLFCType();
				field = "ID";
				type.setId(rs.getInt(field));
				field = "TYPE";
				type.setPvp(rs.getInt(field));
				field = "NAME";
				type.setName(rs.getString(field));
				field = "USE";
				type.setUse(rs.getInt(field));
				field = "BUFF_SPAWN_TIME";
				type.setBuffSpawnSecond(rs.getInt(field));
				field = "POSSIBLE_LEVEL";
				type.setPossibleLevel(rs.getInt(field));
				field = "MIN_PARTY";
				type.setMinParty(rs.getInt(field));
				field = "MAX_PARTY";
				type.setMaxParty(rs.getInt(field));
				field = "NEED_ITEMID";
				type.setNeedItemId(rs.getInt(field));
				field = "NEED_ITEMCOUNT";
				type.setNeedItemCount(rs.getInt(field));
				field = "PLAY_INST";
				type.setPlayInstName(rs.getString(field));
				field = "MAPRT";
				type.setMapRect(rs.getInt("MAPRT_LEFT"), rs.getInt("MAPRT_TOP"), rs.getInt("MAPRT_RIGHT"), rs.getInt("MAPRT_BOTTOM"));
				field = "MAPID";
				type.setBaseMapId(rs.getShort(field));
				field = "STARTPOS";
				type.setStartupPosition(rs.getInt("STARTPOS_REDX"), rs.getInt("STARTPOS_REDY"), rs.getInt("STARTPOS_BLUEX"), rs.getInt("STARTPOS_BLUEY"));
				field = "PLAYTIME";
				type.setPlaySecond(rs.getInt(field));
				field = "READYTIME";
				type.setReadySecond(rs.getInt(field));
				field = "RAND_WINNER_RATIO";
				type.setRandomCompensateRatio(rs.getInt(field));
				_types.put(type.getId(), type);		
			}
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			if(type.getName() == null)	sb.append("null");
			else						sb.append(type.getName());
			sb.append("(");
			sb.append(type.getId());
			sb.append(", field : ");
			if(field == null)			sb.append("null");
			else						sb.append(field);
			sb.append(")을(를) 불러들이는데 실패했습니다.");
			System.out.println(sb.toString());
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public MJLFCType get(int i){
		return _types.get(i);
	}
	
	public void clear(){
		if(_types != null){
			_types.clear();
			_types = null;
		}
	}
}
