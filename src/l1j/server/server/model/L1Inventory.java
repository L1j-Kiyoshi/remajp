/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.CrockSystem;
import l1j.server.GameSystem.INN.INN;
import l1j.server.GameSystem.INN.InnTimer;
import l1j.server.Warehouse.Warehouse;
import l1j.server.server.IdFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.BoardTable;
import l1j.server.server.datatables.FurnitureSpawnTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Ability;
import l1j.server.server.templates.L1Item;

public class L1Inventory extends L1Object {
	private static final long serialVersionUID = 1L;

	private static Logger _log = Logger.getLogger(L1Inventory.class.getName());
	protected List<L1ItemInstance> _items = new CopyOnWriteArrayList<L1ItemInstance>();
	public static final int MAX_AMOUNT = 2000000000; // 2G
	public static final int MAX_WEIGHT = 1500;
	public static final int OK = 0;
	public static final int SIZE_OVER = 1;
	public static final int WEIGHT_OVER = 2;
	public static final int AMOUNT_OVER = 3;
	public static final int WAREHOUSE_TYPE_PERSONAL = 0;
	public static final int WAREHOUSE_TYPE_CLAN = 1;

	public L1Inventory() {
		//
	}

	public int getSize() {
		return _items.size();
	}

	public List<L1ItemInstance> getItems() {
		return _items;
	}

	public int getWeight() {
		int weight = 0;

		for (L1ItemInstance item : _items) {
			weight += item.getWeight();
		}

		return weight;
	}

	public int checkAddItem(L1ItemInstance item, int count) {
		if (item == null) {
			return -1;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return -1;
		}
		if (getSize() > Config.MAX_NPC_ITEM
				|| (getSize() == Config.MAX_NPC_ITEM && (!item.isStackable() || !checkItem(item
						.getItem().getItemId())))
				|| (getSize() == Config.MAX_NPC_ITEM && item.getItem()
						.getItemId() == 40309)) { // 용량 확인
			return SIZE_OVER;
		}
		if (getSize() > Config.MAX_NPC_ITEM
				|| (getSize() == Config.MAX_NPC_ITEM && (!item.isStackable() || !checkItem(item
						.getItem().getItemId())))) {
			return SIZE_OVER;
		}

		int weight = getWeight() + item.getItem().getWeight() * count / 1000
				+ 1;
		if (weight < 0 || (item.getItem().getWeight() * count / 1000) < 0) {
			return WEIGHT_OVER;
		}
		if (weight > (MAX_WEIGHT * Config.RATE_WEIGHT_LIMIT_PET)) {
			return WEIGHT_OVER;
		}

		L1ItemInstance itemExist = findItemId(item.getItemId());
		if (itemExist != null && (itemExist.getCount() + count) > MAX_AMOUNT) {
			return AMOUNT_OVER;
		}

		return OK;
	}

	public int checkAddItemToWarehouse(L1ItemInstance item, int count, int type) {
		if (item == null) {
			return -1;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return -1;
		}
		int maxSize = 100;
		if (type == WAREHOUSE_TYPE_PERSONAL) {
			maxSize = Config.MAX_PERSONAL_WAREHOUSE_ITEM;
		} else if (type == WAREHOUSE_TYPE_CLAN) {
			maxSize = Config.MAX_CLAN_WAREHOUSE_ITEM;
		}
		if (getSize() > maxSize
				|| (getSize() == maxSize && (!item.isStackable() || !checkItem(item
						.getItem().getItemId())))) {
			return SIZE_OVER;
		}

		return OK;
	}

