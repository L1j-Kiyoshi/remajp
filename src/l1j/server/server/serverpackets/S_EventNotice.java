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

import java.util.Iterator;
import java.util.Map;

import l1j.server.GameSystem.Dungeon.DungeonInfo;
import l1j.server.GameSystem.Dungeon.DungeonSystem;
import l1j.server.GameSystem.RotationNotice.RotationNoticeTable;
import l1j.server.GameSystem.RotationNotice.RotationNoticeTable.EventNotice;
import l1j.server.server.Opcodes;
import l1j.server.server.model.L1Quest;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_EventNotice extends ServerBasePacket {
	private static final String S_EventNotice = "[S] S_EventNotice";

	private byte[] _byte = null;

	public S_EventNotice() {
  		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeC(0x8d);
  		writeC(0x00);
  		
  		writeC(0x08);
  		writeC(0x01);
  		
  		writeC(0x10);
  		writeC(0x01);
  		
  		for (EventNotice Notice : RotationNoticeTable.getInstance().getRotationList()){
  			String Info = null;
  			long Length = 0, MsLength = 0, Time = 0, TimeEnd = 0;
  			switch(Notice.InfoType){
  				case 2:
	  				Length = 10;
	  				if(Notice.InfoEndTime == null || 
	  				  System.currentTimeMillis() >= Notice.InfoEndTime.getTime()) continue;
	  				if(Notice.InfoLink != null)
	  					Length += Notice.InfoLink.getBytes().length;
	  				for (String Massage : Notice.Massage)
	  					MsLength += Massage.getBytes().length + 5;
	  				for (int MassageAction : Notice.MassageAction)
	  					MsLength += size7B(MassageAction);
	  			
	  				Info = Notice.Info;
	  				if(Notice.InfoTime != null){
	  					Time = Notice.InfoTime.getTime() / 1000;
	  				}else Time = System.currentTimeMillis() / 1000;
	  				if(Notice.InfoEndTime != null){
	  					TimeEnd = Notice.InfoEndTime.getTime() / 1000;
	  				}else TimeEnd = System.currentTimeMillis() / 1000 + (60 * 60 * 24) ;
	  				Length += MsLength + Info.getBytes().length  + size7B((int)Time) + size7B((int)TimeEnd);
	  				
	  				/** 전체 사이즈 */
	  				writeC(0x1a); 
	  				writeC((int)Length); 
	  				
	  				/** 액션넘버 액션마다 다른 이미지 출력 */
	  				/** 액션넘버 넣었을시 자동으로 c값 확인시 08 다음에 액션값으로 출력 */
	  				writeC(0x08);
	  				writeBit(Notice.InfoAction); 
	  				
	  				/** 물음표 클릭시 될 링크 파워북꺼만 가능 */
	  				writeC(0x1a); 
	  				writeC(Notice.InfoLink != null ? Notice.InfoLink.getBytes().length : 0); 
	  				if(Notice.InfoLink != null) writeByte(Notice.InfoLink.getBytes()); 
	  				
	  				/** 출력될 메세지 */
	  				writeC(0x22);
	  				writeC(Info.getBytes().length); 
	  				writeByte(Info.getBytes()); 
	  				
	  				/** 등장시간 */
	  				writeC(0x28);
	  				write7B(Time);
	  				
	  				/** 삭제시간 */
	  				writeC(0x30);
	  				write7B(TimeEnd);
	  				
	  				/** 사이즈 계산 */
	  				writeC(0x42);
	  				writeBit(MsLength);
	  				for (int i = 0; i < Notice.Massage.length; i++){
	  					writeC(0x0a);
	  					writeBit(3 + Notice.Massage[i].getBytes().length + size7B(Notice.MassageAction[i]));
	  					
	  					writeC(0x08);
	  					writeBit(Notice.MassageAction[i]);
	  					
	  					writeC(0x12);
	  					writeC(Notice.Massage[i].getBytes().length);
	  					writeByte(Notice.Massage[i].getBytes());
	  				}
	  				break;
  			}
  		}
  		writeH(0);
  	}
  			
  	public S_EventNotice(L1PcInstance pc, boolean Event, boolean Delete) {
  		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeC(0x8d);
  		writeC(0x00);
  		
  		writeC(0x08);
  		writeC(0x01);
  		
  		writeC(0x10);
  		writeC(0x01);
  		
  		for (EventNotice Notice : RotationNoticeTable.getInstance().getRotationList()){
  			String Info = null , TeleportMs = "4654";
  			long Length = 0, Time = 0, TimeEnd = 0;
  			switch(Notice.InfoType){
				case 0:
  					Length = 13 + size7B(Notice.Teleport[3]);
  					/** 조건 문 : 이벤트가 참일이고 액션이 19 ~ 20이라면 리턴 처리하지 않는다 */
  					if(Event && (Notice.InfoAction >= 19 && Notice.InfoAction <= 20)){ continue;
  					}else if(!pc.getInventory().checkItem(60384) && pc.getQuest().get_step(L1Quest.QUEST_55_Roon) == 0){
						Info = "엘릭서 룬(55레벨)";
					}else if(!pc.getInventory().checkItem(60391) && pc.getQuest().get_step(L1Quest.QUEST_70_Roon) == 0 && pc.getLevel() >= 70){
						Info = "엘릭서 룬(70레벨)";
					}else if(!pc.getInventory().checkItem(60398) && pc.getQuest().get_step(L1Quest.QUEST_80_Roon) == 0 && pc.getLevel() >= 80){
						Info = "엘릭서 룬(80레벨)";
					}else if(!pc.getInventory().checkItem(60405) && pc.getQuest().get_step(L1Quest.QUEST_85_Roon) == 0 && pc.getLevel() >= 85){
						Info = "엘릭서 룬(85레벨)";
					}else if(!pc.getInventory().checkItem(60412) && pc.getQuest().get_step(L1Quest.QUEST_90_Roon) == 0 && pc.getLevel() >= 90){
						Info = "엘릭서 룬(90레벨)";
					}else continue;
 					if(Notice.InfoLink != null)
  						Length += Notice.InfoLink.getBytes().length;
		  			if(Notice.InfoTime != null){
		  				Time = Notice.InfoTime.getTime() / 1000;
		  			}else Time = System.currentTimeMillis() / 1000;
		  			if(Notice.InfoEndTime != null){
		  				TimeEnd = Notice.InfoEndTime.getTime() / 1000;
		  			}else TimeEnd = System.currentTimeMillis() / 1000 + (60 * 60 * 24) ;
		  			
		  			Length += Info.getBytes().length + TeleportMs.getBytes().length;
		  			Length += size7B((int)Time) + size7B((int)TimeEnd);
		  			
		  			/** 전체 사이즈 */
		  			writeC(0x1a); 
		  			writeC((int)Length); 
		  			
		  			/** 액션넘버 액션마다 다른 이미지 출력 */
		  			/** 액션넘버 넣었을시 자동으로 c값 확인시 08 다음에 액션값으로 출력 */
		  			writeC(0x08);
		  			writeBit(Notice.InfoAction); 
		  			
		  			/** 물음표 클릭시 될 링크 파워북꺼만 가능 */
		  			writeC(0x1a); 
		  			writeC(Notice.InfoLink != null ? Notice.InfoLink.getBytes().length : 0); 
		  			if(Notice.InfoLink != null){
		  				writeByte(Notice.InfoLink.getBytes()); 
		  			}else writeC(0x00);
		  			
		  			/** 출력될 메세지 */
		  			writeC(0x22);
		  			writeC(Info.getBytes().length); 
		  			writeByte(Info.getBytes()); 
		  			
		  			/** 등장시간 */
		  			writeC(0x28);
		  			write7B(Time);
		  			
		  			/** 삭제시간 */
		  			writeC(0x30);
		  			write7B(!Delete ? TimeEnd : System.currentTimeMillis() / 1000);
		  			
		  			/** 이벤트시간에 출력시 텔가능력 */
		  			writeC(0x3a);
		  			writeC(TeleportMs.getBytes().length + size7B(Notice.Teleport[3]) + 3); 
		  			
		  			/** 메세지 띠울시 그메세지 넘버 */
		  			writeC(0x0a);
		  			writeC(TeleportMs.getBytes().length);
		  			writeByte(TeleportMs.getBytes()); 
		  			
		  			/** 아데나 얼마나 요구할것인가 */
		  			writeC(0x10); 
		  			writeBit(Notice.Teleport[3]);
  					break;
  					
  				case 1:
  					Length = 13 + size7B(Notice.Teleport[3]);
  					if(Event && (Notice.InfoAction >= 19 && Notice.InfoAction <= 20)) continue;
  					if(Notice.InfoLink != null) Length += Notice.InfoLink.getBytes().length;
					
  					Info = Notice.Info;
		  			if(Notice.InfoTime != null){
		  				Time = Notice.InfoTime.getTime() / 1000;
		  			}else Time = System.currentTimeMillis() / 1000;
		  			if(Notice.InfoEndTime != null){
		  				TimeEnd = Notice.InfoEndTime.getTime() / 1000;
		  			}else TimeEnd = System.currentTimeMillis() / 1000 + (60 * 60 * 24) ;
		  			Length += Info.getBytes().length + TeleportMs.getBytes().length;
		  			Length += size7B((int)Time) + size7B((int)TimeEnd);
		  			
		  			/** 전체 사이즈 */
		  			writeC(0x1a); 
		  			writeC((int)Length); 
		  			
		  			/** 액션넘버 액션마다 다른 이미지 출력 */
		  			/** 액션넘버 넣었을시 자동으로 c값 확인시 08 다음에 액션값으로 출력 */
		  			writeC(0x08);
		  			writeBit(Notice.InfoAction); 
		  			
		  			/** 물음표 클릭시 될 링크 파워북꺼만 가능 */
		  			writeC(0x1a); 
		  			writeC(Notice.InfoLink != null ? Notice.InfoLink.getBytes().length : 0); 
		  			if(Notice.InfoLink != null){
		  				writeByte(Notice.InfoLink.getBytes()); 
		  			}else writeC(0x00);
		  			
		  			/** 출력될 메세지 */
		  			writeC(0x22);
		  			writeC(Info.getBytes().length); 
		  			writeByte(Info.getBytes()); 
		  			
		  			/** 등장시간 */
		  			writeC(0x28);
		  			write7B(Time);
		  			
		  			/** 삭제시간 */
		  			writeC(0x30);
		  			write7B(!Delete ? TimeEnd : System.currentTimeMillis() / 1000);
		  			
		  			/** 이벤트시간에 출력시 텔가능력 */
		  			writeC(0x3a);
		  			writeC(TeleportMs.getBytes().length + size7B(Notice.Teleport[3]) + 3); 
		  			
		  			/** 메세지 띠울시 그메세지 넘버 */
		  			writeC(0x0a);
		  			writeC(TeleportMs.getBytes().length);
		  			writeByte(TeleportMs.getBytes()); 
		  			
		  			/** 아데나 얼마나 요구할것인가 */
		  			writeC(0x10); 
		  			writeBit(Notice.Teleport[3]);
  					break;
  			}
  		}
  		writeH(0);
  	}
	/**
	public S_EventNotice(int type,int time,int duration,boolean mod) {
		
		String name;
		switch(type){
			case 1:
				name = "에르자베";
				break;
			case 2:
				name = "샌드 웜";
				break;
			case 3:
				name = "무한 대전(저녁 9시)";
				break;
			case 4:
				name = "55레벨 룬";
				break;
			case 5:
				name = "귀걸이 개방";
				break;
			case 6:
				name = "휘장 개방";
				break;
			case 7:
				name = "70레벨 룬";
				break;
			case 8:
				name = "76반지 개방";
				break;
			case 9:
				name = "81반지 개방";
				break;
			case 10:
				name = "83견갑 개방";
				break;
			case 11:
				name = "잊혀진 섬 개방";
				break;
			case 12:
				name = "80레벨 룬";
				break;
			case 13:
				name = "85레벨 룬";
				break;
			case 14:
				name = "90레벨 룬";
				break;
			case 15:
				name = "잊혀진 섬 개방";
				break;
			default:
				name = "Joyserver";
				break;
		}
		int length = name.getBytes().length;
		
		int realTime,realTime2;
		if(mod){
			realTime =RealTimeClock.getInstance().getRealTime().getSeconds()+time;  //몇시간 남음 표기시간
		
			if(realTime <0 )
				realTime = -realTime;
			
			realTime2 =RealTimeClock.getInstance().getRealTime().getSeconds()+duration; //사라지는 시간
		
			if(realTime2 < 0 )
				realTime2 = -realTime2;
			
		}else{
			
			realTime =RealTimeClock.getInstance().getRealTime().getSeconds();
			
			if(realTime <0 )
				realTime = -realTime;
			
			realTime2 =RealTimeClock.getInstance().getRealTime().getSeconds();
			
			if(realTime2 < 0 )
				realTime2 = -realTime2;
		}
		int total = 0x1d+name.getBytes().length;
		
	
		writeC(Opcodes.S_EXTENDED_PROTOBUF);
		writeC(0x8d);
		writeC(0x00);
		writeC(0x8);
		writeC(0x1);
		writeC(0x10);
		writeC(0x1);

		writeC(0x1a); // 0x1a
		writeBit(total);
		writeC(0x8);
		writeC(type);
			
		writeC(0x1a);
		writeC(0x00);
		writeC(0x22);
		writeC(length);
		writeByte(name.getBytes());
		
		
		writeC(0x28);
		
		
		writeBit(realTime);
		
		writeC(0x30);

		writeBit(realTime2);
		
		writeC(0x3a);
		writeC(0x09);
		writeC(0x0a);
		writeC(0x04);
		writeC(0x34);
		writeC(0x36);
		writeC(0x35);
		writeC(0x34);
		writeC(0x10);
		writeC(0x90);
		writeC(0x4e);
		writeH(0x00);
		}
		
				
	
	*/
  	
	/** 접속 관련 패킷 */
	public static final int InDungeon = 0x08b6;
	public static final int InDungeExit = 0x08b8;
	public static final int InDungeStart = 0x08b2;
	public static final int InDungeonOut = 0x08bb;
	public static final int InDungeonOutUse = 0x08b9;
	public static final int InDungeonOutExit = 0x08bc;
	public static final int InDungeonList = 0x08a4;
	public static final int InDungeonSlot = 0x08aa;
	public static final int InDungeonStancet = 0x08ad;
	public static final int InDungeonType = 0x08a8;
	public static final int InDungeonOpen = 0x08ab;
	public static final int InDungeonRenewal = 0x08ae;
	public static final int InDungeonSetUp = 0x08a6;
	
	public static final int InDungeoninvite = 0x02ca;
	
  	/** 인던 관련 알람 부분 정리 */
  	public S_EventNotice(int Type) {
  		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeH(Type);
		switch (Type) {
			/** b4 b6 08 08 01 12 05 08 c9 01 10 01 12 05 08 ca 01 10 00  */
			case InDungeon:
		  		/** 타입 인듯 하고 */
		  		writeC(0x08);
		  		writeC(0x01);
		  		
		  		int TypeNumber, Counter;
		  		Map<Integer, DungeonInfo> DungeonInfoList = DungeonSystem.DungeonInfoList(); 
		  		for (int i = 0; i < 2; i++){
		  			TypeNumber = (i == 0 ? 201 : 202); Counter = 0;
		  			Iterator<Integer> keySetIterator = DungeonInfoList.keySet().iterator();
		  			while (keySetIterator.hasNext()) {
		  				DungeonInfo DungeonKey = DungeonInfoList.get(keySetIterator.next());
		  				if(DungeonKey.TypeNumber == TypeNumber) Counter++; 
		  			}
			  		writeC(0x12);
			  		writeC(2 + size7B(TypeNumber) + size7B(Counter));
			  		
			  		writeC(0x08);
			  		writeBit(TypeNumber);
			  		
			  		writeC(0x10);
			  		writeBit(Counter);
		  		}
				break;
		}
  		writeH(0);
  	}
  			
  	/** b4 b2 08 08 01 10 b0 60 03 81 */
  	public S_EventNotice(int Type, int InDungeType, int InDungeNum) {
  		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeH(Type);
		switch (Type) {
			/** b4 a8 08 08 0f 10 00 0e 00 */
			case InDungeonStancet:
			case InDungeonType:
			case InDungeonSlot:
			case InDungeExit:
			case InDungeStart:
		  		/** 패킷 타입 */
		  		writeC(0x08);
		  		writeC(InDungeType);
		  		
		  		/** 체크 필요 */
		  		writeC(0x10);
		  		writeBit(InDungeNum);
				break;
				
			case InDungeonOut:
		  		/** 패킷 타입 */
		  		writeC(0x08);
		  		writeC(0x01);
		  		
		  		/** 패킷 타입 */
		  		writeC(0x10);
		  		writeC(InDungeNum);
		  		
		  		/** 체크 필요 */
		  		writeC(0x18);
		  		writeBit(InDungeType);
				break;
				
			/** b4 a4 08 08 01 10 01 18 01 */
			case InDungeonList:
		  		/** 체크 필요 */
		  		writeC(0x08);
		  		writeC(0x01);
		  		
		  		/** 체크요망 */
		  		writeC(0x10);
		  		writeC(0x01);
		  		
		  		/** 사이즈 값 */
		  		writeC(0x18);
		  		writeC(DungeonSystem.DungeonInfoList().size());
		  		
		  		Map<Integer, DungeonInfo> DungeonInfoList = DungeonSystem.DungeonInfoList(); 
	  			Iterator<Integer> keySetIterator = DungeonInfoList.keySet().iterator();
	  			while (keySetIterator.hasNext()) {
	  				DungeonInfo DungeonInfo = DungeonInfoList.get(keySetIterator.next());
	  				if(InDungeType != 0 && (DungeonInfo.TypeNumber != InDungeType)) continue;
			  		/** 방정보 갱보 */
			  		writeC(0x22);
			  		writeC(17 + DungeonInfo.Title.getBytes().length + size7B(DungeonInfo.RoomNumber) + size7B(DungeonInfo.MaxSize) + size7B(DungeonInfo.MinSize) +
			  				size7B(DungeonInfo.DungeonList.size()) + size7B(DungeonInfo.TypeNumber) + size7B(DungeonInfo.Level) + size7B(DungeonInfo.Adena));
			  		
			  		/** 방 번호 */
			  		writeC(0x08);
			  		writeBit(DungeonInfo.RoomNumber);
			  		
			  		/** 방 제목 */
			  		writeC(0x12);
			  		writeBit(DungeonInfo.Title.getBytes().length);
			  		writeByte(DungeonInfo.Title.getBytes());
			  		
			  		writeC(0x18);
			  		writeBit(DungeonInfo.DungeonList.size());
			  		
			  		/** 개방소켓 정보 */
			  		writeC(0x20);
			  		writeBit(DungeonInfo.MaxSize);
			  		
			  		writeC(0x28);
			  		writeBit(DungeonInfo.MinSize);
			  		
			  		/** 방 넘버 */
			  		writeC(0x30);
			  		writeBit(DungeonInfo.TypeNumber);
			  		
			  		/** 장금 아이콘 */
			  		writeC(0x38);
			  		writeC(DungeonInfo.Open);
			  		
			  		/** 레벨 */
			  		writeC(0x40);
			  		writeBit(DungeonInfo.Level);
			  		
			  		/** 금액 부분 */
			  		writeC(0x48);
			  		writeBit(DungeonInfo.Adena);
			  		
			  		writeC(0x50);
			  		writeC(0x01);
			  		/** 게임시작 인플레이 세팅 */
			  		writeC(0x58);
			  		writeC(DungeonInfo.InPlaygame ? 1 : 0);
			  		/** 옵저버 세팅 */
			  		writeC(0x60);
			  		writeC(0x00);
	  			}
				break;
		}
  		writeH(0);
  	}
  	
  	public S_EventNotice(int Type, DungeonInfo DungeonInfo, L1PcInstance Pc) {
  		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeH(Type);
		switch (Type) {
			/** b4 a8 08 08 0f 10 00 0e 00 */
			case InDungeonOpen:
		  		/** 인던 넘버 타입 */
		  		writeC(0x08);
		  		writeBit(DungeonInfo.RoomNumber);
		  		
		  		/** 옵저브 타입 */
		  		writeC(0x10);
		  		writeBit(0x00);
		  		
		  		/** 초기 정보 세팅  */
		  		for (L1PcInstance PcList : DungeonInfo.isDungeonInfoUset()){
			  		writeC(0x1a);
			  		writeBit(21 + size7B(PcList.getId()) + PcList.getName().getBytes().length);
			  		
			  		writeC(0x08);
			  		writeBit(PcList.getId());
			  		
			  		/** 서버 넘버 고정 100 */
			  		writeC(0x10);
			  		writeBit(100);
			  		
			  		/** 이름 정보  */
			  		writeC(0x1a);
			  		writeBit(PcList.getName().getBytes().length);
					writeByte(PcList.getName().getBytes());
			  		
					/** 클레스 타입 */
			  		writeC(0x20);
			  		writeBit(PcList.getType());
			  		
			  		/** 이게 멀까 과연 */
			  		writeC(0x28);
			  		writeC(0x01);
			  		writeC(0x30);
			  		writeC(0x01);
			  		writeC(0x38);
			  		writeC(0x01);
			  		writeC(0x40);
			  		writeC(DungeonInfo.DungeonLeadt == PcList.getId() ? 1 : 0);
			  		writeC(0x48);
			  		writeC(DungeonInfo.isDungeonInfoCheck(PcList) ? 1 : 0);
			  		writeC(0x50);
			  		writeC(0x01);
			  		writeC(0x58);
			  		writeC(DungeonInfo.DungeonList.indexOf(DungeonInfo.isUser(PcList)));
				}
		  		
		  		/** 방정보 갱보 */
		  		writeC(0x22);
		  		writeC(17 + DungeonInfo.Title.getBytes().length + size7B(DungeonInfo.RoomNumber) + size7B(DungeonInfo.MaxSize) + size7B(DungeonInfo.MinSize) +
		  				size7B(DungeonInfo.DungeonList.size()) + size7B(DungeonInfo.TypeNumber) + size7B(DungeonInfo.Level) + size7B(DungeonInfo.Adena));
		  		
		  		/** 방 번호 */
		  		writeC(0x08);
		  		writeBit(DungeonInfo.RoomNumber);
		  		
		  		/** 방 제목 */
		  		writeC(0x12);
		  		writeBit(DungeonInfo.Title.getBytes().length);
		  		writeByte(DungeonInfo.Title.getBytes());
		  		
		  		writeC(0x18);
		  		writeBit(DungeonInfo.DungeonList.size());
		  		
		  		/** 개방소켓 정보 */
		  		writeC(0x20);
		  		writeBit(DungeonInfo.MaxSize);
		  		
		  		writeC(0x28);
		  		writeBit(DungeonInfo.MinSize);
		  		
		  		/** 방 넘버 */
		  		writeC(0x30);
		  		writeBit(DungeonInfo.TypeNumber);
		  		
		  		/** 장금 아이콘 */
		  		writeC(0x38);
		  		writeC(DungeonInfo.Open);
		  		
		  		/** 레벨 */
		  		writeC(0x40);
		  		writeBit(DungeonInfo.Level);
		  		
		  		/** 금액 부분 */
		  		writeC(0x48);
		  		writeBit(DungeonInfo.Adena);
		  		
		  		writeC(0x50);
		  		writeC(0x01);
		  		/** 게임시작 인플레이 세팅 */
		  		writeC(0x58);
		  		writeC(DungeonInfo.InPlaygame ? 1 : 0);
		  		/** 옵저버 세팅 */
		  		writeC(0x60);
		  		writeC(0x00);
				break;
				
			case InDungeonRenewal:
			case InDungeonOutUse:
		  		/** 인던 넘버 타입 */
		  		writeC(0x08);
		  		writeBit(DungeonInfo.RoomNumber);
		  		
		  		writeC(0x1a);
		  		writeBit(21 + size7B(Pc.getId()) + Pc.getName().getBytes().length);
		  		
		  		writeC(0x08);
		  		writeBit(Pc.getId());
		  		
		  		/** 서버 넘버 고정 100 */
		  		writeC(0x10);
		  		writeBit(100);
		  		
		  		/** 이름 정보  */
		  		writeC(0x1a);
		  		writeBit(Pc.getName().getBytes().length);
				writeByte(Pc.getName().getBytes());
		  		
				/** 클레스 타입 */
		  		writeC(0x20);
		  		writeBit(Pc.getType());
		  		
		  		/** 이게 멀까 과연 */
		  		writeC(0x28);
		  		writeC(Type == InDungeonRenewal ? 0x01 : 0x00);
		  		writeC(0x30);
		  		writeC(Type == InDungeonRenewal ? 0x01 : 0x00);
		  		writeC(0x38);
		  		writeC(0x01);
		  		/** 파티장 인지 체크 */
		  		writeC(0x40);
		  		writeC(DungeonInfo.DungeonLeadt == Pc.getId() ? 1 : 0);
		  		writeC(0x48);
		  		writeC(DungeonInfo.isDungeonInfoCheck(Pc) ? 1 : 0);
		  		writeC(0x50);
		  		writeC(Type == InDungeonRenewal ? 0x01 : 0x02);
		  		/** 자리 위치 */
		  		writeC(0x58);
		  		writeC(DungeonInfo.DungeonList.indexOf(DungeonInfo.isUser(Pc)));
				break;
				
			case InDungeonOutExit:
		  		writeC(0x08);
		  		writeBit(DungeonInfo.RoomNumber);
		  		
		  		writeC(0x10);
		  		writeBit(Pc.getId());
		  		
		  		/** 이름 정보  */
		  		writeC(0x1a);
		  		writeBit(Pc.getName().getBytes().length);
				writeByte(Pc.getName().getBytes());
				break;
				
			/** 08 01 12 12 08 50 10 01 18 06 20 01 2a 05 08 88 27 10 00 30 c9 01 */
			case InDungeonSetUp:
		  		/** 방 번호 */
		  		writeC(0x08);
		  		writeC(0x01);
		  		
		  		/** 사이즈 */
		  		writeC(0x12);
		  		writeC(10 + size7B(DungeonInfo.Level) + size7B(DungeonInfo.Type) + size7B(DungeonInfo.MaxSize) +
		  				size7B(DungeonInfo.Division) + size7B(DungeonInfo.Adena) + size7B(DungeonInfo.TypeNumber));
		  		
		  		/** 레벨 정보 */
		  		writeC(0x08);
		  		writeBit(DungeonInfo.Level);
		  		
		  		/** 타입 1 : 디펜스  2: 던전*/
		  		writeC(0x10);
		  		writeBit(DungeonInfo.Type);
		  		
		  		/** 인원 소켓 정보 */
		  		writeC(0x18);
		  		writeBit(DungeonInfo.MaxSize);
		  		
		  		/** 분배 방식 */
		  		writeC(0x20);
		  		writeBit(DungeonInfo.Division);
		  		
		  		/** 사이즈 계산 */
		  		writeC(0x2a);
		  		writeC(3 + size7B(DungeonInfo.Adena));
		  		/** 필요 아데나 */
		  		writeC(0x08);
		  		writeBit(DungeonInfo.Adena);
		  		
		  		/** 이건 멀까 */
		  		writeC(0x10);
		  		writeBit(0x00);
		  		
		  		/** 방 정보 */
		  		writeC(0x30);
		  		writeBit(DungeonInfo.TypeNumber);
		  		break;
				
		}
  		writeH(0);
  	}
  	
  	public S_EventNotice(int Type, int InDungeNum, String PcName, String UseName) {
  		writeC(Opcodes.S_EXTENDED_PROTOBUF);
  		writeH(Type);
		switch (Type) {
			/**b4 ca 02 08 91 25 12 08 c3 b5 b1 ba ba d2 c6 d0 1a 08 c3 b5 b1 ba ba d2 c6 d0 20 00 38 01 */
			case InDungeoninvite:
		  		/** 패킷 타입 */
		  		writeC(0x08);
		  		writeC(InDungeNum);
		  		
		  		/** 체크 필요 */
		  		writeC(0x12);
		  		writeBit(PcName.getBytes().length);
				writeByte(PcName.getBytes());
				
		  		/** 체크 필요 */
		  		writeC(0x1a);
		  		writeBit(UseName.getBytes().length);
				writeByte(UseName.getBytes());
				
		  		/** 체크 필요 */
		  		writeC(0x20);
		  		writeBit(0x00);
		  		
		  		writeC(0x38);
		  		writeBit(InDungeNum);
				break;
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
		return S_EventNotice;
	}
}
