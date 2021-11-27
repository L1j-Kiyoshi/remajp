package l1j.server.server.serverpackets;

import java.util.ArrayList;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CharcterRevengeTable;
import l1j.server.server.model.Instance.L1PcInstance;


public class S_Revenge extends ServerBasePacket {

	private static final String S_Revenge = "[S] S_Revenge";
	private byte[] _byte = null;
	public static final int Revenge_All_List = 1052;
	public static final int Revenge_List_Add = 1053;
	public static final int Revenge_List_Provoke = 1055;
	public static final int Revenge_Chase_Loc = 1057;
	public static final int Revenge_Chase_MinMap = 1059;
	/** 복수 종합패킷 */
	public S_Revenge(int type, L1PcInstance pc) {
		CharcterRevengeTable revenge = CharcterRevengeTable.getInstance();
		ArrayList<Integer> objid = revenge.GetRevengeObj(pc.getId());
		ArrayList<Integer> Targetobjid = revenge.GettargetObj(pc.getId());
		ArrayList<String> TargetName = revenge.TargetName(pc.getId());
		ArrayList<String> TargetClanName = revenge.TargetClanName(pc.getId());
		ArrayList<Integer> TargetClass = revenge.TargetClass(pc.getId());
		ArrayList<Integer> TargetClanId = revenge.TargetClanId(pc.getId());
		ArrayList<Integer> BreakTime = revenge.BreakTime(pc.getId());
		ArrayList<Integer> remaintime = revenge.Remaintime(pc.getId());
		ArrayList<Integer> result = revenge.isWin(pc.getId());
		ArrayList<Integer> remaincounter = revenge.CPRemainCount(pc.getId());
		ArrayList<Integer> resultcounter = revenge.WLCount(pc.getId());
		ArrayList<Integer> chasertime = revenge.ChaserTimelist(pc.getId());
		int i = 0;
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case Revenge_All_List: {
			writeC(0x08);
			writeC(0x00);
			writeC(0x10);
			writeC(0x80);
			writeC(0xa3);
			writeC(0x05);
			writeC(0x18);
			writeC(0x90);
			writeC(0x4e);
			for(i = 0; ((i < objid.size()) && (i <= 19)); i++) {
				int Length = 23 + getBitSize(BreakTime.get(i)) + getBitSize(remaintime.get(i)) + getBitSize(result.get(i)) +
						getBitSize(remaincounter.get(i)) + getBitSize(resultcounter.get(i)) + getBitSize(Targetobjid.get(i)) +
						getBitSize(TargetClass.get(i)) + getBitSize(TargetClanId.get(i)) + (getBitSize(chasertime.get(i)) * 2) +
						TargetName.get(i).getBytes().length + TargetClanName.get(i).getBytes().length; 
				writeC(0x22);
				writeBit(Length);
				writeC(0x08);
				write7B(BreakTime.get(i));
				writeC(0x10);
				writeBit(remaintime.get(i));
				writeC(0x18);
				writeBit(result.get(i));
				writeC(0x20);
				writeBit(0x00);
				writeC(0x28);
				writeBit(chasertime.get(i));
				writeC(0x30);
				writeBit(chasertime.get(i));
				writeC(0x38);
				writeBit(remaincounter.get(i));
				writeC(0x40);
				writeBit(resultcounter.get(i));
				writeC(0x48);
				writeBit(0x64);
				writeC(0x50);
				writeBit(Targetobjid.get(i));
				writeC(0x58);
				writeBit(0x64);
				writeC(0x60);
				writeBit(TargetClass.get(i));
				writeC(0x6a);
				writeBit(TargetName.get(i).getBytes().length);
				writeByte(TargetName.get(i).getBytes());
				writeC(0x70);
				writeBit(TargetClanId.get(i));
				writeC(0x7a);
				writeBit(TargetClanName.get(i).getBytes().length);
				writeByte(TargetClanName.get(i).getBytes());
				writeC(0x80);
				writeBit(0x01);
				writeBit(revenge.TargetConnection(Targetobjid.get(i)));
			}
			break;
		   }
		case Revenge_List_Provoke : {
			writeC(0x08);
			writeC(0x00);
			break;
		}
		case Revenge_List_Add: {
			i = 0;
			int Length = 23 + getBitSize(BreakTime.get(i)) + getBitSize(remaintime.get(i)) + getBitSize(result.get(i)) +
					getBitSize(remaincounter.get(i)) + getBitSize(resultcounter.get(i)) + getBitSize(Targetobjid.get(i)) +
					getBitSize(TargetClass.get(i)) + getBitSize(TargetClanId.get(i)) + (getBitSize(chasertime.get(i)) * 2) +
					TargetName.get(i).getBytes().length + TargetClanName.get(i).getBytes().length; 
			writeC(0x0a);
			writeBit(Length);
			writeC(0x08);
			write7B(BreakTime.get(i));
			writeC(0x10);
			writeBit(remaintime.get(i));
			writeC(0x18);
			writeBit(result.get(i));
			writeC(0x20);
			writeBit(0x00);
			writeC(0x28);
			writeBit(chasertime.get(i));
			writeC(0x30);
			writeBit(chasertime.get(i));
			writeC(0x38);
			writeBit(remaincounter.get(i));
			writeC(0x40);
			writeBit(resultcounter.get(i));
			writeC(0x48);
			writeBit(0x64);
			writeC(0x50);
			writeBit(Targetobjid.get(i));
			writeC(0x58);
			writeBit(0x64);
			writeC(0x60);
			writeBit(TargetClass.get(i));
			writeC(0x6a);
			writeBit(TargetName.get(i).getBytes().length);
			writeByte(TargetName.get(i).getBytes());
			writeC(0x70);
			writeBit(TargetClanId.get(i));
			writeC(0x7a);
			writeBit(TargetClanName.get(i).getBytes().length);
			writeByte(TargetClanName.get(i).getBytes());
			writeC(0x80);
			writeBit(0x01);
			writeBit(revenge.TargetConnection(Targetobjid.get(i)));
			break;
		}
		case Revenge_Chase_Loc: {
			int Length = 11 + pc.getName().getBytes().length + size7B(pc.getMapId());
			writeC(0x08);
			writeC(0x00);
			writeC(0x12);
			writeC(Length);
			writeC(0x08);
			writeC(0x64);
			writeC(0x12);
			writeBit(pc.getName().getBytes().length);
			writeByte(pc.getName().getBytes());
			writeC(0x20);
			writeBit(pc.getMapId());
			writeC(0x28);
			writeBit(pc.getX(), pc.getY());
			break;
		}
		case Revenge_Chase_MinMap: {
			int Length = 11 + pc.getName().getBytes().length + size7B(pc.getMapId());
			writeC(0x08);
			writeC(0x00);
			writeC(0x12);
			writeC(Length);
			writeC(0x08);
			writeC(0x64);
			writeC(0x12);
			writeBit(pc.getName().getBytes().length);
			writeByte(pc.getName().getBytes());
			writeC(0x20);
			writeBit(pc.getMapId());
			writeC(0x28);
			writeBit(pc.getX(), pc.getY());
			break;
		    }
		}
		writeH(0x00);
	}
	

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_Revenge;
	}
}