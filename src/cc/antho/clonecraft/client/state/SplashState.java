package cc.antho.clonecraft.client.state;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;

import cc.antho.clonecraft.client.ContextManager;
import cc.antho.clonecraft.client.Game;
import cc.antho.clonecraft.client.core.Texture2D;
import cc.antho.clonecraft.client.ui.UIRenderer;
import cc.antho.clonecraft.client.ui.UITexture;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.state.State;

public class SplashState extends State {

	private UIRenderer uiRenderer;
	private UITexture splash;

	private double timer = 2f;

	public void init() {

		ContextManager.lock();

		uiRenderer = new UIRenderer();
		splash = new UITexture();

		try {

			splash.texture = Texture2D.create("/textures/clonecraft_splash.png", false);
			splash.scale.set(.8f);
			uiRenderer.addElement(splash);

		} catch (final IOException e) {

			e.printStackTrace();

		}

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		Game.getInstance().getWindow().trigger();

		ContextManager.unlock();

	}

	public void tick() {

		timer -= Game.getInstance().getDeltaTime();
		if (timer <= 0d) manager.setState(new MenuState());

		splash.scale.add((float) Game.getInstance().getDeltaTime() / 5f, (float) Game.getInstance().getDeltaTime() / 5f);

	}

	public void render() {

		glViewport(0, 0, Game.getInstance().getWindow().getWidth(), Game.getInstance().getWindow().getHeight());
		glClear(Config.CLEAR);

		uiRenderer.render();

	}

	public void shutdown() {

		uiRenderer.shutdown();
		splash.texture.shutdown();

	}

}
