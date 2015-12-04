package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ychstudio.ecs.components.AnimalComponent;
import com.ychstudio.ecs.components.LifeComponent;
import com.ychstudio.ecs.components.RigidBodyComponent;
import com.ychstudio.ecs.components.StateComponent;

public class AnimalSystem extends IteratingSystem {

    protected ComponentMapper<AnimalComponent> animalM = ComponentMapper.getFor(AnimalComponent.class);
    protected ComponentMapper<RigidBodyComponent> rigidBodyM = ComponentMapper.getFor(RigidBodyComponent.class);
    protected ComponentMapper<LifeComponent> lifeM = ComponentMapper.getFor(LifeComponent.class);
    protected ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);

    public AnimalSystem() {
        super(Family.all(AnimalComponent.class, RigidBodyComponent.class, LifeComponent.class, StateComponent.class)
                .get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        AnimalComponent animal = animalM.get(entity);
        RigidBodyComponent rigidBody = rigidBodyM.get(entity);
        LifeComponent life = lifeM.get(entity);
        StateComponent state = stateM.get(entity);
        
        // TODO add AI

    }

}
