package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.Body;
import com.ychstudio.ecs.components.RigidBody;
import com.ychstudio.ecs.components.Transform;

public class PhysicsSystem extends IteratingSystem {

	protected ComponentMapper<Transform> transformM = ComponentMapper.getFor(Transform.class);
	protected ComponentMapper<RigidBody> rigidBodyM = ComponentMapper.getFor(RigidBody.class);
	
	public PhysicsSystem() {
		super(Family.all(Transform.class, RigidBody.class).get());
	}

	@Override
	protected void processEntity(Entity entity, float deltaTime) {
		Body body = rigidBodyM.get(entity).body;
		Transform transform = transformM.get(entity);

		transform.setPos(body.getPosition());
		transform.rot = body.getAngle();
	}

}
