package cc.antho.clonecraft;

import com.esotericsoftware.minlog.Log;

import cc.antho.clonecraft.client.Debugger;
import cc.antho.clonecraft.client.Game;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.Util;
import cc.antho.clonecraft.core.log.Logger;
import cc.antho.clonecraft.core.log.LoggerImpl;
import cc.antho.clonecraft.server.NetworkServer;

public final class CloneCraft {

	private CloneCraft() {

	}

	public static final void main(final String[] args) {

		Logger.logger = new LoggerImpl();

		Log.setLogger(new Log.Logger() {

			public final void log(final int level, final String category, final String message, final Throwable ex) {

				switch (level) {

					case Log.LEVEL_ERROR:
						Logger.error(message);
						break;
					case Log.LEVEL_WARN:
						Logger.warn(message);
						break;
					case Log.LEVEL_INFO:
						Logger.info(message);
						break;
					case Log.LEVEL_DEBUG:
						Logger.debug(message);
						break;
					case Log.LEVEL_TRACE:
						Logger.trace(message);
						break;

				}

			}

		});

		Logger.info("Starting CloneCraft");

		Logger.debug("Setting look and feel to system");
		Util.setLookAndFeel();

		Logger.debug("Loading config");
		Config.loadConfig();

		final boolean startServer = Util.arrayContains(args, "server");
		final boolean useDebugger = Util.arrayContains(args, "debugger");

		if (useDebugger) Debugger.startThread();

		if (startServer) NetworkServer.startThread();
		else Game.startThread();

	}

}
