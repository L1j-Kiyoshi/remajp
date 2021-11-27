package l1j.server.MJTemplate.Frame;

/** 
 * MJFrameElement
 * made by mjsoft, 2017.
 **/
public class MJFrameElement {
	private static final double BASIC_MILLIS = 1000.0D;
	
	public static MJFrameElement fromRate(double amount, double rate){
		return fromRPS(amount, calcRps(rate));
	}
	
	public static MJFrameElement fromRate(double amount, double basic_millis, double rate){
		return fromRPS(amount, calcRps(basic_millis, rate));
	}
	
	public static MJFrameElement fromRPS(double amount, double rps){
		return create(amount, amount * rps);
	}
	
	public static MJFrameElement create(double amount, double fps){
		MJFrameElement element = new MJFrameElement();
		element.frameAmount = amount;
		element.framePerSecond = fps;
		return element;
	}
	
	public static double calcRps(double basic_millis, double rate){
		return basic_millis / rate;
	}
	
	public static double calcRps(double rate){
		return calcRps(BASIC_MILLIS, rate);
	}
	
	private Double frameAmount;
	private Double framePerSecond;
	private MJFrameElement(){}
	
	public Double getFrameAmount(){
		return frameAmount;
	}
	
	public Double getFramePerSeocnd(){
		return framePerSecond;
	}
	
	public Double getFramePerSecond(Double rps){
		return frameAmount * rps;
	}	
}
