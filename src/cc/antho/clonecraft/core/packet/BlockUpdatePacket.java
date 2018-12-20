package cc.antho.clonecraft.core.packet;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class BlockUpdatePacket extends Packet {

	private static final long serialVersionUID = 1L;

	public final int x, y, z;
	public final String type;

	public BlockUpdatePacket() {

		x = 0;
		y = 0;
		z = 0;
		type = null;

	}

}