	public synchronized L1ItemInstance storeItem(int id, int count, int enchant) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}
		if (temp.isStackable()) {
			L1ItemInstance item = ItemTable.getInstance().FunctionItem(temp);
			item.setCount(count);

			if (findItemId(id) == null) {
				item.setId(ObjectIdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = ItemTable.getInstance().FunctionItem(temp);
			item.setId(ObjectIdFactory.getInstance().nextId());
			item.setEnchantLevel(enchant);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}

	public synchronized L1ItemInstance storeItem(int id, int count,
			int enchant, int bless) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}
		if (temp.isStackable()) {
			L1ItemInstance item = ItemTable.getInstance().FunctionItem(temp);
			item.setCount(count);

			if (findItemId(id) == null) {
				item.setId(ObjectIdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = ItemTable.getInstance().FunctionItem(temp);
			item.setId(ObjectIdFactory.getInstance().nextId());
			item.setEnchantLevel(enchant);
			item.setBless(bless);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}
	
	public synchronized L1ItemInstance storeItem2(int id, int count, int enchant, int bless, int attr) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}
		if (temp.isStackable()) {
			L1ItemInstance item = ItemTable.getInstance().FunctionItem(temp);
			item.setCount(count);

			if (findItemId(id) == null) {
				item.setId(ObjectIdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = ItemTable.getInstance().FunctionItem(temp);
			item.setId(ObjectIdFactory.getInstance().nextId());
			item.setEnchantLevel(enchant);
			item.setBless(bless);
			item.setAttrEnchantLevel(attr);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}
	
	
	public synchronized L1ItemInstance storeItem3(int id, int count, int enchant, int bless, int attr) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}
		if (temp.isStackable()) {
			L1ItemInstance item = ItemTable.getInstance().FunctionItem(temp);
			item.setCount(count);

			if (findItemId(id) == null) {
				item.setId(ObjectIdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = ItemTable.getInstance().FunctionItem(temp);
			item.setId(ObjectIdFactory.getInstance().nextId());
			item.setEnchantLevel(enchant);
			item.setBless(bless);
			item.setAttrEnchantLevel(attr);
			item.setIdentified(true);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}
	public synchronized L1ItemInstance storeItem(int id, int count) {//여기다여기
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}
		if (temp.isStackable()) {
			L1ItemInstance item = ItemTable.getInstance().FunctionItem(temp);
			item.setCount(count);
			if (id == 600248) {
				item.setIdentified(true);
			}
			if (findItemId(id) == null) {
				item.setId(ObjectIdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = ItemTable.getInstance().FunctionItem(temp);
			if (id == 600251 || id == 600252 || id == 600253 || id == 600254
					|| id == 600255 || id == 6015 || id == 7236) {
				item.setIdentified(true);
			}
			item.setId(ObjectIdFactory.getInstance().nextId());
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}

	public synchronized L1ItemInstance storeItem(int id, int count,
			boolean identified) {
		if (count <= 0) {
			return null;
		}
		L1Item temp = ItemTable.getInstance().getTemplate(id);
		if (temp == null) {
			return null;
		}

		if (temp.isStackable()) {
			L1ItemInstance item = ItemTable.getInstance().FunctionItem(temp);
			item.setCount(count);

			if (findItemId(id) == null) {
				item.setId(ObjectIdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = ItemTable.getInstance().FunctionItem(temp);
			if (identified) {
				item.setIdentified(true);
			}

			/*
			 * if(item.getItemId() == 600229){ item.setChargeCount(100); }
			 */

			item.setId(ObjectIdFactory.getInstance().nextId());
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85,
			0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90,
			0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b,
			0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6,
			0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1,
			0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc,
			0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0xc6, 0xc7,
			0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2,
			0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd,
			0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8,
			0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3,
			0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe,
			0xff };

	private String byteWrite(long value) {
		long temp = value / 128;
		StringBuffer sb = new StringBuffer();
		if (temp > 0) {
			sb.append((byte) hextable[(int) value % 128]);
			while (temp >= 128) {
				sb.append((byte) hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				sb.append((int) temp);
		} else {
			if (value == 0) {
				sb.append(0);
			} else {
				sb.append((byte) hextable[(int) value]);
				sb.append(0);
			}
		}
		return sb.toString();
	}

	public synchronized L1ItemInstance storeItem(L1ItemInstance item, boolean ok) {
		if (item.getCount() <= 0) {
			return null;
		}

		item.setEncobjid(byteWrite(item.getId()));

		int itemId = item.getItem().getItemId();
		if (item.isStackable()) {
			L1ItemInstance findItem = null;
			if (item.getItem().getItemId() == 40309) {
				findItem = findItemTicketId(item.getItem().getItemId(),
						item.getSecondId(), item.getTicketId());
			} else if (item.getItem().getItemId() == 40312
					|| item.getItem().getItemId() == 49312
					|| item.getItemId() == 60285) {
				findItem = findItemKey(item.getItem().getItemId(),
						item.getEndTime(), item.getKey());
			} else {
				findItem = findItemId(itemId);
			}

			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}
		
		/** 지배 변신 관련 정보 관리 위하여 */
		DominationSkill(item, this, true);
		
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());

		int chargeCount = item.getItem().getMaxChargeCount();
		switch (itemId) {

		case 5000121:
			Random random = new Random(System.nanoTime());
			chargeCount -= random.nextInt(5);
			break;
		case 40903:
		case 40904:
		case 40905:
			chargeCount = itemId - 40902;
			break;
		case 40906:
			chargeCount = 5;
			break;
		case 40907:
		case 40908:
			chargeCount = 20;
			break;
		}
		item.setChargeCount(chargeCount);
		if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light
			item.setRemainingTime(item.getItem().getLightFuel());
		} else if (itemId >= 60173 && itemId <= 60176) {
			item.setRemainingTime(3600 * 5);
		} else {
			item.setRemainingTime(item.getItem().getMaxUseTime());
		}
		item.setBless(item.getItem().getBless());

		if (itemId == L1ItemId.DRAGON_KEY) {// 드래곤 키
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 259200000);// 3일
			item.setEndTime(deleteTime);
		}
		if (itemId == 40312 || itemId == 49312) {// 여관열쇠
			//System.out.println("ㅇㅇ 10초 1");
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+(3600000 * 4));// 4시간
			item.setEndTime(deleteTime);
		}
		if (itemId == 20344 || itemId == 61000 || (itemId >= 21113 && itemId <= 21120)
				|| (itemId >= 60350 && itemId <= 60352)) { // 토끼투구, 정령악세(깃털)
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 3));// 3시간
			item.setEndTime(deleteTime);
			/*
			 * }else if(itemId >= 60173 && itemId <= 60176){//블레그 종류 마법인형
			 * Timestamp deleteTime = null; deleteTime = new
			 * Timestamp(System.currentTimeMillis() + (3600000*5));// 5시간
			 * item.setEndTime(deleteTime);
			 */} else if (itemId == 600234
				|| itemId == 60009
				|| itemId == 60010
				|| itemId == 21092
				|| itemId == 60061
				|| // 메린, 킬톤 계약서, 요리사모자, 버프코인
				(itemId >= 425000 && itemId <= 425002)
				|| // 엘모어 방어구
				(itemId >= 450000 && itemId <= 450007) // 엘모어 무기
				|| itemId == 430003 || itemId == 430505 || itemId == 430506
				|| itemId == 41915 || itemId == 5000034 
				|| (itemId >= 21125 && itemId <= 21136)
				|| (itemId >= 21139 && itemId <= 21156)) { // 시댄서, 라미아, 스파토이,
															// 허수아비, 에틴
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 7일
			item.setEndTime(deleteTime);
		} else if (itemId == 60011 || itemId == 60014) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 9));// 9시간
			item.setEndTime(deleteTime);
		} else if (itemId == 60012 || itemId == 60015) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 17));// 17시간
			item.setEndTime(deleteTime);
		} else if (itemId == 60013 || itemId == 60016) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24));// 24시간
			item.setEndTime(deleteTime);
		} else if (itemId == 21094) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000);// 1시간
			item.setEndTime(deleteTime);
		} else if (itemId == 21157 || itemId == 121216) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000);// 30분
			item.setEndTime(deleteTime);
			
		} else if (itemId == 21272 || itemId == 421216) { //아르카의유물	
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000);// 30분
			item.setEndTime(deleteTime);
			
		} else if (itemId == 21271 || itemId == 421217 ||itemId == 421218 || itemId == 421219) { //아르카의강화된유물
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 7200000);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 421220 || itemId == 421221) { //조이의유물
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 600369) { //얼어붙은심장
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 86400000);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 30014) { //반역자의투구
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24 * 60);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 600263 || itemId == 600264 || itemId == 600265 || itemId == 600266) {// 안타 발라 린드 마법인형
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24 * 3);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId >= 9075 && itemId <= 9093) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 2일
			item.setEndTime(deleteTime);
			
		} else if (itemId >= 9097 && itemId <= 9098) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 2일
			item.setEndTime(deleteTime);

		} else if (itemId == 221216) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000 * 4);// 30분
			item.setEndTime(deleteTime);

		} else if ((itemId >= 267 && itemId <= 274)
				|| (itemId >= 21158 && itemId <= 21165)) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 3));// 3일
			item.setEndTime(deleteTime);
			// 상아템들
		} else if (/*
					 * itemId == 7 || itemId == 35 || itemId == 48 || itemId ==
					 * 73 || itemId == 105 || itemId == 120 || itemId == 147 ||
					 * itemId == 156 || itemId == 174 || itemId == 175 || itemId
					 * == 224 || itemId == 20028 || itemId == 20082 || itemId ==
					 * 20126 || itemId == 20173 || itemId == 20206 || itemId ==
					 * 20232 || itemId == 20282 || itemId == 201261 || itemId ==
					 * 21098 || (itemId >= 21102 && itemId <= 21112) || itemId
					 * == 21254
					 */
		(itemId >= 21099 && itemId <= 21112) || itemId == 21254
				|| itemId == 20082 || itemId == 7 || itemId == 35
				|| itemId == 48 || itemId == 73 || itemId == 105
				|| itemId == 120 || itemId == 147 || itemId == 7232
				|| itemId == 156 || itemId == 174 || itemId == 175
				|| itemId == 224) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 3일
			item.setEndTime(deleteTime);

		} else if (itemId == 60319) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 22));// 22시간
			item.setEndTime(deleteTime);
		} else if (itemId == 9056 || itemId == 141915 || itemId == 141916) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (86400000 * 7));// 22시간
			item.setEndTime(deleteTime);
		}  else if (itemId == 1419161) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 3));// 3시간
			item.setEndTime(deleteTime);
		}
		if (itemId == 60080 || itemId == 60082) {
			if (item.getCreaterName() == null)
				item.setCreaterName("*케플리샤*");
		}

		/** 생일 시스템 by이러버엉 **/
		// 야메 시스템 ㅋ 이런방식인것만...
		if (itemId == L1ItemId.HAPPY_BIRTHDAY_ELF) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 86400000);
			item.setEndTime(deleteTime);
		}
		if (itemId == L1ItemId.HAPPY_BIRTHDAY_METIS) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000);
			item.setEndTime(deleteTime);
		}
		/** 생일 시스템 by이러버엉 **/
		// 야메 시스템 ㅋ 이런방식인것만...
		if (item.isIdentified() != true) {
			item.setIdentified(false);
		}
		_items.add(item);
		insertItem(item, ok);
		return item;
	}

	public synchronized L1ItemInstance storeItem(L1ItemInstance item) {
		if (item.getCount() <= 0) {
			return null;
		}

		item.setEncobjid(byteWrite(item.getId()));

		int itemId = item.getItem().getItemId();
		if (item.isStackable()) {
			L1ItemInstance findItem = null;
			
			if (item.getItem().getItemId() == 40309 || item.getItem().getItemId() == 40316){
				findItem = findItemTicketId(item.getItem().getItemId(), item.getSecondId(), item.getTicketId());
			} else if (item.getItem().getItemId() == 40312 || item.getItem().getItemId() == 49312 || item.getItemId() == 60285) {
				findItem = findItemKey(item.getItem().getItemId(), item.getEndTime(), item.getKey());
			} else {
				findItem = findItemId(itemId);
			}

			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}

		/** 지배 변신 관련 정보 관리 위하여 */
		DominationSkill(item, this, true);
		
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());

		int chargeCount = item.getItem().getMaxChargeCount();
		switch (itemId) {
		// case 40006:
		// case 40007: //흑단막대
		// case 40008:
		// case 41401:
		// case 140006:
		// case 140008:
		case 5000121:
			Random random = new Random(System.nanoTime());
			chargeCount -= random.nextInt(5);
			break;
		case 40903:
		case 40904:
		case 40905:
			chargeCount = itemId - 40902;
			break;
		case 40906:
			chargeCount = 5;
			break;
		case 40907:
		case 40908:
			chargeCount = 20;
			break;
		}
		item.setChargeCount(chargeCount);
		if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light
			item.setRemainingTime(item.getItem().getLightFuel());
		} else if (itemId >= 60173 && itemId <= 60176) {
			item.setRemainingTime(3600 * 5);
		} else {
			item.setRemainingTime(item.getItem().getMaxUseTime());
		}
		item.setBless(item.getItem().getBless());

		if (itemId == L1ItemId.DRAGON_KEY) {// 드래곤 키
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 259200000);// 3일
			item.setEndTime(deleteTime);
		}
		if (itemId == 40312 || itemId == 49312) {// 여관열쇠
			// System.out.println("ㅇㅇ 10초");
			//System.out.println("ㅇㅇ 10초 2");
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 4));// 4시간
			item.setEndTime(deleteTime);
		}
		if (itemId == 20344 || itemId == 61000 || (itemId >= 21113 && itemId <= 21120)
				|| (itemId >= 60350 && itemId <= 60352)) { // 토끼투구, 정령악세(깃털)
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 3));// 3시간
			item.setEndTime(deleteTime);
			/*
			 * }else if(itemId >= 60173 && itemId <= 60176){//블레그 종류 마법인형
			 * Timestamp deleteTime = null; deleteTime = new
			 * Timestamp(System.currentTimeMillis() + (3600000*5));// 5시간
			 * item.setEndTime(deleteTime);
			 */} else if (itemId == 600234
				|| itemId == 60009
				|| itemId == 60010
				|| itemId == 21092
				|| itemId == 60061
				|| // 메린, 킬톤 계약서, 요리사모자, 버프코인
				(itemId >= 425000 && itemId <= 425002)
				|| // 엘모어 방어구
				(itemId >= 450000 && itemId <= 450007) // 엘모어 무기
				|| itemId == 430003 || itemId == 430505 || itemId == 430506
				|| itemId == 41915 || itemId == 5000034
				|| (itemId >= 21125 && itemId <= 21136)
				|| (itemId >= 21139 && itemId <= 21156)) { // 시댄서, 라미아, 스파토이,
															// 허수아비, 에틴
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 7일
			item.setEndTime(deleteTime);
		} else if (itemId == 60011 || itemId == 60014) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 9));// 9시간
			item.setEndTime(deleteTime);
		} else if (itemId == 60012 || itemId == 60015) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 17));// 17시간
			item.setEndTime(deleteTime);
		} else if (itemId == 60013 || itemId == 60016) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24));// 24시간
			item.setEndTime(deleteTime);
		} else if (itemId == 21094) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000);// 1시간
			item.setEndTime(deleteTime);
		} else if (itemId == 21157 || itemId == 121216) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000);// 30분
			item.setEndTime(deleteTime);
		} else if (itemId == 21272 || itemId == 421216) { //아르카의유물	
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000);// 30분
			item.setEndTime(deleteTime);
			
		} else if (itemId == 21271 || itemId == 421217 ||itemId == 421218 || itemId == 421219) { //아르카의강화된유물
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 7200000);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 421220 || itemId == 421221) { //조이의유물
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 600369) { //얼어붙은심장
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 86400000);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 30014) { //반역자의투구
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24 * 60);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 600263 || itemId == 600264 || itemId == 600265 || itemId == 600266) {// 안타 발라 린드 마법인형
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24 * 3);// 2시간
			item.setEndTime(deleteTime);
			
			
		} else if (itemId >= 9075 && itemId <= 9093) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 2일
			item.setEndTime(deleteTime);
			
		} else if (itemId >= 9097 && itemId <= 9098) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 2일
			item.setEndTime(deleteTime);

		} else if (itemId == 221216) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000 * 4);// 30분
			item.setEndTime(deleteTime);

		} else if ((itemId >= 267 && itemId <= 274)
				|| (itemId >= 21158 && itemId <= 21165)) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 3));// 3일
			item.setEndTime(deleteTime);
			// 상아템들
		} else if (/*
					 * itemId == 7 || itemId == 35 || itemId == 48 || itemId ==
					 * 73 || itemId == 105 || itemId == 120 || itemId == 147 ||
					 * itemId == 156 || itemId == 174 || itemId == 175 || itemId
					 * == 224 || itemId == 20028 || itemId == 20082 || itemId ==
					 * 20126 || itemId == 20173 || itemId == 20206 || itemId ==
					 * 20232 || itemId == 20282 || itemId == 201261 || itemId ==
					 * 21098 || (itemId >= 21102 && itemId <= 21112) || itemId
					 * == 21254
					 */
		(itemId >= 21099 && itemId <= 21112) || itemId == 21254
				|| itemId == 20082 || itemId == 7 || itemId == 35
				|| itemId == 48 || itemId == 73 || itemId == 105
				|| itemId == 120 || itemId == 147 || itemId == 7232
				|| itemId == 156 || itemId == 174 || itemId == 175
				|| itemId == 224) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 3일
			item.setEndTime(deleteTime);

		} else if (itemId == 60319) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 22));// 22시간
			item.setEndTime(deleteTime);
		} else if (itemId == 9056 || itemId == 141915 || itemId == 141916) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (86400000 * 7));// 22시간
			item.setEndTime(deleteTime);
		}  else if (itemId == 1419161) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 3));// 3시간
			item.setEndTime(deleteTime);
		}
		if (itemId == 60080 || itemId == 60082) {
			if (item.getCreaterName() == null)
				item.setCreaterName("*케플리샤*");
		}

		/** 생일 시스템 by이러버엉 **/
		// 야메 시스템 ㅋ 이런방식인것만...
		if (itemId == L1ItemId.HAPPY_BIRTHDAY_ELF) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 86400000);
			item.setEndTime(deleteTime);
		}
		if (itemId == L1ItemId.HAPPY_BIRTHDAY_METIS) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000);
			item.setEndTime(deleteTime);
		}
		/** 생일 시스템 by이러버엉 **/
		// 야메 시스템 ㅋ 이런방식인것만...
		if (item.isIdentified() != true) {
			item.setIdentified(false);
		}
		_items.add(item);
		insertItem(item);
		return item;
	}

	public synchronized L1ItemInstance storeNpcShopItem(L1ItemInstance item) {
		if (item.getCount() <= 0) {
			return null;
		}
		int itemId = item.getItem().getItemId();
		if (item.isStackable()) {
			L1ItemInstance findItem = null;
			if (item.getItem().getItemId() == 40309) {
				findItem = findItemTicketId(item.getItem().getItemId(),
						item.getSecondId(), item.getTicketId());
			} else if (item.getItem().getItemId() == 40312
					|| item.getItem().getItemId() == 49312
					|| item.getItemId() == 60285) {
				findItem = findItemKey(item.getItem().getItemId(),
						item.getEndTime(), item.getKey());
			} else {
				findItem = findItemId(itemId);
			}
			if (findItem != null) {
				findItem.setCount(findItem.getCount() + item.getCount());
				updateItem(findItem);
				return findItem;
			}
		}
		
		/** 지배 변신 관련 정보 관리 위하여 */
		DominationSkill(item, this, true);
		
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		int chargeCount = item.getItem().getMaxChargeCount();
		switch (itemId) {
		// case 40006:
		// case 40007: //흑단막대
		// case 40008:
		// case 41401:
		// case 140006:
		// case 140008:
		case 5000121:
			Random random = new Random(System.nanoTime());
			chargeCount -= random.nextInt(5);
			break;
		case 40903:
		case 40904:
		case 40905:
			chargeCount = itemId - 40902;
			break;
		case 40906:
			chargeCount = 5;
			break;
		case 40907:
		case 40908:
			chargeCount = 20;
			break;
		}
		item.setChargeCount(chargeCount);
		if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light
			item.setRemainingTime(item.getItem().getLightFuel());
		} else if (itemId >= 60173 && itemId <= 60176) {
			item.setRemainingTime(3600 * 5);
		} else {
			item.setRemainingTime(item.getItem().getMaxUseTime());
		}
		item.setBless(item.getItem().getBless());

		if (itemId == 40312 || itemId == 49312) {// 여관열쇠
			//System.out.println("ㅇㅇ 10초 3");
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+(3600000 * 4));// 4시간
			item.setEndTime(deleteTime);
		}
		if (itemId == 20344 || itemId == 61000 || (itemId >= 21113 && itemId <= 21120)
				|| (itemId >= 60350 && itemId <= 60352)) { // 토끼투구, 정령악세(깃털)
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 3));// 3시간
			item.setEndTime(deleteTime);
			/*
			 * }else if(itemId >= 60173 && itemId <= 60176){//블레그 종류 마법인형
			 * Timestamp deleteTime = null; deleteTime = new
			 * Timestamp(System.currentTimeMillis() + (3600000*5));// 5시간
			 * item.setEndTime(deleteTime);
			 */} else if (itemId == 600234
				|| itemId == 60009
				|| itemId == 21092
				|| itemId == 60061
				|| // 메린, 킬톤 계약서, 요리사모자, 버프코인
				(itemId >= 425000 && itemId <= 425002)
				|| // 엘모어 방어구
				(itemId >= 450000 && itemId <= 450007) // 엘모어 무기
				|| itemId == 430003 || itemId == 430505 || itemId == 430506
				|| itemId == 41915 || itemId == 5000034
				|| (itemId >= 21125 && itemId <= 21136)
				|| (itemId >= 21139 && itemId <= 21156)) { // 시댄서, 라미아, 스파토이,
															// 허수아비, 에틴
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 7일
			item.setEndTime(deleteTime);
		} else if (itemId == 60011 || itemId == 60014) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 9));// 9시간
			item.setEndTime(deleteTime);
		} else if (itemId == 60012 || itemId == 60015) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 17));// 17시간
			item.setEndTime(deleteTime);
		} else if (itemId == 60013 || itemId == 60016) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24));// 24시간
			item.setEndTime(deleteTime);
		} else if (itemId == 21094 || itemId == 60010) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000);// 1시간
			item.setEndTime(deleteTime);
		} else if (itemId == 21157 || itemId == 121216) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000);// 30분
			item.setEndTime(deleteTime);
		} else if (itemId == 21272 || itemId == 421216) { //아르카의유물	
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000);// 30분
			item.setEndTime(deleteTime);
			
		} else if (itemId == 21271 || itemId == 421217 ||itemId == 421218 || itemId == 421219) { //아르카의강화된유물
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 7200000);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 421220 || itemId == 421221) { //조이의유물
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 600369) { //얼어붙은심장
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 86400000);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 30014) { //반역자의투구
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24 * 60);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId == 600263 || itemId == 600264 || itemId == 600265 || itemId == 600266) {// 안타 발라 린드 마법인형
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000 * 24 * 3);// 2시간
			item.setEndTime(deleteTime);
			
		} else if (itemId >= 9075 && itemId <= 9093) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 2일
			item.setEndTime(deleteTime);
			
		} else if (itemId >= 9097 && itemId <= 9098) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 7));// 2일
			item.setEndTime(deleteTime);

		} else if (itemId == 221216) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 1800000 * 4);// 30분
			item.setEndTime(deleteTime);

		} else if ((itemId >= 267 && itemId <= 274)
				|| (itemId >= 21158 && itemId <= 21165)) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 24 * 3));// 3일
			item.setEndTime(deleteTime);
		} else if (itemId == 60319) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 22));// 22시간
			item.setEndTime(deleteTime);
		} else if (itemId == 9056 || itemId == 141915 || itemId == 141916) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (86400000 * 7));// 22시간
			item.setEndTime(deleteTime);
		}  else if (itemId == 1419161) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis()
					+ (3600000 * 3));// 3시간
			item.setEndTime(deleteTime);
		}
		if (itemId == 60080 || itemId == 60082) {
			if (item.getCreaterName() == null)
				item.setCreaterName("*케플리샤*");
		}

		/** 생일 시스템 by이러버엉 **/
		// 야메 시스템 ㅋ 이런방식인것만...
		if (itemId == L1ItemId.HAPPY_BIRTHDAY_ELF) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 86400000);
			item.setEndTime(deleteTime);
		}
		if (itemId == L1ItemId.HAPPY_BIRTHDAY_METIS) {
			Timestamp deleteTime = null;
			deleteTime = new Timestamp(System.currentTimeMillis() + 3600000);
			item.setEndTime(deleteTime);
		}
		/** 생일 시스템 by이러버엉 **/
		// 야메 시스템 ㅋ 이런방식인것만...
		if (item.isIdentified() != true) {
			item.setIdentified(false);
		}
		_items.add(item);
		insertItem(item);
		return item;
	}

	public synchronized L1ItemInstance storeTradeItem(L1ItemInstance item) {
		if (item.isStackable()) {
			L1ItemInstance finditem = null;
			if (item.getItem().getItemId() == 40312
					|| item.getItem().getItemId() == 49312
					|| item.getItemId() == 60285) {
				finditem = findItemKey(item.getItem().getItemId(),
						item.getEndTime(), item.getKey());
			} else if (item.getItem().getItemId() == 40309) {
				finditem = findItemTicketId(40309, item.getSecondId(),
						item.getTicketId());
			} else {
				finditem = findItemId(item.getItem().getItemId());
			}
			if (finditem != null) {
				finditem.setCount(finditem.getCount() + item.getCount());
				updateItem(finditem);
				return finditem;
			}
		}
		item.setX(getX());
		item.setY(getY());
		item.setMap(getMapId());
		_items.add(item);
		insertItem(item);
		return item;
	}
	
	public boolean remove_craft(int itemid, int count, int enchant) {
		if (count <= 0) {
			return false;
		}
		if (itemid == 0 && count == 0 && enchant == 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
			L1ItemInstance item = findItemId(itemid);
			if (item != null && item.getCount() >= count) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = check_craft(itemid, enchant);
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}
	
	public L1ItemInstance[] check_craft(int id, int enchant) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItemId() == id) {
				if (!item.isEquipped() && item.getBless() != 0 && item.getBless() < 129 && item.getEnchantLevel() == enchant) {
					itemList.add(item);
				}
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}
	
	public boolean consumeItem(L1ItemInstance item, int count) {//1
		if (count <= 0) {
			return false;
		}
		if (item.isStackable()) {
			if (item != null && item.getCount() >= count) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsIds(item.getItem().getItemId(), item.getBless(),
					item.getEnchantLevel());
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}
	
	public L1ItemInstance[] findItemsIds(int itemId, int bless, int enchant) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item == null)
				continue;
			if (item.getItemId() == itemId && item.getBless() == bless && item.getEnchantLevel() == enchant) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public boolean consumeItem(int itemid, int count) {
		if (count <= 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
			L1ItemInstance item = findItemId(itemid);
			if (item != null && item.getCount() >= count) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(itemid);
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			} else if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}
	public boolean consumeItem(int itemid, int count, int enlevel) {
		if (count <= 0) {
			return false;
		}
		if (ItemTable.getInstance().getTemplate(itemid).isStackable()) {
			L1ItemInstance item = findItemId(itemid);
			if ((item != null) && (item.getCount() >= count)) {
				removeItem(item, count);
				return true;
			}
		} else {
			L1ItemInstance[] itemList = findItemsId(itemid, enlevel);
			if (itemList.length == count) {
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
			if (itemList.length > count) {
				DataComparator dc = new DataComparator();
				extracted(itemList, dc);
				for (int i = 0; i < count; i++) {
					removeItem(itemList[i], 1);
				}
				return true;
			}
		}
		return false;
	}

	@SuppressWarnings("unchecked")
	private void extracted(L1ItemInstance[] itemList, DataComparator dc) {
		Arrays.sort(itemList, dc);
	}

	@SuppressWarnings("rawtypes")
	public class DataComparator implements java.util.Comparator {
		public int compare(Object item1, Object item2) {
			return ((L1ItemInstance) item1).getEnchantLevel()
					- ((L1ItemInstance) item2).getEnchantLevel();
		}
	}
	
	public int removeItem(int objectId) {
		L1ItemInstance item = getItem(objectId);
		return removeItem(item, item.getCount());
	}

	public int removeItem(int objectId, int count) {
		L1ItemInstance item = getItem(objectId);
		return removeItem(item, count);
	}

	public int removeItem(L1ItemInstance item) {
		return removeItem(item, item.getCount());
	}

	public int removeItem(L1ItemInstance item, int count) {
		try {
			if (item == null) {
				return 0;
			}

			if (item.getCount() <= 0 || count <= 0) {
				return 0;
			}
			if (item.getCount() < count) {
				try {
					if (this instanceof L1PcInventory) {
						L1PcInventory in = (L1PcInventory) this;
						String 소지자 = in.getOwner() != null ? in.getOwner()
								.getName() : "없음";
						_log.info("L1Inventory removeItem - item 갯수보다 많은 갯수를 요구해서 처리 -소지자:"
								+ 소지자
								+ " item:"
								+ item.getLogName()
								+ " 요구갯수:"
								+ count);
					}
				} catch (Exception e) {
				}
				count = item.getCount();
			}
			
			/** 지배 변신 관련 정보 관리 위하여 */
			DominationSkill(item, null, false);

			if (item.getItemId() == 40312) {
				InnTimer IT = INN.getInnTimer(item.getKey());
				if (IT != null) {
					IT.키차감(count);
				}
			}

			if (item.getCount() == count) {
				int itemId = item.getItem().getItemId();
				if (itemId == 40314 || itemId == 40316) {
					PetTable.getInstance().deletePet(item.getId());
				} else if (itemId >= 41383 && itemId <= 41400) {
					for (L1Object l1object : L1World.getInstance().getObject()) {
						if (l1object instanceof L1FurnitureInstance) {
							L1FurnitureInstance obj = (L1FurnitureInstance) l1object;
							if (obj.getItemObjId() == item.getId()) {
								FurnitureSpawnTable.getInstance()
										.deleteFurniture(obj);
							}
						}
					}
				} else if (itemId == L1ItemId.DRAGON_KEY) {
					BoardTable.getInstance().delDayExpire(item.getId());
				}

				deleteItem(item);
				L1World.getInstance().removeObject(item);
			} else {
				item.setCount(item.getCount() - count);
				updateItem(item);
			}

			return count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}

	public void deleteItem(L1ItemInstance item) {
		_items.remove(item);
	}

	public synchronized L1ItemInstance tradeItem(int objectId, int count, Warehouse inventory) {
		L1ItemInstance item = getItem(objectId);
		return tradeItem(item, count, inventory);
	}

	public synchronized L1ItemInstance tradeItem(int objectId, int count, L1Inventory inventory) {
		L1ItemInstance item = getItem(objectId);
		return tradeItem(item, count, inventory);
	}

	public synchronized L1ItemInstance tradeItem(L1ItemInstance item, int count, Warehouse inventory) {
		if (item == null) {
			return null;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return null;
		}
		if (item.isEquipped()) {
			return null;
		}
		if (!checkItem(item.getItem().getItemId(), count)) {
			return null;
		}
		
		/** 지배 변신 관련 정보 관리 위하여 */
		DominationSkill(item, null, false);
		
		L1ItemInstance carryItem;
		// 엔진관련 버그 방지 추가
		if (item.getCount() <= count || count < 0) {
			deleteItem(item);
			carryItem = item;
		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
			carryItem = ItemTable.getInstance().createItem(
					item.getItem().getItemId());
			carryItem.setCount(count);
			carryItem.setEnchantLevel(item.getEnchantLevel());
			carryItem.setIdentified(item.isIdentified());
			carryItem.set_durability(item.get_durability());
			carryItem.setChargeCount(item.getChargeCount());
			carryItem.setRemainingTime(item.getRemainingTime());
			carryItem.setLastUsed(item.getLastUsed());
			carryItem.setBless(item.getItem().getBless());
			carryItem.setAttrEnchantLevel(item.getAttrEnchantLevel());
			carryItem.setRegistLevel(item.getRegistLevel()); // 두 곳 모두 추가해주세요.
			carryItem.setKey(item.getKey());
			carryItem.setEndTime(item.getEndTime());
			carryItem.setSecondId(item.getSecondId());
			carryItem.setRoundId(item.getRoundId());
			carryItem.setTicketId(item.getTicketId());
			carryItem.setCreaterName(item.getCreaterName());
		}
		return inventory.storeTradeItem(carryItem);
	}

	public synchronized L1ItemInstance tradeItem(L1ItemInstance item, int count, L1Inventory inventory) {
		if (item == null) {
			return null;
		}
		if (item.getCount() <= 0 || count <= 0) {
			return null;
		}
		if (item.isEquipped()) {
			return null;
		}
		if (!checkItem(item.getItem().getItemId(), count)) {
			return null;
		}
		if (item.getItemId() == L1ItemId.DRAGON_KEY)
			BoardTable.getInstance().delDayExpire(item.getId());

		/** 지배 변신 관련 정보 관리 위하여 */
		DominationSkill(item, inventory, false);
		
		L1ItemInstance carryItem;
		// 엔진관련 버그 방지 추가
		if (item.getCount() <= count || count < 0) {

			deleteItem(item);
			carryItem = item;

		} else {
			item.setCount(item.getCount() - count);
			updateItem(item);
			carryItem = ItemTable.getInstance().createItem(
					item.getItem().getItemId());
			carryItem.setCount(count);
			carryItem.setEnchantLevel(item.getEnchantLevel());
			carryItem.setIdentified(item.isIdentified());
			carryItem.set_durability(item.get_durability());
			carryItem.setChargeCount(item.getChargeCount());
			carryItem.setRemainingTime(item.getRemainingTime());
			carryItem.setLastUsed(item.getLastUsed());
			carryItem.setBless(item.getItem().getBless());
			carryItem.setAttrEnchantLevel(item.getAttrEnchantLevel());
			carryItem.setRegistLevel(item.getRegistLevel()); // 두 곳 모두 추가해주세요.
			carryItem.setEndTime(item.getEndTime());
			carryItem.setKey(item.getKey());
			carryItem.setSecondId(item.getSecondId());
			carryItem.setRoundId(item.getRoundId());
			carryItem.setTicketId(item.getTicketId());
			carryItem.setCreaterName(item.getCreaterName());
		}

		if (carryItem.getSkill() != null) {
			carryItem.getSkillExit();
		}
		return inventory.storeTradeItem(carryItem);
	}

	public L1ItemInstance receiveDamage(int objectId) {
		L1ItemInstance item = getItem(objectId);
		return receiveDamage(item);
	}

	public L1ItemInstance receiveDamage(L1ItemInstance item) {
		return receiveDamage(item, 1);
	}

	public L1ItemInstance receiveDamage(L1ItemInstance item, int count) {
		if (item == null) {
			return null;
		}
		int itemType = item.getItem().getType2();
		int currentDurability = item.get_durability();

		if (item.getItemOwner() != null) {
			L1PcInstance pc = item.getItemOwner();
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.SOUL_OF_FLAME)) {
				return null;
			}
		}

		if ((currentDurability == 0 && itemType == 0) || currentDurability < 0) {
			item.set_durability(0);
			return null;
		}

		if (itemType == 0) {
			int minDurability = (item.getEnchantLevel() + 5) * -1;
			int durability = currentDurability - count;
			if (durability < minDurability) {
				durability = minDurability;
			}
			if (currentDurability > durability) {
				item.set_durability(durability);
			}
		} else {
			int maxDurability = item.getEnchantLevel() + 5;
			int durability = currentDurability + count;
			if (durability > maxDurability) {
				durability = maxDurability;
			}
			if (currentDurability < durability) {
				item.set_durability(durability);
			}
		}

		updateItem(item, L1PcInventory.COL_DURABILITY);
		return item;
	}

	public L1ItemInstance recoveryDamage(L1ItemInstance item) {
		if (item == null) {
			return null;
		}
		int itemType = item.getItem().getType2();
		int durability = item.get_durability();

		if ((durability == 0 && itemType != 0) || durability < 0) {
			item.set_durability(0);
			return null;
		}

		if (itemType == 0) {
			item.set_durability(durability + 1);
		} else {
			item.set_durability(durability - 1);
		}

		updateItem(item, L1PcInventory.COL_DURABILITY);
		return item;
	}

	public L1ItemInstance findItemId(int id) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == id) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance findEncobj(String id) {
		for (L1ItemInstance item : _items) {
			if (item.getEncobjid().equals(id)) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance[] findItemsId(int id) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}
	public L1ItemInstance[] findItemsId(int id, int en) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : this._items) {
			if ((item.getItemId() == id) && (item.getEnchantLevel() == en)) {
				itemList.add(item);
			}
		}
		return (L1ItemInstance[]) itemList.toArray(new L1ItemInstance[0]);
	}
	
	public synchronized L1ItemInstance storeItem(L1Item temp, int count, int enchant) {
		if (count <= 0) return null;
		if (temp == null) return null;
		if (temp.isStackable()) {
			L1ItemInstance item = ItemTable.getInstance().FunctionItem(temp);
			item.setCount(count);
			if (findItemId(temp.getItemId()) == null) {
				item.setId(IdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}
		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = ItemTable.getInstance().FunctionItem(temp);
			item.setId(IdFactory.getInstance().nextId());
			item.setEnchantLevel(enchant);
			item.setIdentified(true);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		return result;
	}

	public L1ItemInstance[] findItemsIds(int id, int id2) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id || item.getItemId() == id2) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}
	
	public L1ItemInstance[] findItemsIds(int id, int id2, int id3, int id4) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id || item.getItemId() == id2 || item.getItemId() == id3 || item.getItemId() == id4) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance[] findItemsIdNotEquipped(int id) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id) {
				if (!item.isEquipped()) {
					itemList.add(item);
				}
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance[] findItemsId_HighEnchant(int id, int enchant) {
		ArrayList<L1ItemInstance> itemList = new ArrayList<L1ItemInstance>();
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id && item.getEnchantLevel() >= enchant) {
				itemList.add(item);
			}
		}
		return itemList.toArray(new L1ItemInstance[] {});
	}

	public L1ItemInstance findItemsIdNotEquipped_Enchant(int id, int enchant) {
		for (L1ItemInstance item : _items) {
			if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
				if (!item.isEquipped())
					return item;
			}
		}
		return null;
	}

	public L1ItemInstance getItem(int objectId) {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			if (item.getId() == objectId) {
				return item;
			}
		}
		return null;
	}

	public boolean checkItem(int id) {
		return checkItem(id, 1);
	}
	// ////////////////인첸된 체크 아이템 재코딩
		public boolean checkEnchantItem(int id, int enchant, int count) {
				int num = 0;
				for (L1ItemInstance item : _items) {
					if (item.isEquipped()) {
						continue;
					}
					if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
						num++;
						if (num == count) {
							return true;
						}
					}
				}
				return false;
			}
			
			public boolean 제작리스트(int id, int enchant, int count) {
				int num = 0;
				for (L1ItemInstance item : _items) {
					if (item.isEquipped()) {
						continue;
					}
					if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
						num += item.getCount();
						if (num >= count) {
							return true;
						}
					}
				}
				return false;
			}

			public boolean consumeEnchantItem(int id, int enchant, int count) {
				for (L1ItemInstance item : _items) {
					if (item.isEquipped()) {
						continue;
					}
					if (item.getItemId() == id && item.getEnchantLevel() == enchant) {
						removeItem(item);
						return true;
					}
				}
				return false;
			}

			// ////////////////////////////////////
			// ////////////////인첸된 체크 아이템 재코딩
			/*속성인첸트템 코딩*/
			public boolean checkAttrEnchantItem(int id,int enchant,int attr, int count) {
				int num = 0;
				for (L1ItemInstance item : _items) {
					if (item.isEquipped()) {
						continue;
					}
					if (item.getItemId() == id && item.getEnchantLevel() == enchant && item.getAttrEnchantLevel() ==attr) {
						num++;
						if (num == count) {
							return true;
						}
					}
				}
				return false;
			}
			
			public boolean consumeAttrItem(int id, int enchant,int attr, int count) {
				for (L1ItemInstance item : _items) {
					if (item.isEquipped()) {
						continue;
					}
					if (item.getItemId() == id && item.getEnchantLevel() == enchant&&item.getAttrEnchantLevel() == attr) {
						removeItem(item);
						return true;
					}
				}
				return false;
			}

				// ////////////////////////////////////
	public boolean checkItem(int id, int count) {
		if (count == 0) {
			return true;
		}
		if (ItemTable.getInstance().getTemplate(id).isStackable()) {
			L1ItemInstance item = findItemId(id);
			if (item != null && item.getCount() >= count) {
				return true;
			}
		} else {
			Object[] itemList = findItemsId(id);
			if (itemList.length >= count) {
				return true;
			}
		}
		return false;
	}

	public boolean checkItemNotEquipped(int id, int count) {
		if (count == 0) {
			return true;
		}
		return count <= countItems(id);
	}

	public boolean checkItem(int[] ids) {
		int len = ids.length;
		int[] counts = new int[len];
		for (int i = 0; i < len; i++) {
			counts[i] = 1;
		}
		return checkItem(ids, counts);
	}
	
	public boolean checkItemandEnchant(int[] ids, int[] counts, int[] en) {
		for (int i = 0; i < ids.length; i++) {
			if (!checkItem(ids[i], counts[i], en[i])) {
				return false;
			}
		}
		return true;
	}
	public boolean checkItem(int id, int count, int en) {
		if (count == 0) {
			return true;
		}
		if (ItemTable.getInstance().getTemplate(id).isStackable()) {
			L1ItemInstance item = findItemId(id);
			if ((item != null) && (item.getCount() >= count))
				return true;
		} else {
			L1ItemInstance[] itemList = findItemsId(id, en);
			if (itemList.length >= count) {
				return true;
			}
		}
		return false;
	}
	public boolean checkItem(int[] ids, int[] counts) {
		for (int i = 0; i < ids.length; i++) {
			if (!checkItem(ids[i], counts[i])) {
				return false;
			}
		}
		return true;
	}
	
	//제작테이블
		public L1ItemInstance findCraftMaterialItem(int descId, int count) {
			for (L1ItemInstance item : _items) {
				if (item != null && !item.isEquipped() && item.getItem().getItemDescId() == descId && item.getCount() >= count) {
					return item;
				}
			}
			return null;
		}

	public int countItems(int id) {
		if (ItemTable.getInstance().getTemplate(id).isStackable()) {
			L1ItemInstance item = findItemId(id);
			if (item != null) {
				return item.getCount();
			}
		} else {
			Object[] itemList = findItemsIdNotEquipped(id);
			return itemList.length;
		}
		return 0;
	}

	public void shuffle() {
		Collections.shuffle(_items);
	}

	public void clearItems() {
		L1ItemInstance item = null;
		for (Object itemObject : _items) {
			item = (L1ItemInstance) itemObject;
			L1World.getInstance().removeObject(item);
		}
		_items.clear();
	}

	public void loadItems() {}

	public void insertItem(L1ItemInstance item) {	}

	public void insertItem(L1ItemInstance item, boolean ok) {	}

	public void updateItem(L1ItemInstance item) {	}

	public void updateItem(L1ItemInstance item, int colmn) {	}
	
	public void CheckItemSkill() {
		if (ItemTable.getInstance().getTemplate(L1ItemId.ADENA).isStackable()) {
		}
	}

	// 새로운 아이템의 격납 : 쪼꼬 재코딩
	public L1ItemInstance storeItem(int id, int count, String name) {
		L1Item sTemp = ItemTable.getInstance().getTemplate(id);
		String sname = "테베 오시리스 제단 열쇠 [" + CrockSystem.getInstance().OpenTime()
				+ "]";
		L1Item temp = ItemTable.getInstance().clone(sTemp, sname);
		if (temp == null)
			return null;
		if (temp.isStackable()) {
			L1ItemInstance item = new L1ItemInstance(temp, count);
			item.setItem(temp);
			item.setCount(count);
			item.setBless(temp.getBless());
			item.setAttrEnchantLevel(0);
			if (!temp.isStackable() || findItemId(id) == null) {// 새롭게 생성할 필요가
																// 있는 경우만 ID의
																// 발행과 L1World에의
																// 등록을 실시한다
				item.setId(ObjectIdFactory.getInstance().nextId());
				L1World.getInstance().storeObject(item);
			}
			return storeItem(item);
		}

		// 스택 할 수 없는 아이템의 경우
		L1ItemInstance result = null;
		L1ItemInstance item = null;
		for (int i = 0; i < count; i++) {
			item = new L1ItemInstance(temp);
			item.setId(ObjectIdFactory.getInstance().nextId());
			item.setBless(temp.getBless());
			item.setAttrEnchantLevel(0);
			L1World.getInstance().storeObject(item);
			storeItem(item);
			result = item;
		}
		// 마지막에 만든 아이템을 돌려준다. 배열을 되돌리도록(듯이) 메소드 정의를 변경하는 편이 좋을지도 모른다.
		return result;
	}

	// 아이템 ID, second_id, ticketId로부터 검색
	public L1ItemInstance findItemTicketId(int id, int secid, int ticketid) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == id && item.getSecondId() == secid
					&& item.getTicketId() == ticketid) {
				return item;
			}
		}
		return null;
	}

	public L1ItemInstance findItemKey(int id, Timestamp endtime, int key) {
		for (L1ItemInstance item : _items) {
			if (item.getItem().getItemId() == id
					&& item.getEndTime() == endtime && item.getKey() == key) {
				return item;
			}
		}
		return null;
	}

	/** 지배 반지 삭제시 변신 버프 같이 삭제 하도록 정리 */
	public void DominationSkill(L1ItemInstance Item, L1Inventory inventory, boolean Newinventory) {
		try {
			/** 만약 지배 반지가 아니라면 무조건 리턴 계산 안함 */
			if (Item.getItemId() != 220281) return;
			if (!Newinventory && this instanceof L1PcInventory) {
				L1PcInventory PcInventory = (L1PcInventory) this;
				/** 1개주고도 하나더 소지하고잇따면 리턴 */
				if(PcInventory.checkItem(220281, 2)) return;
				PcInventory.getOwner().SetDomination(false);
				PcInventory.getOwner().sendPackets(new S_Ability(7, false), true);	
				if (PcInventory.getOwner().getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SHAPE_CHANGE)) {
					if (PcInventory.getOwner().getSkillEffectTimerSet().GetDominationSkill()){
						PcInventory.getOwner().getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
						L1PolyMorph.undoPoly(PcInventory.getOwner());
					}				
				}
			/*}else if (inventory instanceof L1PcInventory) {
				L1PcInventory PcInventory = (L1PcInventory) inventory;
				PcInventory.getOwner().SetDomination(true);
				PcInventory.getOwner().sendPackets(new S_Ability(7, true), true);	
			}*/
			}else if (inventory instanceof L1PcInventory) {
				L1PcInventory PcInventory = (L1PcInventory) inventory;
				if(!PcInventory.getOwner().GetDomination()){
					PcInventory.getOwner().SetDomination(true);
					PcInventory.getOwner().sendPackets(new S_Ability(7, true), true);	
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public boolean consumeItem(int itemid) {
		L1ItemInstance item = findItemId(itemid);
		if (item != null) {
			removeItem(item, item.getCount());
			return true;
		}
		return false;
	}
}
