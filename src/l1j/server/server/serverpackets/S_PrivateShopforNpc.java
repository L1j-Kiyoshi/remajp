package l1j.server.server.serverpackets;

import java.util.ArrayList;
import java.util.List;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.model.ItemClientCode;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.BinaryOutputStream;

public class S_PrivateShopforNpc extends ServerBasePacket {
	
	/** 자동상점에 대한 패킷 정리 */
	public S_PrivateShopforNpc(L1PcInstance pc, int objId, int type) {
		try {
			L1NpcInstance Npc = (L1NpcInstance) L1World.getInstance().findObject(objId);
			L1Shop Shop = NpcShopTable.getInstance().get(Npc.getNpcId());
			
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeC(0x07);
			writeC(0x04);
			
			/** 패킷 타입 분석 */
			writeC(0x08);
			writeBit(type);
			
			/** 엔피씨 오브젝트 */
			writeC(0x10);
			writeBit(Npc.getId());
	
			int Size = 0;
			BinaryOutputStream os = null;
			L1ShopItem ShopItem;
			switch (type) {	
				case 0:
					List<L1ShopItem> ShopItems = Shop.getSellingItems();
					Size = ShopItems.size();
					if(Size == 0) return;
					
					writeC(0x18);
					writeBit(Size);
					pc.setPartnersPrivateShopItemCount(Size);
					L1ItemInstance Item = new L1ItemInstance();
					for (int i = 0; i < Size; i++) {
						ShopItem = ShopItems.get(i);
						/** liTem 정보로 갱신 하도록 설정 */
						L1Item Items = ShopItem.getItem();
						/** 아이템 정보를 변경 해줌 */
						Item = ItemTable.getInstance().createItem(Items.getItemId());
						/** 아이템 정보 처리하기위해 변경 */
						Item.setIdentified(true);
						Item.setCount(ShopItem.getCount());
						Item.setEnchantLevel(ShopItem.getEnchant());
						byte[] Status = null;
						os = new BinaryOutputStream();
						if (Item.getItem().getType2() == 0) {
							os.writeC(0x58); 
							os.writeBit(0x00);
							
							os.writeC(0x70); 
							os.writeBit(Item.getWarehouseType());
						}else{						
							os.writeC(0x58); 
							os.writeBit(0x00);
							
							if(Item.getEnchantLevel() >= 1){
								os.writeC(0x68); 
								os.writeBit(Item.getEnchantLevel());
							}
							
							os.writeC(0x70); 
							os.writeBit(Item.getWarehouseType());
							
							if(Item.getAttrEnchantLevel() >= 1){
								int[] 속성값 = Item.getAttrEnchant(Item.getAttrEnchantLevel());
								os.writeC(0x80);
								os.writeC(0x01);
								os.writeBit(속성값[0]);
								os.writeC(0x88);
								os.writeC(0x01);
								os.writeBit(속성값[1]);
							}
						}
						
						int Count = ShopItem.getCount();
						int Price = ShopItem.getPrice();
						int Type = Item.getStatusType();
						int DescId = ItemClientCode.code(Item.getItemId());
						int Length = 13 + size7B(Item.getId()) + size7B(Item.getItemId()) + size7B(DescId) + size7B(Type) +
							size7B(Count) + size7B(Item.get_gfxid()) + Item.getNumberedViewName(Count).getBytes().length + os.getBytes().length;
						if(Item.getItem().getUseType() == 30) Length += 2;
						if(Item.isIdentified()) Status = Item.getStatusBytes();
						if(Status != null) Length += Status.length + 3;
					
						/** 아이템 대한 기본 상점 정보 */
						writeC(0x22);
						writeBit(Length + size7B(Count) + size7B(Price) + 6);
						
						/** 아잉템 순번 */
						writeC(0x08);
						writeBit(i);
						
						/** 판매할 아이템 겟수 */
						writeC(0x10);
						writeBit(Count);
						
						/** 판매할 아이템의 금액 */
						writeC(0x18);
						writeBit(Price);
						
						/** 아이템정보의 사이즈 길이 */
						writeC(0x22);
						writeBit(Length);
						
						writeC(0x08);
						writeBit(Item.getId());
						
						writeC(0x10);
						writeBit(DescId);
						
						writeC(0x18);
						writeBit(Item.getItemId());
						
						writeC(0x20);
						writeBit(Count);
						
						writeC(0x28); 
						writeBit(Item.getItem().getUseType());
						
						if(Item.getItem().getUseType() == 30){
							int skillid = Item.getItemId() - 40859;
							writeC(0x30); 
							writeBit(skillid);
						}
						
						writeC(0x38); 
						writeBit(Item.get_gfxid());
						
						writeC(0x40); 
						writeBit(Item.getBless());
						
						/** 교환 가능 불가능 세팅 */
						writeC(0x48); 
						write7B(Type);
						
						writeByte(os.getBytes());
						os = null;
						
						writeC(0x92); 
						writeC(0x01);
						writeBit(Item.getNumberedViewName(Count).getBytes().length);
						writeByte(Item.getNumberedViewName(Count).getBytes()); 
						if(Status != null){
							/** 확인 표기부분 */
							writeC(0x9a); 
							writeC(0x01);
							writeBit(Status.length);
							writeByte(Status);
							Status = null;
						}
					}
					Item = null;
					break;
						
				case 1:
					List<L1ShopItem> List = Shop.getPurchasingItems();
					
					ArrayList<L1ShopItem> ListCheck = new ArrayList<L1ShopItem>();
					for (L1ShopItem ListBuyCheck : List) {
						L1ItemInstance Items = pc.getInventory().findItemId(ListBuyCheck.getItemId());
						if (Items != null && !Items.isEquipped() && 
							Items.getItemId() == ListBuyCheck.getItemId() && 
							Items.getEnchantLevel() == ListBuyCheck.getEnchant() && 
							Items.getAttrEnchantLevel() == ListBuyCheck.getAttr() && 
							Items.getBless() == ListBuyCheck.getBless()) {
							ListCheck.add(ListBuyCheck);
						}
					}
					Size = ListCheck.size();
					if(Size == 0) return;
					
					writeC(0x18);
					writeBit(Size);
					
					for (int i = 0; i < Size; i++) {
						ShopItem = ListCheck.get(i);
						Item = pc.getInventory().findItemId(ShopItem.getItemId());
						byte[] Status = null;
						os = new BinaryOutputStream();
						if (Item.getItem().getType2() == 0) {	
							os.writeC(0x58); 
							os.writeBit(0x00);
							
							os.writeC(0x70); 
							os.writeBit(Item.getWarehouseType());
						}else{						
							os.writeC(0x58); 
							os.writeBit(0x00);
							
							if(Item.getEnchantLevel() >= 1){
								os.writeC(0x68); 
								os.writeBit(Item.getEnchantLevel());
							}
							
							os.writeC(0x70); 
							os.writeBit(Item.getWarehouseType());
							
							if(Item.getAttrEnchantLevel() >= 1){
								int[] 속성값 = Item.getAttrEnchant(Item.getAttrEnchantLevel());
								os.writeC(0x80);
								os.writeC(0x01);
								os.writeBit(속성값[0]);
								os.writeC(0x88);
								os.writeC(0x01);
								os.writeBit(속성값[1]);
							}
						}
						
						int Count = ShopItem.getCount();
						int Price = ShopItem.getPrice();
						int Type = Item.getStatusType();
						int DescId = ItemClientCode.code(Item.getItemId());
						int Length = 13 + size7B(Item.getId()) + size7B(Item.getItemId()) + size7B(DescId) + size7B(Type) +
							size7B(Count) + size7B(Item.get_gfxid()) + Item.getNumberedViewName(Count).getBytes().length + os.getBytes().length;
						if(Item.getItem().getUseType() == 30) Length += 2;
						if(Item.isIdentified()) Status = Item.getStatusBytes();
						if(Status != null) Length += Status.length + 3;
						
						/** 아이템 대한 기본 상점 정보 */
						writeC(0x22);
						writeBit(Length + size7B(Count) + size7B(Price) + 6);
						
						/** 아잉템 순번 */
						writeC(0x08);
						writeBit(Shop.getPurchasingItems().indexOf(ShopItem));
						
						/** 판매할 아이템 겟수 */
						writeC(0x10);
						writeBit(Count);
						
						/** 판매할 아이템의 금액 */
						writeC(0x18);
						writeBit(Price);
						
						/** 아이템정보의 사이즈 길이 */
						writeC(0x22);
						writeBit(Length);
						
						writeC(0x08);
						writeBit(Item.getId());
						
						writeC(0x10);
						writeBit(DescId);
						
						writeC(0x18);
						writeBit(Item.getItemId());
						
						writeC(0x20);
						writeBit(Count);
						
						writeC(0x28); 
						writeBit(Item.getItem().getUseType());
						
						if(Item.getItem().getUseType() == 30){
							int skillid = Item.getItemId() - 40859;
							writeC(0x30); 
							writeBit(skillid);
						}
						
						writeC(0x38); 
						writeBit(Item.get_gfxid());
						
						writeC(0x40); 
						writeBit(Item.getBless());
						
						/** 교환 가능 불가능 세팅 */
						writeC(0x48); 
						write7B(Type);
						
						writeByte(os.getBytes());
						os = null;
						
						writeC(0x92); 
						writeC(0x01);
						writeBit(Item.getNumberedViewName(Count).getBytes().length);
						writeByte(Item.getNumberedViewName(Count).getBytes()); 
						if(Status != null){
							/** 확인 표기부분 */
							writeC(0x9a); 
							writeC(0x01);
							writeBit(Status.length);
							writeByte(Status);
							Status = null;
						}
					}
					ListCheck.clear();
					ListCheck = null;
					break;
			}
			writeH(0x00);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
