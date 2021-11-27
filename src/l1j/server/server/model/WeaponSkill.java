/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.BERSERKERS;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.GREATER_HASTE;
import static l1j.server.server.model.skill.L1SkillId.HASTE;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FREEZE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_SPOT1;
import static l1j.server.server.model.skill.L1SkillId.STATUS_SPOT2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_SPOT3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_SPOT4;
import static l1j.server.server.model.skill.L1SkillId.WIND_SHACKLE;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_RangeSkill;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.templates.L1Skills;

//Referenced classes of package l1j.server.server.model:
//L1PcInstance

public class WeaponSkill {

	// private static final int SLOW = 0;
	private static Random _random = new Random(System.nanoTime());

	public static double getIceSpearDamage(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (20 >= chance) {

			L1Magic magic = new L1Magic(pc, cha);

			dmg = magic.calcMagicDamage(L1SkillId.FROZEN_CLOUD);
			dmg = magic.calcMrDefense((int) dmg);
			if (dmg <= 0) {
				dmg = 0;
			}
			pc.sendPackets(new S_SkillSound(cha.getId(), 1804));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 1804));
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha,
					2)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()
						|| object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (CharPosUtil.getZoneType(targetPc) == 1) {
						continue;
					}
				}
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance
						|| cha instanceof L1SummonInstance
						|| cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance
							|| object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}

				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(
							targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
				} else if (object instanceof L1SummonInstance
						|| object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance) {
					targetNpc = (L1NpcInstance) object;
					Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(
							targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
			magic = null;
		}
		return dmg;
	}

	public static double geTornadoAxeDamage(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (10 >= chance) {

			L1Magic magic = new L1Magic(pc, cha);

			dmg = magic.calcMagicDamage(L1SkillId.TORNADO);
			// dmg = magic.calcMrDefense((int)dmg);
			if (dmg <= 0) {
				dmg = 0;
			}

			pc.sendPackets(new S_SkillSound(cha.getId(), 758));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 758));
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha,
					2)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()
						|| object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (CharPosUtil.getZoneType(targetPc) == 1) {
						continue;
					}
				}
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance
						|| cha instanceof L1SummonInstance
						|| cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance
							|| object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				// dmg = calcDamageReduction((L1Character) object, dmg,
				// L1Skills.ATTR_WIND);
				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(
							targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
				} else if (object instanceof L1SummonInstance
						|| object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance) {
					targetNpc = (L1NpcInstance) object;
					Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(
							targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
			magic = null;
		}
		return dmg;
	}

	public static double getBarlogSwordDamage(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (7 >= chance) {

			L1Magic magic = new L1Magic(pc, cha);

			dmg = magic.calcMagicDamage(L1SkillId.METEOR_STRIKE);
			dmg = magic.calcMrDefense((int) dmg);
			if (dmg <= 0) {
				dmg = 0;
			}
			pc.sendPackets(new S_SkillSound(cha.getId(), 762));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 762));
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha, 2)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()
						|| object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (CharPosUtil.getZoneType(targetPc) == 1) {
						continue;
					}
				}
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance
						|| cha instanceof L1SummonInstance
						|| cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance
							|| object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				// dmg = calcDamageReduction((L1Character) object, dmg,
				// L1Skills.ATTR_FIRE);
				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(
							targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
					if (targetPc.getSkillEffectTimerSet().hasSkillEffect(
							ERASE_MAGIC))
						targetPc.getSkillEffectTimerSet().removeSkillEffect(
								ERASE_MAGIC);

				} else if (object instanceof L1SummonInstance
						|| object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance) {
					targetNpc = (L1NpcInstance) object;
					Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(
							targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
			magic = null;
		}
		return dmg;
	}
	
	public static double getBarlogSwordDamagePc(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (7 >= chance) {

			L1Magic magic = new L1Magic(pc, cha);

			dmg = magic.calcMagicDamage(L1SkillId.METEOR_STRIKE);
			dmg = magic.calcMrDefense((int) dmg);
			if (dmg <= 0) {
				dmg = 0;
			}
			pc.sendPackets(new S_SkillSound(cha.getId(), 762));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 762));
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha,
					2)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()
						|| object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (CharPosUtil.getZoneType(targetPc) == 1) {
						continue;
					}
				}
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance
						|| cha instanceof L1SummonInstance
						|| cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance
							|| object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				// dmg = calcDamageReduction((L1Character) object, dmg,
				// L1Skills.ATTR_FIRE);
				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(
							targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
					if (targetPc.getSkillEffectTimerSet().hasSkillEffect(
							ERASE_MAGIC))
						targetPc.getSkillEffectTimerSet().removeSkillEffect(
								ERASE_MAGIC);

				} else if (object instanceof L1SummonInstance
						|| object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance) {
					targetNpc = (L1NpcInstance) object;
					Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(
							targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
			magic = null;
		}
		return dmg;
	}

	public static void AkdukSword(L1PcInstance pc, L1Character cha, int weaponid) {
		int[] stunTimeArray = { 2000, 2500, 3000, 3500, 4000 };
		int rnd = _random.nextInt(stunTimeArray.length);
		int _shockStunDuration = stunTimeArray[rnd];
		int chance = _random.nextInt(100) + 1;
		if (isFreeze(cha)) {
			return;
		}
		if (Config.검스턴확률 >= chance) {
			L1EffectSpawn.getInstance().spawnEffect(81145, _shockStunDuration,
					cha.getX(), cha.getY(), cha.getMapId());
			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				targetPc.getSkillEffectTimerSet().setSkillEffect(SHOCK_STUN,
						_shockStunDuration);
				targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 87));
				Broadcaster.broadcastPacket(targetPc,
						new S_SkillSound(targetPc.getId(), 87));
				targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN,
						true));
			} else if (cha instanceof L1MonsterInstance
					|| cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.getSkillEffectTimerSet().setSkillEffect(SHOCK_STUN,
						_shockStunDuration);
				Broadcaster.broadcastPacket(npc, new S_SkillSound(npc.getId(),
						87));
				npc.setParalyzed(true);
			}
		}
	}

	public static double getBarlogSwordDamage1(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (7 >= chance) {

			L1Magic magic = new L1Magic(pc, cha);

			dmg = magic.calcMagicDamage(L1SkillId.METEOR_STRIKE);
			dmg = magic.calcMrDefense((int) dmg);
			if (dmg <= 0) {
				dmg = 0;
			}
			pc.sendPackets(new S_SkillSound(cha.getId(), 12248));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 12248));
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha,
					2)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()
						|| object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (CharPosUtil.getZoneType(targetPc) == 1) {
						continue;
					}
				}
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance
						|| cha instanceof L1SummonInstance
						|| cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance
							|| object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				// dmg = calcDamageReduction((L1Character) object, dmg,
				// L1Skills.ATTR_FIRE);
				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(
							targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
					if (targetPc.getSkillEffectTimerSet().hasSkillEffect(
							ERASE_MAGIC))
						targetPc.getSkillEffectTimerSet().removeSkillEffect(
								ERASE_MAGIC);

				} else if (object instanceof L1SummonInstance
						|| object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance) {
					targetNpc = (L1NpcInstance) object;
					Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(
							targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
			magic = null;
		}
		return dmg;
	}

	public static double getBarlogSwordDamage(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;

		if (7 + enchant >= chance) {

			L1Magic magic = new L1Magic(pc, cha);

			dmg = magic.calcMagicDamage(L1SkillId.METEOR_STRIKE);
			dmg = magic.calcMrDefense((int) dmg);
			if (dmg <= 0) {
				dmg = 0;
			}
			// 카매방어
			if (isCounterMagic(cha)) {
				dmg = 0;
			} else {
				pc.sendPackets(new S_SkillSound(cha.getId(), 762));
				Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
						762));
			}
			// 카매방어
			pc.sendPackets(new S_SkillSound(cha.getId(), 762));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 762));
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha,
					2)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()
						|| object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (CharPosUtil.getZoneType(targetPc) == 1) {
						continue;
					}
				}
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance
						|| cha instanceof L1SummonInstance
						|| cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance
							|| object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				// dmg = calcDamageReduction((L1Character) object, dmg,
				// L1Skills.ATTR_FIRE);
				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(
							targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
				} else if (object instanceof L1SummonInstance
						|| object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance) {
					targetNpc = (L1NpcInstance) object;
					Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(
							targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
			magic = null;
		}

		return dmg;
	}

	public static void getDeathKnightjin(L1PcInstance pc, L1Character cha) {
		int chance = _random.nextInt(100) + 1;
		int val = 20;
		if (val >= chance) {
			/*
			 * L1SkillUse l1skilluse = new L1SkillUse(); 123
			 * l1skilluse.handleCommands(pc, 4064, cha.getId(), cha.getX(),
			 * cha.getY(),null, 0, L1SkillUse.TYPE_NORMAL); l1skilluse = null;
			 * 
			 * 123
			 */

			ArrayList<L1Character> arrcha = new ArrayList<L1Character>();

			int rand = _random.nextInt(70) + 30;
			for (L1Object chars : L1World.getInstance()
					.getVisibleObjects(pc, 6)) {
				if (chars instanceof L1MonsterInstance) {
					arrcha.add((L1Character) chars);
					((L1NpcInstance) chars).receiveDamage(pc, rand);
				}
			}

			L1Character[] chars = new L1Character[arrcha.size()];

			int i = 0;

			for (L1Character ts : arrcha) {
				chars[i] = ts;
				i++;
			}

			pc.sendPackets(new S_RangeSkill(pc, cha, chars, 11660, 1, 0));
			Broadcaster.broadcastPacket(pc, new S_RangeSkill(pc, cha, chars, 11660,
					1, 0));

		}
	}

	public static double 블레이즈쇼크(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;

		if (val >= chance) {
			int randmg = _random.nextInt(50) + 20;
			dmg = randmg;

			dmg -= dmg * calcAttrResistance(cha, 2);

			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			if (dmg < 20)
				dmg = 20;

			pc.sendPackets(new S_SkillSound(cha.getId(), 3939));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 3939));
		}
		return dmg;
	}


	public static void getSilenceSword(L1PcInstance pc, L1Character cha,
			int enchant) {
		int chance = _random.nextInt(100) + 1;
		int val = 1;
		if (cha instanceof L1MonsterInstance) {
			if (cha.getLevel() > 69) {
				return;
			}
		}
		if (cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EARTH_BIND))
			return;
		if (val <= 0) {
			val = 1;
		}
		if (enchant > 8) {
			val += 1;
		}
		if (enchant > 9) {
			val += 1;
		}
		if (enchant > 10) {
			val += 0;
		}
		if (enchant > 11) {
			val += 0;	
		}	
		if (enchant > 12) {
			val += 1;
		}
		if (enchant > 13) {
			val += 1;
		}   
			
			if (val >= chance) {
			if (!cha.getSkillEffectTimerSet().hasSkillEffect(64)) {
				cha.getSkillEffectTimerSet().setSkillEffect(64, 4 * 1000);
				pc.sendPackets(new S_SkillSound(cha.getId(), 2177));
				Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
						2177));
			}
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

		}
	}
	public static int getTyphoonDmg(L1PcInstance pc, L1Character cha) {
	    int dmg = 0;
	    if (_random.nextInt(100) <= 10)
	    {
	      dmg = 100;
	      pc.sendPackets(new S_SkillSound(cha.getId(), 7977));
	      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 7977));

	      if ((cha instanceof L1PcInstance))
	      {
	        L1PcInstance _pc = (L1PcInstance)cha;

	        if (_pc != null)
	        {
	          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
	          {
	            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
	            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
	            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

	            dmg = 0;
	            return dmg;
	          }
	        }
	      }

	      cha.getSkillEffectTimerSet().removeSkillEffect(153);
	    }
	    return dmg;
	  }

	  public static int getNightmareDmg(L1PcInstance pc, L1Character cha) {
	    int dmg = 0;
	    if (_random.nextInt(100) <= 10)
	    {
	      dmg = 25;
	      pc.sendPackets(new S_SkillSound(cha.getId(), 14339));
	      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 14339));

	      if ((cha instanceof L1PcInstance))
	      {
	        L1PcInstance _pc = (L1PcInstance)cha;

	        if (_pc != null)
	        {
	          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
	          {
	            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
	            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
	            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

	            dmg = 0;
	            return dmg;
	          }
	        }
	      }

	      cha.getSkillEffectTimerSet().removeSkillEffect(153);
	    }
	    return dmg;
	  }
	  
	  public static int getBalaHon1Damage(L1PcInstance pc, L1Character cha) { //발라카스의 장검 (발라카스의혼) 이펙트
		    int dmg = 0;
		    if (_random.nextInt(100) <= 5)
		    {
		      dmg = 30;
		      pc.sendPackets(new S_SkillSound(cha.getId(), 18982));
		      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 18982));

		      if ((cha instanceof L1PcInstance))
		      {
		        L1PcInstance _pc = (L1PcInstance)cha;

		        if (_pc != null)
		        {
		          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
		          {
		            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
		            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
		            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

		            dmg = 0;
		            return dmg;
		          }
		        
		        }
		      }

		      cha.getSkillEffectTimerSet().removeSkillEffect(153);
		    }
		    return dmg;
	  }
	  public static int getBalaHon2Damage(L1PcInstance pc, L1Character cha) { //발라카스의 장검 (발라카스의혼) 이펙트
		    int dmg = 0;
		    if (_random.nextInt(100) <= 5)
		    {
		      dmg = 30;
		      pc.sendPackets(new S_SkillSound(cha.getId(), 18986));
		      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 18986));

		      if ((cha instanceof L1PcInstance))
		      {
		        L1PcInstance _pc = (L1PcInstance)cha;

		        if (_pc != null)
		        {
		          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
		          {
		            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
		            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
		            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

		            dmg = 0;
		            return dmg;
		          }
		        
		        }
		      }

		      cha.getSkillEffectTimerSet().removeSkillEffect(153);
		    }
		    return dmg;
	  }
	  public static int getPapuHon1Dmg(L1PcInstance pc, L1Character cha) { //파푸리온 장궁 (파푸리의혼) 이펙트
		    int dmg = 0;
		    if (_random.nextInt(100) <= 10)
		    {
		      dmg = 25;
		      pc.sendPackets(new S_SkillSound(cha.getId(), 18998));
		      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 18998));

		      if ((cha instanceof L1PcInstance))
		      {
		        L1PcInstance _pc = (L1PcInstance)cha;

		        if (_pc != null)
		        {
		          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
		          {
		            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
		            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
		            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

		            dmg = 0;
		            return dmg;
		          }
		        }
		      }

		      cha.getSkillEffectTimerSet().removeSkillEffect(153);
		    }
		    return dmg;
		  }
	  public static int getPapuHon2Damage(L1PcInstance pc, L1Character cha) { //파푸리온의 이도류 (파푸리온의 혼) 이펙트
		    int dmg = 0;
		    if (_random.nextInt(100) <= 5)
		    {
		      dmg = 30;
		      pc.sendPackets(new S_SkillSound(cha.getId(), 19002));
		      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 19002));

		      if ((cha instanceof L1PcInstance))
		      {
		        L1PcInstance _pc = (L1PcInstance)cha;

		        if (_pc != null)
		        {
		          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
		          {
		            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
		            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
		            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

		            dmg = 0;
		            return dmg;
		          }
		        
		        }
		      }

		      cha.getSkillEffectTimerSet().removeSkillEffect(153);
		    }
		    return dmg;
	  }
	  public static int getAntaHon1Damage(L1PcInstance pc, L1Character cha) { //안타라스의 도끼 (안타라스의 혼) 이펙트
		    int dmg = 0;
		    if (_random.nextInt(100) <= 5)
		    {
		      dmg = 30;
		      pc.sendPackets(new S_SkillSound(cha.getId(), 19006));
		      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 19006));

		      if ((cha instanceof L1PcInstance))
		      {
		        L1PcInstance _pc = (L1PcInstance)cha;

		        if (_pc != null)
		        {
		          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
		          {
		            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
		            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
		            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

		            dmg = 0;
		            return dmg;
		          }
		        
		        }
		      }

		      cha.getSkillEffectTimerSet().removeSkillEffect(153);
		    }
		    return dmg;
	  }
		public static double getAntaHon2DamageNpc(L1PcInstance pc, L1Character cha, //안타라스의 지팡이 // 제로스 소스 따옴
				int enchant) {
			double dmg = 0;
			double plusdmg = 0;
			int chance = _random.nextInt(100) + 1;
			int val = enchant * 3;
			if (val <= 0) {
				val = 1;
			} else
				val += 1;

			if (val >= chance) {
				int pcInt = pc.getAbility().getTotalInt();
				int sp = pc.getAbility().getSp();
				int targetMr = cha.getResistance().getEffectedMrBySkill();
				int randmg = _random.nextInt((pcInt * 5)) + enchant;
				// System.out.println("테베이팩터짐!");
				dmg = sp * 6 + randmg;
				/*
				 * int spdmg = sp*6; pc.sendPackets(new
				 * S_SystemMessage("랜덤 :"+randmg+" 고정 :"+spdmg+" 총합 :"+dmg));
				 */
				/*
				 * int ran = Math.abs(targetMr-60);
				 * 
				 * if(ran == 0){ if(dmg < 30) dmg = 30; //원래 1811 pc.sendPackets(new
				 * S_SkillSound(cha.getId(), 11760));
				 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
				 * 11760));
				 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
				 * 
				 * return dmg; }else if (targetMr < 60){ plusdmg =
				 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
				 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
				 * }else{ plusdmg = _random.nextInt(ran) / 2; dmg -= plusdmg;
				 * //pc.sendPackets(new
				 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
				 */
				dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
						(int) dmg);

				dmg -= dmg * calcAttrResistance(cha, 2);

				if (dmg < 15)
					dmg = 15;
				// 카매방어
				// if (isCounterMagic(cha)) {
				// dmg = 0;
				// }else{
				pc.sendPackets(new S_SkillSound(cha.getId(), 19010));
				Broadcaster.broadcastPacket(pc,
						new S_SkillSound(cha.getId(), 19010));
				if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
					cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

				// }
				// 카매방어
				// pc.sendPackets(new S_SkillSound(cha.getId(), 1811));
				// Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
				// 1811));
				// magic = null;
			}
			return dmg;
		}
		
		public static double getAntaHon2DamagePc(L1PcInstance pc, L1Character cha, //안타혼 
				int enchant) {
			double dmg = 5;
			double plusdmg = 0;
			int chance = _random.nextInt(100) + 5;
			int val = enchant * 1;
			if (val <= 0) {
				val = 1;
			} else
				val += 1;

			if (val >= chance) {
				int pcInt = pc.getAbility().getTotalInt();
				int sp = pc.getAbility().getSp();
				int targetMr = cha.getResistance().getEffectedMrBySkill();
				int randmg = _random.nextInt((pcInt * 5)) + enchant;
				// System.out.println("테베이팩터짐!");
				dmg = sp * 6 + randmg;
				/*
				 * int spdmg = sp*6; pc.sendPackets(new
				 * S_SystemMessage("랜덤 :"+randmg+" 고정 :"+spdmg+" 총합 :"+dmg));
				 */
				/*
				 * int ran = Math.abs(targetMr-60);
				 * 
				 * if(ran == 0){ if(dmg < 30) dmg = 30; //원래 1811 pc.sendPackets(new
				 * S_SkillSound(cha.getId(), 11760));
				 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
				 * 11760));
				 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
				 * 
				 * return dmg; }else if (targetMr < 60){ plusdmg =
				 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
				 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
				 * }else{ plusdmg = _random.nextInt(ran) / 2; dmg -= plusdmg;
				 * //pc.sendPackets(new
				 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
				 */
				dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
						(int) dmg);

				dmg -= dmg * calcAttrResistance(cha, 2);

				if (dmg < 30)
					dmg = 30;
				// 카매방어
				// if (isCounterMagic(cha)) {
				// dmg = 0;
				// }else{
				pc.sendPackets(new S_SkillSound(cha.getId(), 19010));
				Broadcaster.broadcastPacket(pc,
						new S_SkillSound(cha.getId(), 11760));
				if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
					cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

				// }
				// 카매방어
				// pc.sendPackets(new S_SkillSound(cha.getId(), 1811));
				// Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
				// 1811));
				// magic = null;
			}
			return dmg;
		}
	  public static int getLindHon1Damage(L1PcInstance pc, L1Character cha) { //린드비오르의 체인소드 (린드비오르의 혼) 이펙트
		    int dmg = 0;
		    if (_random.nextInt(100) <= 5)
		    {
		      dmg = 30;
		      pc.sendPackets(new S_SkillSound(cha.getId(), 18990));
		      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 18990));

		      if ((cha instanceof L1PcInstance))
		      {
		        L1PcInstance _pc = (L1PcInstance)cha;

		        if (_pc != null)
		        {
		          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
		          {
		            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
		            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
		            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

		            dmg = 0;
		            return dmg;
		          }
		        
		        }
		      }

		      cha.getSkillEffectTimerSet().removeSkillEffect(153);
		    }
		    return dmg;
	  }

	  public static int getSSaulHonDamage(L1PcInstance pc, L1Character cha) {
	    int dmg = 0;
	    if (_random.nextInt(100) <= 5)
	    {
	      dmg = 30;
	      pc.sendPackets(new S_SkillSound(cha.getId(), 8032));
	      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 8032));

	      if ((cha instanceof L1PcInstance))
	      {
	        L1PcInstance _pc = (L1PcInstance)cha;

	        if (_pc != null)
	        {
	          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
	          {
	            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
	            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
	            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

	            dmg = 0;
	            return dmg;
	          }
	        
	        }
	      }

	      cha.getSkillEffectTimerSet().removeSkillEffect(153);
	    }
	    return dmg;
	  }

	  public static int getAnnihilationDmg(L1PcInstance pc, L1Character cha)
	  {
	    int dmg = 0;
	    if (_random.nextInt(100) <= 10)
	    {
	      dmg = 50;
	      pc.sendPackets(new S_SkillSound(cha.getId(), 13987));
	      Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 13987));

	      if ((cha instanceof L1PcInstance))
	      {
	        L1PcInstance _pc = (L1PcInstance)cha;

	        if (_pc != null)
	        {
	          if (_pc.getSkillEffectTimerSet().hasSkillEffect(31))
	          {
	            _pc.getSkillEffectTimerSet().removeSkillEffect(31);
	            Broadcaster.broadcastPacket(_pc, new S_SkillSound(_pc.getId(), 10702));
	            _pc.sendPackets(new S_SkillSound(_pc.getId(), 10702));

	            dmg = 0;
	            return dmg;
	          }
	        }
	      }

	      cha.getSkillEffectTimerSet().removeSkillEffect(153);
	    }
	    return dmg;
	  }

	public static void getPoisonSword(L1PcInstance pc, L1Character cha) {
		int chance = _random.nextInt(100) + 1;
		if (10 >= chance) {
			L1DamagePoison.doInfection(pc, cha, 3000, 10);
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
		}
	}

	public static void 이블트릭(L1PcInstance pc, L1Character target, int enchanlvl) {
		int probability = (enchanlvl * 2) + 9;
		if (probability >= _random.nextInt(100) + 1) {
			// System.out.println("트릭이팩 터짐!!");
			L1이블트릭 트릭 = new L1이블트릭(pc, target);
			트릭.begin();
			if (target.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				target.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
		}
	}

	public static void 이블트릭(L1PcInstance pc, L1Character target, int enchanlvl,
			int gfx) {
		int probability = (enchanlvl * 1);
		if (probability >= _random.nextInt(100) + 1) {
			// System.out.println("트릭이팩 터짐!!");
			L1이블트릭 트릭 = new L1이블트릭(pc, target, gfx);
			트릭.begin();
		}
	}

	public static void 이블리버스(L1PcInstance pc, L1Character target, int enchanlvl) {
		int probability = (enchanlvl * 2) + 8;

		/*
		 * if(pc.getWeapon().getItemId() == 450008){ //마족검{ probability =
		 * enchanlvl; if(pc.getWeapon().getEnchantLevel() >= 8) probability +=
		 * 1; if(pc.getWeapon().getEnchantLevel() >= 9) probability += 2; }
		 */
		if (pc.getWeapon() != null
				&& (pc.getWeapon().getItemId() >= 277 && pc.getWeapon()
						.getItemId() <= 283))
			probability *= 0.5;

		if (probability >= _random.nextInt(100) + 1) {
			int pcInt = pc.getAbility().getTotalInt();
			int randmg = _random.nextInt(pcInt + enchanlvl) + enchanlvl;

			randmg = calcMrDefense(target.getResistance()
					.getEffectedMrBySkill(), (int) randmg);

			// System.out.println("리버스이팩터짐!");
			L1이블리버스 리버스 = new L1이블리버스(pc, target, randmg);
			리버스.begin();
			if (target.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				target.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
		}
	}

	public static void 이블리버스(L1PcInstance pc, L1Character target,
			int enchanlvl, int gfx) {
		int probability = (enchanlvl * 2) + 8;
		if (probability >= _random.nextInt(100) + 1) {
			int pcInt = pc.getAbility().getTotalInt();
			int randmg = _random.nextInt(pcInt + enchanlvl) + enchanlvl;
			// System.out.println("리버스이팩터짐!");
			L1이블리버스 리버스 = new L1이블리버스(pc, target, randmg, gfx);
			리버스.begin();
		}
	}

	public static void 체이서(L1PcInstance pc, L1Character target, int enchanlvl, int cc) {
		int dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (chance <= 1 + enchanlvl) {
			int pcInt = pc.getAbility().getTotalInt();
			int targetMr = target.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt)) + enchanlvl;
			// System.out.println("테베이팩터짐!");
			dmg = pcInt + randmg;
			dmg = calcMrDefense(targetMr, (int) dmg);

			if (dmg < 30)
				dmg = 30;
			dmg /= 3;

			L1Chaser 체이소 = new L1Chaser(pc, target, dmg, cc);
			체이소.begin();
			if (target.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				target.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
		}
	}

	public static void getDiseaseWeapon(L1PcInstance pc, L1Character cha,
			int weaponid) {
		int chance = _random.nextInt(100) + 1;
		int skilltime = weaponid == 412003 ? 64 : 20;
		if (7 >= chance) {
			if (!cha.getSkillEffectTimerSet().hasSkillEffect(56)) {
				cha.addDmgup(-6);
				cha.getAC().addAc(12);
				if (cha instanceof L1PcInstance) {
					L1PcInstance target = (L1PcInstance) cha;
					target.sendPackets(new S_OwnCharAttrDef(target));
				}
			}
			cha.getSkillEffectTimerSet().setSkillEffect(56, skilltime * 1000);
			pc.sendPackets(new S_SkillSound(cha.getId(), 2230));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 2230));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
		}
	}

	/*
	 * public static double getRondeDamage(L1PcInstance pc, L1Character cha) {
	 * double dmg = 0; int chance = _random.nextInt(100) + 1; if (15 >= chance)
	 * {
	 * 
	 * L1Magic magic = new L1Magic(pc,cha);
	 * 
	 * dmg = magic.calcMagicDamage(L1SkillId.ERUPTION); //dmg =
	 * magic.calcMrDefense((int)dmg); if(dmg <=0){ dmg = 0; }
	 * 
	 * pc.sendPackets(new S_SkillSound(cha.getId(), 11750));
	 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 11750));
	 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
	 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
	 * 
	 * magic = null; } return dmg; }
	 */
	public static double getRondeDamage(L1PcInstance pc, L1Character cha,  //론드이도류 
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int locx = cha.getX();
			int locy = cha.getY();
			/*
			 * int pcInt = pc.getAbility().getTotalInt(); int sp =
			 * pc.getAbility().getSp(); int targetMr =
			 * cha.getResistance().getEffectedMrBySkill(); int randmg =
			 * _random.nextInt((pcInt*3))+ enchant; dmg = sp*3 + randmg;
			 */
			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; S_EffectLocation packet =
			 * new S_EffectLocation(locx, locy, (short) 129);
			 * pc.sendPackets(packet); Broadcaster.broadcastPacket(pc, packet);
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2; dmg -= plusdmg; }
			 */
			/*
			 * dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
			 * (int)dmg);
			 * 
			 * 
			 * dmg -= dmg * calcAttrResistance(cha, 8);
			 */

			int randmg = _random.nextInt(20) + 10;

			dmg = randmg;

			if (dmg < 20)
				dmg = 20;
			S_EffectLocation packet = new S_EffectLocation(locx, locy,
					(short) 10145);
			pc.sendPackets(packet);
			Broadcaster.broadcastPacket(pc, packet);
		}
		return dmg;
	}

	public static void getGunjuSword(L1PcInstance pc, L1Character cha,
			int enchant) {
		// double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (18 + enchant >= chance) {
			// dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 2568));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 2568));// 보너스로
																				// 슬로우
																				// 10초
																				// 추가
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

		}

	}
	
	public static double getgrankainroar(L1PcInstance pc, L1Character cha, //커츠의검
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			dmg = sp * 6 + randmg;
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);
			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 18437));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 18437));
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha, 2)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()
						|| object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (CharPosUtil.getZoneType(targetPc) == 1) {
						continue;
					}
				}
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance
						|| cha instanceof L1SummonInstance
						|| cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance
							|| object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(
							targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
					if (targetPc.getSkillEffectTimerSet().hasSkillEffect(
							ERASE_MAGIC))
						targetPc.getSkillEffectTimerSet().removeSkillEffect(
								ERASE_MAGIC);
				} else if (object instanceof L1SummonInstance
						|| object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance) {
					targetNpc = (L1NpcInstance) object;
					Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(
							targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
		}
		return dmg;
	}
	
	public static double ainhasadflash(L1PcInstance pc, L1Character cha, //커츠의검
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant;
		int fettersTime = 2000;
		if (isFreeze(cha)) {
			return dmg;
		}
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			dmg = sp * 6 + randmg;

			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 10405));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 10405)); return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2; dmg -= plusdmg; }
			 */

			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;

			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				targetPc.getSkillEffectTimerSet().setSkillEffect(STATUS_FREEZE,fettersTime);
				pc.sendPackets(new S_SkillSound(targetPc.getId(), 18539));
				Broadcaster.broadcastPacket(pc,new S_SkillSound(cha.getId(), 18539));
				targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND,true));
			}
		}
		return dmg;
	}
	
	public static double ainhasadblow(L1PcInstance pc, L1Character cha, //커츠의검
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			dmg = sp * 6 + randmg;
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);
			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 18457));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 18457));
			L1PcInstance targetPc = null;
			L1NpcInstance targetNpc = null;
			for (L1Object object : L1World.getInstance().getVisibleObjects(cha, 2)) {
				if (object == null) {
					continue;
				}
				if (!(object instanceof L1Character)) {
					continue;
				}
				if (object.getId() == pc.getId()
						|| object.getId() == cha.getId()) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					if (CharPosUtil.getZoneType(targetPc) == 1) {
						continue;
					}
				}
				if (cha instanceof L1MonsterInstance) {
					if (!(object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (cha instanceof L1PcInstance
						|| cha instanceof L1SummonInstance
						|| cha instanceof L1PetInstance) {
					if (!(object instanceof L1PcInstance
							|| object instanceof L1SummonInstance
							|| object instanceof L1PetInstance || object instanceof L1MonsterInstance)) {
						continue;
					}
				}
				if (dmg <= 0) {
					continue;
				}
				if (object instanceof L1PcInstance) {
					targetPc = (L1PcInstance) object;
					targetPc.sendPackets(new S_DoActionGFX(targetPc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(targetPc, new S_DoActionGFX(
							targetPc.getId(), ActionCodes.ACTION_Damage));
					targetPc.receiveDamage(pc, (int) dmg, false);
					if (targetPc.getSkillEffectTimerSet().hasSkillEffect(
							ERASE_MAGIC))
						targetPc.getSkillEffectTimerSet().removeSkillEffect(
								ERASE_MAGIC);

				} else if (object instanceof L1SummonInstance
						|| object instanceof L1PetInstance
						|| object instanceof L1MonsterInstance) {
					targetNpc = (L1NpcInstance) object;
					Broadcaster.broadcastPacket(targetNpc, new S_DoActionGFX(
							targetNpc.getId(), ActionCodes.ACTION_Damage));
					targetNpc.receiveDamage(pc, (int) dmg);
				}
			}
		}
		return dmg;
	}
	
	public static double getgrankainjudge(L1PcInstance pc, L1Character cha, //커츠의검
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int fettersTime = 2000;
		if (isFreeze(cha)) {
			return dmg;
		}
		int chance = _random.nextInt(100) + 1;
		int val = enchant;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			dmg = sp * 6 + randmg;

			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 10405));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 10405)); return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2; dmg -= plusdmg; }
			 */

			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			
			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				targetPc.getSkillEffectTimerSet().setSkillEffect(STATUS_FREEZE,fettersTime);
				pc.sendPackets(new S_SkillSound(targetPc.getId(), 18547));
				Broadcaster.broadcastPacket(pc,new S_SkillSound(cha.getId(), 18547));
				targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND,true));
			}
		}
		return dmg;
	}
	
	public static double getRepperSwordDamage(L1PcInstance pc, L1Character cha, //커츠의검
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 2;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			dmg = sp * 6 + randmg;

			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 10405));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 10405)); return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2; dmg -= plusdmg; }
			 */

			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 18535));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 18535));
		}
		return dmg;
	}

	public static double getKurtSwordDamage(L1PcInstance pc, L1Character cha, //커츠의검
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 2;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			dmg = sp * 6 + randmg;

			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 10405));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 10405)); return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2; dmg -= plusdmg; }
			 */

			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 10405));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 10405));
		}
		return dmg;
	}
	
	
	public static double getbotKurtSwordDamage(L1PcInstance pc, L1Character cha, //커츠의검
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = 14;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((100)) + 50;
			dmg = randmg;

			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 10405));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 10405)); return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2; dmg -= plusdmg; }
			 */

			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 10405));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 10405));
		}
		return dmg;
	}

	// 7.10 조우불골렘 관련 리뉴얼
	public static double getEffectDamage(L1PcInstance pc, L1Character cha,
			int enchant, int effect) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		/*
		 * if(enchant > 7){ val += 1; }
		 * 
		 * if(enchant > 8){ val += 2; } if(enchant < 9){ val += 3; }
		 */
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 2)) + enchant;
			if (cha instanceof L1PcInstance)
				dmg = pcInt * 2 + randmg;
			else
				dmg = pcInt * 2 + randmg;
			if (effect == 11760) {// 크리선버 (제로스지팡이)
				dmg = pcInt * 3 + randmg;
			}
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), effect));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * effect));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2*1.5; dmg -= plusdmg; }
			 */

			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), effect));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
					effect));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

		}
		return dmg;
	}

	public static double getEffectSwordDamage(L1PcInstance pc, L1Character cha,
			int effectid) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (10 >= chance) {
			L1Magic magic = new L1Magic(pc, cha);

			dmg = magic.calcMagicDamage(L1SkillId.CALL_LIGHTNING);
			dmg = magic.calcMrDefense((int) dmg);

			if (dmg <= 0) {
				dmg = 0;
			}
			pc.sendPackets(new S_SkillSound(cha.getId(), effectid));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
					effectid));
			magic = null;
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
		}
		return dmg;
	}
	public static int getLindHon1Damage(L1PcInstance pc, L1Character cha, int enchant) { //린드 비오르의 키링크(린드비오르의 혼) 
		int chance = _random.nextInt(100) + 1;
		int dmg = 0;
		int val = enchant + 3;
		if (val >= chance) {
			double plusdmg = 3;

			int pcInt = pc.getAbility().getTotalInt();
			int randmg = (pc.getAbility().getSp() * 2) + pcInt;

			dmg = randmg;
			
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 18994));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 18994));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
		}
		return dmg;
		// return 0;
	}


               //공명 키링크
	public static int PhantomShock(L1PcInstance pc, L1Character cha, int enchant) {
		int chance = _random.nextInt(100) + 1;
		int dmg = 0;
		int val = enchant + 1;
		if (val >= chance) {
			double plusdmg = 0;

			int pcInt = pc.getAbility().getTotalInt();
			int randmg = (pc.getAbility().getSp() * 2) + pcInt;

			dmg = randmg;

			/*
			 * int pcInt = pc.getAbility().getTotalInt(); int targetMr =
			 * cha.getResistance().getEffectedMrBySkill(); int randmg =
			 * _random.nextInt((pcInt*1))+ enchant; if(cha instanceof
			 * L1PcInstance) dmg = pcInt*1 + randmg; else dmg = pcInt*2 +
			 * randmg;
			 */

			// int ran = Math.abs(targetMr-60);

			/*
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 5201)); Broadcaster.broadcastPacket(pc,
			 * new S_SkillSound(cha.getId(), 5201));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2*1.5; dmg -= plusdmg; }
			 */

			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			dmg -= dmg * calcAttrResistance(cha, 8);
			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 5201));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 5201));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
		}
		return dmg;
		// return 0;
	}

	public static double Icekiring(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 2;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}

		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int randmg = pc.getAbility().getSp() * 3;
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;

			pc.sendPackets(new S_SkillSound(cha.getId(), 6553));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 6553));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}

			if (cha.getCurrentMp() >= 5) {
				cha.setCurrentMp(cha.getCurrentMp() - 5);
			}

		}
		return dmg;
	}
	
	public static double botIcekiringpc(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = 18;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}

		if (val >= chance) {
			int randmg = _random.nextInt((150)) + 100;
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;

			pc.sendPackets(new S_SkillSound(cha.getId(), 6553));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 6553));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}

			if (cha.getCurrentMp() >= 5) {
				cha.setCurrentMp(cha.getCurrentMp() - 5);
			}

		}
		return dmg;
	}
	
	public static double botIcekiringNpc(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = 27;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}

		if (val >= chance) {
			int randmg = _random.nextInt((150)) + 100;
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;

			pc.sendPackets(new S_SkillSound(cha.getId(), 6553));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 6553));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}

			if (cha.getCurrentMp() >= 5) {
				cha.setCurrentMp(cha.getCurrentMp() - 5);
			}

		}
		return dmg;
	}
	
	
	public static double salkiPc(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 2;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}

		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int randmg = pc.getAbility().getSp() * 3 + pcInt;
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;

			pc.sendPackets(new S_SkillSound(cha.getId(), 7004));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 7004));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}

		}
		return dmg;
	}
	
	
	public static double boksalkiPc(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = 18;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}

		if (val >= chance) {
			int randmg = _random.nextInt((150)) + 150;
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;

			pc.sendPackets(new S_SkillSound(cha.getId(), 7004));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 7004));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}

		}
		return dmg;
	}
	
	public static double boksalkiNPc(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = 27;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}

		if (val >= chance) {
			int randmg = _random.nextInt((100)) + 300;
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;

			pc.sendPackets(new S_SkillSound(cha.getId(), 7004));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 7004));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}

		}
		return dmg;
	}
	
	
	public static double 히페리온의절망(L1PcInstance pc, L1Character cha, int effect,
			int enchant) {
		double dmg = 0.0D;
		int locx = cha.getX();
		int locy = cha.getY();
		int pcInt = pc.getAbility().getTotalInt();
		int randmg = pc.getAbility().getSp() * 4 + pcInt;
		int chance = _random.nextInt(100) + 1;
		if (20 + enchant >= chance) {
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;
			S_EffectLocation packet = new S_EffectLocation(locx, locy, effect);
			pc.sendPackets(packet);
			pc.broadcastPacket(packet);
		}
		return dmg;
	}
	
	public static double salkiNPc(L1PcInstance pc, L1Character cha, int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 3;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}

		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int randmg = pc.getAbility().getSp() * 3 + pcInt;
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;

			pc.sendPackets(new S_SkillSound(cha.getId(), 7004));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 7004));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}

		}
		return dmg;
	}

	public static double Icekiring11(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 2;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}

		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int randmg = pc.getAbility().getSp() * 3;
			dmg = randmg;
			if (dmg < 15)
				dmg = 15;

			pc.sendPackets(new S_SkillSound(cha.getId(), 12248));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 12248));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}

		}
		return dmg;
	}

	public static double IceChainSword(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 3)) + enchant;
			// System.out.println("테베이팩터짐!");
			dmg = pcInt * 3 + randmg;

			// System.out.println("마방 적용전 데미지 : "+dmg);
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			// System.out.println("마방 적용후 데미지 : "+dmg);

			// int ran = Math.abs(targetMr-60);

			/*
			 * if(ran == 0){ if(dmg < 30) dmg = 30; S_EffectLocation packet =
			 * new S_EffectLocation(cha.getX(), cha.getY(), (short) 3687);
			 * pc.sendPackets(packet); Broadcaster.broadcastPacket(pc, packet);
			 * if
			 * (cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EARTH_BIND
			 * )
			 * ||cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.ICE_LANCE
			 * )){ return dmg; }
			 * pc.setCurrentHp(pc.getCurrentHp()+_random.nextInt
			 * (pc.getAbility().getTotalInt()*4)+5);
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * //System.out.println("극체 데미지 : "+dmg); return dmg; }else if
			 * (targetMr < 60){ plusdmg = _random.nextInt(ran) / 2 ; dmg +=
			 * plusdmg;
			 * 
			 * }else{ plusdmg = _random.nextInt(ran) / 2 * 1.5; dmg -= plusdmg;
			 * }
			 */

			dmg -= dmg * calcAttrResistance(cha, 4);

			if (dmg < 15)
				dmg = 15;
			// 카매방어
			// if (isCounterMagic(cha)) {
			// dmg = 0;
			// }else{

			S_EffectLocation packet = new S_EffectLocation(cha.getX(),
					cha.getY(), (short) 3687);
			pc.sendPackets(packet);
			Broadcaster.broadcastPacket(pc, packet);
			if (cha.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return dmg;
			}
			pc.setCurrentHp(pc.getCurrentHp()
					+ _random.nextInt(pc.getAbility().getTotalInt() * 4) + 5);
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			// }
		}
		// System.out.println("극체 데미지 : "+dmg);
		return dmg;

	}

	public static double getIceQueenStaffDamage(L1PcInstance pc,
			L1Character cha, int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 2 + 2;
		/*
		 * if(val < 0 ){ val = 3; } if(enchant > 7){ val += 2; } if(enchant >
		 * 8){ val += 3; } if(enchant < 9){ val += 2; }
		 */
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt(pcInt * 3)
					+ _random.nextInt(pc.getAbility().getSp() * 2);
			// System.out.println("테베이팩터짐!");
			dmg = pcInt * 3 + randmg;
			dmg = calcMrDefense(targetMr, (int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 1810)); Broadcaster.broadcastPacket(pc,
			 * new S_SkillSound(cha.getId(), 1810));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
			 * }else{ plusdmg = _random.nextInt(ran) / 2*1.5; dmg -= plusdmg;
			 * //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
			 */
			dmg -= dmg * calcAttrResistance(cha, 4);

			if (dmg < 30)
				dmg = 30;
			// 카매방어
			// if (isCounterMagic(cha)) {
			// dmg = 0;
			// }else{
			pc.sendPackets(new S_SkillSound(cha.getId(), 1810));
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(cha.getId(), 1810));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			// }
		}
		return dmg;
	}

	public static double getMoonBowDamage(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant + 1;
		if (val <= 0) {
			val = 1;
		} else {
			val += 1;
		}
		if (val >= chance) {
			/*
			 * int pcInt = pc.getAbility().getTotalInt(); int targetMr =
			 * cha.getResistance().getEffectedMrBySkill(); int randmg =
			 * _random.nextInt((pcInt*2))+ enchant;
			 * //System.out.println("테베이팩터짐!"); dmg = pcInt*2 + randmg;
			 * 
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; S_UseAttackSkill packet =
			 * new S_UseAttackSkill(pc, cha.getId(), 6288, cha.getX(),
			 * cha.getY(), ActionCodes.ACTION_Attack, false);
			 * pc.sendPackets(packet); Broadcaster.broadcastPacket(pc, packet);
			 * 
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
			 * }else{ plusdmg = _random.nextInt(ran) / 2*1.5; dmg -= plusdmg;
			 * //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
			 */
			int randmg = _random.nextInt(20) + 20;

			dmg = randmg;

			if (dmg < 30)
				dmg = 30;
			// 카매방어
			// if (isCounterMagic(cha)) {
			// dmg = 0;
			// }else{
			S_UseAttackSkill packet = new S_UseAttackSkill(pc, cha.getId(),
					6288, cha.getX(), cha.getY(), ActionCodes.ACTION_Attack,
					false);
			pc.sendPackets(packet);
			Broadcaster.broadcastPacket(pc, packet);
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			// }
			// 카매방어
			// pc.sendPackets(new S_SkillSound(cha.getId(), 1811));
			// Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			// 1811));
			// magic = null;
		}
		return dmg;
	}

	// 할로윈
	public static double halloweenCus(L1PcInstance pc, L1Character cha) {
		if (isFreeze(cha)) {
			return 0;
		}
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (8 >= chance) {// 조정
			int sp = pc.getAbility().getSp();
			int intel = pc.getAbility().getTotalInt();
			double bsk = 0;
			if (pc.getSkillEffectTimerSet().hasSkillEffect(BERSERKERS)) {
				bsk = 0.2;
			}
			dmg = (intel + sp) * (2 + bsk) + _random.nextInt(intel + sp) * 0.3;
			cha.getSkillEffectTimerSet().setSkillEffect(29, 64 * 1000);
			pc.sendPackets(new S_SkillSound(cha.getId(), 752));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(), 752));
			pc.sendPackets(new S_SkillHaste(cha.getId(), 2, 64 * 1000));
			Broadcaster.broadcastPacket(pc, new S_SkillHaste(cha.getId(), 2,
					64 * 1000));
			if (cha.getMoveState().getMoveSpeed() == 0) {
				// if (cha instanceof L1PcInstance) {
				// L1PcInstance pc2 = (L1PcInstance) cha;
				// pc2.sendPackets(new S_SkillHaste(pc2.getId(), 2, 64*1000));
				// }
				// cha.sendPackets(new S_SkillHaste(cha.getId(), 2, 64*1000));
				Broadcaster.broadcastPacket(pc, new S_SkillHaste(cha.getId(),
						2, 64 * 1000));
				cha.getMoveState().setMoveSpeed(2);
			} else if (cha.getMoveState().getMoveSpeed() == 1) {
				int skillNum = 0;
				if (cha.getSkillEffectTimerSet().hasSkillEffect(HASTE)) {
					skillNum = HASTE;
				} else if (cha.getSkillEffectTimerSet().hasSkillEffect(
						GREATER_HASTE)) {
					skillNum = GREATER_HASTE;
				} else if (cha.getSkillEffectTimerSet().hasSkillEffect(
						STATUS_HASTE)) {
					skillNum = STATUS_HASTE;
				}
				if (skillNum != 0) {
					cha.getSkillEffectTimerSet().removeSkillEffect(skillNum);
					// cha.getSkillEffectTimerSet().removeSkillEffect(_skillId);
					cha.getMoveState().setMoveSpeed(0);
					// continue;
				}
			}

		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_RAY);
	}

	//
	public static double halloweenCus2(L1PcInstance pc, L1Character cha) {
		double dmg = 0;
		if (pc.getWeapon() == null)
			return 0;
		int chance = 1 + (pc.getWeapon().getEnchantLevel() / 2);
		if (isFreeze(cha)) {
			return 0;
		}
		if (_random.nextInt(100) + 1 <= chance) {// 조정
			int sp = pc.getAbility().getSp();
			int intel = pc.getAbility().getTotalInt();
			double bsk = 0;
			if (pc.getSkillEffectTimerSet().hasSkillEffect(BERSERKERS)) {
				bsk = 0.2;
			}
			dmg = (intel + sp) * (2 + bsk) + _random.nextInt(intel + sp) * 0.3;
			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				if (targetPc.getSkillEffectTimerSet().hasSkillEffect(
						WIND_SHACKLE))
					targetPc.getSkillEffectTimerSet().removeSkillEffect(
							WIND_SHACKLE);
				targetPc.getSkillEffectTimerSet().setSkillEffect(WIND_SHACKLE,
						4 * 1000);
				targetPc.sendPackets(new S_SkillIconWindShackle(targetPc
						.getId(), 4));
				Broadcaster.broadcastPacket(targetPc,
						new S_SkillIconWindShackle(targetPc.getId(), 4));
				targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 7849));
				Broadcaster.broadcastPacket(targetPc,
						new S_SkillSound(targetPc.getId(), 7849));
				if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
					cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			} else if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				Broadcaster.broadcastPacket(npc, new S_SkillSound(npc.getId(),
						7849));
				if (npc.getSkillEffectTimerSet().hasSkillEffect(WIND_SHACKLE))
					npc.getSkillEffectTimerSet()
							.removeSkillEffect(WIND_SHACKLE);
				npc.getSkillEffectTimerSet().setSkillEffect(WIND_SHACKLE,
						4 * 1000);
				/*
				 * npc.set템프(npc.getAtkspeed());
				 * npc.setAtkspeed((int)(npc.getAtkspeed()*1.5));
				 * npc.set펌프(16000);
				 */
			}
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);
			// cha.getSkillEffectTimerSet().setSkillEffect(STATUS_FREEZE, 3000);
			// cha.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
		}
		return calcDamageReduction(cha, dmg, L1Skills.ATTR_RAY);
	}

	//

	public static void giveSalCheonEffect(L1PcInstance pc, L1Character cha) { // 살천의활
																				// (썬더그랩홀드효과:
																				// 진홍빛크로스보우와
																				// 동일)
		int fettersTime = 3000;
		if (isFreeze(cha)) {
			return;
		}
		if ((_random.nextInt(35) + 1) <= 2) {
			L1EffectSpawn.getInstance().spawnEffect(81182, fettersTime,
					cha.getX(), cha.getY(), cha.getMapId());
			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				targetPc.getSkillEffectTimerSet().removeSkillEffect(
						WIND_SHACKLE);
				targetPc.getSkillEffectTimerSet().setSkillEffect(WIND_SHACKLE,
						16 * 1000);
				targetPc.sendPackets(new S_SkillIconWindShackle(targetPc
						.getId(), 16));
				Broadcaster.broadcastPacket(targetPc,
						new S_SkillIconWindShackle(targetPc.getId(), 16));
				targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 7849));
				Broadcaster.broadcastPacket(targetPc,
						new S_SkillSound(targetPc.getId(), 7849));
			}
			if (cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				Broadcaster.broadcastPacket(npc, new S_SkillSound(npc.getId(),
						7849));
				if (npc.getSkillEffectTimerSet().hasSkillEffect(WIND_SHACKLE))
					npc.getSkillEffectTimerSet()
							.removeSkillEffect(WIND_SHACKLE);
				npc.getSkillEffectTimerSet().setSkillEffect(WIND_SHACKLE,
						16 * 1000);
				// npc.set템프(npc.getAtkspeed());
				// npc.setAtkspeed((int)(npc.getAtkspeed()*1.5));
				// npc.set펌프(16000);
			}
		}
	}
	public static void 섬멸자의체인소드(L1PcInstance pc) { // 체인소드 대미지.
		if (_random.nextInt(100) < 18) {
		  if(pc.포우슬레이어브레이브) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_SPOT1)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(
						L1SkillId.STATUS_SPOT1);
				pc.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_SPOT2, 15 * 1000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 2));
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_SPOT2)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(
						L1SkillId.STATUS_SPOT2);
				pc.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_SPOT3, 15 * 1000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 3));
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_SPOT3)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(
						L1SkillId.STATUS_SPOT3);
				pc.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_SPOT4, 15 * 1000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 4));
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_SPOT4)) {
			} else {
				pc.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_SPOT1, 15 * 1000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1));
			}
		  }else if(!pc.포우슬레이어브레이브) {
				if (pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.STATUS_SPOT1)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(
							L1SkillId.STATUS_SPOT1);
					pc.getSkillEffectTimerSet().setSkillEffect(
							L1SkillId.STATUS_SPOT2, 15 * 1000);
					pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 2));
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.STATUS_SPOT2)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(
							L1SkillId.STATUS_SPOT2);
					pc.getSkillEffectTimerSet().setSkillEffect(
							L1SkillId.STATUS_SPOT3, 15 * 1000);
					pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 3));
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.STATUS_SPOT3)) {
				} else {
					pc.getSkillEffectTimerSet().setSkillEffect(
							L1SkillId.STATUS_SPOT1, 15 * 1000);
					pc.sendPackets(new S_PacketBox(S_PacketBox.SPOT, 1));
				}
			  }
		}
	}

	public static double getZerosDamageNpc(L1PcInstance pc, L1Character cha, //제로스 지팡이 NPC
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 3;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;

		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			// System.out.println("테베이팩터짐!");
			dmg = sp * 6 + randmg;
			/*
			 * int spdmg = sp*6; pc.sendPackets(new
			 * S_SystemMessage("랜덤 :"+randmg+" 고정 :"+spdmg+" 총합 :"+dmg));
			 */
			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; //원래 1811 pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 11760));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 11760));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
			 * }else{ plusdmg = _random.nextInt(ran) / 2; dmg -= plusdmg;
			 * //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
			 */
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			dmg -= dmg * calcAttrResistance(cha, 2);

			if (dmg < 15)
				dmg = 15;
			// 카매방어
			// if (isCounterMagic(cha)) {
			// dmg = 0;
			// }else{
			pc.sendPackets(new S_SkillSound(cha.getId(), 11760));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 11760));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			// }
			// 카매방어
			// pc.sendPackets(new S_SkillSound(cha.getId(), 1811));
			// Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			// 1811));
			// magic = null;
		}
		return dmg;
	}
	
	public static double getZerosDamagePc(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;

		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			// System.out.println("테베이팩터짐!");
			dmg = sp * 6 + randmg;
			/*
			 * int spdmg = sp*6; pc.sendPackets(new
			 * S_SystemMessage("랜덤 :"+randmg+" 고정 :"+spdmg+" 총합 :"+dmg));
			 */
			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; //원래 1811 pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 11760));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 11760));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
			 * }else{ plusdmg = _random.nextInt(ran) / 2; dmg -= plusdmg;
			 * //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
			 */
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			dmg -= dmg * calcAttrResistance(cha, 2);

			if (dmg < 15)
				dmg = 15;
			// 카매방어
			// if (isCounterMagic(cha)) {
			// dmg = 0;
			// }else{
			pc.sendPackets(new S_SkillSound(cha.getId(), 11760));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 11760));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			// }
			// 카매방어
			// pc.sendPackets(new S_SkillSound(cha.getId(), 1811));
			// Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			// 1811));
			// magic = null;
		}
		return dmg;
	}
	
	public static double botgetZerosDamagePc(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = 10;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;

		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((200)) + 200;
			// System.out.println("테베이팩터짐!");
			dmg = randmg;
			/*
			 * int spdmg = sp*6; pc.sendPackets(new
			 * S_SystemMessage("랜덤 :"+randmg+" 고정 :"+spdmg+" 총합 :"+dmg));
			 */
			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; //원래 1811 pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 11760));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 11760));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
			 * }else{ plusdmg = _random.nextInt(ran) / 2; dmg -= plusdmg;
			 * //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
			 */
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			dmg -= dmg * calcAttrResistance(cha, 2);

			if (dmg < 15)
				dmg = 15;
			// 카매방어
			// if (isCounterMagic(cha)) {
			// dmg = 0;
			// }else{
			pc.sendPackets(new S_SkillSound(cha.getId(), 11760));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 11760));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			// }
			// 카매방어
			// pc.sendPackets(new S_SkillSound(cha.getId(), 1811));
			// Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			// 1811));
			// magic = null;
		}
		return dmg;
	}
	
	public static double botgetZerosDamageNPc(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = 28;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;

		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((200)) + 200;
			// System.out.println("테베이팩터짐!");
			dmg = randmg;
			/*
			 * int spdmg = sp*6; pc.sendPackets(new
			 * S_SystemMessage("랜덤 :"+randmg+" 고정 :"+spdmg+" 총합 :"+dmg));
			 */
			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; //원래 1811 pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 11760));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 11760));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
			 * }else{ plusdmg = _random.nextInt(ran) / 2; dmg -= plusdmg;
			 * //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
			 */
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			dmg -= dmg * calcAttrResistance(cha, 2);

			if (dmg < 15)
				dmg = 15;
			// 카매방어
			// if (isCounterMagic(cha)) {
			// dmg = 0;
			// }else{
			pc.sendPackets(new S_SkillSound(cha.getId(), 11760));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 11760));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

			// }
			// 카매방어
			// pc.sendPackets(new S_SkillSound(cha.getId(), 1811));
			// Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			// 1811));
			// magic = null;
		}
		return dmg;
	}

	public static double getDeathKnightSwordDamage(L1PcInstance pc,
			L1Character cha, int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant * 1;
		if (val <= 0) {
			val = 1;
		} else
			val += 1;
		/*
		 * int val = enchant * 3; if(val < 0 ){ val = 3; }else val += 1;
		 * if(enchant > 7){ val += 1; }
		 * 
		 * if(enchant > 8){ val += 1; }
		 */

		/*
		 * if(enchant < 9){ val += 3; }
		 */

		if (val >= chance) {

			int pcInt = pc.getAbility().getTotalInt();
			int sp = pc.getAbility().getSp();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 5)) + enchant;
			// System.out.println("테베이팩터짐!");
			dmg = sp * 6 + randmg;
			dmg = calcMrDefense(targetMr, (int) dmg);
			dmg -= dmg * calcAttrResistance(cha, 2);

			if (dmg < 30)
				dmg = 30;
			pc.sendPackets(new S_SkillSound(cha.getId(), 11660));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 11660));
		}
		return dmg;
	}

	public static double getBaphometStaffDamage(L1PcInstance pc,
			L1Character cha, int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant + 7;

		if (val >= chance) {
			int locx = cha.getX();
			int locy = cha.getY();
			int pcInt = pc.getAbility().getTotalInt();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 4))
					+ _random.nextInt(pc.getAbility().getSp() * 2);
			// System.out.println("테베이팩터짐!");
			dmg = pcInt * 4 + randmg;

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; S_EffectLocation packet =
			 * new S_EffectLocation(locx, locy, (short) 129);
			 * pc.sendPackets(packet); Broadcaster.broadcastPacket(pc, packet);
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
			 * }else{ plusdmg = _random.nextInt(ran) / 2*1.5; dmg -= plusdmg;
			 * //pc.sendPackets(new
			 * S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg)); }
			 */

			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			dmg -= dmg * calcAttrResistance(cha, 1);
			if (dmg < 30)
				dmg = 30;
			S_EffectLocation packet = new S_EffectLocation(locx, locy,
					(short) 129);
			pc.sendPackets(packet);
			Broadcaster.broadcastPacket(pc, packet);
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

		}
		return dmg;
	}

	public static double get수결지Damage(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant + 15;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt(pcInt * 5)
					+ _random.nextInt(pc.getAbility().getSp() * 3);
			// System.out.println("테베이팩터짐!");
			dmg = pcInt * 5 + randmg;
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 10405));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 10405));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2*1.5; dmg -= plusdmg; }
			 */

			// dmg -= dmg * calcAttrResistance(cha, 4);

			if (dmg < 40)
				dmg = 40;

			pc.sendPackets(new S_SkillSound(cha.getId(), 10405));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 10405));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

		}
		return dmg;
	}

	public static double get절망Damage(L1PcInstance pc, L1Character cha,
			int enchant) {
		double dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		int val = enchant + 10;
		if (val >= chance) {
			int pcInt = pc.getAbility().getTotalInt();
			int targetMr = cha.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt(pcInt * 5)
					+ _random.nextInt(pc.getAbility().getSp() * 3);
			// System.out.println("테베이팩터짐!");
			dmg = pcInt * 5 + randmg;
			dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(),
					(int) dmg);

			/*
			 * int ran = Math.abs(targetMr-60);
			 * 
			 * if(ran == 0){ if(dmg < 30) dmg = 30; pc.sendPackets(new
			 * S_SkillSound(cha.getId(), 10405));
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(cha.getId(),
			 * 10405));
			 * if(cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
			 * cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			 * 
			 * return dmg; }else if (targetMr < 60){ plusdmg =
			 * _random.nextInt(ran) / 2 ; dmg += plusdmg; }else{ plusdmg =
			 * _random.nextInt(ran) / 2*1.5; dmg -= plusdmg; }
			 */

			// dmg -= dmg * calcAttrResistance(cha, 4);

			if (dmg < 100)
				dmg = 100;

			pc.sendPackets(new S_SkillSound(cha.getId(), 12248));
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(cha.getId(), 12248));
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);

		}
		return dmg;
	}

	public static double getDiceDaggerDamage(L1PcInstance pc,
			L1PcInstance targetPc, L1ItemInstance weapon) {
		double dmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (3 >= chance) {
			dmg = targetPc.getCurrentHp() * 2 / 3;
			if (targetPc.getCurrentHp() - dmg < 0) {
				dmg = 0;
			}
			String msg = weapon.getLogName();
			pc.sendPackets(new S_ServerMessage(158, msg));
			pc.getInventory().removeItem(weapon, 1);
			if (targetPc.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC))
				targetPc.getSkillEffectTimerSet()
						.removeSkillEffect(ERASE_MAGIC);
		}
		return dmg;
	}

	public static void giveFettersEffect(L1PcInstance pc, L1Character cha) {
		int fettersTime = 8000;
		if (isFreeze(cha)) {
			return;
		}
		if ((_random.nextInt(100) + 1) <= 2) { //진홍 , 붉이 홀드
			L1EffectSpawn.getInstance().spawnEffect(81182, fettersTime,
					cha.getX(), cha.getY(), cha.getMapId());
			if (cha instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) cha;
				targetPc.getSkillEffectTimerSet().setSkillEffect(STATUS_FREEZE,
						fettersTime);
				targetPc.sendPackets(new S_SkillSound(targetPc.getId(), 4184));
				Broadcaster.broadcastPacket(targetPc,
						new S_SkillSound(targetPc.getId(), 4184));
				targetPc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND,
						true));
				if (targetPc.getSkillEffectTimerSet().hasSkillEffect(
						ERASE_MAGIC))
					targetPc.getSkillEffectTimerSet().removeSkillEffect(
							ERASE_MAGIC);
			} else if (cha instanceof L1MonsterInstance
					|| cha instanceof L1SummonInstance
					|| cha instanceof L1PetInstance) {
				L1NpcInstance npc = (L1NpcInstance) cha;
				npc.getSkillEffectTimerSet().setSkillEffect(STATUS_FREEZE,
						fettersTime);
				Broadcaster.broadcastPacket(npc, new S_SkillSound(npc.getId(),
						4184));
				npc.setParalyzed(true);
			}
		}
	}

	public static int getKiringkuDamage(L1PcInstance pc, L1Character cha) {
		int dmg = 0;

		/*
		 * int dmg = 0; int dice = 5; int diceCount = 2; int value = 14; int
		 * KiringkuDamage = 0; int charaIntelligence = 0;
		 * 
		 * for (int i = 0; i < diceCount; i++) { KiringkuDamage +=
		 * (_random.nextInt(dice) + 1); }
		 * 
		 * 
		 * KiringkuDamage += value;
		 * 
		 * 
		 * 
		 * int spByItem = pc.getAbility().getSp() - pc.getAbility().getTrueSp();
		 * charaIntelligence = pc.getAbility().getTotalInt() + spByItem -
		 * 10;//-12 if (charaIntelligence < 1) { charaIntelligence = 1; } double
		 * KiringkuCoefficientA = (1.0 + charaIntelligence * 3.0 / 32.0);
		 * 
		 * KiringkuDamage *= KiringkuCoefficientA;
		 * 
		 * double Mrfloor = 0; if (cha.getResistance().getEffectedMrBySkill() <=
		 * 100) { Mrfloor =
		 * Math.floor((cha.getResistance().getEffectedMrBySkill() -
		 * pc.getBaseMagicHitUp()) / 2); } else if
		 * (cha.getResistance().getEffectedMrBySkill() >= 100) { Mrfloor =
		 * Math.floor((cha.getResistance().getEffectedMrBySkill() -
		 * pc.getBaseMagicHitUp()) / 10); }
		 * 
		 * double KiringkuCoefficientB = 0; if
		 * (cha.getResistance().getEffectedMrBySkill() <= 100) {
		 * KiringkuCoefficientB = 0.9 - 0.01 * Mrfloor; } else if
		 * (cha.getResistance().getEffectedMrBySkill() > 100) {
		 * KiringkuCoefficientB = 0.4 - 0.01 * Mrfloor; } double Kiringkufloor =
		 * Math.floor(KiringkuDamage);
		 * 
		 * dmg += Kiringkufloor + (pc.getWeapon().getEnchantLevel()*2);
		 */

		dmg = calcMrDefense(cha.getResistance().getEffectedMrBySkill(), dmg);

		return dmg;
	}

	public static int calcMrDefense(int MagicResistance, int dmg) {
		double cc = 0;
		if (MagicResistance <= 19) {
			cc = 0.05;
		} else if (MagicResistance <= 29) {
			cc = 0.07;
		} else if (MagicResistance <= 39) {
			cc = 0.1;
		} else if (MagicResistance <= 49) {
			cc = 0.12;
		} else if (MagicResistance <= 59) {
			cc = 0.17;
		} else if (MagicResistance <= 69) {
			cc = 0.20;
		} else if (MagicResistance <= 79) {
			cc = 0.22;
		} else if (MagicResistance <= 89) {
			cc = 0.25;
		} else if (MagicResistance <= 99) {
			cc = 0.27;
		} else if (MagicResistance <= 110) {
			cc = 0.31;
		} else if (MagicResistance <= 120) {
			cc = 0.32;
		} else if (MagicResistance <= 130) {
			cc = 0.34;
		} else if (MagicResistance <= 140) {
			cc = 0.36;
		} else if (MagicResistance <= 150) {
			cc = 0.38;
		} else if (MagicResistance <= 160) {
			cc = 0.40;
		} else if (MagicResistance <= 170) {
			cc = 0.42;
		} else if (MagicResistance <= 180) {
			cc = 0.44;
		} else if (MagicResistance <= 190) {
			cc = 0.46;
		} else if (MagicResistance <= 200) {
			cc = 0.48;
		} else if (MagicResistance <= 220) {
			cc = 0.49;
		} else {
			cc = 0.51;
		}

		dmg -= dmg * cc;

		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	public static int getChaserDamage(L1PcInstance pc, L1Character target,
			int effect) {
		int dmg = 0;
		double plusdmg = 0;
		int chance = _random.nextInt(100) + 1;
		if (chance <= 7) {
			int pcInt = pc.getAbility().getTotalInt();
			int targetMr = target.getResistance().getEffectedMrBySkill();
			int randmg = _random.nextInt((pcInt * 2));

			dmg = pcInt * 5 + randmg;

			int ran = Math.abs(targetMr - 60);

			if (ran == 0) {
				return dmg;
			} else if (targetMr < 60) {
				plusdmg = _random.nextInt(ran) / 2;
				dmg += plusdmg;
				// pc.sendPackets(new
				// S_SystemMessage("마방에의한 데미지 증가 = "+plusdmg+" 토탈데미지 :"+dmg));
			} else {
				plusdmg = _random.nextInt(ran) / 2 * 1.5;
				dmg -= plusdmg;
				// pc.sendPackets(new
				// S_SystemMessage("마방에의한 데미지 감소 = "+plusdmg+" 토탈데미지 :"+dmg));
			}
			dmg /= 2;
			if (dmg < 0)
				dmg = 0;

			pc.sendPackets(new S_SkillSound(target.getId(), effect));
			Broadcaster.broadcastPacket(pc, new S_SkillSound(target.getId(),
					effect));
		}
		return dmg;
	}
	
	public static int getChainSwordDamageNpc(L1PcInstance pc, L1Character cha,
			int itemid, int enchanlvl) {
		int dmg = 10;
		int chance = _random.nextInt(100) + 1;
		int chanceweapon = 15;

		if (itemid == 90084 && enchanlvl == 8) {
			chanceweapon = 20;
		}
		
		if (itemid == 90084 && enchanlvl == 9) {
			chanceweapon = 25;
		}
		
		if (itemid == 90084 && enchanlvl == 10) {
			chanceweapon = 30;
		}

		if (pc.ChainSwordObjid != cha.getId()) {
			if(pc.포우슬레이어브레이브) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT4)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT4);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
				pc.sendPackets(pb, true);
			 }
			} else if(!pc.포우슬레이어브레이브) {
				if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
					pc.sendPackets(pb, true);
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
					pc.sendPackets(pb, true);
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
					pc.sendPackets(pb, true);
				 }
				}
			pc.ChainSwordObjid = cha.getId();
		}
		if (chanceweapon >= chance) {
		  if(pc.포우슬레이어브레이브) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT2,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 2);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT3,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 3);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT4,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 4);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT4)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT4);
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT4,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 4);
				pc.sendPackets(pb, true);
			} else {
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT1,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 1);
				pc.sendPackets(pb, true);
			}
		  }else if(!pc.포우슬레이어브레이브) {
				if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
					pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT2,
							16 * 1000);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 2);
					pc.sendPackets(pb, true);
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
					pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT3,
							16 * 1000);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 3);
					pc.sendPackets(pb, true);
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
					pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT3,
							16 * 1000);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 3);
					pc.sendPackets(pb, true);
				} else {
					pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT1,
							16 * 1000);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 1);
					pc.sendPackets(pb, true);
				}
			  }
		}
		return dmg;
	}

	public static int getChainSwordDamage(L1PcInstance pc, L1Character cha,
			int itemid, int enchanlvl) {
		int dmg = 10;
		int chance = _random.nextInt(100) + 1;
		int chanceweapon = 4;

		if (itemid == 90084 && enchanlvl == 8) {
			chanceweapon = 6;
		}
		
		if (itemid == 90084 && enchanlvl == 9) {
			chanceweapon = 7;
		}
		
		if (itemid == 90084 && enchanlvl == 10) {
			chanceweapon = 12;
		}

		if (pc.ChainSwordObjid != cha.getId()) {
			if(pc.포우슬레이어브레이브) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT4)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT4);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
				pc.sendPackets(pb, true);
			 }
			} else if(!pc.포우슬레이어브레이브) {
				if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
					pc.sendPackets(pb, true);
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
					pc.sendPackets(pb, true);
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
					pc.sendPackets(pb, true);
				 }
				}
			pc.ChainSwordObjid = cha.getId();
		}
		if (chanceweapon >= chance) {
		  if(pc.포우슬레이어브레이브) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT2,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 2);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT3,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 3);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT4,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 4);
				pc.sendPackets(pb, true);
			} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT4)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT4);
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT4,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 4);
				pc.sendPackets(pb, true);
			} else {
				pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT1,
						16 * 1000);
				S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 1);
				pc.sendPackets(pb, true);
			}
		  }else if(!pc.포우슬레이어브레이브) {
				if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
					pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT2,
							16 * 1000);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 2);
					pc.sendPackets(pb, true);
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
					pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT3,
							16 * 1000);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 3);
					pc.sendPackets(pb, true);
				} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
					pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
					pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT3,
							16 * 1000);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 3);
					pc.sendPackets(pb, true);
				} else {
					pc.getSkillEffectTimerSet().setSkillEffect(STATUS_SPOT1,
							16 * 1000);
					S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 1);
					pc.sendPackets(pb, true);
				}
			  }
		}
		return dmg;
	}

	private static double calcDamageReduction(L1Character cha, double dmg,
			int attr) {
		if (isFreeze(cha)) {
			return 0;
		}
		int MagicResistance = 0; // 마법저항
		int RealMagicResistance = 0; // 적용되는 마법저항값
		double calMr = 0.00D; // 마방계산
		double baseMr = 0.00D;
		if (cha instanceof L1PcInstance) {
			baseMr = (_random.nextInt(1000) + 98000) / 100000D;

			if (MagicResistance <= 100) {
				calMr = baseMr - (MagicResistance * 470) / 100000D;
			} else if (MagicResistance > 100) {
				calMr = baseMr - (MagicResistance * 470) / 100000D
						+ ((MagicResistance - 100) * 0.004);
			}
		} else {
			calMr = (200 - RealMagicResistance) / 250.00D;
		}

		dmg *= calMr;

		int resist = 0;
		if (attr == L1Skills.ATTR_EARTH) {
			resist = cha.getResistance().getEarth();
		} else if (attr == L1Skills.ATTR_FIRE) {
			resist = cha.getResistance().getFire();
		} else if (attr == L1Skills.ATTR_WATER) {
			resist = cha.getResistance().getWater();
		} else if (attr == L1Skills.ATTR_WIND) {
			resist = cha.getResistance().getWind();
		}
		int resistFloor = (int) (0.32 * Math.abs(resist));
		if (resist >= 0) {
			resistFloor *= 1;
		} else {
			resistFloor *= -1;
		}
		double attrDeffence = resistFloor / 32.0;
		dmg = (1.0 - attrDeffence) * dmg;

		if (dmg <= 0) {
			dmg = 0;
		} else {
			if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC)) {
				cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
			}
		}
		return dmg;
	}

	// 마법검 카매로방어
	private static boolean isCounterMagic(L1Character cha) {
		if (cha.getSkillEffectTimerSet().hasSkillEffect(COUNTER_MAGIC)) {
			cha.getSkillEffectTimerSet().removeSkillEffect(COUNTER_MAGIC);
			int castgfx = SkillsTable.getInstance().getTemplate(COUNTER_MAGIC)
					.getCastGfx();
			Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(),
					castgfx));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
			}
			return true;
		}
		return false;
	}

	private static boolean isFreeze(L1Character cha) {
		if (cha.getSkillEffectTimerSet().hasSkillEffect(STATUS_FREEZE)) {
			return true;
		}
		if (cha.getSkillEffectTimerSet().hasSkillEffect(ABSOLUTE_BARRIER)
				|| cha.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.STATUS_안전모드)) {
			return true;
		}
		if (cha.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)) {
			return true;
		}
		if (cha.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)) {
			return true;
		}
		if (cha.isParalyzed()) {
			return true;
		}
		if (cha.getSkillEffectTimerSet().hasSkillEffect(COUNTER_MAGIC)) {
			cha.getSkillEffectTimerSet().removeSkillEffect(COUNTER_MAGIC);
			int castgfx = SkillsTable.getInstance().getTemplate(COUNTER_MAGIC)
					.getCastGfx();
			Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(),
					castgfx));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
			}
			return true;
		}
		return false;
	}

	private static double calcAttrResistance(L1Character cha, int attr) {
		int resist = 0;
		if (cha instanceof L1PcInstance) {
			switch (attr) {
			case L1Skills.ATTR_EARTH:
				resist = cha.getResistance().getEarth();
				break;
			case L1Skills.ATTR_FIRE:
				resist = cha.getResistance().getFire();
				break;
			case L1Skills.ATTR_WATER:
				resist = cha.getResistance().getWater();
				break;
			case L1Skills.ATTR_WIND:
				resist = cha.getResistance().getWind();
				break;
			}
		}
		double attrDeffence = resist / 4 * 0.01;

		return attrDeffence;
	}
}
