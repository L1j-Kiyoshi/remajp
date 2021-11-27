package l1j.server.GameSystem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

import l1j.server.GameSystem.NpcBuyShop.NpcBuyShop;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.NpcShopSpawnTable;
import l1j.server.server.datatables.NpcShopTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.templates.L1NpcShop;
import l1j.server.server.templates.L1ShopItem;

public class NpcShopSystem implements TimeListener {

	private volatile static NpcShopSystem _instance;
	private boolean _power = false;
	public static boolean _dragon_power = false;

	public static NpcShopSystem getInstance() {
		if (_instance == null) {
			_instance = new NpcShopSystem();
			RealTimeClock.getInstance().addListener(_instance);
		}
		return _instance;
	}

	private static final int gfxlist[] = { 11322 };

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
	Random _rnd = new Random(System.nanoTime());
	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	class NpcShopTimer implements Runnable {

		public NpcShopTimer() {
		}

		@Override
		public void run() {
			try {
				L1Shop ss = null;
				// int a = 0;//_rnd.nextInt(startX2.length);

				ArrayList<L1NpcShop> list = NpcShopSpawnTable.getInstance()
						.getList();
				Collection<L1Object> objlist = L1World.getInstance()
						.getVisibleObjects(800).values();
				for (int i = 0; i < list.size(); i++) {

					L1NpcShop shop = list.get(i);

					if (shop.getNpcId() >= 8010000
							&& shop.getNpcId() <= 8010002)
						continue;
					ss = NpcShopTable.getInstance().get(shop.getNpcId());
					if(ss == null){
						//System.out.println(shop.getNpcId());
						continue;
					}
					L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(
							shop.getNpcId());
					npc.setId(ObjectIdFactory.getInstance().nextId());
					npc.setMap(shop.getMapId());

					boolean ckck = false;
					int count = 200;
					int count2 = 200;
					L1Map map = L1WorldMap.getInstance().getMap((short) 800);
					while (count-- > 0) {
						/*
						 * System.out.println("a : "+a); int aa =
						 * endX2[a]-startX2[a]+1; int aaa =
						 * endY2[a]-startY2[a]+1; System.out.println("x : "+aa);
						 * System.out.println("y : "+aaa);
						 */
						/*
						 * if(a >= startX2.length){ a=0; }
						 */
						int a = _rnd.nextInt(startX.length);
						int x = startX[a]
								+ _rnd.nextInt(endX[a] - startX[a] + 1);
						int y = startY[a]
								+ _rnd.nextInt(endY[a] - startY[a] + 1);

						boolean ck = false;
						for (L1Object obj : objlist) {
							if (obj.getX() == x && obj.getY() == y) {
								ck = true;
								break;
							}
							if (obj.getLocation().getTileLineDistance(
									new L1Location(x, y, 800)) <= 1) {
								ck = true;
								break;
							}
							if (obj instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) obj;
								if (pc.isPrivateShop()) {
									for (int h = 0; h < 4; h++) {
										int clocx = x + HEADING_TABLE_X[h * 2];
										int clocy = y + HEADING_TABLE_Y[h * 2];
										if (clocx == obj.getX()
												&& clocy == obj.getY())
											ck = true;
									}
								}
							} else if (obj instanceof L1NpcShopInstance
									&& ((L1NpcShopInstance) obj).getState() == 1) {
								for (int h = 0; h < 4; h++) {
									int clocx = x + HEADING_TABLE_X[h * 2];
									int clocy = y + HEADING_TABLE_Y[h * 2];
									if (clocx == obj.getX()
											&& clocy == obj.getY())
										ck = true;
								}
							}
						}
						if (!ck && map.isInMap(x, y) && map.isPassable(x, y)) {
							ckck = true;
							shop.setX(x);
							shop.setY(y);
							shop.setMapId((short) 800);
							break;
						}
					}
					if (!ckck) {
						while (count2-- > 0) {

							/*
							 * if(a >= startX.length){ a=0; }
							 */
							int a = _rnd.nextInt(startX2.length);
							int x = startX2[a]
									+ _rnd.nextInt(endX2[a] - startX2[a] + 1);
							int y = startY2[a]
									+ _rnd.nextInt(endY2[a] - startY2[a] + 1);
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
											int clocx = x
													+ HEADING_TABLE_X[h * 2];
											int clocy = y
													+ HEADING_TABLE_Y[h * 2];
											if (clocx == obj.getX()
													&& clocy == obj.getY())
												ck = true;
										}
									}
								} else if (obj instanceof L1NpcShopInstance
										&& ((L1NpcShopInstance) obj).getState() == 1) {
									for (int h = 0; h < 4; h++) {
										int clocx = x + HEADING_TABLE_X[h * 2];
										int clocy = y + HEADING_TABLE_Y[h * 2];
										if (clocx == obj.getX()
												&& clocy == obj.getY())
											ck = true;
									}
								}
							}
							if (!ck && map.isInMap(x, y)
									&& map.isPassable(x, y)) {
								ckck = true;
								npc.setX(x);
								npc.setY(y);
								npc.setMap((short) 800);
								break;
							}
						}
						if (!ckck)
							continue;
					}
					npc.getLocation().set(shop.getX(), shop.getY(),
							shop.getMapId());

					npc.setHomeX(npc.getX());
					npc.setHomeY(npc.getY());
					npc.getMoveState().setHeading(shop.getHeading());
					npc.getGfxId().setTempCharGfx(
							gfxlist[_rnd.nextInt(gfxlist.length)]);

					npc.setName(shop.getName());
					npc.setTitle(shop.getTitle());

					L1NpcShopInstance obj = (L1NpcShopInstance) npc;

					obj.setShopName(shop.getShopName());

					obj.setState(1);

					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);
					npc.getLight().turnOnOffLight();
					for (L1ShopItem item : ss.getSellingItems()) {
						NpcBuyShop.getInstance().SaveShop(npc, item, item.getPrice(), item.getCount());
					}
					shopRefill(npc);
					_shops.add(npc);
					Thread.sleep(10);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	class NpcDragonShopTimer implements Runnable {

		public NpcDragonShopTimer() {
		}

		@Override
		public void run() {
			try {
				Random _rnd = new Random(System.nanoTime());
				ArrayList<L1NpcShop> list = NpcShopSpawnTable.getInstance()
						.getList();
				// Collection<L1Object> objlist =
				// L1World.getInstance().getVisibleObjects(800).values();
				for (int i = 0; i < list.size(); i++) {

					L1NpcShop shop = list.get(i);

					if (shop.getNpcId() < 8010000 || shop.getNpcId() > 8010002)
						continue;

					L1NpcInstance npc = NpcTable.getInstance().newNpcInstance(
							shop.getNpcId());
					npc.setId(ObjectIdFactory.getInstance().nextId());
					npc.setMap(shop.getMapId());

					npc.getLocation().set(shop.getX(), shop.getY(),
							shop.getMapId());

					npc.setHomeX(npc.getX());
					npc.setHomeY(npc.getY());
					npc.getMoveState().setHeading(shop.getHeading());
					npc.getGfxId().setTempCharGfx(
							gfxlist[_rnd.nextInt(gfxlist.length)]);

					npc.setName(shop.getName());
					npc.setTitle(shop.getTitle());

					L1NpcShopInstance obj = (L1NpcShopInstance) npc;

					obj.setShopName(shop.getShopName());

					obj.setState(1);

					L1World.getInstance().storeObject(npc);
					L1World.getInstance().addVisibleObject(npc);

					npc.getLight().turnOnOffLight();
					shopRefill(npc);
					_shops.add(npc);
					Thread.sleep(10);
				}

			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}

	@Override
	public void onMonthChanged(BaseTime time) {
	}

	@Override
	public void onDayChanged(BaseTime time) {
	}

	private static ArrayList<L1NpcInstance> _shops = new ArrayList<L1NpcInstance>(256);
	
	@Override
	public void onHourChanged(BaseTime time) {
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		if(isPower()){
			MJDShopStorage.updateProcess(_shops);
		}
	}

	public void npcShopStart() {
		NpcShopTimer ns = new NpcShopTimer();
		GeneralThreadPool.getInstance().execute(ns);
		_power = true;
	}
	
	/** 2016.11.24 MJ æ€ºæ≈Õ Ω√ºº **/
	public void npcShopStop() {
		_power = false;
		int size = _shops.size();
		for(int i=0; i<size; i++){
			L1NpcInstance npc = _shops.get(i);
			if(npc == null)
				continue;
			
			GeneralThreadPool.getInstance().execute(new MJDShopStorage(npc, true));
			npc.deleteMe();
		}
		_shops.clear();
	}

	public void npcDragonShopStart() {
		_dragon_power = true;
		NpcDragonShopTimer ns = new NpcDragonShopTimer();
		GeneralThreadPool.getInstance().execute(ns);
	}

	public boolean isPower() {
		return _power;
	}
	
	
	class ShopUpdator implements Runnable{

		@Override
		public void run() {
			
		}
	}
	
	/** 2016.11.24 MJ æ€ºæ≈Õ Ω√ºº **/
	private void shopRefill(L1NpcInstance npc){
		try{
			L1Shop shop = NpcShopTable.getInstance().get(npc.getNpcId());
			if(shop == null)
				return;
			
			List<L1ShopItem> list = shop.getSellingItems();
			if(list == null)
				return;
			
			int size = list.size();
			MJDShopItem ditem 	= null;
			for(int i=0; i<size; i++){
				ditem = MJDShopItem.create(list.get(i), i, false);
				npc.addSellings(ditem);
			}
			
			list = shop.getPurchasingItems();
			if(list == null)
				return;
			
			size = list.size();
			for(int i=0; i<size; i++){
				ditem = MJDShopItem.create(list.get(i), i, true);
				npc.addPurchasings(ditem);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
