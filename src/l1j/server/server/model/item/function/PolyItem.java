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

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ShowPolyList;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class PolyItem extends L1ItemInstance {

	public PolyItem(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = this.getItemId();
			if (itemId == 41154 // 어둠의 비늘
					|| itemId == 41155 // 열화의 비늘
					|| itemId == 41156 // 배덕자의 비늘
					|| itemId == 41157// 증오의 비늘
					|| itemId == 41143 // 러버 얼간이 변신 일부
					|| itemId == 41144 // 라바본아챠 변신 일부
					|| itemId == 5000046
					|| itemId == 5000047
					|| itemId == 5000048
					|| itemId == 5000049
					|| itemId == 5000050
					|| itemId == 5000051
					|| itemId == 5000052
					|| itemId == 5000053
					|| itemId == 5000054
					|| itemId == 5000055
					|| itemId == 5000056
					|| itemId == 5000057
					|| itemId == 5000058
					|| itemId == 3000163
					|| itemId == 3000164
					|| itemId == 3000165
					|| itemId == 3000166
					|| itemId == 3000167
					|| itemId == 3000168
					|| itemId == 3000169
				    || itemId == 3000170
					|| itemId == 3000171
					|| itemId == 3000172
					|| itemId == 3000173
					|| itemId == 41145 // 라버본나이트
					|| itemId == 60087 || itemId == 60125
					|| itemId == 60168
					|| itemId == 60169 || itemId == 60170
					|| itemId == 60183
					|| itemId == 500003 || itemId == 500004
					|| itemId == 500005
					|| (itemId >= 60192 && itemId <= 60196)
					|| itemId == 60264
					|| itemId == 60265 || itemId == 60266) {
				usePolyItem(pc, itemId);
				pc.getInventory().removeItem(useItem, 1);
			} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV30 // 샤르나의 변신 주문서
																	// (레벨 30)
					|| itemId == L1ItemId.SHARNA_POLYSCROLL_LV40 // 샤르나의 변신 주문서
																	// (레벨 40)
					|| itemId == L1ItemId.SHARNA_POLYSCROLL_LV52 // 샤르나의 변신 주문서
																	// (레벨 52)
					|| itemId == L1ItemId.SHARNA_POLYSCROLL_LV55 // 샤르나의 변신 주문서
																	// (레벨 55)
					|| itemId == L1ItemId.SHARNA_POLYSCROLL_LV60 // 샤르나의 변신 주문서
																	// (레벨 60)
					|| itemId == L1ItemId.SHARNA_POLYSCROLL_LV65 // 샤르나의 변신 주문서
																	// (레벨 65)
					|| itemId == L1ItemId.SHARNA_POLYSCROLL_LV70 // 샤르나의 변신 주문서
																	// (레벨 70)
					|| itemId == L1ItemId.SHARNA_POLYSCROLL_LV75 // 샤르나의 변신 주문서
																	// (레벨 75)
					|| itemId == L1ItemId.SHARNA_POLYSCROLL_LV80) { // 샤르나의 변신
																	// 주문서 (레벨
																	// 80)
				useLevelPolyScroll(pc, itemId);
				pc.getInventory().removeItem(useItem, 1);
			} else if (itemId == L1ItemId.POLYSCROLL_ARC) {
				pc.sendPackets(new S_ShowPolyList(pc.getId(), "archmonlist"),
						true);
				if (!pc.isArchShapeChange()) {
					pc.setArchShapeChange(true);
					pc.setArchPolyType(true);
				}
				pc.getInventory().removeItem(useItem, 1);
			} else if (itemId == L1ItemId.POLYBOOK_ARC) {
				pc.sendPackets(new S_ShowPolyList(pc.getId(), "archmonlist"),
						true);
				if (!pc.isArchShapeChange()) {
					pc.setArchShapeChange(true);
					pc.setArchPolyType(false);
				}
			} else if (itemId == 5000142) {
				usePolyItem(pc, itemId);
			}
		}
	}

	private void usePolyItem(L1PcInstance pc, int itemId) {
		/*
		 * if
		 * (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_EARTH_DRAGON
		 * ) ||
		 * pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_FIRE_DRAGON
		 * ) ||
		 * pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON
		 * )){ pc.sendPackets(new S_ServerMessage(1384), true); return; }
		 */
		int polyId = 0;
		int time = 0;
		if (itemId == 60125) { // 세마 변신 주문서
			polyId = 8168;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 60264) {// 싸이 강남스타일
			polyId = 11232;
			time = polyLawfulTime(pc.getLawful(), 1200, 400);
		} else if (itemId == 60265) {// 싸이 새
			polyId = 11236;
			time = polyLawfulTime(pc.getLawful(), 1200, 400);
		} else if (itemId == 60266) {// 싸이 챔피언
			polyId = 11234;
			time = polyLawfulTime(pc.getLawful(), 1200, 400);
		} else if (itemId == 60192) {// 할로윈딸기캔디 - 뱀파이어
			polyId = 8679;
			time = polyLawfulTime(pc.getLawful(), 900, 300);
		} else if (itemId == 60193) {// 할로윈밀크캔디 - 마녀
			polyId = 8676;
			time = polyLawfulTime(pc.getLawful(), 900, 300);
		} else if (itemId == 60194) {// 할로윈바나나캔디 - 데스
			polyId = 8678;
			time = polyLawfulTime(pc.getLawful(), 900, 300);
		} else if (itemId == 60195) {// 할로윈초콜릿캔디 - 고양이
			polyId = 8677;
			time = polyLawfulTime(pc.getLawful(), 900, 300);
		} else if (itemId == 60196) {// 할로윈호박캔디 - 호박허수아비
			polyId = 5645;
			time = polyLawfulTime(pc.getLawful(), 900, 300);
		} else if (itemId == 60168) {// 바다하피
			polyId = 8126;
			time = polyLawfulTime(pc.getLawful(), 900, 300);
		} else if (itemId == 60169) {// 천상의기사
			polyId = 7968;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 60170) {// 소녀케레니스
			polyId = 7407;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 60183) {// 오렌지
			polyId = 8719;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 60087) { // 올딘 변신 주문서
			polyId = 8134;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 41154) { // 어둠의 비늘
			polyId = 3101;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 41155) { // 열화의 비늘
			polyId = 3126;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 41156) { // 배덕자의 비늘
			polyId = 3888;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 41157) { // 증오의 비늘
			polyId = 3784;
			time = polyLawfulTime(pc.getLawful(), 600, 200);
		} else if (itemId == 41143) {
			polyId = 6086;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 41144) {
			polyId = 6087;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 41145) {
			polyId = 6088;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);

		} else if (itemId == 500003) {// 꼬꼬마파랑
			polyId = 10869;
			time = 2700;
		} else if (itemId == 500004) {// 꼬꼬마노랑
			polyId = 10871;
			time = 2700;
		} else if (itemId == 500005) {// 꼬꼬마분홍
			polyId = 10870;
			time = 2701;

		} else if (itemId == 5000046) {
			polyId = 8774;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000047) {
			polyId = 8812;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000048) {
			polyId = 8817;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000049) {
			polyId = 8842;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000050) {
			polyId = 8900;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000051) {
			polyId = 8843;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000052) {
			polyId = 8851;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000053) {
			polyId = 8844;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000054) {
			polyId = 9003;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000055) {
			polyId = 8845;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000056) {
			polyId = 8913;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000057) {
			polyId = 8846;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 5000058) {
			polyId = 8978;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000164) {
			polyId = 16014;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000165) {
			polyId = 15986;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000166) {
			polyId = 16002;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000167) {
			polyId = 16027;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000168) {
			polyId = 16008;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000169) {
			polyId = 16284;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000170) {
			polyId = 16053;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000171) {
			polyId = 16074;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000172) {
			polyId = 16040;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000173) {
			polyId = 16056;
			time = polyLawfulTime(pc.getLawful(), 1800, 900);
		} else if (itemId == 3000163){
			if(pc.getLevel() <= 44){
			if(pc.isElf()){
				polyId = 12314;
				time = polyLawfulTime(pc.getLawful(), 1800, 900);
			}else if(pc.isDarkelf()){
				polyId = 12280;
				time = polyLawfulTime(pc.getLawful(), 1800, 900);
			}else if(pc.isKnight() || pc.isCrown() || pc.isWarrior()||pc.isDragonknight() || pc.isFencer()){
				polyId = 12283;
				time = polyLawfulTime(pc.getLawful(), 1800, 900);
			}else if(pc.isWizard()||pc.isIllusionist()){
				polyId = 12286;
				time = polyLawfulTime(pc.getLawful(), 1800, 900);
			}
		  }
			if(pc.getLevel() >= 45){
				if(pc.isElf()){
					polyId = 12314;
					time = polyLawfulTime(pc.getLawful(), 1800, 900);
				}else if(pc.isDarkelf()){
					polyId = 12280;
					time = polyLawfulTime(pc.getLawful(), 1800, 900);
				}else if(pc.isKnight() || pc.isCrown() || pc.isWarrior()||pc.isDragonknight() ||pc.isIllusionist() || pc.isFencer()){
					polyId = 12283;
					time = polyLawfulTime(pc.getLawful(), 1800, 900);
				}else if(pc.isWizard()){
					polyId = 12286;
					time = polyLawfulTime(pc.getLawful(), 1800, 900);
				}
			}
		} else if (itemId == 5000142) {
			if (pc.getLevel() >= 85) {
				if (pc.isWarrior() || pc.isKnight() || pc.isCrown()
						|| pc.isDarkelf() || pc.isDragonknight() || pc.isFencer()) {
					polyId = 6224;
				} else if (pc.isElf()) {
					polyId = 7968;
				} else if (pc.isWizard() || pc.isIllusionist()) {
					polyId = 6224;
				}
				time = polyLawfulTime(pc.getLawful(), 1800, 900);
				L1PolyMorph.doPoly(pc, polyId, time, L1PolyMorph.MORPH_BY_GM);
				pc.getInventory().consumeItem(itemId, 1);
			} else {
				pc.sendPackets(new S_SystemMessage("85 레벨이상만 사용 가능합니다."), true);
			}
			return;
		}
		L1PolyMorph.doPoly(pc, polyId, time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}

	private void useLevelPolyScroll(L1PcInstance pc, int itemId) {

		/*
		 * if
		 * (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_EARTH_DRAGON
		 * ) ||
		 * pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_FIRE_DRAGON
		 * ) ||
		 * pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_WATER_DRAGON
		 * )){ pc.sendPackets(new S_ServerMessage(1384), true); return; }
		 */
		int polyId = 0;
		if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV30) { // 30
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6822;
				} else {
					polyId = 6823;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6824;
				} else {
					polyId = 6825;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6826;
				} else {
					polyId = 6827;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6828;
				} else {
					polyId = 6829;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6830;
				} else {
					polyId = 6831;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7139;
				} else {
					polyId = 7140;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7141;
				} else {
					polyId = 7142;
				}
			}
		} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV40) { // 40
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6832;
				} else {
					polyId = 6833;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6834;
				} else {
					polyId = 6835;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6836;
				} else {
					polyId = 6837;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6838;
				} else {
					polyId = 6839;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6840;
				} else {
					polyId = 6841;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7143;
				} else {
					polyId = 7144;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7145;
				} else {
					polyId = 7146;
				}
			}
		} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV52) { // 52
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6842;
				} else {
					polyId = 6843;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6844;
				} else {
					polyId = 6845;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6846;
				} else {
					polyId = 6847;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6848;
				} else {
					polyId = 6849;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6850;
				} else {
					polyId = 6851;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7147;
				} else {
					polyId = 7148;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7149;
				} else {
					polyId = 7150;
				}
			}
		} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV55) { // 55
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6852;
				} else {
					polyId = 6853;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6854;
				} else {
					polyId = 6855;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6856;
				} else {
					polyId = 6857;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6858;
				} else {
					polyId = 6859;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6860;
				} else {
					polyId = 6861;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7151;
				} else {
					polyId = 7152;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7153;
				} else {
					polyId = 7154;
				}
			}
		} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV60) { // 60
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6862;
				} else {
					polyId = 6863;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6864;
				} else {
					polyId = 6865;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6866;
				} else {
					polyId = 6867;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6868;
				} else {
					polyId = 6869;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6870;
				} else {
					polyId = 6871;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7155;
				} else {
					polyId = 7156;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7157;
				} else {
					polyId = 7158;
				}
			}
		} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV65) { // 65
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6872;
				} else {
					polyId = 6873;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6874;
				} else {
					polyId = 6875;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6876;
				} else {
					polyId = 6877;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6878;
				} else {
					polyId = 6879;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6880;
				} else {
					polyId = 6881;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7159;
				} else {
					polyId = 7160;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7161;
				} else {
					polyId = 7162;
				}
			}
		} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV70) { // 70
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6882;
				} else {
					polyId = 6883;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6884;
				} else {
					polyId = 6885;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6886;
				} else {
					polyId = 6887;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6888;
				} else {
					polyId = 6889;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6890;
				} else {
					polyId = 6891;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7163;
				} else {
					polyId = 7164;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7165;
				} else {
					polyId = 7166;
				}
			}
		} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV75) { // 75
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6882;
				} else {
					polyId = 6883;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6884;
				} else {
					polyId = 6885;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6886;
				} else {
					polyId = 6887;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6888;
				} else {
					polyId = 6889;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6890;
				} else {
					polyId = 6891;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7163;
				} else {
					polyId = 7164;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7165;
				} else {
					polyId = 7166;
				}
			}
		} else if (itemId == L1ItemId.SHARNA_POLYSCROLL_LV80) { // 80
			if (pc.isCrown()) {
				if (pc.get_sex() == 0) {
					polyId = 6882;
				} else {
					polyId = 6883;
				}
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0) {
					polyId = 6884;
				} else {
					polyId = 6885;
				}
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0) {
					polyId = 6886;
				} else {
					polyId = 6887;
				}
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0) {
					polyId = 6888;
				} else {
					polyId = 6889;
				}
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0) {
					polyId = 6890;
				} else {
					polyId = 6891;
				}
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0) {
					polyId = 7163;
				} else {
					polyId = 7164;
				}
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0) {
					polyId = 7165;
				} else {
					polyId = 7166;
				}
			}
		}
		int time = polyLawfulTime(pc.getLawful(), 1800, 600);
		L1PolyMorph.doPoly(pc, polyId, time, L1PolyMorph.MORPH_BY_ITEMMAGIC);
	}

	private int polyLawfulTime(int lawful, int max, int min) {
		if (lawful >= 32767)
			return max;
		else if (lawful <= -32768)
			return min;
		double d = 65535 / (max - min);
		int lawfulex = lawful + 32768;
		if (lawfulex > 65535)
			lawfulex = 65535;
		int time = (int) (lawfulex / d + min);
		if (time > max)
			time = max;
		else if (time < min)
			time = min;
		return time;
	}
}
