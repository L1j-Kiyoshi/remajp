package l1j.server.GameSystem.NpcBuyShop;

import java.util.Random;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_NpcChatPacket;

public class NpcChatThread extends Thread {
	private static final String[] chat = { "\\\\fTf3아이템 매입합니다.",
			"\\\\fTf2무 한 매 입 !", "\\\\f=f=고 가 매 입 !", "\\\\fTf2아이템 매입합니다~",
			"\\\\f=f=정직 수동 매입해요~", "\\\\fTf3매입합니다~", "매입합니다 이리오세요~",
			"비싸게 매입합니다!", "고가 매입합니다" };
	private static final String[] worldChat = { "시장서검색", "시장상인검색", "잠수중~!",
			"시장 6시!", "시장잠수중!", "무인상점중", "시장!", "시장에있어요", "시장검색요!", "시장서검색요!",
			"시장경비병텔로~", "시장~", "" };
	private FastMap<L1NpcShopInstance, Long> _list;
	private FastTable<L1NpcShopInstance> _list2;
	private int i = 0;
	// private static final int[] time = {20000, 25000, 22000};
	private long worldchat_time = 1000;
	private long tradechat_time = 1000;
	private Random rnd = new Random(System.nanoTime());

	// private long[] time = {24000, 30000, 36000};
	public NpcChatThread() {
		super("l1j.server.GameSystem.NpcBuyShop.NpcChatThread");
		_list = new FastMap<L1NpcShopInstance, Long>();
		_list2 = new FastTable<L1NpcShopInstance>();
		start();
	}

	private static NpcChatThread _instance;

	public static NpcChatThread get() {
		if (_instance == null)
			_instance = new NpcChatThread();
		return _instance;
	}

	public void run() {
		try {
			try {
				int d = -1;
				for (FastMap.Entry<L1NpcShopInstance, Long> e = _list.head(), mapEnd = _list
						.tail(); (e = e.getNext()) != mapEnd;) {
					// for (Entry<L1NpcShopInstance, Long> e :
					// _list.entrySet()){
					long time = e.getValue();
					L1NpcShopInstance npc = e.getKey();
					d++;
					try {
						String test1 = chat[d];
						long test = 20000 + (rnd.nextInt(10) * 1000);// NpcChatThread.time[d];
						test1 = null;
					} catch (Exception e1) {
						continue;
					}
					if (npc == null) {
						_list.remove(npc);
						_list2.remove(npc);
						continue;
					}
					if (time < System.currentTimeMillis()) {
						if (npc.getState() == 3 && npc.show) {
							if (npc.getNormalChat() != null
									&& !npc.getNormalChat()
											.equalsIgnoreCase("")
									&& !npc.getNormalChat().equalsIgnoreCase(
											"null"))
								Broadcaster.broadcastPacket(
										npc,
										new S_NpcChatPacket(npc, npc
												.getNormalChat(), 0), true);
							// if(rnd.nextInt(2) == 0)
							// GeneralThreadPool.getInstance().schedule(new
							// worldchat(npc), 20000);
						}
						_list.put(npc, System.currentTimeMillis() + 20000
								+ (rnd.nextInt(20) * 1000));
					}
				}

				/*
				 * if (L1World.getInstance().isWorldChatElabled()) {
				 * if(worldchat_time < System.currentTimeMillis()){
				 * Collection<L1NpcShopInstance> list =
				 * L1World.getInstance().getAllNpcShop(); if(list.size() > 0){
				 * L1NpcShopInstance[] npc = (L1NpcShopInstance[])
				 * list.toArray(new L1NpcShopInstance[list.size()]);
				 * L1NpcShopInstance ns = npc[rnd.nextInt(npc.length)]; if(ns !=
				 * null && ns._state == 1 && ns.getShopName().length() > 2){
				 * String text = ""; if(ns.getShopName().startsWith("\\")) text
				 * = ns.getShopName().substring(6); else text =
				 * ns.getShopName(); S_ChatPacket cp = new S_ChatPacket(ns,
				 * text, Opcodes.S_OPCODE_MSG, 3); for (L1PcInstance listner :
				 * L1World.getInstance().getAllPlayers()) { if
				 * (!listner.getExcludingList().contains(ns.getName())) { if
				 * (listner.isShowWorldChat()) { listner.sendPackets(cp); } } }
				 * } npc = null; } worldchat_time = System.currentTimeMillis() +
				 * 1000 * (rnd.nextInt(70)+1); } if(tradechat_time <
				 * System.currentTimeMillis()){ Collection<L1NpcShopInstance>
				 * list = L1World.getInstance().getAllNpcShop(); if(list.size()
				 * > 0){ L1NpcShopInstance[] npc = (L1NpcShopInstance[])
				 * list.toArray(new L1NpcShopInstance[list.size()]);
				 * L1NpcShopInstance ns = npc[rnd.nextInt(npc.length)]; if(ns !=
				 * null && ns._state == 1 && ns.getShopName().length() > 2){
				 * String text = ""; if(ns.getShopName().startsWith("\\")) text
				 * = ns.getShopName().substring(6); else text =
				 * ns.getShopName(); S_ChatPacket cp = new S_ChatPacket(ns,
				 * text+" "+worldChat[rnd.nextInt(worldChat.length)],
				 * Opcodes.S_OPCODE_MSG, 12); for (L1PcInstance listner :
				 * L1World.getInstance().getAllPlayers()) { if
				 * (!listner.getExcludingList().contains(ns.getName())) { if
				 * (listner.isShowTradeChat()) { listner.sendPackets(cp); } } }
				 * } npc = null; } tradechat_time = System.currentTimeMillis() +
				 * 1000 * (rnd.nextInt(10)+1); } }
				 */
			} catch (Exception e) {
				e.printStackTrace();
			}
			GeneralThreadPool.getInstance().schedule(this, 100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class worldchat implements Runnable {

		private L1NpcShopInstance npc;

		public worldchat(L1NpcShopInstance _npc) {
			npc = _npc;
		}

		@Override
		public void run() {
			try {

				if (!npc.show)
					return;
				if (L1World.getInstance().isWorldChatElabled()) {
					// if(worldchat_time < System.currentTimeMillis()){
					S_ChatPacket cp;
					rnd.setSeed(System.currentTimeMillis());
					if (rnd.nextInt(1000) > 500)
						cp = new S_ChatPacket(npc, npc.getShopName(),
								Opcodes.S_MESSAGE, 12);
					else
						cp = new S_ChatPacket(npc, npc.getShopName(),
								Opcodes.S_MESSAGE, 3);
					for (L1PcInstance listner : L1World.getInstance()
							.getAllPlayers()) {
						if (!listner.getExcludingList().contains(npc.getName())) {
							if (listner.isShowWorldChat())
								listner.sendPackets(cp);
						}
					}
					// }
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void add(L1NpcShopInstance npc) {
		_list.put(npc,
				20000 + (rnd.nextInt(10) * 1000) + System.currentTimeMillis());
		_list2.add(npc);
	}

	public L1NpcShopInstance[] toArray() {
		return _list2.toArray(new L1NpcShopInstance[_list2.size()]);
	}
}
