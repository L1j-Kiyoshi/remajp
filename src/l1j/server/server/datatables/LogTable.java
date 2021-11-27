package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Timestamp;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.GMCommands;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1ShopItem;
import l1j.server.server.utils.SQLUtil;

//import l1j.server.L1DatabaseFactory;

public class LogTable {

	private static LogTable _instance;

	// public static final int DROP_LOG = 0;
	// public static final int PICKUP_LOG = 1;

	public static LogTable getInstance() {
		if (_instance == null)
			_instance = new LogTable();
		return _instance;
	}

	public LogTable() {
	}

	private static Timestamp start사냥T;
	private static Timestamp start상점T;
	public static boolean 사냥아덴 = false;
	public static boolean 상점아덴 = false;
	public static FastTable<adenLog> 사냥아덴리스트 = new FastTable<adenLog>();
	public static FastTable<adenLog> 상점아덴리스트 = new FastTable<adenLog>();

	public static void 사냥아덴시작() {
		사냥아덴리스트.clear();
		사냥아덴 = true;
		start사냥T = new Timestamp(System.currentTimeMillis());
	}

	public static void 사냥아덴(L1PcInstance pc, int count) {
		if (사냥아덴) {
			if (pc.getNetConnection() == null)
				return;
			adenLog log = null;
			for (adenLog aL : 사냥아덴리스트) {
				if (aL.name.equalsIgnoreCase(pc.getName())) {
					log = aL;
					break;
				}
			}
			if (log != null) {
				log.count += count;
			} else {
				log = new adenLog();
				log.accounts = pc.getAccountName();
				log.name = pc.getName();
				log.count += count;
				사냥아덴리스트.add(log);
			}
		}
	}

	public static void 사냥아덴종료() {
		사냥아덴 = false;
		GeneralThreadPool.getInstance().schedule(
				new saveThread(사냥아덴리스트, start사냥T, false), 1);
	}

	public static void 상점아덴시작() {
		상점아덴리스트.clear();
		상점아덴 = true;
		start상점T = new Timestamp(System.currentTimeMillis());
	}

	public static void 상점아덴(L1PcInstance pc, int count) {
		if (상점아덴) {
			if (pc.getNetConnection() == null)
				return;
			adenLog log = null;
			for (adenLog aL : 상점아덴리스트) {
				if (aL.name.equalsIgnoreCase(pc.getName())) {
					log = aL;
					break;
				}
			}
			if (log != null) {
				log.count += count;
			} else {
				log = new adenLog();
				log.accounts = pc.getAccountName();
				log.name = pc.getName();
				log.count += count;
				상점아덴리스트.add(log);
			}
		}
	}

	public static void 상점아덴종료() {
		상점아덴 = false;
		GeneralThreadPool.getInstance().schedule(
				new saveThread(상점아덴리스트, start상점T, true), 1);
	}

	static class adenLog {
		public String accounts = "";
		public String name = "";
		public int count = 0;
	}

	static class saveThread implements Runnable {
		private FastTable<adenLog> list = new FastTable<adenLog>();
		private Timestamp start;
		private Timestamp end;
		private boolean shop = false;

		public saveThread(FastTable<adenLog> _list, Timestamp _startTime,
				boolean ck) {
			list.addAll(_list);
			start = new Timestamp(_startTime.getTime());
			end = new Timestamp(System.currentTimeMillis());
			shop = ck;
		}

