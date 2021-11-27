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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;

/** by소스 */

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_OwnCharStatus extends ServerBasePacket {
	private static final String S_OWB_CHAR_STATUS = "[S] S_OwnCharStatus";

	private byte[] _byte = null;

	public S_OwnCharStatus(L1PcInstance pc) {
		int time = GameTimeClock.getInstance().getGameTime().getSeconds();
		time = time - (time % 300);
		
		writeC(Opcodes.S_STATUS);
		writeD(pc.getId());

		if (pc.getLevel() < 1) {
			writeC(1);
		} else if (pc.getLevel() > 127) {
			writeC(127);
		} else {
			writeC(pc.getLevel());
		}

		writeD(pc.getExp());
		writeH(pc.getAbility().getTotalStr());
		writeH(pc.getAbility().getTotalInt());			
		writeH(pc.getAbility().getTotalWis());			
		writeH(pc.getAbility().getTotalDex());
		writeH(pc.getAbility().getTotalCon());			
		writeH(pc.getAbility().getTotalCha());			
		writeH(pc.getCurrentHp());						
		writeH(pc.getMaxHp());							
		writeH(pc.getCurrentMp());						
		writeH(pc.getMaxMp());		
		writeD(time);
		writeC(pc.get_food());							
		writeC(pc.getInventory().calcWeightpercent());	
		writeH(pc.getLawful());
		writeH(pc.getResistance().getFire());			
		writeH(pc.getResistance().getWater());			
		writeH(pc.getResistance().getWind());			
		writeH(pc.getResistance().getEarth());		
		writeD(pc._PlayMonKill);
		// FIXME 시간이 비정상적은듯..
	}

	
	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		
		return _byte;
	}

	
	@Override
	public String getType() {
		return S_OWB_CHAR_STATUS;
	}
	
}