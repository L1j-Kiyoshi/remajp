package server.threads.pc;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.Teleportation;

public class SabuTel implements Runnable {
	private L1PcInstance _pc = null;
	public long _time = 150;

	public SabuTel(long time, L1PcInstance pc) {
		_time = time;
		_pc = pc;
	}

	public void run() {
		try {
			if (_pc == null || _pc instanceof L1RobotInstance) {
				return;
			}
			if (_pc.getNetConnection() == null) {
				return;
			}
			Teleportation.doTeleportation(_pc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
