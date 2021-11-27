package l1j.server.netty.coder.manager;

import l1j.server.Config;
import l1j.server.server.GeneralThreadPool;
import server.LineageClient;

public class DecoderManager {
	private static DecoderManager _instance;
	
	public static DecoderManager getInstance(){
		if (_instance == null) {
			_instance = new DecoderManager();
		}
		return _instance;
	}
	private LineageDecoderThread[] dts = new LineageDecoderThread[Config.MAX_ONLINE_USERS]; 
	
	private DecoderManager(){
		for(int i=0; i<dts.length; i++){
			LineageDecoderThread dt = new LineageDecoderThread();
			GeneralThreadPool.getInstance().execute(dt);
			dts[i] = dt;
		}
	}
	
	public void reload() {
		for (LineageDecoderThread thread : dts) {
			if (thread != null) {
				thread.setRunning(false);
			}
		}
		dts = null;
		dts = new LineageDecoderThread[Config.MAX_ONLINE_USERS];
		for (int i = 0; i < dts.length; i++) {
			LineageDecoderThread dt = new LineageDecoderThread();
			GeneralThreadPool.getInstance().execute(dt);
			dts[i] = dt;
		}
	}
	
	private int indexcount = 0;
	
	public int getRowIndex(){
		return indexcount;
	}
	
	public int getindex_size(){
		return dts.length;
	}

	
	public void putClient(LineageClient lc ){
		dts[lc.getthreadIndex()].putClient(lc);
	}
	
	public void removeClient(LineageClient lc, int ix){
		dts[ix].removeClient(lc);
	}
}
