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
package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_0_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_10_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_10_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_11_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_11_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_12_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_12_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_13_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_13_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_14_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_15_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_15_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_16_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_17_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_17_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_18_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_18_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_19_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_19_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_1_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_1_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_20_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_20_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_21_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_21_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_22_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_23_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_23_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_2_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_3_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_3_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_4_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_4_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_5_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_5_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_6_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_7_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_8_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_9_N;
import static l1j.server.server.model.skill.L1SkillId.COOKING_1_9_S;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_닭고기;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_연어;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_칠면조;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_한우;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_탐닭고기;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_탐연어;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_탐칠면조;
import static l1j.server.server.model.skill.L1SkillId.COOKING_NEW_탐한우;
import static l1j.server.server.model.skill.L1SkillId.SPECIAL_COOKING;
import static l1j.server.server.model.skill.L1SkillId.SPECIAL_COOKING2;
import static l1j.server.server.model.skill.L1SkillId.메티스정성스프;
import static l1j.server.server.model.skill.L1SkillId.메티스정성요리;
import static l1j.server.server.model.skill.L1SkillId.싸이매콤한라면;
import static l1j.server.server.model.skill.L1SkillId.싸이시원한음료;
import static l1j.server.server.model.skill.L1SkillId.천하장사버프;

import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;

// Referenced classes of package l1j.server.server.model:
// L1Cooking

public class L1Cooking {

	private L1Cooking() {
	}

	public static void useNewCookingItem(L1PcInstance pc, L1ItemInstance item) {
		int itemId = item.getItem().getItemId();
		int cookingId = 0;
		switch (itemId) {
		case 60075:// 버섯스프 // 닭고기 스프
		case 60729:
			cookingId = COOKING_NEW_닭고기;
			break;
		case 60073:// 키위패롯구이 // 날샌 연어 찜
		case 60727:
			cookingId = COOKING_NEW_연어;
			break;
		case 60074:// 거미다리꼬치구이 // 영리한 칠면조 구이
		case 60728:
			cookingId = COOKING_NEW_칠면조;
			break;
		case 60072:// 코카스테이크 // 한우 스테이크
		case 60726:
			cookingId = COOKING_NEW_한우;
			break;
		case 60259:
			cookingId = 싸이매콤한라면;
			break;
		case 60260:
			cookingId = 싸이시원한음료;
			break;
		case 600353:// 버섯스프 // 닭고기 스프
			cookingId = COOKING_NEW_탐닭고기;
			break;
		case 600351:// 키위패롯구이 // 날샌 연어 찜
			cookingId = COOKING_NEW_탐연어;
			break;
		case 600352:// 거미다리꼬치구이 // 영리한 칠면조 구이
			cookingId = COOKING_NEW_탐칠면조;
			break;
		case 600350:// 코카스테이크 // 한우 스테이크
			cookingId = COOKING_NEW_탐한우;
			break;
		/*
		 * case 11111://메티스의 정성스런 요리 cookingId = 메티스정성요리; break; case 111112://메티스의 정성스런
		 * 요리 cookingId = 메티스정성스프; break;
		 */
		default:
			return;
		}
		if (cookingId == 메티스정성요리 || cookingId == 싸이매콤한라면 || cookingId == 싸이시원한음료)
			newEatCooking(pc, cookingId, 920);
		else if (cookingId == 메티스정성스프)
			newEatCooking(pc, cookingId, 900);
		else
			newEatCooking(pc, cookingId, 1800);
		if (cookingId != 싸이매콤한라면 && cookingId != 싸이시원한음료)
			pc.sendPackets(new S_ServerMessage(76, item.getNumberedName(1)));
		pc.getInventory().removeItem(item, 1);
		pc.sendPackets(new S_EffectLocation(pc.getX(), pc.getY(), 6392));
	}

	public static void newEatCooking(L1PcInstance pc, int cookingId, int time) {
		int cookingType = 0;
		switch (cookingId) {
		case 싸이매콤한라면:
			cookingType = 161;
			pc.addHitup(2);
			pc.addDmgup(2);
			pc.addBowHitup(2);
			pc.addBowDmgup(2);
			pc.addHpr(3);
			pc.addMpr(4);
			pc.getAbility().addSp(2);
			pc.getResistance().addMr(15);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case 싸이시원한음료:
			cookingType = 162;
			break;
		case COOKING_NEW_닭고기:
			cookingType = 160;

			break;
		case 메티스정성스프:
			cookingType = 162;
			break;
		case COOKING_NEW_연어:// 키위패롯구이 // 날샌 연어 찜
			cookingType = 158;
			pc.addBowHitup(1);
			pc.addBowDmgup(2);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));

