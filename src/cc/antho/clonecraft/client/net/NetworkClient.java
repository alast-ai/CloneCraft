package cc.antho.clonecraft.client.net;

import java.io.IOException;

import com.esotericsoftware.kryonet.Client;

import cc.antho.clonecraft.core.ClassRegister;
import cc.antho.clonecraft.core.Config;
import lombok.Getter;

public final class NetworkClient {

	@Getter private static Client networkClient;

	private NetworkClient() {

	}

	public static final void startNetworkClient() {

		networkClient = new Client(16384, 2048);
		networkClient.start();
		ClassRegister.register(networkClient);
		networkClient.addListener(new ClientListener(networkClient));

		try {

			networkClient.connect(5000, Config.ADDRESS, Config.TCP_PORT, Config.UDP_PORT);

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

}
