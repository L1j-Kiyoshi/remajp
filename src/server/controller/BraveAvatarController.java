package server.controller;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;

public class BraveAvatarController implements Runnable {

	private static BraveAvatarController _instance;

	// private static Logger _log =
	// Logger.getLogger(SabuDGTime.class.getName());

	public static BraveAvatarController getInstance() {
		if (_instance == null) {
			_instance = new BraveAvatarController();
		}
		return _instance;
	}

	public BraveAvatarController() {
		// super("server.threads.pc.SabuDGTime");
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

	@Override
	public void run() {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null) {
					continue;
				}
				
				if (pc.getParty() != null && pc.getParty().getLeader().isCrown() && pc.getParty().getLeader().isSkillMastery(121) && pc.getParty().getLeader().getLocation().getTileLineDistance(pc.getLocation()) <= 18) {
					if (pc.getParty().getNumOfMembers() >= 1) {
						if (!pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.BRAVE_AVATAR)) {
							pc.getAbility().addAddedStr((byte) 1);
							pc.getAbility().addAddedDex((byte) 1);
							pc.getAbility().addAddedInt((byte) 1);
							pc.getResistance().addMr(10);
							pc.addDragonLangTolerance(2);
							pc.addSpiritTolerance(2);
							pc.addTechniqueTolerance(2);
							pc.addFearTolerance(2);
							pc.sendPackets(new S_SPMR(pc), true);				// SP, MR 변경 치 전송
							pc.sendPackets(new S_OwnCharStatus(pc), true);		// 최신 캐릭터 전체 상태 정보 전송
							pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 479), true);
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						}
						
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.BRAVE_AVATAR, 30 * 1000);
					}
				} else if (pc.getParty() == null && pc.isCrown() && pc.isSkillMastery(121)) {
					// 클래스 케어 군주 아우라 스킬 적용 by white
					if (!pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.BRAVE_AVATAR)) {
						pc.getAbility().addAddedStr((byte) 1);
						pc.getAbility().addAddedDex((byte) 1);
						pc.getAbility().addAddedInt((byte) 1);
						pc.getResistance().addMr(10);
						pc.addDragonLangTolerance(2);
						pc.addSpiritTolerance(2);
						pc.addTechniqueTolerance(2);
						pc.addFearTolerance(2);
						pc.sendPackets(new S_SPMR(pc), true);				// SP, MR 변경 치 전송
						pc.sendPackets(new S_OwnCharStatus(pc), true);		// 최신 캐릭터 전체 상태 정보 전송
						pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1, 479), true);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					}
					
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.BRAVE_AVATAR, 30 * 1000);
				} else {
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.BRAVE_AVATAR)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.BRAVE_AVATAR);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		GeneralThreadPool.getInstance().schedule(this, 1000);
	}

}
