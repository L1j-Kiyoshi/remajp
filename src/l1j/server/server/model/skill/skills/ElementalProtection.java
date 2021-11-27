package l1j.server.server.model.skill.skills;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

public class ElementalProtection {

	public static void runSkill(L1Character cha) {
		L1PcInstance pc = (L1PcInstance) cha;
		int attr = pc.getElfAttr();
		if (attr == 1) {
			pc.getResistance().addEarth(50);
		} else if (attr == 2) {
			pc.getResistance().addFire(50);
		} else if (attr == 4) {
			pc.getResistance().addWater(50);
		} else if (attr == 8) {
			pc.getResistance().addWind(50);
		} else if (attr == 21) {
			pc.getResistance().addFire(50);
			pc.getResistance().addEarth(50);
		} else if (attr == 24) {
			pc.getResistance().addFire(50);
			pc.getResistance().addWater(50);
		} else if (attr == 28) {
			pc.getResistance().addFire(50);
			pc.getResistance().addWind(50);
		} else if (attr == 41) {
			pc.getResistance().addWater(50);
			pc.getResistance().addEarth(50);
		} else if (attr == 48) {
			pc.getResistance().addWater(50);
			pc.getResistance().addWind(50);
		} else if (attr == 81) {
			pc.getResistance().addWind(50);
			pc.getResistance().addEarth(50);
		}
	}
}
