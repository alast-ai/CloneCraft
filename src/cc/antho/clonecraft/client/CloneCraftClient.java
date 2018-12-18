package cc.antho.clonecraft.client;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

import cc.antho.clonecraft.core.ClassRegister;
import cc.antho.clonecraft.core.ConnectionDefaults;
import lombok.Getter;

public final class CloneCraftClient {

	private static Thread thread;
	@Getter private static Client client;

	private CloneCraftClient() {

	}

	public static final void main() {

		CloneCraftGame.main();

		thread = new Thread(() -> {

			client = new Client();
			ClassRegister.register(client.getKryo());
			client.start();
			client.addListener(new ClientListener());

			try {

				client.connect(5000, ConnectionDefaults.ADDRESS, ConnectionDefaults.TCP_PORT, ConnectionDefaults.TCP_PORT);

			} catch (final IOException e) {

				e.printStackTrace();

			}

		}, "CloneCraftClient");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
