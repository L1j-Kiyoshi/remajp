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

package l1j.server.server.utils;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.FireDragon.FireDragon;
import l1j.server.GameSystem.Hadin.HadinThread;
import l1j.server.GameSystem.NavalWarfare.NavalWarfare;
import l1j.server.GameSystem.NavalWarfare.NavalWarfareController;
import l1j.server.Warehouse.ClanWarehouse;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.TimeController.FishingTimeController;
import l1j.server.server.TimeController.WarTimeController;
import l1j.server.server.datatables.LogTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ACTION_UI;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_MapID;
import l1j.server.server.serverpackets.S_NPCTalkReturn;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_OtherCharPacks;
import l1j.server.server.serverpackets.S_OwnCharPack;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PetPack;
import l1j.server.server.serverpackets.S_PinkName;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SummonPack;

// Referenced classes of package l1j.server.server.utils:
// FaceToFace

public class Teleportation {

	// private static Random _random = new Random(System.nanoTime());

	public Teleportation() {
	}

	public static void doTeleportation(L1PcInstance pc) {
		doTeleportation(pc, false);
	}

	public static void doTeleportation(L1PcInstance pc, boolean type) {
		if (pc == null)
			return;
		try {
			if (pc.isDead() || pc.isPrivateShop()) {
				return;
			}

			int x = pc.getTeleportX();
			int y = pc.getTeleportY();
			short mapId = pc.getTeleportMapId();
			int head = pc.getTeleportHeading();

			L1Map map = L1WorldMap.getInstance().getMap(mapId);
			/** 맵이 다르다면 인던 시스템 정보 처리 할수잇도록 따로관리 */
			boolean ServerInter = false;
			int ServerInterType = 0;
			if (mapId != pc.getMapId()) {
				/** 텔맵이 지배 맵이고 케릭터 맵이 지배가 아니라면 인터서버로 이동 패킷 처리 */
				if ((mapId >= 128520 && mapId <= 128620) && !(pc.getMapId() >= 128520 && pc.getMapId() <= 128620)) {
					ServerInterType = 7;
					ServerInter = true;
				} else if (!(mapId >= 128520 && mapId <= 128620) && (pc.getMapId() >= 128520 && pc.getMapId() <= 128620)) {
					ServerInterType = 99;
					ServerInter = true;
				} else if ((mapId >= 130010 && mapId <= 131000) && !(pc.getMapId() >= 130010 && pc.getMapId() <= 131000)) {
					ServerInterType = 10;
					ServerInter = true;
				} else if (!(mapId >= 130010 && mapId <= 131000) && (pc.getMapId() >= 130010 && pc.getMapId() <= 131000)) {
					ServerInterType = 99;
					ServerInter = true;
				}
			}

			/** 특정 맵 체크해서 그맵이라면 인터서버로 연결 하도록 변경 */
			if (ServerInter) {
				pc.setLocation(x, y, mapId);
				pc.getMoveState().setHeading(head);
				pc.isServerInter(ServerInterType);
				return;
			}

			if (!pc.isGm()) {
				if (x < 0 || y < 0) {
					x = pc.getX();
					y = pc.getY();
					mapId = pc.getMapId();
				}
				try {
					int tile = map.getTile(x, y);
					if (!type && (tile == 0 || tile == 4 || tile == 12 || !map.isInMap(x, y)) && !pc.isGm() && mapId != 4) {
						// System.out.println(mapId);
						x = pc.getX();
						y = pc.getY();
						mapId = pc.getMapId();
					}
				} catch (Exception e) {
					x = pc.getX();
					y = pc.getY();
					mapId = pc.getMapId();
				}
			}

			if (pc.getGfxId().getTempCharGfx() == 11326 || pc.getGfxId().getTempCharGfx() == 11427 || pc.getGfxId().getTempCharGfx() == 10047
					|| pc.getGfxId().getTempCharGfx() == 9688 || pc.getGfxId().getTempCharGfx() == 11322 || pc.getGfxId().getTempCharGfx() == 10069
					|| pc.getGfxId().getTempCharGfx() == 10034 || pc.getGfxId().getTempCharGfx() == 10032) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.SHAPE_CHANGE);
				L1PolyMorph.undoPoly(pc);
			}
			try {
				ClanWarehouse clanWarehouse = null;
				L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
				if (clan != null)
					clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
				if (clanWarehouse != null)
					clanWarehouse.unlock(pc.getId());
			} catch (Exception e) {
			}
			List<L1PcInstance> list = pc.getNearObjects().getKnownPlayers();
			S_RemoveObject ro = new S_RemoveObject(pc.getId());
			for (L1PcInstance target : list) {
				if (target == null)
					continue;
				target.sendPackets(ro);
			}
			if (pc.isReserveGhost()) {
				pc.endGhost();
			} else if (pc.isGhost()) {
				if (mapId != 537 && mapId != 88 && !(x >= 32704 && x <= 32835 && y >= 33110 && y <= 33234 && mapId == 4)) {
					x = pc._ghostSaveLocX;
					y = pc._ghostSaveLocY;
					mapId = pc._ghostSaveMapId;
					head = pc._ghostSaveHeading;
					pc.endGhost();
				}
			}

