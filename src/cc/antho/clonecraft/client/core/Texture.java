package cc.antho.clonecraft.client.core;

import static org.lwjgl.opengl.GL46.*;
import static org.lwjgl.opengl.GL11.GL_NEAREST_MIPMAP_LINEAR;
import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_RGBA8;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_S;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_WRAP_T;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;
import static org.lwjgl.opengl.GL11.glTexParameteri;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL12.GL_TEXTURE_MAX_LEVEL;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;

import cc.antho.ascl8.math.Mathf;
import cc.antho.clonecraft.client.CloneCraft;
import cc.antho.clonecraft.client.pack.PackLoader;
import lombok.Getter;

public class Texture {

	@Getter private final int handle;
	@Getter private final int width, height;

	public Texture(final String file, final boolean atlas) throws IOException {

		final InputStream is = CloneCraft.class.getResourceAsStream(file);
		final BufferedImage image = ImageIO.read(is);
		is.close();
		image.flush();

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
		if (atlas) glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAX_LEVEL, (int) Mathf.log(PackLoader.pack.getSize(), 2));
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
