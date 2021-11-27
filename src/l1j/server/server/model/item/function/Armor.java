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
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class Armor extends L1ItemInstance {

	public Armor(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			/*long 현재시간 = System.currentTimeMillis(); //투망ディレイ시간
			long  ディレイ = 현재시간 - pc.get_투망ディレイ(); 

			if (ディレイ < 1000) {  //-- 1초 라는 뜻. 
			//-- 2초로 하고 싶으면 2000 <- 으로 
			return; 
			} else { 
			pc.set_투망ディレイ(현재시간); 	}*/ 
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			if (useItem.getItem().getType2() == 2) { // 종별：방어용 기구
				if (pc.isCrown() && useItem.getItem().isUseRoyal()
						|| pc.isKnight() && useItem.getItem().isUseKnight()
						|| pc.isElf() && useItem.getItem().isUseElf()
						|| pc.isWarrior() && useItem.getItem().isUseWarrior()
						|| pc.isFencer() && useItem.getItem().isUseFencer()
						|| pc.isWizard() && useItem.getItem().isUseMage()
						|| pc.isDarkelf() && useItem.getItem().isUseDarkelf()
						|| pc.isDragonknight()
						&& useItem.getItem().isUseDragonKnight()
						|| pc.isIllusionist()
						&& useItem.getItem().isUseBlackwizard()) {
              
					
					
					/*
					 * int min = ((L1Armor) useItem.getItem()).getMinLevel();
					 * int max = ((L1Armor) useItem.getItem()).getMaxLevel(); if
					 * (min != 0 && min > pc.getLevel()) { // 이 아이템은%0레벨 이상이 되지
					 * 않으면 사용할 수 없습니다. pc.sendPackets(new S_ServerMessage(318,
					 * String.valueOf(min)), true); } else if (max != 0 && max <
					 * pc.getLevel()) { // 이 아이템은%d레벨 이하만 사용할 수 있습니다. //
					 * S_ServerMessage에서는 인수가 표시되지 않는다 if (max < 50) {
					 * pc.sendPackets(new
					 * S_PacketBox(S_PacketBox.MSG_LEVEL_OVER, max), true); }
					 * else { pc.sendPackets(new S_SystemMessage("이 아이템은" + max
					 * + "레벨 이하만 사용할 수 있습니다. "), true); } } else {
					 */
					UseArmor(pc, useItem);
					// }
				} else if (pc.isGm()) {
					UseArmor(pc, useItem);
				} else {
					// \f1당신의 클래스에서는 이 아이템은 사용할 수 없습니다.
					pc.sendPackets(new S_ServerMessage(264), true);
				}
			}
		}
	}

	private static final int[] cashRingList = { 20297, 20301, 20428, 20429,
			20430, 20431, 20432, 20433, 423011, 425100, 425101, 425102, 425103,
			425104, 425105, 425109, 425110, 425111, 425112, 425113, 525109,
			525110, 525111, 525112, 525113, 625109, 625110, 625111, 625112,
			625113, 21113, 21114, 21246, 21247, 21248, 21249, 21250, 21251,
			21252, 21253 };

	public static void UseArmor(L1PcInstance activeChar, L1ItemInstance armor) {
		int type = armor.getItem().getType();
		L1PcInventory pcInventory = activeChar.getInventory();

		/** 2011.05.19 고정수 배틀존 */
		if ((activeChar.getMapId() == 5302 || activeChar.getMapId() == 5153)
				&& !armor.isEquipped()) {
			if ((armor.getItemId() >= 20452 && armor.getItemId() <= 20455)
					|| (armor.getItemId() >= 42401 && armor.getItemId() <= 42421)
					|| (armor.getItemId() >= 421000 && armor.getItemId() <= 421023)) {
				// activeChar.sendPackets(new
				// S_SystemMessage("\\fY배틀존에서는 터번을 사용하실 수 없습니다."), true);
				activeChar.sendPackets(
						new S_ServerMessage(74, armor.getLogName()), true); // \f1%0은
																			// 사용할
																			// 수
																			// 없습니다.
				return;
			} else if (armor.getItemId() == 20077 || armor.getItemId() == 20062
					|| armor.getItemId() == 120077
					|| armor.getItemId() == 20343 || armor.getItemId() == 20344) {
				activeChar.sendPackets(
						new S_ServerMessage(74, armor.getLogName()), true); // \f1%0은
																			// 사용할
																			// 수
																			// 없습니다.
				return;
			}
		}
		if(armor.getItemId() >= 230000 && armor.getItemId() <= 230002 )
		{
			if(activeChar.getShoulder_Slot() == 0)
			{
				activeChar.sendPackets(
						new S_ServerMessage(74, armor.getLogName()), true); // \f1%0은
																			// 사용할
																			// 수
																			// 없습니다.
				return;
			}
		}
		if(armor.getItemId() >= 231003 && armor.getItemId() <= 231006 )
		{
			if(activeChar.getEmblem_Slot() == 0)
			{
				activeChar.sendPackets(
						new S_ServerMessage(74, armor.getLogName()), true); // \f1%0은
																			// 사용할
																			// 수
																			// 없습니다.
				return;
			}
		}
		boolean equipeSpace; // 장비 하는 개소가 비어 있을까
		if (type == 9) { // 링의 경우
			equipeSpace = pcInventory.getTypeEquipped(2, 9) <= (1 + activeChar
					.getRingSlotLevel());
			try {
				if (equipeSpace) {
					boolean cashring = false;
					;
					for (int i : cashRingList) {
						if (i == armor.getItemId()) {
							cashring = true;
							break;
						}
					}
					L1ItemInstance[] ringlist = pcInventory.getRingEquipped();
					if (ringlist != null && ringlist.length > 0) {
						int count = 0;

						for (L1ItemInstance i : ringlist) {
							if (i == null)
								continue;
							if (cashring) {
								for (int a : cashRingList) {
									if (a == i.getItemId()) {
										count++;
										break;
									}
								}
							} else {
								if (i.getItemId() == armor.getItemId())
									count++;
							}
							if (count >= 2) {
								equipeSpace = false;
								break;
							}
						}
						/*
						 * for(L1ItemInstance i : ringlist){ if(i == null)
						 * continue; boolean ck = false; for(int a :
						 * cashRingList){ if(a == i.getItemId()){ ck = true;
						 * break; } } if(ck == cashring) count++; if(count >=
						 * 2){ equipeSpace = false; break; } }
						 */
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (type == 12) { // 귀걸이
			equipeSpace = pcInventory.getTypeEquipped(2, 12) < (1 + activeChar
					.getEarringSlotLevel());
			int armorid = 0;
			int earid = 0;
			try {
				if (equipeSpace) {
					L1ItemInstance[] earringlist = pcInventory
							.getEarringEquipped();
					if (earringlist != null && earringlist.length > 0) {
						for (L1ItemInstance i : earringlist) {
							if (i == null)
								continue;
							armorid = armor.getItemId();
							earid = i.getItemId();
							if (armorid >= 502007 && armorid <= 502010) {
								armorid -= 2000;
							}
							if (earid >= 502007 && earid <= 502010) {
								earid -= 2000;
							}
							if (earid == armorid)
								equipeSpace = false;

							break;
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			equipeSpace = pcInventory.getTypeEquipped(2, type) <= 0;
		}

		if (equipeSpace && !armor.isEquipped()) { // 사용한 방어용 기구를 장비 하고 있지 않아, 그
													// 장비 개소가 비어 있는 경우(장착을 시도한다)
			int polyid = activeChar.getGfxId().getTempCharGfx();

			if (!L1PolyMorph.isEquipableArmor(polyid, type)) { // 그 변신에서는 장비 불가
				activeChar.sendPackets(new S_SystemMessage("현재 변신 상태에서는 "
						+ armor.getLogName() + "을 착용 할 수 없습니다."), true);
				return;
			}
			if (type == 7 && pcInventory.getTypeEquipped(2, 13) >= 1
					|| type == 13 && pcInventory.getTypeEquipped(2, 7) >= 1) {
				activeChar.sendPackets(new S_ServerMessage(124), true); // \f1
																		// 벌써
																		// 무엇인가를
																		// 장비 하고
																		// 있습니다.
				return;
			}

			if (type == 7) {// 방패
				if (activeChar.getWeapon() != null
						&& activeChar.getWeapon().getItem().isTwohandedWeapon()
						&& armor.getItem().getUseType() != 13) { // 양손 무기
					activeChar.sendPackets(new S_ServerMessage(129), true); // \f1양손의
																			// 무기를
																			// 무장한
																			// 채로
																			// 쉴드(shield)를
																			// 착용할
																			// 수
																			// 없습니다.
					return;
				}
				if (activeChar.getWeapon() != null
						&& activeChar.getSecondWeapon() != null) {
					activeChar.sendPackets(new S_ServerMessage(129), true); // \f1양손의
																			// 무기를
																			// 무장한
																			// 채로
																			// 쉴드(shield)를
																			// 착용할
																			// 수
																			// 없습니다.
					return;
				}
			}

			if (type == 13) {// 가더
				if (activeChar.getWeapon() != null
						&& activeChar.getSecondWeapon() != null) {
					activeChar.sendPackets(new S_ServerMessage(129), true); // \f1양손의
																			// 무기를
																			// 무장한
																			// 채로
																			// 쉴드(shield)를
																			// 착용할
																			// 수
																			// 없습니다.
					return;
				}
			}

			/*
			 * if (type == 3 && pcInventory.getTypeEquipped(2, 4) >= 1) { // 셔츠의
			 * 경우, 망토를 입지 않은가 확인 activeChar.sendPackets(new S_ServerMessage(126,
			 * "$224", "$225"), true); // \f1%1상에%0를 입을 수 없습니다. return; } else
			 * if ((type == 3) && pcInventory.getTypeEquipped(2, 2) >= 1) { //
			 * 셔츠의 경우, 메일을 입지 않은가 확인 activeChar.sendPackets(new
			 * S_ServerMessage(126, "$224", "$226"), true); // \f1%1상에%0를 입을 수
			 * 없습니다. return; } else if ((type == 2) &&
			 * pcInventory.getTypeEquipped(2, 4) >= 1) { // 메일의 경우, 망토를 입지 않은가
			 * 확인 activeChar.sendPackets(new S_ServerMessage(126, "$226",
			 * "$225"), true); // \f1%1상에%0를 입을 수 없습니다. return; }
			 */

			activeChar.cancelAbsoluteBarrier(); // 아브소르트바리아의 해제

			pcInventory.setEquipped(armor, true);
		} else if (armor.isEquipped()) { // 사용한 방어용 기구를 장비 하고 있었을 경우(탈착을 시도한다)
			if (armor.getItem().getBless() == 2) { // 저주해지고 있었을 경우
				activeChar.sendPackets(new S_ServerMessage(150), true); // \f1 뗄
																		// 수가
																		// 없습니다.
																		// 저주를
																		// 걸칠 수
																		// 있고 있는
																		// 것
																		// 같습니다.
				return;
			}
			/*
			 * if (type == 3 && pcInventory.getTypeEquipped(2, 2) >= 1) { // 셔츠의
			 * 경우, 메일을 입지 않은가 확인 activeChar.sendPackets(new
			 * S_ServerMessage(127), true); // \f1그것은 벗을 수가 없습니다. return; } else
			 * if ((type == 2 || type == 3) && pcInventory.getTypeEquipped(2, 4)
			 * >= 1) { // 셔츠와 메일의 경우, 망토를 입지 않은가 확인 activeChar.sendPackets(new
			 * S_ServerMessage(127), true); // \f1그것은 벗을 수가 없습니다. return; }
			 */
			/*
			 * if (type == 7) { if
			 * (activeChar.getSkillEffectTimerSet().hasSkillEffect
			 * (L1SkillId.SOLID_CARRIAGE)) {
			 * activeChar.getSkillEffectTimerSet().
			 * removeSkillEffect(L1SkillId.SOLID_CARRIAGE); } }
			 */

			if (type == 7) {// 방패
				if (activeChar.getWeapon() != null
						&& activeChar.getWeapon().getItem().isTwohandedWeapon()
						&& armor.getItem().getUseType() != 13) { // 양손 무기
					activeChar.sendPackets(new S_ServerMessage(129), true); // \f1양손의
																			// 무기를
																			// 무장한
																			// 채로
																			// 쉴드(shield)를
																			// 착용할
																			// 수
																			// 없습니다.
					return;
				}
				if (activeChar.getWeapon() != null
						&& activeChar.getSecondWeapon() != null) {
					activeChar.sendPackets(new S_ServerMessage(129), true); // \f1양손의
																			// 무기를
																			// 무장한
																			// 채로
																			// 쉴드(shield)를
																			// 착용할
																			// 수
																			// 없습니다.
					return;
				}
			}
			if (type == 13) {// 가더
				if (activeChar.getWeapon() != null
						&& activeChar.getSecondWeapon() != null) {
					activeChar.sendPackets(new S_ServerMessage(129), true); // \f1양손의
																			// 무기를
																			// 무장한
																			// 채로
																			// 쉴드(shield)를
																			// 착용할
																			// 수
																			// 없습니다.
					return;
				}
			}
			pcInventory.setEquipped(armor, false);
		} else {
			activeChar.sendPackets(new S_ServerMessage(124), true); // \f1 벌써
																	// 무엇인가를 장비
																	// 하고 있습니다.
		}
		activeChar.setCurrentHp(activeChar.getCurrentHp());
		activeChar.setCurrentMp(activeChar.getCurrentMp());
		activeChar.sendPackets(new S_OwnCharAttrDef(activeChar), true);
		activeChar.sendPackets(new S_OwnCharStatus(activeChar), true);
		activeChar.sendPackets(
				new S_PacketBox(S_PacketBox.char_ER, activeChar.get_PlusEr()),
				true);
		activeChar.sendPackets(new S_SPMR(activeChar), true);
		L1ItemDelay.onItemUse(activeChar, armor); // 아이템 지연 개시
	}
}
