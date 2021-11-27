package l1j.server.server.model;

import java.util.TimerTask;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Party;

public class L1PartyRefresh extends TimerTask {

	private final L1PcInstance _pc;

	public L1PartyRefresh(L1PcInstance pc) {
		_pc = pc;
	}

	@Override
	public void run() {
		try {
			if (_pc.isDead() || _pc.getParty() == null) {
				_pc.stopRP();
				return;
			}
			rp();
		} catch (Throwable e) {
			e.printStackTrace();
			_pc.stopRP();
		}
	}

	public void rp() {
		_pc.sendPackets(new S_Party(0x6e, _pc));
	}

}
