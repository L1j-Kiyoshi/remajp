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

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_IdentifyDesc;
import l1j.server.server.serverpackets.S_ItemStatus;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class DefiniteScroll extends L1ItemInstance {

	public DefiniteScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(
					packet.readD());
			if (l1iteminstance1 == null)
				return;
			pc.sendPackets(new S_IdentifyDesc(l1iteminstance1), true);
			if (!l1iteminstance1.isIdentified()) {
				l1iteminstance1.setIdentified(true);
				pc.getInventory().updateItem(l1iteminstance1,
						L1PcInventory.COL_IS_ID);
			}
			int mainid1 = l1iteminstance1.getItem().getMainId();
			// int mainid2 = l1iteminstance1.getItem().getMainId2();
			// int mainid3 = l1iteminstance1.getItem().getMainId3();
			L1ItemInstance main = null;
			if (mainid1 == l1iteminstance1.getItem().getItemId()) {
				main = pc.getInventory().findItemId(mainid1);
				if (main != null) {
					if (main.isEquipped()) {
						pc.sendPackets(new S_ItemStatus(main, pc, true, true));
					} else {
						pc.sendPackets(new S_ItemStatus(main, pc, true, false));
					}
				}
			}

			// pc.sendPackets(new S_ItemStatus(l1iteminstance1), true);
			pc.getInventory().removeItem(useItem, 1);
			/*
			 * int add_mpr = l1iteminstance1.getItem().get_addmpr(); int add_hpr
			 * = l1iteminstance1.getItem().get_addhpr(); int safe_enchant =
			 * l1iteminstance1.getItem().get_safeenchant(); int itemid =
			 * l1iteminstance1.getItemId(); if(l1iteminstance1.getItemId() >=
			 * 427303 && l1iteminstance1.getItemId() <= 427305){ safe_enchant =
			 * 6; } if (itemid == 20028 ||itemid == 20283||itemid == 20126
			 * ||itemid == 20173 ||itemid == 20206 ||itemid == 20232 ||itemid ==
			 * 421030 ||itemid == 421031 ||itemid == 21051 ||itemid == 21052
			 * ||itemid == 21053 ||itemid == 21054 ||itemid == 21055 ||itemid ==
			 * 21056 || itemid == 7 || itemid == 35 || itemid == 48 || itemid ==
			 * 73 || itemid == 105 || itemid == 120 || itemid == 147 || itemid
			 * == 156 || itemid == 174 || itemid == 175 || itemid == 224) {
			 * safe_enchant = -1; }
			 * 
			 * StringBuffer sb = new StringBuffer();
			 * if(l1iteminstance1.getItem().getType2() == 1 ||
			 * l1iteminstance1.getItem().getType2() == 2){ if (safe_enchant ==
			 * -1){ sb.append("\\fY[ ÇÇÆ½: "+add_hpr+" ] ");
			 * sb.append("\\fU[ ¿¥Æ½: "+add_mpr+" ] ");
			 * sb.append("\\fT [ ¾ÈÀüÀÎÃ¾ : ºÒ°¡ ] ");
			 * if(l1iteminstance1.isDemonBongin()) sb.append(" [ ¸¶Á· º¸È£ ]");
			 * }else if (safe_enchant == 0){
			 * sb.append("\\fY[ ÇÇÆ½: "+add_hpr+" ] ");
			 * sb.append("\\fU[ ¿¥Æ½: "+add_mpr+" ] ");
			 * sb.append("\\fT [ ¾ÈÀüÀÎÃ¾ : 0 ] ");
			 * if(l1iteminstance1.isDemonBongin()) sb.append(" [ ¸¶Á· º¸È£ ]");
			 * }else{ sb.append("\\fY[ ÇÇÆ½: "+add_hpr+" ] ");
			 * sb.append("\\fU[ ¿¥Æ½: "+add_mpr+" ] ");
			 * sb.append("\\fT [ ¾ÈÀüÀÎÃ¾ : "+safe_enchant+" ] ");
			 * if(l1iteminstance1.isDemonBongin()) sb.append(" [ ¸¶Á· º¸È£ ]"); } }
			 * pc.sendPackets(new S_SystemMessage(sb.toString()), true);
			 * 
			 * sb = null;
			 */
		}
	}
}
