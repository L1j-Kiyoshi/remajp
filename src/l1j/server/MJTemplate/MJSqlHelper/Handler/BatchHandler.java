package l1j.server.MJTemplate.MJSqlHelper.Handler;

import java.sql.PreparedStatement;

public abstract class BatchHandler implements Handler{
	private int _callNumber = 0;
	@Override
	public void handle(PreparedStatement pstm) throws Exception{
		handle(pstm, _callNumber++);
	}
	
	public abstract void handle(PreparedStatement pstm, int callNumber) throws Exception;
}
