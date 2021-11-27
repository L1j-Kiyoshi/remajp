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

package l1j.server.server.clientpackets;

/*import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;*/
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
//import l1j.server.L1DatabaseFactory;
//import l1j.server.server.BadNamesList;
import l1j.server.server.GMCommands;
import l1j.server.server.Opcodes;
import l1j.server.server.UserCommands;
//import l1j.server.server.datatables.CharacterTable;
//import l1j.server.server.datatables.NpcShopSpawnTable;
//import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
//import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
//import l1j.server.server.utils.SQLUtil;
import server.LineageClient;


//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

//chat opecode type
//통상 0x44 0x00
//절규(! ) 0x44 0x00
//속삭임(") 0x56 charname
//전체(&) 0x72 0x03
//트레이드($) 0x44 0x00
//PT(#) 0x44 0x0b
//혈맹(@) 0x44 0x04
//연합(%) 0x44 0x0d
//CPT(*) 0x44 0x0e

public class C_Chat extends ClientBasePacket {
	

	private static final String C_CHAT = "[C] C_Chat";

	private static final String[] textFilter = { "세호", "행복", "폭스", "칸즈", "철웅", "현민", "ㅍㅅ", "ㅎㅂ", "ㅋㅈ", "퐁스" };

	/*private boolean npcshopNameCk(String name) {
		return NpcTable.getInstance().findNpcShopName(name);
	}*/

	/** 변경 가능한지 검사한다 시작 **/

	/*private void chaname(String chaName, String oldname) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE characters SET char_name=? WHERE char_name=?");
			pstm.setString(1, chaName);
			pstm.setString(2, oldname);
			pstm.executeUpdate();
		} catch (Exception e) {

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}*/

	/*private void logchangename(String chaName, String oldname,
			Timestamp datetime) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			String sqlstr = "INSERT INTO Log_Change_name SET Old_Name=?,New_Name=?, Time=?";
			pstm = con.prepareStatement(sqlstr);
			pstm.setString(1, oldname);
			pstm.setString(2, chaName);
			pstm.setTimestamp(3, datetime);
			pstm.executeUpdate();
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}*/

	/*private static boolean isAlphaNumeric(String s) {
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}
			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}
			i++;
		} while (true);
		return flag;
	}*/

	/*private static boolean isInvalidName(String name) {
		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes("EUC-KR").length;
		} catch (UnsupportedEncodingException e) {
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return false;
		}

		if (isAlphaNumeric(name)) {
			return false;
		}
		if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) {
			return false;
		}

		if (BadNamesList.getInstance().isBadName(name)) {
			return false;
		}
		return true;
	}*/

