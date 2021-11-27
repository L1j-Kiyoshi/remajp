package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class HongboTable {
	private static HongboTable _instance;

	public HongboTable() {
	}

	public static HongboTable getInstance() {
		if (_instance == null) {
			_instance = new HongboTable();
		}
		return _instance;
	}

	public void infoCount(L1PcInstance pc) {
		Connection c = null;
		PreparedStatement p = null;
		ResultSet r = null;
		try {
			c = L1DatabaseFactory.getInstance().getConnection();
			p = c.prepareStatement("SELECT h.check FROM hongbo.h WHERE account ='"
					+ pc.getAccountName() + "'");
			r = p.executeQuery();
			if (r.next()) {
				if (r.getInt(1) == 1) {
					pc.sendPackets(new S_SystemMessage("[홍보인증] 홍보기가 인증되었습니다."));
					pc.setadFeature(2);
				} else {
					pc.sendPackets(new S_SystemMessage(
							"[홍보인증] 홍보기를 정상동작 해주십시요!"));
				}

			} else {
				pc.sendPackets(new S_SystemMessage("[홍보인증] 홍보기를 정상동작 해주십시요!"));
			}
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage("[홍보인증] 홍보기를 정상동작 해주십시요!"));
		} finally {
			SQLUtil.close(r);
			SQLUtil.close(p);
			SQLUtil.close(c);
		}

	}
}
