package cc.antho.clonecraft;

import cc.antho.clonecraft.client.CloneCraftGame;
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
		Logger.info("Starting CloneCraft");

		Logger.debug("Setting look and feel to system");
		Util.setLookAndFeel();

		Logger.debug("Loading config");
		Config.loadConfig();

		final boolean startServer = Util.arrayContains(args, "server");
		final boolean useDebugger = Util.arrayContains(args, "debugger");

		if (startServer) NetworkServer.startThread();
		else CloneCraftGame.main(useDebugger);

	}

}
