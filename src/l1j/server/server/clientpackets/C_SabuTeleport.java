package l1j.server.server.clientpackets;

import java.util.Random;

import l1j.server.server.datatables.HouseTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1TownLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.templates.L1House;
import server.LineageClient;

public class C_SabuTeleport extends ClientBasePacket {
	

	private static final String C_SABU_TELEPORT = "[C] C_SabuTel";
	private static Random _random = new Random(System.nanoTime());

	public C_SabuTeleport(byte[] decrypt, LineageClient client) {
		super(decrypt);
		L1PcInstance pc = client.getActiveChar();
		if (pc == null) {
			return;
		}
		try {
			/*
			 * if(pc != null){ pc.sendPackets(new
			 * S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true); }
			 */

			if (!pc.텔대기()) {
				return;
			}

			// pc.텔대기(false);
			int[] loc = new int[3];
			int type = pc.getTelType();
			int ran = _random.nextInt(5);
			int heading = 5;
			boolean 텔이팩 = false;
			switch (type) {
			case 77:// 깃털 41159
				ran = _random.nextInt(9);
				switch (ran) {
				case 0:
					loc[0] = 32781;
					loc[1] = 32830;
					loc[2] = 622;
					break;
				case 1:
					loc[0] = 32774;
					loc[1] = 32839;
					loc[2] = 622;
					break;
				case 2:
					loc[0] = 32770;
					loc[1] = 32834;
					loc[2] = 622;
					break;
				case 3:
					loc[0] = 32756;
					loc[1] = 32826;
					loc[2] = 622;
					break;
				case 4:
					loc[0] = 32766;
					loc[1] = 32817;
					loc[2] = 622;
					break;
				case 5:
					loc[0] = 32781;
					loc[1] = 32815;
					loc[2] = 622;
					break;
				case 6:
					loc[0] = 32755;
					loc[1] = 32818;
					loc[2] = 622;
					break;
				case 7:
					loc[0] = 32752;
					loc[1] = 32846;
					loc[2] = 622;
					break;
				default:
					loc[0] = 32763;
					loc[1] = 32835;
					loc[2] = 622;
					break;
				}
				텔이팩 = true;
				break;
			case 11:// 기란 귀환 주문서 40081
				switch (ran) {
				case 0:
					loc[0] = 33433;
					loc[1] = 32800;
					loc[2] = 4;
					break;
				case 1:
					loc[0] = 33418;
					loc[1] = 32815;
					loc[2] = 4;
					break;
				case 2:
					loc[0] = 33425;
					loc[1] = 32827;
					loc[2] = 4;
					break;
				case 3:
					loc[0] = 33442;
					loc[1] = 32797;
					loc[2] = 4;
					break;
				default:
					loc[0] = 33442;
					loc[1] = 32797;
					loc[2] = 4;
					break;
				}
				텔이팩 = true;
				heading = 5;
				break;
			case 1:// 던전이동
				loc[0] = pc.dx;
				loc[1] = pc.dy;
				loc[2] = pc.dm;
				heading = pc.dh;
				pc.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.ABSOLUTE_BARRIER, 5000);
				break;
			case 2:// 성귀환
				텔이팩 = true;
				loc = L1CastleLocation.getCastleLoc(pc.getClan().getCastleId());
				break;
			case 3:// 아지트귀환
				텔이팩 = true;
				L1House house = HouseTable.getInstance().getHouseTable(
						pc.getClan().getHouseId());
				if (!house.isPurchaseBasement())
					loc = L1HouseLocation.getBasementLoc(pc.getClan()
							.getHouseId());
				else
					loc = L1HouseLocation
							.getHouseLoc(pc.getClan().getHouseId());
				break;
			case 4:// 빽텔
				loc[0] = pc.getX();
				loc[1] = pc.getY();
				loc[2] = pc.getMapId();
				heading = pc.getMoveState().getHeading();
				break;
			case 5:// 일반 귀환주문서들
				if (pc.getMapId() == 350) {// 기란 시장
					byte tempL = (byte) _random.nextInt(3);
					switch (tempL) {
					case 0:
						loc[0] = 32718 + _random.nextInt(5);
						loc[1] = 32841 + _random.nextInt(4);
						loc[2] = 350;
						break;
					case 1:
						loc[0] = 32673 + _random.nextInt(4);
						loc[1] = 32846 + _random.nextInt(5);
						loc[2] = 350;
						break;
					case 2:
						loc[0] = 32703 + _random.nextInt(4);
						loc[1] = 32808 + _random.nextInt(4);
						loc[2] = 350;
						break;
					default:
						break;
					}
				} else if (pc.getMapId() == 340) { // 글루딘 시장
					byte tempL = (byte) _random.nextInt(4);
					switch (tempL) {
					case 0:
						loc[0] = 32808 + _random.nextInt(2);
						loc[1] = 32819 + _random.nextInt(5);
						loc[2] = 340;
						break;
					case 1:
						loc[0] = 32758 + _random.nextInt(3);
						loc[1] = 32866 + _random.nextInt(4);
						loc[2] = 340;
						break;
					case 2:
						loc[0] = 32728 + _random.nextInt(3);
						loc[1] = 32811 + _random.nextInt(4);
						loc[2] = 340;
						break;
					case 3:
						loc[0] = 32764 + _random.nextInt(5);
						loc[1] = 32794 + _random.nextInt(4);
						loc[2] = 340;
						break;
					default:
						break;
					}
				} else if (pc.getMapId() == 360) { // 오렌 시장
					byte tempL = (byte) _random.nextInt(2);
					switch (tempL) {
					case 0:
						loc[0] = 32735 + _random.nextInt(5);
						loc[1] = 32786 + _random.nextInt(2);
						loc[2] = 360;
						break;
					case 1:
						loc[0] = 32733 + _random.nextInt(7);
						loc[1] = 32812;
						loc[2] = 360;
						break;
					default:
						break;
					}
				} else if (pc.getMapId() == 370) { // 은기사 시장
					byte tempL = (byte) _random.nextInt(2);
					switch (tempL) {
					case 0:
						loc[0] = 32736 + _random.nextInt(5);
						loc[1] = 32786 + _random.nextInt(2);
						loc[2] = 370;
						break;
					case 1:
						loc[0] = 32733 + _random.nextInt(5);
						loc[1] = 32809 + _random.nextInt(2);
						loc[2] = 370;
						break;
					default:
						break;
					}
				} else if (pc.getMapId() >= 1400 && pc.getMapId() <= 1499) {
					loc[0] = 33489;
					loc[1] = 32764;
					loc[2] = 4;
				} else if (pc.getMapId() >= 2600 && pc.getMapId() <= 2699) {
					loc[0] = 33702;
					loc[1] = 32502;
					loc[2] = 4;
				} else if (pc.getMapId() >= 2301 && pc.getMapId() <= 2350) {
					loc[0] = 33438;
					loc[1] = 32799;
					loc[2] = 4;
				} else
					loc = Getback.GetBack_Location(pc, true);
				텔이팩 = true;
				break;
			case 6:// 혈귀 성,아지 없을시
				if (pc.getHomeTownId() > 0) {
					loc = L1TownLocation.getGetBackLoc(pc.getHomeTownId());
				} else {
					loc = Getback.GetBack_Location(pc, true);
				}
				텔이팩 = true;
				break;
			case 7:// 이팩트있는 지정좌표 텔 통합[순간,여행자,등등..]
				텔이팩 = true;
				loc[0] = pc.dx;
				loc[1] = pc.dy;
				loc[2] = pc.dm;
				heading = pc.dh;
				break;
			case 8:// 숨계 귀환 주문서 40101
				switch (ran) {
				case 0:
					loc[0] = 32663;
					loc[1] = 32877;
					loc[2] = 2005;
					break;
				case 1:
					loc[0] = 32673;
					loc[1] = 32863;
					loc[2] = 2005;
					break;
				case 2:
					loc[0] = 32673;
					loc[1] = 32845;
					loc[2] = 2005;
					break;
				case 3:
					loc[0] = 32658;
					loc[1] = 32851;
					loc[2] = 2005;
					break;
				default:
					loc[0] = 32674;
					loc[1] = 32863;
					loc[2] = 2005;
					break;
				}
				텔이팩 = true;
				break;
			case 9: // 밀려나기 ( 넉백스킬 (리치, 아이리스 이펙트)
				텔이팩 = true;
				loc[0] = pc.dx;
				loc[1] = pc.dy;
				loc[2] = pc.dm;
				heading = pc.dh;
				pc.sendPackets(new S_SkillSound(pc.getId(), 7392), true);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
						7392), true);
				break;
			case 10: // 추방 막대
				loc[0] = pc.dx;
				loc[1] = pc.dy;
				loc[2] = pc.dm;
				heading = pc.dh;
				break;
			case 12: // 스톰워크
				loc[0] = pc.dx;
				loc[1] = pc.dy;
				loc[2] = pc.dm;
				heading = pc.dh;
				pc.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.ABSOLUTE_BARRIER, 5000);
				break;
			}

			if (loc[0] == 0 || loc[1] == 0) {
				loc[0] = pc.getX();
				loc[1] = pc.getY();
				loc[2] = pc.getMapId();
				heading = pc.getMoveState().getHeading();
			}

			if (텔이팩) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 169), true);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
						169), true);
			}

			// Broadcaster.broadcastPacket(pc, new S_RemoveObject(pc), true);
			// Broadcaster.broadcastPacket(pc, new S_OtherCharPacks(pc), true);
			// Broadcaster.broadcastPacket(pc, new S_MoveCharPacket(pc), true);

			// Broadcaster.broadcastPacket(pc, new S_MoveCharPacket(pc), true);
			// Broadcaster.broadcastPacket(pc, new S_MoveCharPacket(pc), true);

			L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], heading,
					false, 0);
			loc = null;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// GeneralThreadPool.getInstance().schedule(new tell(pc), 100);
			pc.텔대기(false);
			// pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK,
			// false), true);
			clear();
		}
	}

	class tell implements Runnable {

		private L1PcInstance pc = null;

		public tell(L1PcInstance _pc) {
			pc = _pc;
		}

		@Override
		public void run() {
			try {
				pc.텔대기(false);
				pc.sendPackets(new S_Paralysis(
						S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
			} catch (Exception e) {
			}
		}

	}

	@Override
	public String getType() {
		return C_SABU_TELEPORT;
	}
}
