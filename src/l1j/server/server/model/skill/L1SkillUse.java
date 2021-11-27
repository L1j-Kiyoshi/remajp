package l1j.server.server.model.skill;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.GameSystem.Antaras.AntarasRaid;
import l1j.server.GameSystem.Antaras.AntarasRaidSystem;
import l1j.server.GameSystem.Antaras.AntarasRaidTimer;
import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.MJInstanceSystem.MJInstanceEnums.InstStatus;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PolyTable;
import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.datatables.SprTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1Cooking;
import l1j.server.server.model.L1CurseParalysis;
import l1j.server.server.model.L1EffectSpawn;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Magic;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1War;
import l1j.server.server.model.L1World;
import l1j.server.server.model.L1데몰리션;
import l1j.server.server.model.L1토마호크;
import l1j.server.server.model.WeaponSkill;
import l1j.server.server.model.Instance.L1ArrowInstance;
import l1j.server.server.model.Instance.L1AuctionBoardInstance;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1CrownInstance;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1DwarfInstance;
import l1j.server.server.model.Instance.L1EffectInstance;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1FurnitureInstance;
import l1j.server.server.model.Instance.L1GuardInstance;
import l1j.server.server.model.Instance.L1HousekeeperInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1LittleBugInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1NpcShopInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PeopleInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1ScarecrowInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.Instance.L1TeleporterInstance;
import l1j.server.server.model.Instance.L1TowerInstance;
import l1j.server.server.model.gametime.GameTimeClock;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.poison.L1DamagePoison;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_ACTION_UI2;
import l1j.server.server.serverpackets.S_AttackCritical;
import l1j.server.server.serverpackets.S_ChangeHeading;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_CharVisualUpdate;
import l1j.server.server.serverpackets.S_ChatPacket;
import l1j.server.server.serverpackets.S_CloseList;
import l1j.server.server.serverpackets.S_CreateItem;
import l1j.server.server.serverpackets.S_CurseBlind;
import l1j.server.server.serverpackets.S_Dexup;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_DoActionShop;
import l1j.server.server.serverpackets.S_EffectLocation;
import l1j.server.server.serverpackets.S_HPUpdate;
import l1j.server.server.serverpackets.S_InventoryIcon;
import l1j.server.server.serverpackets.S_Invis;
import l1j.server.server.serverpackets.S_MPUpdate;
import l1j.server.server.serverpackets.S_MaanIcons;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_NewSkillIcons;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_OtherCharPacks;
import l1j.server.server.serverpackets.S_OwnCharAttrDef;
import l1j.server.server.serverpackets.S_OwnCharStatus;
import l1j.server.server.serverpackets.S_OwnCharStatus2;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_Paralysis;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_PolyHtml;
import l1j.server.server.serverpackets.S_RangeSkill;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_SMPacketBox;
import l1j.server.server.serverpackets.S_SPMR;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SkillIconAura;
import l1j.server.server.serverpackets.S_SkillIconBlessOfEva;
import l1j.server.server.serverpackets.S_SkillIconGFX;
import l1j.server.server.serverpackets.S_SkillIconShield;
import l1j.server.server.serverpackets.S_SkillIconWindShackle;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_Sound;
import l1j.server.server.serverpackets.S_Strup;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.serverpackets.S_TrueTargetNew;
import l1j.server.server.serverpackets.S_UseAttackSkill;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.CalcExp;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.L1SpawnUtil;

public class L1SkillUse {
	public static final int TYPE_NORMAL = 0;
	public static final int TYPE_LOGIN = 1;
	public static final int TYPE_SPELLSC = 2;
	public static final int TYPE_NPCBUFF = 3;
	public static final int TYPE_GMBUFF = 4;

	private L1Skills _skill;
	private int _skillId;
	private int _getBuffDuration;
	private int _shockStunDuration;
	private int _getBuffIconDuration;
	private int _targetID;
	private int _mpConsume = 0;
	private int _hpConsume = 0;
	private int _targetX = 0;
	private int _targetY = 0;
	private String _message = null;
	private int _skillTime = 0;
	private int _type = 0;
	private boolean _isPK = false;
	private int _bookmarkMapId = 0;
	private int _bookmarkX = 0;
	private int _bookmarkY = 0;
	private int _itemobjid = 0;
	private boolean _checkedUseSkill = false;
	private int _leverage = 10;
	private boolean _isFreeze = false;
	private boolean _isCounterMagic = true;

	private L1Character _user = null;
	private L1Character _target = null;

	private L1PcInstance _player = null;
	private L1NpcInstance _npc = null;
	private L1NpcInstance _targetNpc = null;

	private int _calcType;
	private static final int PC_PC = 1;
	private static final int PC_NPC = 2;
	private static final int NPC_PC = 3;
	private static final int NPC_NPC = 4;
	private Random random = new Random(System.nanoTime());
	private ArrayList<TargetStatus> _targetList;
	private boolean _isGlanceCheckFail = false;
	private boolean USE_SPELLSC = false;

	private static Logger _log = Logger.getLogger(L1SkillUse.class.getName());

	public static Map<Integer, L1Object> _truetarget_list = new HashMap<Integer, L1Object>();

	private static final int[] CAST_WITH_INVIS = { 1, 2, 3, 5, 8, 9, 12, 13, 14, 19, 21, 26, 31, 32, 35, 37, 42, 43, 44, 48, 49, 52, 54, 55, 57, 60, 61, 63, 67,
			68, 69, 72, 73, 75, 78, 79, REDUCTION_ARMOR, 마제스티, BOUNCE_ATTACK, SOLID_CARRIAGE, COUNTER_BARRIER, 97, 98, 99, 100, 101, 102, 104, 105, 106, 107,
			109, 110, 111, 113, 114, 115, 116, 117, 118, 129, 130, 131, 133, 134, 137, 138, 146, 147, 148, 149, 150, 151, 155, 156, 158, 159, 163, 164, 165,
			166, 168, 169, 170, 171, 181, SOUL_OF_FLAME, ADDITIONAL_FIRE, IllUSION_OGRE, PATIENCE, IllUSION_DIAMONDGOLEM, IllUSION_LICH, IllUSION_AVATAR, 인페르노,
			HALPHAS };

	// 카운터 매직으로 방어할수 없는 마법
	private static final int[] EXCEPT_COUNTER_MAGIC = { 1, 2, 3, 5, 8, 9, 12, 13, 14, 19, 21, 26, 31, 32, 35, 37, 42, 43, 44, 48, 49, 52, 54, 55, 57, 60, 61,
			63, 67, 68, 69, 72, 73, 75, 78, 79, FORCE_STUN, ETERNITY, AVENGER, DEMOLITION, SHOCK_STUN, 엠파이어, 파워그립, 데스페라도, PANTERA, 쉐도우스탭, PHANTOM, BLADE,
			REDUCTION_ARMOR, BOUNCE_ATTACK, SOLID_CARRIAGE, 마제스티, COUNTER_BARRIER, HALPHAS, 인페르노, 97, 98, 99, 100, 101, 102, 104, 105, 106, 107, 109, 110, 111,
			ARMOR_BREAK, 113, 114, 115, 116, 117, 118, 129, 130, 131, 132, 134, 137, 138, 146, 147, 148, 149, 150, 151, 155, 156, 158, 159, 161, 163, 164, 165,
			166, 168, 169, 170, 171, 181, SOUL_OF_FLAME, ADDITIONAL_FIRE, FOU_SLAYER, SCALES_EARTH_DRAGON, SCALES_FIRE_DRAGON, SCALES_WATER_DRAGON,
			MIRROR_IMAGE, IllUSION_OGRE, PATIENCE, IllUSION_DIAMONDGOLEM, IllUSION_LICH, IllUSION_AVATAR, INSIGHT, 10026, 10027, 10028, 10029, 30060, 30000,
			30078, 30079, 30011, 30081, 30082, 30083, 30080, 30084, 30010, 9278, 9279, 30002, 30086, 100091, 100092 };

	// 파티 적용 스킬 확인
	private boolean _checkedPartyApplySkill = false;

	// 길드 적용 스킬 확인
	private boolean _checkedClanApplySkill = false;

	public L1SkillUse() {
	}

	private static class TargetStatus {
		private L1Character _target = null;
		private boolean _isCalc = true;

		public TargetStatus(L1Character _cha) {
			_target = _cha;
		}

		public L1Character getTarget() {
			return _target;
		}

		public TargetStatus(L1Character _cha, boolean _flg) {
			_isCalc = _flg;
		}

		public boolean isCalc() {
			return _isCalc;
		}
	}

	public void setLeverage(int i) {
		_leverage = i;
	}

	public int getLeverage() {
		return _leverage;
	}

	private boolean isCheckedUseSkill() {
		return _checkedUseSkill;
	}

	private void setCheckedUseSkill(boolean flg) {
		_checkedUseSkill = flg;
	}

	private boolean isCheckedPartyApplySkill() {
		return _checkedPartyApplySkill;
	}

	private void setCheckedPartyApplySkill(boolean flg) {
		_checkedPartyApplySkill = flg;
	}

	private boolean isCheckedClanApplySkill() {
		return _checkedClanApplySkill;
	}

	private void setCheckedClanApplySkill(boolean flg) {
		_checkedClanApplySkill = flg;
	}

	public boolean checkUseSkill(L1PcInstance player, int skillid, int target_id, int x, int y, String message, int time, int type, L1Character attacker) {
		// 존재버그 관련 추가
		if (player instanceof L1PcInstance) {
			L1PcInstance jonje = L1World.getInstance().getPlayer(player.getName());
			if (jonje == null && player.getAccessLevel() != Config.GMCODE) {
				player.sendPackets(new S_SystemMessage("존재버그 강제종료! 재접속하세요"), true);
				player.sendPackets(new S_Disconnect(), true);
				return false;
			}
		}

		if (skillid == L1SkillId.FIRE_BLESS || skillid == L1SkillId.샌드스톰) {// 댄싱블레이즈 검착용 체
			if (player.getWeapon() == null) {
				player.sendPackets(new S_SystemMessage("마법 사용: 실패(성공하지 못함), 검 무기 착용 필요"));
				return false;
			} else {
				L1ItemInstance weapon = player.getWeapon();
				if (weapon.getItem().getType1() == 24) {
					player.sendPackets(new S_SystemMessage("마법 사용: 실패(성공하지 못함), 검 무기 착용 필요"));
					return false;
				}
			}
		}

		if (skillid == L1SkillId.COUNTER_BARRIER) {// 댄싱블레이즈 검착용 체
			if (player.getWeapon() == null) {
				player.sendPackets(new S_SystemMessage("기술 사용: 실패(성공하지 못함), 양손검 무기 착용 필요"));
				return false;
			} else {
				L1ItemInstance weapon = player.getWeapon();
				if (weapon.getItem().getType1() != 50) {
					player.sendPackets(new S_SystemMessage("기술 사용: 실패(성공하지 못함), 양손검 무기 착용 필요"));
					return false;
				}
			}
		}

		if (skillid == L1SkillId.HALPHAS) {// 할파스 체인소드 착용
			if (player.getWeapon() == null) {
				player.sendPackets(new S_SystemMessage("기술 사용: 실패(성공하지 못함), 체인소드 무기 착용 필요"));
				return false;
			} else {
				L1ItemInstance weapon = player.getWeapon();
				if (weapon.getItem().getType1() != 24) {
					player.sendPackets(new S_SystemMessage("기술 사용: 실패(성공하지 못함), 체인소드 무기 착용 필요"));
					return false;
				}
			}
		}

		if (skillid == L1SkillId.인페르노) {// 인페르노 검착용 체
			if (player.getWeapon() == null) {
				player.sendPackets(new S_SystemMessage("마법 사용: 실패(성공하지 못함), 한손장검 무기 착용 필요 단검 사용 불가"));
				return false;
			} else {
				L1ItemInstance weapon = player.getWeapon();
				if (weapon.getItem().getType1() != 4) {
					player.sendPackets(new S_SystemMessage("마법 사용: 실패(성공하지 못함), 한손장검 무기 착용 필요 단검 사용 불가"));
					return false;
				}
			}
		}

		if (skillid == L1SkillId.블로우어택) {// 댄싱블레이즈 검착용 체
			if (player.getWeapon() == null) {
				player.sendPackets(new S_SystemMessage("기술 사용: 실패(성공하지 못함), 방패 착용 필요"));
				return false;
//			} else {
//				L1ItemInstance weapon = player.getWeapon();
//				if (weapon.getItem().getType1() != 4 && weapon.getItem().getType1() != 46) {
//					player.sendPackets(new S_SystemMessage("기술 사용: 실패(성공하지 못함), 방패  착용 필요"));
//					return false;
//				}
			}
		}

		setCheckedUseSkill(true);
		_targetList = new ArrayList<TargetStatus>();

		_skill = SkillsTable.getInstance().getTemplate(skillid);
		if (_skill == null) {
			return false;
		}

		_skillId = skillid;
		_targetX = x;
		_targetY = y;
		_message = message;
		_skillTime = time;
		_type = type;
		boolean checkedResult = true;

		if (attacker == null) {
			// pc
			_player = player;
			_user = _player;
		} else {
			// npc
			_npc = (L1NpcInstance) attacker;
			_user = _npc;
		}

		if (_skill.getTarget().equals("none") && skillid != SUMMON_MONSTER) {
			_targetID = _user.getId();
			if ((skillid == TELEPORT || skillid == MASS_TELEPORT) && _targetX != 0 && _targetY != 0) {

			} else {
				_targetX = _user.getX();
				_targetY = _user.getY();
			}
		} else {
			_targetID = target_id;
		}

		if (type == TYPE_NORMAL) {
			checkedResult = isNormalSkillUsable();
		} else if (type == TYPE_SPELLSC) {
			checkedResult = isSpellScrollUsable();
		} else if (type == TYPE_NPCBUFF) {
			checkedResult = true;
		}

		if (!checkedResult) {
			return false;
		}

		if (_skillId == SUMMON_MONSTER) {
			_target = _user;
			_calcType = PC_PC;
			makeTargetList();
			return true;
		}

		if (_skillId == FIRE_WALL || _skillId == LIFE_STREAM) {
			return true;
		}

		L1Object l1object = L1World.getInstance().findObject(_targetID);

		if (l1object instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) l1object;
			if (pc.isTeleport() || pc.텔대기()) {
				return false;
			}
		}

		if (l1object == null && _skillId == TRUE_TARGET) {
			return true;
		}

		if (l1object instanceof L1LittleBugInstance) {
			return false;
		}

		if (l1object instanceof L1ItemInstance) {
			_log.fine("skill target item name: " + ((L1ItemInstance) l1object).getViewName());
			return false;
		}

		if (_user instanceof L1PcInstance) {
			if (l1object instanceof L1PcInstance) {
				_calcType = PC_PC;
			} else {
				_calcType = PC_NPC;
				_targetNpc = (L1NpcInstance) l1object;
			}
		} else if (_user instanceof L1NpcInstance) {
			if (l1object instanceof L1PcInstance) {
				_calcType = NPC_PC;
			} else if (_skill.getTarget().equals("none")) {
				_calcType = NPC_PC;
			} else {
				_calcType = NPC_NPC;
				_targetNpc = (L1NpcInstance) l1object;
			}
		}

		if (_skillId == TELEPORT || _skillId == MASS_TELEPORT || _skillId == TRUE_TARGET) {
			_bookmarkMapId = target_id;
			_bookmarkX = x;
			_bookmarkY = y;
		}

		if (_skillId == BRING_STONE) {
			_itemobjid = target_id;
		}

		_target = (L1Character) l1object;

		if (!(_target instanceof L1MonsterInstance) && _skill.getTarget().equals("attack") && _user.getId() != target_id) {
			_isPK = true;
		}

		if (!(l1object instanceof L1Character)) {
			checkedResult = false;
		}

		makeTargetList();

		if (_targetList.size() == 0 && (_user instanceof L1NpcInstance)) {
			checkedResult = false;
		}

