package l1j.server.MJInstanceSystem.MJLFC.Compensate;

import l1j.server.server.model.Instance.L1PcInstance;

public class MJLFCItemCompensate implements MJLFCCompensate{
	private int _partition;
	private int _itemId;
	private int _itemQuantity;
	private int _level;
	@Override
	public void setPartition(int i){
		_partition = i;
	}
	
	@Override
	public int getPartition(){
		return _partition;
	}
	
	@Override
	public void setIdentity(int i){
		_itemId = i;
	}
	
	@Override
	public void setQuantity(int i){
		_itemQuantity = i;
	}
	
	@Override
	public void setLevel(int i){
		_level = i;
	}
	
	@Override
	public void compensate(L1PcInstance pc){
		pc.getInventory().storeItem(_itemId, _itemQuantity, _level);
	}
}
