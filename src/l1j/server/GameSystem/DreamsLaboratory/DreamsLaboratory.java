package l1j.server.GameSystem.DreamsLaboratory;

import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.Astar.World;
import l1j.server.GameSystem.Dungeon.DungeonInfo;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.model.map.L1WorldMap;

public class DreamsLaboratory {

	public L1PcInstance _Pc;
	public int _MapId, _CopyMapId;

	public DreamsLaboratory() {}
	
	public void SystemGo(L1PcInstance Pc, int Type, DungeonInfo DungeonInfo) {
		L1Location Loc;
		
		int Adena = 0;
		for(L1PcInstance PcList : DungeonInfo.isDungeonInfoUset()){
			PcList.getInventory().CheckItemSkill();
			PcList.setDungeonInfoCheck(false);
			if(PcList.getId() == Pc.getId()){
				PcList.getInventory().consumeItem(500021, 1);
			}else{
				PcList.getInventory().consumeItem(500021, 1);
				PcList.getInventory().consumeItem(L1ItemId.ADENA, DungeonInfo.Adena);
				Adena += DungeonInfo.Adena;
			}
			if(PcList.getParty() != null) PcList.getParty().leaveMember(PcList);
			Loc = new L1Location(32800, 32863, (short) _MapId).randomLocation(5, false);
			L1Teleport.teleport(PcList, Loc.getX(), Loc.getY(), (short)Loc.getMapId() , PcList.getMoveState().getHeading(), false, false, 5000);
		}
		/** 유저들이 낸 아데나 파장한테 지급 */
		Pc.getInventory().storeItem(L1ItemId.ADENA, Adena);

		GeneralThreadPool.getInstance().schedule(new DreamsLaboratoryController(DungeonInfo, Type == 3 ? 0 : 1, _MapId), 1000);
	}

	/** 맵이 있는지 체크해서 그맵에 정보 갱신 */
	public int Start(L1PcInstance pc, int Type) {
		synchronized (GameList.DLList) {
			try {
				int MapId = GameList.getDreamsLaboratory(Type);
				if(MapId == 0 || !GameList.addDreamsLaboratory(MapId, this)) return 0;
				
				_Pc = pc;
				_MapId = MapId;
				_CopyMapId = Type == 3 ? 730 : 731;
				
				if (!World.get_map(MapId)) 
					L1WorldMap.getInstance().cloneMap(_CopyMapId, MapId);

				return MapId;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	public void Reset() {
		try {
			/** 모든 오브젝트 삭제 */
			Object_Delete();

			L1V1Map Map = (L1V1Map) L1WorldMap.getInstance().getMap((short) _MapId);
			Map.reset((L1V1Map) L1WorldMap.getInstance().getMap((short) _CopyMapId));

			GameList.removeDreamsLaboratory(_MapId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void Object_Delete() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_MapId).values()) {
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
			if (obj.getMapId() != _MapId) continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}
}