package cc.antho.clonecraft.client.world.thread;

import org.lwjgl.opengl.GL;

import cc.antho.clonecraft.client.ContextManager;
import cc.antho.clonecraft.client.Debugger;
import cc.antho.clonecraft.client.world.Chunk;

public class ChunkDeleteThread extends ChunkThread {

	ChunkDeleteThread() {

		create(() -> {

			final Chunk chunk = queue.get(0);

			if (chunk == null) return;

			queue.remove(chunk);
			Debugger.Values.chunks_queued_delete = queue.size();

			ContextManager.lock();
			final long m = System.currentTimeMillis();

			GL.createCapabilities();

			chunk.getMesh().shutdown();

			currentTime = System.currentTimeMillis() - m;
			totalTime += currentTime;

			ContextManager.unlock();

		}, "Chunk Delete", true);

	}

	protected void debugUpdate() {

		Debugger.Values.chunks_queued_delete = queue.size();

	}

}
