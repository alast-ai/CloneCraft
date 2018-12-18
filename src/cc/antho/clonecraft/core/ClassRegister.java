package cc.antho.clonecraft.core;

import com.esotericsoftware.kryo.Kryo;

import cc.antho.clonecraft.client.world.BlockType;

public class ClassRegister {

	public static void register(final Kryo kryo) {

		kryo.register(BlockType.class);

	}

}
