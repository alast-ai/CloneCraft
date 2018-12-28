package cc.antho.clonecraft.core.audio;

import static org.lwjgl.openal.AL10.*;

import lombok.Getter;

public class AudioSource {

	@Getter private final int handle;

	public AudioSource() {

		handle = alGenSources();

	}

	public void attachBuffer(final AudioBuffer buffer) {

		alSourcei(handle, AL_BUFFER, buffer.getHandle());

	}

	public void setLooping(final boolean looping) {

		alSourcei(handle, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);

	}

	public void play() {

		alSourcePlay(handle);

	}

	public void stop() {

		alSourceStop(handle);

	}

	public void pause() {

		alSourcePause(handle);

	}

	public void shutdown() {

		alDeleteSources(handle);

	}

}
