package cc.antho.clonecraft.common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cc.antho.clonecraft.common.packet.Packet;

public final class ServerConnection extends Connection {

	public ServerConnection(final Socket socket, final ObjectInputStream ois, final ObjectOutputStream oos) {

		super(socket, ois, oos);

	}

	public final void receive(final Packet packet) {

		System.out.println(packet);

	}

}
