package cc.antho.clonecraft.core.log;

import java.util.Objects;

public abstract class Logger {

	public static Logger logger;

	public static boolean use_warn = true;
	public static boolean use_error = true;
	public static boolean use_debug = true;
	public static boolean use_info = true;

	public abstract void _warn(String s);

	public abstract void _error(String s);

	public abstract void _debug(String s);

	public abstract void _info(String s);

	public static void warn(final String s) {

		if (!use_warn) return;
		Objects.requireNonNull(logger)._warn(s);

	}

	public static void error(final String s) {

		if (!use_error) return;
		Objects.requireNonNull(logger)._error(s);

	}

	public static void debug(final String s) {

		if (!use_debug) return;
		Objects.requireNonNull(logger)._debug(s);

	}

	public static void info(final String s) {

		if (!use_info) return;
		Objects.requireNonNull(logger)._info(s);

	}

	public static void warn(final int s) {

		warn(s + "");

	}

	public static void error(final int s) {

		error(s + "");

	}

	public static void debug(final int s) {

		debug(s + "");

	}

	public static void info(final int s) {

		info(s + "");

	}

	public static void warn(final float s) {

		warn(s + "");

	}

	public static void error(final float s) {

		error(s + "");

	}

	public static void debug(final float s) {

		debug(s + "");

	}

	public static void info(final float s) {

		info(s + "");

	}

}
