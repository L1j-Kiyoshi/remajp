package l1j.server.GameSystem.Robot;

import static l1j.server.server.model.skill.L1SkillId.HASTE;
import static l1j.server.server.model.skill.L1SkillId.PHYSICAL_ENCHANT_DEX;
import static l1j.server.server.model.skill.L1SkillId.STATUS_UNDERWATER_BREATH;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Random;

import javolution.util.FastMap;
import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharTitle;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ClanJoinLeaveStatus;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.SQLUtil;

public class Robot {

	private static Random _random = new Random(System.currentTimeMillis());
	public static boolean 인형 = false;

	public static void poly(L1RobotInstance bot) {
		/*
		 * int rr = 0;
		 * 
		 * if (bot.리스봇 || bot.사냥봇) rr = _random.nextInt(3); else rr =
		 * _random.nextInt(2);
		 */

		if (/* rr == 0 || */(bot.리스봇_스폰위치 == 1 && _random.nextInt(10) < 8)) {
			polyNormal(bot);
		} else {
				polyNormal(bot);
		}
	}

	private static void polyNormal(L1RobotInstance bot) {
		if (bot.사냥봇) {
			if (bot.isElf() && bot.getCurrentWeapon() == 20) {
				int f = _random.nextInt(5);
				if (f == 1) {
					bot.getGfxId().setTempCharGfx(Config.로봇요정활1); //요정 남
					bot.setCurrentSprite(Config.로봇요정활1);
				} else if (f == 2) {
					bot.getGfxId().setTempCharGfx(Config.로봇요정활2); //포노스궁수
					bot.setCurrentSprite(Config.로봇요정활2);
				} else if (f == 3) {
					bot.getGfxId().setTempCharGfx(Config.로봇요정활3); // 불타는궁수
					bot.setCurrentSprite(Config.로봇요정활3);
				} else if (f == 4) {
					bot.getGfxId().setTempCharGfx(Config.로봇요정활4); // 불타는궁수
					bot.setCurrentSprite(Config.로봇요정활4);
				} else {
					bot.getGfxId().setTempCharGfx(Config.로봇요정활5); // 불타는궁수
					bot.setCurrentSprite(Config.로봇요정활5);
				}
			} else if (bot.getCurrentWeapon() == 50 || bot.getCurrentWeapon() == 4 || bot.getCurrentWeapon() == 88 || bot.getCurrentWeapon() == 58){
				int f1 = _random.nextInt(3);
				if (f1 == 1) {
					bot.getGfxId().setTempCharGfx(Config.로봇칼1); // 샤르나75 기사 남
					bot.setCurrentSprite(Config.로봇칼1);
				} else if(f1 == 2) {
					bot.getGfxId().setTempCharGfx(Config.로봇칼2); // 샤르나 75 요정 남
					bot.setCurrentSprite(Config.로봇칼2);
				} else {
					bot.getGfxId().setTempCharGfx(Config.로봇칼3); // 샤르나 75 요정 남
					bot.setCurrentSprite(Config.로봇칼3);
				}
			} else if (bot.getCurrentWeapon() == 54){
				int f2 = _random.nextInt(2);
				if (f2 == 1) { 
					bot.getGfxId().setTempCharGfx(Config.로봇칼4); // 샤르나 75 용기사여
					bot.setCurrentSprite(Config.로봇칼4);
				} else {
					bot.getGfxId().setTempCharGfx(Config.로봇칼5); // 샤르나 75 환술 남
					bot.setCurrentSprite(Config.로봇칼5);
				}

			}else if(bot.getCurrentWeapon() == 24) {
				int f3 = _random.nextInt(2);
				if (f3 == 1) {
					bot.getGfxId().setTempCharGfx(Config.로봇칼6); // 샤르나 75 환술 여
					bot.setCurrentSprite(Config.로봇칼6);
				} else {
					bot.getGfxId().setTempCharGfx(Config.로봇칼7); // 샤르나 75 환술 여
					bot.setCurrentSprite(Config.로봇칼7);
				}
			}else if(bot.getCurrentWeapon() == 40) {
				int f4 = _random.nextInt(2);
				if(f4 == 1) {
					bot.getGfxId().setTempCharGfx(Config.로봇칼8); // 샤르나 75 환술 여
					bot.setCurrentSprite(Config.로봇칼8);
				} else {
					bot.getGfxId().setTempCharGfx(Config.로봇칼9); // 샤르나 75 환술 여
					bot.setCurrentSprite(Config.로봇칼9);
				}
			}
		} else {
			// int f = _random.nextInt(100);
			// 버경 봇들
			/*
			 * if(bot.리스봇 && (bot.리스봇_스폰위치 == 0 || bot.리스봇_스폰위치 == 3 ||
			 * (bot.리스봇_스폰위치 >= 16 && bot.리스봇_스폰위치 <= 19))) f =
			 * _random.nextInt(100);
			 */
			bot.getGfxId().setTempCharGfx(bot.getGfxId().getGfxId());
			bot.setCurrentSprite(bot.getGfxId().getGfxId());

			/*
			 * if(f < 15){
			 * bot.getGfxId().setTempCharGfx(bot.getGfxId().getGfxId());
			 * if(bot.버경봇 && bot.isCrown()){ if(_random.nextInt(2) == 0){
			 * if(bot.get_sex() == 0) bot.getGfxId().setTempCharGfx(6094); else
			 * bot.getGfxId().setTempCharGfx(6080); } } }else if(f < 50){
			 * bot.getGfxId().setTempCharGfx(11375); }else if(f < 75){
			 * bot.getGfxId().setTempCharGfx(11328+_random.nextInt(3)); //else
			 * if(f == 3) // bot.getGfxId().setTempCharGfx(11341); }else{
			 * bot.getGfxId().setTempCharGfx(11370); } if(bot.isElf() && f != 0
			 * && bot.getCurrentWeapon() == 20)
			 * bot.getGfxId().setTempCharGfx(bot.getGfxId().getGfxId());
			 */
		}
	}
	
	
	public static void polyWar(L1RobotInstance bot) {
		if (bot.사냥봇) {
			if(bot.isWizard()) {
				int f4 = _random.nextInt(2);
				if(f4 == 1) {
					bot.getGfxId().setTempCharGfx(Config.로봇칼1); // 샤르나 75 환술 여
					bot.setCurrentSprite(Config.로봇칼1);
				} else {
					bot.getGfxId().setTempCharGfx(Config.로봇칼2); // 샤르나 75 환술 여
					bot.setCurrentSprite(Config.로봇칼2);
				}
			}
		}
	}

