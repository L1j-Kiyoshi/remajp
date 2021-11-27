package l1j.server.server.model.Instance;

import java.util.List;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.GameSystem.NpcTradeShop.NpcTradeShop;
import l1j.server.GameSystem.NpcTradeShop.ShopItem;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.FaceToFace;

public class L1NpcShopInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	public int _state = 0;
	private String _shopName;
	private String normal_chat;
	public boolean show = false;
	public boolean deleting = false;
	public boolean tradeItemck = false;
	public int tradeSellItemid = 0;
	public FastTable<String> npcTradeSellChat = new FastTable<String>();

	/**
	 * @param template
	 */
	public L1NpcShopInstance(L1Npc template) {
		super(template);
	}

	@Override
	public void onAction(L1PcInstance player) {
		L1Attack attack = new L1Attack(player, this);
		// attack.calcHit();
		attack.action();
		L1NpcInstance npc = FaceToFace.faceToFaceforNpc(player, true, getId());
		if (npc != null && npc instanceof L1NpcShopInstance) {
			if (player.getTradeID() > 0)
				return;
			L1NpcShopInstance nsi = (L1NpcShopInstance) npc;
			ShopItem[] list = NpcTradeShop.getInstance().getShopList();
			boolean ck = false;
			if (list != null) {
				for (ShopItem shop : list) {
					if (shop.getNpcId() == npc.getNpcId()) {
						ck = true;
						break;
					}
				}
			}
			if (nsi.getState() == 3)
				ck = true;
			if (!npc.Npc_trade && (npc.getNpcTemplate().get_npcId() >= 8100000 && npc.getNpcTemplate().get_npcId() <= 8100002 || ck)) {
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

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.getNearObjects().addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCPack(this), true);
		// if(_state == 1)
		// perceivedFrom.sendPackets( new S_DoActionShop(getId(),
		// ActionCodes.ACTION_Shop, getShopName().getBytes()));
	}

	@Override
	public void onTalkAction(L1PcInstance player) {
	}

	// 0 = 판매상점 1 = 운영자매입 3 = 유저매입
	public int getState() {
		return _state;
	}

	public void setState(int i) {
		_state = i;
	}

	public String getShopName() {
		return _shopName;
	}

	public void setShopName(String name) {
		_shopName = name;
	}

	/** 매입->판매 상점 관련 기본 ShopName **/
	private String _defaultname;

	public String getDefaultName() {
		return _defaultname;
	}

	public void setDefaultName(String name) {
		_defaultname = name;
	}

	private String _lastname;

	public String getLastName() {
		return _lastname;
	}

	public void setLastName(String name) {
		_lastname = name;
	}

	public String getNormalChat() {
		return normal_chat;
	}

	public void setNormalChat(String name) {
		normal_chat = name;
	}

	public class delete implements Runnable {

		private L1NpcShopInstance npc;

		public delete(L1NpcShopInstance _npc) {
			npc = _npc;
		}

		@Override
		public void run() {
			try {
				L1World.getInstance().removeVisibleObject(npc);
				L1World.getInstance().removeObject(npc);
				List<L1PcInstance> players = L1World.getInstance().getRecognizePlayer(npc);
				if (players.size() > 0) {
					S_RemoveObject s_deleteNewObject = new S_RemoveObject(npc);
					for (L1PcInstance pc : players) {
						if (pc != null) {
							pc.getNearObjects().removeKnownObject(npc);
							pc.sendPackets(s_deleteNewObject);
						}
					}
				}
				getNearObjects().removeAllKnownObjects();
				npc.deleting = false;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	private Random _rnd = new Random(System.nanoTime());

	public void shopdelete() {
		int rnd = _rnd.nextInt((60000 * 90)) + (60000 * 30);
		GeneralThreadPool.getInstance().schedule(new delete(this), rnd);
	}
}
