package l1j.server.MJTemplate.MJSqlHelper.Property;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface MJIPropertyHandler {
	public void on_load(String section, MJSqlPropertyReader reader, ResultSet rs) throws SQLException;
}
