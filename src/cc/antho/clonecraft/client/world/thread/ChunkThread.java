package cc.antho.clonecraft.client.world.thread;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;

import cc.antho.clonecraft.client.world.Chunk;

public abstract class ChunkThread {

	// Chunk thread instances
	public static final ChunkModelThread modelThread = new ChunkModelThread();
	public static final ChunkUploadThread uploadThread = new ChunkUploadThread();
	public static final ChunkDeleteThread deleteThread = new ChunkDeleteThread();

	protected Thread thread;
	protected final List<Chunk> queue = new ArrayList<>();
	private boolean syncAdd;

	protected ChunkThread() {

	}

	protected void start() {

		thread.start();

	}

	public void interrupt() {

		thread.interrupt();

	}

	public void join() {

		try {

			thread.join();

		} catch (final InterruptedException e) {

			e.printStackTrace();

		}

	}

	public void add(final Chunk chunk) {

		while (thread.isInterrupted())
			return;

		if (syncAdd) synchronized (queue) {

			if (!queue.contains(chunk)) queue.add(chunk);
			debugUpdate();

		}
		else {

			if (!queue.contains(chunk)) queue.add(chunk);
			debugUpdate();

		}

	}

	public void remove(final Chunk chunk) {

		synchronized (queue) {

			if (!queue.contains(chunk)) queue.remove(chunk);
			debugUpdate();

		}

	}

	protected void create(final Runnable runnable, final String name, final boolean syncAdd) {

		this.syncAdd = syncAdd;

		thread = new Thread(() -> {

			while (!Thread.interrupted())
				synchronized (queue) {
					if (!queue.isEmpty())
						runnable.run();
				}

		}, name);

		thread.setPriority(Thread.MIN_PRIORITY);

	}

	protected abstract void debugUpdate();

	// Static stuff
	public static volatile Vector2f playerPostionChunk = new Vector2f();

	public static void startThreads() {

		modelThread.start();
		uploadThread.start();
		deleteThread.start();

	}

	public static void stopThreads() {

		modelThread.interrupt();
		modelThread.join();

		uploadThread.interrupt();
		uploadThread.join();

		deleteThread.interrupt();
		deleteThread.join();

	}

}
