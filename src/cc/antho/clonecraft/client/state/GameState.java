package cc.antho.clonecraft.client.state;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import cc.antho.clonecraft.client.ClientListener;
import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.ContextManager;
import cc.antho.clonecraft.client.NetworkClient;
import cc.antho.clonecraft.client.PlayerStore;
import cc.antho.clonecraft.client.core.Player;
import cc.antho.clonecraft.client.core.Shader;
import cc.antho.clonecraft.client.core.Texture2D;
import cc.antho.clonecraft.client.mod.ModLoader;
import cc.antho.clonecraft.client.ui.UIRenderer;
import cc.antho.clonecraft.client.ui.UITexture;
import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.client.world.ChunkSection;
import cc.antho.clonecraft.client.world.FreeBlock;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.client.world.thread.ChunkThread;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.math.Mathf;
import cc.antho.clonecraft.core.state.State;
import lombok.Getter;

public class GameState extends State {

	private final Player player = new Player();

	@Getter private World world;
	private Shader chunkShader;
	private Texture2D atlas;

	private FreeBlock freeBlock;
	private UIRenderer uiRenderer;
	private UITexture crosshair;

	public void init() {

		ContextManager.lock();

		loadAtlas();
		loadShader();

		freeBlock = new FreeBlock(BlockType.getBlock("core.sand"));

		world = new World("NFzttn4UxfQD8aOhYyeNDXs3FnXHEioT");

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		CloneCraftGame.getInstance().getWindow().trigger();

		ContextManager.unlock();

		ChunkThread.startThreads();

	}

	private void loadAtlas() {

		try {

			final ModLoader loader = new ModLoader();
			final BufferedImage atlas = loader.loadAll();
			this.atlas = new Texture2D(atlas, true);

			crosshair = new UITexture();
			crosshair.texture = new Texture2D(loader.getMod(Config.CROSSHAIR).getCrosshair(), false);

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	private void loadShader() {

		try {

			final String vertexShader = Shader.loadShaderString("/shaders/chunk_vertex.glsl");
			final String fragmentShader = Shader.loadShaderString("/shaders/chunk_fragment.glsl");

			chunkShader = new Shader(vertexShader, fragmentShader);

			uiRenderer = new UIRenderer();
			uiRenderer.addElement(crosshair);

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

		if (player.curDirty) {

			if (player.curBlock != null) player.curBlock.shutdown();

			player.curBlock = new FreeBlock(BlockType.getPalette().get(player.blockIndex));
			player.curDirty = false;

		}

		final float offsetX = .3f;
		final float offsetY = -.6f;
		final float offsetZ = .7f;

		final Vector3f handPosition = new Vector3f();
		handPosition.add(new Vector3f(player.camera.forward).mul(offsetZ));
		handPosition.add(new Vector3f(player.camera.right).mul(offsetX));
		handPosition.add(new Vector3f(player.camera.up).mul(offsetY));
		handPosition.add(player.camera.position);

		m.translation(handPosition);
		m.scale(.4f);
		m.rotate(Mathf.toRadians(-player.camera.rotation.z), 0, 0, 1);
		m.rotate(Mathf.toRadians(-player.camera.rotation.y), 0, 1, 0);
		m.rotate(Mathf.toRadians(-player.camera.rotation.x), 1, 0, 0);
		chunkShader.loadUniformMatrix4f("u_model", m);

		glDisable(GL_DEPTH_TEST);
		player.curBlock.render();
		glEnable(GL_DEPTH_TEST);

		synchronized (ClientListener.players) {

			for (final PlayerStore store : ClientListener.players.values()) {

				m.translation(store.position);
				chunkShader.loadUniformMatrix4f("u_model", m);

				freeBlock.render();

			}

		}

		final float width = CloneCraftGame.getInstance().getWindow().getWidth();
		final float height = CloneCraftGame.getInstance().getWindow().getHeight();

		if (width > height) crosshair.scale.set(height / width, 1f);
		else crosshair.scale.set(1f, width / height);
		crosshair.scale.mul(0.05f);

		uiRenderer.render();

	}

	public void shutdown() {

		NetworkClient.getNetworkClient().stop();

		ChunkThread.stopThreads();
		chunkShader.shutdown();
		atlas.shutdown();
		uiRenderer.shutdown();
		crosshair.texture.shutdown();

	}

}
