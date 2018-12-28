package cc.antho.clonecraft.client;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferStrategy;
import java.util.Set;

import javax.swing.JFrame;

import cc.antho.clonecraft.client.world.thread.ChunkThread;
import cc.antho.clonecraft.core.math.Mathf;
import lombok.Getter;

public class Debugger {

	@Getter private static Thread thread;
	private static int targetOffset = 0;

	public static class Values {

		public static double deltaTime;
		public static double globalTime;
		public static int ups;
		public static int fps;
		public static int chunks_queued_model;
		public static int chunks_queued_upload;
		public static int chunks_queued_delete;

	}

	public static void start() {

		thread = new Thread(() -> {

			int width, height;
			final JFrame frame = new JFrame("CloneCraft Debugger");
			final Canvas canvas = new Canvas();
			canvas.setPreferredSize(new Dimension(width = 800, height = 500));
			frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			frame.setLayout(new BorderLayout());
			frame.add(canvas, BorderLayout.CENTER);
			frame.pack();

			frame.addWindowListener(new WindowListener() {

				public final void windowOpened(final WindowEvent e) {

				}

				public final void windowIconified(final WindowEvent e) {

				}

				public final void windowDeiconified(final WindowEvent e) {

				}

				public final void windowDeactivated(final WindowEvent e) {

				}

				public final void windowClosing(final WindowEvent e) {

					thread.interrupt();

				}

				public final void windowClosed(final WindowEvent e) {

				}

				public final void windowActivated(final WindowEvent e) {

				}

			});

			canvas.addMouseWheelListener(new MouseWheelListener() {

				public final void mouseWheelMoved(final MouseWheelEvent e) {

					targetOffset += e.getWheelRotation() * 48;
					targetOffset = (int) Mathf.max(0f, targetOffset);

				}

			});

			frame.setVisible(true);
			canvas.createBufferStrategy(2);
			final BufferStrategy bs = canvas.getBufferStrategy();

			float offset = 0;

			while (!Thread.interrupted()) {

				offset = Mathf.approachSmooth(offset, targetOffset, 1f / 10f);

				width = canvas.getWidth();
				height = canvas.getHeight();

				final Graphics2D g = (Graphics2D) bs.getDrawGraphics();

				{

					long free, max, total;
					free = Runtime.getRuntime().freeMemory();
					max = Runtime.getRuntime().maxMemory();
					total = Runtime.getRuntime().totalMemory();

					final int mb_use = (int) ((total - free) / (1024 * 1024));
					final int mb_total = (int) (total / (1024 * 1024));
					final int mb_max = (int) (max / (1024 * 1024));

					final Set<Thread> threadSet = Thread.getAllStackTraces().keySet();

					final int inc = 16;
					int val = 0;

					g.setColor(Color.BLACK);
					g.fillRect(0, 0, width, height);
					g.translate(0, -offset);
					g.setColor(Color.WHITE);
					g.drawString("CloneCraft", 0, val += inc);
					val += inc;
					g.drawString("-- Frames --", 0, val += inc);
					g.drawString("UPS: " + Values.ups, 0, val += inc);
					g.drawString("FPS: " + Values.fps, 0, val += inc);
					g.drawString("DeltaTime: " + Values.deltaTime, 0, val += inc);
					g.drawString("GlobalTime: " + Values.globalTime, 0, val += inc);
					val += inc;
					g.drawString("-- Memory --", 0, val += inc);
					g.setColor(Color.GREEN);
					g.drawString("Using: " + mb_use + "MB", 0, val += inc);
					g.setColor(Color.BLUE);
					g.drawString("Total: " + mb_total + "MB", 0, val += inc);
					g.setColor(Color.WHITE);
					g.drawString("Max: " + mb_max + "MB", 0, val += inc);
					g.fillRect(0, val, width, inc);
					g.setColor(Color.BLUE);
					g.fillRect(0, val, (int) ((float) mb_total / (float) mb_max * width), inc);
					g.setColor(Color.GREEN);
					g.fillRect(0, val, (int) ((float) mb_use / (float) mb_max * width), inc);
					val += inc;

					g.setColor(Color.WHITE);

					val += inc;
					g.drawString("-- Context Threads (Overall) --", 0, val += inc);

					{

						final long mainTotal = Game.getInstance().totalTime;
						final long uploadTotal = ChunkThread.uploadThread.totalTime;
						final long deleteTotal = ChunkThread.deleteThread.totalTime;
						final long totalTime = mainTotal + uploadTotal + deleteTotal;

						g.drawString("main " + Mathf.round((float) mainTotal / (float) totalTime * 100f) + "%", 0, val += inc);
						g.drawString("upload " + Mathf.round((float) uploadTotal / (float) totalTime * 100f) + "%", 0, val += inc);
						g.drawString("delete " + Mathf.round((float) deleteTotal / (float) totalTime * 100f) + "%", 0, val += inc);
						g.drawString("time " + totalTime + "ms", 0, val += inc);

					}

					val += inc;
					g.drawString("-- Context Threads (Current) --", 0, val += inc);

					{

						final long mainTotal = Game.getInstance().currentTime;
						final long uploadTotal = ChunkThread.uploadThread.currentTime;
						final long deleteTotal = ChunkThread.deleteThread.currentTime;
						final long totalTime = mainTotal + uploadTotal + deleteTotal;

						g.drawString("main " + Mathf.round((float) mainTotal / (float) totalTime * 100f) + "%", 0, val += inc);
						g.drawString("upload " + Mathf.round((float) uploadTotal / (float) totalTime * 100f) + "%", 0, val += inc);
						g.drawString("delete " + Mathf.round((float) deleteTotal / (float) totalTime * 100f) + "%", 0, val += inc);
						g.drawString("time " + totalTime + "ms", 0, val += inc);

					}

					val += inc;
					g.drawString("-- Chunk Queues --", 0, val += inc);
					g.drawString("Chunks enqueued model: " + Values.chunks_queued_model, 0, val += inc);
					g.drawString("Chunks enqueued upload: " + Values.chunks_queued_upload, 0, val += inc);
					g.drawString("Chunks enqueued delete: " + Values.chunks_queued_delete, 0, val += inc);

					val += inc;
					g.drawString("-- Threads --", 0, val += inc);
					for (final Thread t : threadSet) {
						g.drawString("Name: " + t.getName(), 0, val += inc);
						g.drawString("  ID: " + t.getId(), 0, val += inc);
						g.drawString("  Alive: " + t.isAlive(), 0, val += inc);
						g.drawString("  Priority: " + t.getPriority(), 0, val += inc);
						g.drawString("  Daemon: " + t.isDaemon(), 0, val += inc);
						g.drawString("  Interrupted: " + t.isInterrupted(), 0, val += inc);
					}

				}

				g.dispose();
				bs.show();

				try {

					Thread.sleep(1000 / 60);

				} catch (final InterruptedException e1) {

					break;

				}

			}

			frame.dispose();

		}, "Debugger");

		thread.start();

	}

}
