package l1j.server.MJTemplate.MJSqlHelper.Property;

import java.sql.ResultSet;
import java.sql.SQLException;
import l1j.server.MJTemplate.MJSqlHelper.Executors.Selector;
import l1j.server.MJTemplate.MJSqlHelper.Handler.FullSelectorHandler;

public class MJSqlPropertyReader {
	public static void do_exec(String table_name, String section_name, String value_name, MJIPropertyHandler handler){
		newInstance(table_name, section_name, value_name, handler).execute();
	}
	
	private static MJSqlPropertyReader newInstance(String table_name, String section_name, String value_name, MJIPropertyHandler handler){
		return newInstance()
				.set_table_name(table_name)
				.set_section_name(section_name)
				.set_value_name(value_name)
				.set_handler(handler);
	}
	private static MJSqlPropertyReader newInstance(){
		return new MJSqlPropertyReader();
	}
	
	private String m_table_name;
	private String m_section_name;
	private String m_value_name;
	private MJIPropertyHandler m_handler;
	private MJSqlPropertyReader(){
	}
	public void execute(){
		Selector.exec(String.format("select %s, %s from %s", m_section_name, m_value_name, m_table_name), new FullSelectorHandler(){
			@Override
			public void result(ResultSet rs) throws Exception {
				while(rs.next())
					m_handler.on_load(rs.getString(m_section_name), MJSqlPropertyReader.this, rs);
			}
		});
	}
	
	public MJSqlPropertyReader set_table_name(String table_name){
		m_table_name = table_name;
		return this;
	}
	public String get_table_name(){
		return m_table_name;
	}
	public MJSqlPropertyReader set_section_name(String section_name){
		m_section_name = section_name;
		return this;
	}
	public String get_section_name(){
		return m_section_name;
	}
	public MJSqlPropertyReader set_value_name(String value_name){
		m_value_name = value_name;
		return this;
	}
	public String get_value_section_name(){
		return m_value_name;
	}
	public MJSqlPropertyReader set_handler(MJIPropertyHandler handler){
		m_handler = handler;
		return this;
	}
	public MJIPropertyHandler get_handler(){
		return m_handler;
	}
	
	public String read_string(ResultSet rs) throws SQLException{
		return rs.getString(m_value_name);
	}
	
	public int read_int(ResultSet rs) throws SQLException{
		return rs.getInt(m_value_name);
	}
	
	public double read_double(ResultSet rs) throws NumberFormatException, SQLException{
		return Double.parseDouble(read_string(rs));
	}
	
	public double read_double_by_percent(ResultSet rs) throws SQLException{
		return (double)read_int(rs) * 0.01;
	}
	public boolean read_boolean(ResultSet rs) throws SQLException{
		return Boolean.parseBoolean(read_string(rs));
	}
}
