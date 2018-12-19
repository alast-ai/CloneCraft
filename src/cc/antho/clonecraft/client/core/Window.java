package cc.antho.clonecraft.client.core;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import cc.antho.clonecraft.core.event.Event;
import cc.antho.clonecraft.core.event.EventDispatcher;
import cc.antho.clonecraft.core.event.EventListener;
import cc.antho.clonecraft.core.events.FramebufferResizeEvent;
import lombok.Getter;

public class Window implements EventListener {

	private static final boolean USE_DEBUG_CALLBACK = false;

	@Getter private final long handle;
	@Getter private final Input input;
	private final Callback debugCallback;

	@Getter private int width, height;

	public Window(final int width, final int height, final boolean fullscreen, final boolean vsync) {

		glfwDefaultWindowHints();
		handle = glfwCreateWindow(this.width = width, this.height = height, "CloneCraft", fullscreen ? glfwGetPrimaryMonitor() : 0, 0);

		if (handle == 0) {

			System.err.println("Failed to create window!");
			glfwTerminate();
			System.exit(0);

		}

		glfwShowWindow(handle);
		glfwMakeContextCurrent(handle);
		GL.createCapabilities();

		glfwSwapInterval(vsync ? 1 : 0);

		input = new Input(this);

		if (USE_DEBUG_CALLBACK) debugCallback = GLUtil.setupDebugMessageCallback();
		else debugCallback = null;

		EventDispatcher.addListener(this);

	}

	public void trigger() {

		EventDispatcher.dispatch(new FramebufferResizeEvent(this, width, height));

	}

	public boolean shouldClose() {

		return glfwWindowShouldClose(handle);

	}

	public void swapBuffers() {

		glfwSwapBuffers(handle);

	}

	public void shutdown() {

		glfwDestroyWindow(handle);
		input.shutdown();
		if (USE_DEBUG_CALLBACK) debugCallback.free();

	}

	public void onEvent(final Event event) {

		if (event instanceof FramebufferResizeEvent) {

			final FramebufferResizeEvent e = (FramebufferResizeEvent) event;

			width = e.getWidth();
			height = e.getHeight();

		}

	}

}
