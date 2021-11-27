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

public class L1Weapon extends L1Item {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public L1Weapon() {
	}

	private int _defense_water = 0;
	private int _defense_wind = 0;
	private int _defense_fire = 0;
	private int _defense_earth = 0;
	private int _range = 0;

	@Override
	public int getRange() {
		return _range;
	}

	public void setRange(int i) {
		_range = i;
	}

	private int _hitModifier = 0;

	@Override
	public int getHitModifier() {
		return _hitModifier;
	}

	public void setHitModifier(int i) {
		_hitModifier = i;
	}

	private int _dmgModifier = 0;

	@Override
	public int getDmgModifier() {
		return _dmgModifier;
	}

	public void setDmgModifier(int i) {
		_dmgModifier = i;
	}
	//추가
	private int _addDmg = 0;
	 
	 @Override
	 public int getaddDmg() {
	  return _addDmg;
	 }
	 public void set_addDmg(int i){
	  _addDmg = i;
	 }
	private int _doubleDmgChance;

	@Override
	public int getDoubleDmgChance() {
		return _doubleDmgChance;
	}

	public void setDoubleDmgChance(int i) {
		_doubleDmgChance = i;
	}

	private int _magicDmgModifier = 0;

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

	@Override
	public int getMagicDmgModifier() {
		return _magicDmgModifier;
	}

	public void setMagicDmgModifier(int i) {
		_magicDmgModifier = i;
	}

	private int _canbedmg = 0;

	@Override
	public int get_canbedmg() {
		return _canbedmg;
	}

	public void set_canbedmg(int i) {
		_canbedmg = i;
	}

	@Override
	public boolean isTwohandedWeapon() {
		int weapon_type = getType();

		boolean bool = (weapon_type == 3 || weapon_type == 4
				|| weapon_type == 5 || weapon_type == 11 || weapon_type == 12
				|| weapon_type == 15 || weapon_type == 16 || weapon_type == 18 || getItemId() == 273); // 베테르랑
																										// 키링크

		return bool;
	}
}
