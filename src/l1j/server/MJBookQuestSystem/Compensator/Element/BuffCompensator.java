package l1j.server.MJBookQuestSystem.Compensator.Element;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_InventoryIcon;

public class BuffCompensator implements CompensatorElement{
	private int 	_buffId;
	private int 	_time;
	private int		_iconId;
	private int		_msgId;
	
	public BuffCompensator(int buffId, int time, int iconId, int msgId){
		_buffId = buffId;
		_time	= time;
		_iconId = iconId;
		_msgId	= msgId;
	}
	
	@Override
	public void compensate(L1PcInstance pc){
		if(_buffId <= 0)
			return;
		
		if(pc.hasSkillEffect(_buffId))
			pc.removeSkillEffect(_buffId);
		/*pc.setSkillEffect(_buffId, _time * 1000);*/
		if(_iconId > 0 && _msgId > 0)
			pc.sendPackets(new S_InventoryIcon(_iconId, true, _msgId, _time));
	}
}
