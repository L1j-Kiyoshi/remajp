package l1j.server.server.command.executor;

import java.util.StringTokenizer;

import l1j.server.server.datatables.AttendanceTable;
import l1j.server.server.model.L1World;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.serverpackets.S_SystemMessage;

public class L1AttendanceModify implements L1CommandExecutor {

	private L1AttendanceModify() {
	}

	public static L1CommandExecutor getInstance() {
		return new L1AttendanceModify();
	}

	@Override
	public void execute(L1PcInstance pc, String cmdName, String arg) {
		try {

			StringTokenizer str = new StringTokenizer(arg);
			String target_name = str.nextToken();
			int modify_order = Integer.parseInt(str.nextToken());
			int modify_number = Integer.parseInt(str.nextToken());
			int modify_result = Integer.parseInt(str.nextToken());

			if (target_name.equalsIgnoreCase("전체")) {
				for(L1PcInstance target : L1World.getInstance().getAllPlayers()) {
					if(target == null || target._robot || target.isPrivateShop() || target.noPlayerCK)
						continue;
					
					if (modify_order == 0) {
						if (modify_number == 0) {
							byte[] reset = new byte[AttendanceTable.getInstance().getHomeSize()];
							target.getAccount().setAttendanceHomeBytes(reset);
						} else {
							target.getAccount().getAttendanceHomeBytes()[modify_number - 1] = (byte) modify_result;
						}
					} else {
						if (modify_number == 0) {
							byte[] reset = new byte[AttendanceTable.getInstance().getPcSize()];
							target.getAccount().setAttendancePcBytes(reset);
						} else {
							target.getAccount().getAttendancePcBytes()[modify_number - 1] = (byte) modify_result;
						}
					}
					target.getAccount().storeAttendBytes();
				}
			} else {
				L1PcInstance target = L1World.getInstance().getPlayer(target_name);
				if (target == null)
					pc.sendPackets(new S_SystemMessage("월드에 존재하지 않는 유저입니다."));

				if (modify_order == 0) {
					if (modify_number == 0) {
						byte[] reset = new byte[AttendanceTable.getInstance().getHomeSize()];
						target.getAccount().setAttendanceHomeBytes(reset);
					} else {
						target.getAccount().getAttendanceHomeBytes()[modify_number - 1] = (byte) modify_result;
					}
				} else {
					if (modify_number == 0) {
						byte[] reset = new byte[AttendanceTable.getInstance().getPcSize()];
						target.getAccount().setAttendancePcBytes(reset);
					} else {
						target.getAccount().getAttendancePcBytes()[modify_number - 1] = (byte) modify_result;
					}
				}
				target.getAccount().storeAttendBytes();
			}
			pc.sendPackets(new S_SystemMessage(target_name + " 의 " + 
					(modify_order == 0 ? "일반출석 " : "피씨방출석 ") + modify_number + "일자의 출석정보가 수정되었습니다."));
		} catch (Exception e) {
			pc.sendPackets(new S_SystemMessage((new StringBuilder())
					.append(".출석수정 [유저이름or전체] [일반(0),피씨(1)] [일자(0-전체)] [0,1,2]로 입력해 주세요. ").toString()));
		}
	}
}
