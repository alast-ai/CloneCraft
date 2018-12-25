package cc.antho.clonecraft.client.ui;

import static org.lwjgl.opengl.GL11.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import cc.antho.clonecraft.client.core.Shader;

public class UIRenderer {

	private final List<UIElement> elements = new ArrayList<>();

	private Shader panelShader;

	public UIRenderer() {

		try {

			final String ui_v = Shader.loadShaderString("/shaders/ui/ui_v.glsl");
			final String panel_f = Shader.loadShaderString("/shaders/ui/panel_f.glsl");
			panelShader = new Shader(ui_v, panel_f);
			panelShader.loadUniform1i("u_sampler", 0);

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

	}

	private void renderPanel(final UIPanel element) {

		panelShader.bind();
		panelShader.loadUniform4f("u_color", element.color.r, element.color.g, element.color.b, element.color.a);

		final Matrix4f matrix = new Matrix4f();
		matrix.translate(new Vector3f(element.position, 0f));
		matrix.scale(new Vector3f(element.scale, 1f));
		panelShader.loadUniformMatrix4f("u_model", matrix);

		UIQuad.render();

	}

}
