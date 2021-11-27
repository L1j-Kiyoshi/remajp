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

import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.FREEZING_BREATH;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.POLLUTE_WATER;

import java.util.Random;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class HealingPotion extends L1ItemInstance {
	private static Random _random = new Random(System.nanoTime());

	public HealingPotion(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;

			if (cha.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND) || cha.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH) || cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.CURSE_PARALYZE)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SHOCK_STUN) || cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.엠파이어)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.FORCE_STUN) || cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.PANTERA)
					|| cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.쉐도우스탭)) {
				return;
			}

			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = useItem.getItemId();

			int delay_id = 0;
			delay_id = ((L1EtcItem) useItem.getItem()).get_delayid();
			if (delay_id != 0) { // 지연 설정 있어
				if (pc.hasItemDelay(delay_id) == true) {
					return;
				}
			}

			int item_minlvl = ((L1EtcItem) useItem.getItem()).getMinLevel();
			int item_maxlvl = ((L1EtcItem) useItem.getItem()).getMaxLevel();

			if (item_minlvl != 0 && item_minlvl > pc.getLevel() && !pc.isGm()) {
				pc.sendPackets(new S_ServerMessage(318, String.valueOf(item_minlvl)), true);
				// 이 아이템은%0레벨 이상이 되지 않으면 사용할 수 없습니다.
				return;
			} else if (item_maxlvl != 0 && item_maxlvl < pc.getLevel() && !pc.isGm()) {
				pc.sendPackets(new S_ServerMessage(673, String.valueOf(item_maxlvl)), true);
				// 이 아이템은%d레벨 이상만 사용할 수 있습니다.
				return;
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션
																			// 상태
				pc.sendPackets(new S_ServerMessage(698), true); // 마력에 의해 아무것도
																// 마실 수가 없습니다.
				return;
			}
			switch (itemId) {
			case 60071: // 위대한 치유의 주문
				if (pc.getLevel() >= 65)
					UseHeallingPotion(pc, 80 + _random.nextInt(41), 197);
				else
					UseHeallingPotion(pc, 120 + _random.nextInt(31), 197);
				break;
			case 60029: // 난쟁이주스
			case 60165: // 픽시의 힐링포션
				UseHeallingPotion(pc, 23 + _random.nextInt(16), 189);
				break;
			case 60030: // 난쟁이진액
				UseHeallingPotion(pc, 4 + _random.nextInt(39), 189);
				break;
			case 40010: // 빨갱이
			case 40019:
			case 40029:
			case 40022: // 신비한 새로 해야됨
			case 240010:
			case 140010:
			case 50752:

			case 60205: // 시원한 체력 물약
			case 60328:
			case 60412: // 승리의 체력 회복제
			case 600362:
				// UseHeallingPotion(pc, 9+_random.nextInt(37), 189);
				UseHeallingPotion(pc, 11 + _random.nextInt(25), 189);
				break;
			// case 60205: // 시원한 체력 물약
			// UseHeallingPotion(pc, 47+_random.nextInt(30), 189);
			// break;
			case 40011: // 주홍이
			case 40020:
			case 40023:
			case 140011:
			case 60248:
			case 60329:
			case 600363:
				UseHeallingPotion(pc, 33 + _random.nextInt(56), 194);
				break;
			case 40012: // 말갱이
			case 40021:
			case 40024:
			case 140012:
			case 600356:
			case 60725:
				UseHeallingPotion(pc, 45 + _random.nextInt(61), 197);// 81
				break;
			case 435000:
				UseHeallingPotion(pc, 33 + _random.nextInt(56), 189);// 81
				break;
			case 40026: // 바나나 주스
			case 40027:
			case 40028:
				UseHeallingPotion(pc, 11 + _random.nextInt(23), 189);
				break;
			case 6014: // 신비한 회복 물약
				UseHeallingPotion(pc, 141 + _random.nextInt(1044), 197);
				break;
			case 40043: // 토끼간
				UseHeallingPotion(pc, 141 + _random.nextInt(1044), 189);
				break;
			case 40058: // 그을린 빵조각
				UseHeallingPotion(pc, 18 + _random.nextInt(21), 189);
				break;
			case 40071: // 타다남은 빵조각
				UseHeallingPotion(pc, 33 + _random.nextInt(51), 197);
				break;
			case 40506: // 엔트의 열매
			case 140506:
				UseHeallingPotion(pc, 46 + _random.nextInt(78), 197);
				break;
			case 40734: // 신뢰의 코인
				UseHeallingPotion(pc, 50 + _random.nextInt(26), 189);
				break;
			case 40930: // 바베큐
				UseHeallingPotion(pc, 79 + _random.nextInt(105), 189);
				break;
			case 41298: // 어린 물고기
				UseHeallingPotion(pc, 8 + _random.nextInt(3), 189);
				break;
			case 41299: // 재빠른 물고기
				UseHeallingPotion(pc, 7 + _random.nextInt(17), 194);
				break;
			case 41300: // 강한 물고기
				UseHeallingPotion(pc, 11 + _random.nextInt(55), 197);
				break;
			case 41337: // 축복받은 보리빵
				UseHeallingPotion(pc, 44 + _random.nextInt(64), 197);
				break;
			case 41403: // 쿠작의 식량
				UseHeallingPotion(pc, 124 + _random.nextInt(477), 189);
				break;
			case 41411: // 은쫑즈
				UseHeallingPotion(pc, 10 + _random.nextInt(5), 189);
				break;
			case 41417:
			case 41418:
			case 41419:
			case 41420:
			case 41421:
				UseHeallingPotion(pc, 90 + _random.nextInt(23), 197);
				break;
			case 41141:
			case L1ItemId.MYSTERY_THICK_HEALING_POTION: // 신비한 농축 힐링 포션
				UseHeallingPotion(pc, 23 + _random.nextInt(34), 189);
				break;
			case 60258:// 마법의 알약
				UseHeallingPotion(pc, 55 + _random.nextInt(86), 197);
				break;
			case 60423:// 용의 호박석
				UseHeallingPotion(pc, 55 + _random.nextInt(81), 197);
				break;
			case 60443: // 크리스마스 쿠키
				if (pc.getLevel() >= 1 && pc.getLevel() <= 60)
					UseHeallingPotion(pc, 120 + _random.nextInt(31), 197);
				else
					UseHeallingPotion(pc, 80 + _random.nextInt(41), 197);
				break;
			}
			pc.getInventory().removeItem(useItem, 1);
			L1ItemDelay.onItemUse(pc, useItem); // 아이템 지연 개시

		}
	}

	private void UseHeallingPotion(L1PcInstance pc, int healHp, int gfxid) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(71) == true) { // 디케이포션
																		// 상태
			pc.sendPackets(new S_ServerMessage(698), true); // 마력에 의해 아무것도 마실 수가
															// 없습니다.
			return;
		}
		// 앱솔루트베리어의 해제
		pc.cancelAbsoluteBarrier();

		pc.sendPackets(new S_SkillSound(pc.getId(), gfxid), true);
		Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfxid), true);
		// pc.sendPackets(new S_ServerMessage(77)); // \f1기분이 좋아졌습니다.
		// healHp *= (_random.nextGaussian() / 5.0D) + 1.0D;
		// System.out.println(healHp);
		// ** 룸티스 푸른빛 귀걸이 끝 **//
		if (pc.getInventory().getEnchantEquipped(500008, 0)) { // 노인챈
			healHp += (healHp * 0.02) + 2;
		}
		if (pc.getInventory().getEnchantEquipped(500008, 1)) { // 1강
			healHp += (healHp * 0.06) + 6;
		}
		if (pc.getInventory().getEnchantEquipped(500008, 2)) { // 2강
			healHp += (healHp * 0.08) + 8;
		}
		if (pc.getInventory().getEnchantEquipped(500008, 3)) { // 3강
			healHp += (healHp * 0.10) + 10;
		}
		if (pc.getInventory().getEnchantEquipped(500008, 4)) { // 4강
			healHp += (healHp * 0.12) + 12;
		}
		if (pc.getInventory().getEnchantEquipped(500008, 5)) { // 5강
			healHp += (healHp * 0.14) + 14;
		}
		if (pc.getInventory().getEnchantEquipped(500008, 6)) { // 6강
			healHp += (healHp * 0.16) + 16;
		}
		if (pc.getInventory().getEnchantEquipped(500008, 7)) { // 7강
			healHp += (healHp * 0.18) + 18;
		}
		if (pc.getInventory().getEnchantEquipped(500008, 8)) { // 8강
			healHp += (healHp * 0.20) + 20;
		}
		// 투사의 문장
		if (pc.getInventory().getEnchantEquipped(30019, 1) || pc.getInventory().getEnchantEquipped(30020, 1) || pc.getInventory().getEnchantEquipped(30021, 1)
				|| pc.getInventory().getEnchantEquipped(30023, 1) || pc.getInventory().getEnchantEquipped(30024, 1)
				|| pc.getInventory().getEnchantEquipped(30025, 1)) { // 1강
			healHp += (healHp * 0.02) + 2;
		}
		if (pc.getInventory().getEnchantEquipped(30019, 2) || pc.getInventory().getEnchantEquipped(30020, 2) || pc.getInventory().getEnchantEquipped(30021, 2)
				|| pc.getInventory().getEnchantEquipped(30023, 2) || pc.getInventory().getEnchantEquipped(30024, 2)
				|| pc.getInventory().getEnchantEquipped(30025, 2)) { // 2강
			healHp += (healHp * 0.04) + 4;
		}
		if (pc.getInventory().getEnchantEquipped(30019, 3) || pc.getInventory().getEnchantEquipped(30020, 3) || pc.getInventory().getEnchantEquipped(30021, 3)
				|| pc.getInventory().getEnchantEquipped(30023, 3) || pc.getInventory().getEnchantEquipped(30024, 3)
				|| pc.getInventory().getEnchantEquipped(30025, 3)) { // 3강
			healHp += (healHp * 0.08) + 8;
		}
		if (pc.getInventory().getEnchantEquipped(30019, 4) || pc.getInventory().getEnchantEquipped(30020, 4) || pc.getInventory().getEnchantEquipped(30021, 4)
				|| pc.getInventory().getEnchantEquipped(30023, 4) || pc.getInventory().getEnchantEquipped(30024, 4)
				|| pc.getInventory().getEnchantEquipped(30025, 4)) { // 4강
			healHp += (healHp * 0.10) + 10;
		}
		if (pc.getInventory().getEnchantEquipped(30019, 5) || pc.getInventory().getEnchantEquipped(30020, 5) || pc.getInventory().getEnchantEquipped(30021, 5)
				|| pc.getInventory().getEnchantEquipped(30023, 5) || pc.getInventory().getEnchantEquipped(30024, 5)
				|| pc.getInventory().getEnchantEquipped(30025, 5)) { // 5강
			healHp += (healHp * 0.12) + 12;
		}
		if (pc.getInventory().getEnchantEquipped(30019, 6) || pc.getInventory().getEnchantEquipped(30020, 6) || pc.getInventory().getEnchantEquipped(30021, 6)
				|| pc.getInventory().getEnchantEquipped(30023, 6) || pc.getInventory().getEnchantEquipped(30024, 6)
				|| pc.getInventory().getEnchantEquipped(30025, 6)) { // 6강
			healHp += (healHp * 0.14) + 14;
		}
		if (pc.getInventory().getEnchantEquipped(30019, 7) || pc.getInventory().getEnchantEquipped(30020, 7) || pc.getInventory().getEnchantEquipped(30021, 7)
				|| pc.getInventory().getEnchantEquipped(30023, 7) || pc.getInventory().getEnchantEquipped(30024, 7)
				|| pc.getInventory().getEnchantEquipped(30025, 7)) { // 7강
			healHp += (healHp * 0.16) + 16;
		}
		if (pc.getInventory().getEnchantEquipped(30019, 8) || pc.getInventory().getEnchantEquipped(30020, 8) || pc.getInventory().getEnchantEquipped(30021, 8)
				|| pc.getInventory().getEnchantEquipped(30023, 8) || pc.getInventory().getEnchantEquipped(30024, 8)
				|| pc.getInventory().getEnchantEquipped(30025, 8)) { // 8강
			healHp += (healHp * 0.18) + 18;
		}

		if (pc.getInventory().getEnchantEquipped(502008, 3)) { // 4강
			healHp += (healHp * 0.12) + 12;
		}
		if (pc.getInventory().getEnchantEquipped(502008, 4)) { // 5강
			healHp += (healHp * 0.14) + 14;
		}
		if (pc.getInventory().getEnchantEquipped(502008, 5)) { // 6강
			healHp += (healHp * 0.16) + 16;
		}
		if (pc.getInventory().getEnchantEquipped(502008, 6)) { // 7강
			healHp += (healHp * 0.18) + 18;
		}
		if (pc.getInventory().getEnchantEquipped(502008, 7)) { // 8강
			healHp += (healHp * 0.20) + 20;
		}
		if (pc.getInventory().getEnchantEquipped(502008, 8)) { // 8강
			healHp += (healHp * 0.22) + 22;
		}

		/** 성장의문장 **/
		if (pc.getInventory().getEnchantEquipped(490020, 1)) { // 1강
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 2)) { // 2강
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 3)) { // 3강
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 4)) { // 4강
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 5)) { // 5강
			healHp = (int) (healHp + (healHp * 0.09D + 9.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 6)) { // 6강
			healHp = (int) (healHp + (healHp * 0.10D + 10.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 7)) { // 7강
			healHp = (int) (healHp + (healHp * 0.11D + 11.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 8)) { // 8강
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 9)) { // 9강
			healHp = (int) (healHp + (healHp * 0.13D + 13.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490020, 10)) { // 10강
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		/** 회복의문장 **/
		if (pc.getInventory().getEnchantEquipped(490022, 1)) { // 1인첸
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 2)) { // 2인첸
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 3)) { // 3인첸
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 4)) { // 4인첸
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 5)) { // 5인첸
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 6)) { // 6인첸
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 7)) { // 7인첸
			healHp = (int) (healHp + (healHp * 0.16D + 16.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 8)) { // 8인첸
			healHp = (int) (healHp + (healHp * 0.18D + 18.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 9)) { // 9인첸
			healHp = (int) (healHp + (healHp * 0.20D + 20.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490022, 10)) { // 10인첸
			healHp = (int) (healHp + (healHp * 0.22D + 22.0D));
		}
		/** 완력의문장 **/
		if (pc.getInventory().getEnchantEquipped(490023, 1)) { // 1강
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 2)) { // 2강
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 3)) { // 3강
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 4)) { // 4강
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 5)) { // 5강
			healHp = (int) (healHp + (healHp * 0.09D + 9.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 6)) { // 6강
			healHp = (int) (healHp + (healHp * 0.10D + 10.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 7)) { // 7강
			healHp = (int) (healHp + (healHp * 0.11D + 11.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 8)) { // 8강
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 9)) { // 9강
			healHp = (int) (healHp + (healHp * 0.13D + 13.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490023, 10)) { // 10강
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		/** 민첩의문장 **/
		if (pc.getInventory().getEnchantEquipped(490024, 1)) { // 1강
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 2)) { // 2강
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 3)) { // 3강
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 4)) { // 4강
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 5)) { // 5강
			healHp = (int) (healHp + (healHp * 0.09D + 9.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 6)) { // 6강
			healHp = (int) (healHp + (healHp * 0.10D + 10.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 7)) { // 7강
			healHp = (int) (healHp + (healHp * 0.11D + 11.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 8)) { // 8강
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 9)) { // 9강
			healHp = (int) (healHp + (healHp * 0.13D + 13.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490024, 10)) { // 10강
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		/** 지식의문장 **/
		if (pc.getInventory().getEnchantEquipped(490025, 1)) { // 1강
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 2)) { // 2강
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 3)) { // 3강
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 4)) { // 4강
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 5)) { // 5강
			healHp = (int) (healHp + (healHp * 0.09D + 9.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 6)) { // 6강
			healHp = (int) (healHp + (healHp * 0.10D + 10.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 7)) { // 7강
			healHp = (int) (healHp + (healHp * 0.11D + 11.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 8)) { // 8강
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 9)) { // 9강
			healHp = (int) (healHp + (healHp * 0.13D + 13.0D));
		}
		if (pc.getInventory().getEnchantEquipped(490025, 10)) { // 10강
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		/** 완력성장의문장 **/
		if (pc.getInventory().getEnchantEquipped(30007, 1)) { // 1강
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 2)) { // 2강
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 3)) { // 3강
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 4)) { // 4강
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 5)) { // 5강
			healHp = (int) (healHp + (healHp * 0.09D + 9.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 6)) { // 6강
			healHp = (int) (healHp + (healHp * 0.10D + 10.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 7)) { // 7강
			healHp = (int) (healHp + (healHp * 0.11D + 11.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 8)) { // 8강
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 9)) { // 9강
			healHp = (int) (healHp + (healHp * 0.13D + 13.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30007, 10)) { // 10강
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		/** 민첩성장의문장 **/
		if (pc.getInventory().getEnchantEquipped(30008, 1)) { // 1강
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 2)) { // 2강
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 3)) { // 3강
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 4)) { // 4강
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 5)) { // 5강
			healHp = (int) (healHp + (healHp * 0.09D + 9.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 6)) { // 6강
			healHp = (int) (healHp + (healHp * 0.10D + 10.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 7)) { // 7강
			healHp = (int) (healHp + (healHp * 0.11D + 11.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 8)) { // 8강
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 9)) { // 9강
			healHp = (int) (healHp + (healHp * 0.13D + 13.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30008, 10)) { // 10강
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		/** 지식성장의문장 **/
		if (pc.getInventory().getEnchantEquipped(30009, 1)) { // 1강
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 2)) { // 2강
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 3)) { // 3강
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 4)) { // 4강
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 5)) { // 5강
			healHp = (int) (healHp + (healHp * 0.09D + 9.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 6)) { // 6강
			healHp = (int) (healHp + (healHp * 0.10D + 10.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 7)) { // 7강
			healHp = (int) (healHp + (healHp * 0.11D + 11.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 8)) { // 8강
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 9)) { // 9강
			healHp = (int) (healHp + (healHp * 0.13D + 13.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30009, 10)) { // 10강
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		/** 완력회복의문장 **/
		if (pc.getInventory().getEnchantEquipped(30010, 1)) { // 1인첸
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 2)) { // 2인첸
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 3)) { // 3인첸
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 4)) { // 4인첸
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 5)) { // 5인첸
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 6)) { // 6인첸
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 7)) { // 7인첸
			healHp = (int) (healHp + (healHp * 0.16D + 16.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 8)) { // 8인첸
			healHp = (int) (healHp + (healHp * 0.18D + 18.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 9)) { // 9인첸
			healHp = (int) (healHp + (healHp * 0.20D + 20.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30010, 10)) { // 10인첸
			healHp = (int) (healHp + (healHp * 0.22D + 22.0D));
		}
		/** 민첩회복의문장 **/
		if (pc.getInventory().getEnchantEquipped(30011, 1)) { // 1인첸
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 2)) { // 2인첸
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 3)) { // 3인첸
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 4)) { // 4인첸
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 5)) { // 5인첸
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 6)) { // 6인첸
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 7)) { // 7인첸
			healHp = (int) (healHp + (healHp * 0.16D + 16.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 8)) { // 8인첸
			healHp = (int) (healHp + (healHp * 0.18D + 18.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 9)) { // 9인첸
			healHp = (int) (healHp + (healHp * 0.20D + 20.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30011, 10)) { // 10인첸
			healHp = (int) (healHp + (healHp * 0.22D + 22.0D));
		}
		/** 지식회복의문장 **/
		if (pc.getInventory().getEnchantEquipped(30012, 1)) { // 1인첸
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 2)) { // 2인첸
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 3)) { // 3인첸
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 4)) { // 4인첸
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 5)) { // 5인첸
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 6)) { // 6인첸
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 7)) { // 7인첸
			healHp = (int) (healHp + (healHp * 0.16D + 16.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 8)) { // 8인첸
			healHp = (int) (healHp + (healHp * 0.18D + 18.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 9)) { // 9인첸
			healHp = (int) (healHp + (healHp * 0.20D + 20.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30012, 10)) { // 10인첸
			healHp = (int) (healHp + (healHp * 0.22D + 22.0D));
		}
		/** 성장회복의문장 **/
		if (pc.getInventory().getEnchantEquipped(30013, 1)) { // 1인첸
			healHp = (int) (healHp + (healHp * 0.02D + 2.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 2)) { // 2인첸
			healHp = (int) (healHp + (healHp * 0.04D + 4.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 3)) { // 3인첸
			healHp = (int) (healHp + (healHp * 0.06D + 6.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 4)) { // 4인첸
			healHp = (int) (healHp + (healHp * 0.08D + 8.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 5)) { // 5인첸
			healHp = (int) (healHp + (healHp * 0.12D + 12.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 6)) { // 6인첸
			healHp = (int) (healHp + (healHp * 0.14D + 14.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 7)) { // 7인첸
			healHp = (int) (healHp + (healHp * 0.16D + 16.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 8)) { // 8인첸
			healHp = (int) (healHp + (healHp * 0.18D + 18.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 9)) { // 9인첸
			healHp = (int) (healHp + (healHp * 0.20D + 20.0D));
		}
		if (pc.getInventory().getEnchantEquipped(30013, 10)) { // 10인첸
			healHp = (int) (healHp + (healHp * 0.22D + 22.0D));
		}
		if (pc.survive && pc.isFencer()) {
			int rnd = _random.nextInt(100);
			double per1 = 45.000 / 100.000;
			double per2 = 25.000 / 100.000;
			double per3 = 15.000 / 100.000;
			int survivehp1 = (int) ((double) pc.getMaxHp() * per1);
			int survivehp2 = (int) ((double) pc.getMaxHp() * per2);
			int survivehp3 = (int) ((double) pc.getMaxHp() * per3);
			if (survivehp1 >= pc.getCurrentHp() && survivehp2 < pc.getCurrentHp()) {
				if (rnd <= 33) {
					pc.sendPackets(new S_SkillSound(pc.getId(), 18566), true);
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18566), true);
					healHp += (int) (pc.getAbility().getTotalCon());
				}
			} else if (survivehp2 >= pc.getCurrentHp() && survivehp3 < pc.getCurrentHp()) {
				if (rnd <= 33) {
					pc.sendPackets(new S_SkillSound(pc.getId(), 18568), true);
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18568), true);
					healHp += (int) (pc.getAbility().getTotalCon() * 2);
				}
			} else if (survivehp3 >= pc.getCurrentHp()) {
				if (rnd <= 33) {
					pc.sendPackets(new S_SkillSound(pc.getId(), 18570), true);
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18570), true);
					healHp += (int) (pc.getAbility().getTotalCon() * 3);
				}
			}
		}

		if (pc.getPotionPlus() > 0) {
			double per = pc.getPotionPlus() / 100.000;

			// System.out.println("per :"+per);
			int addhp = (int) ((double) healHp * per);
			// System.out.println("원래회복 :"+healHp);
			// System.out.println("회복증가 :"+addhp);
			healHp += addhp + pc.getPotionPlus();
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.데스페라도)) {
			int atklv = pc.데스페라도공격자레벨;
			int dflv = pc.getLevel();
			double 저하률 = 0.45;
			if (atklv > dflv) {
				저하률 += (atklv - dflv) * 0.05;
			}
			if (pc.getInventory().checkEquipped(490022)) {// 회복문장
				int enchant = pc.getInventory().getEnchantCount(490022);
				저하률 += (enchant < 1 ? 2 : (enchant + 1) * 2) / 100;
			}
			if (pc.getInventory().checkEquipped(500008)) {// 룸티스
				int enchant = pc.getInventory().getEnchantCount(500008);
				저하률 += (enchant < 1 ? 2 : (enchant + 1) * 2) / 100;
			}
			if (pc.getInventory().checkEquipped(502008)) {// 축 룸티스
				int enchant = pc.getInventory().getEnchantCount(502008);
				저하률 += (enchant < 1 ? 2 : (enchant + 1) * 2) / 100;
			}
			if (저하률 > 0.9) {
				저하률 = 0.9;
			}

			healHp -= (int) ((double) healHp * 저하률);
		} else if (pc.getSkillEffectTimerSet().hasSkillEffect(POLLUTE_WATER) || pc.getSkillEffectTimerSet().hasSkillEffect(10517)) { // 포르트워타중은
																																		// 회복량1/2배
			healHp /= 2;
		}

		if (pc.getSkillEffectTimerSet().hasSkillEffect(10513)) {
			pc.receiveDamage(pc, healHp, true);
		} else if (pc.isPhantomDeathed()) {
			pc.receiveDamage(pc, healHp, true);
		} else {
			pc.setCurrentHp(pc.getCurrentHp() + healHp);
		}
	}
}
