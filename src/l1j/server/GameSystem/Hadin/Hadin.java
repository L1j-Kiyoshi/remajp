/**
 * 본섭 리뉴얼된 Chapter1. 비밀의 마법사 하딘
 * 하딘 맵 및 입장 관리 클래스
 * by. 케인
 */
package l1j.server.GameSystem.Hadin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javolution.util.FastMap;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.Astar.World;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class Hadin {

	private static final FastMap<L1Map, Boolean> mapList = new FastMap<L1Map, Boolean>()
			.shared();

	private static Hadin _instance;

	public static Hadin get() {
		if (_instance == null)
			_instance = new Hadin();
		return _instance;
	}

	private Hadin() {
		for (byte i = 1; i < 100; i++) {
			short mapid = (short) (9000 + i);
			if (mapid != 9000) {
				L1WorldMap.getInstance().cloneMap(9000, mapid);
				World.cloneMap(9000, mapid);
			}
			mapList.put(L1WorldMap.getInstance().getMap(mapid), false);
		}
	}

	public synchronized void start(L1PcInstance pc) {
		try {
			L1Map map = mapCheck();
			if (map == null) {
				S_SystemMessage sm = new S_SystemMessage(
						"인스턴스 던전을 더 이상 생성할수 없습니다.");
				pc.sendPackets(sm, true);
				return;
			}

			synchronized (mapList) {
				mapList.put(map, true);
			}

			for (L1PcInstance Ppc : pc.getParty().getMembers()) {
				if (Ppc != null) {
					L1Teleport.teleport(Ppc, 32726, 32724, (short) map.getId(),
							Ppc.getMoveState().getHeading(), true);
					databaseInsert(Ppc);
				}
			}

			HadinThread.get().add(pc.getParty(), (short) map.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private synchronized L1Map mapCheck() {
		synchronized (mapList) {
			for (FastMap.Entry<L1Map, Boolean> e = mapList.head(), mapEnd = mapList
					.tail(); (e = e.getNext()) != mapEnd;) {
				if (!e.getValue()) {
					return e.getKey();
				}
			}
		}
		return null;
	}

	public void quit(L1Map map) {
		L1V1Map m = (L1V1Map) L1WorldMap.getInstance().getMap(
				(short) map.getId());
		m.reset((L1V1Map) L1WorldMap.getInstance().getMap((short) 9000));
		// World.resetMap(9000, map.getId());
		synchronized (mapList) {
			mapList.put(map, false);
		}
	}

	private void databaseInsert(L1PcInstance pc) {
		Connection con = null;
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pst = con
					.prepareStatement("SELECT * FROM instance_dungeon_in WHERE charName = ?");
			pst.setString(1, pc.getName());
			rs = pst.executeQuery();
			if (rs.next()) {
				int count = rs.getInt("hadin_count");
				Connection con2 = null;
				PreparedStatement pstm2 = null;
				try {
					con2 = L1DatabaseFactory.getInstance().getConnection();
					pstm2 = con2
							.prepareStatement("UPDATE instance_dungeon_in SET hadin_count=? WHERE charName=?");
					pstm2.setInt(1, count + 1);
					pstm2.setString(2, pc.getName());
					pstm2.executeUpdate();
				} catch (SQLException e) {
				} finally {
					SQLUtil.close(pstm2);
					SQLUtil.close(con2);
				}
			} else {
				Connection con2 = null;
				PreparedStatement pstm2 = null;
				try {
					con2 = L1DatabaseFactory.getInstance().getConnection();
					pstm2 = con
							.prepareStatement("INSERT INTO instance_dungeon_in SET charName=?, hadin_count=?");
					pstm2.setString(1, pc.getName());
					pstm2.setInt(2, 1);
					pstm2.executeUpdate();
				} catch (SQLException e) {
				} finally {
					SQLUtil.close(pstm2);
					SQLUtil.close(con2);
				}
			}
		} catch (SQLException e) {
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pst);
			SQLUtil.close(con);
		}
	}
}
