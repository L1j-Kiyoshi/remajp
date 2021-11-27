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
package l1j.server.server.model;

import static l1j.server.server.model.skill.L1SkillId.ABSOLUTE_BARRIER;
import static l1j.server.server.model.skill.L1SkillId.COUNTER_MAGIC;
import static l1j.server.server.model.skill.L1SkillId.EARTH_BIND;
import static l1j.server.server.model.skill.L1SkillId.FREEZING_BREATH;
import static l1j.server.server.model.skill.L1SkillId.ICE_LANCE;
import static l1j.server.server.model.skill.L1SkillId.MOB_BASILL;
import static l1j.server.server.model.skill.L1SkillId.MOB_COCA;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL1;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL10;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL11;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL2;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL3;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL4;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL5;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL6;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL7;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL8;
import static l1j.server.server.model.skill.L1SkillId.PAPOO_SKILL9;

import java.lang.reflect.Constructor;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.GameSystem.Papoo.PaPooRaid;
import l1j.server.GameSystem.Papoo.PaPooRaidSystem;
import l1j.server.GameSystem.Papoo.PaPooTimer;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.MobSkillTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.templates.L1MobSkill;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.L1SpawnUtil;

public class L1MobSkillUse {
	private static Logger _log = Logger
			.getLogger(L1MobSkillUse.class.getName());

	private L1MobSkill _mobSkillTemplate = null;

	private L1NpcInstance _attacker = null;

	private L1Character _target = null;

	private Random _rnd = new Random(System.nanoTime());

	private int _sleepTime = 0;

	private int _skillUseCount[];
	private int ia;
	private int randcount = _rnd.nextInt(2) + 1;
	// private L1MonsterInstance mob = null;
	Timestamp dt = null;

	public L1MobSkillUse(L1NpcInstance npc) {
		_sleepTime = 0;

		_mobSkillTemplate = MobSkillTable.getInstance().getTemplate(npc.getNpcTemplate().get_npcId());
		if (_mobSkillTemplate == null) {
			return;
		}
		_attacker = npc;
		_skillUseCount = new int[getMobSkillTemplate().getSkillSize()];
	}

	private int getSkillUseCount(int idx) {
		return _skillUseCount[idx];
	}

	private void skillUseCountUp(int idx) {
		_skillUseCount[idx]++;
	}

	public void resetAllSkillUseCount() {
		if (getMobSkillTemplate() == null) {
			return;
		}

		for (int i = 0; i < getMobSkillTemplate().getSkillSize(); i++) {
			_skillUseCount[i] = 0;
		}
	}

	public int getSleepTime() {
		return _sleepTime;
	}

	public void setSleepTime(int i) {
		_sleepTime = i;
	}

	public L1MobSkill getMobSkillTemplate() {
		return _mobSkillTemplate;
	}

	public boolean isSkillTrigger(L1Character tg) {
		if (_mobSkillTemplate == null) {
			return false;
		}
		_target = tg;

		int type;
		type = getMobSkillTemplate().getType(0);

		if (type == L1MobSkill.TYPE_NONE) {
			return false;
		}

		int i = 0;
		for (i = 0; i < getMobSkillTemplate().getSkillSize()
				&& getMobSkillTemplate().getType(i) != L1MobSkill.TYPE_NONE; i++) {

			int changeType = getMobSkillTemplate().getChangeTarget(i);
			if (changeType > 0) {
				_target = changeTarget(changeType, i);
			} else {
				_target = tg;
			}

			if (isSkillUseble(i, true)) {// 원래 false 였음 확률 적용안되게
				return true;
			}
		}
		return false;
	}

	public boolean skillUse(L1Character tg, boolean isTriRnd) {
		if (_mobSkillTemplate == null) {
			return false;
		}
		_target = tg;

		int type;
		type = getMobSkillTemplate().getType(0);

		if (type == L1MobSkill.TYPE_NONE) {
			return false;
		}
		byte[] skills = null;
		int skillSizeCounter = 0;
		int skillSize = getMobSkillTemplate().getSkillSize();
		if (skillSize >= 0) {
			skills = new byte[skillSize];
		}
		int i = 0;
		for (i = 0; i < getMobSkillTemplate().getSkillSize() && getMobSkillTemplate().getType(i) != L1MobSkill.TYPE_NONE; i++) {

			int changeType = getMobSkillTemplate().getChangeTarget(i);
			if (changeType > 0) {
				_target = changeTarget(changeType, i);
			} else {
				_target = tg;
			}

			if (isSkillUseble(i, isTriRnd) == false) {
				continue;
			} else {
				skills[skillSizeCounter] = (byte) i;
				skillSizeCounter++;
			}
		}
		if (skillSizeCounter != 0) {
			int num = _rnd.nextInt(skillSizeCounter);
			if (useSkill(skills[num])) {
				return true;
			}
		}
		return false;
	}

