package l1j.server.MJTemplate.MJSqlHelper;

import l1j.server.MJTemplate.MJSqlHelper.Handler.Handler;

public interface Executor {
	public void execute(String query, Handler handler);
}
