package l1j.server.server.model.skill.skills;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

public class ElementalFallDown {
	public static void runSkill(L1Character _user, L1PcInstance _player,
			L1Character cha) {
		if (_user instanceof L1PcInstance) {
			int playerAttr = _player.getElfAttr();
			int i = -50;
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				switch (playerAttr) {
				case 0:
					_player.sendPackets(new S_ServerMessage(79));
					break;
				case 1:
					pc.getResistance().addEarth(i);
					pc.setAddAttrKind(1);
					break;
				case 2:
					pc.getResistance().addFire(i);
					pc.setAddAttrKind(2);
					break;
				case 4:
					pc.getResistance().addWater(i);
					pc.setAddAttrKind(4);
					break;
				case 8:
					pc.getResistance().addWind(i);
					pc.setAddAttrKind(8);
					break;
				case 21:
					pc.getResistance().addFire(i);
					pc.getResistance().addEarth(i);
					pc.setAddAttrKind(21);
					break;
				case 24:
					pc.getResistance().addFire(i);
					pc.getResistance().addWater(i);
					pc.setAddAttrKind(24);
					break;
				case 28:
					pc.getResistance().addFire(i);
					pc.getResistance().addWind(i);
					pc.setAddAttrKind(28);
					break;
				case 41:
					pc.getResistance().addWater(i);
					pc.getResistance().addEarth(i);
					pc.setAddAttrKind(41);
					break;
				case 48:
					pc.getResistance().addWater(i);
					pc.getResistance().addWind(i);
					pc.setAddAttrKind(48);
					break;
				case 81:
					pc.getResistance().addWind(i);
					pc.getResistance().addEarth(i);
					pc.setAddAttrKind(81);
					break;
				default:
					break;
				}
			} else if (cha instanceof L1MonsterInstance) {
				L1MonsterInstance mob = (L1MonsterInstance) cha;
				switch (playerAttr) {
				case 0:
					_player.sendPackets(new S_ServerMessage(79));
					break;
				case 1:
					mob.getResistance().addEarth(i);
					mob.setAddAttrKind(1);
					break;
				case 2:
					mob.getResistance().addFire(i);
					mob.setAddAttrKind(2);
					break;
				case 4:
					mob.getResistance().addWater(i);
					mob.setAddAttrKind(4);
					break;
				case 8:
					mob.getResistance().addWind(i);
					mob.setAddAttrKind(8);
					break;
				case 21:
					mob.getResistance().addFire(i);
					mob.getResistance().addEarth(i);
					mob.setAddAttrKind(21);
					break;
				case 24:
					mob.getResistance().addFire(i);
					mob.getResistance().addWater(i);
					mob.setAddAttrKind(24);
					break;
				case 28:
					mob.getResistance().addFire(i);
					mob.getResistance().addWind(i);
					mob.setAddAttrKind(28);
					break;
				case 41:
					mob.getResistance().addWater(i);
					mob.getResistance().addEarth(i);
					mob.setAddAttrKind(41);
					break;
				case 48:
					mob.getResistance().addWater(i);
					mob.getResistance().addWind(i);
					mob.setAddAttrKind(48);
					break;
				case 81:
					mob.getResistance().addWind(i);
					mob.getResistance().addEarth(i);
					mob.setAddAttrKind(81);
					break;
				default:
					break;
				}
			}
		}
	}
}
