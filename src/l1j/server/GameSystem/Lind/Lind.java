package l1j.server.GameSystem.Lind;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;

public class Lind {

	private FastTable<L1PcInstance> pcList;
	private long endTime;
	private L1Map map;
	public byte Step = 0;
	public byte Sub_Step = 0;
	public L1NpcInstance portal = null;
	public long Sleep = 0;
	public L1NpcInstance dragon_lind = null;
	public FastMap<String, L1NpcInstance> lind_level2;
	public FastMap<String, L1NpcInstance> cloud_list;
	public byte mapckCount = 0;
	public byte MembermapckCount = 0;
	public int[] xymapid = new int[3];

	public Lind(L1Map _map) {
		map = _map;
		pcList = new FastTable<L1PcInstance>();
		endTime = System.currentTimeMillis() + 7200000;// 2시간 후
		LindSpawn.getInstance().fillSpawnTable(map.getId(), 0, false); // 기본 NPC
																		// 스폰
	}

	public long getEndTIme() {
		return endTime;
	}

	public void addMember(L1PcInstance pc) {
		pcList.add(pc);
	}

	public void removeMember(L1PcInstance pc) {
		pcList.remove(pc);
	}

	public L1PcInstance[] getMember() {
		return pcList.toArray(new L1PcInstance[pcList.size()]);
	}

	public L1Map getMap() {
		return map;
	}

	public void setMap(L1Map _map) {
		map = _map;
	}

	public void clear() {
		if (portal != null)
			portal.deleteMe();
		if (pcList.size() > 0)
			pcList.clear();
		map = null;
		endTime = 0;
	}
}
