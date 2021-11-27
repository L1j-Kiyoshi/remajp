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

import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.GameSystem.NpcTradeShop.NpcTradeShop;
import l1j.server.GameSystem.NpcTradeShop.ShopItem;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_Trade;
import l1j.server.server.utils.FaceToFace;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Trade extends ClientBasePacket {
	

	private static final String C_TRADE = "[C] C_Trade";

	public C_Trade(byte abyte0[], LineageClient clientthread) throws Exception {
		super(abyte0);
		try {
			L1PcInstance player = clientthread.getActiveChar();
			if (player.isGhost())
				return;
			if (isTwoLogin(player))
				return;
			if (player.getOnlineStatus() != 1) {
				clientthread.kick();
				return;
			}

			if (player.isInvisble()) {
				S_ServerMessage sm = new S_ServerMessage(334);
				player.sendPackets(sm, true);
				return;
			}

			if (player.getTradeReady()) {
				S_SystemMessage sm = new S_SystemMessage("당신은 현재 교환 중인 상태 입니다.");
				player.sendPackets(sm, true);
				return;
			}

			if (player.getInventory().calcWeightpercent() >= 100) {
				player.sendPackets(new S_ServerMessage(270)); // 무게 게이지가 가득찼습니다.
				return;
			}

			L1NpcInstance npc = FaceToFace.faceToFaceforNpc(player);
			if (npc != null) {
				if (npc instanceof L1NpcShopInstance
						|| npc instanceof GambleInstance) {
					ShopItem[] list = NpcTradeShop.getInstance().getShopList();
					boolean ck = false;
					if (list != null) {
						for (ShopItem shop : list) {
							if (shop.getNpcId() == npc.getNpcId()) {
								ck = true;
								break;
							}
						}
					}
					if (npc instanceof L1NpcShopInstance) {
						L1NpcShopInstance nsi = (L1NpcShopInstance) npc;
						if (nsi.getState() == 3)
							ck = true;
					}
					if (!npc.Npc_trade
							&& (ck
									|| (npc.getNpcTemplate().get_npcId() >= 8100000 && npc
											.getNpcTemplate().get_npcId() <= 8100002) || (npc instanceof GambleInstance && !((GambleInstance) npc).play))) {
						if (!npc.isParalyzed()) {
							player.setTradeID(npc.getId()); // 상대의 오브젝트 ID를 보존해
															// 둔다
							player.setTradeReady(true);
							S_Trade st = new S_Trade(npc.getName());
							player.sendPackets(st, true);
							npc.Npc_trade = true;
						}
						return;
					}
				}
			}

			L1PcInstance target = FaceToFace.faceToFace(player);
			if (target != null) {
				if (target.getInventory().calcWeightpercent() >= 100) {
					player.sendPackets(new S_ServerMessage(271)); // 무게 게이지가
																	// 가득찼습니다.
					return;
				}
				if (target.getTradeReady()) {
					S_SystemMessage sm = new S_SystemMessage(
							"대상은 현재 교환 중인 상태 입니다.");
					player.sendPackets(sm, true);
					return;
				}
				if (player.getAccountName().equalsIgnoreCase(
						target.getAccountName())) {
					S_Disconnect dis = new S_Disconnect();
					player.sendPackets(dis);
					target.sendPackets(dis, true);
					return;
				}
				if (!target.isParalyzed()) {
					player.setTradeID(target.getId()); // 상대의 오브젝트 ID를 보존해 둔다
					target.setTradeID(player.getId());
					S_Message_YN yn = new S_Message_YN(252, player.getName());
					target.sendPackets(yn, true);
				}
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	private boolean isTwoLogin(L1PcInstance c) {
		boolean bool = false;
		L1PcInstance[] list = L1World.getInstance().getAllPlayersToArray();
		for (L1PcInstance target : list) {
			// 무인PC 는 제외
			if (target == null)
				continue;
			if (target.noPlayerCK)
				continue;
			if (target.샌드백)
				continue;
			//
			if (c.getId() != target.getId() && !target.isPrivateShop()) {
				if (c.getNetConnection()
						.getAccountName()
						.equalsIgnoreCase(
								target.getNetConnection().getAccountName())) {
					bool = true;
					break;
				}
			}
		}
		list = null;
		return bool;
	}

	@Override
	public String getType() {
		return C_TRADE;
	}

}
