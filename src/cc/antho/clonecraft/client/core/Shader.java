package cc.antho.clonecraft.client.core;

import static org.lwjgl.opengl.GL46.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import cc.antho.clonecraft.client.CloneCraftGame;
import lombok.Getter;

public class Shader {

	@Getter private final int handle;

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

		return glGetUniformLocation(handle, name);

	}

	public void loadUniform1f(final String name, final float v0) {

		glUniform1f(getUniformLocation(name), v0);

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
