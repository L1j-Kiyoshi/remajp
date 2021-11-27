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

import java.util.logging.Logger;

import l1j.server.server.ActionCodes;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1DoorInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_DoorPack

public class S_DoorPack extends ServerBasePacket {

	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(S_DoorPack.class.getName());
	private static final String S_DOOR_PACK = "[S] S_DoorPack";

	private static final int STATUS_POISON = 1;
	/*
	 * private static final int STATUS_INVISIBLE = 2; private static final int
	 * STATUS_PC = 4; private static final int STATUS_FREEZE = 8; private static
	 * final int STATUS_BRAVE = 16; private static final int STATUS_ELFBRAVE =
	 * 32; private static final int STATUS_FASTMOVABLE = 64; private static
	 * final int STATUS_GHOST = 128;
	 */

	private byte[] _byte = null;

	public S_DoorPack(L1DoorInstance door) {
		buildPacket(door);
	}

	private void buildPacket(L1DoorInstance door) {
		writeC(Opcodes.S_PUT_OBJECT);
		writeH(door.getX());
		writeH(door.getY());
		writeD(door.getId());
		writeH(door.getGfxId().getGfxId());

		int doorStatus = door.getActionStatus();
		int openStatus = door.getOpenStatus();

		if (door.isDead())
			writeC(doorStatus);
		else if (openStatus == ActionCodes.ACTION_Open)
			writeC(openStatus);
		else if (door.getMaxHp() > 1 && doorStatus != 0)
			writeC(doorStatus);
		else
			writeC(openStatus);

		if (door.getGfxId().getGfxId() >= 12127
				&& door.getGfxId().getGfxId() <= 12133) {
			writeC(0x04);
			writeC(0x00);
			writeC(0x00);
			writeD(0x01);
			writeH(0);
			writeS("외성문");
			writeD(0x00);
			writeH(0x00);
			writeD(0xA2B3CAB6);
			writeC(0xC0);
			writeH(0xC5FC);
			writeD(0x000000F5);
			writeC(0xFF);
			writeH(0x00);
			writeH(0xFFFF);
			writeC(0x00);
			writeC(0x05);
			writeH(0x00);
		} else {

			writeC(0);
			writeC(0);
			writeC(0);
			writeD(1);
			writeH(0);
			if (door.getNpcTemplate().get_npcId() == 100852
					|| door.getNpcTemplate().get_npcId() == 100853
					|| door.getNpcTemplate().get_npcId() == 100854
					|| door.getNpcTemplate().get_npcId() == 100855) {
				writeS("피닉스의 알");
			} else {
				writeS(null);
			}

			writeS(null);

			int status = 0;
			if (door.getPoison() != null) {
				if (door.getPoison().getEffectId() == 1) {
					status |= STATUS_POISON;
				}
			}

			writeC(status);
			writeD(0);
			writeS(null);
			writeS(null);
			writeC(0);
			writeC(0xFF);
			writeC(0);
			writeC(0);
			writeC(0);
			writeC(0xFF);
			writeC(0xFF);
		}
	}

	/*
	 * 
	 * [서버패킷] [2013-10-11 오후 9:36:23:034] [OPCODE = 176] 0000: b0 58 83 de 7f e1
	 * 43 00 00 63 2f 1d 04 00 00 01 .X...C..c/..... 0010: 00 00 00 00 00 24 34
	 * 34 30 00 00 00 00 00 00 00 .....$440....... 0020: b6 ca b3 a2 c0 fc c5 f5
	 * 00 00 00 ff 00 00 ff ff ................ 0030: 00 05 00 00 ....
	 * 
	 * 
	 * [서버패킷] [2013-10-11 오후 9:36:26:716] [OPCODE = 176] 0000: b0 60 83 de 7f e0
	 * 43 00 00 61 2f 24 04 00 00 01 .`...C..a/$.... 0010: 00 00 00 00 00 24 34
	 * 34 30 00 00 00 00 00 00 00 .....$440....... 0020: b6 ca b3 a2 c0 fc c5 f5
	 * 00 00 00 ff 00 00 ff ff ................ 0030: 00 05 39 e6
	 * 
	 * [서버패킷] [2013-10-11 오후 9:36:28:563] [OPCODE = 176] 0000: b0 69 83 de 7f e2
	 * 43 00 00 65 2f 1d 04 00 00 01 .i...C..e/..... 0010: 00 00 00 00 00 24 34
	 * 34 30 00 00 00 00 00 00 00 .....$440....... 0020: b6 ca b3 a2 c0 fc c5 f5
	 * 00 00 00 ff 00 00 ff ff ................ 0030: 00 05 91 78 ...x
	 */

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}

	@Override
	public String getType() {
		return S_DOOR_PACK;
	}

}
