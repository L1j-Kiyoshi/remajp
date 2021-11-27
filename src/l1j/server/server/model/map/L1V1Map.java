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
package l1j.server.server.model.map;

import l1j.server.GameSystem.Astar.World;
import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.MapFixKeyTable;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.types.Point;

public class L1V1Map extends L1Map {
	private int _mapId;

	private int _worldTopLeftX;

	private int _worldTopLeftY;

	private int _worldBottomRightX;

	private int _worldBottomRightY;

	private byte _map[][];

	public byte _doorMap[][];

	private boolean _isUnderwater;

	private boolean _isMarkable;

	private boolean _isTeleportable;

	private boolean _isEscapable;

	private boolean _isUseResurrection;

	private boolean _isUsePainwand;

	private boolean _isEnabledDeathPenalty;

	private boolean _isTakePets;

	private boolean _isRecallPets;

	private boolean _isUsableItem;

	private boolean _isUsableSkill;

	private static final byte BITFLAG_IS_IMPASSABLE = (byte) 128; // 1000 0000

	protected L1V1Map() {

	}

	public L1V1Map(int mapId, byte map[][], int worldTopLeftX,
			int worldTopLeftY, boolean underwater, boolean markable,
			boolean teleportable, boolean escapable, boolean useResurrection,
			boolean usePainwand, boolean enabledDeathPenalty, boolean takePets,
			boolean recallPets, boolean usableItem, boolean usableSkill) {
		_mapId = mapId;

		_doorMap = new byte[map.length][map[0].length];
		for (int i = 0; i < map.length; ++i) {
			for (int j = 0; j < map[0].length; ++j) {
				_doorMap[i][j] = 0;
			}
		}
		_map = map;
		_worldTopLeftX = worldTopLeftX;
		_worldTopLeftY = worldTopLeftY;

		_worldBottomRightX = worldTopLeftX + map.length - 1;
		_worldBottomRightY = worldTopLeftY + map[0].length - 1;
		// System.out.println("X > "+worldTopLeftX+" X2 > "+_worldBottomRightX+" Y > "+worldTopLeftY+" Y2 > "+_worldBottomRightY+" > "+_doormap.length+" > "+_map_length);

		_isUnderwater = underwater;
		_isMarkable = markable;
		_isTeleportable = teleportable;
		_isEscapable = escapable;
		_isUseResurrection = useResurrection;
		_isUsePainwand = usePainwand;
		_isEnabledDeathPenalty = enabledDeathPenalty;
		_isTakePets = takePets;
		_isRecallPets = recallPets;
		_isUsableItem = usableItem;
		_isUsableSkill = usableSkill;
	}

	public L1V1Map(L1V1Map map) {
		_mapId = map._mapId;

		_map = new byte[map._map.length][];
		for (int i = 0; i < map._map.length; i++) {
			_map[i] = map._map[i].clone();
		}
		_doorMap = new byte[_map.length][_map[0].length];
		for (int i = 0; i < _map.length; ++i) {
			for (int j = 0; j < _map[0].length; ++j) {
				_doorMap[i][j] = 0;
			}
		}
		_worldTopLeftX = map._worldTopLeftX;
		_worldTopLeftY = map._worldTopLeftY;
		_worldBottomRightX = map._worldBottomRightX;
		_worldBottomRightY = map._worldBottomRightY;

		_isUnderwater = map._isUnderwater;
		_isMarkable = map._isMarkable;
		_isTeleportable = map._isTeleportable;
		_isEscapable = map._isEscapable;
		_isUseResurrection = map._isUseResurrection;
		_isUsePainwand = map._isUsePainwand;
		_isEnabledDeathPenalty = map._isEnabledDeathPenalty;
		_isTakePets = map._isTakePets;
		_isRecallPets = map._isRecallPets;
		_isUsableItem = map._isUsableItem;
		_isUsableSkill = map._isUsableSkill;
	}

	public void reset(L1V1Map map) {
		if (map == null || _map.length != map._map.length)
			return;
		for (int i = 0; i < map._map.length; i++) {
			_map[i] = map._map[i].clone();
		}
		for (int i = 0; i < _map.length; ++i) {
			for (int j = 0; j < _map[0].length; ++j) {
				_doorMap[i][j] = 0;
			}
		}
	}

