package l1j.server.GameSystem.Gamble;

import java.util.Random;

import l1j.server.server.model.Broadcaster;
import l1j.server.server.serverpackets.S_NpcChatPacket;

public class GambleChatThread extends Thread {

	private Random rnd;
	private static GambleChatThread _instance;

	public static GambleChatThread get() {
		if (_instance == null)
			_instance = new GambleChatThread();
		return _instance;
	}

	public GambleChatThread() {
		super("l1j.server.GameSystem.Gamble.GambleChatThread");
		rnd = new Random(System.nanoTime());
		start();
	}

	public void run() {
		while (true) {
			try {
				GambleInstance[] list = Gamble.get().toArray();
				if (list == null || list.length <= 0)
					continue;

				for (GambleInstance gam : list) {
					if (gam == null || !gam.getShow()
							|| gam.getChatMsg() == null
							|| gam.getChatMsg().equals(""))
						continue;
					if (System.currentTimeMillis() > gam.msgTime) {
						if (gam.play) { // 진행중
						} else if (gam.Npc_trade) { // 거래 중일때
							if (gam.getType() == 1) {
								// Broadcaster.broadcastPacket(gam, new
								// S_NpcChatPacket(gam, "1만 아덴 이상만 올려주세용~", 0));
							}
						} else { // 평소 챗
							Broadcaster.broadcastPacket(gam,
									new S_NpcChatPacket(gam, gam.getChatMsg(),
											0), true);
						}
						rnd.setSeed(System.nanoTime());
						gam.msgTime = System.currentTimeMillis()
								+ rnd.nextInt(10000) + 10000;
					}
				}
				list = null;
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
