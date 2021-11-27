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
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1EnchantArmorChance;
import l1j.server.server.utils.SQLUtil;

public class EnchantArmorChance {//enchant_armor_chance
	private static Logger _log = Logger.getLogger(EnchantArmorChance.class.getName());

	private static EnchantArmorChance _instance;
	private final FastMap<String, L1EnchantArmorChance> _armorName = new FastMap<String, L1EnchantArmorChance>();
	private final FastMap<Integer, L1EnchantArmorChance> _armorIdList = new FastMap<Integer, L1EnchantArmorChance>();

	public static EnchantArmorChance getInstance() {
		if (_instance == null) {
			_instance = new EnchantArmorChance();
		}
		return _instance;
	}

	private EnchantArmorChance() {
		System.out.print("■ 방어구인첸 데이터 .......................... ");		
		load();
		System.out.println("■ 로딩 정상 완료");	
		
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM enchant_armor_chance");
			rs = pstm.executeQuery();
			fillTable(rs);
		} catch (SQLException e) {
			_log.log(Level.SEVERE, "error while creating enchant_armor_chance table", e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void reload() {
		EnchantArmorChance oldInstance = _instance;
		_instance = new EnchantArmorChance();
		if (oldInstance != null)
			oldInstance._armorName.clear();
			oldInstance._armorIdList.clear();
	}

	private void fillTable(ResultSet rs) throws SQLException {
		L1EnchantArmorChance ed  = null;
		while (rs.next()) {
			int armorId = rs.getInt("armor_id");
			String armorname = rs.getString("armor_name");
			int e0 = rs.getInt("e0");
			int e1 = rs.getInt("e1");
			int e2 = rs.getInt("e2");
			int e3 = rs.getInt("e3");
			int e4 = rs.getInt("e4");
			int e5 = rs.getInt("e5");
			int e6 = rs.getInt("e6");
			int e7 = rs.getInt("e7");
			int e8 = rs.getInt("e8");
			int e9 = rs.getInt("e9");
			int e10 = rs.getInt("e10");
			int e11 = rs.getInt("e11");
			int e12 = rs.getInt("e12");
			int e13 = rs.getInt("e13");
			int e14 = rs.getInt("e14");
			
			ed = new L1EnchantArmorChance(armorId, armorname, e0, e1, e2, e3, e4, e5, e6, e7, e8, e9, e10, e11, e12, e13, e14);
			
			_armorName.put(armorname, ed);
			_armorIdList.put(armorId, ed);
		}
		_log.config("방어구인첸트 리스트 " + _armorName.size() + "건 로드");
		SQLUtil.close(rs);
	}
	
	public L1EnchantArmorChance getTemplate(String name) {
		return _armorName.get(name);
	}

	public L1EnchantArmorChance getTemplate(int armorId) {
		return _armorIdList.get(armorId);
	}

}
