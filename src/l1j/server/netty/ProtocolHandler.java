package l1j.server.netty;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.LittleEndianHeapChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

import l1j.server.netty.coder.manager.DecoderManager;
import l1j.server.server.Opcodes;
import l1j.server.server.datatables.IpTable;
import l1j.server.server.utils.SystemUtil;
import server.LineageClient;

public final class ProtocolHandler extends SimpleChannelUpstreamHandler {

	private static final TimerPool _timerPool = new TimerPool(300);

	private static Map<String, IpInfo> list_dos = new HashMap<String, IpInfo>();

	private final CopyOnWriteArrayList<LineageClient> _Clientlist = new CopyOnWriteArrayList<LineageClient>();

	public class sessionCheck extends TimerTask {
		Channel session;

		public sessionCheck(Channel _session) {
			session = _session;
		}

		@Override
		public void run() {
			LineageClient lc = (LineageClient) session.getAttachment();
			if (lc != null) {
				if (lc.packetvirsion == false && lc.web_login_ok == true) {
					lc.close();
					lc = null;
				}
			} else {
				session.close();
			}
		}
	}

	@Override
	public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent event) throws Exception {
		try {
			String[] array = event.getChannel().getRemoteAddress().toString().substring(1).split(":");
			int i = Integer.valueOf(array[1]).intValue();
			if (i <= 0) {
				event.getChannel().close();
				System.out.println("close 901");
				return;
			}

			if (IpTable.getInstance().isBannedIp(array[0])) {
				System.out.println("[차단된 IP 접속시도 절단] IP:" + array[0]);
				event.getChannel().close();
				return;
			}

			if (isConnectBan(array[0], System.currentTimeMillis())) {
				System.out.println("[지속적인 접속시도로 인하여 접속을 차단합니다.] IP:" + array[0]);
				event.getChannel().close();
				return;
			}

			System.out.println("[접속 시도 중] IP:" + array[0] + "  Thread:" + Thread.activeCount() + "  Memory:"
					+ SystemUtil.getUsedMemoryMB() + " MB");

			_timerPool.getTimer().schedule(new sessionCheck(event.getChannel()), 3000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private boolean isConnectBan(String ip, long time) {
		IpInfo IP = null;
		synchronized (list_dos) {
			IP = list_dos.get(ip);
			if (IP == null) {
				IP = new IpInfo();
				IP.setCount(0);
				IP.setBlock(false);
				IP.setIp(ip);
				IP.setTime(time);
				list_dos.put(IP.getIp(), IP);
				return false;
			}
		}
		// 블럭된 놈은 무시.
		if (IP.isBlock())
			return true;
		// 3초내에 3번이상 접속하는 놈들 무시.
		if (time < IP.getTime() + 3000) {
			if (IP.getCount() > 3) {
				IP.setBlock(true);
				if (!IpTable.getInstance().isBannedIp(IP.getIp())) {
					IpTable.getInstance().banIp(IP.getIp());
				}
				return true;
			} else {
				IP.setCount(IP.getCount() + 1);
			}
		} else {
			IP.setCount(0);
			IP.setTime(time);
		}

		return false;
	}

	public int getRowIndex() {
		int size = DecoderManager.getInstance().getindex_size();
		int[] index = new int[size];
		for (LineageClient Client : _Clientlist) {
			index[Client.getthreadIndex()]++;
		}
		int temp = 1000;
		int o = 0;
		for (int i = 0; i < index.length; i++) {
			int temp1 = index[i];
			if (temp1 < temp) {
				temp = temp1;
				o = i;
			}
		}
		return o;
	}

	/**
	 * 
	 */
	@Override
	public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		try {
			if (e.getChannel().isConnected()) {
				long seed = Opcodes.getSeed();
				KeyPacket key = new KeyPacket();
				LineageClient lc = new LineageClient(e.getChannel(), seed);

				DecoderManager.getInstance();
				lc.setthreadIndex(getRowIndex());

				_Clientlist.add(lc);

				ChannelBuffer buffer = buffer(key.getBytes(), key.getLength());
				
				e.getChannel().write(buffer);
				e.getChannel().setAttachment(lc);
			} else {
				e.getChannel().close();
			}
		} catch (Exception E) {
			E.printStackTrace();
		}
	}

	private ChannelBuffer buffer(byte[] data, int length) {
		byte[] size = new byte[2];
		size[0] |= length & 0xff;
		size[1] |= length >> 8 & 0xff;
		ChannelBuffer _buffer = new LittleEndianHeapChannelBuffer(length);// +4
		/*
		 * _buffer.writeByte((byte) (0x01)); _buffer.writeByte((byte) (0x00));
		 * _buffer.writeByte((byte) (0x00)); _buffer.writeByte((byte) (0x00));
		 */
		_buffer.writeBytes(size);
		_buffer.writeBytes(data);

		return _buffer;
	}

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event) throws Exception {
		try {
			LineageClient client = (LineageClient) event.getChannel().getAttachment();

			if (client == null) {
				ctx.getChannel().close();
				return;
			}

			ChannelBuffer buffer = (ChannelBuffer) event.getMessage();
			if (client != null) {
				int size = buffer.readableBytes();
				if (size > 0 && size < 4096) {
					byte[] data = buffer.array();
					client.getCircleArray().insert(data, size);
					if (client.getCircleArray().isPacketPull() > 0)
						DecoderManager.getInstance().putClient(client);
				} else {
					System.out.println("[ProtocolHandler] Size Over : " + size);
					if (client.getAccount() == null) {
						client.kick();
					}
				}
				client = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 */
	@Override
	public void channelClosed(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
		LineageClient lc = (LineageClient) e.getChannel().getAttachment();
		if (lc != null) {
			_Clientlist.remove(lc);
			lc.close();
			lc = null;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		// TODO Auto-generated method stub
		LineageClient lc = (LineageClient) e.getChannel().getAttachment();
		if (lc != null) {
			_Clientlist.remove(lc);
			lc.close();
			lc = null;
		}
	}
}
