package cc.antho.clonecraft.core.event;

import java.util.ArrayList;
import java.util.List;

public class EventDispatcher {

	private static List<EventListener> listeners = new ArrayList<>();

	public static void addListener(final EventListener listener) {

		if (!listeners.contains(listener)) listeners.add(listener);

	}

	public static void removeListener(final EventListener listener) {

		if (listeners.contains(listener)) listeners.remove(listener);

	}

	public static void dispatch(final Event event) {

		listeners.forEach(listener -> listener.onEvent(event));

	}

}
