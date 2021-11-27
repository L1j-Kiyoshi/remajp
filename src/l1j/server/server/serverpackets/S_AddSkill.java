package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_AddSkill extends ServerBasePacket {
	private static final String S_ADD_SKILL = "[S] S_AddSkill";

	private byte[] _byte = null;

	public S_AddSkill(int level, int id) {
		int ids[] = new int[32];
		for (int i = 0; i < ids.length; i++) {
			ids[i] = 0;
		}
		ids[level] = id;

		int i6 = ids[4] + ids[5] + ids[6] + ids[7];
		int j6 = ids[8] + ids[9];
		writeC(Opcodes.S_ADD_SPELL);
		if (i6 > 0 && j6 == 0) {
			writeC(50);
		} else if (j6 > 0) {
			writeC(100);
		} else if (i6 == 0 && j6 == 0) {//
			writeC(32);//
		} else {
			writeC(22);
		}
		for (int i : ids) {
			writeC(i);
		}
		writeC(0);
		// writeD(0);
		// writeD(0);
	}

	public S_AddSkill(int level1, int level2, int level3, int level4,
			int level5, int level6, int level7, int level8, int level9,
			int level10, int knight, int l2, int de1, int de2, int royal,
			int l3, int elf1, int elf2, int elf3, int elf4, int elf5, int elf6,
			int k5, int l5, int dk3, int bw1, int bw2, int bw3, int warrior1,
			int warrior2, int warrior3, int warrior4, int elfAttr) {
		writeC(Opcodes.S_ADD_SPELL);
		writeC(0x20);
		writeC(level1);
		writeC(level2);
		writeC(level3);
		writeC(level4);
		writeC(level5);
		writeC(level6);
		writeC(level7);
		writeC(level8);
		writeC(level9);
		writeC(level10);

		writeC(knight);
		writeC(l2);
		writeC(de1);
		writeC(de2);
		writeC(royal);
		writeC(l3);
		writeC(elf1);
		writeC(elf2);
		writeC(elf3);
		writeC(elf4);

		writeC(elf5);
		writeC(elf6);
		writeC(k5);
		writeC(l5);
		writeC(dk3);
		writeC(bw1);
		writeC(bw2);
		writeC(bw3);
		writeC(warrior1);
		writeC(warrior2);
		writeC(warrior3);
		writeC(warrior4);

		int elfAttrValue = 0;

		if (elfAttr != 0) {
			if (elfAttr == 2)
				elfAttrValue = 1;
			else if (elfAttr == 4)
				elfAttrValue = 2;
			else if (elfAttr == 8)
				elfAttrValue = 3;
			else if (elfAttr == 1)
				elfAttrValue = 4;
		}
		writeC(elfAttrValue); // 요정 속성
	}

	public S_AddSkill(int[] lv) {
		writeC(Opcodes.S_ADD_SPELL);
		writeC(0x20);
		for (int loop = 0; loop < lv.length; loop++) {
			writeC(lv[loop]);
		}
		writeD(0x00);
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
		return S_ADD_SKILL;
	}

}
