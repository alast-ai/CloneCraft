package cc.antho.clonecraft.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import cc.antho.clonecraft.common.ConnectionDefaults;
import cc.antho.clonecraft.common.ServerConnection;
import cc.antho.clonecraft.common.packet.OnConnectPacket;
import lombok.Getter;

public final class CloneCraftServer {

	private static Thread thread;
	private static Thread acceptThread;

	private final ServerSocket server;
	@Getter private final Map<Socket, ServerConnection> connections = new HashMap<>();

	private CloneCraftServer() {

		server = createServer(ConnectionDefaults.PORT);
		if (server == null) throw new RuntimeException("Failed to create server!");

		acceptThread = new Thread(() -> {

			try {

				while (!Thread.interrupted()) {

					final Socket client = server.accept();
					final ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
					final ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
					final ServerConnection connection = new ServerConnection(this, client, ois, oos);

					synchronized (connections) {

						connections.put(client, connection);

					}

					connection.submit(new OnConnectPacket());

				}

			} catch (final IOException e) {

				e.printStackTrace();

			}

		}, "CloneCraftServer-Accept");

		acceptThread.setPriority(Thread.MAX_PRIORITY);
		acceptThread.start();

		while (!Thread.interrupted())
			synchronized (connections) {

				for (final ServerConnection client : connections.values())
					client.read();

			}

	}

	private final ServerSocket createServer(final int port) {

		try {

			return new ServerSocket(ConnectionDefaults.PORT);

		} catch (final IOException e) {

			return null;

		}

	}

	public static final void main(final String[] args) {

		thread = new Thread(() -> {

			new CloneCraftServer();

		}, "CloneCraftServer");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
