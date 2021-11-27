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

package l1j.server.server.model.item.function;

import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.L1SpawnUtil;

@SuppressWarnings("serial")
public class BossMobSpawnWand extends L1ItemInstance {

	private static Random _random = new Random(System.nanoTime());

	public BossMobSpawnWand(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			if (pc.getMap().isSafetyZone(pc.getLocation())) {
				S_AttackPacket s_attackStatus = new S_AttackPacket(pc, 0,
						ActionCodes.ACTION_Wand);
				pc.sendPackets(s_attackStatus);
				Broadcaster.broadcastPacket(pc, s_attackStatus, true);
				pc.sendPackets(new S_SystemMessage(
						"마을안에서는 보스 소환 막대를 사용 할 수 없습니다."), true);
				return;
			}
			int pc_castleId = L1CastleLocation.getCastleIdByArea(pc);
			if (pc_castleId >= 1 && pc_castleId <= 8) {
				pc.sendPackets(new S_SystemMessage(
						"공성존에서는 보스 소환 막대를 사용 할 수 없습니다."), true);
				return;
			}

			if (pc.getMap().isCombatZone(pc.getLocation())) {
				S_AttackPacket s_attackStatus = new S_AttackPacket(pc, 0,
						ActionCodes.ACTION_Wand);
				pc.sendPackets(s_attackStatus);
				Broadcaster.broadcastPacket(pc, s_attackStatus, true);
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
			if (pc.getMap().isUsePainwand()) {
				S_AttackPacket s_attackStatus = new S_AttackPacket(pc, 0,
						ActionCodes.ACTION_Wand);
				pc.sendPackets(s_attackStatus);
				Broadcaster.broadcastPacket(pc, s_attackStatus, true);
				// int chargeCount = useItem.getChargeCount();
				// if (chargeCount <= 0 && itemId != 40412) {
				// \f1 아무것도 일어나지 않았습니다.
				// pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
				// true);
				// return;
				// }
				/*
				 * int[] mobArray = { 45773, 45774, 45775, 45776, 45777, 45778,
				 * 45779, 45962, 45961, 45958, 45957, 45960, 45956, 45959,
				 * 81039, 81040, 81041, 81038, 45618, 45643, 45644, 45645,
				 * 45642, 45617, 45622, 45621, 45620, 45651, 45590, 45801,
				 * 45601, 45600, 45652, 45653, 45654, 45672, 45673, 45545,
				 * 45583, 46025, 45671, 45488, 45464, 45473, 45497, 45614 };
				 */
				int[] mobArray = { 45513, 45547, 45606, 45618, 45650, 45652,
						45654, 45672, 81047, 100002, 45601, 45614, 45649,
						45829, 45617, 45545, 45516, 45529, 45488, 45464, 45473,
						45497, 45456, 45642, 45643, 45644, 45645, 45610, 45584,
						45583, 45573, 70984 };

				int rnd = _random.nextInt(mobArray.length);
				L1SpawnUtil.spawn(pc, mobArray[rnd], 0, 300000, false);

				// if (itemId == 6000033) {
				// useItem.setChargeCount(useItem.getChargeCount() - 1);
				// pc.getInventory().updateItem(useItem,
				// L1PcInventory.COL_CHARGE_COUNT);
				// } else {
				pc.getInventory().removeItem(useItem, 1);
				// }

				// if (useItem.getChargeCount() == 0) {
				// pc.getInventory().removeItem(useItem);
				// }

				/** 2011.07.01 고정수 수량성 아이템 미확인으로 되는 문제 */
				if (useItem.isIdentified()) {
					useItem.setIdentified(true);
					pc.sendPackets(new S_ItemName(useItem), true);
				}
			} else {
				// \f1 아무것도 일어나지 않았습니다.
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
			}
		}
	}
}
