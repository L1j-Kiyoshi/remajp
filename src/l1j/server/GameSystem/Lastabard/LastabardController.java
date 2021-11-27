/*package l1j.server.GameSystem.Lastabard;

public class LastabardController {
	private static LastabardController _instance;

	// private int [][] fourthFloor = new int[2][3]; // 라던 4층 두번째 방, 네번째 방

	public static LastabardController getInstance() {
		if (_instance == null) {
			synchronized (LastabardController.class) {
				if (_instance == null) {
					_instance = new LastabardController();
				}
			}
		}

		return _instance;
	}

	public static void start() {
		LastabardSpawnTable.getInstance().Init();
	}

	
	 * public int getMobCount(int mobMapId, int room) { if(room > 4 || room < 0)
	 * return 0; int pos = LastabardData.getPosInMapId(mobMapId); if(pos < 0 ||
	 * pos > 1) return -1; return fourthFloor[pos][room]; }
	 * 
	 * public synchronized void die(int mobMapId, int room) { if(room > 4 ||
	 * room < 0) return; int pos = LastabardData.getPosInMapId(mobMapId); if(pos
	 * < 0 || pos > 1) return; fourthFloor[pos][room]--; }
	 * 
	 * public synchronized void alive(int mobMapId, int room) { if(room > 4 ||
	 * room < 0) return; int pos = LastabardData.getPosInMapId(mobMapId); if(pos
	 * < 0 || pos > 1) return; fourthFloor[pos][room]++; }
	 
}
*/