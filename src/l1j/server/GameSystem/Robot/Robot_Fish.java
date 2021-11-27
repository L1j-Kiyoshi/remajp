package l1j.server.GameSystem.Robot;

import static l1j.server.server.model.skill.L1SkillId.HASTE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class Robot_Fish {

	private static Random _random = new Random(System.nanoTime());
	private static Queue<L1RobotInstance> _queue;
	private static Robot_Fish _instance;

	public static Robot_Fish getInstance() {
		if (_instance == null) {
			_instance = new Robot_Fish();
		}
		return _instance;
	}

	public Robot_Fish() {
		_queue = new ConcurrentLinkedQueue<L1RobotInstance>();
		loadbot();
		ArrayList<L1RobotInstance> list = new ArrayList<L1RobotInstance>();
		while (_queue.size() > 0) {
			L1RobotInstance ro = _queue.poll();
			list.add(ro);
		}
		Collections.shuffle(list);
		for (L1RobotInstance ro : list) {
			_queue.offer(ro);
		}
		GeneralThreadPool.getInstance().schedule(new GoldBootsMent(), 10000);
	}

	class GoldBootsMent implements Runnable {
		private long time = 0;

		@Override
		public void run() {
			try {

				// TODO 자동 생성된 메소드 스텁
				if (!GMCommands.fishBot) {
					GeneralThreadPool.getInstance().schedule(this, 10000);
					return;
				}
				if (time == 0) {// 1000 1초
								// 10000 10초
								// 100000 100초 12~18분 //6~12분
					time = System.currentTimeMillis() + 7200000
							+ _random.nextInt(3600000);
				} else {
					if (System.currentTimeMillis() > time) {
						for (L1Object temp : L1World.getInstance()
								.getVisibleObjects(5490).values()) {
							if (temp instanceof L1PcInstance) {
								L1PcInstance tp = (L1PcInstance) temp;
								tp.sendPackets(new S_PacketBox(
										S_PacketBox.GREEN_MESSAGE,
										"누군가가 황금 장화를 습득하였습니다!"), true);
							}
						}
						time = System.currentTimeMillis() + 3600000
								+ _random.nextInt(3600000);
					}
				}

				GeneralThreadPool.getInstance().schedule(this, 5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void clanSetting(L1RobotInstance temp_bot) {
		synchronized (_queue) {
			for (int i = 0; i < _queue.size(); i++) {
				L1RobotInstance bot = _queue.poll();
				if (bot == null)
					continue;
				if (bot.getName().equalsIgnoreCase(temp_bot.getName())) {
					bot.setClanid(temp_bot.getClanid());
					bot.setClanJoinDate(temp_bot.getClanJoinDate());
					bot.setClanname(temp_bot.getClanname());
					bot.setClanRank(temp_bot.getClanRank());
				}
				_queue.offer(bot);
			}
		}
	}

	private void put(L1RobotInstance bot) {
		synchronized (_queue) {
			_queue.offer(bot);
		}
	}

	public void start_spawn() {
		for (int i = 0; i < 650; i++) {
			L1RobotInstance bot = _queue.poll();
			if (bot == null) {
				continue;
			}
			GeneralThreadPool.getInstance().schedule(new botVisible(bot),
					60000 * (_random.nextInt(60) + 1));
		}
	}

	private void spawn() {
		if (!GMCommands.fishBot)
			return;
		synchronized (_queue) {
			L1RobotInstance bot = _queue.poll();
			if (bot == null)
				return;
			GeneralThreadPool.getInstance().schedule(new botVisible(bot),
					60000 * (_random.nextInt(2) + 1));
		}
	}

	private void direct_spawn() {
		if (!GMCommands.fishBot)
			return;
		synchronized (_queue) {
			L1RobotInstance bot = _queue.poll();
			if (bot == null)
				return;
			GeneralThreadPool.getInstance().schedule(new botVisible(bot),
					1 * (_random.nextInt(2) + 1));
		}
	}

	private static boolean spawning = false;

	class botVisible implements Runnable {
		private L1RobotInstance bot;
		private byte spawn_type = 0;
		private long time = 0;

		public botVisible(L1RobotInstance bot) {
			this.bot = bot;
		}

		@Override
		public void run() {
			// TODO 자동 생성된 메소드 스텁
			try {
				if (spawn_type == 1) {
					if (System.currentTimeMillis() >= time) {
						spawn_type++;
						GeneralThreadPool.getInstance().execute(this);
						return;
					} else {
						if (bot.isDead() || bot._스레드종료) {
							spawn_type = 3;
							GeneralThreadPool.getInstance().schedule(this,
									10000 + _random.nextInt(20000));
							// GeneralThreadPool.getInstance().schedule(this,
							// 100);
							return;
						}
						GeneralThreadPool.getInstance().schedule(this, 100);
						return;
					}
				} else if (spawn_type == 3) {
					if (bot.getClanid() != 0) {
						bot.getClan().removeOnlineClanMember(bot.getName());
					}
					bot.setDead(false);
					L1World.getInstance().removeVisibleObject(bot);
					L1World.getInstance().removeObject(bot);
					for (L1PcInstance pc : L1World.getInstance()
							.getRecognizePlayer(bot)) {
						pc.getNearObjects().removeKnownObject(bot);
						pc.sendPackets(new S_RemoveObject(bot), true);
					}
					Robot.Doll_Delete(bot);
					bot.getNearObjects().removeAllKnownObjects();
					bot.낚시봇 = false;
					bot._스레드종료 = true;
					bot.updateconnect(false);
					put(bot);
					spawn();
					return;
				} else if (spawn_type == 0) {
					if (spawning) {
						GeneralThreadPool.getInstance().schedule(this, 10000);
						// GeneralThreadPool.getInstance().schedule(this, 100);
						return;
					}
					spawning = true;
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							spawning = false;
						}
					}, (30 + _random.nextInt(30)) * 1000);

					/*
					 * GeneralThreadPool.getInstance().schedule(new Runnable(){
					 * 
					 * @Override public void run() { spawning = false; }
					 * 
					 * }, (100));//빠르게
					 */
					L1PcInstance rob = L1World.getInstance().getPlayer(
							bot.getName());
					if (rob != null) {
						put(bot);
						direct_spawn();
						return;
					}
					if (!GMCommands.fishBot) {
						put(bot);
						return;
					}
					bot.setX(32766);
					bot.setY(32831);
					bot.setMap((short) 5490);
					bot.getGfxId().setTempCharGfx(bot.getGfxId().getGfxId());
					bot.낚시봇 = true;
					bot.낚시좌표 = null;
					bot.낚시이동좌표 = null;
					bot.낚시중 = false;
					bot.낚시종료 = false;
					bot.낚시텔 = false;
					bot._스레드종료 = false;
					bot.getMoveState().setHeading(_random.nextInt(8));
					bot.getMoveState().setMoveSpeed(1);
					bot.getSkillEffectTimerSet().setSkillEffect(HASTE,
							(_random.nextInt(400) + 1700) * 1000);
					if (bot.isKnight() || bot.isCrown()) {
						bot.getSkillEffectTimerSet().setSkillEffect(
								L1SkillId.STATUS_BRAVE,
								(_random.nextInt(600) + 400) * 1000);
						bot.getMoveState().setBraveSpeed(1);
					} else if (bot.isElf()) {
						bot.getSkillEffectTimerSet().setSkillEffect(
								L1SkillId.STATUS_ELFBRAVE,
								(_random.nextInt(600) + 400) * 1000);
						bot.getMoveState().setBraveSpeed(1);
					} else if (bot.isDragonknight()) {
						bot.getSkillEffectTimerSet().setSkillEffect(
								L1SkillId.BLOOD_LUST,
								(_random.nextInt(300) + 200) * 1000);
						bot.getMoveState().setBraveSpeed(1);
					} else if (bot.isDarkelf()) {
						bot.getSkillEffectTimerSet().setSkillEffect(
								L1SkillId.MOVING_ACCELERATION,
								(_random.nextInt(600) + 400) * 1000);
						bot.getMoveState().setBraveSpeed(4);
					}
					L1Clan clan = L1World.getInstance().getClan(
							bot.getClanname());
					if (clan != null) {
						if (bot.getClanid() == clan.getClanId()
								&& // 크란을 해산해, 재차, 동명의 크란이 창설되었을 때의 대책
								bot.getClanname()
										.toLowerCase()
										.equals(clan.getClanName()
												.toLowerCase())) {
							clan.addOnlineClanMember(bot.getName(), bot);
							for (L1PcInstance clanMember : clan
									.getOnlineClanMember()) {
								if (clanMember.getId() != bot.getId()) {
									// 지금, 혈맹원의%0%s가 게임에 접속했습니다.
									clanMember.sendPackets(new S_SystemMessage(
											clanMember, "혈맹원 " + bot.getName()
													+ "님께서 방금 게임에 접속하셨습니다."),
											true);
								}
							}
						}
					}

					// Robot.poly(bot);

					L1World.getInstance().storeObject(bot);
					L1World.getInstance().addVisibleObject(bot);

					Robot.clan_join(bot);
					Robot.Doll_Spawn(bot);

					bot.updateconnect(true);
					bot.startAI();
					spawn_type++;
					// time = 3600000+(_random.nextInt(3600000)) +
					// System.currentTimeMillis();
					time = 72000000 + (_random.nextInt(3600000))
							+ System.currentTimeMillis();
					// time = System.currentTimeMillis();
					GeneralThreadPool.getInstance().schedule(this, 1);
				} else if (spawn_type == 4) {
					if (!bot.낚시텔) {
						GeneralThreadPool.getInstance().schedule(this, 2000);
						return;
					}
					bot.setTeleport(true);
					S_SkillSound ss = new S_SkillSound(bot.getId(), 169);
					S_RemoveObject ro = new S_RemoveObject(bot);
					for (L1PcInstance pc : L1World.getInstance()
							.getRecognizePlayer(bot)) {
						pc.sendPackets(ss);
						pc.sendPackets(ro);
						pc.getNearObjects().removeKnownObject(bot);
					}
					L1World.getInstance().moveVisibleObject(bot, 32750, 32809,
							(short) 39);
					bot.setX(32750);
					bot.setY(32809);
					bot.setMap((short) 39);
					bot.setTeleport(false);

					Thread.sleep(2000);

					if (bot.getClanid() != 0) {
						bot.getClan().removeOnlineClanMember(bot.getName());
					}
					L1World.getInstance().removeVisibleObject(bot);
					L1World.getInstance().removeObject(bot);
					for (L1PcInstance pc : L1World.getInstance()
							.getRecognizePlayer(bot)) {
						pc.getNearObjects().removeKnownObject(bot);
						pc.sendPackets(new S_RemoveObject(bot), true);
					}
					Robot.Doll_Delete(bot);
					bot.getNearObjects().removeAllKnownObjects();
					bot.낚시봇 = false;
					bot._스레드종료 = true;
					bot.updateconnect(false);
					put(bot);
					spawn();
				} else if (spawn_type == 2) {
					bot.setFishingTime(0);
					bot.setFishingReady(false);
					bot.setFishing(false);
					bot.setFishingItem(null);
					Broadcaster.broadcastPacket(bot,
							new S_CharVisualUpdate(bot));
					bot.낚시종료 = true;
					spawn_type = 4;
					GeneralThreadPool.getInstance().schedule(this, 2000);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private void loadbot() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM robots");
			rs = pstm.executeQuery();
			while (rs.next()) {
				L1RobotInstance newPc = new L1RobotInstance();
				newPc.setId(ObjectIdFactory.getInstance().nextId());
				newPc.setAccountName("");
				newPc.setName(rs.getString("name"));
				switch (rs.getInt("step")) {
				case 0:
					int level = _random.nextInt(9) + 52;
					newPc.setHighLevel(level);
					newPc.setLevel(level);
					newPc.setExp(ExpTable.getExpByLevel(level)
							+ _random.nextInt(ExpTable
									.getNeedExpNextLevel(level)));
					// newPc.setHighLevel(50);newPc.setLevel(50);
					// newPc.setExp(ExpTable.getExpByLevel(50));
					newPc.getAC().setAc(-60);
					newPc.addHitup(30);
					newPc.addBowHitup(30);
					newPc.addDamageReductionByArmor(10);
					break;
				case 1:
					newPc.setHighLevel(52);
					newPc.setLevel(52);
					newPc.getAC().setAc(-65);
					newPc.setExp(ExpTable.getExpByLevel(52));
					newPc.addHitup(30);
					newPc.addBowHitup(30);
					newPc.addDamageReductionByArmor(10);
					break;
				case 2:
					newPc.setHighLevel(60);
					newPc.setLevel(60);
					newPc.setExp(ExpTable.getExpByLevel(60));
					newPc.getAC().setAc(-70);
					newPc.addHitup(35);
					newPc.addBowHitup(35);
					newPc.addDamageReductionByArmor(13);
					break;
				case 3:
					newPc.setHighLevel(65);
					newPc.setLevel(65);
					newPc.setExp(ExpTable.getExpByLevel(65));
					newPc.getAC().setAc(-75);
					newPc.addHitup(40);
					newPc.addBowHitup(40);
					newPc.addDamageReductionByArmor(15);
					break;
				case 4:
					newPc.setHighLevel(70);
					newPc.setLevel(70);
					newPc.setExp(ExpTable.getExpByLevel(70));
					newPc.getAC().setAc(-80);
					newPc.addHitup(45);
					newPc.addBowHitup(45);
					newPc.addDamageReductionByArmor(17);
					break;
				case 5:
					newPc.setHighLevel(75);
					newPc.setLevel(75);
					newPc.setExp(ExpTable.getExpByLevel(75));
					newPc.getAC().setAc(-85);
					newPc.addHitup(50);
					newPc.addBowHitup(50);
					newPc.addDamageReductionByArmor(20);
					break;
				default:
					newPc.setHighLevel(1);
					newPc.setLevel(1);
					newPc.setExp(ExpTable.getExpByLevel(1));
					newPc.getAC().setAc(-50);
					newPc.addHitup(15);
					newPc.addBowHitup(15);
					newPc.addDamageReductionByArmor(5);
					break;
				}/*
				 * switch (rs.getInt("lawful")) { case
				 * 0:newPc.setLawful(0);break; case
				 * 1:newPc.setLawful(32767);break; case
				 * 2:newPc.setLawful(-32768);break;
				 * default:newPc.setLawful(32767);break; }
				 */
				int random = _random.nextInt(1000);
				if (random > 350)
					newPc.setLawful(32767);
				else if (random > 50)
					newPc.setLawful(0);
				else
					newPc.setLawful(-32768);
				newPc.addBaseMaxHp((short) 1500);
				newPc.setCurrentHp(1500);
				newPc.setDead(false);
				newPc.addBaseMaxMp((short) 100);
				newPc.setCurrentMp(100);
				newPc.getResistance().addMr(150);
				newPc.setTitle(rs.getString("title"));
				newPc.getAbility().setBaseStr(18);
				newPc.getAbility().setStr(35);
				newPc.getAbility().setBaseCon(18);
				newPc.getAbility().setCon(18);
				newPc.getAbility().setBaseDex(18);
				newPc.getAbility().setDex(35);
				newPc.getAbility().setBaseCha(18);
				newPc.getAbility().setCha(18);
				newPc.getAbility().setBaseInt(18);
				newPc.getAbility().setInt(18);
				newPc.getAbility().setBaseWis(18);
				newPc.getAbility().setWis(35);
				newPc.set_sex(rs.getInt("sex"));

				newPc.setClassId(rs.getInt("class"));
				newPc.getGfxId().setTempCharGfx(rs.getInt("class"));
				newPc.getGfxId().setGfxId(rs.getInt("class"));

				int ran = _random.nextInt(100) + 1;
				if (newPc.isKnight()) {
					if (ran < 50)
						newPc.setCurrentWeapon(4);
					else
						newPc.setCurrentWeapon(50);
				} else if (newPc.isElf()) {
					if (newPc.getGfxId().getTempCharGfx() != 6160
							&& newPc.getGfxId().getTempCharGfx() != 11498
							&& ran < 20)
						newPc.setCurrentWeapon(4);
					else
						newPc.setCurrentWeapon(20);
				} else if (newPc.isDarkelf()) {
					if (ran < 50)
						newPc.setCurrentWeapon(58);
					else
						newPc.setCurrentWeapon(54);
				} else if (newPc.isDragonknight()) {
					if (ran < 50)
						newPc.setCurrentWeapon(4);
					else
						newPc.setCurrentWeapon(24);
				} else if (newPc.isIllusionist()) {
					if (ran < 30)
						newPc.setCurrentWeapon(40);
					else
						newPc.setCurrentWeapon(58);
				} else if (newPc.isCrown()) {
					newPc.setCurrentWeapon(0);
				} else if (newPc.isWizard()) {
					newPc.setCurrentWeapon(40);
				} else if (newPc.isWarrior()) {
					if (ran < 50)
						newPc.setCurrentWeapon(11);
					else
						newPc.setCurrentWeapon(88);
				}

				if (newPc.isCrown())
					newPc.setType(0);
				else if (newPc.isKnight())
					newPc.setType(1);
				else if (newPc.isElf())
					newPc.setType(2);
				else if (newPc.isWizard())
					newPc.setType(3);
				else if (newPc.isDarkelf())
					newPc.setType(4);
				else if (newPc.isDragonknight())
					newPc.setType(5);
				else if (newPc.isIllusionist())
					newPc.setType(6);
				else if (newPc.isWarrior())
					newPc.setType(7);
				// newPc.setType(1);
				newPc.getMoveState().setMoveSpeed(0);
				newPc.getMoveState().setBraveSpeed(0);
				newPc.getMoveState().setHeading(0);

				newPc.set_food(39);
				newPc.setClanid(rs.getInt("clanid"));
				newPc.setClanname(rs.getString("clanname"));
				if (newPc.getClanid() > 0)
					newPc.setClanRank(L1Clan.CLAN_RANK_PROBATION);
				else
					newPc.setClanRank(0);
				newPc.setElfAttr(0);
				newPc.set_PKcount(0);
				newPc.setExpRes(0);
				newPc.setPartnerId(0);
				newPc.setAccessLevel((short) 0);
				newPc.setGm(false);
				newPc.setMonitor(false);
				newPc.setHomeTownId(0);
				newPc.setContribution(0);
				newPc.setHellTime(0);
				newPc.setBanned(false);
				newPc.setKarma(0);
				newPc.setReturnStat(0);
				newPc.setGmInvis(false);
				newPc.noPlayerCK = true;
				newPc.setActionStatus(0);
				if (_random.nextInt(100) > 40) {
					newPc.setKills(0);
					newPc.setDeaths(0);
				} else {
					newPc.setKills(_random.nextInt(30));
					newPc.setDeaths(_random.nextInt(20));
				}
				newPc.setNetConnection(null);
				put(newPc);
			}
		} catch (SQLException e) {
		} catch (SecurityException e) {
		} catch (IllegalArgumentException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}
