package cc.antho.clonecraft.client.world;

import cc.antho.clonecraft.client.core.FloatVertexArray;
import cc.antho.clonecraft.core.Mathf;
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

	private static float rt = Mathf.sqrt(2) / 2f;

	public static final BlockFace X = new BlockFace(new float[] {

			0, 0, 0,
			rt, 0, rt,
			0, 1, 0,
			0, 1, 0,
			rt, 0, rt,
			rt, 1, rt,

			rt, 0, rt,
			0, 0, 0,
			rt, 1, rt,
			rt, 1, rt,
			0, 0, 0,
			0, 1, 0,

			0, 0, rt,
			rt, 0, 0,
			0, 1, rt,
			0, 1, rt,
			rt, 0, 0,
			rt, 1, 0,

			rt, 0, 0,
			0, 0, rt,
			rt, 1, 0,
			rt, 1, 0,
			0, 0, rt,
			0, 1, rt

	});

	static {

		X.positions = new FloatVertexArray().append(X.positions).add(rt / 4f, 0, rt / 4f).get();

		X.texCoords = new float[] {

				0, 0,
				1, 0,
				0, 1,
				0, 1,
				1, 0,
				1, 1,

				0, 0,
				1, 0,
				0, 1,
				0, 1,
				1, 0,
				1, 1,

				0, 0,
				1, 0,
				0, 1,
				0, 1,
				1, 0,
				1, 1,

				0, 0,
				1, 0,
				0, 1,
				0, 1,
				1, 0,
				1, 1

		};

	}

	@Getter private float[] positions;
	@Getter private float[] texCoords = new float[] { 0, 0, 1, 0, 0, 1, 1, 1, 0, 1, 1, 0 };

	public BlockFace(final float[] positions) {

		this.positions = positions;

	}

}
