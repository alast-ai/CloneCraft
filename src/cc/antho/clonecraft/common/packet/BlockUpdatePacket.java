package cc.antho.clonecraft.common.packet;

import cc.antho.clonecraft.client.world.BlockType;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class BlockUpdatePacket extends Packet {

	private static final long serialVersionUID = 1L;

	public final int x, y, z;
	public final BlockType type;

	public BlockUpdatePacket() {

		x = 0;
		y = 0;
		z = 0;
		type = null;

	}

}
