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

package l1j.server.server.clientpackets;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_PetInventory;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_PetMenu extends ClientBasePacket {
	

	private static final String C_PET_MENU = "[C] C_PetMenu";

	public C_PetMenu(byte abyte0[], LineageClient clientthread)
			throws Exception {
		super(abyte0);
		try {
			int petId = readD();
			L1PetInstance pet = null;
			L1Object obj = L1World.getInstance().findObject(petId);
			if (obj instanceof L1PetInstance) {
				pet = (L1PetInstance) obj;
			}
			L1PcInstance pc = clientthread.getActiveChar();
			if (pc.getMapId() == 5125 || pc.getMapId() == 5131
					|| pc.getMapId() == 5132 || pc.getMapId() == 5133
					|| pc.getMapId() == 5134) {
				S_SystemMessage sm = new S_SystemMessage(
						"펫 매치 중에는 사용 할 수 없습니다.");
				pc.sendPackets(sm, true);
				return;
			}
			if (pet != null && pc != null) {
				S_PetInventory pe = new S_PetInventory(pet);
				pc.sendPackets(pe, true);
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_PET_MENU;
	}
}
