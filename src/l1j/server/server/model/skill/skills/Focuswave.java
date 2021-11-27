package l1j.server.server.model.skill.skills;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillBrave;

public class Focuswave {
	public static void runSkill(L1Character cha, int buffIconDuration) {
		L1PcInstance pc = (L1PcInstance) cha;
		pc.getMoveState().setBraveSpeed(1);
		pc.sendPackets(new S_SkillBrave(pc.getId(), 12, buffIconDuration));
		Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 12, 0));
	}
}
