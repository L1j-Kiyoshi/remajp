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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJ3SEx.Loader.SpriteInformationLoader;
import l1j.server.MJDShopSystem.MJDShopItem;
import l1j.server.MJDShopSystem.MJDShopStorage;
import l1j.server.server.clientpackets.C_ShopAndWarehouse.ItemObjectCountPair;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.poison.L1Poison;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillTimer;
import l1j.server.server.serverpackets.S_CharTolerance;
import l1j.server.server.serverpackets.S_Poison;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.utils.IntRange;

//Referenced classes of package l1j.server.server.model:
//L1Object, Die, L1PcInstance, L1MonsterInstance,
//L1World, ActionFailed

public class L1Character extends L1Object {
	private static final String TAG = "L1Character";
	
	private static final long serialVersionUID = 1L;

	// 케릭터 기본
	// private BasicProperty basic;

	private String _name;
	private String _title;
	
	public boolean 카운터배리어베테랑 = false;

	private int _level;
	private int _exp;

	private int _lawful;
	private int _karma;

	private int _currentHp;
	private int _trueMaxHp;
	private int _maxHp;

	private int _currentMp;
	private int _trueMaxMp;
	private short _maxMp;
	
	private int _heading; // ● 방향 0. 좌상 1. 상 2. 우상 3. 오른쪽 4. 우하 5. 하 6. 좌하 7. 좌

	public int firstatk = 0;
	private L1Poison _poison = null;
	private boolean _paralyzed;
	private boolean _PHANTOM;
	private boolean _PHANTOMR;
	private boolean _PHANTOMD;
	private boolean _sleeped;
	private L1Paralysis _paralysis;
	private boolean _isDead;
	private boolean _isTrans;
	protected GfxId gfx; // 케릭터 그래픽 ID
	private MoveState moveState; // 이동속도, 바라보는 방향
	protected Light light; // 케릭터 주위 빛
	protected Ability ability; // 능력치, SP, MagicBonus
	protected Resistance resistance; // 저항 (마방, 불, 물, 바람, 땅, 스턴, 동빙, 슬립, 석화)
	protected AC ac; // AC 방어

	public boolean isManaDrain; // 마나 드레인 걸렸었는지

	private NearObjects nearObjects; // 주위 객체 및 플레이어들
	private SkillEffectTimerSet skillEffectTimerSet; // 스킬 타이머

	// 모르는거
	private boolean _isSkillDelay;
	private int _addAttrKind;
	private int actionStatus;
	// 파푸리뉴얼
	public ArrayList<String> marble = new ArrayList<String>();
	public ArrayList<String> marble2 = new ArrayList<String>();
	public ArrayList<String> tro = new ArrayList<String>();
	public ArrayList<String> sael = new ArrayList<String>();
	public ArrayList<String> sael2 = new ArrayList<String>();
	private final Map<Integer, L1DollInstance> _dolllist = new HashMap<Integer, L1DollInstance>();

	public long skilldelayTime;

	// 데미지
	private int _dmgup;
	private int _trueDmgup;
	private int _bowDmgup;
	private int _trueBowDmgup;
	private int _hitup;
	private int _trueHitup;
	private int _bowHitup;
	private int _trueBowHitup;
	
	private final Map<Integer, L1SkillTimer> _skillEffect = new HashMap<Integer, L1SkillTimer>();
	private final Map<Integer, L1ItemDelay.ItemDelayTimer> _itemdelay = new HashMap<Integer, L1ItemDelay.ItemDelayTimer>();
	private final List<L1PcInstance> _knownPlayer = new CopyOnWriteArrayList<L1PcInstance>();

	public L1Character() {
		_level = 1;
		ability = new Ability(this);
		resistance = new Resistance(this);
		ac = new AC(this);
		moveState = new MoveState();
		light = new Light(this);
		nearObjects = new NearObjects();
		gfx = new GfxId();
		skillEffectTimerSet = new SkillEffectTimerSet(this);
	}

