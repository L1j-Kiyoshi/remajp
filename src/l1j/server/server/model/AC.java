package l1j.server.server.model;

import l1j.server.server.utils.IntRange;

public class AC {
	private int ac = 0;
	private int baseAc = 0;
	private int WisAc = 0;
	private L1Character _cha;

	public AC(L1Character cha) {
		_cha = cha;
	}
	
	public L1Character getOwner() {
		return _cha;
	}
	
	public int getAc() {
		return ac + WisAc;
	}

	public void wisacreset(int i) {
		WisAc = i;
	}

	public void addAc(int i) {
		setAc(baseAc + i);
	}

	public void setAc(int i) {
		baseAc = i;
		ac = IntRange.ensure(i, -255, 127);
	}

}
