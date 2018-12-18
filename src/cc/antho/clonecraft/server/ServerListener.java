package cc.antho.clonecraft.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.core.packet.BlockUpdatePacket;

public final class ServerListener extends Listener {

	private final Server server;

	public ServerListener(final Server server) {

		this.server = server;

	}

	public final void received(final Connection connection, final Object object) {

		System.out.println(object);

		if (object instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) object;

			server.sendToAllExceptTCP(connection.getID(), p);

		}

	}

}
