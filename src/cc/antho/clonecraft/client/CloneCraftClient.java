package cc.antho.clonecraft.client;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

import cc.antho.clonecraft.core.ClassRegister;
import lombok.Getter;

public final class CloneCraftClient {

	public static String address;
	public static int tcp, udp;

	private static Thread thread;
	@Getter private static Client networkClient;

	private CloneCraftClient() {

	}

	public static final void main() {

		thread = new Thread(() -> {

			networkClient = new Client();
			ClassRegister.register(networkClient);
			networkClient.addListener(new ClientListener(networkClient));
			networkClient.start();

			try {

				networkClient.connect(5000, address, tcp, udp);

			} catch (final IOException e) {

				e.printStackTrace();

			}

		}, "CloneCraftClient");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
