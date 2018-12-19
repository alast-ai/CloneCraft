package cc.antho.clonecraft.client;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import org.joml.Matrix4f;

import cc.antho.clonecraft.client.core.Player;
import cc.antho.clonecraft.client.core.Shader;
import cc.antho.clonecraft.client.core.Texture;
import cc.antho.clonecraft.client.ui.UIQuad;
import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.client.world.ChunkSection;
import cc.antho.clonecraft.client.world.FreeBlock;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.client.world.thread.ChunkThread;
import cc.antho.clonecraft.core.Mathf;
import cc.antho.clonecraft.core.state.State;
import lombok.Getter;

public class GameState extends State {

	private final Player player = new Player();

	@Getter private World world;
	private Shader chunkShader, uiShader;
	private Texture atlas, crosshair;

	public void init() {

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

			vertexShader = Shader.loadShaderString("/ui_vertex.glsl");
			fragmentShader = Shader.loadShaderString("/ui_fragment.glsl");

			uiShader = new Shader(vertexShader, fragmentShader);

			UIQuad.create();

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	public void tick() {

		player.tick(world);

		ChunkThread.playerPostionChunk.x = Mathf.floor(player.getPosition().x / ChunkSection.SIZE);
		ChunkThread.playerPostionChunk.y = Mathf.floor(player.getPosition().z / ChunkSection.SIZE);

		world.addNewChunks((int) ChunkThread.playerPostionChunk.x, (int) ChunkThread.playerPostionChunk.y);

	}

	FreeBlock freeBlock = new FreeBlock(BlockType.SAND);

	public void render() {

		atlas.bind(0);
		chunkShader.bind();

		glViewport(0, 0, CloneCraftGame.getInstance().getWindow().getWidth(), CloneCraftGame.getInstance().getWindow().getHeight());

		chunkShader.loadUniformMatrix4f("u_view", player.camera.getView());

		if (player.camera.isProjectionDirty()) {

			chunkShader.loadUniformMatrix4f("u_projection", player.camera.getProjection());
			player.camera.setProjectionDirty(false);

		}

		glClear(Config.CLEAR);

		final Matrix4f m = new Matrix4f();

		chunkShader.loadUniformMatrix4f("u_model", m);
		world.render();

		synchronized (ClientListener.players) {

			for (final PlayerStore store : ClientListener.players.values()) {

				m.translation(store.position);
				chunkShader.loadUniformMatrix4f("u_model", m);
				freeBlock.render();

			}

		}

		uiShader.bind();

		final float width = CloneCraftGame.getInstance().getWindow().getWidth();
		final float height = CloneCraftGame.getInstance().getWindow().getHeight();

		final Matrix4f matrix = new Matrix4f();

		if (width > height) matrix.scale(height / width, 1, 1);
		else matrix.scale(1, width / height, 1);
		matrix.scale(.05f);

		crosshair.bind(0);
		uiShader.loadUniformMatrix4f("u_model", matrix);

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
