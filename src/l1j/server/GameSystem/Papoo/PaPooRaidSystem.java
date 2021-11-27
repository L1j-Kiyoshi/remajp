package l1j.server.GameSystem.Papoo;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.GameSystem.Astar.World;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1MonsterInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.utils.L1SpawnUtil;

public class PaPooRaidSystem {

	private static PaPooRaidSystem _instance;
	private final Map<Integer, PaPooRaid> _list = new ConcurrentHashMap<Integer, PaPooRaid>();
	private int[] _mapid2 = { 0, 0, 0, 0, 0, 0 };
	private static Random random = new Random(System.nanoTime());

	public static PaPooRaidSystem getInstance() {
		if (_instance == null) {
			_instance = new PaPooRaidSystem();
		}
		return _instance;
	}

	static class papoomsg implements Runnable {
		private int _mapid = 0;
		private int _type = 0;

		public papoomsg(int mapid, int type) {
			_mapid = mapid;
			_type = type;
		}

		@Override
		public void run() {
			try {

				switch (_type) {
				case 0:
					/*
					 * 실패했을때 멘트 파푸리온 : 이제 물놀이는 끝이다. 너희들은 이제 내 저주를 피할 수 없다! 무녀 사엘
					 * : 이제 제가 할 수 있는 마지막 힘으로 용사님들을 소환하겠습니다. 이제 어쩔 수 없습니다.
					 */
					try {
						ArrayList<L1PcInstance> antapc = null;
						antapc = new ArrayList<L1PcInstance>();
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc.getMapId() == _mapid) {
								antapc.add(pc);
							}
						}
						S_SystemMessage sm = new S_SystemMessage(
								"파푸리온 : 이제 물놀이는 끝이다. 너희들은 이제 내 저주를 피할 수 없다!");
						L1PcInstance[] list = antapc
								.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm);
						}
						list = null;
						sm = null;
						Thread.sleep(5000);
						S_SystemMessage sm2 = new S_SystemMessage(
								"무녀 사엘 : 이제 제가 할 수 있는 마지막 힘으로 용사님들을 소환하겠습니다. 이제 어쩔 수 없습니다.");
						S_SystemMessage sm3 = new S_SystemMessage(
								"20분이 초과되어 레이드 실패! 5초후 기란마을로 이동 됩니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm2);
							pc.sendPackets(sm3);
						}
						list = null;
						sm2 = null;
						sm3 = null;
						Thread.sleep(5000);

						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							L1Teleport.teleport(pc, 33432, 32796, (short) 4, 5,
									true);
						}
						list = null;
						PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
								_mapid);
						removeanta(_mapid);
						ar.setPapoo(false);
						ar.clLairUser();
						if (antapc.size() > 0)
							antapc.clear();
						antapc = null;
					} catch (Exception exception) {
					}
					break;
				case 1:// 파푸 입장시
					try {
						for (L1Object npc : L1World.getInstance().getObject()) {
							if (npc instanceof L1MonsterInstance) {
								if (npc.getMapId() == _mapid) {
									L1MonsterInstance _npc = (L1MonsterInstance) npc;
									if (!_npc.isDead()
											&& (_npc.getNpcId() == 4039000
													|| _npc.getNpcId() == 4039006 || _npc
													.getNpcId() == 4039007)) {
										return;
									}
								}
							}
						}
						PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(
								_mapid);
						ar.setPapoo(true);
						ArrayList<L1PcInstance> antapc = null;
						antapc = new ArrayList<L1PcInstance>();
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc.getMapId() == _mapid) {
								antapc.add(pc);
							}
						}
						/*
						 * 입장시멘트 감히 나의 영역에 들어오다니… 용기가 가상 하구나..
						 * 
						 * 이 비열한 파푸리온! 이제 나를 속인 댓가를 치루게 될 것이다! (무녀 사엘)
						 * 
						 * 봉인을 풀 때 네가 큰 도움이 되었지만.. 나에게 두 번의 자비는 없다.
						 * 
						 * 그때는 네 녀석이 내 뼈 속까지 저주를 내렸지만.. 지금은 다르다! (무녀 사엘) 1차안타 스폰
						 */
						S_SystemMessage sm = new S_SystemMessage(
								"파푸리온 : 감히 나의 영역에 들어오다니...용기가 가상하구나..");
						L1PcInstance[] list = antapc
								.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm);
						}
						list = null;
						sm = null;
						Thread.sleep(5000);
						S_SystemMessage sm2 = new S_SystemMessage(
								"무녀 사엘 : 이 비열한 파푸리온! 이제 나를 속인 댓가를 치루게 될 것이다!");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm2);
						}
						list = null;
						sm2 = null;
						Thread.sleep(5000);
						S_SystemMessage sm3 = new S_SystemMessage(
								"파푸리온 : 봉인을 풀 때 네가 큰 도움이 되었지만..나에게 두 번의 자비는 없다..");
						for (L1PcInstance pc : antapc) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm3);
						}
						sm3 = null;
						Thread.sleep(5000);
						S_SystemMessage sm4 = new S_SystemMessage(
								"무녀 사엘 : 그때는 네 녀석이 내 뼈 속까지 저주를 내렸지만..지금은 다르다!");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm4);
						}
						list = null;
						sm4 = null;
						PapooRaidSpawn.getInstance().fillSpawnTable(_mapid, 1);
						if (antapc.size() > 0)
							antapc.clear();
						antapc = null;
					} catch (Exception exception) {
					}
					break;

				case 2:
					/*
					 * 1차 다이 멘트 파푸리온 : 가소롭구나! 저들이 너와 함께 이승을 떠돌게 될 나의 제물들인 것이냐!
					 * 무녀 사엘 : 용사들이여! 저 사악한 파푸리온을 물리치고 에바 왕국에 내려진 피의 저주를 부디 풀어
					 * 주소서! 파푸리온 : 놀잇감으로는 충분하구나! 흐흐흐...
					 */
					try {
						for (L1Object npc : L1World.getInstance().getObject()) {
							if (npc instanceof L1MonsterInstance) {
								if (npc.getMapId() == _mapid) {
									L1MonsterInstance _npc = (L1MonsterInstance) npc;
									if (!_npc.isDead()
											&& (_npc.getNpcId() == 4039000
													|| _npc.getNpcId() == 4039006 || _npc
													.getNpcId() == 4039007)) {
										return;
									}
								}
							}
						}
						ArrayList<L1PcInstance> antapc = null;
						antapc = new ArrayList<L1PcInstance>();
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc.getMapId() == _mapid) {
								antapc.add(pc);
							}
						}

						S_SystemMessage sm = new S_SystemMessage(
								"파푸리온 : 가소롭구나! 저들이 너와 함께 이승을 떠돌게 될 나의 제물들인 것이냐!");
						L1PcInstance[] list = antapc
								.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm);
						}
						list = null;
						sm = null;
						Thread.sleep(4000);
						S_SystemMessage sm2 = new S_SystemMessage(
								"무녀 사엘 : 용사들이여! 저 사악한 파푸리온을 물리치고 에바 왕국에 내려진 피의 저주를 부디 풀어 주소서!");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm2);
						}
						list = null;
						sm2 = null;
						Thread.sleep(4000);
						S_SystemMessage sm3 = new S_SystemMessage(
								"파푸리온 : 놀잇감으로는 충분하구나! 흐흐흐...");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm3);
						}
						list = null;
						sm3 = null;
						Thread.sleep(10000);
						S_SystemMessage sm4 = new S_SystemMessage(
								"파푸리온 : 뼈 속까지 파고드는 두려움이 무엇인지 이 몸이 알게 해주마!");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm4);
						}
						list = null;
						sm4 = null;
						Thread.sleep(10000);

						PapooRaidSpawn.getInstance().fillSpawnTable(_mapid, 2);
						if (antapc.size() > 0)
							antapc.clear();
						antapc = null;
					} catch (Exception exception) {

					}
					break;
				case 3:
					/*
					 * 2차 다이 멘트
					 * 
					 * 무녀 사엘 : 이제 파푸리온의 힘이 많이 떨어진것 같습니다! 용사들이어 조금 더 힘을 내주소서!
					 * 파푸리온 : 네 놈들이 희망이라 부르는 것이, 단지 헛된 몽상이었음을 알게 해주마! 파푸리온 : 사엘과
					 * 함께 한것을 후회하게 될 것이다! 어리석은 존재들이여...
					 */
					try {
						for (L1Object npc : L1World.getInstance().getObject()) {
							if (npc instanceof L1MonsterInstance) {
								if (npc.getMapId() == _mapid) {
									L1MonsterInstance _npc = (L1MonsterInstance) npc;
									if (!_npc.isDead()
											&& (_npc.getNpcId() == 4039000
													|| _npc.getNpcId() == 4039006 || _npc
													.getNpcId() == 4039007)) {
										return;
									}
								}
							}
						}
						ArrayList<L1PcInstance> antapc = null;
						antapc = new ArrayList<L1PcInstance>();
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc.getMapId() == _mapid) {
								antapc.add(pc);
							}
						}
						S_SystemMessage sm = new S_SystemMessage(
								"무녀 사엘 : 이제 파푸리온의 힘이 많이 떨어진것 같습니다! 용사들이어 조금 더 힘을 내주소서!");
						L1PcInstance[] list = antapc
								.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm);
						}
						list = null;
						sm = null;
						Thread.sleep(4000);
						S_SystemMessage sm2 = new S_SystemMessage(
								"파푸리온 : 네 놈들이 희망이라 부르는 것이, 단지 헛된 몽상이었음을 알게 해주마!");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm2);
						}
						list = null;
						sm2 = null;
						Thread.sleep(10000);

						S_SystemMessage sm3 = new S_SystemMessage(
								"파푸리온 : 사엘과 함께 한것을 후회하게 될 것이다! 어리석은 존재들이여...");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm3);
						}
						list = null;
						sm3 = null;
						Thread.sleep(10000);
						PapooRaidSpawn.getInstance().fillSpawnTable(_mapid, 3);
						if (antapc.size() > 0)
							antapc.clear();
						antapc = null;
					} catch (Exception exception) {
					}
					break;
				case 4:

					/*
					 * 3차 다이 멘트 파푸리온 : 사엘..네 녀석이..어떻게...나의 어머니..실렌이시여 나의
					 * 숨을..거두소서... 무녀 사엘 : 감사합니다..당신들은 역시 아덴 최고의 용사들입니다. 드디어..에바
					 * 왕국의 오랜 저주가 풀릴 수 있을 것 같습니다.
					 */
					try {
						ArrayList<L1PcInstance> antapc = null;
						antapc = new ArrayList<L1PcInstance>();
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc.getNetConnection() != null) {
								if (pc.getMapId() == _mapid) {
									antapc.add(pc);
								}
							}
						}
						L1PcInstance[] list = antapc
								.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(new S_SkillSound(pc.getId(), 7783),
									true);
							Broadcaster.broadcastPacket(pc,
									new S_SkillSound(pc.getId(), 7783), true);
							pc.getSkillEffectTimerSet().setSkillEffect(
									L1SkillId.DRAGONRAID_BUFF,
									(86400 * 1) * 1000);
							Timestamp deleteTime = new Timestamp(
									System.currentTimeMillis() + (86400000 * Config.레이드시간));// 3일
							pc.sendPackets(new S_PacketBox(
									S_PacketBox.드래곤레이드버프, 86400 * 1), true);
							pc.getNetConnection().getAccount()
									.setDragonRaid(deleteTime);
							pc.getNetConnection().getAccount()
									.updateDragonRaidBuff();
							// pc.sendPackets(new
							// S_SystemMessage("드래곤 레이드 마법으로 인해 "+C_SelectCharacter.ss.format(pc.getNetConnection().getAccount().getDragonRaid())+" 이후에 드래곤 포탈 입장이 가능합니다."),
							// true);

							/*
							 * l1skilluse = new L1SkillUse();
							 * l1skilluse.handleCommands(pc,
							 * L1SkillId.DRAGONBLOOD_P, pc.getId(), pc.getX(),
							 * pc.getY(), null, 0, L1SkillUse.TYPE_GMBUFF);
							 * S_PacketBox pb = new
							 * S_PacketBox(S_PacketBox.DRAGONBLOOD, 85, 4320);
							 * pc.sendPackets(pb);pb = null; Timestamp
							 * deleteTime = null; deleteTime = new
							 * Timestamp(System.currentTimeMillis() +
							 * 259200000);// 3일 pc.setpaTime(deleteTime);
							 */
						}
						list = null;
						// 혈흔 버프 투여
						Thread.sleep(3000);
						S_SystemMessage sm = new S_SystemMessage(
								"파푸리온 : 사엘..네 녀석이..어떻게...나의 어머니..실렌이시여 나의 숨을..거두소서...");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm);
						}
						list = null;
						sm = null;
						Thread.sleep(3000);
						S_SystemMessage sm2 = new S_SystemMessage(
								"무녀 사엘 : 감사합니다..당신들은 역시 아덴 최고의 용사들입니다. 드디어..에바 왕국의 오랜 저주가 풀릴 수 있을 것 같습니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm2);
						}
						list = null;
						sm2 = null;
						Thread.sleep(2000);
						S_SystemMessage sm3 = new S_SystemMessage(
								"난쟁이의 외침 : 어서 이 곳을 떠나세요. 곧 문이 닫힐 것입니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm3);
						}
						list = null;
						sm3 = null;
						/*
						 * if(GameList.get용땅() == false){
						 * L1World.getInstance().broadcastServerMessage
						 * ("난쟁이의 외침 : 웰던 마을에 숨겨진 용들의 땅으로 가는 문이 열렸습니다.");
						 * L1SpawnUtil.spawn2( 33726, 32506, (short)4 , 4212013,
						 * 0, 1000*60*60*12 , 0); GameList.set용땅(true); }else{
						 * L1World.getInstance().broadcastServerMessage(
						 * "난쟁이의 외침 : 숨겨진 용들의 땅으로 가는 문이 이미 웰던 마을에 열려 있습니다."); }
						 */

						Thread.sleep(2000);
						// 아이템 분배

						Thread.sleep(10000);
						S_SystemMessage sm6 = new S_SystemMessage(
								"시스템 메시지 : 10초 후에 텔레포트 합니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm6);
						}
						list = null;
						sm6 = null;
						Thread.sleep(5000);
						S_SystemMessage sm7 = new S_SystemMessage(
								"시스템 메시지 : 5초 후에 텔레포트 합니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm7);
						}
						list = null;
						sm7 = null;
						Thread.sleep(1000);
						S_SystemMessage sm8 = new S_SystemMessage(
								"시스템 메시지 : 4초 후에 텔레포트 합니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm8);
						}
						list = null;
						sm8 = null;
						Thread.sleep(1000);
						S_SystemMessage sm9 = new S_SystemMessage(
								"시스템 메시지 : 3초 후에 텔레포트 합니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm9);
						}
						list = null;
						sm9 = null;
						Thread.sleep(1000);
						S_SystemMessage sm10 = new S_SystemMessage(
								"시스템 메시지 : 2초 후에 텔레포트 합니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm10);
						}
						list = null;
						sm10 = null;
						Thread.sleep(1000);
						S_SystemMessage sm11 = new S_SystemMessage(
								"시스템 메시지 : 1초 후에 텔레포트 합니다.");
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							pc.sendPackets(sm11);
						}
						list = null;
						sm11 = null;
						Thread.sleep(1000);
						list = antapc.toArray(new L1PcInstance[antapc.size()]);
						for (L1PcInstance pc : list) {
							if (pc.getMapId() != _mapid) {
								antapc.remove(pc);
								continue;
							}
							if (L1World.getInstance().getPlayer(pc.getName()) == null
									|| pc.getNetConnection() == null) {
								antapc.remove(pc);
								continue;
							}
							L1Teleport.teleport(pc, 33718, 32506, (short) 4, 5,
									true);
						}
						list = null;
						exitAR(_mapid);
						if (antapc.size() > 0)
							antapc.clear();
						antapc = null;
					} catch (Exception exception) {
					}
					break;

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void exitAR(int id) {
			for (L1FieldObjectInstance npc : L1World.getInstance()
					.getAllField()) {
				if (npc.moveMapId == id) {
					npc.deleteMe();
				}
			}
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(id);
			ar.clLairUser();
			ar.setPapoo(false);
			ar.setPaPoo(null);
			ar.threadOn = false;
		}

		public void removeanta(int id) {
			PaPooRaid ar = PaPooRaidSystem.getInstance().getAR(id);
			L1NpcInstance npc = ar.PaPoo();
			if (npc != null && !npc.isDead()) {
				npc.deleteMe();
			}
		}
	}

	static class FafulionMsgTimer implements Runnable {
		private int _mapid = 0;
		private int _type = 0;

		public FafulionMsgTimer(int mapid, int type) {
			_mapid = mapid;
			_type = type;
		}

		@Override
		public void run() {
			try {
				int idlist[] = { 알로퍼스메가, 알로퍼스비아 };

				int x = 0, y = 0, x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0, x4 = 0, y4 = 0;

				int ranid = 0;
				int swich = 0;
				int ranx = 0;
				int rany = 0;

				switch (_type) {
				case 1:
					x = 32743;
					y = 32862; // 1번방
					x1 = 32752;
					y1 = 32809; // 2번방

					x2 = 32809;
					y2 = 32813;
					x3 = 32811;
					y3 = 32854;
					x4 = 32860;
					y4 = 32858;// 3번방
					break;
				case 2:
					x = 32743;
					y = 32733;
					x1 = 32752;
					y1 = 32680; // 2번방

					x2 = 32809;
					y2 = 32685;
					x3 = 32811;
					y3 = 32724;
					x4 = 32860;
					y4 = 32728;// 3번방
					break;
				case 3:
					x = 32743;
					y = 32605;
					x1 = 32752;
					y1 = 32552; // 2번방

					x2 = 32809;
					y2 = 32557;
					x3 = 32811;
					y3 = 32596;
					x4 = 32860;
					y4 = 32598;// 3번방
					break;
				case 4:
					x = 32935;
					y = 32605;
					x1 = 32943;
					y1 = 32552; // 2번방

					x2 = 32999;
					y2 = 32557;
					x3 = 33000;
					y3 = 32596;
					x4 = 33050;
					y4 = 32598;// 3번방
					break;
				}
				/*
				 * 1679 베아사무의 외침 : 곧 알로퍼스들이 몰려올 것입니다. 만반의 준비를 하세요. 베아사무의 외침 :
				 * 녀석들은 높은 지능과 유연한 몸을 가지고 있으며, 촉수가 많은 녀석을 특히 조심하세요. 베아사무의 외침 :
				 * 알로퍼스들은 파푸리온의 안식을 지키기 위해 할파스가 파견한 수하들 입니다. 카임사무의 외침 : 파푸리온의 검은
				 * 숨결을 멈추게 한 용사들이 탄생 하였습니다.!!
				 */
				PaPooRaid PT = PaPooRaidSystem.getInstance().getAR(_mapid);
				// L1Party PT =
				// PaPooRaidSystem.getInstance().getAR(_mapid).getParty(_type);
				S_ServerMessage sm = new S_ServerMessage(1679);
				for (L1PcInstance pc : PT.getMembers()) {
					if (pc.getMapId() != _mapid) {
						continue;
					}
					pc.sendPackets(sm);
				}
				Thread.sleep(2000);
				sm = new S_ServerMessage(1680);
				for (L1PcInstance pc : PT.getMembers()) {
					if (pc.getMapId() != _mapid) {
						continue;
					}
					pc.sendPackets(sm);
				}

				Thread.sleep(2000);
				sm = new S_ServerMessage(1681);
				for (L1PcInstance pc : PT.getMembers()) {
					if (pc.getMapId() != _mapid) {
						continue;
					}
					pc.sendPackets(sm);
				}
				sm = null;
				Thread.sleep(2000);

				for (int i = 0; i < 40; i++) {
					ranid = random.nextInt(2);
					ranx = random.nextInt(15);
					rany = random.nextInt(15);
					swich = random.nextInt(2);
					if (swich == 0) {
						ranx *= -1;
						rany *= -1;
					}
					// 1번방 스폰
					L1SpawnUtil.spawn2(x + ranx, y + rany, (short) _mapid,
							idlist[ranid], 5, 0, 0);
					// 2번방 스폰
					L1SpawnUtil.spawn2(x1 + ranx, y1 + rany, (short) _mapid,
							idlist[ranid], 5, 0, 0);
					L1SpawnUtil.spawn2(x2 + ranx, y2 + rany, (short) _mapid,
							idlist[ranid], 5, 0, 0);
					// 3번방 스폰
					L1SpawnUtil.spawn2(x3 + ranx, y3 + rany, (short) _mapid,
							idlist[ranid], 5, 0, 0);
					L1SpawnUtil.spawn2(x4 + ranx, y4 + rany, (short) _mapid,
							idlist[ranid], 5, 0, 0);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static int 알로퍼스메가 = 4039012;
	private static int 알로퍼스비아 = 4039013;

	public boolean startRaid(L1PcInstance pc) {
		checkAR();
		if (_list.size() >= 15) {
			S_SystemMessage sm = new S_SystemMessage(
					"아덴월드 에 더이상 파푸 포탈을 소환할 수 없습니다.");
			pc.sendPackets(sm);
			sm = null;
			return false;
		}

		ArrayList<L1Object> list = L1World.getInstance().getVisibleObjects(pc,
				0);
		if (list.size() > 0) {
			pc.sendPackets(new S_SystemMessage("이 위치에 파푸 포탈을 소환할 수 없습니다."),
					true);
			return false;
		}
		// pc.getInventory().consumeItem(430116, 1);
		int id = blankMapId();

		PaPooRaid ar = new PaPooRaid(id);

		L1WorldMap.getInstance().cloneMap(1011, id);
		World.cloneMap(1011, id);

		PapooRaidSpawn.getInstance().fillSpawnTable(id, 0);

		L1NpcInstance npc = L1SpawnUtil.spawn2(pc.getX(), pc.getY(),
				pc.getMapId(), 4212016, 0, 7200 * 1000, id);
		L1FieldObjectInstance foi = (L1FieldObjectInstance) npc;
		foi.Potal_Open_pcid = pc.getId();

		L1SpawnUtil.spawn2(32921, 32727, (short) id, 4500103, 0, 0, id);

		L1SpawnUtil.spawn2(32942, 32671, (short) id, 4500107, 0, 0, id);
		_mapid2[id - 1011] = id;
		_list.put(id, ar);
		return true;
	}

	public void checkAR() {
		PaPooRaid ar = null;
		for (int i = 1011; i <= 1016; i++) {
			if (_list.containsKey(i)) {
				ar = _list.get(i);
				if (ar.getEndTime() <= System.currentTimeMillis()
						|| !ar.threadOn) {
					_list.remove(i);
					_mapid2[i - 1011] = 0;
				}
			}
		}
	}

	public PaPooRaid getAR(int id) {
		return _list.get(id);
	}

	/**
	 * 빈 맵 아이디를 가져온다
	 * 
	 * @return
	 */
	public int blankMapId() {
		int mapid = 1011;
		int a0 = 1011;
		int a1 = 1012;
		int a2 = 1013;
		int a3 = 1014;
		int a4 = 1015;
		int a5 = 1016;
		if (_list.size() >= 1) {
			for (int id : _mapid2) {
				if (id == 1011) {
					a0 = 0;
				}
				if (id == 1012) {
					a1 = 0;
				}
				if (id == 1013) {
					a2 = 0;
				}
				if (id == 1014) {
					a3 = 0;
				}
				if (id == 1015) {
					a4 = 0;
				}
				if (id == 1016) {
					a5 = 0;
				}
			}
		}
		if (a0 != 0) {
			return a0;
		}
		if (a1 != 0) {
			return a1;
		}
		if (a2 != 0) {
			return a2;
		}
		if (a3 != 0) {
			return a3;
		}
		if (a4 != 0) {
			return a4;
		}
		if (a5 != 0) {
			return a5;
		}
		return mapid;
	}

	public int countRaidPotal() {
		return _list.size();
	}
}
