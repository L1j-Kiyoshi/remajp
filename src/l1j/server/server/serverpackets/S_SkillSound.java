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
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_SkillSound extends ServerBasePacket {
	private static final String S_SKILL_SOUND = "[S] S_SkillSound";

	private byte[] _byte = null;

	public S_SkillSound(int objid, int gfxid, int aid) {
		L1Object cha = L1World.getInstance().findObject(objid);
		L1PcInstance pc = null;
		if (cha instanceof L1PcInstance) {
			pc = (L1PcInstance) cha;
			if (pc.isInvisble()) {
				S_EffectLocation(pc.getX(), pc.getY(), gfxid);
				return;
			}
		}
		buildPacket(objid, gfxid, aid);
	}

	public S_SkillSound(int objid, int gfxid) {
		L1Object cha = L1World.getInstance().findObject(objid);
		L1PcInstance pc = null;
		if (cha instanceof L1PcInstance) {
			pc = (L1PcInstance) cha;
			if (pc.isInvisble()) {
				S_EffectLocation(pc.getX(), pc.getY(), gfxid);
				return;
			}
		}

		buildPacket(objid, gfxid, 0);
	}

	public void S_EffectLocation(int x, int y, int gfxId) {
		writeC(Opcodes.S_EFFECT_LOC);
		writeH(x);  
		writeH(y);
		writeH(gfxId);
		// writeH(0);
	}

	private void buildPacket(int objid, int gfxid, int aid) {
		// aid는 사용되지 않았다
		writeC(Opcodes.S_EFFECT);
		writeD(objid);
		writeH(gfxid);
		// writeC(0);
		// writeD(0x00000000);
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
		return S_SKILL_SOUND;
	}
}
