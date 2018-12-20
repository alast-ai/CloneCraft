package cc.antho.clonecraft.client.core;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import cc.antho.clonecraft.core.Mathf;
import cc.antho.clonecraft.core.event.Event;
import cc.antho.clonecraft.core.event.EventDispatcher;
import cc.antho.clonecraft.core.event.EventListener;
import cc.antho.clonecraft.core.events.FramebufferResizeEvent;
import lombok.Getter;
import lombok.Setter;

public class Camera implements EventListener {

	@Getter @Setter private boolean projectionDirty = true;
	@Getter private final Matrix4f projection = new Matrix4f();
	@Getter private final Matrix4f view = new Matrix4f();

	public final Vector3f position = new Vector3f();
	public final Vector3f rotation = new Vector3f();

	public final Vector3f forward = new Vector3f();
	public final Vector3f up = new Vector3f();
	public final Vector3f right = new Vector3f();

	public Camera() {

		EventDispatcher.addListener(this);

	}

	public void tick() {

		position.mul(-1f);

		view.rotation(Mathf.toRadians(rotation.z), 0, 0, 1);
		view.rotate(Mathf.toRadians(rotation.x), 1, 0, 0);
		view.rotate(Mathf.toRadians(rotation.y), 0, 1, 0);
		view.translate(position);

		position.mul(-1f);

		final Matrix4f invView = new Matrix4f();
		view.invert(invView);

		{

			final Vector4f v = new Vector4f(0f, 0f, -1f, 0f);
			v.mul(invView);

			forward.set(v.x, v.y, v.z);
			forward.normalize();

		}

		{

			final Vector4f v = new Vector4f(0f, 1f, 0f, 0f);
			v.mul(invView);

			up.set(v.x, v.y, v.z);
			up.normalize();

		}

		forward.cross(up, right);
		right.normalize();

	}

	public void onEvent(final Event event) {

		if (event instanceof FramebufferResizeEvent) {

			final FramebufferResizeEvent e = (FramebufferResizeEvent) event;

			projection.setPerspective(Mathf.toRadians(70f), (float) e.getWidth() / (float) e.getHeight(), .1f, 1000f);
			projectionDirty = true;

		}

	}

}
