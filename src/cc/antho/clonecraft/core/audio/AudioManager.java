package cc.antho.clonecraft.core.audio;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import cc.antho.clonecraft.core.log.Logger;

public final class AudioManager {

	private static float gain = 1f;

	private static long device, context;

	private static List<AudioBuffer> buffers = new ArrayList<>();
	private static List<AudioSource> sources = new ArrayList<>();

	private AudioManager() {

	}

	static final void add(final AudioBuffer buffer) {

		buffers.add(buffer);

	}

	static final void add(final AudioSource source) {

		sources.add(source);
		update(source);

	}

	static void update(final AudioSource source) {

		alSourcef(source.getHandle(), AL_GAIN, gain * source.getGain());

	}

	static final void remove(final AudioBuffer buffer) {

		buffers.remove(buffer);

	}

	static final void remove(final AudioSource source) {

		sources.remove(source);

	}

	public static final void setGain(final float p) {

		gain = p;

		for (int i = 0; i < sources.size(); i++)
			update(sources.get(i));

	}

	public static final float getGain() {

		return gain;

	}

	public static final void init() {

		final String defaultDeviceName = alcGetString(0, ALC_DEFAULT_DEVICE_SPECIFIER);
		device = alcOpenDevice(defaultDeviceName);

		final int[] attributes = { 0 };
		context = alcCreateContext(device, attributes);
		alcMakeContextCurrent(context);

		final ALCCapabilities alcCapabilities = ALC.createCapabilities(device);
		final ALCapabilities alCapabilities = AL.createCapabilities(alcCapabilities);

		if (!alCapabilities.OpenAL10) Logger.error("Yeah audio wont work sooo :/");

	}

	public static final void shutdown() {

		alcDestroyContext(context);
		alcCloseDevice(device);

	}

}
