package cc.antho.clonecraft.core.state.impl;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

import java.io.IOException;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;

import cc.antho.ascl8.math.Mathf;
import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.Config;
import cc.antho.clonecraft.client.core.Player;
import cc.antho.clonecraft.client.core.Shader;
import cc.antho.clonecraft.client.core.Texture;
import cc.antho.clonecraft.client.ui.UIQuad;
import cc.antho.clonecraft.client.world.ChunkSection;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.client.world.thread.ChunkThread;
import cc.antho.clonecraft.core.state.State;
import lombok.Getter;

public class GameState extends State {

	private int chunk_u_view;
	private int chunk_u_projection;
	private int ui_u_model;

	private FloatBuffer matrixBuffer4;

	private final Player camera = new Player();

	@Getter private World world;
	private Shader chunkShader, uiShader;
	private Texture atlas, crosshair;

	public void init() {

		matrixBuffer4 = BufferUtils.createFloatBuffer(16);
		matrixBuffer4.limit(matrixBuffer4.capacity());

		ChunkThread.startThreads();

		world = new World("Oniichan");

		loadShader();
		loadAtlas();

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		CloneCraftGame.getInstance().getWindow().trigger();

	}

	private void loadAtlas() {

		try {

			atlas = new Texture("/atlas.png", true);
			crosshair = new Texture("/crosshair.png", false);

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	private void loadShader() {

		try {

			String vertexShader = Shader.loadShaderString("/chunk_vertex.glsl");
			String fragmentShader = Shader.loadShaderString("/chunk_fragment.glsl");

			chunkShader = new Shader(vertexShader, fragmentShader);

			chunk_u_projection = chunkShader.getUniformLocation("u_projection");
			chunk_u_view = chunkShader.getUniformLocation("u_view");

			vertexShader = Shader.loadShaderString("/ui_vertex.glsl");
			fragmentShader = Shader.loadShaderString("/ui_fragment.glsl");

			uiShader = new Shader(vertexShader, fragmentShader);

			ui_u_model = uiShader.getUniformLocation("u_model");

			UIQuad.create();

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	public void tick() {

		camera.rotate(.3f, .3f);
		camera.move(4f, world);
		camera.update(world);

		ChunkThread.playerPostionChunk.x = Mathf.floor(camera.getPosition().x / ChunkSection.SIZE);
		ChunkThread.playerPostionChunk.y = Mathf.floor(camera.getPosition().z / ChunkSection.SIZE);

		world.addNewChunks((int) ChunkThread.playerPostionChunk.x, (int) ChunkThread.playerPostionChunk.y);

	}

	public void render() {

		atlas.bind(0);
		chunkShader.bind();

		glViewport(0, 0, CloneCraftGame.getInstance().getWindow().getWidth(), CloneCraftGame.getInstance().getWindow().getHeight());

		camera.getView().get(matrixBuffer4);
		glUniformMatrix4fv(chunk_u_view, false, matrixBuffer4);

		if (camera.isProjectionDirty()) {

			camera.getProjection().get(matrixBuffer4);
			glUniformMatrix4fv(chunk_u_projection, false, matrixBuffer4);

			camera.setProjectionDirty(false);

		}

		glClear(Config.CLEAR);

		world.render();

		uiShader.bind();

		final float width = CloneCraftGame.getInstance().getWindow().getWidth();
		final float height = CloneCraftGame.getInstance().getWindow().getHeight();

		final Matrix4f matrix = new Matrix4f();

		if (width > height) matrix.scale(height / width, 1, 1);
		else matrix.scale(1, width / height, 1);
		matrix.scale(.05f);

		matrix.get(matrixBuffer4);

		crosshair.bind(0);
		glUniformMatrix4fv(ui_u_model, false, matrixBuffer4);

		glEnable(GL_BLEND);
		glBlendFunc(GL_ONE_MINUS_DST_COLOR, GL_ZERO);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);
		UIQuad.render();
		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);

	}

	public void shutdown() {

		ChunkThread.stopThreads();
		chunkShader.shutdown();
		atlas.shutdown();

		UIQuad.shutdown();

	}

}
