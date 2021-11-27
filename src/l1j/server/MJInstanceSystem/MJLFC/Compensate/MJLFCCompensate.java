package l1j.server.MJInstanceSystem.MJLFC.Compensate;

import l1j.server.server.model.Instance.L1PcInstance;

public interface MJLFCCompensate {
	public void setPartition(int i);
	public int 	getPartition();
	public void setIdentity(int i);
	public void setQuantity(int i);
	public void setLevel(int i);
	public void compensate(L1PcInstance pc);
}
