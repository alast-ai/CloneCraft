package cc.antho.clonecraft.client.world;

import lombok.Getter;

public class BlockFace {

	public static final BlockFace LEFT = new BlockFace(new float[] {

			0, 0, 0,
			0, 0, 1,
			0, 1, 0,
			0, 1, 1,
			0, 1, 0,
			0, 0, 1

	});

	public static final BlockFace RIGHT = new BlockFace(new float[] {

			1, 0, 1,
			1, 0, 0,
			1, 1, 1,
			1, 1, 0,
			1, 1, 1,
			1, 0, 0

	});

	public static final BlockFace FRONT = new BlockFace(new float[] {

			0, 0, 1,
			1, 0, 1,
			0, 1, 1,
			1, 1, 1,
			0, 1, 1,
			1, 0, 1

	});

	public static final BlockFace BACK = new BlockFace(new float[] {

			1, 0, 0,
			0, 0, 0,
			1, 1, 0,
			0, 1, 0,
			1, 1, 0,
			0, 0, 0

	});

	public static final BlockFace TOP = new BlockFace(new float[] {

			0, 1, 1,
			1, 1, 1,
			0, 1, 0,
			1, 1, 0,
			0, 1, 0,
			1, 1, 1

	});

	public static final BlockFace BOTTOM = new BlockFace(new float[] {

			0, 0, 0,
			1, 0, 0,
			0, 0, 1,
			1, 0, 1,
			0, 0, 1,
			1, 0, 0

	});

	@Getter private final float[] positions;
	@Getter private final float[] texCoords = new float[] { 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0 };

	public BlockFace(final float[] positions) {

		this.positions = positions;

	}

}
