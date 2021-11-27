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
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1StandBy implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1StandBy.class.getName());

	private L1StandBy() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1StandBy();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String status = st.nextToken();
			if (status.equalsIgnoreCase("시작")) {
				Config.STANDBY_SERVER = true;
				pc.sendPackets(new S_SystemMessage("오픈대기 상태가 되었습니다."));
			} else if (status.equalsIgnoreCase("종료")) {
				Config.STANDBY_SERVER = false;
				pc.sendPackets(new S_SystemMessage("오픈대기 상태가 종료되었습니다."));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " [시작/종료] 으로 입력하세요."));
			;
		}
	}
}
