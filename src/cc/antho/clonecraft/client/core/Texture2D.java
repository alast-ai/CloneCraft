package cc.antho.clonecraft.client.core;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL30.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import cc.antho.clonecraft.client.Game;
import cc.antho.clonecraft.client.PackLoader;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.clonecraft.core.math.Mathf;
import lombok.Getter;

public class Texture2D {

	@Getter private final int handle;
	@Getter private final int width, height;

	public static final Texture2D create(final String file, final boolean atlas) throws IOException {

		final InputStream is = Game.class.getResourceAsStream(file);
		final BufferedImage image = ImageIO.read(is);
		final Texture2D texture = new Texture2D(image, atlas);
		is.close();
		image.flush();

		Logger.debug("Created texture " + file);

		return texture;

	}

	public Texture2D(final BufferedImage image, final boolean atlas) throws IOException {

		width = image.getWidth();
		height = image.getHeight();

		final ByteBuffer buffer = BufferUtils.createByteBuffer(image.getWidth() * image.getHeight() * 4);

		for (int y = image.getHeight() - 1; y >= 0; y--)
			for (int x = 0; x < image.getWidth(); x++) {
				final int pixel = image.getRGB(x, y);
				final int a = (pixel & 0xFF000000) >> 24;
				final int r = (pixel & 0x00FF0000) >> 16;
				final int g = (pixel & 0x0000FF00) >> 8;
				final int b = pixel & 0x000000FF;
				buffer.put((byte) r);
				buffer.put((byte) g);
				buffer.put((byte) b);
				buffer.put((byte) a);
			}

		buffer.flip();

		handle = glGenTextures();

		bind(0);

		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA8, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, buffer);
		if (atlas) glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, (int) Mathf.log(PackLoader.SIZE, 2));
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
		glGenerateMipmap(GL_TEXTURE_2D);

	}

	public void bind(final int index) {

		glActiveTexture(GL_TEXTURE0 + index);
		glBindTexture(GL_TEXTURE_2D, handle);

	}

	public void unbind(final int index) {

		glActiveTexture(GL_TEXTURE0 + index);
		glBindTexture(GL_TEXTURE_2D, 0);

	}

	public void shutdown() {

		glDeleteTextures(handle);

	}

}
