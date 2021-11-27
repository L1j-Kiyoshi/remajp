package l1j.server.MJCTSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJCTSystem.MJCTSpell;
import l1j.server.server.utils.SQLUtil;
/** 
 * MJCTCharInfo
 * MJSoft Character TradeSystem - Spell Loader.
 * spell icon loader
 * made by mjsoft, 2016.
 **/
public class MJCTSpellLoader {
	private static MJCTSpellLoader _instance;
	public static MJCTSpellLoader getInstance(){
		if(_instance == null)
			_instance = new MJCTSpellLoader();
		return _instance;
	}
	
	public static void release(){
		
	}
	
	private HashMap<Integer, MJCTSpell> _spells;
	private MJCTSpellLoader(){
		Connection 			con		= null;
		PreparedStatement 	pstm	= null;
		ResultSet 			rs		= null;
		MJCTSpell			sp		= null;
		_spells						= new HashMap<Integer, MJCTSpell>(256);
		try{
			con		= L1DatabaseFactory.getInstance().getConnection();
			pstm	= con.prepareStatement("select * from tb_mjct_spellIcon");
			rs		= pstm.executeQuery();
			while(rs.next()){
				sp 			= new MJCTSpell();
				sp.id		= rs.getInt("spellId") + 1;
				sp.name		= rs.getString("name");
				sp.icon		= rs.getInt("icon");
				sp.xicon	= rs.getInt("xicon");
				_spells.put(sp.id, sp);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs, pstm, con);
		}
	}
	
	public MJCTSpell get(int i){
		return _spells.get(i);
	}
	
	public void clear(){
		if(_spells != null){
			_spells.clear();
			_spells = null;
		}
	}
}