	public L1V1Map clone(int id) {
		L1V1Map map = new L1V1Map(this);
		map._mapId = id;
		return map;
	}

	public int accessTile(int x, int y) {
		if (!isInMap(x, y)) {
			return 0;
		}

		return _map[x - _worldTopLeftX][y - _worldTopLeftY];
	}

	private int accessOriginalTile(int x, int y) {
		return accessTile(x, y) & (~BITFLAG_IS_IMPASSABLE);
	}

	public int gettile(int x, int y) {
		return accessTile(x, y) & (~BITFLAG_IS_IMPASSABLE);
	}

	public void setTile(int x, int y, int tile) {
		if (!isInMap(x, y)) {
			return;
		}
		_map[x - _worldTopLeftX][y - _worldTopLeftY] = (byte) tile;
	}

	public byte[][] getRawTiles() {
		return _map;
	}

	@Override
	public L1V1Map copyMap(int newMapId) {
		return clone(newMapId);
	}

	@Override
	public int getId() {
		return _mapId;
	}

	@Override
	public int getX() {
		return _worldTopLeftX;
	}

	@Override
	public int getY() {
		return _worldTopLeftY;
	}

	@Override
	public int getWidth() {
		return _worldBottomRightX - _worldTopLeftX + 1;
	}

	@Override
	public int getHeight() {
		return _worldBottomRightY - _worldTopLeftY + 1;
	}

	@Override
	public int getTile(int x, int y) {
		short tile = 0;
		try {
			tile = _map[x - _worldTopLeftX][y - _worldTopLeftY];
		} catch (Exception e) {
			// e.printStackTrace();
			// System.out.println(_worldTopLeftX+" "+_worldTopLeftY);
			// System.out.println(x+" "+y +" "+ _mapId);
		}
		if (0 != (tile & BITFLAG_IS_IMPASSABLE)) {
			return 300;
		}
		return accessOriginalTile(x, y);
	}

	@Override
	public int getOriginalTile(int x, int y) {
		return accessOriginalTile(x, y);
	}

	@Override
	public boolean isInMap(Point pt) {
		return isInMap(pt.getX(), pt.getY());
	}

	@Override
	public boolean isInMap(int x, int y) {
		if (_mapId == 4 && (x < 32520 || y < 32070 || (y < 32190 && x < 33950))) {
			return false;
		}
		return (_worldTopLeftX <= x && x <= _worldBottomRightX
				&& _worldTopLeftY <= y && y <= _worldBottomRightY);
	}

	@Override
	public boolean isPassable(Point pt) {
		return isPassable(pt.getX(), pt.getY());
	}

	@Override
	public boolean isPassable(int x, int y) {
		return isPassable(x, y - 1, 4) || isPassable(x + 1, y, 6)
				|| isPassable(x, y + 1, 0) || isPassable(x - 1, y, 2);
	}

	@Override
	public boolean isPassable(Point pt, int heading) {
		return isPassable(pt.getX(), pt.getY(), heading);
	}

	@Override
	public boolean ismPassable(int x, int y, int heading) {
		switch (heading) {
		case 0:
			return (accessTile(x, y) & 8) > 0;
		case 1:
			return ((accessTile(x, y) & 8) > 0 && (accessTile(x, y - 1) & 4) > 0)
					|| ((accessTile(x, y) & 4) > 0 && (accessTile(x + 1, y) & 8) > 0);
		case 2:
			return (accessTile(x, y) & 4) > 0;
		case 3:
			return ((accessTile(x, y + 1) & 8) > 0 && (accessTile(x, y + 1) & 4) > 0)
					|| ((accessTile(x, y) & 4) > 0 && (accessTile(x + 1, y + 1) & 8) > 0);
		case 4:
			return (accessTile(x, y + 1) & 8) > 0;
		case 5:
			return ((accessTile(x, y + 1) & 8) > 0 && (accessTile(x - 1, y + 1) & 4) > 0)
					|| ((accessTile(x - 1, y) & 4) > 0 && (accessTile(x - 1,
							y + 1) & 8) > 0);
		case 6:
			return (accessTile(x - 1, y) & 4) > 0;
		case 7:
			return ((accessTile(x, y) & 8) > 0 && (accessTile(x - 1, y - 1) & 4) > 0)
					|| ((accessTile(x - 1, y) & 4) > 0 && (accessTile(x - 1, y) & 8) > 0);
		}
		return false;
	}

