package l1j.server.server.model;

import java.util.TimerTask;

import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;

public class MpRegenerationByDoll extends TimerTask {
	// private static Logger _log =
	// Logger.getLogger(MpRegenerationByDoll.class.getName());

	private final L1PcInstance _pc;

	public MpRegenerationByDoll(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead()) {
				return;
			}
			regenMp();
		} catch (Throwable e) {
			e.printStackTrace();
			// _log.log(Level.WARNING, e.getLocalizedMessage(), e);
		}
	}

	public void regenMp() {
		int regenMp = 0;
		int dolltype = 0;
		for (L1DollInstance doll : _pc.getDollList()) {
			dolltype = doll.getDollType();
			regenMp = doll.getMpRegenerationValues();
		}

		int newMp = _pc.getCurrentMp() + regenMp;

		_pc.setCurrentMp(newMp);
		if (dolltype == L1DollInstance.DOLLTYPE_남자_여자) {
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 6319));
			Broadcaster.broadcastPacket(_pc,
					new S_SkillSound(_pc.getId(), 6319));
		} else if (dolltype != L1DollInstance.DOLLTYPE_싸이) {
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 6321));
			Broadcaster.broadcastPacket(_pc,
					new S_SkillSound(_pc.getId(), 6321));
		}

	}

}
