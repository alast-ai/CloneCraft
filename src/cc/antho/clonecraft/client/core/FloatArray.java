package cc.antho.clonecraft.client.core;

import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2i;

import cc.antho.clonecraft.core.math.RawFloat;

public class FloatArray {

	private final List<RawFloat> data = new ArrayList<>();

	public FloatArray append(final float[] data) {

		for (final float f : data)
			this.data.add(new RawFloat(f));

		return this;

	}

	public FloatArray append(final FloatArray data) {

		this.data.addAll(data.data);

		return this;

	}

	public FloatArray add(final float x, final float y, final float z) {

		for (int i = 0; i < data.size(); i += 3) {

			data.get(i + 0).value += x;
			data.get(i + 1).value += y;
			data.get(i + 2).value += z;

		}

		return this;

	}

	public FloatArray add(final float x, final float y) {

		for (int i = 0; i < data.size(); i += 2) {

			data.get(i + 0).value += x;
			data.get(i + 1).value += y;

		}

		return this;

	}

	public FloatArray mul(final float x, final float y) {

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
