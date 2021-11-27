package l1j.server.server.model.skill;

import static l1j.server.server.model.skill.L1SkillId.AREA_OF_SILENCE;
import static l1j.server.server.model.skill.L1SkillId.CHILL_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.CONFUSION;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.DISINTEGRATE;
import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.FINAL_BURN;
import static l1j.server.server.model.skill.L1SkillId.FOU_SLAYER;
import static l1j.server.server.model.skill.L1SkillId.HEAL;
import static l1j.server.server.model.skill.L1SkillId.ICE_SPIKE;
import static l1j.server.server.model.skill.L1SkillId.SILENCE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_POISON_SILENCE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_SPOT1;
import static l1j.server.server.model.skill.L1SkillId.STATUS_SPOT2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_SPOT3;
import static l1j.server.server.model.skill.L1SkillId.STATUS_SPOT4;
import static l1j.server.server.model.skill.L1SkillId.TRIPLE_ARROW;
import static l1j.server.server.model.skill.L1SkillId.VAMPIRIC_TOUCH;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CalcStat;

public class AttackSkill {

	private static Logger _log = Logger.getLogger(AttackSkill.class.getName());

	private L1Skills _skill;
	private int _skillId;
	private int _targetID;
	private int _mpConsume = 0;
	private int _hpConsume = 0;
	private int _targetX = 0;
	private int _targetY = 0;
	private L1PcInstance _player = null;
	private L1PcInstance _pc = null;
	private boolean _isCounterMagic = true;

	private L1Character _target = null;

	private int _calcType;
	private static final int PC_PC = 1;
	private static final int PC_NPC = 2;

	public AttackSkill(L1PcInstance player, int skillId, int targetId, int x, int y) {
		_skill = SkillsTable.getInstance().getTemplate(skillId);
		_skillId = skillId;
		_targetX = x;
		_targetY = y;
		_player = player;
		_targetID = targetId;
		L1Object l1object = L1World.getInstance().findObject(_targetID);
		if (!(l1object instanceof L1Character)) {
			return;
		}
		_target = (L1Character) l1object;

		if (_target instanceof L1PcInstance) {
			_calcType = PC_PC;
		} else if (_target instanceof L1NpcInstance) {
			_calcType = PC_NPC;
		}

		// 존재버그 관련 추가
		if (_player instanceof L1PcInstance) {
			L1PcInstance jonje = L1World.getInstance().getPlayer(_player.getName());
			if (jonje == null && player.getAccessLevel() != Config.GMCODE) {
				player.sendPackets(new S_SystemMessage("존재버그 강제종료! 재접속하세요"));
				player.sendPackets(new S_Disconnect());
				return;
			}
		}
		if (isSkillUsable()) {
			RunSkill();
			useConsume();
			// setDelay();
		}
	}

	private boolean isSkillUsable() {
		if (_player.isInvisble() || _player.isInvisDelay()) {
			return false;
		}
		if (_player.getInventory().calcWeightpercent() >= 83) { // 중량 오버이면 스킬을
																// 사용할 수 없다
			_player.sendPackets(new S_ServerMessage(316));
			return false;
		}
		int polyId = _player.getGfxId().getTempCharGfx();
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
		if (poly != null && !poly.canUseSkill()) {
			_player.sendPackets(new S_ServerMessage(285));
			return false;
		}
		if (_player.isSkillDelay()) {
			return false;
		}
		if (_player.getSkillEffectTimerSet().hasSkillEffect(SILENCE) || _player.getSkillEffectTimerSet().hasSkillEffect(AREA_OF_SILENCE)
				|| _player.getSkillEffectTimerSet().hasSkillEffect(STATUS_POISON_SILENCE) || _player.getSkillEffectTimerSet().hasSkillEffect(CONFUSION)) {
			_player.sendPackets(new S_ServerMessage(285));
			return false;
		}
		if (_skillId == DISINTEGRATE && _player.getLawful() < 500) {
			_player.sendPackets(new S_ServerMessage(352, "$967"));
			return false;
		}
		if (_target instanceof L1PcInstance) {
			L1PcInstance tpc = (L1PcInstance) _target;
			if (_skillId == L1SkillId.데스페라도 || _skillId == L1SkillId.SHOCK_STUN || _skillId == L1SkillId.FORCE_STUN || _skillId == L1SkillId.엠파이어
					|| _skillId == L1SkillId.PHANTOM) {
				if (tpc.speed_time_temp > System.currentTimeMillis()) {
					if (_player.getLocation().getTileLineDistance(_target.getLocation()) > 2) {
						// _player.sendPackets(new
						// S_SystemMessage("이동중 무빙 어택이지만 3칸이상으로 판단 마법실패"));
						return false;
					}
				} else {
					if (_player.getLocation().getTileLineDistance(_target.getLocation()) > _skill.getRanged()) {
						// _player.sendPackets(new
						// S_SystemMessage("2칸이상 판단으로 마법실패"));
						return false;
					}
				}
			} else {
				if (_player.getLocation().getTileLineDistance(_target.getLocation()) > _skill.getRanged()) {
					// _player.sendPackets(new
					// S_SystemMessage("2칸이상 판단으로 마법실패"));
					return false;
				}
			}
		} else {
			if (_player.getLocation().getTileLineDistance(_target.getLocation()) > _skill.getRanged()) {
				// _player.sendPackets(new S_SystemMessage("2칸이상 판단으로 마법실패"));
				return false;
			}
		}

		if (!CharPosUtil.isAreaAttack(_player, _target.getX(), _target.getY(), _target.getMapId())) {
			return false;
		}
		if (!CharPosUtil.isAreaAttack(_target, _player.getX(), _player.getY(), _player.getMapId())) {
			return false;
		}
		if (!isHPMPConsume()) {
			return false;
		}
		return true;
	}

