package l1j.server.netty.coder.manager;

import java.util.concurrent.LinkedBlockingQueue;

import l1j.server.netty.CircleArray;
import server.LineageClient;

public class LineageDecoderThread implements Runnable {
	

	public LineageDecoderThread() {
		_client = new LinkedBlockingQueue<LineageClient>();
	}
	private boolean _running = true;

	public void setRunning(boolean flag) {
		this._running = flag;
	}
	private LinkedBlockingQueue<LineageClient> _client;
	int count = 0;
	int pa_size;

	public void run() {
		while (_running) {
			try {
				LineageClient client = _client.take();
				if (client != null) {
					if (!client.isConnected() || client.ckclose == true) {
						client.close();
						removeClient(client);
						continue;
					}

					CircleArray Circle = client.getCircleArray();
					while ((pa_size = Circle.isPacketPull2()) > 0) {
						client.encryptD(Circle.Pull(pa_size));
					}

				} else {
					removeClient(client);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void putClient(LineageClient c) {
		try {
			_client.put(c);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public LineageClient getClient(String id) {
		if (id != null) {
			try {
				for (LineageClient c : _client) {
					if (c != null) {
						if (c.getID() != null && c.getID().equalsIgnoreCase(id)) {
							return c;
						}
					} else {
						removeClient(c);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public void removeClient(LineageClient c) {
		while (_client.contains(c))
			_client.remove(c);
	}

	public int ClientCount() {
		return _client.size();
	}

	public boolean ContainsClient(LineageClient c) {
		return _client.contains(c);
	}

	public LinkedBlockingQueue<LineageClient> getAllClient() {
		return this._client;
	}

}
