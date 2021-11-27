/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.clientpackets;

import java.util.ArrayList;

import l1j.server.Config;
import l1j.server.GameSystem.DogFight.DogFight;
import l1j.server.GameSystem.NpcBuyShop.NpcBuyShop;
import l1j.server.Warehouse.ClanWarehouse;
import l1j.server.Warehouse.ElfWarehouse;
import l1j.server.Warehouse.PackageWarehouse;
import l1j.server.Warehouse.PrivateWarehouse;
import l1j.server.Warehouse.SupplementaryService;
import l1j.server.Warehouse.Warehouse;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.datatables.ClanHistoryTable;
import l1j.server.server.datatables.LogTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.ShopTable;
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.model.shop.L1ShopBuyOrderList;
import l1j.server.server.model.shop.L1ShopSellOrderList;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import manager.LinAllManager;
import server.LineageClient;

public class C_ShopAndWarehouse extends ClientBasePacket {
	private final int TYPE_BUY_SHP = 0; // 상점 or 개인 상점 사기
	private final int TYPE_SEL_SHP = 1; // 상점 or 개인 상점 팔기
	private final int TYPE_PUT_PWH = 2; // 개인 창고 맡기기
	private final int TYPE_GET_PWH = 3; // 개인 창고 찾기
	private final int TYPE_PUT_CWH = 4; // 혈맹 창고 맡기기
	private final int TYPE_GET_CWH = 5; // 혈맹 창고 찾기
	private final int TYPE_PUT_EWH = 8; // 엘프 창고 맡기기
	private final int TYPE_GET_EWH = 9; // 엘프 창고 찾기
	private final int TYPE_GET_MWH = 10; // 패키지 창고 찾기
	private final int TYPE_GET_PET = 12; // 펫 찾기
	private final int TYPE_GET_SSH = 18; // 부가서비스 창고

	public C_ShopAndWarehouse(byte abyte0[], LineageClient clientthread) throws Exception {
		super(abyte0);
		
		try {
			int npcObjectId = readD();
			int resultType = readC();
			int size = readC();
			
			@SuppressWarnings("unused")
			int unknown = readC();
			if (size < 0) {
				return;
			}

			L1PcInstance pc = clientthread.getActiveChar();
			if (pc.getOnlineStatus() == 0 || isTwoLogin(pc)) {
				clientthread.kick();
				clientthread.close();
				return;
			}

			int npcId = 0;
			String npcImpl = "";
			boolean isPrivateShop = false, isNoCheckShop = false;
			L1Object findObject = L1World.getInstance().findObject(npcObjectId);
			if (findObject != null) { // 15셀
				if (findObject instanceof L1NpcInstance) {
					L1NpcInstance targetNpc = (L1NpcInstance) findObject;
					npcId = targetNpc.getNpcTemplate().get_npcId();
					npcImpl = targetNpc.getNpcTemplate().getImpl();
					
					/** 특정 엔피씨만 거리 체크 안하도록 정리 */
					if(npcId == 77221 || npcId == 77223 || npcId == 70014 || npcId == 60187) {
						isNoCheckShop = true;
					}
				} else if (findObject instanceof L1PcInstance) {
					isPrivateShop = true;
				}
				
				int diffLocX = Math.abs(pc.getX() - findObject.getX());
				int diffLocY = Math.abs(pc.getY() - findObject.getY());
				/** 특정 샵은 아이템 위치 상관없이 무조건 상점 열리도록 따로 관리 */
				if (!isNoCheckShop && (diffLocX > 15 || diffLocY > 15)) {
					return;
				}
			}
			
			if(resultType == 0||resultType == 2){
			   if(size > 1000){
				   System.out.println("김하나 등장!! ["+pc.getName()+"] IP "+clientthread.getIp());
				   clientthread.kick();
			   }
			}  //추가
			if (resultType == TYPE_BUY_SHP || resultType == TYPE_GET_PWH || resultType == TYPE_GET_CWH || resultType == TYPE_GET_EWH || resultType == TYPE_GET_MWH) {
				if (pc.getInventory().calcWeightpercent() >= 99) {
					pc.sendPackets(new S_ServerMessage(81)); // 무게 게이지가 가득찼습니다.
					return;
				}
			}

			if (npcObjectId == 7626) {
				npcId = 7626;
				npcImpl = "L1Merchant";
			}

			// System.out.println("123");
			switch (resultType) {
				case TYPE_BUY_SHP: // 상점 or 개인 상점 사기
					// System.out.println("npcid = "+npcId+"size :"+size);
					if (npcId == 70035 || npcId == 70041 || npcId == 70042) {
						int status = L1BugBearRace.getInstance().getBugRaceStatus();
						boolean chk = L1BugBearRace.getInstance().buyTickets;
						if (status != L1BugBearRace.STATUS_READY || chk == false) {
							return;
						}
					} else if (npcId == 72320) {
						int status = DogFight.getInstance().getBugRaceStatus();
						boolean chk = DogFight.getInstance().buyTickets;
						if (status != DogFight.STATUS_READY || chk == false) {
							return;
						}
					}
					
					if (pc.getClan() == null && pc.getLevel() >= Config.무혈상점) {
						pc.sendPackets(new S_SystemMessage(Config.무혈상점 + "레벨 이상은 혈맹이 없으면 상점을 이용할 수 없습니다."));
						if (pc.isGm()) {
						//	pc.sendPackets(new S_SystemMessage("하지만 운영자는 됨ㅋㅋ"));
						} else {
							return;
						}
					}
					
					if (size != 0 && npcImpl.equalsIgnoreCase("L1Merchant")) {
						buyItemFromShop(pc, npcId, size);
					}
					
					if (size != 0 && npcImpl.equalsIgnoreCase("L1NpcShop")) {
						buyItemFromNpcShop(pc, npcId, size);
						break;
					}
	
					if (size != 0 && isPrivateShop) {
						buyItemFromPrivateShop(pc, findObject, size);
					}
					
					break;
	
				case TYPE_SEL_SHP: // 상점 or 개인 상점 팔기
					if (size != 0 && npcImpl.equalsIgnoreCase("L1Merchant"))
						sellItemToShop(pc, npcId, size);
					if (size != 0 && npcImpl.equalsIgnoreCase("L1NpcShop")) {
						sellItemToNpcShop(pc, npcId, size);
						break;
					}
					
					if (size != 0 && isPrivateShop) {
						sellItemToPrivateShop(pc, findObject, size);
					}
					break;
					
				case TYPE_PUT_PWH: // 개인 창고 맡기기
					if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
						putItemToPrivateWarehouse(pc, size);
					}
					break;
					
				case TYPE_GET_PWH: // 개인 창고 찾기
					if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
						getItemToPrivateWarehouse(pc, size);
					}
					break;
					
				case TYPE_PUT_CWH: // 혈맹 창고 맡기기
					if (npcImpl.equalsIgnoreCase("L1Dwarf")) {
						putItemToClanWarehouse(pc, size);
					}
					break;
					
				case TYPE_GET_CWH: // 혈맹 창고 찾기
					if (npcImpl.equalsIgnoreCase("L1Dwarf")) {
						getItemToClanWarehouse(pc, size);
					}
					break;
					
				case TYPE_PUT_EWH: // 엘프 창고 맡기기
					if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
						putItemToElfWarehouse(pc, size);
					}
					break;
					
