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

import l1j.server.server.datatables.CharSoldierTable;
import l1j.server.server.datatables.SoldierTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_SoldierGive;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Soldier;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_SoldierGive extends ClientBasePacket {
	

	private static final String C_SOLDIER_GIVE = "[C] C_SoldierGive";

	public C_SoldierGive(byte abyte0[], LineageClient clientthread) {
		super(abyte0);
		try {
			// SoldierGiveSelect.java 여야 하지만 길어서 셀렉트는 생략.
			int objid = readD(); // 말 건 npc id
			int type = readH(); // 선택

			L1PcInstance pc = clientthread.getActiveChar();
			pc.용병타입 = 1;
			int castle_id = pc.getClan().getCastleId();
			L1Soldier soldier = SoldierTable.getInstance().getSoldierTable(
					castle_id);

			int count = 0;

			switch (type) {
			case 0:
				if (soldier.getSoldier1() <= 0) {
					S_SystemMessage sm4 = new S_SystemMessage(
							soldier.getSoldier1Name() + "는 고용 된 용병이 없습니다.");
					pc.sendPackets(sm4, true);
					S_CloseList cl4 = new S_CloseList(objid);
					pc.sendPackets(cl4, true);
					return;
				} else {
					count = soldier.getSoldier1();
				}
				break;
			case 1:
				if (soldier.getSoldier2() <= 0) {
					S_SystemMessage sm4 = new S_SystemMessage(
							soldier.getSoldier2Name() + "는 고용 된 용병이 없습니다.");
					pc.sendPackets(sm4, true);
					S_CloseList cl4 = new S_CloseList(objid);
					pc.sendPackets(cl4, true);
					return;
				} else {
					count = soldier.getSoldier2();
				}
				break;
			case 2:
				if (soldier.getSoldier3() <= 0) {
					S_SystemMessage sm4 = new S_SystemMessage(
							soldier.getSoldier3Name() + "는 고용 된 용병이 없습니다.");
					pc.sendPackets(sm4, true);
					S_CloseList cl4 = new S_CloseList(objid);
					pc.sendPackets(cl4, true);
					return;
				} else {
					count = soldier.getSoldier3();
				}
				break;
			case 3:
				if (soldier.getSoldier4() <= 0) {
					S_SystemMessage sm4 = new S_SystemMessage(
							soldier.getSoldier4Name() + "는 고용 된 용병이 없습니다.");
					pc.sendPackets(sm4, true);
					S_CloseList cl4 = new S_CloseList(objid);
					pc.sendPackets(cl4, true);
					return;
				} else {
					count = soldier.getSoldier4();
				}
				break;
			default:
				S_SystemMessage sm = new S_SystemMessage(
						"현재 이 부분은 에러사항이며 수정중에 있습니다.");
				pc.sendPackets(sm, true);
				return;
			}

			for (L1Object obj : L1World.getInstance().getVisibleObjects(pc, 1)) {
				if (obj instanceof L1PcInstance) {
					int sumX = pc.getX() - obj.getX();
					int sumY = pc.getY() - obj.getY();

					L1PcInstance target = (L1PcInstance) obj;
					if (sumX == 1 && sumY == -2) {
						send(pc, target, objid, type, count);
						break;
					} else if (sumX == 0 && sumY == -1) {
						send(pc, target, objid, type, count);
						break;
					} else if (sumX == -1 && sumY == -1) {
						send(pc, target, objid, type, count);
						break;
					} else {
						send(pc, pc, objid, type, count);
					}
				} else {
					send(pc, pc, objid, type, count);
				}
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private void send(L1PcInstance pc, L1PcInstance target, int objid,
			int type, int count) {
		int a = CharSoldierTable.getInstance().SoldierCalculate(target.getId());
		int iscount = target.getAbility().getTotalCha() / 6 - a;
		if (count < iscount)
			iscount = count;
		// int iscount = count;
		S_SystemMessage sm = new S_SystemMessage(target.getName()
				+ "님은 배치 되어 있는 용병이 " + a + "명 있습니다.");
		pc.sendPackets(sm, true);
		pc.용병타입 = type;
		S_SoldierGive sg = new S_SoldierGive(target, objid, type, count,
				iscount);
		pc.sendPackets(sg, true);
	}

	@Override
	public String getType() {
		return C_SOLDIER_GIVE;
	}
}
