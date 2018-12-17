package cc.antho.clonecraft.client.world;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import cc.antho.ascl8.math.Mathf;
import cc.antho.ascl8.noise.OpenSimplexNoise;
import cc.antho.clonecraft.client.world.thread.ChunkThread;
import lombok.Getter;

public class World {

	@Getter private volatile List<Chunk> chunks = new ArrayList<>();

	@Getter private final OpenSimplexNoise noise;

	public World(final long seed) {

		final Random r = new Random(seed);

		noise = new OpenSimplexNoise(r.nextLong());

	}

	public World(final String seed) {

		this(seed.hashCode());

	}

	public int getHeightAtWorldPosition(final float x, final float z) {

		return getHeightAtWorldPosition(Mathf.floor(x), Mathf.floor(z));

	}

	public int getHeightAtWorldPosition(final int x, final int z) {

		final double h0 = Mathf.map((float) noise.eval(x / 128d, z / 128d), -1f, 1f, 10f, 100f);
		final double h1 = Mathf.map((float) noise.eval(x / 32d, z / 32d), -1f, 1f, 0f, 10f);

		return (int) (h0 + h1);

	}

	public Chunk addChunk(final int x, final int z) {

		synchronized (chunks) {

			for (int i = chunks.size() - 1; i >= 0; i--) {
				final Chunk c = chunks.get(i);
				if (c.getX() == x && c.getZ() == z) return c;
			}

			final Chunk chunk = new Chunk(x, z, this);

			chunks.add(chunk);

			return chunk;

		}

	}

	public Chunk getChunkFromWorldCoord(final float x, final float z) {

		return getChunk(Mathf.floor(x / ChunkSection.SIZE), Mathf.floor(z / ChunkSection.SIZE));

	}

	public Chunk getChunk(final int x, final int z) {

		synchronized (chunks) {

			for (int i = chunks.size() - 1; i >= 0; i--) {

				final Chunk c = chunks.get(i);
				if (c == null) continue;
				if (c.getX() == x && c.getZ() == z) return c;

			}

		}

		return null;

	}

	public BlockType getBlock(final float x, final float y, final float z) {

		return getBlock(Mathf.floor(x), Mathf.floor(y), Mathf.floor(z));

	}

	public BlockType getBlock(final int x, final int y, final int z) {

		final int cx = Mathf.floor((float) x / (float) ChunkSection.SIZE);
		final int cz = Mathf.floor((float) z / (float) ChunkSection.SIZE);
		final int bx = x - cx * ChunkSection.SIZE;
		final int bz = z - cz * ChunkSection.SIZE;

		final Chunk chunk = getChunk(cx, cz);
		if (chunk == null) return null;

		return chunk.getBlock(bx, y, bz);

	}

	public void setBlock(final float x, final float y, final float z, final BlockType block) {

		setBlock(Mathf.floor(x), Mathf.floor(y), Mathf.floor(z), block);

	}

	public void setBlock(final int x, final int y, final int z, final BlockType block) {

		final int cx = Mathf.floor((float) x / (float) ChunkSection.SIZE);
		final int cz = Mathf.floor((float) z / (float) ChunkSection.SIZE);
		final int bx = x - cx * ChunkSection.SIZE;
		final int bz = z - cz * ChunkSection.SIZE;

		final Chunk chunk = getChunk(cx, cz);
		if (chunk == null) return;

		chunk.setBlock(bx, y, bz, block);

	}

	public void addNewChunks(final int cx, final int cz) {

		final int d = 8;

		for (int zz = -d; zz <= d; zz++)
			for (int xx = -d; xx <= d; xx++) {

				final Chunk chunk = addChunk(xx + cx, zz + cz);
				chunk.generate();

			}

		final List<Chunk> toDelete = new ArrayList<>();

		for (int i = chunks.size() - 1; i >= 0; i--) {

			final Chunk chunk = chunks.get(i);
			final int rx = Math.abs(chunk.getX() - cx);
			final int rz = Math.abs(chunk.getZ() - cz);

			if (rx > d || rz > d) toDelete.add(chunk);

		}

		for (int i = 0; i < toDelete.size(); i++)
			removeChunk(toDelete.get(i));

	}

	public void removeChunk(final Chunk chunk) {

		chunk.getStatus().awaitingDeletion = true;
		ChunkThread.deleteThread.add(chunk);

		synchronized (chunks) {

			chunks.remove(chunk);

		}

	}

	public void render() {

		getChunks().forEach(chunk -> {

			if (chunk != null) chunk.render();

		});

	}

}
