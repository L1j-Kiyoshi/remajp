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
package l1j.server.server.model.npc.action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import l1j.server.L1DatabaseFactory;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1ObjectAmount;
import l1j.server.server.model.L1PcInventory;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.L1NpcHtml;
import l1j.server.server.serverpackets.S_HowManyMake;
import l1j.server.server.serverpackets.S_NewCreateItem;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillSound;
import l1j.server.server.serverpackets.S_SystemMessage;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.IterableElementList;
import l1j.server.server.utils.SQLUtil;

public class L1NpcMakeItemAction extends L1NpcXmlAction {
	private final List<L1ObjectAmount<Integer>> _materials = new ArrayList<L1ObjectAmount<Integer>>();
	private final List<L1ObjectAmount<Integer>> _items = new ArrayList<L1ObjectAmount<Integer>>();
	private final boolean _isAmountInputable;
	private final L1NpcAction _actionOnSucceed;
	private final L1NpcAction _actionOnFail;

	public L1NpcMakeItemAction(Element element) {
		super(element);

		_isAmountInputable = L1NpcXmlParser.getBoolAttribute(element,
				"AmountInputable", true);
		NodeList list = element.getChildNodes();
		for (Element elem : new IterableElementList(list)) {
			if (elem.getNodeName().equalsIgnoreCase("Material")) {
				int id = Integer.valueOf(elem.getAttribute("ItemId"));
				int amount = Integer.valueOf(elem.getAttribute("Amount"));
				_materials.add(new L1ObjectAmount<Integer>(id, amount));
				continue;
			}
			if (elem.getNodeName().equalsIgnoreCase("Item")) {
				int id = Integer.valueOf(elem.getAttribute("ItemId"));
				int amount = Integer.valueOf(elem.getAttribute("Amount"));
				_items.add(new L1ObjectAmount<Integer>(id, amount));
				continue;
			}
		}

		if (_items.isEmpty() || _materials.isEmpty()) {
			throw new IllegalArgumentException();
		}

		Element elem = L1NpcXmlParser.getFirstChildElementByTagName(element,
				"Succeed");
		_actionOnSucceed = elem == null ? null : new L1NpcListedAction(elem);
		elem = L1NpcXmlParser.getFirstChildElementByTagName(element, "Fail");
		_actionOnFail = elem == null ? null : new L1NpcListedAction(elem);

	}

	private int _id = 0;
	private int _count = 0;
	Random _rnadom = new Random(System.nanoTime());