			if (pc.getMapId() == 2699 && (mapId >= 2600 && mapId <= 2698)) {
				// pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.사망패널티),
				// true);
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.unknown1), true);
			}
			if ((pc.getMapId() >= 2600 && pc.getMapId() <= 2698) && !(mapId >= 2600 && mapId <= 2698)) {
				FireDragon fd = null;
				synchronized (GameList.FDList) {
					fd = GameList.getFD(pc.getMapId());
				}
				if (fd != null) {
					fd.Reset();
				}
				if (pc.getInventory().checkItem(7236)) {
					L1ItemInstance item = pc.getInventory().checkEquippedItem(40005); // 7236
					if (item != null) {
						pc.getInventory().setEquipped(item, false, false, false);
					}
					pc.getInventory().consumeItem(40005, 1); // 7236
				}
			}

			L1World.getInstance().moveVisibleObject(pc, x, y, mapId);
			pc.setLocation(x, y, mapId);
			pc.getMoveState().setHeading(head);
			pc.sendPackets(new S_MapID(pc.getMapId(), pc.getMap().isUnderwater()), true);
			if ((pc.isGm() && pc.isGmInvis()) || pc.getMapId() == 2699 || pc.getMapId() == 2100) {
			} else {
				for (L1PcInstance pc2 : L1World.getInstance().getVisiblePlayer(pc)) {
					if (pc.getMapId() != 631)
						pc2.sendPackets(new S_OtherCharPacks(pc, pc2));
				}
			}
			pc.sendPackets(new S_OwnCharPack(pc), true);

			if (pc.isPinkName()) {
				pc.sendPackets(new S_PinkName(pc.getId(), pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.STATUS_PINK_NAME)), true);
				Broadcaster.broadcastPacket(pc, new S_PinkName(pc.getId(), pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.STATUS_PINK_NAME)),
						true);
			}
			pc.getNearObjects().removeAllKnownObjects();
			pc.sendVisualEffectAtTeleport();
			pc.updateObject();
			pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.MEDITATION);
			pc.setCallClanId(0);
			HashSet<L1PcInstance> subjects = new HashSet<L1PcInstance>();
			subjects.add(pc);
			if (!pc.isGhost()) {
				if (pc.getMap().isTakePets()) {
					int pet_heading = head;
					if (pc.getPetList() != null && pc.getPetListSize() > 0) {
						for (L1NpcInstance petNpc : pc.getPetList()) {
							try {
								if (petNpc == null)
									continue;
								L1Location loc = pc.getLocation().randomLocation(3, false);
								int nx = loc.getX();
								int ny = loc.getY();
								if (pc.getMapId() == 5125 || pc.getMapId() == 5131 || pc.getMapId() == 5132 || pc.getMapId() == 5133 || pc.getMapId() == 5134) {
									boolean xy_check = false;
									for (L1Object obj : L1World.getInstance().getVisibleObjects(pc)) {
										if (obj == null || !(obj instanceof L1PetInstance))
											continue;
										if (obj.getX() == 32797 && obj.getY() == 32863 && mapId == obj.getMapId())
											xy_check = true;
									}

									if (xy_check) {
										nx = 32801;
										ny = 32863;
										pet_heading = 6;
									} else {
										nx = 32797;
										ny = 32863;
										pet_heading = 2;
									}
								}
								teleport(petNpc, nx, ny, mapId, pet_heading);
								if (petNpc instanceof L1SummonInstance) {
									L1SummonInstance summon = (L1SummonInstance) petNpc;
									pc.sendPackets(new S_SummonPack(summon, pc));
								} else if (petNpc instanceof L1PetInstance) {
									L1PetInstance pet = (L1PetInstance) petNpc;
									pc.sendPackets(new S_PetPack(pet, pc));
								}

								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer((L1NpcInstance) petNpc)) {
									visiblePc.getNearObjects().removeKnownObject((L1NpcInstance) petNpc);
									subjects.add(visiblePc);
								}

							} catch (Exception e) {
							}
						}
					}
				} else {
					try {
						if (pc.getPetList() != null && pc.getPetListSize() > 0) {
							for (L1NpcInstance petNpc : pc.getPetList()) {
								if (petNpc instanceof L1SummonInstance) {
									((L1SummonInstance) petNpc).Death(null);
								} else if (petNpc instanceof L1PetInstance) {
									((L1PetInstance) petNpc).setCurrentPetStatus(5); // 경계
								}
							}
						}
					} catch (Exception e) {

					}
				}
			}
			for (L1PcInstance updatePc : subjects) {
				try {
					updatePc.updateObject();
				} catch (Exception e) {
				}
			}
			if (!(pc.getMapId() >= 2101 && pc.getMapId() <= 2151)) {
				if (pc.getInventory().checkItem(6013)) {
					pc.getInventory().consumeItem(6013, pc.getInventory().countItems(6013));
				}
				if (pc.getInventory().checkItem(6014)) {
					pc.getInventory().consumeItem(6014, pc.getInventory().countItems(6014));
				}
			}

			if (!(pc.getMapId() >= 1005 && pc.getMapId() <= 1010)) {
				if (pc.getInventory().checkItem(430113)) {
					pc.getInventory().consumeItem(430113, pc.getInventory().countItems(430113));
				}
				if (pc.getInventory().checkItem(430114)) {
					pc.getInventory().consumeItem(430114, pc.getInventory().countItems(430114));
				}
				if (pc.getInventory().checkItem(430115)) {
					pc.getInventory().consumeItem(430115, pc.getInventory().countItems(430115));
				}
			}

			if (!(pc.getMapId() >= 1936 && pc.getMapId() <= 1940) && !(pc.getMapId() >= 10010 && pc.getMapId() <= 10100)) {
				if (pc.getInventory().checkItem(60512)) {
					pc.getInventory().consumeItem(60512, pc.getInventory().countItems(60512));
				}
				if (pc.getInventory().checkItem(60513)) {
					pc.getInventory().consumeItem(60513, pc.getInventory().countItems(60513));
				}
			}

			if (pc.tempm != pc.getMapId()) {
				int time = 0;
				if (pc.tempm == 14 && pc.getMapId() == 813) {
					if (pc.getgiranday() == null) {
						pc.setgirantime(1);
						pc.setgiranday(new Timestamp(System.currentTimeMillis()));
						pc.save();
						pc.sendPackets(new S_ServerMessage(1526, "3"));// 시간남았다.
					}
				}
				if (pc.getMapId() == 54 || (pc.getMapId() >= 15403 && pc.getMapId() <= 15404)) {
					if (pc.getLevel() <= 89) {
						int outtime = 60 * 60 * 2;
						time = outtime - pc.getgirantime();
					} else if (pc.getLevel() >= 90) {
						int outtime = 60 * 60 * 3;
						time = outtime - pc.getgirantime();
					}
				} else if (pc.getMapId() >= 282 && pc.getMapId() <= 285) { // 상아탑
					if (pc.getLevel() <= 89) {
						int outtime = 60 * 60 * 2;
						time = outtime - pc.getivorytime();
					} else if (pc.getLevel() >= 90) {
						int outtime = 60 * 60 * 3;
						time = outtime - pc.getivorytime();
					}
//				} else if (	pc.getMapId() == 285 || pc.getMapId() == 286 || pc.getMapId() == 287 || pc.getMapId() == 288
//						|| pc.getMapId() == 289) { // 상아탑야히진영
//					time = 3600 - pc.getivoryyaheetime();
//				} else if (pc.getMapId() == 452 || pc.getMapId() == 453 || pc.getMapId() == 461 || pc.getMapId() == 462
//						|| pc.getMapId() == 471 || pc.getMapId() == 475 || pc.getMapId() == 495 || pc.getMapId() == 492
//						|| pc.getMapId() == 479) {
//					int outtime = 60 * 60 * 2;
//					time = outtime - pc.get라던time();
				} else if (pc.getMapId() == 10 || pc.getMapId() == 11 || pc.getMapId() == 12) {
					int outtime = 60 * 60 * 2;
					time = outtime - pc.get검은전함time();
				} else if (pc.getMapId() == 1700 || pc.getMapId() == 1703) {
					int outtime = 60 * 60 * 2;
					time = outtime - pc.get잊섬time();
					/** 리마스터 던전 */
				} else if (pc.getMapId() == 491 || pc.getMapId() == 492 || pc.getMapId() == 493) {
					int outtime = 60 * 60 * 3;
					time = outtime - pc.get아투바time();
				} else if (pc.getMapId() == 777) {
					int outtime = 60 * 60 * 2;
					time = outtime - pc.get버땅time();
				} else if (pc.getMapId() == 59 || pc.getMapId() == 60 || pc.getMapId() == 61 || pc.getMapId() == 63) {
					int outtime = 60 * 60 * 2;
					time = outtime - pc.get에바time();
					/** 리마스터 던전 */
				} else if (pc.getMapId() == 10101) {
					int outtime = 60 * 60 * 1;
					time = outtime - pc.get수련time();
					/*
					 * } else if (pc.getMapId() == 5490) { int outtime = 60 * 60 * 8; time = outtime
					 * - pc.get낚시time();
					 */
				} else if (pc.getMapId() == 785 || pc.getMapId() == 788 || pc.getMapId() == 789) {
					int outtime = 60 * 60 * 1;
					time = outtime - pc.get수렵이벤트time();
				} else if (pc.getMapId() >= 653 && pc.getMapId() <= 656) {
					int outtime = 60 * 60 * 2;
					time = outtime - pc.get수상한감옥time();
				} else if (pc.getMapId() >= 1911 && pc.getMapId() <= 1913) {
					int outtime = 60 * 60;
					time = outtime - pc.get수상한천상계곡time();
				} else if (pc.getMapId() == 5501) {
					int outtime = 60 * 60;
					time = outtime - pc.get할로윈time();
				} else if (pc.getMapId() == 820) {
					int outtime = 60 * 40;
					time = outtime - pc.get솔로타운time();
				} else if (pc.getMapId() == 1931) {
					int outtime = 60 * 30;
					time = outtime - pc.getpc몽섬time();
				} else if (pc.getMapId() == 624 || pc.getMapId() == 430) {
					int outtime = 60 * 60 * 2;
					time = outtime - pc.get고무time();
				}

				if (time > 0)
					pc.sendPackets(new S_PacketBox(S_PacketBox.TIME_COUNT, time), true);
			}
			if (pc.getMapId() >= 9103 && pc.getMapId() <= 9199) {
				NavalWarfareController nwc = NavalWarfare.getInstance().getNaval(pc.getMapId());
				if (nwc != null) {
					pc.sendPackets(new S_PacketBox(true, nwc.score), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.ROUND_SHOW, nwc.stage, 12), true);
				}

			} else if (pc.getMapId() >= 9001 && pc.getMapId() <= 9099) {
				if (pc.isInParty()) {
					HadinThread.get().Round_Show(pc.getParty(), pc);
				}
			}

			pc.tempx = pc.getX();
			pc.tempy = pc.getY();
			pc.tempm = pc.getMapId();
			pc.temph = pc.getMoveState().getHeading();

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.IMMUNE_TO_HARM)) {
				if (pc.isWizard()) {
					if (!pc.isSkillMastery(L1SkillId.IMMUNE_TO_HARM)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
					}
				} else {
					pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.IMMUNE_TO_HARM);
				}
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
				pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), pc.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.WIND_SHACKLE)), true);

			}

			if (pc.상인찾기Objid != 0) {
				pc.sendPackets(new S_NPCTalkReturn(pc.상인찾기Objid, "usershop"), true);
				pc.상인찾기Objid = 0;
			}
			if (pc.TownMapTeleporting) {
				pc.sendPackets(new S_PacketBox(S_PacketBox.지도위치보정), true);
				pc.TownMapTeleporting = false;
			}

			if (pc.아인_시선_존) {
				pc.아인_시선_존 = false;
				pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.AINHASAD, pc));
			}

			int castleid = L1CastleLocation.getCastleIdByArea(pc);
			if (castleid != 0) {
				pc.war_zone = true;
				WarTimeController.getInstance().WarTime_SendPacket(castleid, pc);
			} else {
				if (pc.war_zone) {
					pc.war_zone = false;
					pc.sendPackets(new S_NewCreateItem(1, 0, ""), true);
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.주군의버프)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.주군의버프);
						pc.sendPackets(new S_PacketBox(S_PacketBox.NONE_TIME_ICON, 0, 490), true);
					}
				}
			}

			if (pc.getClan() != null) {
				if (pc.getClan().isHuntMapChoice()) {
					if (pc.getClan().getBlessHuntMapIds().contains((int) pc.getMapId())) {
						pc.sendPackets(new S_ACTION_UI(S_ACTION_UI.AINHASAD, pc));
					}
				}
			}
			// 용계 행운지역 입장시 로그기록
			// 제자리 텔렉풀기도 입력될수있음.
			try {
				if (pc.getMapId() == 4 && ((pc.getX() >= 33333 && pc.getX() <= 33338 && pc.getY() >= 32430 && pc.getY() <= 32441)
						|| (pc.getX() >= 33261 && pc.getX() <= 33265 && pc.getY() >= 32396 && pc.getY() <= 32407)
						|| (pc.getX() >= 33390 && pc.getX() <= 33395 && pc.getY() >= 32339 && pc.getY() <= 32350)
						|| (pc.getX() >= 33443 && pc.getX() <= 33483 && pc.getY() >= 32315 && pc.getY() <= 32357)) && !pc.isGm()) {
					LogTable.log_lucky_darkelder(pc);
				}
			} catch (Exception e) {
			}

			pc.사망패널티(false);

			if (pc.isFishing()) {
				pc.setFishingTime(0);
				pc.setFishingReady(false);
				pc.setFishing(false);
				pc.setFishingItem(null);
				S_CharVisualUpdate cv = new S_CharVisualUpdate(pc);
				pc.sendPackets(cv);
				Broadcaster.broadcastPacket(pc, cv, true);
				FishingTimeController.getInstance().removeMember(pc);
			}
		} catch (Exception e) {
			System.out.println("텔 심각 오류코드 100");
			e.printStackTrace();
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			pc.setTeleport(false);
		}
	}

	private static Logger _log = Logger.getLogger(Teleportation.class.getName());

	public static void teleport(L1NpcInstance npc, int x, int y, short map, int head) {
		L1World.getInstance().moveVisibleObject(npc, x, y, map);
		npc.setX(x);
		npc.setY(y);
		npc.setMap(map);
		npc.getMoveState().setHeading(head);
	}

}
