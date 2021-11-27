package l1j.server.server.serverpackets; 

import java.io.IOException; 
import java.util.List; 
import java.util.Map; 

import l1j.server.server.Opcodes; 

public class S_InventorySwap extends ServerBasePacket { 
	private static final String S_INVENTORYSWAP = "[S] S_InventorySwap"; 

	public S_InventorySwap(int index) { 
	buildPacket(index); 
	} 

	public S_InventorySwap(int index, Map<Integer, List<Integer>> swap) { 
	buildPacket(index, swap); 
	} 

	public void buildPacket(int index) { 
	writeC(Opcodes.S_EXTENDED_PROTOBUF); 
	writeH(0x0320); 
	writeC(0x08); 
	writeC(index); 
	writeH(0x00); 
	} 

	public void buildPacket(int index, Map<Integer, List<Integer>> swap) { 
	writeC(Opcodes.S_EXTENDED_PROTOBUF); 
	writeH(0x0320); 

	writeC(0x08); 
	writeBit(index); 

	ServerBasePacket sp = new ServerBasePacket() { 
	public byte[] getContent() throws IOException { 
	return getBytes(); 
	} 
	}; 
	sp.writeC(0x08); 
	sp.writeC(0x00); 
	for (int value : swap.get(0)) { 
	sp.writeC(0x10); 
	sp.writeBit(value); 
	} 
	writeC(0x12); 
	writeBit(sp.getLength() - 2); 
	writeByte(sp.getBytes()); 

	ServerBasePacket sp2 = new ServerBasePacket() { 
	public byte[] getContent() throws IOException { 
	return getBytes(); 
	} 
	}; 
	sp2.writeC(0x08); 
	sp2.writeC(0x01); 
	for (int value : swap.get(1)) { 
	sp2.writeC(0x10); 
	sp2.writeBit(value); 
	} 
	writeC(0x12); 
	writeBit(sp2.getLength() - 2); 
	writeByte(sp2.getBytes()); 

	writeC(0x18); 
	writeC(0x02); 
	writeC(0x20); 
	writeC(0x46); 
	writeC(0x00); 
	writeC(0x00); 
	} 

	@Override 
	public byte[] getContent() { 
	return _bao.toByteArray(); 
	} 

	@Override 
	public String getType() { 
	return S_INVENTORYSWAP; 
	} 
} 
