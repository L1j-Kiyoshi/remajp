package l1j.server.server.Controller;

import java.util.Random;

import l1j.server.server.GeneralThreadPool;
import l1j.server.server.model.L1Teleport;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.CommonUtil;

public class DungeonTimeController implements Runnable {

	private static DungeonTimeController _instance;

	public static DungeonTimeController getInstance() {
		if (_instance == null)
			_instance = new DungeonTimeController();
		return _instance;
	}

	public DungeonTimeController() {
		GeneralThreadPool.getInstance().execute(this);
	}
	
	public boolean isGiranOpen = false;
	
	@Override
	public void run() {
		while (true) {
			try {
				Thread.sleep(60000);
				isOpen();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void isOpen() {
		int hour = Integer.valueOf(CommonUtil.dateFormat("HH"));
		if (!isGiranOpen) {
			if (hour == 6 ) {
				isGiranOpen = true;
				// 멘트
			//	L1World.getInstance()
				//		.broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[공지] 글루디오 던전이 오픈 되었습니다."));
			//	L1World.getInstance().broadcastServerMessage("\\aH");
			} else {
				HomeTel();
			}
		} else {
			if(hour == 7){ //클로즈
				isGiranOpen = false;
				//멘트
			//	L1World.getInstance().broadcastPacketToAll(new S_PacketBox(S_PacketBox.GREEN_MESSAGE, "[공지] 글루디오 던전이 종료 되었습니다."));
			//	L1World.getInstance().broadcastServerMessage("\\aH글루디오 던전이 종료 되었습니다.");
				HomeTel();
			}
		}
	}
	
	private void HomeTel(){
		for (L1PcInstance pc : L1World.getInstance().getAllPlayers()) {
			if(pc == null){
				continue;
			}
			Random random = new Random();
			int i13 = 33444 + random.nextInt(3);
			int k19 = 32800 + random.nextInt(3);
			switch (pc.getMapId()) {
			case 8071: //맵번호 맞는지 확인
				if(!pc.isDead()){
					L1Teleport.teleport(pc, i13, k19, (short) 4, 4, true);
				//}else{
				//	restart(pc, i13, k19, 4);
				}
				break;
			default:
				break;
			}
		}
	}
}