/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
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

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1BoardInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class S_Hunt extends ServerBasePacket {

	private byte[] _byte = null;

	public S_Hunt(L1BoardInstance board) {
		writeC(Opcodes.S_BOARD_LIST);
		writeC(0x00); // ?
		writeD(board.getId());
		writeC(0xFF);
		writeC(0xFF);
		writeC(0xFF);
		writeC(0x7F);
		Connection c = null;
		PreparedStatement p = null;
		PreparedStatement pp = null;
		ResultSet r = null;
		ResultSet rr = null;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			pp = c.prepareStatement("select count(*) as cnt from characters where HuntCount=1");
			rr = pp.executeQuery();
			int count = 0;
			if (rr.next())
				count = rr.getInt("cnt");
			p = c.prepareStatement("select * from characters where HuntPrice > 1 order by HuntPrice desc");
			r = p.executeQuery();
			writeH(count);
			writeH(300);
			while (r.next()) {
				// ++i;
				writeD(r.getInt(2));
				writeS(Config.getserver() + "서버");
				writeS("");
				writeS("현상범  : [" + r.getString(3) + "]");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rr);
			SQLUtil.close(pp);
			SQLUtil.close(r);
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
	}

	public S_Hunt(L1PcInstance pc, int number) {
		writeC(Opcodes.S_BOARD_READ);
		writeD(number);
		Connection c = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("select * from characters where HuntCount=1 and objid=?");
			p.setInt(1, number);
			r = p.executeQuery();
			if (r.next()) {
				writeS("서버");
				writeS("현상범  : [" + r.getString(3) + "]");
				writeS("");
				StringBuffer sb = new StringBuffer();
				//
				sb.append("\r\n\r\n");
				sb.append("현상범 케릭명 : ").append(r.getString(3))
						.append("\r\n\r\n");
				sb.append("\r\n\r\n");
				sb.append("현상금액수 : ").append(r.getInt(59)).append(" 원")
						.append("\r\n\r\n");
				sb.append("\r\n\r\n");
				sb.append("사유 : ").append(r.getString(60));
				sb.append("\r\n\r\n");
				sb.append(Config.getserver() + "서버-");
				writeS(sb.toString());
				sb = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(r);
			SQLUtil.close(p);
			SQLUtil.close(c);
		}
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}
}
