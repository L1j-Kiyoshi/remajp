package l1j.server.server.model.Instance;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import l1j.server.Config;
import l1j.server.GameSystem.DogFight.DogFight;
import l1j.server.server.ActionCodes;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.CharPetBuffTable;
import l1j.server.server.datatables.ExpTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.datatables.PetsSkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.CharPosUtil;
import l1j.server.server.model.L1Attack;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1PinkName;
import l1j.server.server.model.L1World;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.serverpackets.S_DoActionGFX;
import l1j.server.server.serverpackets.S_HPMeter;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_PetPack;
import l1j.server.server.serverpackets.S_PetWindow;
import l1j.server.server.serverpackets.S_RemoveObject;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;
import l1j.server.server.templates.L1PetSkill;
import l1j.server.server.templates.L1PetType;
import l1j.server.server.templates.L1Skills;
import l1j.server.server.utils.IntRange;

public class L1PetInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;
	
	private static final Random _Random = new Random(System.nanoTime());

	public boolean noTarget() {
		/** 3이라면 대기상태 */
		if (_currentPetStatus == 3) return true;
		/** 아니라면 왼만하면 케릭터 옆으로 모일수잇도록 세팅 */
		if (_petMaster != null && _petMaster.getMapId() == getMapId()) {
			if (getLocation().getTileLineDistance(_petMaster.getLocation()) > 2) {
				int dir = moveDirection(_petMaster.getMapId(), _petMaster.getX(), _petMaster.getY());
				/** 화면 밖이면 리턴해서 대기상태로 만듬 */
				if (dir == -1) {
					teleport(_petMaster.getX(), _petMaster.getY(), getMoveState().getHeading());
					return false;
				}
				setDirectionMove(dir);
				setSleepTime(calcSleepTime(getPassispeed(), MOVE_SPEED));
			}
			/** 8번인데 픽업상태가 맞는지 아닌지 체크하는부분 */
			if (_currentPetStatus == 8) DropItemGet();
		} else {
			_currentPetStatus = 3;
			return true;
		}
		return false;
	}
	

	@Override
	/** 어택 액션 정보 패킷 처리 */
	public void onAction(L1PcInstance player) {
		try{
			L1Character cha = this.getMaster();
			L1PcInstance master = (L1PcInstance) cha;
			if (master != null && master.isTeleport()) return;
			if (CharPosUtil.getZoneType(this) == 1) {
				L1Attack attack_mortion = new L1Attack(player, this);
				attack_mortion.action();
				attack_mortion = null;
				return;
			}
	
			if (player.checkNonPvP(player, this)) {
				return;
			}
	
			L1Attack attack = new L1Attack(player, this);
			if (attack.calcHit()) {
				attack.calcDamage();
			}
		
			attack.action();
			attack.commit();
			attack = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 수집상태 아이템 겟업 픽업 */
	public void DropItemGet() {
		try{
			if(_targetItem != null) return;
			for (L1Object obj : L1World.getInstance().getVisibleObjects(this, -1)) {
				if(!(obj instanceof L1GroundInventory)) continue;
				L1GroundInventory DropItem = (L1GroundInventory)obj;
				for (L1ItemInstance Item : DropItem.getItems()) {
					if(getMaster() == Item.getItemOwner()){
						_targetItem = Item;
						_targetItemList.add(_targetItem);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 상태 정보 체크 */
	public void setCurrentPetStatus(int i) {
		_currentPetStatus = i;
		if (_currentPetStatus == 5) {
			setHomeX(getX());
			setHomeY(getY());
		}
		
		if (_currentPetStatus == 10 ||
			_currentPetStatus == 7 ||
			_currentPetStatus == 3) {
			allTargetClear();
		} else {
			onNpcAI();
		}
		/** 아이템 정보 패킷 처리 삭제 */
		if (_currentPetStatus != 8) {
			if(_targetItemList != null){
				_targetItem = null;
				_targetItemList.clear();
			}
		}
	}
	
	/** 액션 ディレイ 줄수있도록 정리 */
	public void onNpcAI() {
		if (isAiRunning()) return;
		setActived(false);
		startAI();
	}
	
	/** 마스터가 공격하면 그대상에게 타켓 지정 */
	public void setMasterTarget(L1Character target) {
		if (target != null && (_currentPetStatus == 1 || _currentPetStatus == 5)) {
			setHate(target, 0);
			onNpcAI();
		}
	}
	
	/** 픽업 아이템 부분 정리 */
	public void pickupTargetItem(L1ItemInstance targetItem) {
		try{
			L1Inventory groundInventory = L1World.getInstance().getInventory(targetItem.getX(), targetItem.getY(), targetItem.getMapId());
			
			L1PcInstance Pc = (L1PcInstance)getMaster();
			if (targetItem.getItem().getItemId() == L1ItemId.ADENA) {
				L1ItemInstance inventoryItem = Pc.getInventory().findItemId(L1ItemId.ADENA);
				int inventoryItemCount = 0;
				if (inventoryItem != null) {
					inventoryItemCount = inventoryItem.getCount();
				}
				if ((long) inventoryItemCount + (long) targetItem.getCount() > 20000000000L) {
					Pc.sendPackets(new S_SystemMessage("소지하고 있는 아데나가 20억을 초과하게 됩니다."), true);
					return;
				}
			}
			
			int 추가무게 = Math.max(targetItem.getCount() * targetItem.getItem().getWeight() / 1000, 1);
			if (Pc.getInventory().calcWeightpercent(추가무게) >= 100) {
				Pc.sendPackets(new S_ServerMessage(82), true);
				return;
			}
			
			if (Pc.getInventory().checkAddItem(targetItem, targetItem.getCount()) == L1Inventory.OK) {
				if (Pc.isInParty()) { // 파티의 경우
					if (Pc.getParty().getLeader().getPartyType() == 1 && targetItem.isDropMobId() != 0) {
						List<L1PcInstance> _membersList = new ArrayList<L1PcInstance>();
						for (L1PcInstance realUser : L1World.getInstance().getVisiblePlayer(Pc, -1)) {
							if (Pc.getParty().isMember(realUser)) _membersList.add(realUser);
						}
						_membersList.add(Pc);
						if (targetItem.getItemId() == L1ItemId.ADENA) {
							if (_membersList.size() > 1) {
								int divAden = targetItem.getCount() / _membersList.size();
								for (int row = 0; row < _membersList.size(); row++) {
									if(_membersList.get(row).getId() == Pc.getId()){
										int modNum = targetItem.getCount() % _membersList.size();
										groundInventory.tradeItem(targetItem, divAden + modNum, _membersList.get(row).getInventory());
									}else groundInventory.tradeItem(targetItem, divAden, _membersList.get(row).getInventory());
									if (targetItem.isDropMobId() != 0) {
										L1Npc npc = NpcTable.getInstance().getTemplate(targetItem.isDropMobId());
										for (L1PcInstance partymember : Pc.getParty().getMembers()) {
											partymember.sendPackets(new S_ServerMessage(813, npc.get_name(), targetItem.getLogName(), partymember.getName()), true);
										}
										targetItem.setDropMobId(0);
									}
								}
							} else {
								groundInventory.tradeItem(targetItem, targetItem.getCount(), Pc.getInventory());
							}
						} else {// 아니면 다른 아이템인가?
							int luckuyNum = _Random.nextInt(_membersList.size());
							L1PcInstance luckyUser = _membersList.get(luckuyNum);
							groundInventory.tradeItem(targetItem, targetItem.getCount(), luckyUser.getInventory());
							if (targetItem.isDropMobId() != 0) {
								L1Npc npc = NpcTable.getInstance().getTemplate(targetItem.isDropMobId());
								for (L1PcInstance partymember : Pc.getParty().getMembers()) {
									partymember.sendPackets(new S_ServerMessage(813, npc.get_name(), targetItem.getLogName(), luckyUser.getName()), true);
								}
								targetItem.setDropMobId(0);
							}
						}
					} else { // 아니면 그냥인가?
						groundInventory.tradeItem(targetItem, targetItem.getCount(), Pc.getInventory());
						if (targetItem.isDropMobId() != 0) {
							L1Npc npc = NpcTable.getInstance().getTemplate(targetItem.isDropMobId());
							for (L1PcInstance partymember : Pc.getParty().getMembers()) {
								partymember.sendPackets(new S_ServerMessage(813, npc.get_name(), targetItem.getLogName(), Pc.getName()), true);
							}
							targetItem.setDropMobId(0);
						}
					}
					Pc.saveInventory();
				} else {
					groundInventory.tradeItem(targetItem, targetItem.getCount(), Pc.getInventory());
				}
			}
			light.turnOnOffLight();
			_targetItemList.remove(_targetItem);
			_targetItem = null;
			setSleepTime(100);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 펫 관련 액션 정리 */
	public void BonusPoint(int[] Point) {
		int PointTemp = Point[0] + Point[1] + Point[2];
		if(getBonusPoint() < PointTemp) return;
		setHunt(getHunt() + Point[0]);
		setSacred(getSacred() + Point[1]);
		setSurvival(getSurvival() + Point[2]);
		setBonusPoint(getBonusPoint() - PointTemp);
		L1PcInstance Pc = (L1PcInstance) getMaster();
		if(Point[2] != 0){
			getPetMr();
			setCurrentHp(getCurrentHp());
			Pc.sendPackets(new S_PetWindow(S_PetWindow.PatStatUpDate, this), true);
		}
		Pc.sendPackets(new S_PetWindow(S_PetWindow.PatBonusPoint, this), true);
		PetTable.UpDatePet(this);
	}
	
	/** 펫 관련 액션 정리 */
	public void PetName(String Name) {
		setName(Name);
		L1ItemInstance item = getMaster().getInventory().getItem(getItemObjId());
		Broadcaster.broadcastPacket(this, new S_ChangeName(getId(), Name), true);
		L1PcInstance Pc = (L1PcInstance) getMaster();
		Pc.sendPackets(new S_PetWindow(S_PetWindow.PatName, this), true);
		Pc.sendPackets(new S_PetWindow(item, this, false, false, true, false, false), true);
		PetTable.UpDatePet(this);
	}

	/** 투지 관련 패킷 처리 계산 */
	public void Fighting(int Exp) {
		/** 예거밤 상태라면 추가적으로 * 30프로 추가 해주도록 세팅 */
		if(SkillCheck(L1SkillId.YeagerNight)){
			Exp += Exp * 0.3;
			setFighting(Exp);
		}else setFighting(Exp);
		/** 투지상태가 10만이상이라면 부스터 효과 발동 */
		L1PcInstance Pc = (L1PcInstance) getMaster();
		if(getFighting() >= 100000){
			if(SkillCheck(L1SkillId.Fighting)){
				getSkillEffectTimerSet().removeSkillEffect(L1SkillId.Fighting);
				getSkillEffectTimerSet().setSkillEffect(L1SkillId.Fighting, 60 * 1000);
			}else getSkillEffectTimerSet().setSkillEffect(L1SkillId.Fighting, 60 * 1000);
			setFighting(0);
			setFriendship(getFriendship() + 1);
			/** 부스터용 패킷 */
			Pc.sendPackets(new S_PetWindow(S_PetWindow.PatPoint, this), true);
			Pc.sendPackets(new S_PetWindow(this, true), true);
		}
		Pc.sendPackets(new S_PetWindow(S_PetWindow.PatPoint, this), true);
	}
	
	/** 투지 관련 패킷 처리 계산 */
	public void Fighting2(int Exp) {
		/** 예거밤 상태라면 추가적으로 * 30프로 추가 해주도록 세팅 */
		if(SkillCheck(L1SkillId.YeagerNight)){
			Exp += Exp * 0.3;
			setFighting(Exp);
		}else setFighting(Exp);
		/** 투지상태가 10만이상이라면 부스터 효과 발동 */
		L1PcInstance Pc = (L1PcInstance) getMaster();
		if(getFighting() >= 100000){
			if(SkillCheck(L1SkillId.Fighting)){
				getSkillEffectTimerSet().removeSkillEffect(L1SkillId.Fighting);
				getSkillEffectTimerSet().setSkillEffect(L1SkillId.Fighting, 60 * 1000);
			}else getSkillEffectTimerSet().setSkillEffect(L1SkillId.Fighting, 60 * 1000);
			setFighting(0);
			setFriendship(getFriendship() + 100);
			/** 부스터용 패킷 */
			Pc.sendPackets(new S_PetWindow(S_PetWindow.PatPoint, this), true);
			Pc.sendPackets(new S_PetWindow(this, true), true);
		}
		Pc.sendPackets(new S_PetWindow(S_PetWindow.PatPoint, this), true);
	}
	
	/** 펫 보너스 스텟관련 정보 패킷 */
	public void onFinalAction(int Action, Object ObjectId) {
		if (Action == 0) return;
		if (_petMaster != null) {
			/** 액션이 구번이라면 어떠한 상태에서도
			 * 타켓에게 공격 하도록 세팅 최우선*/
			if(Action == 9){
				_target = (L1Character)ObjectId;
			}else if(Action == 67){
				int PetAction = Action + _Random.nextInt(2);
				Broadcaster.broadcastPacket(this, new S_DoActionGFX(this.getId(), PetAction), true);
				if(Action == PetAction && getLevel() >= 41){
					int PetLevel = (getLevel() - 41) / 10;
					Broadcaster.broadcastPacket(this, new S_SkillSound(this.getId(), getPetType().getPetEffect() + (PetLevel >= 3 ? 3 : PetLevel)), true);
				}
			}else setCurrentPetStatus(Action);
		}
	}

	public L1PetInstance(L1Npc template, L1PcInstance master, L1Pet l1pet) {
		super(template);

		_petMaster = master;
		_itemObjId = l1pet.getItemObjid();
		_type = PetTypeTable.getInstance().get(template.get_npcId());

		setMaster(master);
		setX(master.getX() + _Random.nextInt(5) - 2);
		setY(master.getY() + _Random.nextInt(5) - 2);
		setMap(master.getMapId());
		getMoveState().setHeading(5);
		setLightSize(template.getLightSize());
		
		setId(l1pet.getObjid());
		setName(l1pet.getName());
		setPetInfo(l1pet.getPetInfo());
		setLevel(l1pet.getLevel());
		setExp(l1pet.getExp());
		
		/** 멧 업데이트 관련 변수들 */
		setHunt(l1pet.getHunt());
		setSurvival(l1pet.getSurvival());
		setSacred(l1pet.getSacred());
		setBonusPoint(l1pet.getBonusPoint());
		
		setElixir(l1pet.getElixir());
		setElixirHunt(l1pet.getElixirHunt());
		setElixirSurvival(l1pet.getElixirSurvival());
		setElixirSacred(l1pet.getElixirSacred());
		
		PetsSkillsTable.LoadSkills(this);
		CharPetBuffTable.LoadBuff(this);
		getPetAc(); getPetMr();
		
		setMaxHp(l1pet.getMaxHp());
		if(l1pet.isPetDead()){
			setCurrentHp(l1pet.getMaxHp()/2);
		}else setCurrentHp(l1pet.getCurrentHp());
		
		setFriendship(l1pet.getFriendship());
		if(l1pet.getFighting() >= 100000){
			setFighting(0);
		}else setFighting(l1pet.getFighting());
		
		setPetSummons(true);
		setDeadExp(l1pet.isPetDeadExp());
		_currentPetStatus = 1;
		
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
		
		master.addPet(this);
		onNpcAI();	
	}
	
	public void deletePet() {
		try {		
			setPetSummons(false);
			_master.sendPackets(new S_SkillSound(getId(), 5935), true);
			Broadcaster.broadcastPacket(_master, new S_SkillSound(getId(), 5935), true);			
			if (_master.getPetList() != null) _master.removePet(this);
			
			/** 펫 버프 저장  */
			PetsSkillsTable.SaveSkills(this, true);
			CharPetBuffTable.SaveBuff(this);
			PetTable.UpDatePet(this);
		} catch (Exception e) {
		} finally {
			deleteMe();
		}
	}
	
	public synchronized void resurrect(int hp) {
		setCurrentHp(hp);
		setDead(false);
		setActionStatus(0);

		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			pc.sendPackets(new S_RemoveObject(this), true);
			pc.getNearObjects().removeKnownObject(this);
			pc.updateObject();
		}
		L1SkillUse skill = new L1SkillUse();
		skill.handleCommands(null, L1SkillId.CANCELLATION, getId(), getX(), getY(), null, 0, L1SkillUse.TYPE_LOGIN, this);
	}

	/** 펫 구입 부분 */
	public L1PetInstance(L1PetType Target, L1PcInstance Master, int itemid) {
		super(null);
		try {
			_petMaster = Master;
			_itemObjId = itemid;
			_type = Target;
			
			setId(ObjectIdFactory.getInstance().nextId());
			
			L1Npc TempNpc = NpcTable.getInstance().getTemplate(Target.getBaseNpcId());
			setting_template(TempNpc);
			
			setPetInfo(Target.getPetInfo());
			
			setHunt(0);
			setSurvival(0);
			setSacred(0);
			setBonusPoint(0);
			
			setElixir(0);
			setElixirHunt(0);
			setElixirSurvival(0);
			setElixirSacred(0);
			
			setFighting(0);
			
			setDead(false);
			setDeadExp(false);
			setProduct(true);
			
			/** 정보 업데이트 후에 아이템 생성 */
			PetTable.getInstance().StoreNewPet(this, getId(), itemid);
			Master.getInventory().PetStoreItem(itemid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	@Override
	/** 펫 데미지를 먹고 펫 다이 관련 된 메소드 */
	public void receiveDamage(L1Character attacker, int damage) {
		L1PcInstance Master = (L1PcInstance)getMaster();
		if (getCurrentHp() > 0 && !isDead()) {
			if (damage > 0) {
				//setHate(attacker, 0);
				if (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.FOG_OF_SLEEPING)) {
					getSkillEffectTimerSet().removeSkillEffect(L1SkillId.FOG_OF_SLEEPING);
				} else if (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.PHANTASM)) {
					getSkillEffectTimerSet().removeSkillEffect(L1SkillId.PHANTASM);
				} else if (getSkillEffectTimerSet().hasSkillEffect(L1SkillId.DARK_BLIND)) {
					getSkillEffectTimerSet().removeSkillEffect(L1SkillId.DARK_BLIND);
				}
				if (attacker instanceof L1PcInstance ||
					attacker instanceof L1PetInstance) {
					L1PinkName.onAction(this, attacker);
				}
			}

			int newHp = getCurrentHp() - damage;
			if (newHp <= 0) {
				/** 버경 레이스 용 펫 이라면 */
				if(DogFight.getInstance().getDogFight(this)){
					synchronized (this) {
						if (getTarget().isDead()) return;
						setCurrentHp(0);
						setDead(true);
					}
				}else{
					setCurrentHp(0);
					setDead(true);
				}
				
				if(Master == null){
					setActionStatus(ActionCodes.ACTION_Die);
					getMap().setPassable(getLocation(), true);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId(), ActionCodes.ACTION_Die), true);
					if(SkillCheck(L1SkillId.Fighting)) getSkillEffectTimerSet().removeSkillEffect(L1SkillId.Fighting);	
				}else{
					L1ItemInstance item = Master.getInventory().findItemId(43203);
					if(getLevel() < 30 || item != null){
						if(getLevel() >= 30){
							Master.getInventory().removeItem(item);
							Master.sendPackets(new S_ServerMessage(638, item.getName()), true);
						}
					}else DeathPetExp();
					
					if (isPinkName()) {
						setPinkName(false);
						getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_PINK_NAME);
					}
					
					if(SkillCheck(L1SkillId.Fighting)){
						getSkillEffectTimerSet().removeSkillEffect(L1SkillId.Fighting);			
					}
					
					/** 펫이 삭제 될때 무조건 디비 업데이트 해주고 정보 갱신한다 */
					PetTable.UpDatePet(this);
					
					setActionStatus(ActionCodes.ACTION_Die);
					getMap().setPassable(getLocation(), true);
					Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId() ,ActionCodes.ACTION_Die), true);
					
					/** 펫 아이템 정보에서 죽음 패킷 처리 하도록 세팅 */
					L1ItemInstance PetItem = getMaster().getInventory().getItem(getItemObjId());
					Master.sendPackets(new S_PetWindow(PetItem, this, false, true, false, false, false), true);
					
					GeneralThreadPool.getInstance().schedule(new Death((L1PetInstance)this), 20000);
				}
			} else {
				setCurrentHp(newHp);
			}
		} else if (!isDead()) {
			setCurrentHp(0);
			setDead(true);
			
			if(Master == null){
				setActionStatus(ActionCodes.ACTION_Die);
				getMap().setPassable(getLocation(), true);
				Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId() ,ActionCodes.ACTION_Die), true);
				if(SkillCheck(L1SkillId.Fighting)) getSkillEffectTimerSet().removeSkillEffect(L1SkillId.Fighting);	
			}else{
				L1ItemInstance item = Master.getInventory().findItemId(43203);
				if(getLevel() <= 30 || item != null){
					Master.getInventory().removeItem(item);
					Master.sendPackets(new S_ServerMessage(638, item.getName()), true);
				}else DeathPetExp();
				
				if (isPinkName()) {
					setPinkName(false);
					getSkillEffectTimerSet().removeSkillEffect(L1SkillId.STATUS_PINK_NAME);
				}
				
				if(SkillCheck(L1SkillId.Fighting)){
					getSkillEffectTimerSet().removeSkillEffect(L1SkillId.Fighting);			
				}
				
				setActionStatus(ActionCodes.ACTION_Die);
				getMap().setPassable(getLocation(), true);
				Broadcaster.broadcastPacket(this, new S_DoActionGFX(getId() ,ActionCodes.ACTION_Die), true);
				
				/** 펫 아이템 정보에서 죽음 패킷 처리 하도록 세팅 */
				L1ItemInstance PetItem = getMaster().getInventory().getItem(getItemObjId());
				Master.sendPackets(new S_PetWindow(PetItem, this, false, true, false, false, false), true);
			
				GeneralThreadPool.getInstance().schedule(new Death((L1PetInstance)this), 30000);
			}
		}
		if(isDogFight()) Broadcaster.wideBroadcastPacket(this, new S_HPMeter(this), true);
		if(Master != null) Master.sendPackets(new S_PetWindow(S_PetWindow.PatHp, (L1PetInstance)this), true);
	}
	
	class Death implements Runnable {
		L1PetInstance _Pet;
		
		public Death(L1PetInstance l1PetInstance) {
			_Pet = l1PetInstance;
		}
		
		@Override
		public void run() {
			try {
				if(!_Pet.isPetSummons() || !_Pet.isDead()) return;
				/** 월드에 마스터가있다면 리맴버 펫으로 펫을 삭제 해주고 아니라면 펫삭제시키면서 리스트도 빼줌 */
				L1PcInstance PetMaster = L1World.getInstance().getPlayer(_Pet.getMaster().getName());
				/** 죽은 케릭터 정보 삭제 */
				_Pet.deletePet();
				/** 펫을 미착용해제 상태로 만듬 */
				if(PetMaster != null){
					L1ItemInstance PetItem = PetMaster.getInventory().getItem(_Pet.getItemObjId());
					PetMaster.sendPackets(new S_PetWindow(PetItem, _Pet, true, false, false, false, false), true);
					PetMaster.removePet(_Pet);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/** 다이 관련 펫 경험치 마이너스 */
	public void DeathPetExp() {
		int oldLevel = getLevel(), exp = getExp();
		int needExp = ExpTable.getNeedExpNextLevel(oldLevel);
		exp -= (int)(needExp * 0.05);
		setExp(exp);
		if(!isDeadExp()){
			setDeadExp(true);
			PetTable.UpDatePet(this);
		}
	}
	
	/** 펫 전용 어택 타입은 따로 관리 스킬에대한 이속 공속 체크 */
	public int calcSleepTime(int sleepTime, int type) {
		double sleep = 0, sleepTimeSpeed = sleepTime;
		switch (type) {
			case MOVE_SPEED:
				if(SkillCheck(L1SkillId.Fighting)){
					sleepTimeSpeed -= sleepTime * 0.20;
				}
				sleep = SkillsSpeed(춤추는날개);
				if(sleep >= 9){
					sleepTimeSpeed -= sleepTimeSpeed * (sleep / 100);
				}else sleepTimeSpeed -= sleepTimeSpeed * (sleep / 10);
				break;
				
			case ATTACK_SPEED:
				if(SkillCheck(L1SkillId.Fighting)){
					sleepTimeSpeed -= sleepTime * 0.20;
				}
				sleep = SkillsSpeed(칼바람발톱);
				if(sleep >= 9){
					sleepTimeSpeed -= sleepTimeSpeed * (sleep / 100);
				}else sleepTimeSpeed -= sleepTimeSpeed * (sleep / 10);
				break;
		}
		return (int)sleepTimeSpeed;
	}
	
	/** 펫 퀘스트나 복구 관련 경험피 패킷 처리*/
	public void AddExpPet(int exp) {
		L1PcInstance pc = (L1PcInstance)this.getMaster();
		int levelBefore = getLevel();
		int totalExp = (int)(getExp() + exp);
		if (totalExp >= ExpTable.getExpByLevel(100)) {
			totalExp = ExpTable.getExpByLevel(100) - 1;
		}
		setExp(totalExp);
		setLevel(ExpTable.getLevelByExp(totalExp));
		pc.sendPackets(new S_PetWindow(S_PetWindow.PatExp, this), true);

		int gap = getLevel() - levelBefore;
		for (int i = 1; i <= gap; i++) {
			IntRange hpUpRange = getPetType().getHpUpRange();
			addMaxHp(hpUpRange.randomValue());
			setCurrentHp(getMaxHp());
		}
		try {
			pc.sendPackets(new S_PetPack(this, pc), true);
		} catch (Exception e) {}
		/** 렙업이라면 기입 */
		if (gap != 0) {
			/** 펫관련 ac 세팅 새로 해주기 */
			getPetAc();
			getPetMr();
			/** 펫 관련 정보 보너스 스텟 관련도 체크 */
			int BonusPoint = getHunt() + getSurvival() + getSacred();
			int BonusPointTemp = 0;
			if(getLevel() > 50){
				BonusPointTemp = (5 + (getLevel() - 50)) - BonusPoint;
			}else BonusPointTemp = (getLevel() / 10) - BonusPoint;
			if(BonusPointTemp > 0) setBonusPoint(BonusPointTemp);
			L1ItemInstance item = pc.getInventory().getItem(getItemObjId());
			
			pc.sendPackets(new S_ItemName(item), true);;
			pc.sendPackets(new S_PetWindow(S_PetWindow.PatLevel, this), true);
			pc.sendPackets(new S_PetWindow(S_PetWindow.PatStatUpDate, this), true);
			try {
				pc.sendPackets(new S_ServerMessage(320, getName()), true);
			} catch (Exception e) {}
		}
	}

	public void dropItem() {
		L1Inventory targetInventory = L1World.getInstance().getInventory(getX(), getY(), getMapId());
		List<L1ItemInstance> items = _inventory.getItems();
		int size = _inventory.getSize();
		L1ItemInstance item = null;
		for (int i = 0; i < size; i++) {
			item = items.get(0);
			item.setEquipped(false);
			_inventory.tradeItem(item, item.getCount(), targetInventory);
		}
	}

	public void setTarget(L1Character target) {
		if (target != null && (_currentPetStatus == 1 || _currentPetStatus == 2 || _currentPetStatus == 5)) {
			setHate(target, 0);
			onNpcAI();
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.getNearObjects().addKnownObject(this);
		perceivedFrom.sendPackets(new S_PetPack(this, perceivedFrom), true);
		if (perceivedFrom == getMaster()) setCurrentHp(getCurrentHp());
		if (isDogFight()) perceivedFrom.sendPackets(new S_HPMeter(this), true);
		if (isDead()) perceivedFrom.sendPackets(new S_DoActionGFX(getId(),ActionCodes.ACTION_Die), true);
	}

	@Override
	public void onItemUse() {
		if (!isActived()) {
			useItem(USEITEM_HASTE, 100);
		}
		if (getCurrentHp() * 100 / getMaxHp() < 40) {
			useItem(USEITEM_HEAL, 100);
		}
	}

	@Override
	public void onGetItem(L1ItemInstance item) {
		if (getNpcTemplate().get_digestitem() > 0) {
			setDigestItem(item);
		}
		Arrays.sort(healPotions);
		Arrays.sort(haestPotions);
		if (Arrays.binarySearch(healPotions, item.getItem().getItemId()) >= 0) {
			if (getCurrentHp() != getMaxHp()) {
				useItem(USEITEM_HEAL, 100);
			}
		} else if (Arrays.binarySearch(haestPotions, item.getItem().getItemId()) >= 0) {
			useItem(USEITEM_HASTE, 100);
		}
	}
	
	public boolean SkillCheck(int L1SkillId) {
		/** 펫 버프 있는가 체크 부분 */
		if(getSkillEffectTimerSet().hasSkillEffect(L1SkillId)){
			return true;
		}else return false;
	}
	
	/** 펫 이름 관련 메소드 */
	public boolean isInvalidName(String name) {
		int numOfNameBytes = 0;
		try {
			numOfNameBytes = name.getBytes("EUC-KR").length;
		} catch (UnsupportedEncodingException e) {
			return false;
		}
		
		int i = Config.이름확인(name.toCharArray());

		if (i < 0) return true;

		if (isAlphaNumeric(name)) return false;

		if (5 < (numOfNameBytes - name.length()) || 12 < numOfNameBytes) {
			return false;
		}
		return true;
	}
	
	private static boolean isAlphaNumeric(String s) {
		boolean flag = true;
		char ac[] = s.toCharArray();
		int i = 0;
		do {
			if (i >= ac.length) {
				break;
			}
			if (!Character.isLetterOrDigit(ac[i])) {
				flag = false;
				break;
			}
			i++;
		} while (true);
		ac = null;
		return flag;
	}

	@Override
	public void setCurrentHp(int i) {
		super.setCurrentHp(i);
	}

	@Override
	public void setCurrentMp(int i) {
		super.setCurrentMp(i);
	}

	/** 보너스 스텟 정보 패킷 처리 */
	public int getBonusHp() {
		int BonusHp = 0;
		BonusHp += getSurvival() * 15;
		BonusHp += getElixirSurvival() * 15;
		if(_PetSkills != null){
			BonusHp += SkillsTable(돌격의심장);
		}
		return BonusHp;
	}
	
	/** 펫용 ac관련 처리 */
	public void getPetAc() {
		int Ac = NpcTable.getInstance().getTemplate(getNpcId()).get_ac();
		Ac -= getLevel() / 3;
		int Bonus = getSurvival() + getElixirSurvival();
		if(Bonus >= 20) Ac -= (Bonus -  10) / 10;
		if(_PetSkills != null){
			Ac -= SkillsTable(강철갑옷);
		}
		getAC().setAc(Ac);
	}
	
	/** 펫용 ac관련 처리 */
	public void getPetMr() {
		int Mr = NpcTable.getInstance().getTemplate(getNpcId()).get_mr();
		Mr += getLevel() / 2;
		Mr += getSurvival() + getElixirSurvival();
		if(_PetSkills != null){
			Mr += SkillsTable(영험한의지);
		}
		getResistance().setBaseMr(Mr);
	}
	
	private L1PcInstance _petMaster;
	private L1PetType _type;
	private int _currentPetStatus;
	private int _itemObjId;
	private int _Food = 0;
	private int _FoodTime = 0;

	public int getCurrentPetStatus() {
		return _currentPetStatus;
	}

	public int getItemObjId() {
		return _itemObjId;
	}
	
	public int getFood() {
		return _Food;
	}

	public void setFood(int i) {
		_Food = i;
	}

	public int getFoodTime() {
		return _FoodTime;
	}

	public void setFoodTime(int i) {
		_FoodTime = i;
	}
	
	/** 펫 관력 메소드들 */
	private int _Hunt;

	public int getHunt() {
		return _Hunt;
	}

	public void setHunt(int i) {
		_Hunt = i;
	}

	private int _Survival;

	public int getSurvival() {
		return _Survival;
	}

	public void setSurvival(int i) {
		_Survival = i;
	}
	
	private int _Sacred;

	public int getSacred() {
		return _Sacred;
	}

	public void setSacred(int i) {
		_Sacred = i;
	}
	
	private int _BonusPoint;

	public int getBonusPoint() {
		return _BonusPoint;
	}

	public void setBonusPoint(int i) {
		_BonusPoint = i;
	}
	
	private int _Elixir;

	public int getElixir() {
		return _Elixir;
	}

	public void setElixir(int i) {
		_Elixir = i;
	}
	
	/** 펫 관력 메소드들 */
	private int _ElixirHunt;

	public int getElixirHunt() {
		return _ElixirHunt;
	}

	public void setElixirHunt(int i) {
		_ElixirHunt = i;
	}

	private int _ElixirSurvival;

	public int getElixirSurvival() {
		return _ElixirSurvival;
	}

	public void setElixirSurvival(int i) {
		_ElixirSurvival = i;
	}
	
	private int _ElixirSacred;

	public int getElixirSacred() {
		return _ElixirSacred;
	}

	public void setElixirSacred(int i) {
		_ElixirSacred = i;
	}
	
	/** 우정 포인트 관련 */
	private int _Friendship;

	public int getFriendship() {
		return _Friendship;
	}

	public void setFriendship(int i) {
		_Friendship = i;
	}
	
	/** 투지 포인트 */
	private int _Fighting;

	public int getFighting() {
		return _Fighting;
	}

	public void setFighting(int i) {
		if(i == 0){
			_Fighting = i;
		}else if(_Fighting + i >= 100000){
			_Fighting = 100000;
		}else{
			_Fighting += i;
		}
	}
	
	/** 투지 포인트  */
	private int _FightingPoint;

	public int getFightingPoint() {
		return _FightingPoint;
	}

	public void setFightingPoint(int i) {
		_FightingPoint = i;
	}
	
	/** 투지 관련 크리티컬 콤보 */
	private int _FightingCombo;

	public int getFightingCombo() {
		return _FightingCombo;
	}

	public void setFightingCombo(int i) {
		_FightingCombo = i;
	}
	
	private L1Character _FightingTarget;

	public L1Character getFightingTarget() {
		return _FightingTarget;
	}

	public void setFightingTarget(L1Character i) {
		_FightingTarget = i;
	}

	/** 펫 관련 정보 */
	private int _PetInfo;

	public int getPetInfo() {
		return _PetInfo;
	}

	public void setPetInfo(int i) {
		_PetInfo = i;
	}

	public L1PetType getPetType() {
		return _type;
	}
	
	/** 펫관련 hp 회복 & 투지 관련 메소드 */
	private int _HpcurPoint;
	
	public void setHpcurPoint(int num) {
		_HpcurPoint = num;
	}

	public int getHpcurPoint() {
		return _HpcurPoint;
	}
	
	/** 펫이 다이 상태이면 리스하고 접속해도 팻 정보에 죽음 효과 표기 */
	private boolean _DeadExp;
	
	public boolean isDeadExp() {
		return _DeadExp;
	}

	public void setDeadExp(boolean flag) {
		_DeadExp = flag;
	}
	
	/** 망각 부분 거래 가능하도록 세팅 하는거 */
	private boolean _Product;
	
	public boolean isProduct() {
		return _Product;
	}

	public void setProduct(boolean flag) {
		_Product = flag;
	}
	
	/** 펫 스킬 관련 정리 */
	private final Map<Integer, L1PetSkill> _PetSkills = new HashMap<Integer, L1PetSkill>();
	
	public Map<Integer, L1PetSkill> getPetSkillsList() {
		return _PetSkills;
	}
	
	public L1PetSkill getPetSkills(int PetSkills) {
		if(_PetSkills.containsKey(PetSkills)){
			return _PetSkills.get(PetSkills);
		}
		return null;
	}
	
	public int getPetSkillLevel(int PetSkills) {
		if(_PetSkills.containsKey(PetSkills)){
			return _PetSkills.get(PetSkills).getSkillLevel();
		}
		return 0;
	}
	
	public ArrayList<L1PetSkill> ArrayPetSkills(){
		ArrayList<L1PetSkill> ArraySkills = new ArrayList<L1PetSkill>();
		for (L1PetSkill Temp : _PetSkills.values()) {
			ArraySkills.add(Temp);
		}
		return ArraySkills;
	}
	
	public void setPetSkills(int SkillNumber, L1PetSkill Skill) {
		if(!_PetSkills.containsKey(SkillNumber)){
			_PetSkills.put(SkillNumber, Skill);
		}
	}
	
	public void addPetSkills(ArrayList<L1PetSkill> PetSkill) {
		for (L1PetSkill Temp : PetSkill) {
			setPetSkills(Temp.getSkillNumber(), Temp);
		}
	}
	
	/** 펫 스킬 체크 */
	public boolean getPetSkills(ArrayList<L1PetSkill> PetSkills) {
		int i = 0, SkillNumber = 0;
		boolean[] SkillChak = new boolean[PetSkills.size()];
		for (L1PetSkill Temp : PetSkills) {
			SkillNumber = Temp.getSkillNumber();
			SkillChak[i] = false;
			if(_PetSkills.containsKey(SkillNumber)){
				if(_PetSkills.get(SkillNumber).getSkillLevel() >= 4){
					SkillChak[i] = true;
				}
			}else return true;
		}
		for (i = 0; i < SkillChak.length; i++) {
			if(!SkillChak[i]) return false;
		}
		return true;
	}
	
	public void RemovePetSkills() {
		_PetSkills.clear();
	}
	
	public static final int 칠흑의발톱 = 1;
	public static final int 천리안 = 2;
	public static final int 포식자 = 3;
	public static final int 백상아리이빨 = 4;
	public static final int 피비릿내 = 5;
	public static final int 강철갑옷 = 7;
	public static final int 만월의재생 = 8;
	public static final int 먹성 = 9;
	public static final int 철갑등껍질 = 10;
	public static final int 돌격의심장 = 11;
	public static final int 영험한의지 = 12;
	public static final int 춤추는날개 = 15;
	public static final int 칼바람발톱 = 16;
	
	/** 기운 관련 */
	public static final int 물의기운 = 18;
	public static final int 불의기운 = 19;
	public static final int 바람의기운 = 20;
	public static final int 땅을기운 = 21;
	public static final int 빛의기운 = 22;
	
	public static final int 도살의발톱 = 23;
	public static final int 미소의재생 = 24;
	
	/** 스킬 관련 */
	public static final int 야성각성 = 14;
	public static final int 야성기합 = 17;
	public static final int 야성증폭 = 13;
	
	
	/** 스킬 업데이트 전용 */
	public void SkillsUpdate(int SkillNumber, int SkillLevel) {
		L1PcInstance Pc = (L1PcInstance) getMaster();
		switch (SkillNumber) {
			case 춤추는날개:
			case 칼바람발톱:
				Pc.sendPackets(new S_PetWindow(this, false), true);
				break;
				
			case 강철갑옷:
				getPetAc();
				Pc.sendPackets(new S_PetWindow(S_PetWindow.PatStatUpDate, this), true);
				break;
				
			case 돌격의심장:
				Pc.sendPackets(new S_PetWindow(S_PetWindow.PatStatUpDate, this), true);
				break;
				
			case 영험한의지:
				getPetMr();
				Pc.sendPackets(new S_PetWindow(S_PetWindow.PatStatUpDate, this), true);
				break;
		}
	}
	
	public int[] SkillsBlood() {
		int[] SkillBlood1 = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 13 }; 
		int[] SkillBlood2 = {0, 5, 10, 15, 20, 25, 30, 35, 42, 49, 58 }; 
		int PetSkillLevel = getPetSkillLevel(피비릿내);
		return new int[] { SkillBlood1[PetSkillLevel], SkillBlood2[PetSkillLevel] };
	}


	public int SkillsTable(int SkillNumber) {
		switch (SkillNumber) {
			case 포식자:
				int[] SkillCombo = {0, 10, 20, 30, 40, 50, 60, 70, 90, 110, 150 }; 
				return SkillCombo[getPetSkillLevel(SkillNumber)];
				
			case 강철갑옷:
				int[] SkillAc = {0, 3, 6, 9, 12, 15, 18, 21, 26, 32, 40 }; 
				return SkillAc[getPetSkillLevel(SkillNumber)];
				
			case 미소의재생:
			case 만월의재생:
				int[] SkillHpHit = {0, 1, 2, 3, 4, 5, 6, 7, 10, 13, 20 }; 
				return SkillHpHit[getPetSkillLevel(SkillNumber)];
				
			case 철갑등껍질:
				int[] SkillReduction = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 15 }; 
				return SkillReduction[getPetSkillLevel(SkillNumber)];
				
			case 돌격의심장:
				int[] SkillHp = {0, 50, 100, 150, 200, 250, 300, 350, 450, 550, 700 }; 
				return SkillHp[getPetSkillLevel(SkillNumber)];
				
			case 영험한의지:
				int[] SkillMr = {0, 3, 6, 9, 12, 15, 18, 21, 27, 33, 42 }; 
				return SkillMr[getPetSkillLevel(SkillNumber)];
				
			case 칠흑의발톱:
				int[] SkillDmg = {0, 2, 4, 6, 8, 10, 12, 14, 17, 20, 25 }; 
				return SkillDmg[getPetSkillLevel(SkillNumber)];
				
			case 도살의발톱:
				int[] SkillHipDmg = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 15 }; 
				return SkillHipDmg[getPetSkillLevel(SkillNumber)];
				
			case 천리안:
				int[] SkillHit = {0, 3, 6, 9, 12, 15, 18, 21, 27, 33, 45 }; 
				return SkillHit[getPetSkillLevel(SkillNumber)];
				
			case 백상아리이빨:
				int[] SkillDmgReduction = {0, 2, 4, 6, 8, 10, 12, 14, 17, 20, 25 }; 
				return SkillDmgReduction[getPetSkillLevel(SkillNumber)];
				
			case 먹성:
				int[] SkillPotion = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillPotion[getPetSkillLevel(SkillNumber)];
				
			case 물의기운:
				int[] SkillWater = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillWater[getPetSkillLevel(SkillNumber)];
				
			case 불의기운:
				int[] SkillFire = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillFire[getPetSkillLevel(SkillNumber)];
				
			case 바람의기운:
				int[] SkillWind = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillWind[getPetSkillLevel(SkillNumber)];
				
			case 땅을기운:
				int[] SkillEarth= {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillEarth[getPetSkillLevel(SkillNumber)];
				
			case 빛의기운:
				int[] SkillRay = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillRay[getPetSkillLevel(SkillNumber)];			
				
			case 야성각성:
				int[] SkillWake = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillWake[getPetSkillLevel(SkillNumber)];
				
			case 야성기합:
				int[] SkillPunishment = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillPunishment[getPetSkillLevel(SkillNumber)];
				
			case 야성증폭:
				int[] SkillAmplification = {0, 1, 2, 3, 4, 5, 6, 7, 9, 11, 14 }; 
				return SkillAmplification[getPetSkillLevel(SkillNumber)];			
				
				
		}
		return 0;
	}
	
	/** 공속 이속 관련 */
	public double SkillsSpeed(int SkillNumber) {
		switch (SkillNumber) {
			case 춤추는날개:
				double[] SkillMove = {0, 5, 9.80, 14.30, 18.50, 22.60, 26.50, 30.20, 33.70, 37.00, 46.40 }; 
				return SkillMove[getPetSkillLevel(춤추는날개)];
				
			case 칼바람발톱:
				double[] SkillAttack = {0, 5, 9.80, 14.30, 18.50, 22.60, 26.50, 30.20, 33.70, 37.00, 46.40 }; 
				return SkillAttack[getPetSkillLevel(칼바람발톱)];
		}
		return 0;
	}

	/** 공속 이속 관련 패킷 정리 */
	public double SpeedTime(int type) {
		/** 스피드 퍼센테이지 계산 */
		double SpeedTime = 0;
		switch (type) {
			case MOVE_SPEED:
				if(SkillCheck(L1SkillId.Fighting)) SpeedTime += 20;
				SpeedTime += SkillsSpeed(춤추는날개);
				break;
				
			case ATTACK_SPEED:
				if(SkillCheck(L1SkillId.Fighting)) SpeedTime += 20;
				SpeedTime += SkillsSpeed(칼바람발톱);
				break;
		}
		return SpeedTime;
	}
	
	/** 속성데미지의 대한 정보 처리 */
	public int[] AttributeDmg() {
		int SkillWake = 5, StatDmg = getSacred() + getElixirSacred();
		int AttributeDmg = 0, AttributeImpact = 3671;
		/** 속성에대한 정보 패킷 */
		switch (getNpcTemplate().get_weakAttr()) {
			case L1Skills.ATTR_EARTH:
				AttributeDmg = SkillsTable(땅을기운) + StatDmg; 
				break;
			
			case L1Skills.ATTR_FIRE:
				AttributeDmg = SkillsTable(불의기운) + StatDmg; 
				break;
				
			case L1Skills.ATTR_WATER:
				AttributeDmg = SkillsTable(물의기운) + StatDmg; 
				break;
				
			case L1Skills.ATTR_WIND:
				AttributeDmg = SkillsTable(바람의기운) + StatDmg; 
				break;
				
			case L1Skills.ATTR_RAY: 				
				AttributeDmg = SkillsTable(빛의기운) + StatDmg; 
				break;
		}
		return new int[]{ SkillWake, AttributeDmg,  AttributeImpact };
	}
	
	/** 속성데미지의 대한 정보 처리 */
	public int[] AttributeCriticalDmg() {
		int StatDmg = getSacred() + getElixirSacred();
		int SkillWake = SkillsTable(야성각성);
		int AttributeDmg = 0, AttributeImpact = 0;
		/** 속성 크리티컬의 임팩트정보 */
		int PetLevel = getLevel();
		if(PetLevel > 40){
			PetLevel = (PetLevel - 41) / 10;
			if(PetLevel == 0) AttributeImpact = 17318;
			if(PetLevel == 1) AttributeImpact = 17320;
			if(PetLevel == 2) AttributeImpact = 17322;
			if(PetLevel >= 3) AttributeImpact = 17324;
			/** 속성에대한 정보 패킷 */
			switch (getNpcTemplate().get_weakAttr()) {
				case L1Skills.ATTR_EARTH:
					AttributeDmg = SkillsTable(땅을기운) + StatDmg; 
					break;
				
				case L1Skills.ATTR_FIRE:
					AttributeDmg = SkillsTable(불의기운) + StatDmg; 
					break;
					
				case L1Skills.ATTR_WATER:
					AttributeDmg = SkillsTable(물의기운) + StatDmg; 
					break;
					
				case L1Skills.ATTR_WIND:
					AttributeDmg = SkillsTable(바람의기운) + StatDmg; 
					break;
					
				case L1Skills.ATTR_RAY: 				
					AttributeDmg = SkillsTable(빛의기운) + StatDmg; 
					break;
			}
			if(SkillWake != 0){
				AttributeDmg += SkillsTable(야성기합);
				AttributeDmg += (AttributeDmg * SkillsTable(야성증폭)) / 100;
			}else SkillWake =+ 5;
			SkillWake += PetLevel;
			if(StatDmg >= 20){
				SkillWake += (StatDmg - 10) / 10;
				AttributeDmg += ((StatDmg - 10) / 10) * 5;
			}
			return new int[]{ SkillWake, AttributeDmg,  AttributeImpact };
		}
		return null;
	}

	public static final int GOOD = 0;
	public static final int NORMAL = 1;
	public static final int BAD = 2;
	
	private static final String[] Name = { "진돗개", "허스키", "세퍼트", "도배르만", "캥그루", "토끼", "너구리", "고냥이", 
		"세인트", "콜리", "비글", "유니콘", "그리폰", "골드드래곤", "블루해츨링", "레드해츨링", "판다", "호랑이", "곰", "여우", "늑대"};

	public L1PetInstance(L1Npc template, int num, int x, int y, int Heading, boolean Judge) {
		super(template);
		setId(ObjectIdFactory.getInstance().nextId());
		setName(Name[num]);
		if(Judge){
			setNameId("#심판 "+Name[num]);
		}else{
			setNameId("#" + (num + 1) + " "+Name[num]);
		}
		setLocation(x, y, 4);
		if(Judge){
			getMoveState().setHeading(5);
		}else getMoveState().setHeading(Heading == 0 ? 4 : 0);
		setCondition(_Random.nextInt(3));
		setNumber(num);
		
		_type = PetTypeTable.getInstance().get(template.get_npcId());
		setPetInfo(_type.getPetInfo());
		
		/** 멧 업데이트 관련 변수들 */
		setHunt(10);
		setSurvival(10);
		setSacred(10);
		
		setLevel(80);
		setMaxHp(1000);
		setCurrentHp(getMaxHp());
		setLightSize(10);
		
		getPetAc(); getPetMr();
		_currentPetStatus = 1;
		
		setDogFight(true);
		if(Judge) setDogJudge(true);
		
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
	}
	
	private boolean _DogFight;

	public boolean isDogFight() {
		return _DogFight;
	}

	public void setDogFight(boolean i) {
		_DogFight = i;
	}
	
	private boolean _DogJudge;

	public boolean isDogJudge() {
		return _DogJudge;
	}
	
	public void setDogJudge(boolean i) {
		_DogJudge = i;
	}
	
	private boolean _Fabrication;

	public boolean isFabrication() {
		return _Fabrication;
	}

	public void setFabrication(boolean i) {
		_Fabrication = i;
	}

	private int _number; // 출전번호

	public void setNumber(int i) {
		_number = i;
	}

	public int getNumber() {
		return _number;
	}

	private int _rain; // 레인번호

	public void setRainNum(int i) {
		_rain = i;
	}

	public int getRainNum() {
		return _rain;
	}

	private int _condition; // 상태

	public void setCondition(int i) {
		_condition = i;
	}

	public int getCondition() {
		return _condition;
	}

	private int _win; // 승리 횟수

	public void setWin(int i) {
		_win = i;
	}

	public int getWin() {
		return _win;
	}

	private int _lose; // 패 횟수

	public void setLose(int i) {
		_lose = i;
	}

	public int getLose() {
		return _lose;
	}

	private String _winPoint; // 승률

	public void setWinPoint(String i) {
		_winPoint = i;
	}

	public String getWinPoint() {
		return _winPoint;
	}

	private double _dividend; // 배당

	public void setDividend(double i) {
		_dividend = i;
	}

	public double getDividend() {
		return _dividend;
	}
}