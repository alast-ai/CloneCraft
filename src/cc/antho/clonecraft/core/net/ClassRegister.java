package cc.antho.clonecraft.core.net;

import java.util.ArrayList;

import org.joml.Vector2i;
import org.joml.Vector3f;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

import cc.antho.clonecraft.core.packet.BlockUpdatePacket;
import cc.antho.clonecraft.core.packet.ChunkChangesPacket;
import cc.antho.clonecraft.core.packet.Packet;
import cc.antho.clonecraft.core.packet.PlayerConnectPacket;
import cc.antho.clonecraft.core.packet.PlayerDisconnectPacket;
import cc.antho.clonecraft.core.packet.PlayerMovePacket;
import cc.antho.clonecraft.core.packet.PlayerSelfConnectPacket;

public final class ClassRegister {

	public static final void register(final Kryo kryo) {

		kryo.register(Vector2i.class);
		kryo.register(Vector3f.class);
		kryo.register(ArrayList.class);

		kryo.register(Packet.class);
		kryo.register(BlockUpdatePacket.class);
		kryo.register(PlayerConnectPacket.class);
		kryo.register(PlayerDisconnectPacket.class);
		kryo.register(PlayerMovePacket.class);
		kryo.register(PlayerSelfConnectPacket.class);
		kryo.register(ChunkChangesPacket.class);

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
