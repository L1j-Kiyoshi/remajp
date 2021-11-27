/* Eva Pack -http://eva.gg.gg
 * 본섭 리뉴얼된 안타라스 레이드 시스템
 * Eva ShaSha
 */

package l1j.server.GameSystem.Antaras;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.ARMOR_BREAK;
import static l1j.server.server.model.skill.L1SkillId.BLESSED_ARMOR;
import static l1j.server.server.model.skill.L1SkillId.BOUNCE_ATTACK;
import static l1j.server.server.model.skill.L1SkillId.COOKING_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.COOKING_END;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.DRESS_EVASION;
import static l1j.server.server.model.skill.L1SkillId.ENCHANT_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.IllUSION_AVATAR;
import static l1j.server.server.model.skill.L1SkillId.IllUSION_DIAMONDGOLEM;
import static l1j.server.server.model.skill.L1SkillId.IllUSION_LICH;
import static l1j.server.server.model.skill.L1SkillId.IllUSION_OGRE;
import static l1j.server.server.model.skill.L1SkillId.REDUCTION_ARMOR;
import static l1j.server.server.model.skill.L1SkillId.SCALES_EARTH_DRAGON;
import static l1j.server.server.model.skill.L1SkillId.SCALES_FIRE_DRAGON;
import static l1j.server.server.model.skill.L1SkillId.SCALES_WATER_DRAGON;
import static l1j.server.server.model.skill.L1SkillId.SHADOW_ARMOR;
import static l1j.server.server.model.skill.L1SkillId.SHADOW_FANG;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.SKILLS_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.SKILLS_END;
import static l1j.server.server.model.skill.L1SkillId.SOLID_CARRIAGE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_BEGIN;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CANCLEEND;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_BARLOG;
import static l1j.server.server.model.skill.L1SkillId.STATUS_CURSE_YAHEE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_DRAGONPERL;
import static l1j.server.server.model.skill.L1SkillId.STATUS_FREEZE;
import static l1j.server.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;
import static l1j.server.server.model.skill.L1SkillId.UNCANNY_DODGE;
import static l1j.server.server.model.skill.L1SkillId.데스페라도;
import static l1j.server.server.model.skill.L1SkillId.파워그립;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.GameSystem.Antaras.AntarasRaidSystem.AntarasMsgTimer;
import l1j.server.GameSystem.Antaras.AntarasRaidSystem.antamsg;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.poison.L1ParalysisPoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;

public class AntarasRaid {

	public AntarasRaidTimer art = null;

	/*
	 * private L1Party Party1 = null; private L1Party Party2 = null; private
	 * L1Party Party3 = null; private L1Party Party4 = null;
	 */
	private final Map<Integer, Boolean> MiniBoss1 = new ConcurrentHashMap<Integer, Boolean>();
	private final Map<Integer, Boolean> MiniBoss2 = new ConcurrentHashMap<Integer, Boolean>();
	private final Map<Integer, Boolean> MiniBoss3 = new ConcurrentHashMap<Integer, Boolean>();

	public boolean MiniRoom1 = false;
	private boolean MiniRoom2 = false;
	private boolean MiniRoom3 = false;
	private boolean MiniRoom4 = false;

	private final ArrayList<L1PcInstance> _antalist = new ArrayList<L1PcInstance>();

	private int _id;
	private long _endtime;
	private boolean _isAntaras = false;
	private L1NpcInstance anta = null;
	private Random random = new Random(System.nanoTime());

	public AntarasRaid(int id) {
		_id = id;
		_endtime = System.currentTimeMillis() + 7200000;// 2시간 후
		MiniBoss1.put(1, false);
		MiniBoss1.put(2, false);
		MiniBoss1.put(3, false);
		MiniBoss1.put(4, false);
		MiniBoss2.put(1, false);
		MiniBoss2.put(2, false);
		MiniBoss2.put(3, false);
		MiniBoss2.put(4, false);
		MiniBoss3.put(1, false);
		MiniBoss3.put(2, false);
		MiniBoss3.put(3, false);
		MiniBoss3.put(4, false);
		GeneralThreadPool.getInstance().execute(new UserCheckThread());
	}

