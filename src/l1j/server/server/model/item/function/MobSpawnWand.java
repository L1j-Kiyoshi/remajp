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
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.L1SpawnUtil;

@SuppressWarnings("serial")
public class MobSpawnWand extends L1ItemInstance {

	private static Random _random = new Random(System.nanoTime());

	private static final int[] mobArray = { 45005, 45009, 45019, 45041, 45043,
			45065, 45068, 45157, 45082, 45024, 45107, 45161, 45126,
			45136, 45184, 45215, 45223, 45021, 45008, 45016, 45025, 45033,
			45046, 45064, 45040, 45147, 45140, 45092, 45155, 45192, 45122,
			45130, 45138, 45213, 45173, 45171, 45143, 45149, 45098, 45127,
			45144, 45079 };

	/*
	 * 소나무 막대에서 나오는 기존의 몬스터는 총 18가지로 개구리,오크,오크궁수,난쟁이,늑대,
	 * 슬라임,좀비,구울,괴물눈,오크전사,늑대인간,해골,스파토이,돌골렘,셀로브,웅골리언트,장로,버그베어
	 * 
	 * 였으며 이번 업데이트로인해 나오는 몬스터의 숫자는 총 43가지로 25마리가 추가되었다.
	 * 
	 * 추가된 몬스터는 사슴,고블린,코볼트,비글,그렘린,임프,임프장로,곰,아울베어,홉고블린,
	 * 난쟁이족전사,헐거인간,랫맨,해골궁수,해골도끼병,오크스카우트,가스트,라이칸스로프,
	 * 두다마라오크,네루가오크,로바오크,간디오크,아투바오크,리자드맨,놀
	 */

	public MobSpawnWand(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			// int itemId = this.getItemId();

			if (pc.getMap().isSafetyZone(pc.getLocation())) {
				S_AttackPacket s_attackStatus = new S_AttackPacket(pc, 0,
						ActionCodes.ACTION_Wand);
				pc.sendPackets(s_attackStatus);
				Broadcaster.broadcastPacket(pc, s_attackStatus, true);
				pc.sendPackets(new S_SystemMessage(
						"마을안에서는 소나무 막대를 사용 할 수 없습니다."), true);
				return;
			}

			if (pc.getMapId() == 622 /*
									 * ||
									 * pc.getMap().isCombatZone(pc.getLocation
									 * ())
									 */) { // 깃털마을이나 컴뱃존
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
				/*
				 * if (chargeCount <= 0 && itemId != 40412) { // \f1 아무것도 일어나지
				 * 않았습니다. pc.sendPackets(new
				 * S_SystemMessage("아무일도 일어나지 않았습니다."), true); return; }
				 */

				int rnd = _random.nextInt(mobArray.length);
				L1SpawnUtil.spawn(pc, mobArray[rnd], 0, 300000, true);

				/*
				 * if (itemId == 40006 || itemId == 140006) {
				 * useItem.setChargeCount(useItem.getChargeCount() - 1);
				 * pc.getInventory().updateItem(useItem,
				 * L1PcInventory.COL_CHARGE_COUNT); } else {
				 */
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
