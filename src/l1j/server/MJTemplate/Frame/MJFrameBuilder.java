package l1j.server.MJTemplate.Frame;
/** 
 * MJFrameBuilder
 * made by mjsoft, 2017.
 **/
public class MJFrameBuilder {
	private double amount;
	private double rate;
	
	public MJFrameBuilder setAmount(double amount){
		this.amount = amount;
		return this;
	}
	
	public MJFrameBuilder setRate(double rate){
		this.rate = rate;
		return this;
	}
	
	public MJFrameElement build(){
		return MJFrameElement.fromRate(amount, rate);
	}
}
