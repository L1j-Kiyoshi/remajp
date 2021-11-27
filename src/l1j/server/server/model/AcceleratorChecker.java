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

import java.util.EnumMap;
import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1Skills;

/**
 * 가속기의 사용을 체크하는 클래스.
 */
public class AcceleratorChecker {

	private final L1PcInstance _pc;

	private int _injusticeCount;

	private int _justiceCount;

	// private static final int INJUSTICE_COUNT_LIMIT = Config.INJUSTICE_COUNT;

	// private static final int JUSTICE_COUNT_LIMIT = Config.JUSTICE_COUNT;

	// 실제로는 이동과 공격의 패킷 간격은 spr의 이론치보다5%만큼 늦다.
	// 그것을 고려해―5로 하고 있다.
	private static final double CHECK_STRICTNESS = (Config.CHECK_STRICTNESS - 5) / 100D;

	private static final double HASTE_RATE = 0.745;

	private static final double WAFFLE_RATE = 0.874;// 874;

	private static final double THIRDSPEED_RATE = 0.874;// by사부

	private final EnumMap<ACT_TYPE, Long> _actTimers = new EnumMap<ACT_TYPE, Long>(
			ACT_TYPE.class);

	private final EnumMap<ACT_TYPE, Long> _checkTimers = new EnumMap<ACT_TYPE, Long>(
			ACT_TYPE.class);

	public static enum ACT_TYPE {
		MOVE, ATTACK, SPELL_DIR, SPELL_NODIR
	}

	// 체크의 결과
	public static final int R_OK = 0;

	public static final int R_DETECTED = 1;

	public static final int R_DISCONNECTED = 2;

	private static final double Level_Rate_0 = 1.392;
	private static final double Level_Rate_15 = 1.321;
	private static final double Level_Rate_30 = 1.25;
	private static final double Level_Rate_45 = 1.178;
	private static final double Level_Rate_50 = 1.107;
	private static final double Level_Rate_52 = 1.035;
	private static final double Level_Rate_55 = 0.964;
	private static final double Level_Rate_75 = 0.892;
	private static final double Level_Rate_80 = 0.821;
	private static final double Level_Rate_82 = 0.812;
	private static final double Level_Rate_84 = 0.794;
	private static final double Level_Rate_86 = 0.754;
	private static final double Level_Rate_87 = 0.700;
	private static final double Level_Rate_88 = 0.650;

	private static final double Move_Level_Rate_0 = 1.023;
	private static final double Move_Level_Rate_15 = 0.992;
	private static final double Move_Level_Rate_45 = 0.960;
	private static final double Move_Level_Rate_50 = 0.929;
	private static final double Move_Level_Rate_52 = 0.898;
	private static final double Move_Level_Rate_55 = 0.867;
	private static final double Move_Level_Rate_60 = 0.835;
	private static final double Move_Level_Rate_65 = 0.804;
	private static final double Move_Level_Rate_70 = 0.773;
	private static final double Move_Level_Rate_75 = 0.773;
	private static final double Move_Level_Rate_80 = 0.773;

	// cache
	private int _attack_gfxid = -1;
	private int _attack_weapon = -1;
	private int _attack_interval = -1;
	private int _move_gfxid = -1;
	private int _move_weapon = -1;
	private int _move_interval = -1;
	private int _spelldir_gfxid = -1;
	private int _spelldir_interval = -1;
	private int _spellnodir_gfxid = -1;
	private int _spellnodir_interval = -1;

	public AcceleratorChecker(L1PcInstance pc) {
		_pc = pc;
		_injusticeCount = 0;
		_justiceCount = 0;
		long now = System.currentTimeMillis();
		for (ACT_TYPE each : ACT_TYPE.values()) {
			_actTimers.put(each, now);
			_checkTimers.put(each, now);
		}
	}