	public C_Chat(byte abyte0[], LineageClient clientthread) {
		super(abyte0);
		try {
			L1PcInstance pc = clientthread.getActiveChar();
			int chatType = readC();
			String chatText = readS();


			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SILENCE)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.AREA_OF_SILENCE)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_POISON_SILENCE)) {
				return;
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_CHAT_PROHIBITED)) { // 채팅 금지중
				S_ServerMessage sm = new S_ServerMessage(242);
				pc.sendPackets(sm); // 현재 채팅 금지중입니다.
				sm = null;
				return;
			}

			if (pc.isDeathMatch() && !pc.isGhost() && !pc.isGm()) {
				S_ServerMessage sm = new S_ServerMessage(912);
				pc.sendPackets(sm); // 채팅을 할 수 없습니다.
				sm = null;
				return;
			}

			if (!pc.isGm()) {
				for (String tt : textFilter) {
					int indexof = chatText.indexOf(tt);
					if (indexof != -1) {
						int count = 100;
						while ((indexof = chatText.indexOf(tt)) != -1) {
							if (count-- <= 0)
								break;
							char[] dd = chatText.toCharArray();
							chatText = "";
							for (int i = 0; i < dd.length; i++) {
								if (i >= indexof && i <= (indexof + tt.length() - 1)) {
									chatText = chatText + "  ";
								} else
									chatText = chatText + dd[i];
							}
						}
					}
				}
			}
            
			switch (chatType) {
			case 0: {
				if (pc.isGhost() && !(pc.isGm() || pc.isMonitor())) {
					return;
				}
				if (chatText.startsWith(".시각")) {
					StringBuilder sb = null;
					sb = new StringBuilder();
					TimeZone kst = TimeZone.getTimeZone("GMT+9");
					Calendar cal = Calendar.getInstance(kst);
					sb.append("[Server Time]" + cal.get(Calendar.YEAR) + "년 " + (cal.get(Calendar.MONTH) + 1) + "월 "
							+ cal.get(Calendar.DATE) + "일 " + cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE)
							+ ":" + cal.get(Calendar.SECOND));
					S_SystemMessage sm = new S_SystemMessage(sb.toString());
					pc.sendPackets(sm, true);
					sb = null;
					return;
				}
				// GM커멘드
				if (chatText.startsWith(".") && (pc.getAccessLevel() == Config.GMCODE)) {
					String cmd = chatText.substring(1);
					GMCommands.getInstance().handleCommands(pc, cmd);
					System.out.println("bb");
					return;
				}
				
				if (chatText.startsWith("$")) {
					String text = chatText.substring(1);
					chatWorld(pc, text, 12);
					if (!pc.isGm()) {
						pc.checkChatInterval();
					}
					return;
				}

	
				if (chatText.startsWith(".")) { // 유저코멘트
					String cmd = chatText.substring(1);
					if (cmd == null) {
						return;
					}
					UserCommands.getInstance().handleCommands(pc, cmd);
					return;
				}

				if (chatText.startsWith("$")) { // 월드채팅
					String text = chatText.substring(1);

					chatWorld(pc, text, 12);
					if (!pc.isGm()) {
						pc.checkChatInterval();
					}
					return;
				}

				/** 텔렉 풀기 **/
	
				S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_SAY, 0);
				if (!pc.getExcludingList().contains(pc.getName())) {
					if (pc.getMapId() != 2699) {
						pc.sendPackets(s_chatpacket);
					}
				}
				for (L1PcInstance listner : L1World.getInstance().getRecognizePlayer(pc)) {
					if (!listner.getExcludingList().contains(pc.getName())) {
						if (listner.getMapId() == 2699) {
							continue;
						}
						listner.sendPackets(s_chatpacket);
					}
				}
				// 돕펠 처리
				L1MonsterInstance mob = null;
				for (L1Object obj : pc.getNearObjects().getKnownObjects()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
							Broadcaster.broadcastPacket(mob, new S_NpcChatPacket(mob, chatText, 0), true);
						}
					}
				}
				
				
			}
				break;
			case 2: {
				try{
				if (pc.isGhost()) {
					return;
				}
				S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_SAY, 2);
				if (!pc.getExcludingList().contains(pc.getName())) {
					pc.sendPackets(s_chatpacket);
				}
				for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(pc, 50)) {
					if (!listner.getExcludingList().contains(pc.getName())) {
						listner.sendPackets(s_chatpacket);
					}
				}
				// 0319eva.LogChatNormalAppend("[일반]", pc.getName(), chatText);
				// 돕펠 처리
				L1MonsterInstance mob = null;
				for (L1Object obj : pc.getNearObjects().getKnownObjects()) {
					if (obj instanceof L1MonsterInstance) {
						mob = (L1MonsterInstance) obj;
						if (mob.getNpcTemplate().is_doppel() && mob.getName().equals(pc.getName())) {
							for (L1PcInstance listner : L1World.getInstance().getVisiblePlayer(mob, 30)) {
								listner.sendPackets(new S_NpcChatPacket(mob, chatText, 2), true);
							}
						}
					}
				}
				} catch (Exception e) {}
			}
			
				break;
			case 3: {
				chatWorld(pc, chatText, chatType);
			}
				break;
			case 4: {
				if (pc.getClanid() != 0) { // 크란 소속중
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
							
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_MESSAGE, 4);

			
					for (L1PcInstance listner : clan.getOnlineClanMember()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(s_chatpacket);
						}
					}
					
				}
			}
				break;
			case 11: {
				if (pc.isInParty()) { // 파티중
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_MESSAGE, 11);
					for (L1PcInstance listner : pc.getParty().getMembers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(s_chatpacket);
						}
					}
			
				}
		
			}
				break;
			case 12: {
				chatWorld(pc, chatText, chatType);
			}
				break;
			case 13: { // 수호기사 채팅
				if (pc.getClanid() != 0) { // 혈맹 소속중
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					int rank = pc.getClanRank();
					if (clan != null
							&& (rank == L1Clan.CLAN_RANK_GUARDIAN || rank == L1Clan.CLAN_RANK_SUBPRINCE || rank == L1Clan.CLAN_RANK_PRINCE)) {
						S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_SAY, 15);
						for (L1PcInstance listner : clan.getOnlineClanMember()) {
							int listnerRank = listner.getClanRank();
							if (!listner.getExcludingList().contains(pc.getName())
									&& (listnerRank == L1Clan.CLAN_RANK_GUARDIAN || rank == L1Clan.CLAN_RANK_SUBPRINCE || listnerRank == L1Clan.CLAN_RANK_PRINCE)) {
								listner.sendPackets(s_chatpacket);
							}
						}
					}
			
				}
			
			}
				break;
			case 14: { // 채팅 파티
				if (pc.isInChatParty()) { // 채팅 파티중
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_SAY, 14);
					for (L1PcInstance listner : pc.getChatParty().getMembers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							listner.sendPackets(s_chatpacket);
						}
					}
				}
	
			}
				break;
			case 15: { // 동맹채팅
				if (pc.getClanid() != 0) { // 혈맹 소속중
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());

					if (clan != null) {
						Integer allianceids[] = clan.Alliance();
						if (allianceids.length > 0) {
							String TargetClanName = null;
							L1Clan TargegClan = null;

							S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_SAY, 13);
							// S_ChatPacket s_chatpacket = new S_ChatPacket(pc,
							// chatText, Opcodes.S_OPCODE_MSG, 15);
							// 원래는 온라인중인 자기의 혈원과 온라인중인 동맹의 혈원한테 쏘아주어야함. (현재는
							// 대처용)
							for (L1PcInstance listner : clan.getOnlineClanMember()) {
								int AllianceClan = listner.getClanid();
								if (pc.getClanid() == AllianceClan) {
									listner.sendPackets(s_chatpacket);
								}
							} // 자기혈맹 전송용

							for (int j = 0; j < allianceids.length; j++) {
								TargegClan = clan.getAlliance(allianceids[j]);
								if (TargegClan != null) {
									TargetClanName = TargegClan.getClanName();
									if (TargetClanName != null) {
										for (L1PcInstance alliancelistner : TargegClan.getOnlineClanMember()) {
											alliancelistner.sendPackets(s_chatpacket);
										} // 동맹혈맹 전송용
									}
								}

							}
						}

					}
				}
				break;
			}
			case 17:
				if (pc.getClanid() != 0) { // 혈맹 소속중
					L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
					if (clan != null && (pc.isCrown() && pc.getId() == clan.getLeaderId())) {
						S_ChatPacket s_chatpacket = new S_ChatPacket(pc, chatText, Opcodes.S_MESSAGE, 17);
						for (L1PcInstance listner : clan.getOnlineClanMember()) {
							if (!listner.getExcludingList().contains(pc.getName())) {
								listner.sendPackets(s_chatpacket);
							}
						}
					}
				}
				break;
				
			}
			
			if (!pc.isGm()) {
				pc.checkChatInterval();
			}
			pc = null; //누수방지
			chatText = null; //누수방지
	
		} catch (Exception e) {
        
		} finally {
		  	clear();
	
			
		}
	}


	private void chatWorld(L1PcInstance pc, String chatText, int chatType) {
		if (pc.getLevel() >= Config.GLOBAL_CHAT_LEVEL) {
			if (pc.isGm() || L1World.getInstance().isWorldChatElabled()) {
				if (pc.get_food() >= 12) { // 5%겟지?
					S_PacketBox pb = new S_PacketBox(S_PacketBox.FOOD, pc.get_food());
					pc.sendPackets(pb, true);
					if (chatType == 3) {
						S_PacketBox pb2 = new S_PacketBox(S_PacketBox.FOOD, pc.get_food());
						pc.sendPackets(pb2, true);
						if (pc.isGm()) {
							L1World.getInstance().broadcastPacketToAll(
									new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "\\f=[******] " + chatText));
							L1World.getInstance().broadcastPacketToAll(
									new S_ChatPacket(pc, "[******] " + chatText, Opcodes.S_MESSAGE, chatType));
							return;
						}
					} else if (chatType == 12) {
						S_PacketBox pb3 = new S_PacketBox(S_PacketBox.FOOD, pc.get_food());
						pc.sendPackets(pb3, true);
						if (pc.isGm()) {
							L1World.getInstance().broadcastPacketToAll(new S_ChatPacket(pc, "[******] " + chatText, Opcodes.S_MESSAGE, chatType));
							return;
						}
					}
					for (L1PcInstance listner : L1World.getInstance().getAllPlayers()) {
						if (!listner.getExcludingList().contains(pc.getName())) {
							if (listner.isShowTradeChat() && chatType == 12) {
								S_ChatPacket cp = new S_ChatPacket(pc, chatText, Opcodes.S_MESSAGE, chatType);
								listner.sendPackets(cp, true);
							} else if (listner.isShowWorldChat() && chatType == 3) {
								S_ChatPacket cp = new S_ChatPacket(pc, chatText, Opcodes.S_MESSAGE, chatType);
								listner.sendPackets(cp, true);
							}
						}
					}
				} else {
					S_ServerMessage sm = new S_ServerMessage(462);
					pc.sendPackets(sm, true);
				}
			} else {
				S_ServerMessage sm = new S_ServerMessage(510);
				pc.sendPackets(sm, true);
			}
		} else {
			S_ServerMessage sm = new S_ServerMessage(195, String.valueOf(Config.GLOBAL_CHAT_LEVEL));
			pc.sendPackets(sm, true);
		}
	}

	@Override
	public String getType() {
		return C_CHAT;
	}
}
