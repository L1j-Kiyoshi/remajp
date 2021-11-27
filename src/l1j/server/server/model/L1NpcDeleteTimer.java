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

import l1j.server.GameSystem.GameList;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;

//public class L1NpcDeleteTimer extends TimerTask {
public class L1NpcDeleteTimer implements Runnable {

	private final L1NpcInstance _npc;
	private final int _timeMillis;

	public L1NpcDeleteTimer(L1NpcInstance npc, int timeMillis) {
		_npc = npc;
		_timeMillis = timeMillis;
	}

	@Override
	public void run() {
		try {
			if (_npc != null && _npc.getNpcId() == 4212013) {
				GameList.set¿ë¶¥(false);
			}
			if (_npc != null
					&& (_npc.getNpcId() == 100586 || _npc.getNpcId() == 100587)) {
				_npc.receiveDamage(_npc, 100000);
				Thread.sleep(2000);
			}
			
			if (_npc != null && (_npc.getNpcId() == 145686 || _npc.getNpcId() == 145685)) {
				_npc.broadcastPacket(new S_DoActionGFX(_npc.getId(), 19), true);
				for (L1Object obj : L1World.getInstance().getVisibleObjects(_npc, 3)) {
					if (obj instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) obj;
						pc.receiveDamage(_npc, 1000, false);
					}
				}
				//Thread.sleep(3000);
			}

			if (_npc != null && !_npc._destroyed)
				_npc.deleteMe();
			// this.cancel();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void begin() {
		GeneralThreadPool.getInstance().schedule(this, _timeMillis);
		// Timer timer = new Timer();
		// timer.schedule(this, _timeMillis);
	}
}
