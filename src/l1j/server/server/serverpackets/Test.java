package l1j.server.server.serverpackets;

import java.util.StringTokenizer;

public class Test extends ServerBasePacket {
	public static final String S_문장주시 = "[S] S_test";
	private byte[] _byte = null;

	public Test(String s) {
		buildPacket(s);
	}

	private void buildPacket(String s) {
		StringTokenizer st = new StringTokenizer(s);
		while (st.hasMoreTokens()) {
			writeC(Integer.parseInt(st.nextToken(), 16));
		}
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
		return S_문장주시;
	}
}
