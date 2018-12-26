package cc.antho.clonecraft.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

import cc.antho.clonecraft.client.core.GameLoop;
import cc.antho.clonecraft.client.core.Input;
import cc.antho.clonecraft.client.core.Window;
import cc.antho.clonecraft.client.state.SplashState;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.clonecraft.core.state.StateManager;
import lombok.Getter;

public final class CloneCraftGame extends GameLoop {

	@Getter private static Thread thread;
	@Getter private static CloneCraftGame instance;

	@Getter private Window window;
	@Getter private final StateManager manager = new StateManager();

	public static Input getInput() {

		return getInstance().getWindow().getInput();

	}

	private CloneCraftGame() {

		super(Config.UPS_SUBDIVIDE);

	}

	public static final void main(final boolean openDebugger) {

		NetworkClient.main();

		thread = new Thread(() -> {

			if (openDebugger) Debugger.start();
			instance = new CloneCraftGame();
			instance.start();

		}, "CloneCraftGame");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

	protected void init() {

		if (!glfwInit()) new IllegalStateException("GLFW Failed to initialize").printStackTrace();
		Logger.debug("Initialized glfw");

		window = new Window(Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT, Config.USE_FULLSCREEN, Config.USE_VSYNC);
		Logger.info("Created window");

		manager.setState(new SplashState());

		Logger.info(glGetString(GL_VERSION));

		glClearColor(Config.CLEAR_RED, Config.CLEAR_GREEN, Config.CLEAR_BLUE, Config.CLEAR_ALPHA);
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

	public long currentTime, totalTime;

	protected void render() {

		ContextManager.lock();

		final long m = System.currentTimeMillis();
		glfwMakeContextCurrent(window.getHandle());

		manager.render();
		window.swapBuffers();

		glfwMakeContextCurrent(0);

		currentTime = System.currentTimeMillis() - m;
		totalTime += currentTime;

		ContextManager.unlock();

	}

	protected void shutdown() {

		manager.setState(null);
		window.shutdown();
		GL.destroy();
		glfwTerminate();

	}

}
