package cc.antho.clonecraft.client.state;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.Config;
import cc.antho.clonecraft.core.state.State;

public class MenuState extends State {

	public void init() {

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		CloneCraftGame.getInstance().getWindow().trigger();

	}

	public void tick() {

		if (CloneCraftGame.getInput().isKeyDown(GLFW_KEY_SPACE)) manager.setState(new GameState());

	}

	public void render() {

		glClear(Config.CLEAR);

	}

	public void shutdown() {

	}

}
