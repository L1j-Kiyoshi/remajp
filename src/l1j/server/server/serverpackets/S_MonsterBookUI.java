package l1j.server.server.serverpackets;

import java.util.HashMap;
import java.util.StringTokenizer;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_MonsterBookUI extends ServerBasePacket {
   /*
    * @param Serverbasepacket
    * 
    * @param S_EXTENDED_PROTOBUF Opcode
    * 
    * @param Type H 값으로 변경
    **/
   public static final int MONSTER_BOOK = 559; // 몬스터 도감활성화
   public static final int MONSTER_LOAD = 560; // 몬스터 도감에 몬스터로드
   public static final int MONSTER_END = 564; // 몬스터 도감 끝
   public static final int MONSTER_ADD = 567; // 몬스터 도감에 몬스터추가
   public static final int MONSTER_CLEAR = 568; // 몬스터 도감 퀘스트클리어

   public S_MonsterBookUI(int type, L1PcInstance pc, boolean i) {
      writeC(Opcodes.S_EXTENDED_PROTOBUF);
      writeC(type);
      String s = "03 0a e7 01 0a 55 12 10 08 01 10 9a 83 2c 18 00 22 06 "
            + "08 d8 87 01 10 01 12 11 08 02 10 81 88 6e 18 c6 20 22 06 08 "
            + "d8 87 01 10 02 12 12 08 03 10 84 a0 b8 03 18 9f 78 22 06 08 "
            + "d8 87 01 10 05 1a 0b 08 01 10 84 a0 b8 03 18 c1 87 01 1a 0b "
            + "08 01 10 b5 bf f0 06 18 c0 87 01 20 01 12 2e 08 00 18 01 22 " // 20 99 - 20 37
            + "0c 08 00 10 28 18 f3 03 20 a7 01 28 00 22 0c 08 01 10 28 18 "
            + "fc 03 20 aa 01 28 00 22 0c 08 02 10 28 18 f6 03 20 a8 01 28 "
            + "00 12 2e 08 01 18 01 22 0c 08 00 10 28 18 b1 09 20 91 03 28 "
            + "00 22 0c 08 01 10 28 18 ae 09 20 90 03 28 00 22 0c 08 02 10 "
            + "28 18 ab 09 20 8f 03 28 00 12 2e 08 02 18 01 22 0c 08 00 10 "
            + "28 18 c0 09 20 96 03 28 00 22 0c 08 01 10 28 18 c3 09 20 97 "
            + "03 28 00 22 0c 08 02 10 28 18 ba 09 20 94 03 28 00 85 28";
      StringTokenizer st = new StringTokenizer(s.toString());
      while (st.hasMoreTokens()) {
         writeC(Integer.parseInt(st.nextToken(), 16));
      }
   }

   /** 테스트 용 **/
   public S_MonsterBookUI(int code, int value) {
      writeC(Opcodes.S_EXTENDED_PROTOBUF);
      writeH(code);
      switch (code) {
      case MONSTER_BOOK: {
         writeC(0x08);
         writeC(0x00);
         writeC(0x10);
         writeC(0x00);
         writeH(0);
         break;
      }
      case MONSTER_LOAD: {
         writeC(8);
         writeC(0);
         writeC(16);
         writeC(0);
         for (int i = 0; i < 555; i++) {
            writeC(26);
            int j = size7B(i) * 2 + 2;
            write7B(j);
            writeC(8);
            write7B(i);
            writeC(16);
            write7B(i);
         }
         writeH(0);
         break;
      }
      case MONSTER_ADD: {
         writeC(0x08);
         writeC(0x72);
         writeC(0x10);
         write7B(10000);
         writeH(0);
         break;
      }
      case MONSTER_CLEAR:
         writeC(0x08);
         write7B(value);
         writeC(0x10);
         write7B(System.currentTimeMillis() / 1000L);
         writeH(0);
         break;
      case MONSTER_END:
         writeC(0x08);
         writeC(0x00);
         writeC(0x10);
         write7B(value);
         writeH(0);
         break;
      }
   }

   public S_MonsterBookUI(L1PcInstance pc, int code) {
      writeC(Opcodes.S_EXTENDED_PROTOBUF);
      writeH(code);
      writeC(0x08);
      switch (code) {
      case MONSTER_BOOK:
         writeC(0);
         writeC(0x10);
         writeC(0);
         break;
      }
      writeH(0);
   }

   public S_MonsterBookUI(int code, int num, int counter) {
      writeC(Opcodes.S_EXTENDED_PROTOBUF);
      writeH(code);
      writeC(0x08);
      switch (code) {
      case MONSTER_ADD:
         write7B(num); // 몬스터번호
         writeC(0x10);
         write7B(counter); // 킬수
         break;
      }
      writeH(0);
   }

   public S_MonsterBookUI(int code, HashMap<Integer, Integer> monsterList) {
      writeC(Opcodes.S_EXTENDED_PROTOBUF);
      writeH(code);
      switch (code) {
      case MONSTER_LOAD:
         writeC(0x08);
         writeC(0);
         writeC(0x10);
         writeC(0);
         if (monsterList != null) {
            monsterList.forEach((monstertnumber, monsterkillcount) -> {
               writeC(0x1a); // 길이
               writeC(size7B(monstertnumber) + size7B(monsterkillcount) + 2);
               writeC(0x08); // 몬스터넘버
               write7B(monstertnumber);
               writeC(0x10); // 몬스터킬수
               write7B(monsterkillcount);
            });
         }
         writeH(0);
         break;
      case MONSTER_BOOK:
         writeC(0x08);
         writeC(0x00);
         writeC(0x10);
         writeC(0x00);
         if (monsterList != null) {
            monsterList.forEach((questNum, value) -> {
               if (value != 0) {
                  long time = System.currentTimeMillis() / 1000L;
                  writeC(0x1a); // 길이
                  writeC(size7B(questNum) + size7B((int) time) + 4);
                  writeC(0x08); // 퀘스트번호
                  write7B(questNum);
                  writeC(0x10); // 저장시간
                  write7B((int) time);
                  writeC(0x18); // 완료여부
                  writeC(value);
               }
            });
         }
         writeH(0);
         break;
      }
   }

   @Override
   public byte[] getContent() {
      return getBytes();
   }
}