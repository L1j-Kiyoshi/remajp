package l1j.server.server.model.item.function;

import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.TimeController.FishingTimeController;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_Fishing;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class Fishing extends L1ItemInstance {

	private static Random _random = new Random(System.nanoTime());

	public Fishing(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			int itemId = this.getItemId();
			if (pc.isFishing()) {
				pc.sendPackets(new S_SystemMessage("낚시: 진행중"), true);
			} else {
				startFishing(pc, itemId, packet.readH(), packet.readH());
			}
		}
	}

	private void startFishing(L1PcInstance pc, int itemId, int fishX, int fishY) {
		if (pc.getMapId() != 5302 && pc.getMapId() != 5490) {
			// 여기에 낚싯대를 던질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(1138), true);
			return;
		}
		int gab = 0;
		int heading = pc.getMoveState().getHeading(); // ● 방향: (0.좌상)(1.상)(
														// 2.우상)(3.오른쪽)(4.우하)(5.하)(6.좌하)(7.좌)
		switch (heading) {
		case 0: // 상좌
			gab = L1WorldMap.getInstance().getMap((short) 5302)
					.getOriginalTile(pc.getX(), pc.getY() - 5);
			break;
		case 1: // 상
			gab = L1WorldMap.getInstance().getMap((short) 5302)
					.getOriginalTile(pc.getX() + 5, pc.getY() - 5);
			break;
		case 2: // 우상
			gab = L1WorldMap.getInstance().getMap((short) 5302)
					.getOriginalTile(pc.getX() + 5, pc.getY() - 5);
			break;
		case 3: // 오른쪽
			gab = L1WorldMap.getInstance().getMap((short) 5302)
					.getOriginalTile(pc.getX() + 5, pc.getY() + 5);
			break;
		case 4: // 우하
			gab = L1WorldMap.getInstance().getMap((short) 5302)
					.getOriginalTile(pc.getX(), pc.getY() + 5);
			break;
		case 5: // 하
			gab = L1WorldMap.getInstance().getMap((short) 5302)
					.getOriginalTile(pc.getX() - 5, pc.getY() + 5);
			break;
		case 6: // 좌하
			gab = L1WorldMap.getInstance().getMap((short) 5302)
					.getOriginalTile(pc.getX() - 5, pc.getY());
			break;
		case 7: // 좌
			gab = L1WorldMap.getInstance().getMap((short) 5302)
					.getOriginalTile(pc.getX() - 5, pc.getY() - 5);
			break;
		}
		int fishGab = pc.getMap().getOriginalTile(fishX, fishY);
		if (gab == 28 && fishGab == 28) {
			if (itemId == 600229 || pc.getInventory().consumeItem(60327, 1)) { // 먹이
				// if (pc.getInventory().consumeItem(41295, 1)) { // 먹이
				pc.sendPackets(new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY), true);
				Broadcaster.broadcastPacket(pc, new S_Fishing(pc.getId(), ActionCodes.ACTION_Fishing, fishX, fishY), true);
				pc.setFishing(true);
				pc.setFishingItem(this);
				pc.fishX = fishX;
				pc.fishY = fishY;
				// long time = System.currentTimeMillis() + 10000 +
				// _random.nextInt(5) * 1000;
				boolean ck = false;
				int is성장 = 0;
				long time = System.currentTimeMillis() + 240000;
				if (itemId == 60334 || itemId == 60478 || itemId == 60479 || itemId == 60523 || itemId == 60524) {// 릴
																			// 장착
																			// 고탄력
																			// 낚싯대
					time = System.currentTimeMillis() + 80000;
					ck = true;
				}
				if (itemId == 600229) {// 릴 장착 고탄력 낚싯대
					time = System.currentTimeMillis() + 40000;
					ck = true;
					is성장 = 1;
				}
				pc.setFishingTime(time);
				FishingTimeController.getInstance().addMember(pc);
				pc.sendPackets(new S_NewCreateItem(S_NewCreateItem.FISH_WINDOW, 1, ck, is성장, pc.getFishingItem().getChargeCount()), true);
			} else {
				// 낚시를 하기 위해서는 먹이가 필요합니다.
				pc.sendPackets(new S_ServerMessage(1137), true);
			}
		} else {
			// 여기에 낚싯대를 던질 수 없습니다.
			pc.sendPackets(new S_ServerMessage(1138), true);
		}
	}
}
