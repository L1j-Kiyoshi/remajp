package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_TempInv extends ServerBasePacket {

	private static final String S_INV_LIST = "[S] S_TempInv";

	public S_TempInv(List<L1ItemInstance> item) {
		List<L1ItemInstance> items = item;
		writeC(Opcodes.S_ADD_INVENTORY_BATCH);
		writeC(items.size());
		byte[] status = null;
		for (L1ItemInstance item1 : items) {
			writeD(item1.getId());
			writeC(item1.getItem().getUseType());
			writeC(0);
			writeH(item1.get_gfxid());
			writeC(item1.getBless());
			writeD(item1.getCount());
			writeC((item1.isIdentified()) ? 1 : 0);
			writeS(item1.getViewName());
			if (!item1.isIdentified()) {
				// 미감정의 경우 스테이터스를 보낼 필요는 없다
				writeC(0);
			} else {
				status = item1.getStatusBytes();
				writeC(status.length);
				for (byte b : status) {
					writeC(b);
				}
			}
		}
	}

	@Override
	public byte[] getContent() {
		return _bao.toByteArray();
	}

	@Override
	public String getType() {
		return S_INV_LIST;
	}
}
