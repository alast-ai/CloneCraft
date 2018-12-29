package cc.antho.clonecraft.client;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.io.IOException;

import org.joml.Vector3f;

import cc.antho.clonecraft.client.core.Camera;
import cc.antho.clonecraft.client.core.Player;
import cc.antho.clonecraft.client.core.Shader;
import cc.antho.clonecraft.client.core.Texture2D;
import cc.antho.clonecraft.client.net.ClientListener;
import cc.antho.clonecraft.client.ui.UIRenderer;
import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.client.world.FreeBlock;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.TMP;
import cc.antho.clonecraft.core.event.Event;
import cc.antho.clonecraft.core.event.EventDispatcher;
import cc.antho.clonecraft.core.event.EventListener;
import cc.antho.clonecraft.core.events.FramebufferResizeEvent;
import cc.antho.clonecraft.core.math.Mathf;
import lombok.Getter;

public class Renderer implements EventListener {

	@Getter private UIRenderer uiRenderer;
	@Getter private Shader chunkShader;
	private FreeBlock freeBlock;

	public void init() {

		EventDispatcher.addListener(this);

		uiRenderer = new UIRenderer();

		try {

			final String vertexShader = Shader.loadShaderString("/shaders/chunk_vertex.glsl");
			final String fragmentShader = Shader.loadShaderString("/shaders/chunk_fragment.glsl");

			chunkShader = new Shader(vertexShader, fragmentShader);

		} catch (final IOException e) {

			e.printStackTrace();

		}

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glEnable(GL_CULL_FACE);
		glEnable(GL_DITHER);

		glFrontFace(GL_CCW);
		glCullFace(GL_BACK);

		glDisable(GL_MULTISAMPLE);

		glClearColor(.7f, .8f, .9f, 1f);

	}

	public void loadFreeBlock(final String block) {

		freeBlock = new FreeBlock(BlockType.getBlock(block));

	}

	public void shutdown() {

		uiRenderer.shutdown();
		chunkShader.shutdown();

		if (freeBlock != null) freeBlock.shutdown();

	}

	public void onEvent(final Event event) {

		if (event instanceof FramebufferResizeEvent) {

			final FramebufferResizeEvent e = (FramebufferResizeEvent) event;
			glViewport(0, 0, e.getWidth(), e.getHeight());

		}

	}

	public void render() {

		glClear(Config.CLEAR);
		uiRenderer.render();

	}

	public void render(final Camera camera, final Player player, final Texture2D atlas, final World world, final Vector3f handPos) {

		glClear(Config.CLEAR);
		atlas.bind(0);

		if (camera != null) {

			chunkShader.bind();
			chunkShader.loadUniformMatrix4f("u_view", camera.getView());

			if (camera.isProjectionDirty()) {

				chunkShader.loadUniformMatrix4f("u_projection", camera.getProjection());
				camera.setProjectionDirty(false);

			}

		}

		if (world != null) {

			TMP.m40.identity();
			chunkShader.bind();
			chunkShader.loadUniformMatrix4f("u_model", TMP.m40);
			world.render();

		}

		if (player != null) {

			if (player.curDirty) {

				if (player.curBlock != null) player.curBlock.shutdown();

				player.curBlock = new FreeBlock(BlockType.getPalette().get(player.blockIndex));
				player.curDirty = false;

			}

			TMP.m40.translation(handPos);
			TMP.m40.scale(.4f);
			TMP.m40.rotate(Mathf.toRadians(-player.camera.rotation.z), 0, 0, 1);
			TMP.m40.rotate(Mathf.toRadians(-player.camera.rotation.y), 0, 1, 0);
			TMP.m40.rotate(Mathf.toRadians(-player.camera.rotation.x), 1, 0, 0);
			chunkShader.loadUniformMatrix4f("u_model", TMP.m40);

			glDisable(GL_DEPTH_TEST);
			player.curBlock.render();
			glEnable(GL_DEPTH_TEST);

		}

		synchronized (ClientListener.players) {

			for (final PlayerStore store : ClientListener.players.values()) {

				TMP.m40.translation(store.position);
				chunkShader.loadUniformMatrix4f("u_model", TMP.m40);

				freeBlock.render();

			}

		}

		uiRenderer.render();

	}

}
