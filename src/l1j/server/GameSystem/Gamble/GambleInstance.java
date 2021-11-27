package l1j.server.GameSystem.Gamble;

import java.util.List;
import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_TradeAddItem;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.FaceToFace;
import l1j.server.server.utils.L1SpawnUtil;

@SuppressWarnings("serial")
public class GambleInstance extends L1NpcInstance {

	public GambleInstance(L1Npc _npc) {
		super(_npc);
	}

	/** Type. 1=소막 **/
	private byte _type;
	private boolean _show;
	private String _chat_msg;

	public String getChatMsg() {
		return _chat_msg;
	}

	public void setChatMsg(String msg) {
		_chat_msg = msg;
	}

	public boolean getShow() {
		return _show;
	}

	public int aden = 0;
	private int clanid = 0;
	private String clanname;

	public int getClanid() {
		return clanid;
	}

	public void setClanid(int i) {
		clanid = i;
	}

	public String getClanname() { // 크란명
		return clanname;
	}

	public void setClanname(String s) {
		clanname = s;
	}

	public void setShow(boolean ck) {
		_show = ck;
		if (getType() != 1
				|| (wand_npc[0] == null || wand_npc[1] == null
						|| wand_npc[2] == null || wand_npc[3] == null
						|| wand_npc[4] == null || wand_npc[5] == null))
			return;
		if (ck) {
			for (int i = 0; i < 6; i++) {
				L1World.getInstance().storeObject(wand_npc[i]);
				L1World.getInstance().addVisibleObject(wand_npc[i]);
			}
		} else {
			for (int i = 0; i < 6; i++) {
				wand_npc[i].delete();
			}
		}
	}

	public long msgTime = 0;

	public byte getType() {
		return _type;
	}

	public void setType(int type) {
		_type = (byte) type;
	}

