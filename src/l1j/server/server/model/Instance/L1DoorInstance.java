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
package l1j.server.server.model.Instance;

import l1j.server.GameSystem.Astar.World;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Door;
import l1j.server.server.serverpackets.S_DoorPack;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.templates.L1Npc;

public class L1DoorInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;
	public static final int PASS = 0;
	public static final int NOT_PASS = 1;

	private int _doorId = 0;
	private int _direction = 0;
	private int _leftEdgeLocation = 0;
	private int _rightEdgeLocation = 0;
	private int _openStatus = ActionCodes.ACTION_Close;
	private int _passable = NOT_PASS;
	private int _keeperId = 0;
	private int _autostatus = 0;

	public L1DoorInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance pc) {
		if (getNpcTemplate().get_npcId() == 100852
				|| getNpcTemplate().get_npcId() == 100853
				|| getNpcTemplate().get_npcId() == 100854
				|| getNpcTemplate().get_npcId() == 100855) {
			return;
		}

		if (getMaxHp() == 0 || getMaxHp() == 1) {
			return;
		}
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(pc, this);
			if (attack.calcHit()) {
				attack.calcDamage();
				attack.addPcPoisonAttack(pc, this);
			}
			attack.action();
			attack.commit();
			castledoorAction(pc);
			attack = null;
		}
	}

	@Override
	public void onAction(L1PcInstance pc, int adddmg) {
		if (getNpcTemplate().get_npcId() == 100852
				|| getNpcTemplate().get_npcId() == 100853
				|| getNpcTemplate().get_npcId() == 100854
				|| getNpcTemplate().get_npcId() == 100855) {
			return;
		}

		if (getMaxHp() == 0 || getMaxHp() == 1) {
			return;
		}
		if (getCurrentHp() > 0 && !isDead()) {
			L1Attack attack = new L1Attack(pc, this);
			if (attack.calcHit()) {
				attack.calcDamage(adddmg);
				attack.addPcPoisonAttack(pc, this);
			}
			attack.action();
			attack.commit();
			castledoorAction(pc);
			attack = null;
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.getNearObjects().addKnownObject(this);
		/** 하딘 인던 관련 보스방 후문 ObjectCreate 패킷 **/
		/*
		 * if(getNpcTemplate().get_npcId() == 5000091){
		 * perceivedFrom.sendPackets(new S_NPCPack(this), true); }else{
		 */
		perceivedFrom.sendPackets(new S_DoorPack(this), true);
		// }
		sendDoorPacket(perceivedFrom);
	}

	@Override
	public void deleteMe() {
		setPassable(PASS);
		sendDoorPacket(null);

		_destroyed = true;
		if (getInventory() != null) {
			getInventory().clearItems();
		}
		allTargetClear();
		_master = null;
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		Broadcaster.broadcastPacket(this, new S_RemoveObject(this));
		/*
		 * S_RemoveObject so = new S_RemoveObject(this); for (L1PcInstance pc :
		 * L1World.getInstance().getRecognizePlayer(this)) {
		 * pc.getNearObjects().removeKnownObject(this); pc.sendPackets(so); }
		 */
		getNearObjects().removeAllKnownObjects();
	}

	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (getMaxHp() == 0 || getMaxHp() == 1) {
			return;
		}

		if (getCurrentHp() > 0 && !isDead()) {
			int newHp = getCurrentHp() - damage;
			if (newHp <= 0 && !isDead()) {
				setCurrentHp(0);
				setDead(true);
				setActionStatus(ActionCodes.ACTION_DoorDie);
				Death death = new Death(attacker);
				GeneralThreadPool.getInstance().execute(death);
			}
			if (newHp > 0) {
				setCurrentHp(newHp);
				/*
				 * if ((getMaxHp() * 1 / 6) > getCurrentHp()) { if (_crackStatus
				 * != 5) { S_DoActionGFX da = new S_DoActionGFX(getId(),
				 * ActionCodes.ACTION_DoorAction5);
				 * Broadcaster.broadcastPacket(this, da, true);
				 * setActionStatus(ActionCodes.ACTION_DoorAction5); _crackStatus
				 * = 5; } } else if ((getMaxHp() * 2 / 6) > getCurrentHp()) { if
				 * (_crackStatus != 4) { S_DoActionGFX da = new
				 * S_DoActionGFX(getId(), ActionCodes.ACTION_DoorAction4);
				 * Broadcaster.broadcastPacket(this, da, true);
				 * setActionStatus(ActionCodes.ACTION_DoorAction4); _crackStatus
				 * = 4; } } else
				 */if ((getMaxHp() * 1 / 4) > getCurrentHp()) {
					if (_crackStatus != 3) {
						S_DoActionGFX da = new S_DoActionGFX(getId(),
								ActionCodes.ACTION_DoorAction3);
						Broadcaster.broadcastPacket(this, da, true);
						setActionStatus(ActionCodes.ACTION_DoorAction3);
						_crackStatus = 3;
					}
				} else if ((getMaxHp() * 2 / 4) > getCurrentHp()) {
					if (getAutoStatus() == 1) {// 이 상태에서 자동 리페어가 되는지 확실하지는 않다;
						repairGate();
					} else if (_crackStatus != 2) {
						S_DoActionGFX da = new S_DoActionGFX(getId(),
								ActionCodes.ACTION_DoorAction2);
						Broadcaster.broadcastPacket(this, da, true);
						setActionStatus(ActionCodes.ACTION_DoorAction2);
						_crackStatus = 2;
					}
				} else if ((getMaxHp() * 3 / 4) > getCurrentHp()) {
					if (_crackStatus != 1) {
						S_DoActionGFX da = new S_DoActionGFX(getId(),
								ActionCodes.ACTION_DoorAction1);
						Broadcaster.broadcastPacket(this, da, true);
						setActionStatus(ActionCodes.ACTION_DoorAction1);
						_crackStatus = 1;
					}
				}
			}
		} else if (!isDead()) {
			setDead(true);
			setActionStatus(ActionCodes.ACTION_DoorDie);
			Death death = new Death(attacker);
			GeneralThreadPool.getInstance().execute(death);
		}
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
	}

	class Death implements Runnable {
		L1Character _lastAttacker;

		public Death(L1Character lastAttacker) {
			_lastAttacker = lastAttacker;
		}

		@Override
		public void run() {
			try {
				setCurrentHp(0);
				setDead(true);
				isPassibleDoor(true);
				setActionStatus(ActionCodes.ACTION_DoorDie);
				getMap().setPassable(getLocation(), true);
				S_DoActionGFX da = new S_DoActionGFX(getId(),
						ActionCodes.ACTION_DoorDie);
				Broadcaster.broadcastPacket(L1DoorInstance.this, da, true);
				setPassable(PASS);
				sendDoorPacket(null);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class DoorTimer implements Runnable {
		@Override
		public void run() {
			try {
				if (_destroyed) { // 이미 파기되어 있지 않은가 체크
					return;
				}
				close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void sendDoorPacket(L1PcInstance pc) {
		int entranceX = getEntranceX();
		int entranceY = getEntranceY();
		int leftEdgeLocation = getLeftEdgeLocation();
		int rightEdgeLocation = getRightEdgeLocation();

		int size = rightEdgeLocation - leftEdgeLocation;
		if (size == 0) {
			if (getDoorId() == 8050 || getDoorId() == 8051
					|| getDoorId() == 8054 || getDoorId() == 8055) {
				for (int y = entranceY - 1; y <= entranceY; y++) {
					sendPacket(pc, entranceX, y);
				}
			} else if (getDoorId() == 8052 || getDoorId() == 8053) {
				for (int x = entranceX + 1; x >= entranceX; x--) {
					sendPacket(pc, x, entranceY);
				}
			} else
				sendPacket(pc, entranceX, entranceY);
		} else {
			if (getDirection() == 0) {
				if (getDoorId() == 2031 || getDoorId() == 2033) {
					for (int y = entranceY - 1; y <= entranceY; y++) {
						for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
							sendPacket(pc, x, y);
						}
					}
				} else if (getDoorId() == 2010) {
					for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
						sendPacket(pc, x, entranceY - 1);
					}
				} else {
					for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
						sendPacket(pc, x, entranceY);
					}
				}
			} else {
				if (getDoorId() == 2001) {
					for (int x = entranceX + 1; x >= entranceX; x--) {
						for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
							sendPacket(pc, x, y);
						}
					}
				} else {
					for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
						sendPacket(pc, entranceX, y);
					}
				}
			}
		}
	}

	private void sendPacket(L1PcInstance pc, int x, int y) {
		/*
		 * if(getPassable() == 0){ System.out.println("문열림"); } if(getPassable()
		 * == 1){ System.out.println("문닫힘"); }
		 */
		S_Door packet = new S_Door(x, y, getDirection(), getPassable());
		if (pc != null) {
			if (pc.getMapId() >= 451 && pc.getMapId() <= 536
					&& pc.getMapId() != 480 && pc.getMapId() != 481
					&& pc.getMapId() != 482 && pc.getMapId() != 483
					&& pc.getMapId() != 484 && pc.getMapId() != 521
					&& pc.getMapId() != 522 && pc.getMapId() != 523
					&& pc.getMapId() != 524) { // 상아탑
				pc.sendPackets(packet);
			} else {
				/** 닫혀있을때만 패킷을 보내는 이유는??????????? by 케인 **/
				// if (getOpenStatus() == ActionCodes.ACTION_Close) {
				pc.sendPackets(packet);
				// }
			}
		} else {
			Broadcaster.broadcastPacket(this, packet);
		}
	}

	// sabusabusabu private Random _rnd = new Random(System.nanoTime());
	// private int potion[] = {};
	public void open() {
		if (isDead()) {
			return;
		}
		if (getOpenStatus() == ActionCodes.ACTION_Close) {
			isPassibleDoor(true);
			if (this.getDoorId() == 113 || this.getDoorId() == 125) {
				GeneralThreadPool.getInstance().schedule(new DoorTimer(), 5000);
			}
			if (this.getDoorId() >= 8011 && this.getDoorId() <= 8022) {
				GeneralThreadPool.getInstance()
						.schedule(new DoorTimer(), 10000);
			}
			if (this.getDoorId() >= 7100 && this.getDoorId() <= 7160) {
				GeneralThreadPool.getInstance().schedule(new DoorTimer(),
						600000);
			}
			if (this.getDoorId() >= 8001 && this.getDoorId() <= 8010) {
				GeneralThreadPool.getInstance().schedule(new DoorTimer(),
						1800000);
			}
			S_DoActionGFX da = new S_DoActionGFX(this.getId(),
					ActionCodes.ACTION_Open);
			Broadcaster.broadcastPacket(this, da, true);
			setOpenStatus(ActionCodes.ACTION_Open);
			setPassable(L1DoorInstance.PASS);
			sendDoorPacket(null);
		}
	}

	public void close() {
		if (isDead()) {
			return;
		}
		if (getNpcTemplate().get_npcId() == 100852
				|| getNpcTemplate().get_npcId() == 100853
				|| getNpcTemplate().get_npcId() == 100854
				|| getNpcTemplate().get_npcId() == 100855) {
			return;
		}
		if (getOpenStatus() == ActionCodes.ACTION_Open) {
			isPassibleDoor(false);
			S_DoActionGFX da = new S_DoActionGFX(this.getId(),
					ActionCodes.ACTION_Close);
			Broadcaster.broadcastPacket(this, da, true);
			setOpenStatus(ActionCodes.ACTION_Close);
			setPassable(L1DoorInstance.NOT_PASS);
			sendDoorPacket(null);
		}
	}

	public void isPassibleDoor(boolean flag) {
		int entranceX = getEntranceX();
		int entranceY = getEntranceY();
		int leftEdgeLocation = this.getLeftEdgeLocation();
		int rightEdgeLocation = this.getRightEdgeLocation();
		int size = rightEdgeLocation - leftEdgeLocation;
		if (size >= 0) {
			if (getDirection() == 0 || getDirection() == 4) {
				if (getDoorId() == 2010) {
					for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
						World.문이동(x, entranceY - 1, this.getMapId(), false,
								flag);
					}
				} else {
					for (int x = leftEdgeLocation; x <= rightEdgeLocation; x++) {
						World.문이동(x, entranceY, this.getMapId(), false, flag);
					}
				}
			} else {
				for (int y = leftEdgeLocation; y <= rightEdgeLocation; y++) {
					World.문이동(entranceX, y, this.getMapId(), true, flag);
				}
			}
		}
	}

	/*
	 * public void isPassibleDoor(boolean flag) { int leftEdgeLocation =
	 * this.getLeftEdgeLocation(); int rightEdgeLocation =
	 * this.getRightEdgeLocation(); int size = rightEdgeLocation -
	 * leftEdgeLocation; if(flag){ flag = false; }else{ flag=true; } if (size >=
	 * 0) { if (this.getDirection() == 0) { for (int doorX = leftEdgeLocation;
	 * doorX <= rightEdgeLocation; doorX++) {
	 * 
	 * World.문이동(doorX, this.getY(), this.getMapId(), 3, flag); World.문이동(doorX,
	 * this.getY(), this.getMapId(), 4, flag); World.문이동(doorX, this.getY(),
	 * this.getMapId(), 5, flag);
	 * 
	 * World.문이동(doorX, this.getY()+1, this.getMapId(), 7, flag);
	 * World.문이동(doorX, this.getY()+1, this.getMapId(), 0, flag);
	 * World.문이동(doorX, this.getY()+1, this.getMapId(), 1, flag);
	 * 
	 * World.문이동(doorX-1, this.getY(), this.getMapId(), 3, flag);
	 * World.문이동(doorX-1, this.getY()+1, this.getMapId(), 1, flag);
	 * 
	 * World.문이동(doorX+1, this.getY(), this.getMapId(), 5, flag);
	 * World.문이동(doorX+1, this.getY()+1, this.getMapId(), 7, flag); } } else {
	 * for (int doorY = leftEdgeLocation; doorY <= rightEdgeLocation; doorY++) {
	 * World.문이동(this.getX(), doorY, this.getMapId(), 5, flag);
	 * World.문이동(this.getX(), doorY, this.getMapId(), 6, flag);
	 * World.문이동(this.getX(), doorY, this.getMapId(), 7, flag);
	 * World.문이동(this.getX()-1, doorY, this.getMapId(), 1, flag);
	 * World.문이동(this.getX()-1, doorY, this.getMapId(), 2, flag);
	 * World.문이동(this.getX()-1, doorY, this.getMapId(), 3, flag);
	 * 
	 * World.문이동(this.getX(), doorY-1, this.getMapId(), 5, flag);
	 * World.문이동(this.getX()-1, doorY-1, this.getMapId(), 3, flag);
	 * 
	 * World.문이동(this.getX(), doorY+1, this.getMapId(), 7, flag);
	 * World.문이동(this.getX()-1, doorY+1, this.getMapId(), 1, flag); } } } }
	 */

	public void repairGate() {
		if (getMaxHp() > 1) {
			setDead(false);
			setCurrentHp(getMaxHp());
			setActionStatus(0);
			setCrackStatus(0);
			setOpenStatus(ActionCodes.ACTION_Open);
			close();
		}
	}

	private void castledoorAction(L1PcInstance pc) {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 13)) {
			if (obj instanceof L1CastleGuardInstance) {
				L1CastleGuardInstance guard = (L1CastleGuardInstance) obj;
				guard.setTarget(pc);
			}
		}
	}

	public int getDoorId() {
		return _doorId;
	}

	public void setDoorId(int i) {
		_doorId = i;
	}

	public int getDirection() {
		return _direction;
	}

	public void setDirection(int i) {
		if (i == 0 || i == 1 || i == 4) {
			_direction = i;
		}
	}

	public int getEntranceX() {
		int entranceX = 0;
		if (getDirection() == 0 || getDirection() == 4) {
			entranceX = getX();
		} else {
			entranceX = getX() - 1;
		}
		return entranceX;
	}

	public int getEntranceY() {
		int entranceY = 0;
		if (getDirection() == 0) {
			entranceY = getY() + 1;
		} else if (getDirection() == 4) {
			entranceY = getY() - 1;
		} else {
			entranceY = getY();
		}
		return entranceY;
	}

	public int getLeftEdgeLocation() {
		return _leftEdgeLocation;
	}

	public void setLeftEdgeLocation(int i) {
		_leftEdgeLocation = i;
	}

	public int getRightEdgeLocation() {
		return _rightEdgeLocation;
	}

	public void setRightEdgeLocation(int i) {
		_rightEdgeLocation = i;
	}

	public int getOpenStatus() {
		return _openStatus;
	}

	public void setOpenStatus(int i) {
		if (i == ActionCodes.ACTION_Open || i == ActionCodes.ACTION_Close) {
			_openStatus = i;
		}
	}

	public int getPassable() {
		return _passable;
	}

	public void setPassable(int i) {
		if (i == PASS || i == NOT_PASS) {
			_passable = i;
		}
	}

	public int getKeeperId() {
		return _keeperId;
	}

	public void setKeeperId(int i) {
		_keeperId = i;
	}

	private int _crackStatus;

	public int getCrackStatus() {
		return _crackStatus;
	}

	public void setCrackStatus(int i) {
		_crackStatus = i;
	}

	public int getAutoStatus() {
		return _autostatus;
	}

	public void setAutoStatus(int i) {
		_autostatus = i;
	}

}