package cc.antho.clonecraft.client.state;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.ContextManager;
import cc.antho.clonecraft.client.Controls;
import cc.antho.clonecraft.client.core.Texture2D;
import cc.antho.clonecraft.client.ui.UIRenderer;
import cc.antho.clonecraft.client.ui.UITexture;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.state.State;

public class SplashState extends State {

	private UIRenderer uiRenderer;
	private UITexture splash;

	public void init() {

		ContextManager.lock();

		uiRenderer = new UIRenderer();
		splash = new UITexture();

		try {

			splash.texture = Texture2D.create("/textures/clonecraft_splash.png", false);
			uiRenderer.addElement(splash);

		} catch (final IOException e) {

			e.printStackTrace();

		}

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		CloneCraftGame.getInstance().getWindow().trigger();

		ContextManager.unlock();

	}

	public void tick() {

		// TODO change to menu after certain amount of time
		if (CloneCraftGame.getInput().isKeyDown(Controls.JUMP)) manager.setState(new GameState());

	}

	public void render() {

		glViewport(0, 0, CloneCraftGame.getInstance().getWindow().getWidth(), CloneCraftGame.getInstance().getWindow().getHeight());
		glClear(Config.CLEAR);

		uiRenderer.render();

	}

	public void shutdown() {

		uiRenderer.shutdown();
		splash.texture.shutdown();

	}

}
