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
package l1j.server.server.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.Akduk1GameSystem;
import l1j.server.GameSystem.Akduk2GameSystem;
import l1j.server.GameSystem.Akduk3GameSystem;
import l1j.server.GameSystem.Akduk4GameSystem;
import l1j.server.GameSystem.BigBuffSystem;
import l1j.server.GameSystem.BuffSystem;
import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.GameSystem.NpcBuyShop.NpcBuyShop;
import l1j.server.GameSystem.NpcTradeShop.NpcTradeShop;
import l1j.server.GameSystem.NpcTradeShop.ShopItem;
import l1j.server.Warehouse.PrivateWarehouse;
import l1j.server.Warehouse.WarehouseManager;
// Referenced classes of package l1j.server.server.model:
// L1Trade
import l1j.server.server.Account;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LogTable;
import l1j.server.server.model.Instance.L1BuffNpcInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TradeAddItem;
import l1j.server.server.serverpackets.S_TradeStatus;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;
import manager.LinAllManager;

public class L1Trade {
	private static L1Trade _instance;

	public L1Trade() {
	}

	public static L1Trade getInstance() {
		if (_instance == null) {
			_instance = new L1Trade();
		}
		return _instance;
	}

	// 케릭교환소스
	private boolean chaTradeChk(L1PcInstance pc, L1PcInstance tradingPartner, int itemid) {
		if (tradingPartner.getNetConnection().getAccount().countCharacters() >= tradingPartner.getNetConnection().getAccount().getCharSlot()) {
			tradingPartner.setChaTradeSlot(true);
		}
		Account account = Account.load(pc.getAccountName()); // 추가
		if (account.getquize() != null) {
			pc.sendPackets(new S_SystemMessage(".퀴즈인증 [설정한퀴즈] \\fY인증후 다시 거래하세요."));
			return false;
		} // 봉인해제시 퀴즈가 설정되어 있지 않다면 신청불가능 하도록

		if (itemid == 100904) { // 하급
			if (pc.getLevel() > 69) {
				pc.sendPackets(new S_SystemMessage("상급 캐릭터교환증표를 이용하세요."));
				return false;
			}
		} else { // 상급
			if (pc.getLevel() < 70) {
				pc.sendPackets(new S_SystemMessage("하급 캐릭터교환증표를 이용하세요."));
				return false;
			}
		}
		return true;
	}

	private boolean chaTrade(L1PcInstance pc, L1PcInstance tradingPartner, L1ItemInstance item) {
		if (!chaTradeChk(pc, tradingPartner, item.getItemId()))
			return false;

		String title = null;
		switch (pc.getType()) {
		case 0:
			title = "군주";
			break;
		case 1:
			title = "기사";
			break;
		case 2:
			title = "엘프";
			break;
		case 3:
			title = "마법사";
			break;
		case 4:
			title = "다크엘프";
			break;
		case 5:
			title = "용기사";
			break;
		case 6:
			title = "환술사";
			break;
		}
		String chatText = "거래 대상 캐릭터의 직업은 [" + title + "] 레벨은 [" + pc.getLevel() + "] 엘릭서 사용 갯수는 [" + pc.getAbility().getElixirCount()
				+ "]입니다. 캐릭터 거래시 거래 완료 후 자동으로 접속이 종료됩니다.";

		S_ChatPacket s_chatpacket = new S_ChatPacket(tradingPartner, chatText, Opcodes.S_SAY, 2);
		if (!tradingPartner.getExcludingList().contains(tradingPartner.getName())) {
			tradingPartner.sendPackets(s_chatpacket);
		}

		chatText = "현재 캐릭터는 상대방의 계정으로 넘어가고 거래 후 자동으로 접속이 종료며 상대방의 거래아이템은 이계정 창고에 저장됩니다.";
		S_ChatPacket s_chatpacket1 = new S_ChatPacket(pc, chatText, Opcodes.S_SAY, 2);
		if (!pc.getExcludingList().contains(pc.getName())) {
			pc.sendPackets(s_chatpacket1);
		}

		return true;
	}

