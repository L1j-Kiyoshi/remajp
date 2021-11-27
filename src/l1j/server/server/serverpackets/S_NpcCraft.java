package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_NpcCraft extends ServerBasePacket {

    private static final String S_NpcCraft = "[S] S_NpcCraft";
    private byte[] _byte = null;
    
    public S_NpcCraft() {
        writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(59);
        writeH(0x08);
        writeH(0x00);
    }

    public S_NpcCraft(int type, int npcId) {  
        writeC(Opcodes.S_EXTENDED_PROTOBUF);
        writeH(type + 1);
        writeH(0x08);
        if (type == 92) {
            writeC(0);
            writeC(0x10);
            writeC(0xe3);
            writeC(0x03);
            writeC(0x56);
            writeC(0xce);
        } else if (type == 54) {
            writeC(0x08);
            writeC(0x03); 
        } else if (type == 56) {
            switch (npcId) {
            case 4212002: 
                craftList(103, 109);
                craftList(496, 497);
                break;
            case 40150: 
                craftList(823, 860);
                break;
            case 700705: // 룸티스의 수정구 완료
                craftList(928, 945);
                craftList(1539, 1544);
                break;
            case 40161: // 무브니
                craftList(823, 852);
                break;

            case 700706: // 스냅퍼의 수정구 완료
                craftList(946, 987);
                break;
            case 9213: // 조우의 불골렘 완료
                craftList(513, 517);
                craftList(113, 116);
                craftList(123, 123);
                craftList(159, 162);
                craftList(271, 290);
                craftList(423, 425);
                break;
            case 70520: //연금술사 촉매추출(완료)
                craftList(95, 99);
                break;
            case 70598: //모리아 얼지,변환유물(완료)
                craftList(198, 198);
                craftList(422, 422);
                break;
            case 70028:
                craftList(264, 270);
                break;
            case 70641:
                craftList(199, 201);
                craftList(570, 571);
                break;
            case 70690:
                craftList(255, 263);
                craftList(820, 820);
                craftList(481, 488);
                break;
            case 70642:
                craftList(518, 519);
                craftList(194, 196);
                craftList(819, 819);
                craftList(821, 821);
                craftList(189, 193);
                craftList(69, 78);
                break;
            case 700702:
                craftList(702, 705);
                craftList(708, 709);
                craftList(714, 717);
                break;
            case 700701:
                craftList(706, 707);
                craftList(710, 711);
                break;
            case 700703:
                craftList(718, 725);
                break;
            case 700704:
                craftList(712, 713);
                break;
            case 71292:
                craftList(157, 158);
                break;
            case 71296:
                craftList(100, 102);             
                break;
            case 4212005: 
                craftList(79, 82);
                break;
            case 4212003:
                craftList(83, 86);
                break;
            case 4212004: 
                craftList(87, 90);
                break;
            case 4212006:
                craftList(91, 94);
                break;
            case 9456: 
                craftList(577, 581);
                break;
            case 70811:
                craftList(417, 420);
                break;
            case 71125:
                craftList(46, 55);
                break;
            case 71119:
                craftList(56, 56); 
                break;
            case 4218001: 
                craftList(62, 65); 
                break;
            case 4219001:
                craftList(67, 68); 
                break;
            case 70904: 
                craftList(69, 78);
                craftList(215, 227); 
                break;
            case 70802: 
            	craftList(69, 78); 
            	break;
            case 70763:
                craftList(211, 214);
                break;
            case 70676: 
                craftList(228, 242);
                break;
            case 81114: 
                craftList(243, 245);
                break;
            case 9458: 
                craftList(582, 584); 
                break;
            case 71128: 
                craftList(314, 329);
                break;
            case 80068:
                craftList(39, 42);
                break;
            case 80069:
                craftList(35, 38);
                break;          
            }
        }
        writeH(0x00);
    }

    private void craftList(int min, int max) {
        for (int i = min; i <= max; i++) {
            writeC(0x12);
            if (i > 127) {
                writeC(0x07);
            } else {
                writeC(0x06);
            }
            writeC(0x08);
            write4bit(i);
            writeH(0x10);
            writeH(0x18);
        }
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
        return S_NpcCraft;
    }
}
