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
package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.SQLUtil;

public class L1Clan {
	static public class ClanMember {
		public String name;
		public int rank;
		public int level;
		public int type;
		public String memo;
		public boolean online;
		public L1PcInstance player;

		public ClanMember(String name, int rank, int level, int type,
				String Memo, boolean online, L1PcInstance pc) {
			this.name = name;
			this.rank = rank;
			this.level = level;
			this.type = type;
			this.memo = Memo;
			this.online = online;
			this.player = pc;
		}
	}

	/*
	 * public static final int CLAN_RANK_PROBATION = 5; public static final int
	 * CLAN_RANK_PUBLIC = 2; public static final int CLAN_RANK_GUARDIAN = 6;
	 * public static final int CLAN_RANK_PRINCE = 4; public static final int
	 * CLAN_RANK_SUBPRINCE = 3;
	 */

	// ÀÏ¹ÝÇ÷
	public static final int CLAN_RANK_PROBATION = 7;
	public static final int CLAN_RANK_PUBLIC = 8;
	public static final int CLAN_RANK_GUARDIAN = 9;
	public static final int CLAN_RANK_PRINCE = 10;
	public static final int CLAN_RANK_Á¤¿¹ = 13;
	public static final int CLAN_RANK_SUBPRINCE = 3;

	@SuppressWarnings("unused")
	private static final Logger _log = Logger.getLogger(L1Clan.class.getName());

	private int _clanId;
	private String _clanName;
	private int _leaderId;
	private String _leaderName;
	private int _castleId;
	private int _houseId;
	private int _WarPoint;

	// private int _alliance;
	private String _createdate;
	private String _CastleHasDate;
	private int _markon;
	private String _Notice;
	private int _join_setting;
	private int _join_type;
	private ArrayList<ClanMember> clanMemberList = new ArrayList<ClanMember>();
	private ArrayList<Integer> allianceList = new ArrayList<Integer>();
	private ArrayList<String> MarkSeeList = new ArrayList<String>();

	public void addMarkSeeList(String clanname) {
		MarkSeeList.add(clanname);
	}

	public void removeMarkSeeList(String clanname) {
		MarkSeeList.remove(clanname);
	}

	public ArrayList<String> getMarkSeeList() {
		return MarkSeeList;
	}

	public ArrayList<ClanMember> getClanMemberList() {
		return clanMemberList;
	}

	public void addClanMember(String name, int rank, int level, int type,
			String memo, int online, L1PcInstance pc) {
		clanMemberList.add(new ClanMember(name, rank, level, type, memo,
				online == 1, online == 1 ? pc : null));
	}

	public void removeClanMember(String name) {
		ClanMember[] list = clanMemberList
				.toArray(new ClanMember[clanMemberList.size()]);
		for (int i = 0; i < list.length; i++) {
			if (list[i].name.equals(name)) {
				clanMemberList.remove(i);
				break;
			}
		}
	}