	public void TradeAddItem(L1PcInstance player, int itemid, int itemcount) {
		L1Object trading_partner = L1World.getInstance().findObject(player.getTradeID());
		L1ItemInstance l1iteminstance = player.getInventory().getItem(itemid);
		if (l1iteminstance != null && trading_partner != null) {
			if (trading_partner instanceof L1PcInstance) {
				L1PcInstance tradepc = (L1PcInstance) trading_partner;
				if (!l1iteminstance.isEquipped()) {
					if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) { // 허상버그
																					// 관련
																					// 추가
						return;
					}
					// 아래부터 캐릭터 교환주문서 소스입니다.
					if (l1iteminstance.getItemId() == 100903 || l1iteminstance.getItemId() == 100904) {
						if (!chaTrade(player, tradepc, l1iteminstance)) {
							return;
						}

						List<?> player_tradelist = player.getTradeWindowInventory().getItems();
						int player_tradecount = player.getTradeWindowInventory().getSize();
						L1ItemInstance l1iteminstance1 = null;
						for (int cnt = 0; cnt < player_tradecount; cnt++) {
							l1iteminstance1 = (L1ItemInstance) player_tradelist.get(cnt);
							if (l1iteminstance1.getItemId() == 100903 || l1iteminstance1.getItemId() == 100904) {
								player.sendPackets(new S_SystemMessage("캐릭교환증표는 하나만 올릴수 있습니다."));
								return;
							}
						}
						player.getInventory().tradeItem(l1iteminstance, itemcount, player.getTradeWindowInventory());
						player.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 0), true);
						tradepc.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 1), true);
					} else {
						player.getInventory().tradeItem(l1iteminstance, itemcount, player.getTradeWindowInventory());
						player.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 0), true);
						tradepc.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 1), true);
					}
				}
			} else if (trading_partner instanceof L1BuffNpcInstance) {
				L1BuffNpcInstance tradenpc = (L1BuffNpcInstance) trading_partner;
				if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) { // 허상버그
																				// 관련
																				// 추가
					player.sendPackets(new S_TradeStatus(1), true);
					player.setTradeOk(false);
					tradenpc.setTradeOk(false);
					player.setTradeReady(false);
					player.setTradeID(0);
					tradenpc.setTradeID(0);
					return;
				}
				if (l1iteminstance.getItemId() != 40308) {
					player.sendPackets(new S_TradeStatus(1), true);
					player.setTradeOk(false);
					tradenpc.setTradeOk(false);
					player.setTradeReady(false);
					player.setTradeID(0);
					tradenpc.setTradeID(0);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "아데나만 올려주세요.", 0), true);
					return;
				}

				if (l1iteminstance.getItemId() == 40308 && itemcount != 100000 && tradenpc.getNpcTemplate().get_npcId() == 7000067) {
					player.sendPackets(new S_TradeStatus(1), true);
					player.setTradeOk(false);
					tradenpc.setTradeOk(false);
					player.setTradeReady(false);
					player.setTradeID(0);
					tradenpc.setTradeID(0);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "비용은 10만아덴 입니다.", 0), true);
				} else if (l1iteminstance.getItemId() == 40308 && itemcount != 200000 && tradenpc.getNpcTemplate().get_npcId() == 7000070) {
					player.sendPackets(new S_TradeStatus(1), true);
					player.setTradeOk(false);
					tradenpc.setTradeOk(false);
					player.setTradeID(0);
					tradenpc.setTradeID(0);
					player.setTradeReady(false);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "비용은 20만아덴 입니다.", 0), true);
				} else {
					player.getInventory().tradeItem(l1iteminstance, itemcount, player.getTradeWindowInventory());
					player.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 0), true);
				}
			} else if (trading_partner instanceof L1NpcShopInstance) {
				if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) { // 허상버그
																				// 관련
																				// 추가
					L1Trade trade = new L1Trade();
					trade.TradeCancel(player);
					((L1NpcShopInstance) trading_partner).Npc_trade = false;
					return;
				}
				boolean ck = false;
				for (ShopItem shop : NpcTradeShop.getInstance().getList()) {
					if (shop.getNpcId() == ((L1NpcShopInstance) trading_partner).getNpcId()) {
						ck = true;
						break;
					}
				}
				if (ck) // 교환 판매 상점
					GeneralThreadPool.getInstance().execute(new NpcShopBuyTradeDelay((L1NpcShopInstance) trading_partner, player, l1iteminstance, itemcount));
				else
					GeneralThreadPool.getInstance().execute(new NpcShopTradeDelay((L1NpcShopInstance) trading_partner, player, l1iteminstance, itemcount));
				player.getInventory().tradeItem(l1iteminstance, itemcount, player.getTradeWindowInventory());
				player.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 0), true);
			} else if (trading_partner instanceof GambleInstance) {
				GambleInstance tradenpc = (GambleInstance) trading_partner;
				if (l1iteminstance.getCount() < itemcount || 0 >= itemcount) { // 허상버그
																				// 관련
																				// 추가
					TradeCancel(player);
				} else if (l1iteminstance.getItemId() != 40308) {
					TradeCancel(player);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "아데나만 올려주세요.", 0), true);
				} else if (l1iteminstance.getItemId() == 40308 && (itemcount < 200000 || itemcount > 50000000)) {
					TradeCancel(player);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "20만~5000만 까지만 가능해요~", 0), true);
				} else {
					player.getInventory().tradeItem(l1iteminstance, itemcount, player.getTradeWindowInventory());
					player.sendPackets(new S_TradeAddItem(l1iteminstance, itemcount, 0), true);
				}
			}
		}
	}

	class NpcShopBuyTradeDelay implements Runnable {
		private L1NpcShopInstance tradenpc;
		private L1PcInstance player;
		private L1ItemInstance l1iteminstance;
		private int itemcount;
		private long time;

		public NpcShopBuyTradeDelay(L1NpcShopInstance npc, L1PcInstance pc, L1ItemInstance item, int count) {
			tradenpc = npc;
			player = pc;
			l1iteminstance = item;
			itemcount = count;
			time = 3000 + System.currentTimeMillis();
			tradenpc.tradeItemck = true;
		}

		@Override
		public void run() {
			while (time > System.currentTimeMillis()) {
				try {
					Thread.sleep(500L);
					if (!tradenpc.Npc_trade)
						break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (tradenpc.Npc_trade)
				addItem();
		}

		private void addItem() {
			if (tradenpc == null || player == null)
				return;

			if (l1iteminstance.getItemId() != 40308) {
				Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "아데나만 올려주세용~", 0), true);
				tradenpc.Npc_trade = false;
				L1Trade trade = new L1Trade();
				trade.TradeCancel(player);
				return;
			}

			/*
			 * int player_tradecount = player.getTradeWindowInventory().getSize();
			 * if(player_tradecount > 1){ Broadcaster.broadcastPacket(tradenpc, new
			 * S_NpcChatPacket(tradenpc, "한번에 올려주세요 ^ ㅡ^", 0)); tradenpc.Npc_trade = false;
			 * L1Trade trade = new L1Trade(); trade.TradeCancel(player); return; }
			 */

			ShopItem[] list = NpcTradeShop.getInstance().getShopList();
			if (list == null) {
				tradenpc.Npc_trade = false;
				L1Trade trade = new L1Trade();
				trade.TradeCancel(player);
				return;
			}
			boolean ck = false;
			for (ShopItem shop : list) {
				if (shop == null || tradenpc.getNpcId() != shop.getNpcId())
					continue;
				double d = itemcount / shop.getPrice();
				if ((shop.getPrice() <= itemcount && itemcount % shop.getPrice() == 0) && d - (int) d == 0.0) {
					L1Item tempL1Item = ItemTable.getInstance().getTemplate(shop.getItemId());
					if (tempL1Item == null) {
						L1Trade trade = new L1Trade();
						trade.TradeCancel(player);
						tradenpc.Npc_trade = false;
						return;
					}
					if (tradenpc.tradeSellItemid != 0 && shop.getItemId() != tradenpc.tradeSellItemid) {
						Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "한번의 한 종류의 물건만 구입가능합니다.", 0), true);
						L1Trade trade = new L1Trade();
						trade.TradeCancel(player);
						tradenpc.Npc_trade = false;
						return;
					}
					L1ItemInstance tempitem = ItemTable.getInstance().FunctionItem(tempL1Item);
					if (tempitem.isStackable()) {
						tempitem.setIdentified(true);
						tempitem.setBless(tempL1Item.getBless());
						tempitem.setEnchantLevel(shop.getEnchant());
						tempitem.setCount((int) d);
						player.sendPackets(new S_TradeAddItem(tempitem, tempitem.getCount(), 1));
					} else {
						for (int i = 0; i < d; i++) {
							tempitem.setIdentified(true);
							tempitem.setEnchantLevel(shop.getEnchant());
							tempitem.setBless(tempL1Item.getBless());
							tempitem.setCount(1);
							player.sendPackets(new S_TradeAddItem(tempitem, tempitem.getCount(), 1));
						}
					}

					tradenpc.tradeItemck = false;
					ck = true;
					tradenpc.tradeSellItemid = shop.getItemId();
					break;
				}
			}
			if (!ck) {
				Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "아덴이 맞지 않네용~", 0), true);
				L1Trade trade = new L1Trade();
				trade.TradeCancel(player);
				tradenpc.Npc_trade = false;
				return;
			}
		}
	}

	class NpcShopTradeDelay implements Runnable {
		private L1NpcShopInstance tradenpc;
		private L1PcInstance player;
		private L1ItemInstance l1iteminstance;
		private int itemcount;
		private long time;

		public NpcShopTradeDelay(L1NpcShopInstance npc, L1PcInstance pc, L1ItemInstance item, int count) {
			tradenpc = npc;
			player = pc;
			l1iteminstance = item;
			itemcount = count;
			time = 3000 + System.currentTimeMillis();
			tradenpc.tradeItemck = true;
		}

		@Override
		public void run() {
			while (time > System.currentTimeMillis()) {
				try {
					Thread.sleep(500L);
					if (!tradenpc.Npc_trade)
						break;
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if (tradenpc.Npc_trade)
				addItem();
		}

		private String[] msg = { "그 템은 매입 안해요", "그 아이템은 매입 안해요", "죄송해요 그건 매입 안합니다" };

		private synchronized void addItem() {
			if (tradenpc == null || player == null)
				return;

			int player_tradecount = player.getTradeWindowInventory().getSize();
			if (player_tradecount > 5) {
				Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "5개 까지만 올려주세요 ^^", 0), true);
				tradenpc.Npc_trade = false;
				L1Trade trade = new L1Trade();
				trade.TradeCancel(player);
				return;
			}
			L1ShopItem item;

			if (tradenpc.getState() == 3)
				item = NpcBuyShop.getInstance().getItem(tradenpc.getNpcId(), l1iteminstance.getItemId(), l1iteminstance.getEnchantLevel(),
						l1iteminstance.getAttrEnchantLevel(), l1iteminstance.getBless());
			else
				item = NpcBuyShop.getInstance().getItem(l1iteminstance.getItemId(), l1iteminstance.getEnchantLevel(), l1iteminstance.getAttrEnchantLevel(),
						l1iteminstance.getBless());
			if (item == null || item.getBuyPrice() == 0) {
				Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, msg[new Random().nextInt(msg.length)], 0), true);
				// player.sendPackets(new S_TradeStatus(1));
				L1Trade trade = new L1Trade();
				trade.TradeCancel(player);
				tradenpc.Npc_trade = false;
				if (tradenpc.getState() == 3)
					LogTable.log_npc_buy_cancel(player, l1iteminstance, itemcount);
				return;
			} else if (l1iteminstance.getEnchantLevel() > 0 && !l1iteminstance.isIdentified()) {
				Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "확인 좀 바르고 오지??", 0), true);
				// player.sendPackets(new S_TradeStatus(1));
				L1Trade trade = new L1Trade();
				trade.TradeCancel(player);
				tradenpc.Npc_trade = false;
				return;
			} else {
				L1Item tempL1Item = ItemTable.getInstance().getTemplate(40308);
				L1ItemInstance tempitem = ItemTable.getInstance().FunctionItem(tempL1Item);
				tempitem.setCount(item.getBuyPrice() * itemcount);
				player.sendPackets(new S_TradeAddItem(tempitem, tempitem.getCount(), 1), true);
				tradenpc.tradeItemck = false;
			}
		}
	}

	private void chaTradeAccount(String accountName, String chaName) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET account_name=? WHERE char_name=?");
			pstm.setString(1, accountName);
			pstm.setString(2, chaName);
			pstm.executeUpdate();
		} catch (Exception e) {

		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	private void chaTradeOk(L1PcInstance player, L1PcInstance trading_partner, boolean chaChk1, boolean chaChk2) {
		if (chaChk1 || chaChk2) {
			player.sendPackets(new S_Disconnect(), true);
			trading_partner.sendPackets(new S_Disconnect(), true);
		}
		if (chaChk1) {
			// player.sendPackets(new S_Disconnect());
			chaTradeAccount(trading_partner.getAccountName(), player.getName());
		}
		if (chaChk2) {
			// trading_partner.sendPackets(new S_Disconnect());
			chaTradeAccount(player.getAccountName(), trading_partner.getName());
		}
	}

	private boolean chaTradeItemChk(List<?> pc_tradelist, int listcount) {
		L1ItemInstance l1iteminstance = null;
		for (int cnt = 0; cnt < listcount; cnt++) {
			l1iteminstance = (L1ItemInstance) pc_tradelist.get(cnt);
			if (l1iteminstance.getItemId() == 100903 || l1iteminstance.getItemId() == 100904) {
				return true;
			}
		}
		return false;
	}

	public void TradeOK_NpcTradeShop(L1PcInstance pc) {
		// try {
		try {

			int cnt;
			L1Object trading_partner = L1World.getInstance().findObject(pc.getTradeID());
			if (trading_partner != null) {
				if (trading_partner instanceof L1NpcShopInstance) {
					L1NpcShopInstance tradenpc = (L1NpcShopInstance) trading_partner;
					if (tradenpc.tradeItemck) {
						TradeCancel(pc);
						return;
					}
					List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
					int player_tradecount = pc.getTradeWindowInventory().getSize();
					if (player_tradecount == 0) {
						TradeCancel(pc);
						return;
					}
					L1ItemInstance pcitem = null;
					for (cnt = 0; cnt < player_tradecount; cnt++) {
						pcitem = (L1ItemInstance) player_tradelist.get(0);
						pc.getTradeWindowInventory().tradeItem(pcitem, pcitem.getCount(), tradenpc.getInventory());
						tradenpc.getInventory().consumeItem(pcitem.getItemId(), pcitem.getCount());
						ShopItem[] list2 = NpcTradeShop.getInstance().getShopList();
						// L1ItemInstance item = null;
						for (ShopItem shop : list2) {
							if (shop == null || tradenpc.getNpcId() != shop.getNpcId())
								continue;
							double d = pcitem.getCount() / shop.getPrice();
							if ((shop.getPrice() <= pcitem.getCount() && pcitem.getCount() % shop.getPrice() == 0) && d - (int) d == 0.0) {
								// if(d - (int)d == 0.0){
								L1Item tempL1Item = ItemTable.getInstance().getTemplate(shop.getItemId());
								if (tempL1Item.isStackable()) {
									L1ItemInstance tempitem = ItemTable.getInstance().FunctionItem(tempL1Item);
									tempitem.setIdentified(true);
									tempitem.setEnchantLevel(shop.getEnchant());
									tempitem.setCount((int) d);
									tempitem.setId(ObjectIdFactory.getInstance().nextId());
									pc.getInventory().storeItem(tempitem);
								} else {
									for (int i = 0; i < d; i++) {
										L1ItemInstance tempitem = ItemTable.getInstance().FunctionItem(tempL1Item);
										tempitem.setIdentified(true);
										tempitem.setEnchantLevel(shop.getEnchant());
										tempitem.setCount(1);
										tempitem.setId(ObjectIdFactory.getInstance().nextId());
										pc.getInventory().storeItem(tempitem);
									}
								}
							}
						}

						LinAllManager.getInstance().TradeAppend(pcitem.getViewName(), pc.getName(), tradenpc.getName());
					}
					tradenpc.tradeSellItemid = 0;
					pc.sendPackets(new S_TradeStatus(0));
					pc.setTradeOk(false);
					pc.setTradeReady(false);
					tradenpc.Npc_trade = false;
					pc.setTradeID(0);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void TradeOK_NpcShop(L1PcInstance pc) {
		// try {
		// try {

		int cnt;
		L1Object trading_partner = L1World.getInstance().findObject(pc.getTradeID());
		if (trading_partner != null) {
			if (trading_partner instanceof L1NpcShopInstance) {
				L1NpcShopInstance tradenpc = (L1NpcShopInstance) trading_partner;
				if (tradenpc.tradeItemck) {
					TradeCancel(pc);
					return;
				}
				List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
				int player_tradecount = pc.getTradeWindowInventory().getSize();
				if (player_tradecount == 0) {
					TradeCancel(pc);
					return;
				}
				L1ItemInstance pcitem = null;
				for (cnt = 0; cnt < player_tradecount; cnt++) {
					pcitem = (L1ItemInstance) player_tradelist.get(0);
					L1ShopItem item;
					if (tradenpc.getState() == 3)
						item = NpcBuyShop.getInstance().getItem(tradenpc.getNpcId(), pcitem.getItemId(), pcitem.getEnchantLevel(), pcitem.getAttrEnchantLevel(),
								pcitem.getBless());
					else
						item = NpcBuyShop.getInstance().getItem(pcitem.getItemId(), pcitem.getEnchantLevel(), pcitem.getAttrEnchantLevel(), pcitem.getBless());
					pc.getTradeWindowInventory().tradeItem(pcitem, pcitem.getCount(), tradenpc.getInventory());
					item.setDeleteTime(pcitem.getEndTime());
					tradenpc.getInventory().consumeItem(pcitem.getItemId(), pcitem.getCount());
					pc.getInventory().storeItem(40308, item.getBuyPrice() * pcitem.getCount());
					if (tradenpc.getState() == 3) {
						LogTable.log_npc_buy_ok(pc, item, pcitem.getCount());
					} else {
						for (int i = 0; i < pcitem.getCount(); i++) {
							NpcBuyShop.getInstance().insert(item.getItemId(), item.getEnchant(), item.getAttr(), item.getDeleteTime(), item.getBless());
						}
					}
					String att = "";
					switch (item.getAttr()) {
					case 1:
						att = "화령1단 ";
						break;
					case 2:
						att = "화령2단 ";
						break;
					case 3:
						att = "화령3단 ";
						break;
					case 4:
						att = "수령1단 ";
						break;
					case 5:
						att = "수령2단 ";
						break;
					case 6:
						att = "수령3단 ";
						break;
					case 7:
						att = "풍령1단 ";
						break;
					case 8:
						att = "풍령2단 ";
						break;
					case 9:
						att = "풍령3단 ";
						break;
					case 10:
						att = "지령1단 ";
						break;
					case 11:
						att = "지령2단 ";
						break;
					case 12:
						att = "지령3단 ";
						break;
					case 33:
						att = "화령4단 ";
						break;
					case 34:
						att = "화령5단 ";
						break;
					case 35:
						att = "수령4단 ";
						break;
					case 36:
						att = "수령5단 ";
						break;
					case 37:
						att = "풍령4단 ";
						break;
					case 38:
						att = "풍령5단 ";
						break;
					case 39:
						att = "지령4단 ";
						break;
					case 40:
						att = "지령5단 ";
						break;
					default:
						break;
					}
					L1PcInstance[] list = L1World.getInstance().getAllPlayersToArray();
					for (L1PcInstance pc1 : list) {
						if (pc1.isGm())
							pc1.sendPackets(new S_SystemMessage("\\fWID:" + pc.getName() + " BUY:" + item.getBuyPrice() + " ITEM:" + att + "" + "+"
									+ item.getEnchant() + item.getItem().getName()));
					}
					list = null;

					LinAllManager.getInstance().TradeAppend(pcitem.getViewName(), pc.getName(), tradenpc.getName());
				}
				tradenpc.tradeSellItemid = 0;
				pc.sendPackets(new S_TradeStatus(0));
				pc.setTradeOk(false);
				pc.setTradeReady(false);
				tradenpc.Npc_trade = false;
				pc.setTradeID(0);
			}
		}
	}

	private static Logger _log = Logger.getLogger(L1Trade.class.getName());

	public void TradeOK(L1PcInstance pc) {
		try {
			int cnt;
			boolean chaChk1 = false; // 캐릭터교환주문서
			boolean chaChk2 = false; // 캐릭터교환주문서
			L1Object trading_partner = L1World.getInstance().findObject(pc.getTradeID());
			if (trading_partner != null) {
				if (trading_partner instanceof L1PcInstance) {
					L1PcInstance tradepc = (L1PcInstance) trading_partner;
					try {
						List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
						int player_tradecount = pc.getTradeWindowInventory().getSize();
						// System.out.println(player_tradecount);
						List<?> trading_partner_tradelist = tradepc.getTradeWindowInventory().getItems();
						int trading_partner_tradecount = tradepc.getTradeWindowInventory().getSize();

						chaChk1 = chaTradeItemChk(player_tradelist, player_tradecount);
						chaChk2 = chaTradeItemChk(trading_partner_tradelist, trading_partner_tradecount);

						L1ItemInstance pcitem = null;
						for (cnt = 0; cnt < player_tradecount; cnt++) {
							pcitem = (L1ItemInstance) player_tradelist.get(0);
							if (chaChk2 && !(pcitem.getItemId() == 100903 || pcitem.getItemId() == 100904)) {
								PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(tradepc.getAccountName());

								if (warehouse == null)
									return;

								if (warehouse.checkAddItemToWarehouse(pcitem, player_tradecount) == L1Inventory.SIZE_OVER) {
									tradepc.sendPackets(new S_ServerMessage(75));
									break;
								}
								pc.getTradeWindowInventory().tradeItem(pcitem, pcitem.getCount(), warehouse);
								LinAllManager.getInstance().TradeAppend(pcitem.getViewName(), pc.getName(), tradepc.getName());
							} else {
								pc.getTradeWindowInventory().tradeItem(pcitem, pcitem.getCount(), tradepc.getInventory());
								LogTable.logtrade(pc, tradepc, pcitem);
								if (player_tradecount == 1 && trading_partner_tradecount == 0 && pcitem.getItemId() == 40308) {
									운영자메시지(pc, "\\fS아덴교환 [" + pc.getName() + "->" + tradepc.getName() + "] 갯수: " + pcitem.getCount());
								}
								LinAllManager.getInstance().TradeAppend(pcitem.getViewName(), pc.getName(), tradepc.getName());
							}
						}
						L1ItemInstance tradepcitem = null;
						for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
							tradepcitem = (L1ItemInstance) trading_partner_tradelist.get(0);
							if (chaChk1 && !(tradepcitem.getItemId() == 100903 || tradepcitem.getItemId() == 100904)) {
								PrivateWarehouse warehouse = WarehouseManager.getInstance().getPrivateWarehouse(pc.getAccountName());
								if (warehouse == null)
									return;

								if (warehouse.checkAddItemToWarehouse(tradepcitem, trading_partner_tradecount) == L1Inventory.SIZE_OVER) {
									pc.sendPackets(new S_ServerMessage(75));
									break;
								}

								tradepc.getTradeWindowInventory().tradeItem(tradepcitem, tradepcitem.getCount(), warehouse);
								
								LinAllManager.getInstance().TradeAppend(tradepcitem.getViewName(), pc.getName(), tradepc.getName());
							} else {
								tradepc.getTradeWindowInventory().tradeItem(tradepcitem, tradepcitem.getCount(), pc.getInventory());
								LogTable.logtrade(tradepc, pc, tradepcitem);
								if (trading_partner_tradecount == 1 && player_tradecount == 0 && tradepcitem.getItemId() == 40308) {
									운영자메시지(tradepc, "\\fS아덴교환 [" + tradepc.getName() + "->" + pc.getName() + "] 갯수: " + tradepcitem.getCount());
								}
								LinAllManager.getInstance().TradeAppend(tradepcitem.getViewName(), pc.getName(), tradepc.getName());
							}
						}
						pc.sendPackets(new S_TradeStatus(0));
						tradepc.sendPackets(new S_TradeStatus(0));
						pc.setTradeOk(false);
						tradepc.setTradeOk(false);
						pc.setTradeID(0);
						tradepc.setTradeID(0);
						pc.setTradeReady(false);
						tradepc.setTradeReady(false);

						if (chaChk1) {
							if (tradepc.getInventory().checkItem(100903, 1)) {
								tradepc.getInventory().consumeItem(100903, 1);
							}
							if (tradepc.getInventory().checkItem(100904, 1)) {
								tradepc.getInventory().consumeItem(100904, 1);
							}
						}
						if (chaChk2) {
							if (pc.getInventory().checkItem(100903, 1)) {
								pc.getInventory().consumeItem(100903, 1);
							}
							if (pc.getInventory().checkItem(100904, 1)) {
								pc.getInventory().consumeItem(100904, 1);
							}
						}
						chaTradeOk(pc, tradepc, chaChk1, chaChk2);

					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if (trading_partner instanceof L1BuffNpcInstance) {
					L1BuffNpcInstance tradenpc = (L1BuffNpcInstance) trading_partner;
					List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
					int player_tradecount = pc.getTradeWindowInventory().getSize();

					L1ItemInstance pcitem = null;
					for (cnt = 0; cnt < player_tradecount; cnt++) {
						pcitem = (L1ItemInstance) player_tradelist.get(0);
						L1Castle castle = CastleTable.getInstance().getCastleTable(4);
						L1Castle castle2 = CastleTable.getInstance().getCastleTable(6);
						int money = castle.getPublicReadyMoney() + pcitem.getCount() / 3;
						int money2 = castle2.getPublicReadyMoney() + pcitem.getCount() / 10;
						castle.setPublicReadyMoney(money);
						castle2.setPublicReadyMoney(money2);
						pc.getTradeWindowInventory().consumeItem(pcitem.getItemId(), pcitem.getCount());
						LinAllManager.getInstance().TradeAppend(pcitem.getViewName(), pc.getName(), tradenpc.getName());
					}

					pc.sendPackets(new S_TradeStatus(0));
					pc.setTradeOk(false);
					tradenpc.setTradeOk(false);
					pc.setTradeID(0);
					tradenpc.setTradeID(0);
					pc.setTradeReady(false);
					pc.getLight().turnOnOffLight();
					tradenpc.getLight().turnOnOffLight();
					if (tradenpc.getNpcTemplate().get_npcId() == 7000067) { // 버프(법사,요정)
						new BuffSystem(tradenpc, pc, pcitem.getCount());
						Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "이용해 주셔서 감사합니다. 고객님.", 0), true);
					} else if (tradenpc.getNpcTemplate().get_npcId() == 7000068) { // 주사위(딜러)
						Akduk2GameSystem gambling = new Akduk2GameSystem();
						gambling.Gambling(pc, pcitem.getCount());
					} else if (tradenpc.getNpcTemplate().get_npcId() == 7000069) { // 소막(딜러)
						Akduk1GameSystem gambling = new Akduk1GameSystem();
						gambling.Gambling(pc, pcitem.getCount());
					} else if (tradenpc.getNpcTemplate().get_npcId() == 7000071) { // 소막2(딜러)
						Akduk3GameSystem gambling = new Akduk3GameSystem();
						gambling.Gambling(pc, pcitem.getCount());
					} else if (tradenpc.getNpcTemplate().get_npcId() == 7000078) { // 묵
																					// 찌
																					// 빠(딜러)
						Akduk4GameSystem gambling = new Akduk4GameSystem();
						gambling.Gambling(pc, pcitem.getCount());
					} else if (tradenpc.getNpcTemplate().get_npcId() == 7000070) { // 버프(군주,환술사,용기사
						new BigBuffSystem(tradenpc, pc);
						Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, "이용해 주셔서 감사합니다. 고객님.", 0), true);
					}
				} else if (trading_partner instanceof GambleInstance) {
					GambleInstance tradenpc = (GambleInstance) trading_partner;
					List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
					int player_tradecount = pc.getTradeWindowInventory().getSize();
					if (!tradenpc.play && player_tradecount == 0) {
						TradeCancel(pc);
						return;
					}
					L1ItemInstance pcitem = null;
					for (cnt = 0; cnt < player_tradecount; cnt++) {
						pcitem = (L1ItemInstance) player_tradelist.get(0);
						pc.getTradeWindowInventory().consumeItem(pcitem.getItemId(), pcitem.getCount());
						
						LinAllManager.getInstance().TradeAppend(pcitem.getViewName(), pc.getName(), tradenpc.getName());
					}
					pc.sendPackets(new S_TradeStatus(0));
					pc.setTradeOk(false);
					pc.setTradeID(0);
					pc.setTradeReady(false);
					tradenpc.Npc_trade = false;
					if (tradenpc.play) {
						if (tradenpc.aden > 0)
							pc.getInventory().storeItem(40308, tradenpc.aden);
						tradenpc.play = false;
						tradenpc.aden = 0;
					} else {
						tradenpc.play = true;
						tradenpc.start(pc, pcitem.getCount());
					}
					pc.getLight().turnOnOffLight();
					tradenpc.getLight().turnOnOffLight();
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			e.printStackTrace();

		}
	}

	private void 운영자메시지(L1PcInstance pc, String msg) {
		if (GMCommands.아덴교환체크) {
			for (L1PcInstance gm : L1World.getInstance().getAllPlayersToArray()) {
				if (gm.isGm() && pc != gm) {
					gm.sendPackets(new S_SystemMessage(msg));
				}
			}
		}
	}

	// 추가

	/*
	 * new BuffSystem(tradenpc, pc); } } } catch (Exception e) {
	 * System.out.println("오류"); } }
	 */

	public void TradeCancel(L1PcInstance pc) {
		int cnt;
		L1Object trading_partner = L1World.getInstance().findObject(pc.getTradeID());
		if (trading_partner != null) {
			if (trading_partner instanceof L1PcInstance) {
				L1PcInstance tradepc = (L1PcInstance) trading_partner;
				List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
				int player_tradecount = pc.getTradeWindowInventory().getSize();

				List<?> trading_partner_tradelist = tradepc.getTradeWindowInventory().getItems();
				int trading_partner_tradecount = tradepc.getTradeWindowInventory().getSize();
				L1ItemInstance pcitem = null;
				try {
					for (cnt = 0; cnt < player_tradecount; cnt++) {
						pcitem = (L1ItemInstance) player_tradelist.get(0);
						pc.getTradeWindowInventory().tradeItem(pcitem, pcitem.getCount(), pc.getInventory());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				L1ItemInstance tradepcitem = null;
				try {
					for (cnt = 0; cnt < trading_partner_tradecount; cnt++) {
						tradepcitem = (L1ItemInstance) trading_partner_tradelist.get(0);
						tradepc.getTradeWindowInventory().tradeItem(tradepcitem, tradepcitem.getCount(), tradepc.getInventory());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				pc.sendPackets(new S_TradeStatus(1), true);
				tradepc.sendPackets(new S_TradeStatus(1), true);
				pc.setTradeOk(false);
				tradepc.setTradeOk(false);
				pc.setTradeID(0);
				tradepc.setTradeID(0);
				pc.setTradeReady(false);
				tradepc.setTradeReady(false);
			} else if (trading_partner instanceof L1BuffNpcInstance) {
				L1BuffNpcInstance tradenpc = (L1BuffNpcInstance) trading_partner;
				List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
				int player_tradecount = pc.getTradeWindowInventory().getSize();

				L1ItemInstance pcitem = null;
				for (cnt = 0; cnt < player_tradecount; cnt++) {
					pcitem = (L1ItemInstance) player_tradelist.get(0);
					pc.getTradeWindowInventory().tradeItem(pcitem, pcitem.getCount(), pc.getInventory());
				}
				pc.sendPackets(new S_TradeStatus(1), true);
				pc.setTradeOk(false);
				tradenpc.setTradeOk(false);
				pc.setTradeReady(false);
				pc.setTradeID(0);
				tradenpc.setTradeID(0);
			} else if (trading_partner instanceof L1NpcShopInstance) {
				L1NpcShopInstance tradenpc = (L1NpcShopInstance) trading_partner;
				List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
				int player_tradecount = pc.getTradeWindowInventory().getSize();

				try {
					L1ItemInstance pcitem = null;
					if (player_tradecount > 0 && player_tradelist != null) {
						for (cnt = 0; cnt < player_tradecount; cnt++) {
							pcitem = (L1ItemInstance) player_tradelist.get(0);

							pc.getTradeWindowInventory().tradeItem(pcitem, pcitem.getCount(), pc.getInventory());
						}
					}
				} catch (Exception e) {
				}
				pc.sendPackets(new S_TradeStatus(1), true);
				pc.setTradeOk(false);
				pc.setTradeReady(false);
				tradenpc.Npc_trade = false;
				pc.setTradeID(0);
				tradenpc.tradeSellItemid = 0;
			} else if (trading_partner instanceof GambleInstance) {
				GambleInstance tradenpc = (GambleInstance) trading_partner;
				List<?> player_tradelist = pc.getTradeWindowInventory().getItems();
				int player_tradecount = pc.getTradeWindowInventory().getSize();

				L1ItemInstance pcitem = null;
				if (player_tradecount > 0 && player_tradelist != null) {
					for (cnt = 0; cnt < player_tradecount; cnt++) {
						pcitem = (L1ItemInstance) player_tradelist.get(0);
						pc.getTradeWindowInventory().tradeItem(pcitem, pcitem.getCount(), pc.getInventory());
					}
				}
				pc.sendPackets(new S_TradeStatus(1), true);
				pc.setTradeOk(false);
				pc.setTradeReady(false);
				tradenpc.Npc_trade = false;
				pc.setTradeID(0);
				if (tradenpc.play)
					tradenpc.play = false;
				if (tradenpc.aden > 0) {
					pc.getInventory().storeItem(40308, tradenpc.aden);
					Broadcaster.broadcastPacket(tradenpc, new S_NpcChatPacket(tradenpc, pc.getName() + "님 교환을 취소하셔서 인벤에 넣어드렸어여~", 0), true);
					tradenpc.aden = 0;
				}
			}
		}
	}
}