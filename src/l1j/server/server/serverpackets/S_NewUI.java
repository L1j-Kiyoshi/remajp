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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;

@SuppressWarnings("unused")
public class S_NewUI extends ServerBasePacket {
	public static final int 패시브추가 = 0x92;
	public static final int 모르는 = 0xb5;
	public static final int 모르는2 = 0xcf;
	public static final int 모르는3 = 0x4e;
	public static final int 혈맹관련 = 0x45;
	public static final int 혈맹 = 0x19;
	public static final int 출첵타이머 = 0x13;
	public static final int 출첵타이머2 = 0x16;
	public static final int 출첵타이머3 = 0x14;

	public static final int 표식 = 0x53;

	public static final int 활력버프 = 0x6e;
	
	public static final int 장비창 = 0x20;

	public static final int ICON_BUFF = 0x6e;
	

	public S_NewUI(int i, int t, int o, int gf, int ms) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(ICON_BUFF);
		writeC(0x00);
		writeC(0x08);
		writeC(0x02);
		writeC(0x10);
		write7B(i);
		writeC(0x18);
		write7B(t);
		writeC(0x20);
		writeC(0x09);
		writeC(0x28);
		write7B(gf);
		writeC(0x30);
		writeC(0x00);
		writeC(0x38);
		writeC(o);
		writeC(0x40);
		write7B(ms);
		writeC(0x48);
		writeC(0x00);
		writeC(0x50);
		writeC(0x00);
		writeC(0x58);
		writeC(0x01);
		writeH(0);
	}

	/**
	 * @param 크래쉬
	 *            ,퓨리,슬레이어,아머가드,타이탄:락,타이탄:블릿,타이탄:매직
	 */
	String 장 = "03 08 00"
			+ "	12 02 08 00 "
			+ "	12 02 08 01 "
			+ "	18 02 20 46 08 95 ";

	String 비 = "03 08 01"
			+ "	12 02 08 00 "
			+ "	12 02 08 01 "
			+ "	18 02 20 46 08 95 ";

	public S_NewUI(int subcode, boolean k, boolean c) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(subcode);
		StringTokenizer st = new StringTokenizer("");
		if (c) {
			if (subcode == 장비창) {
				if (k) {
					st = new StringTokenizer(장.toString());
				}else{
					st = new StringTokenizer(비.toString());
				}
			}
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}
		}
	}
		public S_NewUI(int type, int fa, L1PcInstance pc) {
			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeC(type);
			StringTokenizer st = new StringTokenizer("".toString());
			switch (type) {
			case 장비창: {
				//13 20 03 08 01 82 3C
				writeC(3);
				writeC(8);
				writeC(fa);			
				writeH(0);
			}
			break;
			}
		}

	private static final String 출첵패킷2 = "02 08 00 10 d0 ca bf a6 05 18 00 20 ff ff "
			// + "ff 1c 28 01 30 00 7e a6";
			+ "ff ff ff ff ff ff ff 01 28 01 30 01 38 01 f9 5f";

	private static final String 출첵패킷3 = "02 " + "0a 04 08 17 10 02 "
			+ "0a 04 08 05 10 02 " + "0a 04 08 1c 10 02 "
			+ "0a 04 08 0a 10 02 " + "0a 04 08 21 10 02 "
			+ "0a 04 08 0f 10 02 " + "0a 04 08 26 10 02 "
			+ "0a 04 08 14 10 02 " + "0a 04 08 02 10 02 "
			+ "0a 04 08 19 10 02 " + "0a 04 08 07 10 02 "
			+ "0a 04 08 1e 10 02 " + "0a 04 08 0c 10 02 "
			+ "0a 04 08 23 10 02 " + "0a 04 08 11 10 02 "
			+ "0a 04 08 28 10 02 " + "0a 04 08 16 10 02 "
			+ "0a 04 08 04 10 02 " + "0a 04 08 1b 10 02 "
			+ "0a 04 08 09 10 02 " + "0a 04 08 20 10 02 "
			+ "0a 04 08 0e 10 02 " + "0a 04 08 25 10 02 "
			+ "0a 04 08 13 10 02 " + "0a 04 08 2a 10 02 "
			+ "0a 04 08 01 10 02 " + "0a 04 08 18 10 02 "
			+ "0a 04 08 06 10 02 " + "0a 04 08 1d 10 02 "
			+ "0a 04 08 0b 10 02 " + "0a 04 08 22 10 02 "
			+ "0a 04 08 10 10 02 " + "0a 04 08 27 10 02 "
			+ "0a 04 08 15 10 02 " + "0a 04 08 03 10 02 "
			+ "0a 04 08 1a 10 02 " + "0a 04 08 08 10 02 "
			+ "0a 04 08 1f 10 02 " + "0a 04 08 0d 10 02 "
			+ "0a 04 08 24 10 02 " + "0a 04 08 12 10 02 "
			+ "0a 04 08 29 10 02 " + "00 00";

	private static final String 출첵패킷 = "02 08 90 1c 10 d0 e9 dc a4 05 18 80 a3 05 "
			+ "20 01 28 03 32 43 08 17 12 3f 08 02 10 d3 61 18 "
			+ "03 22 14 67 72 6f 77 74 68 20 63 72 79 73 74 61 "
			+ "6c 20 70 69 65 63 65 28 00 30 af 10 38 01 42 06 "
			+ "24 31 32 38 32 39 4a 06 17 14 00 00 00 00 50 87 "
			+ "ff ff ff ff ff ff ff ff 01 32 44 08 05 12 40 08 "
			+ "02 10 f1 6d 18 03 22 0e 70 73 79 20 73 6f 66 74 "
			+ "20 64 72 69 6e 6b 28 00 30 bc 26 38 01 42 06 24 "
			+ "31 30 39 33 37 4a 0d 15 78 00 03 02 00 00 00 3d "
			+ "e0 33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 32 "
			+ "4b 08 1c 12 47 08 02 10 95 75 18 01 22 15 66 61 "
			+ "6e 74 61 73 79 20 63 72 79 73 74 61 6c 20 70 69 "
			+ "65 63 65 28 00 30 aa 10 38 01 42 0d 24 31 37 35 "
			+ "33 31 20 24 31 37 38 30 31 4a 06 17 14 00 00 00 "
			+ "00 50 87 ff ff ff ff ff ff ff ff 01 32 42 08 0a "
			+ "12 3e 08 02 10 c8 1f 18 64 22 0e 67 6d 20 70 6f "
			+ "74 69 6f 6e 20 31 34 74 68 28 33 30 c1 26 38 01 "
			+ "42 06 24 31 30 39 33 35 4a 0b 17 13 00 00 00 00 "
			+ "3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 "
			+ "32 3f 08 21 12 3b 08 02 10 ea 6b 18 01 22 10 62 "
			+ "61 67 20 6f 66 20 73 61 6e 64 20 77 6f 72 6d 28 "
			+ "00 30 95 09 38 01 42 06 24 31 34 32 39 30 4a 06 "
			+ "17 04 00 00 00 00 50 87 ff ff ff ff ff ff ff ff "
			+ "01 32 3f 08 0f 12 3b 08 02 10 80 7a 18 14 22 0b "
			+ "64 72 75 77 61 20 63 61 6e 64 79 28 33 30 8e 1a "
			+ "38 01 42 06 24 31 30 39 34 36 4a 0b 17 03 00 00 "
			+ "00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff "
			+ "ff 01 32 43 08 26 12 3f 08 02 10 b8 7b 18 01 22 "
			+ "0f 65 76 20 69 76 6f 72 79 20 63 68 61 72 67 65 "
			+ "28 00 30 e0 0f 38 01 42 06 24 32 30 34 35 35 4a "
			+ "0b 17 07 00 00 00 00 3d e0 33 4d cb 50 97 ff ff "
			+ "ff ff ff ff ff ff 01 32 41 08 14 12 3d 08 02 10 "
			+ "c8 20 18 01 22 13 72 75 62 79 20 6f 66 20 64 72 "
			+ "61 67 6f 6e 20 32 30 30 39 28 33 30 8d 1d 38 01 "
			+ "42 05 24 37 39 37 31 4a 06 17 14 00 00 00 00 50 "
			+ "97 ff ff ff ff ff ff ff ff 01 32 44 08 02 12 40 "
			+ "08 02 10 f1 6d 18 03 22 0e 70 73 79 20 73 6f 66 "
			+ "74 20 64 72 69 6e 6b 28 00 30 bc 26 38 01 42 06 "
			+ "24 31 30 39 33 37 4a 0d 15 78 00 03 02 00 00 00 "
			+ "3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 "
			+ "32 40 08 19 12 3c 08 02 10 c6 7b 18 01 22 0c 69 "
			+ "63 65 20 74 65 61 72 20 62 61 67 28 00 30 be 07 "
			+ "38 01 42 06 24 32 30 34 37 39 4a 0b 17 07 00 00 "
			+ "00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff "
			+ "ff 01 32 3f 08 07 12 3b 08 02 10 80 7a 18 05 22 "
			+ "0b 64 72 75 77 61 20 63 61 6e 64 79 28 33 30 8e "
			+ "1a 38 01 42 06 24 31 30 39 34 36 4a 0b 17 03 00 "
			+ "00 00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff "
			+ "ff ff 01 32 4b 08 1e 12 47 08 02 10 95 75 18 01 "
			+ "22 15 66 61 6e 74 61 73 79 20 63 72 79 73 74 61 "
			+ "6c 20 70 69 65 63 65 28 00 30 aa 10 38 01 42 0d "
			+ "24 31 37 35 33 31 20 24 31 37 38 30 31 4a 06 17 "
			+ "14 00 00 00 00 50 87 ff ff ff ff ff ff ff ff 01 "
			+ "32 45 08 0c 12 41 08 02 10 ee 6d 18 0a 22 0f 70 "
			+ "73 79 20 73 70 69 63 79 20 72 61 6d 65 6e 28 00 "
			+ "30 be 26 38 01 42 06 24 31 30 39 33 36 4a 0d 15 "
			+ "78 00 03 02 00 00 00 3d e0 33 4d cb 50 97 ff ff "
			+ "ff ff ff ff ff ff 01 32 3e 08 23 12 3a 08 02 10 "
			+ "bd 6f 18 01 22 0f 6b 69 72 74 61 73 20 73 69 6e "
			+ "69 73 74 65 72 28 00 30 93 2c 38 01 42 06 24 31 "
			+ "35 33 38 34 4a 06 17 0e 00 00 00 00 50 87 ff ff "
			+ "ff ff ff ff ff ff 01 32 48 08 11 12 44 08 02 10 "
			+ "84 1a 18 03 22 10 62 6d 20 6d 61 67 69 63 20 73 "
			+ "63 72 6f 6c 6c 33 28 33 30 ae 2f 38 01 42 05 24 "
			+ "35 38 32 35 4a 10 17 05 00 00 00 00 11 03 18 03 "
			+ "05 03 06 03 23 03 50 97 ff ff ff ff ff ff ff ff "
			+ "01 32 3b 08 28 12 37 08 02 10 b9 77 18 01 22 0c "
			+ "66 69 72 65 20 63 72 79 73 74 61 6c 28 00 30 cc "
			+ "19 38 01 42 06 24 31 38 36 31 37 4a 06 17 15 00 "
			+ "00 00 00 50 83 ff ff ff ff ff ff ff ff 01 32 44 "
			+ "08 16 12 40 08 02 10 c6 20 18 01 22 16 64 69 61 "
			+ "6d 6f 6e 64 20 6f 66 20 64 72 61 67 6f 6e 20 32 "
			+ "30 30 39 28 33 30 89 1d 38 01 42 05 24 37 39 36 "
			+ "39 4a 06 17 14 00 00 00 00 50 97 ff ff ff ff ff "
			+ "ff ff ff 01 32 45 08 04 12 41 08 02 10 ee 6d 18 "
			+ "03 22 0f 70 73 79 20 73 70 69 63 79 20 72 61 6d "
			+ "65 6e 28 00 30 be 26 38 01 42 06 24 31 30 39 33 "
			+ "36 4a 0d 15 78 00 03 02 00 00 00 3d e0 33 4d cb "
			+ "50 97 ff ff ff ff ff ff ff ff 01 32 36 08 1b 12 "
			+ "32 08 02 10 d8 61 18 01 22 07 69 63 71 20 6b 65 "
			+ "79 28 00 30 d7 17 38 01 42 06 24 31 32 38 34 38 "
			+ "4a 06 17 0c 00 00 00 00 50 83 ff ff ff ff ff ff "
			+ "ff ff 01 32 44 08 09 12 40 08 02 10 f1 6d 18 05 "
			+ "22 0e 70 73 79 20 73 6f 66 74 20 64 72 69 6e 6b "
			+ "28 00 30 bc 26 38 01 42 06 24 31 30 39 33 37 4a "
			+ "0d 15 78 00 03 02 00 00 00 3d e0 33 4d cb 50 97 "
			+ "ff ff ff ff ff ff ff ff 01 32 3f 08 20 12 3b 08 "
			+ "02 10 eb 6b 18 01 22 10 62 61 67 20 6f 66 20 61 "
			+ "6e 74 20 71 75 65 65 6e 28 00 30 bb 0e 38 01 42 "
			+ "06 24 31 34 32 38 39 4a 06 17 04 00 00 00 00 50 "
			+ "87 ff ff ff ff ff ff ff ff 01 32 43 08 0e 12 3f "
			+ "08 02 10 c8 1f 18 c8 01 22 0e 67 6d 20 70 6f 74 "
			+ "69 6f 6e 20 31 34 74 68 28 33 30 c1 26 38 01 42 "
			+ "06 24 31 30 39 33 35 4a 0b 17 13 00 00 00 00 3d "
			+ "e0 33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 32 "
			+ "46 08 25 12 42 08 02 10 b7 7b 18 01 22 12 65 76 "
			+ "20 73 6f 75 6c 74 6f 6d 62 20 63 68 61 72 67 65 "
			+ "28 00 30 e0 0f 38 01 42 06 24 32 30 34 35 34 4a "
			+ "0b 17 07 00 00 00 00 3d e0 33 4d cb 50 97 ff ff "
			+ "ff ff ff ff ff ff 01 32 42 08 13 12 3e 08 02 10 "
			+ "b5 7b 18 0a 22 0f 65 76 20 6f 6d 61 6e 20 74 65 "
			+ "6c 62 6f 6f 6b 28 00 30 e3 1e 38 01 42 05 24 39 "
			+ "33 38 31 4a 0b 17 05 00 00 00 00 3d e0 33 4d cb "
			+ "50 97 ff ff ff ff ff ff ff ff 01 32 3e 08 2a 12 "
			+ "3a 08 02 10 8c 6e 18 01 22 0f 70 63 20 69 76 6f "
			+ "72 79 20 65 6c 69 78 69 72 28 00 30 8e 20 38 01 "
			+ "42 06 24 32 30 34 36 32 4a 06 17 13 00 00 00 00 "
			+ "50 93 ff ff ff ff ff ff ff ff 01 32 45 08 01 12 "
			+ "41 08 02 10 ee 6d 18 03 22 0f 70 73 79 20 73 70 "
			+ "69 63 79 20 72 61 6d 65 6e 28 00 30 be 26 38 01 "
			+ "42 06 24 31 30 39 33 36 4a 0d 15 78 00 03 02 00 "
			+ "00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff ff "
			+ "ff 01 32 40 08 18 12 3c 08 02 10 c6 7b 18 01 22 "
			+ "0c 69 63 65 20 74 65 61 72 20 62 61 67 28 00 30 "
			+ "be 07 38 01 42 06 24 32 30 34 37 39 4a 0b 17 07 "
			+ "00 00 00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff "
			+ "ff ff ff 01 32 42 08 06 12 3e 08 02 10 c8 1f 18 "
			+ "32 22 0e 67 6d 20 70 6f 74 69 6f 6e 20 31 34 74 "
			+ "68 28 33 30 c1 26 38 01 42 06 24 31 30 39 33 35 "
			+ "4a 0b 17 13 00 00 00 00 3d e0 33 4d cb 50 97 ff "
			+ "ff ff ff ff ff ff ff 01 32 4b 08 1d 12 47 08 02 "
			+ "10 95 75 18 01 22 15 66 61 6e 74 61 73 79 20 63 "
			+ "72 79 73 74 61 6c 20 70 69 65 63 65 28 00 30 aa "
			+ "10 38 01 42 0d 24 31 37 35 33 31 20 24 31 37 38 "
			+ "30 31 4a 06 17 14 00 00 00 00 50 87 ff ff ff ff "
			+ "ff ff ff ff 01 32 3f 08 0b 12 3b 08 02 10 80 7a "
			+ "18 0a 22 0b 64 72 75 77 61 20 63 61 6e 64 79 28 "
			+ "33 30 8e 1a 38 01 42 06 24 31 30 39 34 36 4a 0b "
			+ "17 03 00 00 00 00 3d e0 33 4d cb 50 97 ff ff ff "
			+ "ff ff ff ff ff 01 32 3d 08 22 12 39 08 02 10 f7 "
			+ "75 18 01 22 0e 62 61 67 20 6f 66 20 61 73 74 61 "
			+ "72 6f 74 28 00 30 a9 10 38 01 42 06 24 31 37 36 "
			+ "35 38 4a 06 17 07 00 00 00 00 50 83 ff ff ff ff "
			+ "ff ff ff ff 01 32 3e 08 10 12 3a 08 02 10 96 1a "
			+ "18 03 22 10 62 6d 20 6c 61 77 66 75 6c 20 74 69 "
			+ "63 6b 65 74 28 00 30 c2 18 38 01 42 05 24 35 38 "
			+ "34 30 4a 06 17 05 00 00 00 00 50 97 ff ff ff ff "
			+ "ff ff ff ff 01 32 46 08 27 12 42 08 02 10 b9 7b "
			+ "18 01 22 0d 65 76 20 67 69 61 6e 74 20 64 6f 6c "
			+ "6c 28 00 30 d8 33 38 01 42 10 24 32 30 34 36 36 "
			+ "20 5b 32 35 39 32 30 30 30 5d 4a 0f 17 08 01 00 "
			+ "00 00 3d e0 33 4d cb 3f 01 24 0a 50 17 32 45 08 "
			+ "15 12 41 08 02 10 c7 20 18 01 22 17 73 61 70 70 "
			+ "68 69 72 65 20 6f 66 20 64 72 61 67 6f 6e 20 32 "
			+ "30 30 39 28 33 30 8f 1d 38 01 42 05 24 37 39 37 "
			+ "30 4a 06 17 14 00 00 00 00 50 97 ff ff ff ff ff "
			+ "ff ff ff 01 32 3f 08 03 12 3b 08 02 10 80 7a 18 "
			+ "03 22 0b 64 72 75 77 61 20 63 61 6e 64 79 28 33 "
			+ "30 8e 1a 38 01 42 06 24 31 30 39 34 36 4a 0b 17 "
			+ "03 00 00 00 00 3d e0 33 4d cb 50 97 ff ff ff ff "
			+ "ff ff ff ff 01 32 40 08 1a 12 3c 08 02 10 c6 7b "
			+ "18 01 22 0c 69 63 65 20 74 65 61 72 20 62 61 67 "
			+ "28 00 30 be 07 38 01 42 06 24 32 30 34 37 39 4a "
			+ "0b 17 07 00 00 00 00 3d e0 33 4d cb 50 97 ff ff "
			+ "ff ff ff ff ff ff 01 32 45 08 08 12 41 08 02 10 "
			+ "ee 6d 18 05 22 0f 70 73 79 20 73 70 69 63 79 20 "
			+ "72 61 6d 65 6e 28 00 30 be 26 38 01 42 06 24 31 "
			+ "30 39 33 36 4a 0d 15 78 00 03 02 00 00 00 3d e0 "
			+ "33 4d cb 50 97 ff ff ff ff ff ff ff ff 01 32 32 "
			+ "08 1f 12 2e 08 02 10 91 78 18 01 22 0c 6f 74 68 "
			+ "65 72 20 73 65 6c 66 20 33 28 44 30 b0 30 38 01 "
			+ "42 06 24 31 38 37 32 34 4a 06 17 03 00 00 00 00 "
			+ "50 17 32 44 08 0d 12 40 08 02 10 f1 6d 18 0a 22 "
			+ "0e 70 73 79 20 73 6f 66 74 20 64 72 69 6e 6b 28 "
			+ "00 30 bc 26 38 01 42 06 24 31 30 39 33 37 4a 0d "
			+ "15 78 00 03 02 00 00 00 3d e0 33 4d cb 50 97 ff "
			+ "ff ff ff ff ff ff ff 01 32 43 08 24 12 3f 08 02 "
			+ "10 b6 7b 18 01 22 0f 65 76 20 67 69 72 61 6e 20 "
			+ "63 68 61 72 67 65 28 00 30 e0 0f 38 01 42 06 24 "
			+ "32 30 34 35 33 4a 0b 17 07 00 00 00 00 3d e0 33 "
			+ "4d cb 50 97 ff ff ff ff ff ff ff ff 01 32 44 08 "
			+ "12 12 40 08 02 10 b4 7b 18 0a 22 10 65 76 20 6a "
			+ "6f 77 6f 6f 20 74 65 6c 62 6f 6f 6b 28 00 30 ff "
			+ "17 38 01 42 06 24 31 35 39 39 34 4a 0b 17 05 00 "
			+ "00 00 00 3d e0 33 4d cb 50 97 ff ff ff ff ff ff "
			+ "ff ff 01 32 44 08 29 12 40 08 02 10 f0 73 18 01 "
			+ "22 10 31 35 74 68 20 72 65 73 63 75 65 20 63 6f "
			+ "69 6e 28 00 30 e2 0f 38 01 42 06 24 31 30 39 33 "
			+ "38 4a 0b 17 05 00 00 00 00 3d e0 33 4d cb 50 97 "
			+ "ff ff ff ff ff ff ff ff 01 31 00";
	
	
	
	  public S_NewUI(L1PcInstance pc, int subcode) {
		    String s = "";
		    if (subcode == 901)
		      s = "09 02 08 00 10 a8 02 6c 72";
		    else if (subcode == 902)
		      s = "07 02 0a 13 08 80 02 10 e4 b1 89 b8 05 22 06 08 01 10 01 18 05 28 01 2b 49";
		    else if (subcode == 903)
		      s = "07 02 0a 13 08 80 02 10 e4 b1 89 b8 05 22 06 08 01 10 02 18 05 28 01 2b 49";
		    else if (subcode == 904)
		      s = "07 02 0a 13 08 80 02 10 e4 b1 89 b8 05 22 06 08 01 10 03 18 05 28 01 2b 49";
		    else if (subcode == 905)
		      s = "07 02 0a 13 08 80 02 10 e4 b1 89 b8 05 22 06 08 01 10 04 18 05 28 01 2b 49";
		    else if (subcode == 906)
		      s = "07 02 0a 13 08 80 02 10 e4 b1 89 b8 05 22 06 08 01 10 05 18 05 28 01 2b 49";
		    else if (subcode == 1001)
		      s = "e5 01 08 04 10 8b 08 18 f8 b9 01 00 00";
		    else if (subcode == 1002)
		      s = "e5 01 08 04 10 8e 08 18 f8 b9 01 00 00";
		    else if (subcode == 1003)
		      s = "e5 01 08 04 10 8e 08 18 f8 b9 01 00 00";
		    else if (subcode == 1004) {
		      s = "0d 02 08 00 10 80 02 00 00";
		    }
		    writeC(223);
		    StringTokenizer st = new StringTokenizer(s);
		    while (st.hasMoreTokens()) {
		      writeC(Integer.parseInt(st.nextToken(), 16));
		    }
		    st = null;
		  }
	
	public S_NewUI(boolean Crash, boolean Purry, boolean Slayer,
			boolean AmorGaurd, boolean TaitanR, boolean TaitanB, boolean TaitanM) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x91);
		writeC(1);
		StringBuilder sb = new StringBuilder();
		if (Crash) {
			sb.append(" 0a 02 08 01 ");
		}
		if (Purry) {
			sb.append(" 0a 02 08 02 ");
		}
		if (Slayer) {
			sb.append(" 0a 02 08 03 ");
		}
		if (AmorGaurd) {
			sb.append(" 0a 04 08 05 10 0a ");
			// writeC(16);writeC(10);
		}
		if (TaitanR) {
			sb.append(" 0a 02 08 06 ");
		}
		if (TaitanB) {
			sb.append(" 0a 02 08 07 ");
		}
		if (TaitanM) {
			sb.append(" 0a 02 08 08 ");
		}

		StringTokenizer st = new StringTokenizer(sb.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
		writeH(0);
	}
	
	public S_NewUI(int Gfxid, boolean on, int sysMsg, long time)	{
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x6E);
		writeC(0x08);
		writeC(on ? 2 : 3);
		writeC(0x10);
		byteWrite(Gfxid);
		writeC(0x18);
		if(time < 0)
		{
			byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0x01 };
			writeByte(minus);
		}
		else
			byteWrite(time);
		writeC(0x20);
		writeC(0x08);
		writeC(0x28);

		byteWrite(Gfxid);

		writeH(0x0030);
		writeC(0x38);
		writeC(0x03);
		writeC(0x40);

		byteWrite(sysMsg);

		writeC(0x48);
		writeC(0x00);
		writeH(0x0050);
		writeC(0x58);
		writeC(0x01);
		writeC(0x60);
		writeC(0x00);
		writeC(0x68);
		writeC(0x00);
		writeC(0x70);
		writeC(0x00);
		writeH(0x00);
	}

	public static final int[] hextable = { 0x80, 0x81, 0x82, 0x83, 0x84, 0x85,
			0x86, 0x87, 0x88, 0x89, 0x8a, 0x8b, 0x8c, 0x8d, 0x8e, 0x8f, 0x90,
			0x91, 0x92, 0x93, 0x94, 0x95, 0x96, 0x97, 0x98, 0x99, 0x9a, 0x9b,
			0x9c, 0x9d, 0x9e, 0x9f, 0xa0, 0xa1, 0xa2, 0xa3, 0xa4, 0xa5, 0xa6,
			0xa7, 0xa8, 0xa9, 0xaa, 0xab, 0xac, 0xad, 0xae, 0xaf, 0xb0, 0xb1,
			0xb2, 0xb3, 0xb4, 0xb5, 0xb6, 0xb7, 0xb8, 0xb9, 0xba, 0xbb, 0xbc,
			0xbd, 0xbe, 0xbf, 0xc0, 0xc1, 0xc2, 0xc3, 0xc4, 0xc5, 0xc6, 0xc7,
			0xc8, 0xc9, 0xca, 0xcb, 0xcc, 0xcd, 0xce, 0xcf, 0xd0, 0xd1, 0xd2,
			0xd3, 0xd4, 0xd5, 0xd6, 0xd7, 0xd8, 0xd9, 0xda, 0xdb, 0xdc, 0xdd,
			0xde, 0xdf, 0xe0, 0xe1, 0xe2, 0xe3, 0xe4, 0xe5, 0xe6, 0xe7, 0xe8,
			0xe9, 0xea, 0xeb, 0xec, 0xed, 0xee, 0xef, 0xf0, 0xf1, 0xf2, 0xf3,
			0xf4, 0xf5, 0xf6, 0xf7, 0xf8, 0xf9, 0xfa, 0xfb, 0xfc, 0xfd, 0xfe,
			0xff };

	private void byteWrite(long value) {
		long temp = value / 128;
		if (temp > 0) {
			writeC(hextable[(int) value % 128]);
			while (temp >= 128) {
				writeC(hextable[(int) temp % 128]);
				temp = temp / 128;
			}
			if (temp > 0)
				writeC((int) temp);
		} else {
			if (value == 0) {
				writeC(0);
			} else {
				writeC(hextable[(int) value]);
				writeC(0);
			}
		}
	}

	private static final String 활력_활력버프1 = "00 08 02 10 " + "f2 "// 버프 종류
			+ "12 18";
	private static final String 활력_활력버프2 = "20 09 28 97 34 30 00 38 00 40 "
			+ "fb 21 "// 버프종류
			+ "48 " + "00 50 00 58 01 60 01 68 e8 21 70 01 45 63";

	private static final String 활력_공격버프1 = "00 08 02 10 f3 12 18";
	private static final String 활력_공격버프2 = "20 09 28 97 34 30 00 38 00 40 fc 21 48 "
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 6d 23";

	private static final String 활력_방어버프1 = "00 08 02 10 f4 12 18";
	private static final String 활력_방어버프2 = "20 09 28 97 34 30 00 38 00 40 fd 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_마법버프1 = "00 08 02 10 f5 12 18";
	private static final String 활력_마법버프2 = "20 09 28 97 34 30 00 38 00 40 fe 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_스턴버프1 = "00 08 02 10 f6 12 18";
	private static final String 활력_스턴버프2 = "20 09 28 97 34 30 00 38 00 40 ff 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_홀드버프1 = "00 08 02 10 f7 12 18";
	private static final String 활력_홀드버프2 = "20 09 28 97 34 30 00 38 00 40 80 22 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";
	
	private static final String 활력_pvp버프1 = "00 08 02 10 f1 12 18";
	private static final String 활력_pvp버프2 = "20 09 28 97 34 30 00 38 00 40 fa 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_화령공격1 = "00 08 02 10 f0 12 18";
	private static final String 활력_화령공격2= "20 09 28 97 34 30 00 38 00 40 f9 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_지령공격1 = "00 08 02 10 ef 12 18";
	private static final String 활력_지령공격2= "20 09 28 97 34 30 00 38 00 40 f8 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_풍령공격1 = "00 08 02 10 ee 12 18";
	private static final String 활력_풍령공격2= "20 09 28 97 34 30 00 38 00 40 f7 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_수령공격1 = "00 08 02 10 ed 12 18";
	private static final String 활력_수령공격2= "20 09 28 97 34 30 00 38 00 40 f6 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_정령방어1 = "00 08 02 10 e8 12 18";
	private static final String 활력_정령방어2= "20 09 28 97 34 30 00 38 00 40 f1 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_화령방어1 = "00 08 02 10 e7 12 18";
	private static final String 활력_화령방어2= "20 09 28 97 34 30 00 38 00 40 f0 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_지령방어1 = "00 08 02 10 e6 12 18";
	private static final String 활력_지령방어2= "20 09 28 97 34 30 00 38 00 40 ef 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_풍령방어1 = "00 08 02 10 e5 12 18";
	private static final String 활력_풍령방어2 = "20 09 28 97 34 30 00 38 00 40 ee 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String 활력_수령방어1 = "00 08 02 10 e4 12 18";
	private static final String 활력_수령방어2 = "20 09 28 97 34 30 00 38 00 40 ed 21 48 "//
			+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";
	
	private static final String N샵버프_완력1 = "00 08 02 10 e9 12 18";
	private static final String N샵버프_완력2 = "20 09 28 97 34 30 00 38 00 40 f2 21 48 "
						+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";
	
	private static final String N샵버프_민첩1 = "00 08 02 10 ea 12 18";
	private static final String N샵버프_민첩2 = "20 09 28 97 34 30 00 38 00 40 f3 21 48 "
						+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String N샵버프_지식1 = "00 08 02 10 eb 12 18";
	private static final String N샵버프_지식2 = "20 09 28 97 34 30 00 38 00 40 f4 21 48 "
						+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";

	private static final String N샵버프_지혜1 = "00 08 02 10 ec 12 18";
	private static final String N샵버프_지혜2 = "20 09 28 97 34 30 00 38 00 40 f5 21 48 "
						+ "00 50 00 58 01 60 01 68 e8 21 70 01 d4 4c";


	public S_NewUI(String 활력코드, long 시간) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(활력버프);

		String 활력버프패킷 = "";
		if (활력코드.equals("활력")) {
			활력버프패킷 = 활력_활력버프1;
		} else if (활력코드.equals("공격")) {
			활력버프패킷 = 활력_공격버프1;
		} else if (활력코드.equals("방어")) {
			활력버프패킷 = 활력_방어버프1;
		} else if (활력코드.equals("마법")) {
			활력버프패킷 = 활력_마법버프1;
		} else if (활력코드.equals("스턴")) {
			활력버프패킷 = 활력_스턴버프1;
		} else if (활력코드.equals("홀드")) {
			활력버프패킷 = 활력_홀드버프1;
		} else if (활력코드.equals("정방")) {
			활력버프패킷 = 활력_정령방어1;
		} else if (활력코드.equals("화방")) {
			활력버프패킷 = 활력_화령방어1;
		} else if (활력코드.equals("지방")) {
			활력버프패킷 = 활력_지령방어1;
		} else if (활력코드.equals("수방")) {
			활력버프패킷 = 활력_수령방어1;
		} else if (활력코드.equals("풍방")) {
			활력버프패킷 = 활력_풍령방어1;
		} else if (활력코드.equals("PVP")) {
			활력버프패킷 = 활력_pvp버프1;
		} else if (활력코드.equals("화공")) {
			활력버프패킷 = 활력_화령공격1;
		} else if (활력코드.equals("지공")) {
			활력버프패킷 = 활력_지령공격1;
		} else if (활력코드.equals("수공")) {
			활력버프패킷 = 활력_수령공격1;
		} else if (활력코드.equals("풍공")) {
			활력버프패킷 = 활력_풍령공격1;
		} else if (활력코드.equals("완력")) {
			활력버프패킷 = N샵버프_완력1;
		} else if (활력코드.equals("민첩")) {
			활력버프패킷 = N샵버프_민첩1;
		} else if (활력코드.equals("지식")) {
			활력버프패킷 = N샵버프_지식1;
		} else if (활력코드.equals("지혜")) {
			활력버프패킷 = N샵버프_지혜1;
		}

		StringTokenizer st = new StringTokenizer(활력버프패킷.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}

		byteWrite(시간 / 1000);

		if (활력코드.equals("활력")) {
			활력버프패킷 = 활력_활력버프2;
		} else if (활력코드.equals("공격")) {
			활력버프패킷 = 활력_공격버프2;
		} else if (활력코드.equals("방어")) {
			활력버프패킷 = 활력_방어버프2;
		} else if (활력코드.equals("마법")) {
			활력버프패킷 = 활력_마법버프2;
		} else if (활력코드.equals("스턴")) {
			활력버프패킷 = 활력_스턴버프2;
		} else if (활력코드.equals("홀드")) {
			활력버프패킷 = 활력_홀드버프2;
		} else if (활력코드.equals("정방")) {
			활력버프패킷 = 활력_정령방어2;
		} else if (활력코드.equals("화방")) {
			활력버프패킷 = 활력_화령방어2;
		} else if (활력코드.equals("지방")) {
			활력버프패킷 = 활력_지령방어2;
		} else if (활력코드.equals("수방")) {
			활력버프패킷 = 활력_수령방어2;
		} else if (활력코드.equals("풍방")) {
			활력버프패킷 = 활력_풍령방어2;
		} else if (활력코드.equals("PVP")) {
			활력버프패킷 = 활력_pvp버프2;
		} else if (활력코드.equals("화공")) {
			활력버프패킷 = 활력_화령공격2;
		} else if (활력코드.equals("지공")) {
			활력버프패킷 = 활력_지령공격2;
		} else if (활력코드.equals("수공")) {
			활력버프패킷 = 활력_수령공격2;
		} else if (활력코드.equals("풍공")) {
			활력버프패킷 = 활력_풍령공격2;
		} else if (활력코드.equals("완력")) {
			활력버프패킷 = N샵버프_완력2;
		} else if (활력코드.equals("민첩")) {
			활력버프패킷 = N샵버프_민첩2;
		} else if (활력코드.equals("지식")) {
			활력버프패킷 = N샵버프_지식2;
		} else if (활력코드.equals("지혜")) {
			활력버프패킷 = N샵버프_지혜2;
		}
		st = new StringTokenizer(활력버프패킷.toString());
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}


	public S_NewUI(int subcode) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(subcode);
		StringTokenizer st = new StringTokenizer(출첵패킷.toString());
		if (subcode == 출첵타이머) {
			st = new StringTokenizer(출첵패킷.toString());
		} else if (subcode == 출첵타이머2) {
			st = new StringTokenizer(출첵패킷2.toString());
		} else if (subcode == 출첵타이머3) {
			st = new StringTokenizer(출첵패킷3.toString());
			// }else if( subcode == 활력버프){
			// st = new StringTokenizer(활력버프패킷.toString());
		}

		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}

	// 0000: 41 53 01 08
	/*
	 * f9 94 df 5d
	 * 
	 * 10 02
	 * 
	 * de c7 AS.....]....
	 */



	public S_NewUI(int subcode, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(subcode);
		switch (subcode) {
		case 표식:
			writeC(0x01);
			writeC(0x08);
			byteWrite(pc.getId());
			writeC(0x10);// 모름
			writeC(pc.get표식());// 모름
			writeH(0);// 모름
			break;
	/*	case 종료:
			writeC(0x03);
			byte[] 기감 = "$12125".getBytes();
			int time = pc.던전시간체크("기감");
			writeC(0x0a);
			writeC(0x10);
			writeC(0x08);
			writeC(0x01);
			writeC(0x12);
			writeC(기감.length);
			writeByte(기감);
			writeC(0x18);
			byteWrite(time);
			writeC(0x20);
			byteWrite(7200);

			byte[] 상아탑 = "$6081".getBytes();
			time = pc.던전시간체크("상아탑");
			writeC(0x0a);
			writeC(0x0f);
			writeC(0x08);
			writeC(0x02);
			writeC(0x12);
			writeC(상아탑.length);
			writeByte(상아탑);
			writeC(0x18);
			byteWrite(time);
			writeC(0x20);
			byteWrite(3600);

			byte[] 상아탑야히 = "$13527".getBytes();
			time = pc.던전시간체크("상아탑야히");
			writeC(0x0a);
			writeC(0x10);
			writeC(0x08);
			writeC(0x0f);
			writeC(0x12);
			writeC(상아탑야히.length);
			writeByte(상아탑야히);
			writeC(0x18);
			byteWrite(time);
			writeC(0x20);
			byteWrite(3600);

			byte[] 정무 = "$19375".getBytes();
			time = pc.던전시간체크("정무");
			writeC(0x0a);
			writeC(0x11);
			writeC(0x08);
			writeC(0xf4);
			writeC(0x03);
			writeC(0x12);
			writeC(정무.length);
			writeByte(정무);
			writeC(0x18);
			byteWrite(time);
			writeC(0x20);
			byteWrite(1800);

			writeH(0x00); 
			break; */
		}
	}


	public S_NewUI(int subcode, String clanname, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(subcode);
		switch (subcode) {
		case 혈맹:
			writeC(0x02);
			writeC(0x0a);
			writeC(clanname.getBytes().length);
			writeSS2(clanname);
			writeC(0x10);// 모름
			writeC(pc.getClanRank());// 모름
			writeH(0);// 모름
			break;
		case 혈맹관련:
			writeC(0x01);
			writeC(0x08);
			writeC(0x02);
			writeH(0);// 모름
			break;
		}

	}

	public S_NewUI(int subcode, int t) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(subcode);
		switch (subcode) {
		case 패시브추가:
			writeC(1);
			writeC(8);// 모름
			writeC(t);
			if (t == 5) {
				writeC(0x10);
				writeC(0x0a);
			}
			if (t == 38) {
				writeC(0x18);
				writeC(0x00);
			}
			writeH(0);
			break;

		case 모르는2:
			// 0000: 1d cf 01 08 80 01 10 00 18 00 df 09 ............
			writeC(1);
			writeC(8);// 모름
			writeC(0);
			writeC(1);
			writeC(0x10);
			writeC(0);
			writeC(0x18);
			writeC(0);
			writeH(0);
			break;

		}
	}

	@Override
	public byte[] getContent() {
		return getBytes();
	}
}
