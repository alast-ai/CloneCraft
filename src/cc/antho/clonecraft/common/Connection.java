package cc.antho.clonecraft.common;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cc.antho.clonecraft.common.packet.Packet;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public abstract class Connection {

	@Getter private final Socket socket;
	@Getter private final ObjectInputStream ois;
	@Getter private final ObjectOutputStream oos;

	public boolean submit(final Packet packet) {

		try {

			oos.writeObject(packet);
			return true;

		} catch (final IOException e) {

			return false;

		}

	}

	public final void read() {

		try {

			receive((Packet) ois.readObject());

		} catch (ClassNotFoundException | IOException e) {

			e.printStackTrace();

		}

	}

	public abstract void receive(final Packet packet);

}
