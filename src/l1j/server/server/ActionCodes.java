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

package l1j.server.server;

public class ActionCodes {

	public ActionCodes() {
	}

	public static final byte ACTION_Appear = 4;
	public static final byte ACTION_Hide = 11;
	public static final byte ACTION_AntharasHide = 20;

	public static final byte ACTION_Walk = 0;
	public static final byte ACTION_Attack = 1;
	public static final byte ACTION_Damage = 2;
	public static final byte ACTION_Idle = 3;
	public static final byte ACTION_SwordWalk = 4;
	public static final byte ACTION_SwordAttack = 5;
	public static final byte ACTION_SwordDamage = 6;
	public static final byte ACTION_SwordIdle = 7;
	public static final byte ACTION_Die = 8;
	public static final byte ACTION_AxeWalk = 11;
	public static final byte ACTION_AxeAttack = 12;
	public static final byte ACTION_AxeDamage = 13;
	public static final byte ACTION_AxeIdle = 14;
	public static final byte ACTION_HideDamage = 13;
	public static final byte ACTION_HideIdle = 14;
	public static final byte ACTION_Pickup = 15;
	public static final byte ACTION_Throw = 16;
	public static final byte ACTION_Wand = 17;
	public static final byte ACTION_SkillAttack = 18;
	public static final byte ACTION_SkillBuff = 19;
	public static final byte ACTION_BowWalk = 20;
	public static final byte ACTION_BowAttack = 21;
	public static final byte ACTION_BowDamage = 22;
	public static final byte ACTION_BowIdle = 23;
	public static final byte ACTION_SpearWalk = 24;
	public static final byte ACTION_SpearAttack = 25;
	public static final byte ACTION_SpearDamage = 26;
	public static final byte ACTION_SpearIdle = 27;
	public static final byte ACTION_On = 28;
	public static final byte ACTION_Off = 29;
	public static final byte ACTION_Open = 28;
	public static final byte ACTION_Close = 29;
	public static final byte ACTION_South = 28;
	public static final byte ACTION_West = 29;
	public static final byte ACTION_AltAttack = 30;
	public static final byte ACTION_SpellDirectionExtra = 31;
	public static final byte ACTION_TowerCrack1 = 33;
	public static final byte ACTION_TowerCrack2 = 34;
	public static final byte ACTION_TowerCrack3 = 35;
	public static final byte ACTION_TowerDie = 36;
	public static final byte ACTION_DoorAction1 = 33;
	public static final byte ACTION_DoorAction2 = 34;
	public static final byte ACTION_DoorAction3 = 35;
	public static final byte ACTION_DoorDie = 36;
	public static final byte ACTION_StaffWalk = 40;
	public static final byte ACTION_StaffAttack = 41;
	public static final byte ACTION_StaffDamage = 42;
	public static final byte ACTION_StaffIdle = 43;
	public static final byte ACTION_Moveup = 44;
	public static final byte ACTION_Movedown = 45;
	public static final byte ACTION_DaggerWalk = 46;
	public static final byte ACTION_DaggerAttack = 47;
	public static final byte ACTION_DaggerDamage = 48;
	public static final byte ACTION_DaggerIdle = 49;
	public static final byte ACTION_TwoHandSwordWalk = 50;
	public static final byte ACTION_TwoHandSwordAttack = 51;
	public static final byte ACTION_TwoHandSwordDamage = 52;
	public static final byte ACTION_TwoHandSwordIdle = 53;
	public static final byte ACTION_EdoryuWalk = 54;
	public static final byte ACTION_EdoryuAttack = 55;
	public static final byte ACTION_EdoryuDamage = 56;
	public static final byte ACTION_EdoryuIdle = 57;
	public static final byte ACTION_ClawWalk = 58;
	public static final byte ACTION_ClawAttack = 59;
	public static final byte ACTION_ClawIdle = 61;
	public static final byte ACTION_ClawDamage = 60;
	public static final byte ACTION_ThrowingKnifeWalk = 62;
	public static final byte ACTION_ThrowingKnifeAttack = 63;
	public static final byte ACTION_ThrowingKnifeDamage = 64;
	public static final byte ACTION_ThrowingKnifeIdle = 65;
	public static final byte ACTION_Think = 66; // Alt+4
	public static final byte ACTION_Aggress = 67; // Alt+3
	public static final byte ACTION_Salute = 68; // Alt+1
	public static final byte ACTION_Cheer = 69; // Alt+2
	public static final byte ACTION_Shop = 70;
	public static final byte ACTION_Fishing = 71;
	public static final byte ACTION_CHAINSWORD_Walk = 83;
	public static final byte ACTION_CHAINSWORD = 84;
}