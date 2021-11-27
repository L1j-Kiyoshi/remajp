package l1j.server.GameSystem.NpcBuyShop;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

public class NpcBuyShop {

	private FastTable<L1NpcShopInstance> shoplist;
	private FastTable<L1ShopItem> itemlist;
	private Random _rnd = new Random(System.nanoTime());

	private FastMap<Integer, L1Shop> _npcShops = new FastMap<Integer, L1Shop>();

	private static NpcBuyShop _instance;

	public static NpcBuyShop getInstance() {
		if (_instance == null) {
			_instance = new NpcBuyShop();
		}
		return _instance;
	}

	public void relodingac() {
		L1Shop shop = null;
		L1NpcShopInstance npc = null;
		int npcid = 0;
		for (FastMap.Entry<Integer, L1Shop> e = _npcShops.head(), mapEnd = _npcShops
				.tail(); (e = e.getNext()) != mapEnd;) {
			try {
				shop = e.getValue();
				if (shop.getNpcId() >= 8110000 && shop.getNpcId() <= 8110003)
					continue;
				npcid = shop.getNpcId();
				npc = L1World.getInstance().getNpcShop(npcid);
				if (npc == null) {
					continue;
				}
				NpcBuyShop.getInstance().상점아이템삭제(npcid);
				// shop = NpcShopTable.getInstance().get(npcid);
				// if(shop )
				if (shop.getSellingItems().size() > 0) {
					boolean ckck = false;
					for (L1ShopItem shopitem : shop.getSellingItems()) {
						ckck = 상점템체크(shopitem.getId());
						if (!ckck) {
							NpcBuyShop.getInstance().SaveShop(npc, shopitem,
									shopitem.getPrice(), shopitem.getCount());
						} else {
							// System.out.println("확인해야할 시장상점 =>> "+npc.getName());
						}
					}
				}

			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void reload() {
		try {
			itemlist = NpcBuyShopPrice.getInstance().load();

			synchronized (_npcShops) {
				for (FastMap.Entry<Integer, L1Shop> e = _npcShops.head(), mapEnd = _npcShops
						.tail(); (e = e.getNext()) != mapEnd;) {
					L1Shop shop = e.getValue();
					if (shop.getNpcId() >= 8110000
							&& shop.getNpcId() <= 8110003)
						continue;
					L1ShopItem[] list = (L1ShopItem[]) shop.getSellingItems()
							.toArray(
									new L1ShopItem[shop.getSellingItems()
											.size()]);
					for (L1ShopItem item : list) {
						if (item == null)
							continue;
						L1ShopItem te = getItem(item.getItemId(),
								item.getEnchant(), item.getAttr(),
								item.getBless());

						if (te == null) {
							shop.getSellingItems().remove(item);
							continue;
						}
						te.setDeleteTime(item.getDeleteTime());
						try {
							if (!NpcBuyShopSell.getInstance().serchItem(
									shop.getNpcId(), te)) {
								shop.getSellingItems().remove(item);
								continue;
							}
						} catch (Exception e1) {
							e1.printStackTrace();
						}
						item.setPrice(te.getPrice());
					}
					list = null;
					L1NpcShopInstance ns = getNpc(shop.getNpcId());
					StringBuilder sb = new StringBuilder();
					FastTable<String> check = new FastTable<String>();
					// sb.append(color[_rnd.nextInt(color.length)]);
					if (shop.getSellingItems().size() > 0) {
						for (L1ShopItem d : shop.getSellingItems()) {
							String att = "";
							switch (d.getAttr()) {
							case 1:
								att = "화령1단";
								break;
							case 2:
								att = "화령2단";
								break;
							case 3:
								att = "화령3단";
								break;
							case 4:
								att = "수령1단";
								break;
							case 5:
								att = "수령2단";
								break;
							case 6:
								att = "수령3단";
								break;
							case 7:
								att = "풍령1단";
								break;
							case 8:
								att = "풍령2단";
								break;
							case 9:
								att = "풍령3단";
								break;
							case 10:
								att = "지령1단";
								break;
							case 11:
								att = "지령2단";
								break;
							case 12:
								att = "지령3단";
								break;
							case 33:
								att = "화령4단";
								break;
							case 34:
								att = "화령5단";
								break;
							case 35:
								att = "수령4단";
								break;
							case 36:
								att = "수령5단";
								break;
							case 37:
								att = "풍령4단";
								break;
							case 38:
								att = "풍령5단";
								break;
							case 39:
								att = "지령4단";
								break;
							case 40:
								att = "지령5단";
								break;
							default:
								break;
							}
							if (!check.contains(att + d.getMsg() + " ")) {
								sb.append(att + d.getMsg()).append(" ");
								ns.setLastName(att + d.getMsg() + " ");
								check.add(att + d.getMsg() + " ");
							}
						}
					}

					sb.append(ns.getDefaultName());
					ns.setShopName(sb.toString());
					if (shop.getSellingItems().size() == 0) {
						ns.shopdelete();

					} else {
						if (ns._state == 1) {
							S_DoActionShop das = new S_DoActionShop(ns.getId(),
									ActionCodes.ACTION_Shop, ns.getShopName()
											.getBytes());
							Broadcaster.broadcastPacket(ns, das);
							das = null;
						}
					}
					sb = null;
					if (check.size() > 0)
						check.clear();
					check = null;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private NpcBuyShop() {
		NpcChatThread.get();
		/** NPC 스폰 **/
		shoplist = NpcBuyShopSpawn.getInstance().load();
		/** 아이템 가격 셋팅 **/
		itemlist = NpcBuyShopPrice.getInstance().load();
	//	System.out.println(":: NpcBuyShop Loading Compleate");
	}

	public boolean 상점템체크(int objid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT COUNT(*) FROM shop_npc WHERE item_objid=?");
			pstm.setInt(1, objid);
			rs = pstm.executeQuery();
			if (rs.next()) {
				return rs.getInt(1) > 0;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return false;
	}

	public void 상점아이템삭제(int npcid, int objid) {// 아이템아이디별 판매 구매 구분후 삭제
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("DELETE FROM shop_npc WHERE npc_id=? AND item_objid=?");
			pstm.setInt(1, npcid);
			pstm.setInt(2, objid);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void 상점아이템업데이트(int npcid, int objid, int count1) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("UPDATE shop_npc SET count=? WHERE npc_id=? AND item_objid=?");
			pstm.setInt(1, count1);
			pstm.setInt(2, npcid);
			pstm.setInt(3, objid);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void 상점아이템삭제(int npcid) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM shop_npc WHERE npc_id=?");
			pstm.setInt(1, npcid);
			pstm.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public void SaveShop(L1NpcInstance npc, L1ShopItem item, int price,
			int sellcount) {
		Connection con = null;

		int bless = item.getBless();
		int attr = item.getAttr();
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("INSERT INTO shop_npc SET npc_id=?, npc_name=?, id=?,  item_id=?, Item_name=?, count=?, enchant=?, selling_price=?, locx=?, locy=?, locm=?, iden=?, attr=?, item_objid=?");
			pstm.setInt(1, npc.getNpcId());
			pstm.setString(2, npc.getName());
			pstm.setInt(3, npc.getId());
			pstm.setInt(4, item.getItemId());
			pstm.setString(5, item.getItem().getName());

			pstm.setInt(6, sellcount);
			pstm.setInt(7, item.getEnchant());
			pstm.setInt(8, price);

			pstm.setInt(9, npc.getX());
			pstm.setInt(10, npc.getY());
			pstm.setInt(11, npc.getMapId());
			/*
			 * 0 = 미확 1 = 확인보통 2 = 축 3 = 저주
			 */
			/*
			 * if (!item.isIdentified()) { pstm.setInt(13, 0); }else{
			 */
			switch (bless) {
			case 0:
				pstm.setInt(12, 2);
				break;// 축
			case 1:
				pstm.setInt(12, 1);
				break;// 보통
			case 2:
				pstm.setInt(12, 3);
				break;// 저주
			}
			// }
			pstm.setInt(13, attr);
			/*
			 * if (item.isIdentified()) { pstm.setInt(13, 1); }else{
			 * pstm.setInt(13, 0); }
			 */
			pstm.setInt(14, item.getId());
			pstm.executeUpdate();
		} catch (SQLException e) {
			System.out.println("중복 objid -> " + npc.getName() + " >>>> [" + item.getItem().getName() + "] 아이디 : " + item.getId());
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("중복 objid -> " + npc.getName() + " >>>> [" + item.getItem().getName() + "] 아이디 : " + item.getId());
			e.printStackTrace();
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	public synchronized L1ShopItem getItem(int id, int enchant, int attr,
			int bles) {
		for (L1ShopItem item : itemlist) {
			if (item.getNpcId() == 0 && item.getItemId() == id
					&& item.getEnchant() == enchant && item.getAttr() == attr
					&& item.getBless() == bles) {
				int objid = ObjectIdFactory.getInstance().nextId();
				// System.out.println(item.getItem().getName()+" 의 아이디 :"+objid);
				L1ShopItem tempItem = new L1ShopItem(item.getItemId(),
						item.getPrice(), item.getBuyPrice(),
						item.getPackCount(), item.getEnchant(), item.getAttr(),
						item.getMsg(), item.getBless(), objid);
				return tempItem;
			}
		}
		return null;
	}

	public synchronized L1ShopItem getItem(int npcid, int id, int enchant,
			int attr, int bles) {
		for (L1ShopItem item : itemlist) {
			if (item.getNpcId() == npcid && item.getItemId() == id
					&& item.getEnchant() == enchant && item.getAttr() == attr
					&& item.getBless() == bles) {
				int objid = ObjectIdFactory.getInstance().nextId();
				L1ShopItem tempItem = new L1ShopItem(npcid, item.getItemId(),
						item.getPrice(), item.getBuyPrice(),
						item.getPackCount(), item.getEnchant(), item.getAttr(),
						item.getMsg(), item.getBless(), objid);
				return tempItem;
			}
		}
		return null;
	}

	public L1Shop get(int npcId) {
		return _npcShops.get(npcId);
	}

	// 실시간 매입
	public synchronized void insert(int itemid, int enchant, int attr,
			Timestamp time, int bless) {
		L1NpcShopInstance ns = null;
		int count = 30;
		boolean ck = true;
		while (count-- > 1) {
			ns = shoplist.get(_rnd.nextInt(shoplist.size()));
			if (ns == null)
				continue;
			if (ns.getNpcId() >= 8110000 && ns.getNpcId() <= 8111000)
				continue;
			if (ns.deleting)
				continue;
			if (ns.getDefaultName() != null && ns.getShopName() != null
					&& !ns.getShopName().equalsIgnoreCase("")) {
				L1Shop s = get(ns.getNpcTemplate().get_npcId());
				if (s == null || s.getSellingItems().size() < 5) {// 무조건 한개씩만
																	// 팔도록 변경.
					ck = false;
					break;
				}
			}
		}
		if (ck || ns == null) {
			S_SystemMessage sm = new S_SystemMessage("> 매입을 통한 판매 상점이 없거나 부족합니다.");
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc.isGm()) {
					pc.sendPackets(sm);
				}
			}
			sm = null;
			System.out.println("> 매입을 통한 판매 상점이 없거나 부족합니다.");
			return;
		}
		int npcId = ns.getNpcTemplate().get_npcId();
		L1Shop shop = get(npcId);
		L1ShopItem item = getItem(itemid, enchant, attr, bless);
		if (item == null) {
			System.out.println(":: 매입 상점 로딩중 가격 없는 아이템 >> " + itemid);
			return;
		}
		if (time != null)
			item.setDeleteTime(time);
		if (shop == null) {
			if (0 == item.getPrice())
				return;
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
			List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
			sellingList.add(item);
			_npcShops
					.put(npcId, new L1Shop(npcId, sellingList, purchasingList));
		} else {
			shop.getSellingItems().add(item);
		}
		StringBuilder sb = new StringBuilder();
		FastTable<String> check = new FastTable<String>();
		if (shop == null)
			shop = get(npcId);
		// String[] color = {"", "\\\\fUf3", "\\\\fTf2", "\\\\f=f=",
		// "\\\\fVf4"};
		// sb.append(color[_rnd.nextInt(color.length)]);

		for (L1ShopItem i : shop.getSellingItems()) {
			String att = "";
			switch (i.getAttr()) {
			case 1:
				att = "화령1단";
				break;
			case 2:
				att = "화령2단";
				break;
			case 3:
				att = "화령3단";
				break;
			case 4:
				att = "수령1단";
				break;
			case 5:
				att = "수령2단";
				break;
			case 6:
				att = "수령3단";
				break;
			case 7:
				att = "풍령1단";
				break;
			case 8:
				att = "풍령2단";
				break;
			case 9:
				att = "풍령3단";
				break;
			case 10:
				att = "지령1단";
				break;
			case 11:
				att = "지령2단";
				break;
			case 12:
				att = "지령3단";
				break;
			case 33:
				att = "화령4단";
				break;
			case 34:
				att = "화령5단";
				break;
			case 35:
				att = "수령4단";
				break;
			case 36:
				att = "수령5단";
				break;
			case 37:
				att = "풍령4단";
				break;
			case 38:
				att = "풍령5단";
				break;
			case 39:
				att = "지령4단";
				break;
			case 40:
				att = "지령5단";
				break;
			default:
				break;
			}
			if (!check.contains(att + i.getMsg() + " ")) {
				sb.append(att + i.getMsg()).append(" ");
				ns.setLastName(att + i.getMsg() + " ");
				check.add(att + i.getMsg() + " ");
			}
		}
		sb.append(ns.getDefaultName());
		ns.setShopName(sb.toString());

		if (shop.getSellingItems().size() == 1) {
			// XYSetting(ns);
			// L1World.getInstance().storeObject(ns);
			// L1World.getInstance().addVisibleObject(ns);
			GeneralThreadPool.getInstance().schedule(new show(ns),
					_rnd.nextInt(2000) + 100);
		} else {
			if (ns._state == 1) {
				S_DoActionShop das = new S_DoActionShop(ns.getId(),
						ActionCodes.ACTION_Shop, ns.getShopName().getBytes());
				Broadcaster.broadcastPacket(ns, das);
				das = null;
				boolean ckck = false;
				for (L1ShopItem i : shop.getSellingItems()) {
					ckck = 상점템체크(i.getId());
					if (!ckck) {
						SaveShop(ns, i, i.getPrice(), i.getCount());
					} else {
						// System.out.println("[1] 확인해야할 시장상점 =>> "+ns.getName()+" item "+i.getItem().getItemId());
					}
				}
			}
		}
		try {
			NpcBuyShopSell.getInstance().storeItem(npcId, ns.getName(), item);
		} catch (Exception e) {
			e.printStackTrace();
		}
		sb = null;
		if (check.size() > 0)
			check.clear();
		check = null;
		// color = null;
	}

	// private static final String[] color = {"", "\\\\fUf3", "\\\\fTf2",
	// "\\\\f=f=", "\\\\fTf2", "\\\\fVf4", "\\\\fDfD", "\\\\fQfQ", "\\\\fCfC",
	// "\\\\fVfV", "\\\\fFfF"};
	// private static final String[] color = {"", "\\\\fUf3", "\\\\fTf2",
	// "\\\\f=f=", "\\\\fVf4"};
	// Load Setting
	public synchronized void add(int npcId, int itemid, int enchant, int attr,
			Timestamp time, int bless) {
		L1Shop shop = get(npcId);

		L1ShopItem item = getItem(itemid, enchant, attr, bless);

		if (item == null) {
			System.out.println(":: 매입 상점 로딩중 가격 없는 아이템 >> " + itemid);
			return;
		}
		if (time != null) {
			item.setDeleteTime(time);
		}
		if (shop == null) {
			if (0 == item.getPrice())
				return;
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
			List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
			sellingList.add(item);
			_npcShops
					.put(npcId, new L1Shop(npcId, sellingList, purchasingList));

		} else {
			shop.getSellingItems().add(item);
		}
		L1NpcShopInstance ns = getNpc(npcId);
		if (ns == null)
			return;
		StringBuilder sb = new StringBuilder();
		FastTable<String> check = new FastTable<String>();
		if (shop == null)
			shop = get(npcId);
		// sb.append(color[_rnd.nextInt(color.length)]);
		for (L1ShopItem i : shop.getSellingItems()) {
			String att = "";
			switch (i.getAttr()) {
			case 1:
				att = "화령1단";
				break;
			case 2:
				att = "화령2단";
				break;
			case 3:
				att = "화령3단";
				break;
			case 4:
				att = "수령1단";
				break;
			case 5:
				att = "수령2단";
				break;
			case 6:
				att = "수령3단";
				break;
			case 7:
				att = "풍령1단";
				break;
			case 8:
				att = "풍령2단";
				break;
			case 9:
				att = "풍령3단";
				break;
			case 10:
				att = "지령1단";
				break;
			case 11:
				att = "지령2단";
				break;
			case 12:
				att = "지령3단";
				break;
			case 33:
				att = "화령4단";
				break;
			case 34:
				att = "화령5단";
				break;
			case 35:
				att = "수령4단";
				break;
			case 36:
				att = "수령5단";
				break;
			case 37:
				att = "풍령4단";
				break;
			case 38:
				att = "풍령5단";
				break;
			case 39:
				att = "지령4단";
				break;
			case 40:
				att = "지령5단";
				break;
			default:
				break;
			}
			if (!check.contains(att + i.getMsg() + " ")) {
				sb.append(att + i.getMsg()).append(" ");
				ns.setLastName(att + i.getMsg() + " ");
				check.add(att + i.getMsg() + " ");
			}
			/*
			 * if(!check.contains(i.getMsg()+" ")){
			 * sb.append(i.getMsg()).append(" "); check.add(i.getMsg()+" "); }
			 */
		}
		sb.append(ns.getDefaultName());
		boolean equalCK = false;
		if (ns.getShopName().equalsIgnoreCase(sb.toString()))
			equalCK = true;
		ns.setShopName(sb.toString());
		if (shop.getSellingItems().size() == 1) {
			// XYSetting(ns);
			// L1World.getInstance().storeObject(ns);
			// L1World.getInstance().addVisibleObject(ns);
			GeneralThreadPool.getInstance().schedule(new show(ns),
					_rnd.nextInt(2000) + 100);
		} else {
			if (ns._state == 1 && !equalCK) {
				S_DoActionShop das = new S_DoActionShop(ns.getId(),
						ActionCodes.ACTION_Shop, ns.getShopName().getBytes());
				Broadcaster.broadcastPacket(ns, das);
				das = null;
				/*
				 * for(L1ShopItem i : shop.getSellingItems()){
				 * System.out.println("id : "+
				 * i.getItemId()+" name : "+i.getItem().getName()); SaveShop(ns,
				 * i, i.getPrice(), i.getCount()); }
				 */
			}
		}
		sb = null;
		if (check.size() > 0)
			check.clear();
		check = null;
	}

	public synchronized void add_2(int npcId, int itemid, int enchant,
			int attr, Timestamp time, int bless) {
		L1Shop shop = get(npcId);

		L1ShopItem item = getItem(itemid, enchant, attr, bless);
		if (item == null) {
			System.out.println(":: 매입 상점 로딩중 가격 없는 아이템 >> " + itemid);
			return;
		}
		if (time != null) {
			item.setDeleteTime(time);
		}
		if (shop == null) {
			if (0 == item.getPrice())
				return;
			List<L1ShopItem> sellingList = new ArrayList<L1ShopItem>();
			List<L1ShopItem> purchasingList = new ArrayList<L1ShopItem>();
			sellingList.add(item);
			_npcShops
					.put(npcId, new L1Shop(npcId, sellingList, purchasingList));
		} else {
			shop.getSellingItems().add(item);
		}
		L1NpcShopInstance ns = getNpc(npcId);
		if (ns == null)
			return;
		StringBuilder sb = new StringBuilder();
		if (shop == null)
			shop = get(npcId);
		sb.append(ns.getDefaultName());
		ns.setShopName(sb.toString());
		if (shop.getSellingItems().size() == 1) {
			// XYSetting(ns);
			ns.setMap((short) 800);
			L1World.getInstance().storeObject(ns);
			L1World.getInstance().addVisibleObject(ns);
		}
		sb = null;
	}

	private show ss = null;

	class show implements Runnable {
		int ii = 0;
		private L1NpcShopInstance npc = null;

		public show(L1NpcShopInstance np) {
			npc = np;
		}

		@Override
		public void run() {
			if (npc == null)
				return;
			try {
				if (ss != null) {
					GeneralThreadPool.getInstance().schedule(this,
							_rnd.nextInt(1000));
					return;
				}
				ss = this;
				// Thread.sleep(20000+_rnd.nextInt(10000));
				// System.out.println("스폰");
				Thread.sleep(_rnd.nextInt(1000) + 1000);

				// Thread.sleep(100);

				XYSetting(npc);

				L1Shop shop = get(npc.getNpcId());
				boolean ckck = false;
				for (L1ShopItem i : shop.getSellingItems()) {
					ckck = 상점템체크(i.getId());
					if (!ckck) {
						SaveShop(npc, i, i.getPrice(), i.getCount());
					} else {
						// System.out.println("[2] 확인해야할 시장상점 =>> "+npc.getName()+" item "+i.getItem().getItemId());
					}
				}
				// System.out.println("시장상점 스폰 =>> "+npc.getName());
				L1World.getInstance().storeObject(npc);
				L1World.getInstance().addVisibleObject(npc);
			} catch (Exception e) {
			}
			ss = null;
		}

	}

	public L1NpcShopInstance getNpc(int npcid) {
		L1NpcShopInstance npc = null;
		for (L1NpcShopInstance n : shoplist) {
			if (n.getNpcTemplate().get_npcId() == npcid)
				return n;
		}
		return npc;
	}

	public boolean BuyShop_Show_or_Delete(boolean type) {
		boolean ck = false;
		L1NpcShopInstance[] list = NpcChatThread.get().toArray();
		for (L1NpcShopInstance npc : list) {
			if (npc.getState() != 0 && type)
				continue;
			else if (npc.getState() != 3 && !type)
				continue;
			if (!npc.show) {
				// XYSetting(npc);
				L1World.getInstance().storeObject(npc);
				L1World.getInstance().addVisibleObject(npc);
				npc.show = true;
				ck = true;
			} else {
				npc.show = false;
				npc.shopdelete();
			}
		}
		list = null;
		return ck;
	}

	public L1NpcShopInstance[] getShopList() {
		L1NpcShopInstance[] npc = shoplist
				.toArray(new L1NpcShopInstance[shoplist.size()]);
		return npc;
	}

	/**
	 * 32804~32815 32912~32917 32810 32815 32918 32923 32786 32798 32912 32917
	 * 32786 32792 32918 32923 32786 32792 32929 32934 32786 32798 32935 32941
	 * 32786 32798 32945 32951 32776 32782 32945 32951 32776 32782 32929 32941
	 * 32776 32792 32912 32923 32776 32782 32901 32908 32786 32798 32901 32908
	 * 32804 32815 32901 32908 32819 32826 32901 32908 32819 32826 32912 32924
	 * ----------------------------- 32803 32813 32883 32897 32817 32826 32883
	 * 32896 32830 32844 32883 32897 32830 32844 32901 32910 32829 32844 32914
	 * 32924 32829 32444 32929 32938 32829 32830 32942 32952 32830 32844 32970
	 * 32955 //1시방향 32817 32826 32955 32970 32803 32813 32955 32970 32788 32798
	 * 32956 32970 32775 32784 32956 32970 32771 32757 32956 32970 32757 32771
	 * 32943 32952 32757 32771 32929 32939 32757 32771 32914 32924 32757 32771
	 * 32901 32910 32757 32771 32883 32897 32775 32785 32883 32897 32790 32798
	 * 32883 32897
	 */
	/*
	 * private static final int startX[] = {32804, 32810, 32786, 32786, 32786,
	 * 32786, 32786, 32776, 32776, 32776, 32776, 32786, 32804, 32819, 32819};
	 * private static final int endX[] = {32815, 32815, 32798, 32792, 32792,
	 * 32798, 32798, 32782, 32782, 32782, 32782, 32798, 32815, 32826, 32826};
	 * private static final int startY[] = {32912, 32918, 32912, 32918, 32929,
	 * 32935, 32945, 32945, 32929, 32912, 32901, 32901, 32901, 32901, 32912};
	 * private static final int endY[] = {32917, 32923, 32917, 32923, 32934,
	 * 32941, 32951, 32951, 32941, 32923, 32908, 32908, 32908, 32908, 32924};
	 */
	private static final int startX[] = { 32804, 32810, 32786, 32786, 32786,
			32786, 32786, 32776, 32776, 32776, 32776, 32786, 32804, 32819,
			32819, 32804, 32810, 32819, 32804, 32819 };
	private static final int endX[] = { 32815, 32815, 32798, 32792, 32792,
			32798, 32798, 32782, 32782, 32782, 32782, 32798, 32815, 32826,
			32826, 32815, 32815, 32826, 32815, 32826 };
	private static final int startY[] = { 32912, 32918, 32912, 32918, 32929,
			32935, 32945, 32945, 32929, 32912, 32901, 32901, 32901, 32901,
			32912, 32935, 32929, 32929, 32945, 32945 };
	private static final int endY[] = { 32917, 32923, 32917, 32923, 32934,
			32941, 32951, 32951, 32941, 32923, 32908, 32908, 32908, 32908,
			32924, 32941, 32934, 32941, 32951, 32952 };

	private static final int startX2[] = { 32803, 32817, 32830, 32830, 32829,
			32829, 32829, 32830, 32817, 32803, 32788, 32775, 32757, 32757,
			32757, 32757, 32757, 32757, 32775, 32790 };
	private static final int endX2[] = { 32813, 32826, 32844, 32844, 32844,
			32844, 32830, 32844, 32826, 32813, 32798, 32784, 32771, 32771,
			32771, 32771, 32771, 32771, 32785, 32798 };
	private static final int startY2[] = { 32883, 32883, 32883, 32901, 32914,
			32929, 32942, 32955, 32955, 32955, 32956, 32956, 32956, 32943,
			32929, 32914, 32901, 32883, 32883, 32883 };
	private static final int endY2[] = { 32897, 32896, 32897, 32910, 32924,
			32938, 32952, 32970, 32970, 32970, 32970, 32970, 32970, 32952,
			32939, 32924, 32910, 32897, 32897, 32897 };

	/*
	 * private static final int startX2[] = {32803, 32817, 32830, 32830, 32829,
	 * 32829, 32829, 32830, 32817, 32803, 32788, 32775, 32771, 32757, 32757,
	 * 32757, 32757, 32757, 32775, 32790}; private static final int endX2[] =
	 * {32813, 32826, 32844, 32844, 32844, 32844, 32830, 32844, 32826, 32813,
	 * 32798, 32784, 32757, 32771, 32771, 32771, 32771, 32771, 32785, 32798};
	 * private static final int startY2[] = {32883, 32883, 32883, 32901, 32914,
	 * 32929, 32942, 32970, 32955, 32955, 32956, 32956, 32956, 32943, 32929,
	 * 32914, 32901, 32883, 32883, 32883}; private static final int endY2[] =
	 * {32897, 32896, 32897, 32910, 32924, 32938, 32952, 32955, 32970, 32970,
	 * 32970, 32970, 32970, 32952, 32939, 32924, 32910, 32897, 32897, 32897};
	 */
	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	private boolean XYSetting(L1NpcShopInstance npc) {
		boolean ckck = false;
		int count = 300;
		int count2 = 300;
		L1Map map = L1WorldMap.getInstance().getMap((short) 800);
		Collection<L1Object> objlist = L1World.getInstance()
				.getVisibleObjects(800).values();
		while (count-- > 0) {

			int a = _rnd.nextInt(startX.length);
			int x = startX[a] + _rnd.nextInt(endX[a] - startX[a] + 1);
			int y = startY[a] + _rnd.nextInt(endY[a] - startY[a] + 1);

			boolean ck = false;
			for (L1Object obj : objlist) {
				if (obj.getX() == x && obj.getY() == y) {
					ck = true;
					break;
				}
				if (obj instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) obj;
					if (pc.isPrivateShop()) {
						for (int h = 0; h < 4; h++) {
							int clocx = x + HEADING_TABLE_X[h * 2];
							int clocy = y + HEADING_TABLE_Y[h * 2];
							if (clocx == obj.getX() && clocy == obj.getY())
								ck = true;
						}
					}
				} else if (obj instanceof L1NpcShopInstance
						&& ((L1NpcShopInstance) obj).getState() == 1) {
					for (int h = 0; h < 4; h++) {
						int clocx = x + HEADING_TABLE_X[h * 2];
						int clocy = y + HEADING_TABLE_Y[h * 2];
						if (clocx == obj.getX() && clocy == obj.getY())
							ck = true;
					}
				}
			}
			if (!ck && map.isInMap(x, y) && map.isPassable(x, y)) {
				ckck = true;
				npc.setX(x);
				npc.setY(y);
				npc.setMap((short) 800);
				break;
			}
		}
		if (!ckck) {
			while (count2-- > 0) {

				int a = _rnd.nextInt(startX2.length);
				int x = startX2[a] + _rnd.nextInt(endX2[a] - startX2[a] + 1);
				int y = startY2[a] + _rnd.nextInt(endY2[a] - startY2[a] + 1);

				boolean ck = false;
				for (L1Object obj : objlist) {
					if (obj.getX() == x && obj.getY() == y) {
						ck = true;
						break;
					}
					if (obj instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) obj;
						if (pc.isPrivateShop()) {
							for (int h = 0; h < 4; h++) {
								int clocx = x + HEADING_TABLE_X[h * 2];
								int clocy = y + HEADING_TABLE_Y[h * 2];
								if (clocx == obj.getX() && clocy == obj.getY())
									ck = true;
							}
						}
					} else if (obj instanceof L1NpcShopInstance
							&& ((L1NpcShopInstance) obj).getState() == 1) {
						for (int h = 0; h < 4; h++) {
							int clocx = x + HEADING_TABLE_X[h * 2];
							int clocy = y + HEADING_TABLE_Y[h * 2];
							if (clocx == obj.getX() && clocy == obj.getY())
								ck = true;
						}
					}
				}
				if (!ck && map.isInMap(x, y) && map.isPassable(x, y)) {
					ckck = true;
					npc.setX(x);
					npc.setY(y);
					npc.setMap((short) 800);
					break;
				}
			}
			if (!ckck)
				return false;
		}
		return true;
	}

	public void NpcBuyShop() {
		NpcChatThread.get();
		/** NPC 스폰 **/
		shoplist = NpcBuyShopSpawn.getInstance().load();
		/** 아이템 가격 셋팅 **/
		itemlist = NpcBuyShopPrice.getInstance().load();
	//	System.out.println(":: NpcBuyShop Loading Compleate");
		
	}
}
