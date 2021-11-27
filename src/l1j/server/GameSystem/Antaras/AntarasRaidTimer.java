package l1j.server.GameSystem.Antaras;

import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.model.Instance.L1NpcInstance;

public class AntarasRaidTimer extends TimerTask {

	private final AntarasRaid _ar;
	private final int _type;
	private final int _timeMillis;
	private final L1NpcInstance _anta;

	public AntarasRaidTimer(L1NpcInstance anta, AntarasRaid ar, int type,
			int timeMillis) {
		_ar = ar;
		_type = type;
		_timeMillis = timeMillis;
		_anta = anta;
	}

	@Override
	public void run() {
		try {
			_ar.timeOverRun(_type);
			if (_anta != null) {
				_anta.skilluse = false;
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