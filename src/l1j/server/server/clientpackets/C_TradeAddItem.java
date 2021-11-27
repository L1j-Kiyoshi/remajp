/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be trading_partnerful,
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

import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BuffNpcInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import server.LineageClient;

public class C_TradeAddItem extends ClientBasePacket {
	

	private static final String C_TRADE_ADD_ITEM = "[C] C_TradeAddItem";

	public C_TradeAddItem(byte abyte0[], LineageClient client) throws Exception {

		super(abyte0);
		try {
			int itemid = readD();
			int itemcount = readD();
			L1PcInstance pc = client.getActiveChar();
			L1Trade trade = new L1Trade();
			L1ItemInstance item = pc.getInventory().getItem(itemid);

			if (item == null)
				return;
			if (itemid != item.getId()) {
				return;
			}
			if (!item.isStackable() && itemcount != 1) {
				return;
			}
			if (itemcount <= 0 || item.getCount() <= 0) {
				return;
			}
			if (itemcount > item.getCount()) {
				itemcount = item.getCount();
			}
			if (itemcount > 2000000000) {
				return;
			}
			if (!item.getItem().isTradable()) {
				S_ServerMessage sm = new S_ServerMessage(210, item.getItem()
						.getName());
				pc.sendPackets(sm, true);
				return;
			}
			if (item.getBless() >= 128) {
				S_ServerMessage sm = new S_ServerMessage(210, item.getItem()
						.getName());
				pc.sendPackets(sm, true);
				return;
			}
			if (item.isEquipped()) {
				S_ServerMessage sm = new S_ServerMessage(906);
				pc.sendPackets(sm, true);
				return;
			}
			
			/** 아이템 오브젝트가 펫목걸이고 그목거리가 망각이 아니라면 */
			if (item.isDogNecklace() && !pc.isGm()) {
				// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(210, item.getViewName()), true);
				return;
			}
			
			/** 펫 이나 인형을 소환하고있다면 */
			if (!checkPetList(pc, item)){
				// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(210, item.getViewName()), true);
				return;
			}
			
			if (pc.getTradeOk())return;

			L1Object tradingPartner = L1World.getInstance().findObject(pc.getTradeID());
			if (tradingPartner == null) return; 
			if (tradingPartner instanceof L1PcInstance) {
				L1PcInstance target = (L1PcInstance) tradingPartner;
				if (pc.getTradeOk() || target.getTradeOk()) {
					return;
				}
				if (target.getInventory().checkAddItem(item, itemcount) != L1Inventory.OK) {
					S_ServerMessage sm = new S_ServerMessage(270);
					target.sendPackets(sm, true);
					S_ServerMessage sm1 = new S_ServerMessage(271);
					pc.sendPackets(sm1, true);
					return;
				}
			} else if (tradingPartner instanceof L1BuffNpcInstance) {
				L1BuffNpcInstance target = (L1BuffNpcInstance) tradingPartner;
				if (pc.getTradeOk() || target.getTradeOk()) {
					return;
				}
			} else if (tradingPartner instanceof L1NpcShopInstance) {
				if (pc.getTradeOk()) {
					return;
				}
			} else if (tradingPartner instanceof GambleInstance) {
				if (pc.getTradeOk()) {
					return;
				}
			}
			trade.TradeAddItem(pc, itemid, itemcount);
		} catch (Exception e) {

		} finally {
			clear();
		}
	}
	
	private boolean checkPetList(L1PcInstance pc, L1ItemInstance item) {
		L1DollInstance doll = null;
		for (Object dollObject : pc.getDollList()) {
			doll = (L1DollInstance) dollObject;
			if (item.getId() == doll.getItemObjId()) {
				pc.sendPackets(new S_ServerMessage(1181), true); //
				return false;
			}
		}
		for (Object petObject : pc.getPetList()) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					pc.sendPackets(new S_ServerMessage(210, item.getViewName()), true);
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getType() {
		return C_TRADE_ADD_ITEM;
	}
}