	private boolean makeItems(L1PcInstance pc, String npcName, int amount) {
		// 제작버그 관련 추가
		if (amount <= 0 || amount > 9999) {
			return false;
		}

		int ran = _rnadom.nextInt(100);
		boolean isEnoughMaterials = true;
		L1Item temp = null;
		for (L1ObjectAmount<Integer> material : _materials) {
			if (!pc.getInventory().checkItemNotEquipped(material.getObject(),
					material.getAmount() * amount)) {
				temp = ItemTable.getInstance()
						.getTemplate(material.getObject());
				int i = 0;
				if (temp.getItemId() == 40346)
					i = 60187; // 안타 숨결
				else if (temp.getItemId() == 40362)
					i = 60188; // 파푸 숨결
				else if (temp.getItemId() == 40370)
					i = 60189; // 린드 숨결
				else if (temp.getItemId() == 40466)
					i = 60186; // 용의 심장
				if (i != 0
						&& pc.getInventory().checkItemNotEquipped(i,
								material.getAmount() * amount)) // 있다면
					continue;
				pc.sendPackets(new S_ServerMessage(337, temp.getName()
						+ "("
						+ ((material.getAmount() * amount) - pc.getInventory()
								.countItems(temp.getItemId())) + ")"), true);
				isEnoughMaterials = false;
			}
		}
		if (!isEnoughMaterials) {
			return false;
		}

		int countToCreate = 0;
		int weight = 0;

		for (L1ObjectAmount<Integer> makingItem : _items) {
			temp = ItemTable.getInstance().getTemplate(makingItem.getObject());
			if (temp.isStackable()) {
				if (!pc.getInventory().checkItem(makingItem.getObject())) {
					countToCreate += 1;
				}
			} else {
				countToCreate += makingItem.getAmount() * amount;
			}
			weight += (temp.getWeight() * (makingItem.getAmount() * amount)) / 1000;
			long _CountToCreate = countToCreate;
			// 제작 버그 관련 추가
			if (_CountToCreate < 0 || _CountToCreate > 9999)
				return false;
		}
		if (pc.getInventory().getSize() + countToCreate > 180) {
			pc.sendPackets(new S_ServerMessage(263), true);
			return false;
		}
		if (pc.getMaxWeight() < pc.getInventory().getWeight() + weight) {
			pc.sendPackets(new S_ServerMessage(82), true);
			return false;
		}

		for (L1ObjectAmount<Integer> material : _materials) {
			if (!pc.getInventory().checkItemNotEquipped(material.getObject(),
					material.getAmount() * amount)) {
				int i = 0;
				if (material.getObject() == 40346)
					i = 60187; // 안타 숨결
				else if (material.getObject() == 40362)
					i = 60188; // 파푸 숨결
				else if (material.getObject() == 40370)
					i = 60189; // 린드 숨결
				else if (material.getObject() == 40466)
					i = 60186; // 용의 심장
				if (i != 0)
					pc.getInventory().consumeItem(i,
							material.getAmount() * amount);
			} else
				pc.getInventory().consumeItem(material.getObject(),
						material.getAmount() * amount);
		}
		L1ItemInstance item = null;
		for (L1ObjectAmount<Integer> makingItem : _items) {
			if (ran < 5
					&& (makingItem.getObject() == 21167
							|| makingItem.getObject() == 21168

							|| makingItem.getObject() == 20049
							|| makingItem.getObject() == 20050

							|| makingItem.getObject() == 20057
							|| makingItem.getObject() == 20109
							|| makingItem.getObject() == 20200
							|| makingItem.getObject() == 20178

							|| makingItem.getObject() == 20152
							|| makingItem.getObject() == 20076
							|| makingItem.getObject() == 20186
							|| makingItem.getObject() == 21259

							|| makingItem.getObject() == 20040
							|| makingItem.getObject() == 20018
							|| makingItem.getObject() == 20025 || makingItem
							.getObject() == 20029)) {// /대성공 확률
				try {
					pc.sendPackets(new S_SkillSound(pc.getId(), 2029));
					item = pc.getInventory().storeItem(makingItem.getObject(),
							makingItem.getAmount() * amount, 0);

					commit("대성공 " + npcName + " : " + item.getName(), npcName,
							makingItem.getAmount() * amount);
					item.setBless(0);
					// item.setIdentified(true);
					// pc.sendPackets(new S_ItemName(t), true);
					pc.getInventory().updateItem(item, L1PcInventory.COL_BLESS);
					pc.getInventory().saveItem(item, L1PcInventory.COL_BLESS);
					L1World.getInstance().broadcastPacketToAll(
							new S_PacketBox(S_PacketBox.GREEN_MESSAGE, pc
									.getName()
									+ "님이 축복받은 "
									+ item.getName()
									+ " 제작에 성공하였습니다."));
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				try {
					int rnd = _rnadom.nextInt(100);
					_id = makingItem.getObject();
					_count = makingItem.getAmount() * amount;
					if (makingItem.getObject() >= 40033
							&& makingItem.getObject() <= 40038) {
						if (rnd < 90) {
							_id = 500209;
							_count = 30;
							pc.sendPackets(new S_NewCreateItem(0X3b, 실패));// 1479
						} else {
							pc.sendPackets(new S_NewCreateItem(0X3b, 성공));// 1479
							pc.sendPackets(new S_SkillSound(pc.getId(), 2029));
						}
					} else {
						// System.out.println("123123");
						pc.sendPackets(new S_NewCreateItem(0X3b, 성공));// 1479
						pc.sendPackets(new S_SkillSound(pc.getId(), 2029));
					}

					item = pc.getInventory().storeItem(_id, _count);
					commit(npcName + " : " + item.getName(), npcName, _count);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (item != null) {
				String itemName = null;
				if (_id != 0) {
					itemName = ItemTable.getInstance().getTemplate(_id)
							.getName();
				} else {
					itemName = ItemTable.getInstance()
							.getTemplate(makingItem.getObject()).getName();
				}

				if (_count != 0) {
					itemName = itemName + " (" + _count + ")";
				} else {
					if (makingItem.getAmount() * amount > 1) {
						itemName = itemName + " (" + makingItem.getAmount()
								* amount + ")";
					}
				}

				_id = 0;
				_count = 0;
				pc.sendPackets(new S_ServerMessage(143, npcName, itemName),
						true);
			}
		}
		return true;
	}

	private final String 성공 = "08 00 12 29 08 d3 7a 10 01 18 ff ff ff "
			+ "ff ff ff ff ff ff 01 20 00 28 01 30 00 38 01 42 "
			+ "06 24 31 30 39 34 37 48 00 50 00 58 b0 2d 62 00 " + "9d 80";
	private final String 실패 = "08 01 1a 04 08 00 10 00 85 32";

	private int countNumOfMaterials(L1PcInventory inv) {
		int count = Integer.MAX_VALUE;
		for (L1ObjectAmount<Integer> material : _materials) {
			int numOfSet = inv.countItems(material.getObject())
					/ material.getAmount();
			count = Math.min(count, numOfSet);
		}
		return count;
	}

	@Override
	public L1NpcHtml execute(String actionName, L1PcInstance pc, L1Object obj,
			byte[] args) {
		int numOfMaterials = countNumOfMaterials(pc.getInventory());
		if (1 < numOfMaterials && _isAmountInputable) {
			pc.sendPackets(new S_HowManyMake(obj.getId(), numOfMaterials,
					actionName));
			return null;
		}
		return executeWithAmount(actionName, pc, obj, 1);
	}

	@Override
	public L1NpcHtml executeWithAmount(String actionName, L1PcInstance pc,
			L1Object obj, int amount) {
		L1NpcInstance npc = (L1NpcInstance) obj;
		L1NpcHtml result = null;
		if (pc.getInventory().calcWeightpercent() >= 90) {
			pc.sendPackets(new S_SystemMessage("무게 게이지가 부족하여 이용 할 수 없습니다."));
			return L1NpcHtml.HTML_CLOSE;
		}
		if (makeItems(pc, npc.getNpcTemplate().get_name(), amount)) {

			if (_actionOnSucceed != null) {
				result = _actionOnSucceed.execute(actionName, pc, obj,
						new byte[0]);
			}
		} else {
			if (_actionOnFail != null) {
				result = _actionOnFail
						.execute(actionName, pc, obj, new byte[0]);
			}
		}
		return result == null ? L1NpcHtml.HTML_CLOSE : result;
	}

	public void commit(String com, String name, int count) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con
					.prepareStatement("SELECT * FROM request_log WHERE command=?");
			pstm.setString(1, com);
			rs = pstm.executeQuery();
			Connection con2 = null;
			PreparedStatement pstm2 = null;
			try {
				con2 = L1DatabaseFactory.getInstance().getConnection();
				if (rs.next()) {
					int amount = rs.getInt("count");
					pstm2 = con2
							.prepareStatement("UPDATE request_log SET count=? WHERE command=?");
					pstm2.setInt(1, amount + count);
					pstm2.setString(2, com);
				} else {
					pstm2 = con2
							.prepareStatement("INSERT INTO request_log SET command=?, count=?");
					pstm2.setString(1, com);
					pstm2.setInt(2, count);
				}
				pstm2.executeUpdate();
			} catch (SQLException e) {
			} finally {
				SQLUtil.close(pstm2);
				SQLUtil.close(con2);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
	}

}
