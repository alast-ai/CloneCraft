package cc.antho.clonecraft.client.world.thread;

import static org.lwjgl.glfw.GLFW.*;

import org.lwjgl.opengl.GL;

import cc.antho.clonecraft.client.CloneCraft;
import cc.antho.clonecraft.client.Debugger;
import cc.antho.clonecraft.client.world.Chunk;

public class ChunkDeleteThread extends ChunkThread {

	ChunkDeleteThread() {

		create(() -> {

			final Chunk chunk = queue.get(0);

			if (chunk == null) return;

			queue.remove(chunk);
			Debugger.Values.chunks_queued_delete = queue.size();

			lock.lock();
			final long m = System.currentTimeMillis();

			// TODO enforces pipeline flush
			glfwMakeContextCurrent(CloneCraft.getInstance().getWindow().getHandle());

			GL.createCapabilities();

			chunk.getMesh().shutdown();

			glfwMakeContextCurrent(0);

			currentTime = System.currentTimeMillis() - m;
			totalTime += currentTime;

			lock.unlock();

		}, "Chunk Delete", true);

	}

	protected void debugUpdate() {

		Debugger.Values.chunks_queued_delete = queue.size();

	}

}
