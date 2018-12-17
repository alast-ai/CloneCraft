package cc.antho.clonecraft.client.world;

import lombok.Getter;

public final class ChunkSection {

	public static final int SIZE = 16;

	@Getter private final BlockType[] blocks = new BlockType[SIZE * SIZE * SIZE];

	@Getter private final int y;

	public ChunkSection(final int y) {

		this.y = y;

	}

	public BlockType getBlock(final int x, final int y, final int z) {

		return blocks[x + z * SIZE + y * SIZE * SIZE];

	}

	public void setBlock(final int x, final int y, final int z, final BlockType block) {

		blocks[x + z * SIZE + y * SIZE * SIZE] = block;

	}

}
