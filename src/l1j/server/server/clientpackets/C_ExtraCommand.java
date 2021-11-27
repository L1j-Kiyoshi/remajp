package l1j.server.server.clientpackets;

import static l1j.server.server.model.skill.L1SkillId.SHAPE_CHANGE;
import l1j.server.GameSystem.Hadin.HadinThread;
import l1j.server.GameSystem.NavalWarfare.NavalWarfare;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import server.LineageClient;

public class C_ExtraCommand extends ClientBasePacket {
	

	private static final String C_EXTRA_COMMAND = "[C] C_ExtraCommand";

	public C_ExtraCommand(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);
		try {
			int actionId = readC();
			if (actionId < 66 || actionId > 69)
				return;

			L1PcInstance pc = client.getActiveChar();
			if (pc.isGhost()) {
				return;
			}
			if (pc.isInvisble()) { // 인비지비리티, 브라인드하이딘그중
				return;
			}
			if (pc.isTeleport()) { // 텔레포트 처리중
				return;
			}

			try {
				/** 하딘 인던 관련 Action **/
				if (pc.isInParty()
						&& (pc.getMapId() >= 9000 && pc.getMapId() <= 9099)) {
					if (pc.getParty().isLeader(pc)) {
						if (actionId == 69 || actionId == 66) {
							HadinThread.get().Alt_Action(pc.getParty(),
									actionId == 69);
						}
					}
					/** 해상전 관련 Action **/
				} else if (pc.isInParty()
						&& (pc.getMapId() >= 9101 && pc.getMapId() <= 9199)) {
					if (pc.getX() == 32799 && pc.getY() == 32808
							&& pc.getParty().isLeader(pc)) {
						if (actionId == 69 || actionId == 66) {
							NavalWarfare.getInstance().NavalTrapCheck(
									pc.getMapId(), actionId == 66);
						}
					}
				}
			} catch (Exception e) {
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(SHAPE_CHANGE)) { // 만일을
																			// 위해,
																			// 변신중은
																			// 타플레이어에
																			// 송신하지
																			// 않는다
				int gfxId = pc.getGfxId().getTempCharGfx();
				if (gfxId != 6080 && gfxId != 6094) {
					short id = 0;
					if (actionId == 0x44) {
						id = 3204;
					} else if (actionId == 0x45) {
						id = 3205;
					} else if (actionId == 0x43) {
						id = 3206;
					} else if (actionId == 0x42) {
						id = 3207;
					}
					pc.sendPackets(new S_EffectLocation(pc.getLocation(), id),
							true);
					Broadcaster.broadcastPacket(pc,
							new S_EffectLocation(pc.getLocation(), id), true); // 주위의
																				// 플레이어에
																				// 송신

					return;
				}
			}

			S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
			Broadcaster.broadcastPacket(pc, gfx, true);
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_EXTRA_COMMAND;
	}
}
