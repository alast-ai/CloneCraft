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

	public static final <T> boolean arrayContains(final T[] array, final T value) {

		for (int i = 0; i < array.length; i++)
			if (array[i].equals(value)) return true;

		return false;

	}

}
