package cc.antho.clonecraft.client.world.thread;

import org.joml.Vector2f;

import cc.antho.clonecraft.client.Debugger;
import cc.antho.clonecraft.client.world.Chunk;

public class ChunkModelThread extends ChunkThread {

	ChunkModelThread() {

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

				if (tmp.getStatus().awaitingDeletion) {

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
			Debugger.Values.chunks_queued_model = queue.size();

			chunk.model();
			ChunkThread.uploadThread.add(chunk);

		}, "Chunk Model", false);

	}

	protected void debugUpdate() {

		Debugger.Values.chunks_queued_model = queue.size();

	}

}
