package cc.antho.clonecraft.core.math;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

public final class Mathf {

	public static final float E = 2.71828182f;
	public static final float PI = 3.14159265f;
	public static final float TAU = 2f * PI;

	private Mathf() {

	}

	public static final float fract(final float value) {

		return value - floor(value);

	}

	public static final float clampMin(final float value, final float min) {

		if (value <= min) return min;
		return value;

	}

	public static final float clampMax(final float value, final float max) {

		if (value >= max) return max;
		return value;

	}

	public static final float clamp(final float value, final float min, final float max) {

		return clampMin(clampMax(value, max), min);

	}

	public static final float mix(final float a, final float b, final float percent) {

		return a * (1f - percent) + b * percent;

	}

	public static final Vector2f mix(final Vector2f a, final Vector2f b, final float percent) {

		return new Vector2f(mix(a.x, b.x, percent), mix(a.y, b.y, percent));

	}

	public static final Vector3f mix(final Vector3f a, final Vector3f b, final float percent) {

		return new Vector3f(mix(a.x, b.x, percent), mix(a.y, b.y, percent), mix(a.z, b.z, percent));

	}

	public static final Vector4f mix(final Vector4f a, final Vector4f b, final float percent) {

		return new Vector4f(mix(a.x, b.x, percent), mix(a.y, b.y, percent), mix(a.z, b.z, percent), mix(a.w, b.w, percent));

	}

	public static final int ceil(final float a) {

		return (int) Math.ceil(a);

	}

	public static final int floor(final float a) {

		return (int) Math.floor(a);

	}

	public static final int round(final float a) {

		return Math.round(a);

	}

	public static final float abs(final float value) {

		return value < 0 ? -value : value;

	}

	public static final float min(final float a, final float b) {

		return a < b ? a : b;

	}

	public static final float max(final float a, final float b) {

		return a > b ? a : b;

	}

	public static final float sin(final float a) {

		return (float) Math.sin(a);

	}

	public static final float cos(final float a) {

		return (float) Math.cos(a);

	}

	public static final float tan(final float a) {

		return (float) Math.tan(a);

	}

	public static final float sinNormalized(final float a) {

		return sin(TAU * a);

	}

	public static final float cosNormalized(final float a) {

		return cos(TAU * a);

	}

	public static final float tanNormalized(final float a) {

		return tan(TAU * a);

	}

	public static final float toRadians(final float a) {

		return a / 180f * PI;

	}

	public static final float toDegrees(final float a) {

		return a * 180f / PI;

	}

	public static float sqrt(final float a) {

		return (float) Math.sqrt(a);

	}

	public static float cbrt(final float a) {

		return (float) Math.cbrt(a);

	}

	public static float pow(final float a, final float b) {

		return (float) Math.pow(a, b);

	}

	public static final float smoothstep(final float edge0, final float edge1, final float x) {

		final float value = clamp((x - edge0) / (edge1 - edge0), 0f, 1f);

		return value * value * (3f - 2f * value);

	}

	public static final float approach(final float current, final float goal, final float delta) {

		final float difference = goal - current;

		if (difference > delta) return current + delta;
		if (difference < -delta) return current - delta;

		return goal;

	}

	public static final float approachSmooth(final float current, final float goal, final float delta) {

		return approach(current, goal, abs(current - goal) * delta);

	}

	public static final float map(final float value, final float start1, final float end1, final float start2, final float end2) {

		return (value - start1) / (end1 - start1) * (end2 - start2) + start2;

	}

	public static float mapClamped(final float value, final float start1, final float end1, final float start2, final float end2) {

		return clamp(map(value, start1, end1, start2, end2), start2, end2);

	}

	public static final float log(final float a, final int base) {

		return (float) (Math.log(a) / Math.log(base));

	}

	private static final float t(final float x, final float edge0, final float edge1) {

		return (x - edge0) / (edge1 - edge0);

	}

	public static final float smoothStep(final float x, final float edge0, final float edge1) {

		final float t = t(x, edge0, edge1);

		return 2f * pow(t, 2) * (-t + 1.5f);

	}

	public static final float smootherStep(final float x, final float edge0, final float edge1) {

		final float t = t(x, edge0, edge1);

		return 6f * pow(t, 3) * (pow(t, 2) - 2.5f * t + 5f / 3f);

	}

	public static final float smoothestStep(final float x, final float edge0, final float edge1) {

		final float t = t(x, edge0, edge1);

		return 20f * pow(t, 4) * (-pow(t, 3) + 3.5f * pow(t, 2) - 4.2f * t + 1.75f);

	}

	public static float barryCentric(final Vector3f p1, final Vector3f p2, final Vector3f p3, final Vector2f pos) {

		final float det = (p2.z - p3.z) * (p1.x - p3.x) + (p3.x - p2.x) * (p1.z - p3.z);
		final float l1 = ((p2.z - p3.z) * (pos.x - p3.x) + (p3.x - p2.x) * (pos.y - p3.z)) / det;
		final float l2 = ((p3.z - p1.z) * (pos.x - p3.x) + (p1.x - p3.x) * (pos.y - p3.z)) / det;

		final float l3 = 1.0f - l1 - l2;
		return l1 * p1.y + l2 * p2.y + l3 * p3.y;

	}

}
