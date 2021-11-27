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

import java.io.IOException;
import java.util.logging.Logger;

import javolution.util.FastTable;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import server.Authorization;
import server.GameServer;
import server.LineageClient;

public class C_AuthLogin extends ClientBasePacket {
	private static final String C_AUTH_LOGIN = "[C] C_AuthLogin";
	private static Logger _log = Logger.getLogger(C_AuthLogin.class.getName());
	public static FastTable<String> nameList = new FastTable<String>();

	public C_AuthLogin(byte[] decrypt, LineageClient client, boolean DDos) throws IOException {
		super(decrypt);
		try {
			if (client == null) {
				return;
			}

			String accountName = readS();
			String password = readS();
			String ip = client.getIp();
			String host = client.getHostname();
			if (GMCommands.접속이름체크) {
				if (nameList.contains(accountName)) {
					GameServer.DuplicationLoginCheck("(계정) " + accountName, "계정 중복 로그인");
					client.kick();
					client.close();
					return;
				}
				nameList.add(accountName);
				GeneralThreadPool.getInstance().schedule(new charNameDelete(accountName), 2000);
			}

			_log.finest("Request AuthLogin from user : " + accountName);

			// 계정 정보 확인
			Authorization.getInstance().auth(client, accountName, password, ip, host);
			DDos = false;
			client.setletteron(true);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	private static String HexToDex(int data, int digits) {
		String number = Integer.toHexString(data);
		for (int i = number.length(); i < digits; i++)
			number = "0" + number;
		return number;
	}

	public static String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(HexToDex(i, 4) + ": ");
			}
			result.append(HexToDex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char) t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}
		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char) t1);
				} else {
					result.append('.');
				}
			}
			result.append("\n");
		}
		return result.toString();
	}

	class charNameDelete implements Runnable {
		private String name = null;

		public charNameDelete(String _name) {
			name = _name;
		}

		@Override
		public void run() {
			try {
				if (nameList.contains(name))
					nameList.remove(name);
			} catch (Exception e) {
				System.out.println("계정로그인 이름 리스트에서 삭제 에러 : " + name);
			}
		}
	}

	@Override
	public String getType() {
		return C_AUTH_LOGIN;
	}
}