package cc.antho.clonecraft.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.core.packet.BlockUpdatePacket;
import cc.antho.clonecraft.core.packet.PlayerConnectPacket;
import cc.antho.clonecraft.core.packet.PlayerDisconnectPacket;
import cc.antho.clonecraft.core.packet.PlayerMovePacket;

public final class ServerListener extends Listener {

	private final Server server;

	public ServerListener(final Server server) {

		this.server = server;

	}

	public final void received(final Connection connection, final Object object) {

		if (object instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) object;

			server.sendToAllExceptTCP(connection.getID(), p);

		} else if (object instanceof PlayerMovePacket) {

			final PlayerMovePacket p = (PlayerMovePacket) object;

			server.sendToAllExceptTCP(connection.getID(), p);

		}

	}

	public final void connected(final Connection connection) {

		server.sendToAllExceptTCP(connection.getID(), new PlayerConnectPacket(connection.getID()));

		for (final Connection c : server.getConnections()) {

			if (c.getID() == connection.getID()) continue;
			connection.sendTCP(new PlayerConnectPacket(c.getID()));

		}

	}

	public final void disconnected(final Connection connection) {

		server.sendToAllExceptTCP(connection.getID(), new PlayerDisconnectPacket(connection.getID()));

	}

}
