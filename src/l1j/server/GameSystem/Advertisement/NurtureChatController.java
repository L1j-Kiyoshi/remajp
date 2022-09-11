package l1j.server.GameSystem.Advertisement;

import java.util.Random;

import javolution.util.FastTable;
import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SystemMessage;

public class NurtureChatController implements Runnable {
	// private static Logger _log =
	// Logger.getLogger(ExpMonitorController.class.getName());

	private static Random rnd = new Random(System.currentTimeMillis());
	private static NurtureChatController _instance;

	public static NurtureChatController getInstance() {
		if (_instance == null) {
			_instance = new NurtureChatController();
		}
		return _instance;
	}

	public NurtureChatController() {
		list = new FastTable<L1RobotInstance>();
		spawn();
		GeneralThreadPool.getInstance().schedule(this, 90000);
	}

	private FastTable<L1RobotInstance> list = null;
	private boolean on = false;

	@Override
	public void run() {
		try {
			if (on) {
				for (L1RobotInstance pc : list) {
					GeneralThreadPool.getInstance().schedule(new chat(pc),
							1000 + rnd.nextInt(10000));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				GeneralThreadPool.getInstance().schedule(this, 300000);
			} catch (Exception e) {
			}
		}
	}

	class chat implements Runnable {
		private L1RobotInstance bot = null;

		public chat(L1RobotInstance _bot) {
			bot = _bot;
		}

		@Override
		public void run() {
			try {
				S_ChatPacket s_chatpacket = new S_ChatPacket(bot,
						"\\\\f=f=ベトナム受動育成 NO.1 育成門のカトク LEVEL79 ご連絡ください!",
						Opcodes.S_SAY, 0);
				for (L1PcInstance listner : L1World.getInstance()
						.getRecognizePlayer(bot)) {
					if (!listner.getExcludingList().contains(bot.getName())) {
						listner.sendPackets(s_chatpacket);
					}
				}
			} catch (Exception e) {
			}
		}

	}

	public void OnOff(L1PcInstance gm, boolean ck) {
		if (ck) {
			if (L1World.getInstance().findObject(list.get(0).getId()) != null) {
				gm.sendPackets(new S_SystemMessage("現在稼働中です."), true);
				return;
			}
			for (L1RobotInstance pc : list) {
				L1World.getInstance().storeObject(pc);
				L1World.getInstance().addVisibleObject(pc);
			}
			on = true;
			gm.sendPackets(new S_SystemMessage("育成チャットロボットが稼働しました."), true);
			return;
		} else {
			if (L1World.getInstance().findObject(list.get(0).getId()) == null) {
				gm.sendPackets(new S_SystemMessage("現在終了している状態です。"), true);
				return;
			}
			for (L1RobotInstance pc : list) {
				L1World.getInstance().removeVisibleObject(pc);
				L1World.getInstance().removeObject(pc);
				for (L1PcInstance pp : L1World.getInstance()
						.getRecognizePlayer(pc)) {
					pp.getNearObjects().removeKnownObject(pc);
					pp.sendPackets(new S_RemoveObject(pc), true);
				}
				pc.getNearObjects().removeAllKnownObjects();
			}
			on = false;
			gm.sendPackets(new S_SystemMessage("育成チャットロボットが終了しました"), true);
			return;
		}
	}

	// 1番目-市場2番目-マルサム3番目から-4番マップ
	private static int[] x = { 32804, 32579, 33058, 33429, 32614, 32738, 33076,
			32618, 33618, 33719, 34059, 33967, 33935, 33537 };
	private static int[] y = { 32927, 32930, 32782, 32812, 32737, 32445, 33393,
			33180, 33250, 32494, 32277, 33259, 33343, 32845 };
	private static short[] heading = { 6, 6, 6, 5, 5, 5, 4, 2, 5, 5, 6, 6, 6, 5 };

	private void spawn() {
		for (int i = 1; i <= x.length; i++) {
			L1RobotInstance newPc = new L1RobotInstance();
			newPc.setId(ObjectIdFactory.getInstance().nextId());
			newPc.setAccountName("");
			String name = "手動育成";
			if (i < 9)
				name += "0" + i;
			else
				name += i;
			newPc.setName(name);
			newPc.setHighLevel(1);
			newPc.setLevel(1);
			newPc.setExp(0);
			newPc.setLawful(0);
			newPc.addBaseMaxHp((short) 1500);
			newPc.setCurrentHp(1500);
			newPc.setDead(false);
			newPc.addBaseMaxMp((short) 100);
			newPc.setCurrentMp(100);
			newPc.getResistance().addMr(150);
			newPc.setTitle("カトク LEVEL79");
			newPc.getAbility().setBaseStr(18);
			newPc.getAbility().setStr(35);
			newPc.getAbility().setBaseCon(18);
			newPc.getAbility().setCon(18);
			newPc.getAbility().setBaseDex(18);
			newPc.getAbility().setDex(35);
			newPc.getAbility().setBaseCha(18);
			newPc.getAbility().setCha(18);
			newPc.getAbility().setBaseInt(18);
			newPc.getAbility().setInt(18);
			newPc.getAbility().setBaseWis(18);
			newPc.getAbility().setWis(35);
			newPc.set_sex(0);
			newPc.setClassId(61);
			newPc.getGfxId().setGfxId(61);
			newPc.getGfxId().setTempCharGfx(61);
			newPc.setType(1);
			newPc.getMoveState().setMoveSpeed(0);
			newPc.getMoveState().setBraveSpeed(0);
			newPc.getMoveState().setHeading(0);
			newPc.set_food(39);
			newPc.setClanRank(0);
			newPc.setElfAttr(0);
			newPc.set_PKcount(0);
			newPc.setExpRes(0);
			newPc.setPartnerId(0);
			newPc.setAccessLevel((short) 0);
			newPc.setGm(false);
			newPc.setMonitor(false);
			newPc.setHomeTownId(0);
			newPc.setContribution(0);
			newPc.setHellTime(0);
			newPc.setBanned(false);
			newPc.setKarma(0);
			newPc.setReturnStat(0);
			newPc.setGmInvis(false);
			newPc.noPlayerCK = true;
			newPc.setActionStatus(0);
			newPc.setKills(0);
			newPc.setDeaths(0);
			newPc.setNetConnection(null);

			newPc.setX(x[i - 1]);
			newPc.setY(y[i - 1]);
			if (i - 1 == 0)
				newPc.setMap((short) 800);
			else if (i - 1 == 1)
				newPc.setMap((short) 0);
			else
				newPc.setMap((short) 4);
			newPc.getMoveState().setHeading(heading[i - 1]);
			list.add(newPc);
		}
	}

}
