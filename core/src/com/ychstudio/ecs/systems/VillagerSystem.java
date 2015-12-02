package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ychstudio.ecs.components.LifeComponent;
import com.ychstudio.ecs.components.RigidBodyComponent;
import com.ychstudio.ecs.components.StateComponent;
import com.ychstudio.ecs.components.VillagerComponent;

public class VillagerSystem extends IteratingSystem {

    protected ComponentMapper<VillagerComponent> villagerM = ComponentMapper.getFor(VillagerComponent.class);
    protected ComponentMapper<LifeComponent> lifeM = ComponentMapper.getFor(LifeComponent.class);
    protected ComponentMapper<RigidBodyComponent> rigidBodyM = ComponentMapper.getFor(RigidBodyComponent.class);
    protected ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);

    private final Vector2 tmpV = new Vector2();

    public VillagerSystem() {
        super(Family.all(VillagerComponent.class, LifeComponent.class, RigidBodyComponent.class, StateComponent.class)
                .get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        VillagerComponent villager = villagerM.get(entity);
        LifeComponent life = lifeM.get(entity);
        StateComponent state = stateM.get(entity);
        RigidBodyComponent rigidBody = rigidBodyM.get(entity);
        Body body = rigidBody.body;

        villager.updateRandomTimer(deltaTime);
        if (villager.isRandomTimerUp()) {
            state.setState(chooseRandom(VillagerComponent.IDLE, VillagerComponent.WANDER));
            if (state.getState() == VillagerComponent.WANDER) {
                villager.targetDir.set(MathUtils.randomTriangular(), MathUtils.randomTriangular());
            }
            villager.resetRandomTimer();
        }

        switch (state.getState()) {
            case VillagerComponent.IDLE:
                state.setState(VillagerComponent.IDLE);
                body.setLinearVelocity(0, 0);
                break;
            case VillagerComponent.WANDER:
                state.setState(VillagerComponent.WANDER);
                body.applyLinearImpulse(tmpV.set(villager.targetDir).scl(villager.speed), body.getWorldCenter(), true);
                if (body.getLinearVelocity().len2() > villager.maxSpeed * villager.maxSpeed) {
                    tmpV.set(body.getLinearVelocity());
                    tmpV.setLength(villager.maxSpeed);
                    body.setLinearVelocity(tmpV);
                }
                break;
            default:
                break;
        }

    }

    protected int chooseRandom(int... choices) {
        return choices[MathUtils.random(choices.length - 1)];
    }

}