	@Override
	public boolean isPassable(int x, int y, int heading) {
		int tile1 = accessTile(x, y);
		int tile2;

		switch (heading) {
		case 0:
			tile2 = accessTile(x, y - 1);
			break;
		case 1:
			tile2 = accessTile(x + 1, y - 1);
			break;
		case 2:
			tile2 = accessTile(x + 1, y);
			break;
		case 3:
			tile2 = accessTile(x + 1, y + 1);
			break;
		case 4:
			tile2 = accessTile(x, y + 1);
			break;
		case 5:
			tile2 = accessTile(x - 1, y + 1);
			break;
		case 6:
			tile2 = accessTile(x - 1, y);
			break;
		case 7:
			tile2 = accessTile(x - 1, y - 1);
			break;
		default:
			return false;
		}
		if (getId() >= 10010 && getId() <= 10100) {
			if (tile2 == 12) {
				tile2 = 15;
			}
		}

		if (tile2 == 0) {
			return false;
		}

		if ((tile2 & BITFLAG_IS_IMPASSABLE) == BITFLAG_IS_IMPASSABLE) {
			return false;
		}

		switch (heading) {
		case 0: {
			return (tile1 & 0x02) == 0x02;
		}
		case 1: {
			int tile3 = accessTile(x, y - 1);
			int tile4 = accessTile(x + 1, y);
			return (tile3 & 0x01) == 0x01 || (tile4 & 0x02) == 0x02;
		}
		case 2: {
			return (tile1 & 0x01) == 0x01;
		}
		case 3: {
			int tile3 = accessTile(x, y + 1);
			return (tile3 & 0x01) == 0x01;
		}
		case 4: {
			return (tile2 & 0x02) == 0x02;
		}
		case 5: {
			return (tile2 & 0x01) == 0x01 || (tile2 & 0x02) == 0x02;
		}
		case 6: {
			return (tile2 & 0x01) == 0x01;
		}
		case 7: {
			int tile3 = accessTile(x - 1, y);
			return (tile3 & 0x02) == 0x02;
		}
		default:
			break;
		}
		return false;
	}

	@Override
	public void setPassable(Point pt, boolean isPassable) {
		setPassable(pt.getX(), pt.getY(), isPassable);
	}

	@Override
	public void setPassable(int x, int y, boolean isPassable) {
		if (isPassable) {
			setTile(x, y, (short) (accessTile(x, y) & (~BITFLAG_IS_IMPASSABLE)));
		} else {
			setTile(x, y, (short) (accessTile(x, y) | BITFLAG_IS_IMPASSABLE));

		}
	}

	@Override
	public boolean isSafetyZone(Point pt) {
		return isSafetyZone(pt.getX(), pt.getY());
	}

	@Override
	public boolean isSafetyZone(int x, int y) {
		int tile = accessOriginalTile(x, y);

		return (tile & 0x30) == 0x10;
	}

	@Override
	public boolean isCombatZone(Point pt) {
		return isCombatZone(pt.getX(), pt.getY());
	}

	@Override
	public boolean isCombatZone(int x, int y) {
		int tile = accessOriginalTile(x, y);

		return (tile & 0x30) == 0x20;
	}

	@Override
	public boolean isNormalZone(Point pt) {
		return isNormalZone(pt.getX(), pt.getY());
	}

	@Override
	public boolean isNormalZone(int x, int y) {
		int tile = accessOriginalTile(x, y);
		return (tile & 0x30) == 0x00;
	}

	@Override
	public boolean isArrowPassable(Point pt) {
		return isArrowPassable(pt.getX(), pt.getY());
	}

	@Override
	public boolean isArrowPassable(int x, int y) {
		return (accessOriginalTile(x, y) & 0x0e) != 0;
	}

