package l1j.server.GameSystem;

import java.util.ArrayList;
import java.util.Random;

import l1j.server.server.ActionCodes;
import l1j.server.server.datatables.DoorSpawnTable;
import l1j.server.server.datatables.ItemTable;
import l1j.server.server.model.Broadcaster;
import l1j.server.server.model.Getback;
import l1j.server.server.model.L1PolyMorph;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1DoorInstance;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1MerchantInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.item.L1ItemId;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.model.skill.L1SkillUse;
import l1j.server.server.serverpackets.S_ChangeShape;
import l1j.server.server.serverpackets.S_DRAGONPERL;
import l1j.server.server.serverpackets.S_Game_PetRacing;
import l1j.server.server.serverpackets.S_Message_YN;
import l1j.server.server.serverpackets.S_NpcChatPacket;
import l1j.server.server.serverpackets.S_PacketBox;
import l1j.server.server.serverpackets.S_SabuTell;
import l1j.server.server.serverpackets.S_ServerMessage;
import l1j.server.server.serverpackets.S_SkillBrave;
import l1j.server.server.serverpackets.S_SkillHaste;
import l1j.server.server.serverpackets.S_SystemMessage;

public class PetRacing implements Runnable {

	Random ran = new Random(System.nanoTime());

	enum STATUS {
		ENTERREADY, READY, PLAY, END, REST
	};

	enum MSG {
		ENTER, WAIT_START, NOT_ENOUGH_STARTMEMBERS, GAMEEND
	};

	private int startPolyId;
	private int winnersCount = 0;
	private int finishMemberCount = 0;
	private boolean timeover;

	private L1PcInstance[] rankList;
	private L1PcInstance[] finishMember;

	private final int LIMIT_ENTERMEMBER_COUNT = 2; // 입장메세지 출력에 필요한 인원(본섭 : 2)
	private final int LIMIT_STARTMEMBER_COUNT = 2; // 경기시작에 필요한 인원 (본섭 : 2)
	private final short PETRACE_MAPID = 5143;

