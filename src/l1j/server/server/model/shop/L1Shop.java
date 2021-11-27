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
package l1j.server.server.model.shop;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.Config;
import l1j.server.GameSystem.DogFight.DogFight;
import l1j.server.GameSystem.NpcBuyShop.NpcBuyShop;
import l1j.server.GameSystem.NpcBuyShop.NpcBuyShopSell;
import l1j.server.server.ActionCodes;
import l1j.server.server.GMCommands;
import l1j.server.server.datatables.CastleTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.LogTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1BugBearRace;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1TaxCalculator;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Castle;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.IntRange;
import manager.LinAllManager;

public class L1Shop {
	private final int _npcId;
	private final List<L1ShopItem> _sellingItems;
	private final List<L1ShopItem> _purchasingItems;

	public L1Shop(int npcId, List<L1ShopItem> sellingItems, List<L1ShopItem> purchasingItems) {
		if (sellingItems == null || purchasingItems == null) {
			throw new NullPointerException();
		}
		_npcId = npcId;
		_sellingItems = sellingItems;
		_purchasingItems = purchasingItems;
	}

	public void sellItems(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		// 세율 안들어가는 NPC
		if (getNpcId() == 70068 || getNpcId() == 70020 || getNpcId() == 70056 || getNpcId() == 70051 || getNpcId() == 70055 || getNpcId() == 4213002
				|| getNpcId() == 70017 || getNpcId() == 4200105 || getNpcId() == 5000136 || getNpcId() == 8000055) {
			if (!NoTaxEnsureSell(pc, orderList)) {
				return;
			}
			NoTaxSellItems(pc.getInventory(), orderList);
			return;
		}
		// 영자쫄따구 엔피씨 상점
		if (getNpcId() >= 8000000) { // 해당 번호 위부터 전부 인식
			if (!NoTaxEnsureSell(pc, orderList)) {
				return;
			}

			NpcShopSellItems(pc, pc.getInventory(), orderList);
			return;

		}

		// 고대의 금화 상인 (트릭)
		if (getNpcId() == 4208001) {
			if (!AGEnsureSell(pc, orderList)) {
				return;
			}
			AGSellItems(pc.getInventory(), orderList);
			return;
		}
		// 프리미엄 상점
		if (getNpcId() == 4221700 || getNpcId() == 646414 || getNpcId() == 646415 || getNpcId() == 646416

				|| getNpcId() == 4220018 || getNpcId() == 4220000 || getNpcId() == 4220001 || getNpcId() == 4220002 || getNpcId() == 4220003
				|| getNpcId() == 4220700 || getNpcId() == 4500172 || getNpcId() == 4500175 || getNpcId() == 4500169 || getNpcId() == 100289
				|| getNpcId() == 100290) {
			if (!ensurePremiumSell(pc, orderList)) {
				return;
			}
			sellPremiumItems(pc.getInventory(), orderList);
			return;
		}
		// 전쟁물자 상인(코인)
		if (getNpcId() == 7000043) {
			if (!ensureMarkSell(pc, orderList)) {
				return;
			}
			sellMarkItems(pc.getInventory(), orderList);
			return;
		}

		// 호박씨 - 블룸
		if (getNpcId() == 100387) {
			if (!호박씨체크(pc, orderList))
				return;
			호박씨소비(pc.getInventory(), orderList);
			return;
		}

		// 싸이비
		if (getNpcId() == 100430) {
			if (!마법의알약체크(pc, orderList))
				return;
			마법의알약소비(pc.getInventory(), orderList);
			return;
		}

		// 환전인
		if (getNpcId() == 100564) {
			if (!환전체크(pc, orderList))
				return;
			환전(pc.getInventory(), orderList);
			return;
		}

		// 베리
		if (getNpcId() == 100605) {
			if (!베리체크(pc, orderList))
				return;
			베리(pc.getInventory(), orderList);
			return;
		}

		// 오림 은둔형
		if (getNpcId() == 77221) {
			if (!가넷체크(pc, orderList))
				return;
			가넷(pc.getInventory(), orderList);
			return;
		}
		// 하딘의 돌골렘
		if (getNpcId() == 77223) {
			if (!가넷2체크(pc, orderList))
				return;
			가넷2(pc.getInventory(), orderList);
			return;
		}

		/** 기사단의 주화 */
		if (getNpcId() == 77227 || getNpcId() == 77229 || (getNpcId() >= 10 && getNpcId() <= 18) || (getNpcId() >= 30 && getNpcId() <= 38)) {
			if (!주화체크(pc, orderList))
				return;
			주화(pc.getInventory(), orderList);
			return;
		}

		// 패키지코인상점
		// 1차 코인 상인
		if (getNpcId() >= 40000 && getNpcId() <= 40035) {
			if (!ensureCashSell1(pc, orderList, getNpcId())) {
				return;
			}
			sellCashItems1(pc, pc.getInventory(), orderList, getNpcId());
			return;
		}

		if (getNpcId() >= 7001411 && getNpcId() <= 7001426) { // 특화 코인상점
			if (!특화코인체크(pc, orderList))
				return;
			특화코인(pc.getInventory(), orderList);
			return;
		}
		// 판도라 증서
		if (getNpcId() == 70014) {
			if (!판도라증서체크(pc, orderList))
				return;
			판도라의증서(pc.getInventory(), orderList);
			return;
		}

		// Tam 포인트
		if (getNpcId() == 100725) {
			if (!TAM체크(pc, orderList))
				return;
			TAM(pc.getInventory(), orderList);
			return;
		}

		// 그 외
		if (!ensureSell(pc, orderList)) {
			return;
		} else {
			sellItems(pc.getInventory(), orderList);
		}
	}

