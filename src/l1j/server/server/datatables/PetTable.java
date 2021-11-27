package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.utils.SQLUtil;

public class PetTable {

	private static Logger _log = Logger.getLogger(PetTable.class.getName());

	private static PetTable _instance;

	private final static HashMap<Integer, L1Pet> _pets = new HashMap<Integer, L1Pet>();

	public static PetTable getInstance() {
		if (_instance == null) {
			_instance = new PetTable();
		}
		return _instance;
	}

	private PetTable() {
		load();
	}

	private void load() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM pets");

			rs = pstm.executeQuery();
			L1Pet pet = null;
			while (rs.next()) {
				pet = new L1Pet();
				int itemobjid = rs.getInt(1);
				pet.setItemObjidd(itemobjid);
				pet.setObjid(rs.getInt(2));
				pet.setNpcId(rs.getInt(3));
				pet.setPetInfo(rs.getInt(4));
				pet.setName(rs.getString(5));
				pet.setLevel(rs.getInt(6));
				pet.setExp(rs.getInt(7));
				pet.setMaxHp(rs.getInt(8));
				pet.setCurrentHp(rs.getInt(9));
				pet.setHunt(rs.getInt(10));
				pet.setSurvival(rs.getInt(11));
				pet.setSacred(rs.getInt(12));
				pet.setBonusPoint(rs.getInt(13));
				pet.setElixir(rs.getInt(14));
				pet.setElixirHunt(rs.getInt(15));
				pet.setElixirSurvival(rs.getInt(16));
				pet.setElixirSacred(rs.getInt(17));
				pet.setFriendship(rs.getInt(18));
				pet.setFighting(rs.getInt(19));
				pet.setPetDead(rs.getBoolean(20));
				pet.setPetDeadExp(rs.getBoolean(21));
				pet.setProduct(rs.getBoolean(22));
				_pets.put(new Integer(itemobjid), pet);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}

	public void StoreNewPet(L1PetInstance pet, int objid, int itemobjid) {
		L1Pet Pet = new L1Pet();
		Pet.setItemObjidd(itemobjid);
		Pet.setObjid(objid);
		Pet.setNpcId(pet.getNpcId());
		Pet.setPetInfo(pet.getPetInfo());
		Pet.setName(pet.getNameId());
		Pet.setLevel(pet.getLevel());
		Pet.setExp(pet.getExp());
		Pet.setMaxHp(pet.getBaseMaxHp());
		Pet.setCurrentHp(pet.getCurrentHp());
		Pet.setHunt(0);
		Pet.setSurvival(0);
		Pet.setSacred(0);
		Pet.setBonusPoint(0);
		Pet.setElixir(0);
		Pet.setElixirHunt(0);
		Pet.setElixirSurvival(0);
		Pet.setElixirSacred(0);
		Pet.setFriendship(0);
		Pet.setFighting(0);
		Pet.setPetDead(false);
		Pet.setPetDeadExp(false);
		Pet.setProduct(true);
		_pets.put(new Integer(itemobjid), Pet);

		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO pets SET Item_Objid = ?, Objid = ?, Npcid = ?, PetInfo = ?, Name = ?, Level = ?, Exp = ?, MaxHp = ?, CurrentHp = ?, Hunt =?, Survival = ?, Sacred = ?, BonusPoint = ?, Elixir = ?,ElixirHunt = ?, ElixirSurvival = ?, ElixirSacred = ?, Friendship = ?, Fighting = ?, Death = ?, DeathExp = ?, Product = ?");
			pstm.setInt(1, Pet.getItemObjid());
			pstm.setInt(2, Pet.getObjid());
			pstm.setInt(3, Pet.getNpcId());
			pstm.setInt(4, Pet.getPetInfo());
			pstm.setString(5, Pet.getName());
			pstm.setInt(6, Pet.getLevel());
			pstm.setInt(7, Pet.getExp());
			pstm.setInt(8, pet.getBaseMaxHp());
			pstm.setInt(9, Pet.getCurrentHp());
			pstm.setInt(10, Pet.getHunt());
			pstm.setInt(11, Pet.getSurvival());
			pstm.setInt(12, Pet.getSacred());
			pstm.setInt(13, Pet.getBonusPoint());
			pstm.setInt(14, Pet.getElixir());
			pstm.setInt(15, Pet.getElixirHunt());
			pstm.setInt(16, Pet.getElixirSurvival());
			pstm.setInt(17, Pet.getElixirSacred());
			pstm.setInt(18, Pet.getFriendship());
			pstm.setInt(19, Pet.getFighting());
			pstm.setString(20, Pet.isPetDead() ? "true" : "false");
			pstm.setString(21, Pet.isPetDeadExp() ? "true" : "false");
			pstm.setString(22, Pet.isProduct() ? "true" : "false");
			pstm.executeUpdate();
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}
	
