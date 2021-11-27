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

package l1j.server.server;

import static l1j.server.server.Opcodes.C_ACCEPT_XCHG;
import static l1j.server.server.Opcodes.C_ACTION;
import static l1j.server.server.Opcodes.C_ADD_BUDDY;
import static l1j.server.server.Opcodes.C_ADD_XCHG;
import static l1j.server.server.Opcodes.C_ALIVE;
import static l1j.server.server.Opcodes.C_ANSWER;
import static l1j.server.server.Opcodes.C_ASK_XCHG;
import static l1j.server.server.Opcodes.C_ATTACK;
import static l1j.server.server.Opcodes.C_ATTACK_CONTINUE;
import static l1j.server.server.Opcodes.C_BAN_MEMBER;
import static l1j.server.server.Opcodes.C_BOARD_DELETE;
import static l1j.server.server.Opcodes.C_BOARD_LIST;
import static l1j.server.server.Opcodes.C_BOARD_READ;
import static l1j.server.server.Opcodes.C_BOARD_WRITE;
import static l1j.server.server.Opcodes.C_BOOKMARK;
import static l1j.server.server.Opcodes.C_BUYABLE_SPELL;
import static l1j.server.server.Opcodes.C_BUY_SELL;
import static l1j.server.server.Opcodes.C_BUY_SPELL;
import static l1j.server.server.Opcodes.C_CANCEL_XCHG;
import static l1j.server.server.Opcodes.C_CHANGE_CASTLE_SECURITY;
import static l1j.server.server.Opcodes.C_CHANGE_DIRECTION;
import static l1j.server.server.Opcodes.C_CHANNEL;
import static l1j.server.server.Opcodes.C_CHAT;
import static l1j.server.server.Opcodes.C_CHECK_INVENTORY;
import static l1j.server.server.Opcodes.C_CHECK_PK;
import static l1j.server.server.Opcodes.C_CREATE_CUSTOM_CHARACTER;
import static l1j.server.server.Opcodes.C_CREATE_PLEDGE;
import static l1j.server.server.Opcodes.C_DEAD_RESTART;
import static l1j.server.server.Opcodes.C_DELETE_BOOKMARK;
import static l1j.server.server.Opcodes.C_DELETE_CHARACTER;
import static l1j.server.server.Opcodes.C_DEPOSIT;
import static l1j.server.server.Opcodes.C_DESTROY_ITEM;
import static l1j.server.server.Opcodes.C_DIALOG;
import static l1j.server.server.Opcodes.C_DROP;
import static l1j.server.server.Opcodes.C_DUEL;
import static l1j.server.server.Opcodes.C_EMBLEM;
import static l1j.server.server.Opcodes.C_ENTER_PORTAL;
import static l1j.server.server.Opcodes.C_ENTER_WORLD;
import static l1j.server.server.Opcodes.C_EXCHANGEABLE_SPELL;
import static l1j.server.server.Opcodes.C_EXCHANGE_SPELL;
import static l1j.server.server.Opcodes.C_EXCLUDE;
import static l1j.server.server.Opcodes.C_EXTENDED;
import static l1j.server.server.Opcodes.C_EXTENDED_PROTOBUF;
import static l1j.server.server.Opcodes.C_FAR_ATTACK;
import static l1j.server.server.Opcodes.C_FIX;
import static l1j.server.server.Opcodes.C_FIXABLE_ITEM;
import static l1j.server.server.Opcodes.C_GET;
import static l1j.server.server.Opcodes.C_GIVE;
import static l1j.server.server.Opcodes.C_GOTO_MAP;
import static l1j.server.server.Opcodes.C_GOTO_PORTAL;
import static l1j.server.server.Opcodes.C_HACTION;
import static l1j.server.server.Opcodes.C_HYPERTEXT_INPUT_RESULT;
import static l1j.server.server.Opcodes.C_JOIN_PLEDGE;
import static l1j.server.server.Opcodes.C_LEAVE_PARTY;
import static l1j.server.server.Opcodes.C_LEAVE_PLEDGE;
import static l1j.server.server.Opcodes.C_LOGIN;
import static l1j.server.server.Opcodes.C_LOGOUT;
import static l1j.server.server.Opcodes.C_MAIL;
import static l1j.server.server.Opcodes.C_MARRIAGE;
import static l1j.server.server.Opcodes.C_MERCENARYEMPLOY;
import static l1j.server.server.Opcodes.C_MOVE;
import static l1j.server.server.Opcodes.C_NPC_ITEM_CONTROL;
import static l1j.server.server.Opcodes.C_ONOFF;
import static l1j.server.server.Opcodes.C_OPEN;
import static l1j.server.server.Opcodes.C_PERSONAL_SHOP;
import static l1j.server.server.Opcodes.C_PLATE;
import static l1j.server.server.Opcodes.C_PLEDGE_WATCH;
import static l1j.server.server.Opcodes.C_QUERY_BUDDY;
import static l1j.server.server.Opcodes.C_QUERY_CASTLE_SECURITY;
import static l1j.server.server.Opcodes.C_QUERY_PERSONAL_SHOP;
import static l1j.server.server.Opcodes.C_QUIT;
import static l1j.server.server.Opcodes.C_RANK_CONTROL;
import static l1j.server.server.Opcodes.C_READ_NEWS;
import static l1j.server.server.Opcodes.C_READ_NOTICE;
import static l1j.server.server.Opcodes.C_REMOVE_BUDDY;
import static l1j.server.server.Opcodes.C_RESTART;
import static l1j.server.server.Opcodes.C_RETURN_SUMMON;
import static l1j.server.server.Opcodes.C_SAVEIO;
import static l1j.server.server.Opcodes.C_SAY;
import static l1j.server.server.Opcodes.C_SHUTDOWN;
import static l1j.server.server.Opcodes.C_SLAVE_CONTROL;
import static l1j.server.server.Opcodes.C_SUMMON;
import static l1j.server.server.Opcodes.C_TAX;
import static l1j.server.server.Opcodes.C_TELL;
import static l1j.server.server.Opcodes.C_THROW;
import static l1j.server.server.Opcodes.C_TITLE;
import static l1j.server.server.Opcodes.C_UPLOAD_EMBLEM;
import static l1j.server.server.Opcodes.C_USE_ITEM;
import static l1j.server.server.Opcodes.C_USE_SPELL;
import static l1j.server.server.Opcodes.C_VOICE_CHAT;
import static l1j.server.server.Opcodes.C_WAR;
import static l1j.server.server.Opcodes.C_WAREHOUSE_CONTROL;
import static l1j.server.server.Opcodes.C_WHO;
import static l1j.server.server.Opcodes.C_WHO_PLEDGE;
import static l1j.server.server.Opcodes.C_WITHDRAW;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
/*import l1j.server.server.clientpackets.C_NpcCraft;*/
import l1j.server.server.clientpackets.C_ActionUi;
import l1j.server.server.clientpackets.C_AddBookmark;
import l1j.server.server.clientpackets.C_AddBuddy;
import l1j.server.server.clientpackets.C_Adenshop;
import l1j.server.server.clientpackets.C_Amount;
import l1j.server.server.clientpackets.C_Attack;
import l1j.server.server.clientpackets.C_Attr;
import l1j.server.server.clientpackets.C_AuthLogin;
import l1j.server.server.clientpackets.C_AutoAttack;
import l1j.server.server.clientpackets.C_BanClan;
import l1j.server.server.clientpackets.C_Board;
import l1j.server.server.clientpackets.C_BoardBack;
import l1j.server.server.clientpackets.C_BoardDelete;
import l1j.server.server.clientpackets.C_BoardRead;
import l1j.server.server.clientpackets.C_BoardWrite;
import l1j.server.server.clientpackets.C_Buddy;
import l1j.server.server.clientpackets.C_CallPlayer;
import l1j.server.server.clientpackets.C_ChangeHeading;
import l1j.server.server.clientpackets.C_CharcterConfig;
import l1j.server.server.clientpackets.C_ChatWhisper;
import l1j.server.server.clientpackets.C_CheckPK;
import l1j.server.server.clientpackets.C_Clan;
import l1j.server.server.clientpackets.C_ClanMarkSee;
import l1j.server.server.clientpackets.C_ClanNotice;
import l1j.server.server.clientpackets.C_CreateClan;
import l1j.server.server.clientpackets.C_CreateNewCharacter;
import l1j.server.server.clientpackets.C_DelBuddy;
import l1j.server.server.clientpackets.C_DeleteBookmark;
import l1j.server.server.clientpackets.C_DeleteChar;
import l1j.server.server.clientpackets.C_DeleteInventoryItem;
import l1j.server.server.clientpackets.C_Deposit;
import l1j.server.server.clientpackets.C_Door;
import l1j.server.server.clientpackets.C_Drawal;
import l1j.server.server.clientpackets.C_DropItem;
import l1j.server.server.clientpackets.C_Emblem;
import l1j.server.server.clientpackets.C_EnterPortal;
import l1j.server.server.clientpackets.C_Exclude;
import l1j.server.server.clientpackets.C_ExtraCommand;
import l1j.server.server.clientpackets.C_Fight;
import l1j.server.server.clientpackets.C_FishClick;
import l1j.server.server.clientpackets.C_FixWeaponList;
import l1j.server.server.clientpackets.C_GiveItem;
import l1j.server.server.clientpackets.C_Horun;
import l1j.server.server.clientpackets.C_HorunOK;
import l1j.server.server.clientpackets.C_ItemUSe;
import l1j.server.server.clientpackets.C_ItemUSe2;
import l1j.server.server.clientpackets.C_JoinClan;
import l1j.server.server.clientpackets.C_KeepALIVE;
import l1j.server.server.clientpackets.C_LeaveClan;
import l1j.server.server.clientpackets.C_LeaveParty;
import l1j.server.server.clientpackets.C_LoginToServerOK;
import l1j.server.server.clientpackets.C_Macro;
import l1j.server.server.clientpackets.C_MailBox;
import l1j.server.server.clientpackets.C_MoveChar;
import l1j.server.server.clientpackets.C_NPCAction;
import l1j.server.server.clientpackets.C_NPCTalk;
import l1j.server.server.clientpackets.C_NoticeClick;
import l1j.server.server.clientpackets.C_PetMenu;
import l1j.server.server.clientpackets.C_PickUpItem;
import l1j.server.server.clientpackets.C_Pledge;
import l1j.server.server.clientpackets.C_Propose;
import l1j.server.server.clientpackets.C_Rank;
import l1j.server.server.clientpackets.C_Report;
import l1j.server.server.clientpackets.C_Restart;
import l1j.server.server.clientpackets.C_RestartAfterDie;
import l1j.server.server.clientpackets.C_ReturnStaus;
import l1j.server.server.clientpackets.C_ReturnToLogin;
import l1j.server.server.clientpackets.C_SabuTeleport;
import l1j.server.server.clientpackets.C_SecurityStatus;
import l1j.server.server.clientpackets.C_SecurityStatusSet;
import l1j.server.server.clientpackets.C_SelectCharacter;
import l1j.server.server.clientpackets.C_SelectList;
import l1j.server.server.clientpackets.C_SelectTarget;
import l1j.server.server.clientpackets.C_Ship;
import l1j.server.server.clientpackets.C_Shop;
import l1j.server.server.clientpackets.C_ShopAndWarehouse;
import l1j.server.server.clientpackets.C_ShopList;
import l1j.server.server.clientpackets.C_SkillBuy;
import l1j.server.server.clientpackets.C_SkillBuyOK;
import l1j.server.server.clientpackets.C_SoldierBuy;
import l1j.server.server.clientpackets.C_TalkIslandQuest;
import l1j.server.server.clientpackets.C_TaxRate;
import l1j.server.server.clientpackets.C_Title;
import l1j.server.server.clientpackets.C_Trade;
import l1j.server.server.clientpackets.C_TradeAddItem;
import l1j.server.server.clientpackets.C_TradeCancel;
import l1j.server.server.clientpackets.C_TradeOK;
import l1j.server.server.clientpackets.C_UsePetItem;
import l1j.server.server.clientpackets.C_UseSkill;
import l1j.server.server.clientpackets.C_War;
import l1j.server.server.clientpackets.C_WarehousePassword;
import l1j.server.server.clientpackets.C_Who;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_Notice;
import server.Authorization;
import server.LineageClient;
//import static l1j.server.server.Opcodes.C_OPCODE_FISHCANCEL; //새로 선언해 줍니당.

