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

package l1j.server.server.clientpackets;

import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1HouseLocation;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1BookMark;
import server.LineageClient;


/**
 * 장소 기억 처리
 * @author user
 *
 */
public class C_AddBookmark extends ClientBasePacket {
	

	private static final String C_ADD_BOOKMARK = "[C] C_AddBookmark";

	
	public C_AddBookmark(byte[] decrypt, LineageClient client) {
		super(decrypt);
		
		try {
			String s = readS();

			L1PcInstance pc = client.getActiveChar();
			if (pc.isGhost()) {
				return;
			}

			if (s.getBytes().length > 30) {
				return;
			}

			if (pc.getMap().isMarkable() || pc.isGm()) {
				if ( pc.getMapId() == 4 
						&& ( (pc.getX() >= 33331 && pc.getX() <= 33341 && pc.getY() >= 32430 && pc.getY() <= 32441)
							|| (pc.getX() >= 33258 && pc.getX() <= 33267 && pc.getY() >= 32396 && pc.getY() <= 32407)
							|| (pc.getX() >= 33509 && pc.getX() <= 33811) 
							&& (pc.getY() >= 32197 && pc.getY() <= 32413)
							|| (pc.getX() >= 34197 && pc.getX() <= 34302 && pc.getY() >= 33104 && pc.getY() <= 33533 && pc.getMap().isNormalZone(pc.getX(), pc.getY()) )
							|| (pc.getX() >= 33453 && pc.getX() <= 33468 && pc.getY() >= 32331 && pc.getY() <= 32341) // 황혼의산맥
							|| (pc.getX() >= 33461 && pc.getX() <= 33474 && pc.getY() >= 32854 && pc.getY() <= 32870) // 아덴의한국민
							|| (pc.getX() >= 33464 && pc.getX() <= 33475 && pc.getY() >= 32843 && pc.getY() <= 32852) // 아덴의한국민
							|| (pc.getX() >= 33388 && pc.getX() <= 33397 && pc.getY() >= 32339 && pc.getY() <= 32350) // 아덴의한국민
							|| (pc.getX() >= 33464 && pc.getX() <= 33531 && pc.getY() >= 33168 && pc.getY() <= 33248) // ||
							/*
							 * (pc.getX() >= 33443 && pc.getX() <= 33483 &&
							 * pc.getY() >= 32315 && pc.getY() <= 32357)
							 */ 
							) /* && !pc.isGm() */ 
					) {
					
					pc.sendPackets(new S_SystemMessage("이곳을 기억할 수 없습니다."), true);  // \f1여기를 기억할 수가 없습니다.
				} else if ( (L1CastleLocation.checkInAllWarArea(pc.getX(), pc.getY(), pc.getMapId())
							|| pc.getMapId() == 66 || L1HouseLocation.isInHouse(pc.getX(), pc.getY(), pc.getMapId()) )
							|| ( (pc.getX() >= 33514 && pc.getX() <= 33809) 
									&& (pc.getY() >= 32216 && pc.getY() <= 32457) 
									&& pc.getMapId() == 4 )
							|| ( (pc.getX() >= 33472 && pc.getX() <= 33536)
									&& (pc.getY() >= 32838 && pc.getY() <= 32876) 
									&& pc.getMapId() == 4 )
							|| ( pc.getX() >= 33464 && pc.getX() <= 33531 && pc.getY() >= 33168 && pc.getY() <= 33248 )
							&& pc.getMapId() == 4
							|| ( (pc.getX() >= 34211 && pc.getX() <= 34287) 
									&& (pc.getY() >= 33103 && pc.getY() <= 33492) 
									&& pc.getMapId() == 4 )
							|| (  pc.getX() >= 32704 && pc.getX() <= 32835 && pc.getY() >= 33110 && pc.getY() <= 33234 && pc.getMapId() == 4 ) 
							&& !pc.isGm() ) {
					
					pc.sendPackets(new S_SystemMessage("이곳을 기억할 수 없습니다."), true); // \f1여기를 기억할 수가 없습니다.
				} else {
					L1BookMark.addBookmark(pc, s);
				}
			} else {
				pc.sendPackets(new S_SystemMessage("이곳을 기억할 수 없습니다."), true); // \f1여기를 기억할 수가 없습니다.
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	
	@Override
	public String getType() {
		return C_ADD_BOOKMARK;
	}
	
}
