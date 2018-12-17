package cc.antho.clonecraft.common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cc.antho.clonecraft.common.packet.Packet;

public final class ClientConnection extends Connection {

	public ClientConnection(final Socket socket, final ObjectInputStream ois, final ObjectOutputStream oos) {

		super(socket, ois, oos);

	}

	public final void receive(final Packet packet) {

		System.out.println(packet);

	}

}
