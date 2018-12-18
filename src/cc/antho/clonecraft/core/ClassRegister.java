package cc.antho.clonecraft.core;

import org.joml.Vector2i;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.core.packet.BlockUpdatePacket;
import cc.antho.clonecraft.core.packet.Packet;

public final class ClassRegister {

	public static final void register(final Kryo kryo) {

		kryo.register(Vector2i.class);

		kryo.register(BlockType.class);

		kryo.register(Packet.class);
		kryo.register(BlockUpdatePacket.class);

	}

	public static final void register(final Server server) {

		register(server.getKryo());

	}

	public static final void register(final Client client) {

		register(client.getKryo());

	}

	private ClassRegister() {

	}

}
