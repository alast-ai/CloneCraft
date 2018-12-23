package cc.antho.clonecraft.client;

import static org.lwjgl.glfw.GLFW.*;

public final class Controls {

	public static final int WALK_FORWARD = GLFW_KEY_W;
	public static final int WALK_BACKWARD = GLFW_KEY_S;
	public static final int STRAFE_LEFT = GLFW_KEY_A;
	public static final int STRAFE_RIGHT = GLFW_KEY_D;
	public static final int SNEAK = GLFW_KEY_LEFT_SHIFT;
	public static final int RUN = GLFW_KEY_LEFT_CONTROL;
	public static final int JUMP = GLFW_KEY_SPACE;

	public static final float SENSITIVITY_X = .3f;
	public static final float SENSITIVITY_Y = .3f;
	public static final int BLOCK_BREAK = GLFW_MOUSE_BUTTON_1;
	public static final int BLOCK_PLACE = GLFW_MOUSE_BUTTON_2;
	public static final int BLOCK_PICK = GLFW_MOUSE_BUTTON_3;

	private Controls() {

	}

}