			break;
		case COOKING_NEW_칠면조:
			cookingType = 159;
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getAbility().addSp(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));

			break;
		case COOKING_NEW_한우:
			cookingType = 157;
			pc.addHitup(1);
			pc.addDmgup(2);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));

			break;
		case COOKING_NEW_탐한우:
			cookingType = 215;
			pc.addHitup(1);
			pc.addDmgup(2);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.addTechniqueHit(3);
			pc.addSpiritHit(3);
			pc.addDragonLangHit(3);
			pc.addFearHit(3);

			break;
		case COOKING_NEW_탐연어:// 키위패롯구이 // 날샌 연어 찜
			cookingType = 216;
			pc.addBowHitup(1);
			pc.addBowDmgup(2);
			pc.addHpr(2);
			pc.addMpr(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.addTechniqueHit(3);
			pc.addSpiritHit(3);
			pc.addDragonLangHit(3);
			pc.addFearHit(3);

			break;
		case COOKING_NEW_탐칠면조:
			cookingType = 217;
			pc.addHpr(2);
			pc.addMpr(3);
			pc.getAbility().addSp(2);
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			pc.addTechniqueHit(3);
			pc.addSpiritHit(3);
			pc.addDragonLangHit(3);
			pc.addFearHit(3);

			break;
		case COOKING_NEW_탐닭고기:
			cookingType = 218;
			pc.addTechniqueTolerance(2);
			pc.addSpiritTolerance(2);
			pc.addDragonLangTolerance(2);
			pc.addFearTolerance(2);

			break;
		case 메티스정성요리:
			cookingType = 151;
			pc.addHitup(2);
			pc.addDmgup(2);
			pc.addBowHitup(2);
			pc.addBowDmgup(2);
			pc.getAbility().addSp(2);
			pc.addHpr(3);
			pc.addMpr(4);
			pc.getResistance().addMr(15);
			pc.sendPackets(new S_SPMR(pc));
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case 천하장사버프:
			cookingType = 187;
			pc.addDamageReductionByArmor(5);
			break;
		default:
			return;
		}

		int cookingId2 = 0;
		if (cookingId == COOKING_NEW_닭고기 || cookingId == 메티스정성스프 || cookingId == 싸이시원한음료 || cookingId == 천하장사버프 || cookingId == COOKING_NEW_탐닭고기)
			cookingId2 = pc.getDessertId();
		else
			cookingId2 = pc.getCookingId();
		if (cookingId2 != 0) {
			pc.getSkillEffectTimerSet().removeSkillEffect(cookingId2);
		}

		pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, pc, cookingType, time));
		pc.getSkillEffectTimerSet().setSkillEffect(cookingId, time * 1000);
		if (cookingId == COOKING_NEW_닭고기 || cookingId == 메티스정성스프 || cookingId == 싸이시원한음료 || cookingId == 천하장사버프 || cookingId == COOKING_NEW_탐닭고기)
			pc.setDessertId(cookingId);
		else
			pc.setCookingId(cookingId);

		if (pc.get_food() < 225) {
			pc.set_food(pc.get_food() + 12);// 요리먹을때 배고픔게이지 올리부분..
			if (pc.get_food() > 225)
				pc.set_food(225);
			pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
		}
		pc.sendPackets(new S_OwnCharStatus(pc));
	}

	public static void useCookingItem(L1PcInstance pc, L1ItemInstance item) {
		int itemId = item.getItem().getItemId();
		/*
		 * if (itemId == 41284 //버섯 스프 || itemId == 49056 //크랩살 스프 || itemId == 49064
		 * //환상의 크랩살 스프 || itemId == 41292 // 환상의 버섯스프 || itemId ==
		 * L1ItemId.COOKFOOD_BASILIST_EGG_SOUP //바실리스크 알 스프 || itemId ==
		 * L1ItemId.SCOOKFOOD_BASILIST_EGG_SOUP) { //환상의 바실리스크 알 스프 if (pc.get_food() !=
		 * 225) { // 100% pc.sendPackets(new
		 * S_SystemMessage("해당 요리는 포만감 100%에서만 먹을수 있습니다.")); // pc.sendPackets(new
		 * S_ServerMessage(74, item.getNumberedName(1))); return; }
		 * 
		 * }
		 */

		if (itemId >= 41277 && itemId <= 41283 // 1차 요리
				|| itemId >= 49049 && itemId <= 49056// 2차 요리
				|| itemId >= L1ItemId.NORMAL_COOKFOOD_3RD_START && itemId <= L1ItemId.COOKFOOD_DEEP_SEA_FISH_STEW// 3차 요리
				|| itemId >= 41285 && itemId <= 41291// 1차 환상의 요리
				|| itemId >= 49057 && itemId <= 49064// 2차 환상의 요리
				|| itemId >= L1ItemId.SPECIAL_COOKFOOD_3RD_START && itemId <= L1ItemId.SCOOKFOOD_DEEP_SEA_FISH_STEW) {// 3차 환상의
																														// 요리
			int cookingId = pc.getCookingId();
			if (cookingId != 0) {
				pc.getSkillEffectTimerSet().removeSkillEffect(cookingId);
			}
		}

		if (itemId == 41284 // 버섯 스프
				|| itemId == 49056// 크랩살 스프
				|| itemId == 49064// 환상의 크랩살 스프
				|| itemId == 41292// 환상의 버섯 스프
				|| itemId == L1ItemId.COOKFOOD_BASILIST_EGG_SOUP // 바실리스크 알 스프
				|| itemId == L1ItemId.SCOOKFOOD_BASILIST_EGG_SOUP) {// 환상의 바실리스크
																	// 알 스프
			int dessertId = pc.getDessertId();
			if (dessertId != 0) {
				pc.getSkillEffectTimerSet().removeSkillEffect(dessertId);
			}
		}

		/** 1차 요리 효과 */
		int cookingId;
		int time = 900;
		switch (itemId) {
		case 41277:
		case 41285:
			if (itemId == 41277) {
				cookingId = COOKING_1_0_N;
			} else {
				cookingId = COOKING_1_0_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41278:
		case 41286:
			if (itemId == 41278) {
				cookingId = COOKING_1_1_N;
			} else {
				cookingId = COOKING_1_1_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41279:
		case 41287:
			if (itemId == 41279) {
				cookingId = COOKING_1_2_N;
			} else {
				cookingId = COOKING_1_2_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41280:
		case 41288:
			if (itemId == 41280) {
				cookingId = COOKING_1_3_N;
			} else {
				cookingId = COOKING_1_3_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41281:
		case 41289:
			if (itemId == 41281) {
				cookingId = COOKING_1_4_N;
			} else {
				cookingId = COOKING_1_4_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41282:
		case 41290:
			if (itemId == 41282) {
				cookingId = COOKING_1_5_N;
			} else {
				cookingId = COOKING_1_5_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41283:
		case 41291:
			if (itemId == 41283) {
				cookingId = COOKING_1_6_N;
			} else {
				cookingId = COOKING_1_6_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 41284:
		case 41292:
			if (itemId == 41284) {
				cookingId = COOKING_1_7_N;
			} else {
				cookingId = COOKING_1_7_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49049:
		case 49057:
			if (itemId == 49049) {
				cookingId = COOKING_1_8_N;
			} else {
				cookingId = COOKING_1_8_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49050:
		case 49058:
			if (itemId == 49050) {
				cookingId = COOKING_1_9_N;
			} else {
				cookingId = COOKING_1_9_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49051:
		case 49059:
			if (itemId == 49051) {
				cookingId = COOKING_1_10_N;
			} else {
				cookingId = COOKING_1_10_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49052:
		case 49060:
			if (itemId == 49052) {
				cookingId = COOKING_1_11_N;
			} else {
				cookingId = COOKING_1_11_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49053:
		case 49061:
			if (itemId == 49053) {
				cookingId = COOKING_1_12_N;
			} else {
				cookingId = COOKING_1_12_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49054:
		case 49062:
			if (itemId == 49054) {
				cookingId = COOKING_1_13_N;
			} else {
				cookingId = COOKING_1_13_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49055:
		case 49063:
			if (itemId == 49055) {
				cookingId = COOKING_1_14_N;
			} else {
				cookingId = COOKING_1_14_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case 49056:
		case 49064:
			if (itemId == 49056) {
				cookingId = COOKING_1_15_N;
			} else {
				cookingId = COOKING_1_15_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case L1ItemId.COOKFOOD_CRUSTCEA_CLAW_CHARCOAL:
		case L1ItemId.SCOOKFOOD_CRUSTCEA_CLAW_CHARCOAL:
			if (itemId == L1ItemId.COOKFOOD_CRUSTCEA_CLAW_CHARCOAL) {
				cookingId = COOKING_1_16_N;
			} else {
				cookingId = COOKING_1_16_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case L1ItemId.COOKFOOD_GRIFFON_CHARCOAL:
		case L1ItemId.SCOOKFOOD_GRIFFON_CHARCOAL:
			if (itemId == L1ItemId.COOKFOOD_GRIFFON_CHARCOAL) {
				cookingId = COOKING_1_17_N;
			} else {
				cookingId = COOKING_1_17_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case L1ItemId.COOKFOOD_COCKATRICE_STEAK:
		case L1ItemId.SCOOKFOOD_COCKATRICE_STEAK:
			if (itemId == L1ItemId.COOKFOOD_COCKATRICE_STEAK) {
				cookingId = COOKING_1_18_N;
			} else {
				cookingId = COOKING_1_18_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case L1ItemId.COOKFOOD_TURTLEKING_CHARCOAL:
		case L1ItemId.SCOOKFOOD_TURTLEKING_CHARCOAL:
			if (itemId == L1ItemId.COOKFOOD_TURTLEKING_CHARCOAL) {
				cookingId = COOKING_1_19_N;
			} else {
				cookingId = COOKING_1_19_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case L1ItemId.COOKFOOD_LESSERDRAGON_WING_SKEWER:
		case L1ItemId.SCOOKFOOD_LESSERDRAGON_WING_SKEWER:
			if (itemId == L1ItemId.COOKFOOD_LESSERDRAGON_WING_SKEWER) {
				cookingId = COOKING_1_20_N;
			} else {
				cookingId = COOKING_1_20_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case L1ItemId.COOKFOOD_DRAKE_CHARCOAL:
		case L1ItemId.SCOOKFOOD_DRAKE_CHARCOAL:
			if (itemId == L1ItemId.COOKFOOD_DRAKE_CHARCOAL) {
				cookingId = COOKING_1_21_N;
			} else {
				cookingId = COOKING_1_21_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case L1ItemId.COOKFOOD_DEEP_SEA_FISH_STEW:
		case L1ItemId.SCOOKFOOD_DEEP_SEA_FISH_STEW:
			if (itemId == L1ItemId.COOKFOOD_DEEP_SEA_FISH_STEW) {
				cookingId = COOKING_1_22_N;
			} else {
				cookingId = COOKING_1_22_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		case L1ItemId.COOKFOOD_BASILIST_EGG_SOUP:
		case L1ItemId.SCOOKFOOD_BASILIST_EGG_SOUP:
			if (itemId == L1ItemId.COOKFOOD_BASILIST_EGG_SOUP) {
				cookingId = COOKING_1_23_N;
			} else {
				cookingId = COOKING_1_23_S;
			}
			eatCooking(pc, cookingId, time);
			break;
		}

		pc.sendPackets(new S_ServerMessage(76, item.getNumberedName(1)));
		pc.getInventory().removeItem(item, 1);
	}

	/** 1차요리 효과 */
	public static void eatCooking(L1PcInstance pc, int cookingId, int time) {
		int cookingType = 0;

		switch (cookingId) {
		case COOKING_1_0_N:// 괴물눈 스테이크
		case COOKING_1_0_S:
			cookingType = 0;
			pc.getResistance().addAllNaturalResistance(10);
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case COOKING_1_1_N:// 곰고기 구이
		case COOKING_1_1_S:
			cookingType = 1;
			pc.addMaxHp(30);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				pc.getParty().updateMiniHP(pc);
			}
			break;
		case COOKING_1_2_N:// 씨호떡
		case COOKING_1_2_S:
			pc.addMpr(3);
			cookingType = 2;
			break;
		case COOKING_1_3_N:// 개미다리치즈구이
		case COOKING_1_3_S:
			cookingType = 3;
			pc.getAC().addAc(-1);
			// pc.sendPackets(new S_OwnCharStatus(pc));
			break;
		case COOKING_1_4_N:// 과일샐러드
		case COOKING_1_4_S:
			cookingType = 4;
			pc.addMaxMp(20);
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			break;
		case COOKING_1_5_N:// 과일탕수육
		case COOKING_1_5_S:
			cookingType = 5;
			pc.addHpr(3);
			break;
		case COOKING_1_6_N:// 멧돼지 꼬치 구이
		case COOKING_1_6_S:
			cookingType = 6;
			pc.getResistance().addMr(5);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOKING_1_7_N:// 버섯스프
		case COOKING_1_7_S:
			cookingType = 7;
			break;
		/** 1차요리 효과끝 */
		case COOKING_1_8_N:// 캐비어카나페
		case COOKING_1_8_S:
			cookingType = 16;
			pc.addHitup(1);
			pc.addDmgup(1);
			break;
		case COOKING_1_9_N:// 악어스테이크
		case COOKING_1_9_S:
			cookingType = 17;
			pc.addMaxHp(30);
			pc.addMaxMp(30);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				pc.getParty().updateMiniHP(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			break;
		case COOKING_1_10_N:// 터틀드래곤과자
		case COOKING_1_10_S:
			cookingType = 18;
			pc.getAC().addAc(-2);
			pc.sendPackets(new S_OwnCharStatus2(pc));
			break;
		case COOKING_1_11_N:// 키위패롯구이
		case COOKING_1_11_S:
			pc.addBowHitup(1);
			pc.addBowDmgup(1);
			cookingType = 19;
			break;
		case COOKING_1_12_N:// 스콜피온구이
		case COOKING_1_12_S:
			pc.addHpr(2);
			pc.addMpr(2);
			cookingType = 20;
			break;
		case COOKING_1_13_N:// 일렉카둠스튜
		case COOKING_1_13_S:
			cookingType = 21;
			pc.getResistance().addMr(10);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOKING_1_14_N:// 거미다리꼬치구이
		case COOKING_1_14_S:
			cookingType = 22;
			pc.getAbility().addSp(1);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOKING_1_15_N:// 크랩살스프
		case COOKING_1_15_S:
			cookingType = 23;
			break;
		/** 2차요리 효과끝 */
		case COOKING_1_16_N:// 시안집게발구이
		case COOKING_1_16_S:
			cookingType = 45;
			pc.addBowHitup(2);
			pc.addBowDmgup(1);
			break;
		case COOKING_1_17_N:// 그리폰구이
		case COOKING_1_17_S:
			cookingType = 46;
			pc.addMaxHp(50);
			pc.addMaxMp(50);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				pc.getParty().updateMiniHP(pc);
			}
			pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
			break;
		case COOKING_1_18_N:// 코카스테이크
		case COOKING_1_18_S:
			cookingType = 47;
			pc.addHitup(2);
			pc.addDmgup(1);
			break;
		case COOKING_1_19_N:// 대왕거북구이
		case COOKING_1_19_S:
			cookingType = 48;
			pc.getAC().addAc(-3);
			pc.sendPackets(new S_OwnCharStatus2(pc));
			break;
		case COOKING_1_20_N:// 레서날개꼬치
		case COOKING_1_20_S:
			cookingType = 49;
			pc.getResistance().addAllNaturalResistance(10);
			pc.getResistance().addMr(15);
			pc.sendPackets(new S_SPMR(pc));
			pc.sendPackets(new S_OwnCharAttrDef(pc));
			break;
		case COOKING_1_21_N:// 드레이크구이
		case COOKING_1_21_S:
			cookingType = 50;
			pc.addMpr(2);
			pc.getAbility().addSp(2);
			pc.sendPackets(new S_SPMR(pc));
			break;
		case COOKING_1_22_N:// 심해어스튜
		case COOKING_1_22_S:
			cookingType = 51;
			pc.addHpr(2);
			pc.addMaxHp(30);
			pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
			if (pc.isInParty()) {
				pc.getParty().updateMiniHP(pc);
			}
			break;
		case COOKING_1_23_N:// 바실스프
		case COOKING_1_23_S:
			cookingType = 52;
			break;
		default:
			break;
		}

		pc.sendPackets(new S_PacketBox(S_PacketBox.ICON_COOKING, pc, cookingType, time));
		pc.getSkillEffectTimerSet().setSkillEffect(cookingId, time * 1000);

		if (cookingId == COOKING_1_7_S || cookingId == COOKING_1_15_S || cookingId == COOKING_1_23_S) {
			if (!pc.getSkillEffectTimerSet().hasSkillEffect(SPECIAL_COOKING2)) {
				pc.getSkillEffectTimerSet().setSkillEffect(SPECIAL_COOKING2, time * 1000);
			}
		} else if (cookingId >= COOKING_1_0_S && cookingId <= COOKING_1_23_S) {
			if (!pc.getSkillEffectTimerSet().hasSkillEffect(SPECIAL_COOKING)) {
				pc.getSkillEffectTimerSet().setSkillEffect(SPECIAL_COOKING, time * 1000);
			}
		}

		if (cookingId == COOKING_1_7_N || cookingId == COOKING_1_7_S || cookingId == COOKING_1_15_N || cookingId == COOKING_1_15_S
				|| cookingId == COOKING_1_23_N || cookingId == COOKING_1_23_S) {
			pc.setDessertId(cookingId);
		} else {
			pc.setCookingId(cookingId);
		}

		if (pc.get_food() < 225) {
			pc.set_food(pc.get_food() + 12);// 요리먹을때 배고픔게이지 올리부분..
			if (pc.get_food() > 225)
				pc.set_food(225);
			pc.sendPackets(new S_PacketBox(S_PacketBox.FOOD, pc.get_food()));
		}

		pc.sendPackets(new S_OwnCharStatus(pc));
	}

}