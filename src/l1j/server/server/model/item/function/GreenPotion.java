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

package l1j.server.server.model.item.function;

import static l1j.server.server.model.skill.L1SkillId.GREATER_HASTE;
import static l1j.server.server.model.skill.L1SkillId.HASTE;
/*import static l1j.server.server.model.skill.L1SkillId.MASS_SLOW;*/
import static l1j.server.server.model.skill.L1SkillId.SLOW;
import static l1j.server.server.model.skill.L1SkillId.STATUS_HASTE;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class GreenPotion extends L1ItemInstance {

	public GreenPotion(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = useItem.getItemId();
			useGreenPotion(pc, itemId);
			pc.getInventory().removeItem(useItem, 1);
		}
	}

	private void useGreenPotion(L1PcInstance pc, int itemId) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션
																		// 상태
			pc.sendPackets(new S_ServerMessage(698), true); // \f1마력에 의해 아무것도 마실
															// 수가 없습니다.
			return;
		}

		// 아브소르트바리아의 해제
		pc.cancelAbsoluteBarrier();

		int time = 0;
		switch (itemId) {
		case L1ItemId.POTION_OF_HASTE_SELF:
		case 60313:
			time = 300;
			break;
		case 40030: //수련자의 촐기 부분
			time = 300;
			break;
		case 40018:
		case 41342:
			time = 1800;
			break;
		case 3000162:
			time = 1800;
			break;
		case 40039:
		case L1ItemId.MYSTERY_QUICK_POTION:
			time = 600;
			break;
		case 40040:
			time = 900;
			break;
		case 41261:
		case 41262:
		case 41268:
		case 41269:
		case 41271:
		case 41272:
		case 41273:
			time = 30;
			break;
		case 41338:
		case 140018:
			time = 2250;
			break;
		case L1ItemId.B_POTION_OF_HASTE_SELF:
			time = 350;
			break;
		case 550000:// 복지 속도
			time = 1200;
			break;
		}

		pc.sendPackets(new S_SkillSound(pc.getId(), 191), true);
		Broadcaster
				.broadcastPacket(pc, new S_SkillSound(pc.getId(), 191), true);
		// XXX:헤이스트아이템 장비시, 취한 상태가 해제되는지 불명
		if (pc.getHasteItemEquipped() > 0) {
			return;
		}
		// 취한 상태를 해제
		pc.setDrink(false);

		// 헤이 파업, 그레이터 헤이 파업과는 중복 하지 않는다
		if (pc.getSkillEffectTimerSet().hasSkillEffect(HASTE)) {
			pc.getSkillEffectTimerSet().killSkillEffectTimer(HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
			Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0),
					true);
			pc.getMoveState().setMoveSpeed(0);
		} else if (pc.getSkillEffectTimerSet().hasSkillEffect(GREATER_HASTE)) {
			pc.getSkillEffectTimerSet().killSkillEffectTimer(GREATER_HASTE);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
			Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0),
					true);
			pc.getMoveState().setMoveSpeed(0);
		}

		// 슬로우, 매스 슬로우, 엔탕르중은 슬로우 상태를 해제할 뿐
		if (pc.getSkillEffectTimerSet().hasSkillEffect(SLOW)) { // 슬로우
			pc.getSkillEffectTimerSet().killSkillEffectTimer(SLOW);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
			Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0),
					true);
			pc.getMoveState().setMoveSpeed(0);
		} else {
			int skilltime = pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.STATUS_HASTE);
			skilltime += time;
			if(skilltime >= 7200){
				skilltime = 7200;
			}
			pc.sendPackets(new S_SystemMessage("가속 효과: 이동 및 공격 속도 향상"), true);
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, skilltime), true);
			Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 1, 0),
					true);
			pc.getMoveState().setMoveSpeed(1);
			pc.getSkillEffectTimerSet().setSkillEffect(STATUS_HASTE,
					skilltime * 1000);
		}
	}
}
