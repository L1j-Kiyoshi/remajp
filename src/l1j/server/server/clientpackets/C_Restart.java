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

import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.MiniGame.DeathMatch;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.server.UserCommands;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_CharAmount;
import l1j.server.server.serverpackets.S_CharPass;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Unknown2;
import server.LineageClient;

public class C_Restart extends ClientBasePacket {
	

	private static final String C_OPCODE_RESTART = "[C] C_Restart";
	private static Logger _log = Logger.getLogger(C_Restart.class.getName());

	public C_Restart(byte[] decrypt, LineageClient client) throws Exception {
		super(decrypt);
		try {
			if (client.getActiveChar() != null) {
				if (DeathMatch.getInstance().isPlayerMember(
						client.getActiveChar())) {
					client.sendPacket(new S_SystemMessage(
							"데스매치에서는 리스 할 수 없습니다."), true);
					return;
				} else if (client.getActiveChar().getMapId() >= 10000
						&& client.getActiveChar().getMapId() <= 10005) {
					client.sendPacket(new S_SystemMessage(
							"린드비오르 레이드 에서는 리스 할 수 없습니다."), true);
					return;
				}
			}
			
			L1PcInstance pc = client.getActiveChar();
			pc.restart = true;
			
			/** 인터서버 접속중인걸로 체크한다면 정보 다 갱신 */
			if(client.getInterServer()){
				client.setInterServer(false);
				client.setInterServerType(0);
			}
			
			/** 2016.11.26 MJ 앱센터 LFC **/
			/* instance space 안 리스 처리 */
			if(MJInstanceSpace.isInInstance(pc)){
				if(pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE)
					return;
				C_RestartAfterDie.restartProcess(pc);
			}

			if (pc.isGm()) {
				Config.remove전체(pc);
			}
			try {
				/** 펫이 소환중인지 체크해서 정리 펫이 만약 죽었다면 그냥 나두도록함 */
				if (pc.getPetList() != null && pc.getPetListSize() > 0) {
					for (Object petObject : pc.getPetList()) {
						if (petObject == null) continue;
						if (petObject instanceof L1PetInstance) {
							L1PetInstance pet = (L1PetInstance) petObject;
							PetTable.UpDatePet(pet);
							pc.removePet((L1NpcInstance) pet);
							if(!pet.isDead()) pet.deletePet();
						} else if (petObject instanceof L1SummonInstance) {
							L1SummonInstance sunm = (L1SummonInstance) petObject;
							sunm.dropItem();
							pc.removePet((L1NpcInstance) sunm);
							sunm.deleteMe();
						}
					}
				}
			} catch (Exception e) {}

			client.CharReStart(true);
			S_PacketBox pb = new S_PacketBox(S_PacketBox.LOGOUT);
			client.sendPacket(pb, true);
			if(Config.캐릭터비번사용여부){
				try {
					if (client.getAccount().iscpwok()) {
						client.sendPacket(new S_CharPass(S_CharPass._케릭선택창진입), true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				// 	_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}

			if (client.getActiveChar() != null) {

				pc.setadFeature(1);
				try {
					pc.save();
				} catch (Exception e) {
				}
				try {
					pc.saveInventory();
				} catch (Exception e) {
				}
				try {
					pc.getNetConnection().getAccount().updateDGTime();
				} catch (Exception e) {
				}

				/*
				 * try{ pc.getNetConnection().getAccount().Ainupdate();
				 * }catch(Exception e){}
				 */
				try {
					pc.getNetConnection().getAccount().updateTam();
				} catch (Exception e) {
				}
				try {
					pc.getNetConnection().getAccount().updateNcoin();
				} catch (Exception e) {
				}

				/** 배틀존 **/
				if (pc.get_DuelLine() != 0) {
					pc.set_DuelLine(0);
				}
				
				/** 2011.04.07 고정수 배틀존 */
				if (!(pc.getInventory().checkItem(999999, 1) || pc
						.getInventory().checkItem(999998, 1))) { // 무인PC 만들기(쿠우)
					_log.fine("Disconnect from: " + pc.getName());
					synchronized (pc) {
						client.quitGame(pc);
						pc.logout();
						client.setActiveChar(null);
						pc.setPacketOutput(null);
					}
				} else { // 무인PC 만들기(쿠우)
					synchronized (pc) {
						pc.noPlayerCK = true; // 무인 제외 추가
						pc.setNetConnection(null);
						client.setActiveChar(null);
						pc.setPacketOutput(null);
					}
				}
				// }
			} else {
				_log.fine("Disconnect Request from Account : "
						+ client.getAccountName());
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}
	
	/** MJCTSystem **/
	public static void restartProcess(L1PcInstance pc){
		//pc.isWorld = false; 임시주석

		L1Clan clan = L1World.getInstance().getClan(pc.getClanid());
		if (clan != null) {
			String CharName = pc.getName(); 
			pc.setOnlineStatus(0);
			clan.removeOnlineClanMember(CharName);
		}
		_log.fine("Disconnect from: " + pc.getName());
		LineageClient client = pc.getNetConnection();
		if(client == null)
			return;

		synchronized (pc) {
			try{
				if(pc.isPrivateShop()){
					UserCommands.privateShop1(pc);
					return;
				}
				pc.setNetConnection(null);
				pc.setPacketOutput(null);
				pc.stopHpRegenerationByDoll();
				pc.stopMpRegenerationByDoll();
				pc.setAIprivateShop(true);
				try {
					pc.save();
					pc.saveInventory();
					pc.logout();
				} catch (Exception e) {
				}
				client.setActiveChar(null);
				client.CharReStart(true);
				client.sendPacket(new S_Unknown2(1)); // 리스버튼을 위한 구조변경 // Episode U
				if(client.getAccount().is_changed_slot()){
					int amountOfChars = client.getAccount().countCharacters();
					int slot = client.getAccount().getCharSlot();
					client.sendPacket(new S_CharAmount(amountOfChars, slot));
					C_NoticeClick.sendCharPacks(client);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
		//MJRestartChain.getInstance().on_restarted(pc); 임시 주석
	}

	@Override
	public String getType() {
		return C_OPCODE_RESTART;
	}
}