	private boolean useSkill(int i) {
		boolean isUseSkill = false;
		int type = getMobSkillTemplate().getType(i);
		// 린드, 샌드웜, 에르자베
		if ((_attacker.getNpcId() >= 100012 && _attacker.getNpcId() <= 100014)
				|| _attacker.getNpcId() == 100420
				|| _attacker.getNpcId() == 100338
				|| _attacker.getNpcId() == 45601) {
			if (type == L1MobSkill.TYPE_MAGIC_ATTACK) {
				if (SendMsg(i)) {
					if (_attacker.getNpcId() == 45601) {
						if (i == 2) {
							for (L1PcInstance cha : L1World.getInstance()
									.getVisiblePlayer(_attacker, 8)) {
								if (cha != null && !cha.isGhost()
										&& !cha.isDead()) {
									L1SkillUse su = new L1SkillUse();
									su.handleCommands(cha, 44, cha.getId(),
											cha.getX(), cha.getY(), null, 0,
											L1SkillUse.TYPE_GMBUFF);
									su = null;
								}
							}
						}
					}
					GeneralThreadPool.getInstance().schedule(
							new delayUseSkill(i, _target), 3000);
					return false;
				}
			}
		}
		switch (type) {
		case L1MobSkill.TYPE_PHYSICAL_ATTACK:
			if (physicalAttack(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
				SendMsg(i);
				AddSkill(i);
			}
			break;
		case L1MobSkill.TYPE_MAGIC_ATTACK:
			if (magicAttack(i, _target) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
				SendMsg(i);
				AddSkill(i);
			}
			break;
		case L1MobSkill.TYPE_SUMMON:
			if (summon(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
				SendMsg(i);
				AddSkill(i);
			}
			break;
		case L1MobSkill.TYPE_POLY:
			if (poly(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
				SendMsg(i);
			}
			break;
		case L1MobSkill.TYPE_PAPOO_1:
			if (papoo() == true) {
				skillUseCountUp(i);
				isUseSkill = true;
			}
			break;
		case L1MobSkill.TYPE_PAPOO_2:
			if (papoo2() == true) {
				skillUseCountUp(i);
				isUseSkill = true;
			}
			break;
		case L1MobSkill.TYPE_PAPOO_3:
			if (papoo3() == true) {
				skillUseCountUp(i);
				isUseSkill = true;
			}
			break;
		case L1MobSkill.TYPE_LINE_ATTACK:
			if (LineMagicAttack(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
				SendMsg(i);
				AddSkill(i);
			}
			break;
		case L1MobSkill.TYPE_KIRTAS_METEOR:
			if (KirMeteorAttack(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
				SendMsg(i);
				AddSkill(i);
			}
			break;
		case L1MobSkill.TYPE_KIRTAS_BARRIER:
			if (KirBarrier(i) == true) {
				skillUseCountUp(i);
				isUseSkill = true;
				SendMsg(i);
				AddSkill(i);
			}
			break;
		}
		return isUseSkill;
	}

	/** 스킬 사용시 추가 스킬 적용 **/
	/** 44켄슬레이션 87쇼크스턴 73데스힐 */
	private void AddSkill(int i) {
		if (_attacker == null)
			return;
		int areaWidth = getMobSkillTemplate().getAreaWidth(i);
		int areaHeight = getMobSkillTemplate().getAreaHeight(i);
		switch (_attacker.getNpcTemplate().get_npcId()) {
		case 100002: // 우그누스 광역 켄슬
			if (i == 1 || i == 2) {
				ArrayList<L1PcInstance> li = AreaBoxList(areaWidth, areaHeight);
				for (L1PcInstance cha : li) {
					if (cha != null && cha.getResistance().getMr() < 148) {
						L1SkillUse su = new L1SkillUse();
						su.handleCommands(cha, 44, cha.getId(), cha.getX(),
								cha.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						su = null;
					}
				}
				if (li.size() > 0)
					li.clear();
				li = null;
			} else if 
			
			(i == 3)
			{	skillUse(10, 87);
			
			}
			else
			break;
		case 46025: // 타로스 광역 데스힐
			if (i == 0) {
				skillUse(10, 73);
			}else if (i == 2){
				skillUse(3, 87);
			}
			break;
		case 910075: 
			if (i == 2) {
				L1SkillUse su = new L1SkillUse();
				su.handleCommands((L1PcInstance) _target, 15028, _target.getId(),
						_target.getX(), _target.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				su = null;
			}
			break;
		case 45673: // 사신 그림 리퍼 광역스턴
			if (i == 5) {// 데스페라도
				L1SkillUse su = new L1SkillUse();
				su.handleCommands((L1PcInstance) _target, 230, _target.getId(),
						_target.getX(), _target.getY(), null, 0,
						L1SkillUse.TYPE_GMBUFF);
				su = null;
			} else 
			
				if (i == 6){
				skillUse(15, 87);
				}
			break;
		case 45672: // 리치
			if (i == 0)
				actionGfx(15);
			else if (i == 4)
				Broadcaster.broadcastPacket(_attacker, new S_SkillSound(
						_attacker.getId(), 761), true);
			break;
		case 45618: // 나발
			if (i == 1) {
				// Broadcaster.broadcastPacket(_attacker, new
				// S_SkillSound(_attacker.getId(), 4631), true);
				skillUse(3, 87);
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						_attacker.getSkillEffectTimerSet().setSkillEffect(
								L1SkillId.COUNTER_BARRIER, 6000 * 1000);
						Broadcaster.broadcastPacket(_attacker,
								new S_SkillSound(_attacker.getId(), 5832), true);
					}

				}, 1000);
			}

			break;
		case 45654: // 아이리스
			if (i == 0)
				actionGfx(4);
			else if (i == 2) { // 아이스 미티어 시 광역스턴
				skillUse(3, 87);
			} else if (i == 3) { // 케릭터 팅기기 시전시 분신 소환
				Broadcaster.broadcastPacket(_attacker, new S_DoActionGFX(_attacker.getId(), ActionCodes.ACTION_SkillBuff), true);
				L1Location loc = _attacker.getLocation().randomLocation(8, false);
				int count = 3 + _rnd.nextInt(3);
				for (int d = 0; d < count; d++) {
					L1NpcInstance npc = L1SpawnUtil
							.spawn2(loc.getX(), loc.getY(),
									(short) loc.getMapId(), 100006, 0, 0, 0);
					if (npc != null) {
						GeneralThreadPool.getInstance().execute(
								new illusion(_attacker, npc, System
										.currentTimeMillis() + 30000));
					}
				}
				Broadcaster.broadcastPacket(_attacker, new S_SkillSound(
						_attacker.getId(), 761), true);
			}
			break;
		case 45653: // 머미로드
			if (i == 1)
				SendGfx(5, 7468);
			else if (i == 3)
				Broadcaster.broadcastPacket(_attacker, new S_SkillSound(
						_attacker.getId(), 2252), true);
			break;
		case 45652: // 쿠거
			if (i == 2) { // 포그
				for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
						_attacker, 8)) {
					if (cha != null) {// new L1SkillUse().handleCommands(cha,
										// 66, cha.getId(), cha.getX(),
										// cha.getY(), null,
										// 0,L1SkillUse.TYPE_GMBUFF);
						L1SkillUse su = new L1SkillUse();
						su.handleCommands(null, 66, cha.getId(), cha.getX(),
								cha.getY(), null, 0, L1SkillUse.TYPE_GMBUFF,
								_attacker);
						su = null;
					}
				}
			} else if (i == 3) { // 전체스턴
				skillUse(15, 87);
			}
			break;
		case 45650: // 죽음의 좀비로드
			if (i == 3) {
				SendGfx(10, 4847);
				actionGfx(10);
			}
			break;
		case 45606: // 공포의 뱀파이어
			if (i == 2) {
				for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
						_attacker, 10)) {
					if (cha != null)
						L1PolyMorph.doPoly(cha, 2885, 100,
								L1PolyMorph.MORPH_BY_NPC);
				}
			}
			break;
		case 45547: // 불신의 시어
			if (i == 3)
				skillUse(8, 44);
			break;
		case 45513: // 왜곡의 제니스 퀸
			if (i == 1) {
				if (_target.getResistance().getMr() < 148) {
					L1SkillUse su = new L1SkillUse();
					su.handleCommands(null, 44, _target.getId(), _target.getX(), _target.getY(), null, 0, L1SkillUse.TYPE_GMBUFF, _attacker);
					su = null;
				}
			} else if (i == 2) {
				for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(_attacker, 8)) {
					if (cha != null || cha != _target) {
						// S_SkillSound s = new S_SkillSound(cha.getId(), 4399);
						// cha.sendPackets(s);
						// Broadcaster.broadcastPacket(cha, s);
						L1DamagePoison.doInfection(_attacker, cha, 1000, 20);
					}
				}
			}
			break;
		case 100014: // 린드3차
			if (i == 13)
				Broadcaster.broadcastPacket(_attacker, new S_PacketBox(83, 2),
						true);
			break;
		case 45580: // 흑장로
		case 45394: // 흑장로
			if (i == 2) {
				_target.getSkillEffectTimerSet().setSkillEffect(10518,
						12 * 1000);
			}
			break;
		case 45545: // 흑장로
			if (i == 3) {
				SendGfx(3, 7977);
				Broadcaster.broadcastPacket(_attacker, new S_SkillSound(
						_attacker.getId(), 4849), true);
				Broadcaster.broadcastPacket(_attacker, new S_SkillSound(
						_attacker.getId(), 2552), true);
			} else if (i == 4) {

				// skillUse(6, 10518);
				for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
						_attacker, 6)) {
					if (cha != null) {
						L1PcInstance target = (L1PcInstance) cha;
						target.getSkillEffectTimerSet()
								.removeSkillEffect(10518);
						target.getSkillEffectTimerSet().setSkillEffect(10518,
								12 * 1000);
						Broadcaster.broadcastPacket(_attacker,
								new S_SkillSound(cha.getId(), 7780), true);
					}
				}
			}
			if (12 > _rnd.nextInt(100)) {
				for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
						_attacker, 10)) {
					if (cha != null && cha.getResistance().getMr() < 148) {
						L1SkillUse su = new L1SkillUse();
						su.handleCommands(cha, 44, cha.getId(), cha.getX(),
								cha.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
						su = null;
					}
				}
			}

			break;
		case 100838:
		case 100851:
			if (i == 0) {
				GeneralThreadPool.getInstance().execute(new 안식처(_attacker));
			}
			break;
		case 100839: {
			if (i == 0) {
				GeneralThreadPool.getInstance().execute(new 안식처(_attacker));
			}
		}
			break;
		case 100850: {
			if (i == 0) {
				GeneralThreadPool.getInstance().execute(new 안식처(_attacker));
			}
		}
			break;
		case 100849: {
			int rndd = _rnd.nextInt(100);
			if (12 > rndd) {
				skillUse(12, 87);
			}
			if (i == 0) {
				GeneralThreadPool.getInstance().execute(new 안식처(_attacker));
			}
		}
			break;
		case 45617: // 피닉스
			int rndd = _rnd.nextInt(100);
			if (12 > rndd)
				skillUse(12, 44);
			else if (24 > rndd)
				skillUse(12, 87);
			break;

		case 100030: // 경비대장
			if (_target.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)) {
				return;
			}
			if (_target.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.ABSOLUTE_BARRIER)) { // 아브소르트바리아중은 아니다
				_target.getSkillEffectTimerSet().killSkillEffectTimer(
						L1SkillId.ABSOLUTE_BARRIER);
			}
			if (_target instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) _target;
				pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true),
						true);
			}

			_target.setSleeped(true);
			if (_target.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.FOG_OF_SLEEPING))
				_target.getSkillEffectTimerSet().killSkillEffectTimer(
						L1SkillId.FOG_OF_SLEEPING);
			_target.getSkillEffectTimerSet().setSkillEffect(
					L1SkillId.FOG_OF_SLEEPING, 32 * 1000);
			Broadcaster.broadcastPacket(_attacker,
					new S_SkillSound(_target.getId(), 7467), true);
			break;
		case 100035: // 옛 늑인
			if (i == 0) {
				Broadcaster.broadcastPacket(_attacker,
						new S_SkillSound(_target.getId(), 6319), true);
			}
			break;

		/*
		 * case 45580: if(i == 2){ skillUse(6, 10518); for(L1PcInstance cha :
		 * L1World.getInstance().getVisiblePlayer(_attacker, 6)){ if(cha !=
		 * null){ L1PcInstance target = (L1PcInstance)cha;
		 * target.getSkillEffectTimerSet().removeSkillEffect(10518);
		 * target.getSkillEffectTimerSet().setSkillEffect(10518, 12 * 1000);
		 * Broadcaster.broadcastPacket(_attacker, new S_SkillSound(cha.getId(),
		 * 7780), true); } } }
		 */
		case 100036: // 옛 커츠
			if (i == 2) {
				skillUse(15, 44);
			} else if (i == 3) {
				skillUse(6, 10518);
				for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
						_attacker, 6)) {
					if (cha != null) {
						L1PcInstance target = (L1PcInstance) cha;
						target.getSkillEffectTimerSet()
								.removeSkillEffect(10518);
						target.getSkillEffectTimerSet().setSkillEffect(10518,
								12 * 1000);
						Broadcaster.broadcastPacket(_attacker,
								new S_SkillSound(cha.getId(), 7780), true);
					}
				}
			}
			break;
		case 100718: // 거대드레이크
		case 45529: // 드레이크
			if (i == 1) {
				ArrayList<L1PcInstance> tempList = new ArrayList<L1PcInstance>();
				for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
						_attacker, 10)) {
					if (cha != null) {
						if (_rnd.nextInt(100) >= 50)
							tempList.add(cha);
					}
				}
				L1SkillUse skillUse = new L1SkillUse();
				for (L1PcInstance cha : tempList) {
					boolean canUseSkill = skillUse.checkUseSkill(null, 40034,
							cha.getId(), cha.getX(), cha.getY(), null, 0,
							L1SkillUse.TYPE_NORMAL, _attacker);
					if (canUseSkill == true) {
						if (getMobSkillTemplate().getLeverage(i) > 0) {
							skillUse.setLeverage(getMobSkillTemplate()
									.getLeverage(i));
						}
						skillUse.handleCommands(null, 40034, cha.getId(),
								cha.getX(), cha.getY(), null, 0,
								L1SkillUse.TYPE_LOGIN, _attacker);
						Broadcaster.broadcastPacket(_attacker,
								new S_SkillSound(cha.getId(), 762), true);
					}
				}
				skillUse = null;
				if (tempList.size() > 0)
					tempList.clear();
				tempList = null;
			}
			break;
		case 100719: // 제로스
			if (i == 2) {
				skillUse4(10, 153);
			} else if (i == 0) {
				_attacker.getSkillEffectTimerSet().setSkillEffect(
						L1SkillId.제로스리덕, 60 * 1000);
			}

			/*
			 * if(i == 1 || i == 2){ int skillid = 40061; if(i == 2) skillid =
			 * 40062; ArrayList<L1PcInstance> tempList = new
			 * ArrayList<L1PcInstance>(); for(L1PcInstance cha :
			 * L1World.getInstance().getVisiblePlayer(_target, 5)){ if(cha !=
			 * null){ if(_rnd.nextInt(100) >= 0) tempList.add(cha); } }
			 * L1SkillUse skillUse = new L1SkillUse(); for(L1PcInstance cha :
			 * tempList){ boolean canUseSkill = skillUse.checkUseSkill(null,
			 * skillid, cha.getId(), cha.getX(), cha.getY(), null, 0,
			 * L1SkillUse.TYPE_NORMAL, _attacker); if (canUseSkill == true) { if
			 * (getMobSkillTemplate().getLeverage(i) > 0) {
			 * skillUse.setLeverage(getMobSkillTemplate().getLeverage(i)); }
			 * skillUse.handleCommands(null, skillid, cha.getId(), cha.getX(),
			 * cha.getY(), null, 0, L1SkillUse.TYPE_LOGIN, _attacker);
			 * Broadcaster.broadcastPacket(_attacker, new
			 * S_DoActionGFX(cha.getId(), ActionCodes.ACTION_Damage), true);
			 * if(i == 2) Broadcaster.broadcastPacket(_attacker, new
			 * S_SkillSound(cha.getId(), 12446), true); else
			 * Broadcaster.broadcastPacket(_attacker, new
			 * S_SkillSound(cha.getId(), 12471), true); } } skillUse = null;
			 * if(tempList.size() > 0) tempList.clear(); tempList = null; }
			 */
			break;
		case 100338: // 에르자베
			/*
			 * if(i == 0){ //범위 토네이도 SendGfx(10, 7331); }else if(i == 8){
			 * skillUse(3, 157); }
			 */
			if (i == 3) { // 데미지 회오리
				SendGfx(1, 10082);
			}
			break;
		case 100420: // 샌드웜
			if (i == 1) // 광역 포그
				skillUse3(6, 66);
			else if (i == 2) { // 디케이
				skillUse2(6, 71);
			} else if (i == 3) // 웨폰브레이크
				skillUse2(8, 27);
			else if (i == 4) // 광역 단일 마법
				SendGfx(1, 10147);
			else if (i == 6) // 커스 패럴
				skillUse2(8, 33);// 커스
			break;
		case 100589:
		case 100588: // 기르타스 배리어
			if (i == 9) { // 디케이 -> 독구름
				skillUse(12, 71);
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						if (_target instanceof L1PcInstance)
							new L1SkillUse().handleCommands(
									(L1PcInstance) _target, 40048,
									_target.getId(), _target.getX(),
									_target.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
					}
				}, 1000);
			} else if (i == 13) {
				skillUse(12, 87);
				GeneralThreadPool.getInstance().schedule(new Runnable() {
					@Override
					public void run() {
						if (_target instanceof L1PcInstance)
							new L1SkillUse().handleCommands(
									(L1PcInstance) _target, 40056,
									_target.getId(), _target.getX(),
									_target.getY(), null, 0,
									L1SkillUse.TYPE_GMBUFF);
					}
				}, 1000);
			}
			break;
		case 45601: // 데스나이트
			if (i == 2) {
				skillUse2(11, 87);
			} else if (i == 4) { // 디케이
				skillUse2(6, 71);
			} else if (i == 5) // 웨폰브레이크
				skillUse2(8, 27);
			break;
		default:
			break;
		}
	}

	private void SendGfx(int range, int num) {
		for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
				_attacker, range)) {
			if (cha != null && !cha.isGhost() && !cha.isDead()) {
				Broadcaster.broadcastPacket(_attacker,
						new S_SkillSound(cha.getId(), num));
			}
		}
	}

	private void actionGfx(int range) {
		for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
				_attacker, range)) {
			if (cha != null && !cha.isGhost() && !cha.isDead()) {
				Broadcaster.broadcastPacket(_attacker,
						new S_DoActionGFX(cha.getId(),
								ActionCodes.ACTION_Damage));
			}
		}
	}

	private void skillUse(int range, int id) {
		for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(_attacker, range)) {
			if (cha != null && !cha.isGhost() && !cha.isDead()) {
				if (id == 44 && cha.getResistance().getMr() >= 150) continue;
				L1SkillUse su = new L1SkillUse();
				su.handleCommands(cha, id, cha.getId(), cha.getX(), cha.getY(),null, 0, L1SkillUse.TYPE_GMBUFF);
				su = null;
			}
		}
	}

	private void skillUse2(int range, int id) {
		for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
				_attacker, range)) {
			if (cha != null && cha != _target && !cha.isGhost()
					&& !cha.isDead()) {
				L1SkillUse su = new L1SkillUse();
				su.handleCommands(cha, id, cha.getId(), cha.getX(), cha.getY(),
						null, 0, L1SkillUse.TYPE_GMBUFF);
				su = null;
			}
		}
	}

	private void skillUse3(int range, int id) {
		for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
				_attacker, range)) {
			if (cha != null && cha != _target && !cha.isGhost()
					&& !cha.isDead()) {
				if (_rnd.nextInt(100) < 30)
					continue;
				L1SkillUse su = new L1SkillUse();
				su.handleCommands(cha, id, cha.getId(), cha.getX(), cha.getY(),
						null, 0, L1SkillUse.TYPE_GMBUFF);
				su = null;
			}
		}
	}

	private void skillUse4(int range, int id) {
		for (L1PcInstance cha : L1World.getInstance().getVisiblePlayer(
				_attacker, range)) {
			if (cha != null && !cha.isGhost() && !cha.isDead()) {
				if (_rnd.nextInt(100) < 30)
					continue;
				L1SkillUse su = new L1SkillUse();
				su.handleCommands(cha, id, cha.getId(), cha.getX(), cha.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
				su = null;
			}
		}
	}

	/*
	 * private void skillUse2(int range, int id){ for(L1PcInstance cha :
	 * L1World.getInstance().getVisiblePlayer(_attacker, range)){ if(cha !=
	 * null){ L1SkillUse su = new L1SkillUse(); su.handleCommands(null, id,
	 * cha.getId(), cha.getX(), cha.getY(), null, 0,
	 * L1SkillUse.TYPE_GMBUFF,_attacker); su = null; } } }
	 */
	private boolean isUseCounterMagic(L1Character cha) {
		if (cha.getSkillEffectTimerSet().hasSkillEffect(COUNTER_MAGIC)) {
			cha.getSkillEffectTimerSet().removeSkillEffect(COUNTER_MAGIC);
			Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(),
					10702), true);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), 10702), true);
			}
			return true;
		}
		return false;
	}

	class 안식처 implements Runnable {
		L1NpcInstance _npc = null;

		public 안식처(L1NpcInstance npc) {
			_npc = npc;
		}

		@Override
		public void run() {
			while (true) {
				try {
					if (_npc == null || _npc._destroyed || _npc.isDead()) {
						break;
					}
					Broadcaster.broadcastPacket(_npc, new S_PacketBox(
							S_PacketBox.이미지스폰, _npc.getId(), 12750, true));
					Thread.sleep(5000);
					Broadcaster.broadcastPacket(_npc, new S_PacketBox(
							S_PacketBox.이미지스폰, _npc.getId(), 12750, false));
					for (L1Object obj : L1World.getInstance()
							.getVisibleObjects(_npc, 10)) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance _pc = (L1PcInstance) obj;
							if (isUseCounterMagic(_pc)) {
								Broadcaster.broadcastPacket(_npc,
										new S_UseAttackSkill(_npc, _pc.getId(),
												12751, _pc.getX(), _pc.getY(),
												18, 0), true);
								continue;
							}
							_pc.receiveDamage(_npc, 400, 2);
							Broadcaster.broadcastPacket(_npc,
									new S_UseAttackSkill(_npc, _pc.getId(),
											12751, _pc.getX(), _pc.getY(), 18),
									true);
						}

					}
					break;
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	class illusion implements Runnable {
		L1NpcInstance _master = null;
		L1NpcInstance _npc = null;
		long _time = 0;

		public illusion(L1NpcInstance master, L1NpcInstance npc, long time) {
			_master = master;
			_npc = npc;
			_time = time;
		}

		@Override
		public void run() {
			while (true) {
				try {
					if (_npc == null || _npc._destroyed || _npc.isDead())
						break;
					if (_master == null || _master._destroyed
							|| _master.isDead()) {
						_npc.deleteMe();
						break;
					}
					if (_time < System.currentTimeMillis()) {
						if (_npc != null && !_npc._destroyed && !_npc.isDead()) {
							_npc.deleteMe();
						}
						break;
					}
					Thread.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}

	private ArrayList<L1PcInstance> AreaBoxList(int width, int height) {
		ArrayList<L1PcInstance> targetList = new ArrayList<L1PcInstance>();
		for (L1Object obj : L1World.getInstance()
				.getVisibleBoxObjects(_attacker,
						_attacker.getMoveState().getHeading(), width, height)) {
			if (!(obj instanceof L1PcInstance))
				continue;
			L1PcInstance cha = (L1PcInstance) obj;
			if (cha.isDead() || cha.isGhost())
				continue;
			if (_attacker instanceof L1SummonInstance
					|| _attacker instanceof L1PetInstance) {
				if (cha.getId() == _attacker.getMaster().getId()
						|| CharPosUtil.getZoneType(cha) == 1)
					continue;
			}

			if (!CharPosUtil.isAreaAttack(_attacker, cha.getX(), cha.getY(),
					cha.getMapId())) {
				continue;
			}
			if (!CharPosUtil.isAreaAttack(cha, _attacker.getX(),
					_attacker.getY(), _attacker.getMapId())) {
				continue;
			}

			if (_target instanceof L1PcInstance
					|| _target instanceof L1SummonInstance
					|| _target instanceof L1PetInstance) {
				if (!cha.isGhost() && !cha.isGmInvis())
					targetList.add(cha);
			}
		}
		return targetList;
	}

	/** 스킬 사용시 대사 출력 **/
	private boolean SendMsg(int i) {
		if (_attacker.getNpcId() == 100584 && i == 6) {
			Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
					_attacker, "voice_kirtas_8", 20), true);
		}
		String msg = getMobSkillTemplate().getMsg(i);
		if (msg == null || msg.equals(""))
			return false;
		Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(_attacker,
				msg, 0), true);
		return true;
	}

	/**
	 * 파푸리온 리뉴얼
	 */
	private void runskill(int num) {
		// System.out.println("스킬사용~"+num);
		switch (num) {
		case PAPOO_SKILL: // 리오타! 누스건 카푸
		{ // 안타라스(리뉴얼) - 용언 스킬1
			if (_attacker.skilluse == false) {
				_attacker.skilluse = true;
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
						_attacker, "$8462", 0), true);
				PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
						_attacker.getMapId());
				PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 5, 2000);// 2초
				antaendtime.begin();
			} else {
				return;
			}
		}
			break;
		case PAPOO_SKILL1: // 리오타! 피로이나
		{ // 안타라스(리뉴얼) - 용언 스킬1
			if (_attacker.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.PAPOO_구슬_ディレイ)) {
				return;
			}
			_attacker.getSkillEffectTimerSet().setSkillEffect(
					L1SkillId.PAPOO_구슬_ディレイ, 120 * 1000);
			if (_attacker.skilluse == false) {
				_attacker.skilluse = true;
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
						_attacker, "$8467", 0), true);
				PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
						_attacker.getMapId());
				PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 6, 2000);// 2초
				antaendtime.begin();
			} else {
				return;
			}
		}
			break;

		case PAPOO_SKILL2: { // 리오타! 라나 폰폰 켄슬+오른손 연속 공격
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8454", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 7,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}
			break;
		case PAPOO_SKILL3: { // 리오타! 레포 폰폰 웨폰+왼손 연속 공격
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8455", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 8,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}
			break;
		case PAPOO_SKILL4: { // 리오타! 테나 론디르 꼬리연속+아이스 브레스
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8460", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 9,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}
			break;
		case PAPOO_SKILL5: { // 리오타! 네나 론디르 머리연속+아이스브레스
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8461", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 10,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}
			break;
		case PAPOO_SKILL6: { // 리오타! 라나 오이므 데스포션 + 오른손 + 아이스이럽
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8458", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 15,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}
			break;
		case PAPOO_SKILL7: { // 리오타! 레포 오이므 데스포션+오른손+고함
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8459", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 16,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}
			break;
		case PAPOO_SKILL8: { // 리오타! 테나 웨인라크 데스힐+꼬리+아이스 브레스
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8457", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 13,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}
			break;
		case PAPOO_SKILL9: { // 리오타! 레나 우누스 리듀스힐+머리공격+아이스브레스
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8456", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 14,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}
			break;
		case PAPOO_SKILL10: { // 리오타! 오니즈 웨인라크 범위캔슬+데스힐+아이스미티어+아이스이럽
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					_attacker.skilluse = true;
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8463", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 11,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}
			}
		}

			break;
		case PAPOO_SKILL11: { // 리오타! 오니즈 쿠스온 웨인라크 범위캔슬+데스힐+아이스미티어+발작
			if (_attacker instanceof L1NpcInstance) {
				if (_attacker.skilluse == false) {
					Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
							_attacker, "$8465", 0), true);
					PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
							_attacker.getMapId());
					_attacker.skilluse = true;
					PaPooTimer antaendtime = new PaPooTimer(_attacker, ar, 12,
							2000);// 2초
					antaendtime.begin();
				} else {
					return;
				}

			}
		}
			break;
		}
	}

	private boolean papoo() {
		/*
		 * 1단계 진주 소환 & 물리 공격 단계 2단계 웨인라크, 오이므, 우누스 디버프 단계 3단계 카푸 소환 단계 리오타! 피로이나
		 * 진주 토르나르 소환
		 * 
		 * 리오타! 누스건 카푸 카푸소환
		 * 
		 * 리오타! 라나 오이므 데스포션 + 오른손 + 아이스이럽
		 * 
		 * 리오타! 레포 오이므 데스포션+오른손+고함
		 * 
		 * 리오타! 레나 우누스 리듀스힐+머리공격+아이스브레스
		 * 
		 * 리오타! 테나 웨인라크 데스힐+꼬리+아이스 브레스
		 * 
		 * 리오타! 라나 폰폰 켄슬+오른손 연속 공격
		 * 
		 * 리오타! 레포 폰폰 웨폰+왼손 연속 공격
		 * 
		 * 리오타! 테나 론디르 꼬리연속+아이스 브레스
		 * 
		 * 리오타! 네나 론디르 머리연속+아이스브레스
		 * 
		 * 리오타! 오니즈 웨인라크 범위캔슬+데스힐+아이스미티어+아이스이럽
		 * 
		 * 리오타! 오니즈 쿠스온 웨인라크 범위캔슬+데스힐+아이스미티어+발작
		 */
		int chance = _rnd.nextInt(100) + 1;
		if (_attacker.getCurrentHp() > 80000) {
			if (chance < 60) {
				int val[] = { PAPOO_SKILL1, PAPOO_SKILL2, PAPOO_SKILL1,
						PAPOO_SKILL3, PAPOO_SKILL4, PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 80) {
				int val[] = { PAPOO_SKILL6, PAPOO_SKILL7, PAPOO_SKILL9,
						PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		} else if (_attacker.getCurrentHp() > 40000) {
			if (chance < 70) {
				int val[] = { PAPOO_SKILL6, PAPOO_SKILL7, PAPOO_SKILL9,
						PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 70) {
				int val[] = { PAPOO_SKILL1, PAPOO_SKILL1, PAPOO_SKILL2,
						PAPOO_SKILL1, PAPOO_SKILL3, PAPOO_SKILL4, PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		} else {
			if (chance < 60) {
				int val[] = { PAPOO_SKILL6, PAPOO_SKILL7, PAPOO_SKILL9,
						PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 60) {
				int val[] = { PAPOO_SKILL, PAPOO_SKILL1, PAPOO_SKILL2,
						PAPOO_SKILL3, PAPOO_SKILL4, PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		}
		if (_attacker.marble.size() == 0) {
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
					_attacker.getMapId());
			if (ar.사엘() != null) {
				ar.사엘().deleteMe();
			}
		}
		if (_attacker.marble2.size() == 0) {
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
					_attacker.getMapId());
			if (ar.사엘2() != null) {
				ar.사엘2().deleteMe();
			}
		}
		if (_attacker.marble.size() > 0) {
			SetHill();
		}
		if (_attacker.marble2.size() > 0) {
			SetHaste();
		}
		return true;
	}

	private boolean papoo2() {
		/*
		 * 2단계 HP에 따른 파푸리온 용언 마법 사용 빈도 1단계 진주 소환 & 물리 공격 단계 2단계 웨인라크, 오이므, 우누스
		 * 디버프 & 카푸 소환 단계 3단계 아이스 미티어 단계 (리오타! 오니즈 XXXX ...)
		 * 
		 * 리오타! 누스건 카푸 카푸소환
		 * 
		 * 리오타! 라나 오이므 데스포션 + 오른손 + 아이스이럽
		 * 
		 * 리오타! 레포 오이므 데스포션+오른손+고함
		 * 
		 * 리오타! 레나 우누스 리듀스힐+머리공격+아이스브레스
		 * 
		 * 리오타! 테나 웨인라크 데스힐+꼬리+아이스 브레스
		 * 
		 * 리오타! 라나 폰폰 켄슬+오른손 연속 공격
		 * 
		 * 리오타! 레포 폰폰 웨폰+왼손 연속 공격
		 * 
		 * 리오타! 테나 론디르 꼬리연속+아이스 브레스
		 * 
		 * 리오타! 네나 론디르 머리연속+아이스브레스
		 * 
		 * 리오타! 오니즈 웨인라크 범위캔슬+데스힐+아이스미티어+아이스이럽
		 * 
		 * 리오타! 오니즈 쿠스온 웨인라크 범위캔슬+데스힐+아이스미티어+발작
		 */
		int chance = _rnd.nextInt(100) + 1;
		if (_attacker.getCurrentHp() > 80000) {
			if (chance < 60) {
				int val[] = { PAPOO_SKILL1, PAPOO_SKILL2, PAPOO_SKILL3,
						PAPOO_SKILL1, PAPOO_SKILL4, PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 80) {
				int val[] = { PAPOO_SKILL6, PAPOO_SKILL7, PAPOO_SKILL9,
						PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		} else if (_attacker.getCurrentHp() > 40000) {
			if (chance < 70) {
				int val[] = { PAPOO_SKILL6, PAPOO_SKILL7, PAPOO_SKILL9,
						PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 70) {
				int val[] = { PAPOO_SKILL1, PAPOO_SKILL2, PAPOO_SKILL1,
						PAPOO_SKILL3, PAPOO_SKILL4, PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		} else {
			if (chance < 60) {
				int val[] = { PAPOO_SKILL, PAPOO_SKILL6, PAPOO_SKILL7,
						PAPOO_SKILL9, PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 60) {
				int val[] = { PAPOO_SKILL10, PAPOO_SKILL1, PAPOO_SKILL2,
						PAPOO_SKILL1, PAPOO_SKILL3, PAPOO_SKILL4, PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		}
		if (_attacker.marble.size() == 0) {
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
					_attacker.getMapId());
			if (ar.사엘() != null) {
				ar.사엘().deleteMe();
			}
		}
		if (_attacker.marble2.size() == 0) {
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
					_attacker.getMapId());
			if (ar.사엘2() != null) {
				ar.사엘2().deleteMe();
			}
		}
		if (_attacker.marble.size() > 0) {
			SetHill();
		}
		if (_attacker.marble2.size() > 0) {
			SetHaste();
		}
		return true;
	}

	private boolean papoo3() {
		/*
		 * 3단계 HP에 따른 파푸리온 용언 마법 사용 빈도 1단계 진주 소환 & 물리 공격 단계 2단계 웨인라크, 오이므, 우누스
		 * 디버프 & 카푸 소환 단계 3단계 아이스 미티어 & 발작 단계 (리오타! 오니즈 XXXX...,리오타! 오니즈 쿠스온
		 * XXXX....)
		 * 
		 * 리오타! 누스건 카푸 카푸소환
		 * 
		 * 리오타! 라나 오이므 데스포션 + 오른손 + 아이스이럽
		 * 
		 * 리오타! 레포 오이므 데스포션+오른손+고함
		 * 
		 * 리오타! 레나 우누스 리듀스힐+머리공격+아이스브레스
		 * 
		 * 리오타! 테나 웨인라크 데스힐+꼬리+아이스 브레스
		 * 
		 * 리오타! 라나 폰폰 켄슬+오른손 연속 공격
		 * 
		 * 리오타! 레포 폰폰 웨폰+왼손 연속 공격
		 * 
		 * 리오타! 테나 론디르 꼬리연속+아이스 브레스
		 * 
		 * 리오타! 네나 론디르 머리연속+아이스브레스
		 * 
		 * 리오타! 오니즈 웨인라크 범위캔슬+데스힐+아이스미티어+아이스이럽
		 * 
		 * 리오타! 오니즈 쿠스온 웨인라크 범위캔슬+데스힐+아이스미티어+발작
		 */
		int chance = _rnd.nextInt(100) + 1;
		if (_attacker.getCurrentHp() > 80000) {
			if (chance < 60) {
				int val[] = { PAPOO_SKILL1, PAPOO_SKILL2, PAPOO_SKILL3,
						PAPOO_SKILL1, PAPOO_SKILL4, PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 80) {
				int val[] = { PAPOO_SKILL6, PAPOO_SKILL7, PAPOO_SKILL9,
						PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		} else if (_attacker.getCurrentHp() > 40000) {
			if (chance < 70) {
				int val[] = { PAPOO_SKILL6, PAPOO_SKILL7, PAPOO_SKILL9,
						PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 70) {
				int val[] = { PAPOO_SKILL1, PAPOO_SKILL2, PAPOO_SKILL3,
						PAPOO_SKILL1, PAPOO_SKILL4, PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		} else {
			if (chance < 60) {
				int val[] = { PAPOO_SKILL, PAPOO_SKILL6, PAPOO_SKILL7,
						PAPOO_SKILL9, PAPOO_SKILL8 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			} else if (chance > 60) {
				int val[] = { PAPOO_SKILL11, PAPOO_SKILL10, PAPOO_SKILL1,
						PAPOO_SKILL1, PAPOO_SKILL2, PAPOO_SKILL3, PAPOO_SKILL4,
						PAPOO_SKILL5 };
				int num = _rnd.nextInt(val.length);
				runskill(val[num]);
				SummonTorr();
				val = null;
			}
		}
		if (_attacker.marble.size() == 0) {
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
					_attacker.getMapId());
			if (ar.사엘() != null) {
				ar.사엘().deleteMe();
			}
		}
		if (_attacker.marble2.size() == 0) {
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
					_attacker.getMapId());
			if (ar.사엘2() != null) {
				ar.사엘2().deleteMe();
			}
		}
		if (_attacker.marble.size() > 0) {

			SetHill();
		}
		if (_attacker.marble2.size() > 0) {
			SetHaste();
		}
		return true;
	}

	private void SummonTorr() {
		/*
		 * 토르나르 소환 안하게 if(_target.tro.size()<6){ for(ia=0;ia<randcount;ia++){
		 * _target.tro.add("토르토르"); L1SpawnUtil.spawn3(_attacker, 4039003, 6,
		 * 30*1000, false); Broadcaster.broadcastPacket(_attacker, new
		 * S_SkillSound(_attacker.getId(), 761), true); } }
		 */
	}

	private void SetHill() {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_attacker,
				-1)) {
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				int num = _rnd.nextInt(100);
				if (num < 50) {
					pc.getSkillEffectTimerSet().removeSkillEffect(10500);
					if (!pc.getSkillEffectTimerSet().hasSkillEffect(10501)) {
						pc.getSkillEffectTimerSet()
								.setSkillEffect(10501, 60000);
						L1SkillUse su = new L1SkillUse();
						su.handleCommands(pc, 10501, pc.getId(), pc.getX(),
								pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
						su = null;
					}
					break;
				}
			}
		}
		Broadcaster.broadcastPacket(_attacker,
				new S_SkillSound(_attacker.getId(), 832), true);
		_attacker.setCurrentHp(_attacker.getCurrentHp() + 500);

	}

	private void SetHaste() {
		for (L1Object obj : L1World.getInstance().getVisibleObjects(_attacker,
				-1)) {
			if (obj instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) obj;
				int num = _rnd.nextInt(100);
				if (num < 50) {
					pc.getSkillEffectTimerSet().removeSkillEffect(10501);
					if (!pc.getSkillEffectTimerSet().hasSkillEffect(10500)) {
						pc.getSkillEffectTimerSet()
								.setSkillEffect(10500, 60000);
						L1SkillUse su = new L1SkillUse();
						su.handleCommands(pc, 10500, pc.getId(), pc.getX(),
								pc.getY(), null, 0, L1SkillUse.TYPE_SPELLSC);
						su = null;
					}
					break;
				}
			}
		}
		Broadcaster.broadcastPacket(_attacker,
				new S_SkillHaste(_attacker.getId(), 1, 0), true);
		Broadcaster.broadcastPacket(_attacker,
				new S_SkillSound(_attacker.getId(), 3104), true);
		_attacker.getMoveState().setMoveSpeed(1);
	}

	private boolean summon(int idx) {
		int summonId = getMobSkillTemplate().getSummon(idx);
		int min = getMobSkillTemplate().getSummonMin(idx);
		int max = getMobSkillTemplate().getSummonMax(idx);
		int count = 0;

		if (summonId == 0) {
			return false;
		}

		/** 이프, 피닉, 흑장, 아이리스, 리치 총 5번까지만 소환하도록 **/
		if (_attacker.getNpcTemplate().get_npcId() == 45516
				|| _attacker.getNpcTemplate().get_npcId() == 45617
				|| _attacker.getNpcTemplate().get_npcId() == 45545
				|| _attacker.getNpcTemplate().get_npcId() == 45654
				|| _attacker.getNpcTemplate().get_npcId() == 45672) {
			if (_attacker.particular_summon_count >= 2) {
				return false;
			} else
				_attacker.particular_summon_count++;
		}

		count = _rnd.nextInt(max) + min;
		mobspawn(summonId, count);

		if (_attacker.getNpcTemplate().get_npcId() != 100839) {
			Broadcaster.broadcastPacket(_attacker,
					new S_SkillSound(_attacker.getId(), 761), true);
		}

		S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(),
				ActionCodes.ACTION_SkillBuff);
		Broadcaster.broadcastPacket(_attacker, gfx, true);

		_sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
		return true;
	}

	private boolean poly(int idx) {
		int polyId = getMobSkillTemplate().getPolyId(idx);
		boolean usePoly = false;

		if (polyId == 0) {
			return false;
		}

		for (L1PcInstance pc : L1World.getInstance()
				.getVisiblePlayer(_attacker)) {
			if (pc.isDead()) {
				continue;
			}
			if (pc.isGhost()) {
				continue;
			}
			if (pc.isGmInvis()) {
				continue;
			}
			if (CharPosUtil.isAreaAttack(_attacker, pc.getX(), pc.getY(),
					pc.getMapId()) == false) {
				continue;
			}
			if (CharPosUtil.isAreaAttack(pc, _attacker.getX(),
					_attacker.getY(), _attacker.getMapId()) == false) {
				continue;
			}
			int npcId = _attacker.getNpcTemplate().get_npcId();
			switch (npcId) {
			case 81082:
			case 450001799:
				pc.getInventory().takeoffEquip(945);
				break;
			default:
				break;
			}
			L1PolyMorph.doPoly(pc, polyId, 1800, L1PolyMorph.MORPH_BY_NPC);

			usePoly = true;
		}
		if (usePoly) {
			for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(
					_attacker)) {
				pc.sendPackets(new S_SkillSound(pc.getId(), 230), true);
				Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(),
						230), true);
				break;
			}
			S_DoActionGFX gfx = new S_DoActionGFX(_attacker.getId(),
					ActionCodes.ACTION_SkillBuff);
			Broadcaster.broadcastPacket(_attacker, gfx, true);

			_sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
		}

		return usePoly;
	}

	private synchronized boolean magicAttack(int idx, L1Character _target) {
		L1SkillUse skillUse = new L1SkillUse();
		int skillid = getMobSkillTemplate().getSkillId(idx);
		boolean canUseSkill = false;
		if (_attacker.getSkillEffectTimerSet()
				.hasSkillEffect(L1SkillId.SILENCE)) {
			skillUse = null;
			return false;
		}
		if (skillid == 43) {
			if (_attacker.getSkillEffectTimerSet().hasSkillEffect(
					L1SkillId.HASTE)) {
				skillUse = null;
				return false;
			}
			_target = _attacker;
		}
		/** 그림리퍼, 리치 켄슬 -> 마방 148이상 안들어가게 **/
		else if (skillid == 44 && _attacker instanceof L1MonsterInstance) {
			if (_attacker.getNpcTemplate().get_npcId() == 45673
					|| _attacker.getNpcTemplate().get_npcId() == 45672) {
				if (_target instanceof L1PcInstance
						&& _target.getResistance().getMr() >= 148) {
					skillUse = null;
					return false;
				}
			}
		}
		if (skillid > 0) {
			canUseSkill = skillUse.checkUseSkill(null, skillid,
					_target.getId(), _target.getX(), _target.getY(), null, 0,
					L1SkillUse.TYPE_NORMAL, _attacker);

		}
		if (canUseSkill == true) {
			if (getMobSkillTemplate().getLeverage(idx) > 0) {
				skillUse.setLeverage(getMobSkillTemplate().getLeverage(idx));
			}
			skillUse.handleCommands(null, skillid, _target.getId(),
					_target.getX(), _target.getY(), null, 0,
					L1SkillUse.TYPE_NORMAL, _attacker);
			L1Skills skill = SkillsTable.getInstance().getTemplate(skillid);
			if (skill.getTarget().equals("attack") && skillid != 18) {
				_sleepTime = _attacker.getNpcTemplate().getAtkMagicSpeed();
				if (skill.getActionId() != 0) {
					int time = SprTable.getInstance().getAttackSpeed(_attacker.getNpcTemplate().get_gfxid(),skill.getActionId());
					if (time > 0 && _sleepTime != time) _sleepTime = time;
				}
			} else {
				_sleepTime = _attacker.getNpcTemplate().getSubMagicSpeed();
			}
			skillUse = null;
			return true;
		}
		skillUse = null;
		return false;
	}

	private boolean physicalAttack(int idx) {
		Map<Integer, Integer> targetList = new ConcurrentHashMap<Integer, Integer>();
		int areaWidth = getMobSkillTemplate().getAreaWidth(idx);
		int areaHeight = getMobSkillTemplate().getAreaHeight(idx);
		int range = getMobSkillTemplate().getRange(idx);
		int actId = getMobSkillTemplate().getActid(idx);
		int gfxId = getMobSkillTemplate().getGfxid(idx);

		if (_attacker.getLocation().getTileLineDistance(_target.getLocation()) > range) {
			return false;
		}

		if (!CharPosUtil.isAreaAttack(_attacker, _target.getX(),
				_target.getY(), _target.getMapId())) {
			return false;
		}
		if (!CharPosUtil.isAreaAttack(_target, _attacker.getX(),
				_attacker.getY(), _target.getMapId())) {
			return false;
		}

		_attacker.getMoveState().setHeading(
				CharPosUtil.targetDirection(_attacker, _target.getX(),
						_target.getY()));

		if (areaHeight > 0) {
			L1Character cha = null;
			ArrayList<L1Object> list = null;
			if (areaWidth == areaHeight)
				list = L1World.getInstance().getVisibleObjects(_attacker,
						areaWidth);
			else
				list = L1World.getInstance().getVisibleBoxObjects(_attacker,
						_attacker.getMoveState().getHeading(), areaWidth,
						areaHeight);
			for (L1Object obj : list) {
				if (!(obj instanceof L1Character)) {
					continue;
				}

				cha = (L1Character) obj;
				if (cha.isDead()) {
					continue;
				}

				if (cha instanceof L1PcInstance) {
					if (((L1PcInstance) cha).isGhost()) {
						continue;
					}
				}

				if (cha instanceof L1PcInstance
						&& _attacker instanceof L1SummonInstance
						|| _attacker instanceof L1PetInstance) {
					if (cha.getId() == _attacker.getMaster().getId()) {
						continue;
					}
					if (CharPosUtil.getZoneType(cha) == 1) {
						continue;
					}
				}

				if (!CharPosUtil.isAreaAttack(_attacker, cha.getX(),
						cha.getY(), cha.getMapId())) {
					continue;
				}
				if (!CharPosUtil.isAreaAttack(cha, _attacker.getX(),
						_attacker.getY(), _attacker.getMapId())) {
					continue;
				}

				if (_target instanceof L1PcInstance
						|| _target instanceof L1SummonInstance
						|| _target instanceof L1PetInstance) {
					if (obj instanceof L1PcInstance
							&& !((L1PcInstance) obj).isGhost()
							&& !((L1PcInstance) obj).isGmInvis()
							|| obj instanceof L1SummonInstance
							|| obj instanceof L1PetInstance) {
						targetList.put(obj.getId(), 0);
					}
				} else {
					if (obj instanceof L1MonsterInstance) {
						targetList.put(obj.getId(), 0);
					}
				}
			}
		} else {
			targetList.put(_target.getId(), 0);
		}

		if (targetList.size() == 0) {
			return false;
		}

		Iterator<Integer> ite = targetList.keySet().iterator();
		L1Attack attack = null;
		while (ite.hasNext()) {
			int targetId = ite.next();
			attack = new L1Attack(_attacker, (L1Character) L1World
					.getInstance().findObject(targetId));
			if (attack.calcHit()) {
				if (getMobSkillTemplate().getLeverage(idx) > 0) {
					attack.setLeverage(getMobSkillTemplate().getLeverage(idx));
				}
				attack.calcDamage();
			}
			if (actId > 0) {
				attack.setActId(actId);
			}

			if (targetId == _target.getId()) {
				if (gfxId > 0) {
					Broadcaster.broadcastPacket(_attacker, new S_SkillSound(
							_attacker.getId(), gfxId), true);
				}
				attack.action();
			}
			attack.commit();
		}
		attack = null;
		_sleepTime = _attacker.getAtkspeed();
		if (actId != 0) {
			int time = SprTable.getInstance().getAttackSpeed(
					_attacker.getNpcTemplate().get_gfxid(), actId);
			if (time > 0 && _sleepTime != time)
				_sleepTime = time;
		}
		return true;
	}

	private boolean isSkillUseble(int skillIdx, boolean isTriRnd) {
		boolean useble = false;
		int type = getMobSkillTemplate().getType(skillIdx);

		if (isTriRnd || type == L1MobSkill.TYPE_SUMMON
				|| type == L1MobSkill.TYPE_POLY) {
			if (getMobSkillTemplate().getTriggerRandom(skillIdx) > 0) {
				int chance = _rnd.nextInt(100) + 1;
				if (chance < getMobSkillTemplate().getTriggerRandom(skillIdx)) {
					useble = true;
				} else {
					return false;
				}
			}
		}

		if (getMobSkillTemplate().getTriggerHp(skillIdx) > 0) {
			int hpRatio = (_attacker.getCurrentHp() * 100)
					/ _attacker.getMaxHp();
			if (hpRatio <= getMobSkillTemplate().getTriggerHp(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerCompanionHp(skillIdx) > 0) {
			L1NpcInstance companionNpc = searchMinCompanionHp();
			if (companionNpc == null) {
				return false;
			}

			int hpRatio = (companionNpc.getCurrentHp() * 100)
					/ companionNpc.getMaxHp();
			if (hpRatio <= getMobSkillTemplate()
					.getTriggerCompanionHp(skillIdx)) {
				useble = true;
				_target = companionNpc;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerRange(skillIdx) != 0) {
			int distance = _attacker.getLocation().getTileLineDistance(
					_target.getLocation());

			if (getMobSkillTemplate().isTriggerDistance(skillIdx, distance)) {
				useble = true;
			} else {
				return false;
			}
		}

		if (getMobSkillTemplate().getTriggerCount(skillIdx) > 0) {
			if (getSkillUseCount(skillIdx) < getMobSkillTemplate()
					.getTriggerCount(skillIdx)) {
				useble = true;
			} else {
				return false;
			}
		}
		return useble;
	}

	private L1NpcInstance searchMinCompanionHp() {
		L1NpcInstance npc;
		L1NpcInstance minHpNpc = null;
		int hpRatio = 100;
		int companionHpRatio;
		int family = _attacker.getNpcTemplate().get_family();

		for (L1Object object : L1World.getInstance().getVisibleObjects(
				_attacker)) {
			if (object instanceof L1NpcInstance) {
				npc = (L1NpcInstance) object;
				if (npc.getNpcTemplate().get_family() == family) {
					companionHpRatio = (npc.getCurrentHp() * 100)
							/ npc.getMaxHp();
					if (companionHpRatio < hpRatio) {
						hpRatio = companionHpRatio;
						minHpNpc = npc;
					}
				}
			}
		}
		return minHpNpc;
	}

	private void mobspawn(int summonId, int count) {
		int i;

		for (i = 0; i < count; i++) {
			mobspawn(summonId);
		}
	}

	private static final int[] gf = { 7989, 7992, 7995, 7998 };

	private void mobspawn(int summonId) {
		try {
			L1Npc spawnmonster = NpcTable.getInstance().getTemplate(summonId);
			if (spawnmonster != null) {
				L1NpcInstance mob = null;
				try {
					String implementationName = spawnmonster.getImpl();
					Constructor<?> _constructor = Class
							.forName(
									(new StringBuilder())
											.append("l1j.server.server.model.Instance.")
											.append(implementationName)
											.append("Instance").toString())
							.getConstructors()[0];
					mob = (L1NpcInstance) _constructor
							.newInstance(new Object[] { spawnmonster });
					mob.setId(ObjectIdFactory.getInstance().nextId());
					L1Location loc = _attacker.getLocation().randomLocation(8,
							false);
					int heading = _rnd.nextInt(8);
					mob.setX(loc.getX());
					mob.setY(loc.getY());
					mob.setHomeX(loc.getX());
					mob.setHomeY(loc.getY());
					short mapid = _attacker.getMapId();
					mob.setMap(mapid);
					mob.getMoveState().setHeading(heading);
					if (summonId == 100005) {
						mob.getGfxId().setTempCharGfx(mob.getGfxId().getTempCharGfx()+ _rnd.nextInt(4));
					} else if (summonId == 100015) {
						mob.getGfxId().setTempCharGfx(gf[_rnd.nextInt(4)]);
					}
					L1World.getInstance().storeObject(mob);
					L1World.getInstance().addVisibleObject(mob);
					L1Object object = L1World.getInstance().findObject(mob.getId());
					if (object instanceof L1MonsterInstance) {
						L1MonsterInstance newnpc = (L1MonsterInstance) object;
						newnpc.set_storeDroped((byte) 0);
						if (summonId == 45061 || summonId == 45161 || summonId == 45181 || summonId == 45455) {
							Broadcaster.broadcastPacket(newnpc, new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Hide), true);
							newnpc.setActionStatus(13);
							Broadcaster.broadcastPacket(newnpc, new S_NPCPack( newnpc), true);
							Broadcaster.broadcastPacket(newnpc, new S_DoActionGFX(newnpc.getId(), ActionCodes.ACTION_Appear), true);
							newnpc.setActionStatus(0);
							Broadcaster.broadcastPacket(newnpc, new S_NPCPack(newnpc), true);
						} else if (summonId == 45741 || summonId == 100019) {
							Broadcaster.broadcastPacket(newnpc, new S_NPCPack(newnpc), true);
						}
						newnpc.onNpcAI();
						newnpc.getLight().turnOnOffLight();
						newnpc.startChat(L1NpcInstance.CHAT_TIMING_APPEARANCE);
						if (summonId == 45741)
							Broadcaster.broadcastPacket(newnpc,
									new S_SkillSound(newnpc.getId(), 8527),
									true);
						if (summonId == 100019)
							Broadcaster.broadcastPacket(newnpc,
									new S_SkillSound(newnpc.getId(), 8531),
									true);
						if (summonId == 45741 || summonId == 100018
								|| summonId == 100019) {
							L1NpcDeleteTimer timer = new L1NpcDeleteTimer(
									newnpc, 20 * 60000);
							timer.begin();
						}
					}
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				}
			}
		} catch (Exception e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
	}

	private L1Character changeTarget(int type, int idx) {
		L1Character target;

		switch (type) {
		case L1MobSkill.CHANGE_TARGET_ME:
			target = _attacker;
			break;
		case L1MobSkill.CHANGE_TARGET_RANDOM:
			List<L1Character> targetList = new ArrayList<L1Character>();
			L1Character cha = null;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(
					_attacker)) {
				if (obj instanceof L1PcInstance || obj instanceof L1PetInstance
						|| obj instanceof L1SummonInstance) {
					cha = (L1Character) obj;

					int distance = _attacker.getLocation().getTileLineDistance(
							cha.getLocation());

					if (!getMobSkillTemplate().isTriggerDistance(idx, distance)) {
						continue;
					}

					if (!CharPosUtil.isAreaAttack(_attacker, cha.getX(),
							cha.getY(), cha.getMapId())) {
						continue;
					}
					if (!CharPosUtil.isAreaAttack(cha, _attacker.getX(),
							_attacker.getY(), _attacker.getMapId())) {
						continue;
					}

					if (!_attacker.getHateList().containsKey(cha)) {
						continue;
					}

					if (cha.isDead()) {
						continue;
					}

					if (cha instanceof L1PcInstance) {
						if (((L1PcInstance) cha).isGhost()) {
							continue;
						}
					}
					targetList.add((L1Character) obj);
				}
			}

			if (targetList.size() == 0) {
				target = _target;
			} else {
				int randomSize = targetList.size() * 100;
				int targetIndex = _rnd.nextInt(randomSize) / 100;
				target = targetList.get(targetIndex);
			}
			break;

		default:
			target = _target;
			break;
		}
		return target;
	}

	private boolean LineMagicAttack(int idx) {
		try {
			GeneralThreadPool.getInstance().execute(new LineMagicThread(idx));
			/*
			 * if((_attacker.getNpcId() == 100420 && idx == 7) ||
			 * (_attacker.getNpcId() == 100338 && idx == 17)){ //샌드웜, 에르자베
			 * for(int i=0; i < 2; i++){
			 * GeneralThreadPool.getInstance().execute(new LineMagicThread(idx,
			 * _rnd.nextInt(8))); } }
			 */
			Broadcaster.broadcastPacket(_attacker,
					new S_DoActionGFX(_attacker.getId(), getMobSkillTemplate()
							.getActid(idx)), true);
			_sleepTime = _attacker.getNpcTemplate().getAtkMagicSpeed();
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	class LineMagicThread implements Runnable {

		private int idx = 0;
		private int heading = -1;

		public LineMagicThread(int id) {
			idx = id;
		}

		public LineMagicThread(int id, int head) {
			idx = id;
			heading = head;
		}

		@Override
		public void run() {
			int skillid = getMobSkillTemplate().getSkillId(idx);
			int triggerRange = getMobSkillTemplate().getTriggerRange(idx);
			if (triggerRange < 0)
				triggerRange = Math.abs(triggerRange);
			short gfx = (short) getMobSkillTemplate().getGfxid(idx);
			int leverage = getMobSkillTemplate().getLeverage(idx);
			try {
				L1Character caster = _attacker;
				boolean threeJump = false;
				if (caster instanceof L1MonsterInstance) {
					L1MonsterInstance mon = (L1MonsterInstance) caster;
					if (mon.getNpcId() == 100420 || mon.getNpcId() == 100338) {
						threeJump = true;
					}
				}

				int xx = caster.getX();
				int yy = caster.getY();
				int a = heading == -1 ? CharPosUtil.calcheading(xx, yy,
						_target.getX(), _target.getY()) : heading;
				int subCount = 0;
				int[] xlist = new int[triggerRange];
				int[] ylist = new int[triggerRange];
				ArrayList<L1PcInstance> list = L1World.getInstance()
						.getVisiblePlayer(caster, triggerRange);
				for (int i = 0; i < triggerRange; i++) {
					int x = xx;
					int y = yy;
					x += HEADING_TABLE_X[a];
					y += HEADING_TABLE_Y[a];

					if (threeJump) { // && !(i == 0 || i == 1 || i == 3 || i ==
										// 4 || i == 6 || i == 7 || i == 9 ||
										// i== 10))
						subCount++;
						if (subCount == 3) {
							Broadcaster.broadcastPacket(caster,
									new S_EffectLocation(x, y, gfx), true);
							subCount = 0;
						}
					} else
						Broadcaster.broadcastPacket(caster,
								new S_EffectLocation(x, y, gfx), true);

					/*
					 * for(L1PcInstance pc : list){ if(pc.getX() != xx ||
					 * pc.getY() != yy) continue; if(pc.isGhost() || pc.isDead()
					 * || pc.isGm()) continue; if
					 * (pc.getSkillEffectTimerSet().hasSkillEffect
					 * (ABSOLUTE_BARRIER) ||
					 * pc.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE) ||
					 * pc
					 * .getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH)
					 * || pc.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)
					 * || pc.getSkillEffectTimerSet().hasSkillEffect(MOB_BASILL)
					 * || pc.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA)
					 * || pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.
					 * STATUS_안전모드)) { continue; } L1Magic _magic = new
					 * L1Magic(_attacker, pc); _magic.setLeverage(leverage); int
					 * dmg = _magic.calcMagicDamage(skillid); _magic.commit(dmg,
					 * 0); if(dmg > 0){ pc.sendPackets(new
					 * S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage),
					 * true); Broadcaster.broadcastPacket(pc, new
					 * S_DoActionGFX(pc.getId(), ActionCodes.ACTION_Damage),
					 * true); } }
					 */
					xlist[i] = x;
					ylist[i] = y;

					xx = x;
					yy = y;
					Thread.sleep(50);
				}
				for (L1PcInstance pc : list) {
					if (pc.isGhost() || pc.isDead() || pc.isGm())
						continue;
					for (int i = 0; i < triggerRange; i++) {
						if (pc.getX() != xlist[i] || pc.getY() != ylist[i])
							continue;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(
								ABSOLUTE_BARRIER)
								|| pc.getSkillEffectTimerSet().hasSkillEffect(
										ICE_LANCE)
								|| pc.getSkillEffectTimerSet().hasSkillEffect(
										FREEZING_BREATH)
								|| pc.getSkillEffectTimerSet().hasSkillEffect(
										EARTH_BIND)
								|| pc.getSkillEffectTimerSet().hasSkillEffect(
										MOB_BASILL)
								|| pc.getSkillEffectTimerSet().hasSkillEffect(
										MOB_COCA)
								|| pc.getSkillEffectTimerSet().hasSkillEffect(
										L1SkillId.STATUS_안전모드)) {
							continue;
						}
						L1Magic _magic = new L1Magic(_attacker, pc);
						_magic.setLeverage(leverage);
						int dmg = _magic.calcMagicDamage(skillid);
						_magic.commit(dmg, 0, 0);
						if (dmg > 0) {
							pc.sendPackets(new S_DoActionGFX(pc.getId(),
									ActionCodes.ACTION_Damage), true);
							Broadcaster.broadcastPacket(pc, new S_DoActionGFX(
									pc.getId(), ActionCodes.ACTION_Damage),
									true);
						}
						break;
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	class delayUseSkill implements Runnable {

		private L1Character target;
		private int skillNum = 0;

		public delayUseSkill(int i, L1Character t) {
			skillNum = i;
			target = t;
		}

		@Override
		public void run() {
			if (_attacker == null)
				return;
			synchronized (_attacker.synchObject) {
				if (_attacker.isDead() || _attacker._destroyed
						|| _attacker.isParalyzed())
					return;
				try {
					_attacker.setParalyzed(true);
					int skillid = getMobSkillTemplate().getSkillId(skillNum);
					L1Skills skill = SkillsTable.getInstance().getTemplate(
							skillid);
					if (magicAttack(skillNum, target) == true) {
						skillUseCountUp(skillNum);
						AddSkill(skillNum);
						int time = SprTable.getInstance().getAttackSpeed(
								_attacker.getNpcTemplate().get_gfxid(),
								skill.getActionId());
						if (time == 0)
							time = SprTable.getInstance().getMoveSpeed(
									_attacker.getNpcTemplate().get_gfxid(),
									skill.getActionId());
						if (time == 0) {
							if (skill.getActionId() == 18)
								time = SprTable.getInstance().getDirSpellSpeed(
										_attacker.getNpcTemplate().get_gfxid());
							else if (skill.getActionId() == 19)
								time = SprTable.getInstance()
										.getNodirSpellSpeed(
												_attacker.getNpcTemplate()
														.get_gfxid());
						}
						if (time > 0) {
							Thread.sleep(_attacker.calcSleepTime(
									getSleepTime(), 2));
						}
					}
				} catch (Exception e) {
				}
				_attacker.setParalyzed(false);
			}
		}
	}

	private boolean KirMeteorAttack(int idx) {
		try {
			Broadcaster.broadcastPacket(_attacker,
					new S_EffectLocation(_target.getX(), _target.getY(),
							(short) 11469), true);
			GeneralThreadPool.getInstance().schedule(
					new KirMeteorThread(idx, _target.getX(), _target.getY()),
					5500);
			// Broadcaster.broadcastPacket(_attacker, new
			// S_DoActionGFX(_attacker.getId(),
			// getMobSkillTemplate().getActid(idx)), true);
			Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
					_attacker, "voice_kirtas_12", 20), true);
			_sleepTime = _attacker.getNpcTemplate().getAtkMagicSpeed() + 5500;
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	class KirMeteorThread implements Runnable {

		private int idx = 0;
		private int x = 0;
		private int y = 0;

		public KirMeteorThread(int id, int x, int y) {
			idx = id;
			this.x = x;
			this.y = y;
		}

		@Override
		public void run() {
			if (_attacker == null)
				return;
			synchronized (_attacker.synchObject) {
				if (_attacker.isDead()
						|| _attacker._destroyed
						|| (_attacker.getActionStatus() == 0 && _attacker
								.isParalyzed()))
					return;
				try {
					if (_attacker.getActionStatus() == 0)
						_attacker.setParalyzed(true);
					int skillid = getMobSkillTemplate().getSkillId(idx);
					int leverage = getMobSkillTemplate().getLeverage(idx);

					Broadcaster.broadcastPacket(_attacker, new S_DoActionGFX(
							_attacker.getId(), 18), true);
					Broadcaster.broadcastPacket(_attacker,
							new S_EffectLocation(x, y, (short) 11473), true);
					ArrayList<L1Object> list = L1World
							.getInstance()
							.getVisiblePoint(
									(new L1Location(x, y, _attacker.getMapId())),
									3);
					for (L1Object obj : list) {
						if (obj instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) obj;
							if (pc.isGhost() || pc.isDead() || pc.isGm())
								continue;
							if (pc.getSkillEffectTimerSet().hasSkillEffect(
									ABSOLUTE_BARRIER)
									|| pc.getSkillEffectTimerSet()
											.hasSkillEffect(ICE_LANCE)
									|| pc.getSkillEffectTimerSet()
											.hasSkillEffect(FREEZING_BREATH)
									|| pc.getSkillEffectTimerSet()
											.hasSkillEffect(EARTH_BIND)
									|| pc.getSkillEffectTimerSet()
											.hasSkillEffect(MOB_BASILL)
									|| pc.getSkillEffectTimerSet()
											.hasSkillEffect(MOB_COCA)
									|| pc.getSkillEffectTimerSet()
											.hasSkillEffect(
													L1SkillId.STATUS_안전모드)) {
								continue;
							}
							L1Magic _magic = new L1Magic(_attacker, pc);
							_magic.setLeverage(leverage);
							int dmg = _magic.calcMagicDamage(skillid);
							_magic.commit(dmg, 0, 0);
							if (dmg > 0 && !pc.isDead()) {
								pc.sendPackets(new S_DoActionGFX(pc.getId(),
										ActionCodes.ACTION_Damage), true);
								Broadcaster.broadcastPacket(pc,
										new S_DoActionGFX(pc.getId(),
												ActionCodes.ACTION_Damage),
										true);
							}
							_magic = null;
						}
					}

					int time = SprTable.getInstance().getDirSpellSpeed(
							_attacker.getNpcTemplate().get_gfxid());
					if (time > 0) {
						Thread.sleep(_attacker.calcSleepTime(time - 50, 2));
					}
				} catch (Exception e) {
				}
				if (_attacker.getActionStatus() == 0)
					_attacker.setParalyzed(false);
			}
		}
	}

	private boolean KirBarrier(int idx) {
		try {
			_attacker.setParalyzed(true);
			int actid = getMobSkillTemplate().getActid(idx);
			Broadcaster.broadcastPacket(_attacker,
					new S_DoActionGFX(_attacker.getId(), actid), true);
			_sleepTime = SprTable.getInstance().getMoveSpeed(
					_attacker.getNpcTemplate().get_gfxid(), actid);
			GeneralThreadPool.getInstance().schedule(new KirBarrierThread(idx),
					_sleepTime);
			GeneralThreadPool.getInstance().schedule(new kir_fire(), 3000);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	class KirBarrierThread implements Runnable {

		private int idx = 0;
		private byte step = 0;
		private byte count = 15;

		public KirBarrierThread(int id) {
			idx = id;
		}

		@Override
		public void run() {
			try {
				// 그외 종료 가능성
				if (_attacker == null || _attacker.isDead()
						|| _attacker._destroyed)
					return;
				if (count-- <= 0) {
					// voice_kirtas_2 나약한 인간따위가 감히 신에게 도전하는가 3 으억! 날이겼다고 생각하는가 나
					// 기르타스
					// 4 이야앗! 5 쿠어엇 6키야하하 7 쿠어아 8영혼만 남겨두고 모두 소멸하리라 9 귀찮은놈들 사라져라
					// 10 모두 녹아버려라 11 타올라라 12죽는거다

					// Broadcaster.broadcastPacket(_attacker, new
					// S_NpcChatPacket(_attacker, "voice_kirtas_1", 20), true);
					Broadcaster.broadcastPacket(
							_attacker,
							new S_DoActionGFX(_attacker.getId(), _attacker
									.getActionStatus() + 1), true);
					_attacker.setActionStatus(0);
					Broadcaster.broadcastPacket(_attacker,
							new S_CharVisualUpdate(_attacker), true);
					_attacker.setParalyzed(false);
					((L1MonsterInstance) _attacker).kir_absolute = false;
					((L1MonsterInstance) _attacker).kir_counter_barrier = false;
					((L1MonsterInstance) _attacker).kir_counter_magic = false;
					((L1MonsterInstance) _attacker).kir_poison_barrier = false;
					// 풀기 액션 슬립 액션 스테이터스
					return;
				}
				if (step == 0) {
					if (idx == 4) { // 녹색
						_attacker.setActionStatus(27 - 3);
						((L1MonsterInstance) _attacker).kir_poison_barrier = true;
					} else if (idx == 5) { // 회색
						_attacker.setActionStatus(7 - 3);
						((L1MonsterInstance) _attacker).kir_absolute = true;
					} else if (idx == 6) { // 빨간
						_attacker.setActionStatus(23 - 3);
						((L1MonsterInstance) _attacker).kir_counter_barrier = true;
					} else if (idx == 7) { // 황색
						_attacker.setActionStatus(43 - 3);
						((L1MonsterInstance) _attacker).kir_counter_magic = true;
					}
					Broadcaster.broadcastPacket(_attacker,
							new S_CharVisualUpdate(_attacker), true);
					Broadcaster.broadcastPacket(
							_attacker,
							new S_DoActionGFX(_attacker.getId(), _attacker
									.getActionStatus() + 3), true);
					step++;
				} else {
					if (_rnd.nextInt(100) <= 10) {
						boolean success = false;
						int actionid = 0;
						if (idx == 4) { // 녹색
							int rndid = _rnd.nextInt(2) + 9;
							if (useSkill(rndid)) {
								actionid = getMobSkillTemplate()
										.getActid(rndid);
								success = true;
								if (rndid == 10) {
									Broadcaster.broadcastPacket(_attacker,
											new S_NpcChatPacket(_attacker,
													"voice_kirtas_10", 20),
											true);
								}
							}
						} else if (idx == 5) { // 회색
							int rndid = _rnd.nextInt(3);
							if (useSkill(rndid)) {
								new S_Sound(rndid);
								actionid = getMobSkillTemplate()
										.getActid(rndid);
								success = true;
							}
						} else if (idx == 6) { // 빨간
							if (useSkill(13)) {
								actionid = getMobSkillTemplate().getActid(13);
								success = true;
								Broadcaster.broadcastPacket(_attacker,
										new S_NpcChatPacket(_attacker,
												"voice_kirtas_8", 20), true);
							}
						} else if (idx == 7) { // 황색
							if (_rnd.nextInt(2) == 0) {
								if (useSkill(11)) {
									actionid = getMobSkillTemplate().getActid(
											11);
									success = true;
									Broadcaster
											.broadcastPacket(_attacker,
													new S_NpcChatPacket(
															_attacker,
															"voice_kirtas_9",
															20), true);
								}
							} else {
								if (useSkill(14)) {
									actionid = getMobSkillTemplate().getActid(
											14);
									success = true;
								}
							}
						}
						if (success) {
							int time = SprTable.getInstance().getAttackSpeed(
									_attacker.getNpcTemplate().get_gfxid(),
									actionid);
							if (time == 0)
								time = SprTable.getInstance().getMoveSpeed(
										_attacker.getNpcTemplate().get_gfxid(),
										actionid);
							if (time == 0) {
								if (actionid == 18)
									time = SprTable.getInstance()
											.getDirSpellSpeed(
													_attacker.getNpcTemplate()
															.get_gfxid());
								else if (actionid == 19)
									time = SprTable.getInstance()
											.getNodirSpellSpeed(
													_attacker.getNpcTemplate()
															.get_gfxid());
							}
							GeneralThreadPool.getInstance()
									.schedule(this, time);
							return;
						}
					}
				}
				GeneralThreadPool.getInstance().schedule(this, 1000);
			} catch (Exception e) {
				e.printStackTrace();
				_attacker.setParalyzed(false);
			}
		}
	}

	class kir_fire implements Runnable {

		@Override
		public void run() {
			try {

				if (_attacker == null || _attacker._destroyed
						|| _attacker.isDead()
						|| _attacker.getActionStatus() == 0)
					return;
				Broadcaster.broadcastPacket(_attacker, new S_NpcChatPacket(
						_attacker, "voice_kirtas_11", 20), true);
				for (int i = 0; i < 2; i++) {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							L1SpawnUtil.spawn2(32854 - 4 + _rnd.nextInt(9),
									32862 - 4 + _rnd.nextInt(9),
									(short) _attacker.getMapId(), 100586, 2,
									_rnd.nextInt(2000) + 13000, 0);
						}
					}, _rnd.nextInt(2000));
				}
				for (int i = 0; i < 4; i++) {
					GeneralThreadPool.getInstance().schedule(new Runnable() {
						@Override
						public void run() {
							L1SpawnUtil.spawn2(32854 - 4 + _rnd.nextInt(9),
									32862 - 4 + _rnd.nextInt(9),
									(short) _attacker.getMapId(), 100587, 2,
									_rnd.nextInt(2000) + 13000, 0);
						}
					}, _rnd.nextInt(2000));
				}
				GeneralThreadPool.getInstance().schedule(this, 18000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
}
