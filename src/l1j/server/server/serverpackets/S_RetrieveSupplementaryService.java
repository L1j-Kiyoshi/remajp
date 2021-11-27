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
package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.Warehouse.SupplementaryService;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.Opcodes;
import l1j.server.server.model.ItemClientCode;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_RetrieveSupplementaryService extends ServerBasePacket {

	private static final String _S_RetrieveSupplementaryService = "[S] S_RetrieveSupplementaryService";

	private BinaryOutputStream os;
	
	public S_RetrieveSupplementaryService(int objid, L1PcInstance pc) {
		if (pc.getInventory().getSize() < 180) {
			SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
			int size = warehouse.getSize();
			if (size > 0) {
				writeC(Opcodes.S_EXTENDED_PROTOBUF);
				writeC(0x08);
				writeC(0x04);
				
				writeC(0x08);
				writeBit(objid);
				
				/** 겟수 체크 */
				writeC(0x10);
				writeBit(size);
				
				/** 상점 패킷 넘버 */
				writeC(0x18);
				writeBit(0x12);
				
				/** 금액 부분 */
				writeC(0x20);
				writeBit(0x00);
				
				L1ItemInstance Item = null;
				for (int i = 0; i < size; i++) {
					Item = (L1ItemInstance) warehouse.getItems().get(i);
					
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
	
					int Type = Item.getStatusType();
					int DescId = ItemClientCode.code(Item.getItemId());
					int Length = 13 + size7B(Item.getId()) + size7B(Item.getItemId()) + size7B(DescId) + size7B(Type) +
						size7B(Item.getCount()) + size7B(Item.get_gfxid()) + Item.getViewName().getBytes().length + os.getBytes().length;
					if(Item.getItem().getUseType() == 30) Length += 2;
					if(Item.isIdentified()) Status = Item.getStatusBytes();
					byte[] DogCollar = null;
					if(Status != null){
						Length += Status.length + 3;
						DogCollar = S_PetWindow.DogCollar(Item);
						if(DogCollar != null){
							Length += DogCollar.length;
						}
					}
				
					/** 아이템 대한 기본 상점 정보 */
					writeC(0x32);
					writeBit(Length + size7B(i) + size7B(Length) + 2);
					
					/** 아잉템 순번 */
					writeC(0x08);
					writeBit(i);
					
					/** 아이템정보의 사이즈 길이 */
					writeC(0x12);
					writeBit(Length);
					
					writeC(0x08);
					writeBit(Item.getId());
					
					writeC(0x10);
					writeBit(DescId);
					
					writeC(0x18);
					writeBit(Item.getItemId());
					
					writeC(0x20);
					writeBit(Item.getCount());
					
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
					int Bless = Item.getBless() >= 128 ? Item.getBless() - 128 : Item.getBless();
					writeBit(Bless);
					
					/** 교환 가능 불가능 세팅 */
					writeC(0x48); 
					write7B(Type);
					
					writeByte(os.getBytes());
					os = null;
					
					writeC(0x92); 
					writeC(0x01);
					writeBit(Item.getViewName().getBytes().length);
					writeByte(Item.getViewName().getBytes()); 
					if(Status != null){
						/** 확인 표기부분 */
						writeC(0x9a); 
						writeC(0x01);
						writeBit(Status.length);
						writeByte(Status);
						if(DogCollar != null){
							writeByte(DogCollar);
						}
						Status = null;
						DogCollar = null;
					}
				}
				writeC(0x38); 
				writeBit(0x01);
				
				writeH(0x00);
			}
		} else {
			/** \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다. */
			pc.sendPackets(new S_ServerMessage(263));
		}
	}

	@Override
	public byte[] getContent() throws IOException {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S_RetrieveSupplementaryService;
	}
}
