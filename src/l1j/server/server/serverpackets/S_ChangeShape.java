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
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket, S_SkillIconGFX, S_CharVisualUpdate

public class S_ChangeShape extends ServerBasePacket {

	private byte[] _byte = null;
	private static final String _S_ChangeShape = "[S] S_ChangeShape";

	public S_ChangeShape(int objId, int polyId) {
		buildPacket(objId, polyId, 0);
	}

	public S_ChangeShape(int objId, int polyId, int weaponTakeoff) {
		buildPacket(objId, polyId, weaponTakeoff);
	}

	private void buildPacket(int objId, int polyId, int weaponTakeoff) {
		writeC(Opcodes.S_POLYMORPH);
		writeD(objId);
		writeH(polyId);
		// 왜 29인가 불명
		if (polyId == 3784 || polyId == 6137 || polyId == 6142
				|| polyId == 6147 || polyId == 6152 || polyId == 6157
				|| polyId == 9205 || polyId == 9206 || polyId == 13152
				|| polyId == 13153 || polyId == 12702 || polyId == 12681
				|| polyId == 15154 || polyId == 15232 || polyId == 14923 || polyId == 15223
				|| polyId == 8812 || polyId == 8817 || polyId == 6267
				|| polyId == 6270 || polyId == 6273 || polyId == 6276
				|| polyId == 16014  || polyId == 15986 || polyId == 16027 
				|| polyId == 16284 || polyId == 16053
				|| polyId == 16040 || polyId == 17541 || polyId == 17515 || polyId == 17531) {
			L1Object ob = L1World.getInstance().findObject(objId);
			if (ob != null && ob instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) ob;
				if (weaponTakeoff == 24 && pc.getWeapon() != null && pc.getWeapon().getItem().getType() == 18) {
					if (polyId == 13152 || polyId == 13153 || polyId == 12702
							|| polyId == 12681 || polyId == 8812 || polyId == 15154 || polyId == 15232
							|| polyId == 14923 || polyId == 15223
							|| polyId == 8817 || polyId == 6267
							|| polyId == 6270 || polyId == 6273
							|| polyId == 6276 || polyId == 16014 
							|| polyId == 15986 || polyId == 16027 
							|| polyId == 16284 || polyId == 16053
							|| polyId == 16040 || polyId == 17541 || polyId == 17515 || polyId == 17531)
						weaponTakeoff = 50;
					else {
						weaponTakeoff = 83;
					}
				}
			}
		}
		writeC(weaponTakeoff > 0 ? weaponTakeoff : 29);
		writeC(0xff);
		writeC(0xff);
		writeC(0x00);
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return _S_ChangeShape;
	}
}
