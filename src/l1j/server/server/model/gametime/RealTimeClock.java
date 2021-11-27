package l1j.server.server.model.gametime;

import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.server.GeneralThreadPool;

public class RealTimeClock {
	private static RealTimeClock _instance;
	private volatile RealTime _currentTime = new RealTime();
	private RealTime _previousTime = null;
	private List<TimeListener> _listeners = new CopyOnWriteArrayList<TimeListener>();

	private class TimeUpdater implements Runnable {
		@Override
		public void run() {
			// while (true) {
			try {
				_previousTime = null;
				_previousTime = _currentTime;
				_currentTime = new RealTime();
				notifyChanged();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// try{
				// Thread.sleep(500);
				// }catch(Exception e){}
				GeneralThreadPool.getInstance().schedule(this, 500);
			}
			// }
		}
	}

	private boolean isFieldChanged(int field) {
		return _previousTime.get(field) != _currentTime.get(field);
	}

	private void notifyChanged() {
		if (isFieldChanged(Calendar.MONTH)) {
			for (TimeListener listener : _listeners) {
				try {
					if (listener != null)
						listener.onMonthChanged(_currentTime);
				} catch (Exception e) {
					System.out
							.println("-- RealTime Clock onMonthChaned Exception Error --");
					e.printStackTrace();
				}
			}
		}
		if (isFieldChanged(Calendar.DAY_OF_MONTH)) {
			for (TimeListener listener : _listeners) {
				try {
					if (listener != null)
						listener.onDayChanged(_currentTime);
				} catch (Exception e) {
					System.out
							.println("-- RealTime Clock onDayChanged Exception Error --");
					e.printStackTrace();
				}
			}
		}
		if (isFieldChanged(Calendar.HOUR_OF_DAY)) {
			for (TimeListener listener : _listeners) {
				try {
					if (listener != null)
						listener.onHourChanged(_currentTime);
				} catch (Exception e) {
					System.out
							.println("-- RealTime Clock onHourChanged Exception Error --");
					e.printStackTrace();
				}
			}
		}
		if (isFieldChanged(Calendar.MINUTE)) {
			for (TimeListener listener : _listeners) {
				try {
					if (listener != null)
						listener.onMinuteChanged(_currentTime);
				} catch (Exception e) {
					System.out
							.println("-- RealTime Clock onMinuteChanged Exception Error --");
					e.printStackTrace();
				}
			}
		}
	}

	private RealTimeClock() {
		GeneralThreadPool.getInstance().execute(new TimeUpdater());
	}

	public static void init() {
		_instance = new RealTimeClock();
	}

	public static RealTimeClock getInstance() {
		return _instance;
	}

	public RealTime getRealTime() {
		return _currentTime;
	}

	public void addListener(TimeListener listener) {
		_listeners.add(listener);
	}

	public void removeListener(TimeListener listener) {
		_listeners.remove(listener);
	}

	public Calendar getRealTimeCalendar() {
		Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT+9")); // 한국
																			// 시간
		return cal;
	}
}
