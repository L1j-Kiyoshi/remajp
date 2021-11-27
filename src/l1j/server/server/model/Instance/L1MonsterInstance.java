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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.Antaras.AntarasRaid;
import l1j.server.GameSystem.Antaras.AntarasRaidSystem;
import l1j.server.GameSystem.Antaras.AntarasRaidTimer;
import l1j.server.GameSystem.Boss.BossAlive;
import l1j.server.GameSystem.FireDragon.FireDragon;
import l1j.server.GameSystem.FireDragon.FireDragonSpawn;
import l1j.server.GameSystem.Lind.Lind;
import l1j.server.GameSystem.Lind.LindThread;
import l1j.server.GameSystem.NavalWarfare.NavalWarfare;
import l1j.server.GameSystem.NavalWarfare.NavalWarfareController;
import l1j.server.GameSystem.Papoo.PaPooRaid;
import l1j.server.GameSystem.Papoo.PaPooRaidSystem;
import l1j.server.GameSystem.Papoo.PaPooTimer;
import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.MJBookQuestSystem.MonsterBook;
import l1j.server.MJBookQuestSystem.Loader.MonsterBookLoader;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.DragonRaidItemTable;
import l1j.server.server.datatables.DropTable;
import l1j.server.server.datatables.MonsterBookTable;
import l1j.server.server.datatables.NPCTalkDataTable;
import l1j.server.server.datatables.QuestInfoTable;
import l1j.server.server.datatables.QuestInfoTable.QuestItemTemp;
import l1j.server.server.datatables.QuestInfoTable.QuestMonTemp;
import l1j.server.server.datatables.UBTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.Dead;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1HateList;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1NpcDeleteTimer;
import l1j.server.server.model.L1NpcTalkData;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1UltimateBattle;
import l1j.server.server.model.L1World;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_MonsterUi;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_QuestTalkIsland;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1QuestInfo;
import l1j.server.server.utils.CalcExp;
import l1j.server.server.utils.L1SpawnUtil;

public class L1MonsterInstance extends L1NpcInstance {
	private static final String TAG = "L1MonsterInstance";
	
	private static final long serialVersionUID = 1L;
	private static Random _Random = new Random(System.nanoTime());
	public static int[][] _classGfxId = { { 0, 1 }, { 48, 61 }, { 37, 138 },
			{ 734, 1186 }, { 2786, 2796 }, { 6658, 6661 }, { 6671, 6650 } };
	private static Random _random = new Random(System.nanoTime());
	private byte _storeDroped;
	private boolean seeon = false;
	private Dead dead = new Dead(this, null);

	public boolean lind_fly = false;
	public boolean lind_fly2 = false;
	private boolean 샌드웜_숨기 = false;
	private int 레오_도망 = 0;

	public boolean lind_level2_cloud = false;
	public byte 해상전보상미믹단계 = 1;
	public boolean kir_absolute = false;
	public boolean kir_counter_barrier = false;
	public boolean kir_counter_magic = false;
	public boolean kir_poison_barrier = false;

	public static 감시자리퍼시간체크 리퍼시간체크;

	private ArrayList<L1PcInstance> 옛셀로브리스트 = new ArrayList<L1PcInstance>();

	private boolean shellHydra = false;
	public boolean shellManClose = false;

	@Override
	public void onItemUse() {
		if (!isActived() && _target != null) {
			// /////////////////파푸
			if (getNpcTemplate().is_doppel() && _target instanceof L1PcInstance) {
				L1PcInstance targetPc = (L1PcInstance) _target;
				setName(_target.getName());
				setNameId(_target.getName());
				setTitle(_target.getTitle());
				setTempLawful(_target.getLawful());
				getGfxId().setTempCharGfx(targetPc.getClassId());
				getGfxId().setGfxId(targetPc.getClassId());
				setPassispeed(640);
				setAtkspeed(900);
				
				for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
					pc.sendPackets(new S_RemoveObject(this), true);
					pc.getNearObjects().removeKnownObject(this);
					pc.updateObject();
				}
				
			}
		}
		
		if (_target != null) {
			// if (getLevel() <= 45) {
			useItem(USEITEM_HASTE, 40);
			// }
		}
		
