package l1j.server.GameSystem.SupportSystem;

public class L1SupportMap{
	private String _Name;
	private int _mapid;
	private double _expRate;
	private double _Adenachance;

	public int getMapId(){
		return _mapid;
	}
	public void setMapId(int id){
		_mapid = id;
	}
		
	public String getName(){
		return _Name;
	}
	public void setName(String name){
		_Name = name;
	}
	
	public double getExpRate(){
		return _expRate;
	}
	public void setExpRate(double i){
		_expRate = i;
	}
	
	public double getAdenaChance(){
		return _Adenachance;
	}
	public void setAdenaChance(double i){
		_Adenachance = i;
	}

}