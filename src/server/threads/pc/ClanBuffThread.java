package server.threads.pc;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_PetWindow;

public class ClanBuffThread implements Runnable {

	private static ClanBuffThread _instance;

	// private static Logger _log =
	// Logger.getLogger(SabuDGTime.class.getName());

	public static ClanBuffThread getInstance() {
		if (_instance == null) {
			_instance = new ClanBuffThread();
		}
		return _instance;
	}

	public ClanBuffThread() {
		// super("server.threads.pc.SabuDGTime");
		GeneralThreadPool.getInstance().schedule(this, 3000);
	}

	@Override
	public void run() {
		try {
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null || pc.getNetConnection() == null) {
					continue;
				}
				if (pc instanceof L1RobotInstance) {
					continue;
				}
				if (pc.getClanid() != 0) {
					L1Clan clan = L1World.getInstance().getClan(
							pc.getClanname());
					if (clan != null) {
						if (clan.getOnlinePrivateShopXMemberCount() >= 3) {
							if (!pc.Ç÷¸Í¹öÇÁ && pc.getLevel() < 99) {
								pc.Ç÷¸Í¹öÇÁ = true;
								pc.sendPackets(new S_PacketBox(
										S_PacketBox.Ç÷¸Í¹öÇÁ, 1), true);
							}
						} else {
							if (pc.Ç÷¸Í¹öÇÁ) {
								pc.sendPackets(new S_PacketBox(
										S_PacketBox.Ç÷¸Í¹öÇÁ, 0), true);
								pc.Ç÷¸Í¹öÇÁ = false;
							}
						}
					}
				} else if (pc.Ç÷¸Í¹öÇÁ) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.Ç÷¸Í¹öÇÁ, 0), true);
					pc.Ç÷¸Í¹öÇÁ = false;
				}
				if (!pc.getMap().isTakePets()) {
					try {
						if (pc.getPetList() != null && pc.getPetListSize() > 0) {
							for (L1NpcInstance petNpc : pc.getPetList()) {
								if (petNpc instanceof L1SummonInstance) {
									((L1SummonInstance) petNpc).Death(null);
								} else if (petNpc instanceof L1PetInstance) {
									L1PetInstance Pet = (L1PetInstance)petNpc;
									L1ItemInstance useItem = pc.getInventory().getItem(Pet.getItemObjId());
									
									/** ¿ÀºêÁ§Æ® Á¦°Å */
									Pet.deletePet();
									/** ÆÐÅ¶ Ã³¸® 1¹ø ¿Â¿ÀÇÁ 2¹ø ´ÙÀÌ 3¹ø ÀÌ¸§ 4¹ø ¿¤¸¯¼­ */
									pc.sendPackets(new S_PetWindow(useItem, Pet, true, false, false, false, false), true);
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		GeneralThreadPool.getInstance().schedule(this, 3000);
	}

}
