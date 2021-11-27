package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PetWindow;
import l1j.server.server.utils.SQLUtil;

public class CharPetBuffTable {
	private CharPetBuffTable() {}

	/** 펫관련 스킬 저장용 체크 */
	public static final int[] PetBuffSkill = { L1SkillId.YeagerNight, L1SkillId.GrowthFoliage, L1SkillId.DogBlood };

	public static void SaveBuff(L1PetInstance Pet) {
		DeleteBuff(Pet.getMaster().getId());
		for (int SkillId : PetBuffSkill) {
			int TimeSec = Pet.getSkillEffectTimerSet().getSkillEffectTimeSec(SkillId);
			if (TimeSec > 0) StoreBuff(Pet.getMaster().getId(), SkillId, TimeSec);
		}
	}
	
	public static void DeleteBuff(int objId) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buff_pet WHERE Objid=?");
			pstm.setInt(1, objId);
			pstm.executeUpdate();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private static void StoreBuff(int objId, int skillId, int time) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_buff_pet SET Objid=?, PetBuff=?, PetBuffTime=?");
			pstm.setInt(1, objId);
			pstm.setInt(2, skillId);
			pstm.setInt(3, time);
			pstm.executeUpdate();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public static void LoadBuff(L1PetInstance Pet) {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM character_buff_pet WHERE Objid=?");
			pstm.setInt(1, Pet.getMaster().getId());
			rs = pstm.executeQuery();
			while(rs.next()){
				int PetBuff = rs.getInt("PetBuff"), PetBuffTime = rs.getInt("PetBuffTime");
				Pet.getSkillEffectTimerSet().setSkillEffect(PetBuff, PetBuffTime * 1000);
				L1PcInstance Pc = (L1PcInstance) Pet.getMaster();
				Pc.sendPackets(new S_PetWindow(PetBuff, PetBuffTime), true);
			}
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
