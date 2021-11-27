package l1j.server.MJTemplate.MJArrangeHelper;

import l1j.server.MJTemplate.Frame.MJFrameElement;

/** 
 * MJArrangeParseeFactory
 * made by mjsoft, 2017.
 **/
public class MJArrangeParseeFactory {
	public static MJArrangeParsee<Integer> createIntArrange(){
		return new MJAbstractArrangeDefaultParsee<Integer>(){
			@Override
			public void parse(int idx, String data) {
				_datas[idx] = Integer.parseInt(data.trim());
			}
		}.init(Integer.class);
	}
	
	public static MJArrangeParsee<Double> createDoubleArrange(){
		return new MJAbstractArrangeDefaultParsee<Double>(){
			@Override
			public void parse(int idx, String data) {
				_datas[idx] = Double.parseDouble(data.trim());
			}
		};
	}
	
	public static MJArrangeParsee<Double> createFrameRateArrange(){
		return new MJAbstractArrangeDefaultParsee<Double>(){
			@Override
			public void parse(int idx, String data) {
				_datas[idx] = MJFrameElement.calcRps(Double.parseDouble(data.trim()));
			}
		}.init(Double.class);
	}
}
