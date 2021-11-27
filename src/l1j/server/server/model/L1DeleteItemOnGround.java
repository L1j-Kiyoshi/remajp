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

import java.util.List;
import java.util.logging.Logger;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class L1DeleteItemOnGround {
	private DeleteTimer _deleteTimer;

	private static final Logger _log = Logger
			.getLogger(L1DeleteItemOnGround.class.getName());

	public L1DeleteItemOnGround() {
	}

	private class DeleteTimer extends Thread {
		public DeleteTimer() {
		}

		@Override
		public void run() {
			try {
				int time = (1000 * 60) * 19;
				while (true) {
					try {
						Thread.sleep(time);
					} catch (Exception exception) {
						_log.warning("L1DeleteItemOnGround error: " + exception);
						break;
					}
					// L1World.getInstance().broadcastServerMessage("월드맵 청소 : 월드 맵상의 아이템이 1분 후에 삭제 됩니다.");
					try {
						Thread.sleep((1000 * 60) - 5000);
					} catch (Exception exception) {
						_log.warning("L1DeleteItemOnGround error: " + exception);
						break;
					}
					// L1World.getInstance().broadcastServerMessage("월드맵 청소 : 월드 맵상의 아이템이 5초 후에 삭제 됩니다.");
					try {
						Thread.sleep(5000);
					} catch (Exception exception) {
						_log.warning("L1DeleteItemOnGround error: " + exception);
						break;
					}
					deleteItem();
					// #################### 여기 부터 추가 #################
					// L1World.getInstance().broadcastServerMessage("월드맵 청소 : 월드 맵상의 아이템이 자동 청소 되었습니다.");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void initialize() {
		_deleteTimer = new DeleteTimer();
		_deleteTimer.start();
		// GeneralThreadPool.getInstance().execute(_deleteTimer); // 타이머 개시
	}

	private void deleteItem() {
		int numOfDeleted = 0;
		for (L1Object obj : L1World.getInstance().getAllItem()) {
			if (!(obj instanceof L1ItemInstance)) {
				continue;
			}
			L1ItemInstance item = (L1ItemInstance) obj;
			if (item.getItemOwner() == null
					|| !(item.getItemOwner() instanceof L1RobotInstance)) {
				if (item.getX() == 0 && item.getY() == 0) { // 지면상의 아이템은 아니고,
															// 누군가의 소유물
					continue;
				}
			}
			if (item.getItem().getItemId() == 40515) { // 정령의 돌
				continue;
			}
			if (L1HouseLocation.isInHouse(item.getX(), item.getY(),
					item.getMapId())) { // 아지트내
				continue;
			}
			// 무한대전시 대전장안 아이템 안사라지게 by 아스라이
			boolean ck = false;
			if (item.getMapId() >= 88 && item.getMapId() <= 98) {
				for (L1UltimateBattle ub : UBTable.getInstance().getAllUb()) {
					if (item.getMapId() == ub.getMapId() && ub.isNowUb()) {
						ck = true;
						break;
					}
				}
			}
			if (ck)
				continue;
			List<L1PcInstance> players = null;
			players = L1World.getInstance().getVisiblePlayer(item, 3);
			if (players.size() < 1) { // 지정 범위내에 플레이어가 없으면 삭제
				L1Inventory groundInventory = L1World
						.getInstance()
						.getInventory(item.getX(), item.getY(), item.getMapId());
				groundInventory.removeItem(item);
				numOfDeleted++;
			}
		}
	//	System.out.println("월드 맵상의 아이템을 자동 삭제. 삭제수: " + numOfDeleted);
	}
}
