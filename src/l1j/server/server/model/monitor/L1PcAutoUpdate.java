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
package l1j.server.server.model.monitor;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.TimeController.FishingTimeController;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;

public class L1PcAutoUpdate extends L1PcMonitor {

	public L1PcAutoUpdate(int oId) {
		super(oId);
	}

	public L1PcAutoUpdate(L1PcInstance l1PcInstance) {
		super(l1PcInstance);
	}

	@Override
	public void execTask(L1PcInstance pc) {
		// System.out.println("½ÇÇà");
		try {
			if (pc.isFishing()) {
				if (pc.getMapId() != 5302 && pc.getMapId() != 5490) {
					pc.setFishingTime(0);
					pc.setFishingReady(false);
					pc.setFishing(false);
					pc.setFishingItem(null);
					pc.sendPackets(new S_CharVisualUpdate(pc));
					Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc));
					FishingTimeController.getInstance().removeMember(pc);
				}
			}
			if (pc.isInvisble()) {

				if (pc.getMapId() == 66) {
					if ((pc.getX() >= 32824 && pc.getX() <= 32832
							&& pc.getY() >= 32815 && pc.getY() <= 32822)) {
						pc.delInvis();
					}
				}

				/*
				 * if(pc.getMapId()==4){ if((pc.getX()>=33628 &&
				 * pc.getX()<=33635 && pc.getY()>=32674 && pc.getY()<=32681)){
				 * pc.delInvis(); }
				 * 
				 * 
				 * 
				 * if((pc.getX()>=33129 && pc.getX()<=33147 && pc.getY()>=32761
				 * && pc.getY()<=32777)){ pc.delInvis(); }
				 * 
				 * }
				 */
			}
		} catch (Exception e) {
		}
		try {
			pc.updateObject();
		} catch (Exception e) {
		}
		GeneralThreadPool.getInstance().schedule(this, 300);
	}
}
