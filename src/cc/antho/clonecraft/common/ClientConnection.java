package cc.antho.clonecraft.common;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.world.Chunk;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.common.packet.BlockUpdatePacket;
import cc.antho.clonecraft.common.packet.Packet;
import cc.antho.clonecraft.core.state.impl.GameState;

public final class ClientConnection extends Connection {

	public ClientConnection(final Socket socket, final ObjectInputStream ois, final ObjectOutputStream oos) {

		super(socket, ois, oos, 0);

	}

	public final void receive(final Packet packet) {

		System.out.println(packet);

		if (packet instanceof BlockUpdatePacket) {

			final BlockUpdatePacket p = (BlockUpdatePacket) packet;

			final World world = ((GameState) CloneCraftGame.getInstance().getManager().getCurrentState()).getWorld();
			world.setBlock(p.x, p.y, p.z, p.type);
			final Chunk chunk = world.getChunkFromWorldCoord(p.x, p.z);
			if (chunk != null) chunk.update();

		}

	}

}
