
package l1j.server.server.clientpackets;

import java.io.UnsupportedEncodingException;
import javolution.util.FastMap;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.LineageClient;

// Referenced classes of package l1j.server.server.clientpackets:
// ClientBasePacket

public class C_Shop extends ClientBasePacket {
	

	/*
	 * C -> S 0000: 02 00 01 00 db 8c 4d 24 0d b6 56 07 01 00 00 00
	 * ......M$..V..... 0010: 00 00 31 32 33 31 32 33 ff 31 32 33 31 32 33 32
	 * ..123123.1231232 0020: 31 33 00 74 72 61 64 65 7a 6f 6e 65 31 00 00 00
	 * 13.tradezone1...
	 */
	private static final String C_SHOP = "[C] C_Shop";

	public C_Shop(byte abyte0[], LineageClient clientthread) {
		super(abyte0);

		L1PcInstance pc = clientthread.getActiveChar();
		if (pc == null || pc.isGhost() || pc.isDead()) {
			return;
		}
		if (pc.isInvisble()) {
			pc.sendPackets(new S_ServerMessage(755));
			return;
		}
		if (pc.getMapId() != 800) {
			pc.sendPackets(new S_SystemMessage("개인상점은 시장에서만  열수 있습니다."));
			return;
		}

		if (pc.getInventory().checkEquipped(427200)
				|| pc.getInventory().checkEquipped(427201)
				|| pc.getInventory().checkEquipped(427202)
				|| pc.getInventory().checkEquipped(427203)
				|| pc.getInventory().checkEquipped(427204)
				|| pc.getInventory().checkEquipped(427205)
				|| pc.getInventory().checkEquipped(427206)
				|| pc.getInventory().checkEquipped(427207)
				|| pc.getInventory().checkEquipped(427113)
				|| pc.getInventory().checkEquipped(427114)
				|| pc.getInventory().checkEquipped(427115)
				|| pc.getInventory().checkEquipped(427116)
				|| pc.getInventory().checkEquipped(427117)
				|| pc.getInventory().checkEquipped(427118)
				|| pc.getInventory().checkEquipped(427119)
				|| pc.getInventory().checkEquipped(427120)
				|| pc.getInventory().checkEquipped(427121)
				|| pc.getInventory().checkEquipped(427122)) {
			pc.sendPackets(new S_SystemMessage(
					"무인상점 개설은 룬을 해제해야 사용이 가능합니다."), true);
			return;
		}
		
		L1ItemInstance checkItem;
		boolean tradable = true;

		
		int type = readC();
		if (type == 0) { // 개시
			
			int sellTotalCount = readH();
			int sellObjectId;
			int sellPrice;
			int sellCount;
			Object[] petlist = null;
			for (int i = 0; i < sellTotalCount; i++) {
				sellObjectId 	= readD();
				sellPrice 		= readD();
				sellCount 		= readD();
				
				/** 개인상점 오류 수정 */
				if(sellTotalCount == 9){ 
					pc.sendPackets(new S_SystemMessage(pc,"물품등록은 8개까지만 가능합니다.")); 
					return;
				}
				
				// 거래 가능한 아이템이나 체크
				checkItem = pc.getInventory().getItem(sellObjectId);
				if ((sellObjectId != checkItem.getId()) || (!checkItem.isStackable() && sellCount != 1) || (checkItem.getCount() < sellCount || checkItem.getCount() <= 0 || sellCount <= 0)) {
					/** 2016.11.24 MJ 앱센터 시세 **/
					pc.disposeShopInfo();
					/** 2016.11.24 MJ 앱센터 시세 **/
					pc.sendPackets(new S_Disconnect());
					return;
				}
				if (!checkItem.isStackable() && sellCount != 1) {
					pc.sendPackets(new S_Disconnect());
					/** 2016.11.24 MJ 앱센터 시세 **/
					pc.disposeShopInfo();
					/** 2016.11.24 MJ 앱센터 시세 **/
					return;
				}
				if (sellCount > checkItem.getCount()) {
					sellCount = checkItem.getCount();
				}
			
				if(checkItem.getBless() >= 128){
					pc.sendPackets(new S_ServerMessage(210, checkItem.getItem().getName())); // \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
					/** 2016.11.24 MJ 앱센터 시세 **/
					pc.disposeShopInfo();
					/** 2016.11.24 MJ 앱센터 시세 **/
					return;
				}
				if (!checkItem.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
				}

				petlist = pc.getPetList().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (checkItem.getId() == pet.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
							break;
						}
					}
				}

				Object[] dollList = pc.getDollList().toArray();
				for (Object dollObject : dollList) {
					if (dollObject instanceof L1DollInstance) {
						L1DollInstance doll = (L1DollInstance) dollObject;
						if (checkItem.getId() == doll.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
							break;
						}
					}
				}
				
				/** 2016.11.24 MJ 앱센터 시세 **/
				pc.addSellings(MJDShopItem.create(checkItem, sellCount, sellPrice, false));
				/** 2016.11.24 MJ 앱센터 시세 **/
			}
			int buyTotalCount = readH();
			int buyObjectId;
			int buyPrice;
			int buyCount;
			for (int i = 0; i < buyTotalCount; i++) {
				buyObjectId = readD();
				buyPrice = readD();
				buyCount = readD();
				
				/** 개인상점 오류 수정 */
				if(buyTotalCount == 9){ 
					pc.sendPackets(new S_SystemMessage(pc,"물품등록은 8개까지만 가능합니다.")); 
					return;
				}
				// 거래 가능한 아이템이나 체크
				checkItem = pc.getInventory().getItem(buyObjectId);
				/*버그방지*/
				if ((buyObjectId != checkItem.getId()) || (!checkItem.isStackable() && buyCount != 1) || (buyCount <= 0 || checkItem.getCount() <= 0)) {
					/** 2016.11.24 MJ 앱센터 시세 **/
					pc.disposeShopInfo();
					/** 2016.11.24 MJ 앱센터 시세 **/
					
					pc.sendPackets(new S_Disconnect());
					return;
				}
				
				if (buyCount > checkItem.getCount()) {
					buyCount = checkItem.getCount();
				}
				/*버그방지*/
				// 거래 가능한 아이템이나 체크
				checkItem = pc.getInventory().getItem(buyObjectId);
				if (!checkItem.getItem().isTradable()) {
					tradable = false;
					pc.sendPackets(new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
				}
				petlist = pc.getPetList().toArray();
				for (Object petObject : petlist) {
					if (petObject instanceof L1PetInstance) {
						L1PetInstance pet = (L1PetInstance) petObject;
						if (checkItem.getId() == pet.getItemObjId()) {
							tradable = false;
							pc.sendPackets(new S_ServerMessage(166, checkItem.getItem().getName(), "거래 불가능합니다. "));
							break;
						}
					}
				}
				
				/** 2016.11.24 MJ 앱센터 시세 **/
				pc.addPurchasings(MJDShopItem.create(checkItem, buyCount, buyPrice, true));
				/** 2016.11.24 MJ 앱센터 시세 **/
				
			}
			if (!tradable) { // 거래 불가능한 아이템이 포함되어 있는 경우, 개인 상점 종료
				/** 2016.11.24 MJ 앱센터 시세 **/
				pc.disposeShopInfo();
				/** 2016.11.24 MJ 앱센터 시세 **/
				
				pc.setPrivateShop(false);
				pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
				pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
				return;
			}
			byte[] chat = readByte();
			String test;
			int poly;
			test = null;
			try {
				test = new String(chat, 0, chat.length, "MS932");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			
			pc.getNetConnection().getAccount().updateShopOpenCount();
			pc.sendPackets(new S_PacketBox(S_PacketBox.상점개설횟수, pc.getNetConnection().getAccount().Shop_open_count), true);
			
			pc.setShopChat(chat);
			pc.setPrivateShop(true);
			pc.sendPackets(new S_DoActionShop(pc.getId(), 70, chat), true);
			Broadcaster.broadcastPacket(pc, new S_DoActionShop(pc.getId(), 70, chat), true);
			/*pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat));
			pc.broadcastPacket(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, chat));*/
			
			poly = 0;
			if (test.matches(".*tradezone1.*"))
				poly = 11479;
			else if (test.matches(".*tradezone2.*"))
				poly = 11483;
			else if (test.matches(".*tradezone3.*"))
				poly = 11480;
			else if (test.matches(".*tradezone4.*"))
				poly = 11485;
			else if (test.matches(".*tradezone5.*"))
				poly = 11482;
			else if (test.matches(".*tradezone6.*"))
				poly = 11486;
			else if (test.matches(".*tradezone7.*"))
				poly = 11481;
			else if (test.matches(".*tradezone8.*")) {
				poly = 11484;
			}
			test = null;
			pc.getSkillEffectTimerSet().killSkillEffectTimer(67);
			L1PolyMorph.undoPoly(pc);
			L1ItemInstance weapon = pc.getWeapon();
			if(weapon != null)
				pc.getInventory().setEquipped(weapon, false, false, false);
			pc.getGfxId().setTempCharGfx(poly);
			pc.sendPackets(new S_ChangeShape(pc.getId(), poly, pc.getCurrentWeapon()));
			if ((!pc.isGmInvis()) && (!pc.isInvisble())) {
				Broadcaster.broadcastPacket(pc, new S_ChangeShape(pc.getId(), poly));
			}
			S_CharVisualUpdate charVisual = new S_CharVisualUpdate(pc, 0x46);
			pc.sendPackets(charVisual);
			Broadcaster.broadcastPacket(pc, charVisual);
			
			/** 2016.11.24 MJ 앱센터 시세 **/
			GeneralThreadPool.getInstance().execute(new MJDShopStorage(pc, false));
			/** 2016.11.24 MJ 앱센터 시세 **/
		} else if (type == 1) { // 종료
			pc.disposeShopInfo();
			pc.setPrivateShop(false);
			pc.sendPackets(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
			pc.broadcastPacket(new S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Idle));
			L1PolyMorph.undoPoly(pc);
			GeneralThreadPool.getInstance().execute(new MJDShopStorage(pc, true));
		}
	}

