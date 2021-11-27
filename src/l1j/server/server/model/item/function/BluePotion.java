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

import static l1j.server.server.model.skill.L1SkillId.DECAY_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION2;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BLUE_POTION3;

import java.util.Random;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class BluePotion extends L1ItemInstance {

	public BluePotion(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = this.getItemId();
			Random _rnd = new Random(System.nanoTime());
			if (itemId == 60206) // 시원한 마력 물약
				useMPPotion(pc, itemId, (8 + _rnd.nextInt(6)));
			else if (itemId == 60249) // 정신의 비약
				useMPPotion(pc, itemId, (6 + _rnd.nextInt(8)));
			else
				useBluePotion(pc, itemId);
			pc.getInventory().removeItem(useItem, 1);
			_rnd = null;
		}
	}

	private void useMPPotion(L1PcInstance pc, int itemid, int mp) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션
																		// 상태
			pc.sendPackets(new S_ServerMessage(698), true); // \f1마력에 의해 아무것도 마실
															// 수가 없습니다.
			return;
		}

		// 아브소르트바리아의 해제
		pc.cancelAbsoluteBarrier();
		pc.sendPackets(new S_SkillSound(pc.getId(), 190), true);
		Broadcaster
				.broadcastPacket(pc, new S_SkillSound(pc.getId(), 190), true);
		pc.setCurrentMp(pc.getCurrentMp() + mp);
	}

	private void useBluePotion(L1PcInstance pc, int item_id) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(DECAY_POTION)) { // 디케이포션
																		// 상태
			pc.sendPackets(new S_ServerMessage(698), true); // \f1마력에 의해 아무것도 마실
															// 수가 없습니다.
			return;
		}

		// 아브소르트바리아의 해제
		pc.cancelAbsoluteBarrier();

		int time = 0;
		int type = 0;
		switch (item_id) {
		case 40015:
		case 40736:
		case 60152:
			type = STATUS_BLUE_POTION;
			time = 600;
			break;
		case 41142:
			type = STATUS_BLUE_POTION2;
			time = 300;
			break;
		case 140015:
			type = STATUS_BLUE_POTION;
			time = 700;
			break;
		case 60166:
		case L1ItemId.MYSTERY_THICK_MANA_POTION:
			type = STATUS_BLUE_POTION3;
			time = 300;
			break;
		case 550004: // 복지 마력
			type = STATUS_BLUE_POTION;
			time = 2400;
			break;
		}
		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_BLUE_POTION))
			pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_BLUE_POTION);
		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_BLUE_POTION2))
			pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_BLUE_POTION2);
		if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_BLUE_POTION3))
			pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_BLUE_POTION3);

		pc.sendPackets(new S_SkillIconGFX(34, time), true);
		pc.sendPackets(new S_SkillSound(pc.getId(), 190), true);
		Broadcaster
				.broadcastPacket(pc, new S_SkillSound(pc.getId(), 190), true);

		pc.getSkillEffectTimerSet().setSkillEffect(type, time * 1000);

		pc.sendPackets(new S_ServerMessage(1007), true); // MP의 회복 속도가 빨라집니다.
	}
}
