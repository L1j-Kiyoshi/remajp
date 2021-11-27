package l1j.server;

import static l1j.server.server.model.skill.L1SkillId.ADVANCE_SPIRIT;
import static l1j.server.server.model.skill.L1SkillId.BLESS_WEAPON;
import static l1j.server.server.model.skill.L1SkillId.FIRE_SHIELD;
import static l1j.server.server.model.skill.L1SkillId.IRON_SKIN;
import static l1j.server.server.model.skill.L1SkillId.NATURES_TOUCH;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_STR;
import static l1j.server.server.model.skill.L1SkillId.STATUS_COMA_5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.Warehouse.PackageWarehouse;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.SQLUtil;

enum SpecialEvent {
	BugRace, AllBuf, InfinityFight, DoNotChatEveryone, DoChatEveryone
};

// 게임 내, 전체 이벤트에 대한 처리를 담당
public class SpecialEventHandler {

	private static volatile SpecialEventHandler uniqueInstance = null;

	private boolean CheckBugrace = false;

	private SpecialEventHandler() {
	}

	public static SpecialEventHandler getInstance() {
		if (uniqueInstance == null) {
			synchronized (SpecialEventHandler.class) {
				if (uniqueInstance == null) {
					uniqueInstance = new SpecialEventHandler();
				}
			}
		}

		return uniqueInstance;
	}

	public void giveFeather() {
		Connection c = null;
		PreparedStatement p = null;
		PreparedStatement p1 = null;
		ResultSet r = null;
		String accountName;
		int count;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("SELECT * FROM hongbo");
			r = p.executeQuery();
			while (r.next()) {
				accountName = r.getString("account");
				count = r.getInt("remaincount");
				if (count <= 0)
					continue;
				count *= 500;
				PackageWarehouse.insertItem(accountName, count);
				p1 = c.prepareStatement("UPDATE hongbo SET excutecount = excutecount + '"
						+ r.getInt("remaincount")
						+ "',remaincount = 0 WHERE account = '"
						+ accountName
						+ "'");
				p1.executeUpdate();
			}
		} catch (Exception e) {
		} finally {
			SQLUtil.close(r);
			SQLUtil.close(p1);
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
	}

	public void doBugRace() {
		if (!CheckBugrace)
			CheckBugrace = true;
		else
			return;
		// BugRaceController.getInstance().BugRaceRestart = true;
	}

	private static final int[] allBuffSkill = {
			L1SkillId.HASTE,
			ADVANCE_SPIRIT,
			// FIRE_WEAPON,
			BLESS_WEAPON, NATURES_TOUCH, L1SkillId.AQUA_PROTECTER, IRON_SKIN,
			L1SkillId.SHINING_AURA, L1SkillId.CONCENTRATION,
			L1SkillId.PATIENCE, L1SkillId.INSIGHT,
			7895,
			L1SkillId.REMOVE_CURSE,
			// L1SkillId.IMMUNE_TO_HARM,
			L1SkillId.IllUSION_OGRE, L1SkillId.IllUSION_DIAMONDGOLEM,
			PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR,
			L1SkillId.FEATHER_BUFF_C, 10528 };

