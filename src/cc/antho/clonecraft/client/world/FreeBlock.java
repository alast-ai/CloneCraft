package cc.antho.clonecraft.client.world;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import cc.antho.clonecraft.client.core.FloatVertexArray;
import cc.antho.clonecraft.client.pack.PackLoader;
import lombok.Getter;

public class FreeBlock {

	@Getter private final int vao;
	@Getter private final int vboPositions;
	@Getter private final int vboCoords;
	private final int vertexCount;

	public FreeBlock(final BlockType type) {

		final FloatVertexArray coordTmp = new FloatVertexArray();
		final FloatVertexArray chunkPositions = new FloatVertexArray();
		final FloatVertexArray chunkCoords = new FloatVertexArray();

		chunkPositions.append(BlockFace.LEFT.getPositions());
		chunkPositions.append(BlockFace.RIGHT.getPositions());
		chunkPositions.append(BlockFace.FRONT.getPositions());
		chunkPositions.append(BlockFace.BACK.getPositions());
		chunkPositions.append(BlockFace.TOP.getPositions());
		chunkPositions.append(BlockFace.BOTTOM.getPositions());

		coordTmp.clear();
		coordTmp.append(BlockFace.LEFT.getTexCoords());
		coordTmp.add(type.getLeft());
		coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
		chunkCoords.append(coordTmp);
		coordTmp.clear();
		coordTmp.append(BlockFace.RIGHT.getTexCoords());
		coordTmp.add(type.getLeft());
		coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
		chunkCoords.append(coordTmp);
		coordTmp.clear();
		coordTmp.append(BlockFace.FRONT.getTexCoords());
		coordTmp.add(type.getLeft());
		coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
		chunkCoords.append(coordTmp);
		coordTmp.clear();
		coordTmp.append(BlockFace.BACK.getTexCoords());
		coordTmp.add(type.getLeft());
		coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
		chunkCoords.append(coordTmp);
		coordTmp.clear();
		coordTmp.append(BlockFace.TOP.getTexCoords());
		coordTmp.add(type.getLeft());
		coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
		chunkCoords.append(coordTmp);
		coordTmp.clear();
		coordTmp.append(BlockFace.BOTTOM.getTexCoords());
		coordTmp.add(type.getLeft());
		coordTmp.mul(PackLoader.WIDTH_SCALE, PackLoader.HEIGHT_SCALE);
		chunkCoords.append(coordTmp);

		vertexCount = chunkPositions.size() / 3;

		float[] positionBuffer = chunkPositions.get();
		float[] coordsBuffer = chunkCoords.get();

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

	public void render() {

		glBindVertexArray(vao);
		glDrawArrays(GL_TRIANGLES, 0, vertexCount);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

}
