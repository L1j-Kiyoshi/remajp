package server.threads.pc;

import java.util.Random;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;

public class DollObserverThread extends Thread {

	private static DollObserverThread _instance;
	// private static Logger _log =
	// Logger.getLogger(DollObserverThread.class.getName());
	Random rnd = new Random();

	public static DollObserverThread getInstance() {
		if (_instance == null) {
			_instance = new DollObserverThread();
			_instance.start();
		}
		return _instance;
	}

	public DollObserverThread() {
		super("server.threads.pc.DollObserverThread");
	}

	public void run() {
		//System.out.println(DollObserverThread.class.getName() + " Ω√¿€");
		while (true) {
			try {
				for (L1PcInstance _client : L1World.getInstance()
						.getAllPlayers()) {
					if (_client instanceof L1RobotInstance) {
						continue;
					}
					if (_client == null || _client.getNetConnection() == null) {
						continue;
					} else {
						for (L1DollInstance doll : _client.getDollList()) {
							S_DoActionGFX da = new S_DoActionGFX(doll.getId(),
									66 + rnd.nextInt(2));
							_client.sendPackets(da);
							Broadcaster.broadcastPacket(_client, da, true);
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				try {
					Thread.sleep(15000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}
}
