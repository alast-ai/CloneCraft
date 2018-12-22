package cc.antho.clonecraft.client.world;

import cc.antho.clonecraft.core.event.EventDispatcher;
import cc.antho.clonecraft.core.events.NetworkPacketEvent;
import cc.antho.clonecraft.core.math.Mathf;
import cc.antho.clonecraft.core.packet.ChunkChangesPacket;

public class ChunkGenerator {

	public static void generate(final Chunk chunk, final World world) {

		for (int z = 0; z < ChunkSection.SIZE; z++)
			for (int x = 0; x < ChunkSection.SIZE; x++) {

				final int gx = x + chunk.getX() * ChunkSection.SIZE;
				final int gz = z + chunk.getZ() * ChunkSection.SIZE;

				final int height = world.getHeightAtWorldPosition(gx, gz);

				chunk.setBlock(x, 0, z, BlockType.getBlock("core.bedrock"));

				for (int y = 1; y < height - 4; y++)
					chunk.setBlock(x, y, z, BlockType.getBlock("core.stone"));

				for (int y = height - 4; y < height; y++)
					chunk.setBlock(x, y, z, BlockType.getBlock("core.dirt"));

				if (world.getGrassNoise().eval(gx / 2f, gz / 2f) > .1f) chunk.setBlock(x, height, z, BlockType.getBlock("core.tallgrass"));

				if (world.getTreeNoise().eval(gx, gz) > .8f) {

					chunk.setBlock(x, height - 1, z, BlockType.getBlock("core.dirt"));

					final int treeHeight = Mathf.round((float) (world.getTreeNoise().eval(gx * 2f, gz * 2f) * 2f + 4f));

					for (int i = 0; i < treeHeight; i++)
						chunk.setBlock(x, height + i, z, BlockType.getBlock("core.log"));

				} else chunk.setBlock(x, height - 1, z, BlockType.getBlock("core.grass"));

			}

		EventDispatcher.dispatch(new NetworkPacketEvent(null, new ChunkChangesPacket(null, chunk.getX(), chunk.getZ()), false, true));

	}

}
