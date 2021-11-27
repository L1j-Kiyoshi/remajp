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

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.BONE_BREAK;
import static l1j.server.server.model.skill.L1SkillId.CURSE_PARALYZE;
import static l1j.server.server.model.skill.L1SkillId.CURSE_PARALYZE2;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.FIRE_WALL;
import static l1j.server.server.model.skill.L1SkillId.FOG_OF_SLEEPING;
import static l1j.server.server.model.skill.L1SkillId.FORCE_STUN;
import static l1j.server.server.model.skill.L1SkillId.FREEZING_BREATH;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.INVISIBILITY;
import static l1j.server.server.model.skill.L1SkillId.LIFE_STREAM;
import static l1j.server.server.model.skill.L1SkillId.MASS_TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.MEDITATION;
import static l1j.server.server.model.skill.L1SkillId.MOB_BASILL;
import static l1j.server.server.model.skill.L1SkillId.MOB_COCA;
import static l1j.server.server.model.skill.L1SkillId.MOB_RANGESTUN_18;
import static l1j.server.server.model.skill.L1SkillId.MOB_RANGESTUN_19;
import static l1j.server.server.model.skill.L1SkillId.MOB_SHOCKSTUN_30;
import static l1j.server.server.model.skill.L1SkillId.쉐도우스탭;
import static l1j.server.server.model.skill.L1SkillId.PANTERA;
import static l1j.server.server.model.skill.L1SkillId.PHANTASM;
import static l1j.server.server.model.skill.L1SkillId.SCALES_EARTH_DRAGON;
import static l1j.server.server.model.skill.L1SkillId.SCALES_FIRE_DRAGON;
import static l1j.server.server.model.skill.L1SkillId.SCALES_Lind_DRAGON;
import static l1j.server.server.model.skill.L1SkillId.SCALES_WATER_DRAGON;
import static l1j.server.server.model.skill.L1SkillId.SHOCK_STUN;
import static l1j.server.server.model.skill.L1SkillId.TELEPORT;
import static l1j.server.server.model.skill.L1SkillId.TRUE_TARGET;
import static l1j.server.server.model.skill.L1SkillId.뫼비우스;
import static l1j.server.server.model.skill.L1SkillId.엠파이어;

import l1j.server.Config;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.server.GMCommands;
import l1j.server.server.datatables.PhoneCheck;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.AcceleratorChecker;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_NewSkillDealay;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;
import server.LineageClient;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

