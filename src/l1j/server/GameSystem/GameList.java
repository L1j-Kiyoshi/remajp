package l1j.server.GameSystem;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.GameSystem.DreamsLaboratory.DreamsLaboratory;
import l1j.server.GameSystem.DreamsTemple.DreamsTemple;
import l1j.server.GameSystem.FireDragon.FireDragon;
import l1j.server.GameSystem.IceQeen.IceQeen;
import l1j.server.GameSystem.ORIM.ORIM;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class GameList {
	private static L1NpcInstance b_death = null;
	private static L1NpcInstance w_death = null;

	private static GameList _instance;

	public static GameList getInstance() {
		if (_instance == null) {
			_instance = new GameList();
		}
		return _instance;
	}

	private static boolean ¿ë¶¥½ºÆù = false;

	public synchronized static void setbdeath(L1NpcInstance npc) {
		b_death = npc;
	}

	public synchronized static L1NpcInstance getbdeath() {
		return b_death;
	}

	public synchronized static void setwdeath(L1NpcInstance npc) {
		w_death = npc;
	}

	public synchronized static L1NpcInstance getwdeath() {
		return w_death;
	}

	public synchronized static void set¿ë¶¥(boolean f) {
		¿ë¶¥½ºÆù = f;
	}

	public synchronized static boolean get¿ë¶¥() {
		return ¿ë¶¥½ºÆù;
	}

	public static ConcurrentHashMap<Integer, DreamsTemple> DTList = new ConcurrentHashMap<Integer, DreamsTemple>(
			0);

	public static DreamsTemple getDT(int id) {
		return DTList.get(id);
	}

	public static boolean addDreamsTemple(int id, DreamsTemple DT) {
		if (DTList.containsKey(id)) {
			return false;
		} else {
			DTList.put(id, DT);
			return true;
		}
	}

	public static void removeDreamsTemple(int id) {
		if (DTList.containsKey(id))
			DTList.remove(id);
	}

	public synchronized static int getIceQueenMapId() {
		int mapid = 0;
		for (int i = 2102; i <= 2151; i++) {
			synchronized (IQList) {
				if (IQList.containsKey(i)) {
					IceQeen IQ = IQList.get(i);
					if (L1World.getInstance().getPlayer(IQ._pc.getName()) == null) {
						IQ.Reset();
						return i;
					}
					L1PcInstance pc = L1World.getInstance().getPlayer(
							IQ._pc.getName());
					if (pc.getMapId() != i && pc.dm != i) {
						IQ.Reset();
						return i;
					}
				} else {
					return i;
				}
			}
		}
		return mapid;
	}

	public static int getDreamsTempleMapId() {
		int mapid = 0;
		for (int i = 10010; i <= 10100; i++) {
			synchronized (DTList) {
				if (DTList.containsKey(i)) {
					DreamsTemple DT = DTList.get(i);
					if (L1World.getInstance().getPlayer(DT._pc.getName()) == null) {
						DT.Reset();
						return i;
					}

					L1PcInstance pc = L1World.getInstance().getPlayer(
							DT._pc.getName());
					if (pc.getMapId() != i && pc.dm != i) {
						DT.Reset();
						return i;
					}

				} else {
					return i;
				}
			}
		}
		return mapid;
	}

	public static ConcurrentHashMap<Integer, FireDragon> FDList = new ConcurrentHashMap<Integer, FireDragon>(
			0);

	public static FireDragon getFD(int id) {
		return FDList.get(id);
	}

	public static boolean addFireDragon(int id, FireDragon FD) {
		if (FDList.containsKey(id)) {
			return false;
		} else {
			FDList.put(id, FD);
			return true;
		}
	}

	public static void removeFireDragon(int id) {
		if (FDList.containsKey(id))
			FDList.remove(id);
	}

	public synchronized static int getFireDragonMapId() {
		int mapid = 0;
		for (int i = 2600; i <= 2698; i++) {
			synchronized (FDList) {
				if (FDList.containsKey(i)) {
					FireDragon FD = FDList.get(i);
					if (L1World.getInstance().getPlayer(FD._pc.getName()) == null) {
						FD.Reset();
						return i;
					}
					L1PcInstance pc = L1World.getInstance().getPlayer(
							FD._pc.getName());
					if (pc.getMapId() != i && pc.dm != i) {
						FD.Reset();
						return i;
					}
				} else {
					return i;
				}
			}
		}
		return mapid;
	}

	public static ConcurrentHashMap<Integer, ORIM> ORList = new ConcurrentHashMap<Integer, ORIM>(
			0);

	public static ORIM getOR(int id) {
		return ORList.get(id);
	}

	public static void addORIM(int id, ORIM OR) {
		ORList.put(id, OR);
	}

	public static void removeORIM(int id) {
		if (ORList.containsKey(id))
			ORList.remove(id);
	}

	public static int getORIMMapId() {
		int mapid = 0;
		int count = 0;
		for (int i = 9103; i <= 9200; i++) {
			ORIM OR = ORList.get(i);
			if (OR != null) {
				L1PcInstance pc = null;
				for (int j = 0; j < OR._pcname.length; j++) {
					pc = L1World.getInstance().getPlayer(OR._pcname[j]);
					if (pc != null) {
						if (pc.getMapId() == i) {
							count++;
						}
					}
				}

				if (count < 3) {
					OR.Reset();
					continue;
				}

			} else {
				return i;
			}
		}
		return mapid;
	}

	public static ConcurrentHashMap<Integer, IceQeen> IQList = new ConcurrentHashMap<Integer, IceQeen>(
			0);

	public static IceQeen getIQ(int id) {
		return IQList.get(id);
	}

	public static boolean addIceQeen(int id, IceQeen IQ) {
		if (IQList.containsKey(id)) {
			return false;
		} else {
			IQList.put(id, IQ);
			return true;
		}
	}

	public static void removeIceQeen(int id) {
		if (IQList.containsKey(id))
			IQList.remove(id);
	}
	
	public static ConcurrentHashMap<Integer, DreamsLaboratory> DLList = new ConcurrentHashMap<Integer, DreamsLaboratory>(0);

	public static DreamsLaboratory getDL(int id) {
		return DLList.get(id);
	}
	
	public static boolean getDLCheck(int id) {
		if (DLList.containsKey(id)) {
			return true;
		}else if(id >= 13001 && id <= 13100){
			return true;
		}else return false;
	}

	public static boolean addDreamsLaboratory(int id, DreamsLaboratory DL) {
		if (DLList.containsKey(id)) {
			return false;
		} else {
			DLList.put(id, DL);
			return true;
		}
	}

	public static void removeDreamsLaboratory(int id) {
		if (DLList.containsKey(id))
			DLList.remove(id);
	}
	
	public static int getDreamsLaboratory(int Type) {
		int MapId = Type == 3 ? 13001 : 13051;
		int EndMapId = Type == 3 ? 13050 : 13100;
		for (int i = MapId; i <= EndMapId; i++) {
			ArrayList<L1PcInstance> Player;
			synchronized (DLList) {
				if (DLList.containsKey(i)) {
					DreamsLaboratory DL = DLList.get(i);
					Player = L1World.getInstance().getMapPlayers(DL._MapId);
					if(Player.size() == 0){
						DL.Reset();
						return i;
					}
				} else {
					return i;
				}
			}
		}
		return 0;
	}

}