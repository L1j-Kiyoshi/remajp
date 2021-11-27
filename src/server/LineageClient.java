package server;

import java.sql.Timestamp;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.LittleEndianHeapChannelBuffer;
import org.jboss.netty.channel.Channel;

import l1j.server.Config;
import l1j.server.netty.CircleArray;
import l1j.server.netty.coder.LineageEncryption;
import l1j.server.netty.coder.manager.DecoderManager;
import l1j.server.server.Account;
import l1j.server.server.GeneralThreadPool;
import l1j.server.server.Opcodes;
import l1j.server.server.PacketHandler;
import l1j.server.server.PacketOutput;
import l1j.server.server.model.L1Party;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;
import l1j.server.server.serverpackets.S_Disconnect;
import l1j.server.server.serverpackets.ServerBasePacket;
import l1j.server.server.templates.L1LoginInfo;

public class LineageClient implements PacketOutput {
	private static Logger _log = Logger.getLogger(LineageClient.class.getName());
	private GeneralThreadPool _threadPool = GeneralThreadPool.getInstance();
	public static final String CLIENT_KEY = "CLIENT";
	private LineageEncryption le;
	private String ID;
	private L1PcInstance activeCharInstance;
	public byte[] PacketD;
	public int PacketIdx;
	private boolean close = false;
	public boolean ckclose = false;
	public boolean notic = false;
	public boolean lintool = false;
	public boolean leaftool = false;
	public int noticcount = 0;
	public int noticcount2 = 0;
	public boolean firstpacket = true;
	public int packetStep = 0;
	public byte[] firstByte = null;
	public int bufsize = 0;
	public int maxsize = 0;
	public boolean bufchek = false;
	public boolean packet = false;
	public boolean synchron = false;
	private String authCode;

	private long _lastPckTime;

	public long getLastPckTime() {
		return _lastPckTime;
	}

	public void setLastPckTime(long now) {
		_lastPckTime = now;
	}

	public void calcPckTime(long i) {
		long time = _lastPckTime + i;
		if (time <= 0) {
			time = 0;
		}
		_lastPckTime = time;
	}

	private CircleArray Circle = new CircleArray(1024 * 5);

	public CircleArray getCircleArray() {
		return Circle;
	}

	public String getAuthCode() {
		return authCode;
	}

	public void setAuthCode(String a) {
		authCode = a;
	}

	private Timestamp connectTimestamp;

	public Timestamp getConnectTimestamp() {
		return connectTimestamp;
	}

	public void setConnectTimestamp(Timestamp connectTime) {
		connectTimestamp = connectTime;
	}

	private boolean authCheck = false;

	public boolean isAuthCheck() {
		return authCheck;
	}

	public void setAuthCheck(boolean auth) {
		authCheck = auth;
	}

	public PacketHandler packetHandler;
	private static Timer observerTimer = new Timer();
	private boolean charRestart = true;
	private int _loginfaieldcount = 0;
	private Account _account;
	private int threadIndex = 0;
	public HcPacket hcPacket = new HcPacket();
	public ServerPavketThread ServerPacket = null;

	public boolean DecodingCK = false;
	ClientThreadObserver observer = new ClientThreadObserver(Config.AUTOMATIC_KICK * 60 * 1000);

	private Channel chnnel = null;

	public LineageClient(Channel _chnnel, long key) {
		chnnel = _chnnel;
		le = new l1j.server.netty.coder.LineageEncryption();
		le.initKeys(key);
		PacketD = new byte[1024 * 4];
		PacketIdx = 0;

		if (Config.AUTOMATIC_KICK > 0) {
			observer.start();
		}
		packetHandler = new PacketHandler(this);

		GeneralThreadPool.getInstance().execute(hcPacket);
	}

	public void setthreadIndex(int ix) {
		threadIndex = ix;
	}

	public int getthreadIndex() {
		return threadIndex;
	}

	public void kick() {
		sendPacket(new S_Disconnect());
		if (chnnel == null)
			return;

		chnnel.close();
		close();
	}

	public void CharReStart(boolean flag) {
		charRestart = flag;
	}

	public boolean CharReStart() {
		return charRestart;
	}

	public void sendPacket(ServerBasePacket bp, boolean flag) {
		sendPacket(bp);
	}