	/**
	 * 액션의 간격이 부정하지 않을까 체크해, 적당 처리를 실시한다.
	 * 
	 * @param type
	 *            - 체크하는 액션의 타입
	 * @return 문제가 없었던 경우는 0, 부정할 경우는 1, 부정 동작이 일정 회수에 이르렀기 때문에 플레이어를 절단 했을 경우는
	 *         2를 돌려준다.
	 */
	public int checkInterval(ACT_TYPE type) {
		int result = R_OK;
		long now = System.currentTimeMillis();
		long interval = now - _actTimers.get(type);
		int rightInterval = getRightInterval(type);

		if (type == ACT_TYPE.MOVE) {
			_pc.speed_time_temp = rightInterval + now;
		}

		interval *= CHECK_STRICTNESS;
		interval *= 1.1;
		if (_pc.isGm() || _pc.isSGm()) {
			return R_OK;
		}

		if (_pc.getGfxId().getTempCharGfx() == 6284) { // 유령의집호박
			_injusticeCount = 0;
			_justiceCount = 0;
			return R_OK;
		}
		if (0 < interval && interval < rightInterval) {
			_injusticeCount++;
			_justiceCount = 0;
			result = R_DETECTED;
			if (_injusticeCount >= Config.INJUSTICE_COUNT) {
				_injusticeCount = 0;
				result = R_DISCONNECTED;
			}
		} else if (interval >= rightInterval) {
			_justiceCount++;
			if (_justiceCount >= Config.JUSTICE_COUNT) {
				_injusticeCount = 0;
				_justiceCount = 0;
			}
		}

		// 검증용
		/*
		 * double rate = (double) interval / rightInterval;
		 * System.out.println(String.format("%s: %d / %d = %.2f (o-%d x-%d)",
		 * type.toString(), interval, rightInterval, rate, _justiceCount,
		 * _injusticeCount));
		 */
		_actTimers.put(type, now);
		return result;
	}

