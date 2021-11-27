package l1j.server.server.model.skill.skills;

import java.util.List;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1BookMark;

public class MassTeleport {

	public static void runSkill(L1Character cha, int _bookmarkId) {
		L1PcInstance pc = (L1PcInstance) cha;
		L1BookMark bookm = pc.getBookMark(_bookmarkId);
		if (bookm != null) {
			if (pc.getMap().isEscapable() || pc.isGm()) {
				int newX = bookm.getLocX();
				int newY = bookm.getLocY();
				short mapId = bookm.getMapId();

				List<L1PcInstance> clanMember = L1World.getInstance()
						.getVisiblePlayer(pc);
				for (L1PcInstance member : clanMember) {
					if (pc.getLocation().getTileLineDistance(
							member.getLocation()) <= 3
							&& member.getClanid() == pc.getClanid()
							&& pc.getClanid() != 0
							&& member.getId() != pc.getId()
							&& !member.isPrivateShop()) {
						L1Teleport.teleport(member, newX, newY, mapId, member
								.getMoveState().getHeading(), true);
					}
				}
				L1Teleport.teleport(pc, newX, newY, mapId, pc.getMoveState()
						.getHeading(), true);
			} else {
				pc.sendPackets(new S_Paralysis(
						S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."));
			}
		} else {
			long time = GameTimeClock.getInstance().getGameTime().getSeconds() % 86400;
			int 정각6 = 7*3600;
			int 정각18 = 19*3600;
			if ((pc.getMap().isTeleportable(pc.getX(), pc.getY()) && pc.getMap().isTeleportable()) || pc.isGm()
					|| ((pc.getMapId() == 101 || pc.getMapId() == 102 || pc.getMapId() == 103 || pc.getMapId() == 104 
					|| pc.getMapId() == 105 || pc.getMapId() == 106 || pc.getMapId() == 107 || pc.getMapId() == 108 || pc.getMapId() == 109 
					|| pc.getMapId() == 110 || pc.getMapId() == 12852 || pc.getMapId() == 12853 || pc.getMapId() == 12854 || pc.getMapId() == 12855
					|| pc.getMapId() == 12856 || pc.getMapId() == 12857 || pc.getMapId() == 12858 || pc.getMapId() == 12859 || pc.getMapId() == 12860
					|| pc.getMapId() == 12861) && pc.is오만텔())
					|| ((pc.getMapId() == 15410 || pc.getMapId() == 15420 || pc.getMapId() == 15430 || pc.getMapId() == 15440) && pc.is지배이반텔())
					|| (!GameTimeClock.getInstance().getGameTime().isNight() &&  pc.getMapId() == 54)) {
				L1Location newLocation = pc.getLocation().randomLocation(200,
						true);
				int newX = newLocation.getX();
				int newY = newLocation.getY();
				short mapId = (short) newLocation.getMapId();

				List<L1PcInstance> clanMember = L1World.getInstance()
						.getVisiblePlayer(pc);
				for (L1PcInstance member : clanMember) {
					if (pc.getLocation().getTileLineDistance(
							member.getLocation()) <= 3
							&& member.getClanid() == pc.getClanid()
							&& pc.getClanid() != 0
							&& member.getId() != pc.getId()
							&& !pc.isPrivateShop()) {
						L1Teleport.teleport(member, newX, newY, mapId, member
								.getMoveState().getHeading(), true);
					}
				}
				L1Teleport.teleport(pc, newX, newY, mapId, pc.getMoveState()
						.getHeading(), true);
			} else {
				pc.sendPackets(new S_Paralysis(
						S_Paralysis.TYPE_TELEPORT_UNLOCK, false));
				pc.sendPackets(new S_ServerMessage(276));
			}
		}
	}

}
