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
package l1j.server.server.datatables;

import static l1j.server.server.model.skill.L1SkillId.*;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.utils.SQLUtil;

public class CharBuffTable {
	private CharBuffTable() {
	}

	private static Logger _log = Logger.getLogger(CharBuffTable.class.getName());

	// ������ ���� ��� ����Ʈ
	private static final int[] buffSkill = {
			9278,
			9279,
			999,
			2,
			67, // ����,����Ʈ, ������ ü����
			3,
			99,
			151,
			159,
			168, // ����(shield), �׸��� �Ƹ�, ���� ��Ų, ���� ȣ��, ���̾� ��Ų
			43,
			54,
			1000,
			1001,
			STATUS_ELFBRAVE, // ���� �ľ�, �׷����� ���� �ľ�, ġ��ħ �̺� �Ϻ�, �׸� �Ϻ�, ���� ����
			52,
			101,
			150, // ȣ-�� ��ũ, ���� �� �����̼�, �����ũ
			26,
			42,
			109,
			110, // PE:DEX, PE:STR, �巹������Ƽ, �巹����ũ��Ÿ��Ƽ
			114,
			115,
			116,
			117, // �׷�������, ���̴׿���, ġ��ħ �̺� �ƿ��
			148,
			155,
			177,
			178,
			179,
			163, // ���̾ƿ���, ���̾� ȣ��, �ٴ׿���
			149,
			156,
			166, // ������ ��Ʈ, ���乫����, ���� ��Ʈ
			1002,
			STATUS_CHAT_PROHIBITED, // ���� �Ϻ�, ä�� ����
			
			/** �������� ��Ŷ ���� */
			BLESS_WEAPON,
			DECREASE_WEIGHT, DECAY_POTION, SILENCE, VENOM_RESIST, WEAKNESS,
			DISEASE, DRESS_EVASION, BERSERKERS, NATURES_TOUCH, WIND_SHACKLE,
			ERASE_MAGIC, ADDITIONAL_FIRE, ELEMENTAL_FALL_DOWN, ELEMENTAL_FIRE,
			STRIKER_GALE, SOUL_OF_FLAME, POLLUTE_WATER, STATUS_TIKAL_BOSSDIE,
			CONCENTRATION, INSIGHT, PANIC, MORTAL_BODY, HORROR_OF_DEATH, FEAR,
			PATIENCE, IMMUNE_TO_HARM, ����Ʈ, FORCE_STUN, 
			HALPHAS, POTENTIAL,JUDGEMENT,

			/** ���� ���� ���� */
			RESIST_MAGIC, CLEAR_MIND, cyclone,
			RESIST_ELEMENTAL,
			ELEMENTAL_PROTECTION,
			L1SkillId.BLOOD_LUST,

			/** ���� ���� */
			PRIME, �׷��̽��ƹ�Ÿ,
			
			/** �÷�Ǯ ��Ű�� ������ �� õ���� ���� */
			EXP_POTION_cash,
			EXP_POTION,
			EXP_POTION2,
			EXP_POTION3,
			STATUS_BLUE_POTION2,
			STATUS_FRUIT,
			STATUS_CASHSCROLL,
			STATUS_CASHSCROLL2,
			STATUS_CASHSCROLL3,
			STATUS_Ŀ������,
			STATUS_Ŀ������,
			STATUS_Ŀ������,
			STATUS_�ÿ��Ѿ�������,
			STATUS_COMA_3,
			STATUS_COMA_5,

			/** ����� �丮 */
			SPECIAL_COOKING,
			// DRAGON_EME_1, DRAGON_EME_2,

			/** ������ ���� ������ ���� ������ */
			STATUS_BLUE_POTION3,
			FEATHER_BUFF_A,
			FEATHER_BUFF_B,
			FEATHER_BUFF_C,
			FEATHER_BUFF_D,
			STATUS_UNDERWATER_BREATH,
			
			/** �丮 1�ܰ� ȿ�� ��ο� */
			COOKING_1_0_N,
			COOKING_1_0_S,
			COOKING_1_1_N,
			COOKING_1_1_S, // �丮
			COOKING_1_2_N, COOKING_1_2_S, COOKING_1_3_N, COOKING_1_3_S,
			COOKING_1_4_N, COOKING_1_4_S, COOKING_1_5_N, COOKING_1_5_S,
			COOKING_1_6_N, COOKING_1_6_S,

			/** �丮 2�ܰ� ȿ�� ��ο� */
			COOKING_1_8_N, COOKING_1_8_S, COOKING_1_9_N, COOKING_1_9_S,
			COOKING_1_10_N, COOKING_1_10_S, COOKING_1_11_N, COOKING_1_11_S,
			COOKING_1_12_N, COOKING_1_12_S, COOKING_1_13_N, COOKING_1_13_S,
			COOKING_1_14_N, COOKING_1_14_S,

			/** �丮 3�ܰ� ȿ�� ��ο� */
			COOKING_1_16_N, COOKING_1_16_S, COOKING_1_17_N, COOKING_1_17_S,
			COOKING_1_18_N, COOKING_1_18_S, COOKING_1_19_N, COOKING_1_19_S,
			COOKING_1_20_N, COOKING_1_20_S, COOKING_1_21_N, COOKING_1_21_S,
			COOKING_1_22_N, COOKING_1_22_S, COOKING_NEW_�ѿ�, COOKING_NEW_����,
			COOKING_NEW_ĥ����, COOKING_NEW_�߰���, ���̸����Ѷ��, ���̽ÿ�������,
			
			COOKING_NEW_Ž�ѿ�, COOKING_NEW_Ž����,
			COOKING_NEW_Žĥ����, COOKING_NEW_Ž�߰���, 

			õ��������,
			���������ʽ�,
			POLY_SAFE,
			�巡���Ǽ������,
			STATUS_WISDOM_POTION
			/** ���� ��ο� */
			// DRAGONBLOOD_A, DRAGONBLOOD_P
			// ,��ȭ����_Ȱ��, ��ȭ����_����, ��ȭ����_���, ��ȭ����_����, ��ȭ����_����, ��ȭ����_Ȧ��
			, FAFU_MAAN, ANTA_MAAN, LIND_MAAN, VALA_MAAN, LIFE_MAAN,
			BIRTH_MAAN, SHAPE_MAAN, HALPAS_MAAN, NEVER_MAAN, miso1, miso2, miso3
			/**������ ü�̼�Ÿ��*/
			,RevengeTime
	        };

	
	/**
	 * ĳ���Ϳ� �������� ���� ��� ����
	 */
	private static void StoreBuff(int objId, int skillId, int time, int polyId) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("INSERT INTO character_buff SET char_obj_id=?, skill_id=?, remaining_time=?, poly_id=?");
			
			pstm.setInt(1, objId);
			pstm.setInt(2, skillId);
			pstm.setInt(3, time);
			pstm.setInt(4, polyId);
			pstm.executeUpdate();
			
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	/**
	 *  DB�� ����� ĳ���� ���� ��� ���� 
	 */
	public static void DeleteBuff(L1PcInstance pc) {
		java.sql.Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("DELETE FROM character_buff WHERE char_obj_id=?");
			pstm.setInt(1, pc.getId());
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
	}

	/**
	 * DB�� ������ ĳ���Ϳ� �������� ���� ��� Ȯ��
	 */
	public static void SaveBuff(L1PcInstance pc) {
		for (int skillId : buffSkill) {
			int timeSec = pc.getSkillEffectTimerSet().getSkillEffectTimeSec(skillId);
			
			if (0 < timeSec) {
				int polyId = 0;
				if (skillId == SHAPE_CHANGE) {
					polyId = pc.getGfxId().getTempCharGfx();
					if(pc.getSkillEffectTimerSet().GetDominationSkill()) polyId *= -1;
				}
				
				// DB�� ����
				StoreBuff(pc.getId(), skillId, timeSec, polyId);
			}
		}
	}

}