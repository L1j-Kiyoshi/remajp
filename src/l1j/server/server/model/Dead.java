package l1j.server.server.model;

import java.util.Random;

import l1j.server.GameSystem.CrockSystem;
import l1j.server.server.GiftBoxController;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;

public class Dead implements Runnable {
	private L1Character lastAttacker;
	private L1MonsterInstance mob;
	Random random = new Random(System.currentTimeMillis());

	public Dead(L1MonsterInstance mob, L1Character attacker) {
		setAttacker(attacker);
		this.mob = mob;
	}

	protected L1Character getAttacker() {
		return lastAttacker;
	}

	protected L1MonsterInstance getDeadMob() {
		return mob;
	}

	public void setAttacker(L1Character a) {
		lastAttacker = a;
	}

	@Override
	public void run() {
		  try {
		   // mob.die(lastAttacker);
		   doSomething();
		   calcDamageInCrystalCave();
		   openAntDoor();
		   GiftBox();
		      calcCombo(this.lastAttacker);
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		 }
	//콤보
	 private void calcCombo(L1Character lastAttacker) {
			int chance = random.nextInt(100)+1;    
			
			if ((lastAttacker instanceof L1PcInstance)) {
			      L1PcInstance pc = (L1PcInstance)lastAttacker;
			      if (!pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.COMBO_BUFF)) {
			        if (pc.getAinHasad() > 10000) {
			        	if(chance < 5){
			        		pc.setComboCount(1);
			        		pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.COMBO_BUFF, 50000);
			        		pc.sendPackets(new S_PacketBox(204, pc.getComboCount()));
			        	}
			         }
			      } else {
			    	  if (pc.getComboCount() < 30) {
			    		  pc.setComboCount(pc.getComboCount() + 1);
			    		  pc.sendPackets(new S_PacketBox(204, pc.getComboCount()));
			      } else {
			    	  	  pc.sendPackets(new S_PacketBox(204, 31));
			      }
			    }
			  }
			}

	public void doSomething() {
		if (mob.getNpcTemplate().get_npcId() == 400016
				|| mob.getNpcTemplate().get_npcId() == 400017// 테베
		) {// 티칼
			int dieCount = CrockSystem.getInstance().dieCount();
			if (!CrockSystem.getInstance().isBossTime())
				return;
			switch (dieCount) {
			// 2명의 보스중 한명도 죽이지 않았을때 둘중 하나를 죽였다면 +1
			case 0:
				CrockSystem.getInstance().dieCount(1);
				break;
			// 2명의 보스중 이미 한명이 죽였고. 이제 또한명이 죽으니 2
			case 1:
				CrockSystem.getInstance().dieCount(2);
				break;
			}
		} else if (mob.getNpcTemplate().get_npcId() == 4036016
				|| mob.getNpcTemplate().get_npcId() == 4036017) {
			if (!CrockSystem.getInstance().isBossTime())
				return;
			int partynpcid = 0;
			if (mob.getNpcTemplate().get_npcId() == 4036016) {
				partynpcid = 4036017;
			} else {
				partynpcid = 4036016;
			}
			if (!TKmonCK(mob.getMapId(), partynpcid)) {
				CrockSystem.getInstance().dieCount(2);
			}
		}  
	}

	private boolean TKmonCK(int mapid, int partynpcid) {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(mapid)
				.values()) {
			if (obj != null && obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getNpcTemplate().get_npcId() == partynpcid
						&& !mon.isDead() && !mon._destroyed)
					return true;
			}
		}
		return false;
	}
	
	public void GiftBox() {
		  if (mob.getNpcTemplate().get_npcId() == 60171){
	          if (!GiftBoxController.getInstance().getBox1Die())
	            GiftBoxController.getInstance().setBox1Die(true);
	        }
	        if (mob.getNpcTemplate().get_npcId() == 60172){
	          if (!GiftBoxController.getInstance().getBox2Die())
	            GiftBoxController.getInstance().setBox2Die(true);
	        }
	        if (mob.getNpcTemplate().get_npcId() == 60173){
	          if (!GiftBoxController.getInstance().getBox3Die())
	            GiftBoxController.getInstance().setBox3Die(true);
	        }
	        if (mob.getNpcTemplate().get_npcId() == 60174){
	          if (!GiftBoxController.getInstance().getBox4Die())
	            GiftBoxController.getInstance().setBox4Die(true);
	        }
	        if (mob.getNpcTemplate().get_npcId() == 60175){
	          if (!GiftBoxController.getInstance().getBox5Die())
	            GiftBoxController.getInstance().setBox5Die(true);
	        }
	        if (mob.getNpcTemplate().get_npcId() == 60176){
	          if (!GiftBoxController.getInstance().getBox6Die())
	            GiftBoxController.getInstance().setBox6Die(true);
	        }
	        if (mob.getNpcTemplate().get_npcId() == 60177){
	          if (!GiftBoxController.getInstance().getBox7Die())
	            GiftBoxController.getInstance().setBox7Die(true);
	        }
	        if (mob.getNpcTemplate().get_npcId() == 60179){
		          if (!GiftBoxController.getInstance().getBox8Die())
		            GiftBoxController.getInstance().setBox8Die(true);
		        }
	        if (mob.getNpcTemplate().get_npcId() == 60180){
		          if (!GiftBoxController.getInstance().getBox9Die())
		            GiftBoxController.getInstance().setBox9Die(true);
		        }
	        if (mob.getNpcTemplate().get_npcId() == 60181){
		          if (!GiftBoxController.getInstance().getBox10Die())
		            GiftBoxController.getInstance().setBox10Die(true);
		        }
	        if (mob.getNpcTemplate().get_npcId() == 60182){
		          if (!GiftBoxController.getInstance().getBox11Die())
		            GiftBoxController.getInstance().setBox11Die(true);
		        }
	        if (mob.getNpcTemplate().get_npcId() == 60183){
		          if (!GiftBoxController.getInstance().getBox12Die())
		            GiftBoxController.getInstance().setBox12Die(true);
		        }
	        if (mob.getNpcTemplate().get_npcId() == 60184){
		          if (!GiftBoxController.getInstance().getBox13Die())
		            GiftBoxController.getInstance().setBox13Die(true);
		        }
	        if (mob.getNpcTemplate().get_npcId() == 60185){
		          if (!GiftBoxController.getInstance().getBox14Die())
		            GiftBoxController.getInstance().setBox14Die(true);
		        }
	        if (mob.getNpcTemplate().get_npcId() == 60186){
		          if (!GiftBoxController.getInstance().getBox15Die())
		            GiftBoxController.getInstance().setBox15Die(true);
		        }
	        if (mob.getNpcTemplate().get_npcId() == 60178){
	          if (GiftBoxController.getInstance().getGiftBoxStart() == true)
	            GiftBoxController.getInstance().setGiftBoxStart(false);
	        }
		}

	private void openAntDoor() {
		if (mob.getSpawn() != null) {
			switch (mob.getSpawn().getId()) {
			case 54100001:
				openDoorCave(7200);
				break;
			case 54200001:
				openDoorCave(7300);
				break;
			case 54300001:
				openDoorCave(7510);
				openDoorCave(7511);
				break;
			case 54300002:
				openDoorCave(7520);
				break;
			case 54300003:
				openDoorCave(7530);
				break;
			case 54300004:
				openDoorCave(7540);
				break;
			case 54300005:
				openDoorCave(7550);
				break;
			default:
				break;
			}
		} else {
			return;
		}
	}

	private void calcDamageInCrystalCave() {
		switch (mob.getNpcTemplate().get_npcId()) {
		case 46143:
			openDoorCave(5000);
			break;
		case 46144:
			openDoorCave(5001);
			break;
		case 46145:
			openDoorCave(5002);
			break;
		case 46146:
			openDoorCave(5003);
			break;
		case 46147:
			openDoorCave(5004);
			break;
		case 46148:
			openDoorCave(5005);
			break;
		case 46149:
			openDoorCave(5006);
			break;
		case 46150:
			openDoorCave(5007);
			break;
		case 46151:
			openDoorCave(5008);
			break;
		case 46152:
			openDoorCave(5009);
			break;
		default:
			break;
		}
	}

	private void openDoorCave(int doorId) {
		L1DoorInstance door = null;
		for (L1Object object : L1World.getInstance().getObject()) {
			if (object instanceof L1DoorInstance) {
				door = (L1DoorInstance) object;
				if (door.getDoorId() == doorId) {
					door.open();
				}
			}

		}
	}
}
