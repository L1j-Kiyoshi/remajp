package server;

import java.io.IOException;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.Account;
import l1j.server.server.AccountAlreadyLoginException;
import l1j.server.server.GameServerFullException;
import l1j.server.server.clientpackets.C_NoticeClick;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_LoginResult;
import l1j.server.server.serverpackets.S_Notice;

public class Authorization {
	private static Authorization uniqueInstance = null;
	private static Logger _log = Logger.getLogger(Authorization.class.getName());

	public static Authorization getInstance() {
		if (uniqueInstance == null) {
			synchronized (Authorization.class) {
				if (uniqueInstance == null)
					uniqueInstance = new Authorization();
			}
		}

		return uniqueInstance;
	}

	private Authorization() {

	}

	public void auth(LineageClient client, String accountName, String password, String ip, String host) throws IOException {
		/** 계정이 영문, 숫자로 되어있는지 체크 **/
		int length = accountName.length();
		char chr;
		for (int i = 0; i < length; i++) {
			chr = accountName.charAt(i);
			if (Character.UnicodeBlock.of(chr) != Character.UnicodeBlock.BASIC_LATIN) {
				if (client.getActiveChar() == null) {
					client.sendPacket(new S_LoginResult(client.getAccount(), 26), true);
				}
				return;
			}
		}

		int accountLength = accountName.getBytes().length;
		int passwordLength = password.getBytes().length;
		if (accountLength > 12 || accountLength < 4) {
			_log.info("계정명 오류 [오류 계정 " + accountName + "] ");
			client.sendPacket(new S_LoginResult(client.getAccount(), S_LoginResult.REASON_WRONG_ACCOUNT));
			return;
		} else if (passwordLength > 12 || passwordLength < 4) {
			// 패스워드 길이 (eva 는 매니저 클래스임)
			_log.info("패스워드 길지 오류 [패스워드 길이 " + passwordLength + "] ");
			client.sendPacket(new S_LoginResult(client.getAccount(), S_LoginResult.REASON_WRONG_PASSWORD));
			return;
		}

		Account account = Account.load(accountName);
		if (account == null) {
			if (Config.AUTO_CREATE_ACCOUNTS) {

				if (Account.checkLoginIP(ip)) {
					S_Notice sn2 = new S_Notice("IP당 2개의 계정만 허용합니다.");
					client.sendPacket(sn2);
					sn2.clear();
					sn2 = null;
					try {
						Thread.sleep(1500);
						client.kick();
						client.close();
					} catch (Exception e1) {
					}
				} else {
					account = Account.create(accountName, password, ip, host);
					account = Account.load(accountName);
				}
			} else {
				_log.warning("account missing for user " + accountName);
			}
		}

		if (account == null || !account.validatePassword(accountName, password)) {
			int lfc = client.getLoginFailedCount();
			client.setLoginFailedCount(lfc + 1);
			if (lfc > 2) {
				disconnect(client);
			} else {
				client.sendPacket(new S_LoginResult(client.getAccount(), 26), true);
			}

			return;
		}

		if (account.isBanned()) {
			_log.info("BAN 계정의 로그인을 거부했습니다. account=" + accountName + " host=" + host);
			client.sendPacket(new S_Notice("서버에서 벤당하신 계정입니다. 운영자에게 문의 하시기 바랍니다"), true);
			disconnect(client);
			return;
		}

		try {
			LoginController.getInstance().login(client, account);
			Account.updateLastActive(account, ip); // 최종 로그인일을 갱신한다
			client.setAccount(account);
			client.sendPacket(new S_LoginResult());
			client.sendPacket(new S_CharPass(), true);
			sendNotice(client);
		} catch (GameServerFullException e) {
			client.sendPacket(new S_Notice("서버 접속인원이 많아 접속이 지연되고있습니다.\n \n 잠시후에 다시 접속을 시도해주시기바랍니다."), true);
			disconnect(client);
			_log.info("접속 인원수를 초과하였습니다. (" + client.getHostname() + ")의 접속 시도를 강제 종료했습니다.");
			return;
		} catch (AccountAlreadyLoginException e) { // by 낭만고양이
			client.sendPacket(new S_LoginResult(client.getAccount(), S_LoginResult.REASON_ACCOUNT_IN_USE), true);
			disconnect(client);
			return;
		}

	}

	public void sendNotice(LineageClient client) {
		String accountName = client.getAccountName();

		// 읽어야할 공지가 있는지 체크
		if (S_Notice.NoticeCount(accountName) > 0) {
			client.sendPacket(new S_Notice(accountName, client), true);
		} else {
			new C_NoticeClick(client);
		}
	}

	private void disconnect(LineageClient client) throws IOException {
		client.kick();
		client.close();
	}
}