	public static boolean 속도버프(L1RobotInstance bot) {
		// TODO 자동 생성된 메소드 스텁
		// 디케이 아닐때
		if (bot.getMap().isUnderwater()) {
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					STATUS_UNDERWATER_BREATH)) {
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(),
						190), true);
				bot.getSkillEffectTimerSet().setSkillEffect(
						STATUS_UNDERWATER_BREATH, 1800 * 1000);
			}
		}
		if (bot.getMoveState().getMoveSpeed() == 0
				&& !bot.getSkillEffectTimerSet().hasSkillEffect(HASTE)
				&& !bot.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.DECAY_POTION)) {
			bot.getMoveState().setMoveSpeed(1);
			bot.getSkillEffectTimerSet().setSkillEffect(HASTE,
					(_random.nextInt(400) + 1700) * 1000);
			Broadcaster.broadcastPacket(bot,
					new S_SkillSound(bot.getId(), 191), true);
		}
		if (_random.nextInt(100) > 10)
			return false;
		if (bot.isKnight() || bot.isCrown() || bot.isWarrior()) {
			// 디케이아닐때
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_BRAVE)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.DECAY_POTION)) {
				bot.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_BRAVE,
						(_random.nextInt(600) + 400) * 1000);
				bot.getMoveState().setBraveSpeed(1);
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(),
						751), true);
				Broadcaster.broadcastPacket(bot, new S_SkillBrave(bot.getId(),
						1, 0), true);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_DRAGONPERL)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.DECAY_POTION)) {
				bot.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_DRAGONPERL,
						(_random.nextInt(600) + 400) * 1000);
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 197), true);
				Broadcaster.broadcastPacket(bot, new S_DRAGONPERL(bot.getId(), 8), true);//
				bot.set진주속도(1);
				return true;
			}
		} else if (bot.isElf()) {
			if (bot.getCurrentWeapon() != 20) {
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.FIRE_BLESS)
						&& !bot.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.SILENCE)) {
					bot.getSkillEffectTimerSet().setSkillEffect(
							L1SkillId.FIRE_BLESS,
							(_random.nextInt(50) + 250) * 1000);
					bot.getMoveState().setBraveSpeed(1);
					Broadcaster.broadcastPacket(bot,
							new S_SkillSound(bot.getId(), 11775), true);
					Broadcaster.broadcastPacket(bot,
							new S_SkillBrave(bot.getId(), 1, 0), true);
					return true;
				}
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.STATUS_DRAGONPERL)
						&& !bot.getSkillEffectTimerSet().hasSkillEffect(
								L1SkillId.DECAY_POTION)) {
					bot.getSkillEffectTimerSet().setSkillEffect(
							L1SkillId.STATUS_DRAGONPERL,
							(_random.nextInt(600) + 400) * 1000);
					Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 197), true);
					Broadcaster.broadcastPacket(bot, new S_DRAGONPERL(bot.getId(), 8), true);//
					bot.set진주속도(1);
					return true;
				}
			} else {
				int rnd = _random.nextInt(100) + 1;
				if(rnd >= 50){
					if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_ELFBRAVE) && !bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DECAY_POTION) &&
							!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.포커스웨이브)) {
						   bot.getSkillEffectTimerSet().setSkillEffect(L1SkillId.STATUS_ELFBRAVE,(_random.nextInt(600) + 400) * 1000);
						   bot.getMoveState().setBraveSpeed(3);
						   Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 751), true);
						   Broadcaster.broadcastPacket(bot, new S_SkillBrave(bot.getId(), 3, 0), true);
						   return true;
					  }
					if (!bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.STATUS_DRAGONPERL)
							&& !bot.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.DECAY_POTION)) {
						bot.getSkillEffectTimerSet().setSkillEffect(
								L1SkillId.STATUS_DRAGONPERL,
								(_random.nextInt(600) + 400) * 1000);
						Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 197), true);
						Broadcaster.broadcastPacket(bot, new S_DRAGONPERL(bot.getId(), 8), true);//
						bot.set진주속도(1);
						return true;
					}
				} else {
					if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_ELFBRAVE) && !bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.포커스웨이브) 
							&& !bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SILENCE)) {
						bot.getSkillEffectTimerSet().setSkillEffect(L1SkillId.포커스웨이브,(_random.nextInt(600) + 400) * 1000);
						bot.getMoveState().setBraveSpeed(1);
						Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 16531), true);
						Broadcaster.broadcastPacket(bot, new S_SkillBrave(bot.getId(), 10, 0), true);
						return true;
					}
					if (!bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.STATUS_DRAGONPERL)
							&& !bot.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.DECAY_POTION)) {
						bot.getSkillEffectTimerSet().setSkillEffect(
								L1SkillId.STATUS_DRAGONPERL,
								(_random.nextInt(600) + 400) * 1000);
						Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 197), true);
						Broadcaster.broadcastPacket(bot, new S_DRAGONPERL(bot.getId(), 8), true);//
						bot.set진주속도(1);
						return true;
					}
				}
			}
		} else if (bot.isDragonknight()) {
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.BLOOD_LUST)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.SILENCE)) {
				bot.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.BLOOD_LUST,
						(_random.nextInt(300) + 200) * 1000);
				bot.getMoveState().setBraveSpeed(1);
				Broadcaster.broadcastPacket(bot, new S_DoActionGFX(bot.getId(),
						19));
				Broadcaster.broadcastPacket(bot, new S_SkillBrave(bot.getId(),
						1, 0), true);
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(),
						6523), true);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_DRAGONPERL)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.DECAY_POTION)) {
				bot.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_DRAGONPERL,
						(_random.nextInt(600) + 400) * 1000);
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 197), true);
				Broadcaster.broadcastPacket(bot, new S_DRAGONPERL(bot.getId(), 8), true);//
				bot.set진주속도(1);
				return true;
			}
		} else if (bot.isDarkelf()) {
			int percent = (int) Math.round((double) bot.getCurrentMp()
					/ (double) bot.getMaxMp() * 100);
			if (percent < 20)
				return false;
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.MOVING_ACCELERATION)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.SILENCE)) {
				new L1SkillUse().handleCommands(bot,
						L1SkillId.MOVING_ACCELERATION, bot.getId(), bot.getX(),
						bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				/*
				 * bot.getSkillEffectTimerSet().setSkillEffect(L1SkillId.
				 * MOVING_ACCELERATION, (_random.nextInt(600)+400) *1000);
				 * bot.getMoveState().setBraveSpeed(4);
				 * Broadcaster.broadcastPacket(bot, new
				 * S_DoActionGFX(bot.getId(), 19));
				 * Broadcaster.broadcastPacket(bot, new
				 * S_SkillBrave(bot.getId(), 4, 0), true);
				 * Broadcaster.broadcastPacket(bot, new
				 * S_SkillSound(bot.getId(), 2945), true);
				 */
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_DRAGONPERL)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.DECAY_POTION)) {
				bot.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_DRAGONPERL,
						(_random.nextInt(600) + 400) * 1000);
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 197), true);
				Broadcaster.broadcastPacket(bot, new S_DRAGONPERL(bot.getId(), 8), true);//
				bot.set진주속도(1);
				return true;
			}
		} else if (bot.isIllusionist()) {
			int percent = (int) Math.round((double) bot.getCurrentMp()
					/ (double) bot.getMaxMp() * 100);
			if (percent < 20)
				return false;
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_BRAVE)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.DECAY_POTION)) {
				new L1SkillUse().handleCommands(bot,
						L1SkillId.STATUS_BRAVE, bot.getId(), bot.getX(),
						bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				   bot.getMoveState().setBraveSpeed(4);
				   Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 751), true);
				   Broadcaster.broadcastPacket(bot, new S_SkillBrave(bot.getId(), 4, 0), true);
				
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_DRAGONPERL)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.DECAY_POTION)) {
				bot.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_DRAGONPERL,
						(_random.nextInt(600) + 400) * 1000);
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 197), true);
				Broadcaster.broadcastPacket(bot, new S_DRAGONPERL(bot.getId(), 8), true);//
				bot.set진주속도(1);
				return true;
			}
		} else if (bot.isWizard()) {
			int percent = (int) Math.round((double) bot.getCurrentMp()
					/ (double) bot.getMaxMp() * 100);
			if (percent < 20)
				return false;
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.HOLY_WALK)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SILENCE)) {
				bot.getSkillEffectTimerSet().setSkillEffect(L1SkillId.HOLY_WALK, (_random.nextInt(14) + 50) * 1000);
				bot.getMoveState().setBraveSpeed(4);
				Broadcaster.broadcastPacket(bot, new S_DoActionGFX(bot.getId(),19));
				Broadcaster.broadcastPacket(bot, new S_SkillBrave(bot.getId(),4, 0), true);
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(),3936), true);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_DRAGONPERL)
					&& !bot.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.DECAY_POTION)) {
				bot.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.STATUS_DRAGONPERL,
						(_random.nextInt(600) + 400) * 1000);
				Broadcaster.broadcastPacket(bot, new S_SkillSound(bot.getId(), 197), true);
				Broadcaster.broadcastPacket(bot, new S_DRAGONPERL(bot.getId(), 8), true);//
				bot.set진주속도(1);
				return true;
			}
		}
		return false;
	}

	public static boolean 클래스버프(L1RobotInstance bot) {
		// TODO 자동 생성된 메소드 스텁
		if (bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SILENCE))
			return false;
		if (!bot.isDragonknight()) {
			int percent = (int) Math.round((double) bot.getCurrentMp()
					/ (double) bot.getMaxMp() * 100);
			if (percent < 30)
				return false;
		}
		if (bot.isKnight()) {
			bot.리덕션아머베테랑 = true;
			bot.레이징포스 = true;
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.REDUCTION_ARMOR)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.REDUCTION_ARMOR, bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.BOUNCE_ATTACK)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.BOUNCE_ATTACK, bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.COUNTER_BARRIER)) {
				bot.getSkillEffectTimerSet().setSkillEffect(L1SkillId.COUNTER_BARRIER,128000);
				bot.broadcastPacket(new S_SkillSound(bot.getId(), 10709));
				return true;
			}
		} else if (bot.isCrown()) {
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.BRAVE_AURA)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.BRAVE_AURA, bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.GLOWING_AURA)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.GLOWING_AURA, bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.PRIME)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.PRIME, bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.샤이닝아머)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.샤이닝아머, bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SHINING_AURA)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.SHINING_AURA, bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
		} else if (bot.isElf()) {
			bot.글로리어스 = true;
			if (bot.getCurrentWeapon() == 20 && bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_ELFBRAVE)) {
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STORM_SHOT)) {
					new L1SkillUse().handleCommands(bot, L1SkillId.STORM_SHOT,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
					return true;
				}
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.WIND_WALK)) {
					new L1SkillUse().handleCommands(bot, L1SkillId.WIND_WALK,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
					return true;
				}
			} else if (bot.getCurrentWeapon() == 20 && bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.포커스웨이브)) {
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.WIND_SHOT)) {
					new L1SkillUse().handleCommands(bot, L1SkillId.WIND_SHOT,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
					return true;
				}
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.AQUA_PROTECTER)) {
					new L1SkillUse().handleCommands(bot, L1SkillId.AQUA_PROTECTER,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
					return true;
				}
			} else {
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.BURNING_WEAPON)) {
					new L1SkillUse().handleCommands(bot,L1SkillId.BURNING_WEAPON, bot.getId(), bot.getX(),bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
					return true;
				}
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.ELEMENTAL_FIRE)) {
					new L1SkillUse().handleCommands(bot,L1SkillId.ELEMENTAL_FIRE, bot.getId(), bot.getX(),bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
					return true;
				}
				if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.ADDITIONAL_FIRE)) {
					new L1SkillUse().handleCommands(bot,L1SkillId.ADDITIONAL_FIRE, bot.getId(), bot.getX(),bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
					return true;
				}
			}
		} else if (bot.isDragonknight()) {
			bot.드래곤스킨 = true;
			bot.아우라키아 = true;
			bot.setSkillMastery(L1SkillId.FEAR);
			bot.setSkillMastery(L1SkillId.HORROR_OF_DEATH);
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_FIRE_DRAGON)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.SCALES_FIRE_DRAGON,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SCALES_Lind_DRAGON)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.SCALES_Lind_DRAGON,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.HALPHAS)) {
				bot.getSkillEffectTimerSet().setSkillEffect(L1SkillId.HALPHAS,128000);
				bot.broadcastPacket(new S_SkillSound(bot.getId(), 17723));
				return true;
			}
		} else if (bot.isIllusionist()) {
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.MIRROR_IMAGE)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.MIRROR_IMAGE,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.CONCENTRATION)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.CONCENTRATION,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				bot.setCurrentMp(bot.getMaxMp());
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.PATIENCE)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.PATIENCE,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.INSIGHT)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.INSIGHT,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.JOY_OF_PAIN)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.JOY_OF_PAIN,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.POTENTIAL)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.POTENTIAL,bot.getId(), bot.getX(), bot.getY(), null, 0, L1SkillUse.TYPE_NORMAL);
				return true;
			}
		} else if (bot.isDarkelf()) {
			bot.파이널번 = true;
			bot.버닝스피릿츠 = true;
			bot.드레스이베이젼 = true;
			bot.루시퍼데스티니 = true;
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SHADOW_ARMOR)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.SHADOW_ARMOR,bot.getId(), bot.getX(), bot.getY(), null, 960, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DOUBLE_BRAKE)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.DOUBLE_BRAKE,bot.getId(), bot.getX(), bot.getY(), null, 192, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.UNCANNY_DODGE)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.UNCANNY_DODGE,bot.getId(), bot.getX(), bot.getY(), null, 960, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SHADOW_FANG)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.SHADOW_FANG,bot.getId(), bot.getX(), bot.getY(), null, 192, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.루시퍼)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.루시퍼,bot.getId(), bot.getX(), bot.getY(), null, 192, L1SkillUse.TYPE_NORMAL);
				return true;
			}
		} else if (bot.isWizard()) {
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.IMMUNE_TO_HARM) && !bot.isPinkName()) {
				new L1SkillUse().handleCommands(bot, L1SkillId.IMMUNE_TO_HARM,bot.getId(), bot.getX(), bot.getY(), null, 60, L1SkillUse.TYPE_NORMAL);
				bot.getSkillEffectTimerSet().setSkillEffect(L1SkillId.SHAPE_CHANGE, 1800 * 1000);
				bot.getGfxId().setTempCharGfx(Config.로봇칼8);
				Broadcaster.broadcastPacket(bot, new S_ChangeShape(bot.getId(), bot.getGfxId().getTempCharGfx()));
				Broadcaster.broadcastPacket(bot, new S_CharVisualUpdate(bot, bot.getCurrentWeapon()));
				bot.setCurrentMp(bot.getMaxMp());
				return true;
			}
			int rand =  _random.nextInt(100);
			if (bot.getCurrentHp() < 1300) {
				if (rand < 60) { 
				new L1SkillUse().handleCommands(bot, L1SkillId.IMMUNE_TO_HARM,bot.getId(), bot.getX(), bot.getY(), null, 60, L1SkillUse.TYPE_NORMAL);
				}
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.Freeze_armor)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.Freeze_armor,bot.getId(), bot.getX(), bot.getY(), null, 960, L1SkillUse.TYPE_NORMAL);
				return true;
			}
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.ADVANCE_SPIRIT)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.ADVANCE_SPIRIT,bot.getId(), bot.getX(), bot.getY(), null, 960, L1SkillUse.TYPE_NORMAL);
				return true;
			}
		} else if (bot.isWarrior()) {
			if (!bot.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.기간틱)) {
				new L1SkillUse().handleCommands(bot, L1SkillId.기간틱,bot.getId(), bot.getX(), bot.getY(), null, 60, L1SkillUse.TYPE_NORMAL);
				return true;
			}
		}
		return false;
	}

	private static long joinTime = 0;

	public static void clan_join(L1RobotInstance bot) {
		// TODO 자동 생성된 메소드 스텁
		if (bot.getClanid() != 0 || bot.isCrown())
			return;
		if (_random.nextInt(10) == 0)
			return;
		if (joinTime == 0) {
			joinTime = System.currentTimeMillis()
					+ (60000 * (20 + _random.nextInt(21)));
			return;
		} else {
			if (joinTime > System.currentTimeMillis())
				return;
			joinTime = System.currentTimeMillis()
					+ (60000 * (20 + _random.nextInt(21)));
		}
		// 로봇중 가입 되어있는 케릭이 650케릭 이상인지
		// 가입하려는혈 총혈 다 받아와서 유저 비교 제일적은혈
		String clanname = robot_clan_count();
		if (clanname == null)
			return;
		L1Clan clan = L1World.getInstance().getClan(clanname);
		if (clan == null)
			return;
		L1PcInstance pc = L1World.getInstance().getPlayer(clan.getLeaderName());
		if (pc == null)
			return;
		// 군주근처에 혈원이 있는지
		for (L1PcInstance pp : L1World.getInstance().getVisiblePlayer(pc)) {
			if (!(pp instanceof L1RobotInstance)
					&& pc.getClanid() == pp.getClanid())
				return;
		}
		// 내 근처에 같은혈 있는지
		for (L1PcInstance pp : L1World.getInstance().getVisiblePlayer(bot)) {
			if (!(pp instanceof L1RobotInstance)
					&& pc.getClanid() == pp.getClanid())
				return;
		}
		// 가입
		for (L1PcInstance clanMembers : clan.getOnlineClanMember()) {
			clanMembers.sendPackets(new S_ServerMessage(94, bot.getName())); // \f1%0이
																				// 혈맹의
																				// 일원으로서
																				// 받아들여졌습니다.
		}
		bot.setClanid(clan.getClanId());
		bot.setClanname(clan.getClanName());
		bot.setClanRank(L1Clan.CLAN_RANK_PROBATION);
		bot.setTitle("");
		bot.setClanJoinDate(new Timestamp(System.currentTimeMillis()));
		Broadcaster.broadcastPacket(bot, new S_CharTitle(bot.getId(), ""));
		clan.addClanMember(bot.getName(), bot.getClanRank(), bot.getLevel(),
				bot.getType(), bot.getMemo(), 1, bot);
		Broadcaster.broadcastPacket(bot, new S_ClanJoinLeaveStatus(bot));
		Broadcaster.broadcastPacket(bot, new S_ReturnedStat(bot,
				S_ReturnedStat.CLAN_JOIN_LEAVE));
		GeneralThreadPool.getInstance().schedule(
				new title((L1RobotInstance) pc, bot),
				3000 + _random.nextInt(2000));
		try {
			Robot_Bugbear.getInstance().clanSetting(bot);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			Robot_ConnectAndRestart.getInstance().clanSetting(bot);
		} catch (Exception e) {
			// TODO: handle exception
		}
		try {
			Robot_Fish.getInstance().clanSetting(bot);
		} catch (Exception e) {
			// TODO: handle exception
		}
		bot.updateclan(bot.getClanname(), bot.getClanid(), bot.getTitle(), true);
	}

	private static String robot_clan_count() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		String clan = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM robots_crown");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String clanname = rs.getString("clanname");
				int clanid = rs.getInt("clanid");
				if (clanid == 0)
					continue;
				Connection con2 = null;
				PreparedStatement pstm2 = null;
				ResultSet rs2 = null;
				try {
					con2 = L1DatabaseFactory.getInstance().getConnection();
					pstm2 = con2
							.prepareStatement("SELECT * FROM robots WHERE clanid=?");
					pstm2.setInt(1, clanid);
					rs2 = pstm2.executeQuery();
					if (!rs2.next()) {
						clan = clanname;
						break;
					}
				} catch (SQLException e) {

				} finally {
					SQLUtil.close(rs2);
					SQLUtil.close(pstm2);
					SQLUtil.close(con2);
				}
			}
		} catch (SQLException e) {

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		if (clan != null)
			return clan;

		int count = 0;
		FastMap<String, Integer> list = new FastMap<String, Integer>();
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM robots");
			rs = pstm.executeQuery();
			while (rs.next()) {
				String clanname = rs.getString("clanname");
				int clanid = rs.getInt("clanid");
				if (clanid == 0)
					continue;
				count++;
				if (count > 650)
					break;
				try {
					int cc = list.get(clanname);
					list.put(clanname, cc + 1);
				} catch (Exception e) {
					list.put(clanname, 0);
				}
			}
		} catch (SQLException e) {

		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		if (count > 650)
			return null;

		int ci = 1000;
		for (FastMap.Entry<String, Integer> e = list.head(), mapEnd = list
				.tail(); (e = e.getNext()) != mapEnd;) {
			int cu = e.getValue();
			if (ci >= cu) {
				ci = cu;
				clan = e.getKey();
			}
		}
		return clan;
	}

	static class title implements Runnable {

		private L1RobotInstance crown;
		private L1RobotInstance joinchar;

		public title(L1RobotInstance _crown, L1RobotInstance _joinchar) {
			crown = _crown;
			joinchar = _joinchar;
		}

		@Override
		public void run() {
			// TODO 자동 생성된 메소드 스텁
			try {

				if (crown._userTitle == null
						|| crown._userTitle.equalsIgnoreCase(""))
					return;
				if (L1World.getInstance().getPlayer(crown.getName()) == null
						|| L1World.getInstance().getPlayer(joinchar.getName()) == null)
					return;

				joinchar.setTitle(crown._userTitle);
				S_CharTitle ct = new S_CharTitle(joinchar.getId(),
						joinchar.getTitle());
				joinchar.sendPackets(ct);
				Broadcaster.broadcastPacket(joinchar, ct, true);
				try {
					if (joinchar instanceof L1RobotInstance)
						joinchar.updateclan(joinchar.getClanname(),
								joinchar.getClanid(), crown._userTitle, true);
					else
						joinchar.save(); // DB에 캐릭터 정보를 써 우
				} catch (Exception e) {
				}

				L1Clan clan = L1World.getInstance()
						.getClan(crown.getClanname());
				if (clan != null) {
					for (L1PcInstance clanPc : clan.getOnlineClanMember()) {
						// \f1%0이%1에 「%2라고 하는 호칭을 주었습니다.
						S_ServerMessage sm = new S_ServerMessage(203,
								crown.getName(), joinchar.getName(),
								joinchar.getTitle());
						clanPc.sendPackets(sm, true);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public static void Doll_Delete(L1RobotInstance bot) {
		// TODO 자동 생성된 메소드 스텁
		Doll_Delete(bot, false);
	}

	public static void Doll_Delete(L1RobotInstance bot, boolean effect) {
		// TODO 자동 생성된 메소드 스텁
		L1DollInstance doll = null;
		for (Object dollObject : bot.getDollList()) {
			doll = (L1DollInstance) dollObject;
		}
		if (doll != null) {
			if (effect)
				Broadcaster.broadcastPacket(doll, new S_SkillSound(
						doll.getId(), 5936), true);
			doll.deleteDoll();
		}
	}

	public static void Doll_Spawn(L1RobotInstance bot) {
		// TODO 자동 생성된 메소드 스텁
		if (!인형)
			return;
		if (bot.인형스폰)
			return;
		if (bot.getDollList().size() > 0)
			return;
		if (!bot.사냥봇 && _random.nextInt(100) < 70)
			return;
		int time = 2000 + _random.nextInt(8000);
		if (bot.버경봇)
			time = 1;
		GeneralThreadPool.getInstance().schedule(new DollSpawn(bot, time != 1),
				time);
		bot.인형스폰 = true;
	}

	static class DollSpawn implements Runnable {
		private L1RobotInstance bot;
		private boolean effect;

		public DollSpawn(L1RobotInstance _bot, boolean _effect) {
			bot = _bot;
			effect = _effect;
		}

		@Override
		public void run() {
			try {

				// TODO 자동 생성된 메소드 스텁
				if (bot.isDead()
						|| bot._스레드종료
						|| L1World.getInstance().getPlayer(bot.getName()) == null)
					return;
				int npcId = 0;
				int dollType = 0;
				int dollTime = 0;
					if (bot.getCurrentWeapon() == 20) {
						npcId = Config.인형타입활엔피씨아이디;
						//npcId = 1600242;
						dollType = Config.인형타입활;
					} else {
					if (bot.isKnight() || bot.isCrown()) {
						npcId = Config.인형타입칼엔피씨아이디1; //스파토이
						//npcId = 1600247; //데나
						dollType = Config.인형타입칼1;
				    } else if (bot.isWarrior()) {
						npcId = Config.인형타입칼엔피씨아이디2; //스파토이
						//npcId = 1600247; //데나
						dollType = Config.인형타입칼2;
					} else if (bot.isDarkelf()) {
						npcId = Config.인형타입칼엔피씨아이디3; //스파토이
						//npcId = 1600247; //데나
						dollType = Config.인형타입칼3;
					} else if (bot.isDragonknight()) {
						npcId = Config.인형타입칼엔피씨아이디4; //스파토이
						//npcId = 1600247; //데나
						dollType = Config.인형타입칼4;
					} else if (bot.getCurrentWeapon() != 20 && bot.isElf()) {
						npcId = Config.인형타입칼엔피씨아이디5; //스파토이
						//npcId = 1600247; //데나
						dollType = Config.인형타입칼5;
					} else if (bot.isWizard() || bot.isIllusionist()) {
						npcId = Config.인형타입칼엔피씨아이디6; //스파토이
						//npcId = 1600247; //데나
						dollType = Config.인형타입칼6;
					}
					if (_random.nextInt(3) == 0) {
						npcId = Config.경험치인형엔피씨아이디;
						//npcId = 1500204;
						dollType = Config.인형타입경험치;
					}
				 }
					dollTime = 1800;
				if (dollType == 0)
					return;
				L1Npc template = NpcTable.getInstance().getTemplate(npcId);
				L1DollInstance doll = new L1DollInstance(template, bot,
						dollType, ObjectIdFactory.getInstance().nextId(),
						dollTime * 1000);
				if (effect)
					Broadcaster.broadcastPacket(bot,
							new S_SkillSound(doll.getId(), 5935), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}


	static class poly implements Runnable {

		L1RobotInstance bot;

		public poly(L1RobotInstance _bot) {
			bot = _bot;
		}

		@Override
		public void run() {
			try {
				// TODO 자동 생성된 메소드 스텁
				if (bot.isDead()
						|| bot._스레드종료
						|| L1World.getInstance().getPlayer(bot.getName()) == null)
					return;
				poly(bot);
				Broadcaster.broadcastPacket(bot, new S_ChangeShape(bot.getId(),
						bot.getGfxId().getTempCharGfx()));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static void 로봇종료(L1RobotInstance bot) {
		if (bot.버경봇) {
			bot._스레드종료 = true;
			bot.getNearObjects().removeAllKnownObjects();
			bot.stopHalloweenRegeneration();
			bot.stopPapuBlessing();
			bot.stopLindBlessing();
			bot.stopHalloweenArmorBlessing();
			bot.stopAHRegeneration();
			bot.stopHpRegenerationByDoll();
			bot.stopMpRegenerationByDoll();
			bot.stopSHRegeneration();
			bot.stopMpDecreaseByScales();
			bot.stopEtcMonitor();
			bot.버경봇_타입 = 0;
		}
	}

}
