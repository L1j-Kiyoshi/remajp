package l1j.server.server.model.item.function;

import l1j.server.server.clientpackets.ClientBasePacket;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.datatables.PetsSkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1ItemDelay;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PetWindow;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.templates.L1Pet;

@SuppressWarnings("serial")
public class PetSummons extends L1ItemInstance {

	public PetSummons(L1Item item) {
		super(item);
	}

	@Override
	public void clickItem(L1Character cha, ClientBasePacket packet) {
		if (cha instanceof L1PcInstance) {
			L1PcInstance pc = (L1PcInstance) cha;
			UsePetSummons(pc, this.getId());
		}
	}

	/** 팻 서먼용 스래드 일단 체크 */
	private void UsePetSummons(L1PcInstance pc, int itemObjectId) {
		try{
			if (pc.isInvisble()) return;
			boolean isPet = true;
			/** 펫 사이즈가 0이상인지 체크후 같은펫인지 아닌지 체크 */
			L1PetInstance Pet = null;
			if(pc.getPet() != null){
				Pet = (L1PetInstance)pc.getPet();
				if (Pet.getItemObjId() == itemObjectId){ 
					isPet = false;
				}else{
					/** 펫 소환 들어왔는데 펫이 이미 가지고있다면 리턴 */
					pc.sendPackets(new S_SystemMessage("이미 다른펫이 소환되어있습니다. "), true);
					return;
				}
			}
			
			L1ItemInstance useItem = pc.getInventory().getItem(itemObjectId);
			if (isPet) {
				/** 여기서 펫을 사용할수 없습니다 */
				if (!pc.getMap().isTakePets()) {
					pc.sendPackets(new S_ServerMessage(563), true);
					return;
				}
				
				L1Pet L1pet = PetTable.getTemplate(itemObjectId);
				if(L1pet == null) return;
				L1Npc NpcTemp = NpcTable.getInstance().getTemplate(L1pet.getNpcId());
				
				if(L1pet.isPetDead()){
					/** 펫 부활에 필요한 결정체 1000개 있는가*/
					/** 결정체가 부족합니다.*/
					if(!pc.getInventory().consumeItem(41246, 1000)){
						pc.sendPackets(new S_SystemMessage("결정체가 부족하여 부활할수없습니다. "), true);
						return;
					}
					
					Pet = new L1PetInstance(NpcTemp, pc, L1pet);
					pc.sendPackets(new S_SkillSound(Pet.getId(), 5935), true);
					Broadcaster.broadcastPacket(pc, new S_SkillSound(Pet.getId(), 5935), true);
					pc.sendPackets(new S_ServerMessage(5274, Pet.getName()), true);
					
					if(Pet.SkillCheck(L1SkillId.DogBlood)){
						for (L1PcInstance Use : L1World.getInstance().getRecognizePlayer(Pet)) {
							Use.sendPackets(new S_PetWindow(S_PetWindow.DogBlood, Pet), true);
						}
					}
					
					/** 스킬 정보 불러옴 */
					pc.sendPackets(new S_PetWindow(Pet.ArrayPetSkills()), true);
					
					/** 펫관련 공속 처리 */
					pc.sendPackets(new S_PetWindow(Pet, false), true);
					
					/** 일단 부활 처리 부터 하도록 하고 그다음 아래에서 소환 처리 함 */
					pc.sendPackets(new S_PetWindow(useItem, Pet, true, true, false, false, false), true);
					
					/** 펫 부활대면 기본적으로 펫 정보 업데이트  */
					PetTable.UpDatePet(Pet);
				/** 펫이 있다면 제거패킷 처리 */
				}else{ 
					/** 펫 소환하기 전에 결정체가 있는지 부터 체크 결정체가 부족합니다.*/
					if(!pc.getInventory().consumeItem(41246, 50)){
						pc.sendPackets(new S_SystemMessage("결정체가 부족하여 소환할수 없습니다. "), true);
						return;
					}
					
					/** 만약 망각 상태라면 모든 정보 초기화 해제 하도록 세팅 */
					Pet = new L1PetInstance(NpcTemp, pc, L1pet);
					if(L1pet.isProduct()){
						Pet.setProduct(false);
						/** 메세지 링크 */
						pc.sendPackets(new S_ServerMessage(5314), true);
						/** 기본 스킬 배워지도록 */
						Pet.addPetSkills(Pet.getPetType().getSkillOneStep());
						pc.sendPackets(new S_PetWindow(Pet.getPetType().getSkillOneStep()), true);
						/** 디비 저장t 시스템  */
						PetsSkillsTable.SaveSkills(Pet, false);
						
						/** 패킷 처리 1번 온오프 2번 다이 3번 이름 4번 엘릭서 */
						pc.sendPackets(new S_PetWindow(useItem, Pet, false, false, false, false, true), true);
						
						/** 펫 부활대면 기본적으로 펫 정보 업데이트  */
						PetTable.UpDatePet(Pet);
					}else pc.sendPackets(new S_PetWindow(Pet.ArrayPetSkills()), true);
					
					if(Pet.SkillCheck(L1SkillId.DogBlood)){
						for (L1PcInstance Use : L1World.getInstance().getRecognizePlayer(Pet)) {
							Use.sendPackets(new S_PetWindow(S_PetWindow.DogBlood, Pet), true);
						}
					}
					
					/** 펫관련 공속 처리 */
					pc.sendPackets(new S_PetWindow(Pet, false), true);
					
					pc.sendPackets(new S_SkillSound(Pet.getId(), 5935), true);
					Broadcaster.broadcastPacket(pc, new S_SkillSound(Pet.getId(), 5935), true);
					pc.sendPackets(new S_ServerMessage(5274, Pet.getName()), true);
					
					/** 패킷 처리 1번 온오프 2번 다이 3번 이름 4번 엘릭서 */
					pc.sendPackets(new S_PetWindow(useItem, Pet, true, false, false, false, false), true);
				}
			} else {
				/** 전투중에는 해제 할수없습니다. */
				if(Pet.getTarget() != null){
					pc.sendPackets(new S_SystemMessage("전투중에는 해제 할수없습니다. "), true);
					return;
				}
				
				/** 현재 펫이 다이 상태라면 해제안됨 */
				if(Pet.isDead()){
					pc.sendPackets(new S_SystemMessage("죽은상태에서는 펫 해제가 불가능 합니다. "), true);
					return;
				}
				
				Pet.deletePet();
				/** 패킷 처리 1번 온오프 2번 다이 3번 이름 4번 엘릭서 */
				pc.sendPackets(new S_PetWindow(useItem, Pet, true, false, false, false, false), true);			
			}
			L1ItemDelay.onItemUse(pc, useItem);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
