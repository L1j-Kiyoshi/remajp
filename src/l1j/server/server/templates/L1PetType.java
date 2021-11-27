package l1j.server.server.templates;

import java.util.ArrayList;

import l1j.server.server.datatables.NpcTable;
import l1j.server.server.utils.IntRange;

public class L1PetType {
	private final int _baseNpcId;
	private final L1Npc _baseNpcTemplate;
	private final String _name;
	private final int _petinfo;
	private final int _PetEffect;
	private final IntRange _hpUpRange;
	
	private ArrayList<L1PetSkill> _SkillOneStep;
	private ArrayList<L1PetSkill> _SkillTwoStep;
	private ArrayList<L1PetSkill> _SkillThreeStep;
	private ArrayList<L1PetSkill> _SkillFourStep;
	private ArrayList<L1PetSkill> _SkillFiveStep;
	
	public L1PetType(int baseNpcId, String name, int petinfo, int PetEffect, IntRange hpUpRange, 
		ArrayList<L1PetSkill> SkillOneStep, ArrayList<L1PetSkill> SkillTwoStep, ArrayList<L1PetSkill> SkillThreeStep,
		ArrayList<L1PetSkill> SkillFourStep, ArrayList<L1PetSkill> SkillFiveStep) {
		_baseNpcId = baseNpcId;
		_baseNpcTemplate = NpcTable.getInstance().getTemplate(baseNpcId);
		_name = name;
		_petinfo = petinfo;
		_PetEffect = PetEffect;
		_hpUpRange = hpUpRange;
		
		_SkillOneStep   = SkillOneStep;
		_SkillTwoStep   = SkillTwoStep;
		_SkillThreeStep = SkillThreeStep;
		_SkillFourStep  = SkillFourStep;
		_SkillFiveStep  = SkillFiveStep;
	}

	public int getBaseNpcId() {
		return _baseNpcId;
	}

	public L1Npc getBaseNpcTemplate() {
		return _baseNpcTemplate;
	}

	public String getName() {
		return _name;
	}
	
	public int getPetInfo() {
		return _petinfo;
	}
	
	public int getPetEffect() {
		return _PetEffect;
	}

	public IntRange getHpUpRange() {
		return _hpUpRange;
	}
	
	public ArrayList<L1PetSkill> getSkillOneStep() {
		return _SkillOneStep;
	}
	
	public ArrayList<L1PetSkill> getSkillTwoStep() {
		return _SkillTwoStep;
	}
	
	public ArrayList<L1PetSkill> getSkillThreeStep() {
		return _SkillThreeStep;
	}
	
	public ArrayList<L1PetSkill> getSkillFourStep() {
		return _SkillFourStep;
	}
	
	public ArrayList<L1PetSkill> getSkillFiveStep() {
		return _SkillFiveStep;
	}
}
