package cc.antho.clonecraft.core.mod;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2i;

import cc.antho.clonecraft.client.PackLoader;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.clonecraft.core.math.Mathf;

public class ModLoader {

	private static final String MOD_FOLDER = "./CloneCraft/mods/";

	private final List<Mod> mods = new ArrayList<>();
	private final Map<String, Vector2i> textures_mapping = new HashMap<>();

	public Mod getMod(final String name) {

		for (final Mod mod : mods)
			if (mod.getName().equals(name)) return mod;

		return null;

	}

	public BufferedImage loadAll() throws IOException {

		final File modsFolder = new File(MOD_FOLDER);
		if (!modsFolder.exists()) throw new RuntimeException("Cannot load game without mod folder");

		Logger.debug("Discovering mods...");
		for (final String mod : modsFolder.list()) {

			Logger.debug("Found mod " + mod);
			mods.add(new Mod(mod));

		}

		Logger.debug("Loading crosshairs for mods...");
		for (final Mod mod : mods)
			mod.loadCrosshair();

		Logger.debug("Loading images for mods...");
		for (final Mod mod : mods)
			mod.loadTextures();

		Logger.debug("Loading modifiers for mods...");
		for (final Mod mod : mods)
			mod.loadModifiers();

		Logger.debug("Creating texture atlas...");
		final BufferedImage atlas = createAtlas();

		Logger.debug("Loading blocks for mods...");
		for (final Mod mod : mods)
			mod.loadBlocks(textures_mapping);

		return atlas;

	}

	private BufferedImage createAtlas() {

		Logger.debug("Discovering max size...");
		int maxsize = 0;
		int count = 0;

		for (final Mod mod : mods)
			for (final BufferedImage image : mod.getTextures().values()) {

				if (image.getWidth() > maxsize) maxsize = image.getWidth();
				if (image.getHeight() > maxsize) maxsize = image.getHeight();
				count++;

			}

		PackLoader.SIZE = maxsize;
		Logger.debug("Max texture size is: " + maxsize);
		Logger.debug(count + " images exist");

		final int textureImageSize = Mathf.ceil(Mathf.sqrt(count));
		final int imageSize = textureImageSize * maxsize;
		Logger.debug("Atlas size is: " + imageSize + "x" + imageSize);

		final BufferedImage atlas = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = (Graphics2D) atlas.getGraphics();

		final List<String> textures_keys = new ArrayList<>();
		final List<BufferedImage> textures_values = new ArrayList<>();

		for (final Mod mod : mods)
			for (final String key : mod.getTextures().keySet()) {

				final String globalKey = mod.getName() + "." + key;
				final BufferedImage image = mod.getTextures().get(key);

				textures_keys.add(globalKey);
				textures_values.add(image);

			}

		exit: for (int y = 0; y < textureImageSize; y++)
			for (int x = 0; x < textureImageSize; x++) {

				final int index = x + y * textureImageSize;

				if (index >= textures_keys.size()) break exit;

				textures_mapping.put(textures_keys.get(index), new Vector2i(x, textureImageSize - y - 1));

				g.drawImage(textures_values.get(index), x * maxsize, y * maxsize, maxsize, maxsize, null);

			}

		g.dispose();

		PackLoader.set(textureImageSize);

		return atlas;

	}

}
