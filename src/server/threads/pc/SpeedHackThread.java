package server.threads.pc;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class SpeedHackThread implements Runnable {

	private static SpeedHackThread _instance;

	public static SpeedHackThread getInstance() {
		if (_instance == null) {
			_instance = new SpeedHackThread();
		}
		return _instance;
	}

	public SpeedHackThread() {
		// super("server.threads.pc.SpeedHackThread");
		GeneralThreadPool.getInstance().schedule(this, 1000);
	//	System.out.println(SpeedHackThread.class.getName() + " Ω√¿€");
	}

	public void run() {
		check_Hacktimer();
		GeneralThreadPool.getInstance().schedule(this, 1000);
		/*
		 * while(true){ try {
		 * 
		 * //System.out.println("UseMemory : " + SystemUtil.getUsedMemoryMB() +
		 * " MB"); check_Hacktimer(); Thread.sleep(1000); } catch
		 * (InterruptedException e) { 
		 * e.printStackTrace(); } }
		 */
	}

	public void check_Hacktimer() {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc instanceof L1RobotInstance) {
					continue;
				}
				if (pc == null || pc.getNetConnection() == null) {
					continue;
				}
				pc.increase_hackTimer();
			}
		} catch (Exception e) {
		}
	}
}
