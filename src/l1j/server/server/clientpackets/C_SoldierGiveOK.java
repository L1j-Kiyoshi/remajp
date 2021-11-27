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
import l1j.server.server.model.gametime.RealTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1CharSoldier;
import l1j.server.server.templates.L1Soldier;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_SoldierGiveOK extends ClientBasePacket {
	

	private static final String C_SOLDIER_GIVE_OK = "[C] C_SoldierGiveOK";

	public C_SoldierGiveOK(byte abyte0[], LineageClient clientthread) {
		super(abyte0);
		try {
			int objid = readD(); // 말 건 npc id
			int index = readH(); // 선택목록 순번
			@SuppressWarnings("unused")
			int unknow = readH(); // ????
			int t_obj = readD(); // pc.getId
			int count = readH(); // 선택갯수

			L1PcInstance pc = clientthread.getActiveChar();

			index = pc.용병타입 + 1;

			L1Object obj = L1World.getInstance().findObject(t_obj);
			L1PcInstance target = (L1PcInstance) obj;

			int npc_id = 0;
			int getCount = 0;

			int castle_id = pc.getClan().getCastleId();
			L1Soldier soldier = SoldierTable.getInstance().getSoldierTable(
					castle_id);

			if (pc.getClanid() != target.getClanid())
				return;

			switch (index) {
			case 1:
				npc_id = soldier.getSoldier1NpcId();
				getCount = soldier.getSoldier1();
				break;
			case 2:
				npc_id = soldier.getSoldier2NpcId();
				getCount = soldier.getSoldier2();
				break;
			case 3:
				npc_id = soldier.getSoldier3NpcId();
				getCount = soldier.getSoldier3();
				break;
			case 4:
				npc_id = soldier.getSoldier4NpcId();
				getCount = soldier.getSoldier4();
				break;
			default:
				S_SystemMessage sm = new S_SystemMessage(
						"현재 이 부분은 에러사항이며 수정중에 있습니다.");
				pc.sendPackets(sm, true);
				return;
			}

			// System.out.println(index+" > "+getCount+" > "+count);
			if (getCount < count)
				return;

			RealTime r = RealTimeClock.getInstance().getRealTime();
			long time = r.getSeconds();

			L1CharSoldier newCharSoldier = new L1CharSoldier(pc.getId());
			newCharSoldier.setSoldierNpc(npc_id);
			newCharSoldier.setSoldierCount(count);
			newCharSoldier.setSoldierCastleId(target.getClan().getCastleId());
			newCharSoldier.setSoldierTime((int) time);

			CharSoldierTable.getInstance().storeCharSoldier(newCharSoldier);

			newCharSoldier = null;

			int sum = getCount - count;
			switch (index) {
			case 1:
				soldier.setSoldier1(sum);
				S_SystemMessage sm = new S_SystemMessage(
						soldier.getSoldier1Name() + "를 " + count
								+ " 명 배치 하였습니다.");
				target.sendPackets(sm, true);
				break;
			case 2:
				soldier.setSoldier2(sum);
				S_SystemMessage sm2 = new S_SystemMessage(
						soldier.getSoldier2Name() + "를 " + count
								+ " 명 배치 하였습니다.");
				target.sendPackets(sm2, true);
				break;
			case 3:
				soldier.setSoldier3(sum);
				S_SystemMessage sm3 = new S_SystemMessage(
						soldier.getSoldier3Name() + "를 " + count
								+ " 명 배치 하였습니다.");
				target.sendPackets(sm3, true);
				break;
			case 4:
				soldier.setSoldier4(sum);
				S_SystemMessage sm4 = new S_SystemMessage(
						soldier.getSoldier4Name() + "를 " + count
								+ " 명 배치 하였습니다.");
				target.sendPackets(sm4, true);
				break;
			default:
				break;
			}

			SoldierTable.getInstance().updateSoldier(soldier);

			S_NPCTalkReturn ntr = new S_NPCTalkReturn(objid, "orville8");
			target.sendPackets(ntr, true);
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_SOLDIER_GIVE_OK;
	}
}