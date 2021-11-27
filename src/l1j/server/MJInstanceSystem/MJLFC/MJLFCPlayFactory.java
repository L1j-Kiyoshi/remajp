package l1j.server.MJInstanceSystem.MJLFC;

import l1j.server.MJInstanceSystem.MJLFC.Template.MJLFCObject;

public class MJLFCPlayFactory {
	public static MJLFCObject create(String s){
		MJLFCObject obj = null;
		try{
			Class<?> cls = Class.forName(complementClassName(s));
			obj = (MJLFCObject)cls.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}	
		return obj;
	}
	
	private static String complementClassName(String className){
		if (className.contains(".")) {
			return className;
		}
		if (className.contains(",")) {
			return className;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("l1j.server.MJInstanceSystem.MJLFC.Template.").append(className);
		return sb.toString();
	}
}
