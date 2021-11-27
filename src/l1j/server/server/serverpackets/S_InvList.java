package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.BinaryOutputStream;

public class S_InvList extends ServerBasePacket {

	private byte[] _byte = null;

	public S_InvList(L1PcInstance pc, L1ItemInstance item) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(588);
		byte[] status = sendItemPacket(pc, item, item.getCount());
		int total_size = status.length;
		writeC(0x0a);
		writeBit(total_size);
		writeByte(status);
		writeC(0x10);
		writeBit(0);
		writeC(0x18);
		writeBit(pc.getId());
		writeH(0);
	}
	
	public S_InvList(L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(588);

		List<L1ItemInstance> list = pc.getInventory().getItems();
		L1ItemInstance item = null;
		int items_size = list.size();
	
		for (int i = 0; i < items_size; i++) {
			item = list.get(i);
			if (item == null)
				continue;
			byte[] status = sendItemPacket(pc, item, item.getCount());
			int total_size = status.length;
			writeC(0x0a);
			writeBit(total_size);
			writeByte(status);
		}
		writeC(0x10);
		writeBit(1);
		writeC(0x18);
		writeBit(pc.getId());
		writeH(0);
	}

	public byte[] sendItemPacket(L1PcInstance pc, L1ItemInstance item, int count) {
		BinaryOutputStream os = new BinaryOutputStream();
		try {
			os.writeC(0x08);
			os.writeBit(item.getId());
			os.writeC(0x10);
			os.writeBit(item.getItem().getItemDescId() == 0 ? -1 : item.getItem().getItemDescId());
			os.writeC(0x18);
			os.writeBit(item.getItem().getItemId());
			os.writeC(0x20);
			os.writeBit(count);

			os.writeC(0x28);
			int use_type = item.getItem().getUseType();
			os.writeBit(use_type);

			if (item.getChargeCount() > 0) {
				os.writeC(0x30);
				os.writeBit(item.getChargeCount());
			}
			os.writeC(0x38);
			os.writeBit(item.get_gfxid());
			os.writeC(0x40);
			os.writeBit(item.getBless());

			os.writeC(0x58);
			os.writeBit(0);

			if (item.getItem().getType2() != 0) {
				os.writeC(0x68);
				os.writeBit(item.getEnchantLevel());
			}

			/**
			 * 0:창고불가 2:특수가능 3:개인/특수가능 7:개인/혈/특수가능
			 **/
			os.writeC(0x70);
			os.writeBit(item.getWarehouseType());

			if (item.getAttrEnchantLevel() > 0) {
				int[] 속성값 = item.getAttrEnchant(item.getAttrEnchantLevel());
				os.writeBit(128);
				os.writeBit(속성값[0]); // 속성값
				os.writeBit(136);
				os.writeBit(속성값[1]); // 속성인챈레벨
			}

			int size = item.getNumberedViewName(count).getBytes().length;

			os.writeBit(146);
			os.writeBit(size);
			os.writeByte(item.getNumberedViewName(count).getBytes());

			if (item.isIdentified()) {
				os.writeBit(154);
				byte[] status = item.getStatusBytes();
				os.writeBit(status.length);
				os.writeByte(status);
			}

			os.writeBit(176); // bless_code??
			os.writeBit(item.getBless());

			os.writeBit(184); // real_enchant??
			os.writeBit(item.getEnchantLevel());

			os.writeBit(200); // 무게
			os.writeBit(item.getWeight());

			os.writeBit(208); // 확인상태
			os.writeBit(item.isIdentified() ? 1 : 0);

			return os.getBytes();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}
}
