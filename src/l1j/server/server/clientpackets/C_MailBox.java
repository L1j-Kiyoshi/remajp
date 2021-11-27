package l1j.server.server.clientpackets;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ExcludeLetterTable;
import l1j.server.server.datatables.LetterTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Clan.ClanMember;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_Letter;
import l1j.server.server.serverpackets.S_LetterList;
import l1j.server.server.serverpackets.S_ReadLetter;
import l1j.server.server.serverpackets.S_RenewLetter;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import server.LineageClient;

public class C_MailBox extends ClientBasePacket {
	

	private static final int TYPE_PRIVATE_MAIL = 0; // 개인 편지
	private static final int TYPE_BLOODPLEDGE_MAIL = 1; // 혈맹 편지
	private static final int TYPE_KEPT_MAIL = 2; // 보관 편지

	private static final int READ_PRIVATE_MAIL = 16; // 개인 편지읽기
	private static final int READ_BLOODPLEDGE_MAIL = 17; // 혈맹 편지읽기
	private static final int READ_KEPT_MAIL_ = 18; // 보관함 편지읽기

	private static final int WRITE_PRIVATE_MAIL = 32; // 개인 편지쓰기
	private static final int WRITE_BLOODPLEDGE_MAIL = 33; // 혈맹 편지쓰기

	private static final int DEL_PRIVATE_MAIL = 48; // 개인 편지삭제30:받은편지 보낸편지 삭제시 재갱신 31:혈맹편지함에서 편지 삭제시 40:받은편지 보낸편지 보관함 이동시 32:보관편지함
													// 삭제시
	private static final int DEL_BLOODPLEDGE_MAIL = 49; // 혈맹 편지삭제
	private static final int DEL_KEPT_MAIL = 50; // 보관함 편지삭제

	private static final int TO_KEEP_MAIL = 64; // 편지 보관하기

	private static final int PRICE_PRIVATEMAIL = 50; // 개인 편지 가격
	private static final int PRICE_BLOODPLEDGEMAIL = 1000; // 혈맹 편지 가격

	private static final int SIZE_PRIVATE_MAILBOX = 40; // 개인 편지함 크기
	private static final int SIZE_BLOODPLEDGE_MAILBOX = 80; // 혈맹 편지함 크기
	private static final int SIZE_KEPTMAIL_MAILBOX = 10; // 편지보관함 크기

	private static final int LIST_DEL_PRIVATE_MAIL = 0x60;
	private static final int LIST_DEL_BLOODPLEDGE_MAIL = 0x61;

	private static final String C_MailBox = "[C] C_MailBox";

