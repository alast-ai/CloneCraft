package cc.antho.clonecraft.core.events;

import cc.antho.commons.event.Event;
import lombok.Getter;

public class FramebufferResizeEvent extends Event {

	@Getter private final int width, height;

	public FramebufferResizeEvent(final int width, final int height) {

		this.width = width;
		this.height = height;

	}

}
