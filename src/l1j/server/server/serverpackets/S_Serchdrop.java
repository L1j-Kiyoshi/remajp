package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.datatables.NpcTable;
import l1j.server.server.utils.SQLUtil;

public class S_Serchdrop extends ServerBasePacket {

	private static final String S_Serchdrop = "[S] S_Serchdrop";

	private static Logger _log = Logger.getLogger(S_Serchdrop.class.getName());

	private byte[] _byte = null;

	int mobid[] = new int[30];
	String mobname[] = new String[30];

	public S_Serchdrop(int itemid) {
		buildPacket(itemid);
	}

	private void buildPacket(int itemid) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		int i = 0;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT `mobId` FROM `droplist` WHERE itemId=? ORDER BY `mobId` DESC LIMIT 30");
			pstm.setInt(1, itemid);
			rs = pstm.executeQuery();

			while (rs.next()) {
				mobid[i] = rs.getInt(1);
				mobname[i] = NpcTable.getInstance().getTemplate(mobid[i])
						.get_name();
				i++;
			}

			writeC(Opcodes.S_BOARD_READ);
			writeD(0);// 넘버
			writeS(" ");// 글쓴이?
			writeS(Config.getserver() + "드랍리스트");
			writeS("");
			writeS("\r검색할 아이템 : "
					+ ItemTable.getInstance().getTemplate(itemid).getName()
					+ "\n\n\r******  드랍하는 몹  ******" + "\n\n\r" + mobname[0]
					+ " | " + mobname[1] + " | " + mobname[2] + " | "
					+ mobname[3] + " | " + mobname[4] + " | " + mobname[5]
					+ " | " + mobname[6] + " | " + mobname[7] + " | "
					+ mobname[8] + " | " + mobname[9] + " | " + mobname[10]
					+ " | " + mobname[11] + " | " + mobname[12] + " | "
					+ mobname[13] + " | " + mobname[14] + " | " + mobname[15]
					+ " | " + mobname[16] + " | " + mobname[17] + " | "
					+ mobname[18] + " | " + mobname[19] + " | " + mobname[20]
					+ " | " + mobname[21] + " | " + mobname[22] + " | "
					+ mobname[23] + " | " + mobname[24] + " | " + mobname[25]
					+ " | " + mobname[26] + " | " + mobname[27] + " | "
					+ mobname[28] + " | " + mobname[29]);

		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
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

	public String getType() {
		return S_Serchdrop;
	}
}
