package cc.antho.clonecraft.core.packet;

import java.util.List;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public final class ChunkChangesPacket extends Packet {

	private static final long serialVersionUID = 1L;

	public List<BlockUpdatePacket> changes;
	public int x, z;

	public ChunkChangesPacket() {

	}

}
