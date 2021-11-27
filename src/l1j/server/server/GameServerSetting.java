package l1j.server.server;

public class GameServerSetting extends Thread {
	private static GameServerSetting _instance;

	public static GameServerSetting getInstance() {
		if (_instance == null) {
			_instance = new GameServerSetting();
		}
		return _instance;
	}

	/** Server Manager 1 관련 부분 **/
	public static boolean isNormal = false;
	public static boolean isWishper = false;
	public static boolean isGlobal = false;
	public static boolean isClan = false;
	public static boolean isChatParty = false;
	public static boolean isShop = false;
	public static boolean isParty = false;

	public static boolean ServerDown = false;

	private GameServerSetting() {
		super("l1j.server.server.GameServerSetting");
	}

	public void run() {
		while (true) {
			try {
				sleep(1000L);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
