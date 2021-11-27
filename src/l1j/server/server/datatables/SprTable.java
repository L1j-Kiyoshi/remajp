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
package l1j.server.server.datatables;

import static l1j.server.server.ActionCodes.ACTION_AltAttack;
import static l1j.server.server.ActionCodes.ACTION_Attack;
import static l1j.server.server.ActionCodes.ACTION_AxeAttack;
import static l1j.server.server.ActionCodes.ACTION_AxeWalk;
import static l1j.server.server.ActionCodes.ACTION_BowAttack;
import static l1j.server.server.ActionCodes.ACTION_BowWalk;
import static l1j.server.server.ActionCodes.ACTION_CHAINSWORD;
import static l1j.server.server.ActionCodes.ACTION_CHAINSWORD_Walk;
import static l1j.server.server.ActionCodes.ACTION_ClawAttack;
import static l1j.server.server.ActionCodes.ACTION_ClawWalk;
import static l1j.server.server.ActionCodes.ACTION_DaggerAttack;
import static l1j.server.server.ActionCodes.ACTION_DaggerWalk;
import static l1j.server.server.ActionCodes.ACTION_EdoryuAttack;
import static l1j.server.server.ActionCodes.ACTION_EdoryuWalk;
import static l1j.server.server.ActionCodes.ACTION_SkillAttack;
import static l1j.server.server.ActionCodes.ACTION_SkillBuff;
import static l1j.server.server.ActionCodes.ACTION_SpearAttack;
import static l1j.server.server.ActionCodes.ACTION_SpearWalk;
import static l1j.server.server.ActionCodes.ACTION_StaffAttack;
import static l1j.server.server.ActionCodes.ACTION_StaffWalk;
import static l1j.server.server.ActionCodes.ACTION_SwordAttack;
import static l1j.server.server.ActionCodes.ACTION_SwordWalk;
import static l1j.server.server.ActionCodes.ACTION_ThrowingKnifeAttack;
import static l1j.server.server.ActionCodes.ACTION_ThrowingKnifeWalk;
import static l1j.server.server.ActionCodes.ACTION_TwoHandSwordAttack;
import static l1j.server.server.ActionCodes.ACTION_TwoHandSwordWalk;
import static l1j.server.server.ActionCodes.ACTION_Walk;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.utils.SQLUtil;

public class SprTable {

	private static Logger _log = Logger.getLogger(SprTable.class.getName());

	private static class Spr {
		private final HashMap<Integer, Integer> moveSpeed = new HashMap<Integer, Integer>();

		private final HashMap<Integer, Integer> attackSpeed = new HashMap<Integer, Integer>();

		private final HashMap<Integer, Integer> dmgMotionSpeed = new HashMap<Integer, Integer>();

		private int nodirSpellSpeed = 1200;

		private int dirSpellSpeed = 1200;
	}

	private static final HashMap<Integer, Spr> _dataMap = new HashMap<Integer, Spr>();

	private static final SprTable _instance = new SprTable();

	private SprTable() {
		loadSprAction();
	}

	public static SprTable getInstance() {
		return _instance;
	}

	/**
	 * spr_action 테이블을 로드한다.
	 */
	public void loadSprAction() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		Spr spr = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM spr_action");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int key = rs.getInt("spr_id");
				if (!_dataMap.containsKey(key)) {
					spr = new Spr();
					_dataMap.put(key, spr);
				} else {
					spr = _dataMap.get(key);
				}

				int actid = rs.getInt("act_id");
				int frameCount = rs.getInt("framecount");
				int frameRate = rs.getInt("framerate");
				int speed = calcMoveActionSpeed(frameCount, frameRate);
				/*
				 * if(key >= 11328 && key <= 11448){ speed =
				 * calcActionSpeed(frameCount, frameRate); }
				 */

