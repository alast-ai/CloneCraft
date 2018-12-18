package cc.antho.clonecraft.core;

import com.esotericsoftware.kryo.Kryo;

import cc.antho.clonecraft.client.world.BlockType;
import cc.antho.clonecraft.common.packet.BlockUpdatePacket;
import cc.antho.clonecraft.common.packet.Packet;

public class ClassRegister {

	public static void register(final Kryo kryo) {

		kryo.register(BlockType.class);

		kryo.register(Packet.class);
		kryo.register(BlockUpdatePacket.class);

	}

}
