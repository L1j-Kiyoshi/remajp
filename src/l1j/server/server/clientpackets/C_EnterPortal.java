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

import l1j.server.server.model.Dungeon;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_EnterPortal extends ClientBasePacket {
	

	private static final String C_ENTER_PORTAL = "[C] C_EnterPortal";
	private static final S_SystemMessage sm = new S_SystemMessage("방또는 홀 대여를 먼저 해주세요.");

	public C_EnterPortal(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);
		try {
			int locx = readH();
			int locy = readH();
			L1PcInstance pc = client.getActiveChar();
			if (pc.isTeleport()) { // 텔레포트 처리중
				return;
			}
			// 지하 감옥에 텔레포트
			boolean ck = Dungeon.getInstance().dg(locx, locy,
					pc.getMap().getId(), pc);
			if (ck) { // 텔관련 문제점 수정
				pc.setTelType(1);
				pc.sendPackets(new S_SabuTell(pc), true);
			} else {
				int xdis = Math.abs(pc.getX() - locx);
				int ydis = Math.abs(pc.getY() - locy);
				if (ydis > 3 || xdis > 3) {
					pc.sendPackets(new S_SystemMessage("잘못된 접근 입니다."), true);
					return;
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
						/*
						 * }else if(pc.getMapId() >= 17408 && pc.getMapId() <=
						 * 17708){ pc.dx = 32632; pc.dy = 32761; pc.dh = 4;
						 * pc.dm = 4; swich = true;
						 */
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
								pc.sendPackets(new S_SystemMessage(
										"다른 여관의 키를 소유 하고있습니다."), true);
							key = null;
						}
					}
				}
				if (pc.getMapId() >= 10000 && pc.getMapId() <= 10005) {
					if ((locx == 32716 || locx == 32717) && locy == 32912) {// 동굴입구->레어입구
						pc.dx = 32735;
						pc.dy = 32847;
						pc.dm = pc.getMapId();
						pc.dh = 4;
						pc.setTelType(1);
						S_SabuTell st = new S_SabuTell(pc);
						pc.sendPackets(st, true);
						return;
					} else if ((locx == 32736 || locx == 32735)
							&& locy == 32846) {// 레어입구 -> 동굴 입구
						pc.dx = 32716;
						pc.dy = 32914;
						pc.dm = pc.getMapId();
						pc.dh = 4;
						pc.setTelType(1);
						S_SabuTell st = new S_SabuTell(pc);
						pc.sendPackets(st, true);
						return;
					}
				}
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_ENTER_PORTAL;
	}
}
