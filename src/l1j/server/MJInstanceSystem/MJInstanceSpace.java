package l1j.server.MJInstanceSystem;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Logger;

import l1j.server.MJInstanceSystem.Loader.MJInstanceLoadManager;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_SabuTell;

public class MJInstanceSpace {
	private static Logger 	_log = Logger.getLogger(MJInstanceSpace.class.getName());
	private static Object	_lock 	= new Object();
	
	private static MJInstanceSpace 	_instance;
	public static MJInstanceSpace getInstance(){
		if(_instance == null)
			_instance = new MJInstanceSpace();
		return _instance;
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	public static void reload(){
		MJInstanceSpace tmp = _instance;
		_instance = new MJInstanceSpace();
		tmp.clear();
		tmp = null;
	}
	
	private ArrayDeque<Integer>					_mapQ;
	private HashMap<Integer, MJInstanceObject> 	_objMap;
	private MJInstanceSpace(){
		_mapQ = new ArrayDeque<Integer>(MJInstanceLoadManager.MIS_COPYMAP_SIZE);
		int size = MJInstanceLoadManager.MIS_COPYMAP_START_ID + MJInstanceLoadManager.MIS_COPYMAP_SIZE;
		for(int i=MJInstanceLoadManager.MIS_COPYMAP_START_ID; i<size; i++)
			_mapQ.push(i);
		
		_objMap = new HashMap<Integer, MJInstanceObject>(MJInstanceLoadManager.MIS_COPYMAP_SIZE);
	}
	
	private L1Map popMap(MJInstanceObject obj){
		L1Map map = null;
		synchronized(_lock){
			if(_mapQ.isEmpty()){
				obj.notifySizeOver();
				return null;
			}
			
			if(obj.getBaseMapId() == -1)
				return null;
			
			map = L1WorldMap.getInstance().cloneMapAndGet(obj.getBaseMapId(), _mapQ.pop());
			obj.setCopyMap(map);
			_objMap.put(obj.getCopyMapId(), obj);
		}
		
		return map;
	}
	
	private void pushMap(MJInstanceObject obj){
		L1Map map = null;
		synchronized(_lock){
			map = obj.getCopyMap();
			if(map == null)
				return;
			
			_objMap.put(map.getId(), null);
			_mapQ.push(map.getId());
			//L1WorldMap.getInstance().removeMap(map.getId());
		}
	}
	
	public void startInstance(MJInstanceObject obj){
		if(_mapQ.isEmpty()){
			obj.notifySizeOver();
			return;
		}
		
		if(popMap(obj) == null)
			return;
		
		try{
			obj.init();
			GeneralThreadPool.getInstance().execute(obj);
		}catch(Exception e){
			e.printStackTrace();
			obj.dispose();
		}
	}
	
	public void releaseInstance(MJInstanceObject obj){
		if(obj == null || obj.getCopyMapId() == -1)
			return;
		
		try{
			
			int copyMapId = obj.getCopyMapId();
			Iterator<L1Object> it = L1World.getInstance().getVisibleObjects(copyMapId).values().iterator();
			while(it.hasNext()){
				L1Object l1obj = it.next();
				if((l1obj instanceof L1DollInstance) || (l1obj instanceof L1SummonInstance))
					continue;
				
				if((l1obj instanceof L1NpcInstance))
					((L1NpcInstance) l1obj).deleteMe();
			}
			
			Collection<L1ItemInstance> items 			= L1World.getInstance().getAllItem();
			L1ItemInstance[] 				itemArray 	= items.toArray(new L1ItemInstance[items.size()]);
			L1ItemInstance 				item			= null;
			L1Inventory						inv				=	 null;
			for(int i=0; i<itemArray.length; i++){
				item = itemArray[i];
				if(item == null)
					continue;
				
				if(copyMapId != item.getMapId())
					continue;
				
				inv = L1World.getInstance().getInventory(item.getX(), item.getY(), (short)copyMapId);
				if(inv == null)
					continue;
				
				inv.removeItem(item);
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		pushMap(obj);
		obj.dispose();
		obj = null;
	}
	
	public static boolean isInInstance(L1PcInstance pc){
		return isInInstance(pc.getMapId());
	}
	
	public static boolean isInInstance(int mid){
		int size	= MJInstanceLoadManager.MIS_COPYMAP_START_ID + MJInstanceLoadManager.MIS_COPYMAP_SIZE;
		return (mid >= MJInstanceLoadManager.MIS_COPYMAP_START_ID && mid < size);
	}
	
	public void getBackPc(L1PcInstance pc){
		if(isInInstance(pc)){
			pc.setMap((short)MJInstanceLoadManager.MIS_ERRBACK_MAPID);
			pc.setX(MJInstanceLoadManager.MIS_ERRBACK_X);
			pc.setY(MJInstanceLoadManager.MIS_ERRBACK_Y);
		}
	}
	
	public int getIdenMap(int mid){
		if(isInInstance(mid)){
			MJInstanceObject obj = _objMap.get(mid);
			if(obj == null)
				return mid;
			
			return obj.getBaseMapId();
		}
		return mid;
	}
	
	public void clear(){
		if(_objMap != null){
			Iterator<MJInstanceObject> 	it 	= _objMap.values().iterator();
			MJInstanceObject 			obj	= null;
			while(it.hasNext()){
				obj = it.next();
				releaseInstance(obj);
			}
			
			_objMap.clear();
			_objMap = null;
		}
		
		if(_mapQ != null){
			_mapQ.clear();
			_mapQ = null;
		}
	}
	
	public static void teleportInstance(L1PcInstance pc, short mapId, int x, int y){
		pc.dx = x;
		pc.dy = y;
		pc.dm = (short) mapId;
		pc.dh = pc.getMoveState().getHeading();
		pc.setTelType(7);
		pc.sendPackets(new S_SabuTell(pc), true);
	}
	
	public MJInstanceObject getOpenObject(int mapid){
		return _objMap.get(mapid);
	}
	
	public int getOpenSize(){
		return _objMap.size();
	}
	
	public Collection<MJInstanceObject> getOpens(){
		return _objMap.values();
	}
	
	public Collection<Integer> getOpensMaps(){
		return _objMap.keySet();
	}
}