	public C_MailBox(byte abyte0[], LineageClient client) {
		super(abyte0);
		try {
			int type = readC();

			if (client == null) {
				return;
			}

			L1PcInstance pc = client.getActiveChar();

			if (pc == null) {
				return;
			}
			synchronized (LetterTable.getInstance()) {
				switch (type) {
				case TYPE_PRIVATE_MAIL:
				case TYPE_BLOODPLEDGE_MAIL:
				case TYPE_KEPT_MAIL:
					break;
				case READ_PRIVATE_MAIL:
					ReadLetter(pc, READ_PRIVATE_MAIL, TYPE_PRIVATE_MAIL);
					break;
				case READ_BLOODPLEDGE_MAIL:
					ReadLetter(pc, READ_BLOODPLEDGE_MAIL, TYPE_BLOODPLEDGE_MAIL);
					break;
				case READ_KEPT_MAIL_:
					ReadLetter(pc, READ_KEPT_MAIL_, TYPE_KEPT_MAIL);
					break;
				case WRITE_PRIVATE_MAIL:
					WritePrivateMail(pc);
					break;
				case WRITE_BLOODPLEDGE_MAIL:
					WriteBloodPledgeMail(pc);
					break;
				case DEL_PRIVATE_MAIL:
					DeleteLetter(pc, DEL_PRIVATE_MAIL, TYPE_PRIVATE_MAIL);
					break;
				case DEL_BLOODPLEDGE_MAIL:
					DeleteLetter(pc, DEL_BLOODPLEDGE_MAIL, TYPE_BLOODPLEDGE_MAIL);
					break;
				case DEL_KEPT_MAIL:
					DeleteLetter(pc, DEL_KEPT_MAIL, TYPE_KEPT_MAIL);
					break;
				case TO_KEEP_MAIL:
					SaveLetter(pc, TO_KEEP_MAIL, TYPE_KEPT_MAIL);
					break;
				case LIST_DEL_PRIVATE_MAIL:
					DeletePriListLetter(pc, DEL_PRIVATE_MAIL, TYPE_PRIVATE_MAIL);
					break;
				case LIST_DEL_BLOODPLEDGE_MAIL:
					DeleteBloodListLetter(pc, DEL_BLOODPLEDGE_MAIL, TYPE_PRIVATE_MAIL);
					break;
				default:
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			clear();
		}
	}

	private void DeleteBloodListLetter(L1PcInstance pc, int type, int typePrivateMail) {
		int size = readD();
		while (size-- > 0) {
			int id = readD();
			LetterTable.getInstance().deleteLetter(id);
			S_RenewLetter rl = new S_RenewLetter(pc, type, id);
			pc.sendPackets(rl, true);
		}
	}

	private void DeletePriListLetter2(L1PcInstance pc, int type, int typePrivateMail) {
		int size = readD();
		while (size-- > 0) {
			int id = readD();
			LetterTable.getInstance().deleteLetter(id);
			S_RenewLetter rl = new S_RenewLetter(pc, type, id);
			pc.sendPackets(rl, true);
		}
	}

	private void DeletePriListLetter(L1PcInstance pc, int type, int typePrivateMail) {
		int size = readD();
		while (size-- > 0) {
			int id = readD();
			LetterTable.getInstance().deleteLetter(id);
			S_RenewLetter rl = new S_RenewLetter(pc, type, id);
			pc.sendPackets(rl, true);
		}
	}

	private boolean payMailCost(L1PcInstance RECEIVER, int PRICE) {
		int AdenaCnt = RECEIVER.getInventory().countItems(L1ItemId.ADENA);
		if (AdenaCnt < PRICE) {
			S_ServerMessage sm = new S_ServerMessage(189, "");
			RECEIVER.sendPackets(sm, true);
			return false;
		}

		RECEIVER.getInventory().consumeItem(L1ItemId.ADENA, PRICE);
		return true;
	}

	private void WritePrivateMail(L1PcInstance sender) {
		if (!payMailCost(sender, PRICE_PRIVATEMAIL))
			return;

		int paper = readH(); // 편지지
		SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
		Date date = new Date();
		try {
			String dTime = formatter.format(date);
			String receiverName = readS();
			String subject = readSS();
			String content = readSS();
			// if(!receiverName.equalsIgnoreCase("메티스") &&
			// !receiverName.equalsIgnoreCase("미소피아")){
			// if(!checkCountMail(sender, receiverName, TYPE_PRIVATE_MAIL,
			// SIZE_PRIVATE_MAILBOX)) return;
			// }
			L1PcInstance target = L1World.getInstance().getPlayer(receiverName);
			// 차단되고 있는 경우
			if (target == null) {
				target = CharacterTable.getInstance().restoreCharacter(receiverName);
				if (target != null)
					ExcludeLetterTable.getInstance().load(target);
			}
			if (target != null && target.getExcludingLetterList().contains(sender.getName())) {
				sender.sendPackets(new S_ServerMessage(117, target.getName()));
				return;
			}
			int objid = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), receiverName, TYPE_PRIVATE_MAIL, subject, content);

			int objid2 = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), sender.getName(), TYPE_PRIVATE_MAIL, subject, content);
			target.sendPackets(new S_Letter(sender, 80, 0, objid, subject));
			sender.sendPackets(new S_Letter(sender, 80, 1, objid2, subject));

			S_ServerMessage sm2 = new S_ServerMessage(1239);
			sender.sendPackets(sm2, true);

