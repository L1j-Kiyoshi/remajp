package l1j.server.GameSystem.Valakas;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import javolution.util.FastTable;
import l1j.server.server.ActionCodes;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.utils.L1SpawnUtil;

public class ValaRaidSystem {
	
	//private static Logger _log = Logger.getLogger(HadinSystem.class.getName());

	private static ValaRaidSystem _instance;
	public static L1NpcInstance portal = null;
	private static FastTable<L1PcInstance> pcList;
	private final ArrayList<Integer> _map = new ArrayList<Integer>();
	private final Map<Integer, ValaRaid> _list = new ConcurrentHashMap<Integer, ValaRaid>();

	private int ValaKasMapID = 1161;
	public static ValaRaidSystem getInstance() {
		if (_instance == null) {
			_instance = new ValaRaidSystem();
		}
		return _instance;
	}

	public ValaRaidSystem(){
		_map.add(ValaKasMapID);
	}
	
    private ArrayList<L1DoorInstance> FireWall;
    
    private int[][] _FireWallSpawn = new int[][]{ 
    		/** 맵번호 and 방향 */
    		{32797, 32851, 2},
			{32787, 32860, 4},
			{32796, 32875, 2},
			{32812, 32860, 4},
    };
	private static Random _random = new Random(System.nanoTime());

	public void random_spawn(int x, int y, int mapid)
	{
		int randx = _random.nextInt(2)+1;
		int randy = _random.nextInt(2)+1;
		
		for(int i = 0 ; i < 2 ; i ++)
		{
			int rr = _random.nextInt(2);
			if(rr == 1)
				L1SpawnUtil.spawn2(x + randx, y + randy, (short) mapid, 5000100, 0, 7200 * 1000, mapid);
			else
				L1SpawnUtil.spawn2(x - randx, y - randy, (short) mapid, 5000100, 0, 7200 * 1000, mapid);
		}
	}
	public void startValakas(L1PcInstance pc){
		if(countVala() >= 6){
			pc.sendPackets(new S_ChatPacket(pc,"인스턴스 던전을 더 이상 생성할수 없습니다."));
			return;
		}
		int id = blankMapId();
		if(id != ValaKasMapID)
			L1WorldMap.getInstance().cloneMap(ValaKasMapID, id);
		ValaRaid vala = new ValaRaid(id);
		pc.dragonmapid = (short) id;

		int fire_delay = 3200;
		FireWall = new ArrayList<L1DoorInstance>();
		L1SpawnUtil.spawn2(pc.getX(), pc.getY(), pc.getMapId(), 910008, 0, 3600 * 1000, id); // 포탈
		L1SpawnUtil.spawn2(32731, 32922, (short) id, 3310015, 0, fire_delay * 1000, id); // 데스나이트 이펙트
		L1SpawnUtil.spawn2(32732, 32922, (short) id, 3310018, 0, fire_delay * 1000, id); // 데스나이트 버프
		L1SpawnUtil.spawn2(32733, 32937, (short) id, 60032, 0, fire_delay * 1000, id); // 창고지기
		L1SpawnUtil.spawn2(32727, 32937, (short) id, 3310016, 0, fire_delay * 1000, id); // 잡화상인
		L1SpawnUtil.spawn2(32751, 32928, (short) id, 3310017, 0, fire_delay * 1000, id); // 레어 입구
		L1SpawnUtil.spawn2(32738, 32925, (short) id, 170017, 0, fire_delay * 1000, id); // 오림
		FireWall.add(DoorSpawn(32763, 32897, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32764, 32898, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32762, 32899, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32762, 32899, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32761, 32898, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32762, 32901, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32763, 32901, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32765, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32767, 32902, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32766, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32763, 32902, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32768, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32770, 32902, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32770, 32902, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32771, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32772, 32902, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32774, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32775, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32776, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32776, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32777, 32901, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32779, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32779, 32903, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32777, 32899, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32779, 32897, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32781, 32897, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32781, 32897, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32781, 32897, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32778, 32900, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32783, 32899, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32784, 32901, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32780, 32895, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32782, 32892, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32780, 32891, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32783, 32892, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32783, 32892, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32781, 32887, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32781, 32887, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32780, 32886, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32781, 32884, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32780, 32883, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32779, 32882, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32778, 32882, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32781, 32882, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32782, 32882, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32778, 32880, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32776, 32880, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32774, 32880, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32774, 32880, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32773, 32879, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32773, 32879, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32773, 32878, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32770, 32881, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32770, 32881, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32770, 32881, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32767, 32883, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32765, 32884, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32763, 32885, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32760, 32887, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32760, 32888, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32760, 32888, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32758, 32891, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32758, 32891, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32760, 32892, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32760, 32892, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32758, 32894, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32759, 32895, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32759, 32895, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32758, 32899, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32757, 32898, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32758, 32885, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32758, 32884, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32758, 32883, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32758, 32883, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32759, 32882, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32762, 32882, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32763, 32881, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32765, 32880, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32774, 32902, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32777, 32901, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32769, 32876, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32776, 32876, (short) id, 0, 5000100, 170  * 1000));
		FireWall.add(DoorSpawn(32780, 32877, (short) id, 0, 5000100, 170  * 1000));	
		FireWall.add(DoorSpawn(32760, 32890, (short) id, 0, 5000100, 170  * 1000));	
		FireWall.add(DoorSpawn(32762, 32887, (short) id, 0, 5000100, 170  * 1000));	
		FireWall.add(DoorSpawn(32779, 32893, (short) id, 0, 5000100, 170  * 1000));	
		FireWall.add(DoorSpawn(32781, 32889, (short) id, 0, 5000100, 170  * 1000));	
		FireWall.add(DoorSpawn(32760, 32897, (short) id, 0, 5000100, 170  * 1000));	
		_list.put(id, vala);
	}
	
