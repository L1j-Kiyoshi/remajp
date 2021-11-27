package l1j.server.MJInstanceSystem.Loader;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJInstanceSystem.MJLFC.MJLFCType;
import l1j.server.MJInstanceSystem.MJLFC.Compensate.MJLFCCompensate;
import l1j.server.MJInstanceSystem.MJLFC.Compensate.MJLFCExpCompensate;
import l1j.server.MJInstanceSystem.MJLFC.Compensate.MJLFCItemCompensate;
import l1j.server.server.utils.SQLUtil;

public class MJLFCCompensateLoader {
	private static MJLFCCompensateLoader _instance;
	public static MJLFCCompensateLoader getInstance(){
		if(_instance == null)
			_instance = new MJLFCCompensateLoader();
		return _instance;
	}
	
	public static void reload(){
		MJLFCCompensateLoader tmp = _instance;
		_instance = new MJLFCCompensateLoader();
		if(tmp != null){
			tmp.clear();
			tmp = null;
		}
	}
	
	public static void release(){
		if(_instance != null){
			_instance.clear();
			_instance = null;
		}
	}
	
	private MJLFCCompensateLoader(){
		Connection 			con 		= null;
		PreparedStatement 	pstm 		= null;
		ResultSet 			rs 			= null;
		MJLFCType			type		= null;
		MJLFCCompensate		compensate 	= null;
		String				field		= null;
		String				sType		= null;
		int					id			= 0;
		try{
			con 	= L1DatabaseFactory.getInstance().getConnection();
			pstm 	= con.prepareStatement("select * from TB_LFCCOMPENSATE");
			rs 		= pstm.executeQuery();
			while(rs.next()){
				field = "LFCID";
				id = rs.getInt(field);
				type = MJLFCTypeLoader.getInstance().get(id);
				if(type == null)
					continue;
				
				field = "TYPE";
				sType = rs.getString(field);
				if(sType == null)
					continue;
				else if(sType.equalsIgnoreCase("exp"))
					compensate = new MJLFCExpCompensate();
				else if(sType.equalsIgnoreCase("item"))
					compensate = new MJLFCItemCompensate();
				else continue;
				
				field = "PARTITION";
				compensate.setPartition(rs.getInt(field));
				field = "IDENTITY";
				compensate.setIdentity(rs.getInt(field));
				field = "QUANTITY";
				compensate.setQuantity(rs.getInt(field));
				field = "LEVEL";
				compensate.setLevel(rs.getInt(field));
				
				type.addCompensates(compensate);
			}
			
		}catch(Exception e){
			StringBuilder sb = new StringBuilder();
			if(type.getName() == null)	sb.append("null");
			else						sb.append(type.getName());
			sb.append("(");
			sb.append(type.getId());
			sb.append(", field : ");
			if(field == null)			sb.append("null");
			else						sb.append(field);
			sb.append(")을(를) 불러들이는데 실패했습니다.");
			System.out.println(sb.toString());
			e.printStackTrace();
		}finally{
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}
	
	public void clear(){
		
	}
}
