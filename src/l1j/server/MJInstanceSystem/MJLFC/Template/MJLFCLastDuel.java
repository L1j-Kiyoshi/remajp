package l1j.server.MJInstanceSystem.MJLFC.Template;

import java.util.ArrayList;
import java.util.logging.Level;

import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.MJInstanceSystem.MJLFC.Creator.MJLFCCreator;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class MJLFCLastDuel extends MJLFCObject{
	public static MJLFCLastDuel createInstance(){
		return new MJLFCLastDuel();
	}
	
	private ArrayList<L1NpcInstance> _boundary;
	
	public MJLFCLastDuel(){
		super();
		_boundary = new ArrayList<L1NpcInstance>();
	}
	
	@Override
	public void init(){
		L1NpcInstance boundary;
		for(int i = 32734; i<32741; i++){
			boundary = spawnBoundary(i, 32863, 0);
			if(boundary != null)
				_boundary.add(boundary);
		}
		super.init();
	}
	
	@Override
	public void run() {
		try{
			waitCount();
			for(L1NpcInstance boundary : _boundary)
				deleteNpc(boundary);
			_boundary.clear();
			_boundary = null;
			L1PcInstance red 	= _red.get(0);
			L1PcInstance blue	= _blue.get(0);
			MJLFCCreator.setInstStatus(red, InstStatus.INST_USERSTATUS_LFC);
			MJLFCCreator.setInstStatus(blue, InstStatus.INST_USERSTATUS_LFC);
			while(_isrun){
				Thread.sleep(1000);
				if(!checkSecond() || red == null || blue == null || red.isDead() || blue.isDead() ||
						red.getMapId() != getCopyMapId() || blue.getMapId() != getCopyMapId()){
					close();
					return;
				}
			}
		}
		catch(Exception e){
			close();
			e.printStackTrace();
			_log.log(Level.SEVERE, "L1MJLfcLastDuel", e);
		}
		return;
	}
	
	@Override
	public void close(){
		super.close();
	}
	
	@Override
	public String getName(){
		return "MJLFCLastDuel";
	}
}