	public int blankMapId(){
		if(_list.size() == 0)
			return ValaKasMapID;
		for(int i = ValaKasMapID ; i <= ValaKasMapID+5; i++){
			ValaRaid h = _list.get(i);
			if(h == null)
				return i;
		}  
		return ValaKasMapID;
	}
	
	private L1DoorInstance DoorSpawn(int X, int Y, short MapId, int Heading, int npcId, int timeMillisToDelete) {
		try {
			L1DoorInstance Door = (L1DoorInstance) NpcTable.getInstance().newNpcInstance(npcId);
			Door.setId(ObjectIdFactory.getInstance().nextId());
			Door.setDoorId(npcId);
			Door.setMap(MapId);
			Door.getLocation().set(X, Y, MapId);
			Door.setHomeX(Door.getX());
			Door.setHomeY(Door.getY());
			Door.setDirection(Heading);
			Door.setLeftEdgeLocation(Heading == 0 ? Door.getX() : Door.getY());
			Door.setRightEdgeLocation(Heading == 0 ? Door.getX() : Door.getY());
			Door.setMaxHp(0);
			Door.setCurrentHp(0);
			Door.setKeeperId(0);
			Door.setOpenStatus(ActionCodes.ACTION_Close);
			for (L1PcInstance Pc : L1World.getInstance().getVisiblePlayer(Door)){
				Door.onPerceive(Pc);
				Pc.sendPackets(new S_DoActionGFX(Door.getId(), ActionCodes.ACTION_Close), true);
			}
			L1World.getInstance().storeObject(Door);
			L1World.getInstance().addVisibleObject(Door);
			Door.isPassibleDoor(false);
			if (0 < timeMillisToDelete) {
				L1NpcDeleteTimer timer = new L1NpcDeleteTimer(Door, timeMillisToDelete);
				timer.begin();
			}
			return Door;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public ValaRaid getVala(int id){
		
		return _list.get(id);
	}

	public void removeVala(int id){
		_list.remove(id);
	}

	public int countVala(){
		return _list.size();
	}
	public static void clear() {
		if (portal != null)
			portal.deleteMe();
		if (pcList.size() > 0)
			pcList.clear();
	}
}