			/*
			 * if(receiverName.equalsIgnoreCase("메티스") ||
			 * receiverName.equalsIgnoreCase("미소피아")){ sendMessageToReceiver(target, sender,
			 * TYPE_PRIVATE_MAIL, 40); }else{ sendMessageToReceiver(target, sender,
			 * TYPE_PRIVATE_MAIL, SIZE_PRIVATE_MAILBOX); }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			date = null;
			formatter = null;
		}
	}

	private void WriteBloodPledgeMail(L1PcInstance sender) {

		if (!payMailCost(sender, PRICE_BLOODPLEDGEMAIL))
			return;

		int paper = readH(); // 편지지
		SimpleDateFormat formatter = new SimpleDateFormat("yy/MM/dd", Locale.KOREA);
		Date date = new Date();
		String dTime = formatter.format(date);
		String receiverName = readS();
		String subject = readSS();
		String content = readSS();

		L1Clan targetClan = null;
		for (L1Clan clan : L1World.getInstance().getAllClans()) {
			if (clan.getClanName().toLowerCase().equals(receiverName.toLowerCase())) {
				targetClan = clan;
				break;
			}
		}
		String name;
		L1PcInstance target = null;
		ArrayList<ClanMember> clanMemberList = targetClan.getClanMemberList();

		for (int i = 0, a = clanMemberList.size(); i < a; i++) {

			name = clanMemberList.get(i).name;

			target = L1World.getInstance().getPlayer(name);
			// 차단되고 있는 경우
			if (target == null) {
				try {
					target = CharacterTable.getInstance().restoreCharacter(name);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (target != null)
					ExcludeLetterTable.getInstance().load(target);
			}
			if (target != null && target.getExcludingLetterList().contains(sender.getName())) {
				sender.sendPackets(new S_ServerMessage(117, target.getName()));
				continue;
			}
			if (!checkCountMail(sender, name, TYPE_BLOODPLEDGE_MAIL, SIZE_BLOODPLEDGE_MAILBOX))
				continue;
			int objid = LetterTable.getInstance().writeLetter(paper, dTime, sender.getName(), name, TYPE_BLOODPLEDGE_MAIL, subject, content);
			target.sendPackets(new S_Letter(sender, 81, 0, objid, subject));
		}

		S_ServerMessage sm2 = new S_ServerMessage(1239);
		sender.sendPackets(sm2, true);

		date = null;
		formatter = null;
	}

	// 편지를 삭제하기위한 메소드
	private void DeleteLetter(L1PcInstance pc, int type, int letterType) {
		int id = readD();
		LetterTable.getInstance().deleteLetter(id);
		S_RenewLetter rl = new S_RenewLetter(pc, type, id);
		pc.sendPackets(rl, true);
	}

	// 편지를 읽기위한 메소드
	private void ReadLetter(L1PcInstance pc, int type, int read) {
		int id = readD();
		LetterTable.getInstance().CheckLetter(id);
		pc.sendPackets(new S_ReadLetter(pc, type, id, read));
	}

	// 편지리스트 출력을위한 메소드
	private void LetterList(L1PcInstance pc, int type, int count) {
		if (pc != null)
			pc.sendPackets(new S_LetterList(pc, type, count), true);
	}

	// 편지리스트 출력을위한 메소드
	private void Letter(L1PcInstance pc, L1PcInstance target, int type, int iden, int objid, String 제목) {
		if (pc != null)
			pc.sendPackets(new S_Letter(target, type, iden, objid, 제목));
	}

	// 편지를 보관하기 위함 메소드
	private void SaveLetter(L1PcInstance pc, int type, int letterType) {
		int id = readD();
		LetterTable.getInstance().SaveLetter(id, letterType);
		S_RenewLetter rl = new S_RenewLetter(pc, type, id);
		pc.sendPackets(rl, true);
	}

	private boolean checkCountMail(L1PcInstance from, String to, int type, int max) {
		int cntMailInMailBox = LetterTable.getInstance().getLetterCount(to, type);
		if (cntMailInMailBox >= max) { // 편지함 만땅
			S_ServerMessage sm = new S_ServerMessage(1261);
			from.sendPackets(sm, true);
			return false;
		}
		return true;
	}

	private void sendMessageToReceiver(L1PcInstance receiver, L1PcInstance sender, final int type, int objid, String 제목) {
		S_ServerMessage sm2 = new S_ServerMessage(1239);
		sender.sendPackets(sm2, true);
		if (receiver != null && receiver.getOnlineStatus() != 0) {
			// LetterList(receiver,type,MAILBOX_SIZE);
			if (sender != null && sender.getOnlineStatus() != 0) {
				Letter(receiver, sender, type, 0, objid, 제목);
				if (receiver.getId() != sender.getId()) {
					receiver.sendPackets(new S_SkillSound(receiver.getId(), 1091), true);
					receiver.sendPackets(new S_ServerMessage(428), true); // 편지가
																			// 도착했습니다.
				}
			}
			// if(sender != null)
			// sender.sendPackets(new S_LetterList(sender,type,MAILBOX_SIZE),
			// true);
		}
	}

	private void BloodsendMessageToReceiver(L1PcInstance receiver, L1PcInstance sender, final int type, int objid, String 제목) {
		if (receiver != null && receiver.getOnlineStatus() != 0) {
			if (sender != null && sender.getOnlineStatus() != 0) {
				Letter(receiver, sender, type, 0, objid, 제목);
				if (receiver.getId() != sender.getId()) {
					// LetterList(receiver,type,MAILBOX_SIZE);
					receiver.sendPackets(new S_SkillSound(receiver.getId(), 1091), true);
					receiver.sendPackets(new S_ServerMessage(428), true); // 편지가
																			// 도착했습니다.
				}
			}
		}
	}

	@Override
	public String getType() {
		return C_MailBox;
	}
}
