package server.threads.pc;

import l1j.server.GameSystem.NpcBuyShop.NpcBuyShop;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1ShopItem;

public class ItemEndTimeThread implements Runnable {

	private static ItemEndTimeThread _instance;

	public static ItemEndTimeThread get() {
		if (_instance == null) {
			_instance = new ItemEndTimeThread();
		}
		return _instance;
	}

	public ItemEndTimeThread() {
	//	System.out.println(":: ItemEndTimeThread Loading Compleate");
		GeneralThreadPool.getInstance().schedule(this, 10000);
	}

	@Override
	public void run() {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null)
					continue;
				pc.getInventory().checkEndTime();
			}
			for (L1NpcShopInstance npc : L1World.getInstance().getAllNpcShop()) {
				if (npc == null || npc.getState() == 1) {
					L1Shop shop = NpcBuyShop.getInstance().get(npc.getNpcId());
					if (shop == null)
						continue;
					L1ShopItem[] list = shop.getSellingItems().toArray(
							new L1ShopItem[shop.getSellingItems().size()]);
					for (L1ShopItem item : list) {
						if (item.getDeleteTime() != null) {
							if (item.getDeleteTime().getTime() < System
									.currentTimeMillis()) {
								shop.getSellingItems().remove(item);
								shop.NpcShopEtc(item);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, 10000);
	}
}
