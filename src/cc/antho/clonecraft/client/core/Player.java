package cc.antho.clonecraft.client.core;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import cc.antho.ascl8.math.Mathf;
import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.Controls;
import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.clienti.CloneCraftClient;
import cc.antho.clonecraft.common.packet.BlockUpdatePacket;
import cc.antho.clonecraft.core.event.Event;
import cc.antho.clonecraft.core.event.EventDispatcher;
import cc.antho.clonecraft.core.event.EventListener;
import cc.antho.clonecraft.core.event.impl.FramebufferResizeEvent;
import lombok.Getter;
import lombok.Setter;

public class Player implements EventListener {

	@Getter @Setter private boolean projectionDirty = true;
	@Getter private final Matrix4f projection = new Matrix4f();
	@Getter private final Matrix4f view = new Matrix4f();

	@Getter private final Vector3f position = new Vector3f(0, 80, 0);
	@Getter private final Vector3f rotation = new Vector3f();

	public Player() {

		EventDispatcher.addListener(this);

	}

	public void rotate(final float xs, final float ys) {

		rotation.x += ys * CloneCraftGame.getInput().getDifferecePos().y;
		rotation.y += xs * CloneCraftGame.getInput().getDifferecePos().x;

		rotation.x = Mathf.clamp(rotation.x, -90f, 90f);
		rotation.y %= 360f;

	}

	private float yVel = 0;
	private boolean canJump = true;

	public Vector3f forward = new Vector3f();

	public void move(float speed, final World world) {

		final Input input = CloneCraftGame.getInput();
		speed *= CloneCraftGame.getInstance().getDeltaTime();

		final Vector3f velocity = new Vector3f();

		float dist = 0;
		if (input.isKeyDown(Controls.WALK_FORWARD)) dist += speed;
		if (input.isKeyDown(Controls.WALK_BACKWARD)) dist -= speed;

		velocity.x += Mathf.sin(Mathf.toRadians(rotation.y)) * dist;
		velocity.z -= Mathf.cos(Mathf.toRadians(rotation.y)) * dist;

		dist = 0;
		if (input.isKeyDown(Controls.STRAFE_LEFT)) dist -= speed;
		if (input.isKeyDown(Controls.STRAFE_RIGHT)) dist += speed;

		velocity.x += Mathf.sin(Mathf.toRadians(rotation.y + 90f)) * dist;
		velocity.z -= Mathf.cos(Mathf.toRadians(rotation.y + 90f)) * dist;

		yVel -= 10f * CloneCraftGame.getInstance().getDeltaTime();

		// if (input.isKeyDown(Controls.SNEAK)) velocity.y -= speed;
		if (input.isKeyDown(Controls.JUMP) && canJump) {
			yVel = 5f;
			canJump = false;
		}

		velocity.y = (float) (yVel * CloneCraftGame.getInstance().getDeltaTime());

		if (velocity.x < 0) {

			final float nextX = position.x + velocity.x;

			final int blockX = Mathf.floor(nextX);
			final int blockZ = Mathf.floor(position.z);
			final int blockY = Mathf.floor(position.y);

			if (world.getBlock(blockX, blockY, blockZ) == null)
				position.x = nextX;

		} else if (velocity.x > 0) {

			final float nextX = position.x + velocity.x;

			final int blockX = Mathf.floor(nextX);
			final int blockZ = Mathf.floor(position.z);
			final int blockY = Mathf.floor(position.y);

			if (world.getBlock(blockX, blockY, blockZ) == null)
				position.x = nextX;

		}

		if (velocity.z < 0) {

			final float nextZ = position.z + velocity.z;

			final int blockX = Mathf.floor(position.x);
			final int blockZ = Mathf.floor(nextZ);
			final int blockY = Mathf.floor(position.y);

			if (world.getBlock(blockX, blockY, blockZ) == null)
				position.z = nextZ;

		} else if (velocity.z > 0) {

			final float nextZ = position.z + velocity.z;

			final int blockX = Mathf.floor(position.x);
			final int blockZ = Mathf.floor(nextZ);
			final int blockY = Mathf.floor(position.y);

			if (world.getBlock(blockX, blockY, blockZ) == null)
				position.z = nextZ;

		}

		if (velocity.y < 0) {

			final float nextY = position.y + velocity.y;

			final int blockX = Mathf.floor(position.x);
			final int blockZ = Mathf.floor(position.z);
			final int blockY = Mathf.floor(nextY);

			if (world.getBlock(blockX, blockY, blockZ) == null)
				position.y = nextY;
			else {
				yVel = 0;
				canJump = true;
			}

		} else if (velocity.y > 0) {

			final float nextY = position.y + velocity.y;

			final int blockX = Mathf.floor(position.x);
			final int blockZ = Mathf.floor(position.z);
			final int blockY = Mathf.floor(nextY);

			if (world.getBlock(blockX, blockY, blockZ) == null)
				position.y = nextY;
			else yVel = 0;

		}

	}

