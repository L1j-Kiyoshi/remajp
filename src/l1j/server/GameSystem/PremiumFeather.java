package l1j.server.GameSystem;

import java.util.Calendar;

import l1j.server.GameSystem.UserRanking.UserRankingController;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.gametime.BaseTime;
import l1j.server.server.model.gametime.RealTimeClock;
import l1j.server.server.model.gametime.TimeListener;
import l1j.server.server.serverpackets.S_ServerMessage;
import server.system.autoshop.AutoShopManager;

public class PremiumFeather implements TimeListener {
	private static PremiumFeather _instance;

	public static void start() {
		if (_instance == null) {
			_instance = new PremiumFeather();
		}
		_instance.some();
		RealTimeClock.getInstance().addListener(_instance);
	}

	private void some() {
	}

	@Override
	public void onDayChanged(BaseTime time) {
	}

	@Override
	public void onHourChanged(BaseTime time) {
	}

	@Override
	public void onMinuteChanged(BaseTime time) {
		int rm = time.get(Calendar.MINUTE);
		if (rm % 12 == 0)
			PremiumTime();
	}

	@Override
	public void onMonthChanged(BaseTime time) {
	}

	private void PremiumTime() {
		int premiumNumber = 6;
		S_ServerMessage sm = new S_ServerMessage(403, "$5116 (" + premiumNumber + ")");
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			UserRankingController.isRenewal = true;
			if (AutoShopManager.getInstance().isAutoShop()) {
				if (!pc.isPrivateShop()) {
					pc.getInventory().storeItem(300, premiumNumber);
					pc.sendPackets(sm);
				}
			} else {
				pc.getInventory().storeItem(300, premiumNumber); 
				pc.sendPackets(sm);
			}
		}
	}
}
