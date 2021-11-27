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

public class S_CharPass extends ServerBasePacket {
	public static final int _케릭선택창진입 = 0x40;
	public static final int _케릭선택창진입2 = 0x0a;
	public static final int _케릭선택창진입3 = 0x16;
	public static final int _비번생성창 = 0x17;
	public static final int _비번생성완료창 = 0x11;
	public static final int _비번입력창 = 0x14;
	public static final int _비번입력비번틀림 = 0x15;
	public static final int _비번변경답변 = 0x13;
	public static final int _비번인증완료 = 0x3f;

	public S_CharPass() {
		writeC(Opcodes.S_NOTICE);// 105
		writeC(0xB5);
		writeC(0x01);
	}

	public S_CharPass(int val) {
		writeC(Opcodes.S_VOICE_CHAT);// 105
		writeC(val);
		switch (val) {

		case _비번입력비번틀림:
			writeD(0xa5);
			writeH(0x01);
			writeH(0x05);
			writeD(0);
			break;
		case _비번생성완료창:
			writeD(0);
			break;
		case _케릭선택창진입2:
			writeD(2);
			break;
		case _케릭선택창진입3:
			writeD(170);
			writeD(0);
			writeD(0);
			writeH(0);
			writeC(1);
			writeC(0);
			break;
		default:
			break;
		}
	}

	public S_CharPass(int val, boolean ck) {
		writeC(Opcodes.S_VOICE_CHAT);// 105
		writeC(val);
		switch (val) {
		case _비번변경답변:
			// fe 13 00 00 00 00 00 00 05 00 00 00 00 00 ..............
			if (ck) {
				writeD(0);
				writeH(0);
				writeH(0x05);
				writeD(0);
			} else {
				writeD(0xa5);
				writeH(0x01);
				writeH(0x05);
				writeD(0);
			}
			break;
		case _케릭선택창진입3:
			if (ck) {
				writeD(0);
				writeH(0);
				writeD(0x05);
				writeD(0);
				writeH(0x01);
			} else {
				writeD(170);
				writeD(0);
				writeD(0);
				writeH(0);
				writeH(1);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	public String getType() {
		return "[S] S_Test";
	}

	@SuppressWarnings("unused")
	private static final String _S__19_Test = "[S] S_Test";
}
