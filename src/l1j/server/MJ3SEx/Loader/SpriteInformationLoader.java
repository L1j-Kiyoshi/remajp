package l1j.server.MJ3SEx.Loader;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import l1j.server.L1DatabaseFactory;
import l1j.server.MJ3SEx.ActionInformation;
import l1j.server.MJ3SEx.EActionCodes;
import l1j.server.MJ3SEx.MJSprBoundary;
import l1j.server.MJ3SEx.SpriteInformation;
import l1j.server.MJ3SEx.IntervalDecorator.ActionIntervalDecoratorFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeHelper;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParseeFactory;
import l1j.server.MJTemplate.MJArrangeHelper.MJArrangeParser;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.SQLUtil;

public class SpriteInformationLoader {
	private static Integer[] _levelToIdx;

	public static int levelToIndex(int level, int sprId) {
		/*
		 * TODO 이벤트 변신 추가 레벨상관없이 동일한 속도 나오도록 사용시 주석해제 속도 레벨지정
		 */
		MJSprBoundary boundary = MJSprBoundary.get_boundary(sprId);
		if (boundary != null)
			level = boundary.get_boundary_level();
		return _levelToIdx == null ? 0 : _levelToIdx[level];
	}

	private static SpriteInformationLoader _instance;

	public static SpriteInformationLoader getInstance() {
		if (_instance == null)
			_instance = new SpriteInformationLoader();
		return _instance;
	}

	public static void release() {
		if (_instance != null) {
			_instance.clear();
			_instance = null;
		}
	}

	public static void reload() {
		SpriteInformationLoader tmp = _instance;
		_instance = new SpriteInformationLoader().loadSpriteInformation();
		if (tmp != null) {
			tmp.clear();
			tmp = null;
		}
	}

	private HashMap<Integer, SpriteInformation> _sprInfos;
	private HashMap<Integer, Long> _spellInfos;

	private SpriteInformationLoader() {
	}

	private HashMap<Integer, Long> loadSpellDelayInformation() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		HashMap<Integer, Long> sps = null;
		try {
			sps = new HashMap<Integer, Long>(320);
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select skill_id, delay from tb_magicdelay");
			rs = pstm.executeQuery();
			while (rs.next())
				sps.put(new Integer(rs.getInt("skill_id")), rs.getLong("delay"));
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return sps;
	}

	public void reloadSpellDelayInformation() {
		HashMap<Integer, Long> tmp = _spellInfos;
		_spellInfos = loadSpellDelayInformation();
		if (tmp != null) {
			tmp.clear();
			tmp = null;
		}
	}

