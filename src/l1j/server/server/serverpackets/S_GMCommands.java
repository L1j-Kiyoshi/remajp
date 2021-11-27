package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;

public class S_GMCommands extends ServerBasePacket {

	private static final String S_GMCommands = "[S] S_GMCommands";

	// private static Logger _log =
	// Logger.getLogger(S_GMCommands.class.getName());

	private byte[] _byte = null;

	public S_GMCommands() {
		buildPacket();
	}

	private void buildPacket() {
		writeC(Opcodes.S_NEWS);
		writeS(".셋팅 .서먼 .청소 .속도 .레벨\n" + ".소환 .출두 .이동 .위치 .부활\n"
				+ ".날씨 .귀환 .버프 .추방 .킬\n" + ".강종 .변신 .채금 .누구 .선물\n"
				+ ".종료 .검색 .검사 .감시 .피바\n" + ".설문 .액션 .몬스터.배치.정보\n"
				+ ".아데나.부활 .올버프.전체버프\n" + ".정리 .채팅 .서버저장 .리로드\n"
				+ ".가라 .감옥 .전체선물 .이미지\n" + ".홈타운 .채금풀기 .투명 .스킬\n"
				+ ".불투명 .뻥 .재실행 .파티소환\n\n" + ".스킬마스터.영구추방 .코드 \n"
				+ ".밴아이피 .감시.비번변경 \n" + ".스킬마스터.고스폰 .리셋트랩\n"
				+ ".무인상점.계정압류.인벤이미지\n" + ".무인추방.영자상점\n" + ".리로드트랩.공성시작.혈전시작\n"
				+ ".잊섬오픈.자동생성.코마버프");
	}

	@Override
	public byte[] getContent() {
		if (_byte == null) {
			_byte = getBytes();
		}
		return _byte;
	}

	public String getType() {
		return S_GMCommands;
	}
}
