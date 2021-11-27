package l1j.server.GameSystem.NpcTradeShop;

import java.util.Random;

import javolution.util.FastMap;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.serverpackets.S_NpcChatPacket;

public class NpcTradeChatThread extends Thread {

	private FastMap<L1NpcShopInstance, Long> _list;
	private int i = 0;
	private static final int[] time = { 20000, 25000, 22000 };
	private Random rnd = new Random(System.nanoTime());

	public NpcTradeChatThread() {
		super("l1j.server.GameSystem.NpcBuyShop.NpcChatThread");
		_list = new FastMap<L1NpcShopInstance, Long>();
		start();
	}

	private static NpcTradeChatThread _instance;

	public static NpcTradeChatThread get() {
		if (_instance == null)
			_instance = new NpcTradeChatThread();
		return _instance;
	}

	public void run() {
		try {
			Thread.sleep(5000L);
			while (true) {
				try {
					int d = -1;
					for (ShopItem shop : NpcTradeShop.getInstance().getList()) {
						if (shop == null)
							continue;
						L1NpcShopInstance npc = L1World.getInstance()
								.getNpcShop(shop.getNpcId());
						if (npc == null) {
							try {
								_list.remove(npc);
							} catch (Exception e) {
							}
							continue;
						}
						String text = npc.npcTradeSellChat.size() > 0 ? npc.npcTradeSellChat
								.get(rnd.nextInt(npc.npcTradeSellChat.size()))
								: null;
						if (npc.Npc_trade || text == null)
							continue;
						long time = 0;
						try {
							time = _list.get(npc);
						} catch (Exception e) {
						}
						d++;
						try {
							long test = NpcTradeChatThread.time[d];
						} catch (Exception e1) {
							continue;
						}
						if (time < System.currentTimeMillis()) {
							if (npc.show)
								Broadcaster.broadcastPacket(npc,
										new S_NpcChatPacket(npc, text/*
																	 * shop.getMsg
																	 * ()
																	 */, 0),
										true);
							_list.put(npc, System.currentTimeMillis()
									+ NpcTradeChatThread.time[d]);
						}
						text = null;
					}
					sleep(100);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