//Referenced classes of package l1j.server.server:
//Opcodes, LoginController, ClientThread, Logins

public class PacketHandler {
	private static Logger _log = Logger.getLogger(PacketHandler.class.getName());

	public PacketHandler(LineageClient clientthread) {
		_client = clientthread;
	}

	private long createCharTime = 0;
	private long 액션시간 = -1;

	public void handlePacket(byte abyte0[], L1PcInstance object) throws Exception {
		if (abyte0 == null || abyte0.length == 0) {
			return;
		}

		long now = System.currentTimeMillis();
		int i = 0;

		try {
			i = abyte0[0] & 0xff;
			if (Config.클라패킷출력) {
				System.out.println("c opcode : " + i);
				System.out.println("[C opocde] = " + i + "[Length] = " + abyte0.length);// 본섭옵코추출
				System.out.println(DataToPacket(abyte0, abyte0.length));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			if (object != null) {

				if (object.window_active_time != -1) {

					if (object.window_active_time <= now) {
						object.window_noactive_count++;

						if (object.window_noactive_count >= 3) {
							object.window_noactive_count = 0;
							object.window_active_time = -1;
						}
					} else {
						object.window_noactive_count = 0;
					}
				} else {
				}
			}

			switch (i) {
			case C_EXTENDED_PROTOBUF: // 종합패킷
				new C_ActionUi(abyte0, _client);
				if (object != null && object.getLevel() <= Config.클라우디아레벨) {
					new C_TalkIslandQuest(abyte0, _client);
				}
				break;
			case C_EXTENDED:
				new C_Adenshop(abyte0, _client);
				break;

			case C_READ_NOTICE:
				Authorization.getInstance().sendNotice(_client);
				break;

			case C_PLEDGE_WATCH:
				new C_ClanMarkSee(abyte0, _client);
				break;

			case C_SHUTDOWN:
				new C_ClanNotice(abyte0, _client);
				break;

			case C_ATTACK_CONTINUE:
				new C_AutoAttack(abyte0, _client);
				break;

			// case C_OPCODE_혈맹매칭: C_혈맹매칭 cm = new C_혈맹매칭(abyte0,
			// _client);break;
			case C_RETURN_SUMMON:
			case C_GOTO_PORTAL:
				new C_SabuTeleport(abyte0, _client);
				break;

			case C_EXCLUDE:
				new C_Exclude(abyte0, _client);
				break;

			case C_SAVEIO:
				new C_CharcterConfig(abyte0, _client);
				break;

			case C_OPEN:
				new C_Door(abyte0, _client);
				break;

			case C_TITLE:
				new C_Title(abyte0, _client);
				break;

			case C_BOARD_DELETE:
				new C_BoardDelete(abyte0, _client);
				break;

			case C_WHO_PLEDGE:
				new C_Pledge(abyte0, _client);
				break;

			case C_CHANGE_DIRECTION:
				new C_ChangeHeading(abyte0, _client);
				break;

			case C_HACTION:
				if (now < 액션시간) {
					return;
				}
				액션시간 = now + 400;
				new C_NPCAction(abyte0, _client);
				break;

			case C_USE_SPELL:
				new C_UseSkill(abyte0, _client);
				break;

			case C_UPLOAD_EMBLEM:
				if (_client.getActiveChar() == null || !_client.getActiveChar().isCrown()) {
					return;
				}
				new C_Emblem(abyte0, _client);
				break;

			case C_CANCEL_XCHG:
				new C_TradeCancel(abyte0, _client);
				break;

			// case C_OPCODE_WARTIMELIST: C_WarTimeList wtl = new
			// C_WarTimeList(abyte0, _client);break;
			case C_BOOKMARK:
				new C_AddBookmark(abyte0, _client);
				break;

			case C_CREATE_PLEDGE:
				new C_CreateClan(abyte0, _client);
				break;

			// case C_VERSION: // 170106 부터 없어짐
			// C_ServerVersion csv = new C_ServerVersion(abyte0, _client);
			// break;

			case C_MARRIAGE:
				new C_Propose(abyte0, _client);
				break;

			case C_BUYABLE_SPELL:
				new C_SkillBuy(abyte0, _client);
				break;

			case C_BOARD_LIST:
				new C_BoardBack(abyte0, _client);
				break;

			case C_PERSONAL_SHOP:
				new C_Shop(abyte0, _client);
				break;

			case C_BOARD_READ:
				new C_BoardRead(abyte0, _client);
				break;

			case C_ASK_XCHG:
				new C_Trade(abyte0, _client);
				break;

			case C_DELETE_CHARACTER:
				if (Config.캐릭터비번사용여부 == true) {
					if (!_client.getAccount().iscpwok()) {
						if (_client.getAccount().getCPW() == null) {
							_client.sendPacket(new S_CharPass(S_CharPass._비번생성창));
							return;
						} else {
							_client.getAccount().setwaitpacket(abyte0);
							_client.sendPacket(new S_CharPass(S_CharPass._비번입력창));
							return;
						}
					}
				}

				if (_client.getActiveChar() == null && createCharTime < System.currentTimeMillis()) {
					new C_DeleteChar(abyte0, _client);
					createCharTime = System.currentTimeMillis() + 2000;
				}
				break;

			case C_CREATE_CUSTOM_CHARACTER:
				if (Config.캐릭터비번사용여부 == true) {
					if (!_client.getAccount().iscpwok()) {
						if (_client.getAccount().getCPW() == null) {
							_client.sendPacket(new S_CharPass(S_CharPass._비번생성창));
							return;
						} else {
							_client.getAccount().setwaitpacket(abyte0);
							_client.sendPacket(new S_CharPass(S_CharPass._비번입력창));
							return;
						}
					}
				}
				if (createCharTime < System.currentTimeMillis()) {
					new C_CreateNewCharacter(abyte0, _client);
					createCharTime = System.currentTimeMillis() + 2000;
				}
				break;

			case C_ALIVE:
				new C_KeepALIVE(abyte0, _client);
				break;

			case C_ANSWER:
				new C_Attr(abyte0, _client);
				break;

			case C_LOGIN:
				new C_AuthLogin(abyte0, _client, true);
				break;

			case C_BUY_SELL:
				new C_ShopAndWarehouse(abyte0, _client);
				break;

			case C_DEPOSIT:
				new C_Deposit(abyte0, _client);
				break;

			case C_WITHDRAW:
				new C_Drawal(abyte0, _client);
				break;

			case C_ONOFF:
				new C_LoginToServerOK(abyte0, _client);
				break;

			case C_BUY_SPELL:
				new C_SkillBuyOK(abyte0, _client);
				break;

			case C_ADD_XCHG:
				new C_TradeAddItem(abyte0, _client);
				break;

			case C_ADD_BUDDY:
				new C_AddBuddy(abyte0, _client);
				break;

			case C_LOGOUT:
				new C_ReturnToLogin(abyte0, _client);
				break;

			case C_SAY:
				// C_Macro chat = new C_Macro(abyte0, _client);
				// chat = null;
				// break;

			case C_ACCEPT_XCHG:
				new C_TradeOK(abyte0, _client);
				break;

			case C_CHECK_PK:
				new C_CheckPK(abyte0, _client);
				break;

			case C_TAX:
				new C_TaxRate(abyte0, _client);
				break;

			case C_DEAD_RESTART:
				new C_RestartAfterDie(abyte0, _client);
				break;

			case C_RESTART:
				new C_Restart(abyte0, _client);
				break;

			case C_QUERY_BUDDY:
				new C_Buddy(abyte0, _client);
				break;

			case C_DROP:
				new C_DropItem(abyte0, _client);
				break;

			case C_LEAVE_PARTY:
				new C_LeaveParty(abyte0, _client);
				break;

			case C_ATTACK:
			case C_FAR_ATTACK:
				new C_Attack(abyte0, _client);
				break;

			// 캐릭터의 쇼트 컷이나 목록 상태가 플레이중에 변동했을 경우에
			// 쇼트 컷이나 목록 상태를 부가해 클라이언트로부터 송신되어 온다
			// 보내져 오는 타이밍은 클라이언트 종료시
			case C_QUIT:
				// =========== IP Check[#PacketHandler] ===========
				// if(Config.AUTH_CONNECT) {
				// LoginAuth authIP = new LoginAuth();
				// authIP.ConnectDelete(_client.getIp());
				// }
				// =========== IP Check[#PacketHandler] ===========
				break;

			case C_BAN_MEMBER:
				new C_BanClan(abyte0, _client);
				break;

			case C_PLATE:
				new C_Board(abyte0, _client);
				break;

			case C_DESTROY_ITEM:
				new C_DeleteInventoryItem(abyte0, _client);
				break;

			case C_TELL:
				new C_ChatWhisper(abyte0, _client);
				break;

			/*
			 * case C_WHO_PARTY: C_Party cparty = new C_Party(abyte0, _client); cparty =
			 * null; break;
			 */

			case C_GET:
				new C_PickUpItem(abyte0, _client);
				break;

			case C_WHO:
				new C_Who(abyte0, _client);
				break;

			case C_GIVE:
				new C_GiveItem(abyte0, _client);
				break;

			case C_MOVE:
				new C_MoveChar(abyte0, _client);
				break;

			case C_DELETE_BOOKMARK:
				new C_DeleteBookmark(abyte0, _client);
				break;
			/*
			 * case C_OPCODE_RESTART: C_RestartAfterDie cra = new C_RestartAfterDie(abyte0,
			 * _client); cra = null; break;
			 */
			case C_LEAVE_PLEDGE:
				new C_LeaveClan(abyte0, _client);
				break;

			case C_DIALOG:
				new C_NPCTalk(abyte0, _client);
				break;

			/*
			 * case C_BANISH_PARTY: C_BanParty cbpp = new C_BanParty(abyte0, _client); cbpp
			 * = null; break;
			 */

			case C_REMOVE_BUDDY:
				new C_DelBuddy(abyte0, _client);
				break;

			case C_WAR:
				new C_War(abyte0, _client);
				break;

			case C_ENTER_WORLD:
				if (Config.캐릭터비번사용여부 == true) {
					if (!_client.getAccount().iscpwok()) {
						if (_client.getAccount().getCPW() == null) {
							_client.sendPacket(new S_CharPass(S_CharPass._비번생성창));
							return;
						} else {
							_client.getAccount().setwaitpacket(abyte0);
							_client.sendPacket(new S_CharPass(S_CharPass._비번입력창));
							return;
						}
					}
				}
				new C_SelectCharacter(abyte0, _client);
				break;

			case C_QUERY_PERSONAL_SHOP:
				new C_ShopList(abyte0, _client);
				break;

			case C_CHAT:
				new C_Macro(abyte0, _client);
				break;

			case C_JOIN_PLEDGE:
				new C_JoinClan(abyte0, _client);
				break;

			case C_READ_NEWS:
				if (S_Notice.NoticeCount(_client.getAccountName()) > 0) {
					_client.sendPacket(new S_Notice(_client.getAccountName(), _client));
				} else {
					new C_NoticeClick(_client);
				}
				break;

			case C_ACTION:
				new C_ExtraCommand(abyte0, _client);
				break;

			case C_BOARD_WRITE:
				new C_BoardWrite(abyte0, _client);
				break;

			case C_USE_ITEM:
				new C_ItemUSe(abyte0, _client);
				new C_ItemUSe2(abyte0, _client);
				break;

			/*
			 * case C_INVITE_PARTY_TARGET: C_CreateParty ccp = new C_CreateParty(abyte0,
			 * _client); ccp = null; break;
			 */

			case C_ENTER_PORTAL:
				new C_EnterPortal(abyte0, _client);
				break;

			case C_HYPERTEXT_INPUT_RESULT:
				new C_Amount(abyte0, _client);
				break;

			case C_FIXABLE_ITEM:
				new C_FixWeaponList(abyte0, _client);
				break;
			// case C_OPCODE_SELECTLIST:

			case C_FIX:
				new C_SelectList(abyte0, _client);
				break;

			// case C_OPCODE_EXIT_GHOST: C_ExitGhost ceg = new
			// C_ExitGhost(abyte0, _client); ceg = null;break;

			case C_SUMMON:
				new C_CallPlayer(abyte0, _client);
				break;

			case C_THROW:
				new C_FishClick(abyte0, _client);
				break; // 수정

			// case C_OPCODE_FISHCLICK: C_FishClick fc = new C_FishClick(abyte0,
			// _client); fc = null;break;

			case C_SLAVE_CONTROL:
				new C_SelectTarget(abyte0, _client);
				break;

			case C_CHECK_INVENTORY:
				new C_PetMenu(abyte0, _client);
				break;

			case C_NPC_ITEM_CONTROL:
				new C_UsePetItem(abyte0, _client);
				break;

			// case C_OPCODE_TELEPORT: C_Teleport cte = new C_Teleport(abyte0,
			// _client); cte = null;break;

			case C_RANK_CONTROL:
				new C_Rank(abyte0, _client);
				break;

			/*
			 * case C_CHAT_PARTY_CONTROL: C_ChatParty cpart = new C_ChatParty(abyte0,
			 * _client); cpart = null; break;
			 */

			case C_DUEL:
				new C_Fight(abyte0, _client);
				break;

			case C_GOTO_MAP:
				new C_Ship(abyte0, _client);
				break;

			case C_MAIL:
				new C_MailBox(abyte0, _client);
				break;

			case C_VOICE_CHAT:
				new C_ReturnStaus(abyte0, _client);
				break;

			case C_WAREHOUSE_CONTROL:
				new C_WarehousePassword(abyte0, _client);
				break; // 창고 비번

			case C_EXCHANGEABLE_SPELL:
				new C_Horun(abyte0, _client);
				break;

			case C_EXCHANGE_SPELL:
				new C_HorunOK(abyte0, _client);
				break;

			case C_MERCENARYEMPLOY:
				new C_SoldierBuy(abyte0, _client);
				break;

			// case C_______OPCODE_SOLDIERGIVE: //용병
			// C_SoldierGive csg2 = new C_SoldierGive(abyte0, _client);
			// csg2 = null;
			// break;
			// case C_OPCODE_SOLDIERGIVEOK:
			// C_SoldierGiveOK csg = new C_SoldierGiveOK(abyte0, _client);
			// csg = null;
			// break;
			// case C_OPCODE_WARTIMESET: C_WarTimeSet wts = new
			// C_WarTimeSet(abyte0, _client); wts = null;break;
			case C_EMBLEM:
				new C_Clan(abyte0, _client);
				break;

			case C_QUERY_CASTLE_SECURITY:
				new C_SecurityStatus(abyte0, _client);
				break;

			case C_CHANGE_CASTLE_SECURITY:
				new C_SecurityStatusSet(abyte0, _client);
				break;

			case C_CHANNEL:
				new C_Report(abyte0, _client);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			_log.log(Level.SEVERE, e.getLocalizedMessage(), (i != 0 ? "[OP:" + i + "]" : "") + e);
		}

	}

	public static String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;

		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(HexToDex(i, 4) + ": ");
			}

			result.append(HexToDex(data[i] & 0xff, 2) + " ");
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

	private static String HexToDex(int data, int digits) {
		String number = Integer.toHexString(data);

		for (int i = number.length(); i < digits; i++) {
			number = "0" + number;
		}

		return number;
	}

	private final LineageClient _client;

}