	public void timeOverRun(int type) {
		switch (type) {
		case -4:// 4번째 방 2분 오버 몹 소환
			AntarasMsgTimer room4 = new AntarasMsgTimer(_id, 4);
			GeneralThreadPool.getInstance().execute(room4);
			break;
		case -3:
			AntarasMsgTimer room3 = new AntarasMsgTimer(_id, 3);
			GeneralThreadPool.getInstance().execute(room3);
			break;
		case -2:
			AntarasMsgTimer room2 = new AntarasMsgTimer(_id, 2);
			GeneralThreadPool.getInstance().execute(room2);
			break;
		case -1:
			AntarasMsgTimer room1 = new AntarasMsgTimer(_id, 1);
			GeneralThreadPool.getInstance().execute(room1);
			break;

		case 0:// 1번방 체크 20분 타임 종료시
			break;
		case 1:// 1번방 체크 20분 타임 종료시
			break;
		case 2:// 2번방 체크 20분 타임 종료시
			break;
		case 3:// 3번방 체크 20분 타임 종료시
			break;
		case 4:// 4번째 방 2분 오버 몹 소환
			break;

		case 5:// 안타라스
			antamsg anta = new antamsg(_id, 0);
			GeneralThreadPool.getInstance().execute(anta);
			// 몹 소환
			break;
		case 6:// 안타방에 있는 사람 모두 텔시킨다
			if (isAntaras() == true) {
				antamsg anta1 = new antamsg(_id, 1);
				GeneralThreadPool.getInstance().execute(anta1);
			}
			break;
		case 7:// 1차안타 뒤질때 멘트 뿌잉
			/*
			 * try{ for (L1PcInstance player:
			 * L1World.getInstance().getAllPlayers()){ if(player.getMapId()==
			 * anta().getMapId()){ createNewItem(player, 787878, 1); //흔적 1 for
			 * (L1Object temp :
			 * L1World.getInstance().getVisibleObjects(player.getMapId
			 * ()).values()){ if(temp instanceof L1PcInstance) ((L1PcInstance)
			 * temp).sendPackets(new S_ServerMessage(813, "안타라스", "달아난 드래곤의 흔적",
			 * player.getName())); } //S_SystemMessage sm1 = new
			 * S_SystemMessage("안타라스 가 달아난 드래곤의 흔적을 주었습니다. ");
			 * //player.sendPackets(sm1); sm1=null; } } }catch(Exception e){}
			 */
			antamsg anta1 = new antamsg(_id, 2);
			GeneralThreadPool.getInstance().execute(anta1);
			break;
		case 8:// 2차안타 뒤질때 멘트 뿌잉
			/*
			 * for (L1PcInstance player: L1World.getInstance().getAllPlayers()){
			 * if(player.getMapId()== anta().getMapId()){ createNewItem(player,
			 * 787878, 1); //흔적 1 for (L1Object temp :
			 * L1World.getInstance().getVisibleObjects
			 * (player.getMapId()).values()){ if(temp instanceof L1PcInstance)
			 * ((L1PcInstance) temp).sendPackets(new S_ServerMessage(813,
			 * "안타라스", "달아난 드래곤의 흔적", player.getName())); } //S_SystemMessage
			 * sm1 = new S_SystemMessage("안타라스 가 달아난 드래곤의 흔적을 주었습니다. ");
			 * //player.sendPackets(sm1); sm1=null; } }
			 */
			antamsg anta2 = new antamsg(_id, 3);
			GeneralThreadPool.getInstance().execute(anta2);
			break;
		case 9:// 3차안타 뒤질때 멘트 뿌잉
			L1World.getInstance().broadcastServerMessage(
					"난쟁이의 외침 : 안타라스의 검은 숨결을 멈추게 한 용사들이 탄생 하였습니다.!!");
			GeneralThreadPool.getInstance().schedule(new Runnable() {
				public void run() {
					for (L1PcInstance player : L1World.getInstance()
							.getAllPlayers()) {
						if (player.getMapId() == anta().getMapId()) {
							createNewItem(player, 5000064, 1); // 흔적 1
							for (L1Object temp : L1World.getInstance()
									.getVisibleObjects(player.getMapId())
									.values()) {
								if (temp instanceof L1PcInstance)
									((L1PcInstance) temp).sendPackets(new S_ServerMessage(
											813, "안타라스", "지룡의 표식", player
													.getName()));
							
							
							}
							// S_SystemMessage sm1 = new
							// S_SystemMessage("안타라스가 죽으면서 증표를 남겼습니다.");
							// player.sendPackets(sm1); sm1=null;
						}
					}
				}
			}, 10000);
			antamsg anta3 = new antamsg(_id, 4);
			GeneralThreadPool.getInstance().execute(anta3);
			break;

		case 10: {// 안타 1차 용언
			if (anta().isParalyzed()) {
				return;
			}// 2510
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					S_DoActionGFX gfx = new S_DoActionGFX(anta().getId(), 18);
					Broadcaster.broadcastPacket(anta(), gfx);
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 5)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							L1ItemInstance weapon = pc.getWeapon();
							if (weapon != null) {
								// S_SkillSound ss = new
								// S_SkillSound(pc.getId(), 172);
								// Broadcaster.broadcastPacket(pc, ss);
								// pc.sendPackets(ss); ss=null;
								S_ServerMessage sm = new S_ServerMessage(268,
										weapon.getLogName());
								pc.sendPackets(sm);
								sm = null;
								pc.getInventory().receiveDamage(weapon, 5);
							}
							// ////////////////////////////////////////웨폰브레이크
							// ////////////////////////////
							L1ParalysisPoison.doInfection(pc, 8000, 15000);
							// ////////////////////////////////////////마비독
							// ////////////////////////////
						}
					}
					int time = SprTable.getInstance().getDirSpellSpeed(
							anta().getNpcTemplate().get_gfxid());
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;
		case 11: {// 안타 티 세토르
			if (anta().isParalyzed()) {
				return;
			}// 2510
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					S_DoActionGFX gfx1 = new S_DoActionGFX(anta().getId(), 41);
					Broadcaster.broadcastPacket(anta(), gfx1);
					gfx1 = null;
					// 스폰
					// 유저들 위치변경...
					L1SpawnUtil.spawn2(anta().getX(), anta().getY(), anta()
							.getMapId(), 4038014, 5, 0, 0);
					L1SpawnUtil.spawn2(anta().getX(), anta().getY(), anta()
							.getMapId(), 4038014, 5, 0, 0);
					L1SpawnUtil.spawn2(anta().getX(), anta().getY(), anta()
							.getMapId(), 4038014, 5, 0, 0);
					L1SpawnUtil.spawn2(anta().getX(), anta().getY(), anta()
							.getMapId(), 4038014, 5, 0, 0);
					L1SpawnUtil.spawn2(anta().getX(), anta().getY(), anta()
							.getMapId(), 4038014, 5, 0, 0);
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 10)) {
						int ax, ay, am;
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							int ran = random.nextInt(100);
							if (ran < 50) {
								ax = pc.getX();
								ay = pc.getY();
								am = pc.getMapId();
								for (L1Object obj2 : L1World.getInstance()
										.getVisibleObjects(anta(), 3)) {
									if (obj2 instanceof L1PcInstance) {
										L1PcInstance pc2 = (L1PcInstance) obj2;
										if (pc2.isDead()) {
											continue;
										}
										L1Teleport.teleport(pc, pc2.getX(), pc2
												.getY(), pc2.getMapId(), pc
												.getMoveState().getHeading(),
												true);
										L1Teleport.teleport(pc2, ax, ay,
												(short) am, pc2.getMoveState()
														.getHeading(), true);
									}
									break;
								}

							}
						}
					}
					int time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 41);
					if (time > 0)
						// anta()._queue.add(anta().calcSleepTime(time, 2));
						Thread.sleep(anta().calcSleepTime(time, 2));
					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;
		case 12: {// 안타 뮤즈삼
			if (anta().isParalyzed()) {
				return;
			}// 47 = 고함
			synchronized (anta().synchObject) {
				try {
					anta().setParalyzed(true);
					Broadcaster.broadcastPacket(anta(), new S_DoActionGFX(
							anta().getId(), 19));
					// Broadcaster.broadcastPacket(anta(), new
					// S_SkillSound(anta().getId(), 7617));
					int _shockStunDuration;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 4)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead())
								continue;
							int[] stunTimeArray = { 3000, 3500, 4000, 4500,
									5000, 5500 };
							_shockStunDuration = stunTimeArray[random
									.nextInt(stunTimeArray.length)];
							S_SkillSound ss1 = new S_SkillSound(pc.getId(),
									4434);
							pc.sendPackets(ss1);
							Broadcaster.broadcastPacket(pc, ss1);
							pc.getSkillEffectTimerSet().setSkillEffect(
									L1SkillId.SHOCK_STUN, _shockStunDuration);
							L1EffectSpawn.getInstance().spawnEffect(81162,
									_shockStunDuration, pc.getX(), pc.getY(),
									pc.getMapId());
							pc.sendPackets(new S_Paralysis(
									S_Paralysis.TYPE_STUN, true));
						}
					}
					int time = SprTable.getInstance().getNodirSpellSpeed(
							anta().getNpcTemplate().get_gfxid());
					if (time > 0)
						// anta()._queue.add(anta().calcSleepTime(time, 2));
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					Broadcaster.broadcastPacket(anta(), new S_DoActionGFX(
							anta().getId(), 20));
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 4)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead())
								continue;
							int ran = random.nextInt(700) + 200;
							pc.receiveDamage(anta(), ran, false);
						}
					}
					time = SprTable.getInstance().getMoveSpeed(
							anta().getNpcTemplate().get_gfxid(), 20);
					if (time > 0)
						// anta()._queue.add(anta().calcSleepTime(time, 2));
						Thread.sleep(anta().calcSleepTime(time, 2));
					anta().setParalyzed(false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
			break;
		case 13: {// 안타 너츠 삼
			try {
				if (anta().isParalyzed()) {
					return;
				}
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					Broadcaster.broadcastPacket(anta(), new S_DoActionGFX(
							anta().getId(), 41));
					int time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 41);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					Broadcaster.broadcastPacket(anta(), new S_DoActionGFX(
							anta().getId(), 47));
					int _shockStunDuration;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 4)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							int[] stunTimeArray = { 3000, 3500, 4000, 4500,
									5000, 5500 };
							_shockStunDuration = stunTimeArray[random
									.nextInt(stunTimeArray.length)];
							S_SkillSound ss1 = new S_SkillSound(pc.getId(),
									4434);
							pc.sendPackets(ss1);
							Broadcaster.broadcastPacket(pc, ss1);
							ss1 = null;
							pc.getSkillEffectTimerSet().setSkillEffect(
									L1SkillId.SHOCK_STUN, _shockStunDuration);
							L1EffectSpawn.getInstance().spawnEffect(81162,
									_shockStunDuration, pc.getX(), pc.getY(),
									pc.getMapId());
							S_Paralysis par = new S_Paralysis(
									S_Paralysis.TYPE_STUN, true);
							pc.sendPackets(par);
							par = null;
						}
					}
					time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 47);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					S_DoActionGFX gfx4 = new S_DoActionGFX(anta().getId(), 30);
					Broadcaster.broadcastPacket(anta(), gfx4);
					gfx4 = null;
					// S_SkillSound ss = new S_SkillSound(anta().getId(), 7361);
					// Broadcaster.broadcastPacket(anta(), ss); ss=null;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 13)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							int ran = random.nextInt(400) + 100;
							pc.receiveDamage(anta(), ran, false);
						}
					}
					time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 30);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;
		case 14: {// 안타 티프 삼
			try {
				if (anta().isParalyzed()) {
					return;
				}
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					// S_SkillSound ss1 = new S_SkillSound(anta().getId(),
					// 7617);
					// Broadcaster.broadcastPacket(anta(), ss1); ss1=null;
					Broadcaster.broadcastPacket(anta(), new S_DoActionGFX(
							anta().getId(), 19));
					int time = SprTable.getInstance().getNodirSpellSpeed(
							anta().getNpcTemplate().get_gfxid());
					int _shockStunDuration;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 4)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							int[] stunTimeArray = { 3000, 3500, 4000, 4500,
									5000, 5500 };
							_shockStunDuration = stunTimeArray[random
									.nextInt(stunTimeArray.length)];
							S_SkillSound ss3 = new S_SkillSound(pc.getId(),
									4434);
							pc.sendPackets(ss3);
							Broadcaster.broadcastPacket(pc, ss3);
							ss3 = null;
							pc.getSkillEffectTimerSet().setSkillEffect(
									L1SkillId.SHOCK_STUN, _shockStunDuration);
							L1EffectSpawn.getInstance().spawnEffect(81162,
									_shockStunDuration, pc.getX(), pc.getY(),
									pc.getMapId());
							S_Paralysis par = new S_Paralysis(
									S_Paralysis.TYPE_STUN, true);
							pc.sendPackets(par);
							par = null;
						}
					}
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					Broadcaster.broadcastPacket(anta(), new S_DoActionGFX(
							anta().getId(), 20));
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 13)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead())
								continue;
							int ran = random.nextInt(700) + 200;
							pc.receiveDamage(anta(), ran, false);
						}
					}
					time = SprTable.getInstance().getMoveSpeed(
							anta().getNpcTemplate().get_gfxid(), 20);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					S_DoActionGFX gfx4 = new S_DoActionGFX(anta().getId(), 30);
					Broadcaster.broadcastPacket(anta(), gfx4);
					gfx4 = null;
					// S_SkillSound ss2 = new S_SkillSound(anta().getId(),
					// 7361);
					// Broadcaster.broadcastPacket(anta(), ss2); ss2=null;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 13)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							int ran = random.nextInt(400) + 100;
							pc.receiveDamage(anta(), ran, false);
						}
					}
					time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 30);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;
		case 15: {// 안타 리라프
			if (anta().isParalyzed()) {
				return;
			}// 2510
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					S_DoActionGFX gfx = new S_DoActionGFX(anta().getId(), 18);
					Broadcaster.broadcastPacket(anta(), gfx);
					gfx = null;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 5)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							L1ItemInstance weapon = pc.getWeapon();
							if (weapon != null) {
								// S_SkillSound ss1 = new
								// S_SkillSound(pc.getId(), 172);
								// Broadcaster.broadcastPacket(pc, ss1);
								// pc.sendPackets(ss1); ss1 = null;
								S_ServerMessage sm = new S_ServerMessage(268,
										weapon.getLogName());
								pc.sendPackets(sm);
								sm = null;
								pc.getInventory().receiveDamage(weapon, 5);
							}
							// ////////////////////////////////////////웨폰브레이크
							// ////////////////////////////
						}
					}
					int time = SprTable.getInstance().getDirSpellSpeed(
							anta().getNpcTemplate().get_gfxid());
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;

		case 16: {// 안타 켄 로우
			if (anta().isParalyzed()) {
				return;
			}// 2510
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					S_DoActionGFX gfx = new S_DoActionGFX(anta().getId(), 18);
					Broadcaster.broadcastPacket(anta(), gfx);
					gfx = null;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 3)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							new L1SkillUse().handleCommands(null, 40055,
									pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_NORMAL, anta());
							break;
						}
					}
					int time = SprTable.getInstance().getDirSpellSpeed(
							anta().getNpcTemplate().get_gfxid());
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
			// ///////////////////////독구름... 파이어월처럼?////////////
		}
			break;
		case 17: {// 안타 티기르
			if (anta().isParalyzed()) {
				return;
			}// 2510
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 3)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							anta().getMoveState().setHeading(
									calcheading(anta().getX(), anta().getY(),
											pc.getX(), pc.getY()));
							Broadcaster.broadcastPacket(anta(),
									new S_ChangeHeading(anta()));
							Broadcaster.broadcastPacket(anta(),
									new S_DoActionGFX(anta().getId(), 5));
							int ran2 = random.nextInt(300) + 50;
							pc.receiveDamage(anta(), ran2, false);
							int time = SprTable.getInstance().getAttackSpeed(
									anta().getNpcTemplate().get_gfxid(), 5);
							if (time > 0)
								Thread.sleep(anta().calcSleepTime(time, 2));
							if (anta().isDead() || anta()._destroyed
									|| pc.isDead()) {
								break;
							}
							S_DoActionGFX gfx3 = new S_DoActionGFX(anta()
									.getId(), 19);
							Broadcaster.broadcastPacket(anta(), gfx3);
							gfx3 = null;
							int ran3 = random.nextInt(500) + 200;
							pc.receiveDamage(anta(), ran3, false);
							time = SprTable.getInstance().getNodirSpellSpeed(
									anta().getNpcTemplate().get_gfxid());
							if (time > 0)
								Thread.sleep(anta().calcSleepTime(time, 2));
							break;
						}
					}

					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;
		case 18: {// 안타 리라프
			if (anta().isParalyzed()) {
				return;
			}// 2510
			synchronized (anta().synchObject) {
				anta().setParalyzed(true);
				try {
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 3)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							anta().getMoveState().setHeading(
									calcheading(anta().getX(), anta().getY(),
											pc.getX(), pc.getY()));
							S_ChangeHeading cs = new S_ChangeHeading(anta());
							Broadcaster.broadcastPacket(anta(), cs);
							cs = null;

							S_DoActionGFX gfx2 = new S_DoActionGFX(anta()
									.getId(), 5);
							Broadcaster.broadcastPacket(anta(), gfx2);
							gfx2 = null;
							int ran2 = random.nextInt(300) + 50;
							pc.receiveDamage(anta(), ran2, false);
							int time = SprTable.getInstance().getAttackSpeed(
									anta().getNpcTemplate().get_gfxid(), 5);
							if (time > 0)
								Thread.sleep(anta().calcSleepTime(time, 2));
							if (anta().isDead() || anta()._destroyed)
								break;
							if (pc.isDead()) {
								break;
							}

							S_DoActionGFX gfx = new S_DoActionGFX(anta()
									.getId(), 12);
							Broadcaster.broadcastPacket(anta(), gfx);
							gfx = null;
							int ran = random.nextInt(300) + 50;
							pc.receiveDamage(anta(), ran, false);
							time = SprTable.getInstance().getAttackSpeed(
									anta().getNpcTemplate().get_gfxid(), 12);
							if (time > 0)
								Thread.sleep(anta().calcSleepTime(time, 2));
							if (anta().isDead() || anta()._destroyed)
								break;
							if (pc.isDead()) {
								break;
							}

							S_DoActionGFX gfx3 = new S_DoActionGFX(anta()
									.getId(), 18);
							Broadcaster.broadcastPacket(anta(), gfx3);
							gfx3 = null;
							int ran3 = random.nextInt(500) + 200;
							pc.receiveDamage(anta(), ran3, false);
							time = SprTable.getInstance().getDirSpellSpeed(
									anta().getNpcTemplate().get_gfxid());
							if (time > 0)
								Thread.sleep(anta().calcSleepTime(time, 2));
							if (anta().isDead() || anta()._destroyed)
								break;
							break;
						}
					}
				} catch (Exception e) {
				}
				anta().setParalyzed(false);
			}
		}
			break;

		case 19: {// 안타 루오 타..
			if (anta().isParalyzed()) {
				return;
			}// 2510
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					S_DoActionGFX gfx3 = new S_DoActionGFX(anta().getId(), 18);
					Broadcaster.broadcastPacket(anta(), gfx3);
					gfx3 = null;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 3)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							new L1SkillUse().handleCommands(null, 40055,
									pc.getId(), pc.getX(), pc.getY(), null, 0,
									L1SkillUse.TYPE_NORMAL, anta());
							break;
						}
					}
					int time = SprTable.getInstance().getDirSpellSpeed(
							anta().getNpcTemplate().get_gfxid());
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 3)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							Broadcaster.broadcastPacket(anta(),
									new S_DoActionGFX(anta().getId(), 19));
							int ran3 = random.nextInt(500) + 200;
							pc.receiveDamage(anta(), ran3, false);
							break;
						}
					}
					time = SprTable.getInstance().getNodirSpellSpeed(
							anta().getNpcTemplate().get_gfxid());
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;

		case 20: {// 케네시.
			if (anta().isParalyzed()) {
				return;
			}// 2510
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					S_DoActionGFX gfx3 = new S_DoActionGFX(anta().getId(), 47);
					Broadcaster.broadcastPacket(anta(), gfx3);
					gfx3 = null;
					int time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 47);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					// /////////////////////캔슬///////////////////////////
					S_DoActionGFX gfx4 = new S_DoActionGFX(anta().getId(), 18);
					Broadcaster.broadcastPacket(anta(), gfx4);
					gfx4 = null;
					boolean ck = false;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 5)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							if (pc.getResistance().getMr() < 150) {
								S_SkillSound ss1 = new S_SkillSound(pc.getId(),
										870);
								Broadcaster.broadcastPacket(pc, ss1);
								pc.sendPackets(ss1);
								ss1 = null;
								// 캔슬
								can(pc);
							}
							L1ParalysisPoison.doInfection(pc, 8000, 15000);// 마비
							if (!ck) {
								ck = true;
								new L1SkillUse().handleCommands(null, 40055,
										pc.getId(), pc.getX(), pc.getY(), null,
										0, L1SkillUse.TYPE_NORMAL, anta());
							}
						}
					}
					time = SprTable.getInstance().getDirSpellSpeed(
							anta().getNpcTemplate().get_gfxid());
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;

					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;

		case 21: {// 안타 뮤즈심<<
			if (anta().isParalyzed()) {
				return;
			}// 47 = 고함
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					S_DoActionGFX gfx2 = new S_DoActionGFX(anta().getId(), 20);
					Broadcaster.broadcastPacket(anta(), gfx2);
					gfx2 = null;
					// S_SkillSound ss1 = new S_SkillSound(anta().getId(),
					// 7617);
					// Broadcaster.broadcastPacket(anta(),ss1);
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 4)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							if (pc.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.COUNTER_MAGIC)) {
								pc.getSkillEffectTimerSet().removeSkillEffect(
										COUNTER_MAGIC);
								int castgfx = SkillsTable.getInstance()
										.getTemplate(COUNTER_MAGIC)
										.getCastGfx();
								S_SkillSound ss2 = new S_SkillSound(pc.getId(),
										castgfx);
								Broadcaster.broadcastPacket(pc, ss2);
								pc.sendPackets(ss2);
								ss2 = null;
								continue;
							}
							int ran = random.nextInt(700) + 200;
							pc.receiveDamage(anta(), ran, false);
						}
					}
					int time = SprTable.getInstance().getMoveSpeed(
							anta().getNpcTemplate().get_gfxid(), 20);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));

					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;
		case 22: {// 안타 너츠 심<<
			if (anta().isParalyzed()) {
				return;
			}
			synchronized (anta().synchObject) {
				anta().setParalyzed(true);
				try {
					int time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 47);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					S_DoActionGFX gfx5 = new S_DoActionGFX(anta().getId(), 41);
					Broadcaster.broadcastPacket(anta(), gfx5);
					gfx5 = null;
					/*
					 * S_DoActionGFX gfx3 = new S_DoActionGFX(anta().getId(),
					 * 47); Broadcaster.broadcastPacket(anta(), gfx3); gfx3 =
					 * null; S_DoActionGFX gfx4 = new
					 * S_DoActionGFX(anta().getId(), 30);
					 * Broadcaster.broadcastPacket(anta(), gfx4); gfx4 = null;
					 */
					// S_SkillSound ss1 = new S_SkillSound(anta().getId(),
					// 7361);
					// Broadcaster.broadcastPacket(anta(), ss1); ss1 = null;
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 13)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							if (pc.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.COUNTER_MAGIC)) {
								pc.getSkillEffectTimerSet().removeSkillEffect(
										COUNTER_MAGIC);
								int castgfx = SkillsTable.getInstance()
										.getTemplate(COUNTER_MAGIC)
										.getCastGfx();
								S_SkillSound ss2 = new S_SkillSound(pc.getId(),
										castgfx);
								Broadcaster.broadcastPacket(pc, ss2);
								pc.sendPackets(ss2);
								ss2 = null;
								continue;
							}
							int ran = random.nextInt(400) + 100;
							pc.receiveDamage(anta(), ran, false);
						}
					}

					time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 41);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
				} catch (Exception e) {
				}
				anta().setParalyzed(false);
			}
		}
			break;
		case 23: {// 안타 티프 심<<
			if (anta().isParalyzed()) {
				return;
			}
			try {
				synchronized (anta().synchObject) {
					anta().setParalyzed(true);
					S_DoActionGFX gfx2 = new S_DoActionGFX(anta().getId(), 20);
					Broadcaster.broadcastPacket(anta(), gfx2);
					gfx2 = null;
					int time = SprTable.getInstance().getMoveSpeed(
							anta().getNpcTemplate().get_gfxid(), 20);
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 4)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							if (pc.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.COUNTER_MAGIC)) {
								pc.getSkillEffectTimerSet().removeSkillEffect(
										COUNTER_MAGIC);
								int castgfx = SkillsTable.getInstance()
										.getTemplate(COUNTER_MAGIC)
										.getCastGfx();
								S_SkillSound ss3 = new S_SkillSound(pc.getId(),
										castgfx);
								Broadcaster.broadcastPacket(pc, ss3);
								pc.sendPackets(ss3);
								ss3 = null;
								continue;
							}
							int ran = random.nextInt(700) + 200;
							pc.receiveDamage(anta(), ran, false);
						}
					}
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					// S_SkillSound ss1 = new S_SkillSound(anta().getId(),
					// 7617);
					// Broadcaster.broadcastPacket(anta(), ss1); ss1 = null;
					/*
					 * S_DoActionGFX gfx5 = new S_DoActionGFX(anta().getId(),
					 * 41); Broadcaster.broadcastPacket(anta(), gfx5); gfx5 =
					 * null; S_DoActionGFX gfx3 = new
					 * S_DoActionGFX(anta().getId(), 47);
					 * Broadcaster.broadcastPacket(anta(), gfx3); gfx3 = null;
					 * S_DoActionGFX gfx4 = new S_DoActionGFX(anta().getId(),
					 * 30); Broadcaster.broadcastPacket(anta(), gfx4); gfx4 =
					 * null;
					 */
					Broadcaster.broadcastPacket(anta(), new S_DoActionGFX(
							anta().getId(), 30));
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(anta(), 13)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isDead()) {
								continue;
							}
							if (pc.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.COUNTER_MAGIC)) {
								pc.getSkillEffectTimerSet().removeSkillEffect(
										COUNTER_MAGIC);
								int castgfx = SkillsTable.getInstance()
										.getTemplate(COUNTER_MAGIC)
										.getCastGfx();
								S_SkillSound ss4 = new S_SkillSound(pc.getId(),
										castgfx);
								Broadcaster.broadcastPacket(pc, ss4);
								pc.sendPackets(ss4);
								ss4 = null;
								continue;
							}
							int ran = random.nextInt(400) + 100;
							pc.receiveDamage(anta(), ran, false);
						}
					}
					time = SprTable.getInstance().getAttackSpeed(
							anta().getNpcTemplate().get_gfxid(), 30);
					if (time > 0)
						Thread.sleep(anta().calcSleepTime(time, 2));
					if (anta().isDead() || anta()._destroyed)
						return;
					anta().setParalyzed(false);
				}
			} catch (Exception e) {
			}
		}
			break;
		}
	}

	private boolean createNewItem(L1PcInstance pc, int item_id, int count) {
		L1ItemInstance item = ItemTable.getInstance().createItem(item_id);
		if (item != null) {
			item.setCount(count);
			if (pc.getInventory().checkAddItem(item, count) == L1Inventory.OK) {
				pc.getInventory().storeItem(item);
			} else { // 가질 수 없는 경우는 지면에 떨어뜨리는 처리의 캔슬은 하지 않는다(부정 방지)
				L1World.getInstance()
						.getInventory(pc.getX(), pc.getY(), pc.getMapId())
						.storeItem(item);
			}
			// S_ServerMessage ss = new S_ServerMessage(403, item.getLogName());
			// pc.sendPackets(ss); // %0를 손에 넣었습니다.
			// ss = null;
			return true;
		} else {
			return false;
		}
	}

	private boolean isNotCancelable(int skillNum) {
		return skillNum == ENCHANT_WEAPON || skillNum == BLESSED_ARMOR
				|| skillNum == ABSOLUTE_BARRIER || skillNum == ADVANCE_SPIRIT
				|| skillNum == SHOCK_STUN || skillNum == 파워그립
				|| skillNum == 데스페라도 || skillNum == STATUS_UNDERWATER_BREATH
				|| skillNum == SHADOW_FANG || skillNum == REDUCTION_ARMOR
				|| skillNum == SOLID_CARRIAGE || skillNum == COUNTER_BARRIER
				|| skillNum == SHADOW_ARMOR || skillNum == ARMOR_BREAK
				|| skillNum == DRESS_EVASION || skillNum == UNCANNY_DODGE
				|| skillNum == SCALES_EARTH_DRAGON 
				|| skillNum == SCALES_WATER_DRAGON
				|| skillNum == SCALES_FIRE_DRAGON || skillNum == BOUNCE_ATTACK
				|| skillNum == IllUSION_OGRE || skillNum == IllUSION_LICH
				|| skillNum == IllUSION_DIAMONDGOLEM
				|| skillNum == IllUSION_AVATAR;
	}

	int 캔슬강제삭제[] = { STATUS_CURSE_BARLOG, STATUS_CURSE_YAHEE, STATUS_DRAGONPERL };

	public void can(L1PcInstance pc) {
		if (pc.getResistance().getMr() >= 150) {
			return;
		}
		for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
			if (isNotCancelable(skillNum) && !pc.isDead()) {
				continue;
			}
			pc.getSkillEffectTimerSet().removeSkillEffect(skillNum);
		}
		for (int i = 0; i < 캔슬강제삭제.length; i++) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(캔슬강제삭제[i]))
				pc.getSkillEffectTimerSet().removeSkillEffect(캔슬강제삭제[i]);
		}
		for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
			if (isNotCancelable(skillNum) && !pc.isDead()) {
				continue;
			}
			pc.getSkillEffectTimerSet().removeSkillEffect(skillNum);
		}

		pc.curePoison();
		pc.cureParalaysis();

		for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
			if (isNotCancelable(skillNum) && !pc.isDead()) {
				continue;
			}
			pc.getSkillEffectTimerSet().removeSkillEffect(skillNum);
		}

		L1PolyMorph.undoPoly(pc);
		S_CharVisualUpdate cvu = new S_CharVisualUpdate(pc);
		pc.sendPackets(cvu);
		Broadcaster.broadcastPacket(pc, cvu);
		cvu = null;

		if (pc.getHasteItemEquipped() > 0) {
			pc.getMoveState().setMoveSpeed(0);
			S_SkillHaste sh = new S_SkillHaste(pc.getId(), 0, 0);
			pc.sendPackets(sh);
			Broadcaster.broadcastPacket(pc, sh);
			sh = null;
		}
		if (pc != null && pc.isInvisble()) {
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.INVISIBILITY)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(
						L1SkillId.INVISIBILITY);
				S_Invis iv = new S_Invis(pc.getId(), 0);
				pc.sendPackets(iv);
				Broadcaster.broadcastPacket(pc, iv);
				S_Sound ss = new S_Sound(147);
				pc.sendPackets(ss);
				iv = null;
				ss = null;

				S_RemoveObject iv2 = new S_RemoveObject(pc.getId());
				pc.sendPackets(iv2);
				Broadcaster.broadcastPacket(pc, iv2);
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.BLIND_HIDING)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(
						L1SkillId.BLIND_HIDING);
				S_Invis iv = new S_Invis(pc.getId(), 0);
				pc.sendPackets(iv);
				Broadcaster.broadcastPacket(pc, iv);
				iv = null;

				S_RemoveObject iv2 = new S_RemoveObject(pc.getId());
				pc.sendPackets(iv2);
				Broadcaster.broadcastPacket(pc, iv2);
			}
		}
		if (pc.getSkillEffectTimerSet()
				.hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
			int timeSec = pc.getSkillEffectTimerSet().getSkillEffectTimeSec(
					STATUS_UNDERWATER_BREATH);
			pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), timeSec), true);
		}
		pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_FREEZE);
		if (pc.is에틴인형()) {
			pc.getMoveState().setMoveSpeed(1);
			S_SkillHaste sh1 = new S_SkillHaste(pc.getId(), 1, -1);
			pc.sendPackets(sh1);
			sh1 = null;
			S_SkillHaste sh2 = new S_SkillHaste(pc.getId(), 1, 0);
			Broadcaster.broadcastPacket(pc, sh2);
			sh2 = null;
		}
	}

	/**
	 * 해당하는 좌표로 방향을 전환할때 사용.
	 */
	public int calcheading(int myx, int myy, int tx, int ty) {
		if (tx > myx && ty > myy) {
			return 3;
		} else if (tx < myx && ty < myy) {
			return 7;
		} else if (tx > myx && ty == myy) {
			return 2;
		} else if (tx < myx && ty == myy) {
			return 6;
		} else if (tx == myx && ty < myy) {
			return 0;
		} else if (tx == myx && ty > myy) {
			return 4;
		} else if (tx < myx && ty > myy) {
			return 5;
		} else {
			return 1;
		}
	}

	/** 안타라스레어에 진입한 유저 수를 가져온다 */
	public int countLairUser() {
		return _antalist.size();
	}

	/** 안타라스레어에 진입할 유저를 넣는다 */
	public void addLairUser(L1PcInstance pc) {
		if (_antalist.contains(pc)) {
			return;
		}
		_antalist.add(pc);
	}

	/** 안타라스레어에 진입할 유저를 넣는다 */
	public void clLairUser() {
		_antalist.clear();
	}

	/** 안타 오브젝트를 돌려준다 */
	public L1NpcInstance anta() {
		return anta;
	}

	/** 안타 오브젝트를 세팅 한다. */
	public void setanta(L1NpcInstance npc) {
		anta = npc;
	}

	/** 안타라스가 떴는지 알려준다 */
	public boolean isAntaras() {
		return _isAntaras;
	}

	/** 안타라스가 떴는지 설정한다 */
	public void setAntaras(boolean flag) {
		_isAntaras = flag;
	}

	public int getAntaId() {
		return _id;
	}

	public long getEndTime() {
		return _endtime;
	}

	private ArrayList<L1NpcInstance> MonList = new ArrayList<L1NpcInstance>();

	private int MonsterCount1 = 200;
	private int MonsterCount2 = 200;
	private int MonsterCount3 = 200;
	private int MonsterCount4 = 200;

	public synchronized void MonsterCount(int type) {
		switch (type) {
		case 1:
			MonsterCount1--;
			if (MonsterCount1 < 1) {
				if (MiniBoss3.get(1) == false) {
					BossSpawn(3, 1);
					MiniBoss3.put(1, true);
				}
			} else if (MonsterCount1 < 81) {
				if (MiniBoss2.get(1) == false) {
					BossSpawn(2, 1);
					MiniBoss2.put(1, true);
				}
			} else if (MonsterCount1 < 161) {
				if (MiniBoss1.get(1) == false) {
					BossSpawn(1, 1);
					MiniBoss1.put(1, true);
				}
			}
			break;
		case 2:
			MonsterCount2--;
			if (MonsterCount2 < 1) {
				if (MiniBoss3.get(2) == false) {
					BossSpawn(3, 2);
					MiniBoss3.put(2, true);
				}
			} else if (MonsterCount2 < 81) {
				if (MiniBoss2.get(2) == false) {
					BossSpawn(2, 2);
					MiniBoss2.put(2, true);
				}
			} else if (MonsterCount2 < 161) {
				if (MiniBoss1.get(2) == false) {
					BossSpawn(1, 2);
					MiniBoss1.put(2, true);
				}
			}
			break;
		case 3:
			MonsterCount3--;
			if (MonsterCount3 < 1) {
				if (MiniBoss3.get(3) == false) {
					BossSpawn(3, 3);
					MiniBoss3.put(3, true);
				}
			} else if (MonsterCount3 < 81) {
				if (MiniBoss2.get(3) == false) {
					BossSpawn(2, 3);
					MiniBoss2.put(3, true);
				}
			} else if (MonsterCount3 < 161) {
				if (MiniBoss1.get(3) == false) {
					BossSpawn(1, 3);
					MiniBoss1.put(3, true);
				}
			}
			break;
		case 4:
			MonsterCount4--;
			if (MonsterCount4 < 1) {
				if (MiniBoss3.get(4) == false) {
					BossSpawn(3, 4);
					MiniBoss3.put(4, true);
				}
			} else if (MonsterCount4 < 81) {
				if (MiniBoss2.get(4) == false) {
					BossSpawn(2, 4);
					MiniBoss2.put(4, true);
				}
			} else if (MonsterCount4 < 161) {
				if (MiniBoss1.get(4) == false) {
					BossSpawn(1, 4);
					MiniBoss1.put(4, true);
				}
			}
			break;
		}
	}

	private int 코마a = 4038003;
	private int 코마b = 4038004;
	private int 코마c = 4038005;

	private void BossSpawn(int step, int type) {
		switch (type) {
		case 1:
			switch (step) {
			case 1:
				L1SpawnUtil.spawn2(32636, 32799, (short) _id, 코마a, 0, 0, 0);
				break;
			case 2:
				L1SpawnUtil.spawn2(32655, 32849, (short) _id, 코마b, 0, 0, 0);
				break;
			case 3:
				L1SpawnUtil.spawn2(32649, 32921, (short) _id, 코마c, 0, 0, 0);
				break;
			}
			break;
		case 2:
			switch (step) {
			case 1:
				L1SpawnUtil.spawn2(32893, 32607, (short) _id, 코마a, 0, 0, 0);
				break;
			case 2:
				L1SpawnUtil.spawn2(32911, 32657, (short) _id, 코마b, 0, 0, 0);
				break;
			case 3:
				L1SpawnUtil.spawn2(32905, 32905, (short) _id, 코마c, 0, 0, 0);
				break;
			}
			break;
		case 3:
			switch (step) {
			case 1:
				L1SpawnUtil.spawn2(32892, 32799, (short) _id, 코마a, 0, 0, 0);
				break;
			case 2:
				L1SpawnUtil.spawn2(32911, 32849, (short) _id, 코마b, 0, 0, 0);
				break;
			case 3:
				L1SpawnUtil.spawn2(32905, 32921, (short) _id, 코마c, 0, 0, 0);
				break;
			}
			break;
		case 4:
			switch (step) {
			case 1:
				L1SpawnUtil.spawn2(32765, 32799, (short) _id, 코마a, 0, 0, 0);
				break;
			case 2:
				L1SpawnUtil.spawn2(32783, 32849, (short) _id, 코마b, 0, 0, 0);
				break;
			case 3:
				L1SpawnUtil.spawn2(32777, 32921, (short) _id, 코마c, 0, 0, 0);
				break;
			}
			break;
		}

		S_SystemMessage sm = new S_SystemMessage(
				"난쟁이의 외침 : 문지기가 나타났습니다.! 그를 물리쳐야 이곳을 통과할 수 있습니다.");
		// for(L1PcInstance pc : getParty(type).getMembers()){
		for (L1PcInstance pc : getMembers()) {
			if (pc.getMapId() != _id) {
				continue;
			}
			pc.sendPackets(sm);
		}
		sm = null;
	}

	public void AddMon(L1NpcInstance npc) {
		MonList.add(npc);
	}

	public void MiniBossReset() {
		for (L1NpcInstance mon : MonList) {
			if (mon.isDead()) {
				continue;
			}
			mon.deleteMe();
		}
		MonList.clear();
	}

	/** 파티 관련 */
	/*
	 * public L1Party getParty(int type){ switch (type) { case 1:return
	 * Party1;case 2:return Party2; case 3:return Party3;case 4:return Party4; }
	 * return null; }
	 */

	public boolean Check(int type) {
		switch (type) {
		case 1:
			return MiniRoom1;
		case 2:
			return MiniRoom2;
		case 3:
			return MiniRoom3;
		case 4:
			return MiniRoom4;
		}
		return false;
	}

	/*
	 * public void setParty(L1Party party, int type){ switch (type) { case
	 * 1:Party1 = party; MiniRoom1 = true; break; case 2:Party2 = party;
	 * MiniRoom2 = true; break; case 3:Party3 = party; MiniRoom3 = true; break;
	 * case 4:Party4 = party; MiniRoom4 = true; break; } }
	 */

	private ArrayList<L1PcInstance> _list = new ArrayList<L1PcInstance>();

	public void addMember(L1PcInstance pc) {
		synchronized (_list) {
			if (!_list.contains(pc)) {
				// System.out.println("추가 > "+pc.getName());
				_list.add(pc);
			}
		}
	}

	public void removeMember(L1PcInstance pc) {
		synchronized (_list) {
			if (_list.contains(pc)) {
				// System.out.println("삭제 > "+pc.getName());
				_list.remove(pc);
			}
		}
	}

	public ArrayList<L1PcInstance> getMembers() {
		return _list;
	}

	private byte MembermapckCount = 0;
	public boolean threadOn = true;

	class UserCheckThread implements Runnable {

		public void run() {
			int size = 0;
			while (threadOn) {
				try {
					L1PcInstance[] list = null;
					synchronized (_list) {
						if ((size = _list.size()) > 0) {
							list = _list.toArray(new L1PcInstance[size]);
						}
					}
					if (list != null && size > 0) {
						boolean ck2 = false;
						for (L1PcInstance pc : list) {
							if (pc == null
									|| pc.getMapId() != _id
									|| pc.getNetConnection() == null
									|| !((pc.getX() >= 32589 && pc.getX() <= 32687) && (pc
											.getY() >= 32781 && pc.getY() <= 32946))) {
								ck2 = true;
							}
						}
						if (ck2) {
							if (MembermapckCount++ > 3) {
								for (L1PcInstance pc : list) {
									if (pc == null
											|| pc.getMapId() != _id
											|| pc.getNetConnection() == null
											|| !((pc.getX() >= 32589 && pc
													.getX() <= 32687) && (pc
													.getY() >= 32781 && pc
													.getY() <= 32946)))
										removeMember(pc);
								}
								MembermapckCount = 0;
							}
						}
					}
					list = null;
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

	}

}