	private boolean isHPMPConsume() {
		int maxminusMP = _skill.getSkillLevel() - 1;
		int minusMP = 0;
		_mpConsume = _skill.getMpConsume();
		_hpConsume = _skill.getHpConsume();
		int currentMp = 0;
		int currentHp = 0;
		currentMp = _player.getCurrentMp();
		currentHp = _player.getCurrentHp();

		if (_skillId >= HEAL && _skillId <= ICE_SPIKE) {
			/*
			 * if (_player.getBaseMagicDecreaseMp() > 0){ minusMP -=
			 * _player.getBaseMagicDecreaseMp(); }
			 */
			int maxConsume = _skill.getMpConsume();

			double balance = CalcStat.마법보너스(_player.getAbility().getTotalInt()) * 0.01;

			minusMP = (int) (maxConsume * balance);

			if (minusMP > maxminusMP)
				minusMP = maxminusMP;

			if (minusMP > 0)
				_mpConsume -= minusMP;

			if (0 < _skill.getMpConsume()) {
				_mpConsume = Math.max(_mpConsume, 1);
			}
		}

		if (currentHp < _hpConsume + 1) {
			_player.sendPackets(new S_ServerMessage(279));
			return false;
		} else if (currentMp < _mpConsume) {
			_player.sendPackets(new S_ServerMessage(278));
			return false;
		}
		return true;
	}

	private void useConsume() {
		if (_skillId == FINAL_BURN) {
			_player.setCurrentHp(1);
			_player.setCurrentMp(0);
		} else {
			int current_hp = _player.getCurrentHp() - _hpConsume;
			_player.setCurrentHp(current_hp);

			int current_mp = _player.getCurrentMp() - _mpConsume;
			_player.setCurrentMp(current_mp);
		}

		int lawful = _player.getLawful() + _skill.getLawful();
		if (lawful > 32767) {
			lawful = 32767;
		}
		if (lawful < -32767) {
			lawful = -32767;
		}
		_player.setLawful(lawful);
	}

	private void setDelay() {
		if (_skill.getReuseDelay() > 0) {
			// L1SkillDelay.onSkillUse(_player, _skill.getReuseDelay());
			_player.setSkillDelay(true);
			GeneralThreadPool.getInstance().schedule(new L1SkillDelay(_player, _skill.getReuseDelay()), _skill.getReuseDelay());
		}
	}

