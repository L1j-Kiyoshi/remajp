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

package l1j.server.server.model.item.function;

import java.util.Random;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class SealScroll extends L1ItemInstance {

	public SealScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = this.getItemId();
			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(
					packet.readD());
			if (itemId == 50020) { // ?????ܼ?

				if (l1iteminstance1.getItem().getType2() == 0) {
					pc.sendPackets(new S_SystemMessage("???????? ???? ?Ҽ? ?ֽ??ϴ?."));
					return;
				}
				if (l1iteminstance1.getBless() >= 0
						&& l1iteminstance1.getBless() <= 3) {
					int Bless = 0;
					switch (l1iteminstance1.getBless()) {
					case 0:
						Bless = 128;
						break; // ??
					case 1:
						Bless = 129;
						break; // ????
					case 2:
						Bless = 130;
						break; // ????
					case 3:
						Bless = 131;
						break; // ??Ȯ??
					}
					l1iteminstance1.setBless(Bless);
					pc.getInventory().updateItem(l1iteminstance1,
							L1PcInventory.COL_BLESS);
					pc.getInventory().saveItem(l1iteminstance1,
							L1PcInventory.COL_BLESS);
					pc.getInventory().removeItem(useItem, 1);
				} else
					pc.sendPackets(new S_SystemMessage("?ƹ??ϵ? ?Ͼ?? ?ʾҽ??ϴ?."),
							true); // \f1 ?ƹ??͵? ?Ͼ?? ?ʾҽ??ϴ?.
			} else if (itemId == 50021) { // ?????????ܼ?
				if (l1iteminstance1.getBless() >= 128
						&& l1iteminstance1.getBless() <= 131) {
					int Bless = 0;
					switch (l1iteminstance1.getBless()) {
					case 128:
						Bless = 0;
						break;
					case 129:
						Bless = 1;
						break;
					case 130:
						Bless = 2;
						break;
					case 131:
						Bless = 3;
						break;
					}
					l1iteminstance1.setBless(Bless);
					pc.getInventory().updateItem(l1iteminstance1,
							L1PcInventory.COL_BLESS);
					pc.getInventory().saveItem(l1iteminstance1,
							L1PcInventory.COL_BLESS);
					pc.getInventory().removeItem(useItem, 1);
				} else
					pc.sendPackets(new S_SystemMessage("?ƹ??ϵ? ?Ͼ?? ?ʾҽ??ϴ?."),
							true); // \f1 ?ƹ??͵? ?Ͼ?? ?ʾҽ??ϴ?.
			} else if (itemId == 50022 || itemId == 60204) { // ?ູ?ֹ???
				/**
				 * 2011.03.09 ?????? ?ູ?ֹ??? (sjsjsj0813@nate.com)
				 */
				if (l1iteminstance1 == null
						|| l1iteminstance1.getItem().getType1() == 0) { // ??????
																		// ?????
					pc.sendPackets(new S_SystemMessage("?ƹ??ϵ? ?Ͼ?? ?ʾҽ??ϴ?."),
							true); // ?ƹ??͵? ?Ͼ?? ?ʾҽ??ϴ?.
					return;
				}
				if (l1iteminstance1.getBless() >= 128 // ?????? ?ϰ???
						|| l1iteminstance1.getBless() == 0) { // ?̹????ϰ???
					pc.sendPackets(new S_SystemMessage("?ƹ??ϵ? ?Ͼ?? ?ʾҽ??ϴ?."),
							true); // ?ƹ??͵? ?Ͼ?? ?ʾҽ??ϴ?.
					return;
				}
				Random random = new Random();
				int ran = random.nextInt(99) + 1;
				if (ran < 7 || itemId == 60204) {// 5???? Ȯ???? ?ູ??þ?? ?????Ѵ?.
					l1iteminstance1.setBless(0);
					pc.sendPackets(new S_SkillSound(pc.getId(), 9268));
					pc.getInventory().updateItem(l1iteminstance1,
							L1PcInventory.COL_BLESS);
					pc.getInventory().saveItem(l1iteminstance1,
							L1PcInventory.COL_BLESS);
					pc.getInventory().removeItem(useItem, 1);
					pc.sendPackets(
							new S_SystemMessage("??æƮ : "
									+ l1iteminstance1.getName()
									+ " ?? ?? ???? ???????? ?????ϴ?."), true);
				} else {
					pc.sendPackets(new S_SystemMessage("??æƮ : "
							+ l1iteminstance1.getName()
							+ " ?? ?? ???? ???????? ???????? ?ƹ??ϵ? ?Ͼ?? ?ʾҽ??ϴ?."), true);
					pc.getInventory().removeItem(useItem, 1);

				}
			} else 
				
				if (itemId == 560010) { // ??Ȯ???ֹ???
				/**
				 * 2011.03.09 ?????? ??Ȯ???ֹ??? (sjsjsj0813@nate.com)
				 */
				if (l1iteminstance1.getBless() >= 128) { // ???ξ??????ϰ??? ??Ȯ?κҰ????ϰ?
					pc.sendPackets(new S_SystemMessage(
							"???ε? ?????? ??Ȯ???ֹ????? ?????? ?? ?????ϴ?."), true);
					return;
				}
				l1iteminstance1.setIdentified(false);
				pc.getInventory().updateItem(l1iteminstance1,
						L1PcInventory.COL_IS_ID);
				pc.getInventory().removeItem(useItem, 1);
				pc.sendPackets(new S_SystemMessage(
						"\\fY?????? ?????????? ??Ȯ?? ?????????? ?????˴ϴ?."), true);
			}
		}
	}
}
