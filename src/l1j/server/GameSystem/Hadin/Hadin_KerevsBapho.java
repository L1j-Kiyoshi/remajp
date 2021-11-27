/**
 * 본섭 리뉴얼된 Chapter1. 비밀의 마법사 하딘
 * 케레니스 vs 바포메트 스레드
 * by. 케인
 */
package l1j.server.GameSystem.Hadin;

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Instance.L1NpcInstance;

public class Hadin_KerevsBapho implements Runnable {
	private static Logger _log = Logger.getLogger(Hadin_KerevsBapho.class
			.getName());
	public boolean on = true;
	private L1NpcInstance Kere;
	private L1NpcInstance Bapho;
	private long KereDieTime = 0;

	public Hadin_KerevsBapho(L1NpcInstance k, L1NpcInstance b) {
		Kere = k;
		Bapho = b;
		KereDieTime = System.currentTimeMillis() + (60000 * 2); // 1
		GeneralThreadPool.getInstance().execute(this);
	}

	@Override
	public void run() {
		while (on) {
			try {
				if (Kere != null && Bapho != null && !Kere.isDead()
						&& !Bapho.isDead() && !Kere._destroyed
						&& !Bapho._destroyed) {
					long currTime = System.currentTimeMillis();
					if (Kere.HadinBossDelay < currTime)
						Kere.Hadin_kere_vs_bapho(Bapho);
					if (Bapho.HadinBossDelay < currTime)
						Bapho.Hadin_kere_vs_bapho(Kere);

					if (KereDieTime < currTime) {
						Kere.die(null);
					}
				} else
					on = false;
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(120L);
				} catch (InterruptedException e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					e.printStackTrace();
				}
			}
		}
	}

}
