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
package l1j.server.server.serverpackets;

import java.util.logging.Logger;

import l1j.server.GameSystem.Gamble.GambleInstance;
import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_WhoCharinfo extends ServerBasePacket {
	private static final String S_WHO_CHARINFO = "[S] S_WhoCharinfo";
	private static Logger _log = Logger
			.getLogger(S_WhoCharinfo.class.getName());

	private byte[] _byte = null;

	public S_WhoCharinfo(L1PcInstance pc) {
		_log.fine("Who charpack for : " + pc.getName());

		String lawfulness = "";
		int lawful = pc.getLawful();
		if (lawful < 0) {
			lawfulness = "\\fW(Chaotic) ";
		} else if (lawful >= 0 && lawful < 500) {
			lawfulness = "\\fW(Neutral) ";
		} else if (lawful >= 500) {
			lawfulness = "\\fW(Lawful) ";
		}

		writeC(Opcodes.S_MESSAGE);
		writeC(0x08);

		String title = "";
		String clan = "";

		if (pc.getTitle().equalsIgnoreCase("") == false) {
			title = "\\aD\"" + pc.getTitle() + "\" ";
		}

		if (pc.getClanid() > 0) {
			clan = "[" + pc.getClanname() + "]";
		}
		// //////////Å³¼öº¸ÀÌ°ÔÇÏ±â--------
		writeS(title + pc.getName() + " " + lawfulness + " " + clan + "\n\r"/*
																			 * +
																			 * "\\fU·¦:"
																			 * +
																			 * pc
																			 * .
																			 * getLevel
																			 * (
																			 * )
																			 */
				+ "\\fV KILL:" + pc.getKills() + "\\fY DEATH:" + pc.getDeaths());
		writeD(0);
	}

	public S_WhoCharinfo(L1NpcInstance pc) {
		_log.fine("Who charpack for : " + pc.getName());

		String lawfulness = "";
		int lawful = pc.getLawful();
		if (lawful < 0) {
			lawfulness = "\\fW(Chaotic) ";
		} else if (lawful >= 0 && lawful < 500) {
			lawfulness = "\\fW(Neutral) ";
		} else if (lawful >= 500) {
			lawfulness = "\\fW(Lawful) ";
		}

		writeC(Opcodes.S_MESSAGE);
		writeC(0x08);

		String title = "";
		String clan = "";

		if (pc instanceof GambleInstance) {
			clan = ((GambleInstance) pc).getClanname();
			clan = clan == null ? "" : ("\\aE[" + clan + "]");
		}

		if (pc.getTitle().equalsIgnoreCase("") == false) {
			title = "\\aD\"" + pc.getTitle() + "\" ";
		}

		writeS("\\aA" + pc.getName() + lawfulness + title + clan/*
																 * +" "+"\n\r\\fY"
																 * +
																 * "Kill : "+pc.
																 * getKills ()+
																 * "  Death : "
																 * +
																 * pc.getDeaths(
																 * )+"  ½Â·ü = "
																 * +String
																 * .format
																 * ("%.2f"
																 * ,winner)
																 */);
		// writeD(0x80157FE4);
		writeD(0);
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = _bao.toByteArray();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_WHO_CHARINFO;
	}
}
