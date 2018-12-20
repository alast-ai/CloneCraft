package cc.antho.clonecraft.client.pack;

public class PackLoader {

	public static int WIDTH = 5;
	public static int HEIGHT = 4;
	public static float WIDTH_SCALE = 1f / WIDTH;
	public static float HEIGHT_SCALE = 1f / HEIGHT;
	public static int SIZE = 0;

	public static void set(final int s) {

		WIDTH = s;
		HEIGHT = s;
		WIDTH_SCALE = 1f / WIDTH;
		HEIGHT_SCALE = 1f / HEIGHT;

	}

}
