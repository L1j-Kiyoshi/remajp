package l1j.server.server.serverpackets;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;

import javolution.util.FastTable;
import l1j.server.L1DatabaseFactory;
import l1j.server.GameSystem.UserRanking.UserRankingController;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.NpcActionTable;
import l1j.server.server.model.L1Object;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1NpcInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.npc.action.L1NpcAction;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.templates.L1UserRanking;
import l1j.server.server.utils.CalcStat;
import l1j.server.server.utils.SQLUtil;
import server.LineageClient;

public class S_NewCreateItem extends ServerBasePacket {
	private static final String TAG = "S_NewCreateItem";
	
	private static final String S_NEWCREATEITEM = "[S] S_NewCreateItem";
	private byte[] _byte = null;

	public static final int WINDOW = 0x37;
	public static final int LIST = 0x39;
	public static final int FISH_WINDOW = 0x3F;
	public static final int EMOTICON = 0x40;

	public static final int unk1 = 0x41;

	public static final int CLAN_JOIN_MESSAGE = 0x43;
	public static final int CASTLE_WAR_TIME_END = 0x44;// 아직 미사용
	public static final int CLAN_JOIN_WAIT = 0x45;
	public static final int CASTLE_WAR_TIME = 0x4C;
	public static final int TAM_POINT = 0x01C2;
	public static final int CLAN_JOIN_SETTING = 0x4D;

	public static final int 대성공제작 = 0x5D;
	public static final int 스킬딜 =  0x0f;
	public static final int unknown1 = 0x4E;
	public static final int TamPage = 0xCD;
	public static final int 사망패널티 = 0xCF;
	public static final int 버프창 = 0x6e;
	public static final int 유저랭킹 = 0x88;
	public static final int 유저리스트랭킹 = 0x89;
	public static final int 토마호크_도트 = 0x93;
	public static final int 신규패킷13 = 0x07;

	public static final int 퀘스트대화 = 11;
	public static final int 퀘스트 = 13;
	public static final int 퀘스트2 = 9;
	public static final int 퀘스트3 = 62;
	public static final int 퀘스트4 = 6;
	
	/** 랭킹 상태창 부분 */
	public static final int 상태창랭킹 = 0x03fe;

	/** 로긴시 오는 패킷들 **/

	public static final int 신규패킷2 = 32;

	public static final int 신규패킷3 = 227;
	public static final int 신규패킷4 = 229;
	public static final int 신규패킷5 = 231;
	public static final int 신규패킷6 = 233;
	public static final int 신규패킷7 = 234;
	public static final int 신규패킷8 = 47;
	public static final int 도감 = 0x30;
	public static final int 신규패킷10 = 126;

	public static final int 신규패킷11 = 118;
	public static final int 신규패킷12 = 119;

	public S_NewCreateItem(int type) {
		buildPacket(type);
	}

	public S_NewCreateItem(int type, boolean ck) {
		buildPacket(type, ck);
	}