	private boolean ensureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = 0;
		if (getNpcId() == 70030 || getNpcId() == 4500166 || getNpcId() == 4500171 || getNpcId() == 4200102) {
			price = orderList.getTotalPriceTaxIncluded();
		} else
			price = orderList.getTotalPrice();

		if (!IntRange.includes(price, 0, 2000000000)) {
			pc.sendPackets(new S_ServerMessage(904, "2000000000"), true);
			return false;
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.sendPackets(new S_ServerMessage(189), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}

		return true;
	}

	private void sellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		try {

			/** 특정 NPC 말고는 세금 계산 안하게 **/
			if (getNpcId() == 70030 || getNpcId() == 4500166 || getNpcId() == 4500171 || getNpcId() == 4200102) {
				if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPriceTaxIncluded())) {
					throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
				}
			} else {
				if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPrice())) {
					throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
				}
			}

			L1ItemInstance item = null;
			for (L1ShopBuyOrder order : orderList.getList()) {
				int itemId = order.getItem().getItemId();
				int amount = order.getCount();
				int enchant = order.getItem().getEnchant();///////// < shop 인챈템 팔기 추가
				item = ItemTable.getInstance().createItem(itemId);
				item.setEnchantLevel(enchant);///////////// < shop 인챈트템 팔기추가
				if (getSellingItems().contains(item)) {
					return;
				}
				if (L1MerchantInstance.oneDayBuyAdd(inv.getOwner().getAccountName(), getNpcId(), itemId))
					continue;
				item.setCount(amount);
				item.setIdentified(true);

				/** 배당을 측정하기 위한 추가 부분 */
				if (getNpcId() == 70035 || getNpcId() == 70041 || getNpcId() == 70042) {
					int[] ticket = L1BugBearRace.getInstance().getTicketInfo(order.getOrderNumber());
					item.setIdentified(false);
					item.setSecondId(ticket[0]);
					item.setRoundId(ticket[1]);
					item.setTicketId(ticket[2]);
					item.setCount(amount);
					item.setCreaterName(L1BugBearRace.getInstance().getTicketBugName(ticket[2]));
					L1BugBearRace.getInstance().addBetting(order.getOrderNumber(), amount);
					ticket = null;
				} else if (getNpcId() == 72320) {
					int[] ticket = DogFight.getInstance().getTicketInfo(order.getOrderNumber());
					item.setIdentified(false);
					item.setSecondId(ticket[0]);
					item.setRoundId(ticket[1]);
					item.setTicketId(ticket[2]);
					item.setCount(ticket[3] * amount);
					item.setCreaterName(DogFight.getInstance().getTicketBugName(ticket[2]));
					DogFight.getInstance().addBetting(order.getOrderNumber(), ticket[3] * amount);
					ticket = null;
				} else
					item.setCount(amount);
				inv.storeItem(item);
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
				LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void NpcShopSellItems(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList) {
		L1NpcShopInstance npc = null;
		npc = L1World.getInstance().getNpcShop(getNpcId());
		List<L1ShopItem> sellList = getSellingItems();
		synchronized (sellList) {
			if (npc == null) {
				pc.sendPackets(new S_SystemMessage("잘못된 접근 메티스에게 연락바랍니다."), true);
				return;
			}

			L1PrivateShopSellList pssl;
			if (pc.getPartnersPrivateShopItemCount() != sellList.size())
				return;
			if (pc.getPartnersPrivateShopItemCount() < sellList.size())
				return;

			if (npc.isTradingInPrivateShop()) {
				pc.sendPackets(new S_SystemMessage("이미 다른사람과 거래중 입니다. 다시 이용해주세요."), true);
				return;
			}

			/*
			 * if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPrice())){ throw new
			 * IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다."); }
			 */
			if (!pc.getInventory().checkItem(L1ItemId.ADENA, orderList.getTotalPrice())) {
				pc.sendPackets(new S_SystemMessage("구입에 필요한 아데나를 소비 할 수 없습니다."), true);
				return;
			}
			L1ItemInstance item = null;

			npc.setTradingInPrivateShop(true);
			try {
				boolean[] isRemoveFromList = new boolean[8];
				for (L1ShopBuyOrder order : orderList.getList()) {
					int orderid = order.getOrderNumber();
					int itemId = order.getItem().getItemId();
					int amount = order.getCount();
					int enchant = order.getItem().getEnchant();
					int attr = order.getItem().getAttr();
					int price = amount * sellList.get(order.getOrderNumber()).getPrice();
					int objid = order.getItem().getId();

					Timestamp deletetime = order.getItem().getDeleteTime();
					int remaindcount = getSellingItems().get(orderid).getCount();

					if (remaindcount < amount)
						continue;

					item = ItemTable.getInstance().createItem(itemId);
					if (getSellingItems().contains(item)) {
						continue;
					}

					if (pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
						inv.consumeItem(L1ItemId.ADENA, price);
					} else {
						continue;
					}

					item.setCount(amount);
					item.setIdentified(true);
					item.setEnchantLevel(enchant);
					item.setAttrEnchantLevel(attr);
					item.setEndTime(deletetime);

					if (remaindcount == amount) {
						isRemoveFromList[orderid] = true;
						try {
							NpcBuyShop.getInstance().상점아이템삭제(_npcId, objid);
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						_sellingItems.get(orderid).setCount(remaindcount - amount);
						try {
							NpcBuyShop.getInstance().상점아이템업데이트(_npcId, objid, remaindcount - amount);
						} catch (Exception e) {
							e.printStackTrace();
							// _log.log(Level.SEVERE, e.getLocalizedMessage(),
							// e);
						}
					}

					inv.storeNpcShopItem(item);
					// inv.storeItem(item);
					LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
					if (GMCommands.무인상점구매체크) {
						for (L1PcInstance temppc : L1World.getInstance().getAllPlayers()) {
							if (temppc == null || temppc.getNetConnection() == null)
								continue;
							if (temppc.isGm()) {
								temppc.sendPackets(new S_SystemMessage("\\fW개상 > 구매:" + pc.getName() + " 판매:" + npc.getName() + " 템:"
										+ item.getNumberedName(0, true) + " 개당가격:" + order.getItem().getPrice() + " 갯수:" + amount), true);
							}
						}
					}
					LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
				}
				for (int i = 7; i >= 0; i--) {
					if (isRemoveFromList[i]) {
						if (_npcId >= 8100000) {
							L1ShopItem tem = _sellingItems.get(i);
							_sellingItems.remove(i);
							NpcShopEtc(tem);
						} else {
							_sellingItems.remove(i);
						}
					}
				}
				isRemoveFromList = null;
				L1Shop ss = null;
				if (_npcId >= 8000000 && _npcId < 8100000) {
					// 법서 상인 무조건 초기화
					if (_npcId >= 8000079 && _npcId <= 8000095)
						_sellingItems.clear();
					if (_sellingItems.size() <= 0) {
						NpcShopTable.getInstance().loadSellShops(_npcId);
						NpcBuyShop.getInstance().상점아이템삭제(_npcId);
						ss = NpcShopTable.getInstance().get(_npcId);
						for (L1ShopItem shopitem : ss.getSellingItems()) {
							NpcBuyShop.getInstance().SaveShop(npc, shopitem, shopitem.getPrice(), shopitem.getCount());
						}

					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				npc.setTradingInPrivateShop(false);
			}
		}
	}

	// private static final String[] color = {"", "\\\\fUf3", "\\\\fTf2",
	// "\\\\f=f=", "\\\\fTf2", "\\\\fVf4", "\\\\fDfD", "\\\\fQfQ", "\\\\fCfC",
	// "\\\\fVfV", "\\\\fFfF"};
	public void NpcShopEtc(L1ShopItem tem) {
		if (tem == null)
			return;

		try {
			NpcBuyShopSell.getInstance().deleteItem(_npcId, tem);
		} catch (Exception e) {
			e.printStackTrace();
		}

		L1NpcShopInstance ns = NpcBuyShop.getInstance().getNpc(_npcId);
		if (ns != null) {
			StringBuilder sb = new StringBuilder();
			FastTable<String> check = new FastTable<String>();
			// sb.append(color[new Random().nextInt(color.length)]);
			if (getSellingItems().size() > 0) {
				for (L1ShopItem d : getSellingItems()) {
					if (!check.contains(d.getMsg() + " ")) {
						ns.setLastName(d.getMsg() + " ");
						sb.append(d.getMsg()).append(" ");
						check.add(d.getMsg() + " ");
					}
				}
			} else {
				sb.append(ns.getLastName());
			}
			sb.append(ns.getDefaultName());
			ns.setShopName(sb.toString());
			if (getSellingItems().size() == 0) {
				ns.deleting = true;
				ns.shopdelete();
			} else {
				if (ns._state == 1)
					Broadcaster.broadcastPacket(ns, new S_DoActionShop(ns.getId(), ActionCodes.ACTION_Shop, ns.getShopName().getBytes()), true);
			}
		}
	}

	private boolean NoTaxEnsureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 2000000000)) {
			pc.sendPackets(new S_ServerMessage(904, "2000000000"), true);
			return false;
		}
		if (_npcId == 5000136) {
			if (orderList.getList().size() > 1) {
				pc.sendPackets(new S_SystemMessage("이동하실 곳을 하나만 선택하세요."), true);
				return false;
			}
			L1ShopBuyOrder item = null;
			if (_npcId == 5000136) {
				try {
					for (int i = 0; i < orderList.getList().size(); i++) {
						item = orderList.getList().get(i);
						if (item.getItem().getItemId() == 5000143) {
							L1Teleport.teleport(pc, 32735, 32771, (short) 55, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000144) {
							L1Teleport.teleport(pc, 33514, 32856, (short) 4, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000145) {

							return false;
						} else if (item.getItem().getItemId() == 5000146) { // 제니스퀸
							L1Teleport.teleport(pc, 32800, 32800, (short) 110, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000147) {
							L1Teleport.teleport(pc, 32800, 32800, (short) 120, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000148) {
							L1Teleport.teleport(pc, 32800, 32800, (short) 130, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000149) {
							L1Teleport.teleport(pc, 32800, 32800, (short) 140, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000150) {
							L1Teleport.teleport(pc, 32796, 32796, (short) 150, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000151) {
							L1Teleport.teleport(pc, 32720, 32821, (short) 160, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000152) {
							L1Teleport.teleport(pc, 32720, 32821, (short) 170, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000153) {
							L1Teleport.teleport(pc, 32724, 32822, (short) 180, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000154) {
							L1Teleport.teleport(pc, 32722, 32827, (short) 190, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000155) {
							L1Teleport.teleport(pc, 32731, 32856, (short) 200, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000156) {
							L1Teleport.teleport(pc, 32726, 32832, (short) 603, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000157) {
							L1Teleport.teleport(pc, 32742, 32856, (short) 543, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000158) {
							L1Teleport.teleport(pc, 33723, 32255, (short) 4, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000159) {
							L1Teleport.teleport(pc, 33385, 32349, (short) 4, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000160) { // 데스나이트
							L1Teleport.teleport(pc, 32799, 32804, (short) 11, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000161) {
							L1Teleport.teleport(pc, 32734, 32895, (short) 522, 5, false);
							return false;
						} else if (item.getItem().getItemId() == 5000162) {
							L1Teleport.teleport(pc, 32904, 32801, (short) 410, 5, false);
							return false;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (!pc.getInventory().checkItem(L1ItemId.ADENA, price)) {
			pc.sendPackets(new S_ServerMessage(189), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void NoTaxSellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(L1ItemId.ADENA, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
		}
		L1ItemInstance item = null;
		Random random = new Random(System.nanoTime());
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			if (L1MerchantInstance.oneDayBuyAdd(inv.getOwner().getAccountName(), getNpcId(), itemId))
				continue;
			item.setCount(amount);
			item.setIdentified(true);
			if (_npcId == 70068 || _npcId == 70020 || _npcId == 70056) {
				item.setIdentified(false);
				int chance = random.nextInt(150) + 1;
				if (chance <= 15) {
					item.setEnchantLevel(-2);
				} else if (chance >= 16 && chance <= 30) {
					item.setEnchantLevel(-1);
				} else if (chance >= 31 && chance <= 89) {
					item.setEnchantLevel(0);
				} else if (chance >= 90 && chance <= 141) {
					item.setEnchantLevel(random.nextInt(2) + 1);
				} else if (chance >= 142 && chance <= 147) {
					item.setEnchantLevel(random.nextInt(3) + 3);
				} else if (chance >= 148 && chance <= 149) {
					item.setEnchantLevel(6);
				} else if (chance == 150) {
					if (random.nextInt(3) == 0)
						item.setEnchantLevel(7);
					else
						item.setEnchantLevel(0);
				}
			}
			if (_npcId == 4200105) {
				int type = item.getItem().getType2();
				if (type == 1) {
					item.setEnchantLevel(7);
				} else if (type == 2) {
					item.setEnchantLevel(5);
				}
			}

			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
		random = null;
	}

	private void AGSellItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(438005, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 아데나를 소비 할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			if (getSellingItems().contains(item)) {
				return;
			}
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
	}

	private boolean AGEnsureSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 2000)) {
			pc.sendPackets(new S_SystemMessage("코인은 한번에 2000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(438005, price)) {
			pc.sendPackets(new S_SystemMessage("코인이 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {

			pc.sendPackets(new S_SystemMessage("소지품이 너무 무거워서 더 들 수 없습니다."), true);
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {

			pc.sendPackets(new S_SystemMessage("한 캐릭터가 들고 다닐 수 있는 아이템의 최대 가짓수는 180개입니다."), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private void sellMarkItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(L1ItemId.TEST_MARK, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 정기를 소비할 수 없습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
	}

	private boolean ensureMarkSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {

		int price = orderList.getTotalPrice();

		if (!pc.getInventory().checkItem(L1ItemId.TEST_MARK, price)) {
			pc.sendPackets(new S_SystemMessage("만월의 정기가 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		int totalCount = pc.getInventory().getSize();
		L1Item temp = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private boolean ensureCashSell1(L1PcInstance pc, L1ShopBuyOrderList orderList, int npcId) {
		int price = orderList.getTotalPrice();
		// 9000001 - 3500000
		if (!pc.getInventory().checkItem(npcId - 760001, price)) {
			pc.sendPackets(new S_SystemMessage("코인이 부족합니다."));
			return false;
		}
		int currentWeight = pc.getInventory().getWeight() * 1000;
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight() * 1000) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82));
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263));
			return false;
		}
		if (price <= 0 || price > 2000000) {
			pc.sendPackets(new S_Disconnect());
			return false;
		}
		return true;
	}

	private void sellCashItems1(L1PcInstance pc, L1PcInventory inv, L1ShopBuyOrderList orderList, int npcId) {
		if (!inv.consumeItem(npcId - 760001, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 1차 코인을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			item.setPackage(true);
			inv.storeItem(item);
		}
	}

	private void sellPremiumItems(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(41159, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 신비한 깃털을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
	}

	private boolean ensurePremiumSell(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 200000)) {
			pc.sendPackets(new S_SystemMessage("픽시의 날개깃털은 한번에 200000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(41159, price)) {
			pc.sendPackets(new S_SystemMessage("픽시의 날개깃털이 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private void 호박씨소비(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(60198, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 할로윈 호박씨를 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
	}

	private boolean 호박씨체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 200000)) {
			pc.sendPackets(new S_SystemMessage("할로윈 호박씨는 한번에 200000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(60198, price)) {
			pc.sendPackets(new S_SystemMessage("할로윈 호박씨가 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private void 마법의알약소비(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(60258, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 마법의알약을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
	}

	private boolean 마법의알약체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 200000)) {
			pc.sendPackets(new S_SystemMessage("마법의 알약은 한번에 200000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(60258, price)) {
			pc.sendPackets(new S_SystemMessage("마법의 알약이 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private void 환전(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(60290, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 환전템을 소비할 수 없었습니다.");
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
	}

	private boolean 환전체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 200000)) {
			pc.sendPackets(new S_SystemMessage("환전템은 한번에 200000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(60290, price)) {
			pc.sendPackets(new S_SystemMessage("환전템이 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private void 특화코인(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(6002491, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 코인이 부족합니다.");
		}
		/*
		 * if(inv.getOwner().getNetConnection().getAccount().berry <
		 * orderList.getTotalPrice()){ throw new
		 * IllegalStateException("구입에 필요한 베리가 부족합니다."); }
		 * inv.getOwner().getNetConnection().getAccount().berry -=
		 * orderList.getTotalPrice();
		 */
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			int enchant = order.getItem().getEnchant();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			item.setEnchantLevel(enchant);
			item.setPackage(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
		/*
		 * try{ inv.getOwner().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
		 * inv.getOwner().getNetConnection().getAccount().berry)); }catch(Exception e){}
		 */
	}

	private void 판도라의증서(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(600249, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 판도라의 증서가 부족합니다.");
		}
		/*
		 * if(inv.getOwner().getNetConnection().getAccount().berry <
		 * orderList.getTotalPrice()){ throw new
		 * IllegalStateException("구입에 필요한 베리가 부족합니다."); }
		 * inv.getOwner().getNetConnection().getAccount().berry -=
		 * orderList.getTotalPrice();
		 */
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
		/*
		 * try{ inv.getOwner().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
		 * inv.getOwner().getNetConnection().getAccount().berry)); }catch(Exception e){}
		 */
	}

	private void 베리(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(60492, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 베리가 부족합니다.");
		}
		/*
		 * if(inv.getOwner().getNetConnection().getAccount().berry <
		 * orderList.getTotalPrice()){ throw new
		 * IllegalStateException("구입에 필요한 베리가 부족합니다."); }
		 * inv.getOwner().getNetConnection().getAccount().berry -=
		 * orderList.getTotalPrice();
		 */
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
		/*
		 * try{ inv.getOwner().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
		 * inv.getOwner().getNetConnection().getAccount().berry)); }catch(Exception e){}
		 */
	}

	private void 가넷(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(600267, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 가넷이 부족합니다.");
		}
		/*
		 * if(inv.getOwner().getNetConnection().getAccount().berry <
		 * orderList.getTotalPrice()){ throw new
		 * IllegalStateException("구입에 필요한 베리가 부족합니다."); }
		 * inv.getOwner().getNetConnection().getAccount().berry -=
		 * orderList.getTotalPrice();
		 */
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
		/*
		 * try{ inv.getOwner().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
		 * inv.getOwner().getNetConnection().getAccount().berry)); }catch(Exception e){}
		 */
	}

	private void 가넷2(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(600272, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 가넷이 부족합니다.");
		}
		/*
		 * if(inv.getOwner().getNetConnection().getAccount().berry <
		 * orderList.getTotalPrice()){ throw new
		 * IllegalStateException("구입에 필요한 베리가 부족합니다."); }
		 * inv.getOwner().getNetConnection().getAccount().berry -=
		 * orderList.getTotalPrice();
		 */
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
		/*
		 * try{ inv.getOwner().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
		 * inv.getOwner().getNetConnection().getAccount().berry)); }catch(Exception e){}
		 */
	}

	/** 기사단의 주화 */
	private void 주화(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (!inv.consumeItem(60716, orderList.getTotalPrice())) {
			throw new IllegalStateException("구입에 필요한 주화가 부족합니다.");
		}
		/*
		 * if(inv.getOwner().getNetConnection().getAccount().berry <
		 * orderList.getTotalPrice()){ throw new
		 * IllegalStateException("구입에 필요한 베리가 부족합니다."); }
		 * inv.getOwner().getNetConnection().getAccount().berry -=
		 * orderList.getTotalPrice();
		 */
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
		/*
		 * try{ inv.getOwner().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
		 * inv.getOwner().getNetConnection().getAccount().berry)); }catch(Exception e){}
		 */
	}

	/** 기사단의 주화 */

	private boolean 판도라증서체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 20000000)) {
			pc.sendPackets(new S_SystemMessage("판도라의 증서는 한번에 20000000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(600249, price)) {
			pc.sendPackets(new S_SystemMessage("판도라의 증서가 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		// System.out.println(currentWeight);
		// System.out.println(orderList.getTotalWeight());
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private boolean 특화코인체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 20000000)) {
			pc.sendPackets(new S_SystemMessage("특화코인은 한번에 20000000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(6002491, price)) {
			pc.sendPackets(new S_SystemMessage("특화코인이 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		// System.out.println(currentWeight);
		// System.out.println(orderList.getTotalWeight());
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private boolean 베리체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 20000000)) {
			pc.sendPackets(new S_SystemMessage("베리는 한번에 20000000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(60492, price)) {
			pc.sendPackets(new S_SystemMessage("베리가 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	/** 기사단의 주화 */
	private boolean 주화체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 20000000)) {
			pc.sendPackets(new S_SystemMessage("주화는 한번에 20000000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(60716, price)) {
			pc.sendPackets(new S_SystemMessage("주화가 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	/** 기사단의 주화 */

	private boolean 가넷2체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 20000000)) {
			pc.sendPackets(new S_SystemMessage("가넷은 한번에 20000000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(600272, price)) {
			pc.sendPackets(new S_SystemMessage("가넷이 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private boolean 가넷체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 20000000)) {
			pc.sendPackets(new S_SystemMessage("가넷은 한번에 20000000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (!pc.getInventory().checkItem(600267, price)) {
			pc.sendPackets(new S_SystemMessage("가넷이 부족합니다."), true);
			return false;
		}
		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 2000000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	private void TAM(L1PcInventory inv, L1ShopBuyOrderList orderList) {
		if (inv.getOwner().getNetConnection().getAccount().tam_point < orderList.getTotalPrice()) {
			throw new IllegalStateException("구입에 필요한 탐이 부족합니다.");
		}
		inv.getOwner().getNetConnection().getAccount().tam_point -= orderList.getTotalPrice();
		try {
			inv.getOwner().getNetConnection().getAccount().updateTam();
		} catch (Exception e) {
		}
		L1ItemInstance item = null;
		for (L1ShopBuyOrder order : orderList.getList()) {
			int itemId = order.getItem().getItemId();
			int amount = order.getCount();
			item = ItemTable.getInstance().createItem(itemId);
			item.setCount(amount);
			item.setIdentified(true);
			inv.storeItem(item);
			LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, order.getItem().getPrice(), amount, 0);
			LinAllManager.getInstance().ShopAppend(item.getLogName(), amount, (amount * order.get_price()), String.valueOf(getNpcId()), inv.getOwner().getName());
		}
		try {
			inv.getOwner().sendPackets(new S_NewCreateItem(S_NewCreateItem.TAM_POINT, inv.getOwner().getNetConnection()), true);
		} catch (Exception e) {
		}
	}

	private boolean TAM체크(L1PcInstance pc, L1ShopBuyOrderList orderList) {
		int price = orderList.getTotalPrice();
		if (!IntRange.includes(price, 0, 20000000)) {
			pc.sendPackets(new S_SystemMessage("탐은 한번에 20000000개 이상 사용할수 없습니다."), true);
			return false;
		}
		if (pc.getNetConnection().getAccount().tam_point < orderList.getTotalPrice()) {
			pc.sendPackets(new S_SystemMessage("탐이 부족합니다."), true);
			return false;
		}

		int currentWeight = pc.getInventory().getWeight();
		if (currentWeight + orderList.getTotalWeight() > pc.getMaxWeight()) {
			// 아이템이 너무 무거워, 더 이상 가질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}
		// 개수 체크
		int totalCount = pc.getInventory().getSize();
		for (L1ShopBuyOrder order : orderList.getList()) {
			L1Item temp = order.getItem().getItem();
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(temp.getItemId())) {
					totalCount += 1;
				}
			} else {
				totalCount += 1;
			}
		}
		if (totalCount > 180) {
			// \f1한사람의 캐릭터가 가지고 걸을 수 있는 아이템은 최대 180개까지입니다.
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (price <= 0 || price > 20000000) {
			pc.sendPackets(new S_Disconnect(), true);
			return false;
		}
		return true;
	}

	/*
	 * private boolean ensureCoinSell(L1PcInstance pc, L1ShopBuyOrderList orderList)
	 * { int price = orderList.getTotalPrice(); // 오버플로우 체크 if
	 * (!IntRange.includes(price, 0, 1000)) { // 총판 매가격은 Play Coin 1000개를 초과할 수
	 * 없습니다. pc.sendPackets(new S_SystemMessage("코인은 한번에 1000개 이상 사용할수 없습니다."),
	 * true); return false; } // 구입할 수 있을까 체크 if
	 * (!pc.getInventory().checkItem(438005, price)) { //System.out.println(price);
	 * // \f1아데나가 부족합니다. pc.sendPackets(new S_SystemMessage("코인이 부족합니다."), true);
	 * 
	 * return false; }
	 * 
	 * int currentWeight = pc.getInventory().getWeight() * 1000; if (currentWeight +
	 * orderList.getTotalWeight() > pc.getMaxWeight() * 1000) { pc.sendPackets(new
	 * S_ServerMessage(82), true); return false; } int totalCount =
	 * pc.getInventory().getSize(); L1Item temp = null; for (L1ShopBuyOrder order :
	 * orderList.getList()) { temp = order.getItem().getItem(); if
	 * (temp.isStackable()) { if (!pc.getInventory().checkItem(temp.getItemId())) {
	 * totalCount += 1; } } else { totalCount += 1; } } if (totalCount > 180) {
	 * pc.sendPackets(new S_ServerMessage(263)); return false; } if (price <= 0 ||
	 * price > 2000000000) { pc.sendPackets(new S_Disconnect(), true); return false;
	 * } return true; }
	 */

	/*
	 * private void payTax(L1ShopBuyOrderList orderList) { payCastleTax(orderList);
	 * payTownTax(orderList); payDiadTax(orderList); }
	 */

	/*
	 * private void payCastleTax(L1ShopBuyOrderList orderList) { L1TaxCalculator
	 * calc = orderList.getTaxCalculator(); int price = orderList.getTotalPrice();
	 * int castleId = L1CastleLocation.getCastleIdByNpcid(_npcId); int castleTax =
	 * calc.calcCastleTaxPrice(price); int nationalTax =
	 * calc.calcNationalTaxPrice(price);
	 * 
	 * if (castleId == L1CastleLocation.ADEN_CASTLE_ID || castleId ==
	 * L1CastleLocation.DIAD_CASTLE_ID) { castleTax += nationalTax; nationalTax = 0;
	 * }
	 * 
	 * if (castleId != 0 && castleTax > 0) { L1Castle castle =
	 * CastleTable.getInstance().getCastleTable(castleId); synchronized (castle) {
	 * int money = castle.getPublicReadyMoney(); if (2000000000 > money) { money +=
	 * castleTax; castle.setPublicReadyMoney(money);
	 * CastleTable.getInstance().updateCastle(castle); } } if (nationalTax > 0) {
	 * L1Castle aden = CastleTable.getInstance().getCastleTable(L1CastleLocation
	 * .ADEN_CASTLE_ID); synchronized (aden) { int money =
	 * aden.getPublicReadyMoney(); if (2000000000 > money) { money += nationalTax;
	 * aden.setPublicReadyMoney(money);
	 * CastleTable.getInstance().updateCastle(aden); } } } } }
	 */
	private void payCastleTax(L1ShopBuyOrderList orderList, int castleId) {
		L1TaxCalculator calc = orderList.getTaxCalculator();
		int price = orderList.getTotalPrice();
		int castleTax = calc.calcCastleTaxPrice(price);
		int nationalTax = calc.calcNationalTaxPrice(price);

		if (castleId == L1CastleLocation.ADEN_CASTLE_ID || castleId == L1CastleLocation.DIAD_CASTLE_ID) {
			castleTax += nationalTax;
			nationalTax = 0;
		}
		if (castleId != 0 && castleTax > 0) {
			L1Castle castle = CastleTable.getInstance().getCastleTable(castleId);
			/*
			 * if(castleId == 4){ castleTax *= 10; }else if(castleId == 1){ castleTax *= 20;
			 * }
			 */
			synchronized (castle) {
				int money = castle.getPublicReadyMoney();
				if (2000000000 > money) {
					money += castleTax;
					castle.setPublicReadyMoney(money);
					CastleTable.getInstance().updateCastle(castle);
				}
			}
			if (nationalTax > 0) {
				L1Castle aden = CastleTable.getInstance().getCastleTable(L1CastleLocation.ADEN_CASTLE_ID);
				synchronized (aden) {
					int money = aden.getPublicReadyMoney();
					if (2000000000 > money) {
						money += nationalTax;
						aden.setPublicReadyMoney(money);
						CastleTable.getInstance().updateCastle(aden);
					}
				}
			}
		}
	}

	/*
	 * private void payDiadTax(L1ShopBuyOrderList orderList) { L1Castle castle =
	 * CastleTable .getInstance().getCastleTable(L1CastleLocation.DIAD_CASTLE_ID);
	 * L1TaxCalculator calc = orderList.getTaxCalculator(); int price =
	 * orderList.getTotalPrice(); int diadTax = calc.calcDiadTaxPrice(price);
	 * 
	 * if (diadTax <= 0) { return; } synchronized (castle) { int money =
	 * castle.getPublicReadyMoney(); if (2000000000 > money) { money = money +
	 * diadTax; castle.setPublicReadyMoney(money);
	 * CastleTable.getInstance().updateCastle(castle); } } }
	 */

	/*
	 * private void payTownTax(L1ShopBuyOrderList orderList) { int price =
	 * orderList.getTotalPrice(); if
	 * (!L1World.getInstance().isProcessingContributionTotal()) { int town_id =
	 * L1TownLocation.getTownIdByNpcid(_npcId); if (town_id >= 1 && town_id <= 10) {
	 * TownTable.getInstance().addSalesMoney(town_id, price); } } }
	 */

	public void buyItems(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		L1NpcInstance shopNpc = null;
		FastTable<L1PcInstance> gmList = null;
		if (GMCommands.sellShopNotice) {
			gmList = new FastTable<L1PcInstance>();
			for (L1PcInstance gm : L1World.getInstance().getAllPlayersToArray()) {
				if (gm != null && gm.isGm())
					gmList.add(gm);
			}
			for (L1Object obj : orderList.getPc().getNearObjects().getKnownObjects()) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcId() == _npcId) {
						shopNpc = npc;
						break;
					}
				}
			}
		}
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (GMCommands.sellShopNotice && gmList != null && gmList.size() > 0) {
				try {
					for (L1PcInstance gm : gmList) {
						gm.sendPackets(new S_SystemMessage(gm, "유저: [" + orderList.getPc().getName() + "] 상점: [" + shopNpc.getNpcId() + "] 판매 아이템: ["
								+ item.getName() + "] 수량: [" + order.getCount() + "]"), true);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;

				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}

		L1ItemInstance itemExist = inv.findItemId(40308);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}

		totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
		if (0 < totalPrice) {
			inv.storeItem(L1ItemId.ADENA, totalPrice);
			// 버경 엔피씨 제외
			if (getNpcId() != 70035 && getNpcId() != 70041 && getNpcId() != 70042)
				LogTable.상점아덴(orderList.getPc(), totalPrice);
		}
		if (gmList != null && gmList.size() > 0)
			gmList.clear();
		gmList = null;
	}

	public void buyItems베리(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}
		L1ItemInstance itemExist = inv.findItemId(40308);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}
		if (0 < totalPrice) {
			inv.storeItem(60492, totalPrice);
			/*
			 * orderList.getPc().getNetConnection().getAccount().berry += totalPrice; try{
			 * orderList.getPc().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
			 * orderList.getPc().getNetConnection().getAccount().berry)); }catch(Exception
			 * e){}
			 */
		}
	}

	public void buyItems주화(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}
		L1ItemInstance itemExist = inv.findItemId(60716);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 주화가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}
		if (0 < totalPrice) {
			inv.storeItem(60716, totalPrice);
			/*
			 * orderList.getPc().getNetConnection().getAccount().berry += totalPrice; try{
			 * orderList.getPc().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
			 * orderList.getPc().getNetConnection().getAccount().berry)); }catch(Exception
			 * e){}
			 */
		}
	}

	public void buyItems가넷(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}
		L1ItemInstance itemExist = inv.findItemId(600267);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}
		if (0 < totalPrice) {
			inv.storeItem(600267, totalPrice);
			/*
			 * orderList.getPc().getNetConnection().getAccount().berry += totalPrice; try{
			 * orderList.getPc().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
			 * orderList.getPc().getNetConnection().getAccount().berry)); }catch(Exception
			 * e){}
			 */
		}
	}

	public void buyItems가넷2(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}
		L1ItemInstance itemExist = inv.findItemId(600272);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}
		if (0 < totalPrice) {
			inv.storeItem(600272, totalPrice);
			/*
			 * orderList.getPc().getNetConnection().getAccount().berry += totalPrice; try{
			 * orderList.getPc().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
			 * orderList.getPc().getNetConnection().getAccount().berry)); }catch(Exception
			 * e){}
			 */
		}
	}

	public void buyItems특화코인(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}
		L1ItemInstance itemExist = inv.findItemId(40308);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}
		if (0 < totalPrice) {
			inv.storeItem(6002491, totalPrice);
			/*
			 * orderList.getPc().getNetConnection().getAccount().berry += totalPrice; try{
			 * orderList.getPc().sendPackets(new S_PacketBox(S_PacketBox.낚시_베리,
			 * orderList.getPc().getNetConnection().getAccount().berry)); }catch(Exception
			 * e){}
			 */
		}
	}

	public void buyItems환전(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}
		L1ItemInstance itemExist = inv.findItemId(40308);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}
		totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
		if (0 < totalPrice) {
			inv.storeItem(60290, totalPrice);
		}
	}

	public void buyItemsFoodHub(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}
		L1ItemInstance itemExist = inv.findItemId(40308);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}
		totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
		if (0 < totalPrice) {
			inv.storeItem(436000, totalPrice);
		}
	}

	public void buyItemsFoodSauce(L1ShopSellOrderList orderList) {
		L1PcInventory inv = orderList.getPc().getInventory();
		int totalPrice = 0;
		L1Object object = null;
		L1ItemInstance item = null;
		for (L1ShopSellOrder order : orderList.getList()) {
			object = inv.getItem(order.getItem().getTargetId());
			item = (L1ItemInstance) object;
			if (item.getItem().getBless() < 128) {
				int count = inv.removeItem(order.getItem().getTargetId(), order.getCount());
				totalPrice += order.getItem().getAssessedPrice() * order.getDividend() * count;
				LogTable.lognpcshop(inv.getOwner(), getNpcId(), item, (int) (order.getItem().getAssessedPrice() * order.getDividend()), count, 1);
			}
		}
		L1ItemInstance itemExist = inv.findItemId(40308);
		if (itemExist != null && (itemExist.getCount() + totalPrice) > 2000000000) {
			inv.getOwner().sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다.")); // \f1%0이%4%1%3%2
			return;
		}
		totalPrice = IntRange.ensure(totalPrice, 0, 2000000000);
		if (0 < totalPrice) {
			inv.storeItem(49048, totalPrice);
		}
	}

	private L1ShopItem getPurchasingItem(int itemId) {
		for (L1ShopItem shopItem : _purchasingItems) {
			if (shopItem.getItemId() == itemId) {
				return shopItem;
			}
		}
		return null;
	}

	private boolean isPurchaseableItem(L1ItemInstance item) {
		if (item == null || item.isEquipped() || item.getEnchantLevel() != 0 || item.getBless() >= 128) {
			return false;
		}
		return true;
	}

	public L1AssessedItem assessItem(L1ItemInstance item) {
		L1ShopItem shopItem = getPurchasingItem(item.getItemId());
		if (shopItem == null) {
			return null;
		}

		return new L1AssessedItem(item.getId(), getAssessedPrice(shopItem));
	}

	private int getAssessedPrice(L1ShopItem item) {
		return (int) (item.getPrice() * Config.RATE_SHOP_PURCHASING_PRICE / item.getPackCount());
	}

	public List<L1AssessedItem> assessItems(L1PcInventory inv) {
		List<L1AssessedItem> result = new ArrayList<L1AssessedItem>();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}
				result.add(new L1AssessedItem(targetItem.getId(), getAssessedPrice(item)));
			}
		}
		return result;
	}

	public List<L1AssessedItem> assessTickets(L1PcInventory inv) {
		List<L1AssessedItem> result = new ArrayList<L1AssessedItem>();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}
				if (L1BugBearRace.getInstance().bugRound() == targetItem.getRoundId()) {
					continue;
				}
				double dividend = L1BugBearRace.getInstance().getTicketPrice(targetItem);
				result.add(new L1AssessedItem(targetItem.getId(), (int) (getAssessedPrice(item) * dividend)));
			}
		}
		return result;
	}

	public List<L1AssessedItem> assessTicketsBugBearRace(L1PcInventory inv) {
		List<L1AssessedItem> result = new ArrayList<L1AssessedItem>();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}
				if (L1BugBearRace.getInstance().getRoundId() == targetItem.getSecondId()
						&& L1BugBearRace.getInstance().getBugRaceStatus() != L1BugBearRace.STATUS_NONE) {
					continue;
				}
				double dividend = L1BugBearRace.getInstance().getTicketPrice(targetItem);
				result.add(new L1AssessedItem(targetItem.getId(), (int) (getAssessedPrice(item) * dividend)));
			}
		}
		return result;
	}

	public List<L1AssessedItem> assessTicketsDogFight(L1PcInventory inv) {
		List<L1AssessedItem> result = new ArrayList<L1AssessedItem>();
		for (L1ShopItem item : _purchasingItems) {
			for (L1ItemInstance targetItem : inv.findItemsId(item.getItemId())) {
				if (!isPurchaseableItem(targetItem)) {
					continue;
				}
				if (DogFight.getInstance().getRoundId() == targetItem.getSecondId() && DogFight.getInstance().getBugRaceStatus() != DogFight.STATUS_NONE) {
					continue;
				}
				double dividend = DogFight.getInstance().getTicketPrice(targetItem);
				result.add(new L1AssessedItem(targetItem.getId(), (int) (getAssessedPrice(item) * dividend)));
			}
		}
		return result;
	}

	public int getNpcId() {
		return _npcId;
	}

	public List<L1ShopItem> getSellingItems() {
		return _sellingItems;
	}

	public List<L1ShopItem> getPurchasingItems() {
		return _purchasingItems;
	}

	public L1ShopBuyOrderList newBuyOrderList() {
		return new L1ShopBuyOrderList(this);
	}

	public L1ShopSellOrderList newSellOrderList(L1PcInstance pc) {
		return new L1ShopSellOrderList(this, pc);
	}

}