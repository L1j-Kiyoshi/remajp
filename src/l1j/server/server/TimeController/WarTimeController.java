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
package l1j.server.server.TimeController;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.CastleTable;
//import l1j.server.server.datatables.CharSoldierTable;
import l1j.server.server.datatables.CharacterTable;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1WarSpawn;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1CastleGuardInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1GuardInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.utils.L1SpawnUtil;

public class WarTimeController implements Runnable {
	private static WarTimeController _instance;
	private L1Castle[] _l1castle = new L1Castle[8];
	private long[] _war_start_time = new long[8];
	private long[] _war_end_time = new long[8];
	private boolean[] _is_now_war = new boolean[8];

	private boolean[] ???????? = new boolean[8];

	private WarTimer[] _war_timer = new WarTimer[8];
	private boolean[] _war_end = new boolean[8];
	private String[] _war_defence_clan = new String[8];
	private String[] _war_attack_clan = new String[8];
	private int[] _war_time = new int[8];

	private WarTimeController() {
		for (int i = 0; i < _l1castle.length; i++) {
			_l1castle[i] = CastleTable.getInstance().getCastleTable(i + 1);
			_war_start_time[i] = _l1castle[i].getWarTime().getTimeInMillis();
			_war_end_time[i] = LongType_setTime(_war_start_time[i],
					Config.ALT_WAR_TIME_UNIT, Config.ALT_WAR_TIME);
		}
	}

	public String WarTimeString(int castle) {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy?? MM?? dd?? HH?? mm??");
		String time = dateFormat.format(new Timestamp(
				_war_start_time[castle - 1]));
		return time;
	}

	private long LongType_setTime(long o, int type, long time) {
		if (Calendar.DATE == type) {
			return o + (60000 * 60 * 24 * time);
		} else if (Calendar.HOUR_OF_DAY == type) {
			return o + (60000 * 60 * time);
		} else if (Calendar.MINUTE == type) {
			return o + (60000 * time);
		}
		return 0;
	}

	public void setWarStartTime(String name, Calendar cal) {
		if (name == null) {
			return;
		}
		if (name.length() == 0) {
			return;
		}

		for (int i = 0; i < _l1castle.length; i++) {
			L1Castle castle = _l1castle[i];
			if (castle.getName().startsWith(name)) {
				castle.setWarTime(cal);
				_war_start_time[i] = ((Calendar) cal.clone()).getTimeInMillis();
				_war_end_time[i] = LongType_setTime(_war_start_time[i],
						Config.ALT_WAR_TIME_UNIT, Config.ALT_WAR_TIME);
			}
		}
	}

	public void setWarExitTime(L1PcInstance pc, String name) {

		if (name == null) {
			return;
		}

		if (name.length() == 0) {
			return;
		}

		for (int i = 0; i < _l1castle.length; i++) {
			L1Castle castle = _l1castle[i];
			if (castle.getName().startsWith(name)) {
				????????[castle.getId() - 1] = true;
				pc.sendPackets(new S_SystemMessage(castle.getName()
						+ " ???? ???? ????"), true);
			}
		}
	}

	public static WarTimeController getInstance() {
		if (_instance == null) {
			_instance = new WarTimeController();
		}
		return _instance;
	}

