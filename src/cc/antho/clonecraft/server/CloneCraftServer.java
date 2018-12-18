package cc.antho.clonecraft.server;

import java.io.IOException;

import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.common.ConnectionDefaults;
import cc.antho.clonecraft.common.ServerListener;
import cc.antho.clonecraft.core.ClassRegister;
import lombok.Getter;

public final class CloneCraftServer {

	private static Thread thread;

	@Getter private final Server server;

	private CloneCraftServer() {

		server = new Server();
		ClassRegister.register(server.getKryo());
		server.start();

		try {

			server.bind(ConnectionDefaults.PORT);

		} catch (final IOException e) {

			e.printStackTrace();

		}

		server.addListener(new ServerListener(this));

	}

	public static final void main(final String[] args) {

		thread = new Thread(() -> {

			new CloneCraftServer();

		}, "CloneCraftServer");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
