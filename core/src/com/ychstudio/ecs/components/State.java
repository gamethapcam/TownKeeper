package com.ychstudio.ecs.components;

import com.badlogic.ashley.core.Component;

public class State implements Component {

	private float stateTime;
	public int currentState;
	
	public State(int currentState) {
		this.currentState = currentState;
		stateTime = 0;
	}
	
	public float getStateTime() {
		return stateTime;
	}
	
	public void addStateTime(float delta) {
		stateTime += delta;
	}
	
	public void resetStateTime() {
		stateTime = 0;
	}
}