		@Override
		public void run() {
			try {
				if (list.size() <= 0)
					return;
				adenLog aL = list.remove(0);
				if (aL == null)
					return;
				if (aL.count <= 0) {
					GeneralThreadPool.getInstance().schedule(this, 100);
					return;
				}
				Connection c = null;
				PreparedStatement p = null;
				try {
					c = L1DatabaseFactory.getInstance().getConnection();
					if (shop) {
						p = c.prepareStatement("INSERT INTO log_adena_shop SET startTime=?, endTime=?, accounts=?, name=?, count=?");
					} else {
						p = c.prepareStatement("INSERT INTO log_adena_monster SET startTime=?, endTime=?, accounts=?, name=?, count=?");
					}
					p.setTimestamp(1, start);
					p.setTimestamp(2, end);
					p.setString(3, aL.accounts);
					p.setString(4, aL.name);
					p.setInt(5, aL.count);
					p.executeUpdate();
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					SQLUtil.close(p);
					SQLUtil.close(c);
				}
				GeneralThreadPool.getInstance().schedule(this, 100);
			} catch (Exception e) {

			}
		}

	}

	public static boolean log_lucky_darkelder(L1PcInstance pc) {
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_lucky_darkelder SET accounts=?, id=?, name=?, time=SYSDATE()");

			p.setString(1, pc.getAccountName());
			p.setInt(2, pc.getId());
			p.setString(3, pc.getName());
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	/*
	 * public static boolean log_adena_monster(L1PcInstance pc, int old_count,
	 * int new_count){ boolean bool = false; Connection c = null;
	 * PreparedStatement p = null; try{ c =
	 * L1DatabaseFactory.getInstance().getConnection(); p = c.prepareStatement(
	 * "INSERT INTO log_adena_monster SET accounts=?, id=?, name=?, old_count=?, new_count=?, time=SYSDATE()"
	 * );
	 * 
	 * p.setTimestamp(1, pc.getAccountName()); p.setInt(2, pc.getId());
	 * p.setString(3, pc.getName()); p.setInt(4, old_count); p.setInt(5,
	 * new_count); p.executeUpdate(); bool = true; }catch(Exception e){
	 * e.printStackTrace(); }finally{ SQLUtil.close(p); SQLUtil.close(c); }
	 * return bool; }
	 */
	public static boolean log_fire_energy(L1PcInstance pc, int old_count,
			int new_count) {
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;

		try {

			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_fire_energy SET accounts=?, id=?, name=?, old_count=?, new_count=?, time=SYSDATE()");

			p.setString(1, pc.getAccountName());
			p.setInt(2, pc.getId());
			p.setString(3, pc.getName());
			p.setInt(4, old_count);
			p.setInt(5, new_count);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	public static boolean log_npc_buy_ok(L1PcInstance pc, L1ShopItem item,
			int count) {
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_npc_buy SET accounts=?, id=?, name=?, itemId=?, itemName=?, itemEnchant=?, count=?, price=?, time=SYSDATE(), note=?");

			p.setString(1, pc.getAccountName());
			p.setInt(2, pc.getId());
			p.setString(3, pc.getName());
			p.setInt(4, item.getItemId());
			p.setString(5, item.getItem().getName());
			p.setInt(6, item.getEnchant());
			p.setInt(7, count);
			p.setInt(8, item.getBuyPrice() * count);
			p.setString(9, "성공");
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	public static boolean log_npc_buy_cancel(L1PcInstance pc,
			L1ItemInstance item, int count) {
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_npc_buy SET accounts=?, id=?, name=?, itemId=?, itemName=?, itemEnchant=?, count=?, price=?, time=SYSDATE(), note=?");

			p.setString(1, pc.getAccountName());
			p.setInt(2, pc.getId());
			p.setString(3, pc.getName());
			p.setInt(4, item.getItemId());
			p.setString(5, item.getName());
			p.setInt(6, item.getEnchantLevel());
			p.setInt(7, count);
			p.setInt(8, 0);
			p.setString(9, "취소");
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	/*
	 * public static boolean logact(L1PcInstance pc, String s){ String sTemp =
	 * ""; boolean bool = false; Connection c = null; PreparedStatement p =
	 * null; try{ sTemp = s; c =
	 * L1DatabaseFactory.getInstance().getConnection(); // data -> date // p =
	 * c.prepareStatement(
	 * "insert into log_test set type=?, char_name=?, comment=?, data=sysdate()"
	 * ); p = c.prepareStatement(
	 * "INSERT INTO log_act SET time=SYSDATE(), type=?, account=?, char_id=?, char_name=?, char_x=?, char_y=?, char_mapid=?"
	 * );
	 * 
	 * p.setString(1, sTemp); p.setString(2, pc.getAccountName()); p.setInt(3,
	 * pc.getId()); p.setString(4, pc.getName()); p.setInt(5, pc.getX());
	 * p.setInt(6, pc.getY()); p.setInt(7, pc.getMapId()); p.executeUpdate();
	 * bool = true; }catch(Exception e){ e.printStackTrace(); }finally{
	 * SQLUtil.close(p); SQLUtil.close(c); } return bool; }
	 */
	/**
	 * 디비로 삭제 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            Log type 0=Drop 1=PickUp 2=Delete
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */
	/*
	 * public static boolean logdel(L1PcInstance pc, L1ItemInstance item, int
	 * count, int type){ String sTemp = ""; boolean bool = false; Connection c =
	 * null; PreparedStatement p = null; try{ switch(type){ case 0://DROP_LOG //
	 * x, y, map, itme_name drop // sTemp = "DROP " + pc.getX() + " " +
	 * pc.getY() + " " + pc.getMap().getId() + " " + item.getName(); sTemp =
	 * "[드랍]"; break; case 1://PICKUP_LOG sTemp = "[픽업]"; break; case 2://
	 * Delete_Log sTemp = "[삭제] "; break; case 3: sTemp = "[용해]"; break; } c =
	 * L1DatabaseFactory.getInstance().getConnection(); // data -> date // p =
	 * c.prepareStatement(
	 * "insert into log_test set type=?, char_name=?, comment=?, data=sysdate()"
	 * ); p = c.prepareStatement(
	 * "INSERT INTO log_del SET time=SYSDATE(), type=?, account=?, char_id=?, char_name=?, item_id=?, item=?, item_name=?, item_enchant=?, item_count=?, char_x=?, char_y=?, char_mapid=?"
	 * );
	 * 
	 * p.setString(1, sTemp); p.setString(2, pc.getAccountName()); p.setInt(3,
	 * pc.getId()); p.setString(4, pc.getName()); p.setInt(5, item.getId());
	 * p.setInt(6, item.getItemId()); p.setString(7, item.getName());
	 * p.setInt(8, item.getEnchantLevel()); p.setInt(9, count); p.setInt(10,
	 * pc.getX()); p.setInt(11, pc.getY()); p.setInt(12, pc.getMapId());
	 * p.executeUpdate(); bool = true; }catch(Exception e){ e.printStackTrace();
	 * }finally{ SQLUtil.close(p); SQLUtil.close(c); } return bool; }
	 */

	/**
	 * 디비로 드랍 픽업 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            Log type 0=Drop 1=PickUp 2=Delete
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */

	public static boolean logitem(L1PcInstance pc, L1ItemInstance item,
			int count, int type) {
		String sTemp = "";
		boolean bool = false;
		/**
		 * Connection c = null; PreparedStatement p = null; try{ switch(type){
		 * case 0://DROP_LOG // x, y, map, itme_name drop // sTemp = "DROP " +
		 * pc.getX() + " " + pc.getY() + " " + pc.getMap().getId() + " " +
		 * item.getName(); if(!GMCommands.로그_드랍) return false; sTemp = "[드랍]";
		 * break; case 1://PICKUP_LOG if(!GMCommands.로그_픽업) return false; sTemp
		 * = "[픽업]"; break; case 2:// Delete_Log sTemp = "[삭제] "; break; case 3:
		 * if(!GMCommands.로그_용해) return false; sTemp = "[용해]"; break; } c =
		 * L1DatabaseFactory.getInstance().getConnection(); // data -> date // p
		 * = c.prepareStatement(
		 * "insert into log_test set type=?, char_name=?, comment=?, data=sysdate()"
		 * ); p = c.prepareStatement(
		 * "INSERT INTO log_item SET time=SYSDATE(), type=?, account=?, char_id=?, char_name=?, item_id=?, item=?, item_name=?, item_enchant=?, item_count=?, char_x=?, char_y=?, char_mapid=?"
		 * );
		 * 
		 * p.setString(1, sTemp); p.setString(2, pc.getAccountName());
		 * p.setInt(3, pc.getId()); p.setString(4, pc.getName()); p.setInt(5,
		 * item.getId()); p.setInt(6, item.getItemId()); p.setString(7,
		 * item.getName()); p.setInt(8, item.getEnchantLevel()); p.setInt(9,
		 * count); p.setInt(10, pc.getX()); p.setInt(11, pc.getY());
		 * p.setInt(12, pc.getMapId()); p.executeUpdate(); bool = true;
		 * }catch(Exception e){ e.printStackTrace(); }finally{ SQLUtil.close(p);
		 * SQLUtil.close(c); }
		 **/
		return bool;
	}

	/**
	 * 디비로 창고 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            Log type 0=개인넣기 1=개인찾기 2=요정넣기 3= 요정찾기 4=웹창고
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */

	public static boolean logwarehouse(L1PcInstance pc, L1ItemInstance item,
			int count, int type) {
		String sTemp = "";
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {
			switch (type) {
			case 0:
				sTemp = "개인넣기";
				break;
			case 1:
				sTemp = "개인찾기";
				break;
			case 2:
				sTemp = "요정넣기";
				break;
			case 3:
				sTemp = "요정찾기";
				break;
			case 4:
				sTemp = "웹창고";
				break;
			}
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_warehouse SET type=?, account=?, char_id=?, char_name=?, item_id=?, item_name=?, item_enchantlvl=?, item_count=?, datetime=SYSDATE()");

			p.setString(1, sTemp);
			p.setString(2, pc.getAccountName());
			p.setInt(3, pc.getId());
			p.setString(4, pc.getName());
			p.setInt(5, item.getId());
			p.setString(6, item.getName());
			p.setInt(7, item.getEnchantLevel());
			p.setInt(8, count);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	/**
	 * 디비로 클랜 창고 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            Log type 0=넣기 1=찾기
	 * @return
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */

	public static boolean logcwarehouse(L1PcInstance pc, L1ItemInstance item,
			int count, int type) {
		String sTemp = "";
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {
			switch (type) {
			case 0:
				sTemp = "넣기";
				break;
			case 1:
				sTemp = "찾기";
				break;
			}
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_cwarehouse SET type=?, clan_id=?, clan_name=?, account=?, char_id=?, char_name=?, item_id=?, item_name=?, item_enchantlvl=?, item_count=?, datetime=SYSDATE()");

			p.setString(1, sTemp);
			p.setInt(2, pc.getClanid());
			p.setString(3, pc.getClanname());
			p.setString(4, pc.getAccountName());
			p.setInt(5, pc.getId());
			p.setString(6, pc.getName());
			p.setInt(7, item.getId());
			p.setString(8, item.getName());
			p.setInt(9, item.getEnchantLevel());
			p.setInt(10, count);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	public static boolean logAdentrade(L1PcInstance player,
			L1PcInstance trading_partner, L1ItemInstance item) {
		// String sTemp = "";
		if (!GMCommands.로그_교환)
			return false;
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO _log_Aden_trade_ok SET time=SYSDATE(), char_account=?,  char_name=?, char_level=?, account_char_count=?, Adena_count=?");

			p.setString(1, trading_partner.getAccountName());
			p.setString(2, trading_partner.getName());
			p.setInt(3, trading_partner.getLevel());
			p.setInt(4, trading_partner.getNetConnection().getAccount()
					.countCharacters());
			p.setInt(5, item.getCount());
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	/**
	 * 디비로 교환 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */

	public static boolean logtrade(L1PcInstance player,
			L1PcInstance trading_partner, L1ItemInstance item) {
		// String sTemp = "";
		if (!GMCommands.로그_교환)
			return false;
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		try {

			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO _log_trade_ok SET time=SYSDATE(), char_account=?, char_id=?, char_name=?, t_account=?, t_id=?, t_name=?, item_id=?, item_name=?, item_enchant=?, item_count=?");

			p.setString(1, player.getAccountName());
			p.setInt(2, player.getId());
			p.setString(3, player.getName());
			p.setString(4, trading_partner.getAccountName());
			p.setInt(5, trading_partner.getId());
			p.setString(6, trading_partner.getName());
			p.setInt(7, item.getId());
			p.setString(8, item.getName());
			p.setInt(9, item.getEnchantLevel());
			p.setInt(10, item.getCount());
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	/**
	 * 디비로 개인상점 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            0=구입 1=판매
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */

	public static boolean logshop(L1PcInstance pc, L1PcInstance targetPc,
			L1ItemInstance item, int price, int count, int type) {
		if (!GMCommands.로그_개인상점)
			return false;

		String sTemp = "";
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		int tPrice = price * count;
		try {

			switch (type) {
			case 0:
				sTemp = "개인상점 - 구매";
				break;
			case 1:
				sTemp = "개인상점 - 판매";
				break;
			}

			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_private_shop SET time=SYSDATE(), type=?, shop_account=?, shop_id=?, shop_name=?, user_account=?, user_id=?, user_name=?, item_id=?, item_name=?, item_enchantlvl=?, price=?, item_count=?, total_price=?");

			p.setString(1, sTemp);
			p.setString(2, targetPc.getAccountName());// 상점 계정
			p.setInt(3, targetPc.getId());// 상점 아이디
			p.setString(4, targetPc.getName());// 상점 네임
			p.setString(5, pc.getAccountName());// 주체자 계정
			p.setInt(6, pc.getId());// 주체자 아이디
			p.setString(7, pc.getName());// 주체자 이름
			p.setInt(8, item.getId());
			p.setString(9, item.getName());
			p.setInt(10, item.getEnchantLevel());
			p.setInt(11, price);
			p.setInt(12, count);
			p.setInt(13, tPrice);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	/**
	 * 디비로 상점 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            0=구입 1=판매
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */

	public static boolean lognpcshop(L1PcInstance pc, int npcid,
			L1ItemInstance item, int price, int count, int type) {
		if (!GMCommands.로그_상점)
			return false;

		String sTemp = "";
		boolean bool = false;
		Connection c = null;
		PreparedStatement p = null;
		int tPrice = price * count;
		try {

			switch (type) {
			case 0:
				sTemp = "상점 - 구매";
				break;
			case 1:
				sTemp = "상점 - 판매";
				break;
			}

			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("INSERT INTO log_shop SET time=SYSDATE(), type=?, npc_id=?, user_account=?, user_id=?, user_name=?, item_id=?, item_name=?, item_enchantlvl=?, price=?, item_count=?, total_price=?");

			p.setString(1, sTemp);
			p.setInt(2, npcid);// 상점 계정
			p.setString(3, pc.getAccountName());// 주체자 계정
			p.setInt(4, pc.getId());// 주체자 아이디
			p.setString(5, pc.getName());// 주체자 이름
			p.setInt(6, item.getId());
			p.setString(7, item.getName());
			p.setInt(8, item.getEnchantLevel());
			p.setInt(9, price);
			p.setInt(10, count);
			p.setInt(11, tPrice);
			p.executeUpdate();
			bool = true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
		return bool;
	}

	/**
	 * 디비로 인챈 로그를 기록
	 * 
	 * @param pc
	 *            Pc Instance
	 * @param item
	 *            item Instance
	 * @param type
	 *            0=성공 1=실패
	 * @return 성공 true 실패 false used : if(LogTable.getInstance().insert(pc,
	 *         item, LogTable.DROP_LOG)){ System.out.println("Log Write OK,"); }
	 */

	public static boolean logEnchant(L1PcInstance pc, L1ItemInstance item,
			int old_enchantlvl, int new_enchantlvl, int type) {

		if (!GMCommands.로그_인첸)
			return false;

		String sTemp = "";
		boolean bool = false;
		/**
		 * Connection c = null; PreparedStatement p = null; try{
		 * 
		 * switch(type){ case 0: sTemp = "성공"; break; case 1: sTemp = "실패";
		 * break; }
		 * 
		 * c = L1DatabaseFactory.getInstance().getConnection(); p =
		 * c.prepareStatement(
		 * "INSERT INTO log_enchant SET type=?, char_account=?, char_id=?, char_name=?, item_id=?, item_name=?, old_enchantlvl=?, new_enchantlvl=?, datetime=SYSDATE()"
		 * );
		 * 
		 * p.setString(1, sTemp); p.setString(2, pc.getAccountName());
		 * p.setInt(3, pc.getId()); p.setString(4, pc.getName()); p.setInt(5,
		 * item.getId()); p.setString(6, item.getName()); p.setInt(7,
		 * old_enchantlvl); p.setInt(8, new_enchantlvl); p.executeUpdate(); bool
		 * = true; }catch(Exception e){ e.printStackTrace(); }finally{
		 * SQLUtil.close(p); SQLUtil.close(c); }
		 **/
		return bool;
	}
	/*
	 * public static boolean locRepairlog(int x, int y, int mapid, int tile){
	 * boolean bool = false; Connection c = null; PreparedStatement p = null;
	 * try{ c = L1DatabaseFactory.getInstance().getConnection(); // data -> date
	 * // p = c.prepareStatement(
	 * "insert into log_test set type=?, char_name=?, comment=?, data=sysdate()"
	 * ); p = c.prepareStatement(
	 * "INSERT INTO log_loc SET time=SYSDATE(), char_x=?, char_y=?, char_mapid=?, tile=?"
	 * );
	 * 
	 * p.setInt(1, x); p.setInt(2, y); p.setInt(3, mapid); p.setInt(4, tile);
	 * p.executeUpdate(); bool = true; }catch(Exception e){ e.printStackTrace();
	 * }finally{ SQLUtil.close(p); SQLUtil.close(c); } return bool; }
	 * 
	 * /* public static boolean autolog(L1PcInstance pc){ String map = "";
	 * boolean bool = false; Connection c = null; PreparedStatement p = null;
	 * try{ // int mapid = pc.getMapId(); // switch(mapid){ // case 9: map =
	 * "본던 3층"; break; // case 10: map = "본던 4층"; break; // case 11: map =
	 * "본던 5층"; break; // case 12: map = "본던 6층"; break; // case 13: map =
	 * "본던 7층"; break; // case 26: map = "사던 2층"; break; // case 27: map =
	 * "사던 3층"; break; // case 28: map = "사던 4층"; break; // case 307: map =
	 * "침공로 1층"; break; // case 308: map = "침공로 2층"; break; // case 309: map =
	 * "침공로 3층"; break; // default: break; // }
	 * 
	 * map = MapsTable.getInstance().getMapName(pc.getMapId());
	 * 
	 * c = L1DatabaseFactory.getInstance().getConnection(); // data -> date // p
	 * = c.prepareStatement(
	 * "insert into log_test set type=?, char_name=?, comment=?, data=sysdate()"
	 * ); p = c.prepareStatement(
	 * "INSERT INTO log_auto SET time=SYSDATE(), account=?, char_id=?, char_name=?, char_level=?, char_x=?, char_y=?, char_mapid=?, mapName=?"
	 * );
	 * 
	 * p.setString(1, pc.getAccountName()); p.setInt(2, pc.getId());
	 * p.setString(3, pc.getName()); p.setInt(4, pc.getLevel()); p.setInt(5,
	 * pc.getX()); p.setInt(6, pc.getY()); p.setInt(7, pc.getMapId());
	 * p.setString(8, map); p.executeUpdate(); bool = true; }catch(Exception e){
	 * e.printStackTrace(); }finally{ SQLUtil.close(p); SQLUtil.close(c); }
	 * return bool; }
	 */

	/**
	 * 
	 * @param pc
	 * @param reason
	 * @return
	 */
	/*
	 * public static boolean logbug(L1PcInstance pc, String reason){ boolean
	 * bool = false; Connection c = null; PreparedStatement p = null; try{
	 * 
	 * c = L1DatabaseFactory.getInstance().getConnection(); // data -> date // p
	 * = c.prepareStatement(
	 * "insert into log_test set type=?, char_name=?, comment=?, data=sysdate()"
	 * ); p = c.prepareStatement(
	 * "INSERT INTO log_bug SET time=SYSDATE(), type=?, account=?, char_id=?, char_name=?, char_x=?, char_y=?, char_mapid=?"
	 * );
	 * 
	 * p.setString(1, reason); p.setString(2, pc.getAccountName()); p.setInt(3,
	 * pc.getId()); p.setString(4, pc.getName()); p.setInt(5, pc.getX());
	 * p.setInt(6, pc.getY()); p.setInt(7, pc.getMapId()); p.executeUpdate();
	 * bool = true; }catch(Exception e){ e.printStackTrace(); }finally{
	 * SQLUtil.close(p); SQLUtil.close(c); } return bool; }
	 */
	// public static boolean logsys(String reason){
	// boolean bool = false;
	// Connection c = null;
	// PreparedStatement p = null;
	// try{
	//
	// c = L1DatabaseFactory.getInstance().getConnection();
	// // data -> date
	// // p =
	// c.prepareStatement("insert into log_test set type=?, char_name=?, comment=?, data=sysdate()");
	// p =
	// c.prepareStatement("INSERT INTO log_bug SET time=SYSDATE(), type=?, account=?, char_id=?, char_name=?, char_x=?, char_y=?, char_mapid=?");
	//
	// p.setString(1, reason);
	// p.setString(2, pc.getAccountName());
	// p.setInt(3, pc.getId());
	// p.setString(4, pc.getName());
	// p.setInt(5, pc.getX());
	// p.setInt(6, pc.getY());
	// p.setInt(7, pc.getMapId());
	// p.executeUpdate();
	// bool = true;
	// }catch(Exception e){
	// e.printStackTrace();
	// }finally{
	// SQLUtil.close(p);
	// SQLUtil.close(c);
	// }
	// return bool;
	// }
	//
	/**
	 * 
	 * @param pc
	 * @param reason
	 * @return
	 */
	/*
	 * public static boolean logdelay(L1PcInstance pc, String reason, long
	 * delaytime){ boolean bool = true; Connection c = null; PreparedStatement p
	 * = null; try{
	 * 
	 * c = L1DatabaseFactory.getInstance().getConnection(); // data -> date // p
	 * = c.prepareStatement(
	 * "insert into log_test set type=?, char_name=?, comment=?, data=sysdate()"
	 * ); p = c.prepareStatement(
	 * "INSERT INTO log_delay SET time=SYSDATE(), type=?, account=?, char_id=?, char_name=?, char_x=?, char_y=?, char_mapid=?, delaytime=?"
	 * );
	 * 
	 * p.setString(1, reason); p.setString(2, pc.getAccountName()); p.setInt(3,
	 * pc.getId()); p.setString(4, pc.getName()); p.setInt(5, pc.getX());
	 * p.setInt(6, pc.getY()); p.setInt(7, pc.getMapId()); p.setLong(8,
	 * delaytime); p.executeUpdate(); bool = true; }catch(Exception e){
	 * e.printStackTrace(); }finally{ SQLUtil.close(p); SQLUtil.close(c); }
	 * return bool; }
	 */
}