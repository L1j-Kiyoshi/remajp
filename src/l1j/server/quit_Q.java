package l1j.server;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import l1j.server.GameSystem.GhostHouse;
import l1j.server.GameSystem.PetRacing;
import l1j.server.GameSystem.Dungeon.DungeonSystem;
import l1j.server.GameSystem.MiniGame.DeathMatch;
import l1j.server.Warehouse.ClanWarehouse;
import l1j.server.Warehouse.WarehouseManager;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.datatables.CharBuffTable;
import l1j.server.server.datatables.MonsterBookTable;
import l1j.server.server.datatables.PetTable;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1CastleLocation;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.L1Trade;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1FollowerInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.Instance.L1SummonInstance;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SystemMessage;
import manager.LinAllManager;
import server.LoginController;

public class quit_Q implements Runnable {
	private long time = 0;
	private final Queue<L1PcInstance> _queue;

	public quit_Q() {
		_queue = new ConcurrentLinkedQueue<L1PcInstance>();
		GeneralThreadPool.getInstance().execute(this);
	}

	public void requestWork(L1PcInstance name) {
		_queue.offer(name);
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(10L);
				synchronized (this){
					L1PcInstance pc = _queue.peek();
					if (_queue.size() > 100) {
						if (System.currentTimeMillis() > time) {
							System.out.println("종료 큐 100개 이상 : " + _queue.size());
							time = System.currentTimeMillis() + (1000 * 10);
						}
					}

					if (pc == null) {
						continue;
					}
					
					if (pc.isPrivateShop()) {
						pc.상점아이템삭제(pc.getId());
					}
					

					LinAllManager.getInstance().LogLogOutAppend(pc.getName(), pc.getNetConnection().getHostname());

					// System.out.println("123 :" +pc.getName());

					try {
						if (Config.접속채팅모니터() > 0) {
							for (L1PcInstance gm : Config.toArray접속채팅모니터()) {
								if (gm.getNetConnection() == null) {
									Config.remove접속(gm);
									continue;
								}
								
								if (gm == pc) {
									continue;
								}
								
								gm.sendPackets(new S_SystemMessage("\\fY[" + pc.getName() + "] (종료) / 계정:" + pc.getAccountName()));
							}
						}

						if (pc.isGm()) Config.remove전체(pc);

						pc.set_delete(true); // 허공버그 픽스
						pc.setadFeature(1);
						pc.setDeathMatch(false);
						pc.setHaunted(false);
						pc.setPetRacing(false);

						// 사망하고 있으면(자) 거리에 되돌려, 공복 상태로 한다
						if (pc.isDead()) {
							int[] loc = Getback.GetBack_Location(pc, true);
							pc.setX(loc[0]);
							pc.setY(loc[1]);
							pc.setMap((short) loc[2]);
							pc.setCurrentHp(pc.getLevel());
							pc.set_food(39); // 10%
							loc = null;
						}
						
						// 자신의 성 근처에서 종료시 내성으로 위치 셋팅
						if (pc.getClan() != null && pc.getClan().getCastleId() > 0) {
							if (L1CastleLocation.checkInWarArea(pc.getClan().getCastleId(), pc)) {
								int[] loc = L1CastleLocation.getCastleLoc(pc.getClan().getCastleId());
								pc.setX(loc[0]);
								pc.setY(loc[1]);
								pc.setMap((short) loc[2]);
								loc = null;
							}
						}

						if (pc.isGhost()) {
							pc.setX(pc._ghostSaveLocX);
							pc.setY(pc._ghostSaveLocY);
							pc.setMap((short) pc._ghostSaveMapId);
							pc.getMoveState().setHeading(pc._ghostSaveHeading);
						}

						// 혈맹창고 사용도중 팅기거나 종료할경우 혈맹창고 사용중 해제(쿠우)
						ClanWarehouse clanWarehouse = null;
						L1Clan clan = L1World.getInstance().getClan(pc.getClanname());
						if (clan != null) {
							clanWarehouse = WarehouseManager.getInstance().getClanWarehouse(clan.getClanName());
							clan.removeOnlineClanMember(pc.getName());
						}
						
						if (clanWarehouse != null) {
							clanWarehouse.unlock(pc.getId());
						}

						// 트레이드를 중지한다
						if (pc.getTradeID() != 0) { // 트레이드중
							L1Trade trade = new L1Trade();
							trade.TradeCancel(pc);
						}

						// 결투중
						if (pc.getFightId() != 0) {
							pc.setFightId(0);
							L1PcInstance fightPc = (L1PcInstance) L1World.getInstance().findObject(pc.getFightId());
							if (fightPc != null) {
								fightPc.setFightId(0);
								fightPc.sendPackets(new S_PacketBox(S_PacketBox.MSG_DUEL, 0, 0));
							}
						}

						if (DeathMatch.getInstance().isEnterMember(pc)) {
							DeathMatch.getInstance().removeEnterMember(pc);
						}
						
						if (GhostHouse.getInstance().isEnterMember(pc)) {
							GhostHouse.getInstance().removeEnterMember(pc);
						}
						
						if (PetRacing.getInstance().isEnterMember(pc)) {
							PetRacing.getInstance().removeEnterMember(pc);
						}

						/** 펫이 소환중인지 체크해서 정리 펫이 만약 죽었다면 그냥 나두도록함 */
						if (pc.getPetList() != null && pc.getPetListSize() > 0) {
							for (Object petObject : pc.getPetList()) {
								if (petObject == null) continue;
								if (petObject instanceof L1PetInstance) {
									L1PetInstance pet = (L1PetInstance) petObject;
									PetTable.UpDatePet(pet);
									pc.removePet((L1NpcInstance) pet);
									if(!pet.isDead()) pet.deletePet();
								} else if (petObject instanceof L1SummonInstance) {
									L1SummonInstance sunm = (L1SummonInstance) petObject;
									sunm.dropItem();
									pc.removePet((L1NpcInstance) sunm);
									sunm.deleteMe();
								}
							}
						}
						
						// 마법 인형을 월드 맵상으로부터 지운다
						if (pc.getDollList() != null && pc.getDollListSize() > 0) {
							for (L1DollInstance doll : pc.getDollList()) {
								if (doll != null) {
									doll.deleteDoll();
								}
							}
						}

						if (pc.getFollowerList() != null && pc.getFollowerList().size() > 0) {
							L1FollowerInstance follower = null;
							for (Object followerObject : pc.getFollowerList().values()) {
								if (followerObject == null)continue;
								follower = (L1FollowerInstance) followerObject;
								follower.setParalyzed(true);
								follower.spawn(follower.getNpcTemplate().get_npcId(), follower.getX(), follower.getY(), follower.getMoveState().getHeading(), follower.getMapId());
								follower.deleteMe();
							}
						}
						
						// 캐릭터 적용 중 버프 목록 DB 삭제
						CharBuffTable.DeleteBuff(pc);
						// 로그아웃 시 현재 적용 중인 버프 목록 DB에 저장
						CharBuffTable.SaveBuff(pc);
						MonsterBookTable.getInstace().saveMonsterBookList(pc.getId());
						pc.getSkillEffectTimerSet().clearRemoveSkillEffectTimer();

						for (L1ItemInstance item : pc.getInventory().getItems()) {
							if (item != null && item.getCount() <= 0) {
								pc.getInventory().deleteItem(item);
							}
						}
						
						pc.setLogOutTime();
						pc.setOnlineStatus(0);
				
						/** 케릭터 정보 저장 */
						pc.save();
						/** 케릭터 인벤 저장 */
						pc.saveInventory();
						
						/** 케릭터 디비 커넥션이 놀이 아니라면 */
						if(pc.getNetConnection() != null){
							/** 인던 정보가 있는지 체크해서 인던 삭제 시키도록 */
							DungeonSystem.isDungeonInfoPcCheck(pc);
							/** 인터서버에서 리스를 했다면 인터서버에서 빠져나오게 세팅 */
							if(!pc.getNetConnection().getInterServer()){
								if (pc.isInParty()) { // 파티중
									pc.getParty().leaveMember(pc);
								}
								
								if (pc.isInChatParty()) {// 채팅파티중
									pc.getChatParty().leaveMember(pc);
								}
								
								/** 팅겨서 소켓만 살아있다면 로그아웃으로 마지막 로그아웃 시간 저장 */
								LoginController.getInstance().logout(pc.getNetConnection());
							}
							
							/** 디비 데이타 정보 저장 할수있도록 따로 보관 소켓이 열려잇어야 저장되는거기때문에 따로 보관 */
							pc.getNetConnection().getAccount().updateAttendanceTime();
							pc.getNetConnection().getAccount().updateDGTime();
							pc.getNetConnection().getAccount().updateTam();
							pc.getNetConnection().getAccount().updateNcoin();
							
							/** 소켓 닫기 체크  */
							pc.setNetConnection(null);
						}
						
						/** 오브젝트 삭제 */
						pc.logout();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					_queue.remove();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}
}