package l1j.server.MJ3SEx.IntervalDecorator;

import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

public class ActionIntervalDecoratorFactory {
	public static final double HASTE_RETARDATION = 1.149D;
	public static final double BRAVE_RETARDATION = 1.149D;
	public static final double THIRD_RETARDATION = 1.158D;
	public static final double HASTE_ACCELERATION = 1D / HASTE_RETARDATION;
	public static final double BRAVE_ACCELERATION = 1D / BRAVE_RETARDATION;
	public static final double THIRD_ACCELERATION = 1D / THIRD_RETARDATION;
	
	public static final ActionIntervalDecorator<L1PcInstance> PC_WALK_DECORATOR = new ActionIntervalDecorator<L1PcInstance>(){
		@Override
		public double decoration(L1PcInstance owner, double interval) {
			if(owner.isHaste())
				interval *= HASTE_ACCELERATION;
			else if(owner.hasSkillEffect(L1SkillId.SLOW))
				interval *= HASTE_RETARDATION;
			if(owner.isBrave() || owner.isBloodLust() || owner.isHurricane()  || owner.isSandstorm()|| 
					owner.isElfBrave() || owner.isFastMovable() || owner.hasSkillEffect(L1SkillId.STATUS_FRUIT) || owner.hasSkillEffect(L1SkillId.포커스웨이브))
				interval *= BRAVE_ACCELERATION;
			if(owner.isThirdSpeed())
				interval *= THIRD_ACCELERATION;
			
			return interval;
		}
	};
	
	public static final ActionIntervalDecorator<L1PcInstance> PC_ATT_DECORATOR = new ActionIntervalDecorator<L1PcInstance>(){
		@Override
		public double decoration(L1PcInstance owner, double interval) {
			if(owner.isHaste())
				interval *= HASTE_ACCELERATION;
			else if(owner.hasSkillEffect(L1SkillId.SLOW))
				interval *= HASTE_RETARDATION;
			if(owner.hasSkillEffect(L1SkillId.포커스웨이브)){
				for(int i=owner.get포커스웨이브() - 1; i>=0; --i)
					interval *= THIRD_ACCELERATION;
			} else if (owner.hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
				if (owner.isdarkhos()) {
					interval *= THIRD_ACCELERATION;
				}
			}
			else if(owner.isBrave() || owner.isBloodLust() || owner.isHurricane()  || owner.isSandstorm())
				interval *= BRAVE_ACCELERATION;
			else if(owner.isElfBrave())
				interval *= THIRD_ACCELERATION;
			if(owner.isThirdSpeed())
				interval *= THIRD_ACCELERATION;
			if(owner.hasSkillEffect(L1SkillId.WIND_SHACKLE))
				interval *= HASTE_RETARDATION;

			return interval;	
		}
	};
	
	public static final ActionIntervalDecorator<L1Character> CHA_WALK_DECORATOR = new ActionIntervalDecorator<L1Character>(){
		@Override
		public double decoration(L1Character owner, double interval) {
				if(owner.isHaste())
					interval *= HASTE_ACCELERATION;
				else if(owner.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SLOW))
					interval *= HASTE_RETARDATION;
				if(owner.getBraveSpeed() == 1)
					interval *= BRAVE_ACCELERATION;
			return interval;
		}
	};
	
	public static final ActionIntervalDecorator<L1Character> CHA_ATT_DECORATOR = new ActionIntervalDecorator<L1Character>(){
		@Override
		public double decoration(L1Character owner, double interval) {
				if(owner.isHaste())
					interval *= HASTE_ACCELERATION;
				else if(owner.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SLOW))
					interval *= HASTE_RETARDATION;
				if(owner.getBraveSpeed() == 1)
					interval *= BRAVE_ACCELERATION;
				if(owner.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.WIND_SHACKLE))
					interval *= HASTE_RETARDATION;
			return interval;	
		}
	};
	
	public static final ActionIntervalDecorator<L1Character> NULL_DECORATOR = new ActionIntervalDecorator<L1Character>(){
		@Override
		public double decoration(L1Character owner, double interval) {
			return interval;	
		}
	};
}
