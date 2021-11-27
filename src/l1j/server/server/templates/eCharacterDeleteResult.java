package l1j.server.server.templates;

public enum eCharacterDeleteResult{
	Success(0),
	InvalidPacket(1),
	BMTypeFail(2),
	;
	private int value;
	eCharacterDeleteResult(int val){
		value = val;
	}
	public int toInt(){
		return value;
	}
	public boolean equals(eCharacterDeleteResult v){
		return value == v.value;
	}
	public static eCharacterDeleteResult fromInt(int i){
		switch(i){
		case 0:
			return Success;
		case 1:
			return InvalidPacket;
		case 2:
			return BMTypeFail;
		default:
			throw new IllegalArgumentException(String.format("invalid arguments eResult, %d", i));
		}
	}
}
