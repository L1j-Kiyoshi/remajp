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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import server.system.autoshop.AutoShop;
import server.system.autoshop.AutoShopManager;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.clientpackets.C_CreateNewCharacter;
import l1j.server.server.datatables.AutoShopTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.CommonUtil;
import l1j.server.server.utils.SQLUtil;

public class L1AutoPcShop implements L1CommandExecutor {
	private static Logger _log = Logger.getLogger(L1AutoPcShop.class.getName());

	private L1AutoPcShop() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1AutoPcShop();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		
		try {
			StringTokenizer st = new StringTokenizer(arg);
			String type = st.nextToken();
			
			if(type.equalsIgnoreCase("1")){
				toShopStart(pc, st);
				return;
			}
			
			if(type.equalsIgnoreCase("2")){
				toShopAddItem(pc, st);
				return;
			}
			
			if(type.equalsIgnoreCase("3")){
				toShopDelItem(pc, st);
				return;
			}
			
			if(type.equalsIgnoreCase("4")){
				toShopEnd(pc, st);
				return;
			}
			
			if(type.equalsIgnoreCase("5")){
				toShopMake(pc, st);
				return;
			}
			
			if(type.equalsIgnoreCase("6")){
				toShopUpdate(pc, st);
				return;
			}
			
			if(type.equalsIgnoreCase("7")){
				toShopUpdateView(pc, st);
				return;
			}
			
			if(type.equalsIgnoreCase("8")){
				toShopUpdateHidden(pc, st);
				return;
			}
			
		} catch (Exception e) { }
		pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
		pc.sendPackets(new S_SystemMessage( ".오토상점 [옵션]" ));
		pc.sendPackets(new S_SystemMessage( " [1:상점시작 2:아이템추가 3.아이템삭제 4:상점종료]"));
		pc.sendPackets(new S_SystemMessage( " [5:상점추가 6:상점수정 7:상점배치 8:상점숨김]"));
		pc.sendPackets(new S_SystemMessage("----------------------------------------------------"));
	}
	
	private void toShopStart(L1PcInstance pc, StringTokenizer st) {
		try {
			int step = Integer.parseInt(st.nextToken());
			AutoShopTable.getInstance().reload(step);
			pc.sendPackets(new S_SystemMessage(step + "단계 오토상점을 로딩하였습니다."));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".오토상점 상점시작 [단계]"));
		}
	}
	
	private void toShopAddItem(L1PcInstance pc, StringTokenizer st) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;	
		
		try {
			String charName = st.nextToken();
			int type = Integer.parseInt(st.nextToken());
			String nameid = st.nextToken();
			int itemid = 0;
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
				if (itemid == 0) {
					pc.sendPackets(new S_SystemMessage("해당 아이템이 발견되지 않습니다. "));
					return;
				}
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			int count = 1;
			if (st.hasMoreTokens()) {
				count = Integer.parseInt(st.nextToken());
			}
			int price = 1;
			if (st.hasMoreTokens()) {
				price = Integer.parseInt(st.nextToken());
			}
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM autoshoplist WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			
			if(!rs.next()) {
				pc.sendPackets(new S_SystemMessage(charName + " 아이디가 autoshoplist 테이블에 생성되어 있지 않습니다."));
				return;
			}	
			
			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
				if (temp.isStackable()) {
					item.setEnchantLevel(-1);
					item.setCount(count);
				} else {
					item.setEnchantLevel(enchant);
					item.setCount(1);
				}
				
				AutoShopTable.getInstance().ShopAddItem(charName, item, type, price);
				
				L1PcInstance player = L1World.getInstance().getPlayer(charName);
				if (player != null) {
					AutoShopTable.getInstance().ReloadAutoPcShop(player);
				}
				pc.sendPackets(new S_SystemMessage(charName + " 상점에 " +  item.getName() + " 아이템을 " + price + " 가격에 판매목록을 추가하였습니다."));
			} else {
				pc.sendPackets(new S_SystemMessage("지정 ID의 아이템은 존재하지 않습니다"));
			}
			
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".오토상점 아이템추가 [캐릭터명] [0:판매, 1:구매] [아이템ID,명칭] [인챈] [수량] [가격]"));
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void toShopDelItem(L1PcInstance pc, StringTokenizer st) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;	
		
		try {
			String charName = st.nextToken();			
			String nameid = st.nextToken();
			int itemid = 0;
			try {
				itemid = Integer.parseInt(nameid);
			} catch (NumberFormatException e) {
				itemid = ItemTable.getInstance().findItemIdByNameWithoutSpace(nameid);
				if (itemid == 0) {
					pc.sendPackets(new S_SystemMessage("해당 아이템이 발견되지 않습니다. "));
					return;
				}
			}
			int enchant = 0;
			if (st.hasMoreTokens()) {
				enchant = Integer.parseInt(st.nextToken());
			}
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM autoshoplist WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			
			if(!rs.next()) {
				pc.sendPackets(new S_SystemMessage(charName + " 아이디가 autoshoplist 테이블에 생성되어 있지 않습니다."));
				return;
			}	
			
			L1Item temp = ItemTable.getInstance().getTemplate(itemid);
			if (temp != null) {
				L1ItemInstance item = ItemTable.getInstance().createItem(itemid);
				if (temp.isStackable()) {
					item.setEnchantLevel(-1);
				} else {
					item.setEnchantLevel(enchant);
				}
				
				AutoShopTable.getInstance().ShopDelItem(charName, item);
				
				L1PcInstance player = L1World.getInstance().getPlayer(charName);
				if (player != null) {
					AutoShopTable.getInstance().ReloadAutoPcShop(player);
				}
				pc.sendPackets(new S_SystemMessage(charName + " 상점에 " +  item.getName() + " 아이템을 판매목록에서 삭제하였습니다."));
			} else {
				pc.sendPackets(new S_SystemMessage("지정 ID의 아이템은 존재하지 않습니다"));
			}
			
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".오토상점 아이템삭제 [캐릭터명] [아이템ID,명칭] [인챈]"));
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void toShopEnd(L1PcInstance pc, StringTokenizer st) {
		try {
			int step = Integer.parseInt(st.nextToken());
			for (L1PcInstance player : L1World.getInstance().getAllPlayers()) {	
				try {
					if (player.getShopStep() == step) {
						AutoShopManager shopManager = AutoShopManager.getInstance(); 
						AutoShop autoshop = shopManager.getShopPlayer(player.getName());
						
						if (autoshop != null && AutoShopTable.getInstance().isAutoShop(player.getId()) != 0) {							
							AutoShopTable.getInstance().removeAutoShop(player.getId());
							shopManager.remove(autoshop);	
							autoshop.logout();
							player.logout();							
							Thread.sleep(10);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			pc.sendPackets(new S_SystemMessage(step + "단계 오토상점을 종료하였습니다."));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".오토상점 상점종료 [단계]"));
		}
	}
	
	private void toShopMake(L1PcInstance pc, StringTokenizer st) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;	
		
		try {			
			String charName = st.nextToken();
			int classType = Integer.parseInt(st.nextToken());
			String message = st.nextToken();
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM characters WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				pc.sendPackets(new S_SystemMessage("이미 " + charName + " 아이디가 characters 테이블에 생성되어있습니다."));
				return;
			}
			
			pstm.close();
			rs.close();
			
			pstm = con.prepareStatement("SELECT * FROM autoshoplist WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			
			if(rs.next()) {
				pc.sendPackets(new S_SystemMessage("이미 " + charName + " 아이디가 autoshoplist 테이블에 생성되어있습니다."));
				return;
			}	
			
			L1PcInstance shop = new L1PcInstance();
			
			shop.setId(ObjectIdFactory.getInstance().nextId());
			shop.setName(charName);						
			shop.set_sex(CommonUtil.random(0, 1));	// 0~1
			if (classType == 0) {
				shop.setType(classType);
			} else {
				shop.setType(CommonUtil.random(1, 6));
			}
			
			if (shop.get_sex() == 0) {
				shop.setClassId(C_CreateNewCharacter.MALE_LIST[shop.getType()]);
			}
			else {
				shop.setClassId(C_CreateNewCharacter.FEMALE_LIST[shop.getType()]);
			}
			
			shop.getGfxId().setTempCharGfx(shop.getClassId());
			shop.getGfxId().setGfxId(shop.getClassId());			
			shop.getMoveState().setHeading(pc.getMoveState().getHeading());
			shop.setLawful(CommonUtil.random(0, 32767));
			shop.setTitle("");
			shop.setX(pc.getX());
			shop.setY(pc.getY());		
			shop.setMap(pc.getMapId());	
			
			shop.setHighLevel(1);
			shop.getAbility().setBaseStr(20);
			shop.getAbility().setBaseDex(20);
			shop.getAbility().setBaseCon(20);
			shop.getAbility().setBaseWis(20);
			shop.getAbility().setBaseCha(20);
			shop.getAbility().setBaseInt(20);	
			shop.addBaseMaxHp((short)200);
			shop.setCurrentHp(shop.getMaxHp());
			shop.addBaseMaxMp((short)20);
			shop.setCurrentMp(shop.getMaxMp());
			shop.resetBaseAc();
			shop.setClanRank(0);
			shop.setClanid(0);
			shop.setClanname("");
			shop.set_food(39); // 17%
			shop.setGm(false);
			shop.setMonitor(false);
			shop.setGmInvis(false);
			shop.setExp(0);
			shop.setActionStatus(0);
			shop.setAccessLevel((short) 0);
			shop.getAbility().setBonusAbility(0);
			shop.resetBaseMr();
			shop.setElfAttr(0);
			shop.set_PKcount(0);
			shop.setExpRes(0);
			shop.setPartnerId(0);
			shop.setOnlineStatus(0);
			shop.setHomeTownId(0);
			shop.setContribution(0);
			shop.setBanned(false);
			shop.setKarma(0);
			shop.setReturnStat(0);
			//shop.setGdungeonTime(0);
			shop.calAinHasad(0);
			shop.setAccountName("");			
			
			AutoShopTable.getInstance().InsertAutoPcShop(shop, message);
			pc.sendPackets(new S_SystemMessage(shop.getName() + " 상점을 생성하였습니다."));
			shop = null;
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".오토상점 상점추가 [캐릭터명] [0:군주, 1:랜덤] [상점메세지]"));
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void toShopUpdate(L1PcInstance pc, StringTokenizer st) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;	
		
		try {			
			String charName = st.nextToken();
			int classType = Integer.parseInt(st.nextToken());
			String message = st.nextToken();
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM autoshoplist WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			
			if(!rs.next()) {
				pc.sendPackets(new S_SystemMessage(charName + " 아이디가 autoshoplist 테이블에 생성되어 있지 않습니다."));
				return;
			}	
			
			L1PcInstance shop = new L1PcInstance();
			
			shop.setId(ObjectIdFactory.getInstance().nextId());
			shop.setName(charName);						
			shop.set_sex(CommonUtil.random(0, 1));	// 0~1
			if (classType == 0) {
				shop.setType(classType);
			} else {
				shop.setType(CommonUtil.random(1, 6));
			}
			
			if (shop.get_sex() == 0) {
				shop.setClassId(C_CreateNewCharacter.MALE_LIST[shop.getType()]);
			}
			else {
				shop.setClassId(C_CreateNewCharacter.FEMALE_LIST[shop.getType()]);
			}
			
			shop.getGfxId().setTempCharGfx(shop.getClassId());
			shop.getGfxId().setGfxId(shop.getClassId());			
			shop.getMoveState().setHeading(pc.getMoveState().getHeading());
			shop.setLawful(CommonUtil.random(0, 32767));
			shop.setTitle("");
			shop.setX(pc.getX());
			shop.setY(pc.getY());		
			shop.setMap(pc.getMapId());	
			
			shop.setHighLevel(1);
			shop.getAbility().setBaseStr(20);
			shop.getAbility().setBaseDex(20);
			shop.getAbility().setBaseCon(20);
			shop.getAbility().setBaseWis(20);
			shop.getAbility().setBaseCha(20);
			shop.getAbility().setBaseInt(20);	
			shop.addBaseMaxHp((short)200);
			shop.setCurrentHp(shop.getMaxHp());
			shop.addBaseMaxMp((short)20);
			shop.setCurrentMp(shop.getMaxMp());
			shop.resetBaseAc();
			shop.setClanRank(0);
			shop.setClanid(0);
			shop.setClanname("");
			shop.set_food(39); // 17%
			shop.setGm(false);
			shop.setMonitor(false);
			shop.setGmInvis(false);
			shop.setExp(0);
			shop.setActionStatus(0);
			shop.setAccessLevel((short) 0);
			shop.getAbility().setBonusAbility(0);
			shop.resetBaseMr();
			shop.setElfAttr(0);
			shop.set_PKcount(0);
			shop.setExpRes(0);
			shop.setPartnerId(0);
			shop.setOnlineStatus(0);
			shop.setHomeTownId(0);
			shop.setContribution(0);
			shop.setBanned(false);
			shop.setKarma(0);
			shop.setReturnStat(0);
			//shop.setGdungeonTime(0);
			shop.calAinHasad(0);
			shop.setAccountName("");
			
			AutoShopTable.getInstance().UpdateAutoPcShop(shop, message);
			L1PcInstance player = L1World.getInstance().getPlayer(charName);
			if (player != null) {
				AutoShopTable.getInstance().ReloadAutoPcShop(player);
			}			
			pc.sendPackets(new S_SystemMessage(shop.getName() + " 상점을 수정하였습니다."));
			shop = null;
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".오토상점 상점수정 [캐릭터명] [0:군주, 1:랜덤] [상점메세지]"));
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void toShopUpdateView(L1PcInstance pc, StringTokenizer st) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;	
		try {
			String charName = st.nextToken();
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM autoshoplist WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			
			if(!rs.next()) {
				pc.sendPackets(new S_SystemMessage(charName + " 아이디가 autoshoplist 테이블에 생성되어 있지 않습니다."));
				return;
			}	
			
			AutoShopTable.getInstance().UpdateAutoPcShopView(charName, 1);
			L1PcInstance player = L1World.getInstance().getPlayer(charName);
			if (player != null) {
				AutoShopTable.getInstance().ReloadAutoPcShop(player);
			}			
			pc.sendPackets(new S_SystemMessage(charName + " 상점을 배치하였습니다."));
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	private void toShopUpdateHidden(L1PcInstance pc, StringTokenizer st) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;	
		try {
			String charName = st.nextToken();
			
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM autoshoplist WHERE char_name=?");
			pstm.setString(1, charName);
			rs = pstm.executeQuery();
			
			if(!rs.next()) {
				pc.sendPackets(new S_SystemMessage(charName + " 아이디가 autoshoplist 테이블에 생성되어 있지 않습니다."));
				return;
			}	
			
			AutoShopTable.getInstance().UpdateAutoPcShopView(charName, 0);
			L1PcInstance player = L1World.getInstance().getPlayer(charName);
			if (player != null) {
				try {
					AutoShopManager shopManager = AutoShopManager.getInstance(); 
					AutoShop autoshop = shopManager.getShopPlayer(player.getName());
					
					if (autoshop != null && AutoShopTable.getInstance().isAutoShop(player.getId()) != 0) {							
						AutoShopTable.getInstance().removeAutoShop(player.getId());
						shopManager.remove(autoshop);	
						autoshop.logout();
						player.logout();		
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}			
			pc.sendPackets(new S_SystemMessage(charName + " 상점을 숨겼습니다."));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(".오토상점 상점숨김 [캐릭터명]"));
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
}