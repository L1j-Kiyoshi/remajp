package l1j.server.MJDShopSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class MJDShopStorage implements Runnable{
	
	private L1Character 	_owner;
	private boolean		_isDelete;
	
	public MJDShopStorage(L1Character c, boolean isDelete){
		_owner 		= c;
		_isDelete 	= isDelete;
	}
	
	@Override
	public void run() {
		if(_owner == null)
			return;

		try{
			if(_isDelete)
				deleteProcess(_owner);
			else
				updateProcess(_owner);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public static void clearProcess(){
		Connection 				con	= null;
		PreparedStatement 	pstm 	= null;	
		try{
			con	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("delete from character_shop");
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void deleteProcess(L1Character c){
		Connection 				con	= null;
		PreparedStatement 	pstm 	= null;	
		try{
			con	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("delete from character_shop where objid=?");
			pstm.setInt(1, c.getId());
			pstm.executeUpdate();
			c.disposeShopInfo();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void deleteProcess(L1Character c, int itemObjId){
		Connection 				con	= null;
		PreparedStatement 	pstm 	= null;	
		try{
			con	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("delete from character_shop where objid=? AND item_objid=?");
			pstm.setInt(1, c.getId());
			pstm.setInt(2, itemObjId);
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void updateProcess(ArrayList<L1NpcInstance> cList){
		PreparedStatement 	pstm 		= null;	
		Connection				con		= null;
		ArrayList<MJDShopItem> list 	= null;
		ArrayList<MJDShopItem> tmp	= null;
		L1Character 				cha	= null;				
		try{
			int cSize = cList.size();
			con		= L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm 		= createUpdatePstm(con);
			for(int c=0; c<cSize; c++){
				cha = cList.get(c);
				list = new ArrayList<MJDShopItem>(14);
				tmp = cha.getSellings();

				if(tmp != null)	
					list.addAll(tmp);
				
				tmp = cha.getPurchasings();
				if(tmp != null)
					list.addAll(tmp);
					
				int size = list.size();
				if(size <= 0){
					list.clear();
					list = null;
					continue;
				}
				
				MJDShopItem ditem = null;
				for(int i=0; i<size; i++){
					ditem = list.get(i);
					if(ditem == null)
						continue;
					
					setUpdatePstm(pstm, cha, ditem);
					pstm.addBatch();
					pstm.clearParameters();
				}
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		}catch(Exception e){
			try {
				con.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(con != null)
					con.setAutoCommit(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	private static void updateProcess(L1Character c){
		PreparedStatement 	pstm 	= null;	
		Connection				con	= null;
		try{
			
			ArrayList<MJDShopItem> list = new ArrayList<MJDShopItem>(14);
			ArrayList<MJDShopItem> tmp = c.getSellings();

			if(tmp != null)
				list.addAll(tmp);
			
			tmp = c.getPurchasings();
			if(tmp != null)
				list.addAll(tmp);
				
			int size = list.size();
			if(size <= 0)
				return;
	
			con = L1DatabaseFactory.getInstance().getConnection();
			con.setAutoCommit(false);
			pstm 	= createUpdatePstm(con);
			MJDShopItem ditem = null;
			for(int i=0; i<size; i++){
				ditem = list.get(i);
				if(ditem == null)
					continue;
				
				setUpdatePstm(pstm, c, ditem);
				pstm.addBatch();
				pstm.clearParameters();
			}
			pstm.executeBatch();
			pstm.clearBatch();
			con.commit();
		}catch(Exception e){
			try {
				con.rollback();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally{
			try {
				if(con != null)
					con.setAutoCommit(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			SQLUtil.close(pstm, con);
		}
	}
	
	public static void updateProcess(L1Character c, MJDShopItem ditem){
		Connection 				con	= null;
		PreparedStatement 	pstm 	= null;	
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= createUpdatePstm(con);
			setUpdatePstm(pstm, c, ditem);
			pstm.executeUpdate();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			SQLUtil.close(pstm, con);
		}
	}
	
	private static PreparedStatement createUpdatePstm(Connection con) throws Exception{
		return con.prepareStatement(
				"insert into character_shop set objid=?, item_objid=?, char_name=?, item_id=?, item_name=?, count=?, enchant=?, price=?, type=?, iden=?, attr=?, invgfx=?, isUser=? " +
				"on duplicate key update count=?, enchant=?, price=?, type=?, iden=?, attr=?, invgfx=?, isUser=?");
	}
	
	private static void setUpdatePstm(PreparedStatement pstm, L1Character c, MJDShopItem ditem) throws Exception{
		int idx			= 0;
		int iden 		= 0;
		boolean isUser	= c instanceof L1PcInstance;
		
		iden = getPackIden2AppIden(ditem.iden);
		pstm.setInt(++idx, c.getId());
		pstm.setInt(++idx, ditem.objId);
		pstm.setString(++idx, c.getName());
		pstm.setInt(++idx, ditem.itemId);
		pstm.setString(++idx, ditem.name);
		pstm.setInt(++idx, ditem.count);
		pstm.setInt(++idx, ditem.enchant);
		pstm.setInt(++idx, ditem.price);
		pstm.setBoolean(++idx, ditem.isPurchase);
		pstm.setInt(++idx, iden);
		pstm.setInt(++idx, ditem.attr);
		pstm.setInt(++idx, ditem.invId);
		pstm.setBoolean(++idx, isUser);
		// on duplicate key.
		pstm.setInt(++idx, ditem.count);
		pstm.setInt(++idx, ditem.enchant);
		pstm.setInt(++idx, ditem.price);
		pstm.setBoolean(++idx, ditem.isPurchase);
		pstm.setInt(++idx, iden);
		pstm.setInt(++idx, ditem.attr);
		pstm.setInt(++idx, ditem.invId);
		pstm.setBoolean(++idx, isUser);
	}
	
	/** pack iden type -> app center iden. **/
	private static final int[] _pIdenToAIden = new int[]{
		3, 2, 4
	};
	public static int getPackIden2AppIden(int i){
		if(i < 0 || i >= _pIdenToAIden.length)
			return 1;
		
		return _pIdenToAIden[i];
	}
	
	/** app center iden type -> pack iden. **/
	private static final int[] _aIdenToPIden = new int[]{
		-1, -1, 1, 0, 2
	};
	public static int getAppIden2PackIden(int i){
		if(i < 0 || i >= _aIdenToPIden.length)
			return -1;
		
		return _aIdenToPIden[i];
	}
}
