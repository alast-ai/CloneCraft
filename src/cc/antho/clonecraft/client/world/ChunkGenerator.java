package cc.antho.clonecraft.client.world;

public class ChunkGenerator {

	public static void generate(final Chunk chunk, final World world) {

		for (int z = 0; z < ChunkSection.SIZE; z++)
			for (int x = 0; x < ChunkSection.SIZE; x++) {

				final int gx = x + chunk.getX() * ChunkSection.SIZE;
				final int gz = z + chunk.getZ() * ChunkSection.SIZE;

				final int height = world.getHeightAtWorldPosition(gx, gz);

				chunk.setBlock(x, 0, z, BlockType.BEDROCK);

				for (int y = 1; y < height - 4; y++)
					chunk.setBlock(x, y, z, BlockType.STONE);

				for (int y = height - 4; y < height; y++)
					chunk.setBlock(x, y, z, BlockType.DIRT);

				if (world.getGrassNoise().eval(gx / 2f, gz / 2f) > .1f) chunk.setBlock(x, height, z, BlockType.TALLGRASS);

				if (world.getTreeNoise().eval(gx, gz) > .8f) {

					chunk.setBlock(x, height - 1, z, BlockType.DIRT);

					for (int i = 0; i < 4; i++)
						chunk.setBlock(x, height + i, z, BlockType.LOG);

				} else chunk.setBlock(x, height - 1, z, BlockType.GRASS);

			}

	}

}
