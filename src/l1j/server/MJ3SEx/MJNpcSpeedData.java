package l1j.server.MJ3SEx;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;
import l1j.server.server.model.Instance.L1NpcInstance;

public class MJNpcSpeedData {
	private static HashMap<Integer, MJNpcSpeedData> m_npc_speed_data;
	public static void do_load(){
		HashMap<Integer, MJNpcSpeedData> npc_speed_data = new HashMap<Integer, MJNpcSpeedData>();
		Selector.exec("select * from npc_speed_data", new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next()){
					MJNpcSpeedData o = newInstance(rs);
					npc_speed_data.put(o.get_monster_id(), o);
				}
			}
		});
		m_npc_speed_data = npc_speed_data;
	}
	
	public static void install_npc(L1NpcInstance npc){
		MJNpcSpeedData data = m_npc_speed_data.get(npc.getNpcId());
		if(data == null)
			return;
		/**임시주석
		npc.setMoveSpeed(data.get_speed());
		npc.setBraveSpeed(data.get_brave());
		*/
	}
	
	private static MJNpcSpeedData newInstance(ResultSet rs) throws SQLException{
		return newInstance()
				.set_monster_id(rs.getInt("monster_id"))
				.set_speed(rs.getInt("speed"))
				.set_brave(rs.getInt("brave"));
	}

	private static MJNpcSpeedData newInstance(){
		return new MJNpcSpeedData();
	}

	private int m_monster_id;
	private int m_speed;
	private int m_brave;
	private MJNpcSpeedData(){}

	public MJNpcSpeedData set_monster_id(int monster_id){
		m_monster_id = monster_id;
		return this;
	}
	public MJNpcSpeedData set_speed(int speed){
		m_speed = speed;
		return this;
	}
	public MJNpcSpeedData set_brave(int brave){
		m_brave = brave;
		return this;
	}
	public int get_monster_id(){
		return m_monster_id;
	}
	public int get_speed(){
		return m_speed;
	}
	public int get_brave(){
		return m_brave;
	}

}