	@Override
	public void sendPacket(ServerBasePacket bp) {
		try {
			if (packetvirsion == true) {
				byte[] content = bp.getContent();

				if (bp == null || bp.getBytes().length <= 0) {
					//System.out.println("서버패킷 리턴 : " + bp.getType());
					return;
				}

				if (ServerPacket == null) {
					ServerPacket = new ServerPavketThread(350);
					_threadPool.execute(ServerPacket);
				}

				if (Config.서버패킷출력) {
					System.out.printf("[Server] opcode:%d, type:%d, size:%d\r\n%s\r\n", content[0] & 0xff, content[1] & 0xff, content.length,
							DataToPacket(content, content.length));
				}
				
				ServerPacket.requestWork(bp.getBytes());
				return;
			}
			ChannelBuffer buffer = Nettybuffer(encryptE(bp.getBytes()), bp.getLength());
			bp.clear();
			chnnel.write(buffer);
		} catch (Exception localException) {
		}
	}

	private ChannelBuffer Nettybuffer(byte[] data, int length) {
		byte[] size = new byte[2];
		size[0] |= length & 0xff;
		size[1] |= length >> 8 & 0xff;
		ChannelBuffer _buffer = new LittleEndianHeapChannelBuffer(length);
		_buffer.writeBytes(size);
		_buffer.writeBytes(data);
		return _buffer;
	}

