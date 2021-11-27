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

import l1j.server.Warehouse.SupplementaryService;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.shop.L1AdenShop;
import l1j.server.server.serverpackets.S_NcoinShop;
import l1j.server.server.serverpackets.S_RetrieveSupplementaryService;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SurvivalCry;
import server.LineageClient;

public class C_Adenshop extends ClientBasePacket {
	private static final String C_ADD_BOOKMARK = "[C] C_AddBookmark";

	public C_Adenshop(byte[] decrypt, LineageClient client) {
		super(decrypt);
		
		try {
			int type = readH();
			L1PcInstance pc = client.getActiveChar();
			switch (type) {
				case 1: { // 상점 열기
					/*
					 * client.sendPacket(new S_SurvivalCry(S_SurvivalCry.LIST, pc),
					 * true); client.sendPacket(new
					 * S_SurvivalCry(S_SurvivalCry.EMAIL, pc), true);
					 * client.sendPacket(new S_SurvivalCry(S_SurvivalCry.POINT, pc),
					 * true);
					 */
					if (pc == null) {
						return;
					}
					
					pc.sendPackets(new S_NcoinShop(pc));
				}
				break;
				case 4: { // OTP 입력
					for (int i = 0; i < 1000; i++) {
						int ff = readH();
						if (ff == 0) {
							break;
						}
					}
					
					for (int i = 0; i < 16 * 8 + 1; i++) {
						readC();
					}
					
					int size = readH();
					
					if (size == 0) {
						return;
					}
					
					L1AdenShop as = new L1AdenShop();
					for (int i = 0; i < size; i++) {
						int id = readD();
						int count = readH();
						
						if (count <= 0 || count >= 10000) {
							return;
						}
						
						as.add(id, count);
					}
					
					if (!as.BugOk()) {
						if (as.commit(pc)) {
							client.sendPacket(new S_SurvivalCry(S_SurvivalCry.OTP_CHECK_MSG, pc), true);
						}
					}
					
					break;
				}
				case 6: { // 부가서비스 창고
					SupplementaryService warehouse = WarehouseManager.getInstance().getSupplementaryService(pc.getAccountName());
					int size = warehouse.getSize();
					if (size > 0) {
						pc.sendPackets(new S_RetrieveSupplementaryService(pc.getId(), pc));
					} else {
						pc.sendPackets(new S_ServerMessage(1625), true);
					}
					
					break;
				}
				case 0x32: {// 동의 및 구매
					client.sendPacket(new S_SurvivalCry(S_SurvivalCry.OTP_SHOW, pc), true);
					break;
				}
				default:
					break;
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
