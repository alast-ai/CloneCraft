package cc.antho.clonecraft.client.mod;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.joml.Vector2i;

import cc.antho.clonecraft.client.core.Texture;
import cc.antho.clonecraft.client.world.BlockType;
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

				textures.put(texturename.substring(0, texturename.lastIndexOf('.')), Texture.loadBufferedImage("./CloneCraft/mods/" + name + "/textures/" + texturename));

			} catch (final IOException e) {

				Logger.debug("Failed to load image: " + texturename);

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

			crosshair = Texture.loadBufferedImage(crosshairfile.getAbsolutePath());

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
