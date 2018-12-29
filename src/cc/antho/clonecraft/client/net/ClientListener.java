package cc.antho.clonecraft.client.net;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import cc.antho.clonecraft.client.Game;
import cc.antho.clonecraft.client.PlayerStore;
import cc.antho.clonecraft.client.state.GameState;
import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.client.world.Chunk;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.core.event.Event;
import cc.antho.clonecraft.core.event.EventDispatcher;
import cc.antho.clonecraft.core.event.EventListener;
import cc.antho.clonecraft.core.events.NetworkPacketEvent;
import cc.antho.clonecraft.core.packet.BlockUpdatePacket;
import cc.antho.clonecraft.core.packet.ChunkChangesPacket;
import cc.antho.clonecraft.core.packet.PlayerConnectPacket;
import cc.antho.clonecraft.core.packet.PlayerDisconnectPacket;
import cc.antho.clonecraft.core.packet.PlayerMovePacket;
import cc.antho.clonecraft.core.packet.PlayerSelfConnectPacket;

public final class ClientListener extends Listener implements EventListener {

	public static final Map<Integer, PlayerStore> players = new HashMap<>();
	public static boolean ready = false;
	public static float playerPacketFreq;

	private final Client client;

	public ClientListener(final Client client) {

		this.client = client;
		EventDispatcher.addListener(this);

	}

	public void received(final Connection connection, final Object object) {

		if (object instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) object;

			final World world = ((GameState) Game.getManager().getCurrentState()).getWorld();
			world.setBlock(p.x, p.y, p.z, BlockType.getBlock(p.type));
			final Chunk chunk = world.getChunkFromWorldCoord(p.x, p.z);
			if (chunk != null) chunk.update();

		} else if (object instanceof PlayerMovePacket) {

			final PlayerMovePacket p = (PlayerMovePacket) object;

			synchronized (players) {
				players.get(p.id).position.set(p.position);
			}

		} else if (object instanceof PlayerConnectPacket) {

			final PlayerConnectPacket p = (PlayerConnectPacket) object;

			synchronized (players) {
				players.put(p.id, new PlayerStore());
			}

		} else if (object instanceof PlayerDisconnectPacket) {

			final PlayerDisconnectPacket p = (PlayerDisconnectPacket) object;

			synchronized (players) {
				players.remove(p.id);
			}

		} else if (object instanceof PlayerSelfConnectPacket) {

			final PlayerSelfConnectPacket p = (PlayerSelfConnectPacket) object;
			playerPacketFreq = p.playerPacketFreq;
			ready = true;
			System.out.println("Server Provided Base Data: PPF" + playerPacketFreq);

		} else if (object instanceof ChunkChangesPacket) {

			final ChunkChangesPacket packet = (ChunkChangesPacket) object;

			final World world = ((GameState) Game.getManager().getCurrentState()).getWorld();

			if (!packet.changes.isEmpty()) {

				for (final BlockUpdatePacket p : packet.changes)
					world.setBlock(p.x, p.y, p.z, BlockType.getBlock(p.type));

				final Chunk chunk = world.getChunk(packet.x, packet.z);
				if (chunk != null) chunk.update();

			}

		}

	}

	public void disconnected(final Connection connection) {

		// CloneCraftGame.getInstance().stop();

	}

	public void onEvent(final Event event) {

		if (event instanceof NetworkPacketEvent) {

			final NetworkPacketEvent e = (NetworkPacketEvent) event;

			if (!e.fromServer) if (e.tcp) client.sendTCP(e.packet);
			else client.sendUDP(e.packet);

		}

	}

}
