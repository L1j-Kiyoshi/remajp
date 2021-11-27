package l1j.server.server.templates;

public class L1PetSkill {
	private final int _SkillNumber;
	private int _SkillLevel;

	public L1PetSkill(int SkillNumber, int SkillLevel){
		_SkillNumber = SkillNumber;
		_SkillLevel = SkillLevel;
	}
			
	public int getSkillNumber() {
		return _SkillNumber;
	}
	
	public int getSkillLevel() {
		return _SkillLevel;
	}
	
	public void setSkillLevel(int SkillLevel) {
		_SkillLevel = SkillLevel;
	}
}