				case TYPE_GET_EWH: // 엘프 창고 찾기
					if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
						getItemToElfWarehouse(pc, size);
					}
					break;
					
				case TYPE_GET_MWH: // 패키지 창고 찾기
					if (size != 0 && npcImpl.equalsIgnoreCase("L1Dwarf")) {
						getItemToPackageWarehouse(pc, size);
					}
					break;
					
				case TYPE_GET_PET: // 펫 찾기
					/** 펫 창고 사용 안함 */
					if (size != 0 && npcImpl.equalsIgnoreCase("L1Merchant"))
					break;
					
				case TYPE_GET_SSH: // 부가 서비스 창고
					if (size != 0 && pc == findObject)
						getItemToSupplementaryService(pc, size);
					break;
					
				default:
					break;
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private void getItemToSupplementaryService(L1PcInstance pc, int size) {
		SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
		if (warehouse == null) return;
		if (size > 100) return;
		
		ArrayList<L1ItemInstance> _List = new ArrayList<L1ItemInstance>();
		ArrayList<Integer> _List_Count = new ArrayList<Integer>();
	
		L1ItemInstance Item = null;
		for (int i = 0, number, count; i < size; i++) {
			number = readD();
			count = readD();
			Item = warehouse.getItems().get(number);
			if (!isAvailableTrade(pc, Item, count)) {
				break;
			}
			
			if (!isAvailablePcWeight(pc, Item, count)) {
				break;
			}
			
			if (count > Item.getCount()) count = Item.getCount();
			ArrayList<String> name = new ArrayList<String>();
			name = L1PcInstance.getPCs(pc.getAccountName());
			
			for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
				if (_pc.getName() == pc.getName()) {
					continue;
				}
				
				if (name.contains(_pc.getName())) {
					if (_pc.getNetConnection() != null) {
						_pc.getNetConnection().close();
					}
				}
			}
			
			_List.add(Item);
			_List_Count.add(count);
		}
		
		for (L1ItemInstance ItemList : _List) {
			int Count = _List_Count.get(_List.indexOf(ItemList));
			warehouse.tradeItem(ItemList, Count, pc.getInventory());
			pc.sendPackets(new S_ServerMessage(403, ItemList.getNumberedName(Count)), true);
		}
		_List.clear();
		_List_Count.clear();
	}

	private void doNothingClanWarehouse(L1PcInstance pc) {
		if (pc == null) {
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan == null) {
			return;
		}

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		if (clanWarehouse == null) {
			return;
		}

		clanWarehouse.unlock(pc.getId());
	}

	private boolean okok(int id) {
		/*
		 * if(id >=40044 && id<=40055){ return true; } if(id ==41246){ return
		 * true; } if(id ==40308){ return true; }
		 */
		return false;
	}

	private void getItemToPackageWarehouse(L1PcInstance pc, int size) {
		int objectId, count;
		L1ItemInstance item = null;
		PackageWarehouse w = WarehouseManager.getInstance().getPackageWarehouse(pc.getAccountName());
		if (w == null) {
			return;
		}

		for (int i = 0; i < size; i++) {
			objectId = readD();
			count = readD();
			item = w.getItem(objectId, count);
			/*
			 * if(item ==null){ if(Config.버그채팅모니터()>0){ for(L1PcInstance gm :
			 * Config.toArray버그채팅모니터()){ if(gm.getNetConnection()==null){
			 * Config.remove버그(gm); continue; } gm.sendPackets(new
			 * S_SystemMessage("복사 버그 의심 : "+pc.getName())); } }
			 * pc.sendPackets(new S_SystemMessage("잘못된 접근 다시 시도해주세요.")); break;
			 * }
			 */
			if (okok(item.getItemId())) {
				pc.sendPackets(new S_SystemMessage("해당 아이템은 당분간 창고이용이 불가능합니다."), true);
				break;
			}
			
			if (!isAvailableTrade(pc, objectId, item, count)) {
				break;
			}
			
			if (count > item.getCount()) {
				count = item.getCount();
			}
			
			if (!isAvailablePcWeight(pc, item, count)) {
				break;
			}

			ArrayList<String> name = new ArrayList<String>();
			name = L1PcInstance.getPCs(pc.getAccountName());
			for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
				if (_pc.getName() == pc.getName()) {
					continue;
				}
				
				if (name.contains(_pc.getName())) {
					if (_pc.getNetConnection() != null) {
						_pc.getNetConnection().close();
					}
				}
			}

			w.tradeItem(item, count, pc.getInventory());
		}
	}

	private void getItemToElfWarehouse(L1PcInstance pc, int size) {
		if (pc.getLevel() < 5 || !pc.isElf()) {
			return;
		}

		ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
		if (elfwarehouse == null) {
			return;
		}
		
		/** 사이즈 만큼 아데나가 있는지 체크 그아데나가 없다면 리턴 */
		if (!pc.getInventory().checkItem(40494, 4 * size)) {
			pc.sendPackets(new S_ServerMessage(189), true);
			return;
		}
		
		ArrayList<L1ItemInstance> _List = new ArrayList<L1ItemInstance>();
		ArrayList<Integer> _List_Count = new ArrayList<Integer>();
	
		L1ItemInstance Item = null;
		for (int i = 0, number, count; i < size; i++) {
			number = readD();
			count = readD();
			Item = elfwarehouse.getItems().get(number);
			if (!isAvailableTrade(pc, Item, count)) break;
			if (!isAvailablePcWeight(pc, Item, count)) break;
			if (count > Item.getCount()) count = Item.getCount();
			ArrayList<String> name = new ArrayList<String>();
			name = L1PcInstance.getPCs(pc.getAccountName());
			for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
				if (_pc.getName() == pc.getName()) {
					continue;
				}
				
				if (name.contains(_pc.getName())) {
					if (_pc.getNetConnection() != null) {
						_pc.getNetConnection().close();
					}
				}
			}
			
			_List.add(Item);
			_List_Count.add(count);
		}
		
		for (L1ItemInstance ItemList : _List) {
			if (!hasMithril(pc)) break; /** 미스릴 체크해서 빼주기 */
			int Count = _List_Count.get(_List.indexOf(ItemList));
			elfwarehouse.tradeItem(ItemList, Count, pc.getInventory());
		}
		
		_List.clear();
		_List_Count.clear();
	}

	private void putItemToElfWarehouse(L1PcInstance pc, int size) {
		if (pc.getLevel() < 5 || !pc.isElf()) {
			return;
		}

		L1Object object = null;
		L1ItemInstance item = null;
		ElfWarehouse elfwarehouse = WarehouseManager.getInstance().getElfWarehouse(pc.getAccountName());
		if (elfwarehouse == null) {
			return;
		}

		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();

			object = pc.getInventory().getItem(objectId);
			item = (L1ItemInstance) object;

			// 운영자 창고 사용 금지
			// if (pc.getAccessLevel() == Config.GMCODE) break;
			// if (item.getId() == 41246) break; // 결정체 창고 사용 금지
			if (!isAvailableTrade(pc, objectId, item, count)) {
				break;
			}
			
			if (count > item.getCount()) {
				count = item.getCount();
			}

			if (!item.getItem().isTradable()) {
				pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				break;
			}

			if (!checkPetList(pc, item)) { 
				break;
			}
			
			if (!isAvailableWhCount(elfwarehouse, pc, item, count)) {
				break;
			}

			if (item.getItem().getItemId() == 430116) {
				pc.sendPackets(new S_SystemMessage("해당 아이템은 창고 이용을 할 수 없습니다."), true);
				break;
			}
			
			if (item.getItem().getItemId() == 49312 || item.getItem().getItemId() == 40312 || item.getItem().getItemId() == 437011  ) {
				pc.sendPackets(new S_SystemMessage("해당 아이템은 창고 이용을 할 수 없습니다."), true);
				break;
			}

			ArrayList<String> name = new ArrayList<String>();
			name = L1PcInstance.getPCs(pc.getAccountName());
			for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
				if (_pc.getName() == pc.getName()) {
					continue;
				}
				
				if (name.contains(_pc.getName())) {
					if (_pc.getNetConnection() != null) {
						_pc.getNetConnection().close();
					}
				}
			}

			pc.getInventory().tradeItem(objectId, count, elfwarehouse);
		}
	}

	private void getItemToClanWarehouse(L1PcInstance pc, int size) {
		if (pc.getLevel() < 5) return;
		if (pc.getClanRank() == L1Clan.CLAN_RANK_PROBATION) return;
		if (size == 0) {
			doNothingClanWarehouse(pc);
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		/** 혈맹원인지 체크 와 혈창이 놀이 아닌지 체크 */
		if (!isAvailableClan(pc, clan)) {
			return;
		}
		
		if (clanWarehouse == null) {
			return;
		}
		
		/** 사이즈 만큼 아데나가 있는지 체크 그아데나가 없다면 리턴 */
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, 100 * size)) {
			pc.sendPackets(new S_ServerMessage(189), true);
			return;
		}
		
		ArrayList<L1ItemInstance> _List = new ArrayList<L1ItemInstance>();
		ArrayList<Integer> _List_Count = new ArrayList<Integer>();
	
		L1ItemInstance Item = null;
		for (int i = 0, number, count; i < size; i++) {
			number = readD();
			count = readD();
			Item = clanWarehouse.getItems().get(number);
			if (!isAvailableTrade(pc, Item, count)) {
				break;
			}
			
			if (!isAvailablePcWeight(pc, Item, count)) {
				break;
			}
			
			if (count > Item.getCount()) count = Item.getCount();
			ArrayList<String> name = new ArrayList<String>();
			name = L1PcInstance.getPCs(pc.getAccountName());
			for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
				if (_pc.getName() == pc.getName()) {
					continue;
				}
				
				if (name.contains(_pc.getName())) {
					if (_pc.getNetConnection() != null) {
						_pc.getNetConnection().close();
					}
				}
			}
			
			_List.add(Item);
			_List_Count.add(count);
		}
	
		for (L1ItemInstance ItemList : _List) {
			if (!hasAdena(pc)) {
				break; /** 아데나 체크해서 빼주기 */
			}
			
			int Count = _List_Count.get(_List.indexOf(ItemList));
			clanWarehouse.tradeItem(ItemList, Count, pc.getInventory());
			ClanHistoryTable.getInstance().add(pc.getClan(), 1, pc.getName(), ItemList.getName(), Count);
		}
		
		_List.clear();
		_List_Count.clear();
		clanWarehouse.unlock(pc.getId());
	}

	private void putItemToClanWarehouse(L1PcInstance pc, int size) {
		if (size == 0) {
			doNothingClanWarehouse(pc);
			return;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());

		if (!isAvailableClan(pc, clan)) {
			return;
		}

		L1Object object = null;
		L1ItemInstance item = null;
		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		if (clanWarehouse == null) {
			return;
		}

		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();

			object = pc.getInventory().getItem(objectId);
			item = (L1ItemInstance) object;
			if (item == null) {
				break;
			}
			
			if (count > item.getCount()) {
				count = item.getCount();
			}
			
			if (!isAvailableTrade(pc, objectId, item, count)) {
				break;
			}

			if (item.getItem().getItemId() == 411591 || item.getItemId() == 40308) { // 깃털,아덴 창고 사용 금지
				pc.sendPackets(new S_ServerMessage(210, item.getItem() .getName()), true); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				return;
			}
			
			if (item.getItem().getItemId() == 49312 || item.getItem().getItemId() == 40312) {
				pc.sendPackets(new S_SystemMessage("해당 아이템은 창고 이용을 할 수 없습니다."), true);
				break;
			}
			
			if (item.getBless() >= 128 || !item.getItem().isTradable()) {
				// \f1%0은 버리거나 또는 타인에게 양도 할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(210, item.getItem() .getName()), true);
				break;
			}
			
			if (item.getItem().getItemId() == 430116 || item.getItemId() == 21255 || item.getItemId() == 437011) { // 21255 용의호박갑옷
				pc.sendPackets(new S_SystemMessage("해당 아이템은 혈맹 창고 이용을 할 수 없습니다."), true);
				break;
			}
			
			/** 아이템 오브젝트가 펫목걸이고 그목거리가 망각이 아니라면 */
			if (item.isDogNecklace() && !pc.isGm()) {
				// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
				pc.sendPackets(new S_ServerMessage(210, item.getViewName()), true);
				return;
			}
			
			if (!checkPetList(pc, item)) {
				break;
			}
			
			if (!isAvailableWhCount(clanWarehouse, pc, item, count)) {
				break;
			}
			
			ArrayList<String> name = new ArrayList<String>();
			name = L1PcInstance.getPCs(pc.getAccountName());
			
			for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
				if (_pc.getName() == pc.getName()) {
					continue;
				}
				
				if (name.contains(_pc.getName())) {
					if (_pc.getNetConnection() != null) {
						_pc.getNetConnection().close();
					}
				}
			}
			
			L1ItemInstance itemExist = clanWarehouse.findItemId(item.getItemId());
			
			if (itemExist != null && ((itemExist.getCount() + count) > 2000000000 || (itemExist.getCount() + count) < 0)) {
				pc.sendPackets(new S_SystemMessage("해당 아이템의 소지 갯수가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
				return;
			}
			
			pc.getInventory().tradeItem(objectId, count, clanWarehouse);

			ClanHistoryTable.getInstance().add(pc.getClan(), 0, pc.getName(), item.getName(), count);
			LogTable.logcwarehouse(pc, item, count, 0);
		}
		
		clanWarehouse.unlock(pc.getId());

	}

	private void getItemToPrivateWarehouse(L1PcInstance pc, int size) {
		PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
		if (warehouse == null) {
			return;
		}
		
		/** 사이즈 만큼 아데나가 있는지 체크 그아데나가 없다면 리턴 */
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, 100 * size)) {
			pc.sendPackets(new S_ServerMessage(189), true);
			return;
		}
		
		ArrayList<L1ItemInstance> _List = new ArrayList<L1ItemInstance>();
		ArrayList<Integer> _List_Count = new ArrayList<Integer>();
	
		L1ItemInstance Item = null;
		for (int i = 0, number, count; i < size; i++) {
			number = readD();
			count = readD();
			Item = warehouse.getItems().get(number);
			if (!isAvailableTrade(pc, Item, count)) {
				break;
			}
			
			if (!isAvailablePcWeight(pc, Item, count)) {
				break;
			}
			
			if (count > Item.getCount()) count = Item.getCount();
			ArrayList<String> name = new ArrayList<String>();
			name = L1PcInstance.getPCs(pc.getAccountName());
			
			for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
				if (_pc.getName() == pc.getName()) {
					continue;
				}
				
				if (name.contains(_pc.getName())) {
					if (_pc.getNetConnection() != null) {
						_pc.getNetConnection().close();
					}
				}
			}
			
			_List.add(Item);
			_List_Count.add(count);
		}
		
		for (L1ItemInstance ItemList : _List) {
			if (!hasAdena(pc)) break; /** 아데나 체크해서 빼주기 */
			int Count = _List_Count.get(_List.indexOf(ItemList));
			warehouse.tradeItem(ItemList, Count , pc.getInventory());
		}
		
		_List.clear();
		_List_Count.clear();
	}

	private void putItemToPrivateWarehouse(L1PcInstance pc, int size) {
		L1Object object = null;
		L1ItemInstance item = null;
		PrivateWarehouse warehouse = WarehouseManager.getInstance() .getPrivateWarehouse(pc.getAccountName());
		
		if (warehouse == null) {
			return;
		}

		for (int i = 0, objectId, count; i < size; i++) {
			objectId = readD();
			count = readD();

			object = pc.getInventory().getItem(objectId);
			item = (L1ItemInstance) object;
			if (pc.getLevel() < Config.ALT_PRIVATE_WAREHOUSE_LEVEL) {
				pc.sendPackets(new S_SystemMessage(Config.ALT_PRIVATE_WAREHOUSE_LEVEL + "렙이하는 창고를 이용을 할 수 없습니다."), true);
				break;
			}

			if (item == null) {
				break;
			}
			
			/** 드다,드에메,드진주,드루비,드사파, 마족무기보호주문서, 판도라꽃향티 창고 넣을수있게 **/
			if (!((item.getItemId() >= 437010 && item.getItemId() <= 437013)
					/**기사단 장비*/
					|| (item.getItemId() >= 60716 && item.getItemId() <= 60725)
					|| (item.getItemId() >= 30098 && item.getItemId() <= 30104)
					|| (item.getItemId() >= 20615 && item.getItemId() <= 20626)
					/**기사단장비*/
					|| item.getItemId() == 5000067
					|| item.getItemId() == 60104
					|| (item.getItemId() >= 21125 && item.getItemId() <= 21136)
					|| (item.getItemId() >= 20452 && item.getItemId() <= 20455)
					|| (item.getItemId() >= 421000 && item.getItemId() <= 421020)
					|| (item.getItemId() >= 66757 && item.getItemId() <= 66788)
					|| item.getItemId() == 560025 || item.getItemId() == 560027
					|| item.getItemId() == 560028 || item.getItemId() == 41159
					|| (item.getItemId() >= 60286 && item.getItemId() <= 60289)
					|| item.getItemId() == 60354
					|| (item.getItemId() >= 40033 && item.getItemId() <= 40038)
					|| (item.getItemId() >= 60261 && item.getItemId() <= 60263)
					|| item.getItemId() == 60396 || item.getItemId() == 60398
					|| (item.getItemId() >= 60423 && item.getItemId() <= 60426)
					|| (item.getItemId() >= 60427 && item.getItemId() <= 60444)
					|| (item.getItemId() >= 60447 && item.getItemId() <= 60472)
					|| item.getItemId() == 21256 || item.getItemId() == 21257)
					|| item.getItemId() == 60492) {
				if (!item.getItem().isTradable()) {
					// \f1%0은 버리거나 또는 타인에게 양도 할 수 없습니다.
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()), true);
					break;
				}
			}
			
			if (okok(item.getItemId())) {
				pc.sendPackets(new S_SystemMessage("해당 아이템은 당분간 창고이용이 불가능합니다."), true);
				break;
			}
			
			// if(pc.getAccessLevel() == Config.GMCODE) return; // 운영자 창고 사용 금지
			// if (item.getId() == 41246) break; // 결정체 창고 사용 금지
			if (item.getItem().getItemId() == 430116
					|| item.getItem().getItemId() == 49312 || item.getItem().getItemId() == 437011
					|| item.getItem().getItemId() == 40312) {
				pc.sendPackets(new S_SystemMessage("해당 아이템은 창고 이용을 할 수 없습니다."), true);
				break;
			}
			
			if (!isAvailableTrade(pc, objectId, item, count)) {
				break;
			}
			
			if (!checkPetList(pc, item)) {
				break;
			}
			
			if (!isAvailableWhCount(warehouse, pc, item, count)) {
				break;
			}
			
			if (count > item.getCount()) {
				count = item.getCount();
			}

			ArrayList<String> name = new ArrayList<String>();
			name = L1PcInstance.getPCs(pc.getAccountName());
			for (L1PcInstance _pc : L1World.getInstance().getAllPlayers()) {
				if (_pc.getName() == pc.getName()) {
					continue;
				}
				
				if (name.contains(_pc.getName())) {
					if (_pc.getNetConnection() != null) {
						_pc.getNetConnection().close();
					}
				}
			}

			L1ItemInstance itemExist = warehouse.findItemId(item.getItemId());
			if (itemExist != null && ((itemExist.getCount() + count) > 2000000000 || (itemExist.getCount() + count) < 0)) {
				pc.sendPackets(new S_SystemMessage("해당 아이템의 소지 갯수가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
				return;
			}

			pc.getInventory().tradeItem(objectId, count, warehouse);
			LogTable.logwarehouse(pc, item, count, 0);
		}
	}

	private void sellItemToPrivateShop(L1PcInstance pc, L1Object findObject, int size) {
		int count;
		int order;
		ArrayList<L1PrivateShopBuyList> buyList;
		L1PrivateShopBuyList psbl;
		int itemObjectId;
		L1ItemInstance item = null;
		int buyPrice;
		int buyTotalCount;
		int buyCount;

		boolean[] isRemoveFromList = new boolean[8];

		L1PcInstance targetPc = null;
		if (findObject instanceof L1PcInstance) {
			targetPc = (L1PcInstance) findObject;
		}
		
		if (targetPc == null) {
			return;
		}
		
		buyList = targetPc.getBuyList();

		synchronized (buyList) {
			for (int i = 0; i < size; i++) {
				itemObjectId = readD();
				count = readCH();
				order = readC();
				item = pc.getInventory().getItem(itemObjectId);
				
				if (item == null) {
					continue;
				}

				if (item.getBless() >= 128){
					pc.sendPackets(new S_ServerMessage(210, item.getItem().getName())); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
					return;
				}
				
				if (!item.getItem().isTradable()) {
					pc.sendPackets(new S_ServerMessage(941), true); //거래 불가 아이템입니다.
					return;
				}

				psbl = (L1PrivateShopBuyList) buyList.get(order);
				buyPrice = psbl.getBuyPrice();
				buyTotalCount = psbl.getBuyTotalCount(); // 살 예정의 개수
				buyCount = psbl.getBuyCount(); // 산 누계
				
				if (buyTotalCount - buyCount == 0)
					break;
				
				if (count > buyTotalCount - buyCount) {
					count = buyTotalCount - buyCount;
				}
				
				int buyItemObjectId = psbl.getItemObjectId();
				L1ItemInstance buyItem = targetPc.getInventory().getItem(buyItemObjectId);

				if (buyItem == null) {
					return;
				}
				
				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(905)); // 장비 하고 있는 아이템은 판매할 수 없습니다.
					continue;
				}

				if (targetPc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 용량 중량 확인 및 메세지 송신
					for (int j = 0; j < count; j++) { // 오버플로우를 체크
						if (buyPrice * j > 2000000000 || buyPrice * j < 0) {
							targetPc.sendPackets(new S_ServerMessage(904, "2000000000"));// 총판 매가격은%d아데나를 초과할 수 없습니다.
							return;
						}
					}	
					
					/** 버그 방지 **/
					if (itemObjectId != item.getId()) {
						pc.sendPackets(new S_Disconnect());
						targetPc.sendPackets(new S_Disconnect());
						return;
					}

					if (count >= item.getCount()) {
						count = item.getCount();
					}

					if (item.getItemId() != buyItem.getItemId()) return;
					if (!item.isStackable() && count !=1) return;					
					if (item.getCount() <= 0 || count <= 0) return;
					if (buyPrice * count <= 0 || buyPrice * count > 2000000000) return;
					//** 개인상점 부분 비셔스 방어 **//	

					if (targetPc.getInventory().checkItem(L1ItemId.ADENA, count * buyPrice)) {
						L1ItemInstance adena = targetPc.getInventory().findItemId(L1ItemId.ADENA);
						if (adena == null) {
							break;
						}
						
						if (adena != null) {
							targetPc.getInventory().tradeItem(adena, count * buyPrice, pc.getInventory());
							pc.getInventory().tradeItem(item, count, targetPc.getInventory());
							
							String message = item.getItem().getName() + " (" + String.valueOf(count) + ")";						
							targetPc.sendPackets(new S_ServerMessage(877, pc.getName(), message));// %1%o에 판매했습니다.
							psbl.setBuyCount(count + buyCount);
							buyList.set(order, psbl);
							
							if (psbl.getBuyCount() == psbl.getBuyTotalCount()) { // 살 예정의 개수를 샀다
								isRemoveFromList[order] = true;
								try {
									pc.상점아이템매입삭제(targetPc.getId(), item.getItemId(), 0);
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								try {
									pc.상점아이템매입업데이트(targetPc.getId(), item.getItemId(), 0, psbl.getBuyTotalCount() - psbl.getBuyCount());
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							
							try {
								pc.saveInventory();
								targetPc.saveInventory();
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					} else {
						targetPc.sendPackets(new S_ServerMessage(189)); // \f1아데나가 부족합니다.
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(271)); // \f1상대가 물건을 너무 가지고 있어 거래할 수 없습니다.
					break;
				}
			}
			
			// 매점한 아이템을 리스트의 말미로부터 삭제
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					buyList.remove(i);
				}
			}
			
		}
	}

	private void sellItemToShop(L1PcInstance pc, int npcId, int size) {
		L1Shop shop = ShopTable.getInstance().get(npcId);
		L1ShopSellOrderList orderList = shop.newSellOrderList(pc);
		int itemNumber;
		long itemcount;

		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if (itemcount <= 0) {
				return;
			}
			
			orderList.add(itemNumber, (int) itemcount, pc);
		}
		
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			if (npcId == 100027) {// 시올
				shop.buyItemsFoodHub(orderList);
			} else if (npcId == 100028) {// 미엘
				shop.buyItemsFoodSauce(orderList);
			} else if (npcId == 100564) {// 환전
				shop.buyItems환전(orderList);
			} else if (npcId == 100605) {// 베리
				shop.buyItems베리(orderList);
			} else if (npcId == 77221) {// 오림
				shop.buyItems가넷(orderList);
			} else if (npcId == 77223) {// 오림
				shop.buyItems가넷2(orderList);
			} else  if (npcId >= 7001411 && npcId <=7001426) {// 특화코인 
				shop.buyItems특화코인(orderList);
				/**기사단의 주화*/
			} else if (npcId == 77227 || npcId == 77229 
					   || (npcId >= 10 && npcId <= 18)
					   || (npcId >= 30 && npcId <= 38)) {
				shop.buyItems주화(orderList);
			} else
				shop.buyItems(orderList);
		}
	}
	
	public class ItemObjectCountPair{
		public ItemObjectCountPair(int o, int c){
			objid = o;
			count = c;
		}
		
		public int objid;
		public int count;
	}

	private void sellItemToNpcShop(L1PcInstance pc, int npcId, int size) {
		L1Shop shop = null;
		if ((shop = NpcBuyShop.getInstance().get(npcId)) == null) {
			shop = NpcShopTable.getInstance().get(npcId);
		}
		
		if (shop == null) {
			return;
		}
		
		L1ShopSellOrderList orderList = shop.newSellOrderList(pc);

		int itemNumber;
		long itemcount;

		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if (itemcount <= 0) {
				return;
			}
			
			orderList.add(itemNumber, (int) itemcount, pc);
		}
		
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.buyItems(orderList);
		}
		
	}

	/** 영자 상점 엔피씨 샵에서 구입 */
	private void buyItemFromNpcShop(L1PcInstance pc, int npcId, int size) {
		L1Shop shop = null;
		if ((shop = NpcBuyShop.getInstance().get(npcId)) == null) {
			shop = NpcShopTable.getInstance().get(npcId);
		}
		
		if (shop == null) {
			return;
		}
		
		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int itemNumber;
		long itemcount;

		for (int i = 0; i < size; i++) {
			itemNumber = readD();
			itemcount = readD();
			if (itemcount <= 0 || itemcount >= 10000) {
				return;
			}
			
			/*
			 * if(size >= 2){ //동시에 다른물건을 살수없게 2개가 선택된다면, pc.sendPackets(new
			 * S_SystemMessage("\\fY한번에 서로 다른아이템을 구입할수없습니다.")); return; }
			 * if(pc.getMapId() == 360){//오렌시장을 잡템만 가능하게했기때문에, 오렌시장에선 15개씩 수량 이상
			 * 안사지게 if(itemcount > 15) { pc.sendPackets(new
			 * S_SystemMessage("\\fY최대구매수량 : 잡템류(15개씩) / 장비류(1개씩)")); return; }
			 * }
			 */

			orderList.add(itemNumber, (int) itemcount, pc);
		}
		
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.sellItems(pc, orderList);
			// 백섭복사 방지 수량성버그방지
			pc.saveInventory();
			// 백섭복사 방지 수량성버그방지
		}
	}
	
	private void buyItemFromPrivateShop(L1PcInstance pc, L1Object findObject, int size) {
		int order;
		int count;
		int price;
		ArrayList<L1PrivateShopSellList> sellList;
		L1PrivateShopSellList pssl;
		int itemObjectId;
		int sellPrice;
		int sellTotalCount;
		int sellCount;
		L1ItemInstance item;
		boolean[] isRemoveFromList = new boolean[8];

		L1PcInstance targetPc = null;
		if (findObject instanceof L1PcInstance) {
			targetPc = (L1PcInstance) findObject;
		}
		
		if (targetPc == null) {
			return;
		}

		sellList = targetPc.getSellList();

		synchronized (sellList) {
			// 품절이 발생해, 열람중의 아이템수와 리스트수가 다르다
			if (pc.getPartnersPrivateShopItemCount() != sellList.size()) {
				return;
			}
			
			if (pc.getPartnersPrivateShopItemCount() < sellList.size()) {
				return;
			}

			for (int i = 0; i < size; i++) { // 구입 예정의 상품
				order = readD();
				count = readD();
				
				pssl = (L1PrivateShopSellList) sellList.get(order);
				itemObjectId = pssl.getItemObjectId();
				sellPrice = pssl.getSellPrice();
				sellTotalCount = pssl.getSellTotalCount(); // 팔 예정의 개수
				sellCount = pssl.getSellCount(); // 판 누계
				item = targetPc.getInventory().getItem(itemObjectId);
				
				if (item == null) {
					break;
				}					

				long nowtime = System.currentTimeMillis();
				if (item.getItemdelay3() >= nowtime) {
					break;
				}
				
				if (count > sellTotalCount - sellCount) {
					count = sellTotalCount - sellCount;
				}	
				
				if (count <= 0) {
					break;
				}	
				
				if (item.isEquipped()) {
					pc.sendPackets(new S_ServerMessage(905, "")); // 장비 하고 있는 아이템 구매못하게.
					continue;
				}
				
				if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) { // 용량 중량 확인 및 메세지 송신					
					for (int j = 0; j < count; j++) { // 오버플로우를 체크
						if (sellPrice * j > 2000000000 || sellPrice * j < 0) {
							pc.sendPackets(new S_ServerMessage(904, "2000000000")); // 총판 매가격은%d아데나를 초과할 수 없습니다.
							return;
						}
					} 

					/** 개인상점 버그방지 **/  					
					if (itemObjectId != item.getId()) {
						pc.sendPackets(new S_Disconnect());
						targetPc.sendPackets(new S_Disconnect());
						return;
					}
					
					if (!item.isStackable() && count != 1) {
						pc.sendPackets(new S_Disconnect());
						targetPc.sendPackets(new S_Disconnect());
						return;
					}
					
					if (count <= 0 || item.getCount() <= 0 || item.getCount() < count) {
						pc.sendPackets(new S_Disconnect());
						targetPc.sendPackets(new S_Disconnect());
						return;
					}
					
					price = count * sellPrice;	
					if (price <= 0 || price > 2000000000)  {
						return;
					}
					
					if (!isAvailableTrade(pc, targetPc, itemObjectId, item, count)) {
					   break;
					}

					if (count >= item.getCount()) {
						count = item.getCount();
					}
					
					if (count > 9999) {
						break;						
					}
					
					/** 개인상점 버그방지 **/  

					if (pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
						L1ItemInstance adena = pc.getInventory().findItemId(L1ItemId.ADENA);
						if (targetPc != null && adena != null) {
							if (targetPc.getInventory().tradeItem(item, count, pc.getInventory()) == null) {
								return;
							}
							
							pc.getInventory().tradeItem(adena, price, targetPc.getInventory());
							String message = item.getItem().getName() + " (" + String.valueOf(count) + ")";						
							targetPc.sendPackets(new S_ServerMessage(877, pc.getName(), message));// %1%o에 판매했습니다.
									
									
							pssl.setSellCount(count + sellCount);							
							sellList.set(order, pssl);
							
							if (pssl.getSellCount() == pssl.getSellTotalCount()) { // 팔 예정의 개수를 팔았다
								isRemoveFromList[order] = true;
							}
							
							try {
								pc.상점아이템삭제(targetPc.getId(), item.getId(), 1);
								pc.saveInventory();
								targetPc.saveInventory();
							} catch (Exception e) {
								//_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
							}
						}
					} else {
						pc.sendPackets(new S_ServerMessage(189)); // \f1아데나가 부족합니다.
						break;
					}
				} else {
					pc.sendPackets(new S_ServerMessage(270)); // \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
					break;
				}
			}
			
			// 품절된 아이템을 리스트의 말미로부터 삭제
			for (int i = 7; i >= 0; i--) {
				if (isRemoveFromList[i]) {
					sellList.remove(i);
				}
			}
			
		}
	}

	private void buyItemFromShop(L1PcInstance pc, int npcId, int size) {
		if (size > 1000) {
			return;
		}
		
		if (npcId == 100800) { //인센스
			npcId = pc.getType();
		} else if (npcId == 77228) { 
			npcId = pc.getType() + 10;
		} else if (npcId == 77230) { 
			npcId = pc.getType() + 30;
		}
		
		/*
		 * 0000: 49 33 e4 69 1b 00 01 00 00 00 00 00 01 00 00 00
		 * I3.i............ 0010: 1b 69 f2 35 00 00 00 00 .i.5....
		 */

		/*
		 * 49 33 e4 69 1b 00 리절트타입 01 사이즈 00 언노운 00 00 00 00 01 00 00 00
		 * I3.i............ 1b 69 f2 35 00 00 00 00 .i.5....
		 */
		/*
		 * 49 12 2f 6b 1b 00 01 00 95 3a ff 00
		 * 
		 * 01 00 00 00 I./k.....:...... fa b3 f3 35 00 00 00 00 ...5....
		 */
		L1Shop shop = ShopTable.getInstance().get(npcId);
		L1ShopBuyOrderList orderList = shop.newBuyOrderList();
		int itemNumber;
		long itemcount;
		int un;
		
		for (int i = 0; i < size; i++) {

			if (npcId == 100725) {
				itemNumber = readH();
				un = readH();
			} else {
				itemNumber = readD();
			}

			if (itemNumber == 14997) {
				itemNumber = 0;
			} else if (itemNumber == 14998) {
				itemNumber = 1;
			} else if (itemNumber == 14825) {
				itemNumber = 2;
			} else if (itemNumber == 15212) {
				itemNumber = 3;
			} else if (itemNumber == 15002) {
				itemNumber = 4;
			} 

			itemcount = readD();
			
			if (itemcount <= 0 || itemcount >= 10000) {
				return;
			}

			if (npcId == 70017 && itemcount > 1) {
				itemcount = 1;
			}
			
			if (npcId == 80080 && itemcount > 1) {
				itemcount = 1;
			}
			
			orderList.add(itemNumber, (int) itemcount, pc);
		}
		
		int bugok = orderList.BugOk();
		if (bugok == 0) {
			shop.sellItems(pc, orderList);
		}
		
	}

	/**
	 * 월드상에 있는 모든 캐릭의 계정을 비교해 같은 계정이 있다면 true 없다면 false
	 * 
	 * @param c
	 *            L1PcInstance
	 * @return 있다면 true
	 */
	private boolean isTwoLogin(L1PcInstance c) {
		boolean bool = false;
		for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			// 무인PC 는 제외
			if (target == null) {
				continue;
			}
			
			if (target.noPlayerCK) {
				continue;
			}
			
			if (target.샌드백) {
				continue;
			}
			
			//
			if (c.getId() != target.getId() && !target.isPrivateShop()) {
				if (c.getNetConnection().getAccountName().equalsIgnoreCase(target.getNetConnection().getAccountName())) {
					bool = true;
					break;
				}
			}
			
		}
		
		return bool;
	}

	private boolean isOverMaxAdena(L1PcInstance pc, int sellPrice, int count) {
		if (sellPrice * count > 2000000000 || sellPrice * count < 0) {
			pc.sendPackets(new S_ServerMessage(904, "2000000000"), true);
			return true;
		}
		
		if (count < 0) {
			return true;
		}
		
		if (sellPrice < 0) {
			return true;
		}
		
		return false;
	}

	private boolean checkPetList(L1PcInstance pc, L1ItemInstance item) {
		L1DollInstance doll = null;
		
		for (Object dollObject : pc.getDollList()) {
			doll = (L1DollInstance) dollObject;
			if (item.getId() == doll.getItemObjId()) {
				pc.sendPackets(new S_SystemMessage("소환되어 있는 인형은 맞길수 없습니다."), true);
				return false;
			}
		}
		
		for (Object petObject : pc.getPetList()) {
			if (petObject instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) petObject;
				if (item.getId() == pet.getItemObjId()) {
					pc.sendPackets(new S_SystemMessage("소환되어 있는 펫은 맞길수 없습니다."), true);
					return false;
				}
			}
		}
		
		return true;
	}

	private boolean isAvailableWhCount(Warehouse warehouse, L1PcInstance pc, L1ItemInstance item, int count) {
		if (warehouse.checkAddItemToWarehouse(item, count) == L1Inventory.SIZE_OVER) {
			// \f1상대가 물건을 너무 가지고 있어 거래할 수 없습니다.
			pc.sendPackets(new S_ServerMessage(75), true);
			return false;
		}
		
		return true;
	}

	private boolean isAvailableClan(L1PcInstance pc, L1Clan clan) {
		if (pc.getClanid() == 0 || clan == null) {
			// \f1혈맹 창고를 사용하려면 혈맹에 가입하지 않으면 안됩니다.
			pc.sendPackets(new S_ServerMessage(208), true);
			return false;
		}
		
		return true;
	}
	
	private boolean isAvailableTrade(L1PcInstance pc, L1ItemInstance item, int count) {
		boolean result = true;
		if (item == null) {
			result = false;
		}
		
		if (!item.isStackable() && count != 1) {
			result = false;
		}
		
		if (item.getCount() <= 0 || item.getCount() > 2000000000) {
			result = false;
		}
		
		if (count <= 0 || count > 2000000000) {
			result = false;
		}
		
		if (!result) {
			pc.sendPackets(new S_Disconnect(), true);
		}
		
		return result;
	}

	private boolean isAvailablePcWeight(L1PcInstance pc, L1ItemInstance item, int count) {
		if (pc.getInventory().checkAddItem(item, count) != L1Inventory.OK) {
			// \f1 가지고 있는 것이 무거워서 거래할 수 없습니다.
			pc.sendPackets(new S_ServerMessage(270), true);
			return false;
		}
		
		return true;
	}

	private boolean hasAdena(L1PcInstance pc) {
		if (!pc.getInventory().consumeItem(L1ItemId.ADENA, 100)) {
			// \f1아데나가 부족합니다.
			pc.sendPackets(new S_ServerMessage(189), true);
			return false;
		}
		
		return true;
	}
	
	private boolean hasMithril(L1PcInstance pc) {
		if (!pc.getInventory().consumeItem(40494, 4)) {
			// \f1아데나가 부족합니다.
			pc.sendPackets(new S_ServerMessage(189), true);
			return false;
		}
		
		return true;
	}

	private boolean isAvailableTrade(L1PcInstance pc, int objectId, L1ItemInstance item, int count) {
		boolean result = true;

		if (item == null) {
			result = false;
		}
		
		if (objectId != item.getId()) {
			result = false;
		}
		
		if (!item.isStackable() && count != 1) {
			result = false;
		}
		
		if (item.getCount() <= 0 || item.getCount() > 2000000000) {
			result = false;
		}
		
		if (count <= 0 || count > 2000000000) {
			result = false;
		}

		if (!result) {
			pc.sendPackets(new S_Disconnect(), true);
		}
		
		return result;
	}

	private boolean isAvailableTrade(L1PcInstance pc, L1PcInstance targetPc, int itemObjectId, L1ItemInstance item, int count) {
		boolean result = true;

		if (itemObjectId != item.getId() || !item.isStackable() && count != 1)
			result = false;
		if (count <= 0 || item.getCount() <= 0 || item.getCount() < count)
			result = false;

		if (!result) {
			pc.sendPackets(new S_Disconnect(), true);
			targetPc.sendPackets(new S_Disconnect(), true);
		}

		return result;
	}

	
	
	@Override
	public String getType() {
		return "[C] C_ShopAndWarehouse";
	}
	
	
}