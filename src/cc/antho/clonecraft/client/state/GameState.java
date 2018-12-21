package cc.antho.clonecraft.client.state;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.joml.Matrix4f;
import org.joml.Vector2i;
import org.joml.Vector3f;

import cc.antho.clonecraft.client.ClientListener;
import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.Config;
import cc.antho.clonecraft.client.PlayerStore;
import cc.antho.clonecraft.client.core.Player;
import cc.antho.clonecraft.client.core.Shader;
import cc.antho.clonecraft.client.core.Texture;
import cc.antho.clonecraft.client.pack.PackLoader;
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

	private FreeBlock freeBlock;
	private FreeBlock curBlock;

	public void init() {

		CloneCraftClient.main();

		ChunkThread.lock.lock();
		glfwMakeContextCurrent(CloneCraftGame.getInstance().getWindow().getHandle());

		loadShader();
		loadAtlas();

		freeBlock = new FreeBlock(BlockType.getBlock("core.sand"));
		curBlock = new FreeBlock(BlockType.getBlock("core.sand"));

		world = new World("NFzttn4UxfQD8aOhYyeNDXs3FnXHEioT");

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		CloneCraftGame.getInstance().getWindow().trigger();

		glfwMakeContextCurrent(0);
		ChunkThread.lock.unlock();

		ChunkThread.startThreads();

	}

	private void loadAtlas() {

		try {

			{

				final File modsFolder = new File("./CloneCraft/mods/");
				if (!modsFolder.exists()) throw new RuntimeException("Cannot load game without mod folder");

				final Map<String, BufferedImage> textures = new HashMap<>();

				for (final String mod : modsFolder.list()) {

					System.out.println("Loading textures from mod: " + mod);

					final File modTextureFolder = new File("./CloneCraft/mods/" + mod + "/textures/");
					if (modTextureFolder.exists()) for (final String texturename : modTextureFolder.list()) {
						System.out.println(texturename);
						textures.put(mod + "." + texturename.substring(0, texturename.lastIndexOf('.')), Texture.loadBufferedImage("./CloneCraft/mods/" + mod + "/textures/" + texturename));

					}

				}

				// generate texture atlas

				int size = 0;

				for (final BufferedImage image : textures.values()) {

					if (image.getWidth() > size) size = image.getWidth();
					if (image.getHeight() > size) size = image.getHeight();

				}

				// TODO resize textures

				PackLoader.SIZE = size;
				System.out.println("Largest image is: " + size);
				final int numtexs = textures.values().size();
				System.out.println(numtexs + " textures were loaded");
				final int image_dir_size = Mathf.ceil(Mathf.sqrt(numtexs));

				final BufferedImage atlas = new BufferedImage(image_dir_size * size, image_dir_size * size, BufferedImage.TYPE_INT_ARGB);
				final Graphics2D g = (Graphics2D) atlas.getGraphics();

				final List<String> textures_keys = new ArrayList<>(textures.keySet());
				final List<BufferedImage> textures_values = new ArrayList<>(textures.values());

				final Map<String, Vector2i> textures_mapping = new HashMap<>();

				for (int y = 0; y < image_dir_size; y++)
					for (int x = 0; x < image_dir_size; x++) {

						final int index = x + y * image_dir_size;

						if (index >= textures.keySet().size()) break;

						textures_mapping.put(textures_keys.get(index), new Vector2i(x, image_dir_size - y - 1));

						g.drawImage(textures_values.get(index), x * size, y * size, size, size, null);

					}

				g.dispose();

				PackLoader.set(image_dir_size);
				this.atlas = new Texture(atlas, true);

				for (final String mod : modsFolder.list()) {

					System.out.println("Loading blocks from mod: " + mod);

					final File modBlockFolder = new File("./CloneCraft/mods/" + mod + "/blocks/");
					if (modBlockFolder.exists()) for (final String blockname : modBlockFolder.list()) {

						System.out.println(blockname);

						final FileInputStream fis = new FileInputStream(new File("./CloneCraft/mods/" + mod + "/blocks/" + blockname));
						final Properties properties = new Properties();
						properties.load(fis);
						fis.close();

						String p;
						final String name = mod + "." + blockname.substring(0, blockname.lastIndexOf('.'));
						final Vector2i left = textures_mapping.get((p = properties.getProperty("face_left")).contains(".") ? p : mod + "." + p);
						final Vector2i right = textures_mapping.get((p = properties.getProperty("face_right")).contains(".") ? p : mod + "." + p);
						final Vector2i front = textures_mapping.get((p = properties.getProperty("face_front")).contains(".") ? p : mod + "." + p);
						final Vector2i back = textures_mapping.get((p = properties.getProperty("face_back")).contains(".") ? p : mod + "." + p);
						final Vector2i top = textures_mapping.get((p = properties.getProperty("face_top")).contains(".") ? p : mod + "." + p);
						final Vector2i bottom = textures_mapping.get((p = properties.getProperty("face_bottom")).contains(".") ? p : mod + "." + p);
						final boolean breakable = Boolean.parseBoolean(properties.getProperty("breakable"));
						final boolean transparent = Boolean.parseBoolean(properties.getProperty("transparent"));
						final boolean xModel = Boolean.parseBoolean(properties.getProperty("xModel"));

						BlockType.registerBlock(name, left, right, front, back, top, bottom, breakable, transparent, xModel);

					}

				}

			}

			crosshair = Texture.create("/textures/crosshair.png", false);

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	private void loadShader() {

		try {

			String vertexShader = Shader.loadShaderString("/shaders/chunk_vertex.glsl");
			String fragmentShader = Shader.loadShaderString("/shaders/chunk_fragment.glsl");

			chunkShader = new Shader(vertexShader, fragmentShader);

			vertexShader = Shader.loadShaderString("/shaders/ui_vertex.glsl");
			fragmentShader = Shader.loadShaderString("/shaders/ui_fragment.glsl");

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

		curBlock.shutdown();
		curBlock = new FreeBlock(BlockType.getBlocks().get(player.blockIndex));

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
		curBlock.render();

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
