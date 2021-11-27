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

import java.util.StringTokenizer;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1AutoPc implements L1CommandExecutor {
	// private static Logger _log = Logger.getLogger(L1AutoPc.class.getName());

	// private Random _random = new Random();

	// private boolean isChk = false;

	private static L1AutoPc _instance;

	private L1AutoPc() {
	}

	public static L1CommandExecutor getInstance() {
		if (_instance == null) {
			_instance = new L1AutoPc();
		}
		return _instance;
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {

		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String pcName = tok.nextToken();
			int type = Integer.parseInt(tok.nextToken());

			// int locType = 0;

			// try {
			// locType = Integer.parseInt(tok.nextToken());
			// } catch (Exception ex) {
			// locType = 0;
			// }

			// int x = 0;
			// int y = 0;

			// int[] loc = { -8, -7, -6, -5, -4, -3, -2, -1, 0, 1, 2, 3, 4, 5,
			// 6, 7, 8 };

			// L1Map map = pc.getMap();
			// short mapId = 0;

			/*
			 * CharacterStorage character = new MySqlCharacterStorage();
			 * Connection con = null; PreparedStatement pstm = null; ResultSet
			 * rs = null;
			 * 
			 * L1PcInstance autoPc = null; AutoShopManager shopManager = null;
			 * int rnd = 0; int SearchCount = 0;
			 */
			switch (type) {
			case 0:
				for (L1PcInstance player : L1World.getInstance()
						.getAllPlayers()) {
					if (player.getName().equalsIgnoreCase(pcName)) {
						player.save();
						player.logout();
					}
				}
				break;
			case 1:
				for (Object obj : L1World.getInstance().getAllPlayers()) {
					if (!((L1PcInstance) obj).isPrivateShop()
							&& ((L1PcInstance) obj).noPlayerCK) {
						((L1PcInstance) obj).logout();
					}
				}
				break;
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName
					+ " [케릭명] [0:끔,1:전체끔] 라고 입력해 주세요. "));
		}
	}
}
