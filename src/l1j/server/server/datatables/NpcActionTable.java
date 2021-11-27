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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javolution.util.FastTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.npc.action.L1NpcListedAction;
import l1j.server.server.model.npc.action.L1NpcMakeItemAction;
import l1j.server.server.model.npc.action.L1NpcXmlParser;
import l1j.server.server.utils.FileUtil;
import l1j.server.server.utils.PerformanceTimer;

public class NpcActionTable {
	private static Logger _log = Logger.getLogger(NpcActionTable.class.getName());

	private static NpcActionTable _instance;
	private final List<L1NpcAction> _actions = new ArrayList<L1NpcAction>();
	private final List<L1NpcAction> _talkActions = new ArrayList<L1NpcAction>();

	private List<L1NpcAction> loadAction(File file, String nodeName)

	throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilder builder = DocumentBuilderFactory.newInstance()
				.newDocumentBuilder();
		Document doc = builder.parse(file);

		if (!doc.getDocumentElement().getNodeName().equalsIgnoreCase(nodeName)) {
			return new ArrayList<L1NpcAction>();
		}
		return L1NpcXmlParser.listActions(doc.getDocumentElement());
	}

	private void loadAction(File file) throws Exception {
		_actions.addAll(loadAction(file, "NpcActionList"));
	}

	private void loadTalkAction(File file) throws Exception {
		_talkActions.addAll(loadAction(file, "NpcTalkActionList"));
	}

	private void loadDirectoryActions(File dir) throws Exception {
		File f = null;
		for (String file : dir.list()) {
			f = new File(dir, file);
			if (FileUtil.getExtension(f).equalsIgnoreCase("xml")) {
				loadAction(f);
				loadTalkAction(f);
			}
		}
	}

	private NpcActionTable() throws Exception {
		File usersDir = new File("./data/xml/NpcActions/users/");
		if (usersDir.exists()) {
			loadDirectoryActions(usersDir);
		}
		loadDirectoryActions(new File("./data/xml/NpcActions/"));
	}

	public static void load() {
		try {
			PerformanceTimer timer = new PerformanceTimer();
			System.out.print("[NpcActionTable] loading npcaction...");
			_instance = new NpcActionTable();
			System.out.println("OK! " + timer.get() + "ms");
		} catch (Exception e) {
			_log.log(Level.SEVERE, "NpcAction를 읽어들일 수 없었습니다", e);
			System.exit(0);
		}
	}

	public static void reload() {
		try {
			NpcActionTable oldInstance = _instance;
			_instance = new NpcActionTable();
			oldInstance._actions.clear();
			oldInstance._talkActions.clear();
		} catch (Exception e) {
		}
	}

	public static NpcActionTable getInstance() {
		return _instance;
	}

	public L1NpcAction get(String actionName, L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _actions) {
			if (action.acceptsRequest(actionName, pc, obj)) {
				return action;
			}
		}
		return null;
	}

	public L1NpcAction get(L1PcInstance pc, L1Object obj) {
		for (L1NpcAction action : _talkActions) {
			if (action.acceptsRequest("", pc, obj)) {
				return action;
			}
		}
		return null;
	}

	public FastTable<L1NpcAction> getlist(L1PcInstance pc, L1Object obj) {
		FastTable<L1NpcAction> list = new FastTable<L1NpcAction>();
		for (L1NpcAction action : _actions) {
			if (action.acceptsRequest2(null, pc, obj)) {
				if (action instanceof L1NpcListedAction) {
					for (L1NpcMakeItemAction aa : ((L1NpcListedAction) action)
							.MakeItemActionlist()) {
						list.add(aa);
					}
				}
			}
		}
		return list;
	}
}
