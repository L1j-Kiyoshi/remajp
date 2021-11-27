/*
n * This program is free software; you can redistribute it and/or modify
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import l1j.server.Config;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

public class Enchant extends L1ItemInstance {
	private static final long serialVersionUID = 1L;

	private static Random _random = new Random(System.nanoTime());

	Calendar currentDate = Calendar.getInstance();
	SimpleDateFormat dateFormat = new SimpleDateFormat("MM.dd h:mm:ss a");
	String time = dateFormat.format(currentDate.getTime());

	public Enchant(L1Item item) {
		super(item);
	}

	public void SuccessEnchant(L1PcInstance pc, L1ItemInstance item, int i) {
		String s = "";
		String sa = "";
		String sb = "";
		String s1 = item.getName();

		if (item.getItem().getType2() == 1) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
				case -1:
					s = item.getLogName();
					sa = "$246";
					sb = "$247";
					break;

				case 1: // '\001'
					s = item.getLogName();
					sa = "$245";
					sb = "$247";
					break;

				case 2: // '\002'
					s = item.getLogName();
					sa = "$245";
					sb = "$248";
					break;

				case 3: // '\003'
					s = item.getLogName();
					sa = "$245";
					sb = "$248";
					break;
				}
			} else {
				switch (i) {
				case -1:
					/*
					 * s = (new StringBuilder()).append( pm +
					 * item.getEnchantLevel()).append(" ").append(s1) .toString(); // \f1%0이%2%1
					 * 빛납니다.
					 */
					s = item.getLogName();
					sa = "$246";
					sb = "$247";
					break;

				case 1: // '\001'
					/*
					 * s = (new StringBuilder()).append( pm +
					 * item.getEnchantLevel()).append(" ").append(s1) .toString(); // \f1%0이%2%1
					 * 빛납니다.
					 */
					s = item.getLogName();
					sa = "$245";
					sb = "$247";
					break;

				case 2: // '\002'
					/*
					 * s = (new StringBuilder()).append( pm +
					 * item.getEnchantLevel()).append(" ").append(s1) .toString(); // \f1%0이%2%1
					 * 빛납니다.
					 */
					s = item.getLogName();
					sa = "$245";
					sb = "$248";
					break;

				case 3: // '\003'
					/*
					 * s = (new StringBuilder()).append( pm +
					 * item.getEnchantLevel()).append(" ").append(s1) .toString(); // \f1%0이%2%1
					 * 빛납니다.
					 */
					s = item.getLogName();
					sa = "$245";
					sb = "$248";
					break;
				}
			}
		} else if (item.getItem().getType2() == 2) {
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				switch (i) {
				case -1:
					s = s1;
					sa = "$246";
					sb = "$247";
					break;

				case 1: // '\001'
					if (item.getItem().getGrade() < 0) {
						s = s1;
						sa = "$252";
						sb = "$247";
					} else {
						s = s1;
						sa = "$245";
						sb = "$248";
					}
					break;
				case 2: // '\002'
					s = s1;
					sa = "$252";
					sb = "$248";
					break;

				case 3: // '\003'
					s = s1;
					sa = "$252";
					sb = "$248";
					break;
				}
			} else {
				switch (i) {
				case -1:
					s = item.getLogName();
					// s = (new StringBuilder()).append(
					// pm + item.getEnchantLevel()).append(" ").append(s1)
					// .toString(); // \f1%0이%2%1 빛납니다.
					sa = "$246";
					sb = "$247";
					break;

				case 1: // '\001'
					s = item.getLogName();
					// s = (new StringBuilder()).append(
					// pm + item.getEnchantLevel()).append(" ").append(s1)
					// .toString(); // \f1%0이%2%1 빛납니다.
					if (item.getItem().getGrade() < 0) {
						sa = "$252";
						sb = "$247";
					} else {
						sa = "$245";
						sb = "$248";
					}
					break;

				case 2: // '\002'
					s = item.getLogName();
					// s = (new StringBuilder()).append(
					// pm + item.getEnchantLevel()).append(" ").append(s1)
					// .toString(); // \f1%0이%2%1 빛납니다.
					sa = "$252";
					sb = "$248";
					break;

				case 3: // '\003'
					s = item.getLogName();
					// s = (new StringBuilder()).append(
					// pm + item.getEnchantLevel()).append(" ").append(s1)
					// .toString(); // \f1%0이%2%1 빛납니다.
					sa = "$252";
					sb = "$248";
					break;
				}
			}
		}

		pc.setLastEnchantItemid(0, null);
		pc.sendPackets(new S_ServerMessage(161, s, sa, sb), true);
		int oldEnchantLvl = item.getEnchantLevel();
		int newEnchantLvl = item.getEnchantLevel() + i;
		int attr_level = item.getAttrEnchantLevel();
		int safe_enchant = item.getItem().get_safeenchant();

		/** 무기 9이상 인첸시 메세지 출력 **/ // by.초작살미남
		if (item.getItem().getType2() == 1 && newEnchantLvl >= 10) {
			L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4444, item), true);
		}
		/** 악세 7이상 인첸시 메세지 출력 **/ // by.초작살미남
		if (item.getItem().getType2() == 2) {
			if (item.getItem().getType() >= 8 && item.getItem().getType() <= 12) {
				if (newEnchantLvl >= 7) {
					L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4445, item), true);
				}
			} /** 방어구 8이상 인첸시 메세지 출력 **/ // by.초작살미남
			else if (newEnchantLvl >= 9) {
				L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 4444, item), true);
			}
		}

		item.setEnchantLevel(newEnchantLvl);

		if (oldEnchantLvl >= 10 && newEnchantLvl < 10 && (attr_level == 4 || attr_level == 9 || attr_level == 14 || attr_level == 19)) {
			if (attr_level == 4)
				item.setAttrEnchantLevel(3);
			else if (attr_level == 9)
				item.setAttrEnchantLevel(8);
			else if (attr_level == 14)
				item.setAttrEnchantLevel(13);
			else if (attr_level == 19)
				item.setAttrEnchantLevel(18);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			pc.sendPackets(new S_ServerMessage(3296, item.getViewName()), true);
		} else if (oldEnchantLvl >= 11 && newEnchantLvl < 11 && (attr_level == 5 || attr_level == 10 || attr_level == 15 || attr_level == 20)) {
			if (attr_level == 5)
				item.setAttrEnchantLevel(4);
			else if (attr_level == 10)
				item.setAttrEnchantLevel(9);
			else if (attr_level == 15)
				item.setAttrEnchantLevel(14);
			else if (attr_level == 20)
				item.setAttrEnchantLevel(19);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
			pc.sendPackets(new S_ServerMessage(3296, item.getViewName()), true);
		}
		pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
		pc.sendPackets(new S_PacketBox(item, S_PacketBox.인챈변경));
		pc.saveInventory();
		if (newEnchantLvl > safe_enchant) {
			pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL);
		}

		if (item.getItem().getType2() == 1 && Config.LOGGING_WEAPON_ENCHANT != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.LOGGING_WEAPON_ENCHANT) {
			}
		}
		if (item.getItem().getType2() == 2 && Config.LOGGING_ARMOR_ENCHANT != 0) {
			if (safe_enchant == 0 || newEnchantLvl >= Config.LOGGING_ARMOR_ENCHANT) {
			}
		}

		if (newEnchantLvl > safe_enchant) {
			if (Config.인첸채팅모니터() > 0) {
				for (L1PcInstance gm : Config.toArray인첸채팅모니터()) {
					if (gm.getNetConnection() == null) {
						Config.remove인첸(gm);
						continue;
					}
					if (gm == pc) {
						continue;
					}
					gm.sendPackets(
							new S_SystemMessage("\\fV[" + pc.getName() + "] " + item.getName() + " : " + oldEnchantLvl + "->" + newEnchantLvl + " [인첸 성공]"),
							true);
				}
			}
		}

		if (item.getItem().getType2() == 2) {
			if (item.isEquipped()) {
				if (item.getItem().getType() >= 8 && item.getItem().getType() <= 12) {
				} else {
					pc.getAC().addAc(-i);
				}
				int i2 = item.getItem().getItemId();
				if (i2 == 500214 || i2 == 20011 || i2 == 20110 || i2 == 120011 || i2 == 9091 || i2 == 20117 || i2 == 420108 || i2 == 420109 || i2 == 420110
						|| i2 == 420111 || i2 == 425108 || i2 == 490008 || i2 == 490017 || i2 == 120194 || i2 == 1020110 || (i2 >= 21169 && i2 <= 21172)) { // 매직
					// 헤룸, 매직
					// 체인 메일
					pc.getResistance().addMr(i);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				if (i2 == 20056 || i2 == 120056 || i2 == 9092 || i2 == 220056 || i2 == 425108 || i2 == 9084 || i2 == 20074 || i2 == 120074) { // 매직 클로크
					pc.getResistance().addMr(i * 2);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				if (i2 == 20078 || i2 == 20079 || i2 == 120079 || i2 == 21137) {
					pc.getResistance().addMr(i * 3);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				if (i2 == 220011 || i2 == 20049 || i2 == 20050 || i2 == 2020049) {
					pc.getResistance().addMr(i * 2);
					pc.sendPackets(new S_SPMR(pc), true);
				}
				if (i2 == 20001) {
					pc.setMaxMp(pc.getMaxMp() + (i * 10));
				}
			}

			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
		}
	}

	public void FailureEnchant(L1PcInstance pc, L1ItemInstance item) {
		FailureEnchant(pc, item, 0);
	}

	public void FailureEnchant(L1PcInstance pc, L1ItemInstance item, int scrollid) {
		String s = "";
		String sa = "";
		int itemType = item.getItem().getType2();
		int itemId = item.getItem().getItemId();
		String nameId = item.getName();
		if (itemType == 1) { // 무기
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				s = nameId; // \f1%0%s 강렬하게%1 빛나더니 증발되어 사라집니다.
				sa = "$245";
			} else {
				s = item.getLogName();
				sa = "$245";
			}
		} else if (itemType == 2) { // 방어용 기구
			if (!item.isIdentified() || item.getEnchantLevel() == 0) {
				s = nameId; // \f1%0%s 강렬하게%1 빛나더니 증발되어 사라집니다.
				if (item.getItem().getGrade() < 0)
					sa = "$252";
				else
					sa = "$245";
			} else {
				s = item.getLogName();
				if (item.getItem().getGrade() < 0)
					sa = "$252";
				else
					sa = "$245";
			}
		}

		if (scrollid == L1ItemId.ORIM_ACCESSORY_ENCHANT_SCROLL) {
			String item_name_id = item.getName();
			int enchant_level = item.getEnchantLevel();
			String pm1 = "";
			String msg = "";
			if (enchant_level > 0) {
				pm1 = "+";
			}
			// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
			msg = (new StringBuilder()).append(pm1 + enchant_level).append(" ").append(item_name_id).toString();
			pc.sendPackets(new S_ServerMessage(161, msg, "$246", "$247"), true);
			int newEnchantLvl = item.getEnchantLevel() - 1;
			item.setEnchantLevel(newEnchantLvl);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.saveInventory();

			if (Config.인첸채팅모니터() > 0) {
				for (L1PcInstance gm : Config.toArray인첸채팅모니터()) {
					if (gm.getNetConnection() == null) {
						Config.remove인첸(gm);
						continue;
					}
					if (gm == pc) {
						continue;
					}
					gm.sendPackets(new S_SystemMessage("\\fV[" + pc.getName() + "] +" + item.getEnchantLevel() + " " + item.getName() + " [오림인첸 실패]"));
				}
			}
		}

		else if (scrollid == 600406) {
			String item_name_id = item.getName();
			int enchant_level = item.getEnchantLevel();
			String pm1 = "";
			String msg = "";
			if (enchant_level > 0) {
				pm1 = "+";
			}
			// 인챈트: %0%s 소멸의 굴레를 극복 하였습니다.
			msg = (new StringBuilder()).append(pm1 + enchant_level).append(" ").append(item_name_id).toString();
			pc.sendPackets(new S_ServerMessage(161, msg, "$246", "$247"), true);
			int newEnchantLvl = item.getEnchantLevel();
			item.setEnchantLevel(newEnchantLvl);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.saveInventory();

			if (Config.인첸채팅모니터() > 0) {
				for (L1PcInstance gm : Config.toArray인첸채팅모니터()) {
					if (gm.getNetConnection() == null) {
						Config.remove인첸(gm);
						continue;
					}
					if (gm == pc) {
						continue;
					}
					gm.sendPackets(new S_SystemMessage("\\fV[" + pc.getName() + "] +" + item.getEnchantLevel() + " " + item.getName() + " [유물인첸 실패]"));
				}
			}
		}

		else if ((itemId >= 450022 && itemId <= 450025) || (itemId >= 427110 && itemId <= 427112)) {
			pc.sendPackets(new S_ServerMessage(1310), true); // 강렬하게 빛났지만 장비가 증발
																// 되지는 않았습니다.
			pc.getInventory().setEquipped(item, false); // 추가해주세요.
			item.setEnchantLevel(0);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL);
			pc.saveInventory();
			if (Config.인첸채팅모니터() > 0) {
				for (L1PcInstance gm : Config.toArray인첸채팅모니터()) {
					if (gm.getNetConnection() == null) {
						Config.remove인첸(gm);
						continue;
					}
					if (gm == pc) {
						continue;
					}
					gm.sendPackets(new S_SystemMessage("\\fV[" + pc.getName() + "] +" + item.getEnchantLevel() + " " + item.getName() + " [인첸 실패]"), true);
				}
			}
		}

		else if (item.isDemonBongin()) {
			if ((item.getItemId() >= 263 && item.getItemId() <= 265) || item.getItemId() == 256 || item.getItemId() == 4500027 || item.getItemId() == 4500026)
				pc.sendPackets(new S_SystemMessage("강렬하게 빛났지만 할로윈의 보호가 깃들어있어 증발 되지는 않았습니다."), true);
			else
				pc.sendPackets(new S_SystemMessage("강렬하게 빛났지만 마족의 보호가 깃들어있어 증발 되지는 않았습니다."), true);
			// pc.sendPackets(new S_ServerMessage(1310), true); // 강렬하게 빛났지만 장비가
			// 증발 되지는 않았습니다.
			pc.getInventory().setEquipped(item, false); // 추가해주세요.

			item.setEnchantLevel(item.getEnchantLevel() - 1);
			item.setDemonBongin(false);
			pc.getInventory().updateItem(item, L1PcInventory.COL_ENCHANTLVL + L1PcInventory.COL_DEMONBONGIN);
			pc.getInventory().saveItem(item, L1PcInventory.COL_ENCHANTLVL + L1PcInventory.COL_DEMONBONGIN);
			pc.saveInventory();
			if (Config.인첸채팅모니터() > 0) {
				for (L1PcInstance gm : Config.toArray인첸채팅모니터()) {
					if (gm.getNetConnection() == null) {
						Config.remove인첸(gm);
						continue;
					}
					if (gm == pc) {
						continue;
					}
					gm.sendPackets(new S_SystemMessage("\\fV[" + pc.getName() + "] +" + item.getEnchantLevel() + " " + item.getName() + " [인첸 실패-> No 증발]"),
							true);
				}
			}

		} else {
			if (pc.getSecondWeapon() != null) {
				if (pc.getSecondWeapon().getId() == item.getId()) {
					pc.getInventory().setEquipped(pc.getSecondWeapon(), false, false, false, true);
				}
			}
			if (pc.getWeapon() != null) {
				if (pc.getWeapon().getId() == item.getId()) {
					pc.getInventory().setEquipped(pc.getWeapon(), false, false, false, false);
				}
			}

			pc.setLastEnchantItemid(item.getId(), item);
			pc.sendPackets(new S_ServerMessage(164, s, sa), true);
			pc.getInventory().removeItem(item, item.getCount());
			pc.saveInventory();
			if (Config.인첸채팅모니터() > 0) {
				for (L1PcInstance gm : Config.toArray인첸채팅모니터()) {
					if (gm.getNetConnection() == null) {
						Config.remove인첸(gm);
						continue;
					}
					if (gm == pc) {
						continue;
					}
					gm.sendPackets(new S_SystemMessage("\\fV[" + pc.getName() + "] +" + item.getEnchantLevel() + " " + item.getName() + " [인첸 실패]"), true);
				}
			}
		}
	}

	public void RegistEnchant(L1PcInstance pc, L1ItemInstance item, int item_id) {
		int level = item.getRegistLevel();
		int chance = _random.nextInt(20) + 1;
		boolean bEquipped = false;

		if (item.isEquipped()) {
			pc.getInventory().setEquipped(item, false);
			bEquipped = true;
		}
		if (item_id == L1ItemId.Inadril_T_ScrollD) {
			switch (level) {
			case 0:
				if (chance <= 5) {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들었습니다."), true);
					item.setRegistLevel(1);
				} else {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들지 못하였습니다."), true);
				}
				break;
			case 1:
				if (chance <= 4) {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들었습니다."), true);
					item.setRegistLevel(2);
				} else {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들지 못하였습니다."), true);
				}
				break;
			case 2:
				if (chance <= 3) {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들었습니다."), true);
					item.setRegistLevel(3);
				} else {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들지 못하였습니다."), true);
				}
				break;
			case 3:
				if (chance <= 2) {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들었습니다."), true);
					item.setRegistLevel(4);
				} else {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들지 못하였습니다."), true);
				}
				break;
			case 4:
				if (chance <= 1) {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들었습니다."), true);
					item.setRegistLevel(5);
				} else {
					pc.sendPackets(new S_SystemMessage("" + item.getLogName() + "에 속성 저항력이 스며들지 못하였습니다."), true);
				}
				break;
			default:
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				break;
			}

			if (bEquipped) {
				pc.getInventory().setEquipped(item, true);
			}
			pc.getInventory().updateItem(item, L1PcInventory.COL_regist);
			pc.getInventory().saveItem(item, L1PcInventory.COL_regist);
			pc.saveInventory();
		}
	}

	public int RandomELevel(L1ItemInstance item, int itemId) {

		if (item.getItem().getType2() == 2 && item.getItem().get_safeenchant() == 0) {
			if (item.getName().indexOf("가더") > 0)
				return 1;
		}

		int j = _random.nextInt(100) + 1;
		if (itemId == L1ItemId.B_SCROLL_OF_ENCHANT_ARMOR || itemId == L1ItemId.B_SCROLL_OF_ENCHANT_WEAPON || itemId == 1430041
				|| itemId == L1ItemId.Inadril_T_ScrollB) {
			if (item.getEnchantLevel() < 0) {
				if (j < 30) {
					return 2;
				} else {
					return 1;
				}
			} else if (item.getEnchantLevel() <= 2) {
				if (j < 32) {
					return 1;
				} else if (j >= 33 && j <= 76) {
					return 2;
				} else if (j >= 77 && j <= 100) {
					return 3;
				}
			} else if (item.getEnchantLevel() >= 3 && item.getEnchantLevel() <= 5) {
				if (j < 40) { // 감자는 50
					return 2;
				} else {
					return 1;
				}
			}
			return 1;
		} else if (itemId == 140129 || itemId == 140130 || itemId == 1430042) {
			if (item.getEnchantLevel() < 0) {
				if (j < 30) {
					return 2;
				} else {
					return 1;
				}
			} else if (item.getEnchantLevel() <= 2) {
				if (j < 32) {
					return 1;
				} else if (j >= 33 && j <= 60) {
					return 2;
				} else if (j >= 61 && j <= 100) {
					return 3;
				}
			} else if (item.getEnchantLevel() >= 3 && item.getEnchantLevel() <= 5) {
				if (j < 40) {
					return 2;
				} else {
					return 1;
				}
			}
			return 1;
		}
		return 1;
	}

	public void AttrEnchant(L1PcInstance pc, L1ItemInstance item, int item_id) { // 속성4단5단
		int attr_level = item.getAttrEnchantLevel();
		int chance = _random.nextInt(1000) + 1;
		if ((item.getEnchantLevel() < 9) && (attr_level == 3 || attr_level == 8 || attr_level == 13 || attr_level == 18)
				&& !(item.getItemId() == 61 || item.getItemId() == 12 || item.getItemId() == 86 || item.getItemId() == 134 || item.getItemId() == 30082
						|| item.getItemId() == 30080 || item.getItemId() == 30081 || item.getItemId() == 30083 || item.getItemId() == 450044
						|| item.getItemId() == 450045 || item.getItemId() == 450046 || item.getItemId() == 450047 || item.getItemId() == 450048
						|| item.getItemId() == 450049 || item.getItemId() == 450050 || item.getItemId() == 450051 || item.getItemId() == 30110
						|| item.getItemId() == 30111 || item.getItemId() == 30112)) {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
			return;
		} else if (item.getEnchantLevel() < 10 && (attr_level == 4 || attr_level == 9 || attr_level == 14 || attr_level == 19)
				&& !(item.getItemId() == 61 || item.getItemId() == 12 || item.getItemId() == 86 || item.getItemId() == 134 || item.getItemId() == 30082
						|| item.getItemId() == 30080 || item.getItemId() == 30081 || item.getItemId() == 30083 || item.getItemId() == 450044
						|| item.getItemId() == 450045 || item.getItemId() == 450046 || item.getItemId() == 450047 || item.getItemId() == 450048
						|| item.getItemId() == 450049 || item.getItemId() == 450050 || item.getItemId() == 450051 || item.getItemId() == 30110
						|| item.getItemId() == 30111 || item.getItemId() == 30112)) {
			pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																			// 아무것도
																			// 일어나지
																			// 않았습니다.
			return;
		}
		if (item_id == L1ItemId.FIRE_ENCHANT_WEAPON_SCROLL || item_id == 60722) { // 불의 무기 강화 주문서
			if (attr_level == 0 || (attr_level >= 6)) {
				if (attr_level == 9 && attr_level == 10 && attr_level == 14 && attr_level == 15 && attr_level == 19 && attr_level == 20)
					pc.sendPackets(new S_ServerMessage(3297, item.getLogName()), true);
				else if (chance < 300) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(1);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 1) {
				if (chance < 150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(2);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 2) {
				if (chance < 80) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(3);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 3) {
				if (chance < 50) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(4);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 4) {
				if (chance < 30) {
					// pc.sendPackets(new S_ServerMessage(1410,
					// item.getLogName()), true);
					pc.sendPackets(new S_ServerMessage(3296, item.getLogName()), true);
					item.setAttrEnchantLevel(5);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 5) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
				return;
			}
		} else if (item_id == L1ItemId.WATER_ENCHANT_WEAPON_SCROLL || item_id == 60721) { // 물의 무기
			// 강화
			// 주문서
			if ((attr_level >= 0 && attr_level <= 5) || (attr_level >= 11)) {
				if (attr_level == 4 && attr_level == 5 && attr_level == 14 && attr_level == 15 && attr_level == 19 && attr_level == 20)
					pc.sendPackets(new S_ServerMessage(3297, item.getLogName()), true);
				else if (chance < 300) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(6);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 6) {
				if (chance < 150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(7);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 7) {
				if (chance < 80) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(8);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 8) {
				if (chance < 50) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(9);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 9) {
				if (chance < 30) {
					// pc.sendPackets(new S_ServerMessage(1410,
					// item.getLogName()), true);
					pc.sendPackets(new S_ServerMessage(3296, item.getLogName()), true);
					item.setAttrEnchantLevel(10);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 10) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
				return;
			}
		} else if (item_id == L1ItemId.WIND_ENCHANT_WEAPON_SCROLL || item_id == 60719) { // 바람의 무기
			// 강화
			// 주문서
			if ((attr_level >= 0 && attr_level <= 10) || (attr_level >= 16)) {
				if (attr_level == 4 && attr_level == 5 && attr_level == 9 && attr_level == 10 && attr_level == 19 && attr_level == 20)
					pc.sendPackets(new S_ServerMessage(3297, item.getLogName()), true);
				else if (chance < 300) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(11);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 11) {
				if (chance < 150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(12);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 12) {
				if (chance < 80) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(13);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 13) {
				if (chance < 50) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(14);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 14) {
				if (chance < 30) {
					// pc.sendPackets(new S_ServerMessage(1410,
					// item.getLogName()), true);
					pc.sendPackets(new S_ServerMessage(3296, item.getLogName()), true);
					item.setAttrEnchantLevel(15);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 15) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
				return;
			}
		} else if (item_id == L1ItemId.EARTH_ENCHANT_WEAPON_SCROLL || item_id == 60720) { // 땅의 무기
			// 강화
			// 주문서
			if (attr_level <= 15) {
				if (attr_level == 4 && attr_level == 5 && attr_level == 9 && attr_level == 10 && attr_level == 14 && attr_level == 15)
					pc.sendPackets(new S_ServerMessage(3297, item.getLogName()), true);
				else if (chance < 300) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(16);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 16) {
				if (chance < 150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(17);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 17) {
				if (chance < 80) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(18);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 18) {
				if (chance < 50) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(19);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 19) {
				if (chance < 30) {
					// pc.sendPackets(new S_ServerMessage(1410,
					// item.getLogName()), true);
					pc.sendPackets(new S_ServerMessage(3296, item.getLogName()), true);
					item.setAttrEnchantLevel(20);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 20) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
				return;
			}

		} else if (item_id == L1ItemId.FIRE_ENCHANT_WEAPON_SCROLL_100) { // 불의
																			// 무기
																			// 강화
																			// 주문서
			if (attr_level == 0 || (attr_level >= 6)) {
				if (attr_level == 9 && attr_level == 10 && attr_level == 14 && attr_level == 15 && attr_level == 19 && attr_level == 20)
					pc.sendPackets(new S_ServerMessage(3297, item.getLogName()), true);
				else if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(1);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 1) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(2);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 2) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(3);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 3) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(4);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 4) {
				if (chance < 1150) {
					// pc.sendPackets(new S_ServerMessage(1410,
					// item.getLogName()), true);
					pc.sendPackets(new S_ServerMessage(3296, item.getLogName()), true);
					item.setAttrEnchantLevel(5);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 5) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
				return;
			}
		} else if (item_id == L1ItemId.WATER_ENCHANT_WEAPON_SCROLL_100) { // 물의
																			// 무기
																			// 강화
																			// 주문서
			if ((attr_level >= 0 && attr_level <= 5) || (attr_level >= 11)) {
				if (attr_level == 4 && attr_level == 5 && attr_level == 14 && attr_level == 15 && attr_level == 19 && attr_level == 20)
					pc.sendPackets(new S_ServerMessage(3297, item.getLogName()), true);
				else if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(6);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 6) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(7);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 7) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(8);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 8) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(9);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 9) {
				if (chance < 1150) {
					// pc.sendPackets(new S_ServerMessage(1410,
					// item.getLogName()), true);
					pc.sendPackets(new S_ServerMessage(3296, item.getLogName()), true);
					item.setAttrEnchantLevel(10);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 10) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
				return;
			}
		} else if (item_id == L1ItemId.WIND_ENCHANT_WEAPON_SCROLL_100) { // 바람의
																			// 무기
																			// 강화
																			// 주문서
			if ((attr_level >= 0 && attr_level <= 10) || (attr_level >= 16)) {
				if (attr_level == 4 && attr_level == 5 && attr_level == 9 && attr_level == 10 && attr_level == 19 && attr_level == 20)
					pc.sendPackets(new S_ServerMessage(3297, item.getLogName()), true);
				else if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(11);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 11) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(12);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 12) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(13);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 13) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(14);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 14) {
				if (chance < 1150) {
					// pc.sendPackets(new S_ServerMessage(1410,
					// item.getLogName()), true);
					pc.sendPackets(new S_ServerMessage(3296, item.getLogName()), true);
					item.setAttrEnchantLevel(15);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 15) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
				return;
			}
		} else if (item_id == L1ItemId.EARTH_ENCHANT_WEAPON_SCROLL_100) { // 땅의
																			// 무기
																			// 강화
																			// 주문서
			if (attr_level <= 15) {
				if (attr_level == 4 && attr_level == 5 && attr_level == 9 && attr_level == 10 && attr_level == 14 && attr_level == 15)
					pc.sendPackets(new S_ServerMessage(3297, item.getLogName()), true);
				else if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(16);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 16) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(17);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 17) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(18);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 18) {
				if (chance < 1150) {
					pc.sendPackets(new S_ServerMessage(1410, item.getLogName()), true);
					item.setAttrEnchantLevel(19);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 19) {
				if (chance < 1150) {
					// pc.sendPackets(new S_ServerMessage(1410,
					// item.getLogName()), true);
					pc.sendPackets(new S_ServerMessage(3296, item.getLogName()), true);
					item.setAttrEnchantLevel(20);
					pc.sendPackets(new S_PacketBox(item, 172), true);
				} else {
					pc.sendPackets(new S_ServerMessage(1411, item.getLogName()), true);
				}
			} else if (attr_level == 20) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true); // \f1
																				// 아무것도
																				// 일어나지
																				// 않았습니다.
				return;
			}
		}

		pc.getInventory().consumeItem(item_id, 1);
		pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		pc.saveInventory();

	}

	public void StepEnchant(L1PcInstance pc, L1ItemInstance item, int item_id) { // <<검색
		int step_level = item.getStepEnchantLevel();
		int chance = _random.nextInt(100) + 1;
		boolean bEquipped = false;

		if (item.isEquipped()) {
			pc.getInventory().setEquipped(item, false);
			bEquipped = true;
		}
		if (item_id == L1ItemId.Add_ENCHANT_WEAPON_SCROLL) {
			{
				if (step_level == 0) {
					if (chance < 20) {
						item.setStepEnchantLevel(1);
						pc.sendPackets(new S_SystemMessage("\\fW무기 1단계 강화  업그레이드 성공!"), true);
					} else {
						pc.sendPackets(new S_SystemMessage("\\fY무기 1단계 강화  업그레이드 실패!"), true);
					}
				} else if (step_level == 1) {
					if (chance < 10) {
						item.setStepEnchantLevel(2);
						pc.sendPackets(new S_SystemMessage("\\fW무기 2단계 강화  업그레이드 성공!"), true);
					} else {
						item.setStepEnchantLevel(1);
						pc.sendPackets(new S_SystemMessage("\\fY무기 2단계 강화  업그레이드 실패!"), true);
					}
				} else if (step_level == 2) {
					if (chance < 5) {
						item.setStepEnchantLevel(3);
						pc.sendPackets(new S_SystemMessage("\\fW무기 3단계 강화  업그레이드 성공!"), true);
					} else {
						item.setStepEnchantLevel(2);
						pc.sendPackets(new S_SystemMessage("\\fY무기 3단계 강화  업그레이드 실패!"), true);
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
					return;
				}
			}
		}

		if (item_id == L1ItemId.Add_ENCHANT_WEAPON_SCROLL_100) {// 특화 강화줌서100%
			{
				if (step_level == 0) {
					// if (chance < 100) {
					item.setStepEnchantLevel(1);
					pc.sendPackets(new S_SystemMessage("\\fW무기 1단계 강화  업그레이드 성공!"), true);
					// }
				} else if (step_level == 1) {
					// if (chance < 100) {
					item.setStepEnchantLevel(2);
					pc.sendPackets(new S_SystemMessage("\\fW무기 2단계 강화  업그레이드 성공!"), true);
					// }
				} else if (step_level == 2) {
					// if (chance < 100) {
					item.setStepEnchantLevel(3);
					pc.sendPackets(new S_SystemMessage("\\fW무기 3단계 강화  업그레이드 성공!"), true);
					// }
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
					return;
				}

			}
		}

		if (bEquipped) {
			pc.getInventory().setEquipped(item, true);
		}
		pc.getInventory().consumeItem(item_id, 1);
		pc.getInventory().updateItem(item, L1PcInventory.COL_STEPENCHANTLVL);
		pc.getInventory().saveItem(item, L1PcInventory.COL_STEPENCHANTLVL);
		pc.saveInventory();

	}

	public void DragonArmorEnchant(L1PcInstance pc, L1ItemInstance item, int item_id) {
		int attr_level = item.getAttrEnchantLevel();
		// int safe_enchant = item.getItem().get_safeenchant();
		int chance = _random.nextInt(100) + 1;
		boolean bEquipped = false;

		if (item.isEquipped()) {
			pc.getInventory().setEquipped(item, false);
			bEquipped = true;
		}

		if (item_id == 5000138) {
			if (attr_level == 0) {
				if (chance < 20) {
					item.setAttrEnchantLevel(41);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 41) {
				if (chance < 20) {
					item.setAttrEnchantLevel(42);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 41) {
				if (chance < 20) {
					item.setAttrEnchantLevel(42);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 42) {
				if (chance < 20) {
					item.setAttrEnchantLevel(43);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 43) {
				if (chance < 20) {
					item.setAttrEnchantLevel(44);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 44) {
				if (chance < 20) {
					item.setAttrEnchantLevel(45);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 45) {
				if (chance < 20) {
					item.setAttrEnchantLevel(46);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 46) {
				if (chance < 20) {
					item.setAttrEnchantLevel(47);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 47) {
				if (chance < 20) {
					item.setAttrEnchantLevel(48);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 48) {
				if (chance < 20) {
					item.setAttrEnchantLevel(49);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 49) {
				if (chance < 20) {
					item.setAttrEnchantLevel(50);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 50) {
				if (chance < 20) {
					item.setAttrEnchantLevel(51);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 51) {
				if (chance < 20) {
					item.setAttrEnchantLevel(52);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 52) {
				if (chance < 20) {
					item.setAttrEnchantLevel(53);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 53) {
				if (chance < 20) {
					item.setAttrEnchantLevel(54);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 54) {
				if (chance < 20) {
					item.setAttrEnchantLevel(55);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 55) {
				if (chance < 20) {
					item.setAttrEnchantLevel(56);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 56) {
				if (chance < 20) {
					item.setAttrEnchantLevel(57);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 57) {
				if (chance < 20) {
					item.setAttrEnchantLevel(58);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 58) {
				if (chance < 20) {
					item.setAttrEnchantLevel(59);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 59) {
				if (chance < 20) {
					item.setAttrEnchantLevel(60);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 60) {
				if (chance < 20) {
					item.setAttrEnchantLevel(61);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 61) {
				if (chance < 20) {
					item.setAttrEnchantLevel(62);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 62) {
				if (chance < 20) {
					item.setAttrEnchantLevel(63);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 63) {
				if (chance < 20) {
					item.setAttrEnchantLevel(64);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 64) {
				if (chance < 20) {
					item.setAttrEnchantLevel(65);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 65) {
				if (chance < 20) {
					item.setAttrEnchantLevel(66);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 66) {
				if (chance < 20) {
					item.setAttrEnchantLevel(67);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 67) {
				if (chance < 20) {
					item.setAttrEnchantLevel(68);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 68) {
				if (chance < 20) {
					item.setAttrEnchantLevel(69);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else if (attr_level == 69) {
				if (chance < 20) {
					item.setAttrEnchantLevel(70);
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 성공!!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("데미지 리덕션 업그레이드 실패!!"), true);
				}
			} else

			{
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		}

		if (item_id == 5000139) {
			if (attr_level == 0) {
				if (chance < 20) {
					item.setAttrEnchantLevel(24);
					pc.sendPackets(new S_SystemMessage("\\fW수룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY수룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else if (attr_level == 24) {
				if (chance < 10) {
					item.setAttrEnchantLevel(25);
					pc.sendPackets(new S_SystemMessage("\\fW수룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY수룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else if (attr_level == 25) {
				if (chance < 5) {
					item.setAttrEnchantLevel(26);
					pc.sendPackets(new S_SystemMessage("\\fW수룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY수룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		}

		if (item_id == 5000140) {
			if (attr_level == 0) {
				if (chance < 20) {
					item.setAttrEnchantLevel(27);
					pc.sendPackets(new S_SystemMessage("\\fW풍룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY풍룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else if (attr_level == 27) {
				if (chance < 10) {
					item.setAttrEnchantLevel(28);
					pc.sendPackets(new S_SystemMessage("\\fW풍룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY풍룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else if (attr_level == 28) {
				if (chance < 5) {
					item.setAttrEnchantLevel(29);
					pc.sendPackets(new S_SystemMessage("\\fW풍룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY풍룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		}

		if (item_id == 5000141) {
			if (attr_level == 0) {
				if (chance < 20) {
					item.setAttrEnchantLevel(30);
					pc.sendPackets(new S_SystemMessage("\\fW화룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY화룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else if (attr_level == 30) {
				if (chance < 10) {
					item.setAttrEnchantLevel(31);
					pc.sendPackets(new S_SystemMessage("\\fW화룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY화룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else if (attr_level == 31) {
				if (chance < 5) {
					item.setAttrEnchantLevel(32);
					pc.sendPackets(new S_SystemMessage("\\fW화룡의 갑옷 강화  업그레이드 성공!"), true);
				} else {
					pc.sendPackets(new S_SystemMessage("\\fY화룡의 갑옷 강화  업그레이드 실패!"), true);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
		}

		if (bEquipped) {
			pc.getInventory().setEquipped(item, true);
		}
		pc.getInventory().consumeItem(item_id, 1);
		pc.getInventory().updateItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		pc.getInventory().updateItem(item, L1PcInventory.COL_IS_ID);
		pc.getInventory().saveItem(item, L1PcInventory.COL_ATTRENCHANTLVL);
		pc.getInventory().saveItem(item, L1PcInventory.COL_IS_ID);
		pc.saveInventory();
	}
}
