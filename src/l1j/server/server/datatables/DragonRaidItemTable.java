package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.utils.SQLUtil;

public class DragonRaidItemTable {

	private static DragonRaidItemTable _instance;

	public static DragonRaidItemTable get() {
		if (_instance == null) {
			_instance = new DragonRaidItemTable();
		}
		return _instance;
	}

	private FastTable<DragonRaidItem> list;

	public DragonRaidItemTable() {
		list = new FastTable<DragonRaidItem>();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM dragon_raid_item");
			rs = pstm.executeQuery();
			while (rs.next()) {
				DragonRaidItem item = new DragonRaidItem();
				item.setNpcId(rs.getInt("npcid"));
				item.setMemberMinSize(rs.getInt("member_size_min"));
				item.setMemberMaxSize(rs.getInt("member_size_max"));
				item.setItemId(rs.getInt("itemid"));
				item.setCount(rs.getInt("count"));
				item.setChance(rs.getInt("chance"));
				list.add(item);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void reload() {
		DragonRaidItemTable oldInstance = _instance;
		_instance = new DragonRaidItemTable();
		oldInstance.list.clear();
	}

	public void insertItem(L1NpcInstance npc, int memberSize) {
		Random rnd = new Random(System.nanoTime());
		for (DragonRaidItem temp : list) {
			if (npc.getNpcId() == temp.getNpcId()) {
				// System.out.println(temp.getMemberMinSize() +" > "+
				// memberSize+" > "+temp.getMemberMaxSize() +" > "+
				// memberSize+" > "+ temp.getChance());
				if (temp.getMemberMinSize() <= memberSize
						&& temp.getMemberMaxSize() >= memberSize) {
					int rand = rnd.nextInt(100) + 1;
					if (rand <= temp.getChance()) {
						int count = temp.getCount();
						// if(temp.getCount() > 1)
						// count = rnd.nextInt(temp.getCount())+1;
						npc.getInventory().storeItem(temp.getItemId(), count);
					}
				}
			}
		}
	}

	class DragonRaidItem {
		private int npcid;
		private int member_size_min;
		private int member_size_max;
		private int itemid;
		private int count;
		private int chance;

		public void setNpcId(int i) {
			npcid = i;
		}

		public int getNpcId() {
			return npcid;
		}

		public void setMemberMinSize(int i) {
			member_size_min = i;
		}

		public int getMemberMinSize() {
			return member_size_min;
		}

		public void setMemberMaxSize(int i) {
			member_size_max = i;
		}

		public int getMemberMaxSize() {
			return member_size_max;
		}

		public void setItemId(int i) {
			itemid = i;
		}

		public int getItemId() {
			return itemid;
		}

		public void setCount(int i) {
			count = i;
		}

		public int getCount() {
			return count;
		}

		public void setChance(int i) {
			chance = i;
		}

		public int getChance() {
			return chance;
		}
	}

}
