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
package l1j.server.server.clientpackets;

import java.math.BigInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class ClientBasePacket {
	private static Logger _log = Logger.getLogger(ClientBasePacket.class.getName());
	private byte _decrypt[];
	private int _off;
	
	public boolean isRead(int size) {
		return _off + size <= _decrypt.length;
	}

	public void clear() {
		_decrypt = null;
		_off = 0;
	}

	/** 인 페킷이있을시 사용 부분 */
	public ClientBasePacket() {}
	
	public ClientBasePacket(byte abyte0[]) {
		_log.finest("type=" + getType() + ", len=" + abyte0.length);
		_decrypt = abyte0;
		_off = 1;
	}
	
	public int read4(int size) {
		if (size == 0) return 0;
		int i = _decrypt[_off++] & 0x7f;
		if (size == 1) return i;
		if (size >= 2) i |= (_decrypt[_off++] << 8 & 0x7f00) >> 1;
		if (size >= 3) i |= (_decrypt[_off++] << 16 & 0x7f0000) >> 2;
		if (size >= 4) i |= (_decrypt[_off++] << 24 & 0x7f000000) >> 3;
		if (size >= 5) i |= ((long) _decrypt[_off++] << 32 & 0x7f00000000L) >> 4;
		return i;
	}

	/**
	 *  4Byte 처리
	 * @return
	 */
	public int readD() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		i |= _decrypt[_off++] << 16 & 0xff0000;
		i |= _decrypt[_off++] << 24 & 0xff000000;
		return i;
	}
	
	public int readD2() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		i |= _decrypt[_off++] << 16 & 0xff0000;
		i |= _decrypt[_off++] << 21 & 0xff000000;
		return i;
	}

	/**
	 *  1Byte 처리
	 * @return
	 */
	public int readC() {
		int i = _decrypt[_off++] & 0xff;
		return i;
	}
	
	public void readP(int a) {
		_off += a;
	}

	public int readKH() {
		int i = (_decrypt[_off++] & 0xff) - 128;
		i |= (_decrypt[_off++] & 0xff) << 7;
		return i;
	}
	
	public int readKCH() {
		int i = (_decrypt[_off++] & 0xff) - 128;
		i |= ((_decrypt[_off++] & 0xff) - 128) << 7;
		i |= (_decrypt[_off++] & 0xff) << 14;
		return i;
	}
	
	public int readK() {
		int i = (_decrypt[_off++] & 0xff) - 128;
		i |= ((_decrypt[_off++] & 0xff) - 128) << 7;
		i |= ((_decrypt[_off++] & 0xff) - 128) << 14;
		i |= (_decrypt[_off++] & 0xff) << 21;
		return i;
	}
	public int readK(int len) {
	    int i = 0;
	    switch (len) {
	    case 1:
	      i = (_decrypt[(_off++)] & 0xFF) - 128;
	      break;
	    case 2:
	      i = (_decrypt[(_off++)] & 0xFF) - 128;
	      i |= (_decrypt[(_off++)] & 0xFF) << 7;
	      break;
	    case 3:
	      i = (_decrypt[(_off++)] & 0xFF) - 128;
	      i |= (_decrypt[(_off++)] & 0xFF) - 128 << 7;
	      i |= (_decrypt[(_off++)] & 0xFF) << 14;
	      break;
	    case 4:
	      i = (_decrypt[(_off++)] & 0xFF) - 128;
	      i |= (_decrypt[(_off++)] & 0xFF) - 128 << 7;
	      i |= (_decrypt[(_off++)] & 0xFF) - 128 << 14;
	      i |= (_decrypt[(_off++)] & 0xFF) << 21;
	      break;
	    case 5:
	      i = (_decrypt[(_off++)] & 0xFF) - 128;
	      i |= (_decrypt[(_off++)] & 0xFF) - 128 << 7;
	      i |= (_decrypt[(_off++)] & 0xFF) - 128 << 14;
	      i |= (_decrypt[(_off++)] & 0x7F) << 21;
	      i |= (_decrypt[(_off++)] & 0xFF) << 28;
	      break;
	    case 6:
	      i = (_decrypt[(_off++)] & 0xFF) - 128;
	      i |= (_decrypt[(_off++)] & 0xFF) - 128 << 7;
	      i |= (_decrypt[(_off++)] & 0xFF) - 128 << 14;
	      i |= (_decrypt[(_off++)] & 0x7F) << 21;
	      i |= (_decrypt[(_off++)] & 0x7F) << 28;
	      i |= (_decrypt[(_off++)] & 0xFF) << 35;
	    }

	    return i;
	  }

	
	/**
	 *  2Byte 처리
	 * @return
	 */
	public int readH() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		return i;
	}

	
	/**
	 *  3Byte 처리
	 * @return
	 */
	public int readCH() {
		int i = _decrypt[_off++] & 0xff;
		i |= _decrypt[_off++] << 8 & 0xff00;
		i |= _decrypt[_off++] << 16 & 0xff0000;
		return i;
	}
	

	public double readF() {
		long l = _decrypt[_off++] & 0xff;
		l |= _decrypt[_off++] << 8 & 0xff00;
		l |= _decrypt[_off++] << 16 & 0xff0000;
		l |= _decrypt[_off++] << 24 & 0xff000000;
		l |= (long) _decrypt[_off++] << 32 & 0xff00000000L;
		l |= (long) _decrypt[_off++] << 40 & 0xff0000000000L;
		l |= (long) _decrypt[_off++] << 48 & 0xff000000000000L;
		l |= (long) _decrypt[_off++] << 56 & 0xff00000000000000L;
		return Double.longBitsToDouble(l);
	}

	public void addoff(byte val) {
		try {
			_off += val;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public int read_size() {
		int i = 0;
		while (true) {
			if ((_decrypt[_off + i] & 0xff) < 0x80) {
				break;
			} else {
				i++;
			}
		}
		return i + 1;
	}
	
	public int read_sizeP() {
		int i = 0;
		while (true) {
			if ((_decrypt[_off + i] & 0xff) < 0xeb) {
				break;
			} else {
				i++;
			}
		}
		return i + 1;
	}
	

	public void 패킷위치변경(byte val) {
		int i = 0;
		try {
			while (_decrypt.length >= _off) {
				if (_decrypt[_off] == val) {
					_off++;
					return;
				}
				_off++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String readS(int len) {
		String s = null;
		try {
			s = new String(_decrypt, _off, len, "MS932");
			_off += s.getBytes("MS932").length;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	public String readS() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, "EUC-KR");
			s = s.substring(0, s.indexOf('\0'));
			_off += s.getBytes("EUC-KR").length + 1;
		} catch (Exception e) {
			// _log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}
	
	public String readSAccount() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, "EUC-KR");
			String target = "?";
			int target_num = s.indexOf(target)+1;
			s = s.substring(target_num, (s.substring(target_num).indexOf("*")+target_num));
			_off += s.getBytes("EUC-KR").length + 1;
		} catch (Exception e) {
			// _log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}

	public String readSPassword() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, "EUC-KR");
			String target = "";
			int target_num = s.indexOf(target)+1;
			s = s.substring(target_num, (s.substring(target_num).indexOf("*")+target_num));
			_off += s.getBytes("EUC-KR").length + 1;
		} catch (Exception e) {
			// _log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}

	public byte[] readSByte() {
		byte[] result = new byte[_decrypt.length - _off];
		try {
			System.arraycopy(_decrypt, _off, result, 0, _decrypt.length - _off);
			int i = 1;
			for (; i < result.length + 1; i++) {
				if (result[i - 1] == 0x00) {
					result = new byte[i];
					System.arraycopy(_decrypt, _off, result, 0, result.length);
					break;
				}
			}
			_off += i;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return result;
	}

	@SuppressWarnings("finally")
	public String readSS() {
		String text = null;
		int loc = 0;
		int start = 0;
		try {
			start = _off;
			while (readH() != 0) {
				loc += 2;
			}
			StringBuffer test = new StringBuffer();
			do {
				if ((_decrypt[start] & 0xff) >= 127 || (_decrypt[start + 1] & 0xff) >= 127) {
					/** 한글 **/
					byte[] t = new byte[2];
					t[0] = _decrypt[start + 1];
					t[1] = _decrypt[start];
					test.append(new String(t, 0, 2, "EUC-KR"));
				} else {
					/** 영문&숫자 **/
					test.append(new String(_decrypt, start, 1, "EUC-KR"));
				}
				start += 2;
				loc -= 2;
			} while (0 < loc);

			text = test.toString();
			test = null;
		} catch (Exception e) {
			text = null;
		} finally {
			return text;
		}
	}

	public byte[] readByte(int len) {
		byte[] result = new byte[len];
		try {
			System.arraycopy(_decrypt, _off, result, 0, len);
			_off += len;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	public byte[] readByte() {
		byte[] result = new byte[_decrypt.length - _off];
		try {
			System.arraycopy(_decrypt, _off, result, 0, _decrypt.length - _off);
			_off = _decrypt.length;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return result;
	}

	public byte[] readPass() {
		byte[] result = new byte[20];
		try {
			System.arraycopy(_decrypt, _off, result, 0, 20);
			_off += 20;
		} catch (Exception e) {
			_log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return result;
	}

	public byte[] readB() {
		byte[] BYTE = new byte[_decrypt.length - _off];
		System.arraycopy(_decrypt, _off, BYTE, 0, BYTE.length);
		_off += (BYTE.length + 1);
		return BYTE;
	}
	
	public byte[] readByteL(int length)
	{	
		byte[] result = new byte[length];
		try
		{
			System.arraycopy(_decrypt, _off, result, 0, length);
			_off += length;
		} catch (Exception e){
		}
		return result;
	}
	
	public int readBit()  {
		int i = 0;
		int j = 0;

		while ((this._decrypt[this._off] & 0xFF) >= 128) {
			i |= (this._decrypt[(this._off++)] & 0xFF ^ 0x80) << 7 * j++;
		}
		return i |= (this._decrypt[(this._off++)] & 0xFF) << 7 * j;
	}
	
	public int readLength() {
		byte[] BYTE = new byte[_decrypt.length - _off];
		return BYTE.length;
	}
	
	protected int getBitSize(int obj) {
		int length = 0;
		if (obj < 0) { 
			BigInteger b = new BigInteger("18446744073709551615");
		 	while (BigInteger.valueOf(obj).and(b).shiftRight((length + 1) * 7).longValue() > 0L) {
		 		length++;
			}
			length++;
		} else if (obj <= 127) {
	    	length = 1;
	    } else if (obj <= 16383) {
	    	length = 2;
	    } else if (obj <= 2097151) {
	    	length = 3;
	    } else if (obj <= 268435455) {
	    	length = 4;
	    } else if (obj <= 34359738367L) {
	    	length = 5;
	    }
		return length;
	}

	/**
	 * 클라이언트 패킷의 종류를 나타내는 캐릭터 라인을 돌려준다. ("[C] C_DropItem" 등 )
	 */
	public String getType() {
		return "[C] " + this.getClass().getSimpleName();
	}



	/**
	 *  변경 : 추억을 되살리며 2007~2019, 고구마팩 최초 로그인 적용.  리마스터버전 수정자  by feel.
	 *  게임도리 닉넴 Baron 로컬 접속기 로그인 구현 시 사용
	 */
	public String readRS() {
		String s = null;
		try {
			s = new String(_decrypt, _off, _decrypt.length - _off, "EUC-KR");// EUC-KR
			s = s.substring(0, s.indexOf('\t'));
			_off += s.getBytes("EUC-KR").length + 1;
		} catch (StringIndexOutOfBoundsException e) {

		} catch (Exception e) {
		// _log.log(Level.SEVERE, "OpCode=" + (_decrypt[0] & 0xff), e);
		}
		return s;
	}


}
