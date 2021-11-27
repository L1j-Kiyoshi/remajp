package l1j.server.GameSystem.INN;

import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SabuTell;

public class InnTimer extends TimerTask {
	private int _Å°¸Ê¹øÈ£;
	private int _Å°°¹¼ö;
	private int _timeMillis;
private boolean exit = false;
	public InnTimer(int Å°¸Ê¹øÈ£, int Å°°¹¼ö, int timeMillis) {
		_Å°¸Ê¹øÈ£ = Å°¸Ê¹øÈ£;
		_Å°°¹¼ö = Å°°¹¼ö;
		_timeMillis = timeMillis;
	}

	public synchronized void Å°Â÷°¨(int count) {
		try {
			int check = _Å°°¹¼ö - count;
			if (check <= 0) {
				run();
			} else {
				_Å°°¹¼ö = check;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public synchronized void run() {
		try {
			if(exit)return;
			exit = true;
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.getMapId() == _Å°¸Ê¹øÈ£) {
					pc.setTelType(5);
					S_SabuTell st = new S_SabuTell(pc);
					pc.sendPackets(st, true);
				}
			}
			//System.out.println("111111111111111111111111");
			INN.setINN(_Å°¸Ê¹øÈ£, false);
			INN.setInnTimer(_Å°¸Ê¹øÈ£, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void begin() {
		Timer timer = new Timer();
		timer.schedule(this, _timeMillis);
	}
}