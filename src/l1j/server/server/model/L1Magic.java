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

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.Random;

import l1j.server.Config;
import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.ActionCodes;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CalcStat;

public class L1Magic {

	private int _calcType;

	private final int PC_PC = 1;

	private final int PC_NPC = 2;

	private final int NPC_PC = 3;

	private final int NPC_NPC = 4;

	private L1PcInstance _pc = null;

	private L1PcInstance _targetPc = null;

	private L1NpcInstance _npc = null;

	private L1NpcInstance _targetNpc = null;

	private int _leverage = 13;

	private L1Skills _skill;

	private static Random _random = new Random(System.nanoTime());

	public void setLeverage(int i) {
		_leverage = i;
	}

	private int getLeverage() {
		return _leverage;
	}

	public L1Magic(L1Character attacker, L1Character target) {
		if (attacker instanceof L1PcInstance) {
			if (target instanceof L1PcInstance) {
				_calcType = PC_PC;
				_pc = (L1PcInstance) attacker;
				_targetPc = (L1PcInstance) target;
			} else {
				_calcType = PC_NPC;
				_pc = (L1PcInstance) attacker;
				_targetNpc = (L1NpcInstance) target;
			}
		} else {
			if (target instanceof L1PcInstance) {
				_calcType = NPC_PC;
				_npc = (L1NpcInstance) attacker;
				_targetPc = (L1PcInstance) target;
			} else {
				_calcType = NPC_NPC;
				_npc = (L1NpcInstance) attacker;
				_targetNpc = (L1NpcInstance) target;
			}
		}
	}

