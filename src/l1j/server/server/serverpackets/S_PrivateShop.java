package l1j.server.server.serverpackets;

import java.util.ArrayList;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.utils.BinaryOutputStream;

public class S_PrivateShop extends ServerBasePacket {
	
	public S_PrivateShop(L1PcInstance pc, int objectId, int type) {
		try {
			L1PcInstance shopPc = (L1PcInstance) L1World.getInstance().findObject(objectId);

			if (shopPc == null) {
				return;
			}

			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeC(0x07);
			writeC(0x04);
			 
			writeC(0x08);
			writeC(type);
			writeC(0x10);
			writeBit(shopPc.getId());

			if (type == 0) { // 판매
				ArrayList<?> list = shopPc.getSellList();
				int size = list.size();
				if (size == 0) {
					pc.sendPackets(new S_ServerMessage(908), true); //등록 아이템 없음
					return;
				}
				pc.setPartnersPrivateShopItemCount(size);
				writeC(0x18);
				writeBit(size);
				L1PrivateShopSellList pssl = null;
				L1ItemInstance item = null;
				byte[] b;
				BinaryOutputStream detail;
				L1Item temp;
				for (int i = 0; i < size; i++) {
					pssl = (L1PrivateShopSellList) list.get(i);
					int itemObjectId = pssl.getItemObjectId();
					int count = pssl.getSellTotalCount() - pssl.getSellCount();
					int price = pssl.getSellPrice();
					item = shopPc.getInventory().getItem(itemObjectId);
					if (item != null) {
						detail = new BinaryOutputStream();
						temp = item.getItem();
						detail.writeC(0x08);
						detail.writeBit(item.getId());
						detail.writeC(0x10);
						detail.writeBit(temp.getItemDescId() == 0 ? -1 : temp.getItemDescId());
						detail.writeC(0x20);
						detail.writeBit(count);
						if (temp.getUseType() > 0){
							detail.writeC(0x28);
							detail.writeBit(temp.getUseType());
						}
						detail.writeC(0x38);
						detail.writeBit(item.get_gfxid());
						detail.writeC(0x40);
						detail.writeC(item.getBless());
						detail.writeC(0x48);
						detail.writeBit(item.getStatusType());
						detail.writeC(0x58);
						detail.writeBit(0);
						detail.writeC(0x68);
						detail.writeBit(item.getEnchantLevel());
						detail.writeC(0x70);
						detail.writeBit(item.getShowItemListBit());
						if (temp.getType2() == 1 && item.getAttrEnchantLevel() > 0){
							int attrlevel = item.getAttrEnchantLevel();
							int attrtype = 0;
							if (attrlevel >= 1 && attrlevel <= 5) {
								attrtype = 1;
							} else if (attrlevel >= 6 && attrlevel <= 10) {
								attrtype = 2;
							} else if (attrlevel >= 11 && attrlevel <= 15) {
								attrtype = 3;
							} else if (attrlevel >= 16 && attrlevel <= 20) {
								attrtype = 4;
							}
							detail.writeBit(128);
							detail.writeBit(attrtype);
							detail.writeBit(136);
							detail.writeBit(attrtype == 1 ? attrlevel : attrlevel - (5 * (attrtype -1)));
						}
						
						detail.writeBit(146);
						detail.writeS2(item.getNumberedName(count));
						
						if (item.isIdentified()){
							detail.writeBit(154);
							b = item.getStatusBytes();
							detail.writeBit(b.length);
							for (byte s : b)
								detail.writeC(s);
						}
						
						int bsize = getBitSize(detail.getLength() - 2) + getBitSize(i) +
								getBitSize(count) + getBitSize(price) + detail.getLength() + 2;
						writeC(0x22);
						writeBit(bsize);
						writeC(0x08);
						writeBit(i);
						writeC(0x10);
						writeBit(count);
						writeC(0x18);
						writeBit(price);
						
						writeC(0x22);
						writeBit(detail.getLength() - 2);
						writeByte(detail.getBytes());
						
					}
				}
				writeH(0x00);
			} else if (type == 1) { //매입
				ArrayList<?> list = shopPc.getBuyList();
				int size = list.size();
				if (size == 0) {
					pc.sendPackets(new S_ServerMessage(908), true); //등록 아이템 없음
					return;
				}
				pc.setPartnersPrivateShopItemCount(size);
				writeC(0x18);
				writeBit(size);
				L1PrivateShopBuyList pssl = null;
				L1ItemInstance item = null;
				byte[] b;
				BinaryOutputStream detail;
				L1Item temp;
				for (int i = 0; i < size; i++) {
					pssl = (L1PrivateShopBuyList) list.get(i);
					int itemObjectId = pssl.getItemObjectId();
					int count = pssl.getBuyTotalCount() - pssl.getBuyCount();
					int price = pssl.getBuyPrice();
					item = shopPc.getInventory().getItem(itemObjectId);
					if (item != null) {
						detail = new BinaryOutputStream();
						temp = item.getItem();
						detail.writeC(0x08);
						detail.writeBit(item.getId());
						detail.writeC(0x10);
						detail.writeBit(temp.getItemDescId() == 0 ? -1 : temp.getItemDescId());
						detail.writeC(0x20);
						detail.writeBit(count);
						if (temp.getUseType() > 0){
							detail.writeC(0x28);
							detail.writeBit(temp.getUseType());
						}
						detail.writeC(0x38);
						detail.writeBit(item.get_gfxid());
						detail.writeC(0x40);
						detail.writeC(item.getBless());
						detail.writeC(0x48);
						detail.writeBit(item.getStatusType());
						detail.writeC(0x58);
						detail.writeBit(0);
						detail.writeC(0x68);
						detail.writeBit(item.getEnchantLevel());
						detail.writeC(0x70);
						detail.writeBit(item.getShowItemListBit());
						if (temp.getType2() == 1 && item.getAttrEnchantLevel() > 0){
							int attrlevel = item.getAttrEnchantLevel();
							int attrtype = 0;
							if (attrlevel >= 1 && attrlevel <= 5) {
								attrtype = 1;
							} else if (attrlevel >= 6 && attrlevel <= 10) {
								attrtype = 2;
							} else if (attrlevel >= 11 && attrlevel <= 15) {
								attrtype = 3;
							} else if (attrlevel >= 16 && attrlevel <= 20) {
								attrtype = 4;
							}
							detail.writeBit(128);
							detail.writeBit(attrtype);
							detail.writeBit(136);
							detail.writeBit(attrtype == 1 ? attrlevel : attrlevel - (5 * (attrtype -1)));
						}
						
						detail.writeBit(146);
						detail.writeS2(item.getNumberedName(count));
						
						if (item.isIdentified()){
							detail.writeBit(154);
							b =item.getStatusBytes();
							detail.writeBit(b.length);
							for (byte s : b)
								detail.writeC(s);
						}
						
						int bsize = getBitSize(detail.getLength() - 2) + getBitSize(i) +
								getBitSize(count) + getBitSize(price) + detail.getLength() + 2;
						writeC(0x22);
						writeBit(bsize);
						writeC(0x08);
						writeBit(i);
						writeC(0x10);
						writeBit(count);
						writeC(0x18);
						writeBit(price);
						
						writeC(0x22);
						writeBit(detail.getLength() - 2);
						writeByte(detail.getBytes());
						
					}
				}
				writeH(0x00);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	private BinaryOutputStream os;
//	
//	public S_PrivateShop(L1PcInstance pc, int objectId, int type) {
//		try {
//			writeC(Opcodes.S_EXTENDED_PROTOBUF);
//			writeC(0x07);
//			writeC(0x04);
//			
//			L1PcInstance ShopPc = (L1PcInstance) L1World.getInstance().findObject(objectId);
//			if (ShopPc == null) return;
//			if (!pc.isGm() && pc.getAccountName().equalsIgnoreCase(ShopPc.getAccountName())) {
//				return;
//			}
//			
//			writeC(0x08);
//			writeBit(type);
//			
//			/** 엔피씨 오브젝트 */
//			writeC(0x10);
//			writeBit(ShopPc.getId());
//	
//			int Size = 0;
//			switch (type) {		
//				case 0:
//					ArrayList<MJDShopItem> ListSell = null;
//					ListSell = ShopPc.getSellings();
//					if(ListSell == null) return;
//					Size = ListSell.size();
//					if(Size == 0) return;
//					
//					/** 사이즈 체크 */
//					writeC(0x18);
//					writeBit(Size);
//	
//					pc.setPartnersPrivateShopItemCount(Size);
//					MJDShopItem PrivateSellShop = null;
//					for (int i = 0; i < Size; i++) {
//						PrivateSellShop = ListSell.get(i);
//						L1ItemInstance Item = ShopPc.getInventory().getItem(PrivateSellShop.objId);
//						byte[] Status = null;
//						os = new BinaryOutputStream();
//						if (Item.getItem().getType2() == 0) {
//							os.writeC(0x58); 
//							os.writeBit(0x00);
//							
//							os.writeC(0x70); 
//							os.writeBit(Item.getWarehouseType());
//						}else{						
//							os.writeC(0x58); 
//							os.writeBit(0x00);
//							
//							if(Item.getEnchantLevel() >= 1){
//								os.writeC(0x68); 
//								os.writeBit(Item.getEnchantLevel());
//							}
//							
//							os.writeC(0x70); 
//							os.writeBit(Item.getWarehouseType());
//							
//							if(Item.getAttrEnchantLevel() >= 1){
//								int[] 속성값 = Item.getAttrEnchant(Item.getAttrEnchantLevel());
//								os.writeC(0x80);
//								os.writeC(0x01);
//								os.writeBit(속성값[0]);
//								os.writeC(0x88);
//								os.writeC(0x01);
//								os.writeBit(속성값[1]);
//							}
//						}
//						
//						int Count = PrivateSellShop.count;
//						int Price = PrivateSellShop.price;
//						int Type = Item.getStatusType();
//						int DescId = ItemClientCode.code(Item.getItemId());
//						int Length = 13 + size7B(Item.getId()) + size7B(Item.getItemId()) + size7B(DescId) + size7B(Type) +
//							size7B(Count) + size7B(Item.get_gfxid()) + Item.getNumberedViewName(Count).getBytes().length + os.getBytes().length;
//						if(Item.getItem().getUseType() == 30) Length += 2;
//						if(Item.isIdentified()) Status = Item.getStatusBytes();
//						byte[] DogCollar = null;
//						if(Status != null){
//							Length += Status.length + 3;
//							DogCollar = S_PetWindow.DogCollar(Item);
//							if(DogCollar != null){
//								Length += DogCollar.length;
//							}
//						}
//					
//						/** 아이템 대한 기본 상점 정보 */
//						writeC(0x22);
//						writeBit(Length + size7B(Count) + size7B(Price) + 6);
//						
//						/** 아잉템 순번 */
//						writeC(0x08);
//						writeBit(i);
//						
//						/** 판매할 아이템 겟수 */
//						writeC(0x10);
//						writeBit(Count);
//						
//						/** 판매할 아이템의 금액 */
//						writeC(0x18);
//						writeBit(Price);
//						
//						/** 아이템정보의 사이즈 길이 */
//						writeC(0x22);
//						writeBit(Length);
//						
//						writeC(0x08);
//						writeBit(Item.getId());
//						
//						writeC(0x10);
//						writeBit(DescId);
//						
//						writeC(0x18);
//						writeBit(Item.getItemId());
//						
//						writeC(0x20);
//						writeBit(Count);
//						
//						writeC(0x28); 
//						writeBit(Item.getItem().getUseType());
//						
//						if(Item.getItem().getUseType() == 30){
//							int skillid = Item.getItemId() - 40859;
//							writeC(0x30); 
//							writeBit(skillid);
//						}
//						
//						writeC(0x38); 
//						writeBit(Item.get_gfxid());
//						
//						writeC(0x40); 
//						writeBit(Item.getBless());
//						
//						/** 교환 가능 불가능 세팅 */
//						writeC(0x48); 
//						write7B(Type);
//						
//						writeByte(os.getBytes());
//						os = null;
//						
//						writeC(0x92); 
//						writeC(0x01);
//						writeBit(Item.getNumberedViewName(Count).getBytes().length);
//						writeByte(Item.getNumberedViewName(Count).getBytes()); 
//						if(Status != null){
//							/** 확인 표기부분 */
//							writeC(0x9a); 
//							writeC(0x01);
//							writeBit(Status.length);
//							writeByte(Status);
//							if(DogCollar != null){
//								writeByte(DogCollar);
//							}
//							Status = null;
//							DogCollar = null;
//						}
//					}
//					break;
//					
//				case 1:
//					ArrayList<MJDShopItem> ListCheck = new ArrayList<MJDShopItem>();
//					if(ShopPc.getPurchasings() == null) return;
//					for (MJDShopItem ListBuyCheck : ShopPc.getPurchasings()) {
//						L1ItemInstance Item = ShopPc.getInventory().getItem(ListBuyCheck.objId);
//						for (L1ItemInstance PcItem : pc.getInventory().getItems()) {
//							if (!PcItem.isEquipped() && 
//								 PcItem.getItemId() == Item.getItemId() && 
//								 PcItem.getEnchantLevel() == Item.getEnchantLevel() && 
//								 PcItem.getAttrEnchantLevel() == Item.getAttrEnchantLevel() && 
//								 PcItem.getBless() == Item.getBless()) {
//								 ListCheck.add(ListBuyCheck);
//							}
//						}
//					}
//					
//					Size = ListCheck.size();
//					if(Size == 0) break;
//					
//					/** 사이즈 체크 */
//					writeC(0x18);
//					writeBit(Size);
//					
//					MJDShopItem PrivateBuyShop = null;
//					for (int i = 0; i < Size; i++) {
//						PrivateBuyShop = ListCheck.get(i);
//						L1ItemInstance Item = ShopPc.getInventory().getItem(PrivateBuyShop.objId);
//						byte[] Status = null;
//						os = new BinaryOutputStream();
//						if (Item.getItem().getType2() == 0) {
//							os.writeC(0x58); 
//							os.writeBit(0x00);
//							
//							os.writeC(0x70); 
//							os.writeBit(Item.getWarehouseType());
//						}else{						
//							os.writeC(0x58); 
//							os.writeBit(0x00);
//							
//							if(Item.getEnchantLevel() >= 1){
//								os.writeC(0x68); 
//								os.writeBit(Item.getEnchantLevel());
//							}
//							
//							os.writeC(0x70); 
//							os.writeBit(Item.getWarehouseType());
//							
//							if(Item.getAttrEnchantLevel() >= 1){
//								int[] 속성값 = Item.getAttrEnchant(Item.getAttrEnchantLevel());
//								os.writeC(0x80);
//								os.writeC(0x01);
//								os.writeBit(속성값[0]);
//								os.writeC(0x88);
//								os.writeC(0x01);
//								os.writeBit(속성값[1]);
//							}
//						}
//						
//						int Count = PrivateBuyShop.count;
//						int Price = PrivateBuyShop.price;
//						int Type = Item.getStatusType();
//						int DescId = ItemClientCode.code(Item.getItemId());
//						int Length = 13 + size7B(Item.getId()) + size7B(Item.getItemId()) + size7B(DescId) + size7B(Type) +
//							size7B(Count) + size7B(Item.get_gfxid()) + Item.getNumberedViewName(Count).getBytes().length + os.getBytes().length;
//						if(Item.getItem().getUseType() == 30) Length += 2;
//						if(Item.isIdentified()) Status = Item.getStatusBytes();
//						byte[] DogCollar = null;
//						if(Status != null){
//							Length += Status.length + 3;
//							DogCollar = S_PetWindow.DogCollar(Item);
//							if(DogCollar != null){
//								Length += DogCollar.length;
//							}
//						}
//					
//						/** 아이템 대한 기본 상점 정보 */
//						writeC(0x22);
//						writeBit(Length + size7B(Count) + size7B(Price) + 6);
//						
//						/** 아잉템 순번 */
//						writeC(0x08);
//						writeBit(i);
//						
//						/** 판매할 아이템 겟수 */
//						writeC(0x10);
//						writeBit(Count);
//						
//						/** 판매할 아이템의 금액 */
//						writeC(0x18);
//						writeBit(Price);
//						
//						/** 아이템정보의 사이즈 길이 */
//						writeC(0x22);
//						writeBit(Length);
//						
//						writeC(0x08);
//						writeBit(Item.getId());
//						
//						writeC(0x10);
//						writeBit(DescId);
//						
//						writeC(0x18);
//						writeBit(Item.getItemId());
//						
//						writeC(0x20);
//						writeBit(Count);
//						
//						writeC(0x28); 
//						writeBit(Item.getItem().getUseType());
//						
//						if(Item.getItem().getUseType() == 30){
//							int skillid = Item.getItemId() - 40859;
//							writeC(0x30); 
//							writeBit(skillid);
//						}
//						
//						writeC(0x38); 
//						writeBit(Item.get_gfxid());
//						
//						writeC(0x40); 
//						writeBit(Item.getBless());
//						
//						/** 교환 가능 불가능 세팅 */
//						writeC(0x48); 
//						write7B(Type);
//						
//						writeByte(os.getBytes());
//						os = null;
//						
//						writeC(0x92); 
//						writeC(0x01);
//						writeBit(Item.getNumberedViewName(Count).getBytes().length);
//						writeByte(Item.getNumberedViewName(Count).getBytes()); 
//						if(Status != null){
//							/** 확인 표기부분 */
//							writeC(0x9a); 
//							writeC(0x01);
//							writeBit(Status.length);
//							writeByte(Status);
//							if(DogCollar != null){
//								writeByte(DogCollar);
//							}
//							Status = null;
//							DogCollar = null;
//						}
//					}
//					break;	
//				
//			}
//			writeH(0x00);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
