package cc.antho.clonecraft.client.state;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import cc.antho.clonecraft.client.ContextManager;
import cc.antho.clonecraft.client.Controls;
import cc.antho.clonecraft.client.Game;
import cc.antho.clonecraft.client.core.Texture2D;
import cc.antho.clonecraft.client.ui.UITexture;
import cc.antho.clonecraft.core.state.State;

public class MenuState extends State {

	private UITexture texture;

	public void init() {

		ContextManager.lock();

		final BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = image.createGraphics();
		g.setColor(new Color(0f, 0f, 0f, .7f));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.setColor(Color.WHITE);
		g.drawString("Play", 0, g.getFontMetrics().getHeight());
		g.dispose();

		texture = new UITexture();

		try {

			texture.scale.mul(.5f);
			texture.texture = new Texture2D(image, false);
			Game.getRenderer().getUiRenderer().addElement(texture);

		} catch (final IOException e) {

			e.printStackTrace();

		}

	}

	public void tick() {

		if (Game.getWindow().getInput().isKeyDown(Controls.JUMP)) manager.setState(new GameState());

	}

	public void render() {

		Game.getRenderer().render();

	}

	public void shutdown() {

		Game.getRenderer().getUiRenderer().removeElement(texture);
		texture.texture.shutdown();
		ContextManager.unlock();

	}

}
