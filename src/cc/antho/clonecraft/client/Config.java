package cc.antho.clonecraft.client;

import static org.lwjgl.opengl.GL46.*;

public final class Config {

	// Config Settings
	public static final int UPS_SUBDIVIDE = 100;
	public static final int DEFAULT_WIDTH = 1280;
	public static final int DEFAULT_HEIGHT = 720;
	public static final boolean USE_FULLSCREEN = false;
	public static final boolean USE_VSYNC = false;

	// Clear color
	public static final float CLEAR_RED = .7f;
	public static final float CLEAR_GREEN = .8f;
	public static final float CLEAR_BLUE = .9f;
	public static final float CLEAR_ALPHA = 1f;

	public static final int CLEAR = GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT;

	private Config() {

	}

}
