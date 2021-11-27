package l1j.server.GameSystem.Robot;

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
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class Robot_Bugbear {

	private static Random _random = new Random(System.nanoTime());
	private static Queue<L1RobotInstance> _queue;
	private static Robot_Bugbear _instance;

	public static Robot_Bugbear getInstance() {
		if (_instance == null) {
			_instance = new Robot_Bugbear();
		}
		return _instance;
	}

	public Robot_Bugbear() {
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
	}

	private void put(L1RobotInstance bot) {
		synchronized (_queue) {
			_queue.offer(bot);
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

	public void start_spawn() {
		for (int i = 0; i < 100; i++) {
			L1RobotInstance bot = _queue.poll();
			if (bot == null)
				continue;
			GeneralThreadPool.getInstance().schedule(new botVisible(bot),
					60000 * (_random.nextInt(30) + 1));
			// GeneralThreadPool.getInstance().schedule(new botVisible(bot),
			// 100*(_random.nextInt(30)+1));
		}
	}

	private void spawn() {
		if (!GMCommands.bugbearBot)
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
		if (!GMCommands.bugbearBot)
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
							// 100);//빠르게
							return;
						}
						GeneralThreadPool.getInstance().schedule(this, 100);
						return;
					}
				} else if (spawn_type == 3) {
					if (bot.getClanid() != 0) {
						bot.getClan().removeOnlineClanMember(bot.getName());
					}
					Robot.Doll_Delete(bot);
					bot.setDead(false);
					L1World.getInstance().removeVisibleObject(bot);
					L1World.getInstance().removeObject(bot);
					for (L1PcInstance pc : L1World.getInstance()
							.getRecognizePlayer(bot)) {
						pc.getNearObjects().removeKnownObject(bot);
						pc.sendPackets(new S_RemoveObject(bot), true);
					}
					bot.getNearObjects().removeAllKnownObjects();
					bot.버경봇_타입 = 0;
					bot.loc = null;
					bot.버경봇 = false;
					bot._스레드종료 = true;
					bot.updateconnect(false);
					put(bot);
					spawn();
					return;
				} else if (spawn_type == 0) {
					if (spawning) {
						GeneralThreadPool.getInstance().schedule(this, 10000);
						// GeneralThreadPool.getInstance().schedule(this,
						// 100);//빠르게0
						return;
					}
					spawning = true;
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							spawning = false;
						}

					}, (30 + _random.nextInt(30)) * 1000);

					// GeneralThreadPool.getInstance().schedule(new Runnable(){
					// @Override
					// public void run() {
					// spawning = false;
					// }
					//
					// }, (100));//빠르게
					//
					L1PcInstance rob = L1World.getInstance().getPlayer(
							bot.getName());
					if (rob != null) {
						put(bot);
						direct_spawn();
						return;
					}
					if (!GMCommands.bugbearBot) {
						put(bot);
						return;
					}
					while (true) {
						bot.setX(33537 + _random.nextInt(4));
						bot.setY(32845 + _random.nextInt(14));
						bot.setMap((short) 4);
						boolean ck = false;
						ck = L1World.getInstance().getVisiblePlayer(bot, 0)
								.size() > 0;
						if (ck)
							continue;
						if (bot.getMap().isInMap(bot.getX(), bot.getY())
								&& bot.getMap().isPassable(bot.getX(),
										bot.getY()))
							break;
					}
					// bot.버경봇_이전_lawful = bot.getLawful();
					// bot.setLawful(_random.nextInt(32768));
					bot.버경봇 = true;
					bot._스레드종료 = false;

					bot.getMoveState().setHeading(_random.nextInt(8));
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

					Robot.poly(bot);
					L1World.getInstance().storeObject(bot);
					L1World.getInstance().addVisibleObject(bot);
					Robot.clan_join(bot);
					Robot.Doll_Spawn(bot);

					bot.updateconnect(true);
					bot.startAI();
					spawn_type++;
					// time = 1800000+(_random.nextInt(1800000)) +
					// System.currentTimeMillis();
					// time = System.currentTimeMillis();
					// time = 600000+(_random.nextInt(600000)) +
					// System.currentTimeMillis();
					time = 72000000 + (_random.nextInt(3600000))
							+ System.currentTimeMillis();
					GeneralThreadPool.getInstance().schedule(this, 1);
				} else {
					if (L1BugBearRace.getInstance().getBugRaceStatus() == L1BugBearRace.STATUS_PLAYING || bot.버경봇_타입 != 2) {
						GeneralThreadPool.getInstance().schedule(this, 1000);
						return;
					}
					Thread.sleep(1000 + _random.nextInt(30000));
					// Thread.sleep(100);//빠르게
					if (bot.getClanid() != 0) {
						bot.getClan().removeOnlineClanMember(bot.getName());
					}

					if (_random.nextInt(100) < 30) {
						S_SkillSound ss = new S_SkillSound(bot.getId(), 169);
						S_RemoveObject ro = new S_RemoveObject(bot);
						for (L1PcInstance pc : L1World.getInstance()
								.getRecognizePlayer(bot)) {
							pc.sendPackets(ss);
							pc.sendPackets(ro);
							pc.getNearObjects().removeKnownObject(bot);
						}
					} else {
						for (L1PcInstance pc : L1World.getInstance()
								.getRecognizePlayer(bot)) {
							pc.getNearObjects().removeKnownObject(bot);
							pc.sendPackets(new S_RemoveObject(bot), true);
						}
					}

					L1World.getInstance().removeVisibleObject(bot);
					L1World.getInstance().removeObject(bot);

					Robot.Doll_Delete(bot);
					bot.getNearObjects().removeAllKnownObjects();
					// bot.setLawful(bot.버경봇_이전_lawful);
					// bot.버경봇_이전_lawful = 0;
					bot.버경봇 = false;
					bot._스레드종료 = true;
					bot.버경봇_타입 = 0;
					bot.loc = null;
					bot.updateconnect(false);
					put(bot);
					spawn();
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
					int level = _random.nextInt(9) + 70;
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
					newPc.setHighLevel(71);
					newPc.setLevel(71);
					newPc.setExp(ExpTable.getExpByLevel(71));
					newPc.getAC().setAc(-80);
					newPc.addHitup(45);
					newPc.addBowHitup(45);
					newPc.addDamageReductionByArmor(17);
					break;
				case 2:
					newPc.setHighLevel(72);
					newPc.setLevel(72);
					newPc.setExp(ExpTable.getExpByLevel(72));
					newPc.getAC().setAc(-80);
					newPc.addHitup(45);
					newPc.addBowHitup(45);
					newPc.addDamageReductionByArmor(17);
					break;
				case 3:
					newPc.setHighLevel(73);
					newPc.setLevel(73);
					newPc.setExp(ExpTable.getExpByLevel(73));
					newPc.getAC().setAc(-80);
					newPc.addHitup(45);
					newPc.addBowHitup(45);
					newPc.addDamageReductionByArmor(17);
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
					newPc.setCurrentWeapon(50);
				} else if (newPc.isElf()) {
					if (newPc.getGfxId().getTempCharGfx() != 12314
							&& newPc.getGfxId().getTempCharGfx() != 15848 && newPc.getGfxId().getTempCharGfx() != 13346 && newPc.getGfxId().getTempCharGfx() != 15830
									 && newPc.getGfxId().getTempCharGfx() != 15814
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
						newPc.setCurrentWeapon(58);
				} else if (newPc.isCrown()) {
					newPc.setCurrentWeapon(46);
				} else if (newPc.isWizard()) {
					newPc.setCurrentWeapon(40);
				} else if (newPc.isWarrior()) {
					if (ran < 50)
					newPc.setCurrentWeapon(24);
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
				if (_random.nextInt(100) > 10) {
					newPc.setKills(0);
					newPc.setDeaths(0);
				} else {
					newPc.setKills(_random.nextInt(50));
					newPc.setDeaths(_random.nextInt(10));
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
