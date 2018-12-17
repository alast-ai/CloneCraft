package cc.antho.clonecraft.client.world;

import org.joml.Vector2i;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BlockType {

	public static final BlockType GRASS = new BlockType(
			new Vector2i(0, 1),
			new Vector2i(1, 1),
			new Vector2i(1, 0));

	public static final BlockType DIRT = new BlockType(
			new Vector2i(1, 0));

	public static final BlockType STONE = new BlockType(
			new Vector2i(2, 0));

	@Getter private final Vector2i left, right, front, back, top, bottom;

	private BlockType(final Vector2i side, final Vector2i top, final Vector2i bottom) {

		this(side, side, side, side, top, bottom);

	}

	private BlockType(final Vector2i tex) {

		this(tex, tex, tex);

	}

}
