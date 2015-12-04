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
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.ychstudio.ecs.components.AnimalComponent;
import com.ychstudio.ecs.components.AnimalComponent.Kind;
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

/**
 * ActorBuilder is used to create objects in the game, such as player,
 * villagers, obstacles, etc.
 * 
 * @author yichen
 *
 */
public class ActorBuilder {
    private static final ActorBuilder instance = new ActorBuilder();

    private World world;
    private Engine engine;

    private AssetManager assetManager = GameManager.assetManager;

    private ActorBuilder() {
    }

    /**
     * get the instance of ActorBuilder
     * 
     * @param world
     *            the Box2D world for ActorBuilder
     * @param engine
     *            the Ashley engine for ActorBuilder
     * @return the instance of ActorBuilder
     */
    public static ActorBuilder getInstance(World world, Engine engine) {
        instance.world = world;
        instance.engine = engine;
        return instance;
    }

    /**
     * create player in the game
     * 
     * @param x
     *            the player's x position
     * @param y
     *            the player's y position
     */
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

    /**
     * create an obstacle by the given rectangle
     * 
     * @param rectangle
     *            the rectangle area of the obstacle
     */
    public void createObstacle(Rectangle rectangle) {
        float x = rectangle.x;
        float y = rectangle.y;

        float[] vertices = new float[4 * 2];

        vertices[0] = 0;
        vertices[1] = 0;
        vertices[2] = 0;
        vertices[3] = 0 + rectangle.height;
        vertices[4] = 0 + rectangle.width;
        vertices[5] = 0 + rectangle.height;
        vertices[6] = 0 + rectangle.width;
        vertices[7] = 0;

        createObstacle(x, y, vertices);
    }

    /**
     * create an obstacle by given x, y, and vertices
     * 
     * @param x
     *            the x position of the obstacle
     * @param y
     *            the y position of the obstacle
     * @param vertices
     *            the vertex array of the obstacle area
     */
    public void createObstacle(float x, float y, float[] vertices) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.StaticBody;
        bodyDef.position.set(x, y);

        Body body = world.createBody(bodyDef);

        PolygonShape polygonShape = new PolygonShape();
        polygonShape.set(vertices);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = polygonShape;
        fixtureDef.filter.categoryBits = GameManager.WALL_BIT;
        fixtureDef.filter.maskBits = GameManager.PLAYER_BIT | GameManager.ANIMAL_BIT;

        body.createFixture(fixtureDef);

        polygonShape.dispose();
    }

    /**
     * create a tent in the game
     * 
     * @param x
     *            the tent's x position
     * @param y
     *            the tent's y position
     */
    public void createTent(float x, float y) {
        Entity entity = new Entity();
        entity.add(new TentComponent(new Vector2(x, y)));
        engine.addEntity(entity);
    }

    /**
     * create a villager in the game
     * 
     * @param tent
     *            the tent that the villager belongs to
     */
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

    /**
     * create a farm in the game
     * 
     * @param rectangle
     *            the farm's rectangle area
     */
    public void createFarm(Rectangle rectangle) {
        Entity entity = new Entity();
        entity.add(new FarmComponent(rectangle));
        engine.addEntity(entity);
    }

    /**
     * create an animal of a random kind
     * 
     * @param x
     *            the x position of the created animal
     * @param y
     *            the y position of the created animal
     */
    public void createAnimal(float x, float y, FarmComponent farm) {
        createAnimal(x, y, farm, null);
    }

    /**
     * create an animal of a assigned kind if kind is not null, otherwise create
     * an animal of a random kind
     * 
     * @param x
     *            the x position of the created animal
     * @param y
     *            the y position of the created animal
     * @param kind
     *            the assigned kind for the animal, if kind is null, the animal
     *            will be create with a random kind
     */
    public void createAnimal(float x, float y, FarmComponent farm, Kind kind) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(x, y);
        bodyDef.linearDamping = 6f;

        Body body = world.createBody(bodyDef);

        CircleShape circleShape = new CircleShape();
        circleShape.setRadius(AnimalComponent.radius);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = circleShape;
        fixtureDef.filter.categoryBits = GameManager.ANIMAL_BIT;
        fixtureDef.filter.maskBits = GameManager.WALL_BIT;

        body.createFixture(fixtureDef);
        circleShape.dispose();

        if (kind == null) {
            kind = Kind.getRandomKind();
        }
        
        TextureRegion animalTextureRegion = null;
        switch (kind) {
            case CHICKEN:
                animalTextureRegion = assetManager.get("img/actors.pack", TextureAtlas.class).findRegion("Chicken");
                break;
            case SHEEP:
                animalTextureRegion = assetManager.get("img/actors.pack", TextureAtlas.class).findRegion("Sheep");
                break;
            case COW:
                animalTextureRegion = assetManager.get("img/actors.pack", TextureAtlas.class).findRegion("Cow");
                break;
            default:
                break;
        }
        
        TextureRegion textureRegion = new TextureRegion(animalTextureRegion, 32, 0, 32, 32);
        
        AnimationComponent animationComponent = new AnimationComponent();
        Array<TextureRegion> keyFrames = new Array<>();
        // Idle
        keyFrames.add(textureRegion);
        Animation animation = new Animation(0.1f, keyFrames, PlayMode.NORMAL);
        animationComponent.putAnimation(AnimalComponent.IDLE, animation);
        
        keyFrames.clear();
        
        // Move up
        for(int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(animalTextureRegion, 32 * i, 32 * 3, 32, 32));
        }
        animation = new Animation(0.1f, keyFrames, PlayMode.LOOP_PINGPONG);
        animationComponent.putAnimation(AnimalComponent.MOVE_UP, animation);
        
        keyFrames.clear();
        
        // Move down
        for(int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(animalTextureRegion, 32 * i, 32 * 0, 32, 32));
        }
        animation = new Animation(0.1f, keyFrames, PlayMode.LOOP_PINGPONG);
        animationComponent.putAnimation(AnimalComponent.MOVE_UP, animation);
        
        keyFrames.clear();
        
        // Move left
        for(int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(animalTextureRegion, 32 * i, 32 * 1, 32, 32));
        }
        animation = new Animation(0.1f, keyFrames, PlayMode.LOOP_PINGPONG);
        animationComponent.putAnimation(AnimalComponent.MOVE_UP, animation);
        
        keyFrames.clear();
        
        // Move right
        for(int i = 0; i < 3; i++) {
            keyFrames.add(new TextureRegion(animalTextureRegion, 32 * i, 32 * 2, 32, 32));
        }
        animation = new Animation(0.1f, keyFrames, PlayMode.LOOP_PINGPONG);
        animationComponent.putAnimation(AnimalComponent.MOVE_UP, animation);
        
        Entity entity = new Entity();
        entity.add(new AnimalComponent(farm, kind));
        entity.add(new RigidBodyComponent(body));
        entity.add(new TransformComponent());
        entity.add(new StateComponent(AnimalComponent.IDLE));
        entity.add(new RendererComponent(textureRegion, 1f, 1f));
        entity.add(animationComponent);
        
        engine.addEntity(entity);
        body.setUserData(entity);
    }

}
