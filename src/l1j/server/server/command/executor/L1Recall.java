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

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1Recall implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1Recall.class.getName());

	private L1Recall() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1Recall();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			Collection<L1PcInstance> targets = null;
			if (arg.equalsIgnoreCase("全体")) {
				targets = L1World.getInstance().getAllPlayers();
			} else {
				targets = new ArrayList<L1PcInstance>();
				L1PcInstance tg = L1World.getInstance().getPlayer(arg);
				if (tg == null) {
					pc.sendPackets(new S_SystemMessage("このような キャラクターは ありません。 "), true);
					return;
				}
				targets.add(tg);
			}

			for (L1PcInstance target : targets) {
				if (target.isPrivateShop()) {
					pc.sendPackets(new S_SystemMessage(target.getName()
							+ " キャラクターは 個人商店モードです."), true);
					return;
				}

				L1Location loc = L1Teleport.ToTargetFront(pc, 1);

				if (target instanceof L1RobotInstance) {
					L1RobotInstance rob = (L1RobotInstance) target;
					rob.ディレイ(500);
					L1Teleport.ロボットテル(rob, loc.getX(), loc.getY(),
							(short) loc.getMapId(), true);
					continue;
				}

				target.dx = loc.getX();
				target.dy = loc.getY();
				target.dm = (short) loc.getMapId();
				target.dh = target.getMoveState().getHeading();
				target.setTelType(7);
				target.sendPackets(new S_SabuTell(target));

				pc.sendPackets(new S_SystemMessage((new StringBuilder())
						.append(target.getName()).append(" 를 소환했습니다. ")
						.toString()));
				target.sendPackets(new S_SystemMessage("게임 마스터에 소환되었습니다. "),
						true);
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName
					+ " [전체, 캐릭터명]으로 입력해 주세요. "), true);
		}
	}
}
