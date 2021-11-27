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

import java.util.TimerTask;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1EquipmentTimer extends TimerTask {

	public L1EquipmentTimer(L1PcInstance pc, L1ItemInstance item) {
		_pc = pc;
		_item = item;
	}

	@Override
	public void run() {
		try {

			_item.getLastStatus().updateRemainingTime();
			if ((_item.getRemainingTime() - 1) > 0) {
				if (_pc.getOnlineStatus() == 0) {
					_item.stopEquipmentTimer();
				}
				_item.setRemainingTime(_item.getRemainingTime() - 1);
			} else {
				if (_item.getItemId() == 20383) {
					_item.setRemainingTime(0);
					if (_item.isEquipped()) {
						_pc.getInventory().setEquipped(_item, false, false,
								false);
					}
				} else
					_pc.getInventory().removeItem(_item, 1);
				this.cancel();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private final L1PcInstance _pc;
	private final L1ItemInstance _item;
}