	private void buildPacket(int type, boolean ck) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {

		case 토마호크_도트:
			// 0000: 06 93 01 08 84 c7 bf 60 27 61 .......`'a
			writeC(1);
			writeC(8);
			writeC(0x84);
			writeC(0xc7);
			writeC(0xbf);
			writeC(0x60);
			writeH(0);
			break;
		case 버프창:
			// 0000: 06 6e 00 08 01 10 ba 04 38 b9 .n......8.
			// 0000: 06 6e 00 08 03 10 ba 04 7a 31 .n......z1
			writeC(0);
			writeC(8);
			if (ck) {
				writeC(1);
			} else {
				writeC(3);
			}
			writeC(0x10);
			writeC(0xba);
			writeC(0x04);
			writeH(0);
			break;
		case CLAN_JOIN_WAIT:
			writeC(1);
			writeC(8);
			writeC(2);
			writeH(0);
			break;
		case 사망패널티://
			if (ck) {
				writeC(1);
				writeC(8);
				writeC(0x80);
				writeC(1);
				writeC(0x10);
				writeC(00);
				writeC(0x18);
				writeC(0);
				writeH(0);
			} else {
				writeC(1);
				writeC(8);
				writeC(0);
				writeC(0x10);
				writeC(0);
				writeC(0x18);
				writeC(0);
				writeH(0);
			}
			break;
		}
	}

	private void buildPacket(int type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);

		switch (type) {
		case 0x91:// 모름
			writeC(01);
			writeC(0x88);
			writeC(0xd4);
			break;
		case unk1:
			writeC(1);
			writeC(8);
			writeC(0x80);
			writeC(0xe1);
			writeC(1);
			writeC(0x10);
			writeC(0xe5);
			writeC(0xe0);
			writeC(1);
			writeC(0x4a);
			writeC(0);
			break;
		case unknown1:
			writeC(1);
			writeC(8);
			writeC(3);
			writeC(0x10);
			writeC(0);
			writeC(0x18);
			writeC(0);
			writeH(0);
			break;

		case WINDOW:
			writeC(0x00);
			writeH(0x0308);
			// writeH(0x4356);
			writeH(0x9D29);
			break;
		case CLAN_JOIN_WAIT:
			writeC(0x01);
			writeH(0x0208);
			int size = 1;

			if (size > 0) {
				Collection<L1PcInstance> list = L1World.getInstance()
						.getAllPlayers();
				int i = 0;
				for (L1PcInstance pc : list) {
					writeC(0x12);
					if (i == 0)
						writeC(39);
					else if (i == 1)
						writeC(38);
					else if (i == 2)
						writeC(40);
					else
						writeC(39);
					/*
					 * else if(i == 3) writeC(39); else if(i == 4) writeC(38);
					 * else if(i == 5) writeC(41);
					 */
					writeC(0x08);
					writeD(0x1203A9A2);
					writeD(0xC5C3B208);
					writeD(0xB7ACC5EB);
					writeH(0x18B4);

					// byteWrite(pc.getId());
					// if(i == 0 || i == 3 || i == 4 || i >= 6){
					if (i == 0 || i >= 3) {
						writeD(0x02D8D1BE);
					} else {
						writeC(0xC3);
						writeH(0x5C8A);
					}
					writeC(0x22);
					byte[] name = pc.getName().getBytes();
					writeC(name.length);
					writeByte(name);
					writeC(0x2A);
					writeC("1".getBytes().length);// name.length);
					writeByte("1".getBytes());
					// byte[] memo = pc.getTitle().getBytes();//임시로 호칭
					// writeC(memo.length);
					// writeByte(memo);
					writeC(0x30);
					writeC(L1World.getInstance().getPlayer(pc.getName()) != null ? 1
							: 0);// 접속중
					writeC(0x38);
					writeC(pc.getType());// 클래스
					i++;
					if (i == 2)
						break;
				}
			}
			writeH(0x00);
			break;
		}
	}

	public S_NewCreateItem(int type, int subtype) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case CLAN_JOIN_MESSAGE:
			writeH(0x0801);
			writeC(subtype);
			writeH(0x00);
			break;
		default:
			break;
		}
	}

	public S_NewCreateItem(int type, L1UserRanking rank) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case 유저리스트랭킹:
			int star = UserRankingController.getInstance().getStarCount(rank.getName());
			byte[] name = rank.getName().getBytes();
			int length = 11 + byteWriteCount(rank.getCurRank())
					+ byteWriteCount(rank.getOldRank())
					+ byteWriteCount(rank.getClassId()) + name.length;
			writeC(0x0A);
			writeC(length);
			writeC(0x08);
			writeC(star);
			writeC(0x10);
			byteWrite(rank.getCurRank());
			writeC(0x18);
			byteWrite(rank.getOldRank());
			writeC(0x20);
			byteWrite(rank.getClassId());
			writeC(0x2A);
			writeC(name.length);
			writeByte(name);
			writeC(0x30);
			writeC(0x00);
			writeC(0x38);
			writeC(0x00);

			L1UserRanking classRank = UserRankingController.getInstance()
					.getClassRank(rank.getClassId(), rank.getName());

			length = 7 + byteWriteCount(classRank.getCurRank())
					+ byteWriteCount(classRank.getOldRank())
					+ byteWriteCount(rank.getClassId()) + name.length;
			writeC(0x12);
			writeC(length);
			writeC(0x08);
			writeC(star);
			writeC(0x10);
			byteWrite(classRank.getCurRank());
			writeC(0x18);
			byteWrite(classRank.getOldRank());
			writeC(0x20);
			byteWrite(rank.getClassId());
			writeC(0x2A);
			writeC(name.length);
			writeByte(name);

			if (rank.getCurRank() != 1) {
				writeC(0x18);
				writeC(0x01);
			}
			writeC(0x20);
			writeC(0x01);
			writeC(0x30);
			writeC(0x01);
			writeH(0x00);
			break;
		}
	}

	public S_NewCreateItem(int type, List<L1UserRanking> list, int classId,
			int totalPage, int curPage) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case 유저랭킹:
			writeH(0x08);
			writeC(0x10);
			byteWrite(System.currentTimeMillis() / 2000);
			writeC(0x18);
			writeC(classId);
			writeC(0x20);
			writeC(totalPage);
			writeC(0x28);
			writeC(curPage);

			for (L1UserRanking user : list) {
				byte[] name = user.getName().getBytes();
				int rank = user.getCurRank();
				int oldRank = user.getOldRank();

				int buffCount = 0;

				if (classId == 9) {
					if (rank >= 1 && rank <= 10)
						buffCount = 11;
					else if (rank >= 11 && rank <= 20)
						buffCount = 10;
					else if (rank >= 21 && rank <= 40)
						buffCount = 9;
					else if (rank >= 41 && rank <= 60)
						buffCount = 8;
					else if (rank >= 61 && rank <= 80)
						buffCount = 7;
					else if (rank >= 81 && rank <= 100)
						buffCount = 6;
					else if (rank >= 101 && rank <= 120)
						buffCount = 5;
					else if (rank >= 121 && rank <= 140)
						buffCount = 4;
					else if (rank >= 141 && rank <= 160)
						buffCount = 3;
					else if (rank >= 161 && rank <= 180)
						buffCount = 2;
					else if (rank >= 181 && rank <= 200)
						buffCount = 1;
				} else {
					buffCount = UserRankingController.getInstance()
							.getStarCount(user.getName());
				}

				int length = 8 + name.length + byteWriteCount(rank)
						+ byteWriteCount(oldRank);
				if(classId !=9){
					length += 2;
				}
				writeC(0x32);
				byteWrite(length);

				writeC(0x08);
				writeC(buffCount);
				writeC(0x10);
				byteWrite(rank);
				writeC(0x18);
				byteWrite(oldRank);
				writeC(0x20);
				writeC(user.getClassId());
				writeC(0x2A);
				writeC(name.length);
				writeByte(name);
				if(classId !=9){
					if(UserRankingController.getInstance().getStarCount(user.getClassId(), user.getName())){
						writeC(0x40);
						writeC(0x0b);
					}else{
						writeC(0x40);
						writeC(0x00);
					}
				}
			}
			writeH(0x00);
			break;
		}
	}
	
	public S_NewCreateItem(int type, L1UserRanking classRank, L1UserRanking rank, boolean Ms) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch(type){
			case 상태창랭킹:
				writeC(0x08);
				if(Ms){
					writeC(0x00);
					/** 오브젝트 정보 갱신 */
					writeC(0x10);
					byteWrite(System.currentTimeMillis() / 1000);
					
					/** 클레스 랭킹 이전 */
					writeC(0x18);
					writeBit(classRank == null ? -1 : classRank.getCurRank());
					
					/** 클레스 랭킹 현제 */
					writeC(0x20);
					writeBit(classRank == null ? -1 : classRank.getOldRank());
					
					/** 전체 랭킹 이전 */
					writeC(0x28);
					writeBit(rank == null ? -1 : rank.getCurRank());
					
					/** 전체 랭킹 현제 */
					writeC(0x30);
					writeBit(rank == null ? -1 : rank.getOldRank());
				}else writeC(0x01);
				writeH(0x00);
				break;
		}
	}

	String TamP = "";
	String tampage = "cd "
			+ "01 0a 1c 08 0e 10 b8 f9 1a 18 00 "// 여법사
			+ "20 00 2a "
			+ "0a "// 이름길이
			+ "c6 c4 c7 c1 b8 ae bf c2 c5 b3 "// 이름
			+ "30 "
			+ "30 "// 레벨
			+ "38 "
			+ "03 "// 클래스
			+ "40 "
			+

			"01 0a 1d 08 0e 10 ba f9 1a 18 00 "// 여요정
			+ "20 00 2a " + "0b " + "4c 69 76 65 6f 72 44 65 61 74 68 "
			+ "30 "
			+ "2f "
			+ "38 "
			+ "02 "
			+ "40 "

			+ "01 0a 1c 08 0e 10 c9 a5 ec 06 18 00 "// 남전사
			+ "20 00 2a " + "09 " + "44 64 64 64 64 64 64 64 33 " + "30 "
			+ "10 "
			+ "38 "
			+ "07 "
			+ "40 "

			+ "00 0a 1e 08 0e 10 bf f9 1a 18 00 "// 남기사
			+ "20 00 2a " + "0c " + "b8 b6 c0 bd b8 b8 ba f1 b4 dc b0 e1 "
			+ "30 " + "0d " + "38 "
			+ "01 "
			+ "40 "

			+ "00 0a 1a 08 0e 10 bb f9 1a 18 00 "// 남요정
			+ "20 00 2a " + "08 " + "bb f3 b4 eb c0 fb c0 ce " + "30 " + "0d "
			+ "38 " + "02 "
			+ "40 "

			+ "00 0a 19 08 0e 10 a6 e1 49 18 00 "// 남군주
			+ "20 00 2a " + "07 " + "59 6f 75 72 6c 69 70 " + "30 " + "0b "
			+ "38 " + "00 " + "40 "

			+ "00 0a 1f 08 0e 10 ad 87 81 07 18 00 "
			+ // 남전사
			"20 00 2a " + "0c " + "41 61 77 65 66 69 6f 70 61 77 65 6a "
			+ "30 " + "01 " + "38 " + "07 " + "40 "

			+ "00 10 03 18 00 20 00 00 00";

	public S_NewCreateItem(int type, long 남은시간, int 방어, boolean ck) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) { 
		case 버프창:// 255 / 65025 / 16581375 / 4228250625 
			writeC(0x00);
			writeC(0x08);
			writeC(0x02);
			writeC(0x10);
			writeC(0xe0);
			writeC(0x11);
			writeC(0x18);
			byteWrite(남은시간 / 1000);
			
			String s="";
			StringTokenizer st;
			switch(방어){
				case 1:
					s = "20 08 28 c9 40 30 00 38 10 40";
					break;
				case 2:
					s = "20 08 28 ca 40 30 00 38 10 40 ";	
					break;
				case 3:
					s = "20 08 28 cb 40 30 00 38 10 40";
					break;
				case 4:
					s = "20 08 28 cc 40 30 00 38 10 40";
					break;
				case 5:
					s = "20 08 28 cd 40 30 00 38 10 40";
					break;
			}
			
			st = new StringTokenizer(s);
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}

			switch(방어){
				case 1:
					writeH(0x20d5);
					break;
				case 2:
					writeH(0x20d6);
					break;
				case 3:
					writeH(0x20d7);
					break;
				case 4:
					writeH(0x27b6);
					break;
				case 5:
					writeH(0x27b7);
					break;				
			}
			s="48 00 50 00 58 01";
			// 48 00 50 00 58 01
			// 48 d5 20 50 00 58 01
			st = new StringTokenizer(s);
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}
			writeH(0);// 시간값46 e7
			// 00 00
		default:
			break;
		}
	}


	private int tamwaitcount(int obj) {
		int count = 0;
		Connection con = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		try {
			con = L1DatabaseFactory.getInstance().getConnection();
			pstm = con.prepareStatement("SELECT * FROM tam WHERE objid = ?"); // 케릭터
			// 테이블에서
			// 군주만
			// 골라와서
			pstm.setInt(1, obj);
			rs = pstm.executeQuery();
			while (rs.next()) {
				count++;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			SQLUtil.close(rs);
			SQLUtil.close(pstm);
			SQLUtil.close(con);
		}
		return count;
	}

	public S_NewCreateItem(String account, int type) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(type);
		switch (type) {
		case TamPage:
			writeC(1);// _sex);//남자여자?
			Connection con = null;
			PreparedStatement pstm = null;
			ResultSet rs = null;

			int _level = 0;
			int _class = 0;
			int _sex = 0;
			String _Name = null;
			Timestamp tamtime = null;
			int _objid = 0;
			int objidcount;
			long time = 0;
			long sysTime = System.currentTimeMillis();
			int tamcount;
			try {
				con = L1DatabaseFactory.getInstance().getConnection();
				pstm = con
						.prepareStatement("SELECT objid, TamEndTime, level, char_name, Type,Sex FROM characters WHERE account_name=? ORDER BY `TamEndTime` DESC, `EXP` DESC");
				pstm.setString(1, account);
				rs = pstm.executeQuery();
				while (rs.next()) {

					tamcount = 0;
					objidcount = 0;
					time = 0;
					tamtime = null;

					_objid = rs.getInt("objid");
					_level = rs.getInt("level");
					_class = rs.getInt("Type");
					_sex = rs.getInt("Sex");
					_Name = rs.getString("char_name");
					tamtime = rs.getTimestamp("TamEndTime");

					if (tamtime != null) {
						if (sysTime < tamtime.getTime()) {
							time = tamtime.getTime() - sysTime;
						}
					}

					// System.out.println(tamwaitcount(_objid));
					/*
					 * 남군주"00 0a 19 08 00 10 a6 e1 49 18 "//남군주 여군주
					 * "00 0a 19 08 00 10 f3 ad 8e 01 18 "//여군주 남기사
					 * "00 0a 1e 08 00 10 bf f9 1a 18 "//남기사 여기사
					 * "01 0a 1d 08 00 10 e0 ed 81 07 18 " 남요정
					 * "00 0a 1a 08 00 10 bb f9 1a 18 "//남요정 여요정
					 * "01 0a 1d 08 00 10 ba f9 1a 18 "//여요정 남법사
					 * "01 0a 1f 08 00 10 b3 fb 8f 01 18 "//남법사 여법사
					 * "01 0a 1c 08 00 10 b8 f9 1a 18 "// 여법사 남다엘
					 * "01 0a 1a 08 00 10 e1 ed 81 07 18 " 여다엘
					 * "00 0a 1b 08 00 10 e2 ed 81 07 18 " 남용기사
					 * "00 0a 1d 08 00 10 e6 ed 81 07 18 " 여용기사
					 * "00 0a 1d 08 00 10 e7 ed 81 07 18 " 남환술사
					 * "00 0a 1f 08 00 10 9e dd 8f 01 18 "//남환술사 여환술사
					 * "00 0a 1e 08 00 10 df ed 81 07 18 " 남전사
					 * "01 0a 1c 08 00 10 c9 a5 ec 06 18 "//남전사 여전사
					 * "01 0a 1d 08 00 10 e3 ed 81 07 18 "
					 */
					/*
					 * if(_class == 0){//군주 if(_sex == 0){temp2 = "a6 e1 49";
					 * addlen=3; }else{temp2 = "f3 ad 8e 01"; addlen=4;} }else
					 * if(_class == 1){//기사 if(_sex == 0){temp2 = "bf f9 1a"
					 * ;addlen=3; }else{temp2 = "e0 ed 81 07";addlen=4;} }else
					 * if(_class == 2){//요정 if(_sex == 0){temp2 = "bb f9 1a"
					 * ;addlen=3; }else{temp2 = "ba f9 1a";addlen=3;} }else
					 * if(_class == 3){//법사 if(_sex == 0){temp2 = "b3 fb 8f 01"
					 * ;addlen=4; }else{temp2 = "b8 f9 1a"; addlen=3;} }else
					 * if(_class == 4){//다크엘프 if(_sex == 0){temp2 =
					 * "e1 ed 81 07";addlen=4; }else{temp2 = "e2 ed 81 07"
					 * ;addlen=4;} }else if(_class == 5){//용기사 if(_sex ==
					 * 0){temp2 = "e6 ed 81 07";addlen=4; }else{temp2 =
					 * "e7 ed 81 07";addlen=4;} }else if(_class == 6){//환술
					 * if(_sex == 0){temp2 = "9e dd 8f 01";addlen=4; }else{temp2
					 * = "df ed 81 07";addlen=4;} }else if(_class == 7){//전사
					 * if(_sex == 0){temp2 = "c9 a5 ec 06";addlen=4; }else{temp2
					 * = "e3 ed 81 07";addlen=4;} }
					 */

					if (time == 0) {
						tamcount = 1;
					} else {
						tamcount = byteWriteCount(time / 1000);
					}
					objidcount = byteWriteCount(_objid);

					writeC(0x0a);//
					// System.out.println(objidcount);
					writeC(_Name.getBytes().length + 14 + objidcount/* addlen */
							+ tamcount);// 패킷전체길이
					writeC(0x08);//
					writeC(0x00);// 변화 (서버 다를 변경)
					writeC(0x10);//
					byteWrite(_objid);// 탐 있을때 올라감.
					/*
					 * StringTokenizer st = new StringTokenizer(temp2);
					 * while(st.hasMoreTokens()){
					 * writeC(Integer.parseInt(st.nextToken(), 16)); }
					 */
					writeC(0x18);
					if (time == 0) {
						writeC(0);// 탐
					} else {
						byteWrite(time / 1000);// 탐 있을때 올라감.
					}
					writeC(0x20);
					writeC(tamwaitcount(_objid));
					writeC(0x2a);//
					writeC(_Name.getBytes().length);// 이름 길이
					writeByte(_Name.getBytes());// 이름 뒤에 0 없는이름.
					writeC(0x30);//
					writeC(_level);// 레벨
					writeC(0x38);//
					writeC(_class);// 클래스번호
					writeC(0x40);//
					writeC(_sex);// _sex);//남자여자?
				}
				// 포문 나온후 마지막에 줘야할 패킷
				// writeC(0x00);
				writeC(0x10);
				writeC(0x03);
				writeC(0x18);
				writeC(0x00);
				writeC(0x20);
				writeC(0x00);
				writeH(0);
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				SQLUtil.close(rs);
				SQLUtil.close(pstm);
				SQLUtil.close(con);
			}
			break;
		default:
			break;
		}
	}

	public S_NewCreateItem(int type, LineageClient client) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case TAM_POINT:
			writeC(0x08);
			int value = client.getAccount().tam_point;
			if (value <= 0)
				writeC(0x00);

			byteWrite(value);
			writeH(0x00);
			break;
		}
	}

	public S_NewCreateItem(int id, int id2, boolean bool, boolean bool2) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x01C2);
		writeC(0x08);
		writeC(id);
		writeH(id2);
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

	static final int div1 = 128 * 128 * 128 * 128;
	static final int div2 = 128 * 128 * 128;
	static final int div3 = 128 * 128;
	static final int div4 = 128;

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

	private int byteWriteCount(long value) {
		long temp = value / 128;
		int count = 0;
		if (temp > 0) {
			count++;
			while (temp >= 128) {
				count++;
				temp = temp / 128;
			}
			if (temp > 0)
				count++;
		} else {
			if (value == 0) {
				count++;
			} else {
				count += 2;
			}
		}
		return count;
	}


	public S_NewCreateItem(int type, int subType, boolean ck, int i, int Count) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		switch (type) {
		case FISH_WINDOW:
			writeC(0x08);
			writeC(subType);
			if (subType == 1) { // 시작시
				writeC(0x10);
				if (ck) {// 릴 장착
					if (i == 1) {
						writeC(0x28);// 성장의낚싯대
					} else {
						writeC(0x50);// 시간 (초단위)
					}
				} else {// 릴 미장착
					writeH(0x01F0);// 시간 (초단위)
				}
				/** 릴 장착 여부 */
				writeC(0x18);
				writeC(ck ? 0x02 : 0x01);// 1: 릴 미장착 2:: 릴장착
				/** 수량성 아이템 체크 */
				writeC(0x20);
				writeBit(Count);
				/** 버림수 필요없음 */
				writeH(0x00);
			} else if (subType == 2) { // 실패 거나 낚았을때
				/** 버림수 필요없음 */
				writeH(0x00);
			}
			break;
		}
	}

	public S_NewCreateItem(int warType, int second, String castle) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(CASTLE_WAR_TIME);
		writeC(0x08);
		writeC(warType);// 1수성 2공성
		writeC(0x10);
		if (second > 0) {
			int total = second *= 2;
			boolean ck = false;
			while (true) {
				if ((second -= 126) <= 0)
					break;
				if (!ck) {
					total += 256;
					ck = true;
				} else
					total += 128;
			}
			if (total <= 126)
				writeC(total);
			else
				writeH(total);
			writeC(0x1A);
			writeC(castle.getBytes().length);
			writeS(castle);
			writeC(0x14);
		} else {
			writeC(0x00);
			writeH(0x00);
		}
	}

	/*
	 * 공성 끝났을때 멘트 writeC(Opcodes.S_EXTENDED_PROTOBUF);
	 * writeH(CASTLE_WAR_TIME_END); writeD(0x0D120408); writeC(0x08);
	 * writeC(0x91);//성 타입 writeH(0x1207); writeC(0x08); writeS("성혈이름");
	 * writeC(0x00);
	 */

	public S_NewCreateItem(L1PcInstance pc, L1Object obj) {
		try {
			if (obj instanceof L1NpcInstance) {
				if (((L1NpcInstance) obj).getNpcId() == 100709) { // 아툰(10검)
					writeC(Opcodes.S_EXTENDED_PROTOBUF);
					writeH(LIST);
					writeH(0x08);
					writeC(0x12);
					writeC(0x07);
					writeC(0x08);
					writeH(0x02F7);
					writeH(0x10);
					writeH(0x18);
					writeH(0x18);
					writeH(0x00);
					return;
				} else if (((L1NpcInstance) obj).getNpcId() == 81121) { // 고대의
					// 정령
					writeC(Opcodes.S_EXTENDED_PROTOBUF);
					writeH(LIST);
					writeH(0x08);
					writeC(0x12);
					writeC(0x06);
					writeC(0x08);
					writeC(0x22);
					writeH(0x10);
					writeH(0x18);
					writeH(0x00);
					return;
				} else if (((L1NpcInstance) obj).getNpcId() == 100692) {
					writeC(Opcodes.S_EXTENDED_PROTOBUF);
					writeH(LIST);
					writeH(0x08);
					writeC(0x12);
					writeC(0x07);
					writeC(0x08);
					writeH(0x02E7);
					writeH(0x10);
					writeH(0x18);
					writeH(0x0712);
					writeC(0x08);
					writeH(0x02E8);
					writeH(0x10);
					writeH(0x18);
					writeH(0x0712);
					writeC(0x08);
					writeH(0x02E9);
					writeH(0x10);
					writeH(0x18);
					writeH(0x0712);
					writeC(0x08);
					writeH(0x02EA);
					writeH(0x10);
					writeH(0x18);
					writeH(0x0712);
					writeC(0x08);
					writeH(0x02EB);
					writeH(0x10);
					writeH(0x18);
					writeH(0xEA30);
					return;
				} else if (((L1NpcInstance) obj).getNpcId() == 100671) {
					writeC(Opcodes.S_EXTENDED_PROTOBUF);
					writeH(LIST);
					writeH(0x08);
					writeC(0x12);
					writeC(0x07);
					writeC(0x08);
					writeH(741);
					writeH(0x10);
					writeH(0x18);
					writeC(0x12);
					writeC(0x07);
					writeC(0x08);
					writeH(742);
					writeH(0x10);
					writeH(0x18);
					writeC(0x12);
					writeC(0x07);
					for (int i = 730; i <= 736; i++) {
						writeC(0x08);
						writeH(i);
						writeH(0x10);
						writeH(0x18);
						if (i == 736)
							writeH(0x00);
						else {
							writeC(0x12);
							writeC(0x07);
						}
					}
					return;

				} else if (((L1NpcInstance) obj).getNpcId() == 101001) {
					String s = "39 00 08 00 12 07 08 ce 05 10 00 18 00 12 07 "
							+ "08 cf 05 10 00 18 00 12 07 08 d0 05 10 00 18 00 "
							+ "12 07 08 d1 05 10 00 18 00 12 07 08 d2 05 10 00 "
							+ "18 00 12 07 08 d3 05 10 00 18 00 12 07 08 d4 05 "
							+ "10 00 18 00 12 07 08 d5 05 10 00 18 00 18 00 35 32";
					StringTokenizer st = new StringTokenizer(s);
					writeC(Opcodes.S_EXTENDED_PROTOBUF);
					while (st.hasMoreTokens()) {
						writeC(Integer.parseInt(st.nextToken(), 16));
					}
					return;
				} else if (((L1NpcInstance) obj).getNpcId() == 101027) {
					String s = "39 00 08 00 " + "12 07 08 a0 07 10 00 18 00 "
							+ "12 07 08 a1 07 10 00 18 00 "
							+ "12 07 08 a2 07 10 00 18 00 "
							+ "12 07 08 a3 07 10 00 18 00 "
							+ "12 07 08 a4 07 10 00 18 00 "
							+ "12 07 08 a5 07 10 00 18 00 "

							+ "12 07 08 a6 07 10 00 18 00 "
							+ "12 07 08 a7 07 10 00 18 00 "
							+ "12 07 08 a8 07 10 00 18 00 "
							+ "12 07 08 a9 07 10 00 18 00 "
							+ "12 07 08 aa 07 10 00 18 00 "
							+ "12 07 08 ab 07 10 00 18 00 "

							+ "12 07 08 ac 07 10 00 18 00 "
							+ "12 07 08 ad 07 10 00 18 00 "
							+ "12 07 08 ae 07 10 00 18 00 "
							+ "12 07 08 af 07 10 00 18 00 "
							+ "12 07 08 b0 07 10 00 18 00 "
							+ "12 07 08 b1 07 10 00 18 00 "

							+ "12 07 08 83 0C 10 00 18 00 "
							+ "12 07 08 84 0C 10 00 18 00 "
							+ "12 07 08 85 0C 10 00 18 00 "
							+ "12 07 08 86 0C 10 00 18 00 "
							+ "12 07 08 87 0C 10 00 18 00 "
							+ "12 07 08 88 0C 10 00 18 00 " + "18 00 44 d9";
					writeC(Opcodes.S_EXTENDED_PROTOBUF);
					StringTokenizer st = new StringTokenizer(s);
					while (st.hasMoreTokens()) {
						writeC(Integer.parseInt(st.nextToken(), 16));
					}
					return;
				} else if (((L1NpcInstance) obj).getNpcId() == 70027) {
					String s = "39 00 08 00 " + "12 07 08 b3 15 10 00 18 00 "
							+ "12 07 08 e8 15 10 00 18 00 "
							+ "12 07 08 ab 15 10 00 18 00 "
							+ "12 07 08 ac 15 10 00 18 00 "
							+ "12 07 08 ad 15 10 00 18 00 "
							+ "12 07 08 ae 15 10 00 18 00 "
							+ "12 07 08 af 15 10 00 18 00 "
							+ "12 07 08 b0 15 10 00 18 00 "
							+ "12 07 08 b1 15 10 00 18 00 "
							+ "12 07 08 b2 15 10 00 18 00 "
							+ "12 07 08 e4 15 10 00 18 00 "
							+ "12 07 08 e5 15 10 00 18 00 "
							+ "12 07 08 fc 18 10 00 18 00 "
							+ "12 07 08 e6 15 10 00 18 00 "
							+ "12 07 08 e7 15 10 00 18 00 " + "18 00 c7 29";
					writeC(Opcodes.S_EXTENDED_PROTOBUF);
					StringTokenizer st = new StringTokenizer(s);
					while (st.hasMoreTokens()) {
						writeC(Integer.parseInt(st.nextToken(), 16));
					}
					return;


				} else if (((L1NpcInstance) obj).getNpcId() == 101028) {
					String s = "39 00 08 00 "
							+ "12 07 08 b2 07 10 00 18 00 "// 1970
							+ "12 07 08 b3 07 10 00 18 00 "
							+ "12 07 08 b4 07 10 00 18 00 "
							+ "12 07 08 b5 07 10 00 18 00 "
							+ "12 07 08 b6 07 10 00 18 00 "
							+ "12 07 08 b7 07 10 00 18 00 "

							+ "12 07 08 b8 07 10 00 18 00 "
							+ "12 07 08 b9 07 10 00 18 00 "
							+ "12 07 08 ba 07 10 00 18 00 "
							+ "12 07 08 bb 07 10 00 18 00 "
							+ "12 07 08 bc 07 10 00 18 00 "
							+ "12 07 08 bd 07 10 00 18 00 "

							+ "12 07 08 be 07 10 00 18 00 "
							+ "12 07 08 bf 07 10 00 18 00 "
							+ "12 07 08 c0 07 10 00 18 00 "
							+ "12 07 08 c1 07 10 00 18 00 "
							+ "12 07 08 c2 07 10 00 18 00 "
							+ "12 07 08 c3 07 10 00 18 00 "

							+ "12 07 08 c4 07 10 00 18 00 "
							+ "12 07 08 c5 07 10 00 18 00 "
							+ "12 07 08 c6 07 10 00 18 00 "
							+ "12 07 08 c7 07 10 00 18 00 "
							+ "12 07 08 c8 07 10 00 18 00 "
							+ "12 07 08 c9 07 10 00 18 00 "

							+ "12 07 08 ca 07 10 00 18 00 "
							+ "12 07 08 cb 07 10 00 18 00 "
							+ "12 07 08 cc 07 10 00 18 00 "
							+ "12 07 08 cd 07 10 00 18 00 "
							+ "12 07 08 ce 07 10 00 18 00 "
							+ "12 07 08 cf 07 10 00 18 00 "

							+ "12 07 08 d0 07 10 00 18 00 "
							+ "12 07 08 d1 07 10 00 18 00 "

							+ "12 07 08 d2 07 10 00 18 00 "
							+ "12 07 08 d3 07 10 00 18 00 "
							+ "12 07 08 d4 07 10 00 18 00 "
							+ "12 07 08 d5 07 10 00 18 00 "

							+ "12 07 08 d6 07 10 00 18 00 "
							+ "12 07 08 d7 07 10 00 18 00 "
							+ "12 07 08 d8 07 10 00 18 00 "
							+ "12 07 08 d9 07 10 00 18 00 "
							+ "12 07 08 da 07 10 00 18 00 "
							+ "12 07 08 db 07 10 00 18 00 "// 2011
							+ "18 00 00 00";
					StringTokenizer st = new StringTokenizer(s);
					writeC(Opcodes.S_EXTENDED_PROTOBUF);
					while (st.hasMoreTokens()) {
						writeC(Integer.parseInt(st.nextToken(), 16));
					}
					return;

					/*
					 * }else if(((L1NpcInstance)obj).getNpcId() == 101002){
					 * writeC(Opcodes.S_EXTENDED_PROTOBUF); writeH(LIST);
					 * writeH(0x08);
					 * 
					 * writeC(0x12);writeC(0x07);writeC(0x08); writeH(1474);
					 * writeH(0x10);writeH(0x18);
					 * 
					 * writeC(0x12);writeC(0x07);writeC(0x08); writeH(1475);
					 * writeH(0x10);writeH(0x18); //1476 = 타락로브//1477 = 타락망토
					 * writeC(0x12);writeC(0x07);writeC(0x08); writeH(1478);
					 * writeH(0x10);writeH(0x18);
					 * 
					 * writeC(0x12);writeC(0x07);writeC(0x08); writeH(1479);
					 * writeH(0x10);writeH(0x18); writeC(0x18);writeC(0x00);
					 * writeC(0x26);writeC(0x70);
					 * 
					 * return; }else if(((L1NpcInstance)obj).getNpcId() ==
					 * 101003){ String s =
					 * "19 39 00 08 00 12 07 08 be 05 10 00 18 00 12 07 "+
					 * "08 bf 05 10 00 18 00 12 07 08 c0 05 10 00 18 00 "+
					 * "12 07 08 c1 05 10 00 18 00 12 07 08 c4 05 10 00 "+
					 * "18 00 12 07 08 c5 05 10 00 18 00 12 07 08 ca 05 "+
					 * "10 00 18 00 12 07 08 cb 05 10 00 18 00 12 07 08 "+
					 * "cc 05 10 00 18 00 12 07 08 cd 05 10 00 18 00 18 00 75 d0"
					 * ; StringTokenizer st = new StringTokenizer(s);
					 * while(st.hasMoreTokens()){
					 * writeC(Integer.parseInt(st.nextToken(), 16)); } return;
					 */
				}
			}
			FastTable<L1NpcAction> list = NpcActionTable.getInstance().getlist(
					pc, obj);
			if (list.size() <= 0) {
				if (obj instanceof L1NpcInstance) {
					L1NpcInstance npc = (L1NpcInstance) obj;
					if (npc.getNpcId() == 70614) {// 안톤
						writeC(Opcodes.S_EXTENDED_PROTOBUF);
						writeH(LIST);
						writeH(0x08);
						writeC(0x12);
						writeC(0x07);
						for (int i = 655; i <= 670; i++) {
							writeC(0x08);
							writeH(i);
							writeH(0x10);
							writeH(0x18);
							if (i == 670)
								writeH(0x00);
							else {
								writeC(0x12);
								writeC(0x07);
							}
						}
					}
				}
				return;
			}

			writeC(Opcodes.S_EXTENDED_PROTOBUF);
			writeH(LIST);
			writeH(0x08);
			writeC(0x12);
			writeC(0x07);
			FastTable<Integer> ck = new FastTable<Integer>();
			if (obj instanceof L1NpcInstance) {
				L1NpcInstance npc = (L1NpcInstance) obj;
				if (npc.getNpcId() == 100029) {// 조우의불골렘 제로스, 멸마갑옷
					writeC(0x08);
					writeH(1153);
					writeH(0x10);
					writeH(0x18);
					for (int a = 1154; a <= 1157; a++) {
						writeC(0x12);
						writeC(0x07);
						writeC(0x08);
						writeH(a);
						writeH(0x10);
						writeH(0x18);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String check = // 13 02
	"08 90 1c 10 d0 81 87 a5 05 18 80 a3 05 "
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
			+ "ff ff ff ff ff ff ff ff 01 00 00";

	String stat1 = "0a 06 08 19 10 01 18 01 12 06 08 19 10 "
			+ "01 18 01 1a 08 08 19 10 01 18 01 38 32 22 06 08 "
			+ "19 10 01 18 01 2a 06 08 19 10 01 30 32 32 0b 08 "
			+ "ff ff ff ff ff ff ff ff ff 01 23 ca";

	String stat2 = "0a 06 08 23 10 01 18 01 12 06 08 23 10 "
			+ "01 18 01 1a 08 08 23 10 01 18 01 38 64 22 06 08 "
			+ "23 10 01 18 01 2a 08 08 23 10 01 18 01 30 64 32 "
			+ "0b 08 ff ff ff ff ff ff ff ff ff 01 68 92";

	String stat3 = "0a 08 08 2d 10 03 18 03 20 01 12 08 08 "
			+ "2d 10 03 18 03 20 01 1a 09 08 2d 10 03 18 03 38 "
			+ "96 01 22 08 08 2d 10 03 18 03 20 01 2a 09 08 2d "
			+ "10 03 18 02 30 96 01 32 0b 08 ff ff ff ff ff ff "
			+ "ff ff ff 01 eb 88";

	public S_NewCreateItem(int type, String s) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		StringTokenizer st = new StringTokenizer(s);
		if (type == 532) {
			st = new StringTokenizer(check);
		}
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}  


	/*
	 * 43 e3 01 08 10 12 09 길이 08 03 근댐 10 07 근명 18 00 근치 20 b4 10 무게 79 27
	 */

	public S_NewCreateItem(String name, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x01e5);
		writeC(0x08);
		byteWrite(pc.getInventory().calcWeightpercent());
		// writeC();
		if (name.equalsIgnoreCase("무게")) {
			int 최대소지무게 = pc.getMaxWeight();
			// CalcStat.최대소지무게(pc.getAbility().getTotalStr(),
			// pc.getAbility().getTotalCon());
			writeC(0x10);
			byteWrite(pc.getInventory().getWeight());

			writeC(0x18);
			byteWrite(최대소지무게);

			writeH(0);
		}
	}

	/*
	 * ea 01
	 * 
	 * 08 14 힘 10 0a 인트 18 0a 위즈 20 0b 덱스 28 10 콘 30 08 카리
	 * 
	 * d8 44 D
	 */

	public S_NewCreateItem(int type, String name, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);// ea01
		if (name.equalsIgnoreCase("스탯툴팁")) {
			int str, inte, wis, dex, con, cha = 0;
			str = pc.getAbility().getStr();
			inte = pc.getAbility().getInt();
			wis = pc.getAbility().getWis();
			dex = pc.getAbility().getDex();
			con = pc.getAbility().getCon();
			cha = pc.getAbility().getCha();
			writeC(0x08);
			writeC(str);

			writeC(0x10);
			writeC(inte);

			writeC(0x18);
			writeC(wis);

			writeC(0x20);
			writeC(dex);

			writeC(0x28);
			writeC(con);

			writeC(0x30);
			writeC(cha);

			writeH(0);
		} else if (name.equalsIgnoreCase("툴팁1")) {
			StringTokenizer st = new StringTokenizer(stat1);
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}
		} else if (name.equalsIgnoreCase("툴팁2")) {
			StringTokenizer st = new StringTokenizer(stat2);
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}
		} else if (name.equalsIgnoreCase("툴팁3")) {
			StringTokenizer st = new StringTokenizer(stat3);
			while (st.hasMoreTokens()) {
				writeC(Integer.parseInt(st.nextToken(), 16));
			}
		}
	}

	public S_NewCreateItem(int type, int 상태, int stat, int stat2, String name,
			int classtype, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		writeC(0x08);
		writeC(상태 * 2);
		if (name.equalsIgnoreCase("힘")) {
			int len = 0;
			byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0x01 };

			int 근거리대미지 = CalcStat.근거리대미지(stat);
			int 근거리명중 = CalcStat.근거리명중(stat);
			int 근거리치명타 = CalcStat.근거리치명타(stat);
			int 최대소지무게 = CalcStat.최대소지무게(stat, stat2);

			len += 4 + 4 + 1;
			if (근거리대미지 < 0) {
				len += 9;
			}
			if (근거리명중 < 0) {
				len += 9;
			}
			if (근거리치명타 < 0) {
				len += 9;
			}
			if (최대소지무게 < 0) {
				len += 9;
			}

			writeC(0x12);
			writeC(len);

			writeC(0x08);
			writeC(근거리대미지);
			if (근거리대미지 < 0)
				writeByte(minus);
			writeC(0x10);
			writeC(근거리명중);
			if (근거리명중 < 0)
				writeByte(minus);
			writeC(0x18);
			writeC(근거리치명타);
			if (근거리치명타 < 0)
				writeByte(minus);
			writeC(0x20);
			byteWrite(최대소지무게);

			writeH(0);
		} else if (name.equalsIgnoreCase("덱")) {
			/*
			 * 43 e3 01 08 10 2a 13 08 04 원거리대미지 10 03 원거리명중 18 00 원거리치명 20 fc
			 * ff ff ff ff ff ff ff ff 01 물방 28 06 원거리회피 3d 69
			 */
			int len = 0;
			byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0x01 };
			int 원거리대미지 = CalcStat.원거리대미지(stat);
			int 원거리명중 = CalcStat.원거리명중(stat);
			int 원거리치명타 = CalcStat.원거리치명타(stat);
			int 물리방어력 = CalcStat.물리방어력(stat);
			int 원거리회피 = CalcStat.원거리회피(stat);

			len += 5 + 5;

			if (원거리대미지 < 0) {
				len += 9;
			}
			if (원거리명중 < 0) {
				len += 9;
			}
			if (원거리치명타 < 0) {
				len += 9;
			}
			if (물리방어력 < 0) {
				len += 9;
			}
			if (원거리회피 < 0) {
				len += 9;
			}

			writeC(0x2a);
			writeC(len);

			writeC(0x08);
			writeC(원거리대미지);
			if (원거리대미지 < 0)
				writeByte(minus);

			writeC(0x10);
			writeC(원거리명중);
			if (원거리명중 < 0)
				writeByte(minus);

			writeC(0x18);
			writeC(원거리치명타);
			if (원거리치명타 < 0)
				writeByte(minus);

			writeC(0x20);
			writeC(물리방어력);
			if (물리방어력 < 0)
				writeByte(minus);

			writeC(0x28);
			writeC(원거리회피);
			if (원거리회피 < 0)
				writeByte(minus);

			writeH(0);
		} else if (name.equalsIgnoreCase("콘")) {
			/*
			 * 32 0b 08 06 회복틱 10 00 물약회복증가 18 98 11 최대소지무게 20 0b 레벨업시hp증가 28 00
			 * ? 93 e5
			 */

			int len = 0;
			byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0x01 };
			int 피회복틱 = CalcStat.피회복틱(stat);
			int 물약회복증가 = CalcStat.물약회복증가(stat);
			int 최대소지무게 = CalcStat.최대소지무게(stat, stat2);
			int 레벨업피증가량 = CalcStat.레벨업피증가량(classtype, stat);

			len += 5 + 5 + 1 + 1 + byteWriteCount(pc == null ? 0 : pc.getBaseMaxHp());

			if (피회복틱 < 0) {
				len += 9;
			}
			if (물약회복증가 < 0) {
				len += 9;
			}
			if (레벨업피증가량 < 0) {
				len += 9;
			}

			writeC(0x32);
			writeC(len);

			writeC(0x08);
			writeC(피회복틱);
			if (피회복틱 < 0)
				writeByte(minus);

			writeC(0x10);
			writeC(물약회복증가);
			if (물약회복증가 < 0)
				writeByte(minus);

			writeC(0x18);
			byteWrite(최대소지무게);

			writeC(0x20);
			writeC(레벨업피증가량);
			if (레벨업피증가량 < 0)
				writeByte(minus);

			writeC(0x28);
			writeC(0);
			
			writeC(0x30);
			writeBit(pc == null ? 0 : pc.getBaseMaxHp());

			writeH(0);
		} else if (name.equalsIgnoreCase("인트")) {
			/*
			 * 43 e3 01 08 10 1a 13 08 00 마법대미지 10 fe ff ff ff ff ff ff ff ff 01
			 * 마법명중 18 00 마법 치명타 20 03 마법 보너스 28 08 엠소모 감소 3d 69
			 */
			/*
			 * 0000: 43 e3 01 08 10 1a 13 08 00 10 fd ff ff ff ff ff
			 * C............... 0010: ff ff ff 01 18 00 20 02 28 06 25 22 ......
			 * .(.%"
			 */

			int len = 0;
			byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0x01 };
			int 마법대미지 = CalcStat.마법대미지(stat);
			int 마법명중 = CalcStat.마법명중(stat);
			int 마법치명타 = CalcStat.마법치명타(stat);
			int 마법보너스 = CalcStat.마법보너스(stat);
			int 엠소모감소 = CalcStat.엠소모감소(stat);

			len += 5 + 5;

			if (마법대미지 < 0) {
				len += 9;
			}
			if (마법명중 < 0) {
				len += 9;
			}
			if (마법치명타 < 0) {
				len += 9;
			}
			if (마법보너스 < 0) {
				len += 9;
			}
			if (엠소모감소 < 0) {
				len += 9;
			}

			writeC(0x1a);
			writeC(len);

			writeC(0x08);
			writeC(마법대미지);
			if (마법대미지 < 0)
				writeByte(minus);

			writeC(0x10);
			writeC(마법명중);
			if (마법명중 < 0)
				writeByte(minus);

			writeC(0x18);
			writeC(마법치명타);
			if (마법치명타 < 0)
				writeByte(minus);

			writeC(0x20);
			writeC(마법보너스);
			if (마법보너스 < 0)
				writeByte(minus);

			writeC(0x28);
			writeC(엠소모감소);
			if (엠소모감소 < 0)
				writeByte(minus);

			writeH(0);
		} else if (name.equalsIgnoreCase("위즈")) {
			/*
			 * 08 10 22 0c 08 02 엠회복틱 10 02 18 25
			 * 
			 * 20 04 28 07 30 00 fb 2c
			 */
			int len = 0;
			byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0x01 };
			int 엠회복틱 = CalcStat.엠회복틱(stat);
			int 엠피물약회복 = CalcStat.엠피물약회복(stat);
			int 마법방어 = CalcStat.마법방어(classtype, stat);
			int[] 레벨업엠피증가량 = CalcStat.레벨업엠피증가량(classtype, stat);
			int hp = 0;
			if (stat >= 45) {
				hp = 300;
			} else if (stat >= 35) {
				hp = 150;
			} else if (stat >= 25) {
				hp = 50;
			}
			len += 6 + 4 + byteWriteCount(마법방어) + byteWriteCount(hp) + 1 + byteWriteCount(pc == null ? 0 : pc.getBaseMaxMp());

			if (stat >= 45) {
				len++;
			} else if (stat >= 35) {
				len++;
			}

			if (엠회복틱 < 0) {
				len += 9;
			}
			if (엠피물약회복 < 0) {
				len += 9;
			}

			writeC(0x22);
			writeC(len);

			writeC(0x08);
			writeC(엠회복틱);
			if (엠회복틱 < 0)
				writeByte(minus);

			writeC(0x10);
			writeC(엠피물약회복);
			if (엠피물약회복 < 0)
				writeByte(minus);

			writeC(0x18);
			byteWrite(마법방어);

			writeC(0x20);
			writeC(레벨업엠피증가량[0]);

			writeC(0x28);
			writeC(레벨업엠피증가량[1]);

			writeC(0x30);
			byteWrite(hp);
			/*
			 * if(stat >= 45){ writeC(0x96); writeC(2); }else if(stat >= 35){
			 * writeC(0x96); writeC(1); }else if(stat >= 25){ writeC(0x32);
			 * }else{ writeC(0); }
			 */
			
			writeC(0x38);
			writeBit(pc == null ? 0 : pc.getBaseMaxMp());

			writeH(0);

		} else if (name.equalsIgnoreCase("카리")) {
			writeC(0x3a);
			writeC(0x02);

			writeC(0x08);
			writeC(0x01);

			writeH(0);
		}
	}

	public S_NewCreateItem(int type, String s1, int objid, String s2, int gfxid) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(type);
		StringTokenizer st = new StringTokenizer(s1);

		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}

		byteWrite(objid);

		st = new StringTokenizer(s2);

		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
		byteWrite(gfxid);
		writeH(0);
	}

	public S_NewCreateItem(String s) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
	}

	/*
	 * 1470=은날 1471=금날 1472=명법망 1473=마령로브 1474=마부 1475=암장 1476=타로브 1477=타락망토
	 * 1478=타장 1479=타부 1480=오림 1481=세마 1482=카파모자 1483=메르모자 1484=발터모자 1485=세마모자
	 * 1486=지령블랙 1487=수 1488=풍 1489=화 1490=6마투 1491=7마투 1492=8마투 1493=9마투
	 */

	public S_NewCreateItem(int skillId, boolean on, int classId, long time) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeH(0x6E);
		writeC(0x08);
		writeC(on ? 2 : 3);
		writeC(0x10);
		byteWrite(skillId);
		writeC(0x18);
		if (time < 0) {
			byte[] minus = { (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,
					(byte) 0xff, (byte) 0x01 };
			writeByte(minus);
		} else
			byteWrite(time);
		writeC(0x20);
		writeC(0x08);
		writeC(0x28);
		if (skillId == L1SkillId.RANKING_BUFF_1) {
			byteWrite(7096);
		} else if (skillId == L1SkillId.RANKING_BUFF_2) {
			byteWrite(7095);
		} else if (skillId == L1SkillId.RANKING_BUFF_3) {
			byteWrite(7094);
		} else if (skillId == L1SkillId.RANKING_BUFF_4) {
			byteWrite(7094);
		} else if (skillId == L1SkillId.RANKING_BUFF_5) {
			byteWrite(7093);
		} else if (skillId == L1SkillId.RANKING_BUFF_6) {
			byteWrite(7093);
		} else if (skillId == L1SkillId.RANKING_BUFF_7) {
			byteWrite(8532);
		} else if (skillId == L1SkillId.RANKING_BUFF_8) {
			byteWrite(8532);
		} else if (skillId == L1SkillId.RANKING_BUFF_9) {
			byteWrite(8532);
		} else if (skillId == L1SkillId.RANKING_BUFF_10) {
			byteWrite(8532);
		} else if (skillId == L1SkillId.RANKING_BUFF_11) {
			byteWrite(8532);
		}
		writeH(0x0030);
		writeC(0x38);
		writeC(0x03);
		writeC(0x40);

		int msgNum = 0;

		if (skillId == L1SkillId.RANKING_BUFF_1)
			msgNum = 5148;
		else if (skillId == L1SkillId.RANKING_BUFF_2)
			msgNum = 5145;
		else if (skillId == L1SkillId.RANKING_BUFF_3)
			msgNum = 5144;
		else if (skillId == L1SkillId.RANKING_BUFF_4)
			msgNum = 5143;
		else if (skillId == L1SkillId.RANKING_BUFF_5)
			msgNum = 5142;
		else if (skillId == L1SkillId.RANKING_BUFF_6)
			msgNum = 5141;
		else if (skillId == L1SkillId.RANKING_BUFF_7)
			msgNum = 5140;
		else if (skillId == L1SkillId.RANKING_BUFF_8)
			msgNum = 5139;
		else if (skillId == L1SkillId.RANKING_BUFF_9)
			msgNum = 5138;
		else if (skillId == L1SkillId.RANKING_BUFF_10)
			msgNum = 5137;
		else if (skillId == L1SkillId.RANKING_BUFF_11)
			msgNum = 5136;

		if (classId == 2 && (msgNum == 5148 || msgNum == 5145)) {
			msgNum += 1;
		} else if ((classId == 3 || classId == 6) && (msgNum == 5148 || msgNum == 5145)) {
			msgNum += 2;
		}

		byteWrite(msgNum);

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

	/** 제작리스트 코드 **/
	private int createCode(int itemid) {
		switch (itemid) {
		/*
		 * 1719 2층 주문서 1720 3층 1721 4 1722 5 1723 6 1724 7 1725 8 1726 9 1727 10
		 * 1728 정상
		 */
		case 40033:
			return 2195;
			
		case 40034:
			return 2196;
		case 40035:
			return 2197;
		case 40036:
			return 2198;
		case 40037:
			return 2199;
		case 40038:
			return 2200;

		case 500207:
			return 934;
		case 40104:
			return 1719;// 2층주문서
		case 40105:
			return 1720;// 3층주문서
		case 40106:
			return 1721;// 4층주문서
		case 40107:
			return 1722;// 5층주문서
		case 40108:
			return 1723;// 6층주문서
		case 40109:
			return 1724;// 7층주문서
		case 40110:
			return 1725;// 8층주문서
		case 40111:
			return 1726;// 9층주문서
		case 40112:
			return 1727;// 10층주문서
		case 40113:
			return 1728;// 정상 주문서

			/*
			 * 1729 변이 1층 부적 1730 2층 1731 3 1732 4 1733 5 1734 6 1735 7 1736 8
			 * 1737 9 1738 10
			 */

		case 5000100:
			return 1729;// 1
		case 5000101:
			return 1730;// 2
		case 5000102:
			return 1731;// 3
		case 5000103:
			return 1732;// 4
		case 5000104:
			return 1733;// 5
		case 5000105:
			return 1734;// 6
		case 5000106:
			return 1735;// 7
		case 5000107:
			return 1736;// 8
		case 5000108:
			return 1737;// 9
		case 5000109:
			return 1738;// 10
			/*
			 * 1739 혼돈 1층 부적 1740 2층 1741 3 1742 4 1743 5 1744 6 1745 7 1746 8
			 * 1747 9 1748 10
			 */
		case 5000110:
			return 1739;// 1
		case 5000111:
			return 1740;// 2
		case 5000112:
			return 1741;// 3
		case 5000113:
			return 1742;// 4
		case 5000114:
			return 1743;// 5
		case 5000115:
			return 1744;// 6
		case 5000116:
			return 1745;// 7
		case 5000117:
			return 1746;// 8
		case 5000118:
			return 1747;// 9
		case 5000119:
			return 1748;// 10

		case 20050:
			return 1470;// 은날
		case 20049:
			return 1471;// 금날
		case 20057:
			return 1472;// 명망
		case 20109:
			return 1473;// 마로
		case 20200:
			return 1474;// 마부
		case 20178:
			return 1475;// 암장
		case 20152:
			return 1476;// 타롭
		case 20076:
			return 1477;// 타망
		case 20186:
			return 1478;// 타장
		case 20216:
			return 1479;// 타부
		case 21167:
			return 1480;// 오림
		case 21168:
			return 1481;// 세마
		case 41140:
			return 3521; // 제작테이블(보석) 시작
		case 20422:
			return 3522;
		case 21013:
			return 3523;
		case 21011:
			return 3524;
		case 21017:
			return 3525;
		case 20447:
			return 3526;
		case 20448:
			return 3527;
		case 20450:
			return 3528;
		case 20449:
			return 3529;
		case 222346:
			return 5591;
		case 222347:
			return 5592;
		case 222348:
			return 5593;
		case 222349:
			return 5594;// 제작테이블(보석) 끝
		case 84:
			return 5576; // 흑왕도
		case 164:
			return 5577;// 흑왕아
		case 189:
			return 5578;// 흑왕궁
		
		case 20040:
			return 1482;// 카파모
		case 20018:
			return 1483;// 메르모
		case 20025:
			return 1484;// 발터모
		case 20029:
			return 1485;// 세마모
		case 50749:
			return 1486;// 지
		case 50747:
			return 1487;// 수
		case 50748:
			return 1488;// 풍
		case 50750:
			return 1489;// 화

			/*
			 * case 50750: return 1490;//6신마투 case 50750: return 1491;//7신마투
			 * case 50750: return 1492;//8신마투 case 50750: return 1493;//9신마투
			 */
		case 130220:
			return 1716;// 격분의 장갑
		case 90084:
			return 1717;// 섬멸자의 체인소드
		case 90083:
			return 1715;// 포효의 이도류

		case 7227:
			return 1158;// 오우거의도끼
		case 7225:
			return 1159;// 산적의도끼
		case 7245:
			return 1210;// 마력의 장갑
		case 7246:
			return 1211;// 빛나는 마력의장갑

		case 7252:
			return 1217;
		case 7253:
			return 1218;
		case 7254:
			return 1219;
		case 7255:
			return 1220;
		case 7256:
			return 1221;

		case 60502:
			return 938;// 풍령의 보상:3만
		case 60503:
			return 939;// 지령의 보상:35만
		case 60504:
			return 940;// 수령의 보상:120만
		case 60505:
			return 941;// 화령의 보상:500만
			// 연금술사 //
		case 430008:
			return 4008;
		case 430007:
			return 4009;
		case 3000100:
			return 5088;
		case 6017:
			return 3781;

		case 20301:
			return 2;// 체력의인장
		case 20265:
			return 3;// 조화의인장
		case 20297:
			return 4;// 정신력의인장
		case 40391:
			return 5;// 계산기
		case 40921:
			return 6;// 원소의지배자
		case 218:
			return 7;// 앙가스의철퇴
		case 20396:
			return 8;// 시버인의방패
		case 210:
			return 9;// 시버인의창
		case 40623:
			return 10;// 1세대가보
		case 40624:
			return 14;// 2세대가보
		case 40625:
			return 18;// 3세대가보
		case 40626:
			return 22;// 4세대가보
		case 40627:
			return 26;// 5세대가보
		case 40628:
			return 30;// 6세대가보
		case 40417:
			return 34;// 정령의결정
		case 185:
			return 35;// 악마의크로스보우
		case 63:
			return 36;// 악마의칼
		case 85:
			return 37;// 악마의이도류
		case 165:
			return 38;// 악마의크로우
		case 20062:
			return 39;// 발록의핏빛망토
		case 40416:
			return 40;// 저주받은피
		case 40615:
			return 41;// 그신2층열쇠
		case 40616:
			return 42;// 그신3층열쇠
		case 40853:
			return 43;// 라스타바드이동주문서(조각)
		case 40084:
			return 44;// 디아드이동주문서
		case 40118:
		case 40120:
			return 45;// 라스타바드이동주문서(조각)
		case 61:
			return 46;// 진명황의집행검
		case 12:
			return 47;// 바칼
		case 86:
			return 48;// 붉이
		case 134:
			return 49;// 수결지
		case 20410:
			return 50;// 진명황의 장갑
		case 20395:
			return 51;// 진명황의 갑옷
		case 20408:
			return 52;// 진명황의 부츠
		case 20390:
			return 53;// 진명황의 투구
		case 20402:
			return 54;// 진명황의 망토
		case 40677:
			return 55;// 어둠의주괴
		case 41027:
			return 56;// 완성 라바역사서
		case 20235:
			return 60;// 에바의방패
		case 410000:
			return 61;// 소체	
		case 410001:
			return 62;// 파체
		case 6000:
			return 65;// 극체
		case 410003:
			return 66;// 사파이어키링크
		case 410004:
			return 67;// 흑요석키링크
		case 6001:
			return 68;// 냉한의키링크
		case 40526:
			return 69;// 얇은판금
		case 40779:
			return 70;// 강철괴
		case 40443:
			return 71;// 블랙미스릴
		case 40445:
			return 72;// 블랙미스릴판금
		case 40467:
			return 73;// 은괴
		case 40469:
			return 74;// 은판금
		case 40488:
			return 75;// 황금괴
		case 40487:
			return 76;// 황금판금
		case 40440:
			return 77;// 백금괴
		case 40439:
			return 78;// 백금판금
		case 420100:
			return 79;// 안타완력
		case 420112:
			return 80;// 발라완력
		case 420104:
			return 81;// 파푸완력
		case 420108:
			return 82;// 린드완력
		case 420103:
			return 83;// 안타마력
		case 420115:
			return 84;// 발라마력
		case 420107:
			return 85;// 파푸마력
		case 420111:
			return 86;// 린드마력
		case 420102:
			return 87;// 안타인내
		case 420114:
			return 88;// 발라인내
		case 420106:
			return 89;// 파푸인내
		case 420110:
			return 90;// 린드인내
		case 420101:
			return 91;// 안타예지
		case 420113:
			return 92;// 발라예지
		case 420105:
			return 93;// 파푸예지
		case 420109:
			return 94;// 린드예지
		case 60386:
			return 95;// 촉매추출주머니
		case 40318:
			return 96;// 마돌
		case 40319:
			return 97;// 정령옥
		case 40320:
			return 98;// 흑마석
		case 40031:
			return 99;// 악마의피
		case 21095:
			return 100;// 체력의가더
		case 21096:
			return 101;// 마나의가더
		case 21097:
			return 102;// 마법사가더
		case 430106:
			return 103;// 지룡마안
		case 430104:
			return 104;// 수룡마안
		case 430107:
			return 105;// 화룡마안
		case 430105:
			return 106;// 풍룡마안
		case 430108:
			return 107;// 탄생마안
		case 430109:
			return 108;// 형상마안
		case 430110:
			return 109;// 생명마안
		case 4305136:
			return 6133;// 흑룡의마안
		case 4305167:
			return 6134;// 절대의마안
		case 41916:
			return 110;// 마족검
		case 41917:
			return 111;// 마족활
		case 41918:
			return 112;// 마족지팡이
		case 41920:
			return 113;// 마족크로우
		case 41921:
			return 114;// 마족체인소드
		case 41919:
			return 115;// 마족키링크
		case 60041:
			return 116;// 7마단
		case 60042:
			return 117;// 7광풍
		case 60043:
			return 118;// 7파대
		case 60044:
			return 119;// 7천지
		case 60045:
			return 120;// 7혹한의창
		case 60046:
			return 121;// 7뇌신
		case 60047:
			return 122;// 7살천
		case 60048:
			return 123;// 8마단
		case 60049:
			return 124;// 8광풍
		case 60050:
			return 125;// 8파대
		case 60051:
			return 126;// 8천지
		case 60052:
			return 127;// 8혹한의창
		case 60053:
			return 384;// 8뇌신
		case 60054:
			return 385;// 8살천
		case 49072:
			return 392;// 데몬동상
		case 49073:
			return 393;// 드레이크동상
		case 49074:
			return 394;// 쿠거동상
		case 49075:
			return 395;// 아이리스동상
		case 49076:
			return 396;// 그미노동상
		case 20083:
			return 397;// 야히셔츠
		case 20131:
			return 398;// 야히 갑옷
		case 20069:
			return 399;// 야히의 망토
		case 20179:
			return 400;// 야히의장갑
		case 20209:
			return 401;// 야히의부츠
		case 20290:
			return 402;// 야히의반지
		case 20261:
			return 403;// 야히의목걸이
		case 20031:
			return 404;// 야히의투구
		case 196:
			return 405;// 앨리스1
		case 197:
			return 406;// 앨리스2
		case 198:
			return 407;// 앨리스3
		case 199:
			return 408;// 앨리스4
		case 200:
			return 409;// 앨리스5
		case 201:
			return 410;// 앨리스6
		case 202:
			return 411;// 앨리스7
		case 203:
			return 412;// 앨리스8
		case 420000:
			return 413;// 명가더
		case 420003:
			return 414;// 투사가더
		case 60387:
			return 415;// 7환체
		case 60388:
			return 416;// 8환체
		case 60389:
			return 417;// 7공키
		case 60390:
			return 418;// 8공키
		case 40502:
			return 419;// 실
		case 40504:
			return 420;// 아라크네의거미줄
		case 40495:
			return 421;// 미스릴실
		case 40497:
			return 422;// 미스릴판금
		case 40509:
			return 423;// 오리하루콘판금
		case 88:
			return 424;// 판의뿔
		case 40521:
			return 425;// 페어리의날개
		case 40494:
			return 426;// 미스릴
		case 40508:
			return 427;// 오리하루콘
		case 180:
			return 428;// 크로스보우
		case 181:
			return 429;// 장궁
		case 22:
			return 430;// 메일브레이커
		case 42:
			return 431;// 레이피어
		case 20033:
			return 432;// 엘름의축복
		case 20137:
			return 433;// 요정족사슬갑옷
		case 20138:
			return 434;// 요정족판금갑옷
		case 20073:
			return 435;// 요정족망토
		case 20191:
			return 436;// 활골무
		case 20187:
			return 437;// 파워글로브
		case 20236:
			return 438;// 요정족방패
		case 40520:
			return 439;// 페어리더스트
		case 40746:
			return 440;// 미스릴화살
		case 40748:
			return 441;// 오리하루콘화살
		case 40493:
			return 442;// 마법플룻
		case 40068:
			return 443;// 엘븐와퍼
		case 40506:
			return 444;// 엔트의열매
		case 20003:
			return 445;// 강철면갑
		case 20091:
			return 446;// 강철판금갑옷
		case 20163:
			return 447;// 강철장갑
		case 20194:
			return 448;// 강철부츠
		case 20220:
			return 449;// 강철방패
		case 6003:
			return 450;// 극한투구
		case 6004:
			return 451;// 극한갑옷
		case 6005:
			return 452;// 극한부츠
		case 49015:
			return 453;// 블랙미스릴용액
		case 121:
			return 454;// 얼지
		case 20085:
			return 455;// 티셔츠
		case 20056:
			return 456;// 마법망토
		case 20063:
			return 457;// 보호망토
		case 57:
			return 458;// 싸울
		case 20146:
			return 459;// 지룡비늘갑옷
		case 20127:
			return 460;// 수룡비늘갑옷
		case 20156:
			return 461;// 풍룡비늘갑옷
		case 20159:
			return 462;// 화룡비늘갑옷
		case 20130:
			return 463;// 안타마갑주(숨결)
		case 20153:
			return 464;// 파푸마갑주
		case 20108:
			return 465;// 린드마갑주
		case 20119:
			return 466;// 발라마갑주
		case 190:
			return 467;// 사이하의활
		case 120187:
			return 468;// 타라스의장갑
		case 120194:
			return 469;// 타라스의부츠
		case 21166:
			return 470;// 대마법사의모자
		case 158:
			return 475;// 어둠의크로우
		case 75:
			return 476;// 어둠의이도류
		case 168:
			return 477;// 어둠의활
		case 162:
			return 478;// 흑크
		case 81:
			return 479;// 흑이
		case 177:
			return 480;// 흑활
		case 157:
			return 481;// 은색의크로우
		case 74:
			return 482;// 은이
		case 40747:
			return 483;// 블미화살
		case 40162:
			return 484;// 골렘의숨결
		case 40413:
			return 485;// 얼녀숨결
		case 40169:
			return 486;// 드레이크숨결
		case 40409:
			return 487;// 불새의숨결
		case 20309:
			return 488;// 빛나는신벨
		case 20311:
			return 489;// 빛나는정벨
		case 20310:
			return 490;// 빛나는영벨
		case 40018:
			return 491;// 강화촐기
		case 40428:
			return 492;// 달빛의눈물
		case 40525:
			return 493;// 그랑카인의눈물
		case 40227:
			return 494;// 글로잉웨폰 법서
		case 40229:
			return 495;// 샤이닝실드 법서
		case 40230:
			return 496;// 브레이브멘탈 법서
		case 40231:
			return 497;// 런클랜 법서
		case 40208:
			return 498;// 라이프스트림 법서
		case 119:
			return 499;// 데몬의지팡이
		case 124:
			return 500;// 바포지팡이
		case 123:
			return 501;// 베레스의지팡이
		case 427104:
			return 502;// 사냥개이빨
		case 427109:
			return 503;// 승리의이빨
		case 427103:
			return 504;// 황금의이빨
		case 427107:
			return 505;// 신마의이빨
		case 427101:
			return 506;// 파멸의이빨
		case 427003:
			return 507;// 스켈펫아머
		case 427006:
			return 508;// 크로스펫아머
		case 427007:
			return 509;// 체인펫아머
		case 427005:
			return 510;// 미스릴펫아머
		case 40406:
			return 511;// 고급피혁

		case 40404:
			return 640;// 저주의피혁(대지)
		case 40402:
			return 641;// 저주의피혁(물결)
		case 40403:
			return 642;// 저주의피혁(바람)
		case 40401:
			return 643;// 저주의피혁(열화)
		case 20054:
			return 644;// 대지의망토
		case 20059:
			return 645;// 물결의망토
		case 20061:
			return 646;// 바람의망토
		case 20071:
			return 647;// 열화의망토
		case 550001:
			return 648;// 농축용기의물약
		case 550002:
			return 649;// 농축집중의물약
		case 550003:
			return 650;// 농축지혜의물약
		case 550004:
			return 651;// 농축마력의물약
		case 550000:
			return 652;// 농축속도의물약
		case 550005:
			return 653;// 농축호흡의물약
		case 550006:
			return 654;// 농축변신의물약
		case 21169:
			return 655;// +0멸마의판금갑옷
		case 21170:
			return 656;// +0멸마의비늘갑옷
		case 21171:
			return 657;// +0멸마의가죽갑옷
		case 21172:
			return 658;// +0멸마의로브갑옷
			/*
			 * 659 +1멸마의판금갑옷 660 +1멸마의비늘갑옷 661 +1멸마의가죽갑옷 662 +1멸마의로브갑옷 663
			 * +2멸마의판금갑옷 ~ 670 +3멸마의로브갑옷
			 */

		case 60126:
			return 671;// 7파괴의크로우
		case 60127:
			return 672;// 8파괴의크로우
		case 60128:
			return 673;// 7파괴의이도류
		case 60129:
			return 674;// 8파괴의이도류
		case 40925:
			return 685;// 정화의물약
		case 21006:
			return 686;// 영혼의 귀걸이 기사
		case 21009:
			return 687;// 열정의 귀걸이
		case 21012:
			return 688;// 명예의 귀걸이
		case 21007:
			return 690;// 영혼의 귀걸이 다엘
		case 21008:
			return 691;// 분노의귀걸이
		case 21010:
			return 692;// 용맹의귀걸이
		case 21014:
			return 694;// 영혼의 귀걸이 법사
		case 21015:
			return 695;// 지혜의귀걸이
		case 21016:
			return 696;// 진실의귀걸이
		case 20435:
			return 698;// 남작의지령반지
		case 20439:
			return 699;// 백작의지령반지
		case 20443:
			return 700;// 공작의지령반지
		case 20436:
			return 702;// 남작의수령반지
		case 20440:
			return 703;// 백작의수령반지
		case 20444:
			return 704;// 공작의수령반지
		case 20438:
			return 706;// 남작의풍령반지
		case 20442:
			return 707;// 백작의풍령반지
		case 20446:
			return 708;// 공작의풍령반지
		case 20437:
			return 710;// 남작의화령반지
		case 20441:
			return 711;// 백작의화령반지
		case 20445:
			return 712;// 공작의화령반지
		case 60423:
			return 741;// 용의 호박석
		case 60486:
			return 929;// 포노스포상
		case 60487:
			return 930;// 포노스포상
		case 60488:
			return 931;// 포노스포상
		case 60489:
			return 932;// 포노스포상
		case 1437009:
			return 1008;// 축복받은 드상
		case 7322:
			return 1009;// 드래곤의연금술용액
		case 7251:
			return 1160;// 숨겨진마족무기함도끼

		case 40722:
			return 2183;// 금호박
		case 435000:
			return 2184;// 호박파이
		case 21269:
			return 2185;// 호박 목걸이

		case 90085:
			return 15776;// 양손검
		case 90086:
			return 15777;// 지팡이
		case 90087:
			return 15778;// 한손검
		case 90088:
			return 15779;// 활
		case 90089:
			return 15780;// 이도류
		case 90090:
			return 15781;// 체인소드
		case 90091:
			return 15782;// 키링크
		case 90092:
			return 15783;// 도끼
		default:
			return 0;
		}
	}

	public static int createItemByCode(int code) {
		switch (code) {

		case 5555:
			return 8020; // -- 환생의 보석
		case 5608:
			return 8022; // -- 환생의 보석 주머니
		case 5547:
			return 40048; // -- 고급다이아
		case 5548:
			return 40051; // -- 고급에메랄드
		case 5549:
			return 40049; // -- 고급루비
		case 5550:
			return 40050; // -- 고급사파
		case 5551:
			return 40052; // -- 최고급다이아몬드
		case 5552:
			return 40055; // -- 고급에메랄드
		case 5553:
			return 40053; // -- 최고급루비
		case 5554:
			return 40054; // -- 최고급사파
		case 5604:
			return 6010; // -- 극한티아라원석
		case 6396:
			return 6011; // -- 극한의샌달원석
		case 5605:
			return 6012; // -- 극한의샌달원석
		case 5606:
			return 437010; // -- 드래곤의다이아몬드
		case 5607:
			return 437009; // -- 드래곤의보물상자
			// -------------------------------------------------
			
		case 2195:
			return 40033;
		case 2196:
			return 40034;
		case 2197:
			return 40035;
		case 2198:
			return 40036;
		case 2199:
			return 40037;
		case 2200:
			return 40038;

		case 934:
			return 500207;
		case 2183:
			return 40722;// 금호박
		case 2184:
			return 435000;// 호박파이
		case 2185:
			return 21269;// 호박 목걸이

		case 15776:
			return 90085;// 양손검
		case 15777:
			return 90086;// 지팡이
		case 15778:
			return 90087;// 한손검
		case 15779:
			return 90088;// 활
		case 15780:
			return 90089;// 이도류
		case 15781:
			return 90090;// 체인소드
		case 15782:
			return 90091;// 키링크
		case 15783:
			return 90092;// 도끼

		case 1719:
			return 40104;// 2층주문서
		case 1720:
			return 40105;// 3층주문서
		case 1721:
			return 40106;// 4층주문서
		case 1722:
			return 40107;// 5층주문서
		case 1723:
			return 40108;// 6층주문서
		case 1724:
			return 40109;// 7층주문서
		case 1725:
			return 40110;// 8층주문서
		case 1726:
			return 40111;// 9층주문서
		case 1727:
			return 40112;// 10층주문서
		case 1728:
			return 40113;// 정상 주문서

			/*
			 * 1729 변이 1층 부적 1730 2층 1731 3 1732 4 1733 5 1734 6 1735 7 1736 8
			 * 1737 9 1738 10
			 */
		case 1729:
			return 5000100;// 1
		case 1730:
			return 5000101;// 2
		case 1731:
			return 5000102;// 3
		case 1732:
			return 5000103;// 4
		case 1733:
			return 5000104;// 5
		case 1734:
			return 5000105;// 6
		case 1735:
			return 5000106;// 7
		case 1736:
			return 5000107;// 8
		case 1737:
			return 5000108;// 9
		case 1738:
			return 5000109;// 10

			/*
			 * 1739 혼돈 1층 부적 1740 2층 1741 3 1742 4 1743 5 1744 6 1745 7 1746 8
			 * 1747 9 1748 10
			 */
		case 1739:
			return 5000110;// 1
		case 1740:
			return 5000111;// 2
		case 1741:
			return 5000112;// 3
		case 1742:
			return 5000113;// 4
		case 1743:
			return 5000114;// 5
		case 1744:
			return 5000115;// 6
		case 1745:
			return 5000116;// 7
		case 1746:
			return 5000117;// 8
		case 1747:
			return 5000118;// 9
		case 1748:
			return 5000119;// 10

		case 1716:
			return 130220;// 격분의 장갑
		case 1717:
			return 90084;// 섬멸자의 체인소드
		case 1715:
			return 90083;// 포효의 이도류

		case 1470:
			return 20050;// 은날
		case 1471:
			return 20049;// 금날
		case 1472:
			return 20057;// 명망
		case 1473:
			return 20109;// 마로
		case 1474:
			return 20200;// 마부
		case 1475:
			return 20178;// 암장
		case 1476:
			return 20152;// 타롭
		case 1477:
			return 20076;// 타망
		case 1478:
			return 20186;// 타장
		case 1479:
			return 20216;// 타부
		case 1480:
			return 21167;// 오림
		case 1481:
			return 21168;// 세마
		case 3522:
			return 20422; // 제작테이블(보석) 시작
		case 3523:
			return 21013;
		case 3524:
			return 21011;
		case 3525:
			return 21017;
		case 3526:
			return 20447;
		case 3527:
			return 20448;
		case 3528:
			return 20450;
		case 3529:
			return 20449;
		case 5591:
			return 222346;
		case 5592:
			return 222347;
		case 5593:
			return 222348;
		case 5594:
			return 222349;// 제작테이블(보석) 끝
		case 5576:
			return 84; // 흑왕도
		case 5577:
			return 164;// 흑왕아
		case 5578:
			return 189;// 흑왕궁			
		case 1482:
			return 20040;// 카파모
		case 1483:
			return 20018;// 메르모
		case 1484:
			return 20025;// 발터모
		case 1485:
			return 20029;// 세마모

		case 1486:
			return 50749;// 지
		case 1487:
			return 50747;// 수
		case 1488:
			return 50748;// 풍
		case 1489:
			return 50750;// 화

		case 1008:
			return 1437009;// 축드상
		case 1009:
			return 7322;// 드래곤의연금술용액
		case 1158:
			return 7227;// 오우거의도끼
		case 1159:
			return 7225;// 산적의도끼
		case 1210:
			return 7245;// 마력의 장갑
		case 1211:
			return 7246;// 빛나는 마력의장갑

		case 1217:
			return 7252;
		case 1218:
			return 7253;
		case 1219:
			return 7254;
		case 1220:
			return 7255;
		case 1221:
			return 7256;

		case 471:
			return 20130;
		case 472:
			return 20153;
		case 473:
			return 20108;
		case 474:
			return 20119;
		case 47:
			return 12;
		case 430:
			return 22;
		case 431:
			return 42;
		case 458:
			return 57;
		case 46:
			return 61;
		case 36:
			return 63;
		case 482:
			return 74;
		case 476:
			return 75;
		case 479:
			return 81;
		case 58:
			return 84;
		case 37:
			return 85;
		case 48:
			return 86;
		case 424:
			return 88;
		case 499:
			return 119;
		case 454:
			return 121;
		case 501:
			return 123;
		case 500:
			return 124;
		case 49:
			return 134;
		case 481:
			return 157;
		case 475:
			return 158;
		case 478:
			return 162;
		case 57:
			return 164;
		case 38:
			return 165;
		case 477:
			return 168;
		case 480:
			return 177;
		case 428:
			return 180;
		case 429:
			return 181;
		case 35:
			return 185;
		case 59:
			return 189;
		case 467:
			return 190;
		case 405:
			return 196;
		case 406:
			return 197;
		case 407:
			return 198;
		case 408:
			return 199;
		case 409:
			return 200;
		case 410:
			return 201;
		case 411:
			return 202;
		case 412:
			return 203;
		case 9:
			return 210;
		case 7:
			return 218;
		case 65:
			return 6000;
		case 68:
			return 6001;
		case 450:
			return 6003;
		case 451:
			return 6004;
		case 452:
			return 6005;
		case 445:
			return 20003;
		case 404:
			return 20031;
		case 432:
			return 20033;
		case 456:
			return 20056;
		case 39:
			return 20062;
		case 457:
			return 20063;
		case 399:
			return 20069;
		case 435:
			return 20073;
		case 397:
			return 20083;
		case 455:
			return 20085;
		case 446:
			return 20091;
		case 465:
			return 20108;
		case 466:
			return 20119;
		case 460:
			return 20127;
		case 463:
			return 20130;
		case 398:
			return 20131;
		case 433:
			return 20137;
		case 434:
			return 20138;
		case 459:
			return 20146;
		case 464:
			return 20153;
		case 461:
			return 20156;
		case 462:
			return 20159;
		case 447:
			return 20163;
		case 400:
			return 20179;
		case 437:
			return 20187;
		case 436:
			return 20191;
		case 448:
			return 20194;
		case 401:
			return 20209;
		case 449:
			return 20220;
		case 60:
			return 20235;
		case 438:
			return 20236;
		case 403:
			return 20261;
		case 3:
			return 20265;
		case 402:
			return 20290;
		case 4:
			return 20297;
		case 2:
			return 20301;
		case 488:
			return 20309;
		case 490:
			return 20310;
		case 489:
			return 20311;
		case 53:
			return 20390;
		case 51:
			return 20395;
		case 8:
			return 20396;
		case 54:
			return 20402;
		case 52:
			return 20408;
		case 50:
			return 20410;
		case 100:
			return 21095;
		case 101:
			return 21096;
		case 102:
			return 21097;
		case 470:
			return 21166;
		case 491:
			return 40018;
		case 99:
			return 40031;
		case 443:
			return 40068;
		case 44:
			return 40084;
		case 43:
			return 40118;
		case 484:
			return 40162;
		case 486:
			return 40169;
		case 498:
			return 40208;
		case 494:
			return 40227;
		case 495:
			return 40229;
		case 496:
			return 40230;
		case 497:
			return 40231;
		case 26:
			return 40267;
		case 30:
			return 40268;
		case 96:
			return 40318;
		case 97:
			return 40319;
		case 98:
			return 40320;
		case 5:
			return 40391;
		case 511:
			return 40406;
		case 487:
			return 40409;
		case 485:
			return 40413;
		case 40:
			return 40416;
		case 34:
			return 40417;
		case 492:
			return 40428;
		case 78:
			return 40439;
		case 77:
			return 40440;
		case 71:
			return 40443;
		case 72:
			return 40445;
		case 73:
			return 40467;
		case 74:
			return 40469;
		case 76:
			return 40487;
		case 75:
			return 40488;
		case 442:
			return 40493;
		case 426:
			return 40494;
		case 421:
			return 40495;
		case 422:
			return 40497;
		case 419:
			return 40502;
		case 420:
			return 40504;
		case 444:
			return 40506;
		case 427:
			return 40508;
		case 423:
			return 40509;
		case 439:
			return 40520;
		case 425:
			return 40521;
		case 493:
			return 40525;
		case 69:
			return 40526;
		case 41:
			return 40615;
		case 42:
			return 40616;
		case 10:
			return 40623;
		case 14:
			return 40624;
		case 18:
			return 40625;
		case 22:
			return 40626;
		case 55:
			return 40677;
		case 440:
			return 40746;
		case 483:
			return 40747;
		case 441:
			return 40748;
		case 70:
			return 40779;
		case 6:
			return 40921;
		case 56:
			return 41027;
		case 110:
			return 41916;
		case 111:
			return 41917;
		case 112:
			return 41918;
		case 115:
			return 41919;
		case 113:
			return 41920;
		case 114:
			return 41921;
		case 1160:
			return 7251;// 숨겨진마족무기함도끼

		case 453:
			return 49015;
		case 392:
			return 49072;
		case 393:
			return 49073;
		case 394:
			return 49074;
		case 395:
			return 49075;
		case 396:
			return 49076;
		case 116:
			return 60041;
		case 117:
			return 60042;
		case 118:
			return 60043;
		case 119:
			return 60044;
		case 120:
			return 60045;
		case 121:
			return 60046;
		case 122:
			return 60047;
		case 123:
			return 60048;
		case 124:
			return 60049;
		case 125:
			return 60050;
		case 126:
			return 60051;
		case 127:
			return 60052;
		case 384:
			return 60053;
		case 385:
			return 60054;
		case 468:
			return 120187;
		case 469:
			return 120194;
		case 61:
			return 410000;
		case 62:
			return 410001;
		case 66:
			return 410003;
		case 67:
			return 410004;
		case 413:
			return 420000;
		case 414:
			return 420003;
		case 79:
			return 420100;
		case 91:
			return 420101;
		case 87:
			return 420102;
		case 83:
			return 420103;
		case 81:
			return 420104;
		case 93:
			return 420105;
		case 89:
			return 420106;
		case 85:
			return 420107;
		case 82:
			return 420108;
		case 94:
			return 420109;
		case 90:
			return 420110;
		case 86:
			return 420111;
		case 80:
			return 420112;
		case 92:
			return 420113;
		case 88:
			return 420114;
		case 84:
			return 420115;
		case 507:
			return 427003;
		case 510:
			return 427005;
		case 508:
			return 427006;
		case 509:
			return 427007;
		case 506:
			return 427101;
		case 504:
			return 427103;
		case 502:
			return 427104;
		case 505:
			return 427107;
		case 503:
			return 427109;
		case 104:
			return 430104;
		case 106:
			return 430105;
		case 103:
			return 430106;
		case 105:
			return 430107;
		case 107:
			return 430108;
		case 108:
			return 430109;
		case 109:
			return 430110;
		case 6133:
			return 4305136;
		case 6134:
			return 4305137;

		case 95:
			return 60386;
		case 415:
			return 60387;
		case 416:
			return 60388;
		case 417:
			return 60389;
		case 418:
			return 60390;

		case 640:
			return 40404;
		case 641:
			return 40402;
		case 642:
			return 40403;
		case 643:
			return 40401;
		case 644:
			return 20054;
		case 645:
			return 20059;
		case 646:
			return 20061;
		case 647:
			return 20071;
		case 648:
			return 550001;
		case 649:
			return 550002;
		case 650:
			return 550003;
		case 651:
			return 550004;
		case 652:
			return 550000;
		case 653:
			return 550005;
		case 654:
			return 550006;
		case 655:
			return 21169;
		case 656:
			return 21170;
		case 657:
			return 21171;
		case 658:
			return 21172;
		case 671:
			return 60126;
		case 672:
			return 60127;
		case 673:
			return 60128;
		case 674:
			return 60129;
		case 685:
			return 40925;
		case 686:
			return 21006;
		case 687:
			return 21009;
		case 688:
			return 21012;
		case 689:
			return 21013;
		case 690:
			return 21007;
		case 691:
			return 21008;
		case 692:
			return 21010;
		case 693:
			return 21011;
		case 694:
			return 21014;
		case 695:
			return 21015;
		case 696:
			return 21016;
		case 697:
			return 21017;
		case 698:
			return 20435;
		case 699:
			return 20439;
		case 700:
			return 20443;
		case 701:
			return 20447;
		case 702:
			return 20436;
		case 703:
			return 20440;
		case 704:
			return 20444;
		case 705:
			return 20448;
		case 706:
			return 20438;
		case 707:
			return 20442;
		case 708:
			return 20446;
		case 709:
			return 20450;
		case 710:
			return 20437;
		case 711:
			return 20441;
		case 712:
			return 20445;
		case 713:
			return 20449;
		case 741:
			return 60423;// 용의 호박석
		case 742:
			return 212551;// 용의 호박갑옷 (호박석)
		case 731:
			return 212552;// 용의 호박갑옷 (8무기)
		case 732:
			return 212553;// 용의 호박갑옷 (8방어구)
		case 733:
			return 212554;// 용의 호박갑옷 (9무기)
		case 734:
			return 212555;// 용의 호박갑옷 (9방어구)
		case 735:
			return 212556;// 용의 호박갑옷 (10방어구)
		case 736:
			return 212557;// 용의 호박갑옷 (10방어구)
		case 743:
			return 60443;// 크리스마스 쿠키
		case 744:
			return 60439;// 선물 상자(모태솔로)
		case 745:
			return 60440;// 선물 상자(솔로의품격)
		case 746:
			return 60441;// 선물 상자(무적의솔로부대)
		case 747:
			return 60442;// 선물 상자(솔로천국커플지옥)
		case 759:
			return 60474;// 아툰의 무기상자
		case 929:
			return 60486;// 포노스포상
		case 930:
			return 60487;// 포노스포상
		case 931:
			return 60488;// 포노스포상
		case 932:
			return 60489;// 포노스포상
		case 938:
			return 60502;// 풍령의 보상:3만
		case 939:
			return 60503;// 지령의 보상:35만
		case 940:
			return 60504;// 수령의 보상:120만
		case 941:
			return 60505;// 화령의 보상:500만
			// 연금술사 //
		case 4008:
			return 430008;
		case 4009:
			return 430007;
		case 5088:
			return 3000100;
		case 3781:
			return 6017;
		case 6849:
			return 300011; //5신성한완력의반지
		case 6852:
			return 300012; //6신성한완력의반지
		case 6855:
			return 300013; //7신성한완력의반지
		case 6858:
			return 300014; //8신성한완력의반지
		case 6861:
			return 300015; //9신성한완력의반지
		default:
			return 0;
		}
	}

	public S_NewCreateItem(L1PcInstance pc, int type, int chat_type,
			String chat_text, String target_name) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);

		switch (type) {
		case 3:
			writeC(0x03);
			break;
		case 4:
			writeC(0x04);
			break;
		}

		writeC(0x02);
		writeC(0x08);
		writeC(0x00);
		writeC(0x10);
		writeC(chat_type);

		writeC(0x1a);
		byte[] text_byte = chat_text.getBytes();
		writeC(text_byte.length);
		writeByte(text_byte);

		switch (type) {
		case 3:
			writeC(0x22);

			if (chat_type == 0) {
				writeC(0x00);
				writeC(0x28);
				writeC(0x00);
				writeC(0x30);
				writeC(0x18);
			} else if (chat_type == 1) {
				byte[] name_byte = target_name.getBytes();
				writeC(name_byte.length);
				writeByte(name_byte);
				writeC(0x30);
				writeC(0x00);
			}
			break;
		case 4:
			writeC(0x2a);
			try {
				if (pc.isGm() && chat_type == 3) {
					byte[] name = "******".getBytes("MS932");
					writeC(name.length);
					writeByte(name);
				} else if (pc.getAge() != 0 && chat_type == 4) {// 여기 되게 되잇는데...
					String names = pc.getName() + "(" + pc.getAge() + ")";
					byte[] name = names.getBytes("MS932");
					writeC(name.length);
					writeByte(name);
				} else {
					byte[] name = pc.getName().getBytes("MS932");
					writeC(name.length);
					writeByte(name);
				}
			} catch (UnsupportedEncodingException e) {
			}

			if (chat_type == 0) {
				writeC(0x38);
				byteWrite(pc.getId());
				writeC(0x40);
				byteWrite(pc.getX());
				writeC(0x48);
				byteWrite(pc.getY());
			}
			break;
		}
		L1UserRanking rank = UserRankingController.getInstance().getTotalRank(pc.getName());

		if (rank != null) {
			int uRank = rank.getCurRank();
			int number = 0;
			if (uRank >= 1 && uRank <= 10) {
				number = 11;
			} else if (uRank >= 11 && uRank <= 20) {
				number = 10;
			} else if (uRank >= 21 && uRank <= 40) {
				number = 9;
			} else if (uRank >= 41 && uRank <= 60) {
				number = 8;
			} else if (uRank >= 61 && uRank <= 80) {
				number = 7;
			} else if (uRank >= 81 && uRank <= 100) {
				number = 6;
			} else if (uRank >= 101 && uRank <= 120) {
				number = 5;
			} else if (uRank >= 121 && uRank <= 140) {
				number = 4;
			} else if (uRank >= 141 && uRank <= 160) {
				number = 3;
			} else if (uRank >= 161 && uRank <= 180) {
				number = 2;
			} else if (uRank >= 181 && uRank <= 200) {
				number = 1;
			}
			writeC(0x50);
			writeC(number);
		}

		writeH(0x00);
	}
	
	
	  
	public S_NewCreateItem(L1PcInstance pc, int chat_types, String s) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF); // 7월자는 36
		writeC(0x04);
		writeC(0x02);
		writeC(0x08);
		writeC(0x39);
		writeC(0x10);
		writeC(chat_types);
		writeC(0x1A);
		byte[] chat_byte = s.getBytes();
		writeC(chat_byte.length);
		writeByte(chat_byte);
		writeC(0x2A);
		byte[] name = pc.getName().getBytes();
		writeC(name.length);
		writeByte(name);
		writeC(0x28);
		writeC(0x22);
		writeC(0x50);
		L1UserRanking rank = UserRankingController.getInstance().getTotalRank(
				pc.getName());
		if (rank != null) {
			int uRank = rank.getCurRank();
			if (uRank >= 1 && uRank <= 10)
				writeC(0x0b);
			else if (uRank >= 11 && uRank <= 20)
				writeC(0x0a);
			else if (uRank >= 21 && uRank <= 40)
				writeC(0x09);
			else if (uRank >= 41 && uRank <= 60)
				writeC(0x08);
			else if (uRank >= 61 && uRank <= 80)
				writeC(0x07);
			else if (uRank >= 81 && uRank <= 100)
				writeC(0x06);
			else if (uRank >= 101 && uRank <= 120)
				writeC(0x05);
			else if (uRank >= 121 && uRank <= 140)
				writeC(0x04);
			else if (uRank >= 141 && uRank <= 160)
				writeC(0x03);
			else if (uRank >= 161 && uRank <= 180)
				writeC(0x02);
			else if (uRank >= 181 && uRank <= 200)
				writeC(0x01);
		} else {
			writeC(0x00);
		}

		if (chat_types == 0) {
			writeC(0x38);// 오브젝??
			writeK(pc.getId());
			writeC(0x40);
			writeK(pc.getX());
			writeC(0x48);
			writeK(pc.getY());
		}
		writeH(0x00);
	}

	public S_NewCreateItem(int type, byte[] chat, int count, String targetNam1,
			L1PcInstance pc) {

		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x03);
		writeC(0x02);
		writeC(0x08);
		writeK(count); // 카운트

		writeC(0x10);
		writeC(type); // 타입?

		writeC(0x1a);
		writeC(chat.length);
		writeByte(chat);

		writeC(0x22);
		if (type == 1) {
			try {
				byte[] name = targetNam1.getBytes("EUC-KR");
				writeC(name.length);
				writeByte(name);
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			writeC(0);
		}
		writeC(0x28);
		writeC(0x00);
		writeC(0x30);
		writeC(0x00);
		writeC(0x50);
		L1UserRanking rank = UserRankingController.getInstance().getTotalRank(
				pc.getName());
		if (rank != null) {
			int uRank = rank.getCurRank();
			if (uRank >= 1 && uRank <= 10)
				writeC(0x0b);
			else if (uRank >= 11 && uRank <= 20)
				writeC(0x0a);
			else if (uRank >= 21 && uRank <= 40)
				writeC(0x09);
			else if (uRank >= 41 && uRank <= 60)
				writeC(0x08);
			else if (uRank >= 61 && uRank <= 80)
				writeC(0x07);
			else if (uRank >= 81 && uRank <= 100)
				writeC(0x06);
			else if (uRank >= 101 && uRank <= 120)
				writeC(0x05);
			else if (uRank >= 121 && uRank <= 140)
				writeC(0x04);
			else if (uRank >= 141 && uRank <= 160)
				writeC(0x03);
			else if (uRank >= 161 && uRank <= 180)
				writeC(0x02);
			else if (uRank >= 181 && uRank <= 200)
				writeC(0x01);
		} else {
			writeC(0x00);
		}

		writeH(0);

	}

	public S_NewCreateItem(int type, byte[] chat, int count, String targetName) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x03);
		writeC(0x02);
		writeC(0x08);
		writeK(count); // 카운트

		writeC(0x10);
		writeC(type); // 타입?

		writeC(0x1a);
		writeC(chat.length);
		writeByte(chat);

		writeC(0x22);
		if (type == 1) {
			try {
				byte[] name = targetName.getBytes("EUC-KR");
				writeC(name.length);
				writeByte(name);
			} catch (UnsupportedEncodingException e) {
			}
		} else {
			writeC(0);
		}

		writeC(0x28);
		writeC(0x00);
		writeC(0x30);
		writeC(0x00);
		writeC(0x50);
		L1UserRanking rank = UserRankingController.getInstance().getTotalRank(
				targetName);
		if (rank != null) {
			int uRank = rank.getCurRank();
			if (uRank >= 1 && uRank <= 10)
				writeC(0x0b);
			else if (uRank >= 11 && uRank <= 20)
				writeC(0x0a);
			else if (uRank >= 21 && uRank <= 40)
				writeC(0x09);
			else if (uRank >= 41 && uRank <= 60)
				writeC(0x08);
			else if (uRank >= 61 && uRank <= 80)
				writeC(0x07);
			else if (uRank >= 81 && uRank <= 100)
				writeC(0x06);
			else if (uRank >= 101 && uRank <= 120)
				writeC(0x05);
			else if (uRank >= 121 && uRank <= 140)
				writeC(0x04);
			else if (uRank >= 141 && uRank <= 160)
				writeC(0x03);
			else if (uRank >= 161 && uRank <= 180)
				writeC(0x02);
			else if (uRank >= 181 && uRank <= 200)
				writeC(0x01);
		} else {
			writeC(0x00);
		}

		writeH(0);

	}

	public S_NewCreateItem(int type, byte[] chat, int count, L1PcInstance pc) {
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x04);
		writeC(0x02);
		writeC(0x08);
		writeK(count); // 카운트
		writeC(0x10);
		writeC(type); // 타입?
		writeC(0x1a);
		writeC(chat.length);
		writeByte(chat);
		writeC(0x2a);
		try {
			if (pc.isGm() && type == 3) {
				byte[] name = "******".getBytes("EUC-KR");
				writeC(name.length);
				writeByte(name);
			} else if (pc.getAge() != 0 && type == 4) {
				String names = pc.getName() + "(" + pc.getAge() + ")";
				byte[] name = names.getBytes("EUC-KR");
				writeC(name.length);
				writeByte(name);
			} else {
				byte[] name = pc.getName().getBytes("EUC-KR");
				writeC(name.length);
				writeByte(name);
			}
		} catch (UnsupportedEncodingException e) {
		}
		writeC(0x28);
		writeC(0x22);
		writeC(0x50);
		L1UserRanking rank = UserRankingController.getInstance().getTotalRank(
				pc.getName());
		if (rank != null) {
			int uRank = rank.getCurRank();
			if (uRank >= 1 && uRank <= 10)
				writeC(0x0b);
			else if (uRank >= 11 && uRank <= 20)
				writeC(0x0a);
			else if (uRank >= 21 && uRank <= 40)
				writeC(0x09);
			else if (uRank >= 41 && uRank <= 60)
				writeC(0x08);
			else if (uRank >= 61 && uRank <= 80)
				writeC(0x07);
			else if (uRank >= 81 && uRank <= 100)
				writeC(0x06);
			else if (uRank >= 101 && uRank <= 120)
				writeC(0x05);
			else if (uRank >= 121 && uRank <= 140)
				writeC(0x04);
			else if (uRank >= 141 && uRank <= 160)
				writeC(0x03);
			else if (uRank >= 161 && uRank <= 180)
				writeC(0x02);
			else if (uRank >= 181 && uRank <= 200)
				writeC(0x01);
		} else {
			writeC(0x00);
		}

		if (type == 0) {
			writeC(0x38);// 오브젝??
			writeK(pc.getId());
			writeC(0x40);
			writeK(pc.getX());
			writeC(0x48);
			writeK(pc.getY());
		}

		writeH(0);

	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	@Override
	public String getType() {
		return S_NEWCREATEITEM;
	}
}