	/** 펫 정보 업데이트용 전체 업데이트 */
	public static void UpDatePet(L1PetInstance Pet) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE pets SET Objid = ?, Npcid = ?, PetInfo = ?, Name = ?, Level = ?, Exp = ?, MaxHp = ?, CurrentHp = ?, Hunt =?, Survival = ?, Sacred = ?, BonusPoint = ?, Elixir = ?, ElixirHunt = ?, ElixirSurvival = ?, ElixirSacred = ?, Friendship = ?, Fighting = ?, Death = ?, DeathExp = ?, Product = ? WHERE Item_Objid=?");
			pstm.setInt(1, Pet.getId());
			pstm.setInt(2, Pet.getNpcId());
			pstm.setInt(3, Pet.getPetInfo());
			pstm.setString(4, Pet.getName());
			pstm.setInt(5, Pet.getLevel());
			pstm.setInt(6, Pet.getExp());
			pstm.setInt(7, Pet.getBaseMaxHp());
			pstm.setInt(8, Pet.getCurrentHp());
			pstm.setInt(9, Pet.getHunt());
			pstm.setInt(10, Pet.getSurvival());
			pstm.setInt(11, Pet.getSacred());
			pstm.setInt(12, Pet.getBonusPoint());
			pstm.setInt(13, Pet.getElixir());
			pstm.setInt(14, Pet.getElixirHunt());
			pstm.setInt(15, Pet.getElixirSurvival());
			pstm.setInt(16, Pet.getElixirSacred());
			pstm.setInt(17, Pet.getFriendship());
			pstm.setInt(18, Pet.getFighting());
			pstm.setString(19, Pet.isDead() ? "true" : "false");
			pstm.setString(20, Pet.isDeadExp() ? "true" : "false");
			pstm.setString(21, Pet.isProduct() ? "true" : "false");
			pstm.setInt(22, Pet.getItemObjId());
			pstm.executeUpdate();
			
			/** 펫 정보도 갱신 해줘야함 */
			L1Pet UpDatePet = getTemplate(Pet.getItemObjId());
			UpDatePet.setObjid(Pet.getId());
			UpDatePet.setNpcId(Pet.getNpcId());
			UpDatePet.setPetInfo(Pet.getPetInfo());
			UpDatePet.setName(Pet.getName());
			UpDatePet.setLevel(Pet.getLevel());
			UpDatePet.setExp(Pet.getExp());
			UpDatePet.setMaxHp(Pet.getBaseMaxHp());
			UpDatePet.setCurrentHp(Pet.getCurrentHp());
			UpDatePet.setHunt(Pet.getHunt());
			UpDatePet.setSurvival(Pet.getSurvival());
			UpDatePet.setSacred(Pet.getSacred());
			UpDatePet.setBonusPoint(Pet.getBonusPoint());
			UpDatePet.setElixir(Pet.getElixir());
			UpDatePet.setElixirHunt(Pet.getElixirHunt());
			UpDatePet.setElixirSurvival(Pet.getElixirSurvival());
			UpDatePet.setElixirSacred(Pet.getElixirSacred());
			UpDatePet.setFriendship(Pet.getFriendship());
			UpDatePet.setFighting(Pet.getFighting());
			UpDatePet.setPetDead(Pet.isDead());
			UpDatePet.setPetDeadExp(Pet.isDeadExp());
			UpDatePet.setProduct(Pet.isProduct());
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	/**
	 * Pets 테이블에 이미 이름이 존재할까를 돌려준다.
	 * 
	 * @param nameCaseInsensitive
	 *            조사하는 애완동물의 이름. 대문자 소문자의 차이는 무시된다.
	 * @return 이미 이름이 존재하면 true
	 */
	public static boolean isNameExists(String nameCaseInsensitive) {
		String nameLower = nameCaseInsensitive.toLowerCase();
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT Item_Objid FROM pets WHERE LOWER(name)=?");
			pstm.setString(1, nameLower);
			rs = pstm.executeQuery();
			if (!rs.next()) return false;
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return true;
	}

	public void deletePet(int itemobjid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM pets WHERE Item_Objid=?");
			pstm.setInt(1, itemobjid);
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		_pets.remove(itemobjid);
	}


	public static L1Pet getTemplate(int itemobjid) {
		return _pets.get(new Integer(itemobjid));
	}
	
	public L1Pet[] getPetTableList() {
		return _pets.values().toArray(new L1Pet[_pets.size()]);
	}
}
