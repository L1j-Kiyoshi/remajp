package server.threads.pc;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import server.GameServer;

public class SkillReiterationThread implements Runnable {

	private static SkillReiterationThread _instance;

	// private static Logger _log =
	// Logger.getLogger(SabuDGTime.class.getName());

	public static SkillReiterationThread getInstance() {
		if (_instance == null) {
			_instance = new SkillReiterationThread();
		}
		return _instance;
	}

	public SkillReiterationThread() {
		// super("server.threads.pc.SabuDGTime");
		GeneralThreadPool.getInstance().schedule(this, 500);
	}

	@Override
	public void run() {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null) {
					continue;
				}
				if (pc instanceof L1RobotInstance) {
					continue;
				}
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.크레이)) {
					if (!(pc.getMapId() >= 1005 && pc.getMapId() <= 1010)) {
						if (pc.크레이) {
							pc.크레이 = false;
							pc.sendPackets(new S_PacketBox(
									S_PacketBox.DRAGON_EME, 45, false));
							pc.getAC().addAc(8);
							pc.getResistance().addMr(-20);
							pc.addMaxHp(-200);
							pc.addMaxMp(-100);
							pc.addHpr(-3);
							pc.addMpr(-3);
							pc.getResistance().addEarth(-30);
							pc.addDmgup(-3);
							pc.addBowDmgup(-3);
							pc.addHitup(-10);
							pc.addBowHitup(-10);
							pc.addWeightReduction(-40);
							pc.sendPackets(
									new S_HPUpdate(pc.getCurrentHp(), pc
											.getMaxHp()), true);
							pc.sendPackets(
									new S_MPUpdate(pc.getCurrentMp(), pc
											.getMaxMp()), true);
							pc.sendPackets(new S_OwnCharAttrDef(pc), true);
							pc.sendPackets(new S_SPMR(pc), true);
						}
					} else {
						if (!pc.크레이) {
							pc.크레이 = true;
							pc.sendPackets(new S_PacketBox(
									S_PacketBox.DRAGON_EME, 45, true));
							pc.getAC().addAc(-8);
							pc.getResistance().addMr(20);
							pc.addMaxHp(200);
							pc.addMaxMp(100);
							pc.addHpr(3);
							pc.addMpr(3);
							pc.getResistance().addEarth(30);
							pc.addDmgup(3);
							pc.addBowDmgup(3);
							pc.addHitup(10);
							pc.addBowHitup(10);
							pc.addWeightReduction(40);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc
									.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc
									.getMaxMp()));
							pc.sendPackets(new S_OwnCharAttrDef(pc), true);
							pc.sendPackets(new S_SPMR(pc), true);
						}
					}
				}

				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.군터의조언)) {
					if (!(pc.getMapId() >= 10000 && pc.getMapId() <= 10005)) {
						if (pc.군터) {
							pc.군터 = false;
							pc.sendPackets(new S_PacketBox(
									S_PacketBox.DRAGON_EME, 74, false));
							pc.addBowDmgup(-5);
							pc.addBowHitup(-7);
							pc.addHpr(-10);
							pc.addMaxHp(-100);
							pc.addMaxMp(-40);
							pc.getResistance().addMr(-15);
							pc.getAbility().addAddedDex((byte) -5);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc
									.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc
									.getMaxMp()));
							pc.sendPackets(new S_OwnCharStatus(pc));
							pc.sendPackets(new S_SPMR(pc), true);
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER,
									pc.get_PlusEr()), true);
						}
					} else {
						if (!pc.군터) {
							pc.군터 = true;
							pc.sendPackets(new S_PacketBox(
									S_PacketBox.DRAGON_EME, 74, true));
							pc.addBowDmgup(5);
							pc.addBowHitup(7);
							pc.addHpr(10);
							pc.addMaxHp(100);
							pc.addMaxMp(40);
							pc.getResistance().addMr(15);
							pc.getAbility().addAddedDex((byte) 5);
							pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc
									.getMaxHp()));
							pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc
									.getMaxMp()));
							pc.sendPackets(new S_OwnCharStatus(pc));
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER,
									pc.get_PlusEr()), true);
							pc.sendPackets(new S_SPMR(pc), true);
						}
					}
				}

				pc.버그체크시간++;
				if (pc.버그체크시간 > 120) {
					// if(!GameServer.getInstance().checkbug(pc.getName())){
					// pc.아덴체크();
					// }
					if (!GameServer.getInstance().checkac(pc.getName())) {
					//	pc.방어체크();
						pc.스탯체크();
					}
					pc.버그체크시간 = 0;
				}
				// 단테스 버프
				if (pc.getX() >= 32769 && pc.getX() <= 32881
						&& pc.getY() >= 32741 && pc.getY() <= 32840
						&& pc.getMapId() == 479) {
					if (!pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.단테스버프)) {
						pc.addDmgup(2);
						pc.addBowDmgup(2);
						pc.getAbility().addSp(2);
						pc.sendPackets(new S_SPMR(pc), true);
					}
					pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.단테스버프,
							60 * 1000);
				} else {
					if (pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.단테스버프)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(
								L1SkillId.단테스버프);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, 500);
	}

}
