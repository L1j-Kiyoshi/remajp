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
package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javolution.util.FastMap;
import server.system.autoshop.AutoShop;
import server.system.autoshop.AutoShopManager;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ActionCodes;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.clientpackets.C_CreateNewCharacter;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1Shop;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_InvList;
import l1j.server.server.templates.L1NpcShop;
import l1j.server.server.templates.L1PrivateShopBuyList;
import l1j.server.server.templates.L1PrivateShopSellList;
import l1j.server.server.utils.SQLUtil;

public class AutoShopTable {
	
	private static Logger _log = Logger.getLogger(AutoShopTable.class.getName());
	
	private static AutoShopTable _instance;

	public final Map<Integer, L1PcInstance> autoshoptable = new FastMap<Integer, L1PcInstance>();
	
	private final Map<Integer, L1Shop> _npcShops = new HashMap<Integer, L1Shop>();
	
	private ArrayList<L1NpcShop> npcShoplist = new ArrayList<L1NpcShop>();
	

	public static AutoShopTable getInstance() {
		if (_instance == null) {
			_instance = new AutoShopTable();
		}
		return _instance;
	}

	private AutoShopTable() {
		//PerformanceTimer timer = new PerformanceTimer();
		System.out.print("■ 구무인상점 데이터 .......................... ");;
		fillSpawnTable(0);
		System.out.println("■ 로딩 정상 완료");
	}

	private synchronized void fillSpawnTable(int step) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet shopRs = null;
		ResultSet itemRs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM autoshoplist where step = ? and count = '1'");
			pstm.setInt(1, step);
			shopRs = pstm.executeQuery();
			
			AutoShopManager shopManager = AutoShopManager.getInstance(); 
			AutoShop autoshop = null;
			L1PcInstance pc = null;				
			
