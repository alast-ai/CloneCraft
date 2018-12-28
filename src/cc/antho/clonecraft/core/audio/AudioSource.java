package cc.antho.clonecraft.core.audio;

import static org.lwjgl.openal.AL10.*;

import lombok.Getter;

public class AudioSource {

	@Getter private final int handle;
	@Getter private float gain = 1f;

	public AudioSource() {

		handle = alGenSources();

		AudioManager.add(this);

	}

	public void attachBuffer(final AudioBuffer buffer) {

		alSourcei(handle, AL_BUFFER, buffer.getHandle());

	}

	public void setLooping(final boolean looping) {

		alSourcei(handle, AL_LOOPING, looping ? AL_TRUE : AL_FALSE);

	}

	public void setGain(final float gain) {

		this.gain = gain;
		AudioManager.update(this);

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

		AudioManager.remove(this);

	}

}
