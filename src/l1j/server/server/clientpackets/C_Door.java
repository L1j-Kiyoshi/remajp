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
package l1j.server.server.clientpackets;

import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1House;
import l1j.server.server.utils.L1SpawnUtil;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket, C_Door

public class C_Door extends ClientBasePacket {
	

	private static final String C_DOOR = "[C] C_Door";
	private static Random _random = new Random(System.nanoTime());

	public C_Door(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);
		try {
			@SuppressWarnings("unused")
			int locX = readH();
			@SuppressWarnings("unused")
			int locY = readH();
			int objectId = readD();

			L1PcInstance pc = client.getActiveChar();
			L1DoorInstance door = null;
			L1Object obj = L1World.getInstance().findObject(objectId);
			if (obj instanceof L1DoorInstance) {
				door = (L1DoorInstance) L1World.getInstance().findObject(
						objectId);
			}
			if (door == null) {
				return;
			}
			// System.out.println("잘못된 문 좌표 x :"+obj.getX()+" y :"+obj.getY()+" m :"+obj.getMapId());

			if (door.getDoorId() == 7200 || door.getDoorId() == 7300
				|| door.getDoorId() == 7510 || door.getDoorId() == 7511
				|| door.getDoorId() == 7520 || door.getDoorId() == 7530
				|| door.getDoorId() == 7540 || door.getDoorId() == 7550) {
				return;
			}
			if ((door.getDoorId() >= 5000 && door.getDoorId() <= 5009)) {
				return;
			} else if ((door.getDoorId() >= 4005 && door.getDoorId() <= 4067)) { // 라스타바드
																					// 문
				return;
			}
			/** 하딘 인던 문 클릭 방지 by 케인 **/
			if (!pc.isGm() && (door.getDoorId() == 5000044 || door.getDoorId() == 5000045)) {
				return;
			}
			
			/** 인던 문짝은 패킷처리로만 열수 있도록 새팅 */
			if (door.getDoorId() == 46401) {
				return;
			}
			
			if (door.getDoorId() >= 4040000 && door.getDoorId() <= 4040004) {
				return;
			}
			if (door.getNpcId() == 100647)
				return;
			if (door.getDoorId() == 4038101) {
				if (pc.getInventory().checkItem(430113)) {
					pc.getInventory().consumeItem(430113, 1);
				} else {
					S_SystemMessage sm = new S_SystemMessage(
							"문을 열기 위해서는 코마의 숨결(A) 가 필요합니다.");
					pc.sendPackets(sm, true);
					return;
				}
			}
			if (door.getDoorId() == 4038102) {
				if (pc.getInventory().checkItem(430114)) {
					pc.getInventory().consumeItem(430114, 1);
				} else {
					S_SystemMessage sm = new S_SystemMessage(
							"문을 열기 위해서는 코마의 숨결(B) 가 필요합니다.");
					pc.sendPackets(sm, true);
					return;
				}
			}
			if (door.getDoorId() == 4038103) {
				if (pc.getInventory().checkItem(430115)) {
					pc.getInventory().consumeItem(430115, 1);
				} else {
					S_SystemMessage sm = new S_SystemMessage(
							"문을 열기 위해서는 코마의 숨결(C) 가 필요합니다.");
					pc.sendPackets(sm, true);
					return;
				}
			}

			if (door.getDoorId() == 4038201) {
				if (pc.getInventory().checkItem(54028)) {
					pc.getInventory().consumeItem(54028, 1);
				} else {
					S_SystemMessage sm = new S_SystemMessage(
							"문을 열기 위해서는 바다의 비밀(A) 가 필요합니다.");
					pc.sendPackets(sm, true);
					return;
				}
			}
			if (door.getDoorId() == 4038202) {
				if (pc.getInventory().checkItem(54029)) {
					pc.getInventory().consumeItem(54029, 1);
				} else {
					S_SystemMessage sm = new S_SystemMessage(
							"문을 열기 위해서는 바다의 비밀(B) 가 필요합니다.");
					pc.sendPackets(sm, true);
					return;
				}
			}
			if (door.getDoorId() == 4038203) {
				if (pc.getInventory().checkItem(54030)) {
					pc.getInventory().consumeItem(54030, 1);
				} else {
					S_SystemMessage sm = new S_SystemMessage(
							"문을 열기 위해서는 바다의 비밀(C) 가 필요합니다.");
					pc.sendPackets(sm, true);
					return;
				}
			}

			if (door != null && !isExistKeeper(pc, door.getKeeperId())) {
				if (door.getDoorId() == 113
						|| (door.getDoorId() >= 8011 && door.getDoorId() <= 8022)) {
					if (pc.getInventory().checkItem(40163)) {
						pc.getInventory().consumeItem(40163, 1);
					} else {
						return;
					}
				}
				if (door.getDoorId() == 125) {
					if (pc.getInventory().checkItem(40313)) {
						pc.getInventory().consumeItem(40313, 1);
					} else {
						return;
					}
				}
				if (door.getDoorId() == 8025) {
					if (pc.getInventory().checkItem(54028)) {
						pc.getInventory().consumeItem(54028, 1);
					} else {
						return;
					}
				}
				if (door.getDoorId() == 8024) {
					if (pc.getInventory().checkItem(54029)) {
						pc.getInventory().consumeItem(54029, 1);
					} else {
						return;
					}
				}
				if (door.getDoorId() == 8023) {
					if (pc.getInventory().checkItem(54030)) {
						pc.getInventory().consumeItem(54030, 1);
					} else {
						return;
					}
				}
				if (door.getDoorId() >= 7100 && door.getDoorId() < 8000) {
					if (pc.getInventory().checkItem(L1ItemId.ANTCATALYST, 1)) {
						antEgg(pc, door, door.getMapId());
						return;
					} else {
						return;
					}
				}

				if (door.getNpcTemplate().get_npcId() == 100852
						|| door.getNpcTemplate().get_npcId() == 100853
						|| door.getNpcTemplate().get_npcId() == 100854
						|| door.getNpcTemplate().get_npcId() == 100855) {
					if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
						return;
					}
					int difflocx = Math.abs(pc.getX() - door.getX());
					int difflocy = Math.abs(pc.getY() - door.getY());
					if (difflocx > 3 || difflocy > 3) {
						return;
					}
					피닉스의알(pc, door);
					return;
				}

				if (door.getDoorId() >= 8001 && door.getDoorId() <= 8010) {
					if (pc.getInventory().checkItem(L1ItemId.GIRANCAVE_BOXKEY,
							1)) {
						giranCaveBox(pc, door);
						return;
					} else {
						return;
					}

				}
				if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
					door.close();
				} else if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
					door.open();
				}
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private void 피닉스의알(L1PcInstance pc, L1DoorInstance door) {
		int ran = _random.nextInt(100) + 1;
		L1ItemInstance item = null;
		if (door.isDead()) {
			return;
		}
		if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
			door.open();
			if (ran < 10) {
				item = pc.getInventory().storeItem(40012, 10);
				pc.sendPackets(new S_ServerMessage(403, item.getName()
						+ " (10)"), true);
			} else {
				item = pc.getInventory().storeItem(40010, 10);
				pc.sendPackets(new S_ServerMessage(403, item.getName()
						+ " (10)"), true);
			}
		}
	}

	private void giranCaveBox(L1PcInstance pc, L1DoorInstance door) {
		int ran = _random.nextInt(100) + 1;
		L1ItemInstance item = null;
		if (door.isDead()) {
			return;
		}
		if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
			pc.getInventory().consumeItem(L1ItemId.GIRANCAVE_BOXKEY, 1);
			door.open();
			if (ran >= 0 && ran <= 60) {
				item = pc.getInventory().storeItem(40308, 500000);
				pc.sendPackets(new S_ServerMessage(403, item.getName()
						+ " (500000)"), true);
				// pc.sendPackets(new
				// S_SystemMessage(item.getName()+" (500000)을 획득 하였습니다."),
				// true);
				// pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			} else if (ran >= 61 && ran <= 70) {
				item = pc.getInventory().storeItem(40308, 700000);
				pc.sendPackets(new S_ServerMessage(403, item.getName()
						+ " (700000)"), true);
				// pc.sendPackets(new
				// S_SystemMessage(item.getName()+" (700000)을 획득 하였습니다."),
				// true);
				// pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			} else if (ran >= 71 && ran <= 75) {
				item = pc.getInventory().storeItem(40308, 800000);
				pc.sendPackets(new S_ServerMessage(403, item.getName()
						+ " (800000)"), true);
				// pc.sendPackets(new
				// S_SystemMessage(item.getName()+" (800000)을 획득 하였습니다."),
				// true);
				// pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			} else if (ran >= 76 && ran <= 80) {
				item = pc.getInventory().storeItem(40308, 1000000);
				pc.sendPackets(new S_ServerMessage(403, item.getName()
						+ " (1000000)"), true);
				// pc.sendPackets(new
				// S_SystemMessage(item.getName()+" (1000000)을 획득 하였습니다."),
				// true);
				// pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			} else if (ran >= 81 && ran <= 90) {
				item = pc.getInventory().storeItem(40074, 1);
				pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
				// pc.sendPackets(new
				// S_SystemMessage(item.getName()+" (1)을 획득 하였습니다."), true);
				// pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			} else if (ran >= 91 && ran <= 100) {
				item = pc.getInventory().storeItem(40087, 1);
				pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
				// pc.sendPackets(new
				// S_SystemMessage(item.getName()+" (1)을 획득 하였습니다."), true);
				// pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
			}
		}

	}

	private static final int[] mobid = { 45946, 45947, 45948, 45949, 45950,
			45951, 45115, 45190 };
	private static final int[] itemLow = { 148, 52, 20149, 20115, 20231, 40053 };
	private static final int[] itemMiddle = { 40087, 40074 };
	private static final int[] itemHigh = { 64, 140087, 140074 };

	private void antEgg(L1PcInstance pc, L1DoorInstance door, short mapid) {
		int ran = _random.nextInt(100) + 1;
		L1ItemInstance item = null;
		if (door.isDead()) {
			return;
		}
		if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
			pc.getInventory().consumeItem(L1ItemId.ANTCATALYST, 1);
			door.open();
			if (ran >= 0 && ran < 40) {
				L1SpawnUtil.spawn(pc, mobid[ran % mobid.length], 0, 300000,
						false);
			} else if (ran >= 40 && ran < 95) {
				item = pc.getInventory().storeItem(
						itemLow[ran % itemLow.length], 1);
				pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
			} else if ((ran >= 95 && ran <= 99)
					&& (mapid >= 541 && mapid <= 543)) {
				item = pc.getInventory().storeItem(
						itemMiddle[ran % itemMiddle.length], 1);
				pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
			} else if ((ran == 100) && (mapid >= 541 && mapid <= 543)) {
				item = pc.getInventory().storeItem(
						itemHigh[ran % itemHigh.length], 1);
				pc.sendPackets(new S_ServerMessage(403, item.getName()), true);
			} else {
				L1SpawnUtil.spawn(pc, mobid[ran % mobid.length], 0, 300000,
						false);
			}
		}
	}

	private boolean isExistKeeper(L1PcInstance pc, int keeperId) {
		if (keeperId == 0) {
			return false;
		}

		L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
		if (clan != null) {
			int houseId = clan.getHouseId();
			if (houseId != 0) {
				L1House house = HouseTable.getInstance().getHouseTable(houseId);
				if (keeperId == house.getKeeperId()) {
					return false;
				}
			}
		}
		return true;
	}

	@Override
	public String getType() {
		return C_DOOR;
	}
}
