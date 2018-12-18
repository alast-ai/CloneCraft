package cc.antho.clonecraft.core.packet;

import org.joml.Vector3f;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public final class PlayerMovePacket extends Packet {

	private static final long serialVersionUID = 1L;

	public int id;
	public Vector3f position;
	public Vector3f rotation;

}
