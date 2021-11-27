package l1j.server.GameSystem.NavalWarfare;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javolution.util.FastMap;
import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class NavalWarfare {

	private static Random _rnd = new Random(System.nanoTime());
	private FastMap<L1Map, Boolean> mapList = null;
	private FastTable<NavalWarfareController> navalList = null;
	private static NavalWarfare uniqueInstance = null;

	public static NavalWarfare getInstance() {
		if (uniqueInstance == null) {
			synchronized (NavalWarfare.class) {
				if (uniqueInstance == null)
					uniqueInstance = new NavalWarfare();
			}
		}
		return uniqueInstance;
	}

	public NavalWarfare() {
		mapList = new FastMap<L1Map, Boolean>().shared();
		navalList = new FastTable<NavalWarfareController>();

		for (int i = 9103; i < 9200; i++) {
			L1WorldMap.getInstance().cloneMap(9101, i);
			// World.cloneMap(9101, i);
			mapList.put(L1WorldMap.getInstance().getMap((short) i), false);
		}
	}

	public boolean start(L1PcInstance pc) {
		L1Map map = null;
		synchronized (mapList) {
			map = mapCheck();
			if (map == null) {
				pc.sendPackets(new S_SystemMessage("인원이 너무 많아 입장 하실 수 없습니다."));
				return true;
			}
			mapList.put(map, true);
		}

		for (L1PcInstance tempt : L1World.getInstance().getAllPlayers()) {
			if (tempt.getMapId() == map.getId())
				L1Teleport.teleport(pc, 32574, 32942, (short) 0, 4, true);
		}

		NavalWarfareController nwfc = new NavalWarfareController(pc,
				map.getId(), false);
		for (L1PcInstance Ppc : pc.getParty().getMembers()) {
			if (Ppc != null) {
				L1Teleport.teleport(Ppc, 32792 + _rnd.nextInt(5), 32801 + _rnd
						.nextInt(5), (short) map.getId(), Ppc.getMoveState()
						.getHeading(), true);
				nwfc.addMember(Ppc);
				databaseInsert(Ppc);
			}
		}
		synchronized (navalList) {
			navalList.add(nwfc);
		}
		GeneralThreadPool.getInstance().schedule(nwfc, 15000);
		return false;
	}

	private L1Map mapCheck() {
		for (FastMap.Entry<L1Map, Boolean> e = mapList.head(), mapEnd = mapList
				.tail(); (e = e.getNext()) != mapEnd;) {
			if (!e.getValue()) {
				return e.getKey();
			}
		}
		return null;
	}

	public NavalWarfareController getNaval(short mapid) {
		NavalWarfareController nwc = null;
		synchronized (navalList) {
			for (NavalWarfareController nw : navalList) {
				if (nw.mapid == mapid) {
					nwc = nw;
					break;
				}
			}
		}
		return nwc;
	}

	public void NavalTrapCheck(short mapid, boolean ad) {
		NavalWarfareController nwc = null;
		synchronized (navalList) {
			for (NavalWarfareController nw : navalList) {
				if (nw.mapid == mapid) {
					nwc = nw;
					break;
				}
			}
		}
		if (nwc != null) {
			nwc.TrapAction(ad);
		}
	}

	public boolean NavalMoveTrapOn(short mapid) {
		NavalWarfareController nwc = null;
		synchronized (navalList) {
			for (NavalWarfareController nw : navalList) {
				if (nw.mapid == mapid) {
					nwc = nw;
					break;
				}
			}
		}
		if (nwc == null)
			return false;
		return nwc.bossMoveOn;
	}

	public boolean NavalMoveTrapOn2(short mapid) {
		NavalWarfareController nwc = null;
		synchronized (navalList) {
			for (NavalWarfareController nw : navalList) {
				if (nw.mapid == mapid) {
					nwc = nw;
					break;
				}
			}
		}
		if (nwc == null)
			return false;
		return nwc.bossMoveOn2;
	}

	public boolean NavalMoveTrapOn3(short mapid) {
		NavalWarfareController nwc = null;
		synchronized (navalList) {
			for (NavalWarfareController nw : navalList) {
				if (nw.mapid == mapid) {
					nwc = nw;
					break;
				}
			}
		}
		if (nwc == null)
			return false;
		return nwc.bossMoveOn3;
	}

	public void quit(L1Map map) {
		synchronized (mapList) {
			mapList.put(map, false);
		}
		NavalWarfareController nwc = null;
		synchronized (navalList) {
			for (NavalWarfareController nw : navalList) {
				if (nw.mapid == map.getId()) {
					nwc = nw;
					break;
				}
			}
			if (nwc != null)
				navalList.remove(nwc);
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
				int count = rs.getInt("orim_count");
				Connection con2 = null;
				PreparedStatement pstm2 = null;
				try {
					con2 = L1DatabaseFactory.getInstance().getConnection();
					pstm2 = con2
							.prepareStatement("UPDATE instance_dungeon_in SET orim_count=? WHERE charName=?");
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
							.prepareStatement("INSERT INTO instance_dungeon_in SET charName=?, orim_count=?");
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