	@SuppressWarnings("unused")
	private int getSpellPower() {
		int spellPower = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			spellPower = _pc.getAbility().getSp();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			spellPower = _npc.getAbility().getSp();
		}
		return spellPower;
	}

	private int getMagicLevel() {
		int magicLevel = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicLevel = _pc.getAbility().getMagicLevel();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			magicLevel = _npc.getAbility().getMagicLevel();
		}
		return magicLevel;
	}

	private int getMagicBonus() {
		int magicBonus = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicBonus = _pc.getAbility().getMagicBonus();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			magicBonus = _npc.getAbility().getMagicBonus();
		}
		return magicBonus;
	}

	private int getLawful() {
		int lawful = 0;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			lawful = _pc.getLawful();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			lawful = _npc.getLawful();
		}
		return lawful;
	}

	private int getTargetMr() {
		int mr = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			mr = _targetPc.getResistance().getEffectedMrBySkill();
		} else {
			mr = _targetNpc.getResistance().getEffectedMrBySkill();
		}
		return mr;
	}

	/* ■■■■■■■■■■■■■■ 성공 판정 ■■■■■■■■■■■■■ */
	// ●●●● 확률계 마법의 성공 판정 ●●●●
	// 계산방법
	// 공격측 포인트：LV + ((MagicBonus * 3) * 마법 고유 계수)
	// 방어측 포인트：((LV / 2) + (MR * 3)) / 2
	// 공격 성공율：공격측 포인트 - 방어측 포인트
	public boolean calcProbabilityMagic(int skillId) {
		int probability = 0;
		boolean isSuccess = false;

		if (_pc != null && _pc.isGm()) {
			return true;
		}

		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId >= 45912
					&& npcId <= 45915
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_WATER)) {
				return false;
			}
			
			if (npcId >= 145685 && npcId <= 145686  && !_pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.할파스권속버프)) {
				return false;
			}
			
			if (npcId == 45916
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_MITHRIL_POWDER)) {
				return false;
			}
			if (npcId == 45941
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_WATER_OF_EVA)) {
				return false;
			}
			if (!_pc.getSkillEffectTimerSet().hasSkillEffect(
					STATUS_CURSE_BARLOG)
					&& (npcId == 445752 || npcId == 445753)) {//발록버프 해제
				return false;
			}
			if (!_pc.getSkillEffectTimerSet() //야히 버프없이 때리게 가능
					.hasSkillEffect(STATUS_CURSE_YAHEE)
					&& (npcId == 45675111 || npcId == 81082111 || npcId == 45625111
							|| npcId == 45674111 || npcId == 45685111)) {
				return false;
			}
			if (npcId >= 46068 && npcId <= 46091
					&& _pc.getGfxId().getTempCharGfx() == 6035) {
				return false;
			}
			if (npcId >= 46092 && npcId <= 46106
					&& _pc.getGfxId().getTempCharGfx() == 6034) {
				return false;
			}
		}

		if (!checkZone(skillId)) {
			return false;
		}

		if (skillId == CANCELLATION) {
			if (_calcType == PC_PC && _pc != null && _targetPc != null) {
				if (_pc.getId() == _targetPc.getId()) {
					return true;
				}
			
				/*
				 * if (_targetPc.isInvisble()){ _targetPc.delInvis(); return
				 * true; }
				 */

				if (_pc.isInParty()) {
					if (_pc.getParty().isMember(_targetPc)) {
						return true;
					}
				}

				if (CharPosUtil.getZoneType(_pc) == 1
						|| CharPosUtil.getZoneType(_targetPc) == 1) {
					return false;
				}
			}
			if (_calcType == PC_NPC || _calcType == NPC_PC
					|| _calcType == NPC_NPC) {
				return true;
			}
		}

		if (_calcType == PC_NPC
				&& _targetNpc.getNpcTemplate().isCantResurrect()

		) { // 50렙 이상 npc 에게 아래 마법 안걸림:즉 보스몬스터에게 사용불가
			if (skillId == WEAPON_BREAK || skillId == CURSE_PARALYZE
					|| skillId == MANA_DRAIN || skillId == WEAKNESS
					|| skillId == SILENCE /*|| skillId == DISEASE*/
					|| skillId == DECAY_POTION /*|| skillId == MASS_SLOW*/
					|| skillId == ERASE_MAGIC || skillId == AREA_OF_SILENCE
					|| skillId == WIND_SHACKLE

					|| skillId == FOG_OF_SLEEPING || skillId == ICE_LANCE
					|| skillId == POLLUTE_WATER || skillId == RETURN_TO_NATURE
					|| skillId == THUNDER_GRAB || skillId == 파워그립
					|| skillId == 쉐도우스탭) {
				return false;
			}
		}

		/*
		 * if(_calcType == PC_PC){ if(_targetPc.getLevel() <= Config.MAX_LEVEL
		 * || _pc.getLevel() <= Config.MAX_LEVEL){ //레벨65까지 if(_pc.getClanid()
		 * == 0 || _targetPc.getClanid() == 0){///무혈일경우 _skill =
		 * SkillsTable.getInstance().getTemplate(skillId); if (skillId !=
		 * L1Skills.TYPE_CHANGE) { // 버프계 _pc.sendPackets(new
		 * S_SystemMessage("\\fW혈맹이 없거나 레벨"+Config.MAX_LEVEL+"이하라 마법을 실패합니다."));
		 * _targetPc.sendPackets(new
		 * S_SystemMessage("\\fW상대방이 당신에게 마법을 실패하였습니다.")); return false; } } } }
		 */

		if (_calcType == PC_PC) { // 디스중첩 안되게
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(DISINTEGRATE)) {
				if (skillId == DISINTEGRATE) {
					return false;
				}
			}
		}
		if (_calcType == PC_PC) { // 이터너티중첩 안되게
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(ETERNITY)) {
				if (skillId == ETERNITY) {
					return false;
				}
			}
		}
		// 아스바인드중은 WB, 왈가닥 세레이션 이외 무효
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)) {
				_skill = SkillsTable.getInstance().getTemplate(skillId);
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION // 확률계
						&& _skill.getType() != L1Skills.TYPE_HEAL // 힐 계
						&& _skill.getType() != L1Skills.TYPE_CHANGE) { // 버프계
					return false;
				}
			}
		} else {
			if (_targetNpc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)) {
				if (skillId != WEAPON_BREAK && skillId != CANCELLATION) {
					return false;
				}
			}
		}

		// 100% 확률을 가지는 스킬
		if (skillId == SMASH || skillId == MIND_BREAK || skillId == AM_BREAK
				|| skillId == IllUSION_AVATAR || skillId == RETURN_TO_NATURE || skillId == AREA_OF_SILENCE) {
			return true;
		}
		probability = calcProbability(skillId);
		int rnd = 0;

		if ((skillId == EARTH_BIND || skillId == DARKNESS
				|| skillId == CURSE_BLIND || skillId == CURSE_PARALYZE)) {
			if (_calcType == PC_NPC) {
				if (_targetNpc.getLevel() >= 70) {
					return false;
				}
			}
		}

		switch (skillId) {
		case CANCELLATION:
		case SILENCE:
		case L1SkillId.데스힐:
			if (_calcType == PC_PC) {
				if (_targetPc instanceof L1RobotInstance) {
					if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ERASE_MAGIC)) {
						probability = 100;
						rnd = 100;
					} else {
						probability = 40;
						rnd = _random.nextInt(_targetPc.getResistance()
								.getEffectedMrBySkill() + 1) + 1;
					}
				}else{
					rnd = _random.nextInt(100) + 1;
				}
					
					
					/* else {
				}
					if ((skillId == SILENCE && _targetPc.getResistance()
							.getEffectedMrBySkill() >= 170)
							|| (skillId == DECAY_POTION && _targetPc
									.getResistance().getEffectedMrBySkill() >= 175)
							|| (skillId == CANCELLATION && _targetPc
									.getResistance().getEffectedMrBySkill() >= 150)
							|| _targetPc.getResistance().getEffectedMrBySkill() >= 160) {
						probability = 0;
						rnd = 100;
					} else {
						rnd = _random.nextInt(_targetPc.getResistance()
								.getEffectedMrBySkill() + 1) + 1;
					}
				}*/

				if (_targetPc.isInvisble()) {
					probability = 0;
				}

			} else if (_calcType == PC_NPC) {
				rnd = 40;
			} else {
				rnd = _random.nextInt(100) + 1;
			}
			break;
		case DECAY_POTION:
		case CURSE_PARALYZE:
		case SLOW:
		case DARKNESS:
		case WEAKNESS:
		case CURSE_POISON:
		case CURSE_BLIND:
		case WEAPON_BREAK:
		case MANA_DRAIN:
			if (_calcType == PC_PC) {
				if (_targetPc instanceof L1RobotInstance) {
					if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ERASE_MAGIC)) {
						probability = 30;
						rnd = 30;
					} else {
						probability = 20;
						rnd = _random.nextInt(_targetPc.getResistance()
								.getEffectedMrBySkill() + 1) + 1;
					}
				}else{
					rnd = _random.nextInt(100) + 1;
				}
					/* else {
				}
					if ((skillId == SILENCE && _targetPc.getResistance()
							.getEffectedMrBySkill() >= 170)
							|| (skillId == DECAY_POTION && _targetPc
									.getResistance().getEffectedMrBySkill() >= 175)
							|| (skillId == CANCELLATION && _targetPc
									.getResistance().getEffectedMrBySkill() >= 150)
							|| _targetPc.getResistance().getEffectedMrBySkill() >= 160) {
						probability = 0;
						rnd = 100;
					} else {
						rnd = _random.nextInt(_targetPc.getResistance()
								.getEffectedMrBySkill() + 1) + 1;
					}
				}*/
			} else if (_calcType == PC_NPC) {
				rnd = 20;
			} else {
			}
			break;
		default:
			rnd = _random.nextInt(100) + 1;
			if (probability > 80)
				probability = 80;
			/** 아랜 -> 얼녀,아이스데몬 마방120이상 대상에게 안들어가게 **/
			if (skillId == ICE_LANCE && _calcType == NPC_PC
					&& (_npc.getNpcId() == 46141 || _npc.getNpcId() == 46142)
					&& _targetPc.getResistance().getEffectedMrBySkill() >= 100)
				probability = 0;
			else if (skillId == ICE_LANCE && _calcType == NPC_PC
					&& _npc.getNpcId() == 100367 && _targetPc.getResistance().getEffectedMrBySkill() >= 140)
				probability = 0;
			else if (skillId == ICE_LANCE && _calcType == NPC_PC
					&& (_npc.getNpcId() == 46141 || _npc.getNpcId() == 46142)) {
				probability *= 0.5;
			} else if (skillId == TURN_UNDEAD && _calcType == PC_NPC
					&& _pc != null && !_pc.isWizard())
				probability *= 0.5;

			break;
		}

	/*	if (_calcType == PC_PC || _calcType == PC_NPC) {
			int addprob = CalcStat.마법명중(_pc.getAbility().getTotalInt()) / 2;
			if (probability != 0) {
				probability += addprob;
			}
		}
*/
		if (probability >= rnd) {
			isSuccess = true;
		} else {
			isSuccess = false;
			if (skillId == TURN_UNDEAD && _calcType == PC_NPC
					&& _targetNpc != null) {
				if (_random.nextInt(100) + 1 < 20) {
					if (!_targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.HASTE)) {
						// new L1SkillUse().handleCommands(_pc, L1SkillId.HASTE,
						// _targetNpc.getId(), _targetNpc.getX(),
						// _targetNpc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						_targetNpc.getSkillEffectTimerSet().setSkillEffect(
								L1SkillId.HASTE, 9999 * 1000);
						Broadcaster.broadcastPacket(_targetNpc,
								new S_SkillHaste(_targetNpc.getId(), 1, 0),
								true);
						_targetNpc.getMoveState().setMoveSpeed(1);
						Broadcaster.broadcastPacket(_targetNpc,
								new S_EffectLocation(_targetNpc.getX(),
										_targetNpc.getY(), (short) 8987), true);
					}
				}
			}
		}

		if (!Config.ALT_ATKMSG) {
			return isSuccess;
		}
		if (_targetPc == null && _targetNpc == null)
			return isSuccess;

		String msg2 = "확률:" + probability + "%";
		String msg3 = "";
		if (isSuccess == true) {
			msg3 = "성공";
		} else {
			msg3 = "실패";
		}

		if (_pc != null && _pc.isGm()) {
			_pc.sendPackets(new S_SystemMessage("\\fT["
					+ _pc.getName()
					+ "] ==> ["
					+ (_targetPc == null ? _targetNpc.getName() : _targetPc
							.getName()) + "][== " + msg2 + " ==][" + msg3 + "]"));
		}
		if (_targetPc != null && _targetPc.isGm()) {
			_targetPc.sendPackets(new S_SystemMessage("\\fY["
					+ _targetPc.getName() + "] <== ["
					+ (_pc == null ? _npc.getName() : _pc.getName()) + "][== "
					+ msg2 + " ==][" + msg3 + "]"));
		}
		/*
		 * if (Config.ALT_ATKMSG) { if ((_calcType == PC_PC || _calcType ==
		 * PC_NPC) && !_pc.isGm()) { return isSuccess; } if ((_calcType == PC_PC
		 * || _calcType == NPC_PC) && !_targetPc.isGm()) { return isSuccess; } }
		 * 
		 * String msg0 = ""; String msg1 = "왜"; String msg2 = ""; String msg3 =
		 * ""; String msg4 = "";
		 * 
		 * if (_calcType == PC_PC || _calcType == PC_NPC) { msg0 =
		 * _pc.getName(); } else if (_calcType == NPC_PC) { msg0 =
		 * _npc.getName(); }
		 * 
		 * msg2 = "probability:" + probability + "%"; if (_calcType == NPC_PC ||
		 * _calcType == PC_PC) { msg4 = _targetPc.getName(); } else if
		 * (_calcType == PC_NPC) { msg4 = _targetNpc.getName(); } if (isSuccess
		 * == true) { msg3 = "성공"; } else { msg3 = "실패"; }
		 * 
		 * if (_calcType == PC_PC || _calcType == PC_NPC) { _pc.sendPackets(new
		 * S_ServerMessage(166, msg0, msg1, msg2, msg3, msg4)); } if (_calcType
		 * == NPC_PC || _calcType == PC_PC) { _targetPc.sendPackets(new
		 * S_ServerMessage(166, msg0, msg1, msg2, msg3, msg4)); }
		 */
		return isSuccess;
	}

	private boolean checkZone(int skillId) {
		if (_pc != null && _targetPc != null) {
			if (CharPosUtil.getZoneType(_pc) == 1
					|| CharPosUtil.getZoneType(_targetPc) == 1) {
				if (skillId == WEAPON_BREAK || skillId == SLOW
						|| skillId == CURSE_PARALYZE || skillId == MANA_DRAIN
						|| skillId == DARKNESS || skillId == WEAKNESS
						/*|| skillId == DISEASE*/ || skillId == DECAY_POTION
						/*|| skillId == MASS_SLOW*/ /*|| skillId == ENTANGLE*/
						|| skillId == ERASE_MAGIC || skillId == EARTH_BIND
						|| skillId == AREA_OF_SILENCE || skillId == 쉐도우스탭
						|| skillId == WIND_SHACKLE || skillId == STRIKER_GALE
						|| skillId == SHOCK_STUN || skillId == 엠파이어  || skillId == 파워그립 || skillId == JUDGEMENT
						|| skillId == 데스페라도 || skillId == FOG_OF_SLEEPING || skillId == PANTERA || skillId == BLADE || skillId == PHANTOM
						|| skillId == ICE_LANCE || skillId == HORROR_OF_DEATH
						|| skillId == POLLUTE_WATER || skillId == FEAR
						|| skillId == ELEMENTAL_FALL_DOWN || skillId == FORCE_STUN
						|| skillId == ETERNITY
						|| skillId == DEMOLITION || skillId == AVENGER
						|| skillId == RETURN_TO_NATURE || skillId == PHANTASM
						|| skillId == CONFUSION || skillId == SILENCE
						|| skillId == L1SkillId.데스힐) {
					return false;
				}
			}
		}
		return true;
	}

	private int calcProbability(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int attackLevel = 0;
		int defenseLevel = 0;
		int probability = 0;
		int attackInt = 0;
		int defenseMr = 0;

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			attackLevel = _pc.getLevel();
			attackInt = _pc.getAbility().getTotalInt();
		} else {
			attackLevel = _npc.getLevel();
			attackInt = _npc.getAbility().getTotalInt();
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			defenseLevel = _targetPc.getLevel();
			defenseMr = _targetPc.getResistance().getEffectedMrBySkill();
		} else {
			defenseLevel = _targetNpc.getLevel();
			defenseMr = _targetNpc.getResistance().getEffectedMrBySkill();
			if (skillId == RETURN_TO_NATURE) {
				if (_targetNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _targetNpc;
					defenseLevel = summon.getMaster().getLevel();
				}
			}
		}

		switch (skillId) {
		case ERASE_MAGIC:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3)  + Config.이레매직확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.이레매직확률;
			}
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}
			if (probability > 60) {
				probability = 60;
			}
			break;

		case STRIKER_GALE:
		case POLLUTE_WATER:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 30;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 30;
			}
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}
			
			if (probability > 60) {
				probability = 60;
			}
			if (probability < 10) {
				probability = 10;
			}
			break;

		case 데스페라도:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.데페확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.데페확률;
			}
			
			if (_calcType == PC_PC && skillId != AREA_OF_SILENCE) {
				probability += _pc.getFearHit();
				probability -= _targetPc.getFearTolerance();
			}
			
			

			if (probability > 60) {
				probability = 60;
			}

			if (probability < 10)
				probability = 10;
			break;
			
		case DEMOLITION:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.DEMOLITION확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.DEMOLITION확률;
			}
			
			if (_calcType == PC_PC && skillId != AREA_OF_SILENCE) {
				probability += _pc.getFearHit();
				probability -= _targetPc.getFearTolerance();
			}
			
			

			if (probability > 60) {
				probability = 60;
			}

			if (probability < 10)
				probability = 10;
			break;

		case 쉐도우스탭:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.쉐도우스탭확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.쉐도우스탭확률;
			}
			
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}

			if (probability > 60) {
				probability = 60;
			}

			if (probability < 10)
				probability = 10;

			break;
		
		case 파워그립:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.그립확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.그립확률;
			}
			
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getFearHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getFearTolerance();
				probability -= stunregi;
			}

			if (probability > 60) {
				probability = 60;
			}

			if (probability < 10)
				probability = 10;

			break;
		case SHOCK_STUN:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.스턴확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.스턴확률;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getTechniqueHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}

			if (probability > 60) {
				probability = 60;
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getTechniqueTolerance();
				probability -= stunregi;
			}
			if (probability < 10) {
				probability = 10;
			}
			
			break;
		case JUDGEMENT:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.저지먼트확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.저지먼트확률;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getFearHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}

			if (probability > 60) {
				probability = 60;
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getFearTolerance();
				probability -= stunregi;
			}
			if (probability < 10) {
				probability = 10;
			}
			
			break;
		case PANTERA:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.판테라확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.판테라확률;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getFearHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}

			if (probability > 60) {
				probability = 60;
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getFearTolerance();
				probability -= stunregi;
			}
			if (probability < 10) {
				probability = 10;
			}
			
			break;
		case PHANTOM:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.팬텀확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.팬텀확률;
			}
			
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getFearHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getFearTolerance();
				probability -= stunregi;
			}

			if (probability > 60) {
				probability = 60;
			}

			if (probability < 10)
				probability = 10;

			break;
		case FORCE_STUN:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.포스스턴확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.포스스턴확률;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getTechniqueHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}

			if (probability > 60) {
				probability = 60;
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getTechniqueTolerance();
				probability -= stunregi;
			}
			if (probability < 10) {
				probability = 10;
			}
			
			break;
		case BLADE:
				probability = 100;
			break;
		case ETERNITY:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.이터너티확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.이터너티확률;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}

			if (probability > 60) {
				probability = 60;
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}
			if (probability < 10) {
				probability = 10;
			}
			
			break;
		case AVENGER:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.어벤저확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.어벤저확률;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}

			if (probability > 60) {
				probability = 60;
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}
			if (probability < 10) {
				probability = 10;
			}
			
			break;
			
		case 엠파이어:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.엠파이어;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.엠파이어;
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getTechniqueHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}

			if (probability > 60) {
				probability = 60;
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getTechniqueTolerance();
				probability -= stunregi;
			}
			if (probability < 10){
				probability = 10;
			}
			break;
			
			
		case EARTH_BIND:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.어바확률;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.어바확률;
			}
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}
			if (probability > 60) {
				probability = 60;
			}
			break;
			
			
		case THUNDER_GRAB:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 40;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 40;
			}
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getDragonLangHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getDragonLangTolerance();
				probability -= stunregi;
			}
			if (probability > 60) {
				probability = 60;
			}
			break;
	
		case AREA_OF_SILENCE:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 45;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 45;
			}
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if (probability > 60) {
				probability = 60;
			}
			if (_calcType == PC_PC && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}
			break;
			
		case ELEMENTAL_FALL_DOWN:
			probability = (int) (40 + (attackLevel - defenseLevel));
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}
			break;
		case RETURN_TO_NATURE:
		case WIND_SHACKLE:
			probability = (int) (1 + (attackLevel - defenseLevel));
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += _pc.getBaseMagicHitUp();
			}
			break;
		case COUNTER_BARRIER:
			probability = 25;
			break;
		case HALPHAS:
			probability = 25;
			break;
		case 인페르노:
			probability = 25;
			break;
		case MORTAL_BODY:
			probability = 15;
			break;
		case PANIC:
			probability = (int) (((l1skills.getProbabilityDice()) / 10D) * (attackLevel - defenseLevel))
					+ l1skills.getProbabilityValue();
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += 2 * _pc.getBaseMagicHitUp();
			}
			break;
		case DARKNESS:
		case WEAKNESS:
		case CURSE_PARALYZE:
		case CURSE_POISON:
		case CURSE_BLIND:
		case WEAPON_BREAK:
		case CANCELLATION: {
			if(attackInt > 50)attackInt=50;
			if (attackLevel >= defenseLevel) {
				defenseMr -= (int) ((attackLevel - defenseLevel) * 5);
			} else if (attackLevel < defenseLevel) {
				defenseMr -= (int) ((attackLevel - defenseLevel) * 5);
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				int 마법명중 = CalcStat.마법명중(_pc.getAbility().getTotalInt());
				if (마법명중 > 14) {
					int temp = 마법명중 - 14;
					defenseMr -= (int) (temp * 4);
				}
				for (L1DollInstance doll : _pc.getDollList()) {
					defenseMr -= doll.get마법명중();
				}
				
				L1ItemInstance run = _pc.getInventory().getItemEquipped(2, 14);
				if (run != null) {
					if (run.getItemId() == 427115) { // 마법 명중 룬
						defenseMr -= 5;
					} else if (run.getItemId() == 427205) { // 장로의 부러진 지팡이
						defenseMr -= 10;
					}
				}
				if (_pc.getInventory().checkEquipped(21269)) {
					defenseMr -= 15;
				}
			}
			//_pc.sendPackets(new S_SystemMessage("최종상대마방 : "+defenseMr));
			if (defenseMr > 150)
				return 0;

			probability = (int) ((attackInt*2) - (defenseMr / 1.5));
			
			//_pc.sendPackets(new S_SystemMessage("확률 : "+probability));
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc.isElf()) {
					if (_calcType == PC_PC) {
						if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.ERASE_MAGIC)) {
							probability *= 2;
						}
					}
					if (_calcType == PC_NPC) {
						if (_targetNpc.getSkillEffectTimerSet()
								.hasSkillEffect(L1SkillId.ERASE_MAGIC)) {
							probability *= 2;
						}
					}
				}
			}
			//_pc.sendPackets(new S_SystemMessage("확률 : "+probability));
			if (_calcType == PC_NPC) {
				if (_targetNpc.getLevel() >= 70) {
					probability = 0;
				}
			}
			if (skillId == SHAPE_CHANGE) {
				if (_calcType == PC_PC) {
					if (_pc.getId() == _targetPc.getId()) {
						// System.out.println("123");
						probability = 30;
					}
				}
			}
			if (probability < 0)
				probability = 0;
		}
			break;
		case DECAY_POTION:
		case SHAPE_CHANGE:
		case SLOW: {
			if(attackInt > 50)attackInt=50;
			if (attackLevel >= defenseLevel) {
				defenseMr -= (int) ((attackLevel - defenseLevel) * 5);
			} else if (attackLevel < defenseLevel) {
				defenseMr -= (int) ((attackLevel - defenseLevel) * 5);
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				int 마법명중 = CalcStat.마법명중(_pc.getAbility().getTotalInt());
				if (마법명중 > 14) {
					int temp = 마법명중 - 14;
					defenseMr -= (int) (temp * 4);
				}
				for (L1DollInstance doll : _pc.getDollList()) {
					defenseMr -= doll.get마법명중();
				}
				
				
				L1ItemInstance run = _pc.getInventory().getItemEquipped(2, 14);
				if (run != null) {
					if (run.getItemId() == 427115) { // 마법 명중 룬
						defenseMr -= 5;
					} else if (run.getItemId() == 427205) { // 장로의 부러진 지팡이
						defenseMr -= 10;
					}
				}
				if (_pc.getInventory().checkEquipped(21269)) {
					defenseMr -= 15;
				}
			}
			//_pc.sendPackets(new S_SystemMessage("최종상대마방 : "+defenseMr));
			if (defenseMr > 160)
				return 0;
			
			probability = (int) ((attackInt*2) - (defenseMr / 1.6));
			//
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc.isElf()) {
					if (_calcType == PC_PC) {
						if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.ERASE_MAGIC)) {
							probability *= 2;
						}
					}
					if (_calcType == PC_NPC) {
						if (_targetNpc.getSkillEffectTimerSet()
								.hasSkillEffect(L1SkillId.ERASE_MAGIC)) {
							probability *= 2;
						}
					}
				}
			}
			//_pc.sendPackets(new S_SystemMessage("확률 : "+probability));
			if (_calcType == PC_NPC) {
				if (_targetNpc.getLevel() >= 70) {
					probability = 0;
				}
			}
			if (skillId == SHAPE_CHANGE) {
				if (_calcType == PC_PC) {
					if (_pc.getId() == _targetPc.getId()) {
						// System.out.println("123");
						probability = 30;
					}
				}
			}
			if (probability < 0)
				probability = 0;
		}
			break;
		case SILENCE:
		case FOG_OF_SLEEPING:
		case L1SkillId.데스힐:{
			if(attackInt > 50)attackInt=50;
			if (attackLevel >= defenseLevel) {
				defenseMr -= (int) ((attackLevel - defenseLevel) * 5);
			} else if (attackLevel < defenseLevel) {
				defenseMr -= (int) ((attackLevel - defenseLevel) * 5);
			}

			if (_calcType == PC_PC || _calcType == PC_NPC) {
				int 마법명중 = CalcStat.마법명중(_pc.getAbility().getTotalInt());
				if (마법명중 > 14) {
					int temp = 마법명중 - 14;
					defenseMr -= (int) (temp * 4);
				}
				for (L1DollInstance doll : _pc.getDollList()) {
					defenseMr -= doll.get마법명중();
				}
				L1ItemInstance run = _pc.getInventory().getItemEquipped(2, 14);
				if (run != null) {
					if (run.getItemId() == 427115) { // 마법 명중 룬
						defenseMr -= 5;
					} else if (run.getItemId() == 427205) { // 장로의 부러진 지팡이
						defenseMr -= 10;
					}
				}
				if (_pc.getInventory().checkEquipped(21269)) {
					defenseMr -= 15;
				}
			}
			
			//_pc.sendPackets(new S_SystemMessage("최종상대마방 : "+defenseMr));
			if (defenseMr > 170)
				return 0;

			probability = (int) ((attackInt*2) - (defenseMr / 1.7));
			//
			// 인트 - mr/5.95 * 9;
			// 인트18 마방 150 인경우
			// 1.2 * 9%
			// 10프로 정도
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc.isElf()) {
					if (_calcType == PC_PC) {
						if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.ERASE_MAGIC)) {
							probability *= 2;
						}
					}
					if (_calcType == PC_NPC) {
						if (_targetNpc.getSkillEffectTimerSet()
								.hasSkillEffect(L1SkillId.ERASE_MAGIC)) {
							probability *= 2;
						}
					}
				}
			}
			//_pc.sendPackets(new S_SystemMessage("확률 : "+probability));
			if (_calcType == PC_NPC) {
				if (_targetNpc.getLevel() >= 70) {
					probability = 0;
				}
			}
			if (skillId == SHAPE_CHANGE) {
				if (_calcType == PC_PC) {
					if (_pc.getId() == _targetPc.getId()) {
						// System.out.println("123");
						probability = 100;
					}
				}
			}
			if (probability < 0)
				probability = 0;
		}
			break;
		case MANA_DRAIN: {
			if(attackInt > 50)attackInt=50;
			if (attackLevel >= defenseLevel) {
				defenseMr -= (int) ((attackLevel - defenseLevel) * 5);
			} else if (attackLevel < defenseLevel) {
				defenseMr -= (int) ((attackLevel - defenseLevel) * 5);
			}
			int 마법명중 = CalcStat.마법명중(_pc.getAbility().getTotalInt());
			if (마법명중 > 14) {
				int temp = 마법명중 - 14;
				defenseMr -= (int) (temp * 4);
			}
			for (L1DollInstance doll : _pc.getDollList()) {
				defenseMr -= doll.get마법명중();
			}
			//_pc.sendPackets(new S_SystemMessage("최종상대마방 : "+defenseMr));
			if (defenseMr > 170)
				return 0;
			probability = (int) ((attackInt*2) - (defenseMr / 1.8));
			//_pc.sendPackets(new S_SystemMessage("확률 : "+probability));
			if (probability < 0) {
				probability = 3;
			} 
			if (probability > 80) {
				probability = 80;
			}
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability += _pc.getBaseMagicHitUp();
			}
		}
			break;
		case 디스트로이:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 2) + 45;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 2) + 45;
			}
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getDragonLangHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getDragonLangTolerance();
				probability -= stunregi;
			}
			
			if (probability > 90) {
				probability = 90;
			}
			if (probability < 20){
				probability = 20;
			}
			break;

		/*
		 * if (attackLevel >= defenseLevel) { defenseMr -= (int) ((attackLevel -
		 * defenseLevel) * 5); } else if (attackLevel < defenseLevel) {
		 * defenseMr += (int) ((attackLevel - defenseLevel) * 5); }
		 * 
		 * int 마법명중11 = CalcStat.마법명중(_pc.getAbility().getTotalInt()); if(마법명중11
		 * != 0){ defenseMr -= (int) (마법명중11 * 5); }
		 * 
		 * L1ItemInstance run11 = _pc.getInventory().getItemEquipped(2, 14); if
		 * (run11 != null) { if (run11.getItemId() == 427115) { // 마법 명중 룬
		 * defenseMr -= 5; } else if (run11.getItemId() == 427205) { // 장로의 부러진
		 * 지팡이 defenseMr -= 10; } } if (_pc.getInventory().checkEquipped(21269))
		 * { defenseMr -= 15; }
		 * 
		 * probability = 32; if (_calcType == PC_PC) { if
		 * (_targetPc.getResistance().getEffectedMrBySkill() >= 145) probability
		 * = 0; } break;
		 */
		/*case MASS_SLOW:
			probability = 32;
			break;*/
		case BONE_BREAK:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.본브레이크;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + Config.본브레이크;
			}
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getDragonLangHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getDragonLangTolerance();
				probability -= stunregi;
			}
			if (_calcType == PC_PC) {
			L1Attack attack = new L1Attack(_pc, _targetPc);
			if (attack.calcHit())
				attack.calcDamage();
			attack.action();
			attack.commit();
			} else if (_calcType == PC_NPC) {
				L1Attack attack2 = new L1Attack(_pc, _targetNpc);
				if (attack2.calcHit())
					attack2.calcDamage();
				attack2.action();
				attack2.commit();
			}
			
			if (probability < 10) {
				probability = 10;
			}
			break;
		case PHANTASM:
		case CONFUSION:
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 40;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 40;
			}
			if ((_calcType == PC_PC || _calcType == PC_NPC) && skillId != AREA_OF_SILENCE) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getDragonLangHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if ((_calcType == PC_PC || _calcType == NPC_PC) && skillId != AREA_OF_SILENCE) {
				int stunregi = _targetPc.getDragonLangTolerance();
				probability -= stunregi;
			}
			if (probability > 60) {
				probability = 60;
			}
			if (probability < 10) {
				probability = 10;
			}
			break;
		case JOY_OF_PAIN:
			probability = 70;
			break;
		case ARMOR_BREAK: //아머
			if (attackLevel >= defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 55;
			} else if (attackLevel < defenseLevel) {
				probability = (int) ((attackLevel - defenseLevel) * 3) + 55;
			}
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) _pc;
					probability += _pc.getSpiritHit();
					if (pc.get어택레벨() != 0) {
						attackLevel += pc.get어택레벨();
					}
				}
			}
			if (_calcType == PC_PC || _calcType == NPC_PC) {
				int stunregi = _targetPc.getSpiritTolerance();
				probability -= stunregi;
			}
			
			if (probability > 80) {
				probability = 80;
			}
			if (probability < 10) {
				probability = 10;
			}
			break;
		case TURN_UNDEAD:
			// if (attackInt >25) attackInt = 25;
			// if (attackLevel > 52) attackLevel = 52;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				probability = (int) ((attackInt * 2 + (attackLevel * 2.5) + _pc
						.getBaseMagicHitUp())
						- (defenseMr + (defenseLevel / 2)) - 84);
				if (!_pc.isWizard()) {
					probability -= 20;
				}
			} else
				probability = (int) ((attackInt * 2 + (attackLevel * 2.5))
						- (defenseMr + (defenseLevel / 2)) - 84);

			if (attackInt >= 20) {
				int addpro = 0;
				if (attackInt >= 75) {
					addpro += 18;
				} else if (attackInt >= 60) {
					addpro += 16;
				} else if (attackInt >= 65) {
					addpro += 14;
				} else if (attackInt >= 55) {
					addpro += 12;
				} else if (attackInt >= 45) {
					addpro += 10;
				} else if (attackInt >= 35) {
					addpro += 8;
				} else if (attackInt >= 30) {
					addpro += 6;
				} else if (attackInt >= 25) {
					addpro += 4;
				} else if (attackInt >= 20) {
					addpro += 2;
				}
				probability += addpro;
			}

			/*
			 * if(_pc.getLevel() >= 75){ int addpro = (_pc.getLevel() - 74) * 2;
			 * if(addpro > 16)addpro = 16; probability += addpro; }
			 */

			for (L1DollInstance doll : _pc.getDollList()) {
				defenseMr -= doll.get마법명중();
			}
			
			if (probability > 0) {
				L1ItemInstance run = _pc.getInventory().getItemEquipped(2, 14);
				if (run != null) {
					if (run.getItemId() == 427115) { // 마법 명중 룬
						probability += 5;
					} else if (run.getItemId() == 427205) { // 장로의 부러진 지팡이
						probability += 10;
					}
				}
				if (_pc.getInventory().checkEquipped(21269)) {
					probability += 15;
				}
			}

			break;
		default: {
			int dice1 = l1skills.getProbabilityDice();
			int diceCount1 = 0;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc.isWizard()) {
					diceCount1 = getMagicBonus() + getMagicLevel() + 1;
				} else if (_pc.isElf()) {
					diceCount1 = getMagicBonus() + getMagicLevel() - 1;
				} else if (_pc.isDragonknight()) {
					diceCount1 = getMagicBonus() + getMagicLevel();
				} else {
					diceCount1 = getMagicBonus() + getMagicLevel() - 1;
				}
			} else {
				diceCount1 = getMagicBonus() + getMagicLevel();
			}
			if (diceCount1 < 1) {
				diceCount1 = 1;
			}
			for (int i = 0; i < diceCount1; i++) {
				probability += (_random.nextInt(dice1) + 1);
			}
			probability = probability * getLeverage() / 10;
			probability -= getTargetMr();

			if (skillId == TAMING_MONSTER) {
				double probabilityRevision = 1;
				if ((_targetNpc.getMaxHp() * 1 / 4) > _targetNpc.getCurrentHp()) {
					probabilityRevision = 1.3;
				} else if ((_targetNpc.getMaxHp() * 2 / 4) > _targetNpc
						.getCurrentHp()) {
					probabilityRevision = 1.2;
				} else if ((_targetNpc.getMaxHp() * 3 / 4) > _targetNpc
						.getCurrentHp()) {
					probabilityRevision = 1.1;
				}
				probability *= probabilityRevision;
			}
		}

			break;
		}
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (probability > 0) {
				if (skillId != L1SkillId.SHOCK_STUN
						&& skillId != L1SkillId.데스페라도
						&& skillId != L1SkillId.파워그립 && skillId != L1SkillId.DEMOLITION) {
					L1ItemInstance run = _pc.getInventory().getItemEquipped(2,
							14);
					if (run != null) {
						if (run.getItemId() == 427115) { // 마법 명중 룬
							probability += 1;
						} else if (run.getItemId() == 427205) { // 장로의 부러진 지팡이
							probability += 2;
						}
					}
					if (_pc.getInventory().checkEquipped(21269)) {
						probability += 3;
					}
				}
			}
		}
		return probability;
	}

	public int calcMagicDamage(int skillId) {
		int damage = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			damage = calcPcMagicDamage(skillId);
			if (_calcType == PC_PC) {
				if (_pc.getClanid() > 0
						&& (_pc.getClanid() == _targetPc.getClanid())) {
					if (skillId == 17 || skillId == 22 || skillId == 25
							|| skillId == 53 || skillId == 53 || skillId == 59
							|| skillId == 62 || skillId == 65 || skillId == 70
							|| skillId == 74 || skillId == 76) { // 미티어 포함한 범위마법들..
						damage = 0;
					}
				}
			}
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			damage = calcNpcMagicDamage(skillId);
			// ////////혈원에게는 범위마법 데미지 0 디플추가////////////
			// ////////혈원에게는 범위마법 데미지 0 디플추가끝///////////
		}

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (skillId == L1SkillId.FIREBALL) {
				damage /= 3;
			}

			damage += CalcStat.마법대미지(_pc.getAbility().getTotalInt());

			double balance = CalcStat.마법보너스(_pc.getAbility().getTotalInt()) * 0.01;

			damage += damage * balance;
		}

		// 파푸가호
		if (_targetPc != null) {
			// Random random = new Random();
			int chance1 = _random.nextInt(100) + 1;
			if ((_targetPc.getInventory().checkEquipped(420104)
					|| _targetPc.getInventory().checkEquipped(420105)
					|| _targetPc.getInventory().checkEquipped(420106) || _targetPc
					.getInventory().checkEquipped(420107)) && chance1 < 5) {
				// 123456 일때 80~100
				// 파푸 가호 7일때 120~140 / 8일때 140~160 9일때 160~180
				int addhp = _random.nextInt(20) + 1;
				int basehp = 80;

				L1ItemInstance item = null;
				if (_targetPc.getInventory().checkEquipped(420104)) {
					item = _targetPc.getInventory().checkEquippedItem(420104);
				}
				if (_targetPc.getInventory().checkEquipped(420105)) {
					item = _targetPc.getInventory().checkEquippedItem(420105);
				}
				if (_targetPc.getInventory().checkEquipped(420106)) {
					item = _targetPc.getInventory().checkEquippedItem(420106);
				}
				if (_targetPc.getInventory().checkEquipped(420107)) {
					item = _targetPc.getInventory().checkEquippedItem(420107);
				}

				if (item.getEnchantLevel() == 7)
					basehp = 120;
				if (item.getEnchantLevel() == 8)
					basehp = 140;
				if (item.getEnchantLevel() == 9)
					basehp = 160;

				_targetPc.setCurrentHp(_targetPc.getCurrentHp() + basehp
						+ addhp);
				_targetPc
						.sendPackets(new S_SkillSound(_targetPc.getId(), 2187));
				Broadcaster.broadcastPacket(_targetPc, new S_SkillSound(
						_targetPc.getId(), 2187));
			} else if (_targetPc.getInventory().checkEquipped(21255)
					&& chance1 < 4) {
				_targetPc.setCurrentHp(_targetPc.getCurrentHp() + 31);
				_targetPc
						.sendPackets(new S_SkillSound(_targetPc.getId(), 2183));
				Broadcaster.broadcastPacket(_targetPc, new S_SkillSound(
						_targetPc.getId(), 2183));
			}
		}
		// 파푸가호

		/** 파번은 마방 공식 제외 (임시) */
		if (skillId != FINAL_BURN) {
			damage = calcMrDefense(damage);
		} else if (skillId == FINAL_BURN && _targetPc != null) { // final burn's
																	// temporary
																	// damage
			damage = _pc.getCurrentMp() / 2;

			/*
			 * if(_targetPc.getResistance().getEffectedMrBySkill() <= 50) damage
			 * = _pc.getCurrentMp() + _random.nextInt(_pc.getCurrentMp()/2+1);
			 * else if(_targetPc.getResistance().getEffectedMrBySkill() > 50 &&
			 * _targetPc.getResistance().getEffectedMrBySkill() < 100) damage =
			 * _pc.getCurrentMp() - _random.nextInt(_pc.getCurrentMp()/2+1);
			 * else if(_targetPc.getResistance().getEffectedMrBySkill() > 100)
			 * damage = _random.nextInt(_pc.getCurrentMp()/2+1);
			 */
		}

		// 피가 1에서 안달던 부분 수정.
		/*
		 * if (_calcType == PC_PC || _calcType == NPC_PC) { if (damage >
		 * _targetPc.getCurrentHp()) { damage = _targetPc.getCurrentHp(); } }
		 * else { if (damage > _targetNpc.getCurrentHp()) { damage =
		 * _targetNpc.getCurrentHp(); } }
		 */
		return damage;
	}

	private int calcTaitanDamage(int type) {
		int damage = 0;
		L1ItemInstance weapon = null;
		weapon = _targetPc.getWeapon();
		if (weapon != null) {
			// (큰 몬스터 타격치 + 추가 타격 옵션+ 인챈트 수치 ) x 2
			damage = (weapon.getItem().getDmgLarge() + weapon.getEnchantLevel() + weapon
					.getItem().getDmgModifier()) * 2;
		}
		return damage;
	}

	public int calcPcFireWallDamage() {
		int dmg = 0;
		double attrDeffence = calcAttrResistance(L1Skills.ATTR_FIRE);
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(FIRE_WALL);
		dmg = (int) ((1.0 - attrDeffence) * l1skills.getDamageValue());

		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(ABSOLUTE_BARRIER)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
						FREEZING_BREATH)
				|| _targetPc.getSkillEffectTimerSet()
						.hasSkillEffect(EARTH_BIND)
				|| _targetPc.getSkillEffectTimerSet()
						.hasSkillEffect(MOB_BASILL)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.STATUS_안전모드)) {
			dmg = 0;
		}

		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	public int calcNpcFireWallDamage() {
		int dmg = 0;
		double attrDeffence = calcAttrResistance(L1Skills.ATTR_FIRE);
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(FIRE_WALL);
		dmg = (int) ((1.0 - attrDeffence) * l1skills.getDamageValue());

		if (_targetNpc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
				|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
						FREEZING_BREATH)
				|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
						EARTH_BIND)
				|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
						MOB_BASILL)
				|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA)) {
			dmg = 0;
		}

		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	private int calcPcMagicDamage(int skillId) {
		int dmg = 0;
		if (skillId == FINAL_BURN) {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				dmg = _pc.getCurrentMp() / 2;
			} else {
				dmg = _pc.getCurrentMp() / 2;
			}
		} else {
			// if (_calcType == PC_PC) {
			dmg = calcMagicDiceDamage(skillId);
			dmg = (dmg * getLeverage()) / 10;
			// }else if (_calcType == NPC_PC) {
			// dmg = calcMagicDiceDamage(skillId);
			// dmg = (dmg * getLeverage()) / 10;
			// }
		}
		dmg -= _targetPc.getDamageReductionByArmor();

		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(SPECIAL_COOKING)) { // 스페셜요리에
																					// 의한
																					// 데미지
																					// 경감
			dmg -= 5;
		}
		
		/** 드래곤의 축복 대미지 감소 **/
		if (_targetPc._dragonbless_1) {
			dmg -= 2;
		} else if (_targetPc._dragonbless_2) {
			dmg -= 3;
		} else if (_targetPc._dragonbless_3) {
			dmg -= 4;
		}
		
		if (_targetPc.isAmorGaurd) { // 아머가드에의한 데미지감소
			int d = _targetPc.getAC().getAc() / 10;
			if (d < 0) {
				dmg += d;
			} else {
				dmg -= d;
			}
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(REDUCTION_ARMOR)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 50) {
				targetPcLvl = 50;
			}
			dmg -= (targetPcLvl - 50) / 5 + 1;
		}
		
		if (_targetPc.infinity_A) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 45) {
				targetPcLvl = 45;
			}
			int dmg2 = ((targetPcLvl- 41) / 4);
			dmg -= dmg2 > 0 ? dmg2 : 0;// +1
		}
		
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(마제스티)) {
			int targetPcLvl = _targetPc.getLevel();
			if (targetPcLvl < 80) {
				targetPcLvl = 80;
			}
			dmg -= (targetPcLvl - 80) + 3;
		}

		if (_calcType == NPC_PC) {
			boolean isNowWar = false;
			int castleId = L1CastleLocation.getCastleIdByArea(_targetPc);
			if (castleId > 0) {
				isNowWar = WarTimeController.getInstance().isNowWar(castleId);
			}
			if (!isNowWar) {
				if (_npc instanceof L1PetInstance) {
					dmg /= 8;
				}
				if (_npc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _npc;
					if (summon.isExsistMaster()) {
						dmg /= 8;
					}
				}
			}
			// Object[] dollList = _targetPc.getDollList().values().toArray();
			// // 마법 인형에 의한 추가 방어
			// L1DollInstance doll = null;
			for (L1DollInstance doll : _targetPc.getDollList()) {
				// doll = (L1DollInstance) dollObject;
				dmg -= doll.getDamageReductionByDoll();
			}

			// dmg -= dmg*0.05;
			// 몹 스킬 데미지 관련
		}

		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_AVATAR)) {
			dmg += dmg / 5;
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(PATIENCE)) {
			dmg -= 4;
		}
		if (_targetPc.드래곤스킨) {
			dmg -= 3;
		}
		if(_targetPc.드래곤스킨 && _targetPc.getLevel() >= 80) {
			int ddmg = _targetPc.getLevel() - 78;
			int i = (ddmg / 2) * 1;
			dmg -= 3 + i;
		
		}
		if (_targetPc.글로리어스) {
			if (_random.nextInt(100) < 2 + 8 - 5) {
				dmg -= 30;
				_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(), 19318));
				Broadcaster.broadcastPacket(_targetPc, new S_SkillSound(_targetPc.getId(), 19318));
			}
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(IMMUNE_TO_HARM)) {
			dmg -= (dmg*Config.뮨데미지);
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(루시퍼)) {
			dmg -= dmg / 10;
		}

		/*
		 * if (_calcType == PC_NPC) {//pc -> npc 데미지 하향 dmg -= dmg*0.3; }
		 */

		/*
		 * if (_targetPc != null) { int chance = _random.nextInt(100); if
		 * (_targetPc.getInventory().checkEquipped(420108) ||
		 * _targetPc.getInventory().checkEquipped(420109) ||
		 * _targetPc.getInventory().checkEquipped(420110) ||
		 * _targetPc.getInventory().checkEquipped(420111)) { // 린드비오르 3차 마갑주 if
		 * (chance <= 15){ // 20% 확률 dmg /= 2; _targetPc.sendPackets(new
		 * S_SystemMessage("린드비오르의 가호를 받았습니다.")); // _targetPc.sendPackets(new
		 * S_SkillSound(_targetPc.getId() , 2188));//임팩트 //
		 * _targetPc.broadcastPacket(new S_SkillSound(_targetPc.getId(), 2188));
		 * } } }
		 */

		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(MOB_BASILL)
				|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA)) {
			dmg = 0;
		}
		
		
		if (_calcType == PC_PC) {
			if (_pc.getMapId() == 5153) {
				if (_pc.get_DuelLine() == _targetPc.get_DuelLine() || _pc.get_DuelLine() == 0) {
					dmg = 0;
				}
			}
		}
		/*
		 * if(_calcType == PC_PC){ if(_targetPc.getLevel() <= Config.MAX_LEVEL
		 * || _pc.getLevel() <= Config.MAX_LEVEL){ //레벨65까지 if(_pc.getClanid()
		 * == 0 || _targetPc.getClanid() == 0){///무혈일경우 dmg = 0;
		 * _pc.sendPackets(new
		 * S_SystemMessage("\\fW혈맹이 없거나 레벨"+Config.MAX_LEVEL+
		 * "이하라 공격마법을 실패합니다.")); _targetPc.sendPackets(new
		 * S_SystemMessage("\\fW상대방의 마법공격을 보호받고 있습니다.")); } } }
		 */
       if(_targetPc.getWeapon() != null) {
		if (_targetPc.isTaitanM) {
			int hpRatio = 100;
			int TitanRatio = 41;
			int 라이징 = 5;
			if (0 < _targetPc.getMaxHp()) {
				hpRatio = 100 * _targetPc.getCurrentHp() / _targetPc.getMaxHp();
			}
			if(_targetPc.getWeapon().getItemId() == 30083 || _targetPc.getWeapon().getItemId() == 31083
					|| _targetPc.getWeapon().getItemId() == 222208 || _targetPc.getWeapon().getItemId() == 30092){
				TitanRatio += 5;
			}
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.라이징)){
				if(_targetPc.getLevel() > 80){
					라이징 += _targetPc.getLevel() - 80;
				}
				if(라이징 > 10){
					라이징 = 10;
				}
				TitanRatio += 라이징;
			}
			if(_targetPc.getSecondWeapon() != null){
			if((_targetPc.isSlayer && _targetPc.getSecondWeapon().getItemId() == 30083) || (_targetPc.isSlayer && _targetPc.getSecondWeapon().getItemId() == 31083)
					|| (_targetPc.isSlayer && _targetPc.getSecondWeapon().getItemId() == 222208) || (_targetPc.isSlayer && _targetPc.getSecondWeapon().getItemId() == 30092)){
				TitanRatio += 5;
			   }
			}
			if (hpRatio < TitanRatio) {
				int chan = _random.nextInt(100) + 1;
				boolean isProbability = false;
				if (_targetPc.getInventory().checkItem(41246, 10)) {
					if (30 > chan) {
						isProbability = true;
						_targetPc.getInventory().consumeItem(41246, 10);
					}
				}
				if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.SHOCK_STUN)
						|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.EARTH_BIND)
						|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.엠파이어)) {
					isProbability = false;
				}

				if (skillId == SHOCK_STUN || skillId == FOU_SLAYER
						|| skillId == TRIPLE_ARROW || skillId == 엠파이어) {
					isProbability = false;
				}

				if (isProbability) {
					if (_calcType == PC_PC) {
						_pc.sendPackets(new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
						Broadcaster.broadcastPacket(_pc, new S_DoActionGFX(_pc.getId(), ActionCodes.ACTION_Damage));
						_targetPc.sendPackets(new S_SkillSound(_targetPc
								.getId(), 12559));
						// Broadcaster.broadcastPacket(_targetPc, new
						// S_SkillSound(_targetPc.getId(), 12559));
						_pc.skillismiss = true;
						_pc.receiveDamage(_targetPc, calcTaitanDamage(0), false);
						dmg = 0;
					} else if (_calcType == NPC_PC) {
						int npcId = _npc.getNpcTemplate().get_npcId();
						if (npcId == 45681 || npcId == 45682 || npcId == 45683
								|| npcId == 45684) {
						} else if (!_npc.getNpcTemplate().get_IsErase()) {
						} else {
							Broadcaster.broadcastPacket(_npc,
									new S_DoActionGFX(_npc.getId(),
											ActionCodes.ACTION_Damage));
							_targetPc.sendPackets(new S_SkillSound(_targetPc
									.getId(), 12559));
							// Broadcaster.broadcastPacket(_targetPc, new
							// S_SkillSound(_targetPc.getId(), 12559));
							_npc.receiveDamage(_targetPc, calcTaitanDamage(0));
							dmg = 0;
						}
					}
				}
			}
		 }
		} else if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
				MORTAL_BODY)) {
			int chan = _random.nextInt(100) + 1;
			boolean isProbability = false;
			if (17 > chan) {
				isProbability = true;
			}
			if (isProbability) {
				if (_calcType == PC_PC) {
					_pc.sendPackets(new S_DoActionGFX(_pc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(_pc,
							new S_DoActionGFX(_pc.getId(),
									ActionCodes.ACTION_Damage));
					_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(),
							6519));
					Broadcaster.broadcastPacket(_targetPc, new S_SkillSound(
							_targetPc.getId(), 6519));
					_pc.receiveDamage(_targetPc, 40, false);
					// dmg = 0;
				} else if (_calcType == NPC_PC) {
					int npcId = _npc.getNpcTemplate().get_npcId();
					if (npcId == 45681 || npcId == 45682 || npcId == 45683
							|| npcId == 45684) {
					} else if (!_npc.getNpcTemplate().get_IsErase()) {
					} else {
						_npc.sendPackets(new S_DoActionGFX(_npc.getId(),
								ActionCodes.ACTION_Damage));
						Broadcaster.broadcastPacket(_npc, new S_DoActionGFX(
								_npc.getId(), ActionCodes.ACTION_Damage));
						_targetPc.sendPackets(new S_SkillSound(_targetPc
								.getId(), 6519));
						Broadcaster.broadcastPacket(_targetPc,
								new S_SkillSound(_targetPc.getId(), 6519));
						_npc.receiveDamage(_targetPc, 40);
						// dmg = 0;
					}
				}
			}
		} /*else if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
				COUNTER_MIRROR)) {
			if (_calcType == PC_PC) {
				if (_targetPc.getAbility().getTotalWis() >= _random
						.nextInt(100)) {
					_pc.sendPackets(new S_DoActionGFX(_pc.getId(),
							ActionCodes.ACTION_Damage));
					Broadcaster.broadcastPacket(_pc,
							new S_DoActionGFX(_pc.getId(),
									ActionCodes.ACTION_Damage));
					_targetPc.sendPackets(new S_SkillSound(_targetPc.getId(),
							4395));
					Broadcaster.broadcastPacket(_targetPc, new S_SkillSound(
							_targetPc.getId(), 4395));
					dmg = dmg / 2;
					_pc.receiveDamage(_targetPc, dmg, false);
					dmg = 0;
					_targetPc.getSkillEffectTimerSet().killSkillEffectTimer(
							COUNTER_MIRROR);
				}
			} else if (_calcType == NPC_PC) {
				int npcId = _npc.getNpcTemplate().get_npcId();
				if (npcId == 45681 || npcId == 45682 || npcId == 45683
						|| npcId == 45684) {
				} else if (!_npc.getNpcTemplate().get_IsErase()) {
				} else {
					if (_targetPc.getAbility().getTotalWis() >= _random
							.nextInt(100)) {
						Broadcaster.broadcastPacket(_npc, new S_DoActionGFX(
								_npc.getId(), ActionCodes.ACTION_Damage));
						_targetPc.sendPackets(new S_SkillSound(_targetPc
								.getId(), 4395));
						Broadcaster.broadcastPacket(_targetPc,
								new S_SkillSound(_targetPc.getId(), 4395));
						_npc.receiveDamage(_targetPc, dmg);
						dmg = 0;
						_targetPc.getSkillEffectTimerSet()
								.killSkillEffectTimer(COUNTER_MIRROR);
					}
				}
			}
		}*/
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(FEATHER_BUFF_A)) {
			dmg -= 3;
		}
		if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(FEATHER_BUFF_B)) {
			dmg -= 2;
		}
		
		if (_calcType == PC_PC) {
			if(_targetPc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_1) 
					 || _targetPc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_2)) {
					dmg -= 2;
				}
				if(_targetPc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_3)) {
						dmg -= 1;
					}
				if(_targetPc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_4)) {
					dmg -= 2;
				}
				if(_targetPc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_5)) {
					dmg -= 1;
				}
				if(_targetPc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_6)) {
					dmg -= 1;
				}
			if(_targetPc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.정상의가호))
				dmg -= 8;
		}
		
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(FAFU_MAAN) // 수룡의
																				// 마안
																				// -
																				// 마법데미지
																				// 50%감소
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							LIFE_MAAN) // 생명의 마안 - 마법데미지 50%감소
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							SHAPE_MAAN) // 형상의 마안 - 마법데미지 50%감소
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							BIRTH_MAAN)) { // 탄생의 마안 - 마법데미지 50%감소
				int MaanMagicCri = _random.nextInt(100) + 1;
				if (MaanMagicCri <= 35) { // 확률
					dmg /= 2;
				}
			}
		}

		if (dmg < 0) {
			dmg = 0;
		}
		return dmg;
	}

	private int calcNpcMagicDamage(int skillId) {
		int dmg = 0;
		if (skillId == FINAL_BURN) {
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				dmg = _pc.getCurrentMp() / 2;
			} else {
				dmg = _pc.getCurrentMp() / 2;
			}
		} else {
			dmg = calcMagicDiceDamage(skillId);
			dmg = (dmg * getLeverage()) / 10;
		}
		if (_targetNpc.getNpcId() == 45640) {
			dmg /= 2;
		}
		/** 발라카스 레이드 리뉴얼 ( 할파스의 권속 ) **/
		if (_calcType == PC_NPC && _targetNpc != null) {
			if (_targetNpc.getNpcId() == 3310033) {
				// 할파스의 권속은 데스나이트 버프가 있어야만 처리 가능
				if (!_pc.getSkillEffectTimerSet().hasSkillEffect(15837)) {
					dmg = 0;
					return 0;
				}
			}
		}
		if (_calcType == PC_NPC) {
			boolean isNowWar = false;
			int castleId = L1CastleLocation.getCastleIdByArea(_targetNpc);
			if (castleId > 0) {
				isNowWar = WarTimeController.getInstance().isNowWar(castleId);
			}
			if (!isNowWar) {
				if (_targetNpc instanceof L1PetInstance) {
					dmg /= 8;
				}
				if (_targetNpc instanceof L1SummonInstance) {
					L1SummonInstance summon = (L1SummonInstance) _targetNpc;
					if (summon.isExsistMaster()) {
						dmg /= 8;
					}
				}
			}
		}

		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(FAFU_MAAN) // 수룡의
																				// 마안
																				// -
																				// 마법데미지
																				// 50%감소
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							LIFE_MAAN) // 생명의 마안 - 마법데미지 50%감소
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							SHAPE_MAAN) // 형상의 마안 - 마법데미지 50%감소
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							BIRTH_MAAN)) { // 탄생의 마안 - 마법데미지 50%감소
				int MaanMagicCri = _random.nextInt(100) + 1;
				if (MaanMagicCri <= 35) { // 확률
					dmg /= 2;
				}
			}
		}

		if (_calcType == PC_NPC && _targetNpc != null) {
			int npcId = _targetNpc.getNpcTemplate().get_npcId();
			if (npcId >= 45912
					&& npcId <= 45915
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_WATER)) {
				dmg = 0;
			}
			if (npcId >= 145685 && npcId <= 145686  && !_pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.할파스권속버프)) {
				dmg = 0;
			}
			if (npcId == 45916
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_MITHRIL_POWDER)) {
				dmg = 0;
			}
			if (npcId == 45941
					&& !_pc.getSkillEffectTimerSet().hasSkillEffect(
							STATUS_HOLY_WATER_OF_EVA)) {
				dmg = 0;
			}
			if (!_pc.getSkillEffectTimerSet().hasSkillEffect(
					STATUS_CURSE_BARLOG)
					&& (npcId == 445752 || npcId == 445753)) { //발록버프 해제
				dmg = 0;
			}
			if (!_pc.getSkillEffectTimerSet() //야히 버프없이 때리게 가능
					.hasSkillEffect(STATUS_CURSE_YAHEE)
					&& (npcId == 45675111 || npcId == 81082111 || npcId == 45625111
							|| npcId == 45674111 || npcId == 45685111)) {
				dmg = 0;
			}
			if (npcId >= 46068 && npcId <= 46091
					&& _pc.getGfxId().getTempCharGfx() == 6035) {
				dmg = 0;
			}
			if (npcId >= 46092 && npcId <= 46106
					&& _pc.getGfxId().getTempCharGfx() == 6034) {
				dmg = 0;
			}

			if (dmg != 0 && _targetNpc.getNpcTemplate().is__MagicBarrier()) {
				int mbrnd = _random.nextInt(100);
				if (mbrnd == 1) {
					_pc.receiveDamage(_targetNpc, dmg, 0);
				}
			}
			// System.out.println("pc -> npc 정상 데미지 : "+dmg);
			dmg -= dmg * 0.3;
			// System.out.println("pc -> npc 30%하향후   데미지 : "+dmg);
		}

		return dmg;
	}

	private int calcMagicDiceDamage(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int diceCount = l1skills.getDamageDiceCount();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;
		int charaIntelligence = 0;
		Random random = new Random();

		for (int i = 0; i < diceCount; i++) {
			magicDamage += (_random.nextInt(dice) + 1);
		}
		magicDamage += value;

		// 크리 50% 증가 10%확률
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			if (skillId == L1SkillId.DISINTEGRATE
					|| skillId == L1SkillId.SUNBURST
					|| skillId == L1SkillId.ERUPTION
					|| skillId == L1SkillId.ETERNITY
					|| skillId == L1SkillId.CONE_OF_COLD
					|| skillId == L1SkillId.CALL_LIGHTNING) {
				int crirnd = 12;
				crirnd += CalcStat.마법치명타(_pc.getAbility().getTotalInt());
				if (_random.nextInt(100) < crirnd) {
					_pc.skillCritical = true;
					magicDamage *= 0.8;
				}
			}
		}

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int weaponAddDmg = 0;
			L1ItemInstance weapon = _pc.getWeapon();
			if (weapon != null) {
				weaponAddDmg = weapon.getItem().getMagicDmgModifier();
			}
			magicDamage += weaponAddDmg;
			magicDamage += random.nextInt(_pc.ability.getInt()) * 0.5;
		}

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			// int spByItem = _pc.getAbility().getSp() -
			// _pc.getAbility().getTrueSp();
			charaIntelligence = _pc.getAbility().getSp();
		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			int spByItem = _npc.getAbility().getSp()
					- _npc.getAbility().getTrueSp();
			charaIntelligence = _npc.getAbility().getTotalInt() + spByItem - 12;
		}
		if (charaIntelligence < 1) {
			charaIntelligence = 1;
		}

		double attrDeffence = calcAttrResistance(l1skills.getAttr());

		/*
		 * double coefficient = (1.0 - attrDeffence + charaIntelligence * 3.2 /
		 * 32.0); if (coefficient < 0) { coefficient = 0; }
		 */
		double coefficient = (charaIntelligence * 3.2 / 32.0);
		if (coefficient < 0) {
			coefficient = 0;
		}

		magicDamage *= coefficient;

		magicDamage -= magicDamage * attrDeffence;

		/** 치명타 발생 부분 추가 - By 시니 - */

		double criticalCoefficient = 1.5;
		int rnd = random.nextInt(100) + 1;
		if (_calcType == PC_PC || _calcType == PC_NPC) {
			int propCritical = CalcStat.마법치명타(_pc.ability.getTotalInt());
			if (criticalOccur(propCritical)) {
				magicDamage *= 1.5;
			}
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				if (_pc.getSkillEffectTimerSet().hasSkillEffect(LIND_MAAN) // 풍룡의
																			// 마안
																			// -
																			// 일정확률로
																			// 마법치명타+1
						|| _pc.getSkillEffectTimerSet().hasSkillEffect(
								SHAPE_MAAN) // 형상의 마안 - 일정확률로 마법치명타+1
						|| _pc.getSkillEffectTimerSet().hasSkillEffect(
								LIFE_MAAN)) { // 생명의 마안 - 일정확률로 마법치명타+1
					int MaanMagicCri = _random.nextInt(100) + 1;
					if (MaanMagicCri <= 20) { // 확률
						magicDamage *= 1.5;
					}
				}
			}
			/** 마법인형 장로 **/
			/*
			 * for (L1DollInstance doll : _pc.getDollList().values()) { // 피씨
			 * 자신이 인형을 가지고 있다면 magicDamage += doll.getMagicDamageByDoll(); // 인형
			 * 대미지를 줌..<이전페이지>가지고 있었을경우만 해당 // 15정도 대미지를 위에 += 플러스 시킴 }
			 */
			/** 마법인형 장로 **/

		} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
			if (rnd <= 15) {
				magicDamage *= criticalCoefficient;
			}
		}

		if (_calcType == PC_PC || _calcType == PC_NPC) {
			magicDamage += _pc.getBaseMagicDmg();
		}
		

		if (_calcType == PC_PC) {
			if( _pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_2) ||
					_pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_1)) {
				magicDamage += 2;
			}
			if( _pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_3)) {
				magicDamage += 1;
			}
		}
		return magicDamage;
	}

	public int calcHealing(int skillId) {
		L1Skills l1skills = SkillsTable.getInstance().getTemplate(skillId);
		int dice = l1skills.getDamageDice();
		int value = l1skills.getDamageValue();
		int magicDamage = 0;

		if (skillId != NATURES_BLESSING) {
			int magicBonus = getMagicBonus();
			if (magicBonus > 10) {
				magicBonus = 10;
			}

			int diceCount = value + magicBonus;
			for (int i = 0; i < diceCount; i++) {
				magicDamage += (_random.nextInt(dice) + 1);
			}
		} else {
			int Int = 0;
			if (_calcType == PC_PC || _calcType == PC_NPC) {
				Int = _pc.getAbility().getTotalInt();
			} else if (_calcType == NPC_PC || _calcType == NPC_NPC) {
				Int = _npc.getAbility().getTotalInt();
			}
			if (Int < 12)
				Int = 12;
			for (int i = 12; i <= Int; i++) {
				if (i == 12)
					magicDamage += (100 + _random.nextInt(80));
				else if (i >= 13 && i <= 17)
					magicDamage += (3 + _random.nextInt(2));
				else if (i >= 18 && i <= 25)
					magicDamage += (10 + _random.nextInt(6));
				else if (i >= 26)
					magicDamage += (1 + _random.nextInt(2));
			}
			magicDamage /= 2.2;
		}

		double alignmentRevision = 1.0;
		if (getLawful() > 0) {
			alignmentRevision += (getLawful() / 32768.0);
		}

		magicDamage *= alignmentRevision;

		if (skillId != NATURES_BLESSING)
			magicDamage = (magicDamage * getLeverage()) / 10;

		return magicDamage;
	}

	/**
	 * MR에 의한 마법 데미지 감소를 처리 한다 수정일자 : 2009.04.15 수정자 : 손영신
	 * 
	 * @param dmg
	 * @return dmg
	 */

	public int calcMrDefense(int dmg) {

		int MagicResistance = 0; // 마법저항
		int RealMagicResistance = 0; // 적용되는 마법저항값
		double calMr = 0.00D; // 마방계산
		double baseMr = 0.00D;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			MagicResistance = _targetPc.getResistance().getEffectedMrBySkill();
			if (MagicResistance > 221) {
				MagicResistance = 221;
			}
		} else {
			MagicResistance = _targetNpc.getResistance().getEffectedMrBySkill();
			if (MagicResistance > 221) {
				MagicResistance = 221;
			}
		}

		/*
		 * RealMagicResistance = MagicResistance - _random.nextInt(5) + 1;
		 * 
		 * 
		 * if(_calcType == PC_PC){ baseMr = (_random.nextInt(1000) + 98000) /
		 * 100000D;
		 * 
		 * if(MagicResistance <= 100 ){ calMr = baseMr - (MagicResistance *
		 * 470)/ 100000D; } else if (MagicResistance>100) { calMr = baseMr -
		 * (MagicResistance * 470)/ 100000D + ((MagicResistance - 100) * 0.004);
		 * } }else{ if(RealMagicResistance > 150){ RealMagicResistance = 150; }
		 * calMr = (200 - RealMagicResistance) / 250.00D;
		 * //System.out.println("데미지 :"+dmg+" 감소율 :"+calMr); }
		 */
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

		// System.out.println("적용데미지 :"+dmg);
		if (dmg < 0) {
			dmg = 0;
		}

		return dmg;
	}

	private boolean criticalOccur(int prop) {
		boolean ok = false;
		int num = _random.nextInt(100) + 1;

		if (prop == 0) {
			return false;
		}
		if (num <= prop) {
			ok = true;
		}
		return ok;
	}

	private double calcAttrResistance(int attr) {
		int resist = 0;
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			switch (attr) {
			case L1Skills.ATTR_EARTH:
				resist = _targetPc.getResistance().getEarth();
				break;
			case L1Skills.ATTR_FIRE:
				resist = _targetPc.getResistance().getFire();
				break;
			case L1Skills.ATTR_WATER:
				resist = _targetPc.getResistance().getWater();
				break;
			case L1Skills.ATTR_WIND:
				resist = _targetPc.getResistance().getWind();
				break;
			}
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			// 취약속성 데미지 10% 증가
			int npc_att = _targetNpc.getNpcTemplate().get_weakAttr();
			if (npc_att == 0)
				return 0;
			if (npc_att >= 8) {
				npc_att -= 8;
				if (attr == 8)
					return -0.2;
			}
			if (npc_att >= 4) {
				npc_att -= 4;
				if (attr == 4)
					return -0.2;
			}
			if (npc_att >= 2) {
				npc_att -= 2;
				if (attr == 2)
					return -0.2;
			}
			if (npc_att >= 1) {
				npc_att -= 1;
				if (attr == 1)
					return -0.2;
			}
			return 0;
		}

		/*
		 * int resistFloor = (int) (0.32 * Math.abs(resist)); if (resist >= 0) {
		 * resistFloor *= 1; } else { resistFloor *= -1; }
		 * 
		 * double attrDeffence = resistFloor / 32.0;
		 */

		// double attrDeffence = resist / 4 * 0.01;
		// double attrDeffence = resist * 0.3 * 0.01;
		double attrDeffence = 0;
		if (resist < 10) {
			attrDeffence = 0.01;
		} else if (resist < 20) {
			attrDeffence = 0.02;
		} else if (resist < 30) {
			attrDeffence = 0.03;
		} else if (resist < 40) {
			attrDeffence = 0.04;
		} else if (resist < 50) {
			attrDeffence = 0.05;
		} else if (resist < 60) {
			attrDeffence = 0.06;
		} else if (resist < 70) {
			attrDeffence = 0.1;
		} else if (resist < 80) {
			attrDeffence = 0.15;
		} else if (resist < 90) {
			attrDeffence = 0.20;
		} else if (resist < 100) {
			attrDeffence = 0.25;
		} else {
			attrDeffence = 0.30;
		}
		return attrDeffence;
	}

	public void commit(int damage, int drainMana, int skillid) {
		if (_calcType == PC_PC || _calcType == NPC_PC) {
			if (skillid == L1SkillId.MANA_DRAIN) {
				commitPc(damage, drainMana, true);
			} else {
				commitPc(damage, drainMana, false);
			}
		} else if (_calcType == PC_NPC || _calcType == NPC_NPC) {
			if (_calcType == PC_NPC) {
				if (damage > 0)
					damage = (int) (damage * 1.40);
			}
			if (skillid == L1SkillId.MANA_DRAIN) {
				commitNpc(damage, drainMana, true);
			} else {
				commitNpc(damage, drainMana, false);
			}

		}

		if (!Config.ALT_ATKMSG) {
			return;
		}

		if (_targetPc == null && _targetNpc == null)
			return;
		if (_pc != null && _pc.isGm()) {
			_pc.sendPackets(new S_SystemMessage("\\fT["
					+ _pc.getName()
					+ "] ==> ["
					+ (_targetPc == null ? _targetNpc.getName() : _targetPc
							.getName())
					+ "][== "
					+ damage
					+ " MAG ==][HP "
					+ (_targetPc == null ? _targetNpc.getCurrentHp()
							: _targetPc.getCurrentHp()) + "]"));
		}
		if (_targetPc != null && _targetPc.isGm()) {
			_targetPc
					.sendPackets(new S_SystemMessage("\\fY["
							+ _targetPc.getName() + "] <== ["
							+ (_pc == null ? _npc.getName() : _pc.getName())
							+ "][== " + damage + " MAG ==][HP "
							+ _targetPc.getCurrentHp() + "]"));
		}
		/*
		 * if (Config.ALT_ATKMSG) { if ((_calcType == PC_PC || _calcType ==
		 * PC_NPC) && !_pc.isGm()) { return; } if ((_calcType == PC_PC ||
		 * _calcType == NPC_PC) && !_targetPc.isGm()) { return; } }
		 * 
		 * String msg0 = ""; String msg1 = "왜"; String msg2 = ""; String msg3 =
		 * ""; String msg4 = "";
		 * 
		 * if (_calcType == PC_PC || _calcType == PC_NPC) { msg0 =
		 * _pc.getName(); } else if (_calcType == NPC_PC) { msg0 =
		 * _npc.getName(); }
		 * 
		 * if (_calcType == NPC_PC || _calcType == PC_PC) { msg4 =
		 * _targetPc.getName(); msg2 = "THP" + _targetPc.getCurrentHp(); } else
		 * if (_calcType == PC_NPC) { msg4 = _targetNpc.getName(); msg2 = "THp"
		 * + _targetNpc.getCurrentHp(); }
		 * 
		 * msg3 = damage + "주었다";
		 * 
		 * if (_calcType == PC_PC || _calcType == PC_NPC) { _pc.sendPackets(new
		 * S_ServerMessage(166, msg0, msg1, msg2, msg3, msg4)); } if (_calcType
		 * == NPC_PC || _calcType == PC_PC) { _targetPc.sendPackets(new
		 * S_ServerMessage(166, msg0, msg1, msg2, msg3, msg4)); }
		 */
	}

	private void commitPc(int damage, int drainMana, boolean ismanadrain) {
		if (_calcType == PC_PC) {
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(
					ABSOLUTE_BARRIER)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							ICE_LANCE)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							FREEZING_BREATH)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							MOB_BASILL)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							MOB_COCA)
					|| _targetPc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.STATUS_안전모드)) {
				damage = 0;
				drainMana = 0;
			}
			if (_targetPc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)
					&& damage >= 0) {
				damage = 0;
				drainMana = 0;
			}
			if (drainMana > 0 && _targetPc.getCurrentMp() > 0) {
				int newMp = 0;

				if (drainMana > _targetPc.getCurrentMp()) {
					drainMana = _targetPc.getCurrentMp();
				}

				if (ismanadrain) {
					newMp = _pc.getCurrentMp() + (drainMana / 2);
				} else {
					newMp = _pc.getCurrentMp() + drainMana;
				}

				_pc.setCurrentMp(newMp);
			}
			_targetPc.receiveManaDamage(_pc, drainMana);
			_targetPc.receiveDamage(_pc, damage, true);
		} else if (_calcType == NPC_PC) {
			/*
			 * if(_npc.getNpcId()== 45338 ||_npc.getNpcId()== 45456
			 * ||_npc.getNpcId()== 45458 ||_npc.getNpcId()== 45488
			 * ||_npc.getNpcId()== 45534 ||_npc.getNpcId()== 45516
			 * ||_npc.getNpcId()== 45529 ||_npc.getNpcId()== 45529
			 * ||_npc.getNpcId()== 45535 ||_npc.getNpcId()== 45545
			 * 
			 * ||_npc.getNpcId()== 45546 ||_npc.getNpcId()== 45573
			 * ||_npc.getNpcId()== 45583 ||_npc.getNpcId()== 45584
			 * ||_npc.getNpcId()== 45600 ||_npc.getNpcId()== 45601
			 * ||_npc.getNpcId()== 45609 ||_npc.getNpcId()== 45610
			 * ||_npc.getNpcId()== 45614 ||_npc.getNpcId()== 45617
			 * ||_npc.getNpcId()== 45625 ||_npc.getNpcId()== 45640
			 * ||_npc.getNpcId()== 45640 ||_npc.getNpcId()== 45640
			 * ||_npc.getNpcId()== 45642 ||_npc.getNpcId()== 45643
			 * ||_npc.getNpcId()== 45644 ||_npc.getNpcId()== 45645
			 * ||_npc.getNpcId()== 45642 ||_npc.getNpcId()== 45643
			 * ||_npc.getNpcId()== 45644 ||_npc.getNpcId()== 45645
			 * ||_npc.getNpcId()== 45642 ||_npc.getNpcId()== 45643
			 * ||_npc.getNpcId()== 45644 ||_npc.getNpcId()== 45645
			 * ||_npc.getNpcId()== 45646 ||_npc.getNpcId()== 45649
			 * ||_npc.getNpcId()== 45651 ||_npc.getNpcId()== 45671
			 * ||_npc.getNpcId()== 45674 ||_npc.getNpcId()== 45675
			 * ||_npc.getNpcId()== 45680 ||_npc.getNpcId()== 45681
			 * ||_npc.getNpcId()== 45684 ||_npc.getNpcId()== 45685
			 * ||_npc.getNpcId()== 45734 ||_npc.getNpcId()== 45735
			 * ||_npc.getNpcId()== 45752 ||_npc.getNpcId()== 45772
			 * ||_npc.getNpcId()== 45795 ||_npc.getNpcId()== 45801
			 * ||_npc.getNpcId()== 45802 ||_npc.getNpcId()== 45829
			 * ||_npc.getNpcId()== 45548 ||_npc.getNpcId()== 46024
			 * ||_npc.getNpcId()== 46025 ||_npc.getNpcId()== 46026
			 * ||_npc.getNpcId()== 46037 ||_npc.getNpcId()== 45935
			 * ||_npc.getNpcId()== 45942 ||_npc.getNpcId()== 45941
			 * ||_npc.getNpcId()== 45931 ||_npc.getNpcId()== 45943
			 * ||_npc.getNpcId()== 45944 ||_npc.getNpcId()== 45492
			 * ||_npc.getNpcId()== 46141 ||_npc.getNpcId()== 46142
			 * ||_npc.getNpcId()== 4037000 ||_npc.getNpcId()== 4037000
			 * ||_npc.getNpcId()== 81163 ||_npc.getNpcId()== 45513
			 * ||_npc.getNpcId()== 45547 ||_npc.getNpcId()== 45606
			 * ||_npc.getNpcId()== 45650 ||_npc.getNpcId()== 45652
			 * ||_npc.getNpcId()== 45653 ||_npc.getNpcId()== 45654
			 * ||_npc.getNpcId()== 45618 ||_npc.getNpcId()== 45672
			 * ||_npc.getNpcId()== 45673){ }else{ //damage -= damage*0.15;
			 * damage -= damage*0.20; }
			 */
			damage -= damage * 0.50;
			_targetPc.receiveDamage(_npc, damage, true);
		}
	}

	private void commitNpc(int damage, int drainMana, boolean ismanadrain) {
		if (_calcType == PC_NPC) {
			if (_targetNpc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
					|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							FREEZING_BREATH)
					|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							EARTH_BIND)
					|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							MOB_BASILL)
					|| _targetNpc.getSkillEffectTimerSet().hasSkillEffect(
							MOB_COCA)) {
				damage = 0;
				drainMana = 0;
			}

			if (_targetNpc instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) _targetNpc;
				if (mon.kir_counter_magic) {
					_pc.receiveDamage(_targetNpc, damage * 2, true);
					drainMana = 0;
					damage = 0;
				} else if (mon.kir_absolute) {
					damage = 0;
					drainMana = 0;
				}
			}
			if (drainMana > 0) {
				int drainValue = _targetNpc.drainMana(drainMana);

				if (ismanadrain) {
					drainValue /= 2;
				}

				int newMp = _pc.getCurrentMp() + drainValue;
				_pc.setCurrentMp(newMp);
			}
			_targetNpc.ReceiveManaDamage(_pc, drainMana);
			_targetNpc.receiveDamage(_pc, damage);
		} else if (_calcType == NPC_NPC) {
			_targetNpc.receiveDamage(_npc, damage);
		}
	}
}
