package cc.antho.clonecraft.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.clonecraft.core.net.ClassRegister;
import lombok.Getter;

public final class NetworkServer {

	public static Thread thread;
	public static NetworkServer instance;

	@Getter private Server server;

	private final Object lock = new Object();

	private NetworkServer() {

	}

	private void start() {

		server = new Server(16384, 2048);
		ClassRegister.register(server);
		server.addListener(new ServerListener(server, Config.PPF));
		server.start();

		try {

			server.bind(Config.TCP_PORT, Config.UDP_PORT);

		} catch (final IOException e) {

			e.printStackTrace();

		}

		try {

			synchronized (lock) {

				lock.wait();

			}

		} catch (final InterruptedException e) {

			// We expect this exception to be thrown

		}

		server.stop();

	}

	public static final void startThread() {

		thread = new Thread(() -> {

			Logger.info("Server thread started");

			instance = new NetworkServer();
			instance.start();
			// This only occurs after the server has stopped
			instance = null;

		}, "CloneCraftServer");

		Logger.info("Starting server thread");
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
