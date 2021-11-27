package l1j.server.server.datatables;

import java.sql.Connection; 
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.templates.Craft;

public class CraftTable {

	private static CraftTable instance;

	public static CraftTable getInstance() {
		if (instance == null) {
			instance = new CraftTable();
		}
		return instance;
	}

	private CraftTable() {
		store_item();
	}
	
	public void reload() {
		CraftTable in = instance;
		instance = new CraftTable();
		if (in != null) {
			in.list.clear();
		}
	}
	
	private ConcurrentHashMap<Integer, Craft> list = new ConcurrentHashMap<Integer, Craft>();

	public void store_item() {
		try {
			Connection con = L1DatabaseFactory.getInstance().getConnection();
			PreparedStatement pst = con.prepareStatement("SELECT * FROM craft_list");
			ResultSet rst = pst.executeQuery();
			
			Craft craft;
			
			while (rst.next()) {
				craft = new Craft();
				craft.set_number(rst.getInt("number"));
				craft.set_action(rst.getInt("action"));
				craft.set_descid(rst.getInt("descid"));
				craft.set_random(rst.getInt("random"));
				
				craft.set_store_itemid(rst.getInt("store_itemid"));
				craft.set_store_count(rst.getInt("store_count"));
				craft.set_store_enchant(rst.getInt("store_enchant"));
				
				craft.set_consume(rst.getInt("consume"));
				craft.set_consume_count(rst.getInt("consume_count"));
				
				craft.set_remove1(rst.getInt("remove1"));
				craft.set_remove_count1(rst.getInt("remove_count1"));
				craft.set_remove_enchant1(rst.getInt("remove_enchant1"));				
				craft.set_remove2(rst.getInt("remove2"));
				craft.set_remove_count2(rst.getInt("remove_count2"));
				craft.set_remove_enchant2(rst.getInt("remove_enchant2"));				
				craft.set_remove3(rst.getInt("remove3"));
				craft.set_remove_count3(rst.getInt("remove_count3"));
				craft.set_remove_enchant3(rst.getInt("remove_enchant3"));				
				craft.set_remove4(rst.getInt("remove4"));
				craft.set_remove_count4(rst.getInt("remove_count4"));
				craft.set_remove_enchant4(rst.getInt("remove_enchant4"));				
				craft.set_remove5(rst.getInt("remove5"));
				craft.set_remove_count5(rst.getInt("remove_count5"));
				craft.set_remove_enchant5(rst.getInt("remove_enchant5"));				
				craft.set_remove6(rst.getInt("remove6"));
				craft.set_remove_count6(rst.getInt("remove_count6"));
				craft.set_remove_enchant6(rst.getInt("remove_enchant6"));				
				craft.set_remove7(rst.getInt("remove7"));
				craft.set_remove_count7(rst.getInt("remove_count7"));
				craft.set_remove_enchant7(rst.getInt("remove_enchant7"));				
				craft.set_remove8(rst.getInt("remove8"));
				craft.set_remove_count8(rst.getInt("remove_count8"));
				craft.set_remove_enchant8(rst.getInt("remove_enchant8"));				
				craft.set_remove9(rst.getInt("remove9"));
				craft.set_remove_count9(rst.getInt("remove_count9"));
				craft.set_remove_enchant9(rst.getInt("remove_enchant9"));
				
				list.put(craft.get_number(), craft);
			}

			System.out.println("[CraftTable Data] : " + list.size() + " °³ loading...OK!");
			
			con.close();
			pst.close();
			rst.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Craft get_template(int action, int descid) {
		Craft[] craft = get_template();
		for (int i = 0; i < craft.length; i++) {
			if (craft[i].get_action() == action && craft[i].get_descid() == descid)  return craft[i];
		}
		return null;
	}
	
	public Craft[] get_template() {
		return (Craft[])  list.values().toArray(new Craft[list.size()]);
	}
}