	public void run() {
		try {
			// while (true) {
			// if(Config.WAR_TIME_AUTO_SETTING)
			// ????????????();
			checkWarTime(); // ???? ?????? ????
			// Thread.sleep(1000);
			GeneralThreadPool.getInstance().schedule(this, 1000);
			// }
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	// ????9?? ??????, ?????? ???? ???? ???? ????
	int ccc = 60;

	private void ????????????() {
		if (ccc-- < 0)
			ccc = 60;
		else {
			if (ccc % 10 == 0) {
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					int castleid = L1CastleLocation.getCastleIdByArea(pc);
					if (castleid != 0) {
						if (isNowWar(castleid)) {
							int warType = AttackDefenceCheck(castleid);
							String name = _war_defence_clan[castleid - 1];
							if (warType == 2)
								name = _war_attack_clan[castleid - 1];
							pc.sendPackets(new S_NewCreateItem(warType,
									_war_time[castleid - 1], name), true);
						}
					}
				}
			}
			return;
		}
		for (int i = 0; i < 8; i++) {
			// 0 ???? 1 ???? 2 ???? 3 ?????? 4 ???????? 5 ???? 6 ?????? 7 ??????
			if (i == 3 || i == 0) {
				long time = System.currentTimeMillis();
				if (!_is_now_war[i]
						&& !(_war_start_time[i] <= time && _war_end_time[i] >= time)) {
					Calendar cal = (Calendar) Calendar.getInstance().clone();
					int CurrentDay = cal.getTime().getDay();
					// 0 = Sunday, 1 = Monday, 2 = Tuesday, 3 = Wednesday, 4 =
					// Thursday, 5 = Friday, 6 = Saturday

					/*
					 * ?????? ???? if(CurrentDay == 5){//??????
					 * if(cal.getTime().getHours() > 21) cal.setTimeInMillis((7
					 * * 24 * 3600000) + cal.getTimeInMillis()); }else
					 * if(CurrentDay == 6){//?????? cal.setTimeInMillis((6 * 24 *
					 * 3600000) + cal.getTimeInMillis()); }else if(CurrentDay >=
					 * 0 && CurrentDay <= 4){ cal.setTimeInMillis(((5 -
					 * CurrentDay) * 24 * 3600000) + cal.getTimeInMillis()); }
					 */

					if (CurrentDay == 0) {// ??????
						if (cal.getTime().getHours() > 21)
							cal.setTimeInMillis((7 * 24 * 3600000)
									+ cal.getTimeInMillis());
					} else {
						cal.setTimeInMillis(((7 - CurrentDay) * 24 * 3600000)
								+ cal.getTimeInMillis());
					}

					// ??, ?? ????????
					/*
					 * if(CurrentDay == 0){//?????? if(cal.getTime().getHours() >
					 * 21) cal.setTimeInMillis((3 * 24 * 3600000) +
					 * cal.getTimeInMillis()); }else if(CurrentDay == 3){//??????
					 * if(cal.getTime().getHours() > 21) cal.setTimeInMillis((4
					 * * 24 * 3600000) + cal.getTimeInMillis()); }else
					 * if(CurrentDay >= 1 && CurrentDay <= 2){
					 * cal.setTimeInMillis(((3 - CurrentDay) * 24 * 3600000) +
					 * cal.getTimeInMillis()); }else if(CurrentDay >= 4 &&
					 * CurrentDay <= 6){ cal.setTimeInMillis(((7 - CurrentDay) *
					 * 24 * 3600000) + cal.getTimeInMillis()); }
					 */

					/*
					 * if(i == 0){ if(CurrentDay == 0){//??????
					 * if(cal.getTime().getHours() > 21) cal.setTimeInMillis((7
					 * * 24 * 3600000) + cal.getTimeInMillis()); }else{
					 * cal.setTimeInMillis(((7 - CurrentDay) * 24 * 3600000) +
					 * cal.getTimeInMillis()); } }else if(i == 2){ if(CurrentDay
					 * == 3){//?????? if(cal.getTime().getHours() > 21)
					 * cal.setTimeInMillis((7 * 24 * 3600000) +
					 * cal.getTimeInMillis()); }else if(CurrentDay >= 0 &&
					 * CurrentDay <= 2){ cal.setTimeInMillis(((3 - CurrentDay) *
					 * 24 * 3600000) + cal.getTimeInMillis()); }else
					 * if(CurrentDay >= 4 && CurrentDay <= 6){
					 * cal.setTimeInMillis(((7 - (CurrentDay-3)) * 24 * 3600000)
					 * + cal.getTimeInMillis()); } }
					 */
					cal.set(Calendar.HOUR_OF_DAY, 21);
					cal.set(Calendar.MINUTE, 0);
					cal.set(Calendar.SECOND, 0);
					setWarStartTime(_l1castle[i].getName(), cal);
					CastleTable.getInstance().updateCastle(_l1castle[i]);
				}
			}
		}
	}

	// ???? 9?? ?????? ???? ???? ????
	/*
	 * private void ????????????() {  if(ccc-- < 0) ccc = 60;
	 * else return; for (int i = 0; i < 8; i++) { // 0 ???? 1 ???? 2 ???? 3 ?????? 4 ????????
	 * 5 ???? 6 ?????? 7 ?????? if(i == 0 || i == 2){ long time =
	 * System.currentTimeMillis(); if (!_is_now_war[i] && !(_war_start_time[i]
	 * <= time && _war_end_time[i] >= time)) { Calendar cal =
	 * (Calendar)Calendar.getInstance().clone(); int CurrentDay =
	 * cal.getTime().getDay(); //??, ?? if(CurrentDay >= 1 && CurrentDay <= 2){
	 * cal.setTimeInMillis(((3 - CurrentDay) * 24 * 3600000) +
	 * cal.getTimeInMillis()); //??, ??, ?? }else if(CurrentDay >= 4 && CurrentDay
	 * <= 6){ cal.setTimeInMillis(((7 - CurrentDay) * 24 * 3600000) +
	 * cal.getTimeInMillis()); //?? }else if(CurrentDay == 0){
	 * if(cal.getTime().getHours() > 21) cal.setTimeInMillis((3 * 24 * 3600000)
	 * + cal.getTimeInMillis()); //?? }else if(CurrentDay == 3){
	 * if(cal.getTime().getHours() > 21) cal.setTimeInMillis((4 * 24 * 3600000)
	 * + cal.getTimeInMillis()); } cal.set(Calendar.HOUR_OF_DAY, 21);
	 * cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
	 * setWarStartTime(_l1castle[i].getName(), cal);
	 * CastleTable.getInstance().updateCastle(_l1castle[i]); } } } }
	 */

	public boolean isNowWar(int castle_id) {
		if (castle_id <= 0) {
			return false;
		}
		return _is_now_war[castle_id - 1];
	}

	public void checkCastleWar(L1PcInstance player) {
		boolean ck = false;
		for (int i = 0; i < 8; i++) {
			if (_is_now_war[i]) {
				if (!ck) {
					player.sendPackets(new S_SystemMessage("???????? ????????????."),
							true);
					player.sendPackets(new S_SystemMessage(
							"???? ???????? ???? ?????? ?????? ????????."), true);
					ck = true;
				}
				String castleName = "??????";
				switch (i + 1) {
				case 2:
					castleName = "???? ????";
					break;
				case 3:
					castleName = "??????????";
					break;
				case 4:
					castleName = "??????";
					break;
				case 5:
					castleName = "????????";
					break;
				case 6:
					castleName = "??????";
					break;
				case 7:
					castleName = "??????";
					break;
				default:
					break;
				}
				String clanName = "";
				for (L1War war : L1World.getInstance().getWarList()) { // ????
																		// ????????
																		// ????
					if (war.GetCastleId() == i + 1) {
						clanName = war.GetDefenceClanName();
					}
				}
				player.sendPackets(new S_SystemMessage("[" + castleName + " = "
						+ clanName + " ????]"), true);
			}
		}
	}

	private void ??????????() {
		for (L1War war : L1World.getInstance().getWarList()) {
			war.CeaseWar(war.GetDefenceClanName(),
					war.GetEnemyClanName(war.GetDefenceClanName()));
			L1World.getInstance().broadcastPacketToAll(
					new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "["
							+ war.GetDefenceClanName() + " ????] VS ["
							+ war.GetEnemyClanName(war.GetDefenceClanName())
							+ " ????]?? ?????? ???? ???? ????????."), true);
		}
	}

