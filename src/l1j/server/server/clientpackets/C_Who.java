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

import java.util.Random;

import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.server.command.executor.L1UserCalc;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_WhoAmount;
import l1j.server.server.serverpackets.S_WhoCharinfo;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Who extends ClientBasePacket {
	

	private static final String C_WHO = "[C] C_Who";

	public C_Who(byte[] decrypt, LineageClient client) {
		super(decrypt);
		try {
			String s = readS();
			L1PcInstance find = L1World.getInstance().getPlayer(s);
			L1PcInstance pc = client.getActiveChar();
			Random ran = new Random();

			if (find != null && (pc.isGm() || !find.isGm())) { // 기존소스와 비교해서 이부분
																// 수정
				S_WhoCharinfo s_whocharinfo = new S_WhoCharinfo(find);
				pc.sendPackets(s_whocharinfo, true);

				int r = ran.nextInt(2);
				L1PcInstance[] list = L1World.getInstance()
						.getAllPlayersToArray();
				int AddUser = (int) (list.length * 1.5) + r;
				list = null;
				// int ShopUser =
				// AutoShopManager.getInstance().getShopPlayerCount();
				int CalcUser = L1UserCalc.getClacUser();
				AddUser += CalcUser;
				String amount = String.valueOf(AddUser);
				S_WhoAmount s_whoamount = new S_WhoAmount(amount);
				pc.sendPackets(s_whoamount, true);
				ran = null;
			} else {
				L1NpcShopInstance npc = null;
				npc = L1World.getInstance().getNpcShop(s);
				if (npc != null) {
					S_WhoCharinfo s_whocharinfo = new S_WhoCharinfo(npc);
					pc.sendPackets(s_whocharinfo, true);
					// return;
				} else {
					GambleInstance gam = L1World.getInstance().getGamble(s);
					if (gam != null) {
						S_WhoCharinfo s_whocharinfo = new S_WhoCharinfo(gam);
						pc.sendPackets(s_whocharinfo, true);
						// return;
					}
				}
				int r = ran.nextInt(2);
				L1PcInstance[] list = L1World.getInstance()
						.getAllPlayersToArray();
				int AddUser = (int) (list.length * 1.5) + r;
				list = null;
				// int ShopUser =
				// AutoShopManager.getInstance().getShopPlayerCount();
				int CalcUser = L1UserCalc.getClacUser();
				AddUser += CalcUser;
				String amount = String.valueOf(AddUser);
				S_WhoAmount s_whoamount = new S_WhoAmount(amount);
				pc.sendPackets(s_whoamount, true);
				ran = null;
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_WHO;
	}
}
