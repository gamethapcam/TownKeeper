package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.physics.box2d.World;
import com.ychstudio.ecs.components.FarmComponent;

public class FarmSystem extends IteratingSystem {

    protected ComponentMapper<FarmComponent> farmM = ComponentMapper.getFor(FarmComponent.class);

    private final World world;

    public FarmSystem(World world) {
        super(Family.all(FarmComponent.class).get());
        this.world = world;
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        FarmComponent farm = farmM.get(entity);

        farm.countDown -= deltaTime;

        if (farm.isTimeUp()) {
            farm.resetCountDown();

            if (!farm.isFull()) {
                farm.currentAnimals++;
                // TODO spawn animal
                System.out.println("animal spawned");
            }
        }
    }

}