	class WarTimer implements Runnable {
		private int castleid = 0;
		private int count = 0;
		public boolean on = true;

		public WarTimer(int _castleid) {
			castleid = _castleid;
		}

		@Override
		public void run() {
			try {
				if (!on)
					return;
				if (count-- > 0) {
					GeneralThreadPool.getInstance().schedule(this, 100);
					return;
				}
				if (_war_time[castleid]-- > 0) {
					count = 9;
					GeneralThreadPool.getInstance().schedule(this, 100);
					return;
				}
				_war_end[castleid] = true;
			} catch (Exception e) {
			}
		}
	}

	private void checkWarTime() {
		L1WarSpawn warspawn = null;
		for (int i = 0; i < 8; i++) {
			if (_war_start_time[i] <= System.currentTimeMillis()
					&& _war_end_time[i] >= System.currentTimeMillis()) {
				if (????????[i] == true) {
					try {
						L1World.getInstance().broadcastPacketToAll(
								new S_SystemMessage("????: "
										+ _l1castle[i].getName()
										+ " ???????? ??????????????!"), true); // %s?? ????????
																	// ??????????????.
						// L1World.getInstance().broadcastPacketToAll(new
						// S_PacketBox(S_PacketBox.MSG_WAR_END, i + 1), true);
						// // %s?? ???????? ????????????.
						_war_start_time[i] = LongType_setTime(
								_war_start_time[i],
								Config.ALT_WAR_INTERVAL_UNIT,
								Config.ALT_WAR_INTERVAL);
						_war_end_time[i] = LongType_setTime(_war_end_time[i],
								Config.ALT_WAR_INTERVAL_UNIT,
								Config.ALT_WAR_INTERVAL);
						_l1castle[i]
								.setWarTime(castle_Calendar_save(_war_start_time[i]));
						_l1castle[i].setTaxRate(10); // ????10????
						CastleTable.getInstance().updateCastle(_l1castle[i]);
						securityStart(_l1castle[i]);// ????????
						int castle_id = i + 1;
						L1FieldObjectInstance flag = null;
						L1CrownInstance crown = null;
						L1TowerInstance tower = null;
						for (L1Object l1object : L1World.getInstance()
								.getObject()) {
							if (l1object == null)
								continue;
							// ???? ?????????? ???? ??????
							if (l1object instanceof L1FieldObjectInstance) {
								flag = (L1FieldObjectInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										flag)) {
									flag.deleteMe();
								}
							}
							// ???????? ???? ??????, ???????? ???? ?????? spawn ????
							if (l1object instanceof L1CrownInstance) {
								crown = (L1CrownInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										crown)) {
									crown.deleteMe();
								}
							}

							if (l1object instanceof L1TowerInstance) {
								tower = (L1TowerInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										tower)) {
									tower.deleteMe();
								}
							}
						}

						warspawn = new L1WarSpawn();
						warspawn.SpawnTower(castle_id);
						for (L1DoorInstance door : DoorSpawnTable.getInstance()
								.getDoorList()) {
							if (door == null)
								continue;
							if (L1CastleLocation
									.checkInWarArea(castle_id, door)) {
								door.repairGate();
							}
						}
						_war_defence_clan[i] = null;
						_war_attack_clan[i] = null;
						_war_time[i] = 0;
						for (L1PcInstance tp : L1World.getInstance()
								.getAllPlayers()) {
							if (tp.war_zone) {
								tp.war_zone = false;
								tp.sendPackets(new S_NewCreateItem(1, 0, ""),
										true);
							}
						}
					} catch (Exception e) {
						System.out.println("???? ???? ?? ???? ???? ???? : \r\n");
						e.printStackTrace();
					}

				}

