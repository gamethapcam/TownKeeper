package com.ychstudio.builders;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.ychstudio.ecs.components.PlayerComponent;
import com.ychstudio.ecs.components.RendererComponent;
import com.ychstudio.ecs.components.RigidBodyComponent;
import com.ychstudio.ecs.components.StateComponent;
import com.ychstudio.ecs.components.TransformComponent;

import gamesys.GameManager;

public class ActorBuilder {
    private static final ActorBuilder instance = new ActorBuilder();

    private World world;
    private Engine engine;

    private AssetManager assetManager = GameManager.assetManager;

    private ActorBuilder() {
    }

    public static ActorBuilder getInstance(World world, Engine engine) {
	instance.world = world;
	instance.engine = engine;
	return instance;
    }

    public void createPlayer(float x, float y) {

	BodyDef bodyDef = new BodyDef();
	bodyDef.type = BodyType.DynamicBody;
	bodyDef.linearDamping = 6f;
	bodyDef.position.set(x, y);

	Body body = world.createBody(bodyDef);

	CircleShape circleShape = new CircleShape();
	circleShape.setRadius(0.3f);

	FixtureDef fixtureDef = new FixtureDef();
	fixtureDef.shape = circleShape;
	fixtureDef.filter.categoryBits = GameManager.PLAYER_BIT;
	fixtureDef.filter.maskBits = GameManager.WALL_BIT;

	body.createFixture(fixtureDef);

	TextureAtlas textureAtlas = assetManager.get("img/actors.pack", TextureAtlas.class);
	TextureRegion textureRegion = new TextureRegion(textureAtlas.findRegion("citizen"), 0, 0, 32, 32);

	Entity entity = new Entity();
	entity.add(new PlayerComponent());
	entity.add(new TransformComponent());
	entity.add(new RigidBodyComponent(body));
	entity.add(new RendererComponent(textureRegion, 1f, 1f));
	entity.add(new StateComponent(0));

	engine.addEntity(entity);

	body.setUserData(entity);

	circleShape.dispose();
    }

}
