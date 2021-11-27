/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.   See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server.server.command.executor;

//import java.util.logging.Logger;

import l1j.server.server.datatables.SkillsTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_CreateItem;
import l1j.server.server.serverpackets.S_NewAddSkill;
import l1j.server.server.serverpackets.S_NewUI;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Skills;

public class L1AddSkill implements L1CommandExecutor {
	// private static Logger _log =
	// Logger.getLogger(L1AddSkill.class.getName());

	private L1AddSkill() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1AddSkill();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {
			int cnt = 0; // 루프 카운터
			String skill_name = ""; // 스킬명
			int skill_id = 0; // 스킬 ID

			int object_id = pc.getId(); // 캐릭터의 objectid를 취득
			pc.sendPackets(new S_SkillSound(object_id, '\343')); // 마법 습득의 효과음을
																	// 울린다
			Broadcaster
					.broadcastPacket(pc, new S_SkillSound(object_id, '\343'));

			if (pc.isCrown()) {
				L1Skills l1skills = null;
				for (cnt = 1; cnt <= 16; cnt++) // LV1~2 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 113; cnt <= 123; cnt++) // 프리 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 241; cnt <= 241; cnt++) // 프리 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				
			} else if (pc.isKnight()) {
				L1Skills l1skills = null;
				for (cnt = 1; cnt <= 8; cnt++) // LV1 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 87; cnt <= 92; cnt++) // 나이트 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 242; cnt <= 242; cnt++) // 프리 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
			} else if (pc.isElf()) {
				L1Skills l1skills = null;
				for (cnt = 1; cnt <= 48; cnt++) // LV1~6 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 129; cnt <= 179; cnt++) // 에르프 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
			} else if (pc.isWizard()) {
				L1Skills l1skills = null;
				for (cnt = 1; cnt <= 80; cnt++) // LV1~10 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
			} else if (pc.isDarkelf()) {
				L1Skills l1skills = null;
				for (cnt = 1; cnt <= 16; cnt++) // LV1~2 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 97; cnt <= 112; cnt++) // DE마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 244; cnt <= 244; cnt++) // 프리 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
			} else if (pc.isDragonknight()) {
				L1Skills l1skills = null;
				for (cnt = 177; cnt <= 200; cnt++) // 용기사스킬
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 245; cnt <= 245; cnt++) // 프리 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
			} else if (pc.isIllusionist()) {
				L1Skills l1skills = null;
				for (cnt = 201; cnt <= 224; cnt++) // 환술사스킬
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 246; cnt <= 246; cnt++) // 프리 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
			} else if (pc.isWarrior()) {
				L1Skills l1skills = null;
				for (cnt = 1; cnt <= 8; cnt++) // LV1 마법
				{
					l1skills = SkillsTable.getInstance(). getTemplate(
							cnt); // 스킬 정보를 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance(). spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 225; cnt <= 230; cnt++) // 전사 액티브 스킬
				{
					l1skills = SkillsTable.getInstance(). getTemplate(cnt); // 스킬 정보를 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance(). spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 247; cnt <= 247; cnt++) // 프리 마법
				{
					l1skills = SkillsTable.getInstance().getTemplate(cnt); // 스킬
																			// 정보를
																			// 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance().spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 300; cnt <= 306; cnt++) // 전사 패시브 스킬
				{
					l1skills = SkillsTable.getInstance(). getTemplate(cnt); // 스킬 정보를 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance(). spellMastery(object_id, skill_id, skill_name, 0, 0); // DB에 등록
				}
				S_NewUI sn1 = new S_NewUI(true, true, true, true, true, true,
						true);
				pc.sendPackets(sn1);
			} else if (pc.isFencer()) {
				L1Skills l1skills = null;
				for (cnt = 1; cnt <= 16; cnt++) // LV1 마법
				{
					l1skills = SkillsTable.getInstance(). getTemplate(
							cnt); // 스킬 정보를 취득
					skill_name = l1skills.getName();
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance(). spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 235; cnt <= 239; cnt++) // 검사 액티브 스킬
				{
					l1skills = SkillsTable.getInstance(). getTemplate(cnt); // 스킬 정보를 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance(). spellMastery(object_id, skill_id,
							skill_name, 0, 0); // DB에 등록
					pc.sendPackets(new S_NewAddSkill(S_NewAddSkill.AddskillNew,pc,cnt), true);
				}
				for (cnt = 550; cnt <= 562; cnt++) // 검사 패시브 스킬
				{
					l1skills = SkillsTable.getInstance(). getTemplate(cnt); // 스킬 정보를 취득
					skill_name = l1skills.getName();
					if(skill_name.equalsIgnoreCase("none"))
						continue;
					skill_id = l1skills.getSkillId();
					SkillsTable.getInstance(). spellMastery(object_id, skill_id, skill_name, 0, 0); // DB에 등록
					if (cnt == 562) {
						pc.sendPackets(new S_CreateItem(146, 36, 0));
					} else {
					pc.sendPackets(new S_CreateItem(146, l1skills.getSkillId() - 527, 0));
					}
				}
			}
			pc.sendPackets(new S_SystemMessage("모든 스킬을 배웠습니다."));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage(cmdName + " 커멘드 에러"));
		}
	}
}
