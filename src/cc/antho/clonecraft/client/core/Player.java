package cc.antho.clonecraft.client.core;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import cc.antho.clonecraft.client.ClientListener;
import cc.antho.clonecraft.client.CloneCraftClient;
import cc.antho.clonecraft.client.CloneCraftGame;
import cc.antho.clonecraft.client.Controls;
import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.client.world.World;
import cc.antho.clonecraft.core.Mathf;
import cc.antho.clonecraft.core.event.EventDispatcher;
import cc.antho.clonecraft.core.events.NetworkPacketEvent;
import cc.antho.clonecraft.core.packet.BlockUpdatePacket;
import cc.antho.clonecraft.core.packet.PlayerMovePacket;
import lombok.Getter;

public class Player {

	public static final float SENSITIVITY_X = .3f;
	public static final float SENSITIVITY_Y = .3f;
	public static final float AXIS_X_LIMIT = 90f;
	public static final float WALK_SPEED = 4f;
	public static final float SPRINT_SPEED = 6f;
	public static final float EYE_HEIGHT = 1.6f;
	public static final float GRAVITY = -18f;
	public static final float JUMP_FORCE = 7f;

	@Getter private final Vector3f position = new Vector3f(0, 80, 0);

	public final Camera camera = new Camera();
	private final Vector3f velocity = new Vector3f();

	private boolean canJump = true;
	private boolean canBreak = false;
	private double timer = 0;

	private double informTimer = 0d;

	public int blockIndex = 0;

