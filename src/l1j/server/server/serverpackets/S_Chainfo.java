package l1j.server.server.serverpackets;

import java.util.List;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_Chainfo extends ServerBasePacket {

	private static final String S_Chainfo = "[S] S_Chainfo";
	private byte[] _byte = null;
	private int lvl, exp, hp, mp, ac, str, con, dex, cha, Int, wis, aden = 0;
	private String PcName, Class = null;

	public S_Chainfo(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		StringBuilder sb = new StringBuilder();

		PcName = pc.getName();
		lvl = pc.getLevel();
		exp = (int) ExpTable.getExpPercentage(lvl, pc.getExp());
		hp = pc.getMaxHp();
		mp = pc.getMaxMp();
		ac = pc.getAC().getAc();
		str = pc.getAbility().getTotalStr();
		dex = pc.getAbility().getTotalDex();
		con = pc.getAbility().getTotalCon();
		Int = pc.getAbility().getTotalInt();
		cha = pc.getAbility().getTotalCha();
		wis = pc.getAbility().getTotalWis();

		if (pc.getClassId() == 0) {
			Class = "군주";
		} else if (pc.getClassId() == 1) {
			Class = "공주";
		} else if (pc.getClassId() == 61) {
			Class = "남기사";
		} else if (pc.getClassId() == 48) {
			Class = "역시ㅏ";
		} else if (pc.getClassId() == 734) {
			Class = "남법사";
		} else if (pc.getClassId() == 1186) {
			Class = "여법사";
		} else if (pc.getClassId() == 138) {
			Class = "남요정";
		} else if (pc.getClassId() == 37) {
			Class = "여요정";
		} else if (pc.getClassId() == 2786) {
			Class = "남다엘";
		} else if (pc.getClassId() == 2796) {
			Class = "여다엘";
		} else if (pc.getClassId() == 6658) {
			Class = "남용기사";
		} else if (pc.getClassId() == 6661) {
			Class = "여용기사";
		} else if (pc.getClassId() == 6671) {
			Class = "남환술사";
		} else if (pc.getClassId() == 6671) {
			Class = "여환술사";
		} else if (pc.getClassId() == 12490) {
			Class = "남전사";
		} else if (pc.getClassId() == 12494) {
			Class = "여전사";
		} else if (pc.getClassId() == 18520) {
			Class = "남검사";
		} else if (pc.getClassId() == 18499) {
			Class = "여검사";
		}

		if (pc.getInventory().checkItem(40308)) {
			aden = pc.getInventory().countItems(40308);
		}
		sb.append(PcName);
		sb.append(" ");
		sb.append(Class);
		sb.append(" 정보\n 레벨:");
		sb.append(lvl);
		sb.append(" ");
		sb.append(exp);
		sb.append("%\n HP:");
		sb.append(hp);
		sb.append(" MP:");
		sb.append(mp);
		sb.append(" AC:");
		sb.append(ac);
		sb.append("\n STR:");
		sb.append(str);
		sb.append(" DEX:");
		sb.append(dex);
		sb.append(" CON:");
		sb.append(con);
		sb.append("\n WIS:");
		sb.append(wis);
		sb.append(" CHA:");
		sb.append(cha);
		sb.append(" INT:");
		sb.append(Int);
		sb.append("\n");
		sb.append(" 아덴 ");
		sb.append(aden);
		sb.append(" 원");
		sb.append("\n");
		sb.append("<착용 아이템 목록>\n");
		List<L1ItemInstance> items = pc.getInventory().getItems();
		for (L1ItemInstance item : items) {
			if (item.isEquipped()) {
				sb.append(ItemLogName(item, item.getCount()));
				sb.append("\n");
			}
		}
		writeC(Opcodes.S_NEWS);
		writeS(sb.toString());
	}

	private String ItemLogName(L1ItemInstance item, int count) {
		int attr = item.getAttrEnchantLevel();
		int elv = item.getEnchantLevel();
		boolean iden = item.isIdentified();

		StringBuilder SB = new StringBuilder();
		// 0:축복 1:통상 2:저주 3:미감정
		if (iden) {
			if (item.getBless() == 0) {
				SB.append(" 축 ");
			} else if (item.getBless() == 1) {
				SB.append(" 보 ");
			} else if (item.getBless() == 2) {
				SB.append(" 저 ");
			}
		} else {
			SB.append(" 미 ");
		}
		if (attr > 0) {
			switch (attr) {
			case 1:
				SB.append("화령:1단 ");
				break;
			case 2:
				SB.append("화령:2단 ");
				break;
			case 3:
				SB.append("화령:3단 ");
				break;
			case 4:
				SB.append("수령:1단 ");
				break;
			case 5:
				SB.append("수령:2단 ");
				break;
			case 6:
				SB.append("수령:3단 ");
				break;
			case 7:
				SB.append("풍령:1단 ");
				break;
			case 8:
				SB.append("풍령:2단 ");
				break;
			case 9:
				SB.append("풍령:3단 ");
				break;
			case 10:
				SB.append("지령:1단 ");
				break;
			case 11:
				SB.append("지령:2단 ");
				break;
			case 12:
				SB.append("지령:3단 ");
				break;
			case 33:
				SB.append("화령:4단 ");
				break;
			case 34:
				SB.append("화령:5단 ");
				break;
			case 35:
				SB.append("수령:4단 ");
				break;
			case 36:
				SB.append("수령:5단 ");
				break;
			case 37:
				SB.append("풍령:4단 ");
				break;
			case 38:
				SB.append("풍령:5단 ");
				break;
			case 39:
				SB.append("지령:4단 ");
				break;
			case 40:
				SB.append("지령:5단 ");
				break;
			}
		}

		if (elv > 0) {
			SB.append("+");
			SB.append(elv);
			SB.append(" ");
		}
		SB.append(item.getName());
		SB.append(" ");
		if (count > 1) {
			SB.append("(");
			SB.append(count);
			SB.append(")");
		}
		return SB.toString();
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_Chainfo;
	}
}
