package cc.antho.clonecraft.client.core;

import static org.lwjgl.opengl.GL20.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;

import cc.antho.clonecraft.client.CloneCraftGame;
import lombok.Getter;

public class Shader {

	private static final FloatBuffer matrixBuffer4 = BufferUtils.createFloatBuffer(16);
	private static final FloatBuffer matrixBuffer3 = BufferUtils.createFloatBuffer(9);

	static {

		matrixBuffer4.limit(matrixBuffer4.capacity());
		matrixBuffer3.limit(matrixBuffer3.capacity());

	}

	@Getter private final int handle;
	private final Map<String, Integer> uniforms = new HashMap<>();

	private static final int createShader(final int type, final String source) {

		final int shader = glCreateShader(type);
		glShaderSource(shader, source);
		glCompileShader(shader);

		return shader;

	}

	public static final String loadShaderString(final String file) throws IOException {

		final InputStream vis = CloneCraftGame.class.getResourceAsStream(file);

		final InputStreamReader isr = new InputStreamReader(vis);
		final BufferedReader br = new BufferedReader(isr);

		String line;
		String string = "";

		while (true) {

			line = br.readLine();
			if (line == null) break;

			string += line + "\n";

		}

		br.close();
		isr.close();
		vis.close();

		return string;

	}

	public Shader(final String vertexSource, final String fragmentSource) {

		final int vs = createShader(GL_VERTEX_SHADER, vertexSource);
		final int fs = createShader(GL_FRAGMENT_SHADER, fragmentSource);

		handle = glCreateProgram();
		glAttachShader(handle, vs);
		glAttachShader(handle, fs);
		glLinkProgram(handle);
		glValidateProgram(handle);
		glDetachShader(handle, vs);
		glDetachShader(handle, fs);
		glDeleteShader(vs);
		glDeleteShader(fs);
		bind();

	}

	public int getUniformLocation(final String name) {

		if (uniforms.containsKey(name)) return uniforms.get(name);

		final int uniform = glGetUniformLocation(handle, name);
		System.out.println("New uniform in shader " + handle + ": " + name + " = " + uniform);
		uniforms.put(name, uniform);

		return uniform;

	}

	public void loadUniform1f(final String name, final float v0) {

		glUniform1f(getUniformLocation(name), v0);

	}

	public void loadUniform2f(final String name, final Vector2f v0) {

		glUniform2f(getUniformLocation(name), v0.x, v0.y);

	}

	public void loadUniform3f(final String name, final Vector3f v0) {

		glUniform3f(getUniformLocation(name), v0.x, v0.y, v0.z);

	}

	public void loadUniform4f(final String name, final Vector4f v0) {

		glUniform4f(getUniformLocation(name), v0.x, v0.y, v0.z, v0.w);

	}

	public void loadUniformMatrix3f(final String name, final Matrix3f v0) {

		v0.get(matrixBuffer3);
		glUniformMatrix3fv(getUniformLocation(name), false, matrixBuffer3);

	}

	public void loadUniformMatrix4f(final String name, final Matrix4f v0) {

		v0.get(matrixBuffer4);
		glUniformMatrix4fv(getUniformLocation(name), false, matrixBuffer4);

	}

	public void bind() {

		glUseProgram(handle);

	}

	public void unbind() {

		glUseProgram(0);

	}

	public void shutdown() {

		glDeleteShader(handle);

	}

}
