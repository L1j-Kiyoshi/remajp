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
package l1j.server.server.command.executor;

import java.util.Collection;
import java.util.logging.Logger;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_WhoAmount;
import server.system.autoshop.AutoShopManager;

public class L1Who implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1Who.class.getName());

	private L1Who() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Who();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int AutoShopUser = AutoShopManager.getInstance()
					.getShopPlayerCount();
			int CalcUser = L1UserCalc.getClacUser();
			Collection<L1PcInstance> players = L1World.getInstance()
					.getAllPlayers();
			String amount = String.valueOf(players.size());
			S_WhoAmount s_whoamount = new S_WhoAmount(amount);
			pc.sendPackets(s_whoamount);
			pc.sendPackets(new S_SystemMessage("무인상점 : " + AutoShopUser));
			pc.sendPackets(new S_SystemMessage("뻥튀기 : " + CalcUser));
			int 봇 = 0;
			int 유저 = 0;
			for (L1PcInstance each : players) {
				if (!each.isPrivateShop()) {
					if (each.noPlayerCK)
						봇++;
					else
						유저++;
				}
			}
			pc.sendPackets(new S_SystemMessage("실제 유저 : " + 유저));
			pc.sendPackets(new S_SystemMessage("로봇 : " + 봇));

			// 온라인의 플레이어 리스트를 표시
			if (arg.equalsIgnoreCase("전체")) {
				StringBuffer gmList = new StringBuffer();
				StringBuffer playList = new StringBuffer();
				StringBuffer noplayList = new StringBuffer();
				StringBuffer shopList = new StringBuffer();

				int countGM = 0, nocountPlayer = 0, countPlayer = 0, countShop = 0;

				for (L1PcInstance each : players) {
					if (each.isGm()) {
						gmList.append(each.getName() + ", ");
						countGM++;
						continue;
					}
					if (!each.isPrivateShop()) {
						if (each.noPlayerCK) {
							noplayList.append(each.getName() + ", ");
							nocountPlayer++;
							continue;
						} else {
							playList.append(each.getName() + ", ");
							countPlayer++;
							continue;
						}
					}
					if (each.isPrivateShop()) {
						shopList.append(each.getName() + ", ");
						countShop++;
					}
				}
				if (gmList.length() > 0) {
					pc.sendPackets(new S_SystemMessage("-- 운영자 (" + countGM
							+ "명)"));
					pc.sendPackets(new S_SystemMessage(gmList.toString()));
				}
				if (noplayList.length() > 0) {
					pc.sendPackets(new S_SystemMessage("-- 허상 ("
							+ nocountPlayer + "개)"));
					pc.sendPackets(new S_SystemMessage(noplayList.toString()));
				}
				if (playList.length() > 0) {
					pc.sendPackets(new S_SystemMessage("-- 플레이어 ("
							+ countPlayer + "명)"));
					pc.sendPackets(new S_SystemMessage(playList.toString()));
				}

				if (shopList.length() > 0) {
					pc.sendPackets(new S_SystemMessage("-- 개인상점 (" + countShop
							+ "명)"));
					pc.sendPackets(new S_SystemMessage(shopList.toString()));
				}
			} else if (arg.equalsIgnoreCase("로봇")) {
				StringBuffer noplayList = new StringBuffer();
				for (L1PcInstance each : players) {
					if (!each.isPrivateShop()) {
						if (each.noPlayerCK) {
							noplayList.append(each.getName() + ", ");
						}
					}
				}
				if (noplayList.length() > 0) {
					pc.sendPackets(new S_SystemMessage("----------"));
					pc.sendPackets(new S_SystemMessage(noplayList.toString()));
				}
			} else if (arg.equalsIgnoreCase("실유저")) {
				StringBuffer playList = new StringBuffer();
				for (L1PcInstance each : players) {
					if (!each.isPrivateShop()) {
						if (!each.noPlayerCK) {
							playList.append(each.getName() + ", ");
						}
					}
				}
				if (playList.length() > 0) {
					pc.sendPackets(new S_SystemMessage("----------"));
					pc.sendPackets(new S_SystemMessage(playList.toString()));
				}
			} else {
				if (arg.equalsIgnoreCase(""))
					return;
				L1PcInstance pp = L1World.getInstance().getPlayer(arg);
				if (pp == null || pp.getNetConnection() == null) {
					pc.sendPackets(new S_SystemMessage(
							"해당 유저는 접속중이 아니거나 로봇, 무인상점입니다."));
					return;
				}
				String ip = pp.getNetConnection().getIp();
				ip = ip.substring(0, ip.lastIndexOf(".") + 1);
				StringBuffer playList = new StringBuffer();
				for (L1PcInstance each : players) {
					if (!each.isPrivateShop()
							&& each.getNetConnection() != null) {
						if (!each.noPlayerCK) {
							if (each.getNetConnection().getIp().indexOf(ip) >= 0) {
								playList.append(each.getName() + ", ");
							}
						}
					}
				}
				if (playList.length() > 0) {
					if (ip.startsWith("183.111.9")) {// 211.233.72
						pc.sendPackets(new S_SystemMessage("프록시 아이피로 접속중 입니다."));
					} else {
						pc.sendPackets(new S_SystemMessage("IP: " + ip
								+ " 대역 ----------"));
						pc.sendPackets(new S_SystemMessage(playList.toString()));
					}
				} else {
					pc.sendPackets(new S_SystemMessage(
							"해당 IP 대역 추가 유저 없음 ----------"));
				}
			}
			players = null;
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".누구 [전체,로봇,실유저] 라고 입력해 주세요. "));
		}
	}
}
