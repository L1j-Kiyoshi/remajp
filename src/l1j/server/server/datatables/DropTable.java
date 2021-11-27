/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */

package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.L1Character;
import l1j.server.server.model.L1GroundInventory;
import l1j.server.server.model.L1Inventory;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.Instance.L1PetInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_ACTION_UI;
//Referenced classes of package l1j.server.server.templates:
//L1Npc, L1Item, ItemTable
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.templates.L1Drop;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.SQLUtil;

public class DropTable {

	private static Logger _log = Logger.getLogger(DropTable.class.getName());

	private static DropTable _instance;

	private final HashMap<Integer, ArrayList<L1Drop>> _droplists; // monster 마다의
																	// 드롭 리스트

	private static final byte HEADING_TABLE_X[] = { 0, 1, 1, 1, 0, -1, -1, -1 };

	private static final byte HEADING_TABLE_Y[] = { -1, -1, 0, 1, 1, 1, 0, -1 };

	public static DropTable getInstance() {
		if (_instance == null) {
			_instance = new DropTable();
		}
		return _instance;
	}

	private DropTable() {
		_droplists = allDropList();
	}
	
	public ArrayList<L1Drop> getDropList(int monid) {
		return _droplists.get(monid);
	}
	
	public L1Drop getDrop(int monid, int itemid) {
		ArrayList<L1Drop> drop = getDropList(monid);
		for (L1Drop d : drop) {
			if (d.getItemid() == itemid) {
				return d;
			}
		}
		return null;
	}
	
	public boolean isDropListItem(int monid, int itemid) {
		ArrayList<L1Drop> drop = getDropList(monid);
		for (L1Drop d : drop) {
			if (d.getItemid() == itemid) {
				return true;
			}
		}
		return false;
	}

	public static void reload() {
		synchronized (_instance) {
			DropTable oldInstance = _instance;
			_instance = new DropTable();
			oldInstance._droplists.clear();
		}
	}

	public static final int CLASSID_PRINCE = 0;
	public static final int CLASSID_PRINCESS = 1;
	public static final int CLASSID_KNIGHT_MALE = 61;
	public static final int CLASSID_KNIGHT_FEMALE = 48;
	public static final int CLASSID_ELF_MALE = 138;
	public static final int CLASSID_ELF_FEMALE = 37;
	public static final int CLASSID_WIZARD_MALE = 734;
	public static final int CLASSID_WIZARD_FEMALE = 1186;
	public static final int CLASSID_DARKELF_MALE = 2786;
	public static final int CLASSID_DARKELF_FEMALE = 2796;
	public static final int CLASSID_DRAGONKNIGHT_MALE = 6658;
	public static final int CLASSID_DRAGONKNIGHT_FEMALE = 6661;
	public static final int CLASSID_ILLUSIONIST_MALE = 6671;
	public static final int CLASSID_ILLUSIONIST_FEMALE = 6650;
	public static final int CLASSID_WARRIOR_MALE = 12490;
	public static final int CLASSID_WARRIOR_FEMALE = 12494;

	private HashMap<Integer, ArrayList<L1Drop>> allDropList() {
		HashMap<Integer, ArrayList<L1Drop>> droplistMap = new HashMap<Integer, ArrayList<L1Drop>>();

		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select * from droplist");
			rs = pstm.executeQuery();
			L1Drop drop = null;
			while (rs.next()) {
				int mobId = rs.getInt("mobId");
				int itemId = rs.getInt("itemId");
				int min = rs.getInt("min");
				int max = rs.getInt("max");
				int chance = rs.getInt("chance");
				int enchant = rs.getInt("Enchant");
				int Rate = rs.getInt("Rate");

				drop = new L1Drop(mobId, itemId, min, max, chance, enchant,
						Rate);

				ArrayList<L1Drop> dropList = droplistMap.get(drop.getMobid());
				if (dropList == null) {
					dropList = new ArrayList<L1Drop>();
					droplistMap.put(new Integer(drop.getMobid()), dropList);
				}
				dropList.add(drop);
			}
		} catch (SQLException e) {
			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return droplistMap;
	}

