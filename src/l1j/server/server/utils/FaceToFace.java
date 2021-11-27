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

package l1j.server.server.utils;

import java.util.List;

import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1BuffNpcInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.utils:
// FaceToFace

public class FaceToFace {

	private FaceToFace() {
	}

	public static L1PcInstance faceToFace(L1PcInstance pc) {
		int pcX = pc.getX();
		int pcY = pc.getY();
		int pcHeading = pc.getMoveState().getHeading();
		List<L1PcInstance> players = L1World.getInstance().getVisiblePlayer(pc,
				1);

		if (players.size() == 0) { // 1 셀 이내에 PC가 없는 경우
			// pc.sendPackets(new S_ServerMessage(93)); // \f1 거기에는 아무도 없습니다.
			return null;
		}
		for (L1PcInstance target : players) {
			int targetX = target.getX();
			int targetY = target.getY();
			int targetHeading = target.getMoveState().getHeading();
			if (pcHeading == 0 && pcX == targetX && pcY == (targetY + 1)) {
				if (targetHeading == 4) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																				// 당신을
																				// 보고
																				// 있지
																				// 않습니다.
					return null;
				}
			} else if (pcHeading == 1 && pcX == (targetX - 1)
					&& pcY == (targetY + 1)) {
				if (targetHeading == 5) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																				// 당신을
																				// 보고
																				// 있지
																				// 않습니다.
					return null;
				}
			} else if (pcHeading == 2 && pcX == (targetX - 1) && pcY == targetY) {
				if (targetHeading == 6) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																				// 당신을
																				// 보고
																				// 있지
																				// 않습니다.
					return null;
				}
			} else if (pcHeading == 3 && pcX == (targetX - 1)
					&& pcY == (targetY - 1)) {
				if (targetHeading == 7) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																				// 당신을
																				// 보고
																				// 있지
																				// 않습니다.
					return null;
				}
			} else if (pcHeading == 4 && pcX == targetX && pcY == (targetY - 1)) {
				if (targetHeading == 0) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																				// 당신을
																				// 보고
																				// 있지
																				// 않습니다.
					return null;
				}
			} else if (pcHeading == 5 && pcX == (targetX + 1)
					&& pcY == (targetY - 1)) {
				if (targetHeading == 1) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																				// 당신을
																				// 보고
																				// 있지
																				// 않습니다.
					return null;
				}
			} else if (pcHeading == 6 && pcX == (targetX + 1) && pcY == targetY) {
				if (targetHeading == 2) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																				// 당신을
																				// 보고
																				// 있지
																				// 않습니다.
					return null;
				}
			} else if (pcHeading == 7 && pcX == (targetX + 1)
					&& pcY == (targetY + 1)) {
				if (targetHeading == 3) {
					return target;
				} else {
					pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																				// 당신을
																				// 보고
																				// 있지
																				// 않습니다.
					return null;
				}
			}
		}
		// pc.sendPackets(new S_ServerMessage(93)); // \f1 거기에는 아무도 없습니다.
		return null;
	}

	public static L1PcInstance faceToFaceForNpc(L1BuffNpcInstance npc) {
		int pcX = npc.getX();
		int pcY = npc.getY();
		int pcHeading = npc.getMoveState().getHeading();
		List<L1PcInstance> players = L1World.getInstance().getVisiblePlayer(
				npc, 1);

		if (players.size() == 0) { // 1 셀 이내에 PC가 없는 경우
			return null;
		}
		for (L1PcInstance target : players) {
			int targetX = target.getX();
			int targetY = target.getY();
			int targetHeading = target.getMoveState().getHeading();
			if (pcHeading == 0 && pcX == targetX && pcY == (targetY + 1)) {
				if (targetHeading == 4) {
					return target;
				} else {
					return null;
				}
			} else if (pcHeading == 1 && pcX == (targetX - 1)
					&& pcY == (targetY + 1)) {
				if (targetHeading == 5) {
					return target;
				} else {
					return null;
				}
			} else if (pcHeading == 2 && pcX == (targetX - 1) && pcY == targetY) {
				if (targetHeading == 6) {
					return target;
				} else {
					return null;
				}
			} else if (pcHeading == 3 && pcX == (targetX - 1)
					&& pcY == (targetY - 1)) {
				if (targetHeading == 7) {
					return target;
				} else {
					return null;
				}
			} else if (pcHeading == 4 && pcX == targetX && pcY == (targetY - 1)) {
				if (targetHeading == 0) {
					return target;
				} else {
					return null;
				}
			} else if (pcHeading == 5 && pcX == (targetX + 1)
					&& pcY == (targetY - 1)) {
				if (targetHeading == 1) {
					return target;
				} else {
					return null;
				}
			} else if (pcHeading == 6 && pcX == (targetX + 1) && pcY == targetY) {
				if (targetHeading == 2) {
					return target;
				} else {
					return null;
				}
			} else if (pcHeading == 7 && pcX == (targetX + 1)
					&& pcY == (targetY + 1)) {
				if (targetHeading == 3) {
					return target;
				} else {
					return null;
				}
			}
		}
		return null;
	}

	public static L1NpcInstance faceToFaceforNpc(L1PcInstance pc) {
		return faceToFaceforNpc(pc, false, 0);
	}

	public static L1NpcInstance faceToFaceforNpc(L1PcInstance pc, boolean ck) {
		return faceToFaceforNpc(pc, ck, 0);
	}

	public static L1NpcInstance faceToFaceforNpc(L1PcInstance pc, boolean ck,
			int objid) {
		int pcX = pc.getX();
		int pcY = pc.getY();
		int pcHeading = pc.getMoveState().getHeading();
		List<L1Object> players = L1World.getInstance().getVisibleObjects(pc, 1);

		if (players.size() == 0) { // 1 셀 이내에 PC가 없는 경우
			return null;
		}
		for (L1Object target2 : players) {
			if (!(target2 instanceof L1NpcInstance))
				continue;
			L1NpcInstance target = (L1NpcInstance) target2;
			if (objid != 0 && target.getId() != objid)
				continue;
			int targetX = target.getX();
			int targetY = target.getY();
			int targetHeading = target.getMoveState().getHeading();
			if (pcHeading == 0 && pcX == targetX && pcY == (targetY + 1)) {
				if (targetHeading == 4) {
					return target;
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																					// 당신을
																					// 보고
																					// 있지
																					// 않습니다.
					return null;
				}
			} else if (pcHeading == 1 && pcX == (targetX - 1)
					&& pcY == (targetY + 1)) {
				if (targetHeading == 5) {
					return target;
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																					// 당신을
																					// 보고
																					// 있지
																					// 않습니다.
					return null;
				}
			} else if (pcHeading == 2 && pcX == (targetX - 1) && pcY == targetY) {
				if (targetHeading == 6) {
					return target;
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																					// 당신을
																					// 보고
																					// 있지
																					// 않습니다.
					return null;
				}
			} else if (pcHeading == 3 && pcX == (targetX - 1)
					&& pcY == (targetY - 1)) {
				if (targetHeading == 7) {
					return target;
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																					// 당신을
																					// 보고
																					// 있지
																					// 않습니다.
					return null;
				}
			} else if (pcHeading == 4 && pcX == targetX && pcY == (targetY - 1)) {
				if (targetHeading == 0) {
					return target;
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																					// 당신을
																					// 보고
																					// 있지
																					// 않습니다.
					return null;
				}
			} else if (pcHeading == 5 && pcX == (targetX + 1)
					&& pcY == (targetY - 1)) {
				if (targetHeading == 1) {
					return target;
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																					// 당신을
																					// 보고
																					// 있지
																					// 않습니다.
					return null;
				}
			} else if (pcHeading == 6 && pcX == (targetX + 1) && pcY == targetY) {
				if (targetHeading == 2) {
					return target;
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																					// 당신을
																					// 보고
																					// 있지
																					// 않습니다.
					return null;
				}
			} else if (pcHeading == 7 && pcX == (targetX + 1)
					&& pcY == (targetY + 1)) {
				if (targetHeading == 3) {
					return target;
				} else {
					if (!ck)
						pc.sendPackets(new S_ServerMessage(91, target.getName())); // \f1%0이
																					// 당신을
																					// 보고
																					// 있지
																					// 않습니다.
					return null;
				}
			}
		}
		// if(!ck)
		// pc.sendPackets(new S_ServerMessage(93)); // \f1 거기에는 아무도 없습니다.
		return null;
	}
}
