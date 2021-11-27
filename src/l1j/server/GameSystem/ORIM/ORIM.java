package l1j.server.GameSystem.ORIM;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.GameList;
import l1j.server.GameSystem.Astar.World;
import l1j.server.GameSystem.NavalWarfare.NavalWarfareController;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.SQLUtil;

public class ORIM {

	public String[] _pcname;
	public L1NpcInstance _bdeath;
	public L1NpcInstance _wdeath;
	public L1NpcInstance _v;
	private int _mapnum;
	private ArrayList<L1NpcInstance> MonList = new ArrayList<L1NpcInstance>();
	public int _bstcon = -1;
	public int _wstcon = -1;
	public int _vstcon = -1;

	public NavalWarfareController _NFC = null;

	private FastTable<L1PcInstance> _member = null;

	public void addMember(L1PcInstance pc) {
		_member.add(pc);
	}

	public int getMembersCount() {
		return _member.size();
	}

	public void removeMember(L1PcInstance pc) {
		_member.remove(pc);
	}

	public void clearMember() {
		_member.clear();
	}

	public boolean isMember(L1PcInstance pc) {
		return _member.contains(pc);
	}

	public L1PcInstance[] getMemberArray() {
		return _member.toArray(new L1PcInstance[getMembersCount()]);
	}

	private static Random _rnd = new Random(System.nanoTime());

	public ORIM() {
	};

	public int Start(L1PcInstance pc, int type) {
		synchronized (GameList.ORList) {
			try {
				int mapid = GameList.getORIMMapId();
				if (mapid == 0) {
					S_SystemMessage sm = new S_SystemMessage(
							"모든 맵이 사용중입니다 잠시후 다시 이용해주세요.");
					pc.sendPackets(sm, true);
					return 0;
				}

				GameList.addORIM(mapid, this);

				if (!World.get_map(mapid)) {
					L1WorldMap.getInstance().cloneMap(9101, mapid);
				}
				int i = 0;
				_pcname = new String[pc.getParty().getNumOfMembers()];
				for (L1PcInstance player : pc.getParty().getMembers()) {
					_pcname[i] = player.getName();
					i++;
				}
				_mapnum = mapid;

				// ORIMSpawn.getInstance().Spawn(pc, mapid, 0);

				return mapid;
			} catch (Exception e) {
				e.printStackTrace();
				return 0;
			}
		}
	}

	public void systemgo(L1PcInstance pc, int mapid, boolean lv) {
		NavalWarfareController nwfc = new NavalWarfareController(pc, mapid, lv);

		for (L1PcInstance Ppc : pc.getParty().getMembers()) {
			if (Ppc != null) {
				L1Teleport.teleport(Ppc, 32792 + _rnd.nextInt(5), 32801 + _rnd
						.nextInt(5), (short) mapid, Ppc.getMoveState()
						.getHeading(), true);
				nwfc.addMember(Ppc);
				databaseInsert(Ppc);
			}
		}

		/*
		 * L1Teleport.teleport(pc, 32792+_rnd.nextInt(5), 32801+_rnd.nextInt(5),
		 * (short)mapid, pc.getMoveState().getHeading(), true);
		 * nwfc.addMember(pc); databaseInsert(pc);
		 */

		_NFC = nwfc;
		GeneralThreadPool.getInstance().schedule(nwfc, 15000);
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

	/*
	 * 발라카스:누가 나를 깨우는가?18869 데스나이트:발라카스! 드디어 너를 만나게 되는구나..18870 발라카스:나의 잠을 깨운
	 * 댓가는..나의 노예가 되어 평생 갚게 되리라..18871 데스나이트:그런 말은 내가 패배했을 때 해도 늦지 않는다.18872
	 * 발라카스:크크..자신감이 넘치는구나..18873 발라카스:죽어라 인간!18874
	 */

	private void MSG(L1NpcInstance npc, int id, String msg, int x, int y) {
		Broadcaster.broadcastPacket(npc, new S_NpcChatPacket(id, msg, x, y),
				true);
	}

	public void Reset() {
		try {
			// System.out.println("오림 인던 맵 삭제 : "+_mapnum);
			for (L1NpcInstance mon : MonList) {
				if (mon == null || mon._destroyed || mon.isDead()) {
					continue;
				}
				mon.deleteMe();
			}
			Object_Delete();
			if (MonList.size() > 0)
				MonList.clear();

			L1V1Map m = (L1V1Map) L1WorldMap.getInstance().getMap(
					(short) _mapnum);
			m.reset((L1V1Map) L1WorldMap.getInstance().getMap((short) 9101));
			// World.resetMap(2101, _mapnum);

			GameList.removeORIM(_mapnum);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void AddMon(L1NpcInstance npc) {
		MonList.add(npc);
	}

	private void Object_Delete() {
		for (L1Object ob : L1World.getInstance().getVisibleObjects(_mapnum)
				.values()) {
			if (ob == null || ob instanceof L1DollInstance
					|| ob instanceof L1SummonInstance
					|| ob instanceof L1PetInstance)
				continue;
			if (ob instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) ob;
				if (npc._destroyed || npc.isDead())
					continue;
				npc.deleteMe();
			}
		}
		for (L1ItemInstance obj : L1World.getInstance().getAllItem()) {
			if (obj.getMapId() != _mapnum)
				continue;
			L1Inventory groundInventory = L1World.getInstance().getInventory(
					obj.getX(), obj.getY(), obj.getMapId());
			groundInventory.removeItem(obj);
		}
	}

}