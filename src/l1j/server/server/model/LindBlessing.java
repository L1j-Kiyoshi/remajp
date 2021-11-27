package l1j.server.server.model;

import java.util.TimerTask;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_EffectLocation;

public class LindBlessing extends TimerTask {
	// private static Logger _log =
	// Logger.getLogger(PapuBlessing.class.getName());

	private final L1PcInstance _pc;

	public LindBlessing(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}
			Papuregen();
		} catch (Throwable e) {
			e.printStackTrace();
			// _log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void Papuregen() {
		// _pc.setCurrentMp(_pc.getCurrentMp() + 10);
		_pc.sendPackets(new S_EffectLocation(_pc.getX(), _pc.getY(),
				(short) 763), true);
		Broadcaster
				.broadcastPacket(_pc,
						new S_EffectLocation(_pc.getX(), _pc.getY(),
								(short) 763), true);
	}
}
