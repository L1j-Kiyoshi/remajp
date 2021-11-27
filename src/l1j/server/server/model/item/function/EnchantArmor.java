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

import java.util.Random;

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.EnchantArmorChance;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Armor;
import l1j.server.server.templates.L1EnchantArmorChance;
import l1j.server.server.templates.L1Item;

public class EnchantArmor extends Enchant {
	private static final long serialVersionUID = 1L;
	private static Random _random = new Random(System.nanoTime());

	public EnchantArmor(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = this.getItemId();
			L1ItemInstance l1iteminstance1 = pc.getInventory().getItem(
					packet.readD());

			if (l1iteminstance1 == null) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
			int safe_enchant = 0;
			if (l1iteminstance1.getItem().getType2() != 2) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}

			if (pc.getLastEnchantItemid() == l1iteminstance1.getId()) {
				pc.setLastEnchantItemid(l1iteminstance1.getId(),
						l1iteminstance1);
				return;
			}
			if (l1iteminstance1.getBless() >= 128) { // 봉인템
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}

			safe_enchant = ((L1Armor) l1iteminstance1.getItem())
					.get_safeenchant();

			if (safe_enchant < 0) { // 강화 불가
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}

			int armorId = l1iteminstance1.getItem().getItemId();
			int armortype = l1iteminstance1.getItem().getType();
			/** 환상의 갑옷 마법 주문서 **/

