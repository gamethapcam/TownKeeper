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
import com.ychstudio.ai.AStarPathFinding;
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
            villager.resetRandomTimer();
            state.setState(chooseRandom(VillagerComponent.IDLE, VillagerComponent.WANDER));
            if (state.getState() == VillagerComponent.WANDER) {
                // set a new target position
                setNewTargetPos(villager, body.getPosition(), 3f, 6f);
            }
        }

        switch (state.getState()) {
            case VillagerComponent.IDLE:
                state.setState(VillagerComponent.IDLE);
                body.setLinearVelocity(0, 0);
                break;
            case VillagerComponent.WANDER:
                state.setState(VillagerComponent.WANDER);

                if (villager.pathNode == null) {
                    villager.makeRandomTimerUp();
                    break;
                }

                if (body.getPosition().dst(villager.pathNode.getPosition()) < 0.1f) {
                    villager.pathNode = villager.pathNode.nextNode;
                    break;
                }

                villager.targetDir.set(villager.pathNode.getPosition().x - body.getPosition().x,
                        villager.pathNode.getPosition().y - body.getPosition().y);

                body.applyLinearImpulse(tmpV1.set(villager.targetDir).scl(villager.speed), body.getWorldCenter(), true);
                if (body.getLinearVelocity().len2() > villager.maxSpeed * villager.maxSpeed) {
                    tmpV1.set(body.getLinearVelocity());
                    tmpV1.setLength(villager.maxSpeed);
                    body.setLinearVelocity(tmpV1);
                }
                break;
            default:
                break;
        }

    }

    protected boolean checkHitWall(VillagerComponent villager, Body body) {
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

    private void setNewTargetPos(VillagerComponent villager, Vector2 currentPos, float minRadius, float maxRadius) {
        AStarPathFinding aStarPathFinding = AStarPathFinding.getInstance();

        float angle = MathUtils.random(360);
        float radius = MathUtils.random(minRadius, maxRadius);

        tmpV1.set(villager.tent.pos.x + MathUtils.sinDeg(angle) * radius,
                villager.tent.pos.y + MathUtils.cosDeg(angle) * radius);

        while (!aStarPathFinding.isWalkableAt(tmpV1)) {
            angle = MathUtils.random(360);
            radius = MathUtils.random(minRadius, maxRadius);
            tmpV1.set(villager.tent.pos.x + MathUtils.sinDeg(angle) * radius,
                    villager.tent.pos.y + MathUtils.cosDeg(angle) * radius);
        }

        villager.targetPos.set(tmpV1);
        villager.pathNode = aStarPathFinding.findPath(currentPos, tmpV1);
    }

    protected int chooseRandom(int... choices) {
        return choices[MathUtils.random(choices.length - 1)];
    }

}
