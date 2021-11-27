package l1j.server.netty;

public class IpInfo {
	private String _ip;
	private int _count;
	private long _time;
	private boolean _isban;
	
	public String getIp() {
		return _ip;
	}
	
	public void setIp(String ip) {
		_ip = ip;
	}
	
	public int getCount() {
		return _count;
	}
	
	public void setCount(int i) {
		_count = i;
	}
	
	public long getTime() {
		return _time;
	}
	
	public void setTime(long time) {
		_time = time;
	}
	
	public boolean isBlock() {
		return _isban;
	}
	
	public void setBlock(boolean flag) {
		_isban = flag;
	}
}
