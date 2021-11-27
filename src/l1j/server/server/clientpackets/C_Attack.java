/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.clientpackets;

import static l1j.server.server.model.Instance.L1PcInstance.REGENSTATE_ATTACK;
import l1j.server.Config;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.PhoneCheck;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.model.AcceleratorChecker;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1LittleBugInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_AttackCritical;
import l1j.server.server.serverpackets.S_AttackPacket;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_UseArrowSkill;
import server.LineageClient;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

public class C_Attack extends ClientBasePacket {

	private int _targetX = 0;
	private int _targetY = 0;
	private static final S_SystemMessage sm = new S_SystemMessage("소지품이 너무 무거워서 전투를 할 수 없습니다.");

	public C_Attack(byte[] decrypt, LineageClient client) {
		super(decrypt);
		try {
			int targetId = readD();
			int x = readH();
			int y = readH();
			_targetX = x;
			_targetY = y;
			L1PcInstance pc = client.getActiveChar();
			L1Object target = L1World.getInstance().findObject(targetId);
			pc.testobj = targetId;
			boolean 얼던 = false;
			if (pc == null || pc.isGhost() || pc.isDead() || pc.isTeleport()) {
				return;
			}

			if (pc.isGm()) {
				if (target instanceof L1NpcInstance) {
					pc._npcnum = ((L1NpcInstance) target).getNpcTemplate().get_npcId();
					pc._npcname = ((L1NpcInstance) target).getNpcTemplate().get_name();
					pc.sendPackets(new S_SystemMessage("npcid :" + pc._npcnum + " 이름 :" + pc._npcname));
				}
				if (target instanceof L1DoorInstance) {
					L1DoorInstance fi = (L1DoorInstance) target;
					pc.sendPackets(new S_SystemMessage("doorid :" + fi.getDoorId()));
				}
			}

			/*
			 * if(PhoneCheck.getnocheck(pc.getAccountName())){ pc.sendPackets(new
			 * S_SystemMessage("폰인증 미입력으로 감옥에 있어야 합니다.")); return; }
			 */
			/*
			 * long time = System.currentTimeMillis();
			 * 
			 * long eve = 0;
			 * 
			 * if(pc._attacktime!=0){ eve = time - pc._attacktime; if(eve != 0){
			 * pc.sendPackets(new S_SystemMessage("클라 속도 : "+ eve)); } }
			 * 
			 * pc._attacktime = time;
			 */

			if (pc.isInvisble()) {
				if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.BLIND_HIDING)) {
					if (!pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.어쌔신)) {
						return;
					} else {
						pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.어쌔신);
						pc.어쌔신 = true;//
					}
				} else {
					return;
				}
			}

			if (pc.isInvisDelay()) {
				return;
			}

			if (pc.getInventory().calcWeightpercent() >= 83) {
				pc.sendPackets(sm); // \f1아이템이 너무 무거워 전투할 수가 없습니다.
				return;
			}

			if (pc.getMapId() == 1931) {
				if (pc.getInventory().getSize() >= 180) {
					pc.sendPackets(new S_SystemMessage("인벤토리를 갯수를 비운 후 입장해주세요."), true);
					L1Teleport.teleport(pc, 33443, 32797, (short) 4, 5, true);
					return;
				}
			}

			if (pc.isPrivateShop()) {
				pc.sendPackets(new S_SystemMessage("상상 자위하지마 색햐ㅋ"), true);
				// pc.getNetConnection().close();
				return;
			}

			if (Config.폰인증) {
				if (PhoneCheck.getnocheck(pc.getAccountName())) {
					pc.sendPackets(new S_SystemMessage("폰인증을위해 대기중 입니다. 자동으로 텔이 안될경우 운영자에게 문의하십시오."));
					return;
				}
			}

			/*
			 * if (target instanceof L1PcInstance){ if (pc.getMapId()==777
			 * ||pc.getMapId()==778 ||pc.getMapId()==779) { S_SystemMessage sm = new
			 * S_SystemMessage("버땅에서 PK는 금지 입니다."); pc.sendPackets(sm); sm.clear(); sm =
			 * null; return; } }
			 */
			if (target != null && target instanceof L1LittleBugInstance) {
				return;
			}
			if (target != null && target instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) target;
				if (npc.getNpcTemplate().get_npcId() == 90000 || npc.getNpcTemplate().get_npcId() == 90002 || npc.getNpcTemplate().get_npcId() == 90009
						|| npc.getNpcTemplate().get_npcId() == 90013 || npc.getNpcTemplate().get_npcId() == 90016) {// 얼던
					얼던 = true;
				}

				int hiddenStatus = ((L1NpcInstance) target).getHiddenStatus();
				if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK || hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY) {
					return;
				}
			}

			// if (Config.CHECK_ATTACK_INTERVAL) {
			int result = pc.getAcceleratorChecker().checkInterval(AcceleratorChecker.ACT_TYPE.ATTACK);
			switch (result) {
			case AcceleratorChecker.R_DETECTED:
			case AcceleratorChecker.R_DISCONNECTED:
				return;
			}
			// }

			// 공격 액션을 취할 수 있는 경우의 처리
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.ABSOLUTE_BARRIER)) { // 아브소르트바리아의 해제
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.ABSOLUTE_BARRIER);
				// pc.startHpRegeneration();
				// pc.startMpRegeneration();
				pc.startHpRegenerationByDoll();
				pc.startMpRegenerationByDoll();
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.뫼비우스)) { // 아브소르트바리아의 해제
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.뫼비우스);
				// pc.startHpRegeneration();
				// pc.startMpRegeneration();
				pc.startHpRegenerationByDoll();
				pc.startMpRegenerationByDoll();
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_안전모드)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_안전모드);
			}
			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.MEDITATION)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.MEDITATION);
			}

			pc.attacking = true;
			pc.delInvis();
			pc.setRegenState(REGENSTATE_ATTACK);
			L1Character cha = null;
			if (target != null && target instanceof L1Character) {
				cha = (L1Character) target;
			}
			// pc.sendPackets(new S_RemoveObject(cha));

			if (target == null) {
				for (L1Object obj : pc.getNearObjects().getKnownObjects()) {
					if (obj == null)
						continue;
					if (obj instanceof L1MonsterInstance) {
						L1MonsterInstance mon = (L1MonsterInstance) obj;
						if (!mon.isDead() && (mon.getNpcId() == 100106 || (mon.getNpcId() >= 100094 && mon.getNpcId() <= 100097))) {
							if (mon.getX() == _targetX && mon.getY() == _targetY) {
								mon.onAction(pc);
								return;
							}
						}
					}
				}
			}

			pc.플레이어상태 = pc.공격_상태;
			pc.상태시간 = System.currentTimeMillis() + 2000;

			if (cha != null && target != null && !cha.isDead() && !얼던) {
				target.onAction(pc);
			} else { // 하늘 공격
				// 활로 지면에 하늘 공격했을 경우는 화살이 날지 않으면 안 된다
				if (얼던) {
					target.onAction(pc);
				}
				int weaponId = 0;
				int weaponType = 0;
				L1ItemInstance weapon = pc.getWeapon();
				L1ItemInstance arrow = null;
				L1ItemInstance sting = null;
				if (weapon != null) {
					weaponId = weapon.getItem().getItemId();
					weaponType = weapon.getItem().getType1();
					if (weaponType == 20) {
						arrow = pc.getInventory().getArrow();
					}
					if (weaponType == 62) {
						sting = pc.getInventory().getSting();
					}
				}
				pc.getMoveState().setHeading(CharPosUtil.targetDirection(pc, x, y)); // 방향세트
				if (weaponType == 20 && (weaponId == 190 || weaponId == 100190 || weaponId == 30082 || weaponId == 30091
						|| (weaponId >= 11011 && weaponId <= 11013) || arrow != null)) {
					calcOrbit(pc.getX(), pc.getY(), pc.getMoveState().getHeading());
					if (arrow != null) {
						if (pc.getGfxId().getTempCharGfx() == 7968 || pc.getGfxId().getTempCharGfx() == 7967) {
							S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 7972, _targetX, _targetY, true);
							pc.sendPackets(ua);
							Broadcaster.broadcastPacket(pc, ua, true);
						} else if (pc.getGfxId().getTempCharGfx() == 8842 || pc.getGfxId().getTempCharGfx() == 8900) { // 헬바인
							S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 8904, _targetX, _targetY, true);
							pc.sendPackets(ua);
							Broadcaster.broadcastPacket(pc, ua, true);
						} else if (pc.getGfxId().getTempCharGfx() == 8845 || pc.getGfxId().getTempCharGfx() == 8913) { // 질리언
							S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 8916, _targetX, _targetY, true);
							pc.sendPackets(ua);
							Broadcaster.broadcastPacket(pc, ua, true);
						} else {
							S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 66, _targetX, _targetY, true);
							pc.sendPackets(ua);
							Broadcaster.broadcastPacket(pc, ua, true);
						}
						pc.getInventory().removeItem(arrow, 1);
					} else if (weaponId == 190 || weaponId == 100190 || weaponId == 30082 || weaponId == 30091) {
						S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 2349, _targetX, _targetY, true);
						pc.sendPackets(ua);
						Broadcaster.broadcastPacket(pc, ua, true);
					} else if (weaponId >= 11011 && weaponId <= 11013) {
						S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 8771, _targetX, _targetY, true);
						pc.sendPackets(ua);
						Broadcaster.broadcastPacket(pc, ua, true);
					} // 추가

				} else if (weaponType == 62 && sting != null) {
					calcOrbit(pc.getX(), pc.getY(), pc.getMoveState().getHeading());
					if (pc.getGfxId().getTempCharGfx() == 7968 || pc.getGfxId().getTempCharGfx() == 7967) {
						S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 7972, _targetX, _targetY, true);
						pc.sendPackets(ua);
						Broadcaster.broadcastPacket(pc, ua, true);
					} else if (pc.getGfxId().getTempCharGfx() == 8842 || pc.getGfxId().getTempCharGfx() == 8900) { // 헬바인
						S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 8904, _targetX, _targetY, true);
						pc.sendPackets(ua);
						Broadcaster.broadcastPacket(pc, ua, true);

					} else if (pc.getGfxId().getTempCharGfx() == 8845 || pc.getGfxId().getTempCharGfx() == 8913) { // 질리언
						S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 8916, _targetX, _targetY, true);
						pc.sendPackets(ua);
						Broadcaster.broadcastPacket(pc, ua, true);
					} else {
						S_UseArrowSkill ua = new S_UseArrowSkill(pc, 0, 2989, _targetX, _targetY, true);
						pc.sendPackets(ua);
						Broadcaster.broadcastPacket(pc, ua, true);
					}
					pc.getInventory().removeItem(sting, 1);
				} else {
					S_AttackPacket ap = new S_AttackPacket(pc, 0, ActionCodes.ACTION_Attack);
					pc.sendPackets(ap);
					Broadcaster.broadcastPacket(pc, ap, true);
				}
				if (pc.getWeapon() != null) {
					if (pc.getWeapon().getItem().getType() == 17) {
						if (pc.getWeapon().getItemId() == 410003) {
							S_SkillSound ss = new S_SkillSound(pc.getId(), 6983);
							pc.sendPackets(ss);
							Broadcaster.broadcastPacket(pc, ss, true);
						} else if (pc.getWeapon().getItemId() == 6001 || pc.getWeapon().getItemId() == 30081 || pc.getWeapon().getItemId() == 31081
								|| pc.getWeapon().getItemId() == 222207 || pc.getWeapon().getItemId() == 30090 || pc.getWeapon().getItemId() == 7238) { // 키링크
																																						// 이펙트
							Broadcaster.broadcastPacket(pc, new S_AttackCritical(pc, target.getId(), 89));
						} else {
							S_SkillSound ss = new S_SkillSound(pc.getId(), 7049);
							pc.sendPackets(ss);
							Broadcaster.broadcastPacket(pc, ss, true);
						}
					}
				}
			}

			// System.out.println("targetid" + targetId);

			GeneralThreadPool.getInstance().schedule(new attacking(pc), calcSleepTime(pc) + 50);

		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			clear();
		}
	}

	class attacking implements Runnable {

		private L1PcInstance pc;

		public attacking(L1PcInstance _pc) {
			pc = _pc;
		}

		@Override
		public void run() {
			try {
				pc.attacking = false;
			} catch (Exception e) {
			}
		}

	}

	private static final byte HEADING_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	private void calcOrbit(int cX, int cY, int head) {
		float disX = Math.abs(cX - _targetX);
		float disY = Math.abs(cY - _targetY);
		float dis = Math.max(disX, disY);
		float avgX = 0;
		float avgY = 0;

		if (dis == 0) {
			avgX = HEADING_X[head];
			avgY = HEADING_Y[head];
		} else {
			avgX = disX / dis;
			avgY = disY / dis;
		}

		int addX = (int) Math.floor((avgX * 15) + 0.59f);
		int addY = (int) Math.floor((avgY * 15) + 0.59f);

		if (cX > _targetX) {
			addX *= -1;
		}
		if (cY > _targetY) {
			addY *= -1;
		}

		_targetX = _targetX + addX;
		_targetY = _targetY + addY;
	}

	protected int calcSleepTime(L1PcInstance pc) {
		int gfxid = pc.getGfxId().getTempCharGfx();
		int weapon = pc.getCurrentWeapon();
		if (gfxid == 3784 || gfxid == 6137 || gfxid == 6142 || gfxid == 6147 || gfxid == 6152 || gfxid == 6157 || gfxid == 9205 || gfxid == 9206
				|| gfxid == 13152 || gfxid == 13153 || gfxid == 12702 || gfxid == 12681 || gfxid == 8812 || gfxid == 15154 || gfxid == 15232 || gfxid == 14923
				|| gfxid == 15223 || gfxid == 8817 || gfxid == 6267 || gfxid == 6270 || gfxid == 6273 || gfxid == 6276 || gfxid == 11328 || gfxid == 11329
				|| gfxid == 11332 || gfxid == 11333 || gfxid == 11334 || gfxid == 11339 || gfxid == 11340 || gfxid == 11341 || gfxid == 11347 || gfxid == 11348
				|| gfxid == 11349 || gfxid == 11350 || gfxid == 11354 || gfxid == 11356 || gfxid == 11357 || gfxid == 11359 || gfxid == 11360 || gfxid == 11361
				|| gfxid == 11364 || gfxid == 11366 || gfxid == 11367 || gfxid == 11370 || gfxid == 11375 || gfxid == 11377 || gfxid == 11379 || gfxid == 11380
				|| gfxid == 11381 || gfxid == 11383 || gfxid == 11384 || gfxid == 11385 || gfxid == 11387 || gfxid == 11388 || gfxid == 11389 || gfxid == 11391
				|| gfxid == 11392 || gfxid == 11393 || gfxid == 11395 || gfxid == 11400 || gfxid == 11401 || gfxid == 11403 || gfxid == 11404 || gfxid == 11405
				|| gfxid == 11407 || gfxid == 11446 || gfxid == 11396 || gfxid == 11397 || gfxid == 11399 || gfxid == 13396 || gfxid == 13393 || gfxid == 13395
				|| gfxid == 16014 || gfxid == 15986 || gfxid == 16027 || gfxid == 16284 || gfxid == 16053 || gfxid == 16040 || gfxid == 17541 || gfxid == 17515
				|| gfxid == 17531) {
			if (weapon == 24 && pc.getWeapon() != null && pc.getWeapon().getItem().getType() == 18) {
				if (gfxid == 13152 || gfxid == 13153 || gfxid == 12702 || gfxid == 15154 || gfxid == 15232 || gfxid == 14923 || gfxid == 15223 || gfxid == 12681
						|| gfxid == 8812 || gfxid == 8817 || gfxid == 6267 || gfxid == 6270 || gfxid == 6273 || gfxid == 6276 || gfxid == 11328
						|| gfxid == 11329 || gfxid == 11332 || gfxid == 11333 || gfxid == 11334 || gfxid == 11339 || gfxid == 11340 || gfxid == 11341
						|| gfxid == 11347 || gfxid == 11348 || gfxid == 11349 || gfxid == 11350 || gfxid == 11354 || gfxid == 11356 || gfxid == 11357
						|| gfxid == 11359 || gfxid == 11360 || gfxid == 11361 || gfxid == 11364 || gfxid == 11366 || gfxid == 11367 || gfxid == 11370
						|| gfxid == 11375 || gfxid == 11377 || gfxid == 11379 || gfxid == 11380 || gfxid == 11381 || gfxid == 11383 || gfxid == 11384
						|| gfxid == 11385 || gfxid == 11387 || gfxid == 11388 || gfxid == 11389 || gfxid == 11391 || gfxid == 11392 || gfxid == 11393
						|| gfxid == 11395 || gfxid == 11400 || gfxid == 11401 || gfxid == 11403 || gfxid == 11404 || gfxid == 11405 || gfxid == 11407
						|| gfxid == 11446 || gfxid == 11396 || gfxid == 11397 || gfxid == 11399 || gfxid == 13396 || gfxid == 13393 || gfxid == 13395
						|| gfxid == 16014 || gfxid == 15986 || gfxid == 16027 || gfxid == 16284 || gfxid == 16053 || gfxid == 16040 || gfxid == 17541
						|| gfxid == 17515 || gfxid == 17531)
					weapon = 50;
				else
					weapon = 83;
			}
		}

		int interval = SprTable.getInstance().getAttackSpeed(gfxid, weapon + 1);
		if (interval == 0) {
			if (weapon + 1 == 1) {
				return 0;
			}
			interval = 640;
		} else {
			if ((gfxid >= 11328 && gfxid <= 11448) || gfxid == 12237 || gfxid == 12702 || gfxid == 12681) {
				if (pc.getLevel() >= 15)
					interval -= 43;
				if (pc.getLevel() >= 30)
					interval -= 43;
				if (pc.getLevel() >= 45)
					interval -= 34;
				if (pc.getLevel() >= 50)
					interval -= 34;
				if (pc.getLevel() >= 52)
					interval -= 25;
				if (pc.getLevel() >= 55)
					interval -= 24;
				if (pc.getLevel() >= 60)
					interval -= 22;
				if (pc.getLevel() >= 65)
					interval -= 21;
				if (pc.getLevel() >= 70)
					interval -= 16;
				if (pc.getLevel() >= 75)
					interval -= 16;
				if (pc.getLevel() >= 80)
					interval -= 16;
			}
		}

		if (pc.isHaste()) {
			interval *= L1PcInstance.HASTE_RATE;
		}
		if (pc.isBloodLust()) { // 블러드러스트
			interval *= L1PcInstance.HASTE_RATE;
		}
		if (pc.isSandstorm()) { // 샌드스톰
			interval *= L1PcInstance.HASTE_RATE;
		}
		if (pc.isHurricane()) { // 허리케인
			interval *= L1PcInstance.HASTE_RATE;
		}
		if (pc.isFocuswave()) { // 포커스웨이브
			interval *= L1PcInstance.HASTE_RATE;
		}
		if (pc.isdarkhos()) { // 포커스웨이브
			interval *= L1PcInstance.HASTE_RATE;
		}
		if (pc.isBrave()) {
			interval *= L1PcInstance.HASTE_RATE;
		}
		if (pc.isElfBrave()) {
			interval *= L1PcInstance.WAFFLE_RATE;
		}
		if (pc.isThirdSpeed()) {
			interval *= L1PcInstance.THIRDSPEED_RATE;
		}
		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.WIND_SHACKLE)) {
			interval *= 2;
		}
		if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.SLOW)) {
			interval *= 2;
		}
		return interval;
	}
}
