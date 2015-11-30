package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ychstudio.ecs.components.PlayerComponent;
import com.ychstudio.ecs.components.RigidBodyComponent;
import com.ychstudio.ecs.components.StateComponent;

public class PlayerSystem extends IteratingSystem {

    protected ComponentMapper<PlayerComponent> playerM = ComponentMapper.getFor(PlayerComponent.class);
    protected ComponentMapper<RigidBodyComponent> rigidBodyM = ComponentMapper.getFor(RigidBodyComponent.class);
    protected ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);

    private final Vector2 tmpV = new Vector2();

    public PlayerSystem() {
        super(Family.all(PlayerComponent.class, RigidBodyComponent.class, StateComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent player = playerM.get(entity);
        RigidBodyComponent rigidBody = rigidBodyM.get(entity);
        StateComponent state = stateM.get(entity);

        Body body = rigidBody.body;

        // Controls
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            body.applyLinearImpulse(tmpV.set(0, player.speed), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            body.applyLinearImpulse(tmpV.set(0, -player.speed), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            body.applyLinearImpulse(tmpV.set(-player.speed, 0), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            body.applyLinearImpulse(tmpV.set(player.speed, 0), body.getWorldCenter(), true);
        }

        // limit velocity
        if (body.getLinearVelocity().len2() > player.maxSpeed * player.maxSpeed) {
            tmpV.set(body.getLinearVelocity());
            tmpV.nor();
            body.setLinearVelocity(tmpV);
        }

    }

}
