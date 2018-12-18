package cc.antho.clonecraft;

import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.server.CloneCraftServer;

public final class CloneCraft {

	private CloneCraft() {

	}

	public static final void main(final String[] args) {

		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {

			e.printStackTrace();

		}

		final int option = JOptionPane.showConfirmDialog(null, "Start dedicated server?", "CloneCraft", JOptionPane.YES_NO_CANCEL_OPTION);

		if (option == 0) CloneCraftServer.main();
		else if (option == 1) CloneCraftClient.main();

	}

}
