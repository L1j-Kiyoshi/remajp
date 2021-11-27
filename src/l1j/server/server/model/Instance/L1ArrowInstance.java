package l1j.server.server.model.Instance;

import java.util.ArrayList;

import l1j.server.server.ActionCodes;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import l1j.server.server.templates.L1Npc;

public class L1ArrowInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public L1ArrowInstance(L1Npc template) {
		super(template);
	}

	private int _targetX = 0;
	private int _targetY = 0;
	private boolean _action = false;

	public void setTarX(int x) {
		_targetX = x;
	}

	public int getTarX() {
		return _targetX;
	}

	public void setTarY(int y) {
		_targetY = y;
	}

	public int getTarY() {
		return _targetY;
	}

	public boolean getAction() {
		return _action;
	}

	public void setAction(boolean a) {
		_action = a;
	}

	public void ai() {
		L1PcInstance target = null;
		int start = 0, end = 0;
		boolean ck = false;
		// List<L1PcInstance> list = getNearObjects().getKnownPlayers();
		// for(L1PcInstance pc : list){
		ArrayList<L1PcInstance> list = L1World.getInstance().getVisiblePlayer(
				this);
		for (L1PcInstance pc : list) {
			if (pc == null)
				continue;
			if (getX() == getTarX()) {
				if (getY() < getTarY()) {
					start = getY();
					end = getTarY();
				} else {
					start = getTarY();
					end = getY();
				}
			} else {
				if (getX() < getTarX()) {
					start = getX();
					end = getTarX();
				} else {
					start = getTarX();
					end = getX();
				}
			}
			if (getX() == pc.getX() && getY() == pc.getY()) {
				ck = true;
				target = pc;
				break;
			}
			for (int i = start; i < end; i++) {
				if ((pc.getX() == getX() && pc.getY() == i)
						|| (pc.getX() == i && pc.getY() == getY())) {
					ck = true;
					target = pc;
					break;
				}
			}
			if (ck)
				break;
		}
		if (list.size() > 0) {
			if (!ck) { // È­»ì GFX 66
				Broadcaster.broadcastPacket(this, new S_UseArrowSkill(this, 0,
						66, getTarX(), getTarY(), false), true);
			} else {
				Broadcaster.broadcastPacket(
						this,
						new S_UseArrowSkill(this, target.getId(), 66, target
								.getX(), target.getY(), true), true);
				Broadcaster.broadcastPacketExceptTargetSight(target,
						new S_DoActionGFX(target.getId(),
								ActionCodes.ACTION_Damage), this, true);
				target.setCurrentHp(target.getCurrentHp() - 30); // º»¼·Àº Dmg 5
			}
		}
	}

	@Override
	public void deleteMe() {
		_destroyed = true;
		allTargetClear();
		_master = null;
		L1World.getInstance().removeVisibleObject(this);
		L1World.getInstance().removeObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.getNearObjects().removeKnownObject(this);
			pc.sendPackets(new S_RemoveObject(this), true);
		}
		getNearObjects().removeAllKnownObjects();
	}

}
