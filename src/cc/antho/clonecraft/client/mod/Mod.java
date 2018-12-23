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
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class Mod {

	@Getter private final String name;
	@Getter private final Map<String, BufferedImage> textures = new HashMap<>();

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
			final Vector2i left = textures_mapping.get((p = properties.getProperty("face_left")).contains(".") ? p : this.name + "." + p);
			final Vector2i right = textures_mapping.get((p = properties.getProperty("face_right")).contains(".") ? p : this.name + "." + p);
			final Vector2i front = textures_mapping.get((p = properties.getProperty("face_front")).contains(".") ? p : this.name + "." + p);
			final Vector2i back = textures_mapping.get((p = properties.getProperty("face_back")).contains(".") ? p : this.name + "." + p);
			final Vector2i top = textures_mapping.get((p = properties.getProperty("face_top")).contains(".") ? p : this.name + "." + p);
			final Vector2i bottom = textures_mapping.get((p = properties.getProperty("face_bottom")).contains(".") ? p : this.name + "." + p);
			final boolean breakable = Boolean.parseBoolean(properties.getProperty("breakable"));
			final boolean transparent = Boolean.parseBoolean(properties.getProperty("transparent"));
			final boolean xModel = Boolean.parseBoolean(properties.getProperty("xModel"));
			final boolean inPalette = Boolean.parseBoolean(properties.getProperty("palette"));

			BlockType.registerBlock(name, left, right, front, back, top, bottom, breakable, transparent, xModel, inPalette);

		}

	}

}
