package cc.antho.clonecraft.client.core;

import static org.lwjgl.glfw.GLFW.*;

import cc.antho.clonecraft.client.Debugger;
import lombok.Getter;

public abstract class GameLoop {

	@Getter private boolean running = false;

	@Getter private final int ups;
	@Getter private double deltaTime = 0, globalTime = 0;

	public GameLoop(final int ups) {

		this.ups = ups;

	}

	public void start() {

		if (running) return;
		running = true;

		run();

	}

	public void stop() {

		running = false;

	}

	private void run() {

		init();

		double currentTime = glfwGetTime();
		double newTime, frameTime;

		double counterTime = glfwGetTime();
		int upsCount = 0;
		int fpsCount = 0;

		while (running) {

			newTime = glfwGetTime();
			frameTime = newTime - currentTime;
			currentTime = newTime;

			input();

			while (frameTime > 0d) {

				deltaTime = Math.min(frameTime, 1d / ups);
				Debugger.Values.deltaTime = deltaTime;
				tick();
				frameTime -= deltaTime;
				globalTime += deltaTime;
				Debugger.Values.globalTime = globalTime;

				upsCount++;

			}

			render();
			fpsCount++;

			if (glfwGetTime() - counterTime >= 1d) {

				counterTime += 1d;

				Debugger.Values.ups = upsCount;
				Debugger.Values.fps = fpsCount;

				upsCount = 0;
				fpsCount = 0;

			}

		}

		shutdown();

	}

	protected abstract void init();

	protected abstract void input();

	protected abstract void tick();

	protected abstract void render();

	protected abstract void shutdown();

}
