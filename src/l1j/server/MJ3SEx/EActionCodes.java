package l1j.server.MJ3SEx;

import java.util.HashMap;

import l1j.server.MJ3SEx.IntervalDecorator.ActionIntervalDecorator;
import l1j.server.MJ3SEx.IntervalDecorator.ActionIntervalDecoratorFactory;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;

public enum EActionCodes {	
	walk(0, 16D, 						ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack(1, 20D, 						ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage(2, 14D, 						ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath(3, 32D, 						ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	walk_onehandsword(4, 16D, 			ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_onehandsword(5, 24D, 		ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_onehandsword(6, 14D, 		ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_onehandsword(7, 44D, 		ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	death(8, 48D, 						ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	breath_two(9, 200D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	walk_axe(11, 16D, 					ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_axe(12, 22D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_axe(13, 14D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_axe(14, 44D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	get(15, 10D, 						ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	wand(17, 30D, 						ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	spell_dir(18, 33D, 					ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	spell_nodir(19, 37D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	walk_bow(20, 16D, 					ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_bow(21, 30D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_bow(22, 14D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_bow(23, 40D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	walk_spear(24, 16D, 				ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_spear(25, 21D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_spear(26, 14D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_spear(27, 44D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	open(28, 4D, 						ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	close(29, 4D, 						ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	alt_attack(30, 32D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	spell_dir_extra(31, 24D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	stateone(32, 12D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	statetwo(33, 12D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	statethree(34, 12D, 				ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	statefour(35, 14D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	statefive(36, 14D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	statesix(37, 14D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	walk_staff(40, 16D, 				ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_staff(41, 22D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_staff(42, 14D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_staff(43, 44D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	moveup(44, 40D, 					ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	movedown(45, 43D, 					ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	walk_dagger(46, 16D, 				ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_dagger(47, 21D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_dagger(48, 14D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_dagger(49, 44D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	walk_largesword(50, 16D, 			ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_largesword(51, 28D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_largesword(52, 14D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_largesword(53, 44D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	walk_double_sword(54, 16D, 			ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_double_sword(55, 26D,		ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_double_sword(56, 11D,	 	ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_double_sword(57, 52D,	 	ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	walk_kiringku(58, 16D, 				ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_kiringku(59, 22D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_kiringku(60, 14D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_kiringku(61, 40D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	walk_claw(58, 16D, 					ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_claw(59, 20D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_claw(60, 11D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_claw(61, 52D, 				ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	walk_shuriken(62, 16D, 				ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_shuriken(63, 24D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_shuriken(64, 11D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_shuriken(65, 39D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	shop_loop(66, 44D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	act_dual(67, 38D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	act_wave_hand(68, 46D, 				ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	act_cheer(69, 40D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	act_shop(70, 44D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	fishing(71, 48D, 					ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	alt_att(72, 18D, 					ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	attack_extra_onehandsword(73, 24D, 	ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	alt_attact_axe(74, 24D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	alt_attack_bow(75, 19D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	alt_attact_spear(76, 21D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	attack_extra_staff(77, 22D, 		ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	alt_attack_dagger(78, 20D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	alt_attact_largesword(79, 25D, 		ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	alt_attack_double_sword(80, 16D, 	ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	alt_attack_claw(81, 19D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	alt_attack_shuriken(82, 19, 		ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	walk_chainsword(83, 16D, 			ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attack_chainsword(84, 24D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_chainsword(85, 14D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_chainsword(86, 48D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	attack_extra_chainsword(87, 24D, 	ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	walk_double_axe	(88, 16D, 			ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	attact_double_axe(89, 22D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK),
	damage_double_axe(90, 12D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_DAMAGE),
	breath_double_axe(91, 48D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_BREATH),
	alt_attact_double_axe(92, 22D, 		ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	attact_extra_double_axe(93, 22D, 	ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	attact_extra_axe(94, 24D, 			ActionIntervalDecoratorFactory.CHA_ATT_DECORATOR, ActionIntervalDecoratorFactory.PC_ATT_DECORATOR, EActionConstruct.ACTION_TYPE_ATTACK_ALT),
	pet_action_one(95, 24D,				ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	pet_action_two(96, 24D,				ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	pet_action_three(97, 24D,			ActionIntervalDecoratorFactory.CHA_WALK_DECORATOR, ActionIntervalDecoratorFactory.PC_WALK_DECORATOR, EActionConstruct.ACTION_TYPE_WALK),
	social_four(98, 40D, 				ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC),
	social_five(99, 40D, 				ActionIntervalDecoratorFactory.NULL_DECORATOR, ActionIntervalDecoratorFactory.NULL_DECORATOR, EActionConstruct.ACTION_TYPE_ETC);
	
	public static final HashMap<Integer, EActionCodes> _eActions;
	static{
		EActionCodes[] codes = EActionCodes.values();
		_eActions = new HashMap<Integer, EActionCodes>(codes.length);
		for(EActionCodes code : codes)
			_eActions.put(code.toInt(), code);
	}
	
	public static EActionCodes fromInt(int i){
		return _eActions.get(i);
	}
	
	private int 	actId;
	private double 	defaultAmount;
	private ActionIntervalDecorator<L1Character> chaDecorator;
	private ActionIntervalDecorator<? super L1PcInstance> pcDecorator;
	private int action_type;
	EActionCodes(int actId, double defaultAmount, ActionIntervalDecorator<L1Character> chaDecorator, ActionIntervalDecorator<? super L1PcInstance> pcDecorator, int action_type){
		this.actId = actId;
		this.defaultAmount = defaultAmount;
		this.chaDecorator = chaDecorator;
		this.pcDecorator = pcDecorator;
		this.action_type = action_type;
	}
	
	public int toInt(){
		return actId;
	}
	
	public boolean is_breath(){
		return (action_type & EActionConstruct.ACTION_TYPE_BREATH) == EActionConstruct.ACTION_TYPE_BREATH;
	}
	public boolean is_walk(){
		return (action_type & EActionConstruct.ACTION_TYPE_WALK) == EActionConstruct.ACTION_TYPE_WALK;
	}
	public boolean is_attack(){
		return (action_type & EActionConstruct.ACTION_TYPE_ATTACK) == EActionConstruct.ACTION_TYPE_ATTACK;
	}
	public boolean is_damage(){
		return (action_type & EActionConstruct.ACTION_TYPE_DAMAGE) == EActionConstruct.ACTION_TYPE_DAMAGE;
	}
	
	public double getAmount(){
		return defaultAmount;
	}
	
	public double decoration(L1Character c, double interval){
		return chaDecorator.decoration(c, interval);
	}
	
	public double decorationForPc(L1PcInstance pc, double interval){
		return pcDecorator.decoration(pc, interval);
	}
}