	@Override
	public boolean isArrowPassable(Point pt, int heading) {
		return isArrowPassable(pt.getX(), pt.getY(), heading);
	}

	@Override
	public boolean isArrowPassable(int x, int y, int heading) {
		// int tile1 = accessTile(x, y);
		// int tile2;
		// @SuppressWarnings("unused")
		// int newX;
		// @SuppressWarnings("unused")
		// int newY;
		int gab = getOriginalTile(x, y);
		int gab2;
		switch (heading) {
		case 0:
			// tile2 = accessTile(x, y - 1);
			// newX = x; newY = y - 1;
			gab2 = getOriginalTile(x, y - 1);
			break;
		case 1: // tile2 = accessTile(x + 1, y - 1);
			// newX = x + 1; newY = y - 1;
			gab2 = getOriginalTile(x + 1, y - 1);
			break;
		case 2: // tile2 = accessTile(x + 1, y);
			// newX = x + 1; newY = y;
			gab2 = getOriginalTile(x + 1, y);
			break;
		case 3: // tile2 = accessTile(x + 1, y + 1);
			// newX = x + 1; newY = y + 1;
			gab2 = getOriginalTile(x + 1, y + 1);
			break;
		case 4: // tile2 = accessTile(x, y + 1);
			// newX = x; newY = y + 1;
			gab2 = getOriginalTile(x, y + 1);
			break;
		case 5: // tile2 = accessTile(x - 1, y + 1);
			// newX = x - 1; newY = y + 1;
			gab2 = getOriginalTile(x - 1, y + 1);
			break;
		case 6: // tile2 = accessTile(x - 1, y);
			// newX = x - 1; newY = y;
			gab2 = getOriginalTile(x - 1, y);
			break;
		case 7: // tile2 = accessTile(x - 1, y - 1);
			// newX = x - 1; newY = y - 1;
			gab2 = getOriginalTile(x - 1, y - 1);
			break;
		default:
			return false;
		}

		if (getId() == 0
				&& ((gab == 12 || gab2 == 12) || (gab == 28 || gab2 == 28))) {
			return false;
		}
		/*
		 * if(getId() == 1937){ System.out.println("gap "+gab+" gap2 "+gab2); }
		 */
		if (gab == 12 || gab2 == 12) {
			return true;
		}

		if (World.문이동(x, y, getId(), heading)) {
			return false;
		}
		// boolean tail = World.isThroughAttack(x, y, this.getId(), heading);
		// if(door || !tail){
		// return false;
		// }

		/*
		 * switch(heading){ case 0:{ return (tile1 & 0x08) == 0x08; } case 1:{
		 * int tile3 = accessTile(x, y - 1); int tile4 = accessTile(x + 1, y);
		 * return (tile3 & 0x04) == 0x04 || (tile4 & 0x08) == 0x08; } case 2:{
		 * return (tile1 & 0x04) == 0x04; } case 3:{ int tile3 = accessTile(x, y
		 * + 1); return (tile3 & 0x04) == 0x04; } case 4:{ return (tile2 & 0x08)
		 * == 0x08; } case 5:{ return (tile2 & 0x04) == 0x04 || (tile2 & 0x08)
		 * == 0x08; } case 6:{ return (tile2 & 0x04) == 0x04; } case 7:{ int
		 * tile3 = accessTile(x - 1, y); return (tile3 & 0x08) == 0x08; }
		 * default: break; }
		 */

		switch (heading) {
		case 0:
			return (accessTile(x, y) & 8) > 0;
		case 1:
			return ((accessTile(x, y) & 8) > 0 && (accessTile(x, y - 1) & 4) > 0)
					|| ((accessTile(x, y) & 4) > 0 && (accessTile(x + 1, y) & 8) > 0);
		case 2:
			return (accessTile(x, y) & 4) > 0;
		case 3:
			return ((accessTile(x, y + 1) & 8) > 0 && (accessTile(x, y + 1) & 4) > 0)
					|| ((accessTile(x, y) & 4) > 0 && (accessTile(x + 1, y + 1) & 8) > 0);
		case 4:
			return (accessTile(x, y + 1) & 8) > 0;
		case 5:
			return ((accessTile(x, y + 1) & 8) > 0 && (accessTile(x - 1, y + 1) & 4) > 0)
					|| ((accessTile(x - 1, y) & 4) > 0 && (accessTile(x - 1,
							y + 1) & 8) > 0);
		case 6:
			return (accessTile(x - 1, y) & 4) > 0;
		case 7:
			return ((accessTile(x, y) & 8) > 0 && (accessTile(x - 1, y - 1) & 4) > 0)
					|| ((accessTile(x - 1, y) & 4) > 0 && (accessTile(x - 1, y) & 8) > 0);
		}

		return false;
	}

