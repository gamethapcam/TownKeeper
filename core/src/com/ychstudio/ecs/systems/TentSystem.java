package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.ychstudio.ecs.components.TentComponent;

public class TentSystem extends IteratingSystem {

    protected ComponentMapper<TentComponent> tentM = ComponentMapper.getFor(TentComponent.class);

    public TentSystem() {
        super(Family.all(TentComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        TentComponent tent = tentM.get(entity);

        tent.countDown -= deltaTime;

        if (tent.countDown <= 0) {
            if (!tent.isFull()) {
                tent.currentCitizens++;
                System.out.println(getClass().getName() + " spawned a citizen at " + tent.pos);
            } else {
                System.out.println(getClass().getName() + " tent is full!!!");
            }

            tent.countDown = tent.spawnTime;
        }

    }

}