			while (shopRs.next()) {
				try {
					String charName = shopRs.getString("char_name");
																		
					pc = L1World.getInstance().getPlayer(charName); 	
					
					if (pc != null) {
						pc.setPrivateShop(false);
						pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						Broadcaster.broadcastPacket(pc, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						//Thread.sleep(1000);
						pc.logout();					
						pc = null;
					}
					
					if (pc == null) {
						pc = new L1PcInstance();
						
						pc.setId(ObjectIdFactory.getInstance().nextId());
						pc.setName(shopRs.getString("char_name"));						
						pc.set_sex(shopRs.getInt("Sex"));	// 0~1
						pc.setType(shopRs.getInt("Type"));	
						
						if (pc.get_sex() == 0) {
							pc.setClassId(C_CreateNewCharacter.MALE_LIST[pc.getType()]);
						}
						else {
							pc.setClassId(C_CreateNewCharacter.FEMALE_LIST[pc.getType()]);
						}
						
						pc.getGfxId().setTempCharGfx(pc.getClassId());
						pc.getGfxId().setGfxId(pc.getClassId());
						
						pc.getMoveState().setHeading(shopRs.getInt("Heading"));
						pc.setLawful(shopRs.getInt("Lawful"));
						pc.setTitle(shopRs.getString("Title"));
						pc.setX(shopRs.getInt("LocX"));
						pc.setY(shopRs.getInt("LocY"));		
						pc.setMap(shopRs.getShort("MapID"));
						pc.setShopStep(shopRs.getInt("step"));
												
						pc.setHighLevel(1);
						pc.getAbility().setBaseStr(20);
						pc.getAbility().setBaseDex(20);
						pc.getAbility().setBaseCon(20);
						pc.getAbility().setBaseWis(20);
						pc.getAbility().setBaseCha(20);
						pc.getAbility().setBaseInt(20);	
						pc.addBaseMaxHp((short)200);
						pc.setCurrentHp(pc.getMaxHp());
						pc.addBaseMaxMp((short)20);
						pc.setCurrentMp(pc.getMaxMp());
						pc.resetBaseAc();
						pc.setClanRank(0);
						pc.setClanid(0);
						pc.setClanname("");
						pc.set_food(39); // 17%
						pc.setGm(false);
						pc.setMonitor(false);
						pc.setGmInvis(false);
						pc.setExp(0);
						pc.setActionStatus(0);
						pc.setAccessLevel((short) 0);
						pc.getAbility().setBonusAbility(0);
						pc.resetBaseMr();
						pc.setElfAttr(0);
						pc.set_PKcount(0);
						pc.setExpRes(0);
						pc.setPartnerId(0);
						pc.setOnlineStatus(0);
						pc.setHomeTownId(0);
						pc.setContribution(0);
						pc.setBanned(false);
						pc.setKarma(0);
						pc.setReturnStat(0);
						pc.calAinHasad(0);
						pc.setAccountName("");
						pc.noPlayerCK = true;
						pc.setNetConnection(null);	
						pc.getMap().setPassable(pc.getLocation(), false);
												
						pc.clearSkillMastery();
						L1World.getInstance().storeObject(pc);
						L1World.getInstance().addVisibleObject(pc);
						
						if (shopRs.getInt("GfxId") != 0) {
							L1PolyMorph.doPoly(pc, shopRs.getInt("GfxId"), 0, L1PolyMorph.MORPH_BY_ITEMMAGIC);
						}
					}
					
					items(pc);
					
					pstm = con.prepareStatement("SELECT * FROM autoshopitemlist WHERE char_name = ? and count = '1'");
					pstm.setString(1, charName);
					itemRs = pstm.executeQuery();
					
					byte[] Chat = null;
					if (!shopRs.getString("sellmsg").trim().equalsIgnoreCase("")) {
						Chat = stringToByte(shopRs.getString("sellmsg").trim());// + shopRs.getString("buymsg"));
					}
					
					ArrayList<L1PrivateShopSellList> sellList = pc.getSellList();
					ArrayList<L1PrivateShopBuyList> buyList = pc.getBuyList();
					sellList.clear();
					buyList.clear();
					
					for (L1ItemInstance item : pc.getInventory().getItems()) {						
						pc.getInventory().removeItem(item);
					}	
					
					pc.getInventory().storeItem(40308, 10000);
					
					while (itemRs.next()) {		
						if (itemRs.getInt("buy_totalcount") < 0) {
							if (itemRs.getInt("itemenchant") > -1) {								
								pc.getInventory().storeItem(itemRs.getInt("itemid"), itemRs.getInt("sell_totalcount"), itemRs.getInt("itemenchant"));
							} else {
								pc.getInventory().storeItem(itemRs.getInt("itemid"), itemRs.getInt("sell_totalcount"));
							} 							
							for (L1ItemInstance item : pc.getInventory().getItems()) {							
								if (item.getItemId() != 40308 && !item.isIdentified()) {
									item.setIdentified(true);
									pc.getInventory().saveItem(item, L1PcInventory.COL_IS_ID);
								}
							}
							L1PrivateShopSellList[] sellItemList = pc.getSellList().toArray(new L1PrivateShopSellList[pc.getSellList().size()]);
							boolean sellCheck = false;
							for (L1ItemInstance item : pc.getInventory().getItems()) {
								for (int i =0; i < sellItemList.length; i++) {
									if (item.getId() == sellItemList[i].getItemObjectId()) {
										sellCheck = true;
										break;
									} else { 
										sellCheck = false;
									}
								}
								
								if (item.getItemId() != 40308 && !sellCheck) {	
									L1PrivateShopSellList sell = new L1PrivateShopSellList();									
									sell.setItemObjectId(item.getId());
									sell.setSellPrice(itemRs.getInt("sell_price"));
									sell.setSellTotalCount(itemRs.getInt("sell_totalcount"));
									sellList.add(sell);
								}
							}
						} else if (itemRs.getInt("sell_totalcount") < 0) {
							if (itemRs.getInt("itemenchant") > -1) {							
								pc.getInventory().storeItem(itemRs.getInt("itemid"), 1, itemRs.getInt("itemenchant"));
							} else {
								pc.getInventory().storeItem(itemRs.getInt("itemid"), 1);
							} 
							for (L1ItemInstance item : pc.getInventory().getItems()) {							
								if (item.getItemId() != 40308 && !item.isIdentified()) {
									item.setIdentified(true);
									pc.getInventory().saveItem(item, L1PcInventory.COL_IS_ID);
								}
							}
							L1PrivateShopBuyList[] buyItemList = pc.getBuyList().toArray(new L1PrivateShopBuyList[pc.getBuyList().size()]);
							boolean buyCheck = false;
							for (L1ItemInstance item : pc.getInventory().getItems()) {		
								for (int i =0; i < buyItemList.length; i++) {
									if (item.getId() == buyItemList[i].getItemObjectId()) {
										buyCheck = true;
										break;
									} else { 
										buyCheck = false;
									}
								}
								if (item.getItemId() != 40308 && !buyCheck) {	
									if (item.getItemId() != 40308) { 
										L1PrivateShopBuyList buy = new L1PrivateShopBuyList();						
										buy.setItemObjectId(item.getId());									
										buy.setBuyPrice(itemRs.getInt("buy_price"));
										buy.setBuyTotalCount(itemRs.getInt("buy_totalcount"));
										buyList.add(buy);
									}
								}
							}
						}
					}	
					
					if (Chat != null) {						
						pc.setShopChat(Chat);
					}
					pc.setPrivateShop(true);
					pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, Chat));
					Broadcaster.broadcastPacket(pc, new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, Chat));	
					
