package l1j.server.MJTemplate.MJArrangeHelper;
/** 
 * MJArrangeParsee
 * made by mjsoft, 2017.
 **/
public interface MJArrangeParsee<T>{
	public MJArrangeParsee<T> init(Class<T> cls);
	public void ready(int size);
	public void parse(int idx, String data);
	public T[] result();
}