	private boolean canBreak = false;

	private double timer = 0;

	public void update(final World world) {

		position.mul(-1f);

		view.rotation(Mathf.toRadians(rotation.z), 0, 0, 1);
		view.rotate(Mathf.toRadians(rotation.x), 1, 0, 0);
		view.rotate(Mathf.toRadians(rotation.y), 0, 1, 0);
		view.translate(position);
		view.translate(0, -1.6f, 0);

		position.mul(-1f);

		final Matrix4f invView = new Matrix4f();
		view.invert(invView);
		final Vector4f v = new Vector4f(0, 0, -1, 0);
		v.mul(invView);
		forward.set(v.x, v.y, v.z);
		forward.normalize();

		if (!canBreak) {

			timer += CloneCraftGame.getInstance().getDeltaTime();
			if (timer > .2f) {

				timer = 0;
				canBreak = true;

			}

		}

		if (CloneCraftGame.getInput().isButtonDown(GLFW_MOUSE_BUTTON_1) && canBreak) {

			canBreak = false;

			final Vector3f p = new Vector3f(position);
			p.y += 1.6f;
			final Vector3f d = new Vector3f(forward).mul(.5f);
			float current_distance = 0;
			final float max_distance = 10;

			while (true) {

				final BlockType type = world.getBlock(p.x, p.y, p.z);
				if (type != null) {

					world.setBlock(p.x, p.y, p.z, null);
					CloneCraftClient.getConnection().submit(new BlockUpdatePacket(Mathf.floor(p.x), Mathf.floor(p.y), Mathf.floor(p.z), null));
					world.getChunkFromWorldCoord(p.x, p.z).update();
					break;

				}

				p.add(d);
				current_distance += d.length();
				if (current_distance >= max_distance) break;

			}

		}

		if (CloneCraftGame.getInput().isButtonDown(GLFW_MOUSE_BUTTON_2) && canBreak) {

			canBreak = false;

			final Vector3f lp = new Vector3f(position);
			final Vector3f p = new Vector3f(position);
			p.y += 1.6f;
			final Vector3f d = new Vector3f(forward).mul(.5f);
			float current_distance = 0;
			final float max_distance = 10;

			while (true) {

				final BlockType type = world.getBlock(p.x, p.y, p.z);

				if (type != null) {

					world.setBlock(lp.x, lp.y, lp.z, BlockType.STONE);
					CloneCraftClient.getConnection().submit(new BlockUpdatePacket(Mathf.floor(lp.x), Mathf.floor(lp.y), Mathf.floor(lp.z), BlockType.STONE));
					world.getChunkFromWorldCoord(lp.x, lp.z).update();
					break;

				} else lp.set(p);

				p.add(d);
				current_distance += d.length();
				if (current_distance >= max_distance) break;

			}

		}

	}

	public void onEvent(final Event event) {

		if (event instanceof FramebufferResizeEvent) {

			final FramebufferResizeEvent e = (FramebufferResizeEvent) event;

			projection.setPerspective(Mathf.toRadians(70f), (float) e.getWidth() / (float) e.getHeight(), .3f, 1000f);
			projectionDirty = true;

		}

	}

}
