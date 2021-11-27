package l1j.server.server.serverpackets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.Opcodes;
import l1j.server.server.utils.SQLUtil;

public class S_CharacterConfig extends ServerBasePacket {

	private static Logger _log = Logger.getLogger(S_CharacterConfig.class
			.getName());
	private static final String S_CHARACTER_CONFIG = "[S] S_CharacterConfig";
	private byte[] _byte = null;

	public S_CharacterConfig(int objectId) {
		buildPacket(objectId);
	}

	public S_CharacterConfig(int objectId, int i) {
		buildPacket(objectId);
	}

	private String s = "0f 29 ec 00 00 00 4e e2 44 d3 40 00 80 32 00 00 "
			+ "74 00 b3 01 09 0c 01 00 7d ff ff ff fd ff 08 00 "
			+ "00 00 00 00 00 00 00 00 00 00 82 00 00 00 2a 33 "
			+ "70 e9 6d 27 22 2c 05 1f 29 35 00 00 00 00 00 00 "
			+ "00 00 00 02 03 1f 00 00 00 00 15 03 38 2a 26 03 "
			+ "04 16 df 7d 71 32 eb c2 85 0d 7f 37 51 03 5a 75 "
			+ "72 25 37 81 b2 2a f2 15 2e 2e e5 98 7b 21 bd 5e "
			+ "6a 3d 4c a0 e2 0b ac 78 18 16 92 76 48 18 bd 04 "
			+ "3b 22 1d c6 0f 16 18 2e c3 27 9f 91 40 39 b0 c4 "
			+ "07 15 b0 c4 07 15 f1 8d f1 23 cc 6f 2d 29 a9 13 "
			+ "13 1f 4b e9 99 01 99 f8 9a 0f 00 00 00 00 00 00 "
			+ "00 00 00 ff ff ff 00 00 00 00 00 00 00 00 00 fe "
			+ "fe fe fe fe fe fe fe fe fe fe fe 03 00 00 00 03 "
			+ "00 00 00 01 6c 02 d1 01 e7 ee 2a 52 00 00 2c 01 "
			+ "00 01 c3 01 00 00 d6 01 00 00 01 06 0a 00 00 00 "
			+ "00 00 00 00";

	public S_CharacterConfig() {
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}

	private void buildPacket(int objectId) {
		int length = 0;
		byte data[] = null;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM character_config WHERE object_id=?");
			pstm.setInt(1, objectId);
			rs = pstm.executeQuery();
			while (rs.next()) {
				length = rs.getInt(2);
				data = rs.getBytes(3);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		writeC(Opcodes.S_EVENT);
		writeC(41);
		writeD(length);
		if (length != 0) {
			writeByte(data);
		} else {
			writeH(0x00);
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
		return S_CHARACTER_CONFIG;
	}
}
