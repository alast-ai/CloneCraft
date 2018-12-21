package cc.antho.clonecraft.server;

import java.util.ArrayList;
import java.util.List;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.client.world.ChunkSection;
import cc.antho.clonecraft.core.Mathf;
import cc.antho.clonecraft.core.packet.BlockUpdatePacket;
import cc.antho.clonecraft.core.packet.ChunkChangesPacket;
import cc.antho.clonecraft.core.packet.PlayerConnectPacket;
import cc.antho.clonecraft.core.packet.PlayerDisconnectPacket;
import cc.antho.clonecraft.core.packet.PlayerMovePacket;
import cc.antho.clonecraft.core.packet.PlayerSelfConnectPacket;

public final class ServerListener extends Listener {

	private final Server server;
	private final float playerPacketFreq;

	private final List<BlockUpdatePacket> blockChanges = new ArrayList<>();

	public ServerListener(final Server server, final float ppf) {

		this.server = server;
		this.playerPacketFreq = ppf;

	}

	public final void received(final Connection connection, final Object object) {

		if (object instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) object;

			blockChanges.add(p);
			server.sendToAllExceptTCP(connection.getID(), p);

		} else if (object instanceof PlayerMovePacket) {

			final PlayerMovePacket p = (PlayerMovePacket) object;

			server.sendToAllExceptTCP(connection.getID(), p);

		} else if (object instanceof ChunkChangesPacket) {

			final ChunkChangesPacket p = (ChunkChangesPacket) object;

			final List<BlockUpdatePacket> changes = new ArrayList<>();

			for (final BlockUpdatePacket change : this.blockChanges) {

				final int cx = Mathf.floor((float) change.x / (float) ChunkSection.SIZE);
				final int cz = Mathf.floor((float) change.z / (float) ChunkSection.SIZE);

				if (cx == p.x && cz == p.z) changes.add(change);

			}

			connection.sendTCP(new ChunkChangesPacket(blockChanges, p.x, p.x));

		}

	}

	public final void connected(final Connection connection) {

		connection.sendTCP(new PlayerSelfConnectPacket(playerPacketFreq));
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
