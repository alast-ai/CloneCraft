package cc.antho.clonecraft.client.pack;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import cc.antho.clonecraft.client.CloneCraftGame;

public class PackLoader {

	public static final int WIDTH = 5;
	public static final int HEIGHT = 4;
	public static final float WIDTH_SCALE = 1f / WIDTH;
	public static final float HEIGHT_SCALE = 1f / HEIGHT;
	public static final String PACK_FILE = "/pack.properties";

	public static Pack pack;

	public static final void load() {

		final InputStream stream = CloneCraftGame.class.getResourceAsStream(PACK_FILE);

		final Properties properties = new Properties();

		try {

			properties.load(stream);

			pack = new Pack(Integer.parseInt(properties.getProperty("size")));

		} catch (final IOException e) {

			e.printStackTrace();

		} finally {

			try {

				stream.close();

			} catch (final IOException e) {

				e.printStackTrace();

			}

		}

	}

}