	public static int Start_X = 32735;
	public static int Start_Y = 32811;
	public static int[][] wmp = {
			{ 1000, 1000, 1000, 108, 108, 109, 109, 110, 111, 111, 111, 112,
					113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124,
					125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 134, 135,
					136, 137, 137, 1000, 1000, 1000 },
			{ 1000, 1000, 107, 108, 108, 109, 109, 110, 111, 111, 111, 112,
					113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124,
					125, 126, 127, 128, 129, 130, 131, 132, 133, 134, 134, 135,
					136, 137, 137, 138, 1000, 1000 },
			{ 1000, 107, 107, 107, 108, 108, 109, 110, 110, 111, 111, 112, 113,
					114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125,
					126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137,
					137, 138, 138, 138, 1000 },
			{ 106, 106, 107, 107, 107, 108, 108, 109, 110, 111, 111, 112, 113,
					114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125,
					126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 136, 137,
					138, 138, 139, 139, 139 },
			{ 106, 106, 106, 107, 107, 107, 108, 109, 110, 111, 111, 112, 113,
					114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125,
					126, 127, 128, 129, 130, 131, 132, 133, 134, 135, 137, 138,
					138, 139, 139, 139, 139 },
			{ 106, 106, 106, 106, 106, 107, 107, 109, 109, 110, 111, 112, 113,
					114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125,
					126, 127, 128, 129, 130, 131, 132, 133, 134, 136, 138, 138,
					139, 139, 140, 140, 140 },
			{ 105, 105, 106, 106, 106, 106, 107, 108, 109, 110, 111, 112, 113,
					114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125,
					126, 127, 128, 129, 130, 131, 132, 133, 135, 138, 138, 139,
					139, 140, 140, 141, 141 },
			{ 104, 105, 105, 105, 106, 106, 106, 107, 109, 110, 111, 112, 113,
					114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124, 125,
					126, 127, 128, 129, 130, 131, 132, 133, 137, 139, 139, 140,
					140, 140, 141, 141, 141 },
			{ 104, 104, 104, 105, 105, 105, 106, 106, 108, 109, 111, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 139, 140, 140, 141, 141, 141, 141, 141, 141 },
			{ 104, 104, 104, 104, 104, 105, 105, 105, 106, 109, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 142, 142, 142, 142, 142, 142, 142, 142 },
			{ 104, 104, 104, 104, 104, 104, 104, 105, 105, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 143, 143, 143, 143, 143, 143, 143, 143 },
			{ 103, 103, 103, 103, 103, 103, 103, 103, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 144, 144, 144, 144, 144, 144, 144, 144 },
			{ 102, 102, 102, 102, 102, 102, 102, 102, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 145, 145, 145, 145, 145, 145, 145, 145 },
			{ 101, 101, 101, 101, 101, 101, 101, 101, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 146, 146, 146, 146, 146, 146, 146, 146 },
			{ 100, 100, 100, 100, 100, 100, 100, 100, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 147, 147, 147, 147, 147, 147, 147, 147 },
			{ 99, 99, 99, 99, 99, 99, 99, 99, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 148, 148, 148, 148, 148, 148, 148, 148 },
			{ 98, 98, 98, 98, 98, 98, 98, 98, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 149, 149, 149, 149, 149, 149, 149, 149 },
			{ 97, 97, 97, 97, 97, 97, 97, 97, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 150, 150, 150, 150, 150, 150, 150, 150 },
			{ 96, 96, 96, 96, 96, 96, 96, 96, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 151, 151, 151, 151, 151, 151, 151, 151 },
			{ 95, 95, 95, 95, 95, 95, 95, 95, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 152, 152, 152, 152, 152, 152, 152, 152 },
			{ 94, 94, 94, 94, 94, 94, 94, 94, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 153, 153, 153, 153, 153, 153, 153, 153 },
			{ 93, 93, 93, 93, 93, 93, 93, 93, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 154, 154, 154, 154, 154, 154, 154, 154 },
			{ 92, 92, 92, 92, 92, 92, 92, 92, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 155, 155, 155, 155, 155, 155, 155, 155 },
			{ 91, 91, 91, 91, 91, 91, 91, 91, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 156, 156, 156, 156, 156, 156, 156, 156 },
			{ 90, 90, 90, 90, 90, 90, 90, 90, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 157, 157, 157, 157, 157, 157, 157, 157 },
			{ 89, 89, 89, 89, 89, 89, 89, 89, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 158, 158, 158, 158, 158, 158, 158, 158 },
			{ 88, 88, 88, 88, 88, 88, 88, 88, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 159, 159, 159, 159, 159, 159, 159, 159 },
			{ 87, 87, 87, 87, 87, 87, 87, 87, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 160, 160, 160, 160, 160, 160, 160, 160 },
			{ 86, 86, 86, 86, 86, 86, 86, 86, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 161, 161, 161, 161, 161, 161, 161, 161 },
			{ 85, 85, 85, 85, 85, 85, 85, 85, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 162, 162, 162, 162, 162, 162, 162, 162 },
			{ 84, 84, 84, 84, 84, 84, 84, 84, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 0, 0, 0, 0, 0, 0, 0, 0 },
			{ 83, 83, 83, 83, 83, 83, 83, 83, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1, 1, 1, 1, 1, 1, 1, 1 },
			{ 82, 82, 82, 82, 82, 82, 82, 82, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 2, 2, 2, 2, 2, 2, 2, 2 },
			{ 81, 81, 81, 81, 81, 81, 81, 81, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 3, 3, 3, 3, 3, 3, 3, 3 },
			{ 80, 80, 80, 80, 80, 80, 80, 80, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 4, 4, 4, 4, 4, 4, 4, 4 },
			{ 79, 79, 79, 79, 79, 79, 79, 79, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 5, 5, 5, 5, 5, 5, 5, 5 },
			{ 78, 78, 78, 78, 78, 78, 78, 78, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 6, 6, 6, 6, 6, 6, 6, 6 },
			{ 77, 77, 77, 77, 77, 77, 77, 77, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 7, 7, 7, 7, 7, 7, 7, 7 },
			{ 76, 76, 76, 76, 76, 76, 76, 76, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 8, 8, 8, 8, 8, 8, 8, 8 },
			{ 75, 75, 75, 75, 75, 75, 75, 75, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 9, 9, 9, 9, 9, 9, 9, 9 },
			{ 74, 74, 74, 74, 74, 74, 74, 74, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 10, 10, 10, 10, 10, 10, 10, 10 },
			{ 73, 73, 73, 73, 73, 73, 73, 73, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 11, 11, 11, 11, 11, 11, 11, 11 },
			{ 72, 72, 72, 72, 72, 72, 72, 72, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 12, 12, 12, 12, 12, 12, 12, 12 },
			{ 71, 71, 71, 71, 71, 71, 71, 71, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 13, 13, 13, 13, 13, 13, 13, 13 },
			{ 70, 70, 70, 70, 70, 70, 70, 70, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 14, 14, 14, 14, 14, 14, 14, 14 },
			{ 69, 69, 69, 69, 69, 69, 69, 69, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 15, 15, 15, 15, 15, 15, 15, 15 },
			{ 68, 68, 68, 68, 68, 68, 68, 68, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 16, 16, 16, 16, 16, 16, 16, 16 },
			{ 67, 67, 67, 67, 67, 67, 67, 67, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 17, 17, 17, 17, 17, 17, 17, 17 },
			{ 66, 66, 66, 66, 66, 66, 66, 66, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 18, 18, 18, 18, 18, 18, 18, 18 },
			{ 65, 65, 65, 65, 65, 65, 65, 65, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 19, 19, 19, 19, 19, 19, 19, 19 },
			{ 64, 64, 64, 64, 64, 64, 64, 64, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 20, 20, 20, 20, 20, 20, 20, 20 },
			{ 63, 63, 63, 63, 63, 63, 63, 63, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 21, 21, 21, 21, 21, 21, 21, 21 },
			{ 62, 62, 62, 62, 62, 62, 62, 62, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 22, 22, 22, 22, 22, 22, 22, 22 },
			{ 61, 61, 61, 61, 61, 61, 61, 61, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 23, 23, 23, 23, 23, 23, 23, 23 },
			{ 60, 60, 60, 60, 60, 60, 60, 60, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 24, 24, 24, 24, 24, 24, 24, 24 },
			{ 59, 59, 59, 59, 59, 59, 59, 59, 59, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					25, 25, 25, 25, 25, 25, 25, 25 },
			{ 59, 59, 59, 59, 59, 58, 58, 58, 57, 57, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 26,
					26, 26, 26, 26, 26, 26, 26, 26 },
			{ 59, 59, 59, 58, 58, 58, 57, 57, 56, 56, 55, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000,
					1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 29, 28, 27,
					26, 26, 26, 26, 26, 26, 26 },
			{ 58, 58, 58, 58, 57, 57, 57, 56, 56, 55, 54, 52, 51, 50, 49, 48,
					47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33,
					32, 30, 29, 28, 27, 27, 27, 26, 26, 26, 26 },
			{ 58, 58, 57, 57, 57, 56, 56, 56, 55, 54, 53, 52, 51, 50, 49, 48,
					47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33,
					32, 31, 29, 29, 28, 27, 27, 27, 27, 27, 26 },
			{ 57, 57, 57, 56, 56, 56, 56, 55, 55, 54, 53, 52, 51, 50, 49, 48,
					47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33,
					32, 31, 30, 29, 29, 28, 28, 27, 27, 27, 27 },
			{ 57, 57, 56, 56, 56, 56, 55, 55, 54, 54, 53, 52, 51, 50, 49, 48,
					47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33,
					32, 31, 30, 29, 29, 28, 28, 28, 28, 27, 27 },
			{ 57, 56, 56, 56, 56, 55, 55, 54, 54, 53, 53, 52, 51, 50, 49, 48,
					47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33,
					32, 31, 31, 30, 29, 29, 28, 28, 28, 28, 28 },
			{ 1000, 56, 56, 56, 55, 55, 55, 54, 54, 53, 53, 52, 51, 50, 49, 48,
					47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34, 33,
					32, 31, 31, 30, 29, 29, 29, 28, 28, 28, 28 },
			{ 1000, 1000, 56, 55, 55, 55, 54, 54, 54, 53, 53, 52, 51, 50, 49,
					48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34,
					33, 32, 31, 31, 30, 30, 29, 29, 29, 28, 1000, 1000 },
			{ 1000, 1000, 1000, 55, 55, 54, 54, 54, 53, 53, 53, 52, 51, 50, 49,
					48, 47, 46, 45, 44, 43, 42, 41, 40, 39, 38, 37, 36, 35, 34,
					33, 32, 31, 31, 30, 30, 29, 29, 29, 1000, 1000, 1000 } };

