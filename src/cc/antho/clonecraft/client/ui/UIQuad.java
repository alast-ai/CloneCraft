package cc.antho.clonecraft.client.ui;

import static org.lwjgl.opengl.GL46.*;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

public class UIQuad {

	private static int vao;
	private static int vbo;

	public static void create() {

		final float[] data = new float[] { -1, -1, 1, -1, -1, 1, 1, 1 };

		vao = glGenVertexArrays();
		glBindVertexArray(vao);
		vbo = glGenBuffers();

		final FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		buffer.put(data);
		buffer.flip();

		glBindBuffer(GL_ARRAY_BUFFER, vbo);
		glBufferData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
		glEnableVertexAttribArray(0);
		glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);

		glBindVertexArray(0);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

	}

	public static void shutdown() {

		glDeleteVertexArrays(vao);
		glDeleteBuffers(vbo);

	}

	public static void render() {

		glBindVertexArray(vao);
		glBindBuffer(GL_ARRAY_BUFFER, 0);

		glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);

	}

}
