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

import l1j.server.MJInstanceSystem.MJInstanceSpace;
import l1j.server.server.Opcodes;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_MapID extends ServerBasePacket {

	private static final String S_MapID = "[S] S_MapID";

	public S_MapID(int mapid, boolean isUnderwater) {
		writeC(Opcodes.S_WORLD);
		if (mapid >= 1005 && mapid <= 1010) mapid = 1005;
		if (mapid >= 1011 && mapid <= 1016) mapid = 1011;
		if (mapid >= 10000 && mapid <= 10005) mapid = 1017;
		if (mapid >= 9001 && mapid <= 9099) mapid = 9000;// 하딘
		if (mapid >= 9103 && mapid <= 9199) mapid = 9101;// 해상전
		if (mapid >= 2102 && mapid <= 2151) mapid = 2101;// 얼던
		if (mapid >= 10010 && mapid <= 10100) mapid = 1936;// 중앙사원
		if (mapid >= 2601 && mapid <= 2698) mapid = 2600;// 화둥안식처
		if (mapid >= 13001 && mapid <= 13050) mapid = 730;// 인던(하)
		if (mapid >= 13051 && mapid <= 13100) mapid = 731;// 인던(중)
		
		/** 2016.11.26 MJ 앱센터 LFC **/
		mapid	= MJInstanceSpace.getInstance().getIdenMap(mapid);
		/** 2016.11.26 MJ 앱센터 LFC **/
		
		writeH(mapid);
		writeC(isUnderwater ? 1 : 0);
		writeC(isUnderwater ? 1 : 0);
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}

	@Override
	public String getType() {
		return S_MapID;
	}
}
