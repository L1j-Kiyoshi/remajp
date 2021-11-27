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
package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.GameSystem.DogFight.DogFight;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

public class S_NoTaxShopSellList extends ServerBasePacket {

	/**
	 * 가게의 물건 리스트를 표시한다. 캐릭터가 BUY 버튼을 눌렀을 때에 보낸다.
	 */
	public S_NoTaxShopSellList(int objId, L1PcInstance pc) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(objId);
		L1Object npcObj = L1World.getInstance().findObject(objId);
		if (npcObj == null)
			return;
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();

		// L1TaxCalculator calc = new L1TaxCalculator(npcId);
		L1Shop shop = null;
		if (npcId == 100800) {
			shop = ShopTable.getInstance().get(pc.getType());
		} else {
			shop = ShopTable.getInstance().get(npcId);
		}

		if (shop == null || shop.getSellingItems().size() < 1) {
			writeH(0);
			return;
		}

		ArrayList<L1ShopItem> shopItems = new ArrayList<L1ShopItem>();
		try {
			shopItems.addAll(shop.getSellingItems());
			for (L1ShopItem si : shopItems.toArray(new L1ShopItem[shopItems.size()])) {
				if (L1MerchantInstance.getOneDayBuy(pc.getAccountName(), npcId, si.getItemId())) {
					shopItems.remove(si);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		writeH(shopItems.size());
		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = shopItems.get(i);
			item = shopItem.getItem();

			int price = (int) (shopItem.getPrice() * Config.RATE_SHOP_SELLING_PRICE);

			writeD(i);
			/*
			 * writeH(ItemClientCode.code(item.getItemId())); //0539 if(item.getItemId() ==
			 * 40008){ writeH(0x0827); }else{ writeC(item.getUseType()); writeC(0); }
			 */
			writeH(shopItem.getItem().getGfxId());
			writeD(price);
			if (shopItem.getPackCount() > 1) {
				writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
			} else if (shopItem.getEnchant() > 0) {
				writeS("+" + shopItem.getEnchant() + " " + item.getName());
			} else if (npcId == 70035 || npcId == 70041 || npcId == 70042) {
				writeS(L1BugBearRace.getInstance().getTicketName(i));
			} else if (npcId == 72320) {
				writeS(DogFight.getInstance().getTicketName(i));
			} else if (shopItem.getItem().getMaxUseTime() > 0) {
				writeS(item.getName() + " [" + item.getMaxUseTime() + "]");
			} else {
				if (item.getItemId() >= 140074 && item.getItemId() <= 140100)
					writeS("\\aD축복 " + item.getName());
				else if (item.getItemId() >= 240074 && item.getItemId() <= 240087)
					writeS("\\fC저주 " + item.getName());
				else
					writeS(item.getName());
			}

			if (item.getType() == 6 && item.getType2() == 0) {
				writeD(0x33);
			} else if (item.getType() == 15 && item.getType2() == 2) {
				writeD(0x02);
			} else if (item.getType() == 8 && item.getType2() == 0) {
				writeD(0x6);
			} else {
				writeD(item.getUseType());
			}

			template = ItemTable.getInstance().getTemplate(item.getItemId());
			if (template == null) {
				writeC(0);
			} else {
				dummy.setItem(template);
				byte[] status = dummy.getStatusBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			}
		}
		if (npcId == 77221) {
			writeH(16661);// 0x00:kaimo 0x01:pearl 0x07:adena
		} else {
			writeH(0x07);// 0x00:kaimo 0x01:pearl 0x07:adena
		}
		if (npcId == 77223) {
			writeH(17839);// 0x00:kaimo 0x01:pearl 0x07:adena
		} else {
			writeH(0x07);// 0x00:kaimo 0x01:pearl 0x07:adena
		}
		if (npcId == 70014) {
			writeH(0x4547);// 0x00:kaimo 0x01:pearl 0x07:adena
		} else {
			writeH(0x07);// 0x00:kaimo 0x01:pearl 0x07:adena
		}
		/** 기사단의 주화 */
		if (npcId >= 77227 && npcId <= 77230) {
			writeH(26532);// 0x00:kaimo 0x01:pearl 0x07:adena
		} else {
			writeH(0x07);// 0x00:kaimo 0x01:pearl 0x07:adena
		}
		shopItems.clear();
		shopItems = null;
	}

	@Override
	public byte[] getContent() throws IOException {
		return _bao.toByteArray();
	}
}
