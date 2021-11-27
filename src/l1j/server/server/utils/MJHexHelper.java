package l1j.server.server.utils;

public class MJHexHelper {
	public static String toString(byte[] data, int len){
		StringBuilder sb 	= new StringBuilder();
		int cnt				= 0;
		for(int i = 0; i < len; i++){
			if(cnt % 16 == 0)
				sb.append(String.format("%04X : ", i));
			sb.append(String.format("%02X ", data[i] & 0xff));
			cnt++;
			
			if(cnt == 16){
				sb.append("\t");
				int p = i - 15;
				for(int j = 0; j < 16; j++)
					sb.append(toHexChar(data[p++]));
				sb.append("\n");
				cnt = 0;
			}
		}
		
		int rest = len % 16;
		if(rest > 0){
			for(int i=0; i< 17 - rest; i++)
				sb.append("   ");
			
			int p = len - rest;
			for(int j=0; j<rest; j++)
				sb.append(toHexChar(data[p++]));
		}
		sb.append("\n");
		return sb.toString();
	}
	
	public static char toHexChar(int i){
		if (i > 0x1f && i < 0x80)
			return (char)i;
		else
			return '.';
	}
	
	public static int getTxtToBytesLength(String s){
		int result	= 1;
		int size	= s.length();
		for(int i=0; i<size; i++){
			if(s.charAt(i) >= 0x7F)
				result += 2;
			else
				result++;
		}
		return result;
	}
}
