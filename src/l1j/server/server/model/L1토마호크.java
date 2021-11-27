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
import l1j.server.server.serverpackets.S_NewCreateItem;

public class L1토마호크 extends TimerTask {
	private static final Random _random = new Random();
	private ScheduledFuture<?> _future = null;
	private int _timeCounter = 0;
	private final L1PcInstance _pc;
	private final L1Character _cha;

	public L1토마호크(L1PcInstance pc, L1Character cha) {
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

			if (_timeCounter >= 6) {
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
				target.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창,
						false), true);
				target.토마호크th = null;
				// System.out.println("패킷쏨.");
			} else if (_cha instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) _cha;
				npc.토마호크th = null;
				// System.out.println("패킷쏨.");
			}
		}
		if (_future != null) {
			_future.cancel(false);
		}
	}

	public void attack() {// 레벨 * 2 / 6
		int damage = _pc.getLevel() / 6;// _random.nextInt(20) +
										// _pc.getLevel()/3;
		if (_cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _cha;
			pc.sendPackets(new S_DoActionGFX(pc.getId(),
					ActionCodes.ACTION_Damage));
			pc.broadcastPacket(new S_DoActionGFX(pc.getId(),
					ActionCodes.ACTION_Damage));
			// pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.토마호크_도트,
			// false), true);
			// pc.broadcastPacket(new S_NewCreateItem(S_NewCreateItem.토마호크_도트,
			// false));
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
			// npc.broadcastPacket(new S_NewCreateItem(S_NewCreateItem.토마호크_도트,
			// false));
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
