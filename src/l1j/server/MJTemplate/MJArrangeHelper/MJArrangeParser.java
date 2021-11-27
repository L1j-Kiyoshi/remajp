package l1j.server.MJTemplate.MJArrangeHelper;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/** 
 * MJArrangeParser
 * made by mjsoft, 2017.
 **/
public class MJArrangeParser {
	public static MJArrangeParsee<?> parsing(String s, String tok, MJArrangeParsee<?> parsee){
		if(s == null || s.equals(""))
			return null;
		
		String[] arr 	= s.split(tok);
		int size		= arr.length;
		parsee.ready(size);
		for(int i=size - 1; i>=0; --i){
			try{
				parsee.parse(i, arr[i]);
			}catch(Exception e){
				break;
			}
		}
		
		return parsee;
	}
	
	public static MJArrangeParsee<?> parsing(NodeList nodes, MJArrangeParsee<?> parsee){
		int size = nodes.getLength();
		parsee.ready(size);
		for(int i=size - 1; i>=0; --i){
			try{
				parsee.parse(i, ((Element)nodes.item(i)).getTextContent());
			}catch(Exception e){
				break;
			}
		}
		return parsee;
	}
}
