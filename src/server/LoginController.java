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
package server;

import java.util.concurrent.ConcurrentHashMap;

import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameServerFullException;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.serverpackets.S_ServerMessage;
import server.message.ServerMessage;

public class LoginController {
	private static String TAG = "LoginController";

	private static LoginController uniqueInstance;
	private ConcurrentHashMap<String, LineageClient> accountsMap = new ConcurrentHashMap<String, LineageClient>();
	private int maxAllowedOnlinePlayers;

	private LoginController() {
	}

	public static LoginController getInstance() {
		if (uniqueInstance == null) {
			synchronized (LoginController.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new LoginController();
				}
			}
		}
		return uniqueInstance;
	}

	public LineageClient[] getAllAccounts() {
		return accountsMap.values().toArray(
				new LineageClient[accountsMap.size()]);
	}

	public int getOnlinePlayerCount() {
		return accountsMap.size();
	}

	public int getMaxAllowedOnlinePlayers() {
		return maxAllowedOnlinePlayers;
	}

	public void setMaxAllowedOnlinePlayers(final int maxAllowedOnlinePlayers) {
		this.maxAllowedOnlinePlayers = maxAllowedOnlinePlayers;
	}

	private void kickClient(final LineageClient client) {
		if (client == null) {
			return;
		}

		if (client.getActiveChar() != null) {
			client.getActiveChar().sendPackets(new S_ServerMessage(ServerMessage.DUPLICATED_IP_CONNECTION));
		}

		try {
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				@Override
				public void run() {
					client.kick();
					client.close();
				}

			}, 1000);
		} catch (Exception e) {
		}
	}

	public synchronized void login(LineageClient client, Account account) throws GameServerFullException, AccountAlreadyLoginException {
		if (!account.isValid()) {
			throw new IllegalArgumentException("인증되지 않은 계정입니다");
		}
		if(client.getInterServer()){
			if (accountsMap.containsKey(account.getName())) {
				accountsMap.remove(account.getName());
			}
		} else {
			if (accountsMap.containsKey(account.getName())) {
				kickClient(accountsMap.remove(account.getName()));
				throw new AccountAlreadyLoginException();
			}
		}

		accountsMap.put(account.getName(), client);
	}

	
	public boolean accountCheck(String acc) {
		if (accountsMap.containsKey(acc)) {
			return true;
		}
		return false;
	}

	
	public synchronized boolean logout(LineageClient client) {
		if (client.getAccountName() == null) {
			return false;
		}
		client.getAccount().updateLastQuit(client.getAccount());
		return accountsMap.remove(client.getAccountName()) != null;
	}
	
	
	public synchronized void logout(String AccountName) {
		if (accountsMap.containsKey(AccountName)) {
			accountsMap.remove(AccountName);
		}
	}

	
	public boolean checkDuplicatedIP(String ip) {
		LineageClient[] list = getAllAccounts();
		boolean ck = false;
		for (LineageClient tempClient : list) {
			if (ip.equalsIgnoreCase(tempClient.getIp())) {
				ck = true;
				break;
			}
		}
		list = null;
		return ck;
	}
	
	
}
