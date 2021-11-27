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
import java.util.ArrayList;
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

public class S_NavarWarfare_Ranking extends ServerBasePacket {

	private static final String S_Ranking = "[C] S_Ranking";

	private static Logger _log = Logger.getLogger(S_NavarWarfare_Ranking.class
			.getName());

	private byte[] _byte = null;
	static String[] name;
	static int[] score;

	public S_NavarWarfare_Ranking(L1PcInstance pc, int number) {
		name = new String[5];
		score = new int[5];
		buildPacket(pc, number);
	}

	public S_NavarWarfare_Ranking(int rank, int score) {
		writeC(Opcodes.S_EVENT);
		writeC(0x70);
		writeD(0x00);
		writeD(0x19);
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM navalwarfare_score order by score desc limit 5");
			rs = pstm.executeQuery();
			while (rs.next()) {
				int score2 = rs.getInt("score");
				ArrayList<String> list = new ArrayList<String>();
				for (int a = 3; a < 11; a++) {
					String tt = rs.getString(a);
					if (tt == null || tt.equalsIgnoreCase(""))
						continue;
					list.add(tt);
				}
				i++;
				writeC(5);
				writeD(score2 < 0 ? 0 : score2);
				for (int a = 0; a < 5; a++) {
					try {
						writeS(list.get(a));
					} catch (Exception e) {
						writeS("");
					}
				}
			}
			// 레코드가 없거나 5보다 작을때
			while (i < 5) {
				writeC(5);
				writeD(0x00);
				writeS("");
				writeS("");
				writeS("");
				writeS("");
				writeS("");
				i++;
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		writeC(rank);
		writeD(score);
		writeH(0x00);
	}

	public S_NavarWarfare_Ranking(L1NpcInstance board) {
		buildPacket(board);
	}

	private void buildPacket(L1NpcInstance board) {
		int count = 0;
		String[][] db = null;
		int[] id = null;
		db = new String[1][3];
		id = new int[1];
		while (count < 1) {
			id[count] = count + 1;
			db[count][0] = "";// Ranking
			db[count][1] = "";
			count++;
		}
		db[0][2] = "  == 해상전 순위 ==";

		writeC(Opcodes.S_BOARD_LIST);
		writeC(0);
		writeD(board.getId());
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0xFF); // ?
		writeC(0x7F); // ?
		writeH(1);
		writeH(300);
		for (int i = 0; i < 1; ++i) {
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
		writeS("운영자");
		switch (number) {
		case 1:
			title = "해상전 순위";
			break;
		}
		writeS(title);
		writeS(date);
		Rank(number);
		writeS(" 1위 점수: " + score[0] + " 명단: " + name[0] + "\n\r\n\r"
				+ " 2위 점수: " + score[1] + " 명단: " + name[1] + "\n\r\n\r"
				+ " 3위 점수: " + score[2] + " 명단: " + name[2] + "\n\r\n\r"
				+ " 4위 점수: " + score[3] + " 명단: " + name[3] + "\n\r\n\r"
				+ " 5위 점수: " + score[4] + " 명단: " + name[4] + "\n\r\n\r"
				+ "             ");
	}

	private int Rank(int number) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			switch (number) {
			case 1:
				pstm = con
						.prepareStatement("SELECT * FROM navalwarfare_score order by score desc limit 5");
				break;
			default:
				pstm = con
						.prepareStatement("SELECT * FROM navalwarfare_score order by score desc limit 5");
				break;
			}
			rs = pstm.executeQuery();
			while (rs.next()) {
				score[i] = rs.getInt("score");
				name[i] = "";
				for (int a = 3; a < 11; a++) {
					String tt = rs.getString(a);
					if (tt == null || tt.equalsIgnoreCase(""))
						continue;
					if (a == 3)
						name[i] = name[i] + tt;
					else
						name[i] = name[i] + "," + tt;
				}
				i++;
			}
			// 레코드가 없거나 5보다 작을때
			while (i < 5) {
				score[i] = 0;
				name[i] = "없음.";
				i++;
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
		return S_Ranking;
	}

}
