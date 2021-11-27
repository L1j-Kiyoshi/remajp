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
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.Teleportation;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Teleport extends ClientBasePacket {
	private static final String C_TELEPORT = "[C] C_Teleport";

	public C_Teleport(byte abyte0[], LineageClient clientthread)
			throws Exception {
		super(abyte0);
		try {
			if (clientthread == null) {
				System.out.println("텔레포트 클라이언트가 존재하지 않습니다");
				return;
			}

			L1PcInstance pc = clientthread.getActiveChar();
			if (pc == null || pc.isPrivateShop() || pc.getAIprivateShop()) {
				return;
			}
			Teleportation.doTeleportation(clientthread.getActiveChar());
		} catch (Exception e) {
			System.out.println("텔 관련 1번오류");
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_TELEPORT;
	}
}