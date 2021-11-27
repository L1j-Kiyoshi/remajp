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

import l1j.server.server.serverpackets.S_SystemMessage;
import server.LineageClient;
import server.LoginController;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_ReturnToLogin extends ClientBasePacket {
	

	private static final String C_RETURN_TO_LOGIN = "[C] C_ReturnToLogin";
	private static Logger _log = Logger.getLogger(C_ReturnToLogin.class.getName());

	public C_ReturnToLogin(byte decrypt[], LineageClient client)
			throws Exception {
		super(decrypt);
		try {
			String account = client.getAccountName();
			StringBuilder sb = new StringBuilder();
			_log.finest(sb.append("account : ").append(account).toString());
			
			sb = null;
			/** 중계기를 통한 client 로그아웃 (접속해제는 안되는상태) **/
			if (client.getActiveChar() != null) {
				S_SystemMessage sm = new S_SystemMessage("중계기 의심-밴처리");
				client.sendPacket(sm, true);
				client.kick();
				client.close();
				return;
			}
			LoginController.getInstance().logout(client);
			// client.kick();
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_RETURN_TO_LOGIN;
	}

}
