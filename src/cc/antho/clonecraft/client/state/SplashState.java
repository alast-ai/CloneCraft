package cc.antho.clonecraft.client.state;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import org.joml.Matrix4f;

import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.core.Shader;
import cc.antho.clonecraft.client.core.Texture;
import cc.antho.clonecraft.client.ui.UIQuad;
import cc.antho.clonecraft.client.world.thread.ChunkThread;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.state.State;

public class SplashState extends State {

	// TODO there shouldn't be a black screen

	private Shader uiShader;
	private Texture splash;

	public void init() {

		ChunkThread.lock.lock();
		glfwMakeContextCurrent(CloneCraftGame.getInstance().getWindow().getHandle());

		loadUI();

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		CloneCraftGame.getInstance().getWindow().trigger();

		glfwMakeContextCurrent(0);
		ChunkThread.lock.unlock();

	}

	private void loadUI() {

		try {

			final String vertexShader = Shader.loadShaderString("/shaders/ui_vertex.glsl");
			final String fragmentShader = Shader.loadShaderString("/shaders/ui_fragment.glsl");

			uiShader = new Shader(vertexShader, fragmentShader);
			splash = Texture.create("/textures/clonecraft_splash_l.png", false);

			UIQuad.create();

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	public void tick() {

		// TODO it should wait a certain amount of time before switching states
		// (not keypress)
		if (CloneCraftGame.getInput().isKeyDown(GLFW_KEY_SPACE)) manager.setState(new GameState());

	}

	public void render() {

		glClear(Config.CLEAR);

		uiShader.bind();

		final Matrix4f matrix = new Matrix4f();

		matrix.scale(1, 1, 1);

		splash.bind(0);
		uiShader.loadUniformMatrix4f("u_model", matrix);

		glEnable(GL_BLEND);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		UIQuad.render();
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);

	}

	public void shutdown() {

		UIQuad.shutdown();
		uiShader.shutdown();
		splash.shutdown();

	}

}
