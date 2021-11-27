package l1j.server.GameSystem.NpcTradeShop;

public class ShopItem {

	public ShopItem() {
	}

	private int _npcid;

	public int getNpcId() {
		return _npcid;
	}

	public void setNpcId(int i) {
		_npcid = i;
	}

	private int _x;

	public int getX() {
		return _x;
	}

	public void setX(int i) {
		_x = i;
	}

	private int _y;

	public int getY() {
		return _y;
	}

	public void setY(int i) {
		_y = i;
	}

	private short _mapid;

	public short getMapId() {
		return _mapid;
	}

	public void setMapId(short i) {
		_mapid = i;
	}

	private byte _heading;

	public byte getHeading() {
		return _heading;
	}

	public void setHeading(byte i) {
		_heading = i;
	}

	private String _title;

	public String getTitle() {
		return _title;
	}

	public void setTitle(String s) {
		_title = s;
	}

	private int _price;

	public int getPrice() {
		return _price;
	}

	public void setPrice(int i) {
		_price = i;
	}

	private int _itemId;

	public int getItemId() {
		return _itemId;
	}

	public void setItemId(int i) {
		_itemId = i;
	}

	private int _enchant;

	public int getEnchant() {
		return _enchant;
	}

	public void setEnchant(int i) {
		_enchant = i;
	}

	private String _msg;

	public String getMsg() {
		return _msg;
	}

	public void setMsg(String s) {
		_msg = s;
	}
}