		return checkedResult;
	}

	/**
	 * 통상의 스킬 사용시에 사용자 상태로부터 스킬이 사용 가능한가 판단한다
	 * 
	 * @return false 스킬이 사용 불가능한 상태인 경우
	 */

	private boolean isNormalSkillUsable() {
		if (_user instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) _user;
			if (pc.isParalyzed()) {
				return false;
			}

			if ((pc.isInvisble() || pc.isInvisDelay()) && !isInvisUsableSkill(pc)) {
				if (_skillId != L1SkillId.AREA_OF_SILENCE && _skillId != L1SkillId.AQUA_PROTECTER) {
					return false;
				} else if (_skillId == L1SkillId.AREA_OF_SILENCE) {
					if (pc.isInvisble()) {
						pc.delInvis();
					}
				}
			}

			// 중량 오버이면 스킬을 사용할 수 없다
			if (pc.getInventory().calcWeightpercent() >= 83 && !pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.RANKING_BUFF_1)) {
				pc.sendPackets(new S_ServerMessage(316), true);
				return false;
			}

			int polyId = pc.getGfxId().getTempCharGfx();
			L1PolyMorph poly = PolyTable.getInstance().getTemplate(polyId);
			if (poly != null && !poly.canUseSkill()) {
				pc.sendPackets(new S_ServerMessage(285), true);
				return false;
			}

			/** 2011.04.07 고정수 배틀존 */

			// /// 공성존에서 특정 마법 사용 못하게 -----------------------
			int castle_id = L1CastleLocation.getCastleIdByArea(pc);
			if (castle_id != 0) {
				if (_skillId == 69 || _skillId == 157) {
					pc.sendPackets(new S_SystemMessage("\\fU공성존에서는 해당마법을 사용 할 수 없습니다."), true);
					return false;
				}
			}

			if (_skillId == 69 || _skillId == 157) {
				L1Object l1object = L1World.getInstance().findObject(_targetID);
				if (l1object != null && l1object instanceof L1PcInstance) {
					int castle_id2 = L1CastleLocation.getCastleIdByArea((L1PcInstance) l1object);
					if (castle_id2 != 0) {
						pc.sendPackets(new S_SystemMessage("\\fU공성존에 있는 대상에게 해당마법을 사용 할 수 없습니다."), true);
						return false;
					}
				}
			}
			// /// 공성존에서 특정 마법 사용 못하게 -----------------------

			if (pc.getMapId() == 5302 || pc.getMapId() == 5153 || pc.getMapId() == 5490) {// 배틀존
				if (_skillId == 69 || _skillId == 78 || _skillId == 71 || _skillId == 39 || _skillId == 20 || _skillId == 33 || _skillId == 67 || _skillId == 61
						|| _skillId == 75 || _skillId == 157 || _skillId == 44 || _skillId == 60) {
					// pc.sendPackets(new S_SystemMessage("배틀존에서는 시전이 불가능합니다."));
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc, "배틀존에서는 시전이 불가능합니다.", Opcodes.S_MESSAGE, 20);
					pc.sendPackets(s_chatpacket);
					return false;
				}
			}

			if (pc.getMapId() == 5001) {// 배틀존 대기실
				if (_skillId == 60 || _skillId == 67) {
					S_ChatPacket s_chatpacket = new S_ChatPacket(pc, "대기실에서는 시전이 불가능합니다.", Opcodes.S_MESSAGE, 20);
					pc.sendPackets(s_chatpacket);
					return false;
				}
			}

			if (pc.getMap().isSafetyZone(pc.getLocation())) {
				if (_skillId == 220 || _skillId == 215 || _skillId == 205 || _skillId == 11 || _skillId == 69 || _skillId == 67) {
					pc.sendPackets(new S_SystemMessage("\\fU마을안에서는 사용할 수 없는 마법입니다."), true);
					return false;
				}
			}

			if (pc.getMapId() == 622) {// 깃털마을
				if (_skillId == 69 || _skillId == 67) {
					pc.sendPackets(new S_SystemMessage("\\fU깃털 마을안에서는 사용할 수 없는 마법입니다."), true);
					return false;
				}
			}

			if (!isAttrAgrees()) {
				return false;
			}

			if (_skillId == SUMMON_MONSTER) {
				Object[] petlist = pc.getPetList().toArray();
				for (Object pet : petlist) {
					if (pet instanceof L1SummonInstance) {
						S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), 19);
						pc.sendPackets(gfx);
						pc.sendPackets(new S_ServerMessage(319));
						return false;
					}
				}
			}

			if (_skillId == ELEMENTAL_PROTECTION && pc.getElfAttr() == 0) {
				pc.sendPackets(new S_ServerMessage(280), true);
				return false;
			}

			// 스킬 ディレイ
			if (pc.isSkillDelay()) {
				return false;
			}

			if (pc.getSkillEffectTimerSet().hasSkillEffect(SILENCE) || pc.getSkillEffectTimerSet().hasSkillEffect(AREA_OF_SILENCE)
					|| pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_POISON_SILENCE) || pc.getSkillEffectTimerSet().hasSkillEffect(CONFUSION)) {
				if (!(_skillId >= SHOCK_STUN && _skillId <= COUNTER_BARRIER) && !(_skillId >= 226 && _skillId <= 230) && !(_skillId == PRIME)
						&& !(_skillId == FORCE_STUN) && !(_skillId == DEMOLITION) && !(_skillId == 엠파이어) && !(_skillId == HALPHAS) && !(_skillId == AVENGER)
						&& !(_skillId >= 235 && _skillId <= 239)) {
					pc.sendPackets(new S_ServerMessage(285), true);
					return false;
				}
			}

			if (isItemConsume() == false && !_player.isGm()) {
				_player.sendPackets(new S_ServerMessage(299), true);
				return false;
			}
		} else if (_user instanceof L1NpcInstance) {
			if (_user.getSkillEffectTimerSet().hasSkillEffect(CONFUSION)) {
				return false;
			}

			if (_user.getSkillEffectTimerSet().hasSkillEffect(SILENCE)) {
				_user.getSkillEffectTimerSet().killSkillEffectTimer(SILENCE);
				return false;
			}
		}

		if (!isHPMPConsume()) {
			return false;
		}

		return true;
	}

	private boolean isSpellScrollUsable() {
		L1PcInstance pc = (L1PcInstance) _user;
		if (pc.isParalyzed()) {
			return false;
		}

		if ((pc.isInvisble() || pc.isInvisDelay()) && !isInvisUsableSkill(pc)) {
			return false;
		}

		return true;
	}

	private boolean isInvisUsableSkill(L1PcInstance pc) {
		for (int skillId : CAST_WITH_INVIS) {
			if (skillId == _skillId) {
				return true;
			}
		}

		return false;
	}

	public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, String message, int timeSecs, int type) {
		L1Character attacker = null;
		handleCommands(player, skillId, targetId, x, y, message, timeSecs, type, attacker);
	}

	private boolean useok = true;

	public void handleCommands(L1PcInstance player, int skillId, int targetId, int x, int y, String message, int timeSecs, int type, L1Character attacker) {
		try {
			USE_SPELLSC = false;

			// 스킬 사용가능 유무 확인
			boolean isUseSkill = true;

			// TODO checkUseSkill() 함수에서 전역변수 저장하기 때문에 isCheckedUseSkill() 함수 결과는 항상 false가
			// 나와야할듯..
			if (!isCheckedUseSkill()) {
				isUseSkill = checkUseSkill(player, skillId, targetId, x, y, message, timeSecs, type, attacker);
				if (!isUseSkill) {
					failSkill();
					return;
				}
			}

			/** 2016.11.26 MJ 앱센터 LFC **/
			if (player != null && (player instanceof L1PcInstance)) {
				if (_skill.getType() != L1Skills.TYPE_CHANGE && _skill.getType() != L1Skills.TYPE_HEAL
						&& player.getInstStatus() == InstStatus.INST_USERSTATUS_LFCINREADY) {
					return;
				}
			}

			// 파티 적용 유무 확인
			for (int partyApplySkillId : partyApplySkillArray) {
				if (_skillId == partyApplySkillId) {
					setCheckedPartyApplySkill(true);
				}
			}

			// 사용한 스킬이 길드원 적용 스킬 유무 확인
			for (int clanApplySkillId : clanApplySkillArray) {
				if (_skillId == clanApplySkillId) {
					setCheckedClanApplySkill(true);
				}
			}

			switch (type) {
			case TYPE_NORMAL:
				boolean imm = false;
				if (_target != null && isUseSkill && _user instanceof L1PcInstance) {
					if ((CharPosUtil.isAreaAttack(_player, _target.getX(), _target.getY(), _target.getMapId()) == false
							|| CharPosUtil.isAreaAttack(_target, _player.getX(), _player.getY(), _player.getMapId()) == false)) {
						if (_skillId == L1SkillId.IMMUNE_TO_HARM && _skill.getType() == L1Skills.TYPE_HEAL) {
							imm = true;
						}
					}
				}

				if ((!_isGlanceCheckFail && !imm) || _skill.getArea() > 0 || _skill.getTarget().equals("none")) {
					runSkill(); // 스킬 사용 처리
					useConsume(); // 스킬 사용 시 HP, MP, 성향치, 재료 등 변경 처리
					sendGrfx(true);
					sendFailMessageHandle();
					// setDelay();
					pinkname();
				} else {
					if (isUseSkill && _user instanceof L1PcInstance) {
						int actionId = _skill.getActionId();
						int castgfx = _skill.getCastGfx();
						int targetid = _target.getId();
						if (castgfx > 0) {
							if (_skill.getTarget().equals("buff") && _skill.getType() != L1Skills.TYPE_HEAL) {
								S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), _skill.getActionId());
								_player.sendPackets(gfx);
								Broadcaster.broadcastPacket(_player, gfx, true);
							} else if (_skill.getTarget().equals("attack") && _skillId != 18) {
								boolean ck = false;
								if (isPcSummonPet(_target)) {
									if (CharPosUtil.getZoneType(_player) == 1 || CharPosUtil.getZoneType(_target) == 1
											|| _player.checkNonPvP(_player, _target)) {

										if (_skillId == 229) {
											_player.sendPackets(new S_UseAttackSkill(_player, _target), true);
											Broadcaster.broadcastPacket(_player, new S_UseAttackSkill(_player, _target), true);
										} else {
											_player.sendPackets(new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId, true), true);
											Broadcaster.broadcastPacket(_player, new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId, true),
													true);
										}

										ck = true;
									}
								}

								if (!ck) {
									if (_skillId == 229) {
										_player.sendPackets(new S_UseAttackSkill(_player, _target), true);
										Broadcaster.broadcastPacket(_player, new S_UseAttackSkill(_player, _target), true);
									} else {
										_player.sendPackets(new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId, true), true);
										Broadcaster.broadcastPacket(_player,
												new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId, true), true);
									}
								}
							} else {
								if (_skillId != 5 && _skillId != 69 && _skillId != 131) {
									if (actionId > 0) {
										S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), _skill.getActionId());
										_player.sendPackets(gfx);
										Broadcaster.broadcastPacket(_player, gfx, true);
									}
									if (/*
										 * _skillId == COUNTER_MIRROR //|| _skillId == TRUE_TARGET ||
										 */_skillId == INVISIBILITY) {
									} else if (_skillId == TURN_UNDEAD) {
										_player.sendPackets(new S_ServerMessage(280), true);
									} else {
										if(_player.isSkillMastery(L1SkillId.FEAR)) {
											_player.sendPackets(new S_SkillSound(targetid, 18959), true);
											Broadcaster.broadcastPacket(_player, new S_SkillSound(targetid, 18959), true);
										}
										if(_player.isSkillMastery(L1SkillId.HORROR_OF_DEATH)) {
											_player.sendPackets(new S_SkillSound(targetid, 18961), true);
											Broadcaster.broadcastPacket(_player, new S_SkillSound(targetid, 18961), true);
										}
										
										S_SkillSound ss = new S_SkillSound(targetid, castgfx);
										_player.sendPackets(ss);
										Broadcaster.broadcastPacket(_player, ss, true);
									}
								}
							}
						}
					}
				}

				if (_user instanceof L1PcInstance) {
					if (!((L1PcInstance) _user).AttackCheckUseSKill) {
						((L1PcInstance) _user).AttackCheckUseSKill = true;
						((L1PcInstance) _user).AttackCheckUseSKillDelay = getRightInterval(((L1PcInstance) _user),
								SkillsTable.getInstance().getTemplate(skillId).getActionId());
						GeneralThreadPool.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								((L1PcInstance) _user).AttackCheckUseSKill = false;
							}
						}, ((L1PcInstance) _user).AttackCheckUseSKillDelay / 4);
					}

				}

				break;

			case TYPE_LOGIN:
				runSkill();
				break;

			case TYPE_SPELLSC:
				USE_SPELLSC = true;
				runSkill();
				setDelay();
				sendFailMessageHandle();
				sendGrfx(true);
				if (_user instanceof L1PcInstance) {
					if (!((L1PcInstance) _user).AttackCheckUseSKill) {
						((L1PcInstance) _user).AttackCheckUseSKill = true;
						((L1PcInstance) _user).AttackCheckUseSKillDelay = getRightInterval(((L1PcInstance) _user),
								SkillsTable.getInstance().getTemplate(skillId).getActionId());
						GeneralThreadPool.getInstance().schedule(new Runnable() {
							@Override
							public void run() {
								((L1PcInstance) _user).AttackCheckUseSKill = false;
							}
						}, ((L1PcInstance) _user).AttackCheckUseSKillDelay / 4);
					}
				}
				break;

			case TYPE_GMBUFF:
				runSkill();
				sendGrfx(false);
				break;

			case TYPE_NPCBUFF:
				runSkill();
				sendGrfx(true);
				break;

			default:
				break;
			}

			setCheckedUseSkill(false);

			// 파티, 혈원 적용 스킬 확인 초기화
			setCheckedPartyApplySkill(false);
			setCheckedClanApplySkill(false);
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("skillId : "+skillId+" / attacker : "+attacker.getName());
			// _log.log(Level.SEVERE, "", e);
		}
	}

	private static Random _random = new Random(System.nanoTime());

	private void pinkname() {
		if ((_skill.getTarget().equals("buff") && _calcType == PC_PC) && CharPosUtil.getZoneType(_user) == 0 && CharPosUtil.getZoneType(_target) != 1) {
			if (_skill.getType() == L1Skills.TYPE_PROBABILITY || _skill.getType() == L1Skills.TYPE_CURSE) {
				if (_target instanceof L1PcInstance) {
					L1PcInstance target = (L1PcInstance) _target;
					L1PinkName.onAction(target, _user);
				}
			} else if (_skill.getType() == L1Skills.TYPE_HEAL || _skill.getType() == L1Skills.TYPE_CHANGE) {
				if (_target instanceof L1PcInstance) {
					L1PcInstance target = (L1PcInstance) _target;
					if (target.isPinkName()) {
						L1PinkName.onAction(target, _user);
					}
				}
			}
		}
	}

	private void failSkill() {

		setCheckedUseSkill(false);
		if (_skillId == TELEPORT || _skillId == MASS_TELEPORT || _skillId == TELEPORT_TO_MOTHER) {
			_player.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
		}
	}

	private boolean isTarget(L1Character cha) throws Exception {
		boolean _flg = false;

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.isGhost() || pc.isGmInvis()) {
				return false;
			}
		}

		if (_calcType == NPC_PC && (cha instanceof L1PcInstance || cha instanceof L1PetInstance || cha instanceof L1SummonInstance)) {
			_flg = true;
		}

		if (cha instanceof L1DoorInstance) {
			if (cha.getMaxHp() == 0 || cha.getMaxHp() == 1) {
				return false;
			}
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_PC && cha instanceof L1PcInstance
				&& _user instanceof L1SummonInstance) {
			L1SummonInstance summon = (L1SummonInstance) _user;

			if (cha.getId() == summon.getMaster().getId()) {
				return false;
			}

			if (CharPosUtil.getZoneType(cha) == 1) {
				return false;
			}
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_PC && cha instanceof L1PcInstance
				&& _user instanceof L1PetInstance) {
			L1PetInstance pet = (L1PetInstance) _user;
			if (cha.getId() == pet.getMaster().getId()) {
				return false;
			}

			if (CharPosUtil.getZoneType(cha) == 1) {
				return false;
			}
		}

		if (cha instanceof L1DollInstance && _skillId != HASTE) {
			return false;
		}

		if (_skill.getSkillId() == RETURN_TO_NATURE && _calcType == PC_NPC
				&& (CharPosUtil.getZoneType(_player) == 1 || CharPosUtil.getZoneType(_target) == 1 || _player.checkNonPvP(_player, _target))) {
			if (!_player.isGm())
				return false;
		}

		if (_calcType == PC_NPC && _target instanceof L1NpcInstance && !(_target instanceof L1PetInstance) && !(_target instanceof L1SummonInstance)
				&& (cha instanceof L1PetInstance || cha instanceof L1SummonInstance || cha instanceof L1PcInstance)) {
			return false;
		}

		if (_calcType == PC_NPC && _target instanceof L1NpcInstance && !(_target instanceof L1GuardInstance) && cha instanceof L1GuardInstance) {
			return false;
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_PC && !(cha instanceof L1PetInstance)
				&& !(cha instanceof L1SummonInstance) && !(cha instanceof L1PcInstance)) {
			return false;
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _calcType == NPC_NPC && _user instanceof L1MonsterInstance
				&& cha instanceof L1MonsterInstance) {
			return false;
		}

		if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK
				&& (cha instanceof L1AuctionBoardInstance || cha instanceof L1BoardInstance || cha instanceof L1CrownInstance || cha instanceof L1DwarfInstance
						|| cha instanceof L1EffectInstance || cha instanceof L1FieldObjectInstance || cha instanceof L1FurnitureInstance
						|| cha instanceof L1HousekeeperInstance || cha instanceof L1MerchantInstance || cha instanceof L1TeleporterInstance)) {
			return false;
		}

		if (_skill.getType() == L1Skills.TYPE_ATTACK && cha.getId() == _user.getId()) {
			return false;
		}

		if (cha.getId() == _user.getId() && _skillId == HEAL_ALL) {
			return false;
		}

		if (((_skill.getTargetTo() & L1Skills.TARGET_TO_PC) == L1Skills.TARGET_TO_PC
				|| (_skill.getTargetTo() & L1Skills.TARGET_TO_CLAN) == L1Skills.TARGET_TO_CLAN
				|| (_skill.getTargetTo() & L1Skills.TARGET_TO_PARTY) == L1Skills.TARGET_TO_PARTY) && cha.getId() == _user.getId() && _skillId != HEAL_ALL) {
			return true;
		}

		if (_user instanceof L1PcInstance && (_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && _isPK == false) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (_player.getId() == summon.getMaster().getId()) {
					return false;
				}
			} else if (cha instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) cha;
				if (_player.getId() == pet.getMaster().getId()) {
					return false;
				}
			}
		}

		if ((_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK) && !(cha instanceof L1MonsterInstance)
				&& !(cha instanceof L1PeopleInstance) && _isPK == false && _target instanceof L1PcInstance) {
			L1PcInstance enemy = (L1PcInstance) cha;
			if (_skillId == COUNTER_DETECTION && CharPosUtil.getZoneType(enemy) != 1
					&& (cha.getSkillEffectTimerSet().hasSkillEffect(INVISIBILITY) || cha.getSkillEffectTimerSet().hasSkillEffect(BLIND_HIDING))) {
				return true;
			}

			if (_player.getClanid() != 0 && enemy.getClanid() != 0) {
				for (L1War war : L1World.getInstance().getWarList()) {
					if (war.CheckClanInWar(_player.getClanname())) {
						if (war.CheckClanInSameWar(_player.getClanname(), enemy.getClanname())) {
							if (L1CastleLocation.checkInAllWarArea(enemy.getX(), enemy.getY(), enemy.getMapId())) {
								return true;
							}
						}
					}
				}
			}

			return false;
		}

		if ((CharPosUtil.isAreaAttack(_user, cha.getX(), cha.getY(), cha.getMapId()) == false
				|| CharPosUtil.isAreaAttack(cha, _user.getX(), _user.getY(), _user.getMapId()) == false) && _skill.isThrough() == false) {
			if (!(_skill.getType() == L1Skills.TYPE_CHANGE || _skill.getType() == L1Skills.TYPE_RESTORE)) {
				_isGlanceCheckFail = true;
				return false;
			}
		}

		if ((cha.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE))
				&& (_skillId == ICE_LANCE || _skillId == SHOCK_STUN || _skillId == 데스페라도 || _skillId == 엠파이어 || _skillId == 파워그립 || _skillId == FORCE_STUN
						|| _skillId == ETERNITY || _skillId == DEMOLITION || _skillId == AVENGER || _skillId == PANTERA || _skillId == 쉐도우스탭 || _skillId == PHANTOM)) {
			return false;
		}

		if ((cha.getSkillEffectTimerSet().hasSkillEffect(MOB_BASILL) && _skillId == MOB_BASILL)
				|| (cha.getSkillEffectTimerSet().hasSkillEffect(MOB_COCA) && _skillId == MOB_COCA)) {
			return false;
		}

		if (cha.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND)) {
			if (_skillId != WEAPON_BREAK && _skillId != CANCELLATION // 확률계
					&& _skillId != 208 && _skillId != 217// 본브레이크, 패닉
					&& _skill.getType() != L1Skills.TYPE_CHANGE) { // 버프 계
				return false;
			}
		}

		if (!(cha instanceof L1MonsterInstance) && (_skillId == TAMING_MONSTER || _skillId == CREATE_ZOMBIE)) {
			return false;
		}

		if (cha.isDead() && (_skillId != CREATE_ZOMBIE && _skillId != RESURRECTION && _skillId != GREATER_RESURRECTION && _skillId != CALL_OF_NATURE)) {
			return false;
		}

		if (!(cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) && cha.isDead() == false
				&& (_skillId == CREATE_ZOMBIE || _skillId == RESURRECTION || _skillId == GREATER_RESURRECTION || _skillId == CALL_OF_NATURE)) {
			return false;
		}

		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			if (pc.getSkillEffectTimerSet().hasSkillEffect(ABSOLUTE_BARRIER) || pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_안전모드)) {
				if (_skillId == CURSE_BLIND || _skillId == WEAPON_BREAK || _skillId == DARKNESS || _skillId == WEAKNESS || _skillId == FOG_OF_SLEEPING
						|| _skillId == SLOW || _skillId == CANCELLATION || _skillId == SILENCE || _skillId == DECAY_POTION || _skillId == MASS_TELEPORT
						|| _skillId == DETECTION || _skillId == HORROR_OF_DEATH || _skillId == COUNTER_DETECTION
						|| _skillId == ERASE_MAGIC || _skillId == FREEZING_BREATH || _skillId == ENTANGLE || _skillId == FEAR
						|| _skillId == PHYSICAL_ENCHANT_DEX || _skillId == PHYSICAL_ENCHANT_STR || _skillId == BLESS_WEAPON || _skillId == FIRE_SHIELD
						|| _skillId == IMMUNE_TO_HARM || _skillId == REMOVE_CURSE || _skillId == CONFUSION || _skillId == MOB_SLOW_1 || _skillId == MOB_SLOW_18
						|| _skillId == MOB_WEAKNESS_1 || _skillId == MOB_DISEASE_1 || _skillId == MOB_BASILL || _skillId == MOB_SHOCKSTUN_30
						|| _skillId == MOB_RANGESTUN_19 || _skillId == MOB_RANGESTUN_18 || _skillId == MOB_DISEASE_30 || _skillId == MOB_WINDSHACKLE_1
						|| _skillId == MOB_COCA || _skillId == MOB_CURSEPARALYZ_19 || _skillId == MOB_CURSEPARALYZ_18 || _skillId == ANTA_SKILL_1
						|| _skillId == ANTA_SKILL_2 || _skillId == ANTA_SKILL_3 // 안타라스 용언
						|| _skillId == ANTA_SKILL_4 || _skillId == ANTA_SKILL_5 || _skillId == ANTA_SKILL_6 || _skillId == ANTA_SKILL_7
						|| _skillId == ANTA_SKILL_8 || _skillId == ANTA_SKILL_9 || _skillId == ANTA_SKILL_10 || _skillId == ANTA_SKILL_11
						|| _skillId == ANTA_SKILL_12 || _skillId == ANTA_SKILL_13 || _skillId == ANTA_SKILL_14 || _skillId == 40027 || _skillId == 데스힐
						|| _skillId == miso1 || _skillId == miso2 || _skillId == miso3) {
					return true;
				} else {
					return false;
				}
			}
		}

		if (cha instanceof L1NpcInstance) {
			int hiddenStatus = ((L1NpcInstance) cha).getHiddenStatus();
			if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
				if (_skillId == DETECTION || _skillId == COUNTER_DETECTION || _skillId == FREEZING_BREATH) {
					return true;
				} else {
					return false;
				}
			} else if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_FLY) {
				return false;
			}
		}

		if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PC) == L1Skills.TARGET_TO_PC && cha instanceof L1PcInstance) {
			_flg = true;
		} else if ((_skill.getTargetTo() & L1Skills.TARGET_TO_NPC) == L1Skills.TARGET_TO_NPC && cha instanceof L1NpcInstance
				&& !(cha instanceof L1BoardInstance || cha instanceof L1ArrowInstance)) {
			_flg = true;
		} else if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PET) == L1Skills.TARGET_TO_PET && _user instanceof L1PcInstance) {
			if (cha instanceof L1SummonInstance) {
				if (_skill.getSkillId() == RETURN_TO_NATURE) {
					_flg = true;
				}

				L1SummonInstance summon = (L1SummonInstance) cha;
				if (summon.getMaster() != null) {
					if (_player.getId() == summon.getMaster().getId()) {
						_flg = true;
					}
				}
			}

			if (cha instanceof L1PetInstance) {
				L1PetInstance pet = (L1PetInstance) cha;
				if (pet.getMaster() != null) {
					if (_player.getId() == pet.getMaster().getId()) {
						_flg = true;
					}
				}
			}
		}

		if (_calcType == PC_PC) {
			if (CharPosUtil.getZoneType(_player) == 1 || CharPosUtil.getZoneType(cha) == 1) {
				if (_skillId == L1SkillId.CURE_POISON || _skillId == L1SkillId.MIND_BREAK || _skillId == L1SkillId.PANIC || _skillId == L1SkillId.PHANTASM
						|| _skillId == L1SkillId.CURSE_BLIND || _skillId == L1SkillId.WEAPON_BREAK || _skillId == L1SkillId.CURSE_PARALYZE
						|| _skillId == L1SkillId.MANA_DRAIN || _skillId == L1SkillId.DARKNESS || _skillId == L1SkillId.WEAKNESS || _skillId == L1SkillId.SILENCE
						|| _skillId == L1SkillId.FOG_OF_SLEEPING || _skillId == L1SkillId.DECAY_POTION || _skillId == L1SkillId.EARTH_BIND
						|| _skillId == L1SkillId.WIND_SHACKLE || _skillId == L1SkillId.쉐도우스탭 || _skillId == L1SkillId.PANTERA || _skillId == L1SkillId.BLADE
						|| _skillId == L1SkillId.PHANTOM || _skillId == L1SkillId.ELEMENTAL_FALL_DOWN)
					_flg = false;
			}
		}

		if (_calcType == PC_PC && cha instanceof L1PcInstance) {
			if ((_skill.getTargetTo() & L1Skills.TARGET_TO_CLAN) == L1Skills.TARGET_TO_CLAN
					&& ((_player.getClanid() != 0 && _player.getClanid() == ((L1PcInstance) cha).getClanid()) || _player.isGm()
							|| (_player.getMapId() >= 1005 && _player.getMapId() <= 1010) || (_player.getMapId() >= 1011 && _player.getMapId() <= 1016)
							|| (_player.getMapId() >= 10000 && _player.getMapId() <= 10005))) {
				return true;
			}

			if ((_skill.getTargetTo() & L1Skills.TARGET_TO_PARTY) == L1Skills.TARGET_TO_PARTY
					&& (_player.getParty().isMember((L1PcInstance) cha) || _player.isGm())) {
				return true;
			}
		}

		return _flg;
	}

	private void makeTargetList() {

		try {
			if (_type == TYPE_LOGIN) {
				_targetList.add(new TargetStatus(_user));
				return;
			}

			if (_skill.getTargetTo() == L1Skills.TARGET_TO_ME && (_skill.getType() & L1Skills.TYPE_ATTACK) != L1Skills.TYPE_ATTACK) {
				_targetList.add(new TargetStatus(_user));
				return;
			}

			if (_skill.getRanged() != -1) {
				if (_user.getLocation().getTileLineDistance(_target.getLocation()) > _skill.getRanged()) {
					return;
				}
			} else {
				if (!_user.getLocation().isInScreen(_target.getLocation())) {
					return;
				}
			}

			if (_skill.getTarget().equals("buff") && _calcType == PC_PC
					&& (CharPosUtil.isAreaAttack(_user, _target.getX(), _target.getY(), _target.getMapId()) == false
							|| CharPosUtil.isAreaAttack(_target, _user.getX(), _user.getY(), _user.getMapId()) == false)) {
				// System.out.println("돌림");
				return;

			}

			if (isTarget(_target) == false && !(_skill.getTarget().equals("none"))) {
				return;
			}

			if (_skillId == LIGHTNING) {
				for (L1Object tgobj : L1World.getInstance().getVisibleLineObjects(_user, _target)) {
					if (tgobj == null) {
						continue;
					}

					if (!(tgobj instanceof L1Character)) {
						continue;
					}

					L1Character cha = (L1Character) tgobj;
					if (isTarget(cha) == false) {
						continue;
					}

					_targetList.add(new TargetStatus(cha));
				}

				return;
			}

			if (_skill.getArea() == 0) {
				if (!CharPosUtil.isAreaAttack(_user, _target.getX(), _target.getY(), _target.getMapId())
						|| !CharPosUtil.isAreaAttack(_target, _user.getX(), _user.getY(), _user.getMapId())) {
					if ((_skill.getType() & L1Skills.TYPE_ATTACK) == L1Skills.TYPE_ATTACK) {
						_targetList.add(new TargetStatus(_target, false));
						return;
					}
				}
				_targetList.add(new TargetStatus(_target));
			} else {

				if (!_skill.getTarget().equals("none")) {
					_targetList.add(new TargetStatus(_target));
				}

				if (_skillId != 49 && !(_skill.getTarget().equals("attack") || _skill.getType() == L1Skills.TYPE_ATTACK)) {
					_targetList.add(new TargetStatus(_user));
				}

				List<L1Object> objects;
				if (_skill.getArea() == -1) {
					if (_user instanceof L1PcInstance) {
						objects = _user.getNearObjects().getKnownObjects();
					} else {
						objects = L1World.getInstance().getVisibleObjects(_user);
					}
				} else {
					if (_target instanceof L1PcInstance) {
						objects = _target.getNearObjects().getVisibleObjects(_target, _skill.getArea());
					} else {
						objects = L1World.getInstance().getVisibleObjects(_target, _skill.getArea());
					}
				}

				for (L1Object tgobj : objects) {
					try {
						if (tgobj == null) {
							continue;
						}

						if (!(tgobj instanceof L1Character)) {
							continue;
						}

						L1Character cha = (L1Character) tgobj;

						/* 범위 공격 마법 PC대상시 공성존 체크 */
						if (_skill.getTarget().equals("attack")) {
							if (_target instanceof L1PcInstance && cha instanceof L1PcInstance) {
								int castleId = L1CastleLocation.getCastleIdByArea(_target);
								if (castleId == 0) {
									continue;
								}
							}
						}

						if (_skill.getArea() == -1) {
							if (!CharPosUtil.isAreaAttack(_user, cha.getX(), cha.getY(), cha.getMapId())
									|| !CharPosUtil.isAreaAttack(cha, _user.getX(), _user.getY(), _user.getMapId())) {
								continue;
							}
						} else {
							if (!CharPosUtil.isAreaAttack(_target, cha.getX(), cha.getY(), cha.getMapId())
									|| !CharPosUtil.isAreaAttack(cha, _target.getX(), _target.getY(), _target.getMapId())) {
								continue;
							}
						}

						if (!isTarget(cha)) {
							if (_skillId == SHAPE_CHANGE) {
								// System.out.println("체크");

							}

							continue;
						}

						_targetList.add(new TargetStatus(cha));
					} catch (Exception e) {
						// e.printStackTrace();
					}
				}

				return;
			}

		} catch (Exception e) {
			_log.finest("exception in L1Skilluse makeTargetList" + e);
		}
	}

	private void sendHappenMessage(L1PcInstance pc) {
		int msgID = _skill.getSysmsgIdHappen();
		if (msgID > 0) {
			pc.sendPackets(new S_ServerMessage(msgID), true);
		}
	}

	private void sendFailMessageHandle() {
		if (_skill.getType() != L1Skills.TYPE_ATTACK && !_skill.getTarget().equals("none") && _targetList.size() == 0) {
			sendFailMessage();
		}
	}

	private void sendFailMessage() {
		int msgID = _skill.getSysmsgIdFail();
		if (msgID > 0 && (_user instanceof L1PcInstance)) {
			_player.sendPackets(new S_ServerMessage(msgID), true);
		}
	}

	private boolean isAttrAgrees() {
		int magicattr = _skill.getAttr();
		if (_user instanceof L1NpcInstance || _user instanceof L1RobotInstance) {
			return true;
		}

		if (!_player.글로리어스 && (_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && _skillId >= 포커스웨이브 && _skillId <= 샌드스톰 && magicattr != 0)
				&& (magicattr != _player.getElfAttr() && !_player.isGm())) {
			return false;
		} else if (_player.글로리어스 && (_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && _skillId >= 포커스웨이브 && _skillId <= 샌드스톰
				&& (magicattr == 1 || magicattr == 2)) && (_player.getElfAttr() == 21)) {
			return true;
		} else if (_player.글로리어스 && (_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && _skillId >= 포커스웨이브 && _skillId <= 샌드스톰
				&& (magicattr == 2 || magicattr == 4)) && (_player.getElfAttr() == 24)) {
			return true;
		} else if (_player.글로리어스 && (_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && _skillId >= 포커스웨이브 && _skillId <= 샌드스톰
				&& (magicattr == 2 || magicattr == 8)) && (_player.getElfAttr() == 28)) {
			return true;
		} else if (_player.글로리어스 && (_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && _skillId >= 포커스웨이브 && _skillId <= 샌드스톰
				&& (magicattr == 1 || magicattr == 4)) && (_player.getElfAttr() == 41)) {
			return true;
		} else if (_player.글로리어스 && (_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && _skillId >= 포커스웨이브 && _skillId <= 샌드스톰
				&& (magicattr == 4 || magicattr == 8)) && (_player.getElfAttr() == 48)) {
			return true;
		} else if (_player.글로리어스 && (_skill.getSkillLevel() >= 17 && _skill.getSkillLevel() <= 22 && _skillId >= 포커스웨이브 && _skillId <= 샌드스톰
				&& (magicattr == 1 || magicattr == 8)) && (_player.getElfAttr() == 81)) {
			return true;
		}

		return true;
	}

	private boolean isHPMPConsume() {
		int minusMP = 0;
		_mpConsume = _skill.getMpConsume();
		_hpConsume = _skill.getHpConsume();
		int currentMp = 0;
		int currentHp = 0;
		if (_user instanceof L1NpcInstance) {
			currentMp = _npc.getCurrentMp();
			currentHp = _npc.getCurrentHp();
		} else {
			currentMp = _player.getCurrentMp();
			currentHp = _player.getCurrentHp();
			if (!(_skillId >= SHOCK_STUN && _skillId <= COUNTER_BARRIER) && !(_skillId >= 225 && _skillId <= 230) && !(_skillId == DEMOLITION)
					&& !(_skillId == FORCE_STUN) && !(_skillId == HALPHAS) && !(_skillId >= JUDGEMENT && _skillId <= BLADE)) {

				int maxConsume = _skill.getMpConsume();

				double balance = CalcStat.엠소모감소(_player.getAbility().getTotalInt()) * 0.01;

				minusMP = (int) (maxConsume * balance);

				if (minusMP > 0) {
					_mpConsume -= minusMP;
				}
			}

			if ((_skillId == PHYSICAL_ENCHANT_DEX || _skillId == HASTE)
					&& (_player.getInventory().checkEquipped(20013) || _player.getInventory().checkEquipped(120013))) {
				_mpConsume /= 2;
			}

			if ((_skillId == HEAL || _skillId == EXTRA_HEAL) && (_player.getInventory().checkEquipped(20014) || _player.getInventory().checkEquipped(120014))) {
				_mpConsume /= 2;
			}

			if ((_skillId == ENCHANT_WEAPON || _skillId == DETECTION || _skillId == PHYSICAL_ENCHANT_STR)
					&& (_player.getInventory().checkEquipped(20015) || _player.getInventory().checkEquipped(120015))) {
				_mpConsume /= 2;
			}

			if (_skillId == HASTE && _player.getInventory().checkEquipped(20008)) {
				_mpConsume /= 2;
			}

			if (_skillId == GREATER_HASTE && _player.getInventory().checkEquipped(20023)) {
				_mpConsume /= 2;
			}

			if (0 < _skill.getMpConsume()) {
				_mpConsume = Math.max(_mpConsume, 1);
			}
		}

		if (currentHp < _hpConsume + 1) {
			if (_user instanceof L1PcInstance) {
				_player.sendPackets(new S_ServerMessage(279), true);
			}

			return false;
		} else if (currentMp < _mpConsume) {
			if (_user instanceof L1PcInstance) {
				_player.sendPackets(new S_ServerMessage(278), true);
			}

			return false;
		}

		return true;
	}

	private boolean isItemConsume() {

		/** 만약 로봇이라면 소비아이템 소비 없이 사용 효과 */
		if (_player instanceof L1RobotInstance) {
			return true;
		}

		int itemConsume = _skill.getItemConsumeId();
		int itemConsumeCount = _skill.getItemConsumeCount();

		if (itemConsume == 0) {
			return true;
		}

		if (_player.getLevel() < 52) {
			int itemConsume2 = 0;
			if (itemConsume == 40318) {// 마돌
				itemConsume2 = 60138;
			} else if (itemConsume == 40319) { // 정령옥
				itemConsume2 = 60137;
			} else if (itemConsume == 40321) { // 흑요석
				itemConsume2 = 60139;
			} else if (itemConsume == 430007) { // 각인의 뼈조각
				itemConsume2 = 60140;
			} else if (itemConsume == 430006) { // 유그드라
				itemConsume2 = 60136;
			} else if (itemConsume == 430008) { // 속성석
				itemConsume2 = 60157;
			}

			if (itemConsume2 != 0) {
				if (_player.getInventory().checkItem(itemConsume2, itemConsumeCount)) {
					return true;
				}
			}
		}

		if (!_player.getInventory().checkItem(itemConsume, itemConsumeCount)) {
			return false;
		}

		return true;
	}

	/**
	 * 스킬 사용 시 HP, MP, 성향치, 재료 등 처리
	 */
	private void useConsume() {

		if (_user instanceof L1NpcInstance) {
			int current_hp = _npc.getCurrentHp() - _hpConsume;
			_npc.setCurrentHp(current_hp);

			int current_mp = _npc.getCurrentMp() - _mpConsume;
			_npc.setCurrentMp(current_mp);
			return;
		}

		if (isHPMPConsume()) {
			if (_skillId == FINAL_BURN) {
				if (_player.getCurrentHp() >= 100) {
					_player.setCurrentHp(100);
				} else {
					_player.setCurrentHp(1);
				}
				_player.setCurrentMp(1);
			} else {
				int current_hp = _player.getCurrentHp() - _hpConsume;
				_player.setCurrentHp(current_hp);

				int current_mp = _player.getCurrentMp() - _mpConsume;
				_player.setCurrentMp(current_mp);
			}
		}

		int lawful = _player.getLawful() + _skill.getLawful();
		if (lawful > 32767) {
			lawful = 32767;
		}

		if (lawful < -32767) {
			lawful = -32767;
		}
		_player.setLawful(lawful);

		int itemConsume = _skill.getItemConsumeId();
		int itemConsumeCount = _skill.getItemConsumeCount();

		if (itemConsume == 0) {
			return;
		}

		if (_player.getLevel() <= 51) {
			int itemConsume2 = 0;
			if (itemConsume == 40318) {// 마돌
				itemConsume2 = 60138;
			} else if (itemConsume == 40319) { // 정령옥
				itemConsume2 = 60137;
			} else if (itemConsume == 40321) { // 흑요석
				itemConsume2 = 60139;
			} else if (itemConsume == 430007) { // 각인의 뼈조각
				itemConsume2 = 60140;
			} else if (itemConsume == 430006) { // 유그드라
				itemConsume2 = 60136;
			} else if (itemConsume == 430008) { // 속성석
				itemConsume2 = 60157;
			}

			if (itemConsume2 != 0) {
				if (_player.getInventory().checkItem(itemConsume2, itemConsumeCount)) {
					_player.getInventory().consumeItem(itemConsume2, itemConsumeCount);
					return;
				}
			}
		}

		_player.getInventory().consumeItem(itemConsume, itemConsumeCount);
	}

	/**
	 * 스킬 타이머 등록 및 버프 아이콘 표시
	 */
	private void addMagicList(L1Character cha, boolean repetition) {

		if (_skillTime == 0) {
			// DB에서 조회한 스킬 지속 시간 * 1000
			_getBuffDuration = _skill.getBuffDuration() * 1000;
			if (_skill.getBuffDuration() == 0) {
				if (_skillId == INVISIBILITY) {
					cha.getSkillEffectTimerSet().setSkillEffect(INVISIBILITY, 0);
				}

				return;
			}
		} else {
			_getBuffDuration = _skillTime * 1000;
		}

		if (_skillId == 데스힐 || _skillId == SHOCK_STUN || _skillId == 데스페라도 || _skillId == 파워그립 || _skillId == 엠파이어 || _skillId == DEMOLITION
				|| _skillId == FORCE_STUN || _skillId == ETERNITY || _skillId == L1SkillId.쉐도우스탭 || _skillId == PANTERA || _skillId == PHANTOM || _skillId == THUNDER_GRAB) {
			_getBuffDuration = _shockStunDuration;
		}

		if (_skillId == ERASE_MAGIC) {
			_getBuffDuration = 32 * 1000;
		}

		if (_skillId == POLLUTE_WATER) {
			_getBuffDuration = 32 * 1000;
		}

		if (_skillId == STRIKER_GALE) {
			_getBuffDuration = 12 * 1000;
		}

		if (_skillId == DARKNESS) {
			_getBuffDuration = 32 * 1000;
		}

		if (_skillId == WEAKNESS) {
			_getBuffDuration = 64 * 1000;
		}

		if (_skillId == CURSE_PARALYZE || _skillId == CURSE_PARALYZE2 || _skillId == MOB_CURSEPARALYZ_18 || _skillId == MOB_CURSEPARALYZ_19
				|| _skillId == 40013) {
			_getBuffDuration = 4 * 1000;
		}

		if ((_skillId == ICE_LANCE || _skillId == FREEZING_BREATH) && !_isFreeze) {
			return;
		}

		cha.getSkillEffectTimerSet().setSkillEffect(_skillId, _getBuffDuration);
		// 스킬 적용 중 스킬을 사용한 경우
		if (cha instanceof L1PcInstance && repetition) {
			L1PcInstance pc = (L1PcInstance) cha;
			// 스킬 아이콘 표시
			sendIcon(pc);
		}

	}

	/**
	 * 스킬 아이콘 표시
	 */
	private void sendIcon(L1PcInstance pc) {

		if (_skillTime == 0) {
			_getBuffIconDuration = _skill.getBuffDuration();
		} else {
			_getBuffIconDuration = _skillTime;
		}

		switch (_skillId) {
		case SHIELD:
			pc.sendPackets(new S_SkillIconShield(2, _getBuffIconDuration), true);
			break;

		case DRESS_DEXTERITY:
			pc.sendPackets(new S_Dexup(pc, 2, _getBuffIconDuration), true);
			break;

		case DRESS_MIGHTY:
			pc.sendPackets(new S_Strup(pc, 2, _getBuffIconDuration), true);
			break;

		case GLOWING_AURA:
			pc.sendPackets(new S_SkillIconAura(113, _getBuffIconDuration), true);
			break;

		/* 샤이닝 실드 */
		case SHINING_AURA: // 스킬 수정 by white
			if (pc.isInParty()) {
				for (L1PcInstance partyMember : pc.getParty().getMembers()) {

					// 시전자랑 파티원이랑 거리 확인
					if (pc.getLocation().getTileDistance(partyMember.getLocation()) <= 14) {
						// 파티원이 죽은 상태이면 pass
						if (partyMember.isDead()) {
							continue;
						}

						if (!partyMember.getSkillEffectTimerSet().hasSkillEffect(SHINING_AURA)) {
							partyMember.getAC().addAc(-4);
						}

						// 스킬 이펙트 표시 및 사운드 출력
						partyMember.sendPackets(new S_SkillSound(partyMember.getId(), 3941));
						// 스킬 이펙트 전역 표시 및 사운드 출력
						partyMember.broadcastPacket(new S_SkillSound(partyMember.getId(), 3941));

						// 스킬 아이콘 표시 패킷 전송
						partyMember.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSkillIconAura, SHINING_AURA, _getBuffIconDuration), true);

						// 샤이닝 실드 버프 효과 적용
						partyMember.sendPackets(new S_OwnCharAttrDef(partyMember));
					}
				}
			} else {
				// 스킬 이펙트 및 사운드 출력
				pc.sendPackets(new S_SkillSound(pc.getId(), 3941), true);
				pc.broadcastPacket(new S_SkillSound(pc.getId(), 3941));

				// 아이콘 표시
				pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSkillIconAura, SHINING_AURA, _getBuffIconDuration), true);
				// XXX 스킬 사운드 출력 -> 스킬 사운드 출력부터 하고 아이콘 표시 할 경우 표시된 버프 아이콘이 사라짐
				// pc.sendPackets(new S_SkillSound(pc.getId(), 3941));
				pc.sendPackets(new S_OwnCharAttrDef(pc));
			}

			break;

		case BRAVE_AURA:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.BRAVE_AURA, true, _getBuffIconDuration), true);
			break;

		case FIRE_WEAPON:
			pc.sendPackets(new S_SkillIconAura(147, _getBuffIconDuration), true);
			break;

		case WIND_SHOT:
			pc.sendPackets(new S_SkillIconAura(148, _getBuffIconDuration), true);
			break;

		case STORM_EYE:
			pc.sendPackets(new S_SkillIconAura(155, _getBuffIconDuration), true);
			break;

		case BURNING_WEAPON:
			pc.sendPackets(new S_SkillIconAura(162, _getBuffIconDuration), true);
			break;

		case STORM_SHOT:
			pc.sendPackets(new S_SkillIconAura(165, _getBuffIconDuration), true);
			break;

		case IRON_SKIN:
			pc.sendPackets(new S_SkillIconShield(10, _getBuffIconDuration), true);
			break;

		case FIRE_SHIELD:
			pc.sendPackets(new S_SkillIconShield(4, _getBuffIconDuration), true);
			break;

		case CLEAR_MIND:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.CLEAR_MIND, true, 1200));
			pc.sendPackets(new S_SystemMessage("클리어마인드: STR+1,DEX+1,INT+1 ."));
			break;

		case cyclone:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.cyclone, true, 960));
			pc.sendPackets(new S_SystemMessage("싸이 클론:일정 확률로 원거리 대미지 1.5배."));
			break;

		case REDUCTION_ARMOR:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.REDUCTION_ARMOR, true, 300));
			break;

		case SOLID_CARRIAGE:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.SOLID_CARRIAGE, true, 192));
			break;

		case ENCHANT_WEAPON:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.ENCHANT_WEAPON, true, 1200));
			break;

		case HOLY_WEAPON:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.HOLY_WEAPON, true, 1200));
			break;

		case BLESSED_ARMOR:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.BLESSED_ARMOR, true, 1800));
			break;

		case 프라이드:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.프라이드, true, 300));
			break;

		case DRESS_EVASION:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.DRESS_EVASION, true, 192));
			break;

		case AQUA_PROTECTER:// 5
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.AQUA_PROTECTER, true, 960));
			break;

		case SHADOW_FANG:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.SHADOW_FANG, true, 192));
			break;

		case ADVANCE_SPIRIT:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.ADVANCE_SPIRIT, true, 1200));
			break;

		case 기간틱:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.기간틱, true, 300));
			break;

		case STRIKER_GALE:// -99
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.STRIKER_GALE, true, _getBuffIconDuration));
			break;

		case PHYSICAL_ENCHANT_STR:
			pc.sendPackets(new S_Strup(pc, 5, _getBuffIconDuration), true);
			break;

		case PHYSICAL_ENCHANT_DEX:
			pc.sendPackets(new S_Dexup(pc, 5, _getBuffIconDuration), true);
			break;
			
		case miso1:
			pc.sendPackets(new S_MaanIcons(L1SkillId.miso1, true, _getBuffIconDuration));
			break;
			
		case miso2:
			pc.sendPackets(new S_MaanIcons(L1SkillId.miso2, true, _getBuffIconDuration));
			break;
			
		case miso3:
			pc.sendPackets(new S_MaanIcons(L1SkillId.miso3, true, _getBuffIconDuration));
			break;

		case IMMUNE_TO_HARM:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.IMMUNE_TO_HARM, true, _getBuffIconDuration));
			pc.sendPackets(new S_SystemMessage("이뮨 투함: 받는 대미지가 일정량 감소합니다."));
			break;

		case BURNING_SPIRIT:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.BURNING_SPIRIT, true, _getBuffIconDuration));
			pc.sendPackets(new S_SystemMessage("버닝스피릿츠: 일정 확률로 근거리 대미지 1.5배 적용."));
			break;

		case ELEMENTAL_FIRE:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.ELEMENTAL_FIRE, true, _getBuffIconDuration));
			pc.sendPackets(new S_SystemMessage("엘리멘탈파이어:  일정 확률로 근거리 대미지 1.5배 적용."));
			break;

		case ENTANGLE:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.ENTANGLE, true, _getBuffIconDuration));
			pc.sendPackets(new S_SystemMessage("퀘이크: 일정 확률로 근거리 대미지 1.5배 적용."));
			break;

		case BLESS_WEAPON: {
			if (pc.getWeapon() == null) {
				useok = false;
				pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
				return;
			}
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.BLESS_WEAPON, true, _getBuffIconDuration));
		}
			break;

		case JUDGEMENT:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.JUDGEMENT, true, _getBuffIconDuration));
			pc.sendPackets(new S_SystemMessage("저지 먼트 : 모든 내성이 감소합니다."));
			break;

		case HASTE:
		case GREATER_HASTE:
			pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration), true);
			Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 1, 0), true);
			break;

		case 블로우어택:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.블로우어택, true, 300));
			pc.sendPackets(new S_SystemMessage("블로우 어택: 일정 확률로 근거리 대미지 1.5배 적용."));
			break;

		case 루시퍼:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.루시퍼, true, 30));
			pc.sendPackets(new S_SystemMessage("루시퍼 : 받는 대미지가 일정량 감소함."));
			if (pc.루시퍼데스티니) {
				pc.sendPackets(new S_NewSkillIcons(L1SkillId.루시퍼, true, 60));
				pc.sendPackets(new S_SystemMessage("루시퍼 : 받는 대미지가 일정량 감소함."));
			}
			break;

		case POTENTIAL:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.POTENTIAL, true, 128));
			pc.sendPackets(new S_SystemMessage("포텐셜 : HP/MP/DG/ER/MR/SP +20%."));
			break;

		case IllUSION_OGRE: { // 일루젼 오거
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_OGRE, true, 128));
		}
			break;

		case CUBE_IGNITION: { // 일루젼 오거
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_IGNITION, true, 128));
		}
			break;

		case IllUSION_LICH: { // 일루젼 오거
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_LICH, true, 128));
		}
			break;

		case 소울배리어: {
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.소울배리어, true, 600));
		}
			break;

		case CUBE_QUAKE: { // 일루젼 오거
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_QUAKE, true, 128));
		}
			break;
		case IllUSION_DIAMONDGOLEM: { // 일루젼 오거
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_DIAMONDGOLEM, true, 128));
		}
			break;

		case CUBE_SHOCK: { // 일루젼 오거
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_SHOCK, true, 128));
		}
			break;

		case IllUSION_AVATAR: { // 일루젼 오거
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_AVATAR, true, 128));
		}
			break;

		case CUBE_BALANCE: { // 일루젼 오거
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_BALANCE, true, 128));
		}
			break;

		case HOLY_WALK:
		case MOVING_ACCELERATION:
			pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration), true);
			Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 4, 0), true);
			break;

		case WIND_WALK:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.WIND_WALK, true, 128));
			pc.sendPackets(new S_SystemMessage("이글 아이: 원거리 치명타 2% 증가."));
			break;

		case Freeze_armor:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.Freeze_armor, true, 300));
			pc.sendPackets(new S_SystemMessage("프리징아머의 효과가 느껴집니다."));
			break;

		case DISEASE:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.DISEASE, true, 300));
			pc.sendPackets(new S_SystemMessage("인챈트 어큐러시의 효과가 느껴집니다."));
			break;

		case 뫼비우스:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.뫼비우스, true, 5));
			pc.sendPackets(new S_SystemMessage("뫼비우스: 원거리 보호 결계(행동 제약을 받음)."));
			break;

		case 마제스티:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.마제스티, true, 300));
			pc.sendPackets(new S_SystemMessage("마제스티: 대미지 감소+2(80레벨 후 2레벨 당 1씩 증가)."));
			break;

		case 샤이닝아머:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.샤이닝아머, true, 300));
			break;

		case 포커스스피릿츠:
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.포커스스피릿츠, true, 300));
			pc.sendPackets(new S_SystemMessage("포커스 스피릿츠: 마법 치명타 +5%."));
			break;

		case 포커스웨이브:
			pc.sendPackets(new S_SkillBrave(pc.getId(), 10, _getBuffIconDuration), true);
			Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 12, 0), true);
			break;

		case 샌드스톰:
			pc.sendPackets(new S_SkillBrave(pc.getId(), 1, _getBuffIconDuration), true);
			Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0), true);
			break;

		case 허리케인:
			pc.sendPackets(new S_SkillBrave(pc.getId(), 9, _getBuffIconDuration), true);
			Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 9, 0), true);
			break;

		case BLOOD_LUST:
			pc.sendPackets(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration), true);
			Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 6, 0), true);
			break;

		case SLOW:
		case MOB_SLOW_1:
		case MOB_SLOW_18:
			pc.sendPackets(new S_SkillHaste(pc.getId(), 2, _getBuffIconDuration), true);
			Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 2, 0), true);
			break;

		/* 군주 스킬 => 스킬 적용 중 시전 시 처리 */
		case PRIME: // 스킬 수정 by white
			// pc.sendPackets(new S_NewSkillIcons(L1SkillId.PRIME, true,
			// _getBuffIconDuration), true);
			// pc.sendPackets(new S_SystemMessage("프라임 : 근거리 대미지/명중 +3, 원거리 대미지/명중 +3, sp+2,
			// 마법명중+2."));

			// 프라임 스킬 적용 중 시전 시 처리
			// useSkillPrime(pc);

			// 길드가 존재하면
			if (pc.getClanid() != 0) {
				// 시전자 화면에 보여지는 유저 확인
				for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 13)) {
					if (pc.getClanid() == member.getClanid()) {
						if (member.isDead()) {
							continue;
						}

						// 적용 중 사용 시 신규 사용자만 적용
						if (!member.getSkillEffectTimerSet().hasSkillEffect(PRIME)) {
							member.addDmgup(3);
							member.addHitup(3);
							member.addBowDmgup(3);
							member.addBowHitup(3);
							member.getAbility().addSp(2);
							member.프라임 = 5;
							member.addTechniqueHit(pc.프라임);
						}

						// 프라임 스킬 버프 효과 갱신
						member.sendPackets(new S_SPMR(member), true);
						member.sendPackets(new S_SystemMessage("프라임 : 근거리 대미지/명중 +3, 원거리 대미지/명중 +3, sp+2, 마법명중+2."));
						// 프라임 효과 아이콘 갱신
						member.sendPackets(new S_NewSkillIcons(L1SkillId.PRIME, true, _getBuffIconDuration), true);
					}
				}
			}

			// 시전자 처리
			pc.sendPackets(new S_SPMR(pc), true);
			pc.sendPackets(new S_SystemMessage("프라임 : 근거리 대미지/명중 +3, 원거리 대미지/명중 +3, sp+2, 마법명중+2."));
			// 중복 사용 시 지속시간 갱신
			pc.sendPackets(new S_NewSkillIcons(L1SkillId.PRIME, true, _getBuffIconDuration), true);

			break;

		case 그레이스아바타: // 스킬 수정 by white

			if (pc.isInParty()) {
				for (L1PcInstance paty : pc.getParty().getMembers()) {

					if (pc.getLocation().getTileLineDistance(paty.getLocation()) <= 14) {
						if (paty.isDead()) {
							continue;
						}

						if (!paty.getSkillEffectTimerSet().hasSkillEffect(그레이스아바타)) {
							int c = 5;// 기본확률
							if (paty.getLevel() > 80) {
								c += (paty.getLevel() - 80);
							}

							if (c > 10) {// 최대확률
								c = 10;
							}

							// 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제
							paty.그레이스아바타 = c;
							// 임팩트의 대한 적중 효과 적용
							paty.addAllTolerance(paty.그레이스아바타);
						}

						paty.sendPackets(new S_NewSkillIcons(L1SkillId.그레이스아바타, true, 60));
					}
				}
			} else {
				// 중복 사용 시 지속시간 갱신
				pc.sendPackets(new S_NewSkillIcons(L1SkillId.그레이스아바타, true, 60));
			}

			break;

		default:
			break;
		}

		pc.sendPackets(new S_OwnCharStatus(pc));
		pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
	}

	private void sendGrfx(boolean isSkillAction) {
		int actionId = _skill.getActionId();
		int castgfx = _skill.getCastGfx();
		
		if (!useok) {
			return;
		}

		if (_user instanceof L1PcInstance) {
			if (castgfx == 0 && _skillId != INVISIBILITY) {
				return;
			}

			if (((L1PcInstance) _user).skillCritical) {
				((L1PcInstance) _user).skillCritical = false;
				if (_skillId == L1SkillId.DISINTEGRATE || _skillId == L1SkillId.SUNBURST) {
					castgfx += 2;
				} else if (_skillId == L1SkillId.ERUPTION || _skillId == L1SkillId.CONE_OF_COLD) {
					castgfx += 4;
				} else if (_skillId == L1SkillId.CALL_LIGHTNING) {
					castgfx += 1;
				}
			}

			if (_skillId == L1SkillId.UNCANNY_DODGE) {
				if (((L1PcInstance) _user).getAC().getAc() <= -100) {
					castgfx = 11766;
				}
			}

			if (_skillId == FIRE_WALL || _skillId == LIFE_STREAM) {
				L1PcInstance pc = (L1PcInstance) _user;
				if (_skillId == FIRE_WALL) {
					pc.getMoveState().setHeading(CharPosUtil.targetDirection(pc, _targetX, _targetY));
					Broadcaster.broadcastPacket(pc, new S_ChangeHeading(pc), true);
				}

				S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), actionId);
				pc.sendPackets(gfx);
				Broadcaster.broadcastPacket(pc, gfx, true);
				return;
			}

			int targetid = _target.getId();

			if (_skillId == THUNDER_GRAB) {
				int ran2 = random.nextInt(60) + 10;
				L1PcInstance pc = (L1PcInstance) _user;
				if (!pc.썬더그랩브레이브) {
					if (_target instanceof L1PcInstance) {
						L1PcInstance target = (L1PcInstance) _target;
						target.sendPackets(new S_SkillSound(target.getId(), 4184), true);
						Broadcaster.broadcastPacket(target, new S_SkillSound(target.getId(), 4184), true);
						if (isTargetCalc(target)) {
							if (!target.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND) && !target.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
									&& !target.getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH)) {
								target.receiveDamage(_user, ran2, false);
							}
						}
					} else if (_target instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) _target;
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 4184), true);
						npc.receiveDamage(_user, ran2);
					}
				} else if (pc.썬더그랩브레이브) {
					if (_target instanceof L1PcInstance) {
						L1PcInstance target = (L1PcInstance) _target;
						target.sendPackets(new S_SkillSound(target.getId(), 17229));
						Broadcaster.broadcastPacket(target, new S_SkillSound(target.getId(), 17229));
						target.sendPackets(new S_SkillSound(target.getId(), 4184), true);
						Broadcaster.broadcastPacket(target, new S_SkillSound(target.getId(), 4184), true);
						if (isTargetCalc(target)) {
							if (!target.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND) && !target.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE)
									&& !target.getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH)) {
								target.receiveDamage(_user, ran2, false);
							}
						}
					} else if (_target instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) _target;
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 17229));
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 4184), true);
						npc.receiveDamage(_user, ran2);
					}
				}
			}

			if (_skillId == 데스페라도) { // 데스페라도
				int gfxid = 12758;
				if (_player.데스페라도앱솔루트) {
					gfxid = 17235;
				}

				if (_target instanceof L1PcInstance) { // Gn.89
					L1PcInstance pc = (L1PcInstance) _target;
					pc.sendPackets(new S_SkillSound(pc.getId(), gfxid), true);
					Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), gfxid), true);
				} else if (_target instanceof L1NpcInstance) {
					Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), gfxid), true);
				}
			}

			if (_skillId == SHOCK_STUN || _skillId == MOB_SHOCKSTUN_30 || _skillId == MOB_RANGESTUN_19 || _skillId == MOB_RANGESTUN_18 || _skillId == 엠파이어) {
				if (_targetList.size() == 0) {
					if (_target instanceof L1PcInstance) { // Gn.89
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 4434), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 4434), true);
						// pc.sendPackets(new S_ServerMessage(280), true);
					} else if (_target instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 4434), true);
					}

					return;
				} else {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 4434), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 4434), true);
					} else if (_target instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 4434), true);
					}

					return;
				}
			}

			if (_skillId == FORCE_STUN) {
				if (_targetList.size() == 0) {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 18412), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18412), true);
						// pc.sendPackets(new S_ServerMessage(280), true);
					} else if (_target instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18412), true);
					}

					return;
				} else {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 18412), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18412), true);
					} else if (_target instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18412), true);
					}

					return;
				}
			}

			if (_skillId == DEMOLITION) {
				if (_targetList.size() == 0) {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 18414), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18414), true);
						// pc.sendPackets(new S_ServerMessage(280), true);
					} else if (_target instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18414), true);
					}

					return;
				} else {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 18414), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18414), true);
					} else if (_target instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18414), true);
					}

					return;
				}
			}

			if (_skillId == AVENGER) {
				if (_targetList.size() == 0) {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 18402), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18402), true);
					} else if (_target instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18402), true);
					}

					return;
				} else {
					if (_target instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) _target;
						pc.sendPackets(new S_SkillSound(pc.getId(), 18402), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 18402), true);
					} else if (_target instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18402), true);
					}

					return;
				}
			}

			if (_skillId == LIGHT) {
				L1PcInstance pc = (L1PcInstance) _target;
				pc.sendPackets(new S_Sound(145), true);
			}

			if (_targetList.size() == 0 && !(_skill.getTarget().equals("none"))) {
				int tempchargfx = _player.getGfxId().getTempCharGfx();
				if (tempchargfx == 5727 || tempchargfx == 5730) {
					actionId = ActionCodes.ACTION_SkillBuff;
				} else if (tempchargfx == 5733 || tempchargfx == 5736) {
					actionId = ActionCodes.ACTION_Attack;
				}

				if (isSkillAction && actionId > 0) {
					S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), actionId);
					_player.sendPackets(gfx);
					Broadcaster.broadcastPacket(_player, gfx, true);
				}

				return;
			}

			/* 스킬 액션 처리 Start */
			if (_skill.getTarget().equals("attack") && _skillId != 18) {
				if (isPcSummonPet(_target)) {
					if (CharPosUtil.getZoneType(_player) == 1 || CharPosUtil.getZoneType(_target) == 1 || _player.skillismiss == true
							|| _player.checkNonPvP(_player, _target)) {
						_player.skillismiss = false;
						_player.sendPackets(new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId), true);
						Broadcaster.broadcastPacket(_player, new S_UseAttackSkill(_player, 0, castgfx, _targetX, _targetY, actionId), true);
						return;
					}
				}

				if (_skill.getArea() == 0) {
					_player.sendPackets(new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId), true);
					Broadcaster.broadcastPacket(_player, new S_UseAttackSkill(_player, targetid, castgfx, _targetX, _targetY, actionId), true);
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _player, true);
				} else {
					L1Character[] cha = new L1Character[_targetList.size()];
					int i = 0;
					for (TargetStatus ts : _targetList) {
						cha[i] = ts.getTarget();
						i++;
					}
					_player.sendPackets(new S_RangeSkill(_player, _target, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), true);
					Broadcaster.broadcastPacket(_player, new S_RangeSkill(_player, _target, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), true);
				}
			} else if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK) {
				L1Character[] cha = new L1Character[_targetList.size()];
				int i = 0;
				for (TargetStatus ts : _targetList) {
					cha[i] = ts.getTarget();
					Broadcaster.broadcastPacketExceptTargetSight(cha[i], new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _player, true);
					i++;
				}
				_player.sendPackets(new S_RangeSkill(_player, _target, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), true);
				Broadcaster.broadcastPacket(_player, new S_RangeSkill(_player, _target, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), true);
			} else if (_skill.getTarget().equals("buff") && _skillId == 쉐도우스탭) {
				_player.setHeading(_player.targetDirection(_targetX, _targetY)); // 방향세트
				_player.sendPackets(new S_AttackCritical(_player, targetid, _targetX, _targetY, 104, true));
				Broadcaster.broadcastPacket(_player, new S_AttackCritical(_player, targetid, _targetX, _targetY, 104, true));// 쉐도우스텝
			} else if (_skill.getTarget().equals("buff") && _skillId == PANTERA) {
				if (_player.Pantera_S) {
					_player.setHeading(_player.targetDirection(_targetX, _targetY)); // 방향세트
					_player.sendPackets(new S_AttackCritical(_player, targetid, _targetX, _targetY, 103, true));
					Broadcaster.broadcastPacket(_player, new S_AttackCritical(_player, targetid, _targetX, _targetY, 103, true));// 새로만듬
				} else {
					_player.setHeading(_player.targetDirection(_targetX, _targetY)); // 방향세트
					_player.sendPackets(new S_AttackCritical(_player, targetid, _targetX, _targetY, 102, true));
					Broadcaster.broadcastPacket(_player, new S_AttackCritical(_player, targetid, _targetX, _targetY, 102, true));// 새로만듬
				}
			} else {
				if (_skillId != 5 && _skillId != 69 && _skillId != 131) {
					if (isSkillAction && actionId > 0) {
						S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), _skill.getActionId());
						// 시전자 스킬 사용 이펙트 표시
						_player.sendPackets(gfx);
						// 다른 사람에게 시전자 스킬 사용 이펙트 표시
						Broadcaster.broadcastPacket(_player, gfx, true);
					}

					if (_skillId == INVISIBILITY) {
						S_Sound ss = new S_Sound(147);
						_player.sendPackets(ss);
						Broadcaster.broadcastPacket(_player, ss, true);
					} else if (_skillId == TRUE_TARGET) {
						// 트루타켓
						return;
					} else {
						if (_skillId == COUNTER_BARRIER) {
							if (_player.카운터배리어베테랑) {
								castgfx = 18965;
							}
						}
						if (_skillId == REDUCTION_ARMOR) {
							if (_player.리덕션아머베테랑) {
								castgfx = 18966;
							}
						}

						if (_skillId == DOUBLE_BRAKE) {
							if (_player.더블브레이크데스티니) {
								castgfx = 17224;
							}
						}

						if (_skillId == ARMOR_BREAK) {
							if (_player.아머브레이크데스티니) {
								castgfx = 17226;
							}
						}

						// 각 상태별로 스킬 사운트 출력 및 타이머 설정 by white
						if (isCheckedPartyApplySkill()) {
							/* 샤이닝 실드는 여기서 처리하면 표시된 버프 아이콘이 사라져 제외 */
							// 파티 적용 스킬
							if (_player.isInParty()) {
								for (L1PcInstance member : _player.getParty().getMembers()) {
									if (_skillId != SHINING_AURA) {
										member.sendPackets(new S_SkillSound(member.getId(), castgfx), true);
										member.broadcastPacket(new S_SkillSound(member.getId(), castgfx));
									}
								}
							} else {
								if (_skillId != SHINING_AURA) {
									// 파티가 없으면 시전자만
									_player.sendPackets(new S_SkillSound(targetid, castgfx), true);
									_player.broadcastPacket(new S_SkillSound(targetid, castgfx), true);
								}
							}
						} else if (isCheckedClanApplySkill()) {
							// 혈맹 적용 스킬
							if (_player.getClanid() != 0) {
								for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(_player, 13)) {
									if (_player.getClanid() == member.getClanid()) {
										member.sendPackets(new S_SkillSound(member.getId(), castgfx), true);
										member.broadcastPacket(new S_SkillSound(member.getId(), castgfx));
									}
								}
							}
							
							// 혈맹이 없거나 시전자는 getVisiblePlayer() 결과 리스트에 미포함이므로 별도 처리
							_player.sendPackets(new S_SkillSound(targetid, castgfx), true);
							Broadcaster.broadcastPacket(_player, new S_SkillSound(targetid, castgfx), true);
						} else {
							if(_skillId == 디스트로이) {
								if(_player.isSkillMastery(L1SkillId.FEAR)) {
									_player.sendPackets(new S_SkillSound(targetid, 18959), true);
									Broadcaster.broadcastPacket(_player, new S_SkillSound(targetid, 18959), true);
								}
								if(_player.isSkillMastery(L1SkillId.HORROR_OF_DEATH)) {
									_player.sendPackets(new S_SkillSound(targetid, 18961), true);
									Broadcaster.broadcastPacket(_player, new S_SkillSound(targetid, 18961), true);
								}
							}
							
							// 개인 적용 스킬
							_player.sendPackets(new S_SkillSound(targetid, castgfx), true);
							Broadcaster.broadcastPacket(_player, new S_SkillSound(targetid, castgfx), true);
						}
						// 스킬 사운드 출력 처리 End

						if (_skillId == GLOWING_AURA) {
							_player.sendPackets(new S_SkillIconAura(113, _getBuffIconDuration), true);
						} else if (_skillId == BRAVE_AURA) {
							_player.sendPackets(new S_NewSkillIcons(L1SkillId.BRAVE_AURA, true, _getBuffIconDuration), true);
						} else if (_skillId == CANCELLATION) {
							L1Object tempObj = L1World.getInstance().findObject(targetid);

							if (tempObj != null && tempObj instanceof L1PcInstance) {
								L1PcInstance tempPlayer = (L1PcInstance) tempObj;

								if (tempPlayer.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_DRAGONPERL)) {
									int reminingtime = tempPlayer.getSkillEffectTimerSet().getSkillEffectTimeSec(L1SkillId.STATUS_DRAGONPERL);
									tempPlayer.sendPackets(new S_PacketBox(S_PacketBox.DRAGONPERL, 8, (reminingtime / 4) - 2), true);
								}

								for (int skillNum = COOKING_BEGIN; skillNum <= COOKING_END; skillNum++) {
									if (tempPlayer.getSkillEffectTimerSet().hasSkillEffect(skillNum)) {
										int cookingType = 0;
										boolean ck = false;

										switch (skillNum) {
										case COOKING_1_0_N:// 괴물눈 스테이크
										case COOKING_1_0_S:
											cookingType = 0;
											break;

										case COOKING_1_1_N:// 곰고기 구이
										case COOKING_1_1_S:
											cookingType = 1;
											break;

										case COOKING_1_2_N:// 씨호떡
										case COOKING_1_2_S:
											cookingType = 2;
											break;

										case COOKING_1_3_N:// 개미다리치즈구이
										case COOKING_1_3_S:
											cookingType = 3;
											break;

										case COOKING_1_4_N:// 과일샐러드
										case COOKING_1_4_S:
											cookingType = 4;
											break;

										case COOKING_1_5_N:// 과일탕수육
										case COOKING_1_5_S:
											cookingType = 5;
											break;

										case COOKING_1_6_N:// 멧돼지 꼬치 구이
										case COOKING_1_6_S:
											cookingType = 6;
											break;

										case COOKING_1_7_N:// 버섯스프
										case COOKING_1_7_S:
											cookingType = 7;
											break;

										/** 1차요리 효과끝 */
										case COOKING_1_8_N:// 캐비어카나페
										case COOKING_1_8_S:
											cookingType = 16;
											break;

										case COOKING_1_9_N:// 악어스테이크
										case COOKING_1_9_S:
											cookingType = 17;
											break;

										case COOKING_1_10_N:// 터틀드래곤과자
										case COOKING_1_10_S:
											cookingType = 18;
											break;

										case COOKING_1_11_N:// 키위패롯구이
										case COOKING_1_11_S:
											cookingType = 19;
											break;

										case COOKING_1_12_N:// 스콜피온구이
										case COOKING_1_12_S:
											cookingType = 20;
											break;

										case COOKING_1_13_N:// 일렉카둠스튜
										case COOKING_1_13_S:
											cookingType = 21;
											break;

										case COOKING_1_14_N:// 거미다리꼬치구이
										case COOKING_1_14_S:
											cookingType = 22;
											break;

										case COOKING_1_15_N:// 크랩살스프
										case COOKING_1_15_S:
											cookingType = 23;
											break;

										/** 2차요리 효과끝 */
										case COOKING_1_16_N:// 시안집게발구이
										case COOKING_1_16_S:
											cookingType = 45;
											break;

										case COOKING_1_17_N:// 그리폰구이
										case COOKING_1_17_S:
											cookingType = 46;
											break;

										case COOKING_1_18_N:// 코카스테이크
										case COOKING_1_18_S:
											cookingType = 47;
											break;

										case COOKING_1_19_N:// 대왕거북구이
										case COOKING_1_19_S:
											cookingType = 48;
											break;

										case COOKING_1_20_N:// 레서날개꼬치
										case COOKING_1_20_S:
											cookingType = 49;
											break;

										case COOKING_1_21_N:// 드레이크구이
										case COOKING_1_21_S:
											cookingType = 50;
											break;

										case COOKING_1_22_N:// 심해어스튜
										case COOKING_1_22_S:
											cookingType = 51;
											break;

										case COOKING_1_23_N:// 바실스프
										case COOKING_1_23_S:
											cookingType = 52;
											break;

										default:
											ck = true;
											break;
										}

										if (!ck) {
											int time = tempPlayer.getSkillEffectTimerSet().getSkillEffectTimeSec(skillNum);
											tempPlayer.sendPackets(new S_PacketBox(53, cookingType, time));
										}
									}
								}

								for (int skillNum = 3077; skillNum <= 3080; skillNum++) {
									if (tempPlayer.getSkillEffectTimerSet().hasSkillEffect(skillNum)) {
										int time = tempPlayer.getSkillEffectTimerSet().getSkillEffectTimeSec(skillNum);
										tempPlayer.getSkillEffectTimerSet().removeSkillEffect(skillNum);
										L1Cooking.newEatCooking(tempPlayer, skillNum, time);
									}
								}

								for (L1ItemInstance item : tempPlayer.getInventory().getItems()) {
									if (item != null && item.isEquipped() && item.getItem().isHasteItem()) {
										if (tempPlayer.getMoveState().getMoveSpeed() != 1) {
											tempPlayer.getMoveState().setMoveSpeed(1);
											tempPlayer.sendPackets(new S_SkillHaste(tempPlayer.getId(), 1, -1));
											Broadcaster.broadcastPacket(tempPlayer, new S_SkillHaste(tempPlayer.getId(), 1, 0));
										}

										break;
									}
								}
							}
						}
					}
				}

				for (TargetStatus ts : _targetList) {
					L1Character cha = ts.getTarget();
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						S_OwnCharStatus so = new S_OwnCharStatus(pc);
						pc.sendPackets(so, true);
					}
				}
			}
			/* 스킬 액션 처리 End */

			if (_skillId == JOY_OF_PAIN || _skillId == DECREASE_WEIGHT || _skillId == COUNTER_MIRROR) {
				_player.sendPackets(new S_NewCreateItem("무게", _player));
			}

		} else if (_user instanceof L1NpcInstance) {
			int targetid = _target.getId();

			if (_user instanceof L1MerchantInstance) {
				Broadcaster.broadcastPacket(_user, new S_SkillSound(targetid, castgfx), true);
				return;
			}

			if (_targetList.size() == 0 && !(_skill.getTarget().equals("none"))) {
				S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
				Broadcaster.broadcastPacket(_user, gfx, true);
				return;
			}

			if (_skill.getTarget().equals("attack") && _skillId == 40046) {
				GeneralThreadPool.getInstance().execute(new TargetLineSkillEffect(_user, _target, _skill.getCastGfx()));
				S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
				Broadcaster.broadcastPacket(_user, gfx, true);
				return;
			}

			if (_skill.getTarget().equals("attack") && _skillId != 18) {
				if (_skill.getArea() == 0) {
					if (castgfx == 0) {
						Broadcaster.broadcastPacket(_user, new S_DoActionGFX(_user.getId(), actionId), true);
					} else {
						Broadcaster.broadcastPacket(_user, new S_UseAttackSkill(_user, targetid, castgfx, _targetX, _targetY, actionId), true);
					}

					// TODO 아래 코드가 현재 위치인지 else 안으로 이동인지 확인 필요
					Broadcaster.broadcastPacketExceptTargetSight(_target, new S_DoActionGFX(targetid, ActionCodes.ACTION_Damage), _user, true);
				} else {
					L1Character[] cha = new L1Character[_targetList.size()];
					int i = 0;
					for (TargetStatus ts : _targetList) {
						cha[i] = ts.getTarget();
						Broadcaster.broadcastPacketExceptTargetSight(cha[i], new S_DoActionGFX(cha[i].getId(), ActionCodes.ACTION_Damage), _user, true);
						i++;
					}

					if (castgfx == 0) {
						Broadcaster.broadcastPacket(_user, new S_DoActionGFX(_user.getId(), actionId), true);
						for (L1Character cc : cha) {
							if (cc instanceof L1PcInstance) {
								L1PcInstance pp = (L1PcInstance) cc;
								pp.sendPackets(new S_DoActionGFX(pp.getId(), ActionCodes.ACTION_Damage), true);
							}
						}
					} else {
						Broadcaster.broadcastPacket(_user, new S_RangeSkill(_user, _target, cha, castgfx, actionId, S_RangeSkill.TYPE_DIR), true);
					}
				}
			} else if (_skill.getTarget().equals("none") && _skill.getType() == L1Skills.TYPE_ATTACK) {
				L1Character[] cha = new L1Character[_targetList.size()];
				int i = 0;
				for (TargetStatus ts : _targetList) {
					cha[i] = ts.getTarget();
					i++;
				}

				if (castgfx == 7987) {
					int x = _user.getX();
					int y = _user.getY();
					if (_user.getX() == 32862 && _user.getY() == 32861) {
						castgfx = 8050;
						x = _user.getX() - 5;
						y = _user.getY() + 5;
					} else if (_user.getX() == 32868 && _user.getY() == 32875) {
						castgfx = 8051;
						x = _user.getX() - 5;
						y = _user.getY();
					} else {
						x = _user.getX();
						y = _user.getY() + 5;
					}

					Broadcaster.broadcastPacket(_user, new S_RangeSkill(_user, _target, x, y, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), true);
				} else if (castgfx == 16258 && ((L1NpcInstance) _user).getNpcTemplate().get_npcId() == 45516) {
					int oriheading = _user.getMoveState().getHeading();
					_user.getMoveState().setHeading(1);
					Broadcaster.broadcastPacket(_user, new S_RangeSkill(_user, _target, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), true);
					_user.getMoveState().setHeading(oriheading);
					Broadcaster.broadcastPacket(_user, new S_ChangeHeading(_user), true);
				} else {
					if (castgfx == 0) {
						Broadcaster.broadcastPacket(_user, new S_DoActionGFX(_user.getId(), actionId), true);
						for (L1Character cc : cha) {
							if (cc instanceof L1PcInstance) {
								L1PcInstance pp = (L1PcInstance) cc;
								pp.sendPackets(new S_DoActionGFX(pp.getId(), ActionCodes.ACTION_Damage), true);
								Broadcaster.broadcastPacketExceptTargetSight(pp, new S_DoActionGFX(pp.getId(), ActionCodes.ACTION_Damage), _user, true);
							}
						}
					} else {
						Broadcaster.broadcastPacket(_user, new S_RangeSkill(_user, _target, cha, castgfx, actionId, S_RangeSkill.TYPE_NODIR), true);
					}
				}
			} else {
				if (_skillId != 5 && _skillId != 69 && _skillId != 131) {
					S_DoActionGFX gfx = new S_DoActionGFX(_user.getId(), _skill.getActionId());
					Broadcaster.broadcastPacket(_user, gfx, true);
					if (castgfx != 0) {
						Broadcaster.broadcastPacket(_user, new S_SkillSound(targetid, castgfx), true);
					}
				}
			}
		}
	}

	private static final int[][] repeatedSkills = { { FIRE_WEAPON, WIND_SHOT, STORM_EYE, BURNING_WEAPON, STORM_SHOT, 블로우어택 }, { SHIELD, IRON_SKIN },
			{ HOLY_WALK, BLOOD_LUST, MOVING_ACCELERATION, 샌드스톰, 허리케인, STATUS_FRUIT, STATUS_BRAVE, STATUS_ELFBRAVE, FIRE_BLESS, 포커스웨이브 },
			{ HASTE, GREATER_HASTE, STATUS_HASTE }, { 프라이드, 기간틱, ADVANCE_SPIRIT }, { PHYSICAL_ENCHANT_DEX, DRESS_DEXTERITY },
			{ PHYSICAL_ENCHANT_STR, DRESS_MIGHTY }, { FEATHER_BUFF_A, FEATHER_BUFF_B, FEATHER_BUFF_C, FEATHER_BUFF_D },
			{ FAFU_MAAN, ANTA_MAAN, LIND_MAAN, VALA_MAAN, LIFE_MAAN, BIRTH_MAAN, SHAPE_MAAN, HALPAS_MAAN, NEVER_MAAN } };

	private void deleteRepeatedSkills(L1Character cha) {
		for (int[] skills : repeatedSkills) {
			for (int id : skills) {
				if (id == _skillId) {
					stopSkillList(cha, skills);
				}
			}
		}
	}

	private void stopSkillList(L1Character cha, int[] repeat_skill) {
		for (int skillId : repeat_skill) {
			if (skillId != _skillId) {
				cha.getSkillEffectTimerSet().removeSkillEffect(skillId);
			}
		}
	}

	private void setDelay() {
		if (_skill.getReuseDelay() > 0) {
			_user.setSkillDelay(true);
			GeneralThreadPool.getInstance().schedule(new L1SkillDelay(_user, _skill.getReuseDelay()), _skill.getReuseDelay());
		}
	}

	private static final int[] BowGFX = { 138, 37, 3860, 3126, 3420, 2284, 3105, 3145, 3148, 3151, 3871, 4125, 2323, 3892, 3895, 3898, 3901, 4917, 4918, 4919,
			4950, 10870, 10869, 10871, 6140, 6145, 6150, 6155, 6269, 6272, 6275, 6278, 6826, 6827, 6836, 6837, 6846, 6847, 6856, 6857, 6866, 6867, 6876, 6877,
			6886, 6887, 6400, 5645, 6399, 7039, 7040, 7041, 7140, 7144, 7148, 6160, 7152, 7156, 7160, 7164, 7139, 7143, 7147, 7151, 7155, 7159, 7163, 7959,
			7967, 7969, 7970, 9214, 8900, 8913, 9225, 9226, 8804, 8808, 8792, 8798, 8786, 8860, 8561, 8562, 8719, 8112, 11498, 13140, 13388, 13380, 11122,
			11126, 11331, 11342, 11352, 11353, 11362, 11363, 11369, 11378, 11382, 11386, 11390, 11394, 11402, 11406, 11412, 11413, 11398, 10874, 7967, 7846,
			7848, 8719, 13631, 13635, 13723, 13725, 13346, 9615, 14927, 12314, 15848, 15814, 15528, 15830, 15831, 16002, 16074, 17535 };

	private static final int[] FouGFX = { 138, 37, 3860, 3126, 3420, 2284, 3105, 3145, 3148, 3151, 3871, 4125, 2323, 3892, 3895, 3898, 3901, 4917, 4918, 4919,
			4950, 6140, 6145, 6150, 6155, 6269, 6272, 6275, 6278, 6826, 6827, 6836, 6837, 6846, 6847, 6856, 6857, 6866, 6867, 6876, 6877, 6886, 6887, 6400,
			5645, 6399, 7039, 7040, 7041, 7140, 7144, 7148, 6160, 13140, 13389, 7959, 7967, 7969, 7970, 9214, 8900, 8913, 9225, 9226, 8804, 8808, 13380, 8792,
			8798, 8786, 8860, 9362, 9363, 9364, 9365, 4769, 16008, 16056, 17541, 17545, 17549, 17515, 17531 };

	private void runSkill() {
		if (_skillId == LIFE_STREAM) {
			L1EffectSpawn.getInstance().spawnEffect(81169, _skill.getBuffDuration() * 1000, _targetX, _targetY, _user.getMapId());
			return;
		}

		if (_skillId == FIRE_WALL) {
			L1EffectSpawn.getInstance().doSpawnFireWall(_user, _targetX, _targetY);
			return;
		}

		if (_skillId == 40048) { // 독구름
			L1EffectSpawn.getInstance().doSpawnPoisonClude(_user, _targetX, _targetY);
			return;
		} else if (_skillId == 40052) { // 에르자베 약한 독구름
			L1EffectSpawn.getInstance().doSpawnPoisonClude(_user, _targetX, _targetY, true);
			return;
		} else if (_skillId == 40055) { // 안타 전방 독구름
			L1EffectSpawn.getInstance().doSpawnAntaBreathPoisonClude(_user, _targetX, _targetY);
			return;
		}

		for (int skillId : EXCEPT_COUNTER_MAGIC) {
			if (_skillId == skillId) {
				_isCounterMagic = false;
				break;
			}
		}

		if (_skillId == 엠파이어 || _skillId == SHOCK_STUN || _skillId == FORCE_STUN /** || _skillId == BONE_BREAK || _skillId == AM_BREAK || _skillId == SMASH */
				&& _user instanceof L1PcInstance) {
			if (_target != _user) {
				_target.onAction(_player);
			}
		}

		if (!isTargetCalc(_target)) {
			return;
		}

		try {
			TargetStatus ts = null;
			L1Character cha = null;
			int dmg = 0;
			int drainMana = 0;
			int heal = 0;
			boolean isSuccess = false;
			int undeadType = 0;

			for (Iterator<TargetStatus> iter = _targetList.iterator(); iter.hasNext();) {
				ts = null;
				cha = null;
				dmg = 0;
				heal = 0;
				isSuccess = false;
				undeadType = 0;

				ts = iter.next();
				cha = ts.getTarget();
				if (!ts.isCalc() || !isTargetCalc(cha)) {
					continue;
				}

				L1Magic _magic = new L1Magic(_user, cha);
				_magic.setLeverage(getLeverage());

				if (cha instanceof L1MonsterInstance) {
					undeadType = ((L1MonsterInstance) cha).getNpcTemplate().get_undead();
				}

				if ((_skill.getType() == L1Skills.TYPE_CURSE || _skill.getType() == L1Skills.TYPE_PROBABILITY) && isTargetFailure(cha)) {
					iter.remove();
					continue;
				}

				if (cha instanceof L1PcInstance) {
					if (_skillTime == 0) {
						_getBuffIconDuration = _skill.getBuffDuration();
					} else {
						_getBuffIconDuration = _skillTime;
					}
				}

				// 사용한 스킬이 반복 스킬 리스트에 포함된 경우면 타이머 삭제
				deleteRepeatedSkills(cha);

				if (_skill.getType() == L1Skills.TYPE_ATTACK && _user.getId() != cha.getId()) {
					if (isUseCounterMagic(cha)) {
						iter.remove();
						continue;
					}

					dmg = _magic.calcMagicDamage(_skillId);
					if (cha instanceof L1ScarecrowInstance) {
						if (_player.getLevel() < 5) {
							ArrayList<L1PcInstance> targetList = new ArrayList<L1PcInstance>();
							targetList.add(_player);
							ArrayList<Integer> hateList = new ArrayList<Integer>();
							hateList.add(1);
							CalcExp.calcExp(_player, cha.getId(), targetList, hateList, cha.getExp());
						}

						int heading = cha.getMoveState().getHeading();

						if (heading < 7) {
							heading++;
						} else {
							heading = 0;
						}

						cha.getMoveState().setHeading(heading);
						S_ChangeHeading ch = new S_ChangeHeading(cha);
						Broadcaster.broadcastPacket(cha, ch, true);
					}

					if (_skill.getSkillId() != L1SkillId.TRIPLE_ARROW && _skill.getSkillId() != L1SkillId.FOU_SLAYER) {
						if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC)) {
							cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
						}
					}
				} else if (_skill.getType() == L1Skills.TYPE_CURSE || _skill.getType() == L1Skills.TYPE_PROBABILITY) {
					isSuccess = _magic.calcProbabilityMagic(_skillId);
					// 이레 마법이 아니고 현제 이레중이라면!!!
					if (_skillId != ERASE_MAGIC && _skillId != EARTH_BIND) {
						if (cha.getSkillEffectTimerSet().hasSkillEffect(ERASE_MAGIC)) {
							cha.getSkillEffectTimerSet().removeSkillEffect(ERASE_MAGIC);
						}
					}

					if (_skillId != FOG_OF_SLEEPING) {
						cha.getSkillEffectTimerSet().removeSkillEffect(FOG_OF_SLEEPING);
					}

					if (_skillId != PHANTASM) {
						cha.getSkillEffectTimerSet().removeSkillEffect(PHANTASM);
					}

					if (isSuccess) {
						if (isUseCounterMagic(cha)) {
							iter.remove();
							continue;
						}
					} else {
						if (_skillId == FOG_OF_SLEEPING && cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_ServerMessage(297));
						}

						if (_skillId == PHANTOM) {
							S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
							_player.sendPackets(gfx);
							Broadcaster.broadcastPacket(_player, gfx, true);
							_player.sendPackets(new S_SkillSound(_target.getId(), 18578), true);
							Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18578), true);
						}

						if (_skillId == PANTERA) {
							if (_player.Pantera_S) {
								_player.setHeading(_player.targetDirection(_targetX, _targetY)); // 방향세트
								_player.sendPackets(new S_AttackCritical(_player, _target.getId(), _targetX, _targetY, 103, true));
								Broadcaster.broadcastPacket(_player, new S_AttackCritical(_player, _target.getId(), _targetX, _targetY, 103, true));// 새로만듬
								L1Location loc = L1Teleport.Pantera(_player, _target, 1);
								_target.dx = loc.getX();
								_target.dy = loc.getY();
								_target.dm = (short) loc.getMapId();
								_player.dh = _player.getMoveState().getHeading();
								L1Teleport.teleport(_player, _target.dx, _target.dy, _target.dm, _player.dh, false, false, 0);
							} else {
								_player.setHeading(_player.targetDirection(_targetX, _targetY)); // 방향세트
								_player.sendPackets(new S_AttackCritical(_player, _target.getId(), _targetX, _targetY, 102, true));
								Broadcaster.broadcastPacket(_player, new S_AttackCritical(_player, _target.getId(), _targetX, _targetY, 102, true));// 새로만듬
								L1Location loc = L1Teleport.Pantera(_player, _target, 1);
								_target.dx = loc.getX();
								_target.dy = loc.getY();
								_target.dm = (short) loc.getMapId();
								_player.dh = _player.getMoveState().getHeading();
								L1Teleport.teleport(_player, _target.dx, _target.dy, _target.dm, _player.dh, false, false, 0);
							}
						}
						
						if (_skillId == 쉐도우스탭) {
								_player.setHeading(_player.targetDirection(_targetX, _targetY)); // 방향세트
								_player.sendPackets(new S_AttackCritical(_player, _target.getId(), _targetX, _targetY, 104, true));
								Broadcaster.broadcastPacket(_player, new S_AttackCritical(_player, _target.getId(), _targetX, _targetY, 104, true));// 새로만듬
								L1Location loc = L1Teleport.ShadowStep(_player, _target, 1);
								_target.dx = loc.getX();
								_target.dy = loc.getY();
								_target.dm = (short) loc.getMapId();
								_player.dh = _player.getMoveState().getHeading();
								L1Teleport.teleport(_player, _target.dx, _target.dy, _target.dm, _player.dh, false, false, 0);
							}

						iter.remove();
						continue;
					}
				} else if (_skill.getType() == L1Skills.TYPE_HEAL) {
					if (cha.getSkillEffectTimerSet().hasSkillEffect(10518) || cha.getSkillEffectTimerSet().hasSkillEffect(데스힐) || cha.isPhantomDeathed()) {
						dmg = _magic.calcHealing(_skillId);
					} else {
						dmg = -1 * _magic.calcHealing(_skillId);
						if (USE_SPELLSC) {
							if (_user instanceof L1PcInstance) {
								L1PcInstance up = (L1PcInstance) _user;
								if (!up.isWizard()) {
									if (up.isElf()) {
										dmg *= 0.8;
									} else if (up.isIllusionist()) {
										dmg *= 0.7;
									} else {
										dmg *= 0.4;
									}
								}
							}
						}
					}

					if (cha.getSkillEffectTimerSet().hasSkillEffect(WATER_LIFE)) {
						dmg *= 2;
					}

					if (cha.getSkillEffectTimerSet().hasSkillEffect(POLLUTE_WATER) || // /파푸 리듀스힐
							cha.getSkillEffectTimerSet().hasSkillEffect(10517)) {
						dmg /= 2;
					}
				}
				
				// 스킬 타이머 등록 확인 및 스킬 확인
				try {
					if (cha.getSkillEffectTimerSet().hasSkillEffect(_skillId) && _skillId != 토마호크 && _skillId != BONE_BREAK && _skillId != 파워그립
							&& _skillId != 데스페라도 && _skillId != SHOCK_STUN && _skillId != THUNDER_GRAB && _skillId != 데스힐 && _skillId != 앱솔루트블레이드
							&& _skillId != 소울배리어 && _skillId != 라이징 && _skillId != 엠파이어 && _skillId != 임팩트 && _skillId != FORCE_STUN
							&& _skillId != DEMOLITION && _skillId != ETERNITY && _skillId != 쉐도우스탭 && _skillId != PANTERA && _skillId != PHANTOM) {

						addMagicList(cha, true);

						if (_skillId != SHAPE_CHANGE) {
							continue;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				
				// ●●●● PC, NPC 양쪽 모두 효과가 있는 스킬 ●●●●
				// GFX Check (Made by HuntBoy)
				switch (_skillId) {
				/* 군주 스킬 처리 START */
				case 그레이스아바타: { // 스킬 수정 by white
					L1PcInstance pc = (L1PcInstance) cha;

					// useSkillGrace(pc);

					if (pc.isInParty()) {
						for (L1PcInstance paty : pc.getParty().getMembers()) {
							if (pc.getLocation().getTileLineDistance(paty.getLocation()) <= 14) {
								if (paty.isDead()) {
									continue;
								}

								int c = 5;// 기본확률
								if (paty.getLevel() > 80) {
									c += (paty.getLevel() - 80);
								}

								if (c > 10) {// 최대확률
									c = 10;
								}

								// 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제
								paty.그레이스아바타 = c;
								// 임팩트의 대한 적중 효과 적용
								paty.addAllTolerance(paty.그레이스아바타);
								paty.sendPackets(new S_NewSkillIcons(L1SkillId.그레이스아바타, true, 60));
							}
						}
					} else {
						// 파티원이 없다면 자기만 버프효과
						int c = 5;// 기본확률
						if (pc.getLevel() > 80) {
							c += (pc.getLevel() - 80);
						}

						// 최대확률
						if (c > 10) {
							c = 10;
						}
						pc.그레이스아바타 = c;
						pc.addAllTolerance(pc.그레이스아바타);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.그레이스아바타, true, 60));
					}

				}
					break;

				case PRIME: { // 스킬 수정 by white
					L1PcInstance pc = (L1PcInstance) cha;

					// 프라임 시전 시 처리
					// useSkillPrime(pc);

					// 길드가 존재하면
					if (pc.getClanid() != 0) {
						// 시전자 화면에 보여지는 유저 확인
						for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 13)) {
							if (pc.getClanid() == member.getClanid()) {
								if (member.isDead()) {
									continue;
								}

								member.addDmgup(3);
								member.addHitup(3);
								member.addBowDmgup(3);
								member.addBowHitup(3);
								member.getAbility().addSp(2);
								member.프라임 = 5;
								member.addTechniqueHit(pc.프라임);
								// 프라임 스킬 버프 효과 적용
								member.sendPackets(new S_SPMR(member), true);
								member.sendPackets(new S_SystemMessage("프라임 : 근거리 대미지/명중 +3, 원거리 대미지/명중 +3, sp+2, 마법명중+2."));
								// 프라임 효과 아이콘 표시
								member.sendPackets(new S_NewSkillIcons(L1SkillId.PRIME, true, _getBuffIconDuration), true);
							}
						}
					}

					// 시전자 적용
					pc.addDmgup(3);
					pc.addHitup(3);
					pc.addBowDmgup(3);
					pc.addBowHitup(3);
					pc.getAbility().addSp(2);
					pc.프라임 = 5;
					pc.addTechniqueHit(pc.프라임);
					// 프라임 스킬 버프 효과 적용
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_SystemMessage("프라임 : 근거리 대미지/명중 +3, 원거리 대미지/명중 +3, sp+2, 마법명중+2."));
					// 프라임 효과 아이콘 표시
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.PRIME, true, _getBuffIconDuration), true);

				}
					break;
				/* 군주 스킬 END */

				case MIRROR_IMAGE:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDg(30);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
					break;

				case UNCANNY_DODGE:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDg(30);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
					break;

				case SCALES_Lind_DRAGON:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDg(7);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.SCALES_Lind_DRAGON, true, 600));
						pc.sendPackets(new S_SystemMessage("각성-풍룡의 결속이 느껴집니다."));
					}
					break;

				case AREA_OF_SILENCE:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						int attackLevel = _user.getLevel();
						int defenseLevel = cha.getLevel();
						int probability = 0;
						if (attackLevel >= defenseLevel) {
							probability = (int) ((attackLevel - defenseLevel) * 5) + 50;
						} else if (attackLevel < defenseLevel) {
							probability = (int) ((attackLevel - defenseLevel) * 6) + 50;
						}

						if (probability > 90) {
							probability = 90;
						}

						if (random.nextInt(100) < probability) {
							if (_user != cha) {
								pc.sendPackets(new S_SkillSound(cha.getId(), 10708), true);
								Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), 10708), true);
							}
						} else {
							continue;
						}
					}
					break;

				case DRAGONBLOOD_A: {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGONBLOOD_A)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGONBLOOD_A);
					}
					pc.getResistance().addWater(50);
					pc.getAC().addAc(-2);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 82, _getBuffIconDuration / 60), true);
					pc.sendPackets(new S_SystemMessage("안타라스의 혈흔에 의해 강해진 느낌이 듭니다. 방어력 +2만큼 상승 효과가 있습니다."), true);
				}
					break;

				case DRAGONBLOOD_P: {

					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGONBLOOD_P)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGONBLOOD_P);
					}
					pc.getResistance().addWind(50);
					pc.addHpr(3);
					pc.addMpr(1);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, _getBuffIconDuration / 60), true);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_SystemMessage("파푸리온의 혈흔에 의해 강해진 느낌이 듭니다. HP 회복율 +3, MP 회복율 +1만큼 상승 효과가 있습니다."), true);
				}
					break;

				case DRAGONBLOOD_L: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DRAGONBLOOD_L)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DRAGONBLOOD_L);
						}
						pc.getAbility().addSp(1);
						pc.sendPackets(new S_SPMR(pc), true);
						pc.sendPackets(new S_PacketBox(S_PacketBox.DRAGONBLOOD, 88, _getBuffIconDuration / 60), true);
						pc.sendPackets(new S_SystemMessage("린드비오르의 혈흔에 의해 강해진 느낌이 듭니다. 주술력 +1, 마법치명타 +1%만큼 상승 효과가 있습니다."), true);
					}
				}
					break;

				case HEUKSA: {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.HEUKSA)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.HEUKSA);
					}
					pc.addMaxHp(20);
					pc.addMaxMp(13);
					pc.getAC().addAc(-2);
				}
					break;

				case miso1: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
							pc.addDmgup(3);
							pc.addBowDmgup(3);
							pc.getAbility().addSp(3);
							pc.addMaxMp(50);
							pc.addMpr(2);
							pc.sendPackets(new S_SPMR(pc));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.sendPackets(new S_MaanIcons(L1SkillId.miso1, true, _getBuffIconDuration));
						}
					}
					break;
					
				case miso2: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
							pc.addMaxHp(100);
							pc.getResistance().addMr(10);
							pc.addHpr(2);
							pc.sendPackets(new S_SPMR(pc));
							pc.sendPackets(new S_OwnCharAttrDef(pc));
							pc.sendPackets(new S_MaanIcons(L1SkillId.miso2, true, _getBuffIconDuration));
						}
					}
				break;
				
				case miso3: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_MaanIcons(L1SkillId.miso3, true, _getBuffIconDuration));
						}
					}
					break;
					
				case HASTE:
				case 40028: {
					if (cha.getMoveState().getMoveSpeed() != 2) {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							if (pc.getHasteItemEquipped() > 0) {
								continue;
							}

							if (pc.getSkillEffectTimerSet().hasSkillEffect(HASTE)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(HASTE);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
								Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
								pc.getMoveState().setMoveSpeed(0);
							} else if (pc.getSkillEffectTimerSet().hasSkillEffect(GREATER_HASTE)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(GREATER_HASTE);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
								Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
								pc.getMoveState().setMoveSpeed(0);
							} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_HASTE)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_HASTE);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
								Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
								pc.getMoveState().setMoveSpeed(0);
							}

							pc.setDrink(false);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration), true);
						}

						Broadcaster.broadcastPacket(cha, new S_SkillHaste(cha.getId(), 1, 0), true);
						cha.getMoveState().setMoveSpeed(1);
					} else {
						int skillNum = 0;
						if (cha.getSkillEffectTimerSet().hasSkillEffect(SLOW)) {
							skillNum = SLOW;
						} else if (cha.getSkillEffectTimerSet().hasSkillEffect(MOB_SLOW_1)) {
							skillNum = MOB_SLOW_1;
						} else if (cha.getSkillEffectTimerSet().hasSkillEffect(MOB_SLOW_18)) {
							skillNum = MOB_SLOW_18;
						}

						if (skillNum != 0) {
							cha.getSkillEffectTimerSet().removeSkillEffect(skillNum);
							cha.getSkillEffectTimerSet().removeSkillEffect(_skillId);// HASTE
							cha.getMoveState().setMoveSpeed(0);
							continue;
						}
					}
				}
					break;

				case CURE_POISON: {
					cha.curePoison();
				}
					break;

				// ///// 크레이버프 추가 //////////
				case 사엘: {
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.크레이)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.크레이);
					}
					pc.addMaxHp(100);
					pc.addMaxMp(50);
					pc.addHpr(3);
					pc.addMpr(3);
					pc.getResistance().addWater(30);
					pc.addDmgup(1);
					pc.addBowDmgup(1);
					pc.addHitup(5);
					pc.addBowHitup(5);
					pc.addWeightReduction(40);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					pc.sendPackets(new S_OwnCharStatus(pc), true);
				}
					break;

				case 크레이: {// 크레이 버프
					L1PcInstance pc = (L1PcInstance) cha;
					if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.사엘)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.사엘);
					}
					pc.getAC().addAc(-8);
					pc.getResistance().addMr(20);
					pc.addMaxHp(200);
					pc.addMaxMp(100);
					pc.addHpr(3);
					pc.addMpr(3);
					pc.getResistance().addEarth(30);
					pc.addDmgup(3);
					pc.addBowDmgup(3);
					pc.addHitup(10);
					pc.addBowHitup(10);
					pc.addWeightReduction(40);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()));
					pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()));
					pc.sendPackets(new S_OwnCharStatus(pc));
				}
					break;

				// ////////////// 크레이버프 추가 //////////////
				case REMOVE_CURSE: {
					cha.curePoison();
					if (cha.getSkillEffectTimerSet().hasSkillEffect(STATUS_CURSE_PARALYZING)
							|| cha.getSkillEffectTimerSet().hasSkillEffect(STATUS_CURSE_PARALYZED)) {
						cha.cureParalaysis();
					}

					if (cha.getSkillEffectTimerSet().hasSkillEffect(CURSE_BLIND) || cha.getSkillEffectTimerSet().hasSkillEffect(DARKNESS)) {
						if (cha.getSkillEffectTimerSet().hasSkillEffect(CURSE_BLIND)) {
							cha.getSkillEffectTimerSet().removeSkillEffect(CURSE_BLIND);
						} else if (cha.getSkillEffectTimerSet().hasSkillEffect(DARKNESS)) {
							cha.getSkillEffectTimerSet().removeSkillEffect(DARKNESS);
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_CurseBlind(0), true);
						}
					}
				}
					break;

				case RESURRECTION:
				case GREATER_RESURRECTION: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_player.getId() != pc.getId()) {
							if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
									if (!visiblePc.isDead()) {
										_player.sendPackets(new S_ServerMessage(592), true);
										return;
									}
								}
							}

							if (pc.getCurrentHp() == 0 && pc.isDead()) {
								if (pc.getMap().isUseResurrection()) {
									if (_skillId == RESURRECTION) {
										pc.setGres(false);
									} else if (_skillId == GREATER_RESURRECTION) {
										pc.setGres(true);
									}
									pc.setTempID(_player.getId());
									pc.sendPackets(new S_Message_YN(322, ""), true);
								}
							}
						}
					}

					if (cha instanceof L1NpcInstance) {
						if (!(cha instanceof L1TowerInstance)) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							if (npc.getNpcTemplate().isCantResurrect() && npc.getMaster() == null) {
								return;
							} else if (npc.getNpcId() >= 100032 && npc.getNpcId() <= 100044) {
								return;
							}

							if (npc instanceof L1PetInstance && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
									if (!visiblePc.isDead()) {
										_player.sendPackets(new S_ServerMessage(592), true);
										return;
									}
								}
							}

							if (npc.getCurrentHp() == 0 && npc.isDead()) {
								npc.resurrect(npc.getMaxHp() / 4);
								npc.setResurrect(true);
							}
						}
					}
				}
					break;

				case CALL_OF_NATURE: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_player.getId() != pc.getId()) {
							if (L1World.getInstance().getVisiblePlayer(pc, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(pc, 0)) {
									if (!visiblePc.isDead()) {
										_player.sendPackets(new S_ServerMessage(592), true);
										return;
									}
								}
							}

							if (pc.getCurrentHp() == 0 && pc.isDead()) {
								pc.setTempID(_player.getId());
								pc.sendPackets(new S_Message_YN(322, ""), true);
							}
						}
					}

					if (cha instanceof L1NpcInstance) {
						if (!(cha instanceof L1TowerInstance)) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							if (npc instanceof L1PetInstance && L1World.getInstance().getVisiblePlayer(npc, 0).size() > 0) {
								for (L1PcInstance visiblePc : L1World.getInstance().getVisiblePlayer(npc, 0)) {
									if (!visiblePc.isDead()) {
										_player.sendPackets(new S_ServerMessage(592), true);
										return;
									}
								}
							}

							if (npc.getCurrentHp() == 0 && npc.isDead()) {
								npc.resurrect(cha.getMaxHp());
								npc.resurrect(cha.getMaxMp() / 100);
								npc.setResurrect(true);
							}
						}
					}
				}
					break;

				case DETECTION:
				case FREEZING_BREATH: {
					if (cha instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						int hiddenStatus = npc.getHiddenStatus();
						if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
							if (npc.getNpcId() != 45682) {
								npc.appearOnGround(_user);
							}
						}
					}
				}
					break;

				case COUNTER_DETECTION: {
					if (cha instanceof L1PcInstance) {
						dmg = _magic.calcMagicDamage(_skillId);
					} else if (cha instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						int hiddenStatus = npc.getHiddenStatus();

						if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
							if (npc.getNpcId() != 45682) {
								npc.appearOnGround(_player);
							}
						} else {
							dmg = 0;
						}
					} else {
						dmg = 0;
					}
				}
					break;

				case MIND_BREAK: {
					if (_target.getCurrentMp() >= 5) {
						_target.setCurrentMp(_target.getCurrentMp() - 5);
						dmg = 25;
					} else {
						return;
					}
				}
					break;

				case TRUE_TARGET: {
					if (_user instanceof L1PcInstance) {
						L1PcInstance pri = (L1PcInstance) _user;
						pri.sendPackets(new S_TrueTargetNew(_targetID, true));
						if (_target instanceof L1PcInstance) {
							int step = pri.getLevel() / 15;
							L1PcInstance target = (L1PcInstance) _target;
							if (step > 0) {
								target.set트루타켓(step);
							}
						} else if (_target instanceof L1PcInstance) {
							int step = pri.getLevel() / 15;
							L1NpcInstance target = (L1NpcInstance) _target;
							if (step > 0) {
								target.set트루타켓(step);
							}
						}

						for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(_target)) {
							if (pri.getClanid() == pc.getClanid()) {
								pc.sendPackets(new S_TrueTargetNew(_targetID, true));
							}
						}

						// 이전에 시전한 트루타겟 찾아서 강제 종료 시키기.
						synchronized (_truetarget_list) {
							L1Object temp = _truetarget_list.remove(_user.getId());
							if (temp != null && temp instanceof L1Character) {
								L1Character temp2 = (L1Character) temp;
								temp2.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.TRUE_TARGET);
							}
						}

						// 트루타겟 활성화.
						_target.getSkillEffectTimerSet().setSkillEffect(L1SkillId.TRUE_TARGET, 16 * 1000);
						synchronized (_truetarget_list) {
							_truetarget_list.put(_user.getId(), _target);
						}
					}
				}
					break;

				/**
				 * 큐브용 스킬 사용자 는 버프효과를 파티원 모두에게 날려줌 버프삭제효과만 개인으로 받음 추신 : 각은 계열은 중복 효과잇음
				 */
				case IllUSION_OGRE: { // 일루젼 오거
					L1PcInstance pc = (L1PcInstance) cha;
					/** 만약 큐브계열이 있다면 삭제 해주자 */
					if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_OGRE)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(IllUSION_OGRE);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_OGRE, false, 128));
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(CUBE_IGNITION)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(CUBE_IGNITION);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_IGNITION, false, 128));
					}

					pc.addDmgup(4);
					pc.addHitup(4);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_OGRE, true, 128));
				}
					break;

				case CUBE_IGNITION: {
					L1PcInstance pc = (L1PcInstance) cha;
					/** 파티가 있다면 파티원이 화면안파티원 검색 해서 버프 같이 날려줌 */
					if (pc.isInParty()) {
						for (L1PcInstance paty : pc.getParty().getMembers()) {
							if (pc.getLocation().getTileLineDistance(paty.getLocation()) <= 14) {
								if (paty.isDead()) {
									continue;
								}

								/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
								if (paty.getSkillEffectTimerSet().hasSkillEffect(IllUSION_OGRE)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(IllUSION_OGRE);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_OGRE, false, 128));
								} else if (paty.getSkillEffectTimerSet().hasSkillEffect(CUBE_IGNITION)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(CUBE_IGNITION);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_IGNITION, false, 128));
								}

								/** 스킬사용자가 아니라면 버프 시간 추가 */
								if (pc != paty) {
									paty.getSkillEffectTimerSet().setSkillEffect(CUBE_IGNITION, _getBuffIconDuration * 1000);
								}

								paty.addDmgup(4);
								paty.addHitup(4);
								paty.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_IGNITION, true, 128));
								paty.sendPackets(new S_SkillSound(paty.getId(), 17270), true);
							}
						}
						/** 파티원이 없다면 자기만 버프효과 */
					} else {
						/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
						if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_OGRE)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(IllUSION_OGRE);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_OGRE, false, 128));
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(CUBE_IGNITION)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(CUBE_IGNITION);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_IGNITION, false, 128));
						}

						pc.addDmgup(4);
						pc.addHitup(4);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_IGNITION, true, 128));
					}
				}
					break;

				case IllUSION_LICH: { // 일루젼 리치
					L1PcInstance pc = (L1PcInstance) cha;
					/** 만약 큐브계열이 있다면 삭제 해주자 */
					if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_LICH)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(IllUSION_LICH);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_LICH, false, 128));
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(CUBE_SHOCK)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(CUBE_SHOCK);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_SHOCK, false, 128));
					}

					pc.getAbility().addSp(2);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_LICH, true, 128));
				}
					break;

				case CUBE_SHOCK: {
					L1PcInstance pc = (L1PcInstance) cha;
					/** 파티가 있다면 파티원이 화면안파티원 검색 해서 버프 같이 날려줌 */
					if (pc.isInParty()) {
						for (L1PcInstance paty : pc.getParty().getMembers()) {
							if (pc.getLocation().getTileLineDistance(paty.getLocation()) <= 14) {
								if (paty.isDead()) {
									continue;
								}

								/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
								if (paty.getSkillEffectTimerSet().hasSkillEffect(IllUSION_LICH)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(IllUSION_LICH);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_LICH, false, 128));
								} else if (paty.getSkillEffectTimerSet().hasSkillEffect(CUBE_SHOCK)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(CUBE_SHOCK);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_SHOCK, false, 128));
								}

								/** 스킬사용자가 아니라면 버프 시간 추가 */
								if (pc != paty) {
									paty.getSkillEffectTimerSet().setSkillEffect(CUBE_SHOCK, _getBuffIconDuration * 1000);
								}

								paty.getAbility().addSp(2);
								paty.sendPackets(new S_SPMR(paty), true);
								paty.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_SHOCK, true, 128));
								paty.sendPackets(new S_SkillSound(paty.getId(), 17269), true);
							}
						}
						/** 파티원이 없다면 자기만 버프효과 */
					} else {
						/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
						if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_LICH)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(IllUSION_LICH);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_LICH, false, 128));
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(CUBE_SHOCK)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(CUBE_SHOCK);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_SHOCK, false, 128));
						}

						pc.getAbility().addSp(2);
						pc.sendPackets(new S_SPMR(pc), true);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_SHOCK, true, 128));
					}
				}
					break;

				case IllUSION_DIAMONDGOLEM: { // 일루젼 골렘
					L1PcInstance pc = (L1PcInstance) cha;
					/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
					if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_DIAMONDGOLEM)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(IllUSION_DIAMONDGOLEM);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_DIAMONDGOLEM, false, 128));
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(CUBE_QUAKE)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(CUBE_QUAKE);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_QUAKE, false, 128));
					}
					pc.getAC().addAc(-8);
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_DIAMONDGOLEM, true, 128));
				}
					break;

				case CUBE_QUAKE: {
					L1PcInstance pc = (L1PcInstance) cha;
					/** 파티가 있다면 파티원이 화면안파티원 검색 해서 버프 같이 날려줌 */
					if (pc.isInParty()) {
						for (L1PcInstance paty : pc.getParty().getMembers()) {
							if (pc.getLocation().getTileLineDistance(paty.getLocation()) <= 14) {
								if (paty.isDead()) {
									continue;
								}

								/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
								if (paty.getSkillEffectTimerSet().hasSkillEffect(IllUSION_DIAMONDGOLEM)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(IllUSION_DIAMONDGOLEM);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_DIAMONDGOLEM, false, 128));
								} else if (paty.getSkillEffectTimerSet().hasSkillEffect(CUBE_QUAKE)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(CUBE_QUAKE);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_QUAKE, false, 128));
								}

								/** 스킬사용자가 아니라면 버프 시간 추가 */
								if (pc != paty) {
									paty.getSkillEffectTimerSet().setSkillEffect(CUBE_QUAKE, _getBuffIconDuration * 1000);
								}
								paty.getAC().addAc(-8);
								paty.sendPackets(new S_OwnCharAttrDef(paty), true);
								paty.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_QUAKE, true, 128));
								paty.sendPackets(new S_SkillSound(paty.getId(), 17268), true);
							}
						}
						/** 파티원이 없다면 자기만 버프효과 */
					} else {
						/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
						if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_DIAMONDGOLEM)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(IllUSION_DIAMONDGOLEM);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_DIAMONDGOLEM, false, 128));
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(CUBE_QUAKE)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(CUBE_QUAKE);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_QUAKE, false, 128));
						}

						pc.getAC().addAc(-8);
						pc.sendPackets(new S_OwnCharAttrDef(pc), true);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_QUAKE, true, 128));
					}
				}
					break;

				case IllUSION_AVATAR: { // 일루젼 아바타
					L1PcInstance pc = (L1PcInstance) cha;
					/** 만약 큐브계열이 있다면 삭제 해주자 */
					/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
					if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_AVATAR)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(IllUSION_AVATAR);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_AVATAR, false, 128));
					} else if (pc.getSkillEffectTimerSet().hasSkillEffect(CUBE_BALANCE)) {
						pc.getSkillEffectTimerSet().removeSkillEffect(CUBE_BALANCE);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_BALANCE, false, 128));
					}

					pc.addDmgup(10);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_AVATAR, true, 128));
				}
					break;

				case CUBE_BALANCE: {
					L1PcInstance pc = (L1PcInstance) cha;
					/** 파티가 있다면 파티원이 화면안파티원 검색 해서 버프 같이 날려줌 */
					if (pc.isInParty()) {
						for (L1PcInstance paty : pc.getParty().getMembers()) {
							if (pc.getLocation().getTileLineDistance(paty.getLocation()) <= 14) {

								if (paty.isDead()) {
									continue;
								}
								/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
								if (paty.getSkillEffectTimerSet().hasSkillEffect(IllUSION_AVATAR)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(IllUSION_AVATAR);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_AVATAR, false, 128));
								} else if (paty.getSkillEffectTimerSet().hasSkillEffect(CUBE_BALANCE)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(CUBE_BALANCE);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_BALANCE, false, 128));
								}

								/** 스킬사용자가 아니라면 버프 시간 추가 */
								if (pc != paty) {
									paty.getSkillEffectTimerSet().setSkillEffect(CUBE_BALANCE, _getBuffIconDuration * 1000);
								}
								paty.addDmgup(10);
								paty.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_BALANCE, true, 128));
								paty.sendPackets(new S_SkillSound(paty.getId(), 17271), true);
							}
						}
						/** 파티원이 없다면 자기만 버프효과 */
					} else {
						/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
						if (pc.getSkillEffectTimerSet().hasSkillEffect(IllUSION_AVATAR)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(IllUSION_AVATAR);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.IllUSION_AVATAR, false, 128));
						} else if (pc.getSkillEffectTimerSet().hasSkillEffect(CUBE_BALANCE)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(CUBE_BALANCE);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_BALANCE, false, 128));
						}

						pc.addDmgup(10);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CUBE_BALANCE, true, 128));
					}
				}
					break;

				case 임팩트: {
					L1PcInstance pc = (L1PcInstance) cha;
					/** 파티가 있다면 파티원이 화면안파티원 검색 해서 버프 같이 날려줌 */
					if (pc.isInParty()) {
						for (L1PcInstance paty : pc.getParty().getMembers()) {
							if (pc.getLocation().getTileLineDistance(paty.getLocation()) <= 14) {
								if (paty.isDead()) {
									continue;
								}

								if (paty.getSkillEffectTimerSet().hasSkillEffect(임팩트)) {
									paty.getSkillEffectTimerSet().removeSkillEffect(임팩트);
									paty.sendPackets(new S_NewSkillIcons(L1SkillId.임팩트, false, 15));
								}

								/** 스킬사용자가 아니라면 버프 시간 추가 */
								if (pc != paty) {
									paty.getSkillEffectTimerSet().setSkillEffect(임팩트, _getBuffIconDuration * 1000);
								}

								int c = 5;// 기본확률
								if (paty.getLevel() > 80) {
									c += (paty.getLevel() - 80);
								}

								if (c > 10) {// 최대확률
									c = 10;
								}

								/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
								paty.임팩트 = c;
								/** 임팩트의 대한 적중 효과 적용 */
								paty.addAllHit(paty.임팩트);
								paty.sendPackets(new S_NewSkillIcons(L1SkillId.임팩트, true, 15));
								paty.sendPackets(new S_SkillSound(paty.getId(), 14513), true);
							}
						}
						/** 파티원이 없다면 자기만 버프효과 */
					} else {
						/** 중복 버프 효과 일류션 큐브 확인 중복이라면 삭제 */
						if (pc.getSkillEffectTimerSet().hasSkillEffect(임팩트)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(임팩트);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.임팩트, false, 15));
						}

						int c = 5;// 기본확률
						if (pc.getLevel() > 80) {
							c += (pc.getLevel() - 80);
						}

						if (c > 10) {// 최대확률
							c = 10;
						}

						pc.임팩트 = c;
						pc.addAllHit(pc.임팩트);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.임팩트, true, 15));
					}
				}
					break;

				case ELEMENTAL_FALL_DOWN: {
					if (_user instanceof L1PcInstance) {
						int playerAttr = _player.getElfAttr();
						int i = -50;
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							switch (playerAttr) {
							case 0:
								_player.sendPackets(new S_ServerMessage(79), true);
								break;

							case 1:
								pc.getResistance().addEarth(i);
								pc.setAddAttrKind(1);
								break;

							case 2:
								pc.getResistance().addFire(i);
								pc.setAddAttrKind(2);
								break;

							case 4:
								pc.getResistance().addWater(i);
								pc.setAddAttrKind(4);
								break;

							case 8:
								pc.getResistance().addWind(i);
								pc.setAddAttrKind(8);
								break;

							case 21:
								pc.getResistance().addFire(i);
								pc.getResistance().addEarth(i);
								pc.setAddAttrKind(21);
								break;

							case 24:
								pc.getResistance().addFire(i);
								pc.getResistance().addWater(i);
								pc.setAddAttrKind(24);
								break;

							case 28:
								pc.getResistance().addFire(i);
								pc.getResistance().addWind(i);
								pc.setAddAttrKind(28);
								break;

							case 41:
								pc.getResistance().addWater(i);
								pc.getResistance().addEarth(i);
								pc.setAddAttrKind(41);
								break;

							case 48:
								pc.getResistance().addWater(i);
								pc.getResistance().addWind(i);
								pc.setAddAttrKind(48);
								break;

							case 81:
								pc.getResistance().addWind(i);
								pc.getResistance().addEarth(i);
								pc.setAddAttrKind(81);
								break;

							default:
								break;
							}

						} else if (cha instanceof L1MonsterInstance) {
							L1MonsterInstance mob = (L1MonsterInstance) cha;
							switch (playerAttr) {
							case 0:
								_player.sendPackets(new S_ServerMessage(79), true);
								break;

							case 1:
								mob.getResistance().addEarth(i);
								mob.setAddAttrKind(1);
								break;

							case 2:
								mob.getResistance().addFire(i);
								mob.setAddAttrKind(2);
								break;

							case 4:
								mob.getResistance().addWater(i);
								mob.setAddAttrKind(4);
								break;

							case 8:
								mob.getResistance().addWind(i);
								mob.setAddAttrKind(8);
								break;

							case 21:
								mob.getResistance().addFire(i);
								mob.getResistance().addEarth(i);
								mob.setAddAttrKind(21);
								break;

							case 24:
								mob.getResistance().addFire(i);
								mob.getResistance().addWater(i);
								mob.setAddAttrKind(24);
								break;

							case 28:
								mob.getResistance().addFire(i);
								mob.getResistance().addWind(i);
								mob.setAddAttrKind(28);
								break;

							case 41:
								mob.getResistance().addWater(i);
								mob.getResistance().addEarth(i);
								mob.setAddAttrKind(41);
								break;

							case 48:
								mob.getResistance().addWater(i);
								mob.getResistance().addWind(i);
								mob.setAddAttrKind(48);
								break;

							case 81:
								mob.getResistance().addWind(i);
								mob.getResistance().addEarth(i);
								mob.setAddAttrKind(81);
								break;

							default:
								break;
							}
						}
					}
				}
					break;

				case IMMUNE_TO_HARM: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.IMMUNE_TO_HARM, true, _getBuffIconDuration));
						pc.sendPackets(new S_SystemMessage("이뮨 투함: 받는 대미지가 일정량 감소합니다."));
						if (pc.isWizard() || pc.isElf()) {
							pc._이뮨어그로 = _player;
						}
					}
				}
					break;

				case BURNING_SPIRIT:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.BURNING_SPIRIT, true, _getBuffIconDuration));
						pc.sendPackets(new S_SystemMessage("버닝스피릿츠: 일정 확률로 근거리 대미지 1.5배 적용."));
					}

					break;

				case ELEMENTAL_FIRE:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.ELEMENTAL_FIRE, true, _getBuffIconDuration));
						pc.sendPackets(new S_SystemMessage("엘리멘탈파이어:  일정 확률로 근거리 대미지 1.5배 적용."));
					}

					break;

				case ENTANGLE:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.ENTANGLE, true, _getBuffIconDuration));
						pc.sendPackets(new S_SystemMessage("퀘이크: 일정 확률로 근거리 대미지 1.5배 적용."));
					}
					break;

				case HEAL:
				case EXTRA_HEAL:
				case GREATER_HEAL:
				case FULL_HEAL:
				case HEAL_ALL:
				case NATURES_TOUCH:
				case NATURES_BLESSING: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.isWizard() || pc.isElf()) {
							pc._healagro = _player;
						}
					}

					if (cha.getSkillEffectTimerSet().hasSkillEffect(WATER_LIFE)) {
						cha.getSkillEffectTimerSet().killSkillEffectTimer(WATER_LIFE);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_PacketBox(S_PacketBox.DEL_ICON), true);
						}
					}

				}
					break;

				case CHILL_TOUCH:
				case VAMPIRIC_TOUCH: {
					heal = dmg;
				}
					break;

				case TRIPLE_ARROW: {
					if (_target.getSkillEffectTimerSet().hasSkillEffect(뫼비우스)) {
						return;
					} else {
						boolean gfxcheck = false;

						int playerGFX = _player.getGfxId().getTempCharGfx();
						for (int gfx : BowGFX) {
							if (playerGFX == gfx) {
								gfxcheck = true;
								break;
							}
						}

						if (!gfxcheck) {
							return;
						}

						for (int i = 3; i > 0; i--) {
							_target.onAction(_player);
						}
						if (_player.글로리어스) {
						_player.sendPackets(new S_SkillSound(_player.getId(), 19317), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 19317), true);
						} else if (!_player.글로리어스) {
						_player.sendPackets(new S_SkillSound(_player.getId(), 15103), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 15103), true);	
						}
					}
				}
					break;
					
				case FOU_SLAYER: {
					boolean gfxcheck = false;

					int playerGFX = _player.getGfxId().getTempCharGfx();

					for (int gfx : FouGFX) {
						if (playerGFX != gfx) {
							gfxcheck = true;
							break;
						}
					}

					if (!gfxcheck) {
						return;
					}

					// 약점 노출 리셋
					_player.ChainSwordObjid = 0;
					if (_player.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT4)) {
						_player.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT4);
						S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
						_player.sendPackets(pb, true);
						dmg += 80;
						if (_player.포우슬레이어브레이브) {
						WeaponSkill.AkdukSword(_player, _target, 60);
						}

					} else if (_player.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT3)) {
						_player.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT3);
						S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
						_player.sendPackets(pb, true);
						dmg += 60;
						if (_player.포우슬레이어브레이브) {
						WeaponSkill.AkdukSword(_player, _target, 60);
						}
					} else if (_player.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT2)) {
						_player.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT2);
						S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
						_player.sendPackets(pb, true);
						dmg += 40;
						if (_player.포우슬레이어브레이브) {
						WeaponSkill.AkdukSword(_player, _target, 60);
						}
					} else if (_player.getSkillEffectTimerSet().hasSkillEffect(STATUS_SPOT1)) {
						_player.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_SPOT1);
						S_PacketBox pb = new S_PacketBox(S_PacketBox.SPOT, 0);
						_player.sendPackets(pb, true);
						dmg += 20;
						if (_player.포우슬레이어브레이브) {
						WeaponSkill.AkdukSword(_player, _target, 60);
						}
					}

					try {

						for (int i = 3; i > 0; i--) {
							_target.onAction(_player, dmg);
						}

						dmg = 0;

					} catch (Exception e) {
						e.printStackTrace();
					}

					L1PcInstance pc1 = (L1PcInstance) _user;
					if (pc1.포우슬레이어브레이브) {
						_player.sendPackets(new S_SkillSound(_player.getId(), 7020), true);
						_player.sendPackets(new S_SkillSound(_targetID, 17231), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 7020), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_targetID, 17231), true);
					} else if (!pc1.포우슬레이어브레이브) {
						_player.sendPackets(new S_SkillSound(_player.getId(), 7020), true);
						_player.sendPackets(new S_SkillSound(_targetID, 6509), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 7020), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_targetID, 6509), true);
					}
				}
					break;

				/** 혈맹버프 **/
				case CLAN_BUFF1: {// 일반 공격 태세
					L1PcInstance pc = (L1PcInstance) cha;
					pc.addDmgupByArmor(2);
					pc.addBowDmgupByArmor(2);
					pc.sendPackets(new S_ACTION_UI2(2724, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7233, 4650));
					pc.sendPackets(new S_ServerMessage(4618, "$22503"));
					pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
					// System.out.println("케릭터 수치값은?? : " + pc.getDmgupByArmor());
				}
					break;

				case CLAN_BUFF2: {// 일반 방어 태세
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getAC().addAc(-3);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_ACTION_UI2(2725, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7235, 4651));
					pc.sendPackets(new S_ServerMessage(4618, "$22504"));
					pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
				}
					break;

				case CLAN_BUFF3: {// 전투 공격 태세
					L1PcInstance pc = (L1PcInstance) cha;// 따로 없으면 그냥 이렇게 하셔도 되요.
					// pc.addPvPDmgup(1); //pvp 추가데미지
					pc.sendPackets(new S_ACTION_UI2(2726, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7237, 4652));
					pc.sendPackets(new S_ServerMessage(4618, "$22505"));
					pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
				}
					break;

				case CLAN_BUFF4: {// 전투 방어 태세
					L1PcInstance pc = (L1PcInstance) cha;
					// pc.addDmgReducPvp(1); //pvp 리덕
					pc.sendPackets(new S_ACTION_UI2(2727, pc.getClan().getBuffTime()[pc.getClan().getBless() - 1], 7, 7239, 4653));
					pc.sendPackets(new S_ServerMessage(4618, "$22506"));
					pc.sendPackets(new S_SkillSound(pc.getId(), 14482));
				}
					break;

				case 10026:
				case 10027:
				case 10028:
				case 10029: {
					if (_user instanceof L1NpcInstance) {
						Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "$3717", 0), true);
					} else {
						Broadcaster.broadcastPacket(_player, new S_ChatPacket(_player, "$3717", 0, 0), true);
					}
				}
					break;

				case 10057: {
					L1Teleport.teleportToTargetFront(cha, _user, 1);
				}
					break;

				case ANTA_SKILL_1: { // 안타라스(리뉴얼) - 용언 스킬1
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "$7914", 0), true);
							// 오브 모크! 세이 리라프[무기손상-웨폰브레이크 + 굳기]
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 10, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_2: { // 용언스킬 2 티 세토르
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "$7948", 0), true);
							// 오브 모크! 티 세토르[유저들 위치 변경 유니드래곤스폰]
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 11, 2000);// 2초
							antaendtime.begin();
						}
					}
				}

					break;

				case ANTA_SKILL_3: { // 용언스킬 3 뮤즈삼
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "$7903", 0), true);
							// 오브 모크! 뮤즈삼 점프+스턴;
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 12, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_4: { // 용언스킬 3 너츠삼
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "$7909", 0), true);
							// 오브 모크! 너츠삼 운석+스턴;
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 13, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_5: { // 용언스킬 3 너츠삼
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "$7915", 0), true);
							// 오브 모크! 티프삼 점프+스턴+운석;
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 14, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_6: { // 용언스킬 리라프
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 리라프..", 0), true);
							// 오브 모크! 리라프 웨폰;
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 15, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_7: { // 용언스킬 켄 로우
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 켄 로우..", 0), true);
							// 오브 모크! 켄 로우 웨폰;
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 16, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_8: { // 안타라스(리뉴얼) - 용언 스킬8
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 티기르..", 0), true);
							// 오브 모크! 티기르 원투+고함;
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 17, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_9: { // 안타라스(리뉴얼) - 용언 스킬9
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 켄 티기르..", 0), true);
							// 오브 모크! 켄 티기르 원투+브레스;
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 18, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_10: { // 안타라스(리뉴얼) - 용언 스킬9
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 루오 타..", 0), true);
							// 오브 모크! 루오 타 독구름+고함;
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 19, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_11: { // 안타라스(리뉴얼) - 용언 스킬9
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 케네시..", 0), true);
							// 오브 모크! 케네시 캔슬+마비
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 20, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_12: { // 안타라스(리뉴얼) - 용언 스킬9
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 뮤즈 심..", 0), true);
							// 오브 모크! 뮤즈심 뮤즈 전단계
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 21, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case ANTA_SKILL_13: { // 안타라스(리뉴얼) - 용언 스킬9
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 너츠 심..", 0), true);
							// 오브 모크! 너츠심
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 22, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case 앱솔루트블레이드: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.앱솔루트블레이드, true, 16));

					}
				}
					break;

				case 데스힐: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						_shockStunDuration = 8 * 1000;// 데스힐도 일단 저변수를쓰자.
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.데스힐, true, 8));
					}
				}
					break;

				case 디스트로이: {
					if (_calcType == PC_PC) {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getAC().addAc(10);
							if(_user instanceof L1PcInstance) {
								L1PcInstance owner = (L1PcInstance) _user;
								if(owner.isSkillMastery(L1SkillId.FEAR) || owner.isSkillMastery(L1SkillId.HORROR_OF_DEATH)) {
									pc.addDg(-20);
									pc.setDestroyFear(true);
								}
								
								if(owner.isSkillMastery(L1SkillId.HORROR_OF_DEATH)) {
									pc.getAbility().addAddedStr(-2);
									pc.getAbility().addAddedInt(-2);
									pc.setDestroyHorror(true);
								}
							}
							
							pc.sendPackets(new S_CreateItem(디스트로이, 30));
						}
					}
				}
					break;

				case 라이징: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.hasSkillEffect(L1SkillId.라이징)) {
							cha.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.라이징);
						}
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.라이징, true, 2400));
						pc.sendPackets(new S_SystemMessage("타이탄 라이징: 타이탄 계열 기술 발동 구간 5%상승."));
					}
				}
					break;

				case 소울배리어: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.소울배리어, 600 * 1000);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.소울배리어, true, 600));
					}
				}
					break;

				case ANTA_SKILL_14: { // 안타라스(리뉴얼) - 용언 스킬9
					if (_user instanceof L1NpcInstance) {
						if (_npc.skilluse == false) {
							_npc.skilluse = true;
							Broadcaster.broadcastPacket(_user, new S_NpcChatPacket(_npc, "오브 모크! 티프 심..", 0), true);
							// 오브 모크! 티프심
							AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(_user.getMapId());
							AntarasRaidTimer antaendtime = new AntarasRaidTimer(_npc, ar, 23, 2000);// 2초
							antaendtime.begin();
						}
					}
				}
					break;

				case SLOW:
				case MOB_SLOW_1:
				case MOB_SLOW_18: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.is에틴인형()) {
							continue;
						}

						if (pc.getHasteItemEquipped() > 0) {
							continue;
						}
					}

					if (cha.getMoveState().getMoveSpeed() == 0) {

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_SkillHaste(pc.getId(), 2, _getBuffIconDuration), true);
						}

						Broadcaster.broadcastPacket(cha, new S_SkillHaste(cha.getId(), 2, _getBuffIconDuration), true);
						cha.getMoveState().setMoveSpeed(2);
					} else if (cha.getMoveState().getMoveSpeed() == 1) {
						int skillNum = 0;
						if (cha.getSkillEffectTimerSet().hasSkillEffect(HASTE)) {
							skillNum = HASTE;
						} else if (cha.getSkillEffectTimerSet().hasSkillEffect(GREATER_HASTE)) {
							skillNum = GREATER_HASTE;
						} else if (cha.getSkillEffectTimerSet().hasSkillEffect(STATUS_HASTE)) {
							skillNum = STATUS_HASTE;
						}

						if (skillNum != 0) {
							cha.getSkillEffectTimerSet().removeSkillEffect(skillNum);
							cha.getSkillEffectTimerSet().removeSkillEffect(_skillId);
							cha.getMoveState().setMoveSpeed(0);
							continue;
						}
					}
				}
					break;

				case SHAPE_CHANGE:
					if (_player != null && _player.isInvisble()) {
						_player.delInvis();
					}

					if (cha instanceof L1PcInstance) {
						int probability = 0;
						int attackInt = _user.getAbility().getTotalInt();
						int defenseMr = cha.getResistance().getEffectedMrBySkill();

						// if (attackInt > 25) attackInt = 25;
						probability = (int) ((attackInt - (defenseMr / 6)) * 4);

						if (_calcType == PC_NPC) {
							if (_targetNpc.getLevel() >= 70) {
								probability = 0;
							}
						}

						if (_calcType == PC_PC) {
							if (_user.getId() == cha.getId()) {
								probability = 100;
							}
						}

						if (probability > _random.nextInt(100)) {
							L1PcInstance pc = (L1PcInstance) cha;
							L1PinkName.onAction(pc, _user);
							if (pc.getInventory().checkEquipped(20281) || pc.getInventory().checkEquipped(120281)) {
								pc.sendPackets(new S_PolyHtml());
								pc.sendPackets(new S_ServerMessage(241, _user.getName()));
							} else if (pc.getSkillEffectTimerSet().GetDominationSkill() && pc != _user) {
								pc.sendPackets(new S_SkillSound(pc.getId(), 15846), true);
								Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 15846), true);
							} else {
								int randomValue = 11;
								if (pc.getLevel() >= 15) {
									randomValue += 9;
								}

								if (pc.getLevel() >= 30) {
									randomValue += 18;
								}

								int polyId = 11328 + _random.nextInt(randomValue);
								if (polyId >= 11358 && polyId <= 11361) {
									polyId = 11371 + _random.nextInt(4);
								} else if (polyId >= 11362 && polyId <= 11365) {
									polyId = 11396 + _random.nextInt(4);
								}

								L1PolyMorph.doPoly(pc, polyId, 7200, L1PolyMorph.MORPH_BY_ITEMMAGIC);

								if (_user.getId() == cha.getId()) {

								} else {
									pc.sendPackets(new S_ServerMessage(241, _user.getName()));
								}
							}
						} else {
							_player.sendPackets(new S_ServerMessage(280));
						}
					} else {
						_player.sendPackets(new S_ServerMessage(280));
					}

					break;

				case CURSE_BLIND:
				case DARKNESS: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_FLOATING_EYE)) {
							pc.sendPackets(new S_CurseBlind(2), true);
						} else {
							pc.sendPackets(new S_CurseBlind(1), true);
						}
					}
				}
					break;

				case CURSE_POISON: {
					L1DamagePoison.doInfection(_user, cha, 3000, 5);
				}
					break;

				case CURSE_PARALYZE:
					if (!cha.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND) && !cha.getSkillEffectTimerSet().hasSkillEffect(CURSE_PARALYZE)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(CURSE_PARALYZE2)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(MOB_CURSEPARALYZ_18)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(MOB_CURSEPARALYZ_19) && !cha.getSkillEffectTimerSet().hasSkillEffect(40013)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE) && !cha.getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH)) {
						if (cha instanceof L1PcInstance) {
							int rrr = _random.nextInt(5) + 15;
							L1CurseParalysis.curse(cha, 8000, rrr * 1000);
						} else if (cha instanceof L1MonsterInstance) {
							L1CurseParalysis.curse(cha, 0, 4000);
						}
					}
					break;

				case CURSE_PARALYZE2:
				case MOB_CURSEPARALYZ_18:
				case MOB_CURSEPARALYZ_19:
				case 40013: {
					if (!cha.getSkillEffectTimerSet().hasSkillEffect(EARTH_BIND) && !cha.getSkillEffectTimerSet().hasSkillEffect(CURSE_PARALYZE)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(CURSE_PARALYZE2)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(MOB_CURSEPARALYZ_18)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(MOB_CURSEPARALYZ_19) && !cha.getSkillEffectTimerSet().hasSkillEffect(40013)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_CURSE_PARALYZING)
							&& !cha.getSkillEffectTimerSet().hasSkillEffect(ICE_LANCE) && !cha.getSkillEffectTimerSet().hasSkillEffect(FREEZING_BREATH)) {
						if (cha instanceof L1PcInstance) {
							((L1PcInstance) cha).sendPackets(new S_SkillSound(cha.getId(), 746));
							Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), 746));
							L1CurseParalysis.curse(cha, 5000, 3000);
						} else if (cha instanceof L1MonsterInstance) {
							Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), 746));
							L1CurseParalysis.curse(cha, 0, 4000);
						}
					}
				}
					break;

				case WEAKNESS:
				case MOB_WEAKNESS_1: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(-5);
						pc.addHitup(-1);
					}
				}
					break;

				case Freeze_armor: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.Add_Er(5);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.Freeze_armor, true, 300));
						pc.sendPackets(new S_SystemMessage("프리징아머의 효과가 느껴집니다."));
					}
				}
					break;

				case DISEASE: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(20);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.DISEASE, true, 300));
						pc.sendPackets(new S_SystemMessage("인챈트 어큐러시의 효과가 느껴집니다."));
					}
				}
					break;

				case MOB_DISEASE_1:
				case MOB_DISEASE_30: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(-6);
						pc.getAC().addAc(12);
					}
				}
					break;
				case PANIC: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedStr((byte) -1);
						pc.getAbility().addAddedDex((byte) -1);
						pc.getAbility().addAddedCon((byte) -1);
						pc.getAbility().addAddedInt((byte) -1);
						pc.getAbility().addAddedWis((byte) -1);
						pc.resetBaseMr();
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
					}
				}
					break;

				case 38:
					if (_npc != null) {
						if (_npc.getNpcId() == 90026) {
							int ran = random.nextInt(100) + 1;

							if (ran < 15) {
								Broadcaster.broadcastPacket(_npc, new S_NpcChatPacket(_npc, "인간주제에 혹한의 힘을 견딜 수 있겠느냐!", 0), true);
							} else if (ran < 30) {
								Broadcaster.broadcastPacket(_npc, new S_NpcChatPacket(_npc, "여왕님의 혼란을 방해할 수는 없다!", 0), true);
							}
						}
					}
					break;

				case 55004:
					if (_npc != null) {
						if (_npc.getNpcId() == 46141 || _npc.getNpcId() == 46142) {
							int ran = random.nextInt(100) + 1;
							if (ran < 20) {
								L1SpawnUtil.spawn2(_npc.getX(), _npc.getY(), _npc.getMapId(), 90026, 5, 0, 0);
								L1SpawnUtil.spawn2(_npc.getX(), _npc.getY(), _npc.getMapId(), 90026, 5, 0, 0);
								L1SpawnUtil.spawn2(_npc.getX(), _npc.getY(), _npc.getMapId(), 90026, 5, 0, 0);
								L1SpawnUtil.spawn2(_npc.getX(), _npc.getY(), _npc.getMapId(), 90026, 5, 0, 0);
								L1SpawnUtil.spawn2(_npc.getX(), _npc.getY(), _npc.getMapId(), 90026, 5, 0, 0);
								Broadcaster.broadcastPacket(_npc, new S_SkillSound(_npc.getId(), 761), true);
								Broadcaster.broadcastPacket(_npc, new S_NpcChatPacket(_npc, "시녀들은 뭐하는 것이냐? 어서 나를 받들라!", 0), true);
							}
						}
					}
					break;

				case DISINTEGRATE:
					if (_npc != null) {
						if (_npc.getNpcId() == 46141 || _npc.getNpcId() == 46142) {
							int ran = random.nextInt(100) + 1;
							if (ran < 3) {
								Broadcaster.broadcastPacket(_npc, new S_NpcChatPacket(_npc, "혹한의 바람이여 이들의 숨결조차 얼어붙게 하라!", 0), true);
							}

							dmg /= 3;
						}
					}
					break;

				case 10035:
					if (_npc != null) {
						if (_npc.getNpcId() == 46141 || _npc.getNpcId() == 46142) {
							int ran = random.nextInt(100) + 1;
							if (ran < 3) {
								Broadcaster.broadcastPacket(_npc, new S_NpcChatPacket(_npc, "얼음 칼날들이여 저들을 모두 베어버려라!", 0), true);
							}
						}
					}
					break;

				case ICE_LANCE: {
					_isFreeze = _magic.calcProbabilityMagic(_skillId);
					if (_npc != null) {
						if (_npc.getNpcId() == 46141 || _npc.getNpcId() == 46142) {
							Broadcaster.broadcastPacket(_npc, new S_NpcChatPacket(_npc, "네게 명한다. 이 자리에서 얼어 죽어라", 0), true);
						}
					}

					if (_isFreeze) {
						int time = _skill.getBuffDuration() * 1000;
						L1EffectSpawn.getInstance().spawnEffect(81168, time, cha.getX(), cha.getY(), cha.getMapId());

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Poison(pc.getId(), 2), true);
							Broadcaster.broadcastPacket(pc, new S_Poison(pc.getId(), 2), true);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true), true);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							Broadcaster.broadcastPacket(npc, new S_Poison(npc.getId(), 2), true);
							npc.setParalyzed(true);
							npc.setParalysisTime(time);
						}
					}
				}
					break;

				case EARTH_BIND:
				case MOB_BASILL:
				case MOB_COCA: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (_skillId == EARTH_BIND) {
							_skillTime = random.nextInt(_skill.getBuffDuration()) + 1;
						}

						pc.sendPackets(new S_Poison(pc.getId(), 2), true);
						Broadcaster.broadcastPacket(pc, new S_Poison(pc.getId(), 2), true);
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true), true);
						if (_skillId == MOB_BASILL) {
							pc.sendPackets(new S_SkillSound(pc.getId(), 1043), true);
							Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 1043), true);
						}

					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						if (_skillId == MOB_BASILL) {
							Broadcaster.broadcastPacket(npc, new S_SkillSound(npc.getId(), 1043), true);
						}

						Broadcaster.broadcastPacket(npc, new S_Poison(npc.getId(), 2), true);
						npc.setParalyzed(true);
						if (_skillId == EARTH_BIND) {
							_skillTime = random.nextInt(_skill.getBuffDuration()) + 1;
							npc.setParalysisTime(_skillTime * 1000);
						} else {
							npc.setParalysisTime(_skill.getBuffDuration() * 1000);
						}
					}
				}
					break;

				case DARK_BLIND:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true), true);
					}

					cha.setSleeped(true);
					break;

				case 기간틱: {
					L1PcInstance pc = (L1PcInstance) cha;
					double per = (pc.getLevel() / 2.000) / 100.000;
					int addhp = (int) ((double) pc.getBaseMaxHp() * per);
					pc.setggHp(addhp);
					pc.addMaxHp(addhp);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.기간틱, true, 300));

					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
				}
					break;

				case 토마호크: {// 레벨 * 2 / 6
					if (cha instanceof L1PcInstance) {
						L1PcInstance target = (L1PcInstance) cha;
						if (target.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.토마호크)) {
							if (target.토마호크th != null) {
								target.토마호크th.stop();
							}
						}
					} else if (cha instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						if (npc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.토마호크)) {
							if (npc.토마호크th != null) {
								npc.토마호크th.stop();
							}
						}
					}

					L1토마호크 토마 = new L1토마호크(_player, cha);
					토마.begin();

					if (cha instanceof L1PcInstance) {
						L1PcInstance target = (L1PcInstance) cha;
						target.토마호크th = 토마;
						target.sendPackets(new S_NewCreateItem(S_NewCreateItem.버프창, true), true);
					} else if (cha instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.토마호크th = 토마;
					}
				}
					break;

				case THUNDER_GRAB: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance target = (L1PcInstance) cha;
						int attackLevel = _user.getLevel();
						int defenseLevel = target.getLevel();
						int probability = 0;
						int _ranpro = random.nextInt(100) + 1;

						if (attackLevel >= defenseLevel) {
							probability = (int) ((attackLevel - defenseLevel) * 5) + 45;
						} else if (attackLevel < defenseLevel) {
							probability = (int) ((attackLevel - defenseLevel) * 5) + 45;
						}

						if (probability > 90) {
							probability = 90;
						}

						int stunregi = target.getDragonLangTolerance();
						probability -= stunregi;

						if (probability < 0) {
							probability = 0;
						}

						if (probability < _ranpro) {
							return;
						}

					} else {
						int probability = 50;
						int _ranpro = random.nextInt(100) + 1;
						if (probability < _ranpro) {
							return;
						}

					}

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						L1PcInstance pc1 = (L1PcInstance) _user;
						if (!pc1.썬더그랩브레이브) {
							_shockStunDuration = stunTimethunder2[random.nextInt(stunTimethunder2.length)];
							pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.THUNDER_GRAB, _shockStunDuration);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true), true);
							L1EffectSpawn.getInstance().spawnEffect(81182, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
						} else if (pc1.썬더그랩브레이브) {
							_shockStunDuration = stunTimethunder[random.nextInt(stunTimethunder.length)];
							pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.THUNDER_GRAB, _shockStunDuration);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true), true);
							L1EffectSpawn.getInstance().spawnEffect(81182, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
						}
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						L1PcInstance pc1 = (L1PcInstance) _user;

						if (!pc1.썬더그랩브레이브) {
							_shockStunDuration = stunTimethunder2[random.nextInt(stunTimethunder2.length)];
							npc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.THUNDER_GRAB, _shockStunDuration);
							L1EffectSpawn.getInstance().spawnEffect(81182, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
						} else if (pc1.썬더그랩브레이브) {
							_shockStunDuration = stunTimethunder[random.nextInt(stunTimethunder.length)];
							npc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.THUNDER_GRAB, _shockStunDuration);
							L1EffectSpawn.getInstance().spawnEffect(81182, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
						}
					}

					return;
				}

				case 40037: // 라미아 발 묶기
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true), true);
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
						npc.setParalysisTime(_skill.getBuffDuration() * 1000);
					}
					break;

				case DRESS_EVASION:// 12
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.Add_Er(18);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.DRESS_EVASION, true, 192));
					}
					break;

				case AQUA_PROTECTER:// 5
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.Add_Er(5);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.AQUA_PROTECTER, true, 960));
					}
					break;

				case SOLID_CARRIAGE:// 15
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.Add_Er(15);
						pc.sendPackets(new S_OwnCharAttrDef(pc));
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.SOLID_CARRIAGE, true, 192));
					}
					break;

				case REDUCTION_ARMOR:
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.리덕션아머베테랑) {
							pc.addFearTolerance(3);
						}
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.REDUCTION_ARMOR, true, 300));
					}
					break;

				case 프라이드: {
					L1PcInstance pc = (L1PcInstance) cha;
					double per = (pc.getLevel() / 4.000) / 100.000;
					int addhp = (int) ((double) pc.getBaseMaxHp() * per);
					pc.setggHp(addhp);
					pc.addMaxHp(addhp);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.프라이드, true, 300));
					if (pc.isInParty()) {
						pc.getParty().updateMiniHP(pc);
					}
				}
					break;

				case 블로우어택: {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.블로우어택, true, 300));
					pc.sendPackets(new S_SystemMessage("블로우 어택: 일정 확률로 근거리 대미지 1.5배 적용."));
				}
					break;

				case 루시퍼: {
					L1PcInstance pc = (L1PcInstance) cha;
					if(pc.루시퍼데스티니 && pc.getLevel() >= 85) {
						int pvpre = pc.getLevel() - 83;
						int i = (pvpre / 2) * 1;
						
						if (i > 10) {
							i = 10;
						}
						if (pc.getLevel() == 95) {
							i = 10;
						}
						pc.getResistance().addcalcPcDefense(i);
						pc.setLucifer(i);
					}
					if (pc.루시퍼데스티니) {
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.루시퍼, true, 60));
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.루시퍼, 60 * 1000);
						pc.sendPackets(new S_SystemMessage("루시퍼 : 받는 대미지가 일정량 감소함."));
						_player.sendPackets(new S_SkillSound(_player.getId(), 19017), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 19017), true);
						
					} else {
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.루시퍼, true, 30));
						pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.루시퍼, 30 * 1000);
						pc.sendPackets(new S_SystemMessage("루시퍼 : 받는 대미지가 일정량 감소함."));
						_player.sendPackets(new S_SkillSound(_player.getId(), 13112), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 13112), true);
					}
				}
					break;

				case POTENTIAL: {
					L1PcInstance pc = (L1PcInstance) cha;
					double per = 20.000 / 100.000;
					int addhp = (int) ((double) pc.getMaxHp() * per);
					int addmp = (int) ((double) pc.getMaxMp() * per);
					int addsp = (int) ((double) pc.getAbility().getSp() * per);
					int adddg = (int) ((double) pc.getDg() * per);
					int addmr = (int) ((double) pc.getResistance().getAddedMr() * per);
					pc.setpotentialDG(adddg);
					pc.addDg(adddg);
					pc.setpotentialSP(addsp);
					pc.getAbility().addSp(addsp);
					pc.setpotentialMR(addmr);
					pc.getResistance().addMr(addmr);
					pc.setggHp(addhp);
					pc.addMaxHp(addhp);
					pc.setggMp(addmp);
					pc.addMaxMp(addmp);
					pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
					pc.sendPackets(new S_SPMR(pc), true);
					pc.sendPackets(new S_OwnCharAttrDef(pc));
					pc.sendPackets(new S_NewSkillIcons(L1SkillId.POTENTIAL, true, 128));
					pc.sendPackets(new S_SystemMessage("포텐셜 : HP/MP/DG/ER/MR/SP +20%."));
				}
					break;

				case STRIKER_GALE:// -99
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_OwnCharStatus2(pc));
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						pc.sendPackets(new S_CharVisualUpdate(pc));
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.STRIKER_GALE, true, _getBuffIconDuration));

					}
					break;

				case 파워그립: {
					_shockStunDuration = random.nextInt(4000) + 2000;
					L1EffectSpawn.getInstance().spawnEffect(91163, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_POWER_GRIP, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance
							|| cha instanceof L1DwarfInstance || cha instanceof L1TeleporterInstance || cha instanceof L1MerchantInstance
							|| cha instanceof L1ScarecrowInstance || cha instanceof L1PeopleInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.파워그립, _shockStunDuration);
					}
				}
					break;

				case 데스페라도: {
					int targetLevel = 0;
					int diffLevel = 0;

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						targetLevel = pc.getLevel();
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						targetLevel = npc.getLevel();

						if (npc.getMaxHp() > 5000) {
							return;
						}
					}

					diffLevel = _player.getLevel() - targetLevel;

					if (diffLevel <= -10 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= -10 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= -8 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= -8 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= -6 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= -6 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= -4 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= -4 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= -2 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= -2 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel == 0 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel == 0 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= 2 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= 2 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= 4 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= 4 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= 6 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= 6 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= 8 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= 8 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel <= 10 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel <= 10 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					} else if (diffLevel > 10 && !_player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(1000) + 2000;
					} else if (diffLevel > 10 && _player.데스페라도앱솔루트) {
						_shockStunDuration = random.nextInt(4000) + 3000;
					}

					L1EffectSpawn.getInstance().spawnEffect(91162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.데스페라도공격자레벨 = _user.getLevel();
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.데스페라도, true, -1));
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance
							|| cha instanceof L1DwarfInstance || cha instanceof L1TeleporterInstance || cha instanceof L1MerchantInstance
							|| cha instanceof L1ScarecrowInstance || cha instanceof L1PeopleInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.데스페라도, _shockStunDuration);
					}
				}
					break;

				case DEMOLITION: {
					int targetLevel = 0;
					int diffLevel = 0;

					if (_user != cha && _user instanceof L1PcInstance) {
						L1PcInstance c = (L1PcInstance) _user;
						// 양손 여부 체크
						if (c == null) {
							return; // 시전자가 없다면 리턴
						}
					}

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						targetLevel = pc.getLevel();
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DEMOLITION)) {
							if (pc.데몰리션th != null) {
								pc.데몰리션th.stop();
							}
						}

						pc.receiveDamage(_user, _random.nextInt(70) + 80, 0);
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						targetLevel = npc.getLevel();
						if (npc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DEMOLITION)) {
							if (npc.데몰리션th != null) {
								npc.데몰리션th.stop();
							}
						}

						npc.receiveDamage(_user, _random.nextInt(70) + 80);
					}

					L1데몰리션 데몰리션 = new L1데몰리션(_player, cha);
					데몰리션.begin();

					diffLevel = _player.getLevel() - targetLevel;

					if (diffLevel <= -10) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= -8) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= -6) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= -4) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= -2) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel == 0) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= 2) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= 4) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= 6) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= 8) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel <= 10) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					} else if (diffLevel > 10) {
						_shockStunDuration = random.nextInt(3000) + 2000;
					}

					L1EffectSpawn.getInstance().spawnEffect(81150, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.데스페라도공격자레벨 = _user.getLevel();
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, true), true);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.DEMOLITION, true, -1));
						pc.데몰리션th = 데몰리션;
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance
							|| cha instanceof L1DwarfInstance || cha instanceof L1TeleporterInstance || cha instanceof L1MerchantInstance
							|| cha instanceof L1ScarecrowInstance || cha instanceof L1PeopleInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.데몰리션th = 데몰리션;
						npc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.DEMOLITION, _shockStunDuration);
					}
				}
					break;

				case 엠파이어: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						// _user 가 pc 라면.
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							// 양손 여부 체크
							if (c == null) {
								return; // 시전자가 없다면 리턴
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
							pc.receiveDamage(_user, _random.nextInt(70) + 80, 0);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							npc.receiveDamage(_user, _random.nextInt(70) + 80);
							if (npc.getMaxHp() > 5000) {
								return;
							}
						}

						diffLevel = _user.getLevel() - targetLevel;

						if (diffLevel <= -10) {
							_shockStunDuration = empierTimeArray1[random.nextInt(empierTimeArray1.length)];
						} else if (diffLevel <= -8) {
							_shockStunDuration = empierTimeArray2[random.nextInt(empierTimeArray2.length)];
						} else if (diffLevel <= -6) {
							_shockStunDuration = empierTimeArray3[random.nextInt(empierTimeArray3.length)];
						} else if (diffLevel <= -4) {
							_shockStunDuration = empierTimeArray4[random.nextInt(empierTimeArray4.length)];
						} else if (diffLevel <= -2) {
							_shockStunDuration = empierTimeArray5[random.nextInt(empierTimeArray5.length)];
						} else if (diffLevel == 0) {
							_shockStunDuration = empierTimeArray6[random.nextInt(empierTimeArray6.length)];
						} else if (diffLevel <= 2) {
							_shockStunDuration = empierTimeArray7[random.nextInt(empierTimeArray7.length)];
						} else if (diffLevel <= 4) {
							_shockStunDuration = empierTimeArray8[random.nextInt(empierTimeArray8.length)];
						} else if (diffLevel <= 6) {
							_shockStunDuration = empierTimeArray9[random.nextInt(empierTimeArray9.length)];
						} else if (diffLevel <= 8) {
							_shockStunDuration = empierTimeArray10[random.nextInt(empierTimeArray10.length)];
						} else if (diffLevel <= 10) {
							_shockStunDuration = empierTimeArray11[random.nextInt(empierTimeArray11.length)];
						} else if (diffLevel > 10) {
							_shockStunDuration = empierTimeArray12[random.nextInt(empierTimeArray12.length)];
						}

						L1EffectSpawn.getInstance().spawnEffect(81153, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true), true);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

					break;

				case BLADE: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							if (c == null) {
								return;
							}

							if (c.getWeapon() == null) {
								return;
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
							int strdmg = 0;
							if (strdmg > 1000) {
								strdmg = 1000;
							} else {
								strdmg = _user.getAbility().getTotalStr() * 10;
							}
							pc.receiveDamage(_user, _random.nextInt(70) + strdmg, 0);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							int strdmg = 0;
							if (strdmg > 1000) {
								strdmg = 1000;
							} else {
								strdmg = _user.getAbility().getTotalStr() * 10;
							}

							npc.receiveDamage(_user, _random.nextInt(70) + strdmg);
							if (npc.getMaxHp() > 5000) {
								return;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					break;

				case SHOCK_STUN: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						// _user 가 pc 라면.
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							// 양손 여부 체크
							if (c == null) {
								return; // 시전자가 없다면 리턴
							}

							if (c.getWeapon() == null) {
								return; // 검이 없다면 리턴
							}

							if (!c.getWeapon().getItem().isTwohandedWeapon()) {
								return; // 현재 검이 양손검이 아니라면 리턴.
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
							pc.receiveDamage(_user, _random.nextInt(70) + 80, 0);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							npc.receiveDamage(_user, _random.nextInt(70) + 80);
							if (npc.getMaxHp() > 5000) {
								return;
							}
						}

						diffLevel = _user.getLevel() - targetLevel;

						if (diffLevel <= -10) {
							_shockStunDuration = stunTimeArray1[random.nextInt(stunTimeArray1.length)];
						} else if (diffLevel <= -8) {
							_shockStunDuration = stunTimeArray2[random.nextInt(stunTimeArray2.length)];
						} else if (diffLevel <= -6) {
							_shockStunDuration = stunTimeArray3[random.nextInt(stunTimeArray3.length)];
						} else if (diffLevel <= -4) {
							_shockStunDuration = stunTimeArray4[random.nextInt(stunTimeArray4.length)];
						} else if (diffLevel <= -2) {
							_shockStunDuration = stunTimeArray5[random.nextInt(stunTimeArray5.length)];
						} else if (diffLevel == 0) {
							_shockStunDuration = stunTimeArray6[random.nextInt(stunTimeArray6.length)];
						} else if (diffLevel <= 2) {
							_shockStunDuration = stunTimeArray7[random.nextInt(stunTimeArray7.length)];
						} else if (diffLevel <= 4) {
							_shockStunDuration = stunTimeArray8[random.nextInt(stunTimeArray8.length)];
						} else if (diffLevel <= 6) {
							_shockStunDuration = stunTimeArray9[random.nextInt(stunTimeArray9.length)];
						} else if (diffLevel <= 8) {
							_shockStunDuration = stunTimeArray10[random.nextInt(stunTimeArray10.length)];
						} else if (diffLevel <= 10) {
							_shockStunDuration = stunTimeArray11[random.nextInt(stunTimeArray11.length)];
						} else if (diffLevel > 10) {
							_shockStunDuration = stunTimeArray12[random.nextInt(stunTimeArray12.length)];
						}

						L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true), true);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

					break;

				case PHANTOM: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						// _user 가 pc 라면.
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							// 양손 여부 체크
							if (c == null) {
								return; // 시전자가 없다면 리턴
							}

							if (c.getWeapon() == null) {
								return; // 검이 없다면 리턴
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
							int strdmg = _user.getAbility().getTotalStr() * 5;
							if (strdmg > 1000) {
								strdmg = 1000;
							}
							pc.receiveDamage(_user, _random.nextInt(70) + strdmg, 0);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							int strdmg = _user.getAbility().getTotalStr() * 5;
							if (strdmg > 1000) {
								strdmg = 1000;
							}

							npc.receiveDamage(_user, _random.nextInt(70) + strdmg);
							if (npc.getMaxHp() > 5000) {
								return;
							}
						}

						diffLevel = _user.getLevel() - targetLevel;

						if (diffLevel <= -10) {
							_shockStunDuration = stunTimeArray1[random.nextInt(stunTimeArray1.length)];
						} else if (diffLevel <= -8) {
							_shockStunDuration = stunTimeArray2[random.nextInt(stunTimeArray2.length)];
						} else if (diffLevel <= -6) {
							_shockStunDuration = stunTimeArray3[random.nextInt(stunTimeArray3.length)];
						} else if (diffLevel <= -4) {
							_shockStunDuration = stunTimeArray4[random.nextInt(stunTimeArray4.length)];
						} else if (diffLevel <= -2) {
							_shockStunDuration = stunTimeArray5[random.nextInt(stunTimeArray5.length)];
						} else if (diffLevel == 0) {
							_shockStunDuration = stunTimeArray6[random.nextInt(stunTimeArray6.length)];
						} else if (diffLevel <= 2) {
							_shockStunDuration = stunTimeArray7[random.nextInt(stunTimeArray7.length)];
						} else if (diffLevel <= 4) {
							_shockStunDuration = stunTimeArray8[random.nextInt(stunTimeArray8.length)];
						} else if (diffLevel <= 6) {
							_shockStunDuration = stunTimeArray9[random.nextInt(stunTimeArray9.length)];
						} else if (diffLevel <= 8) {
							_shockStunDuration = stunTimeArray10[random.nextInt(stunTimeArray10.length)];
						} else if (diffLevel <= 10) {
							_shockStunDuration = stunTimeArray11[random.nextInt(stunTimeArray11.length)];
						} else if (diffLevel > 10) {
							_shockStunDuration = stunTimeArray12[random.nextInt(stunTimeArray12.length)];
						}

						if (_target instanceof L1PcInstance) {
							L1PcInstance target = (L1PcInstance) _target;
							if (_player.phantom_D) {
								int ran = random.nextInt(100) + 1;
								if (ran > 50) {
									S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
									_player.sendPackets(gfx);
									Broadcaster.broadcastPacket(_player, gfx, true);
									_player.sendPackets(new S_SkillSound(_target.getId(), 18580), true);
									Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18580), true);
									L1EffectSpawn.getInstance().spawnEffect(81147, _shockStunDuration, _target.getX(), _target.getY(), _target.getMapId());
									target.setPhantomDeathed(true);
									target.sendPackets(new S_NewSkillIcons(L1SkillId.PHANTOM_D, true, -1));
									target.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, true), true);
								} else {
									S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
									_player.sendPackets(gfx);
									Broadcaster.broadcastPacket(_player, gfx, true);
									_player.sendPackets(new S_SkillSound(_target.getId(), 18578), true);
									Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18578), true);
									L1EffectSpawn.getInstance().spawnEffect(81148, _shockStunDuration, _target.getX(), _target.getY(), _target.getMapId());
									target.setPhantomRippered(true);
									target.sendPackets(new S_NewSkillIcons(L1SkillId.PHANTOM_R, true, -1));
									target.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, true), true);
								}
							} else if (_player.phantom_R) {
								S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
								_player.sendPackets(gfx);
								Broadcaster.broadcastPacket(_player, gfx, true);
								_player.sendPackets(new S_SkillSound(_target.getId(), 18578), true);
								Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18578), true);
								L1EffectSpawn.getInstance().spawnEffect(81148, _shockStunDuration, _target.getX(), _target.getY(), _target.getMapId());
								target.setPhantomRippered(true);
								target.sendPackets(new S_NewSkillIcons(L1SkillId.PHANTOM_R, true, -1));
								target.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, true), true);
							} else {
								S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
								_player.sendPackets(gfx);
								Broadcaster.broadcastPacket(_player, gfx, true);
								_player.sendPackets(new S_SkillSound(_target.getId(), 18578), true);
								Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18578), true);
								L1EffectSpawn.getInstance().spawnEffect(81146, _shockStunDuration, _target.getX(), _target.getY(), _target.getMapId());
								target.sendPackets(new S_NewSkillIcons(L1SkillId.PHANTOM, true, -1));
							}
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							if (_player.phantom_D) {
								int ran = random.nextInt(100) + 1;
								if (ran > 70) {
									S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
									_player.sendPackets(gfx);
									Broadcaster.broadcastPacket(_player, gfx, true);
									_player.sendPackets(new S_SkillSound(_target.getId(), 18580), true);
									Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18580), true);
									L1EffectSpawn.getInstance().spawnEffect(81147, _shockStunDuration, npc.getX(), npc.getY(), npc.getMapId());
									npc.setPhantomDeathed(true);
									npc.setPhantomDTime(_shockStunDuration);
								} else {
									S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
									_player.sendPackets(gfx);
									Broadcaster.broadcastPacket(_player, gfx, true);
									_player.sendPackets(new S_SkillSound(_target.getId(), 18578), true);
									Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18578), true);
									L1EffectSpawn.getInstance().spawnEffect(81148, _shockStunDuration, npc.getX(), npc.getY(), npc.getMapId());
									npc.setPhantomRippered(true);
									npc.setPhantomRTime(_shockStunDuration);
								}
							} else if (_player.phantom_R) {
								S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
								_player.sendPackets(gfx);
								Broadcaster.broadcastPacket(_player, gfx, true);
								_player.sendPackets(new S_SkillSound(_target.getId(), 18578), true);
								Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18578), true);
								L1EffectSpawn.getInstance().spawnEffect(81148, _shockStunDuration, npc.getX(), npc.getY(), npc.getMapId());
								npc.setPhantomRippered(true);
								npc.setPhantomRTime(_shockStunDuration);
							} else {
								S_DoActionGFX gfx = new S_DoActionGFX(_player.getId(), 19);
								_player.sendPackets(gfx);
								Broadcaster.broadcastPacket(_player, gfx, true);
								_player.sendPackets(new S_SkillSound(_target.getId(), 18578), true);
								Broadcaster.broadcastPacket(_target, new S_SkillSound(_target.getId(), 18578), true);
								L1EffectSpawn.getInstance().spawnEffect(81146, _shockStunDuration, npc.getX(), npc.getY(), npc.getMapId());
							}
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					break;

				case 쉐도우스탭: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							if (c == null)
								return;
						}

						L1Location loc = L1Teleport.ShadowStep(_player, _target, 1);
						_target.dx = loc.getX();
						_target.dy = loc.getY();
						_target.dm = (short) loc.getMapId();
						_player.dh = _player.getMoveState().getHeading();
						L1Teleport.teleport(_player, _target.dx, _target.dy, _target.dm, _player.dh, false, false, 0);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
							int strdmg = _user.getAbility().getTotalStr() * 5;
							if (strdmg > 1000) {
								strdmg = 1000;
							}

							pc.receiveDamage(_user, _random.nextInt(70) + strdmg, 0);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							int strdmg = _user.getAbility().getTotalStr() * 5;
							if (strdmg > 1000) {
								strdmg = 1000;
							}

							npc.receiveDamage(_user, _random.nextInt(70) + strdmg);
							if (npc.getMaxHp() > 5000) {
								return;
							}
						}

						diffLevel = _user.getLevel() - targetLevel;

							if (diffLevel <= -10) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= -8) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= -6) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= -4) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= -2) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel == 0) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= 2) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= 4) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= 6) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= 8) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel <= 10) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							} else if (diffLevel > 10) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
							}

							_player.sendPackets(new S_SkillSound(cha.getId(), 18949), true);
							Broadcaster.broadcastPacket(_player, new S_SkillSound(_target.getId(), 18949), true);
							L1EffectSpawn.getInstance().spawnEffect(91163, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.쉐도우스탭, _shockStunDuration);
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true));
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
							npc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.쉐도우스탭, _shockStunDuration);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

					break;
					
				case PANTERA: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							if (c == null)
								return;
						}

						L1Location loc = L1Teleport.Pantera(_player, _target, 1);
						_target.dx = loc.getX();
						_target.dy = loc.getY();
						_target.dm = (short) loc.getMapId();
						_player.dh = _player.getMoveState().getHeading();
						L1Teleport.teleport(_player, _target.dx, _target.dy, _target.dm, _player.dh, false, false, 0);
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
							int strdmg = _user.getAbility().getTotalStr() * 5;
							if (strdmg > 1000) {
								strdmg = 1000;
							}

							pc.receiveDamage(_user, _random.nextInt(70) + strdmg, 0);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							int strdmg = _user.getAbility().getTotalStr() * 5;
							if (strdmg > 1000) {
								strdmg = 1000;
							}

							npc.receiveDamage(_user, _random.nextInt(70) + strdmg);
							if (npc.getMaxHp() > 5000) {
								return;
							}
						}

						diffLevel = _user.getLevel() - targetLevel;

						if (diffLevel <= -10 && !_player.Pantera_S) {
							_shockStunDuration = panteratimeArray1[random.nextInt(panteratimeArray1.length)];
						} else if (diffLevel <= -10 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray1[random.nextInt(panteraStimeArray1.length)];
						} else if (diffLevel <= -8) {
							_shockStunDuration = panteratimeArray2[random.nextInt(panteratimeArray2.length)];
						} else if (diffLevel <= -8 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray2[random.nextInt(panteraStimeArray2.length)];
						} else if (diffLevel <= -6) {
							_shockStunDuration = panteratimeArray3[random.nextInt(panteratimeArray3.length)];
						} else if (diffLevel <= -6 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray3[random.nextInt(panteraStimeArray3.length)];
						} else if (diffLevel <= -4) {
							_shockStunDuration = panteratimeArray4[random.nextInt(panteratimeArray4.length)];
						} else if (diffLevel <= -4 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray4[random.nextInt(panteraStimeArray4.length)];
						} else if (diffLevel <= -2) {
							_shockStunDuration = panteratimeArray5[random.nextInt(panteratimeArray5.length)];
						} else if (diffLevel <= -2 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray5[random.nextInt(panteraStimeArray5.length)];
						} else if (diffLevel == 0) {
							_shockStunDuration = panteratimeArray6[random.nextInt(panteratimeArray6.length)];
						} else if (diffLevel <= 0 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray6[random.nextInt(panteraStimeArray6.length)];
						} else if (diffLevel <= 2) {
							_shockStunDuration = panteratimeArray7[random.nextInt(panteratimeArray7.length)];
						} else if (diffLevel <= 2 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray7[random.nextInt(panteraStimeArray7.length)];
						} else if (diffLevel <= 4) {
							_shockStunDuration = panteratimeArray8[random.nextInt(panteratimeArray8.length)];
						} else if (diffLevel <= 4 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray8[random.nextInt(panteraStimeArray8.length)];
						} else if (diffLevel <= 6) {
							_shockStunDuration = panteratimeArray9[random.nextInt(panteratimeArray9.length)];
						} else if (diffLevel <= 6 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray9[random.nextInt(panteraStimeArray9.length)];
						} else if (diffLevel <= 8) {
							_shockStunDuration = panteratimeArray10[random.nextInt(panteratimeArray10.length)];
						} else if (diffLevel <= 8 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray10[random.nextInt(panteraStimeArray10.length)];
						} else if (diffLevel <= 10) {
							_shockStunDuration = panteratimeArray11[random.nextInt(panteratimeArray11.length)];
						} else if (diffLevel <= 10 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray11[random.nextInt(panteraStimeArray11.length)];
						} else if (diffLevel > 10) {
							_shockStunDuration = panteratimeArray12[random.nextInt(panteratimeArray12.length)];
						} else if (diffLevel > 10 && _player.Pantera_S) {
							_shockStunDuration = 2000 + panteraStimeArray12[random.nextInt(panteraStimeArray12.length)];
						}

						if (_player.Pantera_S) {
							L1EffectSpawn.getInstance().spawnEffect(81143, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							_player.sendPackets(new S_SkillSound(cha.getId(), 18608), true);
							Broadcaster.broadcastPacket(_player, new S_SkillSound(_target.getId(), 18608), true);
						} else {
							L1EffectSpawn.getInstance().spawnEffect(81145, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							_player.sendPackets(new S_SkillSound(cha.getId(), 18505), true);
							Broadcaster.broadcastPacket(_player, new S_SkillSound(_target.getId(), 18505), true);
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true), true);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

					break;

				case JUDGEMENT: {
					try {
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							// 양손 여부 체크
							if (c == null) {
								return; // 시전자가 없다면 리턴
							}

							if (c.getWeapon() == null) {
								return; // 무기가 없다면 리턴
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							int c = 0;// 기본확률
							c += (_user.getAbility().getTotalStr() / 3);

							if (c > 25) {// 최대확률
								c = 25;
							}

							pc.저지먼트 = c;
							pc.addAllTolerance(-(pc.저지먼트));
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.JUDGEMENT, true, _getBuffIconDuration));
							pc.sendPackets(new S_SystemMessage("저지 먼트 : 모든 내성이 감소합니다."));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					break;

				case AVENGER: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						// _user 가 pc 라면.
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							// 양손 여부 체크
							if (c == null) {
								return; // 시전자가 없다면 리턴
							}

							if (c.getWeapon() == null) {
								return; // 무기가 없다면 리턴
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							double per = 30.000 / 100.000;
							int avenger_HP1 = (int) ((double) pc.getMaxHp() * per);
							if (avenger_HP1 < pc.getCurrentHp()) {
								pc.receiveDamage(_user, _random.nextInt(70) + 600, 0);
							} else {
								if (pc != null) {
									_player.sendPackets(new S_SkillSound(pc.getId(), 18404), true);
									Broadcaster.broadcastPacket(_player, new S_SkillSound(pc.getId(), 18404), true);
									pc.setCurrentHp(0);
									pc.death(null);
								}
							}
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							double per = 30.000 / 100.000;
							int avenger_HP2 = (int) ((double) npc.getMaxHp() * per);
							if (npc.getBaseMaxHp() > 5000 || avenger_HP2 < npc.getCurrentHp()) {
								npc.receiveDamage(_user, _random.nextInt(70) + 600);
							} else {
								if (npc != null) {
									_player.sendPackets(new S_SkillSound(npc.getId(), 18404), true);
									Broadcaster.broadcastPacket(_player, new S_SkillSound(npc.getId(), 18404), true);
									npc.setCurrentHp(0);
									npc.setDead(true);
									npc.setActionStatus(ActionCodes.ACTION_Die);
									S_DoActionGFX da = new S_DoActionGFX(npc.getId(), ActionCodes.ACTION_Die);
									Broadcaster.broadcastPacket(npc, da);
									da = null;
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

					break;

				case ETERNITY: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						// _user 가 pc 라면.
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							// 양손 여부 체크
							if (c == null) {
								return; // 시전자가 없다면 리턴
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							if (npc.getMaxHp() > 5000) {
								return;
							}
						}

						diffLevel = _user.getLevel() - targetLevel;
						int ran = random.nextInt(100) + 1;

						if (diffLevel <= -10) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= -8) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= -6) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= -4) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= -2) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel == 0) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= 2) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= 4) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= 6) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= 8) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel <= 10) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						} else if (diffLevel > 10) {
							_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
						}

						L1EffectSpawn.getInstance().spawnEffect(81149, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.ETERNITY, true, -1));
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_DEATH_PERADO, true), true);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

					break;

				case FORCE_STUN: {
					try {
						int targetLevel = 0;
						int diffLevel = 0;
						// _user 가 pc 라면.
						if (_user != cha && _user instanceof L1PcInstance) {
							L1PcInstance c = (L1PcInstance) _user;
							// 양손 여부 체크
							if (c == null) {
								return; // 시전자가 없다면 리턴
							}

							if (c.getWeapon() == null) {
								return; // 검이 없다면 리턴
							}

							if (!c.getWeapon().getItem().isTwohandedWeapon()) {
								return; // 현재 검이 양손검이 아니라면 리턴.
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							targetLevel = pc.getLevel();
							pc.receiveDamage(_user, _random.nextInt(70) + 400, 0);
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							targetLevel = npc.getLevel();
							npc.receiveDamage(_user, _random.nextInt(70) + 400);
							if (npc.getMaxHp() > 5000) {
								return;
							}
						}

						diffLevel = _user.getLevel() - targetLevel;
						int ran = random.nextInt(100) + 1;

						if (diffLevel <= -10) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= -8) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= -6) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= -4) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= -2) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel == 0) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= 2) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= 4) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= 6) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= 8) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel <= 10) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						} else if (diffLevel > 10) {
							if (ran < 35) {
								_shockStunDuration = forcestunTimeArray1[random.nextInt(forcestunTimeArray1.length)];
								L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							} else {
								_shockStunDuration = forcestunTimeArray2[random.nextInt(forcestunTimeArray2.length)];
								L1EffectSpawn.getInstance().spawnEffect(81152, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
							}
						}

						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true), true);
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.FORCE_STUN, true, -1));
						} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
							L1NpcInstance npc = (L1NpcInstance) cha;
							npc.setParalyzed(true);
							npc.setParalysisTime(_shockStunDuration);
						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}

					break;

				case MOB_RANGESTUN_18:
				case MOB_RANGESTUN_19:

					break;

				case MOB_SHOCKSTUN_30: {
					_shockStunDuration = mobstun30TimeArray[random.nextInt(mobstun30TimeArray.length)];
					L1EffectSpawn.getInstance().spawnEffect(81162, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true), true);
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
						npc.setParalysisTime(_shockStunDuration);
					}
				}
					break;

				case 40003: // 불멸의리치, 냉혹의 아이리스 넉백스킬
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_user)) {
						if (pc == null) {
							continue;
						}

						L1Location newLocation = pc.getLocation().randomLocation(10, true);
						int newX = newLocation.getX();
						int newY = newLocation.getY();
						short mapId = (short) newLocation.getMapId();
						if (pc instanceof L1RobotInstance) {
							L1RobotInstance rob = (L1RobotInstance) pc;
							L1Teleport.ロボットテル(rob, newX, newY, (short) mapId, true);
						} else {
							pc.dx = newX;
							pc.dy = newY;
							pc.dm = mapId;
							pc.dh = pc.getMoveState().getHeading();
							pc.setTelType(9);
							pc.sendPackets(new S_SabuTell(pc), true);
						}

					}
					break;

				case 40040: // 옛셸로브 끌어오기
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						L1Location newLocation = _user.getLocation().randomLocation(2, true);
						int newX = newLocation.getX();
						int newY = newLocation.getY();
						short mapId = (short) newLocation.getMapId();
						if (pc instanceof L1RobotInstance) {
							L1RobotInstance rob = (L1RobotInstance) pc;
							L1Teleport.ロボットテル(rob, newX, newY, (short) mapId, true);
						} else {
							pc.dx = newX;
							pc.dy = newY;
							pc.dm = mapId;
							pc.dh = pc.getMoveState().getHeading();
							pc.setTelType(9);
							pc.sendPackets(new S_SabuTell(pc), true);
						}
					}
					break;

				case 40029: // 옛늑인 끌어오기
					for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_user)) {
						if (pc == null) {
							continue;
						}
						L1Location newLocation = _user.getLocation().randomLocation(2, true);
						int newX = newLocation.getX();
						int newY = newLocation.getY();
						short mapId = (short) newLocation.getMapId();

						if (pc instanceof L1RobotInstance) {
							L1RobotInstance rob = (L1RobotInstance) pc;
							L1Teleport.ロボットテル(rob, newX, newY, (short) mapId, true);
						} else {
							pc.dx = newX;
							pc.dy = newY;
							pc.dm = mapId;
							pc.dh = pc.getMoveState().getHeading();
							pc.setTelType(9);
							pc.sendPackets(new S_SabuTell(pc), true);
						}
					}
					break;

				case 40008: // 아이리스 아이스 미티어
					if (_user instanceof L1NpcInstance) {
						for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(_user, 10)) {
							if (pc == null) {
								continue;
							}

							int ran = random.nextInt(100) + 1;
							if (ran < 30) {
								_isFreeze = true;
							}

							if (_isFreeze) {
								int time = 3 * 1000;
								pc.getSkillEffectTimerSet().setSkillEffect(L1SkillId.ICE_LANCE, time);
								L1EffectSpawn.getInstance().spawnEffect(81168, time, cha.getX(), cha.getY(), cha.getMapId());
								pc.sendPackets(new S_Poison(pc.getId(), 2), true);
								Broadcaster.broadcastPacket(pc, new S_Poison(pc.getId(), 2), true);
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_FREEZE, true), true);
							}
						}
					}
					break;

				case BONE_BREAK: {
					_shockStunDuration = BonestunTime[random.nextInt(BonestunTime.length)];
					L1EffectSpawn.getInstance().spawnEffect(4500500, _shockStunDuration, cha.getX(), cha.getY(), cha.getMapId());
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_STUN, true));
					} else if (cha instanceof L1MonsterInstance || cha instanceof L1SummonInstance || cha instanceof L1PetInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.setParalyzed(true);
					}
				}
					break;

				case AM_BREAK: {
					for (L1Object obj : cha.getNearObjects().getVisibleObjects(cha, 15)) {
						// for(L1Object obj :
						// L1World.getInstance().getVisibleObjects(cha, 15)){
						if (obj != null && obj instanceof L1Character) {
							L1Character chara = (L1Character) obj;
							if (chara instanceof L1PcInstance && !((L1PcInstance) chara).isGm()) {
								detection((L1PcInstance) chara);
							} else if (chara instanceof L1NpcInstance) {
								L1NpcInstance npc = (L1NpcInstance) chara;
								int hiddenStatus = npc.getHiddenStatus();
								if (hiddenStatus == L1NpcInstance.HIDDEN_STATUS_SINK) {
									if (npc.getNpcId() != 45682) {
										npc.appearOnGround(_user);
									}
								}
							}
						}
					}
				}
					break;

				case PHANTASM: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true), true);
					}
					cha.setSleeped(true);
				}
					break;

				case WIND_SHACKLE:
					break;

				case MOB_WINDSHACKLE_1: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillIconWindShackle(pc.getId(), _getBuffIconDuration), true);
					}
				}
					break;

				case 40016:
				case CANCELLATION: {
					if (_player != null && _player.isInvisble()) {
						_player.delInvis();
					}

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.isInvisble()) {
							pc.delInvis();
						}
					}

					if (cha instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						int npcId = npc.getNpcTemplate().get_npcId();
						if (npcId == 71092) {
							if (npc.getGfxId().getGfxId() == npc.getGfxId().getTempCharGfx()) {
								npc.getGfxId().setTempCharGfx(1314);
								Broadcaster.broadcastPacket(npc, new S_ChangeShape(npc.getId(), 1314), true);
								return;
							} else {
								return;
							}
						} else if (npcId == 45640) {
							if (npc.getGfxId().getGfxId() == npc.getGfxId().getTempCharGfx()) {
								npc.setCurrentHp(npc.getMaxHp());
								npc.getGfxId().setTempCharGfx(2332);
								Broadcaster.broadcastPacket(npc, new S_ChangeShape(npc.getId(), 2332), true);
								npc.setName("$2103");
								npc.setNameId("$2103");
								Broadcaster.broadcastPacket(npc, new S_ChangeName(npc.getId(), "$2103"), true);
							} else if (npc.getGfxId().getTempCharGfx() == 2332) {
								npc.setCurrentHp(npc.getMaxHp());
								npc.getGfxId().setTempCharGfx(2755);
								Broadcaster.broadcastPacket(npc, new S_ChangeShape(npc.getId(), 2755), true);
								npc.setName("$2488");
								npc.setNameId("$2488");
								Broadcaster.broadcastPacket(npc, new S_ChangeName(npc.getId(), "$2488"), true);
							}
						} else if (npcId == 81209) {
							if (npc.getGfxId().getGfxId() == npc.getGfxId().getTempCharGfx()) {
								npc.getGfxId().setTempCharGfx(4310);
								Broadcaster.broadcastPacket(npc, new S_ChangeShape(npc.getId(), 4310), true);
								return;
							} else {
								return;
							}
						}
					}

					if (!(cha instanceof L1PcInstance)) {
						L1NpcInstance npc = (L1NpcInstance) cha;
						npc.getMoveState().setMoveSpeed(0);
						npc.getMoveState().setBraveSpeed(0);
						Broadcaster.broadcastPacket(npc, new S_SkillHaste(cha.getId(), 0, 0), true);
						Broadcaster.broadcastPacket(npc, new S_SkillBrave(cha.getId(), 0, 0), true);
						npc.setWeaponBreaked(false);
						npc.setParalyzed(false);
						npc.setParalysisTime(0);
					}

					for (int skillNum = SKILLS_BEGIN; skillNum <= SKILLS_END; skillNum++) {
						// System.out.println("캔슬 삭제 버프 번호 : " + skillNum);
						if (isNotCancelable(skillNum) && !cha.isDead()) {
							continue;
						}

						if (skillNum == SHAPE_CHANGE) { //
							if (cha instanceof L1PcInstance) {
								L1PcInstance pc = (L1PcInstance) cha;
								if (pc.getGfxId().getTempCharGfx() >= 13715 && pc.getGfxId().getTempCharGfx() <= 13745
										|| pc.getGfxId().getTempCharGfx() == 15115) {
									continue;
								} else if (pc.getSkillEffectTimerSet().GetDominationSkill()) {
									continue;
								}
							}
						}

						// TotalRank
						cha.getSkillEffectTimerSet().removeSkillEffect(skillNum);
					}

					for (int i = 0; i < 캔슬강제삭제.length; i++) {
						if (cha.getSkillEffectTimerSet().hasSkillEffect(캔슬강제삭제[i])) {
							cha.getSkillEffectTimerSet().removeSkillEffect(캔슬강제삭제[i]);
						}
					}

					for (int skillNum = STATUS_BEGIN; skillNum <= STATUS_CANCLEEND; skillNum++) {
						// System.out.println("캔슬 삭제 버프 번호 : " + skillNum);
						if (isNotCancelable(skillNum) && !cha.isDead()) {
							continue;
						}

						cha.getSkillEffectTimerSet().removeSkillEffect(skillNum);
					}

					cha.curePoison();
					cha.cureParalaysis();

					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_CharVisualUpdate(pc), true);
						Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc), true);

						if (pc.getHasteItemEquipped() > 0) {
							pc.getMoveState().setMoveSpeed(0);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
							Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
						}

						if (pc != null && pc.isInvisble()) {
							if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.INVISIBILITY)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.INVISIBILITY);
								pc.sendPackets(new S_Invis(pc.getId(), 0), true);
								Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0), true);
								pc.sendPackets(new S_Sound(147), true);
							}

							if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.BLIND_HIDING)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.BLIND_HIDING);
								pc.sendPackets(new S_Invis(pc.getId(), 0), true);
								Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 0), true);
								S_RemoveObject iv2 = new S_RemoveObject(pc.getId());
								pc.sendPackets(iv2);
								Broadcaster.broadcastPacket(pc, iv2);
							}
						}

						if (pc.is에틴인형()) {
							pc.getMoveState().setMoveSpeed(1);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 1, -1), true);
							Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 1, 0), true);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_UNDERWATER_BREATH)) {
							int timeSec = pc.getSkillEffectTimerSet().getSkillEffectTimeSec(STATUS_UNDERWATER_BREATH);
							pc.sendPackets(new S_SkillIconBlessOfEva(pc.getId(), timeSec), true);
						}

					}

					cha.getSkillEffectTimerSet().removeSkillEffect(STATUS_FREEZE);
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_CharVisualUpdate(pc), true);
						Broadcaster.broadcastPacket(pc, new S_CharVisualUpdate(pc), true);
						if (pc.isPrivateShop()) {
							pc.sendPackets(new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()), true);
							Broadcaster.broadcastPacket(pc, new S_DoActionShop(pc.getId(), ActionCodes.ACTION_Shop, pc.getShopChat()), true);
						}
					}
				}
					break;

				case TURN_UNDEAD: {
					if (undeadType == 1 || undeadType == 3) {
						dmg = cha.getCurrentHp();
					}
				}
					break;

				case MANA_DRAIN: {
					if (!cha.isManaDrain) {
						int chance = random.nextInt(10) + 10;
						drainMana = chance + (_user.getAbility().getTotalInt() / 2);
						if (cha.getResistance().getMr() >= 101) {
							drainMana -= drainMana * 0.30;
						}

						if (cha.getCurrentMp() < drainMana) {
							drainMana = cha.getCurrentMp();
						}

						if (cha instanceof L1NpcInstance) {
							cha.isManaDrain = true;
						}
					}

					if (_user instanceof L1PcInstance) {
						_player.sendPackets(new S_SkillSound(_player.getId(), 2171), true);
						Broadcaster.broadcastPacket(_player, new S_SkillSound(_player.getId(), 2171), true);
					} else {
						Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 2171), true);
					}
				}
					break;

				case 40015:
				case WEAPON_BREAK: {
					if (_calcType == PC_PC || _calcType == NPC_PC) {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							L1ItemInstance weapon = pc.getWeapon();
							if (weapon != null) {
								int weaponDamage = random.nextInt(_user.getAbility().getTotalInt() / 3) + 1;
								pc.sendPackets(new S_ServerMessage(268, weapon.getLogName()), true);
								pc.getInventory().receiveDamage(weapon, weaponDamage);
							}
						}
					} else {
						((L1NpcInstance) cha).setWeaponBreaked(true);
					}
				}
					break;

				case FOG_OF_SLEEPING: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_SkillSound(pc.getId(), 9218), true);
						Broadcaster.broadcastPacket(pc, new S_SkillSound(pc.getId(), 9218), true);
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_SLEEP, true), true);
					}

					cha.setSleeped(true);
				}
					break;

				case STATUS_FREEZE: {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_BIND, true), true);
					}
				}
					break;

				default:
					break;
				}

				if (_calcType == PC_PC || _calcType == NPC_PC) { // 여기부터
					switch (_skillId) {
					case TELEPORT:
					case MASS_TELEPORT: {
						L1PcInstance pc = (L1PcInstance) cha;
						Random random = new Random();
						// L1BookMark bookm = pc.getBookMark(_bookmarkId);
						L1BookMark bookm = pc.getBookMark(_bookmarkX, _bookmarkY, _bookmarkMapId);
						if (bookm != null) {
							if (pc.getMap().isEscapable() || pc.isGm()) {
								L1Location newLocation = L1Location.BookmarkLoc(pc, bookm);
								int newX = newLocation.getX();
								int newY = newLocation.getY();
								short mapId = bookm.getMapId();
								L1Map map = L1WorldMap.getInstance().getMap(mapId);

								if (mapId == 4 && ((newX >= 33331 && newX <= 33341 && newY >= 32430 && newY <= 32441)
										|| (newX >= 33258 && newX <= 33267 && newY >= 32396 && newY <= 32407)
										|| (newX >= 33388 && newX <= 33397 && newY >= 32339 && newY <= 32350)
										|| (newX >= 33443 && newX <= 33483 && newY >= 32315 && newY <= 32357))) {
									newX = pc.getX();
									newY = pc.getY();
									mapId = pc.getMapId();
								}

								if (_skillId == MASS_TELEPORT) {
									byte count = 0;
									for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 3)) {
										if (pc.getClanid() != 0 && member.getClanid() == pc.getClanid() && member.getId() != pc.getId()
												&& !member.isPrivateShop()) {
											count++;
											int newX2 = newX + random.nextInt(3) + 1;
											int newY2 = newY + random.nextInt(3) + 1;
											boolean ck = false;

											if (L1CastleLocation.checkInAllWarArea(newX2, newY2, mapId)) {
												ck = true;
											} else if (L1HouseLocation.isInHouse(newX2, newY2, mapId)) {
												ck = true;
											} else if (newX2 >= 32704 && newX2 <= 32835 && newY2 >= 33110 && newY2 <= 33234 && mapId == 4) { // 샌드웜지역
												ck = true;
											} else if ((newX2 >= 33472 && newX2 <= 33536) && (newY2 >= 32838 && newY2 <= 32876) && mapId == 4) { // 버경장
												ck = true;
											} else if (mapId == 4 && ((newX2 >= 33331 && newX2 <= 33341 && newY2 >= 32430 && newY2 <= 32441)
													|| (newX2 >= 33258 && newX2 <= 33267 && newY2 >= 32396 && newY2 <= 32407)
													|| (newX2 >= 34197 && newX2 <= 34302 && newY2 >= 33327 && newY2 <= 33533)
													|| (newX2 >= 33453 && newX2 <= 33468 && newY2 >= 32331 && newY2 <= 32341) // 황혼의산맥
													|| (newX2 >= 33388 && newX2 <= 33397 && newY2 >= 32339 && newY2 <= 32350) // 아덴의한국민
													|| (newX2 >= 33464 && newX2 <= 33531 && newY2 >= 33168 && newY2 <= 33248) // ||
											/*
											 * (newX2 >= 33443 && newX2 <= 33483 && newY2 >= 32315 && newY2 <= 32357)
											 */) /* && !pc.isGm() */) {
												ck = true;
											}

											if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2) && !ck) {
												L1Teleport.teleport(member, newX2, newY2, mapId, member.getMoveState().getHeading(), true, true);
											} else {
												L1Teleport.teleport(member, newX, newY, mapId, member.getMoveState().getHeading(), true, true);
											}
										}
									}

									if (count > 0) {

										for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 3)) {
											if (pc.getClanid() != 0 && member.getClanid() == pc.getClanid() && member.getId() != pc.getId()
													&& !member.isPrivateShop()) {
												member.sendPackets(new S_ServerMessage(3655, pc.getName(), "" + count), true);
											}
										}

										pc.sendPackets(new S_ServerMessage(3655, pc.getName(), "" + count), true);
									}
								}

								if (pc.getInventory().checkItem(20288) || pc.getInventory().checkItem(5001130)) {// 순간이동지배반지
									L1Teleport.teleport(pc, newX, newY, mapId, pc.getMoveState().getHeading(), true, true);
								} else {
									int newX2 = (newX - 6) + random.nextInt(12);
									int newY2 = (newY - 6) + random.nextInt(12);

									int aaa = _random.nextInt(100);

									boolean ck = false;
									if (L1CastleLocation.checkInAllWarArea(newX2, newY2, mapId)) {
										ck = true;
									} else if (L1HouseLocation.isInHouse(newX2, newY2, mapId)) {
										ck = true;
									} else if (newX2 >= 32704 && newX2 <= 32835 && newY2 >= 33110 && newY2 <= 33234 && mapId == 4) {// 샌드웜지역
										ck = true;
									} else if ((newX2 >= 33472 && newX2 <= 33536) && (newY2 >= 32838 && newY2 <= 32876) && mapId == 4) { // 버경장
										ck = true;
									} else if (mapId == 4 && ((newX2 >= 33331 && newX2 <= 33341 && newY2 >= 32430 && newY2 <= 32441)
											|| (newX2 >= 33258 && newX2 <= 33267 && newY2 >= 32396 && newY2 <= 32407)
											|| (newX2 >= 34197 && newX2 <= 34302 && newY2 >= 33327 && newY2 <= 33533) // 황혼의산맥
											|| (newX2 >= 33453 && newX2 <= 33468 && newY2 >= 32331 && newY2 <= 32341) // 아덴의한국민
											|| (newX2 >= 33388 && newX2 <= 33397 && newY2 >= 32339 && newY2 <= 32350)
											|| (newX2 >= 33464 && newX2 <= 33531 && newY2 >= 33168 && newY2 <= 33248))) {
										ck = true;
									}

									if (aaa < 50) {// 요정 바보텔 확률
										ck = true;
										newX = pc.getX();
										newY = pc.getY();
									}

									if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2) && !ck) {
										L1Teleport.teleport(pc, newX2, newY2, mapId, pc.getMoveState().getHeading(), true, true);
									} else {
										L1Teleport.teleport(pc, pc.getX(), pc.getY(), mapId, pc.getMoveState().getHeading(), true, true);
									}
								}

							} else {
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
								pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
							}

						} else {
							if ((pc.getMap().isTeleportable(pc.getX(), pc.getY()) && pc.getMap().isTeleportable()) || pc.isGm()
									|| ((pc.getMapId() == 101 || pc.getMapId() == 102 || pc.getMapId() == 103 || pc.getMapId() == 104 || pc.getMapId() == 105
											|| pc.getMapId() == 106 || pc.getMapId() == 107 || pc.getMapId() == 108 || pc.getMapId() == 109
											|| pc.getMapId() == 110 || pc.getMapId() == 12852 || pc.getMapId() == 12853 || pc.getMapId() == 12854
											|| pc.getMapId() == 12855 || pc.getMapId() == 12856 || pc.getMapId() == 12857 || pc.getMapId() == 12858
											|| pc.getMapId() == 12859 || pc.getMapId() == 12860 || pc.getMapId() == 12861) && pc.is오만텔())
									|| ((pc.getMapId() == 15410 || pc.getMapId() == 15420 || pc.getMapId() == 15430 || pc.getMapId() == 15440) && pc.is지배이반텔())
									|| (!GameTimeClock.getInstance().getGameTime().isNight() && pc.getMapId() == 54)) {
								L1Location newLocation = pc.getLocation().randomLocation(200, true);
								int newX = newLocation.getX();
								int newY = newLocation.getY();
								short mapId = (short) newLocation.getMapId();
								L1Map map = L1WorldMap.getInstance().getMap(mapId);

								if (_skillId == MASS_TELEPORT) {
									byte count = 0;
									for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 3)) {
										if (pc.getClanid() != 0 && member.getClanid() == pc.getClanid() && member.getId() != pc.getId()
												&& !member.isPrivateShop()) {
											count++;
											int newX2 = newX + random.nextInt(3) + 1;
											int newY2 = newY + random.nextInt(3) + 1;
											boolean ck = false;
											if (L1CastleLocation.checkInAllWarArea(newX2, newY2, mapId)) {
												ck = true;
											} else if (L1HouseLocation.isInHouse(newX2, newY2, mapId)) {
												ck = true;
											} else if (newX2 >= 32704 && newX2 <= 32835 && newY2 >= 33110 && newY2 <= 33234 && mapId == 4) {// 샌드웜지역
												ck = true;
											} else if ((newX2 >= 33472 && newX2 <= 33536) && (newY2 >= 32838 && newY2 <= 32876) && mapId == 4) {// 버경장
												ck = true;
											} else if (mapId == 4 && ((newX2 >= 33331 && newX2 <= 33341 && newY2 >= 32430 && newY2 <= 32441)
													|| (newX2 >= 33258 && newX2 <= 33267 && newY2 >= 32396 && newY2 <= 32407)
													|| (newX2 >= 34197 && newX2 <= 34302 && newY2 >= 33327 && newY2 <= 33533)
													|| (newX2 >= 33453 && newX2 <= 33468 && newY2 >= 32331 && newY2 <= 32341) // 황혼의산맥
													|| (newX2 >= 33388 && newX2 <= 33397 && newY2 >= 32339 && newY2 <= 32350) // 아덴의한국민
													|| (newX2 >= 33464 && newX2 <= 33531 && newY2 >= 33168 && newY2 <= 33248))) {
												ck = true;
											}

											if (map.isInMap(newX2, newY2) && map.isPassable(newX2, newY2) && !ck) {
												L1Teleport.teleport(member, newX2, newY2, mapId, member.getMoveState().getHeading(), true, true);
											} else {
												L1Teleport.teleport(member, newX, newY, mapId, member.getMoveState().getHeading(), true, true);
											}
										}
									}

									if (count > 0) {
										for (L1PcInstance member : L1World.getInstance().getVisiblePlayer(pc, 3)) {
											if (pc.getClanid() != 0 && member.getClanid() == pc.getClanid() && member.getId() != pc.getId()
													&& !member.isPrivateShop()) {
												pc.sendPackets(new S_ServerMessage(3655, pc.getName(), "" + count), true);
											}
										}

										pc.sendPackets(new S_ServerMessage(3655, pc.getName(), "" + count), true);
									}
								}

								L1Teleport.teleport(pc, newX, newY, mapId, pc.getMoveState().getHeading(), true, true);
							} else {
								pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
								pc.sendPackets(new S_ServerMessage(276), true);
							}
						}
					}
						break;

					case TELEPORT_TO_MOTHER: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getMap().isEscapable() || pc.isGm()) {
							L1Teleport.teleport(pc, 33051, 32337, (short) 4, 5, true, true);
						} else {
							pc.sendPackets(new S_Paralysis(S_Paralysis.TYPE_TELEPORT_UNLOCK, false), true);
							pc.sendPackets(new S_ServerMessage(647), true);
						}
					}
						break;

					case BRING_STONE: {
						L1PcInstance pc = (L1PcInstance) cha;
						Random random = new Random();
						L1ItemInstance item = pc.getInventory().getItem(_itemobjid);
						if (item != null) {
							int dark = (int) (10 + (pc.getLevel() * 0.8) + (pc.getAbility().getTotalWis() - 6) * 1.2);
							int brave = (int) (dark / 2.1);
							int wise = (int) (brave / 2.0);
							int kayser = (int) (wise / 1.9);
							int chance = random.nextInt(100) + 1;
							if (item.getItem().getItemId() == 40320) {
								pc.getInventory().removeItem(item, 1);
								if (dark >= chance) {
									pc.getInventory().storeItem(40321, 1);
									pc.sendPackets(new S_ServerMessage(403, "$2475"), true);
								} else {
									pc.sendPackets(new S_ServerMessage(280), true);
								}
							} else if (item.getItem().getItemId() == 40321) {
								pc.getInventory().removeItem(item, 1);
								if (brave >= chance) {
									pc.getInventory().storeItem(40322, 1);
									pc.sendPackets(new S_ServerMessage(403, "$2476"), true);
								} else {
									pc.sendPackets(new S_ServerMessage(280), true);
								}
							} else if (item.getItem().getItemId() == 40322) {
								pc.getInventory().removeItem(item, 1);
								if (wise >= chance) {
									pc.getInventory().storeItem(40323, 1);
									pc.sendPackets(new S_ServerMessage(403, "$2477"), true);
								} else {
									pc.sendPackets(new S_ServerMessage(280), true);
								}
							} else if (item.getItem().getItemId() == 40323) {
								pc.getInventory().removeItem(item, 1);
								if (kayser >= chance) {
									pc.getInventory().storeItem(40324, 1);
									pc.sendPackets(new S_ServerMessage(403, "$2478"), true);
								} else {
									pc.sendPackets(new S_ServerMessage(280), true);
								}
							}
						}
					}
						break;

					case SUMMON_MONSTER: {
						L1PcInstance pc = (L1PcInstance) cha;
						int[] summons;
						if ((pc.getMap().isRecallPets() && pc.getMapId() != 781 && pc.getMapId() != 782) || pc.isGm()) {
							if (pc.getInventory().checkEquipped(20284) || pc.getInventory().checkEquipped(120284)) {
								summonMonster(pc, _targetID);
							} else {
								summons = new int[] { 81210 };
								int summonid = summons[0];
								int summoncost = 25;
								L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
								L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
								summon.setPetcost(summoncost);
								S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), 19);
								pc.sendPackets(gfx);
							}
						} else {
							pc.sendPackets(new S_ServerMessage(79));
						}
					}
						break;

					case LESSER_ELEMENTAL:
					case GREATER_ELEMENTAL: {
						L1PcInstance pc = (L1PcInstance) cha;
						int attr = pc.getElfAttr();
						if (attr != 0) {
							if (pc.getMap().isRecallPets() || pc.isGm()) {
								int petcost = 0;
								for (Object pet : pc.getPetList()) {
									petcost += ((L1NpcInstance) pet).getPetcost();
								}

								if (petcost == 0) {
									int summonid = 0;
									int summons[];

									if (_skillId == LESSER_ELEMENTAL) {
										summons = new int[] { 45306, 45303, 45304, 45305 };
									} else {
										summons = new int[] { 81053, 81050, 81051, 81052 };
									}

									int npcattr = 1;
									for (int i = 0; i < summons.length; i++) {
										if (npcattr == attr) {
											summonid = summons[i];
											i = summons.length;
										}
										npcattr *= 2;
									}

									if (summonid == 0) {
										Random random = new Random();
										int k3 = random.nextInt(4);
										summonid = summons[k3];
									}

									L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
									L1SummonInstance summon = new L1SummonInstance(npcTemp, pc);
									summon.setPetcost(pc.getAbility().getTotalCha() + 7);
									summons = null;
								}
							} else {
								pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
							}
						}
					}
						break;
					// 이부분은 전체적으로 처리를 하기때문에 제외함

					case ABSOLUTE_BARRIER: {
						L1PcInstance pc = (L1PcInstance) cha;
					}
						break;

					case 뫼비우스: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.뫼비우스, true, 5));
						pc.sendPackets(new S_SystemMessage("뫼비우스: 원거리 보호 결계(행동 제약을 받음)."));
					}
						break;

					case LIGHT:
						break;

					case GLOWING_AURA: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(5);
						pc.addDmgup(5);
						pc.sendPackets(new S_SPMR(pc), true);
						pc.sendPackets(new S_SkillIconAura(113, _getBuffIconDuration), true);
					}
						break;

					case SHINING_AURA: { // 스킬 수정 by white
						L1PcInstance pc = (L1PcInstance) cha;

						if (pc.isInParty()) {
							for (L1PcInstance partyMember : pc.getParty().getMembers()) {

								// 시전자랑 파티원이랑 거리 확인
								if (pc.getLocation().getTileDistance(partyMember.getLocation()) <= 14) {
									// 파티원이 죽은 상태이면 pass
									if (partyMember.isDead()) {
										continue;
									}

									if (pc == partyMember) {
										// 시전자
										partyMember.getAC().addAc(-8);
									} else {
										// 파티원
										partyMember.getAC().addAc(-4);
									}

									// 스킬 이펙트 및 사운드 출력
									partyMember.sendPackets(new S_SkillSound(partyMember.getId(), 3941), true);
									partyMember.broadcastPacket(new S_SkillSound(partyMember.getId(), 3941));

									// 스킬 아이콘 표시 패킷 전송
									partyMember.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSkillIconAura, SHINING_AURA, _getBuffIconDuration), true);

									// 샤이닝 실드 버프 효과 적용
									partyMember.sendPackets(new S_OwnCharAttrDef(partyMember));
								}
							}
						} else {
							// 시전자 -8방 추가
							pc.getAC().addAc(-8);

							// 스킬 이펙트 및 사운드 출력
							pc.sendPackets(new S_SkillSound(pc.getId(), 3941), true);
							pc.broadcastPacket(new S_SkillSound(pc.getId(), 3941));

							// 샤이닝 실드 버프 아이콘 표시
							pc.sendPackets(new S_SMPacketBox(S_SMPacketBox.RMSkillIconAura, SHINING_AURA, _getBuffIconDuration), true);
							// 샤이닝 실드 버프 효과 적용
							pc.sendPackets(new S_OwnCharAttrDef(pc));
						}
					}
						break;

					case BRAVE_AURA: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.BRAVE_AURA, true, _getBuffIconDuration), true);
					}
						break;

					case SHIELD: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-2);
						pc.sendPackets(new S_SkillIconShield(2, _getBuffIconDuration), true);
					}
						break;

					case SHADOW_ARMOR: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addMr(5);
						pc.sendPackets(new S_SPMR(pc), true);
					}
						break;

					case DRESS_DEXTERITY: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_덱업6)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_덱업6);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_덱업7)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_덱업7);
						}

						pc.getAbility().addAddedDex((byte) 3);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						pc.sendPackets(new S_Dexup(pc, 2, _getBuffIconDuration), true);
					}
						break;

					case DRESS_MIGHTY: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업6)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_힘업6);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업7)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_힘업7);
						}

						pc.getAbility().addAddedStr((byte) 3);
						pc.sendPackets(new S_Strup(pc, 2, _getBuffIconDuration), true);
					}
						break;

					case SHADOW_FANG: {
						L1PcInstance pc = (L1PcInstance) cha;
						cha.addDmgup(5);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.SHADOW_FANG, true, 192));
					}
						break;

					case ENCHANT_WEAPON: {
						L1PcInstance pc = (L1PcInstance) cha;
						cha.addDmgup(2);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.ENCHANT_WEAPON, true, 1200));

					}
						break;

					case BLESS_WEAPON: {
						if (!(cha instanceof L1PcInstance)) {
							return;
						}

						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getWeapon() == null) {
							useok = false;
							pc.sendPackets(new S_SystemMessage("아무일도 일어나지 않았습니다."), true);
							return;
						}

						cha.addDmgup(2);
						cha.addHitup(2);
						cha.addBowHitup(2);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.BLESS_WEAPON, true, _getBuffIconDuration));
					}
						break;

					case BLESSED_ARMOR: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-3);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.BLESSED_ARMOR, true, 1800));
					}
						break;

					case HOLY_WEAPON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.HOLY_WEAPON, true, 1200));
					}
						break;

					case EARTH_BLESS: {
						L1PcInstance pc = (L1PcInstance) cha;
					}
						break;

					case RESIST_MAGIC: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getResistance().addMr(10);
						pc.sendPackets(new S_SPMR(pc), true);
					}
						break;

					case CLEAR_MIND: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedInt((byte) 1);
						pc.getAbility().addAddedDex((byte) 1);
						pc.getAbility().addAddedStr((byte) 1);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.CLEAR_MIND, true, 1200));
						pc.sendPackets(new S_SystemMessage("클리어마인드: STR+1,DEX+1,INT+1 ."));
					}
						break;

					case BODY_TO_MIND: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.setCurrentMp(pc.getCurrentMp() + 2);
					}
						break;

					case BLOODY_SOUL: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.setCurrentMp(pc.getCurrentMp() + 15);// 20
					}
						break;

					case ELEMENTAL_PROTECTION: {
						L1PcInstance pc = (L1PcInstance) cha;
						int attr = pc.getElfAttr();

						if (attr == 1) {
							pc.getResistance().addEarth(50);
						} else if (attr == 2) {
							pc.getResistance().addFire(50);
						} else if (attr == 4) {
							pc.getResistance().addWater(50);
						} else if (attr == 8) {
							pc.getResistance().addWind(50);
						} else if (attr == 21) {
							pc.getResistance().addFire(50);
							pc.getResistance().addEarth(50);
						} else if (attr == 24) {
							pc.getResistance().addFire(50);
							pc.getResistance().addWater(50);
						} else if (attr == 28) {
							pc.getResistance().addFire(50);
							pc.getResistance().addWind(50);
						} else if (attr == 41) {
							pc.getResistance().addWater(50);
							pc.getResistance().addEarth(50);
						} else if (attr == 48) {
							pc.getResistance().addWater(50);
							pc.getResistance().addWind(50);
						} else if (attr == 81) {
							pc.getResistance().addWind(50);
							pc.getResistance().addEarth(50);
						}
					}
						break;

					case INVISIBILITY: {
						L1PcInstance pc = (L1PcInstance) cha;

						for (L1DollInstance doll : pc.getDollList()) {
							doll.deleteDoll();
							pc.sendPackets(new S_SkillIconGFX(56, 0));
							pc.sendPackets(new S_OwnCharStatus(pc));
						}

						pc.sendPackets(new S_Invis(pc.getId(), 1));
						Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 1));

						for (L1PcInstance pc2 : L1World.getInstance().getVisiblePlayer(pc)) {
							if (pc2.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE)
									&& pc2.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.CURSE_BLIND)) {
								pc2.sendPackets(new S_OtherCharPacks(pc, pc2, true));
							}
						}

					}
						break;

					case BLIND_HIDING: {
						L1PcInstance pc = (L1PcInstance) cha;

						for (L1DollInstance doll : pc.getDollList()) {
							doll.deleteDoll();
							pc.sendPackets(new S_SkillIconGFX(56, 0));
							pc.sendPackets(new S_OwnCharStatus(pc));
						}

						pc.sendPackets(new S_Invis(pc.getId(), 2));
						Broadcaster.broadcastPacket(pc, new S_Invis(pc.getId(), 2));

						if (pc.isInParty()) {
							for (L1PcInstance tar : L1World.getInstance().getVisiblePlayer(pc)) {
								if (pc.getParty().isMember(tar)) {
									tar.sendPackets(new S_OtherCharPacks(pc, tar, true));
								}
							}
						}

						for (L1PcInstance pc2 : L1World.getInstance().getVisiblePlayer(pc)) {
							if (pc2.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_FLOATING_EYE)
									&& pc2.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.CURSE_BLIND)) {
								pc2.sendPackets(new S_OtherCharPacks(pc, pc2, true));
							}
						}
					}
						break;

					case IRON_SKIN: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-10);
						pc.sendPackets(new S_SkillIconShield(10, _getBuffIconDuration), true);
					}
						break;

					case FIRE_SHIELD: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-4);
						pc.sendPackets(new S_SkillIconShield(4, _getBuffIconDuration), true);
					}
						break;

					case 마제스티: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.마제스티, true, 300));
					}
						break;

					case 샤이닝아머:
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.sendPackets(new S_OwnCharStatus2(pc));
							pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
							pc.sendPackets(new S_CharVisualUpdate(pc));
							pc.sendPackets(new S_NewSkillIcons(L1SkillId.샤이닝아머, true, 300));

						}
						break;

					case cyclone: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.cyclone, true, 960));
						pc.sendPackets(new S_SystemMessage("싸이 클론:일정 확률로 원거리 대미지 1.5배."));
					}
						break;

					case 포커스웨이브: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_FRUIT);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
							pc.getMoveState().setBraveSpeed(0);
						}

						pc.getMoveState().setBraveSpeed(1);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 10, _getBuffIconDuration), true);
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 10, 0), true);
					}
						break;

					case PHYSICAL_ENCHANT_STR: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업6)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_힘업6);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_힘업7)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_힘업7);
						}
						pc.getAbility().addAddedStr((byte) 5);
						pc.sendPackets(new S_Strup(pc, 5, _getBuffIconDuration), true);
					}
						break;

					case PHYSICAL_ENCHANT_DEX: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_덱업6)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_덱업6);
						}

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_덱업7)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_덱업7);
						}

						pc.getAbility().addAddedDex((byte) 5);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						pc.sendPackets(new S_Dexup(pc, 5, _getBuffIconDuration), true);
					}
						break;

					case FIRE_WEAPON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(4);
						pc.addDmgup(2);
						pc.sendPackets(new S_SkillIconAura(147, _getBuffIconDuration), true);
					}
						break;

					case FIRE_BLESS: {
						L1PcInstance pc = (L1PcInstance) cha;

						if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_ELFBRAVE)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(STATUS_ELFBRAVE);
						}

						pc.getMoveState().setBraveSpeed(1);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 1, _getBuffIconDuration), true);
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0), true);

					}
						break;

					case BURNING_WEAPON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(6);
						pc.addHitup(6);
						pc.sendPackets(new S_SkillIconAura(162, _getBuffIconDuration), true);
					}
						break;

					case 포커스스피릿츠: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(5);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.포커스스피릿츠, true, 300));
						pc.sendPackets(new S_SystemMessage("포커스 스피릿츠: 마법 치명타 +5%."));
					}
						break;

					case WIND_SHOT: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addBowHitup(4);
						pc.sendPackets(new S_SkillIconAura(148, _getBuffIconDuration), true);
					}
						break;

					case STORM_EYE: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addBowHitup(2);
						pc.addBowDmgup(3);
						pc.sendPackets(new S_SkillIconAura(155, _getBuffIconDuration), true);
					}
						break;

					case STORM_SHOT: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addBowDmgup(6);
						pc.addBowHitup(3);
						pc.sendPackets(new S_SkillIconAura(165, _getBuffIconDuration), true);
					}
						break;

					case BERSERKERS: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(10);
						pc.addDmgup(5);
						pc.addHitup(2);
					}
						break;

					case SCALES_EARTH_DRAGON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-3);
						pc.sendPackets(new S_OwnCharAttrDef(pc), true);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.SCALES_EARTH_DRAGON, true, 600));
						pc.sendPackets(new S_SystemMessage("각성-지룡의 결속이 느껴집니다."));
					}
						break;

					case SCALES_WATER_DRAGON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.SCALES_WATER_DRAGON, true, 600));
						pc.sendPackets(new S_SystemMessage("각성-수룡의 결속이 느껴집니다."));
					}
						break;

					case SCALES_FIRE_DRAGON: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addTechniqueTolerance(10);
						pc.addHitup(5);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.SCALES_FIRE_DRAGON, true, 600));
						pc.sendPackets(new S_SystemMessage("각성-화룡의 결속이 느껴집니다."));
					}
						break;

					case INSIGHT: { // 인사이트
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAbility().addAddedStr((byte) 1);
						pc.getAbility().addAddedDex((byte) 1);
						pc.getAbility().addAddedCon((byte) 1);
						pc.getAbility().addAddedInt((byte) 1);
						pc.getAbility().addAddedWis((byte) 1);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
						pc.resetBaseMr();
					}
						break;

					case ADVANCE_SPIRIT: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.setAdvenHp(pc.getBaseMaxHp() / 5);
						pc.setAdvenMp(pc.getBaseMaxMp() / 5);
						pc.addMaxHp(pc.getAdvenHp());
						pc.addMaxMp(pc.getAdvenMp());
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.ADVANCE_SPIRIT, true, 1200));
						if (pc.isInParty()) {
							pc.getParty().updateMiniHP(pc);
						}
					}
						break;

					case GREATER_HASTE: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getHasteItemEquipped() > 0) {
							continue;
						}

						if (pc.getMoveState().getMoveSpeed() != 2) {
							if (pc.getSkillEffectTimerSet().hasSkillEffect(HASTE)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(HASTE);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
								Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
								pc.getMoveState().setMoveSpeed(0);
							} else if (pc.getSkillEffectTimerSet().hasSkillEffect(GREATER_HASTE)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(GREATER_HASTE);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
								Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
								pc.getMoveState().setMoveSpeed(0);
							} else if (pc.getSkillEffectTimerSet().hasSkillEffect(STATUS_HASTE)) {
								pc.getSkillEffectTimerSet().killSkillEffectTimer(STATUS_HASTE);
								pc.sendPackets(new S_SkillHaste(pc.getId(), 0, 0), true);
								Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 0, 0), true);
								pc.getMoveState().setMoveSpeed(0);
							}
							pc.setDrink(false);
							pc.getMoveState().setMoveSpeed(1);
							pc.sendPackets(new S_SkillHaste(pc.getId(), 1, _getBuffIconDuration), true);
							Broadcaster.broadcastPacket(pc, new S_SkillHaste(pc.getId(), 1, 0), true);
						} else {
							int skillNum = 0;

							if (pc.getSkillEffectTimerSet().hasSkillEffect(SLOW)) {
								skillNum = SLOW;
							} else if (pc.getSkillEffectTimerSet().hasSkillEffect(MOB_SLOW_1)) {
								skillNum = MOB_SLOW_1;
							} else if (pc.getSkillEffectTimerSet().hasSkillEffect(MOB_SLOW_18)) {
								skillNum = MOB_SLOW_18;
							}

							if (skillNum != 0) {
								pc.getSkillEffectTimerSet().removeSkillEffect(skillNum);
								pc.getSkillEffectTimerSet().removeSkillEffect(GREATER_HASTE);
								pc.getMoveState().setMoveSpeed(0);
								continue;
							}
						}
					}
						break;

					case HOLY_WALK:
					case MOVING_ACCELERATION: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getMoveState().setBraveSpeed(4);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 4, _getBuffIconDuration), true);
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 4, 0), true);
					}
						break;

					case WIND_WALK: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.sendPackets(new S_NewSkillIcons(L1SkillId.WIND_WALK, true, 128));
						pc.sendPackets(new S_SystemMessage("이글 아이: 원거리 치명타 2% 증가."));
					}
						break;

					case BLOOD_LUST: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_FRUIT);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
							pc.getMoveState().setBraveSpeed(0);
						}

						pc.getMoveState().setBraveSpeed(1);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 6, _getBuffIconDuration), true);
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 6, 0), true);
					}
						break;

					case 샌드스톰: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_FRUIT);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
							pc.getMoveState().setBraveSpeed(0);
						}

						pc.getMoveState().setBraveSpeed(1);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 1, _getBuffIconDuration), true);
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 1, 0), true);
					}
						break;

					case 허리케인: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_FRUIT)) {
							pc.getSkillEffectTimerSet().killSkillEffectTimer(L1SkillId.STATUS_FRUIT);
							pc.sendPackets(new S_SkillBrave(pc.getId(), 0, 0), true);
							pc.getMoveState().setBraveSpeed(0);
						}

						pc.getMoveState().setBraveSpeed(1);
						pc.sendPackets(new S_SkillBrave(pc.getId(), 9, _getBuffIconDuration), true);
						Broadcaster.broadcastPacket(pc, new S_SkillBrave(pc.getId(), 9, 0), true);
					}
						break;

					case STATUS_TIKAL_BOSSJOIN: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(5);
						pc.addDmgup(10);
						pc.addBowHitup(5);
						pc.addBowDmgup(10);
						pc.getAbility().addAddedStr((byte) 3);
						pc.getAbility().addAddedDex((byte) 3);
						pc.getAbility().addAddedCon((byte) 3);
						pc.getAbility().addAddedInt((byte) 3);
						pc.getAbility().addAddedWis((byte) 3);
						pc.getAbility().addSp(3);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					}
						break;

					case STATUS_TIKAL_BOSSDIE: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(5);
						pc.addDmgup(5);
						pc.addBowHitup(5);
						pc.addBowDmgup(5);
						pc.getAbility().addAddedStr((byte) 2);
						pc.getAbility().addAddedDex((byte) 2);
						pc.getAbility().addAddedCon((byte) 2);
						pc.getAbility().addAddedInt((byte) 2);
						pc.getAbility().addAddedWis((byte) 2);
						pc.getAbility().addSp(1);
					}
						break;

					case LIND_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addFearTolerance(5);
							pc.Add_Er(10);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;

					case FAFU_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpiritTolerance(5);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;
					case ANTA_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDragonLangTolerance(5);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;
					case VALA_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addTechniqueTolerance(5);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;
					case BIRTH_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpiritTolerance(5);
							pc.addDragonLangTolerance(5);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;
					case SHAPE_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpiritTolerance(5);
							pc.addDragonLangTolerance(5);
							pc.addFearTolerance(5);
							pc.Add_Er(10);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;
					case LIFE_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpiritTolerance(5);
							pc.addDragonLangTolerance(5);
							pc.addFearTolerance(5);
							pc.Add_Er(10);
							pc.addDmgup(2);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;
					case HALPAS_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addDamageReductionByArmor(5);
							pc.getResistance().addMr(10);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;
					case NEVER_MAAN: {
						if (cha instanceof L1PcInstance) {
							L1PcInstance pc = (L1PcInstance) cha;
							pc.addSpiritTolerance(5);
							pc.addDragonLangTolerance(5);
							pc.addFearTolerance(5);
							pc.Add_Er(10);
							pc.addDmgup(2);
							pc.addBowDmgup(2);
							pc.addDamageReductionByArmor(5);
							pc.getResistance().addMr(10);
							pc.sendPackets(new S_MaanIcons(_skillId, true, _getBuffIconDuration));
						}
					}
						break;
					case STATUS_COMA_3: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_COMA_5)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_COMA_5);
						}
						pc.getAC().addAc(-3);
						pc.addHitup(3);
						pc.getAbility().addAddedStr((byte) 5);
						pc.getAbility().addAddedDex((byte) 5);
						pc.getAbility().addAddedCon((byte) 1);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					}
						break;

					case STATUS_COMA_5: {
						L1PcInstance pc = (L1PcInstance) cha;
						if (pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_COMA_3)) {
							pc.getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_COMA_3);
						}
						pc.getAC().addAc(-8);
						pc.addHitup(5);
						pc.getAbility().addAddedStr((byte) 5);
						pc.getAbility().addAddedDex((byte) 5);
						pc.getAbility().addAddedCon((byte) 1);
						pc.getAbility().addSp(1);
						pc.sendPackets(new S_SPMR(pc), true);
						pc.sendPackets(new S_PacketBox(S_PacketBox.char_ER, pc.get_PlusEr()), true);
					}
						break;

					case FEATHER_BUFF_A: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addDmgup(2);
						pc.addHitup(2);
						pc.getAbility().addSp(2);
						pc.sendPackets(new S_SPMR(pc), true);
						pc.addMaxHp(50);
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
						if (pc.isInParty()) {
							pc.getParty().updateMiniHP(pc);
						}
						pc.addHpr(3);
						pc.addMaxMp(30);
						pc.addMpr(3);
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					}
						break;

					case FEATHER_BUFF_B: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addHitup(2);
						pc.getAbility().addSp(1);
						pc.sendPackets(new S_SPMR(pc), true);
						pc.addMaxHp(50);
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
						if (pc.isInParty()) {
							pc.getParty().updateMiniHP(pc);
						}
						pc.addMaxMp(30);
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
					}
						break;

					case FEATHER_BUFF_C: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.addMaxHp(50);
						pc.sendPackets(new S_HPUpdate(pc.getCurrentHp(), pc.getMaxHp()), true);
						if (pc.isInParty()) {
							pc.getParty().updateMiniHP(pc);
						}
						pc.addMaxMp(30);
						pc.sendPackets(new S_MPUpdate(pc.getCurrentMp(), pc.getMaxMp()), true);
						pc.getAC().addAc(-2);
						pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					}
						break;

					case FEATHER_BUFF_D: {
						L1PcInstance pc = (L1PcInstance) cha;
						pc.getAC().addAc(-1);
						pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					}
						break;

					case 50001: {
						if (_user instanceof L1NpcInstance) {
							for (int i = 3; i > 0; i--) {
								if (_target instanceof L1PcInstance) {
									L1PcInstance target = (L1PcInstance) _target;
									// _target.onAction(_npc);
									_target.onAction(_player);
									Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 7020));
									Broadcaster.broadcastPacket(_user, new S_SkillSound(_user.getId(), 11764));
								}
							}
						}

					}
						break;

					default:
						break;
					}
				}

				if (_calcType == PC_NPC || _calcType == NPC_NPC) {
					if (_skillId == TAMING_MONSTER && ((L1MonsterInstance) cha).getNpcTemplate().isTamable()) {
						int petcost = 0;
						for (Object pet : ((L1PcInstance) _user).getPetList()) {
							petcost += ((L1NpcInstance) pet).getPetcost();
						}

						int charisma = _user.getAbility().getTotalCha();
						if (_player.isElf()) {
							charisma += 12;
						} else if (_player.isWizard()) {
							charisma += 6;
						}

						charisma -= petcost;
						if (charisma >= 6) {
							L1SummonInstance summon = new L1SummonInstance(_targetNpc, (L1PcInstance) _user, false);
							_target = summon;
						} else {
							_player.sendPackets(new S_ServerMessage(319), true);
						}

					} else if (_skillId == CREATE_ZOMBIE) {
						int petcost = 0;
						for (Object pet : ((L1PcInstance) _user).getPetList()) {
							petcost += ((L1NpcInstance) pet).getPetcost();
						}

						int charisma = _user.getAbility().getTotalCha();
						if (_player.isElf()) {
							charisma += 12;
						} else if (_player.isWizard()) {
							charisma += 6;
						}

						charisma -= petcost;
						if (charisma >= 6) {
							L1SummonInstance summon = new L1SummonInstance(_targetNpc, (L1PcInstance) _user, true);
							_target = summon;
						} else {
							_player.sendPackets(new S_ServerMessage(319), true);
						}

					} else if (_skillId == WEAK_ELEMENTAL) {
						if (cha instanceof L1MonsterInstance) {
							L1Npc npcTemp = ((L1MonsterInstance) cha).getNpcTemplate();
							int weakAttr = npcTemp.get_weakAttr();
							if ((weakAttr & 1) == 1) {
								Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), 2169), true);
							}

							if ((weakAttr & 2) == 2) {
								Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), 2167), true);
							}

							if ((weakAttr & 4) == 4) {
								Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), 2166), true);
							}

							if ((weakAttr & 8) == 8) {
								Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), 2168), true);
							}
						}

					} else if (_skillId == RETURN_TO_NATURE) {
						if (Config.RETURN_TO_NATURE && cha instanceof L1SummonInstance) {
							L1SummonInstance summon = (L1SummonInstance) cha;
							Broadcaster.broadcastPacket(summon, new S_SkillSound(summon.getId(), 2245), true);
							summon.returnToNature();
						} else {
							if (_user instanceof L1PcInstance) {
								_player.sendPackets(new S_ServerMessage(79), true);
							}
						}
					}
				}

				if (_skillId == 하울) {
					dmg = 40;// 하울데미지
				}

				if (_calcType == PC_NPC) {
					if (_skill.getType() == L1Skills.TYPE_ATTACK) {
						if (dmg < 1) {
							dmg = 1;
						}
					}
				}

				if (_skill.getType() == L1Skills.TYPE_HEAL && _calcType == PC_NPC && undeadType == 1) {
					dmg *= -1;
					dmg *= 0.5;// 힐 언데드 데미지 하향
				}

				if (_skill.getType() == L1Skills.TYPE_HEAL && _calcType == PC_NPC && undeadType == 3) {
					dmg = 0;
				}

				if ((cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) && dmg < 0) {
					dmg = 0;
				}

				// System.out.println("1111111111 : " + dmg);
				if (dmg != 0 || drainMana != 0) {
					if (cha instanceof L1PcInstance) {
						L1PcInstance pc = (L1PcInstance) cha;
						boolean success = true;
						if (_skillId == L1SkillId.ERUPTION) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_ERUPTION >= time) {
								success = false;
							} else {
								pc.ANTI_ERUPTION = time + 500;
							}
						} else if (_skillId == L1SkillId.METEOR_STRIKE) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_METEOR_STRIKE >= time) {
								success = false;
							} else {
								pc.ANTI_METEOR_STRIKE = time + 1000;
							}
						} else if (_skillId == L1SkillId.SUNBURST) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_SUNBURST >= time) {
								success = false;
							} else {
								pc.ANTI_SUNBURST = time + 500;
							}
						} else if (_skillId == L1SkillId.CALL_LIGHTNING) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_CALL_LIGHTNING >= time) {
								success = false;
							} else {
								pc.ANTI_CALL_LIGHTNING = time + 500;
							}
						} else if (_skillId == L1SkillId.DISINTEGRATE) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_DISINTEGRATE >= time) {
								success = false;
							} else {
								pc.ANTI_DISINTEGRATE = time + 1000;
							}
						} else if (_skillId == L1SkillId.ETERNITY) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_ETERNITY >= time) {
								success = false;
							} else {
								pc.ANTI_ETERNITY = time + 1000;
							}
						} else if (_skillId == L1SkillId.BONE_BREAK) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_BONE_BREAK >= time) {
								success = false;
							} else {
								pc.ANTI_BONE_BREAK = time + 500;
							}
						} else if (_skillId == L1SkillId.FINAL_BURN) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_FINAL_BURN >= time) {
								success = false;
							} else {
								pc.ANTI_FINAL_BURN = time + 500;
							}
						} else if (_skillId == L1SkillId.ICE_SPIKE) {
							long time = System.currentTimeMillis();
							if (pc.ANTI_ICE_SPIKE >= time) {
								success = false;
							} else {
								pc.ANTI_ICE_SPIKE = time + 500;
							}
						}

						if (success) {
							_magic.commit(dmg, drainMana, _skillId);
						}

					} else {
						_magic.commit(dmg, drainMana, _skillId);
					}

				}

				if (heal > 0) {
					if ((heal + _user.getCurrentHp()) > _user.getMaxHp()) {
						_user.setCurrentHp(_user.getMaxHp());
					} else {
						_user.setCurrentHp(heal + _user.getCurrentHp());
					}
				}

				// 스킬로 변경된 정보, 캐릭터 상태 정보 갱신
				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
					// 스킬로 변경된 방어, 속성 방어 등 갱신
					pc.sendPackets(new S_OwnCharAttrDef(pc), true);
					// 캐릭터 상태 변경
					pc.sendPackets(new S_OwnCharStatus(pc), true);
					sendHappenMessage(pc);
				}

				// XXX 2899줄에서 타이머 체크 조건에 속하면 호출되거나 여기서 호출됨
				addMagicList(cha, false); // => sendIcon() 호출함(단지 스킬 타이머 등록만을 위해서 호출?)

				if (cha instanceof L1PcInstance) {
					L1PcInstance pc = (L1PcInstance) cha;
					pc.getLight().turnOnOffLight();
				}
			}

			if (_skillId == DETECTION || _skillId == COUNTER_DETECTION || _skillId == FREEZING_BREATH) {
				detection(_player);
			}

		} catch (Exception e) {
			// 스킬 오류 발생 부분에 케릭터명, 몹명, 타켓명순으로 출력
			e.printStackTrace();
			// System.out.println("오류 발생 : " + _player.getAccountName() + " | "
			// + _npc.getName() + " | " + _target.getName());
			// _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}

	} // end runSkill()

	private boolean isNotCancelable(int skillNum) {
		return skillNum == ENCHANT_WEAPON || skillNum == BLESSED_ARMOR || skillNum == ABSOLUTE_BARRIER || skillNum == ADVANCE_SPIRIT || skillNum == 뫼비우스
				|| skillNum == SHOCK_STUN || skillNum == 쉐도우스탭 || skillNum == PANTERA || skillNum == 기간틱 || skillNum == FORCE_STUN || skillNum == DEMOLITION || skillNum == ETERNITY
				|| skillNum == PRIME || skillNum == HALPHAS || skillNum == POTENTIAL || skillNum == PHANTOM || skillNum == 파워그립 || skillNum == 데스페라도
				|| skillNum == 엠파이어 || skillNum == SCALES_Lind_DRAGON || skillNum == STATUS_UNDERWATER_BREATH || skillNum == 인페르노 || skillNum == SHADOW_FANG
				|| skillNum == REDUCTION_ARMOR || skillNum == SOLID_CARRIAGE || skillNum == COUNTER_BARRIER || skillNum == 인페르노 || skillNum == SHADOW_ARMOR
				|| skillNum == ARMOR_BREAK || skillNum == DRESS_EVASION || skillNum == UNCANNY_DODGE || skillNum == SCALES_EARTH_DRAGON
				|| skillNum == SCALES_WATER_DRAGON || skillNum == SCALES_FIRE_DRAGON || skillNum == BOUNCE_ATTACK || skillNum == IllUSION_OGRE
				|| skillNum == IllUSION_LICH || skillNum == IllUSION_DIAMONDGOLEM || skillNum == IllUSION_AVATAR || skillNum == STATUS_DRAGONPERL
				|| skillNum == 프라이드 || skillNum == 블로우어택 || skillNum == CUBE_IGNITION || skillNum == CUBE_QUAKE || skillNum == CUBE_SHOCK
				|| skillNum == CUBE_BALANCE;

	}

	private void detection(L1PcInstance pc) {
		if (pc != null) {
			if (pc != null && !pc.isGmInvis() && pc.isInvisble()) {
				pc.delInvis();
				pc.beginInvisTimer();
			}

			for (L1PcInstance tgt : L1World.getInstance().getVisiblePlayer(pc)) {
				if (!tgt.isGmInvis() && tgt.isInvisble()) {
					tgt.delInvis();
					tgt.beginInvisTimer();
				}
			}

			L1WorldTraps.getInstance().onDetection(pc);

		} else if (_user != null && _user instanceof GambleInstance) {
			for (L1PcInstance tgt : L1World.getInstance().getVisiblePlayer(_user)) {
				if (!tgt.isGmInvis() && tgt.isInvisble()) {
					tgt.delInvis();
					tgt.beginInvisTimer();
				}
			}
		}
	}

	private boolean isTargetCalc(L1Character cha) {
		if (_skill.getTarget().equals("attack") && _skillId != 18) {
			if (isPcSummonPet(cha)) {
				if (CharPosUtil.getZoneType(_player) == 1 || CharPosUtil.getZoneType(cha) == 1 || _player.checkNonPvP(_player, cha)) {
					return false;
				}
			}
		}

		if (_skillId == FOG_OF_SLEEPING && _user.getId() == cha.getId()) {
			return false;
		}

		if (_skillId == MASS_TELEPORT) {
			if (_user.getId() != cha.getId()) {
				return false;
			}
		}

		return true;
	}

	private boolean isPcSummonPet(L1Character cha) {
		if (_calcType == PC_PC) {
			return true;
		}

		if (_calcType == PC_NPC) {
			if (cha instanceof L1SummonInstance) {
				L1SummonInstance summon = (L1SummonInstance) cha;
				if (summon.isExsistMaster()) {
					return true;
				}
			}
			if (cha instanceof L1PetInstance || cha instanceof GambleInstance || cha instanceof L1NpcShopInstance) {
				return true;
			}
		}

		return false;
	}

	private boolean isUseCounterMagic(L1Character cha) {
		if (_isCounterMagic && cha.getSkillEffectTimerSet().hasSkillEffect(COUNTER_MAGIC)) {
			cha.getSkillEffectTimerSet().removeSkillEffect(COUNTER_MAGIC);
			// int castgfx =
			// SkillsTable.getInstance().getTemplate(COUNTER_MAGIC).getCastGfx();
			Broadcaster.broadcastPacket(cha, new S_SkillSound(cha.getId(), 10702), true);
			if (cha instanceof L1PcInstance) {
				L1PcInstance pc = (L1PcInstance) cha;
				pc.sendPackets(new S_SkillSound(pc.getId(), 10702), true);
			}

			return true;
		}

		return false;
	}

	private boolean isTargetFailure(L1Character cha) {
		boolean isTU = false;
		boolean isErase = false;
		boolean isManaDrain = false;
		int undeadType = 0;

		if (cha instanceof L1TowerInstance || cha instanceof L1DoorInstance) {
			return true;
		}

		if (cha instanceof L1PcInstance) {
			if (_calcType == PC_PC && _player.checkNonPvP(_player, cha)) {
				L1PcInstance pc = (L1PcInstance) cha;
				if (_player.getId() == pc.getId() || (pc.getClanid() != 0 && _player.getClanid() == pc.getClanid())) {
					return false;
				}

				return true;
			}

			return false;
		}

		if (cha instanceof L1MonsterInstance) {
			isTU = ((L1MonsterInstance) cha).getNpcTemplate().get_IsTU();
			isErase = ((L1MonsterInstance) cha).getNpcTemplate().get_IsErase();
			undeadType = ((L1MonsterInstance) cha).getNpcTemplate().get_undead();
			isManaDrain = true;
		}

		if ((_skillId == TURN_UNDEAD && (undeadType == 0 || undeadType == 2))
				|| (_skillId == TURN_UNDEAD && isTU == false) || ((_skillId == ERASE_MAGIC || _skillId == SLOW || _skillId == MOB_SLOW_1
						|| _skillId == MOB_SLOW_18 || _skillId == MANA_DRAIN || _skillId == WIND_SHACKLE) && isErase == false)
				|| (_skillId == MANA_DRAIN && isManaDrain == false)) {
			return true;
		}

		return false;
	}

	public static int checkObject(int x, int y, short m, int d) {
		L1Map map = L1WorldMap.getInstance().getMap(m);
		switch (d) {
		case 0:
			if (map.isPassable(x, y, 0)) {
				return 0;
			} else if (map.isPassable(x, y, 7)) {
				return 7;
			} else if (map.isPassable(x, y, 1)) {
				return 1;
			}
			break;

		case 1:
			if (map.isPassable(x, y, 1)) {
				return 1;
			} else if (map.isPassable(x, y, 0)) {
				return 0;
			} else if (map.isPassable(x, y, 2)) {
				return 2;
			}

			break;

		case 2:
			if (map.isPassable(x, y, 2)) {
				return 2;
			} else if (map.isPassable(x, y, 1)) {
				return 1;
			} else if (map.isPassable(x, y, 3)) {
				return 3;
			}
			break;

		case 3:
			if (map.isPassable(x, y, 3)) {
				return 3;
			} else if (map.isPassable(x, y, 2)) {
				return 2;
			} else if (map.isPassable(x, y, 4)) {
				return 4;
			}
			break;

		case 4:
			if (map.isPassable(x, y, 4)) {
				return 4;
			} else if (map.isPassable(x, y, 3)) {
				return 3;
			} else if (map.isPassable(x, y, 5)) {
				return 5;
			}
			break;

		case 5:
			if (map.isPassable(x, y, 5)) {
				return 5;
			} else if (map.isPassable(x, y, 4)) {
				return 4;
			} else if (map.isPassable(x, y, 6)) {
				return 6;
			}
			break;

		case 6:
			if (map.isPassable(x, y, 6)) {
				return 6;
			} else if (map.isPassable(x, y, 5)) {
				return 5;
			} else if (map.isPassable(x, y, 7)) {
				return 7;
			}
			break;

		case 7:
			if (map.isPassable(x, y, 7)) {
				return 7;
			} else if (map.isPassable(x, y, 6)) {
				return 6;
			} else if (map.isPassable(x, y, 0)) {
				return 0;
			}
			break;

		default:
			break;
		}

		return -1;
	}

	class TargetLineSkillEffect implements Runnable {
		private byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };
		private byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

		int effect = 0;
		L1Character caster = null;
		L1Character target = null;

		public TargetLineSkillEffect(L1Character _c, L1Character _t, int _e) {
			caster = _c;
			target = _t;
			effect = _e;
		}

		public void run() {
			try {
				if (caster == null || target == null) {
					return;
				}

				int xx = caster.getX();
				int yy = caster.getY();
				int ia = caster.getLocation().getTileLineDistance(target.getLocation());
				for (int i = 0; i < ia; i++) {
					int a = CharPosUtil.calcheading(xx, yy, target.getX(), target.getY());
					int x = xx;
					int y = yy;
					x += HEADING_TABLE_X[a];
					y += HEADING_TABLE_Y[a];

					Broadcaster.broadcastPacket(caster, new S_EffectLocation(x, y, (short) effect), true);
					if (target.getX() == x && target.getY() == y) {
						break;
					}
					xx = x;
					yy = y;
					Thread.sleep(100);
				}

				if (target instanceof L1PcInstance) {
					((L1PcInstance) target).sendPackets(new S_DoActionGFX(target.getId(), ActionCodes.ACTION_Damage), true);
				}

				Broadcaster.broadcastPacket(target, new S_DoActionGFX(target.getId(), ActionCodes.ACTION_Damage), true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	int 캔슬강제삭제[] = { STATUS_CURSE_BARLOG, STATUS_CURSE_YAHEE/* , STATUS_DRAGONPERL */ };

	private void summonMonster(L1PcInstance pc, int order) {
		int[] summonid_list = null;
		int summonid = 0;
		int summoncost = 25;
		int userlevel = 28;
		switch (order) {
		case 1:
			summonid_list = new int[] { 81210 };
			userlevel = 28;
			break;

		case 2:
			summonid_list = new int[] { 81211 };
			userlevel = 28;
			break;

		case 7:
			summonid_list = new int[] { 81212 };
			userlevel = 40;
			break;

		case 13:
			summonid_list = new int[] { 81213 };
			userlevel = 52;
			break;

		case 19:
			summonid_list = new int[] { 81214 };
			userlevel = 64;
			break;

		case 25:
			summonid_list = new int[] { 81215 };
			userlevel = 76;
			break;

		case 31:
			summonid_list = new int[] { 81216 };
			summoncost = 35;
			userlevel = 80;
			break;

		case 37:
			summonid_list = new int[] { 81217 };
			summoncost = 35;
			userlevel = 82;
			break;

		case 43:
			summonid_list = new int[] { 81218 };
			summoncost = 35;
			userlevel = 84;
			break;

		case 49:
			summonid_list = new int[] { 81219 };
			summoncost = 35;
			userlevel = 86;
			break;

		case 55:
			summonid_list = new int[] { 81220 };
			summoncost = 35;
			userlevel = 88;
			break;

		default:
			break;
		}

		summonid = summonid_list[0];

		if (pc.getLevel() < userlevel) {
			pc.sendPackets(new S_ServerMessage(743));
			return;
		}

		int petcost = 0;
		Object[] petlist = pc.getPetList().toArray();
		for (Object pet : petlist) {
			petcost += ((L1NpcInstance) pet).getPetcost();
		}

		if ((summonid == 810848 || summonid == 810850 || summonid == 810849) && petcost != 0) {
			pc.sendPackets(new S_CloseList(pc.getId()));
			return;
		}

		int charisma = pc.getAbility().getTotalCha() + 6 - petcost;
		int summoncount = charisma / summoncost;

		L1Npc npcTemp = NpcTable.getInstance().getTemplate(summonid);
		L1SummonInstance summon = null;

		for (int cnt = 0; cnt < summoncount; cnt++) {
			summon = new L1SummonInstance(npcTemp, pc);
			if (summonid == 810848 || summonid == 810850 || summonid == 810849) {
				summon.setPetcost(pc.getAbility().getTotalCha() + 7);
			} else {
				summoncount = charisma / summoncost;
			}
		}

		S_DoActionGFX gfx = new S_DoActionGFX(pc.getId(), 19);
		pc.sendPackets(gfx);
	}

	private static final double HASTE_RATE = 0.745;
	private static final double WAFFLE_RATE = 0.874;
	private static final double THIRDSPEED_RATE = 0.874;// by사부

	private int getRightInterval(L1PcInstance _pc, int actid) {
		int gfxid = _pc.getGfxId().getTempCharGfx();
		int interval = 0;

		if (actid == 18) {
			interval = SprTable.getInstance().getDirSpellSpeed(gfxid);
		} else {
			interval = SprTable.getInstance().getNodirSpellSpeed(gfxid);
		}

		if (interval == 0) {
			// System.out.println(_pc.getName() + "의 스킬 사용 spr오류 확인 요망 변신 : " + gfxid);
			interval = 640;
		}

		if (_pc.isHaste()) {
			interval *= HASTE_RATE;
		}

		if (_pc.isBloodLust()) { // 블러드러스트
			interval *= HASTE_RATE;
		}

		if (_pc.isSandstorm()) { // 샌드스톰
			interval *= HASTE_RATE;
		}

		if (_pc.isHurricane()) { // 허리케인
			interval *= HASTE_RATE;
		}

		if (_pc.isFocuswave()) { // 허리케인
			interval *= HASTE_RATE;
		}

		if (_pc.isdarkhos()) { // 다크호스
			interval *= HASTE_RATE;
		}

		if (_pc.isBrave()) {
			interval *= HASTE_RATE;
		}

		if (_pc.isElfBrave()) {
			interval *= WAFFLE_RATE;
		}

		if (_pc.isThirdSpeed()) {
			interval *= THIRDSPEED_RATE;
		}

		if (_pc.getMapId() == 5143) {
			interval *= (HASTE_RATE / 2);
		}

		return interval;
	}

	private static final int[] stunTimethunder = { 3000, 3000, 3000, 3000, 4000, 4000, 4000, 4000, 5000, 5000, 6000, 6000// 2
	};

	private static final int[] stunTimethunder2 = { 000, 1000, 1000, 1000, 1000, 1500, 1500, 1500, // 5
			1500, 1500, 1500, 1500, // 4
			2000, 2000, 2000, 2000 };

	private static final int[] BonestunTime = { 1500, 1500, 1500, // 4
			1800, 1500, 1800, 1500, 1800, // 3
			1500, 1800, 1500, 1800, 1500 };

	private static final int[] stunTimeArray1 = { 1500, 1500, 1500, 1500, // 4
			2000, 2000, 2000, // 3
			2500, 2500, // 2
			3000, 3000, 3500 };

	private static final int[] stunTimeArray2 = { 1500, 1500, 1500, 2000, 2000, 2500, 2500, 3000, 3500 };

	private static final int[] stunTimeArray3 = { 1500, 1500, 2000, 2000, 2500, 3000, 3500 };

	private static final int[] stunTimeArray4 = { 1500, 1500, 2000, 2500, 3000, 3500 };

	private static final int[] stunTimeArray5 = { 1500, 2000, 2500, 3000, 3500 };

	private static final int[] stunTimeArray6 = { 2000, 2500, 3000, 3500 };

	private static final int[] stunTimeArray7 = { 2000, 2500, 3000, 3000, 3000, 3500 };

	private static final int[] stunTimeArray8 = { 2000, 2500, 3000, 3000, 3000, 3500 };

	private static final int[] stunTimeArray9 = { 2000, 2500, 3000, 3000, 3000, 3500 };

	private static final int[] stunTimeArray10 = { 2000, 2500, 3000, 3500, 3500, 3500 };

	private static final int[] stunTimeArray11 = { 2000, 2500, 3000, 3500, 3500, 3500 };

	private static final int[] stunTimeArray12 = { 2000, 2500, 3000, 3500, 3500, 3500 };

	private static final int[] forcestunTimeArray1 = { 2000, 2500, 3000, 3500, 3500, 3500 };

	private static final int[] forcestunTimeArray2 = { 5500, 6000, 6500, 7000, 7000 };

	private static final int[] empierTimeArray1 = { 1500, 1500, 1500, 1500, // 4
			2000, 2000, 2000, // 3
			2500, 2500, // 2
			3000, 3000, 3500 };

	private static final int[] empierTimeArray2 = { 1500, 1500, 1500, 2000, 2000, 2500, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] empierTimeArray3 = { 1500, 1500, 2000, 2000, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] empierTimeArray4 = { 1500, 1500, 2000, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] empierTimeArray5 = { 1500, 2000, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] empierTimeArray6 = { 2000, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] empierTimeArray7 = { 2000, 2500, 3000, 3000, 3000, 3500, 4000, 4500 };

	private static final int[] empierTimeArray8 = { 2000, 2500, 3000, 3000, 3000, 3500, 4000, 4500 };

	private static final int[] empierTimeArray9 = { 2000, 2500, 3000, 3000, 3000, 3500, 4000, 4500 };

	private static final int[] empierTimeArray10 = { 2000, 2500, 3000, 3500, 3500, 3500, 4000, 4500 };

	private static final int[] empierTimeArray11 = { 2000, 2500, 3000, 3500, 3500, 3500, 4000, 4500 };

	private static final int[] empierTimeArray12 = { 2000, 2500, 3000, 3500, 3500, 3500, 4000, 4500 };

	private static final int[] panteratimeArray1 = { 1500, 1500, 1500, 1500, // 4
			2000, 2000, 2000, // 3
			2500, 2500 };

	private static final int[] panteratimeArray2 = { 1500, 1500, 1500, 2000, 2000, 2500, 2500 };

	private static final int[] panteratimeArray3 = { 1500, 1500, 2000, 2000, 2500 };

	private static final int[] panteratimeArray4 = { 1500, 1500, 2000, 2500 };

	private static final int[] panteratimeArray5 = { 1500, 2000, 2500 };

	private static final int[] panteratimeArray6 = { 2000, 2500 };

	private static final int[] panteratimeArray7 = { 2000, 2500 };

	private static final int[] panteratimeArray8 = { 2000, 2500 };

	private static final int[] panteratimeArray9 = { 2000, 2500 };

	private static final int[] panteratimeArray10 = { 2000, 2500 };

	private static final int[] panteratimeArray11 = { 2000, 2500 };

	private static final int[] panteratimeArray12 = { 2000, 2500 };

	private static final int[] panteraStimeArray1 = { 1500, 1500, 1500, 1500, // 4
			2000, 2000, 2000, // 3
			2500, 2500, // 2
			3000, 3000, 3500 };

	private static final int[] panteraStimeArray2 = { 1500, 1500, 1500, 2000, 2000, 2500, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray3 = { 1500, 1500, 2000, 2000, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray4 = { 1500, 1500, 2000, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray5 = { 1500, 2000, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray6 = { 2000, 2500, 3000, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray7 = { 2000, 2500, 3000, 3000, 3000, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray8 = { 2000, 2500, 3000, 3000, 3000, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray9 = { 2000, 2500, 3000, 3000, 3000, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray10 = { 2000, 2500, 3000, 3500, 3500, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray11 = { 2000, 2500, 3000, 3500, 3500, 3500, 4000, 4500 };

	private static final int[] panteraStimeArray12 = { 2000, 2500, 3000, 3500, 3500, 3500, 4000, 4500 };

	private static final int[] mobstun30TimeArray = { 1000, 1200, 1300, 1400, 1500, 2000, 2500 };

	private static final int[] partyApplySkillArray = { ARMOR_BREAK, 그레이스아바타, SHINING_AURA };

	private static final int[] clanApplySkillArray = { PRIME };

}
