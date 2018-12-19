package cc.antho.clonecraft.core.events;

import cc.antho.clonecraft.core.event.Event;
import lombok.Getter;

public class FramebufferResizeEvent extends Event {

	@Getter private final int width, height;

	public FramebufferResizeEvent(final Object sender, final int width, final int height) {

		super(sender);

		this.width = width;
		this.height = height;

	}

}
