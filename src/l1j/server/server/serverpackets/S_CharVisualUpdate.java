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

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_CharVisualUpdate extends ServerBasePacket {
	private static final String _S__0B_S_CharVisualUpdate = "[S] S_CharVisualUpdate";

	private L1PcInstance _cha;

	public S_CharVisualUpdate(L1PcInstance cha) {
		_cha = cha;
		if (_cha.isFishing() || _cha.isFishingReady()) {
			return;
		}
		writeC(Opcodes.S_WIELD);
		writeD(_cha.getId());
		int polyId = _cha.getGfxId().getTempCharGfx();
		int weaponId = _cha.getCurrentWeapon();

		if (polyId == 3784 || polyId == 6137 || polyId == 6142 || polyId == 6147 || polyId == 6152 || polyId == 6157 || polyId == 9205 || polyId == 9206) {
			if (cha instanceof L1RobotInstance) {
				if (weaponId == 24)
					weaponId = 83;
			} else if (weaponId == 24 && _cha.getWeapon() != null && _cha.getWeapon().getItem().getType() == 18)
				weaponId = 83;
		} else if (polyId == 13152 || polyId == 13153 || polyId == 12702
				|| polyId == 15154 || polyId == 15232 || polyId == 14923 || polyId == 15223
				|| polyId == 12681 || polyId == 8812 || polyId == 8817
				|| polyId == 6267 || polyId == 6270 || polyId == 6273
				|| polyId == 6276 || polyId == 16014 
				|| polyId == 15986 || polyId == 16027 || polyId == 16284 || polyId == 16053
				|| polyId == 16040 || polyId == 17541 || polyId == 17515 || polyId == 17531) {
			if (weaponId == 24 && _cha.getWeapon() != null && _cha.getWeapon().getItem().getType() == 18)
				weaponId = 50;
		}
		writeC(weaponId);
		writeC(0xff);
		writeC(0xff);
		writeH(0x00);
	}

	public S_CharVisualUpdate(L1PcInstance cha, int shopActionId) {
		_cha = cha;
		if (_cha.isFishing() || _cha.isFishingReady()) {
			return;
		}
		writeC(Opcodes.S_WIELD);
		writeD(_cha.getId());
		writeC(shopActionId);
		writeC(0xff);
		writeC(0xff);
		writeH(0x00);
	}

	public S_CharVisualUpdate(L1NpcInstance cha) {
		writeC(Opcodes.S_WIELD);
		writeD(cha.getId());
		writeC(cha.getActionStatus());
		writeC(0xff);
		writeC(0xff);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return _S__0B_S_CharVisualUpdate;
	}
}
