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

package l1j.server.server.model.item.function;

import java.util.Random;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class TeleportScroll extends L1ItemInstance {

	public TeleportScroll(L1Item item) {
		super(item);
	}

	private static Random _random = new Random(System.nanoTime());

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		try {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1ItemInstance useItem = pc.getInventory()
						.getItem(this.getId());
				@SuppressWarnings("unused")
				int bmapid = 0;
				bmapid = packet.readH();
				// pc.sendPackets(new
				// S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
				int itemId = useItem.getItemId();
				int delay_id = 0;
				if (useItem.getItem().getType2() == 0) { // 종별：그 외의 아이템
					delay_id = ((L1EtcItem) useItem.getItem()).get_delayid();
				}
				if (delay_id != 0) { // 지연 설정 있어
					if (pc.hasItemDelay(delay_id) == true) {
						pc.sendPackets(new S_Paralysis(
								S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
						return;
					}
				}

				if (itemId == 140100 || itemId == 40100 || itemId == 40099
						|| itemId == 40086 || itemId == 40863) {
					/*
					 * if(!pc.getMap().isTeleportable() && (itemId == 40100 ||
					 * itemId == 40099)){ pc.sendPackets(new
					 * S_SystemMessage("주위의 마력에의해 순간이동을 사용할 수 없습니다."), true);
					 * pc.sendPackets(new
					 * S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false),
					 * true); return; }
					 */
					L1BookMark bookm = pc.getBookMark(packet.readH(),
							packet.readH(), bmapid);
					if (bookm != null) { // 북마크를 취득 할 수 있으면(자) 텔레포트
						if (bookm.getRandomX() > 0 || bookm.getRandomY() > 0) {
							if (pc.getMap().isEscapable() || pc.isGm()) {
								L1Teleport.randomBookmarkTeleport(pc, bookm, pc
										.getMoveState().getHeading(), true);
								pc.getInventory().removeItem(useItem, 1);
							} else {
								pc.sendPackets(
										new S_Paralysis(
												S_Paralysis.TYPE_TELEPORT_UNLOCK,
												false), true);
								pc.sendPackets(new S_SystemMessage(
										"아무일도 일어나지 않았습니다."), true);
							}
						} else if (pc.getMap().isEscapable() || pc.isGm()) {
							L1Location newLocation = L1Location.BookmarkLoc(pc,
									bookm);
							int newX = newLocation.getX();
							int newY = newLocation.getY();
							short mapId = bookm.getMapId();

							if (mapId == 4
									&& ((newX >= 33331 && newX <= 33341
											&& newY >= 32430 && newY <= 32441)
											|| (newX >= 33258 && newX <= 33267
													&& newY >= 32396 && newY <= 32407)
											|| (newX >= 33388 && newX <= 33397
													&& newY >= 32339 && newY <= 32350) || (newX >= 33443
											&& newX <= 33483 && newY >= 32315 && newY <= 32357))) {
								newX = pc.getX();
								newY = pc.getY();
								mapId = pc.getMapId();
							}

							if (!pc.getInventory().checkEquipped(20288)
									&& !pc.getInventory().checkEquipped(5001130) 
									&& itemId != 140100) {
								Random random = new Random(System.nanoTime());
								/*
								 * int newX2 = newX + random.nextInt(12); int
								 * newY2 = newY + random.nextInt(12); L1Map map
								 * = L1WorldMap.getInstance().getMap(mapId);
								 * boolean ck = false; if
								 * (L1CastleLocation.checkInAllWarArea(newX2,
								 * newY2, mapId)) ck = true; else if
								 * (L1HouseLocation.isInHouse(newX2, newY2,
								 * mapId)) ck = true; else if (newX2 >= 32704 &&
								 * newX2 <= 32835 && newY2 >= 33110 && newY2 <=
								 * 33234 && mapId == 4) // 샌드웜지역 ck = true; else
								 * if((newX2 >= 33472 && newX2 <= 33536) &&
								 * (newY2 >= 32838 && newY2 <= 32876) && mapId
								 * == 4) // 버경장 ck = true; else if(mapId == 4 &&
								 * ((newX2 >= 33331 && newX2 <= 33341 && newY2
								 * >= 32430 && newY2 <= 32441) || (newX2 >=
								 * 33258 && newX2 <= 33267 && newY2 >= 32396 &&
								 * newY2 <= 32407) || (newX2 >= 33388 && newX2
								 * <= 33397 && newY2 >= 32339 && newY2 <= 32350)
								 * || (newX2 >= 33443 && newX2 <= 33483 && newY2
								 * >= 32315 && newY2 <= 32357))){ ck = true; }
								 * if (map.isInMap(newX2, newY2) &&
								 * map.isPassable(newX2, newY2) && !ck) { newX =
								 * newX2; newY = newY2; }
								 */

								int newX2 = (newX - 6) + random.nextInt(12);
								int newY2 = (newY - 6) + random.nextInt(12);
								L1Map map = L1WorldMap.getInstance().getMap(
										mapId);
								int aaa = _random.nextInt(100);

								boolean ck = false;
								if (L1CastleLocation.checkInAllWarArea(newX2,
										newY2, mapId))
									ck = true;
								else if (L1HouseLocation.isInHouse(newX2,
										newY2, mapId))
									ck = true;
								else if (newX2 >= 32704 && newX2 <= 32835
										&& newY2 >= 33110 && newY2 <= 33234
										&& mapId == 4) // 샌드웜지역
									ck = true;
								else if ((newX2 >= 33472 && newX2 <= 33536)
										&& (newY2 >= 32838 && newY2 <= 32876)
										&& mapId == 4) // 버경장
									ck = true;
								else if (mapId == 4
										&& ((newX2 >= 33331 && newX2 <= 33341
												&& newY2 >= 32430 && newY2 <= 32441)
												|| (newX2 >= 33258
														&& newX2 <= 33267
														&& newY2 >= 32396 && newY2 <= 32407)
												||

												(newX2 >= 34197
														&& newX2 <= 34302
														&& newY2 >= 33327 && newY2 <= 33533)
												|| // 황혼의산맥
												(newX2 >= 33453
														&& newX2 <= 33468
														&& newY2 >= 32331 && newY2 <= 32341)
												|| // 아덴의한국민

												(newX2 >= 33388
														&& newX2 <= 33397
														&& newY2 >= 32339 && newY2 <= 32350) || (newX2 >= 33464
												&& newX2 <= 33531
												&& newY2 >= 33168 && newY2 <= 33248) // ||
										/*
										 * (newX2 >= 33443 && newX2 <= 33483 &&
										 * newY2 >= 32315 && newY2 <= 32357)
										 */) /* && !pc.isGm() */) {
									ck = true;
								}

								if (aaa < 1) {// 요정 바보텔 확률
									ck = true;
									newX = pc.getX();
									newY = pc.getY();
								}

								if (map.isInMap(newX2, newY2)
										&& map.isPassable(newX2, newY2) && !ck) {
									newX = newX2;
									newY = newY2;
								} else {
									newX = pc.getX();
									newY = pc.getY();
									// L1Teleport.teleport(pc, pc.getX(),
									// pc.getY(), mapId,
									// pc.getMoveState().getHeading(), true,
									// true);
								}
							}

							if (itemId == 40086) { // 매스 텔레포트 주문서
								byte count = 0;
								for (L1PcInstance member : L1World
										.getInstance().getVisiblePlayer(pc)) {
									if (pc.getLocation().getTileLineDistance(
											member.getLocation()) <= 3
											&& member.getClanid() == pc
													.getClanid()
											&& pc.getClanid() != 0
											&& member.getId() != pc.getId()
											&& !member.isPrivateShop()) {
										count++;
										member.dx = newX;
										member.dy = newY;
										member.dm = mapId;
										member.dh = member.getMoveState()
												.getHeading();
										member.setTelType(7);
										member.sendPackets(new S_SabuTell(
												member), true);
									}
								}
								if (count > 0) {
									for (L1PcInstance member : L1World
											.getInstance().getVisiblePlayer(pc)) {
										if (pc.getLocation()
												.getTileLineDistance(
														member.getLocation()) <= 3
												&& member.getClanid() == pc
														.getClanid()
												&& pc.getClanid() != 0
												&& member.getId() != pc.getId()
												&& !member.isPrivateShop()) {
											pc.sendPackets(new S_ServerMessage(
													3655, pc.getName(), ""
															+ count), true);
										}
									}
									pc.sendPackets(
											new S_ServerMessage(3655, pc
													.getName(), "" + count),
											true);
								}
							}

							pc.dx = newX;
							pc.dy = newY;
							pc.dm = mapId;
							pc.dh = pc.getMoveState().getHeading();
							pc.setTelType(7);
							pc.sendPackets(new S_SabuTell(pc, true), true);

							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_Paralysis(
									S_Paralysis.TYPE_TELEPORT_UNLOCK, false),
									true);
							pc.sendPackets(new S_SystemMessage(
									"아무일도 일어나지 않았습니다."), true);
						}
					} else  {
						if ((pc.getMap().isTeleportable(pc.getX(), pc.getY()) && pc.getMap().isTeleportable()) || pc.isGm()
								|| ((pc.getMapId() == 101 || pc.getMapId() == 102 || pc.getMapId() == 103 || pc.getMapId() == 104 
								|| pc.getMapId() == 105 || pc.getMapId() == 106 || pc.getMapId() == 107 || pc.getMapId() == 108 || pc.getMapId() == 109 
								|| pc.getMapId() == 110 || pc.getMapId() == 12852 || pc.getMapId() == 12853 || pc.getMapId() == 12854 || pc.getMapId() == 12855
								|| pc.getMapId() == 12856 || pc.getMapId() == 12857 || pc.getMapId() == 12858 || pc.getMapId() == 12859 || pc.getMapId() == 12860
								|| pc.getMapId() == 12861) && pc.is오만텔())
								|| ((pc.getMapId() == 15410 || pc.getMapId() == 15420 || pc.getMapId() == 15430 || pc.getMapId() == 15440) && pc.is지배이반텔())
								|| (!GameTimeClock.getInstance().getGameTime().isNight() &&  pc.getMapId() == 54)) {
//							 L1Location newLocation = pc.getLocation().saburan(pc.getMap());
							L1Location newLocation = pc.getLocation().randomLocation(200, true);
							int newX = newLocation.getX();
							int newY = newLocation.getY();
							short mapId = (short) newLocation.getMapId();

							if (itemId == 40086) { // 매스텔레포트주문서
								byte count = 0;
								for (L1PcInstance member : L1World
										.getInstance().getVisiblePlayer(pc)) {
									if (pc.getLocation().getTileLineDistance(
											member.getLocation()) <= 3
											&& member.getClanid() == pc
													.getClanid()
											&& pc.getClanid() != 0
											&& member.getId() != pc.getId()
											&& !member.isPrivateShop()) {
										count++;
										member.dx = newX;
										member.dy = newY;
										member.dm = mapId;
										member.dh = member.getMoveState()
												.getHeading();
										member.setTelType(7);
										member.sendPackets(new S_SabuTell(
												member), true);
									}
								}
								if (count > 0) {
									for (L1PcInstance member : L1World
											.getInstance().getVisiblePlayer(pc)) {
										if (pc.getLocation()
												.getTileLineDistance(
														member.getLocation()) <= 3
												&& member.getClanid() == pc
														.getClanid()
												&& pc.getClanid() != 0
												&& member.getId() != pc.getId()
												&& !member.isPrivateShop()) {
											pc.sendPackets(new S_ServerMessage(
													3655, pc.getName(), ""
															+ count), true);
										}
									}
									pc.sendPackets(
											new S_ServerMessage(3655, pc
													.getName(), "" + count),
											true);
								}
							}
							pc.dx = newX;
							pc.dy = newY;
							pc.dm = mapId;
							pc.dh = pc.getMoveState().getHeading();
							pc.setTelType(7);
							pc.sendPackets(new S_SabuTell(pc, true), true);
							pc.getInventory().removeItem(useItem, 1);
						} else {
							pc.sendPackets(new S_Paralysis(
									S_Paralysis.TYPE_TELEPORT_UNLOCK, false),
									true);
							pc.sendPackets(new S_ServerMessage(276), true);
						}
					}
					pc.cancelAbsoluteBarrier(); // 아브소르트바리아의 해제
				} else if (itemId == 240100) { // 저주해진 텔레포트 스크롤(오리지날 아이템)
					pc.setTelType(4);
					pc.sendPackets(new S_SabuTell(pc, true), true);

					// L1Teleport.teleport(pc, pc.getX(), pc.getY(),
					// pc.getMapId(), pc.getMoveState().getHeading(), true);
					pc.getInventory().removeItem(useItem, 1);
					/*
					 * } else if (itemId == 41159){ // 신비한 날개깃털 if
					 * (pc.getMap().isEscapable() || pc.isGm()){ int[] loc =
					 * L1TownLocation.getGetBackLoc(20); L1Teleport.teleport(pc,
					 * loc[0], loc[1], (short) loc[2], 5, true);
					 * pc.getInventory().removeItem(useItem, 1);
					 * pc.cancelAbsoluteBarrier(); } else { pc.sendPackets(new
					 * S_ServerMessage(647)); }
					 */

				}
				L1ItemDelay.onItemUse(pc, useItem); // 아이템 지연 개시
			}
		} catch (Exception e) {
			if (cha == null)
				return;
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_Paralysis(
						S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
			}
		}
	}
}
