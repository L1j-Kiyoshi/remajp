package l1j.server.MJTemplate.MJArrangeHelper;
/** 
 * MJArrangeHelper
 * made by mjsoft, 2017.
 **/
public class MJArrangeHelper {
	public static void cleanup(Object[] arr){
		if(arr != null){
			for(int i = arr.length - 1; i>=0; --i) 
				arr[i] = null;
			arr = null;
		}
	}
	
	public static <T> void setArrayValues(T[] arr, int start, int end, T val){
		for(int i=start; i<=end; ++i)
			arr[i] = val;
	}
}
