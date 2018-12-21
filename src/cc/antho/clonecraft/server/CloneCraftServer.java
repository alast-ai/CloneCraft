package cc.antho.clonecraft.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.core.ClassRegister;
import cc.antho.clonecraft.core.log.Logger;
import lombok.Getter;

public final class CloneCraftServer {

	public static Thread thread;
	public static CloneCraftServer instance;

	@Getter private Server server;

	private final Object lock = new Object();

	private CloneCraftServer() {

	}

	private void start(final int tcp, final int udp, final float ppf) {

		server = new Server();
		ClassRegister.register(server);
		server.addListener(new ServerListener(server, ppf));
		server.start();

		try {

			server.bind(tcp, udp);

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

	public static final void startThread(final int tcp, final int udp, final float ppf) {

		thread = new Thread(() -> {

			Logger.info("Server thread started");

			instance = new CloneCraftServer();
			instance.start(tcp, udp, ppf);
			// This only occurs after the server has stopped
			instance = null;

		}, "CloneCraftServer");

		Logger.info("Starting server thread");
		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
