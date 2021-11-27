package l1j.server.MJTemplate.MJArrangeHelper;

import java.lang.reflect.Array;

/** 
 * MJAbstractArrangeDefaultParsee
 * for array parser helper.
 * usage : new MJAbstractArrangeDefaultParsee<data type>() -> parse() method realize.
 * made by mjsoft, 2017.
 **/
public abstract class MJAbstractArrangeDefaultParsee<E> implements MJArrangeParsee<E>{
	protected E[] _datas;
	protected Class<E> _cls;
	
	@Override
	public MJArrangeParsee<E> init(Class<E> cls){
		_cls = cls;
		return this;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void ready(int size){
		_datas = (E[]) Array.newInstance(_cls, size);
	}
	
	@Override
	public E[] result(){
		return _datas;
	}
	
	@Override
	public abstract void parse(int idx, String data);
}
