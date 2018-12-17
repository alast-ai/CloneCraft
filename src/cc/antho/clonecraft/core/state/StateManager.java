package cc.antho.clonecraft.core.state;

import java.util.Objects;

import lombok.Getter;

public class StateManager {

	@Getter private State currentState;

	public void setState(final State newState) {

		if (currentState != null) {

			currentState.shutdown();
			currentState.manager = null;

		}

		currentState = newState;

		if (currentState != null) {

			currentState.manager = this;
			currentState.init();

		}

	}

	public void tick() {

		Objects.requireNonNull(currentState).tick();

	}

	public void render() {

		Objects.requireNonNull(currentState).render();

	}

}
