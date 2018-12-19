package cc.antho.clonecraft.core.events;

import cc.antho.clonecraft.core.event.Event;
import cc.antho.clonecraft.core.packet.Packet;

public final class NetworkPacketEvent extends Event {

	public final Packet packet;
	public final boolean fromServer;
	public final boolean tcp;

	public NetworkPacketEvent(final Object sender, final Packet packet, final boolean fromServer, final boolean tcp) {

		super(sender);

		this.packet = packet;
		this.fromServer = fromServer;
		this.tcp = tcp;

	}

}
