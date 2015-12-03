package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.ychstudio.ecs.components.LifeComponent;
import com.ychstudio.ecs.components.RigidBodyComponent;
import com.ychstudio.ecs.components.StateComponent;
import com.ychstudio.ecs.components.VillagerComponent;
import com.ychstudio.gamesys.GameManager;

public class VillagerSystem extends IteratingSystem {

    protected ComponentMapper<VillagerComponent> villagerM = ComponentMapper.getFor(VillagerComponent.class);
    protected ComponentMapper<LifeComponent> lifeM = ComponentMapper.getFor(LifeComponent.class);
    protected ComponentMapper<RigidBodyComponent> rigidBodyM = ComponentMapper.getFor(RigidBodyComponent.class);
    protected ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);

    private final Vector2 tmpV1 = new Vector2();
    private final Vector2 tmpV2 = new Vector2();
    private boolean hitWall = false;

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
                // set a new target position
                setNewTargetPos(villager, body.getPosition(), 6f);
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

                body.applyLinearImpulse(tmpV1.set(villager.targetDir).scl(villager.speed), body.getWorldCenter(), true);
                if (body.getLinearVelocity().len2() > villager.maxSpeed * villager.maxSpeed) {
                    tmpV1.set(body.getLinearVelocity());
                    tmpV1.setLength(villager.maxSpeed);
                    body.setLinearVelocity(tmpV1);
                }

                if (checkHitWall(villager, body)) {
                    setNewTargetPos(villager, body.getPosition(), 6f);
                }

                // limit villager's moving area
                if (body.getPosition().x < GameManager.moveBound.x + VillagerComponent.radius) {
                    body.setTransform(GameManager.moveBound.x + VillagerComponent.radius, body.getPosition().y,
                            body.getAngle());
                } else if (body.getPosition().x > GameManager.moveBound.width - VillagerComponent.radius) {
                    body.setTransform(GameManager.moveBound.width - VillagerComponent.radius, body.getPosition().y,
                            body.getAngle());
                }
                if (body.getPosition().y < GameManager.moveBound.y + VillagerComponent.radius) {
                    body.setTransform(body.getPosition().x, GameManager.moveBound.y + VillagerComponent.radius,
                            body.getAngle());
                } else if (body.getPosition().y > GameManager.moveBound.height - VillagerComponent.radius) {
                    body.setTransform(body.getPosition().x, GameManager.moveBound.height - VillagerComponent.radius,
                            body.getAngle());
                }
                break;
            default:
                break;
        }

    }

    private boolean checkHitWall(VillagerComponent villager, Body body) {
        hitWall = false;
        RayCastCallback rayCastCallback = new RayCastCallback() {

            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {
                if (fixture.getFilterData().categoryBits == GameManager.WALL_BIT) {
                    hitWall = true;
                }
                return 0;
            }
        };

        tmpV1.set(body.getPosition());
        tmpV2.set(villager.targetDir).setLength(0.5f).add(tmpV1);

        body.getWorld().rayCast(rayCastCallback, tmpV1, tmpV2);
        if (hitWall) {
            return true;
        }

        if (villager.targetDir.x != 0) {
            tmpV2.set(villager.targetDir.x, 0).setLength(0.5f).add(tmpV1);
            body.getWorld().rayCast(rayCastCallback, tmpV1, tmpV2);
            if (hitWall) {
                return hitWall;
            }
        }

        if (villager.targetDir.y != 0) {
            tmpV2.set(0, villager.targetDir.y).setLength(0.5f).add(tmpV1);
            body.getWorld().rayCast(rayCastCallback, tmpV1, tmpV2);
        }

        return hitWall;
    }

    private void setNewTargetPos(VillagerComponent villager, Vector2 currentPos, float radius) {
        float angle = MathUtils.random(360);

        villager.targetPos.set(villager.tent.pos.x + MathUtils.sinDeg(angle) * radius,
                villager.tent.pos.y + MathUtils.cosDeg(angle) * radius);

        villager.targetDir.set(villager.targetPos.x - currentPos.x, villager.targetPos.y - currentPos.y);
    }

    protected int chooseRandom(int... choices) {
        return choices[MathUtils.random(choices.length - 1)];
    }

}
