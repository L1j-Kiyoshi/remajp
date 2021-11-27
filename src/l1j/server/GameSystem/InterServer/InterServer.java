package l1j.server.GameSystem.InterServer;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import server.LineageClient;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_SelectCharacter;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_ServerVersion;
import l1j.server.server.serverpackets.S_Unknown1;
public class InterServer implements Runnable {

	private static InterServer _instance;
	private static Queue<LineageClient> _queue;

	public static InterServer getInstance() {
		if (_instance == null) {
			_instance = new InterServer();
		}
		return _instance;
	}

	public InterServer() {
		_queue = new ConcurrentLinkedQueue<LineageClient>();
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	public static void RequestInterServer(LineageClient Client) {
		_queue.offer(Client);
	}

	@Override
	public void run() {
		while (true) {
			try {
				LineageClient Client; L1PcInstance Player;
				String InterServerName;
				
				synchronized (this){
					try {
						Thread.sleep(10L);
						Client = _queue.poll();
						if(Client == null) continue;
						
						InterServerName = Client.getInterServerName();
						Player = L1World.getInstance().getPlayer(InterServerName);
						if(Player == null){
							/** 케릭터 정보  갱신 */
							Client.sendPacket(new S_ServerVersion(S_ServerVersion.ServerInter, 1, null));
							Client.sendPacket(new S_LoginResult());
							Client.sendPacket(new S_Unknown1(0));
					
							/** 정리가 끝났다면 로그인 패킷 으로 처리 */
							new C_SelectCharacter(Client, Client.getInterServerName());
							
							/** 완료 되었다면 인터서버 메모리 정보 리셋 살짝 시켜주도록 */
							Client.setInterServerName(null);
						}else _queue.offer(Client);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}