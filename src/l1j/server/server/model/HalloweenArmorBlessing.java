package l1j.server.server.model;

import java.util.TimerTask;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;

public class HalloweenArmorBlessing extends TimerTask {
	// private static Logger _log =
	// Logger.getLogger(PapuBlessing.class.getName());

	private final L1PcInstance _pc;

	public HalloweenArmorBlessing(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}
			Halloweenregen();
		} catch (Throwable e) {
			e.printStackTrace();
			// _log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void Halloweenregen() {
		_pc.sendPackets(new S_SkillSound(_pc.getId(), 12214));
		// Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(),
		// 2245));
	}
}
