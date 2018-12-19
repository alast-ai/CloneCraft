package cc.antho.clonecraft.core.state;

import java.util.Objects;

import lombok.Getter;

public class StateManager {

	@Getter private State currentState;
	private State nextState = null;
	private boolean shouldUpdate = false;

	public void updateStates() {

		if (!shouldUpdate) return;
		shouldUpdate = false;

		if (currentState != null) {

			currentState.shutdown();
			currentState.manager = null;

		}

		currentState = nextState;
		nextState = null;

		if (currentState != null) {

			currentState.manager = this;
			currentState.init();

		}

	}

	public void setState(final State newState) {

		nextState = newState;
		shouldUpdate = true;

	}

	public void tick() {

		Objects.requireNonNull(currentState).tick();

	}

	public void render() {

		Objects.requireNonNull(currentState).render();

	}

}