	private static PetRacing instance;
	private STATUS s;

	private final ArrayList<L1PcInstance> entermember = new ArrayList<L1PcInstance>();
	private final ArrayList<L1PcInstance> playmember = new ArrayList<L1PcInstance>();

	private Random _random = new Random(System.nanoTime());

	public static PetRacing getInstance() {
		if (instance == null) {
			instance = new PetRacing();
		}
		return instance;
	}

	private int msgCount = 12;

	@Override
	public void run() {
		try {
			setStatus(STATUS.PLAY);
			/*
			 * setStatus(STATUS.REST); while(true){ switch (getStatus()) { case
			 * ENTERREADY: npcChat("펫레이싱 경기 입장 2분 전입니다."); Thread.sleep(60000L);
			 * npcChat("펫레이싱 경기 입장 1분 전입니다."); Thread.sleep(30000L); // 2분정도
			 * 입장유저받을겸 기다린다 120000L npcChat("펫레이싱 경기 입장 30초 전입니다.");
			 * enterMsg("펫레이싱 경기 입장 30초 전입니다."); Thread.sleep(10000L);
			 * npcChat("펫레이싱 경기 입장 20초 전입니다.");
			 * enterMsg("펫레이싱 경기 입장 20초 전입니다."); Thread.sleep(10000L);
			 * npcChat("펫레이싱 경기 입장 10초 전입니다.");
			 * enterMsg("펫레이싱 경기 입장 10초 전입니다."); Thread.sleep(5000L);
			 * npcChat("펫레이싱 경기 입장 5초 전입니다."); enterMsg("펫레이싱 경기 입장 5초 전입니다.");
			 * Thread.sleep(1000L); npcChat("펫레이싱 경기 입장 4초 전입니다.");
			 * enterMsg("펫레이싱 경기 입장 4초 전입니다."); Thread.sleep(1000L);
			 * npcChat("펫레이싱 경기 입장 3초 전입니다."); enterMsg("펫레이싱 경기 입장 3초 전입니다.");
			 * Thread.sleep(1000L); npcChat("펫레이싱 경기 입장 2초 전입니다.");
			 * enterMsg("펫레이싱 경기 입장 2초 전입니다."); Thread.sleep(1000L);
			 * npcChat("펫레이싱 경기 입장 1초 전입니다."); enterMsg("펫레이싱 경기 입장 1초 전입니다.");
			 * Thread.sleep(1000L); if(checkEnoughEnterMember()){
			 * sendMessage(MSG.ENTER); } npcChat("펫레이싱 경기 시작 대기 중입니다.");
			 * Thread.sleep(10000L); npcChat("펫레이싱 경기 시작 대기 중입니다.");
			 * Thread.sleep(5000L); setStatus(STATUS.READY); break; case READY:
			 * checkFinalPlayMember(); if (checkEnoughStartMember()){
			 * sendMessage(MSG.WAIT_START); setStatus(STATUS.PLAY); } else {
			 * sendMessage(MSG.NOT_ENOUGH_STARTMEMBERS); getOutPetRacing();
			 * allClear(); npcChat("인원 부족으로 펫레이싱 경기가 종료 되었습니다."); } break; case
			 * PLAY: timeover = false; //Thread.sleep(3000L);
			 * doPolyPlayMember(); Thread.sleep(10000L); countDownStartGame();
			 * //5,4,3,2,1 Thread.sleep(5000L); npcChat("펫레이싱 경기가 시작 되었습니다.");
			 * checkWinnerCount(); startPlayGameMembersGameTime();
			 * petRacingStartDoorOpen(); // 5분 체크 시작 int j = 1; while (j <=
			 * 600){ if(getStatus() == STATUS.END){ break; } Thread.sleep(500L);
			 * playmember_check(); RankList(); // 실시간 등수 변하는부분 ++j; } // 5분 체크
			 * 종료 if (notWinnerGame()) timeover = true; setStatus(STATUS.END);
			 * break; case END: sendMessage(MSG.GAMEEND); Thread.sleep(10000L);
			 * playGameMembersDisplayPacketClear(); getOutPetRacing(); if
			 * (finalCheckFinishMember()){ giveItemToWinnerMember(); }
			 * allClear(); npcChat("펫레이싱 경기가 종료 되었습니다."); break; case REST:
			 * if(msgCount-- < 1){ npcChat("잼있는 펫레이싱 경기 하구 가세요~"); msgCount =
			 * 12; } Thread.sleep(1000L); break; default: Thread.sleep(1000L);
			 * break; } }
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public int getWmp(int x, int y) {
		return wmp[x - Start_X][y - Start_Y];
	}

	private static final S_PacketBox pb_end = new S_PacketBox(
			S_PacketBox.MINIGAME_END);
	private static final S_PacketBox pb_clear = new S_PacketBox(
			S_PacketBox.MINIGAME_TIME_CLEAR);

	private void playmember_check() {
		L1PcInstance[] list = getPlayMemberArray();
		for (L1PcInstance pc : list) {
			if (pc == null)
				continue;
			if (pc.getMapId() != PETRACE_MAPID) {
				pc.sendPackets(pb_end);
				pc.sendPackets(pb_clear);
				pc.setPetRacing(false);
				pc.setPetRacingLAB(1);
				pc.setPetRacingCheckPoint(162);
			}
		}
		list = null;
	}

	private void RankList() {
		rankList = getPlayMemberArray();
		int PlayerLength = rankList.length;
		L1PcInstance temp = null;
		for (int i = 0; i < PlayerLength; i++) {
			for (int j = i + 1; j < PlayerLength; j++) {
				if (rankList[i].getPetRacingLAB() > rankList[j]
						.getPetRacingLAB()) {
					continue;
				}
				if (rankList[i].getPetRacingLAB() < rankList[j]
						.getPetRacingLAB()
						|| rankList[i].getPetRacingCheckPoint() > rankList[j]
								.getPetRacingCheckPoint()) {
					temp = rankList[i];
					rankList[i] = rankList[j];
					rankList[j] = temp;
				}
			}
		}
		int i = 0;
		for (L1PcInstance pc : rankList) {
			S_Game_PetRacing gpr = new S_Game_PetRacing(i++);
			pc.sendPackets(gpr, true);
		}
	}

	private void clearFinishMembers() {
		if (finishMember == null)
			return;
		for (int i = 0, c = finishMember.length; i < c; i++) {
			finishMember[i] = null;
		}
	}

	private void petRacingStartDoorClose() {
		L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(8000);
		if (door != null) {
			if (door.getOpenStatus() == ActionCodes.ACTION_Open) {
				door.close();
			}
		}
	}

	private void giveItemToWinnerMember() {
		L1ItemInstance Present = ItemTable.getInstance().createItem(
				L1ItemId.MINIGAME_PRESENT);
		for (L1PcInstance pc : finishMember) {
			if (_random.nextInt(10) <= 2) {
				pc.getInventory().storeItem(L1ItemId.PETRACING_WINNER_PIECE, 1);
			}
			pc.getInventory().storeItem(L1ItemId.MINIGAME_PRESENT, 1);
			S_ServerMessage sm_403 = new S_ServerMessage(403,
					Present.getLogName());
			pc.sendPackets(sm_403, true);
		}
	}

	public void addFinishMember(L1PcInstance pc) {
		if (isPlay())
			setStatus(STATUS.END);
		if (finishMemberCount >= winnersCount)
			return;
		if (finishMember[finishMemberCount] != pc)
			finishMember[finishMemberCount++] = pc;
	}

	private boolean finalCheckFinishMember() {
		return winnersCount <= finishMemberCount ? true : false;
	}

	private void playGameMembersDisplayPacketClear() {
		L1PcInstance[] list = getPlayMemberArray();
		for (L1PcInstance pc : list) {
			if (pc.getMapId() == PETRACE_MAPID) {
				pc.sendPackets(pb_end);
				pc.sendPackets(pb_clear);
				pc.setPetRacing(false);
				pc.setPetRacingLAB(1);
				pc.setPetRacingCheckPoint(162);
			}
		}
		list = null;
	}

	private boolean notWinnerGame() {
		return (isPlay() && getPlayMemberCount() != 0) ? true : false;
	}

	private void petRacingStartDoorOpen() {
		L1DoorInstance door = DoorSpawnTable.getInstance().getDoor(8000);
		if (door != null) {
			if (door.getOpenStatus() == ActionCodes.ACTION_Close) {
				door.open();
			}
		}
	}

	private static final S_PacketBox pb_start = new S_PacketBox(
			S_PacketBox.MINIGAME_START_TIME);

	private void startPlayGameMembersGameTime() {
		L1PcInstance[] list = getPlayMemberArray();
		for (L1PcInstance pc : list) {
			pc.sendPackets(pb_start);
		}
		list = null;
	}

	private void checkWinnerCount() {
		winnersCount = getPlayMemberCount() > 7 ? 3
				: (getPlayMemberCount() > 4 ? 2 : 1);
		finishMember = new L1PcInstance[winnersCount];
	}

	private static final S_PacketBox pb_count = new S_PacketBox(
			S_PacketBox.MINIGAME_START_COUNT);
	private static final S_Game_PetRacing gpr4_1 = new S_Game_PetRacing(4, 1);

	private void countDownStartGame() {
		RankList();
		L1PcInstance[] list = getPlayMemberArray();
		for (L1PcInstance pc : list) {
			pc.sendPackets(pb_count);
			pc.sendPackets(gpr4_1);
		}
		list = null;
	}

	private static final int[] normalPetPolyId = { 4038, 1540, 929, 934, 979,
			3134, 3211, 5065, 3918, 938, 2145, 1022, 3182 };

	private void doPolyPlayMember() {
		int polyid = ran.nextInt(normalPetPolyId.length);
		setStartPolyId(normalPetPolyId[polyid]);
		L1PcInstance[] list = getPlayMemberArray();
		for (L1PcInstance pc : list) {
			L1PolyMorph.doPoly(pc, normalPetPolyId[polyid], 600,
					L1PolyMorph.MORPH_BY_LOGIN);
			pc.setPetRacingLAB(1);
			pc.setPetRacingCheckPoint(162);
		}
		list = null;
	}

	private void allClear() {
		setStatus(STATUS.REST);
		winnersCount = 0;
		finishMemberCount = 0;
		clearFinishMembers();
		clearEnterMember();
		clearPlayMember();
		petRacingStartDoorClose();
	}

	private void getOutPetRacing() {
		L1SkillUse l1skilluse = null;
		L1PcInstance[] list = getPlayMemberArray();
		for (L1PcInstance pc : list) {
			if (pc != null) {
				if (pc.getMapId() == PETRACE_MAPID) {
					if (getStatus() == STATUS.READY) {
						pc.getInventory().storeItem(L1ItemId.ADENA, 1000); // 1000
																			// 아데나
																			// 지급
					}
					l1skilluse = new L1SkillUse();
					l1skilluse.handleCommands(pc, L1SkillId.CANCELLATION,
							pc.getId(), pc.getX(), pc.getY(), null, 0,
							L1SkillUse.TYPE_LOGIN);
					l1skilluse = null;
					int classId = pc.getClassId();
					if (pc.getGfxId().getTempCharGfx() != classId) {
						pc.getGfxId().setTempCharGfx(classId);
						S_ChangeShape cs = new S_ChangeShape(pc.getId(),
								classId);
						pc.sendPackets(cs);
						Broadcaster.broadcastPacket(pc, cs, true);
					}
					if (pc.getSkillEffectTimerSet().hasSkillEffect(
							L1SkillId.STATUS_DRAGONPERL)) {
						pc.getSkillEffectTimerSet().killSkillEffectTimer(
								L1SkillId.STATUS_DRAGONPERL);
						S_PacketBox pb_dp = new S_PacketBox(
								S_PacketBox.DRAGONPERL, 0, 0);
						pc.sendPackets(pb_dp, true);
						S_DRAGONPERL dp = new S_DRAGONPERL(pc.getId(), 0);
						Broadcaster.broadcastPacket(pc, dp);
						pc.sendPackets(dp, true);
						pc.set진주속도(0);
					}

					int[] loc = Getback.GetBack_Location(pc, true);
					L1Teleport.teleport(pc, loc[0], loc[1], (short) loc[2], 5,
							true);
					loc = null;
				}
			}
		}
		list = null;
	}

	private boolean checkEnoughStartMember() {
		return getPlayMemberCount() >= LIMIT_STARTMEMBER_COUNT ? true : false;
	}

	private boolean checkEnoughEnterMember() {
		return getEnterMemberCount() >= LIMIT_ENTERMEMBER_COUNT ? true : false;
	}

	private void checkFinalPlayMember() {
		L1PcInstance[] list = getPlayMemberArray();
		for (L1PcInstance pc : list) {
			if (pc.getMapId() != PETRACE_MAPID) {
				removePlayMember(pc);
				pc.setPetRacing(false);
				pc.setPetRacingLAB(1);
				pc.setPetRacingCheckPoint(162);
			}
		}
		list = null;
	}

	public void addMember(L1PcInstance pc) {
		if (!isEnterMember(pc)) {
			entermember.add(pc);
			String count = Integer.toString(getEnterMemberCount());

			pc.sendPackets(new S_SystemMessage(count
					+ "번째 순번으로 펫매치에 입장 예약되었습니다."), true);
			count = null;
			if (getStatus() == STATUS.REST)
				setStatus(STATUS.ENTERREADY);
		} else {

			S_SystemMessage sm1230 = new S_SystemMessage("이미 펫매치 입장 예약되어있습니다.");
			pc.sendPackets(sm1230, true);
		}
	}

	private L1MerchantInstance keeper = null;

	private void npcChat(String chat) {
		if (keeper == null) {
			for (L1MerchantInstance npc : L1World.getInstance()
					.getAllMerchant()) {
				if (npc.getNpcId() == 4206002) {
					keeper = npc;
					break;
				}
			}
		}
		if (keeper != null) {
			S_NpcChatPacket nc = new S_NpcChatPacket(keeper, chat, 0);
			Broadcaster.broadcastPacket(keeper, nc, true);
		}
	}

	private void enterMsg(String msg) {
		S_SystemMessage smm = new S_SystemMessage(msg);
		L1PcInstance[] list = getEnterMemberArray();
		for (L1PcInstance pc : list) {
			if (pc != null)
				pc.sendPackets(smm);
		}
		list = null;
	}

	private void sendMessage(MSG message) {
		switch (message) {
		case ENTER:
			S_Message_YN ms = new S_Message_YN(1256, "");
			L1PcInstance[] list = getEnterMemberArray();
			// 경기장에 입장 하시겠습니까
			for (L1PcInstance pc : list) {
				if (pc != null)
					pc.sendPackets(ms);
			}
			list = null;
			break;
		case WAIT_START:
			S_ServerMessage sm = new S_ServerMessage(1257);
			L1PcInstance[] list2 = getPlayMemberArray();
			// 잠시 후 경기가 시작됩니다.
			for (L1PcInstance pc : list2)
				if (pc != null)
					pc.sendPackets(sm);
			list2 = null;
			break;
		case NOT_ENOUGH_STARTMEMBERS:
			S_ServerMessage sm1264 = new S_ServerMessage(1264);
			L1PcInstance[] list3 = getPlayMemberArray();
			// 경기 인원이 부족하여 마을로 꺼지십시오
			for (L1PcInstance pc : list3)
				if (pc != null)
					pc.sendPackets(sm1264);
			list3 = null;
			break;
		case GAMEEND:
			S_ServerMessage sm1263 = new S_ServerMessage(1263);
			S_PacketBox pb1 = new S_PacketBox(
					S_PacketBox.MINIGAME_10SECOND_COUNT);
			L1PcInstance[] list4 = getPlayMemberArray();
			for (L1PcInstance pc : list4) {
				if (pc != null) {
					if (timeover)
						pc.sendPackets(sm1263);
					else
						pc.sendPackets(pb1);
				}
			}
			list4 = null;
			break;
		default:
			break;
		}

	}

	public void RacingCheckPoint(L1PcInstance pc) {
		int x = pc.getX();
		int y = pc.getY();
		try {
			int point = getWmp(x, y);
			if (pc.getPetRacingCheckPoint() <= 2 && point >= 162) {
				pc.setPetRacingCheckPoint(point);
				pc.setPetRacingLAB(pc.getPetRacingLAB() + 1);
				if (pc.getPetRacingLAB() == 2) {
					S_Game_PetRacing gp2 = new S_Game_PetRacing(4, 2);
					pc.sendPackets(gp2, true);
				} else if (pc.getPetRacingLAB() == 3) {
					S_Game_PetRacing gp3 = new S_Game_PetRacing(4, 3);
					pc.sendPackets(gp3, true);
				} else if (pc.getPetRacingLAB() == 4) {
					S_Game_PetRacing gp4 = new S_Game_PetRacing(4, 4);
					pc.sendPackets(gp4, true);
				} else if (pc.getPetRacingLAB() == 5)
					addFinishMember(pc);
			}
			int point2 = point - pc.getPetRacingCheckPoint();
			if (point2 >= -20) {
				if (point < pc.getPetRacingCheckPoint())
					pc.setPetRacingCheckPoint(point);
				else if (point > pc.getPetRacingCheckPoint())
					pc.setPetRacingCheckPoint(point);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static final int[] doPolyId = { 931, 938, 2145, 929, 934, 2734,
			1642, 1540, 3134, 4038, 936, 1310, 3199, 3132, 3143, 3107, 3182,
			3211, 3154, 3188, 3156, 3178, 3184, 6325, 6310, 5089, 6096, 6100,
			5919, 6322, 6314, 931, 938, 2145, 929, 934, 2734, 1642, 1540, 3134,
			4038, 936, 1310, 3199, 3132, 3143, 3107, 3182, 3211, 3154, 3188,
			3156, 3178, 3184, 6325, 6310, 5089, 6096, 6100, 5919, 6322, 6314,
			931, 938, 2145, 929, 934, 2734, 1642, 1540, 3134, 4038, 936, 1310,
			3199, 3132, 3143, 3107, 3182, 3211, 3154, 3188, 3156, 3178, 3184,
			6325, 6310, 5089, 6096, 6100, 5919, 6322, 6314, 945, 947, 945, 947,
			4168, 4168, 2544, 7539, 7870, 2001 };

	public void pushPolyTrap(L1PcInstance pc) {
		// int[] doPolyId = {4133, 3199, 3107, 3132, 3178, 3184, 3156, 1052,
		// 945,
		// 1649, 55, 2541, 1642, 4168, 29, 3188, 7539/*1245*/, 7870/*1590*/,
		// 2001};
		int polyid = ran.nextInt(doPolyId.length);
		L1PolyMorph.doPoly(pc, doPolyId[polyid], 30, L1PolyMorph.MORPH_BY_NPC);
		pc.setTelType(4);
		S_SabuTell st = new S_SabuTell(pc);
		pc.sendPackets(st, true);
	}

	public void pushAccelTrap(L1PcInstance pc) {
		S_SkillHaste sh = new S_SkillHaste(pc.getId(), 1, 30);
		S_SkillBrave sb = new S_SkillBrave(pc.getId(), 1, 30);
		pc.sendPackets(sh, true);
		pc.sendPackets(sb, true);
		pc.getSkillEffectTimerSet().setSkillEffect(43, 1000 * 30);
		pc.getSkillEffectTimerSet().setSkillEffect(1000, 1000 * 30);
		pc.getMoveState().setMoveSpeed(1);
		pc.getMoveState().setBraveSpeed(1);
		pc.setTelType(4);
		S_SabuTell st = new S_SabuTell(pc);
		pc.sendPackets(st, true);
	}

	public L1PcInstance[] getRank() {
		return rankList;
	}

	private void setStartPolyId(int i) {
		startPolyId = i;
	}

	public int getStartPolyId() {
		return startPolyId;
	}

	public boolean isReady() {
		return (getStatus() == STATUS.READY) ? true : false;
	}

	public boolean isPlay() {
		return (getStatus() == STATUS.PLAY) ? true : false;
	}

	private void setStatus(STATUS i) {
		s = i;
	}

	private STATUS getStatus() {
		return s;
	}

	public void addEnterMember(L1PcInstance pc) {
		entermember.add(pc);
	}

	public boolean isEnterMember(L1PcInstance pc) {
		return entermember.contains(pc);
	}

	public void removeEnterMember(L1PcInstance pc) {
		entermember.remove(pc);
	}

	public void clearEnterMember() {
		entermember.clear();
	}

	public int getEnterMemberCount() {
		return entermember.size();
	}

	public L1PcInstance[] getEnterMemberArray() {
		return entermember.toArray(new L1PcInstance[getEnterMemberCount()]);
	}

	public void addPlayMember(L1PcInstance pc) {
		playmember.add(pc);
	}

	public int getPlayMemberCount() {
		return playmember.size();
	}

	public void removePlayMember(L1PcInstance pc) {
		playmember.remove(pc);
	}

	public void clearPlayMember() {
		playmember.clear();
	}

	public boolean isPlayMember(L1PcInstance pc) {
		return playmember.contains(pc);
	}

	public L1PcInstance[] getPlayMemberArray() {
		return playmember.toArray(new L1PcInstance[getPlayMemberCount()]);
	}

}
