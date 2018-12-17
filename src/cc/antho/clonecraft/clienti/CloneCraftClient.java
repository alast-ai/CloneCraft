package cc.antho.clonecraft.clienti;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.common.ClientConnection;
import cc.antho.clonecraft.common.ConnectionDefaults;
import lombok.Getter;

public final class CloneCraftClient {

	private static Thread thread;
	@Getter private static ClientConnection connection;

	private CloneCraftClient() {

	}

	private static final Socket createClient(final String address, final int port) {

		try {

			return new Socket(address, port);

		} catch (final IOException e) {

			return null;

		}

	}

	public static final void main(final String[] args) {

		CloneCraftGame.main();

		thread = new Thread(() -> {

			try {

				final Socket client = createClient(ConnectionDefaults.ADDRESS, ConnectionDefaults.PORT);
				if (client == null) throw new RuntimeException("Failed to create client!");

				final ObjectOutputStream oos = new ObjectOutputStream(client.getOutputStream());
				final ObjectInputStream ois = new ObjectInputStream(client.getInputStream());
				connection = new ClientConnection(client, ois, oos);

			} catch (final IOException e) {

				e.printStackTrace();

			}

			while (!Thread.interrupted())
				connection.read();

		}, "CloneCraftClient");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
