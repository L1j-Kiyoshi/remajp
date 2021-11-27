package server;

import java.sql.Timestamp;
import java.util.Random;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_ClanJoinLeaveStatus;
import l1j.server.server.serverpackets.S_ClanWindow;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_문장주시;

public class WebClient {
	private static String TAG = "WebClient";

	private int _off = 1;
	private int _off2 = 2;

	public String printData(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(fillHex(i, 4) + ": ");
			}
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char) t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}

			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char) t1);
				} else {
					result.append('.');
				}
			}

			result.append("\n");
		}
		return result.toString();
	}

	private String fillHex(int data, int digits) {
		String number = Integer.toHexString(data);

		for (int i = number.length(); i < digits; i++) {
			number = "0" + number;
		}
		return number;
	}

	public WebClient(byte[] abyte0) {
		int op = 0;
		op = abyte0[0] & 0xff;
		String name1;
		String name2;
		String c_pcname;
		String c_clanname;

		try {
			switch (op) {
			/*
			 * 시세 창에서 버튼 클릭시 1byte(opcode)0x00 유저 시세검색후 텔타기위해 버튼 클릭 (data1) 클릭한 유저이름(string)
			 * 끝에 0x00붙임 (data2) 클릭한 버튼의 판매자 이름(string) 끝에 0x00붙임
			 */
			case 상인찾기버튼:
				name1 = readS(abyte0);
				name2 = readS(abyte0);

				// System.out.println("[패킷1]\r\n"+printData(name1.getBytes(),
				// name1.getBytes().length));
				// System.out.println("[패킷2]\r\n"+printData(name2.getBytes(),
				// name2.getBytes().length));
				// System.out.println(name1.getBytes() + " 123 :"+name2);
				L1PcInstance pc = L1World.getInstance().getPlayer(name1);
				L1PcInstance targetpc = L1World.getInstance().getPlayer(name2);
				L1NpcShopInstance targetNpc = L1World.getInstance().getNpcShop(name2);
				if (pc == null)
					return;
				if (pc.isPrivateShop() || pc.isTeleport())
					return;
				if (pc.getMapId() != 800)
					return;
				if (targetpc == null && targetNpc == null) {
					pc.sendPackets(new S_SystemMessage("개인상점을 찾을수 없습니다. 잠시후 다시 이용 바랍니다."));
					return;
				}
				Random rnd = new Random(System.nanoTime());
				if (targetpc != null) {
					if (targetpc.getMapId() != 800)
						return;
					pc.dx = targetpc.getX() + rnd.nextInt(3) - 1;
					pc.dy = targetpc.getY() + rnd.nextInt(3) - 1;
					pc.dm = (short) targetpc.getMapId();
					pc.dh = calcheading(pc.dx, pc.dy, targetpc.getX(), targetpc.getY());
					pc.상인찾기Objid = targetpc.getId();
					pc.setTelType(7);
					pc.sendPackets(new S_SabuTell(pc), true);
				} else if (targetNpc != null) {
					pc.dx = targetNpc.getX() + rnd.nextInt(3) - 1;
					pc.dy = targetNpc.getY() + rnd.nextInt(3) - 1;
					pc.dm = (short) targetNpc.getMapId();
					pc.dh = calcheading(pc.dx, pc.dy, targetNpc.getX(), targetNpc.getY());
					pc.상인찾기Objid = targetNpc.getId();
					pc.setTelType(7);
					pc.sendPackets(new S_SabuTell(pc), true);
				}
				break;
			case 혈맹가입신청버튼:
				int type = 0;
				type = abyte0[1] & 0xff;
				c_pcname = readSS(abyte0);
				c_clanname = readSS(abyte0);
				// System.out.println("[패킷1]\r\n"+printData(c_pcname.getBytes(),
				// c_pcname.getBytes().length));
				// System.out.println("[패킷2]\r\n"+printData(c_clanname.getBytes(),
				// c_clanname.getBytes().length));
				// System.out.println("1 : "+c_pcname + " 2 :"+c_clanname);
				L1PcInstance _pc = L1World.getInstance().getPlayer(c_pcname);
				if (_pc == null)
					return;// 123
				if (_pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.가입신청ディレイ)) {
					int time = _pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.가입신청ディレイ);
					_pc.sendPackets(new S_SystemMessage("(" + time + ") 초후 다시 클릭해주세요."));
					return;
				}

				L1Clan clan = L1World.getInstance().getClan(c_clanname);

				/*
				 * if (clan == null){ _pc.sendPackets(new
				 * S_SystemMessage(c_clanname+" 혈맹을 찾을 수 없습니다.")); return; }
				 */
				if (clan == null) {
					_pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 4), true);
					return;
				}

				L1PcInstance _target = clan.getonline간부();
				/*
				 * if(_target == null){ _pc.sendPackets(new S_SystemMessage(c_clanname
				 * +" 혈맹에 가입 받을수 있는 케릭이 접속중이지 않습니다.")); return; }
				 */
				if (_target == null) {
					_pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 11), true);
					return;
				}

				if (type == 0) {// 가입신청
					혈가입(_pc, _target, clan);
				} else if (type == 1) {// 신청취소

				}
				break;
			default:
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void 혈가입(L1PcInstance player, L1PcInstance target, L1Clan clan) {
		if (player.getClanid() != 0) { // 이미 크란에 가입이 끝난 상태
			if (player.isCrown()) {
				player.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 13), true);
				return;
			} else {
				player.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 9), true);
				return;
			}
		}

		if (player.isCrown()) {
			player.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 13), true);
			return;
		}

		if (clan.getJoinSetting() == 0) {
			player.sendPackets(new S_SystemMessage("[" + clan.getClanName() + "] 혈맹은\n현재 가입할 수 없습니다."), true);
			// player.sendPackets(new
			// S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 8),true);
			return;
		} else if (clan.getJoinType() == 0) {// 즉시가입
			C_Attr.혈맹가입(target, player, clan);
			player.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 0), true);
			return;
		} else {// 승인가입
			target.setTempID(player.getId()); // 상대의 오브젝트 ID를 보존해 둔다
			player.getSkillEffectTimerSet().setSkillEffect(L1SkillId.가입신청ディレイ, 30000);
			// player.sendPackets(new
			// S_SystemMessage("["+clan.getClanName()+"] 혈맹에 가입신청을 하였습니다."));
			player.sendPackets(new S_NewCreateItem(S_NewCreateItem.CLAN_JOIN_MESSAGE, 1), true);
			if (target instanceof L1RobotInstance) {
				Random _rnd = new Random(System.nanoTime());
				GeneralThreadPool.getInstance().schedule(new join(target, player), 1000 + _rnd.nextInt(2000));
			} else {
				S_Message_YN myn = new S_Message_YN(97, player.getName());
				target.sendPackets(myn, true);
			}
		}

	}

	class join implements Runnable {

		private L1PcInstance crown;
		private L1PcInstance joinchar;

		public join(L1PcInstance _crown, L1PcInstance _joinchar) {
			crown = _crown;
			joinchar = _joinchar;
		}

		@Override
		public void run() {
			try {
				if (crown == null || joinchar == null || L1World.getInstance().getPlayer(crown.getName()) == null
						|| L1World.getInstance().getPlayer(joinchar.getName()) == null) {
					return;
				}
				clanJoin(crown, joinchar);
				if (((L1RobotInstance) crown)._userTitle == null || ((L1RobotInstance) crown)._userTitle.equalsIgnoreCase(""))
					return;
				Random _rnd = new Random(System.nanoTime());
				Thread.sleep(3000 + _rnd.nextInt(2000));

				if (L1World.getInstance().getPlayer(crown.getName()) == null || L1World.getInstance().getPlayer(joinchar.getName()) == null)
					return;

				joinchar.setTitle(((L1RobotInstance) crown)._userTitle);
				S_CharTitle ct = new S_CharTitle(joinchar.getId(), joinchar.getTitle());
				joinchar.sendPackets(ct);
				Broadcaster.broadcastPacket(joinchar, ct, true);
				try {
					if (!(joinchar instanceof L1RobotInstance))
						joinchar.save(); // DB에 캐릭터 정보를 써 우
				} catch (Exception e) {
				}

				L1Clan clan = L1World.getInstance().getClan(crown.getClanname());
				if (clan != null) {
					for (L1PcInstance clanPc : clan.getOnlineClanMember()) {
						// \f1%0이%1에 「%2라고 하는 호칭을 주었습니다.
						S_ServerMessage sm = new S_ServerMessage(203, crown.getName(), joinchar.getName(), joinchar.getTitle());
						clanPc.sendPackets(sm, true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void clanJoin(L1PcInstance pc, L1PcInstance joinPc) {
		try {
			int clan_id = pc.getClanid();
			String clanName = pc.getClanname();
			L1Clan clan = L1World.getInstance().getClan(clanName);
			if (clan != null) {
				int maxMember = 300;

				if (joinPc.getClanid() == 0) { // 크란미가입
					if (maxMember <= clan.getClanMemberList().size()) {// clanMembersName.length)
																		// { //
																		// 빈 곳이
																		// 없다
						joinPc.sendPackets(new S_ServerMessage(188, pc.getName())); // %0는 당신을 혈맹원으로서 받아들일 수가 없습니다.
						return;
					}
					for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
						clanMembers.sendPackets(new S_ServerMessage(94, joinPc.getName())); // \f1%0이 혈맹의 일원으로서 받아들여졌습니다.
					}
					joinPc.setClanid(clan_id);
					joinPc.setClanname(clanName);
					joinPc.setClanRank(L1Clan.CLAN_RANK_PROBATION);
					joinPc.setTitle("");
					joinPc.setClanJoinDate(new Timestamp(System.currentTimeMillis()));
					joinPc.sendPackets(new S_CharTitle(joinPc.getId(), ""));
					Broadcaster.broadcastPacket(joinPc, new S_CharTitle(joinPc.getId(), ""));
					joinPc.save(); // DB에 캐릭터 정보를 기입한다
					clan.addClanMember(joinPc.getName(), joinPc.getClanRank(), joinPc.getLevel(), joinPc.getType(), joinPc.getMemo(), joinPc.getOnlineStatus(),
							joinPc);
					joinPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_RANK_CHANGED, 0x07, joinPc.getName()), true);
					joinPc.sendPackets(new S_ClanJoinLeaveStatus(joinPc), true);
					Broadcaster.broadcastPacket(joinPc, new S_ClanJoinLeaveStatus(joinPc));
					joinPc.sendPackets(new S_ReturnedStat(joinPc, S_ReturnedStat.CLAN_JOIN_LEAVE), true);
					Broadcaster.broadcastPacket(joinPc, new S_ReturnedStat(joinPc, S_ReturnedStat.CLAN_JOIN_LEAVE));
					joinPc.sendPackets(new S_ClanWindow(S_ClanWindow.혈마크띄우기, joinPc.getClan().getmarkon()), true);
					joinPc.sendPackets(new S_문장주시(joinPc.getClan(), 2), true);
					pc.sendPackets(new S_PacketBox(pc, S_PacketBox.PLEDGE_REFRESH_PLUS));
					joinPc.sendPackets(new S_ServerMessage(95, clanName)); // \f1%0
																			// 혈맹에
																			// 가입했습니다.
				} else { // 크란 가입이 끝난 상태(크란 연합)
					joinPc.sendPackets(new S_SystemMessage("이미 혈맹에 가입했습니다.")); // \f1당신은
																				// 벌써
																				// 혈맹에
																				// 가입하고
																				// 있습니다.
				}
			}
		} catch (Exception e) {
		}
	}

	public String readSS(byte[] data) {
		String s = null;
		try {
			s = new String(data, _off2, data.length - _off2, "UTF-8");
			s = s.substring(0, s.indexOf('\0'));
			_off2 += s.getBytes("UTF-8").length + 1;
		} catch (Exception e) {
			// _log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}

	public String readS(byte[] data) {
		String s = null;
		try {
			s = new String(data, _off, data.length - _off, "UTF-8");
			s = s.substring(0, s.indexOf('\0'));
			_off += s.getBytes("UTF-8").length + 1;
		} catch (Exception e) {
			// _log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}

	public static final String CLIENT_KEY = "WebClient";

	private static final int 상인찾기버튼 = 0x01;
	private static final int 혈맹가입신청버튼 = 0x02;

	private int calcheading(int myx, int myy, int tx, int ty) {
		if (tx > myx && ty > myy) {
			return 3;
		} else if (tx < myx && ty < myy) {
			return 7;
		} else if (tx > myx && ty == myy) {
			return 2;
		} else if (tx < myx && ty == myy) {
			return 6;
		} else if (tx == myx && ty < myy) {
			return 0;
		} else if (tx == myx && ty > myy) {
			return 4;
		} else if (tx < myx && ty > myy) {
			return 5;
		} else {
			return 1;
		}
	}
	/*
	 * 혈맹 창에서 가입 신청 버튼 클릭시 1byte(opcode)0x01 가입을 위해 버튼을 클릭 1byte(type) 0x00 (data1)
	 * 클릭한 유저이름(string) 끝에 0x00붙임 (data2) 클릭한 버튼의 혈맹 이름(string) 끝에 0x00붙임
	 *
	 *
	 * 혈맹 신청 창에서 신청취소 버튼 클릭시 1byte(opcode)0x01 신청 취소를 위해 버튼을 클릭 1byte(type) 0x01
	 * (data1) 클릭한 유저이름(string) 끝에 0x00붙임 (data2) 클릭한 버튼의 혈맹 이름(string) 끝에 0x00붙임
	 */

}
