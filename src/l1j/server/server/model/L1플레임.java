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
import l1j.server.server.serverpackets.S_NewSkillIcons;
import l1j.server.server.serverpackets.S_SkillSound;

public class L1플레임 extends TimerTask {
	private static final Random _random = new Random();
	private ScheduledFuture<?> _future = null;
	private int _timeCounter = 0;
	private final L1PcInstance _pc;
	private final L1Character _cha;

	public L1플레임(L1PcInstance pc, L1Character cha) {
		_pc = pc;
		_cha = cha;
	}

	@Override
	public void run() {
		try {
			if (_cha == null || _cha.isDead()) {
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
		if (_cha != null) {
			if (_cha instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) _cha;
				target.sendPackets(new S_NewSkillIcons(L1SkillId.FLAME, false, -1));
				target.플레임th = null;
			} else if (_cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) _cha;
				npc.플레임th = null;
			}
		}
		if (_future != null) {
			_future.cancel(false);
		}
	}

	public void attack() {
		int damage = _pc.getAbility().getTotalStr() * 2 + 20;
		if (_cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _cha;
			pc.sendPackets(new S_DoActionGFX(pc.getId(),
					ActionCodes.ACTION_Damage));
			pc.broadcastPacket(new S_DoActionGFX(pc.getId(),
					ActionCodes.ACTION_Damage));
			_pc.sendPackets(new S_SkillSound(pc.getId(), 18509));// 12489
			Broadcaster.broadcastPacket(_pc, new S_SkillSound(pc.getId(), 18509));
			if (pc.getSkillEffectTimerSet()
					.hasSkillEffect(L1SkillId.EARTH_BIND)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.MOB_BASILL)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.MOB_COCA)) {
				return;
			}
			pc.receiveDamage(_pc, damage, false);
		} else if (_cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) _cha;
			npc.broadcastPacket(new S_DoActionGFX(npc.getId(),
					ActionCodes.ACTION_Damage));
			_pc.sendPackets(new S_SkillSound(npc.getId(), 18509));// 12489
			Broadcaster.broadcastPacket(_pc, new S_SkillSound(npc.getId(), 18509));
			if (npc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| npc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)
					|| npc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.MOB_BASILL)
					|| npc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.MOB_COCA)) {
				return;
			}
			npc.receiveDamage(_pc, damage);
		}
	}

}
