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
package l1j.server.server.templates;

import java.sql.Timestamp;

import l1j.server.server.datatables.ItemTable;

public class L1ShopItem {
	// private static final long serialVersionUID = 1L;

	private int _npcid;

	private final int _itemId;

	private final L1Item _item;

	private int _price;

	private int _buy_price;

	private final int _packCount;

	private final int _enchant;

	private int _attr;

	private int _Bless;

	private int _id;

	private int _count;

	private String _msg;

	private Timestamp deletetime;

	public L1ShopItem(int itemId, int price, int packCount, int enchant) {
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_enchant = enchant;
		_count = 1;
	}
	
	
	public L1ShopItem(int itemId, int price, int packCount, int enchant,
			int bless, int id) {
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_enchant = enchant;
		_count = 1;
		_Bless = bless;
		_id = id;
	}

	public L1ShopItem(int itemId, int price, int buy_price, int packCount,
			int enchant, int attr, String msg, int bless, int id) {
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_enchant = enchant;
		_count = 1;
		_buy_price = buy_price;
		_attr = attr;
		_msg = msg;
		_Bless = bless;
		_id = id;
	}

	public L1ShopItem(int npcid, int itemId, int price, int buy_price,
			int packCount, int enchant, int attr, String msg, int bless, int id) {
		_npcid = npcid;
		_itemId = itemId;
		_item = ItemTable.getInstance().getTemplate(itemId);
		_price = price;
		_packCount = packCount;
		_enchant = enchant;
		_count = 1;
		_buy_price = buy_price;
		_attr = attr;
		_msg = msg;
		_Bless = bless;
		_id = id;
	}

	public int getNpcId() {
		return _npcid;
	}

	public int getItemId() {
		return _itemId;
	}

	public L1Item getItem() {
		return _item;
	}

	public void setPrice(int i) {
		_price = i;
	}

	public int getPrice() {
		return _price;
	}

	public int getPackCount() {
		return _packCount;
	}

	public int getEnchant() {
		return _enchant;
	}

	public int getCount() {
		return _count;
	}

	public void setCount(int i) {
		_count = i;
	}

	public int getBless() {
		return _Bless;
	}

	public void setBless(int i) {
		_Bless = i;
	}

	public int getId() {
		return _id;
	}

	public void setId(int i) {
		_id = i;
	}

	public int getAttr() {
		return _attr;
	}

	public void setAttr(int i) {
		_attr = i;
	}

	public int getBuyPrice() {
		return _buy_price;
	}

	public void setBuyPrice(int i) {
		_buy_price = i;
	}

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String msg) {
		_msg = msg;
	}

	public Timestamp getDeleteTime() {
		return deletetime;
	}

	public void setDeleteTime(Timestamp t) {
		deletetime = t;
	}

}
