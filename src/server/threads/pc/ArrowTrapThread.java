package server.threads.pc;

import javolution.util.FastTable;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ArrowInstance;
import l1j.server.server.utils.L1SpawnUtil;

public class ArrowTrapThread implements Runnable {

	private static ArrowTrapThread _instance;

	public static ArrowTrapThread getInstance() {
		if (_instance == null) {
			_instance = new ArrowTrapThread();
		}
		return _instance;
	}

	public ArrowTrapThread() {
		// super("server.threads.pc.ArrowTrapThread");
		Arrow = L1SpawnUtil.ArrowSpawn();
		//System.out.println(ArrowTrapThread.class.getName() + " Ω√¿€");
		GeneralThreadPool.getInstance().schedule(this, 500);
	}

	public FastTable<L1ArrowInstance> Arrow;

	public void run() {
		int size = 0;
		L1ArrowInstance[] list = null;
		try {
			synchronized (Arrow) {
				if ((size = Arrow.size()) > 0) {
					list = (L1ArrowInstance[]) Arrow
							.toArray(new L1ArrowInstance[size]);
				}
			}
			if (size > 0) {
				for (L1ArrowInstance arr : list) {
					if (arr == null || arr._destroyed)
						remove(arr);
					if (arr.getAction()) {
						arr.ai();
						arr.setAction(false);
					} else
						arr.setAction(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			list = null;
			GeneralThreadPool.getInstance().schedule(this, 500);
		}
	}

	public void add(L1ArrowInstance npc) {
		synchronized (Arrow) {
			if (!Arrow.contains(npc))
				Arrow.add(npc);
		}
	}

	public void remove(L1ArrowInstance npc) {
		synchronized (Arrow) {
			if (Arrow.contains(npc))
				Arrow.remove(npc);
		}
	}

}
