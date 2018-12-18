package cc.antho.clonecraft.client;

import java.util.HashMap;
import java.util.Map;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import cc.antho.clonecraft.client.world.Chunk;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.core.packet.BlockUpdatePacket;
import cc.antho.clonecraft.core.packet.PlayerConnectPacket;
import cc.antho.clonecraft.core.packet.PlayerDisconnectPacket;
import cc.antho.clonecraft.core.packet.PlayerMovePacket;

public final class ClientListener extends Listener {

	public static final Map<Integer, PlayerStore> players = new HashMap<>();

	public void received(final Connection connection, final Object object) {

		if (object instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) object;

			final World world = ((GameState) CloneCraftGame.getInstance().getManager().getCurrentState()).getWorld();
			world.setBlock(p.x, p.y, p.z, p.type);
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

		}

	}

}
