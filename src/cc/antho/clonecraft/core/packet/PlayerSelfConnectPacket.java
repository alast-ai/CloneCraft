package cc.antho.clonecraft.core.packet;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public final class PlayerSelfConnectPacket extends Packet {

	private static final long serialVersionUID = 1L;

	public float playerPacketFreq;

}
