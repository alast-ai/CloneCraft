package cc.antho.clonecraft.core;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class Util {

	private Util() {

	}

	public static final void setLookAndFeel() {

		try {

			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {

			e.printStackTrace();

		}

	}

}