				????????[i] = false;

				if (_war_end[i] == true) { // ???? ???? ??????
					_war_end[i] = false;
					try {
						_is_now_war[i] = false;
						L1World.getInstance().broadcastPacketToAll(
								new S_SystemMessage("????: "
										+ _l1castle[i].getName()
										+ " ???????? ??????????????!"), true); // %s?? ????????
																	// ??????????????.
						_war_start_time[i] = LongType_setTime(
								_war_start_time[i],
								Config.ALT_WAR_INTERVAL_UNIT,
								Config.ALT_WAR_INTERVAL);
						_war_end_time[i] = LongType_setTime(_war_end_time[i],
								Config.ALT_WAR_INTERVAL_UNIT,
								Config.ALT_WAR_INTERVAL);
						_l1castle[i]
								.setWarTime(castle_Calendar_save(_war_start_time[i]));
						_l1castle[i].setTaxRate(10); // ????10????
						CastleTable.getInstance().updateCastle(_l1castle[i]);
						securityStart(_l1castle[i]);// ????????
						int castle_id = i + 1;
						L1FieldObjectInstance flag = null;
						L1CrownInstance crown = null;
						L1TowerInstance tower = null;
						for (L1Object l1object : L1World.getInstance()
								.getObject()) {
							if (l1object == null)
								continue;
							if (l1object instanceof L1FieldObjectInstance) {
								flag = (L1FieldObjectInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										flag))
									flag.deleteMe();
							}
							if (l1object instanceof L1CrownInstance) {
								crown = (L1CrownInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										crown))
									crown.deleteMe();
							}
							if (l1object instanceof L1TowerInstance) {
								tower = (L1TowerInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										tower))
									tower.deleteMe();
							}
						}
						warspawn = new L1WarSpawn();
						warspawn.SpawnTower(castle_id);
						for (L1DoorInstance door : DoorSpawnTable.getInstance()
								.getDoorList()) {
							if (L1CastleLocation
									.checkInWarArea(castle_id, door))
								door.repairGate();
						}
						for (L1War war : L1World.getInstance().get_wars()) {
							war.CastleWarThreadExit();
						}
						_war_defence_clan[i] = null;
						_war_attack_clan[i] = null;
						_war_time[i] = 0;
						for (L1PcInstance tp : L1World.getInstance()
								.getAllPlayers()) {
							if (tp.war_zone) {
								tp.war_zone = false;
								tp.sendPackets(new S_NewCreateItem(1, 0, ""),
										true);
							}
							if (tp.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.??????????)) {
								tp.getSkillEffectTimerSet().removeSkillEffect(
										L1SkillId.??????????);
								tp.sendPackets(new S_PacketBox(
										S_PacketBox.NONE_TIME_ICON, 0, 490),
										true);
							}
							int castleid = L1CastleLocation
									.getCastleIdByArea(tp);
							if (castleid != 0) {
								if (tp.getClan().getCastleId() == castleid) {
									tp.getInventory().storeItem(40308, 1); //????????
								}
							}
						}
						for (L1Clan c : L1World.getInstance().getAllClans()) {
							if (i + 1 == c.getCastleId()) {
								c.addWarpoint(2);

								ClanTable.getInstance().updateClan(c);
								if (i == 0)// ????
									L1SpawnUtil.spawn5(33067, 32764, (short) 4,
											6, 100669, 0, 1000 * 60 * 60, c);
								else if (i == 1)// ??????
									L1SpawnUtil.spawn5(32734, 32441, (short) 4,
											6, 100669, 0, 1000 * 60 * 60, c);
								else if (i == 3)// ??????
									L1SpawnUtil.spawn5(33449, 32800, (short) 4,
											6, 100669, 0, 1000 * 60 * 60, c);
								break;
							}
						}
					} catch (Exception e) {
					}
					continue;
				}

				if (_is_now_war[i] == false) {
					try {
						_is_now_war[i] = true;
						// ???? spawn ????
						L1Clan clan = null;

						warspawn = new L1WarSpawn();
						warspawn.SpawnFlag(i + 1);
						// warspawn.SpawnCrown(i + 1);
						// ?????? ?????? ??????

						for (L1DoorInstance door : DoorSpawnTable.getInstance()
								.getDoorList()) {
							if (L1CastleLocation.checkInWarArea(i + 1, door)) {
								door.setAutoStatus(0);// ?????????? ????
								door.repairGate();
							}
						}

						??????????();

						if (_l1castle[i].getCastleSecurity() == 1)
							securityStart(_l1castle[i]);// ????????
						L1World.getInstance().broadcastPacketToAll(
								new S_SystemMessage("????: "
										+ _l1castle[i].getName()
										+ " ???????? ??????????????!"), true); // %s?? ????????
																	// ??????????????.
						// L1World.getInstance().broadcastPacketToAll(new
						// S_PacketBox(S_PacketBox.MSG_WAR_BEGIN, i + 1), true);
						// // %s?? ???????? ??????????????.
						int[] loc = new int[3];

						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							int castleId = i + 1;
							if (L1CastleLocation.checkInWarArea(castleId, pc)
									&& !pc.isGm()) {
								clan = L1World.getInstance().getClan(
										pc.getClanname());
								if (clan != null) {
									if (clan.getCastleId() == castleId) {
										continue;
									}
								}

								loc = L1CastleLocation.getGetBackLoc(castleId);
								L1Teleport.teleport(pc, loc[0], loc[1],
										(short) loc[2], 5, true);
							}
						}
						for (L1Clan tclan : L1World.getInstance().getAllClans()) {
							if (i + 1 == tclan.getCastleId()) {
								_war_defence_clan[i] = tclan.getClanName();
								break;
							}
						}
						_war_time[i] = 60 * 50;
						GeneralThreadPool.getInstance().schedule(
								_war_timer[i] = new WarTimer(i), 1);

						try {
							for (L1GuardInstance guard : L1World.getInstance()
									.getAllGuard()) {
								int[] locb = L1CastleLocation.getWarArea(i + 1);
								if (guard.getMapId() == locb[4]
										&& guard.getX() >= locb[0]
										&& guard.getX() <= locb[1]
										&& guard.getY() >= locb[2]
										&& guard.getY() <= locb[3]) {
									Broadcaster
											.broadcastPacket(
													guard,
													new S_NpcChatPacket(
															guard,
															"???????? ??????????! ???? ?????????? ?????? ????!",
															0), true);
								}
							}
							for (L1CastleGuardInstance guard : L1World
									.getInstance().getAllCastleGuard()) {
								int[] locb = L1CastleLocation.getWarArea(i + 1);
								if (guard.getMapId() == locb[4]
										&& guard.getX() >= locb[0]
										&& guard.getX() <= locb[1]
										&& guard.getY() >= locb[2]
										&& guard.getY() <= locb[3]) {
									Broadcaster
											.broadcastPacket(
													guard,
													new S_NpcChatPacket(
															guard,
															"???????? ??????????! ???? ?????????? ?????? ????!",
															0), true);
								}
							}
						} catch (Exception e) {
						}
					} catch (Exception e) {
						System.out.println("???? ???? ?? ???? ???? : \r\n");
						e.printStackTrace();
					}
				}
			} else { // ???? ???? //???? ???? [else if (_war_end_time[i].before(Rtime))]
				if (????????[i] == true) {
					try {
						L1World.getInstance().broadcastPacketToAll(
								new S_SystemMessage("????: "
										+ _l1castle[i].getName()
										+ " ???????? ??????????????!"), true); // %s?? ????????
																	// ??????????????.
						// L1World.getInstance().broadcastPacketToAll(
						// new S_PacketBox(S_PacketBox.MSG_WAR_END, i + 1)); //
						// %s?? ???????? ????????????.
						_war_start_time[i] = LongType_setTime(
								_war_start_time[i],
								Config.ALT_WAR_INTERVAL_UNIT,
								Config.ALT_WAR_INTERVAL);
						_war_end_time[i] = LongType_setTime(_war_end_time[i],
								Config.ALT_WAR_INTERVAL_UNIT,
								Config.ALT_WAR_INTERVAL);
						_l1castle[i]
								.setWarTime(castle_Calendar_save(_war_start_time[i]));
						_l1castle[i].setTaxRate(10); // ????10????
						CastleTable.getInstance().updateCastle(_l1castle[i]);
						securityStart(_l1castle[i]);// ????????
						int castle_id = i + 1;
						L1FieldObjectInstance flag = null;
						L1CrownInstance crown = null;
						L1TowerInstance tower = null;
						for (L1Object l1object : L1World.getInstance()
								.getObject()) {
							if (l1object == null)
								continue;
							// ???? ?????????? ???? ??????
							if (l1object instanceof L1FieldObjectInstance) {
								flag = (L1FieldObjectInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										flag)) {
									flag.deleteMe();
								}
							}
							// ???????? ???? ??????, ???????? ???? ?????? spawn ????
							if (l1object instanceof L1CrownInstance) {
								crown = (L1CrownInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										crown)) {
									crown.deleteMe();
								}
							}

							if (l1object instanceof L1TowerInstance) {
								tower = (L1TowerInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										tower)) {
									tower.deleteMe();
								}
							}
						}

						warspawn = new L1WarSpawn();
						warspawn.SpawnTower(castle_id);
						for (L1DoorInstance door : DoorSpawnTable.getInstance()
								.getDoorList()) {
							if (L1CastleLocation
									.checkInWarArea(castle_id, door)) {
								door.repairGate();
							}
						}
						_war_defence_clan[i] = null;
						_war_attack_clan[i] = null;
						_war_time[i] = 0;
						for (L1PcInstance tp : L1World.getInstance()
								.getAllPlayers()) {
							if (tp.war_zone) {
								tp.war_zone = false;
								tp.sendPackets(new S_NewCreateItem(1, 0, ""),
										true);
							}
						}
					} catch (Exception e) {
						System.out.println("???? ???? ?? ???? ???? ???? : \r\n");
						e.printStackTrace();
					}
				}
				????????[i] = false;

				if (_is_now_war[i] == true) {
					try {
						_is_now_war[i] = false;
						try {
							WarTimer timer = _war_timer[i];
							if (timer != null) {
								timer.on = false;
								_war_timer[i] = null;
							}
						} catch (Exception e) {
						}
						L1World.getInstance().broadcastPacketToAll(
								new S_SystemMessage("????: "
										+ _l1castle[i].getName()
										+ " ???????? ??????????????!"), true); // %s?? ????????
																	// ??????????????.
						// L1World.getInstance().broadcastPacketToAll(
						// new S_PacketBox(S_PacketBox.MSG_WAR_END, i + 1),
						// true); // %s?? ???????? ????????????.
						_war_start_time[i] = LongType_setTime(
								_war_start_time[i],
								Config.ALT_WAR_INTERVAL_UNIT,
								Config.ALT_WAR_INTERVAL);
						_war_end_time[i] = LongType_setTime(_war_end_time[i],
								Config.ALT_WAR_INTERVAL_UNIT,
								Config.ALT_WAR_INTERVAL);
						_l1castle[i]
								.setWarTime(castle_Calendar_save(_war_start_time[i]));
						_l1castle[i].setTaxRate(10); // ????10????
						// _l1castle[i].setPublicMoney(0); // ??????????
						CastleTable.getInstance().updateCastle(_l1castle[i]);
						securityStart(_l1castle[i]);// ????????
						int castle_id = i + 1;
						L1FieldObjectInstance flag = null;
						L1CrownInstance crown = null;
						L1TowerInstance tower = null;
						for (L1Object l1object : L1World.getInstance()
								.getObject()) {
							if (l1object == null)
								continue;
							// ???? ?????????? ???? ??????
							if (l1object instanceof L1FieldObjectInstance) {
								flag = (L1FieldObjectInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										flag)) {
									flag.deleteMe();
								}
							}
							// ???????? ???? ??????, ???????? ???? ?????? spawn ????
							if (l1object instanceof L1CrownInstance) {
								crown = (L1CrownInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										crown)) {
									crown.deleteMe();
								}
							}

							if (l1object instanceof L1TowerInstance) {
								tower = (L1TowerInstance) l1object;
								if (L1CastleLocation.checkInWarArea(castle_id,
										tower)) {
									tower.deleteMe();
								}
							}
						}

						warspawn = new L1WarSpawn();
						warspawn.SpawnTower(castle_id);
						for (L1DoorInstance door : DoorSpawnTable.getInstance()
								.getDoorList()) {
							if (L1CastleLocation
									.checkInWarArea(castle_id, door)) {
								door.repairGate();
							}
						}
						_war_defence_clan[i] = null;
						_war_attack_clan[i] = null;
						_war_time[i] = 0;
						for (L1PcInstance tp : L1World.getInstance()
								.getAllPlayers()) {
							if (tp.war_zone) {
								tp.war_zone = false;
								tp.sendPackets(new S_NewCreateItem(1, 0, ""),
										true);
							}
							if (tp.getSkillEffectTimerSet().hasSkillEffect(
									L1SkillId.??????????)) {
								tp.getSkillEffectTimerSet().removeSkillEffect(
										L1SkillId.??????????);
								tp.sendPackets(new S_PacketBox(
										S_PacketBox.NONE_TIME_ICON, 0, 490),
										true);
							}
							int castleid = L1CastleLocation
									.getCastleIdByArea(tp);
							if (castleid != 0) {
								if (tp.getClan().getCastleId() == castleid) {
									tp.getInventory().storeItem(40308, 1);//????????
								}
							}
						}
						for (L1Clan c : L1World.getInstance().getAllClans()) {
							if (i + 1 == c.getCastleId()) {
								c.addWarpoint(2);
								ClanTable.getInstance().updateClan(c);
								if (i == 0)// ????
									L1SpawnUtil.spawn5(33067, 32764, (short) 4,
											6, 100669, 0, 1000 * 60 * 60, c);
								else if (i == 1)// ??????
									L1SpawnUtil.spawn5(32734, 32441, (short) 4,
											6, 100669, 0, 1000 * 60 * 60, c);
								else if (i == 3)// ??????
									L1SpawnUtil.spawn5(33449, 32800, (short) 4,
											6, 100669, 0, 1000 * 60 * 60, c);
								break;
							}
						}

						// ???? ???????? ???????? '?????? ????' ???? ????, ???? ????
					} catch (Exception e) {
						System.out.println("???? ???? ??  ???? ???? : \r\n");
						e.printStackTrace();
					}
				}
			}
		}
	}

	public void AttackClanSetting(int castleid, String name) {
		_war_attack_clan[castleid - 1] = name;
		try {
			WarTimer timer = _war_timer[castleid - 1];
			if (timer != null) {
				timer.on = false;
				_war_timer[castleid - 1] = null;
			}
			if (AttackDefenceCheck(castleid) == 1) {
				// Calendar cal = (Calendar) Calendar.getInstance().clone();
				// cal.set(cal.getTime().getYear(), cal.getTime().getMonth(),
				// cal.getTime().getDate(), 21, 0);
				// int time = (int) ((cal.getTimeInMillis() -
				// System.currentTimeMillis())/1000);
				// Timestamp ts = new Timestamp(System.currentTimeMillis());
				// ts.setHours(21); ts.setMinutes(0); ts.setSeconds(0);
				// int time = (int) ((ts.getTime() -
				// System.currentTimeMillis())/1000);
				int time = (int) ((_war_end_time[castleid - 1] - System
						.currentTimeMillis()) / 1000);
				_war_time[castleid - 1] = time;
			} else
				_war_time[castleid - 1] = 20 * 60;
			GeneralThreadPool.getInstance().schedule(
					_war_timer[castleid - 1] = new WarTimer(castleid - 1), 1);
		} catch (Exception e) {
		}
	}

	public int AttackDefenceCheck(int castleid) {
		try {
			if (_war_attack_clan[castleid - 1] == null
					|| _war_attack_clan[castleid - 1].equalsIgnoreCase(""))
				return 1;
			return _war_defence_clan[castleid - 1]
					.equalsIgnoreCase(_war_attack_clan[castleid - 1]) ? 1 : 2;
		} catch (Exception e) {
			return 1;
		}
	}

	public void WarTime_SendPacket(int castleid, L1PcInstance pc) {
		if (isNowWar(castleid)) {
			int warType = AttackDefenceCheck(castleid);
			String name = _war_defence_clan[castleid - 1];
			if (warType == 2)
				name = _war_attack_clan[castleid - 1];
			pc.sendPackets(new S_NewCreateItem(warType,
					_war_time[castleid - 1], name), true);

			if (pc.getClanRank() >= L1Clan.CLAN_RANK_GUARDIAN
					&& !pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.??????????)) {
				pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.??????????,
						3600000);
				pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 1,
						490), true);
			} else if (pc.getClanRank() < L1Clan.CLAN_RANK_GUARDIAN
					&& pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.??????????)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.??????????);
				pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0,
						490), true);
			}
		} else {
			pc.war_zone = false;
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.??????????)) {
				pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.??????????);
				pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0,
						490), true);
			}
		}
	}

	private Calendar castle_Calendar_save(long time) {
		// System.out.println("???? ?? Time : "+time);
		Calendar cal = (Calendar) Calendar.getInstance().clone();
		Date date = new Date();
		date.setTime(time);
		cal.setTime(date);
		return cal;
	}

	private void securityStart(L1Castle castle) {
		int castleId = castle.getId();
		int a = 0, b = 0, c = 0, d = 0, e = 0;
		int[] loc = new int[3];
		L1Clan clan = null;

		switch (castleId) {
		case 1:
		case 2:
		case 3:
		case 4:
			a = 52;
			b = 248;
			c = 249;
			d = 250;
			e = 251;
			break;
		case 5:
		case 6:
		case 7:
		default:
			break;
		}

		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if ((pc.getMapId() == a || pc.getMapId() == b || pc.getMapId() == c
					|| pc.getMapId() == d || pc.getMapId() == e)
					&& !pc.isGm() && !pc.isSGm()) {
				clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null) {
					if (clan.getCastleId() == castleId) {
						continue;
					}
				}
				loc = L1CastleLocation.getGetBackLoc(castleId);
				L1Teleport
						.teleport(pc, loc[0], loc[1], (short) loc[2], 5, true);
			}
		}
		castle.setCastleSecurity(0);
		CastleTable.getInstance().updateCastle(castle);
		CharacterTable.getInstance().updateLoc(castleId, a, b, c, d, e);
	}

}
