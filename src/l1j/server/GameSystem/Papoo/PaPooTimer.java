package l1j.server.GameSystem.Papoo;

import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.model.Instance.L1NpcInstance;

public class PaPooTimer extends TimerTask {

	private final PaPooRaid _ar;
	private final int _type;
	private final int _timeMillis;
	private final L1NpcInstance _papoo;

	public PaPooTimer(L1NpcInstance papoo, PaPooRaid ar, int type,
			int timeMillis) {
		_ar = ar;
		_type = type;
		_timeMillis = timeMillis;
		_papoo = papoo;
	}

	@Override
	public void run() {
		try {
			_ar.timeOverRun(_type);
			if (_papoo != null) {
				_papoo.skilluse = false;
			}
			this.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void begin() {
		Timer timer = new Timer();
		timer.schedule(this, _timeMillis);
	}
}