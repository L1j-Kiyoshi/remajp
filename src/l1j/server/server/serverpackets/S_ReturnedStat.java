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
package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

import javolution.util.FastTable;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1StatReset;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1BookMark;

public class S_ReturnedStat extends ServerBasePacket {

	private static final String S_ReturnedStat = "[S] S_ReturnedStat";
	private byte[] _byte = null;

	public static final int START = 1;

	public static final int LEVELUP = 2;

	public static final int END = 3;

	public static final int LOGIN = 4;

	public static final int POWER_BOOK = 0x19;

	public static final int BOOKMARK = 0x2A;

	public static final int CLAN_JOIN_LEAVE = 0x3C;

	public static final int RING_RUNE_SLOT = 0x43;
	public static final int RING_RUNE_SLOT_CLOSE = 0x44;
	public static final int Unknown_LOGIN2 = 0x44;
	public static final int LOGIN_EQUIP = 0x41;

	public static final int Pet_hp_Window = 0x0C;

	public S_ReturnedStat(L1PcInstance pc, int type) {
		buildPacket(pc, type);
	}

	public S_ReturnedStat(L1StatReset sr) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(LEVELUP);
		writeC(sr.getNowLevel());
		writeC(sr.getEndLevel());
		writeH(sr.getMaxHp());
		writeH(sr.getMaxMp());
		writeH(sr.getAC());
		writeC(sr.getStr());
		writeC(sr.getIntel());
		writeC(sr.getWis());
		writeC(sr.getDex());
		writeC(sr.getCon());
		writeC(sr.getCha());
	}

	public S_ReturnedStat(L1PcInstance pc, int type, int value) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);
		switch (type) {
		case RING_RUNE_SLOT:
			writeD(value);
			if (value == 1) {
				writeD(3);
			} else if (value == 2) {
				writeD(1);
			}
			writeD(0x00);
			writeD(0x00);
			writeD(0x00);
			writeH(0x00);
			writeC(0x00);
			break;
		case Unknown_LOGIN2:
			writeD(1);
			writeC(0x1c);
			writeH(0);
			break;
		}
	}

	public static final int SUBTYPE_RING = 1;
	public static final int SUBTYPE_RUNE = 2;

	public S_ReturnedStat(int type, int subType, int value) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);
		switch (type) {
		case RING_RUNE_SLOT:
			writeD(subType);
			if (subType == SUBTYPE_RING) { // 반지 슬롯
				if (value == 2)
					value = 15;
				else if (value == 1)
					value = 7;
				else if (value == 0)
					value = 3;
				writeC(value);
			} else if (subType == SUBTYPE_RUNE) { // 룬 슬롯
				writeC(1); // 1~3
			}
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeH(0x00);
			break;
		case Unknown_LOGIN2:
			// 0000: fe 44 01 00 00 00 1c 36 66 .D.....6f

			writeD(0x01);
			writeC(0x1c);
			writeH(0);
			// writeH(0x00);
			// writeC(0x00);
			break;
		}
	}

	public S_ReturnedStat(int type, byte[] data) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);
		switch (type) {
		case POWER_BOOK:
			writeC(0x00);
			String s = "c8 cf 6c 69 b5 a8 c2 4d a0 88 f5 ae c1 3d ba 98";
			StringTokenizer st = new StringTokenizer(s);
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}
			writeByte(data);
			break;
		default:
			String s2 = "28 00 00 00 01 00 01 03 28 00 00 00 00 00 "
					+ "00 00 00 00 00 00 6d 7d d2 4a 00 00 00 00 04 13 "
					+ "85 00 04 13 85 00 00 00 00 00 00 00 00 00";
			StringTokenizer st2 = new StringTokenizer(s2);
			while (st2.hasMoreTokens()) {
				writeC(Integer.parseInt(st2.nextToken(), 16));
			}

			break;
		}
	}

	/**
	 * [Server] opcode = 43 0000: 2b /02/ 01 2d/ 0f 00/ 04 00/ 0a 00 /0c 0c 0c
	 * 0c 12 09 +..-............
	 */

	private void buildPacket(L1PcInstance pc, int type) {
		writeC(Opcodes.S_VOICE_CHAT);
		writeC(type);
		switch (type) {
		case START:
			short init_hp = 0;
			short init_mp = 0;
			if (pc.isCrown()) { // CROWN
				init_hp = 14;
				switch (pc.getAbility().getBaseWis()) {
				case 11:
					init_mp = 2;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 3;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 4;
					break;
				default:
					init_mp = 2;
					break;
				}
			} else if (pc.isKnight()) { // KNIGHT
				init_hp = 16;
				switch (pc.getAbility().getBaseWis()) {
				case 9:
				case 10:
				case 11:
					init_mp = 1;
					break;
				case 12:
				case 13:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			} else if (pc.isElf()) { // ELF
				init_hp = 15;
				switch (pc.getAbility().getBaseWis()) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 4;
					break;
				}
			} else if (pc.isWizard()) { // WIZ
				init_hp = 12;
				switch (pc.getAbility().getBaseWis()) {
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 6;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 8;
					break;
				default:
					init_mp = 6;
					break;
				}
			} else if (pc.isDarkelf()) { // DE
				init_hp = 12;
				switch (pc.getAbility().getBaseWis()) {
				case 10:
				case 11:
					init_mp = 3;
					break;
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 4;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 3;
					break;
				}
			} else if (pc.isDragonknight()) { // 용기사
				init_hp = 16;
				init_mp = 2;
			} else if (pc.isWarrior()) { // 전사
				init_hp = 16;
				if (pc.getAbility().getBaseCon() >= 17) {
					init_hp += 1;
				}
				if (pc.getAbility().getBaseCon() >= 19) {
					init_hp += 2;
				}
				init_mp = 1;
			} else if (pc.isIllusionist()) { // 환술사
				init_hp = 14;
				switch (pc.getAbility().getBaseWis()) {
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					init_mp = 5;
					break;
				case 16:
				case 17:
				case 18:
					init_mp = 6;
					break;
				default:
					init_mp = 5;
					break;
				}
			} else if (pc.isFencer()) { // 검사
				init_hp = 16;
				switch (pc.getAbility().getBaseWis()) {
				case 11:
				case 12:
				case 13:
				case 14:
					init_mp = 1;
					break;
				case 15:
					init_mp = 2;
					break;
				default:
					init_mp = 1;
					break;
				}
			}
			writeH(init_hp);
			writeH(init_mp);
			writeC(0x0a);
			writeC(ExpTable.getLevelByExp(pc.getReturnStat()));
			break;
		case LEVELUP:
			writeC(pc.getLevel());
			writeC(ExpTable.getLevelByExp(pc.getReturnStat()));
			writeH(pc.getBaseMaxHp());
			writeH(pc.getBaseMaxMp());
			writeH(pc.getBaseAc());
			writeC(pc.getAbility().getStr());
			writeC(pc.getAbility().getInt());
			writeC(pc.getAbility().getWis());
			writeC(pc.getAbility().getDex());
			writeC(pc.getAbility().getCon());
			writeC(pc.getAbility().getCha());
			if (pc.getLevel() > pc.getHighLevel()) {
				pc.sendPackets(new S_SystemMessage("병신아 꺼져"));
				pc.getNetConnection().kick();
				pc.getNetConnection().close();
			}
			break;
		case END:
			writeC(pc.getAbility().getElixirCount());
			if (pc.getLevel() > pc.getHighLevel()) {
				pc.sendPackets(new S_SystemMessage("병신아 꺼져"));
				pc.getNetConnection().kick();
				pc.getNetConnection().close();
			}
			break;
		case LOGIN:
			/*
			 * pc.getAblilyty에서 반환되는 최소 스탯값 배열 순서 0:힘/1:덱/2:콘/3:위즈/4:카리/5:인트
			 */
			int minStat[] = new int[6];
			minStat = pc.getAbility().getMinStat(pc.getClassId());
			int first = minStat[0] + minStat[5] * 16;
			int second = minStat[3] + minStat[1] * 16;
			int third = minStat[2] + minStat[4] * 16;
			// System.out.println(first + "--" + second + "--" + third );
			writeC(first); // int,str
			writeC(second); // dex,wis
			writeC(third); // cha,con
			writeC(0x00);
			break;
		case BOOKMARK:
			writeC(0x80);
			writeC(0x00);
			if (pc.getBookMarkSize() <= 0) {
				for (int i = 0; i < 128; i++) {
					writeC(0x00);
				}
				writeH(pc.getBookmarkMax());
				writeH(pc.getBookMarkSize());
			} else {
				writeC(2);
				L1BookMark[] list = pc.getBookMark();
				for (int i = 0; i < 127; i++) {
					if (list.length <= i) {
						int count = 0;
						for (int a = 0; a < list.length; a++) {
							if (list[a].get_fast() == 1) {
								writeC(a);
								count++;
							}
						}
						for (int fd = (list.length + count); fd < 127; fd++) {
							writeC(0xFF);
						}
						break;
					}
					writeC(i);
				}
				writeH(pc.getBookmarkMax());
				writeH(pc.getBookMarkSize());
				for (L1BookMark book : list) {
					writeD(book.getId());
					writeS(book.getName());
					writeH(book.getMapId());
					writeH(book.getLocX());
					writeH(book.getLocY());
				}
			}
			break;
		case LOGIN_EQUIP:
			FastTable<L1ItemInstance> eqlist = new FastTable<L1ItemInstance>();
			pc.getInventory().getSize();
			for (L1ItemInstance item : pc.getInventory().getItems()) {
				if (item.isEquipped())
					eqlist.add(item);
			}
			int size = eqlist.size();
			writeC(size);
			if (size > 0) {
				for (L1ItemInstance item : eqlist) {
					writeD(item.getId());
					if (pc.getSecondWeapon() != null) {
						if (pc.getSecondWeapon().equals(item)) {
							writeD(8);
						} else if (item.getItem().getType() == 12) {
							writeD(L1PcInventory.changeval(item.getItem()
									.getType2(), item.getRSN() == 13 ? 0 : 1,
									item.getItem().getType(), item.getItem()
											.getType1()));
						} else {
							writeD(L1PcInventory.changeval(
									item.getItem().getType2(),
									item.getItem().getType() == 9 ? item
											.getRSN() - 19 : 0, item.getItem()
											.getType(), item.getItem()
											.getType1()));
						}
					} else if (item.getItem().getType() == 12) {
						writeD(L1PcInventory
								.changeval(item.getItem().getType2(), item
										.getRSN() == 13 ? 0 : 1, item.getItem()
										.getType(), item.getItem().getType1()));
					} else
						writeD(L1PcInventory
								.changeval(
										item.getItem().getType2(),
										item.getItem().getType() == 9 ? item
												.getRSN() - 19 : 0, item
												.getItem().getType(), item
												.getItem().getType1()));
				}
			}
			writeH(0x00);
			break;

		case CLAN_JOIN_LEAVE:
			writeD(pc.getId());
			writeD(pc.getClanid());
			break;
		/*
		 * case 0x0F: String s2 = "20 00 00 00 52 38 c1 db 20 00 00 00 20 00 "+
		 * "00 00 00 00 00 00 04 15 f0 83 00 00 00 00 4c 4c "+
		 * "4c 96 f2 69 13 96"; StringTokenizer st2 = new StringTokenizer(s2);
		 * while(st2.hasMoreTokens()){ writeC(Integer.parseInt(st2.nextToken(),
		 * 16)); } break;
		 */
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_ReturnedStat;
	}
}