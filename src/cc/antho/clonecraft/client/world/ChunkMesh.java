package cc.antho.clonecraft.client.world;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import cc.antho.clonecraft.client.core.FloatVertexArray;
import cc.antho.clonecraft.client.pack.PackLoader;
import lombok.Getter;

public class ChunkMesh {

	@Getter private int vao;
	@Getter private int vboPositions;
	@Getter private int vboCoords;
	private int vertexCount;
	private final Chunk chunk;

	public ChunkMesh(final World world, final Chunk chunk) {

		this.chunk = chunk;

	}

	/**
	 * Called from chunk thread
	 */
	void model() {

		final FloatVertexArray chunkPositions = new FloatVertexArray();
		final FloatVertexArray chunkCoords = new FloatVertexArray();

		{

			final FloatVertexArray chunkSectionPositions = new FloatVertexArray();
			final FloatVertexArray coordTmp = new FloatVertexArray();

			for (int yy = 0; yy < Chunk.SIZE; yy++) {

				chunkSectionPositions.clear();

				for (int y = 0; y < ChunkSection.SIZE; y++)
					for (int z = 0; z < ChunkSection.SIZE; z++)
						for (int x = 0; x < ChunkSection.SIZE; x++) {

							final int wx = x + chunk.getX() * ChunkSection.SIZE;
							final int wy = y + yy * ChunkSection.SIZE;
							final int wz = z + chunk.getZ() * ChunkSection.SIZE;

							final BlockType block = chunk.getWorld().getBlock(wx, wy, wz);

							if (block == null) continue;

							final FloatVertexArray blockData = new FloatVertexArray();
							final FloatVertexArray blockDataTexs = new FloatVertexArray();

							if (chunk.getWorld().getBlock(wx - 1, wy, wz) == null) {
								blockData.append(BlockFace.LEFT.getPositions());

								coordTmp.clear();
								coordTmp.append(BlockFace.LEFT.getTexCoords());
								coordTmp.add(block.getLeft());
								coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
								blockDataTexs.append(coordTmp);
							}
							if (chunk.getWorld().getBlock(wx + 1, wy, wz) == null) {
								blockData.append(BlockFace.RIGHT.getPositions());

								coordTmp.clear();
								coordTmp.append(BlockFace.RIGHT.getTexCoords());
								coordTmp.add(block.getRight());
								coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
								blockDataTexs.append(coordTmp);
							}
							if (chunk.getWorld().getBlock(wx, wy, wz + 1) == null) {
								blockData.append(BlockFace.FRONT.getPositions());

								coordTmp.clear();
								coordTmp.append(BlockFace.FRONT.getTexCoords());
								coordTmp.add(block.getFront());
								coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
								blockDataTexs.append(coordTmp);
							}
							if (chunk.getWorld().getBlock(wx, wy, wz - 1) == null) {
								blockData.append(BlockFace.BACK.getPositions());

								coordTmp.clear();
								coordTmp.append(BlockFace.BACK.getTexCoords());
								coordTmp.add(block.getBack());
								coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
								blockDataTexs.append(coordTmp);
							}
							if (chunk.getWorld().getBlock(wx, wy + 1, wz) == null) {
								blockData.append(BlockFace.TOP.getPositions());

								coordTmp.clear();
								coordTmp.append(BlockFace.TOP.getTexCoords());
								coordTmp.add(block.getTop());
								coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
								blockDataTexs.append(coordTmp);
							}
							if (chunk.getWorld().getBlock(wx, wy - 1, wz) == null) {
								blockData.append(BlockFace.BOTTOM.getPositions());

								coordTmp.clear();
								coordTmp.append(BlockFace.BOTTOM.getTexCoords());
								coordTmp.add(block.getBottom());
								coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
								blockDataTexs.append(coordTmp);
							}

							chunkCoords.append(blockDataTexs);

							blockData.add(x, y, z);

							chunkSectionPositions.append(blockData);

						}

				chunkSectionPositions.add(0, yy * ChunkSection.SIZE, 0);
				chunkPositions.append(chunkSectionPositions);

			}

			chunkPositions.add(chunk.getX() * ChunkSection.SIZE, 0, chunk.getZ() * ChunkSection.SIZE);

			vertexCount = chunkPositions.size() / 3;

			positionBuffer = chunkPositions.get();
			coordsBuffer = chunkCoords.get();

		}

	}

	float[] positionBuffer;
	float[] coordsBuffer;

	public void upload() {

		shutdown();

		vao = glGenVertexArrays();
		glBindVertexArray(vao);

		vboPositions = glGenBuffers();
		final FloatBuffer buffer = BufferUtils.createFloatBuffer(positionBuffer.length);
		buffer.put(positionBuffer);
		buffer.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboPositions);

		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);

		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

		vboCoords = glGenBuffers();
		final FloatBuffer buffer2 = BufferUtils.createFloatBuffer(coordsBuffer.length);
		buffer2.put(coordsBuffer);
		buffer2.flip();
		glBindBuffer(GL_ARRAY_BUFFER, vboCoords);

		glBufferData(GL_ARRAY_BUFFER, buffer2, GL_STATIC_DRAW);

		glEnableVertexAttribArray(1);
		glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

		positionBuffer = null;
		coordsBuffer = null;

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

	public void shutdown() {

		glDeleteVertexArrays(vao);
		glDeleteBuffers(vboPositions);
		glDeleteBuffers(vboCoords);
		vao = 0;
		vboPositions = 0;
		vboCoords = 0;

	}

	public void render() {

		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

}
