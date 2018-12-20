package cc.antho.clonecraft.client.world;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2i;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockType implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final Map<String, BlockType> types = new HashMap<>();

	public static final BlockType getBlock(final String name) {

		if (name == null) return null;
		if (!types.containsKey(name)) throw new RuntimeException("Block \"" + name + "\" does not exist!");
		return types.get(name);

	}

	public static final List<BlockType> getBlocks() {

		return new ArrayList<>(types.values());

	}

	static {

		types.put("core.grass", new BlockType(new Vector2i(0, 1), new Vector2i(1, 1), new Vector2i(1, 0), true, false, false));
		types.put("core.dirt", new BlockType(new Vector2i(1, 0), true, false));
		types.put("core.stone", new BlockType(new Vector2i(2, 0), true, false));
		types.put("core.log", new BlockType(new Vector2i(2, 1), new Vector2i(3, 0), new Vector2i(3, 0), true, false, false));
		types.put("core.leaves", new BlockType(new Vector2i(3, 1), true, true));
		types.put("core.bricks", new BlockType(new Vector2i(0, 2), true, false));
		types.put("core.bedrock", new BlockType(new Vector2i(1, 2), false, false));
		types.put("core.stonebricks", new BlockType(new Vector2i(2, 2), true, false));
		types.put("core.coal_ore", new BlockType(new Vector2i(3, 2), true, false));
		types.put("core.cobblestone", new BlockType(new Vector2i(0, 3), true, false));
		types.put("core.sand", new BlockType(new Vector2i(1, 3), true, false));
		types.put("core.tallgrass", new BlockType(new Vector2i(2, 3), true, true, true));
		types.put("core.pink_wool", new BlockType(new Vector2i(3, 3), true, false));
		types.put("core.planks", new BlockType(new Vector2i(4, 0), true, false));

	}

	@Getter private final Vector2i left, right, front, back, top, bottom;
	@Getter private final boolean breakable, transparent;
	@Getter private final boolean useXModel;

	private BlockType(final Vector2i side, final Vector2i top, final Vector2i bottom, final boolean breakable, final boolean transparent, final boolean usexmdl) {

		this(side, side, side, side, top, bottom, breakable, transparent, usexmdl);

	}

	private BlockType(final Vector2i tex, final boolean breakable, final boolean transparent, final boolean usexmdl) {

		this(tex, tex, tex, breakable, transparent, usexmdl);

	}

	private BlockType(final Vector2i tex, final boolean breakable, final boolean transparent) {

		this(tex, tex, tex, breakable, transparent, false);

	}

	private BlockType() {

		this(null, true, false);

	}

}
