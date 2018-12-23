package cc.antho.clonecraft.core;

import static org.lwjgl.opengl.GL11.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import cc.antho.clonecraft.core.log.Logger;

public final class Config {

	// Config Settings
	public static final int UPS_SUBDIVIDE = 100;
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;
	public static final boolean USE_FULLSCREEN = false;
	public static final boolean USE_VSYNC = false;

	// Clear color
	public static final float CLEAR_RED = .7f;
	public static final float CLEAR_GREEN = .8f;
	public static final float CLEAR_BLUE = .9f;
	public static final float CLEAR_ALPHA = 1f;

	public static final int CLEAR = GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT;

	public static final String ADDRESS = "127.0.0.1";
	public static final int TCP_PORT = 64200;
	public static final int UDP_PORT = 64201;
	public static final float PLAYER_PACKET_FREQUENCY = .2f;

	public static String CROSSHAIR;

	private Config() {

	}

	public static void loadConfig() {

		final File file = new File("./CloneCraft/config.properties");
		if (!file.exists()) {

			Logger.info("Config doesnt exists, creating using default");

			try {

				file.createNewFile();
				final String content = Loader.loadFileIntoString("/config.properties");
				final FileWriter writer = new FileWriter(file);
				writer.write(content);
				writer.close();

			} catch (final IOException e) {

				e.printStackTrace();

			}

		}

		final Properties properties = new Properties();
		try {

			properties.load(Loader.getStream(file.getAbsolutePath()));

		} catch (final IOException e) {

			Logger.info("Config failed to load");
			e.printStackTrace();

		}

		CROSSHAIR = properties.getProperty("crosshair", "core");

	}

}
