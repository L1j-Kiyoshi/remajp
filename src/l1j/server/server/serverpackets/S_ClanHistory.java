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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

// Referenced classes of package l1j.server.server.serverpackets:
// ServerBasePacket

public class S_ClanHistory extends ServerBasePacket {

	private static final String S_CLAN_HISTORY = "[S] S_ClanHistory";
	private byte[] _byte = null;

	public S_ClanHistory(L1PcInstance pc) {
		buildPacket(pc);
	}

	private void buildPacket(L1PcInstance pc) {
		writeC(Opcodes.S_EVENT);
		writeC(0x75);
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM clan_history WHERE clan_id =? ORDER BY num DESC");
			pstm.setInt(1, pc.getClanid());
			rs = pstm.executeQuery();
			rs.last();
			writeD(rs.getRow());
			rs.beforeFirst();
			while (rs.next()) {
				writeS(rs.getString("char_name"));
				writeC(rs.getInt("ckck"));
				writeS(rs.getString("item_name"));
				writeD(rs.getInt("item_count"));
				Timestamp 분계산 = new Timestamp((System.currentTimeMillis() - rs
						.getTimestamp("time").getTime()));
				writeD((int) 분계산.getTime() / 1000 / 60);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
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
		return S_CLAN_HISTORY;
	}
}
