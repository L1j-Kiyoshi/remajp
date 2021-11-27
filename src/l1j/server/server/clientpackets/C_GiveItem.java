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

package l1j.server.server.clientpackets;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import server.LineageClient;

public class C_GiveItem extends ClientBasePacket {
	private static final String C_GIVE_ITEM = "[C] C_GiveItem";

	public C_GiveItem(byte decrypt[], LineageClient client) {
		super(decrypt);
		try {
			int targetId = readD();
			@SuppressWarnings("unused")
			int x = readH();
			@SuppressWarnings("unused")
			int y = readH();
			int itemId = readD();
			int count = readD();

			L1PcInstance pc = client.getActiveChar();
			L1Object object = L1World.getInstance().findObject(targetId);
			L1NpcInstance target = (L1NpcInstance) object;
			L1Inventory targetInv = target.getInventory();
			L1Inventory inv = pc.getInventory();
			L1ItemInstance item = inv.getItem(itemId);

			if (pc.isGhost()) {
				return;
			}
			if (object == null || item == null) {
				return;
			}
			if (!isNpcItemReceivable(target.getNpcTemplate())) {
				if (!(item.getItem().getItemId() == 40499)
						|| !(item.getItem().getItemId() == 40507)) {
					return;
				}
			}
			if (item.isEquipped()) {
				pc.sendPackets(new S_SystemMessage("착용하고 있는 것을 줄수 없습니다."), true); // \f1장비
																					// 하고
																					// 있는
																					// 것은,
																					// 사람에게
																					// 건네줄
																					// 수가
																					// 없습니다.
				return;
			}
			if (item.getBless() >= 128) {// 봉인
				pc.sendPackets(new S_SystemMessage("착용하고 있는 것을 줄수 없습니다."), true); // \f1장비
																					// 하고
																					// 있는
																					// 것은,
																					// 사람에게
																					// 건네줄
																					// 수가
																					// 없습니다.
				return;
			}
			// if (pc.getAccessLevel() == Config.GMCODE){
			// return;
			// }
			if (itemId != item.getId()) {
				pc.sendPackets(new S_Disconnect(), true);
				return;
			}
			if (!item.isStackable() && count != 1) {
				pc.sendPackets(new S_Disconnect(), true);
				return;
			}
			if (item.getCount() <= 0 || count <= 0) {
				pc.sendPackets(new S_Disconnect(), true);
				return;
			}
			if (count >= item.getCount()) {
				count = item.getCount();
			}
			
			/** 아이템 오브젝트가 펫목걸이고 그목거리가 망각이 아니라면 */
			if (item.isDogNecklace() && !pc.isGm()) {
				// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(210, item.getViewName()), true);
				return;
			}
			
			if (item.getItem().getItemId() == 49312
					|| item.getItem().getItemId() == 40312) {
				pc.sendPackets(new S_SystemMessage(
						"여관열쇠는 /교환을 이용하거나 창고를 이용해주세요."));
				return;
			}
			if (item.getItem().getItemId() == 423012
					|| item.getItem().getItemId() == 423013) { // 10주년티
				pc.sendPackets(new S_ServerMessage(210, item.getItem()
						.getName()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				return;
			}
			if (!item.getItem().isTradable()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem()
						.getName()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				return;
			}
			L1PetInstance pet = null;
			for (Object petObject : pc.getPetList()) {
				if (petObject instanceof L1PetInstance) {
					pet = (L1PetInstance) petObject;
					if (item.getId() == pet.getItemObjId()) {
						// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
						pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()), true);
						return;
					}
				}
			}
			if (target instanceof L1MonsterInstance
					|| target instanceof L1SummonInstance
					|| target instanceof L1PetInstance) {
				if (!pc.isGm() && item.getItemId() == 40308) {
					pc.sendPackets(new S_ServerMessage(210, item.getItem()
							.getName()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수
												// 없습니다.
					return;
				}
				if (target.getNpcTemplate().get_npcId() == 100003
						|| target.getNpcTemplate().get_npcId() == 100814) {
					if (item.getItemId() == 40054
							&& ((L1MonsterInstance) target).shellManClose) {
						((L1MonsterInstance) target).shellManClose = false;
						Broadcaster.broadcastPacket(target, new S_DoActionGFX(
								target.getId(), 3), true);
						target.setActionStatus(0);
						Broadcaster.broadcastPacket(target,
								new S_CharVisualUpdate(target), true);
						inv.removeItem(item, count);
						return;
					}
				}
			}

			if (!pc.isGm()) {
				if (targetInv.checkAddItem(item, count) != L1Inventory.OK) {
					pc.sendPackets(new S_ServerMessage(942), true); // 상대의 아이템이
																	// 너무 무겁기
																	// (위해)때문에,
																	// 더 이상 줄 수
																	// 없습니다.
					return;
				}
			}

			item = inv.tradeItem(item, count, targetInv);
			target.onGetItem(item);
			target.getLight().turnOnOffLight();
			pc.getLight().turnOnOffLight();
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private final static String receivableImpls[] = new String[] { "L1Npc", // NPC
			"L1Monster", // monster
			"L1Guardian", // 요정 숲의 수호자
			"L1Teleporter", // 텔레 포터
			"L1Guard" }; // 가이드

	private boolean isNpcItemReceivable(L1Npc npc) {
		for (String impl : receivableImpls) {
			if (npc.getImpl().equals(impl)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public String getType() {
		return C_GIVE_ITEM;
	}
}
