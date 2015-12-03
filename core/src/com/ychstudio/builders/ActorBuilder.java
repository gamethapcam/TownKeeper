package com.ychstudio.builders;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ychstudio.ecs.components.AnimationComponent;
import com.ychstudio.ecs.components.FarmComponent;
import com.ychstudio.ecs.components.LifeComponent;
import com.ychstudio.ecs.components.PlayerComponent;
import com.ychstudio.ecs.components.RendererComponent;
import com.ychstudio.ecs.components.RigidBodyComponent;
import com.ychstudio.ecs.components.StateComponent;
import com.ychstudio.ecs.components.TentComponent;
import com.ychstudio.ecs.components.TransformComponent;
import com.ychstudio.ecs.components.VillagerComponent;
import com.ychstudio.gamesys.GameManager;

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
        // update GameManager playerCurrentPos
        GameManager.playerCurrentPos.set(x, y);

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.linearDamping = 6f;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(PlayerComponent.radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = GameManager.PLAYER_BIT;
        fixtureDef.filter.maskBits = GameManager.WALL_BIT;

        body.createFixture(fixtureDef);
        circleShape.dispose();

        TextureRegion playerTextureRegion = assetManager.get("img/actors.pack", TextureAtlas.class)
                .findRegion("Player");
        TextureRegion textureRegion = new TextureRegion(playerTextureRegion, 32, 0, 32, 32);

        AnimationComponent animationComponent = new AnimationComponent();
        Animation animation;

        Array<TextureRegion> keyFrames = new Array<>();
        // IDLE
        keyFrames.add(textureRegion);
        animation = new Animation(0.1f, keyFrames, PlayMode.LOOP);
        animationComponent.putAnimation(PlayerComponent.IDLE, animation);

        keyFrames.clear();

        // MOVE
        for (int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(playerTextureRegion, 32 * i, 0, 32, 32));
        }
        animation = new Animation(0.1f, keyFrames, PlayMode.LOOP_PINGPONG);
        animationComponent.putAnimation(PlayerComponent.MOVE, animation);

        Entity entity = new Entity();
        entity.add(new PlayerComponent());
        entity.add(new LifeComponent(20));
        entity.add(new TransformComponent());
        entity.add(new RigidBodyComponent(body));
        entity.add(new RendererComponent(textureRegion, 1f, 1f));
        entity.add(new StateComponent(PlayerComponent.IDLE));
        entity.add(animationComponent);

        engine.addEntity(entity);

        body.setUserData(entity);

    }

    public void createTent(float x, float y) {
        Entity entity = new Entity();
        entity.add(new TentComponent(new Vector2(x, y)));
        engine.addEntity(entity);
    }

    public void createVillager(TentComponent tent) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(tent.pos);
        bodyDef.linearDamping = 6f;

        Body body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(VillagerComponent.radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = GameManager.PLAYER_BIT;
        fixtureDef.filter.maskBits = GameManager.WALL_BIT;

        body.createFixture(fixtureDef);
        circleShape.dispose();

        TextureRegion villagerTextureRegion = assetManager.get("img/actors.pack", TextureAtlas.class)
                .findRegion("Villager");
        TextureRegion textureRegion = new TextureRegion(villagerTextureRegion, 32, 0, 32, 32);

        AnimationComponent animationComponent = new AnimationComponent();
        Animation animation;

        Array<TextureRegion> keyFrames = new Array<>();
        // IDLE
        keyFrames.add(textureRegion);
        animation = new Animation(0.1f, keyFrames, PlayMode.LOOP);
        animationComponent.putAnimation(VillagerComponent.IDLE, animation);

        keyFrames.clear();
        // WANDER
        for (int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(villagerTextureRegion, i * 32, 0, 32, 32));
        }
        animation = new Animation(0.1f, keyFrames, PlayMode.LOOP_PINGPONG);
        animationComponent.putAnimation(VillagerComponent.WANDER, animation);

        Entity entity = new Entity();
        entity.add(new VillagerComponent(tent));
        entity.add(new RigidBodyComponent(body));
        entity.add(new LifeComponent(5));
        entity.add(new TransformComponent());
        entity.add(new RendererComponent(textureRegion, 1f, 1f));
        entity.add(new StateComponent(VillagerComponent.IDLE));
        entity.add(animationComponent);

        engine.addEntity(entity);

        body.setUserData(entity);

    }

    public void createFarm(Rectangle rectangle) {
        Entity entity = new Entity();
        entity.add(new FarmComponent(rectangle));
        engine.addEntity(entity);
    }

}
