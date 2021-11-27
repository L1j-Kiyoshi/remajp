package l1j.server.GameSystem.NpcTradeShop;

import javolution.util.FastTable;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.templates.L1Npc;

public class NpcTradeShop {

	private FastTable<ShopItem> list;
	private FastTable<L1NpcShopInstance> npcList;

	private static NpcTradeShop _instance;

	public static NpcTradeShop getInstance() {
		if (_instance == null) {
			_instance = new NpcTradeShop();
		}
		return _instance;
	}

	private NpcTradeShop() {
		NpcTradeChatThread.get();
		/** 아이템 가격 셋팅 **/
		list = NpcTradeShopTable.getInstance().load();
		npcList = new FastTable<L1NpcShopInstance>();
		npcCreate();
		npcChatString();
	//	System.out.println(":: NpcTradeShop Loading Compleate");
	}

	public void reload() {
		try {
			if (list != null && list.size() > 0)
				list.clear();
			BuyShop_Delete();
			if (npcList != null && npcList.size() > 0)
				npcList.clear();
			list = NpcTradeShopTable.getInstance().load();
			npcList = new FastTable<L1NpcShopInstance>();
			npcCreate();
			npcChatString();
		} catch (Exception e) {
		}
	}

	private void npcChatString() {
		for (ShopItem shop : list) {
			try {
				if (shop == null)
					continue;
				L1NpcShopInstance npc = null;
				for (L1NpcShopInstance np : npcList) {
					if (np == null)
						continue;
					if (shop.getNpcId() == np.getNpcId()) {
						npc = np;
						break;
					}
				}
				if (npc == null || shop.getMsg() == null
						|| shop.getMsg().equalsIgnoreCase(""))
					continue;
				npc.npcTradeSellChat.add(shop.getMsg());
			} catch (Exception e) {
			}
		}
	}

	private void npcCreate() {
		for (ShopItem shop : list) {
			try {
				if (shop == null)
					continue;
				boolean ck = false;
				for (L1NpcShopInstance np : npcList) {
					if (np == null)
						continue;
					if (shop.getNpcId() == np.getNpcId()) {
						ck = true;
						break;
					}
				}
				if (ck)
					continue;
				L1Npc l1npc = NpcTable.getInstance().getTemplate(
						shop.getNpcId());
				if (l1npc == null)
					continue;
				L1NpcShopInstance npc = (L1NpcShopInstance) NpcTable
						.getInstance().newNpcInstance(shop.getNpcId());
				npc.setId(ObjectIdFactory.getInstance().nextId());
				npc.setName(l1npc.get_nameid());
				npc.setNameId(npc.getName());
				npc.setX(shop.getX());
				npc.setY(shop.getY());
				npc.setMap(shop.getMapId());
				npc.getMoveState().setHeading(shop.getHeading());
				npc.setLightSize(l1npc.getLightSize());
				npc.getLight().turnOnOffLight();
				npc.setTitle(shop.getTitle());
				npc.setTempLawful(l1npc.get_lawful());
				npc.setLawful(l1npc.get_lawful());
				npcList.add(npc);
			} catch (Exception e) {
			}
		}
	}

	public boolean BuyShop_Show_or_Delete() {
		boolean ck = false;
		for (L1NpcShopInstance npc : npcList) {
			if (!npc.show) {
				L1World.getInstance().storeObject(npc);
				L1World.getInstance().addVisibleObject(npc);
				npc.show = true;
				ck = true;
			} else {
				npc.show = false;
				npc.shopdelete();
			}
		}
		return ck;
	}

	public boolean BuyShop_Delete() {
		boolean ck = false;
		for (L1NpcShopInstance npc : npcList) {
			if (npc.show) {
				npc.show = false;
				npc.shopdelete();
			}
		}
		return ck;
	}

	public ShopItem[] getShopList() {
		ShopItem[] npc = list.toArray(new ShopItem[list.size()]);
		return npc;
	}

	public FastTable<ShopItem> getList() {
		return list;
	}
}
