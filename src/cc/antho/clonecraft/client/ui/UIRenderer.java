package cc.antho.clonecraft.client.ui;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cc.antho.clonecraft.client.core.Shader;
import cc.antho.clonecraft.core.TMP;

public class UIRenderer {

	private final List<UIElement> elements = new ArrayList<>();

	private Shader panelShader, textureShader;

	public UIRenderer() {

		try {

			final String ui_v = Shader.loadShaderString("/shaders/ui/ui_v.glsl");
			final String panel_f = Shader.loadShaderString("/shaders/ui/panel_f.glsl");
			final String texture_f = Shader.loadShaderString("/shaders/ui/texture_f.glsl");
			panelShader = new Shader(ui_v, panel_f);
			textureShader = new Shader(ui_v, texture_f);
			textureShader.loadUniform1i("u_sampler", 0);

		} catch (final IOException e) {

			e.printStackTrace();

		}

		UIQuad.create();

	}

	public void shutdown() {

		panelShader.shutdown();
		UIQuad.shutdown();

	}

	public void addElement(final UIElement element) {

		if (elements.contains(element)) return;
		elements.add(element);

	}

	public void removeElement(final UIElement element) {

		if (!elements.contains(element)) return;
		elements.remove(element);

	}

	public void render() {

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glDisable(GL_DEPTH_TEST);
		glDepthMask(false);

		for (int i = 0; i < elements.size(); i++) {

			final UIElement element = elements.get(i);
			if (!element.enabled) continue;
			renderElement(element);

		}

		glEnable(GL_DEPTH_TEST);
		glDepthMask(true);
		glDisable(GL_BLEND);

	}

	private void renderElement(final UIElement element) {

		if (element instanceof UIPanel) renderPanel((UIPanel) element);
		else if (element instanceof UITexture) renderTexture((UITexture) element);

	}

	private void renderPanel(final UIPanel element) {

		panelShader.bind();
		panelShader.loadUniform4f("u_color", element.color.r, element.color.g, element.color.b, element.color.a);

		TMP.m40.translation(TMP.v30.set(element.position, 0f));
		TMP.m40.scale(TMP.v31.set(element.scale, 1f));
		panelShader.loadUniformMatrix4f("u_model", TMP.m40);

		UIQuad.render();

	}

	private void renderTexture(final UITexture element) {

		textureShader.bind();
		element.texture.bind(0);

		TMP.m40.translation(TMP.v30.set(element.position, 0f));
		TMP.m40.scale(TMP.v31.set(element.scale, 1f));
		textureShader.loadUniformMatrix4f("u_model", TMP.m40);

		UIQuad.render();

	}

}
