package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.templates.L1PetSkill;
import l1j.server.server.utils.SQLUtil;

public class PetsSkillsTable {
	
	public static void SaveSkills(L1PetInstance Pet, boolean End) {
		for (L1PetSkill Skills : Pet.getPetSkillsList().values()) {
			if(SkillsCheck(Pet.getId(), Skills.getSkillNumber())){
				SkillsUpdate(Pet.getId(), Skills.getSkillNumber(), Skills.getSkillLevel());
			}else SkillsStore(Pet.getId(), Skills.getSkillNumber(), Skills.getSkillLevel());	
		}
		if(End) Pet.RemovePetSkills();
	}
	
	private static boolean SkillsCheck(int Objid, int SkillNumbe) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		boolean SkillsCheck = false;
		try {			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM pets_skills WHERE Objid=?");
			pstm.setInt(1, Objid);
			rs = pstm.executeQuery();
			while(rs.next()){
				int Result = rs.getInt("SkillNumber");
				if(Result == SkillNumbe) SkillsCheck = true;
			}
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return SkillsCheck;
	}
	
	private static void SkillsStore(int Objid, int SkillNumbe, int SkillLevel) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO pets_skills SET Objid=?, SkillNumber=?, SkillLevel=?");
			pstm.setInt(1, Objid);
			pstm.setInt(2, SkillNumbe);
			pstm.setInt(3, SkillLevel);
			pstm.executeUpdate();
		} catch (Exception e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private static void SkillsUpdate(int Objid, int SkillNumbe, int SkillLevel) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE pets_skills SET SkillLevel=? WHERE Objid=? AND SkillNumber=?");
			pstm.setInt(1, SkillLevel);
			pstm.setInt(2, Objid);
			pstm.setInt(3, SkillNumbe);
			pstm.executeUpdate();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void SkillsDelete(int objId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM pets_skills WHERE Objid=?");
			pstm.setInt(1, objId);
			pstm.executeUpdate();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static void LoadSkills(L1PetInstance Pet) {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM pets_skills WHERE Objid=?");
			pstm.setInt(1, Pet.getId());
			rs = pstm.executeQuery();
			while(rs.next()){
				int SkillNumber = rs.getInt("SkillNumber"), SkillLevel = rs.getInt("SkillLevel");
				Pet.setPetSkills(SkillNumber, new L1PetSkill(SkillNumber, SkillLevel));
			}
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
