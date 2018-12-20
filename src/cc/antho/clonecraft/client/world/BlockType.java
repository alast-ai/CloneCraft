package cc.antho.clonecraft.client.world;

import java.io.Serializable;

import org.joml.Vector2i;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockType implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final BlockType GRASS = new BlockType(
			new Vector2i(0, 1),
			new Vector2i(1, 1),
			new Vector2i(1, 0), true, false);

	public static final BlockType DIRT = new BlockType(
			new Vector2i(1, 0), true, false);

	public static final BlockType STONE = new BlockType(
			new Vector2i(2, 0), true, false);

	public static final BlockType LOG = new BlockType(
			new Vector2i(2, 1),
			new Vector2i(3, 0),
			new Vector2i(3, 0), true, false);

	public static final BlockType LEAVES = new BlockType(
			new Vector2i(3, 1), true, true);

	public static final BlockType BRICKS = new BlockType(
			new Vector2i(0, 2), true, false);

	public static final BlockType BEDROCK = new BlockType(
			new Vector2i(1, 2), false, false);

	public static final BlockType STONEBRICKS = new BlockType(
			new Vector2i(2, 2), true, false);

	public static final BlockType COAL_ORE = new BlockType(
			new Vector2i(3, 2), true, false);

	public static final BlockType COBBLESTONE = new BlockType(
			new Vector2i(0, 3), true, false);

	public static final BlockType SAND = new BlockType(
			new Vector2i(1, 3), true, false);

	public static final BlockType TALLGRASS = new BlockType(
			new Vector2i(2, 3), true, true);

	public static final BlockType PINK_WOOL = new BlockType(
			new Vector2i(3, 3), true, false);

	public static final BlockType PLANKS = new BlockType(
			new Vector2i(4, 0), true, false);

	static {

		TALLGRASS.useXModel = true;

	}

	public static final BlockType[] BLOCKS = new BlockType[] { GRASS, DIRT, STONE, LOG, LEAVES, BRICKS, BEDROCK, STONEBRICKS, COAL_ORE, COBBLESTONE, SAND, TALLGRASS, PINK_WOOL, PLANKS };

	@Getter private final Vector2i left, right, front, back, top, bottom;
	@Getter private final boolean breakable, transparent;
	@Getter private boolean useXModel = false;

	private BlockType(final Vector2i side, final Vector2i top, final Vector2i bottom, final boolean breakable, final boolean transparent) {

		this(side, side, side, side, top, bottom, breakable, transparent, false);

	}

	private BlockType(final Vector2i tex, final boolean breakable, final boolean transparent) {

		this(tex, tex, tex, breakable, transparent);

	}

	private BlockType() {

		this(null, true, false);

	}

}
