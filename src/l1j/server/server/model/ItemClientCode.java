package l1j.server.server.model;

import l1j.server.server.datatables.ItemTable;

public class ItemClientCode {

	public static int code(int itemid) {
		int id = 0x0539;
		switch (itemid) {

		case 40747: // 블랙미스릴화살
			id = 1484;
			break;
		default:
			id = ItemTable.getInstance().getTemplate(itemid).getItemDescId();
			id = id > 0 ? id : 0x0539;
			break;
		}
		return id;
	}

}