	public void doScreenComaBuf(final L1PcInstance gm) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				try {
					L1SkillUse l1skilluse = null;
					for (L1PcInstance pc : L1World.getInstance()
							.getVisiblePlayer(gm, -1)) {
						if (pc == null || pc.isPrivateShop()) {
							continue;
						}
						l1skilluse = new L1SkillUse();
						l1skilluse.handleCommands(pc, STATUS_COMA_5,
								pc.getId(), pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
						// pc.sendPackets(new
						// S_SystemMessage("운영자에게 버프를 받았습니다. "));
					}
					for (L1NpcShopInstance pc : L1World.getInstance()
							.getAllNpcShop()) {
						if (pc == null
								|| !(pc.getNpcTemplate().get_npcId() >= 8100000 && pc
										.getNpcTemplate().get_npcId() <= 8100002))
							continue;
						L1Skills _skill = SkillsTable.getInstance()
								.getTemplate(STATUS_COMA_5);
						if (_skill != null)
							Broadcaster.broadcastPacket(
									pc,
									new S_SkillSound(pc.getId(), _skill
											.getCastGfx()));
					}

					for (GambleInstance pc : L1World.getInstance()
							.getAllGamble()) {
						if (pc == null)
							continue;
						L1Skills _skill = SkillsTable.getInstance()
								.getTemplate(STATUS_COMA_5);
						if (_skill != null)
							Broadcaster.broadcastPacket(
									pc,
									new S_SkillSound(pc.getId(), _skill
											.getCastGfx()));
					}
					Thread.sleep(5);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	private static final int[] allBuffComaSkill = {
			L1SkillId.HASTE,
			ADVANCE_SPIRIT,
			// FIRE_WEAPON,
			BLESS_WEAPON, NATURES_TOUCH, L1SkillId.AQUA_PROTECTER, IRON_SKIN,
			L1SkillId.SHINING_AURA, L1SkillId.CONCENTRATION,
			L1SkillId.PATIENCE, L1SkillId.INSIGHT,
			7895,
			L1SkillId.REMOVE_CURSE,
			// L1SkillId.IMMUNE_TO_HARM,
			L1SkillId.IllUSION_OGRE, L1SkillId.IllUSION_DIAMONDGOLEM,
			PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR,
			L1SkillId.FEATHER_BUFF_C, STATUS_COMA_5 };// 10528,

	public void doscreenbuftest(L1PcInstance gm) {
		try {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(gm,
					-1)) {
				if (pc == null || pc.isPrivateShop()) {
					continue;
				}
				pc.화면버프start();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		}
	}

	public void doScreenBuf(final L1PcInstance gm) {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				try {
					L1SkillUse l1skilluse = null;
					for (int i = 0; i < allBuffComaSkill.length; i++) {
						for (L1PcInstance pc : L1World.getInstance()
								.getVisiblePlayer(gm, -1)) {
							if (pc == null || pc.isPrivateShop()) {
								continue;
							}
							if (allBuffComaSkill[i] == 7895) {
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7893);
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7894);
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7895);
								pc.addDmgup(3);
								pc.addHitup(3);
								pc.getAbility().addSp(3);
								pc.sendPackets(new S_SPMR(pc));
								// pc.sendPackets(new S_SkillSound(pc.getId(),
								// 7895));
								// Broadcaster.broadcastPacket(pc, new
								// S_SkillSound(pc.getId(), 7895));
								pc.getSkillEffectTimerSet().setSkillEffect(
										7895, 1800 * 1000);
							} else if (allBuffComaSkill[i] == 10528) {
								if (!pc.getSkillEffectTimerSet()
										.hasSkillEffect(L1SkillId.흑사의기운)) {
									pc.getAC().addAc(-2);
									pc.addMaxHp(20);
									pc.addMaxMp(13);
									
									pc.sendPackets(new S_HPUpdate(pc
											.getCurrentHp(), pc.getMaxHp()));
									pc.sendPackets(new S_MPUpdate(pc
											.getCurrentMp(), pc.getMaxMp()));
									pc.sendPackets(new S_OwnCharStatus(pc));
								}
								pc.sendPackets(new S_SkillSound(pc.getId(),
										4914), true);
								Broadcaster.broadcastPacket(pc,
										new S_SkillSound(pc.getId(), 4914));
								pc.getSkillEffectTimerSet().setSkillEffect(
										L1SkillId.흑사의기운, 1800 * 1000);
							} else {
								l1skilluse = new L1SkillUse();
								l1skilluse.handleCommands(pc,
										allBuffComaSkill[i], pc.getId(),
										pc.getX(), pc.getY(), null, 0,
										L1SkillUse.TYPE_GMBUFF);
							}
							// pc.sendPackets(new
							// S_SystemMessage("운영자에게 버프를 받았습니다. "));
						}
						for (L1NpcShopInstance pc : L1World.getInstance()
								.getAllNpcShop()) {
							if (pc == null
									|| !(pc.getNpcTemplate().get_npcId() >= 8100000 && pc
											.getNpcTemplate().get_npcId() <= 8100002))
								continue;
							if (allBuffComaSkill[i] == 10528) {
								Broadcaster.broadcastPacket(pc,
										new S_SkillSound(pc.getId(), 4914));
							} else {
								L1Skills _skill = SkillsTable.getInstance()
										.getTemplate(allBuffComaSkill[i]);
								if (_skill != null)
									Broadcaster.broadcastPacket(
											pc,
											new S_SkillSound(pc.getId(), _skill
													.getCastGfx()));
							}
						}

						for (GambleInstance pc : L1World.getInstance()
								.getAllGamble()) {
							if (pc == null)
								continue;
							if (allBuffComaSkill[i] == 10528) {
								Broadcaster.broadcastPacket(pc,
										new S_SkillSound(pc.getId(), 4914));
							} else {
								L1Skills _skill = SkillsTable.getInstance()
										.getTemplate(allBuffComaSkill[i]);
								if (_skill != null)
									Broadcaster.broadcastPacket(
											pc,
											new S_SkillSound(pc.getId(), _skill
													.getCastGfx()));
							}
						}
						Thread.sleep(5);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void doAllBuf() {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				try {
					// int[] allBuffSkill = { BLESS_WEAPON,
					// PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR,
					// NATURES_TOUCH, ADVANCE_SPIRIT, EARTH_SKIN, FIRE_WEAPON ,
					// WIND_SHOT, STATUS_COMA_5 };
					// int[] allBuffSkill = { CONCENTRATION, PATIENCE, INSIGHT,
					// PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR, BLESS_WEAPON,
					// ADVANCE_SPIRIT, IRON_SKIN, FEATHER_BUFF_A, BRAVE_AURA,
					// BRAVE_AURA, SHINING_AURA, NATURES_TOUCH };
					L1SkillUse l1skilluse = null;
					for (int i = 0; i < allBuffSkill.length; i++) {
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc == null || pc.isPrivateShop()) {
								continue;
							}
							if (allBuffSkill[i] == 7895) {
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7893);
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7894);
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7895);
								pc.addDmgup(3);
								pc.addHitup(3);
								pc.getAbility().addSp(3);
								pc.sendPackets(new S_SPMR(pc));
								pc.getSkillEffectTimerSet().setSkillEffect(
										7895, 1800 * 1000);
							} else if (allBuffSkill[i] == 10528) {
								if (!pc.getSkillEffectTimerSet()
										.hasSkillEffect(L1SkillId.흑사의기운)) {
									pc.getAC().addAc(-2);
									pc.addMaxHp(20);
									pc.addMaxMp(13);
									pc.sendPackets(new S_HPUpdate(pc
											.getCurrentHp(), pc.getMaxHp()));
									pc.sendPackets(new S_MPUpdate(pc
											.getCurrentMp(), pc.getMaxMp()));
									pc.sendPackets(new S_OwnCharStatus(pc));
								}
								pc.sendPackets(new S_SkillSound(pc.getId(),
										4914), true);
								Broadcaster.broadcastPacket(pc,
										new S_SkillSound(pc.getId(), 4914));
								pc.getSkillEffectTimerSet().setSkillEffect(
										L1SkillId.흑사의기운, 1800 * 1000);
							} else {
								l1skilluse = new L1SkillUse();
								l1skilluse.handleCommands(pc, allBuffSkill[i],
										pc.getId(), pc.getX(), pc.getY(), null,
										0, L1SkillUse.TYPE_GMBUFF);
							}
						}
						for (L1NpcShopInstance pc : L1World.getInstance()
								.getAllNpcShop()) {
							if (pc == null
									|| !(pc.getNpcTemplate().get_npcId() >= 8100000 && pc
											.getNpcTemplate().get_npcId() <= 8100002))
								continue;
							if (allBuffSkill[i] == 10528) {
								Broadcaster.broadcastPacket(pc,
										new S_SkillSound(pc.getId(), 4914));
							} else {
								L1Skills _skill = SkillsTable.getInstance()
										.getTemplate(allBuffSkill[i]);
								if (_skill != null)
									Broadcaster.broadcastPacket(
											pc,
											new S_SkillSound(pc.getId(), _skill
													.getCastGfx()));
							}
						}
						for (GambleInstance pc : L1World.getInstance()
								.getAllGamble()) {
							if (pc == null)
								continue;
							if (allBuffSkill[i] == 10528) {
								Broadcaster.broadcastPacket(pc,
										new S_SkillSound(pc.getId(), 4914));
							} else {
								L1Skills _skill = SkillsTable.getInstance()
										.getTemplate(allBuffSkill[i]);
								if (_skill != null)
									Broadcaster.broadcastPacket(
											pc,
											new S_SkillSound(pc.getId(), _skill
													.getCastGfx()));
							}
						}
						Thread.sleep(5);
					}
					for (L1PcInstance pc : L1World.getInstance()
							.getAllPlayers()) {
						if (pc == null || pc.isPrivateShop()) {
							continue;
						}
						pc.sendPackets(new S_SystemMessage("운영자에게 버프를 받았습니다. "));
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void doAllComa() {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				try {
					L1SkillUse l1skilluse = null;
					for (L1PcInstance pc : L1World.getInstance()
							.getAllPlayers()) {
						if (pc == null || pc.isPrivateShop()) {
							continue;
						}
						l1skilluse = new L1SkillUse();
						l1skilluse.handleCommands(pc, 1025, pc.getId(),
								pc.getX(), pc.getY(), null, 0,
								L1SkillUse.TYPE_GMBUFF);
					}
					for (L1NpcShopInstance pc : L1World.getInstance()
							.getAllNpcShop()) {
						if (pc == null
								|| !(pc.getNpcTemplate().get_npcId() >= 8100000 && pc
										.getNpcTemplate().get_npcId() <= 8100002))
							continue;
						L1Skills _skill = SkillsTable.getInstance()
								.getTemplate(STATUS_COMA_5);
						if (_skill != null)
							Broadcaster.broadcastPacket(
									pc,
									new S_SkillSound(pc.getId(), _skill
											.getCastGfx()));
					}

					for (GambleInstance pc : L1World.getInstance()
							.getAllGamble()) {
						if (pc == null)
							continue;
						L1Skills _skill = SkillsTable.getInstance()
								.getTemplate(STATUS_COMA_5);
						if (_skill != null)
							Broadcaster.broadcastPacket(
									pc,
									new S_SkillSound(pc.getId(), _skill
											.getCastGfx()));
					}
					Thread.sleep(5);
					for (L1PcInstance pc : L1World.getInstance()
							.getAllPlayers()) {
						if (pc == null || pc.isPrivateShop()) {
							continue;
						}
						pc.sendPackets(new S_SystemMessage("운영자에게 코마버프를 받았습니다."));
					}
				} catch (Exception e) {
				}
			}
		});
	}

	public void doAllComaBuf() {
		GeneralThreadPool.getInstance().execute(new Runnable() {
			public void run() {
				try {
					// int[] allBuffSkill = { BLESS_WEAPON,
					// PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR,
					// NATURES_TOUCH, ADVANCE_SPIRIT, EARTH_SKIN, FIRE_WEAPON ,
					// WIND_SHOT, STATUS_COMA_5 };
					// int[] allBuffSkill = { CONCENTRATION, PATIENCE, INSIGHT,
					// PHYSICAL_ENCHANT_DEX, PHYSICAL_ENCHANT_STR, BLESS_WEAPON,
					// ADVANCE_SPIRIT, IRON_SKIN, FEATHER_BUFF_A, BRAVE_AURA,
					// BRAVE_AURA, SHINING_AURA, NATURES_TOUCH };
					L1SkillUse l1skilluse = null;
					for (int i = 0; i < allBuffComaSkill.length; i++) {
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc == null || pc.isPrivateShop()) {
								continue;
							}
							if (allBuffComaSkill[i] == 7895) {
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7893);
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7894);
								pc.getSkillEffectTimerSet().removeSkillEffect(
										7895);
								pc.addDmgup(3);
								pc.addHitup(3);
								pc.getAbility().addSp(3);
								pc.sendPackets(new S_SPMR(pc));
								// pc.sendPackets(new S_SkillSound(pc.getId(),
								// 7895));
								// Broadcaster.broadcastPacket(pc, new
								// S_SkillSound(pc.getId(), 7895));
								pc.getSkillEffectTimerSet().setSkillEffect(
										7895, 1800 * 1000);
								/*
								 * }else if(allBuffComaSkill[i] == 10528){
								 * if(!pc
								 * .getSkillEffectTimerSet().hasSkillEffect
								 * (L1SkillId.흑사의기운)){ pc.getAC().addAc(-2);
								 * pc.addMaxHp(20); pc.addMaxMp(13);
								 * pc.getResistance().addBlind(10);
								 * pc.sendPackets(new
								 * S_HPUpdate(pc.getCurrentHp(),
								 * pc.getMaxHp())); pc.sendPackets(new
								 * S_MPUpdate(pc.getCurrentMp(),
								 * pc.getMaxMp())); pc.sendPackets(new
								 * S_OwnCharStatus(pc)); } pc.sendPackets(new
								 * S_SkillSound(pc.getId(), 4914), true);
								 * Broadcaster.broadcastPacket(pc, new
								 * S_SkillSound(pc.getId(), 4914));
								 * pc.getSkillEffectTimerSet
								 * ().setSkillEffect(L1SkillId.흑사의기운,
								 * 1800*1000);
								 */
							} else {
								l1skilluse = new L1SkillUse();
								l1skilluse.handleCommands(pc,
										allBuffComaSkill[i], pc.getId(),
										pc.getX(), pc.getY(), null, 0,
										L1SkillUse.TYPE_GMBUFF);
							}
							// pc.sendPackets(new
							// S_SystemMessage("운영자에게 버프를 받았습니다. "));
						}
						for (L1NpcShopInstance pc : L1World.getInstance()
								.getAllNpcShop()) {
							if (pc == null
									|| !(pc.getNpcTemplate().get_npcId() >= 8100000 && pc
											.getNpcTemplate().get_npcId() <= 8100002))
								continue;
							if (allBuffComaSkill[i] == 10528) {
								Broadcaster.broadcastPacket(pc,
										new S_SkillSound(pc.getId(), 4914));
							} else {
								L1Skills _skill = SkillsTable.getInstance()
										.getTemplate(allBuffComaSkill[i]);
								if (_skill != null)
									Broadcaster.broadcastPacket(
											pc,
											new S_SkillSound(pc.getId(), _skill
													.getCastGfx()));
							}
						}

						for (GambleInstance pc : L1World.getInstance()
								.getAllGamble()) {
							if (pc == null)
								continue;
							if (allBuffComaSkill[i] == 10528) {
								Broadcaster.broadcastPacket(pc,
										new S_SkillSound(pc.getId(), 4914));
							} else {
								L1Skills _skill = SkillsTable.getInstance()
										.getTemplate(allBuffComaSkill[i]);
								if (_skill != null)
									Broadcaster.broadcastPacket(
											pc,
											new S_SkillSound(pc.getId(), _skill
													.getCastGfx()));
							}
						}
						Thread.sleep(5);
					}
					for (L1PcInstance pc : L1World.getInstance()
							.getAllPlayers()) {
						if (pc == null || pc.isPrivateShop()) {
							continue;
						}
						pc.sendPackets(new S_SystemMessage("운영자에게 버프를 받았습니다. "));
					}

				} catch (Exception e) {

				}
			}
		});
	}

	public void doNotChatEveryone() {
		L1World.getInstance().set_worldChatElabled(false);
		L1World.getInstance().broadcastPacketToAll(
				new S_SystemMessage("[***] 안녕하세요. 운영자 입니다."));
	}

	public void doChatEveryone() {
		L1World.getInstance().set_worldChatElabled(true);
		L1World.getInstance().broadcastPacketToAll(
				new S_SystemMessage("[***] 좋은하루되세요. 감사합니다."));
	}

	public void ReturnStats(L1PcInstance pc) {

		pc.getAbility().initStat(pc.getClassId());
		L1SkillUse l1skilluse = new L1SkillUse();
		l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION, pc.getId(),
				pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_LOGIN);

		if (pc.getWeapon() != null) {
			pc.getInventory().setEquipped(pc.getWeapon(), false, false, false);
		}
		pc.sendPackets(new S_SPMR(pc));
		pc.sendPackets(new S_CharVisualUpdate(pc));
		pc.sendPackets(new S_OwnCharStatus2(pc));

		for (L1ItemInstance armor : pc.getInventory().getItems()) {
			for (int type = 0; type <= 12; type++) {
				if (armor != null) {
					pc.getInventory().setEquipped(armor, false, false, false);
				}
			}
		}
		pc.setReturnStat(pc.getExp());
		pc.sendPackets(new S_SPMR(pc));
		pc.sendPackets(new S_OwnCharAttrDef(pc));
		pc.sendPackets(new S_OwnCharStatus2(pc));
		pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.START));
		try {
			pc.save();
		} catch (Exception e) {
		}
	}

	public void doMapBuf(L1PcInstance base) {
		int[] allBuffSkill = { L1SkillId.HASTE,
				ADVANCE_SPIRIT,
				// FIRE_WEAPON,
				NATURES_TOUCH, L1SkillId.AQUA_PROTECTER, FIRE_SHIELD,
				L1SkillId.SHINING_AURA, L1SkillId.CONCENTRATION,
				L1SkillId.PATIENCE, L1SkillId.INSIGHT, L1SkillId.STATUS_COMA_5 };
		L1SkillUse l1skilluse = null;
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if (pc == null || pc.isPrivateShop()
					|| pc.getMapId() != base.getMapId()) {
				continue;
			}
			l1skilluse = new L1SkillUse();
			for (int i = 0; i < allBuffSkill.length; i++) {
				l1skilluse.handleCommands(pc, allBuffSkill[i], pc.getId(),
						pc.getX(), pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
			}
			pc.getSkillEffectTimerSet().removeSkillEffect(7893);
			pc.getSkillEffectTimerSet().removeSkillEffect(7894);
			pc.getSkillEffectTimerSet().removeSkillEffect(7895);
			pc.addDmgup(3);
			pc.addHitup(3);
			pc.getAbility().addSp(3);
			pc.sendPackets(new S_SPMR(pc));
			// pc.sendPackets(new S_SkillSound(pc.getId(), 7895));
			// Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
			// 7895));
			pc.getSkillEffectTimerSet().setSkillEffect(7895, 1800 * 1000);
			pc.sendPackets(new S_SystemMessage("운영자에게 버프를 받았습니다. "));
		}
		for (L1NpcShopInstance pc : L1World.getInstance().getAllNpcShop()) {
			if (pc == null
					|| !(pc.getNpcTemplate().get_npcId() >= 8100000 && pc
							.getNpcTemplate().get_npcId() <= 8100002)
					|| pc.getMapId() != base.getMapId())
				continue;
			for (int i = 0; i < allBuffSkill.length; i++) {
				L1Skills _skill = SkillsTable.getInstance().getTemplate(
						allBuffSkill[i]);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
						_skill.getCastGfx()));
			}
		}
		for (GambleInstance pc : L1World.getInstance().getAllGamble()) {
			if (pc == null || pc.getMapId() != base.getMapId())
				continue;
			for (int i = 0; i < allBuffSkill.length; i++) {
				L1Skills _skill = SkillsTable.getInstance().getTemplate(
						allBuffSkill[i]);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
						_skill.getCastGfx()));
			}
		}
	}

}
