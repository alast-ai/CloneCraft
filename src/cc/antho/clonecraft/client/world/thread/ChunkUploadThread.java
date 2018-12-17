package cc.antho.clonecraft.client.world.thread;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector2f;
import org.lwjgl.opengl.GL;

import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.Debugger;
import cc.antho.clonecraft.client.world.Chunk;

public class ChunkUploadThread extends ChunkThread {

	ChunkUploadThread() {

		create(() -> {

			// find closest chunk to the player
			final Vector2f chunkpos = new Vector2f();

			Chunk chunk = null;
			float dist = Float.MAX_VALUE;

			for (int i = queue.size() - 1; i >= 0; i--) {

				final Chunk tmp = queue.get(i);
				if (tmp == null) {
					queue.remove(i);
					continue;
				}
				chunkpos.set(tmp.getX(), tmp.getZ());
				final float d = chunkpos.distance(ChunkThread.playerPostionChunk);

				if (d < dist) {

					chunk = tmp;
					dist = d;

				}

			}

			if (chunk == null) return;

			queue.remove(chunk);
			Debugger.Values.chunks_queued_upload = queue.size();

			lock.lock();
			final long m = System.currentTimeMillis();

			// TODO enforces pipeline flush
			glfwMakeContextCurrent(CloneCraftGame.getInstance().getWindow().getHandle());

			GL.createCapabilities();

			chunk.getMesh().upload();
			glfwMakeContextCurrent(0);

			currentTime = System.currentTimeMillis() - m;
			totalTime += currentTime;

			lock.unlock();

		}, "Chunk Upload", false);

	}

	protected void debugUpdate() {

		Debugger.Values.chunks_queued_upload = queue.size();

	}

}
