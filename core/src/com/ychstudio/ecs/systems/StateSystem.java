package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ychstudio.ecs.components.State;

public class StateSystem extends IteratingSystem {

	protected ComponentMapper<State> stateM = ComponentMapper.getFor(State.class);
	
	public StateSystem() {
		super(Family.all(State.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		State state = stateM.get(entity);
		
		state.addStateTime(deltaTime);
		
	}

}
