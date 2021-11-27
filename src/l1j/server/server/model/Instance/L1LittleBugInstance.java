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

import java.util.Random;

import l1j.server.server.ObjectIdFactory;
import l1j.server.server.model.L1World;
import l1j.server.server.serverpackets.S_NPCPack;
import l1j.server.server.templates.L1Npc;

public class L1LittleBugInstance extends L1NpcInstance {
	private static final long serialVersionUID = 1L;

	private static final Random _random = new Random();

	private static final int FIRST_NAMEID = 1213;
	public String[] Name = { "배크두", "투투", "버기", "얼루", "제프리", "카이", "아돌프",
			"오버풋", "럼프스", "부카", "그룩", "쿤드라", "쿠마토", "두렉", "그로돈", "퀘니버", "플루토",
			"두리바", "버루얼", "일레자" };
	private static final int[] gfxId = { 3478, 3479, 3480, 3481, 3497, 3498,
			3499, 3500, 3501, 3502, 3503, 3504, 3505, 3506, 3507, 3508, 3509,
			3510, 3511, 3512 };

	public static final int GOOD = 0;
	public static final int NORMAL = 1;
	public static final int BAD = 2;

	// private static final int[] gfxId = { 5919, 6096, 6100, 6443, 6449, 6452,
	// 6477, 6480, 8650, 7047, 7050, 7053, 7257, 8539, 8549, 6483, 8741, 8745,
	// 8751, 10256};

	public L1LittleBugInstance(L1Npc template, int num, int x, int y) {
		super(template);
		setId(ObjectIdFactory.getInstance().nextId());
		setName("$" + Name[num]);
		setNameId("#" + (num + 1) + " $" + Name[num]);
		gfx.setTempCharGfx(gfxId[num]);
		gfx.setGfxId(gfxId[num]);
		setLocation(x, y, 4);
		getMoveState().setHeading(6);
		setCondition(_random.nextInt(3));
		setNumber(num);
		getMoveState().setMoveSpeed(1); // 촐기
		L1World.getInstance().storeObject(this);
		L1World.getInstance().addVisibleObject(this);
		for (L1PcInstance pc : L1World.getInstance().getRecognizePlayer(this)) {
			onPerceive(pc);
		}
	}

	@Override
	public void onPerceive(L1PcInstance perceivedFrom) {
		perceivedFrom.getNearObjects().addKnownObject(this);
		perceivedFrom.sendPackets(new S_NPCPack(this), true);
	}

	private int _number; // 출전번호

	public void setNumber(int i) {
		_number = i;
	}

	public int getNumber() {
		return _number;
	}

	private int _rain; // 레인번호

	public void setRainNum(int i) {
		_rain = i;
	}

	public int getRainNum() {
		return _rain;
	}

	private int _condition; // 상태

	public void setCondition(int i) {
		_condition = i;
	}

	public int getCondition() {
		return _condition;
	}

	private int _win; // 승리 횟수

	public void setWin(int i) {
		_win = i;
	}

	public int getWin() {
		return _win;
	}

	private int _lose; // 패 횟수

	public void setLose(int i) {
		_lose = i;
	}

	public int getLose() {
		return _lose;
	}

	private String _winPoint; // 승률

	public void setWinPoint(String i) {
		_winPoint = i;
	}

	public String getWinPoint() {
		return _winPoint;
	}

	private double _dividend; // 배당

	public void setDividend(double i) {
		_dividend = i;
	}

	public double getDividend() {
		return _dividend;
	}

}
