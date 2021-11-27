package l1j.server.server.utils;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Map;

/**
 * @author диюн
 */
public class ThreadMonitor extends Thread {

	private int checkInterval = 0;

	public ThreadMonitor(int checkInterval) {
		super("ThreadMonitor");
		this.checkInterval = checkInterval * 1000;
		start();
		System.out.println(":: Thread Monitor On");
		System.out.println(":: --------------------------------");
	}

	@Override
	public void run() {
		boolean noDeadLocks = true;

		while (noDeadLocks) {
			try {
				ThreadMXBean bean = ManagementFactory.getThreadMXBean();
				long[] threadIds = bean.getAllThreadIds();
				ThreadInfo[] tinfos = bean.getThreadInfo(threadIds);
				// build a map with key = CPU time and value = ThreadInfo
				for (int i = 0; i < threadIds.length; i++) {
					long cpuTime = bean.getThreadCpuTime(threadIds[i]);
					// filter out threads that have been terminated
					if (cpuTime != -1 && tinfos[i] != null) {
						long ns = cpuTime;
						double sec = ns / 1000000000;
						if (sec > 1.0) {
							System.out.println("Thread Name: "
									+ tinfos[i].getThreadName()
									+ "  CPU_TIME: " + sec);
							whereami(tinfos[i].getThreadId());
							/*
							 * for(StackTraceElement f :
							 * tinfos[i].getStackTrace()){
							 * System.out.println("\t"+f); }
							 */
						}
					}
				}
				Thread.sleep(checkInterval);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	public void whereami(long id) {
		Map<Thread, StackTraceElement[]> st = Thread.getAllStackTraces();
		for (Map.Entry<Thread, StackTraceElement[]> e : st.entrySet()) {
			StackTraceElement[] el = e.getValue();
			Thread t = e.getKey();
			if (t.getId() == id) {
				System.out.println("\"" + t.getName() + "\"" + " "
						+ (t.isDaemon() ? "daemon" : "") + " prio="
						+ t.getPriority() + " Thread id=" + t.getId() + " "
						+ t.getState());
				for (StackTraceElement line : el) {
					try {
						System.out.println("\t" + line);
					} catch (Exception e1) {
					}
				}
				System.out.println("");
				return;
			}
		}
	}

}