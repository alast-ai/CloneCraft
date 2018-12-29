package cc.antho.clonecraft.client.state;

import static org.lwjgl.opengl.GL11.*;

import java.awt.image.BufferedImage;
import java.io.IOException;

import org.joml.Vector3f;

import cc.antho.clonecraft.client.ContextManager;
import cc.antho.clonecraft.client.Game;
import cc.antho.clonecraft.client.core.Player;
import cc.antho.clonecraft.client.core.Texture2D;
import cc.antho.clonecraft.client.net.NetworkClient;
import cc.antho.clonecraft.client.ui.UITexture;
import cc.antho.clonecraft.client.world.ChunkSection;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.client.world.thread.ChunkThread;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.TMP;
import cc.antho.clonecraft.core.audio.AudioBuffer;
import cc.antho.clonecraft.core.audio.AudioSource;
import cc.antho.clonecraft.core.math.Mathf;
import cc.antho.clonecraft.core.mod.ModLoader;
import cc.antho.clonecraft.core.state.State;
import lombok.Getter;

public class GameState extends State {

	private final Player player = new Player();

	@Getter private World world;
	private Texture2D atlas;

	private UITexture crosshair;

	private AudioBuffer audioBuffer;
	private AudioSource audioSource;

	private final Vector3f handPosition = new Vector3f();

	public void init() {

		audioBuffer = new AudioBuffer("/audio/charms.ogg");
		audioSource = new AudioSource();

		audioSource.attachBuffer(audioBuffer);
		audioSource.setLooping(true);
		audioSource.play();

		Game.getWindow().getInput().lockCursor();

		ContextManager.lock();

		loadAtlas();

		Game.getRenderer().getUiRenderer().addElement(crosshair);
		Game.getRenderer().loadFreeBlock("core.sand");

		world = new World("NFzttn4UxfQD8aOhYyeNDXs3FnXHEioT");

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		Game.getWindow().trigger();

		ContextManager.unlock();

		ChunkThread.startThreads();

		NetworkClient.startNetworkClient();

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

	public void tick() {

		player.tick(world);

		ChunkThread.playerPostionChunk.x = Mathf.floor(player.getPosition().x / ChunkSection.SIZE);
		ChunkThread.playerPostionChunk.y = Mathf.floor(player.getPosition().z / ChunkSection.SIZE);

		world.addNewChunks((int) ChunkThread.playerPostionChunk.x, (int) ChunkThread.playerPostionChunk.y);

	}

	public void render() {

		final float width = Game.getWindow().getWidth();
		final float height = Game.getWindow().getHeight();

		if (width > height) crosshair.scale.set(height / width, 1f);
		else crosshair.scale.set(1f, width / height);
		crosshair.scale.mul(0.05f);

		final float offsetX = .3f;
		final float offsetY = -.6f;
		final float offsetZ = .7f;

		handPosition.set(0f);
		handPosition.add(TMP.v30.set(player.camera.forward).mul(offsetZ));
		handPosition.add(TMP.v31.set(player.camera.right).mul(offsetX));
		handPosition.add(TMP.v32.set(player.camera.up).mul(offsetY));
		handPosition.add(player.camera.position);

		Game.getRenderer().render(player.camera, player, atlas, world, handPosition);

	}

	public void shutdown() {

		ChunkThread.stopThreads();

		Game.getRenderer().getUiRenderer().removeElement(crosshair);

		audioSource.stop();
		audioBuffer.shutdown();
		audioSource.shutdown();

		NetworkClient.getNetworkClient().stop();

		atlas.shutdown();
		crosshair.texture.shutdown();

	}

}
