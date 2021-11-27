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
package l1j.server.server.command.executor;

import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SpawnTable;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.L1SpawnUtil;

public class SabuSpwan implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(SabuSpwan.class.getName());
	private static Random _random = new Random(System.nanoTime());

	private SabuSpwan() {
	}

	public static L1CommandExecutor getInstance() {
		return new SabuSpwan();
	}

	private void sendErrorMessage(L1PcInstance pc, String cmdName) {
		String errorMsg = cmdName + "[ID/이름] [수] [범위] 로 입력 ";
		pc.sendPackets(new S_SystemMessage(errorMsg));
	}

	private int parseNpcId(String nameId) {
		int npcid = 0;
		try {
			npcid = Integer.parseInt(nameId);
		} catch (NumberFormatException e) {
			npcid = NpcTable.getInstance().findNpcIdByNameWithoutSpace(nameId);
		}
		return npcid;
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			StringTokenizer tok = new StringTokenizer(arg);
			String nameId = tok.nextToken();
			int count = 1;
			if (tok.hasMoreTokens()) {
				count = Integer.parseInt(tok.nextToken());
			}
			int range = 0;
			if (tok.hasMoreTokens()) {
				range = Integer.parseInt(tok.nextToken(), 10);
			}
			int npcid = parseNpcId(nameId);

			L1Npc npc = NpcTable.getInstance().getTemplate(npcid);
			if (npc == null) {
				pc.sendPackets(new S_SystemMessage("없는 엔피씨"));
				return;
			}
			for (int i = 0; i < count; i++) {
				L1Location newLocation = null;

				if (range != 0) {
					newLocation = randomLocation(pc.getLocation(), 0, range,
							true);
				} else {

					newLocation = L1Location.saburan(pc.getMap());
				}
				L1SpawnUtil.spawn2(newLocation.getX(), newLocation.getY(),
						(short) newLocation.getMapId(), npcid, 0, 0, 0);
				SpawnTable.getInstance().SabuSpawn(newLocation, npc);
			}
			String msg = String.format("%s(%d) (%d) 를 스폰추가 하였습니다. ",
					npc.get_name(), npcid, count);
			pc.sendPackets(new S_SystemMessage(msg));
		} catch (NoSuchElementException e) {
			sendErrorMessage(pc, cmdName);
		} catch (NumberFormatException e) {
			sendErrorMessage(pc, cmdName);
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			pc.sendPackets(new S_SystemMessage(cmdName + " 내부 에러입니다."));
		}
	}

	public L1Location randomLocation(L1Location baseLocation, int min, int max,
			boolean isRandomTeleport) {
		if (min > max) {
			throw new IllegalArgumentException("min > max가 되는 인수는 무효");
		}
		if (max <= 0) {
			return new L1Location(baseLocation);
		}
		if (min < 0) {
			min = 0;
		}

		L1Location newLocation = new L1Location();
		int newX = 0;
		int newY = 0;
		int locX = baseLocation.getX();
		int locY = baseLocation.getY();
		short mapId = (short) baseLocation.getMapId();
		L1Map map = baseLocation.getMap();

		newLocation.setMap(map);

		int locX1 = locX - max;
		int locX2 = locX + max;
		int locY1 = locY - max;
		int locY2 = locY + max;

		int mapX1 = map.getX();
		int mapX2 = mapX1 + map.getWidth();
		int mapY1 = map.getY();
		int mapY2 = mapY1 + map.getHeight();

		if (locX1 < mapX1) {
			locX1 = mapX1;
		}
		if (locX2 > mapX2) {
			locX2 = mapX2;
		}
		if (locY1 < mapY1) {
			locY1 = mapY1;
		}
		if (locY2 > mapY2) {
			locY2 = mapY2;
		}

		int diffX = locX2 - locX1;
		int diffY = locY2 - locY1;

		int trial = 0;
		int amax = (int) Math.pow(1 + (max * 2), 2);
		int amin = (min == 0) ? 0 : (int) Math.pow(1 + ((min - 1) * 2), 2);
		int trialLimit = 40 * amax / (amax - amin);

		while (true) {
			if (trial >= trialLimit) {
				newLocation.set(locX, locY);
				break;
			}
			trial++;
			try {
				newX = locX1 + _random.nextInt(diffX + 1);
				newY = locY1 + _random.nextInt(diffY + 1);
			} catch (Exception e) {
				// System.out.println("랜덤텔 범위 벗어나는 맵번호 : "+mapId);
			}
			newLocation.set(newX, newY);
			if (baseLocation.getTileLineDistance(newLocation) < min) {
				continue;

			}
			if (isRandomTeleport) {
				if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
					continue;
				}

				if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
					continue;
				}
			}
			if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
				break;
			}
		}
		return newLocation;
	}
}