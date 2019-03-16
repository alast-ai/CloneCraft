package cc.antho.clonecraft.client.core;

import org.joml.Matrix4f;
import org.joml.Vector3f;

import cc.antho.clonecraft.core.TMP;
import cc.antho.clonecraft.core.events.FramebufferResizeEvent;
import cc.antho.clonecraft.core.math.Mathf;
import cc.antho.commons.event.Event;
import cc.antho.commons.event.EventLayer;
import cc.antho.commons.event.EventListener;
import lombok.Getter;
import lombok.Setter;

public class Camera extends EventListener {

	@Getter @Setter private boolean projectionDirty = true;
	@Getter private final Matrix4f projection = new Matrix4f();
	@Getter private final Matrix4f view = new Matrix4f();

	public final Vector3f position = new Vector3f();
	public final Vector3f rotation = new Vector3f();

	public final Vector3f forward = new Vector3f();
	public final Vector3f up = new Vector3f();
	public final Vector3f right = new Vector3f();

	public Camera() {

		EventLayer.ROOT.addListener(this);

	}

	public void tick() {

		position.mul(-1f);

		view.rotation(Mathf.toRadians(rotation.z), 0, 0, 1);
		view.rotate(Mathf.toRadians(rotation.x), 1, 0, 0);
		view.rotate(Mathf.toRadians(rotation.y), 0, 1, 0);
		view.translate(position);

		position.mul(-1f);

		view.invert(TMP.m40);

		{

			TMP.v40.set(0f, 0f, -1f, 0f).mul(TMP.m40);

			forward.set(TMP.v40.x, TMP.v40.y, TMP.v40.z);
			forward.normalize();

		}

		{

			TMP.v40.set(0f, 1f, 0f, 0f).mul(TMP.m40);

			up.set(TMP.v40.x, TMP.v40.y, TMP.v40.z);
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
