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
package l1j.server.server.templates;

public class L1Armor extends L1Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1Armor() {
	}

	private int _ac = 0;
	private int _damageReduction = 0;
	private int _weightReduction = 0;
	private int _Hitup = 0; // ● 근접무기 명중률
	private int _Dmgup = 0; // ● 근접무기 추타율
	private int _bowHitup = 0; // ● 활의 명중율
	private int _bowDmgup = 0; // ● 활의 추타율

	private int _defense_water = 0;
	private int _defense_wind = 0;
	private int _defense_fire = 0;
	private int _defense_earth = 0;

	private int _regist_calcPcDefense = 0;
	private int _regist_PVPweaponTotalDamage = 0;

	@Override
	public int get_ac() {
		return _ac;
	}

	public void set_ac(int i) {
		this._ac = i;
	}

	@Override
	public int getDamageReduction() {
		return _damageReduction;
	}

	public void setDamageReduction(int i) {
		_damageReduction = i;
	}

	@Override
	public int getWeightReduction() {
		return _weightReduction;
	}

	public void setWeightReduction(int i) {
		_weightReduction = i;
	}

	@Override
	public int getHitup() {
		return _Hitup;
	}

	public void setHitup(int i) {
		_Hitup = i;
	}

	@Override
	public int getDmgup() {
		return _Dmgup;
	}

	public void setDmgup(int i) {
		_Dmgup = i;
	}

	@Override
	public int getBowHitup() {
		return _bowHitup;
	}

	public void setBowHitup(int i) {
		_bowHitup = i;
	}

	@Override
	public int getBowDmgup() {
		return _bowDmgup;
	}

	public void setBowDmgup(int i) {
		_bowDmgup = i;
	}

	@Override
	public int get_defense_water() {
		return this._defense_water;
	}

	public void set_defense_water(int i) {
		_defense_water = i;
	}

	@Override
	public int get_defense_wind() {
		return this._defense_wind;
	}

	public void set_defense_wind(int i) {
		_defense_wind = i;
	}

	@Override
	public int get_defense_fire() {
		return this._defense_fire;
	}

	public void set_defense_fire(int i) {
		_defense_fire = i;
	}

	@Override
	public int get_defense_earth() {
		return this._defense_earth;
	}

	public void set_defense_earth(int i) {
		_defense_earth = i;
	}

	/** 잊섬아이템 리뉴얼 PVP데미지리덕션 **/
	@Override
	public int get_regist_calcPcDefense() {
		return this._regist_calcPcDefense;
	}

	public void set_regist_calcPcDefense(int i) {
		_regist_calcPcDefense = i;
	}

	/** 잊섬아이템 리뉴얼 PVP추가데미지 **/
	@Override
	public int get_regist_PVPweaponTotalDamage() {
		return this._regist_PVPweaponTotalDamage;
	}

	public void set_regist_PVPweaponTotalDamage(int i) {
		_regist_PVPweaponTotalDamage = i;
	}

	@Override
	public boolean isStackable() {
		boolean ck = false;
		if (getItemId() >= 427123 && getItemId() <= 427140) // 봉인 유물, 룬 겹치게
			ck = true;
		return ck;
	}
}