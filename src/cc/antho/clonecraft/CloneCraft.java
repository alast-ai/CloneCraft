package cc.antho.clonecraft;

import javax.swing.JOptionPane;

import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.core.Util;
import cc.antho.clonecraft.server.CloneCraftServer;

public final class CloneCraft {

	private CloneCraft() {

	}

	public static final void main(final String[] args) {

		Util.setLookAndFeel();

		final int option = JOptionPane.showConfirmDialog(null, "Start dedicated server?", "CloneCraft", JOptionPane.YES_NO_CANCEL_OPTION);

		if (option == 0) CloneCraftServer.main();
		else if (option == 1) CloneCraftClient.main();

	}

}
