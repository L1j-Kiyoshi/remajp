package l1j.server.server.clientpackets;

import l1j.server.Config;
import l1j.server.server.datatables.LogTable;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import manager.LinAllManager;
import server.LineageClient;

public class C_DropItem extends ClientBasePacket {
	private static final String C_DROP_ITEM = "[C] C_DropItem";

	public C_DropItem(byte[] decrypt, LineageClient client) throws Exception {
		super(decrypt);
		try {
			/*
			 * 01 00 00 00 95 82 2f 80 6c b4 17 1b 01 00 00 00 2f ea 03
			 */
			int size = readD();
			int x = 0;
			int y = 0;
			int objectId = 0;
			int count = -1;

			L1PcInstance pc = client.getActiveChar();
			if (pc == null) {
				return;
			}
			if (pc.getOnlineStatus() != 1) {
				pc.sendPackets(new S_Disconnect(), true);
				return;
			}
			if (pc.isGhost())
				return;
			if (isTwoLogin(pc))
				return;
			L1ItemInstance item = null;
			if (size < 1)
				return;
			if (size > 255)
				return;
			for (int i = 0; i < size; i++) {
				x = readH();
				y = readH();
				objectId = readD();
				count = readD();
				if (x == 0 || y == 0 || objectId == 0 || count == -1) {
					return;
				}
				item = pc.getInventory().getItem(objectId);
				if (item != null) {
					if (!item.getItem().isTradable() && !pc.isGm()) {
						// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
						pc.sendPackets(new S_ServerMessage(210, item.getItem().getName()));
						return;
					}

					/** 아이템 오브젝트가 펫목걸이고 그목거리가 망각이 아니라면 */
					if (item.isDogNecklace() && !pc.isGm()) {
						// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
						pc.sendPackets(new S_ServerMessage(210, item.getViewName()), true);
						return;
					}

					if (!item.isStackable() && count != 1 && !pc.isGm()) {
						return;
					}

					if (item.getCount() <= 0 && !pc.isGm()) {
						return;
					}

					if (count <= 0 || count > 2000000000 && !pc.isGm()) {
						return;
					}

					if (count > item.getCount()) {
						count = item.getCount();
					}

					if (Math.max(Math.abs(x - pc.getX()), Math.abs(y - pc.getY())) > 2) {
						return;
					}

					if (pc.getMapId() == 340 || pc.getMapId() == 350 || pc.getMapId() == 360 || pc.getMapId() == 370 && !pc.isGm()) {
						S_SystemMessage sm = new S_SystemMessage("시장안에서는 바닥에 아이템을 버릴 수 없습니다.");
						pc.sendPackets(sm, true);
						return;
					}

					if (!pc.isGm() && pc.getLevel() < Config.드랍레벨) {
						pc.sendPackets(new S_SystemMessage("레벨 " + Config.드랍레벨 + "부터 버릴 수 있습니다."));
						return;
					}
					if (!pc.isGm() && (item.getItemId() == 40308 || (item.getItemId() >= 40033 && item.getItemId() <= 40038))) {
						pc.sendPackets(new S_SystemMessage("해당 아이템은 바닥에 버릴 수 없습니다."));
						return;
					}

					if (item.getItemId() == 49312 || item.getItemId() == 40312) {
						pc.sendPackets(new S_SystemMessage("여관열쇠는 /교환을 이용하거나 창고를 이용해주세요."));
						return;
					}

					if (item.getBless() >= 128 && !pc.isGm()) {
						S_ServerMessage sm = new S_ServerMessage(210, item.getItem().getName());
						pc.sendPackets(sm, true);
						return;
					}

					if (pc.getPetListSize() > 0) {
						L1PetInstance pet = null;
						S_ServerMessage sm1 = new S_ServerMessage(210, item.getItem().getName());
						for (Object petObject : pc.getPetList()) {
							if (petObject instanceof L1PetInstance) {
								pet = (L1PetInstance) petObject;
								if (item.getId() == pet.getItemObjId()) {
									pc.sendPackets(sm1, true);
									return;
								}
							}
						}
					}
					if (pc.getDollListSize() > 0) {
						L1DollInstance 인형 = null;
						S_SystemMessage sm = new S_SystemMessage("소환중인 인형은 버릴수 없습니다.");
						for (Object 인형오브젝트 : pc.getDollList()) {
							if (인형오브젝트 instanceof L1DollInstance) {
								인형 = (L1DollInstance) 인형오브젝트;
								if (item.getId() == 인형.getItemObjId()) {
									// \f1%0은 버리거나 또는 타인에게 양일을 할 수 없습니다.
									pc.sendPackets(sm, true);
									return;
								}
							}
						}
					}
					if (item.isEquipped()) {
						// \f1삭제할 수 없는 아이템이나 장비 하고 있는 아이템은 버릴 수 없습니다.
						S_SystemMessage sm2 = new S_SystemMessage("삭제 불가능 아이템이나 착용하고 있는 아이템은 버릴 수 없습니다.");
						pc.sendPackets(sm2, true);
						return;
					}

					L1GroundInventory gi = L1World.getInstance().getInventory(x, y, pc.getMapId());
					if (gi.getSize() > 5) {
						pc.sendPackets(new S_SystemMessage("그 곳에는 더이상 아이템을 내려놓을 수 없습니다."), true);
						return;
					}

					int size1 = 0;
					for (L1Object obj : L1World.getInstance().getVisibleObjects(pc)) {
						if (obj instanceof L1GroundInventory) {
							L1GroundInventory ginv = (L1GroundInventory) obj;
							size1 += ginv.getSize();
							if (size1 > 150)
								break;
						}
					}

					if (size1 > 150) {
						pc.sendPackets(new S_SystemMessage("이 화면에는 더이상 아이템을 내려놓을 수 없습니다."), true);
						return;
					}

					LinAllManager.getInstance().PicupAppend(item.getLogName(), pc.getName(), count, LinAllManager.Item_Drop);
					pc.getInventory().tradeItem(item, count, gi);
					LogTable.logitem(pc, item, count, 0);
				}

			}
		} catch (Exception e) {

		} finally {
			clear();
		}

	}

	@Override
	public String getType() {
		return C_DROP_ITEM;
	}

	private boolean isTwoLogin(L1PcInstance c) {
		boolean bool = false;
		for (L1PcInstance target : L1World.getInstance().getAllPlayers()) {
			// 무인PC 는 제외
			if (target == null)
				continue;
			if (target.noPlayerCK)
				continue;
			if (target.샌드백)
				continue;
			//
			if (c.getId() != target.getId() && !target.isPrivateShop()) {
				if (c.getNetConnection().getAccountName().equalsIgnoreCase(target.getNetConnection().getAccountName())) {
					bool = true;
					break;
				}
			}
		}
		return bool;
	}
}
