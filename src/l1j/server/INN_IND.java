package l1j.server;

public class INN_IND {
	public int _type = 0;
	public int _objid = 0;
	public int _npcid = 0;
	public int _count = 0;
	public String _pcname = null;

	public INN_IND(String pcname, int objid, int npcid, int type, int count) {
		_pcname = pcname;
		_type = type;
		_objid = objid;
		_npcid = npcid;
		_count = count;
	}
}