package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;

public class S_PremiumShopSellList extends ServerBasePacket {

	/**
	 * 가게의 물건 리스트를 표시한다. 캐릭터가 BUY 버튼을 눌렀을 때에 보낸다.
	 */
	public S_PremiumShopSellList(int objId, L1PcInstance pc) {
		writeC(Opcodes.S_BUY_LIST);
		writeD(objId);

		L1Object npcObj = L1World.getInstance().findObject(objId);
		if (!(npcObj instanceof L1NpcInstance)) {
			writeH(0);
			return;
		}
		int npcId = ((L1NpcInstance) npcObj).getNpcTemplate().get_npcId();
		L1Shop shop = ShopTable.getInstance().get(npcId);
		L1Shop evans = ShopTable.getInstance().get(pc.getType() + 10);
		L1Shop psymon = ShopTable.getInstance().get(pc.getType() + 30);
		List shopItems = null;
		try {
			if (npcId == 77228) {
				shopItems = evans.getSellingItems();
			} else if (npcId == 77230) {
				shopItems = psymon.getSellingItems();
			} else {
				shopItems = shop.getSellingItems();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (shopItems != null) {
			writeH(shopItems.size());
		} else {
			writeH(0);
			return;
		}

		// L1ItemInstance의 getStatusBytes를 이용하기 위해(때문에)
		L1ItemInstance dummy = new L1ItemInstance();
		L1ShopItem shopItem = null;
		L1Item item = null;
		L1Item template = null;
		for (int i = 0; i < shopItems.size(); i++) {
			shopItem = (L1ShopItem) shopItems.get(i);
			item = shopItem.getItem();
			int price = shopItem.getPrice();
			writeD(i);
			try {
				writeH(shopItem.getItem().getGfxId());
			} catch (Exception e) {
				e.printStackTrace();
			}
			writeD(price);
			if (shopItem.getPackCount() > 1) {
				writeS(item.getName() + " (" + shopItem.getPackCount() + ")");
			} else if (shopItem.getEnchant() > 0) {
				writeS("+" + shopItem.getEnchant() + " " + item.getName());
			} else if (shopItem.getItem().getMaxUseTime() > 0) {
				writeS(item.getName() + " [" + item.getMaxUseTime() + "]");
			} else {
				writeS(item.getName());
			}
			int type = shopItem.getItem().getUseType();
			if (type < 0) {
				type = 0;
			}
			writeD(type);
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
		// 탐상인
		if ((npcId == 100725)) {
			writeC(253);
			writeC(255);
			// 베리
		} else if ((npcId == 100605)) {
			writeC(73);
			writeC(58);
		} else if ((npcId == 77221)) {
			writeH(0x4115);
		} else if ((npcId == 77223)) {
			writeH(0x45af);
		} else if ((npcId == 70014)) {
			writeH(0x4547);
			// 알수없음 0/0
			/** 기사단의 주화 */
		} else if (npcId >= 77227 && npcId <= 77230) {
			writeH(0x67a4);
		} else if ((npcId == 900107)) { // 만월의 정기인데??..
			// writeC(0x2c08); //desc번호 인데..안되네..ㅋ
			writeC(255);
			writeC(255);
			writeC(0);
			writeC(0);
			// 금빛깃털
		} else if ((npcId == 6000002)) {
			writeH(0x3ccf);
		} else if ((npcId >= 7210061 && npcId <= 7210070)) {
			writeH(0x3DE4);
		} else {// 그외 깃털로 표시
			writeC(111);
			writeC(10);
		}
	}

	@Override
	public byte[] getContent() throws IOException {
		return _bao.toByteArray();
	}
}
