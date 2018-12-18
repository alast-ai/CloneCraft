package cc.antho.clonecraft.clienti;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.common.ClientListener;
import cc.antho.clonecraft.common.ConnectionDefaults;
import cc.antho.clonecraft.core.ClassRegister;
import lombok.Getter;

public final class CloneCraftClient {

	private static Thread thread;
	@Getter private static Client client;

	private CloneCraftClient() {

	}

	public static final void main(final String[] args) {

		CloneCraftGame.main();

		thread = new Thread(() -> {

			client = new Client();
			ClassRegister.register(client.getKryo());
			client.start();
			client.addListener(new ClientListener());

			try {

				client.connect(5000, ConnectionDefaults.ADDRESS, ConnectionDefaults.PORT);

			} catch (final IOException e) {

				e.printStackTrace();

			}

		}, "CloneCraftClient");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
