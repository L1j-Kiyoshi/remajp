package l1j.server.GameSystem.Dungeon;

import java.util.ArrayList;

import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class DungeonInfo {
	public int DungeonLeadt;
	
	public int isDungeonLeadt() {
		return DungeonLeadt;
	}
	
	public void setDungeonLeadt(int Object) {
		DungeonLeadt = Object;
	}
	
	public ArrayList<UserList> DungeonList = new ArrayList<UserList>();
	
	public static class UserList {
		public int _Object;
		public String _UserName;
		public L1PcInstance _UserPc;
		
		public UserList(int Object, String UserName, L1PcInstance Pc){
			_Object = Object;
			_UserName = UserName;
			_UserPc = Pc;
		}
	}
	
	public UserList isUser(L1PcInstance Pc) {
		for(UserList User : DungeonList){
			if(User._Object == Pc.getId()) 
				return User;
		}
		return null;
	}
		
	public void setUser(L1PcInstance Pc) {
		UserList User = isUser(Pc);
		if(!DungeonList.contains(User)){
			DungeonList.add(new UserList(Pc.getId(), Pc.getName(), Pc));
		}else DungeonList.remove(User);
	}
	
	public L1PcInstance isDungeonInfoCheckUset(int ObjectId) {
		L1PcInstance Pc;
		for(UserList User : DungeonList){
			Pc = L1World.getInstance().getPlayer(User._UserName);
			if(Pc != null) return Pc;
		}
		return null;
	}
	
	public ArrayList<L1PcInstance> isDungeonInfoUset() {
		ArrayList<L1PcInstance> PcList = new ArrayList<L1PcInstance>();
		L1PcInstance Pc;
		for(UserList User : DungeonList){
			Pc = L1World.getInstance().getPlayer(User._UserName);
			if(Pc != null) PcList.add(Pc);
		}
		return PcList;
	}
	
	public boolean isDungeonInfoCheck() {
		for(L1PcInstance PcList : isDungeonInfoUset()){
			if(!PcList.isDungeonInfoCheck()) return true;
		}
		return false;
	}
	
	public boolean isDungeonInfoCheck(L1PcInstance PcList) {
		return PcList.isDungeonInfoCheck();
	}
	
	public void setDungeonInfoCheck(L1PcInstance PcList, boolean b) {
		PcList.setDungeonInfoCheck(b);
	}
	
	public void isDungeonReady() {
		for(L1PcInstance PcList : isDungeonInfoUset()){
			if(DungeonLeadt == PcList.getId()) continue;
			if(PcList.isDungeonInfoCheck())
			   PcList.setDungeonInfoCheck(false);
		}
	}
	
	public String	Title;
	public int 		RoomNumber;
	public int		TypeNumber;
	public int		Division;
	public int		Type;
	public int		Level;
	public int		Adena;
	public int		MinSize;
	public int		MaxSize;
	public int 		Open;
	public String	OpenPassword;
	public boolean 	InPlaygame = false;
}