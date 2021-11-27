package l1j.server.server.TimeController;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.TimeZone;

import l1j.server.Config;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_ChangeName;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.utils.SQLUtil;

public class ChatTimeController implements Runnable {

	private static ChatTimeController _instance;

	public static ChatTimeController getInstance() {
		if (_instance == null) {
			_instance = new ChatTimeController();
		}
		return _instance;
	}

	@Override
	public void run() {
		try {
			while (true) {
				Thread.sleep(60000);
				StartChat();
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private Calendar getRealTime() {
		TimeZone _tz = TimeZone.getTimeZone(Config.TIME_ZONE);
		Calendar cal = Calendar.getInstance(_tz);
		return cal;
	}

	private void StartChat() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmm");
		int nowtime = Integer.valueOf(sdf.format(getRealTime().getTime()));
		int chat = 1;
		if (nowtime % chat == 0) {
			String name = rank();
			if (name == null)
				return;
			for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
				if (pc == null)
					continue;
				for (L1Object obj : L1World.getInstance().getVisibleObjects(pc,
						-1))
					if (obj != null && obj instanceof L1NpcInstance) {
						L1NpcInstance npc = (L1NpcInstance) obj;
						if (npc.getNpcTemplate().get_npcId() == 7000066) {// 1위동상
							if (npc.getGfxId().getGfxId() == npc.getGfxId()
									.getTempCharGfx()) {
								npc.setName("\\fH랭킹 알리미");
								npc.setNameId("\\fH현재 서버 1위 [" + name + "]");
								Broadcaster.broadcastPacket(npc,
										new S_ChangeName(npc.getId(),
												"\\fH현재 서버 1위 [" + name + "]"),
										true);
							}
							Broadcaster.broadcastPacket(npc,
									new S_NpcChatPacket(npc, "서버 전체랭킹 1위는 "
											+ name + " 님입니다!!", 2), true);
						}
					}
			}
		}
	}

	private String rank() {
		String name = null;
		Connection con = null;
		Statement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.createStatement();
			rs = pstm
					.executeQuery("SELECT `Exp`,`char_name` FROM `characters` WHERE AccessLevel = 0 ORDER BY `Exp` DESC limit 1");
			if (rs.next()) {
				name = rs.getString("char_name");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs, pstm, con);
		}
		return name;
	}
}
