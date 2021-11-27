package l1j.server.server.clientpackets;

import java.io.IOException;

import l1j.server.Config;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.QuestInfoTable;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.templates.L1QuestView;
import l1j.server.server.utils.CalcExp;
import server.LineageClient;

public class C_TalkIslandQuest extends ClientBasePacket {
	private static final String C_CludiaQuest = "[C] C_CludiaQuest";
	private static final int subQuestIds[] = { 309 };// 미구현 완료시키기.

	public C_TalkIslandQuest(byte[] decrypt, LineageClient client) throws IOException {
		super(decrypt);

		L1PcInstance pc = client.getActiveChar();
		if (pc == null)
			return;
		int type = readH();
		if (type == 0x208 || type == 0x020a || type == 0x020c || type == 0x020f) {
		}
		switch (type) {
		case 0x0208: { // 신규 퀘스트 등록
			readP(3);
			int nextid = readBit();
			//synchronized (pc.syncTalkIsland) {
				L1QuestInfo Q_info = pc.getQuestList(nextid);
				if (Q_info != null)
					return;

				Q_info = new L1QuestInfo();
				Q_info.quest_id = nextid;

				pc.quest_list.put(nextid, Q_info);
				if (nextid == 259) { // 랜턴
					// 생성
					//pc.getInventory().TalkIsland_storeItem(60154, 1);
					pc.getInventory().storeItem(60154, 1);
				}
				
				if (nextid == 260) { // 수련자 변신 주문서
					// 생성
					pc.getInventory().storeItem(40096, 5);
				}
				
				if (nextid == 262) { // 수련자의 갑옷 마법 주문서
					// 생성
					pc.getInventory().storeItem(60718, 1);
				}
				
				if (nextid == 264) { // 수련자의 무기 마법 주문서
					// 생성
					pc.getInventory().storeItem(60717, 1);
				}
				
				if (nextid == 274) { // 용해제
					// 생성
					pc.getInventory().storeItem(20089, 1);
					pc.getInventory().storeItem(41245, 10);
				}
				
				if (nextid == 275) { // 마법인형 소환
					// 생성
					pc.getInventory().storeItem(430506, 1);
					pc.getInventory().storeItem(41246, 50);
				}
				
				if (nextid == 276) { // 응축된 화염의 기운
					// 생성
					pc.getInventory().storeItem(60738, 1);
				}
				
				if (nextid == 282) { // 수련자의 장신구 마법 주문서
					// 생성
					pc.getInventory().storeItem(60731, 1);
					pc.getInventory().storeItem(21100, 1);
				}
				
				/*if (nextid == 361) { // 게렝의 마법 주머니 생성
					pc.getInventory().TalkIsland_storeItem(6012, 1);
				}*/
				pc.sendPackets(new S_QuestTalkIsland(9, nextid));
		//	}
		}
			break;
		case 0x020a: { // 시간 업데이트
			readP(3);
			int startid = readBit();

			L1QuestInfo info = pc.getQuestList(startid);
			boolean isSub = false;
			if (info == null || info.end_time != 0)
				return;

			if (info.st_time == 0)
				info.st_time = System.currentTimeMillis();


			if (startid == 257) {// 레벨퀘스트의 경우 정보 갱신용/ 공격 훈련
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 5) {
					info.ck[0] = 5;
				}
			}
			
			if (startid == 266) {
				info.ck[0] = 1;
			}
			if (startid == 269) {
				info.ck[0] = 1;
			} 
			
			if (startid == 279) {// 레벨퀘스트의 경우 정보 갱신용 35레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 35) {
					info.ck[0] = 35;
				}
			}
			
			if (startid == 286) {
				info.ck[0] = 1;
			} 
			
			if (startid == 290) {
				info.ck[0] = 1;
			} 
			
			if (startid == 292) {
				info.ck[0] = 1;
			} 
			
			if (startid == 303) {
				info.ck[0] = 1;
			} 
			
			/*if (startid == 309) {
				info.ck[0] = 1;
			}*/
			
			if (startid == 308) {
				info.ck[0] = 1;
				info.ck[1] = 1;
				info.ck[2] = 1;
				info.ck[3] = 1;
			} 
			
			if (startid == 283) {// 레벨퀘스트의 경우 정보 갱신용 40레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 40) {
					info.ck[0] = 40;
				}
			}
			
