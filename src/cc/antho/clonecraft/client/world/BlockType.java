package cc.antho.clonecraft.client.world;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Vector2i;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockType {

	private static final Map<String, BlockType> types = new HashMap<>();
	private static List<BlockType> palette = new ArrayList<>();

	public static final BlockType getBlock(final String name) {

		if (name == null) return null;
		if (!types.containsKey(name)) {
			System.err.println("Block \"" + name + "\" does not exist!");
			return getBlock("core.unknown");
		}

		return types.get(name);

	}

	public static final List<BlockType> getBlocks() {

		return new ArrayList<>(types.values());

	}

	public static final List<BlockType> getPalette() {

		return palette;

	}

	public static void registerBlock(final String name, final Vector2i left, final Vector2i right, final Vector2i front, final Vector2i back, final Vector2i top, final Vector2i bottom, final boolean breakable, final boolean transparent,
			final boolean xmodel, final boolean inPalette) {

		final BlockType type = new BlockType(name, left, right, front, back, top, bottom, breakable, transparent, xmodel, inPalette);

		types.put(name, type);
		if (inPalette) palette.add(type);

	}

	@Getter private final String name;
	@Getter private final Vector2i left, right, front, back, top, bottom;
	@Getter private final boolean breakable, transparent;
	@Getter private final boolean useXModel, inPalette;

	private BlockType() {

		this(null, null, null, null, null, null, null, false, false, false, false);

	}

}