	public SpriteInformationLoader loadSpriteInformation() {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select spr_id, width, height, action_count from spr_info");
			rs = pstm.executeQuery();
			_sprInfos = new HashMap<Integer, SpriteInformation>(SQLUtil.calcRows(rs));
			while (rs.next()) {
				SpriteInformation sInfo = loadActionInformation(SpriteInformation.create(rs));
				_sprInfos.put(sInfo.getSpriteId(), sInfo);
			}
			loadUserActionInformation();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
			_spellInfos = loadSpellDelayInformation();
		}
		return this;
	}

	private SpriteInformation loadActionInformation(SpriteInformation sInfo) {
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("select act_id, framecount, framerate from spr_action where spr_id=?");
			pstm.setInt(1, sInfo.getSpriteId());
			rs = pstm.executeQuery();
			while (rs.next())
				sInfo.put(ActionInformation.create(rs));

			/*
			 * ActionInformation aInfo = sInfo.get(21); if(aInfo != null &&
			 * aInfo.getFramePerSecond() < 600){ System.out.println(sInfo.getSpriteId() +
			 * " " + aInfo.getFramePerSecond()); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return sInfo;
	}

	private void loadUserActionInformation() {
		try {
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new File("./data/xml/PolyFrame/polyframerate.xml"));

			loadLevelInformation(doc);
			loadActionRates(doc);
			loadExceptionActionRates(doc);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadLevelInformation(Document doc) {
		Integer[] lvlToIdx = new Integer[128];
		Element element = (Element) doc.getElementsByTagName("Level").item(0);
		Integer[] levels = (Integer[]) MJArrangeParser.parsing(element.getAttribute("range"), ",", MJArrangeParseeFactory.createIntArrange()).result();

		for (int i = 0; i < levels.length; i++) {
			if (i + 1 >= levels.length) {
				MJArrangeHelper.<Integer>setArrayValues(lvlToIdx, levels[i], 127, i);
				break;
			}
			MJArrangeHelper.<Integer>setArrayValues(lvlToIdx, levels[i], levels[i + 1] - 1, i);
		}

		_levelToIdx = lvlToIdx;
	}

	private Integer[] loadTargetSprite(Document doc) {
		return (Integer[]) MJArrangeParser.parsing(doc.getElementsByTagName("Target"), MJArrangeParseeFactory.createIntArrange()).result();
	}

	private Integer[] loadExceptionTargetSprite(Document doc) {
		return (Integer[]) MJArrangeParser.parsing(((Element) ((NodeList) doc.getElementsByTagName("Sprite")).item(0)).getAttribute("target"), ",",
				MJArrangeParseeFactory.createIntArrange()).result();
	}

	private void loadActionRates(Document doc) {
		Integer[] targets = loadTargetSprite(doc);
		NodeList nodes = ((Element) ((NodeList) doc.getElementsByTagName("PolyFrameRate")).item(0)).getChildNodes();
		int size = nodes.getLength();
		for (int i = size - 1; i >= 0; --i) {
			Node node = nodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			Element element = (Element) node;
			Double[] rates = (Double[]) MJArrangeParser.parsing(element.getAttribute("rate"), ",", MJArrangeParseeFactory.createFrameRateArrange()).result();
			Integer[] actions = (Integer[]) MJArrangeParser.parsing(element.getAttribute("action"), ",", MJArrangeParseeFactory.createIntArrange()).result();
			for (Integer a : actions) {
				if (EActionCodes.fromInt(a) == null) {
					System.out.println(String.format("invalid action code %d", a));
					continue;
				}

				for (Integer target : targets) {
					SpriteInformation sInfo = get(target);
					if (sInfo == null) {
						System.out.println(String.format("invalid user spirte : %d", target));
					} else {
						sInfo.registerUserActions(a, rates);
					}
				}
			}
		}
	}

	private void loadExceptionActionRates(Document doc) {
		Integer[] targets = loadExceptionTargetSprite(doc);
		NodeList nodes = ((Element) ((NodeList) doc.getElementsByTagName("PolyFrameException")).item(0)).getChildNodes();
		int size = nodes.getLength();
		for (int i = size - 1; i >= 0; --i) {
			Node node = nodes.item(i);
			if (Node.ELEMENT_NODE != node.getNodeType())
				continue;

			if (!node.getNodeName().equalsIgnoreCase("Frame"))
				continue;

			Element element = (Element) node;
			Double[] rates = (Double[]) MJArrangeParser.parsing(element.getAttribute("rate"), ",", MJArrangeParseeFactory.createFrameRateArrange()).result();
			Integer[] actions = (Integer[]) MJArrangeParser.parsing(element.getAttribute("action"), ",", MJArrangeParseeFactory.createIntArrange()).result();

			for (Integer a : actions) {
				for (Integer target : targets) {
					SpriteInformation sInfo = get(target);
					if (sInfo == null) {
						System.out.println(String.format("invalid user exception spirte : %d", target));
					} else {
						sInfo.registerUserActions(a, rates);
					}
				}
			}
			break;
		}
	}

	public SpriteInformation get(int n) {
		SpriteInformation sInfo = _sprInfos.get(n);
		return sInfo == null ? _sprInfos.get(1120) : sInfo;
	}

	public void clear() {
		if (_spellInfos != null) {
			_spellInfos.clear();
			_spellInfos = null;
		}

		Stream<SpriteInformation> stream = createValuesStream();
		if (stream == null)
			return;

		stream.forEach((SpriteInformation sInfo) -> {
			sInfo.dispose();
		});
		_sprInfos.clear();
		_sprInfos = null;
	}

	public long getUseSpellInterval(L1PcInstance pc, int skillId) {
		Long l = _spellInfos.get(skillId);
		if (l == null)
			return 0L;

		return (long) ActionIntervalDecoratorFactory.PC_ATT_DECORATOR.decoration(pc, l);
	}

	public Stream<SpriteInformation> createValuesStream() {
		return _sprInfos == null ? null : _sprInfos.size() > 1024 ? _sprInfos.values().parallelStream() : _sprInfos.values().stream();
	}

	public Stream<Integer> createKeysStream() {
		return _sprInfos == null ? null : _sprInfos.size() > 1024 ? _sprInfos.keySet().parallelStream() : _sprInfos.keySet().stream();
	}
}
