package l1j.server.MJTemplate.MJSqlHelper.Handler;

import java.sql.PreparedStatement;

public interface Handler {
	public void handle(PreparedStatement pstm) throws Exception;
}