				switch (actid) {
				case ACTION_Walk:
				case ACTION_SwordWalk:
				case ACTION_AxeWalk:
				case ACTION_BowWalk:
				case ACTION_SpearWalk:
				case ACTION_StaffWalk:
				case ACTION_DaggerWalk:
				case ACTION_TwoHandSwordWalk:
				case ACTION_EdoryuWalk:
				case ACTION_ClawWalk:
				case ACTION_ThrowingKnifeWalk:
				case ACTION_CHAINSWORD_Walk: {
					speed = calcMoveActionSpeed(frameCount, frameRate);
					spr.moveSpeed.put(actid, speed);
				}
					break;
				case ACTION_SkillAttack: {
					spr.dirSpellSpeed = speed;
				}
					break;
				case ACTION_SkillBuff: {
					spr.nodirSpellSpeed = speed;
				}
					break;
				case ACTION_Attack:
				case ACTION_SwordAttack:
				case ACTION_AxeAttack:
				case ACTION_BowAttack:
				case ACTION_SpearAttack:
				case ACTION_StaffAttack:
				case ACTION_DaggerAttack:
				case ACTION_TwoHandSwordAttack:
				case ACTION_EdoryuAttack:
				case ACTION_ClawAttack:
				case ACTION_ThrowingKnifeAttack:
				case ACTION_AltAttack:
				case ACTION_CHAINSWORD:
				case 89:
				case 74:
				case 75: {
					spr.attackSpeed.put(actid, speed);
				}
					break;
				case 2:
					spr.dmgMotionSpeed.put(actid, speed);
					break;
				default:
					break;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_log.config("SPR 데이터 " + _dataMap.size() + "건 로드");
	}

	/**
	 * 프레임수와 frame rate로부터 액션의 합계 시간(ms)을 계산해 돌려준다.
	 */
	private int calcActionSpeed(int frameCount, int frameRate) {
		// return (int) (frameCount * 40 * (24D / frameRate));
		return (int) (frameCount * 49 * (24D / frameRate));
	}

	private int calcMoveActionSpeed(int frameCount, int frameRate) {
		return (int) (frameCount * 40 * (24D / frameRate));
	}

	/**
	 * 지정된 spr의 공격 속도를 돌려준다. 만약 spr로 지정된 weapon_type의 데이터가 설정되어 있지 않은 경우는, 1.
	 * attack의 데이터를 돌려준다.
	 * 
	 * @param sprid
	 *            - 조사하는 spr의 ID
	 * @param actid
	 *            - 무기의 종류를 나타내는 값. L1Item.getType1()의 변환값 +1과 일치한다
	 * @return 지정된 spr의 공격 속도(ms)
	 */
	public int getAttackSpeed(int sprid, int actid) {
		try {
			boolean ww = false;
			if (actid == 89) {
				actid = 12;
				ww = true;
			}

			if (_dataMap.containsKey(sprid)) {
				if (_dataMap.get(sprid).attackSpeed.containsKey(actid)) {
					if (ww) {
						return (int) (_dataMap.get(sprid).attackSpeed.get(actid) * 0.828);
					}
					return _dataMap.get(sprid).attackSpeed.get(actid);
				} else if (actid == ACTION_Attack) {
					return 0;
				} else {
					if (_dataMap.get(sprid).attackSpeed.get(ACTION_Attack) != null) {
						return _dataMap.get(sprid).attackSpeed.get(ACTION_Attack);
					} else {
						return 0;
					}
				}
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	public int getMoveSpeed(int sprid, int actid) {
		try {
			if (actid == 88) {
				actid = 11;
			}
			if (_dataMap.containsKey(sprid)) {
				if (_dataMap.get(sprid).moveSpeed.containsKey(actid)) {
					return _dataMap.get(sprid).moveSpeed.get(actid);
				} else if (actid == ACTION_Walk) {
					return 0;
				} else {
					return _dataMap.get(sprid).moveSpeed.get(ACTION_Walk);
				}
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;
	}

	public int getDirSpellSpeed(int sprid) {
		if (_dataMap.containsKey(sprid)) {
			return _dataMap.get(sprid).dirSpellSpeed;
		}
		return 0;
	}

	public int getNodirSpellSpeed(int sprid) {
		if (_dataMap.containsKey(sprid)) {
			return _dataMap.get(sprid).nodirSpellSpeed;
		}
		return 0;
	}

	public int getDmgMotionSpeed(int sprid) {
		try {
			if (_dataMap.containsKey(sprid)) {
				if (_dataMap.get(sprid).dmgMotionSpeed.containsKey(2))
					return _dataMap.get(sprid).dmgMotionSpeed.get(2);
			}
		} catch (Exception e) {
			return 0;
		}
		return 0;

	}
}
