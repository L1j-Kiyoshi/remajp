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

import l1j.server.server.Opcodes;
import l1j.server.server.model.ItemClientCode;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_TradeAddItem extends ServerBasePacket {
	private static final String S_TRADE_ADD_ITEM = "[S] S_TradeAddItem";
	
	private BinaryOutputStream os;

	public S_TradeAddItem(L1ItemInstance Item, int count, int type) {
		
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x09);
		writeC(0x04);
		
		writeC(0x08);
		/** 0:교환창 상단 1:교환창 하단 */
		writeBit(type);
		
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
			size7B(count) + size7B(Item.get_gfxid()) + Item.getNumberedViewName(count).getBytes().length + os.getBytes().length;
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
		writeBit(count);
		
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
		writeBit(Item.getNumberedViewName(count).getBytes().length);
		writeByte(Item.getNumberedViewName(count).getBytes()); 
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
		
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_TRADE_ADD_ITEM;
	}
}
