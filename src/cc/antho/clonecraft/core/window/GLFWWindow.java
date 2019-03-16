package cc.antho.clonecraft.core.window;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

import java.io.PrintStream;
import java.nio.IntBuffer;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import cc.antho.clonecraft.core.events.FramebufferResizeEvent;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.commons.event.Event;
import cc.antho.commons.event.EventLayer;
import cc.antho.commons.event.EventListener;
import lombok.Getter;

public class GLFWWindow extends EventListener {

	public static void createPrint(final PrintStream stream) {

		GLFWErrorCallback.createPrint(stream).set();
		Logger.debug("Created GLFW error print stream with " + stream);

	}

	public static void removePrint() {

		glfwSetErrorCallback(null).free();

	}

	public static void init() {

		if (!glfwInit()) throw new IllegalStateException("Unable to initialize GLFW");
		Logger.debug("Initialized GLFW");

	}

	public static void defaultHints() {

		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);

	}

	public static void terminate() {

		glfwTerminate();

	}

	public static void setTime(final double time) {

		glfwSetTime(time);

	}

	public static double getTime() {

		return glfwGetTime();

	}

	public static void pollEvents() {

		glfwPollEvents();

	}

	@Getter private final long handle;
	@Getter private final GLFWInput input;
	@Getter private int width, height;

	public GLFWWindow(final int width, final int height, final String title, final long monitor, final long share) {

		this.width = width;
		this.height = height;

		defaultHints();
		handle = glfwCreateWindow(width, height, title, monitor, share);

		if (handle == NULL) throw new RuntimeException("Failed to create the GLFW window");

		input = new GLFWInput(this);

		centerWindow();

		glfwMakeContextCurrent(handle);
		glfwSwapInterval(0);

		show();

		GL.createCapabilities();

		EventLayer.ROOT.addListener(this);

		Logger.info("Created GLFW window " + this);

	}

	public void show() {

		glfwShowWindow(handle);

	}

	public void hide() {

		glfwHideWindow(handle);

	}

	public void centerWindow() {

		try (MemoryStack stack = stackPush()) {

			final IntBuffer pWidth = stack.mallocInt(1);
			final IntBuffer pHeight = stack.mallocInt(1);

			glfwGetWindowSize(handle, pWidth, pHeight);

			final GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());

			glfwSetWindowPos(handle, (vidmode.width() - pWidth.get(0)) / 2, (vidmode.height() - pHeight.get(0)) / 2);

		}

	}

	public void freeCallbacks() {

		glfwFreeCallbacks(handle);

	}

	public boolean shouldClose() {

		return glfwWindowShouldClose(handle);

	}

	public void swapBuffers() {

		glfwSwapBuffers(handle);

	}

	public void trigger() {

		EventLayer.ROOT.dispatch(new FramebufferResizeEvent(width, height));

	}

	public void destroy() {

		glfwDestroyWindow(handle);

	}

	public void onEvent(final Event event) {

		if (event instanceof FramebufferResizeEvent) {

			final FramebufferResizeEvent e = (FramebufferResizeEvent) event;

			width = e.getWidth();
			height = e.getHeight();

		}

	}

}
