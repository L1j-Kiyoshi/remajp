package server.threads.pc;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_PetWindow;

public class PetHpRegenThread extends Thread {

	private static PetHpRegenThread _instance;
	
	public static PetHpRegenThread getInstance() {
		if (_instance == null) {
			_instance = new PetHpRegenThread();
			_instance.start();
		}
		return _instance;
	}

	public PetHpRegenThread() {
		super("server.threads.pc.PetHpRegenThread");
	}

	public void run() {
		while (true) {
			try {
				/** 1초회 한번 돌게 되어있음 전투중인지 전투중이 아닌지 체크 */
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc instanceof L1RobotInstance) continue;
					if (pc == null || pc.getNetConnection() == null){
						continue;
					} else {
						L1PetInstance Pet = (L1PetInstance) pc.getPet();
						if(pc.isDead() || Pet == null) continue;	
						/** 3초에 한번씩 피회복 */
						if(Pet.getTarget() == null){
							if(Pet.getHpcurPoint() >= 24){
								RegenHp(Pet);
								Pet.setHpcurPoint(0);
								pc.sendPackets(new S_PetWindow(S_PetWindow.PatHp, Pet), true);
							}else Pet.setHpcurPoint(Pet.getHpcurPoint() + 1);
						}else{
							if(Pet.getHpcurPoint() >= 10){
								RegenHp(Pet);
								Pet.setHpcurPoint(0);
								pc.sendPackets(new S_PetWindow(S_PetWindow.PatHp, Pet), true);
							}else Pet.setHpcurPoint(Pet.getHpcurPoint() + 1);
						}
						/** 팻 포인트 잇을때 1분에 한번씩 투기 지급 하도록 세팅 */
						if(Pet.getFightingPoint() >= 60){
							Pet.Fighting(10000);
							Pet.setFightingPoint(0);
						}else Pet.setFightingPoint(Pet.getFightingPoint() + 1);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void RegenHp(L1PetInstance Pet) {
		if (Pet.isDead()) return;
	
		int Bonus = 0;
		/** 지역 체크해서 지역이 회복가능 지역이면 보너스 회복 */
		if (isPlayerInLifeStream(Pet)) {
			Bonus += 10;
		}
	
		/** 각 스킬을 대한 정보를 보관해서 피회복 조금더 빠르게 조절가능하도록 설정 */
		Bonus += Pet.SkillsTable(L1PetInstance.만월의재생);
		Bonus += Pet.SkillsTable(L1PetInstance.미소의재생);
		int BonusHp = Pet.getSurvival() + Pet.getElixirSurvival();
		if(BonusHp >= 20) Bonus += (Bonus -  10) / 10;
		
		/** 임시로 펫의 피의 5프로씩 피회복 되게 세팅 */
		int NewHp = Pet.getCurrentHp();
		NewHp += Bonus + (Pet.getMaxHp() * 1 / 100);
		if (NewHp >= Pet.getMaxHp()) NewHp = Pet.getMaxHp();
		Pet.setCurrentHp(Math.min(NewHp, Pet.getMaxHp()));
	}
	
	/** 지역 체크 */
	private static boolean isPlayerInLifeStream(L1PetInstance Pet) {
		L1EffectInstance effect = null;
		for (L1Object object : Pet.getNearObjects().getKnownObjects()) {
			if (object instanceof L1EffectInstance == false) {
				continue;
			}
			effect = (L1EffectInstance) object;
			if (effect.getNpcId() == 81169 && effect.getLocation().getTileLineDistance(Pet.getLocation()) < 4) {
				return true;
			}
		}
		return false;
	}
}
