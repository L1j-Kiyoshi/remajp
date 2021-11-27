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

import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import l1j.server.GameSystem.UserRanking.UserRankingController;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class PolyScroll extends L1ItemInstance {

	public PolyScroll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = this.getItemId();
			if (usePolyScroll(pc, itemId, packet.readS())) {
				/** 지배 변신 반지 라면 아이템 삭제 안되도록 하자 */
				//System.out.println("변신 string : "+ packet.readS()); //변신값찾기
				if(itemId != 220281) { 
					pc.getInventory().removeItem(useItem, 1);
				}
			} else {
				pc.sendPackets(new S_ServerMessage(181), true); // \f1 그러한
				// monster에게는
				// 변신할 수 없습니다.
			}
		}
	}

	private boolean usePolyScroll(L1PcInstance pc, int item_id, String s) {
		int time = 0;
		if (item_id == 40088 || item_id == 40096) { // 변신 스크롤, 상아의 탑의 변신 스크롤
			time = polyLawfulTime(pc.getLawful(), 1800, 600);
		} else if (item_id == 140088) { // 축복된 변신 스크롤
			time = polyLawfulTime(pc.getLawful(), 2100, 900);
		} else if (item_id == 550006) { // 복지 변신
			time = polyLawfulTime(pc.getLawful(), 7200, 2400);
		} else if (item_id == 60359) { // 조우의 변신주문서
			time = 1800;
		} else if (item_id == 220281) { // 지배 변신 반지일경우 3600 1시간 
			time = 3600;
		}
		
		/** 변신에 대한 옵션 변경 */
		int PolyMorph = L1PolyMorph.MORPH_BY_ITEMMAGIC;
		/** 변신 상태에서 메이플이 들어간다면 체크해서 삭제 */
		if (s.startsWith("maple")) { 
			s = s.replace("maple ", ""); 
			PolyMorph = L1PolyMorph.MORPH_BY_Domination;
		}
		
		if (s.equalsIgnoreCase("tam 60a") || s.equalsIgnoreCase("tam 60b")
			|| s.equalsIgnoreCase("tam 60c")
			|| s.equalsIgnoreCase("tam 60d")
			|| s.equalsIgnoreCase("tam 60e")
			|| s.equalsIgnoreCase("tam 60f")
			|| s.equalsIgnoreCase("tam 60g")
			|| s.equalsIgnoreCase("tam 60h")) {
			if (item_id != 60359)
				return false;
		}
		//System.out.println("변신 string : "+  s); //변신값찾기
		/** 동일외형 변신일시 맞는 클레스로 변경 */
		if (s.equalsIgnoreCase("basic class polymorph")) {
			if (pc.isCrown()) {
				if (pc.get_sex() == 0)
					s = "basic prince male";
				else
					s = "basic prince female";
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0)
					s = "basic knight male";
				else
					s = "basic knight female";
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0)
					s = "basic elf male";
				else
					s = "basic elf female";
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0)
					s = "basic wizard male";
				else
					s = "basic wizard female";
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0)
					s = "basic darkelf male";
				else
					s = "basic darkelf female";
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0)
					s = "basic dragonknight male";
				else
					s = "basic dragonknight female";
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0)
					s = "basic illusionist male";
				else
					s = "basic illusionist female";
			} else if (pc.isWarrior()) {
				if (pc.get_sex() == 0)
					s = "basic warrior male";
				else
					s = "basic warrior female";
			} else if (pc.isFencer()) {
				if (pc.get_sex() == 0)
					s = "basic fencer male";
				else
					s = "basic fencer female";
			}
		}

		/** 랭킹 변신일시 그 랭킹에 맞는 클레스로 변경 */
		if (s.equalsIgnoreCase("ranking class polymorph")) {
			if (pc.isCrown()) {
				if (pc.get_sex() == 0)
					s = "rangking prince male";
				else
					s = "rangking prince female";
			} else if (pc.isKnight()) {
				if (pc.get_sex() == 0)
					s = "rangking knight male";
				else
					s = "rangking knight female";
			} else if (pc.isElf()) {
				if (pc.get_sex() == 0)
					s = "rangking elf male";
				else
					s = "rangking elf female";
			} else if (pc.isWizard()) {
				if (pc.get_sex() == 0)
					s = "rangking wizard male";
				else
					s = "rangking wizard female";
			} else if (pc.isDarkelf()) {
				if (pc.get_sex() == 0)
					s = "rangking darkelf male";
				else
					s = "rangking darkelf female";
			} else if (pc.isDragonknight()) {
				if (pc.get_sex() == 0)
					s = "rangking dragonknight male";
				else
					s = "rangking dragonknight female";
			} else if (pc.isIllusionist()) {
				if (pc.get_sex() == 0)
					s = "rangking illusionist male";
				else
					s = "rangking illusionist female";
			} else if (pc.isWarrior()) {
				if (pc.get_sex() == 0)
					s = "rangking warrior male";
				else
					s = "rangking warrior female";
			} else if (pc.isFencer()) {
				if (pc.get_sex() == 0)
					s = "rangking fencer male";
				else
					s = "rangking fencer female";
			}
		}

		if (s.startsWith("rangking ")) {
			int star = UserRankingController.getInstance().getStarCount(pc.getName());
			if (star <= 8) {
				if(!UserRankingController.getInstance().getStarCount(pc.getType(), pc.getName())) {
				return false;
		 }
	   }
	 }
		
		/** 변신 시작을 위한 정보 체크 */
		L1PolyMorph poly = PolyTable.getInstance().getTemplate(s);
		if (poly != null || s.equals("")) {
			if (s.equals("")) {
				if (pc.getGfxId().getTempCharGfx() == 6034 || pc.getGfxId().getTempCharGfx() == 6035) {
					return true;
				} else {
					pc.getSkillEffectTimerSet().removeSkillEffect(SHAPE_CHANGE);
					return true;
				}
			// 변신주문서로 11328이상 변신 이외의 변신할경우 버그로 간주
			} else if ((poly.getPolyId() != 6698 && poly.getPolyId() != 6697
				&& poly.getPolyId() != 10870 && poly.getPolyId() != 8719
				&& poly.getPolyId() != 7846 && poly.getPolyId() != 7967)
				&& (poly.getPolyId() < 11328|| poly.getPolyId() == 11487 || poly.getPolyId() == 11498)
				&& (item_id == 40088 || item_id == 40096 || item_id == 140088 || item_id == 550006)) {
				return false;
			} else if (poly.getMinLevel() == 100) {
				return false;
			} else if (poly.getMinLevel() <= pc.getLevel() || pc.isGm()) {
				L1PolyMorph.doPoly(pc, poly.getPolyId(), time, PolyMorph);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
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