	@Override
	public boolean isUnderwater() {
		return _isUnderwater;
	}

	@Override
	public boolean isMarkable() {
		return _isMarkable;
	}

	@Override
	public boolean isTeleportable() {
		/*
		 * if(getId() >= 2101 && getId() <= 2151){ return false; }
		 */
		return _isTeleportable;
	}

	@Override
	public boolean isTeleportable(int x, int y) {
		if (getId() == 400
				&& ((x >= 32703 && x <= 32874 && y >= 32908 && y <= 33029) || (x >= 32729
						&& x <= 32813 && y >= 32822 && y <= 32927))) {
			// System.out.println("111111111111111");
			return false;
		}
		return _isTeleportable;
	}

	@Override
	public boolean isEscapable() {
		/*
		 * if(getId() >= 2101 && getId() <= 2151){ return true; }
		 */
		return _isEscapable;
	}

	@Override
	public boolean isUseResurrection() {
		/*
		 * if(getId() >= 2101 && getId() <= 2151){ return true; }
		 */
		return _isUseResurrection;
	}

	@Override
	public boolean isUsePainwand() {
		return _isUsePainwand;
	}

	@Override
	public boolean isEnabledDeathPenalty() {
		/*
		 * if(getId() >= 2101 && getId() <= 2151){ return false; }
		 */
		return _isEnabledDeathPenalty;
	}

	@Override
	public boolean isTakePets() {
		return _isTakePets;
	}

	@Override
	public boolean isRecallPets() {
		return _isRecallPets;
	}

	@Override
	public boolean isUsableItem() {
		/*
		 * if(getId() >= 2101 && getId() <= 2151){ return true; }
		 */
		return _isUsableItem;
	}

	@Override
	public boolean isUsableSkill() {
		/*
		 * if(getId() >= 2101 && getId() <= 2151){ return true; }
		 */
		return _isUsableSkill;
	}

	@Override
	public boolean isFishingZone(int x, int y) {
		return accessOriginalTile(x, y) == 16;
	}

	@Override
	public boolean isExistDoor(int x, int y) {
		for (L1DoorInstance door : DoorSpawnTable.getInstance().getDoorList()) {
			if (_mapId != door.getMapId()) {
				continue;
			}
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				continue;
			}
			if (door.isDead()) {
				continue;
			}
			int leftEdgeLocation = door.getLeftEdgeLocation();
			int rightEdgeLocation = door.getRightEdgeLocation();
			int size = rightEdgeLocation - leftEdgeLocation;
			if (size == 0) {
				if (x == door.getX() && y == door.getY()) {
					return true;
				}
			} else {
				if (door.getDirection() == 0) {
					for (int doorX = leftEdgeLocation; doorX <= rightEdgeLocation; doorX++) {
						if (x == doorX && y == door.getY()) {
							return true;
						}
					}
				} else {
					for (int doorY = leftEdgeLocation; doorY <= rightEdgeLocation; doorY++) {
						if (x == door.getX() && y == doorY) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	@Override
	public boolean isCloseZone(int locX, int locY) {
		int tile = accessOriginalTile(locX, locY);
		// System.out.println("tile : " + tile);
		// 벽인식이라고 체크 될때
		if (tile == 12 || (tile & 0x30) == tile) {
			String key = new StringBuilder().append(_mapId).append(locX)
					.append(locY).toString();
			return !MapFixKeyTable.getInstance().isLockey(key); // key에 없을때
																// true;
		}
		return false;
	}

	@Override
	public String toString(Point pt) {
		return "" + getOriginalTile(pt.getX(), pt.getY());
	}

}