		if (getCurrentHp() * 100 / getMaxHp() < 40) {
			useItem(USEITEM_HEAL, 50);
		}
		
	}

	
	public void teleport(int nx, int ny, int dir) {
		isTeleport = true;
		
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.sendPackets(new S_SkillSound(getId(), 169), true);
			pc.sendPackets(new S_RemoveObject(this), true);
			pc.getNearObjects().removeKnownObject(this);
		}
		
		L1World.getInstance().moveVisibleObject(this, nx, ny, this.getMapId());
		setX(nx);
		setY(ny);
		getMoveState().setHeading(dir);
		
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			try {
				pc.getNearObjects().addKnownObject(this);
				pc.sendPackets(new S_NPCPack(this), true);
			} catch (Exception e) {
			}
		}
		
		isTeleport = false;
	}

	
	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.getNearObjects().addKnownObject(this);
		if (0 < getCurrentHp()) {
			if (getHiddenStatus() == HIDDEN_STATUS_SINK) {
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Hide), true);
			} else if (getHiddenStatus() == HIDDEN_STATUS_FLY) {
				perceivedFrom.sendPackets(new S_DoActionGFX(getId(), ActionCodes.ACTION_Moveup), true);
			}
			
			// testonNpcAI();
			onNpcAI();

			/*
			 * if (getMoveState().getBraveSpeed() == 1) {
			 * perceivedFrom.sendPackets(new S_SkillBrave(getId(), 1, 600000));
			 * }
			 */
		}
		
		perceivedFrom.sendPackets(new S_NPCPack(this), true);
		if ((getNpcTemplate().get_npcId() == 4038000 || getNpcTemplate().get_npcId() == 4200010 || getNpcTemplate().get_npcId() == 4200011) && !seeon) {
			Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Hide), true);
			seeon = true;
		}

		if ((getNpcTemplate().get_npcId() == 4039000 || getNpcTemplate().get_npcId() == 4039006 || getNpcTemplate().get_npcId() == 4039007) && !seeon) {
			Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Hide), true);
			seeon = true;
		}

		
		if ((getMapId() >= 1005 && getMapId() <= 1010) || (getMapId() >= 1011 && getMapId() <= 1016)) {
			if (getMap().getOriginalTile(getX(), getY()) == 0) {
				teleport(perceivedFrom.getX(), perceivedFrom.getY(), 0);
			}
		}
		

	}

	@Override
	public void searchTarget() {
		L1PcInstance targetPlayer = null;
		L1MonsterInstance targetMonster = null; // 허수아비패기

		if (getNpcTemplate().get_npcId() >= 100750 && getNpcTemplate().get_npcId() <= 100757) {
			// System.out.println("123");
			for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					
					if (mon.isDead()) {
						continue;
					}
					
					if (mon.getNpcTemplate().get_npcId() == 100749) {
						// System.out.println("555555555555");
						targetMonster = mon;
						break;
					}
				}
			}
		} else {
			ArrayList<L1PcInstance> list = L1World.getInstance().getVisiblePlayer(this);
			if (list.size() > 1) {
				Collections.shuffle(list);
			}
			
			for (L1PcInstance pc : list) {
				if (pc.getCurrentHp() <= 0 || pc.isDead() || pc.isSGm() || pc.isGm() || pc.isMonitor() || pc.isGhost()) {
					continue;
				}

				if (_backtarget != null) {
					
					if (_backtarget instanceof L1PcInstance) {
						L1PcInstance _pc = (L1PcInstance) _backtarget;
						
						if (pc.getName() == _pc.getName()) {
							_backtargetre++;
							
							if (_backtargetre > 3) {
								_backtarget = null;
								_backtargetre = 0;
							}
							
							continue;
						}
					}
				}

				int mapId = getMapId();
				if (mapId == 88 || mapId == 98 || mapId == 92 || mapId == 91   || mapId == 95 || mapId == 5557) {
					if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) {
						targetPlayer = pc;
						break;
					}
				}

				if (getNpcId() == 45600) {
					if (pc.isCrown() || pc.isDarkelf() || pc.getGfxId().getTempCharGfx() != pc.getClassId()) {
						targetPlayer = pc;
						break;
					}
				}

				if ((getNpcTemplate().getKarma() < 0 && pc.getKarmaLevel() >= 1) || (getNpcTemplate().getKarma() > 0 && pc.getKarmaLevel() <= -1)) {
					continue;
				}

				// 버땅 퀘스트의 변신, 각 진영의 monster로부터 선제 공격받지 않는다
				if (pc.getGfxId().getTempCharGfx() == 6034 && getNpcTemplate().getKarma() < 0 
						|| pc.getGfxId().getTempCharGfx() == 6035 && getNpcTemplate().getKarma() > 0 
						|| pc.getGfxId().getTempCharGfx() == 6035 && getNpcTemplate().get_npcId() == 46070
						|| pc.getGfxId().getTempCharGfx() == 6035 && getNpcTemplate().get_npcId() == 46072) {
					continue;
				}

				if (!getNpcTemplate().is_agro()
						&& !getNpcTemplate().is_agrososc()
						&& getNpcTemplate().is_agrogfxid1() < 0
						&& getNpcTemplate().is_agrogfxid2() < 0) {
					// 말섬 경비 카오인식 제외
					if (getNpcId() != 4030000 && getNpcId() != 4030001 && pc.getLawful() < -1000) {
						targetPlayer = pc;
						break;
					}
					
					continue;
				}
				
				// agrocoi = 투망인식 agrososc = 변신인식 agro = 일반 어그로
				if (!pc.isInvisble() || getNpcTemplate().is_agrocoi()) {
					
					if (pc.getSkillEffectTimerSet().hasSkillEffect(67)) {
						
						if (getNpcTemplate().is_agrososc()) {
							targetPlayer = pc;
							break;
						}
						
					} else if (getNpcTemplate().is_agro()) {
						targetPlayer = pc;
						break;
					}

					if (getNpcTemplate().is_agrogfxid1() >= 0 && getNpcTemplate().is_agrogfxid1() <= 4) {
						if (_classGfxId[getNpcTemplate().is_agrogfxid1()][0] == pc.getGfxId().getTempCharGfx() 
								|| _classGfxId[getNpcTemplate().is_agrogfxid1()][1] == pc.getGfxId().getTempCharGfx()) {
							targetPlayer = pc;
							break;
						}
					} else if (pc.getGfxId().getTempCharGfx() == getNpcTemplate().is_agrogfxid1()) {
						targetPlayer = pc;
						break;
					}

					if (getNpcTemplate().is_agrogfxid2() >= 0 && getNpcTemplate().is_agrogfxid2() <= 4) {
						if (_classGfxId[getNpcTemplate().is_agrogfxid2()][0] == pc.getGfxId().getTempCharGfx()
								|| _classGfxId[getNpcTemplate().is_agrogfxid2()][1] == pc.getGfxId().getTempCharGfx()) {
							targetPlayer = pc;
							break;
						}
					} else if (pc.getGfxId().getTempCharGfx() == getNpcTemplate().is_agrogfxid2()) {
						targetPlayer = pc;
						break;
					}
				}
			}   // 요기부터 허수아비 패기
			
		}  // 여기까지 시발사원
		
		
		if (getNpcTemplate().get_npcId() == 7000042
				|| getNpcTemplate().get_npcId() == 7000009
				|| getNpcTemplate().get_npcId() == 7000013
				|| getNpcTemplate().get_npcId() == 7000016
				|| getNpcTemplate().get_npcId() == 7000019
				|| getNpcTemplate().get_npcId() == 7000006
				|| getNpcTemplate().get_npcId() == 7000010
				|| getNpcTemplate().get_npcId() == 7000014
				|| getNpcTemplate().get_npcId() == 7000017
				|| getNpcTemplate().get_npcId() == 7000020
				|| getNpcTemplate().get_npcId() == 7000007
				|| getNpcTemplate().get_npcId() == 7000011
				|| getNpcTemplate().get_npcId() == 7000015
				|| getNpcTemplate().get_npcId() == 7000018
				|| getNpcTemplate().get_npcId() == 7000021
				|| getNpcTemplate().get_npcId() == 7000022
				|| getNpcTemplate().get_npcId() == 7000023
				|| getNpcTemplate().get_npcId() == 7000024
				|| getNpcTemplate().get_npcId() == 7000025
				|| getNpcTemplate().get_npcId() == 7000026
				|| getNpcTemplate().get_npcId() == 7000027
				|| getNpcTemplate().get_npcId() == 7000028
				|| getNpcTemplate().get_npcId() == 7000029
				|| getNpcTemplate().get_npcId() == 7000030
				|| getNpcTemplate().get_npcId() == 7000031
				|| getNpcTemplate().get_npcId() == 7000032
				|| getNpcTemplate().get_npcId() == 7000033
				|| getNpcTemplate().get_npcId() == 7000034
				|| getNpcTemplate().get_npcId() == 7000035
				|| getNpcTemplate().get_npcId() == 7000036
				|| getNpcTemplate().get_npcId() == 7000037
				|| getNpcTemplate().get_npcId() == 7000038
				|| getNpcTemplate().get_npcId() == 7000039
				|| getNpcTemplate().get_npcId() == 7000040
				|| getNpcTemplate().get_npcId() == 7000041
				|| getNpcTemplate().get_npcId() == 7000008
				|| getNpcTemplate().get_npcId() == 7000012
				|| getNpcTemplate().get_npcId() == 7000013
				|| getNpcTemplate().get_npcId() == 7000014
				|| getNpcTemplate().get_npcId() == 7000015) { // 적을 인식할 몬스터
			
			for (L1Object obj : L1World.getInstance().getVisibleObjects(this)) {
				
				if (obj instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) obj;
					
					if (mon.getHiddenStatus() != 0 || mon.isDead()) {
						continue;
					}
					
					if (mon.getNpcTemplate().get_npcId() == 45003
							|| mon.getNpcTemplate().get_npcId() == 45060
							|| mon.getNpcTemplate().get_npcId() == 45157
							|| mon.getNpcTemplate().get_npcId() == 45241
							|| mon.getNpcTemplate().get_npcId() == 45223
							|| mon.getNpcTemplate().get_npcId() == 45298) {
						targetMonster = mon;
						break;
					}
				}
			}
			
		} // <<추가
		
		if (targetPlayer != null) {
			L1PetInstance Pet = (L1PetInstance) targetPlayer.getPet();
			
			if(Pet != null && _random.nextInt(100) >= 98){
				_hateList.add(Pet, 0);
				_target = Pet;
			} else {
				_hateList.add(targetPlayer, 0);
				_target = targetPlayer;
			}
		}
		
		if (targetMonster != null) {
			_hateList.add(targetMonster, 0);
			_target = targetMonster;
		}
		
	}

	
	public void setTarget(L1Character target) {
		if (target != null) {
			if (target instanceof L1PcInstance || target instanceof L1MonsterInstance) {
				_hateList.add(target, 0);
				_target = target;
			}
		}
	}

	
	public L1Character getTarget() {
		return _target;
	}

	
	@Override
	public void setLink(L1Character cha) {
		if (cha != null && _hateList.isEmpty()) {
			_hateList.add(cha, 0);
			checkTarget();
		}
	}

	
	public L1MonsterInstance(L1Npc template) {
		super(template);
		_storeDroped = 1;
		
		if (getNpcId() == 100342 || getNpcId() == 100422) { // 사막 회오리, 에르자베 회오리
			Random _rnd = new Random(System.nanoTime());
			int time = (_rnd.nextInt(30) + 60) * 1000;
			
			if (getNpcId() == 100422) {
				time = (_rnd.nextInt(60) + 30) * 1000;
			}
			
			L1NpcDeleteTimer timer = new L1NpcDeleteTimer(this, time);
			timer.begin();
			GeneralThreadPool.getInstance().schedule(new 에르자베회오리Timer(this), 500);
		} else if (getNpcId() == 100586 || getNpcId() == 100587) {
			GeneralThreadPool.getInstance().execute(new 기르불());
		} else if (getNpcId() == 100859) {
			GeneralThreadPool.getInstance().schedule(new 하이네트랩Timer(this), 500);
		}

	}

	
	// private int abcd1 = 0;
	// private int abcd2 = 0;
	@Override
	public synchronized void onNpcAI() {
		// abcd1++;
		if (isAiRunning()) {
			return;
		}
		
		/*
		 * abcd2++; if(abcd2 > 1){
		 * System.out.println("[드랍오류1] t1 : "+abcd1+" / t2 : "
		 * +abcd2+" / id :"+getNpcId
		 * ()+" / x-"+getX()+" y-"+getY()+" m-"+getMapId()); }
		 */
		if (_storeDroped == 1) {
			_storeDroped = 0;
			DropTable.getInstance().setDrop(this, getInventory());
			getInventory().shuffle();
		} else if (_storeDroped == 2) {
			_storeDroped = 0;
			DropTable.getInstance().setPainwandDrop(this, getInventory());
			getInventory().shuffle();
		}

		setActived(false);
		startAI();

	}

	
	@Override
	public void onTalkAction(L1PcInstance pc) {
		int objid = getId();
		L1NpcTalkData talking = NPCTalkDataTable.getInstance().getTemplate(getNpcTemplate().get_npcId());
		
		if (talking == null) {
			return;
		}
		
		if (pc.getLawful() < -1000) { // 플레이어가 카오틱
			pc.sendPackets(new S_NPCTalkReturn(talking, objid, 2), true);
		} else {
			pc.sendPackets(new S_NPCTalkReturn(talking, objid, 1), true);
		}
	}

	
	@Override
	public void onAction(L1PcInstance pc, int adddmg) {
		/** 얼던 몹트랜스관련 수정 by사부 **/
		if (getNpcTemplate().get_npcId() == 90000
				|| getNpcTemplate().get_npcId() == 90002
				|| getNpcTemplate().get_npcId() == 90009
				|| getNpcTemplate().get_npcId() == 90013
				|| getNpcTemplate().get_npcId() == 90016) {// 얼던
			receiveDamage(pc, 1);
		} else {
			if (shellman()) {
				L1Attack attack = new L1Attack(pc, this);
				attack.action();
				attack = null;
				return;
			}
			
			if (getCurrentHp() > 0 && !isDead()) {
				L1Attack attack = new L1Attack(pc, this);
				
				if (attack.calcHit()) {
					boolean isCounterBarrier = false;
					
					if (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {

						int chan = _random.nextInt(100) + 1;
						boolean isProbability = false;
						if (20 > chan) {
							isProbability = true;
						}
						
						boolean isShortDistance = attack.isShortDistance();
						if (isProbability && isShortDistance) {
							isCounterBarrier = true;
						}
						
					}
					
					if (isCounterBarrier) {
						attack.actionCounterBarrier();
						attack.commitCounterBarrier();
					}
					
					int enc = pc.getWeapon() == null ? 0 : pc.getWeapon().getEnchantLevel();
					if (enc < 0) {
						enc = 0;
					}
					
					if (getNpcTemplate().get_IsTU() && pc.getWeapon() != null && pc.getWeapon().getItemId() == 412003 && _random.nextInt(100) < 5 + enc) {
						new L1SkillUse().handleCommands(pc, L1SkillId.TURN_UNDEAD, getId(), getX(), getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					} else {
						attack.calcDamage(adddmg);
						attack.calcStaffOfMana();
						attack.calcDrainOfHp();
						attack.addPcPoisonAttack(pc, this);
					}
				}
				
				attack.action();
				attack.commit();
				attack = null;
			}
		}
	}
	

	@Override
	public void onAction(L1PcInstance pc) {
		/** 얼던 몹트랜스관련 수정 by사부 **/
		if (getNpcTemplate().get_npcId() == 90000
				|| getNpcTemplate().get_npcId() == 90002
				|| getNpcTemplate().get_npcId() == 90009
				|| getNpcTemplate().get_npcId() == 90013
				|| getNpcTemplate().get_npcId() == 90016) {// 얼던
			receiveDamage(pc, 1);
		} else {
			if (shellman()) {
				L1Attack attack = new L1Attack(pc, this);
				attack.action();
				attack = null;
				return;
			}
			
			if (getCurrentHp() > 0 && !isDead()) {
				L1Attack attack = new L1Attack(pc, this);
				if (attack.calcHit()) {
					boolean isCounterBarrier = false;
					if (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {

						int chan = _random.nextInt(100) + 1;
						boolean isProbability = false;
						
						if (20 > chan) {
							isProbability = true;
						}
						
						boolean isShortDistance = attack.isShortDistance();
						if (isProbability && isShortDistance) {
							isCounterBarrier = true;
						}
					}
					
					if (isCounterBarrier) {
						attack.actionCounterBarrier();
						attack.commitCounterBarrier();
					}
					
					int enc = pc.getWeapon() == null ? 0 : pc.getWeapon().getEnchantLevel();
					if (enc < 0) {
						enc = 0;
					}
					
					if (getNpcTemplate().get_IsTU() && pc.getWeapon() != null
							&& pc.getWeapon().getItemId() == 412003
							&& _random.nextInt(100) < 5 + enc) {
						new L1SkillUse().handleCommands(pc, L1SkillId.TURN_UNDEAD, getId(), getX(), getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					} else {
						attack.calcDamage();
						attack.calcStaffOfMana();
						attack.calcDrainOfHp();
						attack.addPcPoisonAttack(pc, this);
					}
				}
				
				attack.action();
				attack.commit();
				attack = null;
			}
		}
		
	}

	
	@Override
	public void ReceiveManaDamage(L1Character attacker, int mpDamage) {
		if (mpDamage > 0 && !isDead()) {
			// int Hate = mpDamage / 10 + 10;
			// setHate(attacker, Hate);
			if (!(getNpcId() >= 100750 && getNpcId() <= 100757)) {
				setHate(attacker, mpDamage);
			}

			onNpcAI();

			if (attacker instanceof L1PcInstance) {
				serchLink((L1PcInstance) attacker, getNpcTemplate().get_family());
			}

			int newMp = getCurrentMp() - mpDamage;
			if (newMp < 0) {
				newMp = 0;
			}
			
			setCurrentMp(newMp);
		}
	}

	
	private static boolean 감시자리퍼스폰 = false;

	
	class a implements Runnable {
		L1Character _cha = null;

		public a(L1Character cha) {
			_cha = cha;
		}

		public void run() {
			for (int i = 0; i < 100; i++) {
				receiveDamage(_cha, 50000);
			}
		}
	}

	
	class b implements Runnable {
		L1Character _cha = null;

		public b(L1Character cha) {
			_cha = cha;
		}

		public void run() {
			for (int i = 0; i < 100; i++) {
				receiveDamage(_cha, 50000);
			}
		}
	}

	
	public void testonNpcAI(L1Character cha) {
		a atm = new a(cha);
		b atm2 = new b(cha);
		Thread aa = new Thread(atm, "a");
		Thread bb = new Thread(atm2, "b");
		aa.start();
		bb.start();
	}

	
	@Override
	public void receiveDamage(L1Character attacker, int damage) {
		if (isreodie) {
			return;
		}
		
		if (transok) {
			return;
		}
		
		if (getCurrentHp() > 0) {
			if (getHiddenStatus() != HIDDEN_STATUS_NONE || getHiddenStatus() == HIDDEN_STATUS_FLY) {
				return;
			}
			
			if (attacker instanceof L1PcInstance) {
				if (getNpcTemplate().is_doppel()) {
					L1PinkName.onAction(this, attacker);
				}
			}
			
			if (getNpcId() == 100749 && attacker instanceof L1PcInstance) {
				return;
			}

			if (!(getNpcId() >= 100750 && getNpcId() <= 100757)) {
				if (damage >= 0) {
					if (!(attacker instanceof L1EffectInstance)) {
						setHate(attacker, damage);		// 기존 소스

						/** 몬스터 피 보이게 수정 
						int HpR = 100 * (getCurrentHp() - damage) / getMaxHp();
						broadcastPacket( new S_HPMeter(getId(), HpR, 0xff));
						if(getCurrentHp() < damage){
							String TITLE = "LV=" + getLevel() + " : HP=" + 0; //몹이 죽었을경우 HP가 0
							HpR = 0;
							broadcastPacket( new S_HPMeter(getId(), HpR, 0xff));
							broadcastPacket(new S_CharTitle(getId(), TITLE));
							broadcastPacket(new S_SystemMessage("\\fS" + TITLE));
							setHate( attacker, damage);
						} else {
							String TITLE = "LV=" + getLevel() + " : HP=" + (getCurrentHp() - damage);
							broadcastPacket(new S_CharTitle(getId(), TITLE));
							broadcastPacket(new S_SystemMessage("\\fS" + TITLE));
							setHate( attacker, damage);
						}
						
						Dlog.d("HP : " + HpR);
						*/
						
					}
				}
			}
			
			if (damage > 0 && !isDead()) {
				if (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
					getSkillEffectTimerSet().removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
				} else if (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.PHANTASM)) {
					getSkillEffectTimerSet().removeSkillEffect(L1SkillId.PHANTASM);
				} else if (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DARK_BLIND)) {
					getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DARK_BLIND);
				}
				
				if ((getNpcTemplate().get_npcId() >= 100032 && getNpcTemplate().get_npcId() <= 100044
						|| getNpcId() == 100338  || getNpcId() == 100157
						|| getNpcId() == 1100157 || getNpcId() == 100149
						|| getNpcId() == 1100149 || getNpcId() == 100420
						|| getNpcId() == 100397  || getNpcId() == 100584
						|| getNpcId() == 100588  || getNpcId() == 100589 || getNpcId() == 1000651
						|| getNpcId() == 60443   || getNpcId() == 100718 || getNpcId() == 100719  || getNpcId() == 910021
						|| getNpcId() == 910028
						|| getNpcId() == 910036
						|| getNpcId() == 910042
						|| getNpcId() == 910050
						|| getNpcId() == 910056
						|| getNpcId() == 910075
						|| getNpcId() == 910062
						|| getNpcId() == 910069
						|| getNpcId() == 910014
						|| getNpcId() == 910088
						|| getNpcId() == 45600
						|| getNpcId() == 7280193
						|| getNpcId() == 46025
						|| getNpcId() == 45617
						|| getNpcId() == 45516
						|| getNpcId() == 100716
						|| getNpcId() == 100717)
						&& attacker instanceof L1PcInstance) {
					
					if (옛셀로브리스트 != null && !옛셀로브리스트.contains((L1PcInstance) attacker)) {
						옛셀로브리스트.add((L1PcInstance) attacker);
					}
				}
				
			}

			onNpcAI();

			
			if (attacker instanceof L1PcInstance) {
				serchLink((L1PcInstance) attacker, getNpcTemplate().get_family());
			}
			
			if (attacker instanceof L1SummonInstance) {
				L1SummonInstance sum = (L1SummonInstance) attacker;
				if (getNpcTemplate().get_npcId() == 45681
						|| getNpcTemplate().get_npcId() == 45682
						|| getNpcTemplate().get_npcId() == 45683
						|| getNpcTemplate().get_npcId() == 45684) {
					sumdel(sum);
				}

			}
			
			if (attacker instanceof L1PcInstance && damage > 0) {
				L1PcInstance player = (L1PcInstance) attacker;
				player.setPetTarget(this);

				if (getNpcTemplate().get_npcId() == 45681
						|| getNpcTemplate().get_npcId() == 45682
						|| getNpcTemplate().get_npcId() == 45683
						|| getNpcTemplate().get_npcId() == 45684) {
					recall(player);
				}
			}

			if (getNpcTemplate().get_npcId() == 100719 && getSkillEffectTimerSet().hasSkillEffect(L1SkillId.제로스리덕)) {
				damage *= 0.3;
				
				if (damage < 0) {
					damage = 0;
				}
			}

			if (getNpcTemplate().get_npcId() == 4039001) { // 파푸 오색진주
				if (!attacker.getSkillEffectTimerSet().hasSkillEffect(10501)) {
					damage = 0;
					return;
				} else {
					damage = 1;
				}
			}
			
			if (getNpcTemplate().get_npcId() == 4039002) {
				if (!attacker.getSkillEffectTimerSet().hasSkillEffect(10500)) {
					damage = 0;
					return;
				} else {
					damage = 1;
				}
			}

			if (getNpcId() == 100707 && damage > 0) {
				damage = 1;
			}

			if (getNpcTemplate().get_npcId() == 4039003) {
				damage = 0;
				return;
			}
			
			int 몬스터 = getNpcTemplate().get_npcId();
			int newHp = getCurrentHp() - damage;
			if (!Escape) {
				
				if (getNpcTemplate().get_npcId() == 100045) {
					int hpRatio = 100;
					if (0 < getMaxHp()) {
						hpRatio = 100 * getCurrentHp() / getMaxHp();
					}
					
					if (hpRatio < 40) {
						Escape = true;
						STATUS_Escape = true;
						GeneralThreadPool.getInstance().schedule(new 도망Timer(), 500);
					}
				}
			}
			
			
			/** 인던 수정구라면 패스 !! 데미지 안들감!  */
			if (getNpcId() == 46400){
				if(attacker instanceof L1PcInstance) return;
				if(attacker instanceof L1MonsterInstance){
					int HpRatio = 0;
					if (getCurrentHp() > 0) HpRatio = 100 * newHp / getMaxHp();
					
					for (L1PcInstance User : L1World.getInstance().getVisiblePlayer(this)) {
						User.sendPackets(new S_HPMeter(getId(), HpRatio, 0xff), true);
					}
				}
			}

			
			if (newHp <= 0 && !isDead()) {
				/** 특정 몬스터 잡았을시 정보 리셋 시키도록 세팅 *//*
				if (몬스터 == 100420 || 몬스터 == 100338 || 몬스터 == 45752){
					BossAlive.getInstance().BossDeath(getMapId()); //보스존에서 보스가죽으면 false로 변경
					if (attacker instanceof L1PcInstance) {
						if(!((L1PcInstance) attacker).isRobot()){
						switch(몬스터){
							case 100420:
								BossAlive.getInstance().is샌드웜 = false;
								RotationNoticeTable.getInstance().UpdateNotice(2, null, null);
								for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
									if (pc instanceof L1RobotInstance) continue;
									pc.알림서비스(pc, true);
								}
								break;
							case 100338:
								BossAlive.getInstance().is에르자베 = false;
								RotationNoticeTable.getInstance().UpdateNotice(1, null, null);
								for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
									if (pc instanceof L1RobotInstance) continue;
									pc.알림서비스(pc, true);
								}
								break;
							case 45752:
								BossAlive.getInstance().is발록 = false;
								RotationNoticeTable.getInstance().UpdateNotice(11, null, null);
								for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
									if (pc instanceof L1RobotInstance) continue;
									pc.알림서비스(pc, true);
								}
								break;
							}
						}
					}
				}*/
				
				if (몬스터 == 60171 || 몬스터 == 60172 || 몬스터 == 60173 || 몬스터 == 60174 || 몬스터 == 60175 || 몬스터 == 60176 || 몬스터 == 60177 || 몬스터 == 60178
						|| 몬스터 == 60179 || 몬스터 == 60180 || 몬스터 == 60181 || 몬스터 == 60182 || 몬스터 == 60183 || 몬스터 == 60184 || 몬스터 == 60185 || 몬스터 == 6018){
					BossAlive.getInstance().BossDeath(getMapId()); //보스존에서 보스가죽으면 false로 변경
					if (attacker instanceof L1PcInstance) {
						switch(몬스터){
							case 60171:
								L1World.getInstance().broadcastServerMessage("\\aJ1번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ1번 선물도둑사망."));
								break;
							case 60172:
								L1World.getInstance().broadcastServerMessage("\\aJ2번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ2번 선물도둑사망."));
								break;
							case 60173:
								L1World.getInstance().broadcastServerMessage("\\aJ3번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ3번 선물도둑사망."));
								break;
							case 60174:
								L1World.getInstance().broadcastServerMessage("\\aJ4번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ4번 선물도둑사망."));
								break;
							case 60175:
								L1World.getInstance().broadcastServerMessage("\\aJ5번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ5번 선물도둑사망."));
								break;
							case 60176:
								L1World.getInstance().broadcastServerMessage("\\aJ6번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ6번 선물도둑사망."));
								break;
							case 60177:
								L1World.getInstance().broadcastServerMessage("\\aJ7번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ7번 선물도둑사망."));
								break;
							case 60179:
								L1World.getInstance().broadcastServerMessage("\\aJ8번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ8번 선물도둑사망."));
								break;
							case 60180:
								L1World.getInstance().broadcastServerMessage("\\aJ9번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ9번 선물도둑사망."));
								break;
							case 60181:
								L1World.getInstance().broadcastServerMessage("\\aJ10번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ10번 선물도둑사망."));
								break;
							case 60182:
								L1World.getInstance().broadcastServerMessage("\\aJ11번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ11번 선물도둑사망."));
								break;
							case 60183:
								L1World.getInstance().broadcastServerMessage("\\aJ12번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ12번 선물도둑사망."));
								break;
							case 60184:
								L1World.getInstance().broadcastServerMessage("\\aJ13번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ13번 선물도둑사망."));
								break;
							case 60185:
								L1World.getInstance().broadcastServerMessage("\\aJ14번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ14번 선물도둑사망."));
								break;
							case 60186:
								L1World.getInstance().broadcastServerMessage("\\aJ15번 선물도둑 사망.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aJ15번 선물도둑사망."));
								break;
							case 60178:
								L1World.getInstance().broadcastServerMessage("\\aG선물 도둑 두목이 사망하였습니다.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aG선물 도둑 두목이 사망하였습니다."));
					            L1World.getInstance().broadcastServerMessage("\\aG선물 도둑 잡기 이벤트가 종료 되었습니다.");
					            L1World.getInstance().broadcastPacketToAll(new S_PacketBox(84, "\\aG선물 도둑 잡기 이벤트가 종료 되었습니다."));
					            L1World.getInstance().broadcastServerMessage("\\aG시작 시간: 매 시간 1분,31분.");
					            L1World.getInstance().broadcastServerMessage("\\aG이벤트 위치: 기란 감옥 1층 지역 내부.");
					            L1World.getInstance().broadcastServerMessage("\\aG보상 안내: 오림의장신구상자 및 1천만 아덴.");
					            L1World.getInstance().broadcastServerMessage("\\aG진행 방식: 1~15선물도둑 기감1층 젠 선물 도둑 모두 정리시 대왕 선물 도둑 출현.");
								break;
							}
						}
				}
				
				if (attacker instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) attacker;
					int monNum = MonsterBookTable.getInstace().getMonsterList(getNpcTemplate().get_npcId());                  
                    
					if (monNum != 0 && !pc.noPlayerCK) {//로봇은 도감에 추가하지않는다 제외(오류발생)
                        MonsterBookTable.getInstace().addMon_Counter(pc.getId(), monNum);
                        int monsterkillcount = MonsterBookTable.getInstace().getMon_Conter(pc.getId(), monNum);                     
                        pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_ADD, monNum, monsterkillcount));
                        int monquest1 = MonsterBookTable.getInstace().getQuest1(monNum);
                        int monquest2 = MonsterBookTable.getInstace().getQuest2(monNum);
                        int monquest3 = MonsterBookTable.getInstace().getQuest3(monNum);                        
                        
                        if (monsterkillcount == monquest1) {                            
                            pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_CLEAR, (monNum * 3) - 2));
                        } else if (monsterkillcount == monquest2) {     
                            pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_CLEAR, (monNum * 3) - 1));
                        } else if (monsterkillcount == monquest3) {  
                            pc.sendPackets(new S_MonsterUi(S_MonsterUi.MONSTER_CLEAR, monNum * 3));
                        }
                    }
                    
					if(!pc.isRobot()){
						int bookId = getNpcTemplate().getBookId();
						if(bookId > 0 && !isResurrect()) {	// 북에 없거나 부활된 몬스터는 처리안함.
							MonsterBook book = MonsterBookLoader.getInstance().getTemplate(bookId);
							/*pc.getMonsterBook().addMonster(book);*/
							pc.getWeekQuest().addMonster(book);
						 }
					}
					
					if (QuestInfoTable.getInstance().getQuestMonid(getNpcId())) {
						QuestMonTemp temp = QuestInfoTable.getInstance().getQuestMonTemp(getNpcId());
						if (temp != null) {
							for (L1Character cha : getHateList().toTargetArrayList()) {
								if (cha != null && cha instanceof L1PcInstance) {
									L1PcInstance use = (L1PcInstance) cha;
									L1QuestInfo info = use.getQuestList(temp.quest_id);
									//TODO 기존 몬스터처치 부근문제 해결 주석체크
									if (info == null || info.end_time != 0) {
										continue;
//										return;
									}
									
									if (info.ck[temp.type] >= temp.count) {
										info.ck[temp.type] = temp.count;
									}
									
									info.ck[temp.type] = temp.count; // 한마리잡으면
									use.sendPackets(new S_QuestTalkIsland(use, temp.quest_id, info));
								}
							}
						}
					}
					
					if (getNpcTemplate().get_npcId() == 101022) {
						pc.삼단가속();
					}
					
					if (getNpcTemplate().get_npcId() == 101017) {// 에이치피회복
						new L1SkillUse().handleCommands(pc, L1SkillId.GREATER_HEAL, pc.getId(), pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
					}
					
					if (getNpcTemplate().get_npcId() == 101018) {// 엠피회복
						pc.setCurrentMp(pc.getCurrentMp() + 100);
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
						// new L1SkillUse().handleCommands(pc,
						// L1SkillId.BLESS_WEAPON, getId(), getX(), getY(),
						// null, 1200 ,L1SkillUse.TYPE_GMBUFF);
					}
					
					if (getNpcTemplate().get_npcId() == 101019) {// 블레스웨폰
						new L1SkillUse().handleCommands(pc, L1SkillId.BLESS_WEAPON, pc.getId(), pc.getX(), pc.getY(), null, 1200, L1SkillUse.TYPE_GMBUFF);
					}
					
					if (getNpcTemplate().get_npcId() == 101020) {// 페이션스
						new L1SkillUse().handleCommands(pc, L1SkillId.PATIENCE, pc.getId(), pc.getX(), pc.getY(), null, 600, L1SkillUse.TYPE_GMBUFF);
					}
					
					if (getNpcTemplate().get_npcId() == 101021) {// 어벤
						new L1SkillUse().handleCommands(pc, L1SkillId.ADVANCE_SPIRIT, pc.getId(), pc.getX(), pc.getY(), null, 1200, L1SkillUse.TYPE_GMBUFF);
					}
				}

				
				if (getMapId() >= 2600 && getMapId() <= 2698) {
					if (getNpcTemplate().get_npcId() == 100837 || getNpcTemplate().get_npcId() == 100838) {// 레오
						isreodie = true;
						allTargetClear();
						_isReoTH = true;
						GeneralThreadPool.getInstance().execute(new 레오스레드(this));
						
						return;
					}
					
					if (getNpcTemplate().get_npcId() == 100858) {
						vchat_exit();
					}

					
					if (getNpcTemplate().get_npcId() == 100849
							|| getNpcTemplate().get_npcId() == 100850
							|| getNpcTemplate().get_npcId() == 100851) {
						((L1PcInstance) attacker)._텔.settime(31);
					}

					
					if (getNpcTemplate().get_npcId() >= 100840 && getNpcTemplate().get_npcId() <= 100846) {
						int rnd = _random.nextInt(100);
						if (rnd < 5) {
							NPCchat("$18654", 2);
						} else if (rnd < 10) {
							NPCchat("$18655", 2);
						}
					}
				}
				
				// 100840~100846
				/*
				 * 발라카스님이 널 용서하지 않을 것이다.18654 발라카스님의 저주는 널 평생 이승에서 떠돌게 할것
				 * 이다.18655
				 */
				if (getNpcTemplate().get_npcId() == 100839) {
					NPCchat("$18878", 2);
					Broadcaster.broadcastPacket(this, new S_PacketBox(S_PacketBox.HADIN_DISPLAY, 2));
					
					for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
						if (obj instanceof L1DoorInstance) {
							L1DoorInstance door = (L1DoorInstance) obj;
							if (door.getNpcId() == 100835) {// 100834
								door.open();
								break;
							}
						}
					}
					
				}

				if (getNpcTemplate().get_npcId() == 45955) {
					L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(4047);
					
					if (door != null) {
						door.setDead(false);
						door.open();
					}
				}
				
				
				if (getNpcTemplate().get_npcId() == 45585
						|| getNpcTemplate().get_npcId() == 45574
						|| getNpcTemplate().get_npcId() == 45577
						|| getNpcTemplate().get_npcId() == 45844
						|| getNpcTemplate().get_npcId() == 45588
						|| getNpcTemplate().get_npcId() == 45607
						|| getNpcTemplate().get_npcId() == 45612
						|| getNpcTemplate().get_npcId() == 45863
						|| getNpcTemplate().get_npcId() == 45608
						|| getNpcTemplate().get_npcId() == 45676
						|| getNpcTemplate().get_npcId() == 45963
						|| getNpcTemplate().get_npcId() == 45615
						|| getNpcTemplate().get_npcId() == 45648
						|| getNpcTemplate().get_npcId() == 45602) {
					if (getdoorid() != 0) {
						L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(getdoorid());
						if (door != null) {
							door.setDead(false);
							door.open();
						}
					}
				}

				
				if (getNpcTemplate().get_npcId() == 45959) {
					L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(4051);
					if (door != null) {
						door.setDead(false);
						door.open();
					}
				}

				
				if (attacker instanceof L1PcInstance) {
					L1PcInstance _atker = (L1PcInstance) attacker;
					_atker._PlayMonKill++;
					_atker.sendPackets(new S_OwnCharStatus(_atker), true);
				}

				
				if (getMapId() >= 1005 && getMapId() <= 1010) {
					if (getNpcTemplate().get_npcId() == 4038001 || getNpcTemplate().get_npcId() == 4038002) {
						if (attacker instanceof L1PcInstance) {
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(getMapId());
							ar.MonsterCount(1);
						}
					}
				}
				
				
				if (getMapId() >= 1011 && getMapId() <= 1016) {
					if (getNpcTemplate().get_npcId() == 4039012 || getNpcTemplate().get_npcId() == 4039013) {
						if (attacker instanceof L1PcInstance) {
							PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
									getMapId());
							ar.MonsterCount(1);
						}
					}
				}

				if (getMapId() >= 2101 && getMapId() <= 2151) {
					if (getNpcTemplate().get_npcId() == 46142) {
						if (attacker instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) attacker;
							pc.sendPackets(new S_PacketBox(84,("벽 뒤에 있는 스빈을 만나십시오.")), true);
							pc.getInventory().storeItem(600369, 1);
						}
					} else if (getNpcTemplate().get_npcId() == 46141) {
						if (attacker instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) attacker;
							pc.sendPackets(new S_PacketBox(84, ("얼음 여왕이 시녀의 주술로부터 자유로워졌습니다.")), true);
							pc.getInventory().storeItem(600369, 1);
						}
					} else if (getNpcTemplate().get_npcId() == 46160) {
						if (attacker instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) attacker;
							pc.sendPackets(new S_PacketBox(84, ("아이스 맘보 토끼주술로부터 자유로워졌습니다.")), true);
							pc.getInventory().storeItem(600369, 1);
						}
					}
				}

				
				/** 얼던 몹트랜스관련 수정 by사부 **/
				if (!isTrans() && getNpcTemplate().get_npcId() == 90000
						|| getNpcTemplate().get_npcId() == 90002
						|| getNpcTemplate().get_npcId() == 90009
						|| getNpcTemplate().get_npcId() == 90013
						|| getNpcTemplate().get_npcId() == 90016) {// 얼던
					setTrans(true);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), 11), true);
					transform(this.getNpcId() + 1);
					return;
				}
				

				Random random = new Random();
				if (attacker instanceof L1PcInstance) {
					L1PcInstance apc = (L1PcInstance) attacker;
					if (apc.getInventory().checkEquipped(20344)) {
						int chance1 = random.nextInt(100) + 1;
						if (Config.RATE_DREAM > chance1) {
							getInventory().storeItem(438005, 1); // 만월의정기 모든몹드랍
						}
					}
				}

				
				int npcid = getNpcTemplate().get_npcId();
				int chance2 = random.nextInt(100) + 1;
				int chance3 = random.nextInt(10000) + 1;
				random = null;

				
				if (chance2 <= 2) {
					// 몽섬 바람, 땅, 물, 불 대정령 변환
					if (getNpcId() >= 100726 && getNpcId() <= 100729) {
						Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
						L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 100730, 0, 0, 0);
					} else if (getNpcId() >= 100731 && getNpcId() <= 100734) {
						Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
						L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 100735, 0, 0, 0);
					} else if (getNpcId() >= 100736 && getNpcId() <= 100739) {
						Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
						L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 100740, 0, 0, 0);
					} else if (getNpcId() >= 100741 && getNpcId() <= 100744) {
						Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
						L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 100745, 0, 0, 0);
					}
				}

				
				if (getMapId() >= 9103 && getMapId() <= 9199) {
					NavalWarfareController nwc = NavalWarfare.getInstance().getNaval(getMapId());
					if (nwc != null) {
						if (chance2 < 5){
							nwc.score++;
						}
						
						nwc.score++;
						nwc.scorePacket(nwc.score);
					}
				}
				
				if (chance2 <= 10) {
					if (getMapId() >= 101 && getMapId() <= 110) {
						if ((npcid == 707012 || npcid == 707011 || npcid == 707010
								|| npcid == 707009 || npcid == 707008
								|| npcid == 707006 || npcid == 707005
								|| npcid == 707007 || npcid == 707004 || npcid == 707003)
								&& !감시자리퍼스폰 /* !리퍼시간체크.check(getMapId()) */) {
							// 메두사, 다이어울프, 공포켈베, 난폭스파, 레서드래곤, 저주엘모어병사, 냉혹샤벨,
							// 어둠불타는전사, 공포좀비로드, 물의대정령
							
							감시자리퍼스폰 = true;
							
							GeneralThreadPool.getInstance().schedule(
								new Runnable() {
									@Override
									public void run() {
										L1MonsterInstance.감시자리퍼스폰 = false;
									}

							}, 3600000 * 3);
							
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45590, 0, 0, 0);
							// 리퍼시간체크.add(getMapId(), getId());
						}
					}
				}
				
				
				if (npcid == 45590 || npcid == 910009 || npcid == 910016 || npcid == 910022 || npcid == 910030
						|| npcid == 910041 || npcid == 910044 || npcid == 910052 || npcid == 910059 || npcid == 910065
						|| npcid == 910049) {
					
					if (chance3 < 3) {
						
						if (getMapId() == 12852) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45513, 0, 0, 0);
						} else if (getMapId() == 12853) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45547, 0, 0, 0);
						} else if (getMapId() == 12854) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45606, 0, 0, 0);
						} else if (getMapId() == 12855) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45650, 0, 0, 0);
						} else if (getMapId() == 12856) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45652, 0, 0, 0);
						} else if (getMapId() == 12857) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45653, 0, 0, 0);
						} else if (getMapId() == 12858) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45654, 0, 0, 0);
						} else if (getMapId() == 12859) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45618, 0, 0, 0);
						} else if (getMapId() == 12860) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 45672, 0, 0, 0);
						} else if (getMapId() == 12861) {
							Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
							L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 100002, 0, 0, 0);
						}
					}
				}

				
				// 그림리퍼
				if (npcid == 81047) {
					Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), 4784), true);
				}

				if (npcid == 100719) { // 제로스
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "나는 결국 다시 부활하게 될 것이다. 바로 너희들의 손에 의하여! 으하하하..", 0), true);
				}

				/** 오만 보스 죽을때 대사 **/
				if (npcid == 45673) { // 사신 그림 리퍼
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "오만한 인간들이여 나는 죽음의 신! 그림리퍼! 너희의 생이 끝날 때 다시 만날 것이다. 으하하하하", 0), true);
				} else if (npcid == 45672) { // 불멸의 리치
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "발드..발드 그는 무사한가? 제니스..그녀는?", 0), true);
				} else if (npcid == 45618) { // 어둠의 나이트발드
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "나를 쓰러트린 것이 그대들인가? 나의 왕을 구해다오..부탁하지..", 0), true);
				} else if (npcid == 45654) { // 냉혹한 아이리스
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "아..아버지...어디 계십니까..?", 0), true);
				} else if (npcid == 45653) { // 불사의 머미로드
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "나..나의 제국은 어떻게..되었는가?..", 0), true);
				} else if (npcid == 45650) { // 죽음의 좀비로드
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "내..내...오른손만..있었어도..부..분하다..", 0), true);
				} else if (npcid == 45652) { // 지옥의 쿠거
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "크르르르르르...", 0), true);
				} else if (npcid == 45547) { // 불신의 시어
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "뀌에에에엑!...", 0), true);
				} else if (npcid == 45606) { // 공포의 뱀파이어
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "이..이럴수가..내 마성의 힘을 이겨내다니..", 0), true);
				} else if (npcid == 45513) { // 왜곡의 제니스 퀸
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "에보루타인... 당신..어디 계신가요...", 0), true);
				}

				
				if (npcid == 4038000 && isantarun == false) { // 엔피씨 번호
					isantarun = true;
					AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(getMapId());
					AntarasRaidTimer antaendtime = new AntarasRaidTimer(this, ar, 7, 2000);// 22분 체크
					antaendtime.begin();
				}
				
				if (npcid == 4200010 && isantarun == false) { // 엔피씨 번호
					isantarun = true;
					AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(getMapId());
					AntarasRaidTimer antaendtime = new AntarasRaidTimer(this, ar, 8, 2000);// 22분 체크
					antaendtime.begin();
				}
				
				if (npcid == 4200011 && isantarun == false) { // 엔피씨 번호
					isantarun = true;
					AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(getMapId());
					AntarasRaidTimer antaendtime = new AntarasRaidTimer(this, ar, 9, 2000);// 22분 체크
					antaendtime.begin();
				}
				
				// /////////////////파푸
				if (npcid == 4039000 && ispapoorun == false) { // 엔피씨 번호
					ispapoorun = true;
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(getMapId());
					ar.쓰레기정리();
					PaPooTimer antaendtime = new PaPooTimer(this, ar, 1, 2000); // 22분 체크
					antaendtime.begin();
				}
				
				if (npcid == 4039006 && ispapoorun == false) { // 엔피씨 번호
					ispapoorun = true;
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(getMapId());
					ar.쓰레기정리();
					PaPooTimer antaendtime = new PaPooTimer(this, ar, 2, 2000); // 22분  체크
					antaendtime.begin();
				}
				
				if (npcid == 4039007 && ispapoorun == false) { // 엔피씨 번호
					ispapoorun = true;
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(getMapId());
					ar.쓰레기정리();
					PaPooTimer antaendtime = new PaPooTimer(this, ar, 3, 2000); // 22분 체크
					antaendtime.begin();
				}
				
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						try {
							if (getNpcTemplate().get_npcId() >= 100032 && getNpcTemplate().get_npcId() <= 100044
									|| getNpcId() == 100338
									|| getNpcId() == 100157
									|| getNpcId() == 1100157
									|| getNpcId() == 100149
									|| getNpcId() == 1100149
									|| getNpcId() == 100397
									|| getNpcId() == 100420
									|| getNpcId() == 100584
									|| getNpcId() == 100588
									|| getNpcId() == 100589
									|| getNpcId() == 1000651
									|| getNpcId() == 100707
									|| getNpcId() == 100718
									|| getNpcId() == 100719
									|| getNpcId() == 910021
									|| getNpcId() == 910028
									|| getNpcId() == 910036
									|| getNpcId() == 910042
									|| getNpcId() == 910075
									|| getNpcId() == 910050
									|| getNpcId() == 910056
									|| getNpcId() == 910062
									|| getNpcId() == 910069
									|| getNpcId() == 910014
									|| getNpcId() == 910088
									|| getNpcId() == 45600
									|| getNpcId() == 7280193
									|| getNpcId() == 46025
									|| getNpcId() == 45617
									|| getNpcId() == 45516
									|| getNpcId() == 100716
									|| getNpcId() == 100717) {
								
								int itemid = 0;
								
								if (getNpcTemplate().get_npcId() >= 100034 && getNpcTemplate().get_npcId() <= 100037) {
									itemid = 60057;
								} else if (getNpcId() == 100338) {
									itemid = 60184;
								} else if (getNpcId() == 910021) {
									itemid = 600357;
								} else if (getNpcId() == 910028) {
									itemid = 600407;
								} else if (getNpcId() == 910036) {
									itemid = 600408;
								} else if (getNpcId() == 910042){
									itemid = 600409;
								} else if (getNpcId() == 910050) {
									itemid = 600410;
								} else if (getNpcId() == 910056) {
									itemid = 600411;
								} else if (getNpcId() == 910062) {
									itemid = 600412;
								} else if (getNpcId() == 910069) {
									itemid = 600413;
								} else if (getNpcId() == 910075) {
									itemid = 600414;
								} else if (getNpcId() == 910014) {
									itemid = 600415;
								} else if (getNpcId() == 910088) {
									itemid = 600416;
								} else if (getNpcId() == 45600
										|| getNpcId() == 7280193
										|| getNpcId() == 46025
										|| getNpcId() == 45617
										|| getNpcId() == 45516
										|| getNpcId() == 100716
										|| getNpcId() == 100717) {
									itemid = 600371;
								} else if (getNpcId() == 1000651) {
									itemid = 437009;
								} else if (getNpcId() == 100397) {
									itemid = 60211;
								} else if (getNpcId() == 100420) {
									itemid = 60252;
								} else if (getNpcId() == 100584 || getNpcId() == 100588 || getNpcId() == 100589) {
									itemid = 60311;
								} else if (getNpcId() == 100149 || getNpcId() == 1100149) { // 올딘 해상전
									itemid = 600226;
								} else if (getNpcId() == 100157 || getNpcId() == 1100157) {// 올딘 해상전
									itemid = 500022;
								} else if (getNpcId() == 100707) {
									itemid = 60443;
								} else if (getNpcId() == 100718) {
									itemid = 60497;
								} else if (getNpcId() == 100719) {
									itemid = 60496;
								}
								
								
								if (itemid != 0) {
									if (옛셀로브리스트 != null && 옛셀로브리스트.size() > 0) {
										L1PcInstance[] 옛리스트 = 옛셀로브리스트.toArray(new L1PcInstance[옛셀로브리스트.size()]);
										
										for (L1PcInstance temppc : 옛리스트) {
											int i = getLocation().getTileDistance(temppc.getLocation());
											
											if ((i > 15 || getMapId() != temppc.getMapId()) || temppc.isDead()) {
												continue;
											}
											
											L1ItemInstance tempitem = temppc.getInventory().storeItem(itemid, 1);
											temppc.sendPackets(new S_ServerMessage(403, tempitem.getName()+ " (" + 1+ ")"), true);
											옛셀로브리스트.remove(temppc);
										}
										
										옛리스트 = null;
									}
								}
								
								if (옛셀로브리스트 != null && 옛셀로브리스트.size() > 0) {
									옛셀로브리스트.clear();
								}
								
								옛셀로브리스트 = null;
							}
							
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					
				}, 1);

				if (getMapId() == 1931) {
					int _rnd = _random.nextInt(100);
					int rate = 13;
					
					if (getNpcId() == 100726 || getNpcId() == 100733 || getNpcId() == 100739 || getNpcId() == 100743) {
						rate = 5; // 꿈의혼령 100%지급되게
					}
					
					if (attacker instanceof L1PcInstance) {

						if (getNpcId() >= 100726 && getNpcId() <= 100744
								&& getNpcId() != 100730 && getNpcId() != 100735
								&& getNpcId() != 100740) {
							
							if (_rnd < rate) {
								
								if (attacker.getInventory().countItems(60501) < 50) {
									L1ItemInstance tempitem = attacker.getInventory().storeItem(60501, 1);
									((L1PcInstance) attacker).sendPackets(new S_ServerMessage(403, tempitem.getName() + " (" + 1 + ")"), true);
								}
							}
						}

					} else if (attacker instanceof L1SummonInstance) {
						L1SummonInstance sum = (L1SummonInstance) attacker;
						if (getNpcId() >= 100726 && getNpcId() <= 100744 && getNpcId() != 100730 && getNpcId() != 100735 && getNpcId() != 100740) {
							
							if (sum.getMaster() != null) {
								
								if (rate < rate) {
									
									if (sum.getMaster().getInventory().countItems(60501) < 50) {
										L1ItemInstance tempitem = sum.getMaster().getInventory().storeItem(60501, 1);
										((L1PcInstance) sum.getMaster()).sendPackets(new S_ServerMessage(403, tempitem.getName()+ " ("+ 1+ ")"),true);
									}
								}
							}
						}
					} else if (attacker instanceof L1PetInstance) {
						L1PetInstance sum = (L1PetInstance) attacker;
						if (getNpcId() >= 100726 && getNpcId() <= 100744 && getNpcId() != 100730 && getNpcId() != 100735 && getNpcId() != 100740) {
							if (sum.getMaster() != null) {
								if (rate < rate) {
									if (sum.getMaster().getInventory().countItems(60501) < 50) {
										L1ItemInstance tempitem = sum.getMaster().getInventory().storeItem(60501, 1);
										((L1PcInstance) sum.getMaster()).sendPackets(new S_ServerMessage(403, tempitem.getName() + " (" + 1 + ")"), true);
									}
								}
							}
						}
					}
				}
				
				// 은기사 허수아비
				if (getNpcId() >= 100774 && getNpcId() <= 100776) {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							resurrect(getMaxHp());
						}
					}, 4000);
				}

				int transformId = getNpcTemplate().getTransformId();
				if (transformId == -1) {
					setCurrentHp(0);
					setDead(true);
					setActionStatus(ActionCodes.ACTION_Die);
					
					if (isPapoo()) {
						setDeathProcessing(true);
						setCurrentHp(0);
						setDead(true);
						setDeathProcessing(false);
						setExp(0);
						setKarma(0);
						allTargetClear();
						setActionStatus(ActionCodes.ACTION_Die);
						Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
						deleteMe();
						return;
					} else {
						if (getNpcId() == 45456 && _random.nextInt(100) < 50) {
							Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
							deleteMe();
							GeneralThreadPool.getInstance().schedule(
								new Runnable() {
									@Override
									public void run() {
										try {
											L1NpcInstance mon = L1SpawnUtil.spawn2(getX(), getY(), (short) getMap().getId(), 100628, 0, 0, 0);
											Thread.sleep(500);
											Broadcaster.broadcastPacket(mon, new S_NpcChatPacket(mon, "$15700", 0), true);
										} catch (Exception e) {
										}
									}
								}, 1000);
							
							return;
						}

						// 몬스터 죽었을때 처리
						Death death = new Death(attacker, this);
						GeneralThreadPool.getInstance().execute(death);

						dead.setAttacker(attacker);
						GeneralThreadPool.getInstance().execute(dead);

					}
					
					if (ismarble()) {
						for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 15)) {
							if (obj instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) obj;
								if (npc.getNpcId() == 4039000 || npc.getNpcId() == 4039006 || npc.getNpcId() == 4039007) {
									npc.marble.remove("오색구슬");
									if (npc.marble.size() == 0) {
										// PaPooRaid ar =
										// PaPooRaidSystem.getInstance().getAR(npc.getMapId());
										// ar.사엘().deleteMe();
									}
								}
							}
						}
						
						setCurrentHp(0);
						setDead(true);
						setActionStatus(ActionCodes.ACTION_Die);

						Death death = new Death(attacker, this);
						GeneralThreadPool.getInstance().execute(death);

						dead.setAttacker(attacker);
						GeneralThreadPool.getInstance().execute(dead);

					}
					
					if (ismarble2()) {
						setCurrentHp(0);
						setDead(true);
						
						for (L1Object obj : L1World.getInstance().getVisibleObjects(this, 15)) {
							
							if (obj instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) obj;
								
								if (npc.getNpcId() == 4039000 || npc.getNpcId() == 4039006 || npc.getNpcId() == 4039007) {
									npc.marble2.remove("신비한오색구슬");
									
									if (npc.marble2.size() == 0) {
										// PaPooRaid ar =
										// PaPooRaidSystem.getInstance().getAR(npc.getMapId());
										// ar.사엘2().deleteMe();
										Broadcaster.broadcastPacket(npc, new S_SkillHaste(npc.getId(), 0, 0), true);
										npc.getMoveState().setMoveSpeed(0);
									}
								}
							}
						}
						
						setActionStatus(ActionCodes.ACTION_Die);

						Death death = new Death(attacker, this);
						GeneralThreadPool.getInstance().execute(death);

						dead.setAttacker(attacker);
						GeneralThreadPool.getInstance().execute(dead);

					}
				} else {
					/*
					 * if(isGDMonster()){ setCurrentHp(0); setDead(true);
					 * die2(attacker); ///////리뉴얼안타///// } else
					 */
					if (isAntharas()) {
						setCurrentHp(0);
						setDead(true);
						Death death = new Death(attacker, this);
						GeneralThreadPool.getInstance().execute(death);

						dieAntharas(attacker);
						GeneralThreadPool.getInstance().execute(dead);
						// ////리뉴얼안타//////
					}
					
					if (isPapoo()) {
						setCurrentHp(0);
						setDead(true);
						diePaPoo(attacker);

						Death death = new Death(attacker, this);
						GeneralThreadPool.getInstance().execute(death);

						GeneralThreadPool.getInstance().execute(dead);
					} else {
						// distributeExpDropKarma(attacker);
						transok = true;
						transform(transformId);
					}
				}
				
				if (attacker instanceof L1PcInstance) {
					L1PcInstance apc = (L1PcInstance) attacker;
					if (npcid == 100623) { // 용아병의 혼령(파랑)
						L1Location newLocation = apc.getLocation().randomLocation(200, true);
						int newX = newLocation.getX();
						int newY = newLocation.getY();
						short mapId = (short) newLocation.getMapId();
						apc.dx = newX;
						apc.dy = newY;
						apc.dm = mapId;
						apc.dh = apc.getMoveState().getHeading();
						apc.setTelType(7);
						apc.sendPackets(new S_SabuTell(apc), true);
					} else if (npcid == 100624) { // 용아병의 혼령(노랑)
						if ((getMapId() >= 807 && getMapId() <= 812) || getMapId() == 1) {
							L1Location newLocation = new L1Location(apc.getX(), apc.getY(), apc.getMapId() + 1).randomLocation(200, true);
							int newX = newLocation.getX();
							int newY = newLocation.getY();
							short mapId = (short) newLocation.getMapId();
							apc.dx = newX;
							apc.dy = newY;
							apc.dm = mapId;
							apc.dh = apc.getMoveState().getHeading();
							apc.setTelType(7);
							apc.sendPackets(new S_SabuTell(apc), true);
						} // 33461 32336
					} else if (getNpcId() == 10071133 || getNpcId() == 10071233 //대흑장로 제로스 용계텔소스
							|| getNpcId() == 10071333 || getNpcId() == 10071433
							|| getNpcId() == 10071533 || getNpcId() == 10090133
							|| getNpcId() == 10090233 || getNpcId() == 10090333
							|| getNpcId() == 10090433 || getNpcId() == 10090533
							|| getNpcId() == 10072033) { // 거대 해골들
																		// (용계)
						// if(_random.nextInt(1) == 0){
						if (_random.nextInt(1000) == 100) {
							L1Location newLocation = null;
							
							if (_random.nextInt(1000) < 500) {
								newLocation = new L1Location(33459, 32338, 4);
							} else {
								switch (_random.nextInt(3)) {
									case 0:
										newLocation = new L1Location(33393, 32344, 4);
										break;
									case 1:
										newLocation = new L1Location(33263, 32402, 4);
										break;
									case 2:
										newLocation = new L1Location(33336, 32436, 4);
										break;
									
								}
							}
							
							if (newLocation != null) {
								int newX = newLocation.getX();
								int newY = newLocation.getY();
								short mapId = (short) newLocation.getMapId();
								apc.dx = newX;
								apc.dy = newY;
								apc.dm = mapId;
								apc.dh = apc.getMoveState().getHeading();
								apc.setTelType(7);
								apc.sendPackets(new S_SabuTell(apc), true);
							}
						}
						
						/** 인더용 임팩들 추가 적용 */
						} else if (npcid == 46404) {
							for (L1Object Object : L1World.getInstance().getVisibleObjects(this, 4)) {
								
								if (Object instanceof L1MonsterInstance) {
									L1MonsterInstance Mon = (L1MonsterInstance) Object;
									/** 수호탑 은 제외함  */
									if (Mon.getNpcId() == 46400) {
										continue;
									}
									
									if (Mon.getNpcId() == 46404 || Mon.getNpcId() == 46405) {
										continue;
									}
									
									if (Mon.getNpcId() >= 47220 && Mon.getNpcId() <= 47225) {
										continue;
									}
									
									if (Mon == null || Mon._destroyed || Mon.isDead()) {
										continue;
									}
									
									Mon.receiveDamage(this, 250 + _Random.nextInt(250));
									Broadcaster.wideBroadcastPacket(this, new S_DoActionGFX(this.getId(), ActionCodes.ACTION_Damage), true);
								}
							}
							
							Broadcaster.wideBroadcastPacket(this, new S_SkillSound(this.getId(), 1819), true);
						} else if (npcid == 46405) {
							for (L1Object Object : L1World.getInstance().getVisibleObjects(this, 8)) {
								if (Object instanceof L1MonsterInstance) {
									L1MonsterInstance Mon = (L1MonsterInstance) Object;
									/** 수호탑 은 제외함  */
									if (Mon.getNpcId() == 46400) {
										continue;
									}
									
									if (Mon.getNpcId() == 46404 || Mon.getNpcId() == 46405) {
										continue;
									}
									
									if (Mon.getNpcId() >= 47220 && Mon.getNpcId() <= 47225) {
										continue;
									}
									
									if (Mon == null || Mon._destroyed || Mon.isDead()) {
										continue;
									}
									
									Mon.receiveDamage(this, 500 + _Random.nextInt(250));
									Broadcaster.wideBroadcastPacket(this, new S_DoActionGFX(this.getId(), ActionCodes.ACTION_Damage), true);
								}
							}
							
							Broadcaster.wideBroadcastPacket(this, new S_SkillSound(this.getId(), 7771), true);
						}
				}
				
			} // end if (newHp <= 0 && !isDead()) {
			
			
			if (newHp > 0) {
				if (getNpcId() == 45590) {
					if (리퍼대사체크 == null) {
						리퍼대사체크 = new boolean[4];
					}
					
					int hpRatio = (newHp * 100) / getMaxHp();
					if (hpRatio <= 25 && !리퍼대사체크[2] && !리퍼대사체크[3]) {
						if (!리퍼시간체크.check(getMapId())) {
							리퍼대사체크[3] = true;
							// 리퍼가 보스로 변할 확률
							Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "네 놈들에게 특별히 극한의 공포를 느끼게 해주마!", 0), true);
						} else {
							리퍼대사체크[2] = true;
							Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "크윽..인간들 치고는 제법이구나..", 0), true);
						}
						
						리퍼대사체크[0] = true;
						리퍼대사체크[1] = true;
					} else if (hpRatio <= 50 && !리퍼대사체크[1]) {
						리퍼대사체크[1] = true;
						리퍼대사체크[0] = true;
						Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "심장을 두고 가면 용서해주겠다.으하하!", 0), true);
					} else if (hpRatio <= 75 && !리퍼대사체크[0]) {
						리퍼대사체크[0] = true;
						Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "그림리퍼님에게 드릴 제물이로구나.", 0), true);
					}
				}

				setCurrentHp(newHp);
				hide();
			}
		} else if (!isDead()) {
			if (getNpcTemplate().get_npcId() == 100837 || getNpcTemplate().get_npcId() == 100838) {// 레오
				if (getMapId() >= 2600 && getMapId() <= 2698) {
					isreodie = true;
					allTargetClear();
					_isReoTH = true;
					GeneralThreadPool.getInstance().execute(new 레오스레드(this));
				}
			} else {
				setDead(true);
				setActionStatus(ActionCodes.ACTION_Die);

				Death death = new Death(attacker, this);
				GeneralThreadPool.getInstance().execute(death);

				dead.setAttacker(attacker);
				GeneralThreadPool.getInstance().execute(dead);
			}
			
			// }
			
		}
		
	}

	
	public boolean isreodie = false;
	private boolean[] 리퍼대사체크;

	
	public static class 감시자리퍼시간체크 implements Runnable {

		private FastMap<Integer, Integer> list = new FastMap<Integer, Integer>();

		public boolean check(int map) {
			return list.get(map) != null;
		}

		public void add(int map, int id) {
			list.put(map, id);
		}

		@Override
		public void run() {
			try {
				Date day = new Date(System.currentTimeMillis());
				if (day.getSeconds() == 0 && day.getMinutes() == 0) {
					list.clear();
					GeneralThreadPool.getInstance().schedule(this, 3000);
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			GeneralThreadPool.getInstance().schedule(this, 1000);
		}

	}

	
	public void setDeath(Dead d) {
		dead = d;
	}

	
	private void recall(L1PcInstance pc) {
		if (getMapId() != pc.getMapId()) {
			return;
		}
		
		if (getLocation().getTileLineDistance(pc.getLocation()) > 4) {
			L1Location newLoc = null;
			
			for (int count = 0; count < 10; count++) {
				newLoc = getLocation().randomLocation(3, 4, false);
				
				if (CharPosUtil.isAreaAttack(this, newLoc.getX(), newLoc.getY(), newLoc.getMapId())) {
					
					if (pc instanceof L1RobotInstance) {
						L1RobotInstance rob = (L1RobotInstance) pc;
						L1Teleport.ロボットテル(rob, newLoc.getX(), newLoc.getY(), getMapId(), true);
						break;
					}
					
					L1Teleport.teleport(pc, newLoc.getX(), newLoc.getY(), getMapId(), 5);
					break;
				}
			}
		}
	}

	private void sumdel(L1SummonInstance sum) {
		if (getMapId() != sum.getMapId()) {
			return;
		}
		sum.Death(this);
	}

	
	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);

		if (getMaxHp() > getCurrentHp()) {
			startHpRegeneration();
		}
	}

	
	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);

		if (getMaxMp() > getCurrentMp()) {
			startMpRegeneration();
		}
	}
	

	/**
	 * 몬스터 죽었을 때 처리?
	 *
	 */
	class Death implements Runnable {
		L1Character _lastAttacker;
		L1MonsterInstance _Mon;

		public Death(L1Character lastAttacker, L1MonsterInstance mon) {
			_lastAttacker = lastAttacker;
			_Mon = mon;
		}

		@Override
		public void run() {
			Quset_Drop(_lastAttacker);
			setDeathProcessing(true);
			setCurrentHp(0);
			setDead(true);
			getMap().setPassable(getLocation(), true);
			
			if (getNpcId() == 100106 || (getNpcId() >= 100094 && getNpcId() <= 100097)) { // 난쟁이 부락 알, 안타방 알
				setActionStatus(ActionCodes.ACTION_Open);
				broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Open), true);
				eggDie();
			} else {
				setActionStatus(ActionCodes.ACTION_Die);
				broadcastPacket(new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
			}
			
			startChat(CHAT_TIMING_DEAD);
			distributeExpDropKarma(_lastAttacker);
			giveUbSeal();
			setDeathProcessing(false);
			setExp(0);
			setKarma(0);
			setLawful(0);
			allTargetClear();
			
			if (getNpcId() == 4036016 || getNpcId() == 4036017) { // 제브레퀴
				GeneralThreadPool.getInstance().execute(new 제브부활스레드(_Mon));
			} else {
				startDeleteTimer();
			}
			
		}
		
	}

	
	/*
	 * 32654 33000 32694 33052
	 */
	/*
	 * public void die(L1Character lastAttacker) { setDeathProcessing(true);
	 * setCurrentHp(0); setDead(true); getMap().setPassable(getLocation(),
	 * true); if(getNpcId() == 100106 || (getNpcId() >= 100094 && getNpcId() <=
	 * 100097)){ // 난쟁이 부락 알, 안타방 알 setActionStatus(ActionCodes.ACTION_Open);
	 * Broadcaster.broadcastPacket(this, new
	 * S_DoActionGFX(this.getId(),ActionCodes.ACTION_Open), true); eggDie();
	 * }else{ setActionStatus(ActionCodes.ACTION_Die);
	 * Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(),
	 * ActionCodes.ACTION_Die), true); } startChat(CHAT_TIMING_DEAD);
	 * distributeExpDropKarma(lastAttacker); giveUbSeal();
	 * setDeathProcessing(false); setExp(0); setKarma(0); setLawful(0);
	 * allTargetClear(); if(this.getNpcId() == 4036016 || this.getNpcId() ==
	 * 4036017){ // 제브레퀴 GeneralThreadPool.getInstance().execute(new
	 * 제브부활스레드(this)); }else startDeleteTimer(); }
	 */

	private void eggDie() {
		if (getNpcId() == 100106) {
			int rnd = _random.nextInt(100) + 1;
			if (rnd <= 50) {
				L1SpawnUtil.spawn3(this, 100104, 2, 0, false);
			} else {
				L1SpawnUtil.spawn3(this, 100105, 2, 0, false);
			}
		}
	}

	
	class 레오스레드 implements Runnable {
		private L1MonsterInstance mon;
		private boolean reook = false;
		private int time = 20;
		FireDragon FD = null;

		public 레오스레드(L1MonsterInstance _mon) {
			mon = _mon;
			NPCchat("$18875", 2);
		}

		/*
		 * 모르는멘트들 18632 18634
		 */
		@Override
		public void run() {
			try {

				while (!reook) {
					time--;
					if (time <= 0) {
						if (mon.getNpcId() == 100837) {// 레오1
							mon.teleport(32653, 33000, 2);
							NPCchat("$18876", 2);
							
							try {
								Thread.sleep(2000L);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}

							synchronized (GameList.FDList) {
								FD = GameList.getFD(mon.getMapId());
							}

							if (FD == null) {
								reook = true;
								return;
							}

							FireDragonSpawn.getInstance().Spawn(null, mon.getMapId(), 1);
							_door.open();
							reook = true;
							setDead(true);
							setActionStatus(ActionCodes.ACTION_Die);
							Death death = new Death(null, mon);
							GeneralThreadPool.getInstance().execute(death);
							dead.setAttacker(null);
							GeneralThreadPool.getInstance().execute(dead);
							return;
						} else if (mon.getNpcId() == 100838) {// 레오2
							mon.teleport(32693, 33052, 2);
							NPCchat("$18877", 2);
							
							try {
								Thread.sleep(2000L);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							synchronized (GameList.FDList) {
								FD = GameList.getFD(mon.getMapId());
							}
							
							if (FD == null) {
								reook = true;
								return;
							}

							FireDragonSpawn.getInstance().Spawn(null, mon.getMapId(), 2);
							_door.open();
							// die(null);
							reook = true;
							setDead(true);
							setActionStatus(ActionCodes.ACTION_Die);

							Death death = new Death(null, mon);
							GeneralThreadPool.getInstance().execute(death);

							dead.setAttacker(null);
							GeneralThreadPool.getInstance().execute(dead);

							return;
						}
					} else {
						if (mon.getNpcId() == 100837 && mon.getX() == 32653 && mon.getY() == 33000) {// 레오1
							mon.getMoveState().setHeading(2);
							Broadcaster.broadcastPacket(mon, new S_ChangeHeading(mon));
							NPCchat("$18876", 2);
							
							try {
								Thread.sleep(2000L);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							_door.open();
							synchronized (GameList.FDList) {
								FD = GameList.getFD(mon.getMapId());
							}
							
							if (FD == null) {
								reook = true;
								return;
							}

							FireDragonSpawn.getInstance().Spawn(null, mon.getMapId(), 1);
							// die(null);
							reook = true;
							setDead(true);
							setActionStatus(ActionCodes.ACTION_Die);

							Death death = new Death(null, mon);
							GeneralThreadPool.getInstance().execute(death);

							dead.setAttacker(null);
							GeneralThreadPool.getInstance().execute(dead);
							return;
						} else if (mon.getNpcId() == 100838 && mon.getX() == 32693 && mon.getY() == 33052) {// 레오2
							mon.getMoveState().setHeading(2);
							Broadcaster.broadcastPacket(mon, new S_ChangeHeading(mon));
							NPCchat("$18877", 2);
							
							try {
								Thread.sleep(2000L);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							
							_door.open();
							
							synchronized (GameList.FDList) {
								FD = GameList.getFD(mon.getMapId());
							}
							
							if (FD == null) {
								reook = true;
								return;
							}
							
							FireDragonSpawn.getInstance().Spawn(null, mon.getMapId(), 2);
							// die(null);
							reook = true;
							setDead(true);
							setActionStatus(ActionCodes.ACTION_Die);

							Death death = new Death(null, mon);
							GeneralThreadPool.getInstance().execute(death);

							dead.setAttacker(null);
							GeneralThreadPool.getInstance().execute(dead);
							return;
						}

					}

					try {
						Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	class 제브부활스레드 implements Runnable {

		private L1MonsterInstance mon;

		public 제브부활스레드(L1MonsterInstance _mon) {
			mon = _mon;
		}

		
		@Override
		public void run() {
			try {
				for (int i = 0; i < 20; ++i) {
					if (mon.getNpcId() == 4036016 || mon.getNpcId() == 4036017) {
						int ckid = 0;
						
						if (mon.getNpcId() == 4036017) {
							ckid = 4036016;
						} else if (mon.getNpcId() == 4036016) {
							ckid = 4036017;
						}
						
						if (ckid != 0 && !TKmonCK(ckid)) {
							startDeleteTimer();
							return;
						}
					}
					
					try {
						Thread.sleep(3000L);
						// Thread.sleep(1000L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				
				mon.set제브부활(true);
				mon.resurrect(mon.getMaxHp() / 2);
				if (mon.getNpcId() == 4036016) {
					Broadcaster.broadcastPacket(mon, new S_SkillSound(getId(), 7275), true);
				} else if (mon.getNpcId() == 4036017) {
					Broadcaster.broadcastPacket(mon, new S_SkillSound(getId(), 7274), true);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	/*
	 * private void die2(L1Character lastAttacker) { setDeathProcessing(true);
	 * setCurrentHp(0); setDead(true); getMap().setPassable(getLocation(),
	 * true); startChat(CHAT_TIMING_DEAD); //
	 * distributeExpDropKarma(lastAttacker); setDeathProcessing(false);
	 * setExp(0); setKarma(0); setLawful(0); allTargetClear(); int
	 * transformGfxId = getNpcTemplate().getTransformGfxId(); if (transformGfxId
	 * > 0) Broadcaster.broadcastPacket(this, new S_SkillSound(getId(),
	 * transformGfxId)); deleteMe();
	 * GeneralThreadPool.getInstance().schedule(new GiranTransTimer(this),
	 * 2000); }
	 */

	// ////////////리뉴얼안타///////
	private void dieAntharas(L1Character lastAttacker) {
		setDeathProcessing(true);
		setCurrentHp(0);
		setDead(true);
		getMap().setPassable(getLocation(), true);
		// startChat(CHAT_TIMING_DEAD);
		// distributeExpDropKarma(lastAttacker);
		setDeathProcessing(false);
		setExp(0);
		setKarma(0);
		allTargetClear();
		int transformGfxId = getNpcTemplate().getTransformGfxId();
		
		if (transformGfxId > 0) {
			Broadcaster.broadcastPacket(this, new S_SkillSound(getId(), transformGfxId), true);
		}
		
		setActionStatus(ActionCodes.ACTION_Die);
		Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
		// deleteMe();
	}

	/*
	 * private static class AntharasTransTimer extends TimerTask { L1NpcInstance
	 * _npc; private AntharasTransTimer(L1NpcInstance some) { _npc = some; }
	 * 
	 * @Override public void run() {
	 * 
	 * } }
	 */
	// /////////리뉴얼안타///////
	/*
	 * 리뉴얼 파프
	 */
	private void diePaPoo(L1Character lastAttacker) {
		setDeathProcessing(true);
		setCurrentHp(0);
		setDead(true);
		getMap().setPassable(getLocation(), true);
		setDeathProcessing(false);
		setExp(0);
		setKarma(0);
		allTargetClear();
		setActionStatus(ActionCodes.ACTION_Die);
		Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
		deleteMe();
	}

	/*
	 * private static class GiranTransTimer extends TimerTask { L1NpcInstance
	 * _npc;
	 * 
	 * private GiranTransTimer(L1NpcInstance some) { _npc = some; }
	 * 
	 * @Override public void run() { L1SpawnUtil.spawn2(_npc.getX(),
	 * _npc.getY(), (short) _npc.getMap().getId(),
	 * _npc.getNpcTemplate().getTransformId(), 0, 0, 0); } }
	 * 
	 * private boolean isGDMonster() { int id = getNpcTemplate().get_npcId();
	 * if((id >= 4037100 && id <= 4037102) || (id >= 4037200 && id <= 4037202)
	 * || (id >= 4037400 && id <= 4037403)) return true; return false; }
	 */
	// //////리뉴얼안타//////
	private boolean isAntharas() {
		int id = getNpcTemplate().get_npcId();
		if (id == 4038000 || id == 4200010) {
			return true;
		}
		return false;
	}

	// //////리뉴얼안타///////
	// //////리뉴얼파푸///////
	private boolean isPapoo() {
		int id = getNpcTemplate().get_npcId();
		if (id == 4039000 || id == 4039006) {
			return true;
		}
		return false;
	}

	private boolean ismarble() {
		int id = getNpcTemplate().get_npcId();
		if (id == 4039001) {
			return true;
		}
		return false;
	}

	private boolean ismarble2() {
		int id = getNpcTemplate().get_npcId();
		if (id == 4039002) {
			return true;
		}
		return false;
	}

	private synchronized void distributeExpDropKarma(L1Character lastAttacker) {
		if (lastAttacker == null) {
			return;
		}
		
		L1PcInstance pc = null;
		if (lastAttacker instanceof L1PcInstance) {
			pc = (L1PcInstance) lastAttacker;
		} else if (lastAttacker instanceof L1PetInstance) {
			pc = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
		} else if (lastAttacker instanceof L1SummonInstance) {
			pc = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
		}

		
		if (pc != null) {
			ArrayList<L1Character> targetList = _hateList.toTargetArrayList();
			ArrayList<Integer> hateList = _hateList.toHateArrayList();
			int exp = getExp();
			CalcExp.calcExp(pc, getId(), targetList, hateList, exp);
			if (isDead()) {
				distributeDrop(pc);
				giveKarma(pc);
			}
		} else if (lastAttacker instanceof L1EffectInstance) {
			ArrayList<L1Character> targetList = _hateList.toTargetArrayList();
			ArrayList<Integer> hateList = _hateList.toHateArrayList();
			
			if (hateList.size() != 0) {
				int maxHate = 0;
				
				for (int i = hateList.size() - 1; i >= 0; i--) {
					if (maxHate < ((Integer) hateList.get(i))) {
						maxHate = (hateList.get(i));
						lastAttacker = targetList.get(i);
					}
				}
				
				if (lastAttacker instanceof L1PcInstance) {
					pc = (L1PcInstance) lastAttacker;
				} else if (lastAttacker instanceof L1PetInstance) {
					pc = (L1PcInstance) ((L1PetInstance) lastAttacker).getMaster();
				} else if (lastAttacker instanceof L1SummonInstance) {
					pc = (L1PcInstance) ((L1SummonInstance) lastAttacker).getMaster();
				}
				
				int exp = getExp();
				CalcExp.calcExp(pc, getId(), targetList, hateList, exp);
				if (isDead()) {
					distributeDrop(pc);
					giveKarma(pc);
				}
			}
		}
	}

	
	private static Logger _log = Logger.getLogger(L1MonsterInstance.class.getName());
	boolean rare_item_drop = false;

	private void distributeDrop(L1PcInstance pc) {
		int npcId = getNpcTemplate().get_npcId();
		try {
			if (isResurrect()) {
				return;
			}
			
			if (npcId == 4200011 || npcId == 4039007 || npcId == 100014 || npcId == 145684
					|| npcId == 400016 || npcId == 400017 || npcId == 4036016
					|| npcId == 4036017) {
				int ckid = 0;
				if (npcId == 400016) {
					ckid = 400017;
				} else if (npcId == 400017) {
					ckid = 400016;
				} else if (npcId == 4036017) {
					ckid = 4036016;
				} else if (npcId == 4036016) {
					ckid = 4036017;
				}
				
				if (ckid != 0 && TKmonCK(ckid)) {
					return;
				}
				
				int time = 1;
				if (npcId == 4200011 || npcId == 4039007 || npcId == 100014 || npcId == 145684) {
					time = 10000;
				}
				
				if (rare_item_drop) {
					return;
				}
				
				rare_item_drop = true;
				GeneralThreadPool.getInstance().schedule(new dragondistri(pc), time);
			} else if (npcId == 100155) { // 바다 하피
				if (!isResurrect() && pc.isInParty()) {
					for (L1PcInstance partymember : pc.getParty().getMembers()) {
						
						if (pc.getMapId() != partymember.getMapId()) {
							continue;
						}
						
						L1ItemInstance item = partymember.getInventory().storeItem(60085, 1);
						partymember.sendPackets(new S_ServerMessage(143, getName(), item.getName())); // \f1%0이%1를 주었습니다.
					}
				}
			} else if (npcId == 100156) { // 바다 드레이크
				if (!isResurrect() && pc.isInParty()) {
					for (L1PcInstance partymember : pc.getParty().getMembers()) {
						if (pc.getMapId() != partymember.getMapId()) {
							continue;
						}
						L1ItemInstance item = partymember.getInventory().storeItem(60086, 1);
						partymember.sendPackets(new S_ServerMessage(143, getName(), item.getName())); // \f1%0이%1를 주었습니다.
					}
				}
			} else if (npcId == 100210) { // 보상 미믹
				if (!isResurrect() && pc.isInParty()) {
					for (L1PcInstance partymember : pc.getParty().getMembers()) {
						if (pc.getMapId() != partymember.getMapId()) {
							continue;
						}
						L1ItemInstance item = partymember.getInventory().storeItem(60087 + 해상전보상미믹단계, 1);
						partymember.sendPackets(new S_ServerMessage(143, getName(), item.getName())); // \f1%0이%1를 주었습니다.
					}
				}
		/*	} else if (npcId == 100157) { // 대왕 오징어
				if (!isResurrect() && pc.isInParty()) {
					for (L1PcInstance partymember : pc.getParty().getMembers()) {
						if (pc.getMapId() != partymember.getMapId())
							continue;
						L1ItemInstance item = partymember.getInventory()
								.storeItem(500022, 1);
						partymember.sendPackets(new S_ServerMessage(143,
								getName(), item.getName())); // \f1%0이%1를 주었습니다.
					}
				}*/
			} else if (npcId >= 100653 && npcId <= 100655) {// 검은기사단 부대장
				if (isResurrect()) {
					return;
				}
				
				for (L1PcInstance tempPc : L1World.getInstance().getVisiblePlayer(this, -1)) {
					if (tempPc == null) {
						continue;
					}
					L1ItemInstance item = tempPc.getInventory().storeItem(60393 + (npcId - 100653), 1);
					tempPc.sendPackets(new S_ServerMessage(143, getName(), item .getName())); // \f1%0이%1를 주었습니다.
				}
			} else if (npcId != 45640 || (npcId == 45640 && getGfxId().getTempCharGfx() == 2332)) {

				if (npcId == 100338 || npcId == 100420) { // 에르자베, 샌드웜
					L1Character[] m3list = _dropHateList.getMaxHate3Character();
					if (m3list != null) {
						L1World.getInstance().broadcastPacketToAll(new S_ServerMessage(3320, getName()
								, (m3list[0] != null ? m3list[0].getName() : "없음")
								, (m3list[1] != null ? m3list[1].getName() : "없음")
								, (m3list[2] != null ? m3list[2].getName() : "없음")));
						m3list = null;
					}
				}
				
				ArrayList<L1Character> dropTargetList = _dropHateList.toTargetArrayList();
				ArrayList<Integer> dropHateList = _dropHateList.toHateArrayList();
				
				try {
					DropTable.getInstance().dropShare(L1MonsterInstance.this, dropTargetList, dropHateList, pc);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
			
		} catch (Exception e) {
		}
		
	}

	
	class dragondistri implements Runnable {

		private L1PcInstance pc;

		public dragondistri(L1PcInstance _pc) {
			pc = _pc;
		}

		@Override
		public void run() {
			try {
				Collection<L1Object> DragonDropTargetList = L1World.getInstance().getVisibleObjects(getMapId()).values();
				ArrayList<L1PcInstance> list = new ArrayList<L1PcInstance>();
				L1HateList DragonHate = new L1HateList();
				int ran = 0;
				
				for (L1Object DUser : DragonDropTargetList) {
					if (DUser instanceof L1PcInstance) {
						ran = _random.nextInt(1000);
						DragonHate.add((L1PcInstance) DUser, ran);
						list.add((L1PcInstance) DUser);
					}
				}
				
				DragonRaidItemTable.get().insertItem(L1MonsterInstance.this, list.size());

				ArrayList<L1Character> dropTargetList = DragonHate.toTargetArrayList();
				ArrayList<Integer> dropHateList = DragonHate.toHateArrayList();
				
				try {
					DropTable.getInstance().dropShare(L1MonsterInstance.this, dropTargetList, dropHateList, pc);
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}

				// DropTable.getInstance().dropShare(L1MonsterInstance.this,
				// DragonHate, pc);
			} catch (Exception e) {
			}
		}

	}
	
	private Random _rnd = new Random(System.nanoTime());
	/**클라우디아 몹드랍*/
	private void Quset_Drop(L1Character lastAttacker) {
		if (!(lastAttacker instanceof L1PcInstance)) {
			return;
		}
		
		L1PcInstance pc = (L1PcInstance) lastAttacker;
		int npcId = getNpcId();
		int itemid = 0;
		if (npcId == 60218) { // 식량 주머니
			itemid = 60732; // 수련자의 식량 주머니
		} else if (npcId >= 60223 && npcId <= 60225) { // 뼈조각
			itemid = 60733; // 뼈조각
		} else if (npcId == 60234 || npcId == 60235 || npcId == 60236 || npcId == 60237) { // 무기
			L1QuestInfo weapon = pc.getQuestList(273);
			L1QuestInfo earthrun = pc.getQuestList(304);
			if (weapon != null && weapon.end_time == 0) {
				itemid = 60734; // 무기 도면
			} else if (earthrun != null && earthrun.end_time == 0) {
				itemid = 60746; // 대지의 원소(소)
			}
		} else if (npcId >= 100079 && npcId <= 100081) { // 이상한 뼈조각
			L1QuestInfo bone = pc.getQuestList(281);
			if (bone != null && bone.end_time == 0) {
				itemid = 60740; // 이상한 뼈조각
			}
		} else if (npcId >= 100045 && npcId <= 100050) { // 이상한 장신구
			L1QuestInfo bone = pc.getQuestList(285);
			L1QuestInfo firerun = pc.getQuestList(305);
			if (bone != null && bone.end_time == 0) {
				itemid = 60741; // 이상한 장신구
			} else if (firerun != null && firerun.end_time == 0) {
				itemid = 60750; // 불의 원소(소)
			}
		} else if (npcId == 100074 || npcId == 100062) { // 이상한 눈
			L1QuestInfo eye = pc.getQuestList(289);
			L1QuestInfo waterrun = pc.getQuestList(306);
			if (eye != null && eye.end_time == 0) {
				itemid = 60742; // 이상한 눈
			} else if (waterrun != null && waterrun.end_time == 0) {
				itemid = 60752; // 물의 원소(소)
			}
		} else if (npcId == 100069 || npcId == 100067) { // 장식용 보석
			L1QuestInfo jewel = pc.getQuestList(298);
			L1QuestInfo airrun = pc.getQuestList(307);
			if (jewel != null && jewel.end_time == 0) {
				itemid = 60743; // 장식용 보석
			} else if (airrun != null && airrun.end_time == 0) {
				itemid = 60754; // 공기의 원소(소)
			}
		} else if (npcId >= 7130 && npcId <= 7137 || npcId >= 100079 && npcId <= 100084) {
			L1QuestInfo dayquest = pc.getQuestList(361);
			if (dayquest != null && dayquest.end_time == 0) {
				itemid = 60735;
			}
		}

		if (itemid != 0) {
			if (QuestInfoTable.getInstance().getPickupItem(itemid)) {
				QuestItemTemp temp = QuestInfoTable.getInstance().getPickupInfo(itemid);
				L1QuestInfo info = pc.getQuestList(temp.quest_id);
				if (info != null && info.end_time == 0) {// 활성화 상태라면
					if (pc.getInventory().checkItemCount(itemid) < temp.count) {
						int chance = _rnd.nextInt(10000);
						if (chance < 9000) {
							L1QuestInfo dayquest = pc.getQuestList(361);
							if (dayquest != null && dayquest.end_time == 0) {
								int count = _rnd.nextInt(5) + 1;
								L1ItemInstance item = null;
								item = pc.getInventory().storeItem(itemid, count);
								ServerBasePacket pck = new S_ServerMessage(813, getNpcTemplate().get_name(), item.getLogName(), pc.getName());
								pc.sendPackets(pck);
								return;
							} else {
								L1ItemInstance item = null;
								item = pc.getInventory().storeItem(itemid, temp.count);
								ServerBasePacket pck = new S_ServerMessage(813, getNpcTemplate().get_name(), item.getLogName(), pc.getName());
								pc.sendPackets(pck);
							}
						}
					}
				}
			}
		}
	}

	
	private boolean TKmonCK(int id) {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(getMapId()).values()) {
			if (obj != null && obj instanceof L1MonsterInstance) {
				L1MonsterInstance mon = (L1MonsterInstance) obj;
				if (mon.getNpcTemplate().get_npcId() == id && !mon.isDead() && !mon._destroyed)
					return true;
			}
		}
		
		/*
		 * for(L1Object obj : L1World.getInstance().getObject()){ if(obj!= null
		 * && obj instanceof L1MonsterInstance){ L1MonsterInstance mon =
		 * (L1MonsterInstance)obj; if(mon.getNpcTemplate().get_npcId() == id &&
		 * !mon.isDead() && !mon._destroyed) return true; } }
		 */
		return false;
	}

	
	private void giveKarma(L1PcInstance pc) {
		int karma = getKarma();
		if (karma != 0) {
			int karmaSign = Integer.signum(karma);
			int pcKarmaLevel = pc.getKarmaLevel();
			int pcKarmaLevelSign = Integer.signum(pcKarmaLevel);
			if (pcKarmaLevelSign != 0 && karmaSign != pcKarmaLevelSign) {
				karma *= 5;
			}
			pc.addKarma((int) (karma * Config.RATE_KARMA));
		}
	}

	private void giveUbSeal() {
		if (getUbSealCount() != 0) {
			L1UltimateBattle ub = UBTable.getInstance().getUb(getUbId());
			if (ub != null) {
				for (L1PcInstance pc : ub.getMembersArray()) {
					if (pc != null && !pc.isDead() && !pc.isGhost() && pc.getNetConnection() != null) {
						pc.getInventory().storeItem(500209, 3);
						pc.sendPackets(new S_SystemMessage("퓨어 엘릭서 3개를 획득 하셨습니다")); 
					}
				}
			}
		}
	}

	public byte get_storeDroped() {
		return _storeDroped;
	}

	public void set_storeDroped(byte i) {
		_storeDroped = i;
	}

	private int _ubSealCount = 0;

	public int getUbSealCount() {
		return _ubSealCount;
	}

	public void setUbSealCount(int i) {
		_ubSealCount = i;
	}

	private int _ubId = 0; // UBID

	public int getUbId() {
		return _ubId;
	}

	public void setUbId(int i) {
		_ubId = i;
	}

	private void hide() {
		int npcid = getNpcTemplate().get_npcId();
		if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455) {
			if (getMaxHp() / 3 > getCurrentHp()) {
				int rnd = _random.nextInt(10);
				if (1 > rnd) {
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_SINK);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Hide), true);
					setActionStatus(13);
					Broadcaster.broadcastPacket(this, new S_NPCPack(this), true);
				}
			}
		} else if (npcid == 45682) {
			if (getMaxHp() / 3 > getCurrentHp()) {
				int rnd = _random.nextInt(50);
				if (1 > rnd) {
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_SINK);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_AntharasHide), true);
					setActionStatus(20);
					Broadcaster .broadcastPacket(this, new S_NPCPack(this), true);
				}
			}
		} else if (npcid == 45067 || npcid == 45264 || npcid == 45452
				|| npcid == 45090 || npcid == 45321 || npcid == 45445
				|| npcid == 100352) {
			if (getMaxHp() / 3 > getCurrentHp()) {
				int rnd = _random.nextInt(10);
				if (1 > rnd) {
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_FLY);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Moveup), true);
					setActionStatus(4);
					Broadcaster.broadcastPacket(this, new S_NPCPack(this), true);
				}
			}
		} else if (npcid == 45681) {
			if (getMaxHp() / 3 > getCurrentHp()) {
				int rnd = _random.nextInt(50);
				if (1 > rnd) {
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_FLY);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Moveup), true);
					setActionStatus(11);
					Broadcaster.broadcastPacket(this, new S_NPCPack(this), true);
				}
			}
		} else if (npcid == 100014) {// 린드 비오르
			if (getMapId() >= 10000 && getMapId() <= 10005) {
				if ((getMaxHp() / 2) > getCurrentHp() && !lind_fly) {
					lind_fly = true;
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_FLY);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), 13), true);
					setActionStatus(13);
					Broadcaster.broadcastPacket(this, new S_NPCPack(this), true);
				} else if ((getMaxHp() / 4) > getCurrentHp() && !lind_fly2) {
					lind_fly2 = true;
					Lind lind = LindThread.get().getLind(getMapId());
					lind.Step = 5;
					lind.Sub_Step = 0;
					allTargetClear();
					setHiddenStatus(HIDDEN_STATUS_FLY);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), 13), true);
					setActionStatus(13);
					Broadcaster.broadcastPacket(this, new S_NPCPack(this), true);
				}
			}
		} else if (npcid == 100420) { // 샌드웜
			if ((getMaxHp() / 2) > getCurrentHp() && !샌드웜_숨기) {
				샌드웜_숨기 = true;
				allTargetClear();
				setHiddenStatus(HIDDEN_STATUS_SINK);
				Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Hide), true);
				setActionStatus(13);
				Broadcaster.broadcastPacket(this, new S_NPCPack(this), true);
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						if (getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_NONE) {
							return;
						}
						
						randomWalk();
						GeneralThreadPool.getInstance().schedule(this, getSleepTime());
					}
				}, 500);
				
				int rnd = _random.nextInt(3) + 2;
				for (int i = 0; i < rnd; i++) {
					L1SpawnUtil.spawn3(this, 100421, 6, 600 * 1000, false);
				}
				
			}
		}
	}

	
	public void initHide() {
		int npcid = getNpcTemplate().get_npcId();
		if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 || npcid == 400000 || npcid == 400001) {
			int rnd = _random.nextInt(3);
			if (1 > rnd) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setActionStatus(13);
			}
		} else if (npcid == 45045 || npcid == 45126 || npcid == 45134 || npcid == 45281) {
			int rnd = _random.nextInt(3);
			if (1 > rnd) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setActionStatus(4);
			}
		} else if (npcid == 100072 || npcid == 100627) {
			int rnd = _random.nextInt(3);
			if (1 > rnd) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setActionStatus(11);
			}
		} else if (npcid == 45067 || npcid == 45264 || npcid == 45452
				|| npcid == 45090 || npcid == 45321 || npcid == 45445
				|| npcid == 100352) {
			setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_FLY);
			setActionStatus(4);
		} else if (npcid == 45681) {
			setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_FLY);
			setActionStatus(11);
		}
	}

	
	public void initHideForMinion(L1NpcInstance leader) {
		int npcid = getNpcTemplate().get_npcId();
		if (leader.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_SINK) {
			if (npcid == 45061 || npcid == 45161 || npcid == 45181 || npcid == 45455 || npcid == 400000 || npcid == 400001) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setActionStatus(13);
			} else if (npcid == 45045 || npcid == 45126 || npcid == 45134 || npcid == 45281 || npcid == 100072 || npcid == 100627) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_SINK);
				setActionStatus(4);
			}
		} else if (leader.getHiddenStatus() == L1NpcInstance.HIDDEN_STATUS_FLY) {
			if (npcid == 45067 || npcid == 45264 || npcid == 45452 || npcid == 45090 || npcid == 45321 || npcid == 45445 || npcid == 100352) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_FLY);
				setActionStatus(4);
			} else if (npcid == 45681) {
				setHiddenStatus(L1NpcInstance.HIDDEN_STATUS_FLY);
				setActionStatus(11);
			}
		}
	}
	

	@Override
	protected void transform(int transformId) {
		super.transform(transformId);
		getInventory().clearItems();
		DropTable.getInstance().setDrop(this, getInventory());
		getInventory().shuffle();
		transok = false;
	}

	private int shellCloseCount = 0;

	private boolean shellman() {
		boolean miss = false;
		if (getNpcTemplate().get_npcId() == 100003 || getNpcTemplate().get_npcId() == 100814) {
			if (shellManClose) {
				miss = true;
			} else {
				if (shellCloseCount < 2 && _random.nextInt(100) < 2) {
					shellCloseCount++;
					shellManClose = true;
					Broadcaster.broadcastPacket(this, new S_NpcChatPacket(this, "천한 인간 주제에 보기보다 쎄구나!", 0), true);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), 4), true);
					setActionStatus(4);
					Broadcaster.broadcastPacket(this, new S_CharVisualUpdate(this), true);

					miss = true;
				}
				
				int percent = (int) Math.round((double) getCurrentHp() / (double) getMaxHp() * 100);
				if (!shellHydra) {
					/** 히드라 소환 **/
					if (percent < 50) {
						shellHydra = true;
						L1SpawnUtil.spawn2(getX() - 5 + _random.nextInt(11), getY() - 5 + _random.nextInt(11), (short) getMap().getId(), 100004, 0, 0, 0);
						L1SpawnUtil.spawn2(getX() - 5 + _random.nextInt(11), getY() - 5 + _random.nextInt(11), (short) getMap().getId(), 100004, 0, 0, 0);
						L1SpawnUtil.spawn2(getX() - 5 + _random.nextInt(11), getY() - 5 + _random.nextInt(11), (short) getMap().getId(), 100004, 0, 0, 0);
					}
				} else {
					if (percent > 90) {
						shellHydra = false;
					}
				}
			}
		}
		
		return miss;
	}

	
	private static final short[] list = { 0, 4, 7, 8, 9, 10, 11, 12, 13, 25,
			26, 27, 28, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111,
			112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124,
			125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137,
			138, 139, 140, 141, 142, 143, 144, 145, 146, 147, 148, 149, 150,
			151, 152, 153, 154, 155, 156, 157, 158, 159, 160, 161, 162, 163,
			164, 165, 166, 167, 168, 169, 170, 171, 172, 173, 174, 175, 176,
			177, 178, 179, 180, 181, 182, 183, 184, 185, 186, 187, 188, 189,
			190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 200, 307, 308,
			309, 70, 303, 67, 535 };

	
	class 에르자베회오리Timer implements Runnable {
		private L1MonsterInstance _effect;

		public 에르자베회오리Timer(L1MonsterInstance effect) {
			_effect = effect;
		}

		@Override
		public void run() {
			if (_destroyed) {
				return;
			}
			
			try {
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_effect, 1)) {
					if (pc.isDead() || pc.isGhost()) {
						continue;
					}
					
					L1Location loc = new L1Location();
					if (_effect.getNpcId() == 100422) {// 에르자베 소환 회오리
						L1Map map = L1WorldMap.getInstance().getMap(list[_random.nextInt(list.length)]);
						loc.set(map.getX(), map.getY(), map.getId());
						loc = L1Location.randomRangeLocation(loc, map.getWidth(), map.getHeight(), true);
					} else {
						loc.set(32707, 33121, 4);
						loc = L1Location.randomRangeLocation(loc, 190, 583, false);
					}
					L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 6, true);
				}
				
				GeneralThreadPool.getInstance().schedule(this, 500);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	
	class 도망Timer implements Runnable {
		public 도망Timer() {
		}

		@Override
		public void run() {
			try {
				Thread.sleep(3000);
				STATUS_Escape = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	class 하이네트랩Timer implements Runnable {
		private L1MonsterInstance _effect;

		public 하이네트랩Timer(L1MonsterInstance effect) {
			_effect = effect;
		}

		@Override
		public void run() {
			try {
				if (_destroyed) {
					return;
				}
				
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_effect, 1)) {
					if (pc.isTeleport() || pc.isDead() || pc.isGhost()) {
						continue;
					}
					
					L1Location loc = new L1Location();
					loc.set(33408, 33145, 4);
					loc = L1Location.randomRangeLocation(loc, 126, 174, false);
					L1Teleport.teleport(pc, loc.getX(), loc.getY(), (short) loc.getMapId(), 6, true);

					L1NpcDeleteTimer timer = new L1NpcDeleteTimer(_effect, 1000);
					timer.begin();

					loc.set(33408, 33145, 4);
					loc = L1Location.randomRangeLocation(loc, 126, 174, false);

					L1SpawnUtil.spawn2(loc.getX(), loc.getY(), (short) 4, 100859, 1, 0, 0);
				}
				
				GeneralThreadPool.getInstance().schedule(this, 500);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	
	class 기르불 implements Runnable {
		@Override
		public void run() {
			if (_destroyed || isDead()) {
				return;
			}
			
			try {
				
				for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(
						L1MonsterInstance.this, 1)) {
					if (pc.isDead() || pc.isGhost()) {
						continue;
					}
					
					Broadcaster.broadcastPacket(L1MonsterInstance.this, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage), true);
					
					try {
						pc.receiveDamage(L1MonsterInstance.this, _random.nextInt(50) + 10, true);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
