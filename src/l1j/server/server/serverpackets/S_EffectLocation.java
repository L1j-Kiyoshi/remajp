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
import l1j.server.server.model.L1Location;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.types.Point;

public class S_EffectLocation extends ServerBasePacket {

	private byte[] _byte = null;

	/**
	 * 지정된 위치에 효과를 표시하는 패킷을 구축한다.
	 * 
	 * @param pt
	 *            - 효과를 표시하는 위치를 격납한 Point 오브젝트
	 * @param gfxId
	 *            - 표시하는 효과의 ID
	 */
	public S_EffectLocation(Point pt, short gfxId) {
		this(pt.getX(), pt.getY(), gfxId);
	}

	/**
	 * 지정된 위치에 효과를 표시하는 패킷을 구축한다.
	 * 
	 * @param loc
	 *            - 효과를 표시하는 위치를 격납한 L1Location 오브젝트
	 * @param gfxId
	 *            - 표시하는 효과의 ID
	 */
	public S_EffectLocation(L1Location loc, short gfxId) {
		this(loc.getX(), loc.getY(), gfxId);
	}

	/**
	 * 지정된 위치에 효과를 표시하는 패킷을 구축한다.
	 * 
	 * @param x
	 *            - 효과를 표시하는 위치의 X좌표
	 * @param y
	 *            - 효과를 표시하는 위치의 Y좌표
	 * @param gfxId
	 *            - 표시하는 효과의 ID
	 */
	public S_EffectLocation(int x, int y, short gfxId) {
		writeC(Opcodes.S_EFFECT_LOC);
		writeH(x);
		writeH(y);
		writeH(gfxId);
		writeH(0);
	}

	public S_EffectLocation(int x, int y, int gfxId) {
		writeC(Opcodes.S_EFFECT_LOC);
		writeH(x);
		writeH(y);
		writeH(gfxId);
		writeH(0);
	}
	
	/** 인던형 디버프 */
	public S_EffectLocation(int SkillId, boolean On) {
		int[] NumBer = new int [2];
		switch (SkillId) {
			case L1SkillId.DungeonPoison:
				NumBer[0] = 9568; NumBer[1] = 6291;
				break;
			
			case L1SkillId.DungeonFlare:
				NumBer[0] = 9520; NumBer[1] = 6292;
				break;
		}
		
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(S_NewCreateItem.버프창);
		
		writeC(0x08);
		writeC(On ? 1 : 3);

		writeC(0x10);
		writeBit(SkillId);
		
		if(On){
			writeC(0x18);
			/** 2초 고정 */
			writeBit(2L);
	
			writeC(0x28);
			/** 스킬 아이콘 */
			writeBit(NumBer[0]);
			
			writeC(0x40);
			/** 스킬 메세지 */
			writeBit(NumBer[1]);
			
			/** 디버프형식 스킬들은 아래뜨게 새팅 */
			writeC(0x58);
			writeC(0x00);
		}
		
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
		return "S_EffectLocation";
	}
}
