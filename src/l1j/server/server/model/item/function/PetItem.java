package l1j.server.server.model.item.function;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

import l1j.server.server.ObjectIdFactory;
import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetTypeTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ItemName;
import l1j.server.server.serverpackets.S_PetWindow;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1EtcItem;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1PetType;

@SuppressWarnings("serial")
public class PetItem extends L1ItemInstance {

	private static Random _Random = new Random(System.nanoTime());
	
	public PetItem(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			UsePetItem(pc, this.getId());
		}
	}

	/** 팻 서먼용 스래드 일단 체크 */
	private void UsePetItem(L1PcInstance pc, int itemObjectId) {
		if (pc.isInvisble()) return;
	
		L1ItemInstance useItem = pc.getInventory().getItem(itemObjectId);
		/** 아이템 ディレイ가 있다면 지연 */
		int delay_id = ((L1EtcItem)useItem.getItem()).get_delayid();
		if (delay_id != 0) { // 지연 설정 있어
			if (pc.hasItemDelay(delay_id) == true) {
				return;
			}
		}
		
		/** 펫 소환 들어왔는데 펫이 이미 가지고있다면 리턴 */
		L1PetInstance Pet = PetCheck(pc);
		switch (useItem.getItemId()) {
			case 43200: 
				if(Pet == null || Pet.isDead()) break;
				PetHeallingPotion(Pet, 25 + _Random.nextInt(10));
				pc.getInventory().removeItem(useItem, 1);
				break;
				
			case 43201: 
				if(Pet == null || Pet.isDead()) break;
				PetHeallingPotion(Pet, 45 + _Random.nextInt(15));
				pc.getInventory().removeItem(useItem, 1);
				break;
				
			case 43202: 
				if(Pet == null || Pet.isDead()) break;
				PetHeallingPotion(Pet, 65 + _Random.nextInt(15));
				pc.getInventory().removeItem(useItem, 1);
				break;
				
			/** 회상의 촛불 */
			case 43207: 
				if(Pet == null){
					pc.sendPackets(new S_SystemMessage("펫이 소환되어있는 상태에서만 사용가능합니다."), true); 
					break;
				}
				PetElixirPoint(Pet);
				pc.getInventory().removeItem(useItem, 1);
				break;
				
			/** 펫 엘릭서 부분 정리*/
			case 43208: 
				if(PetCheckMessege(pc, Pet)) break;
				if(PetElixirPoint(Pet, 1)){
					pc.getInventory().removeItem(useItem, 1);
				}else pc.sendPackets(new S_SystemMessage("이 아이템을 사용할수 없습니다."), true);
				break;
				
			case 43209: 
				if(PetCheckMessege(pc, Pet)) break;
				if(PetElixirPoint(Pet, 2)){
					pc.getInventory().removeItem(useItem, 1);
				}else pc.sendPackets(new S_SystemMessage("이 아이템을 사용할수 없습니다."), true);
				break;
				
			case 43210: 
				if(PetCheckMessege(pc, Pet)) break;
				if(PetElixirPoint(Pet, 3)){
					pc.getInventory().removeItem(useItem, 1);
				}else pc.sendPackets(new S_SystemMessage("이 아이템을 사용할수 없습니다."), true);
				break;	
				
			case 43206:
				/** 펫 이름변경권은 펫이 소환안되어있다면 
				 * 메세지 링크가 다름 */
				if(Pet != null){
					L1PetType PetName = PetTypeTable.getInstance().get(Pet.getNpcId());
					Pet.PetName(PetName.getName());
					pc.getInventory().removeItem(useItem, 1);
				/** 현재 상태에서 사용 불가 */
				}else pc.sendPackets(new S_ServerMessage(1413), true);
				break;
				
			/** 야수의 아이스 박스 */
			case 43205:
				Icebox(pc, useItem);
				break;
				
				
			/** 야수의 피 */
			case 43220:
				/** 펫이 놀이 아니거나 투지 부스터 상태가 아니라면 실행 맞다면 리턴해서 메세지 출력 */
				if(Pet != null){
					Pet.Fighting(100000);
					Pet.getFriendship();
					pc.getInventory().removeItem(useItem, 1);
				/** 현재 상태에서 사용 불가 */
				}else pc.sendPackets(new S_ServerMessage(1413), true);
				break;
				
			/** 광견의 피 */
			case 43221:
				/** 펫이 넘버라면 거나 이미 버프사용중이면 사용 안되게 하긔 */
				if(Pet != null && !Pet.SkillCheck(L1SkillId.DogBlood)){
					PetItemSkill(pc, Pet, L1SkillId.DogBlood, 1200);
					pc.getInventory().removeItem(useItem, 1);
				/** 현재 상태에서 사용 불가 */
				}else pc.sendPackets(new S_ServerMessage(1413), true);
				break;

			/** 핏불의 예거밤(투지)*/
			case 43222:
				if(Pet != null && !Pet.SkillCheck(L1SkillId.YeagerNight)){
					PetItemSkill(pc, Pet, L1SkillId.YeagerNight, 1800);
					pc.getInventory().removeItem(useItem, 1);
				/** 현재 상태에서 사용 불가 */
				}else pc.sendPackets(new S_ServerMessage(1413), true);
				break;
				
			/** 성장의 나뭇잎 */
			case 43223:
				if(Pet != null && !Pet.SkillCheck(L1SkillId.GrowthFoliage)){
					PetItemSkill(pc, Pet, L1SkillId.GrowthFoliage, 1800);
					pc.getInventory().removeItem(useItem, 1);
				/** 현재 상태에서 사용 불가 */
				}else pc.sendPackets(new S_ServerMessage(1413), true);
				break;
				/** 야수의 피 대용량 */
			case 43224:
				/** 펫이 놀이 아니거나 투지 부스터 상태가 아니라면 실행 맞다면 리턴해서 메세지 출력 */
				if(Pet != null){
					Pet.Fighting2(100000);
					Pet.getFriendship();
					pc.getInventory().removeItem(useItem, 1);
				/** 현재 상태에서 사용 불가 */
				}else pc.sendPackets(new S_SystemMessage("펫 소환 후 and 야수의피 복용후 이용해주세요."));
				break;
				
			/** 디폴더 상태라면 
			 * 펫 바구니 관련으로 적용 */
			default:
				PetBasket(pc, useItem);
				break;
		}
		if(!useItem.isIdentified()) {
			useItem.setIdentified(true);
			pc.sendPackets(new S_ItemName(useItem), true);
		}
		L1ItemDelay.onItemUse(pc, useItem);
	}
	
	/** 펫 스킬 관련 세팅 */
	public void PetItemSkill(L1PcInstance pc, L1PetInstance Pet, int PetItemSkill, int PetItemSkillTime) {
		/** 펫에게 아이템 시간 이랑 정리 */
		Pet.getSkillEffectTimerSet().setSkillEffect(PetItemSkill, PetItemSkillTime * 1000);
		if(PetItemSkill == L1SkillId.DogBlood){
			for (L1PcInstance Pc : L1World.getInstance().getRecognizePlayer(Pet)) {
				Pc.sendPackets(new S_PetWindow(S_PetWindow.DogBlood, Pet), true);
			}
		}else if(PetItemSkill == L1SkillId.GrowthFoliage){
			Pet.broadcastPacket(new S_SkillSound(Pet.getId(), 16551));
		}else{
			Pet.broadcastPacket(new S_SkillSound(Pet.getId(), 7382));
		}
		pc.sendPackets(new S_PetWindow(PetItemSkill, PetItemSkillTime), true);
	}
	
	Calendar CurrentDate = Calendar.getInstance();
	
	@SuppressWarnings("deprecation")
	private void Icebox(L1PcInstance pc, L1ItemInstance useItem) {
		/** 시간 검색 부분 */
		int ItemTemp = 1000 * 60 * 60 * 24;
		Timestamp LastUsed = useItem.getLastUsed();
		if (LastUsed == null || CurrentDate.getTimeInMillis() > LastUsed.getTime() + ItemTemp) {
			/** 아이템 획득 부분 */
			L1ItemInstance Item = pc.getInventory().storeItem(43220, 1);
			pc.sendPackets(new S_ServerMessage(403, Item.getName()+"(1)"), true);
			
			/** 아ㅣ템 정보 정리 부분 */
			useItem.setChargeCount(useItem.getChargeCount() - 1);
			pc.getInventory().updateItem(useItem,L1PcInventory.COL_CHARGE_COUNT);
			if (useItem.getChargeCount() == 0) pc.getInventory().removeItem(useItem);
			useItem.setLastUsed(new Timestamp(CurrentDate.getTimeInMillis()));
		} else {
			long i = (LastUsed.getTime() + ItemTemp) - CurrentDate.getTimeInMillis();
			Calendar cal = (Calendar)CurrentDate.clone();
			cal.setTimeInMillis(cal.getTimeInMillis() + i);
			StringBuffer sb = new StringBuffer();
			sb.append(i / 60000).append("분 후(");
			if (cal.getTime().getHours() < 10) {
				sb.append("0").append(cal.getTime().getHours()).append(":");
			} else {
				sb.append(cal.getTime().getHours()).append(":");
			}
			if (cal.getTime().getMinutes() < 10) {
				sb.append("0").append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			} else {
				sb.append(cal.getTime().getMinutes()).append(")에 사용할 수 있습니다.");
			}
			pc.sendPackets(new S_SystemMessage(sb.toString()), true);
		}
	}

	/** 펫 엘릭서 관련 세팅 */
	private L1PetInstance PetCheck(L1PcInstance pc) {
		try{
			/** 펫 사이즈가 0이상인지 체크후 같은펫인지 아닌지 체크 */
			L1PetInstance Pet = (L1PetInstance)pc.getPet();
			if (Pet != null) return Pet;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private boolean PetCheckMessege(L1PcInstance pc, L1PetInstance Pet) {
		try{
			if(Pet == null){
				pc.sendPackets(new S_SystemMessage("펫이 소환되어있는 상태에서만 사용가능합니다."), true); 
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/** 펫 관련 물약 세팅 */
	private void PetBasket(L1PcInstance Pc, L1ItemInstance UseItem) {
		try{		
			L1PetType Npc = PetTypeTable.getInstance().PetGetItem(UseItem.getItemId());
			if(Npc != null){
				int IetemObject = ObjectIdFactory.getInstance().nextId();
				new L1PetInstance(Npc, Pc, IetemObject);
				Pc.getInventory().removeItem(UseItem, 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 펫 관련 물약 세팅 */
	private void PetHeallingPotion(L1PetInstance Pet, int healHp) {
		try{			
			/** 펫에게 그래픽 날려줌 주고 Hp회복 */
			Broadcaster.broadcastPacket(Pet, new S_SkillSound(Pet.getId(), 17514), true);
			
			int HpMax = healHp;
			HpMax += (healHp * Pet.SkillsTable(L1PetInstance.먹성)) / 100;
			Pet.setCurrentHp(Pet.getCurrentHp() + HpMax);
			
			L1PcInstance Pc = (L1PcInstance) Pet.getMaster();
			Pc.sendPackets(new S_PetWindow(S_PetWindow.PatStatUpDate, Pet), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 펫 엘릭서 관련 세팅 */
	private void PetElixirPoint(L1PetInstance Pet) {
		try{
			/** 펫 관련 정보 보너스 스텟 관련도 체크 */
			int BonusPointTemp = 0;
			if(Pet.getLevel() > 50){
				BonusPointTemp = (5 + (Pet.getLevel() - 50));
			}else BonusPointTemp = (Pet.getLevel() / 10);
			Pet.setHunt(0);
			Pet.setSurvival(0);
			Pet.setSacred(0);
			Pet.getPetMr();
			Pet.setBonusPoint(BonusPointTemp);
			Pet.setCurrentHp(Pet.getCurrentHp());
			PetTable.UpDatePet(Pet); 
			
			L1PcInstance Pc = (L1PcInstance) Pet.getMaster();
			Pc.sendPackets(new S_PetWindow(S_PetWindow.PatStatUpDate, Pet), true);
			Pc.sendPackets(new S_PetWindow(S_PetWindow.PatBonusPoint, Pet), true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 펫 엘릭서 관련 세팅 */
	private boolean PetElixirPoint(L1PetInstance Pet, int ElixirType) {
		try{
			int Elixir = Pet.getElixir();
			int ElixirTemp = 0;
			if(Pet.getLevel() < 55){
				ElixirTemp = 1;
			}else ElixirTemp = ((Pet.getLevel() - 50) / 5) + 1; 
			/** 사용 가능 */
			if(ElixirTemp > Elixir){
				Pet.setElixir(Pet.getElixir() + 1);
				if(ElixirType == 1){
					Pet.setElixirHunt(Pet.getElixirHunt() + 1);
				}else if(ElixirType == 2){
					Pet.setElixirSurvival(Pet.getElixirSurvival() + 1);
				}else if(ElixirType == 3){
					Pet.setElixirSacred(Pet.getElixirSacred() + 1);
				}
				L1PcInstance Pc = (L1PcInstance) Pet.getMaster();
				L1ItemInstance item = Pc.getInventory().getItem(Pet.getItemObjId());
				/** 패킷 처리 부분 */
				if(ElixirType == 2){
					Pet.getPetMr();
					Pet.setCurrentHp(Pet.getCurrentHp());
					Pc.sendPackets(new S_PetWindow(S_PetWindow.PatStatUpDate, Pet), true);
				}
				Pc.sendPackets(new S_PetWindow(S_PetWindow.PatElixir, Pet), true);
				Pc.sendPackets(new S_PetWindow(item, Pet, false, false, false, true, false), true);
				PetTable.UpDatePet(Pet);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}