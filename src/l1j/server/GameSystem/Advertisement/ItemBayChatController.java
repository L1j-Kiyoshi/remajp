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

public class ItemBayChatController implements Runnable {
	// private static Logger _log =
	// Logger.getLogger(ExpMonitorController.class.getName());

	private static Random rnd = new Random(System.currentTimeMillis());
	private static ItemBayChatController _instance;

	public static ItemBayChatController getInstance() {
		if (_instance == null) {
			_instance = new ItemBayChatController();
		}
		return _instance;
	}

	public ItemBayChatController() {
		list = new FastTable<L1RobotInstance>();
		spawn();
		GeneralThreadPool.getInstance().execute(this);
	}

	private FastTable<L1RobotInstance> list = null;
	private boolean on = false;

	@Override
	public void run() {
		try {
			if (on) {
				for (L1RobotInstance pc : list) {
					GeneralThreadPool.getInstance().schedule(new chat(pc),
							1000 + rnd.nextInt(3000));
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
						"\\aDアデン売買アイテム愛好家、ゲーム名：フランツディス - その他",
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
				gm.sendPackets(new S_SystemMessage("現在稼働中です。"), true);
				return;
			}
			for (L1RobotInstance pc : list) {
				L1World.getInstance().storeObject(pc);
				L1World.getInstance().addVisibleObject(pc);
			}
			on = true;
			gm.sendPackets(new S_SystemMessage("アイテムベイロボットが稼動しました。"), true);
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
			gm.sendPackets(new S_SystemMessage("アイテムベイロボットが終了しました。"), true);
			return;
		}
	}

	private static int[] x = { 32800, 32581, 33058, 33429, 32616, 32740, 33078,
			32616, 33616, 33718, 34060, 33967, 33935, 33535 };
	private static int[] y = { 32923, 32932, 32785, 32814, 32737, 32447, 33393,
			33180, 33250, 32493, 32278, 33257, 33341, 32845 };
	private static short[] heading = { 4, 5, 5, 5, 4, 4, 4, 4, 6, 6, 6, 6, 6, 5 };

	private void spawn() {
		for (int i = 1; i <= x.length; i++) {
			L1RobotInstance newPc = new L1RobotInstance();
			newPc.setId(ObjectIdFactory.getInstance().nextId());
			newPc.setAccountName("");
			String name = "アイテムマニア";
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
			newPc.setTitle("www.itemmania.com");
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