	public void setClanRankMember(String name, int rank) {
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				clanMemberList.get(i).rank = rank;
				break;
			}
		}
	}

	public void addOnlineClanMember(String name, L1PcInstance pc) {
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				clanMemberList.get(i).online = true;
				clanMemberList.get(i).player = pc;
				break;
			}
		}
	}

	public void removeOnlineClanMember(String name) {
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).name.equals(name)) {
				clanMemberList.get(i).online = false;
				clanMemberList.get(i).player = null;
				break;
			}
		}
	}

	public int getmarkon() {
		return _markon;
	}

	public void setmarkon(int i) {
		_markon = i;
	}

	public String getNotice() {
		return _Notice;
	}

	public void setNotice(String s) {
		_Notice = s;
	}

	public int getJoinSetting() {
		return _join_setting;
	}

	public void setJoinSetting(int i) {
		_join_setting = i;
	}

	public int getJoinType() {
		return _join_type;
	}

	public void setJoinType(int i) {
		_join_type = i;
	};
	
	/** Ç÷¸Í¹öÇÁ Æ÷ÀÎÆ® **/	
	private int _bless = 0;
	private int _blesscount = 0;
	private int _attack = 0;
	private int _defence = 0;
	private int _pvpattack = 0;
	private int _pvpdefence = 0;
	public int[] getBuffTime = new int[] { _attack, _defence, _pvpattack, _pvpdefence };

	public int[] getBuffTime() {
		return getBuffTime;
	}

	public void setBuffTime(int i, int j) {
		getBuffTime[i] = IntRange.ensure(j, 0, 172800);
	}

	public void setBuffTime(int a, int b, int c, int d) {
		getBuffTime = new int[] { a, b, c, d };
	}

	public int getBlessCount() {
		return _blesscount;
	}

	public void setBlessCount(int i) {
		_blesscount = IntRange.ensure(i, 0, 400000000);
	}

	public void addBlessCount(int i) {
		_blesscount += i;
		if (_blesscount > 400000000)
			_blesscount = 400000000;
		else if (_blesscount < 0)
			_blesscount = 0;
	}
	public int getBless() {
		return _bless;
	}

	public void setBless(int i) {
		_bless = i;
	}

	public int getClanId() {
		return _clanId;
	}

	public void setClanId(int clan_id) {
		_clanId = clan_id;
	}

	public String getClanName() {
		return _clanName;
	}

	public void setClanName(String clan_name) {
		_clanName = clan_name;
	}

	public int getLeaderId() {
		return _leaderId;
	}

	public void setLeaderId(int leader_id) {
		_leaderId = leader_id;
	}

	public String getLeaderName() {
		return _leaderName;
	}

	public void setLeaderName(String leader_name) {
		_leaderName = leader_name;
	}

	public int getCastleId() {
		return _castleId;
	}

	public void setCastleId(int hasCastle) {
		_castleId = hasCastle;
	}

	public int getWarpoint() {
		return _WarPoint;
	}

	public void setWarpoint(int wp) {
		_WarPoint = wp;
	}

	public void addWarpoint(int wp) {
		_WarPoint += wp;
	}

	public int getHouseId() {
		return _houseId;
	}

	public void setHouseId(int hasHideout) {
		_houseId = hasHideout;
	}

	/*
	 * public int getAlliance() { return _alliance; } public void
	 * setAlliance(int alliance) { _alliance = alliance; }
	 */
	public String getCreateDate() {
		return _createdate;
	}

	public void setCreateDate(String date) {
		_createdate = date;
	}

	public String getCastleHasDate() {
		return _CastleHasDate;
	}

	public void setCastleHasDate(String date) {
		_CastleHasDate = date;
	}

	// ¿Â¶óÀÎÁßÀÇ Ç÷¿ø¼ö
	public int getOnlineMemberCount() {
		int count = 0;
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).online)
				count++;
			// if (L1World.getInstance().getPlayer(clanMemberList.get(i).name)
			// != null) {
			// count++;
			// }
		}
		return count;
	}

	// ¿Â¶óÀÎÁßÀÇ Ç÷¿ø¼ö
	public int getOnlinePrivateShopXMemberCount() {
		int count = 0;
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).online) {
				if (!clanMemberList.get(i).player.isPrivateShop())
					count++;
			}
			// if (L1World.getInstance().getPlayer(clanMemberList.get(i).name)
			// != null) {
			// count++;
			// }
		}
		return count;
	}

	public L1PcInstance getonline°£ºÎ() {
		L1PcInstance pc = null;
		L1PcInstance no1pc = null;
		int oldrank = 0;
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i) == null)
				continue;
			if (!clanMemberList.get(i).online
					|| clanMemberList.get(i).player == null)
				continue;
			pc = clanMemberList.get(i).player;
			if (pc.getClanRank() >= L1Clan.CLAN_RANK_GUARDIAN) {
				if (oldrank < pc.getClanRank()) {
					oldrank = pc.getClanRank();
					no1pc = pc;
				}
			}
		}
		return no1pc;
	}

	// ¿Â¶óÀÎÁß Ç÷¿ø ÀÎ½ºÅÏ½º ¸®½ºÆ®
	public L1PcInstance[] getOnlineClanMember() {
		ArrayList<L1PcInstance> onlineMembers = new ArrayList<L1PcInstance>();
		L1PcInstance pc = null;
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i) == null)
				continue;
			if (!clanMemberList.get(i).online
					|| clanMemberList.get(i).player == null)
				continue;
			pc = clanMemberList.get(i).player;
			// pc = L1World.getInstance().getPlayer(clanMemberList.get(i).name);
			if (pc != null && !onlineMembers.contains(pc)) {
				onlineMembers.add(pc);
			}
		}
		L1PcInstance[] list = onlineMembers
				.toArray(new L1PcInstance[onlineMembers.size()]);
		if (onlineMembers.size() > 0)
			onlineMembers.clear();
		onlineMembers = null;
		return list;
	}

	public String getClanSubPrince() {
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).rank == CLAN_RANK_SUBPRINCE)
				return clanMemberList.get(i).name;
		}
		return null;
	}

	public int getGuardianCount() {
		int count = 0;
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).rank == CLAN_RANK_GUARDIAN)
				count++;
		}
		return count;
	}

	public int getÁ¤¿¹Count() {
		int count = 0;
		for (int i = 0; i < clanMemberList.size(); i++) {
			if (clanMemberList.get(i).rank == CLAN_RANK_Á¤¿¹)
				count++;
		}
		return count;
	}

	public L1Clan getAlliance(int i) {
		if (allianceList.size() > 0) {
			for (int id : allianceList) {
				if (id == i) {
					return L1World.getInstance().getClan(i);
				}
			}
		}
		return null;
	}

	public int getCrownMaxMember() {
		int result = 0;
		int level = 0;
		int cha = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;

		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM characters WHERE objid=?");
			pstm.setInt(1, getLeaderId());
			rs = pstm.executeQuery();

			if (rs.next()) {
				level = rs.getInt("level");
				cha = rs.getInt("Cha");
			}

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}

		if (level != 0) {
			if (level >= 50) {
				result = cha * 9;
			} else if (level >= 45) {
				result = cha * 6;
			} else {
				result = cha * 3;
			}
		}

		return result;
	}

	public void addAlliance(int i) {
		if (i == 0)
			return;
		if (!allianceList.contains((Integer) i))
			allianceList.add((Integer) i);
	}

	public void removeAlliance(int i) {
		if (i == 0)
			return;
		if (allianceList.contains((Integer) i))
			allianceList.remove((Integer) i);
	}

	public Integer[] Alliance() {
		Integer[] i = (Integer[]) allianceList.toArray(new Integer[allianceList
				.size()]);
		return i;
	}

	public int AllianceSize() {
		return allianceList.size();
	}

	public void AllianceDelete() {
		if (allianceList.size() > 0)
			allianceList.clear();
	}
	
	/** 2016.11.25 MJ ¾Û¼¾ÅÍ Ç÷¸Í **/
	private String _joinPassword;
	public String getJoinPassword() {
		return _joinPassword;
	}
	public void setJoinPassword(String s) {
		_joinPassword = s;
	}
	/** 2016.11.25 MJ ¾Û¼¾ÅÍ Ç÷¸Í **/

	private boolean _iscaveopen;

	public boolean isCaveOpen() {
		return _iscaveopen;
	}

	public void setCaveOpen(boolean flag) {
		_iscaveopen = flag;
	}
	
	private boolean _ishuntchoice;
	
	public boolean isHuntMapChoice(){
		return _ishuntchoice;
	}
	
	public void setHuntMapChoice(boolean flag) {
		_ishuntchoice = flag;
	}

	private ArrayList<Integer> _blesshuntmaps;

	public ArrayList<Integer> getBlessHuntMaps() {
		return _blesshuntmaps;
	}

	public void setBlessHuntMaps(ArrayList<Integer> i) {
		_blesshuntmaps = i;
	}

	private ArrayList<Integer> _blesshuntmapstype;

	public ArrayList<Integer> getBlessHuntMapsType() {
		return _blesshuntmapstype;
	}

	public void setBlessHuntMapsType(ArrayList<Integer> i) {
		_blesshuntmapstype = i;
	}

	private ArrayList<Integer> _blesshuntmapids;

	public ArrayList<Integer> getBlessHuntMapIds() {
		return _blesshuntmapids;
	}

	public void setBlessHuntMapIds(ArrayList<Integer> i) {
		_blesshuntmapids = i;
	}
}
