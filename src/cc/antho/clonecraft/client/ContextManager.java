package cc.antho.clonecraft.client;

import static org.lwjgl.glfw.GLFW.*;

import java.util.concurrent.locks.ReentrantLock;

public class ContextManager {

	private static final ReentrantLock lock = new ReentrantLock();

	public static void lock() {

		lock.lock();
		glfwMakeContextCurrent(Game.getWindow().getHandle());

	}

	public static void unlock() {

		glfwMakeContextCurrent(0);
		lock.unlock();

	}

}
