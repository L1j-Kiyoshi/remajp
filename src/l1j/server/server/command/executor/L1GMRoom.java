/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.command.executor;

import java.util.logging.Logger;

import l1j.server.server.GMCommandsConfig;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1GMRoom implements L1CommandExecutor {
	@SuppressWarnings("unused")
	private static Logger _log = Logger.getLogger(L1GMRoom.class.getName());

	private L1GMRoom() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1GMRoom();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int i = 0;
			try {
				i = Integer.parseInt(arg);
			} catch (NumberFormatException e) {
			}

			if (i == 1) {
				L1Teleport.teleport(pc, 32737, 32796, (short) 99, 5, false); // 영자방
			} else if (i == 2) {
				L1Teleport.teleport(pc, 33052, 32339, (short) 4, 5, false); // 요정숲
			} else if (i == 3) {
				L1Teleport.teleport(pc, 32644, 32955, (short) 0, 5, false); // 판도라
			} else if (i == 4) {
				L1Teleport.teleport(pc, 34055, 32290, (short) 4, 5, false); // 오렌
			} else if (i == 5) {
				L1Teleport.teleport(pc, 33427, 32816, (short) 4, 5, false); // 기란
			} else if (i == 6) {
				L1Teleport.teleport(pc, 33047, 32761, (short) 4, 5, false); // 켄말
			} else if (i == 7) {
				L1Teleport.teleport(pc, 32612, 33191, (short) 4, 5, false); // 윈다우드
			} else if (i == 8) {
				L1Teleport.teleport(pc, 33611, 33253, (short) 4, 5, false); // 하이네
			} else if (i == 9) {
				L1Teleport.teleport(pc, 33082, 33390, (short) 4, 5, false); // 은말
			} else if (i == 10) {
				L1Teleport.teleport(pc, 32572, 32944, (short) 0, 5, false); // 말섬
			} else if (i == 11) {
				L1Teleport.teleport(pc, 33964, 33254, (short) 4, 5, false); // 아덴
			} else if (i == 12) {
				L1Teleport.teleport(pc, 32635, 32818, (short) 303, 5, false); // 몽섬
			} else if (i == 13) {
				L1Teleport.teleport(pc, 32828, 32848, (short) 70, 5, false); // 잊섬
			} else if (i == 14) {
				L1Teleport.teleport(pc, 32736, 32787, (short) 15, 5, false); // 켄성
			} else if (i == 15) {
				L1Teleport.teleport(pc, 32735, 32788, (short) 29, 5, false); // 윈성
			} else if (i == 16) {
				L1Teleport.teleport(pc, 32730, 32802, (short) 52, 5, false); // 기란
			} else if (i == 17) {
				L1Teleport.teleport(pc, 32572, 32826, (short) 64, 5, false); // 하이네성
			} else if (i == 18) {
				L1Teleport.teleport(pc, 32895, 32533, (short) 300, 5, false); // 아덴성
			} else if (i == 19) {
				L1Teleport.teleport(pc, 33167, 32775, (short) 4, 5, false); // 켄성
																			// 수호탑
			} else if (i == 20) {
				L1Teleport.teleport(pc, 32674, 33408, (short) 4, 5, false); // 윈성
																			// 수호탑
			} else if (i == 21) {
				L1Teleport.teleport(pc, 33630, 32677, (short) 4, 5, false); // 기란
																			// 수호탑
			} else if (i == 22) {
				L1Teleport.teleport(pc, 33524, 33394, (short) 4, 5, false); // 하이네
																			// 수호탑
			} else if (i == 23) {
				L1Teleport.teleport(pc, 32424, 33068, (short) 440, 5, false); // 해적섬
			} else if (i == 24) {
				L1Teleport.teleport(pc, 32800, 32868, (short) 1001, 5, false); // 베헤모스
			} else if (i == 25) {
				L1Teleport.teleport(pc, 32800, 32856, (short) 1000, 5, false); // 실베리아
			} else if (i == 26) {
				L1Teleport.teleport(pc, 32630, 32903, (short) 780, 5, false); // 테베사막
			} else if (i == 27) {
				L1Teleport.teleport(pc, 32743, 32799, (short) 781, 5, false); // 테베
																				// 피라미드
																				// 내부
			} else if (i == 28) {
				L1Teleport.teleport(pc, 32735, 32830, (short) 782, 5, false); // 테베
																				// 오리시스
																				// 제단
			} else if (i == 29) {
				L1Teleport.teleport(pc, 32538, 32958, (short) 777, 5, false); // 버땅
			} else if (i == 30) {
				L1Teleport.teleport(pc, 33052, 32339, (short) 4, 5, false); // 요정숲
			} else if (i == 31) {
				L1Teleport.teleport(pc, 33506, 32735, (short) 88, 5, false); // 기란
																				// 무대
			} else if (i == 32) {
				L1Teleport.teleport(pc, 32768, 32829, (short) 610, 5, false); // 벗꽃마을
			} else if (i == 33) {
				L1Teleport.teleport(pc, 32791, 32869, (short) 612, 5, false); // 과일의숲
			} else if (i == 34) {
				L1Teleport.teleport(pc, 32769, 32827, (short) 5554, 5, false); // 야외
																				// 결혼식장
			} else if (i == 35) {
				L1Teleport.teleport(pc, 32685, 32870, (short) 2005, 5, false); // 숨겨진계곡
			} else if (i == 36) {
				L1Teleport.teleport(pc, 32895, 32525, (short) 300, 5, true); // 아덴성
			} else if (i == 37) {
				L1Teleport.teleport(pc, 32928, 32864, (short) 6202, 5, true); // 감옥
			} else {
				L1Location loc = GMCommandsConfig.ROOMS.get(arg.toLowerCase());
				if (loc == null) {
					pc.sendPackets(new S_SystemMessage(
							"1.GMroom 2.요숲 3.판도라 4.오렌 5.기란 6.켄말"), true);
					pc.sendPackets(new S_SystemMessage(
							"7.윈다 8.하이네 9.은말 10.말섬 11.아덴 12.몽섬"), true);
					pc.sendPackets(new S_SystemMessage(
							"13.잊섬 14.켄성 15.윈성 16.기란성 17.하이네성"), true);
					pc.sendPackets(new S_SystemMessage(
							"18.아덴성 19.켄성수탑 20.윈성수탑 21.기란수탑"), true);
					pc.sendPackets(new S_SystemMessage(
							"22.하이네수탑 23.해적섬 24.베헤모스 25.실베리아"), true);
					pc.sendPackets(new S_SystemMessage(
							"26.테베사막 27.피라미드내부 28.오리시스제단"), true);
					pc.sendPackets(new S_SystemMessage(
							"29.버려진 땅 30.요정숲 31.기란 무대"), true);
					pc.sendPackets(new S_SystemMessage(
							"32.상담1 33.상담2 34.상담3 35.숨겨진계곡 36.아덴성"), true);
					pc.sendPackets(new S_SystemMessage("37.감옥"), true);
					return;
				}
				L1Teleport.teleport(pc, loc.getX(), loc.getY(),
						(short) loc.getMapId(), 5, false);
			}

			if (i > 0 && i < 28) {
				pc.sendPackets(new S_SystemMessage("운영자 귀환(" + i
						+ ")번으로 이동했습니다."), true);
			}
		} catch (Exception exception) {
			pc.sendPackets(new S_SystemMessage(
					".귀환 [장소명]을 입력 해주세요.(장소명은 GMCommands.xml을 참조)"), true);
		}
	}
}
