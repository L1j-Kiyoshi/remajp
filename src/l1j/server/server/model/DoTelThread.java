package l1j.server.server.model;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.Teleportation;

public class DoTelThread implements Runnable {

	/*
	 * public static final int TELEPORT = 0; public static final int
	 * CHANGE_POSITION = 1; public static final int ADVANCED_MASS_TELEPORT = 2;
	 * public static final int CALL_CLAN = 3; public static final int
	 * DUNGEON_TELEPORT = 4; public static final int NODELAY_TELEPORT = 5;
	 * 
	 * public static final int[] EFFECT_SPR = { 169, 169, 2236, 2281,169 };
	 * public static final int[] EFFECT_TIME = { 280, 440, 440, 1120,280 };
	 */

	private L1PcInstance pc;
	private int x;
	private int y;
	private short mapid;
	private int head;

	public DoTelThread(L1PcInstance _pc, int _x, int _y, short _mapid, int _head) {
		pc = _pc;
		x = _x;
		y = _y;
		mapid = _mapid;
		head = _head;
	}

	@Override
	public void run() {
		try {
			/*
			 * Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
			 * EFFECT_SPR[0]), true); try {
			 * Thread.sleep(EFFECT_TIME[NODELAY_TELEPORT]); } catch (Exception
			 * e) { }
			 */
			pc.setTeleportX(x);
			pc.setTeleportY(y);
			pc.setTeleportMapId(mapid);
			pc.setTeleportHeading(head);
			try {
				Teleportation.doTeleportation(pc);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
