package l1j.server.GameSystem;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Location;
import l1j.server.server.utils.L1SpawnUtil;

public class DesertTornadoController implements Runnable {

	@Override
	public void run() {
		// while(true){
		try {
			for (int i = 0; i < 10; i++) {
				L1Location loc = new L1Location();
				loc.set(32707, 33121, 4);
				loc = L1Location.randomRangeLocation(loc, 190, 583, true);
				L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short) 4, 100342,
						1, 0, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			GeneralThreadPool.getInstance().schedule(this, 75000);
			// try{
			// Thread.sleep(75000);
			// }catch(Exception e){}
		}
		// }
	}

}