			if (armorId >= 423000 && armorId <= 423008) {
				if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (itemId == L1ItemId.SCROLL_OF_ENCHANT_FANTASY_ARMOR) {
				if (armorId >= 423000 && armorId <= 423008) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			/** 환상의 갑옷 마법 주문서 **/

			/** 아놀드 갑옷 마법 주문서 **/
			if (armorId == 21276) {
				if (itemId == 30147) {
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			if (itemId == 30147) {
				if (armorId == 21276) {
				} else {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			
			/** 기사단의 장신구 마법 주문서**/
			if (armorId >= 20615 && armorId <= 20618){
				if(itemId == 60731){
				} else {
					pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
					return;
				}
			}
			if (itemId == 60731){
				if (armorId >= 20615 && armorId <= 20618){
				} else {
					pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
					return;
				}
			}
			
			
			
			/** 기사단의 갑옷 마법 주문서**/
			if (armorId >= 20619 && armorId <= 20626){
				if(itemId == 60718){
				} else {
					pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
					return;
				}
			}
			if (itemId == 60718){
				if (armorId >= 20619 && armorId <= 20626){
				} else {
					pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지 않았습니다.
					return;
				}
			}

			if (armorId != 21137 && itemId == 60251) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			} else if (armorId == 21137 && itemId != 60251) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}

			if (armorId != 21269 && itemId == 160511) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			} else if (armorId == 21269 && itemId != 160511) {
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}

			if (armorId == 21102 || armorId == 21103 || armorId == 21104
					|| armorId == 21105 || armorId == 21106 || armorId == 21107
					|| armorId == 21108 || armorId == 21109 || armorId == 21110
					|| armorId == 21111 || armorId == 21112 || armorId == 10040 || armorId == 21270) {
				if (itemId == L1ItemId.여행방어주문서 || itemId == 60141) {
					int enchant_level = l1iteminstance1.getEnchantLevel();
					if (enchant_level >= 6) {
						pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
								true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (itemId == L1ItemId.여행방어주문서 || itemId == 60141) {
				if (armorId == 21102 || armorId == 21103 || armorId == 21104
						|| armorId == 21105 || armorId == 21106 || armorId == 21107
						|| armorId == 21108 || armorId == 21109 || armorId == 21110
						|| armorId == 21111 || armorId == 21112 || armorId == 10040 || armorId == 21270) {
					int enchant_level = l1iteminstance1.getEnchantLevel();
					if (enchant_level >= 6) {
						pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
								true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}

			/** 창천의 갑옷 마법 주문서 **/
			if (armorId >= 422000 && armorId <= 422020) {
				if (itemId == L1ItemId.CHANGCHUN_ENCHANT_ARMOR_SCROLL) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId >= 422000 && armorId <= 422020) {
				if (armorId >= 22041 && armorId <= 22061) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			/** 창천의 갑옷 마법 주문서 **/

			if (itemId >= L1ItemId.Inadril_T_ScrollA
					&& itemId <= L1ItemId.Inadril_T_ScrollD) {
				if (!(armorId >= 490000 && armorId <= 490017)) {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId >= 490000 && armorId <= 490017) {
				if (!(itemId >= L1ItemId.Inadril_T_ScrollA && itemId <= L1ItemId.Inadril_T_ScrollD)) {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}

			/** 문장 강화석 **/
			if (itemId == 4900211 || itemId == 60780) {
				if (armortype != 16) {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			} else {
				if (armortype == 16) {
					pc.sendPackets(new S_ServerMessage(79));
					return;
				}
			}
			// 아놀드인첸//
			if (armorId == 120192) {
				if (itemId == L1ItemId.아놀드방어주문서 || itemId == 500102) {
					int enchant_level = l1iteminstance1.getEnchantLevel();
					if (enchant_level >= 10) {
						pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
								true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			/** 장신구 강화 주문서 */
			if (itemId == 160511
					|| itemId == L1ItemId.ORIM_ACCESSORY_ENCHANT_SCROLL
					|| itemId == L1ItemId.ORIM_ACCESSORY_ENCHANT_SCROLL_B
					|| itemId == L1ItemId.ACCESSORY_ENCHANT_SCROLL
					|| itemId == 430040 || itemId == 5000500 || itemId == 60776 || itemId == 60774
					|| itemId == 530040 || itemId == 60417 || itemId == 60731) {
				if (armortype >= 8 && armortype <= 12) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armortype >= 8 && armortype <= 12) {
				if (itemId == 160511
						|| itemId == L1ItemId.ORIM_ACCESSORY_ENCHANT_SCROLL
						|| itemId == L1ItemId.ORIM_ACCESSORY_ENCHANT_SCROLL_B
						|| itemId == L1ItemId.ACCESSORY_ENCHANT_SCROLL
						|| itemId == 430040 || itemId == 5000500 || itemId == 60776 || itemId == 60774
						|| itemId == 530040 || itemId == 60417 || itemId == 60731) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			
			
			/** 장신구 강화 주문서 */
			/** 룸티스의 강화 주문서 **/
			int enchant_level = l1iteminstance1.getEnchantLevel();
			if (itemId == 5000500 || itemId == 60776) {
				if (((armorId >= 500007 && armorId <= 500010)
						|| (armorId >= 502007 && armorId <= 502010)) && itemId == 5000500) {
					if (enchant_level >= Config.룸티스최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW룸티스 귀걸이는 "
								+ Config.룸티스최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else if (((armorId >= 500007 && armorId <= 500010)
						|| (armorId >= 502007 && armorId <= 502010)) && itemId == 60776) {
					if (enchant_level >= Config.보호주문서최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW룸티스 보호 주문서는 "
								+ Config.보호주문서최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}

			}
			if ((armorId >= 500007 && armorId <= 500010)
					|| (armorId >= 502007 && armorId <= 502010)) {
				if (itemId == 5000500) {
					if (enchant_level >= Config.룸티스최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW룸티스 귀걸이는 "
								+ Config.룸티스최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else if (itemId == 60776) {
					if (enchant_level >= Config.보호주문서최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW룸티스 보호 주문서는 "
								+ Config.보호주문서최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}

			}
			
			/** 룸티스의 팬던트강화 주문서 **/
			if (itemId == 60768) {
				if ((armorId >= 30034 && armorId <= 30037)) {
					if (enchant_level >= Config.룸티스최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW룸티스 귀걸이는 "
								+ Config.룸티스최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}

			}
			if ((armorId >= 30034 && armorId <= 30037)) {
				if (itemId == 60768) {
					if (enchant_level >= Config.룸티스최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW룸티스 귀걸이는 "
								+ Config.룸티스최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}

			}
			
			if (itemId == 7323) {
				 if (((armortype >= 8 && armortype <= 12) || armortype == 16 || armortype == 20)){
					 if (enchant_level >= 5) {
					  pc.sendPackets(new S_SystemMessage("\\fW오림의 장신구 강화주문서로는 "
									+ 5 + "이상은 인챈트 할 수 없습니다 축오림을 이용해주세요."), true);
							return;
				    }
		        }
			}
			
			if (((armortype >= 8 && armortype <= 12) || armortype == 16 || armortype == 20)) {
				   if (itemId == 7323) {
					   if (enchant_level >= 5) {
							pc.sendPackets(new S_SystemMessage("\\fW오림의 장신구 강화주문서로는 "
									+ 5 + "이상은 인챈트 할 수 없습니다 축오림을 이용해주세요."), true);
							return;
						}
				   }
			}
			/** 룸티스의 강화 주문서 **/
			/** 스냅퍼의 반지 강화 주문서 */
			if (itemId == 60417 || itemId == 60774) {
				if (armorId >= 21246 && armorId <= 21253 && itemId == 60417) {
					if (enchant_level >= Config.스냅퍼최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW스냅퍼반지는 "
								+ Config.스냅퍼최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else if (armorId >= 21246 && armorId <= 21253 && itemId == 60774) {
					if (enchant_level >= Config.보호주문서최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW스냅퍼의 보호 주문서는 "
								+ Config.보호주문서최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId >= 21246 && armorId <= 21253) {
				if (itemId == 60417) {
					if (enchant_level >= Config.스냅퍼최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW스냅퍼반지는 "
								+ Config.스냅퍼최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else if (itemId == 60774) {
					if (enchant_level >= Config.보호주문서최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW스냅퍼반지의 보호 주문서는 "
								+ Config.보호주문서최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}

			}
			/** 휘장 강화 주문서*/
			if (itemId == 5000552 || itemId == 60778) {
				if (armorId >= 231003 && armorId <= 231009 && itemId == 5000552) {
					if (enchant_level >= Config.스냅퍼최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW휘장은 "
								+ Config.스냅퍼최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else if (armorId >= 231003 && armorId <= 231009 && itemId == 60778) {
					if (enchant_level >= Config.보호주문서최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW휘장 보호 주문서는 "
								+ Config.보호주문서최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId >= 231003 && armorId <= 231009) {
				if (itemId == 5000552) {
					if (enchant_level >= Config.스냅퍼최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW휘장은 "
								+ Config.스냅퍼최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else if (itemId == 60778) {
					if (enchant_level >= Config.보호주문서최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW휘장 보호 주문서는 "
								+ Config.보호주문서최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			
			/** 조이의 유물 강화주문서*/
			if (itemId == 600406) {
				if (armorId >= 421220 && armorId <= 421221) {
					if (enchant_level >= Config.조이의유물) {
						pc.sendPackets(new S_SystemMessage("\\fW조이의유물은 "
								+ Config.조이의유물 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId >= 421220 && armorId <= 421221) {
				if (itemId == 600406) {
					if (enchant_level >= Config.조이의유물) {
						pc.sendPackets(new S_SystemMessage("\\fW조이의유물은 "
								+ Config.조이의유물 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			
			/** 문장 강화삭*/
			if (itemId == 4900211  || itemId == 60780) {
				if (armorId >= 30019 && armorId <= 30025 && itemId == 4900211) {
					if (enchant_level >= Config.스냅퍼최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW문장은 "
								+ Config.스냅퍼최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else if (armorId >= 30019 && armorId <= 30025 && itemId == 60780) {
					if (enchant_level >= Config.보호주문서최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW문장 보호 주문서는 "
								+ Config.보호주문서최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId >= 30019 && armorId <= 30025) {
				if (itemId == 4900211) {
					if (enchant_level >= Config.스냅퍼최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW문장은 "
								+ Config.스냅퍼최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else if (itemId == 60780) {
					if (enchant_level >= Config.보호주문서최대인챈) {
						pc.sendPackets(new S_SystemMessage("\\fW문장 보호 주문서는 "
								+ Config.보호주문서최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
						return;
					}
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}

			/** 신기한 반지 강화 주문서 */
			if (itemId == 530040) {
				if ((armorId >= 525109 && armorId <= 525113)
						|| (armorId >= 625109 && armorId <= 625113)) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if ((armorId >= 525109 && armorId <= 525113)
					|| (armorId >= 625109 && armorId <= 625113)) {
				if (itemId == 530040) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			/** 신기한 반지 강화 주문서 */

			/** 순백의 반지 강화 주문서 */
			if (itemId == 430040) {
				if (armorId >= 425109 && armorId <= 425113) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId >= 425109 && armorId <= 425113) {
				if (itemId == 430040) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			/** 순백의 반지 강화 주문서 */
			
			
			/** 용의 티셔츠 전용 강화 주문서 */
			if (itemId == 430042 ||  itemId == 1430042 || itemId == 2430042) {
				if (enchant_level >= Config.MAX_ARMOR) {
					pc.sendPackets(new S_SystemMessage("방어구는 8까지만 현재 인첸트 됩니다."),
							true);
					return;
				}
				if (armorId == 200852 || armorId == 200851 || armorId == 200853
						|| armorId == 200854 || armorId ==30030 || armorId ==30031 || armorId ==30032 || armorId ==30033) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId == 200852 || armorId == 200851 || armorId == 200853
					|| armorId == 200854 || armorId ==30030 || armorId ==30031 || armorId ==30032 || armorId ==30033) {
				if (enchant_level >= Config.MAX_ARMOR) {
					pc.sendPackets(new S_SystemMessage("방어구는 8까지만 현재 인첸트 됩니다."),
							true);
					return;
				}
				if (itemId == 430042 ||  itemId == 1430042 || itemId == 2430042) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			
			/** 유니콘 전용 강화 주문서 *//*
			if (itemId == 740074 || itemId == 1740074 || itemId == 2740074) {
				if (armorId == 501211 || armorId == 501212 || armorId == 501213) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}
			if (armorId == 501211 || armorId == 501212 || armorId == 501213) {
				if (itemId == 740074 || itemId == 1740074 || itemId == 2740074) {
				} else {
					pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."),
							true);
					return;
				}
			}*/
			
			/** 티셔츠 전용 강화 주문서 */
			if (enchant_level >= Config.장신구최대인챈) {
				if (armortype >= 8 && armortype <= 12) { // 강도 : 특
					pc.sendPackets(new S_SystemMessage("\\fW장신구는 "
							+ Config.장신구최대인챈 + "이상은 인챈트 할 수 없습니다."), true);
					return;
				}
			}

			if (enchant_level >= Config.MAX_ARMOR) {
				pc.sendPackets(new S_SystemMessage("\\fW모든 방어구는 현재 +"
						+ Config.MAX_ARMOR + "이상은 인챈할 수 없습니다."), true);
				return;
			}

			if (Config.GAME_SERVER_TYPE == 1
					&& enchant_level >= safe_enchant + 3) {
				pc.sendPackets(new S_SystemMessage(
						"테스트서버에서는 안전인챈+3 이상은 인챈하실수 없습니다."), true);
				return;
			}

			if (itemId == 500302) { // 고대의 서
				if (l1iteminstance1 != null
						&& l1iteminstance1.getItem().getType2() == 1
						|| l1iteminstance1.getItem().getType2() == 2) {
					if (enchant_level >= Config.MAX_ARMOR) { // 강화불가 수치 알아서...
						pc.sendPackets(new S_SystemMessage("\\fW모든 방어구는 현재 +"
								+ Config.MAX_ARMOR + "이상은 인챈할 수 없습니다."));
						return;
					}
					Random random = new Random();
					int k3 = random.nextInt(100);
					if (k3 <= 5) { // -1 될 확율 4%
						SuccessEnchant(pc, l1iteminstance1, -1);
						pc.sendPackets(new S_SystemMessage(
								"\\fY인첸트에 실패하여 아이템의 인첸수치가 -1 내려갔습니다."));
						pc.getInventory().removeItem(useItem, 1);
					}
					if (k3 >= 6 && k3 <= 30) { // +1 될확율 17%
						SuccessEnchant(pc, l1iteminstance1,
								RandomELevel(l1iteminstance1, itemId));
						pc.sendPackets(new S_SystemMessage(
								"\\fW축하합니다!인첸트에 성공하여 아이템의 인첸수치가 +1 올라갔습니다."));
						pc.getInventory().removeItem(useItem, 1);
					}
					if (k3 >= 31 && k3 <= 100) { // 확률은 알아서
						pc.sendPackets(new S_SystemMessage(
								"\\fW인첸트에 실패하였으나 인첸트 수치가 보존 됩니다."));
						pc.getInventory().removeItem(useItem, 1);
					}

				} else {
					pc.sendPackets(new S_ServerMessage(79)); // \f1 아무것도 일어나지
																// 않았습니다.
				}
			} else if (itemId == 500303) { // 고대의 서 100%
				if (l1iteminstance1 != null
						&& l1iteminstance1.getItem().getType2() == 1
						|| l1iteminstance1.getItem().getType2() == 2) {
					if (enchant_level >= 10) { // 강화불가 수치 알아서...
						pc.sendPackets(new S_SystemMessage(
								"\\fW인첸트 제한으로 더이상 인첸트 할수 없습니다"));
						return;
					}

					SuccessEnchant(pc, l1iteminstance1,
							RandomELevel(l1iteminstance1, itemId));
					pc.sendPackets(new S_SystemMessage(
							"\\fW아이템의 인첸수치가 +1 올라갔습니다."));
					pc.getInventory().removeItem(useItem, 1);

				}
			} else if (itemId == L1ItemId.C_SCROLL_OF_ENCHANT_ARMOR
					|| itemId == 2430041 || itemId == 2430042
					|| itemId == L1ItemId.Inadril_T_ScrollC) { // c-zel
				pc.getInventory().removeItem(useItem, 1);
				int rnd = _random.nextInt(100) + 1;
				if (safe_enchant == 0 && rnd <= 30) {
					FailureEnchant(pc, l1iteminstance1);
					return;
				}
				if (enchant_level < -6) {
					// -6이상은 할 수 없다.
					FailureEnchant(pc, l1iteminstance1);
				} else {
					SuccessEnchant(pc, l1iteminstance1, -1);
				}
			} else if (itemId >= 5000138 && itemId <= 5000141) {
				DragonArmorEnchant(pc, l1iteminstance1, itemId);
				// pc.getInventory().removeItem(useItem, 1);
			} else if (itemId == L1ItemId.Inadril_T_ScrollD) {
				pc.getInventory().removeItem(useItem, 1);
				RegistEnchant(pc, l1iteminstance1, itemId);

			} else if (enchant_level < safe_enchant) {
				pc.getInventory().removeItem(useItem, 1);
				SuccessEnchant(pc, l1iteminstance1,
						RandomELevel(l1iteminstance1, itemId));
			} else {
				pc.getInventory().removeItem(useItem, 1);

				// int rnd = _random.nextInt(10000);
				int rnd = _random.nextInt(100) + 1;
				int rndboji = _random.nextInt(1000) + 1;
				int chance = 0;
				/** 방어구 인첸당  확률 **/
				L1EnchantArmorChance l1enchantchance = EnchantArmorChance.getInstance().getTemplate(l1iteminstance1.getItem().getItemId());
				int e0 = l1enchantchance.get0();
				int e1 = l1enchantchance.get1();
				int e2 = l1enchantchance.get2();
				int e3 = l1enchantchance.get3();
				int e4 = l1enchantchance.get4();
				int e5 = l1enchantchance.get5();
				int e6 = l1enchantchance.get6();
				int e7 = l1enchantchance.get7();
				int e8 = l1enchantchance.get8();
				int e9 = l1enchantchance.get9();
				int e10 = l1enchantchance.get10();
				int e11 = l1enchantchance.get11();
				int e12 = l1enchantchance.get12();
				int e13 = l1enchantchance.get13();
				int e14 = l1enchantchance.get14();
				try{
					if(l1enchantchance != null){
					int enchant = l1iteminstance1.getEnchantLevel();
					switch(enchant){
					case 0 : chance += e0; break;
					case 1 : chance += e1; break;
					case 2 : chance += e2; break;
					case 3 : chance += e3; break;
					case 4 : chance += e4; break;
					case 5 : chance += e5; break;
					case 6 : chance += e6; break;
					case 7 : chance += e7; break;
					case 8 : chance += e8; break;
					case 9 : chance += e9; break;
					case 10 : chance += e10; break;
					case 11 : chance += e11; break;
					case 12 : chance += e12; break;
					case 13 : chance += e13; break;
					case 14 : chance += e14; break;
					default:
					break;
					}
				}
				} catch (Exception e){
					 System.out.println("Character Enchant Armor Chance Load Error");
				}
				if(pc.인첸축복){
					chance = 100;
					pc.인첸축복 = false;
				}
				
				if (itemId == 60774 || itemId == 60776 || itemId == 60778 || itemId == 60780) {
					if(enchant_level == 0) {
						chance = (chance*10) - Config.보호주문서확률0;
					} else if(enchant_level == 1) {
						chance = (chance*10) - Config.보호주문서확률1;
					} else if (enchant_level == 2) {
						chance = (chance*10) - Config.보호주문서확률2;
					} else if (enchant_level == 3) {
						chance = (chance*10) - Config.보호주문서확률3;
					} else if (enchant_level == 4) {
						chance = (chance*10) - Config.보호주문서확률4;
					} else if (enchant_level == 5) {
						chance = (chance*10) - Config.보호주문서확률5;
					} else if (enchant_level == 6) {
						chance = (chance*10) - Config.보호주문서확률6;
					}
				}
				
				if (pc.isGm()) {
					pc.sendPackets(new S_SystemMessage("\\fY성공확률: [ " + chance + " ]"));
					pc.sendPackets(new S_SystemMessage("\\fY찬스: [ " + rnd + " ]"));
				}
				
				if (rnd < chance && !(itemId == 60774 || itemId == 60776 || itemId == 60778 || itemId == 60780)) {
					int randomEnchantLevel = RandomELevel(l1iteminstance1,itemId);
					SuccessEnchant(pc, l1iteminstance1, randomEnchantLevel);
				} else if (rndboji < chance && (itemId == 60774 || itemId == 60776 || itemId == 60778 || itemId == 60780)) {
					int randomEnchantLevel = RandomELevel(l1iteminstance1,itemId);
					SuccessEnchant(pc, l1iteminstance1, randomEnchantLevel);
				} else if (enchant_level >= 9 && rnd < (chance * 2)) {
					String item_name_id = l1iteminstance1.getName();
					String pm = "";
					String msg = "";
					if (enchant_level > 0) {
						pm = "+";
					}
					msg = (new StringBuilder()).append(pm + enchant_level)
							.append(" ").append(item_name_id).toString();
					pc.sendPackets(
							new S_ServerMessage(160, msg, "$252", "$248"), true);
				} else {
					if (itemId == L1ItemId.ORIM_ACCESSORY_ENCHANT_SCROLL) {
						int rnddd = _random.nextInt(100);
						if (rnddd < 55) {
							String item_name_id = l1iteminstance1.getName();
							String pm = "";
							String msg = "";
							if (enchant_level > 0) {
								pm = "+";
							}
							msg = (new StringBuilder())
									.append(pm + enchant_level).append(" ")
									.append(item_name_id).toString();

							pc.sendPackets(new S_ServerMessage(4056, msg));
							return;
						}
					} else if (itemId == L1ItemId.ORIM_ACCESSORY_ENCHANT_SCROLL_B) {
						String item_name_id = l1iteminstance1.getName();
						String pm = "";
						String msg = "";
						if (enchant_level > 0) {
							pm = "+";
						}
						msg = (new StringBuilder()).append(pm + enchant_level)
								.append(" ").append(item_name_id).toString();
						pc.sendPackets(new S_ServerMessage(4056, msg));
						return;
					} else if (itemId == 600406) {
							int rnddd = _random.nextInt(100);
							if (rnddd < 55) {
								String item_name_id = l1iteminstance1.getName();
								String pm = "";
								String msg = "";
								if (enchant_level > 0) {
									pm = "+";
								}
								msg = (new StringBuilder())
										.append(pm + enchant_level).append(" ")
										.append(item_name_id).toString();

								pc.sendPackets(new S_ServerMessage(4056, msg));
								return;
							}
					} else if (itemId == 60776) {
						String item_name_id = l1iteminstance1.getName();
						String pm = "";
						String msg = "";
						if (enchant_level > 0) {
							pm = "+";
						}
						msg = (new StringBuilder()).append(pm + enchant_level)
								.append(" ").append(item_name_id).toString();
						pc.sendPackets(new S_ServerMessage(4056, msg));
						return;
					} else if (itemId == 60774) {
						String item_name_id = l1iteminstance1.getName();
						String pm = "";
						String msg = "";
						if (enchant_level > 0) {
							pm = "+";
						}
						msg = (new StringBuilder()).append(pm + enchant_level)
								.append(" ").append(item_name_id).toString();
						pc.sendPackets(new S_ServerMessage(4056, msg));
						return;
					} else if (itemId == 60778 && enchant_level <= 6) {
						String item_name_id = l1iteminstance1.getName();
						String pm = "";
						String msg = "";
						if (enchant_level > 0) {
							pm = "+";
						}
						msg = (new StringBuilder()).append(pm + enchant_level)
								.append(" ").append(item_name_id).toString();
						pc.sendPackets(new S_ServerMessage(4056, msg));
						return;
					} else if (itemId == 60780) {
						String item_name_id = l1iteminstance1.getName();
						String pm = "";
						String msg = "";
						if (enchant_level > 0) {
							pm = "+";
						}
						msg = (new StringBuilder()).append(pm + enchant_level)
								.append(" ").append(item_name_id).toString();
						pc.sendPackets(new S_ServerMessage(4056, msg));
						return;
					}
					FailureEnchant(pc, l1iteminstance1, itemId);
				}
			}
		}
	}
}
