package l1j.server.MJTemplate.MJSqlHelper.Handler;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public abstract class SelectorHandler implements Handler{	
	@Override
	public abstract void handle(PreparedStatement pstm) throws Exception;
	public abstract void result(ResultSet rs) throws Exception;
}
