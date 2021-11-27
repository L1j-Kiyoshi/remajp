package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_ChatPacket extends ServerBasePacket {

	private static final String _S__1F_NORMALCHATPACK = "[S] S_ChatPacket";
	private byte[] _byte = null;

	public S_ChatPacket(String targetname, String chat, int opcode) {
		writeC(opcode);
		writeC(9);
		writeS("-> (" + targetname + ") " + chat);
	}

	// 매니저용 귓말
	public S_ChatPacket(String from, String chat) {
		writeC(Opcodes.S_TELL);
		writeS(from);
		writeS(chat);
	}

	public S_ChatPacket(L1PcInstance pc, String chat) {
		writeC(Opcodes.S_MESSAGE);
		writeC(1);// 10
		writeS(chat);
	}

	// 경비대장 -> 성혈에 메시지 전송
	public S_ChatPacket(String name, String chat, int type, boolean c) {
		writeC(Opcodes.S_MESSAGE);
		writeC(17);
		writeS("{" + name + "} " + chat);
	}

	public S_ChatPacket(L1NpcInstance npc, String chat, int opcode, int type) {
		writeC(opcode);
		switch (type) {
		case 3:
		case 12:
			writeC(type);
			writeS("[" + npc.getName() + "] " + chat);
			break;
		case 9:
			writeC(type);
			writeS("-> (" + npc.getName() + ") " + chat);
			break;
		default:
			break;
		}
	}

	public S_ChatPacket(L1PcInstance pc, String chat, int opcode, int type) {
		writeC(opcode);

		String name = pc.getName();
		switch (type) {
		case 0:
			writeC(type);
			writeD(pc.getId());
			writeS(name + ": " + chat);
			break;
		case 2:
			writeC(type);
			if (pc.isInvisble()) {
				writeD(0);
			} else {
				writeD(pc.getId());
			}

			writeS("<" + pc.getName() + "> " + chat);
			writeH(pc.getX());
			writeH(pc.getY());
			break;
		case 3:
			if (pc.isGm()) {
				writeC(0);
				StringBuilder sb = new StringBuilder();
				if (chat.getBytes().length > 52) {
					boolean ck = false;
					for (char d : chat.toCharArray()) {
						sb.append(d);
						if (!ck) {
							if (sb.toString().getBytes().length >= 53) {
								ck = true;
								sb.append("\\aD");
							}
						}
					}
					chat = sb.toString();
				}
				writeS("\\aD[******] " + chat);
				// L1World.getInstance().broadcastPacketToAll(new
				// S_PacketBox(S_PacketBox.GREEN_MESSAGE,
				// "\\f=[******] " + chat));
			} else {
				writeC(type);
				writeS("[" + name + "] " + chat);
			}
			// eva.LogChatAppend("[전체]", pc.getName(), chat);
			break;
		case 4:
			writeC(type);
			// writeS("{" + pc.getName() +"} " + chat); //원본
			if (pc.getAge() == 0) { // 나이
				writeS("{" + pc.getName() + "} " + chat);
			} else {
				writeS("{" + pc.getName() + "(" + pc.getAge() + ")" + "} "
						+ chat);
			}
			break;
		case 40:// 영자에게보여주기위해
			writeC(4);
			// writeS("{" + pc.getName() +"} " + chat); //원본
			if (pc.getAge() == 0) { // 나이
				writeS(pc.getClanname() + " : {" + pc.getName() + "} " + chat);
			} else {
				writeS(pc.getClanname() + " : {" + pc.getName() + "("
						+ pc.getAge() + ")" + "} " + chat);
			}
			break;
		case 9:
			writeC(type);
			writeS("-> (" + pc.getName() + ") " + chat);
			break;
		case 90:
			writeC(9);
			writeS("-> (" + pc.getName() + ") " + chat);
			break;
		case 11:
			writeC(type);
			writeS("(" + pc.getName() + ") " + chat);
			break;
		case 110:
			name = pc.getParty().getLeader().getName();
			writeC(11);
			writeS(name + " - (" + pc.getName() + ") " + chat);
			break;
		case 12:
			if (pc.isGm()) {
				writeC(0);
				StringBuilder sb = new StringBuilder();
				if (chat.getBytes().length > 52) {
					boolean ck = false;
					for (char d : chat.toCharArray()) {
						sb.append(d);
						if (!ck) {
							if (sb.toString().getBytes().length >= 53) {
								ck = true;
								sb.append("\\aD");
							}
						}
					}
					chat = sb.toString();
				}
				writeS("\\aD[******] " + chat);
			} else {
				writeC(type);
				writeS("[" + name + "] " + chat);
			}
			break;
		case 13: // 동맹
			writeC(13);
			writeD(pc.getId());
			writeS("\\fV[" + pc.getName() + "] " + chat);
			// writeS("\\fV["+pc.getClan().getClanName()+"][" + pc.getName() +
			// "] " + chat);
			// writeS("{{" + pc.getName() + "}} " + chat);
			break;
		case 14:
			writeC(type);
			writeD(pc.getId());
			writeS("(" + pc.getName() + ") " + chat);
			break;
		case 15: // 수호기사
			writeC(15);
			writeD(pc.getId());
			writeS("{{" + pc.getName() + "}} " + chat);
			// writeS("["+pc.getClan().getClanName()+"][" + pc.getName() + "] "
			// + chat);
			break;
		case 16:
			writeS(pc.getName());
			writeS(chat);
			break;
		case 17: // 군주채팅
			writeC(type);
			writeS("{" + pc.getName() + "} " + chat);
			break;
		case 99:
			writeC(3);
			writeS("\\fC" + pc.getName() + "님이 " + chat + "과의 전투에서 승리하였습니다.");
			break;
		case 1001:
			writeC(11);
			writeS(chat);
			break;
		default:
			writeC(type);
			writeD(pc.getId());
			writeS(chat);
			break;
		}
		/*
		 * // 모니터링을 위해 추가 switch (type) { case
		 * ChatMonitorChannel.CHAT_MONITOR_CLAN: writeC(opcode); writeC(type);
		 * writeS("[" + pc.getClanname() + "]{" + pc.getName() + "} " + chat);
		 * break; case ChatMonitorChannel.CHAT_MONITOR_PARTY: writeC(opcode);
		 * writeC(type); writeS(pc.getName() + ": " + chat); break; case
		 * ChatMonitorChannel.CHAT_MONITOR_WHISPER: writeC(opcode);
		 * writeC(type); writeS(pc.getName() + " " + chat); break; case
		 * ChatMonitorChannel.CHAT_MONITOR_GM: writeC(opcode); writeC(type);
		 * if(pc != null) writeS(pc.getName() + "에게 : " + chat); else
		 * writeS(chat); break; }
		 */
	}

	@Override
	public byte[] getContent() {
		if (null == _byte) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return _S__1F_NORMALCHATPACK;
	}

}