					autoshop = shopManager.getShopPlayer(charName);
					if (autoshop == null) {
						autoshop = shopManager.makeAutoShop(pc);
						shopManager.register(autoshop);	
					} 
					autoshoptable.put(pc.getId(), pc);
				} catch (Exception e) {
					System.out.println("오토상점 로딩중 오류!");
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(shopRs);
			SQLUtil.close(itemRs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void ReloadAutoPcShop(L1PcInstance shop) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet shopRs = null;
		ResultSet itemRs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM autoshoplist where char_name = ? and count = '1'");
			pstm.setString(1, shop.getName());
			shopRs = pstm.executeQuery();
			
			AutoShopManager shopManager = AutoShopManager.getInstance(); 
			AutoShop autoshop = null;
			L1PcInstance pc = null;				
			
			while (shopRs.next()) {
				try {
					String charName = shopRs.getString("char_name");
																		
					pc = L1World.getInstance().getPlayer(charName); 	
					
					if (pc != null) {
						pc.setPrivateShop(false);
						pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						Broadcaster.broadcastPacket(pc, new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
						//Thread.sleep(1000);
						pc.logout();					
						pc = null;
					}
					
					if (pc == null) {
						pc = new L1PcInstance();
						
						pc.setId(ObjectIdFactory.getInstance().nextId());
						pc.setName(shopRs.getString("char_name"));						
						pc.set_sex(shopRs.getInt("Sex"));	// 0~1
						pc.setType(shopRs.getInt("Type"));	
						
						if (pc.get_sex() == 0) {
							pc.setClassId(C_CreateNewCharacter.MALE_LIST[pc.getType()]);
						}
						else {
							pc.setClassId(C_CreateNewCharacter.FEMALE_LIST[pc.getType()]);
						}
						
						pc.getGfxId().setTempCharGfx(pc.getClassId());
						pc.getGfxId().setGfxId(pc.getClassId());
						
						pc.getMoveState().setHeading(shopRs.getInt("Heading"));
						pc.setLawful(shopRs.getInt("Lawful"));
						pc.setTitle(shopRs.getString("Title"));
						pc.setX(shopRs.getInt("LocX"));
						pc.setY(shopRs.getInt("LocY"));		
						pc.setMap(shopRs.getShort("MapID"));
						pc.setShopStep(shopRs.getInt("step"));
						
						pc.setHighLevel(1);
						pc.getAbility().setBaseStr(20);
						pc.getAbility().setBaseDex(20);
						pc.getAbility().setBaseCon(20);
						pc.getAbility().setBaseWis(20);
						pc.getAbility().setBaseCha(20);
						pc.getAbility().setBaseInt(20);	
						pc.addBaseMaxHp((short)200);
						pc.setCurrentHp(pc.getMaxHp());
						pc.addBaseMaxMp((short)20);
						pc.setCurrentMp(pc.getMaxMp());
						pc.resetBaseAc();
						pc.setClanRank(0);
						pc.setClanid(0);
						pc.setClanname("");
						pc.set_food(39); // 17%
						pc.setGm(false);
						pc.setMonitor(false);
						pc.setGmInvis(false);
						pc.setExp(0);
						pc.setActionStatus(0);
						pc.setAccessLevel((short) 0);
						pc.getAbility().setBonusAbility(0);
						pc.resetBaseMr();
						pc.setElfAttr(0);
						pc.set_PKcount(0);
						pc.setExpRes(0);
						pc.setPartnerId(0);
						pc.setOnlineStatus(0);
						pc.setHomeTownId(0);
						pc.setContribution(0);
						pc.setBanned(false);
						pc.setKarma(0);
						pc.setReturnStat(0);
						pc.calAinHasad(0);
						pc.setAccountName("");				
						pc.noPlayerCK = true;
						pc.setNetConnection(null);	
												
						pc.clearSkillMastery();
						L1World.getInstance().storeObject(pc);
						L1World.getInstance().addVisibleObject(pc);
						
						if (shopRs.getInt("GfxId") != 0) {
							L1PolyMorph.doPoly(pc, shopRs.getInt("GfxId"), 0, L1PolyMorph.MORPH_BY_ITEMMAGIC);
						}
					}
					
					items(pc);
					
					pstm = con.prepareStatement("SELECT * FROM autoshopitemlist WHERE char_name = ? and count = '1'");
					pstm.setString(1, charName);
					itemRs = pstm.executeQuery();
					
					byte[] Chat = null;
					if (!shopRs.getString("sellmsg").trim().equalsIgnoreCase("")) {
						Chat = stringToByte(shopRs.getString("sellmsg"));// + shopRs.getString("buymsg"));
					}
					
					ArrayList<L1PrivateShopSellList> sellList = pc.getSellList();
					ArrayList<L1PrivateShopBuyList> buyList = pc.getBuyList();
					sellList.clear();
					buyList.clear();
					
					for (L1ItemInstance item : pc.getInventory().getItems()) {						
						pc.getInventory().removeItem(item);
					}	
					
					pc.getInventory().storeItem(40308, 1900000000);
					
					while (itemRs.next()) {		
						if (itemRs.getInt("buy_totalcount") < 0) {
							if (itemRs.getInt("itemenchant") > -1) {								
								pc.getInventory().storeItem(itemRs.getInt("itemid"), itemRs.getInt("sell_totalcount"), itemRs.getInt("itemenchant"));
							} else {
								pc.getInventory().storeItem(itemRs.getInt("itemid"), itemRs.getInt("sell_totalcount"));
							} 							
							for (L1ItemInstance item : pc.getInventory().getItems()) {							
								if (item.getItemId() != 40308 && !item.isIdentified()) {
									item.setIdentified(true);
									pc.getInventory().saveItem(item, L1PcInventory.COL_IS_ID);
								}
							}
							L1PrivateShopSellList[] sellItemList = pc.getSellList().toArray(new L1PrivateShopSellList[pc.getSellList().size()]);
							boolean sellCheck = false;
							for (L1ItemInstance item : pc.getInventory().getItems()) {
								for (int i =0; i < sellItemList.length; i++) {
									if (item.getId() == sellItemList[i].getItemObjectId()) {
										sellCheck = true;
										break;
									} else { 
										sellCheck = false;
									}
								}
								
								if (item.getItemId() != 40308 && !sellCheck) {	
									L1PrivateShopSellList sell = new L1PrivateShopSellList();									
									sell.setItemObjectId(item.getId());
									sell.setSellPrice(itemRs.getInt("sell_price"));
									sell.setSellTotalCount(itemRs.getInt("sell_totalcount"));
									sellList.add(sell);
								}
							}
						} else if (itemRs.getInt("sell_totalcount") < 0) {
							if (itemRs.getInt("itemenchant") > -1) {							
								pc.getInventory().storeItem(itemRs.getInt("itemid"), 1, itemRs.getInt("itemenchant"));
							} else {
								pc.getInventory().storeItem(itemRs.getInt("itemid"), 1);
							} 
							for (L1ItemInstance item : pc.getInventory().getItems()) {							
								if (item.getItemId() != 40308 && !item.isIdentified()) {
									item.setIdentified(true);
									pc.getInventory().saveItem(item, L1PcInventory.COL_IS_ID);
								}
							}
							L1PrivateShopBuyList[] buyItemList = pc.getBuyList().toArray(new L1PrivateShopBuyList[pc.getBuyList().size()]);
							boolean buyCheck = false;
							for (L1ItemInstance item : pc.getInventory().getItems()) {		
								for (int i =0; i < buyItemList.length; i++) {
									if (item.getId() == buyItemList[i].getItemObjectId()) {
										buyCheck = true;
										break;
									} else { 
										buyCheck = false;
									}
								}
								if (item.getItemId() != 40308 && !buyCheck) {	
									if (item.getItemId() != 40308) { 
										L1PrivateShopBuyList buy = new L1PrivateShopBuyList();						
										buy.setItemObjectId(item.getId());									
										buy.setBuyPrice(itemRs.getInt("buy_price"));
										buy.setBuyTotalCount(itemRs.getInt("buy_totalcount"));
										buyList.add(buy);
									}
								}
							}
						}
					}	
					
					if (Chat != null) {						
						pc.setShopChat(Chat);
					}
					pc.setPrivateShop(true);
					pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, Chat));
					Broadcaster.broadcastPacket(pc, new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, Chat));	
					
					autoshop = shopManager.getShopPlayer(charName);
					if (autoshop == null) {
						autoshop = shopManager.makeAutoShop(pc);
						shopManager.register(autoshop);	
					} 
					autoshoptable.put(pc.getId(), pc);
				} catch (Exception e) {
					System.out.println("오토상점 로딩중 오류!");
					e.printStackTrace();
				}
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} catch (SecurityException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(shopRs);
			SQLUtil.close(itemRs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void InsertAutoPcShop(L1PcInstance pc, String message) {		
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			int i = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO autoshoplist SET char_name=?,note=?,count=?,GfxId=?,Sex=?,Type=?,Heading=?,Lawful=?,Title=?,LocX=?,LocY=?,MapID=?,SellMsg=?,BuyMsg=?");
			pstm.setString(++i, pc.getName());
			pstm.setString(++i, "");
			pstm.setInt(++i, 1);
			pstm.setInt(++i, 0);
			pstm.setInt(++i, pc.get_sex());
			pstm.setInt(++i, pc.getType());
			pstm.setInt(++i, pc.getMoveState().getHeading());
			pstm.setInt(++i, pc.getLawful());
			pstm.setString(++i, pc.getTitle());
			pstm.setInt(++i, pc.getX());
			pstm.setInt(++i, pc.getY());
			pstm.setInt(++i, pc.getMapId());
			pstm.setString(++i, message);
			pstm.setString(++i, "");
			pstm.execute();			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void UpdateAutoPcShop(L1PcInstance pc, String message) {		
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			int i = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE autoshoplist SET note=?,count=?,GfxId=?,Sex=?,Type=?,Heading=?,Lawful=?,Title=?,LocX=?,LocY=?,MapID=?,SellMsg=?,BuyMsg=? where char_name=?");			
			pstm.setString(++i, "");
			pstm.setInt(++i, 1);
			pstm.setInt(++i, 0);
			pstm.setInt(++i, pc.get_sex());
			pstm.setInt(++i, pc.getType());
			pstm.setInt(++i, pc.getMoveState().getHeading());
			pstm.setInt(++i, pc.getLawful());
			pstm.setString(++i, pc.getTitle());
			pstm.setInt(++i, pc.getX());
			pstm.setInt(++i, pc.getY());
			pstm.setInt(++i, pc.getMapId());
			pstm.setString(++i, message);
			pstm.setString(++i, "");
			pstm.setString(++i, pc.getName());
			pstm.execute();			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void UpdateAutoPcShopView(String charName, int count) {		
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			int i = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE autoshoplist SET count=? where char_name=?");						
			pstm.setInt(++i, count);
			pstm.setString(++i, charName);
			pstm.execute();			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void ShopAddItem(String charName, L1ItemInstance item, int type, int price) {		
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			int i = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO autoshopitemlist SET char_name=?,count=?,itemid=?,itemname=?,itemenchant=?,sell_totalcount=?,sell_price=?,sell_count=?,buy_totalcount=?,buy_price=?,buy_count=?");
			pstm.setString(++i, charName);
			pstm.setInt(++i, 1);
			pstm.setInt(++i, item.getItemId());
			pstm.setString(++i, item.getName());
			if (type == 0) {
				pstm.setInt(++i, item.getEnchantLevel());
				pstm.setInt(++i, 1);
				pstm.setInt(++i, price);
				pstm.setInt(++i, 0);
				pstm.setInt(++i, -1);
				pstm.setInt(++i, -1);
				pstm.setInt(++i, 0);
			} else if (type == 1) {
				pstm.setInt(++i, item.getEnchantLevel());
				pstm.setInt(++i, -1);
				pstm.setInt(++i, -1);
				pstm.setInt(++i, 0);
				pstm.setInt(++i, 1);
				pstm.setInt(++i, price);
				pstm.setInt(++i, 0);
			}
			pstm.execute();			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void ShopDelItem(String charName, L1ItemInstance item) {		
		Connection con = null;
		PreparedStatement pstm = null;
		
		try {
			int i = 0;
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM autoshopitemlist where char_name=? and itemid=? and itemenchant=?");
			pstm.setString(++i, charName);
			pstm.setInt(++i, item.getItemId());		
			pstm.setInt(++i, item.getEnchantLevel());	
			pstm.execute();			
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public static boolean doesCharNameExist(String name) {
		boolean result = true;
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT char_name FROM autoshoplist WHERE char_name=?");
			pstm.setString(1, name);
			rs = pstm.executeQuery();
			result = rs.next();
		} catch (SQLException e) {
			_log.warning("could not check existing charname:" + e.getMessage());
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return result;
	}
	
	private byte[] stringToByte(String str) {
		int strLen = str.length();
		byte[] cVal = new byte[strLen];
		cVal = str.getBytes();

		return cVal;
	}
	
	public String byteToString(byte[] sVal) {
		StringBuffer st = new StringBuffer();
		String str = new String(sVal);
		str.toString();

		return str;
	}
	
	private void items(L1PcInstance pc) {
		// DB로부터 캐릭터와 창고의 아이템을 읽어들인다
		CharacterTable.getInstance().restoreInventory(pc);
		pc.sendPackets(new S_InvList(pc));
	}

	public void reload(int step) {
		/*
		AutoShopTable oldInstance = _instance;
		_instance = new AutoShopTable();
		if (oldInstance != null)
			oldInstance.autoshoptable.clear();
		*/
		fillSpawnTable(step);
	}
	
	public int isAutoShop(int id) {
		L1PcInstance pc = autoshoptable.get(id);
		
		if (pc == null) {
			return 0;
		}
		
		return pc.getId();
	}
	
	public void removeAutoShop(int id) {
		autoshoptable.remove(id);
	}
	
	public int getAutoShopCount() {
		return autoshoptable.size();
	}
	
	public L1Shop get(int npcId) {
		return _npcShops.get(npcId);
	}
	
	public ArrayList<L1NpcShop> getList() {
		return npcShoplist;
	}

	public boolean getNpc(String name) {
		for (L1NpcShop n : npcShoplist) {
			if (n.getName().equalsIgnoreCase(name))
				return true;
		}
		return false;
	}
}
