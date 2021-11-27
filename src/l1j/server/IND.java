package l1j.server;

import l1j.server.GameSystem.Dungeon.DungeonInfo;

public class IND {
	public int _type = 0;
	public String _pcname = null;
	public DungeonInfo _dungeonInfo;

	public IND(String pcname, int type) {
		_pcname = pcname;
		_type = type;
	}
	
	/** 글로 디오 인던 전용 */
	public IND(String pcname, int type, DungeonInfo DungeonInfo) {
		_pcname = pcname;
		_type = type;
		_dungeonInfo = DungeonInfo;
	}
}