	public boolean play;
	public static int[] mobArray = { 45144, 45192, 45215, 45021, 45157, 45149,
			45024, 45082, 45019, 45009, 45138, 45098, 45127, 45161, 45173,
			45033, 45223, 45060, 45122, 45046, 45213, 45155, 45016, 45140,
			45092, 45040, 45136, 45107, 45025, 45008, 45064, 45079, 45041,
			45147, 45126, 45065, 45184, 45130, 45043, 45005, 45068 };
	public GambleInstance[] wand_npc = new GambleInstance[6];
	private static byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		attack.action();
		L1NpcInstance npc = FaceToFace.faceToFaceforNpc(player, true, getId());
		if (npc != null && npc instanceof GambleInstance) {
			if (player.getTradeID() > 0)
				return;
			if (!npc.Npc_trade && !play) {
				if (!npc.isParalyzed()) {
					if (player.getTradeReady())
						return;
					player.setTradeID(getId()); // 상대의 오브젝트 ID를 보존해 둔다
					S_Message_YN yn = new S_Message_YN(252, npc.getName());
					player.sendPackets(yn, true);
				}
			}
		}
		attack = null;
	}

	public void start(L1PcInstance pc, int item_count) {
		switch (getType()) {
		case 1:
			pc.Gamble_Somak = true;
			GeneralThreadPool.getInstance().execute(
					new Somak(this, pc, item_count));
			break;
		default:
			break;
		}
	}

	private static String chat9 = "오! 축하드려요... 배당금 지급 해 드렸습니다...";
	private static String chat11 = "아쉽군요... 다음기회에 도전해주세요~!";

	class Somak implements Runnable {
		GambleInstance npc;
		L1PcInstance pc;
		int item_count = 0;
		Random rnd;
		int jcount = 100;

		public Somak(GambleInstance _npc, L1PcInstance _pc, int _item_count) {
			npc = _npc;
			pc = _pc;
			item_count = _item_count;
			rnd = new Random(System.nanoTime());
			if (_item_count > 100000000)
				jcount = 1;
			else if (_item_count > 10000000)
				jcount = 30;
			else if (_item_count > 5000000)
				jcount = 50;
			else if (_item_count > 1000000)
				jcount = 60;
			else if (_item_count > 500000)
				jcount = 70;
			else if (_item_count > 100000)
				jcount = 80;
			else if (_item_count > 50000)
				jcount = 90;
		}

		@Override
		public void run() {
			try {
				if (npc == null || pc == null || item_count == 0)
					return;
				String chat = pc.getName() + "님 " + item_count
						+ "아덴 배팅하셨어요~ 1마리맞출때마다 5배 입니다~!";
				Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(npc, chat,
						0), true);
				int count = 15;
				while (count-- > 0) {
					for (int i = 0; i < 4; i++) {
						if (pc.Gamble_Text != null
								&& !pc.Gamble_Text.equals(""))
							break;
						Thread.sleep(1000);
						if (ck())
							return;
					}
					if (pc.Gamble_Text == null || pc.Gamble_Text.equals("")) {
						Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(
								npc, "배팅할 몹이름을 " + (count * 4) + "초 내로 말해주세용~",
								0), true);
					} else
						break;
				}
				if (pc.Gamble_Text == null || pc.Gamble_Text.equals("")) {
					npc.play = false;
					pc.Gamble_Somak = false;
					Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(npc,
							"60초 내로 몹이름을 입력하지 않아 아덴 그냥 갖을게요~", 0), true);
					return;
				}
				Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(npc,
						pc.Gamble_Text + "에 배팅합니다~", 0), true);
				Thread.sleep(2000);
				if (ck())
					return;
				Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(npc,
						"자~! 갑니다~!", 0), true);
				Thread.sleep(2000);
				if (ck())
					return;
				boolean win = false;
				String name[] = new String[6];
				int id[] = new int[6];
				double value = 0;
				for (int i = 0; i < 6; i++) {
					id[i] = mobArray[rnd.nextInt(mobArray.length)];
					L1Npc l1npc = NpcTable.getInstance().getTemplate(id[i]);
					name[i] = l1npc.get_name();
					if (name[i].equalsIgnoreCase(pc.Gamble_Text)) {
						win = true;
						value += 5;
					}
				}
				item_count = (int) (item_count * value);
				Thread.sleep(500);
				if (ck())
					return;
				Broadcaster
						.broadcastPacket(npc.wand_npc[0], new S_DoActionGFX(
								npc.wand_npc[0].getId(),
								ActionCodes.ACTION_Wand), true);
				Broadcaster
						.broadcastPacket(npc.wand_npc[1], new S_DoActionGFX(
								npc.wand_npc[1].getId(),
								ActionCodes.ACTION_Wand), true);
				Broadcaster
						.broadcastPacket(npc.wand_npc[2], new S_DoActionGFX(
								npc.wand_npc[2].getId(),
								ActionCodes.ACTION_Wand), true);
				Broadcaster
						.broadcastPacket(npc.wand_npc[3], new S_DoActionGFX(
								npc.wand_npc[3].getId(),
								ActionCodes.ACTION_Wand), true);
				Broadcaster
						.broadcastPacket(npc.wand_npc[4], new S_DoActionGFX(
								npc.wand_npc[4].getId(),
								ActionCodes.ACTION_Wand), true);
				Broadcaster
						.broadcastPacket(npc.wand_npc[5], new S_DoActionGFX(
								npc.wand_npc[5].getId(),
								ActionCodes.ACTION_Wand), true);
				Thread.sleep(1200);
				if (ck())
					return;
				L1NpcInstance npc1 = L1SpawnUtil.spawn2(npc.wand_npc[0].getX()
						+ HEADING_TABLE_X[npc.wand_npc[0].getMoveState()
								.getHeading()], npc.wand_npc[0].getY()
						+ HEADING_TABLE_Y[npc.wand_npc[0].getMoveState()
								.getHeading()], npc.getMapId(), id[0], 0, 0, 0);
				L1NpcInstance npc2 = L1SpawnUtil.spawn2(npc.wand_npc[1].getX()
						+ HEADING_TABLE_X[npc.wand_npc[1].getMoveState()
								.getHeading()], npc.wand_npc[1].getY()
						+ HEADING_TABLE_Y[npc.wand_npc[1].getMoveState()
								.getHeading()], npc.getMapId(), id[1], 0, 0, 0);
				L1NpcInstance npc3 = L1SpawnUtil.spawn2(npc.wand_npc[2].getX()
						+ HEADING_TABLE_X[npc.wand_npc[2].getMoveState()
								.getHeading()], npc.wand_npc[2].getY()
						+ HEADING_TABLE_Y[npc.wand_npc[2].getMoveState()
								.getHeading()], npc.getMapId(), id[2], 0, 0, 0);
				L1NpcInstance npc4 = L1SpawnUtil.spawn2(npc.wand_npc[3].getX()
						+ HEADING_TABLE_X[npc.wand_npc[3].getMoveState()
								.getHeading()], npc.wand_npc[3].getY()
						+ HEADING_TABLE_Y[npc.wand_npc[3].getMoveState()
								.getHeading()], npc.getMapId(), id[3], 0, 0, 0);
				L1NpcInstance npc5 = L1SpawnUtil.spawn2(npc.wand_npc[4].getX()
						+ HEADING_TABLE_X[npc.wand_npc[4].getMoveState()
								.getHeading()], npc.wand_npc[4].getY()
						+ HEADING_TABLE_Y[npc.wand_npc[4].getMoveState()
								.getHeading()], npc.getMapId(), id[4], 0, 0, 0);
				L1NpcInstance npc6 = L1SpawnUtil.spawn2(npc.wand_npc[5].getX()
						+ HEADING_TABLE_X[npc.wand_npc[5].getMoveState()
								.getHeading()], npc.wand_npc[5].getY()
						+ HEADING_TABLE_Y[npc.wand_npc[5].getMoveState()
								.getHeading()], npc.getMapId(), id[5], 0, 0, 0);
				GeneralThreadPool.getInstance().execute(
						new attack(npc.wand_npc[0], npc1));
				GeneralThreadPool.getInstance().execute(
						new attack(npc.wand_npc[1], npc2));
				GeneralThreadPool.getInstance().execute(
						new attack(npc.wand_npc[2], npc3));
				GeneralThreadPool.getInstance().execute(
						new attack(npc.wand_npc[3], npc4));
				GeneralThreadPool.getInstance().execute(
						new attack(npc.wand_npc[4], npc5));
				GeneralThreadPool.getInstance().execute(
						new attack(npc.wand_npc[5], npc6));
				Thread.sleep(1500);
				if (ck())
					return;
				if (win) {
					Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(npc,
							chat9, 0), true);
					for (int i = 0; i < 4; i++) {
						if (pc == null || pc.isDead() || npc.Npc_trade)
							break;
						if (npc.getTileLineDistance(pc) == 1) {
							if (FaceToFace.faceToFaceforNpc(pc, true,
									npc.getId()) != null) {
								pc.setTradeID(npc.getId()); // 상대의 오브젝트 ID를 보존해
															// 둔다
								pc.sendPackets(
										new S_Message_YN(252, npc.getName()),
										true); // %0%s가 당신과 아이템의 거래를 바라고 있습니다.
												// 거래합니까? (Y/N)
							}
						}
						Thread.sleep(2000);
						if (ck())
							return;
						if (i == 3 && !npc.Npc_trade) {
							pc.getInventory().storeItem(L1ItemId.ADENA,
									item_count);
							Broadcaster.broadcastPacket(npc,
									new S_NpcChatPacket(npc, pc.getName()
											+ "님 교환을 계속 안받으셔서 인벤에 넣어드렸어여~", 0),
									true);
							npc.play = false;
						}
					}
					if (npc.Npc_trade) {
						L1Item tempL1Item = ItemTable.getInstance()
								.getTemplate(40308);
						L1ItemInstance tempitem = ItemTable.getInstance()
								.FunctionItem(tempL1Item);
						tempitem.setCount(item_count);
						pc.sendPackets(
								new S_TradeAddItem(tempitem, tempitem
										.getCount(), 1), true);
						aden = item_count;
					}
				} else {
					Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(npc,
							chat11, 0), true);
					npc.play = false;
				}
				pc.Gamble_Somak = false;
				pc.Gamble_Text = "";
				name = null;
				id = null;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		private boolean ck() {
			if (npc == null || !npc.getShow() || pc == null
					|| pc.getNetConnection() == null) {
				if (npc != null)
					npc.play = false;
				if (pc != null) {
					pc.Gamble_Somak = false;
					pc.Gamble_Text = "";
				}
				return true;
			}
			return false;
		}
	}

	public void delete() {
		try {
			L1World.getInstance().removeVisibleObject(this);
			L1World.getInstance().removeObject(this);
			List<L1PcInstance> players = L1World.getInstance()
					.getRecognizePlayer(this);
			if (players.size() > 0) {
				S_RemoveObject s_deleteNewObject = new S_RemoveObject(this);
				for (L1PcInstance pc : players) {
					if (pc != null) {
						pc.getNearObjects().removeKnownObject(this);
						pc.sendPackets(s_deleteNewObject);
					}
				}
			}
			getNearObjects().removeAllKnownObjects();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class attack implements Runnable {
		GambleInstance attacker;
		L1NpcInstance mon;
		int heading = 0;

		public attack(GambleInstance _attacker, L1NpcInstance _mon) {
			attacker = _attacker;
			mon = _mon;
			heading = attacker.getMoveState().getHeading();
		}

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				while (true) {
					try {
						if (attacker == null || mon == null
								|| attacker._destroyed || attacker.isDead()
								|| mon._destroyed || mon.isDead())
							break;
						attacker.getMoveState().setHeading(
								CharPosUtil.targetDirection(attacker,
										mon.getX(), mon.getY())); // 방향세트
						S_UseArrowSkill uas = new S_UseArrowSkill(attacker,
								mon.getId(), 66, mon.getX(), mon.getY(), true);
						Broadcaster.broadcastPacket(attacker, uas, true);
						mon.receiveDamage(attacker, 50);

						Thread.sleep(attacker.calcSleepTime(attacker
								.getNpcTemplate().get_atkspeed()));
						if (attacker == null || mon == null
								|| attacker._destroyed || attacker.isDead()
								|| mon._destroyed || mon.isDead())
							break;
						if (mon.getHiddenStatus() == HIDDEN_STATUS_SINK) {
							L1SkillUse su = new L1SkillUse();
							su.handleCommands(null, 13, attacker.getId(),
									attacker.getX(), attacker.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF, attacker);
							su = null;
							Thread.sleep(attacker.calcSleepTime(attacker
									.getNpcTemplate().get_atkspeed()));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (getPoison() != null) {
					if (getPoison().getEffectId() == 1
							|| getPoison().getEffectId() == 2) {
						S_SkillSound ss = new S_SkillSound(attacker.getId(),
								192);
						Broadcaster.broadcastPacket(attacker, ss, true);
						attacker.curePoison();
					}
				}
				attacker.getMoveState().setHeading(heading);
				S_ChangeHeading ch = new S_ChangeHeading(attacker);
				Broadcaster.broadcastPacket(attacker, ch, true);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}
}