	/**
	 * 캐릭터를 부활시킨다.
	 * 
	 * @param hp
	 *            부활 후의 HP
	 */
	public void resurrect(int hp) {
		if (!isDead())
			return;
		if (hp <= 0)
			hp = 1;

		setCurrentHp(hp);
		setDead(false);
		setActionStatus(0);
		L1PolyMorph.undoPoly(this);

		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.sendPackets(new S_RemoveObject(this));
			pc.getNearObjects().removeKnownObject(this);
			pc.updateObject();
		}
	}

	/**
	 * 캐릭터의 현재의 HP를 돌려준다.
	 * 
	 * @return 현재의 HP
	 */
	public int getCurrentHp() {
		return _currentHp;
	}

	/**
	 * 캐릭터의 HP를 설정한다.
	 * 
	 * @param i
	 *            캐릭터의 새로운 HP
	 */
	public void setCurrentHp(int i) {
		if (i >= getMaxHp()) {
			i = getMaxHp();
		}
		if (i < 0)
			i = 0;

		_currentHp = i;
	}

	/**
	 * 캐릭터의 현재의 MP를 돌려준다.
	 * 
	 * @return 현재의 MP
	 */
	public int getCurrentMp() {
		return _currentMp;
	}

	/**
	 * 캐릭터의 MP를 설정한다.
	 * 
	 * @param i
	 *            캐릭터의 새로운 MP
	 */
	public void setCurrentMp(int i) {
		if (i >= getMaxMp()) {
			i = getMaxMp();
		}
		if (i < 0)
			i = 0;

		_currentMp = i;
	}

	/**
	 * 캐릭터의 잠상태를 돌려준다.
	 * 
	 * @return 잠상태를 나타내는 값. 잠상태이면 true.
	 */
	public boolean isSleeped() {
		return _sleeped;
	}

	/**
	 * 캐릭터의 잠상태를 설정한다.
	 * 
	 * @param sleeped
	 *            잠상태를 나타내는 값. 잠상태이면 true.
	 */
	public void setSleeped(boolean sleeped) {
		_sleeped = sleeped;
	}

	/**
	 * 캐릭터의 마비 상태를 돌려준다.
	 * 
	 * @return 마비 상태를 나타내는 값. 마비 상태이면 true.
	 */
	public boolean isParalyzed() {
		return _paralyzed;
	}

	/**
	 * 캐릭터의 마비 상태를 돌려준다.
	 * 
	 * @return 마비 상태를 나타내는 값. 마비 상태이면 true.
	 */
	public void setParalyzed(boolean paralyzed) {
		_paralyzed = paralyzed;
	}
	
	/**팬텀R타임*/
	public boolean isPhantomRippered() {
		return _PHANTOMR;
	}
	
	public void setPhantomRippered(boolean PHANTOMR) {
		_PHANTOMR = PHANTOMR;
	}
	/**팬텀R타임*/
	
	/**팬텀D타임*/
	public boolean isPhantomDeathed() {
		return _PHANTOMD;
	}
	
	public void setPhantomDeathed(boolean PHANTOMD) {
		_PHANTOMD = PHANTOMD;
	}
	/**팬텀D타임*/

	public L1Paralysis getParalysis() {
		return _paralysis;
	}

	public void setParalaysis(L1Paralysis p) {
		_paralysis = p;
	}
	public int getZoneType() {
		if (getMap().isSafetyZone(getLocation())) {
			/** 배틀존 **/
			if (getMapId() == 5153) {
				return -1;
			} else {
				return 1;
			}
		} else if (getMap().isCombatZone(getLocation())) {
			return -1;
		} else { // 노멀존
			return 0;
		}
	}
	public void cureParalaysis() {
		if (_paralysis != null) {
			_paralysis.cure();
		}
	}

	public void broadcastPacket(ServerBasePacket packet) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet);
		}
	}// 추가

	public void broadcastPacket(ServerBasePacket packet, boolean ck) {
		for (L1PcInstance pc : L1World.getInstance().getVisiblePlayer(this)) {
			pc.sendPackets(packet);
		}
		if (ck) {
			packet.clear();
			packet = null;
		}
	}// 추가

	/**
	 * 캐릭터의 목록을 돌려준다.
	 * 
	 * @return 캐릭터의 목록을 나타내는, L1Inventory 오브젝트.
	 */
	public L1Inventory getInventory() {
		return null;
	}

	/**
	 * 캐릭터에, skill delay 추가
	 * 
	 * @param flag
	 */
	public void setSkillDelay(boolean flag) {
		_isSkillDelay = flag;
	}

	/**
	 * 캐릭터의 독 상태를 돌려준다.
	 * 
	 * @return 스킬 지연중인가.
	 */
	public boolean isSkillDelay() {
		return _isSkillDelay;
	}

	/**
	 * 캐릭터에, Item delay 추가
	 * 
	 * @param delayId
	 *            아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디크로크이면 1.
	 * @param timer
	 *            지연 시간을 나타내는, L1ItemDelay.ItemDelayTimer 오브젝트.
	 */
	public void addItemDelay(int delayId, L1ItemDelay.ItemDelayTimer timer) {
		_itemdelay.put(delayId, timer);
	}

	/**
	 * 캐릭터로부터, Item delay 삭제
	 * 
	 * @param delayId
	 *            아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디크로크이면 1.
	 */
	public void removeItemDelay(int delayId) {
		_itemdelay.remove(delayId);
	}

	/**
	 * 캐릭터에, Item delay 이 있을까
	 * 
	 * @param delayId
	 *            조사하는 아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디 클로크이면 1.
	 * @return 아이템 지연이 있으면 true, 없으면 false.
	 */
	public boolean hasItemDelay(int delayId) {
		return _itemdelay.containsKey(delayId);
	}

	/**
	 * 캐릭터의 item delay 시간을 나타내는, L1ItemDelay.ItemDelayTimer를 돌려준다.
	 * 
	 * @param delayId
	 *            조사하는 아이템 지연 ID. 통상의 아이템이면 0, 인비지비리티크로크, 바르로그브랏디 클로크이면 1.
	 * @return 아이템 지연 시간을 나타내는, L1ItemDelay.ItemDelayTimer.
	 */
	public L1ItemDelay.ItemDelayTimer getItemDelayTimer(int delayId) {
		return _itemdelay.get(delayId);
	}

	/**
	 * 캐릭터에, 독을 추가한다.
	 * 
	 * @param poison
	 *            독을 나타내는, L1Poison 오브젝트.
	 */
	public void setPoison(L1Poison poison) {
		_poison = poison;
	}

	/**
	 * 캐릭터의 독을 치료한다.
	 */
	public void curePoison() {
		if (_poison == null) {
			return;
		}
		_poison.cure();
	}

	/**
	 * 캐릭터의 독상태를 돌려준다.
	 * 
	 * @return 캐릭터의 독을 나타내는, L1Poison 오브젝트.
	 */
	public L1Poison getPoison() {
		return _poison;
	}

	/**
	 * 캐릭터에 독의 효과를 부가한다
	 * 
	 * @param effectId
	 * @see S_Poison#S_Poison(int, int)
	 */
	public void setPoisonEffect(int effectId) {
		Broadcaster.broadcastPacket(this, new S_Poison(getId(), effectId));
	}

	// /////////////////////////////////////////// 이부분 필수
	// //////////////////////////////////////////////////////////////////////////////
	/**
	 * 캐릭터의 가시 범위에 있는 플레이어에, 패킷을 송신한다.
	 * 
	 * @param packet
	 *            송신하는 패킷을 나타내는 ServerBasePacket 오브젝트.
	 */

	// /////////////////////////////////////////////이부분
	// 필수/////////////////////////////////////////////////////////////////////////////////////
	private int _tempCharGfx; // ● 베이스 그래픽 ID

	public int getTempCharGfx() {
		return _tempCharGfx;
	}

	public void setTempCharGfx(int i) {
		_tempCharGfx = i;
	}

	public int getExp() {
		return _exp;
	}

	public void setExp(int exp) {
		_exp = exp;
	}

	public String getName() {
		return _name;
	}

	public void setName(String s) {
		_name = s;
	}

	public String getTitle() {
		return _title;
	}

	public void setTitle(String s) {
		_title = s;
	}

	public int getLevel() {
		return _level;
	}

	public void setLevel(long level) {
		_level = (int) level;
	}
	
	public ArrayList<L1DollInstance> getDollList() {
		ArrayList<L1DollInstance> doll = new ArrayList<L1DollInstance>();
		synchronized (_dolllist) {
			doll.addAll(_dolllist.values());
		}
		return doll;
	}
	
	public int getMaxHp() {
		int maxhp = _maxHp;
		if(this instanceof L1PetInstance) {
			L1PetInstance Pet = (L1PetInstance) this;
			maxhp += Pet.getBonusHp();
		}else if(this instanceof L1PcInstance) {
			L1PcInstance Pc = (L1PcInstance) this;
			if(Pc.infinity_BL && Pc.getLevel() >= 60) {
				int infinit_bl = Pc.getLevel() - 57;
				maxhp += (infinit_bl / 3) * 50;
			}
			if (getAbility().getCon() >= 45){
				maxhp += 300;
			}else if (getAbility().getCon() >= 35){
				maxhp += 150;
			}else if (getAbility().getCon() >= 25){
				maxhp += 50;
			}
		}
		return maxhp;
	}
	
	/** 펫용 모든 정보 없는 처리 */
	public int getBaseMaxHp() {
		return _trueMaxHp; 
	}

	public void addMaxHp(int i) {
		setMaxHp(_trueMaxHp + i);
	}

	public void setMaxHp(int hp) {
		_trueMaxHp = hp;
		if (this instanceof L1MonsterInstance) {
			_maxHp = hp;
		} else {
			_maxHp = IntRange.ensure(_trueMaxHp, 1, 32767);
		}
		_currentHp = Math.min(_currentHp, _maxHp);
	}

	public short getMaxMp() {
		return _maxMp;
	}

	public void setMaxMp(int mp) {
		_trueMaxMp = mp;
		_maxMp = (short) IntRange.ensure(_trueMaxMp, 0, 32767);
		_currentMp = Math.min(_currentMp, _maxMp);
	}

	public void addMaxMp(int i) {
		setMaxMp(_trueMaxMp + i);
	}

	public void healHp(int pt) {
		setCurrentHp(getCurrentHp() + pt);
	}

	private int _loadHp;
	public List<L1PcInstance> getKnownPlayers() {
		return _knownPlayer;
	}
	public int get_loadHp() {
		return _loadHp;
	}

	public void set_loadHp(int _loadHp) {
		this._loadHp = _loadHp;
	}

	private int _loadMp;

	public int get_loadMp() {
		return _loadMp;
	}

	public void set_loadMp(int _loadMp) {
		this._loadMp = _loadMp;
	}

	public int getAddAttrKind() {
		return _addAttrKind;
	}

	public void setAddAttrKind(int i) {
		_addAttrKind = i;
	}

	public int getDmgup() {
		return _dmgup;
	}

	public void addDmgup(int i) {
		_trueDmgup += i;
		if (_trueDmgup >= 127) {
			_dmgup = 127;
		} else if (_trueDmgup <= -128) {
			_dmgup = -128;
		} else {
			_dmgup = _trueDmgup;
		}
	}

	public int getBowDmgup() {
		return _bowDmgup;
	}

	public void addBowDmgup(int i) {
		_trueBowDmgup += i;
		if (_trueBowDmgup >= 127) {
			_bowDmgup = 127;
		} else if (_trueBowDmgup <= -128) {
			_bowDmgup = -128;
		} else {
			_bowDmgup = _trueBowDmgup;
		}
	}

	public int getHitup() {
		return _hitup;
	}

	public void addHitup(int i) {
		_trueHitup += i;
		if (_trueHitup >= 127) {
			_hitup = 127;
		} else if (_trueHitup <= -128) {
			_hitup = -128;
		} else {
			_hitup = _trueHitup;
		}
	}

	public int getBowHitup() {
		return _bowHitup;
	}

	public void addBowHitup(int i) {
		_trueBowHitup += i;
		if (_trueBowHitup >= 127) {
			_bowHitup = 127;
		} else if (_trueBowHitup <= -128) {
			_bowHitup = -128;
		} else {
			_bowHitup = _trueBowHitup;
		}
	}

	public boolean ispapoorun = false;
	public boolean isantarun = false;

	public boolean isDead() {
		return _isDead;
	}

	public void setDead(boolean flag) {
		_isDead = flag;
	}
	
	/** 펫관련 소환 부분 */
	private boolean _PetSummons;

	public boolean isPetSummons() {
		return _PetSummons;
	}
	
	public void setPetSummons(boolean i) {
		_PetSummons = i;
	}

	public boolean transok = false;

	public boolean isTrans() {
		return _isTrans;
	}

	public void setTrans(boolean flag) {
		_isTrans = flag;
	}

	public int getActionStatus() {
		return actionStatus;
	}

	public void setActionStatus(int i) {
		actionStatus = i;
	}

	public int getLawful() {
		return _lawful;
	}

	public void setLawful(int i) {
		_lawful = i;
	}
	public int getHeading() {
		return _heading;
	}

	public void setHeading(int i) {
		_heading = i;
	}
	
	/**
	 *  캐릭터 성향 변경  
	 */
	public synchronized void addLawful(int i) {
		_lawful += i;
		if (_lawful > 32767) {
			_lawful = 32767;
		} else if (_lawful < -32768) {
			_lawful = -32768;
		}
	}

	public int checkMove() {
		if (getMap().isPassable(getLocation())) {
			return 1;
		} else {
			return 0;
		}
	}

	

	/* Kill & Death 시스템? -by 천국- */
	private int _Kills;

	public int getKills() {
		return _Kills;
	}

	public void setKills(int Kills) {
		_Kills = Kills;
	}

	private int _Deaths;

	public int getDeaths() {
		return _Deaths;
	}

	public void setDeaths(int Deaths) {
		_Deaths = Deaths;
	}
	

	/* Kill & Death 시스템? -by 천국- */

	/** 캐릭터의 우호도을 돌려준다. */
	public int getKarma() {
		return _karma;
	}

	/** 캐릭터의 우호도을 설정한다. */
	public void setKarma(int karma) {
		_karma = karma;
	}

	public GfxId getGfxId() {
		return gfx;
	}

	public NearObjects getNearObjects() {
		return nearObjects;
	}

	public Light getLight() {
		return light;
	}

	public Ability getAbility() {
		return ability;
	}

	public Resistance getResistance() {
		return resistance;
	}

	public AC getAC() {
		return ac;
	}

	public MoveState getMoveState() {
		return moveState;
	}

	public SkillEffectTimerSet getSkillEffectTimerSet() {
		return skillEffectTimerSet;
	}

	public boolean isInvisble() {
		return (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.INVISIBILITY) || getSkillEffectTimerSet()
				.hasSkillEffect(L1SkillId.BLIND_HIDING));
	}
	
	public void removeSkillEffect(int skillId) {
		L1SkillTimer timer = _skillEffect.remove(skillId);
		if (timer != null) {
			timer.end();
		}
	}
	
	/**
	 * 캐릭터로부터, 스킬의 타이머를 재실행 한다
	 * 
	 * @param skillId
	 *            설정하는 효과의 스킬 ID.
	 */
	public void startSkillEffectTimer(int skillId) {
		if (getSkillEffectTimerSet().hasSkillEffect(skillId)) {
			int remainingTimeMills = getSkillEffectTimerSet().getSkillEffectTimeSec(skillId) * 1000;

			if (remainingTimeMills >= 0) {
				getSkillEffectTimerSet().killSkillEffectTimer(skillId);
				getSkillEffectTimerSet().setSkillEffect(skillId, remainingTimeMills);
			}
		} else {
			return;
		}
	}
	
	public void stopSkillEffectTimer(int skillId) {
		L1SkillTimer timer = _skillEffect.get(skillId);
		if (timer != null) {
			timer.kill();
		}
	}
	
	/**
	 * 캐릭터가 인식하고 있는 모든 오브젝트를 돌려준다.
	 * 
	 * @return 캐릭터가 인식하고 있는 오브젝트를 나타내는 List<L1Object>.
	 */
	private final List<L1Object> _knownObjects = new CopyOnWriteArrayList<L1Object>();
	public List<L1Object> getKnownObjects() {
		return _knownObjects;
	}


	// **지엠 버프 따로 저장 **// by 도우너
	private int _buffnoch;

	public int getBuffnoch() {
		return _buffnoch;
	}
	  private int 어택레벨;
		 
		 public int get어택레벨() {
		     return 어택레벨;
		 }
		 public void set어택레벨(int i) {
			 어택레벨 = i;
		 }
	public void setBuffnoch(int buffnoch) {
		_buffnoch = buffnoch;
		// **지엠 버프 따로 저장 **//
	}
	private int 락구간;
	 
	 public int get락구간상승() {
	     return 락구간;
	 }
	 public void set락구간상승(int i) {
		 락구간 = i;
	 }
	 
	 /**
		 * 지정된 좌표에 대할 방향을 돌려준다.
		 * 
		 * @param tx
		 *            좌표의 X치
		 * @param ty
		 *            좌표의 Y치
		 * @return 지정된 좌표에 대할 방향
		 */
		public int targetDirection(int tx, int ty) {
			float dis_x = Math.abs(getX() - tx); // X방향의 타겟까지의 거리
			float dis_y = Math.abs(getY() - ty); // Y방향의 타겟까지의 거리
			float dis = Math.max(dis_x, dis_y); // 타겟까지의 거리

			if (dis == 0)
				return getHeading();

			int avg_x = (int) Math.floor((dis_x / dis) + 0.59f); // 상하 좌우가 조금 우선인 둥근
			int avg_y = (int) Math.floor((dis_y / dis) + 0.59f); // 상하 좌우가 조금 우선인 둥근

			int dir_x = 0;
			int dir_y = 0;

			if (getX() < tx)
				dir_x = 1;
			if (getX() > tx)
				dir_x = -1;

			if (getY() < ty)
				dir_y = 1;
			if (getY() > ty)
				dir_y = -1;

			if (avg_x == 0)
				dir_x = 0;
			if (avg_y == 0)
				dir_y = 0;

			if (dir_x == 1 && dir_y == -1)
				return 1; // 상
			if (dir_x == 1 && dir_y == 0)
				return 2; // 우상
			if (dir_x == 1 && dir_y == 1)
				return 3; // 오른쪽
			if (dir_x == 0 && dir_y == 1)
				return 4; // 우하
			if (dir_x == -1 && dir_y == 1)
				return 5; // 하
			if (dir_x == -1 && dir_y == 0)
				return 6; // 좌하
			if (dir_x == -1 && dir_y == -1)
				return 7; // 왼쪽
			if (dir_x == 0 && dir_y == -1)
				return 0; // 좌상

			return getHeading();
		}

		/**
		 * 지정된 좌표까지의 직선상에, 장애물이 존재*하지 않는가*를 돌려준다.
		 * 
		 * @param tx
		 *            좌표의 X치
		 * @param ty
		 *            좌표의 Y치
		 * @return 장애물이 없으면 true, 어느 false를 돌려준다.
		 */
	
	//허공버그픽스
	private boolean delete;  //-- 수정
	public boolean get_delete() {
		return delete;
	}
	public void set_delete(boolean b) {
		this.delete = b;
	}
	
	/** 2016.11.24 MJ 앱센터 시세 **/
	private ArrayList<MJDShopItem> _sellings;		// 판매 목록
	private ArrayList<MJDShopItem> _purchasings;	// 매입 목록
	public void disposeShopInfo(){
		disposeSellings();
		disposePurchasings();
	}
	private MJDShopItem findDShopItem(ArrayList<MJDShopItem> list, int objid){
		if(list == null)
			return null;
		
		int size = list.size();
		MJDShopItem item = null;
		for(int i=0; i<size; i++){
			item = list.get(i);
			if(item.objId == objid)
				return item;
		}
		return null;
	}
	public MJDShopItem findSellings(int objid){
		return findDShopItem(_sellings, objid);
	}
	public void updateSellingsForOrderList(ArrayList<ItemObjectCountPair> list){
		list.sort(new decending());
		for(ItemObjectCountPair pair : list){
			MJDShopItem item = _sellings.get(pair.objid);
			if(item == null)
				continue;
			
			if(item.count <= pair.count){
				_sellings.remove(item);
				MJDShopStorage.deleteProcess(this, item.objId);
			}else{
				item.count -= pair.count;
				MJDShopStorage.updateProcess(this, item);
			}
		}
	}
	
	class decending implements Comparator<ItemObjectCountPair>{
		@Override
		public int compare(ItemObjectCountPair o1, ItemObjectCountPair o2) {
			if(o1.objid == o2.objid)
				return 0;
			else if(o1.objid > o2.objid)
				return -1;
			else
				return 1;
		}		
	}
	
	public void updateSellings(int objid, int count){
		MJDShopItem item = findSellings(objid);
		if(item == null)
			return;
		if(item.count <= count){
			_sellings.remove(item);
			MJDShopStorage.deleteProcess(this, item.objId);
		}else{
			item.count -= count;
			MJDShopStorage.updateProcess(this, item);
		}
	}
	public ArrayList<MJDShopItem> getSellings(){
		return _sellings;
	}
	public void setSellings(ArrayList<MJDShopItem> list){
		_sellings = list;
	}
	public void addSellings(MJDShopItem item){
		if(_sellings == null)
			_sellings = new ArrayList<MJDShopItem>(7);
		_sellings.add(item);
		
	}
	public void disposeSellings(){
		if(_sellings != null){
			_sellings.clear();
			_sellings = null;
		}
	}
	public MJDShopItem findPurchasings(int objid){
		return findDShopItem(_purchasings, objid);
	}
	public void updatePurchasings(int objid, int count){
		MJDShopItem item = findPurchasings(objid);
		if(item == null) return;
		/** 판매겟수가 최대판매겟수보다 높다면 삭제 아니라면 - */
		if(count >= item.count){
			_purchasings.remove(item);
			MJDShopStorage.deleteProcess(this, item.objId);
		}else{
			item.count -= count;
			MJDShopStorage.updateProcess(this, item);
		}
	}
	public ArrayList<MJDShopItem> getPurchasings(){
		return _purchasings;
	}
	public void setPurchasings(ArrayList<MJDShopItem> list){
		_purchasings = list;
	}
	public void addPurchasings(MJDShopItem item){
		if(_purchasings == null)
			_purchasings = new ArrayList<MJDShopItem>(7);
		_purchasings.add(item);
	}
	public void disposePurchasings(){
		if(_purchasings != null){
			_purchasings.clear();
			_purchasings = null;
		}
	}
	/** 2016.11.24 MJ 앱센터 시세 **/
	private int _technique = 0;
	private int _spirit = 0;
	private int _dragonlang = 0;
	private int _fear = 0;
	private int _alltolerance = 0;

	private int _techniquehit = 0;
	private int _spirithit = 0;
	private int _dragonlanghit = 0;
	private int _fearhit = 0;
	private int _allhit = 0;
	
	public int getTechniqueTolerance() {
		return _technique;
	}

	public void addTechniqueTolerance(int i) {
		_technique += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.TOLERANCE,
						S_CharTolerance.TECHNIQUE, getTechniqueTolerance() - getAllTolerance()));
		}
	}

	public int getSpiritTolerance() {
		return _spirit;
	}

	public void addSpiritTolerance(int i) {
		_spirit += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.TOLERANCE,
						S_CharTolerance.SPIRIT, getSpiritTolerance() - getAllTolerance()));
		}
	}

	public int getDragonLangTolerance() {
		return _dragonlang;
	}

	public void addDragonLangTolerance(int i) {
		_dragonlang += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.TOLERANCE,
						S_CharTolerance.DRAGON, getDragonLangTolerance() - getAllTolerance()));
		}
	}

	public int getFearTolerance() {
		return _fear;
	}

	public void addFearTolerance(int i) {
		_fear += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.TOLERANCE, S_CharTolerance.FEAR,
						getFearTolerance() - getAllTolerance()));
		}
	}

	public int getAllTolerance() {
		return _alltolerance;
	}

	public void addAllTolerance(int i) {
		_technique += i;
		_spirit += i;
		_dragonlang += i;
		_fear += i;
		_alltolerance += i;
		
		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.TOLERANCE, S_CharTolerance.ALL,
						getAllTolerance()));
		}
	}

	public int getTechniqueHit() {
		return _techniquehit;
	}

	public void addTechniqueHit(int i) {
		_techniquehit += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.HIT, S_CharTolerance.TECHNIQUE,
						getTechniqueHit() - getAllHit()));
		}
	}

	public int getSpiritHit() {
		return _spirithit;
	}

	public void addSpiritHit(int i) {
		_spirithit += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.HIT, S_CharTolerance.SPIRIT,
						getSpiritHit() - getAllHit()));
		}
	}

	public int getDragonLangHit() {
		return _dragonlanghit;
	}

	public void addDragonLangHit(int i) {
		_dragonlanghit += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.HIT, S_CharTolerance.DRAGON,
						getDragonLangHit() - getAllHit()));
		}
	}

	public int getFearHit() {
		return _fearhit;
	}

	public void addFearHit(int i) {
		_fearhit += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.HIT, S_CharTolerance.FEAR,
						getFearHit() - getAllHit()));
		}
	}

	public int getAllHit() {
		return _allhit;
	}

	public void addAllHit(int i) {
		_techniquehit += i;
		_spirithit += i;
		_dragonlanghit += i;
		_fearhit += i;
		_allhit += i;

		if (this instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) this;
			if (pc.getOnlineStatus() == 1)
				pc.sendPackets(new S_CharTolerance(S_CharTolerance.CHAR_TOLERANCE_AND_HIT, S_CharTolerance.HIT, S_CharTolerance.ALL, getAllHit()));
		}
	}
	
	/** 지배 전용 케릭터 정보 저장 */
	private boolean Domination;

	public int dx = 0;
	public int dy = 0;
	public short dm = 0;
	public short fdmap = 0;
	public int dh = 0;
	
	public boolean GetDomination() {
		return Domination;
	}
	
	public void SetDomination(boolean b) {
		this.Domination = b;
	}
	
	
	
	/**mjSpr관련*/
	protected SpriteInformation _currentSpriteInfo;

	public int getCurrentSpriteId() {
		return _currentSpriteInfo == null ? 1120 : _currentSpriteInfo.getSpriteId();
	}

	public SpriteInformation getCurrentSprite() {
		return _currentSpriteInfo;
	}

	public void setCurrentSprite(int spriteId) {
		if (!equalsCurrentSprite(spriteId))
			_currentSpriteInfo = SpriteInformationLoader.getInstance().get(spriteId);
	}

	public boolean equalsCurrentSprite(int compareSpriteId) {
		return getCurrentSpriteId() == compareSpriteId;
	}

	public long getCurrentSpriteInterval(EActionCodes actionCode) {
		return (long) _currentSpriteInfo.getInterval(this, actionCode);
	}

	public long getCurrentSpriteInterval(int actionCode) {
		return (long) _currentSpriteInfo.getInterval(this, actionCode);
	}
	
	private int _moveSpeed; // ● 스피드 0. 통상 1. 헤이 파업 2. 슬로우
	
	public int getMoveSpeed() {
		return _moveSpeed;
	}

	public void setMoveSpeed(int i) {
		_moveSpeed = i;
	}
	
	public boolean isHaste() {
		return (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.STATUS_HASTE) || getSkillEffectTimerSet().hasSkillEffect(L1SkillId.HASTE)
				|| getSkillEffectTimerSet().hasSkillEffect(L1SkillId.GREATER_HASTE) || getMoveSpeed() == 1);
	}
	
	private int _braveSpeed; // ● 치우침 이브 상태 0. 통상 1. 치우침 이브
	
	public int getBraveSpeed() {
		return _braveSpeed;
	}

	public void setBraveSpeed(int i) {
		_braveSpeed = i;
	}
	
	private int _Lucifer;
	
	public int getLucifer() {
		return _Lucifer;
	}

	public void setLucifer(int i) {
		_Lucifer = i;
	}
	
	private int _halpas_faith_pvp_reduc;

	private boolean _halpas_armor;

	private int _halpas_armor_enchant;

	public int get_halpas_faith_pvp_reduc() {
		return _halpas_faith_pvp_reduc;
	}

	public void set_halpas_faith_pvp_reduc(int _halpas_faith_pvp_reduc) {
		this._halpas_faith_pvp_reduc = _halpas_faith_pvp_reduc;
	}

	public boolean is_halpas_armor() {
		return _halpas_armor;
	}

	public void set_halpas_armor(boolean _halpas_armor) {
		this._halpas_armor = _halpas_armor;
	}

	public int get_halpas_armor_enchant() {
		return _halpas_armor_enchant;
	}

	public void set_halpas_armor_enchant(int _halpas_armor_enchant) {
		this._halpas_armor_enchant = _halpas_armor_enchant;
	}
	
	public boolean isDestroyFear() {
		return destroyFear;
	}

	public void setDestroyFear(boolean destroyFear) {
		this.destroyFear = destroyFear;
	}

	public boolean isDestroyHorror() {
		return destroyHorror;
	}

	public void setDestroyHorror(boolean destroyHorror) {
		this.destroyHorror = destroyHorror;
	}

	private boolean destroyFear;
	private boolean destroyHorror;
	
}
