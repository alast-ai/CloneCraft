package cc.antho.clonecraft.common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cc.antho.clonecraft.common.packet.BlockUpdatePacket;
import cc.antho.clonecraft.common.packet.Packet;
import cc.antho.clonecraft.server.CloneCraftServer;

public final class ServerConnection extends Connection {

	private final CloneCraftServer server;

	public ServerConnection(final CloneCraftServer server, final int id, final Socket socket, final ObjectInputStream ois, final ObjectOutputStream oos) {

		super(socket, ois, oos, id);

		this.server = server;

	}

	public final void receive(final Packet packet) {

		System.out.println(packet);

		if (packet instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) packet;

			synchronized (server.getConnections()) {

				for (final ServerConnection connection : server.getConnections().values()) {

					if (connection.getId() == getId()) continue;
					connection.submit(p);

				}

			}

		}

	}

}
