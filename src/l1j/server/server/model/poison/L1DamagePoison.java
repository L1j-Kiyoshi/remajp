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
package l1j.server.server.model.poison;

import java.util.Random;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;

public class L1DamagePoison extends L1Poison {

	private Thread _timer;
	private final L1Character _attacker;
	private final L1Character _target;
	private final int _damageSpan;
	private final int _damage;
	private final int _type;// 0=그외 // 1=인첸트베놈

	private L1DamagePoison(L1Character attacker, L1Character cha,
			int damageSpan, int damage) {
		_attacker = attacker;
		_target = cha;
		_damageSpan = damageSpan;
		_damage = damage;
		_type = 0;
		doInfection();
	}

	private L1DamagePoison(L1Character attacker, L1Character cha,
			int damageSpan, int damage, int type) {
		_attacker = attacker;
		_target = cha;
		_damageSpan = damageSpan;
		_damage = damage;
		_type = type;
		doInfection();
	}

	private static Random _random = new Random(System.nanoTime());

	private class NormalPoisonTimer extends Thread {
		private NormalPoisonTimer() {
			super(
					"l1j.server.server.model.poison.L1DamagePoison.NormalPoisonTimer");
		}

		@Override
		public void run() {
			try {

				L1PcInstance player = null;
				L1MonsterInstance mob = null;
				while (true) {
					try {
						Thread.sleep(_damageSpan);
					} catch (InterruptedException e) {
						break;
					}

					if (!_target.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.STATUS_POISON)) {
						break;
					}
					if (_target instanceof L1PcInstance) {
						player = (L1PcInstance) _target;
						if (_type == 1) {
							player.receiveDamage(_attacker,
									_random.nextInt(49) + 1, false);
						} else {
							player.receiveDamage(_attacker, _damage, false);
						}

						if (player.isDead()) {
							break;
						}
					} else if (_target instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) _target;
						if (_type == 1) {
							mob.receiveDamage(_attacker,
									_random.nextInt(49) + 1);
						} else {
							mob.receiveDamage(_attacker, _damage);
						}

						if (mob.isDead()) {
							return;
						}
					}
				}
				cure();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	boolean isDamageTarget(L1Character cha) {
		return (cha instanceof L1PcInstance)
				|| (cha instanceof L1MonsterInstance);
	}

	private void doInfection() {
		_target.getSkillEffectTimerSet().setSkillEffect(
				L1SkillId.STATUS_POISON, 30000);
		_target.setPoisonEffect(1);

		if (isDamageTarget(_target)) {
			if (_target instanceof L1PcInstance)
				((L1PcInstance) _target).sendPackets(new S_PacketBox(
						S_PacketBox.커스종류, 1, 30), true);
			_timer = new NormalPoisonTimer();
			GeneralThreadPool.getInstance().execute(_timer);
		}
	}

	public static boolean doInfection(L1Character attacker, L1Character cha,
			int damageSpan, int damage) {
		if (!isValidTarget(cha)) {
			return false;
		}

		cha.setPoison(new L1DamagePoison(attacker, cha, damageSpan, damage));
		return true;
	}

	public static boolean doInfection(L1Character attacker, L1Character cha,
			int damageSpan, int damage, int type) {
		if (!isValidTarget(cha)) {
			return false;
		}

		cha.setPoison(new L1DamagePoison(attacker, cha, damageSpan, damage,
				type));
		return true;
	}

	@Override
	public int getEffectId() {
		return 1;
	}

	@Override
	public void cure() {
		if (_timer != null) {
			_timer.interrupt();
		}
		if (_target instanceof L1PcInstance)
			((L1PcInstance) _target).sendPackets(new S_PacketBox(
					S_PacketBox.커스종류, 0, 0), true);
		_target.setPoisonEffect(0);
		_target.getSkillEffectTimerSet().killSkillEffectTimer(
				L1SkillId.STATUS_POISON);
		_target.setPoison(null);
	}
}
