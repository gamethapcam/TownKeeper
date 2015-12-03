package com.ychstudio.ecs.systems;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.ychstudio.ecs.components.LifeComponent;
import com.ychstudio.ecs.components.PlayerComponent;
import com.ychstudio.ecs.components.RigidBodyComponent;
import com.ychstudio.ecs.components.StateComponent;
import com.ychstudio.ecs.components.TransformComponent;
import com.ychstudio.gamesys.GameManager;

public class PlayerSystem extends IteratingSystem {

    protected ComponentMapper<PlayerComponent> playerM = ComponentMapper.getFor(PlayerComponent.class);
    protected ComponentMapper<LifeComponent> lifeM = ComponentMapper.getFor(LifeComponent.class);
    protected ComponentMapper<RigidBodyComponent> rigidBodyM = ComponentMapper.getFor(RigidBodyComponent.class);
    protected ComponentMapper<StateComponent> stateM = ComponentMapper.getFor(StateComponent.class);
    protected ComponentMapper<TransformComponent> transformM = ComponentMapper.getFor(TransformComponent.class);

    private final Vector2 tmpV = new Vector2();

    public PlayerSystem() {
        super(Family.all(PlayerComponent.class, LifeComponent.class, RigidBodyComponent.class, StateComponent.class,
                TransformComponent.class).get());
    }

    @Override
    protected void processEntity(Entity entity, float deltaTime) {
        PlayerComponent player = playerM.get(entity);
        LifeComponent life = lifeM.get(entity);
        RigidBodyComponent rigidBody = rigidBodyM.get(entity);
        StateComponent state = stateM.get(entity);
        TransformComponent transform = transformM.get(entity);

        Body body = rigidBody.body;

        // Controls
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            body.applyLinearImpulse(tmpV.set(0, player.speed), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            body.applyLinearImpulse(tmpV.set(0, -player.speed), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            body.applyLinearImpulse(tmpV.set(-player.speed, 0), body.getWorldCenter(), true);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            body.applyLinearImpulse(tmpV.set(player.speed, 0), body.getWorldCenter(), true);
        }

        // limit velocity
        if (body.getLinearVelocity().len2() > player.maxSpeed * player.maxSpeed) {
            tmpV.set(body.getLinearVelocity());
            tmpV.nor();
            body.setLinearVelocity(tmpV);
        }

        if (body.getLinearVelocity().len2() > 0.1f) {
            state.setState(PlayerComponent.MOVE);
        } else {
            state.setState(PlayerComponent.IDLE);
        }

        // limit player's moving area
        if (body.getPosition().x < GameManager.moveBound.x + PlayerComponent.radius) {
            body.setTransform(GameManager.moveBound.x + PlayerComponent.radius, body.getPosition().y,
                    body.getAngle());
        } else if (body.getPosition().x > GameManager.moveBound.width - PlayerComponent.radius) {
            body.setTransform(GameManager.moveBound.width - PlayerComponent.radius, body.getPosition().y,
                    body.getAngle());
        }
        if (body.getPosition().y < GameManager.moveBound.y + PlayerComponent.radius) {
            body.setTransform(body.getPosition().x, GameManager.moveBound.y + PlayerComponent.radius,
                    body.getAngle());
        } else if (body.getPosition().y > GameManager.moveBound.height - PlayerComponent.radius) {
            body.setTransform(body.getPosition().x, GameManager.moveBound.height - PlayerComponent.radius,
                    body.getAngle());
        }

        // update player's location to GameManager for the camera to follow
        GameManager.playerCurrentPos.set(body.getPosition().x, body.getPosition().y);
    }

}