	private boolean isTwoLogin(L1PcInstance c) {
		boolean bool = false;
		L1PcInstance[] list = L1World.getInstance().getAllPlayersToArray();
		for (L1PcInstance target : list) {
			// 무인PC 는 제외
			if (target == null)
				continue;
			if (target.noPlayerCK)
				continue;
			if (target.샌드백)
				continue;
			//
			if (c.getId() != target.getId() && !target.isPrivateShop()) {
				if (c.getNetConnection() != null
						&& target.getNetConnection() != null) {
					if (c.getNetConnection()
							.getAccountName()
							.equalsIgnoreCase(
									target.getNetConnection().getAccountName())) {
						bool = true;
						break;
					}
				}
			}
		}
		list = null;
		return bool;
	}

	private static FastMap<String, Integer> 상점개설_계정횟수 = new FastMap<String, Integer>();

	public static boolean get상점개설계정횟수(String account) {
		synchronized (상점개설_계정횟수) {
			int time = 0;
			try {
				time = 상점개설_계정횟수.get(account);
			} catch (Exception e) {
			}
			if (time >= 50)
				return false;
			상점개설_계정횟수.put(account, time++);
			return true;
		}
	}

	public static void reset상점개설계정횟수() {
		synchronized (상점개설_계정횟수) {
			상점개설_계정횟수.clear();
		}
	}

	@Override
	public String getType() {
		return C_SHOP;
	}
}