	/*
	 * private void doDisconnect(String type) { if (!(_pc.getAccessLevel() ==
	 * Config.GMCODE)) { L1Teleport.teleport(_pc, _pc.getSpeedHackX(),
	 * _pc.getSpeedHackY(), _pc.getSpeedHackMapid(), _pc.getSpeedHackHeading(),
	 * false); //_pc.sendPackets(new S_ServerMessage(945)); // 위법 프로그램이 발견되었으므로,
	 * 종료합니다. //_pc.sendPackets(new S_Disconnect()); } else { // GM는 절단 하지 않는다
	 * _pc.sendPackets(new S_SystemMessage(
	 * "[스핵 절단] 캐릭명 - "+_pc.getName()+" / 변신 - "
	 * +_pc.getGfxId().getTempCharGfx()+" / 타입 -"+type)); _injusticeCount = 0; }
	 * 
	 * }
	 */
	/**
	 * PC 상태로부터 지정된 종류의 액션의 올바른 인터벌(ms)을 계산해, 돌려준다.
	 * 
	 * @param type
	 *            - 액션의 종류
	 * @param _pc
	 *            - 조사하는 PC
	 * @return 올바른 인터벌(ms)
	 */
	private int getRightInterval(ACT_TYPE type) {
		int interval;
		int gfxid = _pc.getGfxId().getTempCharGfx();
		int weapon = _pc.getCurrentWeapon();

		switch (type) {
		case ATTACK:
			if (_attack_gfxid != gfxid || _attack_weapon != weapon) {
				_attack_gfxid = gfxid;
				_attack_weapon = weapon;
				_attack_interval = SprTable.getInstance().getAttackSpeed(gfxid, weapon + 1);
			}

			interval = _attack_interval;

			if (gfxid == 13140 || gfxid == 15154 || gfxid == 15232 || gfxid == 14923 || gfxid == 15223) {
				interval *= Level_Rate_80;
			} else if (gfxid == 17272 || gfxid == 17273 || gfxid == 17274 || gfxid == 17275 || gfxid == 17276 || gfxid == 17277) {
				interval *= Level_Rate_84;
			} else if (gfxid == 16014 || gfxid == 15986 || gfxid == 16008 || gfxid == 16002 || gfxid == 16027) {
				interval *= Level_Rate_86;
			} else if (gfxid == 16284 || gfxid == 16053 || gfxid == 16056 || gfxid == 16074 || gfxid == 16040) {
				interval *= Level_Rate_88;
			} else {
				if (_pc.getLevel() >= 88) {
					interval *= Level_Rate_88;
				} else if (_pc.getLevel() >= 87) {
					interval *= Level_Rate_87;
				} else if (_pc.getLevel() >= 86) {
					interval *= Level_Rate_86;
				} else if (_pc.getLevel() >= 84) {
					interval *= Level_Rate_84;
				} else if (_pc.getLevel() >= 82) {
					interval *= Level_Rate_82;
				} else if (_pc.getLevel() >= 80) {
					interval *= Level_Rate_80;
				} else if (_pc.getLevel() >= 75) {
					interval *= Level_Rate_75;
				} else if (_pc.getLevel() >= 55) {
					interval *= Level_Rate_55;
				} else if (_pc.getLevel() >= 52) {
					interval *= Level_Rate_52;
				} else if (_pc.getLevel() >= 50) {
					interval *= Level_Rate_50;
				} else if (_pc.getLevel() >= 45) {
					interval *= Level_Rate_45;
				} else if (_pc.getLevel() >= 30) {
					interval *= Level_Rate_30;
				} else if (_pc.getLevel() >= 15) {
					interval *= Level_Rate_15;
				} else {
					interval *= Level_Rate_0;
				}
			
			}
			break;

		case MOVE:
			if (_move_gfxid != gfxid || _move_weapon != weapon) {
				_move_gfxid = gfxid;
				_move_weapon = weapon;
				_move_interval = SprTable.getInstance().getMoveSpeed(gfxid,
						weapon);
				// System.out.printf("%s: gfxid:%d, weapon:%d, interval:%d\n",
				// _pc.getName(), gfxid, weapon, _move_interval);
			}
			interval = _move_interval;
			/*
			 * if(gfxid == 11333 || gfxid == 11355 || gfxid == 11364 || gfxid ==
			 * 11379) interval *= 0.9; else if(gfxid == 11343) interval *= 0.8;
			 */
			if (gfxid == 13140) {
				interval *= Move_Level_Rate_80;
			}
			if (gfxid == 11333 || // "lv1 dwarf" ; 난쟁이
					gfxid == 11343 || // "lv15 ungoliant" ; 웅골리언트
					gfxid == 11355 || // "lv30 cockatrice" ; 코카트리스
					gfxid == 11364 || // "lv45 baphomet" ; 바포메트
					gfxid == 11379// "lv52 beleth" ; 베레스
			) {
				if (_pc.getLevel() >= 80) {
					interval *= Move_Level_Rate_80;
				} else if (_pc.getLevel() >= 75) {
					interval *= Move_Level_Rate_75;
				} else if (_pc.getLevel() >= 70) {
					interval *= Move_Level_Rate_70;
				} else if (_pc.getLevel() >= 65) {
					interval *= Move_Level_Rate_65;
				} else if (_pc.getLevel() >= 60) {
					interval *= Move_Level_Rate_60;
				} else if (_pc.getLevel() >= 55) {
					interval *= Move_Level_Rate_55;
				} else if (_pc.getLevel() >= 52) {
					interval *= Move_Level_Rate_52;
				} else if (_pc.getLevel() >= 50) {
					interval *= Move_Level_Rate_50;
				} else if (_pc.getLevel() >= 45) {
					interval *= Move_Level_Rate_45;
				} else if (_pc.getLevel() >= 15) {
					interval *= Move_Level_Rate_15;
				} else {
					interval *= Move_Level_Rate_0;
				}
			}
			break;

		case SPELL_DIR:
			if (_spelldir_gfxid != gfxid) {
				_spelldir_gfxid = gfxid;
				_spelldir_interval = SprTable.getInstance().getDirSpellSpeed(
						gfxid);
			}
			interval = _spelldir_interval;
			break;

		case SPELL_NODIR:
			if (_spellnodir_gfxid != gfxid) {
				_spellnodir_gfxid = gfxid;
				_spellnodir_interval = SprTable.getInstance()
						.getNodirSpellSpeed(gfxid);
			}
			interval = _spellnodir_interval;
			break;

		default:
			return 0;
		}
		if (_pc.isHaste()) {
			interval *= HASTE_RATE;
		}
		if (type.equals(ACT_TYPE.MOVE) && _pc.isFastMovable()) {
			interval *= HASTE_RATE;
		}
		if (type.equals(ACT_TYPE.MOVE) && _pc.isIllusionist()
				&& _pc.isUgdraFruit()) {
			interval *= HASTE_RATE;
		}
		if (_pc.isBloodLust()) { // 블러드러스트
			interval *= HASTE_RATE;
		}
		if (_pc.isSandstorm()) { // 샌드스톰
			interval *= HASTE_RATE;
		}
		if (_pc.isFocuswave()) { // 샌드스톰
			interval *= HASTE_RATE;
		}
		
		if (_pc.isdarkhos()) { // 샌드스톰
			interval *= HASTE_RATE;
		}
		
		if (_pc.isHurricane()) { // 허리케인
			interval *= HASTE_RATE;
		}

		if (_pc.isBrave()) {
			interval *= HASTE_RATE;
		}
		if (_pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.FIRE_BLESS)) {
			interval *= HASTE_RATE;
		}
		if (type.equals(ACT_TYPE.MOVE) && _pc.isElfBrave()) {
			interval *= HASTE_RATE;
		}

