/* Eva Pack -http://eva.gg.gg
 * 본섭 리뉴얼된 안타라스 레이드 시스템
 */

package l1j.server.GameSystem.Antaras;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

import l1j.server.Config;
import l1j.server.GameSystem.Astar.World;
import l1j.server.server.ObjectIdFactory;
import l1j.server.server.datatables.TrapTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Location;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1FieldObjectInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1TrapInstance;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.trap.L1Trap;
import l1j.server.server.model.trap.L1WorldTraps;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.types.Point;
import l1j.server.server.utils.L1SpawnUtil;

public class AntarasRaidSystem {
	// private static Logger _log =
	// Logger.getLogger(AntarasRaidSystem.class.getName());

	private static AntarasRaidSystem _instance;
	private final Map<Integer, AntarasRaid> _list = new ConcurrentHashMap<Integer, AntarasRaid>();
	private int[] _mapid2 = { 0, 0, 0, 0, 0, 0 };
	private static Random random = new Random(System.nanoTime());

	public static AntarasRaidSystem getInstance() {
		if (_instance == null) {
			_instance = new AntarasRaidSystem();
		}
		return _instance;
	}

	static class antamsg implements Runnable {
		private int _mapid = 0;
		private int _type = 0;

		public antamsg(int mapid, int type) {
			_mapid = mapid;
			_type = type;
		}