	@SuppressWarnings("resource")
	public static boolean SabuDrop(int npcid, int itemid, int max, int rate,
			String mobname, String itemname, String note) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("select * from droplist where mobId = ? and itemId = ?");
			pstm.setInt(1, npcid);
			pstm.setInt(2, itemid);
			rs = pstm.executeQuery();
			while (rs.next()) {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
				return false;
			}
			pstm = con
					.prepareStatement("INSERT INTO droplist SET mobId=?,itemId=?,min=?,max=?,chance=?,mobname=?,itemname=?,mobnote=?");
			pstm.setInt(1, npcid);
			pstm.setInt(2, itemid);
			pstm.setInt(3, 1);
			pstm.setInt(4, max);
			pstm.setInt(5, rate);
			pstm.setString(6, mobname);
			pstm.setString(7, itemname);
			pstm.setString(8, note);
			pstm.execute();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);

		}
		return true;
	}

	// 인벤트리에 드롭을 설정
	public void setDrop(L1NpcInstance npc, L1Inventory inventory) {
		/** 서버 오픈 대기 */
		if (Config.STANDBY_SERVER) {
			return;
		}
		// 드롭 리스트의 취득
		int mobId = npc.getNpcTemplate().get_npcId();
		ArrayList<L1Drop> dropList = _droplists.get(mobId);
		if (dropList == null) {
			return;
		}

		// 레이트 취득
		double droprate = Config.RATE_DROP_ITEMS;
		if (droprate <= 0) {
			droprate = 0;
		}
		double adenarate = Config.RATE_DROP_ADENA;
		if (adenarate <= 0) {
			adenarate = 0;
		}
		if (droprate <= 0 && adenarate <= 0) {
			return;
		}
		if (inventory.getSize() >= 1) {
			System.out.println("[드랍오류2] npcid : " + npc.getNpcId() + " / x : "
					+ npc.getX() + " y : " + npc.getY() + " m : "
					+ npc.getMapId());
		}
		int itemId;
		int itemCount;
		int addCount;
		int randomChance;
		L1ItemInstance item;

		/** 환상 이벤트 **/
		L1ItemInstance Fitem;
		L1ItemInstance Citem;
		L1ItemInstance Citem2;

		Random random = new Random(System.nanoTime());

		for (L1Drop drop : dropList) {
			// 드롭 아이템의 취득
			itemId = drop.getItemid();
			if (adenarate == 0 && itemId == L1ItemId.ADENA) {
				continue; // 아데나레이트 0으로 드롭이 아데나의 경우는 스르
			}

			// 드롭 찬스 판정
			randomChance = random.nextInt(0xf4240) + 1;
			double rateOfMapId = MapsTable.getInstance().getDropRate(npc.getMapId());
			double rateOfItem = DropItemTable.getInstance().getDropRate(itemId);
			double resultDroprate = drop.getChance() * droprate * rateOfMapId;

			resultDroprate = (int) (resultDroprate * rateOfItem);

			if (droprate == 0 || resultDroprate < randomChance) {
				continue;
			}
			// 드롭 개수를 설정
			double amount = DropItemTable.getInstance().getDropAmount(itemId);
			int min = drop.getMin();
			int max = drop.getMax();
			min = (int) (min * amount);
			max = (int) (max * amount);

			itemCount = min;
			addCount = max - min + 1;

			if (addCount > 1) {
				itemCount += random.nextInt(addCount);
			}
			if (itemId == L1ItemId.ADENA) { // 드롭이 아데나의 경우는 아데나레이트를 건다
			if (npc.getMapId() == 410) {
					itemCount = 0;
				} else {
					itemCount *= adenarate;
				}
			}
			
			// 화둥 몹들 아데나 안나오게
					
		    if(itemId == L1ItemId.ADENA && npc.getMapId() == 5557 && npc.getMaxHp() > 10000){ 
		    	continue; 
		    }
						 
			if (itemCount < 0) {
				itemCount = 0;
			}
			if (itemCount > 2000000000) {
				itemCount = 2000000000;
			}

			L1Item Litem = ItemTable.getInstance().getTemplate(itemId);
			// 아이템의 생성
			if (Litem != null) {

				if (Litem.isStackable()) {
					// 아이템의 생성
					item = ItemTable.getInstance().createItem(itemId);
					if (item == null) continue;
					/** 최초 소지하고있던 아데나 체크 */
					item.setTempCount(itemCount);
					item.setCount(itemCount);
					if (drop.getEnchant() != 0) {
						System.out.println("[오류] droplist : 겹쳐지는 아이템에 인첸됨.("+ item.getItemId() + ")");
						item.setEnchantLevel(drop.getEnchant());
					}
					// 아이템 격납
					inventory.storeItem(item);
				} else {
					for (int i = 0; i < itemCount; ++i) {
						// 아이템의 생성
						item = ItemTable.getInstance().createItem(itemId);
						item.setCount(1);
						if (drop.getEnchant() != 0) {
							item.setEnchantLevel(drop.getEnchant());
						}
						// 아이템 격납
						inventory.storeItem(item);
					}
				}
			}
		}
		/** 환상 이벤트 **/
		if (Config.ALT_FANTASYEVENT == true) {
			int itemRandom = random.nextInt(100) + 1;
			int countRandom = random.nextInt(100) + 1;
			int item1Random = random.nextInt(100 + 1);
			int Fcount = 0;
			int Itemnum = 0;
			if (item1Random <= 50) {
				Itemnum = 40127;
			} else {
				Itemnum = 40128;
			}
			if (countRandom <= 90) {
				Fcount = 1;
			} else if (countRandom >= 91) {
				Fcount = 2;
			}
			if (itemRandom <= 40) {
			} else if (itemRandom >= 46 || itemRandom <= 70) {
				Fitem = ItemTable.getInstance().createItem(Itemnum);
				Fitem.setCount(Fcount);
				inventory.storeItem(Fitem);
			} else if (itemRandom >= 96) {
				Fitem = ItemTable.getInstance().createItem(Itemnum);
				Fitem.setCount(Fcount);
				inventory.storeItem(Fitem);
			}
		}
		/** 추석 이벤트 **//*
		if (Config.ALT_CHUSEOKEVENT == true) {
			short mapid = npc.getMapId();
			int itemRandom = random.nextInt(100) + 1;
		if ((mapid >= 12859 && mapid <= 12862)||
				(mapid >= 35 && mapid <= 37) || mapid == 1700
					|| mapid == 15410 || mapid == 15410) {
			if (itemRandom <= 33) {
				Citem = ItemTable.getInstance().createItem(9096);
				inventory.storeItem(Citem);
			}
		  }
		}*/
		
		/** 추석 이벤트 **/
		if (Config.ALT_CHUSEOKEVENT == true) {
			short mapid = npc.getMapId();
			int itemRandom = random.nextInt(1000) + 1;
		if (mapid == 479) {
			if (itemRandom <= Config.라던수표) {
				Citem = ItemTable.getInstance().createItem(600368);
				inventory.storeItem(Citem);
			}
		  }
		}
		
		/** 깃털 이벤트 **/
		if (Config.ALT_FEATURE == true) {
			short mapid = npc.getMapId();
		if ((mapid >= 109 && mapid <= 110) || (mapid == 12862) ||
				(mapid == 479) || (mapid == 53)) { 
			int itemRandom = random.nextInt(100) + 1;
			if (itemRandom <= 10) {
				Fitem = ItemTable.getInstance().createItem(600249);
				inventory.storeItem(Fitem);
			}
		  }
		}

		/** 테스트 서버 **/
		if (Config.GAME_SERVER_TYPE == 1) {
			short mapid = npc.getMapId();
			if ((mapid >= 450 && mapid <= 478)
					|| (mapid >= 490 && mapid <= 496)
					|| (mapid >= 530 && mapid <= 536)) {
				return;
			}
			int lvl = npc.getLevel();
			int itemRandom = 0;
			if (lvl >= 20) {
				itemRandom = random.nextInt(lvl * 5 + 1) + lvl;
				Citem = ItemTable.getInstance().createItem(L1ItemId.TEST_MARK);
				Citem.setCount(itemRandom);
				inventory.storeItem(Citem);
			}
		}
		/**바포메트 감옥 열쇠*/

//		if (npc.getMapId() >= 1 && npc.getMapId() <= 2) {
//			int itemRandom = random.nextInt(10000);
//			if (itemRandom <= 100) {// 0.02%
//				Citem = ItemTable.getInstance().createItem(40163); // 바포메트 감옥 열쇠
//				Citem.setCount(1);
//				inventory.storeItem(Citem);
//			}
//		}
		random = null;
	}

	private static int ladunMonList[] = { 45254, 45256, 45287, 45398, 45412,
			45448, 45459, 45460, 45461, 45462, 45465, 45466, 45467, 45474,
			45475, 45483, 45484, 45486, 45489, 45495, 45512, 45517, 45518,
			45521, 45523, 45526, 45527, 45533, 45669, 45830, 45836, 45837,
			45841, 45846, 45850, 45851, 45852, 45854, 45855, 45856, 45857,
			45858, 45864, 45865, 45868, 45869, 45872, 45976, 45977, 45978,
			45979, 45980, 45981, 45982, 45983, 45984, 45985, 45986, 45987,
			45988, 45989, 45991, 45992, 45994, 45995, 45997, 46004, 46047 };

	public boolean autook(L1PcInstance pc) {
		L1PcInstance ckpc = L1World.getInstance().getPlayer(pc.getName());
		/*
		 * if(ckpc instanceof L1RobotInstance){ return true; }
		 */
		if (ckpc == null) {
			return false;
		}
		if (ckpc.getNetConnection() == null) {
			return false;
		}
		if (ckpc.getNetConnection().isClosed()) {
			return false;
		}
		if (ckpc.getSkillEffectTimerSet().hasSkillEffect(
				L1SkillId.STATUS_AUTOROOT)) {
			return false;
		}
		return true;
	}

	// 드롭을 분배
	public void dropShare(L1NpcInstance npc, ArrayList<?> acquisitorList, ArrayList<?> hateList, L1PcInstance pc) {
		/** 서버 오픈 대기 */
		if (Config.STANDBY_SERVER) {
			return;
		}
		
		L1Inventory inventory = npc.getInventory();
		int mobId = npc.getNpcTemplate().get_npcId();
		if (inventory.getSize() == 0) {
			return;
		}
		
		if (acquisitorList.size() != hateList.size()) {
			return;
		}
		// 헤이트의 합계를 취득
			int totalHate = 0;
			L1Character acquisitor;
			for (int i = hateList.size() - 1; i >= 0; i--) {
				acquisitor = (L1Character) acquisitorList.get(i);
				if (acquisitor != null
						&& acquisitor.isDead()
						&& (npc.getNpcId() == 4200011 || npc.getNpcId() == 4039007
								|| npc.getNpcId() == 100014
								|| npc.getNpcId() == 400016
								|| npc.getNpcId() == 400017
										|| npc.getNpcId() == 145684
								|| npc.getNpcId() == 4036016 || npc.getNpcId() == 4036017)) {
					acquisitorList.remove(i);
					hateList.remove(i);
				} else if (acquisitor != null
						&& acquisitor.getMapId() == npc.getMapId()
						&& (acquisitor.getLocation().getTileLineDistance(
								npc.getLocation()) <= Config.LOOTING_RANGE || (npc
								.getNpcId() == 4200011
								|| npc.getNpcId() == 4039007
								|| npc.getNpcId() == 100014
								|| npc.getNpcId() == 400016
								|| npc.getNpcId() == 400017
										|| npc.getNpcId() == 145684
								|| npc.getNpcId() == 4036016 || npc.getNpcId() == 4036017))) {
					totalHate += (Integer) hateList.get(i);
				} else { // null였거나 죽기도 하고 멀었으면 배제
					acquisitorList.remove(i);
					hateList.remove(i);
				}
			}
			// 드롭의 분배
			L1ItemInstance item;
			L1Inventory targetInventory = null;
			L1PcInstance player;
			Random random = new Random();
			int randomInt;
			int chanceHate;
			int itemId;

			for (int i = inventory.getSize(); i > 0; i--) {
				item = null;
				item = inventory.getItems().get(0);
				if (item == null) continue;
				
				itemId = item.getItem().getItemId();
				
				/** 아이템 정보 리스트에서 아데나인지 체크해서 
				 * 가호를 소지하고있다면 아데나를 준다 소지하고있는 대상은 라스트 어택자 */
				/** 가호 아이템 소지시 아데나 획득량  */
				if(itemId == L1ItemId.ADENA && pc != null){
					boolean blessing = false;
					double blessingS = 0;
					
					int Adena = 0;
					if(item.getCount() != item.getTempCount()){
						Adena = item.getTempCount();
					}else Adena = item.getCount();
					if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,8)){
						blessingS = 2.8;
						blessing = true;
					}else if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,7)){
						blessingS = 2.7;
					    blessing = true;
					}else if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,6)){
						blessingS = 2.6;
						blessing = true;
					}else if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,5)){
						blessingS = 2.5;
						blessing = true;
					}else if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,4)){
						blessingS = 2.4;
						blessing = true;
					}else if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,3)){
						blessingS = 2.3;
						blessing = true;
					}else if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,2)){
						blessingS = 2.2;
						blessing = true;
					}else if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,1)){
						blessingS = 2.1;
						blessing = true;
					}else if(pc.getInventory().checkItem(42015) && pc.getInventory().checkEquippedEnchant(421220,0)){
						blessingS = 2.0;
						blessing = true;
					}else if (pc.getInventory().checkItem(42015)){
						blessingS = 1.5;
						blessing = true;
					}else if(pc.getInventory().checkItem(42017)){
						blessingS = 1.1;
						blessing = true;
					}else blessing = false;
					if(blessing)Adena *= blessingS;
					item.setCount(Adena);		
				}
				
				/** 감시자 리퍼 -> 트랜스된 보스몹들 부적 드랍안되게 수정. **/
				if (pc != null) {
					if (itemId == 60135) {// 와퍼
						if (!pc.isElf()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 60137) {// 정옥
						if (!pc.isElf()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 60138) {// 마돌 법사,요정
						if (!pc.isElf() && !pc.isWizard()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 240016) {// 지혜
						if (!pc.isWizard()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 60133) {// 악피
						if (!pc.isCrown()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 60134) {// 용기
						if (!pc.isKnight() && !pc.isWarrior()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 60136) {// 유그드라열매
						if (!pc.isIllusionist()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 60139) {// 흑요석
						if (!pc.isDarkelf()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 60140) {// 뼈조각
						if (!pc.isDragonknight()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
					if (itemId == 60157) {// 속성석
						if (!pc.isIllusionist()) {
							inventory.removeItem(item, item.getCount());
							continue;
						}
					}
				}

				boolean isGround = false;
				if (item.getItem().getType2() == 0 && item.getItem().getType() == 2) { // light계 아이템
					item.setNowLighting(false);
				}
				if ((Config.AUTO_LOOT != 0 || AutoLoot.getInstance().isAutoLoot(itemId)) && totalHate > 0) {
					randomInt = random.nextInt(totalHate);
					chanceHate = 0;
					for (int j = hateList.size() - 1; j >= 0; j--) {
						chanceHate += (Integer) hateList.get(j);
						if (chanceHate > randomInt) {
							acquisitor = (L1Character) acquisitorList.get(j);
							/** 펫이라면 무조건 주인권한으로 변경 */
							if (acquisitor instanceof L1PetInstance) acquisitor = ((L1PetInstance) acquisitor).getMaster();
							if (acquisitor.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
								targetInventory = acquisitor.getInventory();
								if (acquisitor instanceof L1PcInstance) {
									player = (L1PcInstance) acquisitor;
									L1ItemInstance l1iteminstance = player.getInventory().findItemId(L1ItemId.ADENA);
									// 소지 아데나를 체크
									if (l1iteminstance != null && l1iteminstance.getCount() > 2000000000) {
										targetInventory = L1World.getInstance().getInventory(acquisitor.getX(),acquisitor.getY(), acquisitor.getMapId());
										// 가질 수 없기 때문에 발밑에 떨어뜨린다
										player.sendPackets(new S_ServerMessage(166, "소지하고 있는 아데나","2,000,000,000을 초과하고 있습니다."));		
									} else {
										if (npc.getNpcId() == 4200011 // 아이템 드랍메세지..
												|| npc.getNpcId() == 4039007
												|| npc.getNpcId() == 100014
												|| npc.getNpcId() == 400016
												|| npc.getNpcId() == 400017
												|| npc.getNpcId() == 4036016
														|| npc.getNpcId() == 145684
												|| npc.getNpcId() == 4036017) {
											Collection<L1Object> DragonDropTargetList = L1World .getInstance() .getVisibleObjects( npc.getMapId()) .values();
											for (L1Object DUser : DragonDropTargetList) {
												if (DUser instanceof L1PcInstance && player != DUser) 
													((L1PcInstance) DUser) .sendPackets(new S_ServerMessage( 813, npc.getName(), item.getLogName(), player.getName()));
											}
											player.sendPackets(new S_ServerMessage( 813, npc.getName(), item .getLogName(), player .getName()));
										} else if (player.isInParty()) { // 파티의 경우
											for (L1PcInstance partymember : player .getParty().getMembers()) {
												if(partymember.PartyRootMent){
												partymember.sendPackets(new S_ServerMessage( 813, npc.getName(), item.getLogName(), player.getName()));
												}
												}
										} else if (player.RootMent) {
											player.sendPackets(new S_ServerMessage(143, npc.getName(), item.getLogName()));
										}
									}
								}
							} else {
								targetInventory = L1World.getInstance().getInventory(acquisitor.getX(), acquisitor.getY(),acquisitor.getMapId()); 
								// 가질 수 없기 때문에발밑에떨어뜨린다
							}
							break;
						}
					}
				} else { // Non 오토 루팅

					int maxHatePc = -1;
					int maxHate = -1;

					for (int j = hateList.size() - 1; j >= 0; j--) {
						if (maxHate < (Integer) hateList.get(j)) {
							maxHatePc = j;
							maxHate = (Integer) hateList.get(j);
						}
					}

					if (maxHatePc != -1
							&& acquisitorList.get(maxHatePc) instanceof L1PcInstance) {
						item.startItemOwnerTimer((L1PcInstance) acquisitorList
								.get(maxHatePc));
					} else {
						item.startItemOwnerTimer(pc);
					}

					/*
					 * int size = list.toTargetArrayList().size(); L1PcInstance
					 * owner = null; for(int ss=0; ss<size; ss++){ L1Character cha =
					 * list.getMaxHateCharacter(); if(!acquisitorList.contains(cha)
					 * || !(cha instanceof L1PcInstance)){ list.remove(cha); }else{
					 * owner = (L1PcInstance) cha; break; } }
					 */
					/*
					 * if(owner == null) owner = pc; item.setDropMobId(mobId);
					 * item.startItemOwnerTimer(owner);
					 */

					// item.startItemOwnerTimer(pc);

					/*
					 * List<Integer> dirList = new ArrayList<Integer>(); for (int j
					 * = 0; j < 8; j++) { dirList.add(j); } int x = 0; int y = 0;
					 * int dir = 0; do { if (dirList.size() == 0) { x = 0; y = 0;
					 * break; } randomInt = random.nextInt(dirList.size()); dir =
					 * dirList.get(randomInt); dirList.remove(randomInt); x =
					 * HEADING_TABLE_X[dir]; y = HEADING_TABLE_Y[dir]; } while
					 * (!npc.getMap().isPassable(npc.getX()+x,
					 * npc.getY()+y));//!npc.getMap().isPassable(npc.getX(),
					 * npc.getY(), dir));
					 */

					List<Integer> dirList = new ArrayList<Integer>();
					for (int j = 0; j < 8; j++) {
						dirList.add(j);
					}
					int x = 0;
					int y = 0;
					int dir = 0;
					do {
						if (dirList.size() == 0) {
							x = 0;
							y = 0;
							break;
						}
						randomInt = random.nextInt(dirList.size());
						dir = dirList.get(randomInt);
						dirList.remove(randomInt);
						switch (dir) {
						case 0:
							x = 0;
							y = -1;
							break;
						case 1:
							x = 1;
							y = -1;
							break;
						case 2:
							x = 1;
							y = 0;
							break;
						case 3:
							x = 1;
							y = 1;
							break;
						case 4:
							x = 0;
							y = 1;
							break;
						case 5:
							x = -1;
							y = 1;
							break;
						case 6:
							x = -1;
							y = 0;
							break;
						case 7:
							x = -1;
							y = -1;
							break;
						}
					} while (!npc.getMap().isPassable(npc.getX(), npc.getY(), dir));

					targetInventory = L1World.getInstance().getInventory(
							npc.getX() + x, npc.getY() + y, npc.getMapId());
					isGround = true;
					dirList = null;
				}
				if (itemId >= 40131 && itemId <= 40135) {
					if (isGround || targetInventory == null) {
						inventory.removeItem(item, item.getCount());
						continue;
					}
				}
				L1ItemInstance resultItem = inventory.tradeItem(item,
						item.getCount(), targetInventory);

				if (resultItem != null
						&& targetInventory instanceof L1GroundInventory) {
					resultItem.setDropMobId(mobId);
				}
				int item_Id = item.getItem().getItemId();
				
				if (item_Id == 40222 || item_Id == 41148 || item_Id == 60199 || item_Id == 7310 || item_Id == 600512
		      	 || item_Id == 55596    || item_Id == 55594    || item_Id == 55599   || item_Id == 600510 || item_Id == 600360
				 || item_Id == 600485   || item_Id == 600483   || item_Id == 600482  || item_Id == 600511 || item_Id == 600484
				 || item_Id == 600329   || item_Id == 600486   || item_Id == 600487  || item_Id == 600488 || item_Id == 600489
				 || item_Id == 30115    || item_Id == 30116    || item_Id == 30117   || item_Id == 30118  || item_Id == 600512	
				 || item_Id == 30119    || item_Id == 30120    || item_Id == 30121   || item_Id == 30122  || item_Id == 60510
				 || item_Id == 27528  || item_Id == 27529  || item_Id == 27530 || item_Id == 6002649|| item_Id == 30110
				 || item_Id == 10171    || item_Id == 20279    || item_Id == 10172   || item_Id == 20314  || item_Id == 20614
				 || item_Id == 40354    || item_Id == 40346    || item_Id == 40370   || item_Id == 40362  || item_Id == 60783
				 || item_Id == 600360   || item_Id == 600359   || item_Id == 600358  || item_Id == 600328 || item_Id == 900019
				 || item_Id == 20077    || item_Id == 60783    || item_Id == 5000003 || item_Id == 5000004|| item_Id == 5000005
				 || item_Id == 5000006  || item_Id == 5000007  || item_Id == 101010  || item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783|| item_Id == 60783) {
					L1World.getInstance().broadcastPacketToAll(new S_ACTION_UI(S_ACTION_UI.리마월드메시지, 403,"어느 아덴 용사가 ", item), true); // 드랍 아이템 먹을때 멘트출력 
				}
			}
			npc.getLight().turnOnOffLight();
			random = null;
		}

		public void setPainwandDrop(L1NpcInstance npc, L1Inventory inventory) {
			/** 서버 오픈 대기 */
			if (Config.STANDBY_SERVER) {
				return;
			}

			// 드롭 리스트의 취득
			int mobId = npc.getNpcTemplate().get_npcId();
			ArrayList<L1Drop> dropList = _droplists.get(mobId);
			if (dropList == null) {
				return;
			}

			// 레이트 취득
			double droprate = Config.RATE_DROP_ITEMS;
			if (droprate <= 0) {
				droprate = 0;
			}
			double adenarate = Config.RATE_DROP_ADENA;
			if (adenarate <= 0) {
				adenarate = 0;
			}
			if (droprate <= 0 && adenarate <= 0) {
				return;
			}

			int itemId;
			int itemCount;
			int addCount;
			int randomChance;
			L1ItemInstance item;
			Random random = new Random(System.nanoTime());

			for (L1Drop drop : dropList) {
				// 드롭 아이템의 취득
				itemId = drop.getItemid();
				if (adenarate == 0 && itemId == L1ItemId.ADENA) {
					continue; // 아데나레이트 0으로 드롭이 아데나의 경우는 스르
				}
				if (itemId != L1ItemId.ADENA) {
					continue;
				}

				// 드롭 찬스 판정
				randomChance = random.nextInt(0xf4240) + 1;
				double rateOfMapId = MapsTable.getInstance().getDropRate(npc.getMapId());
				double rateOfItem = DropItemTable.getInstance().getDropRate(itemId);
				if (droprate == 0 || drop.getChance() * droprate * rateOfMapId * rateOfItem < randomChance) {
					continue;
				}

				// 드롭 개수를 설정
				double amount = DropItemTable.getInstance().getDropAmount(itemId);
				int min = drop.getMin();
				int max = drop.getMax();
				if (amount < 0) {
					min = (int) (min / amount);
					max = (int) (max / amount);
				} else {
					min = (int) (min * amount);
					max = (int) (max * amount);
				}

				itemCount = min;
				addCount = max - min + 1;

				if (addCount > 1) {
					itemCount += random.nextInt(addCount);
				}
				
				/** 아데나라면 인벤에 소지 효과 부여 */
				if (itemId == L1ItemId.ADENA) {
					if (npc.getMapId() == 410) {
						itemCount = 0;
					} else {
						itemCount *= adenarate;
					}
				}
				if (itemCount < 0) {
					itemCount = 0;
				}
				if (itemCount > 2000000000) {
					itemCount = 2000000000;
				}

				// 아이템의 생성
				item = ItemTable.getInstance().createItem(itemId);
				item.setCount(itemCount);
				// 아이템 격납
				inventory.storeItem(item);
			}
		}
	}