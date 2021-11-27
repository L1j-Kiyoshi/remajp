package l1j.server.GameSystem.Gamble;

import javolution.util.FastTable;
import l1j.server.server.model.L1World;

public class Gamble {

	private FastTable<GambleInstance> list;
	private static Gamble _instance;

	public static Gamble get() {
		if (_instance == null)
			_instance = new Gamble();
		return _instance;
	}

	public Gamble() {
		list = new FastTable<GambleInstance>();
	//	System.out.println(":: Gamble Loading complete");
	}

	public void Load() {
		GambleSpawn.getInstance();
		GambleChatThread.get();
	}

	public void add(GambleInstance gi) {
		list.add(gi);
	}

	public void remove(GambleInstance gi) {
		list.remove(gi);
	}

	public GambleInstance[] toArray() {
		if (list.size() <= 0)
			return null;
		return (GambleInstance[]) list.toArray(new GambleInstance[list.size()]);
	}

	public boolean Gamble_Show_or_Delete() {
		boolean ck = false;
		for (GambleInstance npc : list) {
			if (npc.getType() == 1
					&& (npc.wand_npc[0] == null || npc.wand_npc[1] == null || npc.wand_npc[2] == null))
				continue;
			if (!npc.getShow()) {
				L1World.getInstance().storeObject(npc);
				L1World.getInstance().addVisibleObject(npc);
				npc.setShow(true);
				ck = true;
			} else {
				npc.setShow(false);
				npc.delete();
			}
		}
		return ck;
	}
}
