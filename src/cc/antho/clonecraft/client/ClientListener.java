package cc.antho.clonecraft.client;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import cc.antho.clonecraft.client.world.Chunk;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.core.packet.BlockUpdatePacket;

public final class ClientListener extends Listener {

	public void received(final Connection connection, final Object object) {

		System.out.println(object);

		if (object instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) object;

			final World world = ((GameState) CloneCraftGame.getInstance().getManager().getCurrentState()).getWorld();
			world.setBlock(p.x, p.y, p.z, p.type);
			final Chunk chunk = world.getChunkFromWorldCoord(p.x, p.z);
			if (chunk != null) chunk.update();

		}

	}

}
