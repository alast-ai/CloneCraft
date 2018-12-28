package cc.antho.clonecraft.client.core;

import static org.lwjgl.glfw.GLFW.*;

import java.util.HashSet;
import java.util.Set;

import org.joml.Vector2f;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

import cc.antho.clonecraft.core.event.EventDispatcher;
import cc.antho.clonecraft.core.events.FramebufferResizeEvent;
import lombok.Getter;

public class Input {

	GLFWFramebufferSizeCallback framebufferSizeCallback;
	GLFWKeyCallback keyCallback;
	GLFWCursorPosCallback cursorPosCallback;
	GLFWMouseButtonCallback mouseButtonCallback;
	GLFWScrollCallback scrollCallback;

	@Getter private final Vector2f lastPos = new Vector2f();
	@Getter private final Vector2f currentPos = new Vector2f();
	@Getter private final Vector2f differecePos = new Vector2f();
	@Getter private final Vector2f scrollBar = new Vector2f();
	private final Window window;

	private final Set<Integer> keys = new HashSet<>();
	private final Set<Integer> keysLast = new HashSet<>();
	private final Set<Integer> buttons = new HashSet<>();

	public Input(final Window window) {

		this.window = window;

		glfwSetFramebufferSizeCallback(window.getHandle(), framebufferSizeCallback = new GLFWFramebufferSizeCallback() {

			public final void invoke(final long window, final int width, final int height) {

				EventDispatcher.dispatch(new FramebufferResizeEvent(this, width, height));

			}

		});

		glfwSetKeyCallback(window.getHandle(), keyCallback = new GLFWKeyCallback() {

			public final void invoke(final long window, final int key, final int scancode, final int action, final int mods) {

				if (action == GLFW_PRESS && !keys.contains(key)) {
					keys.add(key);
					keysLast.add(key);
				} else if (action == GLFW_RELEASE && keys.contains(key)) keys.remove(key);

			}

		});

		glfwSetMouseButtonCallback(window.getHandle(), mouseButtonCallback = new GLFWMouseButtonCallback() {

			public final void invoke(final long window, final int button, final int action, final int mods) {

				if (action == GLFW_PRESS && !buttons.contains(button)) buttons.add(button);
				else if (action == GLFW_RELEASE && buttons.contains(button)) buttons.remove(button);

			}

		});

		glfwSetCursorPosCallback(window.getHandle(), cursorPosCallback = new GLFWCursorPosCallback() {

			public final void invoke(final long window, final double xpos, final double ypos) {

				currentPos.set((float) xpos, (float) ypos);

			}

		});

		glfwSetScrollCallback(window.getHandle(), scrollCallback = new GLFWScrollCallback() {

			public final void invoke(final long window, final double xoffset, final double yoffset) {

				scrollBar.set((float) xoffset, (float) yoffset);

			}

		});

	}

	public void clearLastKeys() {

		keysLast.clear();

	}

	public boolean isKeyPressed(final int key) {

		return keysLast.contains(key);

	}

	public boolean isKeyDown(final int key) {

		return keys.contains(key);

	}

	public boolean isButtonDown(final int button) {

		return buttons.contains(button);

	}

	public void lockCursor() {

		glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_DISABLED);

	}

	public void unlockCursor() {

		glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);

	}

	public void update() {

		clearLastKeys();
		lastPos.set(currentPos);
		glfwPollEvents();
		differecePos.set(currentPos).sub(lastPos);

	}

	public void shutdown() {

		glfwSetFramebufferSizeCallback(window.getHandle(), null);
		glfwSetKeyCallback(window.getHandle(), null);
		glfwSetCursorPosCallback(window.getHandle(), null);
		glfwSetMouseButtonCallback(window.getHandle(), null);
		glfwSetScrollCallback(window.getHandle(), null);

		scrollCallback.free();
		framebufferSizeCallback.free();
		keyCallback.free();
		cursorPosCallback.free();
		mouseButtonCallback.free();

		glfwSetInputMode(window.getHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);

	}

}
