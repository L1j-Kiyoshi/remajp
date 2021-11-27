//package l1j.server.server.model.item;
//
//import java.io.File;
//import java.sql.Connection;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Random;
//import java.util.concurrent.CopyOnWriteArrayList;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import javax.xml.bind.JAXBContext;
//import javax.xml.bind.Unmarshaller;
//import javax.xml.bind.annotation.XmlAccessType;
//import javax.xml.bind.annotation.XmlAccessorType;
//import javax.xml.bind.annotation.XmlAttribute;
//import javax.xml.bind.annotation.XmlElement;
//import javax.xml.bind.annotation.XmlRootElement;
//
//import l1j.server.L1DatabaseFactory;
//import l1j.server.server.datatables.ItemTable;
//import l1j.server.server.model.L1Inventory;
//import l1j.server.server.model.L1World;
//import l1j.server.server.model.Instance.L1FieldObjectInstance;
//import l1j.server.server.model.Instance.L1ItemInstance;
//import l1j.server.server.model.Instance.L1PcInstance;
//import l1j.server.server.serverpackets.S_Message_YN;
//import l1j.server.server.serverpackets.S_ServerMessage;
//import l1j.server.server.templates.L1Item;
//import l1j.server.server.utils.SQLUtil;
//
//@XmlAccessorType(XmlAccessType.FIELD)
//public class L1TreasureBox {
//
//	private static Logger _log = Logger.getLogger(L1TreasureBox.class.getName());
//
//	@XmlAccessorType(XmlAccessType.FIELD)
//	@XmlRootElement(name = "TreasureBoxList")
//	private static class TreasureBoxList implements Iterable<L1TreasureBox> {
//		@XmlElement(name = "TreasureBox")
//		private List<L1TreasureBox> _list;
//
//		public Iterator<L1TreasureBox> iterator() {
//			return _list.iterator();
//		}
//	}
//
//	@XmlAccessorType(XmlAccessType.FIELD)
//	private static class Item {
//		@XmlAttribute(name = "ItemId")
//		private int _itemId;
//
//		@XmlAttribute(name = "Count")
//		private int _count;
//
//		@XmlAttribute(name = "Enchant")
//		private int _enchant;
//
//		private int _chance;
//
//		@XmlAttribute(name = "Chance")
//		private void setChance(double chance) {
//			_chance = (int) (chance * 10000);
//		}
//
//		public int getItemId() {
//			return _itemId;
//		}
//
//		public int getCount() {
//			return _count;
//		}
//
//		// 아이템 인첸트 레벨
//		public int getEnchant() {
//			return _enchant;
//		}
//
//		public double getChance() {
//			return _chance;
//		}
//	}
//
//	private static enum TYPE {
//		RANDOM, SPECIFIC
//	}
//
//	private static final String PATH = "./data/xml/Item/TreasureBox.xml";
//
//	private static final HashMap<Integer, L1TreasureBox> _dataMap = new HashMap<Integer, L1TreasureBox>();
//
//	public static L1TreasureBox get(int id) {
//		return _dataMap.get(id);
//	}
//
//	@XmlAttribute(name = "ItemId")
//	private int _boxId;
//
//	@XmlAttribute(name = "Type")
//	private TYPE _type;
//
//	private int getBoxId() {
//		return _boxId;
//	}
//
//	private TYPE getType() {
//		return _type;
//	}
//
//	@XmlElement(name = "Item")
//	private CopyOnWriteArrayList<Item> _items;
//
//	private List<Item> getItems() {
//		return _items;
//	}
//
//	private int _totalChance;
//
//	private int getTotalChance() {
//		return _totalChance;
//	}
//
//	private void init() {
//		for (Item each : getItems()) {
//			_totalChance += each.getChance();
//			if (ItemTable.getInstance().getTemplate(each.getItemId()) == null) {
//				getItems().remove(each);
//				_log.warning("아이템 ID " + each.getItemId() + " 의 템플릿이 발견되지 않았습니다.");
//			}
//		}
//		if (getTotalChance() != 0 && getTotalChance() != 1000000) {
//			_log.warning("ID " + getBoxId() + "의 확률의 합계가 100%가 되지 않습니다.");
//		}
//	}
//
//	public static void load() {
//		try {
//			JAXBContext context = JAXBContext.newInstance(L1TreasureBox.TreasureBoxList.class);
//
//			Unmarshaller um = context.createUnmarshaller();
//
//			File file = new File(PATH);
//			TreasureBoxList list = (TreasureBoxList) um.unmarshal(file);
//
//			for (L1TreasureBox each : list) {
//				each.init();
//				_dataMap.put(each.getBoxId(), each);
//			}
//		} catch (Exception e) {
//			_log.log(Level.SEVERE, PATH + "의 로드에 실패.", e);
//			System.exit(0);
//		}
//	}
//
//	public boolean open(L1PcInstance pc) {
//		L1ItemInstance item = null;
//		Random random = null;
//
//		if (getType().equals(TYPE.SPECIFIC)) {
//			if (pc.getInventory().getSize() + getItems().size() > 180) {
//				return false;
//			}
//		} else if (getType().equals(TYPE.RANDOM)) {
//			if (pc.getInventory().getSize() + 1 > 180) {
//				return false;
//			}
//		}
//
//		if (getType().equals(TYPE.SPECIFIC)) {
//			for (Item each : getItems()) {
//				item = ItemTable.getInstance().createItem(each.getItemId());
//				if (item != null) {
//					item.setCount(each.getCount());
//					item.setEnchantLevel(each.getEnchant());
//					if ((item.getItemId() >= 450008 && item.getItemId() <= 450013) || item.getItemId() == 450020) {
//						int[] at = { 1, 4, 7, 10 };
//						random = new Random(System.nanoTime());
//						item.setAttrEnchantLevel(at[random.nextInt(4)]);
//						// 붉은사자 무기 상자
//					} else if (getBoxId() >= 60399 && getBoxId() <= 60405) {
//						item.setEnchantLevel(9);
//						int[] at = { 3, 6, 9, 12 };
//						random = new Random(System.nanoTime());
//						item.setAttrEnchantLevel(at[random.nextInt(4)]);
//						item.setIdentified(true);
//						// 붉은사자 방어구 상자
//					} else if (getBoxId() >= 60406 && getBoxId() <= 60409) {
//						item.setEnchantLevel(5);
//						item.setIdentified(true);
//					}
//					storeItem(pc, item);
//					if (item.getItemId() == 40308) {
//						L1Item tempItem = ItemTable.getInstance().getTemplate(getBoxId());
//						aden_store_databaseInsert(pc, tempItem);
//					}
//				}
//			}
//
//		} else if (getType().equals(TYPE.RANDOM)) {
//			boolean ck = false;
//			random = new Random(System.nanoTime());
//			if (getBoxId() == 430117) { // 드루가가방
//				item = ItemTable.getInstance().createItem(40308);
//				if (item != null) {
//					item.setCount(1000);
//					storeItem(pc, item);
//					if (item.getItemId() == 40308) {
//						L1Item tempItem = ItemTable.getInstance().getTemplate(getBoxId());
//						aden_store_databaseInsert(pc, tempItem);
//					}
//					ck = true;
//				}
//			}
//			if (!ck) {
//				int chance = 0;
//				int r = random.nextInt(getTotalChance());
//
//				for (Item each : getItems()) {
//					chance += each.getChance();
//					////////////////////////////////
//					/*
//					 * if (r < chance) { item =
//					 * ItemTable.getInstance().createItem(each.getItemId()); if (item != null &&
//					 * !open(pc)) { item.setCount(each.getCount()); storeItem(pc, item); //메티스상자
//					 * if((item.getItemId() == 1430509) || each.getItemId() == 600247){
//					 * L1World.getInstance().broadcastPacketToAll(new S_PacketBox
//					 * (S_PacketBox.GREEN_MESSAGE,"아덴 월드의 어느 용사가 " + item.getName() +
//					 * " 를(을) 획득하였습니다.")); } } break; }
//					 */
//					////////////////////////////////
//					if (r < chance) {
//						item = ItemTable.getInstance().createItem(each.getItemId());
//						if (item != null) {
//							item.setCount(each.getCount());
//							item.setEnchantLevel(each.getEnchant());
//							if ((item.getItemId() >= 450008 && item.getItemId() <= 450013) || item.getItemId() == 450020) {
//								int[] at = { 1, 4, 7, 10 };
//								random = new Random(System.nanoTime());
//								item.setAttrEnchantLevel(at[random.nextInt(4)]);
//							} else if (getBoxId() == 60320) {
//								if (item.getItemId() == 266) {//
//									int[] at = { 2, 5, 8, 11 };
//									random = new Random(System.nanoTime());
//									item.setAttrEnchantLevel(at[random.nextInt(4)]);
//								} else if (item.getItemId() == 6001) {// 냉키
//									int[] at = { 3, 6, 9, 12 };
//									random = new Random(System.nanoTime());
//									item.setAttrEnchantLevel(at[random.nextInt(4)]);
//								} else if (item.getItemId() == 410005) {
//									random = new Random(System.nanoTime());
//									item.setEnchantLevel(3 + random.nextInt(3));
//								}
//							} else if (getBoxId() >= 60338 && getBoxId() <= 60344) {
//								random = new Random(System.nanoTime());
//								int rnd = random.nextInt(361);
//								if (rnd > 271)
//									item.setEnchantLevel(0);
//								else if (rnd > 191)
//									item.setEnchantLevel(1);
//								else if (rnd > 121)
//									item.setEnchantLevel(2);
//								else if (rnd > 61)
//									item.setEnchantLevel(3);
//								else if (rnd > 11)
//									item.setEnchantLevel(4);
//								else if (rnd > 1)
//									item.setEnchantLevel(5);
//								else if (rnd == 1)
//									item.setEnchantLevel(6);
//								else if (rnd == 0)
//									item.setEnchantLevel(7);
//							} else if (getBoxId() == 60345) {
//								random = new Random(System.nanoTime());
//								int rnd = random.nextInt(3961);
//								if (rnd > 3061)
//									item.setEnchantLevel(0);
//								else if (rnd > 2261)
//									item.setEnchantLevel(1);
//								else if (rnd > 1561)
//									item.setEnchantLevel(2);
//								else if (rnd > 961)
//									item.setEnchantLevel(3);
//								else if (rnd > 461)
//									item.setEnchantLevel(4);
//								else if (rnd > 61)
//									item.setEnchantLevel(5);
//								else if (rnd > 11)
//									item.setEnchantLevel(6);
//								else if (rnd > 1)
//									item.setEnchantLevel(7);
//								else if (rnd == 1)
//									item.setEnchantLevel(8);
//								else if (rnd == 0)
//									item.setEnchantLevel(9);
//							}
//							storeItem(pc, item);
//							if (item.getItemId() == 40308) {
//								L1Item tempItem = ItemTable.getInstance().getTemplate(getBoxId());
//								aden_store_databaseInsert(pc, tempItem);
//							}
//							if (item.getItem().getItemId() == 430116) {
//								pc.sendPackets(new S_Message_YN(1565, null));
//							}
//						}
//						break;
//					}
//				}
//			}
//		}
//		random = null;
//
//		if (item == null) {
//			return false;
//		} else {
//			int itemId = getBoxId();
//
//			if (itemId == 40576 || itemId == 40577 || itemId == 40578 || itemId == 40411 || itemId == 49013) {
//				pc.death(null);
//			}
//			return true;
//		}
//	}
//
//	private static void storeItem(L1PcInstance pc, L1ItemInstance item) {
//		L1Inventory inventory;
//
//		if (item.getItemId() >= 60173 && item.getItemId() <= 60176) {
//			item.setIdentified(true);
//		}
//		if (pc.getInventory().checkAddItem(item, item.getCount()) == L1Inventory.OK) {
//			inventory = pc.getInventory();
//		} else {
//			inventory = L1World.getInstance().getInventory(pc.getLocation());
//		}
//		inventory.storeItem(item);
//		pc.sendPackets(new S_ServerMessage(403, item.getLogName()));
//	}
//
//	public int getDragonKeyCheck() {
//		int i = 0;
//		for (L1FieldObjectInstance npc : L1World.getInstance().getAllField()) {
//			if (npc.getNpcId() == 100011 || npc.getNpcId() == 4212015 || npc.getNpcId() == 777773 || npc.getNpcId() == 4212016 || npc.getNpcId() == 910008)
//				i++;
//		}
//		Connection con = null;
//		PreparedStatement pstm = null;
//		ResultSet rs = null;
//		try {
//			con = L1DatabaseFactory.getInstance().getConnection();
//			pstm = con.prepareStatement("SELECT * FROM character_items WHERE item_id = ?");
//			pstm.setInt(1, 430116);
//			rs = pstm.executeQuery();
//			while (rs.next()) {
//				i++;
//				if (i >= 6)
//					break;
//			}
//
//			pstm.close();
//			rs.close();
//
//			pstm = con.prepareStatement("SELECT * FROM npc_shop_sell WHERE item_id = ?");
//			pstm.setInt(1, 430116);
//			rs = pstm.executeQuery();
//			while (rs.next()) {
//				i++;
//				if (i >= 6)
//					break;
//			}
//		} catch (SQLException e) {
//			_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
//		} finally {
//			SQLUtil.close(rs);
//			SQLUtil.close(pstm);
//			SQLUtil.close(con);
//		}
//		return i;
//	}
//
//	private void aden_store_databaseInsert(L1PcInstance pc, L1Item box) {
//		Connection con = null;
//		PreparedStatement pst = null;
//		ResultSet rs = null;
//		try {
//			con = L1DatabaseFactory.getInstance().getConnection();
//			pst = con.prepareStatement("SELECT * FROM log_treasurebox_aden WHERE char_name = ? AND box_id = ?");
//			pst.setString(1, pc.getName());
//			pst.setInt(2, box.getItemId());
//			rs = pst.executeQuery();
//			if (rs.next()) {
//				int count = rs.getInt("count");
//				Connection con2 = null;
//				PreparedStatement pstm2 = null;
//				try {
//					con2 = L1DatabaseFactory.getInstance().getConnection();
//					pstm2 = con2.prepareStatement("UPDATE log_treasurebox_aden SET count=? WHERE char_name=? AND box_id=?");
//					pstm2.setInt(1, count + 1);
//					pstm2.setString(2, pc.getName());
//					pstm2.setInt(3, box.getItemId());
//					pstm2.executeUpdate();
//				} catch (SQLException e) {
//				} finally {
//					SQLUtil.close(pstm2);
//					SQLUtil.close(con2);
//				}
//			} else {
//				Connection con2 = null;
//				PreparedStatement pstm2 = null;
//				try {
//					con2 = L1DatabaseFactory.getInstance().getConnection();
//					pstm2 = con.prepareStatement("INSERT INTO log_treasurebox_aden SET char_name=?, box_id =?, box_name=?, count=?");
//					pstm2.setString(1, pc.getName());
//					pstm2.setInt(2, box.getItemId());
//					pstm2.setString(3, box.getName());
//					pstm2.setInt(4, 1);
//					pstm2.executeUpdate();
//				} catch (SQLException e) {
//				} finally {
//					SQLUtil.close(pstm2);
//					SQLUtil.close(con2);
//				}
//			}
//		} catch (SQLException e) {
//		} finally {
//			SQLUtil.close(rs);
//			SQLUtil.close(pst);
//			SQLUtil.close(con);
//		}
//	}
//}
