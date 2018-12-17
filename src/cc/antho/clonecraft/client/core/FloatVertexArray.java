package cc.antho.clonecraft.client.core;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

public class FloatVertexArray {

	private final List<RawFloat> data = new ArrayList<>();

	public FloatVertexArray append(final float[] data) {

		for (final float f : data)
			this.data.add(new RawFloat(f));

		return this;

	}

	public FloatVertexArray append(final FloatVertexArray data) {

		this.data.addAll(data.data);

		return this;

	}

	public FloatVertexArray add(final float x, final float y, final float z) {

		for (int i = 0; i < data.size(); i += 3) {

			data.get(i + 0).value += x;
			data.get(i + 1).value += y;
			data.get(i + 2).value += z;

		}

		return this;

	}

	public FloatVertexArray add(final float x, final float y) {

		for (int i = 0; i < data.size(); i += 2) {

			data.get(i + 0).value += x;
			data.get(i + 1).value += y;

		}

		return this;

	}

	public FloatVertexArray mul(final float x, final float y) {

		for (int i = 0; i < data.size(); i += 2) {

			data.get(i + 0).value *= x;
			data.get(i + 1).value *= y;

		}

		return this;

	}

	public int size() {

		return data.size();

	}

	public float[] get() {

		final float[] array = new float[data.size()];

		for (int i = 0; i < array.length; i++)
			array[i] = data.get(i).value;

		return array;

	}

	public void add(final Vector2i vec) {

		add(vec.x, vec.y);

	}

	public void clear() {

		data.clear();

	}

}
