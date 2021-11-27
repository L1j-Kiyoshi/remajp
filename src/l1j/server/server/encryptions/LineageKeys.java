package l1j.server.server.encryptions;

public class LineageKeys {
	public long[] encodeKey = { 0, 0 };

	public long[] decodeKey = { 0, 0 };

	public byte[] HashKey = new byte[256]; // 서버 암호화에 이용
}
