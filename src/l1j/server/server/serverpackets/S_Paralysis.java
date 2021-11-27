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

package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_Paralysis extends ServerBasePacket {
	public S_Paralysis(int type) {
		writeC(type);
		writeC(2);
		writeH(0);
	}

	public S_Paralysis(int type, boolean flag, int time) {
		writeC(Opcodes.S_PARALYSE);
		if (type == TYPE_PARALYSIS) // 체가 완전하게 마비되었습니다.
		{
			if (flag == true) {
				writeC(2);
			} else {
				writeC(3);
			}
		} else if (type == TYPE_PARALYSIS2) // 체가 완전하게 마비되었습니다.
		{
			if (flag == true) {
				writeC(4);
			} else {
				writeC(5);
			}
		} else if (type == TYPE_TELEPORT_UNLOCK) // 텔레포트 대기 상태의 해제
		{
			writeC(7);
			// 8 ,9 독 마비? 시간 288?
		} else if (type == TYPE_SLEEP) // 강력한 수마가 덮쳐 와, 자 버렸습니다.
		{
			if (flag == true) {
				writeC(10);
			} else {
				writeC(11);
			}
		} else if (type == TYPE_FREEZE) // 체가 얼었습니다.
		{
			if (flag == true) {
				writeC(12);
			} else {
				writeC(13);
			}
		} else if (type == TYPE_STUN) // 스탠 상태입니다.
		{
			if (flag == true) {
				writeC(22);
			} else {
				writeC(23);
			}
		} else if (type == TYPE_BIND) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(24);
			} else {
				writeC(25);
			}

		} else if (type == TYPE_POWER_GRIP) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(26);
			} else {
				writeC(27);
			}
		} else if (type == test1) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(28);
			} else {
				writeC(29);
			}

		} else if (type == TYPE_DEATH_PERADO) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(30);
			} else {
				writeC(31);
			}
		} else if (type == test2) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(32);
			} else {
				writeC(33);
			}
		} else if (type == test3) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(34);
			} else {
				writeC(35);
			}
		} else if (type == test4) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(36);
			} else {
				writeC(37);
			}
		} else if (type == test5) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(38);
			} else {
				writeC(39);
			}
		} else if (type == test6) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(40);
			} else {
				writeC(41);
			}
		} else if (type == test7) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(42);
			} else {
				writeC(43);
			}
		} else if (type == test8) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(44);
			} else {
				writeC(45);
			}
		} else if (type == test9) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(46);
			} else {
				writeC(47);
			}
		} else if (type == test10) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(48);
			} else {
				writeC(49);
			}
		}
		writeH(time); // 시간??
	}

	public S_Paralysis(int type, boolean flag) {
		writeC(Opcodes.S_PARALYSE);
		if (type == TYPE_PARALYSIS) // 체가 완전하게 마비되었습니다.
		{
			if (flag == true) {
				writeC(2);
			} else {
				writeC(3);
			}
		} else if (type == TYPE_PARALYSIS2) // 체가 완전하게 마비되었습니다.
		{
			if (flag == true) {
				writeC(4);
			} else {
				writeC(5);
			}
		} else if (type == TYPE_TELEPORT_UNLOCK) // 텔레포트 대기 상태의 해제
		{
			writeC(7);
			// 8 ,9 독 마비? 시간 288?
		} else if (type == TYPE_SLEEP) // 강력한 수마가 덮쳐 와, 자 버렸습니다.
		{
			if (flag == true) {
				writeC(10);
			} else {
				writeC(11);
			}
		} else if (type == TYPE_FREEZE) // 체가 얼었습니다.
		{
			if (flag == true) {
				writeC(12);
			} else {
				writeC(13);
			}
		} else if (type == TYPE_STUN) // 스탠 상태입니다.
		{
			if (flag == true) {
				writeC(22);
			} else {
				writeC(23);
			}
		} else if (type == TYPE_BIND) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(24);
			} else {
				writeC(25);
			}

		} else if (type == TYPE_POWER_GRIP) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(26);
			} else {
				writeC(27);
			}
		} else if (type == test1) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(28);
			} else {
				writeC(29);
			}

		} else if (type == TYPE_DEATH_PERADO) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(30);
			} else {
				writeC(31);
			}
		} else if (type == test2) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(32);
			} else {
				writeC(33);
			}
		} else if (type == test3) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(34);
			} else {
				writeC(35);
			}
		} else if (type == test4) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(36);
			} else {
				writeC(37);
			}
		} else if (type == test5) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(38);
			} else {
				writeC(39);
			}
		} else if (type == test6) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(40);
			} else {
				writeC(41);
			}
		} else if (type == test7) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(42);
			} else {
				writeC(43);
			}
		} else if (type == test8) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(44);
			} else {
				writeC(45);
			}
		} else if (type == test9) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(46);
			} else {
				writeC(47);
			}
		} else if (type == test10) // 다리가 속박된 것처럼 움직일 수 없습니다.
		{
			if (flag == true) {
				writeC(48);
			} else {
				writeC(49);
			}
		} else
			writeH(0x00); // 시간??
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__2F_PARALYSIS;
	}

	public static final int TYPE_PARALYSIS = 1;

	public static final int TYPE_PARALYSIS2 = 2;

	public static final int TYPE_SLEEP = 3;

	public static final int TYPE_FREEZE = 4;

	public static final int TYPE_STUN = 5;

	public static final int TYPE_BIND = 6;

	public static final int TYPE_TELEPORT_UNLOCK = 7;

	public static final int TYPE_POWER_GRIP = 8;
	public static final int TYPE_DEATH_PERADO = 9;
	
	public static final int test1 = 10;
	public static final int test2 = 11;
	public static final int test3 = 12;
	public static final int test4 = 13;
	public static final int test5 = 14;
	public static final int test6 = 15;
	public static final int test7 = 16;
	public static final int test8 = 17;
	public static final int test9 = 18;
	public static final int test10 = 19;

	private static final String _S__2F_PARALYSIS = "[S] S_Paralysis";
}
