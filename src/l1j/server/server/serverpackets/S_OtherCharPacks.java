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

package l1j.server.server.serverpackets;

import l1j.server.GameSystem.Robot.L1RobotInstance;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

//Referenced classes of package l1j.server.server.serverpackets:
//ServerBasePacket, S_OtherCharPacks

public class S_OtherCharPacks extends ServerBasePacket {

	private static final String S_OTHER_CHAR_PACKS = "[S] S_OtherCharPacks";
	private static final int STATUS_POISON = 1;
	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	// private static final int BLOOD_LUST = 100;
	private static final int STATUS_GHOST = 128;

	private byte[] _byte = null;

	public S_OtherCharPacks(L1PcInstance pc, L1PcInstance user) {
		build(pc, user, false);
	}

	public S_OtherCharPacks(L1PcInstance pc, L1PcInstance user, boolean invis) {
		build(pc, user, invis);
	}

	public void build(L1PcInstance pc, L1PcInstance user, boolean invis) {
		int status = STATUS_PC;

		if (pc.getPoison() != null) {
			if (pc.getPoison().getEffectId() == 1) {
				status |= STATUS_POISON;
			}
		}

		if (pc.isInvisble()) {
			status |= STATUS_INVISIBLE;
		}
		if (pc.isBrave() || pc.getSkillEffectTimerSet().hasSkillEffect(L1SkillId.FIRE_BLESS)) {
			status |= STATUS_BRAVE;
		}

		if (pc.isElfBrave()) {
			status |= STATUS_BRAVE;
			status |= STATUS_ELFBRAVE;
		}

		if (pc.isUgdraFruit()) {
			status |= STATUS_FASTMOVABLE;
		}

		if (pc.isBloodLust()) {
			status |= STATUS_BRAVE;
		}
		if (pc.isSandstorm()) {
			status |= STATUS_BRAVE;
		}
		if (pc.isFocuswave()) {
			status |= STATUS_BRAVE;
		}
		if (pc.isHurricane()) {
			status |= STATUS_BRAVE;
		}
		if (pc.isdarkhos()) {
			status |= STATUS_ELFBRAVE;
		}

		if (pc.isFastMovable() || pc.getMapId() == 5143) {
			status |= STATUS_FASTMOVABLE;
		}

		if (pc.isGhost()) {
			status |= STATUS_GHOST;
		}

		if (pc.isParalyzed()) {
			status |= STATUS_FREEZE;
		}
		// int addbyte = 0;
		// int addbyte1 = 1;

		writeC(Opcodes.S_PUT_OBJECT);
		writeH(pc.getX());
		writeH(pc.getY());
		writeD(pc.getId());

		if (pc.isDead()) {
			writeH(pc.getTempCharGfxAtDead());
		} else {
			writeH(pc.getGfxId().getTempCharGfx());
		}

		if (pc.isDead()) {
			writeC(pc.getActionStatus());
		} else if (pc.isPrivateShop()) {
			writeC(70);
		} else {
			int polyId = pc.getGfxId().getTempCharGfx();
			int weaponId = pc.getCurrentWeapon();

			if (polyId == 3784 || polyId == 6137 || polyId == 6142 || polyId == 6147 || polyId == 6152 || polyId == 6157 || polyId == 9205 || polyId == 9206) {
				if (pc instanceof L1RobotInstance) {
					if (weaponId == 24)
						weaponId = 83;
				} else if (weaponId == 24 && pc.getWeapon() != null && pc.getWeapon().getItem().getType() == 18)
					weaponId = 83;
			} else if (polyId == 13152 || polyId == 13153 || polyId == 12702 || polyId == 12681 || polyId == 8812 || polyId == 8817 || polyId == 6267
					|| polyId == 6270 || polyId == 6273 || polyId == 6276) {
				if (weaponId == 24 && pc.getWeapon() != null && pc.getWeapon().getItem().getType() == 18)
					weaponId = 50;
			}
			writeC(weaponId);
		}
		writeC(pc.getMoveState().getHeading());
		// writeC(0); // makes char invis (0x01), cannot move. spells display
		writeC(pc.getLight().getChaLightSize());
		// System.out.println(pc.getMoveState().getMoveSpeed());
		writeC(pc.getMoveState().getMoveSpeed());
		writeD(1);// pc.getExp());

		// writeC(0x00);
		writeH(pc.getLawful());

		if (pc.getHuntCount() == 1) {
			writeS(pc.getName());
		} else if (pc.getHuntCount() == 2) {
			writeS(pc.getName());
		} else if (pc.getHuntCount() == 3) {
			writeS(pc.getName());
		} else {
			writeS(pc.getName());
		}
		if (pc.getLevel() <= 0) {
			writeS("");// 신규호칭
		} else {
			writeS(pc.getTitle());
		}

		writeC(status);
		writeD(pc.getClanid());
		writeS(pc.getClanname()); // 크란명
		writeS(null); // 펫호팅?
		// writeC(0); // ?
		writeC(0);
		// writeS(null); // 펫호팅?
		// writeC(0); // ?
		/*
		 * if(pc.getClanid() == 0 || pc.getClan()== null){//이시발놈! writeC(0xb0); }else
		 * writeC(pc.getClanRank()*0x10);
		 */// ??
		/*
		 * if(pc.is_isInParty()) // 파티중 { writeC(100 * pc.get_currentHp() /
		 * pc.get_maxHp()); } else { writeC(0xFF); }
		 */
		if (pc.isInParty() && pc.getParty().isMember(user)) // 파티중
		{
			writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
		} else {
			writeC(0xFF);
			// writeC(0xFF);
		}
		if (pc.isThirdSpeed())
			writeC(0x08);
		else
			writeC(0); // 타르쿡크 거리(대로)
		writeC(0); // PC = 0, Mon = Lv
		if (pc.isPrivateShop())
			writeByte(pc.getShopChat());
		writeC(0); //

		writeC(0xff);
		writeC(0xff);

		/*
		 * int hpRatio = 100; int mpRatio = 100; if (0 < pc.getMaxHp()) { hpRatio = 100
		 * * pc.getCurrentHp() / pc.getMaxHp(); }
		 * 
		 * if (0 < pc.getMaxMp()) { mpRatio = 100 * pc.getCurrentMp() / pc.getMaxMp(); }
		 * writeC(hpRatio); writeC(mpRatio);
		 */
		/*
		 * if (pc.isInParty() && pc.getParty().isMember(user)) {
		 * 
		 * }else{ writeC(0xFF); writeC(0xFF); }
		 */

		writeC(0);
		int stype = 0;
		if (pc.getLevel() >= 15)
			stype++;
		if (pc.getLevel() >= 30)
			stype++;
		if (pc.getLevel() >= 45)
			stype++;
		if (pc.getLevel() >= 50)
			stype++;
		if (pc.getLevel() >= 52)
			stype++;
		if (pc.getLevel() >= 55)
			stype++;
		if (pc.getLevel() >= 60)
			stype++;
		if (pc.getLevel() >= 65)
			stype++;
		if (pc.getLevel() >= 70)
			stype++;
		if (pc.getLevel() >= 75)
			stype++;
		if (pc.getLevel() >= 80)
			stype++;
		if (pc.getLevel() >= 82)
			stype++;
		if (pc.getLevel() >= 85)
			stype++;
		if (stype > 13)
			stype = 13;

		writeC(stype);
		writeC(0xFF);
		writeH(0x00);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_OTHER_CHAR_PACKS;
	}

}