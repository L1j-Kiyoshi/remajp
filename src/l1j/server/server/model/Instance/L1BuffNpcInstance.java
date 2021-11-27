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
package l1j.server.server.model.Instance;

import java.util.Timer;
import java.util.TimerTask;

import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.templates.L1Npc;
import l1j.server.server.utils.FaceToFace;

public class L1BuffNpcInstance extends L1NpcInstance {

	private static final long serialVersionUID = 1L;

	private int _tradeID;
	private boolean _tradeOk;

	public L1BuffNpcInstance(L1Npc template) {
		super(template);
		if (this.getNpcId() == 7000067) { // ¹öÇÁ µô·¯
			TradeTimer treadtimer = new TradeTimer(this);
			treadtimer.begin();
		}
		if (this.getNpcId() == 7000068) { // ÁÖ»çÀ§ µô·¯
			TradeTimer treadtimer = new TradeTimer(this);
			treadtimer.begin();
		}
		if (this.getNpcId() == 7000069) { // ¼Ò¸· µô·¯
			TradeTimer treadtimer = new TradeTimer(this);
			treadtimer.begin();
		}
		if (this.getNpcId() == 7000070) { // ±ºÁÖ¹öÇÁ µô·¯
			TradeTimer treadtimer = new TradeTimer(this);
			treadtimer.begin();
		}
		if (this.getNpcId() == 7000071) { // ¼Ò¸·2 µô·¯
			TradeTimer treadtimer = new TradeTimer(this);
			treadtimer.begin();
		}
		if (this.getNpcId() == 7000078) { // ¹¬ Âî ºü
			TradeTimer treadtimer = new TradeTimer(this);
			treadtimer.begin();

		}
	}

	public class TradeTimer extends TimerTask {
		private final L1BuffNpcInstance _npc;

		public TradeTimer(L1BuffNpcInstance npc) {
			_npc = npc;
		}

		public void begin() {
			Timer timer = new Timer();
			timer.schedule(this, 0, 5000);
		}

		@Override
		public void run() {
			try {
				if (_npc.getTradeID() != 0) {
					return;
				}
				L1PcInstance target = FaceToFace.faceToFaceForNpc(_npc);
				if (target != null) {
					if (!target.isParalyzed() && !target.isGambling() && !target.isGambling3() && !target.isGambling1() && !target.isGambling4()) {
						target.setTradeID(_npc.getId());
						target.sendPackets(new S_Message_YN(252, _npc.getName()), true);
						Thread.sleep(5000L);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onAction(L1PcInstance pc) {
	}

	public int getTradeID() {
		return _tradeID;
	}

	public void setTradeID(int tradeID) {
		_tradeID = tradeID;
	}

	public void setTradeOk(boolean tradeOk) {
		_tradeOk = tradeOk;
	}

	public boolean getTradeOk() {
		return _tradeOk;
	}
}