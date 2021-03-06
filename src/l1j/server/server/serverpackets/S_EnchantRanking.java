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
package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_EnchantRanking extends ServerBasePacket {

	private static final String S_EnchantRanking = "[S] S_EnchantRanking";

	private static Logger _log = Logger.getLogger(S_EnchantRanking.class
			.getName());

	private byte[] _byte = null;
	static String[] name;
	static String[] name1;
	static String[] castlename;
	static String[] clanname;
	static String[] leadername;
	static int[] enchantlvl;
	static int[] aden;
	static int[] aden2;
	static int[] armor;
	static int[] level;
	static int[] Ac;
	static int[] priaden;
	static int[] castleid;
	static int[] hascastle;
	static int[] taxrate;
	static int[] castleaden;

	public S_EnchantRanking(L1PcInstance pc, int number) {
		name = new String[10];
		name1 = new String[10];
		enchantlvl = new int[10];
		aden = new int[10];
		aden2 = new int[10];
		armor = new int[10];
		level = new int[10];
		Ac = new int[10];
		priaden = new int[10];
		castlename = new String[10];
		clanname = new String[10];
		leadername = new String[10];
		castleid = new int[10];
		hascastle = new int[10];
		taxrate = new int[10];
		castleaden = new int[10];
		buildPacket(pc, number);
	}

	public S_EnchantRanking(L1NpcInstance board) {
		buildPacket(board);
	}

	private void buildPacket(L1NpcInstance board) {
		int count = 0;
		String[][] db = null;
		int[] id = null;
		db = new String[8][3];
		id = new int[8];
		while (count < 8) {
			id[count] = count + 1;
			db[count][0] = "????????";
			db[count][1] = "";
			count++;
		}
		db[0][2] = "1. ???? ????";
		db[1][2] = "2. ?????? ????";
		db[2][2] = "3. ???? ????";
		db[3][2] = "4. ???? ????";
		db[4][2] = "5. ???????? ????";
		db[5][2] = "6. ???????? ????";
		db[6][2] = "7. ???? ????";
		db[7][2] = "8. ????-Ac ????";

		writeC(Opcodes.S_BOARD_LIST);
		writeC(0x00);
		writeD(board.getId());
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0x7F); // ?
		writeH(8);
		writeH(300);
		for (int i = 0; i < 8; ++i) {
			writeD(id[i]);
			writeS(db[i][0]);
			writeS(db[i][1]);
			writeS(db[i][2]);
		}
		db = null;
		id = null;
	}

	private void buildPacket(L1PcInstance pc, int number) {
		String date = time();
		String title = null;
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		writeS("??????");
		switch (number) {
		case 1:
			title = "???? ????";
			break;
		case 2:
			title = "?????? ????";
			break;
		case 3:
			title = "???? ????";
			break;
		case 4:
			title = "???? ????";
			break;
		case 5:
			title = "???????? ????";
			break;
		case 6:
			title = "???????? ????";
			break;
		case 7:
			title = "???? ????";
			break;
		case 8:
			title = "????-Ac ????";
			break;
		}
		writeS(title);
		writeS(date);
		Rank(pc, number);
		if (number == 1) { // ??????????????
			writeS("\n\r" + "  1?? " + "+" + enchantlvl[0] + " " + name[0]
					+ "\n\r" + "  ?????? : " + name1[0] + "\n\r" + "  2?? " + "+"
					+ enchantlvl[1] + " " + name[1] + "\n\r" + "  ?????? : "
					+ name1[1] + "\n\r" + "  3?? " + "+" + enchantlvl[2] + " "
					+ name[2] + "\n\r" + "  ?????? : " + name1[2] + "\n\r"
					+ "  4?? " + "+" + enchantlvl[3] + " " + name[3] + "\n\r"
					+ "  ?????? : " + name1[3] + "\n\r" + "  5?? " + "+"
					+ enchantlvl[4] + " " + name[4] + "\n\r" + "  ?????? : "
					+ name1[4] + "\n\r" + "  6?? " + "+" + enchantlvl[5] + " "
					+ name[5] + "\n\r" + "  ?????? : " + name1[5] + "\n\r"
					+ "  7?? " + "+" + enchantlvl[6] + " " + name[6] + "\n\r"
					+ "  ?????? : " + name1[6] + "\n\r" + "  8?? " + "+"
					+ enchantlvl[7] + " " + name[7] + "\n\r" + "  ?????? : "
					+ name1[7] + "\n\r" + "  9?? " + "+" + enchantlvl[8] + " "
					+ name[8] + "\n\r" + "  ?????? : " + name1[8] + "\n\r"
					+ " 10?? " + "+" + enchantlvl[9] + " " + name[9] + "\n\r"
					+ "  ?????? : " + name1[9] + "\n\r" + "      ");
		} else if (number == 2) { // ??????????????
			writeS("\n\r" + "  1?? " + "+" + armor[0] + " " + name[0] + "\n\r"
					+ "  ?????? : " + name1[0] + "\n\r" + "  2?? " + "+" + armor[1]
					+ " " + name[1] + "\n\r" + "  ?????? : " + name1[1] + "\n\r"
					+ "  3?? " + "+" + armor[2] + " " + name[2] + "\n\r"
					+ "  ?????? : " + name1[2] + "\n\r" + "  4?? " + "+" + armor[3]
					+ " " + name[3] + "\n\r" + "  ?????? : " + name1[3] + "\n\r"
					+ "  5?? " + "+" + armor[4] + " " + name[4] + "\n\r"
					+ "  ?????? : " + name1[4] + "\n\r" + "  6?? " + "+" + armor[5]
					+ " " + name[5] + "\n\r" + "  ?????? : " + name1[5] + "\n\r"
					+ "  7?? " + "+" + armor[6] + " " + name[6] + "\n\r"
					+ "  ?????? : " + name1[6] + "\n\r" + "  8?? " + "+" + armor[7]
					+ " " + name[7] + "\n\r" + "  ?????? : " + name1[7] + "\n\r"
					+ "  9?? " + "+" + armor[8] + " " + name[8] + "\n\r"
					+ "  ?????? : " + name1[8] + "\n\r" + " 10?? " + "+" + armor[9]
					+ " " + name[9] + "\n\r" + "  ?????? : " + name1[9] + "\n\r"
					+ "      ");
		} else if (number == 3) { // ??????????????
			writeS("\n\r" + "  1?? " + "$ " + aden[0] + " ??????\n\r" + "  ?????? : "
					+ name[0] + "\n\r" + "  2?? " + "$ " + aden[1] + " ??????\n\r"
					+ "  ?????? : " + name[1] + "\n\r" + "  3?? " + "$ " + aden[2]
					+ " ??????\n\r" + "  ?????? : " + name[2] + "\n\r" + "  4?? "
					+ "$ " + aden[3] + " ??????\n\r" + "  ?????? : " + name[3]
					+ "\n\r" + "  5?? " + "$ " + aden[4] + " ??????\n\r"
					+ "  ?????? : " + name[4] + "\n\r" + "  6?? " + "$ " + aden[5]
					+ " ??????\n\r" + "  ?????? : " + name[5] + "\n\r" + "  7?? "
					+ "$ " + aden[6] + " ??????\n\r" + "  ?????? : " + name[6]
					+ "\n\r" + "  8?? " + "$ " + aden[7] + " ??????\n\r"
					+ "  ?????? : " + name[7] + "\n\r" + "  9?? " + "$ " + aden[8]
					+ " ??????\n\r" + "  ?????? : " + name[8] + "\n\r" + " 10?? "
					+ "$ " + aden[9] + " ??????\n\r" + "  ?????? : " + name[9]
					+ "\n\r" + "      ");
		} else if (number == 4) { // ??????????????
			writeS("\n\r" + "  1?? " + name[0] + " \n\r" + "  ???????? : "
					+ level[0] + "\n\r" + "  2?? " + name[1] + " \n\r"
					+ "  ???????? : " + level[1] + "\n\r" + "  3?? " + name[2]
					+ " \n\r" + "  ???????? : " + level[2] + "\n\r" + "  4?? "
					+ name[3] + " \n\r" + "  ???????? : " + level[3] + "\n\r"
					+ "  5?? " + name[4] + " \n\r" + "  ???????? : " + level[4]
					+ "\n\r" + "  6?? " + name[5] + " \n\r" + "  ???????? : "
					+ level[5] + "\n\r" + "  7?? " + name[6] + " \n\r"
					+ "  ???????? : " + level[6] + "\n\r" + "  8?? " + name[7]
					+ " \n\r" + "  ???????? : " + level[7] + "\n\r" + "  9?? "
					+ name[8] + " \n\r" + "  ???????? : " + level[8] + "\n\r"
					+ " 10?? " + name[9] + " \n\r" + "  ???????? : " + level[9]
					+ "\n\r" + "      ");
		} else if (number == 5) { // ??????????????
			writeS("\n\r" + "  1?? " + priaden[0] + "???? ????\n\r" + "  ?????? : "
					+ name[0] + "\n\r" + "  2?? " + priaden[1] + "???? ????\n\r"
					+ "  ?????? : " + name[1] + "\n\r" + "  3?? " + priaden[2]
					+ "???? ????\n\r" + "  ?????? : " + name[2] + "\n\r" + "  4?? "
					+ priaden[3] + "???? ????\n\r" + "  ?????? : " + name[3] + "\n\r"
					+ "  5?? " + priaden[4] + "???? ????\n\r" + "  ?????? : " + name[4]
					+ "\n\r" + "  6?? " + priaden[5] + "???? ????\n\r" + "  ?????? : "
					+ name[5] + "\n\r" + "  7?? " + priaden[6] + "???? ????\n\r"
					+ "  ?????? : " + name[6] + "\n\r" + "  8?? " + priaden[7]
					+ "???? ????\n\r" + "  ?????? : " + name[7] + "\n\r" + "  9?? "
					+ priaden[8] + "???? ????\n\r" + "  ?????? : " + name[8] + "\n\r"
					+ " 10?? " + priaden[9] + "???? ????\n\r" + "  ?????? : " + name[9]
					+ "\n\r" + "      ");
		} else if (number == 6) { // ??????????????
			writeS("\n\r" + "  1?? " + "$ " + aden2[0] + " ??????\n\r" + "  ?????? : "
					+ name[0] + "\n\r" + "  2?? " + "$ " + aden2[1] + " ??????\n\r"
					+ "  ?????? : " + name[1] + "\n\r" + "  3?? " + "$ " + aden2[2]
					+ " ??????\n\r" + "  ?????? : " + name[2] + "\n\r" + "  4?? "
					+ "$ " + aden2[3] + " ??????\n\r" + "  ?????? : " + name[3]
					+ "\n\r" + "  5?? " + "$ " + aden2[4] + " ??????\n\r"
					+ "  ?????? : " + name[4] + "\n\r" + "  6?? " + "$ " + aden2[5]
					+ " ??????\n\r" + "  ?????? : " + name[5] + "\n\r" + "  7?? "
					+ "$ " + aden2[6] + " ??????\n\r" + "  ?????? : " + name[6]
					+ "\n\r" + "  8?? " + "$ " + aden2[7] + " ??????\n\r"
					+ "  ?????? : " + name[7] + "\n\r" + "  9?? " + "$ " + aden2[8]
					+ " ??????\n\r" + "  ?????? : " + name[8] + "\n\r" + " 10?? "
					+ "$ " + aden2[9] + " ??????\n\r" + "  ?????? : " + name[9]
					+ "\n\r" + "      ");
		} else if (number == 7) { // ??????????????
			writeS("\n\r" + "  1?? ???? : " + priaden[0] + " ??\n\r" + "  ?????? : "
					+ name[0] + "\n\r" + "  2?? ???? : " + priaden[1] + " ??\n\r"
					+ "  ?????? : " + name[1] + "\n\r" + "  3?? ???? : " + priaden[2]
					+ " ??\n\r" + "  ?????? : " + name[2] + "\n\r" + "  4?? ???? : "
					+ priaden[3] + " ??\n\r" + "  ?????? : " + name[3] + "\n\r"
					+ "  5?? ???? : " + priaden[4] + " ??\n\r" + "  ?????? : "
					+ name[4] + "\n\r" + "  6?? ???? : " + priaden[5] + " ??\n\r"
					+ "  ?????? : " + name[5] + "\n\r" + "  7?? ???? : " + priaden[6]
					+ " ??\n\r" + "  ?????? : " + name[6] + "\n\r" + "  8?? ???? : "
					+ priaden[7] + " ??\n\r" + "  ?????? : " + name[7] + "\n\r"
					+ "  9?? ???? : " + priaden[8] + " ??\n\r" + "  ?????? : "
					+ name[8] + "\n\r" + " 10?? ???? : " + priaden[9] + " ??\n\r"
					+ "  ?????? : " + name[9] + "\n\r" + "      ");

		} else if (number == 8) { // ??????????????
			writeS("\n\r" + "  1?? " + name[0] + " \n\r" + "  ???????? Ac : "
					+ Ac[0] + "\n\r" + "  2?? " + name[1] + " \n\r"
					+ "  ???????? Ac : " + Ac[1] + "\n\r" + "  3?? " + name[2]
					+ " \n\r" + "  ???????? Ac : " + Ac[2] + "\n\r" + "  4?? "
					+ name[3] + " \n\r" + "  ???????? Ac : " + Ac[3] + "\n\r"
					+ "  5?? " + name[4] + " \n\r" + "  ???????? Ac : " + Ac[4]
					+ "\n\r" + "  6?? " + name[5] + " \n\r" + "  ???????? Ac : "
					+ Ac[5] + "\n\r" + "  7?? " + name[6] + " \n\r"
					+ "  ???????? Ac : " + Ac[6] + "\n\r" + "  8?? " + name[7]
					+ " \n\r" + "  ???????? Ac : " + Ac[7] + "\n\r" + "  9?? "
					+ name[8] + " \n\r" + "  ???????? Ac : " + Ac[8] + "\n\r"
					+ " 10?? " + name[9] + " \n\r" + "  ???????? Ac : " + Ac[9]
					+ "\n\r" + "      ");
		}

	}

	private int Rank(L1PcInstance pc, int number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		// int objid = pc.getId();
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			switch (number) {

			case 1: // ??????????????
				pstm = con
						.prepareStatement("SELECT enchantlvl, weapon.name, characters.char_name  FROM character_items, weapon, characters WHERE character_items.item_id in(select item_id from weapon) And character_items.char_id in(select objid from characters where AccessLevel = 0) And character_items.item_id=weapon.item_id And character_items.char_id=characters.objid And count = 1 order by character_items.enchantlvl desc limit 10");
				// pstm =
				// con.prepareStatement("SELECT enchantlvl, weapon.name, characters.char_name FROM character_items, weapon, characters WHERE character_items.item_id in(select item_id from weapon) And character_items.char_id in(select objid from characters where AccessLevel = 200) And character_items.item_id=weapon.item_id And character_items.char_id=characters.objid And character_items.is_equipped = 1 order by character_items.enchantlvl desc limit 10");
				break;
			case 2: // ??????????????
				pstm = con
						.prepareStatement("SELECT enchantlvl, armor.name, characters.char_name  FROM character_items, armor, characters WHERE character_items.item_id in(select item_id from armor) And character_items.char_id in(select objid from characters where AccessLevel = 0) And character_items.item_id=armor.item_id And character_items.char_id=characters.objid And count = 1 order by character_items.enchantlvl desc limit 10");
				break;
			case 3:
				pstm = con
						.prepareStatement("SELECT count, characters.char_name FROM character_items, characters WHERE item_id in(select item_id from etcitem) And char_id in(select objid from characters where AccessLevel = 0) And character_items.char_id=characters.objid And item_id = 40308 order by count desc limit 10");
				break;
			// pstm =
			// con.prepareStatement("SELECT enchantlvl, characters.char_name, armor.name  FROM character_items, characters, armor WHERE item_id in(select item_id from armor) And char_id in(select objid from characters where AccessLevel = 200) And char_id=characters.objid And item_id=armor.item_id And count = 1 order by enchantlvl desc limit 10");
			case 4: // ??????????????
				pstm = con
						.prepareStatement("SELECT level, char_name FROM characters WHERE AccessLevel = 0 order by level desc limit 10");
				break;
			case 5:
				pstm = con
						.prepareStatement("SELECT count, characters.char_name FROM character_items, characters 			WHERE item_id in(select item_id from etcitem) And char_id in(select objid from characters where AccessLevel = 0) And character_items.char_id=characters.objid And item_id = 41159 order by count desc limit 10");
				break;
			case 6:
				pstm = con
						.prepareStatement("SELECT count, accounts.login FROM character_warehouse, accounts   WHERE item_id in(select item_id from etcitem) And character_warehouse.account_name=accounts.login And item_id = 40308 order by count desc limit 10");
				break;
			case 7:
				pstm = con
						.prepareStatement("SELECT count, characters.char_name FROM character_items, characters WHERE item_id in(select item_id from etcitem) And char_id in(select objid from characters where AccessLevel = 0) And character_items.char_id=characters.objid And item_id = 40087 order by count desc limit 10");
				break;

			case 8: // ?????????????????????????? ??????? ???????? ?? desc ?????????????????? ?????????? ?????????????? ??
					// ???????? ???????? ?????????????? ???????????? ??...
				pstm = con
						.prepareStatement("SELECT Ac, char_name FROM characters WHERE AccessLevel = 0 order by Ac  limit 10");
				break;

			default:
				pstm = con
						.prepareStatement("SELECT char_name FROM characters WHERE AccessLevel = 0 order by Exp desc limit 10");
				break;
			}

			rs = pstm.executeQuery();
			if (number == 1) { // ???????????????? ?? ???????????? ?????? ????????;;????????
				while (rs.next()) {
					enchantlvl[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					name1[i] = rs.getString(3);
					i++;
				}
			} else if (number == 2) { // ??????????????
				while (rs.next()) {
					armor[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					name1[i] = rs.getString(3);
					i++;
				}
			} else if (number == 3) { // ??????????????
				while (rs.next()) {
					aden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 4) { // ??????????????
				while (rs.next()) {
					level[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 5) { // ??????????????
				while (rs.next()) {
					priaden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 6) { // ??????????????
				while (rs.next()) {
					aden2[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 7) { // ??????????????
				while (rs.next()) {
					priaden[i] = rs.getInt(1);
					name[i] = rs.getString(2);
					i++;
				}
			} else if (number == 8) { // ??????????????
				while (rs.next()) {
					Ac[i] = rs.getInt(1);
					name[i] = rs.getString(2);// ?????????
					i++;

				}
			} else {
				while (rs.next()) {
					name[i] = rs.getString(1);
					i++;
				}

				// ???????? ?????? 5???? ??????
				while (i < 10) {
					name[i] = "????.";
					i++;
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		return i;
	}

	private static String time() {
		TimeZone tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(tz);
		int year = cal.get(Calendar.YEAR) - 2000;
		String year2;
		if (year < 10) {
			year2 = "0" + year;
		} else {
			year2 = Integer.toString(year);
		}
		int Month = cal.get(Calendar.MONTH) + 1;
		String Month2 = null;
		if (Month < 10) {
			Month2 = "0" + Month;
		} else {
			Month2 = Integer.toString(Month);
		}
		int date = cal.get(Calendar.DATE);
		String date2 = null;
		if (date < 10) {
			date2 = "0" + date;
		} else {
			date2 = Integer.toString(date);
		}
		return year2 + "/" + Month2 + "/" + date2;
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_EnchantRanking;
	}

}
