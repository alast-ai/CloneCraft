package cc.antho.clonecraft.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.core.ClassRegister;
import cc.antho.clonecraft.core.ConnectionDefaults;
import lombok.Getter;

public final class CloneCraftServer {

	public static Thread thread;
	public static CloneCraftServer instance;

	@Getter private Server server;
	@Getter public Boolean started = false;

	private CloneCraftServer() {

	}

	private void start(final int Port) {

		server = new Server();
		ClassRegister.register(server);
		server.addListener(new ServerListener(server));
		server.start();

		try {

			server.bind(Port, Port);
			started = true;

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	public static final void main(final int Port) {

		thread = new Thread(() -> {

			instance = new CloneCraftServer();
			instance.start(Port);

		}, "CloneCraftServer");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