public class C_UseSkill extends ClientBasePacket {
	public C_UseSkill(byte abyte0[], LineageClient client) throws Exception {
		super(abyte0);
		
		try {
			long currentMillis = System.currentTimeMillis();
			L1PcInstance pc = client.getActiveChar();
			int row = readH();
			int skillId = 0;
			String message = null;
			int targetId = 0;
			int targetX = 0;
			int targetY = 0;
			
			if(row >= 5000 && row <= 5002) {
				skillId = row  - 4765;
			} else if(row == 5003) {
				skillId =239;
			} else if(row == 5004) {
				skillId =238;
			} else {
				skillId = row  + 1;
			}
			
			if (GMCommands.마법속도체크 && pc.isGm()) {
				long time = System.currentTimeMillis();
				long eve = 0;
				if (pc._skilltime != 0) {
					eve = time - pc._skilltime;
					if (eve != 0) {
						pc.sendPackets(new S_SystemMessage("클라 마법 평균 속도 : " + eve));
					}
				}
				pc._skilltime = time;
			}
			
			//System.out.println("row" + row);

			if (pc.isTeleport() || pc.isDead() || pc.isGhost()) {
				return;
			}
			
			if (!pc.isSkillMastery(skillId)) {
				return;
			}
			
			if (!pc.getMap().isUsableSkill()) {
				S_ServerMessage sm = new S_ServerMessage(563);
				pc.sendPackets(sm, true);
				return;
			}
			
			/*
			 * if(PhoneCheck.getnocheck(pc.getAccountName())){
			 * pc.sendPackets(new S_SystemMessage("폰인증 미입력으로 감옥에 있어야 합니다."));
			 * return; }
			 */

			if (Config.폰인증) {
				if (PhoneCheck.getnocheck(pc.getAccountName())) {
					pc.sendPackets(new S_SystemMessage(
							"폰인증을위해 대기중 입니다. 자동으로 텔이 안될경우 운영자에게 문의하십시오."));
					return;
				}
			}

			/** 제어 스킬 중에 임의로 아이템 사용 못하게 **/
			if (pc.getSkillEffectTimerSet().hasSkillEffect(SHOCK_STUN)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(엠파이어)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(PANTERA)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(FORCE_STUN)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(MOB_SHOCKSTUN_30)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(MOB_RANGESTUN_19)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(MOB_RANGESTUN_18)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(MOB_BASILL)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(BONE_BREAK)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(PHANTASM)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(FOG_OF_SLEEPING)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(CURSE_PARALYZE)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(CURSE_PARALYZE2)) {
				
				return;
			}

			
			if (GMCommands.트리플포우스핵 && (skillId == L1SkillId.TRIPLE_ARROW || skillId == L1SkillId.FOU_SLAYER)) {
			} else {
				int result;
				result = pc.getAcceleratorChecker().checkSkillInterval(skillId);
				if (result != AcceleratorChecker.R_OK) {
					/*
					 * pc.add_hc(); if(skillId == L1SkillId.BLOODY_SOUL ||
					 * skillId == L1SkillId.TRIPLE_ARROW || skillId ==
					 * L1SkillId.FOU_SLAYER){ }else{ S_SystemMessage sm = new
					 * S_SystemMessage("주의하세요 불법 프로그램 사용은 제제 대상이 됩니다.");
					 * pc.sendPackets(sm, true); L1Teleport.teleport(pc,
					 * pc.getX(), pc.getY(), pc.getMapId(),
					 * pc.getMoveState().getHeading()); }
					 */
					/*
					 * if(pc.get_hc()>4){ pc.getNetConnection().kick();
					 * pc.getNetConnection().close(); }
					 */
					return;
				} else {
					// pc.radd_hc();
				}
			}

			// 요구 간격을 체크한다
			/*
			 * if (Config.CHECK_SPELL_INTERVAL && !(skillId == FOU_SLAYER)) {
			 * int result; // FIXME 어느 스킬이 dir/no dir일까의 판단이 적당 if
			 * (SkillsTable.getInstance(). getTemplate(skillId). getActionId()
			 * == ActionCodes.ACTION_SkillAttack) { result =
			 * pc.getAcceleratorChecker(). checkInterval(
			 * AcceleratorChecker.ACT_TYPE.SPELL_DIR); } else { result =
			 * pc.getAcceleratorChecker(). checkInterval(
			 * AcceleratorChecker.ACT_TYPE.SPELL_NODIR); } if (result ==
			 * AcceleratorChecker.R_DISCONNECTED) { return; } }
			 */

			if (pc.getMapId() == 1931) {
				if (pc.getInventory().getSize() >= 180) {
					pc.sendPackets(new S_SystemMessage("인벤토리를 갯수를 비운 후 입장해주세요."), true);
					L1Teleport.teleport(pc, 33443, 32797, (short) 4, 5, true);
					return;
				}
			}

			if (skillId == L1SkillId.CANCELLATION) {
				if (pc.isInvisble()) {
					pc.delInvis();
				}
			}
			
			if (!pc.아우라키아) {
				if (skillId == SCALES_EARTH_DRAGON
						|| skillId == SCALES_WATER_DRAGON
						|| skillId == SCALES_FIRE_DRAGON || skillId == SCALES_Lind_DRAGON) {
					if (pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_EARTH_DRAGON)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(SCALES_EARTH_DRAGON);
						// return;
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_WATER_DRAGON)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(SCALES_WATER_DRAGON);
						// return;
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(	SCALES_FIRE_DRAGON)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(SCALES_FIRE_DRAGON);
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_Lind_DRAGON)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(SCALES_Lind_DRAGON);

					}
				}
			} else if(pc.아우라키아) {
				if (skillId == SCALES_EARTH_DRAGON
						|| skillId == SCALES_WATER_DRAGON
						|| skillId == SCALES_FIRE_DRAGON || skillId == SCALES_Lind_DRAGON) {
					if ((pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_EARTH_DRAGON) && pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_WATER_DRAGON)) 
							||(pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_EARTH_DRAGON) && pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_FIRE_DRAGON))
							||(pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_EARTH_DRAGON) && pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_Lind_DRAGON))
							||(pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_WATER_DRAGON) && pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_FIRE_DRAGON))
							||(pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_WATER_DRAGON) && pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_Lind_DRAGON))
							||(pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_FIRE_DRAGON) && pc.getSkillEffectTimerSet().hasSkillEffect(SCALES_Lind_DRAGON))) {
						pc.getSkillEffectTimerSet().removeSkillEffect(SCALES_EARTH_DRAGON);
						pc.getSkillEffectTimerSet().removeSkillEffect(SCALES_WATER_DRAGON);
						pc.getSkillEffectTimerSet().removeSkillEffect(SCALES_FIRE_DRAGON);
						pc.getSkillEffectTimerSet().removeSkillEffect(SCALES_Lind_DRAGON);
					}
				}
			}

			
			if (abyte0.length > 4) {
				try {
					
					switch (skillId) {
					/*case CALL_CLAN:
					case RUN_CLAN:
						charName = readS();
						break;*/
					case TRUE_TARGET:
						targetId = readD();
						targetX = readH();
						targetY = readH();
						// message = readS();
						break;
						
					case TELEPORT:
					case MASS_TELEPORT:
						targetId = readH(); // MapID
						// targetId = readD(); // Bookmark ID
						targetX = readH();
						targetY = readH();
						break;
						
					case FIRE_WALL:
					case LIFE_STREAM:
						targetX = readH();
						targetY = readH();
						break;
						//					case JUDGEMENT:
						//					case PHANTOM:
						//					case PANTERA:
						//					case HELLFIRE:
						//					case BLADE:
						//						skillId = row  + 101;
						//					    break;
					case L1SkillId.SUMMON_MONSTER:
						targetId = readH();
						break;
						
					default: {
						int target1 = readD();
						L1Object target = L1World.getInstance().findObject(target1);
						targetId = target.getId();
						targetX = target.getX();
						targetY = target.getY();
					}
					break;
					}
					
				} catch (Exception e) {
					// _log.log(Level.SEVERE, "", e);
				}
			}

			if (skillId != TELEPORT && pc.getSkillEffectTimerSet().hasSkillEffect(ABSOLUTE_BARRIER)) { // 아브소르트바리아의 해제
				pc.getSkillEffectTimerSet().killSkillEffectTimer(ABSOLUTE_BARRIER);
				pc.startHpRegenerationByDoll();
				pc.startMpRegenerationByDoll();
			}

			if (skillId != TELEPORT 	&& pc.getSkillEffectTimerSet().hasSkillEffect(뫼비우스)) { // 아브소르트바리아의 해제
				pc.getSkillEffectTimerSet().killSkillEffectTimer(뫼비우스);
				pc.startHpRegenerationByDoll();
				pc.startMpRegenerationByDoll();
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_안전모드)) {
				pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_안전모드);
			}

			pc.getSkillEffectTimerSet().killSkillEffectTimer(MEDITATION);

			try {
				L1Object target2 = L1World.getInstance().findObject(targetId);
				if (target2 != null && SkillsTable.getInstance().getTemplate(skillId).getTarget().equalsIgnoreCase("attack")) {
					if (skillId != L1SkillId.FIRE_WALL) {
						int calcx = (int) pc.getX() - target2.getLocation().getX();
						int calcy = (int) pc.getY() 	- target2.getLocation().getY();
						int range = SkillsTable.getInstance().getTemplate(skillId).getRanged();
						if (range != -1) {
							if (Math.abs(calcx) > range || Math.abs(calcy) > range) {
								return;
							}
						}
					}
				}

				if (!pc.isGm() && target2 != null && target2 instanceof L1PcInstance && skillId == L1SkillId.CANCELLATION) {
					L1PcInstance tpc = (L1PcInstance) target2;
					if (tpc.getMapId() == 2005) {
						return;
					}
				}

				/*
				 * if (target2 != null && target2 instanceof L1PcInstance){
				 * if(SkillsTable.getInstance(). getTemplate(skillId).
				 * getActionId() == ActionCodes.ACTION_SkillAttack){ if
				 * (pc.getMapId()==777 ||pc.getMapId()==778
				 * ||pc.getMapId()==779) { pc.sendPackets(new
				 * S_SystemMessage("버땅에서 PK는 금지 입니다."), true); return; }
				 * 
				 * }else{ if(skillId == L1SkillId.CANCELLATION ||skillId ==
				 * L1SkillId.SLOW ||skillId == L1SkillId.DECAY_POTION ||skillId
				 * == L1SkillId.MANA_DRAIN ||skillId == L1SkillId.SILENCE){ if
				 * (pc.getMapId()==777 ||pc.getMapId()==778
				 * ||pc.getMapId()==779) { pc.sendPackets(new
				 * S_SystemMessage("버땅에서 상대에게 해로운 마법은 금지 입니다."), true); return;
				 * } } } }
				 */
				/*if (skillId == CALL_CLAN || skillId == RUN_CLAN) {
					if (charName.isEmpty()) {
						return;
					}

					StringBuffer sb = new StringBuffer();
					for (int i = 0; i < charName.length(); i++) {
						if (charName.charAt(i) == '[') {
							break;
						}
						sb.append(charName.charAt(i));
					}

					L1PcInstance target = L1World.getInstance().getPlayer(
							sb.toString());
					sb = null;

					if (target == null) {
						pc.sendPackets(new S_ServerMessage(73, charName), true);
						return;
					}

					if (pc.isPinkName() || target.isPinkName()) {
						pc.sendPackets(new S_SystemMessage(
								"시전자 또는 대상이 전투 중입니다."), true);
						return;
					}

					if (pc.isGhost()) {
						pc.sendPackets(new S_SystemMessage(
								"현재 시전 할 수 없는 대상입니다."), true);
						return;
					}

					if (pc.getClan() == null || pc.getClanid() == 0) {
						pc.sendPackets(new S_SystemMessage(
								"혈맹을 창설해야 사용 가능 합니다."), true);
						return;
					}

					if (pc.getClanid() != target.getClanid()) {
						pc.sendPackets(new S_ServerMessage(414), true);
						return;
					}

					targetId = target.getId();
					if (skillId == CALL_CLAN) {
						int callClanId = pc.getCallClanId();
						if (callClanId == 0 || callClanId != targetId) {
							pc.setCallClanId(targetId);
							pc.setCallClanHeading(pc.getMoveState()
									.getHeading());
						}
					}
				}*/
				
				if (skillId == INVISIBILITY) {
					if (pc.getMapId() == 5153) {
						pc.sendPackets(new S_ServerMessage(563), true); // \f1
						// 여기에서는
						// 사용할 수
						// 없습니다.
						return;
					}

				}
				
				/*
				 * switch(skillId){ case 4: //에너지 볼트 case 6: //아이스 대거 case 7:
				 * //윈드 커터 case 10: //칠 터치 case 15: //파이어 애로우 case 16: //스탈락
				 * case 28: //뱀파이어릭 터치 case 30: //어스 재일 case 34: //콜 라이트닝 case
				 * 38: //콘 오브 콜드 case 45: //이럽션 case 46: //선 버스트 case 77:
				 * //디스인티그레이트 case 108: //파이널 번 case 132: //트리플 애로우 case 187:
				 * //포우 슬레이어 case 203: //스매쉬 new AttackSkill(pc, skillId,
				 * targetId, targetX, targetY); return; }
				 */
				// if(skillId == L1SkillId.AREA_OF_SILENCE)
				// GeneralThreadPool.getInstance().execute(new areaSilence(pc));
				pc.플레이어상태 = pc.공격_상태;
				pc.상태시간 = System.currentTimeMillis() + 2000;
				pc.sendPackets(new S_NewSkillDealay(), true);
				L1SkillUse l1skilluse = new L1SkillUse();
				L1Skills skill = SkillsTable.getInstance().getTemplate(skillId);
				long j = pc.getCurrentSpriteInterval(skill.getTarget().equals("attack") ? EActionCodes.spell_dir : EActionCodes.spell_nodir);
				long l = (SpriteInformationLoader.getInstance().getUseSpellInterval(pc, skillId) + j) - 5L;
				pc.lastSpellUseMillis = currentMillis + (l > 0 ? l : 0L);
				l1skilluse.handleCommands(pc, skillId, targetId, targetX, targetY, message, 0, L1SkillUse.TYPE_NORMAL);
				pc.sendPackets(new S_NewSkillDealay(pc, S_NewSkillDealay.RemasterSkillDealay, skillId, (int) l), true);
				pc.sendPackets(new S_NewSkillDealay(), true);
				l1skilluse = null;

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}
	

	class areaSilence implements Runnable {

		private L1PcInstance pc;

		public areaSilence(L1PcInstance _pc) {
			pc = _pc;
		}

		@Override
		public void run() {
			int i = 10;
			try {
				while (i > 0) {
					if (pc == null || pc.getNetConnection() == null) {
						return;
					}
					
					if (i <= 3) {
						pc.sendPackets(new S_SystemMessage(i + "초 - 에어리어 오브 사일런스 쿨타임"), true);
					}
					
					i--;
					Thread.sleep(1000);
				}
				
				pc.sendPackets(new S_SystemMessage("에어리어 오브 사일런스 쿨타임 종료"), true);
			} catch (Exception e) {
			}
		}

	}
	
	
}
