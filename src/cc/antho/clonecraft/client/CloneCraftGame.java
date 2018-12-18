package cc.antho.clonecraft.client;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL;

import cc.antho.clonecraft.client.core.GameLoop;
import cc.antho.clonecraft.client.core.Input;
import cc.antho.clonecraft.client.core.Window;
import cc.antho.clonecraft.client.pack.PackLoader;
import cc.antho.clonecraft.client.world.thread.ChunkThread;
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

	public static final void main() {

		thread = new Thread(() -> {

			instance = new CloneCraftGame();
			instance.start();

		}, "CloneCraftGame");

		thread.setPriority(Thread.MAX_PRIORITY);
		thread.start();

	}

	protected void init() {

		PackLoader.load();

		if (!glfwInit()) new IllegalStateException("GLFW Failed to initialize").printStackTrace();

		window = new Window(Config.DEFAULT_WIDTH, Config.DEFAULT_HEIGHT, Config.USE_FULLSCREEN, Config.USE_VSYNC);

		manager.setState(new GameState());

		System.out.println(glGetString(GL_VERSION));

		glClearColor(Config.CLEAR_RED, Config.CLEAR_GREEN, Config.CLEAR_BLUE, Config.CLEAR_ALPHA);
		glfwMakeContextCurrent(0);

		Debugger.start();

	}

	protected void input() {

		window.getInput().update();

		if (window.shouldClose()) stop();

	}

	protected void tick() {

		manager.tick();

	}

	public long currentTime, totalTime;

	protected void render() {

		ChunkThread.lock.lock();

		final long m = System.currentTimeMillis();
		glfwMakeContextCurrent(window.getHandle());

		manager.render();
		window.swapBuffers();

		glfwMakeContextCurrent(0);

		currentTime = System.currentTimeMillis() - m;
		totalTime += currentTime;

		ChunkThread.lock.unlock();

	}

	protected void shutdown() {

		manager.setState(null);
		window.shutdown();
		GL.destroy();
		glfwTerminate();

	}

}