	public void close() {
		if (!close) {
			close = true;
			try {
				if (activeCharInstance != null) {
					if (getLastPckTime() > 0) {
						GeneralThreadPool.getInstance().schedule(new PlayerTimerKick(activeCharInstance), getLastPckTime());
					} else if (activeCharInstance.isPinkName() || activeCharInstance.isParalyzed()) {
						GeneralThreadPool.getInstance().schedule(new PlayerTimerKick(activeCharInstance), 1000);
					} else {
						activeCharInstance.logout();
					}
					setActiveChar(null);
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				LoginController.getInstance().logout(this);
				stopObsever();
				DecoderManager.getInstance().removeClient(this, threadIndex);

				if (chnnel != null) {
					chnnel.close(); //
				}
			}
		}
	}

	class PlayerTimerKick implements Runnable {
		L1PcInstance _pc;

		PlayerTimerKick(L1PcInstance pc) {
			_pc = pc;
		}

		@Override
		public void run() {
			_pc.logout();
		}
	}

	public void setActiveChar(L1PcInstance pc) {
		activeCharInstance = pc;
	}

	public L1PcInstance getActiveChar() {
		return activeCharInstance;
	}

	public void setAccount(Account account) {
		_account = account;
	}

	public Account getAccount() {
		return _account;
	}

	public String getAccountName() {
		if (_account == null) {
			return null;
		}
		String name = _account.getName();
		return name;
	}

	public String getHostname() {
		String HostName = null;
		if (chnnel == null)
			return null;
		if (chnnel.getRemoteAddress() == null)
			return null;
		StringTokenizer st = new StringTokenizer(chnnel.getRemoteAddress().toString().substring(1), ":");
		HostName = st.nextToken();
		st = null;
		return HostName;
	}

	public int getLoginFailedCount() {
		return _loginfaieldcount;
	}

	public void setLoginFailedCount(int i) {
		_loginfaieldcount = i;
	}

	public byte[] encryptD(byte[] data) {
		try {

			int length = PacketSize(data) - 2;
			byte[] temp = new byte[length];
			char[] incoming = new char[length];
			System.arraycopy(data, 2, temp, 0, length);
			incoming = le.getUChar8().fromArray(temp, incoming, length);
			incoming = le.decrypt(incoming, length);
			data = le.getUByte8().fromArray(incoming, temp);

			PacketHandler(data);
		} catch (Exception e) {
			_log.log(Level.WARNING, "LineageClient.encryptD 예외 발생.", e);
		}
		return null;
	}

	public byte[] encryptE(byte[] data) {
		try {
			char[] data1 = le.getUChar8().fromArray(data);
			data1 = le.encrypt_S(data1);
			return le.getUByte8().fromArray(data1);
		} catch (Exception e) {
			_log.log(Level.WARNING, "LineageClient.encryptE 예외 발생.", e);
		}
		return null;
	}

	private int PacketSize(byte[] data) {
		int length = data[0] & 0xff;
		length |= data[1] << 8 & 0xff00;
		return length;
	}

	public String getID() {
		return ID;
	}

	public void setID(String id) {
		ID = id;
	}

	public boolean isConnected() {
		return chnnel.isConnected();
	}

	@SuppressWarnings("unused")
	public String getIp() {
		String _Ip = null;
		if (chnnel == null)
			return null;
		if (chnnel.getRemoteAddress() == null)
			return null;
		StringTokenizer st = new StringTokenizer(chnnel.getRemoteAddress().toString().substring(1), ":");
		if (st == null)
			return null;
		_Ip = st.nextToken();
		st = null;
		return _Ip;
	}

	public void stopObsever() {
		observer.cancel();
	}

	public boolean isClosed() {
		if (!chnnel.isConnected())
			return true;
		else {
			return false;
		}
	}

	public boolean chconnet(byte[] data) {
		if (data[0] == 1 && data[1] == 3 && data[2] == 6 && data[3] == 7 && data[4] == 9) {
			return true;
		}
		return false;
	}

	public boolean packetvirsion = false;
	public boolean web_login_ok = false;
	public int packetcount = 0;
	private PacketHandler _handler = new PacketHandler(LineageClient.this);

	public void PacketHandler(byte[] data) throws Exception {
		int opcode = data[0] & 0xFF;

		if (opcode != Opcodes.C_ALIVE) {
			observer.packetReceived();
		}

		if (CharReStart()) {
			if (!(opcode == Opcodes.C_ENTER_WORLD || opcode == Opcodes.S_KICK || opcode == Opcodes.C_LOGOUT || opcode == Opcodes.C_VOICE_CHAT
					|| opcode == Opcodes.C_EXTENDED_PROTOBUF // 157
					|| opcode == Opcodes.S_CREATE_CHARACTER_CHECK// S_CREATE_CHARACTER_CHECK
					|| opcode == Opcodes.S_EXTENDED_PROTOBUF || opcode == Opcodes.C_CREATE_CUSTOM_CHARACTER || opcode == Opcodes.C_QUIT
					|| opcode == Opcodes.C_SHIFT_SERVER || opcode == Opcodes.C_CHANNEL // 55
					|| opcode == Opcodes.S_NUM_CHARACTER || opcode == Opcodes.C_LOGIN || opcode == Opcodes.C_READ_NEWS || opcode == Opcodes.C_VERSION
					|| opcode == Opcodes.C_ONOFF || opcode == Opcodes.S_CHARACTER_INFO || opcode == Opcodes.C_DELETE_CHARACTER)) {
				return;
			}
		}
		if (opcode == Opcodes.C_EXTENDED_PROTOBUF) {
			int sub_code = data[1] & 0xFF;
			int sub_code_seed = data[2] & 0xFF;
			if (sub_code == 52 && sub_code_seed == 3) {
				packetvirsion = true;
				web_login_ok = true;
			}
		}

		if (activeCharInstance == null) {
			packetHandler.handlePacket(data, activeCharInstance);
			return;
		}
		if (activeCharInstance.getReturnStatus() != 0) {
			if (opcode == Opcodes.C_ATTACK || opcode == Opcodes.C_ASK_XCHG || opcode == Opcodes.C_ACCEPT_XCHG || opcode == Opcodes.C_USE_ITEM
					|| opcode == Opcodes.C_DROP || opcode == Opcodes.C_GET || opcode == Opcodes.C_FAR_ATTACK || opcode == Opcodes.C_GIVE
					|| opcode == Opcodes.C_USE_SPELL || opcode == Opcodes.C_ADD_XCHG || opcode == Opcodes.C_BUY_SELL || opcode == Opcodes.C_DESTROY_ITEM
					|| opcode == Opcodes.C_MOVE || opcode == Opcodes.C_PERSONAL_SHOP) {
				return;
			}
		} else if (activeCharInstance.isParalyzed() || activeCharInstance.isSleeped()) {
			if (activeCharInstance.hasSkillEffect(L1SkillId.THUNDER_GRAB) || activeCharInstance.hasSkillEffect(L1SkillId.STATUS_FREEZE)) {
				if (opcode == Opcodes.C_DROP || opcode == Opcodes.C_GET || opcode == Opcodes.C_GIVE || opcode == Opcodes.C_RESTART) {
					return;
				}
			} else {
				if (opcode == Opcodes.C_ATTACK || opcode == Opcodes.C_ASK_XCHG || opcode == Opcodes.C_ACCEPT_XCHG || opcode == Opcodes.C_DROP
						|| opcode == Opcodes.C_GET || opcode == Opcodes.C_FAR_ATTACK || opcode == Opcodes.C_GIVE || opcode == Opcodes.C_USE_SPELL
						|| opcode == Opcodes.C_RESTART || opcode == Opcodes.C_ADD_XCHG || opcode == Opcodes.C_LOGOUT || opcode == Opcodes.C_MOVE) {
					return;
				}
			}
		}

		if (opcode == Opcodes.C_ATTACK || opcode == Opcodes.C_FAR_ATTACK || opcode == Opcodes.C_MOVE || opcode == Opcodes.C_USE_SPELL) {
			hcPacket.requestWork(data);
		} else {
			_handler.handlePacket(data, activeCharInstance);
		}
	}

	public String printData(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(fillHex(i, 4) + ": ");
			}
			result.append(fillHex(data[i] & 0xff, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[charpoint++];
					if (t1 > 0x1f && t1 < 0x80) {
						result.append((char) t1);
					} else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}

		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}

			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[charpoint++];
				if (t1 > 0x1f && t1 < 0x80) {
					result.append((char) t1);
				} else {
					result.append('.');
				}
			}

