package cc.antho.clonecraft.core.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.stb.STBVorbis.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.libc.LibCStdlib.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;

import org.lwjgl.BufferUtils;

import cc.antho.clonecraft.core.Loader;
import lombok.Getter;

public class AudioBuffer {

	@Getter private final int handle;

	public AudioBuffer(final String file) {

		stackPush();
		final IntBuffer channelsBuffer = stackMallocInt(1);
		stackPush();
		final IntBuffer sampleRateBuffer = stackMallocInt(1);

		ByteBuffer buffer = null;

		try {

			final InputStream stream = Loader.getStream(file, true);
			final byte[] data = Loader.loadByteArray(stream);

			buffer = BufferUtils.createByteBuffer(data.length);
			buffer.put(data);
			buffer.flip();

		} catch (final IOException e) {

			e.printStackTrace();

		}

		final ShortBuffer rawAudioBuffer = stb_vorbis_decode_memory(buffer, channelsBuffer, sampleRateBuffer);

		final int channels = channelsBuffer.get();
		final int sampleRate = sampleRateBuffer.get();

		stackPop();
		stackPop();

		int format = -1;
		if (channels == 1) format = AL_FORMAT_MONO16;
		else if (channels == 2) format = AL_FORMAT_STEREO16;

		handle = alGenBuffers();

		alBufferData(handle, format, rawAudioBuffer, sampleRate);

		free(rawAudioBuffer);

		AudioManager.add(this);

	}

	public void shutdown() {

		alDeleteBuffers(handle);

		AudioManager.remove(this);

	}

}
