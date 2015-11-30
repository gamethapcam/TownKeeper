package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ychstudio.ecs.components.AnimationComponent;
import com.ychstudio.ecs.components.RendererComponent;
import com.ychstudio.ecs.components.StateComponent;

public class AnimationSystem extends IteratingSystem {

    protected ComponentMapper<AnimationComponent> animationM = ComponentMapper.getFor(AnimationComponent.class);
    protected ComponentMapper<RendererComponent> rendererM = ComponentMapper.getFor(RendererComponent.class);
    protected ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);

    public AnimationSystem() {
        super(Family.all(AnimationComponent.class, RendererComponent.class, StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {

    }

}
