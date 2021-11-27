package l1j.server.GameSystem.Delivery;

import java.sql.Timestamp;

public class Delivery {

	private String char_name;

	public String getName() {
		return char_name;
	}

	public void setName(String name) {
		char_name = name;
	}

	private int item_obj_id;

	public int getItemObjId() {
		return item_obj_id;
	}

	public void setItemObjId(int id) {
		item_obj_id = id;
	}

	private int item_id;

	public int getItemId() {
		return item_id;
	}

	public void setItemId(int id) {
		item_id = id;
	}

	private Timestamp time;

	public Timestamp getTime() {
		return time;
	}

	public void setTime(Timestamp id) {
		time = id;
	}

	private int count;

	public int getCount() {
		return count;
	}

	public void setCount(int id) {
		count = id;
	}

	private int clock_count;

	public int getClockCount() {
		return clock_count;
	}

	public void setClockCount(int count) {
		clock_count = count;
	}

	public void reset() {
		char_name = null;
		time = null;
	}

}
