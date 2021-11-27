package l1j.server.server.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.types.Point;

public class NearObjects {
	public List<L1Object> knownObjects = new CopyOnWriteArrayList<L1Object>();
	public List<L1PcInstance> knownPlayer = new CopyOnWriteArrayList<L1PcInstance>();

	/**
	 * 지정된 오브젝트를, 캐릭터가 인식하고 있을까를 돌려준다.
	 * 
	 * @param obj
	 *            조사하는 오브젝트.
	 * @return 오브젝트를 캐릭터가 인식하고 있으면 true, 하고 있지 않으면 false. 자기 자신에 대해서는 false를
	 *         돌려준다.
	 */
	public boolean knownsObject(L1Object obj) {
		return knownObjects.contains(obj);
	}

	/**
	 * 캐릭터가 인식하고 있는 모든 오브젝트를 돌려준다.
	 * 
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1Object>.
	 */
	public List<L1Object> getKnownObjects() {
		return knownObjects;
	}

	/**
	 * 캐릭터가 인식하고 있는 모든 플레이어를 돌려준다.
	 * 
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1PcInstance>
	 */
	public List<L1PcInstance> getKnownPlayers() {
		return knownPlayer;
	}

	/**
	 * 캐릭터에, 새롭게 인식하는 오브젝트를 추가한다.
	 * 
	 * @param obj
	 *            새롭게 인식하는 오브젝트.
	 */
	public void addKnownObject(L1Object obj) {
		if (!knownObjects.contains(obj)) {
			knownObjects.add(obj);
			if (obj instanceof L1PcInstance) {
				if (!knownPlayer.contains((L1PcInstance) obj))
					knownPlayer.add((L1PcInstance) obj);
			}
		}
	}

	/**
	 * 캐릭터로부터, 인식하고 있는 오브젝트를 삭제한다.
	 * 
	 * @param obj
	 *            삭제하는 오브젝트.
	 */
	public void removeKnownObject(L1Object obj) {
		if (knownObjects.contains(obj))
			knownObjects.remove(obj);
		if (obj instanceof L1PcInstance) {
			if (knownPlayer.contains((L1PcInstance) obj))
				knownPlayer.remove((L1PcInstance) obj);
		}
	}

	/**
	 * 캐릭터로부터, 모든 인식하고 있는 오브젝트를 삭제한다.
	 */
	public void removeAllKnownObjects() {
		if (knownObjects.size() > 0)
			knownObjects.clear();
		if (knownPlayer.size() > 0)
			knownPlayer.clear();
	}

	public ArrayList<L1Object> getVisibleObjects(L1Object object, int radius) {

		L1Map map = object.getMap();
		Point pt = object.getLocation();
		ArrayList<L1Object> result = new ArrayList<L1Object>();
		for (L1Object element : knownObjects) {
			if (element == null || element.equals(object)) {
				continue;
			}
			if (map != element.getMap()) {
				continue;
			}

			if (radius == -1) {
				if (pt.isInScreen(element.getLocation())) {
					result.add(element);
				}
			} else if (radius == 0) {
				if (pt.isSamePoint(element.getLocation())) {
					result.add(element);
				}
			} else {
				if (pt.getTileLineDistance(element.getLocation()) <= radius) {
					result.add(element);
				}
			}
		}
		return result;
	}
}