package cc.antho.clonecraft.client.state;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import cc.antho.clonecraft.client.ContextManager;
import cc.antho.clonecraft.client.Controls;
import cc.antho.clonecraft.client.Game;
import cc.antho.clonecraft.client.core.Texture2D;
import cc.antho.clonecraft.client.ui.UIRenderer;
import cc.antho.clonecraft.client.ui.UITexture;
import cc.antho.clonecraft.core.Config;
import cc.antho.clonecraft.core.state.State;

public class MenuState extends State {

	private UIRenderer uiRenderer;

	public void init() {

		ContextManager.lock();

		uiRenderer = new UIRenderer();

		final BufferedImage image = new BufferedImage(100, 30, BufferedImage.TYPE_INT_ARGB);
		final Graphics2D g = image.createGraphics();
		g.setColor(new Color(0f, 0f, 0f, .7f));
		g.fillRect(0, 0, image.getWidth(), image.getHeight());
		g.setColor(Color.WHITE);
		g.drawString("Play", 0, g.getFontMetrics().getHeight());
		g.dispose();

		final UITexture texture = new UITexture();
		try {
			texture.scale.mul(.5f);
			texture.texture = new Texture2D(image, false);
		} catch (final IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		uiRenderer.addElement(texture);

		glEnable(GL_TEXTURE_2D);
		glEnable(GL_DEPTH_TEST);
		glFrontFace(GL_CCW);
		glEnable(GL_CULL_FACE);
		glCullFace(GL_BACK);

		Game.getInstance().getWindow().trigger();

		ContextManager.unlock();

	}

	public void tick() {

		if (Game.getInput().isKeyDown(Controls.JUMP)) manager.setState(new GameState());

	}

	public void render() {

		glViewport(0, 0, Game.getInstance().getWindow().getWidth(), Game.getInstance().getWindow().getHeight());
		glClear(Config.CLEAR);

		uiRenderer.render();

	}

	public void shutdown() {

		uiRenderer.shutdown();

	}

}
