package l1j.server.server.templates;

/** 펫 관련 정보 저장 시스템 */
public class L1Pet {
	public L1Pet() {}

	private int _ItemObjid;

	public int getItemObjid() {
		return _ItemObjid;
	}

	public void setItemObjidd(int i) {
		_ItemObjid = i;
	}

	private int _Objid;

	public int getObjid() {
		return _Objid;
	}

	public void setObjid(int i) {
		_Objid = i;
	}

	private int _NpcId;

	public int getNpcId() {
		return _NpcId;
	}

	public void setNpcId(int i) {
		_NpcId = i;
	}
	
	/** 펫 관련 이미지 넘버 저장 */
	private int _PetInfo;

	public int getPetInfo() {
		return _PetInfo;
	}

	public void setPetInfo(int i) {
		_PetInfo = i;
	}

	private String _Name;

	public String getName() {
		return _Name;
	}

	public void setName(String s) {
		_Name = s;
	}

	private int _Level;

	public int getLevel() {
		return _Level;
	}

	public void setLevel(int i) {
		_Level = i;
	}
	
	private int _Exp;

	public int getExp() {
		return _Exp;
	}

	public void setExp(int i) {
		_Exp = i;
	}


	private int _MaxHp;

	public int getMaxHp() {
		return _MaxHp;
	}

	public void setMaxHp(int i) {
		_MaxHp = i;
	}

	private int _CurrentHp;

	public int getCurrentHp() {
		return _CurrentHp;
	}

	public void setCurrentHp(int i) {
		_CurrentHp = i;
	}
	
	private int _Hunt;

	public int getHunt() {
		return _Hunt;
	}

	public void setHunt(int i) {
		_Hunt = i;
	}

	private int _Survival;

	public int getSurvival() {
		return _Survival;
	}

	public void setSurvival(int i) {
		_Survival = i;
	}
	
	private int _Sacred;

	public int getSacred() {
		return _Sacred;
	}

	public void setSacred(int i) {
		_Sacred = i;
	}

	private int _BonusPoint;

	public int getBonusPoint() {
		return _BonusPoint;
	}

	public void setBonusPoint(int i) {
		_BonusPoint = i;
	}
	
	private int _Elixir;

	public int getElixir() {
		return _Elixir;
	}

	public void setElixir(int i) {
		_Elixir = i;
	}
	
	/** 펫 관력 메소드들 */
	private int _ElixirHunt;

	public int getElixirHunt() {
		return _ElixirHunt;
	}

	public void setElixirHunt(int i) {
		_ElixirHunt = i;
	}

	private int _ElixirSurvival;

	public int getElixirSurvival() {
		return _ElixirSurvival;
	}

	public void setElixirSurvival(int i) {
		_ElixirSurvival = i;
	}
	
	private int _ElixirSacred;

	public int getElixirSacred() {
		return _ElixirSacred;
	}

	public void setElixirSacred(int i) {
		_ElixirSacred = i;
	}
	
	/** 우정 포인트 */
	private int _Friendship;

	public int getFriendship() {
		return _Friendship;
	}

	public void setFriendship(int i) {
		_Friendship = i;
	}
	
	/** 투지 포인트 */
	private int _Fighting;

	public int getFighting() {
		return _Fighting;
	}

	public void setFighting(int i) {
		_Fighting = i;
	}
	
	/** 펫이 다이 상태이면 리스하고 접속해도 팻 정보에 죽음 효과 표기 */
	private boolean _PetDead;
	
	public boolean isPetDead() {
		return _PetDead;
	}

	public void setPetDead(boolean flag) {
		_PetDead = flag;
	}
	
	/** 펫이 다이 상태이면 리스하고 접속해도 팻 정보에 죽음 효과 표기 */
	private boolean _PetDeadExp;
	
	public boolean isPetDeadExp() {
		return _PetDeadExp;
	}

	public void setPetDeadExp(boolean flag) {
		_PetDeadExp = flag;
	}
	
	/** 망각 부분 거래 가능하도록 세팅 하는거 */
	private boolean _Product;
	
	public boolean isProduct() {
		return _Product;
	}

	public void setProduct(boolean flag) {
		_Product = flag;
	}
}