package cc.antho.clonecraft.core.packet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public final class PlayerConnectPacket extends Packet {

	private static final long serialVersionUID = 1L;

	public int id;

}
