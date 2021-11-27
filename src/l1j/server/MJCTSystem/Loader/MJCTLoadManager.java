package l1j.server.MJCTSystem.Loader;

import java.util.ArrayDeque;

import l1j.server.MJCTSystem.MJCTObject;
//import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
/** 
 * MJCTCharInfo
 * MJSoft Character TradeSystem - Load Manager
 * CTSystem managed class.
 * made by mjsoft, 2016.
 **/
public class MJCTLoadManager {
	public static final int CTSYSTEM_STORE_ID = 3000468;	
	public static final int CTSYSTEM_LOAD_ID = 3000469;
	
	private static MJCTLoadManager _instance;
	public static MJCTLoadManager getInstance(){
		if(_instance == null)
			_instance = new MJCTLoadManager();
		return _instance;
	}
	
	private static ArrayDeque<String> parseToStringArray(String s){
		String[] arr 	= s.split(" ");
		int size		= arr.length;
		ArrayDeque<String> argsQ = new ArrayDeque<String>(size);
		for(int i=0; i<size; i++){
			try{
				argsQ.offer(arr[i]);
			}catch(Exception e){
				break;
			}
		}
		return argsQ;
	}
	
	private static final S_SystemMessage _basicMenus = new S_SystemMessage(".구슬" + "[1. 캐릭터정보] [2. 인벤토리] [3. 스킬]" + "[캐릭터 이름]\n"
			+ "(EX1:.구슬 1 메티스, EX2:.구슬 2 메티스, EX3:.구슬 3 메티스)");
	public static void commands(L1PcInstance pc, String param){
		try{
			ArrayDeque<String> argsQ = parseToStringArray(param);
			if(argsQ == null || argsQ.isEmpty())
				throw new Exception("");
			
			int 	cmd	= Integer.parseInt(argsQ.poll());
			
			switch(cmd){
			case 1:
				infoAskCommands(pc, argsQ.poll());
				break;
			case 2:
				inventoryAskCommands(pc, argsQ.poll());
				break;
			case 3:
				skillsAskCommands(pc, argsQ.poll());
				break;
			default:
				throw new Exception("");
			}
			
		}catch(Exception e){
			pc.sendPackets(_basicMenus, false);
		}
	}
	
	public static final S_SystemMessage _invalidCharacter = new S_SystemMessage("캐릭터 정보를 찾을 수 없습니다.");
	public static final S_SystemMessage _DontHaveSpell = new S_SystemMessage("캐릭터의 스펠 정보를 찾을 수 없습니다.");
	public static final S_SystemMessage _DontHaveInvItem = new S_SystemMessage("캐릭터의 아이템 정보를 찾을 수 없습니다.");
	public static void infoAskCommands(L1PcInstance pc, String cName){
		MJCTObject obj = MJCTSystemLoader.getInstance().get(cName);
		if(obj == null){
			pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
			return;
		}
		
		if(obj.infoPck != null)
			pc.sendPackets(obj.infoPck, false);
	}
	
	public static void inventoryAskCommands(L1PcInstance pc, String cName){
		MJCTObject obj = MJCTSystemLoader.getInstance().get(cName);
		if(obj == null){
			pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
			return;
		}
		
		if(obj.invPck != null)
			pc.sendPackets(obj.invPck, false);
	}
	
	public static void skillsAskCommands(L1PcInstance pc, String cName){
		MJCTObject obj = MJCTSystemLoader.getInstance().get(cName);
		if(obj == null){
			pc.sendPackets(MJCTLoadManager._invalidCharacter, false);
			return;
		}
		
		if(obj.spPck != null)
			pc.sendPackets(obj.spPck, false);
	}
	
	private MJCTLoadManager(){}
	
	public void load(){
		MJCTSpellLoader.getInstance();
		MJCTSystemLoader.getInstance();
	}
	
	public void  release(){
		MJCTSpellLoader.release();
		MJCTSystemLoader.release();
	}
}
