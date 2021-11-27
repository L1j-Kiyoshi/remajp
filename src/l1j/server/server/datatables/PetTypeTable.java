package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.L1PetSkill;
import l1j.server.server.templates.L1PetType;
import l1j.server.server.utils.IntRange;
import l1j.server.server.utils.SQLUtil;

public class PetTypeTable {
	private static PetTypeTable _instance;
	
	private Map<Integer, L1PetType> _types = new HashMap<Integer, L1PetType>();
	
	private Map<Integer, Integer> _PetGetItem = new HashMap<Integer, Integer>();
	
	public static void load() {
		_instance = new PetTypeTable();
	}

	public static PetTypeTable getInstance() {
		return _instance;
	}

	private PetTypeTable() {
		loadTypes();
	}

	private void loadTypes() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM pettypes");
			rs = pstm.executeQuery();
			IntRange hpUpRange = null;
			StringTokenizer SkillStep;
			while (rs.next()) {
				int PetGetItem = rs.getInt("PetGetItem");
				int baseNpcId = rs.getInt("BaseNpcId");
				String name = rs.getString("Name");
				int PetInfo = rs.getInt("PetInfo");
				int PetEffect = rs.getInt("PetEffect");
				int hpUpMin = rs.getInt("HpUpMin");
				int hpUpMax = rs.getInt("HpUpMax");
				
				SkillStep = new StringTokenizer(rs.getString("SkillOneStep"));


				ArrayList<L1PetSkill> SkillOneStep = new ArrayList<L1PetSkill>();
				while (SkillStep.hasMoreElements()) {
					StringTokenizer SkillStepTemp = new StringTokenizer(SkillStep.nextToken(), ",");
					while (SkillStepTemp.hasMoreElements()) {
						int Number = Integer.parseInt(SkillStepTemp.nextToken());
						SkillOneStep.add(new L1PetSkill(Number, 0));
					}
				}
				
				SkillStep = new StringTokenizer(rs.getString("SkillTwoStep"));
				ArrayList<L1PetSkill> SkillTwoStep = new ArrayList<L1PetSkill>();
				while (SkillStep.hasMoreElements()) {
					StringTokenizer SkillStepTemp = new StringTokenizer(SkillStep.nextToken(), ",");
					while (SkillStepTemp.hasMoreElements()) {
						int Number = Integer.parseInt(SkillStepTemp.nextToken());
						SkillTwoStep.add(new L1PetSkill(Number, 0));
					}
				}
				
				SkillStep = new StringTokenizer(rs.getString("SkillThreeStep"));
				ArrayList<L1PetSkill> SkillThreeStep = new ArrayList<L1PetSkill>();
				while (SkillStep.hasMoreElements()) {
					StringTokenizer SkillStepTemp = new StringTokenizer(SkillStep.nextToken(), ",");
					while (SkillStepTemp.hasMoreElements()) {
						int Number = Integer.parseInt(SkillStepTemp.nextToken());
						SkillThreeStep.add(new L1PetSkill(Number, 0));
					}
				}
				
				SkillStep = new StringTokenizer(rs.getString("SkillFourStep"));
				ArrayList<L1PetSkill> SkillFourStep = new ArrayList<L1PetSkill>();
				while (SkillStep.hasMoreElements()) {
					StringTokenizer SkillStepTemp = new StringTokenizer(SkillStep.nextToken(), ",");
					while (SkillStepTemp.hasMoreElements()) {
						int Number = Integer.parseInt(SkillStepTemp.nextToken());
						SkillFourStep.add(new L1PetSkill(Number, 0));
					}
				}
				
				SkillStep = new StringTokenizer(rs.getString("SkillFiveStep"));
				ArrayList<L1PetSkill> SkillFiveStep = new ArrayList<L1PetSkill>();
				while (SkillStep.hasMoreElements()) {
					StringTokenizer SkillStepTemp = new StringTokenizer(SkillStep.nextToken(), ",");
					while (SkillStepTemp.hasMoreElements()) {
						int Number = Integer.parseInt(SkillStepTemp.nextToken());
						SkillFiveStep.add(new L1PetSkill(Number, 0));
					}
				}
				
				hpUpRange = new IntRange(hpUpMin, hpUpMax);
				_types.put(baseNpcId, new L1PetType(baseNpcId, name, PetInfo, PetEffect, hpUpRange, 
						SkillOneStep, SkillTwoStep, SkillThreeStep, SkillFourStep, SkillFiveStep));
				_PetGetItem.put(PetGetItem, baseNpcId);
			}
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public L1PetType get(int baseNpcId) {
		return _types.get(baseNpcId);
	}

	public L1PetType PetGetItem(int PetGetItem) {
		int NpcId = _PetGetItem.get(PetGetItem);
		return _types.get(NpcId);
	}
}