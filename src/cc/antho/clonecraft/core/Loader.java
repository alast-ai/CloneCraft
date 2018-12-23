package cc.antho.clonecraft.core;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class Loader {

	public static final InputStream getStream(final String input) throws FileNotFoundException {

		if (input.startsWith("/")) return Loader.class.getResourceAsStream(input);
		return new FileInputStream(input);

	}

	public static final String loadFileIntoString(final String file) throws IOException {

		final InputStream stream = getStream(file);
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
