package cc.antho.clonecraft.client;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

import cc.antho.clonecraft.core.ClassRegister;
import lombok.Getter;

public final class CloneCraftClient {

	private static Thread thread;
	@Getter private static Client networkClient;

	private CloneCraftClient() {

	}

	public static final void main(final String address, final int tcp, final int udp) {

		thread = new Thread(() -> {

			networkClient = new Client();
			ClassRegister.register(networkClient.getKryo());
			networkClient.start();
			networkClient.addListener(new ClientListener());

			try {

				networkClient.connect(5000, address, tcp, udp);
				CloneCraftGame.main();

			} catch (final IOException e) {

			}

		}, "CloneCraftClient");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
