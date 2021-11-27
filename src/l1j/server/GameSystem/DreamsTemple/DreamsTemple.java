package l1j.server.GameSystem.DreamsTemple;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.Astar.World;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_SystemMessage;

public class DreamsTemple {

	private static Random _rnd = new Random(System.nanoTime());
	public L1PcInstance _pc;
	private int _mapnum;

	public DreamsTemple() {
	}

	public void systemgo(L1PcInstance pc, int mapid) {
		pc.getInventory().consumeItem(60511, 1);
		DreamsTempleController dtc = new DreamsTempleController(pc, mapid);

		GeneralThreadPool.getInstance().schedule(dtc, 5000);
	}

	public int start(L1PcInstance pc) {
		synchronized (GameList.DTList) {
			try {
				int mapid = GameList.getDreamsTempleMapId();
				if (mapid == 0) {
					S_SystemMessage sm = new S_SystemMessage(
							"모든 맵이 사용중입니다 잠시후 다시 이용해주세요.");
					pc.sendPackets(sm, true);
					return 0;
				}

				boolean ok = GameList.addDreamsTemple(mapid, this);
				if (!ok) {
					S_SystemMessage sm = new S_SystemMessage(
							"맵설정이 잘못 되었습니다. 다시 입장 신청 바랍니다.");
					pc.sendPackets(sm, true);
					// System.out.println("1");
					return 0;
				}
				if (!World.get_map(mapid)) {
					L1WorldMap.getInstance().cloneMap(1936, mapid);
				}

				_pc = pc;
				_mapnum = mapid;

				return mapid;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	public void Reset() {
		try {
			for (L1NpcInstance mon : MonList) {
				if (mon == null || mon._destroyed || mon.isDead()) {
					continue;
				}
				mon.deleteMe();
			}
			Object_Delete();

			if (MonList.size() > 0)
				MonList.clear();

			L1V1Map m = (L1V1Map) L1WorldMap.getInstance().getMap(
					(short) _mapnum);
			m.reset((L1V1Map) L1WorldMap.getInstance().getMap((short) 1936));
			// World.resetMap(2101, _mapnum);

			GameList.removeDreamsTemple(_mapnum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private ArrayList<L1NpcInstance> MonList = new ArrayList<L1NpcInstance>();

	public void AddMon(L1NpcInstance npc) {
		MonList.add(npc);
	}

	private void Object_Delete() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum)
				.values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				if (npc._destroyed || npc.isDead())
					continue;
				npc.deleteMe();
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != _mapnum)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(
					obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}

}
