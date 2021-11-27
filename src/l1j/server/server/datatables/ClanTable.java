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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.Warehouse.ClanWarehouse;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server:
// IdFactory

public class ClanTable {
	private static Logger _log = Logger.getLogger(ClanTable.class.getName());
	private static ClanTable _instance;

	private final HashMap<Integer, L1Clan> _clans = new HashMap<Integer, L1Clan>();

	private final HashMap<Integer, L1Clan> _clancastle = new HashMap<Integer, L1Clan>();

	public static ClanTable getInstance() {
		if (_instance == null) {
			_instance = new ClanTable();
		}
		return _instance;
	}
	
	public static void reload() {
		ClanTable oldInstance = _instance;
		_instance = new ClanTable();
		if (oldInstance != null) {
			oldInstance._clans.clear();
			oldInstance._clancastle.clear();
		}
	}

	private ClanTable() {
		{
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT * FROM clan_data ORDER BY clan_id");

				rs = pstm.executeQuery();
				L1Clan clan = null;
				while (rs.next()) {
					clan = new L1Clan();
					int clan_id = rs.getInt(1);
					int castle_id = rs.getInt(5);
					clan.setClanId(clan_id);
					clan.setClanName(rs.getString(2));
					clan.setLeaderId(rs.getInt(3));
					clan.setLeaderName(rs.getString(4));
					clan.setCastleId(castle_id);
					clan.setHouseId(rs.getInt(6));
					clan.addAlliance(rs.getInt(7));
					clan.addAlliance(rs.getInt(8));
					clan.addAlliance(rs.getInt(9));
					clan.addAlliance(rs.getInt(10));
					clan.setCreateDate(rs.getString(11));
					clan.setmarkon(rs.getInt(12));
					clan.setNotice(rs.getString(13));
					clan.setJoinSetting(rs.getInt(14));
					clan.setJoinType(rs.getInt(15));
					clan.setWarpoint(rs.getInt(18));
					clan.setCastleHasDate(rs.getString(19));
					clan.setJoinPassword(rs.getString(20));
					clan.setCaveOpen(rs.getInt("is_cave_open") == 1 ? true : false);

					ArrayList<Integer> maps = getStringToArrayList(rs.getString("bless_hunt_maps"));
					ArrayList<Integer> maps2 = getStringToArrayList(rs.getString("bless_hunt_maps_type"));
					ArrayList<Integer> maps3 = getStringToArrayList(rs.getString("bless_hunt_map_ids"));
					clan.setBlessHuntMaps(maps);
					clan.setBlessHuntMapsType(maps2);
					clan.setBlessHuntMapIds(maps3);

					clan.setHuntMapChoice(rs.getInt("is_hunt_choice") == 1 ? true : false);
					L1World.getInstance().storeClan(clan);
					_clans.put(clan_id, clan);
					if (castle_id > 0) {
						_clancastle.put(castle_id, clan);
					}
				}

				for (int i = 1; i < 9; i++) {
					// 1 ÄËÆ® 2 ¿À¼º 3 À©¼º 4 ±â¶õ¼º 5 ÇÏÀÌ³×¼º 6 ³­¼º 7 ¾Æµ§¼º 8 µð¾Æµå
					if (i == 4) {
						if (_clancastle.get(i) == null) {
							clan = new L1Clan();
							int clan_id = ObjectIdFactory.getInstance().nextId();
							clan.setClanId(clan_id);
							clan.setClanName("_Giran");
							clan.setLeaderId(ObjectIdFactory.getInstance().nextId());
							clan.setLeaderName("-Giran-");
							clan.setCastleId(i);
							clan.setCreateDate("2013/01/01");
							L1World.getInstance().storeClan(clan);
							_clans.put(clan_id, clan);
							_clancastle.put(i, clan);
						}
					}
					if (i == 1) {
						if (_clancastle.get(i) == null) {
							clan = new L1Clan();
							int clan_id = ObjectIdFactory.getInstance().nextId();
							clan.setClanId(clan_id);
							clan.setClanName("_Kent");
							clan.setLeaderId(ObjectIdFactory.getInstance().nextId());
							clan.setLeaderName("-Kent-");
							clan.setCastleId(i);
							clan.setCreateDate("2013/01/01");
							L1World.getInstance().storeClan(clan);
							_clans.put(clan_id, clan);
							_clancastle.put(i, clan);
						}
					}
				}

			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

		String name;
		String Memo;
		int rank;
		int level;
		int type;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con.prepareStatement("SELECT char_name, ClanRank, level, Type , Clan_Memo FROM characters WHERE ClanID = ?");
				pstm.setInt(1, clan.getClanId());
				rs = pstm.executeQuery();

				while (rs.next()) {

					name = rs.getString("char_name");
					rank = rs.getInt("ClanRank");
					level = rs.getInt("level");
					type = rs.getInt("Type");
					Memo = rs.getString("Clan_Memo");

					clan.addClanMember(name, rank, level, type, Memo, 0, null);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
			}
			try {
				pstm = con.prepareStatement("SELECT name, class FROM robots WHERE clanid = ?");
				pstm.setInt(1, clan.getClanId());
				rs = pstm.executeQuery();

				while (rs.next()) {

					name = rs.getString("name");
					rank = L1Clan.CLAN_RANK_PUBLIC;
					level = 65;
					int clas = rs.getInt("class");
					if (clas == 0 || clas == 1)
						type = 0;
					else if (clas == 61 || clas == 48)
						type = 1;
					else if (clas == 138 || clas == 37)
						type = 2;
					else if (clas == 734 || clas == 1186)
						type = 3;
					else if (clas == 2786 || clas == 2796)
						type = 4;
					else if (clas == 6658 || clas == 6661)
						type = 5;
					else
						type = 6;
					Memo = "";

					clan.addClanMember(name, rank, level, type, Memo, 0, null);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
			}
			try {
				pstm = con.prepareStatement("SELECT name, clan_memo FROM robots_crown WHERE clanid = ?");
				pstm.setInt(1, clan.getClanId());
				rs = pstm.executeQuery();

				while (rs.next()) {

					name = rs.getString("name");
					rank = L1Clan.CLAN_RANK_PRINCE;
					level = 65;
					type = 0;
					Memo = rs.getString("clan_memo");

					clan.addClanMember(name, rank, level, type, Memo, 0, null);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
			}

			try {
				pstm = con.prepareStatement("SELECT * FROM clan_mark_observe WHERE clanid = ?");
				pstm.setInt(1, clan.getClanId());
				rs = pstm.executeQuery();

				while (rs.next()) {
					String observe_name = rs.getString("mark_observe_name");
					clan.addMarkSeeList(observe_name);
				}
			} catch (SQLException e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
		}

		ClanWarehouse clanWarehouse;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
			clanWarehouse.loadItems();
		}
	}

	public L1Clan createClan(L1PcInstance player, String clan_name) {
		return createClan(player, clan_name, 0);
	}

	public L1Clan createClan(L1PcInstance player, String clan_name, int clanid) {
		for (L1Clan oldClans : L1World.getInstance().getAllClans()) {
			if (oldClans.getClanName().equalsIgnoreCase(clan_name)) {
				return null;
			}
		}

		L1Clan clan = new L1Clan();
		if (clanid == 0)
			clan.setClanId(ObjectIdFactory.getInstance().nextId());
		else
			clan.setClanId(clanid);
		clan.setClanName(clan_name);
		clan.setLeaderId(player.getId());
		clan.setLeaderName(player.getName());
		clan.setCastleId(0);
		clan.setHouseId(0);
		SimpleDateFormat s = new SimpleDateFormat("yyyy/MM/dd", Locale.KOREA);
		clan.setCreateDate(s.format(Calendar.getInstance().getTime()));
		clan.setJoinSetting(1);
		clan.setJoinType(1);

		Connection con = null;
		PreparedStatement pstm = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"INSERT INTO clan_data SET clan_id=?, clan_name=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, create_date=?, markon=?, total_m=? , current_m=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setString(2, clan.getClanName());
			pstm.setInt(3, clan.getLeaderId());
			pstm.setString(4, clan.getLeaderName());
			pstm.setInt(5, clan.getCastleId());
			pstm.setInt(6, clan.getHouseId());
			pstm.setString(7, clan.getCreateDate());
			pstm.setInt(8, clan.getmarkon());
			pstm.setInt(9, clan.getClanMemberList().size());
			pstm.setInt(10, clan.getOnlineMemberCount());
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		L1World.getInstance().storeClan(clan);
		_clans.put(clan.getClanId(), clan);

		player.setClanid(clan.getClanId());
		player.setClanname(clan.getClanName());
		player.setClanRank(L1Clan.CLAN_RANK_PRINCE);
		clan.addClanMember(player.getName(), player.getClanRank(), player.getLevel(), player.getType(), player.getMemo(), player.getOnlineStatus(), player);
		try {
			player.save();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return clan;
	}

	public void updateClanBlessHunt(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE clan_data SET is_cave_open=?, bless_hunt_maps=?, bless_hunt_maps_type=?, bless_hunt_map_ids=?, is_hunt_choice=? "
							+ "WHERE clan_name=?");

			pstm.setInt(1, clan.isCaveOpen() ? 1 : 0);

			if (clan.isCaveOpen()) {
				StringBuffer msg = new StringBuffer();
				StringBuffer msg2 = new StringBuffer();
				StringBuffer msg3 = new StringBuffer();
				for (Integer id : clan.getBlessHuntMaps()) {
					msg.append(id).append(",");
				}

				for (Integer id : clan.getBlessHuntMapsType()) {
					msg2.append(id).append(",");
				}

				for (Integer id : clan.getBlessHuntMapIds()) {
					msg3.append(id).append(",");
				}

				pstm.setString(2, msg.toString());
				pstm.setString(3, msg2.toString());
				pstm.setString(4, msg3.toString());

				msg = null;
				msg2 = null;
				msg3 = null;
			} else {
				pstm.setString(2, null);
				pstm.setString(3, null);
				pstm.setString(4, null);
			}

			pstm.setInt(5, clan.isHuntMapChoice() ? 1 : 0);
			pstm.setString(6, clan.getClanName());
			pstm.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(con);
			SQLUtil.close(pstm);
		}
	}

	public void updateClan(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement(
					"UPDATE clan_data SET clan_id=?, leader_id=?, leader_name=?, hascastle=?, hashouse=?, alliance=?, alliance2=?, alliance3=?, "
							+ "alliance4=?, create_date=?, markon=?, Notice=?, join_setting=?, join_type=? , total_m=? , current_m=? , War_point=? , "
							+ "Castle_hasdate=? WHERE clan_name=?");
			pstm.setInt(1, clan.getClanId());
			pstm.setInt(2, clan.getLeaderId());
			pstm.setString(3, clan.getLeaderName());
			pstm.setInt(4, clan.getCastleId());
			pstm.setInt(5, clan.getHouseId());

			int count = 6;
			for (int i : clan.Alliance()) {
				pstm.setInt(count, i);
				count++;
			}
			if (count < 10) {
				for (; count < 10; count++) {
					pstm.setInt(count, 0);
				}
			}
			pstm.setString(10, clan.getCreateDate());
			pstm.setInt(11, clan.getmarkon());
			pstm.setString(12, clan.getNotice());
			pstm.setInt(13, clan.getJoinSetting());
			pstm.setInt(14, clan.getJoinType());

			pstm.setInt(15, clan.getClanMemberList().size());
			pstm.setInt(16, clan.getOnlineMemberCount());
			pstm.setInt(17, clan.getWarpoint());

			pstm.setString(18, clan.getCastleHasDate());

			pstm.setString(19, clan.getClanName());

			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void deleteClan(String clan_name) {
		L1Clan clan = L1World.getInstance().getClan(clan_name);
		if (clan == null) {
			return;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_data WHERE clan_name=?");
			pstm.setString(1, clan_name);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		ClanWarehouse clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
		clanWarehouse.clearItems();
		clanWarehouse.deleteAllItems();

		L1World.getInstance().removeClan(clan);
		_clans.remove(clan.getClanId());
	}

	public void insertObserverClan(int clanid, String observe_name) {
		L1Clan clan = getTemplate(clanid);
		if (clan == null) {
			return;
		}

		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO clan_mark_observe SET clanid=?, mark_observe_name=?");
			pstm.setInt(1, clanid);
			pstm.setString(2, observe_name);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void updateBless(int clanid, int bless) {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("UPDATE clan_data SET bless=? WHERE clan_id=?")) {
			pstm.setInt(1, bless);
			pstm.setInt(2, clanid);
			pstm.execute();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	public void deleteObserveClan(int clanid, String observe_name) {
		L1Clan clan = getTemplate(clanid);
		if (clan == null) {
			return;
		}
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM clan_mark_observe WHERE clanid=? AND mark_observe_name=?");
			pstm.setInt(1, clanid);
			pstm.setString(2, observe_name);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public L1Clan getTemplate(int clan_id) {
		return _clans.get(clan_id);
	}

	public HashMap<Integer, L1Clan> getClanCastles() {
		return _clancastle;
	}

	/** 2016.11.25 MJ ¾Û¼¾ÅÍ Ç÷¸Í **/
	public void updateClanPassword(L1Clan clan) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE clan_data SET join_password=? WHERE clan_name=?");
			pstm.setString(1, clan.getJoinPassword());
			pstm.setString(2, clan.getClanName());
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/** 2016.11.25 MJ ¾Û¼¾ÅÍ Ç÷¸Í **/

	private ArrayList<Integer> getStringToArrayList(String set) {
		ArrayList<Integer> list = new ArrayList<Integer>();

		if (set != null && set.length() > 0) {
			for (String value : set.split(","))
				list.add(Integer.valueOf(value));
		}
		return list;
	}
}