		public void run() {
			try {
				switch (_type) {
				case 0:// 용레어 처음 입장시
					try {
						AntaTrapSpawn();
						AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(
								_mapid);
						ar.setAntaras(true);
						ArrayList<L1PcInstance> antapc = null;
						antapc = new ArrayList<L1PcInstance>();
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc.getMapId() == _mapid) {
								antapc.add(pc);
							}
						}
						/*
						 * 입장시멘트 안타라스 : 나의 잠을 깨우는자! 누구인가? 5초 크레이 : 안타라스! 너를 쫓아
						 * 이곳 칠흑의 어둠까지 왔다! 안타라스 : 가소롭군. 다시 한번 죽여주마, 크레이! 1차안타 스폰
						 */
						S_SystemMessage sm = new S_SystemMessage(
								"안타라스 : 나의 잠을 깨우는자! 누구인가?");
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
						S_SystemMessage sm1 = new S_SystemMessage(
								"크레이 : 안타라스! 너를 쫓아 이곳 칠흑의 어둠까지 왔다!");
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
							pc.sendPackets(sm1);
						}
						list = null;
						sm1 = null;
						Thread.sleep(5000);

						S_SystemMessage sm2 = new S_SystemMessage(
								"안타라스 : 가소롭군. 다시 한번 죽여주마, 크레이!");
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
						AntarasRaidSpawn.getInstance()
								.fillSpawnTable(_mapid, 5);

						antapc.clear();
					} catch (Exception exception) {
					}
					break;
				case 1:
					/*
					 * 실패했을때 멘트 안타라스 : 네 녀석의 무모함도 여기까지다..! 이 곳에서 종말을 맞이하라! 크레이 :
					 * 더 이상 소중한 용사들을 잃을 수는 없소. 마지막 남은 힘으로 이제 그대들을 소환하겠소.
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
								"안타라스 : 네 녀석의 무모함도 여기까지다..! 이 곳에서 종말을 맞이하라!");
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
						S_SystemMessage sm1 = new S_SystemMessage(
								"크레이 : 더 이상 소중한 용사들을 잃을 수는 없소. 마지막 남은 힘으로 이제 그대들을 소환하겠소.");
						S_SystemMessage sm2 = new S_SystemMessage(
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
							pc.sendPackets(sm1);
							pc.sendPackets(sm2);
						}
						list = null;
						sm1 = null;
						sm2 = null;
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
						AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(
								_mapid);
						removeanta(_mapid);
						ar.setAntaras(false);
						ar.clLairUser();
						antapc.clear();

						exitAR(_mapid);
					} catch (Exception exception) {
					}
					break;
				case 2:
					/*
					 * 1차 다이 멘트 안타라스 : 어리석은 자여! 나의 분노를 자극하는 구나. 크레이 : 용사들이여 그대들의
					 * 칼에 아덴의 운명이 걸려있다. 안타라스의 검은 숨결을 멈추게 할 자는 그대들 뿐이다! 안타라스 : 이런
					 * 조무래기들로 나를 이길 수 있을 것 같은가! 크하하하..
					 */
					try {
						ArrayList<L1PcInstance> antapc = null;
						antapc = new ArrayList<L1PcInstance>();
						S_SystemMessage sm = new S_SystemMessage(
								"안타라스 : 어리석은 자여! 나의 분노를 자극하는 구나.");
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc.getMapId() == _mapid) {
								antapc.add(pc);
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
							pc.sendPackets(sm);
						}
						list = null;
						sm = null;
						Thread.sleep(4000);
						S_SystemMessage sm1 = new S_SystemMessage(
								"크레이 : 용사들이여 그대들의 칼에 아덴의 운명이 걸려있다. 안타라스의 검은 숨결을 멈추게 할 자는 그대들 뿐이다!");
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
							pc.sendPackets(sm1);
						}
						list = null;
						sm1 = null;
						Thread.sleep(4000);
						S_SystemMessage sm2 = new S_SystemMessage(
								"안타라스 : 이런 조무래기들로 나를 이길 수 있을 것 같은가! 크하하하..");
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
								"안타라스 : 이제 맛있는 식사를 해볼까? 너희 피냄새가 나를 미치게 하는구나.");
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
						AntarasRaidSpawn.getInstance()
								.fillSpawnTable(_mapid, 6);
						antapc.clear();
					} catch (Exception exception) {
					}
					break;
				case 3:
					/*
					 * 2차 다이 멘트 크레이 : 우오오오옷! 피맺힌 원혼들의 외침이 들리지 않는가! 죽어랏! 안타라스 :
					 * 감히 나를 상대하려 하다니..그러고도 너희가 살길 바라느냐?
					 */
					try {
						ArrayList<L1PcInstance> antapc = null;
						antapc = new ArrayList<L1PcInstance>();
						S_SystemMessage sm = new S_SystemMessage(
								"크레이 : 우오오오옷! 피맺힌 원혼들의 외침이 들리지 않는가! 죽어랏!");
						for (L1PcInstance pc : L1World.getInstance()
								.getAllPlayers()) {
							if (pc.getMapId() == _mapid) {
								antapc.add(pc);
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
							pc.sendPackets(sm);
						}
						list = null;
						sm = null;
						Thread.sleep(4000);
						S_SystemMessage sm1 = new S_SystemMessage(
								"안타라스 : 감히 나를 상대하려 하다니..그러고도 너희가 살길 바라느냐?");
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
							pc.sendPackets(sm1);
						}
						list = null;
						sm1 = null;
						Thread.sleep(10000);

						S_SystemMessage sm2 = new S_SystemMessage(
								"안타라스 : 나의 분노가 하늘에 닿았다. 이제 곧 나의 아버지가 나설 것이다.");
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
						AntarasRaidSpawn.getInstance()
								.fillSpawnTable(_mapid, 7);
						antapc.clear();
					} catch (Exception exception) {
					}
					break;
				case 4:

					/*
					 * 2차 다이 멘트 안타라스 : 황혼의 저주가 그대들에게 있을 지어다! 실렌이여, 나의 어머니여, 나의
					 * 숨을.. 거두소서... 크레이 : 오오.. 최강의 용사임을 증명한 최고의 기사여! 엄청난 시련을
					 * 이겨내고 당신의 손에 안타라스의 피를 묻혔는가! 드디어 이 원한을 풀겠구나. 으하하하하!! 고맙다. 땅
					 * 위에 가장 강한 용사들이여! 난쟁이의 외침 : 웰던 마을에 숨겨진 용들의 땅으로 가는 문이 열렸습니다.
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
									(86400 * 2) * 1000);
							Timestamp deleteTime = new Timestamp(
									System.currentTimeMillis()
											+ (86400000 * Config.레이드시간));// 3일
							pc.sendPackets(new S_PacketBox(
									S_PacketBox.드래곤레이드버프, 86400 * 2), true);
							pc.getNetConnection().getAccount()
									.setDragonRaid(deleteTime);
							pc.getNetConnection().getAccount()
									.updateDragonRaidBuff();
						}
						list = null;
						// 혈흔 버프 투여
						Thread.sleep(3000);
						S_SystemMessage sm = new S_SystemMessage(
								"안타라스 : 황혼의 저주가 그대들에게 있을 지어다! 실렌이여, 나의 어머니여, 나의 숨을.. 거두소서...");
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
						S_SystemMessage sm1 = new S_SystemMessage(
								"크레이 : 오오.. 최강의 용사임을 증명한 최고의 기사여! 엄청난 시련을 이겨내고 당신의 손에 안타라스의 피를 묻혔는가! 드디어 이 원한을 풀겠구나. 으하하하하!! 고맙다. 땅 위에 가장 강한 용사들이여!");
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
							pc.sendPackets(sm1);
						}
						list = null;
						sm1 = null;
						Thread.sleep(2000);
						S_SystemMessage sm2 = new S_SystemMessage(
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
							pc.sendPackets(sm2);
						}
						list = null;
						sm2 = null;
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
						antapc.clear();
					} catch (Exception exception) {
					}
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}

		private static final int[] traplist = { 3, 98, 99, 100 };

		private void AntaTrapSpawn() {
			L1TrapInstance trap = null;
			L1TrapInstance base = null;
			L1Trap trapTemp = null;
			L1Location loc = null;
			Point rndPt = null;
			for (int trapId : traplist) {
				trapTemp = TrapTable.getInstance().getTemplate(trapId);
				loc = new L1Location();
				loc.setMap(_mapid);
				loc.setX(32784);
				loc.setY(32691);
				rndPt = new Point();
				rndPt.setX(20);
				rndPt.setY(20);
				int count = 18;
				int span = 0;
				int trapDoorId = 0;
				for (int i = 0; i < count; i++) {
					trap = new L1TrapInstance(ObjectIdFactory.getInstance()
							.nextId(), trapTemp, loc, rndPt, span, trapDoorId);
					trap.setRespawn(false);
					L1World.getInstance().addVisibleObject(trap);
					L1WorldTraps.getInstance().addTrap(trap);
					// System.out.println(trap.getX()
					// +" > "+trap.getY()+" > "+trap.getMapId());
				}
				base = new L1TrapInstance(ObjectIdFactory.getInstance()
						.nextId(), loc);
				base.setRespawn(false);
				L1World.getInstance().addVisibleObject(base);
				L1WorldTraps.getInstance().addBase(base);
			}
		}

		public void exitAR(int id) {
			for (L1FieldObjectInstance npc : L1World.getInstance()
					.getAllField()) {
				if (npc.moveMapId == id) {
					npc.deleteMe();
				}
			}
			AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(id);
			ar.clLairUser();
			ar.setAntaras(false);
			ar.setanta(null);
			ar.MiniBossReset();
			ar.threadOn = false;
		}

		public void removeanta(int id) {
			AntarasRaid ar = AntarasRaidSystem.getInstance().getAR(id);
			L1NpcInstance npc = ar.anta();
			if (npc != null && !npc.isDead()) {
				npc.deleteMe();
			}
		}
	}

	static class AntarasMsgTimer implements Runnable {
		private int _mapid = 0;
		private int _type = 0;

		public AntarasMsgTimer(int mapid, int type) {
			_mapid = mapid;
			_type = type;
		}

		public void run() {
			try {
				int idlist[] = { 코마윰, 코마신 };

				int x = 0, y = 0, x1 = 0, y1 = 0, x2 = 0, y2 = 0, x3 = 0, y3 = 0, x4 = 0, y4 = 0;

				int ranid = 0;
				int ranx = 0;
				int rany = 0;

				switch (_type) {
				case 1:
					x = 32663;
					y = 32792;
					x1 = 32613;
					y1 = 32815;
					x2 = 32631;
					y2 = 32845;
					x3 = 32671;
					y3 = 32865;
					x4 = 32623;
					y4 = 32916;
					break;
				case 2:
					x = 32919;
					y = 32600;
					x1 = 32870;
					y1 = 32624;
					x2 = 32886;
					y2 = 32655;
					x3 = 32925;
					y3 = 32679;
					x4 = 32881;
					y4 = 32713;
					break;
				case 3:
					x = 32919;
					y = 32792;
					x1 = 32868;
					y1 = 32819;
					x2 = 32886;
					y2 = 32846;
					x3 = 32924;
					y3 = 32871;
					x4 = 32881;
					y4 = 32907;
					break;
				case 4:
					x = 32791;
					y = 32792;
					x1 = 32759;
					y1 = 32844;
					x2 = 32740;
					y2 = 32817;
					x3 = 32798;
					y3 = 32869;
					x4 = 32747;
					y4 = 32903;
					break;
				}

				AntarasRaidSpawn.getInstance().fillSpawnTable(_mapid, _type); // 알
																				// 스폰
				AntarasRaid PT = AntarasRaidSystem.getInstance().getAR(_mapid);
				// L1Party PT =
				// AntarasRaidSystem.getInstance().getAR(_mapid).getParty(_type);
				S_ServerMessage smm = new S_ServerMessage(1588);
				for (L1PcInstance pc : PT.getMembers()) {
					if (pc.getMapId() != _mapid) {
						continue;
					}
					pc.sendPackets(smm);
				}
				smm = null;
				Thread.sleep(2000);
				S_ServerMessage smm1 = new S_ServerMessage(1589);
				for (L1PcInstance pc : PT.getMembers()) {
					if (pc.getMapId() != _mapid) {
						continue;
					}
					pc.sendPackets(smm1);
				}
				smm1 = null;
				Thread.sleep(2000);
				S_ServerMessage smm2 = new S_ServerMessage(1590);
				for (L1PcInstance pc : PT.getMembers()) {
					if (pc.getMapId() != _mapid) {
						continue;
					}
					pc.sendPackets(smm2);
				}
				smm2 = null;
				Thread.sleep(2000);
				S_ServerMessage smm3 = new S_ServerMessage(1591);
				for (L1PcInstance pc : PT.getMembers()) {
					if (pc.getMapId() != _mapid) {
						continue;
					}
					pc.sendPackets(smm3);
				}
				smm3 = null;
				for (int i = 0; i < 40; i++) {
					ranid = random.nextInt(2);
					ranx = random.nextInt(15);
					rany = random.nextInt(15);
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

	private static int 코마윰 = 4038001;
	private static int 코마신 = 4038002;

	public boolean startRaid(L1PcInstance pc) {
		checkAR();
		if (_list.size() >= 5) {
			pc.sendPackets(
					new S_SystemMessage("아덴월드 에 더이상 안타 포탈을 소환할 수 없습니다."), true);
			return false;
		}

		ArrayList<L1Object> list = L1World.getInstance().getVisibleObjects(pc,
				0);
		if (list.size() > 0) {
			pc.sendPackets(new S_SystemMessage("이 위치에 안타 포탈을 소환할 수 없습니다."),
					true);
			return false;
		}

		// pc.getInventory().consumeItem(430116, 1);
		int id = blankMapId();

		AntarasRaid ar = new AntarasRaid(id);

		L1WorldMap.getInstance().cloneMap(1005, id);
		World.cloneMap(1005, id);
		AntarasRaidSpawn.getInstance().fillSpawnTable(id, 0);

		L1NpcInstance npc = L1SpawnUtil.spawn2(pc.getX(), pc.getY(),
				pc.getMapId(), 4212015, 0, 7200 * 1000, id);
		L1FieldObjectInstance foi = (L1FieldObjectInstance) npc;
		foi.Potal_Open_pcid = pc.getId();

		L1SpawnUtil.spawn2(32680, 32744, (short) id, 4500101, 0, 0, id);

		L1SpawnUtil.spawn2(32703, 32670, (short) id, 4500102, 0, 0, id);

		_mapid2[id - 1005] = id;
		_list.put(id, ar);
		return true;
	}

	public void checkAR() {
		AntarasRaid ar = null;
		for (int i = 1005; i <= 1010; i++) {
			if (_list.containsKey(i)) {
				ar = _list.get(i);
				if (ar.getEndTime() <= System.currentTimeMillis()
						|| !ar.threadOn) {
					_list.remove(i);
					_mapid2[i - 1005] = 0;
				}
			}
		}
	}

	public AntarasRaid getAR(int id) {
		return _list.get(id);
	}

	/**
	 * 빈 맵 아이디를 가져온다
	 * 
	 * @return
	 */
	public int blankMapId() {
		int mapid = 1005;
		int a0 = 1005;
		int a1 = 1006;
		int a2 = 1007;
		int a3 = 1008;
		int a4 = 1009;
		int a5 = 1010;
		if (_list.size() >= 1) {
			for (int id : _mapid2) {
				if (id == 1005) {
					a0 = 0;
				}
				if (id == 1006) {
					a1 = 0;
				}
				if (id == 1007) {
					a2 = 0;
				}
				if (id == 1008) {
					a3 = 0;
				}
				if (id == 1009) {
					a4 = 0;
				}
				if (id == 1010) {
					a5 = 0;
				}
			}
		}
		if (a0 != 0) {
			System.out.println("안타레이드 맵 생성 : 1005");
			return a0;
		}
		if (a1 != 0) {
			System.out.println("안타레이드 맵 생성 : 1006");
			return a1;
		}
		if (a2 != 0) {
			System.out.println("안타레이드 맵 생성 : 1007");
			return a2;
		}
		if (a3 != 0) {
			System.out.println("안타레이드 맵 생성 : 1008");
			return a3;
		}
		if (a4 != 0) {
			System.out.println("안타레이드 맵 생성 : 1009");
			return a4;
		}
		if (a5 != 0) {
			System.out.println("안타레이드 맵 생성 : 1010");
			return a5;
		}
		return mapid;
	}

	public int countRaidPotal() {
		return _list.size();
	}
}
