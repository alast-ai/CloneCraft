package cc.antho.clonecraft.core.events;

import cc.antho.clonecraft.core.packet.Packet;
import cc.antho.commons.event.Event;

public final class NetworkPacketEvent extends Event {

	public final Packet packet;
	public final boolean fromServer;
	public final boolean tcp;

	public NetworkPacketEvent(final Packet packet, final boolean fromServer, final boolean tcp) {

		this.packet = packet;
		this.fromServer = fromServer;
		this.tcp = tcp;

	}

}
