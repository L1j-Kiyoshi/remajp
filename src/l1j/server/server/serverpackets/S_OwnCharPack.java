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

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

//Referenced classes of package l1j.server.server.serverpackets:
//ServerBasePacket

public class S_OwnCharPack extends ServerBasePacket {

	private static final String S_OWN_CHAR_PACK = "[S] S_OwnCharPack";
	private static final int STATUS_INVISIBLE = 2;
	private static final int STATUS_PC = 4;
	private static final int STATUS_FREEZE = 8;
	private static final int STATUS_BRAVE = 16;
	private static final int STATUS_ELFBRAVE = 32;
	private static final int STATUS_FASTMOVABLE = 64;
	private static final int STATUS_GHOST = 128;
	// private static final int BLOOD_LUST = 100;

	private byte[] _byte = null;

	public S_OwnCharPack(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		int status = STATUS_PC;

		// 굴독같은 초록의 독
		// if (pc.isPoison()) {
		// status |= STATUS_POISON;
		// }
		if (pc.isInvisble() || pc.isGmInvis()) {
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
		writeC(Opcodes.S_PUT_OBJECT);
		writeH(pc.getX());
		writeH(pc.getY());
		writeD(pc.getId());
		/*
		 * String s = "00 00 04 05 00 01 01 "+
		 * "00 00 00 ff 7f c6 e4 b8 a3 b3 ad 73 00 74 69 74 "+
		 * "6c 65 32 32 32 00 04 31 0a 08 00 b2 c3 c5 eb c5 "+
		 * "ac b7 b4 00 00 40 ff 00 00 00 ff ff 00 04 00 37 "; StringTokenizer st = new
		 * StringTokenizer(s); while(st.hasMoreTokens()){
		 * writeC(Integer.parseInt(st.nextToken(), 16)); }
		 */

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
				if (weaponId == 24 && pc.getWeapon() != null && pc.getWeapon().getItem().getType() == 18)
					weaponId = 83;
			} else if (polyId == 13152 || polyId == 13153 || polyId == 12702 || polyId == 15154 || polyId == 15232 || polyId == 14923 || polyId == 15223
					|| polyId == 12681 || polyId == 8812 || polyId == 8817 || polyId == 6267 || polyId == 6270 || polyId == 6273 || polyId == 6276
					|| polyId == 16014 || polyId == 15986 || polyId == 16027 || polyId == 16284 || polyId == 16053 || polyId == 16040 || polyId == 17541
					|| polyId == 17515 || polyId == 17531) {
				if (weaponId == 24 && pc.getWeapon() != null && pc.getWeapon().getItem().getType() == 18)
					weaponId = 50;
			}
			writeC(weaponId);
		}

		writeC(pc.getMoveState().getHeading());
		// writeC(addbyte);
		writeC(pc.getLight().getOwnLightSize());
		// System.out.println(pc.getMoveState().getMoveSpeed());
		writeC(pc.getMoveState().getMoveSpeed());
		writeD(pc.getExp());
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
		writeS(null);
		if (pc.getClanid() == 0 || pc.getClan() == null) {// 이시발놈!
			writeC(0);
		} else
			writeC(pc.getClanRank() * 0x10);
		// writeC(0); // ?
		// writeC(0xb0);
		// writeS(null); // 펫호팅?
		// writeC(0); // ?
		/*
		 * if(pc.getClanid() == 0 || pc.getClan()== null){//이시발놈! writeC(0xb0); }else
		 * writeC(pc.getClanRank()*0x10);
		 */// ??
			// writeC(0xB0); // ??
		/*
		 * if (pc.isInParty()) // 파티중 { writeC(100 * pc.getCurrentHp() / pc.getMaxHp());
		 * //writeC(100 * pc.getCurrentMp() / pc.getMaxMp()); } else {
		 */
		writeC(0xFF);
		// writeC(0xFF);
		// }
		if (pc.isThirdSpeed())
			writeC(0x08);
		else
			writeC(0); // 타르쿡크 거리(대로)
		writeC(0); // PC = 0, Mon = Lv
		if (pc.isPrivateShop())
			writeByte(pc.getShopChat());
		writeC(0); // ?
		writeC(0xFF);
		writeC(0xFF);
		writeC(0x00);
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

	/** 신 규 오 브 젝 트 표 현 - 연구가 필요함.. x,y **/
	/*
	 * public S_ACTION_UI(L1PcInstance pc) { int tt= pc.getY();
	 * writeC(Opcodes.S_ACTION_UI); writeC(NEW_OBJECT); writeC(0x00); writeC(0x08);
	 * writeH(32768+pc.getX());//X좌표
	 * 
	 * if (tt < 32768){ writeH(pc.getY() + 32000 + ((pc.getY()-32640)*3+1));//Y좌표
	 * }else{ writeH(pc.getY() + ((pc.getY()-32640)*3+1));//Y좌표 } //writeH(0xffbe);
	 * //writeH(0x8281); writeC(0x08); writeC(0x10); write4bit(pc.getId()); //f2 9a
	 * f4 80 //writeD(0x80f49af2); writeC(0x18); //01 18 //writeH(0x1801);//??
	 * write4bit(pc.getTempCharGfx()); writeC(0x20); if (pc.isDead()) {
	 * writeC(pc.getStatus()); } else if (pc.isPrivateShop()) { writeC(70); } else {
	 * writeC(pc.getCurrentWeapon()); } writeC(0x28); writeC(pc.getHeading());//히딩
	 * //30 00 38 01 40 00 4a String s = "30 00 38 01 40 00 4a"; StringTokenizer st1
	 * = new StringTokenizer(s.toString()); while (st1.hasMoreTokens()) {
	 * writeC(Integer.parseInt(st1.nextToken(), 16)); } byte[] name =
	 * pc.getName().getBytes(); writeC(name.length);//이름길이 writeByte(name);//이름
	 * writeC(0x52); byte[] title = pc.getTitle().getBytes(); writeC(title.length);
	 * writeByte(title);
	 * 
	 * String sb =
	 * "58 00 60 00 68 00 70 00 78 00 80 01 01 88 01 00 90 01 00 98 01 00 "+
	 * "a2 01 00 aa 01 00 b0 01 00 b8 01 ff ff ff ff ff ff ff ff ff "+
	 * "01 c0 01 00 "+ "ca 01 00 "+
	 * "d0 01 ff ff ff ff ff ff ff ff ff 01 d8 01 00 e0 "+
	 * "01 01 f0 01 ff ff ff ff ff ff ff ff ff 01 80 02 08 00 00"; StringTokenizer
	 * st = new StringTokenizer(sb.toString()); while (st.hasMoreTokens()) {
	 * writeC(Integer.parseInt(st.nextToken(), 16)); } }
	 */

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_OWN_CHAR_PACK;
	}

}