		if (type.equals(ACT_TYPE.ATTACK) && _pc.isElfBrave()) {
			interval *= WAFFLE_RATE;
		}
		if (_pc.isThirdSpeed()) {
			interval *= THIRDSPEED_RATE;
		}
		if (_pc.getMapId() == 5143) {
			interval *= (HASTE_RATE / 2);
		}
		if (type.equals(ACT_TYPE.MOVE) && (gfxid == 6697 || gfxid == 6698)) {
			interval *= HASTE_RATE;
		}

		return interval;
	}

	private long _skillNextUseTime = 0;
	private long _skillNextUseTime2 = 0;
	private long _skillNextUseTime3 = 0;
	private long _skillNextUseTime4 = 0;

//	private static Map<Integer, Integer> _skillInterval = new HashMap<Integer, Integer>();
//	static {
//		int e = 50; // 오차범위
//
//		_skillInterval.put(187, 2400 - e); // 포우 슬레이어
//		_skillInterval.put(189, 2400 - e); // 쇼크스킨
//		_skillInterval.put(192, 4400 - e); // 썬더 그랩
//		_skillInterval.put(87, 6000 - e); // 스턴
//		_skillInterval.put(123, 6000 - e); // 엠파이어
//		_skillInterval.put(146, 1550 - e); // 블러드 투 쏘울
//		_skillInterval.put(130, 500 - e); // 바디투마인드 - 500
//		_skillInterval.put(35, 600 - e); // 그레이터 힐 - 600
//		_skillInterval.put(132, 250 - e); // 트리플 - 400
//		_skillInterval.put(57, 300 - e); // 풀 힐 - 900
//		_skillInterval.put(19, 500 - e); // 익스트라 힐 - 500
//		_skillInterval.put(45, 500 - e); // 이럽션 - 500
//		_skillInterval.put(38, 500 - e); // 콘오브콜드 - 500
//		_skillInterval.put(46, 800 - e); // 선버스트 - 900
//		_skillInterval.put(70, 850 - e); // 파이어스톰 - 850
//		_skillInterval.put(65, 2300 - e); // 라이트닝 스톰 - 2300
//		_skillInterval.put(78, 12400 - e); // 앱솔루트 배리어 - 12400
//		_skillInterval.put(74, 3400 - e); // 미티어 - 3400
//		_skillInterval.put(77, 4000 - e); // 디스 - 5350
//		_skillInterval.put(44, 850 - e); // 캔슬 - 850
//		_skillInterval.put(29, 850 - e); // 슬로 - 850
//		_skillInterval.put(39, 850 - e); // 마나드레인 - 850
//		_skillInterval.put(27, 3500 - e); // 웨폰브레이크 - 3500
//		_skillInterval.put(208, 2500 - e); // 볼브레이크 - 3500
//
//	}

	public int checkSkillInterval(int skillId) {
		int result = R_OK;
		long now = System.currentTimeMillis();
		int rinterval;
		int rintervalPantera;
		int rintervalBlade;
		int rintervalFencer;

		if (_skillNextUseTime - now > 12400) {
			_skillNextUseTime = now;
		}
		
		if (_skillNextUseTime2 - now > 12400) {
			_skillNextUseTime2 = now;
		}
		
		if (_skillNextUseTime3 - now > 12400) {
			_skillNextUseTime3 = now;
		}
		
		if (_skillNextUseTime4 - now > 12400) {
			_skillNextUseTime4 = now;
		}

		if (now < _skillNextUseTime) {
			result = R_DETECTED;
		} else {
			if(_pc.getType() == 8) {
				if (skillId == 237) {
					rintervalPantera = getRightSkillInterval(skillId);
					_skillNextUseTime2 = now + (long) rintervalPantera;
				} else if (skillId == 239) {
					rintervalBlade = getRightSkillInterval(skillId);
					_skillNextUseTime3 = now + (long) rintervalBlade;
				} else {
					rintervalFencer = getRightSkillInterval(skillId);
					_skillNextUseTime4 = now + (long) rintervalFencer;
				}
			} else { 
				rinterval = getRightSkillInterval(skillId);
				_skillNextUseTime = now + (long) rinterval;
			}
		}
		
		return result;
	}

	private int getRightSkillInterval(int skillId) {
		L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
		long currentMillis = System.currentTimeMillis();
		long j = _pc.getCurrentSpriteInterval(skill.getTarget().equals("attack") ? EActionCodes.spell_dir : EActionCodes.spell_nodir);
		long l = (SpriteInformationLoader.getInstance().getUseSpellInterval(_pc, skillId) + j) - 5L;
		_pc.lastSpellUseMillis = currentMillis + (l > 0 ? l : 0L);
		Integer delay = (int)l;
		return delay;
	}
}
