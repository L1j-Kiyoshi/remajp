package l1j.server.MJ3SEx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class MJSprBoundary {
	private static HashMap<Integer, MJSprBoundary> m_boundaries;
	public static void do_load(){
		HashMap<Integer, MJSprBoundary> boundaries = new HashMap<Integer, MJSprBoundary>();
		Selector.exec("select * from spr_boundary", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJSprBoundary o = newInstance(rs);
					boundaries.put(o.get_sprite_id(), o);
				}
			}
		});
		m_boundaries = boundaries;
	}
	
	public static MJSprBoundary get_boundary(int sprite_id){
		return m_boundaries.get(sprite_id);
	}

	private static MJSprBoundary newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_sprite_id(rs.getInt("sprite_id"))
				.set_boundary_level(rs.getInt("boundary_level"));
	}

	private static MJSprBoundary newInstance(){
		return new MJSprBoundary();
	}

	private int m_sprite_id;
	private int m_boundary_level;
	private MJSprBoundary(){}

	public MJSprBoundary set_sprite_id(int sprite_id){
		m_sprite_id = sprite_id;
		return this;
	}
	public MJSprBoundary set_boundary_level(int boundary_level){
		m_boundary_level = boundary_level;
		return this;
	}
	public int get_sprite_id(){
		return m_sprite_id;
	}
	public int get_boundary_level(){
		return m_boundary_level;
	}

}

