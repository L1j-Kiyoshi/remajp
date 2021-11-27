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

import static l1j.server.server.model.skill.L1SkillId.ERASE_MAGIC;

import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.ScheduledFuture;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_PacketBox;

public class L1이블리버스 extends TimerTask {
	private static final Random _random = new Random();
	private ScheduledFuture<?> _future = null;
	private int _timeCounter = 0;
	private final L1PcInstance _pc;
	private final L1Character _cha;
	private double _dmg = 0;
	private int _gfx = 8150;

	public L1이블리버스(L1PcInstance pc, L1Character cha, double dmg) {
		_cha = cha;
		_pc = pc;
		_dmg = dmg;
	}

	public L1이블리버스(L1PcInstance pc, L1Character cha, double dmg, int gfx) {
		_cha = cha;
		_pc = pc;
		_dmg = dmg;
		_gfx = gfx;
	}

	@Override
	public void run() {
		try {
			if (_cha == null || _cha.isDead() || _cha.getCurrentHp() <= 0) {
				stop();
				return;
			}
			_timeCounter++;
			attack();

			if (_timeCounter >= 3) {
				stop();
				return;
			}
		} catch (Throwable e) {
			stop();
			e.printStackTrace();
			return;
		}
	}

	public void begin() {
		_future = GeneralThreadPool.getInstance().scheduleAtFixedRate(this,
				1000, 1000);

	}

	public void stop() {
		if (_future != null) {
			_future.cancel(false);
		}
	}

	public void attack() {
		int tempdmg = (int) _dmg / 3;
		if (tempdmg < 1) {
			tempdmg = 1;
		}
		_dmg = _random.nextInt(tempdmg) + 2;
		int tempclac = _cha.getCurrentHp() - (int) _dmg;
		if (tempclac <= 0 && _cha.getCurrentHp() != 1) {
			_dmg = _cha.getCurrentHp();
		} else if (_cha.getCurrentHp() == 1) {
			_dmg = 1;
		}
		S_EffectLocation packet = new S_EffectLocation(_cha.getX(),
				_cha.getY(), (short) _gfx);
		_pc.sendPackets(packet);
		_pc.broadcastPacket(packet);
		int HOKKUHP = 0;
		/*
		 * if(_pc.getWeapon() != null && _pc.getWeapon().getItemId() == 450008)
		 * //마족검 HOKKUHP =
		 * _random.nextInt((_pc.getAbility().getTotalInt()+1)/2); else
		 */if (_pc.getWeapon() != null
				&& (_pc.getWeapon().getItemId() >= 277 && _pc.getWeapon()
						.getItemId() <= 283))
			HOKKUHP = 0;
		else {
			HOKKUHP = _random.nextInt(5) + 1;
		}

		if (_cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _cha;
			if (_timeCounter == 1) {
				pc.sendPackets(new S_DoActionGFX(pc.getId(),
						ActionCodes.ACTION_Damage));
				pc.broadcastPacket(new S_DoActionGFX(pc.getId(),
						ActionCodes.ACTION_Damage));
			}
			if (pc.getSkillEffectTimerSet()
					.hasSkillEffect(L1SkillId.EARTH_BIND)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return;
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(ERASE_MAGIC);
				pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_AURA));
			}

			if (HOKKUHP > 0 && pc.getCurrentHp() > 0) {
				if (HOKKUHP > pc.getCurrentHp()) {
					HOKKUHP = pc.getCurrentHp();
				}
				int newHp = pc.getCurrentHp() - HOKKUHP;
				pc.setCurrentHp(newHp);
				if (pc.getResistance().getEffectedMrBySkill() < 100) {
					newHp = _pc.getCurrentHp() + HOKKUHP;
					_pc.setCurrentHp(newHp);
				}
			}
			pc.receiveDamage(_pc, _dmg, false);
		} else if (_cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) _cha;
			if (_timeCounter == 1)
				npc.broadcastPacket(new S_DoActionGFX(npc.getId(),
						ActionCodes.ACTION_Damage));

			if (npc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| npc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return;
			}
			if (HOKKUHP > 0 && npc.getCurrentHp() > 0) {
				if (HOKKUHP > npc.getCurrentHp()) {
					HOKKUHP = npc.getCurrentHp();
				}
				int newHp = npc.getCurrentHp() - HOKKUHP;
				npc.setCurrentHp(newHp);
				newHp = _pc.getCurrentHp() + HOKKUHP;
				_pc.setCurrentHp(newHp);
			}
			npc.receiveDamage(_pc, (int) _dmg);

		}
	}

}
