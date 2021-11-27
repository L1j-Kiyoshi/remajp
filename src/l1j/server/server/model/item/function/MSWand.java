package l1j.server.server.model.item.function;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_RangeSkill;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("serial")
public class MSWand extends L1ItemInstance {

	private static Random _random = new Random(System.nanoTime());

	public MSWand(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		try {
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				L1ItemInstance useItem = pc.getInventory()
						.getItem(this.getId());
				int spellsc_objid = 0;
				int spellsc_x = 0;
				int spellsc_y = 0;
				spellsc_objid = packet.readD();
				spellsc_x = packet.readH();
				spellsc_y = packet.readH();
				pc.cancelAbsoluteBarrier();
				int delay_id = 0;
				delay_id = ((L1EtcItem) useItem.getItem()).get_delayid();
				if (delay_id != 0) { // 지연 설정 있어
					if (pc.hasItemDelay(delay_id) == true) {
						return;
					}
				}
				if (pc.isInvisble()) {
					pc.sendPackets(new S_ServerMessage(1003));
					return;
				}

				if (spellsc_objid == 0)
					return;

				/*
				 * int chargeCount = useItem.getChargeCount(); if (chargeCount
				 * <= 0) { pc.sendPackets(new
				 * S_SystemMessage("아무일도 일어나지 않았습니다.")); return; }
				 */
				L1Object target = L1World.getInstance().findObject(
						spellsc_objid);

				if (target != null && !(target instanceof L1ItemInstance)) {

					int heding = CharPosUtil.targetDirection(pc, spellsc_x,
							spellsc_y);

					pc.getMoveState().setHeading(heding);

					if (CharPosUtil.isAreaAttack(pc, target.getX(),
							target.getY(), target.getMapId()) == false) {
						pc.sendPackets(new S_AttackPacket(pc, 0,
								ActionCodes.ACTION_Wand));
						Broadcaster.broadcastPacket(pc, new S_AttackPacket(pc,
								0, ActionCodes.ACTION_Wand));
						return; // 직선상에 장애물이 있다
					} else if (CharPosUtil.isAreaAttack((L1Character) target,
							pc.getX(), pc.getY(), pc.getMapId()) == false) {
						pc.sendPackets(new S_AttackPacket(pc, 0,
								ActionCodes.ACTION_Wand));
						Broadcaster.broadcastPacket(pc, new S_AttackPacket(pc,
								0, ActionCodes.ACTION_Wand));
						return; // 직선상에 장애물이 있다
					} else {
						if (target.getId() == pc.getId()) {
							pc.sendPackets(new S_SystemMessage(
									"아무일도 일어나지 않았습니다."));
							return;
						}
						if (!(target instanceof L1MonsterInstance)) {
							return;
						}
						L1MonsterInstance tar = (L1MonsterInstance) target;
						ArrayList<L1MonsterInstance> tempcha = null;
						tempcha = new ArrayList<L1MonsterInstance>();
						tempcha.add(tar);
						for (L1Object obj : L1World.getInstance()
								.getVisibleObjects(target, 3)) {
							if (obj instanceof L1MonsterInstance) {
								L1MonsterInstance temptarget = (L1MonsterInstance) obj;
								if (temptarget.getId() == pc.getId()
										|| tar == obj || temptarget.isDead()) {
									continue;
								}
								tempcha.add(temptarget);
							}
						}
						if (tempcha.size() > 0) {
							L1MonsterInstance chalist[] = new L1MonsterInstance[tempcha
									.size()];
							for (int i = 0; i < tempcha.size(); i++) {
								chalist[i] = tempcha.get(i);
								doWandAction(pc, chalist[i]);
							}
							pc.sendPackets(new S_RangeSkill(pc, tar, target.getX(),
									target.getY(), chalist, 762, 17,
									S_RangeSkill.TYPE_NODIR));
							Broadcaster.broadcastPacket(pc, new S_RangeSkill(
									pc, tar, target.getX(), target.getY(), chalist,
									762, 17, S_RangeSkill.TYPE_NODIR));
						}
					}
				} else {
					return;
				}
				L1ItemDelay.onItemUse(pc, useItem); // 아이템 지연 개시
				// useItem.setChargeCount(useItem.getChargeCount() - 1);
				// pc.getInventory().updateItem(useItem,
				// L1PcInventory.COL_CHARGE_COUNT);
				// if (useItem.getChargeCount() == 0){
				pc.getInventory().removeItem(useItem, 1);
				// }

				/** 2011.07.01 고정수 수량성 아이템 미확인으로 되는 문제 */
				/*
				 * if (useItem.isIdentified()) { useItem.setIdentified(true);
				 * pc.sendPackets(new S_ItemName(useItem)); }
				 */
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void doWandAction(L1PcInstance user, L1MonsterInstance target) {

		if (CharPosUtil.isAreaAttack(user, target.getX(), target.getY(),
				target.getMapId()) == false) {
			return; // 직선상에 장애물이 있다
		}
		if (CharPosUtil.isAreaAttack((L1Character) target, user.getX(),
				user.getY(), user.getMapId()) == false) {
			return; // 직선상에 장애물이 있다
		}

		// XXX 적당한 데미지 계산, 요점 수정
		int dmg = _random.nextInt(200) + 200;
		target.receiveDamage(user, dmg);
	}

	public boolean isFreeze(L1PcInstance pc) {
		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_FREEZE)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.ABSOLUTE_BARRIER)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.ICE_LANCE)
				|| pc.getSkillEffectTimerSet().hasSkillEffect(
						L1SkillId.EARTH_BIND)) {
			return true;
		}
		return false;
	}
}
