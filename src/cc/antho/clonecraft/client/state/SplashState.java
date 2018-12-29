package cc.antho.clonecraft.client.state;

import java.io.IOException;

import cc.antho.clonecraft.client.ContextManager;
import cc.antho.clonecraft.client.Game;
import cc.antho.clonecraft.client.core.Texture2D;
import cc.antho.clonecraft.client.ui.UITexture;
import cc.antho.clonecraft.core.state.State;

public class SplashState extends State {

	private UITexture splash;
	private double timer = 2f;

	public void init() {

		ContextManager.lock();

		splash = new UITexture();

		try {

			splash.texture = Texture2D.create("/textures/clonecraft_splash.png", false);
			splash.scale.set(.8f);

			Game.getRenderer().getUiRenderer().addElement(splash);

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	public void tick() {

		final double delta = Game.getInstance().getDeltaTime();

		timer -= delta;
		if (timer <= 0d) manager.setState(new MenuState());

		splash.scale.add((float) delta / 8f, (float) delta / 8f);

	}

	public void render() {

		Game.getRenderer().render();

	}

	public void shutdown() {

		Game.getRenderer().getUiRenderer().removeElement(splash);
		splash.texture.shutdown();

		ContextManager.unlock();

	}

}
