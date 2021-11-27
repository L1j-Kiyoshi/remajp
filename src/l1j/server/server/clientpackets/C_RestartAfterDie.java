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

import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.FireDragon.FireDragon;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_OtherCharPacks;
import l1j.server.server.serverpackets.S_OwnCharPack;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerVersion;
import l1j.server.server.serverpackets.S_Weather;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_RestartAfterDie extends ClientBasePacket {
	

	private static final String C_RESTART = "[C] C_Restart";

	public C_RestartAfterDie(byte abyte0[], LineageClient clientthread)
			throws Exception {
		super(abyte0);
		try {
			L1PcInstance pc = clientthread.getActiveChar();

			if (!pc.isDead()) return;

			/** 2016.11.26 MJ 앱센터 LFC **/
			/* instance space 안에서는 리스불가 */
			if(MJInstanceSpace.isInInstance(pc) && pc.getInstStatus() != InstStatus.INST_USERSTATUS_NONE) return;
			
			restartProcess(pc);
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	public static void restartProcess(L1PcInstance pc){
		int[] loc;
		if (pc.getHellTime() > 0) {
			loc = new int[3];
			loc[0] = 32701;
			loc[1] = 32777;
			loc[2] = 666;
		} else {
			loc = Getback.GetBack_Restart(pc);
			if (pc.getMapId() >= 2100 && pc.getMapId() <= 2151) {
				loc[0] = 34063;
				loc[1] = 32270;
				loc[2] = (short) 4;
			}
		}

		/** 2011.07.31 고정수 복사 버그 방지 */
	
		if (pc.getInventory().checkItem(6013)) {
			pc.getInventory().consumeItem(6013,
					pc.getInventory().countItems(6013));
		}
		if (pc.getInventory().checkItem(6014)) {
			pc.getInventory().consumeItem(6014,
					pc.getInventory().countItems(6014));
		}
		if (pc.getInventory().checkItem(60512)) {
			pc.getInventory().consumeItem(60512,
					pc.getInventory().countItems(60512));
		}
		if (pc.getInventory().checkItem(60513)) {
			pc.getInventory().consumeItem(60513,
					pc.getInventory().countItems(60513));
		}
		try {
			if (pc.getInventory().checkItem(7236)) {
				L1ItemInstance item = pc.getInventory().checkEquippedItem(
						7236);
				if (item != null) {
					pc.getInventory()
							.setEquipped(item, false, false, false);
				}
				pc.getInventory().consumeItem(7236,
						pc.getInventory().countItems(7236));
				FireDragon fd = null;
				synchronized (GameList.FDList) {
					fd = GameList.getFD(pc.getMapId());
				}
				if (fd != null) {
					if (fd._pc.getId() == pc.getId()) {
						fd.Reset();
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		pc.getNearObjects().removeAllKnownObjects();
		S_RemoveObject sro = new S_RemoveObject(pc);
		Broadcaster.broadcastPacket(pc, sro, true);
		
		/** 케릭터 터가 인던에서 리스를 했을경우 */
		if(pc.getNetConnection().getInterServer()){
			/** 타입 넘버가 10이라면 인던 이기때문에 파티가 강제 해제 시키도록 */
			if(pc.getNetConnection().getInterServerType() == 10){
				/** 맵 좌표 정리 */
				pc.setLocation(loc[0], loc[1], loc[2]);
				
				/** 파티 제거및 케릭터 정보 리셋 */
				pc.getParty().leaveMember(pc);
				pc.getNetConnection().setInterServerType(99);
				pc.sendPackets(new S_ServerVersion(S_ServerVersion.ServerInterOpen, 99, pc), true);
				return;
			}
		}

		pc.setCurrentHp(pc.getLevel());
		pc.set_food(39); // 죽었을때 겟지? 10%
		pc.setDead(false);
		pc.setActionStatus(0);
		L1World.getInstance().moveVisibleObject(pc, loc[0], loc[1], loc[2]);
		pc.setX(loc[0]);
		pc.setY(loc[1]);
		pc.setMap((short) loc[2]);

		pc.tempx = pc.getX();
		pc.tempy = pc.getY();
		pc.tempm = pc.getMapId();
		pc.temph = pc.getMoveState().getHeading();

		S_MapID smp = new S_MapID(pc.getMapId(), pc.getMap().isUnderwater());
		pc.sendPackets(smp, true);

		for (L1PcInstance pc2 : L1World.getInstance().getVisiblePlayer(pc)) {
			pc2.sendPackets(new S_OtherCharPacks(pc, pc2));
		}

		S_OwnCharPack ocp2 = new S_OwnCharPack(pc);
		pc.sendPackets(ocp2, true);
		S_CharVisualUpdate cvu = new S_CharVisualUpdate(pc);
		pc.sendPackets(cvu, true);
		S_Weather sw = new S_Weather(L1World.getInstance().getWeather());
		pc.sendPackets(sw, true);
		
		S_ChangeShape cs = new S_ChangeShape(pc.getId(), pc.getClassId());
		pc.sendPackets(cs);
		//System.out.println("44343");
		Broadcaster.broadcastPacket(pc, cs, true);
		L1ItemInstance weapon = pc.getWeapon();
		if (weapon != null) {
			S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc);
			pc.sendPackets(charVisual);
			Broadcaster.broadcastPacket(pc, charVisual);
			pc.sendPackets(new S_PacketBox(S_PacketBox.WEAPON_RANGE, pc,weapon, true), true);
		} else {
			pc.sendPackets(new S_PacketBox(S_PacketBox.WEAPON_RANGE, pc,weapon, false), true);
		}
		
		if (pc.getHellTime() > 0) {
			pc.beginHell(false);
		}
		L1ItemInstance item = pc.getInventory().checkEquippedItem(20344);
		if (item != null) { //
			if (item.isEquipped()) {
				pc.getInventory().setEquipped(item, false);
			}
		}
		loc = null;
	}
	
	@Override
	public String getType() {
		return C_RESTART;
	}
}