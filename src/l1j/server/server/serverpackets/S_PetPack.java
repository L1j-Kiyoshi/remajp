package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.skill.L1SkillId;

public class S_PetPack extends ServerBasePacket {

	private static final String S_PET_PACK = "[S] S_PetPack";

	private byte[] _byte = null;

	public S_PetPack(L1PetInstance pet, L1PcInstance pc) {
		buildPacket(pet, pc);
	}

	private void buildPacket(L1PetInstance pet, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x77);
		writeC(0x00);

		/** 좌표패킷 */
		writeC(0x08);
		writeBit(pet.getX(), pet.getY());

		/** 오브젝트 */
		writeC(0x10);
		writeBit(pet.getId());

		/** 그래픽 넘버 */
		writeC(0x18);
		writeBit(pet.getGfxId().getTempCharGfx());
		
		/** 무기 착용 타입 */
		writeC(0x20);
		writeBit(pet.getActionStatus());
		
		/** 보는 방향 */
		writeC(0x28);
		writeBit(pet.getMoveState().getHeading());

		/** 라이트 */
		writeC(0x30);
		writeBit(pet.getLight().getOwnLightSize());

		/** 패킷 확인요망 */
		writeC(0x38);
		writeBit(1); 

		/** 라우풀 */
		writeC(0x40);
		writeBit(pet.getLawful());

		/** 케릭터 이름 */
		writeC(0x4a);
		String name = null;
		if(pet.isDogFight()) {
			name = pet.getNameId();
		}else name = pet.getName();
		if (name.length() > 0) {
			writeC(name.getBytes().length);
			writeByte(name.getBytes());
		}else writeC(0x00);

		writeC(0x52);
		String title = pet.getTitle();
		if (title != null && title.length() > 0) {
			writeC(title.getBytes().length);
			writeByte(title.getBytes());
		}else writeC(0x00);

		/** 헤이스트 상태 */
		writeC(0x58);
		writeBit(pet.getMoveState().getMoveSpeed());

		/** 용기 상태 */
		writeC(0x60);
		writeBit(pet.getMoveState().getBraveSpeed());

		/** 체크필요 */
		writeC(0x68);
		writeC(0x00);

		/** 관란자 */
		writeC(0x70);
		writeBit(0);

		/** 굳은 상태 */
		writeC(0x78);
		writeBit(pet.getParalysis() != null ? 1L : 0L);

		writeC(0x80);
		writeC(0x01);
		writeBit(0);

		/** 인비지 */
		writeC(0x88);
		writeC(0x01);
		writeBit(pet.isInvisble() ? 1L : 0L);

		/** 독 상태 */
		writeC(0x90);
		writeC(0x01);
		writeBit(pet.getPoison() != null ? 1L : 0L);

		writeC(0x98);
		writeC(0x01);
		writeBit(0);

		/** 클렌 네임 */
		writeC(0xa2);
		writeC(0x01);
		writeBit(0);

		/** 인형 머리위 이름 */
		writeC(0xaa);
		writeC(0x01);
		if (pet.getMaster() == null || pet.getMaster().getName().length() == 0) {
			writeC(0);
		} else {
			String clanName = pet.getMaster().getName();
			writeC(clanName.getBytes().length);
			writeByte(clanName.getBytes());
		}
		
		writeC(0xb0);
		writeC(0x01);
		writeBit(0);

		writeC(0xb8);
		writeC(0x01);
		writeBit(-1);

		writeC(0xc0);
		writeC(0x01);
		writeBit(0);

		writeC(0xd0);
		writeC(0x01);
		writeBit(-1);

		writeC(0xd8);
		writeC(0x01);
		writeBit(0);

		writeC(0xe0);
		writeC(0x01);
		int stype = 0;
		if (pet.getLevel() >= 15) stype++;
		if (pet.getLevel() >= 30) stype++;
		if (pet.getLevel() >= 45) stype++;
		if (pet.getLevel() >= 50) stype++;
		if (pet.getLevel() >= 52) stype++;
		if (pet.getLevel() >= 55) stype++;
		if (pet.getLevel() >= 60) stype++;
		if (pet.getLevel() >= 65) stype++;
		if (pet.getLevel() >= 70) stype++;
		if (pet.getLevel() >= 75) stype++;
		if (pet.getLevel() >= 80) stype++;
		if (pet.getLevel() >= 82) stype++;
		if (pet.getLevel() >= 85) stype++;
		if (pet.getLevel() >= 90) stype++;
		if (stype > 14) stype = 14;
		writeC(stype);

		writeC(0xf0);
		writeC(0x01);
		writeBit(-1);

		writeC(0x80);
		writeC(0x02);
		writeC(0x00);
		
		writeC(0x98);
		writeC(0x02);
		writeC(0x00);
		
		/** 펫 인포 넘버 */
		writeC(0xb8);
		writeC(0x02);
		writeBit(pet.getPetInfo());
		
		/** 각 속도 관련 패킷 같은데 */
		writeC(0xc2);
		writeC(0x02);
		
		/** 사이즈 */
		writeC(0x14);
		
		/** 공속 패킷 처리 */
		writeC(0x09);
		writeF(pet.SpeedTime(0));
		
		/** 이속 패킷 처리 */
		writeC(0x11);
		writeF(pet.SpeedTime(1));
		
		/** 아이콘 상태패킷 100 광견 */
		writeC(0x18);
		if(pet.SkillCheck(L1SkillId.DogBlood)){
			writeBit(0x64);
		}else writeBit(0x0c);
		
		writeH(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}

		return _byte;
	}

	@Override
	public String getType() {
		return S_PET_PACK;
	}
}