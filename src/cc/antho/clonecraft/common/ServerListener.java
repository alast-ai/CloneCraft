package cc.antho.clonecraft.common;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import cc.antho.clonecraft.common.packet.BlockUpdatePacket;
import cc.antho.clonecraft.server.CloneCraftServer;

public final class ServerListener extends Listener {

	private final CloneCraftServer server;

	public ServerListener(final CloneCraftServer server) {

		this.server = server;

	}

	public void received(final Connection connection, final Object object) {

		System.out.println(object);

		if (object instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) object;

			server.getServer().sendToAllExceptTCP(connection.getID(), p);

		}

	}

}
