package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayDeque;

import l1j.server.MJCTSystem.MJCTCharInfo;
import l1j.server.MJCTSystem.MJCTSpell;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.ItemClientCode;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.BinaryOutputStream;

/** MJCTSystem **/
public class S_CTPacket extends ServerBasePacket {

	public boolean NonValue = false;
	private static BinaryOutputStream os;

	public static S_CTPacket getCharacterInfo(MJCTCharInfo info) {
		S_CTPacket pck = new S_CTPacket();
		pck.writeC(Opcodes.S_BOARD_READ);
		pck.writeD(1);
		pck.writeS(info.name);
		pck.writeS("캐릭터 정보");
		pck.writeS("");
		pck.writeS(info.toString());
		return pck;
	}

	public static S_CTPacket getSpellList(ArrayDeque<MJCTSpell> spQ) {
		S_CTPacket pck = new S_CTPacket();
		L1ItemInstance Item = ItemTable.getInstance().createItem(40005);
		int size = spQ.size();
		if (size > 0) {
			pck.writeC(Opcodes.S_EXTENDED_PROTOBUF);
			pck.writeC(0x08);
			pck.writeC(0x04);

			pck.writeC(0x08);
			pck.writeBit(Item.getId());

			/** 겟수 체크 */
			pck.writeC(0x10);
			pck.writeBit(size);

			/** 상점 패킷 넘버 */
			pck.writeC(0x18);
			pck.writeBit(0x03);

			/** 금액 부분 */
			pck.writeC(0x20);
			pck.writeBit(0x64);

			while (!spQ.isEmpty()) {
				int i = 0;
				MJCTSpell sp = spQ.poll();
				byte[] Status = null;
				os = new BinaryOutputStream();
				if (Item.getItem().getType2() == 0) {
					os.writeC(0x58);
					os.writeBit(0x00);

					os.writeC(0x70);
					os.writeBit(Item.getWarehouseType());
				} else {
					os.writeC(0x58);
					os.writeBit(0x00);

					if (Item.getEnchantLevel() >= 1) {
						os.writeC(0x68);
						os.writeBit(Item.getEnchantLevel());
					}

					os.writeC(0x70);
					os.writeBit(Item.getWarehouseType());

					if (Item.getAttrEnchantLevel() >= 1) {
						int[] 속성값 = Item.getAttrEnchant(Item.getAttrEnchantLevel());
						os.writeC(0x80);
						os.writeC(0x01);
						os.writeBit(속성값[0]);
						os.writeC(0x88);
						os.writeC(0x01);
						os.writeBit(속성값[1]);
					}
				}

				int Type = Item.getStatusType();
				int DescId = ItemClientCode.code(Item.getItemId());
				int Length = 13 + pck.size7B(Item.getId()) + pck.size7B(Item.getItemId()) + pck.size7B(DescId) + pck.size7B(Type) + pck.size7B(Item.getCount())
						+ pck.size7B(sp.xicon) + sp.name.getBytes().length + os.getBytes().length;
				if (Item.getItem().getUseType() == 30)
					Length += 2;
				if (Item.isIdentified())
					Status = Item.getStatusBytes();
				byte[] DogCollar = null;
				if (Status != null) {
					Length += Status.length + 3;
					DogCollar = S_PetWindow.DogCollar(Item);
					if (DogCollar != null) {
						Length += DogCollar.length;
					}
				}

				/** 아이템 대한 기본 상점 정보 */
				pck.writeC(0x32);
				pck.writeBit(Length + pck.size7B(i) + pck.size7B(Length) + 2);

				/** 아잉템 순번 */
				pck.writeC(0x08);
				pck.writeBit(i);

				/** 아이템정보의 사이즈 길이 */
				pck.writeC(0x12);
				pck.writeBit(Length);

				pck.writeC(0x08);
				pck.writeBit(Item.getId());

				pck.writeC(0x10);
				pck.writeBit(DescId);

				pck.writeC(0x18);
				pck.writeBit(Item.getItemId());

				pck.writeC(0x20);
				pck.writeBit(Item.getCount());

				pck.writeC(0x28);
				pck.writeBit(Item.getItem().getUseType());

				if (Item.getItem().getUseType() == 30) {
					int skillid = Item.getItemId() - 40859;
					pck.writeC(0x30);
					pck.writeBit(skillid);
				}

				pck.writeC(0x38);
				pck.writeBit(sp.xicon);

				pck.writeC(0x40);
				int Bless = Item.getBless() >= 128 ? Item.getBless() - 128 : Item.getBless();
				pck.writeBit(Bless);

				/** 교환 가능 불가능 세팅 */
				pck.writeC(0x48);
				pck.write7B(Type);

				pck.writeByte(os.getBytes());
				os = null;

				pck.writeC(0x92);
				pck.writeC(0x01);
				pck.writeBit(sp.name.getBytes().length);
				pck.writeByte(sp.name.getBytes());
				if (Status != null) {
					/** 확인 표기부분 */
					pck.writeC(0x9a);
					pck.writeC(0x01);
					pck.writeBit(Status.length);
					pck.writeByte(Status);
					if (DogCollar != null) {
						pck.writeByte(DogCollar);
					}
					Status = null;
					DogCollar = null;
				}
				i++;
			}
			pck.writeC(0x38);
			pck.writeBit(0x01);
			pck.writeH(0x00);
		} else {
			pck.NonValue = true;
		}
		spQ.clear();
		return pck;
	}

//	public static S_CTPacket getInvList(ArrayDeque<MJCTItem> itemQ){
//		   S_CTPacket pck = new S_CTPacket();
//		   int size = itemQ.size();
//			if (size > 0) {
//				pck.writeC(Opcodes.S_EXTENDED_PROTOBUF);
//				pck.writeC(0x08);
//				pck.writeC(0x04);
//				
//				pck.writeC(0x08);
//				pck.writeBit(287852488);
//				
//				/** 겟수 체크 */
//				pck.writeC(0x10);
//				pck.writeBit(size);
//				
//				/** 상점 패킷 넘버 */
//				pck.writeC(0x18);
//				pck.writeBit(0x03);
//				
//				/** 금액 부분 */
//				pck.writeC(0x20);
//				pck.writeBit(0x64);
//				
//				L1ItemInstance Item1 = null;
//				while(!itemQ.isEmpty()){
//					int i = 0;
//					byte[] Status = null;
//					os = new BinaryOutputStream();
//					MJCTItem item = itemQ.poll();
//					Item1 = new L1ItemInstance();
//					Item1.setItem(item.item);					
//					if (Item1.getItem().getType2() == 0) {
//						os.writeC(0x58); 
//						os.writeBit(0x00);
//						
//						os.writeC(0x70); 
//						os.writeBit(Item1.getWarehouseType());
//					}else{						
//						os.writeC(0x58); 
//						os.writeBit(0x00);
//						
//						if(Item1.getEnchantLevel() >= 1){
//							os.writeC(0x68); 
//							os.writeBit(Item1.getEnchantLevel());
//						}
//						
//						os.writeC(0x70); 
//						os.writeBit(Item1.getWarehouseType());
//						
//						if(Item1.getAttrEnchantLevel() >= 1){
//							int[] 속성값 = Item1.getAttrEnchant(Item1.getAttrEnchantLevel());
//							os.writeC(0x80);
//							os.writeC(0x01);
//							os.writeBit(속성값[0]);
//							os.writeC(0x88);
//							os.writeC(0x01);
//							os.writeBit(속성값[1]);
//						}
//					}
//	
//					int Type = Item1.getStatusType();
//					int DescId = ItemClientCode.code(Item1.getItemId());
//					int Length = 13 + pck.size7B(Item1.getId()) + pck.size7B(Item1.getItemId()) + pck.size7B(DescId) + pck.size7B(Type) +
//							pck.size7B(Item1.getCount()) + pck.size7B(Item1.get_gfxid()) + Item1.getViewName().getBytes().length + os.getBytes().length;
//					if(Item1.getItem().getUseType() == 30) {
//						Length += 2;
//					}
//					if(item.iden == 1) {
//						Status = Item1.getStatusBytes();
//					}
//					byte[] DogCollar = null;
//					if(Status != null){
//						Length += Status.length + 3;
//						DogCollar = S_PetWindow.DogCollar(Item1);
//						if(DogCollar != null){
//							Length += DogCollar.length;
//						}
//					}
//				
//					/** 아이템 대한 기본 상점 정보 */
//					pck.writeC(0x32);
//					pck.writeBit(Length + pck.size7B(i) + pck.size7B(Length) + 2);
//					
//					/** 아잉템 순번 */
//					pck.writeC(0x08);
//					pck.writeBit(i);
//					
//					/** 아이템정보의 사이즈 길이 */
//					pck.writeC(0x12);
//					pck.writeBit(Length);
//					
//					pck.writeC(0x08);
//					pck.writeBit(Item1.getId());
//					
//					pck.writeC(0x10);
//					pck.writeBit(DescId);
//					
//					pck.writeC(0x18);
//					pck.writeBit(Item1.getItemId());
//					
//					pck.writeC(0x20);
//					pck.writeBit(Item1.getCount());
//					
//					pck.writeC(0x28); 
//					pck.writeBit(Item1.getItem().getUseType());
//					
//					if(Item1.getItem().getUseType() == 30){
//						int skillid = Item1.getItemId() - 40859;
//						pck.writeC(0x30); 
//						pck.writeBit(skillid);
//					}
//					
//					pck.writeC(0x38); 
//					pck.writeBit(Item1.get_gfxid());
//					
//					pck.writeC(0x40); 
//					pck.writeBit(item.iden == 1 ? 1 : 0);
//					
//					/** 교환 가능 불가능 세팅 */
//					pck.writeC(0x48); 
//					pck.write7B(Type);
//					
//					pck.writeByte(os.getBytes());
//					os = null;
//					
//					pck.writeC(0x92); 
//					pck.writeC(0x01);
//					pck.writeBit(Item1.getViewName().getBytes().length);
//					pck.writeByte(Item1.getViewName().getBytes()); 
//					if(Status != null){
//						/** 확인 표기부분 */
//						pck.writeC(0x9a); 
//						pck.writeC(0x01);
//						pck.writeBit(Status.length);
//						pck.writeByte(Status);
//						if(DogCollar != null){
//							pck.writeByte(DogCollar);
//						}
//						Status = null;
//						DogCollar = null;
//					}
//				}
//				pck.writeC(0x38); 
//				pck.writeBit(0x01);
//				
//				pck.writeH(0x00);
//			} else {
//				pck.NonValue = true;
//			}
//			itemQ.clear();
//			return pck;
//	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}
}
