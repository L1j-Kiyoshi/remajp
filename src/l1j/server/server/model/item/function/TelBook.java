package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class TelBook extends L1ItemInstance {
	public TelBook(L1Item item) {
		super(item);
	}
	

	private static final int[][] TownAddBook = { { 34060, 32281, 4 }, // 오렌
			{ 33079, 33390, 4 }, // 은기사
			{ 32750, 32439, 4 }, // 오크숲
			{ 32612, 33188, 4 }, // 윈다우드
			{ 33720, 32492, 4 }, // 웰던
			{ 32872, 32912, 304 }, // 침묵의 동굴
			{ 32612, 32781, 4 }, // 글루디오
			{ 33067, 32803, 4 }, // 켄트
			{ 33933, 33358, 4 }, // 아덴
			{ 33601, 33232, 4 }, // 하이네
			{ 32574, 32942, 0 }, // 말하는 섬
			{ 33430, 32815, 4 } }; // 기란

	private static final int[][] DungeonAddBook = { { 32797, 32799, 101 }, // 오만1
			// { 32809, 32723, 7 }, // 본던1
			{ 32764, 32842, 77 }, // 오렌
			{ 32745, 32852, 59 }, // 에바왕국1층
			{ 32776, 32732, 45 }, // 개미굴
			{ 32681, 32877, 450 }, // 고대 거인의 무덤

			// { 32577, 32674, 400 }, // 대공동 저항군
			// { 32920, 32798, 430 }, // 정무
			{ 32929, 32994, 410 }, // 마족
			{ 34266, 32190, 4 }, // 그신
			{ 32758, 33459, 4 }, // 욕망
			{ 32680, 32803, 450 }, // 라던중앙광장
			{ 32843, 32694, 550 } }; // 선박의 무덤 수면
	/*
	 * { 32743, 32833, 72 }, // 수정동굴 { 33444, 32828, 4 }, // 용던1 { 32763, 32843,
	 * 77 }, // 오렌3 { 32711, 32793, 59 }, // 에바1 { 32637, 33327, 4 }, // 개미굴 {
	 * 32538, 32803, 400 }, // 대공동 저항군 { 32920, 32800, 430 }, // 정무 { 32927,
	 * 32995, 410 }, // 마족 { 34267, 32189, 4 }, // 그신 { 32760, 33461, 4 }}; //
	 * 욕망
	 */

	private static final int[][] OmanTowerAddBook = { { 32725, 32794, 101 }, // 오만1
			{ 32730, 32802, 102 }, // 오만2
			{ 32726, 32802, 103 }, // 오만3
			{ 32620, 32858, 104 }, // 오만4
			{ 32602, 32866, 105 }, // 오만5
			{ 32611, 32862, 106 }, // 오만6
			{ 32618, 32865, 107 }, // 오만7
			{ 32602, 32866, 108 }, // 오만8
			{ 32613, 32866, 109 }, // 오만9
			{ 32730, 32802, 110 }, // 오만10
			{ 32638, 32805, 111 }, // 오만 정상1
			{ 32799, 32963, 111 } }; // 오만 정상2
	
	private static final int[][] RuleTowerAddBook = {{32728,32795,12852},
			{32721,32797,12853},
			{32724,32803,12854},
			{32596,32860,12855},
			{32596,32860,12856},
			{32596,32860,12857},
			{32596,32860,12858},
			{32596,32860,12859},
			{32596,32860,12860},
			{32730,32802,12861},}; // 오만 정상2

	private static final int[][] OmanTower2AddBook = { { 32725, 32794, 101 }, // 오만1
			{ 32730, 32802, 102 }, // 오만2
			{ 32726, 32802, 103 }, // 오만3
			{ 32620, 32858, 104 }, // 오만4
			{ 32602, 32866, 105 }, // 오만5
			{ 32611, 32862, 106 }, // 오만6
			{ 32618, 32865, 107 }, // 오만7
			{ 32602, 32866, 108 }, // 오만8
			{ 32613, 32866, 109 }, // 오만9
			{ 32730, 32802, 110 }, // 오만10
			{ 32638, 32805, 111 }, // 오만 정상1
			{ 32799, 32963, 111 } }; // 오만 정상2

			private static final int[][] JouAddBook = { 
			          { 34266, 32190, 4 }, // 그신입구
			          { 32507, 32924, 9 }, // 말섬 북쪽 섬
			   { 32491, 32855, 9 }, // 말섬 던전 입구
			   { 32409, 32938, 9 }, // 말섬 오크 망루 지대
			   { 32706, 33150, 9 }, // 말섬 흑기사 전초기지
			   { 32883, 32647, 4 }, // 본토 죽음의 폐허
			   { 32875, 32927, 4 }, // 본토 망자의 무덤
			   { 32811, 32726, 807 }, // 글루디오 던전 1층
			   { 32599, 32289, 4 }, // 본토 오크 부락
			   { 32908, 33222, 4 }, // 본토 사막(에르자베)
			   { 32716, 33136, 4 }, // 본토 사막(샌드웜)
			   { 32806, 32726, 19 }, // 요정 숲 던전 1층
			   { 33753, 32794, 4 }, //밀림 지대
			   { 33753, 32794, 4 }, //밀림 지대
			   { 32811, 32909, 4 }, // 본토 흑기사 출몰 지역
			   { 32766, 32798, 20 }, // 요정 숲 던전 2층
			   { 33764, 33314, 4 }, // 본토 거울의 숲
			   { 33182, 33006, 4 }, // 본토 암흑용의 상흔
			   { 34125, 32799, 4 }, // 본토 풍룡의 둥지
			   { 34126, 32192, 4 }, // 본토 오렌 설벽
			   { 32763, 32843, 77 }, // 상아탑 4층 입구
			   { 32809, 32810, 30 }, // 용의 던전 1층
			   { 32706, 32820, 32 }, // 용의 던전 3층
			   { 33429, 32826, 4 }, // 기란 감옥 입구
			   { 33429, 32826, 4 }, // 기란 감옥 입구
			   { 32744, 32799, 35 }, // 용의 던전 5층
			   { 32764, 32776, 810 }, // 글루디오던전 4층
			   { 33329, 32460, 4 }, // 용의계곡입구
			   { 34051, 32561, 4 }, // 본토 엘모어 격전지
			   { 32804, 32726, 812 }, // 글루디오 던전 6층
			   { 33643, 32417, 4 }, // 본토 화룡의 둥지 입구
			   { 32804, 32266, 4 }, // 아투바 오크 은신처
	}; // 시장

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			L1ItemInstance useItem = pc.getInventory().getItem(this.getId());
			int itemId = useItem.getItemId();
			if (pc.get_DuelLine() != 0) {
				pc.sendPackets(new S_SystemMessage("배틀존에서 사용할 수 없습니다."));
				return;
			}
			
			if (pc.getMapId() == 99 || pc.getMapId() == 6202 || pc.getMapId() == 1700  || pc.getMapId() == 1708  || pc.getMapId() == 1710 || pc.getMapId() == 1703) {
				pc.sendPackets(new S_SystemMessage(
						"주위의 마력에의해 순간이동을 사용할 수 없습니다."), true);
				pc.sendPackets(new S_Paralysis(
						S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
				return;
			}
			int BookTel = packet.readC();
			switch (itemId) {
			case 560025:
			case 560026:

				int[] TownAddBookList = TownAddBook[BookTel];
				if (TownAddBookList != null) {
					S_SkillSound packe = null;
					packe = new S_SkillSound(pc.getId(), 169);
					pc.sendPackets(packe);
					Broadcaster.broadcastPacket(pc, packe, true);

					L1Teleport.teleport(pc, TownAddBookList[0],
							TownAddBookList[1], (short) TownAddBookList[2], 3,
							false, 0);
					pc.getInventory().removeItem(useItem, 1);
				}
				break;
			case 560027:
				int[] DungeonAddBookList = DungeonAddBook[BookTel];
				/*
				 * if(BookTel == 2){ if(pc.isKnight()){ DungeonAddBookList[0] =
				 * 32737; DungeonAddBookList[1] = 32811; DungeonAddBookList[2] =
				 * 276; }else if(pc.isCrown()){ DungeonAddBookList[0] = 32734;
				 * DungeonAddBookList[1] = 32852; DungeonAddBookList[2] = 277;
				 * }else if(pc.isElf()){ DungeonAddBookList[0] = 32735;
				 * DungeonAddBookList[1] = 32867; DungeonAddBookList[2] = 275;
				 * }else if(pc.isDarkelf()){ DungeonAddBookList[0] = 32736;
				 * DungeonAddBookList[1] = 32809; DungeonAddBookList[2] = 273;
				 * }else if(pc.isDragonknight()){ DungeonAddBookList[0] = 32734;
				 * DungeonAddBookList[1] = 32852; DungeonAddBookList[2] = 274;
				 * }else if(pc.isIllusionist()){ DungeonAddBookList[0] = 32809;
				 * DungeonAddBookList[1] = 32830; DungeonAddBookList[2] = 272;
				 * }else if(pc.isWizard()){ DungeonAddBookList[0] = 32739;
				 * DungeonAddBookList[1] = 32856; DungeonAddBookList[2] = 271; }
				 * }
				 */
				if (DungeonAddBookList != null) {
					S_SkillSound packe = null;
					packe = new S_SkillSound(pc.getId(), 169);
					pc.sendPackets(packe);
					Broadcaster.broadcastPacket(pc, packe, true);

					L1Teleport.teleport(pc, DungeonAddBookList[0],
							DungeonAddBookList[1],
							(short) DungeonAddBookList[2], 3, false, 0);
					pc.getInventory().removeItem(useItem, 1);
				}
				break;
			case 560028:

				int[] OmanTowerAddBookList = OmanTowerAddBook[BookTel];
				if (OmanTowerAddBookList != null) {
					S_SkillSound packe = null;
					packe = new S_SkillSound(pc.getId(), 169);
					pc.sendPackets(packe);
					Broadcaster.broadcastPacket(pc, packe, true);

					L1Teleport.teleport(pc, OmanTowerAddBookList[0],
							OmanTowerAddBookList[1],
							(short) OmanTowerAddBookList[2], 3, false, 0);
					pc.getInventory().removeItem(useItem, 1);
				}
				break;
			case 60203:
				int[] OmanAddBookList = OmanTower2AddBook[BookTel];
				if (OmanAddBookList != null) {
					S_SkillSound packe = null;
					packe = new S_SkillSound(pc.getId(), 169);
					pc.sendPackets(packe);
					Broadcaster.broadcastPacket(pc, packe, true);

					L1Teleport.teleport(pc, OmanAddBookList[0],
							OmanAddBookList[1], (short) OmanAddBookList[2], 3,
							false, 0);
				}
				break;
			case 600306:
				int[] OmanAddBookList1 = RuleTowerAddBook[BookTel];
				if (OmanAddBookList1 != null) {
					S_SkillSound packe = null;
					packe = new S_SkillSound(pc.getId(), 169);
					pc.sendPackets(packe);
					Broadcaster.broadcastPacket(pc, packe, true);

					L1Teleport.teleport(pc, OmanAddBookList1[0],
							OmanAddBookList1[1], (short) OmanAddBookList1[2], 3,
							false, 0);
				}
				break;
			case 60360:// 조우의 이동 기억책
				int[] jouAddBookList = JouAddBook[BookTel];
				if (jouAddBookList != null) {
					S_SkillSound packe = new S_SkillSound(pc.getId(), 169);
					pc.sendPackets(packe);
					Broadcaster.broadcastPacket(pc, packe, true);

					L1Teleport.teleport(pc, jouAddBookList[0],
							jouAddBookList[1], (short) jouAddBookList[2], 3,
							false, 0);
					pc.getInventory().removeItem(useItem, 1);
				}
				break;
			}
		}
	}
}
