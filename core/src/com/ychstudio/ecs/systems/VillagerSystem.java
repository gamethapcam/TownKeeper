package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ychstudio.ecs.components.RigidBodyComponent;
import com.ychstudio.ecs.components.StateComponent;
import com.ychstudio.ecs.components.VillagerComponent;

public class VillagerSystem extends IteratingSystem {

    protected ComponentMapper<VillagerComponent> villagerM = ComponentMapper.getFor(VillagerComponent.class);
    protected ComponentMapper<RigidBodyComponent> rigidBodyM = ComponentMapper.getFor(RigidBodyComponent.class);
    protected ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);

    public VillagerSystem() {
        super(Family.all(VillagerComponent.class, RigidBodyComponent.class, StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VillagerComponent villager = villagerM.get(entity);
        RigidBodyComponent rigidBody = rigidBodyM.get(entity);
        StateComponent state = stateM.get(entity);

        // TODO: villager AI

    }

}
