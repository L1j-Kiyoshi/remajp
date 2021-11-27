package l1j.server.server.serverpackets;

import java.io.IOException;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.QuestInfoTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.templates.L1QuestView;
import l1j.server.server.utils.BinaryOutputStream;

public class S_QuestTalkIsland extends ServerBasePacket {

	private static final String S_Quest = "[S] S_Quest";

	private byte[] _byte = null;

	// 6 접속, 7 업데이트, 9멘트??, d완료후 , 16 창닫기

	public S_QuestTalkIsland(L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x06);
		writeC(0x02);
		byte[] status = null;
		synchronized (pc.syncTalkIsland) {
			for (L1QuestInfo info : pc.quest_list.values()) {
				if (info.end_time != 0) { // 종료된 프로필인경우
					writeC(0x0a);
					status = quset_end(pc, info.quest_id, info);
					writeC(status.length);
					for (byte b : status) {
						writeC(b);
					}
				} else { // 목록에 있는 퀘스트 프로필
					writeC(0x0a);
					status = quset_info(pc, info.quest_id, info);
					writeC(status.length);
					for (byte b : status) {
						writeC(b);
					}
				}
			}
		}
		writeH(0);
	}

	public S_QuestTalkIsland(int type, int value) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		if (type == 14) { // 13 0e 02 08 a8 02 96 61 ??
			writeC(0x02);
			writeC(0x08);
			writeBit(value);
			writeH(0);
		} else {
			writeC(0x02);
			writeC(0x08);
			writeC(0x00);
			writeC(0x10);
			writeBit(value);
			writeH(0);
		}
	}

	public S_QuestTalkIsland(L1PcInstance pc, int questid, L1QuestInfo Q_info) { // 뭔가 목표달성
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x07);
		writeC(0x02);

		byte[] status = null;

		writeC(0x0a);
		status = quset_info(pc, questid, Q_info);
		writeC(status.length);
		for (byte b : status) {
			writeC(b);
		}
		writeH(0);
	}

	/*
	 * 0a 11 //길이값 08 a8 02 //퀘스트번호 10 ac c5 c9 b8 05 //퀘스트 시작시간 18 ac c5 c9 b8
	 * 05 //퀘스트 완료시간 28 01
	 */
	public byte[] quset_end(L1PcInstance pc, int questid, L1QuestInfo Q_info) {
		BinaryOutputStream os = new BinaryOutputStream();

		os.writeC(0x08); // 퀘번호
		os.writeBit(questid);

		os.writeC(0x10); // 시간
		os.writeBit(Q_info.st_time);

		os.writeC(0x18); // 종료
		os.writeBit(Q_info.end_time);

		os.writeC(0x28);
		os.writeC(0x01);

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return os.getBytes();
	}

	public byte[] quest_pass(int qid) {
		BinaryOutputStream os = new BinaryOutputStream();
		long now = System.currentTimeMillis();
		os.writeC(0x08); // 퀘번호
		os.writeBit(qid);

		os.writeC(0x10); // 시간
		os.writeBit(now);

		os.writeC(0x18); // 종료
		os.writeBit(now);

		os.writeC(0x28);
		os.writeC(0x01);

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return os.getBytes();
	}

	public byte[] quset_info(L1PcInstance pc, int questid, L1QuestInfo Q_info) {
		BinaryOutputStream os = new BinaryOutputStream();

		os.writeC(0x08); // 퀘번호
		os.writeBit(questid);

		os.writeC(0x10); // 시간
		os.writeBit(Q_info.st_time);

		L1QuestView view = QuestInfoTable.getInstance().getQuestView(questid);
		if (view == null) {
			os.writeC(0x22);
			os.writeC(0x06);

			os.writeC(0x08);
			os.writeC(0x01);

			os.writeC(0x10); // 현목표
			os.writeC(Q_info.ck[0]);

			os.writeC(0x18); // 최종목표
			os.writeC(1);
		} else {
			for (int i = 0; i < 4; i++) {
				if (view.max_count[i] != 0) {
					os.writeC(0x22);
					os.writeC(0x06);

					os.writeC(0x08);
					os.writeC(i + 1);

					os.writeC(0x10); // 현목표
					os.writeC(Q_info.ck[i]);

					os.writeC(0x18); // 최종목표
					os.writeC(view.max_count[i]);
				}
			}
		}
		os.writeC(0x28);
		os.writeC(0x01);

		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return os.getBytes();
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_Quest;
	}
}
