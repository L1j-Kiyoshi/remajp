/* This program is free software; you can redistribute it and/or modify
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
package l1j.server.server.clientpackets;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_MOVE;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.CrockSystem;
import l1j.server.GameSystem.PetRacing;
import l1j.server.GameSystem.Astar.World;
import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.GameSystem.NavalWarfare.NavalWarfare;
import l1j.server.Warehouse.ClanWarehouse;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.GMCommands;
import l1j.server.server.Jibaetower;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.EvaSystemTable;
import l1j.server.server.model.AcceleratorChecker;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Dungeon;
import l1j.server.server.model.DungeonRandom;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_MoveCharPacket;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_NewSkillIcons;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_SMPacketBox;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1EvaSystem;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;

public class C_MoveChar extends ClientBasePacket {
	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };
	private static final S_SystemMessage sm = new S_SystemMessage("방또는 홀 대여를 먼저 해주세요.");

	// 이동
	public boolean ckok(int x, int y, int m, int h) {
		int clocx = x + HEADING_TABLE_X[h];
		int clocy = y + HEADING_TABLE_Y[h];
		boolean result = false;
		if (m == 4) {
			if (x == 33636 && y == 32673) {
				return true;
			}
			if (x == 33627 && y == 32682) {
				return true;
			}
			if (x == 33628 && y == 32674) {
				return true;
			}
			if (x == 33635 && y == 32681) {
				return true;
			}

			if (clocx == 33636 && clocy == 32673) {
				return true;
			}
			if (clocx == 33627 && clocy == 32682) {
				return true;
			}
			if (clocx == 33628 && clocy == 32674) {
				return true;
			}
			if (clocx == 33635 && clocy == 32681) {
				return true;
			}
		}
		return result;
	}
	
	
	public void commit(L1PcInstance pc, int _persent) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM _Auto_Check WHERE Name=?");
			pstm.setString(1, pc.getName());
			rs = pstm.executeQuery();
			if (rs.next()) {
				pc.Auto_check = true;
			} else {
				Connection con2 = null;
				PreparedStatement pstm2 = null;
				try {
					con2 = L1DatabaseFactory.getInstance().getConnection();
					pstm2 = con2
							.prepareStatement("INSERT INTO _Auto_Check SET Account=?, Name=?, IP=?, Persent=?");
					pstm2.setString(1, pc.getAccountName());
					pstm2.setString(2, pc.getName());
					pstm2.setString(3, pc.getNetConnection().getIp());
					pstm2.setInt(4, _persent);
					pstm2.executeUpdate();
				} catch (SQLException e) {
					e.printStackTrace();
				} finally {
					SQLUtil.close(pstm2);
					SQLUtil.close(con2);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public C_MoveChar(byte decrypt[], LineageClient client) throws Exception {
		super(decrypt);
		Random _Random = new Random(System.nanoTime());

		try {
			int locx = readH();
			int locy = readH();
			int heading = readC();
			
			L1PcInstance pc = client.getActiveChar();
			if (pc == null)
				return;

			/** 파티중 지도표시 **/
			if (pc.isInParty()) {
				int Move = _Random.nextInt(10) + 1;
				if(Move >= 8) pc.getParty().updateMiniHP(pc);
			}

			if (heading > 7) {
				heading = Integer.parseInt(Integer.toHexString(heading).substring(1, 2));
			}

			if (heading < 0 || heading > 7)
				return;

			if (pc.텔대기()) {
				// System.out.println("55555");
				return;
			}

			if (Config.뚫어방어사용 && pc.getMapId() != 2100 && pc.getMapId() != 2699) {
				if (!World.isThroughObject(pc.getX(), pc.getY(), pc.getMapId(),
						heading)) {
					if (GMCommands.길팅체크) {
						System.out.println("길팅 : 뚫어체크1 name : " + pc.getName());
					}
					pc.getMoveState().setHeading(heading);
					pc.sendPackets(new S_PacketBox(S_PacketBox.유저빽스탭, pc));
					Broadcaster.broadcastPacket(pc, new S_ChangeHeading(pc));
					return;
				}
				// 타일이 이동불가가 아니고 만약 아무것도 없으면 이동가능으로 셋팅
				if (World.문이동(pc.getX(), pc.getY(), pc.getMapId(), heading)) {
					if (GMCommands.길팅체크) {
						System.out.println("길팅 : 뚫어체크2 name : " + pc.getName());
					}
					pc.getMoveState().setHeading(heading);
					pc.sendPackets(new S_PacketBox(S_PacketBox.유저빽스탭, pc));
					Broadcaster.broadcastPacket(pc, new S_ChangeHeading(pc));
					return;
				}
			}
			

			locx += HEADING_TABLE_X[heading];
			locy += HEADING_TABLE_Y[heading];

			if (Config.유체이탈방어사용) {
				if (pc.tempx != 0 && pc.tempy != 0) {
					int calcx = (int) pc.tempx - locx;
					int calcy = (int) pc.tempy - locy;
					// System.out.println(calcx+" "+calcy);
					if (Math.abs(calcx) > 1 || Math.abs(calcy) > 1) {
						if (GMCommands.길팅체크) {
							System.out.println("길팅 : 유체이탈체크 name : "+ pc.getName());
						}
						pc.getMoveState().setHeading(heading);
						pc.sendPackets(new S_PacketBox(S_PacketBox.유저빽스탭, pc));
						Broadcaster
								.broadcastPacket(pc, new S_ChangeHeading(pc));
						/*
						 * if(pc.Sabutelok()){ pc.setTelType(4);
						 * pc.sendPackets(new S_SabuTell(pc), true); }
						 */
						return;
					}
				}
			}

			if (pc.getTradeID() > 0) {
				L1Object tp = L1World.getInstance().findObject(pc.getTradeID());
				if (tp != null
						&& (tp instanceof L1NpcShopInstance || tp instanceof GambleInstance)) {
					if (((L1NpcInstance) tp).Npc_trade) {
						L1Trade t = new L1Trade();
						t.TradeCancel(pc);
					} else
						pc.setTradeID(0);
				}
			}

			if (pc.getMapId() != 2100 && pc.getMapId() != 2699) {
				boolean ck = false;
				for (L1PcInstance objs : L1World.getInstance().getAllPlayers()) {
					// for(L1PcInstance objs :
					// pc.getNearObjects().getKnownPlayers()){
					if (objs.isDead()) {
						continue;
					}
					if (objs.getX() == locx && objs.getY() == locy
							&& objs.getMapId() == pc.getMapId()) {
						if (objs.isGm() && objs.isGmInvis()) {
						} else {
							ck = true;
							break;
						}
					}
				}
				if (ck) {
					if (GMCommands.길팅체크) {
						System.out.println("길팅 : 중첩체크 name : " + pc.getName());
					}
					// pc.updateObject();
					pc.getMoveState().setHeading(heading);
					pc.sendPackets(new S_PacketBox(S_PacketBox.유저빽스탭, pc));
					Broadcaster.broadcastPacket(pc, new S_ChangeHeading(pc));

					try {
						ClanWarehouse clanWarehouse = null;
						L1Clan clan = L1World.getInstance().getClan(
								pc.getClanname());
						if (clan != null)
							clanWarehouse = WarehouseManager.getInstance()
									.getClanWarehouse(clan.getClanName());
						if (clanWarehouse != null)
							clanWarehouse.unlock(pc.getId());
					} catch (Exception e) {
					}
					if (pc.isPinkName()) {
						pc.sendPackets(
								new S_PinkName(pc.getId(), pc
										.getSkillEffectTimerSet()
										.getSkillEffectTimeSec(
												L1SkillId.STATUS_PINK_NAME)),
								true);
						Broadcaster.broadcastPacket(
								pc,
								new S_PinkName(pc.getId(), pc
										.getSkillEffectTimerSet()
										.getSkillEffectTimeSec(
												L1SkillId.STATUS_PINK_NAME)),
								true);
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
						pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), pc.getSkillEffectTimerSet()
										.getSkillEffectTimeSec(L1SkillId.WIND_SHACKLE)), true);

					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_DRAGONPERL)) {
						int reminingtime = pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.STATUS_DRAGONPERL);
						pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONPERL,8, (reminingtime / 4) - 2), true);
						pc.sendPackets(new S_DRAGONPERL(pc.getId(), 8), true);
						Broadcaster.broadcastPacket(pc,new S_DRAGONPERL(pc.getId(), 8), true);
						pc.set진주속도(1);
					}
					// pc.setTelType(4);
					// pc.sendPackets(new S_SabuTell(pc), true);
					return;
				}
			}

			if (pc.getLevel() >= 52) { // 지정 레벨
				if (pc.getMapId() == 2010) {// 폭풍수련던전
					// L1Teleport.teleport(pc, 33080, 33390, (short) 4, 5,
					// true);
					pc.sendPackets(new S_SkillSound(pc.getId(), 169), true);
					Broadcaster.broadcastPacket(pc, new S_SkillSound(
							pc.getId(), 169), true);
					L1Teleport.teleport(pc, new L1Location(33080, 33390, 4), 5,
							false);// 텔 패킷 미사용
					return;
				}
			
			} 
			
			if (pc.getLevel() >= 70) { // 지정 레벨
				if (pc.getMapId() == 1 || pc.getMapId() == 2) {// 말던
					// L1Teleport.teleport(pc, 33080, 33390, (short) 4, 5,
					// true);
					pc.sendPackets(new S_SkillSound(pc.getId(), 169), true);
					Broadcaster.broadcastPacket(pc, new S_SkillSound(
							pc.getId(), 169), true);
					L1Teleport.teleport(pc, new L1Location(33080, 33390, 4), 5,
							false);// 텔 패킷 미사용
					return;
				}
			
			} 
			else if (pc.getMapId() == 1931) {
				if (pc.getInventory().getSize() >= 180) {
					pc.sendPackets(
							new S_SystemMessage("인벤토리를 갯수를 비운 후 입장해주세요."), true);
					L1Teleport.teleport(pc, 33443, 32797, (short) 4, 5, true);
					return;
				}
				/*
				 * int count = pc.getInventory().countItems(60501); if(count >=
				 * 5){ L1Teleport.teleport(pc, 33443, 32797, (short) 4, 5,
				 * true); return; }
				 */
			}

			int result;
			result = pc.getAcceleratorChecker().checkInterval(
					AcceleratorChecker.ACT_TYPE.MOVE);
			if (Config.CHECK_MOVE_INTERVAL == true) {
			if (result == AcceleratorChecker.R_DISCONNECTED) {
				if (GMCommands.길팅체크) {
					System.out.println("길팅 : 스핵체크 name : " + pc.getName());
				}
				pc.getMoveState().setHeading(heading);
				pc.sendPackets(new S_PacketBox(S_PacketBox.유저빽스탭, pc));
				Broadcaster.broadcastPacket(pc, new S_ChangeHeading(pc));
				
				if(pc.Sabutelok()){ 
				pc.setTelType(4); pc.sendPackets(new S_SabuTell(pc), true); 
				}
				 
				return;
			  }
			}
		 
		  /*  *//** SPR체크 **//*
			if (pc.MoveSpeedCheck >= 1) {
				if (pc.MoveSpeedCheck == 1) {
					pc.MoveSpeed = System.currentTimeMillis();
					pc.sendPackets(new S_SystemMessage(pc,"[체크시작]"), true);
				}
				pc.MoveSpeedCheck++;
				if (pc.MoveSpeedCheck >= 12) {
					pc.MoveSpeedCheck = 0;
					double k = (System.currentTimeMillis() - pc.MoveSpeed) / 10D;
					String s = String.format("%.0f", k);
					pc.MoveSpeed = 0;
					pc.sendPackets(new S_SystemMessage(pc,"-----------------------------------------"), true);
					pc.sendPackets(new S_SystemMessage(pc,"해당변신은 " + s + "이 이속으로 적절한값입니다."), true);
					pc.sendPackets(new S_SystemMessage(pc,"-----------------------------------------"), true);
				}
			}
			   *//** SPR체크 **/

			pc.플레이어상태 = pc.이동_상태;
			pc.상태시간 = System.currentTimeMillis() + 2000;

			pc.getSkillEffectTimerSet().killSkillEffectTimer(
					L1SkillId.MEDITATION);
			pc.setCallClanId(0);

			if (!pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.ABSOLUTE_BARRIER)) { // 아브소르트바리아중은 아니다
				pc.setRegenState(REGENSTATE_MOVE);
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.STATUS_안전모드)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(
						L1SkillId.STATUS_안전모드);
			}

			if (pc.하딘보스룸입장 && pc.getMapId() <= 9001 && pc.getMapId() >= 9100) {
				pc.하딘보스룸입장 = false;
			}

			if (pc.getParty() != null) {
				if (pc.getMapId() >= 9001 && pc.getMapId() <= 9100
						&& pc.getX() == 32723 && pc.getY() == 32848
						&& (heading == 7 || heading == 6 || heading == 5)) {
					if (!pc.getParty().isLeader(pc)) {
						pc.하딘보스룸입장 = true;
					}
				}
			} else {
				pc.하딘보스룸입장 = false;
			}

			// 배 낑김 현상으로 인한 탈출루트
			if (pc.getMapId() == 5 || pc.getMapId() == 6 || pc.getMapId() == 83
					|| pc.getMapId() == 84 || pc.getMapId() == 446
					|| pc.getMapId() == 447|| pc.getMapId() == 1700) {
				long servertime = GameTimeClock.getInstance().getGameTime()
						.getSeconds();
				long nowtime = servertime % 86400;
				// if(nowtime < 0){
				// nowtime=nowtime-nowtime-nowtime;
				// }
				boolean timetel = false;
				if ((nowtime >= 30 * 360 + 20 && nowtime < 40 * 360)
						|| (nowtime >= 60 * 360 + 20 && nowtime < 70 * 360)
						|| (nowtime >= 90 * 360 + 20 && nowtime < 100 * 360)
						|| (nowtime >= 120 * 360 + 20 && nowtime < 130 * 360)
						|| (nowtime >= 150 * 360 + 20 && nowtime < 160 * 360)
						|| (nowtime >= 180 * 360 + 20 && nowtime < 190 * 360)
						|| (nowtime >= 210 * 360 + 20 && nowtime < 220 * 360)
						|| (nowtime >= 20 && nowtime < 10 * 360)

				) {
					if (pc.getMapId() == 5) {// 말섬 -> 본토
						pc.dx = 32561;
						pc.dy = 32727;
						pc.dh = 4;
						pc.dm = 4;
						timetel = true;
					} else if (pc.getMapId() == 84) {// 잊섬 -> 본토
						pc.dx = 33434;
						pc.dy = 33480;
						pc.dh = 4;
						pc.dm = 4;
						timetel = true;
					} else if (pc.getMapId() == 447) { // 숨겨진 선착장 -> 해적섬
						pc.dx = 32297;
						pc.dy = 33074;
						pc.dh = 4;
						pc.dm = 440;
						timetel = true;
					}
				} else if ((nowtime >= 15 * 360 + 20 && nowtime < 25 * 360)
						|| (nowtime >= 45 * 360 + 20 && nowtime < 55 * 360)
						|| (nowtime >= 75 * 360 + 20 && nowtime < 85 * 360)
						|| (nowtime >= 105 * 360 + 20 && nowtime < 115 * 360)
						|| (nowtime >= 135 * 360 + 20 && nowtime < 145 * 360)
						|| (nowtime >= 165 * 360 + 20 && nowtime < 175 * 360)
						|| (nowtime >= 195 * 360 + 20 && nowtime < 205 * 360)
						|| (nowtime >= 225 * 360 + 20 && nowtime < 235 * 360)

				) {
					if (pc.getMapId() == 6) {// 본토 -> 말섬
						pc.dx = 32632;
						pc.dy = 32965;
						pc.dh = 4;
						pc.dm = 0;
						timetel = true;
					} else if (pc.getMapId() == 83) {// 본토 -> 잊섬
						pc.dx = 32621;
						pc.dy = 33014;
						pc.dh = 4;
						pc.dm = 1700;
						timetel = true;
					} else if (pc.getMapId() == 446) { // 해적섬 -> 숨겨진 선착장
						pc.dx = 32751;
						pc.dy = 32859;
						pc.dh = 4;
						pc.dm = 445;
						timetel = true;
					}
				}
				if (timetel) {
					pc.setTelType(1);
					pc.sendPackets(new S_SabuTell(pc), true);
					return;
				}
			}

			if ((locx >= 32744 && locx <= 32746) && locy == 32808) {
				if (pc.getMapId() >= 16896 && pc.getMapId() <= 17196) {
					pc.dx = 32600;
					pc.dy = 32930;
					pc.dh = 4;
					pc.dm = 0;
					pc.setTelType(1);
					pc.sendPackets(new S_SabuTell(pc), true);
					return;
				}
			}
			if ((locx >= 32744 && locx <= 32746) && locy == 32807) {
				boolean swich = false;
				if (pc.getMapId() >= 17920 && pc.getMapId() <= 18220) {
					pc.dx = 32632;
					pc.dy = 32761;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 18944 && pc.getMapId() <= 19244) {
					pc.dx = 33437;
					pc.dy = 32789;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 19968 && pc.getMapId() <= 20268) {
					pc.dx = 33986;
					pc.dy = 33312;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 24064 && pc.getMapId() <= 24364) {
					pc.dx = 34066;
					pc.dy = 32254;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 20992 && pc.getMapId() <= 21292) {
					pc.dx = 32628;
					pc.dy = 33167;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 22016 && pc.getMapId() <= 22316) {
					pc.dx = 33116;
					pc.dy = 33379;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 23040 && pc.getMapId() <= 23340) {
					pc.dx = 33605;
					pc.dy = 33275;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 25088 && pc.getMapId() <= 25388) {
					pc.dx = 32449;
					pc.dy = 33048;
					pc.dh = 4;
					pc.dm = 440;
					swich = true;
				}
				if (swich) {
					pc.setTelType(1);
					pc.sendPackets(new S_SabuTell(pc), true);
					return;
				}
			}
			if ((locx == 32744) && locy == 32803) {
				boolean swich = false;
				if (pc.getMapId() >= 17408 && pc.getMapId() <= 17708) {
					pc.dx = 32631;
					pc.dy = 32761;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				}
				if (swich) {
					pc.setTelType(1);
					pc.sendPackets(new S_SabuTell(pc), true);
					return;
				}
			}
			if ((locx >= 32745 && locx <= 32746) && locy == 32803) {
				boolean swich = false;
				if (pc.getMapId() >= 16384 && pc.getMapId() <= 16684) {
					if (locx != 32745) {
						pc.dx = 32600;
						pc.dy = 32930;
						pc.dh = 4;
						pc.dm = 0;
						swich = true;
					}

				} else if (pc.getMapId() >= 18432 && pc.getMapId() <= 18732) {
					pc.dx = 33437;
					pc.dy = 32789;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 19456 && pc.getMapId() <= 19756) {
					pc.dx = 33986;
					pc.dy = 33312;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 23552 && pc.getMapId() <= 23852) {
					pc.dx = 34066;
					pc.dy = 32254;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 20480 && pc.getMapId() <= 20780) {
					pc.dx = 32628;
					pc.dy = 33167;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 21504 && pc.getMapId() <= 21804) {
					pc.dx = 33116;
					pc.dy = 33379;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 22528 && pc.getMapId() <= 22828) {
					pc.dx = 33605;
					pc.dy = 33275;
					pc.dh = 4;
					pc.dm = 4;
					swich = true;
				} else if (pc.getMapId() >= 24576 && pc.getMapId() <= 24876) {
					pc.dx = 32449;
					pc.dy = 33048;
					pc.dh = 4;
					pc.dm = 440;
					swich = true;
				}
				if (swich) {
					pc.setTelType(1);
					pc.sendPackets(new S_SabuTell(pc), true);
					return;
				}
			}

			if ((locx == 32600 && locy == 32930 && pc.getMapId() == 0)
					|| (locx == 33986 && locy == 33312 && pc.getMapId() == 4)
					|| (locx == 33437 && locy == 32789 && pc.getMapId() == 4)
					|| (locx == 34066 && locy == 32254 && pc.getMapId() == 4)
					|| (locx == 32632 && locy == 32761 && pc.getMapId() == 4)
					|| (locx == 33605 && locy == 33275 && pc.getMapId() == 4)
					|| (locx == 33116 && locy == 33379 && pc.getMapId() == 4)
					|| (locx == 32628 && locy == 33167 && pc.getMapId() == 4)
					|| (locx == 32451 && locy == 33046 && pc.getMapId() == 440)) {
				short keymap = 0;
				L1ItemInstance[] key = null;
				if (pc.getInventory().checkItem(40312)) {
					key = pc.getInventory().findItemsId(40312);
				}
				if (pc.getInventory().checkItem(49312)) {
					key = pc.getInventory().findItemsId(49312);
				}
				if (key != null) {
					if (key.length == 0) {
						pc.sendPackets(sm);
					}
					for (int i = 0; i < key.length; i++) {
						if (key[i].getEndTime().getTime() > System
								.currentTimeMillis()) {
							if (((locx == 32600 && locy == 32930 && pc
									.getMapId() == 0) && (((short) key[i]
									.getKey() >= 16384 && (short) key[i]
									.getKey() <= 16684) || ((short) key[i]
									.getKey() >= 16896 && (short) key[i]
									.getKey() <= 17196)))
									|| ((locx == 33986 && locy == 33312 && pc
											.getMapId() == 4) && (((short) key[i]
											.getKey() >= 19456 && (short) key[i]
											.getKey() <= 19756) || ((short) key[i]
											.getKey() >= 19968 && (short) key[i]
											.getKey() <= 20268)))
									|| ((locx == 33437 && locy == 32789 && pc
											.getMapId() == 4) && (((short) key[i]
											.getKey() >= 18432 && (short) key[i]
											.getKey() <= 18732) || ((short) key[i]
											.getKey() >= 18944 && (short) key[i]
											.getKey() <= 19244)))
									|| ((locx == 34066 && locy == 32254 && pc
											.getMapId() == 4) && (((short) key[i]
											.getKey() >= 23552 && (short) key[i]
											.getKey() <= 23852) || ((short) key[i]
											.getKey() >= 24064 && (short) key[i]
											.getKey() <= 24364)))
									|| ((locx == 32632 && locy == 32761 && pc
											.getMapId() == 4) && (((short) key[i]
											.getKey() >= 17408 && (short) key[i]
											.getKey() <= 17708) || ((short) key[i]
											.getKey() >= 17920 && (short) key[i]
											.getKey() <= 18220)))
									|| ((locx == 33605 && locy == 33275 && pc
											.getMapId() == 4) && (((short) key[i]
											.getKey() >= 22528 && (short) key[i]
											.getKey() <= 22828) || ((short) key[i]
											.getKey() >= 23040 && (short) key[i]
											.getKey() <= 23340)))
									|| ((locx == 33116 && locy == 33379 && pc
											.getMapId() == 4) && (((short) key[i]
											.getKey() >= 21504 && (short) key[i]
											.getKey() <= 21804) || ((short) key[i]
											.getKey() >= 22016 && (short) key[i]
											.getKey() <= 22316)))
									|| ((locx == 32628 && locy == 33167 && pc
											.getMapId() == 4) && (((short) key[i]
											.getKey() >= 20480 && (short) key[i]
											.getKey() <= 20780) || ((short) key[i]
											.getKey() >= 20992 && (short) key[i]
											.getKey() <= 21292)))
									|| ((locx == 32451 && locy == 33046 && pc
											.getMapId() == 440) && (((short) key[i]
											.getKey() >= 24576 && (short) key[i]
											.getKey() <= 24876) || ((short) key[i]
											.getKey() >= 25088 && (short) key[i]
											.getKey() <= 25388)))) {
								keymap = (short) key[i].getKey();
								break;
							}
						}
					}
					if (keymap != 0) {
						if (keymap >= 16384 && keymap <= 16684) {
							pc.dx = 32746;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 16896 && keymap <= 17196) {
							pc.dx = 32744;
							pc.dy = 32808;
							pc.dh = 6;
						} else if (keymap >= 17408 && keymap <= 17708) {
							pc.dx = 32744;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 17920 && keymap <= 18220) {
							pc.dx = 32745;
							pc.dy = 32807;
							pc.dh = 6;
						} else if (keymap >= 18432 && keymap <= 18732) {
							pc.dx = 32745;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 18944 && keymap <= 19244) {
							pc.dx = 32745;
							pc.dy = 32807;
							pc.dh = 6;
						} else if (keymap >= 19456 && keymap <= 19756) {
							pc.dx = 32745;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 19968 && keymap <= 20268) {
							pc.dx = 32745;
							pc.dy = 32807;
							pc.dh = 6;
						} else if (keymap >= 23552 && keymap <= 23852) {
							pc.dx = 32745;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 24064 && keymap <= 24364) {
							pc.dx = 32745;
							pc.dy = 32807;
							pc.dh = 6;
						} else if (keymap >= 20480 && keymap <= 20780) {
							pc.dx = 32745;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 20992 && keymap <= 21292) {
							pc.dx = 32745;
							pc.dy = 32807;
							pc.dh = 6;
						} else if (keymap >= 21504 && keymap <= 21804) {
							pc.dx = 32745;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 22016 && keymap <= 22316) {
							pc.dx = 32745;
							pc.dy = 32807;
							pc.dh = 6;
						} else if (keymap >= 22528 && keymap <= 22828) {
							pc.dx = 32745;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 23040 && keymap <= 23340) {
							pc.dx = 32745;
							pc.dy = 32807;
							pc.dh = 6;
						} else if (keymap >= 24576 && keymap <= 24876) {
							pc.dx = 32745;
							pc.dy = 32803;
							pc.dh = 6;
						} else if (keymap >= 25088 && keymap <= 25388) {
							pc.dx = 32745;
							pc.dy = 32807;
							pc.dh = 6;
						}
						pc.dm = keymap;
						pc.setTelType(1);
						pc.sendPackets(new S_SabuTell(pc), true);
						key = null;
						return;
					} else {
						if (key.length == 0)
							pc.sendPackets(sm);
						else
							pc.sendPackets(new S_SystemMessage("다른 여관의 키를 소유 하고있습니다."), true);
						key = null;
					}
				}
			} else if (Dungeon.getInstance().dg(locx, locy,
					pc.getMap().getId(), pc)) { // 지하 감옥에 텔레포트 했을 경우
				pc.setTelType(1);
				pc.sendPackets(new S_SabuTell(pc), true);
				return;
			}

			if (pc.getMapId() >= 5001 && pc.getMapId() <= 5069) {
				if (pc.getX() == 32768 && pc.getY() == 32759) {
					int[] loc = L1HouseLocation.getHouseLoc(pc.getClan()
							.getHouseId());
					pc.dx = loc[0];
					pc.dy = loc[1];
					pc.dm = (short) loc[2];
					pc.dh = 6;
					pc.setTelType(1);
					pc.sendPackets(new S_SabuTell(pc), true);
					return;
				}
			}

			if (pc.getMapId() >= 10000 && pc.getMapId() <= 10005) {
				if ((pc.getX() == 32716 || pc.getX() == 32717)
						&& pc.getY() == 32912) {// 동굴입구->레어입구
					pc.dx = 32735;
					pc.dy = 32847;
					pc.dm = pc.getMapId();
					pc.dh = 4;
					pc.setTelType(1);
					pc.sendPackets(new S_SabuTell(pc), true);
					return;
				} else if ((pc.getX() == 32736 || pc.getX() == 32735)
						&& pc.getY() == 32846) {// 레어입구 -> 동굴 입구
					pc.dx = 32716;
					pc.dy = 32914;
					pc.dm = pc.getMapId();
					pc.dh = 4;
					pc.setTelType(1);
					pc.sendPackets(new S_SabuTell(pc), true);
					return;
				}
			} else if (pc.getMapId() >= 9103 && pc.getMapId() <= 9199) {
				if ((locx == 32677 && locy == 32800)
						|| (locx == 32804 && locy == 32861)
						|| (locx == 32740 && locy == 32861)) {
					if (NavalWarfare.getInstance().NavalMoveTrapOn(
							pc.getMapId())
							|| NavalWarfare.getInstance().NavalMoveTrapOn2(
									pc.getMapId())
							|| NavalWarfare.getInstance().NavalMoveTrapOn3(
									pc.getMapId())) {
						L1Teleport.teleport(pc, 32795, 32803,
								(short) pc.getMapId(), 4, true);
						return;
					}
				} else if (locx == 32799 && locy == 32809) {
					if (NavalWarfare.getInstance().NavalMoveTrapOn(
							pc.getMapId())) {
						L1Teleport.teleport(pc, 32671, 32802,
								(short) pc.getMapId(), 4, true);
						return;
					} else if (NavalWarfare.getInstance().NavalMoveTrapOn2(
							pc.getMapId())) {
						L1Teleport.teleport(pc, 32800, 32863,
								(short) pc.getMapId(), 4, true);
						return;
					} else if (NavalWarfare.getInstance().NavalMoveTrapOn3(
							pc.getMapId())) {
						L1Teleport.teleport(pc, 32735, 32862,
								(short) pc.getMapId(), 4, true);
						return;
					}
				}
			}

			if (DungeonRandom.getInstance().dg(locx, locy, pc.getMap().getId(), pc)) { // 텔레포트처가 랜덤인 텔레포트 지점
				return;
			}

			L1World.getInstance().Move(pc, locx, locy);

			pc.getMap().setPassable(pc.getLocation(), true);
			pc.getLocation().set(locx, locy);
			pc.getMoveState().setHeading(heading);

			if (pc.getMapId() != 4)
				pc.updateObject();

			Broadcaster.broadcastPacket(pc, new S_MoveCharPacket(pc), true);
			pc.사망패널티(false);
			pc.tempx = pc.getX();
			pc.tempy = pc.getY();
			pc.tempm = pc.getMapId();
			pc.temph = pc.getMoveState().getHeading();

			pc.getMap().setPassable(pc.getLocation(), false);
			if (CrockSystem.getInstance().isOpen()) {
				L1EvaSystem eva = EvaSystemTable.getInstance().getSystem(1);
				int[] loc = CrockSystem.getInstance().loc();
				if (Math.abs(loc[0] - pc.getX()) <= 1
						&& Math.abs(loc[1] - pc.getY()) <= 1
						&& loc[2] == pc.getMap().getId()) {
					switch (eva.getMoveLocation()) {
					case 0:
						return;
					case 1:
						L1Teleport.teleport(pc, 32639, 32876, (short) 780, 2,
								false);
						break;// 테베
					case 2:
						L1Teleport.teleport(pc, 32793, 32754, (short) 783, 2,
								false);
						break;// 티칼
					}
				}
			}

			if (pc.getMapId() == 5153) {
				if (pc.get_DuelLine() == 0 && !pc.isGm()) {
					L1Teleport.teleport(pc, 33419, 32810, (short) 4, 5, true);
				}
			}
			
			int castleid = L1CastleLocation.getCastleIdByArea(pc);
			if (castleid != 0) {
				if (!pc.war_zone) {
					pc.war_zone = true;
					WarTimeController.getInstance().WarTime_SendPacket(
							castleid, pc);
				}
			} else {
				if (pc.war_zone) {
					pc.war_zone = false;
					pc.sendPackets(new S_NewCreateItem(1, 0, ""), true);
					if (pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.주군의버프)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(
								L1SkillId.주군의버프);
						pc.sendPackets(new S_PacketBox(
								S_PacketBox.NONE_TIME_ICON, 0, 490), true);
					}
				}
			}
			
			if(pc.getInventory().checkItem(5001130, 1) && (pc.getMapId()== 15410 || pc.getMapId()== 15420 || pc.getMapId()== 15430 || pc.getMapId()== 154140
					|| pc.getMapId()== 495 || pc.getMapId()== 452 || pc.getMapId()== 453 || pc.getMapId()== 479
					|| pc.getMapId()== 461 || pc.getMapId()== 462 || pc.getMapId()== 471 || pc.getMapId()== 462
					|| pc.getMapId()== 475)) {
				     if(!pc.순간이동지배) {
				    	pc.순간이동지배 = true;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.순간이동지배, true, (long)-1));
				     }
		    }else if((!pc.getInventory().checkItem(5001130, 1)) || (pc.getMapId()!= 15410 || pc.getMapId()!= 15420 || pc.getMapId()!= 15430 || pc.getMapId()!= 154140
					|| pc.getMapId()!= 495 || pc.getMapId()!= 452 || pc.getMapId()!= 453 || pc.getMapId()!= 479
					|| pc.getMapId()!= 461 || pc.getMapId()!= 462 || pc.getMapId()!= 471 || pc.getMapId()!= 462
					|| pc.getMapId()!= 475)) {
		    	if(pc.순간이동지배) {
			    	pc.순간이동지배 = false;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.순간이동지배, false, (long)-1));
		    	}
			}
		/*if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
			if(pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION) || pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION2)
			|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION_cash)|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.드래곤의성장버프)) {
				     if(!pc.경험치물약타이머) {
				    	pc.경험치물약타이머 = true;
				    	pc.sendPackets(new S_PacketBox(1, 1800, true, true, true));
						pc.sendPackets(new S_PacketBox(2, 1800, true, true, true));
				     }
		    }
		}else if (!pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
			if(pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION) || pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION2)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION_cash)|| pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.드래곤의성장버프)) {
		    	if(pc.경험치물약타이머) {
			    	pc.경험치물약타이머 = false;
			    	pc.sendPackets(new S_PacketBox(1, 1800, true, true, true));
		    	}
			}
		}*/
			
			/** 경험치 물약 on off **/
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.EXP_POTION_cash)){ //빛나는 성장의 물약
				if (pc.getMap().isSafetyZone(pc.getX(), pc.getY())) {
					pc.stopSkillEffectTimer(L1SkillId.EXP_POTION_cash); //타이머 중지
					pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.EMERALD_ICON, pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.EXP_POTION_cash), 6, 0x02)); //off
				} else {
					pc.startSkillEffectTimer(L1SkillId.EXP_POTION_cash); //타이머 시작
					pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.EMERALD_ICON, pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.EXP_POTION_cash), 6, 0x01)); //on
				}
			}
			

			

			if (pc.getLevel() >= 52) { // 지정 레벨
				if (pc.getMapId() == 2010 || pc.getMapId() == 2233
						|| pc.getMapId() == 2234 || pc.getMapId() == 2235) {// 폭풍수련던전
					L1Teleport.teleport(pc, 33080, 33390, (short) 4, 5, true); // WB
				}
			}
			if (pc.getLevel() >= 99) { // 지정 레벨
				if ((pc.getMapId() >= 25 && pc.getMapId() <= 28)
						|| (pc.getMapId() >= 2221 && pc.getMapId() <= 2232)) {// 수련던전
					L1Teleport.teleport(pc, 33080, 33390, (short) 4, 5, true); // WB
				}
			
			}
			if (pc.getLevel() >= 99) { // 지정 레벨
				if ((pc.getMapId() >= 30 && pc.getMapId() <= 36)
						|| (pc.getMapId() >= 2221 && pc.getMapId() <= 2232)) {// 용던
					L1Teleport.teleport(pc, 33440, 32809, (short) 4, 5, true); // WB
				}
			
			}
			
//			if(!Jibaetower.getInstance().getOBStart()){
//				if (pc.getMapId() == 12852
//						|| pc.getMapId() == 12853 || pc.getMapId() == 12854
//						|| pc.getMapId() == 12855 || pc.getMapId() == 12856
//						|| pc.getMapId() == 12857 || pc.getMapId() == 12858
//						|| pc.getMapId() == 12859 || pc.getMapId() == 12860
//						|| pc.getMapId() == 12861
//						|| pc.getMapId() == 12862) {
//				L1Teleport.teleport(pc, 33924, 33349, (short) 4, 5, true);
//			   }
//			}
			

			if (pc.isPetRacing())
				PetRacing.getInstance().RacingCheckPoint(pc);
			L1WorldTraps.getInstance().onPlayerMoved(pc);
		} catch (Exception e) {
		} finally {
			clear();
		}
	}
}