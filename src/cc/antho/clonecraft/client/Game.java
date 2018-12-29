package cc.antho.clonecraft.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.Version;

import cc.antho.clonecraft.client.core.GameLoop;
import cc.antho.clonecraft.client.state.SplashState;
import cc.antho.clonecraft.core.audio.AudioManager;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.clonecraft.core.state.StateManager;
import cc.antho.clonecraft.core.window.GLFWWindow;
import lombok.Getter;

public final class Game extends GameLoop {

	@Getter private static Game instance;
	@Getter private static Thread thread;
	@Getter private static GLFWWindow window;
	@Getter private static final StateManager manager = new StateManager();
	@Getter private static Renderer renderer;

	private Game() {

		super(100);

	}

	public static final void startThread() {

		thread = new Thread(() -> {

			instance = new Game();
			instance.start();

		}, "CloneCraftGame");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

	protected void init() {

		GLFWWindow.createPrint(System.err);
		GLFWWindow.init();
		window = new GLFWWindow(1280, 720, "CloneCraft", 0l, 0l);

		Logger.info(Version.getVersion());
		Logger.info(glGetString(GL_VERSION));

		AudioManager.init();

		renderer = new Renderer();
		renderer.init();
		window.trigger();

		manager.setState(new SplashState());

		glfwMakeContextCurrent(0);

	}

	protected void input() {

		window.getInput().update();

		if (window.shouldClose()) stop();

		manager.updateStates();

	}

	protected void tick() {

		manager.tick();

	}

	protected void render() {

		ContextManager.lock();

		manager.render();
		window.swapBuffers();

		ContextManager.unlock();

	}

	protected void shutdown() {

		manager.setState(null);
		manager.updateStates();

		renderer.shutdown();

		AudioManager.shutdown();

		window.freeCallbacks();
		window.destroy();
		GLFWWindow.terminate();
		GLFWWindow.removePrint();

	}

}
