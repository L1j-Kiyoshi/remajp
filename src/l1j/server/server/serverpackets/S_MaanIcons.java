package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.skill.L1SkillId;

public class S_MaanIcons extends ServerBasePacket {

	public S_MaanIcons(int skillId, boolean on, long time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(110);
		writeC(0x08);
		writeBit(on ? 1 : 3);
		writeC(0x10);
		writeBit(skillId); // 스킬아이디

		if (on) {
			writeC(0x18);
			writeBit(time); // 시간
			writeC(0x20);
			writeBit(8); // duration_show_type
			writeC(0x28);
			int icon_id = 3624;
			if (skillId == L1SkillId.HALPAS_FAITH_STANDBY)
				icon_id = 9943;
			if (skillId == L1SkillId.HALPAS_FAITH_DELAY)
				icon_id = 9944;
			if (skillId == L1SkillId.HALPAS_FAITH_PVP_REDUC)
				icon_id = 9943;
			if (skillId == L1SkillId.miso1)
				icon_id = 8134;
			if (skillId == L1SkillId.miso2)
				icon_id = 8133;
			if (skillId == L1SkillId.miso3)
				icon_id = 8132;
			
			writeBit(icon_id); // 시작시 표현될 인벤이미지번호
			writeC(0x30);
			writeBit(icon_id); // 종료시 표현될 인벤이미지번호
			writeC(0x38);
			
			int sort = 8;
			if (skillId == L1SkillId.HALPAS_FAITH_DELAY)
				sort = 5;
			if (skillId == L1SkillId.HALPAS_FAITH_PVP_REDUC)
				sort = 8;
			if (skillId == L1SkillId.miso1)
				sort = 12;
			if (skillId == L1SkillId.miso2)
				sort = 12;
			if (skillId == L1SkillId.miso3)
				sort = 12;
			writeBit(sort); // icon_priority (우선순위)

			int skill_string_no = 0;
			if (skillId == L1SkillId.ANTA_MAAN)
				skill_string_no = 1597;
			if (skillId == L1SkillId.FAFU_MAAN)
				skill_string_no = 1610;
			if (skillId == L1SkillId.VALA_MAAN)
				skill_string_no = 1612;
			if (skillId == L1SkillId.LIND_MAAN)
				skill_string_no = 1611;
			if (skillId == L1SkillId.BIRTH_MAAN)
				skill_string_no = 1613;
			if (skillId == L1SkillId.SHAPE_MAAN)
				skill_string_no = 1614;
			if (skillId == L1SkillId.LIFE_MAAN)
				skill_string_no = 1615;
			if (skillId == L1SkillId.HALPAS_MAAN)
				skill_string_no = 7453;
			if (skillId == L1SkillId.NEVER_MAAN)
				skill_string_no = 7454;
			if (skillId == L1SkillId.HALPAS_FAITH_STANDBY)
				skill_string_no = 7465;
			if (skillId == L1SkillId.HALPAS_FAITH_DELAY)
				skill_string_no = 7437;
			if (skillId == L1SkillId.HALPAS_FAITH_PVP_REDUC)
				skill_string_no = 7436;
			if (skillId == L1SkillId.miso1)
				skill_string_no = 4997;
			if (skillId == L1SkillId.miso2)
				skill_string_no = 4996;
			if (skillId == L1SkillId.miso3)
				skill_string_no = 4995;
			
			writeC(0x40);
			writeBit(skill_string_no); // 스트링 번호(아이콘 안에 내용)

			int skill_start_string_no = 0;
			if (skillId == L1SkillId.ANTA_MAAN)
				skill_start_string_no = 1597;
			if (skillId == L1SkillId.FAFU_MAAN)
				skill_start_string_no = 1610;
			if (skillId == L1SkillId.VALA_MAAN)
				skill_start_string_no = 1612;
			if (skillId == L1SkillId.LIND_MAAN)
				skill_start_string_no = 1611;
			if (skillId == L1SkillId.BIRTH_MAAN)
				skill_start_string_no = 1613;
			if (skillId == L1SkillId.SHAPE_MAAN)
				skill_start_string_no = 1614;
			if (skillId == L1SkillId.LIFE_MAAN)
				skill_start_string_no = 1615;
			if (skillId == L1SkillId.HALPAS_MAAN)
				skill_start_string_no = 7453;
			if (skillId == L1SkillId.NEVER_MAAN)
				skill_start_string_no = 7454;
			if (skillId == L1SkillId.HALPAS_FAITH_STANDBY)
				skill_start_string_no = 7465;
			if (skillId == L1SkillId.HALPAS_FAITH_DELAY)
				skill_start_string_no = 7437;
			if (skillId == L1SkillId.HALPAS_FAITH_PVP_REDUC)
				skill_start_string_no = 7436;
			if (skillId == L1SkillId.miso1)
				skill_start_string_no = 4997;
			if (skillId == L1SkillId.miso2)
				skill_start_string_no = 4996;
			if (skillId == L1SkillId.miso3)
				skill_start_string_no = 4995;
			writeC(0x48);
			writeBit(skill_start_string_no); // 버프시작시 채팅메세지
			writeC(0x50);
			writeBit(0); // 버프종료시 채팅메세지
			writeC(0x58);
			writeBit(1); // is_good (버프 / 디버프 구분)
			writeC(0x60);
			writeBit(0x00); // overlap_buff_icon
			writeC(0x68);
			writeBit(0x00); // main_tooltip_str_id
			writeC(0x70);
			writeBit(0x00); // buff_icon_priority
		} else {
			int skill_end_iven_icon = 0;
			writeC(0x30);
			writeBit(skill_end_iven_icon); // 종료시 표현될 인벤이미지번호

			int skill_end_string_no = 0;
			writeC(0x50);
			writeBit(skill_end_string_no); // 버프종료시 채팅메세지
		}

		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
