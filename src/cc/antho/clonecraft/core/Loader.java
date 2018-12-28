package cc.antho.clonecraft.core;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.imageio.ImageIO;

public final class Loader {

	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String FILE_SEP = File.separator;
	public static final String GAME_DIR = USER_DIR + FILE_SEP + "CloneCraft" + FILE_SEP;

	public static final byte[] loadByteArray(final InputStream stream) throws IOException {

		final ByteArrayOutputStream os = new ByteArrayOutputStream();

		final byte[] tmp = new byte[1024];
		int len;

		while ((len = stream.read(tmp)) != -1)
			os.write(tmp, 0, len);

		return os.toByteArray();

	}

	public static final InputStream getStream(final String input, final boolean relative) throws FileNotFoundException {

		if (relative) return Loader.class.getResourceAsStream(input);
		return new FileInputStream(input);

	}

	public static final BufferedImage loadBufferedImage(final InputStream stream) throws IOException {

		final BufferedImage image = ImageIO.read(stream);
		stream.close();
		image.flush();

		return image;

	}

	public static final String loadString(final InputStream stream) throws IOException {

		final InputStreamReader isr = new InputStreamReader(stream);
		final BufferedReader br = new BufferedReader(isr);

		String line;
		final StringBuilder builder = new StringBuilder();

		while (true) {

			line = br.readLine();
			if (line == null) break;
			builder.append(line + "\n");

		}

		final String contents = builder.toString();

		br.close();
		isr.close();
		stream.close();

		return contents;

	}

}
