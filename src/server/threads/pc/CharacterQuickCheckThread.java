package server.threads.pc;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import server.LoginController;

public class CharacterQuickCheckThread extends Thread {

	private static CharacterQuickCheckThread _instance;

	public static CharacterQuickCheckThread getInstance() {
		if (_instance == null) {
			_instance = new CharacterQuickCheckThread();
			_instance.start();
		}
		return _instance;
	}

	public CharacterQuickCheckThread() {
		super("server.threads.pc.CharacterQuickCheckThread");
	}

	public void run() {
		while (true) {
			try {
				for (L1PcInstance _client : L1World.getInstance().getAllPlayers()) {
					if (_client instanceof L1RobotInstance) continue;
					if (_client instanceof L1RobotInstance) continue;
					if (_client.isPrivateShop() || _client.getNetConnection() == null) continue;		
					if (_client.샌드백) continue;
					/** 클라이언트는 닫혀있는데 케릭이 남아있따면 */
					if (_client.getNetConnection().isClosed()) {
						/** 소켓이 닫혀있따면 파티상태라면 파티 빼주도록 세팅 */
						if (_client.isInParty()) // 파티중
							_client.getParty().leaveMember(_client);
						if (_client.isInChatParty()) // 채팅파티중
							_client.getChatParty().leaveMember(_client);
						/** 모든 정보 저장을 위해 빠져나가도록 세팅 */
						_client.getNetConnection().quitGame(_client);
						synchronized (_client) {
							LoginController.getInstance().logout(_client.getAccountName());
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