			result.append("\n");
		}
		return result.toString();
	}

	private String fillHex(int data, int digits) {
		String number = Integer.toHexString(data);

		for (int i = number.length(); i < digits; i++) {
			number = "0" + number;
		}
		return number;
	}

	public boolean obcheck = false;

	class ClientThreadObserver extends TimerTask {
		private int _checkct = 1;

		private final int _disconnectTimeMillis;

		public ClientThreadObserver(int disconnectTimeMillis) {
			_disconnectTimeMillis = disconnectTimeMillis;
		}

		public void start() {
			observerTimer.scheduleAtFixedRate(ClientThreadObserver.this, 1000 * 60, _disconnectTimeMillis);
		}

		@Override
		public void run() {
			try {
				if (!chnnel.isConnected()) {
					cancel();
					return;
				}
				if (_checkct > 0) {
					_checkct = 0;
					return;
				}
				if (activeCharInstance == null) {
					kick();

					cancel();
					return;
				}
			} catch (Exception e) {
				_log.log(Level.SEVERE, e.getLocalizedMessage(), e);
				cancel();
			}
		}

		public void packetReceived() {
			_checkct++;
		}
	}

	public class ServerPavketThread implements Runnable {
		private final BlockingQueue<byte[]> _queue;

		private byte[] c = { 1, 2, 3, 4 };

		public ServerPavketThread() {
			_queue = new LinkedBlockingQueue<byte[]>();

		}

		public ServerPavketThread(int capacity) {
			_queue = new LinkedBlockingQueue<byte[]>(capacity);
			charStat = new int[6];
		}

		public void requestclose() {
			requestWork(c);
		}

		public void requestWork(byte data[]) {
			_queue.offer(data);
		}

		public void requestclear() {
			_queue.clear();
		}

		public void run() {
			byte[] data;
			while (chnnel.isConnected()) {
				try {
					data = _queue.poll(3000, TimeUnit.MILLISECONDS);
					if (data != null && chnnel.isConnected()) {
						try {
							ChannelBuffer buffer = Nettybuffer(encryptE(data), data.length + 2);
							chnnel.write(buffer);
						} catch (Exception e) {
						}
					}
				} catch (InterruptedException e1) {
				}
			}
			_queue.clear();
			return;
		}
	}

	public boolean packetch = false;
	public int[] charStat;
	private int chatCount;

	public int getChatCount() {
		return chatCount;
	}

	public void setChatCount(int i) {
		chatCount = i;
	}

	public class HcPacket implements Runnable {
		private final BlockingQueue<byte[]> _queue;
		private byte[] c = { 1, 2, 3, 4 };

		public HcPacket() {
			_queue = new LinkedBlockingQueue<byte[]>();
			_handler = new PacketHandler(LineageClient.this);
			charStat = new int[6];
		}

		public HcPacket(int capacity) {
			_queue = new LinkedBlockingQueue<byte[]>(capacity);
			_handler = new PacketHandler(LineageClient.this);
		}

		public void requestclose() {
			requestWork(c);
		}

		public void requestWork(byte data[]) {
			_queue.offer(data);
		}

		public void requestclear() {
			_queue.clear();
		}

		public void run() {
			byte[] data;
			while (chnnel.isConnected()) {
				try {
					data = _queue.poll(3000, TimeUnit.MILLISECONDS);
					if (data != null && chnnel.isConnected()) {
						try {
							_handler.handlePacket(data, activeCharInstance);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
			_queue.clear();
			return;
		}
	}

	private String HexToDex(int data, int digits) {
		String number = Integer.toHexString(data);
		for (int i = number.length(); i < digits; i++)
			number = "0" + number;
		return number;
	}

	public String DataToPacket(byte[] data, int len) {
		StringBuffer result = new StringBuffer();
		int counter = 0;
		for (int i = 0; i < len; i++) {
			if (counter % 16 == 0) {
				result.append(HexToDex(i, 4) + ": ");
			}
			result.append(HexToDex(data[i] & 0xFF, 2) + " ");
			counter++;
			if (counter == 16) {
				result.append("   ");
				int charpoint = i - 15;
				for (int a = 0; a < 16; a++) {
					int t1 = data[(charpoint++)];
					if ((t1 > 31) && (t1 < 128))
						result.append((char) t1);
					else {
						result.append('.');
					}
				}
				result.append("\n");
				counter = 0;
			}
		}
		int rest = data.length % 16;
		if (rest > 0) {
			for (int i = 0; i < 17 - rest; i++) {
				result.append("   ");
			}
			int charpoint = data.length - rest;
			for (int a = 0; a < rest; a++) {
				int t1 = data[(charpoint++)];
				if ((t1 > 31) && (t1 < 128))
					result.append((char) t1);
				else {
					result.append('.');
				}
			}
			result.append("\n");
		}
		return result.toString();
	}

	private boolean _charPasswordOk;

	public boolean isCharPassOk() {
		return _charPasswordOk;
	}

	public void setCharPassOk(boolean flag) {
		_charPasswordOk = flag;
	}

	private boolean _charNameChange;

	public boolean isCharNameChange() {
		return _charNameChange;
	}

	public void setCharNameChange(boolean flag) {
		_charNameChange = flag;
	}

	private int _noticeReadCount;

	public int getNoticeReadCount() {
		return _noticeReadCount;
	}

	public void setNoticeReadCount(int i) {
		_noticeReadCount = i;
	}

	public void addNoticeReadCount(int i) {
		_noticeReadCount += i;
	}

	private L1LoginInfo loginInfo;

	public L1LoginInfo getLoginInfo() {
		return loginInfo;
	}

	public void setLoginInfo(L1LoginInfo loginInfo) {
		this.loginInfo = loginInfo;
	}

	public void quitGame(L1PcInstance pc) {
		Config._quit_Q.requestWork(pc);
	}

	/** 인터 서버 체크 용 */
	private String InterServerName;

	public String getInterServerName() {
		return InterServerName;
	}

	public void setInterServerName(String InterName) {
		InterServerName = InterName;
	}

	private boolean InterServer = false;

	/** 인터 서버 체크 용 */
	public boolean getInterServer() {
		return InterServer;
	}

	public void setInterServer(boolean Inter) {
		InterServer = Inter;
	}

	private int InterServerType;

	/** 인터 서버 체크 용 */
	public int getInterServerType() {
		return InterServerType;
	}

	public void setInterServerType(int Inter) {
		InterServerType = Inter;
	}

	private L1Party InterServerParty;

	/** 인터 서버 체크 용 */
	public L1Party getInterServerParty() {
		return InterServerParty;
	}

	public void setInterServerParty(L1Party Inter) {
		InterServerParty = Inter;
	}

	private int InterServerNotice;

	/** 인터 서버 체크 용 */
	public int getInterServerNotice() {
		return InterServerNotice;
	}

	public void setInterServerNotice(int Notice) {
		InterServerNotice = Notice;
	}

	/** 인터 서버 체크 용 */
	public boolean getletteron() {
		return _letteron;
	}

	public void setletteron(boolean letteron) {
		_letteron = letteron;
	}

	private boolean _letteron = false;

	/** 인터 서버에 대한 처리 부분 */
	public void ServerInterKick() {
		try {
			if (activeCharInstance != null)
				quitGame(activeCharInstance);
			synchronized (activeCharInstance) {
				setActiveChar(null);
			}
			if (Config.새로운패킷구조) {
				this.chnnel.close();
			} else {
				this.chnnel.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void packetwaitgo(byte[] bb) {
		if (bb == null) {
			return;
		}
		try {
			if (Config.새로운패킷구조) {
				packetHandler.handlePacket(bb, activeCharInstance);
			} else {
				packetHandler.handlePacket(bb, activeCharInstance);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}