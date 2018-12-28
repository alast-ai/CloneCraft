package cc.antho.clonecraft.core.audio;

import static org.lwjgl.openal.ALC10.*;

import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;
import org.lwjgl.openal.ALCapabilities;

import cc.antho.clonecraft.core.log.Logger;

public final class AudioManager {

	private static long device, context;

	private AudioManager() {

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