			if (startid == 287) {// 레벨퀘스트의 경우 정보 갱신용 45레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 45) {
					info.ck[0] = 45;
				}
			}
			
			if (startid == 291) {// 레벨퀘스트의 경우 정보 갱신용 48레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 48) {
					info.ck[0] = 48;
				}
			}
			
			if (startid == 294) {// 레벨퀘스트의 경우 정보 갱신용 50레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 50) {
					info.ck[0] = 50;
				}
			}
			
			if (startid == 297) {// 레벨퀘스트의 경우 정보 갱신용 52레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 52) {
					info.ck[0] = 52;
				}
			}
			
			if (startid == 300) {// 레벨퀘스트의 경우 정보 갱신용 54레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 54) {
					info.ck[0] = 54;
				}
			}
			
			if (startid == 302) {// 레벨퀘스트의 경우 정보 갱신용 55레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 55) {
					info.ck[0] = 55;
				}
			}
			
			/*if (startid == 361) {// 레벨퀘스트의 경우 정보 갱신용 70레벨 달성
				info.ck[0] = pc.getLevel();
				if (info.ck[0] > 70) {
					info.ck[0] = 70;
				}
			}*/
			
			for (int isSubQuestId : subQuestIds) {
				if (isSubQuestId == startid) {
					isSub = true;
					info.ck[0] = 1; // 서브 퀘스트라면 바로 완료 셋팅
					break;
				}
			}

			pc.sendPackets(new S_QuestTalkIsland(pc, startid, info));

			if (isSub) {
				// System.out.println("서브퀘스트 완료 : " + startid);
				info.end_time = System.currentTimeMillis();
				pc.sendPackets(new S_QuestTalkIsland(14, startid)); // 바로 끝나는 퀘
			}
		}
			break;
		case 0x020c: { // 퀘스트 보상받기
			try {
				if (Config.STANDBY_SERVER) {
					pc.sendPackets(new S_SystemMessage("\\aH알림: 오픈대기 상태에선 해당 행동이 불가능 합니다 ."), true);
//					pc.sendPackets("정식오픈후 다시 한번 수련자의 속도향상 물약을 클릭하여,퀘스트를 진행하세요");
					return;
				}
				readP(3);
				int questid = readBit();

				L1QuestInfo E_info = pc.getQuestList(questid);
				if (E_info == null || E_info.end_time != 0 || E_info.ck[0] == 0)
					return;

				E_info.end_time = System.currentTimeMillis();

				/*
				 * r 재료가 필요한경우 체크해서 삭제한다.
				 */
				if (questid == 258 // 식량
						|| questid == 263// 뼈조각
						|| questid == 273// 무기 도면
						|| questid == 281// 이상한 뼈조각
						|| questid == 285// 이상한 장신구 조각
						|| questid == 289// 이상한 눈알
						|| questid == 298// 보석 
						|| questid == 304// 룬 만들기 1단계
						|| questid == 305// 룬 만들기 2단계
						|| questid == 306// 룬 만들기 3단계
						|| questid == 307// 룬 만들기 4단계
						|| questid == 308// 룬 만들기 4단계
				) {
					L1QuestView view = QuestInfoTable.getInstance().getQuestView(questid);
					if (view != null) { // 혹시 몰라서 null체크
						for (int i = 0; i < 4; i++) {
							if (view.pick_item[i] != 0) { // 재료아이템
								if (!pc.getInventory().checkItem(view.pick_item[i], view.max_count[i])) {
									pc.sendPackets(new S_SystemMessage("재료가 부족합니다."));
									return;
								}
							}
						}
						// 재료가 존재할경우 삭제
						for (int i = 0; i < 4; i++) {
							if (view.pick_item[i] != 0) {
								//pc.getInventory().consumeItem(view.pick_item[i], view.max_count[i]);
								pc.getInventory().consumeItem(view.pick_item[i]);
							}
						}
					}
				}

				/*
				 * 요리지급 279~286 45달성퀘 320 아나클랜, 322 고대거인, 324 지너스 329 죽은왕 , 341
				 * 뇌룡카슈베, 346 수룡샤크 350 정령왕의대결,
				 */
				int cooktype = 0;
				
				if (questid == 301) {
					L1Teleport.teleport(pc, 32562, 33089,(short) 9, 5, true, true, 5000);
					pc.sendPackets(new S_SystemMessage("\\aH알림: 네르바에게 대화를 걸어 '엘릭서의 룬' 제작 하세요 ."), true);
				}
				
				if (questid == 290) {
					readC();
					cooktype = readC() + 1;
				}
				quest_storeitem(pc, questid, cooktype);
				
				pc.sendPackets(new S_QuestTalkIsland(13, questid));

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			break;
		case 0x020f: {// 퀘스트 이동클릭
			readP(3);
			int telid = readBit();
			L1QuestView view = QuestInfoTable.getInstance().getQuestView(telid);
			if (view != null) {
				if (view.tel[1] != 0) {
					//L1Teleport.teleport(pc, view.tel[1], view.tel[2],(short) view.tel[0], 5, true, true, 5000);
					pc.start_teleport(view.tel[1], view.tel[2], (short) view.tel[0], 5, 169, true, true);
					pc.sendPackets(new S_QuestTalkIsland(16, telid)); // 창닫기

				} else {
					System.out.println("등록되지 않은 퀘스트텔id = " + telid);
				}
			}
		}
			break;
		}

	}

	/**
	 * 퀘스트 아이템 지급
	 */
	private void quest_storeitem(L1PcInstance pc, int questid, int type) {
		try {
			// 퀘리스트에 등록되어있다면
			if (QuestInfoTable.getInstance().getQuestViewid(questid)) {
				L1QuestView view = QuestInfoTable.getInstance().getQuestView(questid);
				if (view == null) {
					return;
				}
				if (view._exp != 0) {
					// System.out.println("view._exp : " + view._exp);
					CalcExp.AddExp(pc, view._exp);
					if (pc.getLevel() < 35 && questid == 278) { // 레벨 35 달성
						pc.setExp(ExpTable.getExpByLevel(35));
					} else if (pc.getLevel() < 40 && questid == 282) { // 레벨 40 달성
						pc.setExp(ExpTable.getExpByLevel(40));
					} else if (pc.getLevel() < 45 && questid == 286) { // 레벨 45 달성
						pc.setExp(ExpTable.getExpByLevel(45));
					} else if (pc.getLevel() < 48 && questid == 290) { // 레벨 48 달성
						pc.setExp(ExpTable.getExpByLevel(48));
					} else if (pc.getLevel() < 50 && questid == 293) { // 레벨 50 달성
						pc.setExp(ExpTable.getExpByLevel(50));
					} else if (pc.getLevel() < 52 && questid == 296) { // 레벨 52 달성
						pc.setExp(ExpTable.getExpByLevel(52));
					} else if (pc.getLevel() < 54 && questid == 299) { // 레벨 54 달성
						pc.setExp(ExpTable.getExpByLevel(54));
					} else if (pc.getLevel() < 55 && questid == 301) { // 레벨 55 달성
						pc.setExp(ExpTable.getExpByLevel(55));
					}
				}
				for (int i = 0; i < 4; i++) {
					if (view.item_id[i] != 0) {
						pc.getInventory().storeItem(view.item_id[i], view.item_count[i]);
					}
				}
				
				if (type != 0) {
					pc.getInventory().storeItem(60725 + type, 1);
				} else if (type != 0 && questid == 303) { // 전사의 환영물약상자
					pc.getInventory().storeItem(50751, 1);
				}
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getType() {
		return C_CludiaQuest;
	}
}