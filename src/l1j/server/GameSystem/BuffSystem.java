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
package l1j.server.GameSystem;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BuffNpcInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_DoActionGFX;

public class BuffSystem implements Runnable {
	private final L1BuffNpcInstance _npc;
	private final L1PcInstance _pc;

	// private int _adena;
	public BuffSystem(L1BuffNpcInstance npc, L1PcInstance pc, int adena) {
		_npc = npc;
		_pc = pc;
		// _adena = adena;
		new Thread(this).start();
	}

	public void run() {
		try {
			L1NpcInstance npc1 = null;
			L1NpcInstance npc2 = null;
			for (Object object : L1World.getInstance().getVisibleObjects(_npc,
					3)) {
				if (object instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) object;
					if (npc.getNpcId() == 7000074) {
						npc1 = npc;
					} else if (npc.getNpcId() == 7000075) {
						npc2 = npc;
					}
				}
			}

			if (_pc.isElf()) {// 요정이면 윈드샷
				Broadcaster.broadcastPacket(npc1,
						new S_DoActionGFX(_npc.getId(), 19));
				new L1SkillUse().handleCommands(_pc, L1SkillId.WIND_SHOT,
						_pc.getId(), _pc.getX(), _pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			} else {// 요정아니면 파이어웨폰
				Broadcaster.broadcastPacket(npc1,
						new S_DoActionGFX(_npc.getId(), 19));
				new L1SkillUse().handleCommands(_pc, L1SkillId.FIRE_WEAPON,
						_pc.getId(), _pc.getX(), _pc.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
			}
			Broadcaster.broadcastPacket(npc1, new S_DoActionGFX(_npc.getId(),
					19));
			new L1SkillUse().handleCommands(_pc, L1SkillId.BLESS_WEAPON,
					_pc.getId(), _pc.getX(), _pc.getY(), null, 0,
					L1SkillUse.TYPE_GMBUFF);
			Broadcaster.broadcastPacket(npc1, new S_DoActionGFX(npc1.getId(),
					19));
			new L1SkillUse().handleCommands(_pc, L1SkillId.FIRE_SHIELD,
					_pc.getId(), _pc.getX(), _pc.getY(), null, 0,
					L1SkillUse.TYPE_GMBUFF);
			Broadcaster.broadcastPacket(npc1, new S_DoActionGFX(npc2.getId(),
					19));
			new L1SkillUse().handleCommands(_pc,
					L1SkillId.PHYSICAL_ENCHANT_DEX, _pc.getId(), _pc.getX(),
					_pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			Broadcaster.broadcastPacket(npc1, new S_DoActionGFX(_npc.getId(),
					19));
			new L1SkillUse().handleCommands(_pc,
					L1SkillId.PHYSICAL_ENCHANT_STR, _pc.getId(), _pc.getX(),
					_pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			Thread.sleep(1000L);
			Broadcaster.broadcastPacket(npc2, new S_DoActionGFX(_npc.getId(),
					19));
			new L1SkillUse().handleCommands(_pc, L1SkillId.ADVANCE_SPIRIT,
					_pc.getId(), _pc.getX(), _pc.getY(), null, 0,
					L1SkillUse.TYPE_GMBUFF);
			Broadcaster.broadcastPacket(npc2, new S_DoActionGFX(_npc.getId(),
					19));
			new L1SkillUse().handleCommands(_pc, L1SkillId.NATURES_TOUCH,
					_pc.getId(), _pc.getX(), _pc.getY(), null, 0,
					L1SkillUse.TYPE_GMBUFF);
			Broadcaster.broadcastPacket(npc2, new S_DoActionGFX(_npc.getId(),
					19));
			new L1SkillUse().handleCommands(_pc, L1SkillId.FULL_HEAL,
					_pc.getId(), _pc.getX(), _pc.getY(), null, 0,
					L1SkillUse.TYPE_GMBUFF);
			Broadcaster.broadcastPacket(npc2, new S_DoActionGFX(_npc.getId(),
					19));

			/*
			 * if(_adena >= 1000000){//100만버프 Thread.sleep(1000L);
			 * Broadcaster.broadcastPacket(npc1, new S_DoActionGFX(_npc.getId(),
			 * 19)); new L1SkillUse().handleCommands(_pc,
			 * L1SkillId.STATUS_TIKAL_BOSSDIE, _pc.getId(), _pc.getX(),
			 * _pc.getY(), null, 0 ,L1SkillUse.TYPE_GMBUFF);//상아탑버프 } if(_adena
			 * >= 10000000){//1000만버프 Thread.sleep(2000L);
			 * Broadcaster.broadcastPacket(npc2, new S_DoActionGFX(_npc.getId(),
			 * 19)); new L1SkillUse().handleCommands(_pc,
			 * L1SkillId.STATUS_COMA_5, _pc.getId(), _pc.getX(), _pc.getY(),
			 * null, 0 ,L1SkillUse.TYPE_GMBUFF);//코마버프 }
			 */
			// Broadcaster.broadcastPacket(npc1, new S_DoActionGFX(npc1.getId(),
			// 19));
			// Broadcaster.broadcastPacket(npc1, new S_SkillSound(npc1.getId(),
			// 2178));
			// Broadcaster.broadcastPacket(npc2, new S_DoActionGFX(npc2.getId(),
			// 19));
			// Broadcaster.broadcastPacket(npc2, new S_SkillSound(npc2.getId(),
			// 2178));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
