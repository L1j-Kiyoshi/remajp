package l1j.server.server.model.gametime;

public class GameTime extends BaseTime {
	// 2003년 7월 3일 12:00(UTC)이 1월 1일00:00
	protected static final long BASE_TIME_IN_MILLIS_REAL = 1483196400186L;
	
	@Override
	protected long getBaseTimeInMil() {
		return BASE_TIME_IN_MILLIS_REAL;
	}

	@Override
	protected int makeTime(long timeMillis) {
		long t1 = timeMillis - getBaseTimeInMil();
		if (t1 < 0L)
			throw new IllegalArgumentException();
		t1 %= 86400000L;
		int t2 = (int) (t1 * 6L / 1000L);
		int t3 = t2 % 3;
		return t2 - t3;
	}
}