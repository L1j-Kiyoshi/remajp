package l1j.server.server.serverpackets;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1PcInstance;

public class S_UserStatus extends ServerBasePacket {

	private static final String S_UserCommands3 = "[C] S_UserCommands3";

	private byte[] _byte = null;

	public S_UserStatus(L1PcInstance pc, int number) {
		buildPacket(pc, number);
	}

	@SuppressWarnings("deprecation")
	private void buildPacket(L1PcInstance pc, int number) {

		int calcaden = 0;
		int calclevel = pc.getLevel() - pc._PlayLevel;
		int calcexp = pc.getExp() - pc._PlayEXP;

		Date _lastlogout = new Date();
		String lastLogout = null;

		if (pc.getLogOutTime() != null) {
			StringBuilder sb = null;
			sb = new StringBuilder();
			_lastlogout.setTime(pc.getLogOutTime().getTime());

			Calendar cal = Calendar.getInstance(Locale.KOREA);
			cal.setTime(_lastlogout);

			sb.append(cal.get(Calendar.YEAR) + "-"
					+ (cal.get(Calendar.MONTH) + 1) + "-"
					+ cal.get(Calendar.DATE) + ". "
					+ cal.get(Calendar.HOUR_OF_DAY) + ":"
					+ cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
			lastLogout = sb.toString();
			sb = null;
		} else {
			lastLogout = "최초 접속 케릭 입니다.";
		}

		if (pc.getInventory().countItems(40308) > 0) {
			calcaden = pc.getInventory().countItems(40308) - pc._PlayAden;
		} else {
			calcaden = -pc._PlayAden;
		}

		long nowtime = System.currentTimeMillis();
		Timestamp nowstamp = new Timestamp(nowtime);

		int girantime = 0;
		int girantemp = 60 * 60 * 3;
		int giranh = 0;
		int girans = 0;
		int giranm = 0;

		int 수상time = 0;
		int 수상temp = 60 * 60 * 2;
		int 수상h = 0;
		int 수상s = 0;
		int 수상m = 0;

		int ravatime = 0;
		int ravatemp = 18000;
		int ravah = 0;
		int ravas = 0;
		int ravam = 0;

		int ivorytime = 0;
		int ivorytemp = 3600;
		int ivoryh = 0;
		int ivorys = 0;
		int ivorym = 0;

		if (pc.getgiranday() != null
				&& pc.getgiranday().getDate() == nowstamp.getDate()
				&& pc.getgirantime() != 0) {
			girantime = pc.getgirantime();
			girans = (girantemp - girantime) % 60;
			giranm = (girantemp - girantime) / 60 % 60;
			giranh = (girantemp - girantime) / 60 / 60;// 시간
		} else {
			giranh = 2;
			giranm = 0;
			girans = 0;
		}

		if (pc.get수상한감옥day() != null
				&& pc.get수상한감옥day().getDate() == nowstamp.getDate()
				&& pc.get수상한감옥time() != 0) {
			수상time = pc.get수상한감옥time();
			수상s = (수상temp - 수상time) % 60;
			수상m = (수상temp - 수상time) / 60 % 60;
			수상h = (수상temp - 수상time) / 60 / 60;// 시간
		} else {
			수상h = 2;
			수상m = 0;
			수상s = 0;
		}

		if (pc.getravaday() != null
		// && pc.getravaday().getDate() == nowstamp.getDate()
				&& pc.getravatime() != 0) {
			ravatime = pc.getravatime();
			ravas = (ravatemp - ravatime) % 60;
			ravam = (ravatemp - ravatime) / 60 % 60;
			ravah = (ravatemp - ravatime) / 60 / 60;// 시간
		} else {
			ravah = 2;
			ravam = 0;
			ravas = 0;
		}

		if (pc.getivoryday() != null
				&& pc.getivoryday().getDate() == nowstamp.getDate()
				&& pc.getivorytime() != 0) {
			ivorytime = pc.getivorytime();
			ivorys = (ivorytemp - ivorytime) % 60;
			ivorym = (ivorytemp - ivorytime) / 60 % 60;
			ivoryh = (ivorytemp - ivorytime) / 60 / 60;// 시간
		} else {
			ivoryh = 1;
			ivorym = 0;
			ivorys = 0;
		}

		// String 패턴 = "#,##0.0000";
		// DecimalFormat 소수점 = new DecimalFormat(패턴);

		writeC(Opcodes.S_BOARD_READ);
		writeD(number);// 넘버
		writeS(pc.getName());// 글쓴이?
		writeS(pc.getName() + " 의 정보");// 제목?
		writeS("  ");// 날짜?
		// System.out.println(경험치);

		writeS("Logout : " + lastLogout + "\nLogin : " + pc._PlayTime
				+ "\n=======[획득 경험치]=======\n" + "[레벨업 : " + calclevel
				+ " EXP : " + calcexp + "]\n" + "\n=======[획득 아데나]=======\n"
				+ "[" + calcaden + "원]\n" + "\n======[죽인 몬스터 수]=====\n" + "["
				+ pc._PlayMonKill + "마리]\n" + "\n=======[죽인 유저 수]======\n"
				+ "[" + pc._PlayPcKill + "명]\n" + "\n=======[입장   시간]=======\n"
				+ "[기란감옥/지배자결계  : \n"
				+ giranh + "시간 " + giranm + "분 " + girans
				+ "초]\n" + "[수상한감옥: " + 수상h + "시간 " + 수상m + "분 " + 수상s + "초]\n"
				+ "[라던      : " + ravah + "시간 " + ravam + "분 " + ravas + "초]\n"
				+ "[상아탑    : " + ivoryh + "시간 " + ivorym + "분 " + ivorys
				+ "초]\n" + "=============================");
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_UserCommands3;
	}
}
