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
package l1j.server.server.model;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_NPCPack;

public class L1UbSpawn implements Comparable<L1UbSpawn> {
	private int _id;
	private int _ubId;
	private int _pattern;
	private int _group;
	private int _npcTemplateId;
	private int _amount;
	private int _spawnDelay;
	private int _sealCount;
	private String _name;

	// --------------------start getter/setter--------------------
	public int getId() {
		return _id;
	}

	public void setId(int id) {
		_id = id;
	}

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int ubId) {
		_ubId = ubId;
	}

	public int getPattern() {
		return _pattern;
	}

	public void setPattern(int pattern) {
		_pattern = pattern;
	}

	public int getGroup() {
		return _group;
	}

	public void setGroup(int group) {
		_group = group;
	}

	public int getNpcTemplateId() {
		return _npcTemplateId;
	}

	public void setNpcTemplateId(int npcTemplateId) {
		_npcTemplateId = npcTemplateId;
	}

	public int getAmount() {
		return _amount;
	}

	public void setAmount(int amount) {
		_amount = amount;
	}

	public int getSpawnDelay() {
		return _spawnDelay;
	}

	public void setSpawnDelay(int spawnDelay) {
		_spawnDelay = spawnDelay;
	}

	public int getSealCount() {
		return _sealCount;
	}

	public void setSealCount(int i) {
		_sealCount = i;
	}

	public String getName() {
		return _name;
	}

	public void setName(String name) {
		_name = name;
	}

	// --------------------end getter/setter--------------------

	public void spawnOne() {
		L1UltimateBattle ub = UBTable.getInstance().getUb(_ubId);
		L1Location loc = ub.getLocation().randomLocation(
				(ub.getLocX2() - ub.getLocX1()) / 2, false);
		L1MonsterInstance mob = new L1MonsterInstance(NpcTable.getInstance()
				.getTemplate(getNpcTemplateId()));

		mob.setId(ObjectIdFactory.getInstance().nextId());
		mob.getMoveState().setHeading(5);
		mob.setX(loc.getX());
		mob.setHomeX(loc.getX());
		mob.setY(loc.getY());
		mob.setHomeY(loc.getY());
		mob.setMap((short) loc.getMapId());
		mob.set_storeDroped((byte) ((3 < getGroup()) ? 1 : 0));
		mob.setUbSealCount(getSealCount());
		mob.setUbId(getUbId());

		L1World.getInstance().storeObject(mob);
		L1World.getInstance().addVisibleObject(mob);

		S_NPCPack s_npcPack = new S_NPCPack(mob);
		ArrayList<L1PcInstance> ll = L1World.getInstance().getRecognizePlayer(
				mob);
		if (ll.size() > 0) {
			for (L1PcInstance pc : ll) {
				pc.getNearObjects().addKnownObject(mob);
				mob.getNearObjects().addKnownObject(pc);
				pc.sendPackets(s_npcPack);
			}
			Random rnd = new Random(System.nanoTime());
			mob.setTarget(ll.get(rnd.nextInt(ll.size())));
			rnd = null;
		}
		
		if (getNpcTemplateId() == 910117 || getNpcTemplateId() == 910021
				|| getNpcTemplateId() == 910028 || getNpcTemplateId() == 910036 || getNpcTemplateId() == 910042 || getNpcTemplateId() == 910050
				|| getNpcTemplateId() == 910056 || getNpcTemplateId() == 910062 || getNpcTemplateId() == 910069|| getNpcTemplateId() == 910075
				|| getNpcTemplateId() == 910014 || getNpcTemplateId() == 910103 || getNpcTemplateId() == 910104  || getNpcTemplateId() == 45516
				|| getNpcTemplateId() == 45617 || getNpcTemplateId() == 100339) {
			for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(mob)) {
				mob.onPerceive(visiblePc);
				visiblePc.sendPackets(new S_NPCPack(mob), true);
				visiblePc.sendPackets(new S_DoActionGFX(mob.getId(), 4));
			}
		}

		mob.onNpcAI();
		mob.getLight().turnOnOffLight();
		// mob.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);

	}

	public void spawnAll() {
		for (int i = 0; i < getAmount(); i++) {
			spawnOne();
		}
	}

	public int compareTo(L1UbSpawn rhs) {
		if (getId() < rhs.getId()) {
			return -1;
		}
		if (getId() > rhs.getId()) {
			return 1;
		}
		return 0;
	}

	// private static final Logger _log =
	// Logger.getLogger(L1UbSpawn.class.getName());
}
