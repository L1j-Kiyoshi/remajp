package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;

public class CraftInfoTable {
	private static CraftInfoTable ins;
	private HashMap<Integer, L1CraftInfo> craftInfoList = new HashMap<Integer, L1CraftInfo>();
	private HashMap<Integer, int[]> craftNpcList = new HashMap<Integer, int[]>();
	public static CraftInfoTable getIns() {
		if (ins == null) ins = new CraftInfoTable();
		return ins;
	}
	private CraftInfoTable() {
		loadCraftInfos();
		loadCraftNpcs();
	}
	public void reLoad() {
		CraftInfoTable oldIns = ins;
		ins = new CraftInfoTable();
		oldIns.craftInfoList.clear();
		oldIns.craftNpcList.clear();
		oldIns = null;
	}
	private void loadCraftInfos() {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection();
				PreparedStatement pstm = con.prepareStatement("SELECT * FROM craft_info_list");
				ResultSet rs = pstm.executeQuery()) {

			while (rs.next()) {
				L1CraftInfo craftInfo = new L1CraftInfo();
				int craftId = rs.getInt("craft_id");
				craftInfo.craftId = craftId;
				craftInfo.itemId = rs.getInt("item_id");
				craftInfo.descId = rs.getInt("desc_id");
				craftInfo.name = rs.getString("name");
				craftInfo.makeCount = rs.getInt("make_count");
				craftInfo.enchant = rs.getInt("enchant");
				craftInfo.bless = rs.getInt("bless");
				craftInfo.attr = rs.getInt("attr");
				craftInfo.probability = rs.getInt("probability");
				craftInfo.materialList = materialList(craftInfo, rs.getString("material_list"));
				if (craftInfo.materialList == null || craftInfo.materialList.size() == 0) {
					System.out.println("재료아이템 갯수 오류 craftId : " + craftId);
					craftInfo = null;
				} else {
					craftInfoList.put(craftId, craftInfo);
				}
				//break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void loadCraftNpcs() {
		try (Connection con = L1DatabaseFactory.getInstance().getConnection(); 
				PreparedStatement pstm = con.prepareStatement("SELECT * FROM craft_npcs"); 
				ResultSet rs = pstm.executeQuery()) {

			while (rs.next()) {
				int npcId = rs.getInt("npc_id");
				int[] craftList = craftListToIntArray(rs.getString("craft_id_list"));
				if (craftList.length == 0 || craftList == null) {
					System.out.println("제작리스트 갯수 오류 npcId : " + npcId);
				} else {
					craftNpcList.put(npcId, craftList);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int[] getCraftNpc(int npcId) {
		return craftNpcList.get(npcId);
	}
	public L1CraftInfo getCraftInfo(int craftId) {
		return craftInfoList.get(craftId);
	}
	private int[] craftListToIntArray(String craftIdList) {
		try {
			craftIdList = trim(craftIdList);
			StringTokenizer stCraftIdList = new StringTokenizer(craftIdList, ",");
			int size = stCraftIdList.countTokens();
			if (size <= 0) return null;
			int[] result = new int[size];
			for (int i = 0; i < size; i++) {
				int craftId = Integer.parseInt(stCraftIdList.nextToken(), 10);
				//System.out.println("list : " + list);
				if (craftId == 0) return null;
				result[i] = craftId;
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private String trim(String craftList) {
		craftList.trim();
		craftList = craftList.replaceAll("\\p{Z}", "");
		craftList = craftList.replaceAll("\\p{Space}", "");
		craftList = craftList.replaceAll("=", "");
		return craftList;
	}
	private ArrayList<Material> materialList(L1CraftInfo ci, String materialList) {
		try {
			StringTokenizer stMaterialList = new StringTokenizer(materialList, "\r\n"); //재료들 전체 담김.
			ArrayList<Material> result = new ArrayList<Material>(); //여기에는 재료가 담긴다. 정보화해서.
			while (stMaterialList.hasMoreTokens()) {
				Material matrial = new Material();
				for (int i = 0; i < 5; i++) {
					String infos = trim(stMaterialList.nextToken());
					//System.out.println("totalInfo : " + infos);
					//Dlog.d("totalInfo : " + infos);
					if (infos.startsWith("descId:")) {
						infos = infos.replace("descId:", "");
						matrial.descIds = strToInfo(infos); //여기에는 descId 가 담긴다. 대체아이템 포함.
					} else if (infos.startsWith("count:")) {
						infos = infos.replace("count:", "");
						matrial.count = strToInfo(infos); //여기에는 count 가 담긴다. 대체아이템 포함.
					} else if (infos.startsWith("enchant:")) {
						infos = infos.replace("enchant:", "");
						matrial.enchant = strToInfo(infos); //여기에는 count 가 담긴다. 대체아이템 포함.
					} else if (infos.startsWith("bless:")) {
						infos = infos.replace("bless:", "");
						matrial.bless = strToInfo(infos); //여기에는 count 가 담긴다. 대체아이템 포함.
					} 
				}
				int len = matrial.descIds.length;
				if (len == matrial.count.length && len == matrial.enchant.length && len == matrial.bless.length) {
					result.add(matrial);
				} else {
					System.out.println("Material length 오류 craftId : " + ci.craftId);
					return null;
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private int[] strToInfo(String infos) {
		StringTokenizer stInfos = new StringTokenizer(infos, ",");
		int[] result = new int[stInfos.countTokens()];
		for (int i = 0; i < result.length; i++) {
			result[i] = Integer.parseInt(stInfos.nextToken());
		}
		return result;
	}
	public class L1CraftInfo {
		public int craftId;
		public int itemId;
		public String name;
		public int descId;
		public int makeCount;
		public int enchant;
		public int bless;
		public int attr;
		public int probability;
		public ArrayList<Material> materialList;
	}
	public class Material { //하나의 재료.
		public int[] descIds;
		public int[] count;
		public int[] enchant;
		public int[] bless;
	}
}
