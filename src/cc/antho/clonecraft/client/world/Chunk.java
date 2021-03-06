package cc.antho.clonecraft.client.world;

import cc.antho.clonecraft.client.world.thread.ChunkThread;
import lombok.Getter;
import lombok.Setter;

public class Chunk {

	public static final int SIZE = 16;

	private final ChunkSection[] sections = new ChunkSection[SIZE];

	@Getter private final int x, z;
	@Getter @Setter private ChunkMesh mesh;
	@Getter private final World world;

	@Getter private final ChunkStatus status = new ChunkStatus();

	public Chunk(final int x, final int z, final World world) {

		this.x = x;
		this.z = z;
		this.world = world;

		for (int y = 0; y < sections.length; y++)
			sections[y] = new ChunkSection(y);

		mesh = new ChunkMesh(world, this);

	}

	public void generate() {

		if (status.generated) return;

		ChunkGenerator.generate(this, world);

		status.generated = true;

		// Send this chunk and neighboring chunks to be put into the model queue

		update();

	}

	/**
	 * Called from chunk thread
	 */
	public void model() {

		mesh.model();

	}

	public ChunkSection getSection(final int y) {

		return sections[y];

	}

	public BlockType getBlock(final int x, final int y, final int z) {

		final int sectionIndex = (int) Math.floor((float) y / (float) ChunkSection.SIZE);
		final int yValue = y - sectionIndex * ChunkSection.SIZE;

		if (sectionIndex < 0) return null;
		if (sectionIndex >= SIZE - 1) return null;

		final ChunkSection section = sections[sectionIndex];
		return section.getBlock(x, yValue, z);

	}

	public void setBlock(final int x, final int y, final int z, final BlockType block) {

		final int sectionIndex = (int) Math.floor((float) y / (float) SIZE);
		final int yValue = y - sectionIndex * SIZE;

		final ChunkSection section = sections[sectionIndex];
		section.setBlock(x, yValue, z, block);

	}

	public void render() {

		if (mesh != null) mesh.render();

	}

	public void update() {

		Chunk c;
		ChunkThread.modelThread.add(this);
		if ((c = getWorld().getChunk(getX() - 1, getZ())) != null) ChunkThread.modelThread.add(c);
		if ((c = getWorld().getChunk(getX() + 1, getZ())) != null) ChunkThread.modelThread.add(c);
		if ((c = getWorld().getChunk(getX(), getZ() - 1)) != null) ChunkThread.modelThread.add(c);
		if ((c = getWorld().getChunk(getX(), getZ() + 1)) != null) ChunkThread.modelThread.add(c);

	}

}
