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

public class L1이블트릭 extends TimerTask {
	private static final Random _random = new Random();
	private ScheduledFuture<?> _future = null;
	private int _timeCounter = 0;
	private final L1PcInstance _pc;
	private final L1Character _cha;
	private int _gfx = 8152;

	public L1이블트릭(L1PcInstance pc, L1Character cha) {
		_cha = cha;
		_pc = pc;
	}

	public L1이블트릭(L1PcInstance pc, L1Character cha, int gfx) {
		_cha = cha;
		_pc = pc;
		_gfx = gfx;
	}

	@Override
	public void run() {
		try {
			if (_cha == null || _cha.isDead() || _cha.getCurrentMp() <= 0) {
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
		int damage = _random.nextInt(4) + 1;
		S_EffectLocation packet = new S_EffectLocation(_cha.getX(),
				_cha.getY(), (short) _gfx);
		_pc.sendPackets(packet);
		_pc.broadcastPacket(packet);
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
			if (damage > 0 && pc.getCurrentMp() > 0) {
				if (damage > pc.getCurrentMp()) {
					damage = pc.getCurrentMp();
				}
				int newMp = pc.getCurrentMp() - damage;
				pc.setCurrentMp(newMp);
				newMp = _pc.getCurrentMp() + damage;
				_pc.setCurrentMp(newMp);
			}
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
			if (damage > 0 && npc.getCurrentMp() > 0) {
				if (damage > npc.getCurrentMp()) {
					damage = npc.getCurrentMp();
				}
				int newMp = npc.getCurrentMp() - damage;
				npc.setCurrentMp(newMp);
				newMp = _pc.getCurrentMp() + damage;
				_pc.setCurrentMp(newMp);
			}
		}
	}

}
