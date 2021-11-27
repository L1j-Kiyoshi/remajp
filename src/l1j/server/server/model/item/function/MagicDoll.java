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

import l1j.server.Config;
import l1j.server.server.clientpackets.ClientBasePacket;
//import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;

@SuppressWarnings("serial")
public class MagicDoll extends L1ItemInstance {

	public MagicDoll(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			useMagicDoll(pc, itemId, this);
		}
	}

	private void useMagicDoll(L1PcInstance pc, int itemId, L1ItemInstance item) {
		if (pc.isInvisble()) {
			return;
		}
		boolean isAppear = true;

		L1DollInstance doll = null;
		for (Object dollObject : pc.getDollList()) {
			doll = (L1DollInstance) dollObject;
			if (doll.getItemObjId() == item.getId()) { // 이미 꺼내고 있는 매직 실업 수당
				isAppear = false;
				break;
			}
		}

		if (isAppear) {

			int npcId = 0;
			int dollType = 0;
			int consumecount = 0;
			int dollTime = 0;

			/*
			 * int castle_id = L1CastleLocation.getCastleIdByArea(pc);//추가 if
			 * (castle_id != 0){ // 인형 공성존에서 사용 불가 if(itemId == 41248 || itemId
			 * == 41249 || itemId == 41250 || itemId == 430000 || itemId ==
			 * 430001 || itemId == 430002 || itemId == 430003 || itemId ==
			 * 430004 || itemId == 430500 || itemId == 430505 || itemId ==
			 * 430506 || itemId == 430501 || itemId == 430502 || itemId ==
			 * 430503 || itemId == 41915 || itemId == 500144 || itemId == 500145
			 * || itemId == 500146 || itemId == 5000034 || itemId == 5000035 ||
			 * itemId == 5000036 || itemId == 430504){ pc.sendPackets(new
			 * S_SystemMessage("\\fY공성지역에서는 사용 할 수 없습니다.")); return; } }
			 */

			switch (itemId) {
			case L1ItemId.DOLL_BUGBEAR:
				npcId = 80106;
				dollType = L1DollInstance.DOLLTYPE_BUGBEAR;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_SUCCUBUS:
				npcId = 80107;
				dollType = L1DollInstance.DOLLTYPE_SUCCUBUS;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_WAREWOLF:
				npcId = 80108;
				dollType = L1DollInstance.DOLLTYPE_WAREWOLF;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_STONEGOLEM:
				npcId = 4500150;
				dollType = L1DollInstance.DOLLTYPE_STONEGOLEM;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_ELDER:
				npcId = 4500151;
				dollType = L1DollInstance.DOLLTYPE_ELDER;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_CRUSTACEA:
				npcId = 4500152;
				dollType = L1DollInstance.DOLLTYPE_CRUSTACEA;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_SEADANCER:
				npcId = 4500153;
				dollType = L1DollInstance.DOLLTYPE_SEADANCER;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_SNOWMAN:
				npcId = 4500154;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_COCATRIS:
				npcId = 4500155;
				dollType = L1DollInstance.DOLLTYPE_COCATRIS;
				consumecount = 50;
				dollTime = 1800;
				break;
			case L1ItemId.DOLL_DRAGON_M:
				npcId = 4500156;
				dollType = L1DollInstance.DOLLTYPE_DRAGON_M;
				consumecount = 50;
				dollTime = 18000;
				break;
			case L1ItemId.DOLL_DRAGON_W:
				npcId = 4500157;
				dollType = L1DollInstance.DOLLTYPE_DRAGON_W;
				consumecount = 50;
				dollTime = 18000;
				break;
			case L1ItemId.DOLL_HIGH_DRAGON_M:
				npcId = 4500158;
				dollType = L1DollInstance.DOLLTYPE_HIGH_DRAGON_M;
				consumecount = 50;
				dollTime = 18000;
				break;
			case L1ItemId.DOLL_HIGH_DRAGON_W:
				npcId = 4500159;
				dollType = L1DollInstance.DOLLTYPE_HIGH_DRAGON_W;
				consumecount = 50;
				dollTime = 18000;
				pc.sendPackets(new S_SystemMessage(
						"MP 회복+4, 일정 확률로 독 공격의 효과 적용"));
				break;
			case L1ItemId.DOLL_LAMIA:
				npcId = 4500160;
				dollType = L1DollInstance.DOLLTYPE_LAMIA;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"MP 회복+4, 일정 확률로 독 공격의 효과 적용"));
				break;
			case L1ItemId.DOLL_SPATOI:
				npcId = 4500161;
				dollType = L1DollInstance.DOLLTYPE_SPATOI;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+2,스턴 내성+10"));
				break;
			case 500202: // 1,800초 동안 근거리 대미지+2,스턴 내성+12,근거리 명중+2
				npcId = 1500202;
				dollType = L1DollInstance.DOLLTYPE_사이클롭스;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+2, 근거리 명중+2,기술 내성+12"));
				break;
			case 500203: // 1,800초 동안 경험치 보너스+10%, 대미지 리덕션+1
				npcId = 1500203;
				dollType = L1DollInstance.DOLLTYPE_자이언트;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"경험치 보너스+10%, 대미지 리덕션+1, 축복 소모 효율+5%"));
				break;
				
			case 600309: // 1,800초 동안 경험치 보너스+10%, 대미지 리덕션+1
				npcId = 101143;
				dollType = L1DollInstance.DOLLTYPE_축자이언트;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"경험치 보너스+10%, 대미지 리덕션+1,AC-2, 축복 소모 효율+5%"));
				break;

			case 500204: //흑장로 1,800초 동안 64초마다 MP 회복 +15, 공격 시 일정 확률로 콜 라이트닝 발동
				npcId = 1500204;
				dollType = L1DollInstance.DOLLTYPE_흑장로;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"MP 절대 회복+15(64초), 공격 시 일정 확률로 콜 라이트닝 발동"));
				break;
			case 600308: //축흑장로 1,800초 동안 64초마다 MP 회복 +15, 공격 시 일정 확률로 콜 라이트닝 발동
				npcId = 101142;
				dollType = L1DollInstance.DOLLTYPE_축흑장로;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"MP 절대 회복+15(64초), 공격 시 일정 확률로 콜 라이트닝 발동, AC-2"));
				break;
			case 500205: // 1,800초 동안 64초마다 MP 15 회복, SP+1
				npcId = 1500205;
				dollType = L1DollInstance.DOLLTYPE_서큐퀸;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"MP 절대 회복+15(64초), SP+1, AC-2"));
				break;
				
			case 600310: // 1,800초 동안 64초마다 MP 15 회복, SP+1
				npcId = 101144;
				dollType = L1DollInstance.DOLLTYPE_축서큐버스퀸;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"64초마다 MP 회복+15, SP+1, AC-2"));
				break;

			case 141919:// 라미아
				npcId = 4500160;
				dollType = L1DollInstance.DOLLTYPE_LAMIA;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"MP 회복+4, 일정 확률로 독 공격의 효과 적용"));
				break;
			case 141920:// 스파토이
				npcId = 4500161;
				dollType = L1DollInstance.DOLLTYPE_SPATOI;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+2, 스턴 내성+10"));
				break;
			case 141922:// 에틴
				npcId = 45000161;
				dollType = L1DollInstance.DOLLTYPE_에틴;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"헤이스트, AC-2, 홀드 내성+10"));
				break;
			case 141921:// 허수아비
				npcId = 41915;
				dollType = L1DollInstance.DOLLTYPE_HUSUABI;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"원거리 명중+2, 근거리 명중+2, HP+50, MP+30"));
				break;
			case 141918:// 시댄서
				npcId = 4500153;
				dollType = L1DollInstance.DOLLTYPE_SEADANCER;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"32초마다 HP 회복 +25"));
				break;

			case 500108:// 인어
				npcId = 1500108;
				dollType = L1DollInstance.DOLLTYPE_인어;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"경험치 보너스 +3%"));
				break;
			case 500109:// 눈사람
				npcId = 1500110;
				dollType = L1DollInstance.DOLLTYPE_눈사람;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+1, 근거리 명중+1"));
				break;

			case 500110:// 킹버그
				npcId = 1500109;
				dollType = L1DollInstance.DOLLTYPE_킹_버그베어;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"기술 내성+8, MP 절대 회복+10(64초)"));
				break;

			case 600234:// 안씀
				npcId = 1600234;
				dollType = L1DollInstance.DOLLTYPE_이벤트인형;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"기술 내성+8, MP 절대 회복+10(64초)"));
				break;
			case 600312:// 축 킹버그
				npcId = 101146;
				dollType = L1DollInstance.DOLLTYPE_축킹버그베어;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"스턴 내성+8, 64초마다 MP 회복+10, AC-2"));
				break;

			case 600241:// 목각
				npcId = 1600241;
				dollType = L1DollInstance.DOLLTYPE_목각;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"최대 HP+50"));
				break;
			case 600242:// 라바
				npcId = 1600242;
				dollType = L1DollInstance.DOLLTYPE_라바골렘;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+1, 대미지 리덕션+1"));
				break;
			case 600243:// 다골
				npcId = 1600243;
				dollType = L1DollInstance.DOLLTYPE_다이아몬드골렘;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"대미지 리덕션+2"));
				break;
			case 600313:// 축다골
				npcId = 101147;
				dollType = L1DollInstance.DOLLTYPE_축다이아몬드골렘;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"대미지 리덕션+2, AC-2"));
				break;
			case 600244:// 시어
				npcId = 1600244;
				dollType = L1DollInstance.DOLLTYPE_시어;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"원거리 대미지+5, HP 절대 회복+30(32초)"));
				break;
			case 600245:// 나발
				npcId = 1600245;
				dollType = L1DollInstance.DOLLTYPE_나이트발드;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+2, 근거리 명중+2, 기술 적중+5"));
				break;
				
				
			case 600314://  리치
				npcId = 101148;
				dollType = L1DollInstance.DOLLTYPE_축리치;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"SP+2, 최대 HP+80,AC-2, PvP 추가 대미지+2"));
				break;
			case 600315:// 축사이 1,800초 동안 근거리 대미지+2,스턴 내성+12,근거리 명중+2
				npcId = 101149;
				dollType = L1DollInstance.DOLLTYPE_축사이클롭스;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+2, 근거리 명중+2,기술 내성+12, AC-2, PvP 추가 대미지+2"));
				break;
			case 600317:// 축시어
				npcId = 101151;
				dollType = L1DollInstance.DOLLTYPE_축시어;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"원거리 대미지+5, HP 절대 회복+30(32초),AC-2, PvP 추가 대미지+2"));
				break;
			case 600316:// 축나발
				npcId = 101150;
				dollType = L1DollInstance.DOLLTYPE_축나이트발드;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+2, 근거리 명중+2, 기술 적중+5,AC-2, PvP 추가 대미지+2"));
				break;
			case 600318:// 축아이리스
				npcId = 101152;
				dollType = L1DollInstance.DOLLTYPE_축아이리스;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"포우 슬레이어 단계별 대미지+10, 대미지 리덕션+3,AC-2, PvP 추가 대미지+2"));
				break;
			case 600319:// 축뱀파
				npcId = 101153;
				dollType = L1DollInstance.DOLLTYPE_축뱀파이어;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+2, 근거리 명중+2,AC-2, PvP 추가 대미지+2, 공포 적중+3, 기술 내성+5"));
				break;
			case 600320:// 축머미
				npcId = 101154;
				dollType = L1DollInstance.DOLLTYPE_축머미로드;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("대미지 감소+2, 경험치 보너스+10%, MP 절대 회복+15(64초), 축복 소모 효율+8%, AC-2, PvP 추가 대미지+2"));
				break;
				
			case 600321:// 축데몬
				npcId = 101155;
				dollType = L1DollInstance.DOLLTYPE_축데몬;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"기술 내성+12, 기술 적중+10,AC-3, PvP 추가 대미지+2, PvP 대미지 감소+4"));
				break;
			case 600322:// 축데스
				npcId = 101156;
				dollType = L1DollInstance.DOLLTYPE_축데스나이트;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"대미지 리덕션+5, 경험치 보너스+20%, 축복 소모 효율+10%, 마법 발동: 헬파이어, AC-3, PvP 추가 대미지+2, PvP 대미지 감소+4"));
				break;
			case 600323:
				npcId = 101157;
				dollType = L1DollInstance.DOLLTYPE_축바란카;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"기술 내성+12, 정령 적중+10, AC-3, PvP 추가 대미지+2, PvP 대미지 감소+4"));
				break;
			case 600324:
				npcId = 101158;
				dollType = L1DollInstance.DOLLTYPE_축타락;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("SP+3, 기술 내성+10, 마법 적중+5, 용언 적중+5, AC-3, PvP 추가 대미지+2, PvP 대미지 감소+4"));
				break;
			case 600325:
				npcId = 101159;
				dollType = L1DollInstance.DOLLTYPE_축바포메트;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("공포 적중+5, 기술 내성+10, AC-3, PvP 추가 대미지+2, PvP 대미지 감소+4"));
				break;	
			case 600326:
				npcId = 101160;
				dollType = L1DollInstance.DOLLTYPE_축얼음여왕;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("원거리 대미지+5, 원거리 명중+5, 기술 내성+10, 정령 적중+5, AC-3, PvP 추가 대미지+2, PvP 대미지 감소+4"));
				break;
			case 600327:
				npcId = 101161;
				dollType = L1DollInstance.DOLLTYPE_축커츠;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("AC-5, 포우 슬레이어 단계별 대미지+10, 대미지 감소+3, 기술 내성+10, 용언 적중+5, PvP 추가 대미지+2, PvP 대미지 감소+4"));
				break;
				
				
				
			case 600246:// 데몬
				npcId = 1600246;
				dollType = L1DollInstance.DOLLTYPE_데몬;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"기술 내성+12, 기술 적중+10"));
				break;
			case 600247:// 데스
				npcId = 1600247;
				dollType = L1DollInstance.DOLLTYPE_데스나이트;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"대미지 리덕션+5, 경험치 보너스+20%, 축복 소모 효율+10%, 마법 발동: 헬파이어"));
				break;
			case 600259:// 안타라스
				npcId = 1600259;
				dollType = L1DollInstance.DOLLTYPE_안타라스;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"대미지 리덕션+6, 경험치 보너스+25%, MP 절대 회복+15(64초),축복 소모 효율+10% ,AC-3, PvP 대미지+4, PvP 대미지 감소+2"));
				break;
			case 600263:// 안타라스
				npcId = 1600259;
				dollType = L1DollInstance.DOLLTYPE_안타라스;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"대미지 리덕션+6, 경험치 보너스+25%, MP 절대 회복+15(64초),축복 소모 효율+10% ,AC-3, PvP 대미지+4, PvP 대미지 감소+2"));
				break;
			case 600260:// 파푸리온
				npcId = 1600260;
				dollType = L1DollInstance.DOLLTYPE_파푸리온;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"SP+4, 마법 적중+8, 기술 적중+3, 정령 적중+3, 용언 적중+3,공포 적중+3,기술 내성+8, 정령 내성+8, 용언 내성+8,공포 내성+8, MP 절대 회복+5(64초), AC-3, PvP 대미지+4, PvP 대미지 감소+2"));
				break;
			case 600264:// 파푸리온
				npcId = 1600260;
				dollType = L1DollInstance.DOLLTYPE_파푸리온;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"SP+4, 마법 적중+8, 기술 적중+3, 정령 적중+3, 용언 적중+3,공포 적중+3,기술 내성+8, 정령 내성+8, 용언 내성+8,공포 내성+8, MP 절대 회복+5(64초), AC-3, PvP 대미지+4, PvP 대미지 감소+2"));
				break;
			case 600261:// 린드비오르
				npcId = 1600261;
				dollType = L1DollInstance.DOLLTYPE_린드비오르;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"원거리 대미지+4, 원거리 명중+8, 기술 적중+3, 정령 적중+3, 용언 적중+3,공포 적중+3,기술 내성+8, 정령 내성+8, 용언 내성+8,공포 내성+8,MP 절대 회복+5(64초), AC-3, PvP 대미지+4, PvP 대미지 감소+2"));
				break;
			case 600265:// 린드비오르
				npcId = 1600261;
				dollType = L1DollInstance.DOLLTYPE_린드비오르;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"원거리 대미지+4, 원거리 명중+8, 기술 적중+3, 정령 적중+3, 용언 적중+3,공포 적중+3,기술 내성+8, 정령 내성+8, 용언 내성+8,공포 내성+8,MP 절대 회복+5(64초), AC-3, PvP 대미지+4, PvP 대미지 감소+2"));
				break;
			case 600262:// 발라카스
				npcId = 1600262;
				dollType = L1DollInstance.DOLLTYPE_발라카스;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+4, 근거리 명중+8, 기술 적중+3, 정령 적중+3, 용언 적중+3,공포 적중+3,기술 내성+8, 정령 내성+8, 용언 내성+8,공포 내성+8, MP 절대 회복+5(64초), AC-3, PvP 대미지+4, PvP 대미지 감소+2"));
				break;
			case 600266:// 발라카스
				npcId = 1600262;
				dollType = L1DollInstance.DOLLTYPE_발라카스;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+4, 근거리 명중+8, 기술 적중+3, 정령 적중+3, 용언 적중+3,공포 적중+3,기술 내성+8, 정령 내성+8, 용언 내성+8,공포 내성+8, MP 절대 회복+5(64초), AC-3, PvP 대미지+4, PvP 대미지 감소+2"));
				break;

			case L1ItemId.DOLL_GremRin:
				npcId = 100882;
				dollType = L1DollInstance.DOLLTYPE_그렘린;
				consumecount = 50;
				dollTime = 1800;
				break;

			case L1ItemId.DOLL_ETIN:
				npcId = 45000161;
				dollType = L1DollInstance.DOLLTYPE_에틴;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"헤이스트, AC-2, 홀드 내성+10"));
				break;

			case L1ItemId.DOLL_RICH:
				npcId = 45000162;
				dollType = L1DollInstance.DOLLTYPE_RICH;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"SP+2, 최대 HP+80"));
				break;

			case L1ItemId.DOLL_PHENIX:
				npcId = 45000163;
				dollType = L1DollInstance.DOLLTYPE_ETIN;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"SP+2, 최대 HP+80"));
				break;

			case 500144: // 눈사람(A)
				npcId = 700196;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN_A;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 500145: // 눈사람(B)
				npcId = 700197;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN_B;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 500146: // 눈사람(C)
				npcId = 700198;
				dollType = L1DollInstance.DOLLTYPE_SNOWMAN_C;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 41915:
				npcId = 41915;
				dollType = L1DollInstance.DOLLTYPE_HUSUABI;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"원거리 명중+2, 근거리 명중+2, HP+50, MP+30"));
				break;

			case 141915://안씀
				npcId = 141915;
				dollType = L1DollInstance.DOLLTYPE_HW_HUSUABI;
				consumecount = 50;
				dollTime = 1800;
				break;

			case 141916://안씀
				npcId = 101033;
				dollType = L1DollInstance.DOLLTYPE_튼튼한기사;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 1419161://안씀
				npcId = 1010331;
				dollType = L1DollInstance.DOLLTYPE_행운의기사;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"\\fS아이템드랍율 2배상승 / 경험치보너스 30% 증가"));
				break;

			case 142920://아이리스
				npcId = 101134;
				dollType = L1DollInstance.DOLLTYPE_아이리스;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"포우 슬레이어 단계별 대미지+10, 대미지 리덕션+3"));
				break;
			case 142921://뱀파
				npcId = 101135;
				dollType = L1DollInstance.DOLLTYPE_뱀파이어;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"근거리 대미지+2, 근거리 명중+2, 공포 적중+3, 기술 내성+5"));
				break;
			case 142922://바란카
				npcId = 101136;
				dollType = L1DollInstance.DOLLTYPE_바란카;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"기술 내성+12, 정령 적중+10"));
				break;
			case 751: //머미로드
				npcId = 101138;
				dollType = L1DollInstance.DOLLTYPE_머미로드;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("대미지 감소+2, 경험치 보너스+10%, MP 절대 회복+15(64초), 축복 소모 효율+8%"));
				break;
			case 752://타락
				npcId = 101137;
				dollType = L1DollInstance.DOLLTYPE_타락;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("SP+3, 기술 내성+10, 마법 적중+5, 용언 적중+5"));
				break;
			case 5000045:// 지배자
				npcId  = 45000364;
				dollType = L1DollInstance.DOLLTYPE_헌신1등급;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 753://바포메트
				npcId = 101139;
				dollType = L1DollInstance.DOLLTYPE_바포메트;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("공포 적중+5, 기술 내성+10"));
				break;	
			case 754://얼음여왕
				npcId = 101140;
				dollType = L1DollInstance.DOLLTYPE_얼음여왕;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("원거리 대미지+5, 원거리 명중+5, 기술 내성+10, 정령 적중+5"));
				break;
			case 755://커츠
				npcId = 101141;
				dollType = L1DollInstance.DOLLTYPE_커츠;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage("AC-2, 포우 슬레이어 단계별 대미지+10, 대미지 감소+3, 기술 내성+10, 용언 적중+5"));
				break;

			case 437018:
				npcId = 4000009;
				dollType = L1DollInstance.DOLLTYPE_HELPER;
				consumecount = 50;
				dollTime = 300;
				break;
			case 60173:
				npcId = 100320;
				dollType = L1DollInstance.DOLLTYPE_블레그;
				consumecount = 10;
				dollTime = 1800;
				break;
			case 60174:
				npcId = 100321;
				dollType = L1DollInstance.DOLLTYPE_레데그;
				consumecount = 10;
				dollTime = 1800;
				break;
			case 60175:
				npcId = 100322;
				dollType = L1DollInstance.DOLLTYPE_엘레그;
				consumecount = 10;
				dollTime = 1800;
				break;
			case 60176:
				npcId = 100323;
				dollType = L1DollInstance.DOLLTYPE_그레그;
				consumecount = 10;
				dollTime = 1800;
				break;
			case 60261:
				npcId = 100431;
				dollType = L1DollInstance.DOLLTYPE_싸이;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 60262:
				npcId = 100432;
				dollType = L1DollInstance.DOLLTYPE_싸이;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 60263:
				npcId = 100433;
				dollType = L1DollInstance.DOLLTYPE_싸이;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 60309:
				npcId = 100579;
				dollType = L1DollInstance.DOLLTYPE_단디;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 60310:
				npcId = 100580;
				dollType = L1DollInstance.DOLLTYPE_쎄리;
				consumecount = 50;
				dollTime = 1800;
				break;
			case 60324://드레이크
				npcId = 100604;
				dollType = L1DollInstance.DOLLTYPE_드레이크;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"원거리 대미지+2, MP 절대 회복+6(64초)"));
				break;
			case 600311:
				npcId = 101145;
				dollType = L1DollInstance.DOLLTYPE_축드레이크킹;
				consumecount = 50;
				dollTime = 1800;
				pc.sendPackets(new S_SystemMessage(
						"원거리 대미지+2, MP 절대 회복+6(64초),AC-2"));
				break;
			case 60447:
				npcId = 100677;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60448:
				npcId = 100678;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60449:
				npcId = 100679;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60450:
				npcId = 100680;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60451:
				npcId = 100681;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60452:
				npcId = 100682;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60453:
				npcId = 100683;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60454:
				npcId = 100684;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60455:
				npcId = 100685;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60456:
				npcId = 100686;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60457:
				npcId = 100687;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60458:
				npcId = 100688;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60459:
				npcId = 100689;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			case 60460:
				npcId = 100690;
				dollType = L1DollInstance.DOLLTYPE_남자_여자;
				consumecount = 500;
				dollTime = 18000;
				break;
			}
			if (itemId >= 60173 && itemId <= 60176) {
				if (!pc.getInventory().checkItem(41159, consumecount)) {
					pc.sendPackets(new S_ServerMessage(337, "$5116"), true);
					return;
				}
			} else {
				if (!pc.getInventory().checkItem(41246, consumecount)) {
					pc.sendPackets(new S_ServerMessage(337, "$5240"), true);
					return;
				}
			}
			if (pc.getDollListSize() >= Config.MAX_DOLL_COUNT) {
				// pc.sendPackets(new S_ServerMessage(319), true);
				// return;

				// 인형 사용
				doll.deleteDoll();
				//pc.sendPackets(new S_SkillIconGFX(56, 0), true);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
				pc.sendPackets(
						new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()),
						true);

				/*L1Npc template = NpcTable.getInstance().getTemplate(npcId);
				doll = new L1DollInstance(template, pc, dollType, itemObjectId,
						dollTime * 1000);
				pc.sendPackets(new S_SkillSound(doll.getId(), 5935), true);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(doll.getId(),
						5935), true);
				pc.sendPackets(new S_SkillIconGFX(56, dollTime), true);
				pc.sendPackets(new S_OwnCharStatus(pc), true);
				pc.sendPackets(
						new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()),
						true);
				pc.sendPackets(new S_ServerMessage(1143), true);
				pc.getInventory().consumeItem(41246, consumecount);
				return;*/
			}
			if (itemId == 437018 && pc.getLevel() > 70) {
				pc.sendPackets(new S_SystemMessage(
						"공주 마법 인형은 Lv70 까지 사용할 수 있습니다."), true);
				return;
			}
			// DollMent(pc,itemId);
			L1Npc template = NpcTable.getInstance().getTemplate(npcId);
			doll = new L1DollInstance(template, pc, dollType, item.getId(),
					dollTime * 1000);
			pc.sendPackets(new S_SkillSound(doll.getId(), 5935), true);
			Broadcaster.broadcastPacket(pc,
					new S_SkillSound(doll.getId(), 5935), true);
						
			//pc.sendPackets(new S_SkillIconGFX(56, dollTime), true);
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SUMMON_PET_NOTI, dollTime, doll, item, true));
					
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(
					new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
			pc.sendPackets(new S_ServerMessage(1143), true);
			if (itemId >= 60173 && itemId <= 60176)
				pc.getInventory().consumeItem(41159, consumecount);
			else
				pc.getInventory().consumeItem(41246, consumecount);
		} else {
			doll.deleteDoll();
			//pc.sendPackets(new S_SkillIconGFX(56, 0), true);
			pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.SUMMON_PET_NOTI, -1, doll, item, false));
			pc.sendPackets(new S_OwnCharStatus(pc), true);
			pc.sendPackets(
					new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
		}
		pc.sendPackets(new S_ItemName(this), true);
	}

	/*
	 * private void DollMent(L1PcInstance pc, int itemObjectId){ switch
	 * (itemObjectId) { case L1ItemId.DOLL_BUGBEAR: pc.sendPackets(new
	 * S_SystemMessage("\\fS마법인형이 무게 게이지를 10% 늘려줍니다."), true); break; case
	 * L1ItemId.DOLL_SUCCUBUS: pc.sendPackets(new
	 * S_SystemMessage("\\fS마법인형에 의해 1분에 MP가 15씩 회복됩니다."), true); break; case
	 * L1ItemId.DOLL_ELDER: pc.sendPackets(new
	 * S_SystemMessage("\\fS마법인형에 의해 확율마법추타 1분에 MP가 18씩 회복됩니다."), true); break;
	 * case L1ItemId.DOLL_WAREWOLF: pc.sendPackets(new
	 * S_SystemMessage("\\fS7%확률로 물리추가타격 +15 효과가 발동됩니다."), true); break; case
	 * L1ItemId.DOLL_CRUSTACEA: pc.sendPackets(new
	 * S_SystemMessage("\\fS7%확률로 물리추가타격 +15 효과가 발동됩니다."), true); break; case
	 * L1ItemId.DOLL_행운의기사: pc.sendPackets(new
	 * S_SystemMessage("\\fS아이템드랍율 2배상승 / 경험치보너스 30% 증가"), true); break; case
	 * L1ItemId.DOLL_STONEGOLEM: pc.sendPackets(new
	 * S_SystemMessage("\\fS10%확률로 15씩 데미지가 경감됩니다."), true); break; case
	 * L1ItemId.DOLL_SEADANCER: pc.sendPackets(new
	 * S_SystemMessage("\\fS1분에 HP가 70씩 회복됩니다."), true); break; case
	 * L1ItemId.DOLL_SNOWMAN: pc.sendPackets(new
	 * S_SystemMessage("\\fSAC -3, 동빙내성 +7 효과가 유지됩니다."), true); break; case
	 * L1ItemId.DOLL_COCATRIS: pc.sendPackets(new
	 * S_SystemMessage("\\fS활 명중 +1, 활 추가타격 +1 효과가 발동됩니다."), true); break; case
	 * L1ItemId.DOLL_DRAGON_M: case L1ItemId.DOLL_DRAGON_W: pc.sendPackets(new
	 * S_SystemMessage("\\fS엠틱 +4 증가 효과가  발동됩니다."), true); break; case
	 * L1ItemId.DOLL_HIGH_DRAGON_M: case L1ItemId.DOLL_HIGH_DRAGON_W:
	 * pc.sendPackets(new S_SystemMessage("\\fS엠틱 4증가 무게 게이지 5%증가 효과가  발동됩니다."),
	 * true); break; case L1ItemId.DOLL_LAMIA: pc.sendPackets(new
	 * S_SystemMessage("\\fS엠틱+4, 독공격 의 효과가  발동됩니다."), true); break; case
	 * L1ItemId.DOLL_SPATOI: pc.sendPackets(new
	 * S_SystemMessage("\\fS근거리 추가타격 +2, 스턴내성 +10효과가  발동됩니다."), true); break;
	 * case L1ItemId.DOLL_ETIN: pc.sendPackets(new
	 * S_SystemMessage("\\fS무한 헤이스트(속도관련 디버프 무시) Ac-2, 홀드내성 +10 효과가  발동됩니다."),
	 * true); break; case 41915: pc.sendPackets(new
	 * S_SystemMessage("\\fS활명중+2 명중+2 HP+50 MP+30 효과가 발동됩니다."), true); break;
	 * case 500144: pc.sendPackets(new
	 * S_SystemMessage("\\fS경험치10%증가, 활명중+5 효과가 발동됩니다."), true); break; case
	 * 500145: pc.sendPackets(new
	 * S_SystemMessage("\\fS경험치10%증가, 1분당 MP 20회복 효과가  발동됩니다."), true); break;
	 * case 500146: pc.sendPackets(new
	 * S_SystemMessage("\\fS경험치10%증가, 32초당 Hp200회복 효과가  발동됩니다."), true); break;
	 * case L1ItemId.DOLL_RICH: pc.sendPackets(new
	 * S_SystemMessage("\\fS추가타격+2, 공격성공+1, 활추타+2, 활명중+1, SP+3"), true);
	 * pc.sendPackets(new
	 * S_SystemMessage("\\fS데미지리덕+3, 스턴내성+10, 홀드내성+10, 마방+10,"), true);
	 * pc.sendPackets(new
	 * S_SystemMessage("\\fSHP+30, MP+40, 엠틱+20 효과가 발동됩니다.")); break; case
	 * L1ItemId.DOLL_PHENIX: pc.sendPackets(new
	 * S_SystemMessage("\\fS스펠파워+3, 엠틱+20, 데미지리덕+3, "), true);
	 * pc.sendPackets(new S_SystemMessage("\\fS스턴내성+10, 홀드내성+10, 마방+10, "),
	 * true); pc.sendPackets(new S_SystemMessage("\\fSHP+30, MP+40 효과가 발동됩니다."),
	 * true); break; } }
	 */

}
