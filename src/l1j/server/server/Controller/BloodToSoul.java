package l1j.server.server.Controller;

import java.util.TimerTask;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;

public class BloodToSoul extends TimerTask {
	private final L1PcInstance _pc;

	public BloodToSoul(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			
			if(_pc == null || _pc.isDead() || _pc.getCurrentHp() <= 25) {
				return;
			}
			
			if(!(_pc.getInventory().consumeItem(30078, 1) || _pc.getInventory().consumeItem(40319, 1))) {
				return;
			}
			
			_pc.setCurrentHp(_pc.getCurrentHp() - 25);
			_pc.setCurrentMp(_pc.getCurrentMp() + 15);
			_pc.sendPackets(new S_SkillSound(_pc.getId(), 2178));
			_pc.broadcastPacket(new S_SkillSound(_pc.getId(), 2178));
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