	public void tick(final World world) {

		velocity.x = 0;
		velocity.z = 0;

		final Input input = CloneCraftGame.getInput();

		final float deltaTime = (float) CloneCraftGame.getInstance().getDeltaTime();

		informTimer += deltaTime;

		// Rotate the camera using the mouse
		camera.rotation.x += SENSITIVITY_X * CloneCraftGame.getInput().getDifferecePos().y;
		camera.rotation.y += SENSITIVITY_Y * CloneCraftGame.getInput().getDifferecePos().x;
		camera.rotation.x = Mathf.clamp(camera.rotation.x, -AXIS_X_LIMIT, AXIS_X_LIMIT);
		camera.rotation.y %= 360f;

		float dist = 0;
		if (input.isKeyDown(Controls.WALK_FORWARD)) dist++;
		if (input.isKeyDown(Controls.WALK_BACKWARD)) dist--;
		dist *= WALK_SPEED * deltaTime;

		velocity.x += Mathf.sin(Mathf.toRadians(camera.rotation.y)) * dist;
		velocity.z -= Mathf.cos(Mathf.toRadians(camera.rotation.y)) * dist;

		dist = 0;
		if (input.isKeyDown(Controls.STRAFE_LEFT)) dist--;
		if (input.isKeyDown(Controls.STRAFE_RIGHT)) dist++;
		dist *= WALK_SPEED * deltaTime;

		velocity.x += Mathf.sin(Mathf.toRadians(camera.rotation.y + 90f)) * dist;
		velocity.z -= Mathf.cos(Mathf.toRadians(camera.rotation.y + 90f)) * dist;

		velocity.y += GRAVITY * deltaTime;

		// if (input.isKeyDown(Controls.SNEAK)) velocity.y -= speed;
		if (input.isKeyDown(Controls.JUMP) && canJump) {

			velocity.y = JUMP_FORCE;
			canJump = false;

		}

		// velocity.y *= deltaTime;

		if (velocity.x < 0) {

			final float nextX = position.x + velocity.x;

			final int blockX = Mathf.floor(nextX - .2f);
			final int blockZ0 = Mathf.floor(position.z - .2f);
			final int blockZ1 = Mathf.floor(position.z + .2f);
			final int blockY = Mathf.floor(position.y);

			if (world.getBlock(blockX, blockY, blockZ0) == null && world.getBlock(blockX, blockY, blockZ1) == null) position.x = nextX;

		} else if (velocity.x > 0) {

			final float nextX = position.x + velocity.x;

			final int blockX = Mathf.floor(nextX + .2f);
			final int blockZ0 = Mathf.floor(position.z - .2f);
			final int blockZ1 = Mathf.floor(position.z + .2f);
			final int blockY = Mathf.floor(position.y);

			if (world.getBlock(blockX, blockY, blockZ0) == null && world.getBlock(blockX, blockY, blockZ1) == null) position.x = nextX;

		}

		if (velocity.z < 0) {

			final float nextZ = position.z + velocity.z;

			final int blockX0 = Mathf.floor(position.x - .2f);
			final int blockX1 = Mathf.floor(position.x + .2f);
			final int blockZ = Mathf.floor(nextZ - .2f);
			final int blockY = Mathf.floor(position.y);

			if (world.getBlock(blockX0, blockY, blockZ) == null && world.getBlock(blockX1, blockY, blockZ) == null) position.z = nextZ;

		} else if (velocity.z > 0) {

			final float nextZ = position.z + velocity.z;

			final int blockX0 = Mathf.floor(position.x - .2f);
			final int blockX1 = Mathf.floor(position.x + .2f);
			final int blockZ = Mathf.floor(nextZ + .2f);
			final int blockY = Mathf.floor(position.y);

			if (world.getBlock(blockX0, blockY, blockZ) == null && world.getBlock(blockX1, blockY, blockZ) == null) position.z = nextZ;

		}

		if (velocity.y < 0) {

			final float nextY = position.y + velocity.y * deltaTime;

			final int blockY = Mathf.floor(nextY);

			final int blockX0 = Mathf.floor(position.x - .2f);
			final int blockZ0 = Mathf.floor(position.z - .2f);
			final int blockX1 = Mathf.floor(position.x + .2f);
			final int blockZ1 = Mathf.floor(position.z + .2f);

			if (world.getBlock(blockX0, blockY, blockZ0) == null && world.getBlock(blockX1, blockY, blockZ1) == null && world.getBlock(blockX0, blockY, blockZ1) == null && world.getBlock(blockX1, blockY, blockZ0) == null)
				position.y = nextY;
			else {
				velocity.y = 0;
				canJump = true;
			}

		} else if (velocity.y > 0) {

			final float nextY = position.y + velocity.y * deltaTime;

			final int blockY = Mathf.floor(nextY);

			final int blockX0 = Mathf.floor(position.x - .2f);
			final int blockX1 = Mathf.floor(position.x + .2f);
			final int blockZ0 = Mathf.floor(position.z - .2f);
			final int blockZ1 = Mathf.floor(position.z + .2f);

			if (world.getBlock(blockX0, blockY, blockZ0) == null && world.getBlock(blockX1, blockY, blockZ1) == null && world.getBlock(blockX0, blockY, blockZ1) == null && world.getBlock(blockX1, blockY, blockZ0) == null)
				position.y = nextY;
			else velocity.y = 0f;

		}

		if (ClientListener.ready && informTimer > ClientListener.playerPacketFreq) {

			informTimer = 0;
			EventDispatcher.dispatch(new NetworkPacketEvent(this, new PlayerMovePacket(CloneCraftClient.getNetworkClient().getID(), camera.position, camera.rotation), false, false));

		}

		camera.position.set(position);
		camera.position.y += EYE_HEIGHT;
		camera.tick();

		if (!canBreak) {

			timer += CloneCraftGame.getInstance().getDeltaTime();
			if (timer > .2f) {

				timer = 0;
				canBreak = true;

			}

		}

		if (input.isKeyPressed(GLFW_KEY_LEFT)) if (blockIndex <= 0) blockIndex = BlockType.BLOCKS.length - 1;
		else blockIndex--;

		if (input.isKeyPressed(GLFW_KEY_RIGHT)) if (blockIndex >= BlockType.BLOCKS.length - 1) blockIndex = 0;
		else blockIndex++;

		if (input.isButtonDown(GLFW_MOUSE_BUTTON_1) && canBreak) {

			canBreak = false;

			final Vector3f p = new Vector3f(camera.position);
			final Vector3f d = new Vector3f(camera.forward).mul(.5f);
			float current_distance = 0;
			final float max_distance = 10;

			while (true) {

				final BlockType type = world.getBlock(p.x, p.y, p.z);
				if (type != null) {

					world.setBlock(p.x, p.y, p.z, null);

					EventDispatcher.dispatch(new NetworkPacketEvent(this, new BlockUpdatePacket(Mathf.floor(p.x), Mathf.floor(p.y), Mathf.floor(p.z), null), false, true));

					world.getChunkFromWorldCoord(p.x, p.z).update();
					break;

				}

				p.add(d);
				current_distance += d.length();
				if (current_distance >= max_distance) break;

			}

		}

		if (input.isButtonDown(GLFW_MOUSE_BUTTON_2) && canBreak) {

			canBreak = false;

			final Vector3f lp = new Vector3f(camera.position);
			final Vector3f p = new Vector3f(camera.position);

			final Vector3f d = new Vector3f(camera.forward).mul(.5f);
			float current_distance = 0;
			final float max_distance = 10;

			while (true) {

				final BlockType type = world.getBlock(p.x, p.y, p.z);

				if (type != null) {

					final BlockType currentBlock = BlockType.BLOCKS[blockIndex];

					world.setBlock(lp.x, lp.y, lp.z, currentBlock);
					EventDispatcher.dispatch(new NetworkPacketEvent(this, new BlockUpdatePacket(Mathf.floor(lp.x), Mathf.floor(lp.y), Mathf.floor(lp.z), currentBlock), false, true));
					world.getChunkFromWorldCoord(lp.x, lp.z).update();
					break;

				} else lp.set(p);

				p.add(d);
				current_distance += d.length();
				if (current_distance >= max_distance) break;

			}

		}

	}

}
