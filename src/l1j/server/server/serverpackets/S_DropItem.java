package l1j.server.server.serverpackets;

import java.text.SimpleDateFormat;
import java.util.Locale;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_DropItem extends ServerBasePacket {
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"MM-dd HH:mm", Locale.KOREA);
	private static final String _S__OB_DropItem = "[S] S_DropItem";

	private byte[] _byte = null;

	public S_DropItem(L1ItemInstance item) {
		buildPacket(item);
	}

	private void buildPacket(L1ItemInstance item) {
		writeC(Opcodes.S_PUT_OBJECT);
		writeH(item.getX());
		writeH(item.getY());
		writeD(item.getId());
		writeH(item.getItem().getGroundGfxId());
		writeC(0);
		writeC(0);
		if (item.isNowLighting()) {
			writeC(item.getItem().getLightRange());
		} else {
			writeC(0);
		}
		writeC(0);
		writeD(item.getCount());
		writeH(0);

		/**
		 * 아이템드랍시 인첸/속성 표시 by사부
		 */
		/***********************************************************************
		 ***********************************************************************/
		StringBuffer sb = null;
		sb = new StringBuffer();
		if (item.isIdentified()) {
			if (item.getItem().getType2() == 1
					|| item.getItem().getType2() == 2) {
				switch (item.getAttrEnchantLevel()) {// by사부
				case 1:
					sb.append("$6115");
					break;
				case 2:
					sb.append("$6116");
					break;
				case 3:
					sb.append("$6117");
					break;
				case 4:
					sb.append("$6118");
					break;
				case 5:
					sb.append("$6119");
					break;
				case 6:
					sb.append("$6120");
					break;
				case 7:
					sb.append("$6121");
					break;
				case 8:
					sb.append("$6122");
					break;
				case 9:
					sb.append("$6123");
					break;
				case 10:
					sb.append("$6124");
					break;
				case 11:
					sb.append("$6125");
					break;
				case 12:
					sb.append("$6126");
					break;
				case 33:
					sb.append("화령:4단 ");
					break;
				case 34:
					sb.append("화령:5단 ");
					break;
				case 35:
					sb.append("수령:4단 ");
					break;
				case 36:
					sb.append("수령:5단 ");
					break;
				case 37:
					sb.append("풍령:4단 ");
					break;
				case 38:
					sb.append("풍령:5단 ");
					break;
				case 39:
					sb.append("지령:4단 ");
					break;
				case 40:
					sb.append("지령:5단 ");
					break;
				default:
					sb.append(" ");
					break;
				// 주석안할경우 ALT눌렀을때나 마우스올려놨을때나 인첸표시
				// 주석할경우 ALT눌렀을때만 인첸표시 (본섭)
				}
				// 인첸 +0 일때도 표기되게 하실분은 밑에 if (item.getEnchantLevel() >= 0) {로 교체
				// by사부
				if (item.getEnchantLevel() > 0) {
					sb.append("+" + item.getEnchantLevel() + " ");
				} else if (item.getEnchantLevel() < 0) {
					sb.append(String.valueOf(item.getEnchantLevel()) + " ");
				}
			}
		}
		sb.append(item.getItem().getNameId());
		int itemId = item.getItem().getItemId();
		if (item.getCount() > 1) {
			sb.append(" (" + item.getCount() + ")");
		} else {
			if (item.getItem().getLightRange() != 0 && item.isNowLighting()) {
				sb.append(" ($10)");
			}
		}

		if (itemId == 40312 || itemId == 49312) {
			if (item.getEndTime() != null) {
				sb.append(" [" + sdf.format(item.getEndTime().getTime())
						+ "] 식별번호 : " + item.getKey());
			}
		}

		if (itemId == 60285) {// 훈련소 열쇠
			if (item.getEndTime() != null) {
				int mapid = item.getKey() - 1399;
				sb.append(" (" + (mapid > 9 ? mapid : "0" + mapid) + ") ["
						+ sdf.format(item.getEndTime().getTime()) + "]");
			}
		}
		writeS(sb.toString());
		writeC(0);
		writeD(0);
		writeD(0);
		writeC(0xFF);
		writeC(0);
		writeC(0);
		writeH(0xFFFF);
		writeC(0);
		writeC(0x08);
		writeC(0x12);
		writeC(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return _S__OB_DropItem;
	}

}
