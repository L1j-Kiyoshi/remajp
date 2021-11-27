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

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.ClanTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_Emblem;
import l1j.server.server.serverpackets.S_ReturnedStat;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;

public class C_Emblem extends ClientBasePacket {
	private static final String C_EMBLEM = "[C] C_Emblem";
	private static Logger _log = Logger.getLogger(C_Emblem.class.getName());

	public C_Emblem(byte abyte0[], LineageClient clientthread) throws Exception {
		super(abyte0);
		try {
			L1PcInstance player = clientthread.getActiveChar();
			if (player.getClanid() != 0) {
				if (!player.isCrown() || player.getClanRank() != L1Clan.CLAN_RANK_PRINCE) {
					return;
				}
				
				try {
					File deleteFile = new File("emblem/" + String.valueOf(player.getClanid()));
					deleteFile.delete();
				} catch (Exception e) {
				}

				player.setClanid(ObjectIdFactory.getInstance().nextId());
				player.getClan().setClanId(player.getClanid());
				ClanTable.getInstance().updateClan(player.getClan());
				
				for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
					if (pc.getClanid() > 0) {
						if (pc.getClanname().equalsIgnoreCase(player.getClanname())) {
							pc.setClanid(player.getClanid());
						}
					}
				}
				
				updateClanId(player);
				String emblem_file = String.valueOf(player.getClanid());
				FileOutputStream fos = null;
				
				try {
					fos = new FileOutputStream("emblem/" + emblem_file);
					
					for (short cnt = 0; cnt < 1251; cnt++) {
						fos.write(readC());
					}
				} catch (Exception e) {
					_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
					throw e;
				} finally {
					if (null != fos) {
						fos.close();
					}
					
					fos = null;
				}
				
				for (L1PcInstance pc : player.getClan().getOnlineClanMember()) {
					pc.sendPackets(new S_ReturnedStat(pc, S_ReturnedStat.CLAN_JOIN_LEAVE), true);
					Broadcaster.broadcastPacket(pc, new S_ReturnedStat(pc, S_ReturnedStat.CLAN_JOIN_LEAVE), true);
				}
				
				 S_Emblem em = new S_Emblem(player.getClanid(), player.getClanid());
				 player.sendPackets(em);
				 L1World.getInstance().broadcastPacketToAll(em, true);
			}
		} catch (Exception e) {
			// e.printStackTrace();
		} finally {
			clear();
		}
	}

	
	private void updateClanId(L1PcInstance player) {
		Connection con = null;
		PreparedStatement pstm = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("UPDATE characters SET ClanID=? WHERE Clanname=?");
			pstm.setInt(1, player.getClanid());
			pstm.setString(2, player.getClanname());
			pstm.executeUpdate();
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

	
	@Override
	public String getType() {
		return C_EMBLEM;
	}
	
}
