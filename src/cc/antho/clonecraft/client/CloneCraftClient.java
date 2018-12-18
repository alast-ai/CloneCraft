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

	public static final void main(String IPAddr, int Port) {

		CloneCraftGame.main();

		thread = new Thread(() -> {

			networkClient = new Client();
			ClassRegister.register(networkClient.getKryo());
			networkClient.start();
			networkClient.addListener(new ClientListener());

			try {

				networkClient.connect(5000, IPAddr, Port, Port);

			} catch (final IOException e) {

				if(e.getMessage().startsWith("Unable to connect to")) {
					
					if(CloneCraftGame.getInstance() != null) {
						
						CloneCraftGame.getInstance().stop();
						Debugger.getThread().interrupt();
						
					}
					
				}

			}

		}, "CloneCraftClient");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

}
