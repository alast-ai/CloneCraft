package cc.antho.clonecraft.core;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class Loader {

	public static final String USER_DIR = System.getProperty("user.dir");
	public static final String FILE_SEP = File.separator;
	public static final String GAME_DIR = USER_DIR + FILE_SEP + "CloneCraft" + FILE_SEP;

	public static final InputStream getStream(final String input, final boolean relative) throws FileNotFoundException {

		if (relative) return Loader.class.getResourceAsStream(input);
		return new FileInputStream(input);

	}

	public static final String loadFileIntoString(final String file, final boolean relative) throws IOException {

		final InputStream stream = getStream(file, relative);
		final InputStreamReader isr = new InputStreamReader(stream);
		final BufferedReader br = new BufferedReader(isr);

		String line;
		final StringBuilder builder = new StringBuilder();
		for (;;) {

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