	private boolean isPcSummonPet(L1Character cha) {
		if (_calcType == PC_PC) {
			return true;
		}

		if (_calcType == PC_NPC) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (summon.isExsistMaster()) {
					return true;
				}
			}
			if (cha instanceof L1PetInstance) {
				return true;
			}
		}
		return false;
	}

	private boolean isUseCounterMagic(L1Character cha) {
		if (_isCounterMagic && cha.getSkillEffectTimerSet().hasSkillEffect(COUNTER_MAGIC)) {
			cha.getSkillEffectTimerSet().removeSkillEffect(COUNTER_MAGIC);
			int castgfx = SkillsTable.getInstance().getTemplate(COUNTER_MAGIC).getCastGfx();
			Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), castgfx));
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), castgfx));
			}
			return true;
		}
		return false;
	}

	public void RunSkill() {
		try {
			int actionId = _skill.getActionId();
			int castgfx = _skill.getCastGfx();
			if (castgfx == 0) {
				return;
			}

			if (isPcSummonPet(_target)) {
				if (CharPosUtil.getZoneType(_player) == 1 || CharPosUtil.getZoneType(_target) == 1 || _player.checkNonPvP(_player, _target)) {
					_player.sendPackets(new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId));
					Broadcaster.broadcastPacket(_player, new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId));
					return;
				}
			}

			_player.sendPackets(new S_UseAttackSkill(_player, _targetID, castgfx, _targetX, _targetY, actionId));
			Broadcaster.broadcastPacket(_player, new S_UseAttackSkill(_player, _targetID, castgfx, _targetX, _targetY, actionId));
			Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(_targetID, ActionCodes.ACTION_Damage), _player);

			if (_skillId == 132 || _skillId == 187) {
				_isCounterMagic = false;
			}
			if (isUseCounterMagic(_target)) {
				return;
			}

			int dmg = 0;
			int drainMana = 0;
			int heal = 0;

			L1Magic _magic = new L1Magic(_player, _target);
			_magic.setLeverage(10);

			dmg = _magic.calcMagicDamage(_skillId);
			// _player.sendPackets(new S_SystemMessage("마법 데미지 :"+dmg));
			// 공격 스킬일때!! 이레이즈 여부 판멸후 제거
			if (_skill.getSkillId() != L1SkillId.TRIPLE_ARROW && _skill.getSkillId() != L1SkillId.FOU_SLAYER) {
				if (_target.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC)) {
					_target.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
				}
			}

			switch (_skillId) {
			case CHILL_TOUCH:
			case VAMPIRIC_TOUCH:
				heal = dmg;
				break;
			case TRIPLE_ARROW: { // 수정
				boolean gfxcheck = false;
				int[] BowGFX = { 138, 37, 3860, 3126, 3420, 2284, 3105, 3145, 3148, 3151, 3871, 4125, 2323, 3892, 3895, 3898, 3901, 4917, 4918, 4919, 4950,
						6140, 6145, 6150, 6155, 6269, 6272, 6275, 6278, 6826, 6827, 6836, 6837, 6846, 6847, 6856, 6857, 6866, 6867, 6876, 6877, 6886, 6887,
						6400, 5645, 6399, 7039, 7040, 7041, 7140, 7144, 7148, 6160, 7152, 7156, 7160, 7164, 7139, 7143, 7147, 7151, 7155, 7159, 7163, 7959,
						7967, 7969, 7970, 9214, 8900, 8913, 9225, 9226, 8804, 8808, 10870, 10869, 10871, 13388, 13380, 8792, 8798, 8786, 8860, 13346, 9615,
						14927, 12314, 15848, 15814, 15528, 15830, 15831, 16002, 16074, 17535 };

				int playerGFX = _player.getGfxId().getTempCharGfx();
				for (int gfx : BowGFX) {
					if (playerGFX == gfx) {
						gfxcheck = true;
						break;
					}
				}
				if (!gfxcheck) {
					return;
				}

				for (int i = 3; i > 0; i--) {
					_target.onAction(_player);
				}
				_player.sendPackets(new S_SkillSound(_player.getId(), 4394));
				Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 4394));
			}
				break;

			case FOU_SLAYER: {
				boolean gfxcheck = false;
				int[] FouGFX = { 138, 37, 3860, 3126, 3420, 2284, 3105, 3145, 3148, 3151, 3871, 4125, 2323, 3892, 3895, 3898, 3901, 4917, 4918, 4919, 4950,
						6140, 6145, 6150, 6155, 6269, 6272, 6275, 6278, 6826, 6827, 6836, 6837, 6846, 6847, 6856, 6857, 6866, 6867, 6876, 6877, 6886, 6887,
						6400, 5645, 6399, 7039, 7040, 7041, 7140, 7144, 7148, 6160, 13389, 13380, 7959, 7967, 7969, 7970, 9214, 8900, 8913, 9225, 9226, 8804,
						8808, 13140, 8792, 8798, 8786, 8860, 9362, 9363, 9364, 9365, 16008, 16056, 17541, 17545, 17549, 17515, 17531 };
				int playerGFX = _player.getGfxId().getTempCharGfx();
				for (int gfx : FouGFX) {
					if (playerGFX != gfx) {
						gfxcheck = true;
						break;
					}
				}
				if (!gfxcheck) {
					return;
				}

				for (int i = 3; i > 0; i--) {
					_target.onAction(_player);
				}

				L1PcInstance pc = (L1PcInstance) _pc;
				if (pc.포우슬레이어브레이브) {
					_player.sendPackets(new S_SkillSound(_player.getId(), 7020), true);
					_player.sendPackets(new S_SkillSound(_targetID, 17231), true);
					Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 7020), true);
					Broadcaster.broadcastPacket(_player, new S_SkillSound(_targetID, 17231), true);
				} else if (!pc.포우슬레이어브레이브) {
					_player.sendPackets(new S_SkillSound(_player.getId(), 7020), true);
					_player.sendPackets(new S_SkillSound(_targetID, 6509), true);
					Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 7020), true);
					Broadcaster.broadcastPacket(_player, new S_SkillSound(_targetID, 6509), true);
				}
				if (_player.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
					dmg += 20;
				}
				if (_player.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
					dmg += 40;
				}
				if (_player.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
					dmg += 60;
				}
				if (_player.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT4)) {
					dmg += 80;
				}
			}
				break;
			}

			if ((_target instanceof L1TowerInstance || _target instanceof L1DoorInstance) && dmg < 0) {
				dmg = 0;
			}

			if (heal > 0) {
				_player.setCurrentHp(heal + _player.getCurrentHp());
			}

			if (dmg != 0 || drainMana != 0) {
				_magic.commit(dmg, drainMana, _skillId);
			}

			_magic = null;
		} catch (Exception e) {
			// 스킬 오류 발생 부분에 케릭터명, 몹명, 타켓명순으로 출력

			// System.out.println("오류 발생 : " + _player.getAccountName() + " | "
			// + _npc.getName() + " | " + _target.getName());
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

}
