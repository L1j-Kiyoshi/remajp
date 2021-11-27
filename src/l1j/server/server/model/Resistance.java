package l1j.server.server.model;

import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.IntRange;

public class Resistance {
	private static final int LIMIT_MIN = -128;
	private static final int LIMIT_MAX = 127;
	private static final int LIMIT_MIN_MR = -350;
	private static final int LIMIT_MAX_MR = 350;

	private int baseMr = 0; // 기본 마법 방어
	private int addedMr = 0; // 아이템이나 마법에 의해 추가된 마법 방어를 포함한 마법 방어

	private int fire = 0; // 불 저항
	private int water = 0; // 물 저항
	private int wind = 0; // 바람 저항
	private int earth = 0; // 땅 저항

	private int calcPcDefense = 0; //잊섬아이템리뉴얼 대미지감소
	private int PVPweaponTotalDamage = 0; //잊섬아이템리뉴얼 추가대미지

	private L1Character character = null;

	public Resistance() {
	}

	public Resistance(L1Character cha) {
		init();
		character = cha;
	}

	public void init() {
		baseMr = addedMr = 0;
		fire = water = wind = earth = 0;
		calcPcDefense = PVPweaponTotalDamage = 0;
	}

	private int checkMrRange(int i, final int MIN) {
		return IntRange.ensure(i, MIN, LIMIT_MAX_MR);
	}

	private byte checkRange(int i) {
		return (byte) IntRange.ensure(i, LIMIT_MIN, LIMIT_MAX);
	}

	public int getEffectedMrBySkill() {
		int effectedMr = getMr();
		if (character.getSkillEffectTimerSet().hasSkillEffect(
				L1SkillId.ERASE_MAGIC))
			effectedMr /= 2; // 25%
/*		else if (character.getSkillEffectTimerSet().hasSkillEffect(
				L1SkillId.CUBE_SHOCK))
			effectedMr -= (effectedMr / 4); // -25%
*/
		return effectedMr;
	}

	public int getAddedMr() {
		return addedMr;
	}

	public int getMr() {
		return checkMrRange(baseMr + addedMr, LIMIT_MIN_MR);
	}

	public int getBaseMr() {
		return baseMr;
	}

	public void addMr(int i) {
		setAddedMr(addedMr + i);
	}

	public void setBaseMr(int i) {
		baseMr = checkMrRange(i, LIMIT_MIN_MR);
	}

	private void setAddedMr(int i) {
		addedMr = checkMrRange(i, -baseMr);
	}

	public int getcalcPcDefense() {
    	 return calcPcDefense;
    } 
     
	 public int getPVPweaponTotalDamage() {
		 return PVPweaponTotalDamage;
	} 
	 
	public int getFire() {
		return fire;
	}

	public int getWater() {
		return water;
	}

	public int getWind() {
		return wind;
	}

	public int getEarth() {
		return earth;
	}

	public void addFire(int i) {
		fire = checkRange(fire + i);
	}

	public void addWater(int i) {
		water = checkRange(water + i);
	}

	public void addWind(int i) {
		wind = checkRange(wind + i);
	}

	public void addEarth(int i) {
		earth = checkRange(earth + i);
	}

	public void addcalcPcDefense(int i) {
		calcPcDefense = checkRange(calcPcDefense + i);
	} 
    
	public void addPVPweaponTotalDamage(int i) { 
    	PVPweaponTotalDamage = checkRange(
    	PVPweaponTotalDamage + i);
    	}

	public void addAllNaturalResistance(int i) {
		addFire(i);
		addWater(i);
		addWind(i);
		addEarth(i);
	}


}
