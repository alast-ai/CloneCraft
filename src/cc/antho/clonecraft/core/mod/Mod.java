package cc.antho.clonecraft.core.mod;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.joml.Vector2i;

import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.core.Loader;
import cc.antho.clonecraft.core.log.Logger;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Mod {

	@Getter private final String name;
	@Getter private final Map<String, BufferedImage> textures = new HashMap<>();
	@Getter private BufferedImage crosshair;

	public void loadTextures() {

		Logger.debug("Loading textures for mod: " + name);

		final File modTextureFolder = new File("./CloneCraft/mods/" + name + "/textures/");
		if (!modTextureFolder.exists()) {

			Logger.debug("No texture directory for " + name + " exist");
			return;

		}

		for (final String texturename : modTextureFolder.list()) {

			Logger.debug("Loading image: " + texturename);

			try {

				textures.put(texturename.substring(0, texturename.lastIndexOf('.')), Loader.loadBufferedImage(Loader.GAME_DIR + "mods/" + name + "/textures/" + texturename, false));

			} catch (final IOException e) {

				Logger.debug("Failed to load image: " + texturename);

			}

		}

	}

	public void loadModifiers() {

		Logger.debug("Loading modifiers for mod: " + name);
		final File file = new File("./CloneCraft/mods/" + name + "/modifiers/");

		if (!file.exists()) {

			Logger.debug("No modifiers found");
			return;

		}

		for (final String name : file.list()) {

			final BufferedImage image = textures.get(name);

			try {

				for (final String line : Loader.loadString(Loader.GAME_DIR + "mods/" + this.name + "/modifiers/" + name, false).split("\n")) {

					final String[] tokens = line.split(" ");

					if (tokens[0].equals("mul")) for (int y = 0; y < image.getHeight(); y++)
						for (int x = 0; x < image.getWidth(); x++) {

							final int p = image.getRGB(x, y);
							final int a = (int) (((p & 0xFF000000) >> 24) * Float.parseFloat(tokens[4]));
							final int r = (int) (((p & 0x00FF0000) >> 16) * Float.parseFloat(tokens[1]));
							final int g = (int) (((p & 0x0000FF00) >> 8) * Float.parseFloat(tokens[2]));
							final int b = (int) ((p & 0x000000FF) * Float.parseFloat(tokens[3]));

							image.setRGB(x, y, a << 24 | r << 16 | g << 8 | b);

						}
					else if (tokens[0].equals("underlay")) {

						final BufferedImage l0 = textures.get(tokens[1]);
						final BufferedImage l1 = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
						Graphics g = l1.getGraphics();
						g.drawImage(image, 0, 0, null);
						g.dispose();
						g = image.getGraphics();
						g.drawImage(l0, 0, 0, null);
						g.drawImage(l1, 0, 0, null);
						g.dispose();

					}

				}

			} catch (final IOException e) {

				e.printStackTrace();

			}

		}

	}

	public void loadCrosshair() {

		Logger.debug("Loading crosshair for mod: " + name);
		final File crosshairfile = new File("./CloneCraft/mods/" + name + "/crosshair.png");

		if (!crosshairfile.exists()) {

			Logger.debug("Crosshair not found");
			return;

		}

		try {

			crosshair = Loader.loadBufferedImage(crosshairfile.getAbsolutePath(), false);

		} catch (final IOException e) {

			Logger.debug("Crosshair failed to load");

		}

	}

	public void loadBlocks(final Map<String, Vector2i> textures_mapping) {

		Logger.debug("Loading blocks for mod: " + name);

		final File modBlockFolder = new File("./CloneCraft/mods/" + name + "/blocks/");

		if (!modBlockFolder.exists()) Logger.debug("No blocks in mod " + name + " exist");

		for (final String blockname : modBlockFolder.list()) {

			Logger.debug("Loading block: " + blockname);

			final Properties properties = new Properties();

			try {

				final FileInputStream fis = new FileInputStream(new File("./CloneCraft/mods/" + name + "/blocks/" + blockname));
				properties.load(fis);
				fis.close();

			} catch (final IOException e) {

				Logger.debug("Failed to load block " + blockname);
				continue;

			}

			String p;
			final String name = this.name + "." + blockname.substring(0, blockname.lastIndexOf('.'));
			final Vector2i left = textures_mapping.get((p = properties.getProperty("face_left", "core.unknown")).contains(".") ? p : this.name + "." + p);
			final Vector2i right = textures_mapping.get((p = properties.getProperty("face_right", "core.unknown")).contains(".") ? p : this.name + "." + p);
			final Vector2i front = textures_mapping.get((p = properties.getProperty("face_front", "core.unknown")).contains(".") ? p : this.name + "." + p);
			final Vector2i back = textures_mapping.get((p = properties.getProperty("face_back", "core.unknown")).contains(".") ? p : this.name + "." + p);
			final Vector2i top = textures_mapping.get((p = properties.getProperty("face_top", "core.unknown")).contains(".") ? p : this.name + "." + p);
			final Vector2i bottom = textures_mapping.get((p = properties.getProperty("face_bottom", "core.unknown")).contains(".") ? p : this.name + "." + p);
			final boolean breakable = Boolean.parseBoolean(properties.getProperty("breakable", "true"));
			final boolean transparent = Boolean.parseBoolean(properties.getProperty("transparent", "false"));
			final boolean xModel = Boolean.parseBoolean(properties.getProperty("xModel", "false"));
			final boolean inPalette = Boolean.parseBoolean(properties.getProperty("palette", "true"));

			BlockType.registerBlock(name, left, right, front, back, top, bottom, breakable, transparent, xModel, inPalette);

		}

	}

}
