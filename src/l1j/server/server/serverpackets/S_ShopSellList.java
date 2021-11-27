package l1j.server.server.serverpackets;

import java.io.IOException;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.GameSystem.DogFight.DogFight;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

public class S_ShopSellList extends ServerBasePacket {

	/**
	 * 가게의 물건 리스트를 표시한다. 캐릭터가 BUY 버튼을 눌렀을 때에 보낸다.
	 */
	public S_ShopSellList(int objId, L1PcInstance pc) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(objId);

		L1Object npcObj = L1World.getInstance().findObject(objId);
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();

		L1TaxCalculator calc = new L1TaxCalculator(npcId);

		L1Shop shop = null;
		if (npcId == 100800) {
			shop = ShopTable.getInstance().get(pc.getType());
		} else {
			shop = ShopTable.getInstance().get(npcId);
		}
		FastTable<L1ShopItem> shopItems = new FastTable<L1ShopItem>();
		try {
			shopItems.addAll(shop.getSellingItems());
			for (L1ShopItem si : shopItems.toArray(new L1ShopItem[shopItems
					.size()])) {
				if (L1MerchantInstance.getOneDayBuy(pc.getAccountName(), npcId,
						si.getItemId())) {
					shopItems.remove(si);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*
		 * List<L1ShopItem> shopItems = null; try { shopItems =
		 * shop.getSellingItems(); } catch (Exception e) { System.out.println(
		 * "S_ShopSellList 오류 엔피시번호 : "+npcId); }
		 */
		if (shopItems == null || shopItems.size() < 1) {
			writeH(0);
			return;
		}

		// if(shopItems != null){
		writeH(shopItems.size());
		// }

		// L1ItemInstance의 getStatusBytes를 이용하기 위해(때문에)
		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = shopItems.get(i);
			item = shopItem.getItem();
			int price = calc.layTax((int) (shopItem.getPrice() * Config.RATE_SHOP_SELLING_PRICE));
			int price1 = (int) (shopItem.getPrice()); // BUY를 눌러 실제 인벤에서 사라지는 아데나의 양 계산부분 
			writeD(i);
			try {
				writeH(shopItem.getItem().getGfxId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (npcId == 70035 || npcId == 70041 || npcId == 70042 | npcId == 72320) {
				writeD(price1); // 버그상점 이라면 price1 만큼 가져온다
			} else {
				writeD(price); // 그외에 price 만큼 가져온다
			}
			if (shopItem.getPackCount() > 1) {
				writeS(item.getNameId() + " (" + shopItem.getPackCount() + ")");
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
					writeS("축복 " + item.getName());
				else if (item.getItemId() >= 240074 && item.getItemId() <= 240087)
					writeS("저주 " + item.getName());
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

		if (npcId == 70014) {
			writeH(0x4547);// 0x00:kaimo 0x01:pearl 0x07:adena
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
