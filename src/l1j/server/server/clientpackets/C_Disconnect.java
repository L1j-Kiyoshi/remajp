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

import java.util.logging.Logger;


import l1j.server.server.model.Instance.L1PcInstance;
import server.LineageClient;

public class C_Disconnect extends ClientBasePacket {
	

	private static final String C_DISCONNECT = "[C] C_Disconnect";
	private static Logger _log = Logger.getLogger(C_Disconnect.class.getName());

	public C_Disconnect(byte[] decrypt, LineageClient client) {
		super(decrypt);
		try {
			client.CharReStart(true);
			L1PcInstance pc = client.getActiveChar();
			if (pc != null) {
				_log.fine("Disconnect from: " + pc.getName());
			
				/** 인터서버 접속중인걸로 체크한다면 정보 다 갱신 */
				if(client.getInterServer()){
					client.setInterServer(false);
					client.setInterServerType(0);
				}
				
				client.quitGame(pc);
				synchronized (pc) {
					pc.logout();
					client.setActiveChar(null);
					client.close();
				}
			} else {
				_log.fine("Disconnect Request from Account : "
						+ client.getAccountName());
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_DISCONNECT;
	}
}
