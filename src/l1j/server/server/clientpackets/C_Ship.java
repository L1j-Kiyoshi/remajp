/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
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

import java.util.logging.Logger;

import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharPack;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Ship extends ClientBasePacket {
	private static final String C_SHIP = "[C] C_Ship";
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(C_Ship.class.getName());

	// C -> S
	// 0000: 78 00 00 77 7f d0 80 bd x..w...
	public C_Ship(byte abyte0[], LineageClient client) {
		super(abyte0);
		StringBuffer sb = new StringBuffer();
		try {
			int shipMapId = readH();
			int locX = readH();
			int locY = readH();

			L1PcInstance pc = client.getActiveChar();
			int mapId = pc.getMapId();
			sb.append("배 내리기 패킷 >> " + pc.getName() + " 현위치> " + mapId + " 내릴맵> " + shipMapId);
			switch (mapId) {
			case 5:
				pc.getInventory().consumeItem(40299, 1);
				break;
			case 6:
				pc.getInventory().consumeItem(40298, 1);
				break;
			case 83:
				pc.getInventory().consumeItem(40300, 1);
				break;
			case 84:
				pc.getInventory().consumeItem(40301, 1);
				break;
			case 446:
				pc.getInventory().consumeItem(40303, 1);
				break;
			case 447:
				pc.getInventory().consumeItem(40302, 1);
				break;
			case 1700:
				break;
			default:
				System.out.println("특정 좌표 이동 중계기 버그 > " + pc.getName() + " 현재 맵> " + mapId + " x>" + pc.getX() + " y>" + pc.getY());
				System.out.println(" 이동시도 맵> " + shipMapId + " x>" + locX + " y>" + locY);
				return;
			}

			pc.sendPackets(new S_OwnCharPack(pc), true);
			L1Teleport.teleport(pc, locX, locY, (short) shipMapId, 0, true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
			sb = null;
		}
	}

	@Override
	public String getType() {
		return C_SHIP;
	}
}
