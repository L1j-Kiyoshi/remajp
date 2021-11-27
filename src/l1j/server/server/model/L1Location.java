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
package l1j.server.server.model;

import java.util.Random;

import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.templates.L1BookMark;
import l1j.server.server.types.Point;

public class L1Location extends Point {
	private static Random _random = new Random(System.nanoTime());
	protected L1Map _map = L1Map.newNull();

	public L1Location() {
		super();
	}

	public L1Location(L1Location loc) {
		this(loc._x, loc._y, loc._map);
	}

	public L1Location(int x, int y, int mapId) {
		super(x, y);
		setMap(mapId);
	}

	public L1Location(int x, int y, L1Map map) {
		super(x, y);
		_map = map;
	}

	public L1Location(Point pt, int mapId) {
		super(pt);
		setMap(mapId);
	}

	public L1Location(Point pt, L1Map map) {
		super(pt);
		_map = map;
	}

	public void set(L1Location loc) {
		_map = loc._map;
		_x = loc._x;
		_y = loc._y;
	}

	public void set(int x, int y, int mapId) {
		set(x, y);
		setMap(mapId);
	}

	public void set(int x, int y, L1Map map) {
		set(x, y);
		_map = map;
	}

	public void set(Point pt, int mapId) {
		set(pt);
		setMap(mapId);
	}

	public void set(Point pt, L1Map map) {
		set(pt);
		_map = map;
	}

	public L1Map getMap() {
		return _map;
	}

	public int getMapId() {
		return _map.getId();
	}

	public void setMap(L1Map map) {
		_map = map;
	}

