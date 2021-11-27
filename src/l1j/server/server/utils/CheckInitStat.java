/** 
 * From. LinFreedom
 * 캐릭터 스테이츠  검사 
 * 
 * 
 * 
 * */
package l1j.server.server.utils;

import l1j.server.server.model.Instance.L1PcInstance;

public class CheckInitStat {

	// KKK 케릭터의 최소스탯을 검사
	private CheckInitStat() {
	}

	/**
	 * 캐릭터의 최소스탯을 검사
	 * 
	 * @param pc
	 * @return true : 정상 or 운영자, false : 비정상
	 */
	public static boolean CheckPcStat(L1PcInstance pc) {
		if (pc == null) { // 만약 pc가 없다면
			return false;
		}
		if (pc.isGm()) { // pc가 운영자라면
			return true;
		}

		int str = pc.getAbility().getBaseStr();
		int dex = pc.getAbility().getBaseDex();
		int cha = pc.getAbility().getBaseCha();
		int con = pc.getAbility().getBaseCon();
		int intel = pc.getAbility().getBaseInt();
		int wis = pc.getAbility().getBaseWis();
		int basestr = 0;
		int basedex = 0;
		int basecon = 0;
		int baseint = 0;
		int basewis = 0;
		int basecha = 0;
		switch (pc.getType()) {
		case 0: // 군주
			basestr = 13;
			basedex = 10;
			basecon = 10;
			basewis = 11;
			basecha = 13;
			baseint = 10;
			break;
		case 1: // 기사
			basestr = 16;
			basedex = 12;
			basecon = 14;
			basewis = 9;
			basecha = 12;
			baseint = 8;
			break;
		case 2: // 요정
			basestr = 11;
			basedex = 12;
			basecon = 12;
			basewis = 12;
			basecha = 9;
			baseint = 12;
			break;
		case 3: // 법사
			basestr = 8;
			basedex = 7;
			basecon = 12;
			basewis = 12;
			basecha = 8;
			baseint = 12;
			break;
		case 4: // 다크엘프
			basestr = 12;
			basedex = 15;
			basecon = 8;
			basewis = 10;
			basecha = 9;
			baseint = 11;
			break;
		case 5: // 용기사
			basestr = 13;
			basedex = 11;
			basecon = 14;
			basewis = 12;
			basecha = 8;
			baseint = 11;
			break;
		case 6: // 환술사
			basestr = 11;
			basedex = 10;
			basecon = 12;
			basewis = 12;
			basecha = 8;
			baseint = 12;
			break;
		}

		if (str < basestr || dex < basedex || con < basecon || cha < basecha
				|| intel < baseint || wis < basewis) { // 초기스탯보다 작다면
			return false;
		}
		return true;
	}

}
