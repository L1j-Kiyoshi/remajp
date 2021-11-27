package l1j.server.GameSystem.TraningCenter;

import java.sql.Connection;
import java.sql.PreparedStatement;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.utils.L1SpawnUtil;
import l1j.server.server.utils.SQLUtil;

public class TraningCenter {

	private static TraningCenter _instance;
	private FastMap<L1Map, Boolean> mapList = null;
	public static final int time = 3600000 * 2; // 2½Ã°£

	public static TraningCenter get() {
		if (_instance == null) {
			synchronized (TraningCenter.class) {
				if (_instance == null)
					_instance = new TraningCenter();
			}
		}
		return _instance;
	}

	public TraningCenter() {
		mapList = new FastMap<L1Map, Boolean>().shared();

		mapList.put(L1WorldMap.getInstance().getMap((short) 1400), false);
		for (int i = 1401; i < 1500; i++) {
			L1WorldMap.getInstance().cloneMap(1400, i);
			mapList.put(L1WorldMap.getInstance().getMap((short) i), false);
		}

		String[] qlist = { "character_warehouse", "character_elf_warehouse",
				"character_items", "clan_warehouse" };
		for (String qu : qlist) {
			if (qu == null)
				continue;
			Connection con = null;
			PreparedStatement pstm = null;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("delete FROM " + qu
						+ " WHERE item_id=?");
				pstm.setInt(1, 60285);
				pstm.executeUpdate();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

	//	System.out.println(":: TraningCenter Loading Compleate");
	}

	public L1Map start() {
		synchronized (mapList) {
			L1Map map = mapCheck();
			if (map == null) {
				return null;
			}
			mapList.put(map, true);
			L1SpawnUtil.spawn2(32902, 32818, (short) map.getId(), 100563, 0, 0,
					0);
			new ExitTimer(map);
			return map;
		}
	}

	public L1Map mapCheck() {
		for (FastMap.Entry<L1Map, Boolean> e = mapList.head(), mapEnd = mapList
				.tail(); (e = e.getNext()) != mapEnd;) {
			if (!e.getValue()) {
				return e.getKey();
			}
		}
		return null;
	}

	public void quit(L1Map map) {
		synchronized (mapList) {
			// ((L1V1Map)map).reset((L1V1Map)L1WorldMap.getInstance().getMap((short)1499));
			mapList.put(map, false);
		}
	}

	class ExitTimer implements Runnable {

		private L1Map map;

		public ExitTimer(L1Map _map) {
			map = _map;
			GeneralThreadPool.getInstance().schedule(this, time);
		}

		@Override
		public void run() {
			try {

				for (L1Object ob : L1World.getInstance()
						.getVisibleObjects(map.getId()).values()) {
					if (ob == null || ob instanceof L1DollInstance
							|| ob instanceof L1SummonInstance
							|| ob instanceof L1PetInstance)
						continue;
					if (ob instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) ob;
						npc.deleteMe();
					} else if (ob instanceof L1PcInstance) {
						L1Teleport.teleport((L1PcInstance) ob, 33489, 32764,
								(short) 4, 5, true);
					}
				}
				for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
					if (obj.getMapId() != map.getId())
						continue;
					L1Inventory groundInventory = L1World.getInstance()
							.getInventory(obj.getX(), obj.getY(),
									obj.getMapId());
					groundInventory.removeItem(obj);
				}
				quit(map);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