	public void setMap(int mapId) {
		_map = L1WorldMap.getInstance().getMap((short) mapId);
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof L1Location)) {
			return false;
		}
		L1Location loc = (L1Location) obj;
		return (this.getMap() == loc.getMap()) && (this.getX() == loc.getX())
				&& (this.getY() == loc.getY());
	}

	@Override
	public int hashCode() {
		return 7 * _map.getId() + super.hashCode();
	}

	@Override
	public String toString() {
		return String.format("(%d, %d) on %d", _x, _y, _map.getId());
	}

	public static L1Location BookmarkLoc(L1PcInstance player, L1BookMark bookm) {

		L1Location newLocation = new L1Location();
		L1Location baseLocation = new L1Location();

		baseLocation.set(bookm.getLocX(), bookm.getLocY(), bookm.getMapId());

		int randomX, ran, randomY = 0;

		int newx = bookm.getLocX();
		int newy = bookm.getLocY();
		short mapId = (short) bookm.getMapId();
		L1Map map = baseLocation.getMap();
		int trial = 0;
		int trialLimit = 60;
		int 퓨전 = 0;
		while (true) {
			if (trial >= trialLimit) {
				newLocation.set(newx, newy);
				break;
			}
			trial++;

			if (trial > 1) {
				randomX = _random.nextInt(2);
				randomY = _random.nextInt(2);
				ran = _random.nextInt(100);
				if (ran < 50) {
					newx += randomX;
					newy += randomY;
				} else {
					newx -= randomX;
					newy -= randomY;
				}
			}

			newLocation.set(newx, newy, mapId);

			for (L1Object objs : L1World.getInstance().getVisiblePoint(
					newLocation, 3)) {
				if (!(objs instanceof L1PcInstance)) {
					continue;
				}
				L1PcInstance pc = (L1PcInstance) objs;
				if (pc.getId() != player.getId()
						&& pc.getX() == newLocation.getX()
						&& pc.getY() == newLocation.getY()) {
					퓨전 = 1;
				} else if (pc.getId() == player.getId()
						&& pc.getX() == newLocation.getX()
						&& pc.getY() == newLocation.getY()) {
					퓨전 = 2;
				}
			}
			/*
			 * if(map.isPassable(newx, newy)){ System.out.println("이즈파써블"); }
			 * if(map.isInMap(newx, newy)){ System.out.println("이즈인맵"); } if(퓨전
			 * == 0){ System.out.println("퓨전"); }
			 */
			if (퓨전 == 2) {
				break;
			}
			if (퓨전 == 0 && map.isInMap(newx, newy)
					&& map.isPassable(newx, newy)) {
				break;
			}
			퓨전 = 0;

		}
		return newLocation;
	}

	public static L1Location randomBookmarkLocation(L1BookMark bookm,
			boolean isRandomTeleport) {

		L1Location newLocation = new L1Location();
		L1Location baseLocation = new L1Location();

		baseLocation.set(bookm.getLocX(), bookm.getLocY(), bookm.getMapId());

		int randomX, randomY = 0;

		int newx = bookm.getLocX() - (bookm.getRandomX() / 2);
		int newy = bookm.getLocY() - (bookm.getRandomY() / 2);
		short mapId = (short) bookm.getMapId();
		L1Map map = baseLocation.getMap();

		while (true) {
			randomX = _random.nextInt(bookm.getRandomX()) + 1;
			randomY = _random.nextInt(bookm.getRandomY()) + 1;

			newx += randomX;
			newy += randomY;

			newLocation.set(newx, newy, mapId);

			if (isRandomTeleport) {
				if (L1CastleLocation.checkInAllWarArea(newx, newy, mapId)) {
					continue;
				}

				if (L1HouseLocation.isInHouse(newx, newy, mapId)) {
					continue;
				}
				/** 샌드웜 지역 **/
				if (newx >= 32704 && newx <= 32835 && newy >= 33110
						&& newy <= 33234 && mapId == 4)
					continue;
				
				/** 용의 계곡 던전 1층 **/
				if (mapId == 30 && !(newx >= 32718 && newx <= 32818 && newy >= 32718 && newy <= 32818))
					continue;
				/** 아투바 오크 은신처 2층 **/
				if (mapId == 492 && !(newx >= 32738 && newx <= 32812 && newy >= 32916 && newy <= 32999))
					continue;
				// 버경장
				if ((newx >= 33472 && newx <= 33536)
						&& (newy >= 32838 && newy <= 32876) && mapId == 4)
					continue;
				// 화둥 위
				if ((newx >= 33472 && newx <= 33792) && newy <= 32191
						&& mapId == 4)
					continue;

				if ((newx >= 33464 && newx <= 33531)
						&& (newy >= 33168 && newy <= 33248) && mapId == 4)
					continue;

				if (mapId == 400
						&& ((newx >= 32703 && newx <= 32874 && newy >= 32908 && newy <= 33029) || (newx >= 32729
								&& newx <= 32813 && newy >= 32822 && newy <= 32927))) {
					continue;
				}
				// 용계 행운지역
				if (mapId == 4
						&& ((newx >= 33331 && newx <= 33341 && newy >= 32430 && newy <= 32441)
								|| (newx >= 33258 && newx <= 33267
										&& newy >= 32396 && newy <= 32407)
								|| (newx >= 33388 && newx <= 3397
										&& newy >= 32339 && newy <= 32350) || (newx >= 33443
								&& newx <= 33483 && newy >= 32315 && newy <= 32357))) {
					continue;
				}
			}

			if (map.isInMap(newx, newy) && map.isPassable(newx, newy)) {
				break;
			}

		}
		return newLocation;
	}

	public L1Location randomLocation(int max, boolean isRandomTeleport) {
		return randomLocation(0, max, isRandomTeleport);
	}

	public L1Location randomLocation(int min, int max, boolean isRandomTeleport) {
		return L1Location.randomLocation(this, min, max, isRandomTeleport);
	}

	public static L1Location saburan(L1Map m) {
		L1Location newLocation = null;
		newLocation = new L1Location();

		short mapId = 0;
		mapId = (short) m.getId();
		L1Map map = null;
		map = m;

		newLocation.setMap(map);
		int newX = 0;
		int newY = 0;

		int StartX = 0;
		StartX = map.getX();

		int StartY = 0;
		StartY = map.getY();

		int diffX = 0;
		diffX = map.getWidth();
		int diffY = 0;
		diffY = map.getHeight();
		while (true) {
			newX = StartX + _random.nextInt(diffX + 1);
			newY = StartY + _random.nextInt(diffY + 1);
			newLocation.set(newX, newY);
			if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
				continue;
			}
			if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
				continue;
			}

			if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
				break;
			}
		}
		return newLocation;
	}

	public static L1Location randomLocation(L1Location baseLocation, int min, int max, boolean isRandomTeleport) {
		if (min > max) {
			throw new IllegalArgumentException("min > max가 되는 인수는 무효");
		}
		if (max <= 0) {
			return new L1Location(baseLocation);
		}
		if (min < 0) {
			min = 0;
		}

		L1Location newLocation = new L1Location();
		int newX = 0;
		int newY = 0;
		int locX = baseLocation.getX();
		int locY = baseLocation.getY();
		short mapId = (short) baseLocation.getMapId();
		L1Map map = baseLocation.getMap();

		newLocation.setMap(map);

		int locX1 = locX - max;
		int locX2 = locX + max;
		int locY1 = locY - max;
		int locY2 = locY + max;

		int mapX1 = map.getX();
		int mapX2 = mapX1 + map.getWidth();
		int mapY1 = map.getY();
		int mapY2 = mapY1 + map.getHeight();

		if (locX1 < mapX1) {
			locX1 = mapX1;
		}
		if (locX2 > mapX2) {
			locX2 = mapX2;
		}
		if (locY1 < mapY1) {
			locY1 = mapY1;
		}
		if (locY2 > mapY2) {
			locY2 = mapY2;
		}

		int diffX = locX2 - locX1;
		int diffY = locY2 - locY1;

		int trial = 0;
		int amax = (int) Math.pow(1 + (max * 2), 2);
		int amin = (min == 0) ? 0 : (int) Math.pow(1 + ((min - 1) * 2), 2);
		int trialLimit = 60 * amax / (amax - amin);

		while (true) {
			// System.out.println("최고루프:"+trialLimit+" 현재루프:"+trial);
			if (trial >= trialLimit) {
				newLocation.set(locX, locY);
				break;
			}
			trial++;
			try {
				newX = locX1 + L1Location._random.nextInt(diffX + 1);
				newY = locY1 + L1Location._random.nextInt(diffY + 1);
			} catch (Exception e) {
				// System.out.println("랜덤텔 범위 벗어나는 맵번호 : "+mapId);
			}
			newLocation.set(newX, newY);
			if (baseLocation.getTileLineDistance(newLocation) < min) {
				continue;

			}
			if (isRandomTeleport) {
				if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
					continue;
				}

				if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
					continue;
				}

				/** 샌드웜 지역 **/
				if (newX >= 32704 && newX <= 32835 && newY >= 33110
						&& newY <= 33234 && mapId == 4)
					continue;
				// 버경장
				if ((newX >= 33472 && newX <= 33536)
						&& (newY >= 32838 && newY <= 32876) && mapId == 4)
					continue;
				// 화둥 위
				if ((newX >= 33472 && newX <= 33792) && newY <= 32191
						&& mapId == 4)
					continue;

				if ((newX >= 33464 && newX <= 33531)
						&& (newY >= 33168 && newY <= 33248) && mapId == 4)
					continue;

				if (mapId == 400
						&& ((newX >= 32703 && newX <= 32874 && newY >= 32908 && newY <= 33029) || (newX >= 32729
								&& newX <= 32813 && newY >= 32822 && newY <= 32927))) {
					continue;
				}

				if (mapId == 4
						&& ((newX >= 33331 && newX <= 33341 && newY >= 32430 && newY <= 32441)
								|| (newX >= 33258 && newX <= 33267
										&& newY >= 32396 && newY <= 32407)
								|| (newX >= 33388 && newX <= 33397
										&& newY >= 32339 && newY <= 32350) || (newX >= 33443
								&& newX <= 33483 && newY >= 32315 && newY <= 32357))) {
					continue;
				}
				
				/** 용의 계곡 던전 1층 **/
				if (mapId == 30 && !(newX >= 32718 && newX <= 32818 && newY >= 32718 && newY <= 32818))
					continue;
				/** 아투바 오크 은신처 2층 **/
				if (mapId == 492 && !(newX >= 32738 && newX <= 32812 && newY >= 32916 && newY <= 32999))
					continue;
			}
			if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
				break;
			}
		}
		return newLocation;
	}

	public static L1Location randomRangeLocation(L1Location baseLocation,
			int MaxX, int MaxY, boolean isRandomTeleport) {
		L1Location newLocation = new L1Location();
		int newX = 0;
		int newY = 0;
		int locX = baseLocation.getX();
		int locY = baseLocation.getY();
		short mapId = (short) baseLocation.getMapId();
		L1Map map = baseLocation.getMap();

		newLocation.setMap(map);

		int trial = 0;
		int trialLimit = 50;

		if (MaxX > MaxY)
			trialLimit = MaxX;
		else
			trialLimit = MaxY;

		while (true) {
			if (trial >= trialLimit) {
				newLocation.set(locX, locY);
				break;
			}
			trial++;
			try {
				newX = locX
						+ (L1Location._random.nextInt(MaxX / 2 + 1) + L1Location._random
								.nextInt(MaxX / 2 + 1));
				newY = locY
						+ (L1Location._random.nextInt(MaxY / 2 + 1) + L1Location._random
								.nextInt(MaxY / 2 + 1));
			} catch (Exception e) {
				// System.out.println("랜덤텔 범위 벗어나는 맵번호 : "+mapId);
			}
			newLocation.set(newX, newY);

			if (isRandomTeleport) {
				if (L1CastleLocation.checkInAllWarArea(newX, newY, mapId)) {
					continue;
				}

				if (L1HouseLocation.isInHouse(newX, newY, mapId)) {
					continue;
				}

				/** 샌드웜 지역 **/
				if (newX >= 32704 && newX <= 32835 && newY >= 33110
						&& newY <= 33234)
					continue;
				// 용계 행운지역
				if (mapId == 400
						&& ((newX >= 32703 && newX <= 32874 && newY >= 32908 && newY <= 33029) || (newX >= 32729
								&& newX <= 32813 && newY >= 32822 && newY <= 32927))) {
					continue;
				}
				if (mapId == 4
						&& ((newX >= 33331 && newX <= 33341 && newY >= 32430 && newY <= 32441)
								|| (newX >= 33258 && newX <= 33267
										&& newY >= 32396 && newY <= 32407)
								|| (newX >= 33388 && newX <= 33397
										&& newY >= 32339 && newY <= 32350) || (newX >= 33443
								&& newX <= 33483 && newY >= 32315 && newY <= 32357))) {
					continue;
				}
				
				/** 용의 계곡 던전 1층 **/
				if (mapId == 30 && !(newX >= 32718 && newX <= 32818 && newY >= 32718 && newY <= 32818))
					continue;
				/** 아투바 오크 은신처 2층 **/
				if (mapId == 492 && !(newX >= 32738 && newX <= 32812 && newY >= 32916 && newY <= 32999))
					continue;
			}
			if (map.isInMap(newX, newY) && map.isPassable(newX, newY)) {
				break;
			}
		}
		return newLocation;
	}
}
