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

public class L1Chaser extends TimerTask {
	private ScheduledFuture<?> _future = null;
	private int _timeCounter = 0;
	private final L1PcInstance _pc;
	private final L1Character _cha;
	private double _dmg = 0;
	private int _cc = 0;

	public L1Chaser(L1PcInstance pc, L1Character cha, double dmg, int cc) {
		_cha = cha;
		_pc = pc;
		_dmg = dmg;
		_cc = cc;
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
		if (_cha.getCurrentHp() - (int) _dmg <= 0 && _cha.getCurrentHp() != 1) {
			_dmg = _cha.getCurrentHp();
		} else if (_cha.getCurrentHp() == 1) {
			_dmg = 1;
		}
		S_EffectLocation packet = null;
		if (_cc == 0) {
			packet = new S_EffectLocation(_cha.getX(), _cha.getY(),
					(short) 6985);
		} else {
			packet = new S_EffectLocation(_cha.getX(), _cha.getY(),
					(short) 7179);
		}
		_pc.sendPackets(packet);
		_pc.broadcastPacket(packet);
		if (_cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _cha;
			pc.sendPackets(new S_DoActionGFX(pc.getId(),
					ActionCodes.ACTION_Damage));
			pc.broadcastPacket(new S_DoActionGFX(pc.getId(),
					ActionCodes.ACTION_Damage));
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
			pc.receiveDamage(_pc, _dmg, false);
		} else if (_cha instanceof L1NpcInstance) {
			L1NpcInstance npc = (L1NpcInstance) _cha;
			npc.broadcastPacket(new S_DoActionGFX(npc.getId(),
					ActionCodes.ACTION_Damage));
			if (npc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.EARTH_BIND)
					|| npc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.ICE_LANCE)) {
				return;
			}
			npc.receiveDamage(_pc, (int) _dmg);
		}
	}

}
