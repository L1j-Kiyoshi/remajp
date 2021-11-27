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
package l1j.server.server.clientpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.UserRanking.UserRankingController;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharPacks;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.templates.L1UserRanking;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;

public class C_NoticeClick {
	

	private static final String C_NOTICE_CLICK = "[C] C_NoticeClick";
	private static Logger _log = Logger.getLogger(C_NoticeClick.class.getName());
	private static final int LIMIT_MIN = 1;
	private static final int LIMIT_MAX = 32767;

	public C_NoticeClick(LineageClient client) {
		try {
			
			if (client.getAccountName() == null)
				return;

			deleteCharacter(client);
			accountTimeCheck(client);

			if (client.getAccount().getCPW() == null) {
				client.sendPacket(new S_CharPass(S_CharPass._케릭선택창진입3, false),
						true); //
			} else {
				client.sendPacket(new S_CharPass(S_CharPass._케릭선택창진입3, true),
						true); //
			}

			/*client.getAccount().탐포인트업데이트(client.getAccount());*/ //미접속탐지급

			client.sendPacket(new S_NewCreateItem(S_NewCreateItem.TAM_POINT,
					client), true);

			int amountOfChars = client.getAccount().countCharacters();
			int slot = client.getAccount().getCharSlot();
			client.sendPacket(new S_CharAmount(amountOfChars, slot), true);
			client.sendPacket(new S_CharPass(S_CharPass._케릭선택창진입2), true);
			if (amountOfChars > 0)
				sendCharPacks(client);

			client.sendPacket(new S_CharPass(S_CharPass._케릭선택창진입), true);

		} catch (Exception e) {
		}
	}

	private void accountTimeCheck(LineageClient client) {
		int time = client.getAccount().getAccountTime() + 1;
		int count = 0;

		if (time == 1)
			time = 0;
		if (client.getAccount().getAccountTimeReady() > 0)
			count = 1;
		time = 0;

		client.sendPacket(new S_PacketBox(S_PacketBox.ACCOUNT_TIME, count,
				time * 60), true);
	}

	private void deleteCharacter(LineageClient client) {

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			conn = L1DatabaseFactory.getInstance().getConnection();
			pstm = conn
					.prepareStatement("SELECT * FROM characters WHERE account_name=?");
			pstm.setString(1, client.getAccountName());
			rs = pstm.executeQuery();
			Timestamp deleteTime = null;
			Calendar cal = null;
			L1Clan clan = null;
			while (rs.next()) {
				String name = rs.getString("char_name");
				String clanname = rs.getString("Clanname");
				deleteTime = rs.getTimestamp("DeleteTime");
				if (deleteTime != null) {
					cal = Calendar.getInstance();
					long checkDeleteTime = ((cal.getTimeInMillis() - deleteTime
							.getTime()) / 1000) / 3600;
					if (checkDeleteTime >= 0) {
						if (clanname != null && !clanname.equalsIgnoreCase("")) {
							clan = L1World.getInstance().getClan(clanname);
							if (clan != null) {
								clan.removeClanMember(name);
							}
						}
						CharacterTable.getInstance().deleteCharacter(
								client.getAccountName(), name);
					}
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}
	}

	public static void sendCharPacks(LineageClient client) {

		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {

			conn = L1DatabaseFactory.getInstance().getConnection();
			// pstm =
			// conn.prepareStatement("SELECT * FROM characters WHERE account_name=? ORDER BY objid ");
			pstm = conn
					.prepareStatement("SELECT * FROM characters WHERE account_name=? order by Exp desc");
			pstm.setString(1, client.getAccountName());
			rs = pstm.executeQuery();
			S_CharPacks cpk = null;
			while (rs.next()) {
				String name = rs.getString("char_name");
				String clanname = rs.getString("Clanname");
				int type = rs.getInt("Type");
				byte sex = rs.getByte("Sex");
				int lawful = rs.getInt("Lawful");

				int currenthp = rs.getInt("CurHp");
				currenthp = checkRange(currenthp);

				int currentmp = rs.getInt("CurMp");
				currentmp = checkRange(currentmp);

				int lvl;
				if (Config.CHARACTER_CONFIG_IN_SERVER_SIDE) {
					lvl = rs.getInt("level");
					if (lvl < 1)
						lvl = 1;
					else if (lvl > 127)
						lvl = 127;
				} else {
					lvl = 1;
				}

				int ac;
				if (rs.getInt("Ac") < -128) {
					ac = (byte) -128;
				} else {
					ac = rs.getByte("Ac");
				}
				int str = rs.getByte("Str");
				int dex = rs.getByte("Dex");
				int con = rs.getByte("Con");
				int wis = rs.getByte("Wis");
				int cha = rs.getByte("Cha");
				int intel = rs.getByte("Intel");
				int accessLevel = rs.getShort("AccessLevel");
				// ***********생일*******
				int birth = rs.getInt("BirthDay");
				// **********************
				L1UserRanking rank = UserRankingController.getInstance().getTotalRank(name);
				if(rank != null){
					client.sendPacket(new S_NewCreateItem(S_NewCreateItem.유저리스트랭킹, rank));
				}
				
				cpk = new S_CharPacks(name, clanname, type, sex, lawful,
						currenthp, currentmp, ac, lvl, str, dex, con, wis, cha,
						intel, accessLevel, birth);

				client.sendPacket(cpk);
				cpk = null;
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(conn);
		}
	}

	private static int checkRange(int i) {
		if (i < LIMIT_MIN)
			return LIMIT_MIN;
		else if (i > LIMIT_MAX)
			return LIMIT_MAX;
		return i;
	}

	public String getType() {
		return C_NOTICE_CLICK;
	}
}