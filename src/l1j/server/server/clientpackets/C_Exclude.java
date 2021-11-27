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

import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.server.datatables.ExcludeLetterTable;
import l1j.server.server.datatables.ExcludeTable;
import l1j.server.server.model.L1ExcludingLetterList;
import l1j.server.server.model.L1ExcludingList;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import server.LineageClient;

//Referenced classes of package l1j.server.server.clientpackets:
//ClientBasePacket

public class C_Exclude extends ClientBasePacket {
	

	private static final String C_EXCLUDE = "[C] C_Exclude";
	private static Logger _log = Logger.getLogger(C_Exclude.class.getName());

	/**
	 * C_1 /exclude 커멘드를 쳤을 때에 보내진다
	 */
	public C_Exclude(byte[] decrypt, LineageClient client) {
		super(decrypt);
		try {
			String name = readS();
			int addndel = readC();// 1추가 0삭제
			int type = readC();// 0일반 1편지
			L1PcInstance pc = client.getActiveChar();
			if (pc == null)
				return;

			try {

				/*
				 * if(type == 0){ if(!pc.차단Load){ pc.차단Load = true;
				 * pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE2,
				 * pc.getExcludingList().getList(), type), true); } }else
				 * if(type == 1){ if(!pc.차단편지Load){ pc.차단편지Load = true;
				 * pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE2,
				 * pc.getExcludingLetterList().getList(), type), true); } }
				 */
				if (name.isEmpty()) {
					pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE2, pc.getExcludingList().getList(), 0), true);
					pc.sendPackets(new S_PacketBox(S_PacketBox.ADD_EXCLUDE2, pc.getExcludingLetterList().getList(), 1), true);
					return;
				}

				if (addndel == 1) {// 추가
					L1ExcludingList exList = pc.getExcludingList();
					L1ExcludingLetterList exletterList = pc.getExcludingLetterList();
					switch (type) {// 일반 0 편지 1
					case 0:
						if (exList.contains(name)) {
							/*
							 * String temp = exList.remove(name); S_PacketBox pb
							 * = new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp,
							 * type); pc.sendPackets(pb, true);
							 * ExcludeTable.getInstance().delete(pc.getName(),
							 * name);
							 */
						} else {
							if (exList.isFull()) {
								S_SystemMessage sm = new S_SystemMessage("차단된 사용자가 너무 많습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							exList.add(name);
							S_PacketBox pb = new S_PacketBox(S_PacketBox.ADD_EXCLUDE, name, 0);
							pc.sendPackets(pb, true);
							ExcludeTable.getInstance().add(pc.getName(), name);
						}
						break;
					case 1:
						if (exletterList.contains(name)) {
							/*
							 * String temp = exList.remove(name); S_PacketBox pb
							 * = new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp,
							 * type); pc.sendPackets(pb, true);
							 * ExcludeLetterTable
							 * .getInstance().delete(pc.getName(), name);
							 */
						} else {
							if (exletterList.isFull()) {
								S_SystemMessage sm = new S_SystemMessage("차단된 사용자가 너무 많습니다.");
								pc.sendPackets(sm, true);
								return;
							}
							exletterList.add(name);
							S_PacketBox pb = new S_PacketBox(S_PacketBox.ADD_EXCLUDE, name, 1);
							pc.sendPackets(pb, true);
							ExcludeLetterTable.getInstance().add(pc.getName(), name);
						}
						break;
					default:
						break;
					}
				} else if (addndel == 0) {// 삭제
					L1ExcludingList exList = pc.getExcludingList();
					L1ExcludingLetterList exletterList = pc.getExcludingLetterList();
					switch (type) {// 일반 0 편지 1
					case 0:
						if (exList.contains(name)) {
							String temp = exList.remove(name);
							S_PacketBox pb = new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp, 0);
							pc.sendPackets(pb, true);
							ExcludeTable.getInstance().delete(pc.getName(), name);
						}
						break;
					case 1:
						if (exletterList.contains(name)) {
							String temp = exletterList.remove(name);
							S_PacketBox pb = new S_PacketBox(S_PacketBox.REM_EXCLUDE, temp, 1);
							pc.sendPackets(pb, true);
							ExcludeLetterTable.getInstance().delete(pc.getName(), name);
						}
						break;
					default:
						break;
					}
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			}
		} catch (Exception e) {

		} finally {
			clear();
		}
	}

	@Override
	public String getType() {
		return C_EXCLUDE;
	}
}
