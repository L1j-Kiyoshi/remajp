package l1j.server.server.model.item.function;

import java.sql.Timestamp;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class SpellBreath extends L1ItemInstance {

	public SpellBreath(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = this.getItemId();
			L1ItemInstance target = pc.getInventory().getItem(packet.readD());
			int targetId = target.getItemId();
			if (targetId >= 427123 && targetId <= 427140) {
				int tempid = 0;
				if (targetId == 427123)
					tempid = 427114;
				else if (targetId == 427124)
					tempid = 427113;
				else if (targetId == 427125)
					tempid = 427115;
				else if (targetId == 427126)
					tempid = 427116;
				else if (targetId == 427127)
					tempid = 427117;
				else if (targetId == 427128)
					tempid = 427118;
				else if (targetId == 427129)
					tempid = 427119;
				else if (targetId == 427130)
					tempid = 427200;
				else if (targetId == 427131)
					tempid = 427201;
				else if (targetId == 427132)
					tempid = 427202;
				else if (targetId == 427133)
					tempid = 427203;
				else if (targetId == 427134)
					tempid = 427204;
				else if (targetId == 427135)
					tempid = 427205;
				else if (targetId == 427136)
					tempid = 427206;
				else if (targetId == 427137)
					tempid = 427207;
				else if (targetId == 427138)
					tempid = 427120;
				else if (targetId == 427139)
					tempid = 427121;
				else if (targetId == 427140)
					tempid = 427122;
				if (tempid == 0)
					return;
				L1ItemInstance item = ItemTable.getInstance()
						.createItem(tempid);
				if (itemId == 500018)
					item.setEndTime(new Timestamp(
							System.currentTimeMillis() + 600000));
				// item.getItem().setMaxUseTime(600);
				else if (itemId == 500019)
					item.setEndTime(new Timestamp(
							System.currentTimeMillis() + 3600000));
				// item.getItem().setMaxUseTime(3600);
				else if (itemId == 500020)
					item.setEndTime(new Timestamp(
							System.currentTimeMillis() + 10800000));
				// item.getItem().setMaxUseTime(10800);
				if (item != null) {
					if (pc.getInventory().checkAddItem(item, 1) == L1Inventory.OK) {
						pc.getInventory().storeItem(item);
						pc.sendPackets(
								new S_ServerMessage(403, item.getViewName()),
								true);
					}
				}
				pc.getInventory().removeItem(useItem, 1);
				pc.getInventory().removeItem(target, 1);
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
			}
		